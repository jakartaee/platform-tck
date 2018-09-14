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

package com.sun.ts.tests.ejb30.sec.stateful.common;

import com.sun.ts.lib.util.RemoteLoggingInitException;
import com.sun.ts.lib.util.TestUtil;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.EJBs;
import javax.ejb.EJB;
import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.ejb.SessionContext;
import javax.ejb.EJBException;
import javax.ejb.Remote;
import javax.ejb.Remove;
import java.util.Properties;

@Stateful(name = "TestEJB")
@Remote({ Test.class })
@EJBs({
    @EJB(name = "SecTestEJB", beanName = "SecTestEJB", beanInterface = SecTest.class),
    @EJB(name = "SecTestRoleRefEJB", beanName = "SecTestRoleRefEJB", beanInterface = SecTestRoleRef.class) })
@TransactionManagement(TransactionManagementType.CONTAINER)
// @RolesReferenced("Administrator", "Employee", "Manager")

public class TestEJB implements Test {

  @EJB(beanName = "SecTestEJB")
  private SecTest ejb1ref = null;

  @EJB(beanName = "SecTestRoleRefEJB")
  private SecTestRoleRef ejb2ref = null;

  // references to ejb interfaces
  private SessionContext sctx = null;

  private static final String UserNameProp = "user";

  private static final String UserPasswordProp = "password";

  private String username = "";

  private String password = "";

  private Properties props = null;

  @Resource
  public void setSessionContext(SessionContext sc) {
    sctx = sc;
  }

  @TransactionAttribute(TransactionAttributeType.NEVER)
  public boolean IsCallerB1(String caller) {
    String name = sctx.getCallerPrincipal().getName();
    TestUtil.logMsg("IsCallerB1: " + name);

    if (name.indexOf(caller) < 0)
      return false;
    else
      return true;
  }

  @TransactionAttribute(TransactionAttributeType.NEVER)
  public boolean InRole(String role) {
    try {
      boolean result = ejb1ref.EjbSecRoleRef(role);
      return result;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      return false;
    }
  }

  @RolesAllowed({ "Administrator", "Manager", "VP", "Employee" })
  @TransactionAttribute(TransactionAttributeType.NEVER)
  public boolean EjbNotAuthz() {
    try {
      ejb1ref.EjbNotAuthz();
      return false;
    } catch (EJBException e) { // REVISIT
      TestUtil.logMsg("Caught EJBException as expected");
      cleanup(ejb1ref);
      return true;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      cleanup(ejb1ref);
      return false;
    }
  }

  @Remove
  private void cleanup(SecTest ejbref) {

  }

  @RolesAllowed({ "Administrator", "Manager", "VP", "Employee" })
  @TransactionAttribute(TransactionAttributeType.NEVER)
  public boolean EjbIsAuthz() {
    TestUtil.logMsg("Starting Caller authorization test");
    try {
      boolean result = ejb1ref.EjbIsAuthz();
      if (!result)
        return false;

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      return false;
    }
    return true;
  }

  @RolesAllowed({ "Administrator", "Manager", "VP", "Employee" })
  @TransactionAttribute(TransactionAttributeType.NEVER)
  public boolean EjbSecRoleRef(String role) {
    TestUtil.logMsg("Starting Security role reference positive test");
    try {
      boolean result = ejb1ref.EjbSecRoleRef(role);

      if (!result)
        return false;
      return true;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      return false;
    }
  }

  @RolesAllowed({ "Administrator", "Manager", "VP", "Employee" })
  @TransactionAttribute(TransactionAttributeType.NEVER)
  public boolean EjbSecRoleRef1(String role) {
    TestUtil.logMsg("Starting Security role reference negative test");
    try {
      boolean result = ejb1ref.EjbSecRoleRef(role);
      if (result)
        return false;
      return true;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      return false;
    }
  }

  @RolesAllowed({ "Administrator", "Manager", "VP", "Employee" })
  @TransactionAttribute(TransactionAttributeType.NEVER)
  public boolean EjbSecRoleRefScope(String role) {
    try {

      // caller must be in security role linked to emp_secrole_ref. call must
      // succeed.
      boolean result = ejb1ref.EjbSecRoleRef(role);
      if (!result)
        return false;

      result = ejb2ref.EjbSecRoleRefScope(role);
      if (result)
        return false;
      return true;

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      return false;
    }
  }

  @RolesAllowed({ "Administrator", "Manager", "VP", "Employee" })
  @TransactionAttribute(TransactionAttributeType.NEVER)
  public boolean EjbOverloadedSecRoleRefs(String role1, String role2) {
    TestUtil.logMsg("Starting Overloaded security role references test");
    try {
      boolean result = ejb1ref.EjbOverloadedSecRoleRefs(role1);
      if (!result) {
        TestUtil
            .logErr("EjbOverloadedSecRoleRefs(emp_secrole_ref) returned false");
        return false;
      }
      result = ejb1ref.EjbOverloadedSecRoleRefs(role1, role2);

      if (result) {
        TestUtil.logErr(
            "EjbOverloadedSecRoleRefs(emp_secrole_ref,mgr_secrole_ref) returned true");
        return false;
      }
      return true;
    } catch (Exception e) {
      TestUtil.logErr("EjbOverloadedSecRoleRefs(" + role1 + "," + role2
          + ") failed with Exception: ", e);
      return false;
    }
  }

  @RolesAllowed({ "Administrator", "Manager", "VP", "Employee" })
  @TransactionAttribute(TransactionAttributeType.NEVER)
  public boolean checktest1() {
    try {
      boolean result = ejb1ref.checktest1();
      return result;

    } catch (Exception e) {
      TestUtil.logErr("Exception for checktest1: ", e);
      return false;
    }
  }

  @RolesAllowed({ "Administrator", "Manager", "VP", "Employee" })
  @TransactionAttribute(TransactionAttributeType.NEVER)
  public boolean excludetest1() {
    try {

      boolean result = ejb1ref.excludetest1();
      TestUtil.logErr("Should not be here.");
      return false;
    } catch (EJBException e) {
      TestUtil.logTrace("Got expected EJBException");
      return true;
    } catch (Exception ex) {
      TestUtil.logErr("Got wrong Exception in excludetest1:", ex);
      return false;
    }
  }

}
