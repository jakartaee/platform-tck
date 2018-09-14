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

package com.sun.ts.tests.websocket.common.impl;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.websocket.SendHandler;
import javax.websocket.SendResult;

/**
 * This SendHandler implementation is used to wait maximum given number of
 * seconds for SendResult and onResult method called
 * 
 * The main goal is to pass SendHandler object to a caller code
 */
public class WaitingSendHandler implements SendHandler {
  private volatile CountDownLatch latch;

  private volatile SendResult result = null;

  public WaitingSendHandler() {
    latch = new CountDownLatch(1);
  }

  public SendResult waitForResult(long seconds) {
    try {
      latch.await(seconds, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      throw new RuntimeException("Wait has been interrupted", e);
    }
    if (latch != null && latch.getCount() != 0)
      throw new IllegalStateException("onResult has not been called on time");
    if (result == null)
      throw new IllegalStateException("SendResult is null");
    return result;
  }

  @Override
  public void onResult(SendResult result) {
    this.result = result;
    latch.countDown();
  }

}
