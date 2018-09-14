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
package com.sun.ts.tests.jsf.api.javax_faces.view.viewdeclarationlang;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_view_vdl_web";

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

  /* Run Tests */

  // ------------------------------------------------------------- test
  // methods

  /**
   * @testName: vdlGetComponentMetadataUSOETest
   * @assertion_ids: JSF:JAVADOC:2251
   * @test_Strategy: Validate that getComponentMetadata throws an
   *                 UnsupportedOperationException if in the JSP VDL.
   * 
   * @since 2.1
   */
  public void vdlGetComponentMetadataUSOETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "vdlGetComponentMetadataUSOETest");
    invoke();
  }

  /**
   * @testName: vdlGetScriptComponentResourceUSOETest
   * @assertion_ids: JSF:JAVADOC:2255
   * @test_Strategy: Validate that getScriptComponentResource throws an
   *                 UnsupportedOperationException if in the JSP VDL.
   * 
   * @since 2.1
   */
  public void vdlGetScriptComponentResourceUSOETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "vdlGetScriptComponentResourceUSOETest");
    invoke();
  }

  /**
   * @testName: vdlGetComponentMetadataNPETest
   * @assertion_ids: JSF:JAVADOC:2249
   * @test_Strategy: Validate that getComponentMetadata throws a
   *                 NullPointerException if in the Facelets VDL and either of
   *                 the args are null.
   * 
   * @since 2.1
   */
  public void vdlGetComponentMetadataNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "vdlGetComponentMetadataNPETest");
    invoke();
  }

  /**
   * @testName: vdlGetScriptComponentResourceNPETest
   * @assertion_ids: JSF:JAVADOC:2253
   * @test_Strategy: Validate that getScriptComponentResource throws a
   *                 NullPointerException if in the Facelets VDL and either of
   *                 the args are null.
   * 
   * @since 2.1
   */
  public void vdlGetScriptComponentResourceNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "vdlGetScriptComponentResourceNPETest");
    invoke();
  }

  /**
   * @testName: vdlRenderViewNPETest
   * @assertion_ids: JSF:JAVADOC:2262
   * @test_Strategy: Validate that renderView throws a NullPointerException if
   *                 any of the args are null.
   * 
   * @since 2.1
   */
  public void vdlRenderViewNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "vdlRenderViewNPETest");
    invoke();
  }

  /**
   * @testName: vdlRetargetMethodExpressionsNPETest
   * @assertion_ids: JSF:JAVADOC:2268
   * @test_Strategy: Validate that retargetMethodExpressions throws a
   *                 NullPointerException if any of the args are null.
   * 
   * @since 2.1
   */
  public void vdlRetargetMethodExpressionsNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "vdlRetargetMethodExpressionsNPETest");
    invoke();
  }

  /**
   * @testName: vdlRetargetAttachedObjectsNPETest
   * @assertion_ids: JSF:JAVADOC:2266
   * @test_Strategy: Validate that retargetAttachedObjects throws a
   *                 NullPointerException if any of the args are null.
   * 
   * @since 2.1
   */
  public void vdlRetargetAttachedObjectsNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "vdlRetargetAttachedObjectsNPETest");
    invoke();
  }

  /**
   * @testName: vdlRestoreViewNPETest
   * @assertion_ids: JSF:JAVADOC:2264
   * @test_Strategy: Validate that restoreView throws a NullPointerException if
   *                 any of the args are null.
   * 
   * @since 2.1
   */
  public void vdlRestoreViewNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "vdlRestoreViewNPETest");
    invoke();
  }

  /**
   * @testName: vdlCreateViewNPETest
   * @assertion_ids: JSF:JAVADOC:2247
   * @test_Strategy: Validate that createView throws a NullPointerException if
   *                 any of the args are null.
   * 
   * @since 2.1
   */
  public void vdlCreateViewNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "vdlCreateViewNPETest");
    invoke();
  }

  /**
   * @testName: vdlGetViewMetadataNPETest
   * @assertion_ids: JSF:JAVADOC:2258
   * @test_Strategy: Validate that getViewMetadata throws a NullPointerException
   *                 if any of the args are null.
   * 
   * @since 2.1
   */
  public void vdlGetViewMetadataNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "vdlGetViewMetadataNPETest");
    invoke();
  }

  /**
   * @testName: vdlGetViewMetaDataTest
   * @assertion_ids: JSF:JAVADOC:2246; JSF:JAVADOC:2244
   * @test_Strategy: Validate that getViewMetaData() contains the correct
   *                 information.
   * 
   * @since 2.0
   */
  public void vdlGetViewMetaDataTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "vdlGetViewMetaDataTest");
    invoke();
  }

  /**
   * @testName: vdlGetIdTest
   * @assertion_ids: JSF:JAVADOC:2246; JSF:JAVADOC:2601
   * @test_Strategy: Validate that getId() contains the correct information.
   * 
   * @since 2.0
   */
  public void vdlGetIdTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "vdlGetIdTest");
    invoke();
  }

  /**
   * @testName: vdlViewExistsTest
   * @assertion_ids: JSF:JAVADOC:2246; JSF:JAVADOC:2602
   * @test_Strategy: Validate that viewExists() returns the correct boolean
   *                 value when a view exists or not.
   * 
   * @since 2.0
   */
  public void vdlViewExistsTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "vdlViewExistsTest");
    invoke();
  }
}
