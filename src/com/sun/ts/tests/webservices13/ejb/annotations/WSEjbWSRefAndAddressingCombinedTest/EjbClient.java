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

package com.sun.ts.tests.webservices13.ejb.annotations.WSEjbWSRefAndAddressingCombinedTest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.xml.ws.*;
import javax.xml.ws.soap.*;
import javax.ejb.EJBException;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.SessionContext;
import javax.annotation.Resource;
import javax.annotation.PostConstruct;

import com.sun.ts.tests.jaxws.common.*;

@Stateless(name = "WSEjbWSRefAndAddressingCombinedTestClntBean")
@Remote({ EjbClientIF.class })
public class EjbClient implements EjbClientIF {

  private static final boolean debug = false;

  @Addressing
  @WebServiceRef(name = "service/wsejbwsrefandaddrcombtestdefaultechoport", type = Echo.class, value = EchoService.class)
  Echo defaultEchoPort = null;

  @Addressing(enabled = true)
  @WebServiceRef(name = "service/wsejbwsrefandaddrcombtestenabledechoport", type = Echo.class, value = EchoService.class)
  Echo enabledEchoPort = null;

  @Addressing(enabled = true, required = true)
  @WebServiceRef(name = "service/wsejbwsrefandaddrcombtestrequiredechoport", type = Echo.class, value = EchoService.class)
  Echo requiredEchoPort = null;

  @Addressing(enabled = false)
  @WebServiceRef(name = "service/wsejbwsrefandaddrcombtestdisabledechoport", type = Echo.class, value = EchoService.class)
  Echo disabledEchoPort = null;

  @WebServiceRef(name = "service/wsejbwsrefandaddrcombtestservice")
  EchoService service = null;

  @PostConstruct
  public void postConstruct() {
    System.out.println("EjbClient:postConstruct()");
    System.out.println("EjbClient DEBUG: service=" + service);
    System.out.println("EjbClient DEBUG: defaultEchoPort=" + defaultEchoPort);
    System.out.println("EjbClient DEBUG: enabledEchoPort=" + enabledEchoPort);
    System.out.println("EjbClient DEBUG: requiredEchoPort=" + requiredEchoPort);
    System.out.println("EjbClient DEBUG: disabledEchoPort=" + disabledEchoPort);
    if (service == null || defaultEchoPort == null || enabledEchoPort == null
        || requiredEchoPort == null || disabledEchoPort == null) {
      throw new EJBException("postConstruct failed: injection failure");
    }
  }

  public void init(Properties p) {
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
    if (testName.equals("VerifyAddrHeadersExistForRequiredEchoPort"))
      return VerifyAddrHeadersExistForRequiredEchoPort();
    else if (testName.equals("VerifyAddrHeadersDoNotExistForDisabledEchoPort"))
      return VerifyAddrHeadersDoNotExistForDisabledEchoPort();
    else
      return VerifyAddrHeadersMayExistForEnabledEchoPort();
  }

  private String getTargetEndpointAddress(Object stub) throws Exception {
    BindingProvider bindingprovider = (BindingProvider) stub;
    java.util.Map<String, Object> context = bindingprovider.getRequestContext();
    String url = (String) context
        .get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
    return url;
  }

  private boolean VerifyAddrHeadersExistForRequiredEchoPort() {
    try {
      TestUtil.logMsg("EjbClient invoking EchoService echo() method");
      TestUtil.logMsg(
          "Addressing headers MUST be present on the SOAPRequest/SOAPResponse");
      TestUtil.logMsg("VerifyAddrHeadersExistForRequiredEchoPort");
      Holder<String> testName = new Holder(
          "VerifyAddrHeadersExistForRequiredEchoPort");
      requiredEchoPort.echo("Echo from EjbClient on requiredEchoPort",
          testName);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private boolean VerifyAddrHeadersDoNotExistForDisabledEchoPort() {
    try {
      TestUtil.logMsg("EjbClient invoking EchoService echo() method");
      TestUtil.logMsg(
          "Addressing headers MUST NOT be present on the SOAPRequest/SOAPResponse");
      TestUtil.logMsg("VerifyAddrHeadersDoNotExistForDisabledEchoPort");
      Holder<String> testName = new Holder(
          "VerifyAddrHeadersDoNotExistForDisabledEchoPort");
      disabledEchoPort.echo("Echo from EjbClient on disabledEchoPort",
          testName);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private boolean VerifyAddrHeadersMayExistForEnabledEchoPort() {
    try {
      TestUtil.logMsg("EjbClient invoking EchoService echo() method");
      TestUtil.logMsg(
          "Addressing headers MAY be present on the SOAPRequest/SOAPResponse");
      TestUtil.logMsg("VerifyAddrHeadersMayExistForEnabledEchoPort");
      Holder<String> testName = new Holder(
          "VerifyAddrHeadersMayExistForEnabledEchoPort");
      enabledEchoPort.echo("Echo from EjbClient on enabledEchoPort", testName);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
