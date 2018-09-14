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
package com.sun.ts.tests.jsf.api.javax_faces.application.viewexpiredex;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_appl_viewexpiredex_web";

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
   * @testName: viewExpiredExceptionTest
   * @assertion_ids: JSF:JAVADOC:335; JSF:JAVADOC:336; JSF:JAVADOC:337;
   *                 JSF:JAVADOC:338; JSF:JAVADOC:339; JSF:JAVADOC:340;
   *                 JSF:JAVADOC:341
   * @test_Strategy: Validated that we are able to call the following
   *                 constructors, and then call getViewId(), getMessage, &
   *                 getCause to confirm the proper data had been set or not
   *                 set.
   * 
   *                 - ViewExpiredException() -
   *                 ViewExpiredException(java.lang.String viewId) -
   *                 ViewExpiredException(java.lang.String message,
   *                 java.lang.String viewId) -
   *                 ViewExpiredException(java.lang.Throwable cause,
   *                 java.lang.String viewId) -
   *                 ViewExpiredException(java.lang.String message,
   *                 java.lang.Throwable cause, java.lang.String viewId)
   * 
   * @since 2.0
   */
  public void viewExpiredExceptionTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "viewExpiredExceptionTest");
    invoke();
  }

} // end of URLClient
