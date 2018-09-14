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
package com.sun.ts.tests.jsf.api.javax_faces.application.protectedviewex;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_appl_protectedviewex_web";

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
   * @testName: protectViewExceptionTest
   * @assertion_ids: JSF:JAVADOC:2685; JSF:JAVADOC:2686; JSF:JAVADOC:2687;
   *                 JSF:JAVADOC:2688
   * @test_Strategy: Validated that we are able to call the following
   *                 constructors, and then call getViewId() & getMessage &
   *                 getCause to confirm the proper data had been set or not
   *                 set.
   * 
   *                 - ProtectedViewException(java.lang.Throwable rootCause) -
   *                 ProtectedViewException(java.lang.String message,
   *                 java.lang.Throwable cause) - public
   *                 ProtectedViewException(java.lang.String message) - public
   *                 ProtectedViewException()
   * 
   * @since 2.0
   */
  public void protectViewExceptionTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "protectViewExceptionTest");
    invoke();
  }

} // end of URLClient
