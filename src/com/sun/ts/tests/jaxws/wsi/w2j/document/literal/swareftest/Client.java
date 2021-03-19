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
 * $Id: Client.java 59593 2009-09-28 15:35:32Z af70133 $
 */

package com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swareftest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.rmi.*;
import java.util.*;
import java.nio.charset.Charset;

import jakarta.xml.ws.*;
import jakarta.xml.soap.*;
import jakarta.activation.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.*;

import java.util.Properties;

import javax.xml.namespace.QName;

import com.sun.javatest.Status;

import com.sun.ts.tests.jaxws.common.*;

import javax.naming.InitialContext;

import com.sun.ts.tests.jaxws.sharedclients.ClientFactory;

import com.sun.ts.lib.harness.*;

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

  private TSURL ctsurl = new TSURL();

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "wsidlswareftest.endpoint.1";

  private static final String WSDLLOC_URL = "wsidlswareftest.wsdlloc.1";

  private static final String CTXROOT = "wsidlswareftest.ctxroot.1";

  private String surl = null;

  private String file = null;

  private String ctxroot = null;

  private URL wsdlurl = null;

  private static final String NAMESPACEURI = "http://SwaRefTestService.org/wsdl";

  private static final String SERVICE_NAME = "WSIDLSwaRefTestService";

  private static final String PORT_NAME = "SwaRefTestPort";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  private DataHandler dh1 = null;

  private DataHandler dh2 = null;

  private DataHandler dh3 = null;

  private DataHandler dh4 = null;

  private URL url1 = null;

  private URL url2 = null;

  private URL url3 = null;

  private URL url4 = null;

  static WSIDLSwaRefTestService service = null;

  /***********************************************************************
   * All the test cases in this file test all of the assertions specified in the
   * WSI Attachment Profile 1.0 specification.
   **********************************************************************/
  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    file = JAXWS_Util.getURLFromProp(ENDPOINT_URL);
    surl = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(WSDLLOC_URL);
    wsdlurl = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    ctxroot = JAXWS_Util.getURLFromProp(CTXROOT);
    TestUtil.logMsg("Service Endpoint URL: " + surl);
    TestUtil.logMsg("WSDL Location URL:    " + wsdlurl);
    TestUtil.logMsg("Context Root:         " + ctxroot);
  }

  SwaRefTest port = null;

  private void getPortStandalone() throws Exception {
    port = (SwaRefTest) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        WSIDLSwaRefTestService.class, PORT_QNAME, SwaRefTest.class);
    JAXWS_Util.setTargetEndpointAddress(port, surl);
  }

  private void getTargetEndpointAddress(Object port) throws Exception {
    TestUtil.logMsg("Get Target Endpoint Address for port=" + port);
    String url = JAXWS_Util.getTargetEndpointAddress(port);
    TestUtil.logMsg("Target Endpoint Address=" + url);
  }

  private void getPortJavaEE() throws Exception {
    TestUtil.logMsg("Obtain service via WebServiceRef annotation");
    TestUtil.logMsg("service=" + service);
    port = (SwaRefTest) service.getPort(SwaRefTest.class);
    TestUtil.logMsg("port=" + port);
    TestUtil.logMsg("Obtained port");
    getTargetEndpointAddress(port);
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
      modeProperty = p.getProperty(MODEPROP);
      if (modeProperty.equals("standalone")) {
        getTestURLs();
        getPortStandalone();
      } else {
        TestUtil.logMsg(
            "WebServiceRef is not set in Client (get it from specific vehicle)");
        service = (WSIDLSwaRefTestService) getSharedObject();
        getTestURLs();
        getPortJavaEE();
      }
      TestUtil.logMsg("Create URL's to attachments");
      url1 = ctsurl.getURL("http", hostname, portnum, ctxroot + "/attach.txt");
      url2 = ctsurl.getURL("http", hostname, portnum, ctxroot + "/attach.html");
      url3 = ctsurl.getURL("http", hostname, portnum, ctxroot + "/attach.xml");
      url4 = ctsurl.getURL("http", hostname, portnum, ctxroot + "/attach.jpeg");
      TestUtil.logMsg("url1=" + url1);
      TestUtil.logMsg("url2=" + url2);
      TestUtil.logMsg("url3=" + url3);
      TestUtil.logMsg("url4=" + url4);
      TestUtil.logMsg("Create DataHandler's to attachments");
      dh1 = new DataHandler(url1);
      dh2 = new DataHandler(url2);
      dh3 = new DataHandler(url3);
      dh4 = new DataHandler(javax.imageio.ImageIO.read(url4), "image/jpeg");
      TestUtil.logMsg("dh1.getContentType()=" + dh1.getContentType());
      TestUtil.logMsg("dh2.getContentType()=" + dh2.getContentType());
      TestUtil.logMsg("dh3.getContentType()=" + dh3.getContentType());
      TestUtil.logMsg("dh4.getContentType()=" + dh4.getContentType());
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
    TestUtil.logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    TestUtil.logMsg("cleanup ok");
  }

  /*
   * @testName: EchoSingleSwaRefAttachmentTest
   *
   * @assertion_ids: WSI:SPEC:R2901; WSI:SPEC:R2907; WSI:SPEC:R2909;
   * WSI:SPEC:R2910; WSI:SPEC:R2911; WSI:SPEC:R2931; WSI:SPEC:R2921;
   * WSI:SPEC:R2926; WSI:SPEC:R2929; WSI:SPEC:R2946; JAXWS:SPEC:10011;
   * WSI:SPEC:R2927; JAXWS:SPEC:2051; JAXWS:SPEC:2052; JAXWS:SPEC:2053;
   * WSI:SPEC:R2940; WSI:SPEC:R2928;
   *
   * @test_Strategy: Send and Receive a single attachment via swaRef type.
   *
   */
  public void EchoSingleSwaRefAttachmentTest() throws Fault {
    TestUtil.logMsg("EchoSingleSwaRefAttachmentTest");
    boolean pass = true;

    try {
      TestUtil
          .logMsg("Send and receive (text/xml) attachment via the swaRef type");
      SwaRefTypeRequest request = new SwaRefTypeRequest();
      DataHandler swaRefInput = dh3;
      request.setAttachment(swaRefInput);
      SwaRefTypeResponse response = port.echoSingleSwaRefAttachment(request);
      if (!ValidateSingleSwaRefAttachmentTestCase(request, response,
          "text/xml"))
        pass = false;
      TestUtil.logMsg(
          "Send and receive (text/plain) attachment via the swaRef type");
      swaRefInput = dh1;
      request.setAttachment(swaRefInput);
      response = port.echoSingleSwaRefAttachment(request);
      if (!ValidateSingleSwaRefAttachmentTestCase(request, response,
          "text/plain"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("EchoSingleSwaRefAttachmentTest failed", e);
    }
    if (!pass)
      throw new Fault("EchoSingleSwaRefAttachmentTest failed");
  }

  /*
   * @testName: EchoMultipleSwaRefAttachmentsTest
   *
   * @assertion_ids: WSI:SPEC:R2901; WSI:SPEC:R2907; WSI:SPEC:R2909;
   * WSI:SPEC:R2910; WSI:SPEC:R2911; WSI:SPEC:R2931; WSI:SPEC:R2921;
   * WSI:SPEC:R2926; WSI:SPEC:R2929; WSI:SPEC:R2946; JAXWS:SPEC:10011;
   * WSI:SPEC:R2927; JAXWS:SPEC:2051; JAXWS:SPEC:2052; JAXWS:SPEC:2053;
   * WSI:SPEC:R2940; WSI:SPEC:R2928;
   *
   * @test_Strategy: Send and Receive multiple attachments via swaRef type.
   *
   */
  public void EchoMultipleSwaRefAttachmentsTest() throws Fault {
    TestUtil.logMsg("SwaRefAttachmentsTest2");
    boolean pass = true;

    try {
      TestUtil.logMsg(
          "Send and receive (text/xml, text/plain, text/html) attachments via the swaRef type");
      SwaRefTypeRequest2 request = new SwaRefTypeRequest2();
      request.setAttachment1(dh3);
      request.setAttachment2(dh1);
      request.setAttachment3(dh4);
      SwaRefTypeResponse2 response = port
          .echoMultipleSwaRefAttachments(request);
      if (!ValidateMultipleSwaRefAttachmentsTestCase(request, response))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("EchoMultipleSwaRefAttachmentsTest failed", e);
    }
    if (!pass)
      throw new Fault("EchoMultipleSwaRefAttachmentsTest failed");
  }

  /*******************************************************************************
   * Validate request, response and attachments (swaRefAttachments)
   ******************************************************************************/
  private boolean ValidateSingleSwaRefAttachmentTestCase(
      SwaRefTypeRequest request, SwaRefTypeResponse response, String type) {
    boolean result = true;
    TestUtil.logMsg("--------------------------------------------------------");
    TestUtil.logMsg("Validating the request, the response, and the attachment");
    TestUtil.logMsg("--------------------------------------------------------");
    if (type.equals("text/xml")) {
      try {
        StreamSource sr1 = new StreamSource(
            request.getAttachment().getInputStream());
        StreamSource sr2 = new StreamSource(
            response.getAttachment().getInputStream());
        String tmpStr = AttachmentHelper.validateAttachmentData(sr1, sr2,
            "XmlAttachment");
        if (tmpStr != null) {
          TestUtil.logErr(tmpStr);
          result = false;
        }
      } catch (Exception e) {
        result = false;
        TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
        TestUtil.printStackTrace(e);
      }
    } else if (type.equals("image/jpeg")) {
      try {
        Image image1 = javax.imageio.ImageIO
            .read(request.getAttachment().getInputStream());
        Image image2 = javax.imageio.ImageIO
            .read(response.getAttachment().getInputStream());
        if (!AttachmentHelper.compareImages(image1, image2,
            new Rectangle(0, 0, 100, 120), "JpegAttachment"))
          result = false;
      } catch (Exception e) {
        result = false;
        TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
        TestUtil.printStackTrace(e);
      }
    } else if (type.equals("text/plain")) {
      try {
        byte data1[] = new byte[4096];
        byte data2[] = new byte[4096];
        InputStream is = request.getAttachment().getInputStream();
        int count1 = AttachmentHelper.readTheData(is, data1, 4096);
        is = response.getAttachment().getInputStream();
        int count2 = AttachmentHelper.readTheData(is, data2, 4096);
        if (!AttachmentHelper.validateAttachmentData(count1, data1, count2,
            data2, "PlainTextAttachment"))
          result = false;
      } catch (Exception e) {
        result = false;
        TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
        TestUtil.printStackTrace(e);
      }
    }
    return result;
  }

  private boolean ValidateMultipleSwaRefAttachmentsTestCase(
      SwaRefTypeRequest2 request, SwaRefTypeResponse2 response) {
    boolean result = true;
    TestUtil
        .logMsg("---------------------------------------------------------");
    TestUtil
        .logMsg("Validating the request, the response, and the attachments");
    TestUtil
        .logMsg("---------------------------------------------------------");
    try {
      StreamSource sr1 = new StreamSource(
          request.getAttachment1().getInputStream());
      StreamSource sr2 = new StreamSource(
          response.getAttachment1().getInputStream());
      String tmpStr = AttachmentHelper.validateAttachmentData(sr1, sr2,
          "XmlAttachment");
      if (tmpStr != null) {
        TestUtil.logErr(tmpStr);
        result = false;
      }
      byte data1[] = new byte[4096];
      byte data2[] = new byte[4096];
      InputStream is = request.getAttachment2().getInputStream();
      int count1 = AttachmentHelper.readTheData(is, data1, 4096);
      is = response.getAttachment2().getInputStream();
      int count2 = AttachmentHelper.readTheData(is, data2, 4096);
      if (!AttachmentHelper.validateAttachmentData(count1, data1, count2, data2,
          "PlainTextAttachment"))
        result = false;
      Image image1 = javax.imageio.ImageIO
          .read(request.getAttachment3().getInputStream());
      Image image2 = javax.imageio.ImageIO
          .read(response.getAttachment3().getInputStream());
      if (!AttachmentHelper.compareImages(image1, image2,
          new Rectangle(0, 0, 100, 120), "JpegAttachment"))
        result = false;
    } catch (Exception e) {
      result = false;
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    }
    return result;
  }
}
