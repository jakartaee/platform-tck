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

import java.lang.reflect.Type;

import jakarta.json.JsonObject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.serializer.DeserializationContext;
import jakarta.json.bind.serializer.JsonbDeserializer;
import jakarta.json.bind.serializer.JsonbSerializer;
import jakarta.json.bind.serializer.SerializationContext;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonParser;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

/**
 * A ContextResolver that installs a custom serialization for SomeMessage type
 */
@Provider
public class CustomJsonbResolver implements ContextResolver<Jsonb> {
    public static final String NO_SHA256_ERROR_MSG = "No sha256 found in top level JSON object";

    public final Jsonb getContext(final Class<?> type) {
        if (!SomeMessage.class.isAssignableFrom(type))
            return null;

        System.out.printf("CustomJsonbResolver.getContext(%s)\n", type);
        return JsonbBuilder.create(new JsonbConfig().withSerializers(new CustomSerializer()).withDeserializers(new CustomDeserializer()));
    }

    static final class CustomSerializer implements JsonbSerializer<SomeMessage> {
        @Override
        public void serialize(final SomeMessage msg, final JsonGenerator generator, final SerializationContext ctx) {
            String msgHash64 = null;
            try {
                msgHash64 = Utils.generateHash(msg);
            } catch (Exception e) {
                throw new IllegalStateException("Failed to generate SHA-256", e);
            }
            System.out.printf("CustomJsonbResolver.CustomSerializer.serialize(%s); sha256=%s\n", msg, msgHash64);

            generator.writeStartObject();
            generator.write("sha256", msgHash64);
                generator.writeStartObject("msg");
                generator.write("content", msg.getMsg());
                generator.write("recipient", msg.getRecipient());
                generator.write("sender", msg.getSender());
                String signature = msg.getSignature();
                generator.write("signature", signature != null ? signature : "");
                generator.write("secure", msg.isSecure());
                generator.writeEnd();
            generator.writeEnd();
        }
    }

    static final class CustomDeserializer implements JsonbDeserializer<SomeMessage> {
        @Override
        public SomeMessage deserialize(final JsonParser parser, final DeserializationContext ctx, final Type rtType) {
            final SomeMessage someMessage = new SomeMessage();
            JsonObject wrapper = parser.getObject();
            if(!wrapper.containsKey("sha256")) {
                throw new WebApplicationException(NO_SHA256_ERROR_MSG, Response.Status.BAD_REQUEST);
            }
            String sha256 = wrapper.getString("sha256");

            JsonObject msg = wrapper.getJsonObject("msg");
            String content = msg.getString("content");
            String recipient = msg.getString("recipient");
            String sender = msg.getString("sender");
            String signature = msg.getString("signature");
            boolean secure = msg.getBoolean("secure");
            someMessage.setMsg(content);
            someMessage.setRecipient(recipient);
            someMessage.setSender(sender);
            someMessage.setSignature(signature);
            someMessage.setSecure(secure);
            boolean verified = Utils.verifyMsgHash(someMessage, sha256);
            someMessage.setVerified(verified);
            System.out.printf("CustomJsonbResolver.CustomDeserializer.deserialize(%s); sha256=%s\n", someMessage, sha256);

            return someMessage;
        }
    }
}
