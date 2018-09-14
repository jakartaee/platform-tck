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

package com.sun.ts.tests.ejb30.tx.common.session.inheritance;

import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.NamingException;

abstract public class BBeanBase extends TxBeanBase {

  public BBeanBase() {
  }

  // @TransactionAttribute(TransactionAttributeType.NEVER)
  // is annotated in super class method implementation. It
  // must not be inherited for this method. This method has
  // REQUIRES_NEW transaction attribute.
  @Override()
  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  public String foo() {
    return super.foo();
  }

  @Override()
  protected void fooImpl() {
    setRollbackOnly();
  }
}
