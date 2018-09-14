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
package com.sun.ts.tests.ejb30.common.invocationcontext;

import java.util.Properties;

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import javax.ejb.EJB;

abstract public class ClientBase extends EETest {
  protected Properties props;

  @EJB(beanName = "InvocationContextBean")
  static private InvocationContextIF bean;

  @EJB(beanName = "InvocationContextInterceptorBean")
  static private InvocationContextIF bean2;

  private InvocationContextIF[] beans = new InvocationContextIF[] { bean,
      bean2 };

  public void setup(String[] args, Properties p) {
    props = p;
  }

  public void cleanup() {
  }

  /*
   * testName: setParametersIllegalArgumentException
   * 
   * @test_Strategy:
   */
  public void setParametersIllegalArgumentException()
      throws TestFailedException {
    InvocationContextTestImpl.setParametersIllegalArgumentException(beans);
  }

  /*
   * testName: getTarget
   * 
   * @test_Strategy: the bean business method getTarget return the identity
   * hashcode for the bean instance, which is compared to the result of
   * InvocationContext.getTarget()'s. They should be the same. This test is
   * executed for AroundInvoke methods in both the bean class and in interceptor
   * class.
   */
  public void getTarget() throws TestFailedException {
    InvocationContextTestImpl.getTarget(beans);
  }

  /*
   * testName: getContextData
   * 
   * @test_Strategy: Put context data in interceptor method and verify it inside
   * finally block after executing business method. This test is executed for
   * AroundInvoke methods in both the bean class and in interceptor class.
   */
  public void getContextData() throws TestFailedException {
    InvocationContextTestImpl.getContextData(beans);
  }

  /*
   * testName: getSetParameters
   * 
   * @test_Strategy: the bean business method getSetParametersEmpty takes no
   * params. InvocationContext.getParameters() should return null or empty
   * params. Setting params to a non-empty array should result in
   * IllegalArgumentException. The bean business method getSetParameters takes 2
   * String params, verified and modified in interceptor. The client should get
   * the result based on the new params.
   */
  public void getSetParameters() throws TestFailedException {
    InvocationContextTestImpl.getSetParametersEmpty(beans);
    InvocationContextTestImpl.getSetParameters(beans);
  }

  /*
   * testName: proceedAgain
   * 
   * @test_Strategy: call proceed() from interceptor method. The first call
   * results in TestFailedException, and the subsequent proceed() call returns
   * true. Expecting TestFailedException from the first proceed call, and true
   * value from the second proceed call.
   */
  public void proceedAgain() throws TestFailedException {
    InvocationContextTestImpl.proceedAgain(beans);
  }
}
