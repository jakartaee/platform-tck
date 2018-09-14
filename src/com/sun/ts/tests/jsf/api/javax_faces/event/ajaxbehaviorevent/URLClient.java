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
package com.sun.ts.tests.jsf.api.javax_faces.event.ajaxbehaviorevent;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_event_ajaxbehaviorevent_web";

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

  // ------------------------------------------------------------
  // BahaviorEvent
  /**
   * @testName: behaviorEventCtorTest
   * @assertion_ids: JSF:JAVADOC:1784; JSF:JAVADOC:1790
   * @test_Strategy: Ensure an AjaxBehaviorEvent can be created by passing a
   *                 valid UIComponent.
   */
  public void behaviorEventCtorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "behaviorEventCtorTest");
    invoke();
  }

  /**
   * @testName: behaviorEventIAETest
   * @assertion_ids: JSF:JAVADOC:1784
   * @test_Strategy: Ensure an IllegalArgumentException is thrown if a null is
   *                 passed for either the component or behavior args to the
   *                 AjaxBehaviorEvent constructor.
   */
  public void behaviorEventIAETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "behaviorEventIAETest");
    invoke();
  }

  /**
   * @testName: behaviorEventGetBehaviorTest
   * @assertion_ids: JSF:JAVADOC:1784; JSF:JAVADOC:1791
   * @test_Strategy: Ensure getComponent() returns the component passed to the
   *                 AjaxBehaviorEvent constructor.
   */
  public void behaviorEventGetBehaviorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "behaviorEventGetBehaviorTest");
    invoke();
  }

  /**
   * @testName: behaviorEventIsApproiateListenerPosTest
   * @assertion_ids: JSF:JAVADOC:1785; JSF:JAVADOC:1812
   * @test_Strategy: Ensure isAppropriateListener() returns true if an
   *                 BehaviorListener is passed.
   */
  public void behaviorEventIsApproiateListenerPosTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "behaviorEventIsApproiateListenerPosTest");
    invoke();
  }

  /**
   * @testName: behaviorEventIsApproiateListenerNegTest
   * @assertion_ids: JSF:JAVADOC:1785
   * @test_Strategy: Ensure isAppropriateListener() returns false if a non-
   *                 BehaviorListener is passed.
   */
  public void behaviorEventIsApproiateListenerNegTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "behaviorEventIsApproiateListenerNegTest");
    invoke();
  }

  /**
   * @testName: behaviorEventProcessListenerTest
   * @assertion_ids: JSF:JAVADOC:1788; JSF:JAVADOC:1813; JSF:JAVADOC:1786
   * @test_Strategy: Ensure calling behaviorEvent.processListener() calls
   *                 through to BehaviorListener.
   */
  public void behaviorEventProcessListenerTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "behaviorEventProcessListenerTest");
    invoke();
  }
} // end of URLClient
