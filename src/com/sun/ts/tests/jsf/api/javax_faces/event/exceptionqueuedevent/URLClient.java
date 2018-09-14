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
package com.sun.ts.tests.jsf.api.javax_faces.event.exceptionqueuedevent;

import java.io.PrintWriter;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_event_exceptionqueuedevent_web";

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
   * @testName: exceptionQueuedEventCtorTest
   * @assertion_ids: JSF:JAVADOC:1796
   * @test_Strategy: Ensure Instantiate a new ExceptionQueuedEvent.
   * 
   * @since 2.0
   */
  public void exceptionQueuedEventCtorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "exceptionQueuedEventCtorTest");
    invoke();
  }

  /**
   * @testName: exceptionQueuedEventGetContextTest
   * @assertion_ids: JSF:JAVADOC:1797
   * @test_Strategy: Ensure getContext() returns the
   *                 ExceptionQueuedEventContext.
   * 
   * @since 2.0
   */
  public void exceptionQueuedEventGetContextTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "exceptionQueuedEventGetContextTest");
    invoke();
  }

  /**
   * @testName: exceptionQueuedEventIsApproiateListenerPostiveTest
   * @assertion_ids: JSF:JAVADOC:1796
   * @test_Strategy: Ensure isAppropriateListener() Returns true if and only if
   *                 the argument listener is an instance of
   *                 SystemEventListener.
   * 
   * @since 2.0
   */
  public void exceptionQueuedEventIsApproiateListenerPostiveTest()
      throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "exceptionQueuedEventIsApproiateListenerPostiveTest");
    invoke();
  }

  /**
   * @testName: exceptionQueuedEventIsApproiateListenerNegativeTest
   * @assertion_ids: JSF:JAVADOC:1796
   * @test_Strategy: Ensure isAppropriateListener() Returns true if and only if
   *                 the argument listener is *NOT* an instance of
   *                 SystemEventListener.
   * 
   * @since 2.0
   */
  public void exceptionQueuedEventIsApproiateListenerNegativeTest()
      throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "exceptionQueuedEventIsApproiateListenerNegativeTest");
    invoke();
  }

  /**
   * @testName: exceptionQueuedEventProcessListenerTest
   * @assertion_ids: JSF:JAVADOC:1799; JSF:JAVADOC:1800
   * @test_Strategy: Ensure calling ExceptionQueuedEvent.processListener() calls
   *                 through to ComponentSystemEventListener.processEvent().
   * 
   * @since 2.0
   */
  public void exceptionQueuedEventProcessListenerTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "exceptionQueuedEventProcessListenerTest");
    invoke();
  }
} // end of URLClient
