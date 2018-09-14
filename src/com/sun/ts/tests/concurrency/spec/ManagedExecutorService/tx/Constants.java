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

package com.sun.ts.tests.concurrency.spec.ManagedExecutorService.tx;

final public class Constants {
  private Constants() {
  };

  public static final String UT_JNDI_NAME = "java:comp/UserTransaction";

  public static final String CONTEXT_PATH = "/concurrency_spec_managedExecutorService_tx_web";

  public static final String DS_JNDI_NAME = "jdbc/DB1";

  public static final String TX_SERVLET_NAME = "tx";

  public static final String TX_SERVLET_URI = "/tx";

  public static final String PARAM_COMMIT = "isCommit";

  public static final String PARAM_VALUE_CANCEL = "cancel";

  public static final String DEFAULT_PTABLE = "concurrencetable";

  public static final String TABLE_P = "concurrencetable";

  public static final String USERNAME = "user1";

  public static final String PASSWORD = "password1";

  public static final String SQL_TEMPLATE = "Dbschema_Concur_Insert";
}
