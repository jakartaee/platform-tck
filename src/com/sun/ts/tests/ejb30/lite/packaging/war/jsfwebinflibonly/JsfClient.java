/*
 * Copyright (c) 2022 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.lite.packaging.war.jsfwebinflibonly;

import static com.sun.ts.tests.ejb30.common.helper.ServiceLocator.lookupByShortNameNoTry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase;
import com.sun.ts.tests.ejb30.lite.packaging.war.webinflib.BeanBase;
import com.sun.ts.tests.ejb30.lite.packaging.war.webinflib.OneBean;
import com.sun.ts.tests.ejb30.lite.packaging.war.webinflib.ThreeBean;
import com.sun.ts.tests.ejb30.lite.packaging.war.webinflib.TwoBean;

import jakarta.annotation.Resource;
import jakarta.transaction.UserTransaction;

/**
 * See com.sun.ts.tests.ejb30.lite.packaging.war.webinflib.Client. All ejb
 * classes are packaged under WEB-INF/lib/*.jar
 */
@jakarta.inject.Named("client")
@jakarta.enterprise.context.RequestScoped
public class JsfClient
    extends EJBLiteJsfClientBase implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String CLIENT_REF_NAME_PREFIX = JsfClient.class.getName();
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
