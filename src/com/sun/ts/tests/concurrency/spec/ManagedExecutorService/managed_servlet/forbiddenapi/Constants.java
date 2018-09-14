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

package com.sun.ts.tests.concurrency.spec.ManagedExecutorService.managed_servlet.forbiddenapi;

public class Constants {
  private Constants() {

  }

  public static final String SERVLET_TEST_NAME = "test";

  public static final String SERVLET_TEST_URL = "/test";

  public static final String OP_NAME = "opts";

  public static final String OP_AWAITTERMINATION = "OP_AwaitTermination";

  public static final String OP_ISSHUTDOWN = "OP_IsShutdown";

  public static final String OP_ISTERMINATED = "OP_IsTerminated";

  public static final String OP_SHUTDOWN = "OP_Shutdown";

  public static final String OP_SHUTDOWNNOW = "OP_ShutdownNOW";

  public static final String SUCCESSMESSAGE = "Successfull!";

  public static final String FAILMESSAGE = "Fail!";
}
