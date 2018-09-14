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
package com.sun.ts.tests.jsf.api.javax_faces.view.viewdeclarationlangwrapper;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

import java.io.PrintWriter;

public class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_view_vdl_wrapper_web";

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
   * @testName: vdlWrapperGetComponentMetadataUSOETest
   * 
   * @assertion_ids: JSF:JAVADOC:2610
   * 
   * @test_Strategy: Validate that getComponentMetadata throws an
   *                 UnsupportedOperationException if in the JSP VDL.
   * 
   * @since 2.1
   */
  public void vdlWrapperGetComponentMetadataUSOETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "vdlWrapperGetComponentMetadataUSOETest");
    invoke();
  }

  /**
   * @testName: vdlWrapperGetScriptComponentResourceUSOETest
   * 
   * @assertion_ids: JSF:JAVADOC:2615
   * 
   * @test_Strategy: Validate that getScriptComponentResource throws an
   *                 UnsupportedOperationException if in the JSP VDL.
   * 
   * @since 2.1
   */
  public void vdlWrapperGetScriptComponentResourceUSOETest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "vdlWrapperGetScriptComponentResourceUSOETest");
    invoke();
  }

  /**
   * @testName: vdlWrapperGetComponentMetadataNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:2608
   * 
   * @test_Strategy: Validate that getComponentMetadata throws a
   *                 NullPointerException if in the Facelets VDL and either of
   *                 the args are null.
   * 
   * @since 2.1
   */
  public void vdlWrapperGetComponentMetadataNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "vdlWrapperGetComponentMetadataNPETest");
    invoke();
  }

  /**
   * @testName: vdlWrapperGetScriptComponentResourceNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:2613
   * 
   * @test_Strategy: Validate that getScriptComponentResource throws a
   *                 NullPointerException if in the Facelets VDL and either of
   *                 the args are null.
   * 
   * @since 2.1
   */
  public void vdlWrapperGetScriptComponentResourceNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "vdlWrapperGetScriptComponentResourceNPETest");
    invoke();
  }

  /**
   * @testName: vdlWrapperRenderViewNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:2623
   * 
   * @test_Strategy: Validate that renderView throws a NullPointerException if
   *                 any of the args are null.
   * 
   * @since 2.1
   */
  public void vdlWrapperRenderViewNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "vdlWrapperRenderViewNPETest");
    invoke();
  }

  /**
   * @testName: vdlWrapperRetargetMethodExpressionsNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:2629
   * 
   * @test_Strategy: Validate that retargetMethodExpressions throws a
   *                 NullPointerException if any of the args are null.
   * 
   * @since 2.1
   */
  public void vdlWrapperRetargetMethodExpressionsNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "vdlWrapperRetargetMethodExpressionsNPETest");
    invoke();
  }

  /**
   * @testName: vdlWrapperRetargetAttachedObjectsNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:2627
   * 
   * @test_Strategy: Validate that retargetAttachedObjects throws a
   *                 NullPointerException if any of the args are null.
   * 
   * @since 2.1
   */
  public void vdlWrapperRetargetAttachedObjectsNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "vdlWrapperRetargetAttachedObjectsNPETest");
    invoke();
  }

  /**
   * @testName: vdlWrapperRestoreViewNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:2625
   * 
   * @test_Strategy: Validate that restoreView throws a NullPointerException if
   *                 any of the args are null.
   * 
   * @since 2.1
   */
  public void vdlWrapperRestoreViewNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "vdlWrapperRestoreViewNPETest");
    invoke();
  }

  /**
   * @testName: vdlWrapperCreateViewNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:2606
   * 
   * @test_Strategy: Validate that createView throws a NullPointerException if
   *                 any of the args are null.
   * 
   * @since 2.1
   */
  public void vdlWrapperCreateViewNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "vdlWrapperCreateViewNPETest");
    invoke();
  }

  /**
   * @testName: vdlWrapperGetViewMetadataNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:2618
   * 
   * @test_Strategy: Validate that getViewMetadata throws a NullPointerException
   *                 if any of the args are null.
   * 
   * @since 2.1
   */
  public void vdlWrapperGetViewMetadataNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "vdlWrapperGetViewMetadataNPETest");
    invoke();
  }

}
