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

package com.sun.ts.tests.jsf.api.javax_faces.validator.regexvalidator;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.jsf.api.javax_faces.validator.common.BaseValidatorClient;

public class URLClient extends BaseValidatorClient {

  private static final String CONTEXT_ROOT = "/jsf_validator_regexvalidator_web";

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

  // ------------------------------------------------------------ Test methods

  /**
   * @testName: regexValidatorCtorTest
   * 
   * @assertion_ids: JSF:JAVADOC:2210; JSF:JAVADOC:2221
   * 
   * @test_Strategy: Validate regexValidator no-arg constructor.
   */
  public void regexValidatorCtorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "regexValidatorCtorTest");
    invoke();
  }

  /**
   * @testName: regexValidateNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:2216; JSF:JAVADOC:2223
   * 
   * @test_Strategy: Verify the following method call(s) throw a
   *                 NullPointerException.
   * 
   *                 validate(FacesContext, null, Object) validate(null,
   *                 UIComponent, Object)
   */
  public void regexValidateNPETest() throws EETest.Fault {
    TEST_PROPS.setProperty(APITEST, "regexValidateNPETest");
    invoke();
  }

  /**
   * @testName: regexValidateSetGetPatternTest
   * @assertion_ids: JSF:JAVADOC:2206; JSF:JAVADOC:2213
   * @test_Strategy: Validate that setPattern & getPattern methods function
   *                 correctly.
   * 
   * @since 2.2
   */
  public void regexValidateSetGetPatternTest() throws EETest.Fault {
    TEST_PROPS.setProperty(APITEST, "regexValidateSetGetPatternTest");
    invoke();
  }

  // ------------------------------------------------------- StateHolder Tests
  /**
   * @testName: stateHolderIsSetTransientTest
   * @assertion_ids: JSF:JAVADOC:2208; JSF:JAVADOC:2214
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
   * @assertion_ids: JSF:JAVADOC:2211; JSF:JAVADOC:2212
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
   * @assertion_ids: JSF:JAVADOC:2205; JSF:JAVADOC:2207; JSF:JAVADOC:2209
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
