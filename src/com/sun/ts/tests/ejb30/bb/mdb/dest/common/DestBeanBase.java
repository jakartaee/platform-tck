/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.bb.mdb.dest.common;

import com.sun.ts.tests.ejb30.common.helper.TLogger;
import com.sun.ts.tests.ejb30.common.messaging.StatusReporter;
import javax.annotation.Resource;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import static com.sun.ts.tests.ejb30.common.messaging.Constants.TEST_NAME_KEY;
import static com.sun.ts.tests.ejb30.common.messaging.Constants.TEST_NUMBER_KEY;

/**
 * This class must not implement javax.jms.MessageListener interface. A subclass
 * may choose one of the following to specify messaging type: implements
 * javax.jms.MessageListener; uses
 * annotation @MessageDriven(messageListenerInterface=MessageListener.class);
 * uses descritpor element messaging-type
 *
 */
abstract public class DestBeanBase {
  public static final String test1 = "test1";

  abstract public jakarta.ejb.EJBContext getEJBContext();

  @Resource(name = "qFactory")
  private QueueConnectionFactory qFactory;

  @Resource(name = "replyQueue")
  private Queue replyQueue;

  // ================== business methods ====================================
  public void onMessage(javax.jms.Message msg) {
    boolean status = false;
    String reason = null;
    String testname = null;
    int testNumber = 0;
    try {
      testname = msg.getStringProperty(TEST_NAME_KEY);
      testNumber = msg.getIntProperty(TEST_NUMBER_KEY);
    } catch (javax.jms.JMSException e) {
      status = false;
      reason = this.getClass().getName()
          + "Failed to get test name/number from message: " + msg;
      TLogger.log(reason);
      StatusReporter.report(testname, status, reason,
          (QueueConnectionFactory) getEJBContext().lookup("qFactory"),
          (Queue) getEJBContext().lookup("replyQueue"));
      return;
    }
    // testNumber is always 0 unless client specifies it otherwise.
    if (testname.equals(test1) && testNumber == 0) {
      status = true;
      reason = this.getClass().getName() + " received message from " + testname
          + ", status " + status;
    } else {
      status = false;
      reason = this.getClass().getName() + "Unrecognized testname: " + testname
          + ", testnum: " + testNumber;
    }
    StatusReporter.report(testname, status, reason,
        (QueueConnectionFactory) getEJBContext().lookup("qFactory"),
        (Queue) getEJBContext().lookup("replyQueue"));
  }
}
