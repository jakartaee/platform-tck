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
package com.sun.ts.tests.ejb30.lite.packaging.war.enventry;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import static com.sun.ts.tests.ejb30.common.helper.Helper.*;

abstract public class InterceptorBase extends ComponentBase {

  @Resource // should be able to inject EJBContext
  private EJBContext ejbContext;

  // inject 3 float fields that are declared in ejb-jar.xml#OneBean, TwoBean,
  // and ThreeBean

  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.OneBean/myFloat", description = "declared in ejb-jar.xml#OneBean")
  private float myFloatFromOne;

  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.TwoBean/myFloat", description = "declared in ejb-jar.xml#TwoBean")
  private float myFloatFromTwo;

  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.ThreeBean/myFloat", description = "declared in ejb-jar.xml#ThreeBean")
  private float myFloatFromThree;

  /**
   * Checks injections into InterceptorBase class only. It cannot call the
   * template method checkInjections, since the verify method in subclass will
   * be called, which verifies injections in subclass. This PostConstruct method
   * should be called before any PostConstruct in subclasses. When the subclass
   * is a method-level interceptor, any PostConstruct methods on the superclass
   * or subclass are ignored.
   */
  @SuppressWarnings("unused")
  @PostConstruct
  private void postConstruct(InvocationContext inv) {
    verifyInterceptorBase();
    try {
      inv.proceed();
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  protected void verifyInterceptorBase() throws RuntimeException {
    StringBuilder sb = new StringBuilder();
    try {
      assertNotEquals("Check injected EJBContext ", ejbContext, null);
      assertEquals("Check myFloatFromOne ", (float) 1, myFloatFromOne, sb);
      assertEquals("Check myFloatFromTwo ", (float) 2, myFloatFromTwo, sb);
      assertEquals("Check myFloatFromThree ", (float) 3, myFloatFromThree, sb);
      setInjectionStatusAndRecord(true, sb.toString());
    } catch (Throwable e) {
      setInjectionStatusAndRecord(false, sb.toString(), e);
    }
  }

  /**
   * Collects the result of injections. For business method
   * getInjectionStatusForInterceptors, a List<Boolean> param is passed in to
   * hold injection status from Interceptor0, 1, and 2 in that order.
   * 
   * For business method getInjectionRecordsForInterceptors, a
   * List<List<String>> is passed in to hold injection records from
   * Interceptor0, 1, and 2 in that order. Each inner list holds the injection
   * record for the superclass and the subclass of an interceptor instance, in
   * that order.
   */
  @SuppressWarnings("unused")
  @AroundInvoke
  private Object intercept(InvocationContext inv) throws Exception {
    String methodName = inv.getMethod().getName();
    Object[] params = inv.getParameters();
    if (methodName.equals("getInjectionStatusForInterceptors")) {
      Object param = params[0];
      List<Boolean> sta = (List<Boolean>) param;
      sta.add(getInjectionStatus());
    } else if (methodName.equals("getInjectionRecordsForInterceptors")) {
      Object param = params[0];
      List<List<String>> rec = (List<List<String>>) param;
      List<String> injectionRecords = getInjectionRecords();
      if (injectionRecords != null) {
        rec.add(injectionRecords);
      }
    }
    return inv.proceed();
  }
}
