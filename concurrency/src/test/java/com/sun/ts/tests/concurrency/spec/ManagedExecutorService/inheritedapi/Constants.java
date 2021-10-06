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

package com.sun.ts.tests.concurrency.spec.ManagedExecutorService.inheritedapi;

final public class Constants {
  private Constants() {
  };

  public static final String CONTEXT_PATH = "/concurrency_spec_managedExecutorService_inheritedapi_web";

  public static final String HOST_KEY = "webServerHost";

  public static final String PORT_KEY = "webServerPort";

  public static final String COMMON_SERVLET_NAME = "common";

  public static final String COMMON_SERVLET_URI = "/common";

  public static final String TASK_RAN = "task ran";

  public static final int MAX_WAIT_TIME = 3; // (s)
}
