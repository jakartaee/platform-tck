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

package com.sun.ts.tests.common.dao;

import java.util.Properties;

/**
 * DAO Exception wrapping back-end specific exceptions.
 */
public class DAOException extends Exception implements java.io.Serializable {

  protected Throwable cause;

  public DAOException(String message) {
    super(message);
    this.cause = null;
  }

  public DAOException(String message, Throwable t) {
    super(message);
    this.cause = t;
  }

  public Throwable getCause() {
    return this.cause;
  }

  public String toString() {
    StringBuffer sb = new StringBuffer();

    sb.append("DAO exception: ");
    sb.append(super.toString());
    if (null != cause) {
      sb.append(" caused by : ");
      sb.append(cause.toString());
    }

    return sb.toString();
  }

}
