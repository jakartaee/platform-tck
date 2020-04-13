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

package com.sun.ts.tests.ejb30.common.callback;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;
import org.omg.CORBA.ORB;

/**
 * A callback listerner for stateless session beans. It uses ejb* for method
 * names. Callback methods may throw RuntimeException. They are declared in the
 * throws list, though not necessary.
 */
public class InterceptorA extends InterceptorBase {
  @Resource()
  private ORB orb;

  public InterceptorA() {
    super();
  }

  public String getInjectedLocation() {
    String result = (orb == null) ? NOT_INJECTED : "A";
    return result;
  }

  @Override
  protected String getShortName() {
    return "A";
  }

  @PostConstruct
  protected void myCreate(InvocationContext inv) throws RuntimeException {
    myCreate0(inv, "A");
  }

  @PreDestroy
  protected void myRemove(InvocationContext inv) throws RuntimeException {
    super.myRemove(inv);
  }

  @AroundInvoke
  protected Object intercept(InvocationContext inv) throws Exception {
    return super.intercept(inv);
  }
}
