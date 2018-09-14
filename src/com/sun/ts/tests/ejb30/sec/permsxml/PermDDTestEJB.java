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

package com.sun.ts.tests.ejb30.sec.permsxml;

import com.sun.ts.lib.util.RemoteLoggingInitException;
import com.sun.ts.lib.util.TestUtil;

import javax.ejb.Stateful;
import javax.ejb.Remote;

import java.security.AccessController;
import java.security.Permission;
import java.security.PrivilegedExceptionAction;
import java.security.AccessControlException;
import java.security.SecurityPermission;
import java.io.FilePermission;
import java.util.PropertyPermission;
import java.net.SocketPermission;
import java.lang.RuntimePermission;

@Stateful(name = "PermDDTestEJB")
@Remote({ PermDDTestIF.class })
public class PermDDTestEJB implements PermDDTestIF {
  private static final String SEC_MGR_WARNING = "ERROR:  Security Manager is NOT enabled and must be for these tests.  If you have passed these tests while running with Security Manager enabled, you can use keywords to bypass the running of these tests when Security Manager is disabled.";

  /*
   * This validates that a permission is bundled with the app and that the app
   * does have grants for that perm. This perm should be granted at both: -
   * configuration (config.vi) to add perm to appserver polcy - in local
   * permissions.xml This should validate we can have the perm declared in both
   * places and it still works. This tests the permission.xml from within an ejb
   */
  public boolean validateCustomPerm() {
    boolean rval = false; // assume fail

    debug("Enterred: PermDDTestEJB.validateCustomPerm().");

    try {
      if (null == System.getSecurityManager()) {
        // security manager is NOT enabled and must be for these tests
        debug(SEC_MGR_WARNING);
        return false;
      }

      // call a priviledged method
      SecurityPermission perm = new SecurityPermission("CTSPermission1_name");
      doCheckPermission(perm);

      // we have perms so should get here
      debug("CTSPermission1_name permission okay");
      debug("SUCCESS:  validateCustomPerm passed.");
      rval = true;
    } catch (AccessControlException ex) {
      debug(
          "FAILURE:  CTSPermission1_name perm missing, throwing AccessControlException.");
      ex.printStackTrace();
      return false;
    } catch (Exception ex) {
      debug("FAILURE:  validateCustomPerm(), throwing unexpected Exception.");
      ex.printStackTrace();
      return false;
    }

    debug("Leaving PermDDTestEJB.validateCustomPerm() with rval = " + rval);

    return rval;
  }

  /*
   * This expects to have our user defined permission (SecurityPermission) named
   * "CTSPermission2_name" that is NOT defined in the appserver security policy
   * subsystem (e.g. server.policy) but it is defined in the permissions.xml.
   * This validates that a permission is bundled with the app and that the app
   * does NOT have the higher level app server grants for that perm but this
   * does have local grants for that permission via permissions.xml. There are
   * no actions define for this perm. This tests the permission.xml from within
   * an ejb
   */
  public boolean validateLocalGrantForCustomPerm() {
    boolean rval = false; // assume fail

    debug("Enterred: PermDDTestEJB.validateLocalGrantForCustomPerm().");

    try {
      if (null == System.getSecurityManager()) {
        // security manager is NOT enabled and must be for these tests
        debug(SEC_MGR_WARNING);
        return false;
      }

      // call a priviledged method
      SecurityPermission perm = new SecurityPermission("CTSPermission2_name");
      doCheckPermission(perm);

      // we have locally defined grant/perms (thru permissions.xml) so we should
      // get here
      debug("SUCCESS:  validateLocalGrantForCustomPerm passed.");
      rval = true;
    } catch (AccessControlException ex) {
      debug(
          "CTSPermission2_name permission property threw unexpected AccessControlException.");
      debug(
          "FAILURE:  validateLocalGrantForCustomPerm() did threw unexpected AccessControlException.");
    } catch (Exception ex) {
      debug(
          "FAILURE:  validateLocalGrantForCustomPerm had unexpeted exception.");
      ex.printStackTrace();
    }

    return rval;
  }

  /*
   * This validates that a perm (CTSPropertyPermission) is bundled with the app
   * and that the app does NOT have grants for that perms write action but it
   * should have perms for the read action. The perm is defined ONLY in
   * permissions.xml and not defined at the higher (app server) level. This
   * tests the permission.xml from within an ejb
   */
  public boolean validateRestrictedLocalPerm() {
    boolean rval = false; // assume fail

    debug("Enterred: PermDDTestEJB.validateRestrictedLocalPerm().");

    try {
      if (null == System.getSecurityManager()) {
        // security manager is NOT enabled and must be for these tests
        debug(SEC_MGR_WARNING);
        return false;
      }

      // call a priviledged method
      PropertyPermission readPropertyPerm = new PropertyPermission(
          "TestPropertyPerm", "read");

      try {
        doCheckPermission(readPropertyPerm);
        // should get here
        debug(
            "SUCCESS:  validateRestrictedLocalPerm() has grant for read of TestPropertyPerm");
        rval = true;
      } catch (AccessControlException ex) {
        // should not get here.
        debug(
            "FAILURE:  validateRestrictedLocalPerm() threw unexpected exception for read of TestPropertyPerm.");
        ex.printStackTrace();
        return false;
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
       * should have had an excpetion thrown
       * debug("FAILURE:  validateRestrictedLocalPerm() did not throw expected exception for write of TestPropertyPerm."
       * ); return false; } catch (AccessControlException ex) {
       * debug("SUCCESS:  validateRestrictedLocalPerm() threw expected exception for write of TestPropertyPerm."
       * ); rval = true; }
       */
      debug("SUCCESS:  validateRestrictedLocalPerm passed.");

    } catch (Exception ex) {
      debug("FAILURE:  validateRestrictedLocalPerm had unexpected exception.");
      ex.printStackTrace();
      rval = false;
    }

    return rval;
  }

  /*
   * This validates that the servlet supports the list of required perms that
   * are listed in JavaEE7 spec, Table EE.6-2 This tests the permission.xml from
   * within an ejb
   */
  public boolean validateRequiredPermSet() {
    boolean rval = false; // assume fail

    debug("Enterred: PermDDTestEJB.validateRequiredPermSet().");

    try {
      if (null == System.getSecurityManager()) {
        // security manager is NOT enabled and must be for these tests
        debug(SEC_MGR_WARNING);
        return false;
      }

      RuntimePermission rtperm = new RuntimePermission("loadLibrary.*");
      doCheckPermission(rtperm);
      debug("validateRequiredPermSet():  valid perm for: " + rtperm.toString());

      RuntimePermission rtperm2 = new RuntimePermission("queuePrintJob");
      doCheckPermission(rtperm2);
      debug(
          "validateRequiredPermSet():  valid perm for: " + rtperm2.toString());

      SocketPermission socperm = new SocketPermission("*", "connect");
      doCheckPermission(socperm);
      debug(
          "validateRequiredPermSet():  valid perm for: " + socperm.toString());

      FilePermission fperm = new FilePermission("*", "read");
      doCheckPermission(fperm);
      debug("validateRequiredPermSet():  valid perm for: " + fperm.toString());

      PropertyPermission pperm = new PropertyPermission("*", "read");
      doCheckPermission(pperm);
      debug("validateRequiredPermSet():  valid perm for: " + pperm.toString());

      // if we have perms we should get here
      debug("SUCCESS:  validateRequiredPermSet passed.");
      rval = true;
    } catch (AccessControlException ex) {
      debug(
          "FAILURE:  validateRequiredPermSet had perm missing, throwing AccessControlException.");
      ex.printStackTrace();
    } catch (Exception ex) {
      debug("FAILURE:  validateRequiredPermSet had unexpected Exception.");
      ex.printStackTrace();
    }

    return rval;
  }

  /*
   * This validates that the servlet will properly fail a permission check when
   * a particular grant is missing. (in this case, the perm with matching name
   * does not exist in permissions.xml and so should fail access check. This
   * tests the permission.xml from within an ejb.
   */
  public boolean validateMissingPermFails() {
    boolean rval = false; // assume fail

    debug("Enterred: PermDDTestEJB.validateMissingPermFails().");

    try {
      if (null == System.getSecurityManager()) {
        // security manager is NOT enabled and must be for these tests
        debug(SEC_MGR_WARNING);
        return false;
      }

      // call a non-priviledged method
      SecurityPermission perm = new SecurityPermission("CTSPermission3_name");
      doCheckPermission(perm);

      // we do NOT have perms for this perm with name "CTSPermission3_name" so
      // should NOT get here
      debug("FAILURE:  CTSPermission3_name permission okay");
    } catch (AccessControlException ex) {
      debug("SUCCESS:  CTSPermission3_name doesnt exist and was caught.");
      debug("SUCCESS:  validateMissingPermFails passed.");
      rval = true;
    } catch (Exception ex) {
      debug("FAILURE:  validateMissingPermFails threw unexpected exception.");
      ex.printStackTrace();
    }

    return rval;
  }

  /*
   * This validates that a perm (PropertyPermission) that is bundled with the
   * app. Additionally, we want to verify the perm for class=PropertyPermission
   * does NOT pass the access check since we will be trying to access a
   * non-existing/invalid named perm. Then, as a sanity check, access a perm
   * using a name that IS validly defined in permissions.xml (but not defined at
   * any higher appserver level). This tests the permission.xml from within an
   * ejb.
   */
  public boolean validateLocalPermsInvalidName() {

    debug("Enterred: PermDDTestEJB.validateLocalPermsInvalidName().");

    try {
      if (null == System.getSecurityManager()) {
        // security manager is NOT enabled and must be for these tests
        debug(SEC_MGR_WARNING);
        return false;
      }

      // call a priviledged method that does not exist
      SecurityPermission readPropertyPerm = new SecurityPermission(
          "NonExistingName");

      try {
        doCheckPermission(readPropertyPerm);
        // should NOT get here
        debug(
            "FAILURE:  validateLocalPermsInvalidName() did not throw expected AccessControlException.");
        return false;
      } catch (AccessControlException ex) {
        // should get here.
        debug(
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
        debug(
            "CTSPermission_second_name permission property granted AccessControlException.");
      } catch (AccessControlException ex) {
        // should NOT get here
        debug(
            "FAILURE:  validateLocalPermsInvalidName() threw unexpected exception.");
        return false;
      } catch (Exception ex) {
        // should NOT get here
        debug(
            "FAILURE:  validateLocalPermsInvalidName had unexpeted exception.");
        ex.printStackTrace();
        return false;
      }

      debug("SUCCESS:  validateLocalPermsInvalidName passed.");

    } catch (Exception ex) {
      debug(
          "FAILURE:  validateLocalPermsInvalidName had unexpected exception.");
      ex.printStackTrace();
    }

    return true;
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

  private void debug(String str) {
    System.out.println(str);
    TestUtil.logMsg(str);
  }

}
