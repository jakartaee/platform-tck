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

package com.sun.ts.tests.ejb30.common.allowed.stateful;

import com.sun.ts.tests.ejb30.common.allowed.AllowedBeanBase;
import com.sun.ts.tests.ejb30.common.allowed.AllowedIF;
import com.sun.ts.tests.ejb30.common.allowed.AllowedLocalIF;
import com.sun.ts.tests.ejb30.common.allowed.Constants;
import java.util.Properties;
import javax.ejb.SessionContext;

public class StatefulAllowedBeanBase extends AllowedBeanBase
    implements AllowedIF, AllowedLocalIF, java.io.Serializable, Constants {
  protected String testMethod;

  protected Properties afterBeginResults;

  protected Properties beforeCompletionResults;

  protected Properties afterCompletionResults;

  protected void cancelTimersPostInvoke(SessionContext sctx) {
    StatefulCancelInterceptor.getInstance().cancelTimers(sctx);
  }

  public String getTestMethod() {
    return testMethod;
  }

  @Override
  public Properties runOperations(SessionContext sctx) {
    return StatefulOperations.getInstance().run(sctx);
  }

  /////////////////////////////////////////////////////////////////////////
  // SessionSynchronization methods
  /////////////////////////////////////////////////////////////////////////
  public void afterCompletion(boolean param) {
    if (afterCompletionTest.equals(getTestMethod())) {
      afterCompletionResults = null;
      afterCompletionResults = runOperations(sessionContext);
      testMethod = null;
    }
  }

  public void beforeCompletion() {
    if (beforeCompletionTest.equals(getTestMethod())) {
      beforeCompletionResults = null;
      beforeCompletionResults = runOperations(sessionContext);
      testMethod = null;
    }
  }

  public void afterBegin() {
    if (afterBeginTest.equals(getTestMethod())) {
      afterBeginResults = null;
      afterBeginResults = runOperations(sessionContext);
      testMethod = null;
    }
  }

  /////////////////////////////////////////////////////////////////////////
  // business methods
  /////////////////////////////////////////////////////////////////////////
  public void setTestMethod(String testMethod) {
    this.testMethod = testMethod;
  }

  public java.util.Properties getResultsAfterCompletion() {
    return afterCompletionResults;
  }

  public java.util.Properties beforeCompletionTest() {
    return beforeCompletionResults;
  }

  public void afterCompletionTest() {
    // do nothing
  }

  public java.util.Properties afterBeginTest() {
    return afterBeginResults;
  }
}
