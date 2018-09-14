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

package com.sun.ts.tests.compat12.ejb.sec;

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
   * @testName: compat12SecTest1
   * 
   * @assertion_ids: JavaEE:SPEC:283
   *
   * @test_Strategy: Verify application portability by being able to deploy this
   * application built on a J2EE 1.2 platform.
   *
   * 1. Create a stateful session bean with a method. 2. Protect bean method
   * with multiple security roles. 3. Call bean method as a principal who is not
   * in any of the security roles that protects the method. 4. Verify
   * java.rmi.RemoteException is generated.
   */

  public void compat12SecTest1() throws Fault {
    logMsg("Starting No caller authorization test");
    try {
      ejb1ref = ejb1home.create(props);
      ejb1ref.EjbNotAuthz();
      logErr(
          "Method call did not generate an expected java.rmi.RemoteException");
      throw new Fault("No caller authorization test failed");
    } catch (java.rmi.RemoteException e) {
      logMsg("Caught java.rmi.RemoteException as expected");
      logMsg("No authorization test passed");
    } catch (Exception e) {
      throw new Fault("No caller authorization test failed:", e);
    }
  }

  /*
   * @testName: compat12SecTest2
   * 
   * @assertion_ids: JavaEE:SPEC:283
   * 
   * @test_Strategy: Verify application portability by being able to deploy this
   * application built on a J2EE 1.2 platform.
   * 
   * 1. Create a stateful session bean with a method. 2. Protect a method within
   * the bean with multiple security roles. 3. Call bean method as a principal
   * who is in one some of the security roles but not in others. 4. Verify call
   * returns successfully.
   * 
   */

  public void compat12SecTest2() throws Fault {
    logMsg("Starting Caller authorization test");
    try {
      ejb1ref = ejb1home.create(props);
      if (ejb1ref.EjbIsAuthz())
        logMsg("Caller authorization test passed");
      else
        throw new Fault("Caller authorization test failed");
    } catch (Exception e) {
      throw new Fault("Caller authorization test failed: ", e);
    }
  }

  /*
   * @testName: compat12SecTest3
   * 
   * @assertion_ids: JavaEE:SPEC:283 A security role reference name must be the
   * security role name used in isCallerInRole() api call. This is a positive
   * test.
   *
   * @test_Strategy: 1. Create a stateful session bean. 2. Protect a method in
   * the bean using a security role (role1). 3. Link a security role ref -
   * emp_secrole_ref - to role1 in the bean. 4. Invoke the method with
   * emp_secrole_ref as parameter. 5. bean calls isCallerInRole(emp_secrole_ref)
   * and returns return value. 6. Verify return value is true.
   */

  public void compat12SecTest3() throws Fault {
    logMsg("Starting Security role reference positive test");
    try {
      ejb1ref = ejb1home.create(props);
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
   * @testName: compat12SecTest4
   * 
   * @assertion_ids: JavaEE:SPEC:283
   * 
   * @test_Strategy: Verify application portability by being able to deploy this
   * application built on a J2EE 1.2 platform. 1. Create a stateful session
   * bean. 2. Protect a method in the bean using a security role (role1). 3.
   * Link a security role ref - emp_secrole_ref - to role1 in the bean. 4.
   * Invoke the method with mgr_secrole_ref as a parameter. 5. bean calls
   * isCallerInRole(mgr_secrole_ref) and returns return value. 6. Verify return
   * value is false.
   */

  public void compat12SecTest4() throws Fault {
    logMsg("Starting Security role reference negative test");
    try {
      ejb1ref = ejb1home.create(props);
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
   * @testName: compat12SecTest5
   * 
   * @assertion_ids: JavaEE:SPEC:283
   * 
   * @test_Strategy: Verify application portability by being able to deploy this
   * application built on a J2EE 1.2 platform.
   *
   * 1. Create two stateful session beans (ejb1 and ejb2). 2. Link security role
   * reference (roleref) to role1 in ejb1 and role2 in ejb2. 3. Ensure caller
   * principal is in role1 but not in role2. 4. Invoke method in ejb1 that
   * returns value of isCallerInRole(roleref). Verify return value is true. 5.
   * Invoke method in ejb2 that returns value of isCallerInRole(roleref). Verify
   * return value is false.
   */

  public void compat12SecTest5() throws Fault {
    logMsg("Starting Security role reference scope test");
    try {
      ejb1ref = ejb1home.create(props);

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
      ejb2ref = ejb2home.create(props);

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
   * @testName: compat12SecTest6
   * 
   * @assertion_ids: JavaEE:SPEC:283
   * 
   * @test_Strategy: Verify application portability by being able to deploy this
   * application built on a J2EE 1.2 platform. This test verifies that the
   * correct overloaded method is called depending upon the number of arguments.
   * 
   * 1. Create a stateful session bean with overloaded methods. 2. Call method1
   * passing emp_secrole_ref. 3. Method1 returns isCallerInRole(emp_secrole_ref)
   * which must be true. 4. Call method2 passing two role references as
   * parameters. 5. Method must return false ( caller not in both security role
   * refs).
   */

  public void compat12SecTest6() throws Fault {
    logMsg("Starting Overloaded security role references test");
    try {
      ejb1ref = ejb1home.create(props);
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
      throw new Fault("Overloaded security role references test failed");
    }
    logMsg("Overloaded security role references test passed");
  }

  public void cleanup() throws Fault {
    try {
      if (ejb1ref != null) {
        ejb1ref.remove();
        ejb1ref = null;
      }
      if (ejb2ref != null) {
        ejb2ref.remove();
        ejb2ref = null;
      }
    } catch (Exception e) {
      logErr("Cleanup failed: ", e);
    }
    logMsg("cleanup ok");
  }
}
