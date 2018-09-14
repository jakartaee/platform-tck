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

package com.sun.ts.tests.jsf.api.javax_faces.application.viewhandler;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

import java.io.PrintWriter;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_appl_viewhandler_web";

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
   * 
   * @assertion_ids: JSF:JAVADOC:368; JSF:JAVADOC:343
   * 
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
   */
  public void viewHandlerCalculateLocaleTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "viewHandlerCalculateLocaleTest");
    invoke();
  }

  /**
   * @testName: viewHandlerCreateViewTest
   * @assertion_ids: JSF:JAVADOC:368; JSF:JAVADOC:347
   * @test_Strategy: Verify a new UIViewRoot instance can be obtained by calling
   *                 createView(). Additionally verify if a UIViewRoot already
   *                 exists in the FacesContext, the locale and renderkit ID is
   *                 copied from the old instance to the new instance.
   */
  public void viewHandlerCreateViewTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "viewHandlerCreateViewTest");
    invoke();
  }

  /**
   * @testName: viewHandlerRenderViewNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:368; JSF:JAVADOC:364
   * 
   * @test_Strategy: Verify a NullPointerException is thrown if either argument
   *                 to renderView() is null.
   */
  public void viewHandlerRenderViewNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "viewHandlerRenderViewNPETest");
    invoke();
  }

  /**
   * @testName: viewHandlerCalculateLocaleNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:368; JSF:JAVADOC:344
   * 
   * @test_Strategy: Verify a NullPointerException is thrown if a null argument
   *                 is passed to calculateLocale.
   */
  public void viewHandlerCalculateLocaleNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "viewHandlerCalculateLocaleNPETest");
    invoke();
  }

  /**
   * @testName: viewHandlerCreateViewNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:368; JSF:JAVADOC:348
   * 
   * @test_Strategy: Verify a NullPointerException is thrown if a null value for
   *                 the FacesContext argument is passed.
   */
  public void viewHandlerCreateViewNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "viewHandlerCreateViewNPETest");
    invoke();
  }

  /**
   * @testName: viewHandlerCalculateRenderKitIdNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:368; JSF:JAVADOC:346
   * 
   * @test_Strategy: Validate that a NullPointerException is thrown when
   *                 FacesContext is null.
   */
  public void viewHandlerCalculateRenderKitIdNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "viewHandlerCalculateRenderKitIdNPETest");
    invoke();
  }

  /**
   * @testName: viewHandlerGetResourceURLNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:368; JSF:JAVADOC:357
   * 
   * @test_Strategy: Validate that a NullPointerException is thrown when either
   *                 arg is null. (FacesContext, String)
   */
  public void viewHandlerGetResourceURLNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "viewHandlerGetResourceURLNPETest");
    invoke();
  }

  /**
   * @testName: viewHandlerRestoreViewNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:368; JSF:JAVADOC:366
   * 
   * @test_Strategy: Validate that a NullPointerException is thrown when
   *                 FacesContext is null.
   */
  public void viewHandlerRestoreViewNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "viewHandlerRestoreViewNPETest");
    invoke();
  }

  /**
   * @testName: viewHandlerWriteStateNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:368; JSF:JAVADOC:371
   * 
   * @test_Strategy: Validate that a NullPointerException is thrown when
   *                 FacesContext is null.
   */
  public void viewHandlerWriteStateNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "viewHandlerWriteStateNPETest");
    invoke();
  }

  /**
   * @testName: viewHandlerGetActionURLNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:368; JSF:JAVADOC:352
   * 
   * @test_Strategy: Verify a NullPointerException is thrown if either argument
   *                 to renderView() is null.
   */
  public void viewHandlerGetActionURLNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "viewHandlerGetActionURLNPETest");
    invoke();
  }

  /**
   * @testName: viewHandlerCalculateCharEncodingHDRTest
   * 
   * @assertion_ids: JSF:JAVADOC:368; JSF:JAVADOC:342
   * 
   * @test_Strategy: Validate If the Content-Type request header has a charset
   *                 parameter, that it is extracted and returned as the
   *                 encoding.
   * 
   * @since: 1.2
   */
  public void viewHandlerCalculateCharEncodingHDRTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "viewHandlerCalculateCharEncodingHDRTest");
    super.setMyCharacterSet("charset=Thomas");
    invoke();
  }

  /**
   * @testName: viewHandlerCalculateCharEncodingNULLTest
   * 
   * @assertion_ids: JSF:JAVADOC:368; JSF:JAVADOC:342
   * 
   * @test_Strategy: Validate that null is returned if charset is not set via
   *                 the request header or through lookup on external context.
   * 
   * @since: 1.2
   */
  public void viewHandlerCalculateCharEncodingNULLTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "viewHandlerCalculateCharEncodingNULLTest");
    invoke();
  }

  /**
   * @testName: viewHandlerCalculateCharEncodingEXTTest
   * 
   * @assertion_ids: JSF:JAVADOC:368; JSF:JAVADOC:342
   * 
   * @test_Strategy: Validate check for the existence of a session by calling
   *                 ExternalContext.getSession(boolean) passing false as the
   *                 argument. If that method returns true, get the session Map
   *                 by calling ExternalContext.getSessionMap() and look for a
   *                 value under the key given by the value of the symbolic
   *                 constant CHARACTER_ENCODING_KEY. If present, return the
   *                 value, converted to String.
   * 
   * @since: 1.2
   */
  public void viewHandlerCalculateCharEncodingEXTTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "viewHandlerCalculateCharEncodingEXTTest");
    invoke();
  }

  /**
   * @testName: viewHandlerCalculateRenderKitIdTest
   * 
   * @assertion_ids: JSF:JAVADOC:368; JSF:JAVADOC:345
   * 
   * @test_Strategy: Validate that we get a String returned for a valid
   *                 RenderKit ID and that we never receive 'null'.
   * 
   * @since: 1.2
   */
  public void viewHandlerCalculateRenderKitIdTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "viewHandlerCalculateRenderKitIdTest");
    invoke();
  }

  /**
   * @testName: viewHandlerAddGetRemoveProtectedViewsTest
   * 
   * @assertion_ids: JSF:JAVADOC:368; JSF:JAVADOC:2689; JSF:JAVADOC:2690;
   *                 JSF:JAVADOC:2691
   * 
   * @test_Strategy: Validate that a protected view can be set and that the
   *                 given protected view is in the Set that is returned from
   *                 getProtectedViewsUnmodifiable().
   * 
   * @since: 2.2
   */
  public void viewHandlerAddGetRemoveProtectedViewsTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "viewHandlerAddGetRemoveProtectedViewsTest");
    invoke();
  }

  /**
   * @testName: viewHandlerDeriveLogicalViewIDTest
   * 
   * @assertion_ids: JSF:JAVADOC:368; JSF:JAVADOC:2533
   * 
   * @test_Strategy: Validate that we Derive and return the viewId from the
   *                 current request. (with no physical view)
   * 
   * @since: 2.2
   */
  public void viewHandlerDeriveLogicalViewIDTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "viewHandlerDeriveLogicalViewIDTest");
    invoke();
  }

} // end of URLClient
