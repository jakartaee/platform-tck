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

package com.sun.ts.tests.websocket.ee.javax.websocket.clientendpointonmessage;

import java.io.IOException;
import java.nio.ByteBuffer;

import javax.websocket.MessageHandler;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.sun.ts.tests.websocket.common.util.IOUtil;

@ServerEndpoint("/srv/{param}")
public class WSCServer {

  private enum State {
    INIT, SECOND, FINAL
  };

  // endpoint instance is created once per connection
  private State state = State.INIT;

  @OnOpen
  public void onOpen(final Session session,
      final @PathParam("param") String op) {
    session.addMessageHandler(new MessageHandler.Whole<ByteBuffer>() {
      @Override
      public void onMessage(ByteBuffer message) {
        echo(op, IOUtil.byteBufferToString(message), session);
      }
    });
  }

  @OnMessage
  public void echo(@PathParam("param") String op, String echo,
      Session session) {
    switch (state) {
    case INIT:
      state = State.SECOND;
      op(op, echo, session);
      break;
    case SECOND:
      state = State.FINAL;
      op(op, echo, session);
      break;
    case FINAL:
      // do not send anything, otherwise it would not ever stop
      try {
        session.close();
      } catch (IOException e) {
        onError(session, e);
      }
      break;
    }
  }

  private void op(String param, String echo, Session session) {
    if (param == null)
      throw new RuntimeException("Path param is null");
    OPS op = OPS.valueOf(param);
    switch (op) {
    case TEXT:
      try {
        session.getBasicRemote().sendText(echo);
      } catch (IOException e) {
        onError(session, e);
      }
      break;
    case TEXTPARTIAL:
      String[] tokens = echo.split("_");
      for (int i = 0; i != tokens.length; i++)
        try {
          boolean isLast = i == tokens.length - 1;
          if (isLast)
            session.getBasicRemote().sendText(tokens[i], true);
          else
            session.getBasicRemote().sendText(tokens[i] + "_", false);
        } catch (IOException e) {
          onError(session, e);
        }
      break;
    case BINARY:
      try {
        session.getBasicRemote().sendBinary(ByteBuffer.wrap(echo.getBytes()));
      } catch (IOException e) {
        onError(session, e);
      }
      break;
    case BINARYPARTIAL:
      tokens = echo.split("_");
      for (int i = 0; i != tokens.length; i++)
        try {
          boolean isLast = i == tokens.length - 1;
          ByteBuffer buf;
          if (isLast) {
            buf = ByteBuffer.wrap(tokens[i].getBytes());
            session.getBasicRemote().sendBinary(buf, true);
          } else {
            buf = ByteBuffer.wrap((tokens[i] + "_").getBytes());
            session.getBasicRemote().sendBinary(buf, false);
          }
        } catch (IOException e) {
          onError(session, e);
        }
      break;
    case PONG:
      try {
        session.getBasicRemote().sendPong(ByteBuffer.wrap(echo.getBytes()));
      } catch (IOException e) {
        onError(session, e);
      }
      break;
    default:
      throw new IllegalStateException(op + " not implemented");
    }
  }

  @OnError
  public void onError(Session session, Throwable t) {
    System.out.println("@OnError in" + getClass().getName());
    t.printStackTrace(); // Write to error log, too
    String message = IOUtil.printStackTrace(t);
    try {
      if (session.isOpen())
        session.getBasicRemote().sendText(message);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
