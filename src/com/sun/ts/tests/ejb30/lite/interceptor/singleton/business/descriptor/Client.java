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
package com.sun.ts.tests.ejb30.lite.interceptor.singleton.business.descriptor;

import javax.ejb.EJB;

import com.sun.ts.tests.ejb30.lite.interceptor.common.business.InterceptorIF;

public class Client
    extends com.sun.ts.tests.ejb30.lite.interceptor.common.business.ClientBase {

  @EJB(beanName = "InterceptorBean")
  protected void setBean(InterceptorIF b) {
    bean = b;
  }

  @EJB(beanName = "InterceptorOverrideBean")
  protected void setOverrideBean(InterceptorIF b) {
    overrideBean = b;
  }

  @EJB(beanName = "InterceptorOverride34Bean")
  protected void setOverride34Bean(InterceptorIF b) {
    override34Bean = b;
  }

  /*
   * @testName: allInterceptors
   * 
   * @test_Strategy: all interceptors (Interceptor1-7) at default, class-level,
   * and method-level should be invoked, as well as AroundInvoke methods on bean
   * class.
   */
  /*
   * @testName: excludeDefaultInterceptors
   * 
   * @test_Strategy: all interceptors except default interceptors should be
   * invoked in the correct order.
   */
  /*
   * @testName: excludeClassInterceptors
   * 
   * @test_Strategy: all interceptors except class interceptors should be
   * invoked in the correct order.
   */
  /*
   * @testName: overrideInterceptorMethod
   * 
   * @test_Strategy: If an interceptor method is overridden, it is no longer
   * invoked, whether the overriding method is an interceptor method or not.
   * This test also excludes default and class-level interceptors.
   */
  /*
   * @testName: overrideBeanInterceptorMethod
   * 
   * @test_Strategy: If an interceptor method is overridden, it is no longer
   * invoked. This test override with a non-interceptor method. This test also
   * excludes default and class-level interceptors.
   */
  /*
   * @testName: overrideBeanInterceptorMethod2
   * 
   * @test_Strategy: If an interceptor method is overridden, it is no longer
   * invoked. This test override with a non-interceptor method. This test also
   * excludes default and declares no class-level interceptors.
   */
  /*
   * @testName: overrideBeanInterceptorMethod3
   * 
   * @test_Strategy: If an interceptor method is overridden, it is no longer
   * invoked. This test override with an interceptor method.
   */
  /*
   * @testName: overrideBeanInterceptorMethod4
   * 
   * @test_Strategy: If an interceptor method is overridden, it is no longer
   * invoked. This test override with an interceptor method.
   */
  /*
   * @testName: skipProceed
   * 
   * @test_Strategy:
   */
  /*
   * @testName: getContextData
   * 
   * @test_Strategy: add data to InvocationContext context data, and retrieve
   * them in EJBContext.getContextData().
   */
  /*
   * @testName: applicationExceptionRollback
   * 
   * @test_Strategy: invokes TestBean, a BMT bean, which in turn invokes
   * InterceptorBean. InterceptorBean.applicationExceptionRollback has only a
   * method interceptor (9). Its around-invoke method throws
   * AtCheckedRollbackAppException. Verifies that the client should get
   * AtCheckedRollbackAppException, and this application exception from
   * interceptor's around-invoke method should cause the tx to rollback.
   */

}
