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

package com.sun.ts.tests.concurrency.spec.ContextService.tx;

import java.io.Serializable;
import java.sql.Connection;

public interface TestWorkInterface extends Runnable, Serializable {
  public void doSomeWork();

  public String getResult();

  public void setConnection(Connection conn);

  public void setSQLTemplate(String sqlTemplate);

  public void needBeginTx(boolean beginTx);

  public void needCommit(boolean commit);

  public void needRollback(boolean rollback);

  public void setUserName(String name);

  public void setPassword(String pass);
}
