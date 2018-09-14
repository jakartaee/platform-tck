/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.bb.async.stateful.annotated;

import java.util.concurrent.Future;

import javax.ejb.Asynchronous;
import javax.ejb.Stateful;

import com.sun.ts.tests.ejb30.common.calc.CalculatorException;
import com.sun.ts.tests.ejb30.bb.async.common.annotated.AsyncBeanBase;

@Stateful
public class AsyncBean extends AsyncBeanBase {

  @Override
  @Asynchronous
  public void addAway(int a, int b, int key) {
    super.addAway(a, b, key);
  }

  @Override
  @Asynchronous
  public Future<Integer> addReturn(int a, int b) {
    return super.addReturn(a, b);
  }

  @Asynchronous
  @Override
  public Future<Integer> futureRuntimeException() throws RuntimeException {
    return super.futureRuntimeException();
  }

  @Override
  @Asynchronous
  public Future<Integer> futureException() throws CalculatorException {
    return super.futureException();
  }

  @Override
  @Asynchronous
  public Future<Integer> identityHashCode() {
    return super.identityHashCode();
  }

  @Asynchronous
  @Override
  public void voidRuntimeException(Integer key) throws RuntimeException {
    super.voidRuntimeException(key);
  }
}
