/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.lite.view.equals;

import javax.ejb.EJB;

import com.sun.ts.tests.ejb30.common.busiface.AnnotatedLocalBusinessInterface1;
import com.sun.ts.tests.ejb30.common.busiface.BusinessLocalIF1;
import com.sun.ts.tests.ejb30.common.busiface.BusinessLocalIF2;
import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;

public class Client extends EJBLiteClientBase {

  @EJB(name = "noInterfaceStateless", beanName = "StatelessEqualsBean")
  private StatelessEqualsBean noInterfaceStateless;

  @EJB(name = "noInterfaceStatelessAgain", beanName = "StatelessEqualsBean")
  private StatelessEqualsBean noInterfaceStatelessAgain;

  @EJB(beanName = "StatelessEqualsBean", name = "businessLocalIF1Stateless")
  private BusinessLocalIF1 businessLocalIF1Stateless;

  @EJB(beanName = "StatelessEqualsBean", name = "businessLocalIF2Stateless")
  private BusinessLocalIF2 businessLocalIF2Stateless;

  @EJB(beanName = "StatelessEqualsBean", name = "annotatedLocalBusinessInterface1Stateless")
  private AnnotatedLocalBusinessInterface1 annotatedLocalBusinessInterface1Stateless;

  @EJB(name = "noInterfaceStateful", beanName = "StatefulEqualsBean")
  private StatefulEqualsBean noInterfaceStateful;

  @EJB(name = "noInterfaceStatefulAgain", beanName = "StatefulEqualsBean")
  private StatefulEqualsBean noInterfaceStatefulAgain;

  @EJB(beanName = "StatefulEqualsBean", name = "businessLocalIF1Stateful")
  private BusinessLocalIF1 businessLocalIF1Stateful;

  @EJB(beanName = "StatefulEqualsBean", name = "businessLocalIF2Stateful")
  private BusinessLocalIF2 businessLocalIF2Stateful;

  @EJB(beanName = "SingletonEqualsBean", name = "businessLocalIF1Singleton")
  private BusinessLocalIF1 businessLocalIF1Singleton;

  @EJB(beanName = "SingletonEqualsBean", name = "businessLocalIF1SingletonAgain")
  private BusinessLocalIF1 businessLocalIF1SingletonAgain;

  @EJB(beanName = "SingletonEqualsBean", name = "businessLocalIF2Singleton")
  private BusinessLocalIF2 businessLocalIF2Singleton;

  /*
   * @testName: statelessEquals
   */
  public void statelessEquals() {
    StatelessEqualsBean b0 = (StatelessEqualsBean) lookup(
        "noInterfaceStateless", "StatelessEqualsBean",
        StatelessEqualsBean.class);
    StatelessEqualsBean b00 = (StatelessEqualsBean) lookup(
        "noInterfaceStatelessAgain", "StatelessEqualsBean",
        StatelessEqualsBean.class);

    // self equals, equals among bean refs to the same EJB of the same business
    // interface
    assertEquals(null, noInterfaceStateless, noInterfaceStateless);
    assertEquals(null, b0, b0);
    assertEquals(null, b0, b00);
    assertEquals(null, noInterfaceStateless, b0);
    assertEquals(null, noInterfaceStateless, noInterfaceStatelessAgain);

    // not equals if having different business interface
    assertNotEquals(null, noInterfaceStateless, businessLocalIF1Stateless);
    assertNotEquals(null, noInterfaceStateless, businessLocalIF2Stateless);
    assertNotEquals(null, noInterfaceStateless,
        annotatedLocalBusinessInterface1Stateless);

    assertNotEquals(null, businessLocalIF1Stateless, businessLocalIF2Stateless);
    assertNotEquals(null, businessLocalIF1Stateless,
        annotatedLocalBusinessInterface1Stateless);

    assertNotEquals(null, annotatedLocalBusinessInterface1Stateless,
        businessLocalIF2Stateless);

    // different bean types of the same business interface are not equal
    assertNotEquals(null, businessLocalIF1Singleton, businessLocalIF1Stateless);
    assertNotEquals(null, businessLocalIF1Stateful, businessLocalIF1Stateless);
    assertNotEquals(null, businessLocalIF1Stateful, businessLocalIF1Singleton);

    // bean ref from calling getBusinessObject
    StatelessEqualsBean bob0 = noInterfaceStateless
        .getBusinessObject(StatelessEqualsBean.class);
    StatelessEqualsBean bob00 = noInterfaceStateless
        .getBusinessObject(StatelessEqualsBean.class);

    assertEquals(null, bob0, bob00);
    assertEquals(null, bob00, noInterfaceStatelessAgain);

    StatelessEqualsBean boba = noInterfaceStatelessAgain
        .getBusinessObject(StatelessEqualsBean.class);
    StatelessEqualsBean bobaa = noInterfaceStatelessAgain
        .getBusinessObject(StatelessEqualsBean.class);

    assertEquals(null, boba, bob0);
    assertEquals(null, bobaa, noInterfaceStateless);
  }

  /*
   * @testName: statefulEquals
   */
  public void statefulEquals() {
    StatefulEqualsBean b0 = (StatefulEqualsBean) lookup("noInterfaceStateful",
        "StatefulEqualsBean", StatefulEqualsBean.class);
    StatefulEqualsBean b00 = (StatefulEqualsBean) lookup(
        "noInterfaceStatefulAgain", "StatefulEqualsBean",
        StatefulEqualsBean.class);

    // self equals, not equals among bean refs to the same EJB of the same
    // business interface
    assertEquals(null, noInterfaceStateful, noInterfaceStateful);
    assertEquals(null, b0, b0);
    assertNotEquals(null, b00, b0);
    assertNotEquals(null, noInterfaceStateful, b0);
    assertNotEquals(null, noInterfaceStateful, noInterfaceStatefulAgain);

    // not equals if having different business interface
    assertNotEquals(null, noInterfaceStateful, businessLocalIF1Stateful);
    assertNotEquals(null, noInterfaceStateful, businessLocalIF2Stateful);

    assertNotEquals(null, businessLocalIF1Stateful, businessLocalIF2Stateful);

    // bean ref from calling getBusinessObject
    StatefulEqualsBean bob0 = noInterfaceStateful
        .getBusinessObject(StatefulEqualsBean.class);
    StatefulEqualsBean bob00 = noInterfaceStateful
        .getBusinessObject(StatefulEqualsBean.class);

    assertEquals(null, bob0, bob00);
    assertEquals(null, bob00, noInterfaceStateful);

    StatefulEqualsBean boba = noInterfaceStatefulAgain
        .getBusinessObject(StatefulEqualsBean.class);
    StatefulEqualsBean bobaa = noInterfaceStatefulAgain
        .getBusinessObject(StatefulEqualsBean.class);

    assertNotEquals(null, boba, bob0);
    assertNotEquals(null, bobaa, noInterfaceStateful);
  }

  /*
   * @testName: singletonEquals
   */
  public void singletonEquals() {
    BusinessLocalIF1 b0 = (BusinessLocalIF1) lookup("businessLocalIF1Singleton",
        "SingletonEqualsBean", BusinessLocalIF1.class);
    BusinessLocalIF1 b00 = (BusinessLocalIF1) lookup(
        "businessLocalIF1SingletonAgain", "SingletonEqualsBean",
        BusinessLocalIF1.class);

    // self equals, equals among bean refs to the same EJB of the same business
    // interface
    assertEquals(null, b0, b0);
    assertEquals(null, b0, b00);
    assertEquals(null, businessLocalIF1Singleton,
        businessLocalIF1SingletonAgain);

    // not equals if having different business interface
    assertNotEquals(null, businessLocalIF1Singleton, businessLocalIF2Singleton);
  }

}
