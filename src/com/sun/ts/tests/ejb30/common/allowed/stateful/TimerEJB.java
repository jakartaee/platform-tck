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

import com.sun.ts.tests.ejb30.common.helper.TLogger;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.EJBException;
import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.TimerHandle;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless(name = "TimerEJB")
@Local({ TimerLocalIF.class })
public class TimerEJB implements TimerLocalIF {
  private SessionContext sessionContext;

  @Resource
  public void setSessionContext(SessionContext sc) {
    this.sessionContext = sc;
  }

  @Timeout
  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  private void ejbTimeout(javax.ejb.Timer timer) {
  }

  // @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  public TimerHandle startTimer(long duration, String info) {
    try {
      javax.ejb.TimerService ts = sessionContext.getTimerService();
      TLogger.logTrace("create Timer");
      javax.ejb.Timer t = ts.createTimer(duration, info);
      return t.getHandle();
    } catch (Exception e) {
      TLogger.printStackTrace(e);
      throw new EJBException("startTimer:" + e);
    }
  }

  public void findAndCancelTimer() {
    Collection ccol = null;
    try {
      TLogger.logTrace("findAndCancelTimer method entered");
      javax.ejb.TimerService ts = sessionContext.getTimerService();
      TLogger.logTrace("getTimers");
      ccol = ts.getTimers();
      if (!ccol.isEmpty()) {
        TLogger.logTrace("Timer Collection Not Empty");
        Iterator i = ccol.iterator();
        while (i.hasNext()) {
          TLogger.logTrace("Looking up next timer");
          javax.ejb.Timer t = (javax.ejb.Timer) i.next();
          TLogger.logTrace("Cancel timer with info: " + t.getInfo());
          t.cancel();
        }
      } else {
        TLogger.logTrace("Timer Collection is Empty");
      }
    } catch (Exception e) {
      // ignore
    }
  }

  // ===========================================================

}
