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

package ee.jakarta.tck.core.jsonb;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import ee.jakarta.tck.core.common.Utils;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.spi.JsonbProvider;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Validate that a custom deployment local JsonbProvider implementation is seen and
 * can be injected via CDI
 */
@ExtendWith(ArquillianExtension.class)
public class JsonbApplicationIT {
    private static final String EXPECTED_JSONB_PROVIDER = CustomJsonbProvider.class.getName();

    @Deployment(testable = true)
    public static WebArchive createTestArchive() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, JsonbApplicationIT.class.getSimpleName()+".war");

        archive.addClass(JsonbApplicationIT.class)
                .addClass(CustomJsonbProvider.class)
                .addClass(CustomJsonbBuilder.class)
                .addClass(JsonbProviderProducer.class)
                .addClass(Utils.class)
                .addAsServiceProvider(JsonbProvider.class, CustomJsonbProvider.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        System.out.printf("test archive: %s\n", archive.toString(true));
        archive.as(ZipExporter.class).exportTo(new File("/tmp/" + archive.getName()), true);
        return archive;
    }

    @Test
    public void testApplicationContextSharedBetweenJaxRsRequests(TestReporter reporter) throws Exception {

    }

    @Inject
    JsonbProvider customProvider;

    /**
     * Test that the custom provider is seen via ServiceLoader
     * @param reporter - test reporter
     */
    @Test
    public void testCustomProvider(TestReporter reporter) {
        // Load my provider
        JsonbProvider provider = JsonbProvider.provider(EXPECTED_JSONB_PROVIDER);
        String providerClass = provider.getClass().getName();
        System.out.printf("provider class=%s\n", providerClass);
        reporter.publishEntry("provider class=" + providerClass);
        if (providerClass.equals(EXPECTED_JSONB_PROVIDER))
            reporter.publishEntry("Current provider does matches " + EXPECTED_JSONB_PROVIDER);
        else {
            reporter.publishEntry("Current provider does NOT matches " + EXPECTED_JSONB_PROVIDER);
            ServiceLoader<JsonbProvider> loader = ServiceLoader.load(JsonbProvider.class);
            Iterator<JsonbProvider> it = loader.iterator();
            List<JsonbProvider> providers = new ArrayList<>();
            while (it.hasNext()) {
                providers.add(it.next());
            }
            reporter.publishEntry("Providers: " + providers);
            System.out.printf("Providers: %s\n", providers);
            Assertions.fail("Failed to find JsonbProvider: " + EXPECTED_JSONB_PROVIDER);
        }
    }

    /**
     * Test that the custom provider is seen by a CDI producer and is injected into customProvider
     * @param reporter
     */
    @Test
    public void testInjectCustomProvider(TestReporter reporter) {
        reporter.publishEntry("customProvider: " + customProvider);
        Assertions.assertNotNull(customProvider, "Injected JsonbProvider");
        Assertions.assertTrue(customProvider instanceof CustomJsonbProvider, "Injected instance is a CustomJsonbProvider");
    }

    /**
     * Validate JsonbProvider methods are sent to CustomJsonbProvider
     * @param reporter
     */
    @Test
    public void testUseCustomProvider(TestReporter reporter) {
        System.out.printf("testUseCustomProvider\n");
        JsonbBuilder builder = customProvider.create();
        String method = Utils.popStack();
        Assertions.assertEquals("create()Ljakarta/json/bind/JsonbBuilder;", method);
        Jsonb jsonb = builder.build();
        method = Utils.popStack();
        Assertions.assertEquals("build()Ljakarta/json/bind/Jsonb;", method);
    }

}
