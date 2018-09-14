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

import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.sun.ts.tests.websocket.common.util.IOUtil;

@ServerEndpoint(value = "/onclose")
public class WSOnClosePathParamServer {

  public static final String RESET = "path params has been reset";

  private static final String[] p = new String[11];

  @OnMessage
  public String param(String content) {
    int i = Integer.parseInt(content);
    if (i == -1) {
      for (int j = 0; j != p.length; j++)
        p[j] = "";
      return RESET;
    }
    return get(i);
  }

  public static final void set(int i, String param) {
    p[i] = param;
  }

  private static final String get(int i) {
    return p[i];
  }

  @OnError
  public void onError(Session session, Throwable t) throws IOException {
    t.printStackTrace(); // Write to error log, too
    String message = IOUtil.printStackTrace(t);
    session.getBasicRemote().sendText(message);
  }

}
