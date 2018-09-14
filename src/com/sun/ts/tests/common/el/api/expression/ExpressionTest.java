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

package com.sun.ts.tests.common.el.api.expression;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.el.Expression;
import javax.el.MethodExpression;
import javax.el.MethodInfo;
import javax.el.ValueExpression;
import javax.el.ELContext;
import javax.el.PropertyNotWritableException;

public class ExpressionTest {

  // Set New Line from system prop, if not defined used "\n as default."
  private static final String NLINE = System.getProperty("line.separator",
      "\n");

  private ExpressionTest() {
  }

  public static boolean testMethodInfo(MethodInfo minfo, String expectedName,
      Class expectedReturnType, int expectedNumParams,
      Class[] expectedParamTypes, StringBuffer buf) {

    boolean pass = true;
    String name = minfo.getName();
    Class returnType = minfo.getReturnType();
    Class[] paramTypes = minfo.getParamTypes();
    int numParams = paramTypes.length;

    if (!name.equals(expectedName)) {
      buf.append("Did not get expected method name." + NLINE);
      buf.append("Expected name = " + expectedName + NLINE);
      buf.append("Computed name = " + name + NLINE);
      pass = false;
    }
    if (!returnType.equals(expectedReturnType)) {
      buf.append("Did not get expected return type." + NLINE);
      buf.append(
          "Expected return type = " + expectedReturnType.getName() + NLINE);
      buf.append("Computed return type = " + returnType.getName() + NLINE);
      pass = false;
    }
    if (numParams != expectedNumParams) {
      buf.append("Did not get expected number of parameters." + NLINE);
      buf.append(
          "Expected number of parameters = " + expectedNumParams + NLINE);
      buf.append("Computed number of parameters = " + numParams + NLINE);
      pass = false;
    } else {
      for (int i = 0; i < numParams; ++i) {
        if (!(paramTypes[i].equals(expectedParamTypes[i]))) {
          buf.append("Did not get expected parameter type." + NLINE);
          buf.append("Expected parameter type = "
              + expectedParamTypes[i].getName() + NLINE);
          buf.append(
              "Computed parameter type = " + paramTypes[i].getName() + NLINE);
          pass = false;
        }
      }
    }
    return pass;
  }

  public static boolean testValueExpression(ValueExpression vexp,
      ELContext context, String exprStr, Class expectedType,
      Object expectedValue, boolean expectedReadOnly,
      boolean expectedLiteralText, StringBuffer buf) {

    boolean pass = true;

    // getValue()
    Object retrievedValue = vexp.getValue(context);
    if (!retrievedValue.equals(expectedValue)) {
      pass = false;
      buf.append("getValue() does not return expected value" + NLINE);
      buf.append("Expected value = " + expectedValue.toString() + NLINE);
      buf.append("Computed value = " + retrievedValue.toString() + NLINE);
    }

    // setValue()
    try {
      vexp.setValue(context, "blue");
      String newValue = (String) vexp.getValue(context);
      if (expectedReadOnly) {
        pass = false;
        buf.append("setValue() succeeded on a read-only value" + NLINE);
      } else if (!newValue.equals("blue")) {
        pass = false;
        buf.append(
            "Did not get correct set value for " + "ValueExpression." + NLINE);
        buf.append("Expected value = " + "blue" + NLINE);
        buf.append("Computed value = " + newValue + NLINE);
      }
    } catch (PropertyNotWritableException pnwe) {
      if (!expectedReadOnly) {
        pass = false;
        buf.append(
            "setValue() threw " + "PropertyNotWritableException" + NLINE);
        buf.append("on a writable value" + NLINE);
      } else {
        buf.append(
            "PropertyNotWritableException caught as " + "expected." + NLINE);
      }
    }

    // getType()
    Class type = vexp.getType(context);
    String typeName = (type == null) ? "null" : type.getName();
    buf.append("Type retrieved is " + typeName + NLINE);

    // getExpectedType()
    Class retrievedType = vexp.getExpectedType();
    if (!(retrievedType.equals(expectedType))) {
      pass = false;
      buf.append(
          "getExpectedType() does not return expected " + "type" + NLINE);
      buf.append("Expected type = " + expectedType.toString() + NLINE);
      buf.append("Computed type = " + retrievedType.toString() + NLINE);
    }

    // isReadOnly()
    if ((vexp.isReadOnly(context)) != expectedReadOnly) {
      pass = false;
      buf.append("isReadOnly() did not return " + expectedReadOnly + NLINE);
    }

    // isLiteralText()
    if ((vexp.isLiteralText()) != expectedLiteralText) {
      pass = false;
      buf.append(
          "isLiteralText() did not return " + expectedLiteralText + NLINE);
    }

    // getExpressionString()
    String retrievedStr = vexp.getExpressionString();
    if (!retrievedStr.equals(exprStr)) {
      pass = false;
      buf.append(
          "getExpressionString() does not return expected " + "string" + NLINE);
      buf.append("Expected string = " + exprStr + NLINE);
      buf.append("Computed string = " + retrievedStr + NLINE);
    }

    return pass;
  }

  public static boolean testMethodExpression(MethodExpression mexp,
      ELContext context, String exprStr, Object[] params,
      Object expectedReturnValue, boolean expectedLiteralText,
      StringBuffer buf) {

    boolean pass = true;

    // getMethodInfo()
    try {
      MethodInfo minfo = mexp.getMethodInfo(context);
    } catch (Exception e) {
      pass = false;
      buf.append("getMethodInfo() threw an unexpected exception" + NLINE);
      buf.append(e.getMessage() + NLINE);
    }

    // invoke()
    try {
      Object returnValue = mexp.invoke(context, params);
      if (returnValue == null) {
        if (expectedReturnValue != null) {
          pass = false;
          buf.append("invoke() unexpectedly returned null" + NLINE);
        }
      } else if (expectedReturnValue == null) {
        pass = false;
        buf.append(
            "invoke() unexpectedly returned non-null " + "value" + NLINE);
      } else if (!returnValue.equals(expectedReturnValue)) {
        pass = false;
        buf.append(
            "invoke() returned an object of wrong type or " + "value" + NLINE);
        buf.append(
            "Expected return value: " + expectedReturnValue.toString() + NLINE);
        buf.append("Computed return value: " + returnValue.toString() + NLINE);
      }
    } catch (Exception e) {
      pass = false;
      buf.append("invoke() threw an unexpected exception" + NLINE);
      buf.append(e.getMessage() + NLINE);
    }

    // isLiteralText()
    if ((mexp.isLiteralText()) != expectedLiteralText) {
      pass = false;
      buf.append(
          "isLiteralText() did not return " + expectedLiteralText + NLINE);
    }

    // getExpressionString()
    String retrievedStr = mexp.getExpressionString();
    if (!retrievedStr.equals(exprStr)) {
      pass = false;
      buf.append(
          "getExpressionString() does not return expected " + "string" + NLINE);
      buf.append("Expected string = " + exprStr + NLINE);
      buf.append("Computed string = " + retrievedStr + NLINE);
    }

    return pass;
  }

  public static boolean equalsTest(Expression exp1, Expression exp2,
      StringBuffer buf) {

    String e1str = exp1.getExpressionString();
    String e2str = (exp2 == null) ? null : exp2.getExpressionString();

    buf.append("Testing equality: " + e1str + " and " + e2str + NLINE);

    if (!exp1.equals(exp2)) {
      if (exp2 != null) {
        buf.append("Expression " + e1str + " is not equal to " + "Expression "
            + e2str + NLINE);
      }
      return false;
    } else {
      int hcode1 = exp1.hashCode();
      int hcode2 = exp2.hashCode();

      if (hcode1 != hcode2) {
        buf.append("Expressions " + e1str + " and " + e2str + " are "
            + "equal, but their" + NLINE);
        buf.append("hashcodes aren't the same." + NLINE);
        buf.append("Hashcode for " + e1str + ": " + hcode1 + NLINE);
        buf.append("Hashcode for " + e2str + ": " + hcode2 + NLINE);
        return false;
      }
    }

    return true;
  }

  public static boolean expressionSerializableTest(Expression exp,
      StringBuffer buf) {

    ObjectOutputStream out = null;
    ObjectInputStream in = null;
    Expression desexp = null;
    ByteArrayOutputStream bos = null;

    // Serialize the Expression.
    try {
      bos = new ByteArrayOutputStream();
      out = new ObjectOutputStream(bos);
      out.writeObject(exp);
    } catch (IOException ioe) {
      buf.append(
          "Failed to serialize the Expression!" + NLINE + ioe.toString());
      return false;
    } finally {
      if (out != null) {
        try {
          out.close();
        } catch (Exception close) {
          // Do not fail the test if close does not happen.
        }
      }
    }

    // Deserialize the Expression.
    try {
      byte[] byteBuf = bos.toByteArray();
      in = new ObjectInputStream(new ByteArrayInputStream(byteBuf));
      desexp = (Expression) in.readObject();
    } catch (IOException ioe) {
      buf.append(
          "Failed to deserialize the Expression!" + NLINE + ioe.toString());
      return false;
    } catch (ClassNotFoundException cnfe) {
      buf.append("Could not find class of serialized Expression" + NLINE
          + cnfe.toString());
      return false;
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (Exception close) {
          // Do not fail the test if close does not happen.
        }
      }
    }

    // Test Expression After Serialization
    if (!equalsTest(desexp, exp, buf)) {
      buf.append("'getExpressionString' after serialization took " + "place."
          + NLINE + "Expected: " + exp.getExpressionString() + NLINE
          + "Received: " + desexp.getExpressionString() + NLINE);
      return false;
    }

    return true;
  }
}
