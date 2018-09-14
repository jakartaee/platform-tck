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

package com.sun.ts.tests.jsonb.customizedmapping.propertyorder;

import java.util.Properties;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.config.PropertyOrderStrategy;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jsonb.customizedmapping.propertyorder.model.CustomOrderContainer;
import com.sun.ts.tests.jsonb.customizedmapping.propertyorder.model.PartialOrderContainer;
import com.sun.ts.tests.jsonb.customizedmapping.propertyorder.model.RenamedPropertiesContainer;
import com.sun.ts.tests.jsonb.customizedmapping.propertyorder.model.SimpleContainer;
import com.sun.ts.tests.jsonb.customizedmapping.propertyorder.model.SimpleOrderContainer;

/**
 * @test
 * @sources PropertyOrderCustomizationTest.java
 * @executeClass com.sun.ts.tests.jsonb.customizedmapping.propertyorder.PropertyOrderCustomizationTest
 **/
public class PropertyOrderCustomizationTest extends ServiceEETest {
  private static final long serialVersionUID = 10L;

  private final Jsonb jsonb = JsonbBuilder.create();

  public static void main(String[] args) {
    EETest t = new PropertyOrderCustomizationTest();
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
   * @testName: testAnyPropertyOrderStrategy
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.2
   *
   * @test_Strategy: Assert that no error occurs when using
   * PropertyOrderStrategy.ANY
   */
  public Status testAnyPropertyOrderStrategy() throws Fault {
    JsonbConfig config = new JsonbConfig();
    config.setProperty(JsonbConfig.PROPERTY_ORDER_STRATEGY,
        PropertyOrderStrategy.ANY);
    Jsonb jsonb = JsonbBuilder.create(config);

    String jsonString = jsonb.toJson(new SimpleContainer() {
      {
        setStringInstance("Test String");
      }
    });
    SimpleContainer unmarshalledObject = jsonb.fromJson(jsonString,
        SimpleContainer.class);
    if (!("Test String".equals(unmarshalledObject.getStringInstance())
        && unmarshalledObject.getIntInstance() == 0
        && unmarshalledObject.getLongInstance() == 0)) {
      throw new Fault(
          "Failed to correctly marshal and unmarshal object using PropertyOrderStrategy.ANY.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testLexicographicalPropertyOrderStrategy
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.2
   *
   * @test_Strategy: Assert that marshalling property order is lexicographical
   * when using PropertyOrderStrategy.LEXICOGRAPHICAL and unmarshalling property
   * order is the order of appearance in the JSON document
   */
  public Status testLexicographicalPropertyOrderStrategy() throws Fault {
    JsonbConfig config = new JsonbConfig();
    config.setProperty(JsonbConfig.PROPERTY_ORDER_STRATEGY,
        PropertyOrderStrategy.LEXICOGRAPHICAL);
    Jsonb jsonb = JsonbBuilder.create(config);

    String jsonString = jsonb.toJson(new SimpleOrderContainer() {
      {
        setStringInstance("Test String");
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"intInstance\"\\s*\\:\\s*0\\s*,\\s*\"longInstance\"\\s*\\:\\s*0\\s*,\\s*\"stringInstance\"\\s*\\:\\s*\"Test String\"\\s*\\}")) {
      throw new Fault(
          "Failed to correctly marshal properties in lexicographical order using PropertyOrderStrategy.LEXICOGRAPHICAL.");
    }

    SimpleOrderContainer unmarshalledObject = jsonb.fromJson(
        "{ \"intInstance\" : 1, \"stringInstance\" : \"Test String\", \"longInstance\" : 1 }",
        SimpleOrderContainer.class);
    if (unmarshalledObject.getIntInstance() != 3) {
      throw new Fault(
          "Failed to correctly unmarshal properties in order of appearance using PropertyOrderStrategy.LEXICOGRAPHICAL.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testReversePropertyOrderStrategy
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.2
   *
   * @test_Strategy: Assert that marshalling property order is reverse
   * lexicographical when using PropertyOrderStrategy.REVERSE and unmarshalling
   * property order is the order of appearance in the JSON document
   */
  public Status testReversePropertyOrderStrategy() throws Fault {
    JsonbConfig config = new JsonbConfig();
    config.setProperty(JsonbConfig.PROPERTY_ORDER_STRATEGY,
        PropertyOrderStrategy.REVERSE);
    Jsonb jsonb = JsonbBuilder.create(config);

    String jsonString = jsonb.toJson(new SimpleOrderContainer() {
      {
        setStringInstance("Test String");
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"stringInstance\"\\s*\\:\\s*\"Test String\"\\s*,\\s*\"longInstance\"\\s*\\:\\s*0\\s*,\\s*\"intInstance\"\\s*\\:\\s*0\\s*\\}")) {
      throw new Fault(
          "Failed to correctly marshal properties in reverse lexicographical order using PropertyOrderStrategy.REVERSE.");
    }

    SimpleOrderContainer unmarshalledObject = jsonb.fromJson(
        "{ \"intInstance\" : 1, \"stringInstance\" : \"Test String\", \"longInstance\" : 1 }",
        SimpleOrderContainer.class);
    if (unmarshalledObject.getIntInstance() != 3) {
      throw new Fault(
          "Failed to correctly unmarshal properties in order of appearance using PropertyOrderStrategy.REVERSE.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testCustomPropertyOrder
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.2-2
   *
   * @test_Strategy: Assert that marshalling property order is as specified by
   * JsonbPropertyOrder annotation and unmarshalling property order is the order
   * of appearance in the JSON document
   */
  public Status testCustomPropertyOrder() throws Fault {
    String jsonString = jsonb.toJson(new CustomOrderContainer() {
      {
        setStringInstance("Test String");
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"longInstance\"\\s*\\:\\s*0\\s*,\\s*\"intInstance\"\\s*\\:\\s*0\\s*,\\s*\"stringInstance\"\\s*\\:\\s*\"Test String\"\\s*\\}")) {
      throw new Fault(
          "Failed to correctly marshal properties in custom order using JsonbPropertyOrder annotation.");
    }

    CustomOrderContainer unmarshalledObject = jsonb.fromJson(
        "{ \"intInstance\" : 1, \"stringInstance\" : \"Test String\", \"longInstance\" : 0 }",
        CustomOrderContainer.class);
    if (unmarshalledObject.getIntInstance() != 3) {
      throw new Fault(
          "Failed to correctly unmarshal properties in order of appearance using JsonbPropertyOrder annotation.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testCustomPropertyOrderStrategyOverride
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.3-1
   *
   * @test_Strategy: Assert that marshalling property order is as specified by
   * JsonbPropertyOrder annotation regardless of PropertyOrderStrategy specified
   * and unmarshalling property order is the order of appearance in the JSON
   * document
   */
  public Status testCustomPropertyOrderStrategyOverride() throws Fault {
    JsonbConfig config = new JsonbConfig();
    config.setProperty(JsonbConfig.PROPERTY_ORDER_STRATEGY,
        PropertyOrderStrategy.REVERSE);
    Jsonb jsonb = JsonbBuilder.create(config);

    String jsonString = jsonb.toJson(new CustomOrderContainer() {
      {
        setStringInstance("Test String");
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"longInstance\"\\s*\\:\\s*0\\s*,\\s*\"intInstance\"\\s*\\:\\s*0\\s*,\\s*\"stringInstance\"\\s*\\:\\s*\"Test String\"\\s*\\}")) {
      throw new Fault(
          "Failed to correctly marshal properties in custom order using JsonbPropertyOrder annotation.");
    }

    CustomOrderContainer unmarshalledObject = jsonb.fromJson(
        "{ \"intInstance\" : 1, \"stringInstance\" : \"Test String\", \"longInstance\" : 0 }",
        CustomOrderContainer.class);
    if (unmarshalledObject.getIntInstance() != 3) {
      throw new Fault(
          "Failed to correctly unmarshal properties in order of appearance using JsonbPropertyOrder annotation and PropertyOrderStrategy.REVERSE.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testCustomPartialPropertyOrder
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.2-2
   *
   * @test_Strategy: In that case, properties included in annotation declaration
   * will be serialized first (in defined order), followed by any properties not
   * included in the definition. The order of properties not included in the
   * definition is not guaranteed
   */
  public void testCustomPartialPropertyOrder() throws Fault {
    String jsonString = jsonb.toJson(new PartialOrderContainer() {
      {
        setStringInstance("Test String");
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"longInstance\"\\s*\\:\\s*0\\s*,\\s*\"intInstance\"\\s*\\:\\s*0\\s*,\\s*\"stringInstance\"\\s*\\:\\s*\"Test String\".*\\}")) {
      System.out.append("Got JSON: ").println(jsonString);
      throw new Fault(
          "Failed to correctly marshal properties in custom order using JsonbPropertyOrder annotation.");
    }
    if (!jsonString.contains("anotherIntInstance")
        || !jsonString.contains("anIntInstance")
        || !jsonString.contains("yetAnotherIntInstance")) {
      System.out.append("Got JSON: ").println(jsonString);
      throw new Fault("Did not marshall all expected properties");
    }

    PartialOrderContainer unmarshalledObject = jsonb.fromJson(
        "{ \"anIntInstance\" : 100, \"yetAnotherIntInstance\":100, \"anotherIntInstance\": 100, \"intInstance\" : 1, \"stringInstance\" : \"Test String\", \"longInstance\" : 0 }",
        PartialOrderContainer.class);
    if (unmarshalledObject.getIntInstance() != 3) {
      System.out.append("Got Int instance: ")
          .println(unmarshalledObject.getIntInstance());
      throw new Fault(
          "Failed to correctly unmarshal properties in order of appearance using JsonbPropertyOrder annotation.");
    }
    if (unmarshalledObject.getAnotherIntInstance() != 100) {
      System.out.append("Got AnotherInt instance: ")
          .println(unmarshalledObject.getAnotherIntInstance());
      throw new Fault(
          "Failed to correctly unmarshal properties in order of appearance using JsonbPropertyOrder annotation.");
    }
    if (unmarshalledObject.getYetAnotherIntInstance() != 100) {
      System.out.append("Got YetAnotherInt instance: ")
          .println(unmarshalledObject.getYetAnotherIntInstance());
      throw new Fault(
          "Failed to correctly unmarshal properties in order of appearance using JsonbPropertyOrder annotation.");
    }
    if (unmarshalledObject.getAnIntInstance() != 100) {
      System.out.append("Got AnInt instance: ")
          .println(unmarshalledObject.getAnIntInstance());
      throw new Fault(
          "Failed to correctly unmarshal properties in order of appearance using JsonbPropertyOrder annotation.");
    }
  }

  /*
   * @testName: testCustomPartialPropertyOrderStrategyOverride
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.3-1
   *
   * @test_Strategy: In that case, properties included in annotation declaration
   * will be serialized first (in defined order), followed by any properties not
   * included in the definition. The order of properties not included in the
   * definition is not guaranteed
   */
  public void testCustomPartialPropertyOrderStrategyOverride() throws Fault {
    JsonbConfig config = new JsonbConfig();
    config.setProperty(JsonbConfig.PROPERTY_ORDER_STRATEGY,
        PropertyOrderStrategy.REVERSE);
    Jsonb jsonb = JsonbBuilder.create(config);

    String jsonString = jsonb.toJson(new PartialOrderContainer() {
      {
        setStringInstance("Test String");
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"longInstance\"\\s*\\:\\s*0\\s*,\\s*\"intInstance\"\\s*\\:\\s*0\\s*,\\s*\"stringInstance\"\\s*\\:\\s*\"Test String\".*\\}")) {
      System.out.append("Got JSON: ").println(jsonString);
      throw new Fault(
          "Failed to correctly marshal properties in custom order using JsonbPropertyOrder annotation.");
    }
    if (!jsonString.contains("anotherIntInstance")
        || !jsonString.contains("anIntInstance")
        || !jsonString.contains("yetAnotherIntInstance")) {
      System.out.append("Got JSON: ").println(jsonString);
      throw new Fault("Did not marshall all expected properties");
    }

    PartialOrderContainer unmarshalledObject = jsonb.fromJson(
        "{ \"anIntInstance\" : 100, \"yetAnotherIntInstance\":100, \"anotherIntInstance\": 100, \"intInstance\" : 1, \"stringInstance\" : \"Test String\", \"longInstance\" : 0 }",
        PartialOrderContainer.class);
    if (unmarshalledObject.getIntInstance() != 3) {
      System.out.append("Got Int instance: ")
          .println(unmarshalledObject.getIntInstance());
      throw new Fault(
          "Failed to correctly unmarshal properties in order of appearance using JsonbPropertyOrder annotation and PropertyOrderStrategy.REVERSE.");
    }
    if (unmarshalledObject.getAnotherIntInstance() != 100) {
      System.out.append("Got AnotherInt instance: ")
          .println(unmarshalledObject.getAnotherIntInstance());
      throw new Fault(
          "Failed to correctly unmarshal properties in order of appearance using JsonbPropertyOrder annotation.");
    }
    if (unmarshalledObject.getYetAnotherIntInstance() != 100) {
      System.out.append("Got YetAnotherInt instance: ")
          .println(unmarshalledObject.getYetAnotherIntInstance());
      throw new Fault(
          "Failed to correctly unmarshal properties in order of appearance using JsonbPropertyOrder annotation.");
    }
    if (unmarshalledObject.getAnIntInstance() != 100) {
      System.out.append("Got AnInt instance: ")
          .println(unmarshalledObject.getAnIntInstance());
      throw new Fault(
          "Failed to correctly unmarshal properties in order of appearance using JsonbPropertyOrder annotation.");
    }
  }

  /*
   * @testName: testLexicographicalPropertyOrderRenamedProperties
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.2
   *
   * @test_Strategy: Assert that marshalling property order is lexicographical
   * after property renaming has been applied and unmarshalling property order
   * is the order of appearance in the JSON document
   */
  public Status testLexicographicalPropertyOrderRenamedProperties()
      throws Fault {
    JsonbConfig config = new JsonbConfig();
    config.setProperty(JsonbConfig.PROPERTY_ORDER_STRATEGY,
        PropertyOrderStrategy.LEXICOGRAPHICAL);
    Jsonb jsonb = JsonbBuilder.create(config);

    String jsonString = jsonb.toJson(new RenamedPropertiesContainer() {
      {
        setStringInstance("Test String");
        setLongInstance(1);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"first\"\\s*\\:\\s*0\\s*,\\s*\"second\"\\s*\\:\\s*\"Test String\"\\s*,\\s*\"third\"\\s*\\:\\s*1\\s*\\}")) {
      throw new Fault(
          "Failed to correctly marshal renamed properties in lexicographical order using PropertyOrderStrategy.LEXICOGRAPHICAL.");
    }

    RenamedPropertiesContainer unmarshalledObject = jsonb.fromJson(
        "{ \"first\" : 1, \"second\" : \"Test String\", \"third\" : 1 }",
        RenamedPropertiesContainer.class);
    if (unmarshalledObject.getIntInstance() != 3) {
      throw new Fault(
          "Failed to correctly unmarshal renamed properties in order of appearance using PropertyOrderStrategy.LEXICOGRAPHICAL.");
    }

    return Status.passed("OK");
  }
}
