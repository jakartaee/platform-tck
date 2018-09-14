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

package com.sun.ts.tests.jsf.api.javax_faces.event.abortprocessingexception;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_event_abortprocessingexception_web";

  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out, true),
        new PrintWriter(System.err, true));
    s.exit();
  }

  public Status run(String[] args, PrintWriter out, PrintWriter err) {
    setContextRoot(CONTEXT_ROOT);
    setServletName(DEFAULT_SERVLET_NAME);
    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Test Declarations */

  /**
   * @testName: abortProcessingExceptionNoArgCtorTest
   * @assertion_ids: JSF:JAVADOC:1774
   * @test_Strategy: Verify AbortProcessingException no-arg ctor.
   */
  public void abortProcessingExceptionNoArgCtorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "abortProcessingExceptionNoArgCtorTest");
    invoke();
  }

  /**
   * @testName: abortProcessingExceptionCtor01Test
   * @assertion_ids: JSF:JAVADOC:1775
   * @test_Strategy: Verify an AbortProcessingException can be created passing a
   *                 message argument and that the message can be obtained via
   *                 getMessage().
   */
  public void abortProcessingExceptionCtor01Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "abortProcessingExceptionCtor01Test");
    invoke();
  }

  /**
   * @testName: abortProcessingExceptionCtor02Test
   * @assertion_ids: JSF:JAVADOC:1776
   * @test_Strategy: Verify an AbortProcessingException can be created passing a
   *                 message and root cause exception and that the message can
   *                 be obtained via getMessage() and the root cause can be
   *                 obtained by getCause().
   */
  public void abortProcessingExceptionCtor02Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "abortProcessingExceptionCtor02Test");
    invoke();
  }

  /**
   * @testName: abortProcessingExceptionCtor03Test
   * @assertion_ids: JSF:JAVADOC:1777
   * @test_Strategy: Verify an AbortProcessingException can be created passing a
   *                 root cause Exception and that the exception can be obtained
   *                 by calling getCause().
   */
  public void abortProcessingExceptionCtor03Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "abortProcessingExceptionCtor03Test");
    invoke();
  }

} // end of URLClient
