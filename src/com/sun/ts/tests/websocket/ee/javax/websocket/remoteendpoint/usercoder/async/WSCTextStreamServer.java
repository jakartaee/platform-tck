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

package com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.usercoder.async;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.websocket.EncodeException;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.usercoder.TextStreamCoderBool;
import com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.usercoder.TextStreamCoderByte;
import com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.usercoder.TextStreamCoderChar;
import com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.usercoder.TextStreamCoderDouble;
import com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.usercoder.TextStreamCoderFloat;
import com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.usercoder.TextStreamCoderInt;
import com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.usercoder.TextStreamCoderLong;
import com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.usercoder.TextStreamCoderShort;

@ServerEndpoint(value = "/textstream", encoders = { TextStreamCoderBool.class,
    TextStreamCoderByte.class, TextStreamCoderChar.class,
    TextStreamCoderDouble.class, TextStreamCoderInt.class,
    TextStreamCoderLong.class, TextStreamCoderFloat.class,
    TextStreamCoderShort.class })
public class WSCTextStreamServer extends WSCCommonServer {

  @OnMessage
  public void onMessage(String msg, Session session) throws IOException,
      EncodeException, InterruptedException, ExecutionException {
    super.onMessage(msg, session);
  }

  @OnError
  public void onError(Session session, Throwable t) throws IOException {
    super.onError(session, t);
  }
}
