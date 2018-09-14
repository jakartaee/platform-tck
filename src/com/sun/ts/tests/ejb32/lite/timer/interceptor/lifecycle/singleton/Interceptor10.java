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

package com.sun.ts.tests.ejb32.lite.timer.interceptor.lifecycle.singleton;

import java.util.logging.Level;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.interceptor.AroundConstruct;
import java.lang.reflect.Method;

import com.sun.ts.tests.ejb30.common.appexception.AtCheckedRollbackAppException;
import com.sun.ts.tests.ejb30.common.helper.Helper;

public class Interceptor10 extends InterceptorBase {

  private static final String simpleName = "Interceptor10";

  @AroundConstruct
  @SuppressWarnings("unused")
  private void aroundConstruct(InvocationContext ic) {

    Helper.getLogger().logp(Level.INFO, simpleName, "aroundConstruct",
        "Adding aroundConstruct record: " + simpleName);
    // Target should be null before proceed()
    Object savedTarget = ic.getTarget();

    try {
      // Only for @AroundConstruct Lifecycle callback
      // InvocationContext.getMethod() should return null
      assertNullGetMethod(ic);
      ic.proceed();
    } catch (Exception ex) {
      ex.printStackTrace();
      return;
    }

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
    } else {
      Helper.getLogger().logp(Level.INFO, simpleName, "assertNullGetMethod",
          "InvocationContext.getMethod() returned null as expected: "
              + simpleName);
    }
  }

}
