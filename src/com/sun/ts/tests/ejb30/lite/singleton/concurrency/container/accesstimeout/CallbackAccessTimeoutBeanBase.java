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
package com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.accesstimeout;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import javax.interceptor.InvocationContext;

abstract public class CallbackAccessTimeoutBeanBase {
  protected static final long ACCESS_TIMEOUT_MILLIS = 1000;

  protected static final long AROUND_INVOKE_WAIT_MILLIS = 600;

  // This method returns a resultVal immediately. In the test this method
  // should be the first method invoked on this bean in order to verify
  // wait time in @PostConstruct has no effect has AccessTimeout of this method.

  public int postConstructWait(int resultVal) {
    return resultVal;
  }

  public void postConstruct() {
    busyWait(ACCESS_TIMEOUT_MILLIS * 2, "PostConstruct");
  }

  public void preDestroy() {
    busyWait(ACCESS_TIMEOUT_MILLIS * 2, "PreDestroy");
  }

  // When invoked as a business method, it will time out
  // (AROUND_INVOKE_WAIT_MILLIS * 2 ms)
  public Object intercept(InvocationContext inv) throws Exception {
    if (inv != null) {
      if (inv.getMethod().getName().equals("postConstructWait")
          || inv.getMethod().getName().equals("postConstruct")) {
        return inv.proceed();
      }
    }
    busyWait(AROUND_INVOKE_WAIT_MILLIS, "AroundInvoke");
    return (inv == null ? null : inv.proceed());
  }

  protected void busyWait(long waitMillis, String methodName) {
    Helper.getLogger().fine("Waiting in " + methodName
        + ", but it should not affect AccessTimeout:" + waitMillis);
    Helper.busyWait(waitMillis);
  }
}
