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

package com.sun.ts.tests.servlet.spec.rdspecialchar;

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
  public Status run(String args[], PrintWriter out, PrintWriter err) {

    setContextRoot("/servlet_spec_rdspecialchar_web");
    setServletName("TestServlet");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /*
   * @testName: querySemicolonInclude
   *
   * @assertion_ids: Servlet:SPEC:76.1; Servlet:SPEC:76.2; Servlet:SPEC:76.3;
   * Servlet:SPEC:76.4; Servlet:SPEC:76.5;
   *
   * @test_Strategy: 1. Create servlets TestServlet and IncludedServlet; 2. In
   * TestServlet, get RequestDispatcher by using
   * ServletContext.getRequestDispatcher(path), with special character ";" in
   * path as part of query string, and access IncludedServlet using
   * RequestDispatcher.include. 3. Verify that IncludedServlet is invoked.
   */

  public void querySemicolonInclude() throws Fault {
    TEST_PROPS.setProperty(APITEST, "querySemicolonInclude");
    invoke();
  }

  /*
   * @testName: querySemicolonForward
   *
   * @assertion_ids: Servlet:SPEC:76.1; Servlet:SPEC:76.2; Servlet:SPEC:76.3;
   * Servlet:SPEC:76.4; Servlet:SPEC:76.5;
   *
   * @test_Strategy: 1. Create servlets TestServlet and IncludedServlet; 2. In
   * TestServlet, get RequestDispatcher by using
   * ServletContext.getRequestDispatcher(path), with special character ";" in
   * path as part of query string, and access IncludedServlet using
   * RequestDispatcher.forward. 3. Verify that IncludedServlet is invoked.
   */

  public void querySemicolonForward() throws Fault {
    TEST_PROPS.setProperty(APITEST, "querySemicolonForward");
    invoke();
  }

}
