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
package com.sun.ts.tests.ejb30.lite.stateful.concurrency.common;

import java.util.concurrent.Future;

public interface StatefulConcurrencyIF {
  public static final String containerConcurrencyBeanLocal = "containerConcurrencyBeanLocal";

  public static final String containerConcurrencyBeanRemote = "containerConcurrencyBeanRemote";

  public static final String containerConcurrencyBeanNoInterface = "containerConcurrencyBeanNoInterface";

  public static final String defaultConcurrencyBeanLocal = "defaultConcurrencyBeanLocal";

  public static final String defaultConcurrencyBeanRemote = "defaultConcurrencyBeanRemote";

  public static final String defaultConcurrencyBeanNoInterface = "defaultConcurrencyBeanNoInterface";

  public static final String notAllowedConcurrencyBeanLocal = "notAllowedConcurrencyBeanLocal";

  public static final String notAllowedConcurrencyBeanRemote = "notAllowedConcurrencyBeanRemote";

  public static final String notAllowedConcurrencyBeanNoInterface = "notAllowedConcurrencyBeanNoInterface";

  public static final long PING_WAIT_MILLIS = 10 * 1000;

  public static final int CONCURRENT_INVOCATION_TIMES = 2;

  /**
   * This is not an async method unless declared as such in subclasses.
   */
  Future<String> ping();
}
