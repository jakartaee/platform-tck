/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

import java.io.Serializable;
import jakarta.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class AtCheckedRollbackAppException extends Exception
    implements Serializable {

  public AtCheckedRollbackAppException() {
    super();
  }

  /**
   * @param message
   */
  public AtCheckedRollbackAppException(String message) {
    super(message);
  }

  /**
   * @param message
   * @param cause
   */
  public AtCheckedRollbackAppException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param cause
   */
  public AtCheckedRollbackAppException(Throwable cause) {
    super(cause);
  }

}
