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

package ee.jakarta.tck.core.rest.jsonb.cdi;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;
import java.util.Base64;
import java.util.List;

import ee.jakarta.tck.core.rest.JaxRsActivator;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Response;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.extension.ExtendWith;

import static ee.jakarta.tck.core.rest.jsonb.cdi.ApplicationResource.INVALID_SIGNATURE_ERORR;
import static ee.jakarta.tck.core.rest.jsonb.cdi.CustomJsonbResolver.NO_SHA256_ERROR_MSG;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

@ExtendWith(ArquillianExtension.class)
public class CustomJsonbSerializationIT {
    static PrivateKey privateKey;

    @ArquillianResource
    private URL contextPath;

    @Deployment(testable = false)
    public static WebArchive createTestArchive() throws Exception {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, CustomJsonbSerializationIT.class.getSimpleName() + ".war");

        String pubKeyString = generateKeyPair();
        archive.addClass(ApplicationResource.class)
                .addClass(CustomJsonbResolver.class)
                .addClass(KeysProducer.class)
                .addClass(SomeMessage.class)
                .addClass(Utils.class)
                .addClass(JaxRsActivator.class)
                .addAsResource(new StringAsset(pubKeyString), "key.pub");
        ;
        System.out.printf("test archive: %s\n", archive.toString(true));
        archive.as(ZipExporter.class).exportTo(new File("/tmp/" + archive.getName()), true);
        return archive;
    }

    private static String generateKeyPair() throws Exception {
        ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256r1");
        KeyPairGenerator g = KeyPairGenerator.getInstance("EC");
        g.initialize(ecSpec, new SecureRandom());

        KeyPair keyPair = g.generateKeyPair();

        PublicKey publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
        System.out.println("Public key format: " + publicKey.getFormat());
        System.out.println("Private key format: " + privateKey.getFormat());

        String pubKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        System.out.println(pubKeyString);
        return pubKeyString;
    }

    @Test
    public void testCustomSerializationWithCDIProvider(TestReporter reporter) throws Exception {
        URI appIdURI = contextPath.toURI().resolve("rest/json-tests");
        reporter.publishEntry(String.format("appIdURI: %s", appIdURI.toASCIIString()));

        SomeMessage sentMsg = new SomeMessage();
        sentMsg.setMsg("Hello from testCustomSerializationWithCDIProvider");
        sentMsg.setSender("testCustomSerializationWithCDIProvider");
        sentMsg.setRecipient("ApplicationResource");

        Signature ecdsaSign = Signature.getInstance("SHA256withECDSA");
        ecdsaSign.initSign(privateKey);
        ecdsaSign.update(sentMsg.getMsg().getBytes(StandardCharsets.UTF_8));
        byte[] signature = ecdsaSign.sign();
        sentMsg.setSignature(Base64.getEncoder().encodeToString(signature));

        try (final Client client = ClientBuilder.newBuilder().register(new CustomJsonbResolver()).build()) {
            final SomeMessage responseMsg = client.target(appIdURI)
                    .request(APPLICATION_JSON_TYPE)
                    .buildPost(Entity.entity(sentMsg, APPLICATION_JSON_TYPE))
                    .invoke(SomeMessage.class);

            System.out.println(responseMsg);
            Assertions.assertNotNull(responseMsg, "Received non-null reponse");
            // Verify the msg.signature was validated by server
            Assertions.assertTrue(responseMsg.isSecure(), "Reponse message was flagged secure");
            // Validate the msg hash was verified by CustomJsonbResolver
            Assertions.assertTrue(responseMsg.isVerified(), "Reponse message was flagged verified");
        }
    }

    @Test
    public void testMissingClientSerializationWithCDIProvider(TestReporter reporter) throws Exception {
        URI appIdURI = contextPath.toURI().resolve("rest/json-tests");
        reporter.publishEntry(String.format("appIdURI: %s", appIdURI.toASCIIString()));

        SomeMessage sentMsg = new SomeMessage();
        sentMsg.setMsg("Hello from testMissingClientSerializationWithCDIProvider");
        sentMsg.setSender("testMissingClientSerializationWithCDIProvider");
        sentMsg.setRecipient("ApplicationResource");

        Signature ecdsaSign = Signature.getInstance("SHA256withECDSA");
        ecdsaSign.initSign(privateKey);
        ecdsaSign.update(sentMsg.getMsg().getBytes(StandardCharsets.UTF_8));
        byte[] signature = ecdsaSign.sign();
        sentMsg.setSignature(Base64.getEncoder().encodeToString(signature));

        try (final Client client = ClientBuilder.newBuilder().build()) {
            final Response response = client.target(appIdURI)
                    .request(APPLICATION_JSON_TYPE)
                    .buildPost(Entity.entity(sentMsg, APPLICATION_JSON_TYPE))
                    .invoke();

            System.out.printf("Response(%d), reason=%s\n", response.getStatus(), response.getStatusInfo().getReasonPhrase());
            final List<Integer> expected = List.of(Response.Status.BAD_REQUEST.getStatusCode(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
            Assertions.assertTrue(expected.contains(response.getStatus()),
                    () -> String.format("Expected one of %s got %d: %s", expected, response.getStatus(), response.readEntity(String.class)));
        }

    }

    @Test
    public void testCustomSerializationWithCDIProviderBadSignature(TestReporter reporter) throws Exception {
        URI appIdURI = contextPath.toURI().resolve("rest/json-tests");
        reporter.publishEntry(String.format("appIdURI: %s", appIdURI.toASCIIString()));

        SomeMessage sentMsg = new SomeMessage();
        sentMsg.setMsg("Hello from testCustomSerializationWithCDIProviderBadSignature");
        sentMsg.setSender("testCustomSerializationWithCDIProviderBadSignature");
        sentMsg.setRecipient("ApplicationResource");

        sentMsg.setSignature(Base64.getEncoder().encodeToString("bad signature".getBytes(StandardCharsets.UTF_8)));

        try (final Client client = ClientBuilder.newBuilder().register(new CustomJsonbResolver()).build()) {
            final Response response = client.target(appIdURI)
                    .request(APPLICATION_JSON_TYPE)
                    .buildPost(Entity.entity(sentMsg, APPLICATION_JSON_TYPE))
                    .invoke();

            System.out.printf("Response(%d), reason=%s\n", response.getStatus(), response.getStatusInfo().getReasonPhrase());
            Assertions.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        }
    }

    @Test
    public void testCustomSerializationWithCDIProviderWrongSignature(TestReporter reporter) throws Exception {
        URI appIdURI = contextPath.toURI().resolve("rest/json-tests");
        reporter.publishEntry(String.format("appIdURI: %s", appIdURI.toASCIIString()));

        SomeMessage sentMsg = new SomeMessage();
        sentMsg.setMsg("Hello from testCustomSerializationWithCDIProviderBadSignature");
        sentMsg.setSender("testCustomSerializationWithCDIProviderBadSignature");
        sentMsg.setRecipient("ApplicationResource");

        Signature ecdsaSign = Signature.getInstance("SHA256withECDSA");
        ecdsaSign.initSign(privateKey);
        ecdsaSign.update("The wrong message".getBytes(StandardCharsets.UTF_8));
        byte[] signature = ecdsaSign.sign();
        sentMsg.setSignature(Base64.getEncoder().encodeToString(signature));

        try (final Client client = ClientBuilder.newBuilder().register(new CustomJsonbResolver()).build()) {
            final Response response = client.target(appIdURI)
                    .request(APPLICATION_JSON_TYPE)
                    .buildPost(Entity.entity(sentMsg, APPLICATION_JSON_TYPE))
                    .invoke();

            System.out.printf("Response(%d), reason=%s\n", response.getStatus(), response.getStatusInfo().getReasonPhrase());
            Assertions.assertEquals(Response.Status.METHOD_NOT_ALLOWED.getStatusCode(), response.getStatus());

        }
    }
}