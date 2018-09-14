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
 * @(#)TestEJB.java	1.4 05/07/26
 */

package com.sun.ts.tests.ejb30.sec.stateless.common;

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
import javax.ejb.Stateless;
import javax.ejb.SessionContext;
import javax.ejb.EJBException;
import javax.ejb.Remote;

// Set JNDI name
@Stateless(name = "TestEJB")
@Remote({ Test.class })
// Set EJB References
@EJBs({
    @EJB(name = "SecTestEJB", beanName = "SecTestEJB", beanInterface = SecTest.class),
    @EJB(name = "SecTestRoleRefEJB", beanName = "SecTestRoleRefEJB", beanInterface = SecTestRoleRef.class) })
@TransactionManagement(TransactionManagementType.CONTAINER)
// @DeclareRoles("Administrator", "Employee", "Manager")

public class TestEJB implements Test {
  // Lookup SecTestEJB and save the reference in ejb1
  @EJB(beanName = "SecTestEJB")
  private SecTest ejb1 = null;

  // Lookup SecTestRoleRefEJB and save the reference in ejb2
  @EJB(beanName = "SecTestRoleRefEJB")
  private SecTestRoleRef ejb2 = null;

  private SessionContext sctx = null;

  private static final String UserNameProp = "user";

  private static final String UserPasswordProp = "password";

  private String username = "";

  private String password = "";

  @RolesAllowed({ "Administrator", "Manager", "VP", "Employee" })
  @TransactionAttribute(TransactionAttributeType.NEVER)
  public void initLogging(java.util.Properties p) {
    try {
      TestUtil.init(p);
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg("TestEJB initLogging failed.");
      throw new EJBException(e.getMessage());
    }
  }

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
  public boolean IsCallerB2(String caller, java.util.Properties p) {
    try {
      ejb1.initLogging(p);
      boolean result = ejb1.IsCaller(caller);
      return result;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      return false;
    }
  }

  @TransactionAttribute(TransactionAttributeType.NEVER)
  public boolean InRole(String role, java.util.Properties p) {
    try {
      ejb1.initLogging(p);
      boolean result = ejb1.EjbSecRoleRef(role);
      return result;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      return false;
    }
  }

  @RolesAllowed({ "Administrator", "Manager", "VP", "Employee" })
  @TransactionAttribute(TransactionAttributeType.NEVER)
  public boolean EjbNotAuthz(java.util.Properties p) {
    try {
      ejb1.initLogging(p);
      ejb1.EjbNotAuthz();
      TestUtil.logErr(
          "Method call did not generate an expected java.rmi.RemoteException");
      return false;
    } catch (EJBException e) { // REVISIT
      TestUtil.logMsg("Caught java.rmi.RemoteException as expected");
      // cleanup(ejb1);
      return true;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      cleanup(ejb1);
      return false;
    }
  }

  private void cleanup(SecTest ejbref) {

  }

  @RolesAllowed({ "Administrator", "Manager", "VP", "Employee" })
  @TransactionAttribute(TransactionAttributeType.NEVER)
  public boolean EjbIsAuthz(java.util.Properties p) {
    TestUtil.logMsg("Starting Caller authorization test");
    try {
      ejb1.initLogging(p);
      boolean result = ejb1.EjbIsAuthz();

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
  public boolean EjbSecRoleRef(String role, java.util.Properties p) {
    TestUtil.logMsg("Starting Security role reference positive test");
    try {
      ejb1.initLogging(p);
      boolean result = ejb1.EjbSecRoleRef(role);

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
  public boolean EjbSecRoleRef1(String role, java.util.Properties p) {
    TestUtil.logMsg("Starting Security role reference negative test");
    try {
      ejb1.initLogging(p);
      boolean result = ejb1.EjbSecRoleRef(role);

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
  public boolean EjbSecRoleRefScope(String role, java.util.Properties p) {
    try {
      ejb1.initLogging(p);

      // caller must be in security role linked to emp_secrole_ref. call must
      // succeed.
      boolean result = ejb1.EjbSecRoleRef(role);

      if (!result)
        return false;

      ejb2.initLogging(p);
      result = ejb2.EjbSecRoleRefScope(role);

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
  public boolean EjbOverloadedSecRoleRefs(String role1, String role2,
      java.util.Properties p) {
    TestUtil.logMsg("Starting Overloaded security role references test");
    try {
      ejb1.initLogging(p);
      boolean result = ejb1.EjbOverloadedSecRoleRefs(role1);

      if (!result) {
        TestUtil
            .logErr("EjbOverloadedSecRoleRefs(emp_secrole_ref) returned false");
        return false;
      }

      ejb1.initLogging(p);
      result = ejb1.EjbOverloadedSecRoleRefs(role1, role2);

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
  public boolean checktest1(java.util.Properties p) {
    TestUtil.logMsg("Starting uncheckted test1 ... ");

    try {
      ejb1.initLogging(p);
      boolean result = ejb1.checktest1();
      return result;
    } catch (Exception e) {
      TestUtil.logErr("uncheckted test1 failed with exception: ", e);
      return false;
    }
  }

  @RolesAllowed({ "Administrator", "Manager", "VP", "Employee" })
  @TransactionAttribute(TransactionAttributeType.NEVER)
  public boolean excludetest1(java.util.Properties p) {
    TestUtil.logMsg("Starting exclude test1 ... ");

    try {
      ejb1.initLogging(p);
      boolean result = ejb1.excludetest1();
      return false;
    } catch (EJBException ex) { // REVISIT
      TestUtil.printStackTrace(ex);
      TestUtil.logTrace("Got excpected exception");
      return true;

    } catch (Exception e) {
      TestUtil.logErr("exclude-list test1 failed with exception: ", e);
      return false;
    }
  }

}
