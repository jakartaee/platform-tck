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
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.ejb.SessionContext;

public abstract class AllowedBeanBase implements AllowedIF, AllowedLocalIF {

  protected SessionContext sessionContext;

  protected Properties preInvokeResults;

  protected Properties postInvokeResults = new Properties();

  abstract public Properties runOperations(SessionContext sctx);

  public void timeout(javax.ejb.Timer timer) {
  }

  public void setSessionContext(SessionContext sc) {
    this.sessionContext = sc;
  }

  // @todo this @Around is redundant
  @AroundInvoke
  public Object intercept(InvocationContext inv) throws Exception {
    String methodName = inv.getMethod().getName();
    TLogger.log("calling interceptor method prior to " + methodName);
    boolean isPreInvokeTest = false;
    boolean isPostInvokeTest = false;
    if (methodName.equalsIgnoreCase("preInvokeTest")) {
      isPreInvokeTest = true;
    } else if (methodName.equalsIgnoreCase("getResultsPostInvoke")) {
      isPostInvokeTest = true;
    }
    try {
      if (isPreInvokeTest) {
        this.preInvokeResults = null;
        this.preInvokeResults = runOperations(this.sessionContext);
      }
      Object result = inv.proceed();
      return result;
    } catch (TestFailedException e) {
      throw e;
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new IllegalStateException(e);
    } finally {
      if (isPostInvokeTest) {
        Properties prps = runOperations(this.sessionContext);
        this.postInvokeResults.clear();
        this.postInvokeResults.putAll(prps);
        cancelTimersPostInvoke(sessionContext);
      }
    }
  }

  protected void cancelTimersPostInvoke(SessionContext sctx) {
    CancelInterceptor.getInstance().cancelTimers(sctx);
  }

  // ===================== business methods ===========================
  public void remove() {
  }

  public void txNotSupported() throws TestFailedException {
  }

  public void txSupports() throws TestFailedException {
  }

  public void txNever() throws TestFailedException {
  }

  public void utBeginTest() throws TestFailedException {
  }

  public Properties business() {
    return runOperations(this.sessionContext);
  }

  public Properties preInvokeTest() {
    return preInvokeResults;
  }

  public void postInvokeTest() {
    // return postInvokeResults;
  }

  public Properties getResultsPostInvoke() {
    return this.postInvokeResults;
  }

  public void setTestMethod(String testMethod) {
  }

  public Properties getResultsAfterCompletion() {
    return null;
  }

  public Properties beforeCompletionTest() {
    return null;
  }

  public void afterCompletionTest() {
  }

  public Properties afterBeginTest() {
    return null;
  }

}
