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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;

import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.HttpMethodConstraint;
import javax.servlet.annotation.HttpConstraint;
import javax.annotation.security.DeclareRoles;

import com.sun.ts.tests.jaspic.tssv.util.JASPICData;
import com.sun.ts.tests.jaspic.tssv.util.IdUtil;

@DeclareRoles({ "Administrator", "Manager", "Employee" })
@ServletSecurity(value = @HttpConstraint(rolesAllowed = {
    "Administrator" }), httpMethodConstraints = {
        @HttpMethodConstraint(value = "GET", rolesAllowed = "Administrator"),
        @HttpMethodConstraint(value = "POST", rolesAllowed = "Administrator") })
@WebServlet(name = "ModTestServlet", urlPatterns = { "/ModTestServlet" })
public class ModTestServlet extends HttpServlet {

  private String logFileLocation;

  private String servletAppContext = null;

  private String providerConfigFileLocation;

  private String testMethod = null;

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    out.println("Enterred ModTestServlet->doGet()");

    doTests(request, response);

    out.println("Laving ModTestServlet->doGet()");
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    System.out.println("In ModTestServlet->doPost()");

    doTests(request, response);

    PrintWriter out = response.getWriter();
    out.println("Enterred ModTestServlet->doPost()");

  }

  public void service(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    System.out.println("In ModTestServlet->service()");
    PrintWriter out = response.getWriter();
    out.println("Enterred ModTestServlet->service()");

    super.service(request, response);
  }

  public void doTests(HttpServletRequest request,
      HttpServletResponse response) {

    PrintWriter out = null;
    try {
      out = response.getWriter();
    } catch (Exception ex) {
      debug("got exception in ModTestServlet");
      ex.printStackTrace();
    }

    // get some common props that are passed into our servlet request
    getPropsAndParams(request, response);

    if (testMethod.equals("testAuthenResultsOnHttpServlet")) {
      MTSAuthenResultsOnHttpServlet(request, response);
    } else if (testMethod.equals("testAuthenIsUserInRole")) {
      MTSAuthenIsUserInRole(request, response);
    } else if (testMethod.equals("testRemoteUserCorrespondsToPrin")) {
      MTSRemoteUserCorrespondsToPrin(request, response);
    } else if (testMethod.equals("testAuthenAfterLogout")) {
      testAuthenAfterLogout(request, response);
    } else if (testMethod.equals("testGPCIsUserInRole")) {
      testGPCIsUserInRole(request, response);
    } else if (testMethod.equals("testGPCGetUserPrincipal")) {
      testGPCGetUserPrincipal(request, response);
    } else if (testMethod.equals("testGPCGetRemoteUser")) {
      testGPCGetRemoteUser(request, response);
    } else if (testMethod.equals("testGPCGetAuthType")) {
      testGPCGetAuthType(request, response);
    } else {
      // if here, it most likely means that we ran a test from within
      // Client.java which
      // invoked this servlet with the only intent being to check if access
      // could be
      // made. Some tests only care that they can access this servlet and do NOT
      // need to run any particular testMethod in here.
      debug("WARNING:  ModTestServlet,doTests()  testMethod not recognized: "
          + testMethod);
    }
  }

  // this pulls out some params that are passed in on our servlet req
  // that is made from within the client code (e.g. Client.java).
  private void getPropsAndParams(HttpServletRequest req,
      HttpServletResponse response) {

    // set logfile location
    logFileLocation = req.getParameter("log.file.location");
    if ((logFileLocation != null)
        && (-1 < logFileLocation.indexOf(JASPICData.DEFAULT_LOG_FILE))) {
      // if here, we have logfile location value which contains
      // JASPICData.DEFAULT_LOG_FILE
      debug("logFileLocation already set");
    } else if (logFileLocation != null) {
      debug("logFileLocation NOT set completely");
      System.setProperty("log.file.location", logFileLocation);
    } else {
      debug("ModTestServlet: logFileLocation null");
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

    // Reusing some old code that is passing vendorACFClass in - so while
    // we dont currently use it in this servlet, - it IS being passed in
    // so just note it is here.
    // String vendorACFClass = req.getParameter("vendor.authconfig.factory");

    servletAppContext = IdUtil.getAppContextId(JASPICData.LAYER_SERVLET);

    return;
  }

  /*
   * This should be satisfying assertion JASPIC:SPEC:322.
   */
  public void MTSAuthenResultsOnHttpServlet(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      PrintWriter out = response.getWriter();
      out.println("Enterred ModTestServlet->MTSAuthenResultsOnHttpServlet()");
      System.out.println("In ModTestServlet->MTSAuthenResultsOnHttpServlet()");

      // after successful login, we should see non-null values for all of these!
      String strAuthType = request.getAuthType();
      String strRemoteUser = request.getRemoteUser();
      Principal pp = request.getUserPrincipal();

      if ((strAuthType != null) && (strRemoteUser != null) && (pp != null)) {
        out.println("request.getAuthType() = " + strAuthType);
        out.println("request.getRemoteUser() = " + strRemoteUser);
        out.println("request.getUserPrincipal() = " + pp.toString());
        out.println("Authentication results on HttpServletRequest ok.");
        out.println("ModTestServlet->testAuthenResultsOnHttpServlet() passed");
      } else if ((strRemoteUser == null) && (pp == null)
          && (strAuthType == null)) {
        out.println("request.getRemoteUser() == pp == null");
        out.println("ModTestServlet->testAuthenResultsOnHttpServlet() passed");
      } else {
        out.println("Authentication results on HttpServletRequest Error.");
        out.println("request.getAuthType() = " + strAuthType);
        out.println("request.getRemoteUser() = " + strRemoteUser);
        if (pp != null) {
          out.println("request.getUserPrincipal() = " + pp.toString());
        } else {
          out.println("request.getUserPrincipal() = null");
        }
      }
    } catch (Exception ex) {
      System.out
          .println("ModTestServlet->MTSAuthenResultsOnHttpServlet() failed");
      ex.printStackTrace();
    }

    return;
  } // MTSAuthenResultsOnHttpServlet()

  /*
   * This is basically the same thing as MTSAuthenResultsOnHttpServlet() but we
   * are returning slightly different return values so...
   */
  public void testAuthenAfterLogout(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      PrintWriter out = response.getWriter();
      out.println("Enterred ModTestServlet->testAuthenAfterLogout()");
      debug("In ModTestServlet->testAuthenAfterLogout()");

      // after successful login, we must get a non-null
      // value for getUserPrincipal()
      Principal pp = request.getUserPrincipal();

      if (pp != null) {
        // good - pp was not null which indicates successful authN
        out.println("ModTestServlet->testAuthenAfterLogout() passed");
      } else {
        // oh oh - we got null pp which indicates we are not authenticated
        out.println(
            "testAuthenAfterLogout() failed - getUserPrincipal() returned null.");
      }
    } catch (Exception ex) {
      debug("ModTestServlet->testAuthenAfterLogout() failed");
      ex.printStackTrace();
    }

    return;
  } // testAuthenAfterLogout()

  /*
   * This will get called after we have authenticated with our user j2ee in role
   * Administrator. When we get into this test method, a call to isUserInRole
   * better return true. if we have not authenticated, we expect to get false.
   */
  public void MTSAuthenIsUserInRole(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      PrintWriter out = response.getWriter();
      out.println("Enterred ModTestServlet->MTSAuthenIsUserInRole()");
      System.out.println("In ModTestServlet->MTSAuthenIsUserInRole()");

      // after successful login, we should see true value for isUserInRole
      boolean bval = request.isUserInRole("Administrator");

      if (bval == true) {
        debug(out, "isUserInRole() returns true.");
        out.println("ModTestServlet->testAuthenIsUserInRole() passed");
      } else {
        debug(out, "Authentication Error - isUserInRole() returns false.");
        debug(out, "request.getRemoteUser() = " + request.getRemoteUser());
        Principal pp = request.getUserPrincipal();
        if (pp != null) {
          debug(out, "request.getUserPrincipal().getName() = " + pp.getName());
        } else {
          debug(out, "request.getUserPrincipal() == null");
        }
        out.println("ModTestServlet->MTSAuthenIsUserInRole() failed");
      }
    } catch (Exception ex) {
      System.out.println("ModTestServlet->MTSAuthenIsUserInRole() failed");
      ex.printStackTrace();
    }

    return;
  } // MTSAuthenIsUserInRole()

  /*
   * (Per jsr-196 spec v1.1 section 3.8.4) This will get called after we have
   * authenticated with our user j2ee in role Administrator. When we get into
   * this test method, a call to getUserPrincipal(), getRemoteUser(), and to
   * getUserPrincipal().getName() should all CORRESPOND. "Correspond" is not a
   * hard term as it implies there could be some user mappings involved.
   * 
   * (Using Servlet spec v3.1, section 13.3 - it states:
   * "The getRemoteUser method returns the name of the remote user" AND "Calling
   * the getName method on the Principal returned by getUserPrincipal returns
   * the name of the remote user." We can use these two Servlet spec references
   * combined with our JASPIC reference to validate that principal.getName()
   * equals the same value returned by getRemoteUser().
   */
  public void MTSRemoteUserCorrespondsToPrin(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      PrintWriter out = response.getWriter();
      out.println("Enterred ModTestServlet->MTSRemoteUserCorrespondsToPrin()");
      System.out.println("In ModTestServlet->MTSRemoteUserCorrespondsToPrin()");

      // after successful login, we should see true value for isUserInRole
      String strRemoteUser = request.getRemoteUser();
      Principal pp = request.getUserPrincipal();
      String prinName = null;
      if (pp != null) {
        prinName = pp.getName();
      }

      if ((prinName != null) && (strRemoteUser != null)
          && prinName.equals(strRemoteUser)) {
        out.println("principal.getName() = " + prinName);
        out.println("request.getRemoteUser() = " + strRemoteUser);
        out.println("ModTestServlet->testRemoteUserCorrespondsToPrin() passed");
      } else {
        out.println("ModTestServlet->MTSRemoteUserCorrespondsToPrin() failed");
        out.println("principal.getName() = " + prinName);
        out.println("request.getRemoteUser() = " + strRemoteUser);
        if (pp != null) {
          out.println(
              "request.getUserPrincipal().toString() = " + pp.toString());
        } else {
          out.println("request.getUserPrincipal() = null");
        }
      }

    } catch (Exception ex) {
      System.out
          .println("ModTestServlet->MTSRemoteUserCorrespondsToPrin() failed");
      ex.printStackTrace();
    }

    return;
  } // MTSRemoteUserCorrespondsToPrin()

  /*
   * This will get called after we have authenticated with our user j2ee in role
   * Administrator. When we get into this test method, a call to isUserInRole
   * better return true. if we have not authenticated, we expect to get false.
   */
  public void testGPCIsUserInRole(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      boolean bval = false;
      PrintWriter out = response.getWriter();
      out.println("Enterred ModTestServlet->testGPCIsUserInRole()");

      // first make sure user is in role!
      // after successful login, we should see true value for isUserInRole
      bval = request.isUserInRole("Administrator");
      if (bval == true) {
        debug(out, "isUserInRole() returns true.");
      } else {
        debug(out, "Authentication Error - isUserInRole() returns false.");
        debug(out, "request.getRemoteUser() = " + request.getRemoteUser());
        out.println("ModTestServlet->testGPCIsUserInRole() failed");
      }

      if (bval) {
        out.println("ModTestServlet->testGPCIsUserInRole() passed");
      }

    } catch (Exception ex) {
      System.out.println("ModTestServlet->testGPCIsUserInRole() failed");
      ex.printStackTrace();
    }

    return;
  }

  /*
   * This will get called after we have authenticated with our user j2ee in role
   * Administrator. When we get into this test method, a call to isUserInRole
   * better return true. if we have not authenticated, we expect to get false.
   */
  public void testGPCGetUserPrincipal(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      boolean bval = false;

      PrintWriter out = response.getWriter();
      out.println("Enterred ModTestServlet->testGPCGetUserPrincipal()");

      // getUserPrincipal() should have been set on proper validateRequest()
      if (request.getUserPrincipal() == null) {
        debug(out,
            "ERROR - request.getUserPrincipal() == null but should not!");
      }

      String principalName = request.getUserPrincipal().getName();
      if (principalName != null) {
        debug(out, "request.getUserPrincipal().getName() = " + principalName);
        bval = true;
      } else {
        debug(out,
            "ERROR - request.getUserPrincipal().getName() == null but should not be");
        out.println("ModTestServlet->testGPCGetUserPrincipal() failed");
      }

      if (bval) {
        out.println("ModTestServlet->testGPCGetUserPrincipal() passed");
      }

    } catch (Exception ex) {
      System.out.println("ModTestServlet->testGPCGetUserPrincipal() failed");
      ex.printStackTrace();
    }

    return;
  }

  /*
   * This will get called after we have authenticated with our user j2ee in role
   * Administrator. When we get into this test method, a call to isUserInRole
   * better return true. if we have not authenticated, we expect to get false.
   */
  public void testGPCGetRemoteUser(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      boolean bval = false;

      PrintWriter out = response.getWriter();
      out.println("Enterred ModTestServlet->testGPCGetRemoteUser()");

      // getRemoteUser() should have been set on proper validateRequest()
      String remoteUser = request.getRemoteUser();
      if (remoteUser != null) {
        debug(out, "request.getRemoteUser() = " + remoteUser);
        bval = true;
      } else {
        debug(out, "ERROR - request.getRemoteUser() == null but should not be");
        out.println("ModTestServlet->testGPCGetRemoteUser() failed");
      }

      if (bval) {
        out.println("ModTestServlet->testGPCGetRemoteUser() passed");
      }

    } catch (Exception ex) {
      System.out.println("ModTestServlet->testGPCGetRemoteUser() failed");
      ex.printStackTrace();
    }

    return;
  }

  /*
   * This will get called after we have authenticated with our user j2ee in role
   * Administrator. When we get into this test method, a call to isUserInRole
   * better return true. if we have not authenticated, we expect to get false.
   */
  public void testGPCGetAuthType(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      boolean bval = false;

      PrintWriter out = response.getWriter();
      out.println("Enterred ModTestServlet->testGPCGetAuthType()");
      debug("enterred ModTestServlet->testGPCGetAuthType()");

      // getAuthType() should have been set on proper validateRequest()
      String authType = request.getAuthType();
      if (authType != null) {
        debug(out, "request.getAuthType() = " + authType);
        bval = true;
      } else {
        debug(out, "ERROR - request.getAuthType() == null but should not be");
        out.println("ModTestServlet->testGPCGetAuthType() failed");
      }

      if (bval) {
        out.println("ModTestServlet->testGPCGetAuthType() passed");
      }

    } catch (Exception ex) {
      System.out.println("ModTestServlet->testGPCGetAuthType() failed");
      ex.printStackTrace();
    }

    return;
  }

  public void debug(PrintWriter out, String str) {
    out.println(str);
    System.out.println(str);
  }

  public void debug(String str) {
    System.out.println(str);
  }

}
