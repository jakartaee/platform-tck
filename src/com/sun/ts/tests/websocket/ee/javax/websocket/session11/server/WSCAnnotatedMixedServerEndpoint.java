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

package com.sun.ts.tests.websocket.ee.javax.websocket.session11.server;

import java.io.IOException;
import java.io.InputStream;

import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.PongMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.sun.ts.tests.websocket.common.impl.StringPongMessage;
import com.sun.ts.tests.websocket.common.io.StringInputStream;
import com.sun.ts.tests.websocket.common.util.IOUtil;
import com.sun.ts.tests.websocket.ee.javax.websocket.session11.common.TypeEnum;

@ServerEndpoint("/exception")
public class WSCAnnotatedMixedServerEndpoint {

  public static final String EXCEPTION = "Exception: ";

  static class StringThrowingPartialMessageHandler
      extends StringPartialMessageHandler {
    public StringThrowingPartialMessageHandler(Session session) {
      super(session);
    }

    @Override
    public void onMessage(String partialMessage, boolean last) {
      if (TypeEnum.STRING_PARTIAL.name().equals(partialMessage)) {
        if (last)
          session.addMessageHandler(String.class, this);
      } else
        super.onMessage(partialMessage, last);
    }
  }

  static class InputStreamThrowingMessageHandler
      extends InputStreamMessageHandler {
    public InputStreamThrowingMessageHandler(Session session) {
      super(session);
    }

    @Override
    public void onMessage(InputStream message) {
      String msg = null;
      try {
        msg = IOUtil.readFromStream(message);
      } catch (IOException e) {
        // do nothing
      }
      if (TypeEnum.INPUTSTREAM.name().equals(msg))
        session.addMessageHandler(InputStream.class, this);
      else
        super.onMessage(new StringInputStream(msg));
    }
  }

  static class PongMessageThrowingMessageHandler extends PongMessageHandler {
    public PongMessageThrowingMessageHandler(Session session) {
      super(session);
    }

    @Override
    public void onMessage(PongMessage message) {
      String msg = null;
      msg = IOUtil.byteBufferToString(message.getApplicationData());
      if (TypeEnum.PONG.name().equals(msg))
        session.addMessageHandler(PongMessage.class, this);
      else
        super.onMessage(new StringPongMessage(msg));
    }
  }

  @OnOpen
  public void onOpen(Session session) {
    session.addMessageHandler(String.class,
        new StringThrowingPartialMessageHandler(session));
    session.addMessageHandler(InputStream.class,
        new InputStreamThrowingMessageHandler(session));
    session.addMessageHandler(PongMessage.class,
        new PongMessageThrowingMessageHandler(session));
  }

  @OnError
  public void onError(Session session, Throwable t) throws IOException {
    // this @OnError should catch RuntimeException thrown by second
    // session.addMessageHandler call
    sendException(session, t);
  }

  static void sendException(Session session, Throwable t) {
    String ex = EXCEPTION + t.getMessage();
    try {
      session.getBasicRemote().sendText(ex);
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }
}
