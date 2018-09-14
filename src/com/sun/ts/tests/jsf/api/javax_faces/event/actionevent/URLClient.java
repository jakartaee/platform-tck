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

package com.sun.ts.tests.jsf.api.javax_faces.event.actionevent;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_event_actionevent_web";

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
   * @testName: actionEventCtorTest
   * @assertion_ids: JSF:JAVADOC:1778
   * @test_Strategy: Ensure an ActionEvent can be created by passing a valid
   *                 UIComponent.
   */
  public void actionEventCtorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "actionEventCtorTest");
    invoke();
  }

  /**
   * @testName: actionEventCtorIllegalArgumentExceptionTest
   * @assertion_ids: JSF:JAVADOC:1778
   * @test_Strategy: Ensure an IllegalArgumentException is thrown if a a null
   *                 component is passed to the ActionEvent constructor.
   */
  public void actionEventCtorIllegalArgumentExceptionTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "actionEventCtorIllegalArgumentExceptionTest");
    invoke();
  }

  /**
   * @testName: actionEventGetComponentTest
   * @assertion_ids: JSF:JAVADOC:1778
   * @test_Strategy: Ensure getComponent() returns the component passed to the
   *                 ActionEvent constructor.
   */
  public void actionEventGetComponentTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "actionEventGetComponentTest");
    invoke();
  }

  /**
   * @testName: actionEventIsApproiateListenerPostiveTest
   * @assertion_ids: JSF:JAVADOC:1779; JSF:JAVADOC:1812
   * @test_Strategy: Ensure isAppropriateListener() returns true if an
   *                 ActionListener is passed.
   */
  public void actionEventIsApproiateListenerPostiveTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "actionEventIsApproiateListenerPostiveTest");
    invoke();
  }

  /**
   * @testName: actionEventIsApproiateListenerNegativeTest
   * @assertion_ids: JSF:JAVADOC:1779; JSF:JAVADOC:1783
   * @test_Strategy: Ensure isAppropriateListener() returns false if a non-
   *                 ActionListener is passed.
   */
  public void actionEventIsApproiateListenerNegativeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "actionEventIsApproiateListenerNegativeTest");
    invoke();
  }

  /**
   * @testName: actionEventProcessListenerTest
   * @assertion_ids: JSF:JAVADOC:1780; JSF:JAVADOC:1813; JSF:JAVADOC:1782
   * @test_Strategy: Ensure calling ActionEvent.processListener() calls through
   *                 to ActionListener.processAction().
   */
  public void actionEventProcessListenerTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "actionEventProcessListenerTest");
    invoke();
  }

} // end of URLClient
