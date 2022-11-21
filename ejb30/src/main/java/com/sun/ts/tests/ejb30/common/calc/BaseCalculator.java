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

/**
 * An implementation class of <code>Calculator</code> interface. It forwards to
 * an instance of <code>NoInterfaceCalculator</code> for all operations.
 * 
 * @author Cheng Fang
 */
public class BaseCalculator implements Calculator {

  private NoInterfaceCalculator calc = new NoInterfaceCalculator();

  /** Creates a new instance of BaseCalculator */
  public BaseCalculator() {
  }

  public int subtract(int a, int b) {
    return calc.subtract(a, b);
  }

  public int add(int a, int b) {
    return calc.add(a, b);
  }

  /*
   * Tests that the methods in business interface may throw arbitrary
   * application exceptions.
   * 
   * @see com.sun.ts.tests.ejb30.common.calc.Calculator#throwIt()
   */
  public void throwIt() throws CalculatorException {
    calc.throwIt();
  }
}
