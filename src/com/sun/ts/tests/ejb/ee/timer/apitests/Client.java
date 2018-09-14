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
 * @(#)Client.java	1.3 03/05/16
 */

package com.sun.ts.tests.ejb.ee.timer.apitests;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.util.*;
import javax.ejb.*;
import java.rmi.*;

import com.sun.javatest.Status;

public class Client extends EETest {
  private static final String testName = "apiTimerTests";

  private static final String testLookup = "java:comp/env/ejb/TimerBeanEJB";

  private TimerBean beanRef = null;

  private TimerBeanHome beanHome = null;

  private Properties props = null;

  private TSNamingContext nctx = null;

  private long timerDuration = 0;

  private long timerWait = 0;

  private static final String user = "user", password = "password";

  private String user_value, password_value;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   * ejb_timeout; ejb_wait; user; password;
   * 
   * @class.testArgs:
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    user_value = props.getProperty(user);
    password_value = props.getProperty(password);

    logMsg("user_value=" + user_value);
    logMsg("password_value=" + password_value);

    try {
      logMsg("Obtain naming context");
      nctx = new TSNamingContext();
      logMsg("Obtain login context and login as: " + user_value);
      TSLoginContext lc = new TSLoginContext();
      lc.login(user_value, password_value);

      // Get EJB Home ...
      logMsg("Looking up home interface for EJB: " + testLookup);
      beanHome = (TimerBeanHome) nctx.lookup(testLookup, TimerBeanHome.class);

      timerDuration = Long.parseLong(p.getProperty("ejb_timeout"));
      timerWait = Long.parseLong(p.getProperty("ejb_wait"));

      logMsg("Setup ok");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("Setup failed:", e);
    }
  }

  /* Run test */

  /*
   * @testName: apiTimerTest1
   * 
   * @assertion_ids: EJB:JAVADOC:215
   *
   * @test_Strategy: Pass a negative duration value to createTimer and verify an
   * IllegalArgumentException is thrown.
   *
   */

  public void apiTimerTest1() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TimerBean) beanHome.create();
      beanRef.initLogging(props);
      pass = beanRef.test1();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("apiTimerTest1 failed", e);
    }

    if (beanRef != null)
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.logErr("Unexpected Exception caught removing bean:" + e, e);
      }

    if (!pass)
      throw new Fault("apiTimerTest1 failed");
  }

  /*
   * @testName: apiTimerTest2
   * 
   * @assertion_ids: EJB:JAVADOC:219
   * 
   * @test_Strategy: Pass a negative initialDuration value to createTimer and
   * verify an IllegalArgumentException is thrown.
   * 
   */

  public void apiTimerTest2() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TimerBean) beanHome.create();
      beanRef.initLogging(props);
      pass = beanRef.test2();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("apiTimerTest2 failed", e);
    }

    if (beanRef != null)
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.logErr("Unexpected Exception caught removing bean:" + e, e);
      }

    if (!pass)
      throw new Fault("apiTimerTest2 failed");
  }

  /*
   * @testName: apiTimerTest3
   * 
   * @assertion_ids: EJB:JAVADOC:219
   * 
   * @test_Strategy: Pass a negative intervalDuration value to createTimer and
   * verify an IllegalArgumentException is thrown.
   * 
   */

  public void apiTimerTest3() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TimerBean) beanHome.create();
      beanRef.initLogging(props);
      pass = beanRef.test3();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("apiTimerTest3 failed", e);
    }

    if (beanRef != null)
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.logErr("Unexpected Exception caught removing bean:" + e, e);
      }

    if (!pass)
      throw new Fault("apiTimerTest3 failed");
  }

  /*
   * @testName: apiTimerTest4
   * 
   * @assertion_ids: EJB:JAVADOC:223
   * 
   * @test_Strategy: Pass a null expiration value to createTimer and verify an
   * IllegalArgumentException is thrown.
   * 
   */

  public void apiTimerTest4() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TimerBean) beanHome.create();
      beanRef.initLogging(props);
      pass = beanRef.test4();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("apiTimerTest4 failed", e);
    }

    if (beanRef != null)
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.logErr("Unexpected Exception caught removing bean:" + e, e);
      }

    if (!pass)
      throw new Fault("apiTimerTest4 failed");
  }

  /*
   * @testName: apiTimerTest5
   * 
   * @assertion_ids: EJB:JAVADOC:227
   * 
   * @test_Strategy: If initialExpiration is null, verify createTimer throws an
   * IllegalArgumentException.
   * 
   */

  public void apiTimerTest5() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TimerBean) beanHome.create();
      beanRef.initLogging(props);
      pass = beanRef.test5();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("apiTimerTest5 failed", e);
    }

    if (beanRef != null)
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.logErr("Unexpected Exception caught removing bean:" + e, e);
      }

    if (!pass)
      throw new Fault("apiTimerTest5 failed");
  }

  /*
   * @testName: apiTimerTest6
   * 
   * @assertion_ids: EJB:JAVADOC:192
   * 
   * @test_Strategy: Ensure that a NoSuchObjectLocalException is thrown when
   * attempting to cancel a timer that has already been cancelled.
   * 
   */

  public void apiTimerTest6() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TimerBean) beanHome.create();
      beanRef.initLogging(props);
      pass = beanRef.test6();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("apiTimerTest6 failed", e);
    }

    if (beanRef != null)
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.logErr("Unexpected Exception caught removing bean:" + e, e);
      }

    if (!pass)
      throw new Fault("apiTimerTest6 failed");
  }

  /*
   * @testName: apiTimerTest7
   * 
   * @assertion_ids: EJB:JAVADOC:208
   * 
   * @test_Strategy: Ensure that a NoSuchObjectLocalException is thrown when
   * attempting to getTimeRemaining on a timer that has already been cancelled.
   * 
   */

  public void apiTimerTest7() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TimerBean) beanHome.create();
      beanRef.initLogging(props);
      pass = beanRef.test7();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("apiTimerTest7 failed", e);
    }

    if (beanRef != null)
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.logErr("Unexpected Exception caught removing bean:" + e, e);
      }

    if (!pass)
      throw new Fault("apiTimerTest7 failed");
  }

  /*
   * @testName: apiTimerTest8
   * 
   * @assertion_ids: EJB:JAVADOC:204
   * 
   * @test_Strategy: Ensure that a NoSuchObjectLocalException is thrown when
   * attempting to getNextTimeout on a timer that has already been cancelled.
   * 
   */

  public void apiTimerTest8() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TimerBean) beanHome.create();
      beanRef.initLogging(props);
      pass = beanRef.test8();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("apiTimerTest8 failed", e);
    }

    if (beanRef != null)
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.logErr("Unexpected Exception caught removing bean:" + e, e);
      }

    if (!pass)
      throw new Fault("apiTimerTest8 failed");
  }

  /*
   * @testName: apiTimerTest9
   * 
   * @assertion_ids: EJB:JAVADOC:200
   * 
   * @test_Strategy: Ensure that a NoSuchObjectLocalException is thrown when
   * attempting to getInfo on a timer that has already been cancelled.
   * 
   */

  public void apiTimerTest9() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TimerBean) beanHome.create();
      beanRef.initLogging(props);
      pass = beanRef.test9();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("apiTimerTest9 failed", e);
    }

    if (beanRef != null)
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.logErr("Unexpected Exception caught removing bean:" + e, e);
      }

    if (!pass)
      throw new Fault("apiTimerTest9 failed");
  }

  /*
   * @testName: apiTimerTest10
   * 
   * @assertion_ids: EJB:JAVADOC:196
   * 
   * @test_Strategy: Ensure that a NoSuchObjectLocalException is thrown when
   * attempting to getHandle on a timer that has already been cancelled.
   * 
   */

  public void apiTimerTest10() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TimerBean) beanHome.create();
      beanRef.initLogging(props);
      pass = beanRef.test10();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("apiTimerTest10 failed", e);
    }

    if (beanRef != null)
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.logErr("Unexpected Exception caught removing bean:" + e, e);
      }

    if (!pass)
      throw new Fault("apiTimerTest10 failed");
  }

  public void cleanup() throws Fault {
    try {
      beanRef = (TimerBean) beanHome.create();
      beanRef.findAndCancelTimer();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception caught removing timers:" + e, e);
    }
    logMsg("cleanup ok");
  }
}
