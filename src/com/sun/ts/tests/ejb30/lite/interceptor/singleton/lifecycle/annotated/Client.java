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
package com.sun.ts.tests.ejb30.lite.interceptor.singleton.lifecycle.annotated;

import javax.ejb.EJB;

import com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.InterceptorIF;

public class Client extends
    com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.ClientBase {

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

  @EJB(beanName = "AroundConstructInterceptorBean")
  protected void setAroundConstructInterceptorBean(InterceptorIF b) {
    aroundConstructBean = b;
  }

  /*
   * @testName: allInterceptors
   * 
   * @test_Strategy: all interceptors at default and class-level should be
   * invoked, as well as PostConstruct methods on bean class. For Interceptor8,
   * the PostConstruct methods from super and super-super classes are overridden
   * and therefore are not invoked.
   */
  /*
   * @testName: overrideBeanInterceptorMethod
   * 
   * @test_Strategy: If a PostConstruct method is overridden, it is no longer
   * invoked. This test override with a non-PostConstruct method. This test also
   * excludes default and class-level interceptors.
   */
  /*
   * @testName: overrideBeanInterceptorMethod3
   * 
   * @test_Strategy: If a PostConstruct method is overridden, it is no longer
   * invoked. This test override with a PostConstruct method.
   */
  /*
   * @testName: aroundConstructInterceptorTest
   *
   * @test_Strategy: Add 2 class-level AroundConstruct interceptors, and one is
   * another's superclass, then verify the invocation order of all the
   * AroundConstruct and PostConstruct lifecycle callbacks.
   */
}
