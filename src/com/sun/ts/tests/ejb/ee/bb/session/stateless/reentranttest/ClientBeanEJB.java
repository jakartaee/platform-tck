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
 * @(#)ClientBeanEJB.java	1.8 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.session.stateless.reentranttest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.ejb.*;
import java.rmi.*;

public class ClientBeanEJB implements SessionBean {
  private SessionContext sctx = null;

  private Properties props = null;

  private static final String testLookup = "java:comp/env/ejb/TestBeanLocal";

  private TestBeanLocal beanLocalRef = null;

  private TestBeanLocalHome beanLocalHome = null;

  // ===========================================================
  // EJB Specification Required Methods

  public void ejbCreate(Properties p) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    props = p;
    try {
      TestUtil.logMsg("initialize remote logging");
      TestUtil.init(p);
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new CreateException(e.getMessage());
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
  // ClientBean interface (our business methods)

  public boolean loopBackSameBeanLocal() {
    TestUtil.logTrace("loopBackSameBeanLocal");

    boolean pass = true;

    TestUtil.logMsg("Perform loopback test - local");
    try {
      TestUtil.logMsg("Looking up the TestBean Local Home " + testLookup);
      beanLocalHome = (TestBeanLocalHome) lookup(testLookup,
          TestBeanLocalHome.class);
      // create EJB instance
      TestUtil.logMsg("Create Local EJB instance");
      beanLocalRef = (TestBeanLocal) beanLocalHome.create();
      TestUtil.logMsg("Performing self-referential loopback call test");
      pass = beanLocalRef.loopBackSameBeanLocal();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg("Exception: " + e);
      pass = false;
    } finally {
      try {
        beanLocalRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return pass;
  }

  public boolean loopBackAnotherBeanLocal() {
    TestUtil.logTrace("loopBackAnotherBeanLocal");

    boolean pass = true;

    try {
      TestUtil.logMsg("Looking up the TestBean Local Home " + testLookup);
      beanLocalHome = (TestBeanLocalHome) lookup(testLookup,
          TestBeanLocalHome.class);
      // create EJB instance
      TestUtil.logMsg("Create Local EJB instance");
      beanLocalRef = (TestBeanLocal) beanLocalHome.create();
      TestUtil.logMsg("Performing loopback call test");
      pass = beanLocalRef.loopBackAnotherBeanLocal();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg("Exception: " + e);
      pass = false;
    } finally {
      try {
        beanLocalRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return pass;
  }

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
