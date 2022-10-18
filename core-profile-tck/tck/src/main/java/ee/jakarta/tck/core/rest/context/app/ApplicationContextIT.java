/*
 * Copyright (c) "2022" Red Hat and others
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

package ee.jakarta.tck.core.rest.context.app;

import java.io.File;
import java.net.URI;
import java.net.URL;

import ee.jakarta.tck.core.rest.JaxRsActivator;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Validate a
 */
@ExtendWith(ArquillianExtension.class)
public class ApplicationContextIT {
    @ArquillianResource
    private URL contextPath;

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, ApplicationContextIT.class.getSimpleName()+".war");

        archive.addClass(SimpleApplicationBean.class)
                .addClass(ApplicationResource.class)
                .addClass(JaxRsActivator.class);
        System.out.printf("test archive: %s\n", archive.toString(true));
        archive.as(ZipExporter.class).exportTo(new File("/tmp/" + archive.getName()), true);
        return archive;
    }

    @Test
    public void testApplicationContextSharedBetweenJaxRsRequests(TestReporter reporter) throws Exception {
        URI appIdURI = contextPath.toURI().resolve("rest/application-id");
        reporter.publishEntry(String.format("appIdURI: %s", appIdURI.toASCIIString()));

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(appIdURI);
        String request1 = target.request(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN).get(String.class);
        // Make a second request and make sure the same context is used
        String request2 = target.request(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN).get(String.class);
        // should be same random number
        assertEquals(Double.parseDouble(request1), Double.parseDouble(request2));
    }

}
