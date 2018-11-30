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

package com.sun.ts.tests.jsonb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.function.BiPredicate;
import java.util.regex.Pattern;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;

public class MappingTester<T> {
  private static final String JSON_PATTERN_PREFIX = "\\{\\s*";

  private static final String JSON_PATTERN_PROPERTY = "\"instance\"\\s*:\\s*";

  private static final String JSON_PATTERN_SUFFIX = "\\s*\\}";

  private static final String JSON_PREFIX = "{ ";

  private static final String JSON_PROPERTY = "\"instance\" : ";

  private static final String JSON_SUFFIX = " }";

  // 53 means max bit value of number with sign bit included
  private static final int MAX_BIT_SIZE = 53;

  // -1022 is the lowest range of the exponent
  // more https://en.wikipedia.org/wiki/Exponent_bias
  private static final int MIN_RANGE = -1022;

  // 1023 is the highest range of the exponent
  // more https://en.wikipedia.org/wiki/Exponent_bias
  private static final int MAX_RANGE = 1023;

  private final Jsonb jsonb = JsonbBuilder.create();

  private final Class<? extends TypeContainer<T>> typeContainerClass;

  private final boolean nillable;

  private T testValue;

  private BiPredicate<T, T> testUnmarshall = (f, s) -> {
    return f.equals(s);
  };

  private String fixedRegExp = null;

  public MappingTester(Class<? extends TypeContainer<T>> typeContainerClass) {
    this(typeContainerClass, true);
  }

  public MappingTester(Class<? extends TypeContainer<T>> typeContainerClass,
      boolean nillable) {
    this.typeContainerClass = typeContainerClass;
    this.nillable = nillable;
  }

  public Status test(T value, String expectedRepresentation) throws Fault {
    try {
      this.testValue = value;
      return combine(testMarshalling(value, expectedRepresentation),
          testMarshallingToStream(value, expectedRepresentation),
          testMarshallingToWriter(value, expectedRepresentation),
          testMarshallingByType(value, expectedRepresentation),
          testMarshallingByTypeToStream(value, expectedRepresentation),
          testMarshallingByTypeToWriter(value, expectedRepresentation),
          testUnmarshallingByClass(value, expectedRepresentation),
          testUnmarshallingByClassFromStream(value, expectedRepresentation),
          testUnmarshallingByClassFromReader(value, expectedRepresentation),
          testUnmarshallingByType(value, expectedRepresentation),
          testUnmarshallingByTypeFromStream(value, expectedRepresentation),
          testUnmarshallingByTypeFromReader(value, expectedRepresentation));
    } catch (IllegalAccessException | InstantiationException e) {
      throw new Fault(e.getMessage(), e);
    }
  }

  public static Status combine(Status... statusValues) throws Fault {
    if (statusValues.length == 0) {
      throw new Fault("No status information provided.");
    }

    StringBuilder failures = new StringBuilder();
    for (Status status : statusValues) {
      if (status.isFailed() || status.isError()) {
        failures.append('{').append(status.getReason()).append('}');
      }
    }

    if (failures.length() == 0) {
      return Status.passed("OK");
    } else {
      throw new Fault(failures.toString());
    }
  }

  private String getExpectedJsonString(String expectedRepresentation) {
    if (nillable || !expectedRepresentation.isEmpty()) {
      return JSON_PATTERN_PREFIX + JSON_PATTERN_PROPERTY
          + fixExpectedRepresentation(expectedRepresentation)
          + JSON_PATTERN_SUFFIX;
    } else {
      return JSON_PATTERN_PREFIX + JSON_PATTERN_SUFFIX;
    }
  }

  private String getJsonString(String expectedRepresentation) {
    if (nillable || !expectedRepresentation.isEmpty()) {
      return JSON_PREFIX + JSON_PROPERTY + expectedRepresentation + JSON_SUFFIX;
    } else {
      return JSON_PREFIX + JSON_SUFFIX;
    }
  }

  private String fixExpectedRepresentation(String representation) {
    // JSON cannot parse regexp, but the test is about to check for regexp
    if (fixedRegExp != null)
      return fixedRegExp;
    // value Float.MAX_VALUE can have E38 or E+38, depending on impl
    representation = representation.replace("E38", "E[\\+]?\\+38");
    if (!Number.class.isInstance(testValue)) {
      representation = Pattern.quote(representation);
      // quote numbers that do not fit into double decimal precision
    } else {
      if (Long.class.isInstance(testValue)) {
        Long longTestValue = Long.class.cast(testValue);
        if (!isIEEE754(BigDecimal.valueOf(longTestValue))) {
          representation = quote(representation);
        }
      } else if (BigDecimal.class.isInstance(testValue)) {
        if (!isIEEE754((BigDecimal)testValue)) {
          representation = quote(representation);
        }
      } else if (BigInteger.class.isInstance(testValue)) {
        if (!isIEEE754(new BigDecimal((BigInteger)testValue))) {
          representation = quote(representation);
        }
      }
    }
    return representation;
  }

  private Status testMarshalling(T value, String expectedRepresentation)
      throws IllegalAccessException, InstantiationException {
    TypeContainer<T> container = typeContainerClass.newInstance();
    container.setInstance(value);

    String jsonString = jsonb.toJson(container);
    if (jsonString.matches(getExpectedJsonString(expectedRepresentation))) {
      return Status.passed("OK");
    } else {
      System.out.append("jsonString ").println(jsonString);
      System.out.append("does not match expected ")
          .println(getExpectedJsonString(expectedRepresentation));
      return Status.failed("[testMarshalling] - Failed to correctly marshal "
          + value.getClass().getName() + " property with value " + value);
    }
  }

  private Status testMarshallingToStream(T value, String expectedRepresentation)
      throws IllegalAccessException, InstantiationException {
    TypeContainer<T> container = typeContainerClass.newInstance();
    container.setInstance(value);

    try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
      jsonb.toJson(container, stream);
      String jsonString = new String(stream.toByteArray(),
          StandardCharsets.UTF_8);
      if (jsonString.matches(getExpectedJsonString(expectedRepresentation))) {
        return Status.passed("OK");
      } else {
        System.out.append("jsonString ").println(jsonString);
        System.out.append("does not match expected ")
            .println(getExpectedJsonString(expectedRepresentation));
        return Status
            .failed("[testMarshallingToStream] - Failed to correctly marshal "
                + value.getClass().getName() + " property with value " + value);
      }
    } catch (IOException e) {
      return Status.error(e.getMessage());
    }
  }

  private Status testMarshallingToWriter(T value, String expectedRepresentation)
      throws IllegalAccessException, InstantiationException {
    TypeContainer<T> container = typeContainerClass.newInstance();
    container.setInstance(value);

    try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(stream,
            StandardCharsets.UTF_8)) {
      jsonb.toJson(container, writer);
      String jsonString = new String(stream.toByteArray(),
          StandardCharsets.UTF_8);
      if (jsonString.matches(getExpectedJsonString(expectedRepresentation))) {
        return Status.passed("OK");
      } else {
        System.out.append("jsonString ").println(jsonString);
        System.out.append("does not match expected ")
            .println(getExpectedJsonString(expectedRepresentation));
        return Status
            .failed("[testMarshallingToWriter] - Failed to correctly marshal "
                + value.getClass().getName() + " property with value " + value);
      }
    } catch (IOException e) {
      return Status.error(e.getMessage());
    }
  }

  private Status testMarshallingByType(T value, String expectedRepresentation)
      throws IllegalAccessException, InstantiationException {
    TypeContainer<T> container = typeContainerClass.newInstance();
    container.setInstance(value);

    String jsonString = jsonb.toJson(container, TypeContainer.class);
    if (jsonString.matches(getExpectedJsonString(expectedRepresentation))) {
      return Status.passed("OK");
    } else {
      System.out.append("jsonString ").println(jsonString);
      System.out.append("does not match expected ")
          .println(getExpectedJsonString(expectedRepresentation));
      return Status
          .failed("[testMarshallingByType] - Failed to correctly marshal "
              + value.getClass().getName() + " property with value " + value);
    }
  }

  private Status testMarshallingByTypeToStream(T value,
      String expectedRepresentation)
      throws IllegalAccessException, InstantiationException {
    TypeContainer<T> container = typeContainerClass.newInstance();
    container.setInstance(value);

    try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
      jsonb.toJson(container, TypeContainer.class, stream);
      String jsonString = new String(stream.toByteArray(),
          StandardCharsets.UTF_8);
      if (jsonString.matches(getExpectedJsonString(expectedRepresentation))) {
        return Status.passed("OK");
      } else {
        System.out.append("jsonString ").println(jsonString);
        System.out.append("does not match expected ")
            .println(getExpectedJsonString(expectedRepresentation));
        return Status.failed(
            "[testMarshallingByTypeToStream] - Failed to correctly marshal "
                + value.getClass().getName() + " property with value " + value);
      }
    } catch (IOException e) {
      return Status.error(e.getMessage());
    }
  }

  private Status testMarshallingByTypeToWriter(T value,
      String expectedRepresentation)
      throws IllegalAccessException, InstantiationException {
    TypeContainer<T> container = typeContainerClass.newInstance();
    container.setInstance(value);

    try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(stream,
            StandardCharsets.UTF_8)) {

      jsonb.toJson(container, TypeContainer.class, writer);
      String jsonString = new String(stream.toByteArray(),
          StandardCharsets.UTF_8);
      if (jsonString.matches(getExpectedJsonString(expectedRepresentation))) {
        return Status.passed("OK");
      } else {
        System.out.append("jsonString ").println(jsonString);
        System.out.append("does not match expected ")
            .println(getExpectedJsonString(expectedRepresentation));
        return Status.failed(
            "[testMarshallingByTypeToWriter] - Failed to correctly marshal "
                + value.getClass().getName() + " property with value " + value);
      }
    } catch (IOException e) {
      return Status.error(e.getMessage());
    }
  }

  private Status testUnmarshallingByClass(T value,
      String expectedRepresentation) {
    String jsonString = getJsonString(expectedRepresentation);

    TypeContainer<T> typeContainer = jsonb.fromJson(jsonString,
        typeContainerClass);
    if (testUnmarshall(value, typeContainer.getInstance())) {
      return Status.passed("OK");
    } else {
      System.out.append("value ").println(typeContainer.getInstance());
      System.out.append("does not match expected ").println(value);
      return Status
          .failed("[testUnmarshallingByClass] - Failed to correctly unmarshal "
              + value.getClass().getName() + " property with value " + value);
    }
  }

  private Status testUnmarshallingByClassFromStream(T value,
      String expectedRepresentation) {
    String jsonString = getJsonString(expectedRepresentation);
    try (ByteArrayInputStream stream = new ByteArrayInputStream(
        jsonString.getBytes(StandardCharsets.UTF_8))) {
      TypeContainer<T> typeContainer = jsonb.fromJson(stream,
          typeContainerClass);
      if (testUnmarshall(value, typeContainer.getInstance())) {
        return Status.passed("OK");
      } else {
        System.out.append("value ").println(typeContainer.getInstance());
        System.out.append("does not match expected ").println(value);
        return Status.failed(
            "[testUnmarshallingByClassFromStream] - Failed to correctly unmarshal "
                + value.getClass().getName() + " property with value " + value);
      }
    } catch (IOException e) {
      return Status.error(e.getMessage());
    }
  }

  private Status testUnmarshallingByClassFromReader(T value,
      String expectedRepresentation) {
    String jsonString = getJsonString(expectedRepresentation);
    try (
        ByteArrayInputStream stream = new ByteArrayInputStream(
            jsonString.getBytes(StandardCharsets.UTF_8));
        InputStreamReader reader = new InputStreamReader(stream,
            StandardCharsets.UTF_8)) {

      TypeContainer<T> typeContainer = jsonb.fromJson(reader,
          typeContainerClass);
      if (testUnmarshall(value, typeContainer.getInstance())) {
        return Status.passed("OK");
      } else {
        System.out.append("value ").println(typeContainer.getInstance());
        System.out.append("does not match expected ").println(value);
        return Status.failed(
            "[testUnmarshallingByClassFromReader] - Failed to correctly unmarshal "
                + value.getClass().getName() + " property with value " + value);
      }
    } catch (IOException e) {
      return Status.error(e.getMessage());
    }
  }

  private Status testUnmarshallingByType(T value,
      String expectedRepresentation) {
    String jsonString = getJsonString(expectedRepresentation);

    TypeContainer<T> typeContainer = jsonb.fromJson(jsonString,
        (Type) typeContainerClass);
    if (testUnmarshall(value, typeContainer.getInstance())) {
      return Status.passed("OK");
    } else {
      System.out.append("value ").println(typeContainer.getInstance());
      System.out.append("does not match expected ").println(value);
      return Status
          .failed("[testUnmarshallingByType] - Failed to correctly unmarshal "
              + value.getClass().getName() + " property with value " + value);
    }
  }

  private Status testUnmarshallingByTypeFromStream(T value,
      String expectedRepresentation) {
    String jsonString = getJsonString(expectedRepresentation);
    try (ByteArrayInputStream stream = new ByteArrayInputStream(
        jsonString.getBytes(StandardCharsets.UTF_8))) {
      TypeContainer<T> typeContainer = jsonb.fromJson(stream,
          (Type) typeContainerClass);
      if (testUnmarshall(value, typeContainer.getInstance())) {
        return Status.passed("OK");
      } else {
        System.out.append("value ").println(typeContainer.getInstance());
        System.out.append("does not match expected ").println(value);
        return Status.failed(
            "[testUnmarshallingByTypeFromStream] - Failed to correctly unmarshal "
                + value.getClass().getName() + " property with value " + value);
      }
    } catch (IOException e) {
      return Status.error(e.getMessage());
    }
  }

  private Status testUnmarshallingByTypeFromReader(T value,
      String expectedRepresentation) {
    String jsonString = getJsonString(expectedRepresentation);
    try (
        ByteArrayInputStream stream = new ByteArrayInputStream(
            jsonString.getBytes(StandardCharsets.UTF_8));
        InputStreamReader reader = new InputStreamReader(stream,
            StandardCharsets.UTF_8)) {

      TypeContainer<T> typeContainer = jsonb.fromJson(reader,
          (Type) typeContainerClass);
      if (testUnmarshall(value, typeContainer.getInstance())) {
        return Status.passed("OK");
      } else {
        System.out.append("value ").println(typeContainer.getInstance());
        System.out.append("does not match expected ").println(value);
        return Status.failed(
            "[testUnmarshallingByTypeFromReader] - Failed to correctly unmarshal "
                + value.getClass().getName() + " property with value " + value);
      }
    } catch (IOException e) {
      return Status.error(e.getMessage());
    }
  }

  protected boolean testUnmarshall(T value, T fromJson) {
    return testUnmarshall.test(value, fromJson);
  }

  public MappingTester<T> setUnmarshallTestPredicate(
      BiPredicate<T, T> testPredicate) {
    this.testUnmarshall = testPredicate;
    return this;
  }

  public MappingTester<T> setMarshallExpectedRegExp(String regExp) {
    this.fixedRegExp = regExp;
    return this;
  }

  /**
   * Checks whether the value of {@link BigDecimal} matches format IEEE-754
   *
   * @param value
   *            value which is going to be checked
   * @return true if value matches format IEEE-754
   */
  private static boolean isIEEE754(BigDecimal value) {
    // scale of the number
    int scale = value.scale();
    // bit value of number without scale
    int valBits = value.unscaledValue().abs().bitLength();
    // bit value of scaled number
    int intBitsScaled = value.toBigInteger().bitLength();
    // Number whose bit length is than 53 or is not in range is considered as non
    // IEEE 754-2008 binary64 compliant
    return valBits <= MAX_BIT_SIZE && intBitsScaled <= MAX_BIT_SIZE && MIN_RANGE <= scale && scale <= MAX_RANGE;
  }

  private String quote(String value) {
    return new StringBuilder(value.length() + 2).append('"').append(value).append('"').toString();
  }
}
