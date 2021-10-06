/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.lite.tx.cm.stateful.sessionsync;

import java.util.List;

public interface SessionSyncIF {
  public static final String afterBegin = "afterBegin";

  public static final String beforeCompletion = "beforeCompletion";

  public static final String afterCompletion = "afterCompletion";

  public static final String getHistory = "getHistory";

  public static final String aroundInvoke1 = "aroundInvoke1";

  public static final String aroundInvoke2 = "aroundInvoke2";

  /**
   * Gets the sequence of invocations of SessionSynchronization callbacks
   * 
   * @return a read-only copy of the callback invocation history
   */
  public List<String> getHistory();

  /**
   * Adds an item to the history of SessionSynchronization callbacks.
   * 
   * @param s
   */
  public void addToHistory(String s);

}
