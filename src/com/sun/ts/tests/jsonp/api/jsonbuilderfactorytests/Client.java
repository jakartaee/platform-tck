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
package com.sun.ts.tests.jsonp.api.jsonbuilderfactorytests;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jsonp.api.common.TestResult;
import com.sun.ts.tests.jsonp.common.*;
import java.util.Map;
import java.util.Properties;
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
   * @testName: jsonBuilderFactoryTest1
   * 
   * @assertion_ids: JSONP:JAVADOC:445; JSONP:JAVADOC:453; JSONP:JAVADOC:454;
   * JSONP:JAVADOC:455;
   * 
   * @test_Strategy: Tests the JsonBuilderFactory API.
   *
   * JsonBuilderFactory builderFactory = Json.createBuilderFactory(Map<String,
   * ?>); JsonArray array = builderFactory.createArrayBuilder() JsonObject
   * object = builderFactory.createObjectBuilder()
   */
  public void jsonBuilderFactoryTest1() throws Fault {
    boolean pass = true;
    try {
      logMsg("Create JsonBuilderFactory with Map<String, ?> with EMPTY config");
      JsonBuilderFactory builderFactory = Json
          .createBuilderFactory(JSONP_Util.getEmptyConfig());
      logMsg("Checking factory configuration properties");
      Map<String, ?> config = builderFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;

      logMsg("---------------------------------------------------");
      logMsg("TEST CASE [JsonBuilderFactory.createArrayBuilder()]");
      logMsg("---------------------------------------------------");
      logMsg("Create JsonArrayBuilder using JsonBuilderFactory");
      JsonArray expJsonArray = JSONP_Util.createJsonArrayFromString("[0,2]");
      JsonArray actJsonArray = builderFactory.createArrayBuilder().add(0).add(2)
          .build();
      if (!JSONP_Util.assertEqualsJsonArrays(expJsonArray, actJsonArray))
        pass = false;

      logMsg("----------------------------------------------------");
      logMsg("TEST CASE [JsonBuilderFactory.createObjectBuilder()]");
      logMsg("----------------------------------------------------");
      logMsg("Create JsonObjectBuilder using JsonBuilderFactory");
      JsonObject expJsonObject = JSONP_Util
          .createJsonObjectFromString("{\"foo\":\"bar\"}");
      JsonObject actJsonObject = builderFactory.createObjectBuilder()
          .add("foo", "bar").build();
      if (!JSONP_Util.assertEqualsJsonObjects(expJsonObject, actJsonObject))
        pass = false;

    } catch (Exception e) {
      throw new Fault("jsonBuilderFactoryTest1 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonBuilderFactoryTest1 Failed");
  }

  /*
   * @testName: jsonBuilderFactoryTest2
   * 
   * @assertion_ids: JSONP:JAVADOC:445; JSONP:JAVADOC:455;
   * 
   * @test_Strategy: Tests the JsonBuilderFactory API.
   *
   * JsonBuilderFactory builderFactory = Json.createBuilderFactory(Map<String,
   * ?>); Map<String, ?> config = JsonBuilderFactory.getConfigInUse();
   *
   * Test for the following 3 scenarios: 1) no supported provider property
   * (empty config) 2) non supported provider property
   */
  public void jsonBuilderFactoryTest2() throws Fault {
    boolean pass = true;
    JsonBuilderFactory builderFactory;
    Map<String, ?> config;
    try {
      logMsg("----------------------------------------------");
      logMsg("Test scenario1: no supported provider property");
      logMsg("----------------------------------------------");
      logMsg("Create JsonBuilderFactory with Map<String, ?> with EMPTY config");
      builderFactory = Json.createBuilderFactory(JSONP_Util.getEmptyConfig());
      config = builderFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;

      logMsg("-----------------------------------------------");
      logMsg("Test scenario2: non supported provider property");
      logMsg("-----------------------------------------------");
      logMsg("Create JsonBuilderFactory with Map<String, ?> with FOO config");
      builderFactory = Json.createBuilderFactory(JSONP_Util.getFooConfig());
      config = builderFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;
    } catch (Exception e) {
      throw new Fault("jsonBuilderFactoryTest2 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonBuilderFactoryTest2 Failed");
  }

  /*
   * @testName: jsonBuilderFactory11Test
   * 
   * @assertion_ids: JSONP:JAVADOC:614; JSONP:JAVADOC:615;
   * 
   * @test_Strategy: Tests JsonBuilderFactory API methods added in JSON-P 1.1.
   */
  public void jsonBuilderFactory11Test() throws Fault {
    BuilderFactory factoryTest = new BuilderFactory();
    final TestResult result = factoryTest.test();
    result.eval();
  }

}
