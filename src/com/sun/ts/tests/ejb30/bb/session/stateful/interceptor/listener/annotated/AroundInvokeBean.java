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

package com.sun.ts.tests.ejb30.bb.session.stateful.interceptor.listener.annotated;

import com.sun.ts.tests.ejb30.common.calc.CalculatorException;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import com.sun.ts.tests.ejb30.common.interceptor.AroundInvokeBase;
import com.sun.ts.tests.ejb30.common.interceptor.AroundInvokeIF;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.annotation.Resource;
import javax.ejb.Remove;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.ejb.SessionContext;
import javax.interceptor.Interceptors;
import com.sun.ts.tests.ejb30.common.interceptor.AroundInvokeTestImpl;
import com.sun.ts.tests.ejb30.common.interceptor.Constants;
import java.io.Serializable;
import java.lang.reflect.Method;
import javax.ejb.SessionSynchronization;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

@Stateful(name = "AroundInvokeBean")
@Remote({ AroundInvokeIF.class })
// This bean must use cmt, since it uses setRollbackOnly
@TransactionManagement(TransactionManagementType.CONTAINER)
@Interceptors({ com.sun.ts.tests.ejb30.common.interceptor.Interceptor1.class,
    com.sun.ts.tests.ejb30.common.interceptor.Interceptor2.class })
// @todo redundant implements

public class AroundInvokeBean extends AroundInvokeBase
    implements AroundInvokeIF, SessionSynchronization, Constants, Serializable {
  @Resource(name = "ejbContext")
  private SessionContext ejbContext;

  private boolean afterCompletionCalled;

  private boolean beforeCompletionCalled;

  private boolean afterBeginCalled;

  public AroundInvokeBean() {
    super();
  }

  // ============ abstract methods from super ==========================
  protected javax.ejb.EJBContext getEJBContext() {
    return this.ejbContext;
  }

  // ============= interceptor method ==================================
  /**
   * There is some duplication between this method in
   * stateful/interceptor/listener and stateful/interceptor/method. It's too
   * much work to abstract them out, because: (1) this method need to access
   * bean's internal state. If we delegate to a helper class, then we need to
   * pass this bean to it, and we need a common type for this bean. (2) we do
   * not want to populate common/interceptor with stateful-related stuff.
   * 
   */
  @AroundInvoke
  public Object intercept(InvocationContext ctx) throws Exception {
    // this interceptor should be invoked last, unless overrid by deployment
    // descriptor.
    Object result = null;
    int orderInChain = 3;
    Method meth = ctx.getMethod();
    String methName = meth.getName();

    if (afterBeginTest.equals(methName)) {
      // afterBegin() should already have been called. Ideally, this
      // should be checked in the first interceptor (this one is the last),
      // but we need access to the bean's internal state.
      if (!isAfterBeginCalled()) {
        throw new CalculatorException(
            "afterBegin() was not called before this interceptor.");
      } else { // good, go to proceed(), which is in intercept2
        TLogger.log("good, isAfterBeginCalled: " + isAfterBeginCalled()
            + ", proceed...");
        result = AroundInvokeTestImpl.intercept2(ctx, orderInChain);
        return result;
      }
    } else if (beforeCompletionTest.equals(methName)) {
      // nothing at this point, more later in try/catch/finally
    } else {
      result = AroundInvokeTestImpl.intercept2(ctx, orderInChain);
      return result;
    }

    // we know here we are dealing w/ beforeCompletionTest
    if (!beforeCompletionTest.equals(methName)) {
      throw new IllegalStateException(
          "Only beforeCompletionTest can get here, but current method name is "
              + methName);
    }
    try {
      result = ctx.proceed();
    } catch (CalculatorException e) {
      throw e;
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new IllegalStateException(e);
    } finally {
      // beforeCompletion must not be called yet
      if (isBeforeCompletionCalled()) {
        throw new CalculatorException(
            "beforeCompletion must not be called before any interceptor.");
      } else {
        TLogger.log("good, isBeforeCompletionCalled after proceed(): "
            + isBeforeCompletionCalled());
      }
    }
    return result;
  }

  // ============ SessionSynchronization related methods =================
  public void afterCompletion(boolean param) {
    setAfterCompletionCalled(true);
  }

  public void beforeCompletion() {
    setBeforeCompletionCalled(true);
  }

  public void afterBegin() {
    setAfterBeginCalled(true);
  }

  private boolean isAfterCompletionCalled() {
    return afterCompletionCalled;
  }

  private void setAfterCompletionCalled(boolean afterCompletionCalled) {
    this.afterCompletionCalled = afterCompletionCalled;
  }

  private boolean isBeforeCompletionCalled() {
    return beforeCompletionCalled;
  }

  private void setBeforeCompletionCalled(boolean beforeCompletionCalled) {
    this.beforeCompletionCalled = beforeCompletionCalled;
  }

  private boolean isAfterBeginCalled() {
    return afterBeginCalled;
  }

  private void setAfterBeginCalled(boolean afterBeginCalled) {
    this.afterBeginCalled = afterBeginCalled;
  }

  @Remove
  public void remove() {
  }

  // ============= override business methods from super ================
}
