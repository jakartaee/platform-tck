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

package com.sun.ts.tests.el.api.jakarta_el.expressionfactory;

import java.util.Properties;



import com.sun.ts.tests.el.common.elcontext.SimpleELContext;
import com.sun.ts.tests.el.common.util.ELTestUtil;
import com.sun.ts.tests.el.common.util.ExprEval;

import jakarta.el.ELContext;
import jakarta.el.ELException;
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
   * @testName: newInstanceTest
   * 
   * @assertion_ids: EL:JAVADOC:119; EL:JAVADOC:120
   * @test_Strategy: Verify that an ExpressionFactory can be instantiated with
   *                 the newInstance() API.
   */
  @Test
  public void newInstanceTest() throws Exception {

    try {
      ExpressionFactory.newInstance();
      ExpressionFactory.newInstance(null);

      Properties props = new Properties();
      props.setProperty("jakarta.el.cacheSize", "128M");
      ExpressionFactory.newInstance(props);

    } catch (Exception ex) {
      throw new Exception(ex);
    }
  }

  /**
   * @testName: createValueExpressionTest
   * 
   * @assertion_ids: EL:JAVADOC:63
   * @test_Strategy: Verify that the ExpressionFactory can handle the types of
   *                 input specified in the javadoc when invoking the
   *                 createValueExpression(ELContext, String, Class) method.
   */
  @Test
  public void createValueExpressionTest() throws Exception {

    boolean pass = true;

    String[] exprStr = {

        // $ delimiter
        "${employee.lastname}",

        // # delimiter
        "#{employee.lastname}",

        // literal text
        "John Doe",

        // multiple expressions using the same delimiter
        "${employee.firstname}${employee.lastname}",
        "#{employee.firstname}#{employee.lastname}",

        // mixed literal text and expressions using the same delimiter
        "Name: ${employee.firstname}${employee.lastname}",
        "Name: #{employee.firstname}#{employee.lastname}"

    };

    ValueExpression vexp = null;

    try {
      ExpressionFactory expFactory = ExpressionFactory.newInstance();
      SimpleELContext context = new SimpleELContext();

      for (int i = 0; i < exprStr.length; ++i) {
        try {
          vexp = expFactory.createValueExpression(context, exprStr[i],
              Object.class);
        } catch (NullPointerException npe) {
          pass = false;
          logger.log(Logger.Level.ERROR, ELTestUtil.FAIL + exprStr[i]);
          ELTestUtil.printStackTrace(npe);
          continue;
        } catch (ELException ee) {
          pass = false;
          logger.log(Logger.Level.ERROR, ELTestUtil.FAIL + exprStr[i]);
          ELTestUtil.printStackTrace(ee);
          continue;
        }

        if (!vexp.getExpressionString().equals(exprStr[i])) {
          pass = false;
          logger.log(Logger.Level.ERROR, "Failed. Expression string mismatch.");
          logger.log(Logger.Level.ERROR, "Expected: " + exprStr[i]);
          logger.log(Logger.Level.ERROR, "Received: " + vexp.getExpressionString());
        }
      }
    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass)
      throw new Exception(ELTestUtil.FAIL);
  }

  /**
   * @testName: createValueExpression2Test
   * 
   * @assertion_ids: EL:JAVADOC:64
   * @test_Strategy: Verify the functionality of the
   *                 createValueExpression(Object, Class) method.
   */
  @Test
  public void createValueExpression2Test() throws Exception {

    boolean pass = true;

    // test cases mix coercable and non-coercable object/type
    // instances
    ObjectAndType[] testCases = {
        new ObjectAndType("some string", String.class),
        new ObjectAndType(null, String.class),
        new ObjectAndType(Integer.valueOf(1), String.class),
        new ObjectAndType(Double.valueOf(1.5d), Integer.class),
        new ObjectAndType("10000", Long.class),
        new ObjectAndType(Integer.valueOf(1), Boolean.class),
        new ObjectAndType(Boolean.TRUE, Character.class),
        new ObjectAndType(Integer.valueOf(1), Class.class) };

    ExpressionFactory expFactory = ExpressionFactory.newInstance();

    for (int i = 0; i < testCases.length; ++i) {
      try {
        expFactory.createValueExpression(testCases[i].obj, testCases[i].type);
      } catch (Exception e) {
        pass = false;
        logger.log(Logger.Level.ERROR, "Exception: Test Case " + i);
        ELTestUtil.printStackTrace(e);
      }
    }

    if (!pass)
      throw new Exception(ELTestUtil.FAIL);
  }

  /**
   * @testName: createValueExpressionELExceptionTest
   * 
   * @assertion_ids: EL:JAVADOC:63
   * @test_Strategy: Verify that
   *                 ExpressionFactory.createValueExpression(ELContext, String,
   *                 Class) throws an ELException for mixed delimiter
   *                 expressions and expressions with syntactical errors.
   */
  @Test
  public void createValueExpressionELExceptionTest() throws Exception {

    boolean pass = true;

    String[] exprStr = {

        // $ delimiter with missing bracket
        "${employee.lastname",

        // # delimiter with incomplete [] operation
        "#{employee[lastname}",

        // invalid operation
        "${ 5 ! 3 }",

        // binary operation with missing operand
        "${ 5 + }",

        // multiple expressions with mixed delimiters
        "${employee.firstname}#{employee.lastname}",
        "#{employee.firstname}${employee.lastname}",

        // mixed literal text and expressions with mixed delimiters
        "Name: ${employee.firstname}#{employee.lastname}",
        "Name: #{employee.firstname}${employee.lastname}"

    };

    try {
      ExpressionFactory expFactory = ExpressionFactory.newInstance();
      SimpleELContext context = new SimpleELContext();

      for (int i = 0; i < exprStr.length; ++i) {
        try {
          expFactory.createValueExpression(context, exprStr[i], Object.class);
        } catch (ELException ee) {
          continue;
        } catch (NullPointerException npe) {
          pass = false;
          logger.log(Logger.Level.ERROR, ELTestUtil.FAIL + exprStr[i]);
          ELTestUtil.printStackTrace(npe);
          continue;
        }
        pass = false;
        logger.log(Logger.Level.ERROR, "Failed. No ELException thrown when calling");
        logger.log(Logger.Level.ERROR, "createValueExpression() with parameter " + exprStr[i]);
      }
    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass)
      throw new Exception(ELTestUtil.FAIL);
  }

  /**
   * @testName: createMethodExpressionTest
   * 
   * @assertion_ids: EL:JAVADOC:62
   * @test_Strategy: Verify that the ExpressionFactory can handle the types of
   *                 input specified in the javadoc when invoking the
   *                 createMethodExpression(ELContext, String, Class) method,
   *                 with the restriction that only expressions that share the
   *                 same syntax as an lvalue are allowed (EL Spec 1.2.1.2).
   */
  @Test
  public void createMethodExpressionTest() throws Exception {

    boolean pass = true;

    String[] exprStr = {

        // $ delimiter
        "${add}", "${vect.add}", "${vect[add]}",

        // # delimiter
        "#{add}", "#{vect.add}", "#{vect[add]}",

        // literal text
        "add" };

    MethodExpression mexp = null;
    Class<?>[] paramTypes = {};

    try {
      ExpressionFactory expFactory = ExpressionFactory.newInstance();
      SimpleELContext context = new SimpleELContext();

      for (int i = 0; i < exprStr.length; ++i) {
        try {
          mexp = expFactory.createMethodExpression(context, exprStr[i],
              Object.class, paramTypes);
        } catch (NullPointerException npe) {
          pass = false;
          logger.log(Logger.Level.ERROR, ELTestUtil.FAIL + exprStr[i]);
          ELTestUtil.printStackTrace(npe);
          continue;
        } catch (ELException ee) {
          pass = false;
          logger.log(Logger.Level.ERROR, ELTestUtil.FAIL + exprStr[i]);
          ELTestUtil.printStackTrace(ee);
          continue;
        }

        if (!mexp.getExpressionString().equals(exprStr[i])) {
          pass = false;
          logger.log(Logger.Level.ERROR, "Failed. Expression string mismatch.");
          logger.log(Logger.Level.ERROR, "Expected: " + exprStr[i]);
          logger.log(Logger.Level.ERROR, "Received: " + mexp.getExpressionString());
        }
      }
    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass)
      throw new Exception(ELTestUtil.FAIL);
  }

  /**
   * @testName: createMethodExpressionELExceptionTest
   * 
   * @assertion_ids: EL:JAVADOC:63; EL:JAVADOC:253
   * 
   * @test_Strategy: Verify that ExpressionFactory.createMethodExpression()
   *                 throws an ELException for expressions with syntactical
   *                 errors, and for expressions that are not lvalues.
   */
  @Test
  public void createMethodExpressionELExceptionTest() throws Exception {

    boolean pass = true;

    String[] exprStr = {

        // $ delimiter with missing bracket
        "${vect.add",

        // # delimiter with incomplete [] operation
        "#{vect[add}",

        // Expressions that are not lvalues
        // constant value
        "${ 5 }",

        // unary operation
        "${ -A }",

        // binary operation
        "${ A + B }",

        // multiple expressions
        "${vect.remove}${vect.add}", "#{vect.remove}#{vect.add}",
        "${vect.remove}#{vect.add}", "#{vect.remove}${vect.add}"

    };

    Class<?>[] paramTypes = {};

    try {
      ExpressionFactory expFactory = ExpressionFactory.newInstance();
      SimpleELContext context = new SimpleELContext();

      for (int i = 0; i < exprStr.length; ++i) {
        try {
          expFactory.createMethodExpression(context, exprStr[i], Object.class,
              paramTypes);
        } catch (ELException ee) {
          continue;
        } catch (NullPointerException npe) {
          pass = false;
          logger.log(Logger.Level.ERROR, ELTestUtil.FAIL + exprStr[i]);
          ELTestUtil.printStackTrace(npe);
          continue;
        }
        pass = false;
        logger.log(Logger.Level.ERROR, "Failed. No ELException thrown when calling");
        logger.log(Logger.Level.ERROR, "createMethodExpression() with parameter " + exprStr[i]);
      }

    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass)
      throw new Exception(ELTestUtil.FAIL);
  }

  /**
   * @testName: createExpressionNPETest
   * 
   * @assertion_ids: EL:JAVADOC:62; EL:JAVADOC:63; EL:JAVADOC:64;
   *                 EL:JAVADOC:253; EL:JAVADOC:254; EL:JAVADOC:256
   * 
   * @test_Strategy: Verify that ExpressionFactory.createValueExpression() and
   *                 ExpressionFactory.createMethodExpression() throw a
   *                 NullPointerException under the conditions stated in the
   *                 javadoc.
   */
  @Test
  public void createExpressionNPETest() throws Exception {
    ExpressionFactory expFactory = ExpressionFactory.newInstance();
    SimpleELContext context = new SimpleELContext();

    logger.log(Logger.Level.INFO, "Testing: ELContext.createValueExpression(context, "
        + "function, null)");
    ELTestUtil.checkForNPE(expFactory, "createValueExpression",
        new Class<?>[] { ELContext.class, String.class, Class.class },
        new Object[] { context, "function", null });

    logger.log(Logger.Level.INFO, 
        "Testing: ELContext.createValueExpression(instance, " + "null)");
    ELTestUtil.checkForNPE(expFactory, "createValueExpression",
        new Class<?>[] { Object.class, Class.class },
        new Object[] { "function", null });

    logger.log(Logger.Level.INFO, "Testing: ELContext.createMethodExpression(context, "
        + "instance, returnTypes, null)");
    ELTestUtil.checkForNPE(expFactory, "createMethodExpression",
        new Class<?>[] { ELContext.class, String.class, Class.class,
            Class[].class },
        new Object[] { context, "${foo}", Object.class, null });

  }

  /**
   * @testName: coerceToTypeTest
   * 
   * @assertion_ids: EL:JAVADOC:61
   * @test_Strategy: Verify that the coerceToType() method coerces an object to
   *                 a specific type according to the EL type conversion rules.
   */
  @Test
  public void coerceToTypeTest() throws Exception {

    boolean pass = true;

    ObjectAndType[] testCases = {
        new ObjectAndType("some string", String.class),
        new ObjectAndType(null, String.class),
        new ObjectAndType(Integer.valueOf(1), String.class),
        new ObjectAndType(Double.valueOf(1.5d), String.class),
        new ObjectAndType(Boolean.FALSE, String.class),
        new ObjectAndType(Double.valueOf(1.5d), Integer.class),
        new ObjectAndType(Integer.valueOf(1), Float.class),
        new ObjectAndType("10000", Long.class),
        new ObjectAndType("no value", Boolean.class),
        new ObjectAndType(null, null) };

    ExpressionFactory expFactory = ExpressionFactory.newInstance();

    for (int i = 0; i < testCases.length; ++i) {
      try {
        Object coercedObj = expFactory.coerceToType(testCases[i].obj,
            testCases[i].type);
        if (coercedObj == null) {
          if (!(testCases[i].type == null || testCases[i].obj == null)) {
            pass = false;
            logger.log(Logger.Level.ERROR, "Failed: Test Case " + i);
          }

        } else if (!ExprEval.compareClass(coercedObj, testCases[i].type)) {
          pass = false;
          logger.log(Logger.Level.ERROR, "Failed: Test Case " + i);
        }
      } catch (Exception e) {
        pass = false;
        logger.log(Logger.Level.ERROR, "Exception: Test Case " + i);
        ELTestUtil.printStackTrace(e);
      }
    }

    if (!pass)
      throw new Exception(ELTestUtil.FAIL);
  }

  /**
   * @testName: coerceToTypeELExceptionTest
   * 
   * @assertion_ids: EL:JAVADOC:61; EL:JAVADOC:251
   * 
   * @test_Strategy: Verify that the coerceToType() method throws an ELException
   *                 for invalid type conversions.
   */
  @Test
  public void coerceToTypeELExceptionTest() throws Exception {

    boolean pass = true;

    ObjectAndType[] testCases = {
        new ObjectAndType(Integer.valueOf(1), Boolean.class),
        new ObjectAndType(Boolean.FALSE, Long.class),
        new ObjectAndType(Boolean.TRUE, Character.class),
        new ObjectAndType(true, Float.class),
        new ObjectAndType("non-numeric string", Long.class),
        new ObjectAndType(Integer.valueOf(1), Class.class) };

    ExpressionFactory expFactory = ExpressionFactory.newInstance();

    for (int i = 0; i < testCases.length; ++i) {
      try {
        expFactory.coerceToType(testCases[i].obj, testCases[i].type);
        pass = false;
        logger.log(Logger.Level.ERROR, "Test Case " + i + " did not cause an exception");
      } catch (ELException elx) {
        pass = true;

      } catch (Exception e) {
        pass = false;
        logger.log(Logger.Level.ERROR, "Test Case " + i + " threw an exception");
        logger.log(Logger.Level.ERROR, "but it was not an ELException");
        ELTestUtil.printStackTrace(e);
      }
    }

    if (!pass)
      throw new Exception(ELTestUtil.FAIL);
  }

  private static class ObjectAndType {

    public Object obj;

    public Class<?> type;

    public ObjectAndType(Object obj, Class<?> type) {
      this.obj = obj;
      this.type = type;
    }
  }
}
