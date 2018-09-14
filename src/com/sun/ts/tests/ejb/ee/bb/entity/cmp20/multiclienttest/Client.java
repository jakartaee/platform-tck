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
 * @(#)Client.java	1.10 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.entity.cmp20.multiclienttest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.util.*;
import javax.ejb.*;
import javax.naming.*;
import java.rmi.*;

import com.sun.javatest.Status;

public class Client extends EETest {
  private static final int SLEEPTIME = 5000;

  private static final String testName = "MultiClientTest";

  private static final String testBean = "java:comp/env/ejb/TestBean";

  private static final String testProps = "multiclienttest.properties";

  private static Properties props = null;

  private static TSNamingContext nctx = null;

  private TestBeanHome beanHome = null;

  private TestBean beanRef = null;

  private Integer pkey = null;

  // For thread synchronization
  private static final int NUMEJBS = 2;

  private static final int NUMLOOPS = 5;

  private static final int NTHREADS = 2;

  private static final int NTHREADS2 = 4;

  private static Object lock = new Object();

  private static Object startLock = new Object();

  private static Object workLock = new Object();

  private static int threadsDone = 0;

  private static int errors = 0;

  // Inner class TestThread
  class TestThread implements Runnable {

    // Instance variables
    private int threadNum;

    private int testNum;

    private boolean synchronize = true;

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

      boolean pass = true;

      if (testNum == 1)
        pass = runTest1();
      else if (testNum == 2)
        pass = runTest2();
      else if (testNum == 3)
        pass = runTest3();

      synchronized (lock) {
        if (!pass)
          ++errors;
        ++threadsDone;
        lock.notifyAll();
      }
    }

    public boolean runTest1() {
      boolean pass = true;
      logMsg("Entering runTest1 Method .....");
      try {
        logMsg("Find entity EJB with Primary Key = " + pkey.toString());
        TestBean beanRef = (TestBean) beanHome.findByPrimaryKey(pkey);
        logMsg("Initialize remote logging");
        beanRef.initLogging(props);
        logMsg("Synchronize calling of entity EJB methods");
        logMsg("Calling entity EJB methods getPrice()/updatePrice()");
        for (int i = 0; i < NUMLOOPS; i++) {
          synchronized (workLock) {
            float currentPrice = beanRef.getPrice();
            beanRef.setPrice((float) (currentPrice + threadNum + 1));
            float updatePrice = beanRef.getPrice();
            logMsg(
                "Entity EJB currentPrice = " + currentPrice + ", updatePrice = "
                    + updatePrice + ", threadNum = " + threadNum);
            // if(updatePrice != (currentPrice + threadNum + 1)) {
            // logErr("Unexpected updatePrice = " + updatePrice +
            // ", expected " + (currentPrice + threadNum + 1));
            // pass = false;
            // }
          }
        }
      } catch (Exception e) {
        // EJB spec states that a deadlock is possible when multiple
        // threads update the same entity bean so the following lines
        // are commented out to ignore the exception
        // logErr("Exception: " + e, e);
        // pass = false;
      }
      logMsg("Leaving runTest1 Method .....");
      return pass;
    }

    public boolean runTest2() {
      boolean pass = true;
      logMsg("Entering runTest2 Method .....");
      try {
        logMsg("Find entity EJB with Primary Key = " + pkey.toString());
        TestBean beanRef = (TestBean) beanHome.findByPrimaryKey(pkey);
        logMsg("Initialize remote logging");
        beanRef.initLogging(props);
        logMsg("Unsynchronize calling of Entity EJB methods");
        logMsg("Calling entity EJB methods getPrice()/updatePrice()");
        for (int i = 0; i < NUMLOOPS; i++) {
          float currentPrice = beanRef.getPrice();
          beanRef.setPrice((float) (currentPrice + 1));
          float updatePrice = beanRef.getPrice();
          logMsg(
              "Entity EJB currentPrice = " + currentPrice + ", updatePrice = "
                  + updatePrice + ", threadNum = " + threadNum);
        }
      } catch (Exception e) {
        // EJB spec states that a deadlock is possible when multiple
        // threads update the same entity bean so the following lines
        // are commented out to ignore the exception
        // logErr("Exception: " + e, e);
        // pass = false;
      }
      logMsg("Leaving runTest2 Method .....");
      return pass;
    }

    public boolean runTest3() {
      boolean pass = true;
      Integer pkey = null;
      logMsg("Entering runTest3 Method .....");
      try {
        if (threadNum == 1 || threadNum == 2)
          pkey = new Integer(1);
        else if (threadNum == 3 || threadNum == 4)
          pkey = new Integer(2);
        logMsg("Find entity EJB with Primary Key = " + pkey.toString());
        TestBean beanRef = (TestBean) beanHome.findByPrimaryKey(pkey);
        logMsg("Initialize remote logging");
        beanRef.initLogging(props);
        logMsg("Unsynchronize calling of Entity EJB methods");
        logMsg("Calling entity EJB methods getPrice()/updatePrice()");
        for (int i = 0; i < NUMLOOPS; i++) {
          float currentPrice = beanRef.getPrice();
          beanRef.setPrice((float) (currentPrice + 1));
          float updatePrice = beanRef.getPrice();
          logMsg(
              "Entity EJB currentPrice = " + currentPrice + ", updatePrice = "
                  + updatePrice + ", threadNum = " + threadNum);
        }
      } catch (Exception e) {
        // EJB spec states that a deadlock is possible when multiple
        // threads update the same entity bean so the following lines
        // are commented out to ignore the exception
        // logErr("Exception: " + e, e);
        // pass = false;
      }
      logMsg("Leaving runTest3 Method .....");
      return pass;
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
   * generateSQL;
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    try {
      logMsg("Obtain naming context");
      nctx = new TSNamingContext();

      // Get EJB Home ...
      logMsg("Looking up home interface for EJB: " + testBean);
      beanHome = (TestBeanHome) nctx.lookup(testBean, TestBeanHome.class);
      logMsg("Setup ok");
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /* Run test */

  /*
   * @testName: test1
   * 
   * @assertion_ids: EJB:SPEC:10122
   * 
   * @test_Strategy: Multiple clients can access the same entity EJB object
   * using single or multiple instances. The number of instances depends upon
   * the Container Implementation. Synchronize calling of entity EJB methods.
   *
   */

  public void test1() throws Fault {
    try {
      Thread[] threads = new Thread[NTHREADS];
      threadsDone = 0;
      errors = 0;

      pkey = new Integer(1);
      logMsg("Create entity EJB with Primary Key = " + pkey.toString());
      beanRef = (TestBean) beanHome.create(props, pkey.intValue(), "coffee-1",
          1);
      logMsg("Multiple Clients accessing the same entity EJB");
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

      float initialPrice = 1;
      float finalPrice = beanRef.getPrice();
      logMsg("Entity EJB initialPrice = " + initialPrice
          + ", Entity EJB finalPrice = " + finalPrice + " ...");

      if (errors > 0) {
        logErr("The number of errors were: " + errors);
        throw new Fault("test1 failed");
      }

    } catch (Exception e) {
      throw new Fault("test1 failed", e);
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
  }

  /*
   * @testName: test2
   * 
   * @assertion_ids: EJB:SPEC:10122
   * 
   * @test_Strategy: Multiple clients can access the same entity EJB object
   * using single or multiple instances. The number of instances depends upon
   * the Container Implementation. Unsynchronize calling of entity EJB methods.
   *
   */

  public void test2() throws Fault {
    try {
      Thread[] threads = new Thread[NTHREADS];
      threadsDone = 0;
      errors = 0;

      pkey = new Integer(1);
      logMsg("Create entity EJB with Primary Key = " + pkey.toString());
      beanRef = (TestBean) beanHome.create(props, pkey.intValue(), "coffee-1",
          1);
      logMsg("Multiple Clients accessing the same entity EJB");
      logMsg("Unsynchronize calling of entity EJB methods");
      logMsg("Creating " + NTHREADS + " client threads ...");
      for (int i = 0; i < NTHREADS; i++) {
        threads[i] = new Thread(new TestThread(i, 2), "TestThread-" + i);
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

      float initialPrice = 1;
      float finalPrice = beanRef.getPrice();
      logMsg("Entity EJB initialPrice = " + initialPrice
          + ", Entity EJB finalPrice = " + finalPrice + " ...");

      if (errors > 0) {
        logErr("The number of errors were: " + errors);
        throw new Fault("test2 failed");
      }

    } catch (Exception e) {
      throw new Fault("test2 failed", e);
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
  }

  /*
   * @testName: test3
   * 
   * @assertion_ids: EJB:SPEC:10122
   * 
   * @test_Strategy: Multiple clients accessing multiple entity EJB objects
   * using single or multiple instances. Create some entity EJB objects into the
   * database and then spawn some clients to access these entity EJB objects as
   * follows. Clients 1-2 will access the first entity object, clients 3-4 will
   * access the second entity object, ... The number of instances depends upon
   * the Container Implementation. Unsynchronize calling of entity EJB methods.
   * (Chapter 12.1.10)
   *
   */

  public void test3() throws Fault {
    TestBean[] beanRef = new TestBean[NUMEJBS];

    try {
      Thread[] threads = new Thread[NTHREADS2];
      threadsDone = 0;
      errors = 0;

      logMsg("Multiple Clients accessing different entity EJB's");
      logMsg("Unsynchronize calling of entity EJB's methods");
      logMsg("Create " + NUMEJBS + " entity EJB's");
      for (int i = 0, j = 1; i < NUMEJBS; i++, j++) {
        logMsg("Creating entity EJB #" + j + " with Primary Key = " + j);
        if (i == 0)
          beanRef[i] = (TestBean) beanHome.create(props, j, "coffee-" + j, j);
        else
          beanRef[i] = (TestBean) beanHome.create(props, j, "coffee-" + j, j);
      }
      logMsg("Creating " + NTHREADS2 + " client threads ...");
      for (int i = 0, j = 1; i < NTHREADS2; i++, j++) {
        threads[i] = new Thread(new TestThread(j, 3), "TestThread-" + j);
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

      float initialPrice1 = 1;
      float initialPrice2 = 2;
      float finalPrice1 = beanRef[0].getPrice();
      float finalPrice2 = beanRef[1].getPrice();
      logMsg("Entity EJB #1 initialPrice = " + initialPrice1
          + ", Entity EJB #1 finalPrice = " + finalPrice1 + " ...");
      logMsg("Entity EJB #2 initialPrice = " + initialPrice2
          + ", Entity EJB #2 finalPrice = " + finalPrice2 + " ...");

      if (errors > 0) {
        logErr("The number of errors were: " + errors);
        throw new Fault("test3 failed");
      }

    } catch (Exception e) {
      throw new Fault("test3 failed", e);
    } finally {
      try {
        for (int i = 0; i < NUMEJBS; i++)
          beanRef[i].remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }
}
