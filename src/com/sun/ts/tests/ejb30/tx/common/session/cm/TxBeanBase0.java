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

package com.sun.ts.tests.ejb30.tx.common.session.cm;

import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.TransactionAttributeType;

abstract public class TxBeanBase0 {
  @Resource
  private SessionContext sctx;

  public SessionContext getSessionContext() {
    return sctx;
  }

  protected String illegalGetSetRollbackOnly(TransactionAttributeType attr)
      throws TestFailedException {
    String result = "";
    try {
      getSessionContext().getRollbackOnly();
      result += "Calling EJBContext.getRollbackOnly inside a method of tx attr "
          + attr + " should have caused IllegalStateException";
      throw new TestFailedException(result);
    } catch (IllegalStateException expected) {
      result += "Got expected IllegalStateException when call EJBContext.getRollbackOnly inside a method with tx attr "
          + attr + ".";
    }
    try {
      getSessionContext().setRollbackOnly();
      result += "Calling EJBContext.setRollbackOnly inside a method of tx attr "
          + attr + " should have caused IllegalStateException";
      throw new TestFailedException(result);
    } catch (IllegalStateException expected) {
      result += "Got expected IllegalStateException when call EJBContext.setRollbackOnly inside a method with tx attr "
          + attr + ".";
    }
    return result;
  }

  public String systemExceptionTest() throws TestFailedException {
    throw new AssertionError(
        "This AssertionError must cause the tx to be marked for rollback.");
  }
}
