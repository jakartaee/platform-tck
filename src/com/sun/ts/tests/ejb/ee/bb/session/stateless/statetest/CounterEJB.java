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
 * @(#)CounterEJB.java	1.16 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.session.stateless.statetest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import javax.ejb.*;
import java.rmi.*;
import java.util.*;

public class CounterEJB implements SessionBean {
  private SessionContext sctx;

  public void ejbCreate() throws CreateException {
    TestUtil.logTrace("ejbCreate");
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

  public int decrement(int value) throws InvalidTransactionException {
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

  public int decrement(int value, int n) throws InvalidTransactionException {
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

  public int increment(int value) {
    TestUtil.logTrace("increment");
    TestUtil.logMsg("counter value before transaction: " + value);
    TestUtil.logMsg("increment counter by 1");
    value += 1;
    TestUtil.logMsg("counter value after transaction: " + value);
    return value;
  }

  public int increment(int value, int n) {
    TestUtil.logTrace("increment");
    TestUtil.logMsg("counter value before transaction: " + value);
    TestUtil.logMsg("increment counter by " + n);
    value += n;
    TestUtil.logMsg("counter value after transaction: " + value);
    return value;
  }

  public int value(int value) {
    TestUtil.logTrace("value");
    TestUtil.logMsg("counter value: " + value);
    return value;
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
