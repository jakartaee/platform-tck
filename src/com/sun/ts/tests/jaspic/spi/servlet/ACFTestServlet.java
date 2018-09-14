/*
 * Copyright (c) 2011, 2018 Oracle and/or its affiliates. All rights reserved.
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

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.*;

import com.sun.ts.tests.jaspic.tssv.util.*;
import com.sun.ts.tests.jaspic.spi.common.*;

import javax.security.auth.message.config.AuthConfigFactory;
import javax.security.auth.message.config.AuthConfigProvider;

import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.HttpMethodConstraint;
import javax.servlet.annotation.HttpConstraint;
import javax.annotation.security.DeclareRoles;

@DeclareRoles({ "Administrator", "Manager", "Employee" })
@ServletSecurity(value = @HttpConstraint(rolesAllowed = {
    "Administrator" }), httpMethodConstraints = {
        @HttpMethodConstraint(value = "GET", rolesAllowed = "Administrator"),
        @HttpMethodConstraint(value = "POST", rolesAllowed = "Administrator") })
@WebServlet(name = "ACFTestServlet", urlPatterns = { "/ACFTestServlet" })
public class ACFTestServlet extends HttpServlet {
  /**
  * 
  */
  private static final long serialVersionUID = 1L;

  private String logFileLocation;

  private String servletAppContext = null;

  private String providerConfigFileLocation;

  private String vendorACFClass;

  private String testMethod = null;

  private transient CommonTests commonTests;

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doPost(request, response);
  }

  @SuppressWarnings("unused")
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doTests(request, response);
  }

  public void doTests(HttpServletRequest request,
      HttpServletResponse response) {

    PrintWriter out = null;
    try {
      out = response.getWriter();
    } catch (Exception ex) {
      debug("got exception in ACFTestServlet");
      ex.printStackTrace();
    }
    commonTests = new CommonTests(out);

    // get some common props
    getPropsAndParams(request, response);

    if (testMethod == null) {
      getRegistrationContextId(request, response);
      AuthConfigFactoryVerifyPersistence(request, response);

    } else if (testMethod.equals("ACFRemoveRegistration")) {
      ACFRemoveRegistration(request, response);

    } else if (testMethod.equals("ACFGetRegistrationIDs")) {
      ACFGetRegistrationIDs(request, response);

    } else if (testMethod.equals("ACFGetRegistrationContext")) {
      ACFGetRegistrationContext(request, response);

    } else if (testMethod.equals("ACFDetachListener")) {
      ACFDetachListener(request, response);

    } else if (testMethod.equals("ACFGetFactory")) {
      ACFGetFactory(request, response);

    } else if (testMethod.equals("ACFSwitchFactorys")) {
      ACFSwitchFactorys(request, response);

    } else if (testMethod.equals("testACFComesFromSecFile")) {
      testACFComesFromSecFile(request, response);

    } else if (testMethod.equals("ACFPersistentRegisterOnlyOneACP")) {
      ACFPersistentRegisterOnlyOneACP(request, response);

    } else if (testMethod.equals("ACFInMemoryRegisterOnlyOneACP")) {
      ACFInMemoryRegisterOnlyOneACP(request, response);

    } else if (testMethod.equals("ACFUnregisterACP")) {
      ACFUnregisterACP(request, response);

    } else if (testMethod.equals("AuthConfigFactoryRegistration")) {
      AuthConfigFactoryRegistration(request, response);

    } else if (testMethod.equals("ACFInMemoryNotifyOnUnReg")) {
      ACFInMemoryNotifyOnUnReg(request, response);

    } else if (testMethod.equals("ACFPersistentNotifyOnUnReg")) {
      ACFPersistentNotifyOnUnReg(request, response);

    } else if (testMethod.equals("ACFInMemoryPrecedenceRules")) {
      ACFInMemoryPrecedenceRules(request, response);

    } else if (testMethod.equals("ACFPersistentPrecedenceRules")) {
      ACFPersistentPrecedenceRules(request, response);

    } else if (testMethod.equals("ACFRemoveRegistrationWithBadId")) {
      ACFRemoveRegistrationWithBadId(request, response);
    }

    // restore original (CTS) factory class
    // note: this should be done in many of the calls but its a safety measure
    // to ensure we are resetting things back to expected defaults
    try {
      CommonUtils.resetDefaultACF();
    } catch (Exception ex) {
      debug("ACFTestServlet:  error calling CommonUtils.resetDefaultACF(): "
          + ex.getMessage());
      ex.printStackTrace();
    }
  }

  private void getPropsAndParams(HttpServletRequest req,
      HttpServletResponse response) {

    // set logfile location
    logFileLocation = req.getParameter("log.file.location");
    if ((logFileLocation != null)
        && (-1 < logFileLocation.indexOf(JASPICData.DEFAULT_LOG_FILE))) {
      // if here, we have logfile location value which contains
      // JASPICData.DEFAULT_LOG_FILE
      debug("logFileLocation already set");
    } else {
      debug("logFileLocation NOT set completely");
      System.setProperty("log.file.location", logFileLocation);
    }
    debug("logFileLocation = " + logFileLocation);

    // set provider config file
    providerConfigFileLocation = req
        .getParameter("provider.configuration.file");
    debug("TS Provider ConfigFile = " + providerConfigFileLocation);
    if (providerConfigFileLocation == null) {
      debug("ERROR:  getPropsAndParams(): providerConfigFileLocation = null");
    } else {
      debug("getPropsAndParams(): providerConfigFileLocation = "
          + providerConfigFileLocation);
    }

    // set testMethod
    testMethod = req.getParameter("method.under.test");

    // set vendor class
    vendorACFClass = req.getParameter("vendor.authconfig.factory");
    if (vendorACFClass == null) {
      debug("ERROR:  getPropsAndParams(): vendorACFClass = null");
    } else {
      debug("getPropsAndParams(): vendorACFClass = " + vendorACFClass);
    }

    servletAppContext = IdUtil.getAppContextId(JASPICData.LAYER_SERVLET);

    return;
  }

  /*
   * This is testing that acf.removeRegistration(arg) will return FALSE when
   * invalid arg supplied. (this requirement described in javadoc for api)
   */
  public void ACFRemoveRegistration(HttpServletRequest request,
      HttpServletResponse response) {
    PrintWriter out = null;
    try {
      System.out.println("in ACFTestServlet.ACFRemoveRegistration");
      commonTests._ACF_testFactoryRemoveRegistration();
      System.out.println(
          "in ACFTestServlet ... just called commonTests._ACF_testFactoryRemoveRegistration");
      out = response.getWriter();
      if (out != null) {
        System.out.println("in ACFTestServlet ... out != null so passed");
        out.println("ACFTestServlet->ACFRemoveRegistration() passed");
        out.flush();
      } else {
        System.out.println("in ACFTestServlet ... out == null so failed");
      }
    } catch (Exception ex) {
      System.out.println("ACFTestServlet->ACFRemoveRegistration() failed");
      ex.printStackTrace();
    }
  }

  /*
   * This is testing that acf.getRegistrationIDs(acp) NEVER returns null hint:
   * this must return empty array even if unrecognized acp. (this requirement
   * described in javadoc for api)
   */
  public void ACFGetRegistrationIDs(HttpServletRequest request,
      HttpServletResponse response) {
    PrintWriter out = null;
    try {
      commonTests._ACF_testFactoryGetRegistrationIDs();
      out = response.getWriter();
      if (out != null) {
        out.println("ACFTestServlet->ACFGetRegistrationIDs() passed");
        out.flush();
      }
    } catch (Exception ex) {
      System.out.println("ACFTestServlet->ACFGetRegistrationIDs() failed");
      ex.printStackTrace();
    }
  }

  /*
   * This is testing that acf.getRegistrationContext(string) returns NULL for an
   * unrecognized string (this requirement described in javadoc for api)
   */
  public void ACFGetRegistrationContext(HttpServletRequest request,
      HttpServletResponse response) {
    PrintWriter out = null;
    try {
      commonTests._ACF_testFactoryGetRegistrationContext();
      out = response.getWriter();
      if (out != null) {
        out.println("ACFTestServlet->ACFGetRegistrationContext() passed");
        out.flush();
      }
    } catch (Exception ex) {
      System.out.println("ACFTestServlet->ACFGetRegistrationContext() failed");
      ex.printStackTrace();
    }
  }

  /*
   * This is testing that acf.detachListener(...) returns non-NULL for an
   * unfound listner (this requirement described in javadoc for api)
   */
  public void ACFDetachListener(HttpServletRequest request,
      HttpServletResponse response) {
    PrintWriter out = null;
    try {
      commonTests._ACF_testFactoryDetachListener(vendorACFClass);
      out = response.getWriter();
      if (out != null) {
        out.println("ACFTestServlet->ACFDetachListener() passed");
        out.flush();
      }
    } catch (Exception ex) {
      System.out.println("ACFTestServlet->ACFDetachListener() failed");
      ex.printStackTrace();
    }
  }

  public void ACFGetFactory(HttpServletRequest request,
      HttpServletResponse response) {
    PrintWriter out = null;
    try {
      commonTests._ACF_getFactory();
      out = response.getWriter();
      if (out != null) {
        out.println("ACFTestServlet->ACFGetFactory() passed");
        out.flush();
      }
    } catch (Exception ex) {
      System.out.println("ACFTestServlet->ACF_getFactory() failed");
      ex.printStackTrace();
    }
  }

  public void ACFSwitchFactorys(HttpServletRequest request,
      HttpServletResponse response) {
    PrintWriter out = null;
    try {
      out = response.getWriter();
      commonTests._ACFSwitchFactorys(vendorACFClass);
      if (out != null) {
        out.println("ACFTestServlet->ACFSwitchFactorys() passed");
        out.flush();
      }
    } catch (Exception ex) {
      System.out.println("ACFTestServlet->ACFSwitchFactorys() failed");
      ex.printStackTrace();
    }
  }

  public void testACFComesFromSecFile(HttpServletRequest request,
      HttpServletResponse response) {
    PrintWriter out = null;
    try {
      out = response.getWriter();
      commonTests._testACFComesFromSecFile();
      out.println("ACFTestServlet->testACFComesFromSecFile() passed");
      out.flush();
    } catch (Exception ex) {
      System.out.println("ACFTestServlet->testACFComesFromSecFile() failed");
      ex.printStackTrace();
    }
  }

  public void ACFPersistentRegisterOnlyOneACP(HttpServletRequest request,
      HttpServletResponse response) {
    PrintWriter out = null;
    try {
      out = response.getWriter();
      commonTests._ACFRegisterOnlyOneACP(logFileLocation,
          providerConfigFileLocation, vendorACFClass, true);
      out.println("ACFTestServlet->ACFPersistentRegisterOnlyOneACP() passed");
      out.flush();
    } catch (Exception ex) {
      System.out
          .println("ACFTestServlet->ACFPersistentRegisterOnlyOneACP() failed");
      ex.printStackTrace();
    }
  }

  public void ACFInMemoryRegisterOnlyOneACP(HttpServletRequest request,
      HttpServletResponse response) {
    PrintWriter out = null;
    try {
      out = response.getWriter();
      commonTests._ACFRegisterOnlyOneACP(logFileLocation,
          providerConfigFileLocation, vendorACFClass, false);
      out.println("ACFTestServlet->ACFInMemoryRegisterOnlyOneACP() passed");
      out.flush();
    } catch (Exception ex) {
      System.out
          .println("ACFTestServlet->ACFInMemoryRegisterOnlyOneACP() failed");
      ex.printStackTrace();
    }
  }

  public void ACFUnregisterACP(HttpServletRequest request,
      HttpServletResponse response) {
    PrintWriter out = null;
    try {
      out = response.getWriter();
      commonTests._ACFUnregisterACP(logFileLocation, providerConfigFileLocation,
          vendorACFClass);
      out.println("ACFTestServlet->ACFUnregisterACP() passed");
      out.flush();
    } catch (Exception ex) {
      System.out.println("ACFTestServlet->ACFUnregisterACP() failed");
      ex.printStackTrace();
    }
  }

  /*
   * This is verifying that the invocation of removeRegistration() with an
   * invalid and non-existant regId will return false.
   */
  public void ACFRemoveRegistrationWithBadId(HttpServletRequest request,
      HttpServletResponse response) {
    PrintWriter out = null;
    try {
      out = response.getWriter();
      commonTests._ACFRemoveRegistrationWithBadId();
      out.println("ACFTestServlet->ACFRemoveRegistrationWithBadId() passed");
      out.flush();
    } catch (Exception ex) {
      System.out
          .println("ACFTestServlet->ACFRemoveRegistrationWithBadId() failed");
      ex.printStackTrace();
    }
  }

  /**
   * 1. Get System properties log.file.location, provider.configuration.file and
   * vendor.authconfig.factory
   *
   * 2. Use the system properties to read the TestSuite providers defined in
   * ProviderConfigruation.xml file and register them with vendor's authconfig
   * factory.
   */
  public void AuthConfigFactoryRegistration(HttpServletRequest request,
      HttpServletResponse response) {

    PrintWriter out = null;
    try {
      out = response.getWriter();
      AuthConfigFactory registerACF = CommonUtils.register(logFileLocation,
          providerConfigFileLocation, vendorACFClass);

      if (registerACF != null) {
        out.println("ACFTestServlet->AuthConfigFactoryRegistration() passed");
      } else {
        out.println("ACFTestServlet->AuthConfigFactoryRegistration() failed");
      }
      out.flush();
    } catch (Exception ex) {
      System.out
          .println("ACFTestServlet->AuthConfigFactoryRegistration() failed");
      ex.printStackTrace();
    }
  }

  /**
   *          
   */
  public void ACFInMemoryNotifyOnUnReg(HttpServletRequest request,
      HttpServletResponse response) {

    PrintWriter out = null;
    try {
      out = response.getWriter();

      // test using in-memory registration
      commonTests._ACFTestNotifyOnUnReg(vendorACFClass, false);

      out.println("ACFTestServlet->ACFInMemoryNotifyOnUnReg() passed");
      out.flush();
    } catch (Exception ex) {
      System.out.println("ACFTestServlet->ACFInMemoryNotifyOnUnReg() failed");
      ex.printStackTrace();
    }
  }

  /**
   *          
   */
  public void ACFPersistentNotifyOnUnReg(HttpServletRequest request,
      HttpServletResponse response) {

    PrintWriter out = null;
    try {
      out = response.getWriter();

      // now test using persistent registration
      commonTests._ACFTestNotifyOnUnReg(vendorACFClass, true);

      out.println("ACFTestServlet->ACFPersistentNotifyOnUnReg() passed");
      out.flush();
    } catch (Exception ex) {
      System.out.println("ACFTestServlet->ACFPersistentNotifyOnUnReg() failed");
      ex.printStackTrace();
    }
  }

  /**
   *          
   */
  public void ACFInMemoryPrecedenceRules(HttpServletRequest request,
      HttpServletResponse response) {

    PrintWriter out = null;
    try {
      out = response.getWriter();

      // test using in-memory registration
      commonTests._ACFTestPrecedenceRules(vendorACFClass, false);

      out.println("ACFTestServlet->ACFInMemoryPrecedenceRules() passed");
      out.flush();
    } catch (Exception ex) {
      System.out.println("ACFTestServlet->ACFInMemoryPrecedenceRules() failed");
      ex.printStackTrace();
    }
  }

  /**
   *          
   */
  public void ACFPersistentPrecedenceRules(HttpServletRequest request,
      HttpServletResponse response) {

    PrintWriter out = null;
    try {
      out = response.getWriter();

      // now test using persistent registration
      commonTests._ACFTestPrecedenceRules(vendorACFClass, true);

      out.println("ACFTestServlet->ACFPersistentPrecedenceRules() passed");
      out.flush();
    } catch (Exception ex) {
      System.out
          .println("ACFTestServlet->ACFPersistentPrecedenceRules() failed");
      ex.printStackTrace();
    }
  }

  /**
   *
   * 1. Get System properties log.file.location, provider.configuration.file and
   * vendor.authconfig.factory
   *
   * 2. Use the system properties to read the TestSuite providers defined in
   * ProviderConfigruation.xml file and register them with vendor's authconfig
   * factory.
   *
   *
   * Description This will use an appContext value that was used to register a
   * provider, and it will see if it can use the
   * AuthConfigFactory.RegistrationContext API to try and access the same
   * appContext value that was used during the registration process.
   *
   */
  public void getRegistrationContextId(HttpServletRequest request,
      HttpServletResponse response) {
    String appContext = "localhost /Hello_web/Hello";

    PrintWriter out = null;
    try {
      out = response.getWriter();

      // register providers in vendor factory
      AuthConfigFactory registeredACF = CommonUtils.register(logFileLocation,
          providerConfigFileLocation, vendorACFClass);
      if (registeredACF == null) {
        out.println("getRegistrationContextId failed");
      }

      // verify we can access a given provider (any provider) appcontext id
      boolean bVerified = false;
      AuthConfigFactory acf = AuthConfigFactory.getFactory();
      String[] regIDs = acf.getRegistrationIDs(null);
      for (int ii = 0; ii < regIDs.length; ii++) {
        // loop through the ACF's registration ids

        if (regIDs[ii] != null) {
          AuthConfigFactory.RegistrationContext acfReg;
          acfReg = acf.getRegistrationContext(regIDs[ii]);
          if (acfReg != null) {
            debug("appContext = " + appContext);
            debug("acfReg.getAppContext() = " + acfReg.getAppContext());
            debug("layer = " + acfReg.getMessageLayer());
            String str = acfReg.getAppContext();
            if ((str != null) && (str.equals(appContext))) {
              // we found our provider info
              debug("Found it : RegistrationID for our ACP=" + regIDs[ii]);
              bVerified = true;
              break;
            }
          }
        }
      }

      if (!bVerified) {
        String msg = "Could not find appContext=" + appContext;
        msg += " in the ACF's list of registration id info";
        debug(msg);
        out.println(
            "getRegistrationContextId() TSProviders registration failed");
      }

      out.println("getRegistrationContextId() TSProviders registration passed");
    } catch (SecurityException ex) {
      // if here we may not have permission to invoke ACF.getFactory...
      String msg = "SecurityException:  make sure you have permission to call ACF.getFactory() or ";
      msg = msg
          + "ACF.setFactory().  You may need to explicitly set your server side security policies.";
      if (out == null) {
        System.out.println(msg);
      } else {
        out.println(msg);
      }
      ex.printStackTrace();
    } catch (Exception ex) {
      System.out.println(
          "getRegistrationContextId() TSProviders registration failed");
      ex.printStackTrace();
    }
  }

  /**
   *
   * 1. Get System properties log.file.location, provider.configuration.file and
   * vendor.authconfig.factory
   *
   * 2. Load vendor's AuthConfigFactory and make sure the registered providers
   * return properly for the right message layer and appContextId
   *
   * Note: We test the persistance behaviour for vendor's AuthConfigFactory by
   * registering providers from a persisted file, then we verify the
   * registrations went correctly.
   *
   */
  public void AuthConfigFactoryVerifyPersistence(HttpServletRequest request,
      HttpServletResponse response) {
    boolean verified = false;

    PrintWriter out = null;
    try {
      out = response.getWriter();

      // register providers in vendor factory
      AuthConfigFactory registerACF = CommonUtils.register(logFileLocation,
          providerConfigFileLocation, vendorACFClass);
      if (registerACF != null) {
        out.println("AuthConfigFactoryVerifyPersistence failed");
      }

      // Get system default AuthConfigFactory
      AuthConfigFactory acf = AuthConfigFactory.getFactory();

      if (acf != null) {
        debug("Default ACF class name = " + acf.getClass().getName());
        verified = verifyRegistrations(acf);
      } else {
        out.println("AuthConfigFactoryVerifyPersistence() failed");
        debug("Default ACF is null"
            + " can't verify registrations for TestSuite Providers");
      }
    } catch (SecurityException ex) {
      // if here we may not have permission to invoke ACF.getFactory...
      String msg = "SecurityException:  make sure you have permission to call ACF.getFactory.";
      msg = msg
          + "  You may need to explicitly set your server side security policies.";
      debug(msg);
      ex.printStackTrace();

    } catch (Exception e) {
      System.out.println("AuthConfigFactoryVerifyPersistence() failed");
      e.printStackTrace();
    }

    if ((verified) && (out != null)) {
      out.println("AuthConfigFactoryVerifyPersistence() passed");
    }
  }

  private boolean verifyRegistrations(AuthConfigFactory acf) {
    debug("Verifying Provider Registrations ...");

    try {
      // Get AuthConfigProviderServlet
      AuthConfigProvider servletACP = acf
          .getConfigProvider(JASPICData.LAYER_SERVLET, servletAppContext, null);

      if (servletACP != null) {
        if (servletACP.getClass().getName().equals(
            "com.sun.ts.tests.jaspic.tssv.config.TSAuthConfigProviderServlet")) {
          debug("TSAuthConfigProviderServlet registered for"
              + " message layer=HttpServlet" + " and appContextId="
              + servletAppContext);
        } else {
          debug("Wrong provider registerd for " + " message layer=HttpServlet"
              + " and appContextId=" + servletAppContext);
          return false;
        }

      } else {
        debug("Error : No AuthConfigprovider registerd for"
            + " message layer=HttpServlet" + " and appContextId="
            + servletAppContext);
        return false;
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return true;
  }

  public void debug(String str) {
    System.out.println(str);
  }

}
