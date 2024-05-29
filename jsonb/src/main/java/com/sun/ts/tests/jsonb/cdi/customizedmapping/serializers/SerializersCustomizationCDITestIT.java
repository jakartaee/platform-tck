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

// import com.sun.ts.lib.harness.ServiceEETest;
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

import java.lang.System.Logger;

/**
 * @test
 * @sources SerializersCustomizationTest.java
 * @executeClass com.sun.ts.tests.jsonb.customizedmapping.serializers.SerializersCustomizationTest
 **/
@ExtendWith(ArquillianExtension.class)
public class SerializersCustomizationCDITestIT { // extends ServiceEETest {

  private static final long serialVersionUID = 10L;

  private final Jsonb jsonb = JsonbBuilder.create();

  // public static void main(String[] args) {
  //   EETest t = new SerializersCustomizationCDITest();
  //   Status s = t.run(args, System.out, System.err);
  //   s.exit();
  // }

  public void setup(String[] args, Properties p) throws Exception {
    // logMsg("setup ok");
  }

  public void cleanup() throws Exception {
    // logMsg("cleanup ok");
  }

  private static final Logger logger = System.getLogger(SerializersCustomizationCDITestIT.class.getName());

  private static String packagePath = SerializersCustomizationCDITestIT.class.getPackageName().replace(".", "/");

  @BeforeEach
  void logStartTest(TestInfo testInfo) {
      logger.log(Logger.Level.INFO, "STARTING TEST : " + testInfo.getDisplayName());
  }

  @AfterEach
  void logFinishTest(TestInfo testInfo) {
      logger.log(Logger.Level.INFO, "FINISHED TEST : " + testInfo.getDisplayName());
  }

  @Deployment(testable = false)
  public static WebArchive createServletDeployment() throws IOException {
  
    // EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "jsonb_cdi_customizedmapping_serializers_servlet_vehicle.ear");
    WebArchive war = ShrinkWrap.create(WebArchive.class, "jsonb_cdi_customizedmapping_serializers_servlet_vehicle_web.war");
    war.addClass(SerializersCustomizationCDITestIT.class);

    war.addClass(com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class);
    war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
    war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
    war.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
    war.addClass(com.sun.ts.lib.harness.EETest.class);
    war.addClass(com.sun.ts.lib.harness.RemoteStatus.class);
    war.addClass(com.sun.javatest.Status.class);
    war.addClass(com.sun.ts.lib.harness.ServiceEETest.class);

    war.addClass(com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalBuilder.class);
    war.addClass(com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalDeserializer.class);
    war.addClass(com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalDeserializerInjected.class);
    war.addClass(com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalListDeserializerInjected.class);
    war.addClass(com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalListSerializer.class);
    war.addClass(com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalSerializer.class);

    war.addClass(com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.Animal.class);
    war.addClass(com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.AnimalShelterWithInjectedSerializer.class);
    war.addClass(com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.Cat.class);
    war.addClass(com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.Dog.class);

    // InputStream inStream = SerializersCustomizationCDITestIT.class.getClassLoader().getResourceAsStream(packagePath + "/servlet_vehicle_web.xml");
    // String webXml = editWebXmlString(inStream, "jsonb_cdi_customizedmapping_serializers_servlet_vehicle");
    // war.setWebXML(new StringAsset(webXml));

    war.setWebXML(SerializersCustomizationCDITestIT.class.getClassLoader().getResource(packagePath+"/servlet_vehicle_web.xml"));

    InputStream inStream = SerializersCustomizationCDITestIT.class.getClassLoader().getResourceAsStream(packagePath+"/beans.xml");
    String beansXml = toString(inStream);
    war.addAsWebInfResource(new StringAsset(beansXml), "beans.xml");
    // war.as(ZipExporter.class).exportTo(new File("/Users/alwjosep/Downloads/jsonbprovidertests_servlet_vehicle_web.war"), true);

    // ear.addAsModule(war);

    return war;

  }

  public static String toString(InputStream inStream) throws IOException{
      try (BufferedReader bufReader = new BufferedReader(new InputStreamReader(inStream, StandardCharsets.UTF_8))) {
        return bufReader.lines().collect(Collectors.joining(System.lineSeparator()));
      }
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
