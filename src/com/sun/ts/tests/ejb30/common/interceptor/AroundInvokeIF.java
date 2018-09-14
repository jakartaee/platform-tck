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

import com.sun.ts.tests.ejb30.common.calc.CalculatorException;
import java.util.ArrayList;

public interface AroundInvokeIF {
  public void remove(); // for sfsb only

  public void getBeanTest() throws CalculatorException;

  public String getParametersTest(String param) throws CalculatorException;

  public void getParametersEmptyTest() throws CalculatorException;

  public String setParametersTest(String param) throws CalculatorException;

  public void getEJBContextTest() throws CalculatorException;

  public void getContextDataTest() throws CalculatorException;

  public void getMethodTest() throws CalculatorException;

  public String exceptionTest() throws CalculatorException;

  public void suppressExceptionTest() throws CalculatorException;

  public void txRollbackOnlyTest() throws CalculatorException;

  public void txRollbackOnlyAfterTest() throws CalculatorException;

  public boolean sameSecContextTest() throws CalculatorException;

  public void runtimeExceptionTest() throws CalculatorException;

  public void runtimeExceptionAfterTest() throws CalculatorException;

  // orderTest and sameInvocationContextTest are only used when interceptor
  // classes
  // are used, since they need multiple interceptors. They should not be tested
  // in
  // case of interceptor method. Kept here for simplicity.

  public void orderTest() throws CalculatorException;

  public void sameInvocationContextTest() throws CalculatorException;

  // afterBeginTest and beforeCompletionTest can only be used with sfsb with
  // SessionSynchronization
  public void afterBeginTest() throws CalculatorException;

  public void beforeCompletionTest() throws CalculatorException;

  public ArrayList<String> methodLevelInterceptorMixedTest(
      ArrayList<String> alist) throws CalculatorException;

  public ArrayList<String> methodLevelClassLevelInterceptorMixedTest(
      ArrayList<String> alist) throws CalculatorException;

  public ArrayList<String> repeatedInterceptors(ArrayList<String> alist)
      throws CalculatorException;

  public ArrayList<String> interceptorOrderingOverride(ArrayList<String> alist)
      throws CalculatorException;

}
