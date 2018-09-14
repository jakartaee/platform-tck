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

package com.sun.ts.tests.jsp.spec.tagfiles.directives.general;

import java.io.PrintWriter;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

public class URLClient extends AbstractUrlClient {
  private static final String CONTEXT_ROOT = "/jsp_tagfile_directives_general_web";

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

    setContextRoot(CONTEXT_ROOT);

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: negativePageDirectiveTest
   * 
   * @assertion_ids: JSP:SPEC:223
   * 
   * @test_Strategy: If a page directive is used in a tag file, a translation
   * error must result.
   */

  public void negativePageDirectiveTest() throws Fault {
    String testName = "negativePageDirective";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeTagDirectiveTest
   * 
   * @assertion_ids: JSP:SPEC:226
   * 
   * @test_Strategy: If a tag directive is used in a jsp page, a translation
   * error must result.
   */

  public void negativeTagDirectiveTest() throws Fault {
    String testName = "negativeTagDirective";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeAttributeDirectiveTest
   * 
   * @assertion_ids: JSP:SPEC:227
   * 
   * @test_Strategy: If a attribute directive is used in a jsp page, a
   * translation error must result.
   */

  public void negativeAttributeDirectiveTest() throws Fault {
    String testName = "negativeAttributeDirective";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeVariableDirectiveTest
   * 
   * @assertion_ids: JSP:SPEC:228
   * 
   * @test_Strategy: If a variable directive is used in a jsp page, a
   * translation error must result.
   */

  public void negativeVariableDirectiveTest() throws Fault {
    String testName = "negativeVariableDirective";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: positiveIncludeContextRelativeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: use tag include directive with a context relative path.
   */

  public void positiveIncludeContextRelativeTest() throws Fault {
    String testName = "positiveIncludeContextRelative";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: positiveIncludePageRelativeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: use tag include directive with a page relative path.
   */

  public void positiveIncludePageRelativeTest() throws Fault {
    String testName = "positiveIncludePageRelative";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: negativeIncludeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: use tag include directive to include unsuitable content
   */

  public void negativeIncludeTest() throws Fault {
    String testName = "negativeInclude";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: positiveTaglibTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: invoke a tag file from within a tag file.
   */

  public void positiveTaglibTest() throws Fault {
    String testName = "positiveTaglib";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED|from invokee");
    invoke();
  }

}
