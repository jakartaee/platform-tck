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

import com.sun.ts.tests.ejb30.common.allowed.stateful.StatefulOperations;
import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import java.util.Properties;
import javax.ejb.SessionContext;
import com.sun.ts.tests.ejb30.common.allowed.Constants;
import com.sun.ts.tests.ejb30.common.allowed.Operations;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import javax.ejb.EJBException;
import javax.ejb.TimerHandle;
import javax.transaction.UserTransaction;

public class StatefulBMTOperations extends StatefulOperations
    implements Constants {

  private static StatefulBMTOperations instance = new StatefulBMTOperations();

  protected StatefulBMTOperations() {
    super();
  }

  public static StatefulBMTOperations getInstance() {
    return instance;
  }

  @Override
  public void runRollbackOnly(SessionContext sctx, Properties results) {
    getSetRollbackOnly(sctx, results);
  }

  @Override
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
    // try {
    // sctx.getUserTransaction().rollback();
    // results.setProperty(UserTransaction_Methods_Test4, allowed);
    // } catch (IllegalStateException e) {
    // results.setProperty(UserTransaction_Methods_Test4, disallowed);
    // } catch (Exception e) {
    // results.setProperty(UserTransaction_Methods_Test4, e.toString());
    // }
    //
    // // UserTransaction Methods Test5
    // try {
    // sctx.getUserTransaction().setRollbackOnly();
    // results.setProperty(UserTransaction_Methods_Test5, allowed);
    // } catch (IllegalStateException e) {
    // results.setProperty(UserTransaction_Methods_Test5, disallowed);
    // } catch (Exception e) {
    // results.setProperty(UserTransaction_Methods_Test5, e.toString());
    // }

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
}
