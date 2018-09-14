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
 * @(#)Client.java	1.26 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.session.stateful.statetest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.util.*;
import javax.ejb.*;
import javax.transaction.*;
import java.rmi.*;

import com.sun.javatest.Status;

//**********************************************************************
//State test to validate state is maintained in a stateful session bean.
//
//No state information needs to be maintained in the client.
//**********************************************************************

public class Client extends EETest {
  private static final int SLEEPTIME = 5000;

  private static final String testName = "StateTest";

  private static final String testLookup = "java:comp/env/ejb/Counter";

  private static Properties props = null;

  private static TSNamingContext nctx = null;

  private static CounterHome counterHome = null;

  // For thread synchronization
  private static final int NTHREADS = 2;

  private static Object lock = new Object();

  private static Object startLock = new Object();

  private static int threadsDone = 0;

  private static int errors = 0;

  // Inner class CounterThread
  class CounterThread implements Runnable {

    // Instance variables
    private int threadNum;

    private Counter counter;

    public CounterThread() {
    }

    public CounterThread(int n) {
      threadNum = n;
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

      boolean pass = runTest();

      synchronized (lock) {
        if (!pass)
          ++errors;
        ++threadsDone;
        lock.notifyAll();
      }
    }

    public boolean runTest() {
      boolean pass = true;
      try {
        // create EJB instance
        logMsg("Client EJB instance for thread" + threadNum);
        counter = (Counter) counterHome.create();
        logMsg("initialize remote logging");
        counter.initLogging(props);
        int value = counter.value();
        logMsg("Counter.value(): " + value);
        if (value != 0) {
          logErr("value: expected 0, received " + value);
          pass = false;
        } else {
          logMsg("value: expected 0, received " + value);
        }
        logMsg("increment counter");
        value = counter.increment();
        logMsg("Counter.value(): " + value);
        if (value != 1) {
          logErr("value: expected 1, received " + value);
          pass = false;
        } else {
          logMsg("value: expected 1, received " + value);
        }
        logMsg("decrement counter");
        value = counter.decrement();
        logMsg("Counter.value(): " + value);
        if (value != 0) {
          logErr("value: expected 0, received " + value);
          pass = false;
        } else {
          logMsg("value: expected 0, received " + value);
        }
        try {
          logMsg("decrement counter again - InvalidTransactionException");
          value = counter.decrement();
          logErr("no exception occurred - unexpected");
          pass = false;
        } catch (InvalidTransactionException e) {
          logMsg("InvalidTransactionException received as expected");
        } catch (Exception e) {
          logErr("Exception: " + e, e);
          pass = false;
        }
      } catch (Exception e) {
        logErr("Exception: " + e, e);
        pass = false;
      }
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
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    try {
      logMsg("Obtain naming context");
      nctx = new TSNamingContext();

      // Get EJB Home ...
      logMsg("Looking up home interface for EJB: " + testLookup);
      counterHome = (CounterHome) nctx.lookup(testLookup, CounterHome.class);
      logMsg("Setup ok");
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /* Run test */

  /*
   * @testName: test1
   * 
   * @assertion_ids: EJB:SPEC:53
   * 
   * @test_Strategy: A STATEFUL session bean contains conversational state which
   * must be retained across methods and transactions. Demonstrate using single
   * client.
   *
   */

  public void test1() throws Fault {
    try {
      Thread[] threads = new Thread[1];
      threadsDone = 0;
      errors = 0;

      logMsg("Creating " + 1 + " threads ...");
      for (int i = 0; i < 1; i++) {
        threads[i] = new Thread(new CounterThread(i), "CounterThread-" + i);
        threads[i].start();
      }

      // Wait for creation
      logMsg("wait for thread creation ...");
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
      logMsg("notify all threads to start");
      synchronized (startLock) {
        threadsDone = 0;
        startLock.notifyAll();
      }

      // Wait for completion
      logMsg("wait for all threads to finish");
      synchronized (lock) {
        while (threadsDone < threads.length) {
          lock.wait();
        }
      }

      if (errors > 0) {
        logMsg("The number of errors were: " + errors);
        throw new Fault("test1 failed");
      }

    } catch (Exception e) {
      throw new Fault("test1 failed", e);
    }
  }

  /*
   * @testName: test2
   * 
   * @assertion_ids: EJB:SPEC:53
   * 
   * @test_Strategy: A STATEFUL session bean contains conversational state which
   * must be retained across methods and transactions. Demonstrate using
   * multiple clients.
   *
   */

  public void test2() throws Fault {
    try {
      Thread[] threads = new Thread[NTHREADS];
      threadsDone = 0;
      errors = 0;

      logMsg("Creating " + NTHREADS + " threads ...");
      for (int i = 0; i < NTHREADS; i++) {
        threads[i] = new Thread(new CounterThread(i), "CounterThread-" + i);
        threads[i].start();
      }

      // Wait for creation
      logMsg("wait for thread creation ...");
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
      logMsg("notify all threads to start");
      synchronized (startLock) {
        threadsDone = 0;
        startLock.notifyAll();
      }

      // Wait for completion
      logMsg("wait for all threads to finish");
      synchronized (lock) {
        while (threadsDone < threads.length) {
          lock.wait();
        }
      }

      if (errors > 0) {
        logMsg("The number of errors were: " + errors);
        throw new Fault("test1 failed");
      }

    } catch (Exception e) {
      throw new Fault("test1 failed", e);
    }
  }

  /*
   * @testName: test3
   * 
   * @assertion_ids: EJB:SPEC:56
   * 
   * @test_Strategy: Create a Stateful Session bean Create a transaction within
   * the bean Set a conversational value within the bean Force the transaction
   * to rollback Varify the the conversation value doesn't change
   *
   */
  public void test3() throws Fault {
    boolean pass = false;

    try {

      Counter beanRef = (Counter) counterHome.create();
      logMsg("initialize remote logging");
      beanRef.initLogging(props);

      try {
        beanRef.setVal();
        TestUtil.logMsg(
            "This is the value of the Flag : " + beanRef.getRollBackStatus());
      } catch (Exception e) {
        TestUtil.logMsg("Caught expected exception " + e);
      }

      pass = beanRef.getVal();

    } catch (Exception e) {
      TestUtil.logErr("Caught exception : " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    } catch (Error e) {
      TestUtil.logErr("Caught error : " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault("test3 failed");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }
}
