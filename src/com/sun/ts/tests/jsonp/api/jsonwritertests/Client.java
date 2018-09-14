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

package com.sun.ts.tests.jsonp.api.jsonwritertests;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.tests.jsonp.api.common.TestResult;
import com.sun.ts.tests.jsonp.common.*;
import java.io.*;
import java.util.*;
import javax.json.*;

// $Id$
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
   * @testName: jsonWriterTest1
   * 
   * @assertion_ids: JSONP:JAVADOC:105; JSONP:JAVADOC:106; JSONP:JAVADOC:110;
   * 
   * @test_Strategy: Tests JsonWriter API's for writing out a JsonObject.
   * Comparison is done by reading the JsonWriter output using JsonReader and
   * recreating the JsonObject and than performing a JsonObject comparison for
   * equality.
   *
   * Tests using API methods: Json.createWriter(Writer) and
   * writer.writeObject(JsonObject)
   *
   */
  public void jsonWriterTest1() throws Fault {
    boolean pass = true;
    try {

      logMsg("Create sample JsonObject for testing");
      JsonObject myJsonObject1 = JSONP_Util.createSampleJsonObject();

      logMsg("Write the JsonObject 'myJsonObject1' out to a JsonWriter");
      StringWriter sWriter = new StringWriter();
      try (JsonWriter writer = Json.createWriter(sWriter)) {
        writer.writeObject(myJsonObject1);
        logMsg("Close JsonWriter");
      }

      logMsg("Save contents of the JsonWriter as a String");
      String contents = sWriter.toString();

      logMsg("Dump contents of JsonWriter as a String");
      logMsg("JsonWriterContents=" + contents);

      logMsg(
          "Read the JsonObject back into 'myJsonObject2' using a JsonReader");
      JsonReader reader = Json.createReader(new StringReader(contents));
      JsonObject myJsonObject2 = (JsonObject) reader.read();

      logMsg("Compare myJsonObject1 and myJsonObject2 for equality");
      pass = JSONP_Util.assertEqualsJsonObjects(myJsonObject1, myJsonObject2);
    } catch (Exception e) {
      throw new Fault("jsonWriterTest1 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonWriterTest1 Failed");
  }

  /*
   * @testName: jsonWriterTest2
   * 
   * @assertion_ids: JSONP:JAVADOC:105; JSONP:JAVADOC:187; JSONP:JAVADOC:110;
   * 
   * @test_Strategy: Tests JsonWriter API's for writing out a JsonObject.
   * Comparison is done by comparing the expected JsonObject text output with
   * the actual JsonObject text output from the JsonWriter for equality.
   *
   * Tests using API methods: Json.createWriter(OutputStream) and
   * writer.writeObject(JsonObject)
   *
   */
  public void jsonWriterTest2() throws Fault {
    boolean pass = true;
    try {

      logMsg("Create sample JsonObject for testing");
      JsonObject myJsonObject1 = JSONP_Util.createSampleJsonObject();

      logMsg("Write the JsonObject 'myJsonObject1' out to a JsonWriter");
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      JsonWriter writer = Json.createWriter(baos);
      writer.writeObject(myJsonObject1);
      logMsg("Close JsonWriter");
      baos.close();
      writer.close();

      logMsg("Save contents of the JsonWriter as a String");
      String actJsonObjectText = baos.toString("UTF-8");

      logMsg("Dump contents of JsonWriter as a String");
      logMsg("JsonWriterContents=" + actJsonObjectText);

      logMsg(
          "Compare expected JsonObject text with actual JsonObject text for equality");
      pass = JSONP_Util.assertEqualsJsonText(
          JSONP_Util.EXPECTED_SAMPLEJSONOBJECT_TEXT, actJsonObjectText);
    } catch (Exception e) {
      throw new Fault("jsonWriterTest2 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonWriterTest2 Failed");
  }

  /*
   * @testName: jsonWriterTest3
   * 
   * @assertion_ids: JSONP:JAVADOC:105; JSONP:JAVADOC:106; JSONP:JAVADOC:107;
   * 
   * @test_Strategy: Tests JsonWriter API's for writing out a JsonArray.
   * Comparison is done by reading the JsonWriter output using JsonReader and
   * recreating the JsonArray and than performing a JsonArray comparison for
   * equality.
   *
   * Tests using API methods: Json.createWriter(Writer) and
   * writer.writeArray(JsonArray)
   *
   */
  public void jsonWriterTest3() throws Fault {
    boolean pass = true;
    try {

      logMsg("Create sample JsonArray for testing");
      JsonArray myJsonArray1 = JSONP_Util.createSampleJsonArray();

      logMsg("Write the JsonArray 'myJsonArray1' out to a JsonWriter");
      StringWriter sWriter = new StringWriter();
      try (JsonWriter writer = Json.createWriter(sWriter)) {
        writer.writeArray(myJsonArray1);
        logMsg("Close JsonWriter");
      }

      logMsg("Save contents of the JsonWriter as a String");
      String contents = sWriter.toString();

      logMsg("Dump contents of JsonWriter as a String");
      logMsg("JsonWriterContents=" + contents);

      logMsg("Read the JsonArray back into 'myJsonArray2' using a JsonReader");
      JsonArray myJsonArray2;
      try (JsonReader reader = Json.createReader(new StringReader(contents))) {
        myJsonArray2 = (JsonArray) reader.read();
        logMsg("Close JsonReader");
      }

      logMsg("Compare myJsonArray1 and myJsonArray2 for equality");
      pass = JSONP_Util.assertEqualsJsonArrays(myJsonArray1, myJsonArray2);
    } catch (Exception e) {
      throw new Fault("jsonWriterTest3 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonWriterTest3 Failed");
  }

  /*
   * @testName: jsonWriterTest4
   * 
   * @assertion_ids: JSONP:JAVADOC:105; JSONP:JAVADOC:187; JSONP:JAVADOC:107;
   * 
   * @test_Strategy: Tests JsonWriter API's for writing out a JsonArray.
   * Comparison is done by comparing the expected JsonArray text output with the
   * actual JsonArray text output from the JsonWriter for equality.
   *
   * Tests using API methods: Json.createWriter(OutputStream) and
   * writer.writeArray(JsonArray)
   *
   */
  public void jsonWriterTest4() throws Fault {
    boolean pass = true;
    try {

      logMsg("Create sample JsonArray for testing");
      JsonArray myJsonArray1 = JSONP_Util.createSampleJsonArray();

      logMsg("Write the JsonArray 'myJsonArray1' out to a JsonWriter");
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      JsonWriter writer = Json.createWriter(baos);
      writer.writeArray(myJsonArray1);
      logMsg("Close JsonWriter");
      baos.close();
      writer.close();

      logMsg("Save contents of the JsonWriter as a String");
      String actJsonArrayText = baos.toString("UTF-8");

      logMsg("Dump contents of JsonWriter as a String");
      logMsg("JsonWriterContents=" + actJsonArrayText);

      logMsg(
          "Compare expected JsonArray text with actual JsonArray text for equality");
      pass = JSONP_Util.assertEqualsJsonText(
          JSONP_Util.EXPECTED_SAMPLEJSONARRAY_TEXT, actJsonArrayText);
    } catch (Exception e) {
      throw new Fault("jsonWriterTest4 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonWriterTest4 Failed");
  }

  /*
   * @testName: jsonWriterTest5
   * 
   * @assertion_ids: JSONP:JAVADOC:105; JSONP:JAVADOC:424; JSONP:JAVADOC:110;
   * JSONP:JAVADOC:452;
   * 
   * @test_Strategy: Tests JsonWriter API's for writing out a JsonObject.
   * Comparison is done by comparing the expected JsonObject text output with
   * the actual JsonObject text output from the JsonWriter for equality.
   *
   * Tests using API methods:
   * Json.createWriterFactory(Map<String,?>).createWriter(OutputStream, Charset)
   * writer.writeObject(JsonObject)
   *
   * For encoding use UTF-16BE.
   */
  public void jsonWriterTest5() throws Fault {
    boolean pass = true;
    try {

      logMsg("Create sample JsonObject for testing");
      JsonObject myJsonObject1 = JSONP_Util.createSampleJsonObject();

      logMsg("Write the JsonObject 'myJsonObject1' out to a JsonWriter");
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      JsonWriter writer = Json.createWriterFactory(JSONP_Util.getEmptyConfig())
          .createWriter(baos, JSONP_Util.UTF_16BE);
      writer.writeObject(myJsonObject1);
      logMsg("Close JsonWriter");
      baos.close();
      writer.close();

      logMsg("Save contents of the JsonWriter as a String");
      String actJsonObjectText = JSONP_Util
          .removeWhitespace(baos.toString("UTF-16BE"));

      logMsg("Dump contents of JsonWriter as a String");
      logMsg("JsonWriterContents=" + actJsonObjectText);

      logMsg(
          "Compare expected JsonObject text with actual JsonObject text for equality");
      pass = JSONP_Util.assertEqualsJsonText(
          JSONP_Util.EXPECTED_SAMPLEJSONOBJECT_TEXT, actJsonObjectText);
    } catch (Exception e) {
      throw new Fault("jsonWriterTest5 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonWriterTest5 Failed");
  }

  /*
   * @testName: jsonWriterTest6
   * 
   * @assertion_ids: JSONP:JAVADOC:105; JSONP:JAVADOC:424; JSONP:JAVADOC:107;
   * JSONP:JAVADOC:452;
   * 
   * @test_Strategy: Tests JsonWriter API's for writing out a JsonArray.
   * Comparison is done by comparing the expected JsonArray text output with the
   * actual JsonArray text output from the JsonWriter for equality.
   *
   * Tests using API methods:
   * Json.createWriterFactory.createWriter(OutputStream, Charset)
   * writer.writeArray(JsonArray)
   *
   * For encoding use UTF-8.
   */
  public void jsonWriterTest6() throws Fault {
    boolean pass = true;
    try {

      logMsg("Create a configuration with PRETT_PRINTING enabled.");
      Map<String, ?> config = JSONP_Util.getPrettyPrintingConfig();

      logMsg("Create sample JsonArray for testing");
      JsonArray myJsonArray1 = JSONP_Util.createSampleJsonArray();

      logMsg("Write the JsonArray 'myJsonArray1' out to a JsonWriter");
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      JsonWriter writer = Json.createWriterFactory(config).createWriter(baos,
          JSONP_Util.UTF_8);
      writer.writeArray(myJsonArray1);
      logMsg("Close JsonWriter");
      baos.close();
      writer.close();

      logMsg("Save contents of the JsonWriter as a String");
      String actJsonArrayText = JSONP_Util
          .removeWhitespace(baos.toString("UTF-8"));

      logMsg("Dump contents of JsonWriter as a String");
      logMsg("JsonWriterContents=" + actJsonArrayText);

      logMsg(
          "Compare expected JsonArray text with actual JsonArray text for equality");
      pass = JSONP_Util.assertEqualsJsonText(
          JSONP_Util.EXPECTED_SAMPLEJSONARRAY_TEXT, actJsonArrayText);
    } catch (Exception e) {
      throw new Fault("jsonWriterTest6 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonWriterTest6 Failed");
  }

  /*
   * @testName: jsonWriterTest7
   * 
   * @assertion_ids: JSONP:JAVADOC:105; JSONP:JAVADOC:106; JSONP:JAVADOC:191;
   * 
   * @test_Strategy: Tests JsonWriter API's for writing out a JsonObject.
   * Comparison is done by reading the JsonWriter output using JsonReader and
   * recreating the JsonObject and than performing a JsonObject comparison for
   * equality.
   *
   * Tests using API methods: Json.createWriter(Writer) and
   * writer.write(JsonStructure)
   *
   */
  public void jsonWriterTest7() throws Fault {
    boolean pass = true;
    try {
      logMsg("Create sample JsonObject for testing");
      JsonObject myJsonObject1 = JSONP_Util.createSampleJsonObject();

      logMsg("Write the JsonObject 'myJsonObject1' out to a JsonWriter");
      StringWriter sWriter = new StringWriter();
      try (JsonWriter writer = Json.createWriter(sWriter)) {
        writer.write(myJsonObject1);
        logMsg("Close JsonWriter");
      }

      logMsg("Save contents of the JsonWriter as a String");
      String contents = sWriter.toString();

      logMsg("Dump contents of JsonWriter as a String");
      logMsg("JsonWriterContents=" + contents);

      logMsg(
          "Read the JsonObject back into 'myJsonObject2' using a JsonReader");
      JsonReader reader = Json.createReader(new StringReader(contents));
      JsonObject myJsonObject2 = (JsonObject) reader.read();

      logMsg("Compare myJsonObject1 and myJsonObject2 for equality");
      pass = JSONP_Util.assertEqualsJsonObjects(myJsonObject1, myJsonObject2);
    } catch (Exception e) {
      throw new Fault("jsonWriterTest7 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonWriterTest7 Failed");
  }

  /*
   * @testName: jsonWriterTest8
   * 
   * @assertion_ids: JSONP:JAVADOC:105; JSONP:JAVADOC:106; JSONP:JAVADOC:191;
   * 
   * @test_Strategy: Tests JsonWriter API's for writing out a JsonArray.
   * Comparison is done by reading the JsonWriter output using JsonReader and
   * recreating the JsonArray and than performing a JsonArray comparison for
   * equality.
   *
   * Tests using API methods: Json.createWriter(Writer) and
   * writer.write(JsonStructure)
   *
   */
  public void jsonWriterTest8() throws Fault {
    boolean pass = true;
    try {

      logMsg("Create sample JsonArray for testing");
      JsonArray myJsonArray1 = JSONP_Util.createSampleJsonArray();

      logMsg("Write the JsonArray 'myJsonArray1' out to a JsonWriter");
      StringWriter sWriter = new StringWriter();
      try (JsonWriter writer = Json.createWriter(sWriter)) {
        writer.write(myJsonArray1);
        logMsg("Close JsonWriter");
      }

      logMsg("Save contents of the JsonWriter as a String");
      String contents = sWriter.toString();

      logMsg("Dump contents of JsonWriter as a String");
      logMsg("JsonWriterContents=" + contents);

      logMsg("Read the JsonArray back into 'myJsonArray2' using a JsonReader");
      JsonReader reader = Json.createReader(new StringReader(contents));
      JsonArray myJsonArray2 = (JsonArray) reader.read();

      logMsg("Compare myJsonArray1 and myJsonArray2 for equality");
      pass = JSONP_Util.assertEqualsJsonArrays(myJsonArray1, myJsonArray2);
    } catch (Exception e) {
      throw new Fault("jsonWriterTest8 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonWriterTest8 Failed");
  }

  /*
   * @testName: jsonWriterUTFEncodedTests
   * 
   * @assertion_ids: JSONP:JAVADOC:110; JSONP:JAVADOC:423; JSONP:JAVADOC:452;
   * 
   * @test_Strategy: Tests various JsonWriter API's to create a JsonObject.
   *
   * The output is written to an OutputStream using all supported UTF encodings
   * and read back as a string and filtered to remove whitespace and is compared
   * against an expected string. The following UTF encodings are tested:
   *
   * UTF8 UTF16 UTF16LE UTF16BE UTF32LE UTF32BE
   *
   * { "object":{"string":"string","number":1,"true":true,"false":false,"null":
   * null}, "array":["string", 1, true, false, null] }
   */
  public void jsonWriterUTFEncodedTests() throws Fault {
    boolean pass = true;
    logMsg(
        "Create expected JSON text with no whitespace for use in comparsion");
    String expJson = "{\"object\":{\"string\":\"string\",\"number\":1,\"true\":true,\"false\":false,\"null\":null},\"array\":[\"string\",1,true,false,null]}";
    try {
      logMsg(
          "-----------------------------------------------------------------------------------------------");
      logMsg(
          "TEST CASE [Json.createWriterFactory(Map<String,?>).createWriter(OutputStream, Charset) as UTF-8]");
      logMsg(
          "-----------------------------------------------------------------------------------------------");
      logMsg("Create JsonWriter using UTF-8 encoding");
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      JsonWriter writer = Json.createWriterFactory(JSONP_Util.getEmptyConfig())
          .createWriter(baos, JSONP_Util.UTF_8);
      JSONP_Util.writeJsonObjectFromString(writer, expJson);

      // Dump JsonText output
      TestUtil.logMsg("Generated Output=" + baos.toString("UTF-8"));

      // Do comparison
      logMsg(
          "Read the JSON text back from OutputStream using UTF-8 encoding removing whitespace");
      String actJson = JSONP_Util.removeWhitespace(baos.toString("UTF-8"));
      if (!JSONP_Util.assertEqualsJsonText(expJson, actJson))
        pass = false;

    } catch (Exception e) {
      pass = false;
      logErr("Exception occurred testing generation to UTF-8 encoding: " + e);
    }
    try {
      logMsg(
          "------------------------------------------------------------------------------------------------");
      logMsg(
          "TEST CASE [Json.createWriterFactory(Map<String,?>).createWriter(OutputStream, Charset) as UTF-16]");
      logMsg(
          "------------------------------------------------------------------------------------------------");
      logMsg("Create JsonWriter using UTF-16 encoding");
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      JsonWriter writer = Json.createWriterFactory(JSONP_Util.getEmptyConfig())
          .createWriter(baos, JSONP_Util.UTF_16);
      JSONP_Util.writeJsonObjectFromString(writer, expJson);

      // Dump JsonText output
      TestUtil.logMsg("Generated Output=" + baos.toString("UTF-16"));

      // Do comparison
      logMsg(
          "Read the JSON text back from OutputStream using UTF-16 encoding removing whitespace");
      String actJson = JSONP_Util.removeWhitespace(baos.toString("UTF-16"));
      if (!JSONP_Util.assertEqualsJsonText(expJson, actJson))
        pass = false;

    } catch (Exception e) {
      pass = false;
      logErr("Exception occurred testing generation to UTF-16 encoding: " + e);
    }
    try {
      logMsg(
          "--------------------------------------------------------------------------------------------------");
      logMsg(
          "TEST CASE [Json.createWriterFactory(Map<String,?>).createWriter(OutputStream, Charset) as UTF-16LE]");
      logMsg(
          "--------------------------------------------------------------------------------------------------");
      logMsg("Create JsonWriter using UTF-16LE encoding");
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      JsonWriter writer = Json.createWriterFactory(JSONP_Util.getEmptyConfig())
          .createWriter(baos, JSONP_Util.UTF_16LE);
      JSONP_Util.writeJsonObjectFromString(writer, expJson);

      // Dump JsonText output
      TestUtil.logMsg("Generated Output=" + baos.toString("UTF-16LE"));

      // Do comparison
      logMsg(
          "Read the JSON text back from OutputStream using UTF-16LE encoding removing whitespace");
      String actJson = JSONP_Util.removeWhitespace(baos.toString("UTF-16LE"));
      if (!JSONP_Util.assertEqualsJsonText(expJson, actJson))
        pass = false;

    } catch (Exception e) {
      pass = false;
      logErr(
          "Exception occurred testing generation to UTF-16LE encoding: " + e);
    }
    try {
      logMsg(
          "--------------------------------------------------------------------------------------------------");
      logMsg(
          "TEST CASE [Json.createWriterFactory(Map<String,?>).createWriter(OutputStream, Charset) as UTF-16BE]");
      logMsg(
          "--------------------------------------------------------------------------------------------------");
      logMsg("Create JsonWriter using UTF-16BE encoding");
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      JsonWriter writer = Json.createWriterFactory(JSONP_Util.getEmptyConfig())
          .createWriter(baos, JSONP_Util.UTF_16BE);
      JSONP_Util.writeJsonObjectFromString(writer, expJson);

      // Dump JsonText output
      TestUtil.logMsg("Generated Output=" + baos.toString("UTF-16BE"));

      // Do comparison
      logMsg(
          "Read the JSON text back from OutputStream using UTF-16BE encoding removing whitespace");
      String actJson = JSONP_Util.removeWhitespace(baos.toString("UTF-16BE"));
      if (!JSONP_Util.assertEqualsJsonText(expJson, actJson))
        pass = false;

    } catch (Exception e) {
      pass = false;
      logErr(
          "Exception occurred testing generation to UTF-16BE encoding: " + e);
    }
    try {
      logMsg(
          "--------------------------------------------------------------------------------------------------");
      logMsg(
          "TEST CASE [Json.createWriterFactory(Map<String,?>).createWriter(OutputStream, Charset) as UTF-32LE]");
      logMsg(
          "--------------------------------------------------------------------------------------------------");
      logMsg("Create JsonWriter using UTF-32LE encoding");
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      JsonWriter writer = Json.createWriterFactory(JSONP_Util.getEmptyConfig())
          .createWriter(baos, JSONP_Util.UTF_32LE);
      JSONP_Util.writeJsonObjectFromString(writer, expJson);

      // Dump JsonText output
      TestUtil.logMsg("Generated Output=" + baos.toString("UTF-32LE"));

      // Do comparison
      logMsg(
          "Read the JSON text back from OutputStream using UTF-32LE encoding removing whitespace");
      String actJson = JSONP_Util.removeWhitespace(baos.toString("UTF-32LE"));
      if (!JSONP_Util.assertEqualsJsonText(expJson, actJson))
        pass = false;

    } catch (Exception e) {
      pass = false;
      logErr(
          "Exception occurred testing generation to UTF-32LE encoding: " + e);
    }
    try {
      logMsg(
          "--------------------------------------------------------------------------------------------------");
      logMsg(
          "TEST CASE [Json.createWriterFactory(Map<String,?>).createWriter(OutputStream, Charset) as UTF-32BE]");
      logMsg(
          "--------------------------------------------------------------------------------------------------");
      logMsg("Create JsonWriter using UTF-32BE encoding");
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      JsonWriter writer = Json.createWriterFactory(JSONP_Util.getEmptyConfig())
          .createWriter(baos, JSONP_Util.UTF_32BE);
      JSONP_Util.writeJsonObjectFromString(writer, expJson);

      // Dump JsonText output
      TestUtil.logMsg("Generated Output=" + baos.toString("UTF-32BE"));

      // Do comparison
      logMsg(
          "Read the JSON text back from OutputStream using UTF-32BE encoding removing whitespace");
      String actJson = JSONP_Util.removeWhitespace(baos.toString("UTF-32BE"));
      if (!JSONP_Util.assertEqualsJsonText(expJson, actJson))
        pass = false;

    } catch (Exception e) {
      pass = false;
      logErr(
          "Exception occurred testing generation to UTF-32BE encoding: " + e);
    }
    if (!pass)
      throw new Fault("jsonWriterUTFEncodedTests Failed");
  }

  /*
   * @testName: jsonWriterWithConfigTest1
   * 
   * @assertion_ids: JSONP:JAVADOC:105; JSONP:JAVADOC:422; JSONP:JAVADOC:110;
   * JSONP:JAVADOC:452;
   * 
   * @test_Strategy: Tests JsonWriter API's for writing out a JsonObject.
   * Comparison is done by comparing the expected JsonObject text output with
   * the actual JsonObject text output from the JsonWriter for equality.
   *
   * Tests using API methods:
   * Json.createWriterFactory(Map<String,?>).createWriter(Writer)
   * writer.writeObject(JsonObject)
   *
   */
  public void jsonWriterWithConfigTest1() throws Fault {
    boolean pass = true;
    try {

      logMsg("Create a configuration with PRETT_PRINTING enabled.");
      Map<String, ?> config = JSONP_Util.getPrettyPrintingConfig();

      logMsg("Create sample JsonObject for testing");
      JsonObject myJsonObject1 = JSONP_Util.createSampleJsonObject();

      logMsg("Write the JsonObject 'myJsonObject1' out to a JsonWriter");
      StringWriter swriter = new StringWriter();
      try (JsonWriter writer = Json.createWriterFactory(config)
          .createWriter(swriter)) {
        writer.writeObject(myJsonObject1);
        logMsg("Close JsonWriter");
      }

      logMsg("Save contents of the JsonWriter as a String");
      String actJsonObjectText = JSONP_Util
          .removeWhitespace(swriter.toString());

      logMsg("Dump contents of JsonWriter as a String");
      logMsg("JsonWriterContents=" + actJsonObjectText);

      logMsg(
          "Compare expected JsonObject text with actual JsonObject text for equality");
      pass = JSONP_Util.assertEqualsJsonText(
          JSONP_Util.EXPECTED_SAMPLEJSONOBJECT_TEXT, actJsonObjectText);
    } catch (Exception e) {
      throw new Fault("jsonWriterWithConfigTest1 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonWriterWithConfigTest1 Failed");
  }

  /*
   * @testName: jsonWriterWithConfigTest2
   * 
   * @assertion_ids: JSONP:JAVADOC:105; JSONP:JAVADOC:423; JSONP:JAVADOC:107;
   * 
   * @test_Strategy: Tests JsonWriter API's for writing out a JsonArray.
   * Comparison is done by comparing the expected JsonArray text output with the
   * actual JsonArray text output from the JsonWriter for equality.
   *
   * Tests using API methods:
   * Json.createWriterFactory(Map<String,?>).creatWriter(OutputStream)
   * writer.writeArray(JsonArray)
   *
   */
  public void jsonWriterWithConfigTest2() throws Fault {
    boolean pass = true;
    try {

      logMsg("Create a configuration with PRETT_PRINTING enabled.");
      Map<String, ?> config = JSONP_Util.getPrettyPrintingConfig();

      logMsg("Create sample JsonArray for testing");
      JsonArray myJsonArray1 = JSONP_Util.createSampleJsonArray();

      logMsg("Write the JsonArray 'myJsonArray1' out to a JsonWriter");
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      JsonWriter writer = Json.createWriterFactory(config).createWriter(baos);
      writer.writeArray(myJsonArray1);
      logMsg("Close JsonWriter");
      baos.close();
      writer.close();

      logMsg("Save contents of the JsonWriter as a String");
      String actJsonArrayText = JSONP_Util
          .removeWhitespace(baos.toString("UTF-8"));

      logMsg("Dump contents of JsonWriter as a String");
      logMsg("JsonWriterContents=" + actJsonArrayText);

      logMsg(
          "Compare expected JsonArray text with actual JsonArray text for equality");
      pass = JSONP_Util.assertEqualsJsonText(
          JSONP_Util.EXPECTED_SAMPLEJSONARRAY_TEXT, actJsonArrayText);
    } catch (Exception e) {
      throw new Fault("jsonWriterWithConfigTest2 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonWriterWithConfigTest2 Failed");
  }

  /*
   * @testName: jsonWriterExceptionTests
   * 
   * @assertion_ids: JSONP:JAVADOC:105; JSONP:JAVADOC:106; JSONP:JAVADOC:109;
   * JSONP:JAVADOC:112; JSONP:JAVADOC:222;
   * 
   * @test_Strategy: Test for JsonWriter exception test conditions. o
   * IllegalStateException
   *
   */
  public void jsonWriterExceptionTests() throws Fault {
    boolean pass = true;
    JsonWriter writer = null;

    // IllegalStateException if writer.close() already called before
    // writer.writeArray(JsonArray)
    try {
      logMsg("Create sample JsonArray for testing");
      JsonArray jsonArray = JSONP_Util.createSampleJsonArray();

      logMsg("Create JsonWriter, write something and close it");
      StringWriter sWriter = new StringWriter();
      writer = Json.createWriter(sWriter);
      writer.writeArray(jsonArray);
      writer.close();

      logMsg(
          "IllegalStateException if writer.close() already called before writer.writeArray(JsonArray)");
      writer.writeArray(jsonArray);
      pass = false;
      logErr("Failed to throw IllegalStateException");
    } catch (IllegalStateException e) {
      logMsg("Got expected IllegalStateException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    }

    // IllegalStateException if writer.writeArray() called after
    // writer.writeArray(JsonArray)
    try {
      logMsg("Create sample JsonArray for testing");
      JsonArray jsonArray = JSONP_Util.createSampleJsonArray();

      logMsg("Create JsonWriter and write out array");
      StringWriter sWriter = new StringWriter();
      writer = Json.createWriter(sWriter);
      writer.writeArray(jsonArray);

      logMsg(
          "IllegalStateException if writer.writeArray(JsonArray) called after writer.writeArray(JsonArray)");
      writer.writeArray(jsonArray);
      pass = false;
      logErr("Failed to throw IllegalStateException");
    } catch (IllegalStateException e) {
      logMsg("Got expected IllegalStateException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (writer != null)
        writer.close();
    }

    // IllegalStateException if writer.writeObject() called after
    // writer.writeArray(JsonArray)
    try {
      logMsg("Create sample JsonArray for testing");
      JsonArray jsonArray = JSONP_Util.createSampleJsonArray();

      logMsg("Create sample JsonObject for testing");
      JsonObject jsonObject = JSONP_Util.createSampleJsonObject();

      logMsg("Create JsonWriter and write out array");
      StringWriter sWriter = new StringWriter();
      writer = Json.createWriter(sWriter);
      writer.writeArray(jsonArray);

      logMsg(
          "IllegalStateException if writer.writeObject(JsonObject) called after writer.writeArray(JsonArray)");
      writer.writeObject(jsonObject);
      pass = false;
      logErr("Failed to throw IllegalStateException");
    } catch (IllegalStateException e) {
      logMsg("Got expected IllegalStateException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (writer != null)
        writer.close();
    }

    // IllegalStateException if writer.close() already called before
    // writer.writeObject(JsonArray)
    try {
      logMsg("Create sample JsonObject for testing");
      JsonObject jsonObject = JSONP_Util.createSampleJsonObject();

      logMsg("Create JsonWriter, write something and close it");
      StringWriter sWriter = new StringWriter();
      writer = Json.createWriter(sWriter);
      writer.writeObject(jsonObject);
      writer.close();

      logMsg(
          "IllegalStateException if writer.close() already called before writer.writeObject(JsonObject)");
      writer.writeObject(jsonObject);
      pass = false;
      logErr("Failed to throw IllegalStateException");
    } catch (IllegalStateException e) {
      logMsg("Got expected IllegalStateException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    }

    // IllegalStateException if writer.writeObject() called after
    // writer.writeObject(JsonObject)
    try {
      logMsg("Create sample JsonObject for testing");
      JsonObject jsonObject = JSONP_Util.createSampleJsonObject();

      logMsg("Create JsonWriter and write out object");
      StringWriter sWriter = new StringWriter();
      writer = Json.createWriter(sWriter);
      writer.writeObject(jsonObject);

      logMsg(
          "IllegalStateException if writer.writeObject(JsonObject) called after writer.writeObject(JsonObject)");
      writer.writeObject(jsonObject);
      pass = false;
      logErr("Failed to throw IllegalStateException");
    } catch (IllegalStateException e) {
      logMsg("Got expected IllegalStateException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (writer != null)
        writer.close();
    }

    // IllegalStateException if writer.writeArray() called after
    // writer.writeObject(JsonObject)
    try {
      logMsg("Create sample JsonArray for testing");
      JsonArray jsonArray = JSONP_Util.createSampleJsonArray();

      logMsg("Create sample JsonObject for testing");
      JsonObject jsonObject = JSONP_Util.createSampleJsonObject();

      logMsg("Create JsonWriter and write out object");
      StringWriter sWriter = new StringWriter();
      writer = Json.createWriter(sWriter);
      writer.writeObject(jsonObject);

      logMsg(
          "IllegalStateException if writer.writeArray(JsonArray) called after writer.writeObject(JsonObject)");
      writer.writeArray(jsonArray);
      pass = false;
      logErr("Failed to throw IllegalStateException");
    } catch (IllegalStateException e) {
      logMsg("Got expected IllegalStateException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (writer != null)
        writer.close();
    }

    // IllegalStateException if writer.close() already called before
    // writer.write(JsonArray)
    try {
      logMsg("Create sample JsonArray for testing");
      JsonArray jsonArray = JSONP_Util.createSampleJsonArray();

      logMsg("Create JsonWriter, write something and close it");
      StringWriter sWriter = new StringWriter();
      writer = Json.createWriter(sWriter);
      writer.write(jsonArray);
      writer.close();

      logMsg(
          "IllegalStateException if writer.close() already called before writer.write(JsonArray)");
      writer.write(jsonArray);
      pass = false;
      logErr("Failed to throw IllegalStateException");
    } catch (IllegalStateException e) {
      logMsg("Got expected IllegalStateException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    }

    // IllegalStateException if writer.write(JsonArray) called after
    // writer.writeArray(JsonArray)
    try {
      logMsg("Create sample JsonArray for testing");
      JsonArray jsonArray = JSONP_Util.createSampleJsonArray();

      logMsg("Create JsonWriter and write out array");
      StringWriter sWriter = new StringWriter();
      writer = Json.createWriter(sWriter);
      writer.writeArray(jsonArray);

      logMsg(
          "IllegalStateException if writer.write(JsonArray) called after writer.writeArray(JsonArray)");
      writer.write(jsonArray);
      pass = false;
      logErr("Failed to throw IllegalStateException");
    } catch (IllegalStateException e) {
      logMsg("Got expected IllegalStateException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (writer != null)
        writer.close();
    }

    // IllegalStateException if writer.write(JsonObject) called after
    // writer.writeJsonObject(JsonObject)
    try {
      logMsg("Create sample JsonObject for testing");
      JsonObject jsonObject = JSONP_Util.createSampleJsonObject();

      logMsg("Create JsonWriter and write out object");
      StringWriter sWriter = new StringWriter();
      writer = Json.createWriter(sWriter);
      writer.writeObject(jsonObject);

      logMsg(
          "IllegalStateException if writer.write(JsonObject) called after writer.writeObject(JsonObject)");
      writer.write(jsonObject);
      pass = false;
      logErr("Failed to throw IllegalStateException");
    } catch (IllegalStateException e) {
      logMsg("Got expected IllegalStateException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (writer != null)
        writer.close();
    }

    if (!pass)
      throw new Fault("jsonWriterExceptionTests Failed");
  }

  /*
   * @testName: jsonWriterIOErrorTests
   * 
   * @assertion_ids: JSONP:JAVADOC:108; JSONP:JAVADOC:111; JSONP:JAVADOC:221;
   * JSONP:JAVADOC:414;
   * 
   * @test_Strategy: Tests for JsonException for testable i/o errors.
   *
   */
  public void jsonWriterIOErrorTests() throws Fault {
    boolean pass = true;

    // Trip JsonException if there is an i/o error on JsonWriter.close()
    try {
      logMsg("Create sample JsonObject for testing");
      JsonObject myJsonObject = JSONP_Util.createSampleJsonObject();
      logMsg(
          "Trip JsonException if there is an i/o error on JsonWriter.close().");
      MyBufferedWriter mbw = new MyBufferedWriter(new StringWriter());
      try (JsonWriter writer = Json.createWriter(mbw)) {
        writer.writeObject(myJsonObject);
        mbw.setThrowIOException(true);
        logMsg("Calling JsonWriter.close()");
      }
      logErr("Did not get expected JsonException");
      pass = false;
    } catch (JsonException e) {
      logMsg("Caught expected JsonException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    }

    // Trip JsonException if there is an i/o error on
    // JsonWriter.writeObject(JsonObject)
    try {
      logMsg("Create sample JsonObject for testing");
      JsonObject myJsonObject = JSONP_Util.createSampleJsonObject();
      logMsg(
          "Trip JsonException if there is an i/o error on JsonWriter.writeObject(JsonObject).");
      MyBufferedWriter mbw = new MyBufferedWriter(new StringWriter());
      try (JsonWriter writer = Json.createWriter(mbw)) {
        mbw.setThrowIOException(true);
        logMsg("Calling JsonWriter.writeObject(JsonObject)");
        writer.writeObject(myJsonObject);
      }
      logErr("Did not get expected JsonException");
      pass = false;
    } catch (JsonException e) {
      logMsg("Caught expected JsonException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    }

    // Trip JsonException if there is an i/o error on
    // JsonWriter.writeArray(JsonArray)
    try {
      logMsg("Create sample JsonArray for testing");
      JsonArray myJsonArray = JSONP_Util.createSampleJsonArray();
      logMsg(
          "Trip JsonException if there is an i/o error on JsonWriter.writeArray(JsonArray).");
      MyBufferedWriter mbw = new MyBufferedWriter(new StringWriter());
      try (JsonWriter writer = Json.createWriter(mbw)) {
        mbw.setThrowIOException(true);
        logMsg("Calling JsonWriter.writeArray(JsonArray)");
        writer.writeArray(myJsonArray);
      }
      logErr("Did not get expected JsonException");
      pass = false;
    } catch (JsonException e) {
      logMsg("Caught expected JsonException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    }

    // Trip JsonException if there is an i/o error on
    // JsonWriter.write(JsonStructure)
    try {
      logMsg("Create sample JsonObject for testing");
      JsonObject myJsonObject = JSONP_Util.createSampleJsonObject();
      logMsg(
          "Trip JsonException if there is an i/o error on JsonWriter.write(JsonStructure).");
      MyBufferedWriter mbw = new MyBufferedWriter(new StringWriter());
      try (JsonWriter writer = Json.createWriter(mbw)) {
        mbw.setThrowIOException(true);
        logMsg("Calling JsonWriter.write(JsonStructure)");
        writer.write(myJsonObject);
      }
      logErr("Did not get expected JsonException");
      pass = false;
    } catch (JsonException e) {
      logMsg("Caught expected JsonException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    }

    if (!pass)
      throw new Fault("jsonWriterIOErrorTests Failed");
  }

  /*
   * @testName: jsonWriter11Test
   * 
   * @assertion_ids: JSONP:JAVADOC:650; JSONP:JAVADOC:583; JSONP:JAVADOC:584;
   * JSONP:JAVADOC:585; JSONP:JAVADOC:586; JSONP:JAVADOC:587; JSONP:JAVADOC:588;
   * JSONP:JAVADOC:662; JSONP:JAVADOC:663; JSONP:JAVADOC:664; JSONP:JAVADOC:665;
   * JSONP:JAVADOC:666; JSONP:JAVADOC:667;
   * 
   * @test_Strategy: Tests JsonWriter API methods added in JSON-P 1.1.
   */
  public void jsonWriter11Test() throws Fault {
    Writer writerTest = new Writer();
    final TestResult result = writerTest.test();
    result.eval();
  }

}
