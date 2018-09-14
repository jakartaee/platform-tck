/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;

@Path("repeat")
public class RepeatedCasterResource {

  private static boolean cast = false;

  private static volatile Boolean isOpen = false;

  private static int cnt = 0;

  @POST
  @Path("set")
  @Produces(MediaType.TEXT_PLAIN)
  public boolean set(boolean b) {
    isOpen = false;
    cast = b;
    cnt = 0;
    return cast;
  }

  @GET
  @Path("cast")
  @Produces(MediaType.SERVER_SENT_EVENTS)
  public void send(@Context SseEventSink sink, @Context Sse sse) {
    new Thread() {
      public void run() {
        synchronized (isOpen) {
          isOpen = !sink.isClosed();
        }
        while (!sink.isClosed() && cast) {
          sink.send(sse.newEvent(String.valueOf(cnt++)));
          try {
            Thread.sleep(500L);
          } catch (InterruptedException e1) {
            cast = false;
          }
          synchronized (isOpen) {
            isOpen = !sink.isClosed();
            System.out.println("ISOPEN " + isOpen);
          }
        }
        cast = false;
      };
    }.start();
  }

  @GET
  @Path("isopen")
  public boolean isOpen() {
    synchronized (isOpen) {
      System.out.println("ASKED ISOPEN " + isOpen);
      return isOpen;
    }
  }
}
