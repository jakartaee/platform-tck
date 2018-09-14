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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.ejb.EJB;
import javax.ejb.EJBException;

import com.sun.ts.tests.ejb30.common.calc.CalculatorException;
import com.sun.ts.tests.ejb30.bb.async.common.AsyncClientBase;

/**
 * These tests verify various ways of specifying @Asynchronous on interfaces and
 * bean class and their superclasses. Some of these requirements are also
 * covered in ../annotated directory. This test directory focuses on type-level
 * 
 * @Asynchronous on business interface and super-interfaces, and type-level
 * @Asynchronous on bean superclasses.
 */

public class MetadataClientBase extends AsyncClientBase {
  @EJB(name = "beanClassLevel", beanName = "BeanClassLevelBean")
  protected PlainInterfaceTypeLevelIF beanClassLevel;

  @EJB(name = "beanClassLevelRemote", beanName = "BeanClassLevelBean")
  protected PlainInterfaceTypeLevelRemoteIF beanClassLevelRemote;

  protected PlainInterfaceTypeLevelIF getBeanClassLevel() {
    return beanClassLevel;
  }

  protected PlainInterfaceTypeLevelRemoteIF getBeanClassLevelRemote() {
    return beanClassLevelRemote;
  }

  // for stateful, after calling voidRuntimeException, the bean instance will be
  // discarded,
  // and the subsequent b.futureRuntimeException will generate EJBException("not
  // such ejb...")
  // so pass in b2 for use in the second call.
  protected void beanClassLevelRuntimeException(PlainInterfaceTypeLevelIF b,
      PlainInterfaceTypeLevelIF b2)
      throws InterruptedException, TimeoutException {
    b.voidRuntimeException();
    try {
      b2.futureRuntimeException().get(1, TimeUnit.MINUTES);
      throw new RuntimeException("Expecting ExecutionException, but got none.");
    } catch (final ExecutionException ex) {
      final EJBException ejbException = (EJBException) ex.getCause();
      final RuntimeException runtimeException = (RuntimeException) ejbException
          .getCause();
      assertEquals(null, "futureRuntimeException",
          runtimeException.getMessage());
    }
  }

  protected void customFutureImpl(PlainInterfaceTypeLevelIF b)
      throws InterruptedException, ExecutionException {
    final TimeUnit timeUnit = TimeUnit.NANOSECONDS;
    final Future<TimeUnit> result = b.customFutureImpl(timeUnit);
    for (int i = 0; i < 2; i++) {
      assertEquals(result.toString(), timeUnit, result.get());
    }
  }

  protected void beanClassLevelSyncMethod(PlainInterfaceTypeLevelIF b) {
    try {
      b.syncMethodException0();
      throw new RuntimeException(
          "Expecting CalculatorException, but got none.");
    } catch (final CalculatorException ex) {
      appendReason("Got expected " + ex);
    }
    try {
      b.syncMethodException3();
      throw new RuntimeException(
          "Expecting CalculatorException, but got none.");
    } catch (final CalculatorException ex) {
      appendReason("Got expected " + ex);
    }
  }

  /*
   * testName: beanClassLevelReturnType
   * 
   * @test_Strategy:verify 2 types of return types in bean class: Future<T> and
   * T.
   */
  public void beanClassLevelReturnType()
      throws InterruptedException, ExecutionException {
    final boolean expected = true;
    assertEquals(null, expected, getBeanClassLevel().futureReturnType().get());
    assertEquals(null, expected,
        getBeanClassLevelRemote().futureReturnType().get());
  }

  /*
   * testName: beanClassLevelRuntimeException
   * 
   * @test_Strategy: for async method with void return type, RuntimeException is
   * not visible to the client. For Future return type, RuntimeException is
   * wrapped as EJBException and then as ExecutionException.
   */
  public void beanClassLevelRuntimeException()
      throws InterruptedException, TimeoutException {
    beanClassLevelRuntimeException(getBeanClassLevel(), getBeanClassLevel());
    beanClassLevelRuntimeException(getBeanClassLevelRemote(),
        getBeanClassLevelRemote());
  }

  /*
   * testName: customFutureImpl
   * 
   * @test_Strategy: Async method returning a custom Future impl.
   */
  public void customFutureImpl()
      throws InterruptedException, ExecutionException {
    customFutureImpl(getBeanClassLevel());
    customFutureImpl(getBeanClassLevelRemote());
  }

  /*
   * testName: beanClassLevelSyncMethod
   * 
   * @test_Strategy: syncMethodException is implemented in a bean superclass
   * that is not annotated with @Asynchronous.
   */
  public void beanClassLevelSyncMethod() {
    beanClassLevelSyncMethod(getBeanClassLevel());
    beanClassLevelSyncMethod(getBeanClassLevelRemote());
  }
}
