/*
 * Copyright (c) 2016, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsonp.api.jsonwritertests;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jsonp.api.common.JsonValueType;
import com.sun.ts.tests.jsonp.api.common.SimpleValues;
import com.sun.ts.tests.jsonp.api.common.TestResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.stream.JsonParser;

import static com.sun.ts.tests.jsonp.api.common.JsonAssert.*;
import static com.sun.ts.tests.jsonp.api.common.SimpleValues.*;

// $Id$
/**
 * JavaScript Object Notation (JSON) compatibility tests for {@link JsonWriter}.
 */
public class Writer {

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
   * Creates an instance of JavaScript Object Notation (JSON) compatibility
   * tests for {@link JsonWriter}.
   */
  Writer() {
    super();
  }

  /**
   * {@link JsonWriter} API methods added in JSON-P 1.1.
   * 
   * @return Result of all tests in this suite.
   */
  TestResult test() {
    final TestResult result = new TestResult(
        "JsonWriter API methods added in JSON-P 1.1.");
    TestUtil.logMsg("JsonWriter API methods added in JSON-P 1.1.");
    testWriteValue(result);
    testDoubleWriteValue(result);
    testIOExceptionOnWriteValue(result);
    return result;
  }

  /**
   * Test {@code void write(JsonValue)} method on all child types of
   * {@code JsonValue}.
   * 
   * @param result
   *          Test suite result.
   */
  private void testWriteValue(final TestResult result) {
    for (Object value : VALUES) {
      final String typeName = JsonValueType.getType(value).name();
      TestUtil
          .logMsg(" - write(JsonValue) for " + typeName + " as an argument");
      final JsonValue jsonValue = SimpleValues.toJsonValue(value);
      final StringWriter strWriter = new StringWriter();
      try (final JsonWriter writer = Json.createWriter(strWriter)) {
        writer.write(jsonValue);
      } catch (JsonException ex) {
        TestUtil.logMsg("Caught JsonException: " + ex.getLocalizedMessage());
        result.fail("write(JsonValue)",
            "Caught JsonException: " + ex.getLocalizedMessage());
      }
      final String data = strWriter.toString();
      TestUtil.logMsg("    - Data: " + data);
      final JsonParser parser = Json.createParser(new StringReader(data));
      parser.next();
      final JsonValue outValue = parser.getValue();
      if (operationFailed(jsonValue, outValue)) {
        result.fail("write(JsonValue)",
            "Writer output " + valueToString(outValue) + " value shall be "
                + valueToString(jsonValue));
      }
    }
  }

  /**
   * Test {@code void write(JsonValue)} method with duplicated {@code JsonValue}
   * write call. Second call is expected to throw {@code IllegalStateException}
   * exception.
   * 
   * @param result
   *          Test suite result.
   */
  private void testDoubleWriteValue(final TestResult result) {
    for (Object value : VALUES) {
      final String typeName = JsonValueType.getType(value).name();
      TestUtil.logMsg(
          " - duplicate write(JsonValue) for " + typeName + " as an argument");
      final JsonValue jsonValue = SimpleValues.toJsonValue(value);
      final StringWriter strWriter = new StringWriter();
      try (final JsonWriter writer = Json.createWriter(strWriter)) {
        // 1st attempt to write the data shall pass
        writer.write(jsonValue);
        try {
          // 2nd attempt to write the data shall throw IllegalStateException
          writer.write(jsonValue);
          result.fail("write(JsonValue)",
              "Duplicate call of write(JsonValue) shall throw IllegalStateException");
        } catch (IllegalStateException ex) {
          TestUtil.logMsg("    - Expected exception: " + ex.getMessage());
        } catch (Throwable t) {
          result.fail("write(JsonValue)",
              "Duplicate call of write(JsonValue) shall throw IllegalStateException, not "
                  + t.getClass().getSimpleName());
        }
      } catch (JsonException ex) {
        TestUtil.logMsg("Caught JsonException: " + ex.getLocalizedMessage());
        result.fail("write(JsonValue)",
            "Caught JsonException: " + ex.getLocalizedMessage());
      }
    }
  }

  /**
   * Test {@code void write(JsonValue)} method with write call that causes
   * IOException. IOException shall be encapsulated in JsonException.
   * 
   * @param result
   *          Test suite result.
   */
  @SuppressWarnings("ConvertToTryWithResources")
  private void testIOExceptionOnWriteValue(final TestResult result) {
    TestUtil.logMsg(" - write(JsonValue) into already closed file writer");
    final JsonValue jsonValue = SimpleValues.toJsonValue(DEF_VALUE);
    File temp = null;
    JsonWriter writer;
    // Close writer before calling write method.
    try {
      temp = File.createTempFile("testIOExceptionOnWriteValue", ".txt");
      TestUtil.logMsg("    - Temporary file: " + temp.getAbsolutePath());
      final FileWriter fileWriter = new FileWriter(temp);
      writer = Json.createWriter(fileWriter);
      fileWriter.close();
    } catch (IOException ex) {
      TestUtil.logMsg("Caught IOException: " + ex.getLocalizedMessage());
      result.fail("write(JsonValue)",
          "Caught IOException: " + ex.getLocalizedMessage());
      return;
    } finally {
      if (temp != null) {
        temp.delete();
      }
    }
    try {
      writer.write(jsonValue);
      result.fail("write(JsonValue)",
          "Call of write(JsonValue) on already closed file writer shall throw JsonException");
    } catch (JsonException ex) {
      TestUtil.logMsg("    - Expected exception: " + ex.getMessage());
    } catch (Throwable t) {
      result.fail("write(JsonValue)",
          "Call of write(JsonValue) on already closed file writer shall throw JsonException, not "
              + t.getClass().getSimpleName());
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
