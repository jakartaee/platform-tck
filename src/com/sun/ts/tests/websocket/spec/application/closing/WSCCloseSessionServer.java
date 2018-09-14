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

package com.sun.ts.tests.websocket.spec.application.closing;

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.sun.ts.tests.websocket.common.util.IOUtil;

@ServerEndpoint(value = "/closesession")
public class WSCCloseSessionServer {
  static int lastCloseCode = 0;

  static final String MESSAGES[] = { "idle", "lastcode" };

  @OnMessage
  public String onMessage(String msg, Session session) {
    if (MESSAGES[0].equals(msg)) {
      setLastCloseCode(0);
      session.setMaxIdleTimeout(1);
    } else if (MESSAGES[1].equals(msg))
      msg = getLastCloseCode();
    return msg;
  }

  @OnError
  public void onError(Session session, Throwable thr) throws IOException {
    thr.printStackTrace(); // Write to error log, too
    String message = IOUtil.printStackTrace(thr);
    session.getBasicRemote().sendText(message);
  }

  @OnClose
  public void onClose(CloseReason reason) {
    int lastCloseCode = reason.getCloseCode().getCode();
    setLastCloseCode(lastCloseCode);
  }

  private static String getLastCloseCode() {
    return String.valueOf(lastCloseCode);
  }

  private static void setLastCloseCode(int lastCloseCode) {
    WSCCloseSessionServer.lastCloseCode = lastCloseCode;
  }

}
