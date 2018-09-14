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

package com.sun.ts.tests.el.api.javax_el.methodexpression;

import java.util.Properties;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.MethodNotFoundException;
import javax.el.PropertyNotFoundException;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.el.api.expression.ExpressionTest;
import com.sun.ts.tests.el.common.elcontext.SimpleELContext;
import com.sun.ts.tests.el.common.util.ELTestUtil;
import com.sun.ts.tests.el.common.util.ResolverType;

public class ELClient extends ServiceEETest {

  private static final String NL = System.getProperty("line.seperator", "\n");

  private Properties testProps;

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
    // does nothing at this point
  }

  /**
   * @testName: positiveMethodExpressionTest
   * 
   * @assertion_ids: EL:JAVADOC:84; EL:JAVADOC:85; EL:JAVADOC:58; EL:JAVADOC:60
   * @test_Strategy: Validate the behavior of MethodExpression API
   *                 MethodExpression.getMethodInfo() MethodExpression.invoke()
   *                 Expression.isLiteralText() Expression.getExpressionString()
   */

  public void positiveMethodExpressionTest() throws Fault {

    StringBuffer buf = new StringBuffer();

    boolean pass1, pass2, pass3, pass4;

    try {
      ExpressionFactory expFactory = ExpressionFactory.newInstance();

      SimpleELContext simpleContext = new SimpleELContext(
          ResolverType.VECT_ELRESOLVER);
      ELContext context = simpleContext.getELContext();

      // case 1: non-null return value
      Object[] params1 = new Object[] { "new member" };
      Class<?>[] paramTypes1 = { Object.class };
      String exprStr1 = "#{vect.add}";

      MethodExpression mexp1 = expFactory.createMethodExpression(context,
          exprStr1, boolean.class, paramTypes1);
      pass1 = ExpressionTest.testMethodExpression(mexp1, context, exprStr1,
          params1, Boolean.TRUE, false, buf);

      // case 2: null return value
      Object[] params2 = new Object[] { Integer.valueOf(0), "new member" };
      Class<?>[] paramTypes2 = { int.class, Object.class };
      String exprStr2 = "#{vect.add}";

      MethodExpression mexp2 = expFactory.createMethodExpression(context,
          exprStr2, null, paramTypes2);
      pass2 = ExpressionTest.testMethodExpression(mexp2, context, exprStr2,
          params2, null, false, buf);

      // case 3: literal expression returning String
      Object[] params3 = null;
      Class<?>[] paramTypes3 = {};
      String exprStr3 = "true";

      MethodExpression mexp3 = expFactory.createMethodExpression(context,
          exprStr3, String.class, paramTypes3);
      pass3 = ExpressionTest.testMethodExpression(mexp3, context, exprStr3,
          params3, "true", true, buf);

      // case 4: literal expression returning non-String value
      Object[] params4 = null;
      Class<?>[] paramTypes4 = {};
      String exprStr4 = "true";

      MethodExpression mexp4 = expFactory.createMethodExpression(context,
          exprStr4, Boolean.class, paramTypes4);
      pass4 = ExpressionTest.testMethodExpression(mexp4, context, exprStr4,
          params4, Boolean.TRUE, true, buf);

    } catch (Exception ex) {
      throw new Fault(ex);
    }

    if (!(pass1 && pass2 && pass3 && pass4))
      throw new Fault(ELTestUtil.FAIL + buf.toString());
  }

  /**
   * @testName: negativeMethodExpressionTest
   * 
   * @assertion_ids: EL:JAVADOC:84; EL:JAVADOC:85; EL:JAVADOC:302;
   *                 EL:JAVADOC:306; EL:JAVADOC:303; EL:JAVADOC:307;
   *                 EL:JAVADOC:309; EL:JAVADOC:310; EL:JAVADOC:304;
   *                 EL:JAVADOC:308
   * 
   * @test_Strategy: Validate the behavior of MethodExpression API
   *                 MethodExpression.getMethodInfo() MethodExpression.invoke()
   */

  public void negativeMethodExpressionTest() throws Fault {

    boolean pass = true;

    ExpressionFactory expFactory = ExpressionFactory.newInstance();

    SimpleELContext simpleContext = new SimpleELContext(
        ResolverType.VECT_ELRESOLVER);
    ELContext context = simpleContext.getELContext();

    Object[] params = new Object[] { "new member" };
    Class<?>[] paramTypes = { Object.class };

    // case 1: NullPointerException. Null ELContext passed to methods.
    String exprStr1 = "#{vect.add}";

    MethodExpression mexp1 = expFactory.createMethodExpression(context,
        exprStr1, boolean.class, paramTypes);
    try {
      mexp1.getMethodInfo(null);
      pass = false;
      TestUtil.logErr("Call to getMethodInfo() with null ELContext "
          + "parameter did not" + NL);
      TestUtil.logErr(" cause an exception to be thrown" + NL);

    } catch (NullPointerException npe) {
      // do nothing test passed!
      TestUtil.logMsg("Expected Exception Thrown, when Null ELContext "
          + "passed to getMethodInfo(): " + npe.getClass().getSimpleName());

    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Call to getMethodInfo() with null ELContext "
          + "threw the wrong Exception!" + NL + "Expected: NullPointerException"
          + NL + "Received: " + e.toString() + NL);

      e.printStackTrace();
    }

    try {
      mexp1.invoke(null, params);
      pass = false;
      TestUtil.logErr(
          "Call to invoke() with null ELContext parameter did " + "not" + NL);
      TestUtil.logErr(" cause an exception to be thrown" + NL);

    } catch (NullPointerException npe) {
      // Do nothing test passed!
      TestUtil.logMsg("Expected Exception Thrown, when Null ELContext "
          + "passed to invoke(): " + npe.getClass().getSimpleName());

    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Call to invoke() with null ELContext "
          + "throw the wrong Exception!" + NL + "Expected: NullPointerException"
          + NL + "Received: " + e.toString() + NL);

      e.printStackTrace();
    }

    // case 2: MethodNotFoundException. Property found, but
    // method does not exist.
    String exprStr2 = "#{vect.noSuchMethod}";

    MethodExpression mexp2 = expFactory.createMethodExpression(context,
        exprStr2, boolean.class, paramTypes);
    try {
      mexp2.getMethodInfo(context);
      pass = false;
      TestUtil.logErr("Call to getMethodInfo() for non-existent method did "
          + "not cause" + NL);
      TestUtil.logErr(" an exception to be thrown" + NL);

    } catch (MethodNotFoundException mnfe) {
      // do nothing test passed!
      TestUtil.logMsg("Expected Exception Thrown, when method does not "
          + "exists for getMethodInfo(): " + mnfe.getClass().getSimpleName());

    } catch (Exception e) {
      pass = false;
      TestUtil.logErr(
          "Call to getMethodInfo() for non-existent method threw the wrong "
              + "exception!" + NL + "Expected: MethodNotFoundException" + NL
              + "Received: " + e.toString() + NL);

      e.printStackTrace();
    }

    try {
      mexp2.invoke(context, params);
      pass = false;
      TestUtil.logErr(
          "Call to invoke() for non-existent method did not " + "cause" + NL);
      TestUtil.logErr(" an exception to be thrown" + NL);

    } catch (MethodNotFoundException mnfe) {
      // do thing test passed!
      TestUtil.logMsg("Expected Exception Thrown, when method does not "
          + "exists for invoke(): " + mnfe.getClass().getSimpleName());

    } catch (Exception e) {
      pass = false;
      TestUtil.logErr(
          "Call to getMethodInfo() for non-existent method threw the wrong "
              + "exception!" + NL + "Expected: MethodNotFoundException" + NL
              + "Received: " + e.toString() + NL);

      e.printStackTrace();
    }

    // case 3: PropertyNotFoundException: No such property in ELContext.
    String exprStr3 = "#{wect.add}";

    MethodExpression mexp3 = expFactory.createMethodExpression(context,
        exprStr3, boolean.class, paramTypes);

    try {
      mexp3.getMethodInfo(context);
      pass = false;
      TestUtil.logErr("Call to getMethodInfo() for non-existent property "
          + "did not cause" + NL);
      TestUtil.logErr(" an exception to be thrown" + NL);

    } catch (PropertyNotFoundException pnfe) {
      // Do nothing test passed!
      TestUtil.logMsg(
          "Expected Exception Thrown, No such property in " + "ELContext"
              + "for getMethodInfo(): " + pnfe.getClass().getSimpleName());

    } catch (Exception e) {
      pass = false;
      TestUtil.logErr(
          "Call to getMethodInfo() for non-existent property threw the wrong "
              + "exception!" + NL + "Expected: PropertyNotFoundException" + NL
              + "Received: " + e.toString() + NL);

      e.printStackTrace();
    }

    try {
      mexp3.invoke(context, params);
      pass = false;
      TestUtil.logErr("Call to invoke() for non-existent property did " + "not "
          + "cause" + NL);
      TestUtil.logErr(" an exception to be thrown" + NL);

    } catch (PropertyNotFoundException pnfe) {
      // Do nothing test passed!
      TestUtil.logMsg("Expected Exception Thrown, No such property in "
          + "ELContext" + "for invoke(): " + pnfe.getClass().getSimpleName());

    } catch (Exception e) {
      pass = false;
      TestUtil.logErr(
          "Call to invoke() for non-existent property threw " + "the wrong "
              + "exception!" + NL + "Expected: PropertyNotFoundException" + NL
              + "Received: " + e.toString() + NL);

      e.printStackTrace();
    }

    // case 4: ELException: Call to invoke() with string literal.
    // Expected return type is void.
    String exprStr4 = "literal";

    MethodExpression mexp4 = expFactory.createMethodExpression(context,
        exprStr4, void.class, paramTypes);

    try {
      mexp4.invoke(context, params);
      pass = false;
      TestUtil.logErr(
          "Call to invoke() for string literal with expected " + "return" + NL);
      TestUtil.logErr(
          " value of void did not cause an exception to be " + "thrown" + NL);

    } catch (ELException ee) {
      // Do nothing test passed!
      TestUtil.logMsg("Expected Exception Thrown. Call to invoke() with "
          + "string literal ELContext for invoke()." + NL
          + "Expected return type is void: " + ee.getClass().getSimpleName());

    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Call to invoke() for string literal with expected return"
          + NL + "value of void caused the wrong exception to be thrown!" + NL
          + "Expected: ELException: " + NL + "Received: " + e.toString());

      e.printStackTrace();
    }

    // case 5: ELException: Call to invoke() with string literal.
    // Expected return type can't be coerced.
    String exprStr5 = "literal";

    MethodExpression mexp5 = expFactory.createMethodExpression(context,
        exprStr5, Double.class, paramTypes);

    try {
      mexp5.invoke(context, params);
      pass = false;
      TestUtil.logErr("Call to invoke() for string literal with "
          + "non-coercable expected " + NL);
      TestUtil.logErr(
          "return value did not cause an exception to be " + "thrown" + NL);

    } catch (ELException ee) {
      // Do nothing test passed!
      TestUtil.logMsg("Expected Exception Thrown. Call to invoke() with "
          + "string literal ELContext for " + "invoke()." + NL
          + "Expected return type can't be coerced: "
          + ee.getClass().getSimpleName());

    } catch (Exception e) {
      pass = false;
      TestUtil.logErr(
          "Call to invoke() for string literal with non-coercable expected "
              + NL + "return value caused the wrong exception to be thrown!"
              + NL + "Expected: ELException: " + NL + "Received: "
              + e.toString());

      e.printStackTrace();
    }

    if (!pass)
      throw new Fault(ELTestUtil.FAIL);

  }

  /*
   * @testName: methodExpressionSerializableTest
   * 
   * @assertion_ids: EL:SPEC:44
   * 
   * @test_Strategy: Validate that MethodExpression implements Serializable and
   * that a MethodExpression can be manually serialized and deserialized.
   */
  public void methodExpressionSerializableTest() throws Fault {

    StringBuffer buf = new StringBuffer();
    Class<?>[] paramTypes = { Object.class };
    String exprStr = "#{vect.add}";

    boolean pass = true;

    try {

      ExpressionFactory expFactory = ExpressionFactory.newInstance();
      ELContext context = (new SimpleELContext()).getELContext();

      // Setup eval-expression
      MethodExpression evalmexp = expFactory.createMethodExpression(context,
          exprStr, Boolean.class, paramTypes);
      TestUtil.logTrace(
          "Eval Method Expression For Testing: " + evalmexp.toString() + NL);

      // Setup literal-expression
      MethodExpression literalmexp = expFactory.createMethodExpression(context,
          "vect.add", Boolean.class, paramTypes);
      TestUtil.logTrace("Literal Method Expression For Testing: "
          + literalmexp.toString() + NL);

      // Test both eval & literal expressions.
      if (!(ExpressionTest.expressionSerializableTest(evalmexp, buf)
          && ExpressionTest.expressionSerializableTest(literalmexp, buf))) {
        pass = false;
      }

    } catch (Exception ex) {
      throw new Fault(ex);

    }

    if (!pass)
      throw new Fault(ELTestUtil.FAIL + buf.toString());
    else
      TestUtil.logTrace(buf.toString());
  }
}
