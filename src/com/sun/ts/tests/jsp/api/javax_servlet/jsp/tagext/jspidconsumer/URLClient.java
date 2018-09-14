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

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.jspidconsumer;

import java.io.PrintWriter;
import java.io.IOException;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.lib.util.TestUtil;
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

    setContextRoot("/jsp_jspidconsumer_web");
    // setTestJsp("SetJspIdTest");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: setJspIdTest
   * 
   * @assertion_ids: JSP:JAVADOC:434
   * 
   * @test_Strategy: Validate the behavior of JspIdConsumer.setJspId() Implement
   * the setJspId() method in a tag handler. Verify that the ID generated
   * conforms to the rules set forth in the javadoc.
   */
  public void setJspIdTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_jspidconsumer_web/SetJspIdTest.jsp HTTP 1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: multipleJspIdTest
   * 
   * @assertion_ids: JSP:JAVADOC:434
   * 
   * @test_Strategy: Validate the behavior of JspIdConsumer.setJspId() Implement
   * the setJspId() method in multiple tag handlers. Verify that each tag has a
   * unique ID. [JspConsumerIdUniqueIdString]
   */
  public void multipleJspIdTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_jspidconsumer_web/MultipleJspIdTest.jsp HTTP 1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: sameJspIdTest
   * 
   * @assertion_ids: JSP:JAVADOC:434
   * 
   * @test_Strategy: Validate the behavior of JspIdConsumer.setJspId() Implement
   * the setJspId() method in a single tag handler. Verify that when the jsp
   * page is invoked multiple times, the tag's ID does not change.
   * [JspConsumerIdUniqueIdString]
   */
  public void sameJspIdTest() throws Fault {

    for (int i = 1; i < SameJspIdTag.NUM_INVOC; ++i) {
      TEST_PROPS.setProperty(REQUEST,
          "GET /jsp_jspidconsumer_web/SameJspIdTest.jsp HTTP 1.1");
      TEST_PROPS.setProperty(IGNORE_STATUS_CODE, "true");
      TEST_PROPS.setProperty(IGNORE_BODY, "true");
      invoke();
    }
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_jspidconsumer_web/SameJspIdTest.jsp HTTP 1.1");
    TEST_PROPS.setProperty(IGNORE_STATUS_CODE, "false");
    TEST_PROPS.setProperty(IGNORE_BODY, "false");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }
}
