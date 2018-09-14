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

package com.sun.ts.tests.ejb30.common.callback;

import com.sun.ts.tests.ejb30.common.helper.TLogger;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.interceptor.InvocationContext;
import org.omg.CORBA.ORB;

abstract public class InterceptorBase extends InterceptorBaseBase {

  @Resource()
  private ORB orb;

  private String getInjectedBaseLocation() {
    String result = (orb == null) ? NOT_INJECTED : "BASE";
    return result;
  }

  protected String getShortName() {
    return "BASE";
  }

  protected void myCreate0(InvocationContext inv, String symbol)
      throws RuntimeException {
    // assertNullGetMethod(inv); // Only for @AroundConstruct Lifecycle callback
    // InvocationContext.getMethod() should return null
    updateContextData(inv);
    SharedCallbackBeanBase bean = (SharedCallbackBeanBase) inv.getTarget();
    bean.addPostConstructCall(symbol);
    bean.setPostConstructCalled(true);
    TLogger.log("PostConstruct method in " + this + ", shortName=" + symbol
        + ", called for bean " + bean);
    if (bean.getEJBContext() != null) {
      bean.setInjectionDone(true);
    }

    bean.addInjectionLocation(getInjectedBaseBaseLocation());
    bean.addInjectionLocation(getInjectedBaseLocation());
    bean.addInjectionLocation(getInjectedLocation());

    try {
      inv.proceed();
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  protected void myRemove(InvocationContext inv) throws RuntimeException {
    assertNullGetMethod(inv);
    SharedCallbackBeanBase bean = (SharedCallbackBeanBase) inv.getTarget();
    bean.setPreDestroyCalled(true);
    TLogger.log("PreDestroy method in " + this + " called for bean " + bean);
    try {
      inv.proceed();
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  /**
   * myRemove() is declared as an @PreDestroy method in some descriptors, and
   * overriding it would remove it from the interceptor call chain. myRemove0 is
   * thus added to do the same thing while not overriding myRemove().
   */
  protected void myRemove0(InvocationContext inv) throws RuntimeException {
    myRemove(inv);
  }

  protected Object intercept(InvocationContext inv) throws Exception {
    return inv.proceed();
  }

  protected void updateContextData(InvocationContext inv) {
    Map<String, Object> map = inv.getContextData();
    // the map should not be null. If no data has been set, it should return
    // empty Map.
    List val = (List) map.get(POSTCONSTRUCT_CALLS_IN_CONTEXTDATA);

    // if this is the first interceptor, the val List is null
    if (val == null) {
      val = new ArrayList();
      map.put(POSTCONSTRUCT_CALLS_IN_CONTEXTDATA, val);
    }
    val.add(getShortName());
    SharedCallbackBeanBase bean = (SharedCallbackBeanBase) inv.getTarget();
    bean.setPostConstructCallsInContextData(val);
  }

  /**
   * Asserts that InvocationContext.getMethod() returns null for lifecycle
   * interceptor methods. This method should only be used for checking lifecycle
   * interceptor methods.
   */
  public static void assertNullGetMethod(InvocationContext inv)
      throws IllegalStateException {
    Method meth = inv.getMethod();
    if (meth != null) {
      throw new IllegalStateException("InvocationContext.getMethod() must"
          + " return null for lifecycle interceptor methods.  But the"
          + " actual returned value is " + meth);
    }
  }
}
