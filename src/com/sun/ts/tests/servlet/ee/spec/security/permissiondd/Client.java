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

package com.sun.ts.tests.servlet.ee.spec.security.permissiondd;

import java.io.*;
import java.util.Properties;

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
 * Permissions can be declared for an application at the app server level (as
 * might be done in server.policy) or at a local app/module level (as might be
 * done in permissions.xml). This class will test permissons and grants using a
 * variety of settings from both appserver and permissions.xml levels.
 *
 */
public class Client extends ServiceEETest implements Serializable {
  private Properties props = null;

  private String hostname = null;

  private int portnum = 0;

  // this must be the decoded context path corresponding to the web module
  private String contextPath = "/servlet_ee_spec_securitypermissiondd";

  private String crdServletPath = contextPath + "/TestServlet";

  private String username = "";

  private String password = "";

  private String appContextHostname;

  private String servletAppContext = null;

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

      appContextHostname = p.getProperty("logical.hostname.servlet");

      TestUtil.logMsg("setup(): appContextHostname = " + appContextHostname);
      TestUtil.logMsg("setup(): servletAppContext = " + servletAppContext);

    } catch (Exception e) {
      logErr("Error: got exception: ", e);
    }
  }

  public void cleanup() throws Fault {
  }

  /**
   * @testName: ValidateCustomPerm
   *
   * @assertion_ids: JavaEE:SPEC:290; JavaEE:SPEC:293;
   *
   * @test_Strategy: This validates that we have a particular grant under the
   *                 following conditions: - this is testing permissions.xml
   *                 within a Servlet - using locally declared custom Permission
   *                 impl (CTSPermission1) (note: this custom perm does not
   *                 support actions) - have declared grant for this
   *                 (CTSPermission1) in permissions.xml - also declared grant
   *                 for CTSPermission1 at higher app server level (thus we have
   *                 grant declared twice.)
   * 
   */
  public void ValidateCustomPerm() throws Fault {
    String strMsg1 = "SUCCESS:  validateCustomPerm passed.";

    String str = invokeServletAndGetResponse(crdServletPath, "POST",
        "ValidateCustomPerm");
    TestUtil.logMsg("ValidateCustomPerm : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("ValidateCustomPerm : FAILED");
    }

    TestUtil.logMsg("ValidateCustomPerm : PASSED");
  }

  /**
   * @testName: ValidateCustomPermInLib
   *
   * @assertion_ids: JavaEE:SPEC:290; JavaEE:SPEC:293; JavaEE:SPEC:314;
   *
   * @test_Strategy: This validates that we have a particular grant under the
   *                 following conditions: - this is testing permissions.xml
   *                 within a library thats embedded in a Servlet - using
   *                 locally declared custom Permission impl (CTSPermission1)
   *                 (note: this custom perm does not support actions) - have
   *                 declared grant for this (CTSPermission1) in permissions.xml
   *                 - also declared grant for CTSPermission1 at higher app
   *                 server level (thus we have grant declared twice.)
   *
   */
  public void ValidateCustomPermInLib() throws Fault {
    String strMsg1 = "SUCCESS:  ValidateCustomPermInLib passed.";

    String str = invokeServletAndGetResponse(crdServletPath, "POST",
        "ValidateCustomPermInLib");
    TestUtil.logMsg("ValidateCustomPermInLib : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("ValidateCustomPermInLib : FAILED");
    }

    TestUtil.logMsg("ValidateCustomPermInLib : PASSED");
  }

  /**
   * @testName: ValidateCustomPermFromAppServer
   *
   * @assertion_ids: JavaEE:SPEC:290; JavaEE:SPEC:293; JavaEE:SPEC:295;
   *
   * @test_Strategy: This is basically testing that when a permission is set
   *                 within the app server but NOT listed/set within the
   *                 permissions.xml, our deployed app will still be granted
   *                 that permission. Thus omitting it from the permissions.xml
   *                 must not cause the permission to be denied.
   *
   *                 This validates that we have a particular grant under the
   *                 following conditions: - this is testing permissions.xml
   *                 within a Servlet - using a custom Permission defined wihtin
   *                 app server (via initial config) (note: this custom perm is
   *                 CTSPermission1 named "CTSPermission1_name2") - have NO
   *                 declared grant for this (CTSPermission1_name2) in
   *                 permissions.xml - since the perm IS defined within the
   *                 appserver but is not defined within the local
   *                 permissions.xml, the app componets must still have
   *                 permission as set within app server.
   */
  public void ValidateCustomPermFromAppServer() throws Fault {
    String strMsg1 = "SUCCESS:  validateCustomPermFromAppServer passed.";

    String str = invokeServletAndGetResponse(crdServletPath, "POST",
        "ValidateCustomPermFromAppServer");
    TestUtil.logMsg("ValidateCustomPermFromAppServer : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("ValidateCustomPermFromAppServer : FAILED");
    }

    TestUtil.logMsg("ValidateCustomPermFromAppServer : PASSED");
  }

  /**
   * @testName: ValidateCustomPermFromAppServerInLib
   *
   * @assertion_ids: JavaEE:SPEC:290; JavaEE:SPEC:293; JavaEE:SPEC:295;
   *
   * @test_Strategy: This is basically testing that when a permission is set
   *                 within the app server but NOT listed/set within the
   *                 permissions.xml, our deployed app will still be granted
   *                 that permission. Thus omitting it from the permissions.xml
   *                 must not cause the permission to be denied.
   *
   *                 This validates that we have a particular grant under the
   *                 following conditions: - this is testing permissions.xml
   *                 within a Servlet - using a custom Permission defined wihtin
   *                 app server (via initial config) (note: this custom perm is
   *                 CTSPermission1 named "CTSPermission1_name2") - have NO
   *                 declared grant for this (CTSPermission1_name2) in
   *                 permissions.xml - since the perm IS defined within the
   *                 appserver but is not defined within the local
   *                 permissions.xml, the app componets must still have
   *                 permission as set within app server.
   */
  public void ValidateCustomPermFromAppServerInLib() throws Fault {
    String strMsg1 = "SUCCESS:  ValidateCustomPermFromAppServerInLib passed.";

    String str = invokeServletAndGetResponse(crdServletPath, "POST",
        "ValidateCustomPermFromAppServerInLib");
    TestUtil.logMsg("ValidateCustomPermFromAppServerInLib : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("ValidateCustomPermFromAppServerInLib : FAILED");
    }

    TestUtil.logMsg("ValidateCustomPermFromAppServerInLib : PASSED");
  }

  /**
   * @testName: ValidateMissingPermFails
   *
   * @assertion_ids: JavaEE:SPEC:289; JavaEE:SPEC:290;
   *
   * @test_Strategy: This validates that we are NOT granted a certain permission
   *                 and so when we try to do our access checks, we expect to
   *                 see an AccessControl exception returned. This validates
   *                 that we have no grant under the following conditions: -
   *                 this is testing permissions.xml within a Servlet - using
   *                 locally declared Permission impl (CTSPermission1) - have NO
   *                 declared grant in permissions.xml for locally defined
   *                 permission CTSPermission1 with name="CTSPermission2_name" -
   *                 have NO declared grant at higher app server level (e.g.
   *                 server.policy etc) This validates that an
   *                 AccessControlException is properly thrown.
   * 
   */
  public void ValidateMissingPermFails() throws Fault {
    String strMsg1 = "SUCCESS:  validateMissingPermFails passed.";

    String str = invokeServletAndGetResponse(crdServletPath, "POST",
        "ValidateMissingPermFails");

    TestUtil.logMsg("ValidateMissingPermFails : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("ValidateMissingPermFails : FAILED");
    }

    TestUtil.logMsg("ValidateMissingPermFails : PASSED");
  }

  /**
   * @testName: ValidateMissingPermFailsInLib
   *
   * @assertion_ids: JavaEE:SPEC:289; JavaEE:SPEC:290; JavaEE:SPEC:314;
   *
   * @test_Strategy: This validates that we are NOT granted a certain permission
   *                 and so when we try to do our access checks, we expect to
   *                 see an AccessControl exception returned. This validates
   *                 that we have no grant under the following conditions: -
   *                 this is testing permissions.xml within a library thats
   *                 embedded in a Servlet - using locally declared Permission
   *                 impl (CTSPermission1) - have NO declared grant in
   *                 permissions.xml - have NO declared grant at higher app
   *                 server level (e.g. server.policy etc) This validates that
   *                 an AccessControlException is thrown an expected.
   */
  public void ValidateMissingPermFailsInLib() throws Fault {
    String strMsg1 = "SUCCESS:  ValidateMissingPermFailsInLib passed.";

    String str = invokeServletAndGetResponse(crdServletPath, "POST",
        "ValidateMissingPermFailsInLib");

    TestUtil.logMsg("ValidateMissingPermFailsInLib : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("ValidateMissingPermFailsInLib : FAILED");
    }

    TestUtil.logMsg("ValidateMissingPermFailsInLib : PASSED");
  }

  /**
   * @testName: ValidateRequiredPermSet
   *
   * @assertion_ids: JavaEE:SPEC:290; JavaEE:SPEC:296; JavaEE:SPEC:291;
   *
   * @test_Strategy: This validates that the required set of perms are properly
   *                 granted so when we try to do our access checks, we expect
   *                 to see NO AccessControl exception returned. This validates
   *                 that we have grants for multiple perms under the following
   *                 conditions: - this is testing permissions.xml within a
   *                 Servlet - have locally declared grants in permissions.xml -
   *                 we shouldnt care if grants are declared (or not declared)
   *                 at the higher app server level (e.g. via server.policy) -
   *                 This is validating the following are properly granted thru
   *                 permissions.xml: RuntimePermission("loadLibrary.*")
   *                 RuntimePermission("queuePrintJob") SocketPermission("*",
   *                 "connect") FilePermission("*", "read")
   *                 PropertyPermission("*", "read") note: there is a more
   *                 comprehendive listing in the Java EE7 spec, Table EE.6-2.
   *                 We are testing a subset of those.
   */
  public void ValidateRequiredPermSet() throws Fault {
    String strMsg1 = "SUCCESS:  validateRequiredPermSet passed.";

    String str = invokeServletAndGetResponse(crdServletPath, "POST",
        "ValidateRequiredPermSet");

    TestUtil.logMsg("ValidateRequiredPermSet : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("ValidateRequiredPermSet : FAILED");
    }

    TestUtil.logMsg("ValidateRequiredPermSet : PASSED");
  }

  /**
   * @testName: ValidateRequiredPermSetInLib
   *
   * @assertion_ids: JavaEE:SPEC:290; JavaEE:SPEC:296; JavaEE:SPEC:314;
   *                 JavaEE:SPEC:291;
   *
   * @test_Strategy: This validates that the servlet supports the list of
   *                 required perms that are listed in JavaEE7 spec, Table
   *                 EE.6-2. This tests the permission.xml from within a library
   *                 thats embedded within the servlets archive (permddlib.jar).
   *
   *                 This validates that we have grants for multiple perms under
   *                 the following conditions: - this is testing permissions.xml
   *                 within a Servlet - have locally declared grants in
   *                 permissions.xml - we shouldnt care if grants are declared
   *                 (or not declared) at the higher app server level (e.g. via
   *                 server.policy) - This is validating the following are
   *                 properly granted thru permissions.xml:
   *                 RuntimePermission("loadLibrary.*")
   *                 RuntimePermission("queuePrintJob") SocketPermission("*",
   *                 "connect") FilePermission("*", "read")
   *                 PropertyPermission("*", "read") note: there is a more
   *                 comprehendive listing in the Java EE7 spec, Table EE.6-2.
   *                 We are testing a subset of those.
   * 
   */
  public void ValidateRequiredPermSetInLib() throws Fault {
    String strMsg1 = "SUCCESS:  ValidateRequiredPermSetInLib passed.";

    String str = invokeServletAndGetResponse(crdServletPath, "POST",
        "ValidateRequiredPermSetInLib");

    TestUtil.logMsg("ValidateRequiredPermSetInLib : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("ValidateRequiredPermSetInLib : FAILED");
    }

    TestUtil.logMsg("ValidateRequiredPermSetInLib : PASSED");
  }

  /**
   * @testName: ValidateLocalGrantForCustomPerm
   *
   * @assertion_ids: JavaEE:SPEC:292; JavaEE:SPEC:293; JavaEE:SPEC:303;
   *                 JavaEE:SPEC:304;
   *
   * @test_Strategy: This validates that we have a particular grant under the
   *                 following conditions: - this is testing permissions.xml
   *                 within a Servlet - using locally declared custom Permission
   *                 (CTSPermission2) declared for property
   *                 "CTSPermission2_name"; note: CTSPermission2 does NOT have
   *                 support for actions. - have declared grant in
   *                 permissions.xml - have NO declared grant at higher app
   *                 server level (e.g. server.policy etc) We should be allowed
   *                 access control.
   */
  public void ValidateLocalGrantForCustomPerm() throws Fault {
    String strMsg1 = "SUCCESS:  validateLocalGrantForCustomPerm passed.";

    String str = invokeServletAndGetResponse(crdServletPath, "POST",
        "ValidateLocalGrantForCustomPerm");

    TestUtil.logMsg("ValidateLocalGrantForCustomPerm : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("ValidateLocalGrantForCustomPerm : FAILED");
    }

    TestUtil.logMsg("ValidateLocalGrantForCustomPerm : PASSED");
  }

  /**
   * @testName: ValidateLocalGrantForCustomPermInLib
   *
   * @assertion_ids: JavaEE:SPEC:292; JavaEE:SPEC:293; JavaEE:SPEC:303;
   *                 JavaEE:SPEC:304; JavaEE:SPEC:314;
   *
   * @test_Strategy: This validates that we have a particular grant under the
   *                 following conditions: - this is testing permissions.xml
   *                 within a library thats embedded in a Servlet - using
   *                 locally declared custom Permission impl (CTSPermission2)
   *                 note: CTSPermission2 does NOT have support for actions. -
   *                 have declared grant in permissions.xml - have NO declared
   *                 grant at higher app server level (e.g. server.policy etc)
   * 
   */
  public void ValidateLocalGrantForCustomPermInLib() throws Fault {
    String strMsg1 = "SUCCESS:  ValidateLocalGrantForCustomPermInLib passed.";

    String str = invokeServletAndGetResponse(crdServletPath, "POST",
        "ValidateLocalGrantForCustomPermInLib");

    TestUtil.logMsg("ValidateLocalGrantForCustomPermInLib : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("ValidateLocalGrantForCustomPermInLib : FAILED");
    }

    TestUtil.logMsg("ValidateLocalGrantForCustomPermInLib : PASSED");
  }

  /**
   * @testName: ValidateRestrictedLocalPerm
   *
   * @assertion_ids: JavaEE:SPEC:292; JavaEE:SPEC:293; JavaEE:SPEC:303;
   *                 JavaEE:SPEC:304;
   *
   * @test_Strategy: This validates that we have a particular grant under the
   *                 following conditions: - this is testing permissions.xml
   *                 within a Servlet - using our own perm
   *                 (CTSPropertyPermission) that is referenced in
   *                 permission.xml and has read but not write assigned note:
   *                 CTSPropertyPermission has support for actions - have NO
   *                 declared grants for CTSPropertyPermission at higher app
   *                 server level (e.g. server.policy etc) so that it is ONLY
   *                 bundled in this local app and ref'd in permission.xml
   *
   */
  public void ValidateRestrictedLocalPerm() throws Fault {
    String strMsg1 = "SUCCESS:  validateRestrictedLocalPerm passed.";

    String str = invokeServletAndGetResponse(crdServletPath, "POST",
        "ValidateRestrictedLocalPerm");

    TestUtil.logMsg("ValidateRestrictedLocalPerm : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("ValidateRestrictedLocalPerm : FAILED");
    }

    TestUtil.logMsg("ValidateRestrictedLocalPerm : PASSED");
  }

  /**
   * @testName: ValidateRestrictedLocalPermInLib
   *
   * @assertion_ids: JavaEE:SPEC:292; JavaEE:SPEC:293; JavaEE:SPEC:303;
   *                 JavaEE:SPEC:304; JavaEE:SPEC:314;
   *
   * @test_Strategy: This validates that we have a particular grant under the
   *                 following conditions: - this is testing permissions.xml
   *                 within a library thats embedded in a Servlet - using our
   *                 own perm (CTSPropertyPermission) that is referenced in
   *                 permission.xml and has read but not write assigned note:
   *                 CTSPropertyPermission has support for actions - have NO
   *                 declared grants for CTSPropertyPermission at higher app
   *                 server level (e.g. server.policy etc) so that it is ONLY
   *                 bundled in this local app and ref'd in permission.xml
   *
   */
  public void ValidateRestrictedLocalPermInLib() throws Fault {
    String strMsg1 = "SUCCESS:  ValidateRestrictedLocalPermInLib passed.";

    String str = invokeServletAndGetResponse(crdServletPath, "POST",
        "ValidateRestrictedLocalPermInLib");

    TestUtil.logMsg("ValidateRestrictedLocalPermInLib : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("ValidateRestrictedLocalPermInLib : FAILED");
    }

    TestUtil.logMsg("ValidateRestrictedLocalPermInLib : PASSED");
  }

  /**
   * @testName: ValidateLocalPermsInvalidNameInLib
   *
   * @assertion_ids: JavaEE:SPEC:292; JavaEE:SPEC:293; JavaEE:SPEC:303;
   *                 JavaEE:SPEC:304; JavaEE:SPEC:314;
   *
   * @test_Strategy: This validates that we have a particular grant under the
   *                 following conditions: - this is testing permissions.xml
   *                 within a library thats embedded in a Servlet - using our
   *                 own perm (CTSPropertyPermission) that is referenced in
   *                 permission.xml and has read but not write assigned note:
   *                 CTSPropertyPermission has support for actions - we have
   *                 perm (CTSPropertyPermission) WITH read action *but* the
   *                 perm that is declared in permissions.xml has a different
   *                 name then what we are trying to validate in our call to
   *                 AccessController.checkPermission - so we expect
   *                 AccessControlException to be thrown (in the lib call) -
   *                 also, have NO declared grants for CTSPropertyPermission at
   *                 higher app server level (e.g. server.policy etc) so that it
   *                 is ONLY bundled in this local app and ref'd in
   *                 permission.xml
   *
   */
  public void ValidateLocalPermsInvalidNameInLib() throws Fault {
    String strMsg1 = "SUCCESS:  ValidateLocalPermsInvalidNameInLib passed.";

    String str = invokeServletAndGetResponse(crdServletPath, "POST",
        "ValidateLocalPermsInvalidNameInLib");

    TestUtil.logMsg("ValidateLocalPermsInvalidNameInLib : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("ValidateLocalPermsInvalidNameInLib : FAILED");
    }

    TestUtil.logMsg("ValidateLocalPermsInvalidNameInLib : PASSED");
  }

  /**
   * @testName: ValidateLocalPermsInvalidName
   *
   * @assertion_ids: JavaEE:SPEC:292; JavaEE:SPEC:293; JavaEE:SPEC:303;
   *                 JavaEE:SPEC:304; JavaEE:SPEC:314;
   *
   * @test_Strategy: This validates that we have a particular grant under the
   *                 following conditions: - this is testing permissions.xml
   *                 within a Servlet - using our own perm
   *                 (CTSPropertyPermission) that is referenced in
   *                 permission.xml and has read but not write assigned note:
   *                 CTSPropertyPermission has support for actions - we have
   *                 perm (CTSPropertyPermission) WITH read action *but* the
   *                 perm that is declared in permissions.xml has a different
   *                 name then what we are trying to validate in our call to
   *                 AccessController.checkPermission - so we expect
   *                 AccessControlException to be thrown - also, have NO
   *                 declared grants for CTSPropertyPermission at higher app
   *                 server level (e.g. server.policy etc) so that it is ONLY
   *                 bundled in this local app and ref'd in permission.xml
   *
   */
  public void ValidateLocalPermsInvalidName() throws Fault {
    String strMsg1 = "SUCCESS:  validateLocalPermsInvalidName passed.";

    String str = invokeServletAndGetResponse(crdServletPath, "POST",
        "ValidateLocalPermsInvalidName");

    TestUtil.logMsg("ValidateLocalPermsInvalidName : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("ValidateLocalPermsInvalidName : FAILED");
    }

    TestUtil.logMsg("ValidateLocalPermsInvalidName : PASSED");
  }

  /*
   * Convenience method that will establish a url connections and perform a
   * get/post request. A username and password will be passed in the request
   * header and they will be encoded using the BASE64Encoder class.
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

      InputStream content = (InputStream) conn.getInputStream();
      BufferedReader in = new BufferedReader(new InputStreamReader(content));

      try {
        String line;
        while ((line = in.readLine()) != null) {
          TestUtil.logMsg(line);
        }
      } finally {
        in.close();
      }

    } catch (Exception e) {
      TestUtil.logMsg(
          "Abnormal return status encountered while invoking " + sContext);
      TestUtil.logMsg("Exception Message was:  " + e.getMessage());
      // e.printStackTrace();
    }

    return code;
  } // invokeServlet()

  /*
   * This is a convenience method used to post a url to a servlet so that our
   * servlet can do some tests and send back status about success or failure.
   * This passes some params onto the request/context so that the servlet will
   * have info it needs in order to properly perform its serverside ACF and ACP
   * tests.
   *
   */
  private String invokeServletAndGetResponse(String sContext,
      String requestMethod) {
    return invokeServletAndGetResponse(sContext, requestMethod, null);
  }

  private String invokeServletAndGetResponse(String sContext,
      String requestMethod, String testMethod) {

    TSURL ctsurl = new TSURL();
    if (!sContext.startsWith("/")) {
      sContext = "/" + sContext;
    }

    // add some servlet params onto our context
    if (testMethod != null) {
      sContext = sContext + "?" + "method.under.test=" + testMethod;
    }

    TestUtil.logMsg("sContext = " + sContext);
    TestUtil.logMsg("passing to servlet:  testMethod = " + testMethod);

    String url = ctsurl.getURLString("http", hostname, portnum, sContext);
    String retVal = null;

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
      retVal = conn.getResponseMessage() + "\n";
      TestUtil.logMsg("Got response string of: " + retVal);

      InputStream content = (InputStream) conn.getInputStream();
      BufferedReader in = new BufferedReader(new InputStreamReader(content));
      try {
        String line;
        while ((line = in.readLine()) != null) {
          retVal = retVal + line;
          TestUtil.logMsg(line);
        }
      } finally {
        in.close();
      }
    } catch (Exception e) {
      TestUtil.logMsg(
          "Abnormal return status encountered while invoking " + sContext);
      TestUtil.logMsg("Exception Message was:  " + e.getMessage());
    }

    return retVal;
  } // invokeServletAndGetResponse()

}
