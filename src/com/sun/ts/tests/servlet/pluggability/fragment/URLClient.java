/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.servlet.pluggability.fragment;

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
    setContextRoot("/servlet_spec_fragment_web");
    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */
  /*
   * @testName: initParamTest
   *
   * @assertion_ids: Servlet:SPEC:232; Servlet:SPEC:258.1; Servlet:SPEC:258.5.1;
   * Servlet:SPEC:258.6.1; Servlet:SPEC:258.7.1;
   *
   * @test_Strategy: 1. Define servlet TestServlet1 in web.xml as well as
   * web-fragment.xml; 2. Send request to /TestServlet1, verify TestServlet1 is
   * invoked 3. Also verify that <init-param> defined in both web.xml and
   * web-fragment.xml are considered, and the one defined in web.xml take
   * precedence.
   */
  public void initParamTest() throws Fault {
    TEST_PROPS.setProperty(SEARCH_STRING,
        "TestServlet1|msg1=first|msg2=second");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "ignore");
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/TestServlet1" + " HTTP/1.1");
    invoke();
  }

  /*
   * @testName: addServletTest
   *
   * @assertion_ids: Servlet:SPEC:232; Servlet:SPEC:258.1; Servlet:SPEC:258.5.1;
   *
   * @test_Strategy: 1. Define servlet TestServlet3 web-fragment.xml; 2. Send
   * request to /TestServlet3, verify TestServlet3 is invoked 3. Also verify
   * that filter is invoked too.
   */
  public void addServletTest() throws Fault {
    TEST_PROPS.setProperty(SEARCH_STRING,
        "TestFilter3|fragment|three|TestServlet3|msg1=third|msg2=third");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "ignore");
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/TestServlet3" + " HTTP/1.1");
    invoke();
  }

  /*
   * @testName: addServletURLTest
   *
   * @assertion_ids: Servlet:SPEC:232; Servlet:SPEC:258.1; Servlet:SPEC:258.5.1;
   * Servlet:SPEC:258.6.1; Servlet:SPEC:258.7.6;
   *
   * @test_Strategy: 1. Define servlet TestServlet2 web-fragment.xml and
   * web.xml; 2. Send request to URL /TestServlet2 defined in web.xml, verify
   * TestServlet2 is invoked 3. Send request to URL /TestServlet22 defined in
   * web-fragment.xml, verify 404 is returned.
   */
  public void addServletURLTest() throws Fault {
    TEST_PROPS.setProperty(SEARCH_STRING, "TestServlet2");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "ignore");
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/TestServlet2" + " HTTP/1.1");
    invoke();

    TEST_PROPS.setProperty(STATUS_CODE, NOT_FOUND);
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/TestServlet22" + " HTTP/1.1");
    invoke();
  }

  /*
   * @testName: welcomefileTest
   *
   * @assertion_ids: Servlet:SPEC:232; Servlet:SPEC:258.1; Servlet:SPEC:258.5.1;
   * Servlet:SPEC:258.6.1; Servlet:SPEC:258.7.5;
   *
   * @test_Strategy: 1. Define servlet TestServlet4 web-fragment.xml in
   * <welcome-file>; 2. Send request to URL /, verify TestServlet4 is invoked
   */
  public void welcomefileTest() throws Fault {
    TEST_PROPS.setProperty(SEARCH_STRING, "TestServlet4");
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + " HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "ignore");
    invoke();
  }

  /*
   * @testName: filterOrderingTest
   *
   * @assertion_ids: Servlet:SPEC:232; Servlet:SPEC:241; Servlet:SPEC:242;
   * Servlet:SPEC:244; Servlet:SPEC:245; Servlet:SPEC:246; Servlet:SPEC:258.1;
   * Servlet:SPEC:258.5.1; Servlet:SPEC:258.6.1;
   *
   * @test_Strategy: 1. Define four Filters that mapping to TestServlet5 in
   * web.xml and two web-fragment.xml; 2. Send request to TestServlet5 3. Verify
   * that web.xml is always processed first; 4. Verify that <ordering> works
   * accordingly.
   */
  public void filterOrderingTest() throws Fault {
    TEST_PROPS.setProperty(SEARCH_STRING,
        "TestFilter|fragment|none|" + "TestFilter3|fragment|three|"
            + "TestFilter2|fragment|two|"
            + "TestFilter1|fragment|one|TestServlet5");
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/TestServlet5" + " HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "ignore");
    invoke();
  }
}
