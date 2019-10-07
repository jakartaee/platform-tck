/*
 * Copyright (c) 2017, 2019 Oracle and/or its affiliates and others.
 * All rights reserved.
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

package com.sun.ts.tests.servlet.spec.serverpush;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.porting.TSURL;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.WebUtil;
import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;
import jdk.incubator.http.MultiMapResult;
import org.apache.commons.codec.binary.Base64;

import java.net.Authenticator;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.InetAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public class Client extends EETest {
  private static final String CONTEXT_ROOT = "/servlet_spec_serverpush_web";

  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String USERNAME = "authuser";

  private static final String PASSWORD = "authpassword";

  private String requestURI = null;

  private String hostname;

  private int portnum;

  private WebUtil.Response response = null;

  private TSURL tsurl = new TSURL();

  private String authUsername = null;

  private String authPassword = null;

  private CookieManager cm = new CookieManager();

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; authuser; authpassword;
   *
   */
  public void setup(String[] args, Properties p) throws Fault {
    boolean pass = true;

    try {
      authUsername = p.getProperty(USERNAME);
      authPassword = p.getProperty(PASSWORD);
      hostname = p.getProperty(WEBSERVERHOSTPROP);
      if (hostname == null || hostname.equals("")) {
        pass = false;
      }
      try {
        portnum = Integer.parseInt(p.getProperty(WEBSERVERPORTPROP));
      } catch (Exception e) {
        pass = false;
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

    System.out.println(hostname);
    System.out.println(portnum);
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("cleanup");
  }

  /*
   * @testName: serverPushTest
   * 
   * @assertion_ids: N/A;
   * 
   * @test_Strategy: Verify server push can work correctly.
   */
  public void serverPushTest() throws Fault {
    requestURI = "http://" + hostname + ":" + portnum + CONTEXT_ROOT
        + "/TestServlet";
    Map<String, String> headers = new HashMap<>();
    headers.put("foo", "bar");
    List<HttpResponse<String>> responses = sendRequest(headers, null, null);
    verfiyResponses(responses,
        new String[] { "hello", "INDEX from index.html" });
  }

  /*
   * @testName: getNullPushBuilderTest
   * 
   * @assertion_ids: N/A;
   * 
   * @test_Strategy: Verify the returned PushBuilder Object is null if the
   * current connection does not support server push.
   */
  public void getNullPushBuilderTest() throws Fault {
    try {
      requestURI = CONTEXT_ROOT + "/TestServlet";
      TestUtil.logMsg("Sending request \"" + requestURI + "\"");

      response = WebUtil.sendRequest("GET", InetAddress.getByName(hostname),
          portnum, tsurl.getRequest(requestURI), null, null);

      TestUtil.logMsg("response.statusToken:" + response.statusToken);
      TestUtil.logMsg("response.content:" + response.content);

      // Check that the page was found (no error).
      if (response.isError()) {
        TestUtil.logErr("Could not find " + requestURI);
        throw new Fault("getNullPushBuilderTest failed.");
      }

      if (response.content.indexOf("Get Null PushBuilder") < 0) {
        throw new Fault("getNullPushBuilderTest failed.");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getNullPushBuilderTest failed: ", e);
    }
  }

  /*
   * @testName: serverPushInitTest
   * 
   * @assertion_ids: N/A;
   * 
   * @test_Strategy: Verify PushBuilder is initialized correctly.
   */
  public void serverPushInitTest() throws Fault {
    requestURI = "http://" + hostname + ":" + portnum + CONTEXT_ROOT
        + "/TestServlet2";
    Map<String, String> headers = new HashMap<>();
    headers.put("foo", "bar");
    headers.put("If-Match", "*");
    headers.put("Range", "bytes=100-");
    String authString = authUsername + ":" + authPassword;
    logMsg("auth string: " + authString);
    byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
    String authStringEnc = new String(authEncBytes);

    CookieManager cm = new CookieManager();
    headers.put("Authorization", "Basic " + authStringEnc);
    headers.put("Referer", requestURI + "/test");

    List<HttpResponse<String>> responses = sendRequest(headers, null, cm);
    if (responses.size() != 1)
      throw new Fault("Test fail");
    String sessionid = responses.get(0).headers().allValues("set-cookie")
        .stream().filter(value -> value.contains("JSESSIONID=")).findFirst()
        .orElse(null);

    if (sessionid == null) {
      throw new Fault("Test fail: new session ID should be used as the "
          + "PushBuilder's session ID.");
    }

    sessionid = sessionid
        .substring(sessionid.indexOf("JSESSIONID=") + "JSESSIONID=".length());
    if (sessionid.indexOf(";") > 0) {
      sessionid = sessionid.substring(0, sessionid.indexOf(";"));
    } else if (sessionid.indexOf(".") > 0) {
      sessionid = sessionid.substring(0, sessionid.indexOf("."));
    }

    logMsg("Sessionid in cookie: " + sessionid);

    String response = responses.get(0).body();

    StringTokenizer token = new StringTokenizer(response, "\n");
    String newSessionId = "";
    while (token.hasMoreTokens()) {
      String tmp = token.nextToken();
      if (tmp.startsWith("JSESSIONID:")) {
        newSessionId = tmp.substring("JSESSIONID:".length()).trim();
        break;
      }
    }

    if (!sessionid.contains(newSessionId) && !newSessionId.contains(sessionid)) {
      throw new Fault("Test fail: new session ID should be used as the "
          + "PushBuilder's session ID.");
    }

    if (!response.contains("Return new instance:true")) {
      throw new Fault("Test fail: each call to newPushBuilder() should "
          + "create a new instance");
    }

    if (!response.contains("Method:GET")) {
      throw new Fault("Test fail: The method of PushBuilder should be "
          + "initialized to \"GET\"");
    }

    if (!response.contains("foo=bar")) {
      throw new Fault("Test fail: The existing request headers of the current "
          + "HttpServletRequest should be added to the builder");
    }

    if (response.contains("If-Match")) {
      throw new Fault(
          "Test fail: Conditional headers should NOT be added to the builder");
    }

    if (response.contains("Range")) {
      throw new Fault(
          "Test fail: Range headers should NOT be added to the builder");
    }

    if (!response.contains("Authorization")) {
      throw new Fault(
          "Test fail: Authorization headers should be added to the builder");
    }

    if (!(response.contains("referer=" + requestURI)
        || response.contains("Referer=" + requestURI))) {
      throw new Fault(
          "Test fail: Referer headers should be set to " + requestURI);
    }
  }

  /*
   * @testName: serverPushSessionTest
   * 
   * @assertion_ids: N/A;
   * 
   * @test_Strategy: Verify PushBuilder with session works as expected.
   */
  public void serverPushSessionTest() throws Fault {
    try {
      requestURI = CONTEXT_ROOT + "/TestServlet3?generateSession=true";
      TestUtil.logMsg("Sending request \"" + requestURI + "\"");

      response = WebUtil.sendRequest("GET", InetAddress.getByName(hostname),
          portnum, tsurl.getRequest(requestURI), null, null);
      TestUtil.logMsg("The new sessionid is :" + response.content);

      // Check that the page was found (no error).
      if (response.isError()) {
        TestUtil.logErr("Could not find " + requestURI);
        throw new Fault("serverPushSessionTest failed.");
      }

      requestURI = "http://" + hostname + ":" + portnum + CONTEXT_ROOT
          + "/TestServlet3;jsessionid=" + response.content.trim();
      TestUtil.logMsg("Sending request \"" + requestURI + "\"");
      List<HttpResponse<String>> responses = sendRequest(new HashMap<>(), null,
          null);
      String responseStr = responses.get(0).body();

      TestUtil.logMsg("The test result :" + responseStr);
      if (responseStr.indexOf("Test success") < 0) {
        throw new Fault("serverPushSessionTest failed.");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("serverPushSessionTest failed: ", e);
    }
  }

  /*
   * @testName: serverPushCookieTest
   * 
   * @assertion_ids: N/A;
   * 
   * @test_Strategy: Verify PushBuilder with cookie works as expected.
   */
  public void serverPushCookieTest() throws Fault {
    requestURI = "http://" + hostname + ":" + portnum + CONTEXT_ROOT
        + "/TestServlet4";
    Map<String, String> headers = new HashMap<>();
    headers.put("foo", "bar");
    CookieManager cm = new CookieManager();
    List<HttpResponse<String>> responses = sendRequest(headers, null, cm);
    verfiyResponses(responses,
        new String[] { "add cookies [foo,bar] [baz,qux] to response",
            "INDEX from index.html" });
    boolean cookieExisted = false;
    String pbCookies = "";
    try {
      for (HttpResponse<String> r : responses) {
        if (r.body().indexOf("Cookie header in PushBuilder: ") >= 0) {
          cookieExisted = true;
          pbCookies = r.body()
              .substring(r.body().indexOf("Cookie header in PushBuilder: "));
          break;
        }
      }
      if (!cookieExisted) {
        throw new Fault("Wrong Responses");
      }

      if (pbCookies.indexOf("foo") < 0 || pbCookies.indexOf("bar") < 0) {
        throw new Fault(
            "The Cookie header 'foo=bar' should be added to the PushBuilder.");
      }

      if (pbCookies.indexOf("baz") >= 0 || pbCookies.indexOf("qux") >= 0) {
        throw new Fault(
            "the maxAge for Cookie 'baz=qux' is <= 0, it should be removed from the builder");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("serverPushSessionTest failed: ", e);
    }
  }

  /*
   * @testName: serverPushSessionTest2
   * 
   * @assertion_ids: N/A;
   * 
   * @test_Strategy: Verify PushBuilder with Session works as expected.
   */
  public void serverPushSessionTest2() throws Fault {
    requestURI = "http://" + hostname + ":" + portnum + CONTEXT_ROOT
        + "/TestServlet5";
    Map<String, String> headers = new HashMap<>();
    CookieManager cm = new CookieManager();
    cm.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
    List<HttpResponse<String>> responses = sendRequest(headers, null, cm);
    boolean pass = false;

    try {
      List<HttpCookie> cookies = cm.getCookieStore().get(new URI(
          "http://" + hostname + ":" + portnum + CONTEXT_ROOT + "/index.html"));
      for (HttpCookie cookie : cookies) {
        if ("JSESSIONID".equals(cookie.getName())) {
          pass = true;
        }
      }

      if (!pass) {
        for (HttpResponse<String> response : responses) {
          if (response.uri().toString().indexOf("index.html;jsessionid") > 0) {
            pass = true;
          }
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("serverPushSessionTest failed: ", e);
    }

    if (!pass) {
      throw new Fault(
          "If the builder has a session ID, then the pushed request should "
              + "include the session ID either as a Cookie or as a URI parameter as appropriate");
    }
  }

  /*
   * @testName: serverPushMiscTest
   * 
   * @assertion_ids: N/A;
   * 
   * @test_Strategy: Verify some methods of PushBuilder works as expected.
   */
  public void serverPushMiscTest() throws Fault {
    requestURI = "http://" + hostname + ":" + portnum + CONTEXT_ROOT
        + "/TestServlet6";
    Map<String, String> headers = new HashMap<>();
    headers.put("foo", "bar");
    headers.put("baz", "qux");
    List<HttpResponse<String>> responses = sendRequest(headers, null, null);
    HttpResponse<String> pushResp = null;
    HttpRequest pushReq = null;

    for (HttpResponse<String> response : responses) {
      if (response.uri().toString().indexOf("index.html") >= 0) {
        pushResp = response;
        pushReq = response.request();
      }
    }

    if (pushResp == null)
      throw new Fault("can not get push response");

    logMsg(
        "expected header: h1=v1, foo=v2; expected querysting: querystring=1&querystring=2");
    Map<String, List<String>> pushHeaders = pushReq.headers().map();
    logMsg("Current push request header: " + pushHeaders);
    if (!(pushHeaders.get("h1") != null
        && pushHeaders.get("h1").get(0).equals("v1"))) {
      throw new Fault("test fail: could not find header h1=v1");
    }

    if (!(pushHeaders.get("foo") != null
        && pushHeaders.get("foo").get(0).equals("v2"))) {
      throw new Fault("test fail: could not find header foo=v2");
    }

    if (pushHeaders.get("baz") != null) {
      throw new Fault("test fail");
    }

    logMsg("Current query string of the push request is "
        + pushReq.uri().getQuery());
    if (pushReq.uri().getQuery() == null || pushReq.uri().getQuery()
        .indexOf("querystring=1&querystring=2") < 0) {
      throw new Fault(
          "test fail: could not find correct querystring \"querystring=1&querystring=2\"");
    }
  }

  /*
   * @testName: serverPushNegtiveTest
   * 
   * @assertion_ids: N/A;
   * 
   * @test_Strategy: Verify some methods of PushBuilder works as expected.
   */
  public void serverPushNegtiveTest() throws Fault {
    requestURI = "http://" + hostname + ":" + portnum + CONTEXT_ROOT
        + "/TestServlet7";
    Map<String, String> headers = new HashMap<>();

    List<HttpResponse<String>> responses = sendRequest(headers, null, null);
    HttpResponse<String> servletResp = null;
    for (HttpResponse<String> response : responses) {
      if (response.uri().toString().indexOf("TestServlet7") >= 0) {
        servletResp = response;
      }
    }

    if (servletResp == null)
      throw new Fault("can not get servlet response");
    if (servletResp.body().indexOf("test passed") < 0) {
      throw new Fault("test fail");
    }
  }

  private List<HttpResponse<String>> sendRequest(Map<String, String> headers,
      Authenticator auth, CookieManager cm) throws Fault {
    HttpClient.Builder builder = HttpClient.newBuilder();
    if (auth != null)
      builder.authenticator(auth);
    if (cm != null)
      builder.cookieManager(cm);

    HttpClient client = builder.version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.ALWAYS)
        .executor(Executors.newFixedThreadPool(4)).build();
    ;

    List<HttpResponse<String>> responses = new ArrayList<HttpResponse<String>>();

    try {
      // GET
      HttpRequest.Builder requestBuilder = HttpRequest
          .newBuilder(new URI(requestURI)).version(HttpClient.Version.HTTP_2);
      for (Map.Entry<String, String> e : headers.entrySet()) {
        requestBuilder.setHeader(e.getKey(), e.getValue());
      }

      HttpRequest request = requestBuilder.GET().build();

      CompletableFuture<MultiMapResult<String>> sendAsync = client
          .sendAsync(request, HttpResponse.MultiProcessor.asMap((req) -> {
            Optional<HttpResponse.BodyHandler<String>> optional = Optional
                .of(HttpResponse.BodyHandler.asString());
            String msg = " - " + req.uri();
            logMsg(msg);
            return optional;
          }));

      Map<HttpRequest, CompletableFuture<HttpResponse<String>>> multiMapResult = sendAsync
          .join();

      if (multiMapResult != null) {
        for (HttpRequest key : multiMapResult.keySet()) {
          CompletableFuture<HttpResponse<String>> completableFuture = multiMapResult
              .get(key);
          HttpResponse<String> response = completableFuture.get();
          printResponse(response);
          responses.add(response);
        }
      }
    } catch (Exception e) {
      throw new Fault("Test fail", e);
    }
    return responses;
  }

  private void printResponse(HttpResponse<String> response) {
    logMsg("ResponseURI:     " + response.uri());
    logMsg("ResponseBody:     " + response.body());
    logMsg("HTTP-Version: " + response.version());
    logMsg("Statuscode:   " + response.statusCode());
    logMsg("Header:");
    response.headers().map().forEach(
        (header, values) -> logMsg("  " + header + " = " + values.stream()
            .map(String::trim).reduce(String::concat).orElse("hallo")));
  }

  private void verfiyResponses(List<HttpResponse<String>> responses,
      String[] expectedResponses) throws Fault {
    if (responses.size() == 0) {
      throw new Fault("No Responses, expected responses are "
          + Arrays.toString(expectedResponses));
    }

    if (responses.size() != expectedResponses.length) {
      throw new Fault("Wrong Responses, expected responses are "
          + Arrays.toString(expectedResponses));
    }

    for (String s : expectedResponses) {
      boolean found = false;
      for (HttpResponse<String> r : responses) {
        logMsg(r.body());
        if (r.body().indexOf(s) >= 0) {
          found = true;
          break;
        }
      }
      if (!found) {
        throw new Fault("Wrong Responses, expected responses are "
            + Arrays.toString(expectedResponses));
      }
    }
  }
}
