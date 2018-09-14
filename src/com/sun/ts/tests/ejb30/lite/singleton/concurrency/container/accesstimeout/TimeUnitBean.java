/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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

/*
 * $Id$
 */
package com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.accesstimeout;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import java.util.concurrent.TimeUnit;
import javax.ejb.AccessTimeout;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.interceptor.Interceptors;

/**
 * The purpose of this class is to verify various TimeUnit can be processed as
 * valid.
 */
@Singleton(name = "TimeUnit")
@Startup
public class TimeUnitBean {
  // This method returns a resultVal immediately, but its interceptor around-
  // invoke method waits for waitVal millis.
  @AccessTimeout(value = 1000)
  @Interceptors(Interceptor1.class)
  public int timeoutInterceptor(long waitVal, int resultVal) {
    return resultVal;
  }

  @AccessTimeout(value = 0) // concurrent access not allowed
  public int zero(long waitVal, int resultVal) {
    return waitAndReturn(waitVal, resultVal);
  }

  @AccessTimeout(value = -1) // keep waiting
  public int minus1(long waitVal, int resultVal) {
    return waitAndReturn(waitVal, resultVal);
  }

  @AccessTimeout(value = 1000)
  public int defaults(long waitVal, int resultVal) {
    return waitAndReturn(waitVal, resultVal);
  }

  // 1 billion nano is 1 second
  @AccessTimeout(unit = TimeUnit.NANOSECONDS, value = 1000000000)
  public int nanoseconds(long waitVal, int resultVal) {
    return waitAndReturn(waitVal, resultVal);
  }

  // 1 million micro is 1 second
  @AccessTimeout(unit = TimeUnit.MICROSECONDS, value = 1000000)
  public int microseconds(long waitVal, int resultVal) {
    return waitAndReturn(waitVal, resultVal);
  }

  @AccessTimeout(unit = TimeUnit.SECONDS, value = 1)
  public int seconds(long waitVal, int resultVal) {
    return waitAndReturn(waitVal, resultVal);
  }

  @AccessTimeout(unit = TimeUnit.DAYS, value = 1)
  public int days(long waitVal, int resultVal) {
    return waitAndReturn(waitVal, resultVal);
  }

  @AccessTimeout(unit = TimeUnit.HOURS, value = 1)
  public int hours(long waitVal, int resultVal) {
    return waitAndReturn(waitVal, resultVal);
  }

  @AccessTimeout(unit = TimeUnit.MINUTES, value = Integer.MAX_VALUE)
  public int minutes(long waitVal, int resultVal) {
    return waitAndReturn(waitVal, resultVal);
  }

  private int waitAndReturn(long waitVal, int resultVal) {
    Helper.busyWait(waitVal);
    return resultVal;
  }
}
