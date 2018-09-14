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

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import javax.ejb.EJB;

public class ClientBase extends EJBLiteClientBase {
  protected static final long WAIT_FOR_TIMEOUT_STATUS = 60000; // millis

  protected static final long WAIT_FOR_NO_TIMEOUT_STATUS = 60000; // millis

  protected static final int NUM_OF_RECURRING_RECORDS = 3;

  protected static final long POLL_INTERVAL = 1000; // millis

  protected static final long DEFAULT_DURATION = 100L; // time out after these
                                                       // millisec

  protected static final long DEFAULT_INTERVAL = 100L;

  @EJB(beanName = "TimeoutStatusBean")
  protected TimeoutStatusBean statusSingleton;

  @Override
  public void setup(String[] args, Properties p) {
    super.setup(args, p);
    if (needToRemoveStatusAndRecords()) {
      // NOTE: this method only clears records for the timer whose name is
      // the same as the current test name. If the test method creates
      // timers with different names, these timer records will not be cleared.
      // In that case, individual test methods need to take care of it.
      removeStatusAndRecords();
    }
  }

  protected boolean needToRemoveStatusAndRecords() {
    return true;
  }

  protected void passIfNoTimeout(long... waitMillis) {
    passIfNoTimeout(getTestName(), waitMillis);
  }

  protected void passIfNoTimeout(String timerName, long... waitMillis) {
    long waitFor = WAIT_FOR_NO_TIMEOUT_STATUS;
    if (waitMillis.length != 0) {
      waitFor = waitMillis[0];
    }
    Boolean status = pollTimeoutStatus(timerName, waitFor, false);
    assertEquals("Timeout status must not be set.", null, status);
    assertEquals("Timeout result must not be set.", 0,
        statusSingleton.getRecords(timerName).size());
  }

  protected void passIfTimeout(long... waitMillis) {
    passIfTimeout(getTestName(), waitMillis);
  }

  // Usually timerName is the same as getTestName(). This method is used
  // when they are different.
  protected void passIfTimeout(String timerName, long... waitMillis) {
    long waitFor = WAIT_FOR_TIMEOUT_STATUS;
    if (waitMillis.length != 0) {
      waitFor = waitMillis[0];
    }
    Boolean status = pollTimeoutStatus(timerName, waitFor, false);
    appendReason(statusSingleton.getRecords(timerName));
    assertEquals("Check timeout status", Boolean.TRUE, status);
  }

  protected void passIfTimeoutOnce(long... waitMillis) {
    passIfTimeoutOnce(getTestName(), waitMillis);
  }

  protected void passIfTimeoutOnce(String timerName, long... waitMillis) {
    passIfTimeout(timerName, waitMillis);
    // wait again to see if there is any subsequent expiration.
    long waitFor = (waitMillis.length != 0) ? waitMillis[0]
        : WAIT_FOR_TIMEOUT_STATUS;
    TestUtil.sleepMsec((int) waitFor);
    assertEquals("Check num of expirations", 1,
        statusSingleton.getRecords(timerName).size());
  }

  // for recurring timers with interval
  protected void passIfRecurringTimeout(long... waitMillis) {
    passIfRecurringTimeout(getTestName(), waitMillis);
  }

  // for recurring timers with interval
  protected void passIfRecurringTimeout(String timerName, long... waitMillis) {
    long waitFor = WAIT_FOR_TIMEOUT_STATUS * NUM_OF_RECURRING_RECORDS;
    if (waitMillis.length != 0) {
      waitFor = waitMillis[0];
    }
    Boolean status = pollTimeoutStatus(timerName, waitFor, true);
    List<String> records = statusSingleton.getRecords(timerName);
    appendReason(records);
    assertEquals("Check timeout status", Boolean.TRUE, status);
    assertGreaterThan("Check num of timeout records", records.size(),
        NUM_OF_RECURRING_RECORDS - 1);
  }

  private Boolean pollTimeoutStatus(String timerName, long waitMillis,
      boolean recurringTimer) {
    long end = System.currentTimeMillis() + waitMillis;
    Boolean status = null;
    List<String> records = Collections.emptyList();
    if (recurringTimer) {
      while (records.size() < NUM_OF_RECURRING_RECORDS
          && System.currentTimeMillis() < end) {
        TestUtil.sleepMsec((int) POLL_INTERVAL);
        records = statusSingleton.getRecords(timerName);
      }
      status = statusSingleton.getStatus(timerName);
      if (records.size() < NUM_OF_RECURRING_RECORDS) {
        Helper.getLogger()
            .fine("Polling recurring Timeout records timed out after "
                + waitMillis + " millis for timerName " + timerName);
      }
    } else {
      while (status == null && System.currentTimeMillis() < end) {
        TestUtil.sleepMsec((int) POLL_INTERVAL);
        status = statusSingleton.getStatus(timerName);
      }
      if (status == null) {
        Helper.getLogger().fine("Polling Timeout status timed out after "
            + waitMillis + " millis for timerName " + timerName);
      }
    }
    return status;
  }

  protected void removeStatusAndRecords(String... timerName) {
    String tn = (timerName.length == 0) ? getTestName() : timerName[0];
    statusSingleton.removeStatus(tn);
    statusSingleton.removeRecords(tn);
  }
}
