/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * @(#)Client.java	1.19 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.session.stateless.reentranttest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.util.*;
import javax.ejb.*;
import javax.transaction.*;
import java.rmi.*;

import com.sun.javatest.Status;

public class Client extends EETest implements Runnable {
  private static final String testName = "ReEntrantTest";

  private static final String testLookup = "java:comp/env/ejb/TestBean";

  private static final String clientLookup = "java:comp/env/ejb/ClientBean";

  private static final int SLEEP_TIME = 10000;

  private TestBean beanRef = null;

  private TestBeanHome beanHome = null;

  private ClientBean beanRef2 = null;

  private ClientBeanHome beanHome2 = null;

  private Properties props = null;

  private TSNamingContext nctx = null;

  private int successes = 0;

  private boolean isRemote = true;

  public void run() {
    logMsg("Thread: " + Thread.currentThread().getName());
    try {
      beanRef.sleep(SLEEP_TIME);
      incrementSuccesses();
    } catch (RemoteException e) {
      logErr(
          "Thread: " + Thread.currentThread().getName() + ", Exception: " + e,
          e);
    }
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    try {
      logMsg("Obtain naming context");
      nctx = new TSNamingContext();

      // Get EJB Home ...
      logMsg("Looking up home interface for EJB: " + testLookup);
      beanHome = (TestBeanHome) nctx.lookup(testLookup, TestBeanHome.class);

      TestUtil.logMsg(
          "Looking up home interface for ClientBean EJB: " + clientLookup);
      beanHome2 = (ClientBeanHome) nctx.lookup(clientLookup,
          ClientBeanHome.class);

      logMsg("Setup ok");
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /* Run test */

  /*
   * @testName: test1
   * 
   * @assertion_ids: EJB:SPEC:69.1
   * 
   * @test_Strategy: Container must ensure that multiple threads can be
   * executing an instance at any time for a STATELESS sessionbean (i.e.
   * reentrant). Create a stateless Session Bean. Deploy it on the J2EE server.
   * Spawn 2 threads and attempt to call same bean. Both threads should succeed.
   *
   */

  public void test1() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create();
      beanRef.initLogging(props);
      logMsg("Creating Threads T1 and T2");
      Thread t1 = new Thread(this, "T1");
      Thread t2 = new Thread(this, "T2");
      logMsg("Starting Threads T1 and T2");
      t1.start();
      t2.start();
      try {
        t1.join();
      } catch (Exception je) {
        TestUtil.logMsg("test1: Unexpected Exception caught during t1.join");
        je.printStackTrace();
      }

      try {
        t2.join();
      } catch (Exception le) {
        TestUtil.logMsg("test1: Unexpected Exception caught during t2.join");
        le.printStackTrace();
      }

      logMsg("check to ensure both threads succeed - reentrant");
      if (successes != 2) {
        pass = false;
        logMsg("ERROR: test1 - did not get the expected results, "
            + "Expected successes = 2, got: " + successes);
      } else {
        pass = true;
        logMsg("test1 - Expected results received, "
            + "Expected successes = 2, got: " + successes);
      }

    } catch (Exception e) {
      throw new Fault("test1 failed", e);
    } finally {
      try {
        if (null != beanRef) {
          beanRef.remove();
        }
      } catch (Exception re) {
        TestUtil.logMsg("Exception caught while removing bean");
        re.printStackTrace();
      }
    }

    if (!pass)
      throw new Fault("test1 failed");

  }

  /*
   * @testName: test2
   * 
   * @assertion_ids: EJB:SPEC:69.1
   * 
   * @test_Strategy: It is possible for an application to make loopback calls to
   * a session STATELESS instance. This test uses same bean instance. Create a
   * stateless Session EJBean. Deploy it on the J2EE server. Call loopback test
   * on same bean. Self referential test.
   *
   */

  public void test2() throws Fault {
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create();
      beanRef.initLogging(props);
      logMsg("Calling loopback test via same bean");
      boolean pass = beanRef.loopBackSameBean();
      beanRef.remove();
      if (!pass)
        throw new Fault("test2 failed");
    } catch (Exception e) {
      throw new Fault("test2 failed", e);
    }
  }

  /*
   * @testName: test3
   * 
   * @assertion_ids: EJB:SPEC:69.1
   * 
   * @test_Strategy: It is possible for an application to make loopback calls to
   * a session STATELESS instance (i.e. reentrant). This test uses another bean
   * instance. Create a stateless Session EJBean. Deploy it on the J2EE server.
   * Call loopback test on first bean which in turn calls a second bean which
   * then calls first bean again.
   *
   *
   */

  public void test3() throws Fault {
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create();
      beanRef.initLogging(props);
      logMsg("Calling loopback test via different bean");
      boolean pass = beanRef.loopBackAnotherBean(props);
      beanRef.remove();
      if (!pass)
        throw new Fault("test3 failed");
    } catch (Exception e) {
      throw new Fault("test3 failed", e);
    }
  }

  /*
   * @testName: test4
   * 
   * @assertion_ids: EJB:SPEC:69.1
   * 
   * @test_Strategy: It is possible for an application to make loopback calls to
   * a session STATELESS instance (i.e reentrant). This test uses same bean
   * instance. Create a stateless Session EJBean. Deploy it on the J2EE server.
   * Call loopback test on same bean. Self referential test.
   *
   */

  public void test4() throws Fault {
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef2 = (ClientBean) beanHome2.create(props);
      logMsg("Calling loopback test via same bean");
      boolean pass = beanRef2.loopBackSameBeanLocal();
      beanRef2.remove();
      if (!pass)
        throw new Fault("test4 failed");
    } catch (Exception e) {
      throw new Fault("test4 failed", e);
    }
  }

  /*
   * @testName: test5
   * 
   * @assertion_ids: EJB:SPEC:69.1
   * 
   * @test_Strategy: It is possible for an application to make loopback calls to
   * a session STATELESS instance (i.e. reentrant). This test uses another bean
   * instance. Create a stateless Session EJBean. Deploy it on the J2EE server.
   * Call loopback test on first bean which in turn calls a second bean which
   * then calls first bean again.
   *
   *
   */

  public void test5() throws Fault {
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef2 = (ClientBean) beanHome2.create(props);

      logMsg("Calling loopback test via different bean");
      boolean pass = beanRef2.loopBackAnotherBeanLocal();
      beanRef2.remove();
      if (!pass)
        throw new Fault("test5 failed");
    } catch (Exception e) {
      throw new Fault("test5 failed", e);
    }
  }

  private synchronized void incrementSuccesses() {
    successes++;
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }
}
