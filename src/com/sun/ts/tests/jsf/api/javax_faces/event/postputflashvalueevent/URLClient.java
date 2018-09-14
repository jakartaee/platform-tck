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
 * $Id$
 */

package com.sun.ts.tests.jsf.api.javax_faces.event.postputflashvalueevent;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.jsf.api.javax_faces.event.common.BaseEventClient;

public final class URLClient extends BaseEventClient {

  private static final String CONTEXT_ROOT = "/jsf_event_postputflashvalueevent_web";

  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  public Status run(String args[], PrintWriter out, PrintWriter err) {
    // don't reset the context root if already been set
    if (getContextRoot() == null) {
      setContextRoot(CONTEXT_ROOT);
    }
    // don't reset the Servlet name if already set
    if (getServletName() == null) {
      setServletName(DEFAULT_SERVLET_NAME);
    }
    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Test Declarations */

  // ---------------------------------------- PostKeepFlashValueEvent Tests
  /**
   * @testName: postPutFlashValueEventGetKeyTest
   * @assertion_ids: JSF:JAVADOC:2705; JSF:JAVADOC:2704
   * @test_Strategy: Validate that we get the correct value returned from
   *                 PostPutFlashValueEvent.getKey().
   * 
   * @since 2.2
   */
  public void postPutFlashValueEventGetKeyTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "postPutFlashValueEventGetKeyTest");
    invoke();
  }

  // ---------------------------------------- SystemEvent Tests
  /**
   * @testName: systemEventIsAppropriateListenerPostiveTest
   * @assertion_ids: JSF:JAVADOC:1864; JSF:JAVADOC:1863; JSF:JAVADOC:1876
   * @test_Strategy: Ensure isAppropriateListener() Returns true if and only if
   *                 the argument listener is an instance of
   *                 SystemEventListener.
   * 
   * @since 2.2
   */
  public void systemEventIsAppropriateListenerPostiveTest()
      throws EETest.Fault {
    super.systemEventIsAppropriateListenerPostiveTest();
  }

  /**
   * @testName: systemEventIsAppropriateListenerNegativeTest
   * @assertion_ids: JSF:JAVADOC:1864; JSF:JAVADOC:1863; JSF:JAVADOC:1876
   * @test_Strategy: Ensure isAppropriateListener() Returns true if and only if
   *                 the argument listener is *NOT* an instance of
   *                 SystemEventListener.
   * 
   * @since 2.2
   */
  public void systemEventIsAppropriateListenerNegativeTest()
      throws EETest.Fault {
    super.systemEventIsAppropriateListenerNegativeTest();
  }

  /**
   * @testName: systemEventProcessListenerTest
   * @assertion_ids: JSF:JAVADOC:1864; JSF:JAVADOC:1863; JSF:JAVADOC:1877
   * @test_Strategy: Ensure calling preDestroyApplicationEvent.processListener()
   *                 calls through to
   *                 ComponentSystemEventListener.processEvent().
   * 
   * @since 2.2
   */
  public void systemEventProcessListenerTest() throws EETest.Fault {
    super.systemEventProcessListenerTest();
  }

} // end of URLClient
