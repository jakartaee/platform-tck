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

package com.sun.ts.tests.servlet.api.javax_servlet_http.httpsessionbindinglistener;

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
    setContextRoot("/servlet_jsh_httpsessionbindinglistener_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: unBoundTest
   * 
   * @assertion_ids:
   * Servlet:JAVADOC:459;Servlet:JAVADOC:485;Servlet:JAVADOC:486;Servlet:JAVADOC
   * :491
   * 
   * @test_Strategy: Client calls a servlet that sets/sets/removes an attribute
   * from the session. That attribute happens to be a Binding listener. The
   * Listeners valueBound/valueUnbound methods should be called and messages
   * written to a static log. The servlet then reads the log and verifies the
   * result.
   */

  public void unBoundTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "unBoundTest");
    invoke();
  }

  /*
   * @testName: boundTest
   * 
   * @assertion_ids: Servlet:JAVADOC:458;Servlet:JAVADOC:485;Servlet:JAVADOC:486
   * 
   * @test_Strategy: Client calls a servlet that sets an attribute to the
   * session. That attribute happens to be a Binding listener. The Listeners
   * valueBound/valueUnbound methods should be called and messages written to a
   * static log. The servlet then reads the log and verifies the result.
   */

  public void boundTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "boundTest");
    invoke();
  }
}
