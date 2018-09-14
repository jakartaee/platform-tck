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

import javax.ejb.EJBContext;

public abstract class AroundInvokeBase
    implements AroundInvokeIF, java.io.Serializable {
  protected AroundInvokeBase() {
  }

  abstract protected EJBContext getEJBContext();

  protected void ensureRollbackOnly() throws CalculatorException {
    // the tx should have been marked as rollback only in interceptor.
    boolean txStatus = getEJBContext().getRollbackOnly();
    if (!txStatus) {
      throw new CalculatorException(
          "the tx should have been marked as rollback only in interceptor, but getRollbackOnly() in business method returns false.");
    }
  }

  // ===================== business methods ===========================
  public void remove() {
  }

  public boolean sameSecContextTest() throws CalculatorException {
    return true;
    // the check occurs inside the interceptor prior to invoking this method.
    // exceptions will be thrown if the check failed.
  }

  public void getBeanTest() throws CalculatorException {

  }

  public String getParametersTest(String param) throws CalculatorException {
    return param;
  }

  public void getParametersEmptyTest() throws CalculatorException {

  }

  public String setParametersTest(String param) throws CalculatorException {
    return param;
  }

  public void getEJBContextTest() throws CalculatorException {

  }

  public void getContextDataTest() throws CalculatorException {

  }

  public void getMethodTest() throws CalculatorException {

  }

  public String exceptionTest() throws CalculatorException {
    return "This test failed if you see this.  A CalculatorException should have been thrown from the interceptor.";
  }

  public void suppressExceptionTest() throws CalculatorException {
    throw new CalculatorException(
        "This test failed if you see this. This exception should have been supressed by interceptor method.");
  }

  public void sameInvocationContextTest() throws CalculatorException {
  }

  public void orderTest() throws CalculatorException {
  }

  public void txRollbackOnlyTest() throws CalculatorException {
    ensureRollbackOnly();
  }

  public void txRollbackOnlyAfterTest() throws CalculatorException {
    // tx will be marked for rollback only after proceed(), i.e.,
    // after this method invocation.
  }

  public void runtimeExceptionTest() throws CalculatorException {
    ensureRollbackOnly();
  }

  public void runtimeExceptionAfterTest() throws CalculatorException {
  }

  public void beforeCompletionTest() throws CalculatorException {
  }

  public void afterBeginTest() throws CalculatorException {
  }

  public ArrayList<String> methodLevelInterceptorMixedTest(
      ArrayList<String> alist) throws CalculatorException {
    return addBeanName(alist);
  }

  public ArrayList<String> methodLevelClassLevelInterceptorMixedTest(
      ArrayList<String> alist) throws CalculatorException {
    return addBeanName(alist);
  }

  public ArrayList<String> repeatedInterceptors(ArrayList<String> alist)
      throws CalculatorException {
    return addBeanName(alist);
  }

  public ArrayList<String> interceptorOrderingOverride(ArrayList<String> alist)
      throws CalculatorException {
    return addBeanName(alist);
  }

  protected ArrayList<String> addBeanName(ArrayList<String> alist) {
    alist.add("AroundInvokeBean");
    return alist;
  }

}
