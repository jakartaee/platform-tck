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

@ServerEndpoint(value = "/param/{param1}/{param2}/{param3}/{param4}/{param5}/{param6}/{param7}/{param8}/{param9}")
public class WS9StringPathParamServer {
  private final static String ERR = "TCK INTENDED ERROR";

  private String[] p = new String[9];

  @OnOpen
  public void onOpen(@PathParam("param1") String p1,
      @PathParam("param2") String p2, @PathParam("param3") String p3,
      @PathParam("param4") String p4, @PathParam("param5") String p5,
      @PathParam("param6") String p6, @PathParam("param7") String p7,
      @PathParam("param8") String p8, @PathParam("param9") String p9) {
    p[0] = p1;
    p[1] = p2;
    p[2] = p3;
    p[3] = p4;
    p[4] = p5;
    p[5] = p6;
    p[6] = p7;
    p[7] = p8;
    p[8] = p9;
  }

  @OnMessage
  public String param(@PathParam("param1") String p1,
      @PathParam("param2") String p2, @PathParam("param3") String p3,
      @PathParam("param4") String p4, @PathParam("param5") String p5,
      @PathParam("param6") String p6, @PathParam("param7") String p7,
      @PathParam("param8") String p8, @PathParam("param9") String p9,
      String content) throws IOException {
    OPS op = OPS.valueOf(content);
    switch (op) {
    case OPEN:
      content = p[0] + p[1] + p[2] + p[3] + p[4] + p[5] + p[6] + p[7] + p[8];
      break;
    case MESSAGE:
      content = p1 + p2 + p3 + p4 + p5 + p6 + p7 + p8 + p9;
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
      @PathParam("param2") String p2, @PathParam("param3") String p3,
      @PathParam("param4") String p4, @PathParam("param5") String p5,
      @PathParam("param6") String p6, @PathParam("param7") String p7,
      @PathParam("param8") String p8, @PathParam("param9") String p9,
      Session session, Throwable t) throws IOException {
    String msg = t.getMessage();
    if (ERR.equals(msg)) {
      session.getBasicRemote()
          .sendText(p1 + p2 + p3 + p4 + p5 + p6 + p7 + p8 + p9);
    } else {
      t.printStackTrace(); // Write to error log, too
      String message = IOUtil.printStackTrace(t);
      session.getBasicRemote().sendText(message);
    }
  }

  @OnClose
  public void onClose(@PathParam("param1") String p1,
      @PathParam("param2") String p2, @PathParam("param3") String p3,
      @PathParam("param4") String p4, @PathParam("param5") String p5,
      @PathParam("param6") String p6, @PathParam("param7") String p7,
      @PathParam("param8") String p8, @PathParam("param9") String p9) {
    WSOnClosePathParamServer.set(0, p1);
    WSOnClosePathParamServer.set(1, p2);
    WSOnClosePathParamServer.set(2, p3);
    WSOnClosePathParamServer.set(3, p4);
    WSOnClosePathParamServer.set(4, p5);
    WSOnClosePathParamServer.set(5, p6);
    WSOnClosePathParamServer.set(6, p7);
    WSOnClosePathParamServer.set(7, p8);
    WSOnClosePathParamServer.set(8, p9);
  }
}
