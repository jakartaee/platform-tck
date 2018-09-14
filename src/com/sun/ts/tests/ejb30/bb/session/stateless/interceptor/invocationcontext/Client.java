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
package com.sun.ts.tests.ejb30.bb.session.stateless.interceptor.invocationcontext;

import com.sun.javatest.Status;
import com.sun.ts.tests.ejb30.common.invocationcontext.ClientBase;

public class Client extends ClientBase {
  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }
  /*
   * @testName: setParametersIllegalArgumentException
   * 
   * @test_Strategy:
   */
  /*
   * @testName: getTarget
   * 
   * @test_Strategy:the bean business method getTarget return the identity
   * hashcode for the bean instance, which is compared to the result of
   * InvocationContext.getTarget()'s. They should be the same. This test is
   * executed for AroundInvoke methods in both the bean class and in interceptor
   * class.
   */
  /*
   * @testName: getContextData
   * 
   * @test_Strategy: Put context data in interceptor method and verify it inside
   * finally block after executing business method. This test is executed for
   * AroundInvoke methods in both the bean class and in interceptor class.
   */
  /*
   * @testName: getSetParameters
   * 
   * @test_Strategy: the bean business method getSetParametersEmpty takes no
   * params. InvocationContext.getParameters() should return null or empty
   * params. Setting params to a non-empty array should result in
   * IllegalArgumentException. The bean business method getSetParameters takes 2
   * String params, verified and modified in interceptor. The client should get
   * the result based on the new params.
   */
  /*
   * @testName: proceedAgain
   * 
   * @test_Strategy: call proceed() from interceptor method. The first call
   * results in TestFailedException, and the subsequent proceed() call returns
   * true. Expecting TestFailedException from the first proceed call, and true
   * value from the second proceed call.
   */
}
