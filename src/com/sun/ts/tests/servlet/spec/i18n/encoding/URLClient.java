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

package com.sun.ts.tests.servlet.spec.i18n.encoding;

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
    setContextRoot("/servlet_spec_i18n_encoding_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: spec1Test
   * 
   * @assertion_ids: Servlet:JAVADOC:151; Servlet:JAVADOC:137;
   * Servlet:JAVADOC:164; Servlet:JAVADOC:139;
   *
   * @test_Strategy:
   */
  public void spec1Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "spec1Test");
    invoke();
  }

  /*
   * @testName: spec2Test
   * 
   * @assertion_ids: Servlet:JAVADOC:151; Servlet:JAVADOC:137;
   * Servlet:JAVADOC:164; Servlet:JAVADOC:139;
   * 
   * @test_Strategy:
   */
  public void spec2Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "spec2Test");
    invoke();
  }

  /*
   * @testName: spec3Test
   * 
   * @assertion_ids: Servlet:JAVADOC:151; Servlet:JAVADOC:137;
   * Servlet:JAVADOC:164; Servlet:JAVADOC:139;
   * 
   * @test_Strategy:
   */
  public void spec3Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "spec3Test");
    invoke();
  }

}
