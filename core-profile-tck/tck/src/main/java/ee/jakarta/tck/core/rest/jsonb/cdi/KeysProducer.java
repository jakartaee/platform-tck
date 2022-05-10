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

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class KeysProducer {

    private PublicKey publicKey;

    @PostConstruct
    private void loadKeys() throws Exception {
        InputStream keyIS = getClass().getResourceAsStream("/key.pub");
        if(keyIS == null) {
            System.out.println("key.pub: "+getClass().getResourceAsStream("key.pub"));
            throw new IllegalStateException("Failed to find /key.pub");
        }
        byte[] pubKeyData = keyIS.readAllBytes();
        String pubKeyString = new String(pubKeyData, StandardCharsets.UTF_8);
        System.out.println(pubKeyString);
        byte[] keyData = Base64.getDecoder().decode(pubKeyString);
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(keyData);
        System.out.println(publicKeySpec);

        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        publicKey = keyFactory.generatePublic(publicKeySpec);
    }

    @Produces
    public PublicKey getPublicKey() {
        return publicKey;
    }
}
