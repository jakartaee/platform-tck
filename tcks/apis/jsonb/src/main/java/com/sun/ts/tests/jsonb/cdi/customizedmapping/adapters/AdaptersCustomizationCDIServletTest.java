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
 * @sources AdaptersCustomizationTest.java
 * @executeClass com.sun.ts.tests.jsonb.customizedmapping.adapters.AdaptersCustomizationTest
 **/
/*
 * @class.setup_props: webServerHost; webServerPort; ts_home;
 */

@Tag("tck-javatest")
@Tag("jsonb")
@Tag("platform")
@Tag("web")
@ExtendWith(ArquillianExtension.class)
public class AdaptersCustomizationCDIServletTest extends AdaptersCustomizationCDITest {

    static final String VEHICLE_ARCHIVE = "jsonb_cdi_customizedmapping_adapters_servlet_vehicle";

    public static void main(String[] args) {
        AdaptersCustomizationCDIServletTest t = new AdaptersCustomizationCDIServletTest();
        Status s = t.run(args, System.out, System.err);
        s.exit();
    }

    private static final Logger logger = System.getLogger(AdaptersCustomizationCDIServletTest.class.getName());

    private static String packagePath = AdaptersCustomizationCDIServletTest.class.getPackageName().replace(".", "/");

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

        // EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class,
        // "jsonb_cdi_customizedmapping_adapters_servlet_vehicle.ear");
        WebArchive war = ShrinkWrap.create(WebArchive.class, "jsonb_cdi_customizedmapping_adapters_servlet_vehicle_web.war");
        war.addClass(AdaptersCustomizationCDIServletTest.class).addClass(com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class)
                .addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class)
                .addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class)
                .addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class).addClass(EETest.class)
                .addClass(SetupException.class).addClass(Fault.class)
                .addClass(ServiceEETest.class)
                .addClass(com.sun.ts.tests.jsonb.cdi.customizedmapping.adapters.model.adapter.AnimalIdentifier.class)
                .addClass(com.sun.ts.tests.jsonb.cdi.customizedmapping.adapters.model.adapter.AnimalJson.class)
                .addClass(com.sun.ts.tests.jsonb.cdi.customizedmapping.adapters.model.adapter.InjectedAdapter.class)
                .addClass(com.sun.ts.tests.jsonb.cdi.customizedmapping.adapters.model.adapter.InjectedListAdapter.class)
                .addClass(com.sun.ts.tests.jsonb.cdi.customizedmapping.adapters.model.Animal.class)
                .addClass(com.sun.ts.tests.jsonb.cdi.customizedmapping.adapters.model.AnimalShelterInjectedAdapter.class)
                .addClass(com.sun.ts.tests.jsonb.cdi.customizedmapping.adapters.model.Cat.class)
                .addClass(com.sun.ts.tests.jsonb.cdi.customizedmapping.adapters.model.Dog.class)
                .addClass(AdaptersCustomizationCDITest.class);

        // InputStream inStream = AdaptersCustomizationCDIServletTest.class.getClassLoader().getResourceAsStream(packagePath +
        // "/servlet_vehicle_web.xml");
        // String webXml = editWebXmlString(inStream, "jsonb_cdi_customizedmapping_adapters_servlet_vehicle");
        // war.setWebXML(new StringAsset(webXml));
        war.setWebXML(AdaptersCustomizationCDIServletTest.class.getClassLoader().getResource(packagePath + "/servlet_vehicle_web.xml"));

        // InputStream inStream =
        // AdaptersCustomizationCDIServletTest.class.getClassLoader().getResourceAsStream(packagePath+"/beans.xml");
        // String beansXml = toString(inStream);
        // war.addAsWebInfResource(AdaptersCustomizationCDIServletTest.class.getClassLoader().getResource(packagePath+"/beans.xml"));

        URL warResURL = AdaptersCustomizationCDIServletTest.class.getClassLoader().getResource(packagePath + "/beans.xml");
        if (warResURL != null) {
            war.addAsWebResource(warResURL, "/WEB-INF/beans.xml");
        }

        return war;
        // EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class,
        // "jsonb_cdi_customizedmapping_adapters_servlet_vehicle.ear");
        // ear.addAsModule(war);
        // return ear;

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
    @TargetVehicle("servlet")
    public void testCDISupport() throws Exception {
        super.testCDISupport();
    }
}
