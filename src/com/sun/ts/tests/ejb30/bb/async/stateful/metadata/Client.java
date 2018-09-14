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

package com.sun.ts.tests.ejb30.bb.async.stateful.metadata;

import com.sun.ts.tests.ejb30.bb.async.common.metadata.PlainInterfaceTypeLevelIF;
import com.sun.ts.tests.ejb30.bb.async.common.metadata.PlainInterfaceTypeLevelRemoteIF;

/**
 * See superclass ClientBase
 */
public class Client
    extends com.sun.ts.tests.ejb30.bb.async.common.metadata.MetadataClientBase {

  @Override
  protected PlainInterfaceTypeLevelIF getBeanClassLevel() {
    return (PlainInterfaceTypeLevelIF) lookup("beanClassLevel",
        "BeanClassLevelBean", PlainInterfaceTypeLevelIF.class);
  }

  @Override
  protected PlainInterfaceTypeLevelRemoteIF getBeanClassLevelRemote() {
    return (PlainInterfaceTypeLevelRemoteIF) lookup("beanClassLevelRemote",
        "BeanClassLevelBean", PlainInterfaceTypeLevelRemoteIF.class);
  }

  /*
   * @testName: beanClassLevelReturnType
   * 
   * @test_Strategy:verify 2 types of return types in bean class: Future<T> and
   * T.
   */
  /*
   * @testName: beanClassLevelRuntimeException
   * 
   * @test_Strategy: for async method with void return type, RuntimeException is
   * not visible to the client. For Future return type, RuntimeException is
   * wrapped as EJBException and then as ExecutionException.
   */
  /*
   * @testName: customFutureImpl
   * 
   * @test_Strategy: Async method returning a custom Future impl.
   */
  /*
   * @testName: beanClassLevelSyncMethod
   * 
   * @test_Strategy: syncMethodException is implemented in a bean superclass
   * that is not annotated with @Asynchronous.
   */
}
