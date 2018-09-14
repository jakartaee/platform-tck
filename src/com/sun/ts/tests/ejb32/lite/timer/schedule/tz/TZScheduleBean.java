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

package com.sun.ts.tests.ejb32.lite.timer.schedule.tz;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Schedules;
import javax.ejb.Singleton;
import javax.ejb.Timeout;

@Singleton
public class TZScheduleBean extends TZScheduleBareBean {

  @SuppressWarnings("unused")
  @PostConstruct
  private void postConstruct() {
    initZoneTab();
  }

  @Override
  @Timeout
  @Schedules({
      // @Schedule(year = "5000", start="4000-10-26T21:32:52",
      // end="6000-10-26T21:32:52", info = DEFAULT_TZ),
      @Schedule(year = "5000", info = DEFAULT_TZ, persistent = false),
      @Schedule(year = "5000", info = ASIA_SHANGHAI, persistent = false, timezone = ASIA_SHANGHAI),
      @Schedule(year = "5000", info = AMERICA_ARGENTINA_SAN_LUIS, persistent = false, timezone = AMERICA_ARGENTINA_SAN_LUIS),
      @Schedule(year = "5000", info = AMERICA_ARGENTINA_USHUAIA, persistent = false, timezone = AMERICA_ARGENTINA_USHUAIA) })
  protected void year5000() {
    super.year5000();
  }
}
