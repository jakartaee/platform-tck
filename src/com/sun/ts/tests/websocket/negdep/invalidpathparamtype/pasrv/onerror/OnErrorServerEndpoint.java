/*
 * Copyright (c) 2015, 2020 Oracle and/or its affiliates and others.
 * All rights reserved.
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

package com.sun.ts.tests.websocket.negdep.invalidpathparamtype.pasrv.onerror;

import java.io.IOException;

import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import com.sun.ts.tests.websocket.common.stringbean.StringBean;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanTextDecoder;

@ServerEndpoint(value = "/invalid/{arg}", decoders = {
    StringBeanTextDecoder.class })
public class OnErrorServerEndpoint {
  private static String exception = "";

  @OnMessage
  public String echo(String echo) {
    if ("throw".equals(echo))
      throw new RuntimeException(echo);
    return exception + echo;
  }

  // This header makes the endpoint invalid, since only Strings can be
  // @PathParams
  @OnError
  public void onError(Session session, Throwable t,
      @PathParam("arg") StringBean sb) throws IOException {
    exception = sb.get();
    session.getBasicRemote().sendText(t.getMessage());
  }
}
