/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * @(#)TimerBeanEJB.java	1.3 03/05/16
 */

package com.sun.ts.tests.ejb.ee.timer.apitests;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import com.sun.ts.lib.util.RemoteLoggingInitException;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJBException;
import jakarta.ejb.NoSuchObjectLocalException;
import jakarta.ejb.SessionBean;
import jakarta.ejb.SessionContext;
import jakarta.ejb.TimedObject;

public class TimerBeanEJB implements SessionBean, TimedObject {
  private SessionContext sctx = null;

  private TSNamingContext nctx = null;

  public void ejbCreate() throws CreateException {
  }

  public void setSessionContext(SessionContext sc) {
    TestUtil.logTrace("setSessionContext");
    this.sctx = sc;
  }

  public void ejbRemove() {
  }

  public void ejbActivate() {
  }

  public void ejbPassivate() {
  }

  public void ejbTimeout(jakarta.ejb.Timer timer) {
  }

  /*
   * ====================== Test Methods ======================
   */
  public boolean test1() {
    TestUtil.logTrace("test1");
    boolean pass = true;
    long duration = -1;
    String info = "test1";
    try {
      jakarta.ejb.TimerService ts = sctx.getTimerService();
      TestUtil.logTrace("Create Timer");
      jakarta.ejb.Timer t = ts.createTimer(duration, info);
      TestUtil.logErr("No IllegalArgumentException occurred - unexpected");
      pass = false;
    } catch (IllegalArgumentException e) {
      TestUtil.logMsg("IllegalArgumentException occurred - expected");
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception: " + e, e);
      pass = false;
    }
    return pass;
  }

  public boolean test2() {
    TestUtil.logTrace("test2");
    boolean pass = true;
    long initialDuration = -1;
    long intervalDuration = 10;
    String info = "test2";
    try {
      jakarta.ejb.TimerService ts = sctx.getTimerService();
      TestUtil.logTrace("Create Timer");
      jakarta.ejb.Timer t = ts.createTimer(initialDuration, intervalDuration,
          info);
      TestUtil.logErr("No IllegalArgumentException occurred - unexpected");
      pass = false;
    } catch (IllegalArgumentException e) {
      TestUtil.logMsg("IllegalArgumentException occurred - expected");
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception: " + e, e);
      pass = false;
    }
    return pass;
  }

  public boolean test3() {
    TestUtil.logTrace("test3");
    boolean pass = true;
    long initialDuration = 10;
    long intervalDuration = -1;
    String info = "test3";
    try {
      jakarta.ejb.TimerService ts = sctx.getTimerService();
      TestUtil.logTrace("Create Timer");
      jakarta.ejb.Timer t = ts.createTimer(initialDuration, intervalDuration,
          info);
      TestUtil.logErr("No IllegalArgumentException occurred - unexpected");
      pass = false;
    } catch (IllegalArgumentException e) {
      TestUtil.logMsg("IllegalArgumentException occurred - expected");
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception: " + e, e);
      pass = false;
    }
    return pass;
  }

  public boolean test4() {
    TestUtil.logTrace("test4");
    boolean pass = true;
    java.util.Date d = null;
    String info = "test4";
    try {
      jakarta.ejb.TimerService ts = sctx.getTimerService();
      TestUtil.logTrace("Create Timer");
      jakarta.ejb.Timer t = ts.createTimer(d, info);
      TestUtil.logErr("No IllegalArgumentException occurred - unexpected");
      pass = false;
    } catch (IllegalArgumentException e) {
      TestUtil.logMsg("IllegalArgumentException occurred - expected");
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception: " + e, e);
      pass = false;
    }
    return pass;
  }

  public boolean test5() {
    TestUtil.logTrace("test5");
    boolean pass = true;
    java.util.Date d = null;
    long intervalDuration = 10;
    String info = "test5";
    try {
      jakarta.ejb.TimerService ts = sctx.getTimerService();
      TestUtil.logTrace("Create Timer");
      jakarta.ejb.Timer t = ts.createTimer(d, intervalDuration, info);
      TestUtil.logErr("No IllegalArgumentException occurred - unexpected");
      pass = false;
    } catch (IllegalArgumentException e) {
      TestUtil.logMsg("IllegalArgumentException occurred - expected");
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception: " + e, e);
      pass = false;
    }
    return pass;
  }

  public boolean test6() {
    TestUtil.logTrace("test6");
    boolean pass = true;
    long duration = 30000;
    String info = "test6";
    try {
      jakarta.ejb.TimerService ts = sctx.getTimerService();
      TestUtil.logTrace("Create valid timer");
      jakarta.ejb.Timer t = ts.createTimer(duration, info);
      TestUtil.logTrace("Cancel timer");
      t.cancel();
      TestUtil
          .logTrace("Cancel timer again and expect NoSuchObjectLocalException");
      t.cancel();
    } catch (NoSuchObjectLocalException e) {
      TestUtil.logMsg("NoSuchObjectLocalException occurred - expected");
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception: " + e, e);
      pass = false;
    }
    return pass;
  }

  public boolean test7() {
    TestUtil.logTrace("test7");
    boolean pass = true;
    long duration = 30000;
    String info = "test7";
    try {
      jakarta.ejb.TimerService ts = sctx.getTimerService();
      TestUtil.logTrace("create valid timer");
      jakarta.ejb.Timer t = ts.createTimer(duration, info);
      TestUtil.logTrace("cancel timer");
      t.cancel();
      TestUtil.logTrace(
          "Invoke getTimeRemaining and expect a NoSuchObjectLocalException");
      t.getTimeRemaining();
    } catch (NoSuchObjectLocalException e) {
      TestUtil.logMsg("NoSuchObjectLocalException occurred - expected");
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception: " + e, e);
      pass = false;
    }
    return pass;
  }

  public boolean test8() {
    TestUtil.logTrace("test8");
    boolean pass = true;
    long expiration = (System.currentTimeMillis() + (long) 900000);
    java.util.Date d = new Date(expiration);
    String info = "test8";
    try {
      jakarta.ejb.TimerService ts = sctx.getTimerService();
      TestUtil.logTrace("create valid timer");
      jakarta.ejb.Timer t = ts.createTimer(d, info);
      TestUtil.logTrace("cancel timer");
      t.cancel();
      TestUtil.logTrace(
          "Invoke getNextTimeout and expect NoSuchObjectLocalException");
      t.getNextTimeout();
    } catch (NoSuchObjectLocalException e) {
      TestUtil.logMsg("NoSuchObjectLocalException occurred - expected");
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception: " + e, e);
      pass = false;
    }
    return pass;
  }

  public boolean test9() {
    TestUtil.logTrace("test9");
    boolean pass = true;
    long duration = 30000;
    String info = "test9";
    try {
      jakarta.ejb.TimerService ts = sctx.getTimerService();
      TestUtil.logTrace("Create valid timer");
      jakarta.ejb.Timer t = ts.createTimer(duration, info);
      TestUtil.logTrace("Cancel timer");
      t.cancel();
      TestUtil.logTrace("Invoke getInfo and expect NoSuchObjectLocalException");
      t.getInfo();
    } catch (NoSuchObjectLocalException e) {
      TestUtil.logMsg("NoSuchObjectLocalException occurred - expected");
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception: " + e, e);
      pass = false;
    }
    return pass;
  }

  public boolean test10() {
    TestUtil.logTrace("test10");
    boolean pass = true;
    long duration = 30000;
    String info = "test10";
    try {
      jakarta.ejb.TimerService ts = sctx.getTimerService();
      TestUtil.logTrace("Create valid timer");
      jakarta.ejb.Timer t = ts.createTimer(duration, info);
      TestUtil.logTrace("Cancel timer");
      t.cancel();
      TestUtil
          .logTrace("Invoke getHandle and expect NoSuchObjectLocalException");
      t.getHandle();
    } catch (NoSuchObjectLocalException e) {
      TestUtil.logMsg("NoSuchObjectLocalException occurred - expected");
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception: " + e, e);
      pass = false;
    }
    return pass;
  }

  /*
   * ====================== Miscellaneous Methods ======================
   */

  public void findAndCancelTimer() {
    try {
      TestUtil.logTrace("findTimer method entered");
      jakarta.ejb.TimerService ts = sctx.getTimerService();
      TestUtil.logTrace("find Timers");
      Collection ccol = ts.getTimers();
      Iterator i = ccol.iterator();
      while (i.hasNext()) {
        jakarta.ejb.Timer t = (jakarta.ejb.Timer) i.next();
        t.cancel();
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("findTimer:" + e);
    }
  }

  public void initLogging(Properties p) {
    TestUtil.logTrace("initLogging");
    try {
      TestUtil.init(p);
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    }
  }
}
