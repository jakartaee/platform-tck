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

package com.sun.ts.tests.concurrency.api.common;

import java.util.concurrent.*;

public class CommonTasks {

  public static final String SIMPLE_RETURN_STRING = "ok";

  public static class SimpleCallable implements Callable {
    private long waitTime = 0;

    public SimpleCallable() {
    }

    public SimpleCallable(long argWaitTime) {
      this.waitTime = argWaitTime;
    }

    public String call() {
      try {
        if (waitTime != 0) {
          Thread.sleep(waitTime);
        } else {
          Thread.sleep(Util.COMMON_CHECK_INTERVAL);
        }
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      return SIMPLE_RETURN_STRING;
    }
  }

  public static class SimpleRunnable implements Runnable {
    public void run() {
      try {
        Thread.sleep(Util.COMMON_CHECK_INTERVAL);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }

  public static class SimpleArgCallable implements Callable {
    private int value = -1;

    public SimpleArgCallable(int arg) {
      value = arg;
    }

    public Integer call() {
      try {
        Thread.sleep(Util.COMMON_CHECK_INTERVAL);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      return value;
    }
  }
}
