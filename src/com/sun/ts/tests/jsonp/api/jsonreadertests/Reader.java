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

package com.sun.ts.tests.jsonp.api.jsonreadertests;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jsonp.api.common.JsonValueType;
import com.sun.ts.tests.jsonp.api.common.SimpleValues;
import com.sun.ts.tests.jsonp.api.common.TestResult;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.stream.JsonParsingException;

import static com.sun.ts.tests.jsonp.api.common.JsonAssert.*;
import static com.sun.ts.tests.jsonp.api.common.SimpleValues.*;

// $Id$
/**
 * JavaScript Object Notation (JSON) compatibility tests for {@link JsonReader}.
 */
public class Reader {

  /** Tests input data. */
  private static final Object[] VALUES = new Object[] { OBJ_VALUE, // readValue()
                                                                   // for
                                                                   // JsonObject
      createEmptyArrayWithStr(), // readValue() for simple JsonArray
      STR_VALUE, // readValue() for String
      INT_VALUE, // readValue() for int
      LNG_VALUE, // readValue() for long
      DBL_VALUE, // readValue() for double
      BIN_VALUE, // readValue() for BigInteger
      BDC_VALUE, // readValue() for BigDecimal
      BOOL_VALUE, // readValue() for boolean
      null // readValue() for null
  };

  /**
   * Creates an instance of JavaScript Object Notation (JSON) compatibility
   * tests for {@link JsonReader}.
   */
  Reader() {
    super();
  }

  /**
   * {@link JsonReader} API methods added in JSON-P 1.1.
   * 
   * @return Result of all tests in this suite.
   */
  TestResult test() {
    final TestResult result = new TestResult(
        "JsonReader API methods added in JSON-P 1.1.");
    TestUtil.logMsg("JsonReader API methods added in JSON-P 1.1.");
    testReadValue(result);
    testDoubleReadValue(result);
    testIOExceptionOnReadValue(result);
    testReadInvalidValue(result);
    return result;
  }

  /**
   * Test {@code JsonValue readValue()} method on all child types stored in
   * source data.
   * 
   * @param result
   *          Test suite result.
   */
  private void testReadValue(final TestResult result) {
    for (Object value : VALUES) {
      final String typeName = JsonValueType.getType(value).name();
      TestUtil.logMsg(" - readValue() for " + typeName + " in source data");
      final JsonValue jsonValue = SimpleValues.toJsonValue(value);
      final String data = JsonValueType.toStringValue(value);
      TestUtil.logMsg("    - Data: " + data);
      final StringReader strReader = new StringReader(data);
      JsonValue outValue = null;
      try (final JsonReader reader = Json.createReader(strReader)) {
        outValue = reader.readValue();
      } catch (JsonException ex) {
        TestUtil.logMsg("Caught JsonException: " + ex.getLocalizedMessage());
        result.fail("readValue()",
            "Caught JsonException: " + ex.getLocalizedMessage());
      }
      if (operationFailed(jsonValue, outValue)) {
        result.fail("readValue()", "Reader output " + valueToString(outValue)
            + " value shall be " + valueToString(jsonValue));
      }
    }
  }

  /**
   * Test {@code JsonValue readValue()} method with duplicated {@code JsonValue}
   * read call. Second call is expected to throw {@code IllegalStateException}
   * exception.
   * 
   * @param result
   *          Test suite result.
   */
  private void testDoubleReadValue(final TestResult result) {
    for (Object value : VALUES) {
      final String typeName = JsonValueType.getType(value).name();
      TestUtil.logMsg(
          " - duplicate readValue() for " + typeName + " in source data");
      final String data = JsonValueType.toStringValue(value);
      final StringReader strReader = new StringReader(data);
      try (final JsonReader reader = Json.createReader(strReader)) {
        // 1st attempt to read the data shall pass
        reader.readValue();
        try {
          // 2nd attempt to read the data shall throw IllegalStateException
          reader.readValue();
          result.fail("readValue()",
              "Duplicate call of readValue() shall throw IllegalStateException");
        } catch (IllegalStateException ex) {
          TestUtil.logMsg("    - Expected exception: " + ex.getMessage());
        } catch (Throwable t) {
          result.fail("readValue()",
              "Duplicate call of readValue() shall throw IllegalStateException, not "
                  + t.getClass().getSimpleName());
        }
      } catch (JsonException ex) {
        TestUtil.logMsg("Caught JsonException: " + ex.getLocalizedMessage());
        result.fail("readValue()",
            "Caught JsonException: " + ex.getLocalizedMessage());
      }
    }
  }

  /**
   * Test {@code JsonValue readValue()} method with read call that causes
   * IOException. IOException shall be encapsulated in JsonException.
   * 
   * @param result
   *          Test suite result.
   */
  @SuppressWarnings("ConvertToTryWithResources")
  private void testIOExceptionOnReadValue(final TestResult result) {
    TestUtil.logMsg(" - readValue() from already closed file reader");
    File temp = null;
    JsonReader reader;
    // Close writer before calling write method.
    try {
      temp = File.createTempFile("testIOExceptionOnReadValue", ".txt");
      TestUtil.logMsg("    - Temporary file: " + temp.getAbsolutePath());
      try (final FileWriter fileWriter = new FileWriter(temp)) {
        fileWriter.write(JsonValueType.toStringValue(DEF_VALUE));
      }
      final FileReader fileReader = new FileReader(temp);
      reader = Json.createReader(fileReader);
      fileReader.close();
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
      reader.readValue();
      result.fail("readValue()",
          "Call of readValue() on already closed file reader shall throw JsonException");
    } catch (JsonException ex) {
      TestUtil.logMsg("    - Expected exception: " + ex.getMessage());
    } catch (Throwable t) {
      result.fail("readValue()",
          "Call of readValue() on already closed file reader shall throw JsonException, not "
              + t.getClass().getSimpleName());
    }
  }

  /**
   * Test {@code JsonValue readValue()} method with read call on invalid JSON
   * data. JsonParsingException shall be thrown when reading invalid data.
   * 
   * @param result
   *          Test suite result.
   */
  private void testReadInvalidValue(final TestResult result) {
    TestUtil.logMsg(" - readValue() on invalid JSON data");
    // Invalid JSON: starting an array, closing an object.
    final String data = "[" + SimpleValues.toJsonValue(DEF_VALUE) + "}";
    final StringReader strReader = new StringReader(data);
    JsonValue outValue = null;
    try (final JsonReader reader = Json.createReader(strReader)) {
      reader.readValue();
      result.fail("readValue()",
          "Call of readValue() on invalid data shall throw JsonParsingException");
    } catch (JsonParsingException ex) {
      TestUtil.logMsg("    - Expected exception: " + ex.getMessage());
    } catch (Throwable t) {
      result.fail("readValue()",
          "Call of readValue() on invalid data shall throw JsonParsingException, not "
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
