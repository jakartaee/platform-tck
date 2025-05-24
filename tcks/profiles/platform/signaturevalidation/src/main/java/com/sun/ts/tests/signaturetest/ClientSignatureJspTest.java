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
 * @(#)ClientSignatureJspTest.java
 */

package com.sun.ts.tests.signaturetest;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

import com.sun.ts.lib.harness.Fault;
import com.sun.ts.lib.harness.SetupException;
import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.tests.common.base.ServiceEETest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

@ExtendWith(ArquillianExtension.class)
@Tag("signaturetest")
@Tag("platform")
@Tag("web")
public class ClientSignatureJspTest extends JakartaEESigTest implements Serializable {
    static final String VEHICLE_ARCHIVE = "signaturetest_ClientSignatureJspTest_jsp_vehicle_web.war";

    /**
     * JSP container test
     * <p>
     * Only generate the war for this test since it needs to deploy to both Platform + Web Profile.
     * <p>
     * EE 10 JavaEESigTest_jsp_vehicle.ear contents
     * <p>
     * META-INF/MANIFEST.MF
     * JavaEESigTest_jsp_vehicle_web.war
     * <p>
     * JavaEESigTest_jsp_vehicle_web.war contents
     * <p>
     * client.html
     * jsp_vehicle.jsp
     * META-INF/MANIFEST.MF
     * WEB-INF/classes/com/sun/ts/lib/harness/
     *     EETest$Fault.class
     *     EETest$SetupException.class
     *     EETest.class
     *     ServiceEETest.class
     * WEB-INF/classes/com/sun/ts/tests/
     *      common/vehicle/
     *          VehicleClient.class
     *          VehicleRunnable.class
     *          VehicleRunnerFactory.class
     *      signaturetest/
     *          ApiCheckDriver.class
     *          javaee/
     *              JavaEESigTest$Containers.class
     *              JavaEESigTest.class
     *          PackageList.class
     *          SignatureTestDriver$SignatureFileInfo.class
     *          SignatureTestDriver.class
     *          SignatureTestDriverFactory.class
     *          SigTest.class
     *          SigTestData.class
     *          SigTestDriver.class
     *          SigTestEE.class
     *          SigTestResult.class
     * lib/sigtest.jar
     * web.xml
     */
    @TargetsContainer("tck-javatest")
    @OverProtocol("javatest")
    @Deployment(name = "jsp", testable = true)
    public static WebArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
        // the war with the correct archive name
        WebArchive signaturetest_jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, VEHICLE_ARCHIVE);
        // The class files
        signaturetest_jsp_vehicle_web.addClasses(
                ClientSignatureJspTest.class,
                JakartaEESigTest.class,
                SigTestEE.class,

                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.jsp.JSPVehicleRunner.class,

                EETest.class,
                Fault.class,
                SetupException.class,
                ServiceEETest.class,
                com.sun.ts.lib.harness.Status.class,
                com.sun.ts.lib.util.TestUtil.class

        );
        InputStream jspVehicle = Thread.currentThread().getContextClassLoader().
                getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
        signaturetest_jsp_vehicle_web.add(new ByteArrayAsset(jspVehicle), "jsp_vehicle.jsp");
        InputStream clientHtml = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
        signaturetest_jsp_vehicle_web.add(new ByteArrayAsset(clientHtml), "client.html");

        // The web.xml descriptor
        URL warResURL = ClientSignatureJspTest.class.getResource("/com/sun/ts/tests/signaturetest/jsp_vehicle_web.xml");
        if (warResURL != null) {
            signaturetest_jsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
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
            URL signResURL = ClientSignatureJspTest.class.getResource("signature-repository/" + signatureMapFile);
            if (signResURL != null) {
                signaturetest_jsp_vehicle_web.addAsWebInfResource(signResURL, signatureMapFile);
            } else {
                throw new IllegalStateException("missing " + signatureMapFile);
            }

        }

        // Call the archive processor
        archiveProcessor.processWebArchive(signaturetest_jsp_vehicle_web, ClientSignatureJspTest.class, warResURL);
        return signaturetest_jsp_vehicle_web;
    }

    @Test
    @Override
    @TargetVehicle("jsp")
    public void signatureTest() throws Exception {
        super.signatureTest();
    }

}
