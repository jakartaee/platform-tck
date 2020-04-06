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

package com.sun.ts.tests.ejb30.bb.session.stateful.cm.allowed;

import com.sun.ts.tests.ejb30.common.allowed.NoTxAllowedIF;
import com.sun.ts.tests.ejb30.common.allowed.stateful.StatefulOperations;
import javax.annotation.Resource;
import jakarta.ejb.Remote;
import jakarta.ejb.Remove;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;

@Stateful(name = "NoTxAllowedBean")
@Remote({ NoTxAllowedIF.class })
public class NoTxAllowedBean implements NoTxAllowedIF, java.io.Serializable {

  @Resource(name = "ejbContext")
  SessionContext sctx;

  // ===================== business methods ===========================
  @Remove
  public void remove() {
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public void txSupports()
      throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
    StatefulOperations.getInstance().tryRollback(sctx);
  }

  @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
  public void txNotSupported()
      throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
    StatefulOperations.getInstance().tryRollback(sctx);
  }

  @TransactionAttribute(TransactionAttributeType.NEVER)
  public void txNever()
      throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
    StatefulOperations.getInstance().tryRollback(sctx);
  }

}
