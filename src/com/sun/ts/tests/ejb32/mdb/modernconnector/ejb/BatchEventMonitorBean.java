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

package com.sun.ts.tests.ejb32.mdb.modernconnector.ejb;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.interceptor.Interceptors;

import com.sun.ts.tests.ejb32.mdb.modernconnector.connector.EventMonitor;
import com.sun.ts.tests.ejb32.mdb.modernconnector.connector.NoUseListener;

@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "category", propertyValue = "batch") })
@Interceptors(LoggerInterceptor.class)
public class BatchEventMonitorBean implements NoUseListener {

  @Resource
  private MessageDrivenContext messageDrivenContext;

  @EventMonitor(priority = "high")
  public void consumeHigh(String event) {
    messageDrivenContext.getContextData().put("data",
        event + " is processed by high-priority batch monitor");
  }

  @EventMonitor(priority = "normal")
  public void consumeNormal(String event) {
    messageDrivenContext.getContextData().put("data",
        event + " is processed by normal-priority batch monitor");
  }

  @EventMonitor(priority = "low")
  public void consumeLow(String event) {
    messageDrivenContext.getContextData().put("data",
        event + " is processed by low-priority batch monitor");
  }

}
