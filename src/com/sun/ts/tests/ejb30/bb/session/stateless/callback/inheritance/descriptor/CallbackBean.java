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

package com.sun.ts.tests.ejb30.bb.session.stateless.callback.inheritance.descriptor;

import com.sun.ts.tests.ejb30.common.callback.InterceptorH;
import javax.annotation.PostConstruct;
import javax.interceptor.ExcludeDefaultInterceptors;
import javax.interceptor.Interceptors;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import com.sun.ts.tests.ejb30.common.callback.CallbackIF;

/**
 * A bean that overrides and thus disables all lifecycle callback methods in its
 * superclasses. Its class-level interceptor, InterceptorH, also overrides and
 * disables its superclasses' lifecycle callback methods. In both cases,
 * overriding methods themselves are not lifecycle methods.
 */
@Stateless(name = "CallbackBean")
@Remote({ CallbackIF.class })
@ExcludeDefaultInterceptors
@Interceptors({ InterceptorH.class })
public class CallbackBean extends CallbackBeanSuper {
  public CallbackBean() {
    super();
  }

  @PostConstruct
  @Override
  protected void postConstructMethod() throws RuntimeException {
    // addPostConstructCall(BEAN_SHORT_NAME);
    super.postConstructMethod();
  }

  @Override
  protected void postConstructMethodInSuperSuper() throws RuntimeException {
    throw new IllegalStateException("Should not get here.");
  }

  @Override
  protected void postConstructMethodInSuper() throws RuntimeException {
    throw new IllegalStateException("Should not get here.");
  }

}
