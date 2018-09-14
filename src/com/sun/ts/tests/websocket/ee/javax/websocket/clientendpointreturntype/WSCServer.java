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

package com.sun.ts.tests.websocket.ee.javax.websocket.clientendpointreturntype;

import java.io.IOException;
import java.nio.ByteBuffer;

import javax.websocket.MessageHandler;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.sun.ts.tests.websocket.common.util.IOUtil;

@ServerEndpoint("/srv")
public class WSCServer {

  private enum State {
    INIT, SECOND, FINAL
  };

  // endpoint instance is created once per connection
  private State state = State.INIT;

  @OnOpen
  public void onOpen(final Session session) {
    session.addMessageHandler(new MessageHandler.Whole<ByteBuffer>() {
      @Override
      public void onMessage(ByteBuffer message) {
        echo(IOUtil.byteBufferToString(message), session);
      }
    });
  }

  @OnMessage
  public void echo(String echo, Session session) {
    switch (state) {
    case INIT:
      state = State.SECOND;
      op(echo, session);
      break;
    case SECOND:
      state = State.FINAL;
      op(echo, session);
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

  private void op(String echo, Session session) {
    try {
      session.getBasicRemote().sendText(echo);
    } catch (IOException e) {
      onError(session, e);
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
