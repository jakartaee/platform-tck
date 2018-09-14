/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.servlet.spec.security.denyUncovered;

import java.io.*;
import java.util.Properties;
import java.util.Collection;
import java.util.Iterator;
import java.net.HttpURLConnection;
import java.net.URL;
import com.sun.javatest.Status;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

/**
 * This class will be used to perform simple servlet invocations. The servlet
 * invocations should be used to test these assertions on the server.
 *
 * We will check for success or failure from within this file. So the actual
 * testcases in this class will simply consist of checking the server side
 * servlet invocations for success or failure.
 *
 * The tests in this class are intended to test the Servlets
 * deny-uncovered-http-methods DD semantic (Servlet 3.1 spec, section 13.8.4.2).
 *
 */
public class Client extends ServiceEETest implements Serializable {
  private Properties props = null;

  private String hostname = null;

  private int portnum = 0;

  // this must be the decoded context path corresponding to the web module
  private String contextPath = "/servlet_sec_denyUncovered";

  private String ctxtTestServlet = contextPath + "/TestServlet"; // specifies
                                                                 // access to
                                                                 // get & put
                                                                 // only

  private String ctxtAllMethodsAllowedAnno = contextPath
      + "/AllMethodsAllowedAnno";

  private String ctxtExcludeAuthConstraint = contextPath
      + "/ExcludeAuthConstraint";

  private String ctxtPartialDDServlet = contextPath + "/PartialDDServlet";

  private String username = "";

  private String password = "";

  public static void main(String args[]) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /**
   * @class.setup_props: logical.hostname.servlet; webServerHost; webServerPort;
   *                     authuser; authpassword; user; password;
   *                     securedWebServicePort;
   *
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;

    try {
      hostname = p.getProperty("webServerHost");
      portnum = Integer.parseInt(p.getProperty("webServerPort"));
      username = p.getProperty("user");
      password = p.getProperty("password");

    } catch (Exception e) {
      logErr("Error: got exception: ", e);
    }
  }

  public void cleanup() throws Fault {
  }

  /**
   * @testName: testAllMethodsAllowedAnno
   *
   * @assertion_ids: Servlet:SPEC:310
   *
   * @test_Strategy: This validates that we have a deny-uncovered-http-method
   *                 set in our web.xml and NO methods explicitly listed in our
   *                 security constraints. Details for this test include the
   *                 following : - we are working with servlet:
   *                 AllMethodsAllowedAnno which uses servlet annotations only
   *                 and has no web.xml references - the only security that
   *                 should be in effect should be whatever is defined in
   *                 annotations. - attempts to access all denied methods shall
   *                 return 200
   * 
   */
  public void testAllMethodsAllowedAnno() throws Fault {

    int httpStatusCode = invokeServlet(ctxtAllMethodsAllowedAnno, "POST");
    if (httpStatusCode != 200) {
      TestUtil.logMsg("Accessing " + ctxtAllMethodsAllowedAnno
          + "  (POST) returns = " + httpStatusCode);
      throw new Fault("testAllMethodsAllowedAnno : FAILED");
    }

    httpStatusCode = invokeServlet(ctxtAllMethodsAllowedAnno, "GET");
    if (httpStatusCode != 200) {
      TestUtil.logMsg("Accessing " + ctxtAllMethodsAllowedAnno
          + "  (GET) returns = " + httpStatusCode);
      throw new Fault("testAllMethodsAllowedAnno : FAILED");
    }

    httpStatusCode = invokeServlet(ctxtAllMethodsAllowedAnno, "PUT");
    if (httpStatusCode != 200) {
      TestUtil.logMsg("Accessing " + ctxtAllMethodsAllowedAnno
          + "  (PUT) returns = " + httpStatusCode);
      throw new Fault("testAllMethodsAllowedAnno : FAILED");
    }

    httpStatusCode = invokeServlet(ctxtAllMethodsAllowedAnno, "DELETE");
    if (httpStatusCode != 200) {
      TestUtil.logMsg("Accessing " + ctxtAllMethodsAllowedAnno
          + "  (DELETE) returns = " + httpStatusCode);
      throw new Fault("testAllMethodsAllowedAnno : FAILED");
    }

    TestUtil.logMsg("testAllMethodsAllowedAnno : PASSED");
  }

  /**
   * @testName: testAccessToMethodAllowed
   *
   * @assertion_ids: Servlet:SPEC:309;
   *
   * @test_Strategy: This validates that we have a deny-uncovered-http-method
   *                 set in our web.xml for a servlet that explicitly specifies
   *                 protection for Get and Post. This means that Get and Post
   *                 can be access but because we are using
   *                 deny-uncovered-http-methods element, everything else will
   *                 be protected/denied access. - this uses servlet TestServlet
   *                 and it's Get & Post methods. - get & put are specified in
   *                 security constraint - so we should be able to access them -
   *                 attempts to access get & put must be alloed (return 200=ok)
   * 
   */
  public void testAccessToMethodAllowed() throws Fault {

    int httpStatusCode = invokeServlet(ctxtTestServlet, "POST");
    if (httpStatusCode != 200) {
      TestUtil.logMsg("Accessing " + ctxtTestServlet + "  (POST) returns = "
          + httpStatusCode);
      throw new Fault("testAccessToMethodAllowed : FAILED");
    }

    httpStatusCode = invokeServlet(ctxtTestServlet, "GET");
    if (httpStatusCode != 200) {
      TestUtil.logMsg("Accessing " + ctxtTestServlet + "  (GET) returns = "
          + httpStatusCode);
      throw new Fault("testAccessToMethodAllowed : FAILED");
    }

    TestUtil.logMsg("testAccessToMethodAllowed : PASSED");
  }

  /**
   * @testName: testDenySomeUncovered
   *
   * @assertion_ids: Servlet:SPEC:309;
   *
   * @test_Strategy: This validates that we have a deny-uncovered-http-method
   *                 set in our web.xml for a servlet that explicitly specifies
   *                 protection for Get and Post. This means that Get and Post
   *                 can be access but because we are using
   *                 deny-uncovered-http-methods element, everything else will
   *                 be protected/denied access. - this uses servlet TestServlet
   *                 and it's Get & Post methods. - get & put are specified in
   *                 security constraint - so we should be able to access them -
   *                 attempts to access get & put must be alloed (return 200=ok)
   * 
   */
  public void testDenySomeUncovered() throws Fault {

    int httpStatusCode = invokeServlet(ctxtTestServlet, "DELETE");
    if (httpStatusCode != 403) {
      TestUtil.logMsg("Accessing " + ctxtTestServlet + "  (DELETE) returns = "
          + httpStatusCode);
      throw new Fault("testDenySomeUncovered : FAILED");
    }

    httpStatusCode = invokeServlet(ctxtTestServlet, "PUT");
    if (httpStatusCode != 403) {
      TestUtil.logMsg("Accessing " + ctxtTestServlet + "  (PUT) returns = "
          + httpStatusCode);
      throw new Fault("testDenySomeUncovered : FAILED");
    }

    TestUtil.logMsg("testDenySomeUncovered : PASSED");
  }

  /**
   * @testName: testExcludeAuthConstraint
   *
   * @assertion_ids: Servlet:SPEC:309;
   *
   * @test_Strategy: This validates that we have a deny-uncovered-http-method
   *                 set in our web.xml on a servlet that uses an
   *                 excluded-auth-constraint in combination with the
   *                 http-method-omission element. Normally using
   *                 http-method-omissions with excluded-auth-constraint would
   *                 cause the listed methods (get & put in this case) to be
   *                 considered uncovered. Adding in the
   *                 deny-uncovered-http-method means we should be denied access
   *                 to get & put. This test has the following details: - this
   *                 uses servlet ExcludeAuthConstraint - get & put are
   *                 specified in security constraint within
   *                 http-method-omission - get & put should be uncovered (based
   *                 on http-method-mission) BUT because of the
   *                 deny-uncovered-http-method elelent, they must be denied!
   * 
   */
  public void testExcludeAuthConstraint() throws Fault {

    int httpStatusCode = invokeServlet(ctxtExcludeAuthConstraint, "GET");
    if (httpStatusCode != 403) {
      TestUtil.logMsg("Accessing " + ctxtExcludeAuthConstraint
          + "  (GET) returns = " + httpStatusCode);
      throw new Fault("testExcludeAuthConstraint : FAILED");
    }

    httpStatusCode = invokeServlet(ctxtExcludeAuthConstraint, "POST");
    if (httpStatusCode != 403) {
      TestUtil.logMsg("Accessing " + ctxtExcludeAuthConstraint
          + "  (POST) returns = " + httpStatusCode);
      throw new Fault("testExcludeAuthConstraint : FAILED");
    }

    TestUtil.logMsg("testExcludeAuthConstraint : PASSED");
  }

  /**
   * @testName: testPartialDDServlet
   *
   * @assertion_ids: Servlet:SPEC:309;
   *
   * @test_Strategy: This validates that we have a deny-uncovered-http-method
   *                 set in our web.xml on a servlet that uses settings from
   *                 both DD and annotations. The security constraints are set
   *                 in the annotated servlet but the web.xml defines
   *                 deny-uncovered-http-method - and that will apply to all
   *                 uncovered methods in the annotated servlet.
   * 
   *                 This test has the following details: - this uses servlet
   *                 PartialDDServlet (with both annotation and DD settings) -
   *                 get & put are specified in security constraint within
   *                 annotation - get & put should be covered (and accessible by
   *                 role Administrato) but all other methods should be
   *                 uncovered from annotation POV - DD decares
   *                 deny-uncovered-http-method and servlet refs so should cause
   *                 the other "uncovered" methods to get "denied"
   * 
   */
  public void testPartialDDServlet() throws Fault {

    TestUtil.logMsg("Invoking " + ctxtPartialDDServlet + "  (GET)");
    int httpStatusCode = invokeServlet(ctxtPartialDDServlet, "GET");
    if (httpStatusCode != 200) {
      TestUtil.logMsg("Accessing " + ctxtPartialDDServlet + "  (GET) returns = "
          + httpStatusCode);
      throw new Fault("testPartialDDServlet : FAILED");
    }

    TestUtil.logMsg("Invoking " + ctxtPartialDDServlet + "  (POST)");
    httpStatusCode = invokeServlet(ctxtPartialDDServlet, "POST");
    if (httpStatusCode != 200) {
      TestUtil.logMsg("Accessing " + ctxtPartialDDServlet
          + "  (POST) returns = " + httpStatusCode);
      throw new Fault("testPartialDDServlet : FAILED");
    }

    TestUtil.logMsg("Invoking " + ctxtPartialDDServlet + "  (PUT)");
    httpStatusCode = invokeServlet(ctxtPartialDDServlet, "PUT");
    if (httpStatusCode != 403) {
      TestUtil.logMsg("Accessing " + ctxtPartialDDServlet + "  (PUT) returns = "
          + httpStatusCode);
      throw new Fault("testPartialDDServlet : FAILED");
    }

    TestUtil.logMsg("Invoking " + ctxtPartialDDServlet + "  (DELETE)");
    httpStatusCode = invokeServlet(ctxtPartialDDServlet, "DELETE");
    if (httpStatusCode != 403) {
      TestUtil.logMsg("Accessing " + ctxtPartialDDServlet
          + "  (DELETE) returns = " + httpStatusCode);
      throw new Fault("testPartialDDServlet : FAILED");
    }

    TestUtil.logMsg("testPartialDDServlet : PASSED");
  }

  /*
   * Convenience method that will establish a url connections and perform a
   * get/post request. A username and password will be passed in the request
   * header and they will be encoded using the BASE64Encoder class. returns the
   * http status code.
   */
  private int invokeServlet(String sContext, String requestMethod) {
    int code = 200;

    TSURL ctsurl = new TSURL();
    if (!sContext.startsWith("/")) {
      sContext = "/" + sContext;
    }

    String url = ctsurl.getURLString("http", hostname, portnum, sContext);
    try {
      URL newURL = new URL(url);

      // Encode authData
      // hint: make sure username and password are valid for your
      // (J2EE) security realm otherwise you recieve http 401 error.
      String authData = username + ":" + password;
      TestUtil.logMsg("authData : " + authData);

      BASE64Encoder encoder = new BASE64Encoder();

      String encodedAuthData = encoder.encode(authData.getBytes());
      TestUtil.logMsg("encoded authData : " + encodedAuthData);

      // open URLConnection
      HttpURLConnection conn = (HttpURLConnection) newURL.openConnection();

      // set request property
      conn.setDoOutput(true);
      conn.setDoInput(true);
      conn.setRequestProperty("Authorization",
          "Basic " + encodedAuthData.trim());
      conn.setRequestMethod(requestMethod); // POST or GET etc
      conn.connect();

      TestUtil.logMsg("called HttpURLConnection.connect() for url: " + url);
      code = conn.getResponseCode();
      TestUtil.logMsg("Got response code of: " + code);
      String str = conn.getResponseMessage();
      TestUtil.logMsg("Got response string of: " + str);
      /*
       * // not used right now but left here in case we need it InputStream
       * content = (InputStream)conn.getInputStream(); BufferedReader in = new
       * BufferedReader(new InputStreamReader(content));
       * 
       * try { String line; while ((line = in.readLine()) != null) {
       * TestUtil.logMsg(line); } } finally { in.close(); }
       */

    } catch (Exception e) {
      TestUtil.logMsg(
          "Abnormal return status encountered while invoking " + sContext);
      TestUtil.logMsg("Exception Message was:  " + e.getMessage());
      // e.printStackTrace();
    }

    return code;
  } // invokeServlet()

}
