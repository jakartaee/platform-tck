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

package com.sun.ts.tests.jsonp.api.jsonreadertests;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.tests.jsonp.api.common.TestResult;
import com.sun.ts.tests.jsonp.common.*;
import java.io.*;
import java.util.*;
import javax.json.*;
import javax.json.stream.*;

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

  /* Utility Methods */

  /*
   * compareJsonObjectForUTFEncodedTests
   */
  private boolean compareJsonObjectForUTFEncodedTests(JsonObject jsonObject) {
    boolean pass = true;
    logMsg("Comparing JsonObject values to expected results.");
    String expString = "stringValue";
    String actString = jsonObject.getJsonString("stringName").getString();
    if (!JSONP_Util.assertEquals(expString, actString))
      pass = false;
    JsonObject actObject = jsonObject.getJsonObject("objectName");
    expString = "bar";
    actString = actObject.getJsonString("foo").getString();
    if (!JSONP_Util.assertEquals(expString, actString))
      pass = false;
    JsonArray actArray = jsonObject.getJsonArray("arrayName");
    if (!JSONP_Util.assertEquals(1, actArray.getJsonNumber(0).intValue())
        || !JSONP_Util.assertEquals(2, actArray.getJsonNumber(1).intValue())
        || !JSONP_Util.assertEquals(3, actArray.getJsonNumber(2).intValue()))
      pass = false;
    return pass;
  }

  /* Tests */

  /*
   * @testName: readEmptyArrayTest
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:97; JSONP:JAVADOC:184;
   * 
   * @test_Strategy: Test read of an empty array "[]" from stream. Use
   * JsonReader.readArray() API call.
   *
   */
  public void readEmptyArrayTest() throws Fault {
    boolean pass = true;
    JsonReader reader = null;
    try {
      String expJsonText = "[]";
      logMsg("Testing read of " + expJsonText);
      reader = Json.createReader(new StringReader(expJsonText));
      JsonArray array = reader.readArray();
      pass = JSONP_Util.assertEqualsEmptyArrayList(array);
    } catch (Exception e) {
      throw new Fault("readEmptyArrayTest Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
    if (!pass)
      throw new Fault("readEmptyArrayTest Failed");
  }

  /*
   * @testName: readEscapeCharsInArrayTest
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:97; JSONP:JAVADOC:184;
   * 
   * @test_Strategy: Test read of an array from a resource file with special
   * chars in data. Use JsonReader.readArray() API call. Test scenario: Read
   * string of JSON text containing a JSON array from resource file with
   * following data: [ "popeye\"\\\/\b\f\n\r\tolive" ]
   *
   * These characters are backslash escape'd as follows: \" \\ \/ \b \f \n \r \t
   *
   * Create a JsonWriter to write above JsonArray to a string of JSON text.
   * Re-read JsonWriter text back into a JsonArray Compare expected JSON array
   * with actual JSON array for equality.
   *
   */
  public void readEscapeCharsInArrayTest() throws Fault {
    boolean pass = true;
    JsonReader reader = null;
    String resourceFile = "jsonArrayWithEscapeCharsData.json";
    String expString = "popeye" + JSONP_Data.escapeCharsAsString + "olive";
    try {

      logMsg("Reading contents of resource file " + resourceFile);
      String readerContents = JSONP_Util
          .getContentsOfResourceAsString(resourceFile);
      logMsg("readerContents=" + readerContents);

      logMsg("Testing read of resource contents: " + readerContents);
      reader = Json.createReader(new StringReader(readerContents));
      JsonArray expJsonArray = reader.readArray();

      TestUtil.logMsg("Dump of expJsonArray");
      JSONP_Util.dumpJsonArray(expJsonArray);

      logMsg("Comparing JsonArray values with expected results.");
      String actString = expJsonArray.getJsonString(0).getString();
      if (!JSONP_Util.assertEquals(expString, actString))
        pass = false;

      logMsg("Write the JsonArray 'expJsonArray' out to a JsonWriter");
      StringWriter sWriter = new StringWriter();
      try (JsonWriter writer = Json.createWriter(sWriter)) {
        writer.writeArray(expJsonArray);
        logMsg("Close JsonWriter");
      }

      logMsg("Save contents of the JsonWriter as a String");
      String writerContents = sWriter.toString();

      logMsg("Create actJsonArray from read of writer contents: "
          + writerContents);
      reader = Json.createReader(new StringReader(writerContents));
      JsonArray actJsonArray = reader.readArray();

      TestUtil.logMsg("Dump of actJsonArray");
      JSONP_Util.dumpJsonArray(actJsonArray);

      logMsg("Compare expJsonArray and actJsonArray for equality");
      if (!JSONP_Util.assertEqualsJsonArrays(expJsonArray, actJsonArray))
        pass = false;
    } catch (Exception e) {
      throw new Fault("readEscapeCharsInArrayTest Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
    if (!pass)
      throw new Fault("readEscapeCharsInArrayTest Failed");
  }

  /*
   * @testName: readEscapeUnicodeCharsInArrayTest
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:97; JSONP:JAVADOC:184;
   * 
   * @test_Strategy: Test read of an array with unicode chars escaped and not
   * escaped. Use JsonReader.readArray() API call. Test scenario: Read string of
   * JSON text containing a JSON array with the following data: [
   * "\\u0000\u00ff\\uff00\uffff" ]
   *
   * Notice unicode \u0000 and \uff00 is escaped but \u00ff and \uffff is not.
   *
   * Compare expected JSON String with actual JSON String for equality.
   *
   */
  public void readEscapeUnicodeCharsInArrayTest() throws Fault {
    boolean pass = true;
    JsonReader reader = null;
    String unicodeTextString = "[\"\\u0000\u00ff\\uff00\uffff\"]";
    String expResult = "\u0000\u00ff\uff00\uffff";
    try {
      logMsg("Reading array of escaped and non escaped unicode chars.");
      reader = Json.createReader(new StringReader(unicodeTextString));
      JsonArray array = reader.readArray();
      String actResult = array.getJsonString(0).getString();
      pass = JSONP_Util.assertEquals(expResult, actResult);
    } catch (Exception e) {
      throw new Fault("readEscapeUnicodeCharsInArrayTest Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
    if (!pass)
      throw new Fault("readEscapeUnicodeCharsInArrayTest Failed");
  }

  /*
   * @testName: readEscapeUnicodeControlCharsInArrayTest
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:97; JSONP:JAVADOC:184;
   * 
   * @test_Strategy: Test read of an array with unicode control chars escaped.
   * Use JsonReader.readArray() API call. Test scenario: Read string of JSON
   * text containing unicode control chars escaped as a Json Array.
   *
   * Compare expected JSON String with actual JSON String for equality.
   *
   */
  public void readEscapeUnicodeControlCharsInArrayTest() throws Fault {
    boolean pass = true;
    JsonReader reader = null;
    String unicodeTextString = "[\"" + JSONP_Data.unicodeControlCharsEscaped
        + "\"]";
    String expResult = JSONP_Data.unicodeControlCharsNonEscaped;
    try {
      logMsg("Reading array of escaped and non escaped unicode chars.");
      reader = Json.createReader(new StringReader(unicodeTextString));
      JsonArray array = reader.readArray();
      String actResult = array.getJsonString(0).getString();
      pass = JSONP_Util.assertEquals(expResult, actResult);
    } catch (Exception e) {
      throw new Fault("readEscapeUnicodeControlCharsInArrayTest Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
    if (!pass)
      throw new Fault("readEscapeUnicodeControlCharsInArrayTest Failed");
  }

  /*
   * @testName: readEmptyObjectTest
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:97; JSONP:JAVADOC:185;
   * 
   * @test_Strategy: Test read of an empty object "{}" from stream. Use
   * JsonReader.readObject() API call.
   *
   */
  public void readEmptyObjectTest() throws Fault {
    boolean pass = true;
    JsonReader reader = null;
    try {
      String expJsonText = "{}";
      logMsg("Testing read of " + expJsonText);
      reader = Json.createReader(new StringReader(expJsonText));
      JsonObject object = reader.readObject();
      pass = JSONP_Util.assertEqualsEmptyObjectMap(object);
    } catch (Exception e) {
      throw new Fault("readEmptyObjectTest Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
    if (!pass)
      throw new Fault("readEmptyObjectTest Failed");
  }

  /*
   * @testName: readEscapeCharsInObjectTest
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:97; JSONP:JAVADOC:185;
   * 
   * @test_Strategy: Test read of an object from a resource file with special
   * chars in data. Use JsonReader.readObject() API call. Test scenario: Read
   * string of JSON text containing a JSON object from resource file with
   * following data: { "specialChars" : "popeye\"\\\/\b\f\n\r\tolive" }
   *
   * These characters are backslash escape'd as follows: \" \\ \/ \b \f \n \r \t
   *
   * Create a JsonWriter to write above JsonObject to a string of JSON text.
   * Re-read JsonWriter text back into a JsonObject Compare expected JSON object
   * with actual JSON object for equality.
   *
   */
  public void readEscapeCharsInObjectTest() throws Fault {
    boolean pass = true;
    JsonReader reader = null;
    String resourceFile = "jsonObjectWithEscapeCharsData.json";
    String expString = "popeye" + JSONP_Data.escapeCharsAsString + "olive";
    try {

      logMsg("Reading contents of resource file " + resourceFile);
      String readerContents = JSONP_Util
          .getContentsOfResourceAsString(resourceFile);
      logMsg("readerContents=" + readerContents);

      logMsg("Testing read of resource contents: " + readerContents);
      reader = Json.createReader(new StringReader(readerContents));
      JsonObject expJsonObject = reader.readObject();

      TestUtil.logMsg("Dump of expJsonObject");
      JSONP_Util.dumpJsonObject(expJsonObject);

      logMsg("Comparing JsonArray values with expected results.");
      String actString = expJsonObject.getJsonString("escapeChars").getString();
      if (!JSONP_Util.assertEquals(expString, actString))
        pass = false;

      logMsg("Write the JsonObject 'expJsonObject' out to a JsonWriter");
      StringWriter sWriter = new StringWriter();
      try (JsonWriter writer = Json.createWriter(sWriter)) {
        writer.writeObject(expJsonObject);
        logMsg("Close JsonWriter");
      }

      logMsg("Save contents of the JsonWriter as a String");
      String writerContents = sWriter.toString();

      logMsg("Create actJsonObject from read of writer contents: "
          + writerContents);
      reader = Json.createReader(new StringReader(writerContents));
      JsonObject actJsonObject = reader.readObject();

      TestUtil.logMsg("Dump of actJsonObject");
      JSONP_Util.dumpJsonObject(actJsonObject);

      logMsg("Compare expJsonObject and actJsonObject for equality");
      if (!JSONP_Util.assertEqualsJsonObjects(expJsonObject, actJsonObject))
        pass = false;
    } catch (Exception e) {
      throw new Fault("readEscapeCharsInObjectTest Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
    if (!pass)
      throw new Fault("readEscapeCharsInObjectTest Failed");
  }

  /*
   * @testName: readEscapeUnicodeCharsInObjectTest
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:97; JSONP:JAVADOC:185;
   * 
   * @test_Strategy: Test read of an object with unicode chars escaped and not
   * escaped. Use JsonReader.readObject() API call. Test scenario: Read string
   * of JSON text containing a JSON object with the following data: {
   * "unicodechars":"\\u0000\u00ff\\uff00\uffff" ]
   *
   * Notice unicode \u0000 and \uff00 is escaped but \u00ff and \uffff is not.
   *
   * Compare expected JSON String with actual JSON String for equality.
   *
   */
  public void readEscapeUnicodeCharsInObjectTest() throws Fault {
    boolean pass = true;
    JsonReader reader = null;
    String unicodeTextString = "{\"unicodechars\":\"\\u0000\u00ff\\uff00\uffff\"}";
    String expResult = "\u0000\u00ff\uff00\uffff";
    try {
      logMsg("Reading object of escaped and non escaped unicode chars.");
      reader = Json.createReader(new StringReader(unicodeTextString));
      JsonObject object = reader.readObject();
      String actResult = object.getJsonString("unicodechars").getString();
      pass = JSONP_Util.assertEquals(expResult, actResult);
    } catch (Exception e) {
      throw new Fault("readEscapeUnicodeCharsInObjectTest Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
    if (!pass)
      throw new Fault("readEscapeUnicodeCharsInObjectTest Failed");
  }

  /*
   * @testName: readEscapeUnicodeControlCharsInObjectTest
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:97; JSONP:JAVADOC:185;
   * 
   * @test_Strategy: Test read of an array with unicode control chars escaped.
   * Use JsonReader.readObject() API call. Test scenario: Read string of JSON
   * text containing unicode control chars escaped as a Json Object.
   *
   * Compare expected JSON String with actual JSON String for equality.
   *
   */
  public void readEscapeUnicodeControlCharsInObjectTest() throws Fault {
    boolean pass = true;
    JsonReader reader = null;
    String unicodeTextString = "{\"unicodechars\":\""
        + JSONP_Data.unicodeControlCharsEscaped + "\"}";
    String expResult = JSONP_Data.unicodeControlCharsNonEscaped;
    try {
      logMsg("Reading array of escaped and non escaped unicode chars.");
      reader = Json.createReader(new StringReader(unicodeTextString));
      JsonObject object = reader.readObject();
      String actResult = object.getJsonString("unicodechars").getString();
      pass = JSONP_Util.assertEquals(expResult, actResult);
    } catch (Exception e) {
      throw new Fault("readEscapeUnicodeControlCharsInObjectTest Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
    if (!pass)
      throw new Fault("readEscapeUnicodeControlCharsInObjectTest Failed");
  }

  /*
   * @testName: readArrayTest
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:97; JSONP:JAVADOC:184;
   * 
   * @test_Strategy: Test read of an array containing various types from stream.
   * Use JsonReader.readArray() API call. [true, false, null, "booyah",
   * 2147483647, 9223372036854775807, 1.7976931348623157E308,
   * [true,false,null,"bingo",-2147483648,-9223372036854775808,4.9E-324],
   * {"true":true,"false":false,"null":null,"bonga":"boo","int":1,"double":10.4}
   * ] Test scenario: Read string of JSON text above consisting of a JSON array
   * into a JsonArray object. Create an expected List of JsonArray values for
   * use in test comparison. Compare expected list of JsonArray values with
   * actual list for equality.
   *
   */
  public void readArrayTest() throws Fault {
    boolean pass = true;
    JsonReader reader = null;
    try {
      String jsonText = "[true,false,null,\"booyah\",2147483647,9223372036854775807,1.7976931348623157E308,"
          + "[true,false,null,\"bingo\",-2147483648,-9223372036854775808,4.9E-324],"
          + "{\"true\":true,\"false\":false,\"null\":null,\"bonga\":\"boo\",\"int\":1,"
          + "\"double\":10.4}]";

      logMsg("Create the expected list of JsonArray values");
      List<JsonValue> expList = new ArrayList<>();
      expList.add(JsonValue.TRUE);
      expList.add(JsonValue.FALSE);
      expList.add(JsonValue.NULL);
      expList.add(JSONP_Util.createJsonString("booyah"));
      expList.add(JSONP_Util.createJsonNumber(Integer.MAX_VALUE));
      expList.add(JSONP_Util.createJsonNumber(Long.MAX_VALUE));
      expList.add(JSONP_Util.createJsonNumber(Double.MAX_VALUE));
      JsonArray array = Json.createArrayBuilder().add(JsonValue.TRUE)
          .add(JsonValue.FALSE).add(JsonValue.NULL).add("bingo")
          .add(Integer.MIN_VALUE).add(Long.MIN_VALUE).add(Double.MIN_VALUE)
          .build();
      JsonObject object = Json.createObjectBuilder().add("true", JsonValue.TRUE)
          .add("false", JsonValue.FALSE).add("null", JsonValue.NULL)
          .add("bonga", "boo").add("int", 1).add("double", 10.4).build();
      expList.add(array);
      expList.add(object);

      logMsg("Testing read of " + jsonText);
      reader = Json.createReader(new StringReader(jsonText));
      JsonArray myJsonArray = reader.readArray();

      List<JsonValue> actList = myJsonArray;
      logMsg(
          "Compare actual list of JsonArray values with expected list of JsonArray values");
      pass = JSONP_Util.assertEqualsList(expList, actList);
    } catch (Exception e) {
      throw new Fault("readArrayTest Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
    if (!pass)
      throw new Fault("readArrayTest Failed");
  }

  /*
   * @testName: readArrayTest2
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:97; JSONP:JAVADOC:184;
   * JSONP:JAVADOC:178;
   * 
   * @test_Strategy: Test read of an array containing various types from stream.
   * Use JsonReader.readArray() API call. [true, false, null, "booyah",
   * 2147483647, 9223372036854775807, 1.7976931348623157E308,
   * [true,false,null,"bingo",-2147483648,-9223372036854775808,4.9E-324],
   * {"true":true,"false":false,"null":null,"bonga":"boo","int":1,"double":10.4}
   * ] Test Scenario: Create an expected JsonArray of the above JSON array for
   * use in test comparison. Create a JsonWriter to write the above JsonArray to
   * a string of JSON text. Next call JsonReader to read the JSON text from the
   * JsonWriter to a JsonArray object. Compare expected JsonArray object with
   * actual JsonArray object for equality.
   *
   */
  public void readArrayTest2() throws Fault {
    boolean pass = true;
    JsonReader reader = null;
    try {
      logMsg("Create the expected list of JsonArray values");
      JsonArray expJsonArray = Json.createArrayBuilder().add(JsonValue.TRUE)
          .add(JsonValue.FALSE).add(JsonValue.NULL).add("booyah")
          .add(Integer.MAX_VALUE).add(Long.MAX_VALUE).add(Double.MAX_VALUE)
          .add(Json.createArrayBuilder().add(JsonValue.TRUE)
              .add(JsonValue.FALSE).add(JsonValue.NULL).add("bingo")
              .add(Integer.MIN_VALUE).add(Long.MIN_VALUE).add(Double.MIN_VALUE))
          .add(Json.createObjectBuilder().add("true", JsonValue.TRUE)
              .add("false", JsonValue.FALSE).add("null", JsonValue.NULL)
              .add("bonga", "boo").add("int", 1).add("double", 10.4))
          .build();

      logMsg("Write the JsonArray 'expJsonArray' out to a JsonWriter");
      StringWriter sWriter = new StringWriter();
      try (JsonWriter writer = Json.createWriter(sWriter)) {
        writer.writeArray(expJsonArray);
        logMsg("Close JsonWriter");
      }

      logMsg("Save contents of the JsonWriter as a String");
      String jsonText = sWriter.toString();

      logMsg("Dump contents of JsonWriter as a String");
      logMsg("JsonWriterContents=" + jsonText);

      logMsg("Testing read of " + jsonText);
      reader = Json.createReader(JSONP_Util.getInputStreamFromString(jsonText));
      JsonArray actJsonArray = reader.readArray();

      logMsg("Compare expJsonArray and actJsonArray for equality");
      pass = JSONP_Util.assertEqualsJsonArrays(expJsonArray, actJsonArray);
    } catch (Exception e) {
      throw new Fault("readArrayTest2 Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
    if (!pass)
      throw new Fault("readArrayTest2 Failed");
  }

  /*
   * @testName: readArrayTest3
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:449; JSONP:JAVADOC:184;
   * JSONP:JAVADOC:419;
   * 
   * @test_Strategy: Test read of an array containing various types from stream.
   * Use JsonReader.readArray() API call. [true, false, null, "booyah",
   * 2147483647, 9223372036854775807,
   * [true,false,null,"bingo",-2147483648,-9223372036854775808],
   * {"true":true,"false":false,"null":null,"bonga":"boo","int":1} ] Test
   * scenario: Read string of JSON text above consisting of a JSON array into a
   * JsonArray object with an empty configuration. Create a JsonWriter to write
   * the above JsonArray to a string of JSON text. Compare expected JSON text
   * with actual JSON text for equality.
   *
   * Tests the following API's: JsonReader =
   * Json.createReaderFactory(Map<String,?>).createReader(Reader) JsonArray
   * array = JsonReader.readArray()
   *
   */
  public void readArrayTest3() throws Fault {
    boolean pass = true;
    JsonReader reader = null;
    try {
      String expJsonText = "[true,false,null,\"booyah\",2147483647,9223372036854775807,"
          + "[true,false,null,\"bingo\",-2147483648,-9223372036854775808],"
          + "{\"true\":true,\"false\":false,\"null\":null,\"bonga\":\"boo\",\"int\":1}]";

      logMsg("Testing read of " + expJsonText);
      reader = Json.createReaderFactory(JSONP_Util.getEmptyConfig())
          .createReader(new StringReader(expJsonText));
      JsonArray myJsonArray = reader.readArray();

      logMsg("Write the JsonArray 'myJsonArray' out to a JsonWriter");
      StringWriter sWriter = new StringWriter();
      try (JsonWriter writer = Json.createWriter(sWriter)) {
        writer.writeArray(myJsonArray);
        logMsg("Close JsonWriter");
      }

      logMsg("Save contents of the JsonWriter as a String");
      String actJsonText = sWriter.toString();

      logMsg("Compare actual JSON text with expected JSON text");
      pass = JSONP_Util.assertEqualsJsonText(expJsonText, actJsonText);
    } catch (Exception e) {
      throw new Fault("readArrayTest3 Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
    if (!pass)
      throw new Fault("readArrayTest3 Failed");
  }

  /*
   * @testName: readArrayTest4
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:420; JSONP:JAVADOC:184;
   * JSONP:JAVADOC:449;
   * 
   * @test_Strategy: Test read of an array from a resource file with various
   * amounts of data. Use JsonReader.readArray() API call. Test scenario: Read
   * InputStream of JSON text containing a JSON array from resource file with
   * various amounts of data use UTF-8 encoding. Create a JsonWriter to write
   * above JsonArray to a string of JSON text. Re-read JsonWriter text back into
   * a JsonArray Compare expected JSON array with actual JSON array for
   * equality.
   *
   */
  public void readArrayTest4() throws Fault {
    boolean pass = true;
    JsonReader reader = null;
    String resourceFile = "jsonArrayWithAllTypesOfData.json";
    try {
      logMsg(
          "Read contents of InputStream from resource file: " + resourceFile);
      Map<String, ?> config = JSONP_Util.getEmptyConfig();
      reader = Json.createReaderFactory(config).createReader(
          JSONP_Util.getInputStreamFromResource(resourceFile),
          JSONP_Util.UTF_8);
      JsonArray expJsonArray = reader.readArray();

      TestUtil.logMsg("Dump of expJsonArray");
      JSONP_Util.dumpJsonArray(expJsonArray);

      logMsg("Write the JsonArray 'expJsonArray' out to a JsonWriter");
      StringWriter sWriter = new StringWriter();
      try (JsonWriter writer = Json.createWriter(sWriter)) {
        writer.writeArray(expJsonArray);
        logMsg("Close JsonWriter");
      }

      logMsg("Save contents of the JsonWriter as a String");
      String writerContents = sWriter.toString();

      logMsg("Create actJsonArray from read of writer contents: "
          + writerContents);
      reader = Json.createReader(new StringReader(writerContents));
      JsonArray actJsonArray = reader.readArray();

      TestUtil.logMsg("Dump of actJsonArray");
      JSONP_Util.dumpJsonArray(actJsonArray);

      logMsg("Compare expJsonArray and actJsonArray for equality");
      pass = JSONP_Util.assertEqualsJsonArrays(expJsonArray, actJsonArray);
    } catch (Exception e) {
      throw new Fault("readArrayTest4 Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
    if (!pass)
      throw new Fault("readArrayTest4 Failed");
  }

  /*
   * @testName: readArrayTest5
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:97; JSONP:JAVADOC:181;
   * JSONP:JAVADOC:420; JSONP:JAVADOC:449;
   * 
   * @test_Strategy: Test read of an array from a resource file with lots of
   * nesting. Use JsonReader.read() API call. Test scenario: Read InputStream of
   * JSON text containing a JSON array from resource file with lots of nesting
   * use UTF-8 encoding with empty configuration. Create a JsonWriter to write
   * above JsonArray to a string of JSON text. Compare expected JSON text with
   * actual JSON text for equality. Filter all text output to remove whitespace
   * before comparison.
   *
   * Tests the following API's: JsonReader =
   * Json.createReaderFactory(Map<String,?>).createReader(InputStream, Charset)
   * JsonArray array = (JsonArray)JsonReader.read()
   *
   *
   */
  public void readArrayTest5() throws Fault {
    boolean pass = true;
    JsonReader reader = null;
    String resourceFile = "jsonArrayWithLotsOfNestedObjectsData.json";
    try {
      logMsg("Reading contents of resource file " + resourceFile);
      String readerContents = JSONP_Util
          .getContentsOfResourceAsString(resourceFile);
      logMsg("readerContents=" + readerContents);

      // Create expected JSON text from resource contents filtered of whitespace
      // for comparison
      logMsg("Filter readerContents of whitespace for comparison");
      String expJsonText = JSONP_Util.removeWhitespace(readerContents);

      logMsg(
          "Read contents of InputStream from resource file: " + resourceFile);
      reader = Json.createReaderFactory(JSONP_Util.getEmptyConfig())
          .createReader(JSONP_Util.getInputStreamFromResource(resourceFile),
              JSONP_Util.UTF_8);
      JsonArray myJsonArray = (JsonArray) reader.read();

      logMsg("Write the JsonArray 'myJsonArray' out to a JsonWriter");
      StringWriter sWriter = new StringWriter();
      try (JsonWriter writer = Json.createWriter(sWriter)) {
        writer.writeArray(myJsonArray);
        logMsg("Close JsonWriter");
      }

      logMsg("Save contents of the JsonWriter as a String");
      String writerContents = sWriter.toString();

      logMsg("Dump contents of the JsonWriter as a String");
      logMsg("writerContents=" + writerContents);

      logMsg("Filter writerContents of whitespace for comparison");
      String actJsonText = JSONP_Util.removeWhitespace(writerContents);

      logMsg("Compare actual JSON text with expected JSON text");
      pass = JSONP_Util.assertEqualsJsonText(expJsonText, actJsonText);
    } catch (Exception e) {
      throw new Fault("readArrayTest5 Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
    if (!pass)
      throw new Fault("readArrayTest5 Failed");
  }

  /*
   * @testName: readArrayEncodingTest
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:420; JSONP:JAVADOC:449;
   * JSONP:JAVADOC:184;
   * 
   * @test_Strategy: Test read of a JsonArray from a resource file using both
   * encodings of UTF-8 and UTF-16BE.
   * 
   * Test scenario: For each encoding read the appropriate resource file
   * containing a string value. Call JsonArray.getJsonString() to get the value
   * of the JsonString. Compare expected string with actual string for equality.
   */
  public void readArrayEncodingTest() throws Fault {
    boolean pass = true;
    JsonReader reader = null;
    String expString = "a\u65e8\u452c\u8b9e\u6589\u5c57\u5217z";
    String resourceFileUTF8 = "jsonArrayUTF8.json";
    String resourceFileUTF16BE = "jsonArrayUTF16BE.json";
    Map<String, ?> config = JSONP_Util.getEmptyConfig();
    try {
      logMsg("Reading contents of resource file using UTF-8 encoding "
          + resourceFileUTF8);
      InputStream is = JSONP_Util.getInputStreamFromResource(resourceFileUTF8);
      reader = Json.createReaderFactory(config).createReader(is,
          JSONP_Util.UTF_8);
      JsonArray jsonArray = reader.readArray();
      logMsg("Comparing JsonArray values with expected results.");
      String actString = jsonArray.getJsonString(0).getString();
      if (!JSONP_Util.assertEquals(expString, actString))
        pass = false;
    } catch (Exception e) {
      throw new Fault("readArrayEncodingTest Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
    try {
      logMsg("Reading contents of resource file using UTF-16BE encoding "
          + resourceFileUTF16BE);
      InputStream is = JSONP_Util
          .getInputStreamFromResource(resourceFileUTF16BE);
      reader = Json.createReaderFactory(config).createReader(is,
          JSONP_Util.UTF_16BE);
      JsonArray jsonArray = reader.readArray();
      logMsg("Comparing JsonArray values with expected results.");
      String actString = jsonArray.getJsonString(0).getString();
      if (!JSONP_Util.assertEquals(expString, actString))
        pass = false;
    } catch (Exception e) {
      throw new Fault("readArrayEncodingTest Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
    if (!pass)
      throw new Fault("readArrayEncodingTest Failed");
  }

  /*
   * @testName: readObjectTest
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:97; JSONP:JAVADOC:185;
   * 
   * @test_Strategy: Test read of an object containing various types from
   * stream. Use JsonReader.readObject() API call. {"true":true, "false":false,
   * "null":null, "booyah":"booyah", "int":2147483647,
   * "long":9223372036854775807, "double":1.7976931348623157E308,
   * "array":[true,false,null,"bingo",-2147483648,-9223372036854775808,4.9E-324]
   * , "object":{"true":true,"false":false,"null":null,"bonga":"boo","int":1,
   * "double":10.4} } Test scenario: Read string of JSON text above consisting
   * of a JSON object into a JsonObject object. Create an expected map of
   * JsonObject values for use in test comparison. Compare expected map of
   * JsonObject values with actual map for equality.
   *
   */
  public void readObjectTest() throws Fault {
    boolean pass = true;
    JsonReader reader = null;
    try {
      String expJsonText = "{\"true\":true,\"false\":false,\"null\":null,\"booyah\":\"booyah\",\"int\":2147483647,"
          + "\"long\":9223372036854775807,\"double\":1.7976931348623157E308,"
          + "\"array\":[true,false,null,\"bingo\",-2147483648,-9223372036854775808,4.9E-324],"
          + "\"object\":{\"true\":true,\"false\":false,\"null\":null,\"bonga\":\"boo\",\"int\":1,"
          + "\"double\":10.4}}";

      logMsg("Create the expected map of JsonObject values");
      Map<String, JsonValue> expMap = new HashMap<>();
      expMap.put("true", JsonValue.TRUE);
      expMap.put("false", JsonValue.FALSE);
      expMap.put("null", JsonValue.NULL);
      expMap.put("booyah", JSONP_Util.createJsonString("booyah"));
      expMap.put("int", JSONP_Util.createJsonNumber(Integer.MAX_VALUE));
      expMap.put("long", JSONP_Util.createJsonNumber(Long.MAX_VALUE));
      expMap.put("double", JSONP_Util.createJsonNumber(Double.MAX_VALUE));
      JsonArray array = Json.createArrayBuilder().add(JsonValue.TRUE)
          .add(JsonValue.FALSE).add(JsonValue.NULL).add("bingo")
          .add(Integer.MIN_VALUE).add(Long.MIN_VALUE).add(Double.MIN_VALUE)
          .build();
      JsonObject object = Json.createObjectBuilder().add("true", JsonValue.TRUE)
          .add("false", JsonValue.FALSE).add("null", JsonValue.NULL)
          .add("bonga", "boo").add("int", 1).add("double", 10.4).build();
      expMap.put("array", array);
      expMap.put("object", object);

      logMsg("Testing read of " + expJsonText);
      reader = Json.createReader(new StringReader(expJsonText));
      JsonObject myJsonObject = reader.readObject();

      Map<String, JsonValue> actMap = myJsonObject;
      logMsg(
          "Compare actual map of JsonObject values with expected map of JsonObject values");
      pass = JSONP_Util.assertEqualsMap(expMap, actMap);
    } catch (Exception e) {
      throw new Fault("readObjectTest Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
    if (!pass)
      throw new Fault("readObjectTest Failed");
  }

  /*
   * @testName: readObjectTest2
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:178; JSONP:JAVADOC:185;
   * 
   * @test_Strategy: Test read of an object containing various types from
   * stream. Use JsonReader.readObject() API call. {"true":true, "false":false,
   * "null":null, "booyah":"booyah", "int":2147483647,
   * "long":9223372036854775807, "double":1.7976931348623157E308,
   * "array":[true,false,null,"bingo",-2147483648,-9223372036854775808,4.9E-324]
   * , "object":{"true":true,"false":false,"null":null,"bonga":"boo","int":1,
   * "double":10.4} } Test Scenario: Create an expected JsonObject of the above
   * JSON object for use in test comparison. Create a JsonWriter to write the
   * above JsonObject to a string of JSON text. Next call JsonReader to read the
   * JSON text from the JsonWriter to a JsonObject object. Compare expected
   * JsonObject object with actual JsonObject object for equality.
   *
   */
  public void readObjectTest2() throws Fault {
    boolean pass = true;
    JsonReader reader = null;
    try {
      logMsg("Create the expected list of JsonObject values");
      JsonObject expJsonObject = Json.createObjectBuilder()
          .add("true", JsonValue.TRUE).add("false", JsonValue.FALSE)
          .add("null", JsonValue.NULL).add("booyah", "booyah")
          .add("int", Integer.MAX_VALUE).add("long", Long.MAX_VALUE)
          .add("double", Double.MAX_VALUE)
          .add("array",
              Json.createArrayBuilder().add(JsonValue.TRUE).add(JsonValue.FALSE)
                  .add(JsonValue.NULL).add("bingo").add(Integer.MIN_VALUE)
                  .add(Long.MIN_VALUE).add(Double.MIN_VALUE))
          .add("object",
              Json.createObjectBuilder().add("true", JsonValue.TRUE)
                  .add("false", JsonValue.FALSE).add("null", JsonValue.NULL)
                  .add("bonga", "boo").add("int", 1).add("double", 10.4))
          .build();

      logMsg("Write the JsonObject 'expJsonObject' out to a JsonWriter");
      StringWriter sWriter = new StringWriter();
      try (JsonWriter writer = Json.createWriter(sWriter)) {
        writer.writeObject(expJsonObject);
        logMsg("Close JsonWriter");
      }

      logMsg("Save contents of the JsonWriter as a String");
      String jsonText = sWriter.toString();

      logMsg("Dump contents of JsonWriter as a String");
      logMsg("JsonWriterContents=" + jsonText);

      logMsg("Testing read of " + jsonText);
      reader = Json.createReader(JSONP_Util.getInputStreamFromString(jsonText));
      JsonObject actJsonObject = reader.readObject();

      logMsg("Compare expJsonObject and actJsonObject for equality");
      pass = JSONP_Util.assertEqualsJsonObjects(expJsonObject, actJsonObject);
    } catch (Exception e) {
      throw new Fault("readObjectTest2 Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
    if (!pass)
      throw new Fault("readObjectTest2 Failed");
  }

  /*
   * @testName: readObjectTest3
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:185; JSONP:JAVADOC:419;
   * JSONP:JAVADOC:449;
   * 
   * @test_Strategy: Test read of an object containing various types from
   * stream. Use JsonReader.readObject() API call. {"true":true, "false":false,
   * "null":null, "booyah":"booyah", "int":2147483647,
   * "long":9223372036854775807,
   * "array":[true,false,null,"bingo",-2147483648,-9223372036854775808],
   * "object":{"true":true,"false":false,"null":null,"bonga":"boo","int":1} }
   * Test scenario: Read string of JSON text above consisting of a JSON object
   * into a JsonObject object with an empty configuration. Create a JsonWriter
   * to write the above JsonObject to a string of JSON text. Compare expected
   * JSON text with actual JSON text for equality.
   *
   * Tests the following API's: JsonReader =
   * Json.createReaderFactory(Map<String,?>).createReader(Reader) JsonObject
   * object = JsonReader.readObject()
   *
   *
   */
  public void readObjectTest3() throws Fault {
    boolean pass = true;
    JsonReader reader = null;
    try {
      String expJsonText = "{\"true\":true,\"false\":false,\"null\":null,\"booyah\":\"booyah\",\"int\":2147483647,\"long\":9223372036854775807,"
          + "\"array\":[true,false,null,\"bingo\",-2147483648,-9223372036854775808],"
          + "\"object\":{\"true\":true,\"false\":false,\"null\":null,\"bonga\":\"boo\",\"int\":1}}";

      logMsg("Testing read of " + expJsonText);
      reader = Json.createReaderFactory(JSONP_Util.getEmptyConfig())
          .createReader(new StringReader(expJsonText));
      JsonObject myJsonObject = reader.readObject();

      logMsg("Write the JsonObject 'myJsonObject' out to a JsonWriter");
      StringWriter sWriter = new StringWriter();
      try (JsonWriter writer = Json.createWriter(sWriter)) {
        writer.writeObject(myJsonObject);
        logMsg("Close JsonWriter");
      }

      logMsg("Save contents of the JsonWriter as a String");
      String actJsonText = sWriter.toString();

      logMsg("Compare actual JSON text with expected JSON text");
      pass = JSONP_Util.assertEqualsJsonText(expJsonText, actJsonText);
    } catch (Exception e) {
      throw new Fault("readObjectTest3 Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
    if (!pass)
      throw new Fault("readObjectTest3 Failed");
  }

  /*
   * @testName: readObjectTest4
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:97; JSONP:JAVADOC:420;
   * JSONP:JAVADOC:449;
   * 
   * @test_Strategy: Test read of an object from a resource file with various
   * amounts of data. Use JsonReader.readObject() API call. Test scenario: Read
   * InputStream of JSON text containing a JSON object from resource file with
   * various amounts of data use UTF-8 encoding. Create a JsonWriter to write
   * above JsonObject to a string of JSON text. Re-read JsonWriter text back
   * into a JsonObject Compare expected JSON object with actual JSON object for
   * equality.
   *
   */
  public void readObjectTest4() throws Fault {
    boolean pass = true;
    JsonReader reader = null;
    String resourceFile = "jsonObjectWithAllTypesOfData.json";
    try {
      logMsg(
          "Read contents of InputStream from resource file: " + resourceFile);
      Map<String, ?> config = JSONP_Util.getEmptyConfig();
      reader = Json.createReaderFactory(config).createReader(
          JSONP_Util.getInputStreamFromResource(resourceFile),
          JSONP_Util.UTF_8);
      JsonObject expJsonObject = reader.readObject();

      TestUtil.logMsg("Dump of expJsonObject");
      JSONP_Util.dumpJsonObject(expJsonObject);

      logMsg("Write the JsonObject 'expJsonObject' out to a JsonWriter");
      StringWriter sWriter = new StringWriter();
      try (JsonWriter writer = Json.createWriter(sWriter)) {
        writer.writeObject(expJsonObject);
        logMsg("Close JsonWriter");
      }

      logMsg("Save contents of the JsonWriter as a String");
      String writerContents = sWriter.toString();

      logMsg("Create actJsonObject from read of writer contents: "
          + writerContents);
      reader = Json.createReader(new StringReader(writerContents));
      JsonObject actJsonObject = reader.readObject();

      TestUtil.logMsg("Dump of actJsonObject");
      JSONP_Util.dumpJsonObject(actJsonObject);

      logMsg("Compare expJsonObject and actJsonObject for equality");
      pass = JSONP_Util.assertEqualsJsonObjects(expJsonObject, actJsonObject);
    } catch (Exception e) {
      throw new Fault("readObjectTest4 Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
    if (!pass)
      throw new Fault("readObjectTest4 Failed");
  }

  /*
   * @testName: readObjectTest5
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:97; JSONP:JAVADOC:181;
   * JSONP:JAVADOC:420; JSONP:JAVADOC:449;
   * 
   * @test_Strategy: Test read of an object from a resource file with lots of
   * nesting. Use JsonReader.read() API call. Test scenario: Read InputStream of
   * JSON text containing a JSON object from resource file with lots of nesting
   * use UTF-8 encoding with empty configuration. Create a JsonWriter to write
   * above JsonObject to a string of JSON text. Compare expected JSON text with
   * actual JSON text for equality. Filter all text output to remove whitespace
   * before comparison.
   *
   * Tests the following API's: JsonReader =
   * Json.createReaderFactory(Map<String,?>).createReader(InputStream, Charset)
   * JsonReader.read() JsonObject object = (JsonObject)JsonReader.read()
   *
   *
   */
  public void readObjectTest5() throws Fault {
    boolean pass = true;
    JsonReader reader = null;
    String resourceFile = "jsonObjectWithLotsOfNestedObjectsData.json";
    try {
      logMsg("Reading contents of resource file " + resourceFile);
      String readerContents = JSONP_Util
          .getContentsOfResourceAsString(resourceFile);
      logMsg("readerContents=" + readerContents);

      // Create expected JSON text from resource contents filtered of whitespace
      // for comparison
      logMsg("Filter readerContents of whitespace for comparison");
      String expJsonText = JSONP_Util.removeWhitespace(readerContents);

      logMsg(
          "Read contents of InputStream from resource file: " + resourceFile);
      reader = Json.createReaderFactory(JSONP_Util.getEmptyConfig())
          .createReader(JSONP_Util.getInputStreamFromResource(resourceFile),
              JSONP_Util.UTF_8);
      JsonObject myJsonObject = (JsonObject) reader.read();

      logMsg("Write the JsonObject 'myJsonObject' out to a JsonWriter");
      StringWriter sWriter = new StringWriter();
      try (JsonWriter writer = Json.createWriter(sWriter)) {
        writer.writeObject(myJsonObject);
        logMsg("Close JsonWriter");
      }

      logMsg("Save contents of the JsonWriter as a String");
      String writerContents = sWriter.toString();

      logMsg("Dump contents of the JsonWriter as a String");
      logMsg("writerContents=" + writerContents);

      logMsg("Filter writerContents of whitespace for comparison");
      String actJsonText = JSONP_Util.removeWhitespace(writerContents);

      logMsg("Compare actual JSON text with expected JSON text");
      pass = JSONP_Util.assertEqualsJsonText(expJsonText, actJsonText);
    } catch (Exception e) {
      throw new Fault("readObjectTest5 Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
    if (!pass)
      throw new Fault("readObjectTest5 Failed");
  }

  /*
   * @testName: readObjectEncodingTest
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:420; JSONP:JAVADOC:185;
   * JSONP:JAVADOC:449;
   * 
   * @test_Strategy: Test read of a JsonObject from a resource file using both
   * encodings of UTF-8 and UTF-16LE.
   * 
   * Test scenario: For each encoding read the appropriate resource file
   * containing a string value. Call JsonObject.getJsonString() to get the value
   * of the JsonString. Compare expected string with actual string for equality.
   */
  public void readObjectEncodingTest() throws Fault {
    boolean pass = true;
    JsonReader reader = null;
    String expString = "a\u65e8\u452c\u8b9e\u6589\u5c57\u5217z";
    String resourceFileUTF8 = "jsonObjectUTF8.json";
    String resourceFileUTF16LE = "jsonObjectUTF16LE.json";
    try {
      logMsg("Reading contents of resource file using UTF-8 encoding "
          + resourceFileUTF8);
      InputStream is = JSONP_Util.getInputStreamFromResource(resourceFileUTF8);
      Map<String, ?> config = JSONP_Util.getEmptyConfig();
      reader = Json.createReaderFactory(config).createReader(is,
          JSONP_Util.UTF_8);
      JsonObject jsonObject = reader.readObject();
      logMsg("Comparing JsonObject values with expected results.");
      String actString = jsonObject.getJsonString("unicodeChars").getString();
      if (!JSONP_Util.assertEquals(expString, actString))
        pass = false;
    } catch (Exception e) {
      throw new Fault("readObjectEncodingTest Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
    try {
      logMsg("Reading contents of resource file using UTF-16LE encoding "
          + resourceFileUTF16LE);
      InputStream is = JSONP_Util
          .getInputStreamFromResource(resourceFileUTF16LE);
      Map<String, ?> config = JSONP_Util.getEmptyConfig();
      reader = Json.createReaderFactory(config).createReader(is,
          JSONP_Util.UTF_16LE);
      JsonObject jsonObject = reader.readObject();
      logMsg("Comparing JsonObject values with expected results.");
      String actString = jsonObject.getJsonString("unicodeChars").getString();
      if (!JSONP_Util.assertEquals(expString, actString))
        pass = false;
    } catch (Exception e) {
      throw new Fault("readObjectEncodingTest Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
    if (!pass)
      throw new Fault("readObjectEncodingTest Failed");
  }

  /*
   * @testName: readUTFEncodedTests
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:420; JSONP:JAVADOC:185;
   * JSONP:JAVADOC:449;
   * 
   * @test_Strategy: Tests the JsonReader reader. Verifies READING of the
   * JsonObject defined in resource files:
   *
   * jsonObjectEncodingUTF8.json jsonObjectEncodingUTF16.json
   * jsonObjectEncodingUTF16LE.json jsonObjectEncodingUTF16BE.json
   * jsonObjectEncodingUTF32LE.json jsonObjectEncodingUTF32BE.json
   * 
   * Creates the JsonReader via the API:
   *
   * JsonReader reader =
   * Json.createReaderFactory(Map<String,?>).createReader(InputStream, Charset)
   *
   * For each supported encoding supported by JSON RFC read the JsonObject and
   * verify we get the expected results. The Charset encoding is passed in as
   * argument for each encoding type read.
   */
  public void readUTFEncodedTests() throws Fault {
    boolean pass = true;
    JsonReader reader = null;
    Map<String, ?> config = JSONP_Util.getEmptyConfig();
    try {
      logMsg(
          "-----------------------------------------------------------------------------------------------");
      logMsg(
          "TEST CASE [Json.createReaderFactory(Map<String,?>).createReader(InputStream, Charset) as UTF-8]");
      logMsg(
          "-----------------------------------------------------------------------------------------------");
      logMsg(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF8.json)");
      InputStream is = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF8.json");
      logMsg(
          "Create JsonReader from the InputStream with character encoding UTF-8");
      reader = Json.createReaderFactory(config).createReader(is,
          JSONP_Util.UTF_8);
      JsonObject jsonObject = reader.readObject();
      if (!compareJsonObjectForUTFEncodedTests(jsonObject))
        pass = false;
    } catch (Exception e) {
      pass = false;
      logErr("Exception occurred testing reading of UTF-8 encoding: " + e);
    } finally {
      try {
        if (reader != null)
          reader.close();
      } catch (Exception e) {
      }
    }
    try {
      logMsg(
          "------------------------------------------------------------------------------------------------");
      logMsg(
          "TEST CASE [Json.createReaderFactory(Map<String,?>).createReader(InputStream, Charset) as UTF-16]");
      logMsg(
          "------------------------------------------------------------------------------------------------");
      logMsg(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF16.json)");
      InputStream is = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF16.json");
      logMsg(
          "Create JsonReader from the InputStream with character encoding UTF-16");
      reader = Json.createReaderFactory(config).createReader(is,
          JSONP_Util.UTF_16);
      JsonObject jsonObject = reader.readObject();
      if (!compareJsonObjectForUTFEncodedTests(jsonObject))
        pass = false;
    } catch (Exception e) {
      pass = false;
      logErr("Exception occurred testing reading of UTF-16 encoding: " + e);
    } finally {
      try {
        if (reader != null)
          reader.close();
      } catch (Exception e) {
      }
    }
    try {
      logMsg(
          "--------------------------------------------------------------------------------------------------");
      logMsg(
          "TEST CASE [Json.createReaderFactory(Map<String,?>).createReader(InputStream, Charset) as UTF-16LE]");
      logMsg(
          "--------------------------------------------------------------------------------------------------");
      logMsg(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF16LE.json)");
      InputStream is = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF16LE.json");
      logMsg(
          "Create JsonReader from the InputStream with character encoding UTF-16LE");
      reader = Json.createReaderFactory(config).createReader(is,
          JSONP_Util.UTF_16LE);
      JsonObject jsonObject = reader.readObject();
      if (!compareJsonObjectForUTFEncodedTests(jsonObject))
        pass = false;
    } catch (Exception e) {
      pass = false;
      logErr("Exception occurred testing reading of UTF-16LE encoding: " + e);
    } finally {
      try {
        if (reader != null)
          reader.close();
      } catch (Exception e) {
      }
    }
    try {
      logMsg(
          "--------------------------------------------------------------------------------------------------");
      logMsg(
          "TEST CASE [Json.createReaderFactory(Map<String,?>).createReader(InputStream, Charset) as UTF-16BE]");
      logMsg(
          "--------------------------------------------------------------------------------------------------");
      logMsg(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF16BE.json)");
      InputStream is = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF16BE.json");
      logMsg(
          "Create JsonReader from the InputStream with character encoding UTF-16BE");
      reader = Json.createReaderFactory(config).createReader(is,
          JSONP_Util.UTF_16BE);
      JsonObject jsonObject = reader.readObject();
      if (!compareJsonObjectForUTFEncodedTests(jsonObject))
        pass = false;
    } catch (Exception e) {
      pass = false;
      logErr("Exception occurred testing reading of UTF-16BE encoding: " + e);
    } finally {
      try {
        if (reader != null)
          reader.close();
      } catch (Exception e) {
      }
    }
    try {
      logMsg(
          "--------------------------------------------------------------------------------------------------");
      logMsg(
          "TEST CASE [Json.createReaderFactory(Map<String,?>).createReader(InputStream, Charset) as UTF-32LE]");
      logMsg(
          "--------------------------------------------------------------------------------------------------");
      logMsg(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF32LE.json)");
      InputStream is = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF32LE.json");
      logMsg(
          "Create JsonReader from the InputStream with character encoding UTF-32LE");
      reader = Json.createReaderFactory(config).createReader(is,
          JSONP_Util.UTF_32LE);
      JsonObject jsonObject = reader.readObject();
      if (!compareJsonObjectForUTFEncodedTests(jsonObject))
        pass = false;
    } catch (Exception e) {
      pass = false;
      logErr("Exception occurred testing reading of UTF-32LE encoding: " + e);
    } finally {
      try {
        if (reader != null)
          reader.close();
      } catch (Exception e) {
      }
    }
    try {
      logMsg(
          "--------------------------------------------------------------------------------------------------");
      logMsg(
          "TEST CASE [Json.createReaderFactory(Map<String,?>).createReader(InputStream, Charset) as UTF-32BE]");
      logMsg(
          "--------------------------------------------------------------------------------------------------");
      logMsg(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF32BE.json)");
      InputStream is = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF32BE.json");
      logMsg(
          "Create JsonReader from the InputStream with character encoding UTF-32BE");
      reader = Json.createReaderFactory(config).createReader(is,
          JSONP_Util.UTF_32BE);
      JsonObject jsonObject = reader.readObject();
      if (!compareJsonObjectForUTFEncodedTests(jsonObject))
        pass = false;
    } catch (Exception e) {
      pass = false;
      logErr("Exception occurred testing reading of UTF-32BE encoding: " + e);
    } finally {
      try {
        if (reader != null)
          reader.close();
      } catch (Exception e) {
      }
    }
    if (!pass)
      throw new Fault("readUTFEncodedTests Failed");
  }

  /*
   * @testName: readUTFEncodedTests2
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:178; JSONP:JAVADOC:185;
   * 
   * @test_Strategy: Tests the JsonReader reader. Verifies READING of the
   * JsonObject defined in resource files:
   *
   * jsonObjectEncodingUTF8.json jsonObjectEncodingUTF16LE.json
   * jsonObjectEncodingUTF16BE.json jsonObjectEncodingUTF32LE.json
   * jsonObjectEncodingUTF32BE.json
   * 
   * Creates the JsonReader via the API:
   *
   * JsonReader reader = Json.createReader(InputStream istream)
   *
   * For each supported encoding supported by JSON RFC read the JsonObject and
   * verify we get the expected results. The character encoding of the stream is
   * auto-detected and determined as per the RFC.
   */
  public void readUTFEncodedTests2() throws Fault {
    boolean pass = true;
    JsonReader reader = null;
    try {
      logMsg("---------------------------------------------------");
      logMsg("TEST CASE [Json.createReader(InputStream) as UTF-8]");
      logMsg("---------------------------------------------------");
      logMsg(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF8.json)");
      InputStream is = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF8.json");
      logMsg(
          "Create JsonReader from the InputStream and auto-detect character encoding UTF-8");
      reader = Json.createReader(is);
      JsonObject jsonObject = reader.readObject();
      if (!compareJsonObjectForUTFEncodedTests(jsonObject))
        pass = false;
    } catch (Exception e) {
      pass = false;
      logErr("Exception occurred testing reading of UTF-8 encoding: " + e);
    } finally {
      try {
        if (reader != null)
          reader.close();
      } catch (Exception e) {
      }
    }
    try {
      logMsg("------------------------------------------------------");
      logMsg("TEST CASE [Json.createReader(InputStream) as UTF-16LE]");
      logMsg("------------------------------------------------------");
      logMsg(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF16LE.json)");
      InputStream is = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF16LE.json");
      logMsg(
          "Create JsonReader from the InputStream and auto-detect character encoding UTF-16LE");
      reader = Json.createReader(is);
      JsonObject jsonObject = reader.readObject();
      if (!compareJsonObjectForUTFEncodedTests(jsonObject))
        pass = false;
    } catch (Exception e) {
      pass = false;
      logErr("Exception occurred testing reading of UTF-16LE encoding: " + e);
    } finally {
      try {
        if (reader != null)
          reader.close();
      } catch (Exception e) {
      }
    }
    try {
      logMsg("------------------------------------------------------");
      logMsg("TEST CASE [Json.createReader(InputStream) as UTF-16BE]");
      logMsg("------------------------------------------------------");
      logMsg(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF16BE.json)");
      InputStream is = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF16BE.json");
      logMsg(
          "Create JsonReader from the InputStream and auto-detect character encoding UTF-16BE");
      reader = Json.createReader(is);
      JsonObject jsonObject = reader.readObject();
      if (!compareJsonObjectForUTFEncodedTests(jsonObject))
        pass = false;
    } catch (Exception e) {
      pass = false;
      logErr("Exception occurred testing reading of UTF-16BE encoding: " + e);
    } finally {
      try {
        if (reader != null)
          reader.close();
      } catch (Exception e) {
      }
    }
    try {
      logMsg("------------------------------------------------------");
      logMsg("TEST CASE [Json.createReader(InputStream) as UTF-32LE]");
      logMsg("------------------------------------------------------");
      logMsg(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF32LE.json)");
      InputStream is = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF32LE.json");
      logMsg(
          "Create JsonReader from the InputStream and auto-detect character encoding UTF-32LE");
      reader = Json.createReader(is);
      JsonObject jsonObject = reader.readObject();
      if (!compareJsonObjectForUTFEncodedTests(jsonObject))
        pass = false;
    } catch (Exception e) {
      pass = false;
      logErr("Exception occurred testing reading of UTF-32LE encoding: " + e);
    } finally {
      try {
        if (reader != null)
          reader.close();
      } catch (Exception e) {
      }
    }
    try {
      logMsg("------------------------------------------------------");
      logMsg("TEST CASE [Json.createReader(InputStream) as UTF-32BE]");
      logMsg("------------------------------------------------------");
      logMsg(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF32BE.json)");
      InputStream is = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF32BE.json");
      logMsg(
          "Create JsonReader from the InputStream and auto-detect character encoding UTF-32BE");
      reader = Json.createReader(is);
      JsonObject jsonObject = reader.readObject();
      if (!compareJsonObjectForUTFEncodedTests(jsonObject))
        pass = false;
    } catch (Exception e) {
      pass = false;
      logErr("Exception occurred testing reading of UTF-32BE encoding: " + e);
    } finally {
      try {
        if (reader != null)
          reader.close();
      } catch (Exception e) {
      }
    }
    if (!pass)
      throw new Fault("readUTFEncodedTests2 Failed");
  }

  /*
   * @testName: negativeObjectTests
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:97; JSONP:JAVADOC:413;
   * 
   * @test_Strategy: Test various Json Syntax Errors when reading a JsonObject.
   * The tests trip various JsonParsingException/JsonException conditions when
   * reading an object.
   *
   */
  public void negativeObjectTests() throws Fault {
    boolean pass = true;
    JsonReader reader = null;

    // Not an object []

    try {
      logMsg("Testing for not an object '[]'");
      reader = Json.createReader(new StringReader("[]"));
      JsonObject value = reader.readObject();
      pass = false;
      logErr("Failed to throw JsonException");
    } catch (JsonException e) {
      logMsg("Got expected JsonException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Trip JsonParsingException for JsonReader.readObject() if incorrect
    // representation for object
    try {
      logMsg(
          "Trip JsonParsingException for JsonReader.read() if incorrect representation for object.");
      logMsg("Reading " + "{\"name\":\"value\",1,2,3}");
      reader = Json
          .createReader(new StringReader("{\"name\":\"value\",1,2,3}"));
      JsonObject jsonObject = reader.readObject();
      logErr("Did not get expected JsonParsingException");
      pass = false;
    } catch (JsonParsingException e) {
      logMsg("Caught expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    }

    // Missing [

    try {
      logMsg("Testing for missing '['");
      reader = Json.createReader(new StringReader("{1,2]}"));
      JsonObject value = reader.readObject();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing ]

    try {
      logMsg("Testing for missing ']'");
      reader = Json.createReader(new StringReader("{[1,2}"));
      JsonObject value = reader.readObject();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing {

    try {
      logMsg("Testing for missing '{'");
      reader = Json.createReader(new StringReader("}"));
      JsonObject value = reader.readObject();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing }

    try {
      logMsg("Testing for missing '}'");
      reader = Json.createReader(new StringReader("{"));
      JsonObject value = reader.readObject();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing , between array elements test case 1

    try {
      logMsg("Testing for missing ',' between array elements test case 1");
      reader = Json.createReader(new StringReader("{[5\"foo\"]}"));
      JsonObject value = reader.readObject();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing , between array elements test case 2

    try {
      logMsg("Testing for missing ',' between array elements test case 2");
      reader = Json.createReader(new StringReader("{[5{}]}"));
      JsonObject value = reader.readObject();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing , between object elements test case 1

    try {
      logMsg("Testing for missing ',' between object elements test case 1");
      reader = Json.createReader(new StringReader("{\"foo\":\"bar\"5}"));
      JsonObject value = reader.readObject();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing , between object elements test case 2

    try {
      logMsg("Testing for missing ',' between object elements test case 2");
      reader = Json.createReader(new StringReader("{\"one\":1[]}"));
      JsonObject value = reader.readObject();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing key name in object element

    try {
      logMsg("Testing for missing key name in object element");
      reader = Json.createReader(new StringReader("{:\"bar\"}"));
      JsonObject value = reader.readObject();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing value name in object element

    try {
      logMsg("Testing for missing value name in object element");
      reader = Json.createReader(new StringReader("{\"foo\":}"));
      JsonObject value = reader.readObject();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing double quote on a name

    try {
      logMsg("Test for missing double quote on a name");
      reader = Json.createReader(new StringReader("{name\" : \"value\"}"));
      JsonObject value = reader.readObject();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing double quote on a value

    try {
      logMsg("Test for missing double quote on a value");
      reader = Json.createReader(new StringReader("{\"name\" : value\"}"));
      JsonObject value = reader.readObject();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 1

    try {
      logMsg("Incorrect digit value -foo");
      reader = Json.createReader(new StringReader("{\"number\" : -foo}"));
      JsonObject value = reader.readObject();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 2

    try {
      logMsg("Incorrect digit value +foo");
      reader = Json.createReader(new StringReader("{\"number\" : +foo}"));
      JsonObject value = reader.readObject();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 3

    try {
      logMsg("Incorrect digit value -784foo");
      reader = Json.createReader(new StringReader("{\"number\" : -784foo}"));
      JsonObject value = reader.readObject();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 4

    try {
      logMsg("Incorrect digit value +784foo");
      reader = Json.createReader(new StringReader("{\"number\" : +784foo}"));
      JsonObject value = reader.readObject();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 5

    try {
      logMsg("Incorrect digit value 0.1E5E5");
      reader = Json.createReader(new StringReader("{\"number\" : 0.1E5E5}"));
      JsonObject value = reader.readObject();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 6

    try {
      logMsg("Incorrect digit value  0.F10");
      reader = Json.createReader(new StringReader("{\"number\" : 0.F10}"));
      JsonObject value = reader.readObject();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 7

    try {
      logMsg("Incorrect digit value string");
      reader = Json.createReader(new StringReader("{\"number\" : string}"));
      JsonObject value = reader.readObject();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 8 (hex numbers invalid per JSON RFC)

    try {
      logMsg("Incorrect digit value hex numbers invalid per JSON RFC");
      reader = Json.createReader(new StringReader("{\"number\" : 0x137a}"));
      JsonObject value = reader.readObject();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 9 (octal numbers invalid per JSON RFC)

    try {
      logMsg("Incorrect digit value octal numbers invalid per JSON RFC");
      reader = Json.createReader(new StringReader("{\"number\" : 0137}"));
      JsonObject value = reader.readObject();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    if (!pass)
      throw new Fault("negativeObjectTests Failed");
  }

  /*
   * @testName: negativeArrayTests
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:97; JSONP:JAVADOC:412;
   * 
   * @test_Strategy: Test various Json Syntax Errors when reading a JsonArray.
   * The tests trip various JsonParsingException/JsonException conditions when
   * reading an array.
   *
   */
  public void negativeArrayTests() throws Fault {
    boolean pass = true;
    JsonReader reader = null;

    // Not an array {}

    try {
      logMsg("Testing for not an array '{}'");
      reader = Json.createReader(new StringReader("{}"));
      JsonArray value = reader.readArray();
      pass = false;
      logErr("Failed to throw JsonException");
    } catch (JsonException e) {
      logMsg("Got expected JsonException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Trip JsonParsingException for JsonReader.readArray() if incorrect
    // representation for array
    try {
      logMsg(
          "Trip JsonParsingException for JsonReader.readArray() if incorrect representation for array.");
      logMsg("Reading " + "[foo,10,\"name\":\"value\"]");
      reader = Json
          .createReader(new StringReader("[foo,10,\"name\":\"value\"]"));
      JsonArray jsonArray = reader.readArray();
      logErr("Did not get expected JsonParsingException");
      pass = false;
    } catch (JsonParsingException e) {
      logMsg("Caught expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    }

    // Missing [

    try {
      logMsg("Testing for missing '['");
      reader = Json.createReader(new StringReader("]"));
      JsonArray value = reader.readArray();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing ]

    try {
      logMsg("Testing for missing ']'");
      reader = Json.createReader(new StringReader("["));
      JsonArray value = reader.readArray();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing {

    try {
      logMsg("Testing for missing '{'");
      reader = Json.createReader(new StringReader("[1,\"name\":\"value\"}]"));
      JsonArray value = reader.readArray();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing }

    try {
      logMsg("Testing for missing '}'");
      reader = Json.createReader(new StringReader("[1,{\"name\":\"value\"]"));
      JsonArray value = reader.readArray();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing , between array elements test case 1

    try {
      logMsg("Testing for missing ',' between array elements test case 1");
      reader = Json.createReader(new StringReader("[5\"foo\"]"));
      JsonArray value = reader.readArray();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing , between array elements test case 2

    try {
      logMsg("Testing for missing ',' between array elements test case 2");
      reader = Json.createReader(new StringReader("[5{}]"));
      JsonArray value = reader.readArray();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing , between object elements test case 1

    try {
      logMsg("Testing for missing ',' between object elements test case 1");
      reader = Json.createReader(new StringReader("[{\"foo\":\"bar\"5}]"));
      JsonArray value = reader.readArray();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing , between object elements test case 2

    try {
      logMsg("Testing for missing ',' between object elements test case 2");
      reader = Json.createReader(new StringReader("[{\"one\":1[]}]"));
      JsonArray value = reader.readArray();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing key name in object element

    try {
      logMsg("Testing for missing key name in object element");
      reader = Json.createReader(new StringReader("[{:\"bar\"}]"));
      JsonArray value = reader.readArray();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing value name in object element

    try {
      logMsg("Testing for missing value name in object element");
      reader = Json.createReader(new StringReader("[{\"foo\":}]"));
      JsonArray value = reader.readArray();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing double quote on a name

    try {
      logMsg("Test for missing double quote on a name");
      reader = Json.createReader(new StringReader("[{name\" : \"value\"}]"));
      JsonArray value = reader.readArray();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing double quote on a value

    try {
      logMsg("Test for missing double quote on a value");
      reader = Json.createReader(new StringReader("[{\"name\" : value\"}]"));
      JsonArray value = reader.readArray();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 1

    try {
      logMsg("Incorrect digit value -foo");
      reader = Json.createReader(new StringReader("[-foo]"));
      JsonArray value = reader.readArray();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 2

    try {
      logMsg("Incorrect digit value +foo");
      reader = Json.createReader(new StringReader("[+foo]"));
      JsonArray value = reader.readArray();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 3

    try {
      logMsg("Incorrect digit value -784foo");
      reader = Json.createReader(new StringReader("[-784foo]"));
      JsonArray value = reader.readArray();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 4

    try {
      logMsg("Incorrect digit value +784foo");
      reader = Json.createReader(new StringReader("[+784foo]"));
      JsonArray value = reader.readArray();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 5

    try {
      logMsg("Incorrect digit value 0.1E5E5");
      reader = Json.createReader(new StringReader("[0.1E5E5]"));
      JsonArray value = reader.readArray();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 6

    try {
      logMsg("Incorrect digit value  0.F10");
      reader = Json.createReader(new StringReader("[0.F10]"));
      JsonArray value = reader.readArray();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 7

    try {
      logMsg("Incorrect digit value string");
      reader = Json.createReader(new StringReader("[string]"));
      JsonArray value = reader.readArray();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 8 (hex numbers invalid per JSON RFC)

    try {
      logMsg("Incorrect digit value hex numbers invalid per JSON RFC");
      reader = Json.createReader(new StringReader("[0x137a]"));
      JsonArray value = reader.readArray();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 9 (octal numbers invalid per JSON RFC)

    try {
      logMsg("Incorrect digit value octal numbers invalid per JSON RFC");
      reader = Json.createReader(new StringReader("[0137]"));
      JsonArray value = reader.readArray();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    if (!pass)
      throw new Fault("negativeArrayTests Failed");
  }

  /*
   * @testName: illegalStateExceptionTests
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:97; JSONP:JAVADOC:218;
   * JSONP:JAVADOC:220; JSONP:JAVADOC:183;
   * 
   * @test_Strategy: Test IllegalStateException test conditions.
   *
   */
  public void illegalStateExceptionTests() throws Fault {
    boolean pass = true;
    JsonReader reader = null;

    // IllegalStateException if reader.close() called before reader.read()
    try {
      reader = Json.createReader(new StringReader("{}"));
      reader.close();
      logMsg(
          "Calling reader.read() after reader.close() is called is illegal.");
      JsonStructure value = reader.read();
      pass = false;
      logErr("Failed to throw IllegalStateException");
    } catch (IllegalStateException e) {
      logMsg("Got expected IllegalStateException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    }

    // IllegalStateException if reader.read() called after reader.readObject()
    try {
      reader = Json.createReader(new StringReader("{}"));
      JsonObject value = reader.readObject();
      logMsg(
          "Calling reader.readObject() after reader.readObject() was called is illegal.");
      value = (JsonObject) reader.read();
      pass = false;
      logErr("Failed to throw IllegalStateException");
    } catch (IllegalStateException e) {
      logMsg("Got expected IllegalStateException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // IllegalStateException if reader.read() called after reader.readArray()
    try {
      reader = Json.createReader(new StringReader("[]"));
      JsonArray value = reader.readArray();
      logMsg(
          "Calling reader.read() after reader.readArray() was called is illegal.");
      value = (JsonArray) reader.read();
      pass = false;
      logErr("Failed to throw IllegalStateException");
    } catch (IllegalStateException e) {
      logMsg("Got expected IllegalStateException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // IllegalStateException if reader.close() called before reader.readObject()
    try {
      reader = Json.createReader(new StringReader("{}"));
      reader.close();
      logMsg(
          "Calling reader.readObject() after reader.close() is called is illegal.");
      JsonObject value = reader.readObject();
      pass = false;
      logErr("Failed to throw IllegalStateException");
    } catch (IllegalStateException e) {
      logMsg("Got expected IllegalStateException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    }

    // IllegalStateException if reader.readObject() called after
    // reader.readObject()
    try {
      reader = Json.createReader(new StringReader("{}"));
      JsonObject value = reader.readObject();
      logMsg(
          "Calling reader.readObject() after reader.readObject() was called is illegal.");
      value = reader.readObject();
      pass = false;
      logErr("Failed to throw IllegalStateException");
    } catch (IllegalStateException e) {
      logMsg("Got expected IllegalStateException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // IllegalStateException if reader.readArray() called after
    // reader.readObject()
    try {
      reader = Json.createReader(new StringReader("{}"));
      JsonObject obj = reader.readObject();
      logMsg(
          "Calling reader.readArray() after reader.readObject() was called is illegal.");
      JsonArray arr = reader.readArray();
      pass = false;
      logErr("Failed to throw IllegalStateException");
    } catch (IllegalStateException e) {
      logMsg("Got expected IllegalStateException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // IllegalStateException if reader.close() called before reader.readArray()
    try {
      reader = Json.createReader(new StringReader("[]"));
      reader.close();
      logMsg(
          "Calling reader.readArray() after reader.close() is called is illegal.");
      JsonArray value = reader.readArray();
      pass = false;
      logErr("Failed to throw IllegalStateException");
    } catch (IllegalStateException e) {
      logMsg("Got expected IllegalStateException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    }

    // IllegalStateException if reader.readArray() called after
    // reader.readArray()
    try {
      reader = Json.createReader(new StringReader("[]"));
      JsonArray value = reader.readArray();
      logMsg(
          "Calling reader.readArray() after reader.readArray() was called is illegal.");
      value = reader.readArray();
      pass = false;
      logErr("Failed to throw IllegalStateException");
    } catch (IllegalStateException e) {
      logMsg("Got expected IllegalStateException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // IllegalStateException if reader.readObject() called after
    // reader.readArray()
    try {
      reader = Json.createReader(new StringReader("[]"));
      JsonArray arr = reader.readArray();
      logMsg(
          "Calling reader.readObject() after reader.readArray() was called is illegal.");
      JsonObject obj = reader.readObject();
      pass = false;
      logMsg("obj=" + obj);
      logErr("Failed to throw IllegalStateException");
    } catch (IllegalStateException e) {
      logMsg("Got expected IllegalStateException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    if (!pass)
      throw new Fault("illegalStateExceptionTests Failed");
  }

  /*
   * @testName: negativeJsonStructureTests
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:97; JSONP:JAVADOC:411;
   * 
   * @test_Strategy: Test various Json Syntax Errors when reading a
   * JsonStructure. The tests trip various JsonParsingException conditions when
   * doing a read.
   *
   */
  public void negativeJsonStructureTests() throws Fault {
    boolean pass = true;
    JsonReader reader = null;

    // Trip JsonParsingException for JsonReader.read() if incorrect
    // representation for array
    try {
      logMsg(
          "Trip JsonParsingException for JsonReader.read() if incorrect representation for array.");
      logMsg("Reading " + "[foo,10,\"name\":\"value\"]");
      reader = Json
          .createReader(new StringReader("[foo,10,\"name\":\"value\"]"));
      JsonStructure jsonStructure = reader.read();
      logErr("Did not get expected JsonParsingException");
      pass = false;
    } catch (JsonParsingException e) {
      logMsg("Caught expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    }

    // Trip JsonParsingException for JsonReader.read() if incorrect
    // representation for object
    try {
      logMsg(
          "Trip JsonParsingException for JsonReader.read() if incorrect representation for object.");
      logMsg("Reading " + "{\"name\":\"value\",1,2,3}");
      reader = Json
          .createReader(new StringReader("{\"name\":\"value\",1,2,3}"));
      JsonStructure jsonStructure = reader.read();
      logErr("Did not get expected JsonParsingException");
      pass = false;
    } catch (JsonParsingException e) {
      logMsg("Caught expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    }

    // incorrect representation {]
    try {
      logMsg("Testing for incorrect representation '{]'");
      reader = Json.createReader(new StringReader("{]"));
      logMsg(
          "Calling reader.read() with incorrect representation should throw JsonParsingException");
      JsonStructure value = reader.read();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    }

    // Missing [

    try {
      logMsg("Testing for missing '['");
      reader = Json.createReader(new StringReader("{1,2]}"));
      JsonStructure value = reader.read();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing ]

    try {
      logMsg("Testing for missing ']'");
      reader = Json.createReader(new StringReader("{[1,2}"));
      JsonStructure value = reader.read();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing {

    try {
      logMsg("Testing for missing '{'");
      reader = Json.createReader(new StringReader("}"));
      JsonStructure value = reader.read();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing }

    try {
      logMsg("Testing for missing '}'");
      reader = Json.createReader(new StringReader("{"));
      JsonStructure value = reader.read();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing , between array elements test case 1

    try {
      logMsg("Testing for missing ',' between array elements test case 1");
      reader = Json.createReader(new StringReader("{[5\"foo\"]}"));
      JsonStructure value = reader.read();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing , between array elements test case 2

    try {
      logMsg("Testing for missing ',' between array elements test case 2");
      reader = Json.createReader(new StringReader("{[5{}]}"));
      JsonStructure value = reader.read();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing , between object elements test case 1

    try {
      logMsg("Testing for missing ',' between object elements test case 1");
      reader = Json.createReader(new StringReader("{\"foo\":\"bar\"5}"));
      JsonStructure value = reader.read();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing , between object elements test case 2

    try {
      logMsg("Testing for missing ',' between object elements test case 2");
      reader = Json.createReader(new StringReader("{\"one\":1[]}"));
      JsonStructure value = reader.read();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing key name in object element

    try {
      logMsg("Testing for missing key name in object element");
      reader = Json.createReader(new StringReader("{:\"bar\"}"));
      JsonStructure value = reader.read();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing value name in object element

    try {
      logMsg("Testing for missing value name in object element");
      reader = Json.createReader(new StringReader("{\"foo\":}"));
      JsonStructure value = reader.read();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing double quote on a name

    try {
      logMsg("Test for missing double quote on a name");
      reader = Json.createReader(new StringReader("{name\" : \"value\"}"));
      JsonStructure value = reader.read();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing double quote on a value

    try {
      logMsg("Test for missing double quote on a value");
      reader = Json.createReader(new StringReader("{\"name\" : value\"}"));
      JsonStructure value = reader.read();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 1

    try {
      logMsg("Incorrect digit value -foo");
      reader = Json.createReader(new StringReader("{\"number\" : -foo}"));
      JsonStructure value = reader.read();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 2

    try {
      logMsg("Incorrect digit value +foo");
      reader = Json.createReader(new StringReader("{\"number\" : +foo}"));
      JsonStructure value = reader.read();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 3

    try {
      logMsg("Incorrect digit value -784foo");
      reader = Json.createReader(new StringReader("{\"number\" : -784foo}"));
      JsonStructure value = reader.read();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 4

    try {
      logMsg("Incorrect digit value +784foo");
      reader = Json.createReader(new StringReader("{\"number\" : +784foo}"));
      JsonStructure value = reader.read();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 5

    try {
      logMsg("Incorrect digit value 0.1E5E5");
      reader = Json.createReader(new StringReader("{\"number\" : 0.1E5E5}"));
      JsonStructure value = reader.read();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 6

    try {
      logMsg("Incorrect digit value  0.F10");
      reader = Json.createReader(new StringReader("{\"number\" : 0.F10}"));
      JsonStructure value = reader.read();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 7

    try {
      logMsg("Incorrect digit value string");
      reader = Json.createReader(new StringReader("{\"number\" : string}"));
      JsonStructure value = reader.read();
      pass = false;
      logErr("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      logMsg("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    if (!pass)
      throw new Fault("negativeJsonStructureTests Failed");
  }

  /*
   * @testName: jsonReaderIOErrorTests
   * 
   * @assertion_ids: JSONP:JAVADOC:182; JSONP:JAVADOC:217; JSONP:JAVADOC:219;
   * JSONP:JAVADOC:410;
   * 
   * @test_Strategy: Tests for JsonException for testable i/o errors.
   *
   */
  public void jsonReaderIOErrorTests() throws Fault {
    boolean pass = true;

    String jsonArrayText = "[\"name1\",\"value1\"]";
    String jsonObjectText = "{\"name1\":\"value1\"}";

    // Trip JsonException if there is an i/o error on JsonReader.close()
    try {
      logMsg(
          "Trip JsonException if there is an i/o error on JsonReader.close().");
      logMsg("Reading object " + jsonObjectText);
      InputStream is = JSONP_Util.getInputStreamFromString(jsonObjectText);
      MyBufferedInputStream mbi = new MyBufferedInputStream(is);
      try (JsonReader reader = Json.createReader(mbi)) {
        JsonObject jsonObject = reader.readObject();
        logMsg("jsonObject=" + jsonObject);
        mbi.setThrowIOException(true);
        logMsg("Calling JsonReader.close()");
        mbi.setThrowIOException(true);
      }
      logErr("Did not get expected JsonException");
      pass = false;
    } catch (JsonException e) {
      logMsg("Caught expected JsonException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    }

    // Trip JsonException for JsonReader.read() if i/o error
    try {
      logMsg("Trip JsonException for JsonReader.read() if i/o error.");
      logMsg("Reading array " + jsonArrayText);
      MyBufferedReader mbr = new MyBufferedReader(
          new StringReader(jsonArrayText));
      JsonReader reader = Json.createReader(mbr);
      mbr.setThrowIOException(true);
      logMsg("Calling JsonReader.read()");
      JsonStructure jsonStructure = reader.read();
      logErr("Did not get expected JsonException");
      pass = false;
    } catch (JsonException e) {
      logMsg("Caught expected JsonException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    }

    // Trip JsonException for JsonReader.readArray() if i/o error
    try {
      logMsg("Trip JsonException for JsonReader.readArray() if i/o error.");
      logMsg("Reading array " + jsonArrayText);
      MyBufferedReader mbr = new MyBufferedReader(
          new StringReader(jsonArrayText));
      JsonReader reader = Json.createReader(mbr);
      mbr.setThrowIOException(true);
      logMsg("Calling JsonReader.readArray()");
      JsonArray jsonArray = reader.readArray();
      logErr("Did not get expected JsonException");
      pass = false;
    } catch (JsonException e) {
      logMsg("Caught expected JsonException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    }

    // Trip JsonException for JsonReader.readObject() if i/o error
    try {
      logMsg("Trip JsonException for JsonReader.read() if i/o error.");
      logMsg("Reading object " + jsonObjectText);
      MyBufferedReader mbr = new MyBufferedReader(
          new StringReader(jsonObjectText));
      JsonReader reader = Json.createReader(mbr);
      mbr.setThrowIOException(true);
      logMsg("Calling JsonReader.readObject()");
      JsonObject jsonObject = reader.readObject();
      logErr("Did not get expected JsonException");
      pass = false;
    } catch (JsonException e) {
      logMsg("Caught expected JsonException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    }

    if (!pass)
      throw new Fault("jsonReaderIOErrorTests Failed");
  }

  /*
   * @testName: invalidLiteralNamesTest
   * 
   * @assertion_ids: JSONP:JAVADOC:97; JSONP:JAVADOC:411;
   * 
   * @test_Strategy: This test trips various JsonParsingException conditions
   * when reading an uppercase literal name that must be lowercase per JSON RFC
   * for the literal values (true, false or null).
   *
   */
  public void invalidLiteralNamesTest() throws Fault {
    boolean pass = true;
    JsonReader reader;

    // Trip JsonParsingException for JsonReader.read() if invalid liternal TRUE
    // instead of true
    try {
      logMsg(
          "Trip JsonParsingException for JsonReader.read() if invalid liternal TRUE instead of true.");
      logMsg("Reading " + "[TRUE]");
      reader = Json.createReader(new StringReader("[TRUE]"));
      JsonStructure jsonStructure = reader.read();
      logErr("Did not get expected JsonParsingException");
      pass = false;
    } catch (JsonParsingException e) {
      logMsg("Caught expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    }

    // Trip JsonParsingException for JsonReader.read() if invalid liternal FALSE
    // instead of false
    try {
      logMsg(
          "Trip JsonParsingException for JsonReader.read() if invalid liternal FALSE instead of false.");
      logMsg("Reading " + "[FALSE]");
      reader = Json.createReader(new StringReader("[FALSE]"));
      JsonStructure jsonStructure = reader.read();
      logErr("Did not get expected JsonParsingException");
      pass = false;
    } catch (JsonParsingException e) {
      logMsg("Caught expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    }

    // Trip JsonParsingException for JsonReader.read() if invalid liternal NULL
    // instead of null
    try {
      logMsg(
          "Trip JsonParsingException for JsonReader.read() if invalid liternal NULL instead of null.");
      logMsg("Reading " + "[NULL]");
      reader = Json.createReader(new StringReader("[NULL]"));
      JsonStructure jsonStructure = reader.read();
      logErr("Did not get expected JsonParsingException");
      pass = false;
    } catch (JsonParsingException e) {
      logMsg("Caught expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    }

    // Trip JsonParsingException for JsonReader.read() if invalid liternal TRUE
    // instead of true
    try {
      logMsg(
          "Trip JsonParsingException for JsonReader.read() if invalid liternal TRUE instead of true.");
      logMsg("Reading " + "{\"true\":TRUE}");
      reader = Json.createReader(new StringReader("{\"true\":TRUE}"));
      JsonStructure jsonStructure = reader.read();
      logErr("Did not get expected JsonParsingException");
      pass = false;
    } catch (JsonParsingException e) {
      logMsg("Caught expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    }

    // Trip JsonParsingException for JsonReader.read() if invalid liternal FALSE
    // instead of false
    try {
      logMsg(
          "Trip JsonParsingException for JsonReader.read() if invalid liternal FALSE instead of false.");
      logMsg("Reading " + "{\"false\":FALSE}");
      reader = Json.createReader(new StringReader("{\"false\":FALSE}"));
      JsonStructure jsonStructure = reader.read();
      logErr("Did not get expected JsonParsingException");
      pass = false;
    } catch (JsonParsingException e) {
      logMsg("Caught expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    }

    // Trip JsonParsingException for JsonReader.read() if invalid liternal NULL
    // instead of null
    try {
      logMsg(
          "Trip JsonParsingException for JsonReader.read() if invalid liternal NULL instead of null.");
      logMsg("Reading " + "{\"null\":NULL}");
      reader = Json.createReader(new StringReader("{\"null\":NULL}"));
      JsonStructure jsonStructure = reader.read();
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
   * @testName: jsonReader11Test
   * 
   * @assertion_ids: JSONP:JAVADOC:646; JSONP:JAVADOC:583; JSONP:JAVADOC:584;
   * JSONP:JAVADOC:585; JSONP:JAVADOC:586; JSONP:JAVADOC:587; JSONP:JAVADOC:588;
   * JSONP:JAVADOC:662; JSONP:JAVADOC:663; JSONP:JAVADOC:664; JSONP:JAVADOC:665;
   * JSONP:JAVADOC:666; JSONP:JAVADOC:667;
   * 
   * @test_Strategy: Tests JsonReader API methods added in JSON-P 1.1.
   */
  public void jsonReader11Test() throws Fault {
    Reader readerTest = new Reader();
    final TestResult result = readerTest.test();
    result.eval();
  }

}
