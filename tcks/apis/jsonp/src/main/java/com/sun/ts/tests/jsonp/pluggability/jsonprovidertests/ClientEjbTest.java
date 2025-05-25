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


package com.sun.ts.tests.jsonp.pluggability.jsonprovidertests;

import com.sun.ts.lib.harness.Fault;
import com.sun.ts.lib.harness.SetupException;
import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.tests.common.base.ServiceEETest;
import com.sun.ts.tests.jsonp.provider.MyJsonProvider;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.asset.UrlAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

import java.lang.System.Logger;
import java.net.URL;

@Tag("tck-appclient")
@Tag("platform")
@Tag("jsonp")
@ExtendWith(ArquillianExtension.class)
public class ClientEjbTest extends Client {

  private static String packagePath = ClientEjbTest.class.getPackageName().replace(".", "/");

  public static final String TEMP_DIR = System.getProperty("java.io.tmpdir", "/tmp");

  private static boolean providerJarDeployed = false;

  @BeforeEach
  void logStartTest(TestInfo testInfo) throws Exception {
      logger.log(Logger.Level.INFO, "STARTING TEST : " + testInfo.getDisplayName());
  }

  // @BeforeEach
  // public void setup() throws Exception {
  //   createProviderJar();
  //   logger.log(Logger.Level.INFO, "setup ok");
  // }

  @AfterEach
  void logFinishTest(TestInfo testInfo) throws Exception {
      logger.log(Logger.Level.INFO, "FINISHED TEST : " + testInfo.getDisplayName());
  }


  static final String VEHICLE_ARCHIVE = "jsonprovidertests_ejb_vehicle";
  
  @TargetsContainer("tck-appclient")
  @OverProtocol("appclient")
  @Deployment(name = VEHICLE_ARCHIVE, testable = true)
  public static EnterpriseArchive createEjbDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) throws Exception {

    String providerPackagePath = MyJsonProvider.class.getPackageName().replace(".", "/");
    
    JavaArchive jsonp_alternate_provider = ShrinkWrap.create(JavaArchive.class, "jsonp_alternate_provider.jar");
    jsonp_alternate_provider.addClasses(com.sun.ts.tests.jsonp.provider.MyJsonGenerator.class,
      com.sun.ts.tests.jsonp.provider.MyJsonGeneratorFactory.class,
      com.sun.ts.tests.jsonp.provider.MyJsonParser.class,
      com.sun.ts.tests.jsonp.provider.MyJsonParserFactory.class,
      com.sun.ts.tests.jsonp.provider.MyJsonProvider.class,
      com.sun.ts.tests.jsonp.provider.MyJsonReader.class,
      com.sun.ts.tests.jsonp.provider.MyJsonReaderFactory.class,
      com.sun.ts.tests.jsonp.provider.MyJsonWriter.class,
      com.sun.ts.tests.jsonp.provider.MyJsonWriterFactory.class,
      com.sun.ts.tests.jsonp.common.JSONP_Util.class
       )
      .addAsResource(new UrlAsset(MyJsonProvider.class.getClassLoader().getResource(providerPackagePath+"/META-INF/services/jakarta.json.spi.JsonProvider")), "META-INF/services/jakarta.json.spi.JsonProvider");


    JavaArchive jsonprovidertests_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "jsonprovidertests_ejb_vehicle_client.jar");
    jsonprovidertests_ejb_vehicle_client.addClasses(
        com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
        com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
        com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
        com.sun.ts.tests.common.vehicle.VehicleClient.class,
        com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
        com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRunner.class,
        EETest.class,
        ServiceEETest.class,
        com.sun.ts.tests.jsonp.provider.MyJsonProvider.class,
        com.sun.ts.tests.jsonp.provider.MyJsonGenerator.class,
        com.sun.ts.tests.jsonp.common.JSONP_Util.class,
        Client.class
    );

    URL resURL = ClientEjbTest.class.getClassLoader().getResource(packagePath+"/ejb_vehicle_client.xml");
    if(resURL != null) {
      jsonprovidertests_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
    }
    jsonprovidertests_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
    resURL = ClientEjbTest.class.getClassLoader().getResource(packagePath+"/ejb_vehicle_client.jar.sun-application-client.xml");
    if(resURL != null) {
      jsonprovidertests_ejb_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
    }
    archiveProcessor.processClientArchive(jsonprovidertests_ejb_vehicle_client, ClientEjbTest.class, resURL);


    JavaArchive jsonprovidertests_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "jsonprovidertests_ejb_vehicle_ejb.jar");
    jsonprovidertests_ejb_vehicle_ejb.addClasses(
        com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
        com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class,
        com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
        com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
        com.sun.ts.tests.common.vehicle.VehicleClient.class,
        EETest.class,
        Fault.class,
        SetupException.class,
        ServiceEETest.class,
        com.sun.ts.tests.jsonp.common.JSONP_Data.class,
        com.sun.ts.tests.jsonp.common.JSONP_Util.class,
        com.sun.ts.tests.jsonp.common.MyBufferedInputStream.class,
        com.sun.ts.tests.jsonp.common.MyBufferedReader.class,
        com.sun.ts.tests.jsonp.common.MyBufferedWriter.class,
        com.sun.ts.tests.jsonp.common.MyJsonLocation.class,
        com.sun.ts.tests.jsonp.provider.MyJsonProvider.class,
        com.sun.ts.tests.jsonp.provider.MyJsonGenerator.class,
            Client.class
    );
    // The ejb-jar.xml descriptor
    URL ejbResURL = ClientEjbTest.class.getClassLoader().getResource(packagePath+"/ejb_vehicle_ejb.xml");
    if(ejbResURL != null) {
      jsonprovidertests_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
    }
    URL jsonURL = ClientEjbTest.class.getClassLoader().getResource(packagePath+"/jsonArrayWithAllTypesOfData.json");
    jsonprovidertests_ejb_vehicle_ejb.addAsResource(jsonURL, "/jsonArrayWithAllTypesOfData.json");
    jsonURL = ClientEjbTest.class.getClassLoader().getResource(packagePath+"/jsonObjectWithAllTypesOfData.json");
    jsonprovidertests_ejb_vehicle_ejb.addAsResource(jsonURL, "/jsonObjectWithAllTypesOfData.json");

    // The sun-ejb-jar.xml file need to be added or should this be in in the vendor Arquillian extension?
    ejbResURL = ClientEjbTest.class.getClassLoader().getResource(packagePath+"/ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
    if(ejbResURL != null) {
      jsonprovidertests_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
    }
    archiveProcessor.processEjbArchive(jsonprovidertests_ejb_vehicle_ejb, ClientEjbTest.class, ejbResURL);


    EnterpriseArchive jsonprovidertests_ejb_vehicle_client_ear = ShrinkWrap.create(EnterpriseArchive.class, "jsonprovidertests_ejb_vehicle.ear");
    jsonprovidertests_ejb_vehicle_client_ear.addAsModule(jsonprovidertests_ejb_vehicle_client);
    jsonprovidertests_ejb_vehicle_client_ear.addAsModule(jsonprovidertests_ejb_vehicle_ejb);
    jsonprovidertests_ejb_vehicle_client_ear.addAsModule(jsonp_alternate_provider);
    jsonprovidertests_ejb_vehicle_client_ear.addAsLibrary(jsonp_alternate_provider);

    return jsonprovidertests_ejb_vehicle_client_ear;

  }

  /* Tests */

  @Test
  @TargetVehicle("appclient")
  public void jsonProviderTest1() throws Exception {
    super.jsonProviderTest1();
  }
  @Test
  @TargetVehicle("appclient")
  public void jsonProviderTest2() throws Exception {
    super.jsonProviderTest2();
  }
  @Test
  @TargetVehicle("appclient")
  public void jsonProviderTest3() throws Exception {
    super.jsonProviderTest3();
  }
  @Test
  @TargetVehicle("appclient")
  public void jsonProviderTest4() throws Exception {
    super.jsonProviderTest4();
  }
  @Test
  @TargetVehicle("appclient")
  public void jsonProviderTest5() throws Exception {
    super.jsonProviderTest5();
  }
  @Test
  @TargetVehicle("appclient")
  public void jsonProviderTest6() throws Exception {
    super.jsonProviderTest6();
  }
  @Test
  @TargetVehicle("appclient")
  public void jsonProviderTest7() throws Exception {
    super.jsonProviderTest7();
  }
  @Test
  @TargetVehicle("appclient")
  public void jsonProviderTest8() throws Exception {
    super.jsonProviderTest8();
  }
  @Test
  @TargetVehicle("appclient")
  public void jsonProviderTest9() throws Exception {
    super.jsonProviderTest9();
  }
  @Test
  @TargetVehicle("appclient")
  public void jsonProviderTest10() throws Exception {
    super.jsonProviderTest10();
  }
  @Test
  @TargetVehicle("appclient")
  public void jsonProviderTest11() throws Exception {
    super.jsonProviderTest11();
  }
  @Test
  @TargetVehicle("appclient")
  public void jsonProviderTest12() throws Exception {
    super.jsonProviderTest12();
  }
  @Test
  @TargetVehicle("appclient")
  public void jsonProviderTest13() throws Exception {
    super.jsonProviderTest13();
  }
  @Test
  @TargetVehicle("appclient")
  public void jsonProviderTest14() throws Exception {
    super.jsonProviderTest14();
  }
  @Test
  @TargetVehicle("appclient")
  public void jsonProviderTest15() throws Exception {
    super.jsonProviderTest15();
  }

  @Test
  @TargetVehicle("appclient")
  public void jsonProviderTest16() throws Exception {
    super.jsonProviderTest16();
  }

  @Test
  @TargetVehicle("appclient")
  public void jsonProviderTest17() throws Exception {
    super.jsonProviderTest17();
  }

  @Test
  @TargetVehicle("appclient")
  public void jsonProviderTest18() throws Exception {
    super.jsonProviderTest18();
  }
}
