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

package com.sun.ts.tests.ejb30.bb.session.stateful.equals.descriptor;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import com.sun.ts.tests.ejb30.common.equals.Comparator;
import java.util.Properties;
import javax.naming.NamingException;

public class Client extends EETest {
  protected static final boolean CLIENT_LOG_IF_OK = true;

  protected Properties props;

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
  }

  public void cleanup() throws Fault {
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  //////////////////////////////////////////////////////////////////////

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
   * @testName: otherNotEqualsLookup
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: cart1.equals(cart2) should return false for stateful beans.
   */
  public void otherNotEqualsLookup() throws Fault {
    try {
      Object cart = ServiceLocator.lookupByShortName("cart");
      Object cartOther = ServiceLocator.lookupByShortName("cartOther");
      Object shoppingCart = ServiceLocator.lookupByShortName("shoppingCart");
      Object shoppingCartOther = ServiceLocator
          .lookupByShortName("shoppingCartOther");
      Comparator.compare(cart, cartOther, false, CLIENT_LOG_IF_OK);
      Comparator.compare(shoppingCart, shoppingCartOther, false,
          CLIENT_LOG_IF_OK);
    } catch (TestFailedException e) {
      throw new Fault(e);
    } catch (NamingException e) {
      throw new Fault(e);
    }
  }

  /*
   * @testName: differentInterfaceNotEqualLookup
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: cart1.equals(shoppingCart) should return false for stateful
   * beans.
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
   * @testName: differentBeanSameInterfaceNotEqualLookup
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: cart1.equals(cart2) should return false for stateful beans.
   */
  public void differentBeanSameInterfaceNotEqualLookup() throws Fault {
    try {
      Object cart = ServiceLocator.lookupByShortName("cart");
      Object cart2 = ServiceLocator.lookupByShortName("cart2");
      Comparator.compare(cart, cart2, false, CLIENT_LOG_IF_OK);
      TLogger.log("cart.hashCode =" + cart.hashCode());
      TLogger.log("cart2.hashCode=" + cart2.hashCode());
      java.util.HashSet carts = new java.util.HashSet();
      carts.add(cart);
      carts.add(cart2);
      TLogger.log("carts=" + carts);

      Object shoppingCart = ServiceLocator.lookupByShortName("shoppingCart");
      Object shoppingCart2 = ServiceLocator.lookupByShortName("shoppingCart2");
      Comparator.compare(shoppingCart, shoppingCart2, false, CLIENT_LOG_IF_OK);
      TLogger.log("shoppingCart.hashCode =" + shoppingCart.hashCode());
      TLogger.log("shoppingCart2.hashCode=" + shoppingCart2.hashCode());
      java.util.HashSet shoppingCarts = new java.util.HashSet();
      shoppingCarts.add(shoppingCart);
      shoppingCarts.add(shoppingCart2);
      TLogger.log("shoppingCarts=" + shoppingCarts);
    } catch (TestFailedException e) {
      throw new Fault(e);
    } catch (NamingException e) {
      throw new Fault(e);
    }
  }
}
