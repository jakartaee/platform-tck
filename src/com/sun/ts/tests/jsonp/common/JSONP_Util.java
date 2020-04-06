/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsonp.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import java.io.*;
import java.net.*;
import java.lang.reflect.*;
import java.nio.charset.Charset;

import java.math.BigInteger;
import java.math.BigDecimal;

import jakarta.json.*;
import jakarta.json.stream.*;
import jakarta.json.stream.JsonParser.Event.*;
import jakarta.json.JsonValue.ValueType.*;

public final class JSONP_Util {

  // Charset CONSTANTS for all the supported UTF encodings
  public static final Charset UTF_8 = Charset.forName("UTF-8");

  public static final Charset UTF_16 = Charset.forName("UTF-16");

  public static final Charset UTF_16BE = Charset.forName("UTF-16BE");

  public static final Charset UTF_16LE = Charset.forName("UTF-16LE");

  public static final Charset UTF_32BE = Charset.forName("UTF-32BE");

  public static final Charset UTF_32LE = Charset.forName("UTF-32LE");

  // Test Config properties
  public static final String FOO_CONFIG = "com.sun.ts.tests.jsonp.common.FOO_CONFIG";

  // Number of parser errors encountered
  private static int parseErrs = 0;

  // A JsonNumber NumberType is now INTEGRAL or NON_INTEGRAL based on
  // JsonNumber.isIntegral().
  // The following 2 constant definitions represent these NumberType boolean
  // values.
  public static final boolean INTEGRAL = true;

  public static final boolean NON_INTEGRAL = false;

  /*********************************************************************************
   * void dumpContentsOfResource(String resource)
   *********************************************************************************/
  public static void dumpContentsOfResource(String resource) {
    TestUtil.logMsg("Dumping contents of Resource file: " + resource);
    BufferedReader reader = null;
    try {
      InputStream iStream = JSONP_Util.class
          .getResourceAsStream("/" + resource);
      if (iStream == null) {
        TestUtil.logErr(
            "dumpContentsOfResource: no resource found in classpath or archive named "
                + resource);
        return;
      }
      reader = new BufferedReader(new InputStreamReader(iStream));
      String thisLine;
      while ((thisLine = reader.readLine()) != null) {
        TestUtil.logMsg(thisLine);
      }

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (reader != null)
        try {
          reader.close();
        } catch (Exception e) {
          TestUtil.logErr("exception closing stream: " + e);
        }
    }
  }

  /*********************************************************************************
   * void dumpFile(String file)
   *********************************************************************************/
  public static void dumpFile(String file) {
    TestUtil.logMsg("Dumping contents of file: " + file);
    BufferedReader reader = null;
    try {
      FileInputStream fis = new FileInputStream(file);
      if (fis == null) {
        TestUtil.logErr("dumpFile: no file found named " + file);
        return;
      }
      reader = new BufferedReader(new InputStreamReader(fis));
      String thisLine;
      while ((thisLine = reader.readLine()) != null) {
        TestUtil.logMsg(thisLine);
      }

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (reader != null)
        try {
          reader.close();
        } catch (Exception e) {
          TestUtil.logErr("exception closing stream: " + e);
        }
    }
  }

  /*********************************************************************************
   * String getContentsOfResourceAsString(String resource)
   *********************************************************************************/
  public static String getContentsOfResourceAsString(String resource) {
    StringBuilder sb = new StringBuilder();
    BufferedReader reader = null;
    try {
      InputStream iStream = JSONP_Util.class
          .getResourceAsStream("/" + resource);
      if (iStream == null) {
        TestUtil.logErr(
            "dumpContentsOfResource: no resource found in classpath or archive named "
                + resource);
        return null;
      }
      reader = new BufferedReader(new InputStreamReader(iStream));
      String thisLine;
      while ((thisLine = reader.readLine()) != null) {
        sb.append(thisLine);
      }

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (reader != null)
        try {
          reader.close();
        } catch (Exception e) {
          TestUtil.logErr("exception closing stream: " + e);
        }
    }
    return sb.toString();
  }

  /*********************************************************************************
   * Reader getReaderFromResource(String resource)
   *********************************************************************************/
  public static Reader getReaderFromResource(String resource) {
    InputStreamReader reader = null;
    try {
      InputStream iStream = JSONP_Util.class
          .getResourceAsStream("/" + resource);
      if (iStream == null)
        TestUtil.logErr(
            "getReaderFromResource: no resource found in classpath or archive named "
                + resource);
      else
        reader = new InputStreamReader(iStream);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return reader;
  }

  /*********************************************************************************
   * Reader getReaderFromString(String contents)
   *********************************************************************************/
  public static Reader getReaderFromString(String contents) {
    InputStreamReader reader = null;
    try {
      InputStream iStream = new ByteArrayInputStream(contents.getBytes(UTF_8));
      if (iStream == null)
        TestUtil.logErr("getReaderFromString: no input stream");
      else
        reader = new InputStreamReader(iStream);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return reader;
  }

  /*********************************************************************************
   * InputStream getInputStreamFromResource(String resource)
   *********************************************************************************/
  public static InputStream getInputStreamFromResource(String resource) {
    InputStream iStream = null;
    try {
      iStream = JSONP_Util.class.getResourceAsStream("/" + resource);
      if (iStream == null)
        TestUtil.logErr(
            "getInputStreamFromResource: no resource found in classpath or archive named "
                + resource);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return iStream;
  }

  /*********************************************************************************
   * InputStream getInputStreamFromString(String contents)
   *********************************************************************************/
  public static InputStream getInputStreamFromString(String contents) {
    InputStream iStream = null;
    try {
      iStream = new ByteArrayInputStream(contents.getBytes(UTF_8));
      if (iStream == null)
        TestUtil.logErr("getInputStreamFromString: no input stream");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return iStream;
  }

  /*********************************************************************************
   * InputStream getInputStreamFromOutputStream(ByteArrayOutputStream baos)
   *********************************************************************************/
  public static InputStream getInputStreamFromOutputStream(
      ByteArrayOutputStream baos) {
    InputStream iStream = null;
    try {
      iStream = new ByteArrayInputStream(baos.toByteArray());
      if (iStream == null)
        TestUtil.logErr("getInputStreamFromOutputStream: no input stream");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return iStream;
  }

  /*********************************************************************************
   * String removeWhitespace(String text)
   *
   * NOTE: This does not remove whitespace of Json text if a quoted string which
   * can include whitespace.
   *********************************************************************************/
  public static String removeWhitespace(String text) {
    StringReader reader = new StringReader(text);
    StringWriter writer = new StringWriter();
    try {
      boolean quotedString = false;
      boolean backslash = false;
      int c;
      while ((c = reader.read()) != -1) {
        // Skip white space if not quoted string
        if (!quotedString) {
          if (Character.isWhitespace(c))
            continue;
        }
        writer.write(c);
        if (c == '"') {
          if (!backslash)
            quotedString = !quotedString;
          backslash = false;
        } else if (c == '\\')
          backslash = true;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return writer.toString();
  }

  /*********************************************************************************
   * JsonNumber createJsonNumber
   *
   * The following method signatures are available:
   *
   * o JsonNumber createJsonNumber(double) o JsonNumber createJsonNumber(long) o
   * JsonNumber createJsonNumber(int) o JsonNumber createJsonNumber(BigDecimal)
   * o JsonNumber createJsonNumber(BigInteger)
   *********************************************************************************/
  public static JsonNumber createJsonNumber(double val) {
    JsonArray array = Json.createArrayBuilder().add(val).build();
    return array.getJsonNumber(0);
  }

  public static JsonNumber createJsonNumber(long val) {
    JsonArray array = Json.createArrayBuilder().add(val).build();
    return array.getJsonNumber(0);
  }

  public static JsonNumber createJsonNumber(int val) {
    JsonArray array = Json.createArrayBuilder().add(val).build();
    return array.getJsonNumber(0);
  }

  public static JsonNumber createJsonNumber(BigDecimal val) {
    JsonArray array = Json.createArrayBuilder().add(val).build();
    return array.getJsonNumber(0);
  }

  public static JsonNumber createJsonNumber(BigInteger val) {
    JsonArray array = Json.createArrayBuilder().add(val).build();
    return array.getJsonNumber(0);
  }

  /*********************************************************************************
   * JsonString createJsonString(String val)
   *********************************************************************************/
  public static JsonString createJsonString(String val) {
    JsonArray array = Json.createArrayBuilder().add(val).build();
    return array.getJsonString(0);
  }

  /*********************************************************************************
   * void dumpJsonString(JsonString val)
   *********************************************************************************/
  public static void dumpJsonString(JsonString value) {
    TestUtil.logMsg("dumpJsonString->" + toStringJsonString(value));
  }

  /*********************************************************************************
   * void dumpJsonArray(JsonArray value)
   *********************************************************************************/
  public static void dumpJsonArray(JsonArray value) {
    TestUtil.logMsg("dumpJsonArray->" + toStringJsonArray(value));
  }

  /*********************************************************************************
   * void dumpJsonObject(JsonObject value)
   *********************************************************************************/
  public static void dumpJsonObject(JsonObject value) {
    TestUtil.logMsg("dumpJsonObject->" + toStringJsonObject(value));
  }

  /*********************************************************************************
   * void dumpJsonConstant(JsonValue value)
   *********************************************************************************/
  public static void dumpJsonConstant(JsonValue value) {
    TestUtil.logMsg("dumpJsonConstant->" + toStringJsonConstant(value));
  }

  /*********************************************************************************
   * void dumpJsonNumber(JsonNumber value)
   *********************************************************************************/
  public static void dumpJsonNumber(JsonNumber value) {
    TestUtil.logMsg("dumpJsonNumber->" + toStringJsonNumber(value));
  }

  /*********************************************************************************
   * void dumpJsonValue(JsonValue value)
   *********************************************************************************/
  public static void dumpJsonValue(JsonValue value) {
    if (value instanceof JsonNumber) {
      dumpJsonNumber((JsonNumber) value);
    } else if (value instanceof JsonString) {
      dumpJsonString((JsonString) value);
    } else if (value instanceof JsonArray) {
      dumpJsonArray((JsonArray) value);
    } else if (value instanceof JsonObject) {
      dumpJsonObject((JsonObject) value);
    } else
      dumpJsonConstant(value);
  }

  /*********************************************************************************
   * String toStringJsonString(JsonString value)
   *********************************************************************************/
  public static String toStringJsonString(JsonString value) {
    if (value == null)
      return ("JsonString is null");
    return ("\"" + value.getString() + "\"");
  }

  /*********************************************************************************
   * String toStringJsonArray(JsonArray value)
   *********************************************************************************/
  public static String toStringJsonArray(JsonArray value) {
    if (value == null)
      return ("JsonArray is null");
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    Iterator<JsonValue> iter = value.iterator();
    String comma = "";
    while (iter.hasNext()) {
      sb.append(comma + toStringJsonValue(iter.next()));
      if (comma.equals(""))
        comma = ",";
    }
    sb.append("]");
    return (sb.toString());
  }

  /*********************************************************************************
   * String toStringJsonObject(JsonObject value)
   *********************************************************************************/
  public static String toStringJsonObject(JsonObject value) {
    if (value == null)
      return ("JsonObject is null");
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    String comma = "";
    for (Map.Entry<String, JsonValue> entry : value.entrySet()) {
      sb.append(comma + "\"" + entry.getKey() + "\":"
          + toStringJsonValue(entry.getValue()));
      if (comma.equals(""))
        comma = ",";
    }
    sb.append("}");
    return (sb.toString());
  }

  /*********************************************************************************
   * String toStringJsonConstant(JsonValue value)
   *********************************************************************************/
  public static String toStringJsonConstant(JsonValue value) {
    if (value == null)
      return ("JsonValue is null");
    if (value == JsonValue.FALSE)
      return "false";
    else if (value == JsonValue.TRUE)
      return "true";
    else if (value == JsonValue.NULL)
      return "null";
    else
      return "UNKNOWN";
  }

  /*********************************************************************************
   * String toStringJsonNumber(JsonNumber value)
   *********************************************************************************/
  public static String toStringJsonNumber(JsonNumber value) {
    if (value == null)
      return ("JsonNumber is null");
    if (value.isIntegral() == INTEGRAL)
      return ("" + value.longValue());
    else
      return ("" + value.bigDecimalValue());
  }

  /*********************************************************************************
   * String toStringJsonValue(JsonValue value)
   *********************************************************************************/
  public static String toStringJsonValue(JsonValue value) {
    if (value instanceof JsonNumber) {
      return toStringJsonNumber((JsonNumber) value);
    } else if (value instanceof JsonString) {
      return toStringJsonString((JsonString) value);
    } else if (value instanceof JsonArray) {
      return toStringJsonArray((JsonArray) value);
    } else if (value instanceof JsonObject) {
      return toStringJsonObject((JsonObject) value);
    } else
      return toStringJsonConstant(value);
  }

  /*********************************************************************************
   * void dumpSet(Set<String> set, String msg)
   *********************************************************************************/
  public static void dumpSet(Set<String> set, String msg) {
    TestUtil.logMsg("*** Beg: Dumping List contents ***");
    if (msg != null)
      TestUtil.logMsg("*** Message: " + msg);
    Iterator iterator = set.iterator();
    TestUtil.logMsg("Set: (");
    while (iterator.hasNext()) {
      TestUtil.logMsg((String) iterator.next());
    }
    TestUtil.logMsg(")");
    TestUtil.logMsg("*** End: Dumping Set contents ***");
  }

  /*********************************************************************************
   * void dumpSet(Set<String> set)
   *********************************************************************************/
  public static void dumpSet(Set<String> set) {
    dumpSet(set, null);
  }

  /*********************************************************************************
   * String toStringSet(Set<String> set)
   *********************************************************************************/
  public static String toStringSet(Set<String> set) {
    Iterator<String> iter = set.iterator();
    StringBuilder sb = new StringBuilder();
    sb.append("Set: (");
    while (iter.hasNext()) {
      sb.append(iter.next());
      if (iter.hasNext())
        sb.append(",");
    }
    sb.append(")");
    return sb.toString();
  }

  /*********************************************************************************
   * boolean assertEqualsSet(Set<String>expSet, Set<String>actSet)
   *********************************************************************************/
  public static boolean assertEqualsSet(Set<String> expSet,
      Set<String> actSet) {
    if (actSet.equals(expSet)) {
      TestUtil.logMsg("Sets are equal - match (Success)");
      TestUtil.logMsg("Expected: " + toStringSet(expSet));
      TestUtil.logMsg("Actual:   " + toStringSet(actSet));
      return true;
    } else {
      TestUtil.logMsg("Sets are not equal - mismatch (Failure)");
      TestUtil.logErr("Expected: " + toStringSet(expSet));
      TestUtil.logErr("Actual:   " + toStringSet(actSet));
      return false;
    }
  }

  /*********************************************************************************
   * void dumpMap(Map<String,JsonValue> map, String msg)
   *********************************************************************************/
  public static void dumpMap(Map<String, JsonValue> map, String msg) {
    TestUtil.logMsg("*** Beg: Dumping Map contents ***");
    if (msg != null)
      TestUtil.logMsg("*** Message: " + msg);
    TestUtil.logMsg("Map: {");
    for (Map.Entry<String, JsonValue> entry : map.entrySet()) {
      TestUtil.logMsg(
          "\"" + entry.getKey() + "\":" + toStringJsonValue(entry.getValue()));
    }
    TestUtil.logMsg("}");
    TestUtil.logMsg("*** End: Dumping Map contents ***");
  }

  /*********************************************************************************
   * void dumpMap(Map<String,JsonValue> map)
   *********************************************************************************/
  public static void dumpMap(Map<String, JsonValue> map) {
    dumpMap(map, null);
  }

  /*********************************************************************************
   * String toStringMap(Map<String,JsonValue> map)
   *********************************************************************************/
  public static String toStringMap(Map<String, JsonValue> map) {
    StringBuilder sb = new StringBuilder();
    sb.append("Map: {");
    String comma = "";
    for (Map.Entry<String, JsonValue> entry : map.entrySet()) {
      sb.append(comma + "\"" + entry.getKey() + "\":"
          + toStringJsonValue(entry.getValue()));
      if (comma.equals(""))
        comma = ",";
    }
    sb.append("}");
    return sb.toString();
  }

  /*********************************************************************************
   * boolean assertEqualsMap(Map<String,JsonValue>expMap,
   * Map<String,JsonValue>actMap)
   *********************************************************************************/
  public static boolean assertEqualsMap(Map<String, JsonValue> expMap,
      Map<String, JsonValue> actMap) {
    if (actMap.equals(expMap)) {
      TestUtil.logMsg("Maps are equal - match (Success)");
      TestUtil.logMsg("Expected: " + toStringMap(expMap));
      TestUtil.logMsg("Actual:   " + toStringMap(actMap));
      return true;
    } else {
      TestUtil.logMsg("Maps are not equal - mismatch (Failure)");
      TestUtil.logErr("Expected: " + toStringMap(expMap));
      TestUtil.logErr("Actual:   " + toStringMap(actMap));
      return false;
    }
  }

  /*********************************************************************************
   * assertEqualsMap2
   *********************************************************************************/
  public static boolean assertEqualsMap2(Map<String, JsonValue> expMap,
      Map<String, JsonValue> actMap) {
    TestUtil.logMsg("*** Comparing Map expMap and Map actMap for equality ***");
    TestUtil.logMsg("Expected: " + toStringMap(expMap));
    TestUtil.logMsg("Actual:   " + toStringMap(actMap));
    TestUtil.logMsg("Map expMap size should equal Map actMap size");
    if (expMap.size() != actMap.size()) {
      TestUtil.logErr("Map sizes are not equal: expMap size " + expMap.size()
          + ", actMap size " + actMap.size());
      return false;
    } else {
      TestUtil.logMsg("Map sizes are equal with size of " + expMap.size());
    }
    for (Map.Entry<String, ?> entry : expMap.entrySet()) {
      String key = entry.getKey();
      if (actMap.containsKey(key)) {
        if (expMap.get(key) != null && actMap.get(key) != null) {
          if (!expMap.get(key).equals(actMap.get(key))) {
            TestUtil.logErr("key=" + key + ", expMap value " + expMap.get(key)
                + " does not equal actMap value " + actMap.get(key));
            return false;
          }
        }
      } else {
        TestUtil.logErr("actMap does not contain key " + key);
        return false;
      }
    }
    TestUtil.logMsg("Maps expMap and actMap are equal.");
    return true;
  }

  /*********************************************************************************
   * void dumpList(List<JsonValue> list, String msg)
   *********************************************************************************/
  public static void dumpList(List<JsonValue> list, String msg) {
    TestUtil.logMsg("*** Beg: Dumping List contents ***");
    if (msg != null)
      TestUtil.logMsg("*** Message: " + msg);
    Iterator<JsonValue> iter = list.iterator();
    TestUtil.logMsg("List: [");
    while (iter.hasNext()) {
      TestUtil.logMsg("" + toStringJsonValue(iter.next()));
    }
    TestUtil.logMsg("]");
    TestUtil.logMsg("*** End: Dumping List contents ***");
  }

  /*********************************************************************************
   * void dumpList(List<JsonValue> list)
   *********************************************************************************/
  public static void dumpList(List<JsonValue> list) {
    dumpList(list, null);
  }

  /*********************************************************************************
   * String toStringList(List<JsonValue> list)
   *********************************************************************************/
  public static String toStringList(List<JsonValue> list) {
    Iterator<JsonValue> iter = list.iterator();
    StringBuilder sb = new StringBuilder();
    sb.append("List: [");
    String comma = "";
    while (iter.hasNext()) {
      sb.append(comma + toStringJsonValue(iter.next()));
      if (comma.equals(""))
        comma = ",";
    }
    sb.append("]");
    return sb.toString();
  }

  /*********************************************************************************
   * boolean assertEqualsList(List<JsonValue>expList, List<JsonValue>actList)
   *********************************************************************************/
  public static boolean assertEqualsList(List<JsonValue> expList,
      List<JsonValue> actList) {
    if (actList.equals(expList)) {
      TestUtil.logMsg("Lists are equal - match (Success)");
      TestUtil.logMsg("Expected: " + toStringList(expList));
      TestUtil.logMsg("Actual:   " + toStringList(actList));
      return true;
    } else {
      TestUtil.logMsg("Lists are not equal - mismatch (Failure)");
      TestUtil.logErr("Expected: " + toStringList(expList));
      TestUtil.logErr("Actual:   " + toStringList(actList));
      return false;
    }
  }

  /*********************************************************************************
   * assertEqualsList2
   *********************************************************************************/
  public static boolean assertEqualsList2(List<JsonValue> expList,
      List<JsonValue> actList) {
    TestUtil.logMsg(
        "*** Comparing contents of List expList and List actList for equality ***");
    TestUtil.logMsg("Expected: " + toStringList(expList));
    TestUtil.logMsg("Actual:   " + toStringList(actList));
    TestUtil.logMsg("List expList size should equal List actList size");
    if (expList.size() != actList.size()) {
      TestUtil.logErr("List sizes are not equal: expList size " + expList.size()
          + ", actList size " + actList.size());
      return false;
    }
    TestUtil.logMsg("Compare Lists (all elements should MATCH)");
    for (int i = 0; i < expList.size(); i++) {
      if (expList.get(i).equals(actList.get(i))) {
        TestUtil
            .logMsg("expList element " + i + " matches actList element " + i);
      } else {
        TestUtil.logErr(
            "expList element " + i + " does not match actList element " + i);
        TestUtil.logErr("expList[" + i + "]=" + expList.get(i));
        TestUtil.logErr("actList[" + i + "]=" + actList.get(i));
        return false;
      }
    }
    TestUtil.logMsg("Lists are equal (Success)");
    return true;
  }

  /*********************************************************************************
   * void dumpIterator(Iterator<JsonValue> iterator, String msg)
   *********************************************************************************/
  public static void dumpIterator(Iterator<JsonValue> iterator, String msg) {
    TestUtil.logMsg("*** Beg: Dumping Iterator contents ***");
    if (msg != null)
      TestUtil.logMsg("*** Message: " + msg);
    TestUtil.logMsg("Iter: [");
    while (iterator.hasNext()) {
      TestUtil.logMsg("" + toStringJsonValue(iterator.next()));
    }
    TestUtil.logMsg("]");
    TestUtil.logMsg("*** End: Dumping Iterator contents ***");
  }

  /*********************************************************************************
   * void dumpIterator(Iterator<JsonValue> iterator)
   *********************************************************************************/
  public static void dumpIterator(Iterator<JsonValue> iterator) {
    dumpIterator(iterator, null);
  }

  /*********************************************************************************
   * String toStringIterator(Iterator<JsonValue> iterator)
   *********************************************************************************/
  public static String toStringIterator(Iterator<JsonValue> iter) {
    StringBuilder sb = new StringBuilder();
    sb.append("Iterator: [");
    while (iter.hasNext()) {
      sb.append(toStringJsonValue(iter.next()));
      if (iter.hasNext())
        sb.append(",");
    }
    sb.append("]");
    return sb.toString();
  }

  /*********************************************************************************
   * boolean assertEqualsIterator(Iterator<JsonValue>expIt,
   * Iterator<JsonValue>actIt)
   *********************************************************************************/
  public static boolean assertEqualsIterator(Iterator<JsonValue> expIt,
      Iterator<JsonValue> actIt) {
    boolean pass = true;

    TestUtil.logMsg(
        "*** Comparing contents of Iterator expIt and Iterator actIt for equality ***");
    int i = 0;
    while (expIt.hasNext()) {
      if (!actIt.hasNext()) {
        TestUtil.logErr(
            "Iterator expIt contains more elements than Iterator actIt");
        return false;
      }
      ++i;
      JsonValue value1 = expIt.next();
      JsonValue value2 = actIt.next();
      if (assertEqualsJsonValues(value1, value2)) {
        TestUtil.logMsg("Iterator expIt element  " + i
            + " matches Iterator actIt element " + i);
      } else {
        TestUtil.logErr("Iterator expIt element " + i
            + " does not match Iterator actIt element " + i);
        pass = false;
      }
    }
    if (actIt.hasNext()) {
      TestUtil
          .logErr("Iterator actIt contains more elements than Iterator expIt");
      return false;
    }
    if (pass)
      TestUtil.logMsg("Iterators are equal (Success)");
    else
      TestUtil.logMsg("Iterators are not equal (Failure)");
    return pass;
  }

  /*********************************************************************************
   * boolean assertEqualsEmptyArrayList(List<JsonValue> actual)
   *********************************************************************************/
  public static boolean assertEqualsEmptyArrayList(List<JsonValue> actual) {
    if (actual.isEmpty()) {
      TestUtil.logMsg("Array List is empty - expected");
      return true;
    } else {
      TestUtil.logErr("Array List is not empty - unexpected");
      return false;
    }
  }

  /*********************************************************************************
   * boolean assertEqualsEmptyObjectMap(Map<String, JsonValue> actual)
   *********************************************************************************/
  public static boolean assertEqualsEmptyObjectMap(
      Map<String, JsonValue> actual) {
    if (actual.isEmpty()) {
      TestUtil.logMsg("Object Map is empty - expected");
      return true;
    } else {
      TestUtil.logErr("Object Map is not empty - unexpected");
      return false;
    }
  }

  /*********************************************************************************
   * boolean assertEqualsEmptyIterator(Map<String, JsonValue> actual)
   *********************************************************************************/
  public static boolean assertEqualsEmptyIterator(Iterator<JsonValue> actual) {
    if (!actual.hasNext()) {
      TestUtil.logMsg("Iterator is empty - expected");
      return true;
    } else {
      TestUtil.logErr("Iterator is not empty - unexpected");
      return false;
    }
  }

  /*********************************************************************************
   * boolean assertEqualsJsonText(String expected, String actual)
   *********************************************************************************/
  public static boolean assertEqualsJsonText(String expected, String actual) {
    if (actual.equals(expected)) {
      TestUtil.logMsg("JSON text match");
      TestUtil.logMsg("Expected: " + expected);
      TestUtil.logMsg("Actual:   " + actual);
      return true;
    } else {
      TestUtil.logErr("JSON text mismatch");
      TestUtil.logErr("Expected: " + expected);
      TestUtil.logErr("Actual:   " + actual);
      return false;
    }
  }

  /*********************************************************************************
   * boolean assertEqualsJsonArrays(JsonArray expected, JsonArray actual)
   *********************************************************************************/
  public static boolean assertEqualsJsonArrays(JsonArray expected,
      JsonArray actual) {
    if (actual.equals(expected)) {
      TestUtil.logMsg("JsonArray match");
      TestUtil.logMsg("Expected: " + toStringJsonArray(expected));
      TestUtil.logMsg("Actual:   " + toStringJsonArray(actual));
      return true;
    } else {
      TestUtil.logErr("JsonArray mismatch");
      TestUtil.logErr("Expected: " + toStringJsonArray(expected));
      TestUtil.logErr("Actual:   " + toStringJsonArray(actual));
      return false;
    }
  }

  /*********************************************************************************
   * boolean assertEqualsJsonObjects(JsonObject expected, JsonObject actual)
   *********************************************************************************/
  public static boolean assertEqualsJsonObjects(JsonObject expected,
      JsonObject actual) {
    if (actual.equals(expected)) {
      TestUtil.logMsg("JsonObject match");
      TestUtil.logMsg("Expected: " + toStringJsonObject(expected));
      TestUtil.logMsg("Actual:   " + toStringJsonObject(actual));
      return true;
    } else {
      TestUtil.logErr("JsonObject mismatch");
      TestUtil.logErr("Expected: " + toStringJsonObject(expected));
      TestUtil.logErr("Actual:   " + toStringJsonObject(actual));
      return false;
    }
  }

  /*********************************************************************************
   * boolean assertEqualsJsonNumbers(JsonNumber expected, JsonNumber actual)
   *********************************************************************************/
  public static boolean assertEqualsJsonNumbers(JsonNumber expected,
      JsonNumber actual) {
    boolean pass = true;

    if (actual.equals(expected)) {
      TestUtil.logMsg("JsonNumber match");
      TestUtil.logMsg("Expected: " + toStringJsonNumber(expected));
      TestUtil.logMsg("Actual:   " + toStringJsonNumber(actual));
      return true;
    } else {
      TestUtil.logErr("JsonNumber mismatch");
      TestUtil.logErr("Expected: " + toStringJsonNumber(expected));
      TestUtil.logErr("Actual:   " + toStringJsonNumber(actual));
      return false;
    }
  }

  /*********************************************************************************
   * boolean assertEqualsJsonStrings(JsonString expected, JsonString actual)
   *********************************************************************************/
  public static boolean assertEqualsJsonStrings(JsonString expected,
      JsonString actual) {
    boolean pass = true;

    if (actual.equals(expected)) {
      TestUtil.logMsg("JsonString match");
      TestUtil.logMsg("Expected: " + toStringJsonString(expected));
      TestUtil.logMsg("Actual:   " + toStringJsonString(actual));
      return true;
    } else {
      TestUtil.logErr("JsonString mismatch");
      TestUtil.logErr("Expected: " + toStringJsonString(expected));
      TestUtil.logErr("Actual:   " + toStringJsonString(actual));
      return false;
    }
  }

  /*********************************************************************************
   * boolean assertEqualsJsonValues(JsonValue expected, JsonValue actual)
   *********************************************************************************/
  public static boolean assertEqualsJsonValues(JsonValue expected,
      JsonValue actual) {
    boolean pass = true;

    // Comparing JsonNumbers
    if (expected instanceof JsonNumber) {
      if (!(actual instanceof JsonNumber)) {
        TestUtil.logErr("expected type does not match actual type");
        TestUtil.logErr("expected=" + toStringJsonValue(expected));
        TestUtil.logErr("actual=  " + toStringJsonValue(actual));
        pass = false;
      } else {
        pass = assertEqualsJsonNumbers((JsonNumber) expected,
            (JsonNumber) actual);
      }
      // Comparing JsonStrings
    } else if (expected instanceof JsonString) {
      if (!(actual instanceof JsonString)) {
        TestUtil.logErr("expected type does not match actual type");
        TestUtil.logErr("expected=" + toStringJsonValue(expected));
        TestUtil.logErr("actual=  " + toStringJsonValue(actual));
        pass = false;
      } else {
        pass = assertEqualsJsonStrings((JsonString) expected,
            (JsonString) actual);
      }
      // Comparing JsonArrays
    } else if (expected instanceof JsonArray) {
      if (!(actual instanceof JsonArray)) {
        TestUtil.logErr("expected type does not match actual type");
        TestUtil.logErr("expected=" + toStringJsonValue(expected));
        TestUtil.logErr("actual=  " + toStringJsonValue(actual));
        pass = false;
      } else {
        pass = assertEqualsJsonArrays((JsonArray) expected, (JsonArray) actual);
      }
      // Comparing JsonObjects
    } else if (expected instanceof JsonObject) {
      if (!(actual instanceof JsonObject)) {
        TestUtil.logErr("expected type does not match actual type");
        TestUtil.logErr("expected=" + toStringJsonValue(expected));
        TestUtil.logErr("actual=  " + toStringJsonValue(actual));
        pass = false;
      } else {
        pass = assertEqualsJsonObjects((JsonObject) expected,
            (JsonObject) actual);
      }
      // Comparing JsonValues
    } else if (expected.equals(actual)) {
      TestUtil.logMsg("expected matches actual");
      TestUtil.logMsg("expected=" + toStringJsonValue(expected));
      TestUtil.logMsg("actual=  " + toStringJsonValue(actual));
    } else {
      TestUtil.logErr("expected does not match actual");
      TestUtil.logErr("expected=" + toStringJsonValue(expected));
      TestUtil.logErr("actual=  " + toStringJsonValue(actual));
      pass = false;
    }
    return pass;
  }

  /*********************************************************************************
   * boolean assertEqualsJsonValueType(JsonValue.ValueType
   * expected,JsonValue.ValueType actual)
   *********************************************************************************/
  public static boolean assertEqualsJsonValueType(JsonValue.ValueType expected,
      JsonValue.ValueType actual) {
    if (actual == expected) {
      TestUtil.logMsg("JsonValue.ValueType match");
      TestUtil.logMsg("Expected: " + expected);
      TestUtil.logMsg("Actual:   " + actual);
      return true;
    } else {
      TestUtil.logErr("JsonValue.ValueType mismatch");
      TestUtil.logErr("Expected: " + expected);
      TestUtil.logErr("Actual:   " + actual);
      return false;
    }
  }

  /*********************************************************************************
   * boolean assertEqualsJsonNumberType(boolean expected,boolean actual)
   *********************************************************************************/
  public static boolean assertEqualsJsonNumberType(boolean expected,
      boolean actual) {
    if (actual == expected) {
      TestUtil.logMsg("Json NumberType match");
      TestUtil.logMsg("Expected: " + toStringJsonNumberType(expected));
      TestUtil.logMsg("Actual:   " + toStringJsonNumberType(actual));
      return true;
    } else {
      TestUtil.logErr("Json NumberType mismatch");
      TestUtil.logErr("Expected: " + toStringJsonNumberType(expected));
      TestUtil.logErr("Actual:   " + toStringJsonNumberType(actual));
      return false;
    }
  }

  /*********************************************************************************
   * boolean assertEqualsJsonNumberTypes(boolean expected[],boolean actual)
   *********************************************************************************/
  public static boolean assertEqualsJsonNumberTypes(boolean expected[],
      boolean actual) {
    for (int i = 0; i < expected.length; i++) {
      if (actual == expected[i]) {
        TestUtil.logMsg("Json NumberType match");
        TestUtil.logMsg("Expected: " + toStringJsonNumberType(expected[i]));
        TestUtil.logMsg("Actual:   " + toStringJsonNumberType(actual));
        return true;
      }
    }
    TestUtil.logErr("Json NumberType mismatch");
    TestUtil.logErr("Expected: " + toStringJsonNumberTypes(expected));
    TestUtil.logErr("Actual:   " + toStringJsonNumberType(actual));
    return false;
  }

  /*********************************************************************************
   * String toStringJsonNumberType(boolean numberType)
   *********************************************************************************/
  public static String toStringJsonNumberType(boolean numberType) {
    if (numberType == INTEGRAL)
      return "INTEGRAL";
    else
      return "NON_INTEGRAL";
  }

  /*********************************************************************************
   * String toStringJsonNumberTypes(boolean expected[])
   *********************************************************************************/
  public static String toStringJsonNumberTypes(boolean expected[]) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < expected.length; i++) {
      sb.append("" + toStringJsonNumberType(expected[i]));
      if (i + 1 < expected.length)
        sb.append("|");
    }
    return sb.toString();
  }

  /*********************************************************************************
   * boolean assertEquals(Object, Object)
   *********************************************************************************/
  public static boolean assertEquals(Object expected, Object actual) {
    if (actual.equals(expected)) {
      TestUtil.logMsg("Object match");
      TestUtil.logMsg("Expected: " + expected);
      TestUtil.logMsg("Actual:   " + actual);
      return true;
    } else {
      TestUtil.logErr("Object mismatch");
      TestUtil.logErr("Expected: " + expected);
      TestUtil.logErr("Actual:   " + actual);
      return false;
    }
  }

  /*********************************************************************************
   * boolean assertEquals(boolean, boolean)
   *********************************************************************************/
  public static boolean assertEquals(boolean expected, boolean actual) {
    if (actual == expected) {
      TestUtil.logMsg("boolean match");
      TestUtil.logMsg("Expected: " + expected);
      TestUtil.logMsg("Actual:   " + actual);
      return true;
    } else {
      TestUtil.logErr("boolean mismatch");
      TestUtil.logErr("Expected: " + expected);
      TestUtil.logErr("Actual:   " + actual);
      return false;
    }
  }

  /*********************************************************************************
   * boolean assertEquals(short, short)
   *********************************************************************************/
  public static boolean assertEquals(short expected, short actual) {
    if (actual == expected) {
      TestUtil.logMsg("short match");
      TestUtil.logMsg("Expected: " + expected);
      TestUtil.logMsg("Actual:   " + actual);
      return true;
    } else {
      TestUtil.logErr("short mismatch");
      TestUtil.logErr("Expected: " + expected);
      TestUtil.logErr("Actual:   " + actual);
      return false;
    }
  }

  /*********************************************************************************
   * boolean assertEquals(int, int)
   *********************************************************************************/
  public static boolean assertEquals(int expected, int actual) {
    if (actual == expected) {
      TestUtil.logMsg("int match");
      TestUtil.logMsg("Expected: " + expected);
      TestUtil.logMsg("Actual:   " + actual);
      return true;
    } else {
      TestUtil.logErr("int mismatch");
      TestUtil.logErr("Expected: " + expected);
      TestUtil.logErr("Actual:   " + actual);
      return false;
    }
  }

  /*********************************************************************************
   * boolean assertEquals(long, long)
   *********************************************************************************/
  public static boolean assertEquals(long expected, long actual) {
    if (actual == expected) {
      TestUtil.logMsg("long match");
      TestUtil.logMsg("Expected: " + expected);
      TestUtil.logMsg("Actual:   " + actual);
      return true;
    } else {
      TestUtil.logErr("long mismatch");
      TestUtil.logErr("Expected: " + expected);
      TestUtil.logErr("Actual:   " + actual);
      return false;
    }
  }

  /*********************************************************************************
   * boolean assertEquals(float, float)
   *********************************************************************************/
  public static boolean assertEquals(float expected, float actual) {
    if (actual == expected) {
      TestUtil.logMsg("float match");
      TestUtil.logMsg("Expected: " + expected);
      TestUtil.logMsg("Actual:   " + actual);
      return true;
    } else {
      TestUtil.logErr("float mismatch");
      TestUtil.logErr("Expected: " + expected);
      TestUtil.logErr("Actual:   " + actual);
      return false;
    }
  }

  /*********************************************************************************
   * boolean assertEquals(double, double)
   *********************************************************************************/
  public static boolean assertEquals(double expected, double actual) {
    if (actual == expected) {
      TestUtil.logMsg("double match");
      TestUtil.logMsg("Expected: " + expected);
      TestUtil.logMsg("Actual:   " + actual);
      return true;
    } else {
      TestUtil.logErr("double mismatch");
      TestUtil.logErr("Expected: " + expected);
      TestUtil.logErr("Actual:   " + actual);
      return false;
    }
  }

  /*********************************************************************************
   * boolean assertEquals(BigDecimal, BigDecimal)
   *********************************************************************************/
  public static boolean assertEquals(BigDecimal expected, BigDecimal actual) {
    if (actual.equals(expected)) {
      TestUtil.logMsg("BigDecimal match");
      TestUtil.logMsg("Expected: " + expected);
      TestUtil.logMsg("Actual:   " + actual);
      return true;
    } else {
      TestUtil.logErr("BigDecimal mismatch");
      TestUtil.logErr("Expected: " + expected);
      TestUtil.logErr("Actual:   " + actual);
      return false;
    }
  }

  /*********************************************************************************
   * boolean assertEquals(BigInteger, BigInteger)
   *********************************************************************************/
  public static boolean assertEquals(BigInteger expected, BigInteger actual) {
    if (actual.equals(expected)) {
      TestUtil.logMsg("BigInteger match");
      TestUtil.logMsg("Expected: " + expected);
      TestUtil.logMsg("Actual:   " + actual);
      return true;
    } else {
      TestUtil.logErr("BigInteger mismatch");
      TestUtil.logErr("Expected: " + expected);
      TestUtil.logErr("Actual:   " + actual);
      return false;
    }
  }

  /*********************************************************************************
   * boolean assertEquals(String, String)
   *********************************************************************************/
  public static boolean assertEquals(String expected, String actual) {
    if (actual.equals(expected)) {
      TestUtil.logMsg("String match");
      TestUtil.logMsg("Expected: " + expected);
      TestUtil.logMsg("Actual:   " + actual);
      return true;
    } else {
      TestUtil.logErr("String mismatch");
      TestUtil.logErr("Expected: " + expected);
      TestUtil.logErr("Actual:   " + actual);
      return false;
    }
  }

  /*********************************************************************************
   * boolean assertEquals(JsonValue, JsonValue)
   *********************************************************************************/
  public static boolean assertEquals(JsonValue expected, JsonValue actual) {
    if (actual.equals(expected)) {
      TestUtil.logMsg("JsonValue match");
      TestUtil.logMsg("Expected: " + toStringJsonValue(expected));
      TestUtil.logMsg("Actual:   " + toStringJsonValue(actual));
      return true;
    } else {
      TestUtil.logErr("JsonValue mismatch");
      TestUtil.logErr("Expected: " + toStringJsonValue(expected));
      TestUtil.logErr("Actual:   " + toStringJsonValue(actual));
      return false;
    }
  }

  /*********************************************************************************
   * String getNumberTypeString(boolean numberType)
   *********************************************************************************/
  public static String getNumberTypeString(boolean numberType) {
    if (numberType == INTEGRAL)
      return "INTEGRAL";
    else
      return "NON_INTEGRAL";
  }

  /*********************************************************************************
   * boolean getNumberType(String numberType)
   *********************************************************************************/
  public static boolean getNumberType(String numberType) {
    if (numberType.equals("INTEGRAL"))
      return INTEGRAL;
    else
      return NON_INTEGRAL;
  }

  /*********************************************************************************
   * String getValueTypeString(JsonValue.ValueType valueType)
   *********************************************************************************/
  public static String getValueTypeString(JsonValue.ValueType valueType) {
    switch (valueType) {
    case ARRAY:
      return "ARRAY";
    case FALSE:
      return "FALSE";
    case NULL:
      return "NULL";
    case NUMBER:
      return "NUMBER";
    case OBJECT:
      return "OBJECT";
    case STRING:
      return "STRING";
    case TRUE:
      return "TRUE";
    default:
      return null;
    }
  }

  /*********************************************************************************
   * JsonValue.ValueType getValueType(String valueType)
   *********************************************************************************/
  public static JsonValue.ValueType getValueType(String valueType) {
    if (valueType.equals("ARRAY"))
      return JsonValue.ValueType.ARRAY;
    if (valueType.equals("FALSE"))
      return JsonValue.ValueType.FALSE;
    if (valueType.equals("NULL"))
      return JsonValue.ValueType.NULL;
    if (valueType.equals("NUMBER"))
      return JsonValue.ValueType.NUMBER;
    if (valueType.equals("OBJECT"))
      return JsonValue.ValueType.OBJECT;
    if (valueType.equals("STRING"))
      return JsonValue.ValueType.STRING;
    if (valueType.equals("TRUE"))
      return JsonValue.ValueType.TRUE;
    else
      return null;
  }

  /*********************************************************************************
   * void dumpEventType(JsonParser.Event eventType)
   *********************************************************************************/
  public static void dumpEventType(JsonParser.Event eventType) {
    TestUtil.logMsg("JsonParser.Event=" + eventType);
  }

  /*********************************************************************************
   * getEventTypeString(JsonParser.Event eventType)
   *********************************************************************************/
  public static String getEventTypeString(JsonParser.Event eventType) {
    switch (eventType) {
    case START_ARRAY:
      return "START_ARRAY";
    case START_OBJECT:
      return "START_OBJECT";
    case KEY_NAME:
      return "KEY_NAME";
    case VALUE_STRING:
      return "VALUE_STRING";
    case VALUE_NUMBER:
      return "VALUE_NUMBER";
    case VALUE_TRUE:
      return "VALUE_TRUE";
    case VALUE_FALSE:
      return "VALUE_FALSE";
    case VALUE_NULL:
      return "VALUE_NULL";
    case END_OBJECT:
      return "END_OBJECT";
    case END_ARRAY:
      return "END_ARRAY";
    default:
      return null;
    }
  }

  /*********************************************************************************
   * JsonParser.Event getEventType(String eventType)
   *********************************************************************************/
  public static JsonParser.Event getEventType(String eventType) {
    if (eventType.equals("START_ARRAY"))
      return JsonParser.Event.START_ARRAY;
    if (eventType.equals("START_OBJECT"))
      return JsonParser.Event.START_OBJECT;
    if (eventType.equals("KEY_NAME"))
      return JsonParser.Event.KEY_NAME;
    if (eventType.equals("VALUE_STRING"))
      return JsonParser.Event.VALUE_STRING;
    if (eventType.equals("VALUE_NUMBER"))
      return JsonParser.Event.VALUE_NUMBER;
    if (eventType.equals("VALUE_TRUE"))
      return JsonParser.Event.VALUE_TRUE;
    if (eventType.equals("VALUE_FALSE"))
      return JsonParser.Event.VALUE_FALSE;
    if (eventType.equals("VALUE_NULL"))
      return JsonParser.Event.VALUE_NULL;
    if (eventType.equals("END_OBJECT"))
      return JsonParser.Event.END_OBJECT;
    if (eventType.equals("END_ARRAY"))
      return JsonParser.Event.END_ARRAY;
    else
      return null;
  }

  /*********************************************************************************
   * String getConfigName(String configValue)
   *********************************************************************************/
  public static String getConfigName(String configValue) {
    if (configValue.equals(JsonGenerator.PRETTY_PRINTING))
      return "JsonGenerator.PRETTY_PRINTING";
    else if (configValue.equals(JSONP_Util.FOO_CONFIG))
      return "JSONP_Util.FOO_CONFIG";
    else
      return null;
  }

  /*********************************************************************************
   * String getConfigValue(String configProp)
   *********************************************************************************/
  public static String getConfigValue(String configProp) {
    if (configProp.equals("JsonGenerator.PRETTY_PRINING"))
      return JsonGenerator.PRETTY_PRINTING;
    else if (configProp.equals("JSONP_Util.FOO_CONFIG"))
      return JSONP_Util.FOO_CONFIG;
    else
      return null;
  }

  /*********************************************************************************
   * void dumpConfigMap(Map<String,?> map, String msg)
   *********************************************************************************/
  public static void dumpConfigMap(Map<String, ?> map, String msg) {
    TestUtil.logMsg("*** Beg: Dumping Config Map contents ***");
    if (msg != null)
      TestUtil.logMsg("*** Message: " + msg);
    for (Map.Entry<String, ?> entry : map.entrySet()) {
      TestUtil.logMsg("\"" + entry.getKey() + "\":" + entry.getValue());
    }
    TestUtil.logMsg("*** End: Dumping Config Map contents ***");
  }

  /*********************************************************************************
   * void dumpConfigMap(Map<String,?> map)
   *********************************************************************************/
  public static void dumpConfigMap(Map<String, ?> map) {
    dumpConfigMap(map, null);
  }

  /*********************************************************************************
   * boolean doConfigCheck(Map<String,?> config, int expectedSize)
   *********************************************************************************/
  public static boolean doConfigCheck(Map<String, ?> config, int expectedSize) {
    return doConfigCheck(config, expectedSize, null);
  }

  public static boolean doConfigCheck(Map<String, ?> config, int expectedSize,
      String[] expectedProps) {
    boolean pass = true;
    dumpConfigMap(config);
    TestUtil.logMsg("Checking factory configuration property size");
    if (config.size() != expectedSize) {
      TestUtil.logErr("Expecting no of properties=" + expectedSize + ", got="
          + config.size());
      pass = false;
    } else {
      TestUtil.logMsg("Expecting no of properties=" + expectedSize + ", got="
          + config.size());
    }
    if (expectedSize != 0 && expectedProps != null) {
      TestUtil.logMsg("Checking factory configuration property name and value");
      for (int i = 0; i < expectedProps.length; i++) {
        if (config.containsKey(expectedProps[i])) {
          TestUtil
              .logMsg("Does contain key: " + expectedProps[i] + " - expected.");
          if (!JSONP_Util.assertEquals(true, config.get(expectedProps[i]))) {
            pass = false;
          }
        } else {
          TestUtil.logErr(
              "Does not contain key: " + expectedProps[i] + " - unexpected.");
          pass = false;
        }
      }
    }
    return pass;
  }

  /*********************************************************************************
   * boolean isEmptyConfig(Map<String, ?> config)
   *********************************************************************************/
  public boolean isEmptyConfig(Map<String, ?> config) {
    TestUtil.logMsg("isEmptyConfig");
    return config.isEmpty();
  }

  /*********************************************************************************
   * Map<String, ?> getEmptyConfig()
   *********************************************************************************/
  public static Map<String, ?> getEmptyConfig() {
    TestUtil.logMsg("getEmptyConfig");
    Map<String, Object> config = new HashMap<String, Object>();
    return config;
  }

  /*********************************************************************************
   * Map<String, ?> getPrettyPrintingConfig()
   *********************************************************************************/
  public static Map<String, ?> getPrettyPrintingConfig() {
    TestUtil.logMsg("getPrettyPrintConfig");
    Map<String, Object> config = new HashMap<String, Object>();
    TestUtil.logMsg("Added property: JsonGenerator.PRETTY_PRINTING");
    config.put(JsonGenerator.PRETTY_PRINTING, true);
    return config;
  }

  /*********************************************************************************
   * Map<String, ?> getFooConfig()
   *********************************************************************************/
  public static Map<String, ?> getFooConfig() {
    TestUtil.logMsg("getFooConfig");
    Map<String, Object> config = new HashMap<String, Object>();
    TestUtil.logMsg("Added property: JSONP_Util.FOO_CONFIG");
    config.put(JSONP_Util.FOO_CONFIG, true);
    return config;
  }

  /*********************************************************************************
   * Map<String, ?> getAllConfig()
   *********************************************************************************/
  public static Map<String, ?> getAllConfig() {
    TestUtil.logMsg("getAllConfig");
    Map<String, Object> config = new HashMap<String, Object>();
    TestUtil.logMsg("Added property: JsonGenerator.PRETTY_PRINTING");
    config.put(JsonGenerator.PRETTY_PRINTING, true);
    TestUtil.logMsg("Added property: JSONP_Util.FOO_CONFIG");
    config.put(JSONP_Util.FOO_CONFIG, true);
    return config;
  }

  /*********************************************************************************
   * JsonObject createJsonObjectFromString(String jsonObjData)
   *********************************************************************************/
  public static JsonObject createJsonObjectFromString(String jsonObjData) {
    JsonReader reader = null;
    JsonObject object = null;
    try {
      reader = Json.createReader(new StringReader(jsonObjData));
      object = reader.readObject();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (reader != null)
        reader.close();
    }
    return object;
  }

  /*********************************************************************************
   * JsonArray createJsonArrayFromString(String jsonArrData)
   *********************************************************************************/
  public static JsonArray createJsonArrayFromString(String jsonArrData) {
    JsonReader reader = null;
    JsonArray array = null;
    try {
      reader = Json.createReader(new StringReader(jsonArrData));
      array = reader.readArray();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (reader != null)
        reader.close();
    }
    return array;
  }

  /*********************************************************************************
   * void writeJsonObjectFromString(JsonWriter writer, String jsonObjData)
   *********************************************************************************/
  public static void writeJsonObjectFromString(JsonWriter writer,
      String jsonObjData) {
    try {
      JsonObject jsonObject = createJsonObjectFromString(jsonObjData);
      writer.writeObject(jsonObject);
      writer.close();
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e);
    }
  }

  /*********************************************************************************
   * void writeJsonArrayFromString(JsonWriter writer, String jsonArrData)
   *********************************************************************************/
  public static void writeJsonArrayFromString(JsonWriter writer,
      String jsonArrData) {
    try {
      JsonArray jsonArray = createJsonArrayFromString(jsonArrData);
      writer.writeArray(jsonArray);
      writer.close();
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e);
    }
  }

  /*********************************************************************************
   * void testKeyStringValue(JsonParser parser, String name, String value)
   *********************************************************************************/
  public static void testKeyStringValue(JsonParser parser, String name,
      String value) {
    if (!checkNextParserEvent(parser))
      return;
    JsonParser.Event e = parser.next();

    if (e != JsonParser.Event.KEY_NAME) {
      TestUtil.logErr(
          "Expected event: " + getEventTypeString(JsonParser.Event.KEY_NAME)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected event: " + getEventTypeString(e));
    }
    String keyname = parser.getString();
    if (!name.equals(keyname)) {
      TestUtil
          .logErr("Expected keyname: " + name + ", got keyname: " + keyname);
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected keyname: " + keyname);
    }

    if (!checkNextParserEvent(parser))
      return;
    e = parser.next();
    if (e != JsonParser.Event.VALUE_STRING) {
      TestUtil.logErr(
          "Expected event: " + getEventTypeString(JsonParser.Event.VALUE_STRING)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    }
    String keyvalue = parser.getString();
    if (!keyvalue.equals(value)) {
      TestUtil.logErr(
          "Expected keyvalue: " + value + ", got keyvalue: " + keyvalue);
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected keyvalue: " + keyvalue);
    }
  }

  /*********************************************************************************
   * void testKeyIntegerValue(JsonParser parser, String name, int value)
   *********************************************************************************/
  public static void testKeyIntegerValue(JsonParser parser, String name,
      int value) {
    if (!checkNextParserEvent(parser))
      return;
    JsonParser.Event e = parser.next();

    if (e != JsonParser.Event.KEY_NAME) {
      TestUtil.logErr(
          "Expected event: " + getEventTypeString(JsonParser.Event.KEY_NAME)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected event: " + getEventTypeString(e));
    }
    String keyname = parser.getString();
    if (!name.equals(keyname)) {
      TestUtil
          .logErr("Expected keyname: " + name + ", got keyname: " + keyname);
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected keyname: " + keyname);
    }

    if (!checkNextParserEvent(parser))
      return;
    e = parser.next();
    if (e != JsonParser.Event.VALUE_NUMBER) {
      TestUtil.logErr(
          "Expected event: " + getEventTypeString(JsonParser.Event.VALUE_NUMBER)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected event: " + getEventTypeString(e));
    }
    int keyvalue = parser.getInt();
    if (keyvalue != value) {
      TestUtil.logErr(
          "Expected keyvalue: " + value + ", got keyvalue: " + keyvalue);
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected keyvalue: " + keyvalue);
    }
  }

  /*********************************************************************************
   * void testKeyDoubleValue(JsonParser parser, String name, double value)
   *********************************************************************************/
  public static void testKeyDoubleValue(JsonParser parser, String name,
      double value) {
    if (!checkNextParserEvent(parser))
      return;
    JsonParser.Event e = parser.next();

    if (e != JsonParser.Event.KEY_NAME) {
      TestUtil.logErr(
          "Expected event: " + getEventTypeString(JsonParser.Event.KEY_NAME)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected event: " + getEventTypeString(e));
    }
    String keyname = parser.getString();
    if (!name.equals(keyname)) {
      TestUtil
          .logErr("Expected keyname: " + name + ", got keyname: " + keyname);
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected keyname: " + keyname);
    }

    if (!checkNextParserEvent(parser))
      return;
    e = parser.next();
    if (e != JsonParser.Event.VALUE_NUMBER) {
      TestUtil.logErr(
          "Expected event: " + getEventTypeString(JsonParser.Event.VALUE_NUMBER)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected event: " + getEventTypeString(e));
    }
    double keyvalue = parser.getBigDecimal().doubleValue();
    if (keyvalue != value) {
      TestUtil.logErr(
          "Expected keyvalue: " + value + ", got keyvalue: " + keyvalue);
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected keyvalue: " + keyvalue);
    }
  }

  /*********************************************************************************
   * void testKeyLongValue(JsonParser parser, String name, long value)
   *********************************************************************************/
  public static void testKeyLongValue(JsonParser parser, String name,
      long value) {
    if (!checkNextParserEvent(parser))
      return;
    JsonParser.Event e = parser.next();

    if (e != JsonParser.Event.KEY_NAME) {
      TestUtil.logErr(
          "Expected event: " + getEventTypeString(JsonParser.Event.KEY_NAME)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected event: " + getEventTypeString(e));
    }
    String keyname = parser.getString();
    if (!name.equals(keyname)) {
      TestUtil
          .logErr("Expected keyname: " + name + ", got keyname: " + keyname);
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected keyname: " + keyname);
    }

    if (!checkNextParserEvent(parser))
      return;
    e = parser.next();
    if (e != JsonParser.Event.VALUE_NUMBER) {
      TestUtil.logErr(
          "Expected event: " + getEventTypeString(JsonParser.Event.VALUE_NUMBER)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected event: " + getEventTypeString(e));
    }
    long keyvalue = parser.getLong();
    if (keyvalue != value) {
      TestUtil.logErr(
          "Expected keyvalue: " + value + ", got keyvalue: " + keyvalue);
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected keyvalue: " + keyvalue);
    }
  }

  /*********************************************************************************
   * void testKeyBigDecimalValue(JsonParser parser, String name, BigDecimal
   * value)
   *********************************************************************************/
  public static void testKeyBigDecimalValue(JsonParser parser, String name,
      BigDecimal value) {
    if (!checkNextParserEvent(parser))
      return;
    JsonParser.Event e = parser.next();

    if (e != JsonParser.Event.KEY_NAME) {
      TestUtil.logErr(
          "Expected event: " + getEventTypeString(JsonParser.Event.KEY_NAME)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected event: " + getEventTypeString(e));
    }
    String keyname = parser.getString();
    if (!name.equals(keyname)) {
      TestUtil
          .logErr("Expected keyname: " + name + ", got keyname: " + keyname);
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected keyname: " + keyname);
    }

    if (!checkNextParserEvent(parser))
      return;
    e = parser.next();
    if (e != JsonParser.Event.VALUE_NUMBER) {
      TestUtil.logErr(
          "Expected event: " + getEventTypeString(JsonParser.Event.VALUE_NUMBER)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected event: " + getEventTypeString(e));
    }
    BigDecimal keyvalue = parser.getBigDecimal();
    if (keyvalue != value) {
      TestUtil.logErr(
          "Expected keyvalue: " + value + ", got keyvalue: " + keyvalue);
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected keyvalue: " + keyvalue);
    }
  }

  /*********************************************************************************
   * void testKeyTrueValue(JsonParser parser, String name)
   *********************************************************************************/
  public static void testKeyTrueValue(JsonParser parser, String name) {
    if (!checkNextParserEvent(parser))
      return;
    JsonParser.Event e = parser.next();

    if (e != JsonParser.Event.KEY_NAME) {
      TestUtil.logErr(
          "Expected event: " + getEventTypeString(JsonParser.Event.KEY_NAME)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected event: " + getEventTypeString(e));
    }
    String keyname = parser.getString();
    if (!name.equals(keyname)) {
      TestUtil
          .logErr("Expected keyname: " + name + ", got keyname: " + keyname);
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected keyname: " + keyname);
    }

    if (!checkNextParserEvent(parser))
      return;
    e = parser.next();
    if (e != JsonParser.Event.VALUE_TRUE) {
      TestUtil.logErr(
          "Expected event: " + getEventTypeString(JsonParser.Event.VALUE_TRUE)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected event: " + getEventTypeString(e));
    }
  }

  /*********************************************************************************
   * void testKeyFalseValue(JsonParser parser, String name)
   *********************************************************************************/
  public static void testKeyFalseValue(JsonParser parser, String name) {
    if (!checkNextParserEvent(parser))
      return;
    JsonParser.Event e = parser.next();

    if (e != JsonParser.Event.KEY_NAME) {
      TestUtil.logErr(
          "Expected event: " + getEventTypeString(JsonParser.Event.KEY_NAME)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected event: " + getEventTypeString(e));
    }
    String keyname = parser.getString();
    if (!name.equals(keyname)) {
      TestUtil
          .logErr("Expected keyname: " + name + ", got keyname: " + keyname);
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected keyname: " + keyname);
    }

    if (!checkNextParserEvent(parser))
      return;
    e = parser.next();
    if (e != JsonParser.Event.VALUE_FALSE) {
      TestUtil.logErr(
          "Expected event: " + getEventTypeString(JsonParser.Event.VALUE_FALSE)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected event: " + getEventTypeString(e));
    }
  }

  /*********************************************************************************
   * void testKeyNullValue(JsonParser parser, String name)
   *********************************************************************************/
  public static void testKeyNullValue(JsonParser parser, String name) {
    if (!checkNextParserEvent(parser))
      return;
    JsonParser.Event e = parser.next();

    if (e != JsonParser.Event.KEY_NAME) {
      TestUtil.logErr(
          "Expected event: " + getEventTypeString(JsonParser.Event.KEY_NAME)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected event: " + getEventTypeString(e));
    }
    String keyname = parser.getString();
    if (!name.equals(keyname)) {
      TestUtil
          .logErr("Expected keyname: " + name + ", got keyname: " + keyname);
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected keyname: " + keyname);
    }

    if (!checkNextParserEvent(parser))
      return;
    e = parser.next();
    if (e != JsonParser.Event.VALUE_NULL) {
      TestUtil.logErr(
          "Expected event: " + getEventTypeString(JsonParser.Event.VALUE_NULL)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected event: " + getEventTypeString(e));
    }
  }

  /*********************************************************************************
   * void testKeyStartObjectValue(JsonParser parser, String name)
   *********************************************************************************/
  public static void testKeyStartObjectValue(JsonParser parser, String name) {
    if (!checkNextParserEvent(parser))
      return;
    JsonParser.Event e = parser.next();

    if (e != JsonParser.Event.KEY_NAME) {
      TestUtil.logErr(
          "Expected event: " + getEventTypeString(JsonParser.Event.KEY_NAME)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected event: " + getEventTypeString(e));
    }
    String keyname = parser.getString();
    if (!name.equals(keyname)) {
      TestUtil
          .logErr("Expected keyname: " + name + ", got keyname: " + keyname);
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected keyname: " + keyname);
    }

    if (!checkNextParserEvent(parser))
      return;
    e = parser.next();
    if (e != JsonParser.Event.START_OBJECT) {
      TestUtil.logErr(
          "Expected event: " + getEventTypeString(JsonParser.Event.START_OBJECT)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected event: " + getEventTypeString(e));
    }
  }

  /*********************************************************************************
   * void testKeyStartArrayValue(JsonParser parser, String name)
   *********************************************************************************/
  public static void testKeyStartArrayValue(JsonParser parser, String name) {
    if (!checkNextParserEvent(parser))
      return;
    JsonParser.Event e = parser.next();

    if (e != JsonParser.Event.KEY_NAME) {
      TestUtil.logErr(
          "Expected event: " + getEventTypeString(JsonParser.Event.KEY_NAME)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected event: " + getEventTypeString(e));
    }
    String keyname = parser.getString();
    if (!name.equals(keyname)) {
      TestUtil
          .logErr("Expected keyname: " + name + ", got keyname: " + keyname);
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected keyname: " + keyname);
    }

    if (!checkNextParserEvent(parser))
      return;
    e = parser.next();
    if (e != JsonParser.Event.START_ARRAY) {
      TestUtil.logErr(
          "Expected event: " + getEventTypeString(JsonParser.Event.START_ARRAY)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected event: " + getEventTypeString(e));
    }
  }

  /*********************************************************************************
   * boolean checkNextParserEvent(JsonParser parser)
   *********************************************************************************/
  public static boolean checkNextParserEvent(JsonParser parser) {
    if (!parser.hasNext()) {
      TestUtil.logErr("no next parser event found - unexpected");
      parseErrs++;
      return false;
    } else
      return true;
  }

  /*********************************************************************************
   * JsonParser.Event getNextParserEvent(JsonParser parser)
   *********************************************************************************/
  public static JsonParser.Event getNextParserEvent(JsonParser parser) {
    if (parser.hasNext())
      return parser.next();
    else
      return null;
  }

  /*********************************************************************************
   * JsonParser.Event getNextSpecificParserEvent(JsonParser parser,
   * JsonParser.Event thisEvent)
   *********************************************************************************/
  public static JsonParser.Event getNextSpecificParserEvent(JsonParser parser,
      JsonParser.Event thisEvent) {
    while (parser.hasNext()) {
      JsonParser.Event event = parser.next();
      if (event == thisEvent)
        return event;
    }
    return null;
  }

  /*********************************************************************************
   * void testEventType(JsonParser parser, JsonParser.Event expEvent)
   *********************************************************************************/
  public static void testEventType(JsonParser parser,
      JsonParser.Event expEvent) {
    if (!checkNextParserEvent(parser))
      return;
    JsonParser.Event e = parser.next();
    if (e != expEvent) {
      TestUtil.logErr("Expected event: " + getEventTypeString(expEvent)
          + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected event: " + getEventTypeString(e));
    }
  }

  /*********************************************************************************
   * void testStringValue(JsonParser parser, String value)
   *********************************************************************************/
  public static void testStringValue(JsonParser parser, String value) {
    if (!checkNextParserEvent(parser))
      return;
    JsonParser.Event e = parser.next();
    if (e != JsonParser.Event.VALUE_STRING) {
      TestUtil.logErr(
          "Expected event: " + getEventTypeString(JsonParser.Event.VALUE_STRING)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected event: " + getEventTypeString(e));
    }
    String keyvalue = parser.getString();
    if (!keyvalue.equals(value)) {
      TestUtil.logErr(
          "Expected keyvalue: " + value + ", got keyvalue: " + keyvalue);
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected keyvalue: " + keyvalue);
    }
  }

  /*********************************************************************************
   * void testIntegerValue(JsonParser parser, int value)
   *********************************************************************************/
  public static void testIntegerValue(JsonParser parser, int value) {
    if (!checkNextParserEvent(parser))
      return;
    JsonParser.Event e = parser.next();
    if (e != JsonParser.Event.VALUE_NUMBER) {
      TestUtil.logErr(
          "Expected event: " + getEventTypeString(JsonParser.Event.VALUE_NUMBER)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected event: " + getEventTypeString(e));
    }
    int keyvalue = parser.getInt();
    if (keyvalue != value) {
      TestUtil.logErr(
          "Expected keyvalue: " + value + ", got keyvalue: " + keyvalue);
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected keyvalue: " + keyvalue);
    }
  }

  /*********************************************************************************
   * void testDoubleValue(JsonParser parser, double value)
   *********************************************************************************/
  public static void testDoubleValue(JsonParser parser, double value) {
    if (!checkNextParserEvent(parser))
      return;
    JsonParser.Event e = parser.next();
    if (e != JsonParser.Event.VALUE_NUMBER) {
      TestUtil.logErr(
          "Expected event: " + getEventTypeString(JsonParser.Event.VALUE_NUMBER)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected event: " + getEventTypeString(e));
    }
    double keyvalue = parser.getBigDecimal().doubleValue();
    if (keyvalue != value) {
      TestUtil.logErr(
          "Expected keyvalue: " + value + ", got keyvalue: " + keyvalue);
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected keyvalue: " + keyvalue);
    }
  }

  /*********************************************************************************
   * void testLongValue(JsonParser parser, long value)
   *********************************************************************************/
  public static void testLongValue(JsonParser parser, long value) {
    if (!checkNextParserEvent(parser))
      return;
    JsonParser.Event e = parser.next();
    if (e != JsonParser.Event.VALUE_NUMBER) {
      TestUtil.logErr(
          "Expected event: " + getEventTypeString(JsonParser.Event.VALUE_NUMBER)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected event: " + getEventTypeString(e));
    }
    long keyvalue = parser.getLong();
    if (keyvalue != value) {
      TestUtil.logErr(
          "Expected keyvalue: " + value + ", got keyvalue: " + keyvalue);
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected keyvalue: " + keyvalue);
    }
  }

  /*********************************************************************************
   * void testBigDecimalValue(JsonParser parser, BigDecimal value)
   *********************************************************************************/
  public static void testBigDecimalValue(JsonParser parser, BigDecimal value) {
    if (!checkNextParserEvent(parser))
      return;
    JsonParser.Event e = parser.next();
    if (e != JsonParser.Event.VALUE_NUMBER) {
      TestUtil.logErr(
          "Expected event: " + getEventTypeString(JsonParser.Event.VALUE_NUMBER)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected event: " + getEventTypeString(e));
    }
    BigDecimal keyvalue = parser.getBigDecimal();
    if (keyvalue != value) {
      TestUtil.logErr(
          "Expected keyvalue: " + value + ", got keyvalue: " + keyvalue);
      parseErrs++;
    } else {
      TestUtil.logMsg("Got expected keyvalue: " + keyvalue);
    }
  }

  /*********************************************************************************
   * void testTrueValue(JsonParser parser, JsonParser.Event expEvent)
   *********************************************************************************/
  public static void testTrueValue(JsonParser parser,
      JsonParser.Event expEvent) {
    testEventType(parser, expEvent);
  }

  /*********************************************************************************
   * void testFalseValue(JsonParser parser, JsonParser.Event expEvent)
   *********************************************************************************/
  public static void testFalseValue(JsonParser parser,
      JsonParser.Event expEvent) {
    testEventType(parser, expEvent);
  }

  /*********************************************************************************
   * void testNullValue(JsonParser parser, JsonParser.Event expEvent)
   *********************************************************************************/
  public static void testNullValue(JsonParser parser,
      JsonParser.Event expEvent) {
    testEventType(parser, expEvent);
  }

  /*********************************************************************************
   * resetParseErrs()
   *********************************************************************************/
  public static void resetParseErrs() {
    parseErrs = 0;
  }

  /*********************************************************************************
   * int getParseErrs()
   *********************************************************************************/
  public static int getParseErrs() {
    return parseErrs;
  }

  /*********************************************************************************
   * String convertUnicodeCharToString(Char c)
   *
   * Convert unicode to string value of form U+NNNN where NNNN are 4 hex digits
   *
   * Use a binary or with hex value '0x10000' when converting unicode char to
   * hex string and remove 1st char to get the 4 hex digits we need.
   *********************************************************************************/
  public static String convertUnicodeCharToString(char c) {
    return "\\u" + Integer.toHexString((int) c | 0x10000).substring(1);
  }

  /*********************************************************************************
   * boolean isUnicodeControlChar(Char c)
   *
   * The following unicode control chars:
   *
   * U+0000 - U+001F and U+007F U+0080 - U+009F
   *********************************************************************************/
  public static boolean isUnicodeControlChar(char c) {

    if ((c >= '\u0000' && c <= '\u001F') || (c == '\u007F')
        || (c >= '\u0080' && c <= '\u009F'))
      return true;
    else
      return false;
  }

  /*********************************************************************************
   * void writeStringToFile(String string, String file, String encoding)
   *********************************************************************************/
  public static void writeStringToFile(String string, String file,
      String encoding) {
    try {
      FileOutputStream fos = new FileOutputStream(file);
      Writer out = new OutputStreamWriter(fos, encoding);
      out.write(string);
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /*********************************************************************************
   * String readStringFromFile(String file, String encoding)
   *********************************************************************************/
  public static String readStringFromFile(String file, String encoding) {
    StringBuffer buffer = new StringBuffer();
    try {
      FileInputStream fis = new FileInputStream(file);
      InputStreamReader isr = new InputStreamReader(fis, encoding);
      Reader in = new BufferedReader(isr);
      int ch;
      while ((ch = in.read()) > -1) {
        buffer.append((char) ch);
      }
      in.close();
      return buffer.toString();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  /*********************************************************************************
   * void writeStringToStream(String string, OutputStream os, String encoding)
   *********************************************************************************/
  public static void writeStringToStream(String string, OutputStream os,
      String encoding) {
    try {
      Writer out = new OutputStreamWriter(os, encoding);
      out.write(string);
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /*********************************************************************************
   * String readStringFromStream(InputStream is, String encoding)
   *********************************************************************************/
  public static String readStringFromStream(InputStream is, String encoding) {
    StringBuffer buffer = new StringBuffer();
    try {
      InputStreamReader isr = new InputStreamReader(is, encoding);
      Reader in = new BufferedReader(isr);
      int ch;
      while ((ch = in.read()) > -1) {
        buffer.append((char) ch);
      }
      in.close();
      return buffer.toString();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  /*********************************************************************************
   * void writeStringToFile(String string, String file, Charset charset)
   *********************************************************************************/
  public static void writeStringToFile(String string, String file,
      Charset charset) {
    try {
      FileOutputStream fos = new FileOutputStream(file);
      Writer out = new OutputStreamWriter(fos, charset);
      out.write(string);
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /*********************************************************************************
   * String readStringFromFile(String file, Charset charset)
   *********************************************************************************/
  public static String readStringFromFile(String file, Charset charset) {
    StringBuffer buffer = new StringBuffer();
    try {
      FileInputStream fis = new FileInputStream(file);
      InputStreamReader isr = new InputStreamReader(fis, charset);
      Reader in = new BufferedReader(isr);
      int ch;
      while ((ch = in.read()) > -1) {
        buffer.append((char) ch);
      }
      in.close();
      return buffer.toString();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  /*********************************************************************************
   * void writeStringToStream(String string, OutputStream os, Charset charset)
   *********************************************************************************/
  public static void writeStringToStream(String string, OutputStream os,
      Charset charset) {
    try {
      Writer out = new OutputStreamWriter(os, charset);
      out.write(string);
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /*********************************************************************************
   * String readStringFromStream(InputStream is, Charset charset)
   *********************************************************************************/
  public static String readStringFromStream(InputStream is, Charset charset) {
    StringBuffer buffer = new StringBuffer();
    try {
      InputStreamReader isr = new InputStreamReader(is, charset);
      Reader in = new BufferedReader(isr);
      int ch;
      while ((ch = in.read()) > -1) {
        buffer.append((char) ch);
      }
      in.close();
      return buffer.toString();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  /*********************************************************************************
   * Charset getCharset(String encoding)
   *********************************************************************************/
  public static Charset getCharset(String encoding) {
    Charset cs = null;

    try {
      cs = Charset.forName(encoding);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return cs;
  }

  /*********************************************************************************
   * void dumpLocation(JsonLocation location)
   *********************************************************************************/
  public static void dumpLocation(JsonLocation location) {
    if (location != null) {
      TestUtil
          .logMsg("JsonLocation info: lineNumber=" + location.getLineNumber()
              + ", columnNumber=" + location.getColumnNumber()
              + ", streamOffset=" + location.getStreamOffset());
    } else {
      TestUtil.logMsg("JsonLocation is null - no location info");
    }
  }

  /*********************************************************************************
   * void dumpLocation(JsonParser parser)
   *********************************************************************************/
  public static void dumpLocation(JsonParser parser) {
    dumpLocation(parser.getLocation());
  }

  /*********************************************************************************
   * boolean assertEquals(JsonLocation, JsonLocation)
   *********************************************************************************/
  public static boolean assertEquals(JsonLocation expLoc, JsonLocation actLoc) {
    if (expLoc.getLineNumber() == actLoc.getLineNumber()
        && expLoc.getColumnNumber() == actLoc.getColumnNumber()
        && expLoc.getStreamOffset() == actLoc.getStreamOffset()) {
      TestUtil.logMsg("JsonLocations equal - match (Success)");
      TestUtil.logMsg(
          "Expected: JsonLocation info: lineNumber=" + expLoc.getLineNumber()
              + ", columnNumber=" + expLoc.getColumnNumber() + ", streamOffset="
              + expLoc.getStreamOffset());
      TestUtil.logMsg(
          "Actual:   JsonLocation info: lineNumber=" + actLoc.getLineNumber()
              + ", columnNumber=" + actLoc.getColumnNumber() + ", streamOffset="
              + actLoc.getStreamOffset());
      return true;
    } else {
      TestUtil.logErr("JsonLocations not equal - mismatch (Failure)");
      TestUtil.logErr(
          "Expected: JsonLocation info: lineNumber=" + expLoc.getLineNumber()
              + ", columnNumber=" + expLoc.getColumnNumber() + ", streamOffset="
              + expLoc.getStreamOffset());
      TestUtil.logErr(
          "Actual:   JsonLocation info: lineNumber=" + actLoc.getLineNumber()
              + ", columnNumber=" + actLoc.getColumnNumber() + ", streamOffset="
              + actLoc.getStreamOffset());
      return false;
    }
  }

  /*********************************************************************************
   * void addFileToClassPath(String s)
   *********************************************************************************/
  public static void addFileToClassPath(String s) throws Exception {
    addFileToClassPath(new File(s));
  }

  /*********************************************************************************
   * void addFileToClassPath(File f)
   *********************************************************************************/
  public static void addFileToClassPath(File f) throws Exception {
    addURLToClassPath((f.toURI()).toURL());
  }

  /*********************************************************************************
   * void addURLToClassPath(URL url)
   *********************************************************************************/
  public static void addURLToClassPath(URL url) throws Exception {
    TestUtil.logMsg("addURLToClassPath-> " + url.toString());
    URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader
        .getSystemClassLoader();
    try {
      Class urlClassLoaderClass = URLClassLoader.class;
      Method method = urlClassLoaderClass.getDeclaredMethod("addURL",
          new Class[] { URL.class });
      method.setAccessible(true);
      method.invoke(urlClassLoader, new Object[] { url });
    } catch (Throwable t) {
      t.printStackTrace();
      throw new IOException("Error, could not add URL to system classloader");
    }
  }

  /*********************************************************************************
   * JsonArray buildJsonArrayFooBar
   *********************************************************************************/
  public static JsonArray buildJsonArrayFooBar() {
    try {
      JsonArray jsonArray = Json.createArrayBuilder().add("foo").add("bar")
          .build();
      return jsonArray;
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e);
      return null;
    }
  }

  public static final String JSONARRAYFOOBAR = "[\"foo\",\"bar\"]";

  /*********************************************************************************
   * JsonObject buildJsonObjectFooBar()
   *********************************************************************************/
  public static JsonObject buildJsonObjectFooBar() {
    try {
      JsonObject jsonObject = Json.createObjectBuilder().add("foo", "bar")
          .build();
      return jsonObject;
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e);
      return null;
    }
  }

  public static final String JSONOBJECTFOOBAR = "{\"foo\":\"bar\"}";

  /*********************************************************************************
   * JsonObject createSampleJsonObject()
   *
   * Assertion ids covered: 400/401/403/404/406/408/409
   *********************************************************************************/
  public static JsonObject createSampleJsonObject() throws Exception {
    JsonObject object = Json.createObjectBuilder().add("firstName", "John")
        .add("lastName", "Smith").add("age", 25).add("elderly", JsonValue.FALSE)
        .add("patriot", JsonValue.TRUE)
        .add("address",
            Json.createObjectBuilder().add("streetAddress", "21 2nd Street")
                .add("city", "New York").add("state", "NY")
                .add("postalCode", "10021"))
        .add("phoneNumber",
            Json.createArrayBuilder()
                .add(Json.createObjectBuilder().add("type", "home")
                    .add("number", "212 555-1234"))
                .add(Json.createObjectBuilder().add("type", "cell")
                    .add("number", "646 555-4567")))
        .add("objectOfFooBar",
            Json.createObjectBuilder()
                .add("objectFooBar", buildJsonObjectFooBar())
                .add("arrayFooBar", buildJsonArrayFooBar()))
        .add("arrayOfFooBar", Json.createArrayBuilder()
            .add(buildJsonObjectFooBar()).add(buildJsonArrayFooBar()))
        .build();
    return object;
  }

  /*********************************************************************************
   * EXPECTED_SAMPLEJSONOBJECT_TEXT Constant defining expected Json text output
   * of above sample JsonObject
   *********************************************************************************/
  public final static String EXPECTED_SAMPLEJSONOBJECT_TEXT = "{\"firstName\":\"John\",\"lastName\":\"Smith\",\"age\":25,\"elderly\":false,\"patriot\":true,"
      + "\"address\":{\"streetAddress\":\"21 2nd Street\",\"city\":\"New York\",\"state\":\"NY\","
      + "\"postalCode\":\"10021\"},\"phoneNumber\":[{\"type\":\"home\",\"number\":\"212 555-1234\"},"
      + "{\"type\":\"cell\",\"number\":\"646 555-4567\"}],\"objectOfFooBar\":{\"objectFooBar\":"
      + "{\"foo\":\"bar\"},\"arrayFooBar\":[\"foo\",\"bar\"]},\"arrayOfFooBar\":[{\"foo\":\"bar\"},"
      + "[\"foo\",\"bar\"]]}";

  /*********************************************************************************
   * JsonObject createSampleJsonObject2()
   *********************************************************************************/
  public static JsonObject createSampleJsonObject2() throws Exception {
    JsonObject object = Json.createObjectBuilder().add("firstName", "John")
        .add("lastName", "Smith").add("age", 25).add("elderly", JsonValue.FALSE)
        .add("patriot", JsonValue.TRUE)
        .add("address",
            Json.createObjectBuilder().add("streetAddress", "21 2nd Street")
                .add("city", "New York").add("state", "NY")
                .add("postalCode", "10021"))
        .add("phoneNumber",
            Json.createArrayBuilder()
                .add(Json.createObjectBuilder().add("type", "home")
                    .add("number", "212 555-1234"))
                .add(Json.createObjectBuilder().add("type", "cell")
                    .add("number", "535 444-1234")))
        .build();
    return object;
  }

  /*********************************************************************************
   * JsonArray createSampleJsonArray()
   *
   * Assertion ids covered: 400/401/402/403/404/406/409
   *********************************************************************************/
  public static JsonArray createSampleJsonArray() throws Exception { // Indices
    JsonArray array = Json.createArrayBuilder().add(Json.createObjectBuilder() // 0
        .add("name1", "value1").add("name2", "value2")).add(JsonValue.TRUE)
        .add(JsonValue.FALSE).add(JsonValue.NULL) // 1,2,3
        .add(100).add(200L).add("string") // 4,5,6
        .add(BigDecimal.valueOf(123456789)).add(new BigInteger("123456789"))// 7,8
        .add(Json.createObjectBuilder() // 9
            .add("name3", "value3").add("name4", "value4"))
        .add(true).add(false).addNull() // 10,11,12
        .add(Json.createArrayBuilder() // 13
            .add(2).add(4))
        .add(Json.createObjectBuilder() // 14
            .add("objectFooBar", buildJsonObjectFooBar())
            .add("arrayFooBar", buildJsonArrayFooBar()))
        .add(Json.createArrayBuilder() // 15
            .add(buildJsonObjectFooBar()).add(buildJsonArrayFooBar()))
        .build();
    return array;
  }

  /*********************************************************************************
   * EXPECTED_SAMPLEJSONARRAY_TEXT Constant defining expected Json text output
   * of above sample JsonArray
   *********************************************************************************/
  public final static String EXPECTED_SAMPLEJSONARRAY_TEXT = "[{\"name1\":\"value1\",\"name2\":\"value2\"},true,false,null,100,200,\"string\",123456789,123456789,"
      + "{\"name3\":\"value3\",\"name4\":\"value4\"},true,false,null,[2,4],{\"objectFooBar\":"
      + "{\"foo\":\"bar\"},\"arrayFooBar\":[\"foo\",\"bar\"]},[{\"foo\":\"bar\"},[\"foo\",\"bar\"]]]";

  /*********************************************************************************
   * JsonArray createSampleJsonArray2()
   *********************************************************************************/
  public static JsonArray createSampleJsonArray2() throws Exception { // Indices
    JsonArray array = Json.createArrayBuilder().add(Json.createObjectBuilder() // 0
        .add("name1", "value1").add("name2", "value2")).add(JsonValue.TRUE)
        .add(JsonValue.FALSE).add(JsonValue.NULL) // 1,2,3
        .add(Integer.MAX_VALUE).add(Long.MAX_VALUE).add("string") // 4,5,6
        .add(Json.createObjectBuilder() // 7
            .add("name3", "value3").add("name4", "value4"))
        .add(true).add(false).addNull() // 8,9,10
        .add(Json.createArrayBuilder() // 11
            .add(1).add(3))
        .build();
    return array;
  }
}
