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
 * @(#)Client.java	1.17 03/05/16
 */

package com.sun.ts.tests.ejb.ee.sec.bmp.secrunaspropagation;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import java.io.*;
import java.util.*;
import javax.ejb.*;
import com.sun.javatest.Status;

import com.sun.ts.tests.ejb.ee.sec.bmp.common.*;

public class Client extends EETest {

  // JNDI names for looking up ejbs
  private static final String ejbname = "java:comp/env/ejb/Test";

  // references to ejb interfaces
  private TestHome ejbhome = null;

  private Test ejbref = null;

  // Security role references.
  private static final String emp_secrole_ref = "EMP";

  private static final String admin_secrole_ref = "ADMIN";

  private static final String mgr_secrole_ref = "MGR";

  private static final String UserNameProp = "user";

  private static final String UserPasswordProp = "password";

  private static final String AuthUser = "authuser";

  private String authuser = "";

  private String username = "";

  private String password = "";

  private Properties props = null;

  private TSNamingContext nctx = null;

  private TSLoginContext lc = null;

  private boolean newTable = true;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: user; password; authuser; user1; password1;
   * 
   * @class.testArgs: -ap tssql.stmt
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;

    try {
      authuser = props.getProperty(AuthUser);
      username = props.getProperty(UserNameProp);
      password = props.getProperty(UserPasswordProp);

      nctx = new TSNamingContext();

      lc = new TSLoginContext();
      lc.login(username, password);

      ejbhome = (TestHome) nctx.lookup(ejbname, TestHome.class);

    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /*
   * @testName: test1
   * 
   * @assertion_ids: EJB:SPEC:827
   * 
   * @test_Strategy: 1. Create an entity bmp bean TestEJB with runas identity
   * set to <Manager> 2. Create another entity bmp bean SecTestEJB with method
   * EjbIsAuthz 3. Protect the method with multiple security roles including
   * <Manager> 4. Call the bean TestEJB as a principal <username,password>.
   * Which then invokes the method of the bean SecTestEJB. 5. Since then TestEJB
   * uses runas identity, <Manager>, which is one of security roles set on the
   * method permission, so access to the method EjbIsAuthz should be allowed. 6.
   * Verify call returns successfully.
   */

  public void test1() throws Fault {
    logMsg("Starting Caller authorization test");
    try {
      ejbref = ejbhome.create(props, newTable, 1, "coffee-1", 1);

      if (!ejbref.EjbIsAuthz(props))
        throw new Fault("Caller authorization test failed");
      logMsg("Caller authorization test passed");
    } catch (Exception e) {
      throw new Fault("Caller authorization test failed: ", e);
    }
  }

  /*
   * @testName: test2
   * 
   * @assertion_ids: EJB:SPEC:811
   *
   * @test_Strategy: 1. Create an entity bmp bean TestEJB with runas identity
   * set to <Manager> 2. Create another entity bmp bean SecTestEJB with method
   * EjbNotAuthz 3. Protect the method with security role <Administrator> 4.
   * Call the bean TestEJB as a principal <username,password>. Which then
   * invokes the method of the bean SecTestEJB. 5. Since then TestEJB uses runas
   * identity, <Manager>, which does not share any principals with role
   * <Administrator>. so access to the method EjbNotAuthz shouldnot be allowed.
   * 6. Verify java.rmi.RemoteException is generated.
   */

  public void test2() throws Fault {
    logMsg("Starting No caller authorization test");
    try {
      ejbref = ejbhome.create(props, newTable, 1, "coffee-1", 1);

      if (!ejbref.EjbNotAuthz(props))
        throw new Fault("No caller authorization test failed");
      logMsg("No authorization test passed");
    } catch (Exception e) {
      throw new Fault("No caller authorization test failed:", e);
    }
  }

  /*
   * @testName: test3
   * 
   * @assertion_ids: EJB:SPEC:61.7; EJB:SPEC:81.4
   *
   * @test_Strategy: 1. Create an entity bmp bean TestEJB with runas identity
   * set to <Manager> 2. Create another entity bmp bean SecTestEJB with method
   * EjbSecRoleRef. 3. Protect the method with security role <Employee>, Link a
   * security role ref - emp_secrole_ref to role <Employee>. 4. Call the bean
   * TestEJB as a principal <username,password>. Which then invokes the method
   * of the bean SecTestEJB. 5. Since then TestEJB uses runas identity,
   * <Manager>, who'e principals also in role <Employee> so access to the method
   * of bean SecTestEJB should be allowed. 6. method calls
   * isCallerInRole(emp_secrole_ref) and returns return value. 7. Verify return
   * value is true.
   *
   * Note: A security role reference name must be the security role name used in
   * isCallerInRole() api call as specified in EJB 2.0 specification, section
   * 21.2.5.3. This is a positive test.
   */

  public void test3() throws Fault {
    logMsg("Starting Security role reference positive test");
    try {
      ejbref = ejbhome.create(props, newTable, 1, "coffee-1", 1);

      if (!ejbref.EjbSecRoleRef(emp_secrole_ref, props))
        throw new Fault("Security role reference positive test failed");
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
   * @test_Strategy: 1. Create an entity bmp bean TestEJB with runas identity
   * set to <Manager> 2. Create another entity bmp bean SecTestEJB with method
   * EjbSecRoleRef. 3. Protect the method with security role <Employee>, Link a
   * security role ref - emp_secrole_ref to role <Employee>. 4. Call the bean
   * TestEJB as a principal <username,password>. Which then invokes the method
   * of the bean SecTestEJB. 5. Since then TestEJB uses runas identity,
   * <Manager>, so access to the method of bean SecTestEJB should be allowed. 6.
   * method calls isCallerInRole(admin_secrole_ref) and returns return value. 7.
   * Verify return value is false: roles <Manager> and <Administrator> don't
   * share any principals.
   *
   * Note: A security role reference name must be the security role name used in
   * isCallerInRole() api call as specified in EJB 2.0 specification, section
   * 21.2.5.3. This is a negative test.
   */

  public void test4() throws Fault {
    logMsg("Starting Security role reference negative test");
    try {
      ejbref = ejbhome.create(props, newTable, 1, "coffee-1", 1);

      if (!ejbref.EjbSecRoleRef1(admin_secrole_ref, props))
        throw new Fault("Security role reference negative test failed");
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
   * @test_Strategy: 1. Create an entity bmp bean TestEJB with runas identity
   * set to <Manager> 2. Create two more entity bmp beans SecTestEJB and
   * SecTestRoleRefEJB 3. Link security role reference (emp_secrole_ref) to
   * role1 in ejb1 and role2 in ejb2. 4. Ensure caller principal is in role1 but
   * not in role2. 5. Invoke method in SecTestEJB that returns value of
   * isCallerInRole(roleref). Verify return value is true. 6. Invoke method in
   * SecTestRoleRefEJB that returns value of isCallerInRole(roleref). Verify
   * return value is false.
   * 
   * Note: Security role references are scoped to a bean as specified in EJB
   * Specification 21.2.5.3 . So two ejbs in the same application can each
   * define an identical security role reference that is linked to different
   * security roles in each ejb.
   */

  public void test5() throws Fault {
    logMsg("Starting Security role reference scope test");
    try {
      ejbref = ejbhome.create(props, newTable, 1, "coffee-1", 1);

      // caller must be in security role linked to emp_secrole_ref. call must
      // succeed.
      if (!ejbref.EjbSecRoleRefScope(emp_secrole_ref, props))
        throw new Fault("Security role reference scope test failed");
      logMsg("Security role reference scope test passed");
    } catch (Exception e) {
      throw new Fault("Security role reference scope test failed: ", e);
    }
  }

  /*
   * @testName: test6
   * 
   * @assertion_ids: EJB:SPEC:827
   * 
   * @test_Strategy: 1. Create an entity bmp bean TestEJB with runas identity
   * set to <Manager> 2. Create another entity bmp beans SecTestEJB with
   * overloaded methods. 3. Call method1 passing emp_secrole_ref. 4. Method1
   * returns isCallerInRole(emp_secrole_ref) which must be true. 5. Call method2
   * passing two role references as parameters. 6. Method must return false (
   * caller not in both security role refs).
   * 
   * NOTE: EJB Specification v2.0 specifies that java.rmi.RemoteException is
   * returned not only for authorization failures but also for communicaton
   * errors. The test may pass instead of failing if the exception is generated
   * because of a communication failure.
   */

  public void test6() throws Fault {
    logMsg("Starting Overloaded security role references test");
    try {
      ejbref = ejbhome.create(props, newTable, 1, "coffee-1", 1);

      if (!ejbref.EjbOverloadedSecRoleRefs(emp_secrole_ref, admin_secrole_ref,
          props))
        throw new Fault("Overloaded security role references test failed");

      logMsg("Overloaded security role references test passed");
    } catch (Exception e) {
      throw new Fault("Overloaded security role references test failed", e);
    }
  }

  /*
   * @testName: test7
   * 
   * @assertion_ids: EJB:SPEC:827
   * 
   * @test_Strategy: 1. Create an entity bmp bean TestEJB with runas identity
   * set to <Manager> 2. Create another entity bmp beans SecTestEJB. 3.
   * SecTestEJB returns isCallerInRole(mgr_secrole_ref) which must be true.
   *
   * Note: "The unchecked element specifies that a method is not checked for
   * authorization by the container prior to invocation of the method."
   *
   * This test tests that when appclient Client invokes a remote interface
   * method, which has method permission unchecked, access will be allowed.
   */

  public void test7() throws Fault {
    logMsg("Starting caller in role positive test in B2");
    try {
      ejbref = ejbhome.create(props, newTable, 1, "coffee-1", 1);

      if (!ejbref.InRole(mgr_secrole_ref, props))
        throw new Fault("caller in role positive test failed");
      logMsg("caller in role positive test passed");
    } catch (Exception e) {
      throw new Fault(" tesn role positive failed:", e);
    }
  }

  /*
   * @testName: test8
   * 
   * @assertion_ids: EJB:SPEC:61.8
   * 
   * @test_Strategy: 1. Create an entity bmp bean TestEJB with runas identity
   * set to <Manager> 2. Create another entity bmp beans SecTestEJB. 3.
   * SecTestEJB returns isCallerInRole(admin_secrole_ref) which must be false.
   * 
   * Note: A security role reference name must be the security role name used in
   * isCallerInRole() api call as specified in EJB 2.0 specification, section
   * 21.2.5.3. This is a negative test.
   */
  public void test8() throws Fault {
    logMsg("Starting caller in role negative test");
    try {
      ejbref = ejbhome.create(props, newTable, 1, "coffee-1", 1);

      if (ejbref.InRole(admin_secrole_ref, props))
        throw new Fault("caller in role negative test failed");
      logMsg("caller in role negative test passed");
    } catch (Exception e) {
      throw new Fault("caller in role negative test failed:", e);
    }
  }

  /*
   * @testName: test9
   * 
   * @assertion_ids: EJB:SPEC:796
   * 
   * @test_Strategy: 1. Create an entity bmp bean TestEJB with runas identity
   * set to <Manager> 2. Verify that TestEJB returns the correct
   * GetCallerPrincipal() which should not be affected by the bean using runas
   * identity.
   *
   * Note: getCallerPrincipal() returns the principal represent the caller of
   * the EJB, not the principal that correspond to the runas security identity
   * for the bean.
   *
   */

  public void test9() throws Fault {
    logMsg("Starting first bean's getCallerPrincipal test");
    try {
      ejbref = ejbhome.create(props, newTable, 1, "coffee-1", 1);

      if (!ejbref.IsCallerB1(username))
        throw new Fault("first bean's getCallerPrincipal test failed");
      logMsg("first bean's getCallerPrincipal test passed");
    } catch (Exception e) {
      throw new Fault("first bean's getCallerPrincipal test failed:", e);
    }
  }

  /*
   * @testName: test10
   *
   * @assertion_ids: EJB:SPEC:796
   * 
   * @test_Strategy: 1. Create an entity bmp bean TestEJB with runas identity
   * set to <Manager> 2. Create another entity bmp bean SecTestEJB. 3. Verify
   * that SecTestEJB returns the correct GetCallerPrincipal() which is the
   * principal using runas identity, but not the principal invoked TestEJB.
   *
   * Note: getCallerPrincipal() returns the principal represent the caller of
   * the EJB, not the principal that correspond to the runas security identity
   * for the bean.
   */

  public void test10() throws Fault {
    logMsg("Starting second bean's getCallerPrincipal test");
    try {
      ejbref = ejbhome.create(props, newTable, 1, "coffee-1", 1);

      if (ejbref.IsCallerB2(username, props))
        throw new Fault("second bean's getCallerPrincipal test failed");

      if (!ejbref.IsCallerB2(authuser, props))
        throw new Fault("second bean's getCallerPrincipal test failed");

      logMsg("second bean's getCallerPrincipal test passed");
    } catch (Exception e) {
      throw new Fault("second bean's getCallerPrincipal test failed:", e);
    }
  }

  /*
   * @testName: test11
   * 
   * @assertion_ids: EJB:SPEC:827
   * 
   * @test_Strategy: 1. Create a bmp entity bean with runas identity invokes a
   * remote method of the second bean. 2. Have this method with method
   * permission unchecked 3. Verify that access is allowed.
   * 
   * Note: "The unchecked element specifies that a method is not checked for
   * authorization by the container prior to invocation of the method."
   *
   * This test tests that when appclient Client invokes a remote interface
   * method, which has method permission unchecked, access will be allowed.
   */

  public void test11() throws Fault {
    logMsg("Starting unchecked test1");
    try {
      ejbref = ejbhome.create(props, newTable, 1, "coffee-1", 1);
      if (!ejbref.checktest1(props)) {
        logErr("unchecked test returned false");
        throw new Fault("unchecked test1 failed");
      }
      logMsg("unchecked test1 passed.");

    } catch (Exception e) {
      throw new Fault("unchecked test1 failed", e);
    }
  }

  /*
   * @testName: test12
   * 
   * @assertion_ids: EJB:SPEC:808
   *
   * @test_Strategy: 1. Create a bmp entity bean invokes a remote method of the
   * second bean. 2. Set the first bean to use runas identity. 3. Put the method
   * of the second bean on exclude-list. 4. Verify java.rmi.RemoteException is
   * generated. Note: "The exclude-list element defines a set of methods which
   * the assembler marks to be uncallable. It contains one or more methods. If
   * the method permission relation contains methods that are in the exclude
   * list, the deployer should consider those methods to be uncallable."
   *
   * This test tests when appclient invokes an ejb's remote interface method in
   * the exclude list, java.rmi.RemoteException will be thrown.
   */
  public void test12() throws Fault {
    logMsg("Starting exclude-list test1");
    try {
      ejbref = ejbhome.create(props, newTable, 1, "coffee-1", 1);
      if (!ejbref.excludetest1(props)) {
        logErr("excludetest1 returned false");
        throw new Fault("excludetest1 failed");
      }
      logMsg("exclude-list test1 passed");
    } catch (Exception e) {
      throw new Fault("exclude-list test1 failed:", e);
    }
  }

  public void cleanup() throws Fault {
    try {
      if (ejbref != null)
        ejbref.remove();
    } catch (Exception e) {
      logErr("Cleanup failed: ", e);
    }
    logMsg("cleanup ok");
  }
}
