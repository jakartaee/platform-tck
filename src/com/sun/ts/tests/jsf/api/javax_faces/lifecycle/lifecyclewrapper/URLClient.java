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

package com.sun.ts.tests.jsf.api.javax_faces.lifecycle.lifecyclewrapper;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_lifecycle_lifecyclewppr_web";

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
   * @testName: lifecycleWpprAddGetRemovePhaseListenersTest
   * @assertion_ids: JSF:JAVADOC:2768; JSF:JAVADOC:2774; JSF:JAVADOC:2777;
   *                 JSF:JAVADOC:2775
   * @test_Strategy: Verify PhaseListeners can be added, retrieved, and removed.
   */
  public void lifecycleWpprAddGetRemovePhaseListenersTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "lifecycleWpprAddGetRemovePhaseListenersTest");
    invoke();
  }

  /**
   * @testName: lifecycleWpprAddPhaseListenerNPETest
   * @assertion_ids: JSF:JAVADOC:2775;JSF:JAVADOC:2769
   * @test_Strategy: Verify NPE is thrown if attempting to add a null
   *                 PhaseListener to the Lifecycle.
   */
  public void lifecycleWpprAddPhaseListenerNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "lifecycleWpprAddPhaseListenerNPETest");
    invoke();
  }

  /**
   * @testName: lifecycleWpprRemovePhaseListenerNPETest
   * @assertion_ids: JSF:JAVADOC:2775; JSF:JAVADOC:2778
   * @test_Strategy: Verify NPE is thrown if attempting to remove a
   *                 PhaseListener using a null argument.
   */
  public void lifecycleWpprRemovePhaseListenerNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "lifecycleWpprRemovePhaseListenerNPETest");
    invoke();
  }

  /**
   * @testName: lifecycleWpprRenderNPETest
   * @assertion_ids: JSF:JAVADOC:2775; JSF:JAVADOC:2781
   * @test_Strategy: Verify NPE is thrown if attempting to render a response
   *                 when context is null.
   */
  public void lifecycleWpprRenderNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "lifecycleWpprRenderNPETest");
    invoke();
  }
} // end of URLClient
