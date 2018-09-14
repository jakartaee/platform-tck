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

package com.sun.ts.tests.ejb30.bb.session.stateful.cm.allowed;

import java.util.Properties;
import com.sun.ts.tests.ejb30.common.allowed.Constants;
import com.sun.ts.tests.ejb30.common.allowed.MySessionSynchronization;
import javax.annotation.Resource;
import javax.ejb.Remote;
import javax.ejb.Remove;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;

@Stateful(name = "SetRollbackOnlyBean")
@Remote({ SetRollbackOnlyIF.class })
public class SetRollbackOnlyBean implements SetRollbackOnlyIF,
    java.io.Serializable, MySessionSynchronization, Constants {

  @Resource(name = "ejbContext")
  protected SessionContext sctx;

  private String testMethod;

  protected Properties results = new Properties();

  public String getTestMethod() {
    return testMethod;
  }

  protected void runSetRollbackOnly(String methodName) {
    results.remove(methodName);
    try {
      sctx.setRollbackOnly();
      results.setProperty(methodName, allowed);
      System.out.println("## setRollbackOnly() ok");
    } catch (IllegalStateException e) {
      results.setProperty(methodName, disallowed);
    } catch (Exception e) {
      results.setProperty(methodName, e.toString());
    }
  }

  /////////////////////////////////////////////////////////////////////////
  // SessionSynchronization methods
  /////////////////////////////////////////////////////////////////////////
  public void afterCompletion(boolean param) {
    if (afterCompletionSetRollbackOnlyTest.equals(getTestMethod())) {
      runSetRollbackOnly(getTestMethod());
      testMethod = null;
    }
  }

  public void beforeCompletion() {
    if (beforeCompletionSetRollbackOnlyTest.equals(getTestMethod())) {
      runSetRollbackOnly(getTestMethod());
      testMethod = null;
    }
  }

  public void afterBegin() {
    if (afterBeginSetRollbackOnlyTest.equals(getTestMethod())) {
      runSetRollbackOnly(getTestMethod());
      testMethod = null;
    }
  }

  // ===================== business methods ===========================
  @Remove
  public void remove() {
  }

  public String getResultFor(String testMethod) {
    return results.getProperty(testMethod);
  }

  public void setTestMethod(String testMethod) {
    this.testMethod = testMethod;
  }

  public void businessSetRollbackOnlyTest() {
    runSetRollbackOnly(businessSetRollbackOnlyTest);
  }

  public void beforeCompletionSetRollbackOnlyTest() {
  }

  public void afterCompletionSetRollbackOnlyTest() {
  }

  public void afterBeginSetRollbackOnlyTest() {
  }

}
