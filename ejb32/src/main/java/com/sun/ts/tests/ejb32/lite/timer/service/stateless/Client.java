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

package com.sun.ts.tests.ejb32.lite.timer.service.stateless;

import java.util.Properties;

import com.sun.ts.tests.ejb32.timer.service.common.ClientBase;

public class Client extends ClientBase {
  /*
   * @testName: testGetAllTimers
   * 
   * @test_Strategy: create a bunch of auto-timers & programmatic timers
   * respectively in a SLSB and a Singleton Bean, then run getAllTimers() from
   * the stateless bean, which should return all the timers created within the
   * same module. A SFSB with no associated timers is also packaged in the
   * module but should not cause problems.
   */

  public void setup(String[] args, Properties p) {
    super.setup(args, p);
    clientBean = statelessBean;
    autoTimerCount = 3;
    ejbLite = true;
  }
}
