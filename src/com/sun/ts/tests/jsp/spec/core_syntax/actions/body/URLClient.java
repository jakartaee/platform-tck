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

package com.sun.ts.tests.jsp.spec.core_syntax.actions.body;

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

    setContextRoot("/jsp_core_act_body_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: jspBodyTest
   * 
   * @assertion_ids: JSP:SPEC:248
   * 
   * @test_Strategy: Validate that the body of an action is properly delivered
   * to a custom action when provided through jsp:body.
   */
  public void jspBodyTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_body_web/JspBodyTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "Test PASSED|Test PASSED|Test PASSED|Test PASSED");
    invoke();
  }

  /*
   * @testName: jspBodyUsageContextTest
   * 
   * @assertion_ids: JSP:SPEC:248.2;JSP:SPEC:248.3
   * 
   * @test_Strategy: Validate that translation-time errors will occur when
   * jsp:body is used in an incorrected context. - Not nested within a standard
   * or custom action - attempting to provide a body to an action not accepting
   * a body - jsp:body nested within a jsp:body - jsp:body nested within
   * jsp:attribute
   */
  public void jspBodyUsageContextTest() throws Fault {
    for (int i = 0; i < 5; i++) {
      TEST_PROPS.setProperty(REQUEST,
          "GET /jsp_core_act_body_web/JspBodyUsageContextTest" + (i + 1)
              + ".jsp HTTP/1.1");
      TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
      invoke();
    }
  }

  /*
   * @testName: jspBodyEmptyBodyTest
   * 
   * @assertion_ids: JSP:SPEC:248.1
   * 
   * @test_Strategy: Verify that following empty body semantics involving
   * jsp:body: - If an action has one or more jsp:attribute elements and no
   * jsp:body element, then the action is considered empty. - Empty bodies can
   * be provided via jsp:body in the forms of &lt;jsp:body/&gt; and
   * &lt;jsp:body&gt;&lt;/jsp:body&gt;
   */
  public void jspBodyEmptyBodyTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_body_web/JspBodyEmptyBodyTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "Test PASSED|Test PASSED|Test PASSED|Test PASSED|Test PASSED|Test PASSED");
    invoke();
  }
}
