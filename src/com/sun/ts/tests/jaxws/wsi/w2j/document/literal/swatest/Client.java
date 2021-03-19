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

package com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swatest;

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
import com.sun.ts.tests.jaxws.wsi.requests.SOAPRequests;

import com.sun.ts.lib.harness.*;

public class Client extends ServiceEETest implements SOAPRequests {
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
  private static final String ENDPOINT_URL = "wsidlswatest.endpoint.1";

  private static final String ENDPOINT_URL2 = "wsidlswatest.endpoint.2";

  private static final String WSDLLOC_URL = "wsidlswatest.wsdlloc.1";

  private static final String CTXROOT = "wsidlswatest.ctxroot.1";

  private String surl = null;

  private String file = null;

  private String surl2 = null;

  private String file2 = null;

  private String ctxroot = null;

  private URL wsdlurl = null;

  private static final String NAMESPACEURI = "http://SwaTestService.org/wsdl";

  private static final String SERVICE_NAME = "WSIDLSwaTestService";

  private static final String PORT_NAME = "SwaTestOnePort";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  private DataHandler dh1 = null;

  private DataHandler dh2 = null;

  private DataHandler dh3 = null;

  private DataHandler dh4 = null;

  private DataHandler dh5 = null;

  private URL url1 = null;

  private URL url2 = null;

  private URL url3 = null;

  private URL url4 = null;

  private URL url5 = null;

  static WSIDLSwaTestService service = null;

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
    file2 = JAXWS_Util.getURLFromProp(ENDPOINT_URL2);
    surl2 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file2);
    ctxroot = JAXWS_Util.getURLFromProp(CTXROOT);
    TestUtil.logMsg("Service Endpoint URL: " + surl);
    TestUtil.logMsg("Service Endpoint URL2: " + surl2);
    TestUtil.logMsg("WSDL Location URL:    " + wsdlurl);
    TestUtil.logMsg("Context Root:         " + ctxroot);
  }

  SwaTest1 port = null;

  private SwaTestClient1 client1;

  private SwaTestClient2 client2;

  private void getPortStandalone() throws Exception {
    port = (SwaTest1) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        WSIDLSwaTestService.class, PORT_QNAME, SwaTest1.class);
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
    port = (SwaTest1) service.getPort(SwaTest1.class);
    TestUtil.logMsg("port=" + port);
    TestUtil.logMsg("Obtained port");
    getTargetEndpointAddress(port);
    // JAXWS_Util.setTargetEndpointAddress(port, surl);
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

    client1 = (SwaTestClient1) ClientFactory.getClient(SwaTestClient1.class, p,
        this, service);
    client2 = (SwaTestClient2) ClientFactory.getClient(SwaTestClient2.class, p,
        this, service);
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
        service = (WSIDLSwaTestService) getSharedObject();
        getTestURLs();
        getPortJavaEE();
      }
      TestUtil.logMsg("Create URL's to attachments");
      url1 = ctsurl.getURL("http", hostname, portnum, ctxroot + "/attach.txt");
      url2 = ctsurl.getURL("http", hostname, portnum, ctxroot + "/attach.html");
      url3 = ctsurl.getURL("http", hostname, portnum, ctxroot + "/attach.xml");
      url4 = ctsurl.getURL("http", hostname, portnum, ctxroot + "/attach.jpeg");
      url5 = ctsurl.getURL("http", hostname, portnum,
          ctxroot + "/attach2.jpeg");
      TestUtil.logMsg("url1=" + url1);
      TestUtil.logMsg("url2=" + url2);
      TestUtil.logMsg("url3=" + url3);
      TestUtil.logMsg("url4=" + url4);
      TestUtil.logMsg("url5=" + url5);
      TestUtil.logMsg("Create DataHandler's to attachments");
      dh1 = new DataHandler(url1);
      dh2 = new DataHandler(url2);
      dh3 = new DataHandler(url3);
      dh4 = new DataHandler(javax.imageio.ImageIO.read(url4), "image/jpeg");
      dh5 = new DataHandler(javax.imageio.ImageIO.read(url5), "image/jpeg");
      TestUtil.logMsg("dh1.getContentType()=" + dh1.getContentType());
      TestUtil.logMsg("dh2.getContentType()=" + dh2.getContentType());
      TestUtil.logMsg("dh3.getContentType()=" + dh3.getContentType());
      TestUtil.logMsg("dh4.getContentType()=" + dh4.getContentType());
      TestUtil.logMsg("dh5.getContentType()=" + dh5.getContentType());
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
   * @testName: GetMultipleAttachmentsTest
   *
   * @assertion_ids: WSI:SPEC:R2901; WSI:SPEC:R2907; WSI:SPEC:R2909;
   * WSI:SPEC:R2910; WSI:SPEC:R2911; WSI:SPEC:R2931; WSI:SPEC:R2921;
   * WSI:SPEC:R2926; WSI:SPEC:R2929; WSI:SPEC:R2946; JAXWS:SPEC:10011;
   * WSI:SPEC:R2927; JAXWS:SPEC:2051; JAXWS:SPEC:2052; JAXWS:SPEC:2053;
   * WSI:SPEC:R9801;
   *
   * @test_Strategy: Get multiple attachments. Multiple attachments should be
   * returned in the soap response.
   *
   */
  public void GetMultipleAttachmentsTest() throws Fault {
    TestUtil.logMsg("GetMultipleAttachmentsTest");
    boolean pass = true;

    try {
      InputRequestGet request = new InputRequestGet();
      request.setMimeType1("text/plain");
      request.setMimeType2("text/html");
      request.setUrl1(url1.toString());
      request.setUrl2(url2.toString());
      TestUtil.logMsg("Get 2 attachments (text/plain) and (text/html)");
      Holder<jakarta.activation.DataHandler> attach1 = new Holder<jakarta.activation.DataHandler>();
      Holder<jakarta.activation.DataHandler> attach2 = new Holder<jakarta.activation.DataHandler>();
      Holder<OutputResponse> response = new Holder<OutputResponse>();
      port.getMultipleAttachments(request, response, attach1, attach2);
      if (!ValidateRequestResponseAttachmentsGetTestCase(request,
          response.value, attach1, attach2))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetMultipleAttachmentsTest failed", e);
    }
    if (!pass)
      throw new Fault("GetMultipleAttachmentsTest failed");
  }

  /*
   * @testName: PutMultipleAttachmentsTest
   *
   * @assertion_ids: WSI:SPEC:R2901; WSI:SPEC:R2907; WSI:SPEC:R2909;
   * WSI:SPEC:R2910; WSI:SPEC:R2911; WSI:SPEC:R2931; WSI:SPEC:R2921;
   * WSI:SPEC:R2926; WSI:SPEC:R2929; WSI:SPEC:R2946; JAXWS:SPEC:10011;
   * WSI:SPEC:R2927; JAXWS:SPEC:2051; JAXWS:SPEC:2052; JAXWS:SPEC:2053;
   * WSI:SPEC:R9801;
   *
   * @test_Strategy: Put multiple attachments. Multiple attachments should be
   * sent in the soap request and a status result is returned in the soap
   * response.
   *
   */
  public void PutMultipleAttachmentsTest() throws Fault {
    TestUtil.logMsg("PutMultipleAttachmentsTest");
    boolean pass = true;

    try {
      InputRequestPut request = new InputRequestPut();
      request.setMimeType1("text/plain");
      request.setMimeType2("text/html");
      request.setHeader("notused");
      DataHandler attach1 = dh1;
      DataHandler attach2 = dh2;
      TestUtil.logMsg("Put 2 attachments (text/plain) and (text/html)");
      OutputResponseString response = port.putMultipleAttachments(request,
          attach1, attach2);
      if (!response.getMyString().equals("ok")) {
        TestUtil.logErr(
            "Return status is " + response.getMyString() + ", expected ok");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("PutMultipleAttachmentsTest failed", e);
    }

    if (!pass)
      throw new Fault("PutMultipleAttachmentsTest failed");
  }

  /*
   * @testName: EchoMultipleAttachmentsTest
   *
   * @assertion_ids: WSI:SPEC:R2901; WSI:SPEC:R2907; WSI:SPEC:R2909;
   * WSI:SPEC:R2910; WSI:SPEC:R2911; WSI:SPEC:R2931; WSI:SPEC:R2921;
   * WSI:SPEC:R2926; WSI:SPEC:R2929; WSI:SPEC:R2946; JAXWS:SPEC:10011;
   * WSI:SPEC:R2927; JAXWS:SPEC:2051; JAXWS:SPEC:2052; JAXWS:SPEC:2053;
   * WSI:SPEC:R9801;
   *
   * @test_Strategy: Echo multiple attachments. Multiple attachments should be
   * sent in the soap request and returned in the soap response.
   *
   */
  public void EchoMultipleAttachmentsTest() throws Fault {
    TestUtil.logMsg("EchoMultipleAttachmentsTest");
    boolean pass = true;

    try {
      InputRequest request = new InputRequest();
      request.setMimeType1("text/plain");
      request.setMimeType2("text/html");
      Holder<jakarta.activation.DataHandler> attach1 = new Holder<jakarta.activation.DataHandler>();
      Holder<jakarta.activation.DataHandler> attach2 = new Holder<jakarta.activation.DataHandler>();
      attach1.value = dh1;
      attach2.value = dh2;
      TestUtil.logMsg("Echo 2 attachments (text/plain) and (text/html)");
      OutputResponse response = port.echoMultipleAttachments(request, attach1,
          attach2);
      if (!ValidateRequestResponseAttachmentsEchoTestCase(request, response,
          attach1, attach2))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("EchoMultipleAttachmentsTest failed", e);
    }

    if (!pass)
      throw new Fault("EchoMultipleAttachmentsTest failed");
  }

  /*
   * @testName: EchoNoAttachmentsTest
   *
   * @assertion_ids: WSI:SPEC:R2917; JAXWS:SPEC:10011; WSI:SPEC:R2902;
   * WSI:SPEC:R9801;
   *
   * @test_Strategy: Echo no attachments. No attachments should be sent in the
   * soap request or returned in the soap response.
   *
   */
  public void EchoNoAttachmentsTest() throws Fault {
    TestUtil.logMsg("EchoNoAttachmentsTest");
    boolean pass = true;

    try {
      InputRequestString request = new InputRequestString();
      request.setMyString("Hello");
      TestUtil.logMsg("Echo no attachments");
      OutputResponseString response = port.echoNoAttachments(request);
      if (!response.getMyString().equals(request.getMyString())) {
        TestUtil.logErr("OutputString is not equal to InputString");
        TestUtil.logErr("InputString=" + request.getMyString());
        TestUtil.logErr("OutputString=" + response.getMyString());
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("EchoNoAttachmentsTest failed", e);
    }

    if (!pass)
      throw new Fault("EchoNoAttachmentsTest failed");
  }

  /*
   * @testName: EchoAllAttachmentTypesTest
   *
   * @assertion_ids: WSI:SPEC:R2901; WSI:SPEC:R2907; WSI:SPEC:R2909;
   * WSI:SPEC:R2910; WSI:SPEC:R2911; WSI:SPEC:R2931; WSI:SPEC:R2921;
   * WSI:SPEC:R2926; WSI:SPEC:R2929; WSI:SPEC:R2946; JAXWS:SPEC:10011;
   * WSI:SPEC:R2927; WSI:SPEC:R2912; WSI:SPEC:R2908; WSI:SPEC:R2903;
   * WSI:SPEC:R2904; WSI:SPEC:R2941; WSI:SPEC:R2922; WSI:SPEC:R2902;
   * WSI:SPEC:R2916; WSI:SPEC:R9801;
   *
   * @test_Strategy: Echo all attachment types. Attachments for each supported
   * mime type is sent in the soap request and then returned in the soap
   * response. This test sends and returns attachments for all the supported
   * mime types.
   *
   */
  public void EchoAllAttachmentTypesTest() throws Fault {
    TestUtil.logMsg("EchoAllAttachmentTypesTest");
    boolean pass = true;

    try {
      // Make sure soap logging is off for this test case
      if (modeProperty.equals("standalone")) {
        getTestURLs();
        getPortStandalone();
      } else {
        getTestURLs();
        getPortJavaEE();
      }
      TestUtil.logMsg(
          "Echo all attachments (text/plain), (text/html), (text/xml), (image/jpeg)");
      Holder<jakarta.activation.DataHandler> attach1 = new Holder<jakarta.activation.DataHandler>();
      attach1.value = dh1;
      Holder<jakarta.activation.DataHandler> attach2 = new Holder<jakarta.activation.DataHandler>();
      attach2.value = dh2;
      Holder<javax.xml.transform.Source> attach3 = new Holder<javax.xml.transform.Source>();
      attach3.value = new StreamSource(dh3.getInputStream());
      Holder<java.awt.Image> attach4 = new Holder<java.awt.Image>();
      Holder<java.awt.Image> attach5 = new Holder<java.awt.Image>();
      attach4.value = javax.imageio.ImageIO.read(url4);
      attach5.value = javax.imageio.ImageIO.read(url5);
      VoidRequest request = new VoidRequest();
      OutputResponseAll response = port.echoAllAttachmentTypes(request, attach1,
          attach2, attach3, attach4, attach5);
      if (!ValidateRequestResponseAttachmentsEchoAllTestCase(request, response,
          attach1, attach2, attach3, attach4, attach5))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("EchoAllAttachmentTypesTest failed", e);
    }

    if (!pass)
      throw new Fault("EchoAllAttachmentTypesTest failed");
  }

  /*
   * @testName: EchoAttachmentsAndThrowAFaultTest
   *
   * @assertion_ids: WSI:SPEC:R2913; WSI:SPEC:R2920; WSI:SPEC:R2930;
   * WSI:SPEC:R2946; JAXWS:SPEC:10011; WSI:SPEC:R9801;
   *
   * @test_Strategy: Echo attachments and throw back a soap fault. Multiple
   * attachments should be sent in the soap request and the endpoint should
   * throw back a soap fault.
   *
   */
  public void EchoAttachmentsAndThrowAFaultTest() throws Fault {
    TestUtil.logMsg("EchoAttachmentsAndThrowAFaultTest");
    boolean pass = true;

    try {
      InputRequestThrowAFault request = new InputRequestThrowAFault();
      request.setMimeType1("text/plain");
      request.setMimeType2("text/html");
      Holder<jakarta.activation.DataHandler> attach1 = new Holder<jakarta.activation.DataHandler>();
      Holder<jakarta.activation.DataHandler> attach2 = new Holder<jakarta.activation.DataHandler>();
      attach1.value = dh1;
      attach2.value = dh2;
      TestUtil.logMsg("Echo attachments and throw a fault");
      OutputResponse response = port.echoAttachmentsAndThrowAFault(request,
          attach1, attach2);
      pass = false;
    } catch (MyFault e) {
      TestUtil.logMsg("Caught expected MyFault exception: " + e.getMessage());
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("EchoAttachmentsAndThrowAFaultTest failed", e);
    }

    if (!pass)
      throw new Fault("EchoAttachmentsAndThrowAFaultTest failed");
  }

  /*
   * @testName: EchoAttachmentsWithHeaderTest
   *
   * @assertion_ids: WSI:SPEC:R2905; WSI:SPEC:2906; WSI:SPEC:R2946;
   * JAXWS:SPEC:1011; WSI:SPEC:R2906; WSI:SPEC:R9801;
   *
   * @test_Strategy: Send a header with attachments using the soapbind:header
   * element which must be a child of the root part mime:part element.
   *
   */
  public void EchoAttachmentsWithHeaderTest() throws Fault {
    TestUtil.logMsg("EchoAttachmentsWithHeaderTest");
    boolean pass = true;

    try {
      InputRequestWithHeader request = new InputRequestWithHeader();
      request.setMimeType1("text/plain");
      request.setMimeType2("text/html");
      Holder<jakarta.activation.DataHandler> attach1 = new Holder<jakarta.activation.DataHandler>();
      Holder<jakarta.activation.DataHandler> attach2 = new Holder<jakarta.activation.DataHandler>();
      attach1.value = dh1;
      attach2.value = dh2;
      MyHeader header = new MyHeader();
      header.setMessage("do not throw my fault");
      TestUtil.logMsg("Echo attachments with a header");
      OutputResponse response = port.echoAttachmentsWithHeader(request, header,
          attach1, attach2);
      if (!ValidateRequestResponseAttachmentsEchoWithHeaderTestCase(request,
          response, attach1, attach2))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("EchoAttachmentsWithHeaderTest failed", e);
    }

    if (!pass)
      throw new Fault("EchoAttachmentsWithHeaderTest failed");
  }

  /*
   * @testName: EchoAttachmentsWithHeaderAndThrowAFaultTest
   *
   * @assertion_ids: WSI:SPEC:R2905; WSI:SPEC:2906; WSI:SPEC:2913;
   * WSI:SPEC:R2946; JAXWS:SPEC:10011;
   *
   * @test_Strategy: Send a header with attachments and throw a fault back using
   * the soapbind:fault element.
   */
  public void EchoAttachmentsWithHeaderAndThrowAFaultTest() throws Fault {
    TestUtil.logMsg("EchoAttachmentsWithHeaderAndThrowAFaultTest");
    boolean pass = true;

    try {
      InputRequestWithHeader request = new InputRequestWithHeader();
      request.setMimeType1("text/plain");
      request.setMimeType2("text/html");
      Holder<jakarta.activation.DataHandler> attach1 = new Holder<jakarta.activation.DataHandler>();
      Holder<jakarta.activation.DataHandler> attach2 = new Holder<jakarta.activation.DataHandler>();
      attach1.value = dh1;
      attach2.value = dh2;
      MyHeader header = new MyHeader();
      header.setMessage("do throw a fault");
      TestUtil.logMsg("Echo attachments with a header and throw a fault");
      OutputResponse response = port.echoAttachmentsWithHeader(request, header,
          attach1, attach2);
      pass = false;
    } catch (MyFault e) {
      TestUtil.logMsg("Caught expected MyFault exception: " + e.getMessage());
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("EchoAttachmentsWithHeaderAndThrowAFaultTest failed", e);
    }

    if (!pass)
      throw new Fault("EchoAttachmentsWithHeaderAndThrowAFaultTest failed");
  }

  /*
   * @testName: VerifyPutOfSOAPEnvelopesInAttachmentsTest
   *
   * @assertion_ids: WSI:SPEC:R2919; JAXWS:SPEC:10011; WSI:SPEC:R2947
   * WSI:SPEC:R9801;
   *
   * @test_Strategy: Put multiple attachments. Multiple attachments should be
   * sent in the soap request and a status results is returned in the soap
   * response. The attachments contain SOAP Envelopes.
   *
   */
  public void VerifyPutOfSOAPEnvelopesInAttachmentsTest() throws Fault {
    TestUtil.logMsg("VerifyPutOfSOAPEnvelopesInAttachmentsTest");
    boolean pass = true;

    try {
      InputRequestPut request = new InputRequestPut();
      request.setMimeType1("text/xml");
      request.setMimeType2("text/xml");
      request.setHeader("notused");
      StreamSource xmlSrc1 = new StreamSource(new StringReader(R0007_REQUEST));
      StreamSource xmlSrc2 = new StreamSource(new StringReader(R1011_REQUEST));
      DataHandler attach1 = new DataHandler(xmlSrc1, "text/xml");
      DataHandler attach2 = new DataHandler(xmlSrc2, "text/xml");
      TestUtil.logMsg("Put 2 attachments that contain SOAP envelopes");
      OutputResponseString response = port.putMultipleAttachments(request,
          attach1, attach2);
      if (!response.getMyString().equals("ok")) {
        TestUtil.logErr(
            "Return status is " + response.getMyString() + ", expected ok");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("VerifyPutOfSOAPEnvelopesInAttachmentsTest failed", e);
    }

    if (!pass)
      throw new Fault("VerifyPutOfSOAPEnvelopesInAttachmentsTest failed");
  }

  /*
   * @testName: VerifyUTF8EncodingOfRootPartWithoutAttachments
   *
   * @assertion_ids: WSI:SPEC:R2915; JAXWS:SPEC:10011; WSI:SPEC:R9801;
   *
   * @test_Strategy: Test UTF8 encoding of the root part of a multipart/related
   * message without attachments.
   *
   */
  public void VerifyUTF8EncodingOfRootPartWithoutAttachments() throws Fault {
    TestUtil.logMsg("VerifyUTF8EncodingOfRootPartWithoutAttachments");
    boolean pass = true;
    SOAPMessage request;
    String requestAsString;
    SOAPMessage response;
    StreamSource ssrc;

    try {
      TestUtil.logMsg("Construct SOAP RPC request without attachments");
      ssrc = new StreamSource(new ByteArrayInputStream(
          R2915_UTF8_REQUEST_NO_ATTACHMENTS_DOCLIT.getBytes()));
      request = MessageFactory.newInstance().createMessage();
      request.getSOAPPart().setContent(ssrc);
    } catch (Exception e) {
      throw new Fault("Unable to construct SOAP message request (R2915)", e);
    }
    try {
      TestUtil.logMsg(
          "Send SOAP RPC request without attachments using UTF8 encoding");
      Charset cs = Charset.forName("UTF-8");
      response = client1.makeSaajRequest(request, cs);
    } catch (Exception e) {
      throw new Fault("Unable to invoke RPC operation (R2915)", e);
    }
    try {
      SOAPBody body = response.getSOAPPart().getEnvelope().getBody();
      if (body.hasFault()) {
        throw new Fault("Unexpected SOAP fault returned in response (R2915)");
      }
    } catch (SOAPException e) {
      throw new Fault("Invalid SOAP message returned (R2915)", e);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("VerifyUTF8EncodingOfRootPartWithoutAttachments failed",
          e);
    }

    if (!pass)
      throw new Fault("VerifyUTF8EncodingOfRootPartWithoutAttachments failed");
  }

  /*
   * @testName: VerifyUTF16EncodingOfRootPartWithoutAttachments
   *
   * @assertion_ids: WSI:SPEC:R2915; JAXWS:SPEC:10011;
   *
   * @test_Strategy: Test UTF16 encoding of the root part of a multipart/related
   * message without attachments.
   *
   */
  public void VerifyUTF16EncodingOfRootPartWithoutAttachments() throws Fault {
    TestUtil.logMsg("VerifyUTF16EncodingOfRootPartWithoutAttachments");
    boolean pass = true;
    SOAPMessage response;

    try {
      TestUtil.logMsg(
          "Send SOAP RPC request without attachments using UTF16 encoding");
      Charset cs = Charset.forName("UTF-16");
      response = client1
          .makeSaajRequest(R2915_UTF16_REQUEST_NO_ATTACHMENTS_DOCLIT, cs);
    } catch (Exception e) {
      throw new Fault("Unable to invoke RPC operation (R2915)", e);
    }
    try {
      SOAPBody body = response.getSOAPPart().getEnvelope().getBody();
      if (body.hasFault()) {
        throw new Fault("Unexpected SOAP fault returned in response (R2915)");
      }
    } catch (SOAPException e) {
      throw new Fault("Invalid SOAP message returned (R2915)", e);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("VerifyUTF16EncodingOfRootPartWithoutAttachments failed",
          e);
    }

    if (!pass)
      throw new Fault("VerifyUTF16EncodingOfRootPartWithoutAttachments failed");
  }

  /*
   * @testName: VerifyRequestContentTypeHttpHeaderWithAttachments
   *
   * @assertion_ids: WSI:SPEC:R2925; WSI:SPEC:R2932; WSI:SPEC:R2945;
   * JAXWS:SPEC:10011; WSI:SPEC:R9801;
   *
   * @test_Strategy: Verify that the Content-Type HTTP header is correct in the
   * SOAP request.
   *
   */
  public void VerifyRequestContentTypeHttpHeaderWithAttachments() throws Fault {
    TestUtil.logMsg("VerifyRequestContentTypeHttpHeaderWithAttachments");
    boolean pass = true;
    String expected = "ok";
    try {
      InputRequestPut request = new InputRequestPut();
      request.setMimeType1("text/plain");
      request.setMimeType2("text/html");
      request.setHeader("notused");
      DataHandler attach1 = dh1;
      DataHandler attach2 = dh2;
      TestUtil.logMsg("Send SOAP RPC request with 2 attachments");
      OutputResponseString response = client2.putMultipleAttachments(request,
          attach1, attach2);
      String actual = response.getMyString();
      if (!actual.equals(expected)) {
        TestUtil.logErr("Did not get expected result");
        TestUtil.logErr("Expected=" + expected);
        TestUtil.logErr("Actual=" + actual);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(
          "VerifyRequestContentTypeHttpHeaderWithAttachments failed", e);
    }

    if (!pass)
      throw new Fault(
          "VerifyRequestContentTypeHttpHeaderWithAttachments failed");
  }

  /*
   * @testName: VerifyRequestContentTypeHttpHeaderWithoutAttachments
   *
   * @assertion_ids: WSI:SPEC:R2917; WSI:SPEC:R2932; WSI:SPEC:R2945;
   * JAXWS:SPEC:10011; WSI:SPEC:R9801;
   *
   * @test_Strategy: Verify that the Content-Type HTTP header is correct in the
   * SOAP request.
   *
   */
  public void VerifyRequestContentTypeHttpHeaderWithoutAttachments()
      throws Fault {
    TestUtil.logMsg("VerifyRequestContentTypeHttpHeaderWithoutAttachments");
    boolean pass = true;

    String expected = "Hello";
    try {
      InputRequestString request = new InputRequestString();
      request.setMyString(expected);
      TestUtil.logMsg("Send SOAP RPC request without attachments");
      OutputResponseString response = client2.echoNoAttachments(request);
      String actual = response.getMyString();
      if (!actual.equals(expected)) {
        TestUtil.logErr("Did not get expected result");
        TestUtil.logErr("Expected=" + expected);
        TestUtil.logErr("Actual=" + actual);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(
          "VerifyRequestContentTypeHttpHeaderWithoutAttachments failed", e);
    }

    if (!pass)
      throw new Fault(
          "VerifyRequestContentTypeHttpHeaderWithoutAttachments failed");
  }

  /*
   * @testName: VerifyResponseContentTypeHttpHeaderWithAttachments
   *
   * @assertion_ids: WSI:SPEC:R2925; JAXWS:SPEC:10011; WSI:SPEC:R9801;
   *
   * @test_Strategy: Verify that the Content-Type HTTP header is correct in the
   * SOAP response.
   *
   */
  public void VerifyResponseContentTypeHttpHeaderWithAttachments()
      throws Fault {
    TestUtil.logMsg("VerifyResponseContentTypeHttpHeaderWithAttachments");
    boolean pass = true;
    SOAPMessage request = null;
    SOAPMessage response = null;

    try {
      TestUtil.logMsg("Construct SOAP RPC request to get 2 attachments");
      String requestString = doSubstitution(R2925_REQUEST_DOCLIT);
      StreamSource ssrc = new StreamSource(
          new ByteArrayInputStream(requestString.getBytes()));
      request = MessageFactory.newInstance().createMessage();
      request.getSOAPPart().setContent(ssrc);
      InputStream is = client1.makeHTTPRequest(requestString);
      String contentTypeHeader = client1.getResponseHeader("Content-Type");
      int statusCode = client1.getStatusCode();
      TestUtil.logMsg("HTTP header Content-Type = " + contentTypeHeader);
      String mediaType = null;
      if (contentTypeHeader.indexOf("multipart/related") == -1)
        mediaType = "text/xml";
      else
        mediaType = "multipart/related";
      if (contentTypeHeader.indexOf("multipart/related") == -1) {
        TestUtil.logErr("HTTP header Content-Type is incorrect <got:"
            + mediaType + ", expected:multipart/related>");
        pass = false;
      } else
        TestUtil.logMsg("HTTP header Content-Type is correct: " + mediaType);
      if (statusCode < 200 || statusCode > 299) {
        TestUtil.logErr("Unexpected HTTP status code of: " + statusCode);
        pass = false;
      }
    } catch (Exception e) {
      throw new Fault("Unable to construct SOAP message request (R2925)", e);
    }

    if (!pass)
      throw new Fault(
          "VerifyResponseContentTypeHttpHeaderWithAttachments failed");
  }

  /*
   * @testName: VerifyResponseContentTypeHttpHeaderWithoutAttachments
   *
   * @assertion_ids: WSI:SPEC:R2917; JAXWS:SPEC:10011; WSI:SPEC:R9801;
   *
   * @test_Strategy: Verify that the Content-Type HTTP header is correct in the
   * SOAP response.
   *
   */
  public void VerifyResponseContentTypeHttpHeaderWithoutAttachments()
      throws Fault {
    TestUtil.logMsg("VerifyResponseContentTypeHttpHeaderWithoutAttachments");
    boolean pass = true;
    SOAPMessage request = null;
    SOAPMessage response = null;

    try {
      TestUtil.logMsg("Construct SOAP RPC request to get no attachments");
      StreamSource ssrc = new StreamSource(
          new ByteArrayInputStream(R2917_REQUEST_DOCLIT.getBytes()));
      request = MessageFactory.newInstance().createMessage();
      request.getSOAPPart().setContent(ssrc);
      InputStream is = client1.makeHTTPRequest(R2917_REQUEST_DOCLIT);
      String contentTypeHeader = client1.getResponseHeader("Content-Type");
      int statusCode = client1.getStatusCode();
      TestUtil.logMsg("HTTP header Content-Type = " + contentTypeHeader);
      String mediaType = null;
      if (contentTypeHeader.indexOf("multipart/related") == -1)
        mediaType = "text/xml";
      else
        mediaType = "multipart/related";
      if (contentTypeHeader.indexOf("multipart/related") == -1
          && contentTypeHeader.indexOf("text/xml") == -1) {
        TestUtil.logErr("HTTP header Content-Type is incorrect <got:"
            + mediaType + ", expected:multipart/related or text/xml>");
        pass = false;
      } else
        TestUtil.logMsg("HTTP header Content-Type is correct: " + mediaType);
      if (statusCode < 200 || statusCode > 299) {
        TestUtil.logErr("Unexpected HTTP status code of: " + statusCode);
        pass = false;
      }
    } catch (Exception e) {
      throw new Fault("Unable to construct SOAP message request (R2917)", e);
    }

    if (!pass)
      throw new Fault(
          "VerifyResponseContentTypeHttpHeaderWithoutAttachments failed");
  }

  /*
   * @testName: VerifyRequestContentTransferEncodingMimeHeadersWithAttachments
   *
   * @assertion_ids: WSI:SPEC:R2934; WSI:SPEC:R2935; JAXWS:SPEC:10011;
   * WSI:SPEC:R9801;
   *
   * @test_Strategy: Verify that the Content-Transfer-Encoding mime header(s) if
   * set is correct in the SOAP request.
   *
   */
  public void VerifyRequestContentTransferEncodingMimeHeadersWithAttachments()
      throws Fault {
    TestUtil.logMsg(
        "VerifyRequestContentTransferEncodingMimeHeadersWithAttachments");
    boolean pass = true;

    try {
      InputRequestPut request = new InputRequestPut();
      request.setMimeType1("text/plain");
      request.setMimeType2("text/html");
      request.setHeader("Check-Content-Transfer-Encoding");
      DataHandler attach1 = dh1;
      DataHandler attach2 = dh2;
      TestUtil.logMsg("Send SOAP RPC request with 2 attachments");
      OutputResponseString response = client2.putMultipleAttachments(request,
          attach1, attach2);
      String result = response.getMyString();
      if ((result.indexOf("7bit") < 0) && (result.indexOf("8bit") < 0)
          && (result.indexOf("binary") < 0)
          && (result.indexOf("quoted-printable") < 0)
          && (result.indexOf("base64") < 0)
          && (result.indexOf("FAILED") >= 0)) {

        TestUtil.logErr(
            "HTTP header Content-Transfer-Encoding is incorrect <got:" + result
                + ", expected: 7bit, 8bit, binary, quoted-printable, base64>");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(
          "VerifyRequestContentTransferEncodingMimeHeadersWithAttachments failed",
          e);
    }

    if (!pass)
      throw new Fault(
          "VerifyRequestContentTransferEncodingMimeHeadersWithAttachments failed");
  }

  /*******************************************************************************
   * Validate request, response and attachments (getMultipleAttachments)
   ******************************************************************************/
  private boolean ValidateRequestResponseAttachmentsGetTestCase(
      InputRequestGet request, OutputResponse response,
      Holder<jakarta.activation.DataHandler> attach1,
      Holder<jakarta.activation.DataHandler> attach2) {
    boolean result = true;
    TestUtil
        .logMsg("---------------------------------------------------------");
    TestUtil
        .logMsg("Validating the request, the response, and the attachments");
    TestUtil
        .logMsg("---------------------------------------------------------");
    TestUtil.logMsg("Check if the mime types are correct");
    if (!response.getMimeType1().equals(request.getMimeType1())) {
      TestUtil.logErr("MimeType1 is not equal in request and response");
      TestUtil.logErr("Request MimeType1 = " + request.getMimeType1());
      TestUtil.logErr("Response MimeType1 = " + response.getMimeType1());
      result = false;
    }
    if (!response.getMimeType2().equals(request.getMimeType2())) {
      TestUtil.logErr("MimeType2 is not equal in request and response");
      TestUtil.logErr("Request MimeType2 = " + request.getMimeType2());
      TestUtil.logErr("Response MimeType2 = " + response.getMimeType2());
      result = false;
    } else {
      TestUtil.logMsg("The mime types are correct");
    }
    TestUtil.logMsg("Check if the response result is correct");
    if (!response.getResult().equals("ok")) {
      TestUtil.logErr("Return status is " + response + ", expected ok");
      TestUtil.logErr("Return Reason is: " + response.getReason());
      result = false;
    } else {
      TestUtil.logMsg("The response result is correct");
    }
    try {
      TestUtil.logMsg("Check if the attachment contents are correct");
      DataHandler dh1 = new DataHandler(new URL(request.getUrl1()));
      DataHandler dh2 = new DataHandler(new URL(request.getUrl2()));
      byte data1[] = new byte[4096];
      byte data2[] = new byte[4096];
      InputStream is = dh1.getInputStream();
      int count1 = AttachmentHelper.readTheData(is, data1, 4096);
      is = attach1.value.getInputStream();
      int count2 = AttachmentHelper.readTheData(is, data2, 4096);
      if (!AttachmentHelper.validateAttachmentData(count1, data1, count2, data2,
          "Attachment1"))
        result = false;
      is = dh2.getInputStream();
      count1 = AttachmentHelper.readTheData(is, data1, 4096);
      is = attach2.value.getInputStream();
      count2 = AttachmentHelper.readTheData(is, data2, 4096);
      if (!AttachmentHelper.validateAttachmentData(count1, data1, count2, data2,
          "Attachment2"))
        result = false;
      TestUtil.logMsg("The attachment contents are equal");
    } catch (Exception e) {
      result = false;
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    }
    return result;
  }

  /*******************************************************************************
   * Validate request, response and attachments (echoMultipleAttachments)
   ******************************************************************************/
  private boolean ValidateRequestResponseAttachmentsEchoTestCase(
      InputRequest request, OutputResponse response,
      Holder<jakarta.activation.DataHandler> attach1,
      Holder<jakarta.activation.DataHandler> attach2) {
    boolean result = true;
    TestUtil
        .logMsg("---------------------------------------------------------");
    TestUtil
        .logMsg("Validating the request, the response, and the attachments");
    TestUtil
        .logMsg("---------------------------------------------------------");
    TestUtil.logMsg("Check if the mime types are correct");
    if (!response.getMimeType1().equals(request.getMimeType1())) {
      TestUtil.logErr("MimeType1 is not equal in request and response");
      TestUtil.logErr("Request MimeType1 = " + request.getMimeType1());
      TestUtil.logErr("Response MimeType1 = " + response.getMimeType1());
      result = false;
    }
    if (!response.getMimeType2().equals(request.getMimeType2())) {
      TestUtil.logErr("MimeType2 is not equal in request and response");
      TestUtil.logErr("Request MimeType2 = " + request.getMimeType2());
      TestUtil.logErr("Response MimeType2 = " + response.getMimeType2());
      result = false;
    } else {
      TestUtil.logMsg("The mime types are correct");
    }
    TestUtil.logMsg("Check if the response result is correct");
    if (!response.getResult().equals("ok")) {
      TestUtil.logErr("Return status is " + response + ", expected ok");
      TestUtil.logErr("Return Reason is: " + response.getReason());
      result = false;
    } else {
      TestUtil.logMsg("The response result is correct");
    }
    try {
      TestUtil.logMsg("Check if the attachment contents are correct");
      DataHandler dh1 = new DataHandler(url1);
      byte data1[] = new byte[4096];
      byte data2[] = new byte[4096];
      InputStream is = dh1.getInputStream();
      int count1 = AttachmentHelper.readTheData(is, data1, 4096);
      is = attach1.value.getInputStream();
      int count2 = AttachmentHelper.readTheData(is, data2, 4096);
      if (!AttachmentHelper.validateAttachmentData(count1, data1, count2, data2,
          "Attachment1"))
        result = false;

      dh1 = new DataHandler(url2);
      is = dh1.getInputStream();
      count1 = AttachmentHelper.readTheData(is, data1, 4096);
      is = attach2.value.getInputStream();
      count2 = AttachmentHelper.readTheData(is, data2, 4096);
      if (!AttachmentHelper.validateAttachmentData(count1, data1, count2, data2,
          "Attachment2"))
        result = false;
    } catch (Exception e) {
      result = false;
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    }
    return result;
  }

  /*******************************************************************************
   * Validate request, response and attachments (echoAttachmentsWithHeader)
   ******************************************************************************/
  private boolean ValidateRequestResponseAttachmentsEchoWithHeaderTestCase(
      InputRequestWithHeader request, OutputResponse response,
      Holder<jakarta.activation.DataHandler> attach1,
      Holder<jakarta.activation.DataHandler> attach2) {
    boolean result = true;
    TestUtil
        .logMsg("---------------------------------------------------------");
    TestUtil
        .logMsg("Validating the request, the response, and the attachments");
    TestUtil
        .logMsg("---------------------------------------------------------");
    TestUtil.logMsg("Check if the mime types are correct");
    if (!response.getMimeType1().equals(request.getMimeType1())) {
      TestUtil.logErr("MimeType1 is not equal in request and response");
      TestUtil.logErr("Request MimeType1 = " + request.getMimeType1());
      TestUtil.logErr("Response MimeType1 = " + response.getMimeType1());
      result = false;
    }
    if (!response.getMimeType2().equals(request.getMimeType2())) {
      TestUtil.logErr("MimeType2 is not equal in request and response");
      TestUtil.logErr("Request MimeType2 = " + request.getMimeType2());
      TestUtil.logErr("Response MimeType2 = " + response.getMimeType2());
      result = false;
    } else {
      TestUtil.logMsg("The mime types are correct");
    }
    TestUtil.logMsg("Check if the response result is correct");
    if (!response.getResult().equals("ok")) {
      TestUtil.logErr("Return status is " + response + ", expected ok");
      TestUtil.logErr("Return Reason is: " + response.getReason());
      result = false;
    } else {
      TestUtil.logMsg("The response result is correct");
    }
    try {
      TestUtil.logMsg("Check if the attachment contents are correct");
      DataHandler dh1 = new DataHandler(url1);
      DataHandler dh2 = new DataHandler(url2);
      byte data1[] = new byte[4096];
      byte data2[] = new byte[4096];
      InputStream is = dh1.getInputStream();
      int count1 = AttachmentHelper.readTheData(is, data1, 4096);
      is = attach1.value.getInputStream();
      int count2 = AttachmentHelper.readTheData(is, data2, 4096);
      if (!AttachmentHelper.validateAttachmentData(count1, data1, count2, data2,
          "Attachment1"))
        result = false;
      is = dh2.getInputStream();
      count1 = AttachmentHelper.readTheData(is, data1, 4096);
      data2 = new byte[4096];
      is = attach2.value.getInputStream();
      count2 = AttachmentHelper.readTheData(is, data2, 4096);
      if (!AttachmentHelper.validateAttachmentData(count1, data1, count2, data2,
          "Attachment2"))
        result = false;
      TestUtil.logMsg("The attachment contents are equal");
    } catch (Exception e) {
      result = false;
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    }
    return result;
  }

  /*******************************************************************************
   * Validate request, response and attachments (echoAllAttachmentTypes)
   ******************************************************************************/
  private boolean ValidateRequestResponseAttachmentsEchoAllTestCase(
      VoidRequest request, OutputResponseAll response,
      Holder<jakarta.activation.DataHandler> attach1,
      Holder<jakarta.activation.DataHandler> attach2,
      Holder<javax.xml.transform.Source> attach3,
      Holder<java.awt.Image> attach4, Holder<java.awt.Image> attach5) {
    boolean result = true;
    TestUtil
        .logMsg("---------------------------------------------------------");
    TestUtil
        .logMsg("Validating the request, the response, and the attachments");
    TestUtil
        .logMsg("---------------------------------------------------------");
    TestUtil.logMsg("Check if the response result is correct");
    if (!response.getResult().equals("ok")) {
      TestUtil.logErr("Return status is " + response + ", expected ok");
      TestUtil.logErr("Return Reason is: " + response.getReason());
      result = false;
    } else {
      TestUtil.logMsg("The response result is correct");
    }
    try {
      TestUtil.logMsg("Check if the attachment contents are correct");
      DataHandler dh1 = new DataHandler(url1);
      byte data1[] = new byte[4096];
      byte data2[] = new byte[4096];
      InputStream is = dh1.getInputStream();
      int count1 = AttachmentHelper.readTheData(is, data1, 4096);
      is = attach1.value.getInputStream();
      int count2 = AttachmentHelper.readTheData(is, data2, 4096);
      if (!AttachmentHelper.validateAttachmentData(count1, data1, count2, data2,
          "Attachment1"))
        result = false;

      dh1 = new DataHandler(url2);
      is = dh1.getInputStream();
      count1 = AttachmentHelper.readTheData(is, data1, 4096);
      is = attach2.value.getInputStream();
      count2 = AttachmentHelper.readTheData(is, data2, 4096);
      if (!AttachmentHelper.validateAttachmentData(count1, data1, count2, data2,
          "Attachment2"))
        result = false;

      dh1 = new DataHandler(url3);
      StreamSource sr1 = new StreamSource(dh1.getInputStream());
      StreamSource sr2 = (StreamSource) attach3.value;
      String tmpStr = AttachmentHelper.validateAttachmentData(sr1, sr2,
          "Attachment3");
      if (tmpStr != null) {
        TestUtil.logErr(tmpStr);
        result = false;
      } else
        TestUtil.logMsg("Attachment3 xml content is equal in attachment");

      Image image1 = javax.imageio.ImageIO.read(url4);
      Image image2 = attach4.value;
      if (!AttachmentHelper.compareImages(image1, image2,
          new Rectangle(0, 0, 100, 120), "Attachment4"))
        result = false;

      image1 = javax.imageio.ImageIO.read(url5);
      image2 = attach5.value;
      if (!AttachmentHelper.compareImages(image1, image2,
          new Rectangle(0, 0, 100, 120), "Attachment5"))
        result = false;
    } catch (Exception e) {
      result = false;
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    }
    return result;
  }

  private String doSubstitution(String s) {
    String tmp = s.replaceAll("localhost", hostname);
    tmp = tmp.replaceAll("8080", new Integer(portnum).toString());
    String modified = tmp.replaceAll("/WSIDLSwaTest", ctxroot);
    return modified;
  }
}
