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
package com.sun.ts.tests.ejb30.lite.singleton.common;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

abstract public class SingletonInterceptorBase {
  @Resource
  protected EJBContext ejbContext;

  abstract protected Object intercept0(InvocationContext inv, String methodName,
      String interceptorName, Object[] params) throws Exception;

  @AroundInvoke
  private Object intercept(InvocationContext inv) throws Exception {
    String methodName = inv.getMethod().getName();
    Object[] params = inv.getParameters();
    String interceptorName = null;
    if (params != null && params.length > 0) {
      if (params[0] instanceof String) {
        interceptorName = (String) params[0]; // simple class name
      }
    }
    return intercept0(inv, methodName, interceptorName, params);
  }

  /**
   * If the interceptor class (e.g., Interceptor1) is subclassed by the
   * container, getClass().getSimpleName() returns the subclass' name, which is
   * not what we are expecting. The safest way is to always override it.
   */
  protected String getSimpleName() {
    return getClass().getSimpleName();
  }

  @PreDestroy
  private void preDestroy(InvocationContext inv) {
    Helper.getLogger().info("In SingletonInterceptorBase.preDestroy()");
  }
}
