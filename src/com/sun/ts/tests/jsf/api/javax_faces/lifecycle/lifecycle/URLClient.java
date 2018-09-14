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

package com.sun.ts.tests.jsf.api.javax_faces.lifecycle.lifecycle;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_lifecycle_life_web";

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
   * @testName: lifecycleAddGetRemovePhaseListenersTest
   * @assertion_ids: JSF:JAVADOC:1898; JSF:JAVADOC:1892; JSF:JAVADOC:1897;
   *                 JSF:JAVADOC:1899
   * @test_Strategy: Verify PhaseListeners can be added, retrieved, and removed.
   */
  public void lifecycleAddGetRemovePhaseListenersTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "lifecycleAddGetRemovePhaseListenersTest");
    invoke();
  }

  /**
   * @testName: lifecycleAddPhaseListenerNPETest
   * @assertion_ids: JSF:JAVADOC:1898; JSF:JAVADOC:1893
   * @test_Strategy: Verify NPE is thrown if attempting to add a null
   *                 PhaseListener to the Lifecycle.
   */
  public void lifecycleAddPhaseListenerNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "lifecycleAddPhaseListenerNPETest");
    invoke();
  }

  /**
   * @testName: lifecycleRemovePhaseListenerNPETest
   * @assertion_ids: JSF:JAVADOC:1898; JSF:JAVADOC:1900
   * @test_Strategy: Verify NPE is thrown if attempting to remove a
   *                 PhaseListener using a null argument.
   */
  public void lifecycleRemovePhaseListenerNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "lifecycleRemovePhaseListenerNPETest");
    invoke();
  }

  /**
   * @testName: lifecycleExecuteNPETest
   * @assertion_ids: JSF:JAVADOC:1898; JSF:JAVADOC:1896
   * @test_Strategy: Verify NPE is thrown if FacesContext arg is null.
   */
  public void lifecycleExecuteNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "lifecycleExecuteNPETest");
    invoke();
  }

  /**
   * @testName: lifecycleRenderNPETest
   * @assertion_ids: JSF:JAVADOC:1898; JSF:JAVADOC:1903
   * @test_Strategy: Verify NPE is thrown if FacesContext arg is null.
   */
  public void lifecycleRenderNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "lifecycleRenderNPETest");
    invoke();
  }

} // end of URLClient
