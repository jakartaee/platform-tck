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

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.porting.TSLoginContext;

import java.util.Properties;
import javax.ejb.EJB;
import com.sun.javatest.Status;
import javax.annotation.Resource;

import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.security.AccessControlException;
import java.security.Permission;
import java.security.SecurityPermission;
import java.io.FilePermission;
import java.util.PropertyPermission;
import java.net.SocketPermission;
import java.lang.RuntimePermission;

public class Client extends EETest {

  // JNDI names for looking up ejbs
  @EJB(beanName = "PermDDTestEJB")
  static private PermDDTestIF ejbref = null;

  @Resource(lookup = "java:comp/InAppClientContainer")
  static private Boolean inAppClientContainer;

  private static final String UserNameProp = "user";

  private static final String UserPasswordProp = "password";

  private static final String AuthUser = "authuser";

  private String authuser = "";

  private String username = "";

  private String password = "";

  private Properties props = null;

  private TSLoginContext lc = null;

  private static final String SEC_MGR_WARNING = "ERROR:  Security Manager is NOT enabled and must be for these tests.  If you have passed these tests while running with Security Manager enabled, you can use keywords to bypass the running of these tests when Security Manager is disabled.";

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: user; password; authuser;
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;

    try {
      authuser = props.getProperty(AuthUser);
      username = props.getProperty(UserNameProp);
      password = props.getProperty(UserPasswordProp);

      // debug aid
      if (inAppClientContainer.booleanValue() == true) {
        logTrace("In ACC.");
      } else {
        logTrace("NOT in ACC.");
      }

      lc = new TSLoginContext();
      lc.login(username, password);

    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /*
   * @testName: ValidateCustomPerm
   * 
   * @assertion_ids: JavaEE:SPEC:290; JavaEE:SPEC:293;
   *
   * @test_Strategy: This validates that we have a particular grant under the
   * following conditions: - using locally declared Permission impl
   * (SecurityPermission) (note: this perm does not support actions) - have
   * declared grant for this (SecurityPermission) in permissions.xml - also
   * declared grant for SecurityPermission at higher app server level (thus we
   * have grant declared twice.) (Must have grant for SecurityPermission
   * "CTSPermission1_name") - within ejb, use AccessController.checkPermission()
   * to confirm our permissons are as expected
   */
  public void ValidateCustomPerm() throws Fault {
    logTrace("Enterred ValidateCustomPerm()");
    try {
      if (null == System.getSecurityManager()) {
        // security manager is NOT enabled and must be for these tests
        logMsg(SEC_MGR_WARNING);
        throw new Fault(SEC_MGR_WARNING);
      }

      if (ejbref == null) {
        logTrace("ValidateCustomPerm:  oh oh -  ejbref == null!");
        throw new Fault("FAILURE:  unexpected null value for ejbref.");
      } else {
        logTrace("ValidateCustomPerm:  good -  ejbref != null!");
      }

      if (!ejbref.validateCustomPerm()) {
        throw new Fault("ValidateCustomPerm test failed");
      }
    } catch (Exception e) {
      throw new Fault("ValidateCustomPerm test failed: ", e);
    }

    logMsg("SUCCESS:  ValidateCustomPerm Passed");
  }

  /*
   * @testName: ValidateCustomPermInACC
   * 
   * @assertion_ids: JavaEE:SPEC:290; JavaEE:SPEC:293;
   *
   * @test_Strategy: This validates that we have a particular grant under the
   * following conditions: - using locally declared Permission impl
   * (SecurityPermission) (note: this perm does not support actions) - have
   * declared grant for this (SecurityPermission) in permissions.xml - also
   * declared grant for SecurityPermission at higher app server level (thus we
   * have grant declared twice.) (Must have grant for SecurityPermission
   * "CTSPermission1_name") - within ACC, use AccessController.checkPermission()
   * to confirm our permissons are as expected
   */
  public void ValidateCustomPermInACC() throws Fault {
    logTrace("Starting ValidateCustomPermInACC test");
    try {
      if (null == System.getSecurityManager()) {
        // security manager is NOT enabled and must be for these tests
        logMsg(SEC_MGR_WARNING);
        throw new Fault(SEC_MGR_WARNING);
      }

      // call a priviledged method
      SecurityPermission perm = new SecurityPermission("CTSPermission1_name");
      doCheckPermission(perm);

      // we have perms so should get here
      logTrace("CTSPermission1_name permission okay");
    } catch (AccessControlException ex) {
      throw new Fault(
          "FAILURE:  CTSPermission1_name perm missing, throwing AccessControlException.",
          ex);
    } catch (Exception ex) {
      throw new Fault(
          "FAILURE:  ValidateCustomPermInACC(), throwing unexpected Exception.",
          ex);
    }

    logMsg("SUCCESS:  ValidateCustomPermInACC Passed.");
  }

  /*
   * @testName: ValidateLocalGrantForCustomPerm
   *
   * @assertion_ids: JavaEE:SPEC:292; JavaEE:SPEC:293; JavaEE:SPEC:303;
   * JavaEE:SPEC:304;
   *
   * @test_Strategy: This validates that we have a particular grant under the
   * following conditions: - using locally declared Permission impl
   * (SecurityPermission) note: SecurityPermission does NOT have support for
   * actions. - have declared grant in permissions.xml - have NO declared grant
   * at higher app server level (e.g. server.policy etc) - within ejb, use
   * AccessController.checkPermission() to confirm our permissons are as
   * expected
   *
   */
  public void ValidateLocalGrantForCustomPerm() throws Fault {
    logTrace("Starting ValidateLocalGrantForCustomPerm test");

    try {
      if (null == System.getSecurityManager()) {
        // security manager is NOT enabled and must be for these tests
        logMsg(SEC_MGR_WARNING);
        throw new Fault(SEC_MGR_WARNING);
      }

      if (!ejbref.validateLocalGrantForCustomPerm()) {
        throw new Fault("ValidateLocalGrantForCustomPerm test failed");
      }
    } catch (Exception e) {
      throw new Fault("ValidateLocalGrantForCustomPerm test failed:", e);
    }

    logMsg("SUCCESS:  ValidateLocalGrantForCustomPerm Passed");
  }

  /*
   * @testName: ValidateLocalGrantForCustomPermInACC
   *
   * @assertion_ids: JavaEE:SPEC:292; JavaEE:SPEC:293; JavaEE:SPEC:303;
   * JavaEE:SPEC:304;
   *
   * @test_Strategy: This validates that we have a particular grant under the
   * following conditions: - using locally declared Permission impl
   * (SecurityPermission) note: SecurityPermission does NOT have support for
   * actions. - have declared grant in permissions.xml - have NO declared grant
   * at higher app server level (e.g. server.policy etc) - within ACC, use
   * AccessController.checkPermission() to confirm our permissons are as
   * expected
   *
   */
  public void ValidateLocalGrantForCustomPermInACC() throws Fault {
    logTrace("Starting ValidateLocalGrantForCustomPermInACC test");

    try {
      if (null == System.getSecurityManager()) {
        // security manager is NOT enabled and must be for these tests
        logMsg(SEC_MGR_WARNING);
        throw new Fault(SEC_MGR_WARNING);
      }

      // call a priviledged method
      SecurityPermission perm = new SecurityPermission("CTSPermission2_name");
      doCheckPermission(perm);

      // should get here!
      logMsg("SUCCESS:  ValidateLocalGrantForCustomPermInACC passed.");
    } catch (AccessControlException ex) {
      // we do NOT have perms so we should NOT get here
      logMsg(
          "CTSPermission2_name permission property threw unexpected AccessControlException.");
      throw new Fault(
          "FAILURE:  validateLocalGrantForCustomPerm() did threw unexpected AccessControlException.",
          ex);
    } catch (Exception ex) {
      throw new Fault(
          "FAILURE:  ValidateLocalGrantForCustomPermInACC threw unexpected exception.",
          ex);
    }

    logMsg("SUCCESS:  ValidateLocalGrantForCustomPermInACC Passed.");
  }

  /*
   * @testName: ValidateRestrictedLocalPerm
   *
   * @assertion_ids: JavaEE:SPEC:292; JavaEE:SPEC:293; JavaEE:SPEC:303;
   * JavaEE:SPEC:304;
   *
   * @test_Strategy: This validates that we have a particular grant under the
   * following conditions: - using perm (PropertyPermission) that is referenced
   * in permission.xml and has read but not write assigned note:
   * PropertyPermission has support for actions - have NO declared grants for
   * PropertyPermission at higher app server level (e.g. server.policy etc) so
   * that it is ONLY bundled in this local app and ref'd in permission.xml -
   * within ejb, use AccessController.checkPermission() to confirm our
   * permissons are as expected
   *
   */
  public void ValidateRestrictedLocalPerm() throws Fault {
    logTrace("Starting ValidateRestrictedLocalPerm test");
    try {
      if (null == System.getSecurityManager()) {
        // security manager is NOT enabled and must be for these tests
        logMsg(SEC_MGR_WARNING);
        throw new Fault(SEC_MGR_WARNING);
      }

      if (!ejbref.validateRestrictedLocalPerm()) {
        throw new Fault("ValidateRestrictedLocalPerm test failed");
      }
    } catch (Exception e) {
      throw new Fault("ValidateRestrictedLocalPerm test failed:", e);
    }
    logMsg("SUCCESS:  ValidateRestrictedLocalPerm Passed");
  }

  /*
   * @testName: ValidateRestrictedLocalPermInACC
   *
   * @assertion_ids: JavaEE:SPEC:292; JavaEE:SPEC:293; JavaEE:SPEC:303;
   * JavaEE:SPEC:304;
   *
   * @test_Strategy: This validates that we have a particular grant under the
   * following conditions: - using perm (PropertyPermission) that is referenced
   * in permission.xml and has read but not write assigned note:
   * PropertyPermission has support for actions - have NO declared grants for
   * PropertyPermission at higher app server level (e.g. server.policy etc) so
   * that it is ONLY bundled in this local app and ref'd in permission.xml -
   * within ACC, use AccessController.checkPermission() to confirm our
   * permissons are as expected
   *
   */
  public void ValidateRestrictedLocalPermInACC() throws Fault {
    logTrace("Starting ValidateRestrictedLocalPermInACC test");

    try {
      if (null == System.getSecurityManager()) {
        // security manager is NOT enabled and must be for these tests
        logMsg(SEC_MGR_WARNING);
        throw new Fault(SEC_MGR_WARNING);
      }

      // call a priviledged method
      PropertyPermission readPropertyPerm = new PropertyPermission(
          "TestPropertyPerm", "read");

      try {
        doCheckPermission(readPropertyPerm);
        // should get here
        logMsg(
            "ValidateRestrictedLocalPermInACC() has grant for read of TestPropertyPerm");
      } catch (AccessControlException ex) {
        // should not get here.
        throw new Fault(
            "FAILURE:  ValidateRestrictedLocalPermInACC() threw unexpected exception for read of TestPropertyPerm.",
            ex);
      } catch (NullPointerException ex) {
        throw new Fault(
            "FAILURE:  ValidateRestrictedLocalPermInACC() threw NullPointerException for read of TestPropertyPerm.",
            ex);
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
       * should have had an excpetion thrown throw new
       * Fault("FAILURE:  ValidateRestrictedLocalPermInACC() did not throw expected exception for write of TestPropertyPerm."
       * ); } catch (AccessControlException ex) {
       * logMsg("ValidateRestrictedLocalPermInACC() threw expected exception for write of TestPropertyPerm."
       * ); } catch (NullPointerException ex) { throw new
       * Fault("FAILURE:  ValidateRestrictedLocalPermInACC() threw NullPointerException for write of TestPropertyPerm."
       * , ex); }
       */
    } catch (Exception ex) {
      throw new Fault(
          "FAILURE:  ValidateRestrictedLocalPermInACC had unexpected exception.");
    }

    logMsg("SUCCESS:  ValidateRestrictedLocalPermInACC Passed.");
  }

  /*
   * @testName: ValidateMissingPermFails
   *
   * @assertion_ids: JavaEE:SPEC:289; JavaEE:SPEC:290;
   *
   * @test_Strategy: This validates that we are NOT granted a certain permission
   * and so when we try to do our access checks, we expect to see an
   * AccessControl exception returned. This validates that we have no grant
   * under the following conditions: - using locally declared Permission impl
   * (SecurityPermission) - have NO declared grant in permissions.xml - have NO
   * declared grant at higher app server level (e.g. server.policy etc) - within
   * ejb, use AccessController.checkPermission() to confirm our permissons are
   * as expected
   *
   */
  public void ValidateMissingPermFails() throws Fault {
    logTrace("Starting ValidateMissingPermFails test");
    try {
      if (null == System.getSecurityManager()) {
        // security manager is NOT enabled and must be for these tests
        logMsg(SEC_MGR_WARNING);
        throw new Fault(SEC_MGR_WARNING);
      }

      if (!ejbref.validateMissingPermFails()) {
        throw new Fault("ValidateMissingPermFails test failed");
      }
    } catch (Exception e) {
      throw new Fault("ValidateMissingPermFails test failed:", e);
    }

    logMsg("SUCCESS:  ValidateMissingPermFails Passed");
  }

  /*
   * @testName: ValidateMissingPermFailsInACC
   *
   * @assertion_ids: JavaEE:SPEC:289; JavaEE:SPEC:290;
   *
   * @test_Strategy: This validates that we are NOT granted a certain permission
   * and so when we try to do our access checks, we expect to see an
   * AccessControl exception returned. This validates that we have no grant
   * under the following conditions: - using locally declared Permission impl
   * (SecurityPermission) - have NO declared grant in permissions.xml - have NO
   * declared grant at higher app server level (e.g. server.policy etc) - within
   * ACC, use AccessController.checkPermission() to confirm our permissons are
   * as expected
   *
   */
  public void ValidateMissingPermFailsInACC() throws Fault {
    logTrace("Starting ValidateMissingPermFailsInACC test");

    try {
      if (null == System.getSecurityManager()) {
        // security manager is NOT enabled and must be for these tests
        logMsg(SEC_MGR_WARNING);
        throw new Fault(SEC_MGR_WARNING);
      }

      // call a non-priviledged method
      SecurityPermission perm = new SecurityPermission("CTSPermission3_name");
      doCheckPermission(perm);

      // we do NOT have perms for this perm with name "CTSPermission3_name" so
      // should NOT get here
      throw new Fault("FAILURE:  CTSPermission3_name permission okay");
    } catch (AccessControlException ex) {
      // we should get here!
      logMsg("CTSPermission3_name does not exist and was caught.");
    } catch (Exception ex) {
      throw new Fault(
          "FAILURE:  ValidateMissingPermFailsInACC threw unexpected exception.",
          ex);
    }

    logMsg("SUCCESS:  ValidateMissingPermFailsInACC Passed.");
  }

  /*
   * @testName: ValidateRequiredPermSet
   *
   * @assertion_ids: JavaEE:SPEC:290; JavaEE:SPEC:296;
   *
   * @test_Strategy: This validates that we are NOT granted a certain permission
   * and so when we try to do our access checks, we expect to see an
   * AccessControl exception returned. This validates that we have no grant
   * under the following conditions: - using locally declared Permission impl
   * (SecurityPermission) - have NO declared grant in permissions.xml - have NO
   * declared grant at higher app server level (e.g. server.policy etc) - within
   * ejb, use AccessController.checkPermission() to confirm our permissons are
   * as expected
   *
   */
  public void ValidateRequiredPermSet() throws Fault {
    logTrace("Starting ValidateRequiredPermSet test");
    try {
      if (null == System.getSecurityManager()) {
        // security manager is NOT enabled and must be for these tests
        logMsg(SEC_MGR_WARNING);
        throw new Fault(SEC_MGR_WARNING);
      }

      if (!ejbref.validateRequiredPermSet()) {
        throw new Fault("ValidateRequiredPermSet test failed");
      }
    } catch (Exception e) {
      throw new Fault("ValidateRequiredPermSet test failed:", e);
    }

    logMsg("SUCCESS:  ValidateRequiredPermSet Passed");
  }

  /*
   * @testName: ValidateRequiredPermSetInACC
   *
   * @assertion_ids: JavaEE:SPEC:290; JavaEE:SPEC:296;
   *
   * @test_Strategy: This validates that we are NOT granted a certain permission
   * and so when we try to do our access checks, we expect to see an
   * AccessControl exception returned. This validates that we have no grant
   * under the following conditions: - using locally declared Permission impl
   * (SecurityPermission) - have NO declared grant in permissions.xml - have NO
   * declared grant at higher app server level (e.g. server.policy etc) - within
   * ACC, use AccessController.checkPermission() to confirm our permissons are
   * as expected
   *
   */
  public void ValidateRequiredPermSetInACC() throws Fault {
    logMsg("Starting ValidateRequiredPermSetInACC test");

    try {
      if (null == System.getSecurityManager()) {
        // security manager is NOT enabled and must be for these tests
        logMsg(SEC_MGR_WARNING);
        throw new Fault(SEC_MGR_WARNING);
      }

      RuntimePermission rtperm = new RuntimePermission("loadLibrary.*");
      doCheckPermission(rtperm);
      logTrace("ValidateRequiredPermSetInACC():  valid perm for: "
          + rtperm.toString());

      RuntimePermission rtperm2 = new RuntimePermission("queuePrintJob");
      doCheckPermission(rtperm2);
      logTrace("ValidateRequiredPermSetInACC():  valid perm for: "
          + rtperm2.toString());

      SocketPermission socperm = new SocketPermission("*", "connect");
      doCheckPermission(socperm);
      logTrace("ValidateRequiredPermSetInACC():  valid perm for: "
          + socperm.toString());

      FilePermission fperm = new FilePermission("*", "read");
      doCheckPermission(fperm);
      logTrace("ValidateRequiredPermSetInACC():  valid perm for: "
          + fperm.toString());

      PropertyPermission pperm = new PropertyPermission("*", "read");
      doCheckPermission(pperm);
      logTrace("ValidateRequiredPermSetInACC():  valid perm for: "
          + pperm.toString());

    } catch (AccessControlException ex) {
      throw new Fault(
          "FAILURE:  validateRequiredPermSet had perm missing, throwing AccessControlException.",
          ex);

    } catch (Exception ex) {
      throw new Fault(
          "FAILURE:  validateRequiredPermSet had unexpected Exception.", ex);
    }

    logMsg("SUCCESS:  ValidateRequiredPermSetInACC Passed.");
  }

  /*
   * @testName: ValidateLocalPermsInvalidName
   *
   * @assertion_ids: JavaEE:SPEC:290; JavaEE:SPEC:296;
   *
   * @test_Strategy: This validates that a perm (PropertyPermission) that is
   * bundled with the app. Additionally, we want to verify the perm for
   * class=PropertyPermission does NOT pass the access check since we will be
   * trying to access a non-existing/invalid named perm. Then, as a sanity
   * check, access a perm using a name that IS validly defined in
   * permissions.xml (but not defined at any higher appserver level).
   *
   * This tests the following conditions: - NO permission declared in app server
   * nor in permissions.xml for PropertyPermission named "NonExistingName" w/
   * READ action (should get AccessControlException since no grant exists for
   * this) - using locally declared Permission impl (SecurityPermission named
   * "CTSPermission_second_name") (should be allowed to pass AccessController
   * check for this.) - have NO declared grant at higher app server level (e.g.
   * server.policy etc) - within ejb, use AccessController.checkPermission() to
   * confirm our permissons are treated as expected
   *
   */
  public void ValidateLocalPermsInvalidName() throws Fault {

    logMsg("Starting ValidateLocalPermsInvalidName test");
    try {
      if (null == System.getSecurityManager()) {
        // security manager is NOT enabled and must be for these tests
        logMsg(SEC_MGR_WARNING);
        throw new Fault(SEC_MGR_WARNING);
      }

      if (!ejbref.validateLocalPermsInvalidName()) {
        throw new Fault("ValidateLocalPermsInvalidName test failed");
      }
    } catch (Exception e) {
      throw new Fault("ValidateLocalPermsInvalidName test failed:", e);
    }

    logMsg("SUCCESS:  ValidateLocalPermsInvalidName Passed");
  }

  /*
   * @testName: ValidateLocalPermsInvalidNameInACC
   *
   * @assertion_ids: JavaEE:SPEC:290; JavaEE:SPEC:296;
   *
   * @test_Strategy: This validates that a perm (PropertyPermission) that is
   * bundled with the app. Additionally, we want to verify the perm for
   * class=PropertyPermission does NOT pass the access check since we will be
   * trying to access a non-existing/invalid named perm. Then, as a sanity
   * check, access a perm using a name that IS validly defined in
   * permissions.xml (but not defined at any higher appserver level).
   *
   * This tests the following conditions: - NO permission declared in app server
   * nor in permissions.xml for PropertyPermission named "NonExistingName" w/
   * READ action (should get AccessControlException since no grant exists for
   * this) - using locally declared Permission impl (SecurityPermission named
   * "CTSPermission_second_name") (should be allowed to pass AccessController
   * check for this.) - have NO declared grant at higher app server level (e.g.
   * server.policy etc) - within ejb, use AccessController.checkPermission() to
   * confirm our permissons are treated as expected
   *
   */
  public void ValidateLocalPermsInvalidNameInACC() throws Fault {

    logMsg("Starting ValidateLocalPermsInvalidNameInACC test");
    try {
      if (null == System.getSecurityManager()) {
        // security manager is NOT enabled and must be for these tests
        logMsg(SEC_MGR_WARNING);
        throw new Fault(SEC_MGR_WARNING);
      }

      // call a priviledged method that does not exist
      SecurityPermission readPropertyPerm = new SecurityPermission(
          "NonExistingName");

      try {
        doCheckPermission(readPropertyPerm);
        // should NOT get here
        throw new Fault(
            "FAILURE:  ValidateLocalPermsInvalidNameInACC() did not throw expected AccessControlException.");
      } catch (AccessControlException ex) {
        // should get here.
        logMsg(
            "SUCCESS:  ValidateLocalPermsInvalidNameInACC() threw expected AccessControlException");
      } catch (NullPointerException ex) {
        throw new Fault(
            "FAILURE:  ValidateLocalPermsInvalidNameInACC() threw NullPointerException");
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
        logMsg(
            "CTSPermission_second_name permission property granted AccessControlException.");
      } catch (AccessControlException ex) {
        // should NOT get here
        throw new Fault(
            "FAILURE:  ValidateLocalPermsInvalidNameInACC() threw unexpected exception.");
      } catch (Exception ex) {
        // should NOT get here
        ex.printStackTrace();
        throw new Fault(
            "FAILURE:  ValidateLocalPermsInvalidNameInACC had unexpeted exception.");
      }

    } catch (Exception e) {
      throw new Fault("ValidateLocalPermsInvalidNameInACC test failed:", e);
    }

    logMsg("SUCCESS:  ValidateLocalPermsInvalidNameInACC Passed");
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

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }
}
