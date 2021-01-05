/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsp.api.jakarta_servlet.jsp.tagext.tagfileinfo;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

/**
 * Test client for TagFileInfo. Implementation note, all tests are performed
 * within a TagExtraInfo class. If the test fails, a translation error will be
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

    setContextRoot("/jsp_taginfo_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: tagFileInfoGetNameTest
   * 
   * @assertion_ids: JSP:JAVADOC:259
   * 
   * @test_Strategy: Validate TagFileInfo.getName returns the expected values
   * based on what is defined in the TLD.
   */
  public void tagFileInfoGetNameTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagfileinfo_web/GetNameTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagFileInfoGetPathTest
   * 
   * @assertion_ids: JSP:JAVADOC:260
   * 
   * @test_Strategy: Validate TagFileInfo.getPath() returns the expected values
   * based on what is defined in the TLD.
   */
  public void tagFileInfoGetPathTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagfileinfo_web/GetPathTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagFileInfoGetTagInfoTest
   * 
   * @assertion_ids: JSP:JAVADOC:261
   * 
   * @test_Strategy: Validate TagFileInfo.getTagInfo() returns the expected
   * values based on what is defined in the tag file.
   */
  public void tagFileInfoGetTagInfoTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagfileinfo_web/GetTagInfoTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }
}
