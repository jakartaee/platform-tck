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

package com.sun.ts.tests.jsp.spec.core_syntax.scripting.expressions;

import java.io.PrintWriter;
import com.sun.ts.lib.harness.*;
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

    setGeneralURI("/jsp/spec/core_syntax/scripting/expressions");
    setContextRoot("/jsp_coresyntx_script_expressions_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: positiveExprTest
   * 
   * @assertion_ids: JSP:SPEC:77
   * 
   * @test_Strategy: Validate that the container can correctly support a basic
   * expression by validating the output returned.
   */

  public void positiveExprTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveExpr");
    invoke();
  }

  /*
   * @testName: positiveExprCommentTest
   * 
   * @assertion_ids: JSP:SPEC:5
   * 
   * @test_Strategy: Validate that an HTML stye comment with an embedded
   * expression returns the value of the expression within the comment and that
   * the HTML comment is treated as template text.
   */

  public void positiveExprCommentTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveExprComment");
    invoke();
  }

  /*
   * @testName: positiveExprWhiteSpaceTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that the container correctly handles different
   * whitespace values with an expression element.
   */

  public void positiveExprWhiteSpaceTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveExprWhiteSpace");
    invoke();
  }

}
