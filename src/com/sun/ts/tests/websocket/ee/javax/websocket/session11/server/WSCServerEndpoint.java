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

package com.sun.ts.tests.websocket.ee.javax.websocket.session11.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.LinkedList;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.PongMessage;
import javax.websocket.Session;

import com.sun.ts.tests.websocket.common.stringbean.StringBean;
import com.sun.ts.tests.websocket.common.util.IOUtil;
import com.sun.ts.tests.websocket.ee.javax.websocket.session11.common.StringList;
import com.sun.ts.tests.websocket.ee.javax.websocket.session11.common.TypeEnum;

public class WSCServerEndpoint extends Endpoint {
  public WSCServerEndpoint(TypeEnum typeEnum) {
    super();
    this.typeEnum = typeEnum;
  }

  private TypeEnum typeEnum;

  @SuppressWarnings("unchecked")
  @Override
  public void onOpen(Session session, EndpointConfig config) {
    switch (typeEnum) {
    case LINKEDLIST_HASHSET_TEXT:
      LinkedList<HashSet<String>> list = new LinkedList<HashSet<String>>();
      Class<LinkedList<HashSet<String>>> clzLLHS = (Class<LinkedList<HashSet<String>>>) list
          .getClass();
      session.addMessageHandler(clzLLHS,
          new LinkedListHashSetMessageHandler(session));
      break;
    case LIST_TEXT:
      session.addMessageHandler(StringList.class,
          new StringListWholeMessageHandler(session));
      break;
    case STRINGBEAN:
    case STRINGBEANSTREAM:
    case STRINGBEANBINARY:
    case STRINGBEANBINARYSTREAM:
      session.addMessageHandler(StringBean.class,
          new StringBeanMessageHandler(session));
      break;
    case STRING_WHOLE:
      session.addMessageHandler(String.class,
          new StringWholeMessageHandler(session));
      break;
    case STRING_PARTIAL:
      session.addMessageHandler(String.class,
          new StringPartialMessageHandler(session));
      break;
    case READER:
      session.addMessageHandler(Reader.class,
          new ReaderMessageHandler(session));
      break;
    case PONG:
      session.addMessageHandler(PongMessage.class,
          new PongMessageHandler(session));
      // send pingmessage to receive pongmessage
      break;
    case BYTEBUFFER_WHOLE:
      session.addMessageHandler(ByteBuffer.class,
          new ByteBufferMessageHandler(session));
      break;
    case BYTEBUFFER_PARTIAL:
      session.addMessageHandler(ByteBuffer.class,
          new ByteBufferPartialMessageHandler(session));
      break;
    case BYTEARRAY_WHOLE:
      byte[] ba = new byte[0];
      Class<byte[]> baclz = (Class<byte[]>) ba.getClass();
      session.addMessageHandler(baclz, new ByteArrayMessageHandler(session));
      break;
    case BYTEARRAY_PARTIAL:
      ba = new byte[0];
      baclz = (Class<byte[]>) ba.getClass();
      session.addMessageHandler(baclz,
          new ByteArrayPartialMessageHandler(session));
      break;
    case INPUTSTREAM:
      session.addMessageHandler(InputStream.class,
          new InputStreamMessageHandler(session));
      break;
    default:
      break;
    }
  }

  @Override
  public void onError(Session session, Throwable t) {
    super.onError(session, t);
    t.printStackTrace(); // Write to error log, too
    String message = IOUtil.printStackTrace(t);
    try {
      session.getBasicRemote().sendText(message);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
