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

package com.sun.ts.tests.jsp.spec.core_syntax.actions.include;

import java.io.PrintWriter;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

public class URLClient extends AbstractUrlClient {
  private static final String CONTEXT_ROOT = "/jsp_coresyntx_act_include_web";

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

    setGeneralURI("/jsp/spec/core_syntax/actions/include");
    setContextRoot("/jsp_coresyntx_act_include_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: positiveIncludeCtxRelativeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Include content, using a context-relative path, from JSP
   * page into the current JSP page.
   * 
   * jsp:include provides for the inclusion of dynamic resources, within the
   * same context, using a context-relative path. JavaServer Pages Specification
   * v1.2, Sec. 4.4
   */

  public void positiveIncludeCtxRelativeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveIncludeCtxRelative");
    invoke();
  }

  /*
   * @testName: positiveIncludeCtxRelativeHtmlTest
   * 
   * @assertion_ids: JSP:SPEC:164.1
   * 
   * @test_Strategy: Include content, using a context-relative path, from a
   * static HTML page into the current JSP page. PENDING Merge existing tests
   * into one test.
   */

  public void positiveIncludeCtxRelativeHtmlTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveIncludeCtxRelativeHtml");
    invoke();
  }

  /*
   * @testName: positiveIncludePageRelativeTest
   * 
   * @assertion_ids: JSP:SPEC:164.1
   * 
   * @test_Strategy: Include content, using a page-relative path, from a JSP
   * page into the current JSP page.
   */
  public void positiveIncludePageRelativeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveIncludePageRelative");
    invoke();
  }

  /*
   * @testName: positiveRequestAttrCtxRelativeTest
   * 
   * @assertion_ids: JSP:SPEC:164.1;JSP:SPEC:164.9
   * 
   * @test_Strategy: Validate the page attribute of jsp:include can correctly
   * accept request-time attribute values which contain context-relative paths.
   */

  public void positiveRequestAttrCtxRelativeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveRequestAttrCtxRelative");
    invoke();
  }

  /*
   * @testName: positiveRequestAttrPageRelativeTest
   * 
   * @assertion_ids: JSP:SPEC:164.1
   * 
   * @test_Strategy: Validate the page attribute of jsp:include can correctly
   * accept request-time attribute values which contain page-relative paths.
   */

  public void positiveRequestAttrPageRelativeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveRequestAttrPageRelative");
    invoke();
  }

  /*
   * @testName: positiveIncludePageRelative2Test
   * 
   * @assertion_ids: JSP:SPEC:164.1
   * 
   * @test_Strategy: Include content, using a page-relative path, from a JSP
   * page in a different dir into the current JSP page, with a jsp-config
   * url-pattern in web.xml
   */
  public void positiveIncludePageRelative2Test() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveIncludePageRelative2");
    invoke();
  }

  /*
   * @testName: positiveIncludeForwardTest
   * 
   * @assertion_ids: JSP:SPEC:164.1
   * 
   * @test_Strategy: Include a jsp, which forwards to a html file Only the
   * output from the forwarded target should be sent to client, because the test
   * jsp and included jsp shared the same out (JSP 5.4), and a forwarding clears
   * the response buffer (SVR 8.4).
   * 
   */
  public void positiveIncludeForwardTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveIncludeForward");
    invoke();
  }

  /*
   * @testName: includeMappedServletTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: set page attribute to a mapped servlet.
   */

  public void includeMappedServletTest() throws Fault {
    String testName = "includeMappedServletTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "shoule not be served");
    invoke();
  }

  /*
   * @testName: staticStaticTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy:complex set of inclusions in JSP.5.4
   */

  public void staticStaticTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/staticStatic_A.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "In /include/C.jsp");
    invoke();
  }

  /*
   * @testName: dynamicDynamicTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: complex set of inclusions in JSP.5.4
   */

  public void dynamicDynamicTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/dynamicDynamic_A.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "In /include/C.jsp");
    invoke();
  }

  /*
   * @testName: dynamicStaticTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: complex set of inclusions in JSP.5.4
   */

  public void dynamicStaticTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/dynamicStatic_A.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "In /include/C.jsp");
    invoke();
  }

  /*
   * @testName: staticDynamicTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: complex set of inclusions in JSP.5.4
   */

  public void staticDynamicTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/staticDynamic_A.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "In /C.jsp");
    invoke();
  }
}
