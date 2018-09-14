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
package com.sun.ts.tests.ejb30.lite.view.common;

import java.util.Arrays;
import java.util.List;

import javax.ejb.EJB;

import com.sun.ts.tests.ejb30.common.busiface.AnnotatedLocalBusinessInterface1;
import com.sun.ts.tests.ejb30.common.busiface.AnnotatedLocalBusinessInterface2;
import com.sun.ts.tests.ejb30.common.busiface.BusinessLocal1Base;
import com.sun.ts.tests.ejb30.common.busiface.BusinessLocalIF1;
import com.sun.ts.tests.ejb30.common.busiface.BusinessLocalIF2;
import com.sun.ts.tests.ejb30.common.busiface.Constants;
import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;

public class ClientBase extends EJBLiteClientBase {
  protected static final String[] args = { Constants.VALUE };

  protected static final String[] resultReset = { Constants.VALUE_RESET };

  @EJB(beanName = "SerializableLocalBean")
  protected BusinessLocalIF1 serializableLocalBean;

  @EJB(beanName = "ExternalizableLocalBean")
  protected BusinessLocalIF1 externalizableLocalBean;

  @EJB(beanName = "SessionBeanLocalBean")
  protected BusinessLocalIF1 sessionBeanLocalBean;

  @EJB(beanName = "BusinessBean")
  protected BusinessLocalIF1 businessBean1;

  @EJB(beanName = "BusinessBean")
  protected BusinessLocalIF2 businessBean2;

  @EJB(beanName = "AnnotatedInterfaceBean")
  protected AnnotatedLocalBusinessInterface1 annotatedInterfaceBean1;

  @EJB(beanName = "AnnotatedInterfaceBean")
  protected AnnotatedLocalBusinessInterface2 annotatedInterfaceBean2;

  // injected in subclass
  protected BusinessLocal1Base localAndNoInterfaceBeanNoInterfaceView;

  @EJB(beanName = "LocalAndNoInterfaceBean")
  protected BusinessLocalIF1 localAndNoInterfaceBeanLocalView;

  @EJB(beanName = "SuperclassBean")
  private BusinessLocalIF1 superclassBean;

  /*
   * testName: multipleInterfacesLocal
   * 
   * @assertion_ids: EJB:JAVADOC:125
   * 
   * @test_Strategy:
   */
  public void multipleInterfacesLocal() {
    assertEquals("Multi local interfaces:", resultReset[0],
        businessBean1.businessMethodLocal1(args)[0]);
    assertEquals("Multi local interfaces:", resultReset[0],
        businessBean2.businessMethodLocal2(args)[0]);
  }

  /*
   * testName: singleInterfaceLocal
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: overridden in stateful to skip TimedObjectLocalBean
   */
  public void singleInterfaceLocal() {
    assertEquals("local interface & Serializable:", resultReset[0],
        serializableLocalBean.businessMethodLocal1(args)[0]);
    assertEquals("local interface & Externalizable:", resultReset[0],
        externalizableLocalBean.businessMethodLocal1(args)[0]);
    assertEquals("local interface & SessionBean:", resultReset[0],
        sessionBeanLocalBean.businessMethodLocal1(args)[0]);
  }

  /*
   * testName: multipleAnnotatedInterfacesLocal
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   */
  public void multipleAnnotatedInterfacesLocal() {
    assertEquals("Multi @local interfaces:", resultReset[0],
        annotatedInterfaceBean1.businessMethodLocal1(args)[0]);
    assertEquals("Multi @local interfaces:", resultReset[0],
        annotatedInterfaceBean2.businessMethodLocal1(args)[0]);
  }

  /*
   * testName: localAndNoInterfaceView
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   */
  public void localAndNoInterfaceView() {
    appendReason(
        "localAndNoInterfaceBeanLocalView: " + localAndNoInterfaceBeanLocalView,
        "localAndNoInterfaceBeanNoInterfaceView: "
            + localAndNoInterfaceBeanNoInterfaceView);
    assertEquals("Local and no-interface view:", resultReset[0],
        localAndNoInterfaceBeanLocalView.businessMethodLocal1(args)[0]);
    assertEquals("Local and no-interface view:", resultReset[0],
        localAndNoInterfaceBeanNoInterfaceView.businessMethodLocal1(args)[0]);
  }

  /*
   * testName: getBusinessObjectInSuperclassBean
   * 
   * @test_Strategy: Try to get the business object with a particular interface,
   * and call a method on the obtained business object. It verifies that the
   * bean SuperclassBean only expose a local interface BusinessLocalIF1, and no
   * no-interface view.
   */
  public void getBusinessObjectInSuperclassBean() {
    List<String> expected = Arrays.asList(
        IllegalStateException.class.getSimpleName(),
        SuperclassBean.class.getSimpleName());
    String[] businessObjects = new String[2];
    superclassBean.businessMethodLocal1(businessObjects);
    assertEquals(null, expected, Arrays.asList(businessObjects));
  }
}
