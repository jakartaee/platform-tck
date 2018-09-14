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
 * @(#)Client.java	1.16 03/05/16
 */

package com.sun.ts.tests.interop.security.ejbclient.stateless;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import java.io.*;
import java.util.*;
import javax.ejb.*;
import com.sun.javatest.Status;

import com.sun.ts.tests.ejb.ee.sec.stateless.common.*;

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
   * @test_Strategy: 1. Create a stateless session bean with accessing a method
   * from another bean deployed on another J2EE server. 2. Protect the method
   * within the bean with multiple security roles. 3. Call bean method as a
   * principal who is in one some of the security roles but not in others. 4.
   * Verify call returns successfully.
   */

  public void test1() throws Fault {
    logMsg("Starting Caller authorization test");
    try {
      ejbref = ejbhome.create();
      ejbref.initLogging(props);
      if (!ejbref.EjbIsAuthz(props))
        throw new Fault("Caller authorization test failed");
      logMsg("Caller authorization test passed");
    } catch (Exception e) {
      throw new Fault("Caller authorization test failed: ", e);
    }
  }

  /*
   * @testName: test3
   * 
   * @assertion_ids: EJB:SPEC:61.7; EJB:SPEC:81.4
   *
   * @test_Strategy: 1. Create a stateless session bean accessing a method from
   * another bean deployed on another J2EE server. 2. Protect the method in the
   * bean using a security role (role1). 3. Link a security role ref -
   * emp_secrole_ref - to role1 in the bean. 4. Invoke the method with
   * emp_secrole_ref as parameter. 5. bean calls isCallerInRole(emp_secrole_ref)
   * and returns return value. 6. Verify return value is true.
   */

  public void test3() throws Fault {
    logMsg("Starting Security role reference positive test");
    try {
      ejbref = ejbhome.create();
      ejbref.initLogging(props);
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
   * @test_Strategy: 1. Create a stateless session bean accessing a method from
   * another bean deployed on another J2EE server. 2. Protect the method in the
   * bean using a security role (role1). 3. Link a security role ref -
   * emp_secrole_ref - to role1 in the bean. 4. Invoke the method with
   * mgr_secrole_ref as a parameter. 5. bean calls
   * isCallerInRole(mgr_secrole_ref) and returns return value. 6. Verify return
   * value is false.
   */

  public void test4() throws Fault {
    logMsg("Starting Security role reference negative test");
    try {
      ejbref = ejbhome.create();
      ejbref.initLogging(props);
      if (!ejbref.EjbSecRoleRef1(mgr_secrole_ref, props))
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
   * @test_Strategy: 1. Create a stateless session bean accessing two stateless
   * session beans (ejb1 and ejb2) deployed on another J2EE server. 2. Link
   * security role reference (roleref) to role1 in ejb1 and role2 in ejb2. 3.
   * Ensure caller principal is in role1 but not in role2. 4. Invoke method in
   * ejb1 that returns value of isCallerInRole(roleref). Verify return value is
   * true. 5. Invoke method in ejb2 that returns value of
   * isCallerInRole(roleref). Verify return value is false.
   */

  public void test5() throws Fault {
    logMsg("Starting Security role reference scope test");
    try {
      ejbref = ejbhome.create();
      ejbref.initLogging(props);

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
   * @testName: test2
   * 
   * @assertion_ids: EJB:SPEC:811
   * 
   * @test_Strategy: 1. Create a stateless session bean accessing another bean
   * deployed on another J2EE server with overloaded methods. 2. Call method1
   * passing emp_secrole_ref. 3. Method1 returns isCallerInRole(emp_secrole_ref)
   * which must be true. 4. Call method2 passing two role references as
   * parameters. 5. Method must return false ( caller not in both security role
   * refs).
   */

  public void test2() throws Fault {
    logMsg("Starting Overloaded security role references test");
    try {
      ejbref = ejbhome.create();
      ejbref.initLogging(props);
      if (!ejbref.EjbOverloadedSecRoleRefs(emp_secrole_ref, mgr_secrole_ref,
          props))
        throw new Fault("Overloaded security role references test failed");

      logMsg("Overloaded security role references test passed");
    } catch (Exception e) {
      throw new Fault("Overloaded security role references test failed", e);
    }
  }

  /*
   * @testName: test6
   * 
   * @assertion_ids: EJB:SPEC:827
   *
   * @test_Strategy: 1. Create a stateless session bean accessing a method from
   * another bean deployed on another J2EE server. 2. Protect bean method with
   * multiple security roles. 3. Call bean method as a principal who is not in
   * any of the security roles that protects the method. 4. Verify
   * java.rmi.RemoteException is generated.
   */

  public void test6() throws Fault {
    logMsg("Starting No caller authorization test");
    try {
      ejbref = ejbhome.create();
      ejbref.initLogging(props);
      if (!ejbref.EjbNotAuthz(props))
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
   * @test_Strategy: 1. Create a stateless session bean accessing an unchecked
   * method of the second session bean. 2. Deploy the first session bean on one
   * J2EE server 3. Deploy the second session bean on another J2EE server 4.
   * Verify that access is allowed from the first ejb to the unchecked method.
   */

  public void test7() throws Fault {
    logMsg("Starting unchecked test1");
    try {
      ejbref = ejbhome.create();
      ejbref.initLogging(props);
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
   * @testName: test8
   * 
   * @assertion_ids: EJB:SPEC:808
   *
   * @test_Strategy: 1. Create a stateless session bean accessing another
   * stateless session bean with a method on exclude-list. 2. Deploy the first
   * ejb on one J2EE server 3. Deploy the second session bean on another J2EE
   * server 4. Verify java.rmi.RemoteException is generated when the first ejb
   * accessing the method.
   */

  public void test8() throws Fault {
    logMsg("Starting exclude-list test1");
    try {
      ejbref = ejbhome.create();
      ejbref.initLogging(props);
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
