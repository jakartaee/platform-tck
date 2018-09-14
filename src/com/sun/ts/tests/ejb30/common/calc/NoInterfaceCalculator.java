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
 * A local calculator class without implementing any interface. This is to test
 * a bean class without implementing any interface must have its business
 * interface generated. When @Local is applied to such interfaceless bean
 * classes, the generated business interface must have local accessibility.
 * 
 * @author Cheng Fang
 */
public class NoInterfaceCalculator {

  /** Creates a new instance of NoInterfaceCalculator */
  public NoInterfaceCalculator() {
  }

  public int subtract(int a, int b) {
    return a - b;
  }

  public int add(int a, int b) {
    return a + b;
  }

  /*
   * Tests that the methods in business interface may throw arbitrary
   * application exceptions.
   */
  public void throwIt() throws CalculatorException {
    throw new CalculatorException();
  }
}
