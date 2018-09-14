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

package com.sun.ts.tests.concurrency.api.common;

import java.util.*;
import javax.enterprise.concurrent.*;

public class CommonTriggers {

  /**
   * A trigger that only run once.
   */
  public static class OnceTrigger implements Trigger {
    public Date getNextRunTime(LastExecution lastExecutionInfo,
        Date taskScheduledTime) {
      if (lastExecutionInfo != null) {
        return null;
      }
      return new Date();
    }

    public boolean skipRun(LastExecution lastExecutionInfo,
        Date scheduledRunTime) {
      return false;
    }
  }

  /**
   * A trigger that will skip.
   */
  public static class OnceTriggerDelaySkip implements Trigger {

    public OnceTriggerDelaySkip(long argDelay) {
      delay = argDelay;
    }

    private long delay;

    public Date getNextRunTime(LastExecution lastExecutionInfo,
        Date taskScheduledTime) {
      if (lastExecutionInfo != null) {
        return null;
      }
      return new Date(new Date().getTime() + delay);
    }

    public boolean skipRun(LastExecution lastExecutionInfo,
        Date scheduledRunTime) {
      return true;
    }
  }

  /**
   * A fixed-rate trigger
   */
  public static class TriggerFixedRate implements Trigger {
    private Date startTime;

    private long delta;

    private int executionCount = 0;

    private static final int executionCountLimit = 10;

    public TriggerFixedRate(Date startTime, long delta) {
      this.startTime = startTime;
      this.delta = delta;
    }

    public Date getNextRunTime(LastExecution lastExecutionInfo,
        Date taskScheduledTime) {
      executionCount++;
      if (executionCount > executionCountLimit) {
        return null;
      }

      if (lastExecutionInfo == null) {
        return new Date(startTime.getTime() + delta);
      }
      return new Date(lastExecutionInfo.getScheduledStart().getTime() + delta);
    }

    public boolean skipRun(LastExecution lastExecutionInfo,
        Date scheduledRunTime) {
      return false;
    }
  }

}
