/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates and others.
 * All rights reserved.
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

package com.sun.ts.tests.el.spec.mapper;

import com.sun.ts.tests.common.base.ServiceEETest;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.el.common.elcontext.FuncMapperELContext;
import com.sun.ts.tests.el.common.elcontext.VarMapperELContext;
import com.sun.ts.tests.el.common.functionmapper.TCKFunctionMapper;
import com.sun.ts.tests.el.common.util.ExprEval;
import jakarta.el.ELException;
import jakarta.el.ExpressionFactory;
import jakarta.el.ValueExpression;
import jakarta.el.VariableMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.lang.System.Logger;
import java.util.Properties;

@Tag("el")
@Tag("platform")
@Tag("web")
@Tag("tck-javatest")
public class ELClientTest extends ServiceEETest {

  private static final Logger logger = System.getLogger(ELClientTest.class.getName());

  @BeforeEach
  void logStartTest(TestInfo testInfo) {
      logger.log(Logger.Level.INFO, "STARTING TEST : " + testInfo.getDisplayName());
  }

  @AfterEach
  void logFinishTest(TestInfo testInfo) {
      logger.log(Logger.Level.INFO, "FINISHED TEST : " + testInfo.getDisplayName());
  }

  Properties testProps = System.getProperties();

  public static void main(String[] args) {
    ELClientTest theTests = new ELClientTest();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Exception {
    TestUtil.logTrace("Setup method called");
    this.testProps = p;
  }

  public void cleanup() throws Exception {
  }

  /**
   * @testName: ELFunctionBindingTest
   *
   * @assertion_ids: EL:SPEC:31
   * @test_Strategy: [ELFunctionsBinding] Create an ELContext that uses a
   *                 non-null FunctionMapper. Create a ValueExpression from the
   *                 ELContext that references a function assigned by the
   *                 FunctionMapper. Change the function mapping in the
   *                 FunctionMapper. Show that the ValueExpression evaluates as
   *                 before and that the original method mapped to the function
   *                 is being invoked.
   */
  @Test
  public void ELFunctionBindingTest() throws Exception {

    Class expectedClass = Integer.class;
    Integer expectedValue = Integer.valueOf(10);

    // Create ValueExpressions from a function mapped by the
    // FunctionMapper: Int:val() -> Integer.valueOf(String s)
    ExpressionFactory expFactory = ExpressionFactory.newInstance();
    FuncMapperELContext context = new FuncMapperELContext();
    ValueExpression vexp1 = expFactory.createValueExpression(context,
        "${Int:val(10)}", Object.class);
    ValueExpression vexp2 = expFactory.createValueExpression(context,
        "${Int:val(\"string\")}", Object.class);

    // Make sure the expression evaluates as we expect
    Object initialResult = vexp1.getValue(context);
    if (!ExprEval.compareClass(initialResult, expectedClass)) {
      throw new Exception("Wrong class for initial Expression evaluation");
    }
    if (!ExprEval.compareValue(initialResult, expectedValue)) {
      throw new Exception("Wrong value for initial Expression evaluation");
    }

    // Get the FunctionMapper and change the method mapped to Int:val
    TCKFunctionMapper mapper = (TCKFunctionMapper) context.getFunctionMapper();
    mapper.update();

    // Now re-evaluate the expression; results should be the same
    Object finalResult = vexp1.getValue(context);
    if (!ExprEval.compareClass(finalResult, expectedClass)) {
      throw new Exception("Wrong class for final Expression evaluation");
    }
    if (!ExprEval.compareValue(finalResult, expectedValue)) {
      throw new Exception("Wrong value for final Expression evaluation");
    }

    // Verify the method bound to the function behaves as expected for
    // invalid arguments
    try {
      vexp2.getValue(context);

    } catch (Throwable t) {
      if (!(t instanceof ELException)) {
        TestUtil.logErr("Expected ELException to be thrown");
        TestUtil.logErr("instead threw " + t.toString());
        TestUtil.printStackTrace(t);
        throw new Exception("ELException not thrown");
      }
      Throwable cause = t.getCause();
      if (!(cause instanceof NumberFormatException)) {
        TestUtil.logErr("Expected cause to be NumberFormatException");
        TestUtil.logErr("instead cause is " + cause.toString());
        TestUtil.printStackTrace(cause);
        throw new Exception("NumberFormatException not cause");
      }
    }
  }

  /**
   * @testName: ELVariableBindingTest
   *
   * @assertion_ids: EL:SPEC:34
   * @test_Strategy: [ELVariablesBinding] Create an ELContext that uses a
   *                 non-null VariableMapper. Create a ValueExpression from the
   *                 ELContext that references a variable assigned by the
   *                 VariableMapper. Remove the assignment from the
   *                 VariableMapper. Verify that the ValueExpression evaluates
   *                 as before.
   */
  @Test
  public void ELVariableBindingTest() throws Exception {

    Class expectedClass = Double.class;
    Double expectedValue = Double.valueOf(10.0);

    // Create a ValueExpression and use it to set a variable
    ExpressionFactory expFactory = ExpressionFactory.newInstance();
    VarMapperELContext context = new VarMapperELContext(testProps);
    VariableMapper varMapper = context.getVariableMapper();
    ValueExpression vexp1 = expFactory.createValueExpression(context, "#{1e1}",
        Object.class);
    varMapper.setVariable("ten", vexp1);

    // Make sure the variable evaluates as we expect
    ValueExpression vexp2 = varMapper.resolveVariable("ten");
    Object initialResult = vexp2.getValue(context);
    if (!ExprEval.compareClass(initialResult, expectedClass)) {
      throw new Exception("Wrong class for initial Expression evaluation");
    }
    if (!ExprEval.compareValue(initialResult, expectedValue)) {
      throw new Exception("Wrong value for initial Expression evaluation");
    }

    // Remove the variable assignment
    varMapper.setVariable("ten", null);

    // Now re-evaluate the variable; results should be the same
    Object finalResult = vexp2.getValue(context);
    if (!ExprEval.compareClass(finalResult, expectedClass)) {
      throw new Exception("Wrong class for final Expression evaluation");
    }
    if (!ExprEval.compareValue(finalResult, expectedValue)) {
      throw new Exception("Wrong value for final Expression evaluation");
    }

  }
}
