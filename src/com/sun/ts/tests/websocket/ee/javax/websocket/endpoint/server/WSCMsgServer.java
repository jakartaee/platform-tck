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

package com.sun.ts.tests.websocket.ee.javax.websocket.endpoint.server;

import java.io.IOException;

import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.sun.ts.tests.websocket.common.util.IOUtil;

@ServerEndpoint(value = "/msg")
public class WSCMsgServer {
  static final String EMPTY = "empty";

  static final String MESSAGES[] = { "reset", "lastmsg" };

  static String message = EMPTY;

  @OnMessage
  public String onMessage(String msg, Session session) {
    if (MESSAGES[0].equals(msg)) {
      setLastMessage(EMPTY);
    }
    msg = getLastMessage();
    return msg;
  }

  @OnError
  public void onError(Session session, Throwable thr) throws IOException {
    thr.printStackTrace(); // Write to error log, too
    String message = IOUtil.printStackTrace(thr);
    session.getBasicRemote().sendText(message);
  }

  private static String getLastMessage() {
    return message;
  }

  public static void setLastMessage(String lastMessage) {
    WSCMsgServer.message = lastMessage;
  }

}
