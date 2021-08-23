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

import java.util.concurrent.Callable;

/**
 * the Runnable Task to check the context related job.
 */
public class CallableTask<T> extends RunnableTask implements Callable<T> {
  private final T expectedReturnValue;

  @Override
  public T call() {
    run();
    return expectedReturnValue;
  }

  /**
   * Construct the callable task with expected properties.
   * 
   * @param jndiName
   *          the jndi name set for env-entry, ignore jndi test if it is null.
   * @param jndiValue
   *          the jndi value set for jndiName
   * @param className
   *          class name to be loaded inside the task, ignore class loading test
   *          if it is null.
   * @param returned
   *          expected returned object.
   * @param blockTime
   *          block time(in millisecond) for this task.
   */
  public CallableTask(String jndiName, String jndiValue, String className,
      T returned, long blockTime) {
    super(jndiName, jndiValue, className, blockTime);
    this.expectedReturnValue = returned;
  }

  /**
   * Construct the callable task with expected properties.
   * 
   * @param jndiName
   *          the jndi name set for env-entry, ignore jndi test if it is null.
   * @param jndiValue
   *          the jndi value set for jndiName
   * @param className
   *          class name to be loaded inside the task, ignore class loading test
   *          if it is null.
   * @param returned
   *          expected returned object.
   */
  public CallableTask(String jndiName, String jndiValue, String className,
      T returned) {
    super(jndiName, jndiValue, className);
    this.expectedReturnValue = returned;
  }

}
