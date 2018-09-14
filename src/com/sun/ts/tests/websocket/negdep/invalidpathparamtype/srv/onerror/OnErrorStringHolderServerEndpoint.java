/*
 * Copyright (c) 2015, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.websocket.negdep.invalidpathparamtype.srv.onerror;

import java.io.IOException;

import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.sun.ts.tests.websocket.negdep.StringHolder;

@ServerEndpoint("/invalid/{arg}")
public class OnErrorStringHolderServerEndpoint {
  private static String exception = "";

  @OnMessage
  public String echo(String echo) {
    if ("throw".equals(echo))
      throw new RuntimeException(echo);
    return exception + echo;
  }

  @OnError
  public void onError(Session session, Throwable t,
      @PathParam("arg") StringHolder sb) throws IOException {
    exception = sb.toString();
    session.getBasicRemote().sendText(t.getMessage());
  }
}
