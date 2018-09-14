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

package com.sun.ts.tests.ejb30.bb.session.stateless.interceptor.method.descriptor;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.annotation.Resource;
import javax.interceptor.InvocationContext;
import javax.ejb.SessionContext;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import com.sun.ts.tests.ejb30.common.interceptor.AroundInvokeBase;
import com.sun.ts.tests.ejb30.common.interceptor.AroundInvokeIF;
import com.sun.ts.tests.ejb30.common.interceptor.AroundInvokeTestImpl;
import com.sun.ts.tests.ejb30.common.helper.TLogger;

@Stateless(name = "AroundInvokeBean")
@Remote({ AroundInvokeIF.class })
// This bean must use cmt, since it uses setRollbackOnly
@TransactionManagement(TransactionManagementType.CONTAINER)
// @todo redundant implements

public class AroundInvokeBean extends AroundInvokeBase
    implements AroundInvokeIF {
  @Resource(name = "ejbContext")
  private SessionContext ejbContext;

  public AroundInvokeBean() {
    super();
  }

  // @AroundInvoke
  public Object intercept(InvocationContext ctx) throws Exception {
    TLogger.log("Creating AroundInvokeTestImpl with: ejbContext=" + ejbContext
        + ";" + " bean=" + this + ", callerPrincipal="
        + ejbContext.getCallerPrincipal());
    AroundInvokeTestImpl helper = new AroundInvokeTestImpl(this,
        getEJBContext().getCallerPrincipal());
    return helper.intercept(ctx);
  }

  // ============ abstract methods from super ==========================
  protected javax.ejb.EJBContext getEJBContext() {
    return this.ejbContext;
  }

  // ============= override business methods from super ================
}
