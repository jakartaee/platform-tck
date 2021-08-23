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

package com.sun.ts.tests.concurrency.spec.ManagedExecutorService.inheritedapi;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Task<T> implements Runnable, Callable<T> {
  protected boolean ran;

  protected final int taskId;

  protected final AtomicInteger count = new AtomicInteger(0);

  public Task(int id) {
    taskId = id;
  }

  boolean isRan() {
    return ran;
  }

  public int runCount() {
    return count.get();
  }

  static class CommonTask extends Task<Integer> {
    public CommonTask(int id) {
      super(id);
    }

    @Override
    public void run() {
      // each time add count when run called
      count.incrementAndGet();
      ran = true;
    }

    @Override
    public Integer call() throws Exception {
      ran = true;
      return taskId;
    }
  }
}
