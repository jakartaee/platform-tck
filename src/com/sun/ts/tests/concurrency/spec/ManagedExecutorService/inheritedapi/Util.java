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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.sun.ts.lib.util.TestUtil;

public class Util {

  private static final String MANAGED_EXECUTOR_SVC_JNDI_NAME = "java:comp/DefaultManagedExecutorService";

  private Util() {
  }

  public static <T> T waitForTaskComplete(final Future<T> future,
      final int maxTaskWaitTime)
      throws InterruptedException, ExecutionException, TimeoutException {
    T result = null;
    result = future.get(maxTaskWaitTime, TimeUnit.SECONDS);
    return result;
  }

  public static ManagedExecutorService getManagedExecutorService() {
    return lookup(MANAGED_EXECUTOR_SVC_JNDI_NAME);
  }

  public static <T> T lookup(String jndiName) {
    Context ctx = null;
    T targetObject = null;
    try {
      ctx = new InitialContext();
      targetObject = (T) ctx.lookup(jndiName);
    } catch (Exception e) {
    } finally {
      try {
        ctx.close();
      } catch (NamingException e) {
        TestUtil.logErr("failed to lookup resource.", e);
      }
    }
    return targetObject;
  }

  public static String getUrl(String servletUri, String host, int port) {
    return "http://" + host + ":" + port + Constants.CONTEXT_PATH + servletUri;
  }

}
