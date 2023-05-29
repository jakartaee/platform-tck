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

package ee.jakarta.tck.core.json;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import ee.jakarta.tck.core.common.Utils;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.spi.JsonProvider;
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
 * Validate that a custom deployment local JsonProvider implementation is seen and
 * can be injected via CDI
 */
@ExtendWith(ArquillianExtension.class)
public class ApplicationJsonpIT {
    private static final String EXPECTED_JSON_PROVIDER = CustomJsonProvider.class.getName();

    @Deployment(testable = true)
    public static WebArchive createTestArchive() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, ApplicationJsonpIT.class.getSimpleName()+".war");

        archive.addClass(ApplicationJsonpIT.class)
                .addClass(CustomJsonProvider.class)
                .addClass(JsonProviderProducer.class)
                .addClass(Utils.class)
                .addAsServiceProvider(JsonProvider.class, CustomJsonProvider.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "classes/META-INF/beans.xml");
        System.out.printf("test archive: %s\n", archive.toString(true));
        archive.as(ZipExporter.class).exportTo(new File("/tmp/" + archive.getName()), true);
        return archive;
    }

    /**
     * Test that the custom provider is seen via ServiceLoader
     * @param reporter - test reporter
     */
    @Test
    public void testCustomProvider(TestReporter reporter) {
        // Load my provider
        JsonProvider provider = JsonProvider.provider();
        String providerClass = provider.getClass().getName();
        reporter.publishEntry("provider class=" + providerClass);
        if (providerClass.equals(EXPECTED_JSON_PROVIDER))
            reporter.publishEntry("Current provider does matches " + EXPECTED_JSON_PROVIDER);
        else {
            reporter.publishEntry("Current provider does NOT matches " + EXPECTED_JSON_PROVIDER);
            ServiceLoader<JsonProvider> loader = ServiceLoader.load(JsonProvider.class);
            Iterator<JsonProvider> it = loader.iterator();
            List<JsonProvider> providers = new ArrayList<>();
            while (it.hasNext()) {
                providers.add(it.next());
            }
            reporter.publishEntry("Providers: " + providers);
            Assertions.fail("Failed to find JsonProvider: " + EXPECTED_JSON_PROVIDER);
        }
    }

    @Inject
    JsonProvider customProvider;

    /**
     * Test that the custom provider is seen by a CDI producer and is injected into customProvider
     * @param reporter
     */
    @Test
    public void testInjectCustomProvider(TestReporter reporter) {
        reporter.publishEntry("customProvider: " + customProvider);
        Assertions.assertNotNull(customProvider, "Injected JsonProvider");

    }

    /**
     * Validate JsonProvider methods are sent to CustomJsonProvider
     * @param reporter
     */
    @Test
    public void testUseCustomProvider(TestReporter reporter) {
        System.out.printf("testUseCustomProvider\n");
        JsonArrayBuilder arrayBuilder = customProvider.createArrayBuilder();
        String method = Utils.popStack();
        Assertions.assertEquals("createArrayBuilder()Ljakarta/json/JsonArrayBuilder;", method);
        customProvider.createBuilderFactory(null);
        method = Utils.popStack();
        Assertions.assertEquals("createBuilderFactory(Ljava/util/Map;)Ljakarta/json/JsonBuilderFactory;", method);
    }

    /**
     * Validate Json facade calls CustomJsonProvider
     * @param reporter
     */
    @Test
    public void testUseJsonWithCustomProvider(TestReporter reporter) {
        System.out.printf("testUseJsonWithCustomProvider\n");
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        String method = Utils.popStack();
        Assertions.assertEquals("createArrayBuilder()Ljakarta/json/JsonArrayBuilder;", method);
        Json.createBuilderFactory(null);
        method = Utils.popStack();
        Assertions.assertEquals("createBuilderFactory(Ljava/util/Map;)Ljakarta/json/JsonBuilderFactory;", method);
    }
}
