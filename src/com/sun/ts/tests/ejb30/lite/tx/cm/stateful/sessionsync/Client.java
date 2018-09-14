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

import static com.sun.ts.tests.ejb30.lite.tx.cm.stateful.sessionsync.RollbackBean.TestNames.afterBeginException;
import static com.sun.ts.tests.ejb30.lite.tx.cm.stateful.sessionsync.RollbackBean.TestNames.afterBeginSetRollbackOnly;
import static com.sun.ts.tests.ejb30.lite.tx.cm.stateful.sessionsync.RollbackBean.TestNames.afterCompletionException;
import static com.sun.ts.tests.ejb30.lite.tx.cm.stateful.sessionsync.RollbackBean.TestNames.beforeCompletionException;
import static com.sun.ts.tests.ejb30.lite.tx.cm.stateful.sessionsync.RollbackBean.TestNames.beforeCompletionSetRollbackOnly;
import static com.sun.ts.tests.ejb30.lite.tx.cm.stateful.sessionsync.RollbackBean.TestNames.currentTestKey;
import static com.sun.ts.tests.ejb30.lite.tx.cm.stateful.sessionsync.RollbackBean.TestNames.isTxCommittedKey;
import static com.sun.ts.tests.ejb30.lite.tx.cm.stateful.sessionsync.RollbackBean.TestNames.methodException;
import static com.sun.ts.tests.ejb30.lite.tx.cm.stateful.sessionsync.RollbackBean.TestNames.methodSetRollbackOnly;
import static com.sun.ts.tests.ejb30.lite.tx.cm.stateful.sessionsync.SessionSyncIF.afterBegin;
import static com.sun.ts.tests.ejb30.lite.tx.cm.stateful.sessionsync.SessionSyncIF.afterCompletion;
import static com.sun.ts.tests.ejb30.lite.tx.cm.stateful.sessionsync.SessionSyncIF.aroundInvoke1;
import static com.sun.ts.tests.ejb30.lite.tx.cm.stateful.sessionsync.SessionSyncIF.aroundInvoke2;
import static com.sun.ts.tests.ejb30.lite.tx.cm.stateful.sessionsync.SessionSyncIF.beforeCompletion;
import static com.sun.ts.tests.ejb30.lite.tx.cm.stateful.sessionsync.SessionSyncIF.getHistory;

import java.util.Arrays;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.NoSuchEJBException;

import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;
import com.sun.ts.tests.ejb30.common.statussingleton.StatusSingletonBean;

@EJB(name = "ejb/rollbackBean", beanInterface = RollbackBean.class, beanName = "RollbackBean")
public class Client extends EJBLiteClientBase {
  @EJB(beanName = "AnnotatedBean")
  private SessionSyncIF annotatedBean;

  @EJB(beanName = "ImplementingBean")
  private SessionSyncIF implementingBean;

  @EJB(beanName = "DescriptorBean")
  private SessionSyncIF descriptorBean;

  @EJB(beanName = "StatusSingletonBean")
  private StatusSingletonBean statusSingleton;

  /*
   * @testName: sessionSynchronizationCallbackSequence
   * 
   * @test_Strategy: verify the correct sequence of SessionSynchronization
   * 
   * callbacks SessionSynchronization methods can be specified by implementing
   * SessionSynchronization interface, or annotations, or ejb-jar.xml;
   * 
   * SessionSynchronization callback methods can have public, protected, private
   * or default access;
   * 
   * Bean class can implement all or some SessionSynchronization methods, and
   * only those implemented callback can be called;
   * 
   * The correct sequence when interceptor is used (see expectedAll variable in
   * test method)
   * 
   * Such bean classes can only have Required, Requires_New, or Mandatory
   * attributes
   */
  public void sessionSynchronizationCallbackSequence() {
    final List<String> expectedAll = Arrays.asList(afterBegin, aroundInvoke1,
        getHistory, aroundInvoke2, beforeCompletion, afterCompletion);
    final List<String> historyAll = implementingBean.getHistory();
    assertEquals(null, expectedAll, historyAll);

    final List<String> expectedPartial = Arrays.asList(afterBegin,
        aroundInvoke1, getHistory, aroundInvoke2, beforeCompletion);
    for (SessionSyncIF b : new SessionSyncIF[] { annotatedBean,
        descriptorBean }) {
      final List<String> history = b.getHistory();
      assertEquals(null, expectedPartial, history);
    }
  }

  /*
   * @testName: afterBeginException
   * 
   * @test_Strategy: verify tx rollback caused by system exception and
   * setRollbackOnly calls inside business method and SessionSynchronization
   * callback methods.
   * 
   * The container must handle system exceptions: If the instance is in a
   * transaction, mark the transaction for rollback.
   * 
   * Discard the instance (i.e., the container must not invoke any business
   * methods or container callbacks on the instance).
   * 
   * If the instance executed in the client transaction, the container should
   * throw the javax.ejb.EJBTransactionRolledbackException
   */
  public void afterBeginException() {
    rollback(afterBeginException, true, null);
  }

  /*
   * @testName: methodException
   * 
   * @test_Strategy: see test afterBeginException
   */
  public void methodException() {
    rollback(methodException, true, null);
  }

  /*
   * @testName: beforeCompletionException
   * 
   * @test_Strategy: see test afterBeginException
   */
  public void beforeCompletionException() {
    rollback(beforeCompletionException, true, null);
  }

  public void afterCompletionException() {
    // the transaction should have been committed, but we have no way
    // to check, as the bean instance is already discarded.
    rollback(afterCompletionException, true, null);
  }

  /*
   * @testName: afterBeginSetRollbackOnly
   * 
   * @test_Strategy: see test afterBeginException
   */
  public void afterBeginSetRollbackOnly() {
    rollback(afterBeginSetRollbackOnly, false, false);
  }

  /*
   * @testName: methodSetRollbackOnly
   * 
   * @test_Strategy: see test afterBeginException
   */
  public void methodSetRollbackOnly() {
    rollback(methodSetRollbackOnly, false, false);
  }

  public void beforeCompletionSetRollbackOnly() {
    rollback(beforeCompletionSetRollbackOnly, false, false);
  }

  private void rollback(RollbackBean.TestNames testName,
      boolean expectingException, Boolean expectedTxResult) {
    RollbackBean b = (RollbackBean) lookup("ejb/rollbackBean", "RollbackBean",
        null);

    statusSingleton.addResult(currentTestKey.toString(), testName.toString());
    statusSingleton.getAndResetResult(isTxCommittedKey.toString());

    if (expectingException) {
      try {
        b.rollback();
        throw new RuntimeException("Expecting EJBException, but got none.");
      } catch (NoSuchEJBException e) {
        throw new RuntimeException(
            "Expecting other EJBException, but got " + e);
      } catch (EJBException e) {
        appendReason("Got expected " + e);
        // the bean instance should have been discarded.
        try {
          b.rollback();
          throw new RuntimeException(
              "Expecting NoSuchEJBException, but got none.");
        } catch (NoSuchEJBException ee) {
          appendReason("Got expected " + ee);
        }
      }
    } else {
      b.rollback();
    }
    if (expectedTxResult == null) {
      return;
    }
    assertEquals(null, expectedTxResult, b.getAndResetTransactionStatus());
  }
}
