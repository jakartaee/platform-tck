/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.lite.stateful.concurrency.common;

import java.util.ArrayList;
import java.util.List;

public class Pinger extends Thread {
  protected Exception exception;

  private StatefulConcurrencyIF bean;

  protected Runnable runnable;

  public Pinger() {
  }

  public Pinger(StatefulConcurrencyIF bean) {
    this.bean = bean;
  }

  public Pinger(Runnable runnable) {
    super(runnable);
    this.runnable = runnable;
  }

  @Override
  public void run() {
    if (runnable != null) {
      try {
        super.run();
      } catch (Exception e) {
        exception = e;
      }
      return;
    }

    try {
      bean.ping();
    } catch (Exception e) {
      exception = e;
    }
  }

  public Exception getException() {
    return exception;
  }

  public static List<Exception> getExceptionAsList(Pinger... pingers) {
    List<Exception> result = new ArrayList<Exception>();
    for (Pinger p : pingers) {
      result.add(p.getException());
    }
    return result;
  }
}
