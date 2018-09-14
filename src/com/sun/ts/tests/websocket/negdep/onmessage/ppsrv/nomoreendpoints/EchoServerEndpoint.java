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

package com.sun.ts.tests.websocket.negdep.onmessage.ppsrv.nomoreendpoints;

import java.io.IOException;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

import com.sun.ts.tests.websocket.common.util.IOUtil;

public class EchoServerEndpoint extends Endpoint
    implements MessageHandler.Whole<String> {
  private Session session;

  @Override
  public void onOpen(Session session, EndpointConfig config) {
    this.session = session;
    session.addMessageHandler(this);
  }

  @Override
  public void onMessage(String message) {
    try {
      session.getBasicRemote().sendText(message);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void onError(Session session, Throwable thr) {
    thr.printStackTrace(); // Write to error log, too
    String message = IOUtil.printStackTrace(thr);
    try {
      session.getBasicRemote().sendText(message);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
