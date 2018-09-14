/*
 * Copyright (c) 2011, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jsf.api.javax_faces.component.behavior.common;

import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public abstract class BaseBehaviorClient extends AbstractUrlClient {

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  // ----------------------- Behavior Based Tests
  /**
   * testName: behaviorBroadcastNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:1016; JSF:JAVADOC:999; JSF:JAVADOC:998;
   *                 JSF:JAVADOC:1003
   * @test_Strategy: Validate that if BehaviorListener in
   *                 BehaviorBase.broadcast(BehaviorListener) is null that a
   *                 NullPointerException is thrown.
   */
  public void behaviorBroadcastNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "behaviorBroadcastNPETest");
    invoke();
  }

  /**
   * testName: behaviorMICInitialStateTest
   * 
   * @assertion_ids: JSF:JAVADOC:1016; JSF:JAVADOC:999; JSF:JAVADOC:1004;
   *                 JSF:JAVADOC:1005; JSF:JAVADOC:1007
   * @test_Strategy: Validate that we get the correct boolean value when using
   *                 the following methods. - BehaviorBase.markInitialState() -
   *                 BehaviorBase.initialStateMarked() -
   *                 BehaviorBase.clearInitialState()
   */
  public void behaviorMICInitialStateTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "behaviorMICInitialStateTest");
    invoke();
  }

  /**
   * testName: behaviorSITransientTest
   * 
   * @assertion_ids: JSF:JAVADOC:1016; JSF:JAVADOC:999; JSF:JAVADOC:1006;
   *                 JSF:JAVADOC:1008
   * @test_Strategy: Validate that we do not participate in StateSaving or
   *                 Restoring when BehaviorBase.setTransient(false), also make
   *                 sure we get back the correct boolean value form
   *                 BehaviorBase.isTransient().
   */
  public void behaviorSITransientTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "behaviorSITransientTest");
    invoke();
  }

  // ---------------------ClientBehavior Based Tests

  /**
   * testName: clientBehaviorDecodeNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:1016; JSF:JAVADOC:999; JSF:JAVADOC:1012
   * @test_Strategy: Validate that a NullPointerException is thrown if Context
   *                 or Component are null.
   */
  public void clientBehaviorDecodeNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "clientBehaviorDecodeNPETest");
    invoke();
  }

  /**
   * testName: clientBehaviorGetScriptNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:1016; JSF:JAVADOC:999; JSF:JAVADOC:1015
   * @test_Strategy: Validate that a NullPointerException is thrown if
   *                 ClientBehaviorContext null.
   * 
   * @since 2.0
   */
  public void clientBehaviorGetScriptNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "clientBehaviorGetScriptNPETest");
    invoke();
  }

} // end of URLClient
