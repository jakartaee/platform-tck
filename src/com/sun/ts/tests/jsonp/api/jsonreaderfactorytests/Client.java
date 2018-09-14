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
package com.sun.ts.tests.jsonp.api.jsonreaderfactorytests;

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
   * @testName: jsonReaderFactoryTest1
   * 
   * @assertion_ids: JSONP:JAVADOC:419; JSONP:JAVADOC:449; JSONP:JAVADOC:185;
   * JSONP:JAVADOC:459;
   * 
   * @test_Strategy: Tests the JsonReaderFactory API.
   *
   * JsonReaderFactory readerFactory = Json.createReaderFactory(Map<String, ?>);
   * JsonReader reader1 = readerFactory.createReader(Reader) JsonReader reader2
   * = readerFactory.createReader(Reader)
   */
  public void jsonReaderFactoryTest1() throws Fault {
    boolean pass = true;
    JsonReader reader1 = null;
    JsonReader reader2 = null;
    JsonObject jsonObject = null;
    String jsonObjectText = "{\"foo\":\"bar\"}";
    try {
      logMsg("Create JsonReaderFactory with Map<String, ?> with EMPTY config");
      JsonReaderFactory readerFactory = Json
          .createReaderFactory(JSONP_Util.getEmptyConfig());
      logMsg("Checking factory configuration properties");
      Map<String, ?> config = readerFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;
      logMsg("--------------------------------------------------");
      logMsg("TEST CASE [JsonReaderFactory.createReader(Reader)]");
      logMsg("--------------------------------------------------");
      logMsg("Create 1st JsonReader using JsonReaderFactory");
      reader1 = readerFactory.createReader(new StringReader(jsonObjectText));
      if (reader1 == null) {
        logErr("ReaderFactory failed to create reader1");
        pass = false;
      } else {
        jsonObject = reader1.readObject();
        reader1.close();

        if (!JSONP_Util.assertEquals(jsonObject.size(), 1)
            || !JSONP_Util.assertEquals(jsonObject.getString("foo"), "bar"))
          pass = false;
      }

      logMsg("Create 2nd JsonReader using JsonReaderFactory");
      reader2 = readerFactory.createReader(new StringReader(jsonObjectText));
      if (reader2 == null) {
        logErr("ReaderFactory failed to create reader2");
        pass = false;
      } else {
        jsonObject = reader2.readObject();
        reader2.close();

        if (!JSONP_Util.assertEquals(jsonObject.size(), 1)
            || !JSONP_Util.assertEquals(jsonObject.getString("foo"), "bar"))
          pass = false;
      }

    } catch (Exception e) {
      throw new Fault("jsonReaderFactoryTest1 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonReaderFactoryTest1 Failed");
  }

  /*
   * @testName: jsonReaderFactoryTest2
   * 
   * @assertion_ids: JSONP:JAVADOC:420; JSONP:JAVADOC:449; JSONP:JAVADOC:185;
   * JSONP:JAVADOC:459;
   * 
   * @test_Strategy: Tests the JsonReaderFactory API.
   *
   * JsonReaderFactory readerFactory = Json.createReaderFactory(Map<String,?>);
   * JsonReader reader1 = readerFactory.createReader(InputStream, Charset)
   * JsonReader reader2 = readerFactory.createReader(InputStream, Charset)
   *
   * Create reader with both UTF-8 and UTF-16BE.
   */
  public void jsonReaderFactoryTest2() throws Fault {
    boolean pass = true;
    JsonReader reader1 = null;
    JsonReader reader2 = null;
    JsonObject jsonObject = null;
    String jsonObjectText = "{\"foo\":\"bar\"}";
    try {
      logMsg("Create JsonReaderFactory with Map<String, ?> with EMPTY config");
      JsonReaderFactory readerFactory = Json
          .createReaderFactory(JSONP_Util.getEmptyConfig());
      logMsg("Checking factory configuration properties");
      Map<String, ?> config = readerFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;

      logMsg(
          "----------------------------------------------------------------");
      logMsg(
          "TEST CASE [JsonReaderFactory.createReader(InputStream, Charset)]");
      logMsg(
          "----------------------------------------------------------------");
      logMsg(
          "Create 1st JsonReader using JsonReaderFactory with UTF-8 encoding");
      InputStream is1 = JSONP_Util.getInputStreamFromString(jsonObjectText);
      reader1 = readerFactory.createReader(is1, JSONP_Util.UTF_8);
      if (reader1 == null) {
        logErr("ReaderFactory failed to create reader1");
        pass = false;
      } else {
        jsonObject = reader1.readObject();
        reader1.close();

        if (!JSONP_Util.assertEquals(jsonObject.size(), 1)
            || !JSONP_Util.assertEquals(jsonObject.getString("foo"), "bar"))
          pass = false;
      }

      logMsg(
          "Create 2nd JsonReader using JsonReaderFactory with UTF-8 encoding");
      InputStream is2 = JSONP_Util.getInputStreamFromString(jsonObjectText);
      reader2 = readerFactory.createReader(is2, JSONP_Util.UTF_8);
      if (reader2 == null) {
        logErr("ReaderFactory failed to create reader2");
        pass = false;
      } else {
        jsonObject = reader2.readObject();
        reader2.close();

        if (!JSONP_Util.assertEquals(jsonObject.size(), 1)
            || !JSONP_Util.assertEquals(jsonObject.getString("foo"), "bar"))
          pass = false;
      }

    } catch (Exception e) {
      throw new Fault("jsonReaderFactoryTest2 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonReaderFactoryTest2 Failed");
  }

  /*
   * @testName: jsonReaderFactoryTest3
   * 
   * @assertion_ids: JSONP:JAVADOC:429; JSONP:JAVADOC:449; JSONP:JAVADOC:185;
   * JSONP:JAVADOC:459;
   * 
   * @test_Strategy: Tests the JsonReaderFactory API.
   *
   * JsonReaderFactory readerFactory = Json.createReaderFactory(Map<String, ?>);
   * JsonReader reader1 = readerFactory.createReader(InputStream) JsonReader
   * reader2 = readerFactory.createReader(InputStream)
   */
  public void jsonReaderFactoryTest3() throws Fault {
    boolean pass = true;
    JsonReader reader1 = null;
    JsonReader reader2 = null;
    JsonObject jsonObject = null;
    String jsonObjectText = "{\"foo\":\"bar\"}";
    try {
      logMsg("Create JsonReaderFactory with Map<String, ?> with EMPTY config");
      JsonReaderFactory readerFactory = Json
          .createReaderFactory(JSONP_Util.getEmptyConfig());
      logMsg("Checking factory configuration properties");
      Map<String, ?> config = readerFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;

      logMsg("-------------------------------------------------------");
      logMsg("TEST CASE [JsonReaderFactory.createReader(InputStream)]");
      logMsg("-------------------------------------------------------");
      logMsg("Create 1st JsonReader using JsonReaderFactory");
      InputStream is1 = JSONP_Util.getInputStreamFromString(jsonObjectText);
      reader1 = readerFactory.createReader(is1);
      if (reader1 == null) {
        logErr("ReaderFactory failed to create reader1");
        pass = false;
      } else {
        jsonObject = reader1.readObject();
        reader1.close();

        if (!JSONP_Util.assertEquals(jsonObject.size(), 1)
            || !JSONP_Util.assertEquals(jsonObject.getString("foo"), "bar"))
          pass = false;
      }

      logMsg("Create 2nd JsonReader using JsonReaderFactory");
      InputStream is2 = JSONP_Util.getInputStreamFromString(jsonObjectText);
      reader2 = readerFactory.createReader(is2);
      if (reader2 == null) {
        logErr("ReaderFactory failed to create reader2");
        pass = false;
      } else {
        jsonObject = reader2.readObject();
        reader2.close();

        if (!JSONP_Util.assertEquals(jsonObject.size(), 1)
            || !JSONP_Util.assertEquals(jsonObject.getString("foo"), "bar"))
          pass = false;
      }

    } catch (Exception e) {
      throw new Fault("jsonReaderFactoryTest3 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonReaderFactoryTest3 Failed");
  }

  /*
   * @testName: jsonReaderFactoryTest4
   * 
   * @assertion_ids: JSONP:JAVADOC:449; JSONP:JAVADOC:459;
   * 
   * @test_Strategy: Tests the JsonReaderFactory API.
   *
   * JsonReaderFactory readerFactory = Json.createReaderFactory(Map<String, ?>);
   * Map<String, ?> config = JsonReaderFactory.getConfigInUse();
   *
   * Test for the following 3 scenarios: 1) no supported provider property
   * (empty config) 2) non supported provider property
   */
  public void jsonReaderFactoryTest4() throws Fault {
    boolean pass = true;
    JsonReaderFactory readerFactory;
    Map<String, ?> config;
    try {
      logMsg("----------------------------------------------");
      logMsg("Test scenario1: no supported provider property");
      logMsg("----------------------------------------------");
      logMsg("Create JsonReaderFactory with Map<String, ?> with EMPTY config");
      readerFactory = Json.createReaderFactory(JSONP_Util.getEmptyConfig());
      config = readerFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;

      logMsg("-----------------------------------------------");
      logMsg("Test scenario2: non supported provider property");
      logMsg("-----------------------------------------------");
      logMsg("Create JsonReaderFactory with Map<String, ?> with FOO config");
      readerFactory = Json.createReaderFactory(JSONP_Util.getFooConfig());
      config = readerFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;
    } catch (Exception e) {
      throw new Fault("jsonReaderFactoryTest4 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonReaderFactoryTest4 Failed");
  }
}
