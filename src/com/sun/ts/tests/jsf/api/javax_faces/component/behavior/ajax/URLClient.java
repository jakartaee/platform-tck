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
package com.sun.ts.tests.jsf.api.javax_faces.component.behavior.ajax;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.jsf.api.javax_faces.component.behavior.common.BaseBehaviorClient;

public class URLClient extends BaseBehaviorClient {

  private static final String CONTEXT_ROOT = "/jsf_comp_behavior_ajax_web";

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

  // ---------------------------------------------- Behavior Specific
  /**
   * @testName: behaviorBroadcastNPETest
   * @assertion_ids: JSF:JAVADOC:1016; JSF:JAVADOC:971; JSF:JAVADOC:998;
   *                 JSF:JAVADOC:1003
   * @test_Strategy: Validate that if BehaviorListener in
   *                 BehaviorBase.broadcast(BehaviorListener) is null that a
   *                 NullPointerException is thrown.
   */
  public void behaviorBroadcastNPETest() throws EETest.Fault {
    super.behaviorBroadcastNPETest();
  }

  /**
   * @testName: behaviorMICInitialStateTest
   * @assertion_ids: JSF:JAVADOC:1016; JSF:JAVADOC:971; JSF:JAVADOC:999;
   *                 JSF:JAVADOC:1004; JSF:JAVADOC:1005; JSF:JAVADOC:1007
   * @test_Strategy: Validate that we get the correct boolean value when using
   *                 the following methods. - BehaviorBase.markInitialState() -
   *                 BehaviorBase.initialStateMarked() -
   *                 BehaviorBase.clearInitialState()
   */
  public void behaviorMICInitialStateTest() throws EETest.Fault {
    super.behaviorMICInitialStateTest();
  }

  /**
   * @testName: behaviorSITransientTest
   * @assertion_ids: JSF:JAVADOC:1016; JSF:JAVADOC:971; JSF:JAVADOC:999;
   *                 JSF:JAVADOC:1006; JSF:JAVADOC:1010
   * @test_Strategy: Validate that we do not participate in StateSaving or
   *                 Restoring when BehaviorBase.setTransient(false), also make
   *                 sure we get back the correct boolean value form
   *                 BehaviorBase.isTransient().
   */
  public void behaviorSITransientTest() throws Fault {
    super.behaviorSITransientTest();
  }

  // ---------------------------------------------- ClientBehavior Specific
  /**
   * @testName: clientBehaviorDecodeNPETest
   * @assertion_ids: JSF:JAVADOC:1016; JSF:JAVADOC:999; JSF:JAVADOC:1012;
   *                 JSF:JAVADOC:1018
   * @test_Strategy: Validate that a NullPointerException is thrown if Context
   *                 or Component are null.
   */
  public void clientBehaviorDecodeNPETest() throws Fault {
    super.clientBehaviorDecodeNPETest();
  }

  /**
   * @testName: clientBehaviorGetScriptNPETest
   * @assertion_ids: JSF:JAVADOC:1016; JSF:JAVADOC:999; JSF:JAVADOC:1015;
   *                 JSF:JAVADOC:1022
   * @test_Strategy: Validate that a NullPointerException is thrown if
   *                 ClientBehaviorContext null.
   * 
   */
  public void clientBehaviorGetScriptNPETest() throws Fault {
    super.clientBehaviorGetScriptNPETest();
  }

  // -------------------------------- AjaxBehavior Specific
  /**
   * @testName: ajaxBehaviorBroadcastTest
   * @assertion_ids: JSF:JAVADOC:1016; JSF:JAVADOC:971; JSF:JAVADOC:999;
   *                 JSF:JAVADOC:1000; JSF:JAVADOC:969
   * @testStrategy: Verify any Listeners registered with the UICommand instance
   *                are invoked during the INVOKE_APPLICATION_PHASE and in order
   *                of registration.
   */
  public void ajaxBehaviorBroadcastTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "ajaxBehaviorBroadcastTest");
    invoke();
  }

  /**
   * @testName: ajaxBehaviorAddListenerNPETest
   * @assertion_ids: JSF:JAVADOC:1016; JSF:JAVADOC:971; JSF:JAVADOC:970
   * @testStrategy: Validate that we receive a NullPointerException when
   *                providing null Listener.
   */
  public void ajaxBehaviorAddListenerNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "ajaxBehaviorAddListenerNPETest");
    invoke();
  }

  /**
   * @testName: ajaxBehaviorGetSetDelayTest
   * @assertion_ids: JSF:JAVADOC:1016; JSF:JAVADOC:971; JSF:JAVADOC:2700;
   *                 JSF:JAVADOC:2701
   * @testStrategy: Validate that we are able set and get the delay value
   *                properly.
   */
  public void ajaxBehaviorGetSetDelayTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "ajaxBehaviorGetSetDelayTest");
    invoke();
  }

  /**
   * @testName: ajaxBehaviorGetSetExecuteTest
   * @assertion_ids: JSF:JAVADOC:1016; JSF:JAVADOC:971; JSF:JAVADOC:972;
   *                 JSF:JAVADOC:988
   * @testStrategy: Validate that we are able set and get the 'delay' value
   *                properly.
   */
  public void ajaxBehaviorGetSetExecuteTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "ajaxBehaviorGetSetExecuteTest");
    invoke();
  }

  /**
   * @testName: ajaxBehaviorSetIsDisabledTest
   * @assertion_ids: JSF:JAVADOC:1016; JSF:JAVADOC:971; JSF:JAVADOC:980;
   *                 JSF:JAVADOC:987
   * @testStrategy: Validate that we are able set and get the 'disabled' value
   *                properly.
   */
  public void ajaxBehaviorSetIsDisabledTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "ajaxBehaviorSetIsDisabledTest");
    invoke();
  }

  /**
   * @testName: ajaxBehaviorSetIsImmediateTest
   * @assertion_ids: JSF:JAVADOC:1016; JSF:JAVADOC:971; JSF:JAVADOC:982;
   *                 JSF:JAVADOC:989
   * @testStrategy: Validate that we are able set and get the 'disabled' value
   *                properly.
   */
  public void ajaxBehaviorSetIsImmediateTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "ajaxBehaviorSetIsImmediateTest");
    invoke();
  }

  /**
   * @testName: ajaxBehaviorGetSetValueExpressionNPETest
   * @assertion_ids: JSF:JAVADOC:1016; JSF:JAVADOC:979; JSF:JAVADOC:994
   * @test_Strategy: Verify a NullPointerException is thrown if a null value for
   *                 the 'name' argument.
   * 
   * @since 2.0
   */
  public void ajaxBehaviorGetSetValueExpressionNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "ajaxBehaviorGetSetValueExpressionNPETest");
    invoke();
  }

  /**
   * @testName: ajaxBehaviorAddRemoveBehaviorListenerNPETest
   * @assertion_ids: JSF:JAVADOC:1016; JSF:JAVADOC:979; JSF:JAVADOC:984
   * @test_Strategy: Verify a NullPointerException is thrown when
   *                 BehaviorListener is null.
   * 
   * @since 2.0
   */
  public void ajaxBehaviorAddRemoveBehaviorListenerNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "ajaxBehaviorAddRemoveBehaviorListenerNPETest");
    invoke();
  }

  /**
   * @testName: ajaxBehaviorGetSetOnerrorTest
   * @assertion_ids: JSF:JAVADOC:1016; JSF:JAVADOC:974; JSF:JAVADOC:990
   * @test_Strategy: Verify the we get the expected information back.
   * 
   * @since 2.0
   */
  public void ajaxBehaviorGetSetOnerrorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "ajaxBehaviorGetSetOnerrorTest");
    invoke();
  }

  /**
   * @testName: ajaxBehaviorGetSetOnventTest
   * @assertion_ids: JSF:JAVADOC:1016; JSF:JAVADOC:975; JSF:JAVADOC:991
   * @test_Strategy: Verify the we get the expected information back.
   * 
   * @since 2.0
   */
  public void ajaxBehaviorGetSetOnventTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "ajaxBehaviorGetSetOnventTest");
    invoke();
  }

  /**
   * @testName: ajaxBehaviorGetSetRenderTest
   * @assertion_ids: JSF:JAVADOC:1016; JSF:JAVADOC:976; JSF:JAVADOC:992
   * @test_Strategy: Verify the we get the expected information back.
   * 
   * @since 2.0
   */
  public void ajaxBehaviorGetSetRenderTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "ajaxBehaviorGetSetRenderTest");
    invoke();
  }

  /**
   * @testName: ajaxBehaviorGetSetValueExpressionTest
   * @assertion_ids: JSF:JAVADOC:1016; JSF:JAVADOC:977; JSF:JAVADOC:993
   * @test_Strategy: Verify the we get the expected information back.
   * 
   * @since 2.0
   */
  public void ajaxBehaviorGetSetValueExpressionTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "ajaxBehaviorGetSetValueExpressionTest");
    invoke();
  }

  /**
   * @testName: ajaxBehaviorIsSetResetValuesTest
   * @assertion_ids: JSF:JAVADOC:2883; JSF:JAVADOC:2884; JSF:JAVADOC:2885
   * @test_Strategy: Validated the following methods: setResetValues(),
   *                 isResetValues(), isResetValuesSet().
   * 
   * @since 2.2
   */
  public void ajaxBehaviorIsSetResetValuesTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "ajaxBehaviorIsSetResetValuesTest");
    invoke();
  }
}
