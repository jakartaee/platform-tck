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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.AccessTimeout;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import com.sun.ts.tests.ejb30.common.helper.Helper;

/**
 * The purpose of this class is to verify the wait time in post-construct method
 * does not count towards AccessTimeout. This bean is not annotated
 * with @Startup.
 * 
 * @AccessTimeout at class-level also applies to the PostConstruct, AroundInvoke
 *                and PreDestroy methods that are exposed as business methods.
 */
@Singleton
@AccessTimeout(value = 1000)
@Lock(LockType.READ)
public class ClassLevelCallbackAccessTimeoutBean
    extends CallbackAccessTimeoutBeanBase {
  @Override
  public int postConstructWait(int resultVal) {
    return super.postConstructWait(resultVal);
  }

  @Override
  @PostConstruct
  public void postConstruct() {
    super.postConstruct();
  }

  @Override
  @PreDestroy
  public void preDestroy() {
    super.preDestroy();
  }

  @Override
  @AroundInvoke
  public Object intercept(InvocationContext inv) throws Exception {
    return super.intercept(inv);
  }

  protected void busyWait(long waitMillis, String methodName) {
    Helper.getLogger()
        .fine("Waiting in " + methodName + " for millis " + waitMillis);
    Helper.busyWait(waitMillis);
  }

  protected static final long ACCESS_TIMEOUT_MILLIS = 1000;

  protected static final long AROUND_INVOKE_WAIT_MILLIS = 600;
}
