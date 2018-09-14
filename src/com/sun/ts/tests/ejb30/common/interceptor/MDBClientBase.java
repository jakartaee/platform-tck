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

abstract public class MDBClientBase
    extends com.sun.ts.tests.ejb30.common.messaging.ClientBase
    implements com.sun.ts.tests.ejb30.common.messaging.Constants,
    com.sun.ts.tests.ejb30.common.interceptor.Constants {

  /*
   * testName: getBeanTest
   * 
   * @test_Strategy: o using @AroundInvoke annotation in bean class o test
   * InvocationContext methods o interceptor method can throw exceptions
   */
  public void getBeanTest() throws Fault {
    sendReceive(getBeanTest, 0);
  }

  /*
   * testName: getParametersTest
   * 
   * @test_Strategy: o using @AroundInvoke annotation in bean class o test
   * InvocationContext methods o interceptor method can throw exceptions
   */
  public void getParametersTest() throws Fault {
    sendReceive(getParametersTest, 0);
  }

  /*
   * testName: getParametersEmptyTest
   * 
   * @test_Strategy: o no parameters in business method.
   * InvocationContext.getParameters() should return null or Object[]{}; This is
   * verified in interceptor method.
   */
  public void getParametersEmptyTest() throws Fault {
    sendReceive(getParametersEmptyTest, 0);
  }

  /*
   * testName: setParametersTest
   * 
   * @test_Strategy: o using @AroundInvoke annotation in bean class o test
   * InvocationContext methods o interceptor method can throw exceptions
   */
  public void setParametersTest() throws Fault {
    sendReceive(setParametersTest, 0);
  }

  /*
   * testName: getEJBContextTest
   * 
   * @test_Strategy: o using @AroundInvoke annotation in bean class o test
   * InvocationContext methods o interceptor method can throw exceptions
   */
  public void getEJBContextTest() throws Fault {
    sendReceive(getEJBContextTest, 0);
  }

  /*
   * testName: getContextDataTest
   * 
   * @test_Strategy: o using @AroundInvoke annotation in bean class o test
   * InvocationContext methods o interceptor method can throw exceptions
   */
  public void getContextDataTest() throws Fault {
    sendReceive(getContextDataTest, 0);
  }

  /*
   * testName: getMethodTest
   * 
   * @test_Strategy: o using @AroundInvoke annotation in bean class o test
   * InvocationContext methods o interceptor method can throw exceptions
   */
  public void getMethodTest() throws Fault {
    sendReceive(getMethodTest, 0);
  }

  /*
   * testName: exceptionTest
   * 
   * @test_Strategy: o using @AroundInvoke annotation in bean class o test
   * InvocationContext methods o interceptor method can throw exceptions
   */
  public void exceptionTest() throws Fault {
    sendReceive(exceptionTest, 0);
  }

  /*
   * testName: suppressExceptionTest
   * 
   * @test_Strategy: o the checked exception throwb by the business method can
   * be supressed by the interceptor.
   */
  public void suppressExceptionTest() throws Fault {
    sendReceive(suppressExceptionTest, 0);
  }

  /*
   * testName: txRollbackOnlyTest
   * 
   * @test_Strategy: o interceptor marks a tx to be rollback only before
   * proceed();
   */
  public void txRollbackOnlyTest() throws Fault {
    sendReceive(txRollbackOnlyTest, 0);
  }

  /*
   * testName: txRollbackOnlyAfterTest
   * 
   * @test_Strategy: o interceptor marks a tx to be rollback only after
   * proceed();
   */
  public void txRollbackOnlyAfterTest() throws Fault {
    sendReceive(txRollbackOnlyAfterTest, 0);
  }

  /*
   * testName: runtimeExceptionTest
   * 
   * @test_Strategy: o interceptor marks a tx to be rollback only before
   * proceed(), by runtime exception o bean instance is no longer usable after
   * this test
   */
  public void runtimeExceptionTest() throws Fault {
    sendReceive(runtimeExceptionTest, 0);
  }

  /*
   * testName: runtimeExceptionAfterTest
   * 
   * @test_Strategy: o interceptor marks a tx to be rollback only after
   * proceed(), by runtime exception o bean instance is no longer usable after
   * this test
   */
  public void runtimeExceptionAfterTest() throws Fault {
    sendReceive(runtimeExceptionAfterTest, 0);
  }

  ///////////////////////////////////////////////////////////////////////////
  // orderTest and sameInvocationContextTest can only be tested when multiple
  // AroundInvoke methods are used. Since bean class can only have one
  // AroundInvoke method, do not inherite the two tests if you are only using
  // AroundInvoke method in bean class.
  ///////////////////////////////////////////////////////////////////////////
  /*
   * testName: orderTest
   * 
   * @test_Strategy: o the order of interceptors
   */
  public void orderTest() throws Fault {
    sendReceive(orderTest, 0);
  }

  /*
   * testName: sameInvocationContextTest
   * 
   * @test_Strategy: o the same instance of InvocationContext is passed to all
   * interceptors
   */
  public void sameInvocationContextTest() throws Fault {
    sendReceive(sameInvocationContextTest, 0);
  }

  /*
   * testName: sameSecContextTest
   * 
   * @test_Strategy: o interceptor method occurs with the same security context
   * as the business method
   */
  public void sameSecContextTest() throws Fault {
    sendReceive(sameSecContextTest, 0);
  }

}
