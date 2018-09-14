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

package com.sun.ts.tests.common.connector.whitebox.mixedmode;

import javax.resource.spi.BootstrapContext;
import javax.resource.spi.work.WorkManager;
import javax.resource.spi.work.ExecutionContext;
import javax.resource.spi.work.WorkException;
import com.sun.ts.tests.common.connector.whitebox.WorkImpl;
import com.sun.ts.tests.common.connector.whitebox.WorkListenerImpl;
import com.sun.ts.tests.common.connector.whitebox.Debug;

public class PMDWorkManager {
  private BootstrapContext bsc = null;

  private WorkManager wmgr;

  private String sicUser = "";

  private String sicPwd = "";

  private String eisUser = "";

  public PMDWorkManager(BootstrapContext val) {
    debug("enterred constructor");
    this.bsc = val;
    this.wmgr = bsc.getWorkManager();

    this.sicUser = System.getProperty("j2eelogin.name");
    this.sicPwd = System.getProperty("j2eelogin.password");
    this.eisUser = System.getProperty("eislogin.name");
    debug("leaving constructor");
  }

  public void runTests() {
    debug("enterred runTests");
    doWork();
    debug("leaving runTests");
  }

  public void doWork() {
    debug("enterred doWork");

    try {
      WorkImpl workimpl = new WorkImpl(wmgr);

      ExecutionContext ec = new ExecutionContext();
      WorkListenerImpl wl = new WorkListenerImpl();
      wmgr.doWork(workimpl, 5000, ec, wl);
      debug("PMDWorkManager Work Object Submitted");
    } catch (WorkException we) {
      System.out
          .println("PMDWorkManager WorkException thrown is " + we.getMessage());
    } catch (Exception ex) {
      System.out
          .println("PMDWorkManager Exception thrown is " + ex.getMessage());
    }

    debug("leaving doWork");
  }

  public void debug(String out) {
    Debug.trace("PMDWorkManager:  " + out);
  }

}
