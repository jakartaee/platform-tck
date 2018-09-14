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

package com.sun.ts.tests.el.api.javax_el.expression;

import java.util.Properties;

import javax.el.ELContext;
import javax.el.Expression;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.el.api.expression.ExpressionTest;
import com.sun.ts.tests.el.common.elcontext.SimpleELContext;
import com.sun.ts.tests.el.common.util.ELTestUtil;
import com.sun.ts.tests.el.common.util.ResolverType;

public class ELClient extends ServiceEETest {
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

  /**
   * Does nothing.
   * 
   * @throws Fault
   */
  public void cleanup() throws Fault {
    // does nothing at this point
  }

  /**
   * @testName: negativeEqualsTest
   * 
   * @assertion_ids: EL:JAVADOC:56
   * 
   * @test_Strategy: Validate the behavior of Expression API Expression.equals()
   * 
   *                 Verify that an Expression cannot equal null, and that a
   *                 ValueExpression and a MethodExpression cannot be equal.
   */

  public void negativeEqualsTest() throws Fault {

    StringBuffer buf = new StringBuffer();

    boolean pass = true;

    try {

      ExpressionFactory expFactory = ExpressionFactory.newInstance();
      ELContext context = (new SimpleELContext()).getELContext();

      // compare Expressions to null
      ValueExpression vexp1 = expFactory.createValueExpression(context,
          "${null}", Object.class);

      if (ExpressionTest.equalsTest(vexp1, null, buf)) {
        pass = false;
        buf.append("ValueExpression tested equal to null" + TestUtil.NEW_LINE);
      }

      MethodExpression mexp1 = expFactory.createMethodExpression(context,
          "null", null, new Class[] {});

      if (ExpressionTest.equalsTest(mexp1, null, buf)) {
        pass = false;
        buf.append("MethodExpression tested equal to null" + TestUtil.NEW_LINE);
      }

      // compare ValueExpressions to MethodExpressions
      ValueExpression vexp2 = expFactory.createValueExpression(context,
          "literal", Object.class);

      MethodExpression mexp2 = expFactory.createMethodExpression(context,
          "literal", null, new Class[] {});

      if (ExpressionTest.equalsTest(vexp2, mexp2, buf)) {
        pass = false;
        buf.append("ValueExpression tested equal to " + "MethodExpression"
            + TestUtil.NEW_LINE);
      }

      if (ExpressionTest.equalsTest(mexp2, vexp2, buf)) {
        pass = false;
        buf.append("MethodExpression tested equal to " + "ValueExpression"
            + TestUtil.NEW_LINE);
      }

    } catch (Exception ex) {
      throw new Fault(ex);
    }

    if (!pass)
      throw new Fault(ELTestUtil.FAIL + buf.toString());

    TestUtil.logTrace(buf.toString());

  }// End negativeEqualsTest

  /**
   * @testName: expressionHashCodeTest
   * 
   * @assertion_ids: EL:JAVADOC:59
   * 
   * @test_Strategy: Validate the if two objects are equal according to the
   *                 equals(Object) method, then calling the hashCode method on
   *                 each of the two objects must produce the same integer
   *                 result.
   * 
   */

  public void expressionHashCodeTest() throws Fault {
    SimpleELContext simpleContext = new SimpleELContext(
        ResolverType.VECT_ELRESOLVER);

    ExpressionFactory expFactory = ExpressionFactory.newInstance();
    ELContext context = simpleContext.getELContext();

    Class<?>[] paramTypes1 = { Object.class };

    String exprStr1 = "#{vect.add}";

    Expression mexp1 = expFactory.createMethodExpression(context, exprStr1,
        boolean.class, paramTypes1);

    Expression mexp2 = expFactory.createMethodExpression(context, exprStr1,
        boolean.class, paramTypes1);

    if (!mexp1.equals(mexp2)) {
      throw new Fault("Failed: equals check failed!");

    }

    if (!(mexp1.hashCode() == mexp2.hashCode())) {
      throw new Fault("Failed: hashCode check failed!");
    }

  }// End expressionHashCodeTest

}
