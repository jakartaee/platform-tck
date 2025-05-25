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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URLClassLoader;
import java.net.URL;

import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.tests.common.base.ServiceEETest;
import com.sun.ts.tests.jsonp.common.JSONP_Util;
import com.sun.ts.tests.jsonp.provider.MyJsonGenerator;
import com.sun.ts.tests.jsonp.provider.MyJsonProvider;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonException;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;
import jakarta.json.JsonWriter;
import jakarta.json.JsonWriterFactory;
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonGeneratorFactory;
import jakarta.json.stream.JsonParser;
import jakarta.json.stream.JsonParserFactory;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.asset.UrlAsset;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;

import tck.arquillian.protocol.common.TargetVehicle;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

@Tag("tck-appclient")
@Tag("platform")
@Tag("jsonp")
@ExtendWith(ArquillianExtension.class)
public class ClientAppclientTest extends Client {


  private static String packagePath = ClientAppclientTest.class.getPackageName().replace(".", "/");

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

  static final String VEHICLE_ARCHIVE = "jsonprovidertests_appclient_vehicle";
  
  @TargetsContainer("tck-appclient")
  @OverProtocol("appclient")
  @Deployment(name = VEHICLE_ARCHIVE, testable = true)
  public static EnterpriseArchive createAppclientDeployment() throws Exception {

    String providerPackagePath = MyJsonProvider.class.getPackageName().replace(".", "/");
  
    JavaArchive jsonprovidertests_appclient_vehicle_client = ShrinkWrap.create(JavaArchive.class, "jsonprovidertests_appclient_vehicle_client.jar");
    jsonprovidertests_appclient_vehicle_client.addClasses(
      com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
      com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
      com.sun.ts.tests.common.vehicle.VehicleClient.class,
      com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
      com.sun.ts.tests.jsonp.common.JSONP_Data.class,
      com.sun.ts.tests.jsonp.common.JSONP_Util.class,
      com.sun.ts.tests.jsonp.common.MyBufferedReader.class,
      com.sun.ts.tests.jsonp.common.MyBufferedWriter.class,
      com.sun.ts.tests.jsonp.common.MyBufferedInputStream.class,
      com.sun.ts.tests.jsonp.common.MyJsonLocation.class,
      com.sun.ts.tests.jsonp.provider.MyJsonProvider.class,
      com.sun.ts.tests.jsonp.provider.MyJsonGenerator.class,
      EETest.class,
      ServiceEETest.class,
      Client.class
      );

    URL jsonURL = ClientAppclientTest.class.getClassLoader().getResource("com/sun/ts/tests/jsonp/pluggability/jsonprovidertests/jsonArrayWithAllTypesOfData.json");
    jsonprovidertests_appclient_vehicle_client.addAsResource(jsonURL, "classes/jsonArrayWithAllTypesOfData.json");
    jsonURL = ClientAppclientTest.class.getClassLoader().getResource("com/sun/ts/tests/jsonp/pluggability/jsonprovidertests/jsonObjectWithAllTypesOfData.json");
    jsonprovidertests_appclient_vehicle_client.addAsResource(jsonURL, "classes/jsonObjectWithAllTypesOfData.json");

    URL resURL = ClientAppclientTest.class.getClassLoader().getResource(packagePath+"/appclient_vehicle_client.xml");
    if(resURL != null) {
      jsonprovidertests_appclient_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
    }
    jsonprovidertests_appclient_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");


    JavaArchive jsonp_alternate_provider = ShrinkWrap.create(JavaArchive.class, "jsonp_alternate_provider.jar")
      .addClasses(com.sun.ts.tests.jsonp.provider.MyJsonGenerator.class,
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

      jsonp_alternate_provider.as(ZipExporter.class).exportTo(new File(TEMP_DIR + File.separator + "jsonp_alternate_provider.jar"), true);

      providerJarDeployed = true;

    EnterpriseArchive jsonprovidertests_appclient_vehicle_client_ear = ShrinkWrap.create(EnterpriseArchive.class, "jsonprovidertests_appclient_vehicle.ear");
    jsonprovidertests_appclient_vehicle_client_ear.addAsModule(jsonprovidertests_appclient_vehicle_client);
    jsonprovidertests_appclient_vehicle_client_ear.addAsManifestResource(new StringAsset("Main-Class: " + ClientAppclientTest.class.getName() + "\n"), "MANIFEST.MF");
    jsonprovidertests_appclient_vehicle_client_ear.addAsLibrary(jsonp_alternate_provider);
    jsonprovidertests_appclient_vehicle_client_ear.addAsModule(jsonp_alternate_provider);

    return jsonprovidertests_appclient_vehicle_client_ear;

  }

  public void removeProviderJarFromCP() throws Exception {
		if (providerJarDeployed) {
			URLClassLoader currentThreadClassLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
			Thread.currentThread().setContextClassLoader(currentThreadClassLoader.getParent());
			currentThreadClassLoader.close();
			providerJarDeployed = false;
		}
	}

	public void createProviderJar() throws Exception {

    String providerPackagePath = MyJsonProvider.class.getPackageName().replace(".", "/");

    JavaArchive jarArchive = ShrinkWrap.create(JavaArchive.class, "jsonp_alternate_provider.jar")
      .addClasses(com.sun.ts.tests.jsonp.provider.MyJsonGenerator.class,
      com.sun.ts.tests.jsonp.provider.MyJsonGeneratorFactory.class,
      com.sun.ts.tests.jsonp.provider.MyJsonParser.class,
      com.sun.ts.tests.jsonp.provider.MyJsonParserFactory.class,
      com.sun.ts.tests.jsonp.provider.MyJsonProvider.class,
      com.sun.ts.tests.jsonp.provider.MyJsonReader.class,
      com.sun.ts.tests.jsonp.provider.MyJsonReaderFactory.class,
      com.sun.ts.tests.jsonp.provider.MyJsonWriter.class,
      com.sun.ts.tests.jsonp.provider.MyJsonWriterFactory.class)
      .addAsResource(new UrlAsset(MyJsonProvider.class.getClassLoader().getResource(providerPackagePath+"/META-INF/services/jakarta.json.spi.JsonProvider")), "META-INF/services/jakarta.json.spi.JsonProvider");

    jarArchive.as(ZipExporter.class).exportTo(new File(TEMP_DIR + File.separator + "jsonp_alternate_provider.jar"), true);

		ClassLoader currentThreadClassLoader = Thread.currentThread().getContextClassLoader();
		URLClassLoader urlClassLoader = new URLClassLoader(
				new URL[] { new File(TEMP_DIR + File.separator + "jsonp_alternate_provider.jar").toURL() },
				currentThreadClassLoader);
		Thread.currentThread().setContextClassLoader(urlClassLoader);

		providerJarDeployed = true;

	}


  private String providerPath = null;


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
