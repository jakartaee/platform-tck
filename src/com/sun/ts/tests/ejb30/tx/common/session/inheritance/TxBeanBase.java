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
import javax.ejb.EJBContext;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.NamingException;

@TransactionAttribute(TransactionAttributeType.MANDATORY)
abstract public class TxBeanBase implements TxCommonIF {

  public TxBeanBase() {
  }

  @TransactionAttribute(TransactionAttributeType.NEVER)
  public String foo() {
    fooImpl();
    return "foo";
  }

  public String foo(String s) {
    return s;
  }

  public String foo(String s, String ss) {
    return s + ss;
  }

  public String bar() {
    barImpl();
    return "bar";
  }

  /**
   * subclass may override this method to customize the behavior of foo(),
   * without overriding foo(), because overriding foo may affect foo's
   * transaction attribute.
   */
  protected void fooImpl() {
    // noop
  }

  protected void barImpl() {
    // noop
  }

  protected void setRollbackOnly() {
    EJBContext ejbContext = null;
    try {
      ejbContext = (EJBContext) ServiceLocator.lookup("java:comp/EJBContext");
    } catch (NamingException ex) {
      throw new IllegalArgumentException(ex);
    }
    ejbContext.setRollbackOnly();
  }
}
