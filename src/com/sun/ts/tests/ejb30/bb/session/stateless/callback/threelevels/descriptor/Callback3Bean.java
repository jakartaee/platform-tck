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

package com.sun.ts.tests.ejb30.bb.session.stateless.callback.threelevels.descriptor;

import javax.annotation.PostConstruct;
import jakarta.ejb.EJBContext;
import javax.interceptor.ExcludeDefaultInterceptors;
import jakarta.ejb.Remote;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;
import javax.annotation.Resource;

import com.sun.ts.tests.ejb30.common.callback.Callback2IF;
import com.sun.ts.tests.ejb30.common.callback.Callback2BeanBase;

@Stateless(name = "Callback3Bean")
@Remote({ Callback2IF.class })
@ExcludeDefaultInterceptors
public class Callback3Bean extends Callback2BeanBase implements Callback2IF {
  private boolean postConstructOrPreDestroyCalled;

  @Resource
  private SessionContext sctx;

  public Callback3Bean() {
    super();
  }

  public EJBContext getEJBContext() {
    return this.sctx;
  }

  @PostConstruct
  @Override
  protected void postConstructMethod() throws RuntimeException {
    // addPostConstructCall(BEAN_SHORT_NAME);
    super.postConstructMethod();
  }

  // ================== business methods ====================================

}
