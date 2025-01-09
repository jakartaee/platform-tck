/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.lite.stateful.timeout.common;

public interface StatefulTimeoutIF {
  public static final long TIMEOUT_DAYS = 1;

  public static final long TIMEOUT_HOURS = 1;

  public static final long TIMEOUT_MINUTES = 1;

  public static final long TIMEOUT_SECONDS = 5;

  public static final long TIMEOUT_MILLISECONDS = 1000 * 5;

  public static final long TIMEOUT_MICROSECONDS = 1000 * 1000 * 5;

  public static final long TIMEOUT_NANOSECONDS = 1000 * 1000 * 1000 * 5L;

  /**
   * The number of seconds the test will at least wait before verifying the
   * stateful bean's timeout and removal. The default value, 480 seconds (8
   * minutes), should accommodate most cases. This value can be customized by
   * setting the system property test.ejb.stateful.timeout.wait.seconds on the
   * application server. For embed ejb tests, include this system property in
   * testExecuteEjbEmbed in ts.jte.
   * 
   * Also ensure that test harness timeout is set to a great value. See
   * javatest.timeout.factor property in ts.jte.
   */
  public static final long EXTRA_WAIT_SECONDS = Long.valueOf(
      System.getProperty("test.ejb.stateful.timeout.wait.seconds", "480"));

  void ping();
}
