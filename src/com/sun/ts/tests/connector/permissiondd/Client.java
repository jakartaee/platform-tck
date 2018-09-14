/*
 * Copyright (c) 2014, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.connector.permissiondd;

import java.io.*;
import java.util.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.connector.util.DBSupport;
import com.sun.ts.tests.common.connector.whitebox.TSDataSource;
import com.sun.ts.tests.common.connector.whitebox.TSConnection;

/*
 * IMPORTANT:
 *     These tests are similar to the servlet/ee/spec/security/permissiondd
 * tests as well as the ejb30/sec/permsxml.  And in all cases, these tests
 * will fail if the security manager is NOT enabled.  Security Manager
 * must be enabled for these tests to pass.
 *
 */
public class Client extends ServiceEETest implements Serializable {

  private TSNamingContext nctx = null;

  private TSConnection con = null;

  private String whitebox_permissiondd = null;

  private TSDataSource ds1 = null;

  private DBSupport dbutil = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: whitebox-permissiondd; whitebox-anno_no_md;
   *
   * @class.testArgs: -ap tssql.stmt
   *
   */
  public void setup(String[] args, Properties p) throws Fault {

    whitebox_permissiondd = p.getProperty("whitebox-permissiondd");

    logMsg("Using: " + whitebox_permissiondd);

    // Construct our DBSupport object. This object performs interactions
    // on a table, based on the properties object supplied.
    try {
      dbutil = new DBSupport();
    } catch (Exception e) {
      throw new Fault(
          "Exception constructing DBSupport object: " + e.getMessage(), e);
    }

    try {
      nctx = new TSNamingContext();
      ds1 = (TSDataSource) nctx.lookup(whitebox_permissiondd);
      TestUtil.logMsg("ds1 JNDI lookup: " + ds1);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg("Exception during JNDI lookup: " + e.getMessage());
    }
  }

  /*
   * @testName: testValidateCustomPerm
   *
   * @assertion_ids: JavaEE:SPEC:304; JavaEE:SPEC:293;
   * 
   *
   * @test_Strategy: This validates that a permission is bundled with the app
   * and that the app does have grants for that perm. This perm should be
   * granted at both: - configuration (config.vi) to add perm to appserver polcy
   * - in local permissions.xml This should validate we can have the perm
   * declared in both places and it still works. This tests the permission.xml
   * from within a rar by doing the following: a. check that
   * whitebox-permissiondd ran its tests and logged the proper success message.
   *
   */
  public void testValidateCustomPerm() throws Fault {
    Vector log = null;
    boolean b1 = false;

    // test that whitebox-permissiondd logs the following message
    String toCheck1 = "SUCCESS:  validateCustomPerm passed.";

    // Get connection object using no parameters. Container managed
    // signon.
    try {
      ds1.setLogFlag(true);
      con = ds1.getConnection();
      log = ds1.getStateLog();
      TestUtil.logMsg("Got connection and log for whitebox-permissiondd.");
    } catch (Exception e) {
      TestUtil.logMsg("Exception caught on creating connection.");
      throw new Fault(e.getMessage(), e);
    }

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
        break;
      }
    }

    if (b1) {
      TestUtil.logMsg("testValidateCustomPerm PASSED.");
    } else {
      if (!b1) {
        TestUtil.logMsg(
            "Failures can occur if you are not running with Security Manager enabled in your appserver.");
        throw new Fault("testValidateCustomPerm FAILED.");
      }
    }

  }

  /*
   * The test is commented as this test case is not supported by Java EE
   * Platform Specification
   *
   * testName: testValidateCustomPermFromAppServer
   *
   * @assertion_ids: JavaEE:SPEC:290; JavaEE:SPEC:293; JavaEE:SPEC:295;
   * JavaEE:SPEC:304;
   *
   * @test_Strategy: This is basically testing that when a permission is set
   * within the app server but NOT listed/set within the permissions.xml, our
   * deployed app will still be granted that permission. Thus omitting it from
   * the permissions.xml must not cause the permission to be denied.
   *
   * This validates that we have a particular grant under the following
   * conditions: - this is testing permissions.xml within a Servlet - using a
   * custom Permission defined wihtin app server (via initial config) (note:
   * this custom perm is CTSPermission1 named "ConnectorPermission1_name2") -
   * have NO declared grant for this (CTSPermission1_name2) in permissions.xml -
   * since the perm IS defined within the appserver but is not defined within
   * the local permissions.xml, the app componets must still have permission as
   * set within app server.
   *
   */
  public void testValidateCustomPermFromAppServer() throws Fault {
    Vector log = null;
    boolean b1 = false;

    // test that whitebox-permissiondd logs the following message
    String toCheck1 = "SUCCESS:  validateCustomPermFromAppServer passed.";

    // Get connection object using no parameters. Container managed
    // signon.
    try {
      ds1.setLogFlag(true);
      con = ds1.getConnection();
      log = ds1.getStateLog();
      TestUtil.logMsg("Got connection and log for whitebox-permissiondd.");
    } catch (Exception e) {
      TestUtil.logMsg("Exception caught on creating connection.");
      throw new Fault(e.getMessage(), e);
    }

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
        break;
      }
    }

    if (b1) {
      TestUtil.logMsg("testValidateCustomPermFromAppServer PASSED.");
    } else {
      if (!b1) {
        TestUtil.logMsg(
            "Failures can occur if you are not running with Security Manager enabled in your appserver.");
        throw new Fault("testValidateCustomPermFromAppServer FAILED.");
      }
    }
  }

  /*
   * @testName: testValidateLocalGrantForCustomPerm
   *
   * @assertion_ids: JavaEE:SPEC:292; JavaEE:SPEC:293; JavaEE:SPEC:303;
   * JavaEE:SPEC:304;
   * 
   *
   * @test_Strategy: This validates that we have a particular grant under the
   * following conditions: - this is testing permissions.xml within a Servlet -
   * using locally declared custom Permission (CTSPermission2) declared for
   * property "CTSPermission2_name"; note: CTSPermission2 does NOT have support
   * for actions. - have declared grant in permissions.xml - have NO declared
   * grant at higher app server level (e.g. server.policy etc) We should be
   * allowed access control.
   *
   */
  public void testValidateLocalGrantForCustomPerm() throws Fault {
    Vector log = null;
    boolean b1 = false;

    // test that whitebox-permissiondd logs the following message
    String toCheck1 = "SUCCESS:  validateLocalGrantForCustomPerm passed.";

    // Get connection object using no parameters. Container managed
    // signon.
    try {
      ds1.setLogFlag(true);
      con = ds1.getConnection();
      log = ds1.getStateLog();
      TestUtil.logMsg("Got connection and log for whitebox-permissiondd.");
    } catch (Exception e) {
      TestUtil.logMsg("Exception caught on creating connection.");
      throw new Fault(e.getMessage(), e);
    }

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
        break;
      }
    }

    if (b1) {
      TestUtil.logMsg("testValidateLocalGrantForCustomPerm PASSED.");
    } else {
      if (!b1) {
        TestUtil.logMsg(
            "Failures can occur if you are not running with Security Manager enabled in your appserver.");
        throw new Fault("testValidateLocalGrantForCustomPerm FAILED.");
      }
    }

  }

  /*
   * @testName: testValidateRequiredPermSet
   *
   * @assertion_ids: JavaEE:SPEC:290; JavaEE:SPEC:296; JavaEE:SPEC:291;
   * 
   *
   * @test_Strategy: This validates that the required set of perms are properly
   * granted so when we try to do our access checks, we expect to see NO
   * AccessControl exception returned. This validates that we have grants for
   * multiple perms under the following conditions: - this is testing
   * permissions.xml within a resource adapter - have locally declared grants in
   * permissions.xml - we shouldnt care if grants are declared (or not declared)
   * at the higher app server level (e.g. via server.policy) - This is
   * validating the following are properly granted thru permissions.xml:
   * RuntimePermission("loadLibrary.*") RuntimePermission("queuePrintJob")
   * SocketPermission("*", "connect") FilePermission("*", "read")
   * PropertyPermission("*", "read") note: there is a more comprehendive listing
   * in the Java EE7 spec, Table EE.6-2. We are testing a subset of those.
   *
   */
  public void testValidateRequiredPermSet() throws Fault {
    Vector log = null;
    boolean b1 = false;

    // test that whitebox-permissiondd logs the following message
    String toCheck1 = "SUCCESS:  validateRequiredPermSet passed.";

    // Get connection object using no parameters. Container managed
    // signon.
    try {
      ds1.setLogFlag(true);
      con = ds1.getConnection();
      log = ds1.getStateLog();
      TestUtil.logMsg("Got connection and log for whitebox-permissiondd.");
    } catch (Exception e) {
      TestUtil.logMsg("Exception caught on creating connection.");
      throw new Fault(e.getMessage(), e);
    }

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
        break;
      }
    }

    if (b1) {
      TestUtil.logMsg("testValidateRequiredPermSet PASSED.");
    } else {
      if (!b1) {
        TestUtil.logMsg(
            "Failures can occur if you are not running with Security Manager enabled in your appserver.");
        throw new Fault("testValidateRequiredPermSet FAILED.");
      }
    }

  }

  /*
   * @testName: testValidateMissingPermFails
   *
   * @assertion_ids: JavaEE:SPEC:289; JavaEE:SPEC:290; JavaEE:SPEC:304;
   * 
   * @test_Strategy: This validates that we are NOT granted a certain permission
   * and so when we try to do our access checks, we expect to see an
   * AccessControl exception returned. This validates that we have no grant
   * under the following conditions: - this is testing permissions.xml within a
   * resource adapter - using locally declared Permission impl (CTSPermission1)
   * - have NO declared grant in permissions.xml for locally defined permission
   * CTSPermission1 with name="CTSPermission2_name" - have NO declared grant at
   * higher app server level (e.g. server.policy etc) This validates that an
   * AccessControlException is properly thrown.
   *
   */
  public void testValidateMissingPermFails() throws Fault {
    Vector log = null;
    boolean b1 = false;

    // test that whitebox-permissiondd logs the following message
    String toCheck1 = "SUCCESS:  validateMissingPermFails passed.";

    // Get connection object using no parameters. Container managed
    // signon.
    try {
      ds1.setLogFlag(true);
      con = ds1.getConnection();
      log = ds1.getStateLog();
      TestUtil.logMsg("Got connection and log for whitebox-permissiondd.");
    } catch (Exception e) {
      TestUtil.logMsg("Exception caught on creating connection.");
      throw new Fault(e.getMessage(), e);
    }

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
        break;
      }
    }

    if (b1) {
      TestUtil.logMsg("testValidateMissingPermFails PASSED.");
    } else {
      if (!b1) {
        TestUtil.logMsg(
            "Failures can occur if you are not running with Security Manager enabled in your appserver.");
        throw new Fault("testValidateMissingPermFails FAILED.");
      }
    }

  }

  /*
   * @testName: testValidateRestrictedLocalPerm
   *
   * @assertion_ids: JavaEE:SPEC:292; JavaEE:SPEC:293; JavaEE:SPEC:303;
   * JavaEE:SPEC:304;
   *
   * @test_Strategy: This validates that we have a particular grant under the
   * following conditions: - this is testing permissions.xml within a resource
   * adapter - using our own perm (CTSPropertyPermission) that is referenced in
   * permission.xml and has read but not write assigned note:
   * CTSPropertyPermission has support for actions - have NO declared grants for
   * CTSPropertyPermission at higher app server level (e.g. server.policy etc)
   * so that it is ONLY bundled in this local app and ref'd in permission.xml
   *
   */
  public void testValidateRestrictedLocalPerm() throws Fault {
    Vector log = null;
    boolean b1 = false;

    // test that whitebox-permissiondd logs the following message
    String toCheck1 = "SUCCESS:  validateRestrictedLocalPerm passed.";

    // Get connection object using no parameters. Container managed
    // signon.
    try {
      ds1.setLogFlag(true);
      con = ds1.getConnection();
      log = ds1.getStateLog();
      TestUtil.logMsg("Got connection and log for whitebox-permissiondd.");
    } catch (Exception e) {
      TestUtil.logMsg("Exception caught on creating connection.");
      throw new Fault(e.getMessage(), e);
    }

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
        break;
      }
    }

    if (b1) {
      TestUtil.logMsg("testValidateRestrictedLocalPerm PASSED.");
    } else {
      if (!b1) {
        TestUtil.logMsg(
            "Failures can occur if you are not running with Security Manager enabled in your appserver.");
        throw new Fault("testValidateRestrictedLocalPerm FAILED.");
      }
    }

  }

  /*
   * @testName: testValidateLocalPermsInvalidName
   *
   * @assertion_ids: JavaEE:SPEC:292; JavaEE:SPEC:293; JavaEE:SPEC:303;
   * JavaEE:SPEC:304; JavaEE:SPEC:314;
   *
   * @test_Strategy: This validates that we have a particular grant under the
   * following conditions: - this is testing permissions.xml within a resource
   * adapter - using our own perm (CTSPropertyPermission) that is referenced in
   * permission.xml and has read but not write assigned note:
   * CTSPropertyPermission has support for actions - we have perm
   * (CTSPropertyPermission) WITH read action *but* the perm that is declared in
   * permissions.xml has a different name then what we are trying to validate in
   * our call to AccessController.checkPermission - so we expect
   * AccessControlException to be thrown - also, have NO declared grants for
   * CTSPropertyPermission at higher app server level (e.g. server.policy etc)
   * so that it is ONLY bundled in this local app and ref'd in permission.xml
   *
   */
  public void testValidateLocalPermsInvalidName() throws Fault {
    Vector log = null;
    boolean b1 = false;

    // test that whitebox-permissiondd logs the following message
    String toCheck1 = "SUCCESS:  validateLocalPermsInvalidName passed.";

    // Get connection object using no parameters. Container managed
    // signon.
    try {
      ds1.setLogFlag(true);
      con = ds1.getConnection();
      log = ds1.getStateLog();
      TestUtil.logMsg("Got connection and log for whitebox-permissiondd.");
    } catch (Exception e) {
      TestUtil.logMsg("Exception caught on creating connection.");
      throw new Fault(e.getMessage(), e);
    }

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
        break;
      }
    }

    if (b1) {
      TestUtil.logMsg("testValidateLocalPermsInvalidName PASSED.");
    } else {
      if (!b1) {
        TestUtil.logMsg(
            "Failures can occur if you are not running with Security Manager enabled in your appserver.");
        throw new Fault("testValidateLocalPermsInvalidName FAILED.");
      }
    }

  }

  /* cleanup */
  public void cleanup() throws Fault {
    ds1.clearLog();
    TestUtil.logMsg("Cleanup");
    try {
      TestUtil.logTrace("Closing connection in cleanup.");
      if (con != null) {
        con.close();
      }
    } catch (Exception sqle) {
      TestUtil.logErr("Exception on cleanup: " + sqle.getMessage(), sqle);
    }
  }
}
