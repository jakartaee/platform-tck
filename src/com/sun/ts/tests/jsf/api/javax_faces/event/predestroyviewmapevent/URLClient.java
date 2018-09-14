/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * $Id:
 */

package com.sun.ts.tests.jsf.api.javax_faces.event.predestroyviewmapevent;

import java.io.PrintWriter;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_event_predestroyviewmapevent_web";

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
   * @testName: preDestroyViewMapEventCtorTest
   * @assertion_ids: JSF:JAVADOC:1867
   * @test_Strategy: Ensure: Instantiate a new PreDestroyViewMapEvent that
   *                 indicates the argument component is about to be rendered.
   * @since 2.0
   */
  public void preDestroyViewMapEventCtorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "componentSystemEventCtorTest");
    invoke();
  }

  /**
   * @testName: preDestroyViewMapEventIAETest
   * @assertion_ids: JSF:JAVADOC:1867
   * @test_Strategy: Ensure an IllegalArgumentException is thrown if a a null
   *                 component is passed to the beforeRenderEvent constructor.
   * @since 2.0
   */
  public void preDestroyViewMapEventIAETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "componentSystemEventIAETest");
    invoke();
  }

  /**
   * @testName: preDestroyViewMapEventGetComponentTest
   * @assertion_ids: JSF:JAVADOC:1867
   * @test_Strategy: Ensure getComponent() returns the component passed to the
   *                 preDestroyViewMapEvent constructor.
   */
  public void preDestroyViewMapEventGetComponentTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "componentSystemEventGetComponentTest");
    invoke();
  }

  /**
   * @testName: preDestroyViewMapEventIsApproiateListenerPostiveTest
   * @assertion_ids: JSF:JAVADOC:1867; JSF:JAVADOC:1876
   * @test_Strategy: Ensure isAppropriateListener() Returns true if and only if
   *                 the argument listener is an instance of
   *                 SystemEventListener.
   * 
   * @since 2.0
   */
  public void preDestroyViewMapEventIsApproiateListenerPostiveTest()
      throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "componentSystemEventIsApproiateListenerPostiveTest");
    invoke();
  }

  /**
   * @testName: preDestroyViewMapEventIsApproiateListenerNegativeTest
   * @assertion_ids: JSF:JAVADOC:1867; JSF:JAVADOC:1876
   * @test_Strategy: Ensure isAppropriateListener() Returns true if and only if
   *                 the argument listener is *NOT* an instance of
   *                 SystemEventListener.
   * 
   * @since 2.0
   */
  public void preDestroyViewMapEventIsApproiateListenerNegativeTest()
      throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "componentSystemEventIsApproiateListenerNegativeTest");
    invoke();
  }

  /**
   * @testName: preDestroyViewMapEventProcessListenerTest
   * @assertion_ids: JSF:JAVADOC:1867; JSF:JAVADOC:1877
   * @test_Strategy: Ensure calling ComponentSystemEvent.processListener() calls
   *                 through to ComponentSystemEventListener.processEvent().
   * 
   * @since 2.0
   */
  public void preDestroyViewMapEventProcessListenerTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "componentSystemEventProcessListenerTest");
    invoke();
  }

} // end of URLClient
