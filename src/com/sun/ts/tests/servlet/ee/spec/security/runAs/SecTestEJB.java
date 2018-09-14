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

package com.sun.ts.tests.servlet.ee.spec.security.runAs;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.annotation.Resource;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.SessionContext;
import javax.annotation.security.RolesAllowed;

@Stateless(name = "SecTestEJB")
@Remote({ SecTest.class })

public class SecTestEJB implements SecTest {

  private SessionContext sessionContext;

  @RolesAllowed({ "Manager" })
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  /**
   * Returns the name of the caller principal
   */
  public String getCallerPrincipalName() {
    return sessionContext.getCallerPrincipal().getName();
  }

  @Resource
  public void setSessionContext(SessionContext sc) {
    sessionContext = sc;
  }
}
