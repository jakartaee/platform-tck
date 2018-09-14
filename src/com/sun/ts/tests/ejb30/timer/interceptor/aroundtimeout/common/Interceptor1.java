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
package com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.common;

import java.util.logging.Level;

import javax.ejb.Timer;
import javax.interceptor.AroundTimeout;
import javax.interceptor.InvocationContext;

import com.sun.ts.tests.ejb30.common.helper.Helper;

public class Interceptor1 extends InterceptorBase {
  private static final String simpleName = "Interceptor1";

  @SuppressWarnings("unused")
  @AroundTimeout
  private Object aroundTimeoutInInterceptor1(InvocationContext inv)
      throws Exception {
    Object result = null;
    try {
      result = handleAroundTimeout(inv, simpleName, this,
          "aroundTimeoutInInterceptor1");
    } catch (RuntimeException e) {
      Helper.getLogger().log(Level.INFO, null, e);
      addAroundInvokeRecord((Timer) inv.getTimer(), "RuntimeException", this,
          "aroundTimeoutInInterceptor1");
    }
    return result;
  }

}
