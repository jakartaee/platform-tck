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

/*
 * $Id$
 */

package com.sun.ts.tests.jms.ee20.ra.common.messaging;

public interface Constants {
  public static final String PASSED = "PASSED";

  public static final String FAILED = "FAILED";

  public static final String TEST_NAME_KEY = "COM_SUN_JMS_TESTNAME";

  public static final String TEST_NUMBER_KEY = "TestCaseNum";

  public static final String FINDER_TEST_NAME_KEY = "testName";

  public static final long MESSAGE_TIME_TO_LIVE = 30 * 1000;

  /**
   * The two properties ar eno longer used. Number of seconds to sleep after
   * calling QueueConnection.createQueueSession(). This is currently used in
   * StatusReporter. Note that the QueueSession is cached in StatusReporter, so
   * the sleep occurs only at the first time. Without the sleep time, the first
   * test sending back result will fail with errors: Message resent too many
   * times; sending it to DLQ...
   */
  public static final int SLEEP_AFTER_CONNECT_SECONDS = 3;

  public static final int CLIENT_SLEEP_AFTER_SEND_SECONDS = 3;
}
