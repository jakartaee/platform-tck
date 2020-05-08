/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsf.api.jakarta_faces.validator.lengthvalidator;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.jsf.api.jakarta_faces.validator.common.BaseValidatorClient;

public class URLClient extends BaseValidatorClient {

  private static final String CONTEXT_ROOT = "/jsf_validator_lengthvalidator_web";

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
   * @testName: lengthValidatorCtor1Test
   * @assertion_ids: JSF:JAVADOC:2163; JSF:JAVADOC:2221
   * @test_Strategy: Validate the no-arg constructor of the LengthValidator.
   *                 Ensure no exceptions are thrown.
   */
  public void lengthValidatorCtor1Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "lengthValidatorCtor1Test");
    invoke();
  }

  /**
   * @testName: lengthValidatorCtor2Test
   * @assertion_ids: JSF:JAVADOC:2164
   * @test_Strategy: Validate LengthValidator(int maximum) constructor. Ensure
   *                 no exceptions are thrown.
   */
  public void lengthValidatorCtor2Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "lengthValidatorCtor2Test");
    invoke();
  }

  /**
   * @testName: lengthValidatorCtor3Test
   * @assertion_ids: JSF:JAVADOC:2165
   * @test_Strategy: Validate LengthValidator(int maximum, int minimum)
   *                 constructor. Ensure no exceptions are thrown.
   */
  public void lengthValidatorCtor3Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "lengthValidatorCtor3Test");
    invoke();
  }

  /**
   * @testName: lengthValidatorGetSetMaximumTest
   * @assertion_ids: JSF:JAVADOC:2158; JSF:JAVADOC:2169
   * @test_Strategy: Validate the behavior of get/setMaximum().
   */
  public void lengthValidatorGetSetMaximumTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "lengthValidatorGetSetMaximumTest");
    invoke();
  }

  /**
   * @testName: lengthValidatorGetSetMinimumTest
   * @assertion_ids: JSF:JAVADOC:2159; JSF:JAVADOC:2170
   * @test_Strategy: Validate the behavior of get/setMinumum().
   */
  public void lengthValidatorGetSetMinimumTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "lengthValidatorGetSetMinimumTest");
    invoke();
  }

  /**
   * @testName: lengthValidatorValidateTest
   * @assertion_ids: JSF:JAVADOC:2172
   * @test_Strategy: Confirm validation works as expected for valid components.
   *                 No Message objects should be present in the FacesContext.
   */
  public void lengthValidatorValidateTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "lengthValidatorValidateTest");
    invoke();
  }

  /**
   * @testName: lengthValidatorValidateNPETest
   * @assertion_ids: JSF:JAVADOC:2174
   * @test_Strategy: Validate that a NullPointerException is thrown if context
   *                 or component is null
   * 
   * @since 2.2
   */
  public void lengthValidatorValidateNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "lengthValidatorValidateNPETest");
    invoke();
  }

  /**
   * @testName: lengthValidatorValidateMaxViolationTest
   * @assertion_ids: JSF:JAVADOC:2173
   * @test_Strategy: Confirm validation Messages are pushed to the FacesContext
   *                 when the provided UIComponent's value exceeds the
   *                 configured length maximum.
   */
  public void lengthValidatorValidateMaxViolationTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "lengthValidatorValidateMaxViolationTest");
    invoke();
  }

  /**
   * @testName: lengthValidatorValidateMinViolationTest
   * @assertion_ids: JSF:JAVADOC:2173
   * @test_Strategy: Confirm validation Messages are pushed to the FacesContext
   *                 when the provided UIComponent's value is less than the
   *                 configured length minimum.
   */
  public void lengthValidatorValidateMinViolationTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "lengthValidatorValidateMinViolationTest");
    invoke();
  }

  // ------------------------------------------------------- StateHolder Tests
  /**
   * @testName: stateHolderIsSetTransientTest
   * 
   * @assertion_ids: JSF:JAVADOC:2171; JSF:JAVADOC:2162
   * 
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
   * @assertion_ids: JSF:JAVADOC:2167; JSF:JAVADOC:2168
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
   * 
   * @assertion_ids: JSF:JAVADOC:2156; JSF:JAVADOC:2161; JSF:JAVADOC:2166
   * 
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
