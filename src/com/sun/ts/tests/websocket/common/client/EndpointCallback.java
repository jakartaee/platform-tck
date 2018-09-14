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

package com.sun.ts.tests.websocket.common.client;

import java.util.concurrent.CountDownLatch;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.MessageHandler.Whole;
import javax.websocket.Session;

/**
 * Default empty implementation of merged functionality of {@link Endpoint}
 * interface with {@link MessageHandler} interface.
 * </p>
 * Depending on what {@link WebSocketCommonClient.Entity} is defined (partial,
 * whole), {@link Whole} or {@link MessageHandler.Partial} functionality is used
 * with onMessage
 */
public class EndpointCallback {

  public void onError(Session session, Throwable t) {
  }

  public void onMessage(Object o) {
  }

  public void onOpen(Session session, EndpointConfig config) {
  }

  public void onClose(Session session, CloseReason closeReason) {
  }

  public CountDownLatch getCountDownLatch() {
    return ClientEndpoint.getCountDownLatch();
  }

  public StringBuffer getMessageBuilder() {
    return ClientEndpoint.getMessageBuilder();
  }

  public Throwable getLastError() {
    return ClientEndpoint.getLastError();
  }
}
