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

package com.sun.ts.tests.websocket.ee.javax.websocket.server.serverendpointconfig.configurator;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpointConfig;

import com.sun.ts.tests.websocket.common.util.IOUtil;
import com.sun.ts.tests.websocket.common.util.StringUtil;

public class WSCSubprotocolServer extends Endpoint
    implements MessageHandler.Whole<String> {

  Session session;

  ServerEndpointConfig config;

  @Override
  public void onMessage(String msg) {
    List<String> ret = null;
    boolean contains = false;
    if (msg.equals("requested")) {
      contains = StringUtil.contains(
          GetNegotiatedSubprotocolConfigurator.getRequested(),
          getRequestedSubprotocols());
      ret = contains ? getRequestedSubprotocols()
          : GetNegotiatedSubprotocolConfigurator.getRequested();
    } else if (msg.equals("supported")) {
      List<String> subprotocols = config.getSubprotocols();
      contains = StringUtil.contains(
          GetNegotiatedSubprotocolConfigurator.getSupported(), subprotocols);
      ret = contains ? subprotocols
          : GetNegotiatedSubprotocolConfigurator.getSupported();
    } else if (msg.equals("resulted")) {
      ret = Collections.singletonList(
          "{" + GetNegotiatedSubprotocolConfigurator.getResulted() + "}");
    }
    try {
      session.getBasicRemote().sendText(StringUtil.collectionToString(ret));
    } catch (IOException e) {
      e.printStackTrace();
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

  public static List<String> getRequestedSubprotocols() {
    return Arrays.asList(StringUtil.WEBSOCKET_SUBPROTOCOLS[0],
        StringUtil.WEBSOCKET_SUBPROTOCOLS[2],
        StringUtil.WEBSOCKET_SUBPROTOCOLS[4]);
  }

  public static List<String> getSupportedSubprotocols() {
    return Arrays.asList(StringUtil.WEBSOCKET_SUBPROTOCOLS[1],
        StringUtil.WEBSOCKET_SUBPROTOCOLS[3],
        StringUtil.WEBSOCKET_SUBPROTOCOLS[5],
        StringUtil.WEBSOCKET_SUBPROTOCOLS[4],
        StringUtil.WEBSOCKET_SUBPROTOCOLS[2]);
  }

}
