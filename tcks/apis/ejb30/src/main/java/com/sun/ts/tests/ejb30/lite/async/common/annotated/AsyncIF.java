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

/*
 * $Id$
 */
package com.sun.ts.tests.ejb30.lite.async.common.annotated;

import java.util.List;
import java.util.concurrent.Future;

import org.apache.commons.lang3.time.DateUtils;

import com.sun.ts.tests.ejb30.common.calc.CalculatorException;

public interface AsyncIF {
  public static final long MAX_WAIT_MILLIS = DateUtils.MILLIS_PER_MINUTE;

  public static final long POLL_INTERVAL_MILLIS = DateUtils.MILLIS_PER_SECOND;

  public static final String CANCEL_IN_BEAN_KEY = "CANCEL_IN_BEAN_KEY";

  public static final String CANCEL_IN_BEAN_VAL = "CANCEL_IN_BEAN_VAL";

  public static final String CANCEL_IN_CLIENT_KEY = "CANCEL_IN_CLIENT_KEY";

  public static final String CANCEL_IN_CLIENT_VAL = "CANCEL_IN_CLIENT_VAL";

  public void addAway(int a, int b, int key);

  public void voidRuntimeException(Integer key) throws RuntimeException;

  public Future<Integer> futureError() throws AssertionError;

  public Future<Integer> futureRuntimeException() throws RuntimeException;

  public Future<Integer> futureException() throws CalculatorException;

  public Future<List<String>> futureValueList(List<String> vals);

  public Future<Integer> addReturn(int a, int b);

  public Future<Integer> identityHashCode();

  public Future<Boolean> isErrorOccurredInInstance();

  public Future<Boolean> cancelMayInterruptIfRunning();

  public void passByValueOrReference(String[] ss);

  public Future<String> passByValueOrReferenceAsync(String[] ss);
}
