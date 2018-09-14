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
 * @(#)TestBean1EJB.java	1.2 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.session.stateful.bean2beansinglejartest.bean1;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.ejb.*;
import java.rmi.*;
import com.sun.ts.tests.ejb.ee.bb.session.stateful.bean2beansinglejartest.bean2.*;

public class TestBean1EJB implements SessionBean {
  private static final String PINGMSG = "ping from bean1";

  private SessionContext sctx = null;

  private Properties harnessProps = null;

  private static final String testBean2 = "java:comp/env/ejb/TestBean2";

  public void ejbCreate(Properties p) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    harnessProps = p;
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
  // TestBean1 interface (our business methods)

  public void ping() {
    TestUtil.logTrace("ping");
  }

  public boolean callOtherBeanTest() {
    TestUtil.logTrace("callOtherBeanTest");
    boolean pass = true;
    TestBean2Home beanHome = null;
    TestBean2 beanRef = null;
    try {
      // lookup EJB home
      TestUtil.logMsg("lookup home interface for EJB: " + testBean2);
      beanHome = (TestBean2Home) lookup(testBean2, TestBean2Home.class);
      // create EJB instance
      TestUtil.logMsg("Create EJB instance");
      beanRef = (TestBean2) beanHome.create(harnessProps);
      TestUtil.logMsg("bean1 calling bean2");
      String s = beanRef.ping(PINGMSG);
      TestUtil.logMsg("Received: " + s);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return pass;
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
