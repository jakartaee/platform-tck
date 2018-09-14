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
 * $Id: Client.java 66863 2012-07-23 11:26:40Z adf $
 */
package com.sun.ts.tests.jsonp.api.jsonparserfactorytests;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import javax.json.*;
import javax.json.stream.*;
import java.io.*;
import java.nio.charset.Charset;

import com.sun.javatest.Status;
import java.util.Properties;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;

import javax.json.stream.JsonParser.Event.*;

import com.sun.ts.tests.jsonp.common.*;

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
   * @testName: jsonParserFactoryTest1
   * 
   * @assertion_ids: JSONP:JAVADOC:417; JSONP:JAVADOC:164;
   * 
   * @test_Strategy: Tests the JsonParserFactory API.
   *
   * JsonParserFactory parserFactory = Json.createParserFactory(Map<String, ?>);
   * JsonParser parser1 = parserFactory.createParser(Reader) JsonParser parser2
   * = parserFactory.createParser(Reader)
   */
  public void jsonParserFactoryTest1() throws Fault {
    boolean pass = true;
    JsonParser parser1 = null;
    JsonParser parser2 = null;
    JsonParser.Event event = null;
    try {
      logMsg("Create JsonParserFactory with a configuration");
      JsonParserFactory parserFactory = Json
          .createParserFactory(JSONP_Util.getEmptyConfig());
      logMsg("Checking factory configuration properties");
      Map<String, ?> config = parserFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;

      logMsg("--------------------------------------------------");
      logMsg("TEST CASE [JsonParserFactory.createParser(Reader)]");
      logMsg("--------------------------------------------------");
      String jsonObjectString = "{\"foo\":\"bar\"}";
      logMsg("Create 1st JsonParser from the Reader using JsonParserFactory");
      parser1 = parserFactory.createParser(new StringReader(jsonObjectString));
      if (parser1 == null) {
        logErr("ParserFactory failed to create parser1 from Reader");
        pass = false;
      } else {
        logMsg("Parsing " + jsonObjectString);
        logMsg("Verify that JSON Parser Events/Data matches");
        JSONP_Util.resetParseErrs();
        JSONP_Util.testEventType(parser1, JsonParser.Event.START_OBJECT);
        JSONP_Util.testKeyStringValue(parser1, "foo", "bar");
        JSONP_Util.testEventType(parser1, JsonParser.Event.END_OBJECT);
        int parseErrs = JSONP_Util.getParseErrs();
        if (parseErrs != 0) {
          logErr("There were " + parseErrs + " parser errors that occurred.");
          pass = false;
        }
      }

      logMsg("Create 2nd JsonParser from the Reader using JsonParserFactory");
      parser2 = parserFactory.createParser(new StringReader(jsonObjectString));
      if (parser2 == null) {
        logErr("ParserFactory failed to create parser2 from Reader");
        pass = false;
      } else {
        logMsg("Parsing " + jsonObjectString);
        logMsg("Verify that JSON Parser Events/Data matches");
        JSONP_Util.resetParseErrs();
        JSONP_Util.testEventType(parser2, JsonParser.Event.START_OBJECT);
        JSONP_Util.testKeyStringValue(parser2, "foo", "bar");
        JSONP_Util.testEventType(parser2, JsonParser.Event.END_OBJECT);
        int parseErrs = JSONP_Util.getParseErrs();
        if (parseErrs != 0) {
          logErr("There were " + parseErrs + " parser errors that occurred.");
          pass = false;
        }
      }

    } catch (Exception e) {
      throw new Fault("jsonParserFactoryTest1 Failed: ", e);
    } finally {
      try {
        parser1.close();
        parser2.close();
      } catch (Exception e) {
      }
    }
    if (!pass)
      throw new Fault("jsonParserFactoryTest1 Failed");
  }

  /*
   * @testName: jsonParserFactoryTest2
   * 
   * @assertion_ids: JSONP:JAVADOC:417; JSONP:JAVADOC:166;
   * 
   * @test_Strategy: Tests the JsonParserFactory API.
   *
   * JsonParserFactory parserFactory = Json.createParserFactory(Map<String, ?>);
   * JsonParser parser1 = parserFactory.createParser(JsonObject) JsonParser
   * parser2 = parserFactory.createParser(JsonObject)
   */
  public void jsonParserFactoryTest2() throws Fault {
    boolean pass = true;
    JsonParser parser1 = null;
    JsonParser parser2 = null;
    JsonParser.Event event = null;
    try {
      logMsg("Create JsonParserFactory with a configuration");
      JsonParserFactory parserFactory = Json
          .createParserFactory(JSONP_Util.getEmptyConfig());
      logMsg("Checking factory configuration properties");
      Map<String, ?> config = parserFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;

      logMsg("------------------------------------------------------");
      logMsg("TEST CASE [JsonParserFactory.createParser(JsonObject)]");
      logMsg("------------------------------------------------------");
      String jsonObjectString = "{\"foo\":\"bar\"}";
      JsonObject jsonObj = JSONP_Util
          .createJsonObjectFromString(jsonObjectString);
      logMsg(
          "Create 1st JsonParser from the JsonObject using JsonParserFactory");
      parser1 = parserFactory.createParser(jsonObj);
      if (parser1 == null) {
        logErr("ParserFactory failed to create parser1 from JsonObject");
        pass = false;
      } else {
        logMsg("Parsing " + jsonObjectString);
        logMsg("Verify that JSON Parser Events/Data matches");
        JSONP_Util.resetParseErrs();
        JSONP_Util.testEventType(parser1, JsonParser.Event.START_OBJECT);
        JSONP_Util.testKeyStringValue(parser1, "foo", "bar");
        JSONP_Util.testEventType(parser1, JsonParser.Event.END_OBJECT);
        int parseErrs = JSONP_Util.getParseErrs();
        if (parseErrs != 0) {
          logErr("There were " + parseErrs + " parser errors that occurred.");
          pass = false;
        }
      }

      logMsg(
          "Create 2nd JsonParser from the JsonObject using JsonParserFactory");
      parser2 = parserFactory.createParser(jsonObj);
      if (parser2 == null) {
        logErr("ParserFactory failed to create parser2 from JsonObject");
        pass = false;
      } else {
        logMsg("Parsing " + jsonObjectString);
        logMsg("Verify that JSON Parser Events/Data matches");
        JSONP_Util.resetParseErrs();
        JSONP_Util.testEventType(parser2, JsonParser.Event.START_OBJECT);
        JSONP_Util.testKeyStringValue(parser2, "foo", "bar");
        JSONP_Util.testEventType(parser2, JsonParser.Event.END_OBJECT);
        int parseErrs = JSONP_Util.getParseErrs();
        if (parseErrs != 0) {
          logErr("There were " + parseErrs + " parser errors that occurred.");
          pass = false;
        }
      }

    } catch (Exception e) {
      throw new Fault("jsonParserFactoryTest2 Failed: ", e);
    } finally {
      try {
        parser1.close();
        parser2.close();
      } catch (Exception e) {
      }
    }
    if (!pass)
      throw new Fault("jsonParserFactoryTest2 Failed");
  }

  /*
   * @testName: jsonParserFactoryTest3
   * 
   * @assertion_ids: JSONP:JAVADOC:417; JSONP:JAVADOC:167;
   * 
   * @test_Strategy: Tests the JsonParserFactory API.
   *
   * JsonParserFactory parserFactory = Json.createParserFactory(Map<String, ?>);
   * JsonParser parser1 = parserFactory.createParser(JsonArray) JsonParser
   * parser2 = parserFactory.createParser(JsonArray)
   */
  public void jsonParserFactoryTest3() throws Fault {
    boolean pass = true;
    JsonParser parser1 = null;
    JsonParser parser2 = null;
    JsonParser.Event event = null;
    try {
      logMsg("Create JsonParserFactory with a configuration");
      JsonParserFactory parserFactory = Json
          .createParserFactory(JSONP_Util.getEmptyConfig());
      logMsg("Checking factory configuration properties");
      Map<String, ?> config = parserFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;

      logMsg("-----------------------------------------------------");
      logMsg("TEST CASE [JsonParserFactory.createParser(JsonArray)]");
      logMsg("-----------------------------------------------------");
      String jsonArrayString = "[\"foo\",\"bar\"]";
      JsonArray jsonArr = JSONP_Util.createJsonArrayFromString(jsonArrayString);
      logMsg(
          "Create 1st JsonParser from the JsonArray using JsonParserFactory");
      parser1 = parserFactory.createParser(jsonArr);
      if (parser1 == null) {
        logErr("ParserFactory failed to create parser1 from JsonArray");
        pass = false;
      } else {
        logMsg("Parsing " + jsonArrayString);
        logMsg("Verify that JSON Parser Events/Data matches");
        JSONP_Util.resetParseErrs();
        JSONP_Util.testEventType(parser1, JsonParser.Event.START_ARRAY);
        JSONP_Util.testStringValue(parser1, "foo");
        JSONP_Util.testStringValue(parser1, "bar");
        JSONP_Util.testEventType(parser1, JsonParser.Event.END_ARRAY);
        int parseErrs = JSONP_Util.getParseErrs();
        if (parseErrs != 0) {
          logErr("There were " + parseErrs + " parser errors that occurred.");
          pass = false;
        }
      }

      logMsg(
          "Create 2nd JsonParser from the JsonArray using JsonParserFactory");
      parser2 = parserFactory.createParser(jsonArr);
      if (parser2 == null) {
        logErr("ParserFactory failed to create parser2 from JsonArray");
        pass = false;
      } else {
        logMsg("Parsing " + jsonArrayString);
        logMsg("Verify that JSON Parser Events/Data matches");
        JSONP_Util.resetParseErrs();
        JSONP_Util.testEventType(parser2, JsonParser.Event.START_ARRAY);
        JSONP_Util.testStringValue(parser2, "foo");
        JSONP_Util.testStringValue(parser2, "bar");
        JSONP_Util.testEventType(parser2, JsonParser.Event.END_ARRAY);
        int parseErrs = JSONP_Util.getParseErrs();
        if (parseErrs != 0) {
          logErr("There were " + parseErrs + " parser errors that occurred.");
          pass = false;
        }
      }
    } catch (Exception e) {
      throw new Fault("jsonParserFactoryTest3 Failed: ", e);
    } finally {
      try {
        parser1.close();
        parser2.close();
      } catch (Exception e) {
      }
    }
    if (!pass)
      throw new Fault("jsonParserFactoryTest3 Failed");
  }

  /*
   * @testName: jsonParserFactoryTest4
   * 
   * @assertion_ids: JSONP:JAVADOC:417; JSONP:JAVADOC:165;
   * 
   * @test_Strategy: Tests the JsonParserFactory API.
   *
   * JsonParserFactory parserFactory = Json.createParserFactory(Map<String, ?>);
   * JsonParser parser1 = parserFactory.createParser(InputStream) JsonParser
   * parser2 = parserFactory.createParser(InputStream)
   */
  public void jsonParserFactoryTest4() throws Fault {
    boolean pass = true;
    JsonParser parser1 = null;
    JsonParser parser2 = null;
    JsonParser.Event event = null;
    try {
      logMsg("Create JsonParserFactory with a configuration");
      JsonParserFactory parserFactory = Json
          .createParserFactory(JSONP_Util.getEmptyConfig());
      logMsg("Checking factory configuration properties");
      Map<String, ?> config = parserFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;

      logMsg("-------------------------------------------------------");
      logMsg("TEST CASE [JsonParserFactory.createParser(InputStream)]");
      logMsg("-------------------------------------------------------");
      String jsonObjectString = "{\"foo\":\"bar\"}";
      logMsg(
          "Create 1st JsonParser from the InputStream using JsonParserFactory");
      parser1 = parserFactory.createParser(new ByteArrayInputStream(
          jsonObjectString.getBytes(JSONP_Util.UTF_8)));
      if (parser1 == null) {
        logErr("ParserFactory failed to create parser1 from InputStream");
        pass = false;
      } else {
        logMsg("Parsing " + jsonObjectString);
        logMsg("Verify that JSON Parser Events/Data matches");
        JSONP_Util.resetParseErrs();
        JSONP_Util.testEventType(parser1, JsonParser.Event.START_OBJECT);
        JSONP_Util.testKeyStringValue(parser1, "foo", "bar");
        JSONP_Util.testEventType(parser1, JsonParser.Event.END_OBJECT);
        int parseErrs = JSONP_Util.getParseErrs();
        if (parseErrs != 0) {
          logErr("There were " + parseErrs + " parser errors that occurred.");
          pass = false;
        }
      }

      logMsg(
          "Create 2nd JsonParser from the InputStream using JsonParserFactory");
      parser2 = parserFactory.createParser(new ByteArrayInputStream(
          jsonObjectString.getBytes(JSONP_Util.UTF_8)));
      if (parser2 == null) {
        logErr("ParserFactory failed to create parser2 from InputStream");
        pass = false;
      } else {
        logMsg("Parsing " + jsonObjectString);
        logMsg("Verify that JSON Parser Events/Data matches");
        JSONP_Util.resetParseErrs();
        JSONP_Util.testEventType(parser2, JsonParser.Event.START_OBJECT);
        JSONP_Util.testKeyStringValue(parser2, "foo", "bar");
        JSONP_Util.testEventType(parser2, JsonParser.Event.END_OBJECT);
        int parseErrs = JSONP_Util.getParseErrs();
        if (parseErrs != 0) {
          logErr("There were " + parseErrs + " parser errors that occurred.");
          pass = false;
        }
      }

    } catch (Exception e) {
      throw new Fault("jsonParserFactoryTest4 Failed: ", e);
    } finally {
      try {
        parser1.close();
        parser2.close();
      } catch (Exception e) {
      }
    }
    if (!pass)
      throw new Fault("jsonParserFactoryTest4 Failed");
  }

  /*
   * @testName: jsonParserFactoryTest5
   * 
   * @assertion_ids: JSONP:JAVADOC:417; JSONP:JAVADOC:201;
   * 
   * @test_Strategy: Tests the JsonParserFactory API.
   *
   * JsonParserFactory parserFactory = Json.createParserFactory(Map<String, ?>);
   * JsonParser parser1 = parserFactory.createParser(InputStream, Charset)
   * JsonParser parser2 = parserFactory.createParser(InputStream, Charset)
   */
  public void jsonParserFactoryTest5() throws Fault {
    boolean pass = true;
    JsonParser parser1 = null;
    JsonParser parser2 = null;
    JsonParser.Event event = null;
    try {
      logMsg("Create JsonParserFactory with a configuration");
      JsonParserFactory parserFactory = Json
          .createParserFactory(JSONP_Util.getEmptyConfig());
      logMsg("Checking factory configuration properties");
      Map<String, ?> config = parserFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;

      logMsg(
          "----------------------------------------------------------------");
      logMsg(
          "TEST CASE [JsonParserFactory.createParser(InputStream, Charset)]");
      logMsg(
          "----------------------------------------------------------------");
      String jsonObjectString = "{\"foo\":\"bar\"}";
      logMsg(
          "Create 1st JsonParser from the InputStream using JsonParserFactory");
      parser1 = parserFactory.createParser(
          new ByteArrayInputStream(jsonObjectString.getBytes(JSONP_Util.UTF_8)),
          JSONP_Util.UTF_8);
      if (parser1 == null) {
        logErr("ParserFactory failed to create parser1 from InputStream");
        pass = false;
      } else {
        logMsg("Parsing " + jsonObjectString);
        logMsg("Verify that JSON Parser Events/Data matches");
        JSONP_Util.resetParseErrs();
        JSONP_Util.testEventType(parser1, JsonParser.Event.START_OBJECT);
        JSONP_Util.testKeyStringValue(parser1, "foo", "bar");
        JSONP_Util.testEventType(parser1, JsonParser.Event.END_OBJECT);
        int parseErrs = JSONP_Util.getParseErrs();
        if (parseErrs != 0) {
          logErr("There were " + parseErrs + " parser errors that occurred.");
          pass = false;
        }
      }

      logMsg(
          "Create 2nd JsonParser from the InputStream using JsonParserFactory");
      parser2 = parserFactory.createParser(
          new ByteArrayInputStream(jsonObjectString.getBytes(JSONP_Util.UTF_8)),
          JSONP_Util.UTF_8);
      if (parser2 == null) {
        logErr("ParserFactory failed to create parser2 from InputStream");
        pass = false;
      } else {
        logMsg("Parsing " + jsonObjectString);
        logMsg("Verify that JSON Parser Events/Data matches");
        JSONP_Util.resetParseErrs();
        JSONP_Util.testEventType(parser2, JsonParser.Event.START_OBJECT);
        JSONP_Util.testKeyStringValue(parser2, "foo", "bar");
        JSONP_Util.testEventType(parser2, JsonParser.Event.END_OBJECT);
        int parseErrs = JSONP_Util.getParseErrs();
        if (parseErrs != 0) {
          logErr("There were " + parseErrs + " parser errors that occurred.");
          pass = false;
        }
      }

    } catch (Exception e) {
      throw new Fault("jsonParserFactoryTest5 Failed: ", e);
    } finally {
      try {
        parser1.close();
        parser2.close();
      } catch (Exception e) {
      }
    }
    if (!pass)
      throw new Fault("jsonParserFactoryTest5 Failed");
  }

  /*
   * @testName: jsonParserFactoryTest6
   * 
   * @assertion_ids: JSONP:JAVADOC:417; JSONP:JAVADOC:164; JSONP:JAVADOC:428;
   * 
   * @test_Strategy: Tests the JsonParserFactory API.
   *
   * JsonParserFactory parserFactory = Json.createParserFactory(Map<String, ?>);
   * Map<String, ?> config = JsonParserFactory.getConfigInUse();
   *
   * Test for the following 2 scenarios: 1) no supported provider property
   * (empty config) 2) non supported provider property
   */
  public void jsonParserFactoryTest6() throws Fault {
    boolean pass = true;
    JsonParserFactory parserFactory;
    Map<String, ?> config;
    try {
      logMsg("----------------------------------------------");
      logMsg("Test scenario1: no supported provider property");
      logMsg("----------------------------------------------");
      logMsg("Create JsonParserFactory with Map<String, ?> with EMPTY config");
      parserFactory = Json.createParserFactory(JSONP_Util.getEmptyConfig());
      config = parserFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;

      logMsg("-----------------------------------------------");
      logMsg("Test scenario2: non supported provider property");
      logMsg("-----------------------------------------------");
      logMsg("Create JsonParserFactory with Map<String, ?> with FOO config");
      parserFactory = Json.createParserFactory(JSONP_Util.getFooConfig());
      config = parserFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;
    } catch (Exception e) {
      throw new Fault("jsonParserFactoryTest6 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonParserFactoryTest6 Failed");
  }

  /*
   * @testName: jsonParserFactoryExceptionTest
   * 
   * @assertion_ids: JSONP:JAVADOC:225;
   * 
   * @test_Strategy: Test JsonParserFactory exception conditions. Trip the
   * following exception due to unknown encoding or i/o error:
   *
   * javax.json.JsonException
   */
  public void jsonParserFactoryExceptionTest() throws Fault {
    boolean pass = true;

    // Tests JsonParserFactory.createParser(InputStream) for JsonException if
    // i/o error
    try {
      logMsg(
          "Tests JsonParserFactory.createParser(InputStream) for JsonException if i/o error.");
      JsonParserFactory parserFactory = Json
          .createParserFactory(JSONP_Util.getEmptyConfig());
      MyBufferedInputStream mbi = new MyBufferedInputStream(
          JSONP_Util.getInputStreamFromString("{}"), true);
      JsonParser parser = parserFactory.createParser(mbi);
      logErr("Did not get expected JsonException");
      pass = false;
    } catch (JsonException e) {
      logMsg("Caught expected JsonException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    }

    // Tests JsonParserFactory.createParser(InputStream) for JsonException if
    // unknown encoding
    try {
      logMsg(
          "Tests JsonParserFactory.createParser(InputStream) for JsonException if unknown encoding.");
      JsonParserFactory parserFactory = Json
          .createParserFactory(JSONP_Util.getEmptyConfig());
      InputStream is = JSONP_Util
          .getInputStreamFromResource("jsonObjectUnknownEncoding.json");
      JsonParser parser = parserFactory.createParser(is);
      logMsg("parser=" + parser);
      logErr("Did not get expected JsonException");
      pass = false;
    } catch (JsonException e) {
      logMsg("Caught expected JsonException");
    } catch (Exception e) {
      pass = false;
      logErr("Caught unexpected exception: " + e);
    }

    if (!pass)
      throw new Fault("jsonParserFactoryExceptionTest Failed");
  }
}
