/*
 * Copyright (c) 2024 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers;

import com.sun.ts.tests.common.base.ServiceEETest;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.Animal;
import com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.AnimalShelterWithInjectedSerializer;
import com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.Cat;
import com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.Dog;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

import java.util.Properties;

/**
 * @test
 * @sources SerializersCustomizationTest.java
 * @executeClass com.sun.ts.tests.jsonb.customizedmapping.serializers.SerializersCustomizationTest
 **/
public class SerializersCustomizationCDITest extends ServiceEETest {

    private final Jsonb jsonb = JsonbBuilder.create();

    public static void main(String[] args) {
        SerializersCustomizationCDITest t = new SerializersCustomizationCDITest();
        Status s = t.run(args, System.out, System.err);
        s.exit();
    }

    public void setup(String[] args, Properties p) throws Exception {
        // logMsg("setup ok");
    }

    public void cleanup() throws Exception {
        // logMsg("cleanup ok");
    }

    /*
     * @testName: testCDISupport
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.7.2-3
     *
     * @test_Strategy: Assert that CDI injection is supported in serializers and deserializers
     */
    public void testCDISupport() throws Exception {
        AnimalShelterWithInjectedSerializer animalShelter = new AnimalShelterWithInjectedSerializer();
        animalShelter.addAnimal(new Cat(5, "Garfield", 10.5f, true, true));
        animalShelter.addAnimal(new Dog(3, "Milo", 5.5f, false, true));
        animalShelter.addAnimal(new Animal(6, "Tweety", 0.5f, false));

        String jsonString = jsonb.toJson(animalShelter);
        if (!jsonString.matches("\\{\\s*\"animals\"\\s*:\\s*\\[\\s*"
                + "\\{\\s*\"type\"\\s*:\\s*\"cat\"\\s*,\\s*\"cuddly\"\\s*:\\s*true\\s*,\\s*\"age\"\\s*:\\s*5\\s*,\\s*\"furry\"\\s*:\\s*true\\s*,\\s*\"name\"\\s*:\\s*\"Garfield\"\\s*,\\s*\"weight\"\\s*:\\s*10.5\\s*}\\s*,\\s*"
                + "\\{\\s*\"type\"\\s*:\\s*\"dog\"\\s*,\\s*\"barking\"\\s*:\\s*true\\s*,\\s*\"age\"\\s*:\\s*3\\s*,\\s*\"furry\"\\s*:\\s*false\\s*,\\s*\"name\"\\s*:\\s*\"Milo\"\\s*,\\s*\"weight\"\\s*:\\s*5.5\\s*}\\s*,\\s*"
                + "\\{\\s*\"type\"\\s*:\\s*\"animal\"\\s*,\\s*\"age\"\\s*:\\s*6\\s*,\\s*\"furry\"\\s*:\\s*false\\s*,\\s*\"name\"\\s*:\\s*\"Tweety\"\\s*,\\s*\"weight\"\\s*:\\s*0.5\\s*}\\s*"
                + "]\\s*}")) {
            throw new Exception(
                    "Failed to correctly marshall complex type hierarchy using a serializer configured using JsonbTypeSerializer annotation and a deserializer with a CDI managed field configured using JsonbTypeDeserializer annotation.");
        }

        AnimalShelterWithInjectedSerializer unmarshalledObject = jsonb.fromJson("{ \"animals\" : [ "
                + "{ \"type\" : \"cat\", \"cuddly\" : true, \"age\" : 5, \"furry\" : true, \"name\" : \"Garfield\" , \"weight\" : 10.5}, "
                + "{ \"type\" : \"dog\", \"barking\" : true, \"age\" : 3, \"furry\" : false, \"name\" : \"Milo\", \"weight\" : 5.5}, "
                + "{ \"type\" : \"animal\", \"age\" : 6, \"furry\" : false, \"name\" : \"Tweety\", \"weight\" : 0.5}" + " ] }",
                AnimalShelterWithInjectedSerializer.class);
        if (!animalShelter.equals(unmarshalledObject)) {
            throw new Exception(
                    "Failed to correctly unmarshall complex type hierarchy using a serializer configured using JsonbTypeSerializer annotation and a deserializer with a CDI managed field configured using JsonbTypeDeserializer annotation.");
        }
    }
}
