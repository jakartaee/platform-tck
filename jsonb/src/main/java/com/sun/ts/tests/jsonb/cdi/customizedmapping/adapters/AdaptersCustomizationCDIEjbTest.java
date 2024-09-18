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

package com.sun.ts.tests.jsonb.cdi.customizedmapping.adapters;

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
import com.sun.ts.tests.jsonb.cdi.customizedmapping.adapters.model.AnimalShelterInjectedAdapter;
import com.sun.ts.tests.jsonb.cdi.customizedmapping.adapters.model.Animal;
import com.sun.ts.tests.jsonb.cdi.customizedmapping.adapters.model.Cat;
import com.sun.ts.tests.jsonb.cdi.customizedmapping.adapters.model.Dog;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.asset.StringAsset;

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
 * @sources AdaptersCustomizationTest.java
 * @executeClass com.sun.ts.tests.jsonb.customizedmapping.adapters.AdaptersCustomizationTest
 **/
/*
 * @class.setup_props: webServerHost; webServerPort; ts_home;
 */

@Tag("tck-appclient")
@Tag("jsonb")
@Tag("platform")
@Tag("web")
@ExtendWith(ArquillianExtension.class)
public class AdaptersCustomizationCDIEjbTest extends ServiceEETest {

  private static final long serialVersionUID = 10L;

  static final String VEHICLE_ARCHIVE = "jsonb_cdi_customizedmapping_adapters_ejb_vehicle";

  private final Jsonb jsonb = JsonbBuilder.create();

  public static void main(String[] args) {
    AdaptersCustomizationCDIEjbTest t = new AdaptersCustomizationCDIEjbTest();
    Status s = t.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Exception {
    // logMsg("setup ok");
  }

  public void cleanup() throws Exception {
    // logMsg("cleanup ok");
  }

  private static final Logger logger = System.getLogger(AdaptersCustomizationCDIEjbTest.class.getName());

  private static String packagePath = AdaptersCustomizationCDIEjbTest.class.getPackageName().replace(".", "/");

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
  public static EnterpriseArchive createDeploymentVehicle() {
  
    JavaArchive jsonb_cdi_customizedmapping_adapters_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "jsonb_cdi_customizedmapping_adapters_ejb_vehicle_client.jar");
    jsonb_cdi_customizedmapping_adapters_ejb_vehicle_client.addClass(AdaptersCustomizationCDIEjbTest.class)
        .addClass(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class)
        .addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class)
        .addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class)
        .addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class)
        .addClass(com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class)
        .addClass(com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRunner.class)
        .addClass(com.sun.ts.lib.harness.EETest.class)
        .addClass(com.sun.ts.lib.harness.EETest.Fault.class)
        .addClass(com.sun.ts.lib.harness.EETest.SetupException.class)
        .addClass(com.sun.ts.lib.harness.ServiceEETest.class);

    URL resURL = AdaptersCustomizationCDIEjbTest.class.getClassLoader().getResource(packagePath+"/ejb_vehicle_client.xml");
    if(resURL != null) {
      jsonb_cdi_customizedmapping_adapters_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
    }
    jsonb_cdi_customizedmapping_adapters_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + AdaptersCustomizationCDIEjbTest.class.getName() + "\n"), "MANIFEST.MF");

    // resURL = AdaptersCustomizationCDIEjbTest.class.getClassLoader().getResource(packagePath+"/ejb_vehicle_client.jar.sun-application-client.xml");
    // if(resURL != null) {
    //     jsonb_cdi_customizedmapping_adapters_ejb_vehicle_client.addAsManifestResource(resURL, "sun-ejb-jar.xml");
    // }
    // archiveProcessor.processEjbArchive(jsonb_cdi_customizedmapping_adapters_ejb_vehicle_client, AdaptersCustomizationCDIEjbTest.class, resURL);


    JavaArchive jsonb_cdi_customizedmapping_adapters_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "jsonb_cdi_customizedmapping_adapters_ejb_vehicle_ejb.jar");
    // The class files
    jsonb_cdi_customizedmapping_adapters_ejb_vehicle_ejb.addClasses(
        com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
        com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
        com.sun.ts.tests.common.vehicle.VehicleClient.class,
        com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
        com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class,
        com.sun.ts.lib.harness.EETest.class,
        com.sun.ts.lib.harness.EETest.Fault.class,
        com.sun.ts.lib.harness.EETest.SetupException.class,
        com.sun.ts.lib.harness.ServiceEETest.class,
        com.sun.ts.tests.jsonb.cdi.customizedmapping.adapters.model.adapter.AnimalIdentifier.class,
        com.sun.ts.tests.jsonb.cdi.customizedmapping.adapters.model.adapter.AnimalJson.TYPE.class,
        com.sun.ts.tests.jsonb.cdi.customizedmapping.adapters.model.adapter.AnimalJson.class,
        com.sun.ts.tests.jsonb.cdi.customizedmapping.adapters.model.adapter.InjectedAdapter.class,
        com.sun.ts.tests.jsonb.cdi.customizedmapping.adapters.model.adapter.InjectedListAdapter.class,
        com.sun.ts.tests.jsonb.cdi.customizedmapping.adapters.model.Animal.class,
        com.sun.ts.tests.jsonb.cdi.customizedmapping.adapters.model.AnimalShelterInjectedAdapter.class,
        com.sun.ts.tests.jsonb.cdi.customizedmapping.adapters.model.Cat.class,
        com.sun.ts.tests.jsonb.cdi.customizedmapping.adapters.model.Dog.class,
        AdaptersCustomizationCDIEjbTest.class
    );

    // The ejb-jar.xml descriptor
    URL ejbResURL = AdaptersCustomizationCDIEjbTest.class.getClassLoader().getResource(packagePath+"/ejb_vehicle_ejb.xml");
    if(ejbResURL != null) {
      jsonb_cdi_customizedmapping_adapters_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
    }

    URL warResURL = AdaptersCustomizationCDIEjbTest.class.getClassLoader().getResource(packagePath+"/beans.xml");
    if(warResURL != null) {
      jsonb_cdi_customizedmapping_adapters_ejb_vehicle_ejb.addAsManifestResource(warResURL, "beans.xml");
    }


    // The sun-ejb-jar.xml file need to be added or should this be in in the vendor Arquillian extension?
    // ejbResURL = AdaptersCustomizationCDIEjbTest.class.getClassLoader().getResource(packagePath+"/ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
    // if(ejbResURL != null) {
    //     jsonb_cdi_customizedmapping_adapters_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
    // }
    // archiveProcessor.processEjbArchive(jsonb_cdi_customizedmapping_adapters_ejb_vehicle_ejb, AdaptersCustomizationCDIEjbTest.class, ejbResURL);

    EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "jsonb_cdi_customizedmapping_adapters_ejb_vehicle.ear");
    ear.addAsModule(jsonb_cdi_customizedmapping_adapters_ejb_vehicle_client);
    ear.addAsModule(jsonb_cdi_customizedmapping_adapters_ejb_vehicle_ejb);
    
    return ear;

  }

  /*
   * @testName: testCDISupport
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.7.1-3
   *
   * @test_Strategy: Assert that CDI injection is supported in adapters
   */
  @Test
  @TargetVehicle("ejb")
  public void testCDISupport() throws Exception {
    AnimalShelterInjectedAdapter animalShelter = new AnimalShelterInjectedAdapter();
    animalShelter.addAnimal(new Cat(5, "Garfield", 10.5f, true, true));
    animalShelter.addAnimal(new Dog(3, "Milo", 5.5f, false, true));
    animalShelter.addAnimal(new Animal(6, "Tweety", 0.5f, false));

    String jsonString = jsonb.toJson(animalShelter);
    if (!jsonString.matches("\\{\\s*\"animals\"\\s*:\\s*\\[\\s*"
        + "\\{\\s*\"age\"\\s*:\\s*5\\s*,\\s*\"cuddly\"\\s*:\\s*true\\s*,\\s*\"furry\"\\s*:\\s*true\\s*,\\s*\"name\"\\s*:\\s*\"Garfield\"\\s*,\\s*\"type\"\\s*:\\s*\"CAT\"\\s*,\\s*\"weight\"\\s*:\\s*10.5\\s*}\\s*,\\s*"
        + "\\{\\s*\"age\"\\s*:\\s*3\\s*,\\s*\"barking\"\\s*:\\s*true\\s*,\\s*\"furry\"\\s*:\\s*false\\s*,\\s*\"name\"\\s*:\\s*\"Milo\"\\s*,\\s*\"type\"\\s*:\\s*\"DOG\"\\s*,\\s*\"weight\"\\s*:\\s*5.5\\s*}\\s*,\\s*"
        + "\\{\\s*\"age\"\\s*:\\s*6\\s*,\\s*\"furry\"\\s*:\\s*false\\s*,\\s*\"name\"\\s*:\\s*\"Tweety\"\\s*,\\s*\"type\"\\s*:\\s*\"GENERIC\"\\s*,\\s*\"weight\"\\s*:\\s*0.5\\s*}\\s*"
        + "]\\s*}")) {
      throw new Exception(
          "Failed to correctly marshall complex type hierarchy using an adapter with a CDI managed field configured using JsonbTypeAdapter annotation to a simpler class.");
    }

    AnimalShelterInjectedAdapter unmarshalledObject = jsonb
        .fromJson("{ \"animals\" : [ "
            + "{ \"age\" : 5, \"cuddly\" : true, \"furry\" : true, \"name\" : \"Garfield\" , \"type\" : \"CAT\", \"weight\" : 10.5}, "
            + "{ \"age\" : 3, \"barking\" : true, \"furry\" : false, \"name\" : \"Milo\", \"type\" : \"DOG\", \"weight\" : 5.5}, "
            + "{ \"age\" : 6, \"furry\" : false, \"name\" : \"Tweety\", \"type\" : \"GENERIC\", \"weight\" : 0.5}"
            + " ] }", AnimalShelterInjectedAdapter.class);
    if (!animalShelter.equals(unmarshalledObject)) {
      throw new Exception(
          "Failed to correctly unmarshall complex type hierarchy using an adapter with a CDI managed field configured using JsonbTypeAdapter annotation to a simpler class.");
    }
  }
}
