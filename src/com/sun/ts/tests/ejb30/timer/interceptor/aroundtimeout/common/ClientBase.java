/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.common;

import java.util.List;

import javax.ejb.EJB;

import com.sun.ts.tests.ejb30.common.helper.Helper;

public class ClientBase extends com.sun.ts.tests.ejb30.timer.common.ClientBase {

  @EJB(beanName = "AroundTimeoutBean")
  private AroundTimeoutIF aroundTimeoutBean;

  @EJB(beanName = "AroundTimeoutOverrideBean")
  private AroundTimeoutIF aroundTimeoutOverrideBean;

  @EJB(beanName = "AroundTimeoutComplementBean")
  private AroundTimeoutIF aroundTimeoutComplementBean;

  @EJB(beanName = "InvocationContextMethodsBean")
  private AroundTimeoutIF invocationContextMethodsBean;

  @EJB(beanName = "MethodOverrideBean")
  private AroundTimeoutIF methodOverrideBean;

  @EJB(beanName = "MethodOverride2Bean")
  private AroundTimeoutIF methodOverride2Bean;

  protected AroundTimeoutIF getAroundTimeoutExceptionBean() {
    return null;
  }

  /*
   * testName: allInterceptors
   * 
   * @test_Strategy: all interceptors at default and class-level should be
   * invoked. //default interceptors 2, 1 //class-level interceptors 4, 3
   * //method-level interceptors 6, 5
   */
  public void allInterceptors() {
    String[] expectedAroundTimeout = { "InterceptorBase", "Interceptor2",
        "InterceptorBase", "Interceptor1",

        "InterceptorBase", "Interceptor4", "InterceptorBase", "Interceptor3",

        "InterceptorBase", "Interceptor6", "InterceptorBase", "Interceptor5",
        "AroundTimeoutBeanBase", "AroundTimeoutBean",
        "AroundTimeoutBeanBase.timeout" };
    aroundTimeoutTest(aroundTimeoutBean, expectedAroundTimeout, true);
  }

  /*
   * testName: allInterceptorsOverride
   * 
   * @test_Strategy: For AroundTimeoutOverrideBean, interceptor-order in
   * ejb-jar.xml is used to override the ordering of class-level interceptors.
   * exclude-default-interceptors is also applied for AroundTimeoutOverrideBean.
   */
  public void allInterceptorsOverride() {
    String[] expectedAroundTimeout = { "InterceptorBase", "Interceptor3",
        "InterceptorBase", "Interceptor4",

        "InterceptorBase", "Interceptor6", "InterceptorBase", "Interceptor5",
        "AroundTimeoutBeanBase", "AroundTimeoutOverrideBean",
        "AroundTimeoutBeanBase.timeout" };
    aroundTimeoutTest(aroundTimeoutOverrideBean, expectedAroundTimeout, true);
  }

  /*
   * testName: allInterceptorsComplement
   * 
   * @test_Strategy: AroundTimeoutComplementBean's interceptors are also
   * declared in ejb-jar.xml to complement the class-level interceptors.
   * AroundTimeoutComplementBean excludes default interceptors.
   */
  public void allInterceptorsComplement() {
    String[] expectedAroundTimeout = { "InterceptorBase", "Interceptor4",
        "InterceptorBase", "Interceptor3", "InterceptorBase", "Interceptor2",
        "InterceptorBase", "Interceptor1",

        "InterceptorBase", "Interceptor6", "InterceptorBase", "Interceptor5",
        "AroundTimeoutBeanBase", "AroundTimeoutComplementBean",
        "AroundTimeoutBeanBase.timeout" };
    aroundTimeoutTest(aroundTimeoutComplementBean, expectedAroundTimeout, true);
  }

  /*
   * testName: aroundTimeoutMethod
   * 
   * @test_Strategy: override @AroundTimeout method with @AroundTimeout method
   * and verify that the superclass' @AroundTimeout is disabled, and
   * subclass' @AroundTimeout method is invoked
   */
  public void aroundTimeoutMethod() {
    String[] expectedAroundTimeout = { "InterceptorBase", "Interceptor2",
        "InterceptorBase", "Interceptor1", "MethodOverrideBean",
        "MethodOverrideBeanBase.timeout" };
    aroundTimeoutTest(methodOverrideBean, expectedAroundTimeout, true);
  }

  /*
   * testName: aroundTimeoutMethod2
   * 
   * @test_Strategy:override @AroundTimeout method with non-AroundTimeout method
   * and verify that the superclass' @AroundTimeout is disabled
   */
  public void aroundTimeoutMethod2() {
    String[] expectedAroundTimeout = { "InterceptorBase", "Interceptor2",
        "InterceptorBase", "Interceptor1", "MethodOverrideBeanBase.timeout" };
    aroundTimeoutTest(methodOverride2Bean, expectedAroundTimeout, true);
  }

  /*
   * testName: aroundTimeoutException
   * 
   * @test_Strategy: Interceptor1 catches and suppresses the RuntimeException
   * from bean class' AroundTimeout method.
   */
  public void aroundTimeoutException() {
    String[] expectedAroundTimeout = { "InterceptorBase", "Interceptor2",
        "InterceptorBase", "Interceptor1", "AroundTimeoutExceptionBeanBase",
        "RuntimeException" };
    aroundTimeoutTest(getAroundTimeoutExceptionBean(), expectedAroundTimeout,
        false);
  }

  /*
   * testName: invocationContextMethods
   * 
   * @test_Strategy: invokes various methods on InvocationContext
   * inside @AroundTimeout method.
   */
  public void invocationContextMethods() {
    String[] expectedAroundTimeout = {
        // method-level interceptors on AroundTimeoutBeanBase.timeout
        "InterceptorBase", "Interceptor6", "InterceptorBase", "Interceptor5",

        "getMethod", "getParameters", "getTarget", "getContextData",
        "setParameters", "AroundTimeoutBeanBase.timeout" };
    aroundTimeoutTest(invocationContextMethodsBean, expectedAroundTimeout,
        true);
  }

  protected String getAroundTimeoutRecordKey() {
    return getTestName() + AroundTimeoutIF.AROUND_TIMEOUT_RECORD_KEY_SUFFIX;
  }

  private void aroundTimeoutTest(AroundTimeoutIF b,
      String[] expectedAroundTimeout, boolean timeoutInvoked) {
    // use getTestName() + ".around-timeout" as the key for
    // around-timeout-related records,
    // separate from timeout records.
    statusSingleton.removeRecords(getAroundTimeoutRecordKey());
    b.createSecondLaterTimer(getTestName());
    checkAndClearAroundTimeoutRecords(expectedAroundTimeout,
        getAroundTimeoutRecordKey(), timeoutInvoked);
  }

  private void checkAndClearAroundTimeoutRecords(String[] expected,
      String aroundTimeoutRecordKey, boolean timeoutInvoked) {
    if (timeoutInvoked) {
      passIfTimeout();
    } else {
      passIfNoTimeout();
    }
    List<String> records = statusSingleton.getRecords(aroundTimeoutRecordKey);
    statusSingleton.removeStatus(getTestName());
    statusSingleton.removeRecords(aroundTimeoutRecordKey);
    appendReason(Helper.compareResultList(expected, records));
  }

}
