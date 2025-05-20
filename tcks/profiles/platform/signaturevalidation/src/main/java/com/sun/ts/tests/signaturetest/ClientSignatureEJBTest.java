/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */

/*
 * $Id$
 */

/*
 * @(#)ClientSignatureServletTest.java
 */

package com.sun.ts.tests.signaturetest;

import java.io.File;
import java.io.Serializable;
import java.net.URL;

import jakarta.ejb.EJB;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

@ExtendWith(ArquillianExtension.class)
@Tag("signaturetest")
@Tag("platform")
public class ClientSignatureEJBTest extends JakartaEESigTest implements Serializable {
    static final String VEHICLE_ARCHIVE = "signaturetest_ClientSignatureEJBTest_servlet_vehicle_web.war";

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
    @TargetsContainer("tck-javatest")
    @OverProtocol("javatest")
    @Deployment(name = "servlet", testable = true)
    public static WebArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
        // the war with the correct archive name
        WebArchive signaturetest_servlet_vehicle_web = ShrinkWrap.create(WebArchive.class, VEHICLE_ARCHIVE);
        // The class files
        signaturetest_servlet_vehicle_web.addClasses(
                ClientSignatureEJBTest.class,
                SignatureStatefulBean.class,
                JakartaEESigTest.class,
                SigTestEE.class,

                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,

                com.sun.ts.lib.harness.EETest.class,
                Fault.class,
                SetupException.class,
                com.sun.ts.lib.harness.ServiceEETest.class,
                com.sun.ts.lib.harness.Status.class,
                com.sun.ts.lib.util.TestUtil.class

        );
        // The web.xml descriptor
        URL warResURL = ClientSignatureEJBTest.class.getResource("/com/sun/ts/tests/signaturetest/servlet_vehicle_web.xml");
        if (warResURL != null) {
            signaturetest_servlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
        } else {
            throw new IllegalStateException("missing web.xml");
        }

        // add signature map files to WAR deployment
        String[] signatureMapFiles = {
                "jakarta.annotation.sig",
                "jakarta.batch.sig",
                "jakarta.data.sig",
                "jakarta.ejb.sig",
                "jakarta.el.sig",
                "jakarta.enterprise.concurrent.sig",
                "jakarta.enterprise.sig",
                "jakarta.faces.sig",
                "jakarta.interceptor.sig",
                "jakarta.jms.sig",
                "jakarta.json.bind.sig",
                "jakarta.json.sig",
                "jakarta.mail.sig",
                "jakarta.persistence.sig",
                "jakarta.resource.sig",
                "jakarta.security.auth.message.sig",
                "jakarta.security.enterprise.sig",
                "jakarta.security.jacc.sig",
                "jakarta.servlet.jsp.jstl.sig",
                "jakarta.servlet.jsp.sig",
                "jakarta.servlet.sig",
                "jakarta.transaction.sig",
                "jakarta.validation.sig",
                "jakarta.websocket.sig",
                "jakarta.ws.rs.sig"
        };
        for ( String signatureMapFile : signatureMapFiles) {
            URL signResURL = ClientSignatureEJBTest.class.getResource("signature-repository/" + signatureMapFile);
            if (signResURL != null) {
                signaturetest_servlet_vehicle_web.addAsWebInfResource(signResURL, signatureMapFile);
            } else {
                throw new IllegalStateException("missing " + signatureMapFile);
            }

        }

        // Call the archive processor
        archiveProcessor.processWebArchive(signaturetest_servlet_vehicle_web, ClientSignatureEJBTest.class, warResURL);
        return signaturetest_servlet_vehicle_web;
    }

    @EJB(beanName = "SignatureStatefulBean")
    protected SignatureStatefulBean signatureStatefulBean;

    @Test
    @Override
    @TargetVehicle("servlet")
    public void signatureTest() throws Exception {
        signatureStatefulBean.runEJBSignatureTests(this);
        Exception exception = signatureStatefulBean.getTestException();
        if (exception != null ) {
            throw exception;
        }
    }

}
