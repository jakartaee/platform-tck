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

package com.sun.ts.tests.websocket.ee.javax.websocket.endpoint.client;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.Session;

import com.sun.ts.tests.websocket.common.client.StringClientEndpoint;
import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;

public class WSCErrorClientEndpoint extends StringClientEndpoint {
  private static final String EXCEPTION = "TCK test throwable";

  boolean onErrorCalled = false;

  @Override
  public void onMessage(String msg) {
    super.onMessage(msg);
    OPS op = OPS.valueOf(msg);
    switch (op) {
    case THROW:
      throw new RuntimeException(EXCEPTION);
    default:
      break;
    }
  }

  @Override
  public void onOpen(Session session, EndpointConfig config) {
    super.onOpen(session, config);
  }

  @OnClose
  public void onClose(Session session, CloseReason closeReason) {
    super.onClose(session, closeReason);
  }

  @Override
  public void onError(Session session, Throwable t) {
    String msg = WebSocketCommonClient.getCauseMessage(t);
    if (EXCEPTION.equals(msg)) {
      onErrorCalled = true;
      getCountDownLatch().countDown();
    } else
      super.onError(session, t);
  }

}
