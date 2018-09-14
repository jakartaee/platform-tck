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

package com.sun.ts.tests.ejb30.bb.session.stateful.basic;

import com.sun.ts.tests.ejb30.common.calc.BaseRemoteCalculator;
import com.sun.ts.tests.ejb30.common.calc.CalculatorException;
import java.io.Serializable;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.ejb.Remove;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.UserTransaction;

/**
 * A stateful base calculator session bean class that implements
 * javax.ejb.SessionBean. ejbCreate method is optional and not implemented here.
 */

@Stateful(name = "RemoteCalculatorBean", description = "same as <description> element in ejb-jar.xml")
@Remote({ StatefulRemoteCalculator.class })
@TransactionManagement(TransactionManagementType.BEAN)
public class RemoteCalculatorBean extends BaseRemoteCalculator
    implements StatefulRemoteCalculator, Serializable {

  @Resource
  private SessionContext sessionContext;

  // injected in ejb-jar.xml with <resource-env-ref>
  private UserTransaction ut;

  public RemoteCalculatorBean() {
  }

  @Remove
  public void remoteThrowIt() throws CalculatorException {
    if (ut == null) {
      throw new IllegalStateException(
          "UserTransaction is null and has not been injected.");
    }
    super.remoteThrowIt();
  }
}
