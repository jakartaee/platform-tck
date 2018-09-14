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

package com.sun.ts.tests.jsp.spec.core_syntax.directives.include;

import java.io.PrintWriter;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

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

    setGeneralURI("/jsp/spec/core_syntax/directives/include");
    setContextRoot("/jsp_coresyntx_direct_include_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /*
   * @testName: positiveIncludeCtxRelativeDirectiveTest
   * 
   * @assertion_ids: JSP:SPEC:69
   * 
   * @test_Strategy: Using an include directive, include content referenced by a
   * context-relative path. PENDING Merge with test below
   */

  public void positiveIncludeCtxRelativeDirectiveTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveIncludeCtxRelativeDirective");
    invoke();
  }

  /*
   * @testName: positiveIncludePageRelativeDirectiveTest
   * 
   * @assertion_ids: JSP:SPEC:69
   * 
   * @test_Strategy: Using an include directive, include content referenced by a
   * page-relative path.
   */

  public void positiveIncludePageRelativeDirectiveTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveIncludePageRelativeDirective");
    invoke();
  }
}
