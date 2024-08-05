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
 * @(#)InterceptorF.java	1.1 06/02/07
 */

package com.sun.ts.tests.ejb30.common.callback;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.interceptor.InvocationContext;

public class InterceptorH extends InterceptorF {
  public InterceptorH() {
    super();
  }

  @Override
  public String getInjectedLocation() {
    return NOT_INJECTED;
  }

  @Override
  protected String getShortName() {
    return "H";
  }

  @PostConstruct
  protected void myCreateInH(InvocationContext inv) throws RuntimeException {
    myCreate0(inv, "H");
  }

  @PreDestroy
  protected void myRemoveInH(InvocationContext inv) throws RuntimeException {
    myRemove0(inv);
  }

  @Override
  protected void myRemoveInF(InvocationContext inv) throws RuntimeException {
    throw new IllegalStateException("Should not get here.");
  }

  @Override
  protected void myRemoveInE(InvocationContext inv) throws RuntimeException {
    throw new IllegalStateException("Should not get here.");
  }

  @Override
  protected void myRemove(InvocationContext inv) throws RuntimeException {
    throw new IllegalStateException("Should not get here.");
  }

  @Override
  protected void myCreateInF(InvocationContext inv) throws RuntimeException {
    throw new IllegalStateException("Should not get here.");
  }

  @Override
  protected void myCreateInE(InvocationContext inv) throws RuntimeException {
  }

  @Override
  protected void myCreate(InvocationContext inv) throws RuntimeException {
    throw new IllegalStateException("Should not get here.");
  }

}
