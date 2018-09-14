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
package com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle;

import java.util.List;

import javax.ejb.EJB;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;

public class ClientBase extends EJBLiteClientBase {

  @EJB(name = "historySingletonBean", beanName = "HistorySingletonBean")
  protected HistorySingletonBean historySingletonBean;

  // injected in subclass, with @EJB for EJB beans, and @Resource for managed
  // beans
  protected InterceptorIF bean;

  protected InterceptorIF overrideBean;

  protected InterceptorIF override34Bean;

  protected InterceptorIF aroundConstructBean;

  protected InterceptorIF getBean() {
    return bean;
  }

  protected InterceptorIF getOverrideBean() {
    return overrideBean;
  }

  protected InterceptorIF getOverride34Bean() {
    return override34Bean;
  }

  protected InterceptorIF getAroundConstructBean() {
    return aroundConstructBean;
  }

  /*
   * testName: allInterceptors
   * 
   * @test_Strategy: all interceptors at default and class-level should be
   * invoked, as well as PostConstruct methods on bean class. For Interceptor8,
   * the PostConstruct methods from super and super-super classes are overridden
   * and therefore are not invoked.
   */
  public void allInterceptors() {
    String[] expectedPostConstruct = {
        // default interceptors 2, 1, 3
        "InterceptorBaseBase", "InterceptorBase", "Interceptor2",
        "InterceptorBaseBase", "InterceptorBase", "Interceptor1",
        "InterceptorBaseBase", "InterceptorBase", "Interceptor3",
        // class-level interceptors 5, 4
        "InterceptorBaseBase", "InterceptorBase", "InterceptorBaseBase",
        "InterceptorBase", "Interceptor4", "Interceptor8",
        // AroundInvoke methods on bean superclass and bean class
        "InterceptorBeanBase", "InterceptorBean" };
    String[] expectedAroundInvoke = {
        // default interceptors 2, 1, 3
        "InterceptorBaseBase", "InterceptorBase", "InterceptorBaseBase",
        "InterceptorBase", "InterceptorBaseBase", "InterceptorBase",
        // class-level interceptors 5, 4, 8
        "InterceptorBaseBase", "InterceptorBase", "InterceptorBaseBase",
        "InterceptorBase", "InterceptorBaseBase",
        // method-level interceptors 7, 6
        "InterceptorBaseBase", "InterceptorBase", "InterceptorBaseBase",
        "InterceptorBase" };
    interceptorTest(getBean(), expectedPostConstruct, expectedAroundInvoke);
  }

  /*
   * testName: overrideBeanInterceptorMethod
   * 
   * @test_Strategy: If a PostConstruct method is overridden, it is no longer
   * invoked. This test override with a non-PostConstruct method. This test also
   * excludes default and class-level interceptors.
   */
  public void overrideBeanInterceptorMethod() {
    String[] expectedPostConstruct = { "InterceptorOverrideBean" };
    String[] expectedAroundInvoke = {
        // method-level interceptors 7, 6
        "InterceptorBaseBase", "InterceptorBase", "InterceptorBaseBase",
        "InterceptorBase" };
    interceptorTest(getOverrideBean(), expectedPostConstruct,
        expectedAroundInvoke);
  }

  /*
   * testName: overrideBeanInterceptorMethod3
   * 
   * @test_Strategy: If a PostConstruct method is overridden, it is no longer
   * invoked. This test override with a PostConstruct method.
   */
  public void overrideBeanInterceptorMethod3() {
    String[] expectedPostConstruct = { "InterceptorOverride34Bean" };
    String[] expectedAroundInvoke = {
        // method-level interceptors 7, 6
        "InterceptorBaseBase", "InterceptorBase", "InterceptorBaseBase",
        "InterceptorBase" };
    interceptorTest(getOverride34Bean(), expectedPostConstruct,
        expectedAroundInvoke);
  }

  public void aroundConstructInterceptorTest() {
    String[] expectedPostConstruct = {
        // class-level AroundConstruct interceptors 9, A. It's in reverse order
        // because the record has to be added after InvocationContext.proceed().
        // "Interceptor9",
        // "Interceptor9", "InterceptorA",
        "InterceptorA", "Interceptor9", "Interceptor9",
        // class-level PostConstructor interceptors 9, A
        "InterceptorBaseBase", "InterceptorBase", "InterceptorBaseBase",
        "InterceptorBase",
        // PostConstruct methods on bean superclass and bean class
        "InterceptorBeanBase", "AroundConstructInterceptorBean" };

    String[] expectedAroundInvoke = {
        // class-level interceptors 9, A
        "InterceptorBaseBase", "InterceptorBase", "InterceptorBaseBase",
        "InterceptorBase",
        // method-level interceptors 7, 6
        "InterceptorBaseBase", "InterceptorBase", "InterceptorBaseBase",
        "InterceptorBase" };

    interceptorTest(getAroundConstructBean(), expectedPostConstruct,
        expectedAroundInvoke);
  }

  private void interceptorTest(InterceptorIF b, String[] expectedPostConstruct,
      String[] expectedAroundInvoke) {
    try {
      List<String> actualPostConstruct = b.allInterceptors();
      appendReason(
          Helper.compareResultList(expectedPostConstruct, actualPostConstruct));

      List<String> actualAroundInvoke = historySingletonBean
          .getAroundInvokeRecords();
      appendReason(
          Helper.compareResultList(expectedAroundInvoke, actualAroundInvoke));
    } finally {
      historySingletonBean.clearAroundInvokeRecords();
    }
  }
}
