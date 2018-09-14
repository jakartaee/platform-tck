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
import javax.ejb.Local;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

@Stateful
@Local({ LocalTxIF.class })
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class StatefulLocalTxBean extends LocalTxBeanBase implements LocalTxIF {
  @TransactionAttribute(TransactionAttributeType.MANDATORY)
  public void localMandatoryTest() {
    super.localMandatoryTest();
  }

  @TransactionAttribute(TransactionAttributeType.NEVER)
  public void neverTest() {
    super.neverTest();
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  public void requiresNewNoop() throws TestFailedException {
    // super.requiresNewNoop();
    throw new TestFailedException();
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  public void localRequiresNewTest() {
    super.localRequiresNewTest();
  }

  // type-level REQUIRES_NEW
  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  @Remove
  public void localRequiresNewRemoveTest() {
    super.localRequiresNewRemoveTest();
  }
}
