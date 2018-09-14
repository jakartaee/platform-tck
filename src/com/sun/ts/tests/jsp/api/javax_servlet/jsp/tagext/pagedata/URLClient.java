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

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.pagedata;

import java.io.PrintWriter;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

/**
 * Test client for TagAttributeInfo. Implementation note, all tests are
 * performed within a TagExtraInfo class. If the test fails, a translation error
 * will be generated and a ValidationMessage array will be returned.
 */
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

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: pageDataTest
   * 
   * @assertion_ids: JSP:JAVADOC:313
   * 
   * @test_Strategy: Validate the following: - We can get an inputstream from
   * the PageData object provided to the validation method of the
   * TagLibraryValidator. - Validate the XML view of a JSP page: - page
   * directives are jsp:directive.page elements - taglib directives are includes
   * in the namespace declaration in the jsp:root element - include directives
   * are not present in the XML view - template text is wrapped by jsp:text
   * elements - scriptlets are wrapped by jsp:scriptlet elements - declarations
   * are wrapped by jsp:declaration elements - JSP expressions are wrapped by
   * jsp:expression elements - rt expressions are converted from '<%=' '%>' to
   * '%=' '%' - Custom taglib usages are passed through - the jsp:root element
   * is present - the jsp namespace is present in the jsp:root element.
   */
  public void pageDataTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_pagedata_web/PageDataTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "Validator Called|Expression Text|Included template text");
    invoke();
  }

  /*
   * @testName: pageDataTagFileTest
   * 
   * @assertion_ids: JSP:JAVADOC:313
   * 
   * @test_Strategy: same as above.
   */
  public void pageDataTagFileTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_pagedata_web/PageDataTagFileTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "Validator Called|Expression Text in tag file|Included template text in tag file");
    invoke();
  }

}
