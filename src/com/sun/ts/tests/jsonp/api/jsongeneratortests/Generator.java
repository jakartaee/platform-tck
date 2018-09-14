/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsonp.api.jsongeneratortests;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jsonp.api.common.JsonValueType;
import com.sun.ts.tests.jsonp.api.common.TestResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.stream.JsonGenerator;

import static com.sun.ts.tests.jsonp.api.common.JsonAssert.*;
import static com.sun.ts.tests.jsonp.api.common.SimpleValues.*;
import javax.json.JsonObject;

// $Id$
/**
 * JavaScript Object Notation (JSON) compatibility tests: {@link JsonGenerator}
 * API methods.
 */
public class Generator {

  /** Tests input data. */
  private static final Object[] VALUES = new Object[] { OBJ_VALUE, // write(JsonValue)
                                                                   // for
                                                                   // JsonObject
      createEmptyArrayWithStr(), // write(JsonValue) for simple JsonArray
      STR_VALUE, // write(JsonValue) for String
      INT_VALUE, // write(JsonValue) for int
      LNG_VALUE, // write(JsonValue) for long
      DBL_VALUE, // write(JsonValue) for double
      BIN_VALUE, // write(JsonValue) for BigInteger
      BDC_VALUE, // write(JsonValue) for BigDecimal
      BOOL_VALUE, // write(JsonValue) for boolean
      null // write(JsonValue) for null
  };

  /**
   * Creates an instance of {@link JsonGenerator} API methods test.
   */
  Generator() {
    super();
  }

  /**
   * Test {@link JsonGenerator} API methods.
   * 
   * @return Result of all tests in this suite.
   */
  TestResult test() {
    final TestResult result = new TestResult(
        "JsonGenerator API methods for RFC 7159 grammar changes.");
    TestUtil.logMsg("JsonGenerator API methods for RFC 7159 grammar changes.");
    testPrimitiveTypesInRoot(result);
    testWrittingObjectByParts(result);
    return result;
  }

  /**
   * Test primitive types as JSON value stored in JSON document root. RFC 7159
   * grammar changes now allows such a values to be stored in JSON document
   * root.
   */
  private void testPrimitiveTypesInRoot(final TestResult result) {
    for (Object value : VALUES) {
      final String typeName = JsonValueType.getType(value).name();
      TestUtil
          .logMsg(" - write(JsonValue) for " + typeName + " as an argument");
      verifyJsonGeneratorForJsonValue(result, value);
      verifyJsonGeneratorForSimpleType(result, value);
    }
  }

  /**
   * Verify JSON object generation using low level methods
   * {@code writeStartObject()}, {@code writeEnd()}, {@code writeKey(String)}
   * and {@code write(String)}.
   */
  private void testWrittingObjectByParts(final TestResult result) {
    TestUtil.logMsg(" - generate JSON object");
    final StringWriter strWriter = new StringWriter();
    try (JsonGenerator generator = Json.createGenerator(strWriter)) {
      generator.writeStartObject();
      generator.writeKey(STR_NAME);
      generator.write(STR_VALUE);
      generator.writeEnd();
    }
    final String out = strWriter.toString();
    final JsonObject check = createSimpleObjectStr();
    if (operationFailed(check, out)) {
      final String checkStr = check.toString();
      TestUtil.logMsg(
          "     Generated JSON object " + out + " shall be " + checkStr);
      result.fail("generate JSON object",
          "Generated value " + out + " shall be " + checkStr);
    } else {
      TestUtil.logMsg("     Output: " + out);
    }

  }

  /**
   * Verify JSON document root generation for provided JSON value.
   */
  private void verifyJsonGeneratorForJsonValue(final TestResult result,
      final Object value) {
    final StringWriter strWriter = new StringWriter();
    final JsonValue jsonValue = toJsonValue(value);
    try (JsonGenerator generator = Json.createGenerator(strWriter)) {
      generator.write(jsonValue);
    }
    final String out = strWriter.toString();
    if (operationFailed(jsonValue, out)) {
      final String check = jsonValue.toString();
      TestUtil
          .logMsg("     Generated JSON value " + out + " shall be " + check);
      result.fail("write(JsonValue)",
          "Generated value " + out + " shall be " + check);
    } else {
      TestUtil.logMsg("     Output (JsonValue): " + out);
    }
  }

  /**
   * Verify JSON document root generation for provided JSON value.
   */
  @SuppressWarnings("UnnecessaryUnboxing")
  private void verifyJsonGeneratorForSimpleType(final TestResult result,
      final Object value) {
    final StringWriter strWriter = new StringWriter();
    try (JsonGenerator generator = Json.createGenerator(strWriter)) {
      switch (JsonValueType.getType(value)) {
      case String:
        generator.write((String) value);
        break;
      case Integer:
        generator.write(((Integer) value).intValue());
        break;
      case Long:
        generator.write(((Long) value).longValue());
        break;
      case BigInteger:
        generator.write((BigInteger) value);
        break;
      case Double:
        generator.write(((Double) value).doubleValue());
        break;
      case BigDecimal:
        generator.write((BigDecimal) value);
        break;
      case Boolean:
        generator.write(((Boolean) value).booleanValue() ? JsonValue.TRUE
            : JsonValue.FALSE);
        break;
      case JsonValue:
        generator.write((JsonValue) value);
        break;
      case Null:
        generator.write(JsonValue.NULL);
        break;
      default:
        throw new IllegalArgumentException(
            "Value does not match known JSON value type");
      }
    }
    final String out = strWriter.toString();
    if (operationFailed(value, out)) {
      final String check = toJsonValue(value).toString();
      TestUtil
          .logMsg("     Generated simple value " + out + " shall be " + check);
      result.fail("write(JsonValue)",
          "Generated value " + out + " shall be " + check);
    } else {
      TestUtil.logMsg("     Output (simple): " + out);
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
  protected boolean operationFailed(final Object check, final String out) {
    return out == null || !assertEquals(check, out);
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
  protected boolean operationFailed(final JsonValue check, final String out) {
    if (out == null) {
      return true;
    }
    try (final JsonReader reader = Json.createReader(new StringReader(out))) {
      final JsonValue actVal = reader.readValue();
      return !assertEquals((JsonValue) check, actVal);
    }
  }

}
