/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.common.messaging;

import com.sun.ts.tests.ejb30.common.helper.TLogger;
import javax.jms.*;
import com.sun.ts.tests.jms.common.JmsUtil;

public class StatusReporter implements Constants {
  private static StatusReporter reporter = new StatusReporter();

  private static StatusReporter getInstance() {
    return reporter;
  }

  public static void report(String testname, boolean result, String reason,
      QueueConnectionFactory qFactory, Queue replyQueue) {
    StatusReporter reporter = getInstance();
    reporter.report0(testname, result, reason, qFactory, replyQueue);
  }

  public static void report(String testname, boolean result,
      QueueConnectionFactory qFactory, Queue queueR) {
    // report(testname, result, (String) null), qFactory, queueR;
  }

  private void report0(String testname, boolean result, String reason,
      QueueConnectionFactory qFactory, Queue queueR) {
    QueueConnection qConnection = null;
    QueueSession qSession = null;
    try {
      qConnection = qFactory.createQueueConnection();
      qSession = qConnection.createQueueSession(false,
          Session.AUTO_ACKNOWLEDGE);
      sendTestResults(testname, result, reason, qSession, queueR);
    } catch (JMSException e) {
      String description = null;
      if (result) {
        description = "The test " + testname + " passed in the bean.";
      } else {
        description = "The test " + testname + " failed in the bean: " + reason;
      }
      throw new java.lang.IllegalStateException(description
          + " Failed to create connection/session in StatusReporter:", e);
    } finally {
      if (qConnection != null) {
        try {
          qConnection.close();
        } catch (Exception e) {
          // ignore
        }
      }
    }
  }

  public static void sendTestResults(String testCase, boolean results,
      String reason, QueueSession qSession, javax.jms.Queue queueR) {
    TextMessage msg = null;
    QueueSender mSender = null;

    try {
      // create a msg sender for the response queue
      mSender = qSession.createSender(queueR);
      // and we'll send a text msg
      msg = qSession.createTextMessage();
      msg.setStringProperty("TestCase", testCase);
      msg.setText(testCase);
      if (results) {
        msg.setStringProperty("Status", "Pass");
      } else {
        msg.setStringProperty("Status", "Fail");
      }
      mSender.send(msg);
      TLogger.log("Status message (" + results + ") sent for test " + testCase
          + ". Reason:" + reason);
    } catch (JMSException e) {
      TLogger.log("Failed to send back status:", e);
    } catch (Exception e) {
      TLogger.log("Failed to send back status:", e);
    }
  }
}
