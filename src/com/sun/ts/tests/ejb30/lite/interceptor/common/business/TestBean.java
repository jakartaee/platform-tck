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
package com.sun.ts.tests.ejb30.lite.interceptor.common.business;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.ExcludeDefaultInterceptors;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.ejb30.common.appexception.AtCheckedRollbackAppException;
import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;

@Singleton
@ExcludeDefaultInterceptors
@TransactionManagement(TransactionManagementType.BEAN)
public class TestBean {

  @Resource
  private UserTransaction ut;

  public String applicationExceptionRollback() {
    StringBuilder sb = new StringBuilder();
    InterceptorIF interceptorBean = (InterceptorIF) ServiceLocator
        .lookupNoTry("java:module/InterceptorBean");
    try {
      ut.begin();
      interceptorBean.applicationExceptionRollback();
      throw new RuntimeException(
          "Expecting AtCheckedRollbackAppException, but got none");
    } catch (AtCheckedRollbackAppException e) {
      sb.append("Got expected " + e);
      int status;
      try {
        status = ut.getStatus();
      } catch (Exception e2) {
        throw new RuntimeException(e2);
      }
      String statusDisplay = TestUtil.getTransactionStatus(status);
      if (status == Status.STATUS_MARKED_ROLLBACK
          || status == Status.STATUS_ROLLEDBACK
          || status == Status.STATUS_ROLLING_BACK) {
        sb.append("Got expected status code " + statusDisplay);
      } else {
        throw new RuntimeException(
            "Got unexpected transaction status code: " + statusDisplay);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      try {
        ut.rollback();
      } catch (Exception ignore) {
      }
    }
    return sb.toString();
  }
}
