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

package com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.httptest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import jakarta.xml.ws.*;
import javax.xml.namespace.QName;

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

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.httptest.";

  // service and port information
  private static final String NAMESPACEURI = "http://httptestservice.org/wsdl";

  private static final String SERVICE_NAME = "HttpTestService";

  private static final String PORT_NAME = "HelloPort";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  private static final Class PORT_CLASS = Hello.class;

  private TSURL ctsurl = new TSURL();

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "w2jrlhttptest.endpoint.1";

  private static final String WSDLLOC_URL = "w2jrlhttptest.wsdlloc.1";

  private String url = null;

  private URL wsdlurl = null;

  Hello port = null;

  static HttpTestService service = null;

  /************************************************************************
   * Below are defined good and bad SOAP messages which are sent to a web *
   * service endpoint (HttpTestService) over a HttpURLConnection in order * to
   * verify whether we get the correct HTTP status codes as required * and
   * specified in the WSI Basic Profile Version 1.0 Specification. *
   ************************************************************************/
  // expect 2xx http status code
  String GoodSoapMessage = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:enc=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:ns=\"http://httptestservice.org/wsdl\"><soap:Body><ns:hello><parameters><string>World</string></parameters></ns:hello></soap:Body></soap:Envelope>";

  // expect 2xx http status code
  String GoodSoapMessageNoXMLDeclaration = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:enc=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:ns=\"http://httptestservice.org/wsdl\"><soap:Body><ns:hello><parameters><string>World</string></parameters></ns:hello></soap:Body></soap:Envelope>";

  // expect 2xx http status code
  String GoodOneWaySoapMessage = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:enc=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:ns=\"http://httptestservice.org/wsdl\"><soap:Body><ns:helloOneWay><parameters><string>World</string></parameters></ns:helloOneWay></soap:Body></soap:Envelope>";

  // expect 2xx http status code
  String GoodOneWaySoapMessageNoXMLDeclaration = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:enc=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:ns=\"http://httptestservice.org/wsdl\"><soap:Body><ns:helloOneWay><parameters><string>World</string></parameters></ns:helloOneWay></soap:Body></soap:Envelope>";

  // expect 2xx http status code
  String SoapMessageUsingUTF16Encoding = "<?xml version=\"1.0\" encoding=\"utf-16\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:enc=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:ns=\"http://httptestservice.org/wsdl\"><soap:Body><ns:hello><parameters><string>World</string></parameters></ns:hello></soap:Body></soap:Envelope>";

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXWS_Util.getURLFromProp(ENDPOINT_URL);
    url = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(WSDLLOC_URL);
    wsdlurl = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint URL: " + url);
    TestUtil.logMsg("WSDL Location URL:    " + wsdlurl);
  }

  private void getPortStandalone() throws Exception {
    port = (Hello) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        HttpTestService.class, PORT_QNAME, Hello.class);
    JAXWS_Util.setTargetEndpointAddress(port, url);
  }

  private void getPortJavaEE() throws Exception {
    TestUtil.logMsg("Obtain service via WebServiceRef annotation");
    TestUtil.logMsg("service=" + service);
    port = (Hello) service.getPort(Hello.class);
    TestUtil.logMsg("port=" + port);
    TestUtil.logMsg("Obtained port");
    JAXWS_Util.dumpTargetEndpointAddress(port);
    // JAXWS_Util.setSOAPLogging(port);
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
      TestUtil.logMsg("Creating stub instance ...");
      modeProperty = p.getProperty(MODEPROP);
      if (modeProperty.equals("standalone")) {
        getTestURLs();
        getPortStandalone();
      } else {
        TestUtil.logMsg(
            "WebServiceRef is not set in Client (get it from specific vehicle)");
        service = (HttpTestService) getSharedObject();
        getTestURLs();
        getPortJavaEE();
      }
    } catch (Exception e) {
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
   * @testName: TestGoodSoapMessage
   *
   * @assertion_ids: WSI:SPEC:R1125; WSI:SPEC:R1111; WSI:SPEC:R4004;
   * WS4EE:SPEC:113; WS4EE:SPEC:114; WS4EE:SPEC:115; WS4EE:SPEC:117;
   * WS4EE:SPEC:213; WS4EE:SPEC:219; WS4EE:SPEC:221; WS4EE:SPEC:223;
   * WS4EE:SPEC:224; WS4EE:SPEC:228; WS4EE:SPEC:248; WS4EE:SPEC:249;
   * WS4EE:SPEC:183; WS4EE:SPEC:184; WS4EE:SPEC:185; WS4EE:SPEC:186;
   * WS4EE:SPEC:187; WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:5000;
   * WS4EE:SPEC:5002;
   *
   * @test_Strategy: Send a good SOAP RPC request over an HttpURLConnection.
   * Verify that we get a correct HTTP status code of 2xx.
   */
  public void TestGoodSoapMessage() throws Fault {
    boolean pass = true;
    Iterator iterator = null;
    try {
      TestUtil.logMsg("TestGoodSoapMessage");
      TestUtil.logMsg("Send good SOAP RPC request (expect 2xx status code)");
      HttpURLConnection conn = openHttpConnection(url);
      int httpStatusCode = sendRequest(conn, GoodSoapMessage, "utf-8");
      closeHttpConnection(conn);
      if (httpStatusCode < 200 || httpStatusCode > 299) {
        TestUtil
            .logErr("Expected 2xx status code, instead got " + httpStatusCode);
        pass = false;
      } else
        TestUtil
            .logMsg("Received expected 2xx status code of " + httpStatusCode);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("TestGoodSoapMessage failed", e);
    }

    if (!pass)
      throw new Fault("TestGoodSoapMessage failed");
  }

  /*
   * @testName: TestGoodSoapMessageNoXMLDeclaration
   *
   * @assertion_ids: WSI:SPEC:R1125; WSI:SPEC:R1111; WS4EE:SPEC:113;
   * WS4EE:SPEC:114; WS4EE:SPEC:115; WS4EE:SPEC:117; WS4EE:SPEC:213;
   * WS4EE:SPEC:219; WS4EE:SPEC:221; WS4EE:SPEC:223; WS4EE:SPEC:224;
   * WS4EE:SPEC:228; WS4EE:SPEC:248; WS4EE:SPEC:249; WS4EE:SPEC:183;
   * WS4EE:SPEC:184; WS4EE:SPEC:185; WS4EE:SPEC:186; WS4EE:SPEC:187;
   * WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:5000; WS4EE:SPEC:5002;
   *
   * @test_Strategy: Send a good SOAP RPC request over an HttpURLConnection.
   * Soap message does not contain the XML declaration. Verify that we get a
   * correct HTTP status code of 2xx.
   */
  public void TestGoodSoapMessageNoXMLDeclaration() throws Fault {
    boolean pass = true;
    Iterator iterator = null;
    try {
      TestUtil.logMsg("TestGoodSoapMessageNoXMLDeclaration");
      TestUtil.logMsg("Send good SOAP RPC request (expect 2xx status code)");
      HttpURLConnection conn = openHttpConnection(url);
      int httpStatusCode = sendRequest(conn, GoodSoapMessageNoXMLDeclaration,
          "utf-8");
      closeHttpConnection(conn);
      if (httpStatusCode < 200 || httpStatusCode > 299) {
        TestUtil
            .logErr("Expected 2xx status code, instead got " + httpStatusCode);
        pass = false;
      } else
        TestUtil
            .logMsg("Received expected 2xx status code of " + httpStatusCode);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("TestGoodSoapMessageNoXMLDeclaration failed", e);
    }

    if (!pass)
      throw new Fault("TestGoodSoapMessageNoXMLDeclaration failed");
  }

  /*
   * @testName: TestGoodOneWaySoapMessage
   *
   * @assertion_ids: WSI:SPEC:R1125; WSI:SPEC:R1111; WSI:SPEC:R1112;
   * WSI:SPEC:R4004; JAXWS:SPEC:11005; JAXWS:SPEC:10016; WS4EE:SPEC:113;
   * WS4EE:SPEC:114; WS4EE:SPEC:115; WS4EE:SPEC:117; WS4EE:SPEC:213;
   * WS4EE:SPEC:219; WS4EE:SPEC:221; WS4EE:SPEC:223; WS4EE:SPEC:224;
   * WS4EE:SPEC:228; WS4EE:SPEC:248; WS4EE:SPEC:249; WS4EE:SPEC:183;
   * WS4EE:SPEC:184; WS4EE:SPEC:185; WS4EE:SPEC:186; WS4EE:SPEC:187;
   * WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:5000; WS4EE:SPEC:5002;
   *
   * @test_Strategy: Send a good SOAP RPC request over an HttpURLConnection.
   * Verify that we get a correct HTTP status code of 2xx.
   */
  public void TestGoodOneWaySoapMessage() throws Fault {
    boolean pass = true;
    Iterator iterator = null;
    try {
      TestUtil.logMsg("TestGoodOneWaySoapMessage");
      TestUtil.logMsg("Send good SOAP RPC request (expect 2xx status code)");
      HttpURLConnection conn = openHttpConnection(url);
      int httpStatusCode = sendRequest(conn, GoodOneWaySoapMessage, "utf-8");
      closeHttpConnection(conn);
      if (httpStatusCode < 200 || httpStatusCode > 299) {
        TestUtil
            .logErr("Expected 2xx status code, instead got " + httpStatusCode);
        pass = false;
      } else
        TestUtil
            .logMsg("Received expected 2xx status code of " + httpStatusCode);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("TestGoodOneWaySoapMessage failed", e);
    }

    if (!pass)
      throw new Fault("TestGoodOneWaySoapMessage failed");
  }

  /*
   * @testName: TestGoodOneWaySoapMessageNoXMLDeclaration
   *
   * @assertion_ids: WSI:SPEC:R1125; WSI:SPEC:R1111; WSI:SPEC:R1112;
   * JAXWS:SPEC:11005; JAXWS:SPEC:10016; WS4EE:SPEC:113; WS4EE:SPEC:114;
   * WS4EE:SPEC:115; WS4EE:SPEC:117; WS4EE:SPEC:213; WS4EE:SPEC:219;
   * WS4EE:SPEC:221; WS4EE:SPEC:223; WS4EE:SPEC:224; WS4EE:SPEC:228;
   * WS4EE:SPEC:248; WS4EE:SPEC:249; WS4EE:SPEC:183; WS4EE:SPEC:184;
   * WS4EE:SPEC:185; WS4EE:SPEC:186; WS4EE:SPEC:187; WS4EE:SPEC:4000;
   * WS4EE:SPEC:4002; WS4EE:SPEC:5000; WS4EE:SPEC:5002;
   *
   * @test_Strategy: Send a good SOAP RPC request over an HttpURLConnection.
   * Soap message does not contain the XML declaration. Verify that we get a
   * correct HTTP status code of 2xx.
   */
  public void TestGoodOneWaySoapMessageNoXMLDeclaration() throws Fault {
    boolean pass = true;
    Iterator iterator = null;
    try {
      TestUtil.logMsg("TestGoodOneWaySoapMessageNoXMLDeclaration");
      TestUtil.logMsg("Send good SOAP RPC request (expect 2xx status code)");
      HttpURLConnection conn = openHttpConnection(url);
      int httpStatusCode = sendRequest(conn,
          GoodOneWaySoapMessageNoXMLDeclaration, "utf-8");
      closeHttpConnection(conn);
      if (httpStatusCode < 200 || httpStatusCode > 299) {
        TestUtil
            .logErr("Expected 2xx status code, instead got " + httpStatusCode);
        pass = false;
      } else
        TestUtil
            .logMsg("Received expected 2xx status code of " + httpStatusCode);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("TestGoodOneWaySoapMessageNoXMLDeclaration failed", e);
    }

    if (!pass)
      throw new Fault("TestGoodOneWaySoapMessageNoXMLDeclaration failed");
  }

  /*
   * @testName: TestSoapMessageUsingUTF16Encoding
   *
   * @assertion_ids: WSI:SPEC:R1125; WSI:SPEC:R1111; WSI:SPEC:R4003;
   * WSI:SPEC:R4004; WS4EE:SPEC:113; WS4EE:SPEC:114; WS4EE:SPEC:115;
   * WS4EE:SPEC:117; WS4EE:SPEC:213; WS4EE:SPEC:219; WS4EE:SPEC:221;
   * WS4EE:SPEC:223; WS4EE:SPEC:224; WS4EE:SPEC:228; WS4EE:SPEC:248;
   * WS4EE:SPEC:249; WS4EE:SPEC:183; WS4EE:SPEC:184; WS4EE:SPEC:185;
   * WS4EE:SPEC:186; WS4EE:SPEC:187; WS4EE:SPEC:4000; WS4EE:SPEC:4002;
   * WS4EE:SPEC:5000; WS4EE:SPEC:5002;
   *
   * @test_Strategy: Send a good SOAP RPC request over an HttpURLConnection.
   * Send SOAP RPC request using utf-16 encoding. Verify that we get a correct
   * HTTP status code of 2xx.
   */
  public void TestSoapMessageUsingUTF16Encoding() throws Fault {
    boolean pass = true;
    Iterator iterator = null;
    try {
      TestUtil.logMsg("TestSoapMessageUsingUTF16Encoding");
      TestUtil.logMsg("Send SOAP RPC request using utf-16 encoding "
          + "(expect 2xx status code)");
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      OutputStreamWriter out = new OutputStreamWriter(baos, "utf-16");
      out.write(SoapMessageUsingUTF16Encoding);
      out.flush();
      out.close();
      boolean debug = false;
      if (debug) {
        FileOutputStream faos = new FileOutputStream("/tmp/foo");
        out = new OutputStreamWriter(faos, "utf-16");
        out.write(SoapMessageUsingUTF16Encoding);
        out.flush();
        out.close();
        faos.close();
      }
      TestUtil.logMsg("Original SOAP message length="
          + SoapMessageUsingUTF16Encoding.length());
      TestUtil
          .logMsg("Encoded SOAP message length=" + baos.toByteArray().length);
      HttpURLConnection conn = openHttpConnection(url);
      conn.setRequestProperty("Content-Type", "text/xml; charset=utf-16");
      int httpStatusCode = sendRequest(conn, baos.toByteArray(), "utf-16");
      closeHttpConnection(conn);
      if (httpStatusCode < 200 || httpStatusCode > 299) {
        TestUtil
            .logErr("Expected 2xx status code, instead got " + httpStatusCode);
        pass = false;
      } else
        TestUtil
            .logMsg("Received expected 2xx status code of " + httpStatusCode);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("TestSoapMessageUsingUTF16Encoding failed", e);
    }

    if (!pass)
      throw new Fault("TestSoapMessageUsingUTF16Encoding failed");
  }

  private HttpURLConnection openHttpConnection(String s) throws IOException {
    HttpURLConnection conn = (HttpURLConnection) new URL(s).openConnection();
    conn.setDoOutput(true);
    conn.setDoInput(true);
    conn.setRequestMethod("POST");
    conn.setRequestProperty("HTTP-Version", "HTTP/1.1");
    conn.setRequestProperty("Content-Type", "text/xml");
    conn.setRequestProperty("SOAPAction", "\"\"");
    return conn;
  }

  private void closeHttpConnection(HttpURLConnection conn) throws IOException {
    conn.disconnect();
  }

  private int sendRequest(HttpURLConnection conn, String request,
      String charsetName) throws IOException {

    TestUtil.logMsg("Request=" + request);
    return _sendRequest(conn, request.getBytes(charsetName));
  }

  private int sendRequest(HttpURLConnection conn, byte[] request,
      String encoding) throws IOException {

    TestUtil.logMsg("Request=" + new String(request, encoding));
    return _sendRequest(conn, request);
  }

  private int _sendRequest(HttpURLConnection conn, byte[] data)
      throws IOException {

    conn.setRequestProperty("Content-Length",
        Integer.valueOf(data.length).toString());
    OutputStream outputStream = conn.getOutputStream();
    try {
      outputStream = conn.getOutputStream();
      outputStream.write(data);
    } finally {
      try {
        outputStream.close();
      } catch (Throwable t) {
      }
    }

    boolean isFailure = true;
    int responseCode = conn.getResponseCode();

    String responseMessage = conn.getResponseMessage();

    TestUtil.logMsg("ResponseCode=" + responseCode);
    TestUtil.logMsg("ResponseMessage=" + responseMessage);
    if (responseCode == HttpURLConnection.HTTP_OK) {
      isFailure = false;
    }
    InputStream istream = null;
    BufferedReader reader = null;
    try {
      istream = !isFailure ? conn.getInputStream() : conn.getErrorStream();
      if (istream != null) {
        StringBuilder response = new StringBuilder();
        String buf = null;
        reader = new BufferedReader(new InputStreamReader(istream));
        while ((buf = reader.readLine()) != null) {
          response.append(buf);
        }
        if (response.length() != 0)
          TestUtil.logMsg("Response=" + response.toString());
      }
    } finally {
      try {
        reader.close();
        istream.close();
      } catch (Throwable t) {
      }
    }
    return responseCode;
  }
}
