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

package com.sun.ts.tests.jsp.spec.core_syntax.actions.forward;

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

    setGeneralURI("/jsp/spec/core_syntax/actions/forward");
    setContextRoot("/jsp_coresyntx_act_forward_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: positiveForwardCtxRelativeTest
   * 
   * @assertion_ids: JSP:SPEC:165.1
   * 
   * @test_Strategy: Validate that jsp:forward can forward a request to a JSP
   * page within the same context using a page relative-path. PENDING Merege
   * existing forward tests
   */

  public void positiveForwardCtxRelativeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveForwardCtxRelative");
    invoke();
  }

  /*
   * @testName: positiveForwardCtxRelativeHtmlTest
   * 
   * @assertion_ids: JSP:SPEC:165.1
   * 
   * @test_Strategy: Validate that jsp:forward can forward a request to a static
   * resource within the same context using a page-relative path.
   */

  public void positiveForwardCtxRelativeHtmlTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveForwardCtxRelativeHtml");
    invoke();
  }

  /*
   * @testName: positiveForwardPageRelativeTest
   * 
   * @assertion_ids: JSP:SPEC:165.1
   * 
   * @test_Strategy: Validate that jsp:forward can forward a request to a JSP
   * page within the same context using a page-relative path.
   */

  public void positiveForwardPageRelativeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveForwardPageRelative");
    invoke();
  }

  /*
   * @testName: positiveForwardPageRelativeHtmlTest
   * 
   * @assertion_ids: JSP:SPEC:165.1
   * 
   * @test_Strategy: Validate that jsp:forward can forward a request to a static
   * resource within the same context using a page-relative path.
   */

  public void positiveForwardPageRelativeHtmlTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveForwardPageRelativeHtml");
    invoke();
  }

  /*
   * @testName: positiveRequestAttrCtxRelativeTest
   * 
   * @assertion_ids: JSP:SPEC:165.8
   * 
   * @test_Strategy: Validate that jsp:forward can properly accept a
   * request-time attribute containing a context-relative path value.
   */

  public void positiveRequestAttrCtxRelativeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveRequestAttrCtxRelative");
    invoke();
  }

  /*
   * @testName: positiveRequestAttrPageRelativeTest
   * 
   * @assertion_ids: JSP:SPEC:165.8
   * 
   * @test_Strategy: Validate that jsp:forward can properly accept a
   * request-time attribute containing a page-relative path value.
   */

  public void positiveRequestAttrPageRelativeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveRequestAttrPageRelative");
    invoke();
  }

  /*
   * @testName: unbufferedWriteForwardTest
   * 
   * @assertion_ids: JSP:SPEC:165.5
   * 
   * @test_Strategy:If the page output was unbuffered and anything has been
   * written to it, an attempt to forward the request will result in an
   * IllegalStateException.
   */

  public void unbufferedWriteForwardTest() throws Fault {
    String testName = "unbufferedWriteForwardTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_act_forward_web/unbufferedWriteForwardTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Got IllegalStateException");
    invoke();
  }

}
