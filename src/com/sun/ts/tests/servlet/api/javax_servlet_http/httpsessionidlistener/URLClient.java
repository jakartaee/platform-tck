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
 * $Id:$
 */
package com.sun.ts.tests.servlet.api.javax_servlet_http.httpsessionidlistener;

import com.sun.javatest.Status;
import com.sun.ts.tests.servlet.api.common.request.HttpRequestClient;
import java.io.PrintWriter;

public class URLClient extends HttpRequestClient {

  private static final String CONTEXT_ROOT = "/servlet_jsh_httpsessionidlistener_web";

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

    setServletName("TestServlet");
    setContextRoot(CONTEXT_ROOT);

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */
  /*
   * @testName: changeSessionIDTest1
   *
   * @assertion_ids: Servlet:JAVADOC:304; Servlet:JAVADOC:467;
   * Servlet:JAVADOC:476; Servlet:JAVADOC:484; Servlet:JAVADOC:565;
   * Servlet:JAVADOC:566; Servlet:JAVADOC:929; Servlet:JAVADOC:935;
   *
   * @test_Strategy: Send an HttpServletRequest to server; Verify that
   * request.changeSessionId() works.
   */
}
