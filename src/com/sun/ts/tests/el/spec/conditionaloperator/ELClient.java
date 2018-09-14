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

package com.sun.ts.tests.el.spec.conditionaloperator;

import com.sun.javatest.Status;
import com.sun.ts.tests.el.common.util.ExprEval;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.el.common.util.NameValuePair;

import java.util.Properties;

public class ELClient extends ServiceEETest {

  Properties testProps;

  private final boolean[] deferred = { true, false };

  public static void main(String[] args) {
    ELClient theTests = new ELClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("Setup method called");
    this.testProps = p;
  }

  public void cleanup() throws Fault {
  }

  /*
   * @testName: elConditionalStringTest
   * 
   * @assertion_ids: EL:SPEC:26.1.1; EL:SPEC:26.1.2
   * 
   * @test_Strategy: Validate that if a String is passed with the conditional
   * operator, the type is coerced to Boolean and the operator is applied.
   *
   * Example Equation: ${true ? true : false}
   */
  public void elConditionalStringTest() throws Fault {

    this.testConditionals("true", true);
    this.testConditionals("false", false);

  }

  /*
   * @testName: elConditionalBooleanTest
   * 
   * @assertion_ids: EL:SPEC:26.1.1; EL:SPEC:26.1.2
   * 
   * @test_Strategy: Validate that if a Boolean is passed with the conditional
   * operator, that the operator is applied.
   *
   * Example Equation: ${true ? true : false}
   */
  public void elConditionalBooleanTest() throws Fault {

    this.testConditionals(true, true);
    this.testConditionals(false, false);

  }

  // ---------------------------------------------------------- private methods

  private void testConditionals(String testVal, boolean expectedResult)
      throws Fault {

    boolean pass = false;

    NameValuePair value[] = NameValuePair.buildConditionalNameValue(testVal,
        true, false);

    try {
      for (boolean tf : deferred) {
        String expr = ExprEval.buildElExpr(tf, "conditional");
        Object result = ExprEval.evaluateValueExpression(expr, value,
            Object.class);

        TestUtil.logTrace("result is " + result.toString());
        pass = (ExprEval.compareClass(result, Boolean.class)
            && ExprEval.compareValue((Boolean) result, expectedResult));

        if (!pass)
          throw new Fault("TEST FAILED: pass = false");
      }
    } catch (Exception e) {
      throw new Fault(e);
    }
  }

  private void testConditionals(boolean testVal, boolean expectedResult)
      throws Fault {

    boolean pass = false;

    NameValuePair value[] = NameValuePair.buildConditionalNameValue(testVal,
        true, false);

    try {
      for (boolean tf : deferred) {
        String expr = ExprEval.buildElExpr(tf, "conditional");
        Object result = ExprEval.evaluateValueExpression(expr, value,
            Object.class);

        TestUtil.logTrace("result is " + result.toString());
        pass = (ExprEval.compareClass(result, Boolean.class)
            && ExprEval.compareValue((Boolean) result, expectedResult));

        if (!pass)
          throw new Fault("TEST FAILED: pass = false");
      }
    } catch (Exception e) {
      throw new Fault(e);
    }
  }

}
