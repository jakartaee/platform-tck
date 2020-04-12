/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates and others.
 * All rights reserved.
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

/*
 * $Id$
 */
package com.sun.ts.tests.websocket.ee.jakarta.websocket.websocketcontainer;

import java.io.IOException;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/TCKTestServer")
public class WSTestServer {

  @OnOpen
  public void init(Session session) throws IOException {
    System.out.println("===============================");
    session.getBasicRemote().sendText("TCKTestServer opened");
  }

  @OnMessage
  public void respond(String message, Session session) throws IOException {
    System.out.println(
        "TCKTestServer got  message: " + message + " from session " + session);
    session.getBasicRemote().sendText("TCKTestServer received: " + message);
    session.getBasicRemote().sendText("TCKTestServer responds");
  }

  @OnError
  public void onError(Session session, Throwable t) throws IOException {
    session.getBasicRemote().sendText("TCKTestServer onError");
    t.printStackTrace();
  }
}
