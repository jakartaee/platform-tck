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
import java.util.concurrent.Future;

import javax.websocket.EncodeException;
import javax.websocket.Session;

import com.sun.ts.tests.websocket.common.util.IOUtil;
import com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.usercoder.OPS;
import com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.usercoder.WSCSuperEndpoint;

public abstract class WSCCommonServer implements WSCSuperEndpoint {
  public void onMessage(String msg, Session session) throws IOException,
      EncodeException, InterruptedException, ExecutionException {
    OPS op = OPS.valueOf(msg);
    Future<Void> future = null;
    switch (op) {
    case BOOL:
      future = session.getAsyncRemote().sendObject(BOOL);
      break;
    case BYTE:
      future = session.getAsyncRemote().sendObject(NUMERIC.byteValue());
      break;
    case CHAR:
      future = session.getAsyncRemote().sendObject(CHAR);
      break;
    case DOUBLE:
      future = session.getAsyncRemote().sendObject(NUMERIC.doubleValue());
      break;
    case FLOAT:
      future = session.getAsyncRemote().sendObject(NUMERIC.floatValue());
      break;
    case INT:
      future = session.getAsyncRemote().sendObject(NUMERIC.intValue());
      break;
    case LONG:
      future = session.getAsyncRemote().sendObject(NUMERIC.longValue());
      break;
    case SHORT:
      future = session.getAsyncRemote().sendObject(NUMERIC.shortValue());
      break;
    }
    future.get();
  }

  public void onError(Session session, Throwable t) throws IOException {
    System.out.println("@OnError in " + getClass().getName());
    t.printStackTrace(); // Write to error log, too
    String message = "Exception: " + IOUtil.printStackTrace(t);
    session.getBasicRemote().sendText(message);
  }
}
