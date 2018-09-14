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

import java.util.concurrent.CountDownLatch;

import javax.websocket.EndpointConfig;
import javax.websocket.Session;

public class OuterEndpoint extends InnerEndpoint {
  private InnerEndpoint innerEndpoint;

  public OuterEndpoint(InnerEndpoint innerEndpoint, CountDownLatch latch) {
    super(latch);
    this.innerEndpoint = innerEndpoint;
  }

  @Override
  public void onOpen(Session session, EndpointConfig config) {
    session.addMessageHandler(this);
  }

  @Override
  public void onMessage(String message) {
    this.receivedMessage += message;
    innerEndpoint.sendMessage("anything");
    innerEndpoint.await(5L);
    latch.countDown();
  }
}
