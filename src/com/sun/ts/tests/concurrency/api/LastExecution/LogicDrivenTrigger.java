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

package com.sun.ts.tests.concurrency.api.LastExecution;

import java.util.*;
import javax.enterprise.concurrent.*;
import com.sun.ts.lib.util.TestUtil;

/**
 * A trigger that driven by test logic. This trigger is used for test the logic
 * of LastExecution, since trigger can not return value to the client, it is
 * also not ensured to be able to visit jndi. We use the execution times to
 * denote if the test runs successfully. If the trigger is triggered 2 times,
 * the test passes, otherwise the test fails.
 */
public class LogicDrivenTrigger implements Trigger {

  private long delta;

  private String testName;

  private boolean moreThanTwice = false;

  private Date startTime;

  private static final long TIME_COMPARE_INACCURACY = 2 * 1000;

  public static final long LASTEXECUTIONGETRUNNINGTIMETEST_SLEEP_TIME = 5
      * 1000;

  public static final int RIGHT_COUNT = 2;

  public static final int WRONG_COUNT = 1;

  public static final String TEST_NAME_LASTEXECUTIONGETIDENTITYNAMETEST = "lastExecutionGetIdentityNameTest";

  public static final String TEST_NAME_LASTEXECUTIONGETRESULTTEST_RUNNABLE = "lastExecutionGetResultTest_runnable";

  public static final String TEST_NAME_LASTEXECUTIONGETRESULTTEST_CALLABLE = "lastExecutionGetResultTest_callable";

  public static final String TEST_NAME_LASTEXECUTIONGETRUNNINGTIMETEST = "lastExecutionGetRunningTimeTest";

  public LogicDrivenTrigger(long delta, String testName) {
    this.delta = delta;
    this.testName = testName;
    this.startTime = new Date();
  }

  private String getErrStr4NotEqual(String testName, Object expected,
      Object real) {
    String result = testName + "failed, ";
    result += "expected " + expected + ",";
    result += "but got " + real;
    return result;
  }

  private boolean validateDateTimeEquals(Date time1, Date time2) {
    long diff = time1.getTime() - time2.getTime();

    if (Math.abs(diff) < TIME_COMPARE_INACCURACY) {
      return true;
    } else {
      return false;
    }
  }

  public Date getNextRunTime(LastExecution lastExecutionInfo,
      Date taskScheduledTime) {
    if (lastExecutionInfo == null) {
      return new Date();
    } else {
      // we do all test logic check here
      if (!moreThanTwice) {
        if (TEST_NAME_LASTEXECUTIONGETIDENTITYNAMETEST.equals(testName)) {
          if (!Client.IDENTITY_NAME_TEST_ID
              .equals(lastExecutionInfo.getIdentityName())) {
            TestUtil.logErr(
                getErrStr4NotEqual(TEST_NAME_LASTEXECUTIONGETIDENTITYNAMETEST,
                    Client.IDENTITY_NAME_TEST_ID,
                    lastExecutionInfo.getIdentityName()));
            return null;
          }

        } else if (TEST_NAME_LASTEXECUTIONGETRESULTTEST_RUNNABLE
            .equals(testName)) {
          if (lastExecutionInfo.getResult() != null) {
            TestUtil.logErr(getErrStr4NotEqual(
                TEST_NAME_LASTEXECUTIONGETRESULTTEST_RUNNABLE, null,
                lastExecutionInfo.getResult()));
            return null;
          }

        } else if (TEST_NAME_LASTEXECUTIONGETRESULTTEST_CALLABLE
            .equals(testName)) {
          if (!Integer.valueOf(1).equals(lastExecutionInfo.getResult())) {
            TestUtil.logErr(getErrStr4NotEqual(
                TEST_NAME_LASTEXECUTIONGETRESULTTEST_CALLABLE, 1,
                lastExecutionInfo.getResult()));
            return null;
          }

        } else if (TEST_NAME_LASTEXECUTIONGETRUNNINGTIMETEST.equals(testName)) {
          if (!validateDateTimeEquals(this.startTime,
              lastExecutionInfo.getScheduledStart())) {
            TestUtil.logErr(
                getErrStr4NotEqual(TEST_NAME_LASTEXECUTIONGETRUNNINGTIMETEST,
                    this.startTime, lastExecutionInfo.getScheduledStart()));
            return null;
          }
          if (lastExecutionInfo.getScheduledStart()
              .getTime() > lastExecutionInfo.getRunStart().getTime()) {
            TestUtil.logErr(TEST_NAME_LASTEXECUTIONGETRUNNINGTIMETEST
                + "failed, getRunStart time should not be earlier than getScheduledStart");
            return null;
          }
          if ((lastExecutionInfo.getRunEnd().getTime()
              - lastExecutionInfo.getRunStart()
                  .getTime()) < LASTEXECUTIONGETRUNNINGTIMETEST_SLEEP_TIME) {
            TestUtil.logErr(TEST_NAME_LASTEXECUTIONGETRUNNINGTIMETEST
                + "failed, the difference between getRunEnd and getRunStart"
                + "is shorter than the real running time");
            return null;
          }
        }
        moreThanTwice = true;
        return new Date(new Date().getTime() + delta);

      } else {
        return null;
      }
    }
  }

  public boolean skipRun(LastExecution lastExecutionInfo,
      Date scheduledRunTime) {
    return false;
  }
}
