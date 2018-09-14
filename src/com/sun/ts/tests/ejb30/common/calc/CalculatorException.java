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

package com.sun.ts.tests.ejb30.common.calc;

import java.io.Serializable;

/**
 * An application exception for calculator example. It can be used to test that
 * the methods of the business interface may declare arbitrary application
 * exceptions.
 * 
 * @author Cheng Fang
 */
public class CalculatorException extends Exception implements Serializable {

  public CalculatorException() {
    super();
  }

  /**
   * @param message
   */
  public CalculatorException(String message) {
    super(message);
  }

  /**
   * @param message
   * @param cause
   */
  public CalculatorException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param cause
   */
  public CalculatorException(Throwable cause) {
    super(cause);
  }

}
