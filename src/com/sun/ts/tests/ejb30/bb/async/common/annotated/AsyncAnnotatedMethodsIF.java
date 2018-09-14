/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.bb.async.common.annotated;

import java.util.concurrent.Future;

import com.sun.ts.tests.ejb30.common.helper.TestFailedException;

/**
 * This local interface contains 2 methods that are sync methods.
 */
public interface AsyncAnnotatedMethodsIF extends AsyncAnnotatedMethodsCommonIF {
  public Future<Integer> addSyncThrowException(int a, int b)
      throws TestFailedException;

  public Future<Integer> addSyncReturn(int a, int b, long waitMillis);
}
