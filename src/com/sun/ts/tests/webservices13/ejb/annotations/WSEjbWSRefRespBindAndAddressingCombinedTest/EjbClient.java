/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.ts.tests.webservices13.ejb.annotations.WSEjbWSRefRespBindAndAddressingCombinedTest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.xml.ws.*;
import javax.xml.ws.soap.*;
import javax.ejb.EJBException;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.ejb.SessionContext;
import javax.annotation.Resource;
import javax.annotation.PostConstruct;

import com.sun.ts.tests.jaxws.common.*;
import com.sun.ts.tests.jaxws.wsa.common.W3CAddressingConstants;
import com.sun.ts.tests.jaxws.wsa.common.WsaSOAPUtils;

@Stateful(name = "WSEjbWSRefRespBindAndAddressingCombinedTestClntBean")
@Remote({ EjbClientIF.class })
public class EjbClient implements EjbClientIF {

  private Properties harnessProps = null;

  private static final boolean debug = false;

  @Addressing
  @RespectBinding(enabled = true)
  @WebServiceRef(name = "service/wsejbwsrefrespbindandaddrcombtestport1", type = Echo.class, value = EchoService.class)
  Echo port1 = null;

  @Addressing(enabled = true, required = true)
  @RespectBinding(enabled = true)
  @WebServiceRef(name = "service/wsejbwsrefrespbindandaddrcombtestport2", type = Echo.class, value = EchoService.class)
  Echo port2 = null;

  @Addressing(enabled = false)
  @RespectBinding(enabled = true)
  @WebServiceRef(name = "service/wsejbwsrefrespbindandaddrcombtestport3", type = Echo.class, value = EchoService.class)
  Echo port3 = null;

  @WebServiceRef(name = "service/wsejbwsrefrespbindandaddrcombtestservice")
  EchoService service = null;

  @PostConstruct
  public void postConstruct() {
    System.out.println("EjbClient:postConstruct()");
    System.out.println("EjbClient DEBUG: service=" + service);
    System.out.println("EjbClient DEBUG: port1=" + port1);
    System.out.println("EjbClient DEBUG: port2=" + port2);
    System.out.println("EjbClient DEBUG: port3=" + port3);
    if (service == null || port2 == null || port1 == null || port3 == null) {
      throw new EJBException("postConstruct failed: injection failure");
    }
  }

  public void init(Properties p) {
    harnessProps = p;
    try {
      TestUtil.init(p);
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("init: failed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("init: failed");
    }
  }

  public boolean echo(String string, String testName) {
    if (testName.equals("VerifyAddrHeadersExistForEnabledRequiredPort"))
      return VerifyAddrHeadersExistForEnabledRequiredPort();
    else
      return VerifyFaultConditionOnPort();
  }

  private String getTargetEndpointAddress(Object stub) throws Exception {
    BindingProvider bindingprovider = (BindingProvider) stub;
    java.util.Map<String, Object> context = bindingprovider.getRequestContext();
    String url = (String) context
        .get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
    return url;
  }

  private boolean VerifyAddrHeadersExistForEnabledRequiredPort() {
    try {
      TestUtil.logMsg("EjbClient invoking EchoService echo() method");
      TestUtil.logMsg("Addressing headers MUST be present on the SOAPRequest");
      TestUtil.logMsg("VerifyAddrHeadersExistForEnabledRequiredPort");
      Holder<String> testName = new Holder(
          "VerifyAddrHeadersExistForEnabledRequiredPort");
      port2.echo("Echo from EjbClient on port2", testName);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private boolean VerifyFaultConditionOnPort() {
    boolean pass = true;
    try {
      TestUtil.logMsg("EjbClient invoking EchoService echo() method");
      TestUtil.logMsg("VerifyFaultConditionOnPort");
      Holder<String> testName = new Holder("VerifyFaultConditionOnPort");
      port3.echo("Echo from EjbClient on port3", testName);
      TestUtil.logErr("SOAPFaultException was not thrown back");
      pass = false;
    } catch (SOAPFaultException sfe) {
      TestUtil
          .logMsg("Caught expected SOAPFaultException: " + sfe.getMessage());
      try {
        TestUtil.logMsg("FaultCode=" + WsaSOAPUtils.getFaultCode(sfe));
        TestUtil.logMsg("FaultString=" + WsaSOAPUtils.getFaultString(sfe));
        if (WsaSOAPUtils.isMessageAddressingHeaderRequiredFaultCode(sfe)) {
          TestUtil.logMsg(
              "SOAPFault contains expected faultcode MessageAddressingHeaderRequired");
        } else {
          String faultcode = WsaSOAPUtils.getFaultCode(sfe);
          TestUtil.logErr("SOAPFault contains unexpected faultcode got: "
              + faultcode + ", expected: MessageAddressingHeaderRequired");
          pass = false;
        }
        if (WsaSOAPUtils.getFaultString(sfe) == null) {
          TestUtil
              .logErr("The faultstring element MUST EXIST for SOAP 1.1 Faults");
          pass = false;
        }
        if (WsaSOAPUtils.getFaultDetail(sfe) != null) {
          TestUtil.logErr("The faultdetail element MUST NOT EXIST for SOAP 1.1 "
              + "Faults related to header entries");
          pass = false;
        }
      } catch (Exception e2) {
        TestUtil.logErr("Caught unexpected exception: " + e2.getMessage());
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught Exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }
}
