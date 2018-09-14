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

import com.sun.ts.tests.ejb30.common.helper.Helper;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.annotation.security.RunAs;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.EJBs;
import javax.ejb.EJB;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.SessionContext;
import javax.ejb.EJBAccessException;
import javax.ejb.Remote;

@Stateless(name = "InterMediateBean")
// @Remote({InterMediate.class})

// Set EJB References
@EJBs({
    @EJB(name = "TargetBean", beanName = "TargetBean", beanInterface = Target.class) })
@TransactionManagement(TransactionManagementType.CONTAINER)
@DeclareRoles({ "Administrator", "Employee", "Manager" })
@RunAs("Manager")
@RolesAllowed({ "Administrator", "Employee", "Manager" })
public class InterMediateBean implements InterMediate {
  // Lookup TargetBean and save the reference in ejb1
  @EJB(beanName = "TargetBean")
  private Target ejb1 = null;

  private Logger logger = Helper.getLogger();

  private SessionContext sctx = null;

  private static final String UserNameProp = "user";

  private static final String UserPasswordProp = "password";

  private String username = "";

  private String password = "";

  @RolesAllowed({ "Administrator", "Employee", "Manager" })
  @TransactionAttribute(TransactionAttributeType.NEVER)
  public void initLogging(java.util.Properties p) {
    logger = Helper.getLogger();
  }

  @Resource
  public void setSessionContext(SessionContext sc) {
    sctx = sc;
  }

  @RolesAllowed({ "Administrator", "Employee", "Manager" })
  @TransactionAttribute(TransactionAttributeType.NEVER)
  public boolean IsCallerB1(String caller) {
    String name = sctx.getCallerPrincipal().getName();
    logMsg("IsCallerB1: " + name);

    if (name.indexOf(caller) < 0)
      return false;
    else
      return true;
  }

  @RolesAllowed({ "Administrator", "Employee", "Manager" })
  @TransactionAttribute(TransactionAttributeType.NEVER)
  public boolean IsCallerB2(String caller, java.util.Properties p) {
    try {
      // ejb1.initLogging(p);
      logMsg("Running IsCallerB2 :" + caller);
      boolean result = ejb1.IsCaller(caller);
      return result;
    } catch (Exception e) {
      logMsg("Caught Unexpected exception e.getMessage()");
      return false;
    }
  }

  @RolesAllowed({ "Administrator", "Employee", "Manager" })
  @TransactionAttribute(TransactionAttributeType.NEVER)
  public boolean InRole(String role, java.util.Properties p) {
    try {
      // ejb1.initLogging(p);
      logMsg("Running InRole : " + role);
      boolean result = ejb1.EjbSecRoleRef(role);
      return result;
    } catch (Exception e) {
      logMsg("Caught Unexpected exception e.getMessage()");
      return false;
    }
  }

  @RolesAllowed({ "Administrator", "Employee", "Manager" })
  @TransactionAttribute(TransactionAttributeType.NEVER)
  public boolean EjbNotAuthz(java.util.Properties p) {
    try {
      // ejb1.initLogging(p);
      ejb1.EjbNotAuthz();
      logMsg(
          "Method call did not generate an expected javax.ejb.EJBAccessException");
      return false;
    } catch (EJBAccessException e) {
      logMsg("Caught javax.ejb.EJBAccessException as expected");
      cleanup(ejb1);
      return true;
    } catch (Exception e) {
      logMsg("Caught Unexpected exception e.getMessage()");
      cleanup(ejb1);
      return false;
    }
  }

  private void cleanup(Target ejbref) {

  }

  @RolesAllowed({ "Administrator", "Employee", "Manager" })
  @TransactionAttribute(TransactionAttributeType.NEVER)
  public boolean EjbIsAuthz(java.util.Properties p) {
    logMsg("In InterMediateBean.EjbIsAuthz method");
    try {
      // ejb1.initLogging(p);
      boolean result = ejb1.EjbIsAuthz();

      if (!result)
        return false;

    } catch (Exception e) {
      logMsg("Caught Unexpected exception e.getMessage()");
      return false;
    }
    return true;
  }

  @RolesAllowed({ "Administrator", "Employee", "Manager" })
  @TransactionAttribute(TransactionAttributeType.NEVER)
  public boolean EjbSecRoleRef(String role, java.util.Properties p) {
    logMsg("In InterMediateBean.EjbSecRoleRef method");
    try {
      // ejb1.initLogging(p);
      boolean result = ejb1.EjbSecRoleRef(role);

      if (!result)
        return false;
      return true;
    } catch (Exception e) {
      logMsg("Caught Unexpected exception e.getMessage()");
      return false;
    }
  }

  @RolesAllowed({ "Administrator", "Employee", "Manager" })
  @TransactionAttribute(TransactionAttributeType.NEVER)
  public boolean uncheckedTest(java.util.Properties p) {
    logMsg("In InterMediateBean.uncheckedTest method");
    try {
      // ejb1.initLogging(p);
      boolean result = ejb1.uncheckedTest();
      return result;
    } catch (Exception e) {
      logMsg("InterMediateBean.unchecktedTest failed with exception: "
          + e.getMessage());
      return false;
    }
  }

  @RolesAllowed({ "Administrator", "Employee", "Manager" })
  @TransactionAttribute(TransactionAttributeType.NEVER)
  public boolean excludeTest(java.util.Properties p) {
    logMsg("In InterMediateBean.excludeTest method");

    try {
      // ejb1.initLogging(p);
      boolean result = ejb1.excludeTest();
      return false;
    } catch (EJBAccessException ex) {
      logMsg("InterMediateBean : Got expected EJBAccessException");
      return true;

    } catch (Exception e) {
      logMsg("InterMediateBean.excludeTest failed with exception: "
          + e.getMessage());
      return false;
    }
  }

  @TransactionAttribute(TransactionAttributeType.NEVER)
  public void logMsg(String msg) {
    logger.log(Level.INFO, msg);
  }

}
