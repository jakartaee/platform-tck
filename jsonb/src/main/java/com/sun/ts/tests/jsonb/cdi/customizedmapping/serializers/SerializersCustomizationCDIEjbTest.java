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

import java.util.Properties;
import java.util.stream.Collectors;
import java.io.InputStream;
import java.io.IOException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.net.URL;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.AnimalShelterWithInjectedSerializer;
import com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.Animal;
import com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.Cat;
import com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.Dog;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.asset.UrlAsset;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import tck.arquillian.protocol.common.TargetVehicle;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;

import java.lang.System.Logger;

/**
 * @test
 * @sources SerializersCustomizationTest.java
 * @executeClass com.sun.ts.tests.jsonb.customizedmapping.serializers.SerializersCustomizationTest
 **/
@Tag("tck-appclient")
@Tag("jsonb")
@Tag("platform")
@ExtendWith(ArquillianExtension.class)
public class SerializersCustomizationCDIEjbTest extends ServiceEETest {

  private static final long serialVersionUID = 10L;

  static final String VEHICLE_ARCHIVE = "jsonb_cdi_customizedmapping_serializers_servlet_vehicle";

  private final Jsonb jsonb = JsonbBuilder.create();

  public static void main(String[] args) {
    SerializersCustomizationCDIEjbTest t = new SerializersCustomizationCDIEjbTest();
    Status s = t.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Exception {
    // logMsg("setup ok");
  }

  public void cleanup() throws Exception {
    // logMsg("cleanup ok");
  }

  private static final Logger logger = System.getLogger(SerializersCustomizationCDIEjbTest.class.getName());

  private static String packagePath = SerializersCustomizationCDIEjbTest.class.getPackageName().replace(".", "/");

  @BeforeEach
  void logStartTest(TestInfo testInfo) {
      logger.log(Logger.Level.INFO, "STARTING TEST : " + testInfo.getDisplayName());
  }

  @AfterEach
  void logFinishTest(TestInfo testInfo) {
      logger.log(Logger.Level.INFO, "FINISHED TEST : " + testInfo.getDisplayName());
  }

  @TargetsContainer("tck-appclient")
  @OverProtocol("appclient")
  @Deployment(name = VEHICLE_ARCHIVE, order = 2)
  public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {

    JavaArchive jsonb_cdi_customizedmapping_serializers_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "jsonb_cdi_customizedmapping_serializers_ejb_vehicle_client.jar");
    jsonb_cdi_customizedmapping_serializers_ejb_vehicle_client.addClasses(
        com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
        com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
        com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
        com.sun.ts.tests.common.vehicle.VehicleClient.class,
        com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
        com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRunner.class,
        com.sun.ts.lib.harness.EETest.class,
        com.sun.ts.lib.harness.EETest.Fault.class,
        com.sun.ts.lib.harness.EETest.SetupException.class,
        com.sun.ts.lib.harness.ServiceEETest.class,
        com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalSerializer.class,
        com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalListSerializer.class,
        com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalListDeserializerInjected.class,
        com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalDeserializerInjected.class,
        com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalDeserializer.class,
        com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalBuilder.class,
        com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.AnimalShelterWithInjectedSerializer.class,
        com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.Animal.class,
        com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.Cat.class,
        com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.Dog.class,
        SerializersCustomizationCDIEjbTest.class
        );

    URL resURL = SerializersCustomizationCDIEjbTest.class.getClassLoader().getResource(packagePath+"/ejb_vehicle_client.xml");
    if(resURL != null) {
      jsonb_cdi_customizedmapping_serializers_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
    }
    jsonb_cdi_customizedmapping_serializers_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + SerializersCustomizationCDIEjbTest.class.getName() + "\n"), "MANIFEST.MF");

    resURL = SerializersCustomizationCDIEjbTest.class.getClassLoader().getResource(packagePath+"/ejb_vehicle_client.jar.sun-application-client.xml");
    if(resURL != null) {
        jsonb_cdi_customizedmapping_serializers_ejb_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
    }

    archiveProcessor.processClientArchive(jsonb_cdi_customizedmapping_serializers_ejb_vehicle_client, SerializersCustomizationCDIEjbTest.class, resURL);


    JavaArchive jsonb_cdi_customizedmapping_serializers_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "jsonb_cdi_customizedmapping_serializers_ejb_vehicle_ejb.jar");
    jsonb_cdi_customizedmapping_serializers_ejb_vehicle_ejb.addClasses(
        com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
        com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
        com.sun.ts.tests.common.vehicle.VehicleClient.class,
        com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
        com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class,
        com.sun.ts.lib.harness.EETest.class,
        com.sun.ts.lib.harness.EETest.Fault.class,
        com.sun.ts.lib.harness.EETest.SetupException.class,
        com.sun.ts.lib.harness.ServiceEETest.class,
        com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalSerializer.class,
        com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalListSerializer.class,
        com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalListDeserializerInjected.class,
        com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalDeserializerInjected.class,
        com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalDeserializer.class,
        com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalBuilder.class,
        com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalBuilder.TYPE.class,
        com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.Animal.class,
        com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.Cat.class,
        com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.Dog.class,
        com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.AnimalShelterWithInjectedSerializer.class,
        SerializersCustomizationCDIEjbTest.class
    );

    // The ejb-jar.xml descriptor
    URL ejbResURL = SerializersCustomizationCDIEjbTest.class.getClassLoader().getResource(packagePath+"/ejb_vehicle_ejb.xml");
    if(ejbResURL != null) {
      jsonb_cdi_customizedmapping_serializers_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
    }
    URL warResURL = SerializersCustomizationCDIEjbTest.class.getClassLoader().getResource(packagePath+"/beans.xml");
    if(warResURL != null) {
      jsonb_cdi_customizedmapping_serializers_ejb_vehicle_ejb.addAsManifestResource(warResURL, "beans.xml");
    }

    // The sun-ejb-jar.xml file need to be added or should this be in in the vendor Arquillian extension?
    ejbResURL = SerializersCustomizationCDIEjbTest.class.getClassLoader().getResource(packagePath+"/ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
    if(ejbResURL != null) {
        jsonb_cdi_customizedmapping_serializers_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
    }
    archiveProcessor.processEjbArchive(jsonb_cdi_customizedmapping_serializers_ejb_vehicle_ejb, SerializersCustomizationCDIEjbTest.class, ejbResURL);

    EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "jsonb_cdi_customizedmapping_serializers_ejb_vehicle.ear");
    ear.addAsModule(jsonb_cdi_customizedmapping_serializers_ejb_vehicle_client);
    ear.addAsModule(jsonb_cdi_customizedmapping_serializers_ejb_vehicle_ejb);
    
    return ear;

  }


  /*
   * @testName: testCDISupport
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.7.2-3
   *
   * @test_Strategy: Assert that CDI injection is supported in serializers and
   * deserializers
   */
  @Test
  @TargetVehicle("ejb")
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

    AnimalShelterWithInjectedSerializer unmarshalledObject = jsonb
        .fromJson("{ \"animals\" : [ "
            + "{ \"type\" : \"cat\", \"cuddly\" : true, \"age\" : 5, \"furry\" : true, \"name\" : \"Garfield\" , \"weight\" : 10.5}, "
            + "{ \"type\" : \"dog\", \"barking\" : true, \"age\" : 3, \"furry\" : false, \"name\" : \"Milo\", \"weight\" : 5.5}, "
            + "{ \"type\" : \"animal\", \"age\" : 6, \"furry\" : false, \"name\" : \"Tweety\", \"weight\" : 0.5}"
            + " ] }", AnimalShelterWithInjectedSerializer.class);
    if (!animalShelter.equals(unmarshalledObject)) {
      throw new Exception(
          "Failed to correctly unmarshall complex type hierarchy using a serializer configured using JsonbTypeSerializer annotation and a deserializer with a CDI managed field configured using JsonbTypeDeserializer annotation.");
    }
  }
}
