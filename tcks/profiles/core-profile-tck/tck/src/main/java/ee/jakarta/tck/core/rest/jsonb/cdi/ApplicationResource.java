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
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/json-tests")
public class ApplicationResource {
    public static final String INVALID_SIGNATURE_ERORR = "Message signature did not verify";

    @Inject
    private PublicKey publicKey;

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public SomeMessage sendMessage(final SomeMessage message) throws SignatureException {
        System.out.printf("ApplicationResource, msg=%s\n", message);

        SomeMessage reply = new SomeMessage();
        try {
            // Verify the signature sent with the message using injected public key
            Signature ecdsaVerify = Signature.getInstance("SHA256withECDSA");
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(message.getMsg().getBytes(StandardCharsets.UTF_8));
            boolean result = ecdsaVerify.verify(Base64.getDecoder().decode(message.getSignature()));
            System.out.printf("ApplicationResource, secure(%s/%s)=%s\n", message.getMsg(), message.getSignature(), result);
            reply.setSecure(result);
        } catch (Exception e) {
            throw new WebApplicationException("Failed during validation of", Response.Status.INTERNAL_SERVER_ERROR);
        }

        // if signature did not verify, fail
        if(!reply.isSecure()) {
            throw new WebApplicationException(INVALID_SIGNATURE_ERORR, Response.Status.METHOD_NOT_ALLOWED);
        }

        reply.setMsg(message.getMsg() + " ApplicationResource.reply");
        reply.setRecipient(message.getSender());
        reply.setSender("ApplicationResource.sendMessage");
        return reply;
    }
}
