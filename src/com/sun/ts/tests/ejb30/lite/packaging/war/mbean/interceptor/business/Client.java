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
package com.sun.ts.tests.ejb30.lite.packaging.war.mbean.interceptor.business;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.lite.interceptor.common.business.InterceptorIF;

public class Client
    extends com.sun.ts.tests.ejb30.lite.interceptor.common.business.ClientBase {

  // @Resource(lookup = "java:module/InterceptorBean")
  @Resource
  private InterceptorBean bean;

  // @Resource(lookup = "java:module/InterceptorOverrideBean")
  @Resource
  private InterceptorOverrideBean overrideBean;

  // @Resource(lookup = "java:module/InterceptorOverride34Bean")
  @Resource
  private InterceptorOverride34Bean override34Bean;

  @Override
  protected InterceptorIF getBean() {
    return bean;
  }

  @Override
  protected InterceptorIF getOverride34Bean() {
    return override34Bean;
  }

  @Override
  protected InterceptorIF getOverrideBean() {
    return overrideBean;
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
  @Override
  public void excludeDefaultInterceptors() {
    List<String> history = new ArrayList<String>();
    String[] expected = {
        // default interceptors
        "InterceptorBaseBase", "InterceptorBase", "Interceptor2",
        "InterceptorBaseBase", "InterceptorBase", "Interceptor1",
        "InterceptorBaseBase", "InterceptorBase", "Interceptor3",

        // class-level interceptors
        "InterceptorBaseBase", "InterceptorBase", "Interceptor5",
        "InterceptorBaseBase", "InterceptorBase", "Interceptor4",

        // method-level interceptors
        "InterceptorBaseBase", "InterceptorBase", "Interceptor6",
        "InterceptorBaseBase", "InterceptorBase", "Interceptor7",

        // AroundInvoke methods on bean superclass and bean class
        "InterceptorBeanBase", "InterceptorBean" };
    getBean().excludeDefaultInterceptors(history);
    appendReason(Helper.compareResultList(expected, history));
  }

  /*
   * @testName: excludeClassInterceptors
   * 
   * @test_Strategy: all interceptors except class interceptors should be
   * invoked in the correct order.
   */
  @Override
  public void excludeClassInterceptors() {
    List<String> history = new ArrayList<String>();
    String[] expected = {
        // no default interceptors, no class interceptors

        // method-level interceptors
        "InterceptorBaseBase", "InterceptorBase", "Interceptor6",
        "InterceptorBaseBase", "InterceptorBase", "Interceptor7",

        // AroundInvoke methods on bean superclass and bean class
        "InterceptorBeanBase", "InterceptorBean" };
    getBean().excludeClassInterceptors(history);
    appendReason(Helper.compareResultList(expected, history));
  }

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
}
