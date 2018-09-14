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
package com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.singleton.dual;

import javax.interceptor.AroundTimeout;
import javax.interceptor.InvocationContext;

public class Interceptor1 {
  static final String simpleName = "Interceptor1";

  static final String asBusiness = "asBusiness";

  static final String asTimeout = "asTimeout";

  @SuppressWarnings("unused")
  @AroundTimeout
  private Object aroundTimeoutInInterceptor1(InvocationContext inv)
      throws Exception {
    Object result = null;
    String methodName = inv.getMethod().getName();
    if (methodName.equals(asBusiness)) {
      // this @Schedule method will not expire until year 9999.
      throw new IllegalStateException(
          methodName + " should not trigger this @AroundTimeout method.");
    }
    result = inv.proceed();
    return result;
  }

  @SuppressWarnings("unused")
  private Object aroundInvoke(InvocationContext inv) throws Exception {
    Object result = null;
    String methodName = inv.getMethod().getName();
    if (methodName.equals(asTimeout)) {
      throw new IllegalStateException(methodName
          + " should not be triggered as a result of timeout callback");
    }
    result = inv.proceed();
    return result;
  }
}
