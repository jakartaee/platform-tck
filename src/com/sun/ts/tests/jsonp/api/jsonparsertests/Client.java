/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jsonp.api.jsonparsertests;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jsonp.api.common.TestResult;
import com.sun.ts.tests.jsonp.common.*;
import java.io.*;
import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Properties;
import javax.json.*;
import javax.json.stream.*;

public class Client extends ServiceEETest {
  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.setup_props:
   */

  public void setup(String[] args, Properties p) throws Fault {
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * Utitity method to parse various JsonObjectUTF encoded files
   */
  private boolean parseAndVerify_JsonObjectUTF(JsonParser parser)
      throws Exception {
    boolean pass = true;

    try {
      JSONP_Util.resetParseErrs();
      JSONP_Util.testEventType(parser, JsonParser.Event.START_OBJECT);
      JSONP_Util.testKeyStringValue(parser, "stringName", "stringValue");
      JSONP_Util.testKeyStartObjectValue(parser, "objectName");
      JSONP_Util.testKeyStringValue(parser, "foo", "bar");
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      JSONP_Util.testKeyStartArrayValue(parser, "arrayName");
      JSONP_Util.testIntegerValue(parser, 1);
      JSONP_Util.testIntegerValue(parser, 2);
      JSONP_Util.testIntegerValue(parser, 3);
      JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      int parseErrs = JSONP_Util.getParseErrs();
      if (parseErrs != 0) {
        logErr("There were " + parseErrs + " parser errors that occurred.");
        pass = false;
      }
    } catch (Exception e) {
      throw e;
    }
    return pass;
  }

  /*
   * Utitity method to parse JsonObjectWithAllTypesOfData
   */
  private boolean parseAndVerify_JsonObjectWithAllTypesOfData(JsonParser parser)
      throws Exception {
    boolean pass = true;

    try {
      JSONP_Util.resetParseErrs();
      JSONP_Util.testEventType(parser, JsonParser.Event.START_OBJECT);
      JSONP_Util.testKeyStringValue(parser, "emptyString", "");
      JSONP_Util.testKeyStartArrayValue(parser, "emptyArray");
      JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      JSONP_Util.testKeyStartObjectValue(parser, "emptyObject");
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      JSONP_Util.testKeyStringValue(parser, "string", "string");
      JSONP_Util.testKeyIntegerValue(parser, "number", 100);
      JSONP_Util.testKeyTrueValue(parser, "true");
      JSONP_Util.testKeyFalseValue(parser, "false");
      JSONP_Util.testKeyNullValue(parser, "null");
      JSONP_Util.testKeyStartObjectValue(parser, "object");
      JSONP_Util.testKeyStringValue(parser, "emptyString", "");
      JSONP_Util.testKeyStartArrayValue(parser, "emptyArray");
      JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      JSONP_Util.testKeyStartObjectValue(parser, "emptyObject");
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      JSONP_Util.testKeyStringValue(parser, "string", "string");
      JSONP_Util.testKeyIntegerValue(parser, "number", 100);
      JSONP_Util.testKeyTrueValue(parser, "true");
      JSONP_Util.testKeyFalseValue(parser, "false");
      JSONP_Util.testKeyNullValue(parser, "null");
      JSONP_Util.testKeyStartObjectValue(parser, "object");
      JSONP_Util.testKeyStringValue(parser, "name", "value");
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      JSONP_Util.testKeyStartArrayValue(parser, "array");
      JSONP_Util.testStringValue(parser, "one");
      JSONP_Util.testStringValue(parser, "two");
      JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      JSONP_Util.testKeyStartArrayValue(parser, "array");
      JSONP_Util.testStringValue(parser, "string");
      JSONP_Util.testIntegerValue(parser, 100);
      JSONP_Util.testTrueValue(parser, JsonParser.Event.VALUE_TRUE);
      JSONP_Util.testFalseValue(parser, JsonParser.Event.VALUE_FALSE);
      JSONP_Util.testNullValue(parser, JsonParser.Event.VALUE_NULL);
      JSONP_Util.testEventType(parser, JsonParser.Event.START_OBJECT);
      JSONP_Util.testKeyStringValue(parser, "name", "value");
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      JSONP_Util.testEventType(parser, JsonParser.Event.START_ARRAY);
      JSONP_Util.testStringValue(parser, "one");
      JSONP_Util.testStringValue(parser, "two");
      JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      JSONP_Util.testKeyIntegerValue(parser, "intPositive", 100);
      JSONP_Util.testKeyIntegerValue(parser, "intNegative", -100);
      JSONP_Util.testKeyLongValue(parser, "longMax", 9223372036854775807L);
      JSONP_Util.testKeyLongValue(parser, "longMin", -9223372036854775808L);
      JSONP_Util.testKeyDoubleValue(parser, "fracPositive", (double) 0.5);
      JSONP_Util.testKeyDoubleValue(parser, "fracNegative", (double) -0.5);
      JSONP_Util.testKeyDoubleValue(parser, "expPositive1", (double) 7e3);
      JSONP_Util.testKeyDoubleValue(parser, "expPositive2", (double) 7e+3);
      JSONP_Util.testKeyDoubleValue(parser, "expPositive3", (double) 9E3);
      JSONP_Util.testKeyDoubleValue(parser, "expPositive4", (double) 9E+3);
      JSONP_Util.testKeyDoubleValue(parser, "expNegative1", (double) 7e-3);
      JSONP_Util.testKeyDoubleValue(parser, "expNegative2", (double) 7E-3);
      JSONP_Util.testKeyStringValue(parser, "asciiChars",
          JSONP_Data.asciiCharacters);
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      int parseErrs = JSONP_Util.getParseErrs();
      if (parseErrs != 0) {
        logErr("There were " + parseErrs + " parser errors that occurred.");
        pass = false;
      }
    } catch (Exception e) {
      throw e;
    }
    return pass;
  }

  /*
   * Utitity method to parse JsonObjectWithLotsOfNestedObjectsData
   */
  private boolean parseAndVerify_JsonObjectWithLotsOfNestedObjectsData(
      JsonParser parser) throws Exception {
    boolean pass = true;

    try {
      JSONP_Util.resetParseErrs();
      JSONP_Util.testEventType(parser, JsonParser.Event.START_OBJECT);
      for (int i = 1; i < 31; i++) {
        JSONP_Util.testKeyStartObjectValue(parser, "nested" + i);
        JSONP_Util.testKeyStringValue(parser, "name" + i, "value" + i);
      }
      for (int i = 1; i < 31; i++) {
        JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      }
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      int parseErrs = JSONP_Util.getParseErrs();
      if (parseErrs != 0) {
        logErr("There were " + parseErrs + " parser errors that occurred.");
        pass = false;
      }
    } catch (Exception e) {
      throw e;
    }
    return pass;
  }

  /*
   * Utitity method to parse JsonArrayWithLotsOfNestedObjectsData
   */
  private boolean parseAndVerify_JsonArrayWithLotsOfNestedObjectsData(
      JsonParser parser) throws Exception {
    boolean pass = true;

    try {
      JSONP_Util.resetParseErrs();
      JSONP_Util.testEventType(parser, JsonParser.Event.START_ARRAY);
      JSONP_Util.testEventType(parser, JsonParser.Event.START_OBJECT);
      JSONP_Util.testKeyStringValue(parser, "name1", "value1");
      for (int i = 2; i < 31; i++) {
        JSONP_Util.testKeyStartObjectValue(parser, "nested" + i);
        JSONP_Util.testKeyStringValue(parser, "name" + i, "value" + i);
      }
      for (int i = 2; i < 31; i++) {
        JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      }
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      int parseErrs = JSONP_Util.getParseErrs();
      if (parseErrs != 0) {
        logErr("There were " + parseErrs + " parser errors that occurred.");
        pass = false;
      }
    } catch (Exception e) {
      throw e;
    }
    return pass;
  }

  /*
   * Utitity method to parse JsonArrayWithLotsOfNestedArraysData
   */
  private boolean parseAndVerify_JsonArrayWithLotsOfNestedArraysData(
      JsonParser parser) throws Exception {
    boolean pass = true;

    try {
      JSONP_Util.resetParseErrs();
      JSONP_Util.testEventType(parser, JsonParser.Event.START_ARRAY);
      JSONP_Util.testEventType(parser, JsonParser.Event.START_ARRAY);
      JSONP_Util.testStringValue(parser, "name1");
      JSONP_Util.testStringValue(parser, "value1");
      for (int i = 2; i < 31; i++) {
        JSONP_Util.testStringValue(parser, "nested" + i);
        JSONP_Util.testEventType(parser, JsonParser.Event.START_ARRAY);
        JSONP_Util.testStringValue(parser, "name" + i);
        JSONP_Util.testStringValue(parser, "value" + i);
      }
      for (int i = 2; i < 31; i++) {
        JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      }
      JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      int parseErrs = JSONP_Util.getParseErrs();
      if (parseErrs != 0) {
        logErr("There were " + parseErrs + " parser errors that occurred.");
        pass = false;
      }
    } catch (Exception e) {
      throw e;
    }
    return pass;
  }

  /*
   * Utitity method to parse JsonArrayWithMultipleArraysData
   */
  private boolean parseAndVerify_JsonArrayWithMultipleArraysData(
      JsonParser parser) throws Exception {
    boolean pass = true;

    try {
      JSONP_Util.resetParseErrs();
      JSONP_Util.testEventType(parser, JsonParser.Event.START_ARRAY);
      JSONP_Util.testStringValue(parser, "string");
      JSONP_Util.testIntegerValue(parser, 100);
      JSONP_Util.testTrueValue(parser, JsonParser.Event.VALUE_TRUE);
      JSONP_Util.testFalseValue(parser, JsonParser.Event.VALUE_FALSE);
      JSONP_Util.testNullValue(parser, JsonParser.Event.VALUE_NULL);
      JSONP_Util.testEventType(parser, JsonParser.Event.START_OBJECT);
      JSONP_Util.testKeyStringValue(parser, "object", "object");
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      JSONP_Util.testEventType(parser, JsonParser.Event.START_ARRAY);
      JSONP_Util.testStringValue(parser, "one");
      JSONP_Util.testStringValue(parser, "two");
      JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      JSONP_Util.testEventType(parser, JsonParser.Event.START_ARRAY);
      JSONP_Util.testIntegerValue(parser, 100);
      JSONP_Util.testDoubleValue(parser, (double) 7e7);
      JSONP_Util.testTrueValue(parser, JsonParser.Event.VALUE_TRUE);
      JSONP_Util.testFalseValue(parser, JsonParser.Event.VALUE_FALSE);
      JSONP_Util.testNullValue(parser, JsonParser.Event.VALUE_NULL);
      JSONP_Util.testEventType(parser, JsonParser.Event.START_OBJECT);
      JSONP_Util.testKeyStringValue(parser, "object2", "object2");
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      int parseErrs = JSONP_Util.getParseErrs();
      if (parseErrs != 0) {
        logErr("There were " + parseErrs + " parser errors that occurred.");
        pass = false;
      }
    } catch (Exception e) {
      throw e;
    }
    return pass;
  }

  /*
   * Utitity method to parse JsonArrayWithAllTypesOfData
   */
  private boolean parseAndVerify_JsonArrayWithAllTypesOfData(JsonParser parser)
      throws Exception {
    boolean pass = true;

    try {
      JSONP_Util.resetParseErrs();
      JSONP_Util.testEventType(parser, JsonParser.Event.START_ARRAY);
      JSONP_Util.testStringValue(parser, "");
      JSONP_Util.testEventType(parser, JsonParser.Event.START_ARRAY);
      JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      JSONP_Util.testEventType(parser, JsonParser.Event.START_OBJECT);
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      JSONP_Util.testStringValue(parser, "string");
      JSONP_Util.testIntegerValue(parser, 100);
      JSONP_Util.testTrueValue(parser, JsonParser.Event.VALUE_TRUE);
      JSONP_Util.testFalseValue(parser, JsonParser.Event.VALUE_FALSE);
      JSONP_Util.testNullValue(parser, JsonParser.Event.VALUE_NULL);
      JSONP_Util.testEventType(parser, JsonParser.Event.START_OBJECT);
      JSONP_Util.testKeyStringValue(parser, "emptyString", "");
      JSONP_Util.testKeyStartArrayValue(parser, "emptyArray");
      JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      JSONP_Util.testKeyStartObjectValue(parser, "emptyObject");
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      JSONP_Util.testKeyStringValue(parser, "string", "string");
      JSONP_Util.testKeyIntegerValue(parser, "number", 100);
      JSONP_Util.testKeyTrueValue(parser, "true");
      JSONP_Util.testKeyFalseValue(parser, "false");
      JSONP_Util.testKeyNullValue(parser, "null");
      JSONP_Util.testKeyStartObjectValue(parser, "object");
      JSONP_Util.testKeyStringValue(parser, "name", "value");
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      JSONP_Util.testKeyStartArrayValue(parser, "array");
      JSONP_Util.testStringValue(parser, "one");
      JSONP_Util.testStringValue(parser, "two");
      JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      JSONP_Util.testEventType(parser, JsonParser.Event.START_ARRAY);
      JSONP_Util.testStringValue(parser, "string");
      JSONP_Util.testIntegerValue(parser, 100);
      JSONP_Util.testTrueValue(parser, JsonParser.Event.VALUE_TRUE);
      JSONP_Util.testFalseValue(parser, JsonParser.Event.VALUE_FALSE);
      JSONP_Util.testNullValue(parser, JsonParser.Event.VALUE_NULL);
      JSONP_Util.testEventType(parser, JsonParser.Event.START_OBJECT);
      JSONP_Util.testKeyStringValue(parser, "name", "value");
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      JSONP_Util.testEventType(parser, JsonParser.Event.START_ARRAY);
      JSONP_Util.testStringValue(parser, "one");
      JSONP_Util.testStringValue(parser, "two");
      JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      JSONP_Util.testIntegerValue(parser, 100);
      JSONP_Util.testIntegerValue(parser, -100);
      JSONP_Util.testLongValue(parser, 9223372036854775807L);
      JSONP_Util.testLongValue(parser, -9223372036854775808L);
      JSONP_Util.testDoubleValue(parser, (double) 0.5);
      JSONP_Util.testDoubleValue(parser, (double) -0.5);
      JSONP_Util.testDoubleValue(parser, (double) 7e3);
      JSONP_Util.testDoubleValue(parser, (double) 7e+3);
      JSONP_Util.testDoubleValue(parser, (double) 9E3);
      JSONP_Util.testDoubleValue(parser, (double) 9E+3);
      JSONP_Util.testDoubleValue(parser, (double) 7e-3);
      JSONP_Util.testDoubleValue(parser, (double) 7E-3);
      JSONP_Util.testStringValue(parser, JSONP_Data.asciiCharacters);
      JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      int parseErrs = JSONP_Util.getParseErrs();
      if (parseErrs != 0) {
        logErr("There were " + parseErrs + " parser errors that occurred.");
        pass = false;
      }
    } catch (Exception e) {
      throw e;
    }
    return pass;
  }

  private boolean parseAndVerify_JsonHelloWorld(JsonParser parser)
      throws Exception {
    boolean pass = true;

    try {
      JSONP_Util.resetParseErrs();
      JSONP_Util.testEventType(parser, JsonParser.Event.START_OBJECT);
      JSONP_Util.testKeyStartObjectValue(parser, "greetingObj");
      JSONP_Util.dumpLocation(parser);
      JSONP_Util.testKeyStringValue(parser, "hello", "world");
      JSONP_Util.dumpLocation(parser);
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      JSONP_Util.dumpLocation(parser);
      JSONP_Util.testKeyStartArrayValue(parser, "greetingArr");
      JSONP_Util.dumpLocation(parser);
      JSONP_Util.testStringValue(parser, "hello");
      JSONP_Util.dumpLocation(parser);
      JSONP_Util.testStringValue(parser, "world");
      JSONP_Util.dumpLocation(parser);
      JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      JSONP_Util.dumpLocation(parser);
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      JSONP_Util.dumpLocation(parser);
      int parseErrs = JSONP_Util.getParseErrs();
      if (parseErrs != 0) {
        logErr("There were " + parseErrs + " parser errors that occurred.");
        pass = false;
      }
    } catch (Exception e) {
      throw e;
    }
    return pass;
  }

  /* Tests */

  /*
   * @testName: jsonParserTest1
   * 
   * @assertion_ids: JSONP:JAVADOC:117; JSONP:JAVADOC:120; JSONP:JAVADOC:122;
   * JSONP:JAVADOC:133; JSONP:JAVADOC:235; JSONP:JAVADOC:237; JSONP:JAVADOC:239;
   * JSONP:JAVADOC:375; JSONP:JAVADOC:376;
   * 
   * @test_Strategy: Tests the JsonParser parser. Verifies PARSING of the
   * JsonObject defined in "JSONP_Data.jsonObjectWithAllTypesOfData". Creates
   * the JsonParser via the API:
   *
   * JsonParser parser = Json.createParser(Reader)
   */
  public void jsonParserTest1() throws Fault {
    boolean pass = true;
    JsonParser parser = null;
    JsonParser.Event event = null;
    try {
      logMsg("-------------------------------------");
      logMsg("TEST CASE [Json.createParser(Reader)]");
      logMsg("-------------------------------------");
      logMsg("Create Reader from (JSONP_Data.jsonObjectWithAllTypesOfData)");
      StringReader reader = new StringReader(
          JSONP_Data.jsonObjectWithAllTypesOfData);
      logMsg("Create JsonParser from the Reader");
      parser = Json.createParser(reader);
      logMsg(
          "Verify that JSON Parser Events/Data matches (JSONP_Data.jsonObjectWithAllTypesOfData)");
      pass = parseAndVerify_JsonObjectWithAllTypesOfData(parser);
    } catch (Exception e) {
      throw new Fault("jsonParserTest1 Failed: ", e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
    if (!pass)
      throw new Fault("jsonParserTest1 Failed");
  }

  /*
   * @testName: jsonParserTest2
   * 
   * @assertion_ids: JSONP:JAVADOC:117; JSONP:JAVADOC:120; JSONP:JAVADOC:122;
   * JSONP:JAVADOC:417; JSONP:JAVADOC:235; JSONP:JAVADOC:237; JSONP:JAVADOC:239;
   * JSONP:JAVADOC:375; JSONP:JAVADOC:376; JSONP:JAVADOC:166;
   * 
   * @test_Strategy: Tests the JsonParser parser. Verifies PARSING of the
   * JsonObject defined in "JSONP_Data.jsonObjectWithAllTypesOfData". Creates
   * the JsonParser via the API:
   *
   * JsonParser parser =
   * Json.createParserFactory(Map<String,?>).createParser(JsonObject)
   */
  public void jsonParserTest2() throws Fault {
    boolean pass = true;
    JsonParser parser = null;
    JsonParser.Event event = null;
    try {
      logMsg(
          "----------------------------------------------------------------------------");
      logMsg(
          "TEST CASE [Json.createParserFactory(Map<String,?>).createParser(JsonObject)]");
      logMsg(
          "----------------------------------------------------------------------------");
      logMsg(
          "Create JsonObject from (JSONP_Data.jsonObjectWithAllTypesOfData)");
      JsonObject jsonObj = JSONP_Util
          .createJsonObjectFromString(JSONP_Data.jsonObjectWithAllTypesOfData);
      JSONP_Util.dumpJsonObject(jsonObj);
      logMsg("Create JsonParser from the JsonObject");
      parser = Json.createParserFactory(JSONP_Util.getEmptyConfig())
          .createParser(jsonObj);
      logMsg("parser=" + parser);
      logMsg(
          "Verify that JSON Parser Events/Data matches (JSONP_Data.jsonObjectWithAllTypesOfData)");
      pass = parseAndVerify_JsonObjectWithAllTypesOfData(parser);
    } catch (Exception e) {
      throw new Fault("jsonParserTest2 Failed: ", e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
    if (!pass)
      throw new Fault("jsonParserTest2 Failed");
  }

  /*
   * @testName: jsonParserTest3
   * 
   * @assertion_ids: JSONP:JAVADOC:117; JSONP:JAVADOC:122; JSONP:JAVADOC:133;
   * JSONP:JAVADOC:375; JSONP:JAVADOC:376;
   * 
   * @test_Strategy: Tests the JsonParser parser. Verifies PARSING of the
   * JsonObject defined in "JSONP_Data.jsonObjectWithLotsOfNestedObjectsData".
   * Creates the JsonParser via the API:
   *
   * JsonParser parser = Json.createParser(Reader)
   */
  public void jsonParserTest3() throws Fault {
    boolean pass = true;
    JsonParser parser = null;
    JsonParser.Event event = null;
    try {
      logMsg("-------------------------------------------");
      logMsg("TEST CASE [Json.createParser(Reader) again]");
      logMsg("-------------------------------------------");
      logMsg(
          "Create Reader from (JSONP_Data.jsonObjectWithLotsOfNestedObjectsData)");
      StringReader reader = new StringReader(
          JSONP_Data.jsonObjectWithLotsOfNestedObjectsData);
      logMsg("Create JsonParser from the Reader");
      parser = Json.createParser(reader);
      logMsg(
          "Verify that JSON Parser Events/Data matches (JSONP_Data.jsonObjectWithLotsOfNestedObjectsData)");
      pass = parseAndVerify_JsonObjectWithLotsOfNestedObjectsData(parser);
    } catch (Exception e) {
      throw new Fault("jsonParserTest3 Failed: ", e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
    if (!pass)
      throw new Fault("jsonParserTest3 Failed");
  }

  /*
   * @testName: jsonParserTest4
   * 
   * @assertion_ids: JSONP:JAVADOC:117; JSONP:JAVADOC:122; JSONP:JAVADOC:417;
   * JSONP:JAVADOC:375; JSONP:JAVADOC:376; JSONP:JAVADOC:166;
   * 
   * @test_Strategy: Tests the JsonParser parser. Verifies PARSING of the
   * JsonObject defined in "JSONP_Data.jsonObjectithLotsOfNestedObjectsData".
   * Creates the JsonParser via the API:
   *
   * JsonParser parser =
   * Json.createParserFactory(Map<String,?>).createParser(JsonObject)
   */
  public void jsonParserTest4() throws Fault {
    boolean pass = true;
    JsonParser parser = null;
    JsonParser.Event event = null;
    try {
      logMsg(
          "-----------------------------------------------------------------------------------------");
      logMsg(
          "TEST CASE [Json.createParserFactory(Map<String,?>).createParser(JsonObject object) again]");
      logMsg(
          "-----------------------------------------------------------------------------------------");
      logMsg(
          "Create JsonObject from (JSONP_Data.jsonObjectWithLotsOfNestedObjectsData)");
      JsonObject jsonObj = JSONP_Util.createJsonObjectFromString(
          JSONP_Data.jsonObjectWithLotsOfNestedObjectsData);
      JSONP_Util.dumpJsonObject(jsonObj);
      logMsg("Create JsonParser from the JsonObject");
      parser = Json.createParserFactory(JSONP_Util.getEmptyConfig())
          .createParser(jsonObj);
      logMsg(
          "Verify that JSON Parser Events/Data matches (JSONP_Data.jsonObjectWithLotsOfNestedObjectsData)");
      pass = parseAndVerify_JsonObjectWithLotsOfNestedObjectsData(parser);
    } catch (Exception e) {
      throw new Fault("jsonParserTest4 Failed: ", e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
    if (!pass)
      throw new Fault("jsonParserTest4 Failed");
  }

  /*
   * @testName: jsonParserTest5
   * 
   * @assertion_ids: JSONP:JAVADOC:117; JSONP:JAVADOC:120; JSONP:JAVADOC:122;
   * JSONP:JAVADOC:417; JSONP:JAVADOC:235; JSONP:JAVADOC:237; JSONP:JAVADOC:239;
   * JSONP:JAVADOC:375; JSONP:JAVADOC:376; JSONP:JAVADOC:167;
   * 
   * @test_Strategy: Tests the JsonParser parser. Verifies PARSING of the
   * JsonArray defined in "JSONP_Data.jsonArrayWithMultipleArraysData". Creates
   * the JsonParser via the API:
   *
   * JsonParser parser =
   * Json.createParserFactory(Map<String,?>).createParser(JsonArray)
   */
  public void jsonParserTest5() throws Fault {
    boolean pass = true;
    JsonParser parser = null;
    JsonParser.Event event = null;
    try {
      logMsg(
          "---------------------------------------------------------------------------");
      logMsg(
          "TEST CASE [Json.createParserFactory(Map<String,?>).createParser(JsonArray)]");
      logMsg(
          "---------------------------------------------------------------------------");
      logMsg(
          "Create JsonArray from (JSONP_Data.jsonArrayWithMultipleArraysData)");
      JsonArray jsonArr = JSONP_Util.createJsonArrayFromString(
          JSONP_Data.jsonArrayWithMultipleArraysData);
      JSONP_Util.dumpJsonArray(jsonArr);
      logMsg("Create JsonParser from the JsonArray");
      parser = Json.createParserFactory(JSONP_Util.getEmptyConfig())
          .createParser(jsonArr);
      logMsg(
          "Verify that JSON Parser Events/Data matches (JSONP_Data.jsonArrayWithMultipleArraysData)");
      pass = parseAndVerify_JsonArrayWithMultipleArraysData(parser);
    } catch (Exception e) {
      throw new Fault("jsonParserTest5 Failed: ", e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
    if (!pass)
      throw new Fault("jsonParserTest5 Failed");
  }

  /*
   * @testName: jsonParserTest6
   * 
   * @assertion_ids: JSONP:JAVADOC:117; JSONP:JAVADOC:120; JSONP:JAVADOC:122;
   * JSONP:JAVADOC:172; JSONP:JAVADOC:235; JSONP:JAVADOC:237; JSONP:JAVADOC:239;
   * JSONP:JAVADOC:375; JSONP:JAVADOC:376;
   * 
   * @test_Strategy: Tests the JsonParser parser. Verifies PARSING of the
   * JsonArray defined in resource file "jsonArrayWithAllTypesOfData.json".
   * 
   * Creates the JsonParser via the API:
   *
   * JsonParser parser = Json.createParser(InputStream)
   */
  public void jsonParserTest6() throws Fault {
    boolean pass = true;
    JsonParser parser = null;
    JsonParser.Event event = null;
    try {
      logMsg("------------------------------------------");
      logMsg("TEST CASE [Json.createParser(InputStream)]");
      logMsg("------------------------------------------");
      logMsg(
          "Get InputStream from data file as resource (jsonArrayWithAllTypesOfData.json)");
      InputStream istream = JSONP_Util
          .getInputStreamFromResource("jsonArrayWithAllTypesOfData.json");
      logMsg("Create JsonParser from the InputStream");
      parser = Json.createParser(istream);
      logMsg(
          "Verify that JSON Parser Events/Data matches (jsonArrayWithAllTypesOfData.json)");
      pass = parseAndVerify_JsonArrayWithAllTypesOfData(parser);
    } catch (Exception e) {
      throw new Fault("jsonParserTest6 Failed: ", e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
    if (!pass)
      throw new Fault("jsonParserTest6 Failed");
  }

  /*
   * @testName: jsonParserTest7
   * 
   * @assertion_ids: JSONP:JAVADOC:117; JSONP:JAVADOC:120; JSONP:JAVADOC:122;
   * JSONP:JAVADOC:417; JSONP:JAVADOC:164; JSONP:JAVADOC:235; JSONP:JAVADOC:237;
   * JSONP:JAVADOC:239; JSONP:JAVADOC:375; JSONP:JAVADOC:376;
   * 
   * @test_Strategy: Tests the JsonParser parser with a configuration. Verifies
   * PARSING of the JsonObject defined in
   * "JSONP_Data.jsonObjectWithAllTypesOfData". Creates the JsonParser via the
   * following API
   *
   * JsonParser parser = Json.createParserFactory(Map<String,
   * ?>).createParser(Reader)
   */
  public void jsonParserTest7() throws Fault {
    boolean pass = true;
    JsonParser parser = null;
    JsonParser.Event event = null;
    try {
      logMsg(
          "-------------------------------------------------------------------------");
      logMsg(
          "TEST CASE [Json.createParserFactory(Map<String, ?>).createParser(Reader)]");
      logMsg(
          "-------------------------------------------------------------------------");
      logMsg("Create a Reader from (JSONP_Data.jsonObjectWithAllTypesOfData)");
      StringReader reader = new StringReader(
          JSONP_Data.jsonObjectWithAllTypesOfData);
      logMsg("Create JsonParser using Reader and a configuration");
      parser = Json.createParserFactory(JSONP_Util.getEmptyConfig())
          .createParser(reader);
      logMsg("Call JsonParser.toString() to print the JsonObject");
      parser.toString();
      logMsg(
          "Verify that JSON Parser Events/Data matches (JSONP_Data.jsonObjectWithAllTypesOfData)");
      if (!parseAndVerify_JsonObjectWithAllTypesOfData(parser))
        pass = false;
    } catch (Exception e) {
      throw new Fault("jsonParserTest7 Failed: ", e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
    if (!pass)
      throw new Fault("jsonParserTest7 Failed");
  }

  /*
   * @testName: jsonParserTest8
   * 
   * @assertion_ids: JSONP:JAVADOC:117; JSONP:JAVADOC:122; JSONP:JAVADOC:417;
   * JSONP:JAVADOC:167; JSONP:JAVADOC:375; JSONP:JAVADOC:376;
   * 
   * @test_Strategy: Tests the JsonParser parser with a configuration. Verifies
   * PARSING of the JsonArray defined in
   * "JSONP_Data.jsonArrayWithLotsOfNestedObjectsData". Creates the JsonParser
   * via the following API
   *
   * JsonParser parser = Json.createParserFactory(Map<String,
   * ?>).createParser(JsonArray)
   */
  public void jsonParserTest8() throws Fault {
    boolean pass = true;
    JsonParser parser = null;
    JsonParser.Event event = null;
    try {
      logMsg(
          "----------------------------------------------------------------------------");
      logMsg(
          "TEST CASE [Json.createParserFactory(Map<String, ?>).createParser(JsonArray)]");
      logMsg(
          "----------------------------------------------------------------------------");
      logMsg(
          "Create a JsonArray from (JSONP_Data.jsonArrayWithLotsOfNestedObjectsData)");
      JsonArray jsonArr = JSONP_Util.createJsonArrayFromString(
          JSONP_Data.jsonArrayWithLotsOfNestedObjectsData);
      JSONP_Util.dumpJsonArray(jsonArr);
      logMsg("Create JsonParser using JsonArray and a configuration");
      parser = Json.createParserFactory(JSONP_Util.getEmptyConfig())
          .createParser(jsonArr);
      logMsg("Call JsonParser.toString() to print the JsonObject");
      parser.toString();
      logMsg(
          "Verify that JSON Parser Events/Data matches (JSONP_Data.jsonArrayWithLotsOfNestedObjectsData)");
      if (!parseAndVerify_JsonArrayWithLotsOfNestedObjectsData(parser))
        pass = false;
    } catch (Exception e) {
      throw new Fault("jsonParserTest8 Failed: ", e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
    if (!pass)
      throw new Fault("jsonParserTest8 Failed");
  }

  /*
   * @testName: jsonParserTest9
   * 
   * @assertion_ids: JSONP:JAVADOC:117; JSONP:JAVADOC:120; JSONP:JAVADOC:122;
   * JSONP:JAVADOC:417; JSONP:JAVADOC:167; JSONP:JAVADOC:235; JSONP:JAVADOC:237;
   * JSONP:JAVADOC:239; JSONP:JAVADOC:375; JSONP:JAVADOC:376;
   * 
   * @test_Strategy: Tests the JsonParser parser with an empty configuration.
   * Verifies PARSING of the JsonArray defined in
   * "JSONP_Data.jsonArrayWithMultipleArraysData". Creates the JsonParser via
   * the following API
   * 
   * JsonParser parser = Json.createParserFactory(Map<String,
   * ?>).createParser(JsonArray)
   */
  public void jsonParserTest9() throws Fault {
    boolean pass = true;
    JsonParser parser = null;
    JsonParser.Event event = null;
    try {
      logMsg(
          "----------------------------------------------------------------------------");
      logMsg(
          "TEST CASE [Json.createParserFactory(Map<String, ?>).createParser(JsonArray)]");
      logMsg(
          "----------------------------------------------------------------------------");
      logMsg(
          "Create JsonArray from (JSONP_Data.jsonArrayWithMultipleArraysData)");
      JsonArray jsonArr = JSONP_Util.createJsonArrayFromString(
          JSONP_Data.jsonArrayWithMultipleArraysData);
      JSONP_Util.dumpJsonArray(jsonArr);
      logMsg("Create JsonParser using JsonArray and a configuration");
      parser = Json.createParserFactory(JSONP_Util.getEmptyConfig())
          .createParser(jsonArr);
      logMsg("Call JsonParser.toString() to print the JsonArray");
      parser.toString();
      logMsg(
          "Verify that JSON Parser Events/Data matches (JSONP_Data.jsonArrayWithMultipleArraysData)");
      if (!parseAndVerify_JsonArrayWithMultipleArraysData(parser))
        pass = false;
    } catch (Exception e) {
      throw new Fault("jsonParserTest9 Failed: ", e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
    if (!pass)
      throw new Fault("jsonParserTest9 Failed");
  }

  /*
   * @testName: jsonParserTest10
   * 
   * @assertion_ids: JSONP:JAVADOC:117; JSONP:JAVADOC:122; JSONP:JAVADOC:417;
   * JSONP:JAVADOC:165; JSONP:JAVADOC:375; JSONP:JAVADOC:376;
   * 
   * @test_Strategy: Tests the JsonParser parser. Verifies PARSING of the
   * JsonObject defined in resource file
   * "jsonObjectWithLotsOfNestedObjectsData.json". Creates the JsonParser via
   * the following API
   * 
   * JsonParser parser = Json.createParserFactory(Map<String,
   * ?>).createParser(InputStream)
   */
  public void jsonParserTest10() throws Fault {
    boolean pass = true;
    JsonParser parser = null;
    JsonParser.Event event = null;
    try {
      logMsg(
          "------------------------------------------------------------------------------");
      logMsg(
          "TEST CASE [Json.createParserFactory(Map<String, ?>).createParser(InputStream)]");
      logMsg(
          "------------------------------------------------------------------------------");
      logMsg("Create JsonParser using InputStream and a configuration");
      InputStream istream = JSONP_Util.getInputStreamFromResource(
          "jsonObjectWithLotsOfNestedObjectsData.json");
      parser = Json.createParserFactory(JSONP_Util.getEmptyConfig())
          .createParser(istream);
      logMsg("Call JsonParser.toString() to print the JsonObject");
      parser.toString();
      logMsg(
          "Verify that JSON Parser Events/Data matches (jsonObjectWithLotsOfNestedObjectsData.json)");
      if (!parseAndVerify_JsonObjectWithLotsOfNestedObjectsData(parser))
        pass = false;
    } catch (Exception e) {
      throw new Fault("jsonParserTest10 Failed: ", e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
    if (!pass)
      throw new Fault("jsonParserTest10 Failed");
  }

  /*
   * @testName: jsonParserTest11
   * 
   * @assertion_ids: JSONP:JAVADOC:117; JSONP:JAVADOC:120; JSONP:JAVADOC:122;
   * JSONP:JAVADOC:235; JSONP:JAVADOC:237; JSONP:JAVADOC:239; JSONP:JAVADOC:375;
   * JSONP:JAVADOC:376; JSONP:JAVADOC:417; JSONP:JAVADOC:201;
   * 
   * @test_Strategy: Tests the JsonParser parser. Verifies PARSING of the
   * JsonArray defined in resource file
   * "jsonArrayWithAllTypesOfDataUTF16BE.json". Use UTF-16BE encoding.
   * 
   * Creates the JsonParser via the API:
   *
   * JsonParser parser =
   * Json.createParserFactory(Map<String,?>).createParser(InputStream, Charset)
   */
  public void jsonParserTest11() throws Fault {
    boolean pass = true;
    JsonParser parser = null;
    JsonParser.Event event = null;
    try {
      logMsg(
          "--------------------------------------------------------------------------------------");
      logMsg(
          "TEST CASE [Json.createParserFactory(Map<String,?>).createParser(InputStream, Charset)]");
      logMsg(
          "--------------------------------------------------------------------------------------");
      logMsg(
          "Get InputStream from data file as resource (jsonArrayWithAllTypesOfDataUTF16BE.json)");
      InputStream istream = JSONP_Util.getInputStreamFromResource(
          "jsonArrayWithAllTypesOfDataUTF16BE.json");
      logMsg(
          "Create JsonParser from the InputStream with character encoding UTF-16BE");
      parser = Json.createParserFactory(JSONP_Util.getEmptyConfig())
          .createParser(istream, JSONP_Util.UTF_16BE);
      logMsg(
          "Verify that JSON Parser Events/Data matches (jsonArrayWithAllTypesOfDataUTF16BE.json)");
      pass = parseAndVerify_JsonArrayWithAllTypesOfData(parser);
    } catch (Exception e) {
      throw new Fault("jsonParserTest11 Failed: ", e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
    if (!pass)
      throw new Fault("jsonParserTest11 Failed");
  }

  /*
   * @testName: jsonParserTest12
   * 
   * @assertion_ids: JSONP:JAVADOC:117; JSONP:JAVADOC:122; JSONP:JAVADOC:417;
   * JSONP:JAVADOC:375; JSONP:JAVADOC:376; JSONP:JAVADOC:201;
   * 
   * @test_Strategy: Tests the JsonParser parser. Verifies PARSING of the
   * JsonArray defined in resource file
   * "jsonArrayWithLotsOfNestedArraysData.json". Use UTF-8 encoding.
   * 
   * Creates the JsonParser via the API:
   *
   * JsonParser parser = Json.createParserFactory(Map<String,
   * ?>).createParser(InputStream, Charset)
   */
  public void jsonParserTest12() throws Fault {
    boolean pass = true;
    JsonParser parser = null;
    JsonParser.Event event = null;
    try {
      logMsg(
          "---------------------------------------------------------------------------------------");
      logMsg(
          "TEST CASE [Json.createParserFactory(Map<String, ?>).createParser(InputStream, Charset)]");
      logMsg(
          "---------------------------------------------------------------------------------------");
      logMsg(
          "Get InputStream from data file as resource (jsonArrayWithLotsOfNestedArraysData.json)");
      InputStream istream = JSONP_Util.getInputStreamFromResource(
          "jsonArrayWithLotsOfNestedArraysData.json");
      logMsg(
          "Create JsonParser from the InputStream with character encoding UTF-8 and a configuration");
      parser = Json.createParserFactory(JSONP_Util.getEmptyConfig())
          .createParser(istream, JSONP_Util.UTF_8);
      logMsg(
          "Verify that JSON Parser Events/Data matches (jsonArrayWithLotsOfNestedArraysData.json)");
      if (!parseAndVerify_JsonArrayWithLotsOfNestedArraysData(parser))
        pass = false;
    } catch (Exception e) {
      throw new Fault("jsonParserTest12 Failed: ", e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
    if (!pass)
      throw new Fault("jsonParserTest12 Failed");
  }

  /*
   * @testName: jsonParserTest13
   * 
   * @assertion_ids: JSONP:JAVADOC:117; JSONP:JAVADOC:120; JSONP:JAVADOC:122;
   * JSONP:JAVADOC:201; JSONP:JAVADOC:235; JSONP:JAVADOC:237; JSONP:JAVADOC:239;
   * JSONP:JAVADOC:375; JSONP:JAVADOC:376;
   *
   * @test_Strategy: Tests the JsonParser parser. Verifies PARSING of the
   * JsonObject defined in UTF-16LE encoding resource file
   * "jsonObjectWithAllTypesOfDataUTF16LE.json".
   * 
   * Creates the JsonParser via the API:
   *
   * JsonParser parser = Json.createParserFactory(Map<String,
   * ?>).createParser(InputStream, Charset)
   */
  public void jsonParserTest13() throws Fault {
    boolean pass = true;
    JsonParser parser = null;
    JsonParser.Event event = null;
    try {
      logMsg(
          "---------------------------------------------------------------------------------------");
      logMsg(
          "TEST CASE [Json.createParserFactory(Map<String, ?>).createParser(InputStream, Charset)]");
      logMsg(
          "---------------------------------------------------------------------------------------");
      logMsg(
          "Get InputStream from data file as resource (jsonObjectWithAllTypesOfDataUTF16LE.json)");
      InputStream istream = JSONP_Util.getInputStreamFromResource(
          "jsonObjectWithAllTypesOfDataUTF16LE.json");
      logMsg("Create JsonParser from the InputStream using UTF-16LE encoding");
      parser = Json.createParserFactory(JSONP_Util.getEmptyConfig())
          .createParser(istream, JSONP_Util.UTF_16LE);
      logMsg(
          "Verify that JSON Parser Events/Data matches (jsonObjectWithAllTypesOfDataUTF16LE.json)");
      pass = parseAndVerify_JsonObjectWithAllTypesOfData(parser);
    } catch (Exception e) {
      throw new Fault("jsonParserTest13 Failed: ", e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
    if (!pass)
      throw new Fault("jsonParserTest13 Failed");
  }

  /*
   * @testName: jsonParserTest14
   * 
   * @assertion_ids: JSONP:JAVADOC:117; JSONP:JAVADOC:122; JSONP:JAVADOC:172;
   * JSONP:JAVADOC:375; JSONP:JAVADOC:376; JSONP:JAVADOC:477;
   * 
   * @test_Strategy: Tests the JsonParser parser. Verifies PARSING of the
   * JsonObject defined in resource file "jsonHelloWorld.json.json".
   * 
   * Creates the JsonParser via the API:
   *
   * JsonParser parser = Json.createParser(InputStream)
   */
  public void jsonParserTest14() throws Fault {
    boolean pass = true;
    JsonParser parser = null;
    JsonParser.Event event = null;
    try {
      logMsg("------------------------------------------");
      logMsg("TEST CASE [Json.createParser(InputStream)]");
      logMsg("------------------------------------------");
      logMsg(
          "Get InputStream from data file as resource (jsonHelloWorld.json)");
      InputStream istream = JSONP_Util
          .getInputStreamFromResource("jsonHelloWorld.json");
      logMsg("Create JsonParser from the InputStream");
      parser = Json.createParser(istream);
      logMsg(
          "Verify that JSON Parser Events/Data matches (jsonHelloWorld.json)");
      pass = parseAndVerify_JsonHelloWorld(parser);
    } catch (Exception e) {
      throw new Fault("jsonParserTest14 Failed: ", e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
    if (!pass)
      throw new Fault("jsonParserTest14 Failed");
  }

  /*
   * @testName: parseUTFEncodedTests
   * 
   * @assertion_ids: JSONP:JAVADOC:117; JSONP:JAVADOC:122; JSONP:JAVADOC:417;
   * JSONP:JAVADOC:375; JSONP:JAVADOC:376; JSONP:JAVADOC:201;
   * 
   * @test_Strategy: Tests the JsonParser parser. Verifies PARSING of the
   * JsonObject defined in resource files:
   *
   * jsonObjectEncodingUTF8.json jsonObjectEncodingUTF16.json
   * jsonObjectEncodingUTF16LE.json jsonObjectEncodingUTF16BE.json
   * jsonObjectEncodingUTF32LE.json jsonObjectEncodingUTF32BE.json
   * 
   * Creates the JsonParser via the API:
   *
   * JsonParser parser =
   * Json.createParserFactory(Map<String,?>).createParser(InputStream, Charset)
   *
   * For each supported encoding supported by JSON RFC parse the JsonObject and
   * verify we get the expected results. The Charset encoding is passed in as an
   * argument for each encoding type tested.
   */
  public void parseUTFEncodedTests() throws Fault {
    boolean pass = true;
    JsonParser parser = null;
    JsonParser.Event event = null;
    try {
      logMsg(
          "-----------------------------------------------------------------------------------------------");
      logMsg(
          "TEST CASE [Json.createParserFactory(Map<String,?>).createParser(InputStream, Charset) as UTF-8]");
      logMsg(
          "-----------------------------------------------------------------------------------------------");
      logMsg(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF8.json)");
      InputStream istream = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF8.json");
      logMsg(
          "Create JsonParser from the InputStream with character encoding UTF-8");
      parser = Json.createParserFactory(JSONP_Util.getEmptyConfig())
          .createParser(istream, JSONP_Util.UTF_8);
      logMsg(
          "Verify that JSON Parser Events/Data matches (jsonObjectEncodingUTF8.json)");
      if (!parseAndVerify_JsonObjectUTF(parser))
        pass = false;
    } catch (Exception e) {
      pass = false;
      logErr("Exception occurred testing parsing of UTF-8 encoding: " + e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
    try {
      logMsg(
          "------------------------------------------------------------------------------------------------");
      logMsg(
          "TEST CASE [Json.createParserFactory(Map<String,?>).createParser(InputStream, Charset) as UTF-16]");
      logMsg(
          "------------------------------------------------------------------------------------------------");
      logMsg(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF16.json)");
      InputStream istream = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF16.json");
      logMsg(
          "Create JsonParser from the InputStream with character encoding UTF-16");
      parser = Json.createParserFactory(JSONP_Util.getEmptyConfig())
          .createParser(istream, JSONP_Util.UTF_16);
      logMsg(
          "Verify that JSON Parser Events/Data matches (jsonObjectEncodingUTF16.json)");
      if (!parseAndVerify_JsonObjectUTF(parser))
        pass = false;
    } catch (Exception e) {
      pass = false;
      logErr("Exception occurred testing parsing of UTF-16 encoding: " + e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
    try {
      logMsg(
          "--------------------------------------------------------------------------------------------------");
      logMsg(
          "TEST CASE [Json.createParserFactory(Map<String,?>).createParser(InputStream, Charset) as UTF-16LE]");
      logMsg(
          "--------------------------------------------------------------------------------------------------");
      logMsg(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF16LE.json)");
      InputStream istream = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF16LE.json");
      logMsg(
          "Create JsonParser from the InputStream with character encoding UTF-16LE");
      parser = Json.createParserFactory(JSONP_Util.getEmptyConfig())
          .createParser(istream, JSONP_Util.UTF_16LE);
      logMsg(
          "Verify that JSON Parser Events/Data matches (jsonObjectEncodingUTF16LE.json)");
      if (!parseAndVerify_JsonObjectUTF(parser))
        pass = false;
    } catch (Exception e) {
      pass = false;
      logErr("Exception occurred testing parsing of UTF-16LE encoding: " + e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
    try {
      logMsg(
          "--------------------------------------------------------------------------------------------------");
      logMsg(
          "TEST CASE [Json.createParserFactory(Map<String,?>).createParser(InputStream, Charset) as UTF-16BE]");
      logMsg(
          "--------------------------------------------------------------------------------------------------");
      logMsg(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF16BE.json)");
      InputStream istream = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF16BE.json");
      logMsg(
          "Create JsonParser from the InputStream with character encoding UTF-16BE");
      parser = Json.createParserFactory(JSONP_Util.getEmptyConfig())
          .createParser(istream, JSONP_Util.UTF_16BE);
      logMsg(
          "Verify that JSON Parser Events/Data matches (jsonObjectEncodingUTF16BE.json)");
      if (!parseAndVerify_JsonObjectUTF(parser))
        pass = false;
    } catch (Exception e) {
      pass = false;
      logErr("Exception occurred testing parsing of UTF-16BE encoding: " + e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
    try {
      logMsg(
          "--------------------------------------------------------------------------------------------------");
      logMsg(
          "TEST CASE [Json.createParserFactory(Map<String,?>).createParser(InputStream, Charset) as UTF-32LE]");
      logMsg(
          "--------------------------------------------------------------------------------------------------");
      logMsg(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF32LE.json)");
      InputStream istream = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF32LE.json");
      logMsg(
          "Create JsonParser from the InputStream with character encoding UTF-32LE");
      parser = Json.createParserFactory(JSONP_Util.getEmptyConfig())
          .createParser(istream, JSONP_Util.UTF_32LE);
      logMsg(
          "Verify that JSON Parser Events/Data matches (jsonObjectEncodingUTF32LE.json)");
      if (!parseAndVerify_JsonObjectUTF(parser))
        pass = false;
    } catch (Exception e) {
      pass = false;
      logErr("Exception occurred testing parsing of UTF-32LE encoding: " + e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
    try {
      logMsg(
          "-------------------------------------------------------------------------------------------------");
      logMsg(
          "TEST CASE [Json.createParserFactory(Map<String,?>).createParser(InputStream, Charset) as UTF-32BE]");
      logMsg(
          "-------------------------------------------------------------------------------------------------");
      logMsg(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF32BE.json)");
      InputStream istream = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF32BE.json");
      logMsg(
          "Create JsonParser from the InputStream with character encoding UTF-32BE");
      parser = Json.createParserFactory(JSONP_Util.getEmptyConfig())
          .createParser(istream, JSONP_Util.UTF_32BE);
      logMsg(
          "Verify that JSON Parser Events/Data matches (jsonObjectEncodingUTF32BE.json)");
      if (!parseAndVerify_JsonObjectUTF(parser))
        pass = false;
    } catch (Exception e) {
      pass = false;
      logErr("Exception occurred testing parsing of UTF-32BE encoding: " + e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
    if (!pass)
      throw new Fault("parseUTFEncodedTests Failed");
  }

  /*
   * @testName: parseUTFEncodedTests2
   * 
   * @assertion_ids: JSONP:JAVADOC:117; JSONP:JAVADOC:120; JSONP:JAVADOC:122;
   * JSONP:JAVADOC:172; JSONP:JAVADOC:235; JSONP:JAVADOC:237; JSONP:JAVADOC:239;
   * JSONP:JAVADOC:375; JSONP:JAVADOC:376;
   * 
   * @test_Strategy: Tests the JsonParser parser. Verifies PARSING of the
   * JsonObject defined in resource files and auto-detecting the encoding:
   *
   * jsonObjectEncodingUTF8.json jsonObjectEncodingUTF16LE.json
   * jsonObjectEncodingUTF16BE.json jsonObjectEncodingUTF32LE.json
   * jsonObjectEncodingUTF32BE.json
   * 
   * Creates the JsonParser via the API:
   *
   * JsonParser parser = Json.createParser(InputStream)
   *
   * For each supported encoding supported by JSON RFC the above should
   * auto-detect the encoding and verify we get the expected results.
   */
  public void parseUTFEncodedTests2() throws Fault {
    boolean pass = true;
    JsonParser parser = null;
    JsonParser.Event event = null;
    try {
      logMsg(
          "-------------------------------------------------------------------");
      logMsg(
          "TEST CASE [Json.createParser(InputStream) and auto-detect as UTF-8]");
      logMsg(
          "-------------------------------------------------------------------");
      logMsg(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF8.json)");
      InputStream istream = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF8.json");
      logMsg(
          "Create JsonParser from the InputStream and auto-detect character encoding UTF-8");
      parser = Json.createParser(istream);
      logMsg(
          "Verify that JSON Parser Events/Data matches (jsonObjectEncodingUTF8.json)");
      if (!parseAndVerify_JsonObjectUTF(parser))
        pass = false;
    } catch (Exception e) {
      pass = false;
      logErr("Exception occurred testing parsing of UTF-8 encoding: " + e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
    try {
      logMsg(
          "----------------------------------------------------------------------");
      logMsg(
          "TEST CASE [Json.createParser(InputStream) and auto-detect as UTF-16LE]");
      logMsg(
          "----------------------------------------------------------------------");
      logMsg(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF16LE.json)");
      InputStream istream = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF16LE.json");
      logMsg(
          "Create JsonParser from the InputStream and auto-detect character encoding UTF-16LE");
      parser = Json.createParser(istream);
      logMsg(
          "Verify that JSON Parser Events/Data matches (jsonObjectEncodingUTF16LE.json)");
      if (!parseAndVerify_JsonObjectUTF(parser))
        pass = false;
    } catch (Exception e) {
      pass = false;
      logErr("Exception occurred testing parsing of UTF-16LE encoding: " + e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
    try {
      logMsg(
          "----------------------------------------------------------------------");
      logMsg(
          "TEST CASE [Json.createParser(InputStream) and auto-detect as UTF-16BE]");
      logMsg(
          "----------------------------------------------------------------------");
      logMsg(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF16BE.json)");
      InputStream istream = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF16BE.json");
      logMsg(
          "Create JsonParser from the InputStream and auto-detect character encoding UTF-16BE");
      parser = Json.createParser(istream);
      logMsg(
          "Verify that JSON Parser Events/Data matches (jsonObjectEncodingUTF16BE.json)");
      if (!parseAndVerify_JsonObjectUTF(parser))
        pass = false;
    } catch (Exception e) {
      pass = false;
      logErr("Exception occurred testing parsing of UTF-16BE encoding: " + e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
    try {
      logMsg(
          "----------------------------------------------------------------------");
      logMsg(
          "TEST CASE [Json.createParser(InputStream) and auto-detect as UTF-32LE]");
      logMsg(
          "----------------------------------------------------------------------");
      logMsg(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF32LE.json)");
      InputStream istream = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF32LE.json");
      logMsg(
          "Create JsonParser from the InputStream and auto-detect character encoding UTF-32LE");
      parser = Json.createParser(istream);
      logMsg(
          "Verify that JSON Parser Events/Data matches (jsonObjectEncodingUTF32LE.json)");
      if (!parseAndVerify_JsonObjectUTF(parser))
        pass = false;
    } catch (Exception e) {
      pass = false;
      logErr("Exception occurred testing parsing of UTF-32LE encoding: " + e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
    try {
      logMsg(
          "----------------------------------------------------------------------");
      logMsg(
          "TEST CASE [Json.createParser(InputStream) and auto-detect as UTF-32BE]");
      logMsg(
          "----------------------------------------------------------------------");
      logMsg(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF32BE.json)");
      InputStream istream = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF32BE.json");
      logMsg(
          "Create JsonParser from the InputStream and auto-detect character encoding UTF-32BE");
      parser = Json.createParser(istream);
      logMsg(
          "Verify that JSON Parser Events/Data matches (jsonObjectEncodingUTF32BE.json)");
      if (!parseAndVerify_JsonObjectUTF(parser))
        pass = false;
    } catch (Exception e) {
      pass = false;
      logErr("Exception occurred testing parsing of UTF-32BE encoding: " + e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }

    if (!pass)
      throw new Fault("parseUTFEncodedTests2 Failed");
  }

  /*
   * @testName: jsonParserIsIntegralNumberTest
   * 
   * @assertion_ids: JSONP:JAVADOC:120; JSONP:JAVADOC:133; JSONP:JAVADOC:375;
   * JSONP:JAVADOC:376;
   * 
   * @test_Strategy: Test JsonParser.isIntegralNumber() method.
   */
  public void jsonParserIsIntegralNumberTest() throws Fault {
    boolean pass = true;
    JsonParser parser = null;
    String jsonTestString = "[123, 12345.45]";
    try {
      logMsg("Create JsonParser");
      parser = Json.createParser(new StringReader(jsonTestString));
      // INTEGRAL NUMBER TEST
      JsonParser.Event event = JSONP_Util.getNextSpecificParserEvent(parser,
          JsonParser.Event.VALUE_NUMBER); // e=JsonParser.Event.VALUE_NUMBER
      JSONP_Util.dumpEventType(event);
      if (!JSONP_Util.assertEqualsJsonNumberType(parser.isIntegralNumber(),
          JSONP_Util.INTEGRAL))
        pass = false;
      else {
        if (!JSONP_Util.assertEquals(123, parser.getInt()))
          pass = false;
      }
      // NON_INTEGRAL NUMBER TEST
      event = JSONP_Util.getNextSpecificParserEvent(parser,
          JsonParser.Event.VALUE_NUMBER); // e=JsonParser.Event.VALUE_NUMBER
      JSONP_Util.dumpEventType(event);
      if (!JSONP_Util.assertEqualsJsonNumberType(parser.isIntegralNumber(),
          JSONP_Util.NON_INTEGRAL))
        pass = false;
      else {
        if (!JSONP_Util.assertEquals(12345.45,
            parser.getBigDecimal().doubleValue()))
          pass = false;
      }

    } catch (Exception e) {
      throw new Fault("jsonParserIsIntegralNumberTest Failed: ", e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }

    if (!pass)
      throw new Fault("jsonParserIsIntegralNumberTest Failed");
  }

  private boolean tripIllegalStateException(JsonParser parser,
      JsonParser.Event event) {
    boolean pass = true;

    // Check in case event is null
    if (event == null) {
      logErr("event is null - unexpected.");
      return false;
    }
    logMsg("Event=" + JSONP_Util.getEventTypeString(event));
    logMsg("Testing call to JsonParser.getString()");
    if (event != JsonParser.Event.VALUE_STRING
        && event != JsonParser.Event.VALUE_NUMBER
        && event != JsonParser.Event.KEY_NAME) {
      try {
        logMsg("Trip IllegalStateException by calling JsonParser.getString()");
        String string = parser.getString();
        pass = false;
        logErr("Failed to throw IllegalStateException");
      } catch (IllegalStateException e) {
        logMsg("Got expected IllegalStateException");
      } catch (Exception e) {
        pass = false;
        logErr("Caught unexpected exception: " + e);
      }
    } else {
      logMsg("No testing for IllegalStateException for this scenario.");
    }

    logMsg("Testing call to JsonParser.isIntegralNumber()");
    if (event != JsonParser.Event.VALUE_NUMBER) {
      try {
        logMsg(
            "Trip IllegalStateException by calling JsonParser.isIntegralNumber()");
        boolean numberType = parser.isIntegralNumber();
        pass = false;
        logErr("Failed to throw IllegalStateException");
      } catch (IllegalStateException e) {
        logMsg("Got expected IllegalStateException");
      } catch (Exception e) {
        pass = false;
        logErr("Caught unexpected exception: " + e);
      }
    } else {
      logMsg("No testing for IllegalStateException for this scenario.");
    }

    logMsg("Testing call to JsonParser.getBigDecimal()");
    if (event != JsonParser.Event.VALUE_NUMBER) {
      try {
        logMsg(
            "Trip IllegalStateException by calling JsonParser.getBigDecimal()");
        BigDecimal number = parser.getBigDecimal();
        pass = false;
        logErr("Failed to throw IllegalStateException");
      } catch (IllegalStateException e) {
        logMsg("Got expected IllegalStateException");
      } catch (Exception e) {
        pass = false;
        logErr("Caught unexpected exception: " + e);
      }
    } else {
      logMsg("No testing for IllegalStateException for this scenario.");
    }

    logMsg("Testing call to JsonParser.getInt()");
    if (event != JsonParser.Event.VALUE_NUMBER) {
      try {
        logMsg("Trip IllegalStateException by calling JsonParser.getInt()");
        int number = parser.getInt();
        pass = false;
        logErr("Failed to throw IllegalStateException");
      } catch (IllegalStateException e) {
        logMsg("Got expected IllegalStateException");
      } catch (Exception e) {
        pass = false;
        logErr("Caught unexpected exception: " + e);
      }
    } else {
      logMsg("No testing for IllegalStateException for this scenario.");
    }

    logMsg("Testing call to JsonParser.getLong()");
    if (event != JsonParser.Event.VALUE_NUMBER) {
      try {
        logMsg("Trip IllegalStateException by calling JsonParser.getLong()");
        long number = parser.getLong();
        pass = false;
        logErr("Failed to throw IllegalStateException");
      } catch (IllegalStateException e) {
        logMsg("Got expected IllegalStateException");
      } catch (Exception e) {
        pass = false;
        logErr("Caught unexpected exception: " + e);
      }
    } else {
      logMsg("No testing for IllegalStateException for this scenario.");
    }
    return pass;
  }

  /*
   * @testName: jsonParserIllegalExceptionTests
   * 
   * @assertion_ids: JSONP:JAVADOC:121; JSONP:JAVADOC:123; JSONP:JAVADOC:236;
   * JSONP:JAVADOC:238; JSONP:JAVADOC:240;
   * 
   * @test_Strategy: Test JsonParser exception conditions. Trip the following
   * exceptions:
   *
   * java.lang.IllegalStateException
   */
  public void jsonParserIllegalExceptionTests() throws Fault {
    boolean pass = true;
    JsonParser parser = null;
    String jsonTestString = "[\"string\",100,false,null,true,{\"foo\":\"bar\"}]";
    try {
      logMsg("Create JsonParser");
      parser = Json.createParserFactory(JSONP_Util.getEmptyConfig())
          .createParser(new StringReader(jsonTestString));
      JsonParser.Event event = JSONP_Util
          .getNextParserEvent(parser); /* e=JsonParser.Event.START_ARRAY */
      if (!tripIllegalStateException(parser, event))
        pass = false;
      event = JSONP_Util
          .getNextParserEvent(parser); /* e=JsonParser.Event.VALUE_STRING */
      if (!tripIllegalStateException(parser, event))
        pass = false;
      event = JSONP_Util
          .getNextParserEvent(parser); /* e=JsonParser.Event.VALUE_NUMBER */
      if (!tripIllegalStateException(parser, event))
        pass = false;
      event = JSONP_Util
          .getNextParserEvent(parser); /* e=JsonParser.Event.VALUE_FALSE */
      if (!tripIllegalStateException(parser, event))
        pass = false;
      event = JSONP_Util
          .getNextParserEvent(parser); /* e=JsonParser.Event.VALUE_NULL */
      if (!tripIllegalStateException(parser, event))
        pass = false;
      event = JSONP_Util
          .getNextParserEvent(parser); /* e=JsonParser.Event.VALUE_TRUE */
      if (!tripIllegalStateException(parser, event))
        pass = false;
      event = JSONP_Util
          .getNextParserEvent(parser); /* e=JsonParser.Event.START_OBJECT */
      if (!tripIllegalStateException(parser, event))
        pass = false;
      event = JSONP_Util
          .getNextParserEvent(parser); /* e=JsonParser.Event.KEY_NAME */
      if (!tripIllegalStateException(parser, event))
        pass = false;
      event = JSONP_Util
          .getNextParserEvent(parser); /* e=JsonParser.Event.VALUE_STRING */
      if (!tripIllegalStateException(parser, event))
        pass = false;
      event = JSONP_Util
          .getNextParserEvent(parser); /* e=JsonParser.Event.END_OBJECT */
      if (!tripIllegalStateException(parser, event))
        pass = false;
      event = JSONP_Util
          .getNextParserEvent(parser); /* e=JsonParser.Event.END_ARRAY */
      if (!tripIllegalStateException(parser, event))
        pass = false;
    } catch (Exception e) {
      throw new Fault("jsonParserIllegalExceptionTests Failed: ", e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }

    if (!pass)
      throw new Fault("jsonParserIllegalExceptionTests Failed");
  }

  /*
   * @testName: jsonParserIOErrorTests
   * 
   * @assertion_ids: JSONP:JAVADOC:207; JSONP:JAVADOC:389; JSONP:JAVADOC:415;
   * 
   * @test_Strategy: Tests for JsonException for testable i/o errors.
   *
   */
  @SuppressWarnings("ConvertToTryWithResources")
  public void jsonParserIOErrorTests() throws Fault {
    boolean pass = true;

    String jsonText = "{\"name1\":\"value1\",\"name2\":\"value2\"}";

    // Trip JsonException if there is an i/o error on
    // Json.createParser(InputStream)
    try {
      logMsg(
          "Trip JsonException if there is an i/o error on Json.createParser(InputStream).");
      logMsg("Parsing " + jsonText);
      InputStream is = JSONP_Util.getInputStreamFromString(jsonText);
      MyBufferedInputStream mbi = new MyBufferedInputStream(is, true);
      logMsg("Calling Json.createParser(InputStream)");
      JsonParser parser = Json.createParser(mbi);
      logErr("Did not get expected JsonException");
      pass = false;
    } catch (JsonException e) {
      logMsg("Caught expected JsonException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    }

    // Trip JsonException if there is an i/o error on JsonParser.next()
    try {
      logMsg(
          "Trip JsonException if there is an i/o error on JsonParser.next().");
      logMsg("Parsing " + jsonText);
      InputStream is = JSONP_Util.getInputStreamFromString(jsonText);
      MyBufferedInputStream mbi = new MyBufferedInputStream(is, true);
      JsonParser parser = Json.createParser(mbi);
      logMsg("Calling JsonParser.next()");
      parser.next();
      logErr("Did not get expected JsonException");
      pass = false;
    } catch (JsonException e) {
      logMsg("Caught expected JsonException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    }

    // Trip JsonException if there is an i/o error on JsonParser.close()
    try {
      logMsg(
          "Trip JsonException if there is an i/o error on JsonParser.close().");
      logMsg("Parsing " + jsonText);
      InputStream is = JSONP_Util.getInputStreamFromString(jsonText);
      MyBufferedInputStream mbi = new MyBufferedInputStream(is);
      JsonParser parser = Json.createParser(mbi);
      mbi.setThrowIOException(true);
      logMsg("Calling JsonParser.close()");
      parser.close();
      logErr("Did not get expected JsonException");
      pass = false;
    } catch (JsonException e) {
      logMsg("Caught expected JsonException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    }

    if (!pass)
      throw new Fault("jsonParserIOErrorTests Failed");
  }

  /*
   * @testName: jsonParserExceptionTests
   * 
   * @assertion_ids: JSONP:JAVADOC:390; JSONP:JAVADOC:391;
   * 
   * @test_Strategy: Tests for the following exception test cases:
   *
   * JsonParsingException - if incorrect JSON is encountered while advancing
   * parser to next state NoSuchElementException - if there are no more parsing
   * states
   *
   */
  public void jsonParserExceptionTests() throws Fault {
    boolean pass = true;

    // Trip JsonParsingException for JsonParser.next() if incorrect JSON is
    // encountered
    try {
      logMsg(
          "Trip JsonParsingException for JsonParser.next() if incorrect JSON is encountered");
      InputStream is = JSONP_Util.getInputStreamFromString("}{");
      JsonParser parser = Json.createParser(is);
      parser.next();
      logErr("Did not get expected JsonParsingException");
      pass = false;
    } catch (JsonParsingException e) {
      logMsg("Caught expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    }

    // Trip NoSuchElementException for JsonParser.next() if no more parsing
    // states
    try {
      logMsg(
          "Trip NoSuchElementException for JsonParser.next() if no more parsing states");
      InputStream is = JSONP_Util.getInputStreamFromString("{}");
      JsonParser parser = Json.createParser(is);
      parser.next(); // Event -> START_OBJECT {
      parser.next(); // Event -> END_OBJECT }
      parser.next(); // Event -> NoSuchElementException should be thrown
      logErr("Did not get expected NoSuchElementException");
      pass = false;
    } catch (NoSuchElementException e) {
      logMsg("Caught expected NoSuchElementException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    }

    if (!pass)
      throw new Fault("jsonParserExceptionTests Failed");
  }

  /*
   * @testName: invalidLiteralNamesTest
   * 
   * @assertion_ids: JSONP:JAVADOC:133; JSONP:JAVADOC:390;
   * 
   * @test_Strategy: This test trips various JsonParsingException conditions
   * when parsing an uppercase literal name that must be lowercase per JSON RFC
   * for the literal values (true, false or null).
   *
   */
  public void invalidLiteralNamesTest() throws Fault {
    boolean pass = true;

    // Trip JsonParsingException for JsonParser.next() if invalid liternal TRUE
    // instead of true
    try {
      logMsg(
          "Trip JsonParsingException for JsonParser.next() if invalid liternal TRUE instead of true.");
      logMsg("Reading " + "[TRUE]");
      JsonParser parser = Json.createParser(new StringReader("[TRUE]"));
      parser.next(); // Event -> START_OBJECT {
      parser.next(); // Event -> JsonParsingException (invalid literal TRUE)
      logErr("Did not get expected JsonParsingException");
      pass = false;
    } catch (JsonParsingException e) {
      logMsg("Caught expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    }

    // Trip JsonParsingException for JsonParser.next() if invalid liternal FALSE
    // instead of false
    try {
      logMsg(
          "Trip JsonParsingException for JsonParser.next() if invalid liternal FALSE instead of false.");
      logMsg("Reading " + "[FALSE]");
      JsonParser parser = Json.createParser(new StringReader("[FALSE]"));
      parser.next(); // Event -> START_OBJECT {
      parser.next(); // Event -> JsonParsingException (invalid literal FALSE)
      logErr("Did not get expected JsonParsingException");
      pass = false;
    } catch (JsonParsingException e) {
      logMsg("Caught expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    }

    // Trip JsonParsingException for JsonParser.next() if invalid liternal NULL
    // instead of null
    try {
      logMsg(
          "Trip JsonParsingException for JsonParser.next() if invalid liternal NULL instead of null.");
      logMsg("Reading " + "[NULL]");
      JsonParser parser = Json.createParser(new StringReader("[NULL]"));
      parser.next(); // Event -> START_OBJECT {
      parser.next(); // Event -> JsonParsingException (invalid literal NULL)
      logErr("Did not get expected JsonParsingException");
      pass = false;
    } catch (JsonParsingException e) {
      logMsg("Caught expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    }

    if (!pass)
      throw new Fault("invalidLiteralNamesTest Failed");
  }

  /*
   * @testName: jsonParser11Test
   * 
   * @assertion_ids: JSONP:JAVADOC:673; JSONP:JAVADOC:674; JSONP:JAVADOC:675;
   * JSONP:JAVADOC:676; JSONP:JAVADOC:677; JSONP:JAVADOC:678; JSONP:JAVADOC:679;
   * JSONP:JAVADOC:680; JSONP:JAVADOC:583; JSONP:JAVADOC:584; JSONP:JAVADOC:585;
   * JSONP:JAVADOC:586; JSONP:JAVADOC:587; JSONP:JAVADOC:588; JSONP:JAVADOC:662;
   * JSONP:JAVADOC:663; JSONP:JAVADOC:664; JSONP:JAVADOC:665; JSONP:JAVADOC:666;
   * JSONP:JAVADOC:667;
   * 
   * @test_Strategy: Tests JsonParser API methods added in JSON-P 1.1.
   */
  public void jsonParser11Test() throws Fault {
    Parser parserTest = new Parser();
    final TestResult result = parserTest.test();
    result.eval();
  }

}
