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

package com.sun.ts.tests.concurrency.api.common.managedTaskListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.concurrent.ManagedTaskListener;
import com.sun.ts.lib.util.TestUtil;

public class ManagedTaskListenerImpl implements ManagedTaskListener {

  private final List<ListenerEvent> events = Collections
      .synchronizedList(new ArrayList<ListenerEvent>());

  @Override
  public void taskAborted(Future<?> future, ManagedExecutorService mes,
      Object arg2, Throwable arg3) {
    events.add(ListenerEvent.ABORTED);
    TestUtil.logTrace("task aborted");
  }

  @Override
  public void taskDone(Future<?> future, ManagedExecutorService mes,
      Object arg2, Throwable arg3) {
    events.add(ListenerEvent.DONE);
    TestUtil.logTrace("task done");
  }

  @Override
  public void taskStarting(Future<?> future, ManagedExecutorService mes,
      Object arg2) {
    events.add(ListenerEvent.STARTING);
    TestUtil.logTrace("task starting");
  }

  @Override
  public void taskSubmitted(Future<?> future, ManagedExecutorService mes,
      Object arg2) {
    events.add(ListenerEvent.SUBMITTED);
    TestUtil.logTrace("task submitted");
  }

  public boolean eventCalled(ListenerEvent event) {
    return events.contains(event);
  }

  public void clearEvents() {
    events.clear();
  }

  public void update(ListenerEvent event) {
    events.add(event);
  }

  public List<ListenerEvent> events() {
    return events;
  }
}
