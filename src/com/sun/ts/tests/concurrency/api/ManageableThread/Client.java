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

package com.sun.ts.tests.concurrency.api.ManageableThread;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;

import javax.enterprise.concurrent.ManageableThread;
import javax.enterprise.concurrent.ManagedThreadFactory;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.Serializable;
import java.util.Properties;

public class Client extends ServiceEETest implements Serializable {

  public Client() {
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    try {
      // do your setup if any here
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("cleanup");
  }

  /*
   * @testName: isShutdown
   * 
   * @assertion_ids: CONCURRENCY:JAVADOC:20;CONCURRENCY:SPEC:99.1;
   * 
   * @test_Strategy: Lookup default ManagedThreadFactory object and create new
   * thread. Check return value of method isShutdown of new thread.
   */
  public void isShutdown() throws Fault {
    boolean pass = false;
    try {
      InitialContext ctx = new InitialContext();
      ManagedThreadFactory mtf = (ManagedThreadFactory) ctx
          .lookup("java:comp/DefaultManagedThreadFactory");
      ManageableThread m = (ManageableThread) mtf
          .newThread(new TestRunnableWork());
      pass = !m.isShutdown();
    } catch (NamingException ne) {
      TestUtil.logErr("Failed to lookup default ContextService" + ne);
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
    }
    if (!pass)
      throw new Fault("isShutdown failed");
  }
}
