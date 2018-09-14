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
 * @(#)TestBeanEJB.java	1.21 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.session.stateless.sessionbeantest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.ejb.*;
import java.rmi.*;

public class TestBeanEJB implements SessionBean {
  private SessionContext sctx = null;

  // Proper lifecycle create call order for EJBObject creation.
  // A client invokes home.create()
  // - newInstance()
  // - setSessionContext(SessionContext ctx)
  // - ejbCreate()

  private boolean ejbNewInstanceFlag = false;

  private boolean ejbSessionContextFlag = false;

  private boolean ejbCreateFlag = false;

  private boolean createLifeCycleFlag = true;

  // newInstance() invokes default no-arg constructor for class
  public TestBeanEJB() {
    TestUtil.logTrace("newInstance => default constructor called");
    ejbNewInstanceFlag = true;
    if (ejbSessionContextFlag || ejbCreateFlag)
      createLifeCycleFlag = false;
    if (ejbSessionContextFlag)
      TestUtil.logErr("newInstance() not called before setSessionContext()");
    if (ejbCreateFlag)
      TestUtil.logErr("newInstance() not called before ejbCreate()");
  }

  public void ejbCreate() throws CreateException {
    TestUtil.logTrace("ejbCreate");
    ejbCreateFlag = true;
    if (!ejbNewInstanceFlag || !ejbSessionContextFlag)
      createLifeCycleFlag = false;
    if (!ejbNewInstanceFlag)
      TestUtil.logErr("newInstance() not called before ejbCreate()");
    if (!ejbSessionContextFlag)
      TestUtil.logErr("setSessionContext() not called before ejbCreate()");
  }

  public void setSessionContext(SessionContext sc) {
    TestUtil.logTrace("setSessionContext");
    this.sctx = sc;
    ejbSessionContextFlag = true;
    if (!ejbNewInstanceFlag || ejbCreateFlag)
      createLifeCycleFlag = false;
    if (!ejbNewInstanceFlag)
      TestUtil.logErr("newInstance() not called before setSessionContext()");
    if (ejbCreateFlag)
      TestUtil.logErr("ejbCreate() called before setSessionContext()");
  }

  public void ejbRemove() {
    TestUtil.logTrace("ejbRemove");
    reset();
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

  public boolean isCreateLifeCycle() {
    TestUtil.logTrace("isCreateLifeCycle");
    boolean status = createLifeCycleFlag;
    reset();
    return status;
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

  private void reset() {
    ejbSessionContextFlag = false;
    ejbCreateFlag = false;
    createLifeCycleFlag = true;
  }

  // ===========================================================
}
