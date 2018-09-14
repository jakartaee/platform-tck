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
package com.sun.ts.tests.jms.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import java.io.*;
import java.rmi.RemoteException;
import com.sun.javatest.Status;
import javax.jms.*;

/**
 * Class Declaration.
 * 
 * 
 * @see
 * 
 * @author
 * @version 1.16, 09/27/00
 */
public class SessionThread extends Thread {
  private QueueConnection qConnection = null;

  private QueueSession qSession = null;

  private TopicConnection tConnection = null;

  private TopicSession tSession = null;

  // currently, only 1 producer and 1 consumer
  MessageConsumer consumer = null;

  MessageProducer producer = null;

  private boolean replyToMessages = false; // reply when receiving?

  private boolean stayAlive = false; // receive indefinitely

  private int messagesReceivedCount = 0; // total messages received

  boolean stdebug = false; // debug output

  /**
   * Default constructor creates Session Thread with connections specified.
   * 
   * @param QueueConnection
   *          for creating QueueSessions
   * @param TopicConnection
   *          for creating TopicSessions
   */
  public SessionThread(QueueConnection qC, TopicConnection tC)
      throws JMSException {

    if (qC != null && tC != null) {
      throw new JMSException(
          "Both QueueConnection and TopicConnection are assigned, this is an error, it must be one or the other");
    }
    if (qC == null && tC == null) {
      throw new JMSException(
          "Both QueueConnection and TopicConnection are null, this is an error, it must be one or the other");
    }
    if (qC != null) {
      this.qConnection = qC;
      setQueueSession(false, Session.AUTO_ACKNOWLEDGE);
    } else {
      this.tConnection = tC;
      setTopicSession(false, Session.AUTO_ACKNOWLEDGE);
    }
  }

  /**
   * Method for specifying the QueueSession attributes.
   * 
   * @param boolean
   *          transacted
   * @param int
   *          acknowledgement mode
   */
  public void setQueueSession(boolean transacted, int mode)
      throws JMSException {
    if (qConnection != null) {
      qSession = qConnection.createQueueSession(transacted, mode);
    }
  }

  /**
   * Method for specifying the TopicSession attributes..
   * 
   * @param boolean
   *          transacted
   * @param int
   *          acknowledgement mode
   */
  public void setTopicSession(boolean transacted, int mode)
      throws JMSException {
    if (tConnection != null) {
      tSession = tConnection.createTopicSession(transacted, mode);
    }
  }

  /**
   * Return the current QueueSession
   * 
   * @return QueueSession the current QueueSession for this client
   */
  public QueueSession getQueueSession() {
    return qSession;
  }

  /**
   * Return the current TopicSession
   * 
   * @return TopicSession the current TopicSession for this client
   */
  public TopicSession getTopicSession() {
    return tSession;
  }

  /**
   * Used to start the Queue and Topic Connections when they are not the default
   * Connections. May also be used in place of someConnection.start() within the
   * main testing method.
   */
  public void startConnection() throws JMSException {
    if (qConnection != null) {
      if (stdebug) {
        TestUtil.logMsg("*ST: starting QueueConnection");
      }
      qConnection.start();
    }
    if (tConnection != null) {
      if (stdebug) {
        TestUtil.logMsg("*ST: starting TopicConnection");
      }
      tConnection.start();
    }
  }

  /**
   * Used to start only the specified Connection. Useful when it is not the
   * default Connection.
   */
  public void startQueueConnection() throws Exception {
    if (stdebug) {
      TestUtil.logMsg("*ST: starting specified connection -- Queue");
    }
    qConnection.start();
  }

  /**
   * Used to start only the specified Connection. Useful when it is not the
   * default Connection.
   */
  public void startTopicConnection() throws Exception {
    if (stdebug) {
      TestUtil.logMsg("*ST: starting specified connection -- Topic");
    }
    tConnection.start();
  }

  /**
   * Create message producers
   * 
   * @param Destination
   *          Queue or Topic
   */
  public void createProducer(Destination dest) throws Exception {
    if (qSession != null) {
      if (stdebug) {
        TestUtil.logMsg("*ST: creating QueueSender");
      }
      producer = qSession.createSender((Queue) dest);
    } else if (tSession != null) {
      if (stdebug) {
        TestUtil.logMsg("*ST: creating TopicPublisher");
      }
      producer = tSession.createPublisher((Topic) dest);
    } else {
      throw new Exception("Neither Queue or Topic were set");
    }
    TestUtil.logMsg("producer=" + producer);
  }

  /**
   * Create message consumers
   * 
   * @param Destination
   *          Queue or Topic
   */
  public void createConsumer(Destination dest) throws Exception {
    if (qSession != null) {
      if (stdebug) {
        TestUtil.logMsg("*ST: creating QueueReceiver");
      }
      consumer = qSession.createReceiver((Queue) dest);
    } else if (tSession != null) {
      if (stdebug) {
        TestUtil.logMsg("*ST: creating TopicSubscriber");
      }
      consumer = tSession.createSubscriber((Topic) dest);
    } else {
      throw new Exception("Neither Queue or Topic were configured");
    }
    TestUtil.logMsg("consumer=" + consumer);
  }

  /**
   * Set to true to have SessionThread reply automatically to messages.
   * 
   * @param boolean
   *          true for automatic request/reply
   */
  public void setReplyToMessages(boolean boo) {
    replyToMessages = boo;
    if (stdebug) {
      TestUtil.logMsg("*ST: will reply to messages -- " + replyToMessages);
    }
  }

  /**
   * Set to true to have SessionThread keep receiving messages indefinitely.
   * 
   * @param boolean
   *          true for indefinite receive()
   */
  public void setStayAlive(boolean boo) {
    stayAlive = boo;
    if (stdebug) {
      TestUtil
          .logMsg("*ST: will keep receiving after 1st message -- " + stayAlive);
    }
  }

  /**
   * Get the number of messages that have been received by this thread.
   * 
   * @return int number of messages received
   */
  public int getMessagesReceivedCount() {
    return messagesReceivedCount;
  }

  /**
   * Reset the number of messages that have been received by this thread. Useful
   * once "steady-state" has been reached.
   */
  public void resetMessagesReceivedCount() {
    messagesReceivedCount = 0;
    if (stdebug) {
      TestUtil.logMsg("*ST: message count is now " + messagesReceivedCount);
    }
  }

  /**
   * Receive messages
   */
  private void receiveMessages() throws Exception {
    if (consumer == null) {
      throw new Exception("No message consumer ready");
    } else {
      if (replyToMessages) {
        do {

          // get Message
          TestUtil.logMsg("*ST: waiting to receive (reply mode)");
          Message msg = consumer.receive();

          if (msg == null) { // just being safe
            throw new Exception("Cannot respond to null message!");
          }
          messagesReceivedCount++;
          TestUtil.logMsg("*ST: received message -- creating reply");

          // get return Destination
          Destination dest = msg.getJMSReplyTo();

          if (stdebug) {
            TestUtil.logMsg("*ST: replying to " + dest);
          }

          // create Producer and reply with new Message
          if (qSession != null) {
            TestUtil.logMsg("Replying to TemporaryQueue");
            qSession.createSender((Queue) dest).send(qSession.createMessage());
          } else if (tSession != null) {
            TestUtil.logMsg("Replying to TemporaryTopic");
            tSession.createPublisher((Topic) dest)
                .publish(tSession.createMessage());
          } else {

            // this would be a strange case indeed
            throw new Exception("Neither Queue or Topic were configured");
          }
          if (stdebug) {
            TestUtil.logMsg("*ST: keep receiving -- " + stayAlive);
          }
        } while (stayAlive);
      } else { // non reply mode
        do {
          TestUtil
              .logMsg("*ST: waiting to receive. Will continue after receiving: "
                  + stayAlive);
          Message msg = consumer.receive();

          if (msg == null) { // safety first
            throw new Exception("Received null message");
          }
          messagesReceivedCount++;
          if (stdebug) {
            TestUtil.logMsg("*ST: messages received: " + messagesReceivedCount);
          }
          TestUtil.logMsg("*ST: received " + msg.toString());
        } while (stayAlive);
      }
    }
  } // receiveMessages()

  /**
   * Stop it
   */
  public void stopWaiting() throws JMSException {
    TestUtil.logMsg("Attempting to stop MessageConsumer(s)");
    consumer.close();
  }

  /**
   * Run method
   */
  public void run() {
    TestUtil.logMsg("*ST: thread running");
    try {
      receiveMessages();
    } catch (Exception e) {
      TestUtil.logMsg("Session thread: could not receive message");
      TestUtil.logMsg("Reason being: " + e.getMessage());
    }
  }

}
