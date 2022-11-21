/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

import java.util.logging.Logger;

import com.sun.ts.tests.ejb30.common.helper.Helper;

import jakarta.annotation.Resource;
import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;

@Stateless(name = "TargetBean")
// @Remote({Target.class})
@DeclareRoles({ "Administrator", "Manager", "Employee" })

public class TargetBean implements Target {

  private SessionContext sctx;

  private Logger logger = Helper.getLogger();

  @RolesAllowed({ "Administrator", "Manager", "Employee" })
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void initLogging(java.util.Properties p) {
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public boolean IsCaller(String caller) {
    if (sctx.getCallerPrincipal().getName().indexOf(caller) < 0)
      return false;
    else
      return true;
  }

  @RolesAllowed({ "Administrator" })
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public boolean EjbNotAuthz() {
    return true;
  }

  @RolesAllowed({ "Administrator", "Manager", "Employee" })
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public boolean EjbIsAuthz() {
    return true;
  }

  @RolesAllowed({ "Manager", "Employee" })
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public boolean EjbSecRoleRef(String role) {
    return sctx.isCallerInRole(role);
  }

  @PermitAll
  public boolean uncheckedTest() {
    return true;
  }

  @DenyAll
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public boolean excludeTest() {
    return true;
  }

  @Resource
  public void setSessionContext(SessionContext sc) {
    sctx = sc;
  }
}
