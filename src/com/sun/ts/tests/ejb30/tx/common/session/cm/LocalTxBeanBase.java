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
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

abstract public class LocalTxBeanBase extends TxBeanBase0 implements LocalTxIF {
  public void localMandatoryTest() {
  }

  public void neverTest() {
  }

  public void requiresNewNoop() throws TestFailedException {
  }

  public void localRequiresNewTest() {
    getSessionContext().setRollbackOnly();
  }

  public void localRequiresNewRemoveTest() {
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public String localSupportsTest() throws TestFailedException {
    return illegalGetSetRollbackOnly(TransactionAttributeType.SUPPORTS);
  }

  @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
  public String localIllegalGetSetRollbackOnlyNotSupportedTest()
      throws TestFailedException {
    return illegalGetSetRollbackOnly(TransactionAttributeType.NOT_SUPPORTED);
  }

  @TransactionAttribute(TransactionAttributeType.NEVER)
  public String localIllegalGetSetRollbackOnlyNeverTest()
      throws TestFailedException {
    return illegalGetSetRollbackOnly(TransactionAttributeType.NEVER);
  }

}
