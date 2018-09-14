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
package com.sun.ts.tests.ejb30.lite.appexception.common.inheritance;

public class InheritanceBeanBase implements InheritanceIF {

  public void uncheckedAppException1() {
    throw new Exception1(
        "Client should get Exception1 as an ApplicationException");
  }

  public void uncheckedAppException2() {
    throw new Exception2(
        "Client should get Exception2 as an ApplicationException");
  }

  public void uncheckedAppException3() throws RuntimeException {
    throw new Exception3(
        "Client should get Exception3 as an ApplicationException");
  }

  public void uncheckedSystemException4() throws Exception1 {
    throw new Exception4("Exception4 is a system exception");
  }

  public void uncheckedSystemException5() throws Exception1 {
    throw new Exception5("Exception5 is a system exception");
  }

  public void uncheckedAppException6() throws Exception5 {
    throw new Exception6(
        "Client should get Exception6 as an ApplicationException");
  }

  public void uncheckedAppException7() throws Exception5 {
    throw new Exception7(
        "Client should get Exception7 as an ApplicationException");
  }
}
