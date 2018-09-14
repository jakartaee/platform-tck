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

package com.sun.ts.tests.integration.sec.secbasicssl;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.net.ssl.*;
//import sun.misc.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.javatest.Status;
import javax.net.ssl.HttpsURLConnection;

/**
 * @author Raja Perumal
 */
public class Client extends EETest {
  // Configurable constants:
  private String hostname = null;

  private int portnum = 0;

  private String pageBase = "/integration_sec_secbasicssl_web";

  private String basicSSLPage = "/basicSSL.jsp";

  private String webNoAuthzPage = "/webNoAuthz.jsp";

  private String webNotInRolePage = "/webNotInRole.jsp";

  private String webApiRemoteUser1Page = "/webApiRemoteUser1.jsp";

  private String authorizedPage = "/authorized.jsp";

  private String webRoleRefScope1Page = "/webRoleRefScope1.jsp";

  private String rolereversePage = "/rolereverse.jsp";

  private String requestAttributesPage = "/requestAttributes.jsp";

  private String user = null;

  // Constants:
  private final String webHostProp = "webServerHost";

  private final String webPortProp = "webServerPort";

  private final String userProp = "user";

  private final String passString = "PASSED!";

  private final String failString = "FAILED!";

  private final int numTests = 9;

  // Shared test variables:
  private String request = null;

  private WebUtil.Response response = null;

  private TSURL ctsurl = new TSURL();

  private String username = "";

  private String password = "";

  private TSLoginContext lc = null;

  private TSHttpsURLConnection tsHttpsURLConn = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; webServerHost; webServerPort;
   * user; password; securedWebServicePort; certLoginUserAlias;
   * 
   */
  public void setup(String[] args, Properties p) throws Fault {

    TestUtil.logMsg("setup...");

    // Read relevant properties:
    hostname = p.getProperty(webHostProp);
    username = p.getProperty("user");
    password = p.getProperty("password");
    user = p.getProperty(userProp);
    portnum = Integer.parseInt(p.getProperty("securedWebServicePort"));

    TestUtil.logMsg(
        "securedWebServicePort =" + p.getProperty("securedWebServicePort"));

  }

  /*
   * @testName: test_login_basic_over_ssl
   *
   * @assertion_ids: JavaEE:SPEC:249; Servlet:SPEC:26; JavaEE:SPEC:22
   *
   * @test_Strategy: This assertion ensures that HTTP Basic authentication over
   * SSL is supported by the J2EE server. It first calls request.isSecure() to
   * ensure it returns true, indicating that a secure connection is in place.
   * Next, the fully qualified URL for the page being viewed is received via the
   * HttpUtils.getRequestURL( request ) method. This url is checked to make sure
   * it begins with https, indicating HTTPS is in use.
   *
   */
  public void test_login_basic_over_ssl() throws Fault {

    String testName = "test_login_basic_over_ssl";
    String url = ctsurl.getURLString("https", hostname, portnum,
        pageBase + basicSSLPage);

    try {
      URL newURL = new URL(url);

      // Encode authData
      String authData = username + ":" + password;
      TestUtil.logMsg("authData : " + authData);

      BASE64Encoder encoder = new BASE64Encoder();

      String encodedAuthData = encoder.encode(authData.getBytes());
      TestUtil.logMsg("encoded authData : " + encodedAuthData);

      // open HttpsURLConnection using TSHttpsURLConnection
      TSHttpsURLConnection httpsURLConn = getHttpsURLConnection(newURL);

      // set request property
      httpsURLConn.setRequestProperty("Authorization",
          "Basic " + encodedAuthData.trim());

      InputStream content = (InputStream) httpsURLConn.getInputStream();
      BufferedReader in = new BufferedReader(new InputStreamReader(content));

      String output = "";
      String line;
      while ((line = in.readLine()) != null) {
        output = output + line;
        TestUtil.logMsg(line);
      }

      // verify output for expected test result
      verifyTestOutput(output, testName);

      // close connection
      httpsURLConn.disconnect();

    } catch (Exception e) {
      throw new Fault(testName + ": FAILED", e);
    }

  }

  /*
   * @testName: test_web_no_authz
   *
   * @assertion_ids: Servlet:SPEC:154.4
   *
   * @test_Strategy:
   *
   * This assertion checks that a user is granted access to an unprotected web
   * resource (e.g. webNoAuthz.jsp), independent of authentication status. That
   * is, they are not denied access, whether they are authenticated or not. The
   * assertion executes in webNoAuthz.jsp, and calls getUserPrincipal() to
   * ensure the user is not authenticated (should return null). If this value is
   * null, and the user gained access to the page, that means an unauthenticated
   * user was not denied access to an unprotected web resource.
   */
  public void test_web_no_authz() throws Fault {

    String testName = "test_web_no_authz";
    String url = ctsurl.getURLString("https", hostname, portnum,
        pageBase + webNoAuthzPage);

    try {
      URL newURL = new URL(url);

      // invokeHttpsURL() invokes the URL using HttpsURLConnection
      // and returns the output as a String
      String output = invokeHttpsURL(newURL);

      // verify output for expected test result
      verifyTestOutput(output, testName);

    } catch (Exception e) {
      throw new Fault(testName + ": FAILED", e);
    }

  }

  /*
   * @testName: test_web_not_in_role
   *
   * @assertion_ids: Servlet:SPEC:154.1
   *
   * @test_Strategy:
   *
   * This assertion checks that when isCallerInRole() is called from an
   * unprotected web resource, and the caller is not authenticated, it must
   * return false for all roles. The four role references "ADM", "MGR", "VMP",
   * and "EMP" are checked. If a call to isCallerInRole() returns true for any
   * of these role references, the assertion fails.
   *
   */
  public void test_web_not_in_role() throws Fault {

    String testName = "test_web_not_in_role";
    String url = ctsurl.getURLString("https", hostname, portnum,
        pageBase + webNotInRolePage);

    try {
      URL newURL = new URL(url);

      // invokeHttpsURL() invokes the URL using HttpsURLConnection
      // and returns the output as a String
      String output = invokeHttpsURL(newURL);

      // verify output for expected test result
      verifyTestOutput(output, testName);

    } catch (Exception e) {
      throw new Fault(testName + ": FAILED", e);
    }

  }

  /*
   * @testName: test_web_api_remoteuser_1
   *
   * @assertion_ids: Servlet:SPEC:140; Servlet:JAVADOC:368; Servlet:JAVADOC:369
   * 
   * @test_Strategy:
   *
   * The web_api_remoteuser assertion is broken into two parts. The first part,
   * web_api_remoteuser[1] needs to be performed under the identity of an
   * unauthenticated user. This tests that if getRemoteUser() returns null
   * (which means that no user has been authenticated), the isUserInRole()
   * method will always return false and getUserPrincipal() will always return
   * null. We have already established that isUserInRole() always returns false
   * in the "web_not_in_role" assertion. We also already checked that
   * getUserPrincipal() returns null in web_no_authz. This assertion checks that
   * getRemoteUser() returns null as well.
   *
   */
  public void test_web_api_remoteuser_1() throws Fault {
    String testName = "test_web_api_remoteuser_1";
    String url = ctsurl.getURLString("https", hostname, portnum,
        pageBase + webApiRemoteUser1Page);

    try {
      URL newURL = new URL(url);

      // invokeHttpsURL() invokes the URL using HttpsURLConnection
      // and returns the output as a String
      String output = invokeHttpsURL(newURL);

      // verify output for expected test result
      verifyTestOutput(output, testName);

    } catch (Exception e) {
      throw new Fault(testName + ": FAILED", e);
    }

  }

  /*
   * @testName: test_web_api_remoteuser_2
   *
   * @assertion_ids: Servlet:SPEC:140; Servlet:SPEC:368; Servlet:SPEC:369
   *
   * @test_Strategy: The second part of the web_api_remoteuser assertion needs
   * to be performed under the identity of an authenticated user. When
   * authorized.jsp is loaded, this assertion checks that the getRemoteUser()
   * method returns the user name that the client authenticated with. In order
   * to have gained access to this page, the user must have authenticated. A
   * call to getRemoteUser() is made, and the result is later compared to the
   * value specified in the "user" property in ts.jte.
   *
   */
  public void test_web_api_remoteuser_2() throws Fault {

    String testName = "test_web_api_remoteuser_2";
    String url = ctsurl.getURLString("https", hostname, portnum,
        pageBase + authorizedPage);

    try {
      URL newURL = new URL(url);

      // Encode authData
      String authData = username + ":" + password;
      TestUtil.logMsg("authData : " + authData);

      BASE64Encoder encoder = new BASE64Encoder();

      String encodedAuthData = encoder.encode(authData.getBytes());
      TestUtil.logMsg("encoded authData : " + encodedAuthData);

      // open HttpsURLConnection using TSHttpsURLConnection
      TSHttpsURLConnection httpsURLConn = getHttpsURLConnection(newURL);

      // set request property
      httpsURLConn.setRequestProperty("Authorization",
          "Basic " + encodedAuthData.trim());

      InputStream content = (InputStream) httpsURLConn.getInputStream();
      BufferedReader in = new BufferedReader(new InputStreamReader(content));

      String output = "";
      String line;
      while ((line = in.readLine()) != null) {
        output = output + line;
        TestUtil.logMsg(line);
      }

      // compare getRemoteUser() obtained from server's response
      // with the username stored in ts.jte
      //
      // Even though the output need not be identical (because
      // of appserver realms) the output should have substring
      // match for username stored in ts.jte.
      //
      String userNameToSearch = username;
      if (output.indexOf(userNameToSearch) == -1) {
        throw new Fault(testName + ": getRemoteUser(): " + "- did not find \""
            + userNameToSearch + "\" in log.");
      } else
        TestUtil.logMsg("Additional verification done");

      // verify output for expected test result
      verifyTestOutput(output, testName);

      // close connection
      httpsURLConn.disconnect();

    } catch (Exception e) {
      throw new Fault(testName + ": FAILED", e);
    }

  }

  /*
   * @testName: test_web_roleref_scope_1
   *
   * @assertion_ids: Servlet:SPEC:149
   *
   * @test_Strategy:
   *
   * The web_roleref_scope assertion is performed in two separate stages. It
   * tests that given two servlets in the same application, each of which calls
   * isUserInRole( X ), and where X is linked to different roles in the scope of
   * each of the servlets (i.e. R1 for servlet 1 and R2 for servlet 2), then a
   * user whose identity is mapped to R1 but not R2 shall get a true return
   * value from isUserInRole( X ) in servlet 1, and a false return value from
   * servlet 2. The first part of the test is performed in webRoleRefScope1.jsp
   * and ensures that isUserInRole( X ) works correctly for servlet 1.
   */
  public void test_web_roleref_scope_1() throws Fault {
    String testName = "test_web_roleref_scope_1";
    String url = ctsurl.getURLString("https", hostname, portnum,
        pageBase + webRoleRefScope1Page);

    try {
      URL newURL = new URL(url);

      // Encode authData
      String authData = username + ":" + password;
      TestUtil.logMsg("authData : " + authData);

      BASE64Encoder encoder = new BASE64Encoder();

      String encodedAuthData = encoder.encode(authData.getBytes());
      TestUtil.logMsg("encoded authData : " + encodedAuthData);

      // open HttpsURLConnection using TSHttpsURLConnection
      TSHttpsURLConnection httpsURLConn = getHttpsURLConnection(newURL);

      // set request property
      httpsURLConn.setRequestProperty("Authorization",
          "Basic " + encodedAuthData.trim());

      InputStream content = (InputStream) httpsURLConn.getInputStream();
      BufferedReader in = new BufferedReader(new InputStreamReader(content));

      String output = "";
      String line;
      while ((line = in.readLine()) != null) {
        output = output + line;
        TestUtil.logMsg(line);
      }

      // verify output for expected test result
      verifyTestOutput(output, testName);

      // close connection
      httpsURLConn.disconnect();

    } catch (Exception e) {
      throw new Fault(testName + ": FAILED", e);
    }

  }

  /*
   * @testName: test_web_roleref_scope_2
   *
   * @assertion_ids: Servlet:SPEC:149
   * 
   * @test_Strategy:
   *
   * This is the second part of the web_roleref_scope assertion, and is
   * performed in a separate jsp called rolereverse.jsp. In this jsp, the role
   * reference links for the "Manager" and "Administrator" roles are swapped as
   * compared to authorized.jsp (i.e. "ADM" is linked to "Manager" and "MGR" is
   * linked to "Administrator"). This assertion tests that isUserInRole is
   * working correctly for servlet 2.
   *
   */
  public void test_web_roleref_scope_2() throws Fault {
    String testName = "test_web_roleref_scope_2";
    String url = ctsurl.getURLString("https", hostname, portnum,
        pageBase + rolereversePage);

    try {
      URL newURL = new URL(url);

      // Encode authData
      String authData = username + ":" + password;
      TestUtil.logMsg("authData : " + authData);

      BASE64Encoder encoder = new BASE64Encoder();

      String encodedAuthData = encoder.encode(authData.getBytes());
      TestUtil.logMsg("encoded authData : " + encodedAuthData);

      // open HttpsURLConnection using TSHttpsURLConnection
      TSHttpsURLConnection httpsURLConn = getHttpsURLConnection(newURL);

      // set request property
      httpsURLConn.setRequestProperty("Authorization",
          "Basic " + encodedAuthData.trim());

      InputStream content = (InputStream) httpsURLConn.getInputStream();
      BufferedReader in = new BufferedReader(new InputStreamReader(content));

      String output = "";
      String line;
      while ((line = in.readLine()) != null) {
        output = output + line;
        TestUtil.logMsg(line);
      }

      // verify output for expected test result
      verifyTestOutput(output, testName);

      // close connection
      httpsURLConn.disconnect();

    } catch (Exception e) {
      throw new Fault(testName + ": FAILED", e);
    }

  }

  /*
   * @testName: test_web_is_authz
   *
   * @assertion_ids: Servlet:SPEC:140; Servlet:SPEC:142
   * 
   * @test_Strategy:
   *
   * The web_is_authz assertion is performed under the identity of an
   * authenticated user that is authorized to access the page authorized.jsp. It
   * tests that an authenticated user is granted access to a protected web
   * resource (i.e. one that is attributed with an authorization constraint)
   * when its identity is mapped to one of the permitted roles identified in the
   * authorization constraint.
   */
  public void test_web_is_authz() throws Fault {
    String testName = "test_web_is_authz";
    String url = ctsurl.getURLString("https", hostname, portnum,
        pageBase + authorizedPage);

    try {
      URL newURL = new URL(url);

      // Encode authData
      String authData = username + ":" + password;
      TestUtil.logMsg("authData : " + authData);

      BASE64Encoder encoder = new BASE64Encoder();

      String encodedAuthData = encoder.encode(authData.getBytes());
      TestUtil.logMsg("encoded authData : " + encodedAuthData);

      // open HttpsURLConnection using TSHttpsURLConnection
      TSHttpsURLConnection httpsURLConn = getHttpsURLConnection(newURL);
      // set request property
      httpsURLConn.setRequestProperty("Authorization",
          "Basic " + encodedAuthData.trim());

      InputStream content = (InputStream) httpsURLConn.getInputStream();
      BufferedReader in = new BufferedReader(new InputStreamReader(content));

      String output = "";
      String line;
      while ((line = in.readLine()) != null) {
        output = output + line;
        TestUtil.logMsg(line);
      }

      // check for the occurance of the string "PASSED"
      // in the output this ensures that the authorized user is
      // able to access the resource authorized.jsp
      String stringToSearch = "PASSED";
      if (output.indexOf(stringToSearch) == -1) {
        throw new Fault(testName + ": getRemoteUser(): " + "- did not find \""
            + stringToSearch + "\" in log.");
      } else
        TestUtil.logMsg("Additional verification done");

      // close connection
      httpsURLConn.disconnect();

    } catch (Exception e) {
      throw new Fault(testName + ": FAILED", e);
    }

  }

  /*
   * @testName: test_request_attributes
   *
   * @assertion_ids:Servlet:SPEC:26; Servlet:SPEC:26.1; Servlet:SPEC:26.2;
   * Servlet:SPEC:26.3
   *
   * @test_strategy: 1. Look for the following request attributes a)
   * cipher-suite b) key-size c) SSL certificate If any of the above attributes
   * are not set report test failure.
   *
   * Note: If a request has been transmitted over a secure protocol, such as
   * HTTPS, this information must be exposed via the isSecure method of the
   * ServletRequest interface. The web container must expose the following
   * attributes to the servlet programmer. 1) The cipher suite 2) the bit size
   * of the algorithm
   *
   * If there is an SSL certificate associated with the request, it must be
   * exposed by the servlet container to the servlet programmer as an array of
   * objects of type java.security.cert.X509Certificate
   *
   *
   */
  public void test_request_attributes() throws Fault {
    String testName = "test_request_attributes";
    String url = ctsurl.getURLString("https", hostname, portnum,
        pageBase + requestAttributesPage);

    try {
      URL newURL = new URL(url);

      // invokeHttpsURL() invokes the URL using HttpsURLConnection
      // and returns the output as a String
      String output = invokeHttpsURL(newURL);

      // verify output for expected test result
      verifyTestOutput(output, testName);

    } catch (Exception e) {
      throw new Fault(testName + ": FAILED", e);
    }

  }

  /*
   * cleanup
   */
  public void cleanup() throws Fault {
    TestUtil.logMsg("cleanup...");
  }

  public TSHttpsURLConnection getHttpsURLConnection(URL newURL)
      throws IOException {
    // open HttpsURLConnection using TSHttpsURLConnection
    TSHttpsURLConnection httpsURLConn = null;

    httpsURLConn = new TSHttpsURLConnection();
    if (httpsURLConn != null) {
      TestUtil.logMsg("Opening https url connection to: " + newURL.toString());
      httpsURLConn.init(newURL);
      httpsURLConn.setDoInput(true);
      httpsURLConn.setDoOutput(true);
      httpsURLConn.setUseCaches(false);

    } else
      throw new IOException("Error opening httsURLConnection");

    return httpsURLConn;
  }

  public void verifyTestOutput(String output, String testName) throws Fault {
    // check for the occurance of <testName>+": PASSED"
    // message in server's response. If this message is not present
    // report test failure.
    if (output.indexOf(testName + ": PASSED") == -1) {
      TestUtil
          .logMsg("Expected String from the output = " + testName + ": PASSED");
      TestUtil.logMsg("received output = " + output);
      throw new Fault(testName + ": FAILED");
    }
  }

  public String addCookies(String cookieHeader, String cookies) {

    String cookie;

    if (cookieHeader == null) {
      return null;
    }

    int j = cookieHeader.indexOf(";");

    if (j != -1) {
      String cValue = cookieHeader.substring(0, j);
      cookie = cValue.trim();
    } else
      cookie = cookieHeader.trim();

    // append cookie with existing cookies
    if (cookies == null)
      cookies = cookie;
    else
      cookies += ";" + cookie;

    return cookies;
  }

  public String invokeHttpsURL(URL newURL) throws IOException {

    // open HttpsURLConnection using TSHttpsURLConnection
    TSHttpsURLConnection httpsURLConn = getHttpsURLConnection(newURL);

    InputStream content = (InputStream) httpsURLConn.getInputStream();

    BufferedReader in = new BufferedReader(new InputStreamReader(content));

    String output = "";
    String line = "";

    while ((line = in.readLine()) != null) {
      output = output + line;
      TestUtil.logMsg(line);
    }

    TestUtil.logMsg("Output :" + output);

    // close connection
    httpsURLConn.disconnect();

    return output;
  }

}
