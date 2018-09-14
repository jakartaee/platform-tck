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

import javax.ejb.Stateful;
import javax.ejb.Remote;
import javax.ejb.Local;
import javax.ejb.Remove;
import javax.ejb.SessionContext;
import javax.annotation.security.RolesAllowed;
import javax.annotation.security.DenyAll;
import javax.annotation.Resource;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateful(name = "SecTestEJB")
@Remote({ SecTest.class })
@Local({ SecTestLocal.class })
@TransactionManagement(TransactionManagementType.CONTAINER)
// @RolesReferenced(("Administrator", "VP", "Manager", "Employee")

public class SecTestEJB implements SecTest {

  private SessionContext sctx = null;

  @RolesAllowed({ "Manager", "VP" })
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  @Remove
  public boolean EjbNotAuthz() {
    return true;
  }

  @RolesAllowed({ "Administrator", "Manager", "VP", "Employee" })
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  @Remove
  public boolean EjbIsAuthz() {
    return true;
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public boolean EjbSecRoleRef(String role) {
    return sctx.isCallerInRole(role);
  }

  @RolesAllowed({ "Administrator", "Manager", "VP", "Employee" })
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public boolean EjbOverloadedSecRoleRefs(String role1) {
    TestUtil.logMsg(
        "isCallerInRole(" + role1 + ") = " + sctx.isCallerInRole(role1));
    return sctx.isCallerInRole(role1);
  }

  @RolesAllowed({ "Administrator", "Manager", "VP", "Employee" })
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  @Remove
  public boolean EjbOverloadedSecRoleRefs(String role1, String role2) {
    TestUtil
        .logMsg("isCallerInRole(" + role1 + ")= " + sctx.isCallerInRole(role1)
            + "isCallerInRole(" + role2 + ")= " + sctx.isCallerInRole(role2));
    return sctx.isCallerInRole(role1) && sctx.isCallerInRole(role2);
  }

  @Remove
  public boolean checktest1() {
    return true;
  }

  @DenyAll
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  @Remove
  public boolean excludetest1() {
    return true;
  }

  @Resource
  public void setSessionContext(SessionContext sc) {
    sctx = sc;
  }

}
