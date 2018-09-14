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

package com.sun.ts.tests.websocket.ee.javax.websocket.server.pathparam;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.sun.ts.tests.websocket.common.util.IOUtil;

@ServerEndpoint(value = "/param/{param1}/{param2}")
public class WS2StringPathParamServer {
  private final static String ERR = "TCK INTENDED ERROR";

  private String[] p = new String[2];

  @OnOpen
  public void onOpen(@PathParam("param1") String p1,
      @PathParam("param2") String p2) {
    p[0] = p1;
    p[1] = p2;
  }

  @OnMessage
  public String param(@PathParam("param1") String p1,
      @PathParam("param2") String p2, String content) throws IOException {
    OPS op = OPS.valueOf(content);
    switch (op) {
    case OPEN:
      content = p[0] + p[1];
      break;
    case MESSAGE:
      content = p1 + p2;
      break;
    case IOEXCEPTION:
      throw new IOException(ERR);
    case RUNTIMEEXCEPTION:
      throw new RuntimeException(ERR);
    }
    return content;
  }

  @OnError
  public void onError(@PathParam("param1") String p1,
      @PathParam("param2") String p2, Session session, Throwable t)
      throws IOException {
    String msg = t.getMessage();
    if (ERR.equals(msg)) {
      session.getBasicRemote().sendText(p1 + p2);
    } else {
      t.printStackTrace(); // Write to error log, too
      String message = IOUtil.printStackTrace(t);
      session.getBasicRemote().sendText(message);
    }
  }

  @OnClose
  public void onClose(@PathParam("param1") String p1,
      @PathParam("param2") String p2) {
    WSOnClosePathParamServer.set(0, p1);
    WSOnClosePathParamServer.set(1, p2);
  }
}
