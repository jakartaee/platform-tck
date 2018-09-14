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

package com.sun.ts.tests.ejb30.common.equals;

import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.naming.NamingException;

@Stateless(name = "test-bean")
@Remote
public class TestBean extends TestBeanBase implements TestIF {
  @Resource(name = "sessionContext")
  private SessionContext sessionContext;

  protected SessionContext getSessionContext() {
    return sessionContext;
  }

  @EJB(name = "cart")
  private LocalCartIF cart;

  @EJB(name = "cartOther")
  private LocalCartIF cartOther;

  @EJB(name = "shoppingCart")
  private LocalShoppingCartIF shoppingCart;

  @EJB(name = "shoppingCartOther")
  private LocalShoppingCartIF shoppingCartOther;

  public void remove() {
  }

  //////////////////////////////////////////////////////////////////////
  public void selfEquals() throws TestFailedException {
    Comparator.compare(cart, cart, true, LOG_IF_OK);
    Comparator.compare(shoppingCart, shoppingCart, true, LOG_IF_OK);
  }

  public void selfEqualsLookup() throws TestFailedException {
    try {
      Object cart = ServiceLocator.lookupByShortName("cart");
      Object shoppingCart = ServiceLocator.lookupByShortName("shoppingCart");
      Comparator.compare(cart, cart, true, LOG_IF_OK);
      Comparator.compare(shoppingCart, shoppingCart, true, LOG_IF_OK);

      cart = getSessionContext().lookup("cart");
      shoppingCart = getSessionContext().lookup("shoppingCart");
      Comparator.compare(cart, cart, true, LOG_IF_OK);
      Comparator.compare(shoppingCart, shoppingCart, true, LOG_IF_OK);
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
  }

  public void otherEquals() throws TestFailedException {
    Comparator.compare(cart, cartOther, true, LOG_IF_OK);
    Comparator.compare(shoppingCart, shoppingCartOther, true, LOG_IF_OK);
  }

  public void otherEqualsLookup() throws TestFailedException {
    try {
      Object cart = ServiceLocator.lookupByShortName("cart");
      Object cartOther = ServiceLocator.lookupByShortName("cartOther");
      Object shoppingCart = ServiceLocator.lookupByShortName("shoppingCart");
      Object shoppingCartOther = ServiceLocator
          .lookupByShortName("shoppingCartOther");
      Comparator.compare(cart, cartOther, this.cart, true, LOG_IF_OK);
      Comparator.compare(shoppingCart, shoppingCartOther, this.shoppingCart,
          true, LOG_IF_OK);

      cart = getSessionContext().lookup("cart");
      cartOther = getSessionContext().lookup("cartOther");
      shoppingCart = getSessionContext().lookup("shoppingCart");
      shoppingCartOther = getSessionContext().lookup("shoppingCartOther");
      Comparator.compare(cart, cartOther, this.cart, true, LOG_IF_OK);
      Comparator.compare(shoppingCart, shoppingCartOther, this.shoppingCart,
          true, LOG_IF_OK);
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
  }

  public void differentInterfaceNotEqual() throws TestFailedException {
    Comparator.compare(cart, shoppingCart, false, LOG_IF_OK);
  }

  public void differentInterfaceNotEqualLookup() throws TestFailedException {
    try {
      Object cart = ServiceLocator.lookupByShortName("cart");
      Object shoppingCart = ServiceLocator.lookupByShortName("shoppingCart");
      Comparator.compare(cart, shoppingCart, false, LOG_IF_OK);
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////
  // disable methods for stateful beans
  //////////////////////////////////////////////////////////////////////
  public void otherNotEqualsLookup() throws TestFailedException {
    throw new TestFailedException(
        "This method should not be invoked for this test.");
  }

  public void otherNotEquals() throws TestFailedException {
    throw new TestFailedException(
        "This method should not be invoked for this test.");
  }
}
