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

/*
 * $Id$
 */

package com.sun.ts.tests.jms.ee20.ra.common.messaging;

import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.commonee.Client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.Stack;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;

abstract public class QueueClientBase extends Client implements Constants {
  private String currentTestName;

  //////////////////////////////////////////////////////////////////////
  // Queue related stuff
  //////////////////////////////////////////////////////////////////////
  // These are declared in super class jms.commonee.Client
  // protected QueueConnection qConnect;
  // protected QueueSession session;
  // protected QueueConnectionFactory qFactory;
  // protected QueueSender qSender;
  // protected Queue rcvrQueue;
  //
  private Queue sendQ;

  abstract protected void initSendQueue();

  abstract protected void initReceiveQueue();

  abstract protected void initQueueConnectionFactory();

  protected Queue getSendQueue() {
    return sendQ;
  }

  protected void setSendQueue(Queue q) {
    sendQ = q;
  }

  // this receive queue is also used by Topic MDB to send back response.
  protected Queue getReceiveQueue() {
    return rcvrQueue;
  }

  protected void setReceiveQueue(Queue q) {
    rcvrQueue = q;
  }

  protected QueueConnectionFactory getQueueConnectionFactory() {
    return qFactory;
  }

  protected void setQueueConnectionFactory(QueueConnectionFactory qf) {
    qFactory = qf;
  }

  protected String getCurrentTestName() {
    return currentTestName;
  }

  protected void setCurrentTestName(String tn) {
    currentTestName = tn;
  }

  /**
   * Sends message and waits for response. The message should reach the target
   * MDB, and a response should be received by this client. Simplified version
   * since testname can be retrieved from props in setup().
   */
  protected void sendReceive() throws Fault {
    sendReceive(getCurrentTestName(), 0);
  }

  /**
   * Sends message and waits for response. The message should not reach the
   * target MDB, and no response should be received by this client.
   */
  protected void sendReceiveNegative(String testname, int testnum)
      throws Fault {
    sendOnly(testname, testnum);
    if (checkOnResponse(testname)) {
      throw new Fault("This is a negative test that expects no response,"
          + " but actually got a response.");
    } else {
      TLogger.log("No response, as expected");
    }
  }

  /**
   * Sends message and waits for response. The message should reach the target
   * MDB, and a response should be received by this client.
   */
  protected void sendReceive(String testname, int testnum) throws Fault {
    sendOnly(testname, testnum);
    // sometimes have to sleep for awhile. But we shouldn't interfere
    // with thread management
    if (!checkOnResponse(testname)) {
      throw new Fault("checkOnResponse for " + testname + " returned false.");
    }
  }

  protected void sendOnly(String testname, int testnum) throws Fault {
    try {
      createTestMessage(testname, testnum);
      MessageProducer producer = getMessageProducer();
      producer.setTimeToLive(MESSAGE_TIME_TO_LIVE);
      producer.send(msg);
      TLogger.log("message sent from testname: " + testname + ", testnum: "
          + testnum + ", using producer: " + producer);
    } catch (Exception e) {
      throw new Fault(e);
    }
  }

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    setCurrentTestName(p.getProperty(FINDER_TEST_NAME_KEY));
    initTestProperties(p);

    // used by both queue and topic MDB to send back response
    initReceiveQueue();

    try {
      configureQueue();
      configureTopic();
    } catch (JMSException ex) {
      throw new Fault(ex);
    }
    TLogger.log("get the connection and start up");
    TLogger.log("Client: connection started, now send initialization msg!");
  }

  protected MessageProducer getMessageProducer() throws JMSException {
    qSender = session.createSender(getSendQueue());
    return qSender;
  }

  protected void configureQueue() throws JMSException {
    initQueueConnectionFactory();
    qConnect = qFactory.createQueueConnection(jmsUser, jmsPassword);
    session = qConnect.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
    qConnect.start();
    initSendQueue();
  }

  protected void configureTopic() throws JMSException {
  }

  protected void urlTest(URL url) throws Fault {
    urlTest(url, PASSED);
  }

  protected void urlTest(URL url, String expectedFirstLine) throws Fault {
    HttpURLConnection conn = null;
    InputStream is = null;
    TLogger.log("About to connect to url: ");
    TLogger.log(url.toString());
    try {
      conn = (HttpURLConnection) (url.openConnection());
      int code = conn.getResponseCode();
      if (code != HttpURLConnection.HTTP_OK) {
        throw new Fault("Unexpected return code: " + code);
      }

      is = conn.getInputStream();
      BufferedReader input = new BufferedReader(new InputStreamReader(is));
      String line = input.readLine();
      if (line != null) {
        line = line.trim();
      }
      if (!expectedFirstLine.equals(line)) {
        throw new Fault("Wrong response. Expected: " + expectedFirstLine
            + ", received: " + line);
      } else {
        TLogger.log("Got expected response: " + line);
      }
      try {
        input.close();
      } catch (IOException e) {
        // ignore
      }
    } catch (IOException e) {
      throw new Fault(e);
    } finally {
      if (is != null) {
        try {
          is.close();
        } catch (IOException e) {
          // ignore
        }
      }
      if (conn != null) {
        conn.disconnect();
      }
    }
  }

  private void initTestProperties(Properties p) throws Fault {
    jmsUser = p.getProperty("user");
    if (jmsUser == null) {
      TestUtil.logTrace("user is null");
      throw new Fault("Error getting user");
    }

    jmsPassword = p.getProperty("password");
    if (jmsPassword == null) {
      TestUtil.logTrace("passwd is null");
      throw new Fault("Error getting pwd");
    }

    String time = p.getProperty("jms_timeout");
    if (time == null) {
      TestUtil.logTrace("time is null");
      throw new Fault("Error getting time");
    }

    hostname = p.getProperty("harness.host");
    if (hostname == null) {
      TestUtil.logTrace("Hostname is null");
      throw new Fault("Error getting hostname");
    }
    traceFlag = p.getProperty("harness.log.traceflag");
    if (traceFlag == null) {
      TestUtil.logTrace("Hostname is null");
      throw new Fault("Error getting traceflag");
    }
    logPort = p.getProperty("harness.log.port");
    if (logPort == null) {
      TestUtil.logTrace("logport is null");
      throw new Fault("Error getting port");
    }
    timeout = Integer.parseInt(time);
  }
}
