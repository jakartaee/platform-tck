/*
 * Copyright (c) 2013, 2018, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jms.ee20.ra.activationconfig.common;

import static com.sun.ts.tests.jms.ee20.ra.common.messaging.Constants.TEST_NAME_KEY;
import static com.sun.ts.tests.jms.ee20.ra.common.messaging.Constants.TEST_NUMBER_KEY;

import com.sun.ts.tests.jms.ee20.ra.common.messaging.StatusReporter;
import com.sun.ts.tests.jms.ee20.ra.common.messaging.TLogger;

import jakarta.annotation.Resource;
import jakarta.jms.Queue;
import jakarta.jms.QueueConnectionFactory;

/**
 * This class must not implement jakarta.jms.MessageListener interface. A subclass
 * may choose one of the following to specify messaging type: implements
 * jakarta.jms.MessageListener; uses
 * annotation @MessageDriven(messageListenerInterface=MessageListener.class);
 * uses descriptor element messaging-type
 */
abstract public class ActivationConfigBeanBase {
  public static final String test1 = "test1";

  abstract public jakarta.ejb.EJBContext getEJBContext();

  @Resource(name = "qFactory")
  private QueueConnectionFactory qFactory;

  @Resource(name = "replyQueue")
  private Queue replyQueue;

  // ================== business methods ====================================
  public void onMessage(jakarta.jms.Message msg) {
    TLogger.log("Entering onMessage method of: " + this.getClass().getName());
    boolean status = false;
    String reason = null;
    String testname = null;
    int testNumber = 0;
    try {
      testname = msg.getStringProperty(TEST_NAME_KEY);
      testNumber = msg.getIntProperty(TEST_NUMBER_KEY);
    } catch (jakarta.jms.JMSException e) {
      status = false;
      reason = "Failed to get test name/number from message: " + msg;
      TLogger.log(reason);
      StatusReporter.report(testname, status, reason,
          (QueueConnectionFactory) getEJBContext().lookup("qFactory"),
          (Queue) getEJBContext().lookup("replyQueue"));
      return;
    }
    if (testname == null) {
      // not the message from test client
      return;
    }

    // reply to all messages that have a testname. If the message should be
    // filtered out by selector but end up here, it also replies. Mostly
    // likely this is from client.sendReiveNegative(String, in). The client
    // will fail the test if such a reply reaches the client.
    status = true;
    reason = "ActivationConfigBeanBase received message from " + testname
        + ", testnum " + testNumber;

    TLogger.log(reason);
    StatusReporter.report(testname, status, reason,
        (QueueConnectionFactory) getEJBContext().lookup("qFactory"),
        (Queue) getEJBContext().lookup("replyQueue"));
  }
}
