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
 * $Id$
 */

package com.sun.ts.tests.jsp.spec.core_syntax.actions.dobody;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

public class URLClient extends AbstractUrlClient {
  private static final String CONTEXT_ROOT = "/jsp_core_act_dobody_web";

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

    setGeneralURI("/jsp/spec/core_syntax/action/dobody");
    setContextRoot(CONTEXT_ROOT);

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /*
   * @testName: jspDoBodyNonEmptyBodyTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy:
   */

  public void jspDoBodyNonEmptyBodyTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST, "GET " + CONTEXT_ROOT + "/"
        + "JspDoBodyNonEmptyBodyTest1.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST, "GET " + CONTEXT_ROOT + "/"
        + "JspDoBodyNonEmptyBodyTest2.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: jspDoBodyInvalidPageScopeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy:
   */

  public void jspDoBodyInvalidPageScopeTest() throws Exception {
    String testName = "JspDoBodyInvalidPageScopeTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: jspDoBodyInvalidRequestScopeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy:
   */

  public void jspDoBodyInvalidRequestScopeTest() throws Exception {
    String testName = "JspDoBodyInvalidRequestScopeTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: jspDoBodyInvalidApplicationScopeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy:
   */

  public void jspDoBodyInvalidApplicationScopeTest() throws Exception {
    String testName = "JspDoBodyInvalidApplicationScopeTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }
  /*
   * @testName: jspDoBodyInvalidSessionScopeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy:
   */

  public void jspDoBodyInvalidSessionScopeTest() throws Exception {
    String testName = "JspDoBodyInvalidSessionScopeTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }
  /*
   * @testName: jspDoBodyInvalidScopeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy:
   */

  public void jspDoBodyInvalidScopeTest() throws Exception {
    String testName = "JspDoBodyInvalidScopeTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: jspDoBodyJspAttributeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy:
   */

  public void jspDoBodyJspAttributeTest() throws Exception {
    String testName = "JspDoBodyJspAttributeTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: jspDoBodyVarTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy:
   */

  public void jspDoBodyVarTest() throws Exception {
    String testName = "JspDoBodyVarTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: jspDoBodyVarReaderTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy:
   */

  public void jspDoBodyVarReaderTest() throws Exception {
    String testName = "JspDoBodyVarReaderTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: jspDoBodyScopeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy:
   */

  public void jspDoBodyScopeTest() throws Exception {
    String testName = "JspDoBodyScopeTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: jspDoBodyNoVarVarReaderScopeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy:
   */

  public void jspDoBodyNoVarVarReaderScopeTest() throws Exception {
    String testName = "JspDoBodyNoVarVarReaderScopeTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: jspDoBodyVarVarReaderTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy:
   */

  public void jspDoBodyVarVarReaderTest() throws Exception {
    String testName = "JspDoBodyVarVarReaderTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeJspDoBodyUsageContextTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy:
   */

  public void negativeJspDoBodyUsageContextTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST, "GET " + CONTEXT_ROOT + "/"
        + "JspDoBodyUsageContextTest1.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST, "GET " + CONTEXT_ROOT + "/"
        + "JspDoBodyUsageContextTest2.jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: positiveJspDoBodyUsageContextTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy:
   */

  public void positiveJspDoBodyUsageContextTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST, "GET " + CONTEXT_ROOT + "/"
        + "JspDoBodyUsageContextTest3.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }
}
