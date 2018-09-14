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

package com.sun.ts.tests.jsf.api.javax_faces.validator.methodexpressionvalidator;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.jsf.api.javax_faces.validator.common.BaseValidatorClient;

public class URLClient extends BaseValidatorClient {

  private static final String CONTEXT_ROOT = "/jsf_validator_methodvalidator_web";

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
   * @testName: methodExpressionValidatorCtor1Test
   * 
   * @assertion_ids: JSF:JAVADOC:2195; JSF:JAVADOC:2221
   * 
   * @test_Strategy: Validate MethodExpressionValidator no-arg constructor.
   */
  public void methodExpressionValidatorCtor1Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "methodExpressionValidatorCtor1Test");
    invoke();
  }

  /**
   * @testName: methodExpressionValidatorCtor2Test
   * 
   * @assertion_ids: JSF:JAVADOC:2196
   * 
   * @test_Strategy: Validate MethodExpressionValidator(MethodExpression)
   *                 constructor.
   */
  public void methodExpressionValidatorCtor2Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "methodExpressionValidatorCtor2Test");
    invoke();
  }

  /**
   * @testName: methodExpressionValidateNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:2204
   * 
   * @test_Strategy: Verify the following method calls throw a
   *                 NullPointerException.
   * 
   *                 MethodExpression.validate(FacesContext, null, Object)
   *                 MethodExpression.validate(null, UIComponent, Object)
   */
  public void methodExpressionValidateNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "methodExpressionValidateNPETest");
    invoke();
  }

  // ------------------------------------------------------- StateHolder Tests
  /**
   * @testName: stateHolderIsSetTransientTest
   * @assertion_ids: JSF:JAVADOC:2194; JSF:JAVADOC:2201
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
   * @assertion_ids: JSF:JAVADOC:2198
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
   * @assertion_ids: JSF:JAVADOC:2200
   * 
   * @test_Strategy: Validate that a NullPointer is thrown if context is null.
   * 
   * @since: 2.2
   */
  public void stateHolderSaveStateNPETest() throws Fault {
    super.stateHolderSaveStateNPETest();
  }

}
