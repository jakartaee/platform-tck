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

import javax.ejb.EJB;
import javax.naming.NamingException;

import com.sun.javatest.Status;
import com.sun.ts.tests.ejb30.common.equals.CartIF;
import com.sun.ts.tests.ejb30.common.equals.ClientBase;
import com.sun.ts.tests.ejb30.common.equals.Comparator;
import com.sun.ts.tests.ejb30.common.equals.ShoppingCartIF;
import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;

public class Client extends ClientBase {
  // testBean is injected into ClientBase

  @EJB(name = "cart")
  private static CartIF cart;

  @EJB(name = "cartOther")
  private static CartIF cartOther;

  @EJB(name = "shoppingCart")
  private static ShoppingCartIF shoppingCart;

  @EJB(name = "shoppingCartOther")
  private static ShoppingCartIF shoppingCartOther;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  //////////////////////////////////////////////////////////////////////
  /*
   * @testName: equalsNull
   * 
   * @test_Strategy: return false, no NPE.
   *
   */
  public void equalsNull() throws TestFailedException {
    TLogger.log("Comparing " + cart + " to " + null + ", expecting false.");
    Comparator.compare(cart, null, false, CLIENT_LOG_IF_OK);

    TLogger
        .log("Comparing " + cartOther + " to " + null + ", expecting false.");
    Comparator.compare(cartOther, null, false, CLIENT_LOG_IF_OK);

    TLogger.log(
        "Comparing " + shoppingCart + " to " + null + ", expecting false.");
    Comparator.compare(shoppingCart, null, false, CLIENT_LOG_IF_OK);

    TLogger.log("Comparing " + shoppingCartOther + " to " + null
        + ", expecting false.");
    Comparator.compare(shoppingCartOther, null, false, CLIENT_LOG_IF_OK);
  }

  /*
   * @testName: selfEquals
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: cart1.equals(cart1) should return true
   */
  public void selfEquals() throws Fault {
    try {
      Comparator.compare(cart, cart, true, CLIENT_LOG_IF_OK);
      Comparator.compare(shoppingCart, shoppingCart, true, CLIENT_LOG_IF_OK);
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }

  /*
   * @testName: selfEqualsLookup
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: cart1.equals(cart1) should return true
   */
  public void selfEqualsLookup() throws Fault {
    try {
      Object cart = ServiceLocator.lookupByShortName("cart");
      Object shoppingCart = ServiceLocator.lookupByShortName("shoppingCart");
      Comparator.compare(cart, cart, true, CLIENT_LOG_IF_OK);
      Comparator.compare(shoppingCart, shoppingCart, true, CLIENT_LOG_IF_OK);
    } catch (TestFailedException e) {
      throw new Fault(e);
    } catch (NamingException e) {
      throw new Fault(e);
    }
  }

  /*
   * @testName: otherEquals
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: cart1.equals(cart2) should return true for stateless beans.
   */
  public void otherEquals() throws Fault {
    try {
      Comparator.compare(cart, cartOther, true, CLIENT_LOG_IF_OK);
      Comparator.compare(shoppingCart, shoppingCartOther, true,
          CLIENT_LOG_IF_OK);
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }

  /*
   * @testName: otherEqualsLookup
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: cart1.equals(cart2) should return true for stateless beans.
   */
  public void otherEqualsLookup() throws Fault {
    try {
      Object cart = ServiceLocator.lookupByShortName("cart");
      Object cartOther = ServiceLocator.lookupByShortName("cartOther");
      Object shoppingCart = ServiceLocator.lookupByShortName("shoppingCart");
      Object shoppingCartOther = ServiceLocator
          .lookupByShortName("shoppingCartOther");
      Comparator.compare(cart, cartOther, cart, true, CLIENT_LOG_IF_OK);
      Comparator.compare(shoppingCart, shoppingCartOther, shoppingCart, true,
          CLIENT_LOG_IF_OK);
    } catch (TestFailedException e) {
      throw new Fault(e);
    } catch (NamingException e) {
      throw new Fault(e);
    }
  }

  /*
   * @testName: differentInterfaceNotEqual
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: cart1.equals(shoppingCart) should return false for
   * stateless beans.
   */
  public void differentInterfaceNotEqual() throws Fault {
    try {
      Comparator.compare(cart, shoppingCart, false, CLIENT_LOG_IF_OK);
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }

  /*
   * @testName: differentInterfaceNotEqualLookup
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: cart1.equals(shoppingCart) should return false for
   * stateless beans.
   */
  public void differentInterfaceNotEqualLookup() throws Fault {
    try {
      Object cart = ServiceLocator.lookupByShortName("cart");
      Object shoppingCart = ServiceLocator.lookupByShortName("shoppingCart");
      Comparator.compare(cart, shoppingCart, false, CLIENT_LOG_IF_OK);
    } catch (TestFailedException e) {
      throw new Fault(e);
    } catch (NamingException e) {
      throw new Fault(e);
    }
  }

  /*
   * @testName: testBeanselfEquals
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: testBeanotherEquals
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: testBeandifferentInterfaceNotEqual
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: testBeanselfEqualsLookup
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: testBeanotherEqualsLookup
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: testBeandifferentInterfaceNotEqualLookup
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */

}
