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

package com.sun.ts.tests.interop.security.appclient.bmp;

import com.sun.ts.tests.ejb.ee.sec.bmp.common.*;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import java.io.*;
import java.util.*;
import javax.ejb.*;
import com.sun.javatest.Status;

public class Client extends EETest {

  // JNDI names for looking up ejbs
  private static final String ejb1name = "java:comp/env/ejb/SecTest";

  private static final String ejb2name = "java:comp/env/ejb/SecTestRoleRef";

  // references to ejb interfaces
  private SecTestHome ejb1home = null;

  private SecTest ejb1ref = null;

  private SecTestRoleRefHome ejb2home = null;

  private SecTestRoleRef ejb2ref = null;

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

  private TSNamingContext nctx = null;

  private TSLoginContext lc = null;

  private boolean newTable = true;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: user; password; user1; password1;
   * 
   * @class.testArgs: -ap tssql.stmt
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;

    try {
      username = props.getProperty(UserNameProp);
      password = props.getProperty(UserPasswordProp);

      nctx = new TSNamingContext();

      lc = new TSLoginContext();
      lc.login(username, password);

      ejb1home = (SecTestHome) nctx.lookup(ejb1name, SecTestHome.class);
      ejb2home = (SecTestRoleRefHome) nctx.lookup(ejb2name,
          SecTestRoleRefHome.class);

    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /*
   * @testName: test1
   * 
   * @assertion_ids: EJB:SPEC:827
   *
   * @test_Strategy: 1. Create an entity bean accessing a method from another
   * bean deployed on another J2EE server. 2. Protect bean method with multiple
   * security roles. 3. Call bean method as a principal who is not in any of the
   * security roles that protects the method. 4. Verify java.rmi.RemoteException
   * is generated.
   * 
   * NOTE: EJB Specification v2.0 specifies that java.rmi.RemoteException is
   * returned not only for authorization failures but also for communicaton
   * errors. The test may pass instead of failing if the exception is generated
   * because of a communication failure.
   * 
   */

  public void test1() throws Fault {
    logMsg("Starting No caller authorization test");
    try {
      ejb1ref = ejb1home.create(props, newTable, 1, "coffee-1", 1);
      ejb1ref.EjbNotAuthz();
      logErr(
          "Method call did not generate an expected java.rmi.RemoteException");
      ejb1ref.remove();
      throw new Fault("No caller authorization test failed");
    } catch (java.rmi.RemoteException e) {

      try {
        if (ejb1ref != null)
          ejb1ref.remove();
      } catch (javax.ejb.RemoveException rex) {
        logErr("Cannot remove ejb:" + rex);
      } catch (java.rmi.RemoteException re) {
        logErr("Cannot remove ejb: RemoteException:" + re);
      }

      logMsg("Caught java.rmi.RemoteException as expected");
      logMsg("No authorization test passed");
    } catch (Exception e) {
      throw new Fault("No caller authorization test failed:", e);
    }
  }

  /*
   * @testName: test2
   * 
   * @assertion_ids: EJB:SPEC:827
   * 
   * @test_Strategy: 1. Create an entity bean accessing a method from another
   * bean deployed on another J2EE server. 2. Protect a method within the bean
   * with multiple security roles. 3. Call bean method as a principal who is in
   * one some of the security roles but not in others. 4. Verify call returns
   * successfully.
   */

  public void test2() throws Fault {
    boolean pass;

    logMsg("Starting Caller authorization test");
    try {
      ejb1ref = ejb1home.create(props, newTable, 1, "coffee-1", 1);
      pass = ejb1ref.EjbIsAuthz();
      ejb1ref.remove();
      if (pass)
        logMsg("Caller authorization test passed");
      else
        throw new Fault("Caller authorization test failed");
    } catch (Exception e) {

      try {
        if (ejb1ref != null)
          ejb1ref.remove();
      } catch (javax.ejb.RemoveException rex) {
        logErr("Cannot remove ejb:" + rex);

      } catch (java.rmi.RemoteException re) {
        logErr("Cannot remove ejb: RemoteException:" + re);
      }

      throw new Fault("Caller authorization test failed: ", e);
    }
  }

  /*
   * @testName: test3
   * 
   * @assertion_ids: EJB:SPEC:61.7; EJB:SPEC:81.4
   *
   * @test_Strategy: 1. Create an entity bean accessing a method from another
   * bean deployed on another J2EE server. 2. Protect a method in the bean using
   * a security role (say role1). 3. Link a security role ref - emp_secrole_ref
   * - to role1 in the bean. 4. Invoke the method with emp_secrole_ref as
   * parameter. 5. bean calls isCallerInRole(emp_secrole_ref) and returns return
   * value. 6. Verify return value is true.
   */

  public void test3() throws Fault {
    boolean pass;

    logMsg("Starting Security role reference positive test");
    try {
      ejb1ref = ejb1home.create(props, newTable, 1, "coffee-1", 1);
      pass = ejb1ref.EjbSecRoleRef(emp_secrole_ref);
      ejb1ref.remove();
      if (pass)
        logMsg("Security role reference positive test passed");
      else {
        logErr("EjbSecRoleRef(" + emp_secrole_ref + ") returned false");
        throw new Fault("Security role reference positive test failed");
      }
    } catch (Exception e) {
      throw new Fault("Security role reference positive test failed: ", e);
    }
  }
  /*
   * @testName: test4
   * 
   * @assertion_ids: EJB:SPEC:61.8
   *
   * @test_Strategy: 1. Create an entity bean accessing a method from another
   * bean deployed on another J2EE server. 2. Protect a method in the bean using
   * a security role (say role1). 3. Link a security role ref - emp_secrole_ref
   * - to role1 in the bean. 4. Invoke the method with mgr_secrole_ref as a
   * parameter. 5. bean calls isCallerInRole(mgr_secrole_ref) and returns return
   * value. 6. Verify return value is false.
   */

  public void test4() throws Fault {
    boolean pass;

    logMsg("Starting Security role reference negative test");
    try {
      ejb1ref = ejb1home.create(props, newTable, 1, "coffee-1", 1);
      pass = !ejb1ref.EjbSecRoleRef(mgr_secrole_ref);
      ejb1ref.remove();
      if (pass)
        logMsg("Security role reference negative test passed");
      else {
        logErr("EjbSecRoleRef(" + mgr_secrole_ref + ") returned true");
        throw new Fault("Security role reference negative test failed");
      }

    } catch (Exception e) {
      throw new Fault("Security role reference negative test failed: ", e);
    }
  }

  /*
   * @testName: test5
   * 
   * @assertion_ids: EJB:SPEC:799; EJB:SPEC:804
   *
   * @test_Strategy: 1. Create an entity bean accessing two entity beans (say
   * ejb1 and ejb2). 2. Link security role reference (roleref) to role1 in ejb1
   * and role2 in ejb2. 3. Ensure caller principal is in role1 but not in role2.
   * 4. Invoke method in ejb1 that returns value of
   * isCallerInRole(roleref).Verify return value is true. 5. Invoke method in
   * ejb2 that returns value of isCallerInRole(roleref). Verify return value is
   * false.
   */

  public void test5() throws Fault {
    boolean pass;

    logMsg("Starting Security role reference scope test");
    try {
      ejb1ref = ejb1home.create(props, newTable, 1, "coffee-1", 1);

      // caller must be in security role linked to emp_secrole_ref. call must
      // succeed.
      pass = ejb1ref.EjbSecRoleRef(emp_secrole_ref);
      ejb1ref.remove();

      if (pass)
        logMsg("(ejb1) isCallerInRole(" + emp_secrole_ref
            + ") returned true as expected");
      else {
        logErr("isCallerInRole(" + emp_secrole_ref + ") returned false");
        throw new Fault("Security role reference scope test failed");
      }

    } catch (Exception e) {
      throw new Fault("Security role reference scope test failed: ", e);
    }

    try {
      ejb2ref = ejb2home.create(props, newTable, 1, "coffee-1", 1);

      // caller not in security role linked to emp_secrole_ref. call should fail
      pass = ejb2ref.EjbSecRoleRefScope(emp_secrole_ref);
      ejb2ref.remove();

      if (pass) {
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
   * @assertion_ids: EJB:SPEC:827
   * 
   * @test_Strategy: 1. Create an entity bean accessing another bean with
   * overloaded methods (method1, method2) deployed on another J2EE server. 2.
   * Call method1 passing emp_secrole_ref. 3. Method1 returns
   * isCallerInRole(emp_secrole_ref) which must be true. 4. Call method2 passing
   * two role references as parameters. 5. Method must return false ( caller not
   * in both security role refs).
   */

  public void test6() throws Fault {
    logMsg("Starting Overloaded security role references test");
    ejb1ref = null;

    try {
      ejb1ref = ejb1home.create(props, newTable, 1, "coffee-1", 1);

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
      throw new Fault("Overloaded security role references test failed: ", e);
    } finally {
      // perform cleanup. ejb1ref.remove() can throw an exception. Hence the
      // cleanup code must be wrapped in its own try catch clause
      try {
        if (ejb1ref != null) {
          logMsg("Removing object reference");
          ejb1ref.remove();
        }
      } catch (Exception e) {
        throw new Fault("Overloaded security role references test failed: ", e);
      }
    }
    logMsg("Overloaded security role references test passed");
  }

  /*
   * @testName: test7
   * 
   * @assertion_ids: EJB:SPEC:827
   *
   * @test_Strategy: 1. Create a bmp entity bean with an unchecked remote method
   * 2. Deploy appclient Client on one J2EE server 3. Deploy the bmp bean on
   * another J2EE server 4. Verify that access is allowed from appclient Client
   * to the unchecked method.
   */

  public void test7() throws Fault {
    logMsg("Starting unchecked test1");
    try {
      ejb1ref = ejb1home.create(props, newTable, 1, "coffee-1", 1);
      if (!ejb1ref.checktest1()) {
        logErr("unchecked test returned false");
        throw new Fault("unchecked test1 failed");
      }
      logMsg("unchecked test1 passed.");

    } catch (Exception e) {

      try {
        if (ejb1ref != null)
          ejb1ref.remove();
      } catch (javax.ejb.RemoveException rex) {
        logErr("Cannot remove ejb:" + rex);
      } catch (java.rmi.RemoteException re) {
        logErr("Cannot remove ejb: RemoteException:" + re);
      }

      throw new Fault("unchecked test1 failed", e);
    }
  }

  /*
   * @testName: test8
   * 
   * @assertion_ids: EJB:SPEC:808
   * 
   * @test_Strategy: 1. Create a bmp entity bean with a method on exclude-list.
   * 2. Deploy appclient Client on one J2EE server 3. Deploy the bmp bean on
   * another J2EE server 4. Verify java.rmi.RemoteException is generated when
   * appclient Client accessing the method.
   */

  public void test8() throws Fault {
    logMsg("Starting exclude-list test1");
    try {
      ejb1ref = ejb1home.create(props, newTable, 1, "coffee-1", 1);
      ejb1ref.excludetest1();
    } catch (java.rmi.RemoteException e) {

      try {
        if (ejb1ref != null)
          ejb1ref.remove();
      } catch (javax.ejb.RemoveException rex) {
        logErr("Cannot remove ejb:" + rex);
      } catch (java.rmi.RemoteException re) {
        logErr("Cannot remove ejb: RemoteException:" + re);
      }

      logMsg("Caught java.rmi.RemoteException as expected");
      logMsg("exclude-list test1 passed");
    } catch (Exception e) {
      throw new Fault("exclude-list test1 failed:", e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("cleanup");
  }
}
