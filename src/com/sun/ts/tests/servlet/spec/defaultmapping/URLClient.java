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

package com.sun.ts.tests.servlet.spec.defaultmapping;

import java.io.PrintWriter;
import com.sun.javatest.Status;
import com.sun.ts.tests.servlet.common.client.AbstractUrlClient;

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
  public Status run(String[] args, PrintWriter out, PrintWriter err) {
    setContextRoot("/servlet_spec_defaultmapping_web");
    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */
  /* Run test */
  /*
   * @testName: defaultservletTest1
   *
   * @assertion_ids: Servlet:SPEC:134.4;
   *
   * @test_Strategy: 1. Create servlets TestServlet1 with URL /foo/bar/*,
   * TestServlet2 with URL /foo/baR/*, TestServlet3 with URL /TestServlet3,
   * TestServlet4 with URL *.bop, TestServlet5 with URL /foo/bar/TestServlet5.
   * TestServlet6 with URL / 2. Send request with path /TestServlet3/xyz. 3.
   * Verify that TestServlet6 is invoked based on Servlet Spec(11.1)
   */
  public void defaultservletTest1() throws Fault {
    TEST_PROPS.setProperty(SEARCH_STRING, "TestServlet6");
    TEST_PROPS.setProperty(APITEST, "TestServlet3/xyz");
    invoke();
  }

  /*
   * @testName: defaultservletTest
   *
   * @assertion_ids: Servlet:SPEC:134.4;
   *
   * @test_Strategy: 1. Create servlets TestServlet1 with URL /foo/bar/*,
   * TestServlet2 with URL /foo/baR/*, TestServlet3 with URL /TestServlet3,
   * TestServlet4 with URL *.bop, TestServlet5 with URL /foo/bar/TestServlet5
   * TestServlet6 with URL / 2. Send request with path /test/foo/bar/xxx 3.
   * Verify that default Servlet TestServlet6 should be invoked. Since no match
   * is found.
   */
  public void defaultservletTest() throws Fault {
    TEST_PROPS.setProperty(SEARCH_STRING, "TestServlet6");
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/test/foo/bar/xxx" + " HTTP/1.1");
    invoke();
  }
}
