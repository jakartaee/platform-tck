/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsonb.api.builder;

import java.util.Properties;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.spi.JsonbProvider;
import javax.json.spi.JsonProvider;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jsonb.api.model.SimpleContainer;

/**
 * @test
 * @sources JsonbBuilderTest.java
 * @executeClass com.sun.ts.tests.jsonb.api.JsonbBuilderTest
 **/
public class JsonbBuilderTest extends ServiceEETest {
  private static final long serialVersionUID = 10L;

  public static void main(String[] args) {
    EETest t = new JsonbBuilderTest();
    Status s = t.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: testBuild
   *
   * @assertion_ids: JSONB:JAVADOC:25
   *
   * @test_Strategy: Assert that JsonbBuilder.build returns a new instance of
   * javax.json.bind.Jsonb
   */
  public Status testBuild() throws Fault {
    Jsonb jsonb = JsonbBuilder.newBuilder().build();
    if (jsonb == null) {
      throw new Fault(
          "Failed to create a new Jsonb instance using JsonBuilder.build method.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testCreate
   *
   * @assertion_ids: JSONB:JAVADOC:26
   *
   * @test_Strategy: Assert that JsonbBuilder.create returns a new instance of
   * javax.json.bind.Jsonb
   */
  public void testCreate() throws Fault {
    Jsonb jsonb = JsonbBuilder.create();
    if (jsonb == null) {
      throw new Fault(
          "Failed to create a new Jsonb instance using JsonBuilder.create method.");
    }
  }

  /*
   * @testName: testCreateConfig
   *
   * @assertion_ids: JSONB:JAVADOC:27
   *
   * @test_Strategy: Assert that JsonbBuilder.create method with Config argument
   * returns a new instance of javax.json.bind.Jsonb configured with provided
   * configuration
   */
  public void testCreateConfig() throws Fault {
    JsonbConfig jsonbConfig = new JsonbConfig().withNullValues(true);
    Jsonb jsonb = JsonbBuilder.create(jsonbConfig);

    String jsonString = jsonb.toJson(new SimpleContainer() {
      {
        setInstance(null);
      }
    });
    if (!jsonString.matches("\\{\\s*\"instance\"\\s*:\\s*null\\s*}")) {
      throw new Fault(
          "Failed to create a new Jsonb instance using JsonBuilder.create method with Config argument configured with provided configuration.");
    }
  }

  /*
   * @testName: testNewBuilder
   *
   * @assertion_ids: JSONB:JAVADOC:28; JSONB:JAVADOC:80
   *
   * @test_Strategy: Assert that JsonbBuilder.newBuilder returns a new
   * JsonbBuilder instance as returned by
   * javax.json.bind.spi.JsonbProvider#provider method
   */
  public void testNewBuilder() throws Fault {
    JsonbBuilder jsonbBuilder = JsonbBuilder.newBuilder();
    if (jsonbBuilder == null || !jsonbBuilder.getClass()
        .equals(JsonbProvider.provider().create().getClass())) {
      throw new Fault(
          "Failed to create a new Jsonb instance as returned by javax.json.bind.spi.JsonbProvider#provider method using JsonBuilder.newBuilder method.");
    }
  }

  /*
   * @testName: testNewBuilderString
   *
   * @assertion_ids: JSONB:JAVADOC:29; JSONB:JAVADOC:80; JSONB:JAVADOC:81
   *
   * @test_Strategy: Assert that JsonbBuilder.newBuilder method with String
   * argument returns a new JsonbBuilder instance as returned by
   * javax.json.bind.spi.JsonbProvider#provider(String) method
   */
  public void testNewBuilderString() throws Fault {
    JsonbBuilder jsonbBuilder = JsonbBuilder
        .newBuilder(JsonbProvider.provider().getClass().getName());
    if (jsonbBuilder == null || !jsonbBuilder.getClass()
        .equals(JsonbProvider
            .provider(JsonbProvider.provider().getClass().getName()).create()
            .getClass())) {
      throw new Fault(
          "Failed to create a new Jsonb instance as returned by javax.json.bind.spi.JsonbProvider#provider(String) method using JsonBuilder.newBuilder method with String argument.");
    }
  }

  /*
   * @testName: testNewBuilderProvider
   *
   * @assertion_ids: JSONB:JAVADOC:30; JSONB:JAVADOC:78; JSONB:JAVADOC:80
   *
   * @test_Strategy: Assert that JsonbBuilder.newBuilder method with
   * JsonbProvider argument returns a new JsonbBuilder instance as returned by
   * provider#create method
   */
  public void testNewBuilderProvider() throws Fault {
    JsonbBuilder jsonbBuilder = JsonbBuilder
        .newBuilder(JsonbProvider.provider());
    if (jsonbBuilder == null || !jsonbBuilder.getClass()
        .equals(JsonbProvider.provider().create().getClass())) {
      throw new Fault(
          "Failed to create a new Jsonb instance as returned by provider#create method using JsonBuilder.newBuilder method with JsonbProvider argument.");
    }
  }

  /*
   * @testName: testWithConfig
   *
   * @assertion_ids: JSONB:JAVADOC:31
   *
   * @test_Strategy: Assert that JsonbBuilder.withConfig method with JsonbConfig
   * argument returns a new JsonbBuilder instance configured with configuration
   * passed as an argument
   */
  public void testWithConfig() throws Fault {
    JsonbConfig jsonbConfig = new JsonbConfig().withNullValues(true);
    Jsonb jsonb = JsonbBuilder.newBuilder().withConfig(jsonbConfig).build();

    String jsonString = jsonb.toJson(new SimpleContainer() {
      {
        setInstance(null);
      }
    });
    if (!jsonString.matches("\\{\\s*\"instance\"\\s*:\\s*null\\s*}")) {
      throw new Fault(
          "Failed to apply configuration passed as an argument to JsonbBuilder.withConfig method.");
    }
  }

  /*
   * @testName: testWithProvider
   *
   * @assertion_ids: JSONB:JAVADOC:32; JSONB:JAVADOC:80
   *
   * @test_Strategy: Assert that JsonbBuilder.withProvider method with
   * JsonProvider argument returns a new JsonbBuilder instance using the
   * JsonProvider passed as an argument
   */
  public void testWithProvider() throws Fault {
    JsonbBuilder jsonbBuilder = JsonbBuilder.newBuilder()
        .withProvider(JsonProvider.provider());
    if (jsonbBuilder == null) {
      throw new Fault(
          "Failed to create a new JsonbBuilder instance using a specific JsonProvider using JsonbBuilder.withProvider method.");
    }
  }
}
