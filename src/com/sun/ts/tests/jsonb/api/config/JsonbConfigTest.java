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

package com.sun.ts.tests.jsonb.api.config;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import javax.json.bind.JsonbConfig;
import javax.json.bind.adapter.JsonbAdapter;
import javax.json.bind.config.BinaryDataStrategy;
import javax.json.bind.config.PropertyNamingStrategy;
import javax.json.bind.config.PropertyOrderStrategy;
import javax.json.bind.serializer.JsonbDeserializer;
import javax.json.bind.serializer.JsonbSerializer;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jsonb.api.model.SimpleContainerDeserializer;
import com.sun.ts.tests.jsonb.api.model.SimpleContainerSerializer;
import com.sun.ts.tests.jsonb.api.model.SimpleIntegerAdapter;
import com.sun.ts.tests.jsonb.api.model.SimpleIntegerDeserializer;
import com.sun.ts.tests.jsonb.api.model.SimpleIntegerSerializer;
import com.sun.ts.tests.jsonb.api.model.SimplePropertyNamingStrategy;
import com.sun.ts.tests.jsonb.api.model.SimplePropertyVisibilityStrategy;
import com.sun.ts.tests.jsonb.api.model.SimpleStringAdapter;

/**
 * @test
 * @sources JsonbConfigTest.java
 * @executeClass com.sun.ts.tests.jsonb.api.JsonbConfigTest
 **/
public class JsonbConfigTest extends ServiceEETest {

  private static final long serialVersionUID = 10L;

  public static void main(String[] args) {
    EETest t = new JsonbConfigTest();
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
   * @testName: testGetAsMap
   *
   * @assertion_ids: JSONB:JAVADOC:33; JSONB:JAVADOC:35
   *
   * @test_Strategy: Assert that JsonbConfig.getAsMap returns all configuration
   * properties as an unmodifiable map
   */
  public void testGetAsMap() throws Fault {
    JsonbConfig jsonbConfig = new JsonbConfig().withFormatting(true)
        .withNullValues(true);
    Map<String, Object> configMap = jsonbConfig.getAsMap();
    if (configMap.size() != 2 || !configMap.containsKey(JsonbConfig.FORMATTING)
        || !configMap.containsKey(JsonbConfig.NULL_VALUES)) {
      throw new Fault(
          "Failed to get configuration properties as a map using JsonbConfig.getAsMap method.");
    }

    try {
      configMap.put(JsonbConfig.BINARY_DATA_STRATEGY,
          BinaryDataStrategy.BASE_64);
      throw new Fault(
          "Failed to get configuration properties as an unmodifiable map using JsonbConfig.getAsMap method.");
    } catch (UnsupportedOperationException x) {
    }
  }

  /*
   * @testName: testGetProperty
   *
   * @assertion_ids: JSONB:JAVADOC:34; JSONB:JAVADOC:35
   *
   * @test_Strategy: Assert that JsonbConfig.getProperty returns the value of a
   * specific configuration property
   */
  public void testGetProperty() throws Fault {
    JsonbConfig jsonbConfig = new JsonbConfig().withFormatting(true)
        .withNullValues(true);
    Optional<Object> property = jsonbConfig.getProperty(JsonbConfig.FORMATTING);

    if (!property.isPresent() || !(boolean) property.get()) {
      throw new Fault(
          "Failed to get a configuration property using JsonbConfig.getProperty method.");
    }
  }

  /*
   * @testName: testGetUnsetProperty
   *
   * @assertion_ids: JSONB:JAVADOC:34; JSONB:JAVADOC:35
   *
   * @test_Strategy: Assert that JsonbConfig.getProperty returns the value of a
   * specific configuration property
   */
  public void testGetUnsetProperty() throws Fault {
    JsonbConfig jsonbConfig = new JsonbConfig().withFormatting(true)
        .withNullValues(true);
    Optional<Object> property = jsonbConfig.getProperty(JsonbConfig.ADAPTERS);

    if (property.isPresent()) {
      throw new Fault(
          "Failed to get Optional.empty for an unset configuration property using JsonbConfig.getProperty method.");
    }
  }

  /*
   * @testName: testSetProperty
   *
   * @assertion_ids: JSONB:JAVADOC:35; JSONB:JAVADOC:36
   *
   * @test_Strategy: Assert that JsonbConfig.setProperty sets the value of a
   * specific configuration property
   */
  public void testSetProperty() throws Fault {
    JsonbConfig jsonbConfig = new JsonbConfig().setProperty(
        JsonbConfig.PROPERTY_NAMING_STRATEGY,
        PropertyNamingStrategy.UPPER_CAMEL_CASE_WITH_SPACES);

    if (!jsonbConfig.getProperty(JsonbConfig.PROPERTY_NAMING_STRATEGY).get()
        .equals(PropertyNamingStrategy.UPPER_CAMEL_CASE_WITH_SPACES)) {
      throw new Fault(
          "Failed to set a property value using JsonbConfig.setProperty method.");
    }
  }

  /*
   * @testName: testWithAdapters
   *
   * @assertion_ids: JSONB:JAVADOC:35; JSONB:JAVADOC:37
   *
   * @test_Strategy: Assert that JsonbConfig.withAdapters configures custom
   * mapping adapters
   */
  public void testWithAdapters() throws Fault {
    SimpleStringAdapter simpleStringAdapter = new SimpleStringAdapter();
    JsonbConfig jsonbConfig = new JsonbConfig()
        .withAdapters(simpleStringAdapter);

    Object adapters = jsonbConfig.getProperty(JsonbConfig.ADAPTERS).get();
    if (!JsonbAdapter[].class.isAssignableFrom(adapters.getClass())
        || simpleStringAdapter != ((JsonbAdapter[]) adapters)[0]) {
      throw new Fault(
          "Failed to configure a custom adapter using JsonbConfig.withAdapters method.");
    }
  }

  /*
   * @testName: testWithAdaptersMultipleCalls
   *
   * @assertion_ids: JSONB:JAVADOC:35; JSONB:JAVADOC:37
   *
   * @test_Strategy: Assert that multiple JsonbConfig.withAdapters calls result
   * in a merge of adapter values configured
   */
  public void testWithAdaptersMultipleCalls() throws Fault {
    JsonbConfig jsonbConfig = new JsonbConfig()
        .withAdapters(new SimpleIntegerAdapter())
        .withAdapters(new SimpleStringAdapter());

    Object adapters = jsonbConfig.getProperty(JsonbConfig.ADAPTERS).get();
    if (!JsonbAdapter[].class.isAssignableFrom(adapters.getClass())) {
      throw new Fault(
          "Not expected JsobAdapter array but " + adapters.getClass());
    }

    if (((JsonbAdapter[]) adapters).length != 2) {
      throw new Fault(
          "Failed to configure multiple custom adapters using multiple JsonbConfig.withAdapters method calls.");
    }
  }

  /*
   * @testName: testWithMultipleAdapters
   *
   * @assertion_ids: JSONB:JAVADOC:35; JSONB:JAVADOC:37
   *
   * @test_Strategy: Assert that multiple JsonbConfig.withAdapters calls result
   * in a merge of adapter values configured
   */
  public void testWithMultipleAdapters() throws Fault {
    JsonbConfig jsonbConfig = new JsonbConfig()
        .withAdapters(new SimpleIntegerAdapter(), new SimpleStringAdapter());

    Object adapters = jsonbConfig.getProperty(JsonbConfig.ADAPTERS).get();
    if (!JsonbAdapter[].class.isAssignableFrom(adapters.getClass())) {
      throw new Fault(
          "Not expected JsobAdapter array but " + adapters.getClass());
    }
    if (((JsonbAdapter[]) adapters).length != 2) {
      throw new Fault(
          "Failed to configure multiple custom adapters using multiple JsonbConfig.withAdapters method calls.");
    }
  }

  /*
   * @testName: testWithBinaryDataStrategy
   *
   * @assertion_ids: JSONB:JAVADOC:35; JSONB:JAVADOC:38
   *
   * @test_Strategy: Assert that JsonbConfig.withBinaryDataStrategy configures
   * custom binary data strategy
   */
  public void testWithBinaryDataStrategy() throws Fault {
    JsonbConfig jsonbConfig = new JsonbConfig()
        .withBinaryDataStrategy(BinaryDataStrategy.BASE_64_URL);

    Optional<Object> property = jsonbConfig
        .getProperty(JsonbConfig.BINARY_DATA_STRATEGY);
    if (!property.isPresent()
        || !BinaryDataStrategy.BASE_64_URL.equals(property.get())) {
      throw new Fault(
          "Failed to configure a custom binary data strategy using JsonbConfig.withBinaryDataStrategy method.");
    }
  }

  /*
   * @testName: testWithDateFormat
   *
   * @assertion_ids: JSONB:JAVADOC:35; JSONB:JAVADOC:39
   *
   * @test_Strategy: Assert that JsonbConfig.withDateFormat configures custom
   * date format
   */
  public void testWithDateFormat() throws Fault {
    JsonbConfig jsonbConfig = new JsonbConfig().withDateFormat("YYYYMMDD",
        Locale.GERMAN);

    Optional<Object> property = jsonbConfig
        .getProperty(JsonbConfig.DATE_FORMAT);
    if (!property.isPresent() || !"YYYYMMDD".equals(property.get())) {
      throw new Fault(
          "Failed to configure a custom date format using JsonbConfig.withDateFormat method.");
    }
  }

  /*
   * @testName: testWithDeserializers
   *
   * @assertion_ids: JSONB:JAVADOC:35; JSONB:JAVADOC:40
   *
   * @test_Strategy: Assert that JsonbConfig.withDeserializers configures custom
   * deserializers
   */
  public void testWithDeserializers() throws Fault {
    SimpleContainerDeserializer deserializer = new SimpleContainerDeserializer();
    JsonbConfig jsonbConfig = new JsonbConfig().withDeserializers(deserializer);

    Object deserializers = jsonbConfig.getProperty(JsonbConfig.DESERIALIZERS)
        .get();
    if (!JsonbDeserializer[].class.isAssignableFrom(deserializers.getClass())
        || deserializer != ((JsonbDeserializer[]) deserializers)[0]) {
      throw new Fault(
          "Failed to configure a custom deserializer using JsonbConfig.withDeserializers method.");
    }
  }

  /*
   * @testName: testWithDeserializersMultipleCalls
   *
   * @assertion_ids: JSONB:JAVADOC:35; JSONB:JAVADOC:40
   *
   * @test_Strategy: Assert that multiple JsonbConfig.withDeserializers calls
   * result in a merge of deserializer values configured
   */
  public void testWithDeserializersMultipleCalls() throws Fault {
    JsonbConfig jsonbConfig = new JsonbConfig()
        .withDeserializers(new SimpleIntegerDeserializer())
        .withDeserializers(new SimpleContainerDeserializer());

    Object deserializers = jsonbConfig.getProperty(JsonbConfig.DESERIALIZERS)
        .get();
    if (!JsonbDeserializer[].class.isAssignableFrom(deserializers.getClass())) {
      throw new Fault("Not expected JsonbDeserializer array but "
          + deserializers.getClass());
    }

    if (((JsonbDeserializer[]) deserializers).length != 2) {
      throw new Fault(
          "Failed to configure multiple custom deserializers using multiple JsonbConfig.withDeserializers method calls.");
    }
  }

  /*
   * @testName: testWithEncoding
   *
   * @assertion_ids: JSONB:JAVADOC:35; JSONB:JAVADOC:41
   *
   * @test_Strategy: Assert that JsonbConfig.withEncoding configures custom
   * character encoding
   */
  public void testWithEncoding() throws Fault {
    JsonbConfig jsonbConfig = new JsonbConfig().withEncoding("UCS2");

    Optional<Object> property = jsonbConfig.getProperty(JsonbConfig.ENCODING);
    if (!property.isPresent() || !"UCS2".equals(property.get())) {
      throw new Fault(
          "Failed to configure a custom character encoding using JsonbConfig.withEncoding method.");
    }
  }

  /*
   * @testName: testWithFormatting
   *
   * @assertion_ids: JSONB:JAVADOC:35; JSONB:JAVADOC:42
   *
   * @test_Strategy: Assert that JsonbConfig.withFormatting configures whether
   * JSON string formatting is enabled
   */
  public void testWithFormatting() throws Fault {
    JsonbConfig jsonbConfig = new JsonbConfig().withFormatting(true);

    Optional<Object> property = jsonbConfig.getProperty(JsonbConfig.FORMATTING);
    if (!property.isPresent() || !(boolean) property.get()) {
      throw new Fault(
          "Failed to configure JSON string formatting using JsonbConfig.withFormatting method.");
    }
  }

  /*
   * @testName: testWithLocale
   *
   * @assertion_ids: JSONB:JAVADOC:35; JSONB:JAVADOC:43
   *
   * @test_Strategy: Assert that JsonbConfig.withLocale configures custom locale
   */
  public void testWithLocale() throws Fault {
    JsonbConfig jsonbConfig = new JsonbConfig().withLocale(Locale.GERMAN);

    Optional<Object> property = jsonbConfig.getProperty(JsonbConfig.LOCALE);
    if (!property.isPresent() || !Locale.GERMAN.equals(property.get())) {
      throw new Fault(
          "Failed to configure a custom locale using JsonbConfig.withLocale method.");
    }
  }

  /*
   * @testName: testWithNullValues
   *
   * @assertion_ids: JSONB:JAVADOC:35; JSONB:JAVADOC:44
   *
   * @test_Strategy: Assert that JsonbConfig.withNullValues configures
   * serialization of null values
   */
  public void testWithNullValues() throws Fault {
    JsonbConfig jsonbConfig = new JsonbConfig().withNullValues(true);

    Optional<Object> property = jsonbConfig
        .getProperty(JsonbConfig.NULL_VALUES);
    if (!property.isPresent() || !(boolean) property.get()) {
      throw new Fault(
          "Failed to configure serialization of null values using JsonbConfig.withNullValues method.");
    }
  }

  /*
   * @testName: testWithPropertyNamingStrategy
   *
   * @assertion_ids: JSONB:JAVADOC:35; JSONB:JAVADOC:45; JSONB:JAVADOC:69
   *
   * @test_Strategy: Assert that JsonbConfig.withPropertyNamingStrategy
   * configures custom property naming strategy
   */
  public void testWithPropertyNamingStrategy() throws Fault {
    SimplePropertyNamingStrategy propertyNamingStrategy = new SimplePropertyNamingStrategy();
    JsonbConfig jsonbConfig = new JsonbConfig()
        .withPropertyNamingStrategy(propertyNamingStrategy);

    Optional<Object> property = jsonbConfig
        .getProperty(JsonbConfig.PROPERTY_NAMING_STRATEGY);
    if (!property.isPresent() || propertyNamingStrategy != property.get()) {
      throw new Fault(
          "Failed to configure a custom property naming strategy using JsonbConfig.withPropertyNamingStrategy method.");
    }
  }

  /*
   * @testName: testWithPropertyNamingStrategyString
   *
   * @assertion_ids: JSONB:JAVADOC:35; JSONB:JAVADOC:46
   *
   * @test_Strategy: Assert that JsonbConfig.withPropertyNamingStrategy with
   * String argument configures custom property naming strategy
   */
  public void testWithPropertyNamingStrategyString() throws Fault {
    JsonbConfig jsonbConfig = new JsonbConfig()
        .withPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);

    Optional<Object> property = jsonbConfig
        .getProperty(JsonbConfig.PROPERTY_NAMING_STRATEGY);
    if (!property.isPresent()
        || !PropertyNamingStrategy.UPPER_CAMEL_CASE.equals(property.get())) {
      throw new Fault(
          "Failed to configure a custom property naming strategy using JsonbConfig.withPropertyNamingStrategy method with String argument.");
    }
  }

  /*
   * @testName: testWithPropertyOrderStrategy
   *
   * @assertion_ids: JSONB:JAVADOC:35; JSONB:JAVADOC:47
   *
   * @test_Strategy: Assert that JsonbConfig.withPropertyOrderStrategy
   * configures custom property order strategy
   */
  public void testWithPropertyOrderStrategy() throws Fault {
    JsonbConfig jsonbConfig = new JsonbConfig()
        .withPropertyOrderStrategy(PropertyOrderStrategy.LEXICOGRAPHICAL);

    Optional<Object> property = jsonbConfig
        .getProperty(JsonbConfig.PROPERTY_ORDER_STRATEGY);
    if (!property.isPresent()
        || !PropertyOrderStrategy.LEXICOGRAPHICAL.equals(property.get())) {
      throw new Fault(
          "Failed to configure a custom property order strategy using JsonbConfig.withPropertyOrderStrategy method.");
    }
  }

  /*
   * @testName: testWithPropertyVisibilityStrategy
   *
   * @assertion_ids: JSONB:JAVADOC:35; JSONB:JAVADOC:48
   *
   * @test_Strategy: Assert that JsonbConfig.withPropertyVisibilityStrategy
   * configures custom property visibility
   */
  public void testWithPropertyVisibilityStrategy() throws Fault {
    SimplePropertyVisibilityStrategy propertyVisibilityStrategy = new SimplePropertyVisibilityStrategy();
    JsonbConfig jsonbConfig = new JsonbConfig()
        .withPropertyVisibilityStrategy(propertyVisibilityStrategy);

    Optional<Object> property = jsonbConfig
        .getProperty(JsonbConfig.PROPERTY_VISIBILITY_STRATEGY);
    if (!property.isPresent() || propertyVisibilityStrategy != property.get()) {
      throw new Fault(
          "Failed to configure a custom property visibility strategy using JsonbConfig.withPropertyVisibilityStrategy method.");
    }
  }

  /*
   * @testName: testWithSerializers
   *
   * @assertion_ids: JSONB:JAVADOC:35; JSONB:JAVADOC:49
   *
   * @test_Strategy: Assert that JsonbConfig.withSerializers configures custom
   * serializers
   */
  public void testWithSerializers() throws Fault {
    SimpleContainerSerializer serializer = new SimpleContainerSerializer();
    JsonbConfig jsonbConfig = new JsonbConfig().withSerializers(serializer);

    Object serializers = jsonbConfig.getProperty(JsonbConfig.SERIALIZERS).get();
    if (!JsonbSerializer[].class.isAssignableFrom(serializers.getClass())
        || serializer != ((JsonbSerializer[]) serializers)[0]) {
      throw new Fault(
          "Failed to configure a custom serializer using JsonbConfig.withSerializers method.");
    }
  }

  /*
   * @testName: testWithSerializersMultipleCalls
   *
   * @assertion_ids: JSONB:JAVADOC:35; JSONB:JAVADOC:49
   *
   * @test_Strategy: Assert that multiple JsonbConfig.withSerializers calls
   * result in a merge of serializer values configured
   */
  public void testWithSerializersMultipleCalls() throws Fault {
    JsonbConfig jsonbConfig = new JsonbConfig()
        .withSerializers(new SimpleIntegerSerializer())
        .withSerializers(new SimpleContainerSerializer());

    Object serializers = jsonbConfig.getProperty(JsonbConfig.SERIALIZERS).get();
    if (!JsonbSerializer[].class.isAssignableFrom(serializers.getClass())) {
      throw new Fault(
          "Not expected JsonbSerializer array but " + serializers.getClass());
    }

    if (((JsonbSerializer[]) serializers).length != 2) {
      throw new Fault(
          "Failed to configure multiple custom serializers using multiple JsonbConfig.withSerializers method calls.");
    }
  }

  /*
   * @testName: testWithStrictIJson
   *
   * @assertion_ids: JSONB:JAVADOC:35; JSONB:JAVADOC:50
   *
   * @test_Strategy: Assert that JsonbConfig.withStrictIJSON configures strict
   * I-JSON support
   */
  public void testWithStrictIJson() throws Fault {
    JsonbConfig jsonbConfig = new JsonbConfig().withStrictIJSON(true);

    Optional<Object> property = jsonbConfig
        .getProperty(JsonbConfig.STRICT_IJSON);
    if (!property.isPresent() || !(boolean) property.get()) {
      throw new Fault(
          "Failed to configure strict I-JSON support using JsonbConfig.withStrictIJSON method.");
    }
  }
}
