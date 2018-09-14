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

package com.sun.ts.tests.jsonb.customizedmapping.nullhandling;

import java.util.Properties;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jsonb.customizedmapping.nullhandling.model.NillableContainer;
import com.sun.ts.tests.jsonb.customizedmapping.nullhandling.model.NillablePropertyContainer;
import com.sun.ts.tests.jsonb.customizedmapping.nullhandling.model.NillablePropertyNonNillableContainer;
import com.sun.ts.tests.jsonb.customizedmapping.nullhandling.model.NonNillableContainer;
import com.sun.ts.tests.jsonb.customizedmapping.nullhandling.model.NonNillablePropertyContainer;
import com.sun.ts.tests.jsonb.customizedmapping.nullhandling.model.NonNillablePropertyNillableContainer;
import com.sun.ts.tests.jsonb.customizedmapping.nullhandling.model.SimpleContainer;
import com.sun.ts.tests.jsonb.customizedmapping.nullhandling.model.nillable.NillablePackageNillablePropertyNonNillableContainer;
import com.sun.ts.tests.jsonb.customizedmapping.nullhandling.model.nillable.NillablePackageNonNillablePropertyContainer;
import com.sun.ts.tests.jsonb.customizedmapping.nullhandling.model.nillable.NillablePackageSimpleContainer;
import com.sun.ts.tests.jsonb.customizedmapping.nullhandling.model.nonnillable.NonNillablePackageNillableContainer;
import com.sun.ts.tests.jsonb.customizedmapping.nullhandling.model.nonnillable.NonNillablePackageNonNillablePropertyNillableContainer;
import com.sun.ts.tests.jsonb.customizedmapping.nullhandling.model.nonnillable.NonNillablePackageSimpleContainer;

/**
 * @test
 * @sources NullHandlingCustomizationTest.java
 * @executeClass com.sun.ts.tests.jsonb.customizedmapping.nullhandling.NullHandlingCustomizationTest
 **/
public class NullHandlingCustomizationTest extends ServiceEETest {
  private static final long serialVersionUID = 10L;

  private final Jsonb jsonb = JsonbBuilder.create();

  public static void main(String[] args) {
    EETest t = new NullHandlingCustomizationTest();
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
   * @testName: testNillableType
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.3.1-1
   *
   * @test_Strategy: Assert that type annotated as JsonbNillable includes null
   * properties in marshalling
   */
  public Status testNillableType() throws Fault {
    String jsonString = jsonb.toJson(new NillableContainer());
    if (!jsonString
        .matches("\\{\\s*\"stringInstance\"\\s*\\:\\s*null\\s*\\}")) {
      throw new Fault(
          "Failed to correctly marshal null property of type annotated as JsonbNillable.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testNillablePackage
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.3.1-1
   *
   * @test_Strategy: Assert that type under package annotated as JsonbNillable
   * includes null properties in marshalling
   */
  public Status testNillablePackage() throws Fault {
    String jsonString = jsonb.toJson(new NillablePackageSimpleContainer());
    if (!jsonString
        .matches("\\{\\s*\"stringInstance\"\\s*\\:\\s*null\\s*\\}")) {
      throw new Fault(
          "Failed to correctly marshal null property of type under package annotated as JsonbNillable.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testNillableProperty
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.3.1-2
   *
   * @test_Strategy: Assert that property annotated as JsonbProperty with
   * nillable = true having null value is included in marshalling
   */
  public Status testNillableProperty() throws Fault {
    String jsonString = jsonb.toJson(new NillablePropertyContainer());
    if (!jsonString
        .matches("\\{\\s*\"nillableStringInstance\"\\s*\\:\\s*null\\s*\\}")) {
      throw new Fault(
          "Failed to correctly marshal null property annotated as JsonbProperty with nillable = true.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testNullValuesConfig
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.3.2-1
   *
   * @test_Strategy: Assert that null properties are included in marshalling
   * when using JsonbConfig().withNullValues(true)
   */
  public Status testNullValuesConfig() throws Fault {
    Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withNullValues(true));
    String jsonString = jsonb.toJson(new SimpleContainer());
    if (!jsonString
        .matches("\\{\\s*\"stringInstance\"\\s*\\:\\s*null\\s*\\}")) {
      throw new Fault(
          "Failed to correctly marshal null properties when using JsonbConfig().withNullValues(true).");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testNillableTypeNonNillableProperty
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.3-1; JSONB:SPEC:JSB-4.3.1-1;
   * JSONB:SPEC:JSB-4.3.1-2; JSONB:SPEC:JSB-4.3.1-3
   *
   * @test_Strategy: Assert that null property annotated as JsonbProperty with
   * nillable = false of type annotated as JsonbNillable is ignored in
   * marshalling
   */
  public Status testNillableTypeNonNillableProperty() throws Fault {
    String jsonString = jsonb
        .toJson(new NonNillablePropertyNillableContainer());
    if (!jsonString.matches("\\{\\s*\\}")) {
      throw new Fault(
          "Failed to correctly ignore null property annotated as JsonbProperty with nillable = false of type annotated as JsonbNillable.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testNillablePackageNonNillableProperty
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.3-1; JSONB:SPEC:JSB-4.3.1-1;
   * JSONB:SPEC:JSB-4.3.1-2; JSONB:SPEC:JSB-4.3.1-3
   *
   * @test_Strategy: Assert that property annotated as JsonbProperty with
   * nillable = false of type under package annotated as JsonbNillable is
   * ignored in marshalling
   */
  public Status testNillablePackageNonNillableProperty() throws Fault {
    String jsonString = jsonb
        .toJson(new NillablePackageNonNillablePropertyContainer());
    if (!jsonString.matches("\\{\\s*\\}")) {
      throw new Fault(
          "Failed to correctly ignore null property annotated as JsonbProperty(nillable = false) of type under package annotated as JsonbNillable.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testNillablePackageNonNillableTypeNillableProperty
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.3-1; JSONB:SPEC:JSB-4.3.1-1;
   * JSONB:SPEC:JSB-4.3.1-2; JSONB:SPEC:JSB-4.3.1-3
   *
   * @test_Strategy: Assert that property annotated as JsonbProperty with
   * nillable = true of type annotated as JsonbNillable(false) under package
   * annotated as JsonbNillable is included in marshalling
   */
  public Status testNillablePackageNonNillableTypeNillableProperty()
      throws Fault {
    String jsonString = jsonb
        .toJson(new NillablePackageNillablePropertyNonNillableContainer());
    if (!jsonString
        .matches("\\{\\s*\"nillableStringInstance\"\\s*\\:\\s*null\\s*\\}")) {
      throw new Fault(
          "Failed to correctly marshal null property annotated as JsonbProperty(nillable = true) of type annotated as JsonbNillable(false) under package annotated as JsonbNillable.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testNullValuesConfigNonNillablePackage
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.3-1; JSONB:SPEC:JSB-4.3.1-1;
   * JSONB:SPEC:JSB-4.3.1-3; JSONB:SPEC:JSB-4.3.2-1
   *
   * @test_Strategy: Assert that null properties are ignored in marshalling when
   * using JsonbConfig().withNullValues(true) and type under package annotated
   * as JsonbNillable(false)
   */
  public Status testNullValuesConfigNonNillablePackage() throws Fault {
    Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withNullValues(true));
    String jsonString = jsonb.toJson(new NonNillablePackageSimpleContainer());
    if (!jsonString.matches("\\{\\s*\\}")) {
      throw new Fault(
          "Failed to correctly ignore null properties when using JsonbConfig().withNullValues(true) and type under package annotated as JsonbNillable(false).");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testNullValuesConfigNonNillablePackageNillableType
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.3-1; JSONB:SPEC:JSB-4.3.1-1;
   * JSONB:SPEC:JSB-4.3.1-3; JSONB:SPEC:JSB-4.3.2-1
   *
   * @test_Strategy: Assert that null properties are included in marshalling
   * when using JsonbConfig().withNullValues(true) and type annotated as
   * JsonbNillable under package annotated as JsonbNillable(false)
   */
  public Status testNullValuesConfigNonNillablePackageNillableType()
      throws Fault {
    Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withNullValues(true));
    String jsonString = jsonb.toJson(new NonNillablePackageNillableContainer());
    if (!jsonString
        .matches("\\{\\s*\"stringInstance\"\\s*\\:\\s*null\\s*\\}")) {
      throw new Fault(
          "Failed to correctly marshal null properties when using JsonbConfig().withNullValues(true) and type annotated as JsonbNillable under package annotated as JsonbNillable(false).");
    }

    return Status.passed("OK");
  }

  /*
   * @testName:
   * testNullValuesConfigNonNillablePackageNillableTypeNonNillableProperty
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.3-1; JSONB:SPEC:JSB-4.3.1-1;
   * JSONB:SPEC:JSB-4.3.1-2; JSONB:SPEC:JSB-4.3.1-3; JSONB:SPEC:JSB-4.3.2-1
   *
   * @test_Strategy: Assert that null properties annotated as JsonbProperty with
   * nillable = false are ignored in marshalling when using
   * JsonbConfig().withNullValues(true) and type annotated as JsonbNillable
   * under package annotated as JsonbNillable(false)
   */
  public Status testNullValuesConfigNonNillablePackageNillableTypeNonNillableProperty()
      throws Fault {
    Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withNullValues(true));
    String jsonString = jsonb
        .toJson(new NonNillablePackageNonNillablePropertyNillableContainer());
    if (!jsonString.matches("\\{\\s*\\}")) {
      throw new Fault(
          "Failed to correctly ignore null property annotated as JsonbProperty with nillable = false when using JsonbConfig().withNullValues(true) and type annotated as JsonbNillable under package annotated as JsonbNillable(false).");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testNullValuesConfigNonNillableType
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.3-1; JSONB:SPEC:JSB-4.3.1-1;
   * JSONB:SPEC:JSB-4.3.1-3; JSONB:SPEC:JSB-4.3.2-1
   *
   * @test_Strategy: Assert that null properties are ignored in marshalling when
   * using JsonbConfig().withNullValues(true) and type annotated as
   * JsonbNillable(false)
   */
  public Status testNullValuesConfigNonNillableType() throws Fault {
    Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withNullValues(true));
    String jsonString = jsonb.toJson(new NonNillableContainer());
    if (!jsonString.matches("\\{\\s*\\}")) {
      throw new Fault(
          "Failed to correctly ignore null property when using JsonbConfig().withNullValues(true) and type annotated as JsonbNillable(false).");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testNullValuesConfigNonNillableTypeNillableProperty
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.3-1; JSONB:SPEC:JSB-4.3.1-1;
   * JSONB:SPEC:JSB-4.3.1-2; JSONB:SPEC:JSB-4.3.1-3; JSONB:SPEC:JSB-4.3.2-1
   *
   * @test_Strategy: Assert that null properties annotated as JsonbProperty with
   * nillable = true are included in marshalling when using
   * JsonbConfig().withNullValues(true) and type annotated as
   * JsonbNillable(false)
   */
  public Status testNullValuesConfigNonNillableTypeNillableProperty()
      throws Fault {
    Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withNullValues(true));
    String jsonString = jsonb
        .toJson(new NillablePropertyNonNillableContainer());
    if (!jsonString
        .matches("\\{\\s*\"nillableStringInstance\"\\s*\\:\\s*null\\s*\\}")) {
      throw new Fault(
          "Failed to correctly include null property annotated as JsonbProperty with nillable = true when using JsonbConfig().withNullValues(true) and type annotated as JsonbNillable(false).");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testNullValuesConfigNonNillableProperty
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.3-1; JSONB:SPEC:JSB-4.3.1-2;
   * JSONB:SPEC:JSB-4.3.1-3; JSONB:SPEC:JSB-4.3.2-1
   *
   * @test_Strategy: Assert that null properties annotated as JsonbProperty with
   * nillable = false are ignored in marshalling when using
   * JsonbConfig().withNullValues(true)
   */
  public Status testNullValuesConfigNonNillableProperty() throws Fault {
    Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withNullValues(true));
    String jsonString = jsonb.toJson(new NonNillablePropertyContainer());
    if (!jsonString.matches("\\{\\s*\\}")) {
      throw new Fault(
          "Failed to correctly ignore null property annotated as JsonbProperty with nillable = false when using JsonbConfig().withNullValues(true).");
    }

    return Status.passed("OK");
  }
}
