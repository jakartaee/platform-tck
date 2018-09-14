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

public interface InheritanceIF {
  // public void checkedAppException();
  public void uncheckedAppException1();

  public void uncheckedAppException2() throws Exception1;

  public void uncheckedAppException3() throws RuntimeException;

  // declare to throw Exception1, but actually throw Exception4
  public void uncheckedSystemException4() throws Exception1;

  // declare to throw Exception1, but actually throw Exception5
  public void uncheckedSystemException5() throws Exception1;

  // declare to throw Exception5, but actually throw Exception6
  public void uncheckedAppException6() throws Exception5;

  // declare to throw Exception5, but actually throw Exception7
  public void uncheckedAppException7() throws Exception5;

  // public void checkedRollbackAppException();
  // public void uncheckedRollbackAppException();
  //
  // public void atCheckedAppException();
  // public void atUncheckedAppException();
  // public void atCheckedRollbackAppException();
  // public void atUncheckedRollbackAppException();

}
