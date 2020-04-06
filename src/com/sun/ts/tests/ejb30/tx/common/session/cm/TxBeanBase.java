/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
import jakarta.ejb.SessionContext;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;

abstract public class TxBeanBase extends TxBeanBase0 implements TxIF {
  public void mandatoryTest() {

  }

  public void neverTest() {
  }

  public void neverTest(String s) {
  }

  public void mandatoryTest(String s) {
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public String supportsTest() throws TestFailedException {
    return illegalGetSetRollbackOnly(TransactionAttributeType.SUPPORTS);
  }

  @TransactionAttribute(TransactionAttributeType.NEVER)
  public String illegalGetSetRollbackOnlyNeverTest()
      throws TestFailedException {
    return illegalGetSetRollbackOnly(TransactionAttributeType.NEVER);
  }

  @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
  public String illegalGetSetRollbackOnlyNotSupportedTest()
      throws TestFailedException {
    return illegalGetSetRollbackOnly(TransactionAttributeType.NOT_SUPPORTED);
  }

}
