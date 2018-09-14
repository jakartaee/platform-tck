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

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

@Stateless
@Remote({ TxIF.class })
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TxBean extends TxBeanBase implements TxIF {
  @TransactionAttribute(TransactionAttributeType.MANDATORY)
  public void mandatoryTest() {
    super.mandatoryTest();
  }

  @TransactionAttribute(TransactionAttributeType.NEVER)
  public void neverTest() {
    super.neverTest();
  }

  // The two overloaded methods are added to verify that
  // any tx attribute on their no-arg counterpart does not apply here
  public void neverTest(String s) {
    super.neverTest(s);
  }

  public void mandatoryTest(String s) {
    super.mandatoryTest(s);
  }

}
