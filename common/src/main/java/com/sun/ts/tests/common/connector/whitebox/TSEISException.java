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

/*
 * @(#)TSEISException.java	1.0 06/06/02
 */
package com.sun.ts.tests.common.connector.whitebox;

/**
 * An Exception class for the internal use of TSeis Data management system.
 *
 * @version 1.0, 06/06/02
 * @author Binod P.G
 */
public class TSEISException extends Exception {

  /**
   * Creates exception with the message.
   *
   * @param message
   *          Error message
   */
  public TSEISException(String message) {
    super(message);
  }
}
