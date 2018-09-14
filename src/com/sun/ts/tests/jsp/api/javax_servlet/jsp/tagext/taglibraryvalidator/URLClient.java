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
 * @(#)URLClient.java	1.2 10/09/02
 */

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.taglibraryvalidator;

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

    setContextRoot("/jsp_taglibvalidator_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: tagLibraryValidatorAPITest
   * 
   * @assertion_ids: JSP:JAVADOC:212;JSP:JAVADOC:213;JSP:JAVADOC:214
   * 
   * @test_Strategy: Validate the setInitParameters(), getInitParameters(), and
   * validate() methods of the TagLibraryValidator class. This will verify that
   * the configured TLV is called only once for each taglibrary URI defined
   * within the page, that setInitParameters() is invoked by the container
   * before validate is called, and that an empty array of ValidationMessages or
   * a null return value from validate() indicates the page is valid, thus no
   * translation error will occur.
   */
  public void tagLibraryValidatorAPITest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taglibvalidator_web/TagLibraryValidatorTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    TEST_PROPS.setProperty(SEARCH_STRING,
        "Test PASSED.  Validator call count was 1");
    invoke();
  }

  /*
   * @testName: tagLibraryValidatorTranslationFailureTest
   * 
   * @assertion_ids: JSP:JAVADOC:182;JSP:JAVADOC:183;
   * JSP:JAVADOC:184;JSP:JAVADOC:386
   * 
   * @test_Strategy: Validate that a translation error will occur if a non-null
   * or non-empty array of ValidationMessages is returned when the validation()
   * method is called. This also validates the use of the ValidationMessage
   * class.
   */
  public void tagLibraryValidatorTranslationFailureTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taglibvalidator_web/TLVTranslationErrorTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }
}
