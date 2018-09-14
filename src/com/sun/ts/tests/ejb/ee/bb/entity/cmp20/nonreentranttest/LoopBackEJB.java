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
 * @(#)LoopBackEJB.java	1.5 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.entity.cmp20.nonreentranttest;

import com.sun.ts.lib.util.*;

import java.util.*;
import javax.ejb.*;
import java.rmi.*;

public class LoopBackEJB implements SessionBean {
  private SessionContext sctx = null;

  private Properties harnessProps = null;

  private TestBean ref = null;

  private TestBeanLocal refLocal = null;

  public void ejbCreate() throws CreateException {
    TestUtil.logTrace("ejbCreate");
  }

  public void setSessionContext(SessionContext sc) {
    TestUtil.logTrace("setSessionContext");
    sctx = sc;
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

  // ===========================================================
  // LoopBack interface (our business methods)

  public boolean loopBackTest() {
    TestUtil.logTrace("loopBackTest");

    boolean pass;

    TestUtil.logMsg("Perform loopback call test");
    try {
      ref.ping();
      TestUtil.logErr("No exception occurred during loopback call");
      pass = false;
    } catch (RemoteException e) {
      TestUtil.logMsg("Caught RemoteException as expected: " + e);
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  public boolean loopBackTestLocal() {
    TestUtil.logTrace("loopBackTestLocal");

    boolean pass;

    TestUtil.logMsg("Perform loopback call test");
    try {
      refLocal.ping();
      TestUtil.logErr("No exception occurred during loopback call");
      pass = false;
    } catch (EJBException e) {
      TestUtil.logMsg("Caught EJBException as expected: " + e);
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  public void setBeanRef(TestBean r) {
    TestUtil.logTrace("setBeanRef");
    ref = r;
  }

  public void setBeanRefLocal(TestBeanLocal r) {
    TestUtil.logTrace("setBeanRef");
    refLocal = r;
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

  // ===========================================================
}
