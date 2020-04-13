/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates and others.
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

package com.sun.ts.tests.websocket.ee.jakarta.websocket.endpoint.client;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import jakarta.websocket.CloseReason;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.Session;

import com.sun.ts.tests.websocket.common.client.StringClientEndpoint;

public class WSCCloseClientEndpoint extends StringClientEndpoint {
  boolean onCloseCalled = false;

  private CountDownLatch countDown = new CountDownLatch(1);

  @Override
  public void onMessage(String msg) {
    super.onMessage(msg);
  }

  @Override
  public void onOpen(Session session, EndpointConfig config) {
    super.onOpen(session, config);
  }

  @Override
  public void onClose(Session session, CloseReason closeReason) {
    super.onClose(session, closeReason);
    onCloseCalled = true;
    countDown.countDown();
  }

  public void onError(Session session, Throwable t) {
    super.onError(session, t);
  }

  public void waitForClose(long seconds) {
    try {
      countDown.await(seconds, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

}
