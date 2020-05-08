/*
 * Copyright (c) 2017, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.ts.tests.jaxrs.jaxrs21.ee.sse.sseeventsource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseEventSink;
import jakarta.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import com.sun.ts.tests.jaxrs.common.impl.JaxbKeyValueBean;
import com.sun.ts.tests.jaxrs.common.impl.SinglevaluedMap;
import com.sun.ts.tests.jaxrs.jaxrs21.ee.sse.SSEMessage;

@Path("media")
public class MediaTypeResource {
  private static MediaType mediaType = MediaType.WILDCARD_TYPE;

  @POST
  @Path("set")
  public String setMediaType(String media) {
    String[] split = media.split("/", 2);
    mediaType = new MediaType(split[0], split[1]);
    return media;
  }

  @GET
  @Path("data")
  @Produces(MediaType.SERVER_SENT_EVENTS)
  public void sendData(@Context SseEventSink sink, @Context Sse sse) {
    try (SseEventSink s = sink) {
      s.send(sse.newEventBuilder().data(SSEMessage.MESSAGE).mediaType(mediaType)
          .build());
    }
  }

  @GET
  @Path("jaxb")
  @Produces(MediaType.SERVER_SENT_EVENTS)
  public void sendJAXB(@Context SseEventSink sink, @Context Sse sse) {
    try (SseEventSink s = sink) {
      JAXBElement<String> element = new JAXBElement<String>(new QName("name"),
          String.class, SSEMessage.MESSAGE);
      s.send(sse.newEventBuilder().data(element).mediaType(mediaType).build());
    }
  }

  @GET
  @Path("xml")
  @Produces(MediaType.SERVER_SENT_EVENTS)
  public void sendXML(@Context SseEventSink sink, @Context Sse sse) {
    try (SseEventSink s = sink) {
      JaxbKeyValueBean bean = new JaxbKeyValueBean();
      bean.set("key", SSEMessage.MESSAGE);
      s.send(sse.newEventBuilder().data(bean).mediaType(mediaType).build());
    }
  }

  @GET
  @Path("map")
  @Produces(MediaType.SERVER_SENT_EVENTS)
  public void sendMap(@Context SseEventSink sink, @Context Sse sse) {
    try (SseEventSink s = sink) {
      SinglevaluedMap<String, String> map = new SinglevaluedMap<>();
      map.add("key", SSEMessage.MESSAGE);
      s.send(sse.newEventBuilder().data(map).mediaType(mediaType).build());
    }
  }
}
