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
package com.sun.ts.tests.ejb30.lite.packaging.war.webinflib;

import static com.sun.ts.tests.ejb30.common.helper.ServiceLocator.lookupByShortNameNoTry;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.transaction.UserTransaction;

import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;

/**
 * .war packaging of EJB classes are used in many other test directories. This
 * directory covers uncommon packaging structures. Also covers: same class
 * loader, and same naming context between web components and EJBs, and across
 * multiple EJBs packaged in the same .war
 */
public class Client extends EJBLiteClientBase {
  private static final String CLIENT_REF_NAME_PREFIX = Client.class.getName();

  private BeanBase one;

  private BeanBase two;

  private BeanBase three;

  @Resource
  private UserTransaction ut;

  private List<BeanBase> beans;

  @Override
  public void setup(String[] args, Properties p) {
    super.setup(args, p);
    beans = new ArrayList<BeanBase>();
    one = (BeanBase) ServiceLocator
        .lookupNoTry("java:global/" + getModuleName() + "/OneBean");
    two = (BeanBase) ServiceLocator
        .lookupNoTry("java:global/" + getModuleName() + "/TwoBean");
    three = (BeanBase) ServiceLocator
        .lookupNoTry("java:global/" + getModuleName() + "/ThreeBean");
    beans.add(one);
    beans.add(two);
    beans.add(three);
  }

  // related issue 8297
  /*
   * @testName: circularInjection
   * 
   * @test_Strategy: package a stateless bean class in WEB-INF/lib/1.jar, and a
   * singleton bean class in WEB-INF/lib/2.jar, and a stateful in
   * WEB-INF/classes. Circular injections.
   */
  public void circularInjection() {
    for (BeanBase b : beans) {
      assertEquals(b.getInjectionRecords(), true, b.getInjectionStatus());
    }
  }

  /*
   * @testName: sameClassLoader
   * 
   * @test_Strategy: EJBs packaged in a .war have the same class loading
   * requirements as other non-EJB classes packaged in a .war.
   */
  public void sameClassLoader() {
    int expected = System.identityHashCode(getClass().getClassLoader());
    for (BeanBase b : beans) {
      assertEquals(null, expected, b.getClassLoaderId());
    }
  }

  /*
   * @testName: clientToBeanClassLookup
   * 
   * @test_Strategy: client looking up ejb-ref injections in bean class. It must
   * succeed since cleint and ejb packaged together share the same naming. Not
   * for standalone client.
   */
  public void clientToBeanClassLookup() {
    for (BeanBase b : beans) {
      OneBean b1 = (OneBean) lookupByShortNameNoTry(
          b.getRefNamePrefix() + "/one");
      TwoBean b2 = (TwoBean) lookupByShortNameNoTry(
          b.getRefNamePrefix() + "/two");
      ThreeBean b3 = (ThreeBean) lookupByShortNameNoTry(
          b.getRefNamePrefix() + "/three");
      verifyBeans(b1, b2, b3);
    }

    UserTransaction u = (UserTransaction) lookupByShortNameNoTry(
        BeanBase.class.getName() + "/ut");
    assertNotEquals(null, u, null);
  }

  /*
   * @testName: beanClassToClientLookup
   * 
   * @test_Strategy: bean looking up resource injected into client. It must
   * succeed since cleint and ejb packaged together share the same naming. Not
   * for standalone client.
   */
  public void beanClassToClientLookup() {
    for (BeanBase b : beans) {
      UserTransaction u = (UserTransaction) b
          .beanClassToClientLookup(CLIENT_REF_NAME_PREFIX + "/ut");
      assertNotEquals(null, u, null);
    }
  }

  /*
   * @testName: crossEJBLookup
   * 
   * @test_Strategy: EJBContext looking up ejb-ref injected into other beans. It
   * must succeed since ejbs packaged together share the same naming context.
   */
  public void crossEJBLookup() {
    for (BeanBase lookupFrom : beans) {
      for (BeanBase lookupDest : beans) {
        // look up all the OneBeans injected into One, Two, and Three.
        // A totoal of 27 lookups
        OneBean b1 = (OneBean) lookupFrom
            .lookupWithEJBContext(lookupDest.getRefNamePrefix() + "/one");
        TwoBean b2 = (TwoBean) lookupFrom
            .lookupWithEJBContext(lookupDest.getRefNamePrefix() + "/two");
        ThreeBean b3 = (ThreeBean) lookupFrom
            .lookupWithEJBContext(lookupDest.getRefNamePrefix() + "/three");
        verifyBeans(b1, b2, b3);
      }
    }
  }

  private void verifyBeans(Object b1, Object b2, Object b3) {
    assertEquals(null, one, b1);
    assertEquals(null, two, b2);
    assertEquals(null, three, b3);
  }
}
