/*
 * Copyright (c) 2008, 2020 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.timer.schedule.auto.attr.singleton;

import java.util.Collection;
import java.util.logging.Level;

import javax.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.ejb.Timer;

import com.sun.ts.tests.ejb30.common.helper.Helper;

@Singleton
@Startup
// @Startup is needed since the client currently does not have a reference to
// this bean. So
// when the test postConstruct runs, this bean may not been created yet. Use
// Startup to force
// eager creation.
public class ScheduleBean extends
    com.sun.ts.tests.ejb30.timer.schedule.auto.attr.stateless.ScheduleBeanBase3 {
  @SuppressWarnings({ "unused" })
  @PostConstruct
  private void postConstruct() {
    Helper.getLogger().logp(Level.FINE, "ScheduleBean", "postConstruct",
        "Entering " + this);
    Collection<Timer> timers = timerService.getTimers();
    String countResult = Helper.assertEquals("Count auto timers", 5,
        timers.size());
    statusSingleton.setStatus("postConstruct", true);
    statusSingleton.addRecord("postConstruct", countResult);
  }
}
