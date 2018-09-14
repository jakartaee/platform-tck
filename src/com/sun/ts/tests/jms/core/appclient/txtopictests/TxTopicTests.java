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
package com.sun.ts.tests.jms.core.appclient.txtopictests;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import java.util.*;
import com.sun.javatest.Status;
import javax.jms.*;
import java.io.Serializable;
import java.io.IOException;
import java.rmi.RemoteException;

public class TxTopicTests extends ServiceEETest {
  private static final String testName = "com.sun.ts.tests.jms.ee.ejbjspservlet.txtopictests.TxTopicTests";

  private static final String testDir = System.getProperty("user.dir");

  private static final long serialVersionUID = 1L;

  // Harness req's
  private Properties props = null;

  // JMS object
  private transient JmsTool tool = null;

  // properties read from ts.jte file
  long timeout;

  private String jmsUser;

  private String jmsPassword;

  private String mode;

  public static final int TOPIC = 1;

  ArrayList connections = null;

  /* Run test in standalone mode */

  /**
   * Main method is used when not run from the JavaTest GUI.
   * 
   * @param args
   */
  public static void main(String[] args) {
    TxTopicTests theTests = new TxTopicTests();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup: */

  /*
   * setup() is called before each test
   * 
   * @class.setup_props: jms_timeout;user; password; platform.mode;
   * 
   * @exception Fault
   */
  public void setup(String[] args, Properties p) throws Fault {
    try {
      logTrace("In setup");
      // get props
      jmsUser = p.getProperty("user");
      jmsPassword = p.getProperty("password");
      timeout = Long.parseLong(p.getProperty("jms_timeout"));
      mode = p.getProperty("platform.mode");
      if (timeout < 1) {
        throw new Exception("'timeout' (milliseconds) in ts.jte must be > 0");
      }
      connections = new ArrayList(5);

      // get ready for new test
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("Setup failed!", e);
    }
  }

  /* cleanup */

  /*
   * cleanup() is called after each test
   * 
   * Closes the default connections that are created by setup(). Any separate
   * connections made by individual tests should be closed by that test.
   * 
   * @exception Fault
   */
  public void cleanup() throws Fault {
    try {
      if (tool != null) {
        logMsg("Cleanup: Closing Topic Connections");
        tool.closeAllConnections(connections);
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      logErr("An error occurred while cleaning");
      throw new Fault("Cleanup failed!", e);
    }
  }

  /* Tests */

  /*
   * @testName: commitAckMsgTopicTest
   * 
   * @assertion_ids: JMS:SPEC:130; JMS:SPEC:166;
   * 
   * @test_Strategy: Create tx_session and receive one message from a topic.
   * Call commit() and close session. Create new non_tx session and receive
   * message. Should only receive the one message.
   */
  public void commitAckMsgTopicTest() throws Fault {
    String lookup = "DURABLE_SUB_CONNECTION_FACTORY";

    try {
      TextMessage mSent = null;
      TextMessage mReceived = null;
      TopicSession tSess = null;
      TopicPublisher tPub = null;
      TopicSubscriber tSub = null;
      String msg = "test message for commitAckMsgTest";
      String subscriptionName = "commitAckMsgTopicTestSubscription";

      // create tx session
      tool = new JmsTool(JmsTool.DURABLE_TX_TOPIC, jmsUser, jmsPassword, lookup,
          mode);

      tool.getDefaultTopicSubscriber().close();
      tSess = tool.getDefaultTopicSession();
      tPub = tool.getDefaultTopicPublisher();
      tSub = tSess.createDurableSubscriber(tool.getDefaultTopic(),
          subscriptionName);
      tool.getDefaultTopicConnection().start();
      // send message
      logTrace("Send first message");
      mSent = tSess.createTextMessage();
      mSent.setBooleanProperty("lastMessage", false);
      mSent.setText(msg);
      mSent.setStringProperty("COM_SUN_JMS_TESTNAME", "commitAckMsgTopicTest1");
      tPub.publish(mSent);
      tSess.commit();

      logTrace("Send second message");
      mSent.setBooleanProperty("lastMessage", true);
      tPub.publish(mSent);
      tSess.commit();

      logTrace("Message sent. Receive with tx session, do not acknowledge.");
      mReceived = (TextMessage) tSub.receive(timeout);
      if (mReceived == null) {
        logMsg("Did not receive message!");
        throw new Exception("Did not receive message first time");
      }
      logTrace("Received message: \"" + mReceived.getText() + "\"");

      // commit and close session
      logTrace("Call commit() without calling acknowledge().");
      tSess.commit();
      logTrace("Close session and create new one.");
      tSess.close();

      // create new (non-tx) session
      tSess = tool.getDefaultTopicConnection().createTopicSession(false,
          Session.AUTO_ACKNOWLEDGE);
      tSub = tSess.createDurableSubscriber(tool.getDefaultTopic(),
          subscriptionName);

      // check for messages; should receive second message
      mReceived = (TextMessage) tSub.receive(timeout);
      if (mReceived == null) {
        logMsg("Did not receive message!");
        throw new Exception("Did not receive expected message");
      } else if (mReceived.getBooleanProperty("lastMessage") == false) {
        logMsg(
            "Received orignal message again. Was not acknowledged by commit().");
        throw new Exception("Message not acknowledged by commit");
      } else if (mReceived.getBooleanProperty("lastMessage") == true) {
        logMsg("Pass: received proper message");
      }
      tSub.close();
      tSess.unsubscribe(subscriptionName);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("commitAckMsgTopicTest");
    }
  }

  /*
   * @testName: rollbackRecoverTopicTest
   * 
   * @assertion_ids: JMS:SPEC:130;
   * 
   * @test_Strategy: Create tx_session. Receive one message from a topic and
   * call rollback. Attempt to receive message again.
   */
  public void rollbackRecoverTopicTest() throws Fault {
    String lookup = "MyTopicConnectionFactory";

    try {
      TextMessage mSent = null;
      TextMessage mReceived = null;
      TopicSession tSess = null;
      TopicPublisher tPub = null;
      TopicSubscriber tSub = null;
      String msg = "test message for rollbackRecoverTest";

      // close default session and create tx AUTO_ACK session
      tool = new JmsTool(JmsTool.TX_TOPIC, jmsUser, jmsPassword, lookup, mode);
      tool.getDefaultTopicSession().close();
      tSess = tool.getDefaultTopicConnection().createTopicSession(true,
          Session.AUTO_ACKNOWLEDGE);
      tPub = tSess.createPublisher(tool.getDefaultTopic());
      tSub = tSess.createSubscriber(tool.getDefaultTopic());
      tool.getDefaultTopicConnection().start();

      // send message
      mSent = tSess.createTextMessage();
      mSent.setText(msg);
      mSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "rollbackRecoverTopicTest");
      tPub.publish(mSent);
      tSess.commit();

      // receive message
      logTrace("Message sent. Receive with tx session, do not acknowledge.");
      mReceived = (TextMessage) tSub.receive(timeout);
      if (mReceived == null) {
        logMsg("Did not receive message!");
        throw new Exception("Did not receive message first time");
      }
      logTrace("Received message: \"" + mReceived.getText() + "\"");

      // rollback session
      logTrace("Call rollback() without acknowledging message.");
      tSess.rollback();

      // check for messages; should receive one
      logTrace("Attempt to receive message again");
      mReceived = (TextMessage) tSub.receive(timeout);
      if (mReceived == null) {
        logMsg("Did not receive message!");
        throw new Exception("Did not receive expected message");
      }
      if (mReceived.getText().equals(msg)) {
        logMsg("Received orignal message again. Was not acknowledged.");
      } else {
        throw new Exception("Received unexpected message");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("rollbackRecoverTopicTest");
    }
  }

  /*
   * @testName: redeliveredFlagTxTopicTest
   * 
   * @assertion_ids: JMS:SPEC:129; JMS:JAVADOC:371; JMS:SPEC:13; JMS:SPEC:167;
   * 
   * @test_Strategy: Send message to a topic and receive it with a AUTO_ACK
   * session. Check that the redelivered flag is FALSE. Call rollback and
   * receive the message again. Check that the flag is now TRUE.
   */
  public void redeliveredFlagTxTopicTest() throws Fault {
    String lookup = "MyTopicConnectionFactory";

    try {
      TextMessage sentMsg = null;
      TextMessage recMsg = null;

      // create topic setup with AUTO_ACK session for receiving
      tool = new JmsTool(JmsTool.TX_TOPIC, jmsUser, jmsPassword, lookup, mode);
      tool.getDefaultTopicConnection().start();

      // create and publish message
      logTrace("publish and receive one message");
      sentMsg = tool.getDefaultTopicSession().createTextMessage();
      sentMsg.setText("test message for redelivered flag");
      sentMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "redeliveredFlagTxTopicTest");
      tool.getDefaultTopicPublisher().publish(sentMsg);
      tool.getDefaultTopicSession().commit();

      // receive message and check flag
      recMsg = (TextMessage) tool.getDefaultTopicSubscriber().receive(timeout);
      if (recMsg == null) {
        logMsg("Did not receive expected message!");
        throw new Exception("Did not receive message");
      }
      logTrace("Message received. Check redelivered flag.");
      boolean redelivered = recMsg.getJMSRedelivered();

      logTrace("redelivered = " + redelivered);
      if (redelivered == true) {
        throw new Exception("Message redelivered flag should be false");
      }

      // rollback, receive, check flag
      logTrace("calling rollback()");
      tool.getDefaultTopicSession().rollback();
      logTrace("receive message again");
      recMsg = (TextMessage) tool.getDefaultTopicSubscriber().receive(timeout);
      tool.getDefaultTopicSession().commit();
      redelivered = recMsg.getJMSRedelivered();
      logTrace("redelivered flag = " + redelivered);
      if (redelivered == false) {
        throw new Exception("Message redelivered flag should be true");
      }
    } catch (Exception e) {
      logMsg("Error: " + e);
      throw new Fault("redeliveredFlagTxTopicTest", e);
    }
  }

  /*
   * @testName: transactionRollbackOnSessionCloseReceiveTopicTest
   * 
   * @assertion_ids: JMS:SPEC:104; JMS:SPEC:166; JMS:SPEC:167;
   * 
   * @test_Strategy: Use the default topic session, subscriber and publisher.
   * Set up an additional tx topic session and subscriber. Send and receive a
   * transacted message. Send another transacted message, but close the session
   * after receive() with no commit. Verify that the message is not received by
   * receiving again with new session.
   */
  public void transactionRollbackOnSessionCloseReceiveTopicTest() throws Fault {
    String lookup = "DURABLE_SUB_CONNECTION_FACTORY";

    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      String subscriptionName = "TxTopicTestSubscription";

      // set up test tool for Topic for publishing session
      tool = new JmsTool(JmsTool.DURABLE_TX_TOPIC, jmsUser, jmsPassword, lookup,
          mode);
      // close default subscriber and create a durable one
      tool.getDefaultTopicSubscriber().close();

      // Create another topic session and subscriber
      TopicSession newTopicSess = tool.getDefaultTopicConnection()
          .createTopicSession(true, 0);

      TopicSubscriber newSubscriber = newTopicSess
          .createDurableSubscriber(tool.getDefaultTopic(), subscriptionName);

      logTrace("Start connection");
      tool.getDefaultTopicConnection().start();

      // send and receive message
      messageSent = tool.getDefaultTopicSession().createTextMessage();
      messageSent.setBooleanProperty("lastMessage", false);
      messageSent.setText("transaction message test");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "transactionRollbackOnSessionCloseReceiveTopicTest");
      tool.getDefaultTopicPublisher().publish(messageSent);
      tool.getDefaultTopicSession().commit();

      messageReceived = (TextMessage) newSubscriber.receive(timeout);
      newTopicSess.commit();

      // Check to see if correct message received
      if (messageReceived.getText().equals(messageSent.getText())) {
        logTrace("Message text: \"" + messageReceived.getText() + "\"");
        logTrace("Received correct message");
      } else {
        throw new Exception("didn't get the right message");
      }

      // Send another message
      messageSent.setBooleanProperty("lastMessage", true);
      tool.getDefaultTopicPublisher().publish(messageSent);
      tool.getDefaultTopicSession().commit();
      // close the session without doing the commit.
      logTrace("Receive message and call close()");
      messageReceived = (TextMessage) newSubscriber.receive(timeout);
      newTopicSess.close();

      logTrace("Create new session and attempt to receive message");
      newTopicSess = tool.getDefaultTopicConnection().createTopicSession(true,
          0);
      newSubscriber = newTopicSess
          .createDurableSubscriber(tool.getDefaultTopic(), subscriptionName);
      messageReceived = (TextMessage) newSubscriber.receive(timeout);
      newTopicSess.commit();

      newSubscriber.close();
      newTopicSess.unsubscribe(subscriptionName);

      // check message
      if (messageReceived == null) {
        throw new Fault("Fail: Should have received message");
      } else if (messageReceived.getBooleanProperty("lastMessage") == true) {
        logTrace("Pass: received message again, previous tx was rolled back");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("transactionRollbackOnSessionCloseReceiveTopicTest");
    }
  }

  /*
   * @testName: transactionRollbackOnPublishTopicTest
   * 
   * @assertion_ids: JMS:SPEC:123;
   * 
   * @test_Strategy: Use the default topic session, subscriber and publisher.
   * Create durable subscriber to replace default Set up an additional tx topic
   * session and publisher. Send and receive a transacted message. Send another
   * transacted message, but rollback after publish() Send third message and
   * commit Verify that the second message is not received.
   */
  public void transactionRollbackOnPublishTopicTest() throws Fault {
    String lookup = "DURABLE_SUB_CONNECTION_FACTORY";

    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      String subscriptionName = "TxTopicTestSubscription";

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.DURABLE_TX_TOPIC, jmsUser, jmsPassword, lookup,
          mode);
      tool.getDefaultTopicSubscriber().close();
      TopicSubscriber tSub = tool.getDefaultTopicSession()
          .createDurableSubscriber(tool.getDefaultTopic(), subscriptionName);
      logTrace("Start connection");
      tool.getDefaultTopicConnection().start();

      // Create another topic session and publisher
      TopicSession newTopicSess = tool.getDefaultTopicConnection()
          .createTopicSession(true, 0);
      TopicPublisher newPublisher = newTopicSess
          .createPublisher(tool.getDefaultTopic());

      // send and receive message
      logTrace("Creating 1 message");
      messageSent = newTopicSess.createTextMessage();
      messageSent.setBooleanProperty("lastMessage", false);
      messageSent.setText("transaction message test");
      logTrace("Publish the  message");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "transactionRollbackOnPublishTopicTest");
      newPublisher.publish(messageSent);
      logTrace("Call commit");
      newTopicSess.commit();
      logTrace("Receiving message");
      messageReceived = (TextMessage) tSub.receive(timeout);

      // Check to see if correct message received
      if (messageReceived.getText().equals(messageSent.getText())) {
        logTrace("Message text: \"" + messageReceived.getText() + "\"");
        logTrace("Received correct message");
      } else {
        throw new Exception("didn't get the right message");
      }

      // Send another message, but then rollback.
      logTrace("Publish a message, but then rollback");
      newPublisher.publish(messageSent);
      newTopicSess.rollback();

      logTrace("Publish 3rd message");
      messageSent.setBooleanProperty("lastMessage", true);
      newPublisher.publish(messageSent);
      newTopicSess.commit();

      logTrace("Attempt to receive last message only");
      messageReceived = (TextMessage) tSub.receive(timeout);
      tool.getDefaultTopicSession().commit();

      if (messageReceived == null) {
        throw new Fault("Fail: Should have received message");
      } else if (messageReceived.getBooleanProperty("lastMessage") == true) {
        logTrace("Pass: last msg received, proper message was rolledback");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("transactionRollbackOnPublishTopicTest");
    }
  }

  /*
   * @testName: transactionRollbackOnRecTopicTest
   * 
   * @assertion_ids: JMS:SPEC:123;
   * 
   * @test_Strategy: Use the default topic session, subscriber and publisher.
   * Set up an additional topic session, subscriber and pubisher. Send and
   * receive a transacted message. Send another transacted message, but rollback
   * after receive Verify that the message is rolled back to the topic
   */
  public void transactionRollbackOnRecTopicTest() throws Fault {
    String lookup = "MyTopicConnectionFactory";

    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      TextMessage messageReceived2 = null;
      // set up test tool for Topic for transacted session
      tool = new JmsTool(JmsTool.TX_TOPIC, jmsUser, jmsPassword, lookup, mode);
      logTrace("Start connection");
      tool.getDefaultTopicConnection().start();

      // Create another topic Session, receiver and sender
      TopicSession newTopicSess = tool.getDefaultTopicConnection()
          .createTopicSession(true, Session.AUTO_ACKNOWLEDGE);
      TopicPublisher newPublisher = newTopicSess
          .createPublisher(tool.getDefaultTopic());

      // send and receive message
      logTrace("Creating 1 message");
      messageSent = newTopicSess.createTextMessage();
      messageSent.setText("transaction message test");
      logTrace("Sending message");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "transactionRollbackOnRecTopicTest");
      newPublisher.publish(messageSent);
      logTrace("Call commit");
      newTopicSess.commit();
      logTrace("Receiving message");
      messageReceived = (TextMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      logTrace("Call commit");
      tool.getDefaultTopicSession().commit();

      // Check to see if correct message received
      if (messageReceived.getText().equals(messageSent.getText())) {
        logTrace("Message text: \"" + messageReceived.getText() + "\"");
        logTrace("Received correct message");
      } else {
        throw new Exception("didn't get the right message");
      }
      logTrace("Publish the message and commit it");
      newPublisher.publish(messageSent);
      newTopicSess.commit();
      messageReceived = (TextMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      logTrace("Rollback after getting the message");
      tool.getDefaultTopicSession().rollback();
      logTrace("Doing a second receive - getting messageReceived2 ");
      messageReceived2 = (TextMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      logTrace(
          "Since the receive was not committed, I expect the message to still be there");
      if (messageReceived2 == null) {
        throw new Fault("Fail: message was not rolled back to the topic");
      }
      if (messageReceived.getText().equals(messageReceived2.getText())) {
        logTrace("Message text: \"" + messageReceived.getText() + "\"");
        logTrace("Message2 text: \"" + messageReceived2.getText() + "\"");
        logTrace("Pass: Message was recovered");
      } else {
        throw new Fault(
            "Did not receive expected message after not doing the commit");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("transactionRollbackOnRecTopicTest");
    }
  }

  /*
   * @testName: txRollbackOnConnectionCloseReceiveTopicTest
   * 
   * @assertion_ids: JMS:SPEC:104; JMS:SPEC:166;
   * 
   * @test_Strategy: Use the default topic session, subscriber and publisher.
   * Send and receive a transacted message. Send another transacted message, but
   * close the connection after receive() with no commit. Create new connection
   * and attempt to receive message.
   */
  public void txRollbackOnConnectionCloseReceiveTopicTest() throws Fault {
    String lookup = "DURABLE_SUB_CONNECTION_FACTORY";

    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      String subscriptionName = "TxTopicTestSubscription";
      String clientID = null;

      // set up test tool for Topic for transacted session
      tool = new JmsTool(JmsTool.DURABLE_TX_TOPIC, jmsUser, jmsPassword, lookup,
          mode);
      clientID = tool.getDefaultTopicConnection().getClientID();
      tool.getDefaultTopicSubscriber().close();
      TopicSubscriber tSub = tool.getDefaultTopicSession()
          .createDurableSubscriber(tool.getDefaultTopic(), subscriptionName);
      logTrace("Start connection");
      tool.getDefaultTopicConnection().start();

      // send and receive message
      messageSent = tool.getDefaultTopicSession().createTextMessage();
      messageSent.setBooleanProperty("lastMessage", false);
      messageSent.setText("transaction message test");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "txRollbackOnConnectionCloseReceiveTopicTest");
      tool.getDefaultTopicPublisher().publish(messageSent);
      tool.getDefaultTopicSession().commit();
      messageReceived = (TextMessage) tSub.receive(timeout);
      tool.getDefaultTopicSession().commit();

      // Check to see if correct message received
      if (messageReceived.getText().equals(messageSent.getText())) {
        logTrace("Message text: \"" + messageReceived.getText() + "\"");
        logTrace("Received correct message");
      } else {
        throw new Exception("didn't get the right message");
      }

      // Send another message
      logTrace("send second message");
      messageSent.setBooleanProperty("lastMessage", true);
      tool.getDefaultTopicPublisher().publish(messageSent);
      tool.getDefaultTopicSession().commit();

      // receive and close the connection without doing the commit.
      logTrace("Receive message");
      messageReceived = (TextMessage) tSub.receive(timeout);
      if (messageReceived == null) {
        throw new Fault("Should have received second message");
      }
      logTrace("Call connection.close()");
      tool.getDefaultTopicConnection().close();

      logTrace("Create new connection and attempt to receive message");
      TopicConnection newConn = (TopicConnection) tool.getNewConnection(
          JmsTool.DURABLE_TOPIC, jmsUser, jmsPassword, lookup);
      connections.add(newConn);

      // must be same as first connection to use same durable subscription
      String newClientID = newConn.getClientID();
      if (newClientID == null || !newClientID.equals(clientID)) {
        try {
          newConn.setClientID(clientID);
        } catch (JMSException je) {
          TestUtil.printStackTrace(je);
          logMsg("Warning: cannot set client ID to match first connection.\n"
              + "Test may not be able to use same durable subscription");
        }
      }

      TopicSession newTopicSess = newConn.createTopicSession(true, 0);
      TopicSubscriber newSubscriber = newTopicSess
          .createDurableSubscriber(tool.getDefaultTopic(), subscriptionName);

      newConn.start();

      logTrace("receive message");
      messageReceived = (TextMessage) newSubscriber.receive(timeout);
      newTopicSess.commit();

      newSubscriber.close();
      newTopicSess.unsubscribe(subscriptionName);
      newConn.close();

      // check message
      if (messageReceived == null) {
        throw new Fault("Fail: Should have received message");
      } else if (messageReceived.getBooleanProperty("lastMessage") == true) {
        logTrace("Pass: received message again, previous tx was rolled back");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("txRollbackOnConnectionCloseReceiveTopicTest");
    }
  }

  /*
   * @testName: commitRollbackMultiMsgsTest
   *
   * @assertion_ids: JMS:JAVADOC:231; JMS:JAVADOC:221; JMS:JAVADOC:244;
   * JMS:JAVADOC:242; JMS:JAVADOC:229; JMS:JAVADOC:504; JMS:JAVADOC:510;
   * JMS:JAVADOC:597; JMS:JAVADOC:334;
   *
   * @test_Strategy: Test the following APIs:
   *
   * ConnectionFactory.createConnection(String, String)
   * Connection.createSession(boolean, int) Session.createTextMessage(String)
   * Session.createConsumer(Destination) Session.createProducer(Destination)
   * Session.commit() Session.rollback() MessageProducer.send(Message)
   * MessagingConsumer.receive(long timeout)
   * 
   * 1. Create Session with SESSION_TRANSACTED. This is done in the setup()
   * routine. 2. Send x messages to a Topic. 3. Call rollback() to rollback the
   * sent messages. 4. Create a MessageConsumer to consume the messages in the
   * Topic. Should not receive any messages since the sent messages were rolled
   * back. Verify that no messages are received. 5. Send x messages to a Topic.
   * 6. Call commit() to commit the sent messages. 7. Create a MessageConsumer
   * to consume the messages in the Topic. Should receive all the messages since
   * the sent messages were committed. Verify that all messages are received.
   */
  public void commitRollbackMultiMsgsTest() throws Fault {
    boolean pass = true;
    int numMessages = 3;
    try {
      TextMessage tempMsg = null;

      // set up JmsTool for COMMON_TTX setup
      tool = new JmsTool(JmsTool.COMMON_TTX, jmsUser, jmsPassword, mode);
      Destination destination = tool.getDefaultDestination();
      Session session = tool.getDefaultSession();
      Connection connection = tool.getDefaultConnection();
      MessageProducer producer = tool.getDefaultProducer();
      MessageConsumer consumer = tool.getDefaultConsumer();
      Topic topic = (Topic) destination;
      connection.start();

      // Send "numMessages" messages to Topic and call rollback
      TestUtil.logMsg(
          "Send " + numMessages + " messages to Topic and call rollback()");
      for (int i = 1; i <= numMessages; i++) {
        tempMsg = session.createTextMessage("Message " + i);
        tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
            "commitRollbackMultiMsgsTest" + i);
        producer.send(tempMsg);
        TestUtil.logMsg("Message " + i + " sent");
      }

      session.rollback();

      TestUtil.logMsg(
          "Should not consume any messages in Topic since rollback() was called");
      tempMsg = (TextMessage) consumer.receive(timeout);
      if (tempMsg != null) {
        TestUtil.logErr("Received message " + tempMsg.getText()
            + ", expected NULL (NO MESSAGES)");
        pass = false;
      }

      // Send "numMessages" messages to Topic and call commit
      TestUtil.logMsg(
          "Send " + numMessages + " messages to Topic and call commit()");
      for (int i = 1; i <= numMessages; i++) {
        tempMsg = session.createTextMessage("Message " + i);
        tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
            "commitRollbackMultiMsgsTest" + i);
        producer.send(tempMsg);
        TestUtil.logMsg("Message " + i + " sent");
      }

      session.commit();

      TestUtil.logMsg(
          "Should consume all messages in Topic since commit() was called");
      for (int msgCount = 1; msgCount <= numMessages; msgCount++) {
        tempMsg = (TextMessage) consumer.receive(timeout);
        if (tempMsg == null) {
          TestUtil.logErr("MessageConsumer.receive() returned NULL");
          TestUtil.logErr("Message " + msgCount + " missing from Topic");
          pass = false;
        } else if (!tempMsg.getText().equals("Message " + msgCount)) {
          TestUtil.logErr("Received [" + tempMsg.getText()
              + "] expected [Message " + msgCount + "]");
          pass = false;
        } else {
          TestUtil.logMsg("Received message: " + tempMsg.getText());
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("commitRollbackMultiMsgsTest");
    }

    if (!pass) {
      throw new Fault("commitRollbackMultiMsgsTest failed");
    }
  }
}
