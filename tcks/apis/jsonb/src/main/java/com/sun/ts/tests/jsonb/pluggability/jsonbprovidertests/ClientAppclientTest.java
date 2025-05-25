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
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.asset.UrlAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
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

@Tag("tck-appclient")
@Tag("jsonb")
@Tag("platform")
@ExtendWith(ArquillianExtension.class)
public class ClientAppclientTest extends Client {

    static final String VEHICLE_ARCHIVE = "jsonprovidertests_appclient_vehicle";

    private static String packagePath = ClientAppclientTest.class.getPackageName().replace(".", "/");

    private static final String providerPackagePath = MyJsonbProvider.class.getPackageName().replace(".", "/");


    public static final String TEMP_DIR = System.getProperty("java.io.tmpdir", "/tmp");

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
    @Deployment(name = VEHICLE_ARCHIVE, testable = true)
    public static EnterpriseArchive createAppclientDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) throws Exception {

        JavaArchive jsonb_alternate_provider_jar = ShrinkWrap.create(JavaArchive.class, "jsonb_alternate_provider.jar")
                .addClasses(com.sun.ts.tests.jsonb.provider.MyJsonbBuilder.class, com.sun.ts.tests.jsonb.provider.MyJsonbProvider.class)
                .addAsResource(
                        new UrlAsset(com.sun.ts.tests.jsonb.provider.MyJsonbProvider.class.getClassLoader()
                                .getResource(providerPackagePath + "/META-INF/services/jakarta.json.bind.spi.JsonbProvider")),
                        "META-INF/services/jakarta.json.bind.spi.JsonbProvider");

        JavaArchive jsonbprovidertests_appclient_vehicle_client = ShrinkWrap.create(JavaArchive.class,
                "jsonbprovidertests_appclient_vehicle_client.jar");
        jsonbprovidertests_appclient_vehicle_client.addClasses(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class, com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class, EETest.class,
                ServiceEETest.class, Client.class);

        URL resURL = ClientAppclientTest.class.getClassLoader().getResource(packagePath + "/appclient_vehicle_client.xml");
        if (resURL != null) {
            jsonbprovidertests_appclient_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
        }
        jsonbprovidertests_appclient_vehicle_client
                .addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");

        EnterpriseArchive jsonbprovidertests_appclient_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class,
                "jsonbprovidertests_appclient_vehicle.ear");
        jsonbprovidertests_appclient_vehicle_ear.addAsModule(jsonbprovidertests_appclient_vehicle_client);
        jsonbprovidertests_appclient_vehicle_ear.addAsLibrary(jsonb_alternate_provider_jar);
        jsonbprovidertests_appclient_vehicle_ear.addAsModule(jsonb_alternate_provider_jar);

        return jsonbprovidertests_appclient_vehicle_ear;

    }

    @Override
    @Test
    @TargetVehicle("appclient")
    public void jsonbProviderTest1() throws Exception {
        super.jsonbProviderTest1();
    }

    @Override
    @Test
    @TargetVehicle("appclient")
    public void jsonbProviderTest2() throws Exception {
        super.jsonbProviderTest2();
    }

    @Override
    @Test
    @TargetVehicle("appclient")
    public void jsonbProviderTest3() throws Exception {
        super.jsonbProviderTest3();
    }
}