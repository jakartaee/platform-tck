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
package com.sun.ts.tests.jsf.api.javax_faces.event.common;

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public abstract class BaseEventClient extends AbstractUrlClient {

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  // ------------------------------------------ Test Declarations
  // ------------------------------------------- SystemEvent Tests

  /*
   * testName: systemEventIsAppropriateListenerPostiveTest
   * 
   * @assertion_ids: JSF:JAVADOC:1864; JSF:JAVADOC:1863; JSF:JAVADOC:1876
   * 
   * @test_Strategy: Ensure isAppropriateListener() Returns true if and only if
   * the argument listener is an instance of SystemEventListener.
   *
   * @since 2.0
   */
  public void systemEventIsAppropriateListenerPostiveTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "systemEventIsAppropriateListenerPostiveTest");
    invoke();
  }

  /*
   * testName: systemEventIsAppropriateListenerNegativeTest
   * 
   * @assertion_ids: JSF:JAVADOC:1864; JSF:JAVADOC:1863; JSF:JAVADOC:1876
   * 
   * @test_Strategy: Ensure isAppropriateListener() Returns true if and only if
   * the argument listener is *NOT* an instance of SystemEventListener.
   *
   * @since 2.0
   */
  public void systemEventIsAppropriateListenerNegativeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "systemEventIsAppropriateListenerNegativeTest");
    invoke();
  }

  /*
   * testName: systemEventProcessListenerTest
   * 
   * @assertion_ids: JSF:JAVADOC:1864; JSF:JAVADOC:1863; JSF:JAVADOC:1877
   * 
   * @test_Strategy: Ensure calling preDestroyApplicationEvent.processListener()
   * calls through to ComponentSystemEventListener.processEvent().
   *
   * @since 2.0
   */
  public void systemEventProcessListenerTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "systemEventProcessListenerTest");
    invoke();
  }

} // end of URLClient
