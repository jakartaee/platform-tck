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

package com.sun.ts.tests.ejb30.bb.session.stateless.bm.allowed;

import com.sun.ts.tests.ejb30.common.allowed.AllowedBeanBase;
import com.sun.ts.tests.ejb30.common.allowed.AllowedIF;
import com.sun.ts.tests.ejb30.common.allowed.AllowedLocalIF;
import com.sun.ts.tests.ejb30.common.allowed.Operations;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import java.util.Properties;
import javax.interceptor.AroundInvoke;
import javax.annotation.Resource;
import javax.interceptor.Interceptors;
import javax.interceptor.InvocationContext;
import jakarta.ejb.Local;
import jakarta.ejb.Remote;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;
import jakarta.ejb.Timeout;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import com.sun.ts.tests.ejb30.common.allowed.CancelInterceptor;

@Stateless(name = "AllowedBean")
@Remote({ AllowedIF.class })
@Local({ AllowedLocalIF.class })
@TransactionManagement(TransactionManagementType.BEAN)
@Interceptors(com.sun.ts.tests.ejb30.common.allowed.CancelInterceptor.class)
public class AllowedBean extends AllowedBeanBase
    implements AllowedIF, AllowedLocalIF {

  @Resource(name = "ejbContext")
  public void setSessionContext(SessionContext sc) {
    super.setSessionContext(sc);
  }

  @AroundInvoke
  public Object intercept(InvocationContext inv) throws Exception {
    return super.intercept(inv);
  }

  @Timeout
  // @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  public void timeout(jakarta.ejb.Timer timer) {
    TLogger.log("timeout: " + this.getClass().getName());
  }

  @Override
  public Properties runOperations(SessionContext sctx) {
    return Operations.getInstance().run2(sctx, AllowedIF.class);
  }

  // ===================== business methods ===========================
}
