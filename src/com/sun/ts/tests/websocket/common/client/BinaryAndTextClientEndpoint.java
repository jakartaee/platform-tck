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

import java.nio.ByteBuffer;

import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

import com.sun.ts.tests.websocket.common.util.IOUtil;

/**
 * Receive any of ByteBuffer (binary) and String (Text) messages, the client
 * behaves without making any difference on type of message received.
 */
public class BinaryAndTextClientEndpoint extends ClientEndpoint<String> {
  @Override
  public void onOpen(Session session, EndpointConfig config) {
    super.onOpen(session, config);
    session.addMessageHandler(new MessageHandler.Whole<ByteBuffer>() {
      @Override
      public void onMessage(ByteBuffer message) {
        String msg = IOUtil.byteBufferToString(message);
        BinaryAndTextClientEndpoint.this.onMessage(msg);
      }
    });
  }
}
