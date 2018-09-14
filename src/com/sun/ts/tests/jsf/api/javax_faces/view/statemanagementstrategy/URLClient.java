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
package com.sun.ts.tests.jsf.api.javax_faces.view.statemanagementstrategy;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

import java.io.PrintWriter;

public class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_view_statemgmt_web";

  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  public Status run(String args[], PrintWriter out, PrintWriter err) {
    setContextRoot(CONTEXT_ROOT);
    setServletName(DEFAULT_SERVLET_NAME);
    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  // ------------------------ test methods

  /**
   * @testName: stateMgmtStrategyNonNullTest
   * @assertion_ids: JSF:JAVADOC:2243
   * @test_Strategy: Validate that StateManagementStrategy is non-null for views
   *                 authored in Facelets for JSF 2, this specification only
   *                 apply to Facelets for JSF 2.
   * 
   * @since 2.2
   */
  public void stateMgmtStrategyNonNullTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "stateMgmtStratNullForJSPTest");
    invoke();
  }

  /**
   * @testName: stateMgmtStratNullForJSPTest
   * @assertion_ids: JSF:JAVADOC:2243
   * @test_Strategy: Validate that StateManagementStrategy is null for JSP
   *                 views.
   * 
   * @since 2.2
   */
  public void stateMgmtStratNullForJSPTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "stateMgmtStratNullForJSPTest");
    invoke();
  }

}
