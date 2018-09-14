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
package com.sun.ts.tests.jsonp.api.jsonvaluetests;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jsonp.api.common.TestResult;
import com.sun.ts.tests.jsonp.common.*;
import java.util.*;
import javax.json.*;

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

  /* Tests */

  /*
   * @testName: jsonValueTypesTest
   * 
   * @assertion_ids: JSONP:JAVADOC:102;
   * 
   * @test_Strategy: Test JsonValue.getValueType() API method call with all
   * JsonValue types.
   *
   */
  public void jsonValueTypesTest() throws Fault {
    boolean pass = true;
    try {

      JsonValue.ValueType valueType;

      // Testing JsonValue.FALSE case
      logMsg("Testing getValueType for JsonValue.FALSE value");
      valueType = JsonValue.FALSE.getValueType();
      if (valueType != JsonValue.ValueType.FALSE) {
        logErr("Expected JSON FALSE value type but got instead " + valueType);
        pass = false;
      } else
        logMsg("Got expected value type for JSON FALSE value");

      // Testing JsonValue.TRUE case
      logMsg("Testing getValueType for JsonValue.TRUE value");
      valueType = JsonValue.TRUE.getValueType();
      if (valueType != JsonValue.ValueType.TRUE) {
        logErr("Expected JSON TRUE value type but got instead " + valueType);
        pass = false;
      } else
        logMsg("Got expected value type for JSON TRUE value");

      // Testing JsonValue.NULL case
      logMsg("Testing getValueType for JsonValue.NULL value");
      valueType = JsonValue.NULL.getValueType();
      if (valueType != JsonValue.ValueType.NULL) {
        logErr("Expected JSON NULL value type but got instead " + valueType);
        pass = false;
      } else
        logMsg("Got expected value type for JSON NULL value");

      // Testing JsonValue.String case
      logMsg("Testing getValueType for JsonValue.String value");
      valueType = JSONP_Util.createJsonString("string").getValueType();
      if (valueType != JsonValue.ValueType.STRING) {
        logErr("Expected JSON STRING value type but got instead " + valueType);
        pass = false;
      } else
        logMsg("Got expected value type for JSON STRING value");

      // Testing JsonValue.Number case
      logMsg("Testing getValueType for JsonValue.Number value");
      valueType = JSONP_Util.createJsonNumber(Integer.MAX_VALUE).getValueType();
      if (valueType != JsonValue.ValueType.NUMBER) {
        logErr("Expected JSON NUMBER value type but got instead " + valueType);
        pass = false;
      } else
        logMsg("Got expected value type for JSON NUMBER value");

      // Testing JsonValue.Array case
      logMsg("Testing getValueType for JsonValue.Array value");
      valueType = JSONP_Util.createJsonArrayFromString("[]").getValueType();
      if (valueType != JsonValue.ValueType.ARRAY) {
        logErr("Expected JSON ARRAY value type but got instead " + valueType);
        pass = false;
      } else
        logMsg("Got expected value type for JSON ARRAY value");

      // Testing JsonValue.Object case
      logMsg("Testing getValueType for JsonValue.Object value");
      valueType = JSONP_Util.createJsonObjectFromString("{}").getValueType();
      if (valueType != JsonValue.ValueType.OBJECT) {
        logErr("Expected JSON OBJECT value type but got instead " + valueType);
        pass = false;
      } else
        logMsg("Got expected value type for JSON OBJECT value");

    } catch (Exception e) {
      throw new Fault("jsonValueTypesTest Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonValueTypesTest Failed");
  }

  /*
   * @testName: jsonValueOfTest
   * 
   * @assertion_ids: JSONP:JAVADOC:103;
   * 
   * @test_Strategy: Test JsonValue.ValueType.valueOf() API method call with all
   * JsonValue types.
   *
   */
  public void jsonValueOfTest() throws Fault {
    boolean pass = true;

    String valueTypeStrings[] = { "ARRAY", "FALSE", "NULL", "NUMBER", "OBJECT",
        "STRING", "TRUE" };

    for (String valueTypeString : valueTypeStrings) {
      JsonValue.ValueType valueType;
      try {
        logMsg(
            "Testing enum value for string constant name " + valueTypeString);
        valueType = JsonValue.ValueType.valueOf(valueTypeString);
        logMsg("Got enum type " + valueType + " for enum string constant named "
            + valueTypeString);
      } catch (Exception e) {
        logErr("Caught unexpected exception: " + e);
        pass = false;
      }

    }

    logMsg("Testing negative test case for NullPointerException");
    try {
      JsonValue.ValueType.valueOf(null);
      logErr("did not get expected NullPointerException");
      pass = false;
    } catch (NullPointerException e) {
      logMsg("Got expected NullPointerException");
    } catch (Exception e) {
      logErr("Got unexpected exception " + e);
      pass = false;
    }

    logMsg("Testing negative test case for IllegalArgumentException");
    try {
      JsonValue.ValueType.valueOf("INVALID");
      logErr("did not get expected IllegalArgumentException");
      pass = false;
    } catch (IllegalArgumentException e) {
      logMsg("Got expected IllegalArgumentException");
    } catch (Exception e) {
      logErr("Got unexpected exception " + e);
      pass = false;
    }

    if (!pass)
      throw new Fault("jsonValueOfTest Failed");
  }

  /*
   * @testName: jsonValuesTest
   * 
   * @assertion_ids: JSONP:JAVADOC:104;
   * 
   * @test_Strategy: Test JsonValue.ValueType.values() API method call and
   * verify enums returned.
   *
   */
  public void jsonValuesTest() throws Fault {
    boolean pass = true;

    logMsg(
        "Testing API method JsonValue.ValueType.values() to return array of enums.");
    JsonValue.ValueType[] values = JsonValue.ValueType.values();

    for (JsonValue.ValueType valueType : values) {
      String valueString = JSONP_Util.getValueTypeString(valueType);
      if (valueString == null) {
        logErr("Got no value for enum " + valueType);
        pass = false;
      } else
        logMsg("Got " + valueString + " for enum " + valueType);
    }

    if (!pass)
      throw new Fault("jsonValuesTest Failed");
  }

  /*
   * @testName: jsonValueToStringTest
   * 
   * @assertion_ids: JSONP:JAVADOC:288;
   * 
   * @test_Strategy: Test JsonValue.toString() API method call with various
   * JsonValue types.
   *
   */
  public void jsonValueToStringTest() throws Fault {
    boolean pass = true;
    try {
      String stringValue;
      JsonValue jsonValue;

      // Testing JsonValue.FALSE case
      logMsg("Testing JsonValue.toString() for JsonValue.FALSE value");
      stringValue = JsonValue.FALSE.toString();
      logMsg("stringValue=" + stringValue);
      if (!stringValue.equals("false")) {
        logErr("Expected false");
        pass = false;
      } else {
        logMsg("Got " + stringValue);
      }

      // Testing JsonValue.TRUE case
      logMsg("Testing JsonValue.toString() for JsonValue.TRUE value");
      stringValue = JsonValue.TRUE.toString();
      logMsg("stringValue=" + stringValue);
      if (!stringValue.equals("true")) {
        logErr("Expected true");
        pass = false;
      } else {
        logMsg("Got " + stringValue);
      }

      // Testing JsonValue.NULL case
      logMsg("Testing JsonValue.toString() for JsonValue.NULL value");
      stringValue = JsonValue.NULL.toString();
      logMsg("stringValue=" + stringValue);
      if (!stringValue.equals("null")) {
        logErr("Expected null");
        pass = false;
      } else {
        logMsg("Got " + stringValue);
      }

      // Testing JsonString case
      logMsg("Testing JsonValue.toString() for JsonString value");
      jsonValue = JSONP_Util.createJsonString("string");
      stringValue = jsonValue.toString();
      logMsg("stringValue=" + stringValue);
      if (!stringValue.equals("\"string\"")) {
        logErr("Expected \"string\"");
        pass = false;
      } else {
        logMsg("Got " + stringValue);
      }

      // Testing JsonNumber case
      logMsg("Testing JsonValue.toString() for JsonNumber value");
      jsonValue = JSONP_Util.createJsonNumber(10);
      stringValue = jsonValue.toString();
      logMsg("stringValue=" + stringValue);
      if (!stringValue.equals("10")) {
        logErr("Expected 10");
        pass = false;
      } else {
        logMsg("Got " + stringValue);
      }

      // Testing JsonArray case
      logMsg("Testing JsonValue.toString() for JsonArray value");
      jsonValue = JSONP_Util.createJsonArrayFromString("[]");
      stringValue = jsonValue.toString();
      logMsg("stringValue=" + stringValue);
      if (!stringValue.equals("[]")) {
        logErr("Expected []");
        pass = false;
      } else {
        logMsg("Got " + stringValue);
      }

      // Testing JsonObject case
      logMsg("Testing JsonValue.toString() for JsonObject value");
      jsonValue = JSONP_Util.createJsonObjectFromString("{}");
      stringValue = jsonValue.toString();
      logMsg("stringValue=" + stringValue);
      if (!stringValue.equals("{}")) {
        logErr("Expected {}");
        pass = false;
      } else {
        logMsg("Got " + stringValue);
      }

    } catch (Exception e) {
      throw new Fault("jsonValueToStringTest Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonValueToStringTest Failed");
  }

  /*
   * @testName: jsonValue11Test
   * 
   * @assertion_ids: JSONP:JAVADOC:648; JSONP:JAVADOC:649;
   * 
   * @test_Strategy: Tests JsonValue API methods added in JSON-P 1.1.
   */
  public void jsonValue11Test() throws Fault {
    Value valueTest = new Value();
    final TestResult result = valueTest.test();
    result.eval();
  }

  /*
   * @testName: jsonStructure11Test
   * 
   * @assertion_ids: JSONP:JAVADOC:647;
   * 
   * @test_Strategy: Tests JsonStructure API methods added in JSON-P 1.1.
   */
  public void jsonStructure11Test() throws Fault {
    Structure structTest = new Structure();
    final TestResult result = structTest.test();
    result.eval();
  }

}
