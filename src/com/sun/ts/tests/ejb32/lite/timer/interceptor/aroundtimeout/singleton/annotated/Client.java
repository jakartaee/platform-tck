/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb32.lite.timer.interceptor.aroundtimeout.singleton.annotated;

import javax.ejb.EJB;
import javax.ejb.EJBException;

import com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.common.AroundTimeoutIF;
import com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.common.ClientBase;

public class Client extends ClientBase {

  @EJB(beanName = "AroundTimeoutExceptionBean")
  protected AroundTimeoutExceptionBean aroundTimeoutExceptionBean;

  @Override
  protected AroundTimeoutIF getAroundTimeoutExceptionBean() {
    return aroundTimeoutExceptionBean;
  }

  /*
   * @testName: aroundTimeoutExceptionAsBusinessMethod
   * 
   * @test_Strategy: invoke the bean class' aroundTimeout method as a business
   * method.
   */
  public void aroundTimeoutExceptionAsBusinessMethod() throws Exception {
    try {
      aroundTimeoutExceptionBean.aroundTimeout(null);
    } catch (EJBException e) {
      RuntimeException cause = (RuntimeException) e.getCause();
      assertEquals(null, "AroundTimeoutExceptionBeanBase", cause.getMessage());
    }
  }

  /*
   * @testName: allInterceptors
   * 
   * @test_Strategy: all interceptors at default and class-level should be
   * invoked. //default interceptors 2, 1 //class-level interceptors 4, 3
   * //method-level interceptors 6, 5
   */

  /*
   * @testName: allInterceptorsOverride
   * 
   * @test_Strategy: For AroundTimeoutOverrideBean, interceptor-order in
   * ejb-jar.xml is used to override the ordering of class-level interceptors.
   * exclude-default-interceptors is also applied for AroundTimeoutOverrideBean.
   */

  /*
   * @testName: allInterceptorsComplement
   * 
   * @test_Strategy: AroundTimeoutComplementBean's interceptors are also
   * declared in ejb-jar.xml to complement the class-level interceptors.
   * AroundTimeoutComplementBean excludes default interceptors.
   */

  /*
   * @testName: aroundTimeoutMethod
   * 
   * @test_Strategy: override @AroundTimeout method with @AroundTimeout method
   * and verify that the superclass' @AroundTimeout is disabled, and subclass'
   * 
   * @AroundTimeout method is invoked
   */

  /*
   * @testName: aroundTimeoutMethod2
   * 
   * @test_Strategy:override @AroundTimeout method with non-AroundTimeout method
   * and verify that the superclass' @AroundTimeout is disabled
   */

  /*
   * @testName: aroundTimeoutException
   * 
   * @test_Strategy: Interceptor1 catches and suppresses the RuntimeException
   * from bean class' AroundTimeout method.
   */

  /*
   * @testName: invocationContextMethods
   * 
   * @test_Strategy: invokes various methods on InvocationContext inside
   * 
   * @AroundTimeout method.
   */
}
