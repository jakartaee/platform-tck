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
package com.sun.ts.tests.jsonb.pluggability.jsonbprovidertests;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.io.InputStream;
import java.io.IOException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.net.URL;

import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.spi.JsonbProvider;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Filters;
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

import com.sun.ts.tests.jsonb.provider.MyJsonbBuilder;
import com.sun.ts.tests.jsonb.provider.MyJsonbProvider;
// import com.sun.javatest.Status;
// import com.sun.ts.lib.harness.ServiceEETest;
import java.lang.System.Logger;

@ExtendWith(ArquillianExtension.class)
public class ClientIT { //extends ServiceEETest {

    private static final Logger logger = System.getLogger(ClientIT.class.getName());

    private static String packagePath = ClientIT.class.getPackageName().replace(".", "/");
    
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
    
    //   EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "jsonbprovidertests_servlet_vehicle.ear");
      WebArchive war = ShrinkWrap.create(WebArchive.class, "jsonbprovidertests_servlet_vehicle_web.war");
      war.addClass(ClientIT.class);
      war.addClass(com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class);
      war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
      war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
      war.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);

    //   InputStream inStream = ClientIT.class.getClassLoader().getResourceAsStream(packagePath + "/servlet_vehicle_web.xml");
    //   String webXml = editWebXmlString(inStream, "jsonbprovidertests_servlet_vehicle");
    //   war.setWebXML(new StringAsset(webXml));
      war.setWebXML(ClientIT.class.getClassLoader().getResource(packagePath+"/servlet_vehicle_web.xml"));

    //   war.addAsServiceProvider(JsonbProvider.class, com.sun.ts.tests.jsonb.provider.MyJsonbProvider.class);
    //   war.addAsResource(new UrlAsset(ClientIT.class.getClassLoader().getResource(packagePath+"/jakarta.json.bind.spi.JsonbProvider")), "META-INF/services/jakarta.json.bind.spi.JsonbProvider");
      JavaArchive jar = prepackage();
      war.addAsLibrary(jar);

    //   ear.addAsModule(war);
    //   ear.addAsLibrary(jar);
      return war;

    }
  
    public static JavaArchive prepackage() throws IOException {
        JavaArchive jarArchive = ShrinkWrap.create(JavaArchive.class, "jsonb_alternate_provider.jar")
            .addClass(MyJsonbBuilder.class)
            .addClass(MyJsonbProvider.class)
            .addAsResource(new UrlAsset(ClientIT.class.getClassLoader().getResource(packagePath+"/jakarta.json.bind.spi.JsonbProvider")), "META-INF/services/jakarta.json.bind.spi.JsonbProvider");
  
        return jarArchive;
    }

    public static String toString(InputStream inStream) throws IOException{
        try (BufferedReader bufReader = new BufferedReader(new InputStreamReader(inStream, StandardCharsets.UTF_8))) {
          return bufReader.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }
    
    private static final String MY_JSONBROVIDER_CLASS = "com.sun.ts.tests.jsonb.provider.MyJsonbProvider";
    private static final String MY_JSONBBUILDER_CLASS = "com.sun.ts.tests.jsonb.provider.MyJsonbBuilder";

    // public static void main(String[] args) {
    //     Client theTests = new Client();
    //     Status s = theTests.run(args, System.out, System.err);
    //     s.exit();
    // }


    /* Test setup */

    /*
     * @class.setup_props:
     */
    public void setup(String[] args, Properties p) throws Exception {
        logger.log(Logger.Level.INFO, "setup ok");
    }

    public void cleanup() throws Exception {
        logger.log(Logger.Level.INFO, "cleanup ok");
    }

    /* Tests */

    /*
     * @testName: jsonbProviderTest1
     *
     * @test_Strategy: Test call of SPI provider method with signature: o public
     * static JsonbProvider provider()
     */
    @Test
    public void jsonbProviderTest1() throws Exception {
        try {
            // Load any provider
            JsonbProvider provider = JsonbProvider.provider();
            String providerClass = provider.getClass().getName();
            logger.log(Logger.Level.INFO, "provider class=" + providerClass);
        } catch (Exception e) {
            throw new Exception("jsonbProviderTest1 Failed: ", e);
        }
    }

    /*
     * @testName: jsonbProviderTest2
     *
     * @test_Strategy: Test call of SPI provider method with signature: o public
     * static JsonbProvider provider(String provider)
     */
    @Test
    public void jsonbProviderTest2() throws Exception {
        boolean pass = true;
        try {
            // Load my provider
            JsonbProvider provider = JsonbProvider.provider(MY_JSONBROVIDER_CLASS);
            String providerClass = provider.getClass().getName();
            logger.log(Logger.Level.INFO, "provider class=" + providerClass);
            if (providerClass.equals(MY_JSONBROVIDER_CLASS))
            logger.log(Logger.Level.INFO, "Current provider is my provider - expected.");
            else {
                logger.log(Logger.Level.ERROR, "Current provider is not my provider - unexpected.");
                pass = false;
                ServiceLoader<JsonbProvider> loader = ServiceLoader.load(JsonbProvider.class);
                Iterator<JsonbProvider> it = loader.iterator();
                List<JsonbProvider> providers = new ArrayList<>();
                while(it.hasNext()) {
                    providers.add(it.next());
                }
                logger.log(Logger.Level.INFO, "Providers: "+providers);
            }
        } catch (Exception e) {
            throw new Exception("jsonbProviderTest2 Failed: ", e);
        }
        if (!pass)
            throw new Exception("jsonbProviderTest2 Failed");
    }

    /*
     * @testName: jsonbProviderTest3
     *
     * @test_Strategy: Test call of provider method with signature: o public JsonbBuilder create()
     */
    @Test
    public void jsonbProviderTest3() throws Exception {
        try {
            // Load my provider
            JsonbBuilder builder = JsonbProvider.provider(MY_JSONBROVIDER_CLASS).create();
            String providerClass = builder.getClass().getName();
            logger.log(Logger.Level.INFO, "jsonb builder class=" + providerClass);
            if (providerClass.equals(MY_JSONBBUILDER_CLASS))
            logger.log(Logger.Level.INFO, "Current jsonb builder is my builder - expected.");
            else {
                logger.log(Logger.Level.ERROR, "Current jsonb builder is not my builder - unexpected.");
                throw new Exception("jsonbProviderTest3 Failed");
            }
        } catch (Exception e) {
            throw new Exception("jsonbProviderTest3 Failed: ", e);
        }
    }

}