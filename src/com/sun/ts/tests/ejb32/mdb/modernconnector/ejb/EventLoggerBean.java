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

package com.sun.ts.tests.ejb32.mdb.modernconnector.ejb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.ejb.*;

@Startup
@Singleton
@LocalBean
@Lock(LockType.READ)
public class EventLoggerBean implements EventLoggerRemote {

  private final CountDownLatch expected = new CountDownLatch(6);

  private final List<String> eventRecords = new ArrayList<String>();

  public List<String> getEventRecords() {
    return Collections.unmodifiableList(eventRecords);
  }

  public void logEvent(String event) {
    eventRecords.add(event);
    expected.countDown();
  }

  public boolean awaitInvocations() {
    try {
      return expected.await(1, TimeUnit.MINUTES);
    } catch (InterruptedException e) {
      Thread.interrupted();
      return false;
    }
  }
}
