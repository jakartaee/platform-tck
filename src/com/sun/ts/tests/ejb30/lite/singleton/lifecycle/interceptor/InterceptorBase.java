/*
 * Copyright (c) 2008, 2020 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.lite.singleton.lifecycle.interceptor;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.lite.singleton.common.SingletonInterceptorBase;
import jakarta.interceptor.InvocationContext;

public class InterceptorBase extends SingletonInterceptorBase {
  @Override
  final protected Object intercept0(InvocationContext inv, String methodName,
      String interceptorName, Object[] params) throws Exception {
    if (getSimpleName().equals(interceptorName)) {
      Helper.getLogger()
          .fine("Intercepting it and skipping the rest of interceptor chain. "
              + " methodName:" + methodName + ", interceptorName:"
              + interceptorName);
      if ("identityHashCode".equals(methodName)) {
        return identityHashCode();
      } else if ("error".equals(methodName)) {
        error();
      } else {
        throw new IllegalStateException("Invalid methodName: " + methodName);
      }
    }
    return inv.proceed();
  }

  protected void error() throws RuntimeException {
    throw new RuntimeException(
        "System exception from tests, but the singleton's interceptor should not be stroyed.");
  }

  protected int identityHashCode() {
    return System.identityHashCode(this);
  }
}
