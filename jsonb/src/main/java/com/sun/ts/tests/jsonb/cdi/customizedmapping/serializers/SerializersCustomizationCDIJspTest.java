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

import java.lang.System.Logger;

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
public class SerializersCustomizationCDIJspTest extends ServiceEETest {

  static final String VEHICLE_ARCHIVE = "jsonb_cdi_customizedmapping_serializers_jsp_vehicle";

  private final Jsonb jsonb = JsonbBuilder.create();

  public static void main(String[] args) {
    SerializersCustomizationCDIJspTest t = new SerializersCustomizationCDIJspTest();
    Status s = t.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Exception {
    // logMsg("setup ok");
  }

  public void cleanup() throws Exception {
    // logMsg("cleanup ok");
  }

  private static final Logger logger = System.getLogger(SerializersCustomizationCDIJspTest.class.getName());

  private static String packagePath = SerializersCustomizationCDIJspTest.class.getPackageName().replace(".", "/");

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
  public static EnterpriseArchive createServletDeployment() throws Exception {
  
    WebArchive jsonb_cdi_customizedmapping_serializers_jsp_vehicle = ShrinkWrap.create(WebArchive.class, "jsonb_cdi_customizedmapping_serializers_jsp_vehicle_web.war");
    jsonb_cdi_customizedmapping_serializers_jsp_vehicle.addClass(SerializersCustomizationCDIJspTest.class)
      .addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class)
      .addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class)
      .addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class)
      .addClass(com.sun.ts.lib.harness.EETest.class)
      .addClass(com.sun.ts.lib.harness.EETest.SetupException.class)      
      .addClass(com.sun.ts.lib.harness.EETest.Fault.class)      
      .addClass(com.sun.ts.lib.harness.ServiceEETest.class)
      .addClass(com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalBuilder.class)
      .addClass(com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalBuilder.TYPE.class)
      .addClass(com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalDeserializer.class)
      .addClass(com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalDeserializerInjected.class)
      .addClass(com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalListDeserializerInjected.class)
      .addClass(com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalListSerializer.class)
      .addClass(com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalSerializer.class)
      .addClass(com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.Animal.class)
      .addClass(com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.AnimalShelterWithInjectedSerializer.class)
      .addClass(com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.Cat.class)
      .addClass(com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.Dog.class);

    jsonb_cdi_customizedmapping_serializers_jsp_vehicle.setWebXML(SerializersCustomizationCDIJspTest.class.getClassLoader().getResource(packagePath+"/jsp_vehicle_web.xml"));
    URL warResURL = SerializersCustomizationCDIJspTest.class.getClassLoader().getResource(packagePath+"/beans.xml");
    if(warResURL != null) {
      jsonb_cdi_customizedmapping_serializers_jsp_vehicle.addAsWebResource(warResURL, "/WEB-INF/beans.xml");
    }

    // Web content
    URL resURL = SerializersCustomizationCDIJspTest.class.getResource("/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
    jsonb_cdi_customizedmapping_serializers_jsp_vehicle.addAsWebResource(resURL, "/jsp_vehicle.jsp");
    resURL = SerializersCustomizationCDIJspTest.class.getResource("/vehicle/jsp/contentRoot/client.html");
    jsonb_cdi_customizedmapping_serializers_jsp_vehicle.addAsWebResource(resURL, "/client.html");    

    EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "jsonb_cdi_customizedmapping_serializers_jsp_vehicle.ear");
    ear.addAsModule(jsonb_cdi_customizedmapping_serializers_jsp_vehicle);
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
  @TargetVehicle("jsp")
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
