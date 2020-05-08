/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * @(#)URLClient.java	
 */

package com.sun.ts.tests.jsp.api.jakarta_el.valexpression;

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

    setContextRoot("/jsp_valexpr_web");
    setTestJsp("ValueExpressionTest");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: valueExpressionEvaluateTest
   * 
   * @assertion_ids: EL:JAVADOC:110; EL:JAVADOC:111; EL:JAVADOC:112;
   * EL:JAVADOC:113; EL:JAVADOC:114; EL:JAVADOC:60
   * 
   * @test_Strategy: Validate the behavior of ValueExpression API and
   * Expression.isLiteralText() ValueExpression.getValue()
   * ValueExpression.setValue() ValueExpression.getType()
   * ValueExpression.getExpectedType() ValueExpression.isReadOnly()
   * Expression.isLiteralText()
   */
  public void valueExpressionEvaluateTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "valueExpressionEvaluateTest");
    invoke();
  }
}
