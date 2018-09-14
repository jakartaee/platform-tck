/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.servlet.spec.pluggability.ordering.test1;

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
    setContextRoot("/servlet_spec_ordering1_web");
    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */
  /*
   * @testName: initParamTest
   *
   * @assertion_ids: Servlet:SPEC:232; Servlet:SPEC:241; Servlet:SPEC:242;
   * Servlet:SPEC:244; Servlet:SPEC:245; Servlet:SPEC:258.1;
   * Servlet:SPEC:258.5.1; Servlet:SPEC:258.6.1; Servlet:SPEC:258.7.1;
   *
   * @test_Strategy: 1. Define servlet TestServlet4 in web.xml as well as three
   * web-fragment.xml; 2. Send request to /TestServlet4, verify TestServlet4 is
   * invoked 3. Also verify that <init-param> defined in web.xml and all
   * web-fragment.xml are considered, and the one defined in web.xml take
   * precedence.
   */
  public void initParamTest() throws Fault {
    TEST_PROPS.setProperty(SEARCH_STRING, "TestServlet4|"
        + "msg1=first|msg2=second|msg3=third|msg4=fourth|" + "RequestListener");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "ignore");
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/TestServlet4" + " HTTP/1.1");
    invoke();
  }
}
