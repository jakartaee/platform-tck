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
package com.sun.ts.tests.ejb30.timer.schedule.descriptor.stateless;

import javax.ejb.Stateless;
import javax.ejb.Timer;

import com.sun.ts.tests.ejb30.timer.common.TimerBeanBaseWithoutTimeOutMethod;
import com.sun.ts.tests.ejb30.timer.schedule.descriptor.common.TimeoutParamIF;

/**
 * timeout-method is only specified in ejb-jar.xml. The timeout-method (timeout)
 * is implemented in superclass TimerBeanBaseWithoutTimeOutMethod.
 * 
 * The timeout-method for both programmatic and auto timers are specified with
 * <method-params> element.
 */
@Stateless
public class WithParamTimeoutBean extends TimerBeanBaseWithoutTimeOutMethod
    implements TimeoutParamIF {

  public String getBeanName() {
    return WithParamTimeoutBean;
  }

  // timeout method for auto timer declared in ejb-jar.xml
  @SuppressWarnings("unused")
  private void auto(Timer timer) {
    timeoutAlias(timer);
  }

  @SuppressWarnings("unused")
  private void auto() {
    throw new RuntimeException(
        "This is not a timeout method since the params do not match.");
  }

  protected void timeout() {
    throw new RuntimeException("Should not reach here.  "
        + "The timeout-method specified in ejb-jar.xml has exact params.");
  }
}
