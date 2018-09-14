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

package com.sun.ts.tests.securityapi.ham.customform.base;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.WebUtil;
import com.sun.ts.tests.securityapi.common.client.BaseHtmlUnitClient;

public class Client extends BaseHtmlUnitClient {

  private static final long serialVersionUID = -1092294751303245196L;

  protected String pageBase = "/securityapi_ham_customform_base_web";

  private String searchFor = "The user principal is: "; // (+username)

  private String searchForGetRemoteUser = "getRemoteUser(): "; // (+username)

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

  /*
   * @testName: testCustomFormHAMValidateRequest
   *
   * @assertion_ids: Security:JAVADOC:24; Security:JAVADOC:79;
   * Security:SPEC:2.4-3; Security:SPEC:2.4-5;
   *
   *
   * @test_Strategy: 1. Send request to access protected servlet 2. Receive
   * login page(make sure it the expected login page) 3. Send form response with
   * username and password 4. Receive protected servlet (ensure principal is
   * correct, and ensure getRemoteUser() returns the username, and ensure
   * isUserInRole() is working properly)
   */
  public void testCustomFormHAMValidateRequest() throws Fault {
    try {

      String pageSec = pageBase + "/servlet";
      String username = "tom";
      String password = "secret1";

      WebClient webClient = new WebClient();

      Page page = getPage(webClient, pageSec);

      if (!page.getWebResponse().getContentAsString().contains("Login")) {
        TestUtil.logErr("Could not find login page");
        throw new Fault("testCustomFormHAMValidateRequest failed.");
      }

      HtmlForm form = ((HtmlPage) page).getForms().get(0);

      form.getInputByName("form:username").setValueAttribute(username);

      form.getInputByName("form:password").setValueAttribute(password);

      Page servletPage = form.getInputByValue("Login").click();

      if (servletPage == null) {
        TestUtil.logErr("Could not find " + pageSec);
        throw new Fault("testCustomFormHAMValidateRequest failed.");
      }

      TestUtil.logMsg("response statusToken:"
          + servletPage.getWebResponse().getStatusCode());
      TestUtil.logMsg("responseContent:"
          + servletPage.getWebResponse().getContentAsString());

      String responseContent = servletPage.getWebResponse()
          .getContentAsString();
      // Check to make sure we are authenticated by checking the page
      // content. The servlet should output "The user principal is: tom"
      String searchString = searchFor + username;
      if (responseContent.indexOf(searchString) == -1) {
        TestUtil.logErr("User Principal incorrect.  Page received:");
        TestUtil.logErr(responseContent);
        TestUtil.logErr("(Should say: \"" + searchString + "\")");
        throw new Fault("testCustomFormHAMValidateRequest failed.");
      }
      TestUtil.logMsg("User Principal correct.");

      // Check to make sure getRemoteUser returns the user name.
      searchString = searchForGetRemoteUser + username;
      if (responseContent.indexOf(searchString) == -1) {
        TestUtil.logErr("getRemoteUser() did not return " + username + ":");
        TestUtil.logErr(responseContent);
        TestUtil.logErr("(Should say: \"" + searchString + "\")");
        throw new Fault("testCustomFormHAMValidateRequest failed.");
      }
      TestUtil.logMsg("getRemoteUser() correct.");

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
        throw new Fault("testCustomFormHAMValidateRequest failed.");
      }
      TestUtil.logMsg("isUserInRole() correct.");

      // Now that we are authenticated, try accessing the resource again
      // to ensure we need not go through the login page again.
      page = getPage(webClient, pageSec);

      // Check to make sure we are still authenticated.
      searchString = searchFor + username;
      responseContent = page.getWebResponse().getContentAsString();
      if (responseContent.indexOf(searchString) == -1) {
        TestUtil.logErr("User Principal incorrect.  Page received:");
        TestUtil.logErr(responseContent);
        TestUtil.logErr("(Should say: \"" + searchString + "\")");
        throw new Fault("testCustomFormHAMValidateRequest failed.");
      }
      TestUtil.logMsg("User Principal still correct.");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("testCustomFormHAMValidateRequest failed: ", e);
    }
  }

  /*
   * @testName: testLoginToContinueuseRedirectToLogin
   *
   * @assertion_ids: Security:JAVADOC:83
   *
   * @test_Strategy: 1. Send request to access jspSec.jsp 2. Receive correct
   * login page
   */
  public void testLoginToContinueuseRedirectToLogin() throws Fault {
    try {
      String pageSec = pageBase + "/servlet";
      String pageLogin = pageBase + "/login.jsf";

      Page page = getPage(pageSec);

      if (!page.getWebResponse().getContentAsString().contains("Login")) {
        TestUtil.logErr("Could not find login page");
        throw new Fault("testLoginToContinueuseRedirectToLogin failed.");
      }

      if (!page.getWebResponse().getWebRequest().getUrl().toString()
          .contains(pageLogin)) {
        TestUtil
            .logErr(page.getWebResponse().getWebRequest().getUrl().toString());
        TestUtil.logErr("Not redirect to the login uri.");
        throw new Fault("testLoginToContinueuseRedirectToLogin failed.");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("testLoginToContinueuseRedirectToLogin failed: ", e);
    }
  }

  /*
   * @testName: testCustomFormLoginToContinueErrorPage
   *
   * @assertion_ids: Security:JAVADOC:81;
   *
   *
   * @test_Strategy: 1. Send request to access protected servlet 2. Receive
   * login page(make sure it the expected login page) 3. Send form response with
   * username and wrong password 4. Receive error page as expected
   * 
   */
  public void testCustomFormLoginToContinueErrorPage() throws Fault {
    try {

      String pageSec = pageBase + "/servlet";
      String pageError = pageBase + "/login-error-servlet";
      String username = "tom";
      String password = "wrongpwd";

      WebClient webClient = new WebClient();

      Page page = getPage(webClient, pageSec);

      if (!page.getWebResponse().getContentAsString().contains("Login")) {
        TestUtil.logErr("Could not find login page");
        throw new Fault("testCustomFormLoginToContinueErrorPage failed.");
      }

      HtmlForm form = ((HtmlPage) page).getForms().get(0);

      form.getInputByName("form:username").setValueAttribute(username);

      form.getInputByName("form:password").setValueAttribute(password);

      Page servletPage = form.getInputByValue("Login").click();

      if (servletPage == null) {
        TestUtil.logErr("Could not find " + pageSec);
        throw new Fault("testCustomFormLoginToContinueErrorPage failed.");
      }

      TestUtil.logMsg("response statusToken:"
          + servletPage.getWebResponse().getStatusCode());
      TestUtil.logMsg("responseContent:"
          + servletPage.getWebResponse().getContentAsString());

      String responseContent = servletPage.getWebResponse()
          .getContentAsString();
      // Check to make sure we are authenticated by checking the page
      // content. The servlet should output "The user principal is: tom"
      String searchString = searchFor + "null";
      if (responseContent.indexOf(searchString) == -1) {
        TestUtil.logErr("User Principal incorrect.  Page received:");
        TestUtil.logErr(responseContent);
        TestUtil.logErr("(Should say: \"" + searchString + "\")");
        throw new Fault("testCustomFormLoginToContinueErrorPage failed.");
      }
      TestUtil.logMsg("User Principal is null as expected.");

      if (!servletPage.getWebResponse().getWebRequest().getUrl().toString()
          .contains(pageError)) {
        TestUtil
            .logErr(page.getWebResponse().getWebRequest().getUrl().toString());
        TestUtil.logErr("Not redirect to the error uri.");
        throw new Fault("testCustomFormLoginToContinueErrorPage failed.");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("testCustomFormLoginToContinueErrorPage failed: ", e);
    }
  }

  /*
   * @testName: testCustomFormHAMHasCorrectQualifier
   *
   * @assertion_ids: Security:SPEC:2.4-4
   *
   *
   * @test_Strategy: 1. Send request to access protected servlet 2. Receive
   * login page(make sure it the expected login page) 3. Send form response with
   * username and password 4. Receive qualifier and scope info.
   */
  public void testCustomFormHAMHasCorrectQualifier() throws Fault {
    try {

      String pageSec = pageBase + "/servlet2";
      String username = "tom";
      String password = "secret1";

      WebClient webClient = new WebClient();

      Page page = getPage(webClient, pageSec);

      if (!page.getWebResponse().getContentAsString().contains("Login")) {
        TestUtil.logErr("Could not find login page");
        throw new Fault("testCustomFormHAMHasCorrectQualifier failed.");
      }

      HtmlForm form = ((HtmlPage) page).getForms().get(0);

      form.getInputByName("form:username").setValueAttribute(username);

      form.getInputByName("form:password").setValueAttribute(password);

      Page servletPage = form.getInputByValue("Login").click();

      if (servletPage == null) {
        TestUtil.logErr("Could not find " + pageSec);
        throw new Fault("testCustomFormHAMHasCorrectQualifier failed.");
      }

      TestUtil.logMsg("response statusToken:"
          + servletPage.getWebResponse().getStatusCode());
      TestUtil.logMsg("responseContent:"
          + servletPage.getWebResponse().getContentAsString());

      String responseContent = servletPage.getWebResponse()
          .getContentAsString();

      String searchString1 = "Have qualifier @Default: true";
      String searchString2 = "Have scope @ApplicationScoped: true";
      if (responseContent.indexOf(searchString1) == -1
          || responseContent.indexOf(searchString2) == -1) {
        TestUtil.logErr(
            "Form HAM not has needed qualifier or scope.  Page received:");
        TestUtil.logErr(responseContent);
        TestUtil.logErr("(Should say: \"" + searchString1 + "\")");
        TestUtil.logErr("(Should say: \"" + searchString2 + "\")");
        throw new Fault("testCustomFormHAMHasCorrectQualifier failed.");
      }
      TestUtil.logMsg("Custom Form HAM has needed qualifier and scope.");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("testCustomFormHAMHasCorrectQualifier failed: ", e);
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
