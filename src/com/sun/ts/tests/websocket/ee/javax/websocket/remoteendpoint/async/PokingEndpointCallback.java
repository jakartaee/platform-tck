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

package com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.async;

import java.io.IOException;

import javax.websocket.EndpointConfig;
import javax.websocket.Session;

import com.sun.ts.tests.websocket.common.client.SendMessageCallback;
import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient.Entity;

public class PokingEndpointCallback extends SendMessageCallback {

  public PokingEndpointCallback(Entity entity) {
    super(entity);
  }

  private Session session;

  int pokeCnt = 0;

  int pokeMax = 1;

  @Override
  public void onOpen(Session session, EndpointConfig config) {
    super.onOpen(session, config);
    this.session = session;
  }

  public void onMessage(String msg, Session session) throws IOException {
    if (msg.equals(WSCServerSideServer.RESPONSE[0])
        || (msg.equals(WSCServerSideServer.RESPONSE[1])))
      return;
    if (msg.startsWith("-100"))
      msg = OPS.SENDOBJECT_BYTE.name();
    else if (msg.startsWith("-101"))
      msg = OPS.SENDOBJECT_SHORT.name();
    else if (msg.startsWith("-102"))
      msg = OPS.SENDOBJECT_INT.name();
    else if (msg.startsWith("-103"))
      msg = OPS.SENDOBJECT_LONG.name();
    else if (msg.startsWith("-104"))
      msg = OPS.SENDOBJECT_FLOAT.name();
    else if (msg.startsWith("-105"))
      msg = OPS.SENDOBJECT_DOUBLE.name();
    else if (msg.startsWith(String.valueOf((char) 106)))
      msg = OPS.SENDOBJECT_CHAR.name();
    else if (msg.startsWith("false"))
      msg = OPS.SENDOBJECT_BOOL.name();
    OPS ops = OPS.valueOf(msg);
    switch (ops) {
    case SENDBINARY:
    case SENDBINARYHANDLER:
    case SENDOBJECT:
    case SENDOBJECT_BOOL:
    case SENDOBJECT_BYTE:
    case SENDOBJECT_CHAR:
    case SENDOBJECT_DOUBLE:
    case SENDOBJECT_FLOAT:
    case SENDOBJECT_INT:
    case SENDOBJECT_LONG:
    case SENDOBJECT_SHORT:
    case SENDOBJECTHANDLER:
    case SENDTEXT:
    case SENDTEXTHANDLER:
    case BATCHING_ALLOWED:
    case SEND_PONG:
      if (pokeCnt++ <= pokeMax)
        session.getBasicRemote().sendText(OPS.POKE.name());
      break;
    case SENDBINARYTHROWS:
    case SENDBINARYEXECUTIONEXCEPTION:
      //
    case SENDBINARYHANDLERTHROWSONDATA:
    case SENDBINARYHANDLERTHROWSONHANDLER:
      //
    case SENDOBJECTHANDLEREXECUTIONEXCEPTION:
    case SENDOBJECTHANDLERTHROWSONDATA:
    case SENDOBJECTHANDLERTHROWSONHANDLER:
      //
    case SENDOBJECTEXECUTIONEXCEPTION:
    case SENDOBJECTTHROWS:
      //
    case SENDTEXTTHROWS:
      //
    case SENDTEXTHANDLERTHROWSONDATA:
    case SENDTEXTHANDLERTHROWSONHANDLER:
      //
    case POKE:
    case TIMEOUT:
      //
    case SEND_PING:
    case SEND_PING_THROWS:
    case SEND_PONG_THROWS:
      break;
    default:
      break;
    }
  }

  @Override
  public void onMessage(Object o) {
    super.onMessage(o);
    try {
      String msg = o.toString();
      onMessage(msg, session);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }
}
