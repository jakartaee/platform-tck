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

package com.sun.ts.tests.jsf.api.javax_faces.validator.longrangevalidator;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.jsf.api.javax_faces.validator.common.BaseValidatorClient;

public class URLClient extends BaseValidatorClient {

  private static final String CONTEXT_ROOT = "/jsf_validator_longvalidator_web";

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

  // ------------------------------------------------ Test Methods -------

  /**
   * @testName: longValidatorCtor1Test
   * @assertion_ids: JSF:JAVADOC:2182; JSF:JAVADOC:2221
   * @test_Strategy: Validate LongRangeValidator no-arg constructor.
   */
  public void longValidatorCtor1Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "longValidatorCtor1Test");
    invoke();
  }

  /**
   * @testName: longValidatorCtor2Test
   * @assertion_ids: JSF:JAVADOC:2183
   * @test_Strategy: Validate LongRangeValidator(long max).
   */
  public void longValidatorCtor2Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "longValidatorCtor2Test");
    invoke();
  }

  /**
   * @testName: longValidatorCtor3Test
   * @assertion_ids: JSF:JAVADOC:2184
   * @test_Strategy: Validate LongRangeValidator(long max, long min).
   */
  public void longValidatorCtor3Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "longValidatorCtor3Test");
    invoke();
  }

  /**
   * @testName: longValidatorGetSetMaximumTest
   * @assertion_ids: JSF:JAVADOC:2177; JSF:JAVADOC:2188
   * @test_Strategy: Validate get/setMaximum() works as expected.
   */
  public void longValidatorGetSetMaximumTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "longValidatorGetSetMaximumTest");
    invoke();
  }

  /**
   * @testName: longValidatorGetSetMinimumTest
   * @assertion_ids: JSF:JAVADOC:2178; JSF:JAVADOC:2189
   * @test_Strategy: Validate get/setMinimum() works as expected.
   */
  public void longValidatorGetSetMinimumTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "longValidatorGetSetMinimumTest");
    invoke();
  }

  /**
   * @testName: longValidatorValidateTest
   * @assertion_ids: JSF:JAVADOC:2191
   * @test_Strategy: Verify that LongRangeValidator.validate() performs correct
   *                 validations on valid components (i.e. no Messages are
   *                 pushed to the FacesContext.
   */
  public void longValidatorValidateTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "longValidatorValidateTest");
    invoke();
  }

  /**
   * @testName: longValidatorValidateInvalidTypeTest
   * @assertion_ids: JSF:JAVADOC:2192
   * @test_Strategy: Verify if the LongRangeValidator is provided an UIComponent
   *                 with an invalid type, or a String that cannot be converted
   *                 to a Long, a validation failure occurs and that there is at
   *                 least one message in the FacesContext for this component.
   */
  public void longValidatorValidateInvalidTypeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "longValidatorValidateInvalidTypeTest");
    invoke();
  }

  /**
   * @testName: longValidatorValidateMaxViolationTest
   * @assertion_ids: JSF:JAVADOC:2192
   * @test_Strategy: Verify if the LongRangeValidator is provided an UIComponent
   *                 with a value that exceeds the allowable maximum generates
   *                 at least one message in the FacesContext for the component
   *                 in question.
   */
  public void longValidatorValidateMaxViolationTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "longValidatorValidateMaxViolationTest");
    invoke();
  }

  /**
   * @testName: longValidatorValidateMinViolationTest
   * @assertion_ids: JSF:JAVADOC:2192
   * @test_Strategy: Verify if the LongRangeValidator is provided an UIComponent
   *                 with a value that is less than the configured minimum for
   *                 the validator, that at least one message in the
   *                 FacesContext for the component in question.
   */
  public void longValidatorValidateMinViolationTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "longValidatorValidateMinViolationTest");
    invoke();
  }

  /**
   * @testName: longValidatorValidateNPETest
   * @assertion_ids: JSF:JAVADOC:2193
   * @test_Strategy: Verify that a NullPointerExcetption is throw if context or
   *                 component is null
   */
  public void longValidatorValidateNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "longValidatorValidateNPETest");
    invoke();
  }

  // ------------------------------------------------------- StateHolder Tests
  /**
   * @testName: stateHolderIsSetTransientTest
   * @assertion_ids: JSF:JAVADOC:2181; JSF:JAVADOC:2190
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
   * @assertion_ids: JSF:JAVADOC:2186; JSF:JAVADOC:2187
   * @test_Strategy: Validate that saveState and restoreState work properly.
   * 
   * @since: 2.2
   */
  public void stateHolderSaveRestoreStateTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "stateHolderSaveRestoreStateTest");
    invoke();
  }

  // ------------------------------------------------ PartialStateHolder Tests
  /**
   * @testName: validatorPartialStateTest
   * @assertion_ids: JSF:JAVADOC:2175; JSF:JAVADOC:2180; JSF:JAVADOC:2185
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
