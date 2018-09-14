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

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.tagdata;

import java.io.PrintWriter;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

/**
 * Test client for TagData. Implementation note, all tests are performed within
 * a TagExtraInfo class. If the test fails, a translation error will be
 * generated and a ValidationMessage array will be returned.
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
   * @testName: tagDataGetAttributeTest
   * 
   * @assertion_ids: JSP:JAVADOC:271
   * 
   * @test_Strategy: Validate the behavior of TagData.getAttribute().
   */
  public void tagDataGetAttributeTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagdata_web/GetAttributeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: tagDataSetAttributeTest
   * 
   * @assertion_ids: JSP:JAVADOC:272
   * 
   * @test_Strategy: Validate the behavior of TagData.setAttribute().
   */
  public void tagDataSetAttributeTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagdata_web/SetAttributeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: tagDataGetAttributeStringTest
   * 
   * @assertion_ids: JSP:JAVADOC:273;JSP:JAVADOC:274
   * 
   * @test_Strategy: Validate the behavior of TagData.getAttributeString().
   */
  public void tagDataGetAttributeStringTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagdata_web/GetAttributeStringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: tagDataGetAttributesTest
   * 
   * @assertion_ids: JSP:JAVADOC:275
   * 
   * @test_Strategy: Validate the behavior of TagData.getAttributes().
   */
  public void tagDataGetAttributesTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagdata_web/GetAttributesTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: tagDataConstructorTest
   * 
   * @assertion_ids: JSP:JAVADOC:268
   * 
   * @test_Strategy: validate the constructor TagData(Object[][]).
   */
  public void tagDataConstructorTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagdata_web/ConstructorTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }
}
