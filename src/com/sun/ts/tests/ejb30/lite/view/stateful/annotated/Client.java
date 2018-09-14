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
package com.sun.ts.tests.ejb30.lite.view.stateful.annotated;

import java.util.Arrays;
import java.util.List;

import javax.ejb.EJB;

public class Client extends com.sun.ts.tests.ejb30.lite.view.common.ClientBase {

  @EJB(beanName = "LocalAndNoInterfaceBean", beanInterface = LocalAndNoInterfaceBean.class)
  protected void setLocalAndNoInterfaceBeanNoInterfaceView(
      LocalAndNoInterfaceBean b) {
    this.localAndNoInterfaceBeanNoInterfaceView = b;
  }

  @EJB(beanName = "SubclassExtendsPOJOBean")
  private SubclassExtendsPOJOBean subclassExtendsPOJOBean;

  @EJB(beanName = "SubclassExtendsBeanBean")
  private SubclassExtendsBeanBean subclassExtendsBeanBean;

  /*
   * @testName: singleInterfaceLocal
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: overridden in stateful to skip TimedObjectLocalBean
   */
  @Override
  public void singleInterfaceLocal() {
    assertEquals("local interface & Serializable:", resultReset[0],
        serializableLocalBean.businessMethodLocal1(args)[0]);
    assertEquals("local interface & Externalizable:", resultReset[0],
        externalizableLocalBean.businessMethodLocal1(args)[0]);
    assertEquals("local interface & SessionBean:", resultReset[0],
        sessionBeanLocalBean.businessMethodLocal1(args)[0]);
  }

  /*
   * @testName: multipleInterfacesLocal
   * 
   * @assertion_ids: EJB:JAVADOC:125
   * 
   * @test_Strategy:
   */
  /*
   * @testName: multipleAnnotatedInterfacesLocal
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   */
  /*
   * @testName: localAndNoInterfaceView
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   */

  /*
   * @testName: getBusinessObjectInSubclassBean
   * 
   * @test_Strategy:Try to get the business object with a particular interface,
   * and call a method on the obtained business object. Expecting 2 results, the
   * first result is the IllegalStateException when getting business object with
   * BusinessLocalIF1; the second result is "SubclassBean". This is to verify
   * that BusinessLocalIF1, which is implemented by the superclass of the bean
   * class is not exposed as a business interface of the subclass bean.
   */
  public void getBusinessObjectInSubclassBean() {
    List<String> expected = Arrays.asList(
        IllegalStateException.class.getSimpleName(),
        SubclassExtendsPOJOBean.class.getSimpleName());
    String[] businessObjects = new String[2];
    subclassExtendsPOJOBean.businessMethodLocal1(businessObjects);
    assertEquals(null, expected, Arrays.asList(businessObjects));

    List<String> expected2 = Arrays.asList(
        IllegalStateException.class.getSimpleName(),
        SubclassExtendsBeanBean.class.getSimpleName());
    String[] businessObjects2 = new String[2];
    subclassExtendsBeanBean.businessMethodLocal1(businessObjects2);
    assertEquals(null, expected2, Arrays.asList(businessObjects2));
  }
}
