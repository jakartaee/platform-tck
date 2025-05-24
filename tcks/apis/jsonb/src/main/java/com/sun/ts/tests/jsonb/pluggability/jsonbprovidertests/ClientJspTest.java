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

import com.sun.ts.lib.harness.Fault;
import com.sun.ts.tests.common.base.ServiceEETest;
import com.sun.ts.lib.harness.SetupException;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.tests.jsonb.provider.MyJsonbBuilder;
import com.sun.ts.tests.jsonb.provider.MyJsonbProvider;

import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.spi.JsonbProvider;

import java.io.File;
import java.lang.System.Logger;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.ServiceLoader;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.UrlAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;

import tck.arquillian.protocol.common.TargetVehicle;

@Tag("tck-javatest")
@Tag("jsonb")
@Tag("platform")
@Tag("web")
@ExtendWith(ArquillianExtension.class)
public class ClientJspTest extends ServiceEETest {

    private static final Logger logger = System.getLogger(ClientJspTest.class.getName());

    static final String VEHICLE_ARCHIVE = "jsonbprovidertests_jsp_vehicle";

    private static String packagePath = ClientJspTest.class.getPackageName().replace(".", "/");

    private static final String providerPackagePath = MyJsonbProvider.class.getPackageName().replace(".", "/");

    private boolean providerJarDeployed = false;

    public static final String TEMP_DIR = System.getProperty("java.io.tmpdir", "/tmp");

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
    public static WebArchive createJspDeployment() throws Exception {
        JavaArchive jarArchive = ShrinkWrap.create(JavaArchive.class, "jsonb_alternate_provider.jar").addClass(MyJsonbBuilder.class)
                .addClass(MyJsonbProvider.class).addAsResource(
                        new UrlAsset(MyJsonbProvider.class.getClassLoader()
                                .getResource(providerPackagePath + "/META-INF/services/jakarta.json.bind.spi.JsonbProvider")),
                        "META-INF/services/jakarta.json.bind.spi.JsonbProvider");

        WebArchive warArchive = ShrinkWrap.create(WebArchive.class, "jsonbprovidertests_jsp_vehicle_web.war");
        warArchive.addClass(ClientJspTest.class).addClass(com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class)
                .addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class)
                .addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class)
                .addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class).addClass(EETest.class)
                .addClass(Fault.class).addClass(SetupException.class)
                .addClass(ServiceEETest.class)
                .setWebXML(ClientJspTest.class.getClassLoader().getResource(packagePath + "/jsp_vehicle_web.xml"));

        warArchive.addAsLibrary(jarArchive);

        // Web content
        URL resURL = ClientJspTest.class.getResource("/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
        warArchive.addAsWebResource(resURL, "/jsp_vehicle.jsp");
        resURL = ClientJspTest.class.getResource("/vehicle/jsp/contentRoot/client.html");
        warArchive.addAsWebResource(resURL, "/client.html");

        return warArchive;
        // EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "jsonbprovidertests_jsp_vehicle.ear");
        // ear.addAsModule(warArchive);
        // ear.addAsLibrary(jarArchive);
        // return ear;

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

        JavaArchive jarArchive = ShrinkWrap.create(JavaArchive.class, "jsonb_alternate_provider.jar").addClass(MyJsonbBuilder.class)
                .addClass(MyJsonbProvider.class).addAsResource(
                        new UrlAsset(MyJsonbProvider.class.getClassLoader()
                                .getResource(providerPackagePath + "/META-INF/services/jakarta.json.bind.spi.JsonbProvider")),
                        "META-INF/services/jakarta.json.bind.spi.JsonbProvider");

        jarArchive.as(ZipExporter.class).exportTo(new File(TEMP_DIR + File.separator + "jsonb_alternate_provider.jar"), true);

        ClassLoader currentThreadClassLoader = Thread.currentThread().getContextClassLoader();
        URLClassLoader urlClassLoader = new URLClassLoader(
                new URL[] { new File(TEMP_DIR + File.separator + "jsonb_alternate_provider.jar").toURL() }, currentThreadClassLoader);
        Thread.currentThread().setContextClassLoader(urlClassLoader);

        providerJarDeployed = true;

    }

    private static final String MY_JSONBROVIDER_CLASS = "com.sun.ts.tests.jsonb.provider.MyJsonbProvider";
    private static final String MY_JSONBBUILDER_CLASS = "com.sun.ts.tests.jsonb.provider.MyJsonbBuilder";

    public static void main(String[] args) {
        ClientJspTest theTests = new ClientJspTest();
        Status s = theTests.run(args, System.out, System.err);
        s.exit();
    }

    /*
     * @class.setup_props: This is needed by the vehicle base classes
     */
    public void setup(String[] args, Properties p) throws Exception {

    }

    /* Test setup */

    /*
     * @class.setup_props:
     */
    @BeforeEach
    public void setup() throws Exception {
        createProviderJar();
        logger.log(Logger.Level.INFO, "setup ok");
    }

    @AfterEach
    public void cleanup() throws Exception {
        removeProviderJarFromCP();
        logger.log(Logger.Level.INFO, "cleanup ok");
    }

    /* Tests */

    /*
     * @testName: jsonbProviderTest1
     *
     * @test_Strategy: Test call of SPI provider method with signature: o public static JsonbProvider provider()
     */
    @Test
    @TargetVehicle("jsp")
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
     * @test_Strategy: Test call of SPI provider method with signature: o public static JsonbProvider provider(String
     * provider)
     */
    @Test
    @TargetVehicle("jsp")
    public void jsonbProviderTest2() throws Exception {
        boolean pass = true;
        try {
            // Load my provider
            JsonbProvider provider = JsonbProvider.provider(MY_JSONBROVIDER_CLASS);
            String providerClass = provider.getClass().getName();
            logger.log(Logger.Level.INFO, "provider class=" + providerClass);
            if (providerClass.equals(MY_JSONBROVIDER_CLASS)) {
                logger.log(Logger.Level.INFO, "Current provider is my provider - expected.");
            } else {
                logger.log(Logger.Level.ERROR, "Current provider is not my provider - unexpected.");
                pass = false;
                ServiceLoader<JsonbProvider> loader = ServiceLoader.load(JsonbProvider.class);
                Iterator<JsonbProvider> it = loader.iterator();
                List<JsonbProvider> providers = new ArrayList<>();
                while (it.hasNext()) {
                    providers.add(it.next());
                }
                logger.log(Logger.Level.INFO, "Providers: " + providers);
            }
        } catch (Exception e) {
            throw new Exception("jsonbProviderTest2 Failed: ", e);
        }
        if (!pass) {
            throw new Exception("jsonbProviderTest2 Failed");
        }
    }

    /*
     * @testName: jsonbProviderTest3
     *
     * @test_Strategy: Test call of provider method with signature: o public JsonbBuilder create()
     */
    @Test
    @TargetVehicle("jsp")
    public void jsonbProviderTest3() throws Exception {
        try {
            // Load my provider
            JsonbBuilder builder = JsonbProvider.provider(MY_JSONBROVIDER_CLASS).create();
            String providerClass = builder.getClass().getName();
            logger.log(Logger.Level.INFO, "jsonb builder class=" + providerClass);
            if (providerClass.equals(MY_JSONBBUILDER_CLASS)) {
                logger.log(Logger.Level.INFO, "Current jsonb builder is my builder - expected.");
            } else {
                logger.log(Logger.Level.ERROR, "Current jsonb builder is not my builder - unexpected.");
                throw new Exception("jsonbProviderTest3 Failed");
            }
        } catch (Exception e) {
            throw new Exception("jsonbProviderTest3 Failed: ", e);
        }
    }

}