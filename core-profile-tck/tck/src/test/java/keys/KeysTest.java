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

package keys;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Validate key pair generation used by the CustomJsonbSerializationIT
 */
public class KeysTest {
    private static String PLAINTEXT = "Some message to verify";

    @Test
    public void testKeyPairGen() throws Exception {
        ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256r1");
        KeyPairGenerator g = KeyPairGenerator.getInstance("EC");
        g.initialize(ecSpec, new SecureRandom());

        KeyPair keyPair = g.generateKeyPair();

        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        System.out.println("Public key format: " + publicKey.getFormat());
        System.out.println("Private key format: " + privateKey.getFormat());

        File[] keyFiles = generatePublicKeyAndPrivateKey(keyPair, "mykeys");


        byte[] pubKeyData = Files.readAllBytes(keyFiles[1].toPath());
        String pubKeyString = new String(pubKeyData, StandardCharsets.UTF_8);
        System.out.println(pubKeyString);
        byte[] keyData = Base64.getDecoder().decode(pubKeyString);
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(keyData);
        System.out.println(publicKeySpec);

        Signature ecdsaSign = Signature.getInstance("SHA256withECDSA");
        ecdsaSign.initSign(privateKey);
        ecdsaSign.update(PLAINTEXT.getBytes(StandardCharsets.UTF_8));
        byte[] signature = ecdsaSign.sign();
        String sig = Base64.getEncoder().encodeToString(signature);
        System.out.println(sig);

        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        PublicKey publicKeyCheck = keyFactory.generatePublic(publicKeySpec);
        if(!publicKeyCheck.equals(publicKey)) {
            Assertions.fail("PublicKey from file does NOT match!");
        } else {
            System.out.println("PublicKey from file matches!");
        }

        Signature ecdsaVerify = Signature.getInstance("SHA256withECDSA");
        ecdsaVerify.initVerify(publicKeyCheck);
        ecdsaVerify.update(PLAINTEXT.getBytes(StandardCharsets.UTF_8));
        boolean result = ecdsaVerify.verify(Base64.getDecoder().decode(sig));
        Assertions.assertTrue(result,"Veried(%s), " + PLAINTEXT);
    }

    private static File[] generatePublicKeyAndPrivateKey(KeyPair keypair, String base)
            throws IOException {
        File keyFile = File.createTempFile(base, ".key");
        OutputStream out = new FileOutputStream(keyFile);
        out.write(keypair.getPrivate().getEncoded());
        out.close();
        System.out.println(keyFile.getAbsolutePath());

        File pubFile = File.createTempFile(base, ".pub");
        PublicKey publicKey = keypair.getPublic();
        String pubKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        out = new FileOutputStream(pubFile);
        out.write(pubKeyString.getBytes(StandardCharsets.UTF_8));
        out.close();
        System.out.println(keyFile.getAbsolutePath());
        System.out.println(pubKeyString);

        return new File[] {keyFile, pubFile};
    }
}
