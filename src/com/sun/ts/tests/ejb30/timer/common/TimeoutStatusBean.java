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
package com.sun.ts.tests.ejb30.timer.common;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Singleton;
import javax.interceptor.ExcludeDefaultInterceptors;

@Singleton
@ExcludeDefaultInterceptors
public class TimeoutStatusBean {
  // to limit the number of records from some calendar-based recurring timers.
  private final static int maxNumOfRecords = 1000;

  private Map<String, ArrayList<String>> timeoutRecordsMap = new HashMap<String, ArrayList<String>>();

  private Map<String, Boolean> timeoutStatusMap = new HashMap<String, Boolean>();

  // If the status is explicitly set to false, the test must fail.
  public Boolean getStatus(String testName) {
    return timeoutStatusMap.get(testName);
  }

  public void removeStatus(String testName) {
    timeoutStatusMap.remove(testName);
  }

  public void setStatus(String testName, boolean b) {
    timeoutStatusMap.put(testName, b);
    Helper.getLogger()
        .fine("Set timeout status to " + b + ", for test " + testName);
  }

  public void addRecord(String testName, String record) {
    ArrayList<String> previousRecords = timeoutRecordsMap.get(testName);
    if (previousRecords == null) {
      previousRecords = new ArrayList<String>();
      timeoutRecordsMap.put(testName, previousRecords);
    }
    if (previousRecords.size() < maxNumOfRecords) {
      previousRecords.add(record);
    }
    Helper.getLogger()
        .fine("Added timeout record for test " + testName + ": " + record);
  }

  public List<String> getRecords(String testName) {
    ArrayList<String> rec = timeoutRecordsMap.get(testName);
    if (rec == null) {
      return Collections.emptyList();
    }
    return Collections.unmodifiableList(rec);
  }

  public boolean containsRecords(String testName) {
    return timeoutRecordsMap.containsKey(testName);
  }

  public void clearRecords() {
    timeoutRecordsMap.clear();
  }

  public void removeRecords(String testName) {
    timeoutRecordsMap.remove(testName);
  }

  public int recordsSize() {
    return timeoutRecordsMap.size();
  }
}
