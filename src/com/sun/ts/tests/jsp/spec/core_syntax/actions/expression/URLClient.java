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

/*
 * @(#)URLClient.java	1.1 12/09/02
 */

package com.sun.ts.tests.jsp.spec.core_syntax.actions.expression;

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

    setContextRoot("/jsp_core_act_decl_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: jspExpressionUsageContextTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the container's acceptance of the use of
   * jsp:expression in standard JSP pages, JSP documents and Tag files in both
   * standard and XML syntax.
   */
  public void jspExpressionUsageContextTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_expr_web/JspExpressionUsageContextTest1.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_expr_web/JspExpressionUsageContextTest2.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "<root>Test PASSED</root>");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_expr_web/JspExpressionUsageContextTest3.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_expr_web/JspExpressionUsageContextTest4.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "<root><subroot>Test PASSED</subroot></root>");
    invoke();
  }

}
