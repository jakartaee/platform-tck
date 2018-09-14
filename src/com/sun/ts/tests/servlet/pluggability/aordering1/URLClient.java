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
package com.sun.ts.tests.servlet.pluggability.aordering1;

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
    setContextRoot("/servlet_spec_aordering1_web");
    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */
  /*
   * @testName: absoluteOrderingTest
   *
   * @assertion_ids: Servlet:SPEC:230; Servlet:SPEC:231; Servlet:SPEC:232;
   * Servlet:SPEC:233;
   *
   * @test_Strategy: 1. Define six RequestListeners and one servlet, in web.xml
   * and six web-fragment.xml: web.xml - define and package TestServlet1,
   * RequestListener, with <absolute-ordering> of all fragments: fragment1 -
   * define and package TestServlet1, RequestListener1, fragment2 - define and
   * package RequestListener2 fragment3 - define and package RequestListener3,
   * fragment4 - define and package RequestListener4, fragment5 - define and
   * package RequestListener5, 2. Also add a fragment that is not present in
   * <absolute-ordering>; 3. Send request to TestServlet1 4. Verify that web.xml
   * is always processed first; 5. Verify that <absolute-ordering> works
   * accordingly: 6. Verify that not present fragment in <absolute-ordering> is
   * ignored.
   */
  public void absoluteOrderingTest() throws Fault {
    TEST_PROPS.setProperty(SEARCH_STRING,
        "msg1=first|msg2=second|" + "RequestListener|RequestListener1|"
            + "RequestListener2|RequestListener3|"
            + "RequestListener4|RequestListener5");
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/TestServlet1" + " HTTP/1.1");
    invoke();
  }
}
