/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.rmiiiop.ee.marshaltests;

import com.sun.ts.lib.util.*;
import java.rmi.*;
import java.io.*;
import java.util.Properties;
import javax.ejb.*;

public class Timer {
  // Class constants
  private static final long MSEC = 1000;

  private static final String msgHeader = "Total elapsed time in";

  // Class variables
  private long start;

  private long stop;

  private long elapsed;

  private boolean started;

  private int periods;

  private boolean enabled;

  // Constructor methods
  public Timer() {
    init();
  }

  // Private methods
  private void init() {
    start = 0;
    stop = 0;
    elapsed = 0;
    started = false;
    periods = 0;
    enabled = true;
  }

  private void startTimer(boolean resumed) {
    if (enabled && !started) {
      started = true;
      if (!resumed) {
        periods = 0;
        elapsed = 0;
      }
      start = System.currentTimeMillis();
    }
  }

  // Public methods
  public void start() {
    startTimer(false);
  }

  public void stop() {
    if (enabled && started) {
      stop = System.currentTimeMillis();
      elapsed += stop - start;
      started = false;
      periods++;
    }
  }

  public void resume() {
    startTimer(true);
  }

  public void reset() {
    if (enabled)
      init();
  }

  public void enable() {
    enabled = true;
  }

  public void disable() {
    enabled = false;
  }

  public boolean isEnabled() {
    return enabled;
  }

  // Get elapsed times
  public long getRunningElapsed() {
    return enabled ? (System.currentTimeMillis() - start) : 0;
  }

  public long getElapsed() {
    return enabled ? elapsed : 0;
  }

  public long getMilliseconds() {
    return enabled ? elapsed : 0;
  }

  public long getSeconds() {
    return enabled ? elapsed / MSEC : 0;
  }

  public double getTime() {
    return enabled ? elapsed / (double) MSEC : 0;
  }

  public int getPeriods() {
    return enabled ? periods : 0;
  }

  // Get number of days
  public int getDays() {
    if (!enabled)
      return (0);
    long t = getSeconds();
    if (t >= (60 * 60 * 24)) {
      return ((int) (t / (60 * 60 * 24)));
    }
    return (0);
  }

  // Get remaining hours
  public int getHours() {
    if (!enabled)
      return (0);
    long t = getSeconds();
    t = t % (60 * 60 * 24);
    if (t >= (60 * 60)) {
      return ((int) (t / (60 * 60)));
    }
    return (0);
  }

  // Get remaining minutes
  public int getMins() {
    if (!enabled)
      return (0);
    long t = getSeconds();
    t = t % (60 * 60 * 24);
    t = t % (60 * 60);
    if (t >= 60) {
      return ((int) (t / 60));
    }
    return (0);
  }

  // Get remaining seconds
  public int getSecs() {
    if (!enabled)
      return (0);
    long t = getSeconds();
    t = t % (60 * 60 * 24);
    t = t % (60 * 60);
    t = t % 60;
    if (t > 0) {
      return ((int) t);
    }
    return (0);
  }

  // Get remaining milliseconds
  public int getMillis() {
    if (!enabled)
      return (0);
    long t = getMilliseconds();
    t = t % MSEC;
    if (t > 0) {
      return ((int) t);
    }
    return (0);
  }

  // Get String Formatted Time
  public String getStringFormattedTime() {
    int numofdays = getDays();
    int numofhours = getHours();
    int numofmins = getMins();
    int numofsecs = getSecs();
    int numofmillis = getMillis();
    String s = null;

    if (!enabled)
      return ("Timer Not Enabled");
    if (numofdays > 0)
      s = "" + numofdays + " days " + numofhours + " hrs " + numofmins
          + " mins " + numofsecs + " secs " + numofmillis + " millisecs";
    else if (numofhours > 0)
      s = "" + numofhours + " hrs " + numofmins + " mins " + numofsecs
          + " secs " + numofmillis + " millisecs";
    else if (numofmins > 0)
      s = "" + numofmins + " mins " + numofsecs + " secs " + numofmillis
          + " millsecs";
    else if (numofsecs > 0)
      s = "" + numofsecs + " secs " + numofmillis + " millisecs";
    else if (numofmillis > 0)
      s = "" + numofmillis + " millisecs";
    return (s);
  }

  public String GetTimeLeftToRunString(long t) {
    int numofdays = 0;
    int numofhours = 0;
    int numofmins = 0;
    int numofsecs = 0;
    String s = null;

    if (t > (60 * 60 * 24)) {
      numofdays = (int) (t / (60 * 60 * 24));
      t = t % (60 * 60 * 24);
    }
    if (t > (60 * 60)) {
      numofhours = (int) (t / (60 * 60));
      t = t % (60 * 60);
    }
    if (t > 60) {
      numofmins = (int) (t / 60);
      t = t % 60;
    }
    if (t > 0) {
      numofsecs = (int) t;
    }
    if (numofdays > 0)
      s = "" + numofdays + "days:" + numofhours + "hrs:" + numofmins + "mins:"
          + numofsecs + "secs";
    else if (numofhours > 0)
      s = "" + numofhours + "hrs:" + numofmins + "mins:" + numofsecs + "secs";
    else if (numofmins > 0)
      s = "" + numofmins + "mins:" + numofsecs + "secs";
    else if (numofsecs > 0)
      s = "" + numofsecs + "secs";
    return (s);
  }

  // Print times
  public void print() {
    printMilliseconds();
  }

  public void printMilliseconds() {
    MyUtil.logMsg(msgHeader + " milliseconds: " + getMilliseconds());
  }

  public void printSeconds() {
    MyUtil.logMsg(msgHeader + " seconds: " + getSeconds());
  }

  public void printTime() {
    MyUtil.logMsg(msgHeader + " seconds: " + getTime());
  }
}
