/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.websocket.negdep.onopen.client.duplicate;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import com.sun.ts.tests.websocket.common.client.AnnotatedStringClientEndpoint;

@ClientEndpoint
public class AnnotatedOnOpenClientEndpoint
    extends AnnotatedStringClientEndpoint {

  @OnOpen
  public void onOpen(Session session, EndpointConfig config) {
    clientEndpoint.onOpen(session, config, false);
  }

  @OnOpen
  public void onOpen2(Session session, EndpointConfig config) {
    clientEndpoint.onOpen(session, config, false);
  }

  @OnMessage
  public void onMessage(String msg) {
    clientEndpoint.onMessage(msg);
  }

  @OnClose
  public void onClose(Session session, CloseReason closeReason) {
    clientEndpoint.onClose(session, closeReason);
  }

  @OnError
  public void onError(Session session, Throwable t) {
    clientEndpoint.onError(session, t);
  }
}
