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

package com.sun.ts.tests.ejb30.common.equals;

import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import javax.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.Remote;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;
import javax.naming.NamingException;

@Stateless(name = "test-bean-for-stateful")
@Remote

public class TestBeanForStateful extends TestBeanBase implements TestIF {
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

      removeCart(cart);
      removeShoppingCart(shoppingCart);

      cart = getSessionContext().lookup("cart");
      shoppingCart = getSessionContext().lookup("shoppingCart");
      Comparator.compare(cart, cart, true, LOG_IF_OK);
      Comparator.compare(shoppingCart, shoppingCart, true, LOG_IF_OK);

      removeCart(cart);
      removeShoppingCart(shoppingCart);
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
  }

  public void otherNotEquals() throws TestFailedException {
    Comparator.compare(cart, cartOther, false, LOG_IF_OK);
    Comparator.compare(shoppingCart, shoppingCartOther, false, LOG_IF_OK);
  }

  public void otherNotEqualsLookup() throws TestFailedException {
    try {
      Object cart = ServiceLocator.lookupByShortName("cart");
      Object cartOther = ServiceLocator.lookupByShortName("cartOther");
      Object shoppingCart = ServiceLocator.lookupByShortName("shoppingCart");
      Object shoppingCartOther = ServiceLocator
          .lookupByShortName("shoppingCartOther");
      Comparator.compare(cart, cartOther, this.cart, false, LOG_IF_OK);
      Comparator.compare(shoppingCart, shoppingCartOther, this.shoppingCart,
          false, LOG_IF_OK);

      removeCart(cart);
      removeCart(cartOther);
      removeShoppingCart(shoppingCart);
      removeShoppingCart(shoppingCartOther);

      cart = getSessionContext().lookup("cart");
      cartOther = getSessionContext().lookup("cartOther");
      shoppingCart = getSessionContext().lookup("shoppingCart");
      shoppingCartOther = getSessionContext().lookup("shoppingCartOther");
      Comparator.compare(cart, cartOther, this.cart, false, LOG_IF_OK);
      Comparator.compare(shoppingCart, shoppingCartOther, this.shoppingCart,
          false, LOG_IF_OK);

      removeCart(cart);
      removeCart(cartOther);
      removeShoppingCart(shoppingCart);
      removeShoppingCart(shoppingCartOther);
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

      removeCart(cart);
      removeShoppingCart(shoppingCart);
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////
  // disable methods for stateless beans
  //////////////////////////////////////////////////////////////////////
  public void otherEqualsLookup() throws TestFailedException {
    throw new TestFailedException(
        "This method should not be invoked for this test.");
  }

  public void otherEquals() throws TestFailedException {
    throw new TestFailedException(
        "This method should not be invoked for this test.");
  }

  //////////////////////////////////////////////////////////////////////
  private void removeCart(Object aCart) {
    ((LocalCartIF) aCart).remove();
  }

  private void removeShoppingCart(Object aShoppingCart) {
    ((LocalShoppingCartIF) aShoppingCart).remove();
  }
}
