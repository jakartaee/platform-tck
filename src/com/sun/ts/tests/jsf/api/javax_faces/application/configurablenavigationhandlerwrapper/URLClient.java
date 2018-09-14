/*
 * Copyright (c) 2011, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jsf.api.javax_faces.application.configurablenavigationhandlerwrapper;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_appl_confignavihandlerwpr_web";

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

  /**
   * @testName: configNavihandlerwprGetNavigationCaseTest
   * @assertion_ids: JSF:JAVADOC:2515; JSF:JAVADOC:2519; JSF:JAVADOC:2516
   * @test_Strategy: Validate the return of a NavigationCase by testing the
   *                 'fromAction' tag.
   * 
   * @since 2.2
   */
  public void configNavihandlerwprGetNavigationCaseTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "configNavihandlerwprGetNavigationCaseTest");
    invoke();
  }

  /**
   * @testName: configNavihandlerwprGetNavigationCaseNPETest
   * @assertion_ids: JSF:JAVADOC:2515; JSF:JAVADOC:2519; JSF:JAVADOC:2517
   * @test_Strategy: Validate that we get a NullPointerException when
   *                 FacesContext is null.
   * 
   * @since 2.2
   */
  public void configNavihandlerwprGetNavigationCaseNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "configNavihandlerwprGetNavigationCaseNPETest");
    invoke();
  }

  /**
   * @testName: configNavihandlerwprGetNavigationCasesTest
   * @assertion_ids: JSF:JAVADOC:2515; JSF:JAVADOC:2519; JSF:JAVADOC:2518
   * @test_Strategy: Validate that we get a Map of NavigationCases.
   */
  public void configNavihandlerwprGetNavigationCasesTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "configNavihandlerwprGetNavigationCasesTest");
    invoke();
  }

} // end of URLClient
