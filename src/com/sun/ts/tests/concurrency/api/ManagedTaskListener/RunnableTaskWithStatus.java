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

package com.sun.ts.tests.concurrency.api.ManagedTaskListener;

import java.util.concurrent.TimeUnit;

import com.sun.ts.tests.concurrency.api.common.managedTaskListener.ListenerEvent;
import com.sun.ts.tests.concurrency.api.common.managedTaskListener.ManagedTaskListenerImpl;

public class RunnableTaskWithStatus implements Runnable {
  private final ManagedTaskListenerImpl listener;

  private final int blockTime;

  public RunnableTaskWithStatus(ManagedTaskListenerImpl listener) {
    this.listener = listener;
    blockTime = 0;
  }

  RunnableTaskWithStatus(ManagedTaskListenerImpl listener, int blockTime) {
    this.listener = listener;
    this.blockTime = blockTime;
  }

  public void run() {
    listener.update(ListenerEvent.TASK_RUN);
    if (blockTime > 0) {
      try {
        TimeUnit.SECONDS.sleep(blockTime);
      } catch (InterruptedException e) {
      }
    }
  }

}
