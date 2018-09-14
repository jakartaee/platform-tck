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

/*
 * @(#)Client.java	1.3 02/08/21
 */

package com.sun.ts.tests.ejb.ee.timer.session.stateless.cm;

import com.sun.ts.tests.ejb.ee.timer.common.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.util.Properties;
import javax.ejb.*;
import javax.rmi.*;
import javax.jms.QueueConnectionFactory;
import javax.jms.Queue;

import com.sun.javatest.Status;

public class Client extends EETest {

  private static final String testLookup = "java:comp/env/ejb/TestBean";

  private static final String proxyLookup = "java:comp/env/ejb/ProxyBean";

  private static final String queueLookup = "java:comp/env/jms/MyQueue";

  private static final String factoryLookup = "java:comp/env/jms/MyQueueConnectionFactory";

  private TestBeanHome testBeanHome;

  private TestBean testBeanRef;

  private ProxyBeanHome proxyBeanHome;

  private ProxyBean proxyBeanRef;

  private Properties props = new Properties();

  private TSNamingContext jctx;

  private Queue queue;

  private QueueConnectionFactory qcFactory;

  private long ejbTimeout;

  private long msgTimeout;

  private String msg;

  private static final String UserNameProp = "user";

  private static final String UserPasswordProp = "password";

  private static final String AuthUser = "authuser";

  private String authuser = "";

  private String username = "";

  private String password = "";

  private TSLoginContext lc;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   * ejb_timeout; ejb_wait; user; password;
   */
  public void setup(String[] args, Properties p) throws Fault {
    logMsg("Setup tests");
    this.props = p;
    logMsg("ejb_timeout is " + props.getProperty("ejb_timeout"));
    logMsg("ejb_wait is " + props.getProperty("ejb_wait"));

    try {

      authuser = props.getProperty(AuthUser);
      username = props.getProperty(UserNameProp);
      password = props.getProperty(UserPasswordProp);

      jctx = new TSNamingContext();

      lc = new TSLoginContext();
      lc.login(username, password);

      logMsg("Getting the EJB Home interface for " + proxyLookup);
      proxyBeanHome = (ProxyBeanHome) jctx.lookup(proxyLookup,
          ProxyBeanHome.class);
      logMsg("Getting the EJB Home interface for " + testLookup);
      testBeanHome = (TestBeanHome) jctx.lookup(testLookup, TestBeanHome.class);

      logMsg("initializing JMS messaging");
      queue = (Queue) jctx.lookup(queueLookup);
      qcFactory = (QueueConnectionFactory) jctx.lookup(factoryLookup);

      ejbTimeout = Long.parseLong(TestUtil.getProperty("ejb_timeout"));
      msgTimeout = Long.parseLong(TestUtil.getProperty("ejb_wait"));
      logMsg("Setup ok");
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /*
   * @testName: getInfoStrAndCancelSingleEventTest
   * 
   * @assertion_ids: EJB:SPEC:832.1; EJB:SPEC:832.3; EJB:SPEC:834; EJB:SPEC:835;
   * EJB:SPEC:836; EJB:SPEC:837.1; EJB:SPEC:839; EJB:SPEC:846; EJB:JAVADOC:27;
   * EJB:JAVADOC:214; EJB:JAVADOC:197; EJB:JAVADOC:210; EJB:JAVADOC:198;
   * EJB:JAVADOC:190
   *
   * @test_Strategy: Create a bean implementing the TimedObject interface.
   * Create a single-event timer for the bean using a String for the info
   * parameter. Call the timer's getInfo() method. Verify that the info returned
   * is identical to that used to create the timer. Call the timer's cancel()
   * method. Verify that no exception occurs. API tested:
   * EJBContext.getTimerService() TimerService.createTimer(long, Serializable)
   * Timer.getHandle() TimerHandle.getTimer() Timer.getInfo() Timer.cancel()
   */
  public void getInfoStrAndCancelSingleEventTest() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      testBeanRef = (TestBean) testBeanHome.create();

      boolean testResult = false;
      testBeanRef.initLogging(props);

      logMsg("Execute TestBean:getInfoStrAndCancel");
      testResult = testBeanRef.getInfoStrAndCancel(TimerImpl.TIMER_SINGLEEVENT);

      if (!testResult)
        throw new Fault("getInfoStrAndCancelSingleEventTest failed");
      else
        logMsg("getInfoStrAndCancelSingleEventTest passed");
    } catch (Exception e) {
      throw new Fault("getInfoStrAndCancelSingleEventTest failed", e);
    } finally {
      cleanupTestBean(testBeanRef);
    }
  }

  /*
   * @testName: getInfoStrAndCancelIntervalTest
   * 
   * @assertion_ids: EJB:SPEC:832.1; EJB:SPEC:832.3; EJB:SPEC:834; EJB:SPEC:835;
   * EJB:SPEC:836; EJB:SPEC:837.1; EJB:SPEC:839; EJB:SPEC:846; EJB:JAVADOC:27;
   * EJB:JAVADOC:218; EJB:JAVADOC:197; EJB:JAVADOC:210; EJB:JAVADOC:198;
   * EJB:JAVADOC:190
   *
   * @test_Strategy: Create a bean implementing the TimedObject interface.
   * Create an interval timer for the bean using a String for the info
   * parameter. Call the timer's getInfo() method. Verify that the info returned
   * is identical to that used to create the timer. Call the timer's cancel()
   * method. Verify that no exception occurs. API tested:
   * TimerService.createTimer(long, long, Serializable) Timer.getHandle()
   * TimerHandle.getTimer() Timer.getInfo() Timer.cancel()
   */
  public void getInfoStrAndCancelIntervalTest() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      testBeanRef = (TestBean) testBeanHome.create();

      boolean testResult = false;
      testBeanRef.initLogging(props);

      logMsg("Execute TestBean:getInfoStrAndCancel");
      testResult = testBeanRef.getInfoStrAndCancel(TimerImpl.TIMER_INTERVAL);

      if (!testResult)
        throw new Fault("getInfoStrAndCancelIntervalTest failed");
      else
        logMsg("getInfoStrAndCancelIntervalTest passed");
    } catch (Exception e) {
      throw new Fault("getInfoStrAndCancelIntervalTest failed", e);
    } finally {
      cleanupTestBean(testBeanRef);
    }
  }

  /*
   * @testName: getInfoStrAndCancelDateTest
   * 
   * @assertion_ids: EJB:SPEC:832.1; EJB:SPEC:832.3; EJB:SPEC:834; EJB:SPEC:835;
   * EJB:SPEC:836; EJB:SPEC:837.1; EJB:SPEC:839; EJB:SPEC:846; EJB:JAVADOC:27;
   * EJB:JAVADOC:222; EJB:JAVADOC:197; EJB:JAVADOC:210; EJB:JAVADOC:198;
   * EJB:JAVADOC:190
   * 
   * @test_Strategy: Create a bean implementing the TimedObject interface.
   * Create a date timer for the bean using a String for the info parameter.
   * Call the timer's getInfo() method. Verify that the info returned is
   * identical to that used to create the timer. Call the timer's cancel()
   * method. Verify that no exception occurs. API tested:
   * TimerService.createTimer(Date, Serializable) Timer.getHandle()
   * TimerHandle.getTimer() Timer.getInfo() Timer.cancel()
   */
  public void getInfoStrAndCancelDateTest() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      testBeanRef = (TestBean) testBeanHome.create();

      boolean testResult = false;
      testBeanRef.initLogging(props);

      logMsg("Execute TestBean:getInfoStrAndCancel");
      testResult = testBeanRef.getInfoStrAndCancel(TimerImpl.TIMER_DATE);

      if (!testResult)
        throw new Fault("getInfoStrAndCancelDateTest failed");
      else
        logMsg("getInfoStrAndCancelDateTest passed");
    } catch (Exception e) {
      throw new Fault("getInfoStrAndCancelDateTest failed", e);
    } finally {
      cleanupTestBean(testBeanRef);
    }
  }

  /*
   * @testName: getInfoStrAndCancelDateIntervalTest
   * 
   * @assertion_ids: EJB:SPEC:832.1; EJB:SPEC:832.3; EJB:SPEC:834; EJB:SPEC:835;
   * EJB:SPEC:836; EJB:SPEC:837.1; EJB:SPEC:839; EJB:SPEC:846; EJB:JAVADOC:27;
   * EJB:JAVADOC:226; EJB:JAVADOC:197; EJB:JAVADOC:210; EJB:JAVADOC:198;
   * EJB:JAVADOC:190
   * 
   * @test_Strategy: Create a bean implementing the TimedObject interface.
   * Create a date-interval timer for the bean using a String for the info
   * parameter. Call the timer's getInfo() method. Verify that the info returned
   * is identical to that used to create the timer. Call the timer's cancel()
   * method. Verify that no exception occurs. API tested:
   * TimerService.createTimer(Date, long, Serializable) Timer.getHandle()
   * TimerHandle.getTimer() Timer.getInfo() Timer.cancel()
   */
  public void getInfoStrAndCancelDateIntervalTest() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      testBeanRef = (TestBean) testBeanHome.create();

      boolean testResult = false;
      testBeanRef.initLogging(props);

      logMsg("Execute TestBean:getInfoStrAndCancel");
      testResult = testBeanRef
          .getInfoStrAndCancel(TimerImpl.TIMER_DATE_INTERVAL);

      if (!testResult)
        throw new Fault("getInfoStrAndCancelDateIntervalTest failed");
      else
        logMsg("getInfoStrAndCancelDateIntervalTest passed");
    } catch (Exception e) {
      throw new Fault("getInfoStrAndCancelDateIntervalTest failed", e);
    } finally {
      cleanupTestBean(testBeanRef);
    }
  }

  /*
   * @testName: getInfoClassAndCancelSingleEventTest
   * 
   * @assertion_ids: EJB:SPEC:832.1; EJB:SPEC:832.3; EJB:SPEC:834; EJB:SPEC:835;
   * EJB:SPEC:836; EJB:SPEC:837.1; EJB:SPEC:839; EJB:SPEC:846; EJB:JAVADOC:27;
   * EJB:JAVADOC:214; EJB:JAVADOC:197; EJB:JAVADOC:210; EJB:JAVADOC:198;
   * EJB:JAVADOC:193
   * 
   * @test_Strategy: Create a bean implementing the TimedObject interface.
   * Create a single-event timer for the bean using an application-specific
   * class for the info parameter. Call the timer's getInfo() method. Verify
   * that the object returned is identical to that used to create the timer.
   * Call the timer's cancel() method. Verify that no exception occurs. API
   * tested: TimerService.createTimer(long, long, Serializable)
   * Timer.getHandle() TimerHandle.getTimer() Timer.getInfo() Timer.cancel()
   */
  public void getInfoClassAndCancelSingleEventTest() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      testBeanRef = (TestBean) testBeanHome.create();

      boolean testResult = false;
      testBeanRef.initLogging(props);

      logMsg("Execute TestBean:getInfoClassAndCancel");
      testResult = testBeanRef
          .getInfoClassAndCancel(TimerImpl.TIMER_SINGLEEVENT);

      if (!testResult)
        throw new Fault("getInfoClassAndCancelSingleEventTest failed");
      else
        logMsg("getInfoClassAndCancelSingleEventTest passed");
    } catch (Exception e) {
      throw new Fault("getInfoClassAndCancelSingleEventTest failed", e);
    } finally {
      cleanupTestBean(testBeanRef);
    }
  }

  /*
   * @testName: getInfoClassAndCancelIntervalTest
   * 
   * @assertion_ids: EJB:SPEC:832.1; EJB:SPEC:832.3; EJB:SPEC:834; EJB:SPEC:835;
   * EJB:SPEC:836; EJB:SPEC:837.1; EJB:SPEC:839; EJB:SPEC:846; EJB:JAVADOC:189;
   * EJB:JAVADOC:38; EJB:JAVADOC:101; EJB:JAVADOC:53; EJB:JAVADOC:97;
   * EJB:JAVADOC:85
   *
   * @test_Strategy: Create a bean implementing the TimedObject interface.
   * Create an interval timer for the bean using an application-specific class
   * for the info parameter. Call the timer's getInfo() method. Verify that the
   * object returned is identical to that used to create the timer. Call the
   * timer's cancel() method. Verify that no exception occurs. API tested:
   * TimerService.createTimer(long, long, Serializable) Timer.getHandle()
   * TimerHandle.getTimer() Timer.getInfo() Timer.cancel()
   */
  public void getInfoClassAndCancelIntervalTest() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      testBeanRef = (TestBean) testBeanHome.create();

      boolean testResult = false;
      testBeanRef.initLogging(props);

      logMsg("Execute TestBean:getInfoClassAndCancel");
      testResult = testBeanRef.getInfoClassAndCancel(TimerImpl.TIMER_INTERVAL);

      if (!testResult)
        throw new Fault("getInfoClassAndCancelIntervalTest failed");
      else
        logMsg("getInfoClassAndCancelIntervalTest passed");
    } catch (Exception e) {
      throw new Fault("getInfoClassAndCancelIntervalTest failed", e);
    } finally {
      cleanupTestBean(testBeanRef);
    }
  }

  /*
   * @testName: getInfoClassAndCancelDateTest
   * 
   * @assertion_ids: EJB:SPEC:832.1; EJB:SPEC:832.3; EJB:SPEC:834; EJB:SPEC:835;
   * EJB:SPEC:836; EJB:SPEC:837.1; EJB:SPEC:839; EJB:SPEC:846; EJB:JAVADOC:189;
   * EJB:JAVADOC:42; EJB:JAVADOC:101; EJB:JAVADOC:53; EJB:JAVADOC:97;
   * EJB:JAVADOC:85
   *
   * @test_Strategy: Create a bean implementing the TimedObject interface.
   * Create a date timer for the bean using an application-specific class for
   * the info parameter. Call the timer's getInfo() method. Verify that the
   * object returned is identical to that used to create the timer. Call the
   * timer's cancel() method. Verify that no exception occurs. API tested:
   * TimerService.createTimer(Date, Serializable) Timer.getHandle()
   * TimerHandle.getTimer() Timer.getInfo() Timer.cancel()
   */
  public void getInfoClassAndCancelDateTest() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      testBeanRef = (TestBean) testBeanHome.create();

      boolean testResult = false;
      testBeanRef.initLogging(props);

      logMsg("Execute TestBean:getInfoClassAndCancel");
      testResult = testBeanRef.getInfoClassAndCancel(TimerImpl.TIMER_DATE);

      if (!testResult)
        throw new Fault("getInfoClassAndCancelDateTest failed");
      else
        logMsg("getInfoClassAndCancelDateTest passed");
    } catch (Exception e) {
      throw new Fault("getInfoClassAndCancelDateTest failed", e);
    } finally {
      cleanupTestBean(testBeanRef);
    }
  }

  /*
   * @testName: verifyTimeoutCallSingleEventTest
   * 
   * @assertion_ids: EJB:SPEC:858; EJB:JAVADOC:189
   *
   * @test_Strategy: Create a bean implementing the TimedObject interface. In
   * the ejbTimeout method, invoke a JMS send. Create a single-event timer. In
   * the application client, block on a JMS receive for a period longer than the
   * timer's duration. Verify that the message from ejbTimeout was received. API
   * tested: TimerService.createTimer(long, Serializable) Timer.getHandle()
   */
  public void verifyTimeoutCallSingleEventTest() throws Fault {

    try {
      logMsg("Creating EJB TestBean instance");
      testBeanRef = (TestBean) testBeanHome.create();

      boolean testResult = false;
      testBeanRef.initLogging(props);

      logMsg("Execute TestBean:initializeTimer");
      testResult = testBeanRef.initializeTimer(TimerImpl.TIMER_SINGLEEVENT,
          TimerImpl.ACCESS);

      if (testResult) {
        logMsg("initializeTimer succeeded - receiving message");
        msg = TimerImpl.getMessage(queue, qcFactory, msgTimeout);
        testResult = msg.equals(TimerImpl.ACCESS_OK);
      }
      if (!testResult)
        throw new Fault("verifyTimeoutCallSingleEventTest failed");
      else
        logMsg("verifyTimeoutCallSingleEventTest passed");
    } catch (Exception e) {
      throw new Fault("verifyTimeoutCallSingleEventTest failed", e);
    } finally {
      cleanupTestBean(testBeanRef);
    }
  }

  /*
   * @testName: verifyTimeoutCallIntervalTest
   * 
   * @assertion_ids: EJB:SPEC:858; EJB:JAVADOC:189; EJB:JAVADOC:218
   *
   * @test_Strategy: Create a bean implementing the TimedObject interface. In
   * the ejbTimeout method, invoke a JMS send. Create an interval timer. In the
   * application client, block on a JMS receive for a period longer than the
   * timer's duration. Verify that the message from ejbTimeout was received. API
   * tested: TimerService.createTimer(long, long, Serializable)
   * Timer.getHandle()
   */
  public void verifyTimeoutCallIntervalTest() throws Fault {

    try {
      logMsg("Creating EJB TestBean instance");
      testBeanRef = (TestBean) testBeanHome.create();

      boolean testResult = false;
      testBeanRef.initLogging(props);

      logMsg("Execute TestBean:initializeTimer");
      testResult = testBeanRef.initializeTimer(TimerImpl.TIMER_INTERVAL,
          TimerImpl.ACCESS);

      if (testResult) {
        logMsg("initializeTimer succeeded - receiving message");
        msg = TimerImpl.getMessage(queue, qcFactory, msgTimeout);
        testResult = msg.equals(TimerImpl.ACCESS_OK);
      }
      if (!testResult)
        throw new Fault("verifyTimeoutCallIntervalTest failed");
      else
        logMsg("verifyTimeoutCallIntervalTest passed");
    } catch (Exception e) {
      throw new Fault("verifyTimeoutCallIntervalTest failed", e);
    } finally {
      cleanupTestBean(testBeanRef);
    }
  }

  /*
   * @testName: checkedMethodAccessSingleEventTest
   * 
   * @assertion_ids: EJB:SPEC:827; EJB:JAVADOC:189
   *
   * @test_Strategy: Create a bean implementing the TimedObject interface with a
   * run-as security identity. Create a bean with a checked method that permits
   * the identity. In the ejbTimeout method, call the checked method. Report the
   * results of the call with a JMS send(). Create a single-event timer. In the
   * application client, block on a JMS receive for a period longer than the
   * timer's duration. Check the result in the message. API tested:
   * TimerService.createTimer(long, Serializable) Timer.getHandle()
   */
  public void checkedMethodAccessSingleEventTest() throws Fault {

    try {
      logMsg("Creating EJB TestBean instance");
      testBeanRef = (TestBean) testBeanHome.create();

      boolean testResult = false;
      testBeanRef.initLogging(props);

      logMsg("Execute TestBean:initializeTimer");
      testResult = testBeanRef.initializeTimer(TimerImpl.TIMER_SINGLEEVENT,
          TimerImpl.CHKMETH);

      if (testResult) {
        logMsg("initializeTimer succeeded - receiving message");
        msg = TimerImpl.getMessage(queue, qcFactory, msgTimeout);
        testResult = msg.equals(TimerImpl.CHKMETH_OK);
      }
      if (!testResult)
        throw new Fault("checkedMethodAccessSingleEventTest failed");
      else
        logMsg("checkedMethodAccessSingleEventTest passed");
    } catch (Exception e) {
      throw new Fault("checkedMethodAccessSingleEventTest failed", e);
    } finally {
      cleanupTestBean(testBeanRef);
    }
  }

  /*
   * @testName: checkedMethodAccessIntervalTest
   * 
   * @assertion_ids: EJB:SPEC:827; EJB:JAVADOC:189
   *
   * @test_Strategy: Create a bean implementing the TimedObject interface with a
   * run-as security identity. Create a bean with a checked method that permits
   * the identity. In the ejbTimeout method, call the checked method. Report the
   * results of the call with a JMS send(). Create an interval timer. In the
   * application client, block on a JMS receive for a period longer than the
   * timer's duration. Check the result in the message. API tested:
   * TimerService.createTimer(long, long, Serializable) Timer.getHandle()
   */
  public void checkedMethodAccessIntervalTest() throws Fault {

    try {
      logMsg("Creating EJB TestBean instance");
      testBeanRef = (TestBean) testBeanHome.create();

      boolean testResult = false;
      testBeanRef.initLogging(props);

      logMsg("Execute TestBean:initializeTimer");
      testResult = testBeanRef.initializeTimer(TimerImpl.TIMER_INTERVAL,
          TimerImpl.CHKMETH);

      if (testResult) {
        logMsg("initializeTimer succeeded - receiving message");
        msg = TimerImpl.getMessage(queue, qcFactory, msgTimeout);
        testResult = msg.equals(TimerImpl.CHKMETH_OK);
      }
      if (!testResult)
        throw new Fault("checkedMethodAccessIntervalTest failed");
      else
        logMsg("checkedMethodAccessIntervalTest passed");
    } catch (Exception e) {
      throw new Fault("checkedMethodAccessIntervalTest failed", e);
    } finally {
      cleanupTestBean(testBeanRef);
    }
  }

  /*
   * @testName: rollbackTxOnCreationSingleEventTest
   * 
   * @assertion_ids: EJB:SPEC:848; EJB:JAVADOC:50
   *
   * @test_Strategy: Create a bean implementing the TimedObject interface. In
   * the ejbTimeout method, execute a JMS send(). Create a single-event timer
   * within a transaction context. Mark the transaction for rollback. In the
   * application client, block on a JMS receive for a period longer than the
   * timer's duration. Verify that the message from ejbTimeout was not received.
   * Verify that the timer no longer exists. API tested:
   * TimerService.createTimer(long, Serializable) TimerService.getTimers()
   */
  public void rollbackTxOnCreationSingleEventTest() throws Fault {

    try {
      logMsg("Creating EJB TestBean instance");
      testBeanRef = (TestBean) testBeanHome.create();

      boolean testResult = false;
      testBeanRef.initLogging(props);

      logMsg("Execute TestBean:createAndRollback");
      testResult = testBeanRef.createAndRollback(TimerImpl.TIMER_SINGLEEVENT);

      if (testResult) {
        logMsg("createAndRollback succeeded");
        msg = TimerImpl.getMessage(queue, qcFactory, msgTimeout);
        testResult = msg.equals(TimerImpl.NOMSGRECEIVED);
        if (testResult)
          testResult = testBeanRef.verifyTimerIsGone();
      }
      if (!testResult)
        throw new Fault("rollbackTxOnCreationSingleEventTest failed");
      else
        logMsg("rollbackTxOnCreationSingleEventTest passed");
    } catch (Exception e) {
      throw new Fault("rollbackTxOnCreationSingleEventTest failed", e);
    } finally {
      cleanupTestBean(testBeanRef);
    }
  }

  /*
   * @testName: rollbackTxOnCreationIntervalTest
   * 
   * @assertion_ids: EJB:SPEC:848; EJB:JAVADOC:50
   *
   * @test_Strategy: Create a bean implementing the TimedObject interface. In
   * the ejbTimeout method, execute a JMS send(). Create an interval timer
   * within a transaction context. Mark the transaction for rollback. In the
   * application client, block on a JMS receive for a period longer than the
   * timer's duration. Verify that the message from ejbTimeout was not received.
   * Verify that the timer no longer exists. API tested:
   * TimerService.createTimer(long, long, Serializable) TimerService.getTimers()
   */
  public void rollbackTxOnCreationIntervalTest() throws Fault {

    try {
      logMsg("Creating EJB TestBean instance");
      testBeanRef = (TestBean) testBeanHome.create();

      boolean testResult = false;
      testBeanRef.initLogging(props);

      logMsg("Execute TestBean:createAndRollback");
      testResult = testBeanRef.createAndRollback(TimerImpl.TIMER_INTERVAL);

      if (testResult) {
        logMsg("createAndRollback succeeded");
        msg = TimerImpl.getMessage(queue, qcFactory, msgTimeout);
        testResult = msg.equals(TimerImpl.NOMSGRECEIVED);
        if (testResult)
          testResult = testBeanRef.verifyTimerIsGone();
      }
      if (!testResult)
        throw new Fault("rollbackTxOnCreationIntervalTest failed");
      else
        logMsg("rollbackTxOnCreationIntervalTest passed");
    } catch (Exception e) {
      throw new Fault("rollbackTxOnCreationIntervalTest failed", e);
    } finally {
      cleanupTestBean(testBeanRef);
    }
  }

  /*
   * @testName: ejbTimeoutRetrySingleEventTest
   * 
   * @assertion_ids: EJB:SPEC:850; EJB:JAVADOC:189
   *
   * @test_Strategy: Create a cmt bean implementing the TimedObject interface
   * with the transaction attribute of the ejbTimeout method set to RequiresNew.
   * Create an entity bean to store a flag to indicate whether the ejbTimeout
   * method has been called. The bean has remote methods with the transaction
   * attribute RequiresNew for getting and setting the flag. Create a
   * single-event timer and unset the flag. In the ejbTimeout method, get the
   * value of the flag. If the flag is not set, set it (in a separate
   * transaction) and mark the transaction for rollback. If the flag is set,
   * send a JMS message. In the application client, block on a JMS receive for a
   * period longer than the timer's duration. Verify that the message from
   * ejbTimeout was received. API tested: TimerService.createTimer(long,
   * Serializable);
   */
  public void ejbTimeoutRetrySingleEventTest() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      testBeanRef = (TestBean) testBeanHome.create();

      boolean testResult = false;
      testBeanRef.initLogging(props);

      logMsg("Execute TestBean:createFlagStoreAndTimer");
      testResult = testBeanRef.createFlagStoreAndTimer(
          TimerImpl.TIMER_SINGLEEVENT, TimerImpl.RETRY);

      if (testResult) {
        logMsg("createFlagStoreAndTimer succeeded");
        msg = TimerImpl.getMessage(queue, qcFactory, msgTimeout);
        testResult = msg.equals(TimerImpl.RETRY_OK);
      }
      if (!testResult)
        throw new Fault("ejbTimeoutRetrySingleEventTest failed");
      else
        logMsg("ejbTimeoutRetrySingleEventTest passed");
    } catch (Exception e) {
      throw new Fault("ejbTimeoutRetrySingleEventTest failed", e);
    } finally {
      cleanupTestBean(testBeanRef);
    }
  }

  /*
   * @testName: ejbTimeoutRetryIntervalTest
   * 
   * @assertion_ids: EJB:SPEC:850; EJB:JAVADOC:189
   *
   * @test_Strategy: Create a cmt bean implementing the TimedObject interface
   * with the transaction attribute of the ejbTimeout method set to RequiresNew.
   * Create an entity bean to store a flag to indicate whether the ejbTimeout
   * method has been called. The bean has remote methods with the transaction
   * attribute RequiresNew for getting and setting the flag. Create an interval
   * timer and unset the flag. In the ejbTimeout method, get the value of the
   * flag. If the flag is not set, set it (in a separate transaction) and mark
   * the transaction for rollback. If the flag is set, send a JMS message. In
   * the application client, block on a JMS receive for a period longer than the
   * timer's duration. Verify that the message from ejbTimeout was received. API
   * tested: TimerService.createTimer(long, long, Serializable);
   */
  public void ejbTimeoutRetryIntervalTest() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      testBeanRef = (TestBean) testBeanHome.create();

      boolean testResult = false;
      testBeanRef.initLogging(props);

      logMsg("Execute TestBean:createFlagStoreAndTimer");
      testResult = testBeanRef.createFlagStoreAndTimer(TimerImpl.TIMER_INTERVAL,
          TimerImpl.RETRY);

      if (testResult) {
        logMsg("createFlagStoreAndTimer succeeded");
        msg = TimerImpl.getMessage(queue, qcFactory, msgTimeout);
        testResult = msg.equals(TimerImpl.RETRY_OK);
      }
      if (!testResult)
        throw new Fault("ejbTimeoutRetryIntervalTest failed");
      else
        logMsg("ejbTimeoutRetryIntervalTest passed");
    } catch (Exception e) {
      throw new Fault("ejbTimeoutRetryIntervalTest failed", e);
    } finally {
      cleanupTestBean(testBeanRef);
    }
  }

  /*
   * @testName: rollbackTxInEjbTimeoutSingleEventTest
   * 
   * @assertion_ids: EJB:SPEC:864.1; EJB:SPEC:864.2; EJB:JAVADOC:189
   *
   * @test_Strategy: Create a cmt bean implementing the TimedObject interface.
   * Create an entity bean with two flags; one flag is set with a Requires
   * method, the other with RequiresNew. Both flags are initialized unset.
   * Create a single-event timer. In a transaction context in the ejbTimeout
   * method, set both flags, and cause the transaction to be rolled back. When
   * the ejbTimeout method is retried, verify that the transaction has been
   * rolled back if the RequiresNew flag is set and the Requires flag is unset.
   * In the application client, block on a JMS receive for a period longer than
   * the timer's duration. Verify that the correct message from ejbTimeout was
   * received. API tested: TimerService.createTimer(long, Serializable);
   */
  public void rollbackTxInEjbTimeoutSingleEventTest() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      testBeanRef = (TestBean) testBeanHome.create();

      boolean testResult = false;
      testBeanRef.initLogging(props);

      logMsg("Execute TestBean:createFlagStoreAndTimer");
      testResult = testBeanRef.createFlagStoreAndTimer(
          TimerImpl.TIMER_SINGLEEVENT, TimerImpl.ROLLBACK);

      if (testResult) {
        logMsg("createFlagStoreAndTimer succeeded");
        msg = TimerImpl.getMessage(queue, qcFactory, msgTimeout);
        testResult = msg.equals(TimerImpl.ROLLBACK_OK);
      }
      if (!testResult)
        throw new Fault("rollbackTxInEjbTimeoutSingleEventTest failed");
      else
        logMsg("rollbackTxInEjbTimeoutSingleEventTest passed");
    } catch (Exception e) {
      throw new Fault("rollbackTxInEjbTimeoutSingleEventTest failed", e);
    } finally {
      cleanupTestBean(testBeanRef);
    }
  }

  /*
   * @testName: rollbackTxInEjbTimeoutIntervalTest
   * 
   * @assertion_ids: EJB:SPEC:864.1; EJB:SPEC:864.2; EJB:JAVADOC:189
   *
   * @test_Strategy: Create a cmt bean implementing the TimedObject interface.
   * Create an entity bean with two flags; one flag is set with a Requires
   * method, the other with RequiresNew. Both flags are initialized unset.
   * Create an interval timer. In a transaction context in the ejbTimeout
   * method, set both flags, and cause the transaction to be rolled back. When
   * the ejbTimeout method is retried, verify that the transaction has been
   * rolled back if the RequiresNew flag is set and the Requires flag is unset.
   * In the application client, block on a JMS receive for a period longer than
   * the timer's duration. Verify that the correct message from ejbTimeout was
   * received. API tested: TimerService.createTimer(long, long, Serializable);
   */
  public void rollbackTxInEjbTimeoutIntervalTest() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      testBeanRef = (TestBean) testBeanHome.create();

      boolean testResult = false;
      testBeanRef.initLogging(props);

      logMsg("Execute TestBean:createFlagStoreAndTimer");
      testResult = testBeanRef.createFlagStoreAndTimer(TimerImpl.TIMER_INTERVAL,
          TimerImpl.ROLLBACK);

      if (testResult) {
        logMsg("createFlagStoreAndTimer succeeded");
        msg = TimerImpl.getMessage(queue, qcFactory, msgTimeout);
        testResult = msg.equals(TimerImpl.ROLLBACK_OK);
      }
      if (!testResult)
        throw new Fault("rollbackTxInEjbTimeoutIntervalTest failed");
      else
        logMsg("rollbackTxInEjbTimeoutIntervalTest passed");
    } catch (Exception e) {
      throw new Fault("rollbackTxInEjbTimeoutIntervalTest failed", e);
    } finally {
      cleanupTestBean(testBeanRef);
    }
  }

  /*
   * @testName: checkSerializationSingleEventTest
   * 
   * @assertion_ids: EJB:SPEC:852.1
   *
   * @test_Strategy: Create a bean implementing the TimedObject interface.
   * Create a single-event timer and get the timer handle. Verify that the timer
   * handle is serializable. API tested: TimerService.createTimer(long,
   * Serializable) Timer.getHandle()
   */
  public void checkSerializationSingleEventTest() throws Fault {

    try {
      logMsg("Creating EJB TestBean instance");
      testBeanRef = (TestBean) testBeanHome.create();

      boolean testResult = false;
      testBeanRef.initLogging(props);

      logMsg("Execute TestBean:checkSerialization");
      testResult = testBeanRef.isSerializable(TimerImpl.TIMER_SINGLEEVENT);

      if (!testResult)
        throw new Fault("checkSerializationSingleEventTest failed");
      else
        logMsg("checkSerializationSingleEventTest passed");
    } catch (Exception e) {
      throw new Fault("checkSerializationSingleEventTest failed", e);
    } finally {
      cleanupTestBean(testBeanRef);
    }
  }

  /*
   * @testName: checkSerializationIntervalTest
   * 
   * @assertion_ids: EJB:SPEC:852.1
   *
   * @test_Strategy: Create a bean implementing the TimedObject interface.
   * Create an interval timer and get the timer handle. Verify that the timer
   * handle is serializable. API tested: TimerService.createTimer(long, long,
   * Serializable) Timer.getHandle()
   */
  public void checkSerializationIntervalTest() throws Fault {

    try {
      logMsg("Creating EJB TestBean instance");
      testBeanRef = (TestBean) testBeanHome.create();

      boolean testResult = false;
      testBeanRef.initLogging(props);

      logMsg("Execute TestBean:checkSerialization");
      testResult = testBeanRef.isSerializable(TimerImpl.TIMER_INTERVAL);

      if (!testResult)
        throw new Fault("checkSerializationIntervalTest failed");
      else
        logMsg("checkSerializationIntervalTest passed");
    } catch (Exception e) {
      throw new Fault("checkSerializationIntervalTest failed", e);
    } finally {
      cleanupTestBean(testBeanRef);
    }
  }

  /*
   * @testName: checkSerializationDateTest
   * 
   * @assertion_ids: EJB:SPEC:852.1
   *
   * @test_Strategy: Create a bean implementing the TimedObject interface.
   * Create a date timer and get the timer handle. Verify that the timer handle
   * is serializable. API tested: TimerService.createTimer(Date, Serializable)
   * Timer.getHandle()
   */
  public void checkSerializationDateTest() throws Fault {

    try {
      logMsg("Creating EJB TestBean instance");
      testBeanRef = (TestBean) testBeanHome.create();

      boolean testResult = false;
      testBeanRef.initLogging(props);

      logMsg("Execute TestBean:checkSerialization");
      testResult = testBeanRef.isSerializable(TimerImpl.TIMER_DATE);

      if (!testResult)
        throw new Fault("checkSerializationDateTest failed");
      else
        logMsg("checkSerializationDateTest passed");
    } catch (Exception e) {
      throw new Fault("checkSerializationDateTest failed", e);
    } finally {
      cleanupTestBean(testBeanRef);
    }
  }

  /*
   * @testName: checkSerializationInEjbTimeoutSingleEventTest
   * 
   * @assertion_ids: EJB:SPEC:845; EJB:SPEC:847; EJB:SPEC:852.1; EJB:SPEC:852.2;
   * EJB:JAVADOC:189
   *
   * @test_Strategy: Create a bean implementing the TimedObject interface.
   * Create a single-event timer. In the ejbTimeout method, get the timer
   * handle. Serialize and deserialize the handle. Use the handle to get a
   * reference to a timer, and verify that the timer is identical to the one
   * created. API tested: TimerService.createTimer(long, Serializable)
   * Timer.getHandle() Timer.equals(Timer)
   */
  public void checkSerializationInEjbTimeoutSingleEventTest() throws Fault {

    try {
      logMsg("Creating EJB TestBean instance");
      testBeanRef = (TestBean) testBeanHome.create();

      boolean testResult = false;
      testBeanRef.initLogging(props);

      logMsg("Execute TestBean:initializeTimer");
      testResult = testBeanRef.initializeTimer(TimerImpl.TIMER_SINGLEEVENT,
          TimerImpl.SERIALIZE);

      if (testResult) {
        logMsg("initializeTimer succeeded - receiving message");
        msg = TimerImpl.getMessage(queue, qcFactory, msgTimeout);
        testResult = msg.equals(TimerImpl.SERIALIZE_OK);
      }
      if (!testResult)
        throw new Fault("checkSerializationInEjbTimeoutSingleEventTest failed");
      else
        logMsg("checkSerializationInEjbTimeoutSingleEventTest passed");
    } catch (Exception e) {
      throw new Fault("checkSerializationInEjbTimeoutSingleEventTest failed",
          e);
    } finally {
      cleanupTestBean(testBeanRef);
    }
  }

  /*
   * @testName: checkSerializationInEjbTimeoutIntervalTest
   * 
   * @assertion_ids: EJB:SPEC:845; EJB:SPEC:847; EJB:SPEC:852.1; EJB:SPEC:852.2;
   * EJB:JAVADOC:189
   *
   * @test_Strategy: Create a bean implementing the TimedObject interface.
   * Create an interval timer. In the ejbTimeout method, get the timer handle.
   * Serialize and deserialize the handle. Use the handle to get a reference to
   * a timer, and verify that the timer is identical to the one created. API
   * tested: TimerService.createTimer(long, long, Serializable)
   * Timer.getHandle() Timer.equals(Timer)
   */
  public void checkSerializationInEjbTimeoutIntervalTest() throws Fault {

    try {
      logMsg("Creating EJB TestBean instance");
      testBeanRef = (TestBean) testBeanHome.create();

      boolean testResult = false;
      testBeanRef.initLogging(props);

      logMsg("Execute TestBean:initializeTimer");
      testResult = testBeanRef.initializeTimer(TimerImpl.TIMER_INTERVAL,
          TimerImpl.SERIALIZE);

      if (testResult) {
        logMsg("initializeTimer succeeded - receiving message");
        msg = TimerImpl.getMessage(queue, qcFactory, msgTimeout);
        testResult = msg.equals(TimerImpl.SERIALIZE_OK);
      }
      if (!testResult)
        throw new Fault("checkSerializationInEjbTimeoutIntervalTest failed");
      else
        logMsg("checkSerializationInEjbTimeoutIntervalTest passed");
    } catch (Exception e) {
      throw new Fault("checkSerializationInEjbTimeoutIntervalTest failed", e);
    } finally {
      cleanupTestBean(testBeanRef);
    }
  }

  /*
   * @testName: cancelAndRollbackSingleEventTest
   * 
   * @assertion_ids: EJB:SPEC:849; EJB:JAVADOC:189
   *
   * @test_Strategy: Create a bean implementing the TimedObject interface. In
   * the ejbTimeout method, send a JMS message. Create a single-event timer
   * within a transaction context. In a separate transaction, cancel the timer
   * and roll back the transaction. Access the timer and verify that no
   * exception is thrown. In the application client, block on a JMS receive for
   * a period longer than the timer's duration. Verify that the message is
   * received. API tested: TimerService.createTimer(long, Serializable)
   * Timer.getHandle() TimerHandle.getTimer()
   */
  public void cancelAndRollbackSingleEventTest() throws Fault {

    try {
      logMsg("Creating EJB ProxyBean instance");
      proxyBeanRef = (ProxyBean) proxyBeanHome.create(props);

      boolean testResult = false;

      logMsg("Performing EJB ProxyBean setup");
      if (!proxyBeanRef.setup())
        throw new Fault("cancelAndRollbackSingleEventTest failed");

      logMsg("Execute ProxyBean:cancelAndRollback");
      testResult = proxyBeanRef.cancelAndRollback(TimerImpl.TIMER_SINGLEEVENT);

      if (testResult) {
        logMsg("cancelAndRollback succeeded - receiving message");
        msg = TimerImpl.getMessage(queue, qcFactory, msgTimeout);
        testResult = msg.equals(TimerImpl.ACCESS_OK);
      }
      if (!testResult)
        throw new Fault("cancelAndRollbackSingleEventTest failed");
      else
        logMsg("cancelAndRollbackSingleEventTest passed");
    } catch (Exception e) {
      throw new Fault("cancelAndRollbackSingleEventTest failed", e);
    } finally {
      cleanupProxyBean(proxyBeanRef);
    }
  }

  /*
   * @testName: cancelAndRollbackIntervalTest
   * 
   * @assertion_ids: EJB:SPEC:849; EJB:JAVADOC:189
   *
   * @test_Strategy: Create a bean implementing the TimedObject interface. In
   * the ejbTimeout method, send a JMS message. Create an interval timer within
   * a transaction context. In a separate transaction, cancel the timer and roll
   * back the transaction. Access the timer and verify that no exception is
   * thrown. In the application client, block on a JMS receive for a period
   * longer than the timer's duration. Verify that the message is received. API
   * tested: TimerService.createTimer(long, long, Serializable)
   * Timer.getHandle() TimerHandle.getTimer()
   */
  public void cancelAndRollbackIntervalTest() throws Fault {

    try {
      logMsg("Creating EJB ProxyBean instance");
      proxyBeanRef = (ProxyBean) proxyBeanHome.create(props);

      boolean testResult = false;

      logMsg("Performing EJB ProxyBean setup");
      if (!proxyBeanRef.setup())
        throw new Fault("cancelAndRollbackIntervalTest failed");

      logMsg("Execute ProxyBean:cancelAndRollback");
      testResult = proxyBeanRef.cancelAndRollback(TimerImpl.TIMER_INTERVAL);

      if (testResult) {
        logMsg("cancelAndRollback succeeded - receiving message");
        msg = TimerImpl.getMessage(queue, qcFactory, msgTimeout);
        testResult = msg.equals(TimerImpl.ACCESS_OK);
      }
      if (!testResult)
        throw new Fault("cancelAndRollbackIntervalTest failed");
      else
        logMsg("cancelAndRollbackIntervalTest passed");
    } catch (Exception e) {
      throw new Fault("cancelAndRollbackIntervalTest failed", e);
    } finally {
      cleanupProxyBean(proxyBeanRef);
    }
  }

  /*
   * @testName: goneOnExpirationSingleEventTest
   * 
   * @assertion_ids: EJB:SPEC:863.1; EJB:SPEC:863.2; EJB:JAVADOC:55;
   * EJB:JAVADOC:189
   *
   * @test_Strategy: Create a bean implementing the TimedObject interface. In
   * the ejbTimeout method, send a JMS message. Create a single-event timer. In
   * the application client, block on a JMS receive for a period longer than the
   * timer's duration. Verify that the message is received. Verify that the
   * timer is gone by calling a method on the timer and catching the
   * NoSuchObjectLocalException. API tested: TimerService.createTimer(long,
   * Serializable) Timer.getHandle() TimerHandle.getTimer()
   */
  public void goneOnExpirationSingleEventTest() throws Fault {

    try {
      logMsg("Creating EJB ProxyBean instance");
      proxyBeanRef = (ProxyBean) proxyBeanHome.create(props);

      boolean testResult = false;

      if (!proxyBeanRef.setup())
        throw new Fault("goneOnExpirationSingleEventTest failed");

      logMsg("Execute ProxyBean:initializeTimer");
      testResult = proxyBeanRef.initializeTimer(TimerImpl.TIMER_SINGLEEVENT,
          TimerImpl.ACCESS);

      if (testResult) {
        logMsg("initializeTimer succeeded - receiving message");
        msg = TimerImpl.getMessage(queue, qcFactory, msgTimeout);
        logMsg("message received is " + msg);
        testResult = msg.equals(TimerImpl.ACCESS_OK);
      }

      if (testResult) {
        // Sleep in case container hasn't had time to remove timer
        Thread.currentThread().sleep((msgTimeout - ejbTimeout) / 2);
        testResult = proxyBeanRef.timerDoesNotExist();
      }
      if (!testResult)
        throw new Fault("goneOnExpirationSingleEventTest failed");

      logMsg("goneOnExpirationSingleEventTest passed");
    } catch (Exception e) {
      throw new Fault("goneOnExpirationSingleEventTest failed", e);
    } finally {
      cleanupProxyBean(proxyBeanRef);
    }
  }

  /*
   * @testName: goneOnCancellationSingleEventTest
   * 
   * @assertion_ids: EJB:SPEC:867.1; EJB:SPEC:867.2; EJB:JAVADOC:55;
   * EJB:JAVADOC:189
   *
   * @test_Strategy: Create a bean implementing the TimedObject interface. In
   * the ejbTimeout method, send a JMS message. Create a single-event timer
   * within a transaction. In a separate transaction, cancel the timer Verify
   * that the timer is gone by calling a method on the timer and catching the
   * NoSuchObjectLocalException. In the application client, block on a JMS
   * receive for a period longer than the timer's duration. Verify that no
   * message is received. API tested: TimerService.createTimer(long,
   * Serializable) Timer.cancel() Timer.getHandle() TimerHandle.getTimer()
   */
  public void goneOnCancellationSingleEventTest() throws Fault {

    try {
      logMsg("Creating EJB ProxyBean instance");
      proxyBeanRef = (ProxyBean) proxyBeanHome.create(props);

      boolean testResult = false;

      if (!proxyBeanRef.setup())
        throw new Fault("goneOnCancellationSingleEventTest failed");

      logMsg("Execute ProxyBean:initializeAndCancelTimer");
      testResult = proxyBeanRef
          .initializeAndCancelTimer(TimerImpl.TIMER_SINGLEEVENT);

      if (testResult) {
        logMsg("initializeAndCancelTimer succeeded - receiving message");
        msg = TimerImpl.getMessage(queue, qcFactory, msgTimeout);
        logMsg("message is " + msg);
        testResult = msg.equals(TimerImpl.NOMSGRECEIVED);
      }

      if (!testResult)
        throw new Fault("goneOnCancellationSingleEventTest failed");

      logMsg("goneOnCancellationSingleEventTest passed");
    } catch (Exception e) {
      throw new Fault("goneOnCancellationSingleEventTest failed", e);
    } finally {
      cleanupProxyBean(proxyBeanRef);
    }
  }

  /*
   * @testName: goneOnCancellationIntervalTest
   * 
   * @assertion_ids: EJB:SPEC:867.1; EJB:SPEC:867.2; EJB:JAVADOC:55;
   * EJB:JAVADOC:189
   *
   * @test_Strategy: Create a bean implementing the TimedObject interface. In
   * the ejbTimeout method, send a JMS message. Create a interval timer within a
   * transaction. In a separate transaction, cancel the timer Verify that the
   * timer is gone by calling a method on the timer and catching the
   * NoSuchObjectLocalException. In the application client, block on a JMS
   * receive for a period longer than the timer's duration. Verify that no
   * message is received. API tested: TimerService.createTimer(long, long,
   * Serializable) Timer.cancel() Timer.getHandle() TimerHandle.getTimer()
   */
  public void goneOnCancellationIntervalTest() throws Fault {

    try {
      logMsg("Creating EJB ProxyBean instance");
      proxyBeanRef = (ProxyBean) proxyBeanHome.create(props);

      boolean testResult = false;

      if (!proxyBeanRef.setup())
        throw new Fault("goneOnCancellationIntervalTest failed");

      logMsg("Execute ProxyBean:initializeAndCancelTimer");
      testResult = proxyBeanRef
          .initializeAndCancelTimer(TimerImpl.TIMER_INTERVAL);

      if (testResult) {
        logMsg("initializeAndCancelTimer succeeded - receiving message");
        msg = TimerImpl.getMessage(queue, qcFactory, msgTimeout);
        logMsg("message is " + msg);
        testResult = msg.equals(TimerImpl.NOMSGRECEIVED);
      }

      if (!testResult)
        throw new Fault("goneOnCancellationIntervalTest failed");

      logMsg("goneOnCancellationIntervalTest passed");
    } catch (Exception e) {
      throw new Fault("goneOnCancellationIntervalTest failed", e);
    } finally {
      cleanupProxyBean(proxyBeanRef);
    }
  }

  /*
   * @testName: ejbTimeoutAllowedMethodsTest
   * 
   * @assertion_ids: EJB:SPEC:112; EJB:SPEC:112.1; EJB:SPEC:112.2;
   * EJB:SPEC:112.3; EJB:SPEC:112.4; EJB:SPEC:112.6; EJB:SPEC:112.7;
   * EJB:SPEC:112.10; EJB:SPEC:112.11; EJB:SPEC:112.12; EJB:SPEC:112.13;
   * EJB:SPEC:112.14; EJB:SPEC:844; EJB:JAVADOC:28; EJB:JAVADOC:207;
   * EJB:JAVADOC:203; EJB:JAVADOC:199; EJB:JAVADOC:195; EJB:JAVADOC:211
   * 
   * @test_Strategy: Operations allowed and not allowed in the ejbTimeout method
   * of a stateless session bean with container-managed transaction demarcation
   * are: o getEJBHome - allowed o getCallerPrincipal - allowed o isCallerInRole
   * - allowed o getEJBObject - allowed o JNDI_Access - allowed o
   * UserTransaction_Access - not allowed o UserTransaction_Methods_Test1 - not
   * allowed o UserTransaction_Methods_Test2 - not allowed o
   * UserTransaction_Methods_Test3 - not allowed o UserTransaction_Methods_Test4
   * - not allowed o UserTransaction_Methods_Test5 - not allowed o
   * UserTransaction_Methods_Test6 - not allowed o getEJBLocalHome - allowed o
   * getEJBLocalObject - allowed o getTimerService - allowed o
   * Timer_Service_Methods_Test1 - allowed o Timer_Service_Methods_Test2 -
   * allowed o Timer_Service_Methods_Test3 - allowed o
   * Timer_Service_Methods_Test4 - allowed o Timer_Service_Methods_Test5 -
   * allowed o Timer_Service_Methods_Test6 - allowed o
   * Timer_Service_Methods_Test7 - allowed o getRollbackOnly - allowed o
   * setRollbackOnly - NOT TESTED
   * 
   * Create a stateless Session Bean. Deploy it on the J2EE server. Verify
   * correct operations. For getCallerPrincipal(), verify that the return value
   * is non-null.
   * 
   */
  public void ejbTimeoutAllowedMethodsTest() throws Fault {

    try {
      logMsg("Creating EJB TestBean instance");
      testBeanRef = (TestBean) testBeanHome.create();

      boolean testResult = false;
      testBeanRef.initLogging(props);

      logMsg("Execute TestBean:initializeTimer");
      testResult = testBeanRef.initializeTimer(TimerImpl.TIMER_SINGLEEVENT,
          TimerImpl.ALLOWED);

      if (testResult) {
        logMsg("initializeTimer succeeded - receiving message");
        msg = TimerImpl.getMessage(queue, qcFactory, msgTimeout);
        testResult = msg.equals(TimerImpl.ALLOW_OK);
      }
      if (!testResult)
        throw new Fault("ejbTimeoutAllowedMethodsTest failed");
      else
        logMsg("ejbTimeoutAllowedMethodsTest passed");
    } catch (Exception e) {
      throw new Fault("ejbTimeoutAllowedMethodsTest failed", e);
    } finally {
      cleanupTestBean(testBeanRef);
    }
  }

  protected void cleanupTestBean(TestBean testBeanRef) {
    try {
      if (testBeanRef == null)
        testBeanRef = (TestBean) testBeanHome.create();
      testBeanRef.cancelAllTimers();
    } catch (Exception e) {
      TimerImpl.handleException("Exception in cancelAllTimers", e);
    }
    if (testBeanRef != null)
      try {
        testBeanRef.remove();
      } catch (Exception e) {
        TimerImpl.handleException("Exception while removing bean", e);
      }
  }

  protected void cleanupProxyBean(ProxyBean proxyBeanRef) {
    try {
      if (proxyBeanRef == null)
        proxyBeanRef = (ProxyBean) proxyBeanHome.create(props);
      proxyBeanRef.cleanup();
    } catch (Exception e) {
      TimerImpl.handleException("Exception while cleaning up proxy bean", e);
    }
    if (proxyBeanRef != null)
      try {
        proxyBeanRef.remove();
      } catch (Exception e) {
        TimerImpl.handleException("Exception while removing proxy bean", e);
      }
  }

  public void cleanup() throws Fault {
    TimerImpl.flushQueue(queue, qcFactory);
    logMsg("cleanup ok");
  }
}
