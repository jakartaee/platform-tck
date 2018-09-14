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

package com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.usercoder.asyncwithhandler;

import javax.websocket.EndpointConfig;
import javax.websocket.RemoteEndpoint;
import javax.websocket.SendResult;
import javax.websocket.Session;

import com.sun.ts.tests.websocket.common.client.EndpointCallback;
import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient.Entity;
import com.sun.ts.tests.websocket.common.impl.WaitingSendHandler;

public class AsyncEndpointCallback extends EndpointCallback {
  protected Entity entity;

  public AsyncEndpointCallback(Entity entity) {
    super();
    this.entity = entity;
  }

  @Override
  public void onOpen(Session session, EndpointConfig config) {
    super.onOpen(session, config);
    RemoteEndpoint.Async endpoint = session.getAsyncRemote();
    SendResult result = null;
    try {
      WaitingSendHandler handler = new WaitingSendHandler();
      endpoint.sendObject(entity.getEntityAt(0), handler);
      result = handler.waitForResult(4);
    } catch (Exception i) {
      throw new RuntimeException(i);
    }
    if (!result.isOK() || result.getException() != null)
      throw new RuntimeException(result.getException());
  }
}
