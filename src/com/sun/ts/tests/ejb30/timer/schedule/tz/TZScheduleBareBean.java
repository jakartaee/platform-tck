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
package com.sun.ts.tests.ejb30.timer.schedule.tz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;

import javax.ejb.ScheduleExpression;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.timer.common.TimerBeanBaseWithoutTimeOutMethod;
import com.sun.ts.tests.ejb30.timer.common.TimerUtil;

@Stateless
public class TZScheduleBareBean extends TimerBeanBaseWithoutTimeOutMethod {
  private static final String ZONE_TAB = "zone.tab";

  protected static final String DEFAULT_TZ = "DEFAULT_TZ";

  protected static final String PROGRAMMATIC_TIMER_DEFAULT_TZ = "PROGRAMMATIC_TIMER_DEFAULT_TZ";

  protected static final String AMERICA_ARGENTINA_SAN_LUIS = "  America/Argentina/San_Luis  ";

  protected static final String AMERICA_ARGENTINA_USHUAIA = "America/Argentina/Ushuaia";

  protected static final String ASIA_SHANGHAI = "Asia/Shanghai";

  protected List<String> zoneTab;

  @Timeout
  protected void year5000() {
    throw new IllegalStateException("We will not see this in our life.");
  }

  public String defaultTZ() {
    TimerConfig config = new TimerConfig(PROGRAMMATIC_TIMER_DEFAULT_TZ, false);
    ScheduleExpression exp = new ScheduleExpression().year(5000);
    timerService.createCalendarTimer(exp, config);
    return verifyTZ(DEFAULT_TZ, null) + TestUtil.NEW_LINE
        + verifyTZ(PROGRAMMATIC_TIMER_DEFAULT_TZ, null);
  }

  public String shanghaiAndArgentinaTZ() {
    return verifyTZ(ASIA_SHANGHAI, ASIA_SHANGHAI) + TestUtil.NEW_LINE
        + verifyTZ(AMERICA_ARGENTINA_SAN_LUIS, AMERICA_ARGENTINA_SAN_LUIS)
        + TestUtil.NEW_LINE
        + verifyTZ(AMERICA_ARGENTINA_USHUAIA, AMERICA_ARGENTINA_USHUAIA);
  }

  public String allTZ() {
    StringBuilder sb = new StringBuilder();
    List<Timer> timers = new ArrayList<Timer>();
    TimerConfig config = new TimerConfig();
    config.setPersistent(false);
    ScheduleExpression exp = new ScheduleExpression().year(5000);

    for (String z : zoneTab) {
      exp = exp.timezone(z);
      config.setInfo(z);
      timers.add(timerService.createCalendarTimer(exp, config));
    }
    for (String z : zoneTab) {
      verifyTZ(z, z, sb);
      sb.append(TestUtil.NEW_LINE);
    }
    return sb.toString();
  }

  public Timer expireInLaterTZ() {
    TimerConfig config = new TimerConfig("expireInAnotherTZ", false);
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.SECOND, 1);
    ScheduleExpression exp = TimerUtil.getPreciseScheduleExpression(cal);
    exp.timezone(getLaterTZ());
    Timer t = timerService.createCalendarTimer(exp, config);
    Helper.getLogger().fine("Created a timer with schedule " + exp);
    return t;
  }

  protected String verifyTZ(String timerName, String expected,
      StringBuilder... sbs) {
    StringBuilder sb = (sbs.length == 0) ? new StringBuilder() : sbs[0];
    Timer t = findTimer(timerName);
    ScheduleExpression exp = t.getSchedule();
    sb.append("Check TZ in schedule: ").append(TimerUtil.toString(exp));
    String expectedTrimmedUp = (expected == null) ? expected
        : expected.trim().toUpperCase();
    String actualTrimmedUp = (exp.getTimezone() == null) ? null
        : exp.getTimezone().trim().toUpperCase();
    Helper.assertEquals(" ", expectedTrimmedUp, actualTrimmedUp, sb);
    return sb.toString();
  }

  protected void initZoneTab() {
    zoneTab = new ArrayList<String>();
    InputStream is = getClass().getResourceAsStream(ZONE_TAB);
    BufferedReader br = new BufferedReader(new InputStreamReader(is));
    String line = null;
    try {
      while ((line = br.readLine()) != null) {
        line = line.trim();
        if (line.length() >= 3) {// the valid form is "x/y"
          zoneTab.add(line);
        }
      }
    } catch (IOException e) {
      Helper.getLogger().log(Level.WARNING, "Failed to read " + ZONE_TAB, e);
    } finally {
      if (is != null) {
        try {
          is.close();
        } catch (IOException e2) {
        }
      }
    }
    if (zoneTab.size() == 0) {
      Helper.getLogger().warning("0 entries read from " + ZONE_TAB);
      zoneTab = null;
    }
  }

  /**
   * Looks for a TZ that is later than the default TZ by comparing their offset
   * to GMT. The later TZ's offset < default TZ's offset.
   */
  protected String getLaterTZ() {
    TimeZone laterTZ = null;
    TimeZone defaultTZ = TimeZone.getDefault();
    int defaultTZOffset = defaultTZ.getRawOffset();
    int laterTZOffset = defaultTZOffset;
    for (int i = 0; i < zoneTab.size()
        && laterTZOffset >= defaultTZOffset; i++) {
      laterTZ = TimeZone.getTimeZone(zoneTab.get(i));
      laterTZOffset = laterTZ.getRawOffset();
    }
    if (laterTZOffset >= defaultTZOffset) {
      Helper.getLogger().warning(
          "Searched all entries in zone.tab, but couldn't find a TZ later than the default TZ:"
              + defaultTZ + ", defaultTZOffset=" + defaultTZOffset
              + ", laterTZOffset=" + laterTZOffset);
    } else {
      Helper.getLogger().fine(
          "defaultTZ=" + defaultTZ + TestUtil.NEW_LINE + "laterTZ=" + laterTZ);
    }
    return laterTZ.getID();
  }

}
