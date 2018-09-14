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

import java.security.AccessController;
import java.security.AccessControlException;
import java.security.PrivilegedExceptionAction;
import java.security.Permission;

import java.io.FilePermission;
import java.util.PropertyPermission;
import java.net.SocketPermission;
import java.lang.RuntimePermission;
import java.security.SecurityPermission;

import com.sun.ts.lib.util.*;

/**
 * 
 * 
 * 
 * 
 */

public final class PermDDLibrary {

  /**
   * constructor
   *
   */
  public PermDDLibrary() {
  }

  /*
   *
   * NOTE: this test originally used custom permissions (extending Permission)
   * but the status of custom permissions are not currently specified in EE so
   * we've modified this test to use common EE Security permissions.
   */
  public static String ValidateCustomPermInLib() {
    String rval = "";
    try {
      // call a priviledged method
      SecurityPermission perm = new SecurityPermission("CTSPermission1_name");
      doCheckPermission(perm);

      // we have perms so should get here
      debug("CTSPermission1_name permission okay");
      rval = "SUCCESS:  ValidateCustomPermInLib passed.";
    } catch (AccessControlException ex) {
      ex.printStackTrace();
      rval = "FAILURE:  CTSPermission1_name perm missing, throwing AccessControlException.";
      return rval;
    } catch (Exception ex) {
      ex.printStackTrace();
      rval = "FAILURE:  ValidateCustomPermInLib(), throwing unexpected Exception.";
    }

    return rval;
  }

  /*
   * This validates permissions for a lib are the same as the perms for the code
   * that is calling the lib. (in this case, the perms for the lib must be the
   * same as this servlet). per EE 7 spec, section EE.6.2.2.6 (4th para)
   *
   * This validates that a permission is bundled with the app and that the app
   * hasx grants defined thru permssions.xml but not thru the higher appserver
   * level (e.g. server.policy). We expect the perms declared in permissions.xml
   * to suffice and let us successfully call checkPermission().
   */
  public static String ValidateLocalGrantForCustomPermInLib() {
    String rval = "";
    try {
      // call a priviledged method
      SecurityPermission perm = new SecurityPermission("CTSPermission2_name");
      doCheckPermission(perm);

      // we should have perms granted thru permission.xml so should get here.
      rval = "SUCCESS:  ValidateLocalGrantForCustomPermInLib passed.";
      return rval;
    } catch (AccessControlException ex) {
      debug(
          "CTSPermission2_name permission property threw AccessControlException.");
      rval = "FAILURE:  ValidateLocalGrantForCustomPermInLib() threw unexpected exception.";
    } catch (Exception ex) {
      ex.printStackTrace();
      rval = "FAILURE:  ValidateLocalGrantForCustomPermInLib had unexpeted exception.";
    }

    return rval;
  }

  /*
   * This validates permissions for a lib are the same as the perms for the code
   * that is calling the lib. (in this case, the perms for the lib must be the
   * same as this servlet). per EE 7 spec, section EE.6.2.2.6 (4th para)
   *
   * This validates that the servlet supports the list of required perms that
   * are listed in JavaEE7 spec, Table EE.6-2 (or at least some of them.)
   */
  public static String ValidateRequiredPermSetInLib() {
    String rval = "";

    try {
      RuntimePermission rtperm = new RuntimePermission("loadLibrary.*");
      doCheckPermission(rtperm);
      debug("ValidateRequiredPermSetInLib():  valid perm for: "
          + rtperm.toString());

      RuntimePermission rtperm2 = new RuntimePermission("queuePrintJob");
      doCheckPermission(rtperm2);
      debug("ValidateRequiredPermSetInLib():  valid perm for: "
          + rtperm2.toString());

      SocketPermission socperm = new SocketPermission("*", "connect");
      doCheckPermission(socperm);
      debug("ValidateRequiredPermSetInLib():  valid perm for: "
          + socperm.toString());

      FilePermission fperm = new FilePermission("*", "read");
      doCheckPermission(fperm);
      debug("ValidateRequiredPermSetInLib():  valid perm for: "
          + fperm.toString());

      PropertyPermission pperm = new PropertyPermission("*", "read");
      doCheckPermission(pperm);
      debug("ValidateRequiredPermSetInLib():  valid perm for: "
          + pperm.toString());

      // if we have perms we should get here
      rval = "SUCCESS:  ValidateRequiredPermSetInLib passed.";
    } catch (AccessControlException ex) {
      ex.printStackTrace();
      rval = "FAILURE:  ValidateRequiredPermSetInLib had perm missing, throwing AccessControlException.";
      return rval;
    } catch (Exception ex) {
      ex.printStackTrace();
      rval = "FAILURE:  ValidateRequiredPermSetInLib had unexpected Exception.";
    }

    return rval;
  }

  /*
   * This validates permissions for a lib are the same as the perms for the code
   * that is calling the lib. (in this case, the perms for the lib must be the
   * same as this servlet). per EE 7 spec, section EE.6.2.2.6 (4th para)
   *
   * This validates that the servlet will properly fail a permission check when
   * a particular grant is missing. (in this case, the perm with matching name
   * does not exist in permissions.xml and so should fail access check.
   *
   */
  public static String ValidateMissingPermFailsInLib() {
    String rval = "";

    try {
      // call a priviledged perm check that should fail!
      SecurityPermission perm = new SecurityPermission("CTSPermission3_name");
      doCheckPermission(perm);

      // we do NOT have perms for this perm with name "CTSPermission3_name" so
      // should NOT get here
      rval = "FAILURE:  CTSPermission3_name permission okay";
      return rval;
    } catch (AccessControlException ex) {
      debug("SUCCESS:  CTSPermission3_name doesnt exist and was caught.");
      rval = "SUCCESS:  ValidateMissingPermFailsInLib passed.";
    } catch (Exception ex) {
      ex.printStackTrace();
      rval = "FAILURE:  ValidateMissingPermFailsInLib threw unexpected exception.";
    }

    return rval;
  }

  /*
   * This validates permissions for a lib are the same as the perms for the code
   * that is calling the lib. (in this case, the perms for the lib must be the
   * same as this servlet). per EE 7 spec, section EE.6.2.2.6 (4th para) This
   * particular test has a locally defined permission with one name and attempts
   * to validate access for that perm under a different (non-existing) name.
   * Access should be denied. Then, as a sanity check, access a perm using a
   * name that IS validly defined in permissions.xml (but not defined at any
   * higher appserver level). This tests the permission.xml from within a
   * library thats embedded within a servlet
   */
  public static String ValidateLocalPermsInvalidNameInLib() {
    String rval = "";

    try {
      // call a priviledged method
      SecurityPermission secPerm = new SecurityPermission("NonExistingName");

      try {
        doCheckPermission(secPerm);
        // should NOT get here
        rval = "FAILURE:  ValidateLocalPermsInvalidNameInLib() did not throw expected AccessControlException.";
        return rval;
      } catch (AccessControlException ex) {
        // should get here.
        debug(
            "SUCCESS:  ValidateLocalPermsInvalidNameInLib() threw expected AccessControlException");
      }

      // next call (as sanity check) a priviledged method that does exist
      try {
        // call a priviledged method - that exists only in permissions.xml
        SecurityPermission perm = new SecurityPermission(
            "CTSPermission_second_name");
        doCheckPermission(perm);

        // should get here.
        // we have locally defined grant/perms (thru permissions.xml) so we
        // should get here
        debug(
            "CTSPermission_second_name permission property granted AccessControlException.");
      } catch (AccessControlException ex) {
        // should NOT get here
        rval = "FAILURE:  ValidateLocalPermsInvalidNameInLib() threw unexpected exception.";
        return rval;
      } catch (Exception ex) {
        // should NOT get here
        rval = "FAILURE:  ValidateLocalPermsInvalidNameInLib had unexpected exception.";
        ex.printStackTrace();
        return rval;
      }

      rval = "SUCCESS:  ValidateLocalPermsInvalidNameInLib passed.";

    } catch (Exception ex) {
      ex.printStackTrace();
      rval = "FAILURE:  ValidateLocalPermsInvalidNameInLib had unexpected exception.";
    }

    return rval;
  }

  /*
   * This validates permissions for a lib are the same as the perms for the code
   * that is calling the lib. (in this case, the perms for the lib must be the
   * same as this servlet). per EE 7 spec, section EE.6.2.2.6 (4th para) This
   * validates that a perm (PropertyPermission) is bundled with the app and that
   * the app does NOT have grants for that perms write action (though it should
   * have perms for the read action.
   */
  public static String ValidateRestrictedLocalPermInLib() {
    String rval = "";

    try {
      // call a priviledged method
      PropertyPermission readPropertyPerm = new PropertyPermission(
          "TestPropertyPerm", "read");

      try {
        doCheckPermission(readPropertyPerm);
        // should get here
        debug(
            "SUCCESS:  ValidateRestrictedLocalPermInLib() has grant for read of TestPropertyPerm");
      } catch (AccessControlException ex) {
        // should not get here.
        ex.printStackTrace();
        rval = "FAILURE:  ValidateRestrictedLocalPermInLib() threw unexpected exception for read of TestPropertyPerm.";
        return rval;
      }

      /*
       * // XXXX: if EE were to conclusively state it supports local
       * permissions, we could // validate using customPermissions that a write
       * was not assigned but when // using common EE permissions, we can not be
       * sure that EE perms restrict // allowing PropertyPermission "write"
       * actions so we cant assume its restricted // by default and won't test
       * for this at this current rev. PropertyPermission writePropertyPerm =
       * new PropertyPermission("TestPropertyPerm", "write"); try {
       * doCheckPermission(writePropertyPerm); // should NOT get here - we
       * should have had an excpetion thrown rval =
       * "FAILURE:  ValidateRestrictedLocalPermInLib() did not throw expected exception for write of TestPropertyPerm."
       * ; return rval; } catch (AccessControlException ex) { // should get here
       * debug("SUCCESS:  ValidateRestrictedLocalPermInLib() threw expected exception for write of TestPropertyPerm."
       * ); }
       */
      rval = "SUCCESS:  ValidateRestrictedLocalPermInLib passed.";
    } catch (Exception ex) {
      ex.printStackTrace();
      rval = "FAILURE:  ValidateRestrictedLocalPermInLib had unexpected exception.";
    }

    return rval;
  }

  /*
   * This validates that a permission is granted from within the app server not
   * listed/granted from within the local permissions.xml. In such a case the
   * permission shoudl be 'inherited' from teh app server and NOT declined just
   * because it was omitted from the local permissions.xml.
   *
   * NOTE: this test originally used custom permissions (extending Permission)
   * but the status of custom permissions are not currently specified in EE so
   * we've modified this test to use common EE Security permissions.
   */
  public static String ValidateCustomPermFromAppServerInLib() {
    String rval = "";

    try {
      // call a priviledged method
      SecurityPermission perm = new SecurityPermission("CTSPermission1_name2");
      doCheckPermission(perm);

      // we have perms set from within appserver as part of initial config
      // and even though we did not set anything in our permissions.xml for
      // CTSPermission1_name2, we should still make it here.
      debug("CTSPermission1_name2 permission okay");
      rval = "SUCCESS:  ValidateCustomPermFromAppServerInLib passed.";
    } catch (AccessControlException ex) {
      rval = "FAILURE:  CTSPermission1_name2 perm not inherited from app server grants, throwing AccessControlException.";
      ex.printStackTrace();
    } catch (Exception ex) {
      rval = "FAILURE:  ValidateCustomPermFromAppServerInLib(), throwing unexpected Exception.";
      ex.printStackTrace();
    }

    return rval;
  }

  public static void doCheckPermission(Permission pp) throws Exception {
    final Permission perm = pp;
    AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() {
      public Void run() throws AccessControlException {
        AccessController.checkPermission(perm);
        return null;
      }
    });
  }

  public static void debug(String str) {
    System.out.println(str);
    TestUtil.logMsg(str);
  }
}
