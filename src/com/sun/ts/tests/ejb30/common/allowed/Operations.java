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
 * $Id$
 */

package com.sun.ts.tests.ejb30.common.allowed;

import com.sun.ts.tests.ejb30.common.helper.TLogger;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import java.util.Properties;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

public class Operations implements Constants {
  private static Operations instance = new Operations();

  protected Operations() {
    super();
  }

  public static Operations getInstance() {
    return instance;
  }

  public void tryRollback(SessionContext sctx) throws TestFailedException {
    try {
      sctx.getRollbackOnly();
      throw new TestFailedException(
          "Expecting IllegalStateException from getRollbackOnly(),"
              + " but got no exception.");
    } catch (IllegalStateException e) {
      // good
    } catch (Exception e) {
      throw new TestFailedException(
          "Expecting IllegalStateException from getRollbackOnly(), "
              + " but got " + e);
    }
  }

  public void runMessageContext(SessionContext sctx, Properties results) {
    // getMessageContext test
    try {
      sctx.getMessageContext();
      results.setProperty(getMessageContext, allowed);
    } catch (IllegalStateException e) {
      results.setProperty(getMessageContext, disallowed);
    } catch (Exception e) {
      results.setProperty(getMessageContext, e.toString());
    }
  }

  public void runRollbackOnly(SessionContext sctx, Properties results) {
    getSetRollbackOnly(sctx, results);
  }

  public void getSetRollbackOnly(SessionContext sctx, Properties results) {
    // getRollbackOnly test
    try {
      sctx.getRollbackOnly();
      results.setProperty(getRollbackOnly, allowed);
    } catch (IllegalStateException e) {
      results.setProperty(getRollbackOnly, disallowed);
    } catch (Exception e) {
      results.setProperty(getRollbackOnly, e.toString());
    }

    // setRollbackOnly test
    try {
      sctx.setRollbackOnly();
      results.setProperty(setRollbackOnly, allowed);
    } catch (IllegalStateException e) {
      results.setProperty(setRollbackOnly, disallowed);
    } catch (Exception e) {
      results.setProperty(setRollbackOnly, e.toString());
    }
  }

  public void runUserTransaction(SessionContext sctx, Properties results) {
    // UserTransaction Access test
    try {
      UserTransaction ut = sctx.getUserTransaction();
      results.setProperty(UserTransaction, allowed);
    } catch (IllegalStateException e) {
      results.setProperty(UserTransaction, disallowed);
    } catch (Exception e) {
      results.setProperty(UserTransaction, e.toString());
    }

    // UserTransaction Methods Test1
    try {
      sctx.getUserTransaction().begin();
      results.setProperty(UserTransaction_Methods_Test1, allowed);
    } catch (IllegalStateException e) {
      results.setProperty(UserTransaction_Methods_Test1, disallowed);
    } catch (Exception e) {
      results.setProperty(UserTransaction_Methods_Test1, e.toString());
    }

    // UserTransaction Methods Test2
    try {
      sctx.getUserTransaction().commit();
      results.setProperty(UserTransaction_Methods_Test2, allowed);
    } catch (IllegalStateException e) {
      results.setProperty(UserTransaction_Methods_Test2, disallowed);
    } catch (Exception e) {
      results.setProperty(UserTransaction_Methods_Test2, e.toString());
    }

    // UserTransaction Methods Test3
    try {
      sctx.getUserTransaction().getStatus();
      results.setProperty(UserTransaction_Methods_Test3, allowed);
    } catch (IllegalStateException e) {
      results.setProperty(UserTransaction_Methods_Test3, disallowed);
    } catch (Exception e) {
      results.setProperty(UserTransaction_Methods_Test3, e.toString());
    }

    // UserTransaction Methods Test4
    // if commit (Test2) is allowed, this test should get IllegalStateException
    // and the outcome should be disallowed.
    try {
      sctx.getUserTransaction().rollback();
      results.setProperty(UserTransaction_Methods_Test4, allowed);
    } catch (IllegalStateException e) {
      results.setProperty(UserTransaction_Methods_Test4, disallowed);
    } catch (Exception e) {
      results.setProperty(UserTransaction_Methods_Test4, e.toString());
    }

    // UserTransaction Methods Test5
    // if either commit (Test2) or rollback (Test4) is allowed, this test
    // should get IllegalStateException, and the outcome should be
    // disallowed.
    try {
      sctx.getUserTransaction().setRollbackOnly();
      results.setProperty(UserTransaction_Methods_Test5, allowed);
    } catch (IllegalStateException e) {
      results.setProperty(UserTransaction_Methods_Test5, disallowed);
    } catch (Exception e) {
      results.setProperty(UserTransaction_Methods_Test5, e.toString());
    }

    // UserTransaction Methods Test6
    try {
      sctx.getUserTransaction().setTransactionTimeout(0);
      results.setProperty(UserTransaction_Methods_Test6, allowed);
    } catch (IllegalStateException e) {
      results.setProperty(UserTransaction_Methods_Test6, disallowed);
    } catch (Exception e) {
      results.setProperty(UserTransaction_Methods_Test6, e.toString());
    }
  }

  public void runTimers(SessionContext sctx, Properties results) {
    // getTimerService test
    try {
      sctx.getTimerService();
      results.setProperty(getTimerService, allowed);
    } catch (IllegalStateException e) {
      results.setProperty(getTimerService, disallowed);
    } catch (Exception e) {
      results.setProperty(getTimerService, e.toString());
    }

    // @todo TimerService_Methods, etc
    // TimerService_Methods_Test1
    try {
      javax.ejb.TimerService timesrv = sctx.getTimerService();
      javax.ejb.Timer tt = timesrv.createTimer((long) 10000, "test1");
      tt.cancel();
      results.setProperty(TimerService_Methods_Test1, allowed);
    } catch (IllegalStateException e) {
      results.setProperty(TimerService_Methods_Test1, disallowed);
    } catch (Exception e) {
      results.setProperty(TimerService_Methods_Test1, e.toString());
    }

    // TimerService_Methods_Test2
    try {
      javax.ejb.TimerService timesrv2 = sctx.getTimerService();
      javax.ejb.Timer t2 = timesrv2.createTimer((long) 10000, (long) 10000,
          "test2");
      t2.cancel();
      results.setProperty(TimerService_Methods_Test2, allowed);
    } catch (IllegalStateException e) {
      results.setProperty(TimerService_Methods_Test2, disallowed);
    } catch (Exception e) {
      results.setProperty(TimerService_Methods_Test2, e.toString());
    }

    // TimerService_Methods_Test3
    try {
      long expiration = (System.currentTimeMillis() + (long) 900000);
      java.util.Date d = new java.util.Date(expiration);
      javax.ejb.TimerService timesrv3 = sctx.getTimerService();
      javax.ejb.Timer t3 = timesrv3.createTimer(d, "test3");
      t3.cancel();
      results.setProperty(TimerService_Methods_Test3, allowed);
    } catch (IllegalStateException e) {
      results.setProperty(TimerService_Methods_Test3, disallowed);
    } catch (Exception e) {
      results.setProperty(TimerService_Methods_Test3, e.toString());
    }

    // TimerService_Methods_Test4
    try {
      long expiration = (System.currentTimeMillis() + (long) 900000);
      java.util.Date d = new java.util.Date(expiration);
      javax.ejb.TimerService timesrv4 = sctx.getTimerService();
      javax.ejb.Timer t4 = timesrv4.createTimer(d, (long) 10000, "test4");
      t4.cancel();
      results.setProperty(TimerService_Methods_Test4, allowed);
    } catch (IllegalStateException e) {
      results.setProperty(TimerService_Methods_Test4, disallowed);
    } catch (Exception e) {
      results.setProperty(TimerService_Methods_Test4, e.toString());
    }

    // TimerService_Methods_Test5
    try {
      javax.ejb.TimerService ts = sctx.getTimerService();
      java.util.Collection ccol = ts.getTimers();
      results.setProperty(TimerService_Methods_Test5, allowed);
    } catch (IllegalStateException e) {
      results.setProperty(TimerService_Methods_Test5, disallowed);
    } catch (Exception e) {
      results.setProperty(TimerService_Methods_Test5, e.toString());
    }

    // TimerService_Methods_Test6
    try {
      javax.ejb.TimerService timesrv6 = sctx.getTimerService();
      javax.ejb.Timer t6 = timesrv6.createTimer((long) 10000, "test6");
      t6.getHandle();
      t6.cancel();// added 4/15/2005
      results.setProperty(TimerService_Methods_Test6, allowed);
    } catch (IllegalStateException e) {
      results.setProperty(TimerService_Methods_Test6, disallowed);
    } catch (Exception e) {
      results.setProperty(TimerService_Methods_Test6, e.toString());
    }

    // TimerService_Methods_Test7
    try {
      javax.ejb.TimerService timesrv7 = sctx.getTimerService();
      javax.ejb.Timer t7 = timesrv7.createTimer((long) 10000, "test7");
      t7.cancel();
      results.setProperty(TimerService_Methods_Test7, allowed);
    } catch (IllegalStateException e) {
      results.setProperty(TimerService_Methods_Test7, disallowed);
    } catch (Exception e) {
      results.setProperty(TimerService_Methods_Test7, e.toString());
    }
  }

  public void runGetEJBHome(SessionContext sctx, Properties results) {
    try {
      sctx.getEJBHome();
      results.setProperty(getEJBHome, allowed);
    } catch (IllegalStateException e) {
      results.setProperty(getEJBHome, disallowed);
    } catch (Exception e) {
      e.printStackTrace();
      results.setProperty(getEJBHome, e.toString());
    }
  }

  public void runGetCallerPrincipal(SessionContext sctx, Properties results) {
    try {
      sctx.getCallerPrincipal();
      results.setProperty(getCallerPrincipal, allowed);
    } catch (IllegalStateException e) {
      results.setProperty(getCallerPrincipal, disallowed);
    } catch (Exception e) {
      results.setProperty(getCallerPrincipal, e.toString());
    }
  }

  public void runIsCallerInRole(SessionContext sctx, Properties results) {
    try {
      sctx.isCallerInRole(Administrator);
      results.setProperty(isCallerInRole, allowed);
    } catch (IllegalStateException e) {
      results.setProperty(isCallerInRole, disallowed);
    } catch (Exception e) {
      results.setProperty(isCallerInRole, e.toString());
    }
  }

  public void runGetEJBObject(SessionContext sctx, Properties results) {
    try {
      sctx.getEJBObject();
      results.setProperty(getEJBObject, allowed);
    } catch (IllegalStateException e) {
      results.setProperty(getEJBObject, disallowed);
    } catch (Exception e) {
      results.setProperty(getEJBObject, e.toString());
    }
  }

  public void runJndiAccess(SessionContext sctx, Properties results) {
    try {
      InitialContext nctx = new InitialContext();
      boolean myBoolean = (Boolean) nctx
          .lookup("java:comp/env/" + ENV_ENTRY_NAME);
      if (myBoolean) {
        results.setProperty(JNDI_Access, allowed);
      } else {
        results.setProperty(JNDI_Access,
            "jndi lookup is ok but returns" + " unexpected value " + myBoolean);
      }
    } catch (IllegalStateException e) {
      results.setProperty(JNDI_Access, disallowed);
    } catch (Exception e) {
      results.setProperty(JNDI_Access, e.toString());
    }
  }

  public void runEJBContextLookup(SessionContext sctx, Properties results) {
    try {
      boolean myBoolean = (Boolean) sctx.lookup(ENV_ENTRY_NAME);
      if (myBoolean) {
        results.setProperty(EJBContext_lookup, allowed);
      } else {
        results.setProperty(EJBContext_lookup,
            "lookup is ok but returns unexpected value " + myBoolean);
      }
    } catch (IllegalStateException e) {
      results.setProperty(EJBContext_lookup, disallowed);
    } catch (Exception e) {
      results.setProperty(EJBContext_lookup, e.toString());
    }
  }

  public void runGetEJBLocalHome(SessionContext sctx, Properties results) {
    try {
      sctx.getEJBLocalHome();
      results.setProperty(getEJBLocalHome, allowed);
    } catch (IllegalStateException e) {
      results.setProperty(getEJBLocalHome, disallowed);
    } catch (Exception e) {
      results.setProperty(getEJBLocalHome, e.toString());
    }
  }

  public void runGetEJBLocalObject(SessionContext sctx, Properties results) {
    try {
      sctx.getEJBLocalObject();
      results.setProperty(getEJBLocalObject, allowed);
    } catch (IllegalStateException e) {
      results.setProperty(getEJBLocalObject, disallowed);
    } catch (Exception e) {
      results.setProperty(getEJBLocalObject, e.toString());
    }
  }

  // not part of the run(sctx) method
  public void runGetBusinessObject(SessionContext sctx, Properties results,
      Class intf) {
    try {
      sctx.getBusinessObject(intf);
      results.setProperty(getBusinessObject, allowed);
    } catch (IllegalStateException e) {
      results.setProperty(getBusinessObject, disallowed);
    } catch (Exception e) {
      results.setProperty(getBusinessObject, e.toString());
    }
  }

  public Properties run2(SessionContext sctx, Class intf) {
    Properties results = run(sctx);
    runGetBusinessObject(sctx, results, intf);
    return results;
  }

  public Properties run(SessionContext sctx) {
    TLogger.log("about to run operations...");
    Properties results = new Properties();
    // getEJBHome test
    runGetEJBHome(sctx, results);

    // getCallerPrincipal test
    runGetCallerPrincipal(sctx, results);

    // isCallerInRole test
    runIsCallerInRole(sctx, results);

    // getEJBObject test
    // @todo getEJBObject, etc
    runGetEJBObject(sctx, results);

    // JNDI Access test
    runJndiAccess(sctx, results);

    // EJBContext lookup test
    runEJBContextLookup(sctx, results);

    runUserTransaction(sctx, results);

    // getEJBLocalHome test
    // @todo getEJBLocalHome, etc
    runGetEJBLocalHome(sctx, results);

    // getEJBLocalObject test
    runGetEJBLocalObject(sctx, results);

    runTimers(sctx, results);

    runMessageContext(sctx, results);

    runRollbackOnly(sctx, results);

    return results;
  }

}
