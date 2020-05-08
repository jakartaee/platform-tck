/*
 * Copyright (c) 2017, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * $Id: $
 */
package com.sun.ts.tests.jsf.api.jakarta_faces.event.prerenderviewevent;

import java.io.PrintWriter;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_event_prerenderviewevent_web";

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
   * @testName: preRenderViewEventCtorTest
   * @assertion_ids: JSF:JAVADOC:1870
   * @test_Strategy: Ensure: Instantiate a new PreRenderComponentEvent that
   *                 indicates the argument component is about to be rendered.
   * 
   * @since 2.0
   */
  public void preRenderViewEventCtorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "componentSystemEventCtorTest");
    invoke();
  }

  /**
   * @testName: preRenderViewEventIAETest
   * @assertion_ids: JSF:JAVADOC:1870
   * @test_Strategy: Ensure an IllegalArgumentException is thrown if a a null
   *                 component is passed to the preRenderViewEvent constructor.
   * 
   * @since 2.0
   */
  public void preRenderViewEventIAETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "componentSystemEventIAETest");
    invoke();
  }

  /**
   * @testName: preRenderViewEventGetComponentTest
   * @assertion_ids: JSF:JAVADOC:1870
   * @test_Strategy: Ensure getComponent() returns the component passed to the
   *                 AfterpreRenderViewEvent constructor.
   * 
   * @since 2.0
   */
  public void preRenderViewEventGetComponentTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "componentSystemEventGetComponentTest");
    invoke();
  }

  /**
   * @testName: preRenderViewEventIsApproiateListenerPostiveTest
   * @assertion_ids: JSF:JAVADOC:1870; JSF:JAVADOC:1876
   * @test_Strategy: Ensure isAppropriateListener() Returns true if and only if
   *                 the argument listener is an instance of
   *                 SystemEventListener.
   * 
   * @since 2.0
   */
  public void preRenderViewEventIsApproiateListenerPostiveTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "componentSystemEventIsApproiateListenerPostiveTest");
    invoke();
  }

  /**
   * @testName: preRenderViewEventIsApproiateListenerNegativeTest
   * @assertion_ids: JSF:JAVADOC:1870; JSF:JAVADOC:1876
   * @test_Strategy: Ensure isAppropriateListener() Returns true if and only if
   *                 the argument listener is *NOT* an instance of
   *                 SystemEventListener.
   * 
   * @since 2.0
   */
  public void preRenderViewEventIsApproiateListenerNegativeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "componentSystemEventIsApproiateListenerNegativeTest");
    invoke();
  }

  /**
   * @testName: preRenderViewEventProcessListenerTest
   * @assertion_ids: JSF:JAVADOC:1870; JSF:JAVADOC:1877
   * @test_Strategy: Ensure calling ComponentSystemEvent.processListener() calls
   *                 through to ComponentSystemEventListener.processEvent().
   * 
   * @since 2.0
   */
  public void preRenderViewEventProcessListenerTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "componentSystemEventProcessListenerTest");
    invoke();
  }
} // end of URLClient
