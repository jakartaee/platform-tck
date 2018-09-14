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

package com.sun.ts.tests.ejb30.tx.session.stateless.cm.descriptor;

import com.sun.ts.tests.ejb30.tx.common.session.cm.TxBeanBase;
import com.sun.ts.tests.ejb30.tx.common.session.cm.TxIF;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;

@Stateless()
@Remote({ TxIF.class, SameMethodRemoteIF.class })
@Local(SameMethodLocalIF.class)
// @TransactionAttribute(TransactionAttributeType.NEVER)
public class TxBean extends TxBeanBase
    implements TxIF, SameMethodLocalIF, SameMethodRemoteIF {
  // @TransactionAttribute(TransactionAttributeType.MANDATORY)
  public void mandatoryTest() {
    super.mandatoryTest();
  }

  // @TransactionAttribute(TransactionAttributeType.NEVER)
  public void neverTest() {
    super.neverTest();
  }

  public void sameMethod() {
  }

  // The two overloaded methods share the same transaction-attribute with
  // their respective no-arg methods, since transaction-attribute is specified
  // in ejb-jar.xml without using <method-params>
  public void neverTest(String s) {
    super.neverTest(s);
  }

  public void mandatoryTest(String s) {
    super.mandatoryTest(s);
  }

}
