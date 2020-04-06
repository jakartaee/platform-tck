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
 * @(#)SBeanEJB.java	1.8 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.entity.cmp20.nonreentranttest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import jakarta.ejb.*;
import java.rmi.*;

public class SBeanEJB implements SessionBean {
  private SessionContext sctx = null;

  private Properties harnessProps = null;

  private TSNamingContext nctx = null;

  private static final String beanLocal = "java:comp/env/ejb/TestBeanLocal";

  private TestBeanLocal beanLocalRef = null;

  private TestBeanLocalHome beanLocalHome = null;

  // ===========================================================
  // private methods

  private TestBeanLocalHome getTestBeanHome() throws Exception {
    TestUtil.logTrace("getTestBeanHome");
    beanLocalHome = (TestBeanLocalHome) nctx.lookup(beanLocal);
    return beanLocalHome;
  }

  private TestBeanLocal createTestBean(int id, String name, float price)
      throws Exception {
    TestUtil.logTrace("createTestBean");
    beanLocalHome = (TestBeanLocalHome) nctx.lookup(beanLocal);
    beanLocalRef = beanLocalHome.create(harnessProps, id, name, price);
    return beanLocalRef;
  }

  // ===========================================================
  // EJB Specification Required Methods

  public void ejbCreate(Properties p) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    harnessProps = p;
    try {
      TestUtil.logMsg("initialize remote logging");
      TestUtil.init(p);
      TestUtil.logMsg("obtain naming context");
      nctx = new TSNamingContext();
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new CreateException(e.getMessage());
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("unable to obtain naming context");
    }
  }

  public void setSessionContext(SessionContext sc) {
    TestUtil.logTrace("setSessionContext");
    this.sctx = sc;
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

  public void setBeanRef(TestBean ref) {
    TestUtil.logTrace("setBeanRef");
    try {
      beanLocalHome = getTestBeanHome();
      Integer pk = (Integer) ref.getPrimaryKey();
      beanLocalRef = (TestBeanLocal) beanLocalHome.findByPrimaryKey(pk);
    } catch (Exception e) {
      throw new EJBException(e);
    }
  }

  public boolean sleep(int n) {
    TestUtil.logTrace("sleep");
    try {
      beanLocalRef.sleep(n);
      return true;
    } catch (EJBException e) {
      TestUtil.printStackTrace(e);
      return false;
    }
  }

  public boolean loopBackSameBeanLocal() {
    TestUtil.logTrace("loopBackSameBeanLocal");

    boolean pass = true;

    TestBeanLocal ref = null;
    TestUtil.logMsg("Perform loopback test - local");
    try {
      // create EJB instance
      TestUtil.logMsg("Create Local EJB instance");
      ref = createTestBean(1, "coffee-1", (float) 1.0);
      TestUtil.logMsg("Performing self-referential loopback call test");
      pass = ref.loopBackSameBeanLocal();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg("Exception: " + e);
      pass = false;
    } finally {
      try {
        ref.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return pass;
  }

  public boolean loopBackAnotherBeanLocal() {
    TestUtil.logTrace("loopBackAnotherBeanLocal");

    boolean pass = true;

    TestBeanLocal ref = null;
    try {
      // create EJB instance
      TestUtil.logMsg("Create Local EJB instance");
      ref = createTestBean(1, "coffee-1", (float) 1.0);
      TestUtil.logMsg("Performing loopback call test");
      pass = ref.loopBackAnotherBeanLocal();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg("Exception: " + e);
      pass = false;
    } finally {
      try {
        ref.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return pass;
  }

  // ===========================================================
}
