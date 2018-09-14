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
 *   $Id$
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
import javax.ejb.SessionContext;
import javax.ejb.EJBs;
import javax.ejb.EJB;
import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.ejb.Remote;
import javax.ejb.Local;
import javax.ejb.Remove;

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
