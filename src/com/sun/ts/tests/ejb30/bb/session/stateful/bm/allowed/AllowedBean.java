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

package com.sun.ts.tests.ejb30.bb.session.stateful.bm.allowed;

import com.sun.ts.tests.ejb30.common.allowed.AllowedIF;
import com.sun.ts.tests.ejb30.common.allowed.AllowedLocalIF;
import com.sun.ts.tests.ejb30.common.allowed.stateful.TimerLocalIF;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import java.util.Properties;
import javax.interceptor.AroundInvoke;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.interceptor.Interceptors;
import javax.interceptor.InvocationContext;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Remove;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.Status;

@Stateful(name = "AllowedBean")
@Remote({ AllowedIF.class })
@Local({ AllowedLocalIF.class })
@TransactionManagement(TransactionManagementType.BEAN)
@Interceptors({
    com.sun.ts.tests.ejb30.common.allowed.stateful.StatefulCancelInterceptor.class })

@EJB(name = "ejb/TimerEJB", beanName = "TimerEJB", beanInterface = TimerLocalIF.class)
public class AllowedBean extends AllowedBeanNonSessionSynchronizationBase
    implements AllowedIF, AllowedLocalIF, java.io.Serializable {

  @Resource(name = "ejbContext")
  @Override
  public void setSessionContext(SessionContext sc) {
    super.setSessionContext(sc);
  }

  @AroundInvoke
  public Object intercept(InvocationContext inv) throws Exception {
    return super.intercept(inv);
  }

  @Override
  public Properties runOperations(SessionContext sctx) {
    return StatefulBMTOperations.getInstance().run2(sctx, AllowedIF.class);
  }

  // ===================== business methods ===========================
  @Remove
  public void remove() {
    super.remove();
  }

  public void utBeginTest() throws TestFailedException {
    boolean pass = false;
    String reason = null;
    try {
      sessionContext.getUserTransaction().begin();
      TLogger.logMsg("UserTransaction.begin - allowed");
      TLogger.logTrace(
          "Attempt to call begin again before commit() or rollback()");
      sessionContext.getUserTransaction().begin();
      reason = "Was able to call begin again before commit() or rollback()";
    } catch (javax.transaction.NotSupportedException nse) {
      pass = true;
      reason = "javax.transaction.NotSupportedException caught as expected";
      TLogger.log(reason);
    } catch (Exception e) {
      reason = "Unexpected Exception - " + e;
    } finally {
      try {
        if (sessionContext.getUserTransaction()
            .getStatus() != Status.STATUS_NO_TRANSACTION) {
          TLogger.logTrace("Rollback the active TX from the first begin call");
          sessionContext.getUserTransaction().rollback();
        }
      } catch (Exception re) {
        TLogger.logMsg("Exception caught on ut.rollback() - " + re);
      }
    }

    if (!pass) {
      throw new TestFailedException(reason);
    }
  }

}
