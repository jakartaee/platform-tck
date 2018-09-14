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

package com.sun.ts.tests.ejb30.common.allowed.stateful;

import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import java.util.Properties;
import javax.ejb.SessionContext;
import com.sun.ts.tests.ejb30.common.allowed.Constants;
import com.sun.ts.tests.ejb30.common.allowed.Operations;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import javax.ejb.EJBException;
import javax.ejb.ScheduleExpression;
import javax.ejb.TimerHandle;

public class StatefulOperations extends Operations implements Constants {

  private static StatefulOperations instance = new StatefulOperations();

  protected StatefulOperations() {
    super();
  }

  public static StatefulOperations getInstance() {
    return instance;
  }

  @Override
  public void runMessageContext(SessionContext sctx, Properties results) {
    // getMessageContext test
    // try {
    // sctx.getMessageContext();
    // results.setProperty(getMessageContext, allowed);
    // } catch (IllegalStateException e) {
    // results.setProperty(getMessageContext, disallowed);
    // } catch (Exception e) {
    // results.setProperty(getMessageContext, e.toString());
    // }
  }

  @Override
  public void runRollbackOnly(SessionContext sctx, Properties results) {
    // getRollbackOnly test
    try {
      sctx.getRollbackOnly();
      results.setProperty(getRollbackOnly, allowed);
    } catch (IllegalStateException e) {
      results.setProperty(getRollbackOnly, disallowed);
    } catch (Exception e) {
      results.setProperty(getRollbackOnly, e.toString());
    }

    // setRollbackOnly test
    // try {
    // sctx.setRollbackOnly();
    // results.setProperty(setRollbackOnly, allowed);
    // } catch (IllegalStateException e) {
    // results.setProperty(setRollbackOnly, disallowed);
    // } catch (Exception e) {
    // results.setProperty(setRollbackOnly, e.toString());
    // }

  }

  public TimerLocalIF getTimerBean() {
    try {
      TimerLocalIF timerRef = (TimerLocalIF) ServiceLocator.lookup(timerLookup,
          TimerLocalIF.class);
      // TLogger.log("## got timer bean");
      return timerRef;
    } catch (Exception e) {
      // TLogger.log("### failed to get timer bean ");
      // e.printStackTrace();
      throw new EJBException("Failed to get timer bean:", e);
    }
  }

  public TimerHandle getTimeHandle() throws Exception {
    TimerLocalIF timerBean = getTimerBean();
    return timerBean.startTimer(60000, "info");
  }

  @Override
  public void runTimers(SessionContext sctx, Properties results) {
    try {
      TimerHandle th = getTimeHandle();
      javax.ejb.Timer t = th.getTimer();
      TLogger
          .logMsg("Got timer: " + t.toString() + "; hashCode: " + t.hashCode());

      th = t.getHandle();
      TLogger.logMsg("Got timer handle: " + th.toString() + "; hashCode: "
          + th.hashCode());

      long tRemaining = t.getTimeRemaining();
      TLogger.logMsg("Time remaining is " + tRemaining);

      java.util.Date tNextTimeout = t.getNextTimeout();
      TLogger.logMsg("Next timeout is " + tNextTimeout);

      String tInfo = (String) t.getInfo();
      TLogger.logMsg("Timer info is " + tInfo);

      if (t.isCalendarTimer()) {
        ScheduleExpression exp = t.getSchedule();
        TLogger.logMsg("ScheduleExpression is " + exp);
      }

      TLogger.logMsg("Timer isPersistent: " + t.isPersistent());

      results.setProperty(Timer_Methods, allowed);
    } catch (IllegalStateException e) {
      results.setProperty(Timer_Methods, disallowed);
    } catch (Exception e) {
      // e.printStackTrace();
      results.setProperty(Timer_Methods, e.toString());
    }
  }

  public void tryRollback(SessionContext sctx)
      throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
    super.tryRollback(sctx);
    try {
      sctx.setRollbackOnly();
      throw new TestFailedException(
          "Expecting IllegalStateException from setRollbackOnly(),"
              + " but got no exception.");
    } catch (IllegalStateException e) {
      // good
    } catch (Exception e) {
      throw new TestFailedException(
          "Expecting IllegalStateException from setRollbackOnly(), "
              + " but got " + e);
    }
  }
}
