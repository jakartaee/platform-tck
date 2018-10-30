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

package com.sun.ts.tests.jsonb.customizedmapping.visibility;

import java.util.Properties;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jsonb.customizedmapping.visibility.model.CustomFieldVisibilityStrategy;
import com.sun.ts.tests.jsonb.customizedmapping.visibility.model.CustomVisibilityAnnotatedContainer;
import com.sun.ts.tests.jsonb.customizedmapping.visibility.model.SimpleContainer;
import com.sun.ts.tests.jsonb.customizedmapping.visibility.model.customized.PackageCustomizedContainer;

/**
 * @test
 * @sources VisibilityCustomizationTest.java
 * @executeClass com.sun.ts.tests.jsonb.customizedmapping.visibility.VisibilityCustomizationTest
 **/
public class VisibilityCustomizationTest extends ServiceEETest {
  private static final long serialVersionUID = 10L;

  private final Jsonb jsonb = JsonbBuilder.create();

  public static void main(String[] args) {
    EETest t = new VisibilityCustomizationTest();
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
   * @testName: testCustomVisibilityConfig
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.6-1
   *
   * @test_Strategy: Assert that only fields allowed by custom
   * PropertyVisibilityStrategy are available for marshalling and unmarshalling
   * if JsonbConfig.withPropertyVisibilityStrategy is used
   */
  public Status testCustomVisibilityConfig() throws Fault {
    Jsonb jsonb = JsonbBuilder.create(new JsonbConfig()
        .withPropertyVisibilityStrategy(new CustomFieldVisibilityStrategy()));

    String jsonString = jsonb.toJson(new SimpleContainer() {
      {
        setStringInstance("Test String");
      }
    });
    if (!jsonString.matches("\\{\\s*\"floatInstance\"\\s*:\\s*0.0\\s*}")) {
      throw new Fault(
          "Failed to hide fields during marshalling by applying custom visibility strategy using configuration.");
    }

    SimpleContainer unmarshalledObject = jsonb.fromJson(
        "{ \"stringInstance\" : \"Test String\", \"floatInstance\" : 1.0, \"integerInstance\" : 1 }",
        SimpleContainer.class);
    if (unmarshalledObject.getStringInstance() != null
        || unmarshalledObject.getIntegerInstance() != 1 
        || unmarshalledObject.getFloatInstance() != 1.0f) {
      throw new Fault(
          "Failed to ignore fields during unmarshalling by applying custom visibility strategy using configuration.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testCustomVisibilityAnnotation
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.6-1
   *
   * @test_Strategy: Assert that only fields allowed by custom
   * PropertyVisibilityStrategy are available for marshalling and unmarshalling
   * if JsonbVisibility annotation is used on a type
   */
  public Status testCustomVisibilityAnnotation() throws Fault {
    String jsonString = jsonb.toJson(new CustomVisibilityAnnotatedContainer() {
      {
        setStringInstance("Test String");
      }
    });
    if (!jsonString.matches("\\{\\s*\"floatInstance\"\\s*:\\s*0.0\\s*}")) {
      throw new Fault(
          "Failed to hide fields during marshalling by applying custom visibility strategy using JsonbVisibility annotation.");
    }

    CustomVisibilityAnnotatedContainer unmarshalledObject = jsonb.fromJson(
        "{ \"stringInstance\" : \"Test String\", \"floatInstance\" : 1.0, \"integerInstance\" : 1 }",
        CustomVisibilityAnnotatedContainer.class);
    if (unmarshalledObject.getStringInstance() != null
        || unmarshalledObject.getIntegerInstance() != null
        || unmarshalledObject.getFloatInstance() != 0.0f) {
      throw new Fault(
          "Failed to ignore fields during unmarshalling by applying custom visibility strategy using JsonbVisibility annotation.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testCustomVisibilityPackage
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.6-1
   *
   * @test_Strategy: Assert that only fields allowed by custom
   * PropertyVisibilityStrategy are available for marshalling and unmarshalling
   * if JsonbVisibility annotation is used on a package
   */
  public Status testCustomVisibilityPackage() throws Fault {
    String jsonString = jsonb.toJson(new PackageCustomizedContainer() {
      {
        setStringInstance("Test String");
      }
    });
    if (!jsonString.matches("\\{\\s*\"floatInstance\"\\s*:\\s*0.0\\s*}")) {
      throw new Fault(
          "Failed to hide fields during marshalling by applying custom visibility strategy using JsonbVisibility annotation on package.");
    }

    PackageCustomizedContainer unmarshalledObject = jsonb.fromJson(
        "{ \"stringInstance\" : \"Test String\", \"floatInstance\" : 1.0, \"integerInstance\" : 1 }",
        PackageCustomizedContainer.class);
    if (unmarshalledObject.getStringInstance() != null
        || unmarshalledObject.getIntegerInstance() != null
        || unmarshalledObject.getFloatInstance() != 0.0f) {
      throw new Fault(
          "Failed to ignore fields during unmarshalling by applying custom visibility strategy using JsonbVisibility annotation on package.");
    }

    return Status.passed("OK");
  }
}
