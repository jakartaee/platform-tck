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

import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.lib.harness.Fault;
import com.sun.ts.tests.common.base.ServiceEETest;
import com.sun.ts.lib.harness.SetupException;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.tests.common.vehicle.EmptyVehicleRunner;
import com.sun.ts.tests.common.vehicle.VehicleClient;
import com.sun.ts.tests.common.vehicle.VehicleRunnable;
import com.sun.ts.tests.common.vehicle.VehicleRunnerFactory;
import com.sun.ts.tests.common.vehicle.ejb.EJBVehicle;
import com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote;
import com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRunner;
import com.sun.ts.tests.jsonb.cdi.customizedmapping.adapters.model.Animal;
import com.sun.ts.tests.jsonb.cdi.customizedmapping.adapters.model.AnimalShelterInjectedAdapter;
import com.sun.ts.tests.jsonb.cdi.customizedmapping.adapters.model.Cat;
import com.sun.ts.tests.jsonb.cdi.customizedmapping.adapters.model.Dog;
import com.sun.ts.tests.jsonb.cdi.customizedmapping.adapters.model.adapter.AnimalIdentifier;
import com.sun.ts.tests.jsonb.cdi.customizedmapping.adapters.model.adapter.AnimalJson;
import com.sun.ts.tests.jsonb.cdi.customizedmapping.adapters.model.adapter.InjectedAdapter;
import com.sun.ts.tests.jsonb.cdi.customizedmapping.adapters.model.adapter.InjectedListAdapter;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

import java.lang.System.Logger;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

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
@ExtendWith(ArquillianExtension.class)
public class AdaptersCustomizationCDIEjbTest extends AdaptersCustomizationCDITest {

    static final String VEHICLE_ARCHIVE = "jsonb_cdi_customizedmapping_adapters_ejb_vehicle";

    private final Jsonb jsonb = JsonbBuilder.create();

    public static void main(String[] args) {
        AdaptersCustomizationCDIEjbTest t = new AdaptersCustomizationCDIEjbTest();
        Status s = t.run(args, System.out, System.err);
        s.exit();
    }

    private static final Logger logger = System.getLogger(AdaptersCustomizationCDIEjbTest.class.getName());

    private static String packagePath = AdaptersCustomizationCDIEjbTest.class.getPackageName().replace(".", "/");

    @TargetsContainer("tck-appclient")
    @OverProtocol("appclient")
    @Deployment(name = VEHICLE_ARCHIVE, order = 2)
    public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {

        JavaArchive jsonb_cdi_customizedmapping_adapters_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class,
                "jsonb_cdi_customizedmapping_adapters_ejb_vehicle_client.jar");
        jsonb_cdi_customizedmapping_adapters_ejb_vehicle_client.addClasses(
                EmptyVehicleRunner.class,
                VehicleRunnerFactory.class,
                VehicleRunnable.class,
                VehicleClient.class,
                EJBVehicleRemote.class,
                EJBVehicleRunner.class,
                EETest.class,
                Fault.class,
                SetupException.class,
                ServiceEETest.class,
                Animal.class,
                Cat.class,
                Dog.class,
                AnimalShelterInjectedAdapter.class,
                AnimalIdentifier.class,
                AnimalJson.TYPE.class,
                AnimalJson.class,
                InjectedAdapter.class,
                InjectedListAdapter.class,
                AdaptersCustomizationCDIEjbTest.class,
                AdaptersCustomizationCDITest.class);

        URL resURL = AdaptersCustomizationCDIEjbTest.class.getClassLoader().getResource(packagePath + "/ejb_vehicle_client.xml");
        if (resURL != null) {
            jsonb_cdi_customizedmapping_adapters_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
        }
        jsonb_cdi_customizedmapping_adapters_ejb_vehicle_client
                .addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"), "MANIFEST.MF");

        resURL = AdaptersCustomizationCDIEjbTest.class
                                                .getClassLoader()
                                                .getResource(packagePath + "/ejb_vehicle_client.jar.sun-application-client.xml");

        if (resURL != null) {
            jsonb_cdi_customizedmapping_adapters_ejb_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
        }
        archiveProcessor.processClientArchive(
            jsonb_cdi_customizedmapping_adapters_ejb_vehicle_client,
            AdaptersCustomizationCDIEjbTest.class,
            resURL);

        JavaArchive jsonb_cdi_customizedmapping_adapters_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class,
                "jsonb_cdi_customizedmapping_adapters_ejb_vehicle_ejb.jar");
        jsonb_cdi_customizedmapping_adapters_ejb_vehicle_ejb.addClasses(VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class, VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class, EJBVehicle.class,
                EETest.class, Fault.class,
                SetupException.class, ServiceEETest.class,
                AnimalIdentifier.class,
                AnimalJson.TYPE.class,
                AnimalJson.class,
                InjectedAdapter.class,
                InjectedListAdapter.class,
                Animal.class,
                AnimalShelterInjectedAdapter.class,
                Cat.class,
                Dog.class, AdaptersCustomizationCDIEjbTest.class,
                AdaptersCustomizationCDITest.class);

        // The ejb-jar.xml descriptor
        URL ejbResURL = AdaptersCustomizationCDIEjbTest.class.getClassLoader().getResource(packagePath + "/ejb_vehicle_ejb.xml");
        if (ejbResURL != null) {
            jsonb_cdi_customizedmapping_adapters_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
        }
        ejbResURL = AdaptersCustomizationCDIEjbTest.class
                                                   .getClassLoader()
                                                   .getResource(packagePath + "/ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
        if (ejbResURL != null) {
            jsonb_cdi_customizedmapping_adapters_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
        }

        URL warResURL = AdaptersCustomizationCDIEjbTest.class.getClassLoader().getResource(packagePath + "/beans.xml");
        if (warResURL != null) {
            jsonb_cdi_customizedmapping_adapters_ejb_vehicle_ejb.addAsManifestResource(warResURL, "beans.xml");
        }
        archiveProcessor.processEjbArchive(jsonb_cdi_customizedmapping_adapters_ejb_vehicle_ejb, AdaptersCustomizationCDIEjbTest.class,
                ejbResURL);

        EnterpriseArchive jsonb_cdi_customizedmapping_adapters_ejb_vehicle =
            ShrinkWrap.create(EnterpriseArchive.class, "jsonb_cdi_customizedmapping_adapters_ejb_vehicle.ear");

        jsonb_cdi_customizedmapping_adapters_ejb_vehicle.addAsModule(jsonb_cdi_customizedmapping_adapters_ejb_vehicle_client);
        jsonb_cdi_customizedmapping_adapters_ejb_vehicle.addAsModule(jsonb_cdi_customizedmapping_adapters_ejb_vehicle_ejb);

        return jsonb_cdi_customizedmapping_adapters_ejb_vehicle;

    }

    /*
     * @testName: testCDISupport
     *
     * @assertion_ids: JSONB:SPEC:JSB-4.7.1-3
     *
     * @test_Strategy: Assert that CDI injection is supported in adapters
     */
    @Override
    @Test
    @TargetVehicle("ejb")
    public void testCDISupport() throws Exception {
        super.testCDISupport();
    }
}
