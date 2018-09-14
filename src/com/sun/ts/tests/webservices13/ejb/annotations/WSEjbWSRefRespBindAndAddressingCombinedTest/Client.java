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

  private String SERVLET = "/WSEjbWSRefRespBindAndAddressingCombinedTestClnt_web/ServletTest";

  @Addressing
  @RespectBinding(enabled = true)
  @WebServiceRef(name = "service/wsejbwsrefrespbindandaddrcombtestport1", type = Echo.class, value = EchoService.class)
  static Echo port1 = null;

  @Addressing(enabled = true, required = true)
  @RespectBinding(enabled = true)
  @WebServiceRef(name = "service/wsejbwsrefrespbindandaddrcombtestport2", type = Echo.class, value = EchoService.class)
  static Echo port2 = null;

  @Addressing(enabled = false)
  @RespectBinding(enabled = true)
  @WebServiceRef(name = "service/wsejbwsrefrespbindandaddrcombtestport3", type = Echo.class, value = EchoService.class)
  static Echo port3 = null;

  @WebServiceRef(name = "service/wsejbwsrefrespbindandaddrcombtestservice")
  static EchoService service = null;

  @EJB(name = "ejb/WSEjbWSRefRespBindAndAddressingCombinedTestClntBean")
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

      TestUtil.logMsg("Appclient DEBUG: ejbclient=" + ejbclient);
      TestUtil.logMsg("Appclient DEBUG: service=" + service);
      TestUtil.logMsg("Appclient DEBUG: port1=" + port1);
      TestUtil.logMsg("Appclient DEBUG: port2=" + port2);
      TestUtil.logMsg("Appclient DEBUG: port3=" + port3);

    } catch (Exception e) {
      throw new Fault("setup failed:", e);
    }
    if (service == null || port1 == null || port2 == null || port3 == null
        || ejbclient == null) {
      throw new Fault("setup failed: injection failure");
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
   * @testName: VerifyAddrHeadersExistForEnabledRequiredPort
   *
   * @assertion_ids: WS4EE:SPEC:3002; WS4EE:SPEC:3003; WS4EE:SPEC:3005;
   * WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:4004; WS4EE:SPEC:4015;
   *
   * @test_Strategy: Verify Addressing Headers MUST exist on both SOAP request
   * and SOAP response for appclient, servlet, and ejb containers. Addressing is
   * Enabled/Required on client with RespectBinding equal true and the wsdl
   * policy is enabled=true,required=true.
   */
  public void VerifyAddrHeadersExistForEnabledRequiredPort() throws Fault {
    boolean pass = true;

    TestUtil.logMsg("VerifyAddrHeadersExistForEnabledRequiredPort");
    try {
      TestUtil.logMsg("-------------------------------");
      TestUtil.logMsg("Test appclient invocation .....");
      TestUtil.logMsg("-------------------------------");
      TestUtil.logMsg("AppClient invoking EchoService echo() method");
      TestUtil.logMsg("Addressing headers MUST be present on the SOAPRequest");
      TestUtil.logMsg("VerifyAddrHeadersExistForEnabledRequiredPort");
      Holder<String> testName = new Holder(
          "VerifyAddrHeadersExistForEnabledRequiredPort");
      port1.echo("Echo from AppClient on port1", testName);
      TestUtil.logMsg("Addressing Headers Exist (Expected)");
    } catch (Exception e) {
      // TestUtil.printStackTrace(e);
      TestUtil.logErr("Addressing Headers DO NOT Exist (Unexpected)");
      pass = false;
    }
    try {
      TestUtil.logMsg("-----------------------------------");
      TestUtil.logMsg("Test servletclient invocation .....");
      TestUtil.logMsg("-----------------------------------");
      url = ctsurl.getURL("http", hostname, portnum, SERVLET);
      TestUtil.logMsg("Servlet URL: " + url);
      props.setProperty("TEST", "VerifyAddrHeadersExistForEnabledRequiredPort");
      urlConn = TestUtil.sendPostData(props, url);
      Properties p = TestUtil.getResponseProperties(urlConn);
      String passStr = p.getProperty("TESTRESULT");
      if (passStr.equals("fail")) {
        pass = false;
        TestUtil.logErr("Addressing Headers DO NOT Exist (Unexpected)");
      } else
        TestUtil.logMsg("Addressing Headers Exist (Expected)");
    } catch (Exception e) {
      // TestUtil.printStackTrace(e);
      TestUtil.logErr("Addressing Headers DO NOT Exist (Unexpected)");
      pass = false;
    }
    try {
      TestUtil.logMsg("-------------------------------");
      TestUtil.logMsg("Test ejbclient invocation .....");
      TestUtil.logMsg("-------------------------------");
      String testName = "VerifyAddrHeadersExistForEnabledRequiredPort";
      boolean passEjb = ejbclient.echo("Echo from EjbClient on port1",
          testName);
      if (!passEjb) {
        TestUtil.logErr("Addressing Headers DO NOT Exist (Unexpected)");
        pass = false;
      } else
        TestUtil.logMsg("Addressing Headers Exist (Expected)");
    } catch (Exception e) {
      // TestUtil.printStackTrace(e);
      TestUtil.logErr("Addressing Headers DO NOT Exist (Unexpected)");
      pass = false;
    }
    if (!pass)
      throw new Fault("VerifyAddrHeadersExistForEnabledRequiredPort failed");
  }

  /*
   * @testName: VerifyFaultConditionOnPort
   *
   * @assertion_ids: WS4EE:SPEC:3002; WS4EE:SPEC:3003; WS4EE:SPEC:3005;
   * WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:4004; WS4EE:SPEC:4015;
   *
   * @test_Strategy: Verify SOAPFaultException case for appclient, servlet, and
   * ejb containers. Addressing is Disabled on client with RespectBinding equal
   * true and the wsdl policy is enabled=true,required=true. Expect a
   * SOAPFaultException with MessageAddressingHeaderRequired fault code.
   */
  public void VerifyFaultConditionOnPort() throws Fault {
    boolean pass = true;

    TestUtil.logMsg("VerifyFaultConditionOnPort");
    try {
      TestUtil.logMsg("-------------------------------");
      TestUtil.logMsg("Test appclient invocation .....");
      TestUtil.logMsg("-------------------------------");
      TestUtil.logMsg("AppClient invoking EchoService echo() method");
      TestUtil
          .logMsg("Addressing headers MUST NOT be present on the SOAPRequest");
      TestUtil.logMsg("VerifyFaultConditionOnPort");
      Holder<String> testName = new Holder("VerifyFaultConditionOnPort");
      port3.echo("Echo from AppClient on port3", testName);
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
    try {
      TestUtil.logMsg("-----------------------------------");
      TestUtil.logMsg("Test servletclient invocation .....");
      TestUtil.logMsg("-----------------------------------");
      url = ctsurl.getURL("http", hostname, portnum, SERVLET);
      TestUtil.logMsg("Servlet URL: " + url);
      props.setProperty("TEST", "VerifyFaultConditionOnPort");
      urlConn = TestUtil.sendPostData(props, url);
      Properties p = TestUtil.getResponseProperties(urlConn);
      String passStr = p.getProperty("TESTRESULT");
      if (passStr.equals("fail")) {
        pass = false;
        TestUtil.logErr("VerifyFaultCondition (Unexpected)");
      } else
        TestUtil.logMsg("VerifyFaultCondition (Expected)");
    } catch (Exception e) {
      // TestUtil.printStackTrace(e);
      TestUtil.logErr("Caught Exception: " + e.getMessage());
      pass = false;
    }
    try {
      TestUtil.logMsg("-------------------------------");
      TestUtil.logMsg("Test ejbclient invocation .....");
      TestUtil.logMsg("-------------------------------");
      String testName = "VerifyFaultConditionOnPort";
      boolean passEjb = ejbclient.echo("Echo from EjbClient on port3",
          testName);
      if (!passEjb) {
        TestUtil.logErr("VerifyFaultCondition (Unexpected)");
        pass = false;
      } else
        TestUtil.logMsg("VerifyFaultCondition (Expected)");
    } catch (Exception e) {
      TestUtil.logErr("Caught Exception: " + e.getMessage());
      // TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault("VerifyFaultConditionOnPort failed");
  }
}
