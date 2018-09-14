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

package com.sun.ts.tests.ejb32.lite.timer.schedule.descriptor.singleton;

import javax.ejb.Singleton;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.timer.common.TimerBeanBaseWithoutTimeOutMethod;

/**
 * timeout-method is only specified in ejb-jar.xml. The timeout-method (timeout)
 * is implemented in superclass TimerBeanBaseWithoutTimeOutMethod.
 * 
 * The timeout-method is specified without <method-params> element, since the
 * method timeout is not overloaded and sufficient to identify.
 */
@Singleton
public class SingletonTimerBean extends TimerBeanBaseWithoutTimeOutMethod {

  // timeout method for auto timer declared in ejb-jar.xml
  @SuppressWarnings("unused")
  private void year5000() {
    Helper.getLogger().info("In year5000, the timeout-method for auto timer.");
  }

}
