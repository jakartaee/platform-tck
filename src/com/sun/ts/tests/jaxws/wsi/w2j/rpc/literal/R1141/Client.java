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
 * @(#)Client.java	1.15 03/05/21
 */

package com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R1141;

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

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R1141.";

  // The webserver username and password property names (harness properties)
  private static final String USERNAME = "user";

  private static final String PASSWORD = "password";

  // RPC service and port information
  private static final String NAMESPACE_URI = "http://w2jrlr1141testservice.org/wsdl";

  private static final String SERVICE_NAME = "W2JRLR1141TestService";

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
  private static final String ENDPOINT_URL = "wsi.w2jrlr1141.endpoint.1";

  private static final String WSDLLOC_URL = "wsi.w2jrlr1141.wsdlloc.1";

  private String url = null;

  private URL wsdlurl = null;

  static W2JRLR1141TestService service;

  // expect 2xx http status code
  String GoodSoapMessage = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:enc=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:ns=\"http://w2jrlr1141testservice.org/types\"><soap:Body><ns:HelloRequestElement><string>Rocky</string></ns:HelloRequestElement></soap:Body></soap:Envelope>";

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXWS_Util.getURLFromProp(ENDPOINT_URL);
    url = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(WSDLLOC_URL);
    wsdlurl = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint URL: " + url);
    TestUtil.logMsg("WSDL Location URL:    " + wsdlurl);
  }

  private void getTargetEndpointAddress(Object port) throws Exception {
    TestUtil.logMsg("Get Target Endpoint Address for port=" + port);
    String url = JAXWS_Util.getTargetEndpointAddress(port);
    TestUtil.logMsg("Target Endpoint Address=" + url);
  }

  Hello port = null;

  private void getPortStandalone() throws Exception {
    port = (Hello) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        W2JRLR1141TestService.class, PORT_QNAME, Hello.class);
    JAXWS_Util.setTargetEndpointAddress(port, url);
  }

  private void getPortJavaEE() throws Exception {
    TestUtil.logMsg("Obtain service via WebServiceRef annotation");
    TestUtil.logMsg("Obtained service");
    port = (Hello) service.getPort(Hello.class);
    TestUtil.logMsg("Obtained port");
    getTargetEndpointAddress(port);
    // JAXWS_Util.setTargetEndpointAddress(port, url);
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
        getPortStandalone();
      } else {
        TestUtil.logMsg(
            "WebServiceRef is not set in Client (get it from specific vehicle)");
        service = (W2JRLR1141TestService) getSharedObject();
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
   * @testName: TestHTTP10Message
   *
   * @assertion_ids: WSI:SPEC:R1141
   *
   * @test_Strategy: Send a good SOAP RPC request over an HttpURLConnection use
   * a http version of 1.0. Verify that we get a correct HTTP status code of
   * 2xx.
   */
  public void TestHTTP10Message() throws Fault {
    boolean pass = true;
    Iterator iterator = null;
    try {
      TestUtil.logMsg("TestHTTP10Message");
      TestUtil.logMsg("Send good SOAP RPC request (expect 2xx status code)");
      HttpURLConnection conn = openHttp10Connection(url);
      TestUtil
          .logMsg("HTTP VERSION = " + conn.getRequestProperty("HTTP-Version"));
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
   * @testName: TestHTTP11Message
   *
   * @assertion_ids: WSI:SPEC:R1141
   *
   * @test_Strategy: Send a good SOAP RPC request over an HttpURLConnection use
   * a http version of 1.1. Verify that we get a correct HTTP status code of
   * 2xx.
   */
  public void TestHTTP11Message() throws Fault {
    boolean pass = true;
    Iterator iterator = null;
    try {
      TestUtil.logMsg("TestHTTP10Message");
      TestUtil.logMsg("Send good SOAP RPC request (expect 2xx status code)");
      HttpURLConnection conn = openHttpConnection(url);
      TestUtil
          .logMsg("HTTP VERSION = " + conn.getRequestProperty("HTTP-Version"));
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

  private HttpURLConnection openHttp10Connection(String s) throws IOException {
    HttpURLConnection conn = (HttpURLConnection) new URL(s).openConnection();
    conn.setDoOutput(true);
    conn.setDoInput(true);
    conn.setRequestMethod("POST");
    conn.setRequestProperty("HTTP-Version", "HTTP/1.0");
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

    int length = data.length;
    conn.setRequestProperty("Content-Length",
        new Integer(data.length).toString());
    OutputStream outputStream = null;
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
        String response = null;
        String buf = null;
        reader = new BufferedReader(new InputStreamReader(istream));
        while ((buf = reader.readLine()) != null) {
          if (response != null)
            response += buf;
          else
            response = buf;
        }
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
