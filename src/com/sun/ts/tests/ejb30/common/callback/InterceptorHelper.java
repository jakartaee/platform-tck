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
import javax.interceptor.InvocationContext;

public class InterceptorHelper {
  private static final Object[] newParams = new Object[] {};

  private InterceptorHelper() {
  }

  /**
   * When this method is called from an interceptor's lifecycle callback method,
   * InvocationContext should throw IllegalStateException. When called from
   * around-invoke methods, it works as usual and the return value is ignored.
   */
  public static void invokeGetParameters(InvocationContext inv)
      throws IllegalStateException {
    inv.getParameters();
  }

  public static void recordExceptionFromGetParameters(InvocationContext inv,
      SharedCallbackBeanBase bean, boolean throwException) {
    String reason = "WARNING: Expecting java.lang.IllegalStateException, but got none.";
    try {
      invokeGetParameters(inv);
      // failed to get the expected exception
      if (throwException) {
        throw new IllegalStateException(reason);
      } else {
        TLogger.log(reason);
      }
    } catch (IllegalStateException e) {
      // expected exception
      bean.setGetParametersIllegalStateExceptionThrown(true);
    }
  }

  /**
   * When this method is called from an interceptor's lifecycle callback method,
   * InvocationContext should throw IllegalStateException. When called from
   * around-invoke methods, it works as usual and the return value is ignored.
   */
  public static void invokeSetParameters(InvocationContext inv)
      throws IllegalStateException {
    inv.setParameters(newParams);
  }

  public static void recordExceptionFromSetParameters(InvocationContext inv,
      SharedCallbackBeanBase bean, boolean throwException) {
    String reason = "WARNING: Expecting java.lang.IllegalStateException, but got none.";
    try {
      invokeSetParameters(inv);
      // failed to get the expected exception
      if (throwException) {
        throw new IllegalStateException(reason);
      } else {
        TLogger.log(reason);
      }
    } catch (IllegalStateException e) {
      // expected exception
      bean.setSetParametersIllegalStateExceptionThrown(true);
    }
  }

}
