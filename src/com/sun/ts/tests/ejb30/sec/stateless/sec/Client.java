/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

/*
 * $Id$
 */

package com.sun.ts.tests.ejb30.sec.stateless.sec;

import jakarta.ejb.EJB;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.porting.TSLoginContext;
import java.util.Properties;
import com.sun.javatest.Status;
import com.sun.ts.tests.ejb30.sec.stateless.common.SecTest;
import com.sun.ts.tests.ejb30.sec.stateless.common.SecTestRoleRef;
import jakarta.ejb.EJBException;
// EJBException is used in  line number 95 and 300 to satisfy compiler
// the right exception is jakarta.ejb.EJBAccessException

public class Client extends EETest {

  // references to ejb interfaces
  @EJB(beanName = "SecTestEJB")
  static private SecTest ejb1ref = null;

  @EJB(beanName = "SecTestRoleRefEJB")
  static private SecTestRoleRef ejb2ref = null;

  // Security role references.
  private static final String emp_secrole_ref = "EMP";

  private static final String admin_secrole_ref = "ADMIN";

  private static final String mgr_secrole_ref = "MGR";

  private static final String testDir = System.getProperty("user.dir");

  private static final String UserNameProp = "user";

  private static final String UserPasswordProp = "password";

  private String username = "";

  private String password = "";

  private Properties props = null;

  private TSLoginContext lc = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: user; password;
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;

    try {
      username = TestUtil.getProperty(UserNameProp);
      password = TestUtil.getProperty(UserPasswordProp);

      lc = new TSLoginContext();
      lc.login(username, password);

    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /*
   * @testName: test1
   * 
   * @assertion_ids: EJB:SPEC:827; JavaEE:SPEC:30
   * 
   * @test_Strategy: 1. Create a stateless session bean with a method. 2.
   * Protect bean method with multiple security roles. 3. Call bean method as a
   * principal who is not in any of the security roles that protects the method.
   * 4. Verify jakarta.ejb.EJBAccessException is generated.
   */

  public void test1() throws Fault {
    logMsg("Starting No caller authorization test");
    try {
      ejb1ref.EjbNotAuthz();
      logErr(
          "Method call did not generate an expected java.rmi.RemoteException");
      throw new Fault("No caller authorization test failed");
    } catch (jakarta.ejb.EJBException e) {
      logMsg("Caught jakarta.ejb.EJBException as expected");
      logMsg("No authorization test passed");
    } catch (Exception e) {
      throw new Fault("No caller authorization test failed:", e);
    }
  }

  /*
   * @testName: test2
   * 
   * @assertion_ids: EJB:SPEC:827; JavaEE:SPEC:26; JavaEE:SPEC:30
   *
   * @test_Strategy: 1. Create a stateless session bean with a method. 2.
   * Protect a method within the bean with multiple security roles. 3. Call bean
   * method as a principal who is in one some of the security roles but not in
   * others. 4. Verify call returns successfully.
   */

  public void test2() throws Fault {
    logMsg("Starting Caller authorization test");
    try {
      if (ejb1ref.EjbIsAuthz())
        logMsg("Caller authorization test passed");
      else
        throw new Fault("Caller authorization test failed");
    } catch (Exception e) {
      throw new Fault("Caller authorization test failed: ", e);
    }
  }

  /*
   * @testName: test3
   * 
   * @assertion_ids: EJB:SPEC:61.7; EJB:SPEC:81.4
   *
   * @test_Strategy: 1. Create a stateless session bean. 2. Protect a method in
   * the bean using role1 as the security role. 3. Link a security role ref -
   * emp_secrole_ref - to role1 in the bean. 4. Invoke the method with
   * emp_secrole_ref as parameter. 5. bean calls isCallerInRole(emp_secrole_ref)
   * and returns return value. 6. Verify return value is true.
   */

  public void test3() throws Fault {
    logMsg("Starting Security role reference positive test");
    try {
      if (!ejb1ref.EjbSecRoleRef(emp_secrole_ref)) {
        logErr("EjbSecRoleRef(" + emp_secrole_ref + ") returned false");
        throw new Fault("Security role reference positive test failed");
      } else
        logMsg("Security role reference positive test passed");
    } catch (Exception e) {
      throw new Fault("Security role reference positive test failed: ", e);
    }
  }

  /*
   * @testName: test4
   * 
   * @assertion_ids: EJB:SPEC:61.8
   *
   * @test_Strategy: 1. Create a stateless session bean. 2. Protect a method in
   * the bean using a security role - role1. 3. Link a security role ref -
   * emp_secrole_ref - to role1 in the bean. 4. Invoke the method with
   * mgr_secrole_ref as a parameter. 5. bean calls
   * isCallerInRole(mgr_secrole_ref) and returns return value. 6. Verify return
   * value is false.
   */

  public void test4() throws Fault {
    logMsg("Starting Security role reference negative test");
    try {
      if (ejb1ref.EjbSecRoleRef(mgr_secrole_ref)) {
        logErr("EjbSecRoleRef(" + mgr_secrole_ref + ") returned true");
        throw new Fault("Security role reference negative test failed");
      }
      logMsg("Security role reference negative test passed");
    } catch (Exception e) {
      throw new Fault("Security role reference negative test failed: ", e);
    }
  }

  /*
   * @testName: test5
   * 
   * @assertion_ids: EJB:SPEC:799; EJB:SPEC:804
   *
   * @test_Strategy: 1. Create two stateless session beans - SecTestEJB and
   * SecTestRoleRefEJB. 2. Link security role reference (roleref) to role1 in
   * SecTestEJB and role2 in SecTestRoleRefEJB. 3. Ensure caller principal is in
   * role1 but not in role2. 4. Invoke method in SecTestEJB that returns value
   * of isCallerInRole(roleref). Verify return value is true. 5. Invoke method
   * in SecTestRoleRefEJB that returns value of isCallerInRole(roleref). Verify
   * return value is false.
   */

  public void test5() throws Fault {
    logMsg("Starting Security role reference scope test");
    try {
      // caller must be in security role linked to emp_secrole_ref. call must
      // succeed.
      if (!ejb1ref.EjbSecRoleRef(emp_secrole_ref)) {
        logErr("isCallerInRole(" + emp_secrole_ref + ") returned false");
        throw new Fault("Security role reference scope test failed");
      }
      logMsg("(ejb1) isCallerInRole(" + emp_secrole_ref
          + ") returned true as expected");
    } catch (Exception e) {
      throw new Fault("Security role reference scope test failed: ", e);
    }

    try {
      // caller not in security role linked to emp_secrole_ref. call should fail
      if (ejb2ref.EjbSecRoleRefScope(emp_secrole_ref)) {
        logMsg("isCallerInRole(" + emp_secrole_ref + ") returned true");
        throw new Fault("Security role reference scope test failed");
      }
      logMsg("(ejb2) isCallerInRole(" + emp_secrole_ref
          + ") returned false as expected");
      logMsg("Security role reference scope test passed");
    } catch (Exception e) {
      throw new Fault("Security role reference scope test failed: ", e);
    }
  }

  /*
   * @testName: test6
   * 
   * @assertion_ids: EJB:SPEC:811
   *
   * @test_Strategy: 1. Create a stateless session bean with overloaded methods.
   * 2. Call method1 passing emp_secrole_ref. 3. Method returns
   * isCallerInRole(emp_secrole_ref) which must be true. 4. Call method2 passing
   * two role references as parameters. 5. Method must return false ( caller not
   * in both security role refs).
   */

  public void test6() throws Fault {
    logMsg("Starting Overloaded security role references test");
    try {
      if (!ejb1ref.EjbOverloadedSecRoleRefs(emp_secrole_ref)) {
        logErr(
            "EjbOverloadedSecRoleRefs(" + emp_secrole_ref + ") returned false");
        throw new Fault("Overloaded security role references test failed");
      }
      logMsg("EjbOverloadedSecRoleRefs(" + emp_secrole_ref
          + ") returned true as expected");

      if (ejb1ref.EjbOverloadedSecRoleRefs(emp_secrole_ref, mgr_secrole_ref)) {
        logErr("EjbOverloadedRoleRefs(" + emp_secrole_ref + ","
            + mgr_secrole_ref + ") returned true");
        throw new Fault("Overloaded security role references test failed");
      }
      logMsg("EjbOverloadedSecRoleRefs(" + emp_secrole_ref + ","
          + mgr_secrole_ref + ") returned false as expected");

    } catch (Exception e) {
      throw new Fault("Overloaded security role references test failed", e);
    }
    logMsg("Overloaded security role references test passed");
  }

  /*
   * @testName: test7
   * 
   * @assertion_ids: EJB:SPEC:827; JavaEE:SPEC:30
   *
   * @test_Strategy: 1. Create a stateless session bean with a remote interface
   * method permission unchecked 2. Verify that access is allowed.
   */

  public void test7() throws Fault {
    logMsg("Starting unchecked test1");
    try {
      if (!ejb1ref.checktest1()) {
        logErr("unchecked test returned false");
        throw new Fault("unchecked test1 failed");
      }
      logMsg("unchecked test1 passed.");

    } catch (Exception e) {
      throw new Fault("unchecked test1 failed", e);
    }
  }

  /*
   * @testName: test8
   * 
   * @assertion_ids: EJB:SPEC:808
   *
   * @test_Strategy: 1. Create a stateless session bean with a remote method on
   * exclude-list. 2. Verify java.rmi.RemoteException is generated when the
   * method is invoked.
   */

  public void test8() throws Fault {
    logMsg("Starting exclude-list test1");
    try {
      ejb1ref.excludetest1();
    } catch (jakarta.ejb.EJBException e) {
      logMsg("Caught java.rmi.RemoteException as expected");
      logMsg("exclude-list test1 passed");
    } catch (Exception e) {
      throw new Fault("exclude-list test1 failed:", e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }
}
