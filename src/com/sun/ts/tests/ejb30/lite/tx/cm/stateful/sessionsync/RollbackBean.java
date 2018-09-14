/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.lite.tx.cm.stateful.sessionsync;

import javax.annotation.Resource;
import javax.ejb.AfterBegin;
import javax.ejb.AfterCompletion;
import javax.ejb.BeforeCompletion;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.ExcludeDefaultInterceptors;

import com.sun.ts.tests.ejb30.common.statussingleton.StatusSingletonBean;

/**
 * The purpose of this bean is to verify various ways to roll back the
 * transaction inside afterBegin and beforeCompletionAnnotated methods.
 * SessionSynchronization methods are annotated.
 */
@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@ExcludeDefaultInterceptors
public class RollbackBean {

  enum TestNames {
    currentTestKey, isTxCommittedKey, methodSetRollbackOnly, methodException, afterBeginSetRollbackOnly, afterBeginException, beforeCompletionSetRollbackOnly, beforeCompletionException, afterCompletionException
  }

  @EJB
  private StatusSingletonBean statusSingleton;

  @Resource
  private SessionContext sctx;

  private String currentTest;

  private Boolean transactionStatus;

  public boolean getAndResetTransactionStatus() {
    boolean result = transactionStatus;
    transactionStatus = null;
    return result;
  }

  public void rollback() {
    if (currentTest == null) {
      return;
    }
    if (currentTest.equals(TestNames.methodException.toString())) {
      throw new RuntimeException("from businessMethod.");
    }
    if (currentTest.equals(TestNames.methodSetRollbackOnly.toString())) {
      sctx.setRollbackOnly();
    }
  }

  @SuppressWarnings("unused")
  @AfterBegin()
  private void afterBegin() {
    currentTest = statusSingleton
        .getAndResetResult(TestNames.currentTestKey.toString());
    if (currentTest == null) {
      return;
    }
    if (currentTest.equals(TestNames.afterBeginException.toString())) {
      throw new RuntimeException("from afterBegin callback.");
    }
    if (currentTest.equals(TestNames.afterBeginSetRollbackOnly.toString())) {
      sctx.setRollbackOnly();
    }
  }

  @SuppressWarnings("unused")
  @BeforeCompletion
  private void beforeCompletion() {
    if (currentTest == null) {
      return;
    }
    if (currentTest.equals(TestNames.beforeCompletionException.toString())) {
      throw new RuntimeException("from beforeCompletion callback.");
    }
    if (currentTest
        .equals(TestNames.beforeCompletionSetRollbackOnly.toString())) {
      sctx.setRollbackOnly();
    }
  }

  @SuppressWarnings("unused")
  @AfterCompletion
  private void afterCompletion(boolean b) {
    if (currentTest == null) {
      return;
    }
    transactionStatus = b;
    String currentTestSave = currentTest;
    currentTest = null;

    if (currentTestSave.equals(TestNames.afterCompletionException.toString())) {
      throw new RuntimeException("from afterCompletion callback.");
    }
  }

}
