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
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.EJBContext;
import javax.interceptor.InvocationContext;
import javax.ejb.SessionContext;
import javax.interceptor.AroundInvoke;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class CancelInterceptor {
  private static CancelInterceptor instance = new CancelInterceptor();

  public CancelInterceptor() {
    super();
  }

  public static CancelInterceptor getInstance() {
    return instance;
  }

  public void cancelTimers(SessionContext sctx) {
    try {
      if (sctx.getRollbackOnly()) {
        // current tx has been marked to rollback, so do nothing to timer
        return;
      }
      TimerService ts = sctx.getTimerService();
      Collection ccol = ts.getTimers();
      Iterator i = ccol.iterator();
      while (i.hasNext()) {
        Timer t = (javax.ejb.Timer) i.next();
        t.cancel();
      }
    } catch (Exception e) {
      // ignore
    }
  }

  @AroundInvoke
  public Object intercept(InvocationContext inv) throws Exception {
    String methodName = inv.getMethod().getName();
    TLogger.log("calling interceptor listener CancelInterceptor prior to "
        + methodName);
    try {
      Object result = inv.proceed();
      return result;
    } catch (TestFailedException e) {
      throw e;
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new IllegalStateException(e);
    } finally {
      EJBContext ejbContext = getEJBContext();
      SessionContext sctx = (SessionContext) ejbContext;

      boolean isPostInvokeTest = false;
      boolean isRemove = false;
      if (methodName.equalsIgnoreCase("getResultsPostInvoke")) {
        isPostInvokeTest = true;
      } else if (methodName.equals("remove")) {
        isRemove = true;
      }
      if (!isPostInvokeTest && !isRemove) {
        cancelTimers(sctx);
      }
    }
  }

  /**
   * Looks up the EJBContext that is the current EJB's namespace, typically
   * through injection into the bean class. A better way is to either directly
   * inject EJBContext into the current interceptor class, or look up by the
   * well-known name "java:comp/EJBContext" This method was written when the
   * above 2 mechanisms have not been defined.
   * 
   * @return EJBContext
   */
  protected static EJBContext getEJBContext() {
    EJBContext ec = null;
    try {
      InitialContext ic = new InitialContext();
      Object ob = ic.lookup("java:comp/env/ejbContext");
      ec = (EJBContext) ob;
    } catch (NamingException e) {
      throw new IllegalStateException(
          "Make sure SessionContext or MessageDrivenContext with name "
              + "ejbContext is injected into AroundInvoke bean.",
          e);
    }
    return ec;
  }
}
