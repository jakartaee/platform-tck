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

package com.sun.ts.tests.ejb30.bb.localaccess.common;

import static com.sun.ts.tests.ejb30.bb.localaccess.common.Constants.SERVER_MSG;

import com.sun.ts.tests.ejb30.common.appexception.UncheckedAppException;
import com.sun.ts.tests.ejb30.common.calc.CalculatorException;

public class CommonBase implements CommonIF {
  public void passByReferenceTest(String[] args) {
    args[0] = SERVER_MSG;
  }

  public void exceptionTest() throws CalculatorException {
    throw new CalculatorException("Application exception from bean.");
  }

  public void runtimeExceptionTest() {
    throw new UncheckedAppException("UncheckedAppException from bean.");
  }

  public void remove() {
  }
}
