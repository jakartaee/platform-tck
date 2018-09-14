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
package com.sun.ts.tests.ejb30.lite.interceptor.common.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;

public class ClientBase extends EJBLiteClientBase {
  // injected in subclass
  protected InterceptorIF bean;

  protected InterceptorIF overrideBean;

  protected InterceptorIF override34Bean;

  @EJB(beanName = "TestBean")
  protected TestBean testBean;

  protected InterceptorIF getBean() {
    return bean;
  }

  protected InterceptorIF getOverrideBean() {
    return overrideBean;
  }

  protected InterceptorIF getOverride34Bean() {
    return override34Bean;
  }

  /*
   * testName: allInterceptors
   * 
   * @test_Strategy: all interceptors (Interceptor1-7) at default, class-level,
   * and method-level should be invoked, as well as AroundInvoke methods on bean
   * class.
   */
  public void allInterceptors() {
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
        "InterceptorBaseBase", "InterceptorBase", "Interceptor7",
        "InterceptorBaseBase", "InterceptorBase", "Interceptor6",

        // AroundInvoke methods on bean superclass and bean class
        "InterceptorBeanBase", "InterceptorBean" };
    getBean().allInterceptors(history);
    appendReason(Helper.compareResultList(expected, history));
  }

  /*
   * testName: excludeDefaultInterceptors
   * 
   * @test_Strategy: all interceptors except default interceptors should be
   * invoked in the correct order.
   */
  public void excludeDefaultInterceptors() {
    List<String> history = new ArrayList<String>();
    String[] expected = {
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
   * testName: excludeClassInterceptors
   * 
   * @test_Strategy: all interceptors except class interceptors should be
   * invoked in the correct order.
   */
  public void excludeClassInterceptors() {
    List<String> history = new ArrayList<String>();
    String[] expected = {
        // default interceptors
        "InterceptorBaseBase", "InterceptorBase", "Interceptor2",
        "InterceptorBaseBase", "InterceptorBase", "Interceptor1",
        "InterceptorBaseBase", "InterceptorBase", "Interceptor3",

        // method-level interceptors
        "InterceptorBaseBase", "InterceptorBase", "Interceptor6",
        "InterceptorBaseBase", "InterceptorBase", "Interceptor7",

        // AroundInvoke methods on bean superclass and bean class
        "InterceptorBeanBase", "InterceptorBean" };
    getBean().excludeClassInterceptors(history);
    appendReason(Helper.compareResultList(expected, history));
  }

  /*
   * testName: overrideInterceptorMethod
   * 
   * @test_Strategy: If an interceptor method is overridden, it is no longer
   * invoked, whether the overriding method is an interceptor method or not.
   * This test also excludes default and class-level interceptors.
   */
  public void overrideInterceptorMethod() {
    List<String> history = new ArrayList<String>();
    String[] expected = {
        // method-level interceptors
        "Interceptor8", "InterceptorBaseBase", "InterceptorBase",
        "Interceptor6", "InterceptorBaseBase", "InterceptorBase",
        "Interceptor7",

        // AroundInvoke methods on bean superclass and bean class
        "InterceptorBeanBase", "InterceptorBean" };
    getBean().overrideInterceptorMethod(history);
    appendReason(Helper.compareResultList(expected, history));
  }

  /*
   * testName: overrideBeanInterceptorMethod
   * 
   * @test_Strategy: If an interceptor method is overridden, it is no longer
   * invoked. This test override with a non-interceptor method. This test also
   * excludes default and class-level interceptors.
   */
  public void overrideBeanInterceptorMethod() {
    List<String> history = new ArrayList<String>();
    String[] expected = { "InterceptorOverrideBean" };
    getOverrideBean().overrideBeanInterceptorMethod(history);
    appendReason(Helper.compareResultList(expected, history));
  }

  /*
   * testName: overrideBeanInterceptorMethod2
   * 
   * @test_Strategy: If an interceptor method is overridden, it is no longer
   * invoked. This test override with a non-interceptor method. This test also
   * excludes default and declares no class-level interceptors.
   */
  public void overrideBeanInterceptorMethod2() {
    List<String> history = new ArrayList<String>();
    String[] expected = {
        // method-level interceptors
        "InterceptorBaseBase", "InterceptorBase", "Interceptor1",
        // bean class interceptor method
        "InterceptorOverrideBean" };
    getOverrideBean().overrideBeanInterceptorMethod2(history);
    appendReason(Helper.compareResultList(expected, history));
  }

  /*
   * testName: overrideBeanInterceptorMethod3
   * 
   * @test_Strategy: If an interceptor method is overridden, it is no longer
   * invoked. This test override with an interceptor method.
   */
  public void overrideBeanInterceptorMethod3() {
    List<String> history = new ArrayList<String>();
    String[] expected = {
        // method-level interceptors
        "InterceptorBaseBase", "InterceptorBase", "Interceptor1",
        // bean class interceptor method
        "InterceptorOverride34Bean" };
    getOverride34Bean().overrideBeanInterceptorMethod3(history);
    appendReason(Helper.compareResultList(expected, history));
  }

  /*
   * testName: overrideBeanInterceptorMethod4
   * 
   * @test_Strategy: If an interceptor method is overridden, it is no longer
   * invoked. This test override with an interceptor method.
   */
  public void overrideBeanInterceptorMethod4() {
    List<String> history = new ArrayList<String>();
    String[] expected = {
        // bean class interceptor method
        "InterceptorOverride34Bean" };
    getOverride34Bean().overrideBeanInterceptorMethod4(history);
    appendReason(Helper.compareResultList(expected, history));
  }

  /*
   * testName: skipProceed
   * 
   * @test_Strategy: skipProceed in InterceptorBeanBase is declared to ignore
   * default and class interceptors. This business method also has its own
   * method-level interceptors (1 & 2). In Interceptor1's around-invoke method,
   * InvocationContext.proceed is skipped for business method skipProceed.
   */
  public void skipProceed() {
    List<String> history = new ArrayList<String>();
    getBean().skipProceed(history);
    String[] expected = { "InterceptorBaseBase", "InterceptorBase",
        "Interceptor1" };
    appendReason(Helper.compareResultList(expected, history));
  }

  /*
   * testName: getContextData
   * 
   * @test_Strategy: add data to InvocationContext context data, and retrieve
   * them in EJBContext.getContextData().
   */
  public void getContextData() {
    List<String> history = new ArrayList<String>();
    final String[] expected = { "Interceptor4", "InterceptorBaseBase",
        "Interceptor3", "Interceptor2", "Interceptor1", "InterceptorBean",
        "InterceptorBeanBase", "InterceptorBase", "Interceptor5" };

    Map<String, Object> contextData = getBean().getContextData(history);
    appendReason("EJBContext.getContextData(): ", contextData);
    Set<String> keySet = contextData.keySet();

    for (String s : expected) {
      assertEquals("Contains " + s, true, keySet.contains(s));
    }
    assertEquals("# of elements", expected.length, keySet.size());
  }

  /*
   * testName: applicationExceptionRollback
   * 
   * @test_Strategy: invokes TestBean, a BMT bean, which in turn invokes
   * InterceptorBean. InterceptorBean.applicationExceptionRollback has only a
   * method interceptor (9). Its around-invoke method throws
   * AtCheckedRollbackAppException. Verifies that the client should get
   * AtCheckedRollbackAppException, and this application exception from
   * interceptor's around-invoke method should cause the tx to rollback.
   */
  public void applicationExceptionRollback() {
    appendReason(testBean.applicationExceptionRollback());
  }

}
