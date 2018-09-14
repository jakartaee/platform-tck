/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsf.api.javax_faces.flow.flowhandler;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

import java.io.PrintWriter;

public class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_flowhandler_web";

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
   * @testName: facesFLowHandlerAddFlowNPETest
   * @assertion_ids: JSF:JAVADOC:2960
   * @test_Strategy: Verify that a NullPointer is thrown if any of the
   *                 parameters are null
   * 
   */
  public void facesFLowHandlerAddFlowNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesFLowHandlerAddFlowNPETest");
    invoke();
  }

  /**
   * @testName: facesFLowHandlerGetFlowNPETest
   * @assertion_ids: JSF:JAVADOC:2972
   * @test_Strategy: Verify that a NullPointer is thrown if any of the
   *                 parameters are null
   * 
   */
  public void facesFLowHandlerGetFlowNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesFLowHandlerGetFlowNPETest");
    invoke();
  }

  /**
   * @testName: facesFLowHandlerGetCurrentFlowNPETest
   * @assertion_ids: JSF:JAVADOC:2968
   * @test_Strategy: Verify that a NullPointer is thrown if any of the
   *                 parameters are null
   * 
   */
  public void facesFLowHandlerGetCurrentFlowNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesFLowHandlerGetCurrentFlowNPETest");
    invoke();
  }

  /**
   * @testName: facesFLowHandlerTransitionNPETest
   * @assertion_ids: JSF:JAVADOC:2980
   * @test_Strategy: Verify that a NullPointer is thrown if FacesContext or
   *                 toViewID is null.
   * 
   */
  public void facesFLowHandlerTransitionNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesFLowHandlerTransitionNPETest");
    invoke();
  }

  /**
   * @testName: facesFLowHandlerIsActiveNPETest
   * @assertion_ids: JSF:JAVADOC:2976
   * @test_Strategy: Verify that a NullPointer is thrown if any of the
   *                 parameters are null
   * 
   */
  public void facesFLowHandlerIsActiveNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesFLowHandlerIsActiveNPETest");
    invoke();
  }

  /**
   * @testName: facesFLowHandlerAddFlowIAETest
   * @assertion_ids: JSF:JAVADOC:2962; JSF:JAVADOC:2963
   * @test_Strategy: Verify that an IllegalArgumentException is thrown if the
   *                 {@code id} of the flow to add is {@code null} or the empty
   *                 string.
   * 
   *                 Verify that an IllegalArgumentException if the
   *                 {@code definingDocumentId} of the {@code toAdd} is
   *                 {@code null}.
   * 
   */
  public void facesFLowHandlerAddFlowIAETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "facesFLowHandlerGetFlowNPETest");
    invoke();
  }

  /**
   * @testName: facesFLowHandlerClientWindowTransitionNPETest
   * @assertion_ids: JSF:JAVADOC:2965
   * @test_Strategy: Verify that a NullPointer is thrown if context is null.
   * 
   */
  public void facesFLowHandlerClientWindowTransitionNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "facesFLowHandlerClientWindowTransitionNPETest");
    invoke();
  }

  /**
   * @testName: facesFLowHandlerGetLastDisplayedViewIdNPETest
   * @assertion_ids: JSF:JAVADOC:2974
   * @test_Strategy: Verify that a NullPointer is thrown if context is null.
   * 
   */
  public void facesFLowHandlerGetLastDisplayedViewIdNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "facesFLowHandlerGetLastDisplayedViewIdNPETest");
    invoke();
  }

}
