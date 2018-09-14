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

package com.sun.ts.tests.websocket.negdep.onmessage.client.nodecoder;

//import java.nio.ByteBuffer;

import java.nio.ByteBuffer;

import javax.websocket.ClientEndpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import com.sun.ts.tests.websocket.common.client.AnnotatedByteBufferClientEndpoint;
import com.sun.ts.tests.websocket.negdep.StringHolder;

/**
 * There is no tell whether @OnMessage on this endpoint is accepting binary or
 * text messages, there is not Decoder.
 */
@ClientEndpoint
public class OnMessageClientEndpoint extends AnnotatedByteBufferClientEndpoint {

  @OnMessage
  public void onMessage(StringHolder holder) {
    clientEndpoint.onMessage(ByteBuffer.wrap(holder.toString().getBytes()));
  }

  @OnError
  public void onError(Session session, Throwable t) {
    clientEndpoint.onError(session, t);
  }

  @OnOpen
  public void onOpen(Session session, EndpointConfig config) {
    clientEndpoint.onOpen(session, config, false);
  }
}
