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

package com.sun.xml.messaging.saaj.client.p2p.http.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import org.apache.http.impl.io.EmptyInputStream;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.porting.TSURL;
import com.sun.ts.lib.util.TestUtil;
// Import implementation specific classes to test
import com.sun.xml.messaging.saaj.client.p2p.http.handler.*;

import sun.net.www.http.HttpClient;
import sun.net.www.protocol.http.AuthenticationInfo;

public class AbstractClient extends EETest {
  private String srcDir = "src/com/sun/ts/tests/internal/implementation/sjsas/saaj/com/sun/xml/messaging/saaj/client/p2p/http/handler/handlertests";

  private String testDir = null;

  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private TSURL tsurl = new TSURL();

  private URL url = null;

  private URLConnection urlConn = null;

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  private String username = "";

  private String password = "";

  private String authUsername = "";

  private String authPassword = "";

  private static final String SERVLET = "/HttpURLConnection/ReceivingServlet";

  private static final String UserNameProp = "user";

  private static final String PasswordProp = "password";

  private static final String authUserNameProp = "authuser";

  private static final String authPasswordProp = "authpassword";

  private static final String GoodSoapMessage = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:enc=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:ns=\"http://httptestservice.org/wsdl\"><soap:Body><ns:hello><parameters><string>World</string></parameters></ns:hello></soap:Body></soap:Envelope>";

  private HttpURLConnection conn = null;

  public static void main(String[] args) {
    AbstractClient theTests = new AbstractClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    boolean pass = true;
    try {
      String tsHome = p.getProperty("ts_home");
      testDir = tsHome + "/" + srcDir;
      hostname = p.getProperty(WEBSERVERHOSTPROP);
      if (hostname == null)
        pass = false;
      else if (hostname.equals(""))
        pass = false;
      try {
        portnum = Integer.parseInt(p.getProperty(WEBSERVERPORTPROP));
      } catch (Exception e) {
        pass = false;
      }
      username = p.getProperty(UserNameProp);
      password = p.getProperty(PasswordProp);
      authUsername = p.getProperty(authUserNameProp);
      authPassword = p.getProperty(authPasswordProp);
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET);
      TestUtil.logMsg("username=" + username);
      TestUtil.logMsg("password=" + password);
      TestUtil.logMsg("authUsername=" + authUsername);
      TestUtil.logMsg("authPassword=" + authPassword);
      TestUtil.logMsg("url=" + url);
    } catch (Exception e) {
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

  public void BasicAuthenticationTest() throws Fault {
    TestUtil.logTrace("BasicAuthenticationTest");
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "Call BasicAuthentication(boolean, String, int, String, PasswordAuthentication) constructor");

      PasswordAuthentication pa = new PasswordAuthentication(username,
          password.toCharArray());
      BasicAuthentication ba = new BasicAuthentication(false, hostname, portnum,
          "realm", pa);
      TestUtil.logMsg(
          "Call BasicAuthentication(boolean, String, int, String, String) constructor");
      ba = new BasicAuthentication(false, hostname, portnum, "realm", "auth");
      TestUtil.logMsg(
          "Call BasicAuthentication(boolean, URL, String, PasswordAuthentication) constructor");
      ba = new BasicAuthentication(false, url, "realm", pa);
      TestUtil.logMsg(
          "Call BasicAuthentication(boolean, URL, String, String) constructor");
      ba = new BasicAuthentication(false, url, "realm", "auth");
      TestUtil.logMsg("Call BasicAuthentication.getHeaderName() method");
      String headerName = ba.getHeaderName();
      TestUtil.logMsg("Call BasicAuthentication.getHeaderValue() method");
      String headerValue = ba.getHeaderValue();
      TestUtil.logMsg(
          "Call BasicAuthentication.supportsPreemptiveAuthorization() method");
      boolean isPreemptive = ba.supportsPreemptiveAuthorization();
      TestUtil.logMsg("HeaderName=" + headerName + ", HeaderValue="
          + headerValue + ", IsPreemtive=" + isPreemptive);
      TestUtil.logMsg("Call BasicAuthentication.getServerAuth(URL) method");
      AuthenticationInfo ai = ba.getServerAuth(url);
      TestUtil
          .logMsg("Call BasicAuthentication.getServerAuth(URL, String) method");
      ai = ba.getServerAuth(url, "realm");
      TestUtil
          .logMsg("Call BasicAuthentication.getProxyAuth(String, int) method");
      ai = ba.getProxyAuth(hostname, portnum);
      TestUtil.logMsg(
          "Call BasicAuthentication.getProxyAuth(String, int, String) method");
      ai = ba.getProxyAuth(hostname, portnum, "realm");
      TestUtil.logMsg("Call BasicAuthentication.addToCache() method");
      ba.addToCache();
      TestUtil.logMsg("Call BasicAuthentication.removeFromCache() method");
      ba.removeFromCache();
      TestUtil.logMsg("Call BasicAuthentication.cacheKey(boolean) method");
      ba.cacheKey(true);
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("BasicAuthenticationTest failed", e);
    }

    if (!pass)
      throw new Fault("BasicAuthenticationTest failed");
  }

  public void DigestAuthenticationTest() throws Fault {
    TestUtil.logTrace("DigestAuthenticationTest");
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "Call DigestAuthentication(URL, String, String, PasswordAuthentication) constructor");

      PasswordAuthentication pa = new PasswordAuthentication(username,
          password.toCharArray());
      DigestAuthentication ba = new DigestAuthentication(url, "realm", "basic",
          pa);
      TestUtil.logMsg("Call DigestAuthentication.getHeaderName() method");
      String headerName = ba.getHeaderName();
      TestUtil.logMsg("Call DigestAuthentication.getHeaderValue() method");
      String headerValue = ba.getHeaderValue();
      TestUtil.logMsg(
          "Call DigestAuthentication.supportsPreemptiveAuthorization() method");
      boolean isPreemptive = ba.supportsPreemptiveAuthorization();
      TestUtil.logMsg("HeaderName=" + headerName + ", HeaderValue="
          + headerValue + ", IsPreemtive=" + isPreemptive);
      TestUtil.logMsg("Call DigestAuthentication.getServerAuth(URL) method");
      AuthenticationInfo ai = ba.getServerAuth(url);
      TestUtil.logMsg(
          "Call DigestAuthentication.getServerAuth(URL, String) method");
      ai = ba.getServerAuth(url, "realm");
      TestUtil
          .logMsg("Call DigestAuthentication.getProxyAuth(String, int) method");
      ai = ba.getProxyAuth(hostname, portnum);
      TestUtil.logMsg(
          "Call DigestAuthentication.getProxyAuth(String, int, String) method");
      ai = ba.getProxyAuth(hostname, portnum, "realm");
      TestUtil.logMsg("Call DigestAuthentication.addToCache() method");
      ba.addToCache();
      TestUtil.logMsg("Call DigestAuthentication.removeFromCache() method");
      ba.removeFromCache();
      TestUtil.logMsg("Call DigestAuthentication.cacheKey(boolean) method");
      ba.cacheKey(true);
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("DigestAuthenticationTest failed", e);
    }

    if (!pass)
      throw new Fault("DigestAuthenticationTest failed");
  }

  public void HttpURLConnectionTest() throws Fault {
    TestUtil.logTrace("HttpURLConnectionTest");
    boolean pass = true;
    try {
      TestUtil
          .logMsg("Call HttpURLConnection(URL, hostname, portnum) constructor");
      HttpURLConnection conn = new HttpURLConnection(url, hostname, portnum);
      TestUtil.logMsg("Call HttpURLConnection.setDoOutput(boolean) method");
      conn.setDoOutput(true);
      TestUtil.logMsg("Call HttpURLConnection.setDoInput(boolean) method");
      conn.setDoInput(true);
      TestUtil.logMsg("Call HttpURLConnection.getDoOutput() method");
      boolean b = conn.getDoOutput();
      TestUtil.logMsg("Call HttpURLConnection.setDoInput(boolean) method");
      b = conn.getDoInput();
      TestUtil
          .logMsg("Call HttpURLConnection.setRequestMethod(boolean) method");
      conn.setRequestMethod("POST");
      TestUtil.logMsg(
          "Call HttpURLConnection.setRequestProperty(String, String) method");
      conn.setRequestProperty("HTTP-Version", "HTTP/1.1");
      conn.setRequestProperty("Content-Type", "text/xml");
      conn.setRequestProperty("SOAPAction", "\"\"");
      TestUtil.logMsg(
          "Call HttpURLConnection.setAuthenticationProperty(String, String) method");
      conn.setAuthenticationProperty("USER", username);
      conn.setAuthenticationProperty("PASSWORD", password);
      TestUtil
          .logMsg("Call HttpURLConnection.getRequestProperty(String) method");
      String s = conn.getRequestProperty("HTTP-Version");
      int httpStatusCode = sendRequest(conn, GoodSoapMessage);
      TestUtil.logMsg("Call HttpURLConnection.usingProxy() method");
      b = conn.usingProxy();
      TestUtil.logMsg("Call HttpURLConnection.finalize() method");
      conn.finalize();
      TestUtil.logMsg("Call HttpURLConnection.getMethod() method");
      String method = conn.getMethod();
      TestUtil.logMsg("Call HttpURLConnection.getHeaderField(String) method");
      s = conn.getHeaderField("HTTP-Version");
      TestUtil.logMsg("Call HttpURLConnection.getHeaderField(int) method");
      s = conn.getHeaderField(0);
      TestUtil.logMsg("Call HttpURLConnection.getHeaderFieldKey(int) method");
      s = conn.getHeaderFieldKey(0);
      TestUtil.logMsg("Call HttpURLConnection.connect() method");
      conn.connect();
      TestUtil.logMsg("Call HttpURLConnection.disconnect() method");
      conn.disconnect();
      TestUtil.logMsg("Call HttpURLConnection.getNewClient(URL) method");
      HttpClient hc = conn.getNewClient(url);
      TestUtil.logMsg(
          "Call HttpURLConnection.getProxiedClient(URL, String, int) method");
      hc = conn.getProxiedClient(url, hostname, portnum);
      TestUtil.logMsg(
          "Call HttpURLConnection.openConnectionCheckRedirects(URLConnection) method");
      try {
        InputStream is = conn
            .openConnectionCheckRedirects(url.openConnection());
        is.close();
      } catch (Exception e) {
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("HttpURLConnectionTest failed", e);
    }

    if (!pass)
      throw new Fault("HttpURLConnectionTest failed");
  }

  public void HandlerTest() throws Fault {
    TestUtil.logTrace("HandlerTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Call Handler() constructor");
      Handler h = new Handler();
      TestUtil.logMsg("Call Handler(String, int) constructor");
      h = new Handler(hostname, portnum);
      TestUtil.logMsg("Call Handler.getDefaultPort() method");
      int port = h.getDefaultPort();
      TestUtil.logMsg("Call Handler.openConnection(URL) method");
      URLConnection urlconn = h.openConnection(url);
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("HandlerTest failed", e);
    }

    if (!pass)
      throw new Fault("HandlerTest failed");
  }

  public void EmptyInputStreamTest() throws Fault {
    TestUtil.logTrace("EmptyInputStreamTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Call EmptyInputStream() constructor");
      EmptyInputStream eis = new EmptyInputStream();
      TestUtil.logMsg("Call EmptyInputStream.avaliable() method");
      int available = eis.available();
      TestUtil.logMsg("Call EmptyInputStream.read() method");
      int count = eis.read();
      TestUtil.logMsg("Call EmptyInputStream.close() method");
      eis.close();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("EmptyInputStreamTest failed", e);
    }

    if (!pass)
      throw new Fault("EmptyInputStreamTest failed");
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
    TestUtil.logMsg("Call HttpURLConnection.getOutputStream() method");
    OutputStream outputStream = conn.getOutputStream();
    outputStream.write(data);

    boolean isFailure = true;
    TestUtil.logMsg("Call HttpURLConnection.getResponseCode() method");
    int responseCode = conn.getResponseCode();

    TestUtil.logMsg("Call HttpURLConnection.getResponseMessage() method");
    String responseMessage = conn.getResponseMessage();

    TestUtil.logMsg("ResponseCode=" + responseCode);
    TestUtil.logMsg("ResponseMessage=" + responseMessage);
    if (responseCode == HttpURLConnection.HTTP_OK)
      isFailure = false;
    TestUtil.logMsg("Call HttpURLConnection.getInputStream() method");
    InputStream tmp = conn.getInputStream();
    TestUtil.logMsg("Call HttpURLConnection.getErrorStream() method");
    tmp = conn.getErrorStream();
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
