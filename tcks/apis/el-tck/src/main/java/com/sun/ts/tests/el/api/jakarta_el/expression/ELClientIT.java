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

package com.sun.ts.tests.el.api.jakarta_el.expression;

import java.util.Properties;



import com.sun.ts.tests.el.common.api.expression.ExpressionTest;
import com.sun.ts.tests.el.common.elcontext.SimpleELContext;
import com.sun.ts.tests.el.common.util.ELTestUtil;
import com.sun.ts.tests.el.common.util.ResolverType;

import jakarta.el.ELContext;
import jakarta.el.Expression;
import jakarta.el.ExpressionFactory;
import jakarta.el.MethodExpression;
import jakarta.el.ValueExpression;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.lang.System.Logger;

public class ELClientIT {

  private static final Logger logger = System.getLogger(ELClientIT.class.getName());

  @AfterEach
  public void cleanup() throws Exception {
    logger.log(Logger.Level.INFO, "Cleanup method called");
  }

  @BeforeEach
  void logStartTest(TestInfo testInfo) {
    logger.log(Logger.Level.INFO, "STARTING TEST : "+testInfo.getDisplayName());
  }

  @AfterEach
  void logFinishTest(TestInfo testInfo) {
    logger.log(Logger.Level.INFO, "FINISHED TEST : "+testInfo.getDisplayName());
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
  @Test
  public void negativeEqualsTest() throws Exception {

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
        buf.append("ValueExpression tested equal to null" + ELTestUtil.NL);
      }

      MethodExpression mexp1 = expFactory.createMethodExpression(context,
          "null", null, new Class[] {});

      if (ExpressionTest.equalsTest(mexp1, null, buf)) {
        pass = false;
        buf.append("MethodExpression tested equal to null" + ELTestUtil.NL);
      }

      // compare ValueExpressions to MethodExpressions
      ValueExpression vexp2 = expFactory.createValueExpression(context,
          "literal", Object.class);

      MethodExpression mexp2 = expFactory.createMethodExpression(context,
          "literal", null, new Class[] {});

      if (ExpressionTest.equalsTest(vexp2, mexp2, buf)) {
        pass = false;
        buf.append("ValueExpression tested equal to " + "MethodExpression"
            + ELTestUtil.NL);
      }

      if (ExpressionTest.equalsTest(mexp2, vexp2, buf)) {
        pass = false;
        buf.append("MethodExpression tested equal to " + "ValueExpression"
            + ELTestUtil.NL);
      }

    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass)
      throw new Exception(ELTestUtil.FAIL + buf.toString());

    logger.log(Logger.Level.TRACE, buf.toString());

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
  @Test
  public void expressionHashCodeTest() throws Exception {
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
      throw new Exception("Failed: equals check failed!");

    }

    if (!(mexp1.hashCode() == mexp2.hashCode())) {
      throw new Exception("Failed: hashCode check failed!");
    }

  }// End expressionHashCodeTest

}
