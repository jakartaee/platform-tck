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

/*
 *  @(#)SecTestRoleRefEJB.java	1.3 05/07/26
 */

// The purpose of this EJB is to test scoping of security role references.

package com.sun.ts.tests.ejb30.sec.stateless.common;

import com.sun.ts.lib.util.RemoteLoggingInitException;
import com.sun.ts.lib.util.TestUtil;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Local;
import jakarta.ejb.Remote;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;

@Stateless(name = "SecTestRoleRefEJB")
@Remote({ SecTestRoleRef.class })
@Local({ SecTestRoleRefLocal.class })
@TransactionManagement(TransactionManagementType.CONTAINER)
// @DeclareRoles({"EMP=Manager"})

public class SecTestRoleRefEJB implements SecTestRoleRef {

  private SessionContext sctx = null;

  // This is equivalent to * as all the roles can call this method
  // @RolesAllowed("*")

  @RolesAllowed({ "Administrator", "Manager", "VP", "Employee" })
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void initLogging(java.util.Properties p) {
    try {
      TestUtil.init(p);
    } catch (RemoteLoggingInitException e) {
      TestUtil.logErr("SecTestRoleRefEJB initLogging failed.", e);
    }
  }

  @RolesAllowed({ "Manager", "Employee" })
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public boolean EjbSecRoleRefScope(String role) {
    return sctx.isCallerInRole(role);
  }

  @Resource
  public void setSessionContext(SessionContext sc) {
    sctx = sc;
  }

}
