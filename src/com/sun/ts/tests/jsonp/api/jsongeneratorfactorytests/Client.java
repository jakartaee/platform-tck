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
package com.sun.ts.tests.jsonp.api.jsongeneratorfactorytests;

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
   * @testName: jsonGeneratorFactoryTest1
   * 
   * @assertion_ids: JSONP:JAVADOC:162; JSONP:JAVADOC:416; JSONP:JAVADOC:427;
   * 
   * @test_Strategy: Tests the JsonGeneratorFactory API.
   *
   * JsonGeneratorFactory generatorFactory =
   * Json.createGeneratorFactory(Map<String, ?>); JsonGenerator generator1 =
   * generatorFactory.createGenerator(Writer) JsonGenerator generator2 =
   * generatorFactory.createGenerator(Writer)
   */
  public void jsonGeneratorFactoryTest1() throws Fault {
    boolean pass = true;
    JsonGenerator generator1 = null;
    JsonGenerator generator2 = null;
    String expString;
    String actString;
    try {
      logMsg(
          "Create JsonGeneratorFactory with Map<String, ?> with PRETTY_PRINTING config");
      JsonGeneratorFactory generatorFactory = Json
          .createGeneratorFactory(JSONP_Util.getPrettyPrintingConfig());
      logMsg("Checking factory configuration properties");
      Map<String, ?> config = generatorFactory.getConfigInUse();
      String[] props = { JsonGenerator.PRETTY_PRINTING, };
      if (!JSONP_Util.doConfigCheck(config, 1, props))
        pass = false;
      logMsg("--------------------------------------------------------");
      logMsg("TEST CASE [JsonGeneratorFactory.createGenerator(Writer)]");
      logMsg("--------------------------------------------------------");
      logMsg("Create 1st JsonGenerator using JsonGeneratorFactory");
      StringWriter sWriter1 = new StringWriter();
      generator1 = generatorFactory.createGenerator(sWriter1);
      if (generator1 == null) {
        logErr("GeneratorFactory failed to create generator1");
        pass = false;
      } else {
        generator1.writeStartObject().writeEnd();
        generator1.close();
      }
      logMsg("sWriter1=" + sWriter1.toString());
      expString = "{}";
      actString = JSONP_Util.removeWhitespace(sWriter1.toString());
      if (!JSONP_Util.assertEqualsJsonText(expString, actString))
        pass = false;

      logMsg("Create 2nd JsonGenerator using JsonGeneratorFactory");
      StringWriter sWriter2 = new StringWriter();
      generator2 = generatorFactory.createGenerator(sWriter2);
      if (generator2 == null) {
        logErr("GeneratorFactory failed to create generator2");
        pass = false;
      } else {
        generator2.writeStartArray().writeEnd();
        generator2.close();
      }
      logMsg("sWriter2=" + sWriter2.toString());
      expString = "[]";
      actString = JSONP_Util.removeWhitespace(sWriter2.toString());
      if (!JSONP_Util.assertEqualsJsonText(expString, actString))
        pass = false;

    } catch (Exception e) {
      throw new Fault("jsonGeneratorFactoryTest1 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonGeneratorFactoryTest1 Failed");
  }

  /*
   * @testName: jsonGeneratorFactoryTest2
   * 
   * @assertion_ids: JSONP:JAVADOC:163; JSONP:JAVADOC:416; JSONP:JAVADOC:427;
   * 
   * @test_Strategy: Tests the JsonGeneratorFactory API.
   *
   * JsonGeneratorFactory generatorFactory =
   * Json.createGeneratorFactory(Map<String,?>); JsonGenerator generator1 =
   * generatorFactory.createGenerator(OutputStream, Charset) JsonGenerator
   * generator2 = generatorFactory.createGenerator(OutputStream, Charset)
   *
   * Create generator with both UTF-8 and UTF-16BE.
   */
  public void jsonGeneratorFactoryTest2() throws Fault {
    boolean pass = true;
    JsonGenerator generator1 = null;
    JsonGenerator generator2 = null;
    String expString, actString;
    try {
      logMsg(
          "Create JsonGeneratorFactory with Map<String, ?> with PRETTY_PRINTING config");
      JsonGeneratorFactory generatorFactory = Json
          .createGeneratorFactory(JSONP_Util.getPrettyPrintingConfig());
      logMsg("Checking factory configuration properties");
      Map<String, ?> config = generatorFactory.getConfigInUse();
      String[] props = { JsonGenerator.PRETTY_PRINTING, };
      if (!JSONP_Util.doConfigCheck(config, 1, props))
        pass = false;

      logMsg(
          "-----------------------------------------------------------------------");
      logMsg(
          "TEST CASE [JsonGeneratorFactory.createGenerator(OutputStream, Charset)]");
      logMsg(
          "-----------------------------------------------------------------------");
      logMsg(
          "Create 1st JsonGenerator using JsonGeneratorFactory with UTF-8 encoding");
      ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
      generator1 = generatorFactory.createGenerator(baos1, JSONP_Util.UTF_8);
      if (generator1 == null) {
        logErr("GeneratorFactory failed to create generator1");
        pass = false;
      } else {
        generator1.writeStartObject().writeEnd();
        generator1.close();
      }
      logMsg("baos1=" + baos1.toString("UTF-8"));
      expString = "{}";
      actString = JSONP_Util.removeWhitespace(baos1.toString("UTF-8"));
      if (!JSONP_Util.assertEqualsJsonText(expString, actString))
        pass = false;

      logMsg(
          "Create 2nd JsonGenerator using JsonGeneratorFactory with UTF-16BE encoding");
      ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
      generator2 = generatorFactory.createGenerator(baos2, JSONP_Util.UTF_16BE);
      if (generator2 == null) {
        logErr("GeneratorFactory failed to create generator2");
        pass = false;
      } else {
        generator2.writeStartArray().writeEnd();
        generator2.close();
      }
      logMsg("baos2=" + baos2.toString("UTF-16BE"));
      expString = "[]";
      actString = JSONP_Util.removeWhitespace(baos2.toString("UTF-16BE"));
      if (!JSONP_Util.assertEqualsJsonText(expString, actString))
        pass = false;

    } catch (Exception e) {
      throw new Fault("jsonGeneratorFactoryTest2 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonGeneratorFactoryTest2 Failed");
  }

  /*
   * @testName: jsonGeneratorFactoryTest3
   * 
   * @assertion_ids: JSONP:JAVADOC:200; JSONP:JAVADOC:416; JSONP:JAVADOC:427;
   * 
   * @test_Strategy: Tests the JsonGeneratorFactory API.
   *
   * JsonGeneratorFactory generatorFactory =
   * Json.createGeneratorFactory(Map<String, ?>); JsonGenerator generator1 =
   * generatorFactory.createGenerator(OutputStream) JsonGenerator generator2 =
   * generatorFactory.createGenerator(OutputStream)
   */
  public void jsonGeneratorFactoryTest3() throws Fault {
    boolean pass = true;
    JsonGenerator generator1 = null;
    JsonGenerator generator2 = null;
    String expString;
    String actString;
    try {
      logMsg(
          "Create JsonGeneratorFactory with Map<String, ?> with PRETTY_PRINTING config");
      JsonGeneratorFactory generatorFactory = Json
          .createGeneratorFactory(JSONP_Util.getPrettyPrintingConfig());
      logMsg("Checking factory configuration properties");
      Map<String, ?> config = generatorFactory.getConfigInUse();
      String[] props = { JsonGenerator.PRETTY_PRINTING, };
      if (!JSONP_Util.doConfigCheck(config, 1, props))
        pass = false;
      logMsg(
          "-----------------------------------------------------------------");
      logMsg(
          "TEST CASE [JsonGeneratorFactory.createGenerator(OutputStream os)]");
      logMsg(
          "-----------------------------------------------------------------");
      logMsg("Create 1st JsonGenerator using JsonGeneratorFactory");
      ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
      generator1 = generatorFactory.createGenerator(baos1);
      if (generator1 == null) {
        logErr("GeneratorFactory failed to create generator1");
        pass = false;
      } else {
        generator1.writeStartObject().writeEnd();
        generator1.close();
      }
      logMsg("baos1=" + baos1.toString("UTF-8"));
      expString = "{}";
      actString = JSONP_Util.removeWhitespace(baos1.toString("UTF-8"));
      if (!JSONP_Util.assertEqualsJsonText(expString, actString))
        pass = false;

      logMsg("Create 2nd JsonGenerator using JsonGeneratorFactory");
      ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
      generator2 = generatorFactory.createGenerator(baos2);
      if (generator2 == null) {
        logErr("GeneratorFactory failed to create generator2");
        pass = false;
      } else {
        generator2.writeStartArray().writeEnd();
        generator2.close();
      }
      logMsg("baos2=" + baos2.toString("UTF-8"));
      expString = "[]";
      actString = JSONP_Util.removeWhitespace(baos2.toString("UTF-8"));
      if (!JSONP_Util.assertEqualsJsonText(expString, actString))
        pass = false;

    } catch (Exception e) {
      throw new Fault("jsonGeneratorFactoryTest3 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonGeneratorFactoryTest3 Failed");
  }

  /*
   * @testName: jsonGeneratorFactoryTest4
   * 
   * @assertion_ids: JSONP:JAVADOC:416; JSONP:JAVADOC:427;
   * 
   * @test_Strategy: Tests the JsonGeneratorFactory API.
   *
   * JsonGeneratorFactory generatorFactory =
   * Json.createGeneratorFactory(Map<String, ?>); Map<String, ?> config =
   * JsonGeneratorFactory.getConfigInUse();
   *
   * Test for the following 3 scenarios: 1) no supported provider property
   * (empty config) 2) supported provider property 3) supported and non
   * supported provider property
   */
  public void jsonGeneratorFactoryTest4() throws Fault {
    boolean pass = true;
    JsonGeneratorFactory generatorFactory;
    Map<String, ?> config;
    try {
      logMsg("----------------------------------------------");
      logMsg("Test scenario1: no supported provider property");
      logMsg("----------------------------------------------");
      logMsg(
          "Create JsonGeneratorFactory with Map<String, ?> with EMPTY config");
      generatorFactory = Json
          .createGeneratorFactory(JSONP_Util.getEmptyConfig());
      config = generatorFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;

      logMsg("-------------------------------------------");
      logMsg("Test scenario2: supported provider property");
      logMsg("-------------------------------------------");
      logMsg(
          "Create JsonGeneratorFactory with Map<String, ?> with PRETTY_PRINTING config");
      generatorFactory = Json
          .createGeneratorFactory(JSONP_Util.getPrettyPrintingConfig());
      config = generatorFactory.getConfigInUse();
      String[] props = { JsonGenerator.PRETTY_PRINTING, };
      if (!JSONP_Util.doConfigCheck(config, 1, props))
        pass = false;

      logMsg("-------------------------------------------------------------");
      logMsg("Test scenario3: supported and non supported provider property");
      logMsg("-------------------------------------------------------------");
      logMsg("Create JsonGeneratorFactory with Map<String, ?> with all config");
      generatorFactory = Json.createGeneratorFactory(JSONP_Util.getAllConfig());
      config = generatorFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 1, props))
        pass = false;
    } catch (Exception e) {
      throw new Fault("jsonGeneratorFactoryTest4 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonGeneratorFactoryTest4 Failed");
  }
}
