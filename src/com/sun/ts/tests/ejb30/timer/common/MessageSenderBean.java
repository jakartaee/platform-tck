/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.timer.common;

import static com.sun.ts.tests.ejb30.common.messaging.Constants.MESSAGE_TIME_TO_LIVE;
import static com.sun.ts.tests.ejb30.common.messaging.Constants.TEST_NAME_KEY;

import com.sun.ts.tests.ejb30.common.helper.Helper;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Queue;
import jakarta.jms.QueueConnection;
import jakarta.jms.QueueConnectionFactory;
import jakarta.jms.QueueSender;
import jakarta.jms.QueueSession;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;

public class MessageSenderBean {

  public static void sendMessage(QueueConnectionFactory queueConnectionFactory,
      Queue sendQueue, String testname, int testnum) {
    QueueConnection qConn = null;
    try {
      qConn = queueConnectionFactory.createQueueConnection();
      qConn.start();
      QueueSession qSession = qConn.createQueueSession(false,
          Session.AUTO_ACKNOWLEDGE);
      QueueSender qSender = qSession.createSender(sendQueue);
      qSender.setTimeToLive(MESSAGE_TIME_TO_LIVE);
      qSender.send(createTestMessage(qSession, testname, testnum));
      Helper.getLogger()
          .info(String.format(
              "Message sent from testname: %s, testnum: %s, using sender: %s",
              testname, testnum, qSender));
    } catch (JMSException e) {
      throw new RuntimeException(e);
    } finally {
      if (qConn != null) {
        try {
          qConn.close();
        } catch (JMSException e) {
          throw new RuntimeException(e);
        }
      }
    }

  }

  protected static TextMessage createTestMessage(Session session,
      String testname, int num) {
    TextMessage msg;
    try {
      msg = session.createTextMessage();
      msg.setText(testname);
      msg.setIntProperty("TestCaseNum", num);
      msg.setStringProperty("COM_SUN_JMS_TESTNAME", testname);
    } catch (JMSException e) {
      throw new RuntimeException(e);
    }
    return msg;
  }

  public static String getTestName(Message msg) {
    try {
      return msg.getStringProperty(TEST_NAME_KEY);
    } catch (JMSException e) {
      throw new RuntimeException(e);
    }
  }

}
