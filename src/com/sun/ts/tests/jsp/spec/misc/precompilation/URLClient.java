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

package com.sun.ts.tests.jsp.spec.misc.precompilation;

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

    setGeneralURI("/jsp/spec/misc/precompilation");
    setContextRoot("/jsp_misc_precompilation_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: precompileNoValueTest
   * 
   * @assertion_ids: JSP:SPEC:244.1.4
   * 
   * @test_Strategy: Validate that no response body is returned when
   * jsp_precompile has no value.
   */

  public void precompileNoValueTest() throws Fault {
    String testName = "precompileNoValue";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_misc_precompilation_web/precompile.jsp?jsp_precompile HTTP/1.0");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Got the Request");
    invoke();
  }

  /*
   * @testName: precompileFalseTest
   * 
   * @assertion_ids: JSP:SPEC:244.1.2
   * 
   * @test_Strategy: Validate that no response body is returned when
   * jsp_precompile is set to false.
   */

  public void precompileFalseTest() throws Fault {
    String testName = "precompileFalse";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_misc_precompilation_web/precompile.jsp?jsp_precompile=false HTTP/1.0");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Got the Request");
    invoke();
  }

  /*
   * @testName: precompileTrueTest
   * 
   * @assertion_ids: JSP:SPEC:244.1.1
   * 
   * @test_Strategy: Validate that no response body is returned when
   * jsp_precompile is set to true.
   */

  public void precompileTrueTest() throws Fault {
    String testName = "precompileTrue";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_misc_precompilation_web/precompile.jsp?jsp_precompile=true HTTP/1.0");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Got the Request");
    invoke();
  }

  /*
   * @testName: precompileNegativeTest
   * 
   * @assertion_ids: JSP:SPEC:244.1.5
   * 
   * @test_Strategy: Set the jsp_precompile request paramter to a non valid
   * value and validate that a 500 error occurs.
   */

  public void precompileNegativeTest() throws Fault {
    String testName = "precompileNegative";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_misc_precompilation_web/precompile.jsp?jsp_precompile=any_invalid_value HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

}
