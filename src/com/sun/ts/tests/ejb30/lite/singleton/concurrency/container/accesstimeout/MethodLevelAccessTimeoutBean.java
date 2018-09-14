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

import java.util.concurrent.TimeUnit;

import javax.ejb.AccessTimeout;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 * The purpose of this class is to verify method-level @AccessTimeout. This
 * class has no class-level @AccessTimeout metadata. Its superclass,
 * PlainAccessTimeoutBeanBase, contains no AccessTimeout metadata.
 */
@Singleton
@Startup
@Lock(LockType.READ)
public class MethodLevelAccessTimeoutBean extends PlainAccessTimeoutBeanBase
    implements AccessTimeoutIF {
  @Override
  @Lock(LockType.READ)
  @AccessTimeout(unit = TimeUnit.MILLISECONDS, value = 1000)
  public int longRead(long waitTimeMillis, int readVal) {
    return super.longRead(waitTimeMillis, readVal);
  }

  @Override
  @Lock(LockType.WRITE)
  @AccessTimeout(unit = TimeUnit.MILLISECONDS, value = 1000)
  public void longWrite(long waitTimeMillis) {
    super.longWrite(waitTimeMillis);
  }

}
