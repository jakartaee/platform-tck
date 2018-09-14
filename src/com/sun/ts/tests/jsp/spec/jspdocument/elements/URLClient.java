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

package com.sun.ts.tests.jsp.spec.jspdocument.elements;

import java.io.PrintWriter;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

public class URLClient extends AbstractUrlClient {
  private static final String CONTEXT_ROOT = "/jsp_jspdocument_elements_web";

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
   */

  /*
   * @testName: negativeJspRootTest
   * 
   * @assertion_ids: JSP:SPEC:177; JSP:SPEC:173.1
   * 
   * @test_Strategy: use jsp:root not as the top element of a jsp document.
   * jsp-property-group config overrides other determiniations.
   */

  public void negativeJspRootTest() throws Fault {
    String testName = "negativeJspRoot";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + "notJspDocument.jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: jspRootPrefixTest
   * 
   * @assertion_ids: JSP:SPEC:175
   * 
   * @test_Strategy: use a different prefix abc
   */

  public void jspRootPrefixTest() throws Fault {
    String testName = "jspRootPrefix";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    invoke();
  }

  /*
   * @testName: directiveIncludeLocationTest
   * 
   * @assertion_ids: JSP:SPEC:179.1; JSP:SPEC:179; JSP:SPEC:179.3
   * 
   * @test_Strategy: use jsp:directive.include anywhere within a jsp document.
   */

  public void directiveIncludeLocationTest() throws Fault {
    String testName = "directiveIncludeLocation";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    TEST_PROPS.setProperty(SEARCH_STRING, "HELLO|ONE|HELLO|TWO");
    invoke();
  }

  /*
   * @testName: directiveIncludeInJspPageTest
   * 
   * @assertion_ids: JSP:SPEC:179.5
   * 
   * @test_Strategy: use jsp:directive.include in a jsp page
   */

  public void directiveIncludeInJspPageTest() throws Fault {
    String testName = "directiveIncludeInJspPage";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    TEST_PROPS.setProperty(SEARCH_STRING, "HELLO|ONE");
    invoke();
  }

  /*
   * @testName: directivePageInJspPageTest
   * 
   * @assertion_ids: JSP:SPEC:178.39
   * 
   * @test_Strategy: use jsp:directive.page in a jsp page
   */

  public void directivePageInJspPageTest() throws Fault {
    String testName = "directivePageInJspPage";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    TEST_PROPS.setProperty(SEARCH_STRING, "listSize2");
    invoke();
  }

  /*
   * @testName: scriptingTest
   * 
   * @assertion_ids: JSP:SPEC:180; JSP:SPEC:181; JSP:SPEC:182
   * 
   * @test_Strategy: use jsp:declaration, jsp:scriptlet, and jsp:expression in a
   * jsp document.
   */

  public void scriptingTest() throws Fault {
    String testName = "scripting";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    TEST_PROPS.setProperty(SEARCH_STRING, "JSP637|JSP637|99");
    invoke();
  }

  /*
   * @testName: scriptingInJspPageTest
   * 
   * @assertion_ids: JSP:SPEC:180.4; JSP:SPEC:181.2; JSP:SPEC:182.5
   * 
   * @test_Strategy: use jsp:declaration, jsp:scriptlet, and jsp:expression in a
   * jsp page.
   */

  public void scriptingInJspPageTest() throws Fault {
    String testName = "scriptingInJspPage";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    TEST_PROPS.setProperty(SEARCH_STRING, "JSP637|99");
    invoke();
  }

}
