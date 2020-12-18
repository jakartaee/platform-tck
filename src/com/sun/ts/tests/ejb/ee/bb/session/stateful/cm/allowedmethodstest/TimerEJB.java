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
 * @(#)TimerEJB.java	1.3 03/05/27
 */

package com.sun.ts.tests.ejb.ee.bb.session.stateful.cm.allowedmethodstest;

import java.util.Collection;
import java.util.Iterator;

import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJBException;
import jakarta.ejb.SessionBean;
import jakarta.ejb.SessionContext;
import jakarta.ejb.TimedObject;
import jakarta.ejb.TimerHandle;

public class TimerEJB implements SessionBean, TimedObject {
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

  public TimerHandle startTimer(long duration, String info) {
    try {
      TestUtil.logTrace("startTimer method entered");
      jakarta.ejb.TimerService ts = sctx.getTimerService();
      TestUtil.logTrace("create Timer");
      jakarta.ejb.Timer t = ts.createTimer(duration, info);
      return t.getHandle();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("startTimer:" + e);
    }
  }

  public void findAndCancelTimer() {
    Collection ccol = null;
    try {
      TestUtil.logTrace("findAndCancelTimer method entered");
      jakarta.ejb.TimerService ts = sctx.getTimerService();
      TestUtil.logTrace("getTimers");
      ccol = ts.getTimers();
      if (!ccol.isEmpty()) {
        TestUtil.logTrace("Timer Collection Not Empty");
        Iterator i = ccol.iterator();
        while (i.hasNext()) {
          TestUtil.logTrace("Looking up next timer");
          jakarta.ejb.Timer t = (jakarta.ejb.Timer) i.next();
          TestUtil.logTrace("Cancel timer with info: " + t.getInfo());
          t.cancel();
        }
      } else {
        TestUtil.logTrace("Timer Collection is Empty");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("findAndCancelTimer: " + e);
    }
  }

  // ===========================================================

}
