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
package com.sun.ts.tests.ejb30.common.statussingleton;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Singleton;

@Singleton
public class StatusSingletonBean {
  private Map<String, String> results = new HashMap<String, String>();

  public void addResult(int key, int sum) {
    results.put(String.valueOf(key), String.valueOf(sum));
  }

  public void addResult(String key, String val) {
    results.put(key, val);
  }

  public void removeResult(Integer key) {
    results.remove(String.valueOf(key));
  }

  public void removeResult(String key) {
    results.remove(key);
  }

  // IMPORTANT:
  // The client should wait and poll for the result stored in this class.
  // This method should not hold the lock and wait.
  public Integer getAndResetResult(Integer key) {
    final String sKey = String.valueOf(key);
    final String result = results.get(sKey);
    results.remove(sKey);
    return Integer.valueOf(result);
  }

  public String getAndResetResult(String key) {
    final String result = results.get(key);
    results.remove(key);
    return result;
  }

  public boolean isResultAvailable(Integer key) {
    return results.containsKey(String.valueOf(key));
  }

  public boolean isResultAvailable(String key) {
    return results.containsKey(key);
  }
}
