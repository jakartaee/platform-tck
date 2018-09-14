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

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.bodycontent;

import java.io.PrintWriter;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

/**
 * Test client for BodyContent
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
   * @testName: bodyContentFlushTest
   * 
   * @assertion_ids: JSP:JAVADOC:332
   * 
   * @test_Strategy: Validates that an IOException is thrown when
   * BodyContent.flush() is called.
   */
  public void bodyContentFlushTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_bodycontent_web/BodyContentFlushTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: bodyContentClearBodyTest
   * 
   * @assertion_ids: JSP:JAVADOC:333
   * 
   * @test_Strategy: Validate that clearBuffer() works as expected.
   */
  public void bodyContentClearBodyTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_bodycontent_web/BodyContentClearBodyTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: bodyContentReadWriteTest
   * 
   * @assertion_ids: JSP:JAVADOC:334;JSP:JAVADOC:338
   * 
   * @test_Strategy: Validate that a reader can be obtained containing the
   * bodycontent of the tag. Using the content that is read in, obtain a writer
   * and write the content to that writer.
   */
  public void bodyContentReadWriteTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_bodycontent_web/BodyContentReadWriteTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "#TestPASSED#");
    invoke();
  }

  /*
   * @testName: bodyContentWriteOutTest
   * 
   * @assertion_ids: JSP:JAVADOC:336
   * 
   * @test_Strategy: Validate the behavior of BodyContent.writeOut().
   */
  public void bodyContentWriteOutTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_bodycontent_web/BodyContentWriteOutTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: bodyContentGetStringTest
   * 
   * @assertion_ids: JSP:JAVADOC:335
   * 
   * @test_Strategy: Validate the behavior of BodyContent.getString().
   */
  public void bodyContentGetStringTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_bodycontent_web/BodyContentGetStringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }
}
