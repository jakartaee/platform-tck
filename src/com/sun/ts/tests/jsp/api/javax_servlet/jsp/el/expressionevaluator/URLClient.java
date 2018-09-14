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

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.el.expressionevaluator;

import java.io.PrintWriter;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

public class URLClient extends AbstractUrlClient {

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /**
   * Entry point for same-VM execution. In different-VM execution, the main
   * method delegates to this method.
   */
  public Status run(String args[], PrintWriter out, PrintWriter err) {

    setContextRoot("/jsp_expreval_web");
    setTestJsp("ExpressionEvaluatorTest");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: expressionEvaluatorParseExpressionTest
   * 
   * @assertion_ids: JSP:JAVADOC:168
   * 
   * @test_Strategy: Validate the following: - An expression can be prepared
   * using a FunctionMapper. - An expression can be prepared passing a null
   * reference for the FunctionMapper - If the expression uses a function an no
   * prefix is provided, the default prefix will be used.
   */
  public void expressionEvaluatorParseExpressionTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "expressionEvaluatorParseExpressionTest");
    invoke();
  }

  /*
   * @testName: expressionEvaluatorEvaluateTest
   * 
   * @assertion_ids: JSP:JAVADOC:171;JSP:JAVADOC:165
   * 
   * @test_Strategy: Validate the following: - Evaluation can occur using a
   * FunctionMapper. - Evaluation can occur when a null reference passed as the
   * FunctionMapper - If the expression uses a function an no prefix is
   * provided, the default prefix will be used. - When the FunctionMapper is
   * used, the resolveFunction method must be called. - Validate the the
   * provided VariableResolver is used. - Validate the result of the
   * expressions.
   */
  public void expressionEvaluatorEvaluateTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "expressionEvaluatorEvaluateTest");
    invoke();
  }
}
