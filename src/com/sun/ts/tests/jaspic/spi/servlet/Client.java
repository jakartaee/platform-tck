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

package com.sun.ts.tests.jaspic.spi.servlet;

import java.util.Properties;
import java.io.InputStream;
import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.BASE64Encoder;
import com.sun.ts.lib.porting.TSURL;
import com.sun.ts.tests.jaspic.util.LogFileProcessor;
import com.sun.ts.tests.jaspic.tssv.util.IdUtil;

import javax.security.auth.message.config.AuthConfigFactory;
import javax.security.auth.message.config.AuthConfigProvider;

import com.sun.ts.tests.jaspic.tssv.util.JASPICData;
import com.sun.ts.tests.jaspic.tssv.util.ProviderConfigurationXMLFileProcessor;
import com.sun.ts.tests.jaspic.tssv.util.ProviderConfigurationEntry;
import com.sun.ts.tests.jaspic.tssv.config.TSAuthConfigFactoryForStandalone;

/**
 * This class will be used to perform simple servlet invocations. The servlet
 * invocations should be used to test some different security constraints so we
 * can validate that proper AuthModule invocations are occurring. (The servlet
 * security constraints we set will affect which AuthModules will be tested.)
 *
 * This Client class should NOT be testing any of the JSR-196 API's within here.
 * Instead, the actual beef of our testing should be done within the actual
 * factory or provider code which will be called/used from within the MPR.
 * Calling things like the AuthConfigFactory.getDefault() from within this
 * client code is not a valid thing to do. We want a MPR to make this
 * invocations. So for out testing, we will have the bulk of our tests live in
 * the ACF's or ACP's. When those tests are autorun (maybe by having the
 * respective constructors automatically kick them off), then they will perform
 * any server side logging.
 *
 * We will check for success or failure from within this file. So the actual
 * testcases in this class will simply consist of checking the server side log
 * file for key strings which will indicate success or failure.
 *
 * Important: Exercise caution when modifying the search strings that are passed
 * to the logProcessor.verifyLogContains() method calls. These strings should be
 * written to the log file by some of the code in the server side
 * tssv/config/*.java files.
 *
 */
public class Client extends EETest {
  private Properties props = null;

  private String hostname = null;

  private int portnum = 0;

  // this must be the decoded context path corresponding to the web module
  private String contextPath = "/" + JASPICData.SCP_CONTEXT_PATH;

  private String acfServletPath = contextPath + "/ACFTestServlet";

  private String wrapperServletPath = contextPath + "/WrapperServlet";

  private String servletPath = contextPath + "/ModTestServlet";

  private String openToAllServletPath = "/OpenToAllServlet";

  private String allAccessServletPath = contextPath + openToAllServletPath;

  private String staticPagePath = contextPath + "/client.html";

  private String noConstraintPath = contextPath + "/OptionalAuthen";

  private String username = "";

  private String password = "";

  private String appContextHostname;

  // appContext must be in the form of "hostname context-path"
  private String appContext;

  private LogFileProcessor logProcessor = null;

  private boolean initialized = false;

  private boolean bIs115Compatible = false;

  private String logFileLocation;

  private String vendorACF;

  private String providerConfigFileLoc;

  // some private static strings we need to verify ahead of time
  private String ACF_MSG_1 = null;

  private ProviderConfigurationXMLFileProcessor configFileProcessor = null;

  private Collection<ProviderConfigurationEntry> providerConfigurationEntriesCollection = null;

  private String servletAppContext = null;

  private String providerConfigurationFileLocation;

  private String vendorAuthConfigFactoryClass;

  public static void main(String args[]) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /**
   * @class.setup_props: log.file.location; provider.configuration.file;
   *                     vendor.authconfig.factory; logical.hostname.servlet;
   *                     webServerHost; webServerPort; authuser; authpassword;
   *                     user; password; securedWebServicePort;
   *                     servlet.is.jsr115.compatible;
   *
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;

    try {
      hostname = p.getProperty("webServerHost");
      portnum = Integer.parseInt(p.getProperty("webServerPort"));
      username = p.getProperty("user");
      password = p.getProperty("password");

      logFileLocation = p.getProperty("log.file.location");
      vendorACF = p.getProperty("vendor.authconfig.factory");
      providerConfigFileLoc = p.getProperty("provider.configuration.file");
      appContextHostname = p.getProperty("logical.hostname.servlet");
      servletAppContext = IdUtil.getAppContextId(JASPICData.LAYER_SERVLET);

      String str115Compat = p.getProperty("servlet.is.jsr115.compatible");
      if (str115Compat != null) {
        bIs115Compatible = Boolean.valueOf(str115Compat).booleanValue();
      } else {
        bIs115Compatible = false;
      }

      appContext = servletAppContext;
      ACF_MSG_1 = "TSAuthConfigFactory.getConfigProvider ";
      ACF_MSG_1 += "returned non-null provider for Layer : HttpServlet";
      ACF_MSG_1 += " and AppContext :" + appContext;

      TestUtil.logMsg("setup(): logFileLocation = " + logFileLocation);
      TestUtil
          .logMsg("setup(): providerConfigFileLoc = " + providerConfigFileLoc);
      TestUtil.logMsg("setup(): appContextHostname = " + appContextHostname);
      TestUtil.logMsg("setup(): contextPath = " + contextPath);
      TestUtil.logMsg("setup(): appContext = " + appContext);
      TestUtil.logMsg("setup(): servletAppContext = " + servletAppContext);

      // we know what the logFileLocation is, but we can't always be sure it
      // as initialized as a system property so explicitly set it here.
      System.setProperty("log.file.location", logFileLocation);
      System.setProperty("provider.configuration.file", providerConfigFileLoc);
      System.setProperty("logical.hostname.servlet", appContextHostname);

      providerConfigurationFileLocation = props
          .getProperty("provider.configuration.file");
      TestUtil.logMsg("TestSuite Provider ConfigFile = "
          + providerConfigurationFileLocation);

      vendorAuthConfigFactoryClass = props
          .getProperty("vendor.authconfig.factory");
      TestUtil.logMsg(
          "Vendor AuthConfigFactory class = " + vendorAuthConfigFactoryClass);

      if (!initialized) {
        // create LogFileProcessor
        logProcessor = new LogFileProcessor(props);

        // if no TSSVLog.txt file exists, then we want to do something that
        // cause one to be generated.
        File logfile = new File(logFileLocation);
        if (!logfile.exists()) {
          TestUtil.logMsg(
              "setup(): no TSSVLog.txt so access url to indirectly create one");
          String theContext = contextPath + "/"
              + JASPICData.AUTHSTAT_MAND_SUCCESS;
          int statusCode = invokeServlet(theContext, "POST");
          TestUtil.logMsg(
              "setup(): invokeServlet() returned statusCode =" + statusCode);
        }

        // retrieve logs based on application Name
        logProcessor.fetchLogs("pullAllLogRecords|fullLog");

        initialized = true;
      }

    } catch (Exception e) {
      logErr("Error: got exception: ", e);
    }
  }

  public void cleanup() throws Fault {
  }

  /**
   *
   * @keywords: jaspic_servlet
   *
   * @testName: CheckSecureRespForOptionalAuth
   *
   * @assertion_ids: JASPIC:SPEC:59; JASPIC:SPEC:304; JASPIC:JAVADOC:27;
   *                 JASPIC:JAVADOC:10; JASPIC:JAVADOC:11; JASPIC:JAVADOC:28;
   *                 JASPIC:JAVADOC:13;
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify secureResponse was called.
   *
   *                 Description This method will make a URL post that has
   *                 optional security Authen which means that we should NOT
   *                 need to set any principals in the validateRequest methods
   *                 nor the callbacks. The Runtime should allow this to proceed
   *                 to call the secureResponse even though there is no
   *                 principal values explicitly being set. Note: we need to
   *                 specifically hardwire the particular context used in this
   *                 test to NOT have any principals set on the server side.
   * 
   */
  public void CheckSecureRespForOptionalAuth() throws Fault {
    String strHDR = "HttpServlet profile with servletName=/";
    String strMsg1 = strHDR
        + "OptionalAuthen returning  AuthStatus=AuthStatus.SUCCESS";
    String strMsg2 = "secureResponse called for layer=HttpServlet for requestURI="
        + contextPath + "/OptionalAuthen";
    String tempArg1[] = { strMsg1 };
    String tempArg2[] = { strMsg2 };

    boolean verified = false;
    String ctxt1 = contextPath + "/OptionalAuthen";

    // access a servlet which has no security constraint set (which means
    // authentication is optional)
    checkAuthStatus(ctxt1, "SUCCESS", "POST", false);

    // verify we accessed and returned from validateReq call okay
    verified = logProcessor.verifyLogContains(tempArg1);
    if (!verified) {
      throw new Fault("CheckSecureRespForOptionalAuth : FAILED");
    }

    // verify the call to secureResponse occurred
    verified = logProcessor.verifyLogContains(tempArg2);
    if (!verified) {
      throw new Fault("CheckSecureRespForOptionalAuth : FAILED");
    }

    TestUtil.logMsg("CheckSecureRespForOptionalAuth : PASSED");
  }

  /**
   *
   * @keywords: jaspic_servlet
   *
   * @testName: CheckSecureRespForMandatoryAuth
   *
   * @assertion_ids: JASPIC:SPEC:98; JASPIC:SPEC:304; JASPIC:JAVADOC:26;
   *                 JASPIC:JAVADOC:31; JASPIC:JAVADOC:10; JASPIC:JAVADOC:9;
   *                 JASPIC:SPEC:103;
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify secureResponse and CallerPrincipalCallback are
   *                 called.
   *
   *                 Description This method will make a URL post that has
   *                 Mandatory security Authen which means that we need to set a
   *                 principal objects in the validateRequest method
   *                 clientSubject object (or in a callback). Either way, once
   *                 we set a principal object in the clientSubject - The
   *                 Runtime should proceed to call the secureResponse and
   *                 should invoke the CallbackHandler passed to it by the
   *                 runtime to handle a CallerPrincipalCallback using the
   *                 clientSubject as argument to it. For this case, we want to
   *                 ensure the following occur: - AuthStatus returns SUCCESS
   *                 (we know this is occuriing otherwise we would not make it
   *                 into secureResponse) - isMandatory() == true -
   *                 secureResponse is called - CallerPrincipalCallback handler
   *                 gets invoked.
   *
   *                 NOTE: We need our server side to check and ensure there are
   *                 principal values being set for this particular context
   *                 posting.
   *
   */
  public void CheckSecureRespForMandatoryAuth() throws Fault {
    boolean verified = false;
    String strHDR = "HttpServlet profile with servletName=/";
    String theContext = contextPath + "/" + JASPICData.AUTHSTAT_MAND_SUCCESS;
    String strMsg1 = strHDR + JASPICData.AUTHSTAT_MAND_SUCCESS
        + " returning  AuthStatus=AuthStatus.SUCCESS";
    String strMsg2 = "secureResponse called for layer=HttpServlet for requestURI="
        + theContext;
    String strMsg3 = "In HttpServlet : ServerRuntime CallerPrincipalCallback";
    strMsg3 += " called for profile=HttpServlet for servletPath=" + theContext;
    String tempArg1[] = { strMsg1, strMsg2, strMsg3 };

    checkAuthStatus(theContext, "SUCCESS", "POST", false);

    // verify whether the log contains required messages.
    verified = logProcessor.verifyLogContains(tempArg1);
    if (!verified) {
      throw new Fault("CheckSecureRespForMandatoryAuth : FAILED");
    }

    TestUtil.logMsg("CheckSecureRespForMandatoryAuth : PASSED");
  }

  /**
   *
   * @keywords: jaspic_servlet
   *
   * @testName: testSecRespCalledAfterSvcInvoc
   *
   * @assertion_ids: JASPIC:SPEC:108;
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify secureResponse and CallerPrincipalCallback are
   *                 called.
   *
   *                 Description This method will make a URL post that has
   *                 Mandatory security Authen and it will verify that the
   *                 secureResponse() method has NOT been called before the
   *                 servlet/service invocation by checking for the existance of
   *                 a cts proprietary request property that would only get set
   *                 from in secureResponse. So if the property is found, we
   *                 know a call to secureResponse was incorrectly made BEFORE
   *                 the servlet/service invocation but if the request property
   *                 is NOT found, then we can assume that secureResponse was
   *                 NOT called before the servlet/service invocation.
   *
   */
  public void testSecRespCalledAfterSvcInvoc() throws Fault {
    String strMsg1 = "testSecRespCalledAfterSvcInvoc() passed";
    String theContext = contextPath + "/" + JASPICData.AUTHSTAT_MAND_SUCCESS;

    String str = invokeServletAndGetResponse(theContext, "POST",
        "testSecRespCalledAfterSvcInvoc");

    TestUtil.logMsg("testSecRespCalledAfterSvcInvoc : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("testSecRespCalledAfterSvcInvoc : FAILED");
    }

    TestUtil.logMsg("testSecRespCalledAfterSvcInvoc : PASSED");
  }

  /**
   *
   * @keywords: jaspic_servlet
   *
   * @testName: CheckNoAuthReturnsValidStatusCode
   *
   * @assertion_ids: JASPIC:SPEC:93
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Make servlet invocations and check the return status to
   *                 verify proper server side handling.
   *
   *                 Description This method will make a request that is not
   *                 authorized and then verify that the status code returned is
   *                 not indicating the request was OK==200. If we get back a
   *                 statuscode = 200 then there was a problem since we were
   *                 trying to access a page that had mandatory authentication
   *                 set AND we were trying to use an invalid user/pwd to
   *                 perform that access - thus we should NOT get an okay status
   *                 code returned since our bad username/pwd will not have
   *                 perms.
   *
   */
  public void CheckNoAuthReturnsValidStatusCode() throws Fault {
    String noAuthContext = contextPath + "/AnotherMandatoryAuthen";

    // invokeServlet will attempt to access the resource that does
    // not have the proper role creds assigned to it
    // thus we expect a return status code != 200
    int statusCode = invokeServlet(noAuthContext, "POST");

    // if status of 200 was returned then there is a problem since
    // we should have been forbidden from completing our request
    // as our username/pwd change means we should not be authenticated
    if (statusCode == 200) {
      throw new Fault("CheckNoAuthReturnsValidStatusCode : FAILED");
    }

    TestUtil.logMsg("CheckNoAuthReturnsValidStatusCode : PASSED");
  }

  /**
   *
   * @keywords: jaspic_servlet
   *
   * @testName: VerifyRequestDispatchedProperly
   *
   * @assertion_ids: JASPIC:SPEC:317; JASPIC:JAVADOC:27; JASPIC:JAVADOC:28;
   * 
   *
   * @test_Strategy: 1. issue request that has mandatory auth and verify it gets
   *                 authorized. 2. issue a request that is mandatory but NOT
   *                 authorized (eg submit request with invalid user/pwd) and
   *                 verify that secureResponse is still called by MPR.
   *
   *                 Description The request must be dispatched to the resource
   *                 if the request was determined to be authorized; otherwise
   *                 it must NOT be dispatched and the runtime must proceed to
   *                 point (3) in the message processing model.
   *
   */
  public void VerifyRequestDispatchedProperly() throws Fault {
    boolean verified = false;
    String theContext = contextPath + "/" + JASPICData.AUTHSTAT_MAND_SUCCESS;
    String noAuthContext = contextPath + "/AnotherMandatoryAuthen";

    // we should see that our request is properly dispatched
    // we will assume that a proper response code indicates success
    // as we are using correct creds
    int statusCode = invokeServlet(theContext, "POST",
        "VerifyRequestDispatchedProperly");
    if (statusCode != 200) {
      throw new Fault("VerifyRequestDispatchedProperly : FAILED");
    }

    // invokeServlet will attempt to access the resource that
    // does not have proper role creds assigned
    // thus we expect a return status code != 200
    statusCode = invokeServlet(noAuthContext, "POST");

    if (statusCode == 200) {
      // failure since 200 means we were able to properly dispatch the request
      throw new Fault("VerifyRequestDispatchedProperly : FAILED");
    }

    // now verify that the MPR went into the secureResponse
    String strMsg1 = "secureResponse called for layer=HttpServlet for requestURI="
        + theContext;
    String tempArg1[] = { strMsg1 };
    verified = logProcessor.verifyLogContains(tempArg1);
    if (!verified) {
      throw new Fault("VerifyRequestDispatchedProperly : FAILED");
    }

    TestUtil.logMsg("VerifyRequestDispatchedProperly : PASSED");
  }

  /**
   *
   * @keywords: jaspic_servlet
   *
   * @testName: CheckValidateReqAuthException
   *
   * @assertion_ids: JASPIC:SPEC:58; JASPIC:SPEC:320; JASPIC:SPEC:94;
   *                 JASPIC:JAVADOC:1; JASPIC:JAVADOC:28;
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify secureResponse was not called for the given context
   *                 and that an AuthException was called.
   *
   *                 Description This method will make a URL post using a
   *                 context that should cause an AuthException to get thrown
   *                 from the validateRequest. This should cause the
   *                 secureResponse to NOT get called.
   *
   */
  public void CheckValidateReqAuthException() throws Fault {
    boolean verified = false;
    String strHDR = "HttpServlet profile with servletName=/";
    String theContext = contextPath + "/" + JASPICData.AUTHSTAT_THROW_EX_ND;
    String strMsg1 = strHDR + JASPICData.AUTHSTAT_THROW_EX_ND
        + " returning  AuthStatus=AuthException";
    String tempArg1[] = { strMsg1 };

    String strMsg2 = "secureResponse called for layer=HttpServlet for requestURI="
        + contextPath + "/" + JASPICData.AUTHSTAT_THROW_EX_ND;
    String tempArg2[] = { strMsg2 };

    checkAuthStatus(theContext, "AuthException", "POST", false);

    // verify whether the log contains required messages.
    verified = logProcessor.verifyLogContains(tempArg1);
    if (!verified) {
      throw new Fault("CheckValidateReqAuthException : FAILED");
    }

    // also verify that no call to secureResponse was made
    verified = logProcessor.verifyLogContains(tempArg2);
    if (verified) {
      throw new Fault("CheckValidateReqAuthException : FAILED");
    }

    TestUtil.logMsg("CheckValidateReqAuthException : PASSED");
  }

  /**
   *
   * @keywords: jaspic_servlet
   *
   * @testName: CheckValidateReqAlwaysCalled
   *
   * @assertion_ids: JASPIC:SPEC:58; JASPIC:SPEC:320; JASPIC:SPEC:89;
   *                 JASPIC:SPEC:94; JASPIC:JAVADOC:1; JASPIC:JAVADOC:28;
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify secureResponse was not called for the given context
   *                 and that an AuthException was called.
   *
   *                 Description This method will make mulitple posts using
   *                 contexts w/ authN that is both required and non-required.
   *                 We want to confirm that validateRequest is called
   *                 independent of whether authN is required (i.e. including
   *                 when isMandatory() would be false). Among other things,
   *                 This is validating JASPIC spec section 3.8 which states
   *                 "validateRequest must be called for all requests".
   *
   */
  public void CheckValidateReqAlwaysCalled() throws Fault {
    boolean verified = false;
    String strHDR = "validateRequest() called for ";
    String context1 = servletPath; // authN is required
    String context2 = noConstraintPath; // no authN required
    String strMsg1 = strHDR + context1 + ", isMandatory() = true";
    String strMsg2 = strHDR + context2 + ", isMandatory() = false";
    String tempArg1[] = { strMsg1 };
    String tempArg2[] = { strMsg2 };

    invokeServlet(context1, "POST", "CheckValidateReqAlwaysCalled");
    invokeServlet(context2, "POST", "CheckValidateReqAlwaysCalled");

    TestUtil.logMsg("searaching for string1: " + strMsg1);
    TestUtil.logMsg("searaching for string2: " + strMsg2);

    // verify whether the log contains 1st required messages.
    verified = logProcessor.verifyLogContains(tempArg1);
    if (!verified) {
      throw new Fault("CheckValidateReqAlwaysCalled : FAILED");
    }

    // also verify the log contains 2nd required messages.
    verified = logProcessor.verifyLogContains(tempArg2);
    if (!verified) {
      throw new Fault("CheckValidateReqAlwaysCalled : FAILED");
    }

    TestUtil.logMsg("CheckValidateReqAlwaysCalled : PASSED");
  }

  /**
   *
   * @keywords: jaspic_servlet
   *
   * @testName: AuthConfigFactoryGetFactory
   *
   * @assertion_ids: JASPIC:SPEC:7; JASPIC:SPEC:10; JASPIC:JAVADOC:84;
   *                 JASPIC:JAVADOC:94; JASPIC:JAVADOC:79; JASPIC:JAVADOC:97;
   *                 JASPIC:JAVADOC:77; JASPIC:JAVADOC:80; JASPIC:JAVADOC:84;
   *                 JASPIC:JAVADOC:85;
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify Whether AuthConfigFactory.getFactory is called and
   *                 instantiated in the server.
   *
   *                 Description The AuthConfigFactory.getFactory method must be
   *                 used during the container bootstrap processs.
   *
   */
  public void AuthConfigFactoryGetFactory() throws Fault {
    boolean verified = false;
    String args[] = { "TSAuthConfigFactory.getFactory called Indirectly" };

    invokeServlet(servletPath, "POST", "AuthConfigFactoryGetFactory");

    // verify whether the log contains required messages.
    verified = logProcessor.verifyLogContains(args);

    if (!verified) {
      throw new Fault("AuthConfigFactoryGetFactory : FAILED");
    }

    TestUtil.logMsg("AuthConfigFactoryGetFactory : PASSED");
  }

  /**
   *
   * @keywords: jaspic_servlet
   *
   * @testName: GetConfigProvider
   *
   * @assertion_ids: JASPIC:SPEC:8; JASPIC:SPEC:14; JASPIC:JAVADOC:79;
   *                 JASPIC:JAVADOC:84; JASPIC:JAVADOC:85; JASPIC:JAVADOC:91
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify Whether AuthConfigFactory.getConfigProvider is
   *                 called in the server.
   *
   *                 Description The runtime must invoke
   *                 AuthConfigFactory.getConfigProvider to obtain the
   *                 AuthConfigProvider.
   *
   */
  public void GetConfigProvider() throws Fault {
    boolean verified = false;
    String args[] = { "TSAuthConfigFactory.getConfigProvider called" };

    invokeServlet(servletPath, "POST", "GetConfigProvider");

    // verify whether the log contains required messages.
    verified = logProcessor.verifyLogContains(args);

    if (!verified) {
      throw new Fault("GetConfigProvider : FAILED");
    }

    TestUtil.logMsg("GetConfigProvider : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: CheckAuthContextId
   *
   * @assertion_ids: JASPIC:SPEC:80
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify that the authentication context identifier used in
   *                 the call to getAuthContext is equivalent to the value that
   *                 is acquired in the call to getAuthContextID with the
   *                 MessageInfo that is be used in the call to validateRequest.
   *
   *                 Description The authentication context identifier used in
   *                 the call to getAuthContext must be equivalent to the value
   *                 that would be acquired by calling getAuthContextID with the
   *                 MessageInfo that will be used in the call to
   *                 validateRequest.
   *
   */
  public void CheckAuthContextId() throws Fault {
    String strMsg1 = "getAuthContextID() called for layer=HttpServlet shows";
    strMsg1 = strMsg1 + " AuthContextId=/ModTestServlet POST";
    String strMsg2 = "TSServerAuthConfig.getAuthContext:  layer=HttpServlet";
    strMsg2 = strMsg2 + " : appContext=" + appContext
        + " operationId=/ModTestServlet POST";
    String tempArgs[] = { strMsg1, strMsg2 };

    invokeServlet(servletPath, "POST", "CheckAuthContextId");

    boolean verified = logProcessor.verifyLogContains(tempArgs);
    if (!verified) {
      throw new Fault("CheckAuthContextId : FAILED");
    }

    TestUtil.logMsg("CheckAuthContextId : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: CheckAuthContextIdUsingGet
   *
   * @assertion_ids: JASPIC:SPEC:80
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify that the authentication context identifier used in
   *                 the call to getAuthContext is equivalent to the value that
   *                 is acquired in the call to getAuthContextID with the
   *                 MessageInfo that is be used in the call to validateRequest.
   *
   *                 Description The authentication context identifier used in
   *                 the call to getAuthContext must be equivalent to the value
   *                 that would be acquired by calling getAuthContextID with the
   *                 MessageInfo that will be used in the call to
   *                 validateRequest.
   *
   *                 NOTE: this is basically the same as CheckAuthContextId
   *                 except that this is using "GET" instead of "POST". While
   *                 seemingly trivial, it has been suggested that GET and POST
   *                 can often take different branches in impl code so it's
   *                 worth checking both.
   *
   */
  public void CheckAuthContextIdUsingGet() throws Fault {
    String strMsg1 = "getAuthContextID() called for layer=HttpServlet shows";
    strMsg1 = strMsg1 + " AuthContextId=/ModTestServlet GET";
    String strMsg2 = "TSServerAuthConfig.getAuthContext:  layer=HttpServlet";
    strMsg2 = strMsg2 + " : appContext=" + appContext
        + " operationId=/ModTestServlet GET";
    String tempArgs[] = { strMsg1, strMsg2 };

    invokeServlet(servletPath, "GET", "CheckAuthContextIdUsingGet");

    boolean verified = logProcessor.verifyLogContains(tempArgs);
    if (!verified) {
      throw new Fault("CheckAuthContextIdUsingGet : FAILED");
    }

    TestUtil.logMsg("CheckAuthContextIdUsingGet : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: AuthConfigFactoryRegistration
   *
   * @assertion_ids: JASPIC:SPEC:80;
   *
   * @test_Strategy: this was originally in register directory but was moved
   *                 here and the code pulled out and centralized into
   *                 ACFTestServlet.
   *
   */
  public void AuthConfigFactoryRegistration() throws Fault {
    String strMsg1 = "ACFTestServlet->AuthConfigFactoryRegistration() passed";

    String str = invokeServletAndGetResponse(acfServletPath, "POST",
        "AuthConfigFactoryRegistration");
    TestUtil.logMsg("AuthConfigFactoryRegistration : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("AuthConfigFactoryRegistration : FAILED");
    }

    TestUtil.logMsg("AuthConfigFactoryRegistration : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: ACFInMemoryNotifyOnUnReg
   *
   * @assertion_ids: JASPIC:SPEC:336; JASPIC:SPEC:337; JASPIC:SPEC:338;
   *                 JASPIC:SPEC:339; JASPIC:SPEC:342; JASPIC:SPEC:344;
   *
   * @test_Strategy: this was originally in register directory but was moved
   *                 here and the code pulled out and centralized into
   *                 ACFTestServlet.
   *
   */
  public void ACFInMemoryNotifyOnUnReg() throws Fault {
    String strMsg1 = "ACFTestServlet->ACFInMemoryNotifyOnUnReg() passed";

    String str = invokeServletAndGetResponse(acfServletPath, "POST",
        "ACFInMemoryNotifyOnUnReg");
    TestUtil.logMsg("ACFInMemoryNotifyOnUnReg : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("ACFInMemoryNotifyOnUnReg : FAILED");
    }

    TestUtil.logMsg("ACFInMemoryNotifyOnUnReg : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: ACFPersistentNotifyOnUnReg
   *
   * @assertion_ids: JASPIC:SPEC:336; JASPIC:SPEC:337; JASPIC:SPEC:338;
   *                 JASPIC:SPEC:339; JASPIC:SPEC:341; JASPIC:SPEC:344;
   *
   * @test_Strategy: this was originally in register directory but was moved
   *                 here and the code pulled out and centralized into
   *                 ACFTestServlet.
   *
   */
  public void ACFPersistentNotifyOnUnReg() throws Fault {
    String strMsg1 = "ACFTestServlet->ACFPersistentNotifyOnUnReg() passed";

    String str = invokeServletAndGetResponse(acfServletPath, "POST",
        "ACFPersistentNotifyOnUnReg");
    TestUtil.logMsg("ACFPersistentNotifyOnUnReg : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("ACFPersistentNotifyOnUnReg : FAILED");
    }

    TestUtil.logMsg("ACFPersistentNotifyOnUnReg : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: ACFInMemoryPrecedenceRules
   *
   * @assertion_ids: JASPIC:SPEC:328; JASPIC:SPEC:337; JASPIC:SPEC:338;
   *                 JASPIC:SPEC:338; JASPIC:SPEC:339; JASPIC:SPEC:344;
   *
   * @test_Strategy: this was originally in register directory but was moved
   *                 here and the code pulled out and centralized into
   *                 ACFTestServlet.
   *
   */
  public void ACFInMemoryPrecedenceRules() throws Fault {
    String strMsg1 = "ACFTestServlet->ACFInMemoryPrecedenceRules() passed";

    String str = invokeServletAndGetResponse(acfServletPath, "POST",
        "ACFInMemoryPrecedenceRules");
    TestUtil.logMsg("ACFInMemoryPrecedenceRules : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("ACFInMemoryPrecedenceRules : FAILED");
    }

    TestUtil.logMsg("ACFInMemoryPrecedenceRules : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: ACFPersistentPrecedenceRules
   *
   * @assertion_ids: JASPIC:SPEC:328; JASPIC:SPEC:337; JASPIC:SPEC:338;
   *                 JASPIC:SPEC:338; JASPIC:SPEC:339; JASPIC:SPEC:344;
   *
   * @test_Strategy: this was originally in register directory but was moved
   *                 here and the code pulled out and centralized into
   *                 ACFTestServlet.
   *
   */
  public void ACFPersistentPrecedenceRules() throws Fault {
    String strMsg1 = "ACFTestServlet->ACFPersistentPrecedenceRules() passed";

    String str = invokeServletAndGetResponse(acfServletPath, "POST",
        "ACFPersistentPrecedenceRules");
    TestUtil.logMsg("ACFPersistentPrecedenceRules : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("ACFPersistentPrecedenceRules : FAILED");
    }

    TestUtil.logMsg("ACFPersistentPrecedenceRules : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: ACFGetRegistrationContext
   *
   * @assertion_ids: JASPIC:JAVADOC:81;
   *
   * @test_Strategy: This is testing that acf.getRegistrationContext(string)
   *                 returns NULL for an unrecognized string. (this requirement
   *                 described in javadoc for api)
   */
  public void ACFGetRegistrationContext() throws Fault {
    String strMsg1 = "ACFTestServlet->ACFGetRegistrationContext() passed";

    String str = invokeServletAndGetResponse(acfServletPath, "POST",
        "ACFGetRegistrationContext");
    TestUtil.logMsg("ACFGetRegistrationContext : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("ACFGetRegistrationContext : FAILED");
    }

    TestUtil.logMsg("ACFGetRegistrationContext : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: ACFGetRegistrationIDs
   *
   * @assertion_ids: JASPIC:JAVADOC:82;
   *
   * @test_Strategy: This is testing that acf.getRegistrationIDs(acp) NEVER
   *                 returns null hint: this must return empty array even if
   *                 unrecognized acp. (this requirement described in javadoc
   *                 for api)
   * 
   */
  public void ACFGetRegistrationIDs() throws Fault {
    String strMsg1 = "ACFTestServlet->ACFGetRegistrationIDs() passed";

    String str = invokeServletAndGetResponse(acfServletPath, "POST",
        "ACFGetRegistrationIDs");
    TestUtil.logMsg("ACFGetRegistrationIDs : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("ACFGetRegistrationIDs : FAILED");
    }

    TestUtil.logMsg("ACFGetRegistrationIDs : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: ACFRemoveRegistration
   *
   * @assertion_ids: JASPIC:JAVADOC:86;
   *
   * @test_Strategy: This is testing that acf.removeRegistration(arg) will
   *                 return FALSE when invalid arg supplied. (this requirement
   *                 described in javadoc for api)
   *
   */
  public void ACFRemoveRegistration() throws Fault {
    String strMsg1 = "ACFTestServlet->ACFRemoveRegistration() passed";

    String str = invokeServletAndGetResponse(acfServletPath, "POST",
        "ACFRemoveRegistration");
    TestUtil.logMsg("ACFRemoveRegistration : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("ACFRemoveRegistration : FAILED");
    }

    TestUtil.logMsg("ACFRemoveRegistration : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: ACFDetachListener
   *
   * @assertion_ids: JASPIC:JAVADOC:78;
   *
   * @test_Strategy: This is testing that acf.detachListener(...) will return
   *                 non-null when unregistered listener supplied. (this
   *                 requirement described in javadoc for api)
   *
   */
  public void ACFDetachListener() throws Fault {
    String strMsg1 = "ACFTestServlet->ACFDetachListener() passed";

    String str = invokeServletAndGetResponse(acfServletPath, "POST",
        "ACFDetachListener");
    TestUtil.logMsg("ACFDetachListener : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("ACFDetachListener : FAILED");
    }

    TestUtil.logMsg("ACFDetachListener : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: ACFGetFactory
   *
   * @assertion_ids: JASPIC:SPEC:329; JASPIC:SPEC:335; JASPIC:SPEC:7;
   *
   * @test_Strategy: This s mainly concerned with testing the runtimes handling
   *                 of ACF as follows: - get current (CTS) ACF - switch to use
   *                 different (CTS) ACF - verify calls to ACF use the
   *                 newer/expected ACF - restore original ACF
   */
  public void ACFGetFactory() throws Fault {
    String strMsg1 = "ACFTestServlet->ACFGetFactory() passed";

    String str = invokeServletAndGetResponse(acfServletPath, "POST",
        "ACFGetFactory");
    TestUtil.logMsg("ACFGetFactory : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("ACFGetFactory : FAILED");
    }

    TestUtil.logMsg("ACFGetFactory : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: testACFComesFromSecFile
   *
   * @assertion_ids: JASPIC:SPEC:329; JASPIC:SPEC:330;
   *
   * @test_Strategy: This is calling a method on the server(actually servlet)
   *                 side that will invoke getFactory() to verify a non-null
   *                 facotry instance is returned. It will also verify that the
   *                 authconfigprovider.factory security property is properly
   *                 set/used.
   *
   */
  public void testACFComesFromSecFile() throws Fault {
    String strMsg1 = "ACFTestServlet->testACFComesFromSecFile() passed";

    String str = invokeServletAndGetResponse(acfServletPath, "POST",
        "testACFComesFromSecFile");
    TestUtil.logMsg("testACFComesFromSecFile : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("testACFComesFromSecFile : FAILED");
    }

    TestUtil.logMsg("testACFComesFromSecFile : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: ACFPersistentRegisterOnlyOneACP
   *
   * @assertion_ids: JASPIC:SPEC:329; JASPIC:SPEC:331; JASPIC:SPEC:332;
   *                 JASPIC:SPEC:340; JASPIC:SPEC:341;
   *
   * @test_Strategy: This will make a server (actually servlet) side method call
   *                 that will do the following: - load vendors ACF -
   *                 (persistent) register of CTS ACP's in the vendors ACF - get
   *                 list of vendors registered provider ID's - register another
   *                 persistent ACP (this is standalone ACP profile) - verify
   *                 another regId was added for standalone ACP - try to
   *                 re-register (persistently) standalone provider - verify 2nd
   *                 attempt at added standalone provider REPLACED the first but
   *                 it should NOT have added another nor failed. - also confirm
   *                 that regID for standalone ACP stayed the same after 1st and
   *                 2nd attempt to register standalone ACP - verify that the
   *                 2nd re-registering of ACP replaced the ACP AND the
   *                 description. - unregister standalone ACP and setFactory
   *                 back to CTS default
   *
   */
  public void ACFPersistentRegisterOnlyOneACP() throws Fault {
    String strMsg1 = "ACFTestServlet->ACFPersistentRegisterOnlyOneACP() passed";

    String str = invokeServletAndGetResponse(acfServletPath, "POST",
        "ACFPersistentRegisterOnlyOneACP");
    TestUtil.logMsg("ACFPersistentRegisterOnlyOneACP : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("ACFPersistentRegisterOnlyOneACP : FAILED");
    }

    TestUtil.logMsg("ACFPersistentRegisterOnlyOneACP : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: ACFInMemoryRegisterOnlyOneACP
   *
   * @assertion_ids: JASPIC:SPEC:334; JASPIC:SPEC:342; JASPIC:SPEC:343;
   *
   * @test_Strategy: This will make a server (actually servlet) side method call
   *                 that will do the following: - load vendors ACF -
   *                 (persistent) register of CTS ACP's in the vendors ACF - get
   *                 list of vendors registered provider ID's - register
   *                 (in-memory) ACP (this is standalone ACP profile) - verify
   *                 another regId was added for standalone ACP - try to
   *                 re-register (in-memory) standalone provider - verify 2nd
   *                 attempt at added standalone provider REPLACED the first but
   *                 it should NOT have added another nor failed. - also confirm
   *                 that regID for standalone ACP stayed the same after 1st and
   *                 2nd attempt to register standalone ACP - verify that the
   *                 2nd re-registering of ACP replaced the ACP AND the
   *                 description. - unregister standalone ACP and setFactory
   *                 back to CTS default
   *
   */
  public void ACFInMemoryRegisterOnlyOneACP() throws Fault {
    String strMsg1 = "ACFTestServlet->ACFInMemoryRegisterOnlyOneACP() passed";

    String str = invokeServletAndGetResponse(acfServletPath, "POST",
        "ACFInMemoryRegisterOnlyOneACP");
    TestUtil.logMsg("ACFInMemoryRegisterOnlyOneACP : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("ACFInMemoryRegisterOnlyOneACP : FAILED");
    }

    TestUtil.logMsg("ACFInMemoryRegisterOnlyOneACP : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: testRequestWrapper
   *
   * @assertion_ids: JASPIC:SPEC:108; JASPIC:SPEC:311;
   *
   * @test_Strategy:
   * 
   *                 From JASPIC Spec section 3.8.3.5: When a ServerAuthModule
   *                 returns a wrapped message in MessageInfo, or unwraps a
   *                 message in MessageInfo, the message processing runtime must
   *                 ensure that the HttpServletRequest and HttpServletResponse
   *                 objects established by the ServerAuthModule are used in
   *                 downstream processing.
   * 
   *                 This test uses a Server Authentication Module(SAM) that
   *                 wraps and unwraps the HttpRequest as specified by the
   *                 JASPIC spec and expects the runtime to handle the Wrapped
   *                 HttpRequest.
   * 
   */
  public void testRequestWrapper() throws Fault {
    String strMsg1 = "isRequestWrapped = true";
    String strMsg2 = "Incorrect request type";

    String str = invokeServletAndGetResponse(wrapperServletPath, "POST",
        "testRequestWrapper");
    TestUtil.logMsg("testRequestWrapper response msg = " + str);

    int ii = str.indexOf(strMsg1);
    if (ii < 0) {
      TestUtil.logMsg("testRequestWrapper Error - could not find: " + strMsg1);
      throw new Fault("testRequestWrapper : FAILED");
    }

    ii = str.indexOf(strMsg2);
    if (ii >= 0) {
      // this may indicate secureResponse was incorrectly called before service
      // invocation
      TestUtil
          .logMsg("testRequestWrapper Error - found occurance of: " + strMsg2);
      throw new Fault("testRequestWrapper : FAILED");
    }

    TestUtil.logMsg("testRequestWrapper : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: testResponseWrapper
   *
   * @assertion_ids: JASPIC:SPEC:108; JASPIC:SPEC:311;
   *
   * @test_Strategy: From JASPIC Spec section 3.8.3.5: When a ServerAuthModule
   *                 returns a wrapped message in MessageInfo, or unwraps a
   *                 message in MessageInfo, the message processing runtime must
   *                 ensure that the HttpServletRequest and HttpServletResponse
   *                 objects established by the ServerAuthModule are used in
   *                 downstream processing.
   * 
   *                 This test uses a Server Authentication Module(SAM) that
   *                 wraps and unwraps the HttpResponse as specified by the
   *                 JASPIC spec and expects the runtime to handle the Wrapped
   *                 HttpResponse.
   */
  public void testResponseWrapper() throws Fault {
    String strMsg1 = "isResponseWrapped = true";
    String strMsg2 = "Incorrect response type";

    String str = invokeServletAndGetResponse(wrapperServletPath, "POST",
        "testResponseWrapper");
    TestUtil.logMsg("testResponseWrapper response msg = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      TestUtil.logMsg("testResponseWrapper Error - could not find: " + strMsg1);
      throw new Fault("testResponseWrapper : FAILED");
    }

    ii = str.indexOf(strMsg2);
    if (ii >= 0) {
      // this may indicate secureResponse was incorrectly called before service
      // invocation
      TestUtil
          .logMsg("testResponseWrapper Error - found occurance of: " + strMsg2);
      throw new Fault("testResponseWrapper : FAILED");
    }

    TestUtil.logMsg("testResponseWrapper : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: ACFUnregisterACP
   *
   * @assertion_ids: JASPIC:SPEC:344;
   *
   * @test_Strategy: This will make a server (actually servlet) side method call
   *                 that will do the following: - load vendors ACF -
   *                 (persistent) register of CTS ACP's in the vendors ACF - get
   *                 list of vendors registered provider ID's - register
   *                 (in-memory) ACP (this is standalone ACP profile) - verify
   *                 another regId was added for standalone ACP - unregister the
   *                 in-memory ACP we just registered - verify
   *                 removeRegistration() method call returned proper boolean -
   *                 verify expected # of registry eentries - verify 2nd call to
   *                 removeRegistration() with regId that was previously removed
   *                 and should expect return val of false
   *
   */
  public void ACFUnregisterACP() throws Fault {
    String strMsg1 = "ACFTestServlet->ACFUnregisterACP() passed";

    String str = invokeServletAndGetResponse(acfServletPath, "POST",
        "ACFUnregisterACP");
    TestUtil.logMsg("ACFUnregisterACP : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("ACFUnregisterACP : FAILED");
    }

    TestUtil.logMsg("ACFUnregisterACP : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: CheckACFVerifyPersistence
   *
   * @assertion_ids: JASPIC:SPEC:75; JASPIC:SPEC:335;
   *
   * @test_Strategy: This tests the (persistent) registration of CTS ACP's
   *                 within the the vendors ACF.
   *
   */
  public void CheckACFVerifyPersistence() throws Fault {
    String strMsg1 = "AuthConfigFactoryVerifyPersistence() passed";

    String str = invokeServletAndGetResponse(acfServletPath, "POST");
    TestUtil.logMsg("CheckACFVerifyPersistence : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("CheckACFVerifyPersistence : FAILED");
    }

    TestUtil.logMsg("CheckACFVerifyPersistence : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: CheckRegistrationContextId
   *
   * @assertion_ids: JASPIC:SPEC:77;
   *
   * @test_Strategy: This registers CTS ACP's within vendors ACF and then
   *                 verifies the RegistrationContext info can be obtained from
   *                 the vendors ACF.
   *
   */
  public void CheckRegistrationContextId() throws Fault {
    String strMsg1 = "getRegistrationContextId() TSProviders registration passed";

    String str = invokeServletAndGetResponse(acfServletPath, "POST");
    TestUtil.logMsg("CheckRegistrationContextId : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("CheckRegistrationContextId : FAILED");
    }

    TestUtil.logMsg("CheckRegistrationContextId : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: CheckCallbackSupport
   *
   * @assertion_ids: JASPIC:SPEC:72; JASPIC:SPEC:103; JASPIC:JAVADOC:27;
   *                 JASPIC:JAVADOC:40; JASPIC:JAVADOC:46; JASPIC:JAVADOC:31;
   *                 JASPIC:JAVADOC:32; JASPIC:JAVADOC:33; JASPIC:JAVADOC:34;
   *                 JASPIC:JAVADOC:38; JASPIC:JAVADOC:39; JASPIC:JAVADOC:41;
   *                 JASPIC:JAVADOC:42; JASPIC:JAVADOC:43; JASPIC:JAVADOC:44;
   *                 JASPIC:JAVADOC:35; JASPIC:JAVADOC:36; JASPIC:JAVADOC:51;
   *                 JASPIC:JAVADOC:49; JASPIC:JAVADOC:54; JASPIC:JAVADOC:65;
   *                 JASPIC:JAVADOC:63; JASPIC:JAVADOC:68; JASPIC:JAVADOC:69;
   *                 JASPIC:JAVADOC:71; JASPIC:JAVADOC:28; JASPIC:JAVADOC:107;
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify Whether the following callbackHandlers are
   *                 supported: CallerPrincipalCallback, GroupPrincipalCallback,
   *                 and PasswordValidationCallback.
   *
   *                 Description The CallbackHandler passed to
   *                 ServerAuthModule.initialize must support the following
   *                 callbacks: CallerPrincipalCallback, GroupPrincipalCallback,
   *                 and PasswordValidationCallback.
   *
   */
  public void CheckCallbackSupport() throws Fault {
    String strMsg1 = "In HttpServlet : ServerRuntime CallbackHandler supports CallerPrincipalCallback";
    String strMsg2 = "In HttpServlet : ServerRuntime CallbackHandler supports GroupPrincipalCallback";
    String strMsg3 = "In HttpServlet : ServerRuntime CallbackHandler supports PasswordValidationCallback";
    String tempArgs[] = { strMsg1, strMsg2, strMsg3 };

    // by invoking this servlet, we are causing code to access our
    // servlet profile TSServerAuthModule.validateRequest() method
    // which will perform checks to see which CBH's are supported.
    invokeServlet(servletPath, "POST", "CheckCallbackSupport");

    boolean verified = logProcessor.verifyLogContains(tempArgs);
    if (!verified) {
      throw new Fault("CheckCallbackSupport : FAILED");
    }

    TestUtil.logMsg("CheckCallbackSupport : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: testGPCWithNoRequiredAuth
   *
   * @assertion_ids: JASPIC:SPEC:90;
   * @test_Strategy: This is checking that the group callbacks work for case of
   *                 being called with CPC when no auth is required.
   *
   *                 Description This ultmately calls
   *                 ServerCallbackSupport.GroupPrincipalCallbackSupport() with
   *                 no required creds.
   */
  public void testGPCWithNoRequiredAuth() throws Fault {

    String strMsg1 = "In HttpServlet : ServerRuntime GroupPrincipalCallbackSupport():";
    strMsg1 += " successfully called callbackHandler.handle(callbacks)";
    strMsg1 += " for servlet: " + openToAllServletPath;
    strMsg1 += " with isServletAuthMandatory = " + false;

    String tempArgs[] = { strMsg1 };

    // by invoking this servlet, we are causing code to access our
    // servlet profile TSServerAuthModule.validateRequest() method
    // which will perform checks to see which CBH's are supported.
    String str = invokeServletAndGetResponse(allAccessServletPath, "POST",
        "testGPCWithNoRequiredAuth");

    boolean verified = logProcessor.verifyLogContains(tempArgs);
    if (!verified) {
      throw new Fault("testGPCWithNoRequiredAuth : FAILED");
    }

    TestUtil.logMsg("testGPCWithNoRequiredAuth : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: testGPCIsUserInRole
   *
   * @assertion_ids: JASPIC:SPEC:90;
   * @test_Strategy: The ModTestServlet is called which forces our SAM to invoke
   *                 callbacks - including the GPC. This test expects and checks
   *                 that the GPC was invoked with non-null principal and that
   *                 auth was mandatory and that upon return from
   *                 validateRequest, there are values set in
   *                 HttpServletRequest.isUserInRole()
   *
   *                 Description This is checking that the group callbacks work
   *                 for case of being called with CPC when auth is required and
   *                 the check that the validateRequest ensures there was a
   *                 return value set for calls to
   *                 HttpServletRequest.getRemoteUser()
   * 
   */
  public void testGPCIsUserInRole() throws Fault {

    String strChkServlet = "ModTestServlet->testGPCIsUserInRole() passed";

    String strMsg1 = "In HttpServlet : ServerRuntime GroupPrincipalCallbackSupport():";
    strMsg1 += " successfully called callbackHandler.handle(callbacks)";
    strMsg1 += " for servlet: /ModTestServlet";
    strMsg1 += " with isServletAuthMandatory = " + true;

    String tempArgs[] = { strMsg1 };

    // by invoking this servlet, we are causing code to access our
    // servlet profile TSServerAuthModule.validateRequest() method
    // which will perform checks to see that GPC is supported and
    // that the IsUserInRole() is properly populated.
    String str = invokeServletAndGetResponse(servletPath, "POST",
        "testGPCIsUserInRole");

    // First, lets make sure CBH's were actually called
    boolean verified = logProcessor.verifyLogContains(tempArgs);
    if (!verified) {
      throw new Fault("testGPCIsUserInRole : FAILED");
    }

    // next, lets make sure CBH's set principal values as expected
    if (!str.contains(strChkServlet)) {
      String msg = "testGPCIsUserInRole did not have proper credential values returned in ";
      msg += servletPath
          + ".  this could be due to CBH's not properly doing authentication.";
      TestUtil.logMsg(msg);
      throw new Fault("testGPCIsUserInRole : FAILED");
    }

    TestUtil.logMsg("testGPCIsUserInRole : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: testGPCGetRemoteUser
   *
   * @assertion_ids: JASPIC:SPEC:90;
   * @test_Strategy: The ModTestServlet is called which forces our SAM to invoke
   *                 callbacks - including the GPC. This test expects and checks
   *                 that the GPC was invoked with non-null principal and that
   *                 auth was mandatory and that upon return from
   *                 validateRequest, there are values set in
   *                 HttpServletRequest.getRemoteUser()
   *
   *                 Description This is checking that the group callbacks work
   *                 for case of being called with CPC when auth is required and
   *                 the check that the validateRequest ensures there was a
   *                 return value set for calls to
   *                 HttpServletRequest.getRemoteUser()
   * 
   */
  public void testGPCGetRemoteUser() throws Fault {

    String strChkServlet = "ModTestServlet->testGPCGetRemoteUser() passed";

    String strMsg1 = "In HttpServlet : ServerRuntime GroupPrincipalCallbackSupport():";
    strMsg1 += " successfully called callbackHandler.handle(callbacks)";
    strMsg1 += " for servlet: /ModTestServlet";
    strMsg1 += " with isServletAuthMandatory = " + true;

    String tempArgs[] = { strMsg1 };

    // by invoking this servlet, we are causing code to access our
    // servlet profile TSServerAuthModule.validateRequest() method
    // which will perform checks to see that GPC is supported and
    // that the GetRemoteUser() is properly populated.
    String str = invokeServletAndGetResponse(servletPath, "POST",
        "testGPCGetRemoteUser");

    // First, lets make sure CBH's were actually called
    boolean verified = logProcessor.verifyLogContains(tempArgs);
    if (!verified) {
      throw new Fault("testGPCGetRemoteUser : FAILED");
    }

    // next, lets make sure CBH's set principal values as expected
    if (!str.contains(strChkServlet)) {
      String msg = "testGPCGetRemoteUser did not have proper credential values returned in ";
      msg += servletPath
          + ".  this could be due to CBH's not properly doing authentication.";
      TestUtil.logMsg(msg);
      throw new Fault("testGPCGetRemoteUser : FAILED");
    }

    TestUtil.logMsg("testGPCGetRemoteUser : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: testGPCGetUserPrincipal
   *
   * @assertion_ids: JASPIC:SPEC:90;
   * @test_Strategy: The ModTestServlet is called which forces our SAM to invoke
   *                 callbacks - including the GPC. This test expects and checks
   *                 that the GPC was invoked with non-null principal and that
   *                 auth was mandatory and that upon return from
   *                 validateRequest, there are values set in
   *                 HttpServletRequest.getUserPrincipal()
   *
   *                 Description This is checking that the group callbacks work
   *                 for case of being called with CPC when auth is required and
   *                 the check that the validateRequest ensures there was a
   *                 return value set for calls to
   *                 HttpServletRequest.getUserPrincipal()
   * 
   */
  public void testGPCGetUserPrincipal() throws Fault {

    String strChkServlet = "ModTestServlet->testGPCGetUserPrincipal() passed";

    String strMsg1 = "In HttpServlet : ServerRuntime GroupPrincipalCallbackSupport():";
    strMsg1 += " successfully called callbackHandler.handle(callbacks)";
    strMsg1 += " for servlet: /ModTestServlet";
    strMsg1 += " with isServletAuthMandatory = " + true;

    String tempArgs[] = { strMsg1 };

    // by invoking this servlet, we are causing code to access our
    // servlet profile TSServerAuthModule.validateRequest() method
    // which will perform checks to see that GPC is supported and
    // that the GetUserPrincipal() is properly populated.
    String str = invokeServletAndGetResponse(servletPath, "POST",
        "testGPCGetUserPrincipal");

    // First, lets make sure CBH's were actually called
    boolean verified = logProcessor.verifyLogContains(tempArgs);
    if (!verified) {
      throw new Fault("testGPCGetUserPrincipal : FAILED");
    }

    // next, lets make sure CBH's set principal values as expected
    if (!str.contains(strChkServlet)) {
      String msg = "testGPCGetUserPrincipal did not have proper credential values returned in ";
      msg += servletPath
          + ".  this could be due to CBH's not properly doing authentication.";
      TestUtil.logMsg(msg);
      throw new Fault("testGPCGetUserPrincipal : FAILED");
    }

    TestUtil.logMsg("testGPCGetUserPrincipal : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: testGPCGetAuthType
   *
   * @assertion_ids: JASPIC:SPEC:90;
   * @test_Strategy: The ModTestServlet is called which forces our SAM to invoke
   *                 callbacks - including the GPC. This test expects and checks
   *                 that the GPC was invoked with non-null principal and that
   *                 auth was mandatory and that upon return from
   *                 validateRequest, there are values set in
   *                 HttpServletRequest.getAuthType()
   *
   *                 Description This is checking that the group callbacks work
   *                 for case of being called with CPC when auth is required and
   *                 the check that the validateRequest ensures there was a
   *                 return value set for calls to
   *                 HttpServletRequest.getAuthType()
   * 
   */
  public void testGPCGetAuthType() throws Fault {

    String strChkServlet = "ModTestServlet->testGPCGetAuthType() passed";

    String strMsg1 = "In HttpServlet : ServerRuntime GroupPrincipalCallbackSupport():";
    strMsg1 += " successfully called callbackHandler.handle(callbacks)";
    strMsg1 += " for servlet: /ModTestServlet";
    strMsg1 += " with isServletAuthMandatory = " + true;

    String tempArgs[] = { strMsg1 };

    // by invoking this servlet, we are causing code to access our
    // servlet profile TSServerAuthModule.validateRequest() method
    // which will perform checks to see that GPC is supported and
    // that the GetAuthType() is properly populated.
    String str = invokeServletAndGetResponse(servletPath, "POST",
        "testGPCGetAuthType");

    // First, lets make sure CBH's were actually called
    boolean verified = logProcessor.verifyLogContains(tempArgs);
    if (!verified) {
      throw new Fault("testGPCGetAuthType : FAILED");
    }

    // next, lets make sure CBH's set principal values as expected
    TestUtil.logMsg("searching string: " + strChkServlet);
    TestUtil.logMsg("searching in:  " + str);

    if (!str.contains(strChkServlet)) {
      String msg = "testGPCGetAuthType did not have proper credential values returned in ";
      msg += servletPath
          + ".  this could be due to CBH's not properly doing authentication.";
      TestUtil.logMsg(msg);
      throw new Fault("testGPCGetAuthType : FAILED");
    }

    TestUtil.logMsg("testGPCGetAuthType : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: CheckAuthenInValidateRequest
   *
   * @assertion_ids: JASPIC:SPEC:90;
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify if the validateRequest() was called under the
   *                 scenario of already being logged in. If we are already
   *                 logged in, then there are (3) HttpServletRequest methods
   *                 that must return non-null values:` - getAuthType() -
   *                 getRemoteUser() - getUserPrincipal()
   *
   *                 Description The TSSVLog.txt file looking for string that
   *                 indicates we found a scenario with only some of the
   *                 forementioned methods returned non-null. If a user is
   *                 authenticated, they should return non-null values. If the
   *                 user is NON authenticated, these methods should return
   *                 null.
   *
   */
  public void CheckAuthenInValidateRequest() throws Fault {
    String strMsg1 = "validateRequest():  ERROR - invalid authen scenario.";
    String tempArgs[] = { strMsg1 };

    // by invoking this servlet, we are causing code to access our
    // servlet profile TSServerAuthModule.validateRequest() method
    // which will perform checks to see which CBH's are supported.
    invokeServlet(servletPath, "POST", "CheckAuthenInValidateRequest");

    boolean verified = logProcessor.verifyLogContains(tempArgs);
    if (verified) {
      // we found an error statement
      throw new Fault("CheckAuthenInValidateRequest : FAILED");
    }

    TestUtil.logMsg("CheckAuthenInValidateRequest : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: testAuthenResultsOnHttpServlet
   *
   * @assertion_ids: JASPIC:SPEC:90; JASPIC:SPEC:322;
   *
   * @test_Strategy: When authentication is mandatory, after AuthStatus.SUCCESS
   *                 is returned from validateRequest, we should be able to
   *                 verify that HttpServletRequest has non-null values set for
   *                 the following (3) method calls: request.getAuthType()
   *                 request.getRemoteUser() request.getUserPrincipal()
   *
   *                 This is based on javadoc for HttpServletRequest as well as
   *                 on jsr-196 v1.1 spec section 3.8.1.3 and 3.8.4.
   *
   */
  public void testAuthenResultsOnHttpServlet() throws Fault {
    String strMsg1 = "ModTestServlet->testAuthenResultsOnHttpServlet() passed";

    String str = invokeServletAndGetResponse(servletPath, "POST",
        "testAuthenResultsOnHttpServlet");
    TestUtil.logMsg("testAuthenResultsOnHttpServlet : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("testAuthenResultsOnHttpServlet : FAILED");
    }

    TestUtil.logMsg("testAuthenResultsOnHttpServlet : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: testRemoteUserCorrespondsToPrin
   *
   * @assertion_ids: JASPIC:SPEC:321;
   *
   * @test_Strategy: When authentication is mandatory, after AuthStatus.SUCCESS
   *                 is returned from validateRequest, we should be able to
   *                 verify that (a) HttpServletRequest has non-null values set
   *                 for the following methods: request.getRemoteUser() and
   *                 request.getUserPrincipal() (b) that getRemoteUser() and
   *                 getUserPrincipal().getName() are equal.
   *
   *                 This is based on javadoc for HttpServletRequest as well as
   *                 on jsr-196 v1.1 spec section 3.8.4. Specifically, this is
   *                 validating that: "In both cases, the HttpServletRequest
   *                 must be modified as necessary to ensure that the Principal
   *                 returned by getUserPrincipal and the String returned by
   *                 getRemoteUser correspond, ..." Using the above spec
   *                 reference COMBINED with the following Servlet spec
   *                 reference, we see that:
   * 
   *                 (Using Servlet spec v3.1, section 13.3 - it states: "The
   *                 getRemoteUser method returns the name of the remote user"
   *                 AND "Calling the getName() method on the Principal returned
   *                 by getUserPrincipal() returns the name of the remote user."
   *                 We can use these two Servlet spec references combined with
   *                 our JASPIC reference to validate that principal.getName()
   *                 equals (i.e. "corresponds") to the same value returned by
   *                 getRemoteUser().
   *
   */
  public void testRemoteUserCorrespondsToPrin() throws Fault {
    String strMsg1 = "ModTestServlet->testRemoteUserCorrespondsToPrin() passed";

    String str = invokeServletAndGetResponse(servletPath, "POST",
        "testRemoteUserCorrespondsToPrin");

    TestUtil.logMsg("testRemoteUserCorrespondsToPrin : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("testRemoteUserCorrespondsToPrin : FAILED");
    }

    TestUtil.logMsg("testRemoteUserCorrespondsToPrin : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: testAuthenIsUserInRole
   *
   * @assertion_ids: JASPIC:SPEC:321;
   *
   * @test_Strategy: When authentication is mandatory, after AuthStatus.SUCCESS
   *                 is returned from validateRequest, we should be able to
   *                 verify that HttpServletRequest has non-null values set for
   *                 the following method call: request.isUserInRole()
   *
   *                 This is based on javadoc for HttpServletRequest as well as
   *                 on jsr-196 v1.1 spec section 3.8.1.3 and 3.8.4. There is no
   *                 direct spec reference, but it is a scenario that can be
   *                 inferred.
   */
  public void testAuthenIsUserInRole() throws Fault {
    String strMsg1 = "ModTestServlet->testAuthenIsUserInRole() passed";

    String str = invokeServletAndGetResponse(servletPath, "POST",
        "testAuthenIsUserInRole");

    TestUtil.logMsg("testAuthenIsUserInRole : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("testAuthenIsUserInRole : FAILED");
    }

    TestUtil.logMsg("testAuthenIsUserInRole : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: testAuthenAfterLogout
   *
   * @assertion_ids: JASPIC:SPEC:98;
   *
   * @test_Strategy: When authentication is mandatory, after AuthStatus.SUCCESS
   *                 is returned from validateRequest, we should be able to
   *                 verify that we can properly login and invoke callback
   *                 handlers.
   * 
   *                 This test ensures that we can authN when we are not already
   *                 pre-logged in. This will call a servlet that is open to
   *                 all, (thus no security constraints) and it will invoke the
   *                 servlet logout() command. Then it will make a call to a
   *                 servlet which requires mandatory authentication. The
   *                 premise being that authentication does occur - especially
   *                 if not pre-logged in.
   *
   *                 Note the spec does not state that you can not be pre-logged
   *                 in but it is saying that authentication has to be able to
   *                 succeed in a particular manner. (see spec section 3.8.3.1)
   *
   */
  public void testAuthenAfterLogout() throws Fault {
    String strMsg1 = "ModTestServlet->testAuthenAfterLogout() passed";

    // call a servlet that is open to all and requires NO authN to access
    // validate we were not pre-authenticated and if so, force logout
    String str = invokeServletAndGetResponse(allAccessServletPath, "POST",
        "TestAuthenAfterLogout");

    // now make a call to a servlet that requires mandatory authN and
    // make sure we still successfully support callbacks
    str = invokeServletAndGetResponse(servletPath, "POST",
        "testAuthenAfterLogout");

    TestUtil.logMsg("testAuthenAfterLogout : str = " + str);
    int ii = str.indexOf(strMsg1);
    if (ii < 0) {
      throw new Fault("testAuthenAfterLogout : FAILED");
    }

    // next make sure we see our CPC being properly called.
    String strMsg2 = "In HttpServlet : ServerRuntime CallerPrincipalCallback";
    strMsg2 += " called for profile=HttpServlet for servletPath=" + servletPath;
    String strMsg3 = "Validated we are not prelogged in for OpenToAllServlet";
    String tempArg1[] = { strMsg2, strMsg3 };

    TestUtil.logMsg("validating CPC worked and logfile contains: " + strMsg2);
    TestUtil.logMsg(
        "validating we were not prelogged in and that logfile contains: "
            + strMsg3);
    boolean verified = logProcessor.verifyLogContains(tempArg1);
    if (!verified) {
      throw new Fault("testAuthenAfterLogout : FAILED");
    }

    TestUtil.logMsg("testAuthenAfterLogout : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: verifyRuntimeCallOrder
   *
   * @assertion_ids: JASPIC:SPEC:317; JASPIC:SPEC:304;
   *
   * @test_Strategy: This test verifies that the runtime is invoking the proper
   *                 calling order of: validateRequest, process request,
   *                 secureResponse.
   *
   *                 Details After AuthStatus.SUCCESS is returned from
   *                 validateRequest, we need to verify that the request was
   *                 dispatched, and then that the secureResponse() was called -
   *                 in that order. We do this by verifying that the messages
   *                 (i.e. strMsg1, strMsg2, and strMsg3) were called in the
   *                 specific order. The verifyLogContains() method checks these
   *                 strings exist within the TSSVLog.txt file AND that they
   *                 exist within the log file in order that matches the way
   *                 they are declared via "theArgs[]".
   *
   */
  public void verifyRuntimeCallOrder() throws Fault {

    // strings that MUST exist within the TSSVLog.txt file
    String strMsg1 = "TSServerAuthContext.validateRequest called for layer=HttpServlet for requestURI="
        + wrapperServletPath;
    String strMsg2 = "WrapperServlet.doTests() content processed for requestURI";
    String strMsg3 = "secureResponse called for layer=HttpServlet for requestURI="
        + wrapperServletPath;
    String theArgs[] = { strMsg1, strMsg2, strMsg3 };

    TestUtil.logMsg("Enterred verifyRuntimesCallOrder()");

    String str = invokeServletAndGetResponse(wrapperServletPath, "POST",
        "testRequestWrapper");
    TestUtil.logMsg("TestRequestWrapper response msg = " + str);

    boolean verified = logProcessor.verifyLogContains(theArgs, true);
    if (!verified) {
      throw new Fault("verifyRuntimesCallOrder : FAILED");
    }

    TestUtil.logMsg("verifyRuntimesCallOrder : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: VerifyNoInvalidEntries
   *
   * @assertion_ids: JASPIC:SPEC:52; JASPIC:SPEC:96; JASPIC:SPEC:53;
   *                 JASPIC:SPEC:87; JASPIC:SPEC:313; JASPIC:SPEC:60;
   *                 JASPIC:JAVADOC:106; JASPIC:SPEC:322; JASPIC:SPEC:323;
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Invoke a series of web pages with varying servlet perms
   *                 and access/connection scenarios.
   *
   *                 3. Use FetchLog servlet to read the server side log to
   *                 verify no invalid scenarios were encountered.
   * 
   *
   *                 Description This is a negative test case that is used to
   *                 assist in verifying several different assertions. The
   *                 intention of this test is to make sure accessing web
   *                 resources under different circumstances should NOT cause
   *                 any invalid issues to be encountered.
   *
   */
  public void VerifyNoInvalidEntries() throws Fault {

    // try to invoke diff servlet pages to see if this
    // causes an invalid condition to occur
    invokeServlet(staticPagePath, "POST", "VerifyNoInvalidEntries");
    invokeServlet(noConstraintPath, "POST", "VerifyNoInvalidEntries");
    invokeServlet(servletPath, "POST", "VerifyNoInvalidEntries");

    // now check log file lookign for invalid entries
    doCommonVerificationChecks();

    TestUtil.logMsg("VerifyNoInvalidEntries : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: VerifyClientSubjects
   *
   * @assertion_ids: JASPIC:SPEC:96; JASPIC:SPEC:52; JASPIC:JAVADOC:28;
   *                 JASPIC:JAVADOC:106; JASPIC:SPEC:23; JASPIC:SPEC:19;
   *                 JASPIC:SPEC:51;
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify whether the ClientSubject passed to validateRequest
   *                 was null or not.
   *
   *                 Description According to spec section 3.8.2: A new
   *                 clientSubject must be instantiated and passed in the call
   *                 to validateRequest.
   *
   */
  public void VerifyClientSubjects() throws Fault {
    String strMsg1 = "HttpServlet profile: ";
    strMsg1 += "TSServerAuthContext.validateRequest called with non-null client Subject";
    String tempArgs[] = { strMsg1 };

    invokeServlet(servletPath, "POST", "VerifyClientSubjects");

    // first, verify we have a non-null clientSubject
    boolean verified = logProcessor.verifyLogContains(tempArgs);
    if (!verified) {
      throw new Fault("VerifyClientSubjects : FAILED");
    }

    // now verify we have no occurances of null clientSubjects
    checkForInvalidClientSubjects();

    TestUtil.logMsg("VerifyClientSubjects : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: CheckMessageInfo
   *
   * @assertion_ids: JASPIC:SPEC:95; JASPIC:JAVADOC:28; JASPIC:JAVADOC:10;
   *                 JASPIC:JAVADOC:11; JASPIC:SPEC:23; JASPIC:SPEC:19;
   *                 JASPIC:SPEC:69;
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify Whether the messageInfo passed to validateRequest()
   *                 contains proper values for getRequestMessage() and
   *                 getResponseMessage().
   *
   *                 Description The MessageInfo argument used in any call made
   *                 by the message processing runtime to validateRequest must
   *                 have been initialized such that the non-null objects
   *                 returned by the getRequestMessage and getResponseMessage
   *                 methods of the MessageInfo are an instanceof
   *                 HttpServletRequest and HttpServletResponse.
   *
   */
  public void CheckMessageInfo() throws Fault {
    String strMsg1 = "validateRequest: MessageInfo.getRequestMessage() is of type ";
    String strMsg2 = "validateRequest: MessageInfo.getResponseMessage() is of type ";
    String tempArgs[] = { strMsg1 + "javax.servlet.http.HttpServletRequest",
        strMsg2 + "javax.servlet.http.HttpServletResponse" };

    // this should work for servlets and static pages
    // invoking a static should cause the validateRequest to be called
    invokeServlet(staticPagePath, "POST", "CheckMessageInfo");

    boolean verified = logProcessor.verifyLogContains(tempArgs);
    if (!verified) {
      throw new Fault("CheckMessageInfo : FAILED");
    }

    TestUtil.logMsg("CheckMessageInfo : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: VerifyReqPolicy
   *
   * @assertion_ids: JASPIC:SPEC:87; JASPIC:JAVADOC:107; JASPIC:JAVADOC:17;
   *                 JASPIC:JAVADOC:14; JASPIC:JAVADOC:21
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify we have more than 1 target policy AND that
   *                 ProtectionPolicy.getID() returns a proper value.
   * 
   *
   *                 Description Calling getTargetPolicies on the request
   *                 MessagePolicy must return an array containing at least one
   *                 TargetPolicy whose ProtectionPolicy will be interpreted by
   *                 the modules of the context to mean that the source of the
   *                 corresponding targets within the message is to be
   *                 authenticated. To that end, calling the getID method on the
   *                 ProtectionPolicy must return one of the following values:
   *                 ProtectionPolicy.AUTHENTICATE_SENDER,
   *                 ProtectionPolicy.AUTHENTICATE_CONTENT
   *
   */
  public void VerifyReqPolicy() throws Fault {

    // first make sure we have an array with more than 1 TargetPolicy
    invokeServlet(servletPath, "POST", "VerifyReqPolicy");
    checkForInvalidReqPolicy();

    // next, make sure ProtectionPolicy.getID() returns valid value
    checkForInvalidProtectionPolicyID();

    TestUtil.logMsg("VerifyReqPolicy : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: checkSACValidateRequestWithVaryingAccess
   *
   * @assertion_ids: JASPIC:SPEC:89; JASPIC:JAVADOC:28
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify ServerAuthContext.validateRequest() is called for
   *                 our servlet container even when no access will be granted.
   *
   *                 Description This will test that the message processing
   *                 runtime must call validateRequest independent of whether or
   *                 not access to the resource would have been permitted prior
   *                 to the call to validateRequest. Note: This particular
   *                 assertion actually only applies to the case of acceptable
   *                 connections that have been negotiated and will not be
   *                 required for cases where the WebUserDataPermission objects
   *                 allow a container to determine when to reject a request
   *                 befor redirection of that request would ultimately be
   *                 rejected as a result of an excluding auth-constraint. (an
   *                 excluding auth-constraint is an auth-constraint that has no
   *                 roles. (see JSR-115 spec, section 3.1.3.1, footnote 1)
   */
  public void checkSACValidateRequestWithVaryingAccess() throws Fault {
    String HDR = "TSServerAuthContext.validateRequest called for layer=HttpServlet";
    String FTR = " AuthStatus=AuthStatus.SUCCESS";
    String strMsg1 = HDR + " for requestURI=" + noConstraintPath + FTR;
    String tempArg1[] = { strMsg1 };
    String strMsg2 = HDR + " for requestURI=" + servletPath + FTR;
    String tempArg2[] = { strMsg2 };

    // invoking a servlet should cause the validateRequest to be called
    // and here we are invoking a url that has no auth-constraints
    invokeServlet(noConstraintPath, "POST",
        "checkSACValidateRequestWithVaryingAccess");

    boolean verified = logProcessor.verifyLogContains(tempArg1);
    if (!verified) {
      throw new Fault("checkSACValidateRequestWithVaryingAccess : FAILED");
    }

    // invoking a servlet should cause the validateRequest to be called
    // and here we are invoking a url that has auth-constraints
    invokeServlet(servletPath, "POST",
        "checkSACValidateRequestWithVaryingAccess");
    verified = logProcessor.verifyLogContains(tempArg2);
    if (!verified) {
      throw new Fault("checkSACValidateRequestWithVaryingAccess : FAILED");
    }

    TestUtil.logMsg("checkSACValidateRequestWithVaryingAccess : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: VerifyMessageInfoObjects
   *
   * @assertion_ids: JASPIC:SPEC:60; JASPIC:JAVADOC:27; JASPIC:JAVADOC:28;
   *                 JASPIC:SPEC:305;
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Invoke a servlet to cause a validateRequest invocation,
   *                 (make sure it is a servlet that has Authen Permissions
   *                 enabled so that we can return AuthStatus.SUCCESS from
   *                 validateRequest() so that we can be sure to enter into
   *                 secureResponse(). Once in secureResponse, we want to
   *                 validate the MessageInfo object is the same as the
   *                 MessageInfo object passed into the validateRequest()
   *
   *                 Description Verify that the MessageInfo object passed into
   *                 the validateRequest() method is the same MessageInfo object
   *                 that is passed into secureResponse().
   * 
   *
   */
  public void VerifyMessageInfoObjects() throws Fault {
    String strMsg1 = "MessageInfo object from secureRequest matches ";
    strMsg1 += "the  messageInfo object from validateRequest";
    String tempArgs[] = { strMsg1 };

    // invoking this servlet should cause the validateRequest and
    // the secureResponse to be called.
    String theContext = contextPath + "/" + JASPICData.AUTHSTAT_MAND_SUCCESS;
    invokeServlet(theContext, "POST", "VerifyMessageInfoObjects");

    boolean verified = logProcessor.verifyLogContains(tempArgs);
    if (!verified) {
      throw new Fault("VerifyMessageInfoObjects : FAILED");
    }

    TestUtil.logMsg("VerifyMessageInfoObjects : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: VerifyServiceSubjects
   *
   * @assertion_ids: JASPIC:SPEC:53; JASPIC:SPEC:61; JASPIC:SPEC:314;
   *                 JASPIC:SPEC:313; JASPIC:JAVADOC:27; JASPIC:JAVADOC:28;
   *                 JASPIC:SPEC:24
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Invoke a servlet to cause a validateRequest invocation,
   *                 then Use FetchLog servlet to read the server side log to
   *                 verify we have no valid service subject messages.
   *
   *                 Description Verify that If a non-null Subject was used to
   *                 acquire the ServerAuthContext, the same Subject must be
   *                 passed as the serviceSubject in this call (ie to Point 2 in
   *                 MPR model).
   *
   */
  public void VerifyServiceSubjects() throws Fault {

    // ideally we'd like to find a platform independant way to
    // also verify that we have valid (non-null) serviceSubjects
    // but I don't believe this is possible so for now, we will
    // be content to verify no invalid serviceSubjects were found

    // invoking a servlet should cause the validateRequest to be called
    // but does not guarantee a non-null serviceSubject
    invokeServlet(servletPath, "POST", "VerifyServiceSubjects");

    // currently we verify that there were no invalid serviceSubject
    checkForInvalidServiceSubjects();

    TestUtil.logMsg("VerifyServiceSubjects : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: VerifySAContextVerifyReqIsCalled
   *
   * @assertion_ids: JASPIC:SPEC:88; JASPIC:SPEC:50; JASPIC:JAVADOC:28
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify ServerAuthContext.validateRequest() is called for
   *                 our servlet container.
   *
   *                 Description The inbound processing of requests by a servlet
   *                 container corresponds to point (2) in the message
   *                 processing model. At point (2) in the message processing
   *                 model, the runtime must call validateRequest on the
   *                 ServerAuthContext.
   *
   */
  public void VerifySAContextVerifyReqIsCalled() throws Fault {
    String strMsg1 = "TSServerAuthContext.validateRequest called for layer=HttpServlet";
    String tempArgs[] = { strMsg1 };

    // invoking a servlet should cause the validateRequest to be called
    invokeServlet(servletPath, "POST", "VerifySAContextVerifyReqIsCalled");

    boolean verified = logProcessor.verifyLogContains(tempArgs);
    if (!verified) {
      throw new Fault("VerifySAContextVerifyReqIsCalled : FAILED");
    }

    TestUtil.logMsg("VerifySAContextVerifyReqIsCalled : PASSED");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: CheckMsgInfoKey
   *
   * @assertion_ids: JASPIC:SPEC:306; JASPIC:SPEC:300; JASPIC:JAVADOC:73;
   *                 JASPIC:SPEC:92; JASPIC:SPEC:24; JASPIC:SPEC:103;
   * 
   *
   * @test_Strategy: 1. invoke servlet that requires authentication 2. verify
   *                 that the MesageInfo object passed into getAuthContextID,
   *                 validateRequest, and secureResponse all had valid key-value
   *                 pairs.
   *
   *                 Description: This profile requires that the message
   *                 processing runtime conditionally establish the following
   *                 key-value pair within the Map of the MessageInfo object
   *                 passed in the calls to getAuthContextID, validateRequest,
   *                 and secureResponse. (key)
   *                 javax.security.auth.message.MessagePolicy.isMandatory (val)
   *                 Any non-null String value, s, for which
   *                 Boolean.valueOf(s).booleanValue() == true. The MessageInfo
   *                 map must contain this key and its associated value, if and
   *                 only if authentication is required to perform the resource
   *                 access corresponding to the HttpServletRequest to which the
   *                 ServerAuthContext will be applied.
   *
   *                 If we are 115 compatible, there is an additional test we
   *                 must make. The key=javax.security.jacc.PolicyContext better
   *                 exist in the Properties arg passed into getAuthContext().
   *
   */
  public void CheckMsgInfoKey() throws Fault {
    String CMN_HDR = "dumpServletProfileKeys() called with attrs:  layer=HttpServlet";
    CMN_HDR += " servletName=/ModTestServlet callerMethod=";

    String CMN_TAIL = " key=javax.security.auth.message.MessagePolicy.isMandatory";
    CMN_TAIL += " value=Valid";

    String strAuthCtxt = CMN_HDR + "getAuthContextID" + CMN_TAIL;
    String strValReq = CMN_HDR + "validateRequest" + CMN_TAIL;

    // String tempArgs[] = { strAuthCtxt, strValReq, strSecResp };
    String tempArgs[] = { strAuthCtxt, strValReq };

    // see if we can successfully call into the servlet!
    invokeServlet(servletPath, "POST", "CheckMsgInfoKey");
    boolean bVerified = logProcessor.verifyLogContains(tempArgs);

    if (!bVerified) {
      throw new Fault("CheckMsgInfoKey : FAILED");
    }

    //
    // ONLY if we are 115 compatible, we want to make an additional key test
    //
    if (bIs115Compatible) {
      String HDR = "layer=HttpServlet appContext=" + appContext;
      String str1 = HDR
          + " Key=javax.security.jacc.PolicyContext does exist thus 115 compatible";
      String arg1[] = { str1 };

      invokeServlet(servletPath, "POST", "CheckMsgInfoKey");

      // verify whether the log contains required messages.
      bVerified = logProcessor.verifyLogContains(arg1);

      if (!bVerified) {
        throw new Fault("CheckMsgInfoKey : FAILED");
      }
    } else {
      // not 115 compat so the identity must include the caller Principal
      // (established during the validateRequest processing using the
      // corresponding CallerPrincipalCallback)
      // note: by testing the subject=non-null, we are actually verifying that
      // assertion JASPIC:SPEC:103 is met (e.g. non-null clientSubject used in
      // CPC)
      String strMsg1 = "In HttpServlet : ServerRuntime CallerPrincipalCallback";
      strMsg1 += " called for profile=HttpServlet for servletPath="
          + servletPath;
      strMsg1 += " subject=non-null principal set = j2ee";
      String tempArg1[] = { strMsg1 };
      bVerified = logProcessor.verifyLogContains(tempArg1);
      if (!bVerified) {
        throw new Fault("CheckMsgInfoKey : FAILED");
      }
    }

    TestUtil.logMsg("CheckMsgInfoKey : PASSED");
  }

  /**
   *
   * @keywords: jaspic_servlet
   *
   * @testName: CheckServletAppContext
   *
   * @assertion_ids: JASPIC:SPEC:67; JASPIC:SPEC:68; JASPIC:JAVADOC:84;
   *                 JASPIC:JAVADOC:85;
   *
   * @test_Strategy: 1. Verify TSAuthConfigProviderServlet was registered with
   *                 layer="HttpServlet" and that TSServerAuthConfig was invoked
   *                 with this layer value also to satisfy assertion 67.
   *
   *                 2. our ProviderConfiguration should have registered a
   *                 provider for us AND doing that should have caused the MPR
   *                 to use the same appContextId for the ACP and
   *                 ServerAuthConfig. We will check that both were invoked with
   *                 the same/expected appContextId to satisfy assertion 68.
   *
   *                 Description: This test will verify that the MPR uses the
   *                 same appContextId for both the AuthConfigProvider and the
   *                 ServerAuthConfig.
   *
   */
  public void CheckServletAppContext() throws Fault {
    String MNAME = "CheckServletAppContext : ";
    String layer = "HttpServlet";
    // String appContext = "localhost " + contextPath;
    String acfMsg = "getConfigProvider called for Layer : " + layer
        + " and AppContext :" + appContext;
    String sacMsg = "TSServerAuthConfig called for layer=" + layer
        + " : appContext=" + appContext;

    // verify TSAuthConfigProviderServlet was registered properly
    String tempArgs[] = { acfMsg };
    boolean verified = logProcessor.verifyLogContains(tempArgs);
    if (!verified) {
      TestUtil.logMsg(MNAME
          + "Can't verify TSAuthConfigProviderServlet registered properly");
      TestUtil.logMsg(MNAME + "Could not successfully find: " + acfMsg);
      throw new Fault(MNAME + "FAILED");
    } else {
      TestUtil.logMsg(MNAME
          + "Verified TSAuthConfigProviderServlet was registered properly");
    }

    // verify MPR used same appContextId for the ACP and ServerAuthConfig
    String tempArgs2[] = { sacMsg };
    verified = logProcessor.verifyLogContains(tempArgs2);
    if (!verified) {
      TestUtil.logMsg(MNAME
          + "Can't verify MPR used same appContextId for the ACP and SAC");
      TestUtil.logMsg(MNAME + "Could not successfully find: " + sacMsg);
      throw new Fault(MNAME + "FAILED");
    } else {
      TestUtil.logMsg(
          MNAME + "Verified MPR used same appContextId for the ACP and SAC.");
    }

    TestUtil.logMsg(MNAME + "PASSED");
  }

  /**
   *
   * @keywords: jaspic_servlet
   *
   * @testName: CheckACPConfigObjAppContext
   *
   * @assertion_ids: JASPIC:SPEC:74; JASPIC:SPEC:75; JASPIC:SPEC:11;
   *                 JASPIC:SPEC:13; JASPIC:JAVADOC:94
   *
   * @test_Strategy: 1. Verify that a non-null provider was returned during our
   *                 registration of TSAuthConfigProviderServlet and that by
   *                 fact that non-null provider was returned, we need to verify
   *                 that the MPR called getServerAuthConfig for that provider.
   *                 All these verifications need to be done for the
   *                 corresponding layer and appContextId values. (this satisfys
   *                 assertion 75)
   *
   *                 2. Verify the MPR called getConfigProvider for the layer
   *                 and appContextId in order to satisfy assertion 74)
   *
   *                 Description: This test will verify that the ACP was
   *                 properly registered and that the MPR was able to
   *                 successfully use getConfigPRovider.
   *
   */
  public void CheckACPConfigObjAppContext() throws Fault {
    String MNAME = "CheckACPConfigObjAppContext : ";
    String layer = "HttpServlet";
    String sacMsg = "TSAuthConfigProviderServlet.getServerAuthConfig"
        + " : layer=" + layer + " : appContext=" + appContext;

    // make sure everything we need is properly setup
    invokeServlet(servletPath, "POST", "CheckACPConfigObjAppContext");

    // Verify non-null provider returned during registration
    String tempArgs[] = { ACF_MSG_1 };
    boolean verified = logProcessor.verifyLogContains(tempArgs);
    if (!verified) {
      TestUtil.logMsg(MNAME + "Can't verify non-null provider returned");
      TestUtil.logMsg(MNAME + "Could not successfully find: " + ACF_MSG_1);
      throw new Fault(MNAME + "FAILED");
    } else {
      TestUtil.logMsg(MNAME + "non-null provider returned");
    }

    // verify MPR called getConfigProvider for the layer and appContextId
    String tempArgs2[] = { sacMsg };
    verified = logProcessor.verifyLogContains(tempArgs2);
    if (!verified) {
      TestUtil.logMsg(MNAME + "Can't verify MPR called getConfigProvider");
      TestUtil.logMsg(MNAME + "Could not successfully find: " + sacMsg);
      throw new Fault(MNAME + "FAILED");
    } else {
      TestUtil.logMsg(MNAME + "MPR called getConfigProvider");
    }

    TestUtil.logMsg(MNAME + "PASSED");
  }

  /**
   *
   * @keywords: jaspic_servlet
   *
   * @testName: CheckACPContextObjAppContext
   *
   * @assertion_ids: JASPIC:SPEC:74; JASPIC:SPEC:75; JASPIC:SPEC:77
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify the following: - verify non-null ACP is returned by
   *                 ACF - verify same info used to initialize ACP was also used
   *                 to initialize the ServerAuthConfig (SAC) for this provider.
   *                 - verify same info used in ACP and SAC was also used to
   *                 init the auth context.
   *
   *                 Description: This test verifies that AuthConfigFactory
   *                 returns a non-null ACP and that the same info is used to
   *                 init the ACP, SAC, and the ServerAuthContext.
   *
   */
  public void CheckACPContextObjAppContext() throws Fault {
    String MNAME = "CheckACPContextObjAppContext : ";
    String layer = "HttpServlet";
    String sacMsg = "TSServerAuthConfig called for layer=" + layer
        + " : appContext=" + appContext;

    String saConfigMsg = "TSServerAuthContext called for messageLayer=" + layer
        + " : appContext=" + appContext;

    // make sure everything we need is properly setup
    invokeServlet(servletPath, "POST", "CheckACPContextObjAppContext");

    // verify non-null ACP is returned by ACF
    String tempArgs1[] = { ACF_MSG_1 };
    boolean verified = logProcessor.verifyLogContains(tempArgs1);
    if (!verified) {
      TestUtil
          .logMsg("Could not verify that a non-null ACP was returned by ACF");
      TestUtil.logMsg(MNAME + "Could not successfully find: " + ACF_MSG_1);
      throw new Fault(MNAME + "FAILED");
    } else {
      TestUtil.logMsg(MNAME + "Verified non-null ACP returned by ACF");
    }

    // verify same info used to initialize ACP was also used to init SAC
    String tempArgs2[] = { sacMsg };
    verified = logProcessor.verifyLogContains(tempArgs2);
    if (!verified) {
      TestUtil.logMsg(
          MNAME + "Can't verify same context info was used to init SAC.");
      TestUtil.logMsg(MNAME + "Could not successfully find: " + sacMsg);
      throw new Fault(MNAME + "FAILED");
    } else {
      TestUtil.logMsg(MNAME + "same context info was used to init SAC.");
    }

    // verify same info used in ACP and SAC was also used to init auth context
    String tempArgs3[] = { saConfigMsg };
    verified = logProcessor.verifyLogContains(tempArgs3);
    if (!verified) {
      TestUtil.logMsg(MNAME
          + "Could not verify same context info used to init auth context.");
      TestUtil.logMsg(MNAME + "Could not successfully find: " + saConfigMsg);
      throw new Fault(MNAME + "FAILED");
    } else {
      TestUtil.logMsg(MNAME + "Same context info used to init auth context.");
    }

    TestUtil.logMsg(MNAME + "PASSED");
  }

  /**
   *
   * @keywords: jaspic_servlet
   *
   * @testName: CheckMPRCallsGetAuthContext
   *
   * @assertion_ids: JASPIC:SPEC:74; JASPIC:SPEC:78; JASPIC:JAVADOC:100
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify the following: - verify non-null ACP is returned by
   *                 ACF - verify ServerAuthConfig.getAuthContext() is called by
   *                 MPR
   *
   *                 Description: This test is used to test that the MPR
   *                 properly called ServerAuthConfig.getAuthContext(). This
   *                 will also perform an additional test of verifying a
   *                 non-null ACP is returned by ACF.
   *
   */
  public void CheckMPRCallsGetAuthContext() throws Fault {
    String MNAME = "CheckMPRCallsGetAuthContext : ";
    String layer = "HttpServlet";
    String saConfigMsg = "TSServerAuthConfig.getAuthContext:  layer=" + layer
        + " : appContext=" + appContext;

    // make sure everything we need is properly setup
    invokeServlet(servletPath, "POST", "CheckMPRCallsGetAuthContext");

    // verify non-null ACP is returned by ACF
    String tempArgs[] = { ACF_MSG_1 };
    boolean verified = logProcessor.verifyLogContains(tempArgs);
    if (!verified) {
      TestUtil.logMsg(MNAME + "Can't verify non-null ACP returned by ACF");
      TestUtil.logMsg(MNAME + "Could not successfully find: " + ACF_MSG_1);
      throw new Fault(MNAME + "FAILED");
    } else {
      TestUtil.logMsg(MNAME + "Verified non-null ACP is returned by ACF");
    }

    // verify TSServerAuthConfig.getAuthContext() is called by MPR
    String tempArgs2[] = { saConfigMsg };
    verified = logProcessor.verifyLogContains(tempArgs2);
    if (!verified) {
      TestUtil.logMsg(MNAME + "Can't verify getAuthContext() called by MPR.");
      TestUtil.logMsg(MNAME + "Could not successfully find: " + saConfigMsg);
      throw new Fault(MNAME + "FAILED");
    } else {
      TestUtil
          .logMsg(MNAME + "TSServerAuthConfig.getAuthContext() called by MPR");
    }

    TestUtil.logMsg(MNAME + "PASSED");
  }

  /**
   *
   * @keywords: jaspic_servlet
   *
   * @testName: CheckforNonNullAuthContext
   *
   * @assertion_ids: JASPIC:SPEC:79; JASPIC:JAVADOC:100
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify the following: - verify there were no null values
   *                 returned for all calls to getAuthContext() regardless of
   *                 what operationId is. (this satisfies assertion 79) - verify
   *                 we ARE seeing at least one successful value returned from
   *                 our call to getAuthContext() to satisfy oursleves that the
   *                 test really is working correctly as expected.
   *
   *                 Description: This test verifies that there were no null
   *                 instances of ServerAuthContext objects AND we are also
   *                 verifying that we' are getting at least one non-null
   *                 ServerAuthContext instanace.
   *
   */
  public void CheckforNonNullAuthContext() throws Fault {
    String MNAME = "CheckforNonNullAuthContext : ";
    String str;
    str = "TSServerAuthConfig.getAuthContext: returned null ServerAuthContext";
    String tempArgs[] = { str };

    // this string should NOT/NEVER appear in the log file as
    // the spec states:
    // "For all values of the operation argument, the call to
    // getAuthContext must return a non-null authentication context"
    boolean verified = logProcessor.verifyLogContains(tempArgs);
    if (verified) {
      TestUtil.logMsg(MNAME + "got back null ServerAuthContext");
      throw new Fault("CheckforNonNullAuthContext : FAILED");
    } else {
      TestUtil.logMsg(MNAME + "got back valid ServerAuthContext");
    }

    // We know we should have gotten at least one successful (non-null)
    // value from our call go getAuthContext so lets check to make sure.
    str = "TSServerAuthConfig.getAuthContext: returned non-null ServerAuthContext";
    String tempArgs2[] = { str };
    verified = logProcessor.verifyLogContains(tempArgs2);
    if (!verified) {
      TestUtil.logMsg(MNAME + "got back null ServerAuthContext");
      throw new Fault(MNAME + "FAILED");
    } else {
      TestUtil.logMsg(MNAME + "got back valid ServerAuthContext");
    }

    TestUtil.logMsg(MNAME + "PASSED");
  }

  /**
   *
   * @keywords: jaspic_servlet
   *
   * @testName: CheckforNonNullCallback
   *
   * @assertion_ids: JASPIC:SPEC:71
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify the following: - verify there was a non-null cbh
   *                 passed into the
   *                 TSAuthConfigProviderServlet.getServerAuthConfig() method.
   *                 (spec section 3.5 says the cbh must not be null)
   *
   *                 Description: This test verifies that a non-null
   *                 CakkbackHandler instance passed into the
   *                 AuthConfigProvider.getServerAuthConfig().
   *
   */
  public void CheckforNonNullCallback() throws Fault {
    String MNAME = "CheckforNonNullCallback : ";
    String strMsg1 = "layer=HttpServlet appContext=" + appContext;
    strMsg1 += " getServerAuthConfig() received CallbackHandler=non-null";
    String tempArgs[] = { strMsg1 };

    // make sure everything we need is properly setup
    invokeServlet(servletPath, "POST", "CheckforNonNullCallback");

    boolean verified = logProcessor.verifyLogContains(tempArgs);
    if (!verified) {
      throw new Fault(MNAME + "FAILED");
    }

    TestUtil.logMsg(MNAME + "PASSED");
  }

  /*
   * This is convenience method that can be called anywhere and is intended to
   * be used to make sure no unwanted log messages appear which would indicate a
   * failure. To clarify, this means that a particular test case ~may~ cause
   * some unexpected error to occur which would result in a log message getting
   * dumped to the log file - even though we may not be testing that particular
   * assertion at that given moment. This method can be used as a final pass at
   * the end of each test to ensure that no accidental/unexpected errors were
   * encountered.
   *
   */
  public void doCommonVerificationChecks() throws Fault {

    try {
      // checks that should ALWAYS pass
      checkForInvalidReqPolicy();
      checkForInvalidProtectionPolicyID();
      checkForInvalidServiceSubjects();
      checkForInvalidClientSubjects();
      checkForInvalidMessageInfoObjs();
      checkForNullCallback();
      checkForNullAuthConfigObj();
      checkForAuthenMisMatch();
      checkForinvalidMsgInfoMapKey();
    } catch (Fault ff) {
      throw ff;
    }
  }

  /*
   * This is used to verify assertion JASPIC:SPEC:323 which states in spec
   * section 3.8.4 (para 2)
   * 
   */
  public void checkForinvalidMsgInfoMapKey() throws Fault {
    String strMsg1 = "ERROR - invalid setting for javax.servlet.http.authType = null";
    String strMsg2 = "ERROR - mismatch value set for javax.servlet.http.authType and getAuthType()";
    String tempArgs1[] = { strMsg1, strMsg2 };

    // verify that we had NO mismatches between getAuthType() and
    // getRemoteUser()
    boolean verified = logProcessor.verifyLogContainsOneOf(tempArgs1);
    if (verified) {
      throw new Fault("checkForinvalidMsgInfoMapKey : FAILED");
    }
  }

  /*
   * This is used to verify assertion JASPIC:SPEC:322 which states in spec
   * section 3.8.4 (para 1) "Both cases, must also ensure that the value
   * returned by calling getAuthType on the HttpServletRequest is consistent in
   * terms of being null or non-null with the value returned by
   * getUserPrincipal."
   * 
   */
  public void checkForAuthenMisMatch() throws Fault {
    String strMsg1 = "ERROR - HttpServletRequest authentication result mis-match with getAuthType() and getRemoteUser()";
    String tempArgs1[] = { strMsg1 };

    // verify that we had NO mismatches between getAuthType() and
    // getRemoteUser()
    boolean verified = logProcessor.verifyLogContains(tempArgs1);
    if (verified) {
      throw new Fault("checkForAuthenMisMatch : FAILED");
    }
  }

  /*
   * This is used to verify assertion JASPIC:SPEC:16 which states that the
   * AuthConfigProvider must NOT return a null auth config object (in the
   * getServerAuthConfig() call)
   */
  public void checkForNullAuthConfigObj() throws Fault {
    String strMsg1 = "WARNING: getServerAuthConfig() returned null";
    String tempArgs1[] = { strMsg1 };

    // verify that we had NO bad callbacks passed in
    boolean verified = logProcessor.verifyLogContains(tempArgs1);
    if (verified) {
      throw new Fault("checkForNullAuthConfigObj : FAILED");
    }
  }

  /*
   * This is used to verify assertion JASPIC:SPEC:71 which states that the cbh
   * passed into AuthConfigProvider.getServerAuthConfig() must NOT be null. (see
   * spec section 3.5)
   */
  public void checkForNullCallback() throws Fault {
    String strMsg1 = "FAILURE: layer=HttpServlet appContext=" + appContext;
    strMsg1 += " getServerAuthConfig() received CallbackHandler=null";
    String tempArgs1[] = { strMsg1 };

    // verify that we had NO bad callbacks passed in
    boolean verified = logProcessor.verifyLogContains(tempArgs1);
    if (verified) {
      throw new Fault("checkForNullCallback : FAILED");
    }
  }

  /*
   * This is used to verify assertion JASPIC:SPEC:60 which states that the
   * runtime must pass the same MessageInfo instance (that was passed to
   * validateRequest) into secureRequest. (see spec section 2.1.5.2)
   */
  public void checkForInvalidMessageInfoObjs() throws Fault {
    String strMsg1 = "FAILURE:  MessageInfo object in secureRequest does not";
    strMsg1 += " match the messageInfo object from validateRequest";
    String tempArgs1[] = { strMsg1 };

    boolean verified = logProcessor.verifyLogContains(tempArgs1);
    if (verified) {
      throw new Fault("checkForInvalidMessageInfoObjs : FAILED");
    }

  }

  /*
   * This is used to verify assertion JASPIC:SPEC:52 and JASPIC:SPEC:96 which
   * states that A new clientSubject must be instantiated and passed in the call
   * to validateRequest. (see spec section 3.8.2) We look for occurances of
   * strings that indicate we have a null clientSubject passed into our
   * validateRequest and we issue a failure if a match is found.
   */
  public void checkForInvalidClientSubjects() throws Fault {
    String strMsg2 = "FAILURE detected - ClientSubjects should not be read-only.";
    String strMsg3 = "FAILURE detected - ClientSubjects should not be null.";
    String strMsg1 = "HttpServlet profile: ";
    strMsg1 += "TSServerAuthContext.validateRequest called with null client Subject";
    String tempArgs1[] = { strMsg1, strMsg2, strMsg3 };

    boolean verified = logProcessor.verifyLogContainsOneOf(tempArgs1);
    if (verified) {
      throw new Fault("checkForInvalidClientSubjects : FAILED");
    }

  }

  /*
   * This is convenience method that helps verify assertions related to the
   * validateRequest and secureResponse methods. This tests assertion
   * JASPIC:SPEC:53 which states that for the Servlet profile, If a non-null
   * Subject was used to acquire the ServerAuthContext, the same Subject must be
   * passed as the serviceSubject in this call.
   *
   * This also tests assertion JASPIC:SPEC:313 which states that along with the
   * above requirments if a non-null serviceSubject is used in this call, it
   * must not be read-only. (see spec section 2.1.5.2).
   * 
   * Also tested are assertions JASPIC:SPEC:61, JASPIC:SPEC:314 and
   * JASPIC:SPEC:313.
   * 
   * We look for occurances of strings that indicate we have a serviceSubject
   * mismatch in our runtime - which we should not have.
   */
  public void checkForInvalidServiceSubjects() throws Fault {
    String strMsg1 = "FAILURE detected - ServiceSubjects should be the same and are not.";
    String strMsg2 = "ServiceSubjects correctly matched.";
    String strMsg3 = "found a non-null serviceSubject in getAuthContext()";
    String strMsg4 = "FAILURE detected - ServiceSubjects should not be read-only.";
    String strMsg5 = "FAILURE detected - SecureResponse ServiceSubjects should be the same and are not.";
    String strMsg6 = "FAILURE detected - SecureResponse ServiceSubjects should not be read-only.";
    String tempArgs1[] = { strMsg1 };
    String tempArgs2[] = { strMsg2 };
    String tempArgs3[] = { strMsg3 };
    String tempArgs4[] = { strMsg4 };
    String tempArgs5[] = { strMsg5 }; // assertion: JASPIC:SPEC:61 and
                                      // JASPIC:SPEC:314
    String tempArgs6[] = { strMsg6 }; // assertion: JASPIC:SPEC:313

    boolean verified = logProcessor.verifyLogContainsOneOf(tempArgs1);
    if (verified) {
      TestUtil.logMsg(strMsg1);
      throw new Fault("checkForInvalidServiceSubjects : FAILED");
    }

    verified = logProcessor.verifyLogContainsOneOf(tempArgs5);
    if (verified) {
      TestUtil.logMsg(strMsg5);
      throw new Fault("checkForInvalidServiceSubjects : FAILED");
    }

    verified = logProcessor.verifyLogContainsOneOf(tempArgs6);
    if (verified) {
      TestUtil.logMsg(strMsg6);
      throw new Fault("checkForInvalidServiceSubjects : FAILED");
    }

    verified = logProcessor.verifyLogContains(tempArgs3);
    if (verified) {
      // we know that there was a non-null subject passed into getAuthContext()
      // so
      // we better find strMsg2 in our log file too!
      boolean bfound = logProcessor.verifyLogContains(tempArgs2);
      if (!bfound) {
        throw new Fault("checkForInvalidServiceSubjects : FAILED");
      }

      // if we made it to here, then we have matching serviceSubjects
      // and we should NOT have a read-only service subject
      verified = logProcessor.verifyLogContains(tempArgs4);
      if (verified) {
        throw new Fault("checkForInvalidServiceSubjects : FAILED");
      }
    }
  }

  /*
   * This is convenience method that helps verify assertion JASPIC:SPEC:87 which
   * states that for the Servlet profile, Calling the getID() method on the
   * ProtectionPolicy must return a valid ID type (as defined in spec section
   * 3.7.4). We look for a message that will be printed out if any calls to
   * getID() returned an invalid ProtectionPolicy ID.
   *
   */
  public void checkForInvalidProtectionPolicyID() throws Fault {
    String strMsg1 = "Layer=HttpServlet  Invalid ProtectionPolicy.getID()";
    String tempArgs[] = { strMsg1 };

    boolean verified = logProcessor.verifyLogContainsOneOf(tempArgs);
    if (verified) {
      throw new Fault("checkForInvalidProtectionPolicyID : FAILED");
    }
  }

  /*
   * This is convenience method that helps verify assertion JASPIC:SPEC:87 which
   * states that for the Servlet profile, Calling getTargetPolicies on the
   * request MessagePolicy must return an array containing at least one
   * TargetPolicy. We look for a message that will be printed out if any calls
   * to getTargetPolicies return an array with less than one TargetPolicy.
   *
   */
  public void checkForInvalidReqPolicy() throws Fault {
    String strMsg1 = "Layer=HttpServlet requestPolicy=invalid";
    strMsg1 += " in TSServerAuthModule.initialize()";
    String tempArgs[] = { strMsg1 };

    boolean verified = logProcessor.verifyLogContainsOneOf(tempArgs);
    if (verified) {
      throw new Fault("checkForInvalidReqPolicy : FAILED");
    }
  }

  /*
   * helper method to help us verify the various AuthStatus return vals.
   *
   */
  private void checkAuthStatus(String sContext, String statusType,
      String requestMethod, boolean isDispatchingToSvc) {

    // add some servlet params onto our context
    sContext = sContext + "?" + "log.file.location=" + logFileLocation;
    sContext = sContext + "&" + "provider.configuration.file="
        + providerConfigFileLoc;
    sContext = sContext + "&" + "vendor.authconfig.factory=" + vendorACF;

    TestUtil.logMsg("sContext = " + sContext);
    TestUtil
        .logMsg("passing to servlet:  log.file.location = " + logFileLocation);
    TestUtil.logMsg("passing to servlet:  provider.configuration.file = "
        + providerConfigFileLoc);
    TestUtil.logMsg(
        "passing to servlet:  vendor.authconfig.factory = " + vendorACF);

    TSURL ctsurl = new TSURL();
    String url = ctsurl.getURLString("http", hostname, portnum, sContext);
    String msg;

    try {
      URL newURL = new URL(url);

      String authData = username + ":" + password;
      TestUtil.logMsg("authData : " + authData);

      BASE64Encoder encoder = new BASE64Encoder();
      String encodedAuthData = encoder.encode(authData.getBytes());
      TestUtil.logMsg("encoded authData : " + encodedAuthData);

      // open URLConnection, set request props and actually connect
      HttpURLConnection conn = (HttpURLConnection) newURL.openConnection();
      conn.setDoOutput(true);
      conn.setDoInput(true);
      conn.setRequestProperty("Authorization",
          "Basic " + encodedAuthData.trim());
      conn.setRequestMethod(requestMethod); // POST or GET etc
      conn.connect();

      TestUtil.logMsg("called HttpURLConnection.connect() for url: " + url);
      int code = conn.getResponseCode();
      TestUtil.logMsg("Got response code of: " + code);
      String responseMsg = conn.getResponseMessage();
      TestUtil.logMsg("Got response string of: " + responseMsg);

      if (responseMsg != null) {
        if (statusType.equals("SEND_FAILURE")) {
          // Spec section 3.8.3.2
          // for SEND_FAILURE, *if* there is a response sent back by the
          // runtime, then the http status code must = 500
          if (code != 500) {
            msg = "SEND_FAILURE returned Response with invalid HTTP status code";
            msg += " returned status code was " + code;
            throw new Fault(msg + " : FAILED");
          } else {
            TestUtil.logMsg("Got correct return status for SEND_FAILURE");
          }
        } else if (statusType.equals("AuthException")) {
          if ((code != 403) && (code != 404) && (code != 500)) {
            msg = "AuthException returned Response with invalid HTTP status code";
            msg += " returned status code was " + code;
            throw new Fault(msg + " : FAILED");
          } else {
            TestUtil.logMsg("Got correct return status for AuthException");
          }
        }
      }

      // this section does checks for spec section 3.8.3.1
      if (isDispatchingToSvc) {
        // this section does checks for spec section 3.8.3.1
        if ((statusType.equals("SEND_CONTINUE")) && (responseMsg != null)) {
          // spec section 3.8.3.1
          // NOTE: our test must return SEND_CONTINUE befor calling the
          // service invocation in order to expect these status codes
          // If response, http status code must be: 401, 303, or 307
          if ((code != 401) || (code != 303) || (code != 307)) {
            msg = "SEND_CONTINUE returned Response with invalid HTTP status code";
            msg += " returned status code was " + code;
            throw new Fault(msg + " : FAILED");
          } else {
            TestUtil.logMsg("Got correct return status for SEND_CONTINUE");
          }
        }
      }

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

    // lets force an update of our log file
    logProcessor.fetchLogs("pullAllLogRecords|fullLog");

    TestUtil.logMsg("Leaving checkAuthStatus()");
  }

  /*
   * Convenience method that will establish a url connections and perform a
   * get/post request. A username and password will be passed in the request
   * header and they will be encoded using the BASE64Encoder class.
   */
  private int invokeServlet(String sContext, String requestMethod) {

    return invokeServlet(sContext, requestMethod, null);
  }

  private int invokeServlet(String sContext, String requestMethod,
      String testMethod) {
    int code = 200;

    TSURL ctsurl = new TSURL();
    if (!sContext.startsWith("/")) {
      sContext = "/" + sContext;
    }

    if (testMethod != null) {
      // lets set some other request params to be passed into servlet calls
      sContext = sContext + "?" + "log.file.location=" + logFileLocation;
      sContext = sContext + "&" + "provider.configuration.file="
          + providerConfigFileLoc;
      sContext = sContext + "&" + "vendor.authconfig.factory=" + vendorACF;
      sContext = sContext + "&" + "method.under.test=" + testMethod;

      TestUtil.logMsg("sContext = " + sContext);
      TestUtil.logMsg(
          "passing to servlet:  log.file.location = " + logFileLocation);
      TestUtil.logMsg("passing to servlet:  provider.configuration.file = "
          + providerConfigFileLoc);
      TestUtil.logMsg(
          "passing to servlet:  vendor.authconfig.factory = " + vendorACF);
      TestUtil.logMsg("passing to servlet:  testMethod = " + testMethod);
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

    // lets force an update of our log file
    logProcessor.fetchLogs("pullAllLogRecords|fullLog");

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
    sContext = sContext + "?" + "log.file.location=" + logFileLocation;
    sContext = sContext + "&" + "provider.configuration.file="
        + providerConfigFileLoc;
    sContext = sContext + "&" + "vendor.authconfig.factory=" + vendorACF;
    if (testMethod != null) {
      sContext = sContext + "&" + "method.under.test=" + testMethod;
    }

    TestUtil.logMsg("sContext = " + sContext);
    TestUtil
        .logMsg("passing to servlet:  log.file.location = " + logFileLocation);
    TestUtil.logMsg("passing to servlet:  provider.configuration.file = "
        + providerConfigFileLoc);
    TestUtil.logMsg(
        "passing to servlet:  vendor.authconfig.factory = " + vendorACF);
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

    // lets force an update of our log file
    logProcessor.fetchLogs("pullAllLogRecords|fullLog");

    return retVal;
  } // invokeServletAndGetResponse()

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: getRegistrationContextId
   *
   * @assertion_ids: JASPIC:JAVADOC:77
   *
   * @test_Strategy: 1. Get System properties log.file.location,
   *                 provider.configuration.file and vendor.authconfig.factory
   *
   *                 2. Use the system properties to read the TestSuite
   *                 providers defined in ProviderConfigruation.xml file and
   *                 register them with vendor's authconfig factory.
   *
   *
   *                 Description This will use an appContext value that was used
   *                 to register a provider, and it will see if it can use the
   *                 AuthConfigFactory.RegistrationContext API to try and access
   *                 the same appContext value that was used during the
   *                 registration process.
   *
   */
  public void getRegistrationContextId() throws Fault {
    boolean verified = false;
    String appContext = appContextHostname + " /spitests_servlet_web";

    // register providers in vendor factory
    verified = register(logFileLocation, providerConfigurationFileLocation,
        vendorAuthConfigFactoryClass);
    if (!verified) {
      throw new Fault("getRegistrationContextId failed : ");
    }

    // verify we can access a given provider (any provider) appcontext id
    boolean bVerified = false;

    AuthConfigFactory acf = null;

    try {
      acf = AuthConfigFactory.getFactory();
    } catch (SecurityException ex) {
      // if here we may not have permission to invoke ACF.getFactory...
      String msg = "SecurityException:  make sure you have permission to call ACF.getFactory.";
      msg = msg + " Check your server side security policies.";
      TestUtil.logMsg(msg);
      ex.printStackTrace();
    }

    if (acf == null) {
      String msg = "getRegistrationContextId():  Error - call to getFactory() returned null ACF.";
      TestUtil.logMsg(msg);
      throw new Fault("getRegistrationContextId failed : ");
    }

    String[] regIDs = acf.getRegistrationIDs(null);
    for (int ii = 0; ii < regIDs.length; ii++) {
      // loop through the ACF's registration ids

      if (regIDs[ii] != null) {
        AuthConfigFactory.RegistrationContext acfReg;
        acfReg = acf.getRegistrationContext(regIDs[ii]);
        if (acfReg != null) {
          TestUtil.logMsg("appContext = " + appContext);
          TestUtil.logMsg("acfReg.getAppContext() = " + acfReg.getAppContext());
          TestUtil.logMsg("layer = " + acfReg.getMessageLayer());
          String str = acfReg.getAppContext();
          if ((str != null) && (str.equals(appContext))) {
            // we found our provider info
            TestUtil
                .logMsg("Found it : RegistrationID for our ACP=" + regIDs[ii]);
            bVerified = true;
            break;
          }
        }
      }
    }

    if (!bVerified) {
      String msg = "Could not find appContext=" + appContext;
      msg += " in the ACF's list of registration id info";
      TestUtil.logMsg(msg);
      throw new Fault("getRegistrationContextId failed : ");
    }

    TestUtil.logMsg("TestSuite Providers registration successful");
  }

  /**
   * @keywords: jaspic_servlet
   *
   * @testName: AuthConfigFactoryVerifyPersistence
   *
   * @assertion_ids: JASPIC:JAVADOC:75
   *
   * @test_Strategy: 1. Get System properties log.file.location,
   *                 provider.configuration.file and vendor.authconfig.factory
   *
   *                 2. Load vendor's AuthConfigFactory and make sure the
   *                 registered providers return properly for the right message
   *                 layer and appContextId
   *
   *                 Note: We test the persistance behaviour for vendor's
   *                 AuthConfigFactory by registering providers from a persisted
   *                 file, then we verify the registrations went correctly.
   *
   *                 Description
   *
   *
   */
  public void AuthConfigFactoryVerifyPersistence() throws Fault {

    boolean verified = false;

    try {

      // register providers in vendor factory
      verified = register(logFileLocation, providerConfigurationFileLocation,
          vendorAuthConfigFactoryClass);
      if (!verified) {
        throw new Fault("getRegistrationContextId failed : ");
      }

      // Get system default AuthConfigFactory
      AuthConfigFactory acf = AuthConfigFactory.getFactory();

      if (acf != null) {
        TestUtil.logMsg("Default AuthConfigFactory class name = "
            + acf.getClass().getName());

        verified = verifyRegistrations(acf);

      } else {
        TestUtil.logErr("Default AuthConfigFactory is null"
            + " can't verify registrations for TestSuite Providers");
      }
    } catch (SecurityException ex) {
      // if here we may not have permission to invoke ACF.getFactory...
      String msg = "SecurityException:  make sure you have permission to call ACF.getFactory.";
      msg = msg + " Check your server side security policies.";
      TestUtil.logMsg(msg);
      ex.printStackTrace();

    } catch (Exception e) {
      e.printStackTrace();
    }

    if (!verified) {
      throw new Fault("AuthConfigFactoryPersistence failed : ");
    } else
      TestUtil.logMsg("AuthConfigFactory Persistence works");
  }

  public boolean register(String logFileLocation,
      String providerConfigurationFileLocation,
      String vendorAuthConfigFactoryClass) {

    try {

      printVerticalIndent();

      // Get an instance of Vendor's AuthConfigFactory
      AuthConfigFactory vFactory = getAuthConfigFactory(
          vendorAuthConfigFactoryClass);

      // Set vendor's AuthConfigFactory
      AuthConfigFactory.setFactory(vFactory);

      // Get system default AuthConfigFactory
      AuthConfigFactory acf = AuthConfigFactory.getFactory();

      if (acf != null) {
        TestUtil.logMsg("Default AuthConfigFactory class name = "
            + acf.getClass().getName());

      } else {
        TestUtil.logErr("Default AuthConfigFactory is null"
            + " can't register TestSuite Providers with null");
        // System.exit(1);
        return false;
      }

      /**
       * Read the ProviderConfiguration XML file This file contains the list of
       * providers that needs to be loaded by the vendor's default
       * AuthConfigFactory
       */
      providerConfigurationEntriesCollection = readProviderConfigurationXMLFile();

      ProviderConfigurationEntry pce = null;

      printVerticalIndent();
      Iterator iterator = providerConfigurationEntriesCollection.iterator();
      while (iterator.hasNext()) {
        // obtain each ProviderConfigurationEntry and register it
        // with vendor's default AuthConfigFactory
        pce = (ProviderConfigurationEntry) iterator.next();

        if (pce != null) {
          TestUtil.logMsg(
              "Registering Provider " + pce.getProviderClassName() + " ...");
          acf.registerConfigProvider(pce.getProviderClassName(),
              pce.getProperties(), pce.getMessageLayer(),
              pce.getApplicationContextId(), pce.getRegistrationDescription());
          TestUtil.logMsg("Registration Successful");
        }
      }
      printVerticalIndent();

      // Check whether the providers are registered for the right message layer
      // and appContext
      // verifyRegistrations(acf);

    } catch (SecurityException ex) {
      // if here we may not have permission to invoke ACF.getFactory...
      String msg = "SecurityException:  make sure you have permission to call ACF.getFactory";
      msg = msg
          + "or ACF.setFactory().  Check your server side security policies.";
      TestUtil.logMsg(msg);
      ex.printStackTrace();

    } catch (Exception e) {
      TestUtil.logMsg("Error Registering TestSuite AuthConfig Providers");
      e.printStackTrace();
    }

    return true;

  }

  /**
   * This method instantiates and ruturns a AuthConfigFactory based on the
   * specified className
   */
  private AuthConfigFactory getAuthConfigFactory(String className) {

    AuthConfigFactory vFactory = null;

    if (className != null) {
      try {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        Class clazz = Class.forName(className, true, loader);

        vFactory = (AuthConfigFactory) clazz.newInstance();
        TestUtil.logMsg("Instantiated AuthConfigFactory for: " + className);

      } catch (Exception e) {
        TestUtil.logMsg("Error instantiating vendor's "
            + "AuthConfigFactory class :" + className);
        e.printStackTrace();

      }
    }

    return vFactory;
  }

  private void printVerticalIndent() {
    TestUtil
        .logMsg("**********************************************************");
    TestUtil.logMsg("\n");

  }

  private boolean verifyRegistrations(AuthConfigFactory acf) {
    TestUtil.logMsg("Verifying Provider Registrations ...");

    try {
      // Get AuthConfigProviderServlet
      AuthConfigProvider servletACP = acf
          .getConfigProvider(JASPICData.LAYER_SERVLET, servletAppContext, null);

      if (servletACP != null) {
        if (servletACP.getClass().getName().equals(
            "com.sun.ts.tests.jaspic.tssv.config.TSAuthConfigProviderServlet")) {
          TestUtil.logMsg("TSAuthConfigProviderServlet registered for"
              + " message layer=HttpServlet" + " and appContextId="
              + servletAppContext);
        } else {
          TestUtil.logMsg(
              "Wrong provider registerd for " + " message layer=HttpServlet"
                  + " and appContextId=" + servletAppContext);
          return false;
        }

      } else {
        TestUtil.logMsg("Error : No AuthConfigprovider registerd for"
            + " message layer=HttpServlet" + " and appContextId="
            + servletAppContext);
        return false;
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return true;

  }

  /*
   * Read the provider configuration XML file and registers each provider with
   * Vendor's default AuthConfigFactory
   */
  private Collection<ProviderConfigurationEntry> readProviderConfigurationXMLFile() {
    Collection<ProviderConfigurationEntry> providerConfigurationEntriesCollection = null;

    TestUtil.logMsg("Reading TestSuite Providers from :"
        + providerConfigurationFileLocation);
    try {
      // Given the provider configuration xml file
      // This reader parses the xml file and stores the configuration
      // entries as a collection.
      configFileProcessor = new ProviderConfigurationXMLFileProcessor(
          providerConfigurationFileLocation);

      // Retrieve the ProviderConfigurationEntries collection
      providerConfigurationEntriesCollection = configFileProcessor
          .getProviderConfigurationEntriesCollection();

      TestUtil.logMsg("TestSuite Providers read successfully "
          + "from ProviderConfiguration.xml file");

      return providerConfigurationEntriesCollection;

    } catch (Exception e) {
      TestUtil.logMsg("Error loading Providers");
      e.printStackTrace();
    }
    return null;

  }

  /*
   * This is used to make sure we have the correct Factory class being used.
   *
   */
  /*
   * private void setFactoryClass(String acfClass) {
   * 
   * try { AuthConfigFactory acf = AuthConfigFactory.getFactory(); Object acfObj
   * = (Object) acf; if (! (acfObj instanceof
   * com.sun.ts.tests.jaspic.tssv.config.TSAuthConfigFactory)) { if (acf!=null){
   * String curClass = acf.getClass().getName();
   * 
   * TestUtil.logMsg("Changing factory to class: " + acfClass + " from class: "
   * + curClass); } AuthConfigFactory correctAcf = new TSAuthConfigFactory();
   * AuthConfigFactory.setFactory(correctAcf); } } catch (SecurityException ex)
   * { // if here we may not have permission to invoke ACF.getFactory... String
   * msg =
   * "SecurityException:  make sure you have permission to call ACF.getFactory";
   * msg = msg +
   * "or ACF.setFactory().  Check your server side security policies.";
   * TestUtil.logMsg(msg); ex.printStackTrace(); } }
   */

  /**
   *
   * @keywords: jaspic_servlet
   *
   * @testName: AuthConfigFactorySetFactory
   *
   * @assertion_ids: JASPIC:SPEC:7; JASPIC:SPEC:10; JASPIC:JAVADOC:87;
   *                 JASPIC:JAVADOC:80
   *
   * @test_Strategy: 1. Use the static setFactory method to set an ACF and this
   *                 should always work.
   *
   *                 Description This method uses the getFactory() method to get
   *                 the current factory, then it uses the setFactory() to
   *                 change the current ACF. This method then verifies that the
   *                 ACF's were indeed changed. We know that the setFactory()
   *                 works as it is used in the bootstrap process - but this is
   *                 an additional level of testing that allows us to set the
   *                 factory in a slightly different manner than the bootstrap
   *                 (eg reading it out of the java.security file.
   */
  public void AuthConfigFactorySetFactory() throws Fault {
    try {
      // get current AuthConfigFactory
      AuthConfigFactory currentAcf = AuthConfigFactory.getFactory();
      if (currentAcf == null) {
        // this could fail due to permissions or because the system can't
        // find ACF (check for setting of -Djava.security.properties)
        TestUtil.logMsg("FAILURE - Could not get current AuthConfigFactory.");
        throw new Fault("AuthConfigFactorySetFactory : FAILED");
      }
      String currentACFClass = currentAcf.getClass().getName();
      TestUtil.logMsg("currentACFClass = " + currentACFClass);

      // set our ACF to a new/different AuthConfigFactory
      TSAuthConfigFactoryForStandalone newAcf = new TSAuthConfigFactoryForStandalone();
      AuthConfigFactory.setFactory(newAcf);
      String newACFClass = newAcf.getClass().getName();
      TestUtil.logMsg("newACFClass = " + newACFClass);

      // verify our calls to getFactory() are getting the newly set factory
      // instance and not the original ACF instance
      AuthConfigFactory testAcf = AuthConfigFactory.getFactory();
      if (testAcf == null) {
        TestUtil.logMsg("FAILURE - Could not get newly set AuthConfigFactory.");
        throw new Fault("AuthConfigFactorySetFactory : FAILED");
      }
      String newlySetACFClass = testAcf.getClass().getName();
      TestUtil.logMsg("newlySetACFClass = " + newlySetACFClass);

      // Verify ACF's were set correctly
      if (!newlySetACFClass.equals(newACFClass)) {
        TestUtil.logMsg(
            "FAILURE - our current ACF does not match our newly set ACF");
        throw new Fault("AuthConfigFactorySetFactory : FAILED");
      } else {
        String msg = "newlySetACFClass == newACFClass == " + newACFClass;
        TestUtil.logMsg(msg);
      }

      // restore original factory class
      AuthConfigFactory.setFactory(currentAcf);

      TestUtil.logMsg("AuthConfigFactorySetFactory : PASSED");
    } catch (SecurityException ex) {
      // if here we may not have permission to invoke ACF.getFactory...
      String msg = "SecurityException:  make sure you have permission to call ACF.getFactory";
      msg = msg
          + "or ACF.setFactory().  Check your server side security policies.";
      TestUtil.logMsg(msg);
      ex.printStackTrace();
    }
  }

  /**
   *
   * @keywords: jaspic_servlet
   *
   * @testName: ACFSwitchFactorys
   *
   * @assertion_ids:
   * 
   *
   * @test_Strategy: This test does the following: - gets current (CTS) factory
   *                 - sets the vendors ACF thus replacing the CTS ACF - verify
   *                 ACF's were correctly set - reset factory back to the
   *                 original CTS factory
   * 
   *                 1. Use the static setFactory method to set an ACF and this
   *                 should always work. and use the getFactory to verify it
   *                 worked.
   *
   *                 Description
   *
   */
  public void ACFSwitchFactorys() throws Fault {
    String strMsg1 = "ACFTestServlet->ACFSwitchFactorys() passed";

    String str = invokeServletAndGetResponse(acfServletPath, "POST",
        "ACFSwitchFactorys");
    TestUtil.logMsg("ACFSwitchFactorys : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("ACFSwitchFactorys : FAILED");
    }

    TestUtil.logMsg("ACFSwitchFactorys : PASSED");
  }

  /**
   *
   * @keywords: jaspic_servlet
   *
   * @testName: ACFRemoveRegistrationWithBadId
   *
   * @assertion_ids: JASPIC:SPEC:345;
   * 
   *
   * @test_Strategy: This test verifies we get a return value of False when
   *                 invoking ACF.removeRegistration(some_bad_id);
   * 
   *                 1. Use the static setFactory method to get an ACF and then
   *                 attempt to invoke removeRegistration() with an invalid (ie
   *                 non-existant) regId. This should return False (per
   *                 javadoc).
   *
   *                 Description
   *
   */
  public void ACFRemoveRegistrationWithBadId() throws Fault {
    String strMsg1 = "ACFTestServlet->ACFRemoveRegistrationWithBadId() passed";

    String str = invokeServletAndGetResponse(acfServletPath, "POST",
        "ACFRemoveRegistrationWithBadId");
    TestUtil.logMsg("ACFRemoveRegistrationWithBadId : str = " + str);

    int ii = str.indexOf(strMsg1);

    if (ii < 0) {
      throw new Fault("ACFRemoveRegistrationWithBadId : FAILED");
    }

    TestUtil.logMsg("ACFRemoveRegistrationWithBadId : PASSED");
  }

}
