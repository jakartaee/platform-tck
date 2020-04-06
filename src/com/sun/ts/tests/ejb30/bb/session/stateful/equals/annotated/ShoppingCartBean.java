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
 * $Id$
 */

package com.sun.ts.tests.ejb30.bb.session.stateful.equals.annotated;

import jakarta.ejb.Local;
import jakarta.ejb.Remote;
import jakarta.ejb.Remove;
import jakarta.ejb.Stateful;
import jakarta.ejb.SessionContext;
import javax.annotation.Resource;
import com.sun.ts.tests.ejb30.common.equals.ShoppingCartIF;
import com.sun.ts.tests.ejb30.common.equals.LocalShoppingCartIF;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;

@Stateful(name = "shopping-cart-bean")
@Remote({ ShoppingCartIF.class })
@Local({ LocalShoppingCartIF.class })
@TransactionManagement(TransactionManagementType.BEAN)
// use BMT so we can safely remove beans
public class ShoppingCartBean
// implements CartIF, ShoppingCartIF, LocalCartIF, LocalShoppingCartIF
{
  @Resource(name = "sessionContext")
  private SessionContext sessionContext;

  protected SessionContext getSessionContext() {
    return sessionContext;
  }

  public ShoppingCartBean() {
  }

  @Remove
  public void remove() {
  }

}
