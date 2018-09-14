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

package com.sun.ts.tests.ejb30.bb.session.stateless.equals.annotated;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.SessionContext;
import javax.annotation.Resource;
import com.sun.ts.tests.ejb30.common.equals.CartIF;
import com.sun.ts.tests.ejb30.common.equals.LocalCartIF;
import com.sun.ts.tests.ejb30.common.equals.ShoppingCartIF;
import com.sun.ts.tests.ejb30.common.equals.LocalShoppingCartIF;

@Stateless(name = "shopping-cart-bean")
@Remote({ ShoppingCartIF.class })
@Local({ LocalShoppingCartIF.class })
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

  public void remove() {
  }

}
