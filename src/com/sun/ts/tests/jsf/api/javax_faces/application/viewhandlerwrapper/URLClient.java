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

package com.sun.ts.tests.jsf.api.javax_faces.application.viewhandlerwrapper;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_appl_viewhandlerwrap_web";

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
   * @testName: viewHandlerCalculateLocaleTest
   * @assertion_ids: JSF:JAVADOC:373
   * @test_Strategy: Verify the behavior of calculateLocale() under the
   *                 following conditions: 1. Configured Locales: ja Default
   *                 Locale: null Client preferred locales: en Expected result:
   *                 value of Locale.getDefault()
   * 
   *                 2. Configured Locales: en Default Locale: en Client
   *                 preferred locales: de, fr Expected result: en
   * 
   *                 3. Configured Locales: en, fr, en_US Default Locale: en
   *                 Client preferred locales: ja, en-GB, en-US, en-CA, fr
   *                 Expected result: en
   * 
   *                 4. Configured Locales: fr_CA, sv, en Default Local: de
   *                 Client preferred locales: fr, sv Expected result: sv
   * 
   *                 5. Configured Locales: en, fr_CA Default Locale: fr_CA
   *                 Client preferred locales: en_GB, fr_CA Expected result: en
   * @since 1.2
   */
  public void viewHandlerCalculateLocaleTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "viewHandlerCalculateLocaleTest");
    invoke();
  }

  /**
   * @testName: viewHandlerCreateViewTest
   * @assertion_ids: JSF:JAVADOC:375
   * @test_Strategy: Verify a new UIViewRoot instance can be obtained by calling
   *                 createView(). Additionally verify if a UIViewRoot already
   *                 exists in the FacesContext, the locale and renderkit ID is
   *                 copied from the old instance to the new instance.
   * @since 1.2
   */
  public void viewHandlerCreateViewTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "viewHandlerCreateViewTest");
    invoke();
  }

  /**
   * @testName: viewHandlerRenderViewNPETest
   * @assertion_ids: JSF:JAVADOC:385
   * @test_Strategy: Verify a NullPointerException is thrown if either argument
   *                 to renderView() is null.
   * @since 1.2
   */
  public void viewHandlerRenderViewNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "viewHandlerRenderViewNPETest");
    invoke();
  }

  /**
   * @testName: viewHandlerCalculateLocaleNPETest
   * @assertion_ids: JSF:JAVADOC:344
   * @test_Strategy: Verify a NullPointerException is thrown if a null argument
   *                 is passed to calculateLocale.
   * @since 1.2
   */
  public void viewHandlerCalculateLocaleNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "viewHandlerCalculateLocaleNPETest");
    invoke();
  }

  /**
   * @testName: viewHandlerCreateViewNPETest
   * @assertion_ids: JSF:JAVADOC:348
   * @test_Strategy: Verify a NullPointerException is thrown if a null value for
   *                 the FacesContext argument is passed.
   * @since 1.2
   */
  public void viewHandlerCreateViewNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "viewHandlerCreateViewNPETest");
    invoke();
  }

} // end of URLClient
