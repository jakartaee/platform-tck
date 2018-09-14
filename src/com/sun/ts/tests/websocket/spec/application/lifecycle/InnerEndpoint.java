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

package com.sun.ts.tests.websocket.spec.application.lifecycle;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

public class InnerEndpoint extends Endpoint
    implements MessageHandler.Whole<String> {

  protected String receivedMessage = "";

  protected CountDownLatch latch;

  private Session session;

  public InnerEndpoint(CountDownLatch latch) {
    super();
    this.latch = latch;
  }

  @Override
  public void onOpen(Session session, EndpointConfig config) {
    session.addMessageHandler(this);
    this.session = session;
  }

  @Override
  public void onMessage(String message) {
    this.receivedMessage += message;
    latch.countDown();
  }

  public String getReceivedMessage() {
    return receivedMessage;
  }

  public void sendMessage(String message) {
    try {
      session.getBasicRemote().sendText(message);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void await(long seconds) {
    try {
      latch.await(seconds, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

}
