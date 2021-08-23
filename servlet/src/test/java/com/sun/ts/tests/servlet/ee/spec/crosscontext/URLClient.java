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

package com.sun.ts.tests.servlet.ee.spec.crosscontext;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

public class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/servlet_ee_spec_crosscontext1_web";

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

    setContextRoot(CONTEXT_ROOT);
    setTestJsp("JSPAccessServlet");
    System.out.println("Running TESTS");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Run test */

  /*
   * @testName: forwardJSPServlet
   *
   * @assertion_ids: Servlet:SPEC:63;
   *
   * @test_Strategy: 1. Create two web applications; One with a jsp page
   * JSPAccessServlet.jsp under contextroot /servlet_spec_crosscontext1_web; The
   * other with servlet FowardedServlet under contextroot
   * /servlet_spec_crosscontext2_web; 2. client calls to the jsp page; 3. In jsp
   * page, use RequestDispatcher to forward to the servlet; 4. Verify that
   * Session attributes set in jsp cannot be seen in servlet
   */

  public void forwardJSPServlet() throws Fault {
    TEST_PROPS.setProperty(APITEST, "forwardJSPServlet");
    invoke();
  }

  /*
   * @testName: includeJSPServlet
   *
   * @assertion_ids: Servlet:SPEC:63;
   *
   * @test_Strategy: 1. Create two web applications; One with a jsp page
   * JSPAccessServlet.jsp under contextroot /servlet_spec_crosscontext1_web; The
   * other with servlet IncludedServlet under contextroot
   * /servlet_spec_crosscontext2_web; 2. client calls to the jsp page; 3. In jsp
   * page, use RequestDispatcher to include the servlet; 4. Verify that Session
   * attributes set in jsp cannot be seen in servlet and vice versa
   */

  public void includeJSPServlet() throws Fault {
    TEST_PROPS.setProperty(APITEST, "includeJSPServlet");
    invoke();
  }
}
