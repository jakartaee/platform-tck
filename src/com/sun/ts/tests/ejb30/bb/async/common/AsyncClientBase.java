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

package com.sun.ts.tests.ejb30.bb.async.common;

import javax.ejb.EJB;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;
import com.sun.ts.tests.ejb30.common.statussingleton.StatusSingletonBean;

abstract public class AsyncClientBase extends EJBLiteClientBase {

  private static final long DEFAULT_MAX_WAIT_MILLIS = 1000 * 60;

  private static final long POLL_INTERVAL_MILLIS = 500;

  @EJB(beanName = "StatusSingletonBean")
  protected StatusSingletonBean statusSingleton;

  protected Integer getAndResetResult(Integer key, long... maxWaitMillis) {
    final long waitFor = maxWaitMillis.length == 0 ? DEFAULT_MAX_WAIT_MILLIS
        : maxWaitMillis[0];
    final long stopTime = System.currentTimeMillis() + waitFor;
    boolean avail = statusSingleton.isResultAvailable(key);
    while (!avail && System.currentTimeMillis() < stopTime) {
      TestUtil.sleep((int) POLL_INTERVAL_MILLIS);
      avail = statusSingleton.isResultAvailable(key);
    }
    return statusSingleton.getAndResetResult(key);
  }
}
