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
 * @(#)TestBeanEJB.java	1.8 03/05/16
 */

package com.sun.ts.tests.interop.ejb.session.stateful.clientviewtest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import jakarta.ejb.*;
import java.rmi.*;

public class TestBeanEJB implements SessionBean {
  private SessionContext sctx = null;

  private Properties harnessProps = null;

  private int pingCount = 0;

  private boolean ejbCreateFlag = false;

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
    ejbCreateFlag = true;
  }

  public void setSessionContext(SessionContext sc) {
    TestUtil.logTrace("setSessionContext");
    this.sctx = sc;
  }

  public void ejbRemove() {
    TestUtil.logTrace("ejbRemove");
  }

  public void ejbDestroy() {
    TestUtil.logTrace("ejbDestroy");
    ejbCreateFlag = false;
  }

  public void ejbActivate() {
    TestUtil.logTrace("ejbActivate");
  }

  public void ejbPassivate() {
    TestUtil.logTrace("ejbPassivate");
  }

  // ===========================================================
  // TestBean interface (our business methods)

  public String Ping(String s) {
    TestUtil.logTrace("ping");
    return "ping: " + s;
  }

  public boolean isCreated() {
    return ejbCreateFlag;
  }

  public EJBObject Pass(EJBObject obj) {
    TestUtil.logTrace("The EJBObject was passed into a EJBObject.");
    return obj;
  }
  // ===========================================================
}
