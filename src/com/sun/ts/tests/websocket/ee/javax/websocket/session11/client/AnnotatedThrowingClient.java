/*
 * Copyright (c) 2014, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.websocket.ee.javax.websocket.session11.client;

import java.io.InputStream;
import java.io.Reader;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.PongMessage;
import javax.websocket.Session;

import com.sun.ts.tests.websocket.common.client.AnnotatedClientEndpoint;
import com.sun.ts.tests.websocket.common.client.StringClientEndpoint;
import com.sun.ts.tests.websocket.ee.javax.websocket.session11.common.TypeEnum;

@ClientEndpoint
public class AnnotatedThrowingClient extends AnnotatedClientEndpoint<String> {

  TypeEnum typeEnum;

  RuntimeException thrown = null;

  public AnnotatedThrowingClient(TypeEnum typeEnum) {
    super(new StringClientEndpoint());
    this.typeEnum = typeEnum;
  }

  @OnOpen
  public void onOpen(Session session, EndpointConfig config) {
    switch (typeEnum) {
    case PONG:
      session.addMessageHandler(PongMessage.class,
          new PongMessageHandler(clientEndpoint));
      try {
        session.addMessageHandler(PongMessage.class,
            new PongMessageHandler(clientEndpoint));
      } catch (RuntimeException e) {
        thrown = e;
      }
      break;
    case READER:
      session.addMessageHandler(Reader.class,
          new ReaderMessageHandler(clientEndpoint));
      try {
        session.addMessageHandler(Reader.class,
            new ReaderMessageHandler(clientEndpoint));
      } catch (RuntimeException e) {
        thrown = e;
      }
      break;
    case INPUTSTREAM:
      session.addMessageHandler(InputStream.class,
          new InputStreamMessageHandler(clientEndpoint));
      try {
        session.addMessageHandler(InputStream.class,
            new InputStreamMessageHandler(clientEndpoint));
      } catch (RuntimeException e) {
        thrown = e;
      }
      break;
    case STRING_PARTIAL:
      session.addMessageHandler(String.class,
          new StringPartialMessageHandler(clientEndpoint));
      try {
        session.addMessageHandler(String.class,
            new StringPartialMessageHandler(clientEndpoint));
      } catch (RuntimeException e) {
        thrown = e;
      }
      break;
    default:
      break;
    }
    super.onOpen(session, config);
  }

  @OnClose
  @Override
  public void onClose(Session session, CloseReason closeReason) {
    super.onClose(session, closeReason);
  }

  @OnError
  @Override
  public void onError(Session session, Throwable t) {
    super.onError(session, t);
  }

  public RuntimeException getThrown() {
    return thrown;
  }
}
