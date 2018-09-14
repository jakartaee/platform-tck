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

package com.sun.ts.tests.jsonb.customizedmapping.numberformat;

import java.util.Properties;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jsonb.customizedmapping.numberformat.model.AccessorCustomizedDoubleContainer;
import com.sun.ts.tests.jsonb.customizedmapping.numberformat.model.FieldCustomizedDoubleContainer;
import com.sun.ts.tests.jsonb.customizedmapping.numberformat.model.TypeCustomizedDoubleContainer;
import com.sun.ts.tests.jsonb.customizedmapping.numberformat.model.TypeCustomizedFieldOverriddenDoubleContainer;
import com.sun.ts.tests.jsonb.customizedmapping.numberformat.model.customized.PackageCustomizedDoubleContainer;
import com.sun.ts.tests.jsonb.customizedmapping.numberformat.model.customized.PackageCustomizedTypeOverriddenDoubleContainer;
import com.sun.ts.tests.jsonb.customizedmapping.numberformat.model.customized.PackageCustomizedTypeOverriddenFieldOverriddenDoubleContainer;

/**
 * @test
 * @sources NumberFormatCustomizationTest.java
 * @executeClass com.sun.ts.tests.jsonb.customizedmapping.numberformat.NumberFormatCustomizationTest
 **/
public class NumberFormatCustomizationTest extends ServiceEETest {
  private static final long serialVersionUID = 10L;

  private static final String FRENCH_NUMBER = "\"123\\u00a0456,789\"";

  private final Jsonb jsonb = JsonbBuilder.create();

  public static void main(String[] args) {
    EETest t = new NumberFormatCustomizationTest();
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
   * @testName: testNumberFormatPackage
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.9-1
   *
   * @test_Strategy: Assert that package annotation with JsonbNumberFormat is
   * correctly applied
   */
  public Status testNumberFormatPackage() throws Fault {
    String jsonString = jsonb.toJson(new PackageCustomizedDoubleContainer() {
      {
        setInstance(123456.789);
      }
    });
    if (!jsonString
        .matches("\\{\\s*\"instance\"\\s*:\\s*\"123.456,8\"\\s*\\}")) {
      throw new Fault(
          "Failed to correctly customize number format during marshalling using JsonbNumberFormat annotation on package.");
    }

    PackageCustomizedDoubleContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : \"123.456,789\" }",
        PackageCustomizedDoubleContainer.class);
    if (unmarshalledObject.getInstance() != 123456.789) {
      throw new Fault(
          "Failed to correctly customize number format during unmarshalling using JsonbNumberFormat annotation on package.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testNumberFormatType
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.9-1
   *
   * @test_Strategy: Assert that type annotation with JsonbNumberFormat is
   * correctly applied
   */
  public Status testNumberFormatType() throws Fault {
    String jsonString = jsonb.toJson(new TypeCustomizedDoubleContainer() {
      {
        setInstance(123456.789);
      }
    });
    if (!jsonString
        .matches("\\{\\s*\"instance\"\\s*:\\s*\"123,456.79\"\\s*\\}")) {
      throw new Fault(
          "Failed to correctly customize number format during marshalling using JsonbNumberFormat annotation on type.");
    }

    TypeCustomizedDoubleContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : \"123,456.789\" }",
        TypeCustomizedDoubleContainer.class);
    if (unmarshalledObject.getInstance() != 123456.789) {
      throw new Fault(
          "Failed to correctly customize number format during unmarshalling using JsonbNumberFormat annotation on type.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testNumberFormatField
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.9-1
   *
   * @test_Strategy: Assert that field annotation with JsonbNumberFormat is
   * correctly applied
   */
  public Status testNumberFormatField() throws Fault {
    String jsonString = jsonb.toJson(new FieldCustomizedDoubleContainer() {
      {
        setInstance(123456.789);
      }
    });
    if (!jsonString
        .matches("\\{\\s*\"instance\"\\s*:\\s*\"123\\u00a0456,789\"\\s*\\}")) {
      throw new Fault(
          "Failed to correctly customize number format during marshalling using JsonbNumberFormat annotation on field.");
    }

    FieldCustomizedDoubleContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : " + FRENCH_NUMBER + " }",
        FieldCustomizedDoubleContainer.class);
    if (unmarshalledObject.getInstance() != 123456.789) {
      throw new Fault(
          "Failed to correctly customize number format during unmarshalling using JsonbNumberFormat annotation on field.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testNumberFormatAccessors
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.9-1
   *
   * @test_Strategy: Assert that accessor annotation with JsonbNumberFormat is
   * correctly individually applied for marshalling and unmarshalling
   */
  public Status testNumberFormatAccessors() throws Fault {
    String jsonString = jsonb.toJson(new AccessorCustomizedDoubleContainer() {
      {
        setInstance(123456.789);
      }
    });
    if (!jsonString
        .matches("\\{\\s*\"instance\"\\s*:\\s*\"123,456.79\"\\s*\\}")) {
      throw new Fault(
          "Failed to correctly customize number format during marshalling using JsonbNumberFormat annotation on getter.");
    }

    AccessorCustomizedDoubleContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : " + FRENCH_NUMBER + " }",
        AccessorCustomizedDoubleContainer.class);
    if (unmarshalledObject.getInstance() != 123456.789) {
      throw new Fault(
          "Failed to correctly customize number format during unmarshalling using JsonbNumberFormat annotation on setter.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testNumberFormatPackageTypeOverride
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.9-1; JSONB:SPEC:JSB-4.9-2
   *
   * @test_Strategy: Assert that package annotation with JsonbNumberFormat is
   * correctly overridden by type annotation with JsonbNumberFormat
   */
  public Status testNumberFormatPackageTypeOverride() throws Fault {
    String jsonString = jsonb
        .toJson(new PackageCustomizedTypeOverriddenDoubleContainer() {
          {
            setInstance(123456.789);
          }
        });
    if (!jsonString
        .matches("\\{\\s*\"instance\"\\s*:\\s*\"123,456.79\"\\s*\\}")) {
      throw new Fault(
          "Failed to correctly override number format customization using JsonbNumberFormat annotation on package during marshalling using JsonbNumberFormat annotation on type.");
    }

    PackageCustomizedTypeOverriddenDoubleContainer unmarshalledObject = jsonb
        .fromJson("{ \"instance\" : \"123,456.789\" }",
            PackageCustomizedTypeOverriddenDoubleContainer.class);
    if (unmarshalledObject.getInstance() != 123456.789) {
      throw new Fault(
          "Failed to correctly override number format customization using JsonbNumberFormat annotation on package during unmarshalling using JsonbNumberFormat annotation on type.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testNumberFormatTypeFieldOverride
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.9-1; JSONB:SPEC:JSB-4.9-2
   *
   * @test_Strategy: Assert that type annotation with JsonbNumberFormat is
   * correctly overridden by field annotation with JsonbNumberFormat
   */
  public Status testNumberFormatTypeFieldOverride() throws Fault {
    String jsonString = jsonb
        .toJson(new TypeCustomizedFieldOverriddenDoubleContainer() {
          {
            setInstance(123456.789);
          }
        });
    if (!jsonString
        .matches("\\{\\s*\"instance\"\\s*:\\s*\"123,456.8\"\\s*\\}")) {
      throw new Fault(
          "Failed to correctly customize number format during marshalling using JsonbNumberFormat annotation on type.");
    }

    TypeCustomizedFieldOverriddenDoubleContainer unmarshalledObject = jsonb
        .fromJson("{ \"instance\" : \"123,456.789\" }",
            TypeCustomizedFieldOverriddenDoubleContainer.class);
    if (unmarshalledObject.getInstance() != 123456.789) {
      throw new Fault(
          "Failed to correctly customize number format during unmarshalling using JsonbNumberFormat annotation on type.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testNumberFormatPackageTypeOverrideFieldOverride
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.9-1; JSONB:SPEC:JSB-4.9-2
   *
   * @test_Strategy: Assert that package and type annotation with
   * JsonbNumberFormat is correctly overridden by field annotation with
   * JsonbNumberFormat
   */
  public Status testNumberFormatPackageTypeOverrideFieldOverride()
      throws Fault {
    String jsonString = jsonb.toJson(
        new PackageCustomizedTypeOverriddenFieldOverriddenDoubleContainer() {
          {
            setInstance(123456.789);
          }
        });
    if (!jsonString
        .matches("\\{\\s*\"instance\"\\s*:\\s*\"123.456,789\"\\s*\\}")) {
      throw new Fault(
          "Failed to correctly override number format customization using JsonbNumberFormat annotation on package during marshalling using JsonbNumberFormat annotation on type.");
    }

    PackageCustomizedTypeOverriddenFieldOverriddenDoubleContainer unmarshalledObject = jsonb
        .fromJson("{ \"instance\" : \"123.456,789\" }",
            PackageCustomizedTypeOverriddenFieldOverriddenDoubleContainer.class);
    if (unmarshalledObject.getInstance() != 123456.789) {
      throw new Fault(
          "Failed to correctly override number format customization using JsonbNumberFormat annotation on package during unmarshalling using JsonbNumberFormat annotation on type.");
    }

    return Status.passed("OK");
  }
}
