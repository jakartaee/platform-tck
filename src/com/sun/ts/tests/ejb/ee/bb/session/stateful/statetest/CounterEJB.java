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
 * @(#)CounterEJB.java	1.12 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.session.stateful.statetest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import javax.ejb.*;
import java.rmi.*;
import java.util.*;

public class CounterEJB implements SessionBean, SessionSynchronization {
  // State variables
  // o counter number
  // o counter value
  public int number;

  public int value;

  public boolean rollBackStatusFlag = false;

  public boolean bval = false;

  public boolean firstFlag = true;

  private SessionContext sctx;

  private Properties props;

  private Properties harnessProps;

  public void ejbCreate() throws CreateException {
    TestUtil.logTrace("ejbCreate");
    this.number = 1;
    this.value = 0;
    this.bval = false;
    TestUtil.logMsg("counter object created");
    TestUtil.logMsg("number	= " + this.number);
    TestUtil.logMsg("value	= " + this.value);
  }

  public void ejbCreate(int n, int v) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    this.number = n;
    this.value = v;
    this.bval = false;
    TestUtil.logMsg("counter object created");
    TestUtil.logMsg("number	= " + this.number);
    TestUtil.logMsg("value	= " + this.value);
  }

  public void setSessionContext(SessionContext sctx) {
    TestUtil.logTrace("setSessionContext");
    this.sctx = sctx;
  }

  public void ejbRemove() {
    TestUtil.logTrace("ejbRemove");
  }

  public void ejbActivate() {
    TestUtil.logTrace("ejbActivate");
  }

  public void ejbPassivate() {
    TestUtil.logTrace("ejbPassivate");
  }

  // ==============================================================
  // SessionSynchronization Interface Methods
  // ==============================================================

  public void afterBegin() {
    TestUtil.logMsg("**********************************************");
    TestUtil.logMsg("afterBegin() : " + this.getClass().getName());
    TestUtil.logMsg("**********************************************");
  }

  public void beforeCompletion() {
    TestUtil.logMsg("**********************************************");
    TestUtil.logMsg("beforeCompletion() : " + this.getClass().getName());
    TestUtil.logMsg("**********************************************");
  }

  public void afterCompletion(boolean status) {

    if (firstFlag) {
      TestUtil.logMsg("This is the first time through afterCompletion");
      rollBackStatusFlag = status;
      firstFlag = false;
    }

    TestUtil.logMsg("**********************************************");
    TestUtil.logMsg("afterCompletion status [" + status + "] : "
        + this.getClass().getName());
    TestUtil.logMsg("**********************************************");
  }

  public int decrement() throws InvalidTransactionException {
    TestUtil.logTrace("decrement");
    if ((value - 1) < 0)
      throw new InvalidTransactionException(
          "attempt to set counter to negative value");
    else {
      TestUtil.logMsg("counter value before transaction: " + value);
      TestUtil.logMsg("decrement counter by 1");
      value -= 1;
      TestUtil.logMsg("counter value after transaction: " + value);
    }
    return value;
  }

  public int decrement(int n) throws InvalidTransactionException {
    TestUtil.logTrace("decrement");
    if ((value - n) < 0)
      throw new InvalidTransactionException(
          "attempt to set counter to negative value");
    else {
      TestUtil.logMsg("counter value before transaction: " + value);
      TestUtil.logMsg("decrement counter by " + n);
      value -= n;
      TestUtil.logMsg("counter value after transaction: " + value);
    }
    return value;
  }

  public int increment() {
    TestUtil.logTrace("increment");
    TestUtil.logMsg("counter value before transaction: " + value);
    TestUtil.logMsg("increment counter by 1");
    value += 1;
    TestUtil.logMsg("counter value after transaction: " + value);
    return value;
  }

  public int increment(int n) {
    TestUtil.logTrace("increment");
    TestUtil.logMsg("counter value before transaction: " + value);
    TestUtil.logMsg("increment counter by " + n);
    value += n;
    TestUtil.logMsg("counter value after transaction: " + value);
    return value;
  }

  public int value() {
    TestUtil.logTrace("value");
    TestUtil.logMsg("counter value: " + value);
    return value;
  }

  public boolean getVal() {
    return bval;
  }

  public void setVal() {
    TestUtil.logMsg("This is in the setVal");
    TestUtil.logMsg("setRollbackOnly being set.");
    bval = true;
    sctx.setRollbackOnly();
  }

  public boolean getRollBackStatus() {
    return rollBackStatusFlag;
  }

  public void initLogging(Properties p) {
    TestUtil.logTrace("initLogging");
    harnessProps = p;
    try {
      TestUtil.init(p);
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    }
  }
}
