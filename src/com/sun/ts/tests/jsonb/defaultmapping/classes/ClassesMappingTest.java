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

package com.sun.ts.tests.jsonb.defaultmapping.classes;

import java.lang.reflect.Field;
import java.util.Properties;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbException;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jsonb.defaultmapping.classes.model.StringContainerAccessorsWithoutMatchingField;
import com.sun.ts.tests.jsonb.defaultmapping.classes.model.StringContainerFinalField;
import com.sun.ts.tests.jsonb.defaultmapping.classes.model.StringContainerFinalPublicField;
import com.sun.ts.tests.jsonb.defaultmapping.classes.model.StringContainerNoAccessorsPackagePrivateField;
import com.sun.ts.tests.jsonb.defaultmapping.classes.model.StringContainerNoAccessorsPrivateField;
import com.sun.ts.tests.jsonb.defaultmapping.classes.model.StringContainerNoAccessorsProtectedField;
import com.sun.ts.tests.jsonb.defaultmapping.classes.model.StringContainerNoAccessorsPublicField;
import com.sun.ts.tests.jsonb.defaultmapping.classes.model.StringContainerPackagePrivateAccessors;
import com.sun.ts.tests.jsonb.defaultmapping.classes.model.StringContainerPackagePrivateConstructor;
import com.sun.ts.tests.jsonb.defaultmapping.classes.model.StringContainerPrivateAccessors;
import com.sun.ts.tests.jsonb.defaultmapping.classes.model.StringContainerPrivateAccessorsPublicField;
import com.sun.ts.tests.jsonb.defaultmapping.classes.model.StringContainerPrivateConstructor;
import com.sun.ts.tests.jsonb.defaultmapping.classes.model.StringContainerProtectedAccessors;
import com.sun.ts.tests.jsonb.defaultmapping.classes.model.StringContainerProtectedConstructor;
import com.sun.ts.tests.jsonb.defaultmapping.classes.model.StringContainerProtectedStaticNestedClass;
import com.sun.ts.tests.jsonb.defaultmapping.classes.model.StringContainerPublicAccessors;
import com.sun.ts.tests.jsonb.defaultmapping.classes.model.StringContainerPublicAccessorsPublicField;
import com.sun.ts.tests.jsonb.defaultmapping.classes.model.StringContainerPublicConstructor;
import com.sun.ts.tests.jsonb.defaultmapping.classes.model.StringContainerPublicStaticNestedClass;
import com.sun.ts.tests.jsonb.defaultmapping.classes.model.StringContainerStaticField;
import com.sun.ts.tests.jsonb.defaultmapping.classes.model.StringContainerTransientField;

/**
 * @test
 * @sources ClassesMappingTest.java
 * @executeClass com.sun.ts.tests.jsonb.defaultmapping.classes.ClassesMappingTest
 **/
public class ClassesMappingTest extends ServiceEETest {
  private static final long serialVersionUID = 10L;

  private final Jsonb jsonb = JsonbBuilder.create();

  public static void main(String[] args) {
    EETest t = new ClassesMappingTest();
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
   * @testName: testPublicConstructorAccess
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.7-1; JSONB:SPEC:JSB-3.7-2
   *
   * @test_Strategy: Assert that a class with a public constructor can be
   * marshalled and unmarshalled
   */
  public Status testPublicConstructorAccess() throws Fault {
    try {
      String jsonString = jsonb.toJson(new StringContainerPublicConstructor());
      if (!jsonString
          .matches("\\{\\s*\"instance\"\\s*:\\s*\"Test String\"\\s*\\}")) {
        throw new Fault(
            "Failed to get attribute value when marshalling a class with a public constructor.");
      }
    } catch (JsonbException x) {
      throw new Fault(
          "An exception is not expected when marshalling a class with a public constructor.");
    }

    try {
      jsonb.fromJson("{ \"instance\" : \"Test String\" }",
          StringContainerPublicConstructor.class);
      return Status.passed("OK");
    } catch (JsonbException x) {
      throw new Fault(
          "An exception is not expected when unmarshalling a class with a public constructor.");
    }
  }

  /*
   * @testName: testProtectedConstructorAccess
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.7-1; JSONB:SPEC:JSB-3.7-2
   *
   * @test_Strategy: Assert that a class with a protected constructor can be
   * marshalled and unmarshalled
   */
  public Status testProtectedConstructorAccess() throws Fault {
    try {
      String jsonString = jsonb
          .toJson(StringContainerProtectedConstructor.getClassInstance());
      if (!jsonString
          .matches("\\{\\s*\"instance\"\\s*:\\s*\"Test String\"\\s*\\}")) {
        throw new Fault(
            "Failed to get attribute value when marshalling a class with a protected constructor.");
      }
    } catch (JsonbException x) {
      throw new Fault(
          "An exception is not expected when marshalling a class with a protected constructor.");
    }

    try {
      jsonb.fromJson("{ \"instance\" : \"Test String\" }",
          StringContainerProtectedConstructor.class);
      return Status.passed("OK");
    } catch (JsonbException x) {
            throw new Fault("An exception is not expected when unmarshalling a class with a protected constructor.");
   }
  }

  /*
   * @testName: testPrivateConstructorAccess
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.7-1; JSONB:SPEC:JSB-3.7-2
   *
   * @test_Strategy: Assert that a class with a private constructor can be
   * marshalled but not unmarshalled
   */
  public Status testPrivateConstructorAccess() throws Fault {
    try {
      String jsonString = jsonb
          .toJson(StringContainerPrivateConstructor.getClassInstance());
      if (!jsonString
          .matches("\\{\\s*\"instance\"\\s*:\\s*\"Test String\"\\s*\\}")) {
        throw new Fault(
            "Failed to get attribute value when marshalling a class with a private constructor.");
      }
    } catch (JsonbException x) {
      throw new Fault(
          "An exception is not expected when marshalling a class with a private constructor.");
    }

    try {
      jsonb.fromJson("{ \"instance\" : \"Test String\" }",
          StringContainerPrivateConstructor.class);
      throw new Fault(
          "An exception is expected when unmarshalling a class with a private constructor.");
    } catch (JsonbException x) {
      return Status.passed("OK");
    }
  }

  /*
   * @testName: testPackagePrivateConstructorAccess
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.7-1; JSONB:SPEC:JSB-3.7-2
   *
   * @test_Strategy: Assert that a class with a package private constructor can
   * be marshalled but not unmarshalled
   */
  public Status testPackagePrivateConstructorAccess() throws Fault {
    try {
      String jsonString = jsonb
          .toJson(StringContainerPackagePrivateConstructor.getClassInstance());
      if (!jsonString
          .matches("\\{\\s*\"instance\"\\s*:\\s*\"Test String\"\\s*\\}")) {
        throw new Fault(
            "Failed to get attribute value when marshalling a class with a package private constructor.");
      }
    } catch (JsonbException x) {
      throw new Fault(
          "An exception is not expected when marshalling a class with a package private constructor.");
    }

    try {
      jsonb.fromJson("{ \"instance\" : \"Test String\" }",
          StringContainerPackagePrivateConstructor.class);
      throw new Fault(
          "An exception is expected when unmarshalling a class with a package private constructor.");
    } catch (JsonbException x) {
      return Status.passed("OK");
    }
  }

  /*
   * @testName: testPublicAccessors
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.7.1-1; JSONB:SPEC:JSB-3.7.1-2
   *
   * @test_Strategy: Assert that private field with public accessors can be
   * managed and unmanaged
   */
  public Status testPublicAccessors() throws Fault {
    String jsonString = jsonb.toJson(new StringContainerPublicAccessors());
    if (!jsonString
        .matches("\\{\\s*\"instance\"\\s*:\\s*\"Test String\"\\s*\\}")) {
      throw new Fault("Failed to get attribute value using public getter.");
    }

    try {
      StringContainerPublicAccessors unmarshalledObject = jsonb.fromJson(
          "{ \"instance\" : \"New Test String\" }",
          StringContainerPublicAccessors.class);
      Field instanceField = StringContainerPublicAccessors.class
          .getDeclaredField("instance");
      instanceField.setAccessible(true);
      try {
        if (!instanceField.get(unmarshalledObject).equals("New Test String")) {
          throw new Fault("Failed to set attribute value using public setter.");
        }
      } finally {
        instanceField.setAccessible(false);
      }
    } catch (Exception x) {
      throw new Fault(x.getMessage(), x);
    }
    return Status.passed("OK");
  }

  /*
   * @testName: testProtectedAccessors
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.7.1-1; JSONB:SPEC:JSB-3.7.1-2
   *
   * @test_Strategy: Assert that private field with protected accessors is
   * ignored
   */
  public Status testProtectedAccessors() throws Fault {
    String jsonString = jsonb.toJson(new StringContainerProtectedAccessors());
    if (!jsonString.matches("\\{\\s*\\}")) {
      throw new Fault(
          "Failed to ignore attribute value using protected getter.");
    }

    try {
      StringContainerProtectedAccessors unmarshalledObject = jsonb.fromJson(
          "{ \"instance\" : \"New Test String\" }",
          StringContainerProtectedAccessors.class);
      Field instanceField = StringContainerProtectedAccessors.class
          .getDeclaredField("instance");
      instanceField.setAccessible(true);
      try {
        if (!instanceField.get(unmarshalledObject).equals("Test String")) {
          throw new Fault(
              "Failed to ignore setting attribute value using protected setter.");
        }
      } finally {
        instanceField.setAccessible(false);
      }
    } catch (Exception x) {
      throw new Fault(x.getMessage(), x);
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testPrivateAccessors
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.7.1-1; JSONB:SPEC:JSB-3.7.1-2
   *
   * @test_Strategy: Assert that private field with private accessors is ignored
   */
  public Status testPrivateAccessors() throws Fault {
    String jsonString = jsonb.toJson(new StringContainerPrivateAccessors());
    if (!jsonString.matches("\\{\\s*\\}")) {
      throw new Fault("Failed to ignore private value using private getter.");
    }

    try {
      StringContainerPrivateAccessors unmarshalledObject = jsonb.fromJson(
          "{ \"instance\" : \"New Test String\" }",
          StringContainerPrivateAccessors.class);
      Field instanceField = StringContainerPrivateAccessors.class
          .getDeclaredField("instance");
      instanceField.setAccessible(true);
      try {
        if (!instanceField.get(unmarshalledObject).equals("Test String")) {
          throw new Fault(
              "Failed to ignore setting attribute value using private setter.");
        }
      } finally {
        instanceField.setAccessible(false);
      }
    } catch (Exception x) {
      throw new Fault(x.getMessage(), x);
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testPackagePrivateAccessors
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.7.1-1; JSONB:SPEC:JSB-3.7.1-2
   *
   * @test_Strategy: Assert that private field with package private accessors is
   * ignored
   */
  public Status testPackagePrivateAccessors() throws Fault {
    String jsonString = jsonb
        .toJson(new StringContainerPackagePrivateAccessors());
    if (!jsonString.matches("\\{\\s*\\}")) {
      throw new Fault(
          "Failed to ignore private value using package private getter.");
    }

    try {
      StringContainerPackagePrivateAccessors unmarshalledObject = jsonb
          .fromJson("{ \"instance\" : \"New Test String\" }",
              StringContainerPackagePrivateAccessors.class);
      Field instanceField = StringContainerPackagePrivateAccessors.class
          .getDeclaredField("instance");
      instanceField.setAccessible(true);
      try {
        if (!instanceField.get(unmarshalledObject).equals("Test String")) {
          throw new Fault(
              "Failed to ignore setting attribute value using package private setter.");
        }
      } finally {
        instanceField.setAccessible(false);
      }
    } catch (Exception x) {
      throw new Fault(x.getMessage(), x);
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testPublicAccessorsPublicField
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.7.1-1; JSONB:SPEC:JSB-3.7.1-2
   *
   * @test_Strategy: Assert that public field with public accessors is
   * marshalled and unmarshalled using the accessor
   */
  public Status testPublicAccessorsPublicField() throws Fault {
    String jsonString = jsonb
        .toJson(new StringContainerPublicAccessorsPublicField());
    if (!jsonString
        .matches("\\{\\s*\"instance\"\\s*:\\s*\"Getter String\"\\s*\\}")) {
      throw new Fault("Failed to get attribute value using public getter.");
    }

    StringContainerPublicAccessorsPublicField unmarshalledObject = jsonb
        .fromJson("{ \"instance\" : \"New Test String\" }",
            StringContainerPublicAccessorsPublicField.class);
    if (!unmarshalledObject.instance.equals("Setter String")) {
      throw new Fault("Failed to set attribute value using public setter.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testPrivateAccessorsPublicField
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.7.1-1; JSONB:SPEC:JSB-3.7.1-2
   *
   * @test_Strategy: Assert that public field with private accessors is
   * marshalled and unmarshalled using direct field access
   */
  public Status testPrivateAccessorsPublicField() throws Fault {
    String jsonString = jsonb
        .toJson(new StringContainerPrivateAccessorsPublicField());
    if (!jsonString.matches("\\{\\s*\\}")) {
      throw new Fault("Failed to ignore public value using private getter.");
    }

    try {
      StringContainerPrivateAccessorsPublicField unmarshalledObject = jsonb
          .fromJson("{ \"instance\" : \"New Test String\" }",
              StringContainerPrivateAccessorsPublicField.class);
      Field instanceField = StringContainerPrivateAccessorsPublicField.class
          .getDeclaredField("instance");
      instanceField.setAccessible(true);
      try {
        if (!instanceField.get(unmarshalledObject).equals("Test String")) {
          throw new Fault(
              "Failed to ignore setting public value using private setter.");
        }
      } finally {
        instanceField.setAccessible(false);
      }
    } catch (Exception x) {
      throw new Fault(x.getMessage(), x);
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testNoAccessorsPublicField
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.7.1-1; JSONB:SPEC:JSB-3.7.1-2
   *
   * @test_Strategy: Assert that public field with no accessors is marshalled
   * and unmarshalled using direct field access
   */
  public Status testNoAccessorsPublicField() throws Fault {
    String jsonString = jsonb
        .toJson(new StringContainerNoAccessorsPublicField());
    if (!jsonString
        .matches("\\{\\s*\"instance\"\\s*:\\s*\"Test String\"\\s*\\}")) {
      throw new Fault("Failed to get public field value.");
    }

    StringContainerNoAccessorsPublicField unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : \"New Test String\" }",
        StringContainerNoAccessorsPublicField.class);
    if (!unmarshalledObject.instance.equals("New Test String")) {
      throw new Fault("Failed to set public field value.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testNoAccessorsProtectedField
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.7.1-1; JSONB:SPEC:JSB-3.7.1-2
   *
   * @test_Strategy: Assert that protected field with no accessors is ignored
   */
  public Status testNoAccessorsProtectedField() throws Fault {
    String jsonString = jsonb
        .toJson(new StringContainerNoAccessorsProtectedField());
    if (!jsonString.matches("\\{\\s*\\}")) {
      throw new Fault("Failed to ignore getting protected field value.");
    }

    try {
      StringContainerNoAccessorsProtectedField unmarshalledObject = jsonb
          .fromJson("{ \"instance\" : \"New Test String\" }",
              StringContainerNoAccessorsProtectedField.class);
      Field instanceField = StringContainerNoAccessorsProtectedField.class
          .getDeclaredField("instance");
      instanceField.setAccessible(true);
      try {
        if (!instanceField.get(unmarshalledObject).equals("Test String")) {
          throw new Fault("Failed to ignore setting protected field value.");
        }
      } finally {
        instanceField.setAccessible(false);
      }
    } catch (Exception x) {
      throw new Fault(x.getMessage(), x);
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testNoAccessorsPrivateField
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.7.1-1; JSONB:SPEC:JSB-3.7.1-2
   *
   * @test_Strategy: Assert that private field with no accessors is ignored
   */
  public Status testNoAccessorsPrivateField() throws Fault {
    String jsonString = jsonb
        .toJson(new StringContainerNoAccessorsPrivateField());
    if (!jsonString.matches("\\{\\s*\\}")) {
      throw new Fault("Failed to ignore getting private field value.");
    }

    try {
      StringContainerNoAccessorsPrivateField unmarshalledObject = jsonb
          .fromJson("{ \"instance\" : \"New Test String\" }",
              StringContainerNoAccessorsPrivateField.class);
      Field instanceField = StringContainerNoAccessorsPrivateField.class
          .getDeclaredField("instance");
      instanceField.setAccessible(true);
      try {
        if (!instanceField.get(unmarshalledObject).equals("Test String")) {
          throw new Fault("Failed to ignore setting private field value.");
        }
      } finally {
        instanceField.setAccessible(false);
      }
    } catch (Exception x) {
      throw new Fault(x.getMessage(), x);
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testNoAccessorsPackagePrivateField
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.7.1-1; JSONB:SPEC:JSB-3.7.1-2
   *
   * @test_Strategy: Assert that package private field with no accessors is
   * ignored
   */
  public Status testNoAccessorsPackagePrivateField() throws Fault {
    String jsonString = jsonb
        .toJson(new StringContainerNoAccessorsPackagePrivateField());
    if (!jsonString.matches("\\{\\s*\\}")) {
      throw new Fault("Failed to ignore getting package private field value.");
    }

    try {
      StringContainerNoAccessorsPackagePrivateField unmarshalledObject = jsonb
          .fromJson("{ \"instance\" : \"New Test String\" }",
              StringContainerNoAccessorsPackagePrivateField.class);
      Field instanceField = StringContainerNoAccessorsPackagePrivateField.class
          .getDeclaredField("instance");
      instanceField.setAccessible(true);
      try {
        if (!instanceField.get(unmarshalledObject).equals("Test String")) {
          throw new Fault(
              "Failed to ignore setting package private field value.");
        }
      } finally {
        instanceField.setAccessible(false);
      }
    } catch (Exception x) {
      throw new Fault(x.getMessage(), x);
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testTransientField
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.7.1-3
   *
   * @test_Strategy: Assert that transient fields are ignored during marshalling
   * and unmarshalling
   */
  public Status testTransientField() throws Fault {
    String jsonString = jsonb.toJson(new StringContainerTransientField());
    if (!jsonString.matches("\\{\\s*\\}")) {
      throw new Fault("Failed to ignore getting transient field value.");
    }

    try {
      StringContainerTransientField unmarshalledObject = jsonb.fromJson(
          "{ \"instance\" : \"New Test String\" }",
          StringContainerTransientField.class);
      Field instanceField = StringContainerTransientField.class
          .getDeclaredField("instance");
      instanceField.setAccessible(true);
      try {
        if (!instanceField.get(unmarshalledObject).equals("Test String")) {
          throw new Fault("Failed to ignore setting transient field value.");
        }
      } finally {
        instanceField.setAccessible(false);
      }
    } catch (Exception x) {
      throw new Fault(x.getMessage(), x);
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testStaticField
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.7.1-5
   *
   * @test_Strategy: Assert that static fields are ignored during marshalling
   * and unmarshalling
   */
  public Status testStaticField() throws Fault {
    String jsonString = jsonb.toJson(new StringContainerStaticField());
    if (!jsonString.matches("\\{\\s*\\}")) {
      throw new Fault("Failed to ignore getting static field value.");
    }

    try {
      StringContainerStaticField unmarshalledObject = jsonb.fromJson(
          "{ \"instance\" : \"New Test String\" }",
          StringContainerStaticField.class);
      Field instanceField = StringContainerStaticField.class
          .getDeclaredField("instance");
      instanceField.setAccessible(true);
      try {
        if (!instanceField.get(unmarshalledObject).equals("Test String")) {
          throw new Fault("Failed to ignore setting static field value.");
        }
      } finally {
        instanceField.setAccessible(false);
      }
    } catch (Exception x) {
      throw new Fault(x.getMessage(), x);
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testFinalField
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.7.1-4
   *
   * @test_Strategy: Assert that final fields are correctly marshalled but not
   * unmarshalled
   */
  public Status testFinalField() throws Fault {
    String jsonString = jsonb.toJson(new StringContainerFinalField());
    if (!jsonString
        .matches("\\{\\s*\"instance\"\\s*:\\s*\"Test String\"\\s*\\}")) {
      throw new Fault("Failed to get final field value.");
    }

    try {
      StringContainerFinalField unmarshalledObject = jsonb.fromJson(
          "{ \"instance\" : \"New Test String\" }",
          StringContainerFinalField.class);
      Field instanceField = StringContainerFinalField.class
          .getDeclaredField("instance");
      instanceField.setAccessible(true);
      try {
        if (!instanceField.get(unmarshalledObject).equals("Test String")) {
          throw new Fault("Failed to ignore setting final field value.");
        }
      } finally {
        instanceField.setAccessible(false);
      }
    } catch (Exception x) {
      throw new Fault(x.getMessage(), x);
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testFinalPublicField
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.7.1-4
   *
   * @test_Strategy: Assert that final public fields are correctly marshalled
   * but not unmarshalled
   */
  public Status testFinalPublicField() throws Fault {
    String jsonString = jsonb.toJson(new StringContainerFinalPublicField());
    if (!jsonString
        .matches("\\{\\s*\"instance\"\\s*:\\s*\"Test String\"\\s*\\}")) {
      throw new Fault("Failed to get final public field value.");
    }

    try {
      StringContainerFinalPublicField unmarshalledObject = jsonb.fromJson(
          "{ \"instance\" : \"New Test String\" }",
          StringContainerFinalPublicField.class);
      Field instanceField = StringContainerFinalPublicField.class
          .getDeclaredField("instance");
      instanceField.setAccessible(true);
      try {
        if (!instanceField.get(unmarshalledObject).equals("Test String")) {
          throw new Fault("Failed to ignore setting final public field value.");
        }
      } finally {
        instanceField.setAccessible(false);
      }
    } catch (Exception x) {
      throw new Fault(x.getMessage(), x);
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testAccessorsWithoutCorrespondingField
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.7.1-7
   *
   * @test_Strategy: Assert that public accessor methods without a corresponding
   * field are supported
   */
  public Status testAccessorsWithoutCorrespondingField() throws Fault {
    String jsonString = jsonb
        .toJson(new StringContainerAccessorsWithoutMatchingField());
    if (!jsonString
        .matches("\\{\\s*\"instance\"\\s*:\\s*\"Test String\"\\s*\\}")) {
      throw new Fault(
          "Failed to get value from getter without corresponding field.");
    }

    try {
      StringContainerAccessorsWithoutMatchingField unmarshalledObject = jsonb
          .fromJson("{ \"instance\" : \"New Test String\" }",
              StringContainerAccessorsWithoutMatchingField.class);
      Field instanceField = StringContainerAccessorsWithoutMatchingField.class
          .getDeclaredField("field");
      instanceField.setAccessible(true);
      try {
        if (!instanceField.get(unmarshalledObject).equals("New Test String")) {
          throw new Fault(
              "Failed to set value using setter without corresponding field.");
        }
      } finally {
        instanceField.setAccessible(false);
      }
    } catch (Exception x) {
      throw new Fault(x.getMessage(), x);
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testDeserialisationOfNonExistentField
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.7.1-6
   *
   * @test_Strategy: Assert that an attribute without a corresponding field or
   * accessor is ignored during unmarshalling
   */
  public Status testDeserialisationOfNonExistentField() throws Fault {
    try {
      StringContainerPublicAccessors unmarshalledObject = jsonb.fromJson(
          "{ \"field\" : \"New Test String\" }",
          StringContainerPublicAccessors.class);
      Field instanceField = StringContainerPublicAccessors.class
          .getDeclaredField("instance");
      instanceField.setAccessible(true);
      try {
        if (!instanceField.get(unmarshalledObject).equals("Test String")) {
          throw new Fault(
              "Failed to ignore setting value to non existent field.");
        }
      } finally {
        instanceField.setAccessible(false);
      }
    } catch (Exception x) {
      throw new Fault(x.getMessage(), x);
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testPublicStaticNestedClass
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.7.3-1; JSONB:SPEC:JSB-3.7.3-2
   *
   * @test_Strategy: Assert that public static nested class is correctly handled
   */
  public Status testPublicStaticNestedClass() throws Fault {
    String jsonString = jsonb
        .toJson(new StringContainerPublicStaticNestedClass());
    if (!jsonString.matches(
        "\\{\\s*\"nestedClass\"\\s*:\\s*\\{\\s*\"instance\"\\s*:\\s*\"Test String\"\\s*\\}\\s*\\}")) {
      throw new Fault(
          "Failed to get attribute value from public nested class.");
    }

    try {
      StringContainerPublicStaticNestedClass unmarshalledObject = jsonb
          .fromJson(
              "{ \"nestedClass\" : { \"instance\" : \"New Test String\" } }",
              StringContainerPublicStaticNestedClass.class);
      Field instanceField = StringContainerPublicStaticNestedClass.NestedClass.class
          .getDeclaredField("instance");
      instanceField.setAccessible(true);
      try {
        if (!instanceField.get(unmarshalledObject.nestedClass)
            .equals("New Test String")) {
          throw new Fault(
              "Failed to set attribute value to public nested class.");
        }
      } finally {
        instanceField.setAccessible(false);
      }
    } catch (Exception x) {
      throw new Fault(x.getMessage(), x);
    }
    return Status.passed("OK");
  }

  /*
   * @testName: testProtectedStaticNestedClass
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.7.3-1; JSONB:SPEC:JSB-3.7.3-2
   *
   * @test_Strategy: Assert that protected static nested class is correctly
   * handled
   */
  public Status testProtectedStaticNestedClass() throws Fault {
    String jsonString = jsonb
        .toJson(new StringContainerProtectedStaticNestedClass());
    if (!jsonString.matches(
        "\\{\\s*\"nestedClass\"\\s*:\\s*\\{\\s*\"instance\"\\s*:\\s*\"Test String\"\\s*\\}\\s*\\}")) {
      throw new Fault(
          "Failed to get attribute value from protected nested class.");
    }

    try {
      StringContainerProtectedStaticNestedClass unmarshalledObject = jsonb
          .fromJson(
              "{ \"nestedClass\" : { \"instance\" : \"New Test String\" } }",
              StringContainerProtectedStaticNestedClass.class);
      Class<?> nestedClass = StringContainerProtectedStaticNestedClass.class
          .getDeclaredClasses()[0];
      Field instanceField = nestedClass.getDeclaredField("instance");
      instanceField.setAccessible(true);
      try {
        if (!instanceField.get(unmarshalledObject.nestedClass)
            .equals("New Test String")) {
          throw new Fault(
              "Failed to set attribute value to protected nested class.");
        }
      } finally {
        instanceField.setAccessible(false);
      }
    } catch (Exception x) {
      throw new Fault(x.getMessage(), x);
    }
    return Status.passed("OK");
  }

  /*
   * @testName: testAnonymousClass
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.7.4-1
   *
   * @test_Strategy: Assert that marshalling of an anonymous class is supported
   * using default object mapping. Unmarshalling is not supported by the spec so
   * not tested.
   */
  public Status testAnonymousClass() throws Fault {
    Object anonymousInstance = new Object() {
      public String newInstance = "Anonymous";
    };
    String jsonString = jsonb.toJson(anonymousInstance);
    if (!jsonString
        .matches("\\{\\s*\"newInstance\"\\s*:\\s*\"Anonymous\"\\s*\\}")) {
      throw new Fault("Failed to get attribute value from anonymous class.");
    }

    return Status.passed("OK");
  }
}
