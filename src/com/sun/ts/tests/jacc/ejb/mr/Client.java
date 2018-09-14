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

package com.sun.ts.tests.jacc.ejb.mr;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.porting.TSLoginContext;
import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;
import com.sun.ts.tests.ejb30.common.helper.Helper;
import java.util.Properties;
import com.sun.javatest.Status;
import javax.ejb.EJBException;
import javax.ejb.EJB;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Client extends EJBLiteClientBase {
  @EJB(beanName = "InterMediateBean")
  private InterMediate ejbref = null;

  Logger logger = null;

  // Security role references.
  // Note: To test annotation @DeclareRoles, same role names are used as
  // role-links. If there is a need to link different role-names for
  // role-links then old-style deployment descriptor should be used for
  // adding such role references.
  private static final String emp_secrole_ref = "Employee";

  private static final String admin_secrole_ref = "Administrator";

  private static final String mgr_secrole_ref = "Manager";

  private static final String UserNameProp = "user";

  private static final String UserPasswordProp = "password";

  private static final String AuthUser = "authuser";

  private String authuser = "javajoe";

  private String username = "j2ee";

  private String password = "j2ee";

  private Properties props = null;

  private TSLoginContext lc = null;

  /*
   * public static void main(String[] args) { Client theTests = new Client();
   * Status s = theTests.run(args, System.out, System.err); s.exit(); }
   */

  /*
   * @class.setup_props: user; password; authuser;
   */

  public void setup(String[] args, Properties p) {
    super.setup(args, p);
    props = p;
  }

  /*
   * @testName: EjbIsAuthz
   * 
   * @assertion_ids: EJB:SPEC:827; JACC:SPEC:103; // Add JSR250 Assertion for
   * RolesAllowed
   *
   * @test_Strategy: 1. Create a stateless session bean "InterMediateBean" with
   * runas identity set to <Manager> 2. Create another stateless session bean
   * "TargetBean" with method EjbIsAuthz 3. Protect the method with multiple
   * security roles including <Manager> 4. Call the
   * InterMediateBean.EjbIsAuthz() as principal <username,password>. Which then
   * invokes the EjbIsAuthz() on the TargetBean. 5. Since then InterMediateBean
   * uses runas identity, <Manager>, which is one of security roles set on the
   * method permission, so access to the method EjbIsAuthz should be allowed. 6.
   * Verify call returns successfully.
   */

  public void EjbIsAuthz() throws Fault {
    logMsg("Starting EjbIsAuthz test");
    try {
      ejbref.initLogging(props);
      if (!ejbref.EjbIsAuthz(props))
        throw new Fault("EjbIsAuthz test failed");
      logMsg("EjbIsAuthz test passed");
    } catch (Exception e) {
      throw new Fault("EjbIsAuthz test failed: ", e);
    }
  }

  /*
   * @testName: EjbNotAuthz
   * 
   * @assertion_ids: EJB:SPEC:811 ; JACC:SPEC:103; // Add JSR250 assertion for
   * RolesAllowed
   *
   * @test_Strategy: 1. Create a stateless session bean "InterMediateBean" with
   * runas identity set to <Manager> 2. Create another stateless session bean
   * "TargetBean" with method EjbNotAuthz 3. Protect the method with security
   * role <Administrator> 4. Call the bean InterMediateBean.EjbNotAuthz() as
   * principal <username,password>. Which then invokes the EjbNotAuthz() on the
   * bean TargetBean. 5. Since then InterMediateBean uses runas identity,
   * <Manager>, which does not share any principals with role <Administrator>.
   * so access to the method EjbNotAuthz shouldnot be allowed. 6. Verify
   * javax.ejb.EJBAccessException is generated.
   */

  public void EjbNotAuthz() throws Fault {
    logMsg("Starting EjbNotAuthz test");
    try {
      ejbref.initLogging(props);
      if (!ejbref.EjbNotAuthz(props))
        throw new Fault("EjbNotAuthz test failed");
      logMsg("EjbNotAuthz test passed");
    } catch (Exception e) {
      throw new Fault("EjbNotAuthz test failed:", e);
    }
  }

  /*
   * @testName: EjbSecRoleRef
   * 
   * @assertion_ids: EJB:SPEC:61.7; EJB:SPEC:81.4; // Add JSR 250 assertion for
   * DeclareRoles
   *
   * @test_Strategy: 1. Create a stateless session bean "InterMediateBean" with
   * runas identity set to <Manager> 2. Create another stateless session bean
   * "TargetBean" with method EjbSecRoleRef. 3. Protect the method with security
   * role <Employee>, Link a security role ref - emp_secrole_ref to role
   * <Employee>. 4. Call InterMediateBean.EjbSecRoleRef() as principal
   * <username,password>. Which then invokes EjbSecRoleRef on the bean
   * TargetBean. 5. Since then InterMediateBean uses runas identity, <Manager>,
   * who'e principals also in role <Employee> so access to the method of bean
   * TargetBean should be allowed. 6. verify that return value of
   * isCallerInRole(emp_secrole_ref) is true.
   */

  public void EjbSecRoleRef() throws Fault {
    logMsg("Starting EjbSecRoleRef test");
    try {
      ejbref.initLogging(props);
      if (!ejbref.EjbSecRoleRef(emp_secrole_ref, props))
        throw new Fault("EjbSecRoleRef test failed");
      logMsg("EjbSecRoleRef test passed");
    } catch (Exception e) {
      throw new Fault("EjbSecRoleRef test failed: ", e);
    }
  }

  /*
   * @testName: MGR_InRole
   * 
   * @assertion_ids: EJB:SPEC:827; //Add JSR 250 assertion for DeclareRoles
   * 
   * @test_Strategy: 1. Create a stateless session bean "InterMediateBean" with
   * runas identity set to <Manager> 2. Create another stateless session beans
   * "TargetBean" 3. Invoke InterMediateBean.InRole() with mgr_secrole_ref this
   * inturn invokes TargetBean.IsCaller() with mgr_secrole_ref 4. Since
   * InterMediateBean is configured to run as <Manager> the
   * TargetBean.IsCaller() with mgr_secrole_ref must return true.
   */

  public void MGR_InRole() throws Fault {
    logMsg("Starting MGR_InRole");
    try {
      ejbref.initLogging(props);
      if (!ejbref.InRole(mgr_secrole_ref, props))
        throw new Fault("MGR_InRole test failed");
      logMsg("MGR_InRole test passed");
    } catch (Exception e) {
      throw new Fault("MGR_InRole test failed:", e);
    }
  }

  /*
   * @testName: ADM_InRole
   * 
   * @assertion_ids: EJB:SPEC:61.8; // Add JSR250 assertion for DeclareRoles
   * 
   * @test_Strategy: 1. Create a stateless session bean "InterMediateBean" with
   * runas identity set to <Manager> 2. Create another stateless session beans
   * "TargetBean" 3. Invoke InterMediateBean.InRole() with admin_secrole_ref
   * this inturn invokes TargetBean.IsCaller() with admin_secrole_ref 4. Since
   * InterMediateBean is configured to run as <Manager> the
   * TargetBean.IsCaller() with admin_secrole_ref must return false.
   */

  public void ADM_InRole() throws Fault {
    logMsg("Starting ADM_InRole test");
    try {
      ejbref.initLogging(props);
      if (ejbref.InRole(admin_secrole_ref, props))
        throw new Fault("ADM_InRole test failed");
      logMsg("ADM_InRole test passed");
    } catch (Exception e) {
      throw new Fault("ADM_InRole test failed:", e);
    }
  }

  /*
   * @testName: InterMediateBean_CallerPrincipal
   * 
   * @assertion_ids: EJB:SPEC:796; // Add JSR250 assertion for RunAs
   *
   * @test_Strategy: 1. Create a stateless session bean InterMediateBean with
   * runas identity set to <Manager> 2. Verify that InterMediateBean returns the
   * correct getCallerPrincipal() this should not be affected because it is
   * configured to run as <Manager>
   */

  public void InterMediateBean_CallerPrincipal() throws Fault {
    logMsg("Starting InterMediateBean_CallerPrincipal test");
    try {
      ejbref.initLogging(props);
      if (!ejbref.IsCallerB1(username))
        throw new Fault("InterMediateBean_CallerPrincipal test failed");
      logMsg("InterMediateBean_CallerPrincipal test passed");
    } catch (Exception e) {
      throw new Fault("InterMediateBean_CallerPrincipal test failed:", e);
    }
  }

  /*
   * @testName: TargetBean_CallerPrincipal
   * 
   * @assertion_ids: EJB:SPEC:796; // Add JSR250 assertion for RunAs
   * 
   * @test_Strategy: 1. Create a stateless session bean "InterMediateBean" with
   * runas identity set to <Manager> 2. Create another stateless session bean
   * "TargetBean". 3. Verify that TargetBean returns the correct
   * getCallerPrincipal() which is the principal using runas identity, but not
   * the principal invoked InterMediateBean.
   */

  public void TargetBean_CallerPrincipal() throws Fault {
    logMsg("Starting TargetBean_CallerPrincipal test");
    try {
      ejbref.initLogging(props);

      if (ejbref.IsCallerB2(username, props))
        throw new Fault("TargetBean_CallerPrincipal test failed");

      if (!ejbref.IsCallerB2(authuser, props))
        throw new Fault("TargetBean_CallerPrincipal test failed");

      logMsg("TargetBean_CallerPrincipal test passed");
    } catch (Exception e) {
      throw new Fault("TargetBean_CallerPrincipal test failed:", e);
    }
  }

  /*
   * @testName: uncheckedTest
   * 
   * @assertion_ids: EJB:SPEC:827; note: Add JSR250 assertion for PermitAll
   *
   * @test_Strategy: 1. Create a stateless session bean with runas identity
   * <Manager> 2. Invoke InterMediateBean.uncheckedTest() this in-turn invokes
   * TargetBean.uncheckedTest(). 3. Protect the TargetBean's uncheckedTest()
   * with method permission "unchecked" or PermitAll 4. Verify that access is
   * allowed.
   */

  public void uncheckedTest() throws Fault {
    logMsg("Starting uncheckedTest ");
    try {
      ejbref.initLogging(props);
      if (!ejbref.uncheckedTest(props)) {
        logErr("uncheckedTest returned false");
        throw new Fault("uncheckedTest failed");
      }
      logMsg("uncheckedTest passed.");

    } catch (Exception e) {
      throw new Fault("uncheckedTest failed", e);
    }
  }

  /*
   * @testName: excludeTest
   * 
   * @assertion_ids: EJB:SPEC:808; // Add JSR250 assertion for DenyAll
   *
   * @test_Strategy: 1. Create a stateless session bean with runas identity
   * <Manager> 2. Invoke InterMediateBean.excludeTest(), this in-turn invokes
   * TargetBean.excludeTest(). 3. Put the TargetBean's excludeTest() method in
   * the exclude-list or DenyAll 4. Verify that javax.ejb.EJBAccessException is
   * generated.
   */

  public void excludeTest() throws Fault {
    logMsg("Starting excludeTest ");
    try {
      ejbref.initLogging(props);
      if (!ejbref.excludeTest(props)) {
        logErr("excludeTest returned false");
        throw new Fault("excludeTest failed");
      }
      logMsg("excludeTest passed");
    } catch (Exception e) {
      throw new Fault("excludeTest failed:", e);
    }
  }

  public void logMsg(String msg) {
    logger = Helper.getLogger();
    logger.log(Level.INFO, msg);
  }

  /*
   * public void cleanup() throws Fault { logMsg("cleanup ok"); }
   */
}
