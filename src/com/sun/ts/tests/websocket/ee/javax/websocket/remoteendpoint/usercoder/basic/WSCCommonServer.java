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

package com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.usercoder.basic;

import java.io.IOException;

import javax.websocket.EncodeException;
import javax.websocket.Session;

import com.sun.ts.tests.websocket.common.util.IOUtil;
import com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.usercoder.OPS;
import com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.usercoder.WSCSuperEndpoint;

public abstract class WSCCommonServer implements WSCSuperEndpoint {
  public void onMessage(String msg, Session session)
      throws IOException, EncodeException {
    OPS op = OPS.valueOf(msg);
    switch (op) {
    case BOOL:
      session.getBasicRemote().sendObject(BOOL);
      break;
    case BYTE:
      session.getBasicRemote().sendObject(NUMERIC.byteValue());
      break;
    case CHAR:
      session.getBasicRemote().sendObject(CHAR);
      break;
    case DOUBLE:
      session.getBasicRemote().sendObject(NUMERIC.doubleValue());
      break;
    case FLOAT:
      session.getBasicRemote().sendObject(NUMERIC.floatValue());
      break;
    case INT:
      session.getBasicRemote().sendObject(NUMERIC.intValue());
      break;
    case LONG:
      session.getBasicRemote().sendObject(NUMERIC.longValue());
      break;
    case SHORT:
      session.getBasicRemote().sendObject(NUMERIC.shortValue());
      break;
    }
  }

  public void onError(Session session, Throwable t) throws IOException {
    System.out.println("@OnError in " + getClass().getName());
    t.printStackTrace(); // Write to error log, too
    String message = "Exception: " + IOUtil.printStackTrace(t);
    session.getBasicRemote().sendText(message);
  }
}
