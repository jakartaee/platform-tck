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
package com.sun.ts.tests.ejb30.lite.ejbcontext.common;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;

abstract public class ClientBase extends EJBLiteClientBase {
  // cannot inject here since the the beanInterface cannot be inferred from
  // field type. Specifying beanInterface introduces a dependency on concrete
  // bean classes. So inject it in subclass.
  protected EJBContextBeanBase noInterface;

  @EJB(beanName = "EJBContext2Bean")
  private EJBContextIF interface1;

  @EJB(beanName = "EJBContext2Bean")
  private EJBContext2IF interface2;

  // When updating this field, getInvokedBusinessInterface#expected variable
  // must also be updated.
  private EJBContextIF[] beans = new EJBContextIF[3];

  @SuppressWarnings("unused")
  @PostConstruct
  private void postConstruct() {
    beans[0] = noInterface;
    beans[1] = interface1;
    beans[2] = interface2;
  }

  // implemented by subclasses to inject
  abstract protected void setNoInterface(EJBContextBeanBase b);

  // classes in common directories should not directly reference classes in
  // specific subdirectories.
  abstract protected Class<? extends EJBContextBeanBase> getInvokedBusinessInterfaceForNoInterfaceView();

  /*
   * testName: lookupIllegalArgumentException
   * 
   * @test_Strategy: Call EJBContext.lookup with null and another non-existent
   * name, expecting IllegalArgumentException. Valid use of EJBContext.lookup
   * method are tested in various other directories and are not present here.
   */
  public void lookupIllegalArgumentException() throws TestFailedException {
    for (EJBContextIF b : beans) {
      appendReason(b.lookupIllegalArgumentException());
    }
  }

  /*
   * testName: getMessageContextIllegalStateException
   * 
   * @test_Strategy: Call EJBContext.getMessageContext() where it's not
   * supported, expecting IllegalStateException.
   */
  public void getMessageContextIllegalStateException()
      throws TestFailedException {
    for (EJBContextIF b : beans) {
      appendReason(b.getMessageContextIllegalStateException());
    }
  }

  /*
   * testName: getInvokedBusinessInterface
   * 
   * @test_Strategy: Call EJBContext.getInvokedBusinessInterface() on
   * no-interface, interface1 and interface2, and verify the returned interface.
   */
  public void getInvokedBusinessInterface() throws TestFailedException {
    // getInvokedBusinessInterface() returns bean class type for no-interface
    // view
    Class<?>[] expected = { getInvokedBusinessInterfaceForNoInterfaceView(),
        EJBContextIF.class, EJBContext2IF.class };
    for (int i = 0; i < beans.length; i++) {
      assertEquals("", expected[i], beans[i].getInvokedBusinessInterface());
    }
  }

  /*
   * testName: getBusinessObjectIllegalStateException
   * 
   * @test_Strategy: Call EJBContext.getBusinessObject() with invalid interfaces
   * on no-interface, interface1 and interface2, expecting
   * IllegalStateException.
   */
  public void getBusinessObjectIllegalStateException()
      throws TestFailedException {
    for (EJBContextIF b : beans) {
      appendReason(b.getBusinessObjectIllegalStateException());
    }
  }

  /*
   * testName: getBusinessObject
   * 
   * @test_Strategy: Call EJBContext.getBusinessObject() on no-interface,
   * interface1 and interface2, and invoke add() on the obtained business
   * object.
   */
  public void getBusinessObject() {
    EJBContextIF[] beansWithInterfaces = { interface1, interface2 };
    int a = 2, b = 3;
    int expected = a + b;
    for (EJBContextIF bb : beansWithInterfaces) {
      EJBContextIF bob1 = bb.getBusinessObject(EJBContextIF.class);
      EJBContext2IF bob2 = bb.getBusinessObject(EJBContext2IF.class);
      assertEquals("", expected, bob1.add(a, b));
      assertEquals("", expected, bob2.add(a, b));
    }
    // for noInterface view
    EJBContextBeanBase bob3 = noInterface
        .getBusinessObject(getInvokedBusinessInterfaceForNoInterfaceView());
    assertEquals("Check noInterface", expected, bob3.add(a, b));
  }

  /*
   * testName: ejbContextInjections
   * 
   * @test_Strategy: verify setter-, field- and descriptor-injections of
   * EJBContext and SessionContext are performed correctly by the time
   * postConstruct is called. The 2 injections are in both bean base and base
   * classes, and the order of postConstruct calls are also verified.
   */
  public void ejbContextInjections() {
    for (EJBContextIF b : beans) {
      List<String> ir = b.getInjectionRecords();
      assertEquals(ir.toString(), 6, ir.size());
    }
  }

  /*
   * testName: ejbContextInjectionsInInterceptor
   * 
   * @test_Strategy: verify setter-, field- and descriptor-injections of
   * EJBContext and SessionContext are performed correctly by the time
   * postConstruct is called in interceptor1 (the default interceptor).
   */
  public void ejbContextInjectionsInInterceptor() {
    for (EJBContextIF b : beans) {
      List<String> ir = b.getInjectionRecordsInInterceptor();
      assertEquals(ir.toString(), 3, ir.size());
    }
  }

  /*
   * testName: lookupEJBContext
   * 
   * @test_Strategy: look up various injected and declared EJBContextIF in
   * PostConstruct, AroundInvoke and business methods.
   */
  public void lookupEJBContext() {
    for (EJBContextIF b : beans) {
      appendReason(b.lookupEJBContext());
    }
  }

  /*
   * testName: lookupEJBContextInInterceptor
   * 
   * @test_Strategy: look up various injected and declared EJBContextIF in
   * PostConstruct and AroundInvoke of interceptor1
   */
  public void lookupEJBContextInInterceptor() {
    for (EJBContextIF b : beans) {
      appendReason(b.lookupEJBContextInInterceptor());
    }
  }

  /*
   * testName: lookupPortableJNDINames
   * 
   * @test_Strategy: call EJBContext.lookup(string) to look up resources in
   * various portable jndi namespaces.
   */
  public void lookupPortableJNDINames() {
    for (EJBContextIF b : beans) {
      appendReason(b.lookupPortableJNDINames());
    }
  }
}
