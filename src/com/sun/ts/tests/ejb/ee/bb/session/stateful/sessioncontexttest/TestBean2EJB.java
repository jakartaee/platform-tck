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
 * @(#)TestBean2EJB.java	1.15 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.session.stateful.sessioncontexttest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.ejb.*;
import java.rmi.*;
import javax.transaction.*;

public class TestBean2EJB implements SessionBean {
  private SessionContext sctx = null;

  private Properties harnessProps = null;

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
  // TestBean2EJB interface (our business methods)

  public boolean getUserTransactionTest() {
    TestUtil.logTrace("getUserTransactionTest");

    boolean pass = true;

    TestUtil.logMsg("invoke SessionContext.getUserTransaction() method");
    try {
      UserTransaction ut = sctx.getUserTransaction();
      TestUtil.logErr("IllegalStateException not received - unexpected");
      pass = false;
    } catch (IllegalStateException e) {
      TestUtil.logMsg("IllegalStateException received - expected");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  public boolean setRollbackOnlyTest() {
    TestUtil.logTrace("setRollbackOnlyTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("get rollback status");
      if (!sctx.getRollbackOnly()) {
        TestUtil.logMsg("transaction not marked for rollback - expected");
      } else {
        TestUtil.logMsg("transaction marked for rollback - unexpected");
        return false;
      }
      TestUtil.logMsg("mark transaction for rollback");
      sctx.setRollbackOnly();
      TestUtil.logMsg("get rollback status");
      if (sctx.getRollbackOnly()) {
        TestUtil.logMsg("transaction marked for rollback - expected");
      } else {
        TestUtil.logMsg("transaction not marked for rollback - unexpected");
        pass = false;
      }
      return pass;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      return false;
    }
  }

  public boolean getRollbackOnlyTest() {
    TestUtil.logTrace("getRollbackOnlyTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("get rollback status");
      if (!sctx.getRollbackOnly()) {
        TestUtil.logMsg("transaction not marked for rollback - expected");
      } else {
        TestUtil.logMsg("transaction marked for rollback - unexpected");
        return false;
      }
      TestUtil.logMsg("mark transaction for rollback");
      sctx.setRollbackOnly();
      TestUtil.logMsg("get rollback status");
      if (sctx.getRollbackOnly()) {
        TestUtil.logMsg("transaction marked for rollback - expected");
      } else {
        TestUtil.logMsg("transaction not marked for rollback - unexpected");
        pass = false;
      }
      return pass;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      return false;
    }
  }

  // ===========================================================
}
