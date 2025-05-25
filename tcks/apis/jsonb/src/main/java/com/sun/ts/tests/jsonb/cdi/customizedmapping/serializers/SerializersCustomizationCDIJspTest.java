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
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;

import tck.arquillian.protocol.common.TargetVehicle;

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
public class SerializersCustomizationCDIJspTest extends SerializersCustomizationCDITest {

    static final String VEHICLE_ARCHIVE = "jsonb_cdi_customizedmapping_serializers_jsp_vehicle";

    public static void main(String[] args) {
        SerializersCustomizationCDIJspTest t = new SerializersCustomizationCDIJspTest();
        Status s = t.run(args, System.out, System.err);
        s.exit();
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
    public static WebArchive createServletDeployment() throws Exception {

        WebArchive jsonb_cdi_customizedmapping_serializers_jsp_vehicle = ShrinkWrap.create(WebArchive.class,
                "jsonb_cdi_customizedmapping_serializers_jsp_vehicle_web.war");
        jsonb_cdi_customizedmapping_serializers_jsp_vehicle.addClasses(SerializersCustomizationCDIJspTest.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class, com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class, EETest.class,
                SetupException.class, Fault.class,
                ServiceEETest.class,
                com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalBuilder.class,
                com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalBuilder.TYPE.class,
                com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalDeserializer.class,
                com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalDeserializerInjected.class,
                com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalListDeserializerInjected.class,
                com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalListSerializer.class,
                com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.serializer.AnimalSerializer.class,
                com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.Animal.class,
                com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.AnimalShelterWithInjectedSerializer.class,
                com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.Cat.class,
                com.sun.ts.tests.jsonb.cdi.customizedmapping.serializers.model.Dog.class, SerializersCustomizationCDITest.class);

        jsonb_cdi_customizedmapping_serializers_jsp_vehicle
                .setWebXML(SerializersCustomizationCDIJspTest.class.getClassLoader().getResource(packagePath + "/jsp_vehicle_web.xml"));
        URL warResURL = SerializersCustomizationCDIJspTest.class.getClassLoader().getResource(packagePath + "/beans.xml");
        if (warResURL != null) {
            jsonb_cdi_customizedmapping_serializers_jsp_vehicle.addAsWebResource(warResURL, "/WEB-INF/beans.xml");
        }

        // Web content
        URL resURL = SerializersCustomizationCDIJspTest.class.getResource("/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
        jsonb_cdi_customizedmapping_serializers_jsp_vehicle.addAsWebResource(resURL, "/jsp_vehicle.jsp");
        resURL = SerializersCustomizationCDIJspTest.class.getResource("/vehicle/jsp/contentRoot/client.html");
        jsonb_cdi_customizedmapping_serializers_jsp_vehicle.addAsWebResource(resURL, "/client.html");

        return jsonb_cdi_customizedmapping_serializers_jsp_vehicle;
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
    @TargetVehicle("jsp")
    public void testCDISupport() throws Exception {
        super.testCDISupport();
    }
}
