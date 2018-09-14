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

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/TCKTestServerPathParam/{param1}/{param2}")
public class WSTestServerPathParam {

  @OnOpen
  public void init(Session session) throws IOException {
    String message = "========WSTestServerPathParam opened";
    session.getBasicRemote().sendText(message);
  }

  @OnMessage
  public void respond(String message, Session session) {
    System.out
        .println("========WSTestServerPathParam received String:" + message);
    StringBuffer sb = new StringBuffer();

    try {
      session.getBasicRemote().sendText(
          "========WSTestServerPathParam received String: " + message);
      Map<String, String> pathparams = session.getPathParameters();
      Set<String> keys = pathparams.keySet();
      for (Object key : keys) {
        System.out.println(
            ";" + key.toString() + "=" + pathparams.get(key.toString()));
        sb.append(";" + key.toString() + "=" + pathparams.get(key.toString()));
      }
      session.getBasicRemote().sendText(
          "========WSTestServerPathParam: pathparams returned" + sb.toString());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @OnError
  public void onError(Session session, Throwable t) {
    System.out.println("WSTestServerPathParam onError");
    try {
      session.getBasicRemote()
          .sendText("========WSTestServerPathParam onError");
    } catch (Exception e) {
      e.printStackTrace();
    }
    t.printStackTrace();
  }
}
