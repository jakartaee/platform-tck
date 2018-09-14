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

package com.sun.ts.tests.ejb30.bb.async.common.metadata;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.sun.ts.tests.ejb30.common.calc.CalculatorException;

/**
 * @Asynchronous cannot be specified in interface. Move them to bean class or
 *               superclass
 */
public interface PlainInterfaceTypeLevelIF {
  public void voidRuntimeException();

  public Future<Boolean> futureReturnType();

  public Future<Integer> futureRuntimeException();

  public void syncMethodException0() throws CalculatorException;

  public void syncMethodException3() throws CalculatorException;

  public Future<TimeUnit> customFutureImpl(TimeUnit timeUnit);
}
