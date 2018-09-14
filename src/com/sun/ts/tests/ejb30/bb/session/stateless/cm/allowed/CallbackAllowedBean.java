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

package com.sun.ts.tests.ejb30.bb.session.stateless.cm.allowed;

import com.sun.ts.tests.ejb30.common.allowed.CallbackAllowedBeanBase;
import com.sun.ts.tests.ejb30.common.allowed.CallbackAllowedIF;
import com.sun.ts.tests.ejb30.common.allowed.CallbackAllowedLocalIF;
import com.sun.ts.tests.ejb30.common.allowed.Operations;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import java.util.Properties;
import javax.annotation.Resource;
import javax.interceptor.Interceptors;
import javax.ejb.Local;
import javax.annotation.PostConstruct;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless(name = "CallbackAllowedBean")
@Remote({ CallbackAllowedIF.class })
@Local({ CallbackAllowedLocalIF.class })
@Interceptors({ com.sun.ts.tests.ejb30.common.allowed.CancelInterceptor.class })
public class CallbackAllowedBean extends CallbackAllowedBeanBase
    implements CallbackAllowedIF, CallbackAllowedLocalIF {

  @PostConstruct
  public void ejbCreate() {
    super.ejbCreate();
  }

  @Resource(name = "ejbContext")
  public void setSessionContext(SessionContext sc) {
    super.setSessionContext(sc);
  }

  @Timeout
  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  public void timeout(javax.ejb.Timer timer) {
    TLogger.log("timeout: " + this.getClass().getName());
  }

  @Override
  public Properties runOperations(SessionContext sctx) {
    Operations op = Operations.getInstance();
    return op.run2(sctx, CallbackAllowedIF.class);
    // only supports remote access
  }
}
