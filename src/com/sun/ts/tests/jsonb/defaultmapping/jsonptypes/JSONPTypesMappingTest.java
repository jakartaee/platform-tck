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

package com.sun.ts.tests.jsonb.defaultmapping.jsonptypes;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jsonb.defaultmapping.jsonptypes.model.JsonArrayContainer;
import com.sun.ts.tests.jsonb.defaultmapping.jsonptypes.model.JsonNumberContainer;
import com.sun.ts.tests.jsonb.defaultmapping.jsonptypes.model.JsonObjectContainer;
import com.sun.ts.tests.jsonb.defaultmapping.jsonptypes.model.JsonStringContainer;
import com.sun.ts.tests.jsonb.defaultmapping.jsonptypes.model.JsonStructureContainer;
import com.sun.ts.tests.jsonb.defaultmapping.jsonptypes.model.JsonValueContainer;

import java.util.Properties;

/**
 * @test
 * @sources JSONPTypesMappingTest.java
 * @executeClass com.sun.ts.tests.jsonb.defaultmapping.jsonptypes.JSONPTypesMappingTest
 **/
public class JSONPTypesMappingTest extends ServiceEETest {
  private static final long serialVersionUID = 10L;

  private final Jsonb jsonb = JsonbBuilder.create();

  public static void main(String[] args) {
    EETest t = new JSONPTypesMappingTest();
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
   * @testName: testJsonObjectMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.20-1; JSONB:SPEC:JSB-3.20-2;
   * JSONB:SPEC:JSB-3.20-3
   *
   * @test_Strategy: Assert that JsonObject type is correctly handled
   */
  public Status testJsonObjectMapping() throws Fault {
    JsonObject instance = Json.createObjectBuilder()
        .add("jsonObjectInstance",
            Json.createObjectBuilder().add("innerInstance",
                "Inner Test String"))
        .add("jsonArrayInstance",
            Json.createArrayBuilder()
                .add(Json.createObjectBuilder().add("arrayInstance1",
                    "Array Test String 1"))
                .add(Json.createObjectBuilder().add("arrayInstance2",
                    "Array Test String 2")))
        .add("jsonStringInstance", "Test String")
        .add("jsonNumberInstance", Integer.MAX_VALUE)
        .add("jsonTrueInstance", JsonValue.TRUE)
        .add("jsonFalseInstance", JsonValue.FALSE)
        .add("jsonNullInstance", JsonValue.NULL).build();

    String jsonString = jsonb.toJson(new JsonObjectContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches("\\{\\s*\"instance\"\\s*:\\s*\\{"
        + "\\s*\"jsonObjectInstance\"\\s*:\\s*\\{\\s*\"innerInstance\"\\s*:\\s*\"Inner Test String\"\\s*\\}\\s*,"
        + "\\s*\"jsonArrayInstance\"\\s*:\\s*\\["
        + "\\s*\\{\\s*\"arrayInstance1\"\\s*:\\s*\"Array Test String 1\"\\s*\\}\\s*,"
        + "\\s*\\{\\s*\"arrayInstance2\"\\s*:\\s*\"Array Test String 2\"\\s*\\}\\s*\\]\\s*,"
        + "\\s*\"jsonStringInstance\"\\s*:\\s*\"Test String\"\\s*,"
        + "\\s*\"jsonNumberInstance\"\\s*:\\s*2147483647\\s*,"
        + "\\s*\"jsonTrueInstance\"\\s*:\\s*true\\s*,"
        + "\\s*\"jsonFalseInstance\"\\s*:\\s*false\\s*,"
        + "\\s*\"jsonNullInstance\"\\s*:\\s*null\\s*" + "\\}\\s*\\}")) {
      throw new Fault(
          "Failed to marshal object with JsonObject attribute value.");
    }

    JsonObjectContainer unmarshalledObject = jsonb
        .fromJson("{ \"instance\" : { "
            + "\"jsonObjectInstance\" : { \"innerInstance\" : \"Inner Test String\" }, "
            + "\"jsonArrayInstance\" : [ "
            + "{ \"arrayInstance1\" : \"Array Test String 1\" }, "
            + "{ \"arrayInstance2\" : \"Array Test String 2\" } ], "
            + "\"jsonStringInstance\" : \"Test String\", "
            + "\"jsonNumberInstance\" : 2147483647, "
            + "\"jsonTrueInstance\" : true, "
            + "\"jsonFalseInstance\" : false, " + "\"jsonNullInstance\" : null "
            + "} }", JsonObjectContainer.class);
    if (!instance.toString()
        .equals(unmarshalledObject.getInstance().toString())) {
      throw new Fault(
          "Failed to unmarshal object with JsonObject attribute value.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testEmptyJsonObjectMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.20-1; JSONB:SPEC:JSB-3.20-2;
   * JSONB:SPEC:JSB-3.20-3
   *
   * @test_Strategy: Assert that empty JsonObject is correctly handled
   */
  public Status testEmptyJsonObjectMapping() throws Fault {
    JsonObject instance = Json.createObjectBuilder().build();
    String jsonString = jsonb.toJson(new JsonObjectContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches("\\{\\s*\"instance\"\\s*:\\s*\\{\\s*\\}\\s*\\}")) {
      throw new Fault(
          "Failed to marshal object with empty JsonObject attribute value.");
    }

    JsonObjectContainer unmarshalledObject = jsonb
        .fromJson("{ \"instance\" : { } }", JsonObjectContainer.class);
    if (!instance.toString()
        .equals(unmarshalledObject.getInstance().toString())) {
      throw new Fault(
          "Failed to unmarshal object with empty JsonObject attribute value.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testJsonArrayMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.20-1; JSONB:SPEC:JSB-3.20-2;
   * JSONB:SPEC:JSB-3.20-3
   *
   * @test_Strategy: Assert that JsonArray type is correctly handled
   */
  public Status testJsonArrayMapping() throws Fault {
    JsonArray instance = Json.createArrayBuilder()
        .add(Json.createObjectBuilder().add("arrayInstance1",
            "Array Test String 1"))
        .add(Json.createObjectBuilder().add("arrayInstance2",
            "Array Test String 2"))
        .build();

    String jsonString = jsonb.toJson(new JsonArrayContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches("\\{\\s*\"instance\"\\s*:\\s*\\["
        + "\\s*\\{\\s*\"arrayInstance1\"\\s*:\\s*\"Array Test String 1\"\\s*\\}\\s*,"
        + "\\s*\\{\\s*\"arrayInstance2\"\\s*:\\s*\"Array Test String 2\"\\s*\\}\\s*"
        + "\\]\\s*\\}")) {
      throw new Fault(
          "Failed to marshal object with JsonArray attribute value.");
    }

    JsonArrayContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : [ "
            + "{ \"arrayInstance1\" : \"Array Test String 1\" }, "
            + "{ \"arrayInstance2\" : \"Array Test String 2\" } " + "] }",
        JsonArrayContainer.class);
    if (!instance.toString()
        .equals(unmarshalledObject.getInstance().toString())) {
      throw new Fault(
          "Failed to unmarshal object with JsonArray attribute value.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testEmptyJsonArrayMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.20-1; JSONB:SPEC:JSB-3.20-2;
   * JSONB:SPEC:JSB-3.20-3
   *
   * @test_Strategy: Assert that empty JsonArray is correctly handled
   */
  public Status testEmptyJsonArrayMapping() throws Fault {
    JsonArray instance = Json.createArrayBuilder().build();
    String jsonString = jsonb.toJson(new JsonArrayContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches("\\{\\s*\"instance\"\\s*:\\s*\\[\\s*\\]\\s*\\}")) {
      throw new Fault(
          "Failed to marshal object with empty JsonArray attribute value.");
    }

    JsonArrayContainer unmarshalledObject = jsonb
        .fromJson("{ \"instance\" : [ ] }", JsonArrayContainer.class);
    if (!instance.toString()
        .equals(unmarshalledObject.getInstance().toString())) {
      throw new Fault(
          "Failed to unmarshal object with empty JsonArray attribute value.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testJsonObjectStructureMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.20-1; JSONB:SPEC:JSB-3.20-2;
   * JSONB:SPEC:JSB-3.20-3
   *
   * @test_Strategy: Assert that JsonStructure of JsonObject is correctly
   * handled
   */
  public Status testJsonObjectStructureMapping() throws Fault {
    JsonStructure instance = Json.createObjectBuilder()
        .add("jsonObjectInstance",
            Json.createObjectBuilder().add("innerInstance",
                "Inner Test String"))
        .add("jsonArrayInstance",
            Json.createArrayBuilder()
                .add(Json.createObjectBuilder().add("arrayInstance1",
                    "Array Test String 1"))
                .add(Json.createObjectBuilder().add("arrayInstance2",
                    "Array Test String 2")))
        .add("jsonStringInstance", "Test String")
        .add("jsonNumberInstance", Integer.MAX_VALUE)
        .add("jsonTrueInstance", JsonValue.TRUE)
        .add("jsonFalseInstance", JsonValue.FALSE)
        .add("jsonNullInstance", JsonValue.NULL).build();

    String jsonString = jsonb.toJson(new JsonStructureContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches("\\{\\s*\"instance\"\\s*:\\s*\\{"
        + "\\s*\"jsonObjectInstance\"\\s*:\\s*\\{\\s*\"innerInstance\"\\s*:\\s*\"Inner Test String\"\\s*\\}\\s*,"
        + "\\s*\"jsonArrayInstance\"\\s*:\\s*\\["
        + "\\s*\\{\\s*\"arrayInstance1\"\\s*:\\s*\"Array Test String 1\"\\s*\\}\\s*,"
        + "\\s*\\{\\s*\"arrayInstance2\"\\s*:\\s*\"Array Test String 2\"\\s*\\}\\s*\\]\\s*,"
        + "\\s*\"jsonStringInstance\"\\s*:\\s*\"Test String\"\\s*,"
        + "\\s*\"jsonNumberInstance\"\\s*:\\s*2147483647\\s*,"
        + "\\s*\"jsonTrueInstance\"\\s*:\\s*true\\s*,"
        + "\\s*\"jsonFalseInstance\"\\s*:\\s*false\\s*,"
        + "\\s*\"jsonNullInstance\"\\s*:\\s*null\\s*" + "\\}\\s*\\}")) {
      throw new Fault(
          "Failed to marshal object with JsonObject JsonStructure attribute value.");
    }

    JsonStructureContainer unmarshalledObject = jsonb
        .fromJson("{ \"instance\" : { "
            + "\"jsonObjectInstance\" : { \"innerInstance\" : \"Inner Test String\" }, "
            + "\"jsonArrayInstance\" : [ "
            + "{ \"arrayInstance1\" : \"Array Test String 1\" }, "
            + "{ \"arrayInstance2\" : \"Array Test String 2\" } ], "
            + "\"jsonStringInstance\" : \"Test String\", "
            + "\"jsonNumberInstance\" : 2147483647, "
            + "\"jsonTrueInstance\" : true, "
            + "\"jsonFalseInstance\" : false, " + "\"jsonNullInstance\" : null "
            + "} }", JsonStructureContainer.class);
    if (!instance.toString()
        .equals(unmarshalledObject.getInstance().toString())) {
      throw new Fault(
          "Failed to unmarshal object with JsonObject JsonStructure attribute value.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testJsonArrayStructureMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.20-1; JSONB:SPEC:JSB-3.20-2;
   * JSONB:SPEC:JSB-3.20-3
   *
   * @test_Strategy: Assert that JsonStructure of JsonArray is correctly handled
   */
  public Status testJsonArrayStructureMapping() throws Fault {
    JsonStructure instance = Json.createArrayBuilder()
        .add(Json.createObjectBuilder().add("arrayInstance1",
            "Array Test String 1"))
        .add(Json.createObjectBuilder().add("arrayInstance2",
            "Array Test String 2"))
        .build();

    String jsonString = jsonb.toJson(new JsonStructureContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches("\\{\\s*\"instance\"\\s*:\\s*\\["
        + "\\s*\\{\\s*\"arrayInstance1\"\\s*:\\s*\"Array Test String 1\"\\s*\\}\\s*,"
        + "\\s*\\{\\s*\"arrayInstance2\"\\s*:\\s*\"Array Test String 2\"\\s*\\}\\s*"
        + "\\]\\s*\\}")) {
      throw new Fault(
          "Failed to marshal object with JsonArray JsonStructure attribute value.");
    }

    JsonStructureContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : [ "
            + "{ \"arrayInstance1\" : \"Array Test String 1\" }, "
            + "{ \"arrayInstance2\" : \"Array Test String 2\" } " + "] }",
        JsonStructureContainer.class);
    if (!instance.toString()
        .equals(unmarshalledObject.getInstance().toString())) {
      throw new Fault(
          "Failed to unmarshal object with JsonArray JsonStructure attribute value.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testJsonValueMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.20-1; JSONB:SPEC:JSB-3.20-2;
   * JSONB:SPEC:JSB-3.20-3
   *
   * @test_Strategy: Assert that JsonValue type is correctly handled
   */
  public Status testJsonValueMapping() throws Fault {
    JsonValue instance = Json.createObjectBuilder()
        .add("stringInstance", "Test String").build();
    String jsonString = jsonb.toJson(new JsonValueContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\{\\s*\"stringInstance\"\\s*:\\s*\"Test String\"\\s*\\}\\s*\\}")) {
      throw new Fault(
          "Failed to marshal object with JsonValue attribute value.");
    }

    JsonValueContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : { \"stringInstance\" : \"Test String\" } }",
        JsonValueContainer.class);
    if (!instance.toString()
        .equals(unmarshalledObject.getInstance().toString())) {
      throw new Fault(
          "Failed to unmarshal object with JsonValue attribute value.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testJsonStringMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.20-1; JSONB:SPEC:JSB-3.20-2;
   * JSONB:SPEC:JSB-3.20-3
   *
   * @test_Strategy: Assert that JsonString type is correctly handled
   */
  public Status testJsonStringMapping() throws Fault {
    JsonString instance = Json.createObjectBuilder()
        .add("stringInstance", "Test String").build()
        .getJsonString("stringInstance");
    String jsonString = jsonb.toJson(new JsonStringContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString
        .matches("\\{\\s*\"instance\"\\s*:\\s*\"Test String\"\\s*\\}")) {
      throw new Fault(
          "Failed to marshal object with JsonString attribute value.");
    }

    JsonStringContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : \"Test String\" }", JsonStringContainer.class);
    if (!instance.toString()
        .equals(unmarshalledObject.getInstance().toString())) {
      throw new Fault(
          "Failed to unmarshal object with JsonString attribute value.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testJsonNumberMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.20-1; JSONB:SPEC:JSB-3.20-2;
   * JSONB:SPEC:JSB-3.20-3
   *
   * @test_Strategy: Assert that JsonNumber type is correctly handled
   */
  public Status testJsonNumberMapping() throws Fault {
    JsonNumber instance = Json.createObjectBuilder().add("intInstance", 0)
        .build().getJsonNumber("intInstance");
    String jsonString = jsonb.toJson(new JsonNumberContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches("\\{\\s*\"instance\"\\s*:\\s*0\\s*\\}")) {
      throw new Fault(
          "Failed to marshal object with JsonNumber attribute value.");
    }

    JsonNumberContainer unmarshalledObject = jsonb
        .fromJson("{ \"instance\" : 0 }", JsonNumberContainer.class);
    if (!instance.toString()
        .equals(unmarshalledObject.getInstance().toString())) {
      throw new Fault(
          "Failed to unmarshal object with JsonNumber attribute value.");
    }

    return Status.passed("OK");
  }
}
