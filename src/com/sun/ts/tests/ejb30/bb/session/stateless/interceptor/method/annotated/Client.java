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

package com.sun.ts.tests.ejb30.bb.session.stateless.interceptor.method.annotated;

import com.sun.javatest.Status;
import com.sun.ts.tests.ejb30.common.interceptor.AroundInvokeIF;

import com.sun.ts.tests.ejb30.common.interceptor.ClientBase;
import javax.ejb.EJB;

public class Client extends ClientBase {
  @EJB(name = "AroundInvokeBean")
  static private AroundInvokeIF bean;

  protected AroundInvokeIF getBean() {
    return bean;
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props:
   */

  /*
   * @testName: getBeanTest
   * 
   * @assertion_ids: EJB:JAVADOC:258
   * 
   * @test_Strategy: o using @AroundInvoke annotation in bean class o test
   * InvocationContext methods o interceptor method can throw exceptions
   */

  /*
   * @testName: getParametersTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: o using @AroundInvoke annotation in bean class o test
   * InvocationContext methods o interceptor method can throw exceptions
   */

  /*
   * @testName: getParametersEmptyTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: o no parameters in business method.
   * InvocationContext.getParameters() should return null or Object[]{}; This is
   * verified in interceptor method.
   */

  /*
   * @testName: txRollbackOnlyTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: o interceptor marks a tx to be rollback only before
   * proceed();
   */

  /*
   * @testName: txRollbackOnlyAfterTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: o interceptor marks a tx to be rollback only before
   * proceed();
   */

  /*
   * @testName: runtimeExceptionTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: o interceptor marks a tx to be rollback only before
   * proceed(), by runtime exception o bean instance is no longer usable after
   * this test
   */

  /*
   * @testName: runtimeExceptionAfterTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: o interceptor marks a tx to be rollback only after
   * proceed(), by runtime exception o bean instance is no longer usable after
   * this test
   */

  /*
   * @testName: setParametersTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: o using @AroundInvoke annotation in bean class o test
   * InvocationContext methods o interceptor method can throw exceptions
   */

  /*
   * @testName: getContextDataTest
   * 
   * @assertion_ids: EJB:JAVADOC:255
   * 
   * @test_Strategy: o using @AroundInvoke annotation in bean class o test
   * InvocationContext methods o interceptor method can throw exceptions
   */

  /*
   * @testName: getMethodTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: o using @AroundInvoke annotation in bean class o test
   * InvocationContext methods o interceptor method can throw exceptions
   */

  /*
   * @testName: exceptionTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: o using @AroundInvoke annotation in bean class o test
   * InvocationContext methods o interceptor method can throw exceptions
   */

  /*
   * @testName: suppressExceptionTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: o the checked exception throwb by the business method can
   * be supressed by the interceptor.
   */

  /*
   * @testName: sameSecContextTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: o interceptor method occurs with the same security context
   * as the business method
   */

}
