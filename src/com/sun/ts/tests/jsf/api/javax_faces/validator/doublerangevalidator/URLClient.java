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

package com.sun.ts.tests.jsf.api.javax_faces.validator.doublerangevalidator;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.jsf.api.javax_faces.validator.common.BaseValidatorClient;

public class URLClient extends BaseValidatorClient {

  private static final String CONTEXT_ROOT = "/jsf_validator_doublevalidator_web";

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

  // ------------------------------------------------ Test Methods

  /**
   * @testName: doubleValidatorCtor1Test
   * @assertion_ids: JSF:JAVADOC:2136; JSF:JAVADOC:2221
   * @test_Strategy: Validate DoubleRangeValidator no-arg constructor.
   */
  public void doubleValidatorCtor1Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "doubleValidatorCtor1Test");
    invoke();
  }

  /**
   * @testName: doubleValidatorCtor2Test
   * @assertion_ids: JSF:JAVADOC:2137
   * @test_Strategy: Validate DoubleRangeValidator(double max).
   */
  public void doubleValidatorCtor2Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "doubleValidatorCtor2Test");
    invoke();
  }

  /**
   * @testName: doubleValidatorCtor3Test
   * @assertion_ids: JSF:JAVADOC:2138
   * @test_Strategy: Validate DoubleRangeValidator(double max, double min).
   */
  public void doubleValidatorCtor3Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "doubleValidatorCtor3Test");
    invoke();
  }

  /**
   * @testName: doubleValidatorGetSetMaximumTest
   * @assertion_ids: JSF:JAVADOC:2140; JSF:JAVADOC:2148
   * @test_Strategy: Validate get/setMaximum() works as expected.
   */
  public void doubleValidatorGetSetMaximumTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "doubleValidatorGetSetMaximumTest");
    invoke();
  }

  /**
   * @testName: doubleValidatorGetSetMinimumTest
   * @assertion_ids: JSF:JAVADOC:2141; JSF:JAVADOC:2149
   * @test_Strategy: Validate get/setMinimum() works as expected.
   */
  public void doubleValidatorGetSetMinimumTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "doubleValidatorGetSetMinimumTest");
    invoke();
  }

  /**
   * @testName: doubleValidatorValidateTest
   * @assertion_ids: JSF:JAVADOC:2151
   * @test_Strategy: Verify that DoubleRangeValidator.validate() performs
   *                 correct validations on valid components (i.e. no Messages
   *                 are pushed to the FacesContext.
   */
  public void doubleValidatorValidateTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "doubleValidatorValidateTest");
    invoke();
  }

  /**
   * @testName: doubleValidatorValidateInvalidTypeTest
   * @assertion_ids: JSF:JAVADOC:2152
   * @test_Strategy: Verify if the DoubleRangeValidator is provided an
   *                 UIComponent with an invalid type, or a String that cannot
   *                 be converted to a Double, a validation failure occurs and
   *                 that there is at least one message in the FacesContext for
   *                 this component.
   */
  public void doubleValidatorValidateInvalidTypeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "doubleValidatorValidateInvalidTypeTest");
    invoke();
  }

  /**
   * @testName: doubleValidatorValidateMaxViolationTest
   * @assertion_ids: JSF:JAVADOC:2152
   * @test_Strategy: Verify if the DoubleRangeValidator is provided an
   *                 UIComponent with a value that exceeds the allowable maximum
   *                 generates at least one message in the FacesContext for the
   *                 component in question.
   */
  public void doubleValidatorValidateMaxViolationTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "doubleValidatorValidateMaxViolationTest");
    invoke();
  }

  /**
   * @testName: doubleValidatorValidateMinViolationTest
   * @assertion_ids: JSF:JAVADOC:2152
   * @test_Strategy: Verify if the DoubleRangeValidator is provided an
   *                 UIComponent with a value that is less than the configured
   *                 minimum for the validator, that at least one message in the
   *                 FacesContext for the component in question.
   */
  public void doubleValidatorValidateMinViolationTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "doubleValidatorValidateMinViolationTest");
    invoke();
  }

  /**
   * @testName: doubleValidatorValidateNPETest
   * @assertion_ids: JSF:JAVADOC:2153
   * @test_Strategy: Throws NullPointerException if context or component is null
   * 
   * @since: 2.2
   */
  public void doubleValidatorValidateNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "doubleValidatorValidateNPETest");
    invoke();
  }

  // ------------------------------------ StateHolder Tests
  /**
   * @testName: stateHolderIsSetTransientTest
   * @assertion_ids: JSF:JAVADOC:2144; JSF:JAVADOC:2150
   * @test_Strategy: Verify that is/setTransient() performs as expected.
   * 
   * @since: 2.0
   */
  public void stateHolderIsSetTransientTest() throws EETest.Fault {
    super.stateHolderIsSetTransientTest();
  }

  /**
   * @testName: stateHolderRestoreStateNPETest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate that a NullPointer is thrown if context is null.
   * 
   * @since: 2.2
   */
  public void stateHolderRestoreStateNPETest() throws Fault {
    super.stateHolderRestoreStateNPETest();
  }

  /**
   * @testName: stateHolderSaveStateNPETest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate that a NullPointer is thrown if context is null.
   * 
   * @since: 2.2
   */
  public void stateHolderSaveStateNPETest() throws Fault {
    super.stateHolderSaveStateNPETest();
  }

  /**
   * @testName: stateHolderSaveRestoreStateTest
   * @assertion_ids: JSF:JAVADOC:2146; JSF:JAVADOC:2147
   * @test_Strategy: Validate that saveState and restoreState work properly.
   * 
   * @since: 2.2
   */
  public void stateHolderSaveRestoreStateTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "stateHolderSaveRestoreStateTest");
    invoke();
  }

  // ----------------------------------- PartialStateHolder Tests
  /**
   * @testName: validatorPartialStateTest
   * @assertion_ids: JSF:JAVADOC:2135; JSF:JAVADOC:2143; JSF:JAVADOC:2145
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
