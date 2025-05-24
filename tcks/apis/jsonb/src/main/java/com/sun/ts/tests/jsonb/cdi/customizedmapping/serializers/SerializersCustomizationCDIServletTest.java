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

import com.sun.ts.lib.harness.Fault;
import com.sun.ts.lib.harness.SetupException;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.tests.common.base.ServiceEETest;
import com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.Animal;
import com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.AnimalShelterWithInjectedSerializer;
import com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.Cat;
import com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.Dog;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

import java.lang.System.Logger;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;

import tck.arquillian.protocol.common.TargetVehicle;

/**
 * @test
 * @sources SerializersCustomizationTest.java
 * @executeClass com.sun.ts.tests.jsonb.customizedmapping.serializers.SerializersCustomizationTest
 **/
@Tag("tck-javatest")
@Tag("jsonb")
@Tag("platform")
@Tag("web")
@ExtendWith(ArquillianExtension.class)
public class SerializersCustomizationCDIServletTest extends SerializersCustomizationCDITest {

    private static final long serialVersionUID = 10L;

    static final String VEHICLE_ARCHIVE = "jsonb_cdi_customizedmapping_serializers_servlet_vehicle";

    private final Jsonb jsonb = JsonbBuilder.create();

    public static void main(String[] args) {
        SerializersCustomizationCDIServletTest t = new SerializersCustomizationCDIServletTest();
        Status s = t.run(args, System.out, System.err);
        s.exit();
    }

    private static final Logger logger = System.getLogger(SerializersCustomizationCDIServletTest.class.getName());

    private static String packagePath = SerializersCustomizationCDIServletTest.class.getPackageName().replace(".", "/");

    @BeforeEach
    void logStartTest(TestInfo testInfo) {
        logger.log(Logger.Level.INFO, "STARTING TEST : " + testInfo.getDisplayName());
    }

    @AfterEach
    void logFinishTest(TestInfo testInfo) {
        logger.log(Logger.Level.INFO, "FINISHED TEST : " + testInfo.getDisplayName());
    }

    @TargetsContainer("tck-javatest")
    @OverProtocol("javatest")
    @Deployment(name = VEHICLE_ARCHIVE, testable = true)
    public static WebArchive createServletDeployment() throws Exception {

        WebArchive war = ShrinkWrap.create(WebArchive.class, "jsonb_cdi_customizedmapping_serializers_servlet_vehicle_web.war");
        war.addClasses(SerializersCustomizationCDIServletTest.class, com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class, com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class, EETest.class,
                SetupException.class, Fault.class,
                ServiceEETest.class,
                com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalBuilder.class,
                com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalBuilder.TYPE.class,
                com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalDeserializer.class,
                com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalDeserializerInjected.class,
                com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalListDeserializerInjected.class,
                com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalListSerializer.class,
                com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalSerializer.class,
                com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.Animal.class,
                com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.AnimalShelterWithInjectedSerializer.class,
                com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.Cat.class,
                com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.Dog.class, SerializersCustomizationCDITest.class);

        war.setWebXML(SerializersCustomizationCDIServletTest.class.getClassLoader().getResource(packagePath + "/servlet_vehicle_web.xml"));
        URL warResURL = SerializersCustomizationCDIServletTest.class.getClassLoader().getResource(packagePath + "/beans.xml");
        if (warResURL != null) {
            war.addAsWebResource(warResURL, "/WEB-INF/beans.xml");
        }

        return war;
    }

    /*
     * @testName: testCDISupport
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.7.2-3
     *
     * @test_Strategy: Assert that CDI injection is supported in serializers and deserializers
     */
    @Override
    @Test
    @TargetVehicle("servlet")
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
