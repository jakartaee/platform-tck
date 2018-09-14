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

package com.sun.ts.tests.servlet.spec.welcomefiles;

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

    setContextRoot("/servlet_js_welcomefiles_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Run test */

  /*
   * @testName: partialfound1
   *
   * @assertion_ids: Servlet:SPEC:141;
   *
   * @test_Strategy: 1. Create two welcome files <welcome-file-list>
   * <welcome-file>index.html</welcome-file>
   * <welcome-file>default.jsp</welcome-file> </welcome-file-list> 2. Verify
   * that a request URI of /foo will be returned as /foo/index.html based on
   * Servlet Spec(9.10)
   */

  public void partialfound1() throws Fault {
    TEST_PROPS.setProperty(FOLLOW_REDIRECT, "follow_redirect");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "<html>|<head>|<title>index.html</title>|</head>|<body>|INDEX from foo/index.html|</body>|</html>");
    TEST_PROPS.setProperty(APITEST, "foo");
    invoke();
  }

  /*
   * @testName: partialfound2
   *
   * @assertion_ids: Servlet:SPEC:141;
   *
   * @test_Strategy: 1. Create two welcome files <welcome-file-list>
   * <welcome-file>index.html</welcome-file>
   * <welcome-file>default.jsp</welcome-file> </welcome-file-list> 2. Verify
   * that a request URI of /catalog will be returned as /catalog/default.jsp
   * based on Servlet Spec(9.10)
   */

  public void partialfound2() throws Fault {
    TEST_PROPS.setProperty(FOLLOW_REDIRECT, "follow_redirect");
    TEST_PROPS.setProperty(SEARCH_STRING, "HELLO from catalog/default.jsp");
    TEST_PROPS.setProperty(APITEST, "catalog");
    invoke();
  }

}
