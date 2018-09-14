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
package com.sun.ts.tests.ejb30.timer.schedule.descriptor.common;

import javax.ejb.Timer;
import javax.ejb.TimerConfig;

public interface TimeoutParamIF {

  public static final String EmptyParamTimeoutBean = "EmptyParamTimeoutBean";

  public static final String NoParamTimeoutBean = "NoParamTimeoutBean";

  public static final String WithParamTimeoutBean = "WithParamTimeoutBean";

  public static final String PROGRAMMATIC_TIMER_SUFFIX = ".programmatic";

  public static final String AUTO_TIMER_SUFFIX = ".auto";

  public Timer createSecondLaterTimer(String name);

  public Timer createSecondLaterTimer(TimerConfig timerConfig);

  public void cancelAllTimers();

  public String getBeanName();

}
