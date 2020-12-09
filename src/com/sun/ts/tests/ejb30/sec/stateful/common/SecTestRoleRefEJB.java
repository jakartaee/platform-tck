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
 *   $Id$
 */

package com.sun.ts.tests.ejb30.sec.stateful.common;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Local;
import jakarta.ejb.Remote;
import jakarta.ejb.Remove;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;

@Stateful(name = "SecTestRoleRefEJB")
@Remote({ SecTestRoleRef.class })
@Local({ SecTestRoleRefLocal.class })
@TransactionManagement(TransactionManagementType.CONTAINER)
// @RolesReferenced({"EMP=Administrator"})

public class SecTestRoleRefEJB implements SecTestRoleRef {

  private SessionContext sctx = null;

  @RolesAllowed({ "Manager", "Employee" })
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  @Remove
  public boolean EjbSecRoleRefScope(String role) {
    return sctx.isCallerInRole(role);
  }

  @Resource
  public void setSessionContext(SessionContext sc) {
    sctx = sc;
  }
}
