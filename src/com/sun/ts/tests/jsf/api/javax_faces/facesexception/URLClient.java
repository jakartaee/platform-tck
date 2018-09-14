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

package com.sun.ts.tests.jsf.api.javax_faces.facesexception;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

import java.io.PrintWriter;

public class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_facesexception_web";

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
   *
   */

  /* Run test */

  /*
   * @testName: facesExceptionCtor1Test
   * 
   * @assertion_ids: JSF:JAVADOC:1
   * 
   * @test_Strategy: Construct a new exception with no detail message or root
   * cause.
   *
   */
  public void facesExceptionCtor1Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesExceptionCtor1Test");
    invoke();
  }

  /*
   * @testName: facesExceptionCtor2Test
   * 
   * @assertion_ids: JSF:JAVADOC:2
   * 
   * @test_Strategy: Construct a new exception with the specified detail message
   * and no root cause.
   *
   */
  public void facesExceptionCtor2Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesExceptionCtor2Test");
    invoke();
  }

  /*
   * @testName: facesExceptionCtor3Test
   * 
   * @assertion_ids: JSF:JAVADOC:3
   * 
   * @test_Strategy: Construct a new exception with the specified root cause.
   * The detail message will be set to (cause == null ? null : cause.toString()
   *
   */
  public void facesExceptionCtor3Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesExceptionCtor3Test");
    invoke();
  }

  /*
   * @testName: facesExceptionCtor4Test
   * 
   * @assertion_ids: JSF:JAVADOC:4
   * 
   * @test_Strategy: Construct a new exception with the specified detail message
   * and root cause.
   *
   */
  public void facesExceptionCtor4Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesExceptionCtor4Test");
    invoke();
  }

  /*
   * @testName: facesExceptionGetCauseTest
   * 
   * @assertion_ids: JSF:JAVADOC:5
   * 
   * @test_Strategy: Return the cause of this exception, or null if the cause is
   * nonexistent or unknown.
   *
   */
  public void facesExceptionGetCauseTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesExceptionGetCauseTest");
    invoke();
  }

}
