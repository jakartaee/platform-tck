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

package com.sun.ts.tests.ejb30.common.interceptor;

public interface Constants {
  // test methods in client
  public static final String getBeanTest = "getBeanTest";

  public static final String getMethodTest = "getMethodTest";

  public static final String getParametersTest = "getParametersTest";

  public static final String getParametersEmptyTest = "getParametersEmptyTest";

  public static final String setParametersTest = "setParametersTest";

  public static final String getEJBContextTest = "getEJBContextTest";

  public static final String getContextDataTest = "getContextDataTest";

  public static final String exceptionTest = "exceptionTest";

  public static final String txRollbackOnlyTest = "txRollbackOnlyTest";

  public static final String txRollbackOnlyAfterTest = "txRollbackOnlyAfterTest";

  public static final String runtimeExceptionTest = "runtimeExceptionTest";

  public static final String runtimeExceptionAfterTest = "runtimeExceptionAfterTest";

  public static final String suppressExceptionTest = "suppressExceptionTest";

  public static final String methodLevelInterceptorMixedTest = "methodLevelInterceptorMixedTest";

  public static final String repeatedInterceptors = "repeatedInterceptors";

  public static final String methodLevelClassLevelInterceptorMixedTest = "methodLevelClassLevelInterceptorMixedTest";

  public static final String interceptorOrderingOverride = "interceptorOrderingOverride";

  // test method used with interceptor listener class
  public static final String orderTest = "orderTest";

  public static final String sameInvocationContextTest = "sameInvocationContextTest";

  // test method used stateful session beans that implements
  // SessionSynchronization
  public static final String afterBeginTest = "afterBeginTest";

  public static final String beforeCompletionTest = "beforeCompletionTest";

  public static final String sameSecContextTest = "sameSecContextTest";

  // mdb method
  public static final String onMessage = "onMessage";

  public static final String OLD_PARAM_VALUE = "old param value";

  public static final String NEW_PARAM_VALUE = "new param value";

  public static final String INTERCEPTOR_MSG = "from bean class intercept()";

  public static final String INTERCEPTOR_MSG_KEY = "msg.key";

  public static final String INTERCEPTOR_ORDER_KEY = "interceptor.order";

  public static final String SAME_INVOCATIONCONTEXT_KEY = "same.invocationcontext";

  public static final int NUM_OF_INTERCEPTORS = 3;

}
