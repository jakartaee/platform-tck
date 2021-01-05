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

package com.sun.ts.tests.securityapi.ham.rememberme.test3;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.securityapi.common.client.BaseHtmlUnitClient;

public class Client extends BaseHtmlUnitClient {

  private static final long serialVersionUID = -1092294751303245196L;

  protected String pageBase = "/securityapi_ham_rememberme_test3_web";

  private String searchFor = "The user principal is: "; // (+username)

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
    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  private void assertCorrectContent(String testName, String responseContent)
      throws Fault {

    // Check to make sure we are authenticated by checking the page
    // content. The servlet should output "The user principal is: tom"
    String searchString = searchFor + "tom";
    if (responseContent.indexOf(searchString) == -1) {
      TestUtil.logErr("User Principal incorrect.  Page received:");
      TestUtil.logErr(responseContent);
      TestUtil.logErr("(Should say: \"" + searchString + "\")");
      throw new Fault(testName + " failed.");
    }
    TestUtil.logMsg("User Principal correct.");

    // Check to make sure isUserInRole is working properly:
    Map<String, Boolean> roleCheck = new HashMap<String, Boolean>();
    roleCheck.put("Administrator", new Boolean(true));
    roleCheck.put("Manager", new Boolean(true));
    roleCheck.put("Employee", new Boolean(false));
    // roleCheck.put( "Administrator", new Boolean( false ) );
    if (!checkRoles(responseContent, roleCheck)) {
      TestUtil.logErr("isUserInRole() does not work correctly.");
      TestUtil.logErr("Page Received:");
      TestUtil.logErr(responseContent);
      throw new Fault(testName + " failed.");
    }
    TestUtil.logMsg("isUserInRole() correct.");
  }

  /*
   * @testName: testRememberMeisRememberMe
   *
   * @assertion_ids: Security:JAVADOC:169
   *
   *
   * @test_Strategy: Access protected servlet with credential and
   * isRemember=true, then the next call can access the servlet without
   * crdential and rememberme until logout.
   */
  public void testRememberMeisRememberMe() throws Fault {
    try {

      String pageSec = pageBase + "/servlet";
      String pageSecWithCredential = pageBase
          + "/servlet?name=tom&password=secret1&secureonly=false";
      String pageSecLogout = pageBase + "/servlet?logout=true";

      WebClient webClient = new WebClient();

      // 1. Request page when we're not authenticated
      Page page = getPage(webClient, pageSec);
      if (page.getWebResponse().getStatusCode() != 401) {
        TestUtil.logErr("Should get unauthorized response.");
        throw new Fault("testRememberMeisRememberMe failed.");
      }

      // 2. Authenticate, the isRemember is true in HAM
      page = getPage(webClient, pageSecWithCredential);
      assertCorrectContent("testRememberMeisRememberMe",
          page.getWebResponse().getContentAsString());

      // 3. Request same page again within same http session
      page = getPage(webClient, pageSec);
      assertCorrectContent("testRememberMeisRememberMe",
          page.getWebResponse().getContentAsString());

      // 4. Logout
      getPage(webClient, pageSecLogout);

      // 5. Should get unauthorized response.
      page = getPage(webClient, pageSec);
      if (page.getWebResponse().getStatusCode() != 401) {
        TestUtil.logErr("Should get unauthorized response.");
        throw new Fault("testRememberMeisRememberMe failed.");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("testRememberMeisRememberMe failed: ", e);
    }
  }

  /*
   * @testName: testRememberMecookieMaxAgeSecondsExpression
   *
   * @assertion_ids: Security:JAVADOC:167
   *
   * @test_Strategy: Access protected servlet with credential, then the next
   * call can access the servlet without crdential, until time out.
   */
  public void testRememberMecookieMaxAgeSecondsExpression() throws Fault {
    try {

      String pageSec = pageBase + "/servlet";
      String pageSecWithRemember = pageBase
          + "/servlet?name=tom&password=secret1&cookiemaxage=15&secureonly=false";

      WebClient webClient = new WebClient();

      // 1. Request page when we're not authenticated
      Page page = getPage(webClient, pageSec);
      if (page.getWebResponse().getStatusCode() != 401) {
        TestUtil.logErr("Should get unauthorized response.");
        throw new Fault("testRememberMecookieMaxAgeSecondsExpression failed.");
      }

      // 2. Authenticate with remember me
      page = getPage(webClient, pageSecWithRemember);
      assertCorrectContent("testRememberMecookieMaxAgeSecondsExpression",
          page.getWebResponse().getContentAsString());

      // 3. Request same page again within same http session
      page = getPage(webClient, pageSec);
      assertCorrectContent("testRememberMecookieMaxAgeSecondsExpression",
          page.getWebResponse().getContentAsString());

      try {
        Thread.sleep(16000);
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
      }

      // 4. Should get unauthorized response.
      page = getPage(webClient, pageSec);
      if (page.getWebResponse().getStatusCode() != 401) {
        TestUtil.logErr("Should get unauthorized response.");
        throw new Fault("testRememberMecookieMaxAgeSecondsExpression failed.");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("testRememberMecookieMaxAgeSecondsExpression failed: ",
          e);
    }
  }

  /*
   * @testName: testRememberMecookieHttpOnlyExpression
   *
   * @assertion_ids: Security:JAVADOC:166
   *
   *
   * @test_Strategy: When cookieHttpOnly=false, the HttpOnly should not exit in
   * response header.
   * 
   */
  public void testRememberMecookieHttpOnlyExpression() throws Fault {
    try {
      String pageSecWithRemember = pageBase
          + "/servlet?name=tom&password=secret1&cookiemaxage=5&httponly=false";
      String pageSecLogout = pageBase + "/servlet?logout=true";

      WebClient webClient = new WebClient();

      // 1. Authenticate with remember me
      Page page = getPage(webClient, pageSecWithRemember);
      assertCorrectContent("testRememberMecookieHttpOnlyExpression",
          page.getWebResponse().getContentAsString());

      boolean findHttpOnly = false;
      for (NameValuePair header : page.getWebResponse().getResponseHeaders()) {
        // There maybe multi Set-Cookie, we need check the special JSR375 cookie
        if (header.getName().equalsIgnoreCase("Set-Cookie")
            && header.getValue().toLowerCase().contains("jsr375cookiename")) {
          if (header.getValue().toLowerCase().contains("httponly")) {
            findHttpOnly = true;
          }
        }
      }
      if (findHttpOnly) {
        TestUtil.logErr("Found unexpected HttpOnly flag in response head");
        throw new Fault("testRememberMecookieHttpOnlyExpression failed.");
      }

      // logout
      getPage(webClient, pageSecLogout);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("testRememberMecookieHttpOnlyExpression failed: ", e);
    }
  }

  /*
   * @testName: testRememberMecookieSecureOnlyExpression
   *
   * @assertion_ids: Security:JAVADOC:168
   *
   *
   * @test_Strategy: When cookieSecureOnly=false, the secure flag should not
   * exit in response header.
   * 
   */
  public void testRememberMecookieSecureOnlyExpression() throws Fault {
    try {

      String pageSecWithRemember = pageBase
          + "/servlet?name=tom&password=secret1&cookiemaxage=5&secureonly=false";
      String pageSecLogout = pageBase + "/servlet?logout=true";

      WebClient webClient = new WebClient();

      // 1. Authenticate with remember me
      Page page = getPage(webClient, pageSecWithRemember);
      assertCorrectContent("testRememberMecookieSecureOnlyExpression",
          page.getWebResponse().getContentAsString());

      boolean findSecureOnly = false;
      for (NameValuePair header : page.getWebResponse().getResponseHeaders()) {
        // There maybe multi Set-Cookie, we need check the special JSR375 cookie
        if (header.getName().equalsIgnoreCase("Set-Cookie")
            && header.getValue().toLowerCase().contains("jsr375cookiename")) {
          if (header.getValue().toLowerCase().contains("secure")) {
            findSecureOnly = true;
          }
        }
      }
      if (findSecureOnly) {
        TestUtil.logErr("Found unexpected secure flag in response head");
        throw new Fault("testRememberMecookieSecureOnlyExpression failed.");
      }

      // logout
      getPage(webClient, pageSecLogout);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("testRememberMecookieSecureOnlyExpression failed: ", e);
    }
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
}
