/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.common.appexception;

import javax.annotation.Resource;
import javax.ejb.SessionContext;

abstract public class AppExceptionBeanBase
    implements AppExceptionIF, AppExceptionLocalIF {
  @Resource(description = "just to see if descripton field works or not.  It should map to <resource-env-ref>/<description> xml element.")
  private SessionContext sessionContext;

  public void checkedAppException() throws CheckedAppException {
    throw new CheckedAppException();
  }

  public void uncheckedAppException() throws UncheckedAppException {
    throw new UncheckedAppException();
  }

  public void uncheckedRollbackAppException()
      throws UncheckedRollbackAppException {
    throw new UncheckedRollbackAppException();
  }

  public void checkedRollbackAppException() throws CheckedRollbackAppException {
    throw new CheckedRollbackAppException();
  }

  //////////////////////////////////////////////////////////////////////
  // for /at/ and /override/ only
  //////////////////////////////////////////////////////////////////////
  public void atUncheckedAppException() throws AtUncheckedAppException {
    throw new AtUncheckedAppException();
  }

  public void atCheckedAppException() throws AtCheckedAppException {
    throw new AtCheckedAppException();
  }

  public void atUncheckedRollbackAppException()
      throws AtUncheckedRollbackAppException {
    throw new AtUncheckedRollbackAppException();
  }

  public void atCheckedRollbackAppException()
      throws AtCheckedRollbackAppException {
    throw new AtCheckedRollbackAppException();
  }
}
