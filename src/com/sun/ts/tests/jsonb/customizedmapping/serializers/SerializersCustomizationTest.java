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

package com.sun.ts.tests.jsonb.customizedmapping.serializers;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jsonb.customizedmapping.serializers.model.Animal;
import com.sun.ts.tests.jsonb.customizedmapping.serializers.model.AnimalShelter;
import com.sun.ts.tests.jsonb.customizedmapping.serializers.model.AnimalShelterWithSerializer;
import com.sun.ts.tests.jsonb.customizedmapping.serializers.model.Cat;
import com.sun.ts.tests.jsonb.customizedmapping.serializers.model.Dog;
import com.sun.ts.tests.jsonb.customizedmapping.serializers.model.serializer.AnimalDeserializer;
import com.sun.ts.tests.jsonb.customizedmapping.serializers.model.serializer.AnimalSerializer;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import java.util.Properties;

/**
 * @test
 * @sources SerializersCustomizationTest.java
 * @executeClass com.sun.ts.tests.jsonb.customizedmapping.serializers.SerializersCustomizationTest
 **/
public class SerializersCustomizationTest extends ServiceEETest {
  private static final long serialVersionUID = 10L;

  private final Jsonb jsonb = JsonbBuilder.create();

  public static void main(String[] args) {
    EETest t = new SerializersCustomizationTest();
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
   * @testName: testSerializerConfiguration
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.7.2-1
   *
   * @test_Strategy: Assert that JSONB serializers and deserializers can be
   * configured using JsonbConfig.withSerializers and
   * JsonbConfig.withDeserializers and are working as expected
   */
  public Status testSerializerConfiguration() throws Fault {
    Jsonb jsonb = JsonbBuilder
        .create(new JsonbConfig().withSerializers(new AnimalSerializer())
            .withDeserializers(new AnimalDeserializer()));

    AnimalShelter animalShelter = new AnimalShelter();
    animalShelter.addAnimal(new Cat(5, "Garfield", 10.5f, true, true));
    animalShelter.addAnimal(new Dog(3, "Milo", 5.5f, false, true));
    animalShelter.addAnimal(new Animal(6, "Tweety", 0.5f, false));

    String jsonString = jsonb.toJson(animalShelter);
    if (!jsonString.matches("\\{\\s*\"animals\"\\s*:\\s*\\[\\s*"
        + "\\{\\s*\"type\"\\s*:\\s*\"cat\"\\s*,\\s*\"cuddly\"\\s*:\\s*true\\s*,\\s*\"age\"\\s*:\\s*5\\s*,\\s*\"furry\"\\s*:\\s*true\\s*,\\s*\"name\"\\s*:\\s*\"Garfield\"\\s*,\\s*\"weight\"\\s*:\\s*10.5\\s*}\\s*,\\s*"
        + "\\{\\s*\"type\"\\s*:\\s*\"dog\"\\s*,\\s*\"barking\"\\s*:\\s*true\\s*,\\s*\"age\"\\s*:\\s*3\\s*,\\s*\"furry\"\\s*:\\s*false\\s*,\\s*\"name\"\\s*:\\s*\"Milo\"\\s*,\\s*\"weight\"\\s*:\\s*5.5\\s*}\\s*,\\s*"
        + "\\{\\s*\"type\"\\s*:\\s*\"animal\"\\s*,\\s*\"age\"\\s*:\\s*6\\s*,\\s*\"furry\"\\s*:\\s*false\\s*,\\s*\"name\"\\s*:\\s*\"Tweety\"\\s*,\\s*\"weight\"\\s*:\\s*0.5\\s*}\\s*"
        + "]\\s*}")) {
      throw new Fault(
          "Failed to correctly marshall complex type hierarchy using a serializer configured using JsonbConfig.withSerializers and a deserializer configured using JsonbConfig.withDeserializers.");
    }

    AnimalShelter unmarshalledObject = jsonb.fromJson("{ \"animals\" : [ "
        + "{ \"type\" : \"cat\", \"cuddly\" : true, \"age\" : 5, \"furry\" : true, \"name\" : \"Garfield\" , \"weight\" : 10.5}, "
        + "{ \"type\" : \"dog\", \"barking\" : true, \"age\" : 3, \"furry\" : false, \"name\" : \"Milo\", \"weight\" : 5.5}, "
        + "{ \"type\" : \"animal\", \"age\" : 6, \"furry\" : false, \"name\" : \"Tweety\", \"weight\" : 0.5}"
        + " ] }", AnimalShelter.class);
    if (!animalShelter.equals(unmarshalledObject)) {
      throw new Fault(
          "Failed to correctly unmarshall complex type hierarchy using a serializer configured using JsonbConfig.withSerializers and a deserializer configured using JsonbConfig.withDeserializers.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testSerializerAnnotation
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.7.2-2
   *
   * @test_Strategy: Assert that JSONB serializers and deserializers can be
   * configured using JsonbTypeSerializer and JsonbTypeDeserializer annotation
   * and are working as expected
   */
  public Status testSerializerAnnotation() throws Fault {
    AnimalShelterWithSerializer animalShelter = new AnimalShelterWithSerializer();
    animalShelter.addAnimal(new Cat(5, "Garfield", 10.5f, true, true));
    animalShelter.addAnimal(new Dog(3, "Milo", 5.5f, false, true));
    animalShelter.addAnimal(new Animal(6, "Tweety", 0.5f, false));

    String jsonString = jsonb.toJson(animalShelter);
    if (!jsonString.matches("\\{\\s*\"animals\"\\s*:\\s*\\[\\s*"
        + "\\{\\s*\"type\"\\s*:\\s*\"cat\"\\s*,\\s*\"cuddly\"\\s*:\\s*true\\s*,\\s*\"age\"\\s*:\\s*5\\s*,\\s*\"furry\"\\s*:\\s*true\\s*,\\s*\"name\"\\s*:\\s*\"Garfield\"\\s*,\\s*\"weight\"\\s*:\\s*10.5\\s*}\\s*,\\s*"
        + "\\{\\s*\"type\"\\s*:\\s*\"dog\"\\s*,\\s*\"barking\"\\s*:\\s*true\\s*,\\s*\"age\"\\s*:\\s*3\\s*,\\s*\"furry\"\\s*:\\s*false\\s*,\\s*\"name\"\\s*:\\s*\"Milo\"\\s*,\\s*\"weight\"\\s*:\\s*5.5\\s*}\\s*,\\s*"
        + "\\{\\s*\"type\"\\s*:\\s*\"animal\"\\s*,\\s*\"age\"\\s*:\\s*6\\s*,\\s*\"furry\"\\s*:\\s*false\\s*,\\s*\"name\"\\s*:\\s*\"Tweety\"\\s*,\\s*\"weight\"\\s*:\\s*0.5\\s*}\\s*"
        + "]\\s*}")) {
      throw new Fault(
          "Failed to correctly marshall complex type hierarchy using a serializer configured using JsonbTypeSerializer annotation and a deserializer configured using JsonbTypeDeserializer annotation.");
    }

    AnimalShelterWithSerializer unmarshalledObject = jsonb
        .fromJson("{ \"animals\" : [ "
            + "{ \"type\" : \"cat\", \"cuddly\" : true, \"age\" : 5, \"furry\" : true, \"name\" : \"Garfield\" , \"weight\" : 10.5}, "
            + "{ \"type\" : \"dog\", \"barking\" : true, \"age\" : 3, \"furry\" : false, \"name\" : \"Milo\", \"weight\" : 5.5}, "
            + "{ \"type\" : \"animal\", \"age\" : 6, \"furry\" : false, \"name\" : \"Tweety\", \"weight\" : 0.5}"
            + " ] }", AnimalShelterWithSerializer.class);
    if (!animalShelter.equals(unmarshalledObject)) {
      throw new Fault(
          "Failed to correctly unmarshall complex type hierarchy using a serializer configured using JsonbTypeSerializer annotation and a deserializer configured using JsonbTypeDeserializer annotation.");
    }

    return Status.passed("OK");
  }
}
