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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.interceptor.Interceptors;

abstract public class SessionSyncBeanBase implements SessionSyncIF {

  private List<String> history = new ArrayList<String>();

  /*
   * (non-Javadoc)
   * 
   * @see javax.ejb.SessionSynchronization#afterBegin()
   */
  public void afterBegin() {
    // This should be the very first entry point of any business method
    // invocation. So clear all items in history from previous invocations.
    history.clear();
    history.add(afterBegin);
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.ejb.SessionSynchronization#afterCompletion(boolean)
   */
  public void afterCompletion(boolean arg0) {
    history.add(afterCompletion);
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.ejb.SessionSynchronization#beforeCompletion()
   */
  public void beforeCompletion() {
    history.add(beforeCompletion);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.sun.ts.tests.ejb30.lite.tx.cm.stateful.sessionsync.SessionSyncIF#
   * getHistory()
   */
  @Interceptors(Interceptor1.class)
  public List<String> getHistory() {
    history.add(getHistory);
    return Collections.unmodifiableList(history);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.sun.ts.tests.ejb30.lite.tx.cm.stateful.sessionsync.SessionSyncIF#
   * resetHistory()
   */
  public void resetHistory() {
    history.clear();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.sun.ts.tests.ejb30.lite.tx.cm.stateful.sessionsync.SessionSyncIF#
   * addToHistory(java.lang.String)
   */
  public void addToHistory(String s) {
    history.add(s);
  }

}
