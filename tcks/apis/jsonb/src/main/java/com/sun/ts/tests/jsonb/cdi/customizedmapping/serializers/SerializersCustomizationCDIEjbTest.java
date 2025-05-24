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

import com.sun.ts.lib.harness.Fault;
import com.sun.ts.lib.harness.SetupException;
import com.sun.ts.lib.harness.Status;

import java.lang.System.Logger;
import java.net.URL;

import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.tests.common.base.ServiceEETest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
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

/**
 * @test
 * @sources SerializersCustomizationTest.java
 * @executeClass com.sun.ts.tests.jsonb.customizedmapping.serializers.SerializersCustomizationTest
 **/
@Tag("tck-appclient")
@Tag("jsonb")
@Tag("platform")
@ExtendWith(ArquillianExtension.class)
public class SerializersCustomizationCDIEjbTest extends SerializersCustomizationCDITest {

    static final String VEHICLE_ARCHIVE = "jsonb_cdi_customizedmapping_serializers_servlet_vehicle";

    public static void main(String[] args) {
        SerializersCustomizationCDIEjbTest t = new SerializersCustomizationCDIEjbTest();
        Status s = t.run(args, System.out, System.err);
        s.exit();
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

        JavaArchive jsonb_cdi_customizedmapping_serializers_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class,
                "jsonb_cdi_customizedmapping_serializers_ejb_vehicle_client.jar");
        jsonb_cdi_customizedmapping_serializers_ejb_vehicle_client.addClasses(com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class, com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class, com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRunner.class, EETest.class,
                Fault.class, SetupException.class,
                ServiceEETest.class,
                com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalSerializer.class,
                com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalListSerializer.class,
                com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalListDeserializerInjected.class,
                com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalDeserializerInjected.class,
                com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalDeserializer.class,
                com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalBuilder.class,
                com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.AnimalShelterWithInjectedSerializer.class,
                com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.Animal.class,
                com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.Cat.class,
                com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.Dog.class, SerializersCustomizationCDIEjbTest.class,
                SerializersCustomizationCDITest.class);

        URL resURL = SerializersCustomizationCDIEjbTest.class.getClassLoader().getResource(packagePath + "/ejb_vehicle_client.xml");
        if (resURL != null) {
            jsonb_cdi_customizedmapping_serializers_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
        }
        jsonb_cdi_customizedmapping_serializers_ejb_vehicle_client
                .addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"), "MANIFEST.MF");

        resURL = SerializersCustomizationCDIEjbTest.class.getClassLoader()
                .getResource(packagePath + "/ejb_vehicle_client.jar.sun-application-client.xml");
        if (resURL != null) {
            jsonb_cdi_customizedmapping_serializers_ejb_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
        }

        archiveProcessor.processClientArchive(jsonb_cdi_customizedmapping_serializers_ejb_vehicle_client,
                SerializersCustomizationCDIEjbTest.class, resURL);

        JavaArchive jsonb_cdi_customizedmapping_serializers_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class,
                "jsonb_cdi_customizedmapping_serializers_ejb_vehicle_ejb.jar");
        jsonb_cdi_customizedmapping_serializers_ejb_vehicle_ejb.addClasses(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class, com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class, com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class,
                EETest.class, Fault.class,
                SetupException.class, ServiceEETest.class,
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
                SerializersCustomizationCDIEjbTest.class, SerializersCustomizationCDITest.class);

        // The ejb-jar.xml descriptor
        URL ejbResURL = SerializersCustomizationCDIEjbTest.class.getClassLoader().getResource(packagePath + "/ejb_vehicle_ejb.xml");
        if (ejbResURL != null) {
            jsonb_cdi_customizedmapping_serializers_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
        }
        URL warResURL = SerializersCustomizationCDIEjbTest.class.getClassLoader().getResource(packagePath + "/beans.xml");
        if (warResURL != null) {
            jsonb_cdi_customizedmapping_serializers_ejb_vehicle_ejb.addAsManifestResource(warResURL, "beans.xml");
        }

        // The sun-ejb-jar.xml file need to be added or should this be in in the vendor Arquillian extension?
        ejbResURL = SerializersCustomizationCDIEjbTest.class.getClassLoader()
                .getResource(packagePath + "/ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
        if (ejbResURL != null) {
            jsonb_cdi_customizedmapping_serializers_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
        }
        archiveProcessor.processEjbArchive(jsonb_cdi_customizedmapping_serializers_ejb_vehicle_ejb,
                SerializersCustomizationCDIEjbTest.class, ejbResURL);

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
     * @test_Strategy: Assert that CDI injection is supported in serializers and deserializers
     */
    @Override
    @Test
    @TargetVehicle("ejb")
    public void testCDISupport() throws Exception {
        super.testCDISupport();
    }

}
