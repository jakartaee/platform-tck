/*
 * Copyright (c) 2007, 2024 Oracle and/or its affiliates. All rights reserved.
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

/*
 * @(#)ClientServlet.java
 */

package com.sun.ts.tests.signaturetest;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.util.Arrays;
import java.util.Properties;

import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.lib.util.TestUtil;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

@ExtendWith(ArquillianExtension.class)
@Tag("signaturetest")
@Tag("platform")
@Tag("web")
@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientServlet extends JakartaEESigTest implements Serializable {
    static final String VEHICLE_ARCHIVE = "jpa_jpa22_repeatable_attroverride_pmservlet_vehicle";

    /**
     * Servlet container test
     * <p>
     * Only generate the war for this test since it needs to deploy to both Platform + Web Profile.
     * <p>
     * EE 10 JavaEESigTest_servlet_vehicle.ear contents
     * <p>
     * META-INF/MANIFEST.MF
     * JavaEESigTest_servlet_vehicle_web.war
     * <p>
     * JavaEESigTest_servlet_vehicle_web.war contents
     * <p>
     * META-INF/MANIFEST.MF
     * WEB-INF/classes/com/sun/ts/tests/signaturetest/javaee/JavaEESigTest$Containers.class
     * WEB-INF/classes/com/sun/ts/tests/signaturetest/javaee/JavaEESigTest.class
     * WEB-INF/classes/com/sun/ts/lib/harness/EETest$Fault.class
     * WEB-INF/classes/com/sun/ts/lib/harness/EETest$SetupException.class
     * WEB-INF/classes/com/sun/ts/lib/harness/EETest.class
     * WEB-INF/classes/com/sun/ts/lib/harness/ServiceEETest.class
     * WEB-INF/classes/com/sun/ts/tests/common/vehicle/VehicleClient.class
     * WEB-INF/classes/com/sun/ts/tests/common/vehicle/VehicleRunnable.class
     * WEB-INF/classes/com/sun/ts/tests/common/vehicle/VehicleRunnerFactory.class
     * WEB-INF/classes/com/sun/ts/tests/common/vehicle/servlet/ServletVehicle.class
     * WEB-INF/classes/com/sun/ts/tests/signaturetest/ApiCheckDriver.class
     * WEB-INF/classes/com/sun/ts/tests/signaturetest/PackageList.class
     * WEB-INF/classes/com/sun/ts/tests/signaturetest/SigTest.class
     * WEB-INF/classes/com/sun/ts/tests/signaturetest/SigTestData.class
     * WEB-INF/classes/com/sun/ts/tests/signaturetest/SigTestDriver.class
     * WEB-INF/classes/com/sun/ts/tests/signaturetest/SigTestEE.class
     * WEB-INF/classes/com/sun/ts/tests/signaturetest/SigTestResult.class
     * WEB-INF/classes/com/sun/ts/tests/signaturetest/SignatureTestDriver$SignatureFileInfo.class
     * WEB-INF/classes/com/sun/ts/tests/signaturetest/SignatureTestDriver.class
     * WEB-INF/classes/com/sun/ts/tests/signaturetest/SignatureTestDriverFactory.class
     * WEB-INF/lib/sigtest.jar
     * WEB-INF/web.xml
     */

    public static WebArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
        // the war with the correct archive name
        WebArchive signaturetest_servlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "SigTest_servlet_vehicle_web.war");
        // The class files
        signaturetest_servlet_vehicle_web.addClasses(
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
                com.sun.ts.lib.harness.EETest.Fault.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
                ClientServlet.class,
                com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
                com.sun.ts.lib.harness.EETest.class,
                com.sun.ts.lib.harness.ServiceEETest.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
                com.sun.ts.tests.common.vehicle.pmservlet.PMServletVehicle.class,
                com.sun.ts.lib.harness.EETest.SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
        );
        // The web.xml descriptor
        URL warResURL = ClientServlet.class.getResource("/com/sun/ts/tests/common/vehicle/servlet/servlet_vehicle_web.xml");
        if (warResURL != null) {
            signaturetest_servlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
        }

        // add jakarta.tck:sigtest-maven-plugin jar to the war
        // Import Maven runtime dependencies
        File[] files = Maven.resolver()
                .loadPomFromFile("pom.xml")
                .importRuntimeDependencies()
                .resolve()
                .withTransitivity()
                .asFile();
        // add signature test artifact
        Arrays.stream(files).filter(file -> file.getName().contains("sigtest-maven-plugin"))
                .forEach(file -> {
                    signaturetest_servlet_vehicle_web.addAsLibrary(file);
                });
        // Call the archive processor
        archiveProcessor.processWebArchive(signaturetest_servlet_vehicle_web, ClientServlet.class, warResURL);
        return signaturetest_servlet_vehicle_web;
    }


    /* Test setup: */

    /*
     * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
     *
     * @class.testArgs: -ap tssql.stmt
     */
    public void setup(String args[], Properties p) throws Exception {
        super.setup(args, p);
        TestUtil.logMsg("Setup signature tests");
    }

    /* Test cleanup */

    public void cleanup() throws Exception {
        TestUtil.logMsg("Cleanup ok");
    }

    @Test
    @Override
    @TargetVehicle("servlet")
    public void signatureTest() throws java.lang.Exception {
        super.signatureTest();
    }

}
