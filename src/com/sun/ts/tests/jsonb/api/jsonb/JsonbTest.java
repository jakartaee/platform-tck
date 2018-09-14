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

package com.sun.ts.tests.jsonb.api.jsonb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jsonb.api.model.SimpleContainer;

/**
 * @test
 * @sources JsonbTest.java
 * @executeClass com.sun.ts.tests.jsonb.api.JsonbTest
 **/
public class JsonbTest extends ServiceEETest {
  private static final long serialVersionUID = 10L;

  private Jsonb jsonb = JsonbBuilder.create();

  public static void main(String[] args) {
    EETest t = new JsonbTest();
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
   * @testName: testFromJsonStringClass
   *
   * @assertion_ids: JSONB:JAVADOC:1
   *
   * @test_Strategy: Assert that Jsonb.fromJson method with String and Class
   * arguments is working as expected
   */
  public Status testFromJsonStringClass() throws Fault {
    SimpleContainer unmarshalledObject = jsonb
        .fromJson("{ \"instance\" : \"Test String\" }", SimpleContainer.class);
    if (!"Test String".equals(unmarshalledObject.getInstance())) {
      throw new Fault(
          "Failed to unmarshal using Jsonb.fromJson method with String and Class arguments.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testFromJsonStringType
   *
   * @assertion_ids: JSONB:JAVADOC:3
   *
   * @test_Strategy: Assert that Jsonb.fromJson method with String and Type
   * arguments is working as expected
   */
  public Status testFromJsonStringType() throws Fault {
    SimpleContainer unmarshalledObject = jsonb
        .fromJson("{ \"instance\" : \"Test String\" }", new SimpleContainer() {
        }.getClass().getGenericSuperclass());
    if (!"Test String".equals(unmarshalledObject.getInstance())) {
      throw new Fault(
          "Failed to unmarshal using Jsonb.fromJson method with String and Type arguments.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testFromJsonReaderClass
   *
   * @assertion_ids: JSONB:JAVADOC:5
   *
   * @test_Strategy: Assert that Jsonb.fromJson method with Reader and Class
   * arguments is working as expected
   */
  public Status testFromJsonReaderClass() throws Fault {
    try (ByteArrayInputStream stream = new ByteArrayInputStream(
        "{ \"instance\" : \"Test String\" }".getBytes(StandardCharsets.UTF_8));
        InputStreamReader reader = new InputStreamReader(stream)) {

      SimpleContainer unmarshalledObject = jsonb.fromJson(reader,
          SimpleContainer.class);
      if (!"Test String".equals(unmarshalledObject.getInstance())) {
        throw new Fault(
            "Failed to unmarshal using Jsonb.fromJson method with Reader and Class arguments.");
      }
    } catch (IOException e) {
      throw new Fault(e);
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testFromJsonReaderType
   *
   * @assertion_ids: JSONB:JAVADOC:7
   *
   * @test_Strategy: Assert that Jsonb.fromJson method with Reader and Type
   * arguments is working as expected
   */
  public Status testFromJsonReaderType() throws Fault {
    try (ByteArrayInputStream stream = new ByteArrayInputStream(
        "{ \"instance\" : \"Test String\" }".getBytes(StandardCharsets.UTF_8));
        InputStreamReader reader = new InputStreamReader(stream)) {

      SimpleContainer unmarshalledObject = jsonb.fromJson(reader,
          new SimpleContainer() {
          }.getClass().getGenericSuperclass());
      if (!"Test String".equals(unmarshalledObject.getInstance())) {
        throw new Fault(
            "Failed to unmarshal using Jsonb.fromJson method with Reader and Type arguments.");
      }
    } catch (IOException e) {
      throw new Fault(e);
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testFromJsonStreamClass
   *
   * @assertion_ids: JSONB:JAVADOC:9
   *
   * @test_Strategy: Assert that Jsonb.fromJson method with InputStream and
   * Class arguments is working as expected
   */
  public Status testFromJsonStreamClass() throws Fault {
    try (ByteArrayInputStream stream = new ByteArrayInputStream(
        "{ \"instance\" : \"Test String\" }"
            .getBytes(StandardCharsets.UTF_8))) {
      SimpleContainer unmarshalledObject = jsonb.fromJson(stream,
          SimpleContainer.class);
      if (!"Test String".equals(unmarshalledObject.getInstance())) {
        throw new Fault(
            "Failed to unmarshal using Jsonb.fromJson method with InputStream and Class arguments.");
      }
    } catch (IOException e) {
      throw new Fault(e);
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testFromJsonStreamType
   *
   * @assertion_ids: JSONB:JAVADOC:11
   *
   * @test_Strategy: Assert that Jsonb.fromJson method with InputStream and
   * Class arguments is working as expected
   */
  public Status testFromJsonStreamType() throws Fault {
    try (ByteArrayInputStream stream = new ByteArrayInputStream(
        "{ \"instance\" : \"Test String\" }"
            .getBytes(StandardCharsets.UTF_8))) {
      SimpleContainer unmarshalledObject = jsonb.fromJson(stream,
          new SimpleContainer() {
          }.getClass().getGenericSuperclass());
      if (!"Test String".equals(unmarshalledObject.getInstance())) {
        throw new Fault(
            "Failed to unmarshal using Jsonb.fromJson method with InputStream and Type arguments.");
      }
    } catch (IOException e) {
      throw new Fault(e);
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testToJsonObject
   *
   * @assertion_ids: JSONB:JAVADOC:13
   *
   * @test_Strategy: Assert that Jsonb.toJson method with Object argument is
   * working as expected
   */
  public Status testToJsonObject() throws Fault {
    String jsonString = jsonb.toJson(new SimpleContainer());
    if (!jsonString.matches("\\{\\s*\"instance\"\\s*:\\s*\"Test\"\\s*}")) {
      throw new Fault(
          "Failed to marshal using Jsonb.toJson method with Object argument.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testToJsonObjectType
   *
   * @assertion_ids: JSONB:JAVADOC:15
   *
   * @test_Strategy: Assert that Jsonb.toJson method with Object and Type
   * arguments is working as expected
   */
  public Status testToJsonObjectType() throws Fault {
    String jsonString = jsonb.toJson(new SimpleContainer(),
        new SimpleContainer() {
        }.getClass().getGenericSuperclass());
    if (!jsonString.matches("\\{\\s*\"instance\"\\s*:\\s*\"Test\"\\s*}")) {
      throw new Fault(
          "Failed to marshal using Jsonb.toJson method with Object and Type arguments.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testToJsonObjectWriter
   *
   * @assertion_ids: JSONB:JAVADOC:17
   *
   * @test_Strategy: Assert that Jsonb.toJson method with Object and Writer
   * arguments is working as expected
   */
  public Status testToJsonObjectWriter() throws Fault {
    try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(stream)) {

      jsonb.toJson(new SimpleContainer(), writer);
      String jsonString = new String(stream.toByteArray(),
          StandardCharsets.UTF_8);
      if (!jsonString.matches("\\{\\s*\"instance\"\\s*:\\s*\"Test\"\\s*}")) {
        throw new Fault(
            "Failed to marshal using Jsonb.toJson method with Object and Writer arguments.");
      }
    } catch (IOException e) {
      throw new Fault(e);
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testToJsonObjectTypeWriter
   *
   * @assertion_ids: JSONB:JAVADOC:19
   *
   * @test_Strategy: Assert that Jsonb.toJson method with Object, Type and
   * Writer arguments is working as expected
   */
  public Status testToJsonObjectTypeWriter() throws Fault {
    try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(stream)) {

      jsonb.toJson(new SimpleContainer(), new SimpleContainer() {
      }.getClass().getGenericSuperclass(), writer);
      String jsonString = new String(stream.toByteArray(),
          StandardCharsets.UTF_8);
      if (!jsonString.matches("\\{\\s*\"instance\"\\s*:\\s*\"Test\"\\s*}")) {
        throw new Fault(
            "Failed to marshal using Jsonb.toJson method with Object, Type and Writer arguments.");
      }
    } catch (IOException e) {
      throw new Fault(e);
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testToJsonObjectStream
   *
   * @assertion_ids: JSONB:JAVADOC:21
   *
   * @test_Strategy: Assert that Jsonb.toJson method with Object and
   * OutputStream arguments is working as expected
   */
  public Status testToJsonObjectStream() throws Fault {
    try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
      jsonb.toJson(new SimpleContainer(), stream);
      String jsonString = new String(stream.toByteArray(),
          StandardCharsets.UTF_8);
      if (!jsonString.matches("\\{\\s*\"instance\"\\s*:\\s*\"Test\"\\s*}")) {
        throw new Fault(
            "Failed to marshal using Jsonb.toJson method with Object and OutputStream arguments.");
      }
    } catch (IOException e) {
      throw new Fault(e);
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testToJsonObjectTypeStream
   *
   * @assertion_ids: JSONB:JAVADOC:23
   *
   * @test_Strategy: Assert that Jsonb.toJson method with Object, Type and
   * OutputStream arguments is working as expected
   */
  public Status testToJsonObjectTypeStream() throws Fault {
    try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
      jsonb.toJson(new SimpleContainer(), new SimpleContainer() {
      }.getClass().getGenericSuperclass(), stream);
      String jsonString = new String(stream.toByteArray(),
          StandardCharsets.UTF_8);
      if (!jsonString.matches("\\{\\s*\"instance\"\\s*:\\s*\"Test\"\\s*}")) {
        throw new Fault(
            "Failed to marshal using Jsonb.toJson method with Object, Type and OutputStream arguments.");
      }
    } catch (IOException e) {
      throw new Fault(e);
    }

    return Status.passed("OK");
  }
}
