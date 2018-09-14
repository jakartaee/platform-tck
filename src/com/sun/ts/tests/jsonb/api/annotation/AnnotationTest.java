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

package com.sun.ts.tests.jsonb.api.annotation;

import java.util.Properties;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jsonb.api.model.SimpleAnnotatedAdaptedContainer;
import com.sun.ts.tests.jsonb.api.model.SimpleAnnotatedDateContainer;
import com.sun.ts.tests.jsonb.api.model.SimpleAnnotatedDoubleContainer;
import com.sun.ts.tests.jsonb.api.model.SimpleAnnotatedNillableContainer;
import com.sun.ts.tests.jsonb.api.model.SimpleAnnotatedNillablePropertyContainer;
import com.sun.ts.tests.jsonb.api.model.SimpleAnnotatedPropertyOrderContainer;
import com.sun.ts.tests.jsonb.api.model.SimpleAnnotatedPropertyVisibilityContainer;
import com.sun.ts.tests.jsonb.api.model.SimpleAnnotatedSerializedArrayContainer;
import com.sun.ts.tests.jsonb.api.model.SimpleAnnotatedSerializedContainer;
import com.sun.ts.tests.jsonb.api.model.SimpleContainer;
import com.sun.ts.tests.jsonb.api.model.SimplePartiallyAnnotatedPropertyOrderContainer;

/**
 * @test
 * @sources AnnotationTest.java
 * @executeClass com.sun.ts.tests.jsonb.api.AnnotationTest
 **/
public class AnnotationTest extends ServiceEETest {
  private static final long serialVersionUID = 10L;

  private Jsonb jsonb = JsonbBuilder.create();

  public static void main(String[] args) {
    EETest t = new AnnotationTest();
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
   * @testName: testJsonbDateFormat
   *
   * @assertion_ids: JSONB:JAVADOC:57; JSONB:JAVADOC:58
   *
   * @test_Strategy: Assert that JsonbDateFormat annotation can be used to
   * customize date format
   */
  public Status testJsonbDateFormat() throws Fault {
    String jsonString = jsonb.toJson(new SimpleAnnotatedDateContainer());
    if (!jsonString
        .matches("\\{\\s*\"instance\"\\s*:\\s*\"Do, 01 Jan 1970\"\\s*}")) {
      throw new Fault(
          "Failed to customize date format using JsonbDateFormat annotation.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testJsonbNillable
   *
   * @assertion_ids: JSONB:JAVADOC:59
   *
   * @test_Strategy: Assert that JsonbNillable annotation can be used to enable
   * serialization of null values
   */
  public Status testJsonbNillable() throws Fault {
    String jsonString = jsonb.toJson(new SimpleAnnotatedNillableContainer());
    if (!jsonString.matches("\\{\\s*\"instance\"\\s*:\\s*null\\s*}")) {
      throw new Fault(
          "Failed to enable serialization of null values using JsonbNillable annotation.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testJsonbNumberFormat
   *
   * @assertion_ids: JSONB:JAVADOC:60; JSONB:JAVADOC:61
   *
   * @test_Strategy: Assert that JsonbNumberFormat annotation can be used to
   * customize number format
   */
  public Status testJsonbNumberFormat() throws Fault {
    String jsonString = jsonb.toJson(new SimpleAnnotatedDoubleContainer());
    if (!jsonString.matches("\\{\\s*\"instance\"\\s*:\\s*\"123.456,8\"\\s*}")) {
      throw new Fault(
          "Failed to customize number format using JsonbNumberFormat annotation.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testJsonbProperty
   *
   * @assertion_ids: JSONB:JAVADOC:62; JSONB:JAVADOC:63
   *
   * @test_Strategy: Assert that JsonbProperty annotation can be used to
   * customize property name and enable serialization of null values
   */
  public Status testJsonbProperty() throws Fault {
    String jsonString = jsonb
        .toJson(new SimpleAnnotatedNillablePropertyContainer());
    if (!jsonString.matches("\\{\\s*\"nillableInstance\"\\s*:\\s*null\\s*}")) {
      throw new Fault(
          "Failed to customize property name and enable serialization of null values using JsonbProperty annotation.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testJsonbPropertyOrder
   *
   * @assertion_ids: JSONB:JAVADOC:64
   *
   * @test_Strategy: Assert that JsonbPropertyOrder annotation can be used to
   * customize the order in which fields will be serialized
   */
  public Status testJsonbPropertyOrder() throws Fault {
    String jsonString = jsonb
        .toJson(new SimpleAnnotatedPropertyOrderContainer());
    if (!jsonString.matches(
        "\\{\\s*\"secondInstance\"\\s*:\\s*\"Second String\"\\s*,\\s*\"firstInstance\"\\s*:\\s*\"First String\"\\s*}")) {
      throw new Fault(
          "Failed to customize the order in which fields will be serialized using JsonbPropertyOrder annotation.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testJsonbPropertyPartialOrder
   *
   * @assertion_ids: JSONB:JAVADOC:64
   *
   * @test_Strategy: In that case, properties included in annotation declaration
   * will be serialized first (in defined order), followed by any properties not
   * included in the definition. The order of properties not included in the
   * definition is not guaranteed
   */
  public void testJsonbPropertyPartialOrder() throws Fault {
    String jsonString = jsonb
        .toJson(new SimplePartiallyAnnotatedPropertyOrderContainer());
    if (!jsonString.matches(
        "\\{\\s*\"thirdInstance\"\\s*:\\s*\"Third String\"\\s*,\\s*\"fourthInstance\"\\s*:\\s*\"Fourth String\".*}")) {
      System.out.append("Got JSON: ").println(jsonString);
      throw new Fault(
          "Failed to order the fields partially defined using JsonbPropertyOrder annotation.");
    }
  }

  /*
   * @testName: testJsonbTypeAdapter
   *
   * @assertion_ids: JSONB:JAVADOC:65
   *
   * @test_Strategy: Assert that JsonbTypeAdapter annotation can be used to
   * configure a JsonbAdapter implementation to provide custom mapping
   */
  public Status testJsonbTypeAdapter() throws Fault {
    String jsonString = jsonb.toJson(new SimpleAnnotatedAdaptedContainer() {
      {
        setInstance(new SimpleContainer() {
          {
            setInstance("Test String");
          }
        });
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\{\\s*\"instance\"\\s*:\\s*\"Test String Adapted\"\\s*}\\s*}")) {
      throw new Fault(
          "Failed to configure a JsonbAdapter implementation to provide custom mapping using JsonbTypeAdapter annotation.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testJsonbTypeSerializer
   *
   * @assertion_ids: JSONB:JAVADOC:66; JSONB:JAVADOC:67; JSONB:JAVADOC:72;
   * JSONB:JAVADOC:74; JSONB:JAVADOC:75; JSONB:JAVADOC:76
   *
   * @test_Strategy: Assert that JsonbTypeSerializer and JsonbTypeDeserializer
   * annotations can be used to configure a JsonbSerializer and
   * JsonbDeserializer implementation to provide custom mapping
   */
  public Status testJsonbTypeSerializer() throws Fault {
    SimpleAnnotatedSerializedContainer container = new SimpleAnnotatedSerializedContainer();
    SimpleContainer instance = new SimpleContainer();
    instance.setInstance("Test String");
    container.setInstance(instance);

    String jsonString = jsonb.toJson(container);
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\{\\s*\"instance\"\\s*:\\s*\"Test String Serialized\"\\s*}\\s*}")) {
      throw new Fault(
          "Failed to configure a JsonbSerializer implementation to provide custom mapping using JsonbTypeSerializer annotation.");
    }

    SimpleAnnotatedSerializedContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : { \"instance\" : \"Test String\" } }",
        SimpleAnnotatedSerializedContainer.class);
    if (!"Test String Deserialized"
        .equals(unmarshalledObject.getInstance().getInstance())) {
      throw new Fault(
          "Failed to configure a JsonbDeserializer implementation to provide custom mapping using JsonbTypeDeserializer annotation.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testJsonbArrayTypeSerializer
   *
   * @assertion_ids: JSONB:JAVADOC:66; JSONB:JAVADOC:67; JSONB:JAVADOC:73;
   * JSONB:JAVADOC:74; JSONB:JAVADOC:75; JSONB:JAVADOC:77
   *
   * @test_Strategy: Assert that JsonbTypeSerializer and JsonbTypeDeserializer
   * annotations can be used to configure a JsonbSerializer and
   * JsonbDeserializer implementation to provide custom mapping for array type
   */
  public Status testJsonbArrayTypeSerializer() throws Fault {
    SimpleAnnotatedSerializedArrayContainer container = new SimpleAnnotatedSerializedArrayContainer();
    SimpleContainer instance1 = new SimpleContainer();
    instance1.setInstance("Test String 1");
    SimpleContainer instance2 = new SimpleContainer();
    instance2.setInstance("Test String 2");
    container.setInstance(new SimpleContainer[] { instance1, instance2 });

    String jsonString = jsonb.toJson(container);
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\[\\s*\\{\\s*\"instance\"\\s*:\\s*\"Test String 1\"\\s*}\\s*,\\s*\\{\\s*\"instance\"\\s*:\\s*\"Test String 2\"\\s*}\\s*]\\s*}")) {
      throw new Fault(
          "Failed to configure a JsonbSerializer implementation to provide custom mapping using JsonbTypeSerializer annotation.");
    }

    SimpleAnnotatedSerializedArrayContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : [ { \"instance\" : \"Test String 1\" }, { \"instance\" : \"Test String 2\" } ] }",
        SimpleAnnotatedSerializedArrayContainer.class);
    if (!"Test String 1"
        .equals(unmarshalledObject.getInstance()[0].getInstance())) {
      throw new Fault(
          "Failed to configure a JsonbDeserializer implementation to provide custom mapping using JsonbTypeDeserializer annotation.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testJsonbVisibility
   *
   * @assertion_ids: JSONB:JAVADOC:68; JSONB:JAVADOC:70; JSONB:JAVADOC:71
   *
   * @test_Strategy: Assert that JsonbVisibility annotation can be used to
   * customize field visibility
   */
  public Status testJsonbVisibility() throws Fault {
    String jsonString = jsonb
        .toJson(new SimpleAnnotatedPropertyVisibilityContainer());
    if (!jsonString
        .matches("\\{\\s*\"secondInstance\"\\s*:\\s*\"Second String\"\\s*}")) {
      throw new Fault(
          "Failed to customize fields visibility using JsonbVisibility annotation.");
    }

    return Status.passed("OK");
  }
}
