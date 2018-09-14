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

import javax.ejb.Lock;
import javax.ejb.LockType;

import com.sun.ts.tests.ejb30.common.helper.Helper;

/**
 * This class does not specify any @AccessTimeout metadata. Its subclass can
 * specify it at class- or method-level.
 */
abstract public class PlainAccessTimeoutBeanBase implements AccessTimeoutIF {
  @Lock(LockType.READ)
  public int longRead(long waitTimeMillis, int readVal) {
    Helper.busyWait(waitTimeMillis);
    return readVal;
  }

  @Lock(LockType.READ)
  public int longRead2(long waitTimeMillis, int readVal) {
    return longRead(waitTimeMillis, readVal);
  }

  @Lock(LockType.WRITE)
  public void longWrite(long waitTimeMillis) {
    Helper.busyWait(waitTimeMillis);
  }
}
