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

package com.sun.ts.tests.ejb30.timer.basic.mdb;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.MessageDriven;
import javax.ejb.ActivationConfigProperty;
import javax.jms.MessageListener;

import com.sun.ts.tests.ejb30.common.helper.Helper;

//This MDB implements javax.jms.MessageListener interface, so no need to
//use annotation element messageListenerInterface, nor descritpor element
//messaging-type
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue") })
public class TimerBasicBean extends TimerBasicBeanBase
    implements MessageListener {
  public TimerBasicBean() {
    super();
  }

  @SuppressWarnings("unused")
  @PostConstruct
  private void postConstruct() {
    Helper.getLogger().info("In PostConstruct of " + this);
  }

  @SuppressWarnings("unused")
  @PreDestroy
  private void preDestroy() {
    Helper.getLogger().info("In PreDestroy of " + this);
  }

}
