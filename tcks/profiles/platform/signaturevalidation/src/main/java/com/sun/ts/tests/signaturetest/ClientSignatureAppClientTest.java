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
 * @(#)ClientSignatureAppClientTest.java
 */

package com.sun.ts.tests.signaturetest;

import java.io.File;
import java.io.Serializable;
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
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

@ExtendWith(ArquillianExtension.class)
@Tag("signaturetest")
@Tag("platform")
public class ClientSignatureAppClientTest extends JakartaEESigTest implements Serializable {
    static final String VEHICLE_ARCHIVE = "signaturetest_ClientSignatureServletTest_servlet_vehicle_web.war";

    @TargetsContainer("tck-appclient")
    @OverProtocol("appclient")
    @Deployment(name = "appclient", testable = true)
    public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {

        JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "rsMeta_appclient_vehicle_client.jar");
        archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
        archive.addPackages(true, "com.sun.ts.lib.harness");
        archive.addClasses(ClientSignatureAppClientTest.class,
                JakartaEESigTest.class,
                SigTestEE.class,
                com.sun.ts.lib.harness.EETest.class,
                Fault.class,
                SetupException.class,
                com.sun.ts.lib.harness.ServiceEETest.class,
                com.sun.ts.lib.harness.Status.class,
                com.sun.ts.lib.util.TestUtil.class
        );
        // The appclient-client descriptor
        URL appClientUrl = ClientSignatureAppClientTest.class.getResource("application-client.xml");
        if (appClientUrl != null) {
            archive.addAsManifestResource(appClientUrl, "application-client.xml");
        }
        archive.addAsManifestResource(
                new StringAsset("Main-Class: " + "com.sun.ts.tests.common.vehicle.VehicleClient" + "\n"),
                "MANIFEST.MF");

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
        EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "ClientSignatureAppClientTest_appclient_vehicle.ear");
        for (String signatureMapFile : signatureMapFiles) {
            URL signResURL = ClientSignatureAppClientTest.class.getResource("signature-repository/" + signatureMapFile);
            if (signResURL != null) {
                archive.addAsManifestResource(signResURL, signatureMapFile);
            } else {
                throw new IllegalStateException("missing " + signatureMapFile);
            }
            // add jakarta.tck:sigtest-maven-plugin jar to the war
            // Import Maven runtime dependencies
            String profiles = System.getProperty("active.profiles", "");
            String[] activeMavenProfiles = !profiles.isEmpty() ? profiles.split(",") : new String[]{};
            File[] files = Maven.resolver()
                    .loadPomFromFile("pom.xml", activeMavenProfiles)
                    .resolve("jakarta.tck:sigtest-maven-plugin", "jakarta.tck:signaturetest")
                    .withoutTransitivity()
                    .asFile();

            // add signature test artifacts
            ear.addAsLibraries(files);
        }
        ear.addAsModule(archive);
        return ear;
    }


    @Test
    @Override
    @TargetVehicle("appclient")
    public void signatureTest() throws Exception {
        super.signatureTest();
    }

}
