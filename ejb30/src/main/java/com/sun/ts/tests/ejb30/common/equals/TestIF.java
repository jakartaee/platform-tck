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

package com.sun.ts.tests.ejb30.common.equals;

import com.sun.ts.tests.ejb30.common.helper.TestFailedException;

public interface TestIF {
  public static final boolean LOG_IF_OK = true;

  public void remove();

  public void selfEquals() throws TestFailedException;

  // this method is only for stateless beans
  public void otherEquals() throws TestFailedException;

  // this method is only for stateful beans
  public void otherNotEquals() throws TestFailedException;

  public void differentInterfaceNotEqual() throws TestFailedException;

  public void selfEqualsLookup() throws TestFailedException;

  // this method is only for stateless beans
  public void otherEqualsLookup() throws TestFailedException;

  // this method is only for stateful beans
  public void otherNotEqualsLookup() throws TestFailedException;

  public void differentInterfaceNotEqualLookup() throws TestFailedException;

}
