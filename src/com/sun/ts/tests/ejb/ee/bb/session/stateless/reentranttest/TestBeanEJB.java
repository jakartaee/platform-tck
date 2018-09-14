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
 * @(#)TestBeanEJB.java	1.15 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.session.stateless.reentranttest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.ejb.*;
import java.rmi.*;

public class TestBeanEJB implements SessionBean {
  private SessionContext sctx = null;

  private static final String beanName = "java:comp/env/ejb/LoopBack";

  private static final String beanName2 = "java:comp/env/ejb/LoopBackLocal";

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
  // TestBean interface (our business methods)

  public void ping() {
    TestUtil.logTrace("ping");
  }

  public void sleep(int n) {
    TestUtil.logTrace("sleep");
    long t1, t2;
    t1 = System.currentTimeMillis();
    while ((t2 = System.currentTimeMillis()) < (t1 + n))
      ;
  }

  public boolean loopBackSameBean() {
    TestUtil.logTrace("loopBackSameBean");

    boolean pass;

    TestUtil.logMsg("perform loopback test");
    try {
      TestUtil.logMsg("getEJBObject reference");
      TestBean ref = (TestBean) sctx.getEJBObject();
      TestUtil.logMsg("Performing self-referential loopback call test");
      ref.ping();
      TestUtil.logMsg("No exception occurred during loopback call");
      pass = true;
    } catch (RemoteException e) {
      TestUtil.logErr("Caught RemoteException - unexpected: " + e, e);
      pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  public boolean loopBackSameBeanLocal() {
    TestUtil.logTrace("loopBackSameBean");

    boolean pass;

    TestUtil.logMsg("perform loopback test");
    try {
      TestUtil.logMsg("getEJBObject reference");
      TestBeanLocal ref = (TestBeanLocal) sctx.getEJBLocalObject();
      TestUtil.logMsg("Performing self-referential loopback call test");
      ref.ping();
      TestUtil.logErr("No exception occurred during loopback call");
      pass = true;
    } catch (EJBException e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg("Caught EJBException - unexpected: " + e);
      pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  public boolean loopBackAnotherBean(Properties p) {
    TestUtil.logTrace("loopBackAnotherBean");

    boolean pass;

    try {
      // lookup EJB home
      TestUtil.logMsg("lookup home interface for EJB: " + beanName);
      LoopBackHome beanHome = (LoopBackHome) lookup(beanName,
          LoopBackHome.class);
      // create EJB instance
      TestUtil.logMsg("Create EJB instance");
      LoopBack beanRef = (LoopBack) beanHome
          .create((TestBean) sctx.getEJBObject());
      TestUtil.logMsg("initialize remote logging");
      beanRef.initLogging(p);
      TestUtil.logMsg("Performing loopback call test");
      pass = beanRef.loopBackTest();
      beanRef.remove();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  public boolean loopBackAnotherBeanLocal() {
    TestUtil.logTrace("loopBackAnotherBean");

    boolean pass;

    try {
      // lookup EJB home
      TestUtil.logMsg("lookup home interface for EJB: " + beanName);
      LoopBackLocalHome beanLocalHome = (LoopBackLocalHome) lookup(beanName2,
          LoopBackLocalHome.class);
      // create EJB instance
      TestUtil.logMsg("Create EJB instance");
      LoopBackLocal beanLocalRef = (LoopBackLocal) beanLocalHome
          .create((TestBeanLocal) sctx.getEJBLocalObject());
      TestUtil.logMsg("Performing loopback call test");
      pass = beanLocalRef.loopBackTestLocal();
      beanLocalRef.remove();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
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

  // ===========================================================

  private Object lookup(String s, Class c) {
    TSNamingContext nctx = null;
    try {
      TestUtil.logMsg("obtain naming context");
      nctx = new TSNamingContext();
      if (c != null)
        return nctx.lookup(s, c);
      else
        return nctx.lookup(s);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("lookup failed: " + e);
    }
  }
}
