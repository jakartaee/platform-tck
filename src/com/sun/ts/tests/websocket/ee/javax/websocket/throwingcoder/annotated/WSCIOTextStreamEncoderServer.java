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

package com.sun.ts.tests.websocket.ee.javax.websocket.throwingcoder.annotated;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.SendResult;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.sun.ts.tests.websocket.common.impl.WaitingSendHandler;
import com.sun.ts.tests.websocket.common.stringbean.StringBean;
import com.sun.ts.tests.websocket.ee.javax.websocket.throwingcoder.ThrowingIOTextStreamEncoder;
import com.sun.ts.tests.websocket.ee.javax.websocket.throwingcoder.ThrowingTextDecoder;

@ServerEndpoint(value = "/iotextstreamencoder", encoders = {
    ThrowingIOTextStreamEncoder.class })
public class WSCIOTextStreamEncoderServer {

  @OnMessage
  public void echo(String data, Session session)
      throws InterruptedException, ExecutionException {
    WaitingSendHandler handler = new WaitingSendHandler();
    session.getAsyncRemote().sendObject(new StringBean(data), handler);
    SendResult result = handler.waitForResult(4);
    if (result.getException() != null)
      throw new RuntimeException(result.getException());
  }

  @OnError
  public void onError(Session session, Throwable t) throws IOException {
    String message = ThrowingTextDecoder.getCauseMessage(t);
    session.getBasicRemote().sendText(message);
  }

}
