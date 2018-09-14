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

package com.sun.ts.tests.servlet.spec.servletmapping;

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
    setContextRoot("/servlet_js_servletmapping_web");
    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */
  /*
   * @testName: multiURLmappingTest1
   *
   * @assertion_ids: Servlet:SPEC:133; Servlet:SPEC:134.1; Servlet:SPEC:207;
   *
   * @test_Strategy: 1. Create servlet TestServlet1 with multiple URLs:
   * /foo/bar/* /TestServlet1 /foo/baR/TestServlet1 /test/Test1.bop 2. Create
   * servlet TestServlet2 with multiple URLs: /foo/baR/* /TestServlet2 3. Send
   * request to /TestServlet1, verify TestServlet1 is invoked 4. Send request to
   * /TestServlet2, verify TestServlet2 is invoked
   */
  public void multiURLmappingTest1() throws Fault {
    TEST_PROPS.setProperty(SEARCH_STRING, "TestServlet1");
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/TestServlet1" + " HTTP/1.1");
    invoke();

    TEST_PROPS.setProperty(SEARCH_STRING, "TestServlet2");
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/TestServlet2" + " HTTP/1.1");
    invoke();
  }

  /*
   * @testName: multiURLmappingTest2
   *
   * @assertion_ids: Servlet:SPEC:133; Servlet:SPEC:134.1; Servlet:SPEC:134.2;
   * Servlet:SPEC:134.4; Servlet:SPEC:207;
   *
   * @test_Strategy: 1. Create servlet TestServlet1 with multiple URLs:
   * /foo/bar/* /TestServlet1 /foo/baR/TestServlet1 /test/Test1.bop 2. Create
   * servlet TestServlet5 with multiple URLs: /foo/bar/TestServlet5
   * /TestServlet5 3. Send request to /foo/bar/xyz, verify TestServlet1 is
   * invoked 4. Send request to /foo/bar/TestServlet5, verify TestServlet5 is
   * invoked
   */
  public void multiURLmappingTest2() throws Fault {
    TEST_PROPS.setProperty(SEARCH_STRING, "TestServlet1");
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/foo/bar/xyz" + " HTTP/1.1");
    invoke();

    TEST_PROPS.setProperty(SEARCH_STRING, "TestServlet5");
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/foo/bar/TestServlet5" + " HTTP/1.1");
    invoke();
  }

  /*
   * @testName: multiURLmappingTest3
   *
   * @assertion_ids: Servlet:SPEC:133; Servlet:SPEC:134.1; Servlet:SPEC:134.2;
   * Servlet:SPEC:134.4; Servlet:SPEC:207;
   *
   * @test_Strategy: 1. Create servlet TestServlet1 with multiple URLs:
   * /foo/bar/* /TestServlet1 /foo/baR/TestServlet1 /test/Test1.bop 2. Create
   * servlet TestServlet2 with multiple URLs: /foo/baR/* /TestServlet2 3. Send
   * request to /foo/baR/TestServlet1, verify TestServlet1 is invoked 4. Send
   * request to /foo/baR/Ten, verify TestServlet2 is invoked
   */
  public void multiURLmappingTest3() throws Fault {
    TEST_PROPS.setProperty(SEARCH_STRING, "TestServlet1");
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/foo/baR/TestServlet1" + " HTTP/1.1");
    invoke();

    TEST_PROPS.setProperty(SEARCH_STRING, "TestServlet2");
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/foo/baR/Ten" + " HTTP/1.1");
    invoke();
  }

  /*
   * @testName: multiURLmappingTest4
   *
   * @assertion_ids: Servlet:SPEC:133; Servlet:SPEC:134.1; Servlet:SPEC:134.2;
   * Servlet:SPEC:134.3; Servlet:SPEC:134.4; Servlet:SPEC:207;
   *
   * @test_Strategy: 1. Create servlet TestServlet1 with multiple URLs:
   * /foo/bar/* /TestServlet1 /foo/baR/TestServlet1 /test/Test1.bop 2. Create
   * servlet TestServlet4 with multiple URLs: *.bop /TestServlet4 3. Send
   * request to /test/Test1.bop, verify TestServlet1 is invoked 4. Send request
   * to /Test1.bop, verify TestServlet4 is invoked
   */
  public void multiURLmappingTest4() throws Fault {
    TEST_PROPS.setProperty(SEARCH_STRING, "TestServlet1");
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/test/Test1.bop" + " HTTP/1.1");
    invoke();

    TEST_PROPS.setProperty(SEARCH_STRING, "TestServlet4");
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/Test1.bop" + " HTTP/1.1");
    invoke();
  }
}
