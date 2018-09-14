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

/*
 * $Id$
 */
package com.sun.ts.tests.websocket.ee.javax.websocket.session;

import com.sun.ts.tests.websocket.common.util.IOUtil;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/TCKTestServerByte")
public class WSTestServerByte {

  @OnOpen
  public void init(Session session) throws IOException {
    String message = "========TCKTestServerByte opened";
    ByteBuffer data = ByteBuffer.allocate(message.getBytes().length);
    data.put(message.getBytes());
    data.flip();
    session.getBasicRemote().sendBinary(data);
  }

  @OnMessage
  public void respond(ByteBuffer message, Session session) {
    String message_string = IOUtil.byteBufferToString(message);

    System.out.println("TCKTestServerByte got ByteBuffer message: "
        + message_string + " from session " + session);
    try {
      ByteBuffer data = ByteBuffer
          .allocate(("========TCKTestServerByte received ByteBuffer: "
              + "========TCKTestServerByte responds: Message in bytes")
                  .getBytes().length
              + message.capacity());
      data.put(("========TCKTestServerByte received ByteBuffer: ").getBytes());
      data.put(message_string.getBytes());
      data.put(
          ("========TCKTestServerByte responds: Message in bytes").getBytes());
      data.flip();
      session.getBasicRemote().sendBinary(data);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @OnError
  public void onError(Session session, Throwable t) {
    System.out.println("TCKTestServerByte onError");
    try {
      session.getBasicRemote().sendText("========TCKTestServerByte onError");
    } catch (Exception e) {
      e.printStackTrace();
    }
    t.printStackTrace();
  }
}
