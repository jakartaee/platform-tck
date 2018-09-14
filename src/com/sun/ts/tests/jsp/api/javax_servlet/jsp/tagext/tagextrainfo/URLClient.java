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

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.tagextrainfo;

import java.io.PrintWriter;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

/**
 * Test client for TagExtraInfo. If the test fails, a translation error will be
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

    setContextRoot("/jsp_tagextrainfo_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: tagExtraInfoTest
   * 
   * @assertion_ids:
   * JSP:JAVADOC:264;JSP:JAVADOC:265;JSP:JAVADOC:266;JSP:JAVADOC:267
   * 
   * @test_Strategy: Validate the following: - TagExtraInfo.getTagInfo() returns
   * a non-null value as the container called TagExtraInfo.setTagInfo() prior to
   * calling validate. - A null or an emtpy array of ValidationMessage returned
   * by validate does not cause a translation error. - A non-empty array of
   * ValiationMessages causes a translation error. - The default implementation
   * of TagExtraInfo.validate() calls isValid(). If isValid() returns false, a
   * default ValidationMessage array is returned.
   */
  public void tagExtraInfoTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagextrainfo_web/TagExtraInfoNullReturnTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagextrainfo_web/TagExtraInfoEmptyReturnTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagextrainfo_web/TagExtraInfoNonEmptyReturnTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagextrainfo_web/TagExtraInfoDefaultImplTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }
}
