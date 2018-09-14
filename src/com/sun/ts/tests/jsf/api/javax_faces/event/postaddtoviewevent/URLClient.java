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
package com.sun.ts.tests.jsf.api.javax_faces.event.postaddtoviewevent;

import java.io.PrintWriter;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_event_postaddtoviewevent_web";

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
   * @testName: postAddToViewEventCtorTest
   * @assertion_ids: JSF:JAVADOC:1853; JSF:JAVADOC:1792
   * @test_Strategy: Ensure Instantiate a new postAddToViewEvent that indicates
   *                 the argument component was just added to the view.
   * 
   * @since 2.0
   */
  public void postAddToViewEventCtorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "componentSystemEventCtorTest");
    invoke();
  }

  /**
   * @testName: postAddToViewEventIAETest
   * @assertion_ids: JSF:JAVADOC:1853
   * @test_Strategy: Ensure an IllegalArgumentException is thrown if a a null
   *                 component is passed to the PostAddToViewEvent constructor.
   * 
   * @since 2.0
   */
  public void postAddToViewEventIAETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "componentSystemEventIAETest");
    invoke();
  }

  /**
   * @testName: postAddToViewEventGetComponentTest
   * @assertion_ids: JSF:JAVADOC:1853; JSF:JAVADOC:1793
   * @test_Strategy: Ensure getComponent() returns the component passed to the
   *                 PostAddToViewEvent constructor.
   * 
   * @since 2.0
   */
  public void postAddToViewEventGetComponentTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "componentSystemEventGetComponentTest");
    invoke();
  }

  /**
   * @testName: postAddToViewEventIsApproiateListenerPostiveTest
   * @assertion_ids: JSF:JAVADOC:1853; JSF:JAVADOC:1876
   * @test_Strategy: Ensure isAppropriateListener() Returns true if and only if
   *                 the argument listener is an instance of
   *                 SystemEventListener.
   * 
   * @since 2.0
   */
  public void postAddToViewEventIsApproiateListenerPostiveTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "postAddToViewEventIsApproiateListenerPostiveTest");
    invoke();
  }

  /**
   * @testName: postAddToViewEventIsApproiateListenerNegativeTest
   * @assertion_ids: JSF:JAVADOC:1853; JSF:JAVADOC:1852; JSF:JAVADOC:1876
   * @test_Strategy: Ensure isAppropriateListener() Returns true if and only if
   *                 the argument listener is *NOT* an instance of
   *                 SystemEventListener.
   * 
   * @since 2.0
   */
  public void postAddToViewEventIsApproiateListenerNegativeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "componentSystemEventIsApproiateListenerNegativeTest");
    invoke();
  }

  /**
   * @testName: postAddToViewEventProcessListenerTest
   * @assertion_ids: JSF:JAVADOC:1853; JSF:JAVADOC:1877; JSF:JAVADOC:1794
   * @test_Strategy: Ensure calling PostAddToViewEvent.processListener() calls
   *                 through to ComponentSystemEventListener.processEvent().
   * 
   * @since 2.0
   */
  public void postAddToViewEventProcessListenerTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "componentSystemEventProcessListenerTest");
    invoke();
  }
} // end of URLClient
