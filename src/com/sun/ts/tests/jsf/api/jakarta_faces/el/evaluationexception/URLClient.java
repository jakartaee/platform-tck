/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsf.api.jakarta_faces.el.evaluationexception;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

import java.io.PrintWriter;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_el_evalexc_web";

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

  /* Test Declarations */

  /**
   * @testName: evalExceptionNoArgCtorTest
   * @assertion_ids: JSF:JAVADOC:1702
   * @test_Strategy: Validate no-arg ctor of EvaluationException.
   */
  public void evalExceptionNoArgCtorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "noArgCtorTest");
    invoke();
  }

  /**
   * @testName: evalExceptionMessageArgCtorTest
   * @assertion_ids: JSF:JAVADOC:1703
   * @test_Strategy: Validate message ctor of EvaluationException and that
   *                 getMessage() returns the expected result.
   */
  public void evalExceptionMessageArgCtorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "messageArgCtorTest");
    invoke();
  }

  /**
   * @testName: evalExceptionRootCauseArgCtorTest
   * @assertion_ids: JSF:JAVADOC:1704
   * @test_Strategy: Validate the Throwable ctor of EvaluationException and that
   *                 getCause() returns the expected result.
   */
  public void evalExceptionRootCauseArgCtorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "rootCauseArgCtorTest");
    invoke();
  }

  /**
   * @testName: evalExceptionRootCauseMessageArgsCtorTest
   * @assertion_ids: JSF:JAVADOC:1705
   * @test_Strategy: Validate the Throwable and message ctor of
   *                 EvaluationException and that both getMessage() and
   *                 getCause() return the expected results.
   */
  public void evalExceptionRootCauseMessageArgsCtorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "rootCauseMessageArgsCtorTest");
    invoke();
  }

} // end of URLClient
