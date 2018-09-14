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

package com.sun.ts.tests.webservices13.servlet.WSRespBindAndAddressingTestUsingDDs;

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
import javax.naming.InitialContext;

import com.sun.ts.tests.jaxws.common.*;
import com.sun.ts.tests.jaxws.wsa.common.W3CAddressingConstants;
import com.sun.ts.tests.jaxws.wsa.common.WsaSOAPUtils;

@Stateless(name = "WSRespBindAndAddressingTestUsingDDsClntBean")
@Remote({ EjbClientIF.class })
public class EjbClient implements EjbClientIF {

  private Properties harnessProps = null;

  private static final boolean debug = false;

  // Port variables used by test
  Echo port4a = null;

  Echo port5a = null;

  Echo port6a = null;

  Echo2 port7a = null;

  Echo2 port8a = null;

  @PostConstruct
  public void postConstruct() {
    try {
      System.out.println("EjbClient:postConstruct()");

      InitialContext ctx = new InitialContext();
      System.out.println(
          "JNDI lookup java:comp/env/service/wsrespbindandaddrtestusingddsport4a");
      port4a = (Echo) ctx
          .lookup("java:comp/env/service/wsrespbindandaddrtestusingddsport4a");
      System.out.println(
          "JNDI lookup java:comp/env/service/wsrespbindandaddrtestusingddsport5a");
      port5a = (Echo) ctx
          .lookup("java:comp/env/service/wsrespbindandaddrtestusingddsport5a");
      System.out.println(
          "JNDI lookup java:comp/env/service/wsrespbindandaddrtestusingddsport6a");
      port6a = (Echo) ctx
          .lookup("java:comp/env/service/wsrespbindandaddrtestusingddsport6a");
      System.out.println(
          "JNDI lookup java:comp/env/service/wsrespbindandaddrtestusingddsport7a");
      port7a = (Echo2) ctx
          .lookup("java:comp/env/service/wsrespbindandaddrtestusingddsport7a");
      System.out.println(
          "JNDI lookup java:comp/env/service/wsrespbindandaddrtestusingddsport8a");
      port8a = (Echo2) ctx
          .lookup("java:comp/env/service/wsrespbindandaddrtestusingddsport8a");

    } catch (Exception e) {
      System.err.println("EjbClient:postConstruct() Exception: " + e);
      e.printStackTrace();
    }

    System.out.println("Ejbclient DEBUG: port4a=" + port4a);
    System.out.println("Ejbclient DEBUG: port5a=" + port5a);
    System.out.println("Ejbclient DEBUG: port6a=" + port6a);
    System.out.println("Ejbclient DEBUG: port7a=" + port7a);
    System.out.println("Ejbclient DEBUG: port8a=" + port8a);

    if (port4a == null || port5a == null || port6a == null || port7a == null
        || port8a == null) {
      throw new EJBException(
          "postConstruct failed: injection or JNDI lookup failure");
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
    if (testName
        .equals("afCltEnabledREQSvrEnabledREQrbfSvrEnabledCltEnabledTest"))
      return afCltEnabledREQSvrEnabledREQrbfSvrEnabledCltEnabledTest();
    else if (testName
        .equals("afCltNotEnabledSvrEnabledREQrbfSvrEnabledCltEnabledTest"))
      return afCltNotEnabledSvrEnabledREQrbfSvrEnabledCltEnabledTest();
    else if (testName
        .equals("afCltEnabledNotREQSvrNotEnabledrbfSvrEnabledCltEnabledTest"))
      return afCltEnabledNotREQSvrNotEnabledrbfSvrEnabledCltEnabledTest();
    else if (testName
        .equals("afCltEnabledREQSvrNotEnabledrbfSvrEnabledCltEnabledTest"))
      return afCltEnabledREQSvrNotEnabledrbfSvrEnabledCltEnabledTest();
    else
      return false;
  }

  private boolean afCltEnabledREQSvrEnabledREQrbfSvrEnabledCltEnabledTest() {
    try {
      TestUtil.logMsg("EjbClient invoking EchoService echo() method");
      TestUtil.logMsg(
          "Addressing headers MUST be present on the SOAPRequest and SOAPResponse");
      TestUtil
          .logMsg("afCltEnabledREQSvrEnabledREQrbfSvrEnabledCltEnabledTest");
      Holder<String> testName = new Holder(
          "afCltEnabledREQSvrEnabledREQrbfSvrEnabledCltEnabledTest");
      port5a.echo("Echo from EjbClient on port5a", testName);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private boolean afCltNotEnabledSvrEnabledREQrbfSvrEnabledCltEnabledTest() {
    boolean pass = true;
    try {
      TestUtil.logMsg("EjbClient invoking EchoService echo() method");
      TestUtil.logMsg("This scenario MUST throw back a SOAPFault");
      TestUtil
          .logMsg("afCltNotEnabledSvrEnabledREQrbfSvrEnabledCltEnabledTest()");
      Holder<String> testName = new Holder(
          "afCltNotEnabledSvrEnabledREQrbfSvrEnabledCltEnabledTest()");
      port6a.echo("Echo from EjbClient on port6a", testName);
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

  private boolean afCltEnabledNotREQSvrNotEnabledrbfSvrEnabledCltEnabledTest() {
    try {
      TestUtil.logMsg("EjbClient invoking EchoService echo() method");
      TestUtil.logMsg(
          "Addressing headers MAY be present on the SOAPRequest but MUST NOT be present on SOAPResponse");
      TestUtil
          .logMsg("afCltEnabledNotREQSvrNotEnabledrbfSvrEnabledCltEnabledTest");
      Holder<String> testName = new Holder(
          "afCltEnabledNotREQSvrNotEnabledrbfSvrEnabledCltEnabledTest");
      port7a.echo("Echo from EjbClient on port7a", testName);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private boolean afCltEnabledREQSvrNotEnabledrbfSvrEnabledCltEnabledTest() {
    boolean pass = true;
    try {
      TestUtil.logMsg("EjbClient invoking EchoService echo() method");
      TestUtil.logMsg("This scenario MUST throw back a WebServiceException");
      TestUtil
          .logMsg("afCltEnabledREQSvrNotEnabledrbfSvrEnabledCltEnabledTest()");
      Holder<String> testName = new Holder(
          "afCltEnabledREQSvrNotEnabledrbfSvrEnabledCltEnabledTest()");
      port8a.echo("Echo from EjbClient on port8a", testName);
      TestUtil.logErr("WebServiceException was not thrown back");
      pass = false;
    } catch (WebServiceException e) {
      TestUtil.logMsg("Caught expected WebServiceException: " + e.getMessage());
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }
}
