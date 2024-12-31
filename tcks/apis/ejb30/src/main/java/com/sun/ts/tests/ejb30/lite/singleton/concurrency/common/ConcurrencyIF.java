/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.lite.singleton.concurrency.common;

public interface ConcurrencyIF {

  public void addToLinkedList(Integer i);

  public int getLinkedListSizeAndClear();

  public long getAndResetLockedSum();

  public void addLocked(int num);

  public long getAndResetUnlockedSum();

  public void addUnlocked(int num);

  public long getAndResetLockedSumFromInterceptor(String interceptorName);

  public void addLockedFromInterceptor(String interceptorName, int num);

  public long getAndResetUnlockedSumFromInterceptor(String interceptorName);

  public void addUnlockedFromInterceptor(String interceptorName, int num);

}
