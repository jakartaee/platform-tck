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

package com.sun.ts.tests.websocket.ee.javax.websocket.server.serverendpointconfig;

import java.io.IOException;

import jakarta.websocket.EndpointConfig;
import jakarta.websocket.Extension;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.server.ServerEndpointConfig;

import com.sun.ts.tests.websocket.common.util.IOUtil;
import com.sun.ts.tests.websocket.common.util.StringUtil;

@ServerEndpoint(value = "/annotated/subprotocols", subprotocols = { "abc",
    "def" })
public class WSAnnotatedSubprotocolsServer {

  Session session;

  ServerEndpointConfig config;

  @OnMessage
  public void onMessage(String msg) {
    try {
      if (msg.equals("subprotocols"))
        session.getBasicRemote()
            .sendText(StringUtil.objectsToString(config.getSubprotocols()));
      else if (msg.equals("path"))
        session.getBasicRemote().sendText(config.getPath());
      else if (msg.equals("endpoint"))
        session.getBasicRemote().sendText(config.getEndpointClass().getName());
      else if (msg.equals("configurator"))
        session.getBasicRemote()
            .sendText(config.getConfigurator().getClass().getName());
      else if (msg.equals("extensions")) {
        StringBuilder sb = new StringBuilder().append("[");
        Extension[] ext = config.getExtensions().toArray(new Extension[0]);
        for (Extension e : ext)
          sb.append(e.getName()).append(" ");
        sb.append("]");
        session.getBasicRemote().sendText(sb.toString());
      } else
        session.getBasicRemote().sendText(session.getNegotiatedSubprotocol());
    } catch (IOException e) {
      e.printStackTrace();
      try {
        session.getBasicRemote().sendText(IOUtil.printStackTrace(e));
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }
  }

  @OnOpen
  public void onOpen(Session session, EndpointConfig config) {
    this.session = session;
    this.config = (ServerEndpointConfig) config;
  }

  @OnError
  public void onError(Session session, Throwable thr) throws IOException {
    thr.printStackTrace(); // Write to error log, too
    String message = IOUtil.printStackTrace(thr);
    session.getBasicRemote().sendText(message);
  }

}
