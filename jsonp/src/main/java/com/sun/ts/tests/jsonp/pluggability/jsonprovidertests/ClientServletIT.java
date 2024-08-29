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

import java.io.File;
import java.net.URLClassLoader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.nio.charset.StandardCharsets;
import java.net.URL;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.harness.ServiceEETest;
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
import org.jboss.shrinkwrap.api.spec.WebArchive;
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
import static org.junit.jupiter.api.Assertions.assertFalse;

import tck.arquillian.protocol.common.TargetVehicle;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;

import java.lang.System.Logger;

@Tag("tck-javatest")
@ExtendWith(ArquillianExtension.class)
public class ClientServletIT extends ServiceEETest {

  private static final Logger logger = System.getLogger(ClientServletIT.class.getName());

  private static String packagePath = ClientServletIT.class.getPackageName().replace(".", "/");

  public final String TEMP_DIR = System.getProperty("java.io.tmpdir", "/tmp");

  private boolean providerJarDeployed = false;

  @BeforeEach
  void logStartTest(TestInfo testInfo) throws Exception {
      logger.log(Logger.Level.INFO, "STARTING TEST : " + testInfo.getDisplayName());
  }

  @BeforeEach
  public void setup() throws Exception {
    createProviderJar();
    logger.log(Logger.Level.INFO, "setup ok");
  }

  @AfterEach
  void logFinishTest(TestInfo testInfo) throws Exception {
      logger.log(Logger.Level.INFO, "FINISHED TEST : " + testInfo.getDisplayName());
  }

  @AfterEach
  public void cleanup() throws Exception {
    removeProviderJarFromCP();
    MyJsonProvider.clearCalls();
    MyJsonGenerator.clearCalls();
    logger.log(Logger.Level.INFO, "cleanup ok");

  }

  static final String VEHICLE_ARCHIVE = "jsonprovidertests_servlet_vehicle";
  
  @TargetsContainer("tck-javatest")
  @OverProtocol("javatest")
  @Deployment(name = VEHICLE_ARCHIVE, testable = true)
  public static EnterpriseArchive createServletDeployment() throws Exception {

    String providerPackagePath = MyJsonProvider.class.getPackageName().replace(".", "/");
  
    WebArchive jsonprovidertests_servlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jsonprovidertests_servlet_vehicle_web.war");
    jsonprovidertests_servlet_vehicle_web.addClass(ClientServletIT.class)
      .addClass(com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class)
      .addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class)
      .addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class)  
      .addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class)
      .addClass(com.sun.ts.tests.jsonp.common.JSONP_Data.class)
      .addClass(com.sun.ts.tests.jsonp.common.JSONP_Util.class)
      .addClass(com.sun.ts.tests.jsonp.common.MyBufferedReader.class)
      .addClass(com.sun.ts.tests.jsonp.common.MyBufferedWriter.class)
      .addClass(com.sun.ts.tests.jsonp.common.MyBufferedInputStream.class)
      .addClass(com.sun.ts.tests.jsonp.common.MyJsonLocation.class)
      .addClass(com.sun.ts.lib.harness.EETest.class)
      .addClass(com.sun.ts.lib.harness.ServiceEETest.class);

    URL jsonURL = ClientServletIT.class.getClassLoader().getResource("com/sun/ts/tests/jsonp/pluggability/jsonprovidertests/jsonArrayWithAllTypesOfData.json");
    jsonprovidertests_servlet_vehicle_web.addAsWebInfResource(jsonURL, "classes/jsonArrayWithAllTypesOfData.json");
    jsonURL = ClientServletIT.class.getClassLoader().getResource("com/sun/ts/tests/jsonp/pluggability/jsonprovidertests/jsonObjectWithAllTypesOfData.json");
    jsonprovidertests_servlet_vehicle_web.addAsWebInfResource(jsonURL, "classes/jsonObjectWithAllTypesOfData.json");
    URL webXML = ClientServletIT.class.getClassLoader().getResource("com/sun/ts/tests/jsonp/pluggability/jsonprovidertests/servlet_vehicle_web.xml");
    jsonprovidertests_servlet_vehicle_web.setWebXML(webXML);

    JavaArchive jarArchive = ShrinkWrap.create(JavaArchive.class, "jsonp_alternate_provider.jar")
      .addClass(com.sun.ts.tests.jsonp.provider.MyJsonGenerator.class)
      .addClass(com.sun.ts.tests.jsonp.provider.MyJsonGeneratorFactory.class)
      .addClass(com.sun.ts.tests.jsonp.provider.MyJsonParser.class)
      .addClass(com.sun.ts.tests.jsonp.provider.MyJsonParserFactory.class)
      .addClass(com.sun.ts.tests.jsonp.provider.MyJsonProvider.class)
      .addClass(com.sun.ts.tests.jsonp.provider.MyJsonReader.class)
      .addClass(com.sun.ts.tests.jsonp.provider.MyJsonReaderFactory.class)
      .addClass(com.sun.ts.tests.jsonp.provider.MyJsonWriter.class)
      .addClass(com.sun.ts.tests.jsonp.provider.MyJsonWriterFactory.class)     
      .addAsResource(new UrlAsset(MyJsonProvider.class.getClassLoader().getResource(providerPackagePath+"/META-INF/services/jakarta.json.spi.JsonProvider")), "META-INF/services/jakarta.json.spi.JsonProvider");

    jsonprovidertests_servlet_vehicle_web.addAsLibrary(jarArchive);

    EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "jsonprovidertests_servlet_vehicle.ear");
    ear.addAsModule(jsonprovidertests_servlet_vehicle_web);
    ear.addAsLibrary(jarArchive);
    return ear;

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
      .addClass(com.sun.ts.tests.jsonp.provider.MyJsonGenerator.class)
      .addClass(com.sun.ts.tests.jsonp.provider.MyJsonGeneratorFactory.class)
      .addClass(com.sun.ts.tests.jsonp.provider.MyJsonParser.class)
      .addClass(com.sun.ts.tests.jsonp.provider.MyJsonParserFactory.class)
      .addClass(com.sun.ts.tests.jsonp.provider.MyJsonProvider.class)
      .addClass(com.sun.ts.tests.jsonp.provider.MyJsonReader.class)
      .addClass(com.sun.ts.tests.jsonp.provider.MyJsonReaderFactory.class)
      .addClass(com.sun.ts.tests.jsonp.provider.MyJsonWriter.class)
      .addClass(com.sun.ts.tests.jsonp.provider.MyJsonWriterFactory.class)    
      .addAsResource(new UrlAsset(MyJsonProvider.class.getClassLoader().getResource(providerPackagePath+"/META-INF/services/jakarta.json.spi.JsonProvider")), "META-INF/services/jakarta.json.spi.JsonProvider");

    jarArchive.as(ZipExporter.class).exportTo(new File(TEMP_DIR + File.separator + "jsonp_alternate_provider.jar"), true);

		ClassLoader currentThreadClassLoader = Thread.currentThread().getContextClassLoader();
		URLClassLoader urlClassLoader = new URLClassLoader(
				new URL[] { new File(TEMP_DIR + File.separator + "jsonp_alternate_provider.jar").toURL() },
				currentThreadClassLoader);
		Thread.currentThread().setContextClassLoader(urlClassLoader);

		providerJarDeployed = true;

	}

  private static final String MY_JSONPROVIDER_CLASS = "com.sun.ts.tests.jsonp.provider.MyJsonProvider";

  private String providerPath = null;

  // public static void main(String[] args) {
  //   ClientServletIT theTests = new ClientServletIT();
  //   Status s = theTests.run(args, System.out, System.err);
  //   s.exit();
  // }

  /* Test setup */

    /*
     * @class.setup_props:
     * This is needed by the vehicle base classes
     */
  public void setup(String[] args, Properties p) throws Exception {

  }

  /* Tests */

  /*
   * @testName: jsonProviderTest1
   * 
   * @assertion_ids: JSONP:JAVADOC:152;
   * 
   * @test_Strategy: Test call of SPI provider method with signature: o public
   * static JsonProvider provider()
   */
  @Test
  @TargetVehicle("servlet")
  public void jsonProviderTest1() throws Exception {
    boolean pass = true;
    try {
      // Load my provider
      JsonProvider provider = JsonProvider.provider();
      String providerClass = provider.getClass().getName();
      logger.log(Logger.Level.INFO, "provider class=" + providerClass);
      if (providerClass.equals(MY_JSONPROVIDER_CLASS))
        logger.log(Logger.Level.INFO, "Current provider is my provider - expected.");
      else {
        logger.log(Logger.Level.ERROR, "Current provider is not my provider - unexpected.");
        pass = false;
        ServiceLoader<JsonProvider> loader = ServiceLoader.load(JsonProvider.class);
        Iterator<JsonProvider> it = loader.iterator();
        List<JsonProvider> providers = new ArrayList<>();
        while(it.hasNext()) {
            providers.add(it.next());
        }
        logger.log(Logger.Level.INFO, "Providers: "+providers);
      }
    } catch (Exception e) {
      throw new Exception("jsonProviderTest1 Failed: ", e);
    }
    if (!pass)
      throw new Exception("jsonProviderTest1 Failed");
  }

  /*
   * @testName: jsonProviderTest2
   * 
   * @assertion_ids: JSONP:JAVADOC:144;
   * 
   * @test_Strategy: Test call of SPI provider method with signature: o public
   * JsonGenerator createGenerator(Writer)
   */
  @Test
  @TargetVehicle("servlet")
  public void jsonProviderTest2() throws Exception {
    boolean pass = true;
    String expString = "public JsonGenerator createGenerator(Writer)";
    String expString2 = "public JsonGenerator writeStartArray()";
    try {
      logger.log(Logger.Level.INFO, "Calling SPI provider method: " + expString);
      JsonGenerator generator = Json.createGenerator(new StringWriter());
      String actString = MyJsonProvider.getCalls();
      logger.log(Logger.Level.INFO, "Verify SPI provider method was called: " + expString);
      pass = JSONP_Util.assertEquals(expString, actString);
      generator.writeStartArray();
      String actString2 = MyJsonGenerator.getCalls();
      logger.log(Logger.Level.INFO, "Verify SPI generator method was called: " + expString2);
      pass = JSONP_Util.assertEquals(expString2, actString2);
    } catch (Exception e) {
      throw new Exception("jsonProviderTest2 Failed: ", e);
    }
    if (!pass)
      throw new Exception("jsonProviderTest2 Failed");
  }

  /*
   * @testName: jsonProviderTest3
   * 
   * @assertion_ids: JSONP:JAVADOC:192;
   * 
   * @test_Strategy: Test call of SPI provider method with signature: o public
   * JsonGenerator createGenerator(OutputStream)
   */
  @Test
  @TargetVehicle("servlet")
  public void jsonProviderTest3() throws Exception {
    boolean pass = true;
    String expString = "public JsonGenerator createGenerator(OutputStream)";
    String expString2 = "public JsonGenerator writeStartObject()";
    try {
      logger.log(Logger.Level.INFO, "Calling SPI provider method: " + expString);
      JsonGenerator generator = Json
          .createGenerator(new ByteArrayOutputStream());
      String actString = MyJsonProvider.getCalls();
      logger.log(Logger.Level.INFO, "Verify SPI provider method was called: " + expString);
      pass = JSONP_Util.assertEquals(expString, actString);
      generator.writeStartObject();
      String actString2 = MyJsonGenerator.getCalls();
      logger.log(Logger.Level.INFO, "Verify SPI generator method was called: " + expString2);
      pass = JSONP_Util.assertEquals(expString2, actString2);
    } catch (Exception e) {
      throw new Exception("jsonProviderTest3 Failed: ", e);
    }
    if (!pass)
      throw new Exception("jsonProviderTest3 Failed");
  }

  /*
   * @testName: jsonProviderTest4
   * 
   * @assertion_ids: JSONP:JAVADOC:146;
   * 
   * @test_Strategy: Test call of SPI provider method with signature: o public
   * JsonParser createParser(Reader)
   */
  @Test
  @TargetVehicle("servlet")
  public void jsonProviderTest4() throws Exception {
    boolean pass = true;
    String expString = "public JsonParser createParser(Reader)";
    try {
      logger.log(Logger.Level.INFO, "Calling SPI provider method: " + expString);
      JsonParser parser = Json.createParser(new StringReader("{}"));
      String actString = MyJsonProvider.getCalls();
      logger.log(Logger.Level.INFO, "Verify SPI provider method was called: " + expString);
      pass = JSONP_Util.assertEquals(expString, actString);
    } catch (Exception e) {
      throw new Exception("jsonProviderTest4 Failed: ", e);
    }
    if (!pass)
      throw new Exception("jsonProviderTest4 Failed");
  }

  /*
   * @testName: jsonProviderTest5
   * 
   * @assertion_ids: JSONP:JAVADOC:196;
   * 
   * @test_Strategy: Test call of SPI provider method with signature: o public
   * JsonParser createParser(InputStream)
   */
  @Test
  @TargetVehicle("servlet")
  public void jsonProviderTest5() throws Exception {
    boolean pass = true;
    String expString = "public JsonParser createParser(InputStream)";
    try {
      logger.log(Logger.Level.INFO, "Calling SPI provider method: " + expString);
      JsonParser parser = Json
          .createParser(JSONP_Util.getInputStreamFromString("{}"));
      String actString = MyJsonProvider.getCalls();
      logger.log(Logger.Level.INFO, "Verify SPI provider method was called: " + expString);
      pass = JSONP_Util.assertEquals(expString, actString);
    } catch (Exception e) {
      throw new Exception("jsonProviderTest5 Failed: ", e);
    }
    if (!pass)
      throw new Exception("jsonProviderTest5 Failed");
  }

  /*
   * @testName: jsonProviderTest6
   * 
   * @assertion_ids: JSONP:JAVADOC:465;
   * 
   * @test_Strategy: Test call of SPI provider method with signature: o public
   * JsonParserFactory createParserFactory(Map<String, ?>)
   */
  @Test
  @TargetVehicle("servlet")
  public void jsonProviderTest6() throws Exception {
    boolean pass = true;
    String expString = "public JsonParserFactory createParserFactory(Map<String, ?>)";
    try {
      logger.log(Logger.Level.INFO, "Calling SPI provider method: " + expString);
      JsonParserFactory parserFactory = Json
          .createParserFactory(JSONP_Util.getEmptyConfig());
      String actString = MyJsonProvider.getCalls();
      logger.log(Logger.Level.INFO, "Verify SPI provider method was called: " + expString);
      pass = JSONP_Util.assertEquals(expString, actString);
    } catch (Exception e) {
      throw new Exception("jsonProviderTest6 Failed: ", e);
    }
    if (!pass)
      throw new Exception("jsonProviderTest6 Failed");
  }

  /*
   * @testName: jsonProviderTest7
   * 
   * @assertion_ids: JSONP:JAVADOC:426;
   * 
   * @test_Strategy: Test call of SPI provider method with signature: o public
   * JsonParserFactory createParserFactory(Map<String, ?>)
   */
  @Test
  @TargetVehicle("servlet")
  public void jsonProviderTest7() throws Exception {
    boolean pass = true;
    String expString = "public JsonParserFactory createParserFactory(Map<String, ?>)";
    try {
      logger.log(Logger.Level.INFO, "Calling SPI provider method: " + expString);
      JsonParserFactory parserFactory = Json
          .createParserFactory(new HashMap<String, Object>());
      String actString = MyJsonProvider.getCalls();
      logger.log(Logger.Level.INFO, "Verify SPI provider method was called: " + expString);
      pass = JSONP_Util.assertEquals(expString, actString);
    } catch (Exception e) {
      throw new Exception("jsonProviderTest7 Failed: ", e);
    }
    if (!pass)
      throw new Exception("jsonProviderTest7 Failed");
  }

  /*
   * @testName: jsonProviderTest8
   * 
   * @assertion_ids: JSONP:JAVADOC:425;
   * 
   * @test_Strategy: Test call of SPI provider method with signature: o public
   * JsonGeneratorFactory createGeneratorFactory(Map<String, ?>)
   */
  @Test
  @TargetVehicle("servlet")
  public void jsonProviderTest8() throws Exception {
    boolean pass = true;
    String expString = "public JsonGeneratorFactory createGeneratorFactory(Map<String, ?>)";
    try {
      logger.log(Logger.Level.INFO, "Calling SPI provider method: " + expString);
      JsonGeneratorFactory generatorFactory = Json
          .createGeneratorFactory(new HashMap<String, Object>());
      String actString = MyJsonProvider.getCalls();
      logger.log(Logger.Level.INFO, "Verify SPI provider method was called: " + expString);
      pass = JSONP_Util.assertEquals(expString, actString);
    } catch (Exception e) {
      throw new Exception("jsonProviderTest8 Failed: ", e);
    }
    if (!pass)
      throw new Exception("jsonProviderTest8 Failed");
  }

  /*
   * @testName: jsonProviderTest9
   * 
   * @assertion_ids: JSONP:JAVADOC:472;
   * 
   * @test_Strategy: Test call of SPI provider method with signature: o public
   * JsonWriterFactory createWriterFactory(Map<String, ?>)
   */
  @Test
  @TargetVehicle("servlet")
  public void jsonProviderTest9() throws Exception {
    boolean pass = true;
    String expString = "public JsonWriterFactory createWriterFactory(Map<String, ?>)";
    try {
      logger.log(Logger.Level.INFO, "Calling SPI provider method: " + expString);
      JsonWriterFactory factory = Json
          .createWriterFactory(JSONP_Util.getEmptyConfig());
      String actString = MyJsonProvider.getCalls();
      logger.log(Logger.Level.INFO, "Verify SPI provider method was called: " + expString);
      pass = JSONP_Util.assertEquals(expString, actString);
    } catch (Exception e) {
      throw new Exception("jsonProviderTest9 Failed: ", e);
    }
    if (!pass)
      throw new Exception("jsonProviderTest9 Failed");
  }

  /*
   * @testName: jsonProviderTest10
   * 
   * @assertion_ids: JSONP:JAVADOC:223;
   * 
   * @test_Strategy: Test call of SPI provider method with signature: o public
   * JsonParser createParser(InputStream) Tests the case where a JsonException
   * can be thrown. An InputStream of null will cause MyJsonProvider to throw
   * JsonException.
   */
  @Test
  @TargetVehicle("servlet")
  public void jsonProviderTest10() throws Exception {
    boolean pass = true;
    String expString = "public JsonParser createParser(InputStream)";
    try {
      logger.log(Logger.Level.INFO, "Calling SPI provider method: " + expString);
      InputStream in = null;
      JsonParser parser = Json.createParser(in);
      pass = false;
    } catch (JsonException e) {
      logger.log(Logger.Level.INFO, "Caught expected JsonException: " + e);
    } catch (Exception e) {
      throw new Exception("jsonProviderTest10 Failed: ", e);
    }
    if (!pass)
      throw new Exception("jsonProviderTest10 Failed");
  }

  /*
   * @testName: jsonProviderTest11
   * 
   * @assertion_ids: JSONP:JAVADOC:464;
   * 
   * @test_Strategy: Test call of SPI provider method with signature: o public
   * JsonArrayBuilder createArrayBuilder()
   */
  @Test
  @TargetVehicle("servlet")
  public void jsonProviderTest11() throws Exception {
    boolean pass = true;
    String expString = "public JsonArrayBuilder createArrayBuilder()";
    try {
      logger.log(Logger.Level.INFO, "Calling SPI provider method: " + expString);
      JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
      String actString = MyJsonProvider.getCalls();
      logger.log(Logger.Level.INFO, "Verify SPI provider method was called: " + expString);
      pass = JSONP_Util.assertEquals(expString, actString);
    } catch (Exception e) {
      throw new Exception("jsonProviderTest11 Failed: ", e);
    }
    if (!pass)
      throw new Exception("jsonProviderTest11 Failed");
  }

  /*
   * @testName: jsonProviderTest12
   * 
   * @assertion_ids: JSONP:JAVADOC:466;
   * 
   * @test_Strategy: Test call of SPI provider method with signature: o public
   * JsonObjectBuilder createObjectBuilder()
   */
  @Test
  @TargetVehicle("servlet")
  public void jsonProviderTest12() throws Exception {
    boolean pass = true;
    String expString = "public JsonObjectBuilder createObjectBuilder()";
    try {
      logger.log(Logger.Level.INFO, "Calling SPI provider method: " + expString);
      JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
      String actString = MyJsonProvider.getCalls();
      logger.log(Logger.Level.INFO, "Verify SPI provider method was called: " + expString);
      pass = JSONP_Util.assertEquals(expString, actString);
    } catch (Exception e) {
      throw new Exception("jsonProviderTest12 Failed: ", e);
    }
    if (!pass)
      throw new Exception("jsonProviderTest12 Failed");
  }

  /*
   * @testName: jsonProviderTest13
   * 
   * @assertion_ids: JSONP:JAVADOC:465;
   * 
   * @test_Strategy: Test call of SPI provider method with signature: o public
   * JsonBuilderFactory createBuilderFactory(Map<String, ?>)
   */
  @Test
  @TargetVehicle("servlet")
  public void jsonProviderTest13() throws Exception {
    boolean pass = true;
    String expString = "public JsonBuilderFactory createBuilderFactory(Map<String, ?>)";
    try {
      logger.log(Logger.Level.INFO, "Calling SPI provider method: " + expString);
      JsonBuilderFactory objectBuilder = Json
          .createBuilderFactory(JSONP_Util.getEmptyConfig());
      String actString = MyJsonProvider.getCalls();
      logger.log(Logger.Level.INFO, "Verify SPI provider method was called: " + expString);
      pass = JSONP_Util.assertEquals(expString, actString);
    } catch (Exception e) {
      throw new Exception("jsonProviderTest13 Failed: ", e);
    }
    if (!pass)
      throw new Exception("jsonProviderTest13 Failed");
  }

  /*
   * @testName: jsonProviderTest14
   * 
   * @assertion_ids: JSONP:JAVADOC:467;
   * 
   * @test_Strategy: Test call of SPI provider method with signature: o public
   * JsonReader createReader(Reader)
   */
  @Test
  @TargetVehicle("servlet")
  public void jsonProviderTest14() throws Exception {
    boolean pass = true;
    String expString = "public JsonReader createReader(Reader)";
    try {
      logger.log(Logger.Level.INFO, "Calling SPI provider method: " + expString);
      JsonReader reader = Json.createReader(new StringReader("{}"));
      String actString = MyJsonProvider.getCalls();
      logger.log(Logger.Level.INFO, "Verify SPI provider method was called: " + expString);
      pass = JSONP_Util.assertEquals(expString, actString);
    } catch (Exception e) {
      throw new Exception("jsonProviderTest14 Failed: ", e);
    }
    if (!pass)
      throw new Exception("jsonProviderTest14 Failed");
  }

  /*
   * @testName: jsonProviderTest15
   * 
   * @assertion_ids: JSONP:JAVADOC:468;
   * 
   * @test_Strategy: Test call of SPI provider method with signature: o public
   * JsonReader createReader(InputStream)
   */
  @Test
  @TargetVehicle("servlet")
  public void jsonProviderTest15() throws Exception {
    boolean pass = true;
    String expString = "public JsonReader createReader(InputStream)";
    try {
      logger.log(Logger.Level.INFO, "Calling SPI provider method: " + expString);
      JsonReader reader = Json
          .createReader(JSONP_Util.getInputStreamFromString("{}"));
      String actString = MyJsonProvider.getCalls();
      logger.log(Logger.Level.INFO, "Verify SPI provider method was called: " + expString);
      pass = JSONP_Util.assertEquals(expString, actString);
    } catch (Exception e) {
      throw new Exception("jsonProviderTest15 Failed: ", e);
    }
    if (!pass)
      throw new Exception("jsonProviderTest15 Failed");
  }

  /*
   * @testName: jsonProviderTest16
   * 
   * @assertion_ids: JSONP:JAVADOC:470;
   * 
   * @test_Strategy: Test call of SPI provider method with signature: o public
   * JsonWriter createWriter(Writer)
   */
  @Test
  @TargetVehicle("servlet")
  public void jsonProviderTest16() throws Exception {
    boolean pass = true;
    String expString = "public JsonWriter createWriter(Writer)";
    try {
      logger.log(Logger.Level.INFO, "Calling SPI provider method: " + expString);
      JsonWriter writer = Json.createWriter(new StringWriter());
      String actString = MyJsonProvider.getCalls();
      logger.log(Logger.Level.INFO, "Verify SPI provider method was called: " + expString);
      pass = JSONP_Util.assertEquals(expString, actString);
    } catch (Exception e) {
      throw new Exception("jsonProviderTest16 Failed: ", e);
    }
    if (!pass)
      throw new Exception("jsonProviderTest16 Failed");
  }

  /*
   * @testName: jsonProviderTest17
   * 
   * @assertion_ids: JSONP:JAVADOC:471;
   * 
   * @test_Strategy: Test call of SPI provider method with signature: o public
   * JsonWriter createWriter(OutputStream)
   */
  @Test
  @TargetVehicle("servlet")
  public void jsonProviderTest17() throws Exception {
    boolean pass = true;
    String expString = "public JsonWriter createWriter(OutputStream)";
    try {
      logger.log(Logger.Level.INFO, "Calling SPI provider method: " + expString);
      JsonWriter writer = Json.createWriter(new ByteArrayOutputStream());
      String actString = MyJsonProvider.getCalls();
      logger.log(Logger.Level.INFO, "Verify SPI provider method was called: " + expString);
      pass = JSONP_Util.assertEquals(expString, actString);
    } catch (Exception e) {
      throw new Exception("jsonProviderTest17 Failed: ", e);
    }
    if (!pass)
      throw new Exception("jsonProviderTest17 Failed");
  }

  /*
   * @testName: jsonProviderTest18
   * 
   * @assertion_ids: JSONP:JAVADOC:469;
   * 
   * @test_Strategy: Test call of SPI provider method with signature: o public
   * JsonReaderFactory createReaderFactory(Map<String, ?>)
   */
  @Test
  @TargetVehicle("servlet")
  public void jsonProviderTest18() throws Exception {
    boolean pass = true;
    String expString = "public JsonReaderFactory createReaderFactory(Map<String, ?>)";
    try {
      logger.log(Logger.Level.INFO, "Calling SPI provider method: " + expString);
      JsonReaderFactory factory = Json
          .createReaderFactory(JSONP_Util.getEmptyConfig());
      String actString = MyJsonProvider.getCalls();
      logger.log(Logger.Level.INFO, "Verify SPI provider method was called: " + expString);
      pass = JSONP_Util.assertEquals(expString, actString);
    } catch (Exception e) {
      throw new Exception("jsonProviderTest18 Failed: ", e);
    }
    if (!pass)
      throw new Exception("jsonProviderTest18 Failed");
  }
}
