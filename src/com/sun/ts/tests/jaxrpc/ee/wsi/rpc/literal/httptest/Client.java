/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrpc.ee.wsi.rpc.literal.httptest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import javax.xml.rpc.*;
import javax.xml.namespace.QName;

import com.sun.javatest.Status;

import com.sun.ts.tests.jaxrpc.common.*;

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

  String modeProperty = null; // platform.mode -> (standalone|javaEE)

  private static final String PKG_NAME = "com.sun.ts.tests.jaxrpc.ee.wsi.rpc.literal.httptest.";

  // The webserver username and password property names (harness properties)
  private static final String USERNAME = "user";

  private static final String PASSWORD = "password";

  // RPC service and port information
  private static final String NAMESPACE_URI = "http://httptestservice.org/wsdl";

  private static final String SERVICE_NAME = "HttpTestService";

  private static final String PORT_NAME = "HelloPort";

  private QName SERVICE_QNAME;

  private QName PORT_QNAME;

  private static final Class PORT_CLASS = Hello.class;

  private TSURL ctsurl = new TSURL();

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  private String username = null;

  private String password = null;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "wsirlhttptest.endpoint.1";

  private static final String WSDLLOC_URL = "wsirlhttptest.wsdlloc.1";

  private String url = null;

  private URL wsdlurl = null;

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
    String file = JAXRPC_Util.getURLFromProp(ENDPOINT_URL);
    url = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXRPC_Util.getURLFromProp(WSDLLOC_URL);
    wsdlurl = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint URL: " + url);
    TestUtil.logMsg("WSDL Location URL:    " + wsdlurl);
  }

  private void getTestURLfromStub() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    url = (String) stub._getProperty(Stub.ENDPOINT_ADDRESS_PROPERTY);
    TestUtil.logMsg("Service Endpoint URL: " + url);
  }

  // Get Port and Stub access via porting layer interface
  Hello port = null;

  Stub stub = null;

  private void getStubJaxrpc() throws Exception {
    TestUtil.logMsg("Get stub from service implementation class"
        + " using JAXRPC porting instance");
    port = (Hello) JAXRPC_Util.getStub("com.sun.ts.tests.jaxrpc.ee."
        + "wsi.rpc.literal.httptest.HttpTestService", "getHelloPort");
    TestUtil.logMsg("Cast stub to base Stub class ...");
    stub = (javax.xml.rpc.Stub) port;
  }

  private void getStub() throws Exception {
    /* Lookup service then obtain port */
    InitialContext ctx = new InitialContext();
    TestUtil.logMsg("Obtained InitialContext");
    TestUtil.logMsg("Lookup java:comp/env/service/wsirlhttptest");
    javax.xml.rpc.Service svc = (javax.xml.rpc.Service) ctx
        .lookup("java:comp/env/service/wsirlhttptest");
    TestUtil.logMsg("Obtained service");
    port = (Hello) svc.getPort(Hello.class);
    TestUtil.logMsg("Obtained port");
    TestUtil.logMsg("Cast port to base Stub class");
    stub = (javax.xml.rpc.Stub) port;
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.testArgs: -ap jaxrpc-url-props.dat
   * 
   * @class.setup_props: webServerHost; webServerPort; user; password;
   * platform.mode;
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    boolean pass = true;

    // Initialize QNames used by the test
    SERVICE_QNAME = new QName(NAMESPACE_URI, SERVICE_NAME);
    PORT_QNAME = new QName(NAMESPACE_URI, PORT_NAME);

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
      username = p.getProperty(USERNAME);
      password = p.getProperty(PASSWORD);
      TestUtil.logMsg("Creating stub instance ...");
      modeProperty = p.getProperty(MODEPROP);
      if (modeProperty.equals("standalone")) {
        getTestURLs();
        getStubJaxrpc();
        stub._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, url);
        TestUtil.logMsg("Setting target endpoint to " + url + " ...");
        JAXRPC_Util.setSOAPLogging(stub, System.out);
      } else {
        getStub();
        getTestURLfromStub();
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
   * @assertion_ids: JAXRPC:SPEC:530; JAXRPC:SPEC:534;
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
      int httpStatusCode = sendRequest(conn, GoodSoapMessage);
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
   * @assertion_ids: JAXRPC:SPEC:530; JAXRPC:SPEC:534;
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
      int httpStatusCode = sendRequest(conn, GoodSoapMessageNoXMLDeclaration);
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
   * @assertion_ids: JAXRPC:SPEC:530; JAXRPC:SPEC:534;
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
      int httpStatusCode = sendRequest(conn, GoodOneWaySoapMessage);
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
   * @assertion_ids: JAXRPC:SPEC:530; JAXRPC:SPEC:534;
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
          GoodOneWaySoapMessageNoXMLDeclaration);
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
   * @assertion_ids: JAXRPC:SPEC:533; JAXRPC:SPEC:534;
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
      if (TestUtil.traceflag) {
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
      conn.setRequestProperty("Content-Type", "text/xml; charset=\"utf-16\"");
      int httpStatusCode = sendRequest(conn, baos.toByteArray());
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

  private int sendRequest(HttpURLConnection conn, String request)
      throws IOException {

    TestUtil.logMsg("Request=" + request);
    return _sendRequest(conn, request.getBytes());
  }

  private int sendRequest(HttpURLConnection conn, byte[] request)
      throws IOException {

    TestUtil.logMsg("Request=" + request);
    return _sendRequest(conn, request);
  }

  private int _sendRequest(HttpURLConnection conn, byte[] data)
      throws IOException {

    int length = data.length;
    conn.setRequestProperty("Content-Length",
        new Integer(data.length).toString());
    OutputStream outputStream = conn.getOutputStream();
    outputStream.write(data);

    boolean isFailure = true;
    int responseCode = conn.getResponseCode();

    String responseMessage = conn.getResponseMessage();

    TestUtil.logMsg("ResponseCode=" + responseCode);
    TestUtil.logMsg("ResponseMessage=" + responseMessage);
    if (responseCode == HttpURLConnection.HTTP_OK)
      isFailure = false;
    InputStream istream = !isFailure ? conn.getInputStream()
        : conn.getErrorStream();
    if (istream != null) {
      String response = null;
      String buf = null;
      BufferedReader reader = new BufferedReader(
          new InputStreamReader(istream));
      while ((buf = reader.readLine()) != null) {
        if (response != null)
          response += buf;
        else
          response = buf;
      }
    }
    return responseCode;
  }
}
