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

package com.sun.ts.tests.connector.resourceDefs.servlet;

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

import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionFactory;
import javax.naming.InitialContext;
import com.sun.ts.tests.connector.util.DBSupport;
import com.sun.ts.tests.common.connector.whitebox.TSDataSource;
import com.sun.ts.tests.common.connector.whitebox.TSConnection;

/**
 * This class will be used to perform simple servlet invocations. The servlet
 * invocations should be used to test Connector Resource Definition (CRD) uses.
 *
 * We will check for success or failure from within this file. So the actual
 * testcases in this class will simply consist of checking the server side
 * servlet invocations for success or failure.
 *
 */
public class Client extends ServiceEETest implements Serializable {
  private Properties props = null;

  private String hostname = null;

  private int portnum = 0;

  // this must be the decoded context path corresponding to the web module
  private String contextPath = "/servlet_resourcedefs_web";

  private String crdServletPath = contextPath + "/CRDTestServlet";

  private String aodServletPath = contextPath + "/AODTestServlet";

  private String username = "";

  private String password = "";

  private String appContextHostname;

  private String RARGlobalScopedJndiName = "java:global/env/CRDTestServlet_Global_ConnectorResource";

  private String RARModuleScopedJndiName = "java:module/env/CRDTestServlet_Module_ConnectorResource";

  private String RARCompScopedJndiName = "java:comp/env/CRDTestServlet_Comp_ConnectorResource";

  private String RARAppScopedJndiName = "java:app/env/CRDTestServlet_App_ConnectorResource";

  private String AODGlobalScopedJndiName = "java:global/env/CTSAdminObjectForGlobalScope";

  private String AODModuleScopedJndiName = "java:module/env/CTSAdminObjectForModuleScope";

  private String AODCompScopedJndiName = "java:comp/env/CTSAdminObjectForCompScope";

  private String AODAppScopedJndiName = "java:app/env/CTSAdminObjectForAppScope";

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
  /*
   * resdef_global_resdef; resdef_app_resdef; resdef_module_resdef;
   * resdef_comp_resdef;
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
   * @testName: ValidateGlobalResourceDef
   *
   * @assertion_ids: JavaEE:SPEC:306; JavaEE:SPEC:307; JavaEE:SPEC:310;
   *
   * @test_Strategy: - this calls a servlet and passes info about what to test
   *                 as request params. - this is going to test that a Servlet
   *                 can create a ConnectionFactoryDefinition in the global
   *                 scope (e.g. java:/global/env/..) - this verifies that
   *                 creation worked by doing a lookup of the
   *                 ConnectionFactoryDefinition's jndiName.
   * 
   */
  public void ValidateGlobalResourceDef() throws Fault {
    String strMsg1 = "CRDTestServlet->ValidateGlobalResourceDef() passed";

    String str = invokeServletAndGetResponse(crdServletPath, "POST",
        "ValidateGlobalResourceDef", RARGlobalScopedJndiName);
    TestUtil.logMsg("ValidateGlobalResourceDef : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("ValidateGlobalResourceDef : FAILED");
    }

    TestUtil.logMsg("ValidateGlobalResourceDef : PASSED");
  }

  /**
   * @testName: ValidateAppResourceDef
   *
   * @assertion_ids: JavaEE:SPEC:306; JavaEE:SPEC:307; JavaEE:SPEC:310;
   *
   * @test_Strategy: - this calls a servlet and passes info about what to test
   *                 as request params. - this is going to test that a Servlet
   *                 can create a ConnectionFactoryDefinition in the app scope
   *                 (e.g. java:/app/env/..) - this verifies that creation
   *                 worked by doing a lookup of the
   *                 ConnectionFactoryDefinition's jndiName.
   * 
   */
  public void ValidateAppResourceDef() throws Fault {
    String strMsg1 = "CRDTestServlet->ValidateAppResourceDef() passed";

    String str = invokeServletAndGetResponse(crdServletPath, "POST",
        "ValidateAppResourceDef", RARAppScopedJndiName);

    TestUtil.logMsg("ValidateAppResourceDef : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("ValidateAppResourceDef : FAILED");
    }

    TestUtil.logMsg("ValidateAppResourceDef : PASSED");
  }

  /**
   * @testName: ValidateModuleResourceDef
   *
   * @assertion_ids: JavaEE:SPEC:306; JavaEE:SPEC:307; JavaEE:SPEC:310;
   *
   * @test_Strategy: - this calls a servlet and passes info about what to test
   *                 as request params. - this is going to test that a Servlet
   *                 can create a ConnectionFactoryDefinition in the module
   *                 scope (e.g. java:/module/env/..) - this verifies that
   *                 creation worked by doing a lookup of the
   */
  public void ValidateModuleResourceDef() throws Fault {
    String strMsg1 = "CRDTestServlet->ValidateModuleResourceDef() passed";

    String str = invokeServletAndGetResponse(crdServletPath, "POST",
        "ValidateModuleResourceDef", RARModuleScopedJndiName);

    TestUtil.logMsg("ValidateModuleResourceDef : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("ValidateModuleResourceDef : FAILED");
    }

    TestUtil.logMsg("ValidateModuleResourceDef : PASSED");
  }

  /**
   * @testName: ValidateCompResourceDef
   *
   * @assertion_ids: JavaEE:SPEC:306; JavaEE:SPEC:307; JavaEE:SPEC:310;
   *
   * @test_Strategy: - this calls a servlet and passes info about what to test
   *                 as request params. - this is going to test that a Servlet
   *                 can create a ConnectionFactoryDefinition in the component
   *                 scope (e.g. java:/comp/env/..) - this verifies that
   *                 creation worked by doing a lookup of the
   *                 ConnectionFactoryDefinition's jndiName.
   */
  public void ValidateCompResourceDef() throws Fault {
    String strMsg1 = "CRDTestServlet->ValidateCompResourceDef() passed";

    String str = invokeServletAndGetResponse(crdServletPath, "POST",
        "ValidateCompResourceDef", RARCompScopedJndiName);

    TestUtil.logMsg("ValidateCompResourceDef : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("ValidateCompResourceDef : FAILED");
    }

    TestUtil.logMsg("ValidateCompResourceDef : PASSED");
  }

  /**
   * @testName: ValidateGlobalAdminObj
   *
   * @assertion_ids: Connector:SPEC:321; Connector:SPEC:326; Connector:SPEC:327;
   *
   * @test_Strategy: - this calls a servlet and passes info about what to test
   *                 as request params. - this is going to test that a Servlet
   *                 can create a ConnectorAdminObj in the global scope (e.g.
   *                 java:/global/env/..) - this verifies that creation worked
   *                 by doing a lookup of the ConnectorAdminObj's jndiName.
   * 
   */
  public void ValidateGlobalAdminObj() throws Fault {
    String strMsg1 = "ValidateGlobalAdminObj() passed.";

    String str = invokeServletAndGetResponse(aodServletPath, "POST",
        "ValidateGlobalAdminObj", AODGlobalScopedJndiName);
    TestUtil.logMsg("ValidateGlobalAdminObj : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("ValidateGlobalAdminObj : FAILED");
    }

    TestUtil.logMsg("ValidateGlobalAdminObj : PASSED");
  }

  /**
   * @testName: ValidateAppAdminObj
   *
   * @assertion_ids: Connector:SPEC:321; Connector:SPEC:326; Connector:SPEC:327;
   *
   * @test_Strategy: - this calls a servlet and passes info about what to test
   *                 as request params. - this is going to test that a Servlet
   *                 can create a ConnectorAdminObj in the app scope (e.g.
   *                 java:/app/env/..) - this verifies that creation worked by
   *                 doing a lookup of the ConnectorAdminObj's jndiName.
   * 
   */
  public void ValidateAppAdminObj() throws Fault {
    String strMsg1 = "ValidateAppAdminObj() passed.";

    String str = invokeServletAndGetResponse(aodServletPath, "POST",
        "ValidateAppAdminObj", AODAppScopedJndiName);

    TestUtil.logMsg("ValidateAppAdminObj : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("ValidateAppAdminObj : FAILED");
    }

    TestUtil.logMsg("ValidateAppAdminObj : PASSED");
  }

  /**
   * @testName: ValidateModuleAdminObj
   *
   * @assertion_ids: Connector:SPEC:321; Connector:SPEC:326; Connector:SPEC:327;
   *
   * @test_Strategy: - this calls a servlet and passes info about what to test
   *                 as request params. - this is going to test that a Servlet
   *                 can create a ConnectorAdminObj in the module scope (e.g.
   *                 java:/module/env/..) - this verifies that creation worked
   *                 by doing a lookup of the
   */
  public void ValidateModuleAdminObj() throws Fault {
    String strMsg1 = "ValidateModuleAdminObj() passed.";

    String str = invokeServletAndGetResponse(aodServletPath, "POST",
        "ValidateModuleAdminObj", AODModuleScopedJndiName);

    TestUtil.logMsg("ValidateModuleAdminObj : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("ValidateModuleAdminObj : FAILED");
    }

    TestUtil.logMsg("ValidateModuleAdminObj : PASSED");
  }

  /**
   * @testName: ValidateCompAdminObj
   *
   * @assertion_ids: Connector:SPEC:321; Connector:SPEC:326; Connector:SPEC:327;
   *
   * @test_Strategy: - this calls a servlet and passes info about what to test
   *                 as request params. - this is going to test that a Servlet
   *                 can create a ConnectorAdminObj in the component scope (e.g.
   *                 java:/comp/env/..) - this verifies that creation worked by
   *                 doing a lookup of the ConnectorAdminObj's jndiName.
   */
  public void ValidateCompAdminObj() throws Fault {
    String strMsg1 = "ValidateCompAdminObj() passed.";

    String str = invokeServletAndGetResponse(aodServletPath, "POST",
        "ValidateCompAdminObj", AODCompScopedJndiName);

    TestUtil.logMsg("ValidateCompAdminObj : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("ValidateCompAdminObj : FAILED");
    }

    TestUtil.logMsg("ValidateCompAdminObj : PASSED");
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
    return invokeServletAndGetResponse(sContext, requestMethod, null, null);
  }

  private String invokeServletAndGetResponse(String sContext,
      String requestMethod, String testMethod, String rarJndiScope) {

    TSURL ctsurl = new TSURL();
    if (!sContext.startsWith("/")) {
      sContext = "/" + sContext;
    }

    if (rarJndiScope == null) {
      // add placeholder val to be sure nothing is missing!
      rarJndiScope = "NO_VAL_SET";
      TestUtil.logMsg("rarJndiScope ws null so setting to " + rarJndiScope);
    }

    // add some servlet params onto our context
    if (testMethod != null) {
      sContext = sContext + "?" + "method.under.test=" + testMethod + "&"
          + "rar.jndi.scope=" + rarJndiScope;
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
      retVal = conn.getResponseMessage();
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

  private void printVerticalIndent() {
    TestUtil
        .logMsg("**********************************************************");
    TestUtil.logMsg("\n");

  }

}
