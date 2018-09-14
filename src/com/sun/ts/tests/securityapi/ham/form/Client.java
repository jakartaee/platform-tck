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

package com.sun.ts.tests.securityapi.ham.form;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.harness.EETest.Fault;

import java.io.*;
import java.net.*;
import java.util.*;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.WebUtil.Response;

public class Client extends EETest {
  // Configurable constants:

  private String hostname = null;

  private int portnum = 0;

  protected String pageBase = "/securityapi_ham_form_web";

  private String searchFor = "The user principal is: "; // (+username)

  private String searchForGetRemoteUser = "getRemoteUser(): "; // (+username)

  // Constants:
  private final String WebHostProp = "webServerHost";

  private final String WebPortProp = "webServerPort";

  private final String tsHomeProp = "ts_home";

  // Shared test variables:
  private Properties props = null;

  private String request = null;

  private WebUtil.Response response = null;

  private WebUtil.Response loginPageRequestResponse = null;

  private WebUtil.Response errorPageRequestResponse = null;

  private Hashtable cookies = null;

  private TSURL tsurl = new TSURL();

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /**
   * Entry point for same-VM execution. In different-VM execution, the main
   * method delegates to this method.
   */
  public Status run(String args[], PrintWriter out, PrintWriter err) {

    Client theTests = new Client();

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort;
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;

    try {
      hostname = p.getProperty(WebHostProp);
      portnum = Integer.parseInt(p.getProperty(WebPortProp));
    } catch (Exception e) {
      logErr("Error: got exception: ", e);
    }
  }

  /*
   * @testName: testFormHAMValidateRequest
   *
   * @assertion_ids: Security:JAVADOC:24; Security:JAVADOC:80;
   * Security:SPEC:2.4-2; Security:SPEC:2.4-5;
   *
   *
   * @test_Strategy: 1. Send request to access protected servlet 2. Receive
   * login page(make sure it the expected login page) 3. Send form response with
   * username and password 4. Receive protected servlet (ensure principal is
   * correct, and ensure getRemoteUser() returns the username, and ensure
   * isUserInRole() is working properly)
   */
  public void testFormHAMValidateRequest() throws Fault {
    try {

      String pageSec = pageBase + "/servlet";
      String pageSecurityCheck = pageBase + "/j_security_check";
      String username = "tom";
      String password = "secret1";
      requestAndGetForwardLoginPage(pageSec, "testFormHAMValidateRequest");

      // Send response to login form with session id cookie:

      request = pageSecurityCheck;
      TestUtil.logMsg(
          "Sending request \"" + request + "\" with login information.");
      Properties postData = new Properties();
      postData.setProperty("j_username", username);
      postData.setProperty("j_password", password);
      response = WebUtil.sendRequest("POST", InetAddress.getByName(hostname),
          portnum, tsurl.getRequest(request), postData, cookies);

      TestUtil.logMsg("response.statusToken:" + response.statusToken);
      TestUtil.logMsg("response.content:" + response.content);

      // Check that the page was found (no error).
      if (response.isError()) {
        TestUtil.logErr("Could not find " + request);
        throw new Fault("testFormHAMValidateRequest failed.");
      }

      // Call followRedirect() to make sure we receive the required page
      response = followRedirect(response, "testFormHAMValidateRequest");

      // Print response content
      TestUtil.logMsg("received response content  1: " + response.content);

      // Check to make sure we are authenticated by checking the page
      // content. The jsp should output "The user principal is: j2ee"
      String searchString = searchFor + username;
      if (response.content.indexOf(searchString) == -1) {
        TestUtil.logErr("User Principal incorrect.  Page received:");
        TestUtil.logErr(response.content);
        TestUtil.logErr("(Should say: \"" + searchString + "\")");
        throw new Fault("testFormHAMValidateRequest failed.");
      }
      TestUtil.logMsg("User Principal correct.");

      // Check to make sure getRemoteUser returns the user name.
      searchString = searchForGetRemoteUser + username;
      if (response.content.indexOf(searchString) == -1) {
        TestUtil.logErr("getRemoteUser() did not return " + username + ":");
        TestUtil.logErr(response.content);
        TestUtil.logErr("(Should say: \"" + searchString + "\")");
        throw new Fault("testFormHAMValidateRequest failed.");
      }
      TestUtil.logMsg("getRemoteUser() correct.");

      // Check to make sure isUserInRole is working properly:
      Map<String, Boolean> roleCheck = new HashMap<String, Boolean>();
      roleCheck.put("Administrator", new Boolean(true));
      roleCheck.put("Manager", new Boolean(true));
      roleCheck.put("Employee", new Boolean(false));
      // roleCheck.put( "Administrator", new Boolean( false ) );
      if (!checkRoles(response.content, roleCheck)) {
        TestUtil.logErr("isUserInRole() does not work correctly.");
        TestUtil.logErr("Page Received:");
        TestUtil.logErr(response.content);
        throw new Fault("testFormHAMValidateRequest failed.");
      }
      TestUtil.logMsg("isUserInRole() correct.");

      // Now that we are authenticated, try accessing the resource again
      // to ensure we need not go through the login page again.
      request = pageSec;
      TestUtil.logMsg("Cookies =" + cookies.toString());
      TestUtil.logMsg("Re-sending request \"" + request + "\"");
      response = WebUtil.sendRequest("GET", InetAddress.getByName(hostname),
          portnum, tsurl.getRequest(request), null, cookies);

      // Check that the page was found (no error).
      if (response.isError()) {
        TestUtil.logErr("Could not find " + pageSec);
        throw new Fault("testFormHAMValidateRequest failed.");
      }

      // Check to make sure we are still authenticated.
      if (response.content.indexOf(searchString) == -1) {
        TestUtil.logErr("User Principal incorrect.  Page received:");
        TestUtil.logErr(response.content);
        TestUtil.logErr("(Should say: \"" + searchString + "\")");
        throw new Fault("testFormHAMValidateRequest failed.");
      }
      TestUtil.logMsg("User Principal still correct.");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("testFormHAMValidateRequest failed: ", e);
    }
  }

  /*
   * @testName: testLoginToContinueLoginPage
   *
   * @assertion_ids: Security:JAVADOC:82
   *
   * @test_Strategy: 1. Send request to access jspSec.jsp 2. Receive correct
   * login page
   */
  public void testLoginToContinueLoginPage() throws Fault {
    try {
      String pageSec = pageBase + "/servlet";
      String pageLogin = pageBase + "/form-login-servlet";

      // Request restricted jsp page.
      request = pageSec;
      TestUtil.logMsg("Sending request \"" + request + "\"");
      response = WebUtil.sendRequest("GET", InetAddress.getByName(hostname),
          portnum, tsurl.getRequest(request), null, null);

      // Check that the page was found (no error).
      if (response.isError()) {
        TestUtil.logErr("Could not find " + request);
        throw new Fault("testLoginToContinueLoginPage" + " failed.");
      }

      // Request login page
      request = pageLogin;
      TestUtil.logMsg("Sending request \"" + request + "\"");
      loginPageRequestResponse = WebUtil.sendRequest("GET",
          InetAddress.getByName(hostname), portnum, tsurl.getRequest(request),
          null, cookies);

      // Check that the page was found (no error).
      if (loginPageRequestResponse.isError()) {
        TestUtil.logErr("Could not find " + request);
        throw new Fault("testLoginToContinuLoginPage  failed.");
      }

      // Compare the received login page with the expected login page
      if (response.content.equals(loginPageRequestResponse.content)) {
        TestUtil.logMsg("Received the expected login page");
      } else {
        TestUtil.logMsg("Received incorrect login page");
        throw new Fault("testLoginToContinueLoginPage  failed.");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("testLoginToContinueLoginPage failed: ", e);
    }
  }

  /*
   * @testName: testLoginToContinueerrorPage
   *
   * @assertion_ids: Security:JAVADOC:81
   *
   * @test_Strategy: 1. Send request to access jspSec.jsp 2. Receive login page
   * 3. Send form response with username and incorrect password 4. Receive error
   * page (make sure it is the expected error page)
   */
  public void testLoginToContinueerrorPage() throws Fault {
    try {
      String pageSec = pageBase + "/servlet";
      String pageSecurityCheck = pageBase + "/j_security_check";
      String pageError = pageBase + "/form-login-error-servlet";
      String username = "tom";
      String password = "wrongpassword";

      requestAndGetForwardLoginPage(pageSec, "testLoginToContinueerrorPage");

      // Send response to login form with session id cookie and username
      // and incorrect password:
      request = pageSecurityCheck;
      TestUtil.logMsg("Sending request \"" + request
          + "\" with incorrect login information.");
      Properties postData = new Properties();
      postData.setProperty("j_username", username);
      postData.setProperty("j_password", "incorrect" + password);
      response = WebUtil.sendRequest("POST", InetAddress.getByName(hostname),
          portnum, tsurl.getRequest(request), postData, cookies);
      TestUtil.logMsg("response.statusToken:" + response.statusToken);

      // Call followRedirect() to make sure we receive the required page
      response = followRedirect(response, "testLoginToContinueerrorPage");

      // Check to make sure the user principal is null:
      String searchString = searchFor + "null";
      if (response.content.indexOf(searchString) == -1) {
        TestUtil.logErr("User principal is not null in error page:");
        TestUtil.logErr(response.content);
        throw new Fault("testLoginToContinueerrorPage failed.");
      }

      TestUtil.logMsg("User Principal is null as expected.");

      // Request error page
      request = pageError;
      TestUtil.logMsg("Sending request \"" + request + "\"");
      errorPageRequestResponse = WebUtil.sendRequest("GET",
          InetAddress.getByName(hostname), portnum, tsurl.getRequest(request),
          null, cookies);

      // Check that the page was found (no error).
      if (errorPageRequestResponse.isError()) {
        TestUtil.logErr("Could not find " + request);
        throw new Fault("testLoginToContinueerrorPage  failed.");
      }

      // Compare the received error page with the expected error page
      // i.e Check whether
      // response.content ==errorPageRequestResponse.content
      if (response.content.equals(errorPageRequestResponse.content)) {
        TestUtil.logMsg("Received the expected error page");
      } else {
        TestUtil.logMsg("Received incorrect error page");
        throw new Fault("testLoginToContinueerrorPage  failed.");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("testLoginToContinueerrorPage failed: ", e);
    }
  }

  /*
   * @testName: testFormHAMHasCorrectQualifier
   *
   * @assertion_ids: Security:SPEC:2.4-4
   *
   *
   * @test_Strategy: 1. Send request to access servlet2 2. Receive login
   * page(make sure it the expected login page) 3. Send form response with
   * username and password 4. Receive qualifier and scope info.
   */
  public void testFormHAMHasCorrectQualifier() throws Fault {
    try {

      String pageSec = pageBase + "/servlet2";
      String pageSecurityCheck = pageBase + "/j_security_check";
      String username = "tom";
      String password = "secret1";
      requestAndGetForwardLoginPage(pageSec, "testFormHAMHasCorrectQualifier");

      // Send response to login form with session id cookie:
      request = pageSecurityCheck;
      TestUtil.logMsg(
          "Sending request \"" + request + "\" with login information.");
      Properties postData = new Properties();
      postData.setProperty("j_username", username);
      postData.setProperty("j_password", password);
      response = WebUtil.sendRequest("POST", InetAddress.getByName(hostname),
          portnum, tsurl.getRequest(request), postData, cookies);

      TestUtil.logMsg("response.statusToken:" + response.statusToken);
      TestUtil.logMsg("response.content:" + response.content);

      // Check that the page was found (no error).
      if (response.isError()) {
        TestUtil.logErr("Could not find " + request);
        throw new Fault("testFormHAMHasCorrectQualifier failed.");
      }

      // Call followRedirect() to make sure we receive the required page
      response = followRedirect(response, "testFormHAMHasCorrectQualifier");

      // Print response content
      TestUtil.logMsg("received response content  1: " + response.content);

      String content = response.content;
      String searchString1 = "Have qualifier @Default: true";
      String searchString2 = "Have scope @ApplicationScoped: true";
      if (content.indexOf(searchString1) == -1
          || content.indexOf(searchString2) == -1) {
        TestUtil.logErr(
            "Form HAM not has needed qualifier or scope.  Page received:");
        TestUtil.logErr(response.content);
        TestUtil.logErr("(Should say: \"" + searchString1 + "\")");
        TestUtil.logErr("(Should say: \"" + searchString2 + "\")");
        throw new Fault("testFormHAMHasCorrectQualifier failed.");
      }
      TestUtil.logMsg("Form HAM has needed qualifier and scope.");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("testFormHAMHasCorrectQualifier failed: ", e);
    }
  }

  private void requestAndGetForwardLoginPage(String request, String testName)
      throws Exception {
    // Request restricted jsp page.
    TestUtil.logMsg("Sending request \"" + request + "\"");
    response = WebUtil.sendRequest("GET", InetAddress.getByName(hostname),
        portnum, tsurl.getRequest(request), null, null);

    // Check that the page was found (no error).
    if (response.isError()) {
      TestUtil.logErr("Could not find " + request);
      throw new Fault(testName + " failed.");
    }

    // Extract cookies:
    cookies = response.cookies;

  }

  /**
   * Helper method to check that isUserInRole is working correctly. Searches the
   * given page content for "isUserInRole( x ): !y!" for each x = key in
   * Hashtable and y = corresponding value in hashtable. If all results are as
   * expected, returns true, else returns false.
   */
  private boolean checkRoles(String content, Map<String, Boolean> roleCheck) {
    Iterator<String> keys = roleCheck.keySet().iterator();
    boolean pass = true;

    while (pass && keys.hasNext()) {
      String key = (String) keys.next();
      boolean expected = ((Boolean) roleCheck.get(key)).booleanValue();

      String search = "isUserInRole(\"" + key + "\"): !" + expected + "!";
      String logMsg = "Searching for \"" + search + "\": ";

      if (content.indexOf(search) == -1) {
        pass = false;
        logMsg += "NOT FOUND!";
      } else {
        logMsg += "found.";
      }

      TestUtil.logMsg(logMsg);
    }

    return pass;
  }

  public void cleanup() throws Fault {
    logMsg("cleanup");
  }

  /**
   * Helper method that is used in tests. Performs the following actions:
   *
   * 1. Checks whether the response.statusToken==302 or 301
   * if(response.statusToken==302) || (response.statusToken==301) send request
   * to redirected URL 2. Returns Response object
   *
   * @param response
   *          The initial page response
   * @param testNum
   *          The test number for correct display of error messages.
   */
  public WebUtil.Response followRedirect(WebUtil.Response response,
      String testName) throws Exception {

    // if (response.statusToken=302)
    // send request to redirected URL
    if ((response.statusToken.equals("301"))
        || (response.statusToken.equals("302"))) {
      // We should receive a redirection page:
      if (response.location == null) {
        TestUtil.logErr("redirection URL : null");
        throw new Fault(testName + " failed.");
      }

      // Extract location from redirection and format new request:
      request = WebUtil.getRequestFromURL(response.location);
      TestUtil.logMsg("Redirect to: " + response.location);

      // update cookies if the webserver choose to send cookies,
      // immediately after a successful http post request.
      addNewCookies(cookies, response.cookies);

      // Request redirected page
      TestUtil.logMsg("Sending request \"" + request + "\"");
      response = WebUtil.sendRequest("GET", InetAddress.getByName(hostname),
          portnum, request, null, cookies);

      // After a succesful http post request,
      // Sun's Reference Implementation returns a redirected URL
      // (Note: No cookies are sent back to the client at this point)
      // Only when the client accesses the redirected URL,
      // Sun RI sends a cookie (single sign on cookie) back to
      // the client. So update cookies hashtable with new cookies
      addNewCookies(cookies, response.cookies);

      // Check that the page was found (no error).
      if (response.isError()) {
        TestUtil.logErr("Status Token " + response.statusToken);
        TestUtil.logErr("Could not find " + request);
        throw new Fault(testName + " failed.");
      }
    } else {
      // After a successful post request, if a webserver
      // returns the webresource without redirecting to new URL
      // then update any new cookies received during this process.
      addNewCookies(cookies, response.cookies);

    }

    return response;
  }

  /**
   * Helper method that is used to update cookies
   * 
   * This helper method retrieves cookies from "newCookies" hashtable and
   * updates it to "oldCookies" hashtable
   *
   * @param oldCookies
   *          Hashtable containing original cookies
   * @param newCookies
   *          Hashtable containing new cookies error messages.
   */
  public void addNewCookies(Hashtable oldCookies, Hashtable newCookies) {
    // Add new cookie/cookies to the existing cookies Hashtable
    for (Enumeration e = newCookies.keys(); e.hasMoreElements();) {
      // get cookie name
      String name = (String) e.nextElement();

      // get value for this name
      String value = (String) newCookies.get(name);

      if (oldCookies == null) {
        oldCookies = new Hashtable();
      }

      // Add this name value pair (cookie) to old cookies
      oldCookies.put(name.trim(), value.trim());

    }
  }
}
