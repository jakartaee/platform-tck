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

/*
 * $Id$
 */

package com.sun.ts.tests.ejb30.sec.stateful.lsecr;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.porting.TSLoginContext;
import com.sun.javatest.Status;
import com.sun.ts.tests.ejb30.sec.stateful.common.lTest;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import java.util.Properties;

public class Client extends EETest {

  // references to ejb interfaces
  @EJB(beanName = "lTestEJB")
  static private lTest ejbref = null;

  // Security role references.
  private static final String emp_secrole = "EMP";

  private static final String admin_secrole = "ADMIN";

  private static final String mgr_secrole = "MGR";

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
      username = props.getProperty(UserNameProp);
      password = props.getProperty(UserPasswordProp);
      lc = new TSLoginContext();
      lc.login(username, password);

    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /*
   * @testName: test1
   * 
   * @assertion_ids: EJB:SPEC:827; EJB:SPEC:822; EJB:SPEC:817; EJB:SPEC:815;
   * EJB:SPEC:814; EJB:SPEC:786; EJB:SPEC:785
   *
   * @test_Strategy: 1. Create a stateful session bean using run-as identity
   * accessing a method of a second bean. 2. Protect the method within the bean
   * with multiple security roles. 3. Call the method as a principal in run-as
   * identity which is in one of the security roles but not in others. 4. Verify
   * call returns successfully.
   */

  public void test1() throws Fault {
    logMsg("Starting Caller authorization test");
    try {
      if (!ejbref.EjbIsAuthz())
        throw new Fault("Caller authorization test failed");
      logMsg("Caller authorization test passed");
    } catch (Exception e) {
      throw new Fault("Caller authorization test failed: ", e);
    }
  }

  /*
   * @testName: test3
   *
   * @assertion_ids: EJB:SPEC:61.7; EJB:SPEC:81.4; EJB:SPEC:827; EJB:SPEC:822;
   * EJB:SPEC:817; EJB:SPEC:815; EJB:SPEC:814; EJB:SPEC:786; EJB:SPEC:785
   *
   * @test_Strategy: 1. Create a stateful session bean using run-as identity
   * accessing a method of a second bean. 2. Protect the method using a security
   * role (role1). while run-as identity is in. 3. Link a security role name -
   * emp_secrole - to role1 in the bean. 4. Invoke the method with emp_secrole
   * as parameter. 5. bean calls isCallerInRole(emp_secrole) and returns return
   * value. 6. Verify return value is true.
   */

  public void test3() throws Fault {
    logMsg("Starting Security role reference positive test");
    try {
      if (!ejbref.EjbSecRoleRef(emp_secrole))
        throw new Fault("Security role reference positive test failed");
      logMsg("Security role reference positive test passed");
    } catch (Exception e) {
      throw new Fault("Security role reference positive test failed: ", e);
    }
  }

  /*
   * @testName: test4
   * 
   * @assertion_ids: EJB:SPEC:61.8; EJB:SPEC:786
   *
   * @test_Strategy: 1. Create a stateful session bean using run-as identity
   * accessing a method of a second bean. 2. Protect the method with multiple
   * roles include role1, while run-as identity is in. 3. Link a security role
   * name - mgr_secrole - to role1 in the bean. 4. Invoke the method with
   * admin_secrole as a parameter. 5. bean calls isCallerInRole(admin_secrole)
   * and returns return value. 6. Verify return value is false.
   */

  public void test4() throws Fault {
    logMsg("Starting Security role reference negative test");
    try {
      if (!ejbref.EjbSecRoleRef1(admin_secrole))
        throw new Fault("Security role reference negative test failed");
      logMsg("Security role reference negative test passed");
    } catch (Exception e) {
      throw new Fault("Security role reference negative test failed: ", e);
    }
  }

  /*
   * @testName: test5
   * 
   * @assertion_ids: EJB:SPEC:799;EJB:SPEC:804
   *
   * @test_Strategy: 1. Create a stateful session bean using run-as identity
   * accessing a method of a second bean. 2. Link security role name (roleref)
   * to role1 in ejb1 and role2 in ejb2. 3. Ensure run-as identity is in role1
   * but not in role2. 4. Invoke method in ejb1 that returns value of
   * isCallerInRole(roleref). Verify return value is true. 5. Invoke method in
   * ejb2 that returns value of isCallerInRole(roleref). Verify return value is
   * false.
   */

  public void test5() throws Fault {
    logMsg("Starting Security role reference scope test");
    try {
      // caller must be in security role linked to emp_secrole. call must
      // succeed.
      if (!ejbref.EjbSecRoleRefScope(emp_secrole))
        throw new Fault("Security role reference scope test failed");
      logMsg("Security role reference scope test passed");
    } catch (Exception e) {
      throw new Fault("Security role reference scope test failed: ", e);
    }
  }

  /*
   * @testName: test2
   * 
   * @assertion_ids: EJB:SPEC:811
   *
   * @test_Strategy: 1. Create a stateful session bean using run-as identity
   * accessing a second bean with overloaded methods: method1 and method2. 2.
   * Call method1 passing emp_secrole, which run-as identity is in. 3. Method1
   * returns isCallerInRole(emp_secrole) which must be true. 4. Call method2
   * passing two role references as parameters. 5. Method must return false.
   * (Make sure run-as identity not in both security roles)
   */

  public void test2() throws Fault {
    logMsg("Starting Overloaded security role references test");
    try {
      if (!ejbref.EjbOverloadedSecRoleRefs(emp_secrole, admin_secrole))
        throw new Fault("Overloaded security role references test failed");

      logMsg("Overloaded security role references test passed");
    } catch (Exception e) {
      throw new Fault("Overloaded security role references test failed", e);
    }
  }

  /*
   * @testName: test6
   * 
   * @assertion_ids: EJB:SPEC:647.5 ; EJB:SPEC:827
   *
   * @test_Strategy: 1. Create a stateful session bean using run-as identity
   * accessing a method of a second bean. 2. Protect the second bean's method
   * with multiple security roles. 3. Call the method as run-as identity which
   * is not in any of the security roles that protects the method. 4. Verify
   * javax.ejb.EJBException is generated.
   */

  public void test6() throws Fault {
    logMsg("Starting No caller authorization test");
    try {
      if (!ejbref.EjbNotAuthz())
        throw new Fault("No caller authorization test failed");
      logMsg("No authorization test passed");
    } catch (Exception e) {
      throw new Fault("No caller authorization test failed:", e);
    }
  }

  /*
   * @testName: test7
   * 
   * @assertion_ids: EJB:SPEC:827
   *
   * @test_Strategy: 1. Create a stateful session bean invokes a local method of
   * the second bean. 2. Have this local method with method permission unchecked
   * 3. Verify that access is allowed.
   */

  public void test7() throws Fault {
    logMsg("Starting unchecked test1");
    try {
      if (!ejbref.checktest1()) {
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
   * @test_Strategy: 1. Create a stateful session bean using runas identity
   * invokes a local method of the second bean. 2. Put this local method on
   * exclude-list. 3. Verify javax.ejb.EJBException is generated.
   */

  public void test8() throws Fault {
    logMsg("Starting exclude-list test1");
    try {
      if (!ejbref.excludetest1()) {
        logErr("excludetest1 returned false");
        throw new Fault("excludetest1 failed");
      }
      logMsg("exclude-list test1 passed");
    } catch (Exception e) {
      throw new Fault("exclude-list test1 failed:", e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }
}
