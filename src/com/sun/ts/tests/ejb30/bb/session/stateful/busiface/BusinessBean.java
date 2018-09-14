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

package com.sun.ts.tests.ejb30.bb.session.stateful.busiface;

import com.sun.ts.tests.ejb30.common.busiface.BusinessBeanBase;
import com.sun.ts.tests.ejb30.common.busiface.BusinessIF1;
import com.sun.ts.tests.ejb30.common.busiface.BusinessIF2;
import com.sun.ts.tests.ejb30.common.busiface.BusinessLocalIF1;
import com.sun.ts.tests.ejb30.common.busiface.BusinessLocalIF2;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.SessionContext;
import javax.annotation.Resource;
import javax.ejb.EJBException;

@Stateful(name = "BusinessBean")
@Remote({ BusinessIF1.class, BusinessIF2.class })
@Local({ BusinessLocalIF1.class, BusinessLocalIF2.class })
public class BusinessBean extends BusinessBeanBase
    implements java.io.Serializable {
  @Resource
  private SessionContext sessionContext;

  public BusinessBean() {
  }

  @Remove
  public void remove() {
  }

  @Remove
  public void remove2() {
  }

  //////////////////////////////////////////////////////////////////////
  // These 3 methods should not be called since this bean does not
  // implement SessionSynchronization. Their superclass implementation
  // throws EJBException upon being called.
  // Having these methods here to verify they must not be called.
  //////////////////////////////////////////////////////////////////////
  @Override
  public void afterCompletion(boolean b) throws EJBException {
    super.afterCompletion(b);
  }

  @Override
  public void beforeCompletion() throws EJBException {
    super.beforeCompletion();
  }

  @Override
  public void afterBegin() throws EJBException {
    super.afterBegin();
  }
}
