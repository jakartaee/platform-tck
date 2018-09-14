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

package com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.usercoder.asyncwithhandler;

import java.io.IOException;

import javax.websocket.EncodeException;
import javax.websocket.SendResult;
import javax.websocket.Session;

import com.sun.ts.tests.websocket.common.impl.WaitingSendHandler;
import com.sun.ts.tests.websocket.common.util.IOUtil;
import com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.usercoder.OPS;
import com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.usercoder.WSCSuperEndpoint;

public abstract class WSCCommonServer implements WSCSuperEndpoint {
  public void onMessage(String msg, Session session)
      throws IOException, EncodeException {
    OPS op = OPS.valueOf(msg);
    WaitingSendHandler handler = new WaitingSendHandler();
    switch (op) {
    case BOOL:
      session.getAsyncRemote().sendObject(BOOL, handler);
      break;
    case BYTE:
      session.getAsyncRemote().sendObject(NUMERIC.byteValue(), handler);
      break;
    case CHAR:
      session.getAsyncRemote().sendObject(CHAR, handler);
      break;
    case DOUBLE:
      session.getAsyncRemote().sendObject(NUMERIC.doubleValue(), handler);
      break;
    case FLOAT:
      session.getAsyncRemote().sendObject(NUMERIC.floatValue(), handler);
      break;
    case INT:
      session.getAsyncRemote().sendObject(NUMERIC.intValue(), handler);
      break;
    case LONG:
      session.getAsyncRemote().sendObject(NUMERIC.longValue(), handler);
      break;
    case SHORT:
      session.getAsyncRemote().sendObject(NUMERIC.shortValue(), handler);
      break;
    }
    SendResult result = handler.waitForResult(4);
    if (!result.isOK() || result.getException() != null)
      throw new RuntimeException(result.getException());
  }

  public void onError(Session session, Throwable t) throws IOException {
    System.out.println("@OnError in " + getClass().getName());
    t.printStackTrace(); // Write to error log, too
    String message = "Exception: " + IOUtil.printStackTrace(t);
    session.getBasicRemote().sendText(message);
  }
}
