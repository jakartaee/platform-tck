/*
 * Copyright (c) 2017, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsonp.api.jsonarraytests;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jsonp.api.common.TestResult;
import java.util.ArrayList;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;

import static com.sun.ts.tests.jsonp.api.common.JsonAssert.*;
import static com.sun.ts.tests.jsonp.api.common.SimpleValues.*;
import java.util.Iterator;
import java.util.List;
import jakarta.json.JsonNumber;

// $Id$
/**
 * JavaScript Object Notation (JSON) compatibility tests:
 * {@link JsonArrayBuilder} API factory methods added in JSON-P 1.1.<br>
 */
public class ArrayBuilders {

  /**
   * Creates an instance of {@link JsonArrayBuilder} API factory methods added
   * in JSON-P 1.1 test.
   */
  ArrayBuilders() {
    super();
  }

  /**
   * Test {@link JsonArrayBuilder} factory method added in JSON-P 1.1.
   * 
   * @return Result of all tests in this suite.
   */
  TestResult test() {
    final TestResult result = new TestResult(
        "JsonArrayBuilder API factory methods added in JSON-P 1.1.");
    TestUtil
        .logMsg("JsonArrayBuilder API factory methods added in JSON-P 1.1.");
    testCreateFromCollection(result);
    testCreateFromJsonArray(result);
    testGetStringValuesAs(result);
    testGetIntValuesAs(result);
    return result;
  }

  /**
   * Test {@link Json#createArrayBuilder(Collection<Object>)} method.
   * 
   * @param result
   *          Test suite result.
   */
  private void testCreateFromCollection(final TestResult result) {
    TestUtil.logMsg(" - Json#createArrayBuilder(Collection<Object>)");
    final JsonArray check = createSimpleStringArray5();
    final ArrayList<Object> values = new ArrayList<>(check.size());
    for (final JsonValue value : check) {
      values.add(((JsonString) value).getString());
    }
    final JsonArrayBuilder builder = Json.createArrayBuilder(values);
    final JsonArray out = builder.build();
    if (operationFailed(check, out)) {
      result.fail("createArrayBuilder(Collection<Object>)", "Builder output "
          + valueToString(out) + " value shall be " + valueToString(check));
    }
  }

  /**
   * Test {@link Json#createArrayBuilder(JsonArray)} method.
   * 
   * @param result
   *          Test suite result.
   */
  private void testCreateFromJsonArray(final TestResult result) {
    TestUtil.logMsg(" - Json#createArrayBuilder(JsonArray)");
    final JsonArray check = createSimpleStringArray5();
    final JsonArrayBuilder builder = Json.createArrayBuilder(check);
    final JsonArray out = builder.build();
    if (operationFailed(check, out)) {
      result.fail("createArrayBuilder(JsonArray)", "Builder output "
          + valueToString(out) + " value shall be " + valueToString(check));
    }
  }

  /**
   * Test {@code default JsonArray#getValuesAs(Function<K,T>)} method on
   * {@code String} values.
   * 
   * @param result
   *          Test suite result.
   */
  private void testGetStringValuesAs(final TestResult result) {
    TestUtil.logMsg(" - getValuesAs(Function<K,T> on String array");
    final JsonArray in = createStringArray2();
    final List<String> out = in.getValuesAs(JsonString::getString);
    boolean failed = in.size() != out.size();
    if (!failed) {
      final Iterator<JsonValue> inIt = in.iterator();
      final Iterator<String> outIt = out.iterator();
      while (!failed && inIt.hasNext()) {
        final JsonValue inVal = inIt.next();
        final String outVal = outIt.next();
        failed = !((JsonString) inVal).getString().equals(outVal);
      }
    }
    if (failed) {
      result.fail("getValuesAs(Function<K,T>)", "Returned Array "
          + out.toString() + " content shall match " + valueToString(in));
    }
  }

  /**
   * Test {@code default JsonArray#getValuesAs(Function<K,T>)} method on
   * {@code int} values.
   * 
   * @param result
   *          Test suite result.
   */
  private void testGetIntValuesAs(final TestResult result) {
    TestUtil.logMsg(" - getValuesAs(Function<K,T> on int array");
    final JsonArray in = createIntArray2();
    final List<Integer> out = in.getValuesAs(JsonNumber::intValue);
    boolean failed = in.size() != out.size();
    if (!failed) {
      final Iterator<JsonValue> inIt = in.iterator();
      final Iterator<Integer> outIt = out.iterator();
      while (!failed && inIt.hasNext()) {
        final JsonValue inVal = inIt.next();
        final Integer outVal = outIt.next();
        failed = ((JsonNumber) inVal).intValue() != outVal;
      }
    }
    if (failed) {
      result.fail("getValuesAs(Function<K,T>)", "Returned Array "
          + out.toString() + " content shall match " + valueToString(in));
    }
  }

  /**
   * Operation result check.
   * 
   * @param check
   *          Expected modified JSON value.
   * @param out
   *          Operation output.
   * @return Value of {@code true} if operation passed or {@code false}
   *         otherwise.
   */
  protected boolean operationFailed(final JsonValue check,
      final JsonValue out) {
    return out == null || !assertEquals(check, out);
  }

}
