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
package com.sun.ts.tests.ejb30.lite.ejbcontext.stateless;

import com.sun.ts.tests.ejb30.lite.ejbcontext.common.ClientBase;
import com.sun.ts.tests.ejb30.lite.ejbcontext.common.EJBContextBeanBase;
import javax.ejb.EJB;

public class Client extends ClientBase {
  @Override
  @EJB(beanInterface = EJBContextBean.class, beanName = "EJBContextBean")
  protected void setNoInterface(EJBContextBeanBase b) {
    noInterface = b;
  }

  @Override
  protected Class<? extends EJBContextBeanBase> getInvokedBusinessInterfaceForNoInterfaceView() {
    return EJBContextBean.class;
  }

  /*
   * @testName: lookupIllegalArgumentException
   * 
   * @test_Strategy: Call EJBContext.lookup with null and another non-existent
   * name, expecting IllegalArgumentException. Valid use of EJBContext.lookup
   * method are tested in various other directories and are not present here.
   */
  /*
   * @testName: getMessageContextIllegalStateException
   * 
   * @test_Strategy: Call EJBContext.getMessageContext() where it's not
   * supported, expecting IllegalStateException.
   */
  /*
   * @testName: getInvokedBusinessInterface
   * 
   * @test_Strategy: Call EJBContext.getInvokedBusinessInterface() on
   * no-interface, interface1 and interface2, and verify the returned interface.
   */
  /*
   * @testName: getBusinessObjectIllegalStateException
   * 
   * @test_Strategy: Call EJBContext.getBusinessObject() with invalid interfaces
   * on no-interface, interface1 and interface2, expecting
   * IllegalStateException.
   */
  /*
   * @testName: getBusinessObject
   * 
   * @test_Strategy: Call EJBContext.getBusinessObject() on no-interface,
   * interface1 and interface2, and invoke add() on the obtained business
   * object.
   */
  /*
   * @testName: ejbContextInjections
   * 
   * @test_Strategy: verify setter-, field- and descriptor-injections of
   * EJBContext and SessionContext are performed correctly by the time
   * postConstruct is called. The 2 injections are in both bean base and base
   * classes, and the order of postConstruct calls are also verified.
   */
  /*
   * @testName: ejbContextInjectionsInInterceptor
   * 
   * @test_Strategy: verify setter-, field- and descriptor-injections of
   * EJBContext and SessionContext are performed correctly by the time
   * postConstruct is called in interceptor1 (the default interceptor).
   */
  /*
   * @testName: lookupEJBContext
   * 
   * @test_Strategy: look up various injected and declared EJBContextIF in
   * PostConstruct, AroundInvoke and business methods.
   */
  /*
   * @testName: lookupEJBContextInInterceptor
   * 
   * @test_Strategy: look up various injected and declared EJBContextIF in
   * PostConstruct and AroundInvoke of interceptor1
   */
  /*
   * @testName: lookupPortableJNDINames
   * 
   * @test_Strategy: call EJBContext.lookup(string) to look up resources in
   * various portable jndi namespaces.
   */
}
