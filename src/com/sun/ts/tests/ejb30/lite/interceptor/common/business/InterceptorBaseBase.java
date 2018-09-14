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

import java.util.List;
import java.util.logging.Level;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import com.sun.ts.tests.ejb30.common.helper.Helper;

abstract public class InterceptorBaseBase {
  // we cannot use a getSimpleName() method, since it will always return the
  // simpleName of the subclass. So we have to declare a static simpleName
  // in each layer of the class hierarchy
  private static final String simpleName = "InterceptorBaseBase";

  @AroundInvoke
  protected Object interceptInInterceptorBaseBase(InvocationContext inv)
      throws Exception {
    Helper.getLogger().logp(Level.FINE, simpleName,
        "interceptInInterceptorBaseBase",
        "Adding around-invoke record: " + simpleName + ", this:" + this);
    addToHistory(inv, simpleName);
    return inv.proceed();
  }

  // Only add the simple name of the actual concrete class.
  // No names like InterceptorBaseBase or InterceptorBase will be added.
  // It's possible some interceptors are configured twice (e.g., as both
  // class-level and method-level interceptors. In this case,
  // the second adding will overwrite the first one in the context data.
  private static void addContextData(InvocationContext inv, String name) {
    inv.getContextData().put(name, inv.getTarget());
  }

  // the history, an ordered list of interceptor names, is the param of the
  // business method. Each interceptor adds its simpleName
  // to this list, which is verified by the client after business method
  // invocation.
  // Therefore, this only works for local EJB invocations.
  @SuppressWarnings("unchecked")
  public static void addToHistory(InvocationContext inv, String name) {
    Object[] params = inv.getParameters();
    List<String> history = (List<String>) params[0];
    history.add(name);
    addContextData(inv, name);
  }
}
