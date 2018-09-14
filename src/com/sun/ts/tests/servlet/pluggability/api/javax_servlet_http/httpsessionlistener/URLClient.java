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
package com.sun.ts.tests.servlet.pluggability.api.javax_servlet_http.httpsessionlistener;

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

    setServletName("TestServlet");
    setContextRoot("/servlet_pluh_httpsessionlistener_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: createdTest
   * 
   * @assertion_ids: Servlet:JAVADOC:454
   * 
   * @test_Strategy: Client calls a servlet that creates a session. The listener
   * should detect the creation and write a message to a static log. The Servlet
   * then reads the log and verifies the result. As a result of the test, the
   * javax.servlet.http.Event.getSession() method is tested.
   *
   */
  public void createdTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "createdTest");
    invoke();
  }

  /*
   * @testName: destroyedTest
   * 
   * @assertion_ids: Servlet:JAVADOC:455
   * 
   * @test_Strategy: Client calls a servlet that creates and the invalidates a
   * session. The listener should detect the changes and write a message to a
   * static log. The Servlet then reads the log file and verifies the result. As
   * a result of the test, the javax.servlet.http.Event.getSession() method is
   * tested.
   */
  public void destroyedTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "destroyedTest");
    invoke();
  }
}
