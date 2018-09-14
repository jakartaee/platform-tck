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

import com.sun.ts.tests.ejb30.common.callback.InterceptorI;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.interceptor.ExcludeDefaultInterceptors;
import javax.interceptor.Interceptors;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import com.sun.ts.tests.ejb30.common.callback.Callback2IF;

/**
 * A bean that overrides and thus disables all lifecycle callback methods in its
 * superclasses. Its class-level interceptor, InterceptorH, also overrides and
 * disables its superclasses' lifecycle callback methods. In both cases, one
 * overriding method is still @PostConstruct method, and the other is
 * re-annotated as @PreDestroy method.
 */
@Stateless(name = "Callback2Bean")
@Remote({ Callback2IF.class })
@Interceptors({ InterceptorI.class })
@ExcludeDefaultInterceptors
public class Callback2Bean extends Callback2BeanSuper {
  public Callback2Bean() {
    super();
  }

  @Override
  @PostConstruct
  protected void postConstructMethodInSuperSuper() throws RuntimeException {
    postConstructMethod();
  }

  @Override
  @PreDestroy
  protected void postConstructMethodInSuper() throws RuntimeException {
  }

}
