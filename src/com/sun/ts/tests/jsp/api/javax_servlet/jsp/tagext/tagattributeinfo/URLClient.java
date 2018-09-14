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

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.tagattributeinfo;

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
   * @testName: tagAttributeInfoGetNameTest
   * 
   * @assertion_ids: JSP:JAVADOC:278
   * 
   * @test_Strategy: Validate the behavior of TagAttributeInfo.getName().
   */
  public void tagAttributeInfoGetNameTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagattrinfo_web/GetNameTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: tagAttributeInfoGetTypeNameTest
   * 
   * @assertion_ids: JSP:JAVADOC:279
   * 
   * @test_Strategy: Validate the behavior of TagAttributeInfo.getTypeName().
   */
  public void tagAttributeInfoGetTypeNameTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagattrinfo_web/GetTypeNameTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: tagAttributeInfoCanBeRequestTimeTest
   * 
   * @assertion_ids: JSP:JAVADOC:280
   * 
   * @test_Strategy: Validate the behavior of
   * TagAttributeInfo.catBeRequestTime().
   */
  public void tagAttributeInfoCanBeRequestTimeTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagattrinfo_web/CanBeRequestTimeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: tagAttributeInfoIsRequiredTest
   * 
   * @assertion_ids: JSP:JAVADOC:281
   * 
   * @test_Strategy: Validate the behavior of TagAttributeInfo.isRequired().
   */
  public void tagAttributeInfoIsRequiredTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagattrinfo_web/IsRequiredTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: tagAttributeInfoIsFragmentTest
   * 
   * @assertion_ids: JSP:JAVADOC:283
   * 
   * @test_Strategy: Validate the behavior of TagAttributeInfo.siFragment().
   */
  public void tagAttributeInfoIsFragmentTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagattrinfo_web/IsFragmentTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: tagAttributeInfoToStringTest
   * 
   * @assertion_ids: JSP:JAVADOC:284
   * 
   * @test_Strategy: Validate the behavior of TagAttributeInfo.toString().
   */
  public void tagAttributeInfoToStringTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagattrinfo_web/ToStringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: tagAttributeInfoGetIdAttribute
   * 
   * @assertion_ids: JSP:JAVADOC:282
   * 
   * @test_Strategy: Convenience static method that goes through an array of
   * TagAttributeInfo objects and looks for "id".
   */
  public void tagAttributeInfoGetIdAttribute() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagattrinfo_web/GetIdAttribute.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }
}
