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

package com.sun.ts.tests.websocket.common.client;

import static com.sun.ts.tests.websocket.common.client.WebSocketCommonClient.logTrace;
import static com.sun.ts.tests.websocket.common.client.WebSocketCommonClient.logMsg;

import java.io.IOException;
import java.nio.ByteBuffer;

import javax.websocket.EndpointConfig;
import javax.websocket.PongMessage;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;

import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient.Entity;
import com.sun.ts.tests.websocket.common.impl.StringPingMessage;
import com.sun.ts.tests.websocket.common.util.IOUtil;

/**
 * The default {@link EndpointCallback} that is used in tests. This callback
 * sends an entity on onOpen to a server endpoint set by
 * {@link WebSocketCommonClient#setEntity(Object...)} or by
 * 
 * <pre>
 * setProperty(Property.CONTENT, ...)
 * </pre>
 * 
 * </p>
 * Note that no callback is used when no entity is defined to be sent to a
 * server side
 */
public class SendMessageCallback extends EndpointCallback {
  protected Entity entity;

  public SendMessageCallback(Entity entity) {
    super();
    this.entity = entity;
  }

  @Override
  public void onOpen(Session session, EndpointConfig config) {
    super.onOpen(session, config);
    logMsg("Sending entity", entity);
    RemoteEndpoint.Basic endpoint = session.getBasicRemote();
    try {
      if (entity.isInstance(String.class))
        sendString(endpoint, entity, 0);
      else if (entity.isInstance(ByteBuffer.class))
        sendBytes(endpoint, entity, 0);
      else if (entity.isInstance(PongMessage.class))
        sendPingPong(endpoint, entity.getEntityAt(PongMessage.class, 0));
      else
        sendObject(endpoint, entity.getEntityAt(Object.class, 0));
    } catch (Exception i) {
      throw new RuntimeException(i);
    }
  }

  protected void sendString(RemoteEndpoint.Basic endpoint, Entity entity,
      int index) throws IOException {
    String message = entity.getEntityAt(String.class, index);
    if (entity.length() - 1 > index) {
      endpoint.sendText(message, false);
      logTrace("RemoteEndpoint.Basic.sendText(", message, ", false)");
      sendString(endpoint, entity, index + 1);
    } else if (index > 0) {
      endpoint.sendText(message, true);
      logTrace("RemoteEndpoint.Basic.sendText(", message, ", true)");
    } else {
      endpoint.sendText(message);
      logTrace("RemoteEndpoint.Basic.sendText(", message, ")");
    }
  }

  protected void sendBytes(RemoteEndpoint.Basic endpoint, Entity entity,
      int index) throws IOException {
    ByteBuffer msg = entity.getEntityAt(ByteBuffer.class, index);
    String bytes = new String(msg.array());
    if (entity.length() - 1 > index) {
      endpoint.sendBinary(msg, false);
      logTrace("RemoteEndpoint.Basic.sendBinary(", bytes, ", false)");
      sendBytes(endpoint, entity, index + 1);
    } else if (index > 0) {
      endpoint.sendBinary(msg, true);
      logTrace("RemoteEndpoint.Basic.sendBinary(", bytes, ", true)");
    } else {
      endpoint.sendBinary(msg);
      logTrace("RemoteEndpoint.Basic.sendBinary(", bytes, ")");
    }
  }

  protected void sendPingPong(RemoteEndpoint.Basic endpoint, PongMessage pong)
      throws Exception {
    ByteBuffer buffer = pong.getApplicationData();
    String msg = IOUtil.byteBufferToString(buffer);
    if (StringPingMessage.class.isInstance(pong)) {
      endpoint.sendPing(buffer);
      logTrace("RemoteEndpoint.Basic.sendPing(", msg, ")");
    } else {
      endpoint.sendPong(buffer);
      logTrace("RemoteEndpoint.Basic.sendPong(", msg, ")");
    }
  }

  protected void sendObject(RemoteEndpoint.Basic endpoint, Object message)
      throws Exception {
    String entity = null;
    if (message.getClass().getName().equals("[B"))
      entity = new String((byte[]) message);
    else
      entity = message.toString();
    logTrace("RemoteEndpoint.Basic.sendObject(", entity, ")");
    endpoint.sendObject(message);
  }

}
