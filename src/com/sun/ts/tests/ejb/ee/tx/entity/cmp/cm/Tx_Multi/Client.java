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
 * $Id$
 */

package com.sun.ts.tests.ejb.ee.tx.entity.cmp.cm.Tx_Multi;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.util.*;
import javax.ejb.*;
import javax.naming.*;
import java.rmi.*;
import com.sun.ts.tests.ejb.ee.tx.txECMPbean.*;

import com.sun.javatest.Status;

public class Client extends EETest {

  private static final int SLEEPTIME = 5000;

  private static final String testName = "Tx_Multi";

  private static final String tLookupS1 = "java:comp/env/ejb/TestBean";

  private static final String tLookupER = "java:comp/env/ejb/TxRequired";

  private static Properties testProps = null;

  private static TSNamingContext nctx = null;

  private String tName1 = null;

  private S1TestBeanHome s1Home = null;

  private TxECMPBeanHome ERHome = null;

  private S1TestBean beanRefS1 = null;

  private S1TestBean beanRefS2 = null;

  private TxECMPBean beanRefER = null;

  private Integer pkeyR = null;

  private Integer pkeyM = null;

  // For thread synchronization
  private static final int NUMLOOPS = 3;

  private static final int NTHREADS = 2;

  private static Object lock = new Object();

  private static Object startLock = new Object();

  private static Object workLock = new Object();

  private static int threadsDone = 0;

  private static int errors = 0;

  // Inner class TestThread
  class TestThread implements Runnable {
    // Instance variables
    private int threadNum = 0;

    private int testNum = 0;

    private boolean synchronize = true;

    // n represents the number of threads
    // t is the test case number id
    public TestThread(int n, int t) {
      this.threadNum = n;
      this.testNum = t;
    }

    public void run() {
      synchronized (lock) {
        ++threadsDone;
        lock.notifyAll();
      }

      synchronized (startLock) {
        try {
          startLock.wait();
        } catch (InterruptedException ignore) {
        }
      }

      boolean testResult = false;

      if (testNum == 1)
        testResult = runTest1();
      if (testNum == 3)
        testResult = runTest3();

      synchronized (lock) {
        if (!testResult)
          ++errors;
        ++threadsDone;
        lock.notifyAll();
      }
    }
  }

  /* Test setup */

  /*
   * @class.setup_props: java.naming.factory.initial;
   * 
   * @class.testArgs: -ap tssql.stmt
   */
  public void setup(String[] args, Properties p) throws Fault {
    logMsg("setup");
    testProps = p;

    try {
      TestUtil.logTrace("Getting the initial context");
      nctx = new TSNamingContext();

      // Lookup table names
      tName1 = TestUtil.getTableName(TestUtil.getProperty("TxEBean_Delete"));
      logMsg("Looking up table name " + tName1);

      // Get the first Session EJB Home and create an instance
      logMsg("Looking up home interface for EJB: " + tLookupS1);
      s1Home = (S1TestBeanHome) nctx.lookup(tLookupS1, S1TestBeanHome.class);
      logMsg("Creating Session S1 EJB");
      beanRefS1 = (S1TestBean) s1Home.create(testProps);

      // Get the second Session EJB instance
      beanRefS2 = (S1TestBean) s1Home.create(testProps);

      // Get the Entity EJB Home and create an instance - Required
      pkeyR = new Integer(20);
      logMsg("Looking up home interface for EJB: " + tLookupER);
      ERHome = (TxECMPBeanHome) nctx.lookup(tLookupER, TxECMPBeanHome.class);
      logMsg("Creating entity EJB = " + pkeyR.toString());
      beanRefER = (TxECMPBean) ERHome.create(tName1, pkeyR,
          tName1 + "-" + pkeyR.intValue(), (float) 20.00, testProps);

      // No need to create an instance in the Mandatory case - only a key.
      // Instance will be created in the test.
      pkeyM = new Integer(21);

      logMsg("setup ok");
    } catch (Exception e) {
      throw new Fault("setup failed:", e);
    }
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Run test */

  /*
   * @testName: test1
   *
   * @assertion_ids: EJB:SPEC:10122; EJB:SPEC:583.3.1; EJB:SPEC:583.3.2;
   * EJB:SPEC:583.3.4
   *
   * @test_Strategy: Container managed Tx commit - Required entity EJBs. Create
   * multiple client's which access the same entity object. Perform updates to
   * the Entity EJB's instance data. Ensure that the container properly
   * synchronizes access to the entity object via transactions. Ensure the
   * object instance data and database data are updated on method return.
   */

  public void test1() throws Fault {
    logMsg("test1");
    try {
      Thread[] threads = new Thread[NTHREADS];
      threadsDone = 0;
      errors = 0;

      // Start the threads
      logMsg("Multiple Clients (Session EJBs) accessing the same entity EJB");
      logMsg("Synchronize calling of entity EJB methods");
      logMsg("Creating " + NTHREADS + " client threads ...");

      for (int i = 0; i < NTHREADS; i++) {
        threads[i] = new Thread(new TestThread(i, 1), "TestThread-" + i);
        threads[i].start();
      }

      // Wait for creation
      logMsg("Wait for thread creation ...");
      synchronized (lock) {
        while (threadsDone < threads.length) {
          lock.wait();
        }
        try {
          Thread.sleep(SLEEPTIME);
        } catch (Exception e) {
        }
      }

      // Notify all to start
      logMsg("Notify all threads to start ...");
      synchronized (startLock) {
        threadsDone = 0;
        startLock.notifyAll();
      }

      // Wait for completion
      logMsg("Wait for all threads to finish ...");
      synchronized (lock) {
        while (threadsDone < threads.length) {
          lock.wait();
        }
      }

      if (errors > 0) {
        TestUtil.logErr("The number of errors were: " + errors);
        throw new Fault("test1 failed");
      }

    } catch (Exception e) {
      throw new Fault("test1 failed", e);
    }
  }

  public boolean runTest1() {
    logMsg("runTest1");
    boolean b1, b2, testResult;
    b1 = b2 = testResult = false;

    try {
      logMsg("Synchronize calling of Session EJB methods");
      for (int i = 0; i < NUMLOOPS; i++) {
        synchronized (workLock) {
          b1 = beanRefS1.doTest1(pkeyR, tName1, i + 1);
          b2 = beanRefS2.doTest1(pkeyR, tName1, i + 2);
        }
      }
      if (b1 && b2)
        testResult = true;

    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e.getMessage(), e);
      testResult = false;
    }
    logMsg("Leaving runTest1 Method .....");
    return testResult;
  }

  /*
   * @testName: test3
   *
   * @assertion_ids: EJB:SPEC:10122; EJB:SPEC:587.1; EJB:SPEC:587.3
   *
   * @test_Strategy: Container managed Tx - Mandatory entity EJBs. Create
   * multiple client's which access the same entity object. Attempt to create an
   * instances of an Entity EJB (TxECMPBean) bean, without a transaction
   * context. Ensure that javax.transacton.TransactionRequiredException
   * exception is thrown for both instances.
   */

  public void test3() throws Fault {
    logMsg("test3");
    try {
      Thread[] threads = new Thread[NTHREADS];
      threadsDone = 0;
      errors = 0;

      // Start the threads
      logMsg("Multiple Clients (Session EJBs) accessing the same entity EJB");
      logMsg("Synchronize calling of entity EJB methods");
      logMsg("Creating " + NTHREADS + " client threads ...");

      for (int i = 0; i < NTHREADS; i++) {
        threads[i] = new Thread(new TestThread(i, 3), "TestThread-" + i);
        threads[i].start();
      }

      // Wait for creation
      logMsg("Wait for thread creation ...");
      synchronized (lock) {
        while (threadsDone < threads.length) {
          lock.wait();
        }
        try {
          Thread.sleep(SLEEPTIME);
        } catch (Exception e) {
        }
      }

      // Notify all to start
      logMsg("Notify all threads to start ...");
      synchronized (startLock) {
        threadsDone = 0;
        startLock.notifyAll();
      }

      // Wait for completion
      logMsg("Wait for all threads to finish ...");
      synchronized (lock) {
        while (threadsDone < threads.length) {
          lock.wait();
        }
      }

      if (errors > 0) {
        TestUtil.logErr("The number of errors were: " + errors);
        throw new Fault("test3 failed");
      }

    } catch (Exception e) {
      throw new Fault("test3 failed", e);
    }
  }

  public boolean runTest3() {
    logMsg("runTest3");
    boolean b1, b2, testResult;
    b1 = b2 = testResult = false;

    // Only need to verify the exceptions get thrown once - only loop once.
    int t3numloops = 1;
    try {
      logMsg("Synchronize calling of Session EJB methods");
      for (int i = 0; i < t3numloops; i++) {
        synchronized (workLock) {
          b1 = beanRefS1.doTest3(pkeyM, tName1, i + 1);
          b2 = beanRefS2.doTest3(pkeyM, tName1, i + 2);
        }
      }
      if (b1 && b2)
        testResult = true;

    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e.getMessage(), e);
      testResult = false;
    }
    logMsg("Leaving runTest3 Method .....");
    return testResult;
  }

  public void cleanup() throws Fault {
    logMsg("cleanup");
    try {
      if (beanRefS1 != null) {
        beanRefS1.remove();
      }
      if (beanRefS2 != null) {
        beanRefS2.remove();
      }
      if (beanRefER != null) {
        beanRefER.remove();
      }
      logMsg("cleanup ok");
    } catch (Exception e) {
      TestUtil.logErr("Exception removing EJBs" + e.getMessage(), e);
    }
  }

}
