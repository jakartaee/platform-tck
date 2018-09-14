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

package com.sun.ts.tests.ejb30.lite.singleton.concurrency.common;

import javax.ejb.Lock;
import javax.ejb.LockType;

abstract public class BeanBase implements ConcurrencyIF {

  private static final String msg = "Should not reach here. The interceptors should have returned "
      + "the result. Maybe the interceptors are not ignored?";

  // Interceptor3 in bm or cm, is bound to these methods in ejb-jar.xml

  @Lock(LockType.WRITE)
  public long getAndResetLockedSumFromInterceptor(String interceptorName) {
    throw new RuntimeException(msg);
  }

  @Lock(LockType.WRITE)
  public void addLockedFromInterceptor(String interceptorName, int num) {
    throw new RuntimeException(msg);
  }

  @Lock(LockType.READ)
  public long getAndResetUnlockedSumFromInterceptor(String interceptorName) {
    throw new RuntimeException(msg);
  }

  @Lock(LockType.READ)
  public void addUnlockedFromInterceptor(String interceptorName, int num) {
    throw new RuntimeException(msg);
  }
}
