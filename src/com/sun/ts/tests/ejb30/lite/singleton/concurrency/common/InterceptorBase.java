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
package com.sun.ts.tests.ejb30.lite.singleton.concurrency.common;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.lite.singleton.common.SingletonInterceptorBase;
import java.util.logging.Level;
import javax.interceptor.InvocationContext;

abstract public class InterceptorBase extends SingletonInterceptorBase {
  abstract protected long getAndResetLockedSum();

  abstract protected void addLocked(int num);

  abstract protected long getAndResetUnlockedSum();

  abstract protected void addUnlocked(int num);

  @Override
  final protected Object intercept0(InvocationContext inv, String methodName,
      String interceptorName, Object[] params) throws Exception {
    if (getSimpleName().equals(interceptorName)) {
      if (Helper.getLogger().isLoggable(Level.FINE)) {
        Helper.getLogger().finest(
            "Intercepting it and skipping the rest of interceptor chain. "
                + " methodName:" + methodName + ", interceptorName:"
                + interceptorName);
      }
      if ("addLockedFromInterceptor".equals(methodName)) {
        addLocked((Integer) params[1]);
        return null;
      } else if ("getAndResetLockedSumFromInterceptor".equals(methodName)) {
        return getAndResetLockedSum();
      } else if ("addUnlockedFromInterceptor".equals(methodName)) {
        addUnlocked((Integer) params[1]);
        return null;
      } else if ("getAndResetUnlockedSumFromInterceptor".equals(methodName)) {
        return getAndResetUnlockedSum();
      } else {
        throw new IllegalStateException("Invalid methodName: " + methodName);
      }
    } else {
      Helper.getLogger().finest("interceptorName does not match, so proceed:"
          + interceptorName + " vs " + getSimpleName());
    }
    return inv.proceed();
  }
}
