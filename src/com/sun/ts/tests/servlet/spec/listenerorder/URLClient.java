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
package com.sun.ts.tests.servlet.spec.listenerorder;

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
    setContextRoot("/servlet_spec_listenerorder_web");
    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */
  /*
   * @testName: requestListenerOrderTest
   *
   * @assertion_ids:
   *
   * @test_Strategy: 1. Define servlet TestServlet in web.xml; 2. Define three
   * RequestListeners in web.xml in the order of ServletRequestListener1,
   * ServletRequestListener2, ServletRequestListener3; 3. Send request to
   * /TestServlet, verify TestServlet is invoked 3. Also verify in all
   * RequestListeners that they are invoked in the order declared in web.xml
   */
  public void requestListenerOrderTest() throws Fault {
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    TEST_PROPS.setProperty(SEARCH_STRING, "TestServlet is invoked");
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/TestServlet" + " HTTP/1.1");
    invoke();
  }
}
