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
package com.sun.ts.tests.servlet.pluggability.api.javax_servlet.dispatchertype;

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
    setContextRoot("/servlet_plu_dispatchertype_web");
    setServletName("TestServlet");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Run test */

  /*
   * @testName: valuesTest
   *
   * @assertion_ids: Servlet:JAVADOC:654;
   *
   * @test_Strategy: 1. Create a servlet - TestServlet 2. Client send a request
   * to TestServlet 3. In TestServlet, call DispatcherType.values 4. Verify that
   * DispatcherType.values works properly.
   */
  public void valuesTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "valuesTest");
    invoke();
  }

  /*
   * @testName: valueOfTest
   *
   * @assertion_ids: Servlet:JAVADOC:653;
   *
   * @test_Strategy: 1. Create a servlet - TestServlet 2. Client send a request
   * to TestServlet 3. In TestServlet, call DispatcherType.valueOf 4. Verify
   * that DispatcherType.valueOf works properly.
   */
  public void valueOfTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "valueOfTest");
    invoke();
  }
  /*
   * @testName: valueOfNullTest
   *
   * @assertion_ids: Servlet:JAVADOC:653;
   *
   * @test_Strategy: 1. Create a servlet - TestServlet 2. Client send a request
   * to TestServlet 3. In TestServlet, call DispatcherType.valueOf(String name)
   * 4. Verify that DispatcherType.valueOf throws NullPointerException when name
   * is null
   */

  public void valueOfNullTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "valueOfNullTest");
    invoke();
  }
  /*
   * @testName: valueOfInvalidTest
   *
   * @assertion_ids: Servlet:JAVADOC:653;
   *
   * @test_Strategy: 1. Create a servlet - TestServlet 2. Client send a request
   * to TestServlet 3. In TestServlet, call DispatcherType.valueOf(String name)
   * 4. Verify that DispatcherType.valueOf throws IllegalArgumentException when
   * name is invalid Dispatcher type
   */

  public void valueOfInvalidTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "valueOfInvalidTest");
    invoke();
  }
}
