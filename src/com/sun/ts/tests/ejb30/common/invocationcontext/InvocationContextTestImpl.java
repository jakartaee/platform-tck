/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.common.invocationcontext;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import java.util.Arrays;
import java.util.Map;
import javax.interceptor.InvocationContext;

/**
 * A collection of test methods that are used by both server- and client-sides.
 * Test logics are grouped here to make it easier to share between ejb30 and
 * ejb30 lite tests.
 */
public class InvocationContextTestImpl {
  private static final String[] paramsNew = new String[] { "new", "new" };

  private static final String[] paramsOld = new String[] { "old", "old" };

  private static final String paramsCombined = "newnew";

  private InvocationContextTestImpl() {
  }

  public static void interceptAll(InvocationContext inv)
      throws TestFailedException {
    InvocationContextTestImpl impl = new InvocationContextTestImpl();
    String methodName = inv.getMethod().getName();
    if ("setParametersIllegalArgumentExceptionForNumber".equals(methodName)) {
      impl.setParametersIllegalArgumentExceptionForNumber(inv, methodName);
    } else if ("setParametersIllegalArgumentExceptionForChar"
        .equals(methodName)) {
      impl.setParametersIllegalArgumentExceptionForChar(inv, methodName);
    } else if ("setParametersIllegalArgumentExceptionForString"
        .equals(methodName)) {
      impl.setParametersIllegalArgumentExceptionForString(inv, methodName);
    } else if ("setParametersIllegalArgumentExceptionForStringArray"
        .equals(methodName)) {
      impl.setParametersIllegalArgumentExceptionForStringArray(inv, methodName);
    } else if ("setParametersIllegalArgumentExceptionForShortLong"
        .equals(methodName)) {
      impl.setParametersIllegalArgumentExceptionForShortLong(inv, methodName);
    } else if ("proceedAgain".equals(methodName)) {
      impl.proceedAgain(inv, methodName);
    } else if ("getTarget".equals(methodName)) {
      impl.getTarget(inv, methodName);
    } else if ("getSetParametersEmpty".equals(methodName)) {
      impl.getSetParametersEmpty(inv, methodName);
    } else if ("getSetParameters".equals(methodName)) {
      impl.getSetParameters(inv, methodName);
    } else if ("getContextData".equals(methodName)) {
      impl.getContextData(inv, methodName);
    } else if ("getTimer".equals(methodName)) {
      impl.getTimer(inv, methodName);
    }
  }

  private void getTimer(InvocationContext inv, String methodName)
      throws TestFailedException {
    Object t = inv.getTimer();
    if (t != null) {
      throw new TestFailedException(
          "Expecting null from getTimer(), but got " + t);
    }
  }

  private void handleException(Exception exception) throws TestFailedException {
    if (exception != null) {
      if (exception instanceof TestFailedException) {
        throw (TestFailedException) exception;
      } else if (exception instanceof RuntimeException) {
        throw (RuntimeException) exception;
      } else {
        throw new TestFailedException(exception);
      }
    }
  }

  private void getContextData(InvocationContext inv, String methodName)
      throws TestFailedException {
    Object result = null;
    Exception exception = null;
    Map<String, Object> contextData = inv.getContextData();
    String key = "one";
    Object data = 1;
    contextData.put(key, data);
    try {
      result = inv.proceed();
      if (result != null) {
        throw new TestFailedException("Expecting proceed() to return null.");
      }
    } catch (Exception e) {
      exception = e;
    } finally {
      handleException(exception);
      Object actual = inv.getContextData().get(key);
      if (!data.equals(actual)) {
        throw new TestFailedException(
            "Expecting " + data + " from getContextData, but got " + actual);
      }
      Helper.getLogger()
          .info("Got expected data from getContextData:" + actual);
    }
  }

  private void getSetParameters(InvocationContext inv, String methodName)
      throws TestFailedException {
    Object[] params = inv.getParameters();
    if (!Arrays.equals(params, paramsOld)) {
      throw new TestFailedException("Expecting " + Arrays.asList(paramsOld)
          + ", actual " + Arrays.asList(params));
    }
    inv.setParameters(paramsNew);
  }

  private void getSetParametersEmpty(InvocationContext inv, String methodName)
      throws TestFailedException {
    Object[] params = inv.getParameters();
    if (params == null || params.length == 0) {
      Helper.getLogger().info(
          "Got expected null/empty params from InvocationContext.getParameters()");
    } else {
      throw new TestFailedException(
          "Expecting null/empty from InvocationContext.getParameters(), but got "
              + params);
    }
    try {
      inv.setParameters(new Object[] { Boolean.TRUE });
      throw new TestFailedException(
          "Expecting IllegalArgumentException from InvocationContext.setParameters, but got none.");
    } catch (IllegalArgumentException e) {
      Helper.getLogger().info(
          "Got expected IllegalArgumentException from InvocationContext.setParameters");
    }
  }

  private void getTarget(InvocationContext inv, String methodName)
      throws TestFailedException {
    int id1 = System.identityHashCode(inv.getTarget());
    int id2 = 0;
    Exception exception = null;
    try {
      id2 = (Integer) inv.proceed();
    } catch (Exception e) {
      exception = e;
    } finally {
      handleException(exception);
      if (id1 == id2) {
        Helper.getLogger()
            .info("Got the expected bean instance from getTarget(): " + id1);
      } else {
        throw new TestFailedException("target1: " + id1 + ", and target2" + id2
            + " are not the same bean instance.");
      }
    }
  }

  private void proceedAgain(InvocationContext inv, String methodName)
      throws TestFailedException {
    Object result = null;
    try {
      result = inv.proceed();
      throw new TestFailedException(
          "Expecting TestFailedException, but got none.");
    } catch (TestFailedException e) {
      Helper.getLogger().info(
          "Got expected TestFailedException in the 1st proceed call: " + e);
      Object[] params = inv.getParameters();
      params[0] = 1;
      try {
        result = inv.proceed();
      } catch (Exception ee) {
        throw new TestFailedException("Unexpected exception: ", e);
      }
    } catch (Exception e) {
      throw new TestFailedException("Expecting TestFailedException, but got ",
          e);
    }
  }

  // for business method (Number, Number)
  private void setParametersIllegalArgumentExceptionForNumber(
      InvocationContext inv, String methodName) throws TestFailedException {
    String reason = "";
    Object[] objects = null;
    // try to reset it in various ways, and may result in
    // IllegalArgumentException
    try {
      inv.setParameters(null);
      throw new TestFailedException(
          "Didn't get IllegalArgumentException when setParameters(null).  The original business method is "
              + methodName);
    } catch (IllegalArgumentException e) {
      reason = "Got expected IllegalArgumentException when setParameters(null).  The original business method is "
          + methodName;
      Helper.getLogger().info(reason);
    }

    try {
      objects = new Object[] { new Integer(0) };
      inv.setParameters(objects);
      throw new TestFailedException(
          "Didn't get IllegalArgumentException when setParameters to "
              + Arrays.asList(objects) + ".  The original business method is "
              + methodName);
    } catch (IllegalArgumentException e) {
      reason = "Got expected IllegalArgumentException when setParameters to "
          + Arrays.asList(objects) + ".  The original business method is "
          + methodName;
      Helper.getLogger().info(reason);
    }

    try {
      objects = new Object[] { new Integer(0), new Integer(1), new Integer(2) };
      inv.setParameters(objects);
      throw new TestFailedException(
          "Didn't get IllegalArgumentException when setParameters to "
              + Arrays.asList(objects) + ".  The original business method is "
              + methodName);
    } catch (IllegalArgumentException e) {
      reason = "Got expected IllegalArgumentException when setParameters to "
          + Arrays.asList(objects) + ".  The original business method is "
          + methodName;
      Helper.getLogger().info(reason);
    }

    try {
      objects = new Object[] { "a", "b" };
      inv.setParameters(objects);
      throw new TestFailedException(
          "Didn't get IllegalArgumentException when setParameters to "
              + Arrays.asList(objects) + ".  The original business method is "
              + methodName);
    } catch (IllegalArgumentException e) {
      reason = "Got expected IllegalArgumentException when setParameters to "
          + Arrays.asList(objects) + ".  The original business method is "
          + methodName;
      Helper.getLogger().info(reason);
    }

    // this one is valid, and the target business method should still be
    // (Number, Number)
    Integer[] ints = new Integer[] { 5, 5 };
    inv.setParameters(ints);

    Object[] paramsAgain = inv.getParameters();
    if (!Arrays.equals(ints, paramsAgain)) {
      throw new TestFailedException("Expecting " + Arrays.asList(ints)
          + "when getParameters() after setParameters(...), but got "
          + paramsAgain);
    }
  }

  // for business method (char, char)
  private void setParametersIllegalArgumentExceptionForChar(
      InvocationContext inv, String methodName) throws TestFailedException {
    String reason = "";
    Object[] objects = null;
    // try to reset it in various ways, and may result in
    // IllegalArgumentException
    try {
      inv.setParameters(null);
      throw new TestFailedException(
          "Didn't get IllegalArgumentException when setParameters(null).  The original business method is "
              + methodName);
    } catch (IllegalArgumentException e) {
      reason = "Got expected IllegalArgumentException when setParameters(null).  The original business method is "
          + methodName;
      Helper.getLogger().info(reason);
    }

    try {
      objects = new Object[] { new Character('0') };
      inv.setParameters(objects);
      throw new TestFailedException(
          "Didn't get IllegalArgumentException when setParameters to "
              + Arrays.asList(objects) + ".  The original business method is "
              + methodName);
    } catch (IllegalArgumentException e) {
      reason = "Got expected IllegalArgumentException when setParameters to "
          + Arrays.asList(objects) + ".  The original business method is "
          + methodName;
      Helper.getLogger().info(reason);
    }

    try {
      objects = new Character[] { '0', '1', '2' };
      inv.setParameters(objects);
      throw new TestFailedException(
          "Didn't get IllegalArgumentException when setParameters to "
              + Arrays.asList(objects) + ".  The original business method is "
              + methodName);
    } catch (IllegalArgumentException e) {
      reason = "Got expected IllegalArgumentException when setParameters to "
          + Arrays.asList(objects) + ".  The original business method is "
          + methodName;
      Helper.getLogger().info(reason);
    }

    try {
      objects = new Object[] { "a", "b" };
      inv.setParameters(objects);
      throw new TestFailedException(
          "Didn't get IllegalArgumentException when setParameters to "
              + Arrays.asList(objects) + ".  The original business method is "
              + methodName);
    } catch (IllegalArgumentException e) {
      reason = "Got expected IllegalArgumentException when setParameters to "
          + Arrays.asList(objects) + ".  The original business method is "
          + methodName;
      Helper.getLogger().info(reason);
    }

    // this one is valid, and the target business method should still be
    // (char, char), not (Object, Object)
    objects = new Object[] { new Character('5'), new Character('5') };
    inv.setParameters(objects);

    Object[] paramsAgain = inv.getParameters();
    if (!Arrays.equals(objects, paramsAgain)) {
      throw new TestFailedException("Expecting " + Arrays.asList(objects)
          + "when getParameters() after setParameters(...), but got "
          + paramsAgain);
    }
  }

  // for business method (String, String)
  private void setParametersIllegalArgumentExceptionForString(
      InvocationContext inv, String methodName) throws TestFailedException {
    String reason = "";
    Object[] objects = null;

    // try to reset it in various ways, and may result in
    // IllegalArgumentException
    try {
      inv.setParameters(null);
      throw new TestFailedException(
          "Didn't get IllegalArgumentException when setParameters(null).  The original business method is "
              + methodName);
    } catch (IllegalArgumentException e) {
      reason = "Got expected IllegalArgumentException when setParameters(null).  The original business method is "
          + methodName;
      Helper.getLogger().info(reason);
    }

    try {
      objects = new Object[] { "0" };
      inv.setParameters(objects);
      throw new TestFailedException(
          "Didn't get IllegalArgumentException when setParameters to "
              + Arrays.asList(objects) + ".  The original business method is "
              + methodName);
    } catch (IllegalArgumentException e) {
      reason = "Got expected IllegalArgumentException when setParameters to "
          + Arrays.asList(objects) + ".  The original business method is "
          + methodName;
      Helper.getLogger().info(reason);
    }

    try {
      objects = new String[] { "", "", "" };
      inv.setParameters(objects);
      throw new TestFailedException(
          "Didn't get IllegalArgumentException when setParameters to "
              + Arrays.asList(objects) + ".  The original business method is "
              + methodName);
    } catch (IllegalArgumentException e) {
      reason = "Got expected IllegalArgumentException when setParameters to "
          + Arrays.asList(objects) + ".  The original business method is "
          + methodName;
      Helper.getLogger().info(reason);
    }

    try {
      objects = new Object[] { new Object(), new Object() };
      inv.setParameters(objects);
      throw new TestFailedException(
          "Didn't get IllegalArgumentException when setParameters to "
              + Arrays.asList(objects) + ".  The original business method is "
              + methodName);
    } catch (IllegalArgumentException e) {
      reason = "Got expected IllegalArgumentException when setParameters to "
          + Arrays.asList(objects) + ".  The original business method is "
          + methodName;
      Helper.getLogger().info(reason);
    }

    // this one is valid, and the target business method should still be
    // (String, String), not (Object, Object)
    objects = new Object[] { null, null };
    inv.setParameters(objects);

    Object[] paramsAgain = inv.getParameters();
    if (!Arrays.equals(objects, paramsAgain)) {
      throw new TestFailedException("Expecting " + Arrays.asList(objects)
          + "when getParameters() after setParameters(...), but got "
          + paramsAgain);
    }
  }

  // for business method (String[])
  private void setParametersIllegalArgumentExceptionForStringArray(
      InvocationContext inv, String methodName) throws TestFailedException {
    String reason = "";
    Object[] objects = null;

    // try to reset it in various ways, and may result in
    // IllegalArgumentException
    try {
      inv.setParameters(null);
      throw new TestFailedException(
          "Didn't get IllegalArgumentException when setParameters(null).  The original business method is "
              + methodName);
    } catch (IllegalArgumentException e) {
      reason = "Got expected IllegalArgumentException when setParameters(null).  The original business method is "
          + methodName;
      Helper.getLogger().info(reason);
    }

    try {
      objects = new String[] { "0" };
      inv.setParameters(objects);
      throw new TestFailedException(
          "Didn't get IllegalArgumentException when setParameters to "
              + Arrays.asList(objects) + ".  The original business method is "
              + methodName);
    } catch (IllegalArgumentException e) {
      reason = "Got expected IllegalArgumentException when setParameters to "
          + Arrays.asList(objects) + ".  The original business method is "
          + methodName;
      Helper.getLogger().info(reason);
    }

    try {
      objects = new Object[] { new Object[] { "0" } };
      inv.setParameters(objects);
      throw new TestFailedException(
          "Didn't get IllegalArgumentException when setParameters to "
              + Arrays.asList(objects) + ".  The original business method is "
              + methodName);
    } catch (IllegalArgumentException e) {
      reason = "Got expected IllegalArgumentException when setParameters to "
          + Arrays.asList(objects) + ".  The original business method is "
          + methodName;
      Helper.getLogger().info(reason);
    }

    try {
      objects = new Object[] { new Object() };
      inv.setParameters(objects);
      throw new TestFailedException(
          "Didn't get IllegalArgumentException when setParameters to "
              + Arrays.asList(objects) + ".  The original business method is "
              + methodName);
    } catch (IllegalArgumentException e) {
      reason = "Got expected IllegalArgumentException when setParameters to "
          + Arrays.asList(objects) + ".  The original business method is "
          + methodName;
      Helper.getLogger().info(reason);
    }

    // this one is valid, and the target business method should still be
    // (String[])
    objects = new Object[] { new String[] { null } };
    inv.setParameters(objects);

    Object[] paramsAgain = inv.getParameters();
    if (!Arrays.equals(objects, paramsAgain)) {
      throw new TestFailedException("Expecting " + Arrays.asList(objects)
          + "when getParameters() after setParameters(...), but got "
          + paramsAgain);
    }
  }

  // for business method (short, long)
  private void setParametersIllegalArgumentExceptionForShortLong(
      InvocationContext inv, String methodName) throws TestFailedException {
    String reason = "";
    Object[] objects = null;

    try {
      objects = new Object[] { Short.MIN_VALUE - 1, new Long(1) };
      inv.setParameters(objects);
      throw new TestFailedException(
          "Didn't get IllegalArgumentException when setParameters to "
              + Arrays.asList(objects) + ".  The original business method is "
              + methodName);
    } catch (IllegalArgumentException e) {
      reason = "Got expected IllegalArgumentException when setParameters to "
          + Arrays.asList(objects) + ".  The original business method is "
          + methodName;
      Helper.getLogger().info(reason);
    }

    try {
      objects = new Object[] { Integer.MAX_VALUE, new Long(1) };
      inv.setParameters(objects);
      throw new TestFailedException(
          "Didn't get IllegalArgumentException when setParameters to "
              + Arrays.asList(objects) + ".  The original business method is "
              + methodName);
    } catch (IllegalArgumentException e) {
      reason = "Got expected IllegalArgumentException when setParameters to "
          + Arrays.asList(objects) + ".  The original business method is "
          + methodName;
      Helper.getLogger().info(reason);
    }

    // this one is valid
    objects = new Object[] { Short.valueOf((short) 5), Long.valueOf(5) };
    inv.setParameters(objects);

    Object[] paramsAgain = inv.getParameters();
    if (!Arrays.equals(objects, paramsAgain)) {
      throw new TestFailedException("Expecting " + Arrays.asList(objects)
          + "when getParameters() after setParameters(...), but got "
          + paramsAgain);
    }
    Helper.getLogger().info("Got expected values from getParameters(): "
        + Arrays.toString(paramsAgain));
  }

  // The following methods are referenced from ClientBase and
  // lite/interceptor/*/invocationcontext/Client

  public static void setParametersIllegalArgumentException(
      InvocationContextIF[] beans) throws TestFailedException {
    for (InvocationContextIF b : beans) {
      setParametersIllegalArgumentExceptionForNumber0(b);
      setParametersIllegalArgumentExceptionForChar0(b);
      setParametersIllegalArgumentExceptionForString0(b);
      setParametersIllegalArgumentExceptionForStringArray0(b);
      setParametersIllegalArgumentExceptionForShortLong0(b);
    }
  }

  public static void getTarget(InvocationContextIF[] beans)
      throws TestFailedException {
    for (InvocationContextIF b : beans) {
      Helper.getLogger().info(
          String.format("bean instance from getTarget(): %s", b.getTarget()));
    }
  }

  public static void getContextData(InvocationContextIF[] beans)
      throws TestFailedException {
    for (InvocationContextIF b : beans) {
      b.getContextData();
    }
  }

  public static void getSetParametersEmpty(InvocationContextIF[] beans)
      throws TestFailedException {
    for (InvocationContextIF b : beans) {
      b.getSetParametersEmpty();
    }
  }

  public static void getSetParameters(InvocationContextIF[] beans)
      throws TestFailedException {
    for (InvocationContextIF b : beans) {
      // String result = b.getSetParameters(paramsOld[0], paramsOld[1]);
      String result = b.getSetParameters(paramsOld[0], paramsOld[1]);
      if (result.equals(paramsCombined)) {
        Helper.getLogger()
            .info("Got expected result after InvocationContext.setParameters: "
                + result + ", from bean " + b);
      } else {
        throw new TestFailedException("Expecting " + paramsCombined
            + ", actual, " + result + ", from bean " + b);
      }
    }
  }

  public static void proceedAgain(InvocationContextIF[] beans)
      throws TestFailedException {
    for (InvocationContextIF b : beans) {
      if (b.proceedAgain(0)) {
        Helper.getLogger().info(
            "Got expected result from calling proceed twice: true from bean "
                + b);
      } else {
        throw new TestFailedException(
            "Expecting true from proceedAgain, but got false from bean " + b);
      }
    }
  }

  public static void getTimer(InvocationContextIF[] beans)
      throws TestFailedException {
    for (InvocationContextIF b : beans) {
      b.getTimer();
    }
  }

  private static void setParametersIllegalArgumentExceptionForNumber0(
      InvocationContextIF bean) throws TestFailedException {
    Number m = 8;
    Number n = 9;
    Number[] result = null;
    Number[] expected = new Number[] { 5, 5 };
    result = bean.setParametersIllegalArgumentExceptionForNumber(m, n);
    if (Arrays.equals(expected, result)) {
      Helper.getLogger().info("Got expected return value "
          + Arrays.asList(result) + " from bean " + bean);
    } else {
      throw new RuntimeException(
          "Expecting " + Arrays.asList(expected) + ", but the actual result is "
              + Arrays.asList(result) + " from bean " + bean);
    }
  }

  private static void setParametersIllegalArgumentExceptionForChar0(
      InvocationContextIF bean) throws TestFailedException {
    char m = '8';
    char n = '9';
    char[] result = null;
    char[] expected = new char[] { '5', '5' };
    result = bean.setParametersIllegalArgumentExceptionForChar(m, n);
    if (Arrays.equals(expected, result)) {
      Helper.getLogger().info("Got expected return value "
          + Arrays.asList(result) + " from bean " + bean);
    } else {
      throw new TestFailedException(
          "Expecting " + Arrays.asList(expected) + ", but the actual result is "
              + Arrays.asList(result) + " from bean " + bean);
    }
  }

  private static void setParametersIllegalArgumentExceptionForString0(
      InvocationContextIF bean) throws TestFailedException {
    String m = "8";
    String n = "9";
    String[] result = null;
    String[] expected = new String[] { null, null };
    result = bean.setParametersIllegalArgumentExceptionForString(m, n);
    if (Arrays.equals(expected, result)) {
      Helper.getLogger().info("Got expected return value "
          + Arrays.asList(result) + " from bean " + bean);
    } else {
      throw new TestFailedException(
          "Expecting " + Arrays.asList(expected) + ", but the actual result is "
              + Arrays.asList(result) + " from bean " + bean);
    }
  }

  private static void setParametersIllegalArgumentExceptionForStringArray0(
      InvocationContextIF bean) throws TestFailedException {
    String m = "8";
    String[] strings = new String[] { m };
    String[] result = null;
    String[] expected = new String[] { null };
    result = bean.setParametersIllegalArgumentExceptionForStringArray(strings);
    if (Arrays.equals(expected, result)) {
      Helper.getLogger().info("Got expected return value "
          + Arrays.asList(result) + " from bean " + bean);
    } else {
      throw new TestFailedException(
          "Expecting " + Arrays.asList(expected) + ", but the actual result is "
              + Arrays.asList(result) + " from bean " + bean);
    }
  }

  private static void setParametersIllegalArgumentExceptionForShortLong0(
      InvocationContextIF bean) throws TestFailedException {
    short m = 0;
    long n = 1;
    Number[] result = null;
    Number[] expected = { new Byte((byte) 5), new Integer(5) };
    result = bean.setParametersIllegalArgumentExceptionForShortLong(m, n);

    // using Arrays.equals will return false, since numeirc coersion changed
    // data type
    // if(Arrays.equals(expected, result)) {
    if (result != null && result.length == 2 && result[0] != null
        && result[1] != null && (result[0].intValue() == expected[0].intValue())
        && (result[1].intValue() == expected[1].intValue())) {
      Helper.getLogger().info("Got expected return value "
          + Arrays.asList(result) + " from bean " + bean);
    } else {
      throw new TestFailedException(
          "Expecting " + Arrays.asList(expected) + ", but the actual result is "
              + Arrays.asList(result) + " from bean " + bean);
    }
  }
}
