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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Utils {
    static String generateHash(SomeMessage msg) throws NoSuchAlgorithmException {
        byte[] msgContent = getMessageContent(msg);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] msgHash = md.digest(msgContent);
        String msgHash64 = Base64.getEncoder().encodeToString(msgHash);
        System.out.printf("generateHash(%s) = %s\n", msg, msgHash64);
        return msgHash64;
    }

    static byte[] getMessageContent(SomeMessage msg) {
        StringBuilder tmp = new StringBuilder();
        tmp.append(msg.getMsg());
        tmp.append(msg.getRecipient());
        tmp.append(msg.getSender());
        tmp.append(msg.getSignature() != null ? msg.getSignature() : "");
        return tmp.toString().getBytes(StandardCharsets.UTF_8);
    }
    static boolean verifyMsgHash(SomeMessage msg, String sha256) {
        boolean verified = false;
        byte[] msgContent = getMessageContent(msg);
        String msgHash64 = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] msgHash = md.digest(msgContent);
            msgHash64 = Base64.getEncoder().encodeToString(msgHash);
            verified = msgHash64.equals(sha256);
            System.out.printf("verifyMsgHash, %s == %s; %s\n", sha256, msgHash64, verified);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to generate SHA-256", e);
        }
        return verified;
    }
}
