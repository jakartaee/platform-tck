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
package com.sun.ts.tests.jsf.api.javax_faces.validator.beanvalidator;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.jsf.api.javax_faces.validator.common.BaseValidatorClient;

public class URLClient extends BaseValidatorClient {

  private static final String CONTEXT_ROOT = "/jsf_validator_beanvalidator_web";

  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  public Status run(String args[], PrintWriter out, PrintWriter err) {
    setContextRoot(CONTEXT_ROOT);
    setServletName(DEFAULT_SERVLET_NAME);
    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Run Tests */

  // ------------------------------------------------------------ Test Methods

  /**
   * @testName: beanValidatorCtorTest
   * 
   * @assertion_ids: JSF:JAVADOC:2123; JSF:JAVADOC:2221
   * 
   * @test_Strategy: Validate beanValidator no-arg constructor.
   */
  public void beanValidatorCtorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "beanValidatorCtorTest");
    invoke();
  }

  /**
   * @testName: beanvalidatorClearInitialStateTest
   * 
   * @assertion_ids: JSF:JAVADOC:2123; JSF:JAVADOC:2124
   * 
   * @test_Strategy: Validate clearInitialState() works as expected.
   */
  public void beanvalidatorClearInitialStateTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "beanvalidatorClearInitialStateTest");
    invoke();
  }

  /**
   * @testName: beanvalidatorgetsetValidationGroupsTest
   * 
   * @assertion_ids: JSF:JAVADOC:2123; JSF:JAVADOC:2125; JSF:JAVADOC:2132;
   *                 JSF:JAVADOC:2128
   * 
   * @test_Strategy: Verify that beanValidator.set/getValidationGroups performs
   *                 as expected.
   */
  public void beanvalidatorgetsetValidationGroupsTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "beanvalidatorgetsetValidationGroupsTest");
    invoke();
  }

  // ------------------------------------------------------- StateHolder Tests
  /**
   * @testName: stateHolderIsSetTransientTest
   * @assertion_ids: JSF:JAVADOC:2127; JSF:JAVADOC:2131
   * @test_Strategy: Verify that is/setTransient() performs as expected.
   * 
   * @since: 2.0
   */
  public void stateHolderIsSetTransientTest() throws EETest.Fault {
    super.stateHolderIsSetTransientTest();
  }

  /**
   * @testName: stateHolderRestoreStateNPETest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that a NullPointer is thrown if context is null.
   * 
   * @since: 2.2
   */
  public void stateHolderRestoreStateNPETest() throws Fault {
    super.stateHolderRestoreStateNPETest();
  }

  /**
   * @testName: stateHolderSaveStateNPETest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that a NullPointer is thrown if context is null.
   * 
   * @since: 2.2
   */
  public void stateHolderSaveStateNPETest() throws Fault {
    super.stateHolderSaveStateNPETest();
  }

  /**
   * @testName: stateHolderSaveRestoreStateTest
   * 
   * @assertion_ids: JSF:JAVADOC:2129; JSF:JAVADOC:2130
   * 
   * @test_Strategy: Validate that we are able to saveSate and restoreState.
   */
  public void stateHolderSaveRestoreStateTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "stateHolderSaveRestoreStateTest");
    invoke();
  }

  // ------------------------------------------------ PartialStateHolder Tests
  /**
   * @testName: validatorPartialStateTest
   * @assertion_ids: JSF:JAVADOC:2124; JSF:JAVADOC:2126; JSF:JAVADOC:2128
   * @test_Strategy: Verify that the following method calls perform as expected.
   * 
   *                 clearInitialState(); initialStateMarked();
   *                 markInitialState();
   * 
   * 
   * @since: 2.0
   */
  public void validatorPartialStateTest() throws EETest.Fault {
    super.validatorPartialStateTest();
  }
}
