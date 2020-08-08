/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

/*
 * $Id$
 */

package com.sun.ts.tests.jaxws.ee.w2j.document.literal.mtomfeature;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import com.sun.ts.tests.jaxws.common.AttachmentHelper;

import java.io.*;
import java.net.*;
import java.rmi.*;
import java.util.*;

import java.awt.Image;
import jakarta.xml.ws.*;
import jakarta.xml.ws.soap.SOAPBinding;
import jakarta.xml.ws.soap.AddressingFeature;
import jakarta.xml.ws.soap.MTOMFeature;
import javax.xml.namespace.QName;

import jakarta.activation.DataHandler;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import java.util.Properties;
import com.sun.ts.tests.jaxws.sharedclients.ClientFactory;

import com.sun.javatest.Status;

import com.sun.ts.tests.jaxws.common.*;

import javax.naming.InitialContext;

public class Client extends ServiceEETest {

  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String MODEPROP = "platform.mode";

  String modeProperty = null; // platform.mode -> (standalone|jakartaEE)

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.ee.w2j.document.literal.mtomfeature.";

  // service and port information
  private static final String NAMESPACEURI = "http://mtomfeatureservice.org/wsdl";

  private static final String SERVICE_NAME = "MTOMFeatureTestService";

  private static final String PORT_NAME1 = "MTOMFeatureTest1Port";

  private static final String PORT_NAME2 = "MTOMFeatureTest2Port";

  private static final String PORT_NAME3 = "MTOMFeatureTest3Port";

  private static final String PORT_NAME4 = "MTOMFeatureTest4Port";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME1 = new QName(NAMESPACEURI, PORT_NAME1);

  private QName PORT_QNAME2 = new QName(NAMESPACEURI, PORT_NAME2);

  private QName PORT_QNAME3 = new QName(NAMESPACEURI, PORT_NAME3);

  private QName PORT_QNAME4 = new QName(NAMESPACEURI, PORT_NAME4);

  private TSURL ctsurl = new TSURL();

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  private URL docURL1 = null;

  private URL docURL2 = null;

  private URL docURL3 = null;

  private URL docURL4 = null;

  private URL docURL11 = null;

  private URL docURL12 = null;

  private URL docURL13 = null;

  private URL docURL14 = null;

  private URL docURLSmallJpeg = null;

  private URL docURLBigJpeg = null;

  String SDOC1 = "text.xml";

  String SDOC2 = "application.xml";

  String SDOC3 = "attach.html";

  String SDOC4 = "small.jpg";

  String SDOC11 = "text2.xml";

  String SDOC12 = "application2.xml";

  String SDOC13 = "attach2.html";

  String SDOC14 = "big.jpg";

  String SDOCSmallJpeg = "small.jpg";

  String SDOCBigJpeg = "big.jpg";

  // URL properties used by the test
  private static final String ENDPOINT_URL1 = "mtomfeature.endpoint.1";

  private static final String ENDPOINT_URL2 = "mtomfeature.endpoint.2";

  private static final String ENDPOINT_URL3 = "mtomfeature.endpoint.3";

  private static final String ENDPOINT_URL4 = "mtomfeature.endpoint.4";

  private static final String WSDLLOC_URL = "mtomfeature.wsdlloc.1";

  private static final String CTXROOT = "mtomfeature.ctxroot.1";

  private String url1 = null;

  private String url2 = null;

  private String url3 = null;

  private String url4 = null;

  private URL wsdlurl = null;

  private String ctxroot = null;

  private MTOMFeatureTest1 port1 = null;

  private MTOMFeatureTest2 port2 = null;

  private MTOMFeatureTest3 port3_1 = null;

  private MTOMFeatureTest3 port3_2 = null;

  private MTOMFeatureTest4 port4_1 = null;

  private MTOMFeatureTest4 port4_2 = null;

  private WebServiceFeature[] mtomenabled = { new MTOMFeature(true) };

  private WebServiceFeature[] mtomenabledtheshold2000 = {
      new MTOMFeature(true, 2000) };

  private WebServiceFeature[] mtomdisabledtheshold2000 = {
      new MTOMFeature(false, 2000) };

  static MTOMFeatureTestService service = null;

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXWS_Util.getURLFromProp(ENDPOINT_URL1);
    url1 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(WSDLLOC_URL);
    wsdlurl = ctsurl.getURL(PROTOCOL, hostname, portnum, file);

    file = JAXWS_Util.getURLFromProp(ENDPOINT_URL2);
    url2 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(ENDPOINT_URL3);
    url3 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(ENDPOINT_URL4);
    url4 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    ctxroot = JAXWS_Util.getURLFromProp(CTXROOT);
    TestUtil.logMsg("Service Endpoint URL1: " + url1);
    TestUtil.logMsg("Service Endpoint URL2: " + url2);
    TestUtil.logMsg("Service Endpoint URL3: " + url3);
    TestUtil.logMsg("Service Endpoint URL4: " + url4);
    TestUtil.logMsg("WSDL Location URL:    " + wsdlurl);
    TestUtil.logMsg("Context Root:         " + ctxroot);
  }

  private void getPortStandalone() throws Exception {
    port1 = (MTOMFeatureTest1) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        MTOMFeatureTestService.class, PORT_QNAME1, MTOMFeatureTest1.class,
        mtomenabled);
    JAXWS_Util.setTargetEndpointAddress(port1, url1);
    port2 = (MTOMFeatureTest2) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        MTOMFeatureTestService.class, PORT_QNAME2, MTOMFeatureTest2.class,
        mtomenabled);
    JAXWS_Util.setTargetEndpointAddress(port2, url2);
    port3_1 = (MTOMFeatureTest3) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        MTOMFeatureTestService.class, PORT_QNAME3, MTOMFeatureTest3.class,
        mtomenabledtheshold2000);
    JAXWS_Util.setTargetEndpointAddress(port3_1, url3);
    port3_2 = (MTOMFeatureTest3) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        MTOMFeatureTestService.class, PORT_QNAME3, MTOMFeatureTest3.class,
        mtomdisabledtheshold2000);
    JAXWS_Util.setTargetEndpointAddress(port3_2, url3);
    port4_1 = (MTOMFeatureTest4) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        MTOMFeatureTestService.class, PORT_QNAME4, MTOMFeatureTest4.class,
        mtomenabledtheshold2000);
    JAXWS_Util.setTargetEndpointAddress(port4_1, url4);
    port4_2 = (MTOMFeatureTest4) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        MTOMFeatureTestService.class, PORT_QNAME4, MTOMFeatureTest4.class,
        mtomdisabledtheshold2000);
    JAXWS_Util.setTargetEndpointAddress(port4_2, url4);

  }

  private void getPortJavaEE() throws Exception {
    TestUtil.logMsg("Obtaining service via WebServiceRef annotation");
    TestUtil.logMsg("service=" + service);
    port1 = (MTOMFeatureTest1) service.getPort(MTOMFeatureTest1.class,
        mtomenabled);
    port2 = (MTOMFeatureTest2) service.getPort(MTOMFeatureTest2.class,
        mtomenabled);
    port3_1 = (MTOMFeatureTest3) service.getPort(MTOMFeatureTest3.class,
        mtomenabledtheshold2000);
    port3_2 = (MTOMFeatureTest3) service.getPort(MTOMFeatureTest3.class,
        mtomdisabledtheshold2000);
    port4_1 = (MTOMFeatureTest4) service.getPort(MTOMFeatureTest4.class,
        mtomenabledtheshold2000);
    port4_2 = (MTOMFeatureTest4) service.getPort(MTOMFeatureTest4.class,
        mtomdisabledtheshold2000);
    // SOAPBinding binding = (SOAPBinding)((BindingProvider)port).getBinding();
    // JAXWS_Util.setSOAPLogging(port);
    TestUtil.logMsg("port=" + port1);
    TestUtil.logMsg("Obtained port1");
    JAXWS_Util.dumpTargetEndpointAddress(port1);
    TestUtil.logMsg("port=" + port2);
    TestUtil.logMsg("Obtained port2");
    JAXWS_Util.dumpTargetEndpointAddress(port2);
    TestUtil.logMsg("port=" + port3_1);
    TestUtil.logMsg("Obtained port3_1");
    JAXWS_Util.dumpTargetEndpointAddress(port3_1);
    TestUtil.logMsg("port=" + port3_2);
    TestUtil.logMsg("Obtained port3_2");
    JAXWS_Util.dumpTargetEndpointAddress(port3_2);
    TestUtil.logMsg("port=" + port4_1);
    TestUtil.logMsg("Obtained port4_1");
    JAXWS_Util.dumpTargetEndpointAddress(port4_1);
    TestUtil.logMsg("port=" + port4_2);
    TestUtil.logMsg("Obtained port4_2");
    JAXWS_Util.dumpTargetEndpointAddress(port4_2);
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.testArgs: -ap jaxws-url-props.dat
   * 
   * @class.setup_props: webServerHost; webServerPort; platform.mode;
   */

  public void setup(String[] args, Properties p) throws Fault {
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
      modeProperty = p.getProperty(MODEPROP);
      if (modeProperty.equals("standalone")) {
        getTestURLs();
        getPortStandalone();
      } else {
        TestUtil.logMsg(
            "WebServiceRef is not set in Client (get it from specific vehicle)");
        service = (MTOMFeatureTestService) getSharedObject();
        getTestURLs();
        getPortJavaEE();
      }
      docURL1 = ctsurl.getURL(PROTOCOL, hostname, portnum,
          ctxroot + "/" + SDOC1);
      docURL2 = ctsurl.getURL(PROTOCOL, hostname, portnum,
          ctxroot + "/" + SDOC2);
      docURL3 = ctsurl.getURL(PROTOCOL, hostname, portnum,
          ctxroot + "/" + SDOC3);
      docURL4 = ctsurl.getURL(PROTOCOL, hostname, portnum,
          ctxroot + "/" + SDOC4);
      docURL11 = ctsurl.getURL(PROTOCOL, hostname, portnum,
          ctxroot + "/" + SDOC11);
      docURL12 = ctsurl.getURL(PROTOCOL, hostname, portnum,
          ctxroot + "/" + SDOC12);
      docURL13 = ctsurl.getURL(PROTOCOL, hostname, portnum,
          ctxroot + "/" + SDOC13);
      docURL14 = ctsurl.getURL(PROTOCOL, hostname, portnum,
          ctxroot + "/" + SDOC14);
      docURLSmallJpeg = ctsurl.getURL(PROTOCOL, hostname, portnum,
          ctxroot + "/" + SDOCSmallJpeg);
      docURLBigJpeg = ctsurl.getURL(PROTOCOL, hostname, portnum,
          ctxroot + "/" + SDOCBigJpeg);

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("setup failed:", e);
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
   * @assertion_ids: JAXWS:SPEC:6015; JAXWS:SPEC:6015.2; JAXWS:SPEC:6015.3;
   * WS4EE:SPEC:5006; JAXWS:JAVADOC:192; JAXWS:SPEC:7021; JAXWS:SPEC:7021.1;
   *
   * @test_Strategy: Test MTOM attachments on input.
   */
  public void ClientEnabledServerEnabledMTOMInTest() throws Fault {
    TestUtil.logMsg("ClientEnabledServerEnabledMTOMInTest");
    boolean pass = true;

    try {
      DataType data = new DataType();

      data.setDocName1(SDOC1);
      data.setDocName2(SDOC2);
      data.setDocName3(SDOC3);
      data.setDocName4(SDOC4);

      data.setDocUrl1(docURL1.toString());
      data.setDocUrl2(docURL2.toString());
      data.setDocUrl3(docURL3.toString());
      data.setDocUrl4(docURL4.toString());

      StreamSource doc1 = AttachmentHelper.getSourceDoc(docURL1);
      StreamSource doc2 = AttachmentHelper.getSourceDoc(docURL2);
      DataHandler doc3 = AttachmentHelper.getDataHandlerDoc(docURL3);
      Image doc4 = AttachmentHelper.getImageDoc(docURL4);

      data.setDoc1(doc1);
      data.setDoc2(doc2);
      data.setDoc3(doc3);
      data.setDoc4(doc4);

      TestUtil
          .logMsg("Send 4 documents using MTOM via webservice method mtomIn()");
      TestUtil.logMsg("Documents to send: [" + SDOC1 + "," + SDOC2 + "," + SDOC3
          + "," + SDOC4 + "]");
      String result = port1.mtomIn(data);
      if (!result.equals("")) {
        TestUtil
            .logErr("An error occurred with one or more of the attachments");
        TestUtil.logErr("result=" + result);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred");
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault("ClientEnabledServerEnabledMTOMInTest failed");
  }

  /*
   * @testName: ClientEnabledServerEnabledMTOMInOutTest
   *
   * @assertion_ids: JAXWS:SPEC:6015; JAXWS:SPEC:6015.2; JAXWS:SPEC:6015.3;
   * WS4EE:SPEC:5006; JAXWS:JAVADOC:192; JAXWS:SPEC:7021; JAXWS:SPEC:7021.1;
   *
   * @test_Strategy: Test MTOM attachments on input and output.
   */
  public void ClientEnabledServerEnabledMTOMInOutTest() throws Fault {
    TestUtil.logMsg("ClientEnabledServerEnabledMTOMInOutTest");
    boolean pass = true;

    try {
      Holder<String> hDocName1 = new Holder<String>(SDOC1);
      Holder<String> hDocName2 = new Holder<String>(SDOC2);
      Holder<String> hDocName3 = new Holder<String>(SDOC3);
      Holder<String> hDocName4 = new Holder<String>(SDOC4);

      Holder<String> hDocUrl1 = new Holder<String>(docURL1.toString());
      Holder<String> hDocUrl2 = new Holder<String>(docURL2.toString());
      Holder<String> hDocUrl3 = new Holder<String>(docURL3.toString());
      Holder<String> hDocUrl4 = new Holder<String>(docURL4.toString());
      Holder<String> hDocUrl11 = new Holder<String>(docURL11.toString());
      Holder<String> hDocUrl12 = new Holder<String>(docURL12.toString());
      Holder<String> hDocUrl13 = new Holder<String>(docURL13.toString());
      Holder<String> hDocUrl14 = new Holder<String>(docURL14.toString());

      StreamSource doc1 = AttachmentHelper.getSourceDoc(docURL1);
      StreamSource doc2 = AttachmentHelper.getSourceDoc(docURL2);
      DataHandler doc3 = AttachmentHelper.getDataHandlerDoc(docURL3);
      Image doc4 = AttachmentHelper.getImageDoc(docURL4);

      Holder<Source> hDoc1 = new Holder<Source>(doc1);
      Holder<Source> hDoc2 = new Holder<Source>(doc2);
      Holder<DataHandler> hDoc3 = new Holder<DataHandler>(doc3);
      Holder<Image> hDoc4 = new Holder<Image>(doc4);
      Holder<String> hResult = new Holder<String>("");
      TestUtil.logMsg(
          "Send and receieve 4 documents using MTOM via webservice method mtomInOut()");
      TestUtil.logMsg("Documents to send: [" + SDOC1 + "," + SDOC2 + "," + SDOC3
          + "," + SDOC4 + "]");
      TestUtil.logMsg("Documents to receive: [" + SDOC11 + "," + SDOC12 + ","
          + SDOC13 + "," + SDOC14 + "]");
      port1.mtomInOut(hDocName1, hDocName2, hDocName3, hDocName4, hDocUrl1,
          hDocUrl2, hDocUrl3, hDocUrl4, hDocUrl11, hDocUrl12, hDocUrl13,
          hDocUrl14, hDoc1, hDoc2, hDoc3, hDoc4, hResult);
      if (!(hResult.value).equals("")) {
        TestUtil.logErr("Server-side errors occurred:\n" + hResult.value);
        pass = false;
      }
      TestUtil.logMsg("Verify the contents of the received documents");

      doc1 = AttachmentHelper.getSourceDoc(docURL11);
      doc2 = AttachmentHelper.getSourceDoc(docURL12);
      doc3 = AttachmentHelper.getDataHandlerDoc(docURL13);
      doc4 = AttachmentHelper.getImageDoc(docURL14);

      // Now test the documents that were sent back by Server
      String tmpRes = AttachmentHelper.validateAttachmentData(doc1, hDoc1.value,
          SDOC11);
      if (tmpRes != null) {
        TestUtil.logErr("Client-side error for doc11:|" + tmpRes + "|");
        pass = false;
      }
      tmpRes = AttachmentHelper.validateAttachmentData(doc2, hDoc2.value,
          SDOC12);
      if (tmpRes != null) {
        TestUtil.logErr("Client-side error for doc12:|" + tmpRes + "|");
        pass = false;
      }
      tmpRes = AttachmentHelper.validateAttachmentData(doc3, hDoc3.value,
          SDOC13);
      if (tmpRes != null) {
        TestUtil.logErr("Client-side error for doc13:|" + tmpRes + "|");
        pass = false;
      }
      tmpRes = AttachmentHelper.validateAttachmentData(doc4, hDoc4.value,
          SDOC14);
      if (tmpRes != null) {
        TestUtil.logErr("Client-side error for doc14:|" + tmpRes + "|");
        pass = false;
      }
      if (pass)
        TestUtil.logMsg("All received documents are as expected (ok)");

    } catch (Exception e) {
      TestUtil.logErr("Exception occurred");
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault("ClientEnabledServerEnabledMTOMInOutTest failed");
  }

  /*
   * @testName: ClientEnabledServerEnabledMTOMOutTest
   *
   * @assertion_ids: JAXWS:SPEC:6015; JAXWS:SPEC:6015.2; JAXWS:SPEC:6015.3;
   * WS4EE:SPEC:5006; JAXWS:JAVADOC:192; JAXWS:SPEC:7021; JAXWS:SPEC:7021.1;
   *
   * @test_Strategy: Test MTOM attachments on output.
   */
  public void ClientEnabledServerEnabledMTOMOutTest() throws Fault {
    TestUtil.logMsg("ClientEnabledServerEnabledMTOMOutTest");
    boolean pass = true;

    try {

      StreamSource doc1 = AttachmentHelper.getSourceDoc(docURL1);
      StreamSource doc2 = AttachmentHelper.getSourceDoc(docURL2);
      DataHandler doc3 = AttachmentHelper.getDataHandlerDoc(docURL3);
      Image doc4 = AttachmentHelper.getImageDoc(docURL4);

      String urls = docURL1.toString() + "," + docURL2.toString() + ","
          + docURL3.toString() + "," + docURL4.toString();
      TestUtil.logTrace("urls=" + urls);
      TestUtil.logMsg(
          "Receive 4 documents using MTOM via webservice method mtomOut()");
      TestUtil.logMsg("Documents to receive: [" + SDOC1 + "," + SDOC2 + ","
          + SDOC3 + "," + SDOC4 + "]");
      DataType data = port1.mtomOut(urls);
      TestUtil.logMsg("Verify the contents of the received documents");
      String tmpRes = AttachmentHelper.validateAttachmentData(doc1,
          data.getDoc1(), SDOC1);
      if (tmpRes != null) {
        TestUtil.logErr("Client-side error doc1:|" + tmpRes + "|");
        pass = false;
      }
      tmpRes = AttachmentHelper.validateAttachmentData(doc2, data.getDoc2(),
          SDOC2);
      if (tmpRes != null) {
        TestUtil.logErr("Client-side error doc2:|" + tmpRes + "|");
        pass = false;
      }
      tmpRes = AttachmentHelper.validateAttachmentData(doc3, data.getDoc3(),
          SDOC3);
      if (tmpRes != null) {
        TestUtil.logErr("Client-side error doc3:|" + tmpRes + "|");
        pass = false;
      }
      tmpRes = AttachmentHelper.validateAttachmentData(doc4, data.getDoc4(),
          SDOC4);
      if (tmpRes != null) {
        TestUtil.logErr("Client-side error doc4:|" + tmpRes + "|");
        pass = false;
      }
      if (pass)
        TestUtil.logMsg("All received documents are as expected (ok)");
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred");
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault("ClientEnabledServerEnabledMTOMOutTest failed");
  }

  /*
   * @testName: ClientEnabledServerEnabledMTOMOut2Test
   *
   * @assertion_ids: JAXWS:SPEC:6015; JAXWS:SPEC:6015.2; JAXWS:SPEC:6015.3;
   * WS4EE:SPEC:5006; JAXWS:JAVADOC:192; JAXWS:SPEC:7021; JAXWS:SPEC:7021.1;
   *
   * @test_Strategy: Test MTOM attachments on output.
   */
  public void ClientEnabledServerEnabledMTOMOut2Test() throws Fault {
    TestUtil.logMsg("ClientEnabledServerEnabledMTOMOut2Test");
    boolean pass = true;

    try {

      Image doc = AttachmentHelper.getImageDoc(docURLSmallJpeg);
      String urls = docURLSmallJpeg.toString();
      TestUtil.logMsg("urls=" + urls);
      TestUtil.logMsg(
          "Receive 1 document using MTOM via webservice method mtomOut2()");
      TestUtil.logMsg("Document to receive: [" + SDOCSmallJpeg + "]");
      DataType3 data = port1.mtomOut2(urls);
      TestUtil.logMsg("Verify the content of the received document");
      String tmpRes = AttachmentHelper.validateAttachmentData(doc,
          data.getDoc(), SDOCSmallJpeg);
      if (tmpRes != null) {
        TestUtil.logErr("Client-side error:|" + tmpRes + "|");
        pass = false;
      }
      if (pass)
        TestUtil.logMsg("The received document is as expected (ok)");
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred");
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault("ClientEnabledServerEnabledMTOMOut2Test failed");
  }

  /*
   * @testName: ClientEnabledServerDisabledMTOMInTest
   *
   * @assertion_ids: JAXWS:SPEC:6015; JAXWS:SPEC:6015.2; JAXWS:SPEC:6015.3;
   * WS4EE:SPEC:5006; JAXWS:JAVADOC:192; JAXWS:SPEC:7021; JAXWS:SPEC:7021.1;
   *
   * @test_Strategy: Test MTOM attachments on input.
   */
  public void ClientEnabledServerDisabledMTOMInTest() throws Fault {
    TestUtil.logMsg("ClientEnabledServerDisabledMTOMInTest");
    boolean pass = true;

    try {
      DataType data = new DataType();

      data.setDocName1(SDOC1);
      data.setDocName2(SDOC2);
      data.setDocName3(SDOC3);
      data.setDocName4(SDOC4);

      data.setDocUrl1(docURL1.toString());
      data.setDocUrl2(docURL2.toString());
      data.setDocUrl3(docURL3.toString());
      data.setDocUrl4(docURL4.toString());

      StreamSource doc1 = AttachmentHelper.getSourceDoc(docURL1);
      StreamSource doc2 = AttachmentHelper.getSourceDoc(docURL2);
      DataHandler doc3 = AttachmentHelper.getDataHandlerDoc(docURL3);
      Image doc4 = AttachmentHelper.getImageDoc(docURL4);

      data.setDoc1(doc1);
      data.setDoc2(doc2);
      data.setDoc3(doc3);
      data.setDoc4(doc4);

      TestUtil
          .logMsg("Send 4 documents using MTOM via webservice method mtomIn()");
      TestUtil.logMsg("Documents to send: [" + SDOC1 + "," + SDOC2 + "," + SDOC3
          + "," + SDOC4 + "]");
      String result = port2.mtomIn(data);
      if (!result.equals("")) {
        TestUtil
            .logErr("An error occurred with one or more of the attachments");
        TestUtil.logErr("result=" + result);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred");
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault("ClientEnabledServerDisabledMTOMInTest failed");
  }

  /*
   * @testName: ClientEnabledServerDisabledMTOMInOutTest
   *
   * @assertion_ids: JAXWS:SPEC:6015; JAXWS:SPEC:6015.2; JAXWS:SPEC:6015.3;
   * WS4EE:SPEC:5006; JAXWS:JAVADOC:192; JAXWS:SPEC:7021; JAXWS:SPEC:7021.1;
   *
   * @test_Strategy: Test MTOM attachments on input and output.
   */
  public void ClientEnabledServerDisabledMTOMInOutTest() throws Fault {
    TestUtil.logMsg("ClientEnabledServerDisabledMTOMInOutTest");
    boolean pass = true;

    try {
      Holder<String> hDocName1 = new Holder<String>(SDOC1);
      Holder<String> hDocName2 = new Holder<String>(SDOC2);
      Holder<String> hDocName3 = new Holder<String>(SDOC3);
      Holder<String> hDocName4 = new Holder<String>(SDOC4);

      Holder<String> hDocUrl1 = new Holder<String>(docURL1.toString());
      Holder<String> hDocUrl2 = new Holder<String>(docURL2.toString());
      Holder<String> hDocUrl3 = new Holder<String>(docURL3.toString());
      Holder<String> hDocUrl4 = new Holder<String>(docURL4.toString());
      Holder<String> hDocUrl11 = new Holder<String>(docURL11.toString());
      Holder<String> hDocUrl12 = new Holder<String>(docURL12.toString());
      Holder<String> hDocUrl13 = new Holder<String>(docURL13.toString());
      Holder<String> hDocUrl14 = new Holder<String>(docURL14.toString());

      StreamSource doc1 = AttachmentHelper.getSourceDoc(docURL1);
      StreamSource doc2 = AttachmentHelper.getSourceDoc(docURL2);
      DataHandler doc3 = AttachmentHelper.getDataHandlerDoc(docURL3);
      Image doc4 = AttachmentHelper.getImageDoc(docURL4);

      Holder<Source> hDoc1 = new Holder<Source>(doc1);
      Holder<Source> hDoc2 = new Holder<Source>(doc2);
      Holder<DataHandler> hDoc3 = new Holder<DataHandler>(doc3);
      Holder<Image> hDoc4 = new Holder<Image>(doc4);
      Holder<String> hResult = new Holder<String>("");
      TestUtil.logMsg(
          "Send and receieve 4 documents using MTOM via webservice method mtomInOut()");
      TestUtil.logMsg("Documents to send: [" + SDOC1 + "," + SDOC2 + "," + SDOC3
          + "," + SDOC4 + "]");
      TestUtil.logMsg("Documents to receive: [" + SDOC11 + "," + SDOC12 + ","
          + SDOC13 + "," + SDOC14 + "]");
      port2.mtomInOut(hDocName1, hDocName2, hDocName3, hDocName4, hDocUrl1,
          hDocUrl2, hDocUrl3, hDocUrl4, hDocUrl11, hDocUrl12, hDocUrl13,
          hDocUrl14, hDoc1, hDoc2, hDoc3, hDoc4, hResult);
      if (!(hResult.value).equals("")) {
        TestUtil.logErr("Server-side errors occurred:\n" + hResult.value);
        pass = false;
      }
      TestUtil.logMsg("Verify the contents of the received documents");

      doc1 = AttachmentHelper.getSourceDoc(docURL11);
      doc2 = AttachmentHelper.getSourceDoc(docURL12);
      doc3 = AttachmentHelper.getDataHandlerDoc(docURL13);
      doc4 = AttachmentHelper.getImageDoc(docURL14);

      // Now test the documents that were sent back by Server
      String tmpRes = AttachmentHelper.validateAttachmentData(doc1, hDoc1.value,
          SDOC11);
      if (tmpRes != null) {
        TestUtil.logErr("Client-side error doc11:|" + tmpRes + "|");
        pass = false;
      }
      tmpRes = AttachmentHelper.validateAttachmentData(doc2, hDoc2.value,
          SDOC12);
      if (tmpRes != null) {
        TestUtil.logErr("Client-side error doc12:|" + tmpRes + "|");
        pass = false;
      }
      tmpRes = AttachmentHelper.validateAttachmentData(doc3, hDoc3.value,
          SDOC13);
      if (tmpRes != null) {
        TestUtil.logErr("Client-side error doc13:|" + tmpRes + "|");
        pass = false;
      }
      tmpRes = AttachmentHelper.validateAttachmentData(doc4, hDoc4.value,
          SDOC14);
      if (tmpRes != null) {
        TestUtil.logErr("Client-side error doc14:|" + tmpRes + "|");
        pass = false;
      }
      if (pass)
        TestUtil.logMsg("All received documents are as expected (ok)");

    } catch (Exception e) {
      TestUtil.logErr("Exception occurred");
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault("ClientEnabledServerDisabledMTOMInOutTest failed");
  }

  /*
   * @testName: ClientEnabledServerDisabledMTOMOutTest
   *
   * @assertion_ids: JAXWS:SPEC:6015; JAXWS:SPEC:6015.2; JAXWS:SPEC:6015.3;
   * WS4EE:SPEC:5006; JAXWS:JAVADOC:192; JAXWS:SPEC:7021; JAXWS:SPEC:7021.1;
   *
   * @test_Strategy: Test MTOM attachments on output.
   */
  public void ClientEnabledServerDisabledMTOMOutTest() throws Fault {
    TestUtil.logMsg("ClientEnabledServerDisabledMTOMOutTest");
    boolean pass = true;

    try {

      StreamSource doc1 = AttachmentHelper.getSourceDoc(docURL1);
      StreamSource doc2 = AttachmentHelper.getSourceDoc(docURL2);
      DataHandler doc3 = AttachmentHelper.getDataHandlerDoc(docURL3);
      Image doc4 = AttachmentHelper.getImageDoc(docURL4);

      String urls = docURL1.toString() + "," + docURL2.toString() + ","
          + docURL3.toString() + "," + docURL4.toString();
      TestUtil.logTrace("urls=" + urls);
      TestUtil.logMsg(
          "Receive 4 documents using MTOM via webservice method mtomOut()");
      TestUtil.logMsg("Documents to receive: [" + SDOC1 + "," + SDOC2 + ","
          + SDOC3 + "," + SDOC4 + "]");
      DataType data = port2.mtomOut(urls);
      TestUtil.logMsg("Verify the contents of the received documents");
      String tmpRes = AttachmentHelper.validateAttachmentData(doc1,
          data.getDoc1(), SDOC1);
      if (tmpRes != null) {
        TestUtil.logErr("Client-side error doc1:|" + tmpRes + "|");
        pass = false;
      }
      tmpRes = AttachmentHelper.validateAttachmentData(doc2, data.getDoc2(),
          SDOC2);
      if (tmpRes != null) {
        TestUtil.logErr("Client-side error doc2:|" + tmpRes + "|");
        pass = false;
      }
      tmpRes = AttachmentHelper.validateAttachmentData(doc3, data.getDoc3(),
          SDOC3);
      if (tmpRes != null) {
        TestUtil.logErr("Client-side error doc3:|" + tmpRes + "|");
        pass = false;
      }
      tmpRes = AttachmentHelper.validateAttachmentData(doc4, data.getDoc4(),
          SDOC4);
      if (tmpRes != null) {
        TestUtil.logErr("Client-side error doc4:|" + tmpRes + "|");
        pass = false;
      }
      if (pass)
        TestUtil.logMsg("All received documents are as expected (ok)");
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred");
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault("ClientEnabledServerDisabledMTOMOutTest failed");
  }

  /*
   * @testName: ClientEnabledServerDisabledMTOMOut2Test
   *
   * @assertion_ids: JAXWS:SPEC:6015; JAXWS:SPEC:6015.2; JAXWS:SPEC:6015.3;
   * WS4EE:SPEC:5006; JAXWS:JAVADOC:192; JAXWS:SPEC:7021; JAXWS:SPEC:7021.1;
   *
   * @test_Strategy: Test MTOM attachments on output.
   */
  public void ClientEnabledServerDisabledMTOMOut2Test() throws Fault {
    TestUtil.logMsg("ClientEnabledServerDisabledMTOMOut2Test");
    boolean pass = true;

    try {

      Image doc = AttachmentHelper.getImageDoc(docURLSmallJpeg);
      String urls = docURLSmallJpeg.toString();
      TestUtil.logMsg("urls=" + urls);
      TestUtil.logMsg(
          "Receive 1 document using MTOM via webservice method mtomOut2()");
      TestUtil.logMsg("Document to receive: [" + SDOCSmallJpeg + "]");
      DataType3 data = port2.mtomOut2(urls);
      TestUtil.logMsg("Verify the content of the received document");
      String tmpRes = AttachmentHelper.validateAttachmentData(doc,
          data.getDoc(), SDOCSmallJpeg);
      if (tmpRes != null) {
        TestUtil.logErr("Client-side error:|" + tmpRes + "|");
        pass = false;
      }
      if (pass)
        TestUtil.logMsg("The received document is as expected (ok)");
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred");
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault("ClientEnabledServerDisabledMTOMOut2Test failed");
  }

  /*
   * @testName: ClientEnabledServerEnabledLT2000Test
   *
   * @assertion_ids: JAXWS:SPEC:6015; JAXWS:SPEC:6015.2; JAXWS:SPEC:6015.3;
   * JAXWS:SPEC:6015.4; JAXWS:SPEC:6015.5; JAXWS:JAVADOC:192; JAXWS:JAVADOC:193;
   * JAXWS:SPEC:7021; JAXWS:SPEC:7021.1; JAXWS:SPEC:7021.2;
   *
   * @test_Strategy: Test that XOP is enabled when sending an attachment that is
   * less than 2000 bytes. The endpoint has mtom enabled with the threshold set
   * to 2000
   */
  public void ClientEnabledServerEnabledLT2000Test() throws Fault {
    TestUtil.logMsg("ClientEnabledServerEnabledLT2000Test");
    boolean pass = true;

    try {
      DataType3 d = new DataType3();
      d.setTestName("ClientEnabledServerEnabledLT2000Test");
      Image doc = AttachmentHelper.getImageDoc(docURLSmallJpeg);
      d.setDoc(doc);

      TestUtil.logMsg("Sending a jpg document that is below the threshold ["
          + SDOCSmallJpeg + "]");
      port3_1.threshold2000(d);

      TestUtil.logMsg("Verify the content of the received document");
      String tmpRes = AttachmentHelper.validateAttachmentData(doc, d.getDoc(),
          SDOCSmallJpeg);
      if (tmpRes != null) {
        TestUtil.logErr("Client-side error:|" + tmpRes + "|");
        pass = false;
      }
      if (pass)
        TestUtil.logMsg("The received document is as expected (ok)");
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred");
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault("ClientEnabledServerEnabledGT2000Test failed");
  }

  /*
   * @testName: ClientEnabledServerEnabledGT2000Test
   *
   * @assertion_ids: JAXWS:SPEC:6015; JAXWS:SPEC:6015.2; JAXWS:SPEC:6015.3;
   * JAXWS:SPEC:6015.4; JAXWS:SPEC:6015.5; JAXWS:JAVADOC:192; JAXWS:JAVADOC:193;
   * JAXWS:SPEC:7021; JAXWS:SPEC:7021.1; JAXWS:SPEC:7021.2;
   *
   * @test_Strategy: Test that XOP is enabled when sending an attachment that is
   * greater than 2000 bytes. The endpoint has mtom enabled with the threshold
   * set to 2000
   */
  public void ClientEnabledServerEnabledGT2000Test() throws Fault {
    TestUtil.logMsg("ClientEnabledServerEnabledGT2000Test");
    boolean pass = true;

    try {
      DataType3 d = new DataType3();
      d.setTestName("ClientEnabledServerEnabledGT2000Test");
      Image doc = AttachmentHelper.getImageDoc(docURLBigJpeg);
      d.setDoc(doc);

      TestUtil.logMsg("Sending a jpg document that is above the threshold ["
          + SDOCBigJpeg + "]");
      port3_1.threshold2000(d);

      TestUtil.logMsg("Verify the content of the received document");
      String tmpRes = AttachmentHelper.validateAttachmentData(doc, d.getDoc(),
          SDOCBigJpeg);
      if (tmpRes != null) {
        TestUtil.logErr("Client-side error:|" + tmpRes + "|");
        pass = false;
      }
      if (pass)
        TestUtil.logMsg("The received document is as expected (ok)");
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred");
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault("ClientEnabledServerEnabledGT2000Test failed");
  }

  /*
   * @testName: ClientDisabledServerEnabledLT2000Test
   *
   * @assertion_ids: JAXWS:SPEC:6015; JAXWS:SPEC:6015.2; JAXWS:SPEC:6015.3;
   * JAXWS:SPEC:6015.4; JAXWS:SPEC:6015.5; JAXWS:JAVADOC:192; JAXWS:JAVADOC:193;
   * JAXWS:SPEC:7021; JAXWS:SPEC:7021.1; JAXWS:SPEC:7021.2;
   *
   * @test_Strategy: Test that XOP is disabled when sending an attachment that
   * is less than 2000 bytes. The endpoint has mtom enabled with the threshold
   * set to 2000
   */
  public void ClientDisabledServerEnabledLT2000Test() throws Fault {
    TestUtil.logMsg("ClientDisabledServerEnabledLT2000Test");
    boolean pass = true;

    try {
      DataType3 d = new DataType3();
      d.setTestName("ClientDisabledServerEnabledLT2000Test");
      Image doc = AttachmentHelper.getImageDoc(docURLSmallJpeg);
      d.setDoc(doc);

      TestUtil.logMsg("Sending a jpg document that is below the threshold ["
          + SDOCSmallJpeg + "]");
      port3_2.threshold2000(d);

      TestUtil.logMsg("Verify the content of the received document");
      String tmpRes = AttachmentHelper.validateAttachmentData(doc, d.getDoc(),
          SDOCSmallJpeg);
      if (tmpRes != null) {
        TestUtil.logErr("Client-side error:|" + tmpRes + "|");
        pass = false;
      }
      if (pass)
        TestUtil.logMsg("The received document is as expected (ok)");
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred");
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault("ClientDisabledServerEnabledLT2000Test failed");
  }

  /*
   * @testName: ClientDisabledServerEnabledGT2000Test
   *
   * @assertion_ids: JAXWS:SPEC:6015; JAXWS:SPEC:6015.2; JAXWS:SPEC:6015.3;
   * JAXWS:SPEC:6015.4; JAXWS:SPEC:6015.5; JAXWS:JAVADOC:192; JAXWS:JAVADOC:193;
   * JAXWS:SPEC:7021; JAXWS:SPEC:7021.1; JAXWS:SPEC:7021.2;
   *
   * @test_Strategy: Test that XOP is disabled when sending an attachment that
   * is greater than 2000 bytes. The endpoint has mtom enabled with the
   * threshold set to 2000
   */
  public void ClientDisabledServerEnabledGT2000Test() throws Fault {
    TestUtil.logMsg("ClientDisabledServerEnabledGT2000Test");
    boolean pass = true;

    try {
      DataType3 d = new DataType3();
      d.setTestName("ClientDisabledServerEnabledGT2000Test");
      Image doc = AttachmentHelper.getImageDoc(docURLBigJpeg);
      d.setDoc(doc);

      TestUtil.logMsg("Sending a jpg document that is above the threshold ["
          + SDOCBigJpeg + "]");
      port3_2.threshold2000(d);

      TestUtil.logMsg("Verify the content of the received document");
      String tmpRes = AttachmentHelper.validateAttachmentData(doc, d.getDoc(),
          SDOCBigJpeg);
      if (tmpRes != null) {
        TestUtil.logErr("Client-side error:|" + tmpRes + "|");
        pass = false;
      }
      if (pass)
        TestUtil.logMsg("The received document is as expected (ok)");
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred");
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault("ClientDisabledServerEnabledGT2000Test failed");
  }

  /*
   * @testName: ClientEnabledServerDisabledLT2000Test
   *
   * @assertion_ids: JAXWS:SPEC:6015; JAXWS:SPEC:6015.2; JAXWS:SPEC:6015.3;
   * JAXWS:SPEC:6015.4; JAXWS:SPEC:6015.5; JAXWS:JAVADOC:192; JAXWS:JAVADOC:193;
   * JAXWS:SPEC:7021; JAXWS:SPEC:7021.1; JAXWS:SPEC:7021.2;
   *
   * @test_Strategy: Test that XOP is enabled when sending an attachment that is
   * less than 2000 bytes. The endpoint has mtom disabled with the threshold set
   * to 2000
   */
  public void ClientEnabledServerDisabledLT2000Test() throws Fault {
    TestUtil.logMsg("ClientEnabledServerDisabledLT2000Test");
    boolean pass = true;

    try {
      DataType3 d = new DataType3();
      d.setTestName("ClientEnabledServerDisabledLT2000Test");
      Image doc = AttachmentHelper.getImageDoc(docURLSmallJpeg);
      d.setDoc(doc);

      TestUtil.logMsg("Sending a jpg document that is below the threshold ["
          + SDOCSmallJpeg + "]");
      port4_1.threshold2000(d);

      TestUtil.logMsg("Verify the content of the received document");
      String tmpRes = AttachmentHelper.validateAttachmentData(doc, d.getDoc(),
          SDOCSmallJpeg);
      if (tmpRes != null) {
        TestUtil.logErr("Client-side error:|" + tmpRes + "|");
        pass = false;
      }
      if (pass)
        TestUtil.logMsg("The received document is as expected (ok)");
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred");
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault("ClientEnabledServerDisabledLT2000Test failed");
  }

  /*
   * @testName: ClientEnabledServerDisabledGT2000Test
   *
   * @assertion_ids: JAXWS:SPEC:6015; JAXWS:SPEC:6015.2; JAXWS:SPEC:6015.3;
   * JAXWS:SPEC:6015.4; JAXWS:SPEC:6015.5; JAXWS:JAVADOC:192; JAXWS:JAVADOC:193;
   * JAXWS:SPEC:7021; JAXWS:SPEC:7021.1; JAXWS:SPEC:7021.2;
   *
   * @test_Strategy: Test that XOP is enabled when sending an attachment that is
   * greater than 2000 bytes. The endpoint has mtom disabled with the threshold
   * set to 2000
   */
  public void ClientEnabledServerDisabledGT2000Test() throws Fault {
    TestUtil.logMsg("ClientEnabledServerDisabledGT2000Test");
    boolean pass = true;

    try {
      DataType3 d = new DataType3();
      d.setTestName("ClientEnabledServerDisabledGT2000Test");
      Image doc = AttachmentHelper.getImageDoc(docURLBigJpeg);
      d.setDoc(doc);

      TestUtil.logMsg("Sending a jpg document that is above the threshold ["
          + SDOCBigJpeg + "]");
      port4_1.threshold2000(d);

      TestUtil.logMsg("Verify the content of the received document");
      String tmpRes = AttachmentHelper.validateAttachmentData(doc, d.getDoc(),
          SDOCBigJpeg);
      if (tmpRes != null) {
        TestUtil.logErr("Client-side error:|" + tmpRes + "|");
        pass = false;
      }
      if (pass)
        TestUtil.logMsg("The received document is as expected (ok)");
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred");
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault("ClientEnabledServerDisabledGT2000Test failed");
  }

  /*
   * @testName: ClientDisabledServerDisabledLT2000Test
   *
   * @assertion_ids: JAXWS:SPEC:6015; JAXWS:SPEC:6015.2; JAXWS:SPEC:6015.3;
   * JAXWS:SPEC:6015.4; JAXWS:SPEC:6015.5; JAXWS:JAVADOC:192; JAXWS:JAVADOC:193;
   * JAXWS:SPEC:7021; JAXWS:SPEC:7021.1; JAXWS:SPEC:7021.2;
   *
   * @test_Strategy: Test that XOP is enabled when sending an attachment that is
   * less than 2000 bytes. The endpoint has mtom disabled with the threshold set
   * to 2000
   */
  public void ClientDisabledServerDisabledLT2000Test() throws Fault {
    TestUtil.logMsg("ClientDisabledServerDisabledLT2000Test");
    boolean pass = true;

    try {
      DataType3 d = new DataType3();
      d.setTestName("ClientDisabledServerDisabledLT2000Test");
      Image doc = AttachmentHelper.getImageDoc(docURLSmallJpeg);
      d.setDoc(doc);

      TestUtil.logMsg("Sending a jpg document that is below the threshold ["
          + SDOCSmallJpeg + "]");
      port4_2.threshold2000(d);

      TestUtil.logMsg("Verify the content of the received document");
      String tmpRes = AttachmentHelper.validateAttachmentData(doc, d.getDoc(),
          SDOCSmallJpeg);
      if (tmpRes != null) {
        TestUtil.logErr("Client-side error:|" + tmpRes + "|");
        pass = false;
      }
      if (pass)
        TestUtil.logMsg("The received document is as expected (ok)");
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred");
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault("ClientDisabledServerDisabledLT2000Test failed");
  }

  /*
   * @testName: ClientDisabledServerDisabledGT2000Test
   *
   * @assertion_ids: JAXWS:SPEC:6015; JAXWS:SPEC:6015.2; JAXWS:SPEC:6015.3;
   * JAXWS:SPEC:6015.4; JAXWS:SPEC:6015.5; JAXWS:JAVADOC:192; JAXWS:JAVADOC:193;
   * JAXWS:SPEC:7021; JAXWS:SPEC:7021.1; JAXWS:SPEC:7021.2;
   *
   * @test_Strategy: Test that XOP is enabled when sending an attachment that is
   * less than 2000 bytes. The endpoint has mtom disabled with the threshold set
   * to 2000
   */
  public void ClientDisabledServerDisabledGT2000Test() throws Fault {
    TestUtil.logMsg("ClientDisabledServerDisabledGT2000Test");
    boolean pass = true;

    try {
      DataType3 d = new DataType3();
      d.setTestName("ClientDisabledServerDisabledGT2000Test");
      Image doc = AttachmentHelper.getImageDoc(docURLBigJpeg);
      d.setDoc(doc);

      TestUtil.logMsg("Sending a jpg document that is above the threshold ["
          + SDOCBigJpeg + "]");
      port4_2.threshold2000(d);

      TestUtil.logMsg("Verify the content of the received document");
      String tmpRes = AttachmentHelper.validateAttachmentData(doc, d.getDoc(),
          SDOCBigJpeg);
      if (tmpRes != null) {
        TestUtil.logErr("Client-side error:|" + tmpRes + "|");
        pass = false;
      }
      if (pass)
        TestUtil.logMsg("The received document is as expected (ok)");
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred");
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault("ClientDisabledServerDisabledGT2000Test failed");
  }

}
