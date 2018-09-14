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

package com.sun.ts.tests.jsonb.customizedmapping.propertynames;

import java.util.Properties;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.JsonbException;
import javax.json.bind.config.PropertyNamingStrategy;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jsonb.customizedmapping.propertynames.model.DuplicateNameContainer;
import com.sun.ts.tests.jsonb.customizedmapping.propertynames.model.PropertyNameCustomizationAccessorsContainer;
import com.sun.ts.tests.jsonb.customizedmapping.propertynames.model.PropertyNameCustomizationContainer;
import com.sun.ts.tests.jsonb.customizedmapping.propertynames.model.StringContainer;
import com.sun.ts.tests.jsonb.customizedmapping.propertynames.model.TransientAnnotatedPropertyContainer;
import com.sun.ts.tests.jsonb.customizedmapping.propertynames.model.TransientGetterAnnotatedPropertyContainer;
import com.sun.ts.tests.jsonb.customizedmapping.propertynames.model.TransientGetterPlusCustomizationAnnotatedFieldContainer;
import com.sun.ts.tests.jsonb.customizedmapping.propertynames.model.TransientGetterPlusCustomizationAnnotatedGetterContainer;
import com.sun.ts.tests.jsonb.customizedmapping.propertynames.model.TransientPlusCustomizationAnnotatedGetterContainer;
import com.sun.ts.tests.jsonb.customizedmapping.propertynames.model.TransientPlusCustomizationAnnotatedPropertyContainer;
import com.sun.ts.tests.jsonb.customizedmapping.propertynames.model.TransientPlusCustomizationAnnotatedSetterContainer;
import com.sun.ts.tests.jsonb.customizedmapping.propertynames.model.TransientPropertyContainer;
import com.sun.ts.tests.jsonb.customizedmapping.propertynames.model.TransientSetterAnnotatedPropertyContainer;
import com.sun.ts.tests.jsonb.customizedmapping.propertynames.model.TransientSetterPlusCustomizationAnnotatedFieldContainer;
import com.sun.ts.tests.jsonb.customizedmapping.propertynames.model.TransientSetterPlusCustomizationAnnotatedSetterContainer;

/**
 * @test
 * @sources PropertyNameCustomizationTest.java
 * @executeClass com.sun.ts.tests.jsonb.customizedmapping.propertynames.PropertyNameCustomizationTest
 **/
public class PropertyNameCustomizationTest extends ServiceEETest {
  private static final long serialVersionUID = 10L;

  private final Jsonb jsonb = JsonbBuilder.create();

  public static void main(String[] args) {
    EETest t = new PropertyNameCustomizationTest();
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
   * @testName: testTransientField
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.1.1-1
   *
   * @test_Strategy: Assert that transient fields are ignored during
   * marshalling/unmarshalling
   */
  public Status testTransientField() throws Fault {
    String jsonString = jsonb.toJson(new TransientPropertyContainer() {
      {
        setInstance("String Value");
      }
    });
    if (!jsonString.matches("\\{\\s*\\}")) {
      throw new Fault(
          "Failed to ignore transient property during marshalling.");
    }

    TransientPropertyContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : \"Test String\" }", TransientPropertyContainer.class);
    if (unmarshalledObject.getInstance() != null) {
      throw new Fault(
          "Failed to ignore transient property during unmarshalling.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testTransientAnnotatedField
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.1.1-1
   *
   * @test_Strategy: Assert that fields annotated as JsonbTransient are ignored
   * during marshalling/unmarshalling
   */
  public Status testTransientAnnotatedField() throws Fault {
    String jsonString = jsonb.toJson(new TransientAnnotatedPropertyContainer() {
      {
        setInstance("String Value");
      }
    });
    if (!jsonString.matches("\\{\\s*\\}")) {
      throw new Fault(
          "Failed to ignore JsonbTransient property during marshalling.");
    }

    TransientAnnotatedPropertyContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : \"Test String\" }",
        TransientAnnotatedPropertyContainer.class);
    if (unmarshalledObject.getInstance() != null) {
      throw new Fault(
          "Failed to ignore JsonbTransient property during unmarshalling.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testTransientAnnotatedGetter
   * 
   * @assertion_ids: JSONB:SPEC:JSB-4.1.1-1
   * 
   * @test_Strategy: Assert that fields with getters annotated as JsonbTransient
   * are ignored during marshalling
   */
  public void testTransientAnnotatedGetter() throws Fault {
    String jsonString = jsonb
        .toJson(new TransientGetterAnnotatedPropertyContainer() {
          {
            setInstance("String Value");
          }
        });
    if (!jsonString.matches("\\{\\s*\\}")) {
      throw new Fault(
          "Failed to ignore @JsonbTransient on getter during marshalling.");
    }
  }

  /*
   * @testName: testTransientAnnotatedSetter
   * 
   * @assertion_ids: JSONB:SPEC:JSB-4.1.1-1
   * 
   * @test_Strategy: Assert that fields with setters annotated as JsonbTransient
   * are ignored during unmarshalling
   */
  public void testTransientAnnotatedSetter() throws Fault {
    TransientSetterAnnotatedPropertyContainer unmarshalledObject = jsonb
        .fromJson("{ \"instance\" : \"Test String\" }",
            TransientSetterAnnotatedPropertyContainer.class);
    if (unmarshalledObject.getInstance() != null) {
      throw new Fault(
          "Failed to ignore @JsonbTransient on setter during unmarshalling.");
    }
  }

  /*
   * @testName: testTransientPlusCustomizationAnnotatedField
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.1.1-2
   *
   * @test_Strategy: Assert that JsonbException is thrown for fields annotated
   * as both JsonbTransient and other Jsonb customization annotations
   */
  public Status testTransientPlusCustomizationAnnotatedField() throws Fault {
    try {
      jsonb.toJson(new TransientPlusCustomizationAnnotatedPropertyContainer());
      throw new Fault(
          "JsonbException not thrown for property annotated with both JsonbTransient and other Jsonb customization annotation.");
    } catch (Exception x) {
      if (!JsonbException.class.isAssignableFrom(x.getClass())) {
        throw new Fault(
            "JsonbException expected for property annotated with both JsonbTransient and other Jsonb customization annotation.");
      }
    }

    try {
      jsonb.fromJson("{ \"instance\" : \"Test String\" }",
          TransientPlusCustomizationAnnotatedPropertyContainer.class);
      throw new Fault(
          "JsonbException not thrown for property annotated with both JsonbTransient and other Jsonb customization annotation.");
    } catch (Exception x) {
      if (!JsonbException.class.isAssignableFrom(x.getClass())) {
        throw new Fault(
            "JsonbException expected for property annotated with both JsonbTransient and other Jsonb customization annotation.");
      }
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testTransientPlusCustomizationAnnotatedGetter
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.1.1-2
   *
   * @test_Strategy: Assert that JsonbException is thrown for fields annotated
   * as both JsonbTransient and other Jsonb customization annotations
   */
  public void testTransientPlusCustomizationAnnotatedGetter() throws Fault {
    try {
      jsonb.toJson(new TransientPlusCustomizationAnnotatedGetterContainer());
      throw new Fault(
          "JsonbException not thrown for property annotated with JsonbTransient and getter with other Jsonb customization annotation.");
    } catch (Exception x) {
      if (!JsonbException.class.isAssignableFrom(x.getClass())) {
        throw new Fault(
            "JsonbException expected for property annotated with JsonbTransient and getter with other Jsonb customization annotation.");
      }
    }

    try {
      jsonb.fromJson("{ \"instance\" : \"Test String\" }",
          TransientPlusCustomizationAnnotatedGetterContainer.class);
      throw new Fault(
          "JsonbException not thrown for property annotated with JsonbTransient and getter with other Jsonb customization annotation.");
    } catch (Exception x) {
      if (!JsonbException.class.isAssignableFrom(x.getClass())) {
        throw new Fault(
            "JsonbException expected for property annotated with JsonbTransient and getter with other Jsonb customization annotation.");
      }
    }
  }

  /*
   * @testName: testTransientPlusCustomizationAnnotatedSetter
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.1.1-2
   *
   * @test_Strategy: Assert that JsonbException is thrown for fields annotated
   * as both JsonbTransient and other Jsonb customization annotations
   */
  public void testTransientPlusCustomizationAnnotatedSetter() throws Fault {
    try {
      jsonb.toJson(new TransientPlusCustomizationAnnotatedSetterContainer());
      throw new Fault(
          "JsonbException not thrown for property annotated with JsonbTransient and setter with other Jsonb customization annotation.");
    } catch (Exception x) {
      if (!JsonbException.class.isAssignableFrom(x.getClass())) {
        throw new Fault(
            "JsonbException expected for property annotated with JsonbTransient and setter with other Jsonb customization annotation.");
      }
    }

    try {
      jsonb.fromJson("{ \"instance\" : \"Test String\" }",
          TransientPlusCustomizationAnnotatedSetterContainer.class);
      throw new Fault(
          "JsonbException not thrown for property annotated with JsonbTransient and setter with other Jsonb customization annotation.");
    } catch (Exception x) {
      if (!JsonbException.class.isAssignableFrom(x.getClass())) {
        throw new Fault(
            "JsonbException expected for property annotated with JsonbTransient and setter with other Jsonb customization annotation.");
      }
    }
  }

  /*
   * @testName: testTransientGetterPlusCustomizationAnnotatedField
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.1.1-3
   *
   * @test_Strategy: Assert that JsonbException is thrown for fields annotated
   * as both JsonbTransient and other Jsonb customization annotations
   */
  public void testTransientGetterPlusCustomizationAnnotatedField()
      throws Fault {
    try {
      jsonb.toJson(
          new TransientGetterPlusCustomizationAnnotatedFieldContainer());
      throw new Fault(
          "JsonbException not thrown for getter annotated with JsonbTransient and property with other Jsonb customization annotation.");
    } catch (Exception x) {
      if (!JsonbException.class.isAssignableFrom(x.getClass())) {
        throw new Fault(
            "JsonbException expected for getter annotated with JsonbTransient and property with other Jsonb customization annotation.");
      }
    }

    try {
      jsonb.fromJson("{ \"instance\" : \"Test String\" }",
          TransientGetterPlusCustomizationAnnotatedFieldContainer.class);
      throw new Fault(
          "JsonbException not thrown for getter annotated with JsonbTransient and property with other Jsonb customization annotation.");
    } catch (Exception x) {
      if (!JsonbException.class.isAssignableFrom(x.getClass())) {
        throw new Fault(
            "JsonbException expected for getter annotated with JsonbTransient and property with other Jsonb customization annotation.");
      }
    }
  }

  /*
   * @testName: testTransientGetterPlusCustomizationAnnotatedGetter
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.1.1-3
   *
   * @test_Strategy: Assert that JsonbException is thrown for fields annotated
   * as both JsonbTransient and other Jsonb customization annotations
   */
  public void testTransientGetterPlusCustomizationAnnotatedGetter()
      throws Fault {
    try {
      jsonb.toJson(
          new TransientGetterPlusCustomizationAnnotatedGetterContainer());
      throw new Fault(
          "JsonbException not thrown for property annotated with JsonbTransient and getter with other Jsonb customization annotation.");
    } catch (Exception x) {
      if (!JsonbException.class.isAssignableFrom(x.getClass())) {
        throw new Fault(
            "JsonbException expected for property annotated with JsonbTransient and getter with other Jsonb customization annotation.");
      }
    }

    try {
      jsonb.fromJson("{ \"instance\" : \"Test String\" }",
          TransientGetterPlusCustomizationAnnotatedGetterContainer.class);
      throw new Fault(
          "JsonbException not thrown for property annotated with JsonbTransient and getter with other Jsonb customization annotation.");
    } catch (Exception x) {
      if (!JsonbException.class.isAssignableFrom(x.getClass())) {
        throw new Fault(
            "JsonbException expected for property annotated with JsonbTransient and getter with other Jsonb customization annotation.");
      }
    }
  }

  /*
   * @testName: testTransientSetterPlusCustomizationAnnotatedSetter
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.1.1-4
   *
   * @test_Strategy: Assert that JsonbException is thrown for fields annotated
   * as both JsonbTransient and other Jsonb customization annotations
   */
  public void testTransientSetterPlusCustomizationAnnotatedSetter()
      throws Fault {
    try {
      jsonb.toJson(
          new TransientSetterPlusCustomizationAnnotatedSetterContainer());
      throw new Fault(
          "JsonbException not thrown for property annotated with JsonbTransient and setter with other Jsonb customization annotation.");
    } catch (Exception x) {
      if (!JsonbException.class.isAssignableFrom(x.getClass())) {
        throw new Fault(
            "JsonbException expected for property annotated with JsonbTransient and setter with other Jsonb customization annotation.");
      }
    }

    try {
      jsonb.fromJson("{ \"instance\" : \"Test String\" }",
          TransientSetterPlusCustomizationAnnotatedSetterContainer.class);
      throw new Fault(
          "JsonbException not thrown for property annotated with JsonbTransient and setter with other Jsonb customization annotation.");
    } catch (Exception x) {
      if (!JsonbException.class.isAssignableFrom(x.getClass())) {
        throw new Fault(
            "JsonbException expected for property annotated with JsonbTransient and setter with other Jsonb customization annotation.");
      }
    }
  }

  /*
   * @testName: testTransientSetterPlusCustomizationAnnotatedField
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.1.1-4
   *
   * @test_Strategy: Assert that JsonbException is thrown for fields annotated
   * as both JsonbTransient and other Jsonb customization annotations
   */
  public void testTransientSetterPlusCustomizationAnnotatedField()
      throws Fault {
    try {
      jsonb.toJson(
          new TransientSetterPlusCustomizationAnnotatedFieldContainer());
      throw new Fault(
          "JsonbException not thrown for setter annotated with JsonbTransient and property with other Jsonb customization annotation.");
    } catch (Exception x) {
      if (!JsonbException.class.isAssignableFrom(x.getClass())) {
        throw new Fault(
            "JsonbException expected for setter annotated with JsonbTransient and property with other Jsonb customization annotation.");
      }
    }

    try {
      jsonb.fromJson("{ \"instance\" : \"Test String\" }",
          TransientSetterPlusCustomizationAnnotatedFieldContainer.class);
      throw new Fault(
          "JsonbException not thrown for setter annotated with JsonbTransient and property with other Jsonb customization annotation.");
    } catch (Exception x) {
      if (!JsonbException.class.isAssignableFrom(x.getClass())) {
        throw new Fault(
            "JsonbException expected for setter annotated with JsonbTransient and property with other Jsonb customization annotation.");
      }
    }
  }

  /*
   * @testName: testPropertyNameCustomization
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.1.2-1
   *
   * @test_Strategy: Assert that property name can be customized using
   * JsonbProperty annotation
   */
  public Status testPropertyNameCustomization() throws Fault {
    String jsonString = jsonb.toJson(new PropertyNameCustomizationContainer() {
      {
        setInstance("Test String");
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"stringInstance\"\\s*\\:\\s*\"Test String\"\\s*\\}")) {
      throw new Fault(
          "Failed to customize property name during marshalling using JsonbProperty annotation.");
    }

    PropertyNameCustomizationContainer unmarshalledObject = jsonb.fromJson(
        "{ \"stringInstance\" : \"Test String\" }",
        PropertyNameCustomizationContainer.class);
    if (!"Test String".equals(unmarshalledObject.getInstance())) {
      throw new Fault(
          "Failed to customize property name during unmarshalling using JsonbProperty annotation.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testPropertyNameCustomizationAccessors
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.1.2-1; JSONB:SPEC:JSB-4.1.2-2;
   * JSONB:SPEC:JSB-4.1.2-3; JSONB:SPEC:JSB-4.1.2-4
   *
   * @test_Strategy: Assert that property name can be individually customized
   * for marshalling and unmarshalling using JsonbProperty annotation on
   * accessors
   */
  public Status testPropertyNameCustomizationAccessors() throws Fault {
    String jsonString = jsonb
        .toJson(new PropertyNameCustomizationAccessorsContainer() {
          {
            setInstance("Test String");
          }
        });
    if (!jsonString.matches(
        "\\{\\s*\"getterInstance\"\\s*\\:\\s*\"Test String\"\\s*\\}")) {
      throw new Fault(
          "Failed to customize property name during marshalling using JsonbProperty annotation on getter.");
    }

    PropertyNameCustomizationAccessorsContainer unmarshalledObject = jsonb
        .fromJson("{ \"setterInstance\" : \"Test String\" }",
            PropertyNameCustomizationAccessorsContainer.class);
    if (!"Test String".equals(unmarshalledObject.getInstance())) {
      throw new Fault(
          "Failed to customize property name during unmarshalling using JsonbProperty annotation on setter.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testIdentityPropertyNamingStrategy
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.1.3-1
   *
   * @test_Strategy: Assert that property name is the same as the field name
   * when using PropertyNamingStrategy.IDENTITY
   */
  public Status testIdentityPropertyNamingStrategy() throws Fault {
    JsonbConfig config = new JsonbConfig();
    config.setProperty(JsonbConfig.PROPERTY_NAMING_STRATEGY,
        PropertyNamingStrategy.IDENTITY);
    Jsonb jsonb = JsonbBuilder.create(config);

    String jsonString = jsonb.toJson(new StringContainer() {
      {
        setStringInstance("Test String");
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"stringInstance\"\\s*\\:\\s*\"Test String\"\\s*\\}")) {
      throw new Fault(
          "Failed to correctly marshal property using PropertyNamingStrategy.IDENTITY.");
    }

    StringContainer unmarshalledObject = jsonb.fromJson(
        "{ \"stringInstance\" : \"Test String\" }", StringContainer.class);
    if (!"Test String".equals(unmarshalledObject.getStringInstance())) {
      throw new Fault(
          "Failed to correctly unmarshal property using PropertyNamingStrategy.IDENTITY.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testLowerCaseWithDashesPropertyNamingStrategy
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.1.3-1
   *
   * @test_Strategy: Assert that property name is the same as the field name
   * when using PropertyNamingStrategy.LOWER_CASE_WITH_DASHES
   */
  public Status testLowerCaseWithDashesPropertyNamingStrategy() throws Fault {
    JsonbConfig config = new JsonbConfig();
    config.setProperty(JsonbConfig.PROPERTY_NAMING_STRATEGY,
        PropertyNamingStrategy.LOWER_CASE_WITH_DASHES);
    Jsonb jsonb = JsonbBuilder.create(config);

    String jsonString = jsonb.toJson(new StringContainer() {
      {
        setStringInstance("Test String");
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"string-instance\"\\s*\\:\\s*\"Test String\"\\s*\\}")) {
      throw new Fault(
          "Failed to correctly marshal property using PropertyNamingStrategy.LOWER_CASE_WITH_DASHES.");
    }

    StringContainer unmarshalledObject = jsonb.fromJson(
        "{ \"string-instance\" : \"Test String\" }", StringContainer.class);
    if (!"Test String".equals(unmarshalledObject.getStringInstance())) {
      throw new Fault(
          "Failed to correctly unmarshal property using PropertyNamingStrategy.LOWER_CASE_WITH_DASHES.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testLowerCaseWithUnderscoresPropertyNamingStrategy
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.1.3-1
   *
   * @test_Strategy: Assert that property name is the same as the field name
   * when using PropertyNamingStrategy.LOWER_CASE_WITH_UNDERSCORES
   */
  public Status testLowerCaseWithUnderscoresPropertyNamingStrategy()
      throws Fault {
    JsonbConfig config = new JsonbConfig();
    config.setProperty(JsonbConfig.PROPERTY_NAMING_STRATEGY,
        PropertyNamingStrategy.LOWER_CASE_WITH_UNDERSCORES);
    Jsonb jsonb = JsonbBuilder.create(config);

    String jsonString = jsonb.toJson(new StringContainer() {
      {
        setStringInstance("Test String");
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"string_instance\"\\s*\\:\\s*\"Test String\"\\s*\\}")) {
      throw new Fault(
          "Failed to correctly marshal property using PropertyNamingStrategy.LOWER_CASE_WITH_UNDERSCORES.");
    }

    StringContainer unmarshalledObject = jsonb.fromJson(
        "{ \"string_instance\" : \"Test String\" }", StringContainer.class);
    if (!"Test String".equals(unmarshalledObject.getStringInstance())) {
      throw new Fault(
          "Failed to correctly unmarshal property using PropertyNamingStrategy.LOWER_CASE_WITH_UNDERSCORES.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testUpperCamelCasePropertyNamingStrategy
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.1.3-1
   *
   * @test_Strategy: Assert that property name is the same as the field name
   * when using PropertyNamingStrategy.UPPER_CAMEL_CASE
   */
  public Status testUpperCamelCasePropertyNamingStrategy() throws Fault {
    JsonbConfig config = new JsonbConfig();
    config.setProperty(JsonbConfig.PROPERTY_NAMING_STRATEGY,
        PropertyNamingStrategy.UPPER_CAMEL_CASE);
    Jsonb jsonb = JsonbBuilder.create(config);

    String jsonString = jsonb.toJson(new StringContainer() {
      {
        setStringInstance("Test String");
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"StringInstance\"\\s*\\:\\s*\"Test String\"\\s*\\}")) {
      throw new Fault(
          "Failed to correctly marshal property using PropertyNamingStrategy.UPPER_CAMEL_CASE.");
    }

    StringContainer unmarshalledObject = jsonb.fromJson(
        "{ \"StringInstance\" : \"Test String\" }", StringContainer.class);
    if (!"Test String".equals(unmarshalledObject.getStringInstance())) {
      throw new Fault(
          "Failed to correctly unmarshal property using PropertyNamingStrategy.UPPER_CAMEL_CASE.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testUpperCamelCaseWithSpacesPropertyNamingStrategy
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.1.3-1
   *
   * @test_Strategy: Assert that property name is the same as the field name
   * when using PropertyNamingStrategy.UPPER_CAMEL_CASE_WITH_SPACES
   */
  public Status testUpperCamelCaseWithSpacesPropertyNamingStrategy()
      throws Fault {
    JsonbConfig config = new JsonbConfig();
    config.setProperty(JsonbConfig.PROPERTY_NAMING_STRATEGY,
        PropertyNamingStrategy.UPPER_CAMEL_CASE_WITH_SPACES);
    Jsonb jsonb = JsonbBuilder.create(config);

    String jsonString = jsonb.toJson(new StringContainer() {
      {
        setStringInstance("Test String");
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"String Instance\"\\s*\\:\\s*\"Test String\"\\s*\\}")) {
      throw new Fault(
          "Failed to correctly marshal property using PropertyNamingStrategy.UPPER_CAMEL_CASE_WITH_SPACES.");
    }

    StringContainer unmarshalledObject = jsonb.fromJson(
        "{ \"String Instance\" : \"Test String\" }", StringContainer.class);
    if (!"Test String".equals(unmarshalledObject.getStringInstance())) {
      throw new Fault(
          "Failed to correctly unmarshal property using PropertyNamingStrategy.UPPER_CAMEL_CASE_WITH_SPACES.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testCaseInsensitivePropertyNamingStrategy
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.1.3-1
   *
   * @test_Strategy: Assert that property name is the same as the field name
   * when using PropertyNamingStrategy.CASE_INSENSITIVE
   */
  public Status testCaseInsensitivePropertyNamingStrategy() throws Fault {
    JsonbConfig config = new JsonbConfig();
    config.setProperty(JsonbConfig.PROPERTY_NAMING_STRATEGY,
        PropertyNamingStrategy.CASE_INSENSITIVE);
    Jsonb jsonb = JsonbBuilder.create(config);

    String jsonString = jsonb.toJson(new StringContainer() {
      {
        setStringInstance("Test String");
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"stringInstance\"\\s*\\:\\s*\"Test String\"\\s*\\}")) {
      throw new Fault(
          "Failed to correctly marshal property using PropertyNamingStrategy.CASE_INSENSITIVE.");
    }

    StringContainer unmarshalledObject = jsonb.fromJson(
        "{ \"stringInstance\" : \"Test String\" }", StringContainer.class);
    if (!"Test String".equals(unmarshalledObject.getStringInstance())) {
      throw new Fault(
          "Failed to correctly unmarshal property using PropertyNamingStrategy.CASE_INSENSITIVE.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testDuplicateName
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.1.4-1
   *
   * @test_Strategy: Assert that JsonbException is thrown for property name
   * duplication as a result of property name customization
   */
  public Status testDuplicateName() throws Fault {
    try {
      jsonb.toJson(new DuplicateNameContainer());
      throw new Fault(
          "JsonbException not thrown for property name duplication as a result of property name customization.");
    } catch (Exception x) {
      if (!JsonbException.class.isAssignableFrom(x.getClass())) {
        throw new Fault(
            "JsonbException expected for property name duplication as a result of property name customization.");
      }
    }

    try {
      jsonb.fromJson("{ \"instance\" : \"Test String\" }",
          DuplicateNameContainer.class);
      throw new Fault(
          "JsonbException not thrown for property name duplication as a result of property name customization.");
    } catch (Exception x) {
      if (!JsonbException.class.isAssignableFrom(x.getClass())) {
        throw new Fault(
            "JsonbException expected for property name duplication as a result of property name customization.");
      }
    }

    return Status.passed("OK");
  }
}
