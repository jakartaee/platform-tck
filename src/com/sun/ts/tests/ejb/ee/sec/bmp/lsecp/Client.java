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

package com.sun.ts.tests.ejb.ee.sec.bmp.lsecp;

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
  private static final String ejbname = "java:comp/env/ejb/lTest";

  // references to ejb interfaces
  private lTestHome ejbhome = null;

  private lTest ejbref = null;

  // Security role references.
  private static final String emp_secrole = "EMP";

  private static final String admin_secrole = "ADMIN";

  private static final String mgr_secrole = "MGR";

  private static final String UserNameProp = "user";

  private static final String UserPasswordProp = "password";

  private String username = "";

  private String password = "";

  private boolean newTable = true;

  private Properties props = null;

  private TSNamingContext nctx = null;

  private TSLoginContext lc = null;

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

      ejbhome = (lTestHome) nctx.lookup(ejbname, lTestHome.class);

    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /*
   * @testName: test1
   * 
   * @assertion_ids: EJB:SPEC:827
   * 
   * @test_Strategy: 1. Create a bmp entity bean invokes a local method of the
   * second bean. 2. Protect the second method within the bean with multiple
   * security roles. 3. Call the bean method as a principal who is in some of
   * the security roles defined for the second method. 4. Verify call returns
   * successfully. Note: A caller principal who is in at least one of the
   * security roles defined on a method permission element for an EJB method
   * must be allowed to execute the method.
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
   * @testName: test3
   * 
   * @assertion_ids: EJB:SPEC:61.7; EJB:SPEC:81.4
   *
   * @test_Strategy: 1. Create a bmp entity bean invokes a local method of the
   * second bean. 2. Protect the method in the second bean using a security role
   * (role1). 3. Link a security role ref - emp_secrole - to role1 in the bean.
   * 4. Invoke the method with emp_secrole as parameter. 5. bean calls
   * isCallerInRole(emp_secrole) and returns return value. 6. Verify return
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

      if (!ejbref.EjbSecRoleRef(emp_secrole, props))
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
   * @test_Strategy: 1. Create a bmp entity bean invokes a local method of the
   * second bean. 2. Protect the method in the bean using a security role
   * (role1). 3. Link a security role ref - emp_secrole - to role1 in the bean.
   * 4. Invoke the method with mgr_secrole as a parameter. 5. bean calls
   * isCallerInRole(mgr_secrole) and returns return value. 6. Verify return
   * value is false.
   *
   * Note: A security role reference name must be the security role name used in
   * isCallerInRole() api call as specified in EJB 2.0 specification, section
   * 21.2.5.3. This is a negative test.
   *
   */

  public void test4() throws Fault {
    logMsg("Starting Security role reference negative test");
    try {
      ejbref = ejbhome.create(props, newTable, 1, "coffee-1", 1);

      if (!ejbref.EjbSecRoleRef1(mgr_secrole, props))
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
   * @test_Strategy: 1. Create a bmp entity bean invokes local methods of the
   * two bmp entity beans (ejb1 and ejb2). 2. Link security role reference
   * (roleref) to role1 in ejb1 and role2 in ejb2. 3. Ensure caller principal is
   * in role1 but not in role2. 4. Invoke method in ejb1 that returns value of
   * isCallerInRole(roleref). Verify return value is true. 5. Invoke method in
   * ejb2 that returns value of isCallerInRole(roleref). Verify return value is
   * false.
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

      // caller must be in security role linked to emp_secrole. call must
      // succeed.
      if (!ejbref.EjbSecRoleRefScope(emp_secrole, props))
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
   * @test_Strategy: 1. Create a bmp entity bean invokes the second bean with
   * overloaded methods: method1 and method2. 2. Call method1 passing
   * emp_secrole. 3. Method1 returns isCallerInRole(emp_secrole) which must be
   * true. 4. Call method2 passing two role references as parameters. 5. Method
   * must return false ( caller not in both security role refs).
   *
   * Note: Methods declared in a method-permission element can be overloaded as
   * specified in EJB Specification 2.0 21.3.2. This test verifies that correct
   * overloaded method is called depending upon the number of arguments.
   */

  public void test2() throws Fault {
    logMsg("Starting Overloaded security role references test");
    try {
      ejbref = ejbhome.create(props, newTable, 1, "coffee-1", 1);

      if (!ejbref.EjbOverloadedSecRoleRefs(emp_secrole, mgr_secrole, props))
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
   * @test_Strategy: 1. Create a bmp entity bean invokes a local method of the
   * second bean. 2. Protect bean method with multiple security roles. 3. Call
   * bean method as a principal who is not in any of the security roles that
   * protects the method. 4. Verify javax.ejb.EJBException is generated.
   *
   * Note: If a caller principal is not in one of the security roles defined in
   * a method permission element for the method, then the ejb container must
   * generate a javax.ejb.EJBException.
   * 
   * This is a local interface test.
   *
   * EJB Specification v2.0 specifies that javax.ejb.EJBException is returned
   * not only for authorization failures but also for communicaton errors. The
   * test may pass instead of failing if the exception is generated because of a
   * communication failure.
   */

  public void test6() throws Fault {
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
   * @testName: test7
   *
   * @assertion_ids: EJB:SPEC:827
   *
   * @test_Strategy: 1. Create a bmp entity bean invokes a local method of the
   * second bean. 2. Have this local method with method permission unchecked 3.
   * Verify that access is allowed.
   *
   * Note: "The unchecked element specifies that a method is not checked for
   * authorization by the container prior to invocation of the method."
   */

  public void test7() throws Fault {
    logMsg("Starting unchecked test");
    try {
      ejbref = ejbhome.create(props, newTable, 1, "coffee-1", 1);

      if (!ejbref.checktest1(props))
        throw new Fault("checktest1 returned false");
      logMsg("unchecked test passed");
    } catch (Exception e) {
      throw new Fault("unchecked test failed:", e);
    }
  }

  /*
   * @testName: test8
   *
   * @assertion_ids: EJB:SPEC:808
   *
   * @test_Strategy: 1. Create a bmp entity bean invokes a local method of the
   * second bean. 2. Put this local method on exclude-list. 3. Verify
   * javax.ejb.EJBException is generated. Note: "The exclude-list element
   * defines a set of methods which the assembler marks to be uncallable. It
   * contains one or more methods. If the method permission relation contains
   * methods that are in the exclude list, the deployer should consider those
   * methods to be uncallable."
   *
   * This test tests when a local interface method in the exclude list,
   * javax.ejb.EJBException will be thrown when invoked.
   */

  public void test8() throws Fault {
    logMsg("Starting exclude-list test");
    try {
      ejbref = ejbhome.create(props, newTable, 1, "coffee-1", 1);

      if (!ejbref.excludetest1(props))
        throw new Fault("excludetest1 returned false");
      logMsg("exclude-list test passed");
    } catch (Exception e) {
      throw new Fault("exclude-list test failed:", e);
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
