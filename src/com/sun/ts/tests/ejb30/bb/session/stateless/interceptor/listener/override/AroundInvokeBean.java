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

package com.sun.ts.tests.ejb30.bb.session.stateless.interceptor.listener.override;

import javax.interceptor.AroundInvoke;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.annotation.Resource;
import javax.interceptor.InvocationContext;
import javax.ejb.SessionContext;
import javax.interceptor.Interceptors;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import com.sun.ts.tests.ejb30.common.interceptor.AroundInvokeBase;
import com.sun.ts.tests.ejb30.common.interceptor.AroundInvokeIF;
import com.sun.ts.tests.ejb30.common.interceptor.AroundInvokeTestImpl;

@Stateless(name = "AroundInvokeBean")
@Remote({ AroundInvokeIF.class })
// This bean must use cmt, since it uses setRollbackOnly
@TransactionManagement(TransactionManagementType.CONTAINER)
@Interceptors({ com.sun.ts.tests.ejb30.common.interceptor.Interceptor2.class,
    com.sun.ts.tests.ejb30.common.interceptor.Interceptor1.class })

public class AroundInvokeBean extends AroundInvokeBase
    implements AroundInvokeIF {
  @Resource(name = "ejbContext")
  private SessionContext ejbContext;

  public AroundInvokeBean() {
    super();
  }

  // ============ abstract methods from super ==========================
  protected javax.ejb.EJBContext getEJBContext() {
    return this.ejbContext;
  }

  @AroundInvoke
  public Object intercept(InvocationContext ctx) throws Exception {
    // this interceptor should be invoked last, unless overrid by deployment
    // descriptor.
    Object result = null;
    int orderInChain = 3;
    result = AroundInvokeTestImpl.intercept2(ctx, orderInChain);
    return result;
  }

  // ============= override business methods from super ================

}
