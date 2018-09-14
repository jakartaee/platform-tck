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

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.HttpMethodConstraint;
import javax.servlet.annotation.HttpConstraint;
import javax.annotation.security.DeclareRoles;

import java.security.AccessController;
import java.security.AccessControlException;
import java.security.PrivilegedExceptionAction;
import java.security.Permission;
import java.security.SecurityPermission;

import java.util.PropertyPermission;
import java.net.SocketPermission;

import com.sun.ts.lib.util.*;

/*
 * This servlet will be used to assist in testing the permissions.xml
 * security support (as listed in Java EE 7 spec).  This will perform
 * validations in the Servlet container. 
 * 
 * This is intended to be called from a Client, and will send back 
 * status from the tests that are invoked.  The Client will invoked this
 * servlet, and pass in a request parameter that indicates which test to
 * call.  
 *
 */
@ServletSecurity(value = @HttpConstraint(rolesAllowed = {
    "Administrator" }), httpMethodConstraints = {
        @HttpMethodConstraint(value = "GET", rolesAllowed = "Administrator"),
        @HttpMethodConstraint(value = "POST", rolesAllowed = "Administrator") })
@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
  private String servletAppContext = null;

  private String testMethod = null;

  private static final String SEC_MGR_WARNING = "ERROR:  Security Manager is NOT enabled and must be for these tests.  If you have passed these tests while running with Security Manager enabled, you can use keywords to bypass the running of these tests when Security Manager is disabled.";

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    debug("in TestServlet.doGet()");
    getPropsAndParams(request, response);
    doPost(request, response);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    debug("in TestServlet.doPost()");
    getPropsAndParams(request, response);
    doTests(request, response);
  }

  private void doTests(HttpServletRequest request,
      HttpServletResponse response) {

    debug("in TestServlet.doTests()");
    PrintWriter out = null;
    try {
      out = response.getWriter();

      // security manager must be enabled for these tests to be valid
      if (null == System.getSecurityManager()) {
        // security manager is NOT enabled and must be for these tests
        send_output(out, SEC_MGR_WARNING);
        return;
      }

    } catch (Exception ex) {
      debug("got exception in TestServlet");
      ex.printStackTrace();
    }

    // get some common props
    getPropsAndParams(request, response);

    if (testMethod.equals("ValidateCustomPerm")) {
      // test permission.xml from within a servlet
      debug("TestServlet.doTests(): testMethod == ValidateCustomPerm");
      validateCustomPerm(request, response);

    } else if (testMethod.equals("ValidateCustomPermFromAppServer")) {
      // test permission.xml allows perm setting from app server
      debug(
          "TestServlet.doTests(): testMethod == ValidateCustomPermFromAppServer");
      validateCustomPermFromAppServer(request, response);

    } else if (testMethod.equals("ValidateLocalGrantForCustomPerm")) {
      // test permission.xml from within a servlet
      debug(
          "TestServlet.doTests(): testMethod == validateLocalGrantForCustomPerm");
      validateLocalGrantForCustomPerm(request, response);

    } else if (testMethod.equals("ValidateRequiredPermSet")) {
      // test permission.xml from within a servlet
      debug("TestServlet.doTests(): testMethod == validateRequiredPermSet");
      validateRequiredPermSet(request, response);

    } else if (testMethod.equals("ValidateMissingPermFails")) {
      // test permission.xml from within a servlet
      debug("TestServlet.doTests(): testMethod == ValidateMissingPermFails");
      validateMissingPermFails(request, response);

    } else if (testMethod.equals("ValidateRestrictedLocalPerm")) {
      // test permission.xml from within a servlet
      debug("TestServlet.doTests(): testMethod == ValidateRestrictedLocalPerm");
      validateRestrictedLocalPerm(request, response);

    } else if (testMethod.equals("ValidateLocalPermsInvalidName")) {
      // test permission.xml from within a servlet
      debug("TestServlet.doTests(): testMethod == ValidateRestrictedLocalPerm");
      validateLocalPermsInvalidName(request, response);

    } else if (testMethod.equals("ValidateCustomPermInLib")) {
      // test permission.xml from within a library thats embedded within servlet
      debug("TestServlet.doTests(): testMethod == ValidateCustomPermInLib");
      validateCustomPermInLib(request, response);

    } else if (testMethod.equals("ValidateCustomPermFromAppServerInLib")) {
      // test permission.xml from within a library thats embedded within servlet
      debug(
          "TestServlet.doTests(): testMethod == ValidateCustomPermFromAppServerInLib");
      validateCustomPermFromAppServerInLib(request, response);

    } else if (testMethod.equals("ValidateLocalGrantForCustomPermInLib")) {
      // test permission.xml from within a library thats embedded within servlet
      debug(
          "TestServlet.doTests(): testMethod == validateLocalGrantForCustomPermInLib");
      validateLocalGrantForCustomPermInLib(request, response);

    } else if (testMethod.equals("ValidateRequiredPermSetInLib")) {
      // test permission.xml from within a library thats embedded within servlet
      debug(
          "TestServlet.doTests(): testMethod == validateRequiredPermSetInLib");
      validateRequiredPermSetInLib(request, response);

    } else if (testMethod.equals("ValidateMissingPermFailsInLib")) {
      // test permission.xml from within a library thats embedded within servlet
      debug(
          "TestServlet.doTests(): testMethod == ValidateMissingPermFailsInLib");
      validateMissingPermFailsInLib(request, response);

    } else if (testMethod.equals("ValidateRestrictedLocalPermInLib")) {
      // test permission.xml from within a library thats embedded within servlet
      debug(
          "TestServlet.doTests(): testMethod == ValidateRestrictedLocalPermInLib");
      validateRestrictedLocalPermInLib(request, response);

    } else if (testMethod.equals("ValidateLocalPermsInvalidNameInLib")) {
      // test permission.xml from within a library thats embedded within servlet
      debug(
          "TestServlet.doTests(): testMethod == ValidateLocalPermsInvalidNameInLib");
      validateLocalPermsInvalidNameInLib(request, response);
    }

  }

  private void getPropsAndParams(HttpServletRequest req,
      HttpServletResponse response) {

    // set testMethod
    testMethod = req.getParameter("method.under.test");

    return;
  }

  /*
   * This validates that a perm (PropertyPermission) is bundled with the app and
   * that the app does NOT have grants for that perms write action but it should
   * have perms for the read action. The perm is defined ONLY in permissions.xml
   * and not defined at the higher (app server) level. This tests the
   * permission.xml from within a servlet
   */
  public void validateRestrictedLocalPerm(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      PrintWriter out = response.getWriter();

      try {
        // call a priviledged method
        PropertyPermission readPropertyPerm = new PropertyPermission(
            "TestPropertyPerm", "read");

        try {
          doCheckPermission(readPropertyPerm);
          // should get here
          send_output(out,
              "SUCCESS:  validateRestrictedLocalPerm() has grant for read of TestPropertyPerm");
        } catch (AccessControlException ex) {
          // should not get here.
          send_output(out,
              "FAILURE:  validateRestrictedLocalPerm() threw unexpected exception for read of TestPropertyPerm.");
          ex.printStackTrace();
          return;
        }
        /*
         * // XXXX: if EE were to conclusively state it supports local
         * permissions, we could // validate using customPermissions that a
         * write was not assigned but when // using common EE permissions, we
         * can not be sure that EE perms restrict // allowing PropertyPermission
         * "write" actions so we cant assume its restricted // by default and
         * won't test for this at this current rev. PropertyPermission
         * writePropertyPerm = new PropertyPermission("TestPropertyPerm",
         * "write"); try { doCheckPermission(writePropertyPerm); // should NOT
         * get here - we should have had an excpetion thrown send_output(out,
         * "FAILURE:  validateRestrictedLocalPerm() did not throw expected exception for write of TestPropertyPerm."
         * ); return; } catch (AccessControlException ex) { send_output(out,
         * "SUCCESS:  validateRestrictedLocalPerm() threw expected exception for write of TestPropertyPerm."
         * ); }
         */
        send_output(out, "SUCCESS:  validateRestrictedLocalPerm passed.");

      } catch (Exception ex) {
        send_output(out,
            "FAILURE:  validateRestrictedLocalPerm had unexpected exception.");
        ex.printStackTrace();
      }

    } catch (Exception ex) {
      System.out.println("TestServlet->validateRestrictedLocalPerm() failed");
      ex.printStackTrace();
    }
  }

  /*
   * This validates that a perm (PropertyPermission) that is bundled with the
   * app. Additionally, we want to verify the perm for class=PropertyPermission
   * does NOT pass the access check since we will be trying to access a
   * non-existing/invalid named perm. Then, as a sanity check, access a perm
   * using a name that IS validly defined in permissions.xml (but not defined at
   * any higher appserver level). This tests the permission.xml from within a
   * servlet
   */
  public void validateLocalPermsInvalidName(HttpServletRequest request,
      HttpServletResponse response) {

    PrintWriter out = null;

    try {
      out = response.getWriter();

      // call a priviledged method that does not exist
      SecurityPermission secPerm = new SecurityPermission("NonExistingName");

      try {
        doCheckPermission(secPerm);
        // should NOT get here
        send_output(out,
            "FAILURE:  validateLocalPermsInvalidName() did not throw expected AccessControlException.");
        return;
      } catch (AccessControlException ex) {
        // should get here.
        send_output(out,
            "SUCCESS:  validateLocalPermsInvalidName() threw expected AccessControlException");
      }

      // next call a priviledged method that does exist
      try {
        // call a priviledged method - that exists only in permissions.xml
        SecurityPermission perm = new SecurityPermission(
            "CTSPermission_second_name");
        doCheckPermission(perm);

        // should get here.
        // we have locally defined grant/perms (thru permissions.xml) so we
        // should get here
        send_output(out,
            "CTSPermission_second_name permission property granted AccessControlException.");
      } catch (AccessControlException ex) {
        // should NOT get here
        send_output(out,
            "FAILURE:  validateLocalPermsInvalidName() threw unexpected exception.");
        return;
      } catch (Exception ex) {
        // should NOT get here
        send_output(out,
            "FAILURE:  validateLocalPermsInvalidName had unexpeted exception.");
        ex.printStackTrace();
        return;
      }

      send_output(out, "SUCCESS:  validateLocalPermsInvalidName passed.");

    } catch (Exception ex) {
      send_output(out,
          "FAILURE:  validateLocalPermsInvalidName had unexpected exception.");
      ex.printStackTrace();
    }
  }

  /*
   * This validates that a permission is bundled with the app and that the app
   * does NOT have the higher level app server grants for that perm but this
   * does have local grants for that permission via permissions.xml. There are
   * no actions define for this perm. This tests the permission.xml from within
   * a servlet
   */
  public void validateLocalGrantForCustomPerm(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      PrintWriter out = response.getWriter();

      try {
        // call a priviledged method
        SecurityPermission perm = new SecurityPermission("CTSPermission2_name");
        doCheckPermission(perm);

        // we have locally defined grant/perms (thru permissions.xml) so we
        // should get here
        send_output(out,
            "CTSPermission2_name permission property granted AccessControlException.");
        send_output(out, "SUCCESS:  validateLocalGrantForCustomPerm passed.");
      } catch (AccessControlException ex) {
        send_output(out,
            "FAILURE:  validateLocalGrantForCustomPerm() threw unexpected AccessControlException.");
        ex.printStackTrace();
      } catch (Exception ex) {
        send_output(out,
            "FAILURE:  validateLocalGrantForCustomPerm had unexpeted exception.");
        ex.printStackTrace();
      }

    } catch (Exception ex) {
      System.out
          .println("TestServlet->validateLocalGrantForCustomPerm() failed");
      ex.printStackTrace();
    }
  }

  /*
   * This validates that a permission is bundled with the app and that the app
   * does have grants for that perm. This perm should be granted at both: -
   * configuration (config.vi) to add perm to appserver polcy - in local
   * permissions.xml This should validate we can have the perm declared in both
   * places and it still works. This tests the permission.xml from within a
   * servlet
   *
   * NOTE: this test originally used custom permissions (extending Permission)
   * but the status of custom permissions are not currently specified in EE so
   * we've modified this test to use common EE Security permissions.
   *
   */
  public void validateCustomPerm(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      PrintWriter out = response.getWriter();

      try {
        // call a priviledged method
        SecurityPermission perm = new SecurityPermission("CTSPermission1_name");
        doCheckPermission(perm);

        // we have perms so should get here
        send_output(out, "CTSPermission1_name permission okay");
        send_output(out, "SUCCESS:  validateCustomPerm passed.");
      } catch (AccessControlException ex) {
        send_output(out,
            "FAILURE:  CTSPermission1_name perm missing, throwing AccessControlException.");
        ex.printStackTrace();
      } catch (Exception ex) {
        send_output(out,
            "FAILURE:  validateCustomPerm(), throwing unexpected Exception.");
        ex.printStackTrace();
      }

    } catch (Exception ex) {
      System.out.println("TestServlet->validateCustomPerm() failed");
      ex.printStackTrace();
    }
  }

  /*
   * This validates that a permission is granted from within the app server not
   * listed/granted from within the local permissions.xml. In such a case the
   * permission shoudl be 'inherited' from teh app server and NOT declined just
   * because it was omitted from the local permissions.xml. This tests the
   * permission.xml from within a servlet
   *
   * NOTE: this test originally used custom permissions (extending Permission)
   * but the status of custom permissions are not currently specified in EE so
   * we've modified this test to use common EE Security permissions.
   */
  public void validateCustomPermFromAppServer(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      PrintWriter out = response.getWriter();

      try {
        // call a priviledged method
        SecurityPermission perm = new SecurityPermission(
            "CTSPermission1_name2");
        doCheckPermission(perm);

        // we have perms set from within appserver as part of initial config
        // and even though we did not set anything in our permissions.xml for
        // CTSPermission1_name2, we should still make it here.
        send_output(out, "CTSPermission1_name2 permission okay");
        send_output(out, "SUCCESS:  validateCustomPermFromAppServer passed.");
      } catch (AccessControlException ex) {
        send_output(out,
            "FAILURE:  CTSPermission1_name perm missing, throwing AccessControlException.");
        ex.printStackTrace();
      } catch (Exception ex) {
        send_output(out,
            "FAILURE:  validateCustomPermFromAppServer(), throwing unexpected Exception.");
        ex.printStackTrace();
      }

    } catch (Exception ex) {
      System.out
          .println("TestServlet->validateCustomPermFromAppServer() failed");
      ex.printStackTrace();
    }
  }

  /*
   * This validates that the servlet will properly fail a permission check when
   * a particular grant is missing. (in this case, the perm with matching name
   * does not exist in permissions.xml and so should fail access check. This
   * tests the permission.xml from within a servlet
   */
  public void validateMissingPermFails(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      PrintWriter out = response.getWriter();

      try {
        // call a non-priviledged method
        SecurityPermission perm = new SecurityPermission("CTSPermission3_name");
        doCheckPermission(perm);

        // we do NOT have perms for this perm with name "CTSPermission3_name" so
        // should NOT get here
        send_output(out, "FAILURE:  CTSPermission3_name permission okay");
      } catch (AccessControlException ex) {
        send_output(out,
            "SUCCESS:  CTSPermission3_name doesnt exist and was caught.");
        send_output(out, "SUCCESS:  validateMissingPermFails passed.");
      } catch (Exception ex) {
        send_output(out,
            "FAILURE:  validateMissingPermFails threw unexpected exception.");
        ex.printStackTrace();
      }

    } catch (Exception ex) {
      System.out.println("TestServlet->validateMissingPermFails() failed");
      ex.printStackTrace();
    }
  }

  /*
   * This validates that the servlet supports the list of required perms that
   * are listed in JavaEE7 spec, Table EE.6-2 This tests the permission.xml from
   * within a servlet
   */
  public void validateRequiredPermSet(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      PrintWriter out = response.getWriter();

      try {
        RuntimePermission rtperm = new RuntimePermission("loadLibrary.*");
        doCheckPermission(rtperm);
        debug(
            "validateRequiredPermSet():  valid perm for: " + rtperm.toString());

        RuntimePermission rtperm2 = new RuntimePermission("queuePrintJob");
        doCheckPermission(rtperm2);
        debug("validateRequiredPermSet():  valid perm for: "
            + rtperm2.toString());

        SocketPermission socperm = new SocketPermission("*", "connect");
        doCheckPermission(socperm);
        debug("validateRequiredPermSet():  valid perm for: "
            + socperm.toString());

        FilePermission fperm = new FilePermission("*", "read");
        doCheckPermission(fperm);
        debug(
            "validateRequiredPermSet():  valid perm for: " + fperm.toString());

        PropertyPermission pperm = new PropertyPermission("*", "read");
        doCheckPermission(pperm);
        debug(
            "validateRequiredPermSet():  valid perm for: " + pperm.toString());

        // if we have perms we should get here
        send_output(out, "SUCCESS:  validateRequiredPermSet passed.");
      } catch (AccessControlException ex) {
        send_output(out,
            "FAILURE:  validateRequiredPermSet had perm missing, throwing AccessControlException.");
        ex.printStackTrace();
      } catch (Exception ex) {
        send_output(out,
            "FAILURE:  validateRequiredPermSet had unexpected Exception.");
        ex.printStackTrace();
      }

    } catch (Exception ex) {
      System.out.println("TestServlet->validateRequiredPermSet() failed");
      ex.printStackTrace();
    }
  }

  /*
   * This validates that a permission is bundled with the app and that the app
   * does NOT have grants for that perm. This tests the permission.xml from
   * within a library thats embedded within a servlet
   */
  public void validateRestrictedLocalPermInLib(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      PrintWriter out = response.getWriter();

      String str = PermDDLibrary.ValidateRestrictedLocalPermInLib();
      send_output(out, str);

    } catch (Exception ex) {
      System.out
          .println("TestServlet->validateRestrictedLocalPermInLib() failed");
      ex.printStackTrace();
    }
  }

  /*
   * This validates that a permission is bundled with the app and that the app
   * does NOT have grants for that perm. This tests the permission.xml from
   * within a library thats embedded within a servlet
   */
  public void validateLocalGrantForCustomPermInLib(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      PrintWriter out = response.getWriter();

      String str = PermDDLibrary.ValidateLocalGrantForCustomPermInLib();
      send_output(out, str);

    } catch (Exception ex) {
      System.out.println(
          "TestServlet->validateLocalGrantForCustomPermInLib() failed");
      ex.printStackTrace();
    }
  }

  /*
   * This validates that a permission is bundled with the app and that the app
   * does have grants for that perm. This perm should be granted at both: -
   * configuration (config.vi) to add perm to appserver polcy - in local
   * permissions.xml This should validate we can have the perm declared in both
   * places and it still works. This tests the permission.xml from within a
   * library thats embedded within a servlet
   */
  public void validateCustomPermInLib(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      PrintWriter out = response.getWriter();

      String str = PermDDLibrary.ValidateCustomPermInLib();
      send_output(out, str);

    } catch (Exception ex) {
      System.out.println("TestServlet->validateCustomPermInLib() failed");
      ex.printStackTrace();
    }
  }

  /*
   * This validates that the servlet will properly fail a permission check when
   * a particular grant is missing. This tests the permission.xml from within a
   * library thats embedded within a servlet
   */
  public void validateMissingPermFailsInLib(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      PrintWriter out = response.getWriter();

      String str = PermDDLibrary.ValidateMissingPermFailsInLib();
      send_output(out, str);

    } catch (Exception ex) {
      System.out.println("TestServlet->validateMissingPermFailsInLib() failed");
      ex.printStackTrace();
    }
  }

  /*
   * This validates that the servlet supports the list of required perms that
   * are listed in JavaEE7 spec, Table EE.6-2 This tests the permission.xml from
   * within a library thats embedded within a servlet
   */
  public void validateRequiredPermSetInLib(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      PrintWriter out = response.getWriter();

      String str = PermDDLibrary.ValidateRequiredPermSetInLib();
      send_output(out, str);

    } catch (Exception ex) {
      System.out.println("TestServlet->validateRequiredPermSetInLib() failed");
      ex.printStackTrace();
    }
  }

  /*
   * This validates permissions for a lib are the same as the perms for the code
   * that is calling the lib. (in this case, the perms for the lib must be the
   * same as this servlet). per EE 7 spec, section EE.6.2.2.6 (4th para) This
   * particular test has a locally defined permission with one name and attempts
   * to validate access for that perm under a different (non-existing) name. The
   * actions do match but the name does not. Access should be denied. This tests
   * the permission.xml from within a library thats embedded within a servlet
   */
  public void validateLocalPermsInvalidNameInLib(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      PrintWriter out = response.getWriter();

      String str = PermDDLibrary.ValidateLocalPermsInvalidNameInLib();
      send_output(out, str);

    } catch (Exception ex) {
      System.out
          .println("TestServlet->validateLocalPermsInvalidNameInLib() failed");
      ex.printStackTrace();
    }
  }

  /*
   * This validates permissions for a lib are the same as the perms for the code
   * that is calling the lib. (in this case, the perms for the lib must be the
   * same as this servlet). per EE 7 spec, section EE.6.2.2.6 (4th para)
   * 
   * This validates that a permission is granted from within the app server not
   * listed/granted from within the local permissions.xml. In such a case the
   * permission shoudl be 'inherited' from teh app server and NOT declined just
   * because it was omitted from the local permissions.xml.
   * 
   */
  public void validateCustomPermFromAppServerInLib(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      PrintWriter out = response.getWriter();

      String str = PermDDLibrary.ValidateCustomPermFromAppServerInLib();
      send_output(out, str);

    } catch (Exception ex) {
      System.out.println(
          "TestServlet->validateCustomPermFromAppServerInLib() failed");
      ex.printStackTrace();
    }
  }

  public void send_output(PrintWriter out, String str) {
    if (out != null) {
      out.println(str);
      out.flush();
      debug(str);
    } else {
      print_err("ERROR, Null PrintWriter:  can not properly send back message: "
          + str);
    }
  }

  public void print_err(String str) {
    System.err.println(str);
    debug(str);
  }

  public void doCheckPermission(Permission pp) throws Exception {
    final Permission perm = pp;
    AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() {
      public Void run() throws AccessControlException {
        AccessController.checkPermission(perm);
        return null;
      }
    });
  }

  public void debug(String str) {
    TestUtil.logMsg(str);
    System.out.println(str);
  }

}
