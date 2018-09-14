/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.websocket.ee.javax.websocket.server.serverendpointconfig.builder;

import java.io.IOException;
import java.util.List;

import javax.websocket.Decoder;
import javax.websocket.Encoder;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Extension;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpointConfig;

import com.sun.ts.tests.websocket.common.util.IOUtil;
import com.sun.ts.tests.websocket.common.util.StringUtil;

public class WSCommonServer extends Endpoint
    implements MessageHandler.Whole<String> {

  protected Session session;

  protected ServerEndpointConfig config;

  @Override
  public void onMessage(String message) {
    try {
      if (message.equals("subprotocols"))
        session.getBasicRemote()
            .sendText(StringUtil.objectsToString(config.getSubprotocols()));
      else if (message.equals("path"))
        session.getBasicRemote().sendText(config.getPath());
      else if (message.equals("endpoint"))
        session.getBasicRemote().sendText(config.getEndpointClass().getName());
      else if (message.equals("configurator"))
        session.getBasicRemote()
            .sendText(config.getConfigurator().getClass().getName());
      else if (message.equals("extensions")) {
        StringBuilder sb = new StringBuilder().append("[");
        Extension[] ext = config.getExtensions().toArray(new Extension[0]);
        for (Extension e : ext)
          sb.append(e.getName()).append(" ");
        sb.append("]");
        session.getBasicRemote().sendText(sb.toString());
      } else if (message.equals("decoders")) {
        List<Class<? extends Decoder>> list = config.getDecoders();
        session.getBasicRemote()
            .sendText("{" + StringUtil.objectsToString(list) + "}");
      } else if (message.equals("encoders")) {
        List<Class<? extends Encoder>> list = config.getEncoders();
        session.getBasicRemote()
            .sendText("{" + StringUtil.objectsToString(list) + "}");
      }
    } catch (IOException e) {
      e.printStackTrace();
      try {
        session.getBasicRemote().sendText(IOUtil.printStackTrace(e));
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }
  }

  @Override
  public void onOpen(Session session, EndpointConfig config) {
    this.session = session;
    this.config = (ServerEndpointConfig) config;
    session.addMessageHandler(this);
  }

  @Override
  public void onError(Session session, Throwable thr) {
    thr.printStackTrace(); // Write to error log, too
    String message = IOUtil.printStackTrace(thr);
    try {
      session.getBasicRemote().sendText(message);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
