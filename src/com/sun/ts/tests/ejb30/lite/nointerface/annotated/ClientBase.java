/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.lite.nointerface.annotated;

import javax.ejb.EJBException;
import javax.ejb.NoSuchEJBException;

import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;
import com.sun.ts.tests.ejb30.common.lite.NumberEnum;

/**
 * no interface view EJBs are used in many other test directories. This
 * directory covers uncommon requirements.
 */
public class ClientBase extends EJBLiteClientBase {
  protected BeanBase stateless;

  protected BeanBase stateful;

  protected BeanBase statefulToBeRemoved;

  protected BeanBase singleton;

  protected HasInterface hasInterfaceSingleton;

  private String nonBusinessMethods(BeanBase b) throws RuntimeException {
    String result = "";
    try {
      throw new RuntimeException(b.nonBusinessMethod());
    } catch (EJBException expected) {
      result += " Got expected when invoking a protected method on no interface bean:"
          + expected;
    }
    try {
      throw new RuntimeException(b.nonBusinessMethod2());
    } catch (EJBException expected) {
      result += " Got expected when invoking a package-default-access method on no interface bean:"
          + expected;
    }
    return result;
  }

  /*
   * testName: nonBusinessMethods
   * 
   * @test_Strategy: Invoking non-public methods results in EJBException.
   */
  public void nonBusinessMethods() {
    appendReason(nonBusinessMethods(stateless), nonBusinessMethods(stateful),
        nonBusinessMethods(singleton));
  }

  /*
   * testName: invokeRemovedStateful
   * 
   * @test_Strategy: Invoking a removed stateful no-interface bean results in
   * javax.ejb.NoSuchEJBException. Also verify the overloaded method with same
   * method name but different params is not considered a remove-method.
   */
  public void invokeRemovedStateful() {
    statefulToBeRemoved.remove(false);
    statefulToBeRemoved.remove(false);
    statefulToBeRemoved.remove();
    try {
      String result = statefulToBeRemoved.passAsParam(singleton);
      throw new RuntimeException(
          "Expected javax.ejb.NoSuchEJBException when invoking a removed stateful no-interface bean, but got "
              + result);
    } catch (NoSuchEJBException expected) {
      appendReason(
          "Got expected when invoking a removed stateful no-interface bean",
          expected);
    }
  }

  /*
   * testName: passAsParam
   * 
   * @test_Strategy: no-interface view bean reference can be passed by param of
   * any local business interface or no-interface method. Using varargs...
   */
  public void passAsParam() {
    appendReason(stateless.passAsParam(stateless),
        stateless.passAsParam(stateful), stateless.passAsParam(singleton),
        stateless.passAsParam(stateless, stateful, singleton),
        stateful.passAsParam(stateless), stateful.passAsParam(stateful),
        stateful.passAsParam(singleton),
        stateful.passAsParam(stateless, stateful, singleton),
        singleton.passAsParam(stateless), singleton.passAsParam(stateful),
        singleton.passAsParam(singleton),
        singleton.passAsParam(stateless, stateful, singleton),
        hasInterfaceSingleton.passAsParam(stateless),
        hasInterfaceSingleton.passAsParam(stateful),
        hasInterfaceSingleton.passAsParam(singleton),
        hasInterfaceSingleton.passAsParam(stateless, stateful, singleton));
  }

  /*
   * testName: passAsReturn
   * 
   * @test_Strategy: no-interface view bean reference can be passed by param of
   * any local business interface or no-interface method. Using covariant return
   * types. Also tests that injected no-interface beans can be looked up via
   * jndi.
   */
  public void passAsReturn() {
    BeanBase less = (BeanBase) lookup("ejb/stateless",
        "NoInterfaceStatelessBean", null);
    BeanBase ful = (BeanBase) lookup("ejb/stateful", "NoInterfaceStatefulBean",
        null);
    BeanBase sing = (BeanBase) lookup("ejb/singleton",
        "NoInterfaceSingletonBean", null);
    appendReason(less.passAsReturn(), ful.passAsReturn(), sing.passAsReturn(),
        hasInterfaceSingleton.passAsReturn());
  }

  /*
   * testName: passEnumAsParams
   * 
   * @test_Strategy: pass (NumberEnum, NumberIF) to each bean, which returns the
   * sum.
   */
  public void passEnumAsParams() {
    int expected = NumberEnum.ONE.add(NumberEnum.TWO.getNumber());
    assertEquals(null, expected,
        stateless.passEnumAsParams(NumberEnum.ONE, NumberEnum.TWO));
    assertEquals(null, expected,
        stateful.passEnumAsParams(NumberEnum.ONE, NumberEnum.TWO));
    assertEquals(null, expected,
        singleton.passEnumAsParams(NumberEnum.ONE, NumberEnum.TWO));
    assertEquals(null, expected,
        hasInterfaceSingleton.passEnumAsParams(NumberEnum.ONE, NumberEnum.TWO));
  }

  /*
   * testName: passEnumAsReturn
   * 
   * @test_Strategy: pass (NumberEnum, NumberIF) to each bean, which returns the
   * sum.
   */
  public void passEnumAsReturn() {
    NumberEnum expected = NumberEnum.THREE;
    assertEquals(null, expected,
        stateless.passEnumAsReturn(expected.getNumber()));
    assertEquals(null, expected,
        stateful.passEnumAsReturn(expected.getNumber()));
    assertEquals(null, expected,
        singleton.passEnumAsReturn(expected.getNumber()));
    assertEquals(null, expected,
        hasInterfaceSingleton.passEnumAsReturn(expected.getNumber()));
  }
}
