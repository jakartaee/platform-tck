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

import java.io.Serializable;
import javax.ejb.ApplicationException;

@ApplicationException
public class AtCheckedAppException extends Exception implements Serializable {

  public AtCheckedAppException() {
    super();
  }

  /**
   * @param message
   */
  public AtCheckedAppException(String message) {
    super(message);
  }

  /**
   * @param message
   * @param cause
   */
  public AtCheckedAppException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param cause
   */
  public AtCheckedAppException(Throwable cause) {
    super(cause);
  }

}
