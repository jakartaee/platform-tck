/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.common;

import java.util.concurrent.Future;

import com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.StatefulConcurrencyIF;

public interface AccessTimeoutIF extends StatefulConcurrencyIF {

  public static final long SUPER_CLASS_METHOD_LEVEL_TIMEOUT_MILLIS = 1000;

  public static final long SUPER_CLASS_LEVEL_TIMEOUT_MILLIS = PING_WAIT_MILLIS
      / 2;

  public static final long SUPER_CLASS_METHOD_LEVEL_OVERRIDE_TIMEOUT_MILLIS = PING_WAIT_MILLIS
      * 2;

  public static final long BEAN_CLASS_LEVEL_TIMEOUT_MILLIS = PING_WAIT_MILLIS
      / 2;

  public static final long BEAN_METHOD_LEVEL_TIMEOUT_MILLIS = PING_WAIT_MILLIS
      / 2;

  public static final long BEAN_METHOD_LEVEL_OVERRIDE_TIMEOUT_MILLIS = PING_WAIT_MILLIS
      * 2;

  public static final String annotatedSuperClassAccessTimeoutBeanLocal = "annotatedSuperClassAccessTimeoutBeanLocal";

  public static final String beanClassLevelAccessTimeoutBeanLocal = "beanClassLevelAccessTimeoutBeanLocal";

  public static final String beanClassMethodLevelAccessTimeoutBeanLocal = "beanClassMethodLevelAccessTimeoutBeanLocal";

  public static final String beanClassMethodLevelOverrideAccessTimeoutBeanLocal = "beanClassMethodLevelOverrideAccessTimeoutBeanLocal";

  public static final String annotatedSuperClassAccessTimeoutBeanRemote = "annotatedSuperClassAccessTimeoutBeanRemote";

  public static final String beanClassLevelAccessTimeoutBeanRemote = "beanClassLevelAccessTimeoutBeanRemote";

  public static final String beanClassMethodLevelAccessTimeoutBeanRemote = "beanClassMethodLevelAccessTimeoutBeanRemote";

  public static final String beanClassMethodLevelOverrideAccessTimeoutBeanRemote = "beanClassMethodLevelOverrideAccessTimeoutBeanRemote";

  /**
   * These methods are not async method unless declared as such in subclasses.
   */
  Future<String> beanClassLevel();

  Future<String> beanClassLevel2();

  Future<String> beanSuperClassLevel();

  Future<String> beanClassMethodLevel();

  Future<String> beanSuperClassMethodLevel();

  Future<String> beanClassMethodLevelOverride();

  Future<String> beanSuperClassMethodLevelOverride();
}
