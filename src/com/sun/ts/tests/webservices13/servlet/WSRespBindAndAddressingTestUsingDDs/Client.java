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
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxws.common.*;
import com.sun.ts.tests.jaxws.wsa.common.W3CAddressingConstants;
import com.sun.ts.tests.jaxws.wsa.common.WsaSOAPUtils;

import com.sun.javatest.Status;

import javax.ejb.EJB;
import javax.xml.ws.*;
import javax.xml.ws.soap.*;
import javax.xml.namespace.QName;
import javax.naming.InitialContext;
import java.net.*;
import java.util.Properties;
import java.util.Iterator;

public class Client extends EETest {

  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private TSURL ctsurl = new TSURL();

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private URL url = null;

  private URLConnection urlConn = null;

  private String SERVLET = "/WSRespBindAndAddressingTestUsingDDsClnt_web/ServletTest";

  /*************************************************************************************************
   * Table to cover Addressing/RespectBinding annotation combinations.
   * 
   * These test scenarios exist for covering the various combinations of
   * Addressing/RespectBinding annotations in JSR 109. This test case is
   * borrowed from the jaxws respectbindingfeature test case which uses JAXWS
   * API's to control these same combinations. This test case demonstrates that
   * these combinations can now be all annotation driven via JSR 109.
   * 
   * There exist the following 20+ scenarios for Addressing/RespectBinding
   * combinations (only 5 test cases are valid):
   * 
   * ------------------- ------------------- ---------------------
   * --------------- Client (Addressing) Server (Addressing)
   * RespectBindingFeature Expected Result -------------------
   * ------------------- --------------------- --------------- 1).
   * Enabled/NotRequired Enabled/NotRequired N/A N/A-not TCK test 2).
   * Enabled/Required Enabled/NotRequired N/A N/A-not TCK test 3). NotEnabled
   * Enabled/NotRequired N/A N/A-not TCK test 4a) Enabled/NotRequired
   * Enabled/Required S-Enabled/C-Enabled Expect No Error 4b)
   * Enabled/NotRequired Enabled/Required S-Enabled/C-Disabled N/A-not TCK test
   * 4c) Enabled/NotRequired Enabled/Required S-Disabled/C-Enabled N/A-not TCK
   * test 4d) Enabled/NotRequired Enabled/Required S-Disabled/C-Disabled N/A-not
   * TCK test 5a) Enabled/Required Enabled/Required S-Enabled/C-Enabled Expect
   * No Error 5b) Enabled/Required Enabled/Required S-Enabled/C-Disabled N/A-not
   * TCK test 5c) Enabled/Required Enabled/Required S-Disabled/C-Enabled N/A-not
   * TCK test 5d) Enabled/Required Enabled/Required S-Disabled/C-Disabled
   * N/A-not TCK test 6a) NotEnabled Enabled/Required S-Enabled/C-Enabled Expect
   * Exception 6b) NotEnabled Enabled/Required S-Enabled/C-Disabled N/A-not TCK
   * test 6c) NotEnabled Enabled/Required S-Disabled/C-Enabled N/A-not TCK test
   * 6d) NotEnabled Enabled/Required S-Disabled/C-Disabled N/A-not TCK test 7a)
   * Enabled/NotRequired NotEnabled S-Enabled/C-Enabled Expect No Error 7b)
   * Enabled/NotRequired NotEnabled S-Enabled/C-Disabled N/A-not TCK test 7c)
   * Enabled/NotRequired NotEnabled S-Disabled/C-Enabled N/A-not TCK test 7d)
   * Enabled/NotRequired NotEnabled S-Disabled/C-Disabled N/A-not TCK test 8a)
   * Enabled/Required NotEnabled S-Enabled/C-Enabled Expect Exception 8b)
   * Enabled/Required NotEnabled S-Enabled/C-Disabled N/A-not TCK test 8c)
   * Enabled/Required NotEnabled S-Disabled/C-Enabled N/A-not TCK test 8d)
   * Enabled/Required NotEnabled S-Disabled/C-Disabled N/A-not TCK test
   * 
   * From the last column you can see that the only valid test cases are:
   * 4a,5a,6a,7a,8a. According to the JAXWS Javadoc API the behavior of
   * RespectBinding(enabled=false) is implementation specific, so we cannot test
   * for any combo where RespectBinding is not enabled.
   * 
   * test scenarios 4a, 5a, 6a use Echo port test scenarios 7a, 8a use Echo2
   * port
   * 
   * where Echo port is configured via- WSDL: <wsam:Addressing/> // Addressing
   * enabled/required in WSDL
   * EchoImpl.java: @BindingType(value=SOAPBinding.SOAP11HTTP_BINDING) @RespectBinding(enabled=true)
   * // Impl enables RespectBinding
   * 
   * where Echo2 port is configured via- WSDL: <wsam:Addressing/> // Addressing
   * enabled/required in WSDL Echo2Impl.java: @Addressing(enabled=false) // Impl
   * turns off Addressing @RespectBinding(enabled=true) // Impl enables
   * RespectBinding
   * 
   *************************************************************************************************/

  // Port variables used by test
  Echo port4a = null;

  Echo port5a = null;

  Echo port6a = null;

  Echo2 port7a = null;

  Echo2 port8a = null;

  @EJB(name = "ejb/WSRespBindAndAddressingTestUsingDDsClntBean")
  static EjbClientIF ejbclient;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.setup_props: webServerHost; webServerPort;
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    boolean pass = true;
    try {
      hostname = p.getProperty(WEBSERVERHOSTPROP);
      if (hostname == null)
        pass = false;
      else if (hostname.equals(""))
        pass = false;
      try {
        portnum = Integer.parseInt(p.getProperty(WEBSERVERPORTPROP));
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        pass = false;
      }

      InitialContext ctx = new InitialContext();
      TestUtil.logMsg(
          "JNDI lookup java:comp/env/service/wsrespbindandaddrtestusingddsport4a");
      port4a = (Echo) ctx
          .lookup("java:comp/env/service/wsrespbindandaddrtestusingddsport4a");
      TestUtil.logMsg(
          "JNDI lookup java:comp/env/service/wsrespbindandaddrtestusingddsport5a");
      port5a = (Echo) ctx
          .lookup("java:comp/env/service/wsrespbindandaddrtestusingddsport5a");
      TestUtil.logMsg(
          "JNDI lookup java:comp/env/service/wsrespbindandaddrtestusingddsport6a");
      port6a = (Echo) ctx
          .lookup("java:comp/env/service/wsrespbindandaddrtestusingddsport6a");
      TestUtil.logMsg(
          "JNDI lookup java:comp/env/service/wsrespbindandaddrtestusingddsport7a");
      port7a = (Echo2) ctx
          .lookup("java:comp/env/service/wsrespbindandaddrtestusingddsport7a");
      TestUtil.logMsg(
          "JNDI lookup java:comp/env/service/wsrespbindandaddrtestusingddsport8a");
      port8a = (Echo2) ctx
          .lookup("java:comp/env/service/wsrespbindandaddrtestusingddsport8a");

    } catch (Exception e) {
      throw new Fault("setup failed:", e);
    }

    TestUtil.logMsg("Appclient DEBUG: ejbclient=" + ejbclient);
    TestUtil.logMsg("Appclient DEBUG: port4a=" + port4a);
    TestUtil.logMsg("Appclient DEBUG: port5a=" + port5a);
    TestUtil.logMsg("Appclient DEBUG: port6a=" + port6a);
    TestUtil.logMsg("Appclient DEBUG: port7a=" + port7a);
    TestUtil.logMsg("Appclient DEBUG: port8a=" + port8a);

    if (port4a == null || port5a == null || port6a == null || port7a == null
        || port8a == null || ejbclient == null) {
      throw new Fault("setup failed: injection or JNDI lookup failure");
    }

    if (!pass) {
      TestUtil.logErr(
          "Please specify host & port of web server " + "in config properties: "
              + WEBSERVERHOSTPROP + ", " + WEBSERVERPORTPROP);
      throw new Fault("setup failed:");
    }
    ejbclient.init(p);
    TestUtil.logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    TestUtil.logMsg("cleanup ok");
  }

  /*
   * @testName: afCltEnabledREQSvrEnabledREQrbfSvrEnabledCltEnabledTest
   *
   * @assertion_ids: WS4EE:SPEC:3002; WS4EE:SPEC:3003; WS4EE:SPEC:3005;
   * WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:4015; WS4EE:SPEC:4020;
   * WS4EE:SPEC:4021;
   *
   * @test_Strategy: Test RespectBinding Feature. Addressing Client
   * Enabled/Required, Server Enabled/Required; RespectBinding Server Enabled,
   * Client Enabled. Addressing headers MUST be present on SOAPRequest and
   * SOAPResponse.
   */
  public void afCltEnabledREQSvrEnabledREQrbfSvrEnabledCltEnabledTest()
      throws Fault {
    boolean pass = true;

    TestUtil.logMsg("afCltEnabledREQSvrEnabledREQrbfSvrEnabledCltEnabledTest");
    try {
      TestUtil.logMsg("-------------------------------");
      TestUtil.logMsg("Test appclient invocation .....");
      TestUtil.logMsg("-------------------------------");
      TestUtil.logMsg("AppClient invoking EchoService echo() method");
      TestUtil.logMsg("Addressing headers MUST be present on the SOAPRequest");
      TestUtil
          .logMsg("afCltEnabledREQSvrEnabledREQrbfSvrEnabledCltEnabledTest");
      Holder<String> testName = new Holder(
          "afCltEnabledREQSvrEnabledREQrbfSvrEnabledCltEnabledTest");
      port5a.echo("Echo from AppClient on port5a", testName);
      TestUtil.logMsg("Addressing Headers Exist (Expected)");
    } catch (Exception e) {
      TestUtil.logErr("Addressing Headers DO NOT Exist (Unexpected)");
      pass = false;
    }
    try {
      TestUtil.logMsg("-----------------------------------");
      TestUtil.logMsg("Test servletclient invocation .....");
      TestUtil.logMsg("-----------------------------------");
      url = ctsurl.getURL("http", hostname, portnum, SERVLET);
      TestUtil.logMsg("Servlet URL: " + url);
      props.setProperty("TEST",
          "afCltEnabledREQSvrEnabledREQrbfSvrEnabledCltEnabledTest");
      urlConn = TestUtil.sendPostData(props, url);
      Properties p = TestUtil.getResponseProperties(urlConn);
      String passStr = p.getProperty("TESTRESULT");
      if (passStr.equals("fail")) {
        pass = false;
        TestUtil.logErr("Addressing Headers DO NOT Exist (Unexpected)");
      } else
        TestUtil.logMsg("Addressing Headers Exist (Expected)");
    } catch (Exception e) {
      TestUtil.logErr("Addressing Headers DO NOT Exist (Unexpected)");
      pass = false;
    }
    try {
      TestUtil.logMsg("-------------------------------");
      TestUtil.logMsg("Test ejbclient invocation .....");
      TestUtil.logMsg("-------------------------------");
      String testName = "afCltEnabledREQSvrEnabledREQrbfSvrEnabledCltEnabledTest";
      pass = ejbclient.echo("Echo from EjbClient on port5a", testName);
      if (!pass)
        TestUtil.logErr("Addressing Headers DO NOT Exist (Unexpected)");
      else
        TestUtil.logMsg("Addressing Headers Exist (Expected)");
    } catch (Exception e) {
      TestUtil.logErr("Addressing Headers DO NOT Exist (Unexpected)");
      pass = false;
    }
    if (!pass)
      throw new Fault(
          "afCltEnabledREQSvrEnabledREQrbfSvrEnabledCltEnabledTest failed");
  }

  /*
   * @testName: afCltNotEnabledSvrEnabledREQrbfSvrEnabledCltEnabledTest
   *
   * @assertion_ids: WS4EE:SPEC:3002; WS4EE:SPEC:3003; WS4EE:SPEC:3005;
   * WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:4015; WS4EE:SPEC:4020;
   * WS4EE:SPEC:4021;
   *
   * @test_Strategy: Test RespectBinding Feature. Addressing Client NotEnabled,
   * Server Enabled/Required; RespectBinding Server Enabled, Client Enabled.
   * This scenario MUST throw back a SOAP Fault. Make sure the SOAP Fault has
   * the correct information in it. The SOAP Fault faultcode must be:
   * MessageAddressingHeaderRequired.
   */
  public void afCltNotEnabledSvrEnabledREQrbfSvrEnabledCltEnabledTest()
      throws Fault {
    boolean pass = true;

    TestUtil.logMsg("afCltNotEnabledSvrEnabledREQrbfSvrEnabledCltEnabledTest");
    try {
      TestUtil.logMsg("-------------------------------");
      TestUtil.logMsg("Test appclient invocation .....");
      TestUtil.logMsg("-------------------------------");
      TestUtil.logMsg("AppClient invoking EchoService echo() method");
      TestUtil.logMsg("Expect a SOAPFaultException to be thrown back");
      TestUtil
          .logMsg("afCltNotEnabledSvrEnabledREQrbfSvrEnabledCltEnabledTest");
      Holder<String> testName = new Holder(
          "afCltNotEnabledSvrEnabledREQrbfSvrEnabledCltEnabledTest");
      port6a.echo("Echo from AppClient on port6a", testName);
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
      pass = false;
    }
    try {
      TestUtil.logMsg("-----------------------------------");
      TestUtil.logMsg("Test servletclient invocation .....");
      TestUtil.logMsg("-----------------------------------");
      url = ctsurl.getURL("http", hostname, portnum, SERVLET);
      TestUtil.logMsg("Servlet URL: " + url);
      props.setProperty("TEST",
          "afCltNotEnabledSvrEnabledREQrbfSvrEnabledCltEnabledTest");
      urlConn = TestUtil.sendPostData(props, url);
      Properties p = TestUtil.getResponseProperties(urlConn);
      String passStr = p.getProperty("TESTRESULT");
      if (passStr.equals("fail")) {
        pass = false;
        TestUtil.logErr(
            "afCltNotEnabledSvrEnabledREQrbfSvrEnabledCltEnabledTest (Unexpected)");
      } else
        TestUtil.logMsg(
            "afCltNotEnabledSvrEnabledREQrbfSvrEnabledCltEnabledTest (Expected)");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
    }
    try {
      TestUtil.logMsg("-------------------------------");
      TestUtil.logMsg("Test ejbclient invocation .....");
      TestUtil.logMsg("-------------------------------");
      String testName = "afCltNotEnabledSvrEnabledREQrbfSvrEnabledCltEnabledTest";
      pass = ejbclient.echo("Echo from EjbClient on port6a", testName);
      if (!pass)
        TestUtil.logErr(
            "afCltNotEnabledSvrEnabledREQrbfSvrEnabledCltEnabledTest (Unexpected)");
      else
        TestUtil.logMsg(
            "afCltNotEnabledSvrEnabledREQrbfSvrEnabledCltEnabledTest (Expected)");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      pass = false;
    }
    if (!pass)
      throw new Fault(
          "afCltNotEnabledSvrEnabledREQrbfSvrEnabledCltEnabledTest failed");
  }

  /*
   * @testName: afCltEnabledNotREQSvrNotEnabledrbfSvrEnabledCltEnabledTest
   *
   * @assertion_ids: WS4EE:SPEC:3002; WS4EE:SPEC:3003; WS4EE:SPEC:3005;
   * WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:4015; WS4EE:SPEC:4020;
   * WS4EE:SPEC:4021;
   *
   * @test_Strategy: Test RespectBinding Feature. Addressing Client
   * Enabled/NotRequired, Server NotEnabled; RespectBinding Server Enabled,
   * Client Enabled. Addressing headers MAY be present on SOAPRequest but MUST
   * NOT be present on SOAPResponse.
   */
  public void afCltEnabledNotREQSvrNotEnabledrbfSvrEnabledCltEnabledTest()
      throws Fault {
    boolean pass = true;

    TestUtil
        .logMsg("afCltEnabledNotREQSvrNotEnabledrbfSvrEnabledCltEnabledTest");
    try {
      TestUtil.logMsg("-------------------------------");
      TestUtil.logMsg("Test appclient invocation .....");
      TestUtil.logMsg("-------------------------------");
      TestUtil.logMsg("AppClient invoking EchoService echo() method");
      TestUtil.logMsg(
          "Addressing headers MAY be present on the SOAPRequest but MUST NOT be present on SOAPResponse");
      TestUtil
          .logMsg("afCltEnabledNotREQSvrNotEnabledrbfSvrEnabledCltEnabledTest");
      Holder<String> testName = new Holder(
          "afCltEnabledNotREQSvrNotEnabledrbfSvrEnabledCltEnabledTest");
      port7a.echo("Echo from AppClient on port7a", testName);
      TestUtil
          .logMsg("Addressing Headers DO NOT EXIST in SOAPResponse (Expected)");
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.logErr("Addressing Headers EXIST in SOAPResponse (Unexpected)");
      pass = false;
    }
    try {
      TestUtil.logMsg("-----------------------------------");
      TestUtil.logMsg("Test servletclient invocation .....");
      TestUtil.logMsg("-----------------------------------");
      url = ctsurl.getURL("http", hostname, portnum, SERVLET);
      TestUtil.logMsg("Servlet URL: " + url);
      props.setProperty("TEST",
          "afCltEnabledNotREQSvrNotEnabledrbfSvrEnabledCltEnabledTest");
      urlConn = TestUtil.sendPostData(props, url);
      Properties p = TestUtil.getResponseProperties(urlConn);
      String passStr = p.getProperty("TESTRESULT");
      if (passStr.equals("fail")) {
        pass = false;
        TestUtil
            .logErr("Addressing Headers EXIST in SOAPResponse (Unexpected)");
      } else
        TestUtil.logMsg(
            "Addressing Headers DO NOT EXIST in SOAPResponse (Expected)");
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.logErr("Addressing Headers EXIST in SOAPResponse (Unexpected)");
      pass = false;
    }
    try {
      TestUtil.logMsg("-------------------------------");
      TestUtil.logMsg("Test ejbclient invocation .....");
      TestUtil.logMsg("-------------------------------");
      String testName = "afCltEnabledNotREQSvrNotEnabledrbfSvrEnabledCltEnabledTest";
      boolean passEjb = ejbclient.echo("Echo from EjbClient on port7a",
          testName);
      if (!passEjb) {
        pass = false;
        TestUtil
            .logErr("Addressing Headers EXIST in SOAPResponse (Unexpected)");
      } else
        TestUtil.logMsg(
            "Addressing Headers DO NOT EXIST in SOAPResponse (Expected)");
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
      TestUtil.logErr("Addressing Headers EXIST in SOAPResponse (Unexpected)");
      pass = false;
    }
    if (!pass)
      throw new Fault(
          "afCltEnabledNotREQSvrNotEnabledrbfSvrEnabledCltEnabledTest failed");
  }

  /*
   * @testName: afCltEnabledREQSvrNotEnabledrbfSvrEnabledCltEnabledTest
   *
   * @assertion_ids: WS4EE:SPEC:3002; WS4EE:SPEC:3003; WS4EE:SPEC:3005;
   * WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:4015; WS4EE:SPEC:4020;
   * WS4EE:SPEC:4021;
   *
   * @test_Strategy: Test RespectBinding Feature. Addressing Client
   * Enabled/Required, Server NotEnabled; RespectBinding Server Enabled, Client
   * Enabled. This scenario MUST throw back a WebServiceException.
   */
  public void afCltEnabledREQSvrNotEnabledrbfSvrEnabledCltEnabledTest()
      throws Fault {
    boolean pass = true;

    TestUtil.logMsg("afCltEnabledREQSvrNotEnabledrbfSvrEnabledCltEnabledTest");
    try {
      TestUtil.logMsg("-------------------------------");
      TestUtil.logMsg("Test appclient invocation .....");
      TestUtil.logMsg("-------------------------------");
      TestUtil.logMsg("AppClient invoking EchoService echo() method");
      TestUtil.logMsg("Expect a WebServiceException to be thrown back");
      TestUtil
          .logMsg("afCltEnabledREQSvrNotEnabledrbfSvrEnabledCltEnabledTest");
      Holder<String> testName = new Holder(
          "afCltEnabledREQSvrNotEnabledrbfSvrEnabledCltEnabledTest");
      port8a.echo("Echo from AppClient on port8a", testName);
      TestUtil.logErr("WebServiceException was not thrown back");
      pass = false;
    } catch (WebServiceException e) {
      TestUtil.logMsg("Caught expected WebServiceException: " + e.getMessage());
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      pass = false;
    }
    try {
      TestUtil.logMsg("-----------------------------------");
      TestUtil.logMsg("Test servletclient invocation .....");
      TestUtil.logMsg("-----------------------------------");
      url = ctsurl.getURL("http", hostname, portnum, SERVLET);
      TestUtil.logMsg("Servlet URL: " + url);
      props.setProperty("TEST",
          "afCltEnabledREQSvrNotEnabledrbfSvrEnabledCltEnabledTest");
      urlConn = TestUtil.sendPostData(props, url);
      Properties p = TestUtil.getResponseProperties(urlConn);
      String passStr = p.getProperty("TESTRESULT");
      if (passStr.equals("fail")) {
        pass = false;
        TestUtil.logErr(
            "afCltEnabledREQSvrNotEnabledrbfSvrEnabledCltEnabledTest (Unexpected)");
      } else
        TestUtil.logMsg(
            "afCltEnabledREQSvrNotEnabledrbfSvrEnabledCltEnabledTest (Expected)");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      pass = false;
    }
    try {
      TestUtil.logMsg("-------------------------------");
      TestUtil.logMsg("Test ejbclient invocation .....");
      TestUtil.logMsg("-------------------------------");
      String testName = "afCltEnabledREQSvrNotEnabledrbfSvrEnabledCltEnabledTest";
      pass = ejbclient.echo("Echo from EjbClient on port8a", testName);
      if (!pass)
        TestUtil.logErr(
            "afCltEnabledREQSvrNotEnabledrbfSvrEnabledCltEnabledTest (Unexpected)");
      else
        TestUtil.logMsg(
            "afCltEnabledREQSvrNotEnabledrbfSvrEnabledCltEnabledTest (Expected)");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      pass = false;
    }
    if (!pass)
      throw new Fault(
          "afCltEnabledREQSvrNotEnabledrbfSvrEnabledCltEnabledTest failed");
  }
}
