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

package com.sun.ts.tests.webservices13.servlet.WSMTOMFeaturesTestUsingAnnotations;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import com.sun.ts.tests.jaxws.common.AttachmentHelper;
import com.sun.ts.tests.jaxws.common.JAXWS_Util;

import java.io.*;
import java.net.*;
import java.rmi.*;
import java.util.*;

import java.awt.Image;
import javax.xml.ws.*;
import javax.xml.ws.soap.*;
import javax.xml.namespace.QName;

import javax.xml.transform.stream.StreamSource;

import javax.ejb.EJB;

import com.sun.javatest.Status;

public class Client extends EETest {

  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String CTXROOT = "/WSMTOMFeaturesTestUsingAnnotations_web";

  private String SERVLET_CLIENT = "/WSMTOMFeaturesTestUsingAnnotationsClnt_web/ServletTest";

  private TSURL ctsurl = new TSURL();

  private URL url = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  private URLConnection urlConn = null;

  private Properties props = null;

  private URL docURL1 = null;

  private URL docURL2 = null;

  private URL docURL3 = null;

  private String sdocURL1 = null;

  private String sdocURL2 = null;

  private String sdocURL3 = null;

  String SDOC1 = "text.xml";

  String SDOC2 = "big.jpg";

  String SDOC3 = "small.jpg";

  private String ctxroot = null;

  // MTOM(true) on client/MTOM(true) on endpoint
  @MTOM(enabled = true)
  @WebServiceRef(name = "service/wsmtomfeaturestestusingannotationsport1_1", value = MTOMTestService.class)
  static MTOMTest1 port1_1 = null;

  // MTOM() on client/MTOM(true) on endpoint
  @MTOM()
  @WebServiceRef(name = "service/wsmtomfeaturestestusingannotationsport1_2", value = MTOMTestService.class)
  static MTOMTest1 port1_2 = null;

  // MTOM(true) on client/MTOM(false) on endpoint
  @MTOM(enabled = true)
  @WebServiceRef(name = "service/wsmtomfeaturestestusingannotationsport2", value = MTOMTestService.class)
  static MTOMTest2 port2 = null;

  // MTOM(true, 2000) on client/MTOM(true,2000) on endpoint
  @MTOM(enabled = true, threshold = 2000)
  @WebServiceRef(name = "service/wsmtomfeaturestestusingannotationsport3_1", value = MTOMTestService.class)
  static MTOMTest3 port3_1 = null;

  // MTOM(false, 2000) on client/MTOM(true,2000) on endpoint
  @MTOM(enabled = false, threshold = 2000)
  @WebServiceRef(name = "service/wsmtomfeaturestestusingannotationsport3_2", value = MTOMTestService.class)
  static MTOMTest3 port3_2 = null;

  // MTOM() on client/MTOM(true,2000) on endpoint
  @MTOM()
  @WebServiceRef(name = "service/wsmtomfeaturestestusingannotationsport3_3", value = MTOMTestService.class)
  static MTOMTest3 port3_3 = null;

  // MTOM(true, 2000) on client/MTOM(false,2000) on endpoint
  @MTOM(enabled = true, threshold = 2000)
  @WebServiceRef(name = "service/wsmtomfeaturestestusingannotationsport4", value = MTOMTestService.class)
  static MTOMTest4 port4 = null;

  @WebServiceRef(name = "service/wsmtomfeaturestestusingannotationsservice")
  static MTOMTestService service = null;

  @EJB(name = "ejb/WSMTOMFeaturesTestUsingAnnotationsClntBean")
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
    boolean pass = true;
    props = p;

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
      TestUtil.logMsg("AppClient DEBUG: ejbclient=" + ejbclient);
      TestUtil.logMsg("AppClient DEBUG: service=" + service);
      TestUtil.logMsg("AppClient DEBUG: port1_1=" + port1_1);
      TestUtil.logMsg("AppClient DEBUG: port1_2=" + port1_2);
      TestUtil.logMsg("AppClient DEBUG: port2=" + port2);
      TestUtil.logMsg("AppClient DEBUG: port3_1=" + port3_1);
      TestUtil.logMsg("AppClient DEBUG: port3_2=" + port3_2);
      TestUtil.logMsg("AppClient DEBUG: port3_3=" + port3_3);
      TestUtil.logMsg("AppClient DEBUG: port4=" + port4);

      TestUtil.logMsg("Endpoint Context Root:         " + CTXROOT);

      docURL1 = ctsurl.getURL(PROTOCOL, hostname, portnum,
          CTXROOT + "/" + SDOC1);
      docURL2 = ctsurl.getURL(PROTOCOL, hostname, portnum,
          CTXROOT + "/" + SDOC2);
      docURL3 = ctsurl.getURL(PROTOCOL, hostname, portnum,
          CTXROOT + "/" + SDOC3);
      sdocURL1 = docURL1.toString();
      sdocURL2 = docURL2.toString();
      sdocURL3 = docURL3.toString();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("setup failed:", e);
    }

    if (service == null || ejbclient == null || port1_1 == null
        || port1_2 == null || port2 == null || port3_1 == null
        || port3_2 == null || port3_3 == null || port4 == null) {
      throw new Fault("setup failed: injection failure");
    }

    if (!pass) {
      TestUtil.logErr(
          "Please specify host & port of web server " + "in config properties: "
              + WEBSERVERHOSTPROP + ", " + WEBSERVERPORTPROP);
      throw new Fault("setup failed:");
    }
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: ClientEnabledServerEnabledMTOMInTest
   *
   * @assertion_ids: WS4EE:SPEC:3002; WS4EE:SPEC:3003; WS4EE:SPEC:3005;
   * WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:4013; WS4EE:SPEC:4015;
   *
   * @test_Strategy: Test MTOM attachments on input where mtom is enabled in the
   * endpoint and the client.
   */
  public void ClientEnabledServerEnabledMTOMInTest() throws Fault {
    TestUtil.logMsg("ClientEnabledServerEnabledMTOMInTest");
    boolean pass = true;

    TestUtil.logMsg("----------------------------------");
    TestUtil.logMsg("Testing appclient client");
    TestUtil.logMsg("----------------------------------");
    try {
      DataType data = new DataType();
      data.setDocName(SDOC1);
      data.setDocUrl(docURL1.toString());
      StreamSource doc = AttachmentHelper.getSourceDoc(docURL1);
      data.setDoc(doc);
      TestUtil
          .logMsg("Send 1 document using MTOM via webservice method mtomIn()");
      TestUtil.logMsg("Document to send: [" + SDOC1 + "]");
      String result = port1_1.mtomIn(data);
      if (!result.equals("")) {
        TestUtil.logErr("Appclient failed");
        TestUtil.logErr("result=" + result);
        pass = false;
      } else {
        TestUtil.logMsg("Appclient passed");
      }
    } catch (Exception e) {
      TestUtil.logErr("Appclient failed");
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!execute_servlet_client("ClientEnabledServerEnabledMTOMInTest", SDOC1,
        sdocURL1)) {
      pass = false;
    }
    if (!execute_ejb_client("ClientEnabledServerEnabledMTOMInTest", SDOC1,
        sdocURL1)) {
      pass = false;
    }

    if (!pass)
      throw new Fault("ClientEnabledServerEnabledMTOMInTest failed");
  }

  /*
   * @testName: ClientEnabledServerDisabledMTOMInTest
   *
   * @assertion_ids: WS4EE:SPEC:3002; WS4EE:SPEC:3003; WS4EE:SPEC:3005;
   * WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:4013; WS4EE:SPEC:4015;
   *
   * @test_Strategy: Test MTOM attachments on input where mtom is disabled in
   * the endpoint but enabled on the client.
   */
  public void ClientEnabledServerDisabledMTOMInTest() throws Fault {
    TestUtil.logMsg("ClientEnabledServerDisabledMTOMInTest");
    boolean pass = true;

    TestUtil.logMsg("----------------------------------");
    TestUtil.logMsg("Testing appclient client");
    TestUtil.logMsg("----------------------------------");
    try {
      DataType data = new DataType();
      data.setDocName(SDOC1);
      data.setDocUrl(docURL1.toString());
      StreamSource doc = AttachmentHelper.getSourceDoc(docURL1);
      data.setDoc(doc);
      TestUtil
          .logMsg("Send 1 document using MTOM via webservice method mtomIn()");
      TestUtil.logMsg("Document to send: [" + SDOC1 + "]");
      String result = port2.mtomIn(data);
      if (!result.equals("")) {
        TestUtil.logErr("Appclient failed");
        TestUtil
            .logErr("An error occurred with one or more of the attachments");
        TestUtil.logErr("result=" + result);
        pass = false;
      } else {
        TestUtil.logMsg("Appclient passed");
      }
    } catch (Exception e) {
      TestUtil.logErr("Appclient failed");
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!execute_servlet_client("ClientEnabledServerDisabledMTOMInTest", SDOC1,
        sdocURL1)) {
      pass = false;
    }
    if (!execute_ejb_client("ClientEnabledServerDisabledMTOMInTest", SDOC1,
        sdocURL1)) {
      pass = false;
    }
    if (!pass)
      throw new Fault("ClientEnabledServerDisabledMTOMInTest failed");
  }

  /*
   * @testName: ClientEnabledServerEnabledMTOMInDefaultTest
   *
   * @assertion_ids: WS4EE:SPEC:3002; WS4EE:SPEC:3003; WS4EE:SPEC:3005;
   * WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:4013; WS4EE:SPEC:4015;
   *
   * @test_Strategy: Test MTOM attachments on input where mtom is enabled in the
   * endpoint and the client.
   */
  public void ClientEnabledServerEnabledMTOMInDefaultTest() throws Fault {
    TestUtil.logMsg("ClientEnabledServerEnabledMTOMInDefaultTest");
    boolean pass = true;

    TestUtil.logMsg("----------------------------------");
    TestUtil.logMsg("Testing appclient client");
    TestUtil.logMsg("----------------------------------");
    try {
      DataType data = new DataType();
      data.setDocName(SDOC1);
      data.setDocUrl(docURL1.toString());
      StreamSource doc = AttachmentHelper.getSourceDoc(docURL1);
      data.setDoc(doc);
      TestUtil
          .logMsg("Send 1 document using MTOM via webservice method mtomIn()");
      TestUtil.logMsg("Document to send: [" + SDOC1 + "]");
      String result = port1_2.mtomIn(data);
      if (!result.equals("")) {
        TestUtil.logErr("Appclient failed");
        TestUtil.logErr("result=" + result);
        pass = false;
      } else {
        TestUtil.logMsg("Appclient passed");
      }
    } catch (Exception e) {
      TestUtil.logErr("Appclient failed");
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!execute_servlet_client("ClientEnabledServerEnabledMTOMInDefaultTest",
        SDOC1, sdocURL1)) {
      pass = false;
    }
    if (!execute_ejb_client("ClientEnabledServerEnabledMTOMInDefaultTest",
        SDOC1, sdocURL1)) {
      pass = false;
    }

    if (!pass)
      throw new Fault("ClientEnabledServerEnabledMTOMInTest failed");
  }

  /*
   * @testName: ClientEnabledServerEnabledGT2000Test
   *
   * @assertion_ids: WS4EE:SPEC:3002; WS4EE:SPEC:3003; WS4EE:SPEC:3005;
   * WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:4013; WS4EE:SPEC:4015;
   *
   * @test_Strategy: Test that XOP is enabled when sending an attachment that is
   * greater than 2000 bytes. The client and endpoint have mtom enabled with the
   * threshold set to 2000
   */
  public void ClientEnabledServerEnabledGT2000Test() throws Fault {
    TestUtil.logMsg("ClientEnabledServerEnabledGT2000Test");
    boolean pass = true;

    TestUtil.logMsg("----------------------------------");
    TestUtil.logMsg("Testing appclient client");
    TestUtil.logMsg("----------------------------------");
    try {
      DataType data = new DataType();
      data.setDocName(SDOC2);
      data.setDocUrl(docURL2.toString());
      Image doc = AttachmentHelper.getImageDoc(docURL2);
      data.setDoc2(doc);
      TestUtil
          .logMsg("Send 1 document using MTOM via webservice method mtomIn()");
      TestUtil.logMsg("Document to send: [" + SDOC2 + "]");
      String result = port3_1.mtomIn2000(data);
      if (!result.equals("")) {
        TestUtil.logErr("Appclient failed");
        TestUtil.logErr("result=" + result);
        pass = false;
      } else {
        TestUtil.logMsg("Appclient passed");
      }
    } catch (Exception e) {
      TestUtil.logErr("Appclient failed");
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!execute_servlet_client("ClientEnabledServerEnabledGT2000Test", SDOC2,
        sdocURL2)) {
      pass = false;
    }
    if (!execute_ejb_client("ClientEnabledServerEnabledGT2000Test", SDOC2,
        sdocURL2)) {
      pass = false;
    }
    if (!pass)
      throw new Fault("ClientEnabledServerEnabledGT2000Test failed");
  }

  /*
   * @testName: ClientEnabledServerEnabledGT2000DefaultTest
   *
   * @assertion_ids: WS4EE:SPEC:3002; WS4EE:SPEC:3003; WS4EE:SPEC:3005;
   * WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:4013; WS4EE:SPEC:4015;
   *
   * @test_Strategy: Test that XOP is enabled when sending an attachment that is
   * greater than 0 bytes. The endpoint has mtom enabled with the threshold set
   * to 2000
   */
  public void ClientEnabledServerEnabledGT2000DefaultTest() throws Fault {
    TestUtil.logMsg("ClientEnabledServerEnabledGT2000DefaultTest");
    boolean pass = true;

    TestUtil.logMsg("----------------------------------");
    TestUtil.logMsg("Testing appclient client");
    TestUtil.logMsg("----------------------------------");
    try {
      DataType data = new DataType();
      data.setDocName(SDOC2);
      data.setDocUrl(docURL2.toString());
      Image doc = AttachmentHelper.getImageDoc(docURL2);
      data.setDoc2(doc);
      TestUtil
          .logMsg("Send 1 document using MTOM via webservice method mtomIn()");
      TestUtil.logMsg("Document to send: [" + SDOC2 + "]");
      String result = port3_3.mtomIn2000(data);
      if (!result.equals("")) {
        TestUtil.logErr("Appclient failed");
        TestUtil.logErr("result=" + result);
        pass = false;
      } else {
        TestUtil.logMsg("Appclient passed");
      }
    } catch (Exception e) {
      TestUtil.logErr("Appclient failed");
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!execute_servlet_client("ClientEnabledServerEnabledGT2000DefaultTest",
        SDOC2, sdocURL2)) {
      pass = false;
    }
    if (!execute_ejb_client("ClientEnabledServerEnabledGT2000DefaultTest",
        SDOC2, sdocURL2)) {
      pass = false;
    }
    if (!pass)
      throw new Fault("ClientEnabledServerEnabledGT2000DefaultTest failed");
  }

  /*
   * @testName: ClientDisabledServerEnabledGT2000Test
   *
   * @assertion_ids: WS4EE:SPEC:3002; WS4EE:SPEC:3003; WS4EE:SPEC:3005;
   * WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:4013; WS4EE:SPEC:4015;
   *
   * @test_Strategy: Test that XOP is disabled when sending an attachment that
   * is greater than 2000 bytes. The client has mtom disabled and the endpoint
   * has mtom enabled with the threshold set to 2000
   */
  public void ClientDisabledServerEnabledGT2000Test() throws Fault {
    TestUtil.logMsg("ClientDisabledServerEnabledGT2000Test");
    boolean pass = true;

    TestUtil.logMsg("----------------------------------");
    TestUtil.logMsg("Testing appclient client");
    TestUtil.logMsg("----------------------------------");
    try {
      DataType data = new DataType();
      data.setDocName(SDOC2);
      data.setDocUrl(docURL2.toString());
      Image doc = AttachmentHelper.getImageDoc(docURL2);
      data.setDoc2(doc);
      TestUtil
          .logMsg("Send 1 document using MTOM via webservice method mtomIn()");
      TestUtil.logMsg("Document to send: [" + SDOC2 + "]");
      String result = port3_2.mtomIn2000(data);
      if (!result.equals("")) {
        TestUtil.logErr("Appclient failed");
        TestUtil.logErr("result=" + result);
        pass = false;
      } else {
        TestUtil.logMsg("Appclient passed");
      }
    } catch (Exception e) {
      TestUtil.logErr("Appclient failed");
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!execute_servlet_client("ClientDisabledServerEnabledGT2000Test", SDOC2,
        sdocURL2)) {
      pass = false;
    }
    if (!execute_ejb_client("ClientDisabledServerEnabledGT2000Test", SDOC2,
        sdocURL2)) {
      pass = false;
    }
    if (!pass)
      throw new Fault("ClientDisabledServerEnabledGT2000Test failed");
  }

  /*
   * @testName: ClientEnabledServerDisabledGT2000Test
   *
   * @assertion_ids: WS4EE:SPEC:3002; WS4EE:SPEC:3003; WS4EE:SPEC:3005;
   * WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:4013; WS4EE:SPEC:4015;
   *
   * @test_Strategy: Test that XOP is enabled when sending an attachment that is
   * greater than 2000 bytes. The client has mtom enabled and the endpoint has
   * mtom disbled with the threshold set to 2000
   */
  public void ClientEnabledServerDisabledGT2000Test() throws Fault {
    TestUtil.logMsg("ClientEnabledServerDisabledGT2000Test");
    boolean pass = true;
    TestUtil.logMsg("----------------------------------");
    TestUtil.logMsg("Testing appclient client");
    TestUtil.logMsg("----------------------------------");
    try {
      DataType data = new DataType();
      data.setDocName(SDOC2);
      data.setDocUrl(docURL2.toString());
      Image doc = AttachmentHelper.getImageDoc(docURL2);
      data.setDoc2(doc);
      TestUtil
          .logMsg("Send 1 document using MTOM via webservice method mtomIn()");
      TestUtil.logMsg("Document to send: [" + SDOC2 + "]");
      String result = port4.mtomIn2000(data);
      if (!result.equals("")) {
        TestUtil.logErr("Appclient failed");
        TestUtil.logErr("result=" + result);
        pass = false;
      } else {
        TestUtil.logMsg("Appclient passed");
      }
    } catch (Exception e) {
      TestUtil.logErr("Appclient failed");
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!execute_servlet_client("ClientEnabledServerDisabledGT2000Test", SDOC2,
        sdocURL2)) {
      pass = false;
    }
    if (!execute_ejb_client("ClientEnabledServerDisabledGT2000Test", SDOC2,
        sdocURL2)) {
      pass = false;
    }
    if (!pass)
      throw new Fault("ClientEnabledServerDisabledGT2000Test failed");
  }

  /*
   * @testName: ClientEnabledServerEnabledLT2000Test
   *
   * @assertion_ids: WS4EE:SPEC:3002; WS4EE:SPEC:3003; WS4EE:SPEC:3005;
   * WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:4013; WS4EE:SPEC:4015;
   *
   * @test_Strategy: Test that XOP is enabled when sending an attachment that is
   * greater than 2000 bytes. The client and endpoint have mtom enabled with the
   * threshold set to 2000
   */
  public void ClientEnabledServerEnabledLT2000Test() throws Fault {
    TestUtil.logMsg("ClientEnabledServerEnabledLT2000Test");
    boolean pass = true;

    TestUtil.logMsg("----------------------------------");
    TestUtil.logMsg("Testing appclient client");
    TestUtil.logMsg("----------------------------------");
    try {
      DataType data = new DataType();
      data.setDocName(SDOC3);
      data.setDocUrl(docURL3.toString());
      Image doc = AttachmentHelper.getImageDoc(docURL3);
      data.setDoc2(doc);
      TestUtil
          .logMsg("Send 1 document using MTOM via webservice method mtomIn()");
      TestUtil.logMsg("Document to send: [" + SDOC3 + "]");
      String result = port3_1.mtomIn2000(data);
      if (!result.equals("")) {
        TestUtil.logErr("Appclient failed");
        TestUtil.logErr("result=" + result);
        pass = false;
      } else {
        TestUtil.logMsg("Appclient passed");
      }
    } catch (Exception e) {
      TestUtil.logErr("Appclient failed");
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!execute_servlet_client("ClientEnabledServerEnabledLT2000Test", SDOC3,
        sdocURL3)) {
      pass = false;
    }
    if (!execute_ejb_client("ClientEnabledServerEnabledLT2000Test", SDOC3,
        sdocURL3)) {
      pass = false;
    }
    if (!pass)
      throw new Fault("ClientEnabledServerEnabledLT2000Test failed");
  }

  private boolean execute_servlet_client(String testname, String SDOC,
      String sdocURL) {
    boolean pass = true;
    TestUtil.logMsg("----------------------------------");
    TestUtil.logMsg("Testing servlet client");
    TestUtil.logMsg("----------------------------------");
    try {
      url = ctsurl.getURL("http", hostname, portnum, SERVLET_CLIENT);
      TestUtil.logMsg("Servlet URL: " + url);
      props.setProperty("TEST", testname);
      props.setProperty("SDOC", SDOC);
      props.setProperty("sdocURL", sdocURL);
      urlConn = TestUtil.sendPostData(props, url);
      Properties p = TestUtil.getResponseProperties(urlConn);
      String result = p.getProperty("TESTRESULT");
      if (!result.equals("")) {
        TestUtil.logErr("Servlet failed");
        TestUtil.logErr("result=" + result);
        pass = false;
      } else {
        TestUtil.logMsg("Servlet passed");
      }
    } catch (Exception e) {
      TestUtil.logErr("Servlet failed");
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  private boolean execute_ejb_client(String testname, String SDOC,
      String sdocURL) {
    boolean pass = true;
    TestUtil.logMsg("----------------------------------");
    TestUtil.logMsg("Testing ejb client");
    TestUtil.logMsg("----------------------------------");
    try {
      props.setProperty("TEST", testname);
      props.setProperty("SDOC", SDOC);
      props.setProperty("sdocURL", sdocURL);
      Properties p = ejbclient.execute(props);
      String result = p.getProperty("TESTRESULT");
      if (!result.equals("")) {
        TestUtil.logErr("EJB failed");
        TestUtil.logErr("result=" + result);
        pass = false;
      } else {
        TestUtil.logMsg("EJB passed");
      }
    } catch (Exception e) {
      TestUtil.logErr("EJB failed");
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

}
