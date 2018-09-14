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

package com.sun.ts.tests.ejb30.tx.common.session.cm;

import com.sun.ts.tests.ejb30.common.helper.TestFailedException;

public interface LocalTxIF {
  public void localMandatoryTest();

  public String systemExceptionTest() throws TestFailedException; // this method
                                                                  // is also in
                                                                  // TxIF

  public void neverTest();

  void localRequiresNewTest();

  void localRequiresNewRemoveTest();

  String localSupportsTest() throws TestFailedException;

  String localIllegalGetSetRollbackOnlyNeverTest() throws TestFailedException;

  String localIllegalGetSetRollbackOnlyNotSupportedTest()
      throws TestFailedException;

  void requiresNewNoop() throws TestFailedException;
}
