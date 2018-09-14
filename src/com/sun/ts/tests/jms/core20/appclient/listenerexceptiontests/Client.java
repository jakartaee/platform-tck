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
package com.sun.ts.tests.jms.core20.appclient.listenerexceptiontests;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import javax.jms.*;
import java.io.*;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import com.sun.javatest.Status;

public class Client extends ServiceEETest {
  private static final String testName = "com.sun.ts.tests.jms.core20.appclient.listenerexceptiontests.Client";

  private static final String testDir = System.getProperty("user.dir");

  private static final long serialVersionUID = 1L;

  // JMS tool which creates and/or looks up the JMS administered objects
  private transient JmsTool tool = null;

  // JMS objects
  private transient ConnectionFactory cf = null;

  private transient Queue queue = null;

  private transient Session session = null;

  private transient Destination destination = null;

  private transient Connection connection = null;

  private transient MessageConsumer consumer = null;

  private transient MessageProducer producer = null;

  private transient JMSContext context = null;

  private transient JMSContext contextToSendMsg = null;

  private transient JMSContext contextToCreateMsg = null;

  private transient JMSConsumer jmsconsumer = null;

  private transient JMSProducer jmsproducer = null;

  // Harness req's
  private Properties props = null;

  // properties read from ts.jte file
  long timeout;

  String user;

  String password;

  String mode;

  // used for tests
  ArrayList queues = null;

  ArrayList connections = null;

  boolean jmscontextTest = false;

  /* Run test in standalone mode */

  /**
   * Main method is used when not run from the JavaTest GUI.
   * 
   * @param args
   */
  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * Utility methods
   */
  private void setupForQueue2() throws Fault {
    try {
      // set up JmsTool for COMMON_Q setup
      TestUtil.logMsg("Setup JmsTool for COMMON_Q setup");
      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      cf = tool.getConnectionFactory();
      destination = tool.getDefaultDestination();
      queue = (Queue) destination;
      tool.getDefaultConnection().close();
      TestUtil.logMsg("Create JMSContext with AUTO_ACKNOWLEDGE");
      context = cf.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
      contextToSendMsg = cf.createContext(user, password,
          JMSContext.AUTO_ACKNOWLEDGE);
      contextToCreateMsg = cf.createContext(user, password,
          JMSContext.AUTO_ACKNOWLEDGE);
      jmsconsumer = context.createConsumer(destination);
      jmsproducer = contextToSendMsg.createProducer();
      jmscontextTest = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e);
      throw new Fault("setupForQueue2 failed!", e);
    }
  }

  private void setupForQueue() throws Fault {
    try {
      // set up JmsTool for COMMON_Q setup
      TestUtil.logMsg("Setup JmsTool for COMMON_Q setup");
      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      cf = tool.getConnectionFactory();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      consumer = tool.getDefaultConsumer();
      producer = tool.getDefaultProducer();
      destination = tool.getDefaultDestination();
      queue = (Queue) destination;
      connection.start();
      jmscontextTest = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e);
      throw new Fault("setupForQueue failed!", e);
    }
  }

  private void setupForQueueWithMultipleSessions() throws Fault {
    Connection newConnection = null;
    Session newSession = null;
    boolean transacted = false;
    try {
      // set up JmsTool for COMMON_Q setup
      TestUtil.logMsg("Setup JmsTool for COMMON_Q setup");
      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      cf = tool.getConnectionFactory();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      producer = tool.getDefaultProducer();
      destination = tool.getDefaultDestination();
      queue = (Queue) destination;

      // Create a consumer that uses new connection and new session
      newConnection = tool.getNewConnection(JmsTool.QUEUE, user, password);
      newSession = newConnection.createSession(transacted,
          Session.AUTO_ACKNOWLEDGE);
      consumer = newSession.createConsumer(destination);
      connection.start();
      jmscontextTest = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e);
      throw new Fault("setupForQueue failed!", e);
    }
  }

  /* Test setup: */

  /*
   * setup() is called before each test
   * 
   * @class.setup_props: jms_timeout; user; password; platform.mode;
   * 
   * @exception Fault
   */
  public void setup(String[] args, Properties p) throws Fault {
    try {
      // get props
      timeout = Long.parseLong(p.getProperty("jms_timeout"));
      user = p.getProperty("user");
      password = p.getProperty("password");
      mode = p.getProperty("platform.mode");

      // check props for errors
      if (timeout < 1) {
        throw new Exception(
            "'jms_timeout' (milliseconds) in ts.jte must be > 0");
      }
      if (user == null) {
        throw new Exception("'user' in ts.jte must not be null ");
      }
      if (password == null) {
        throw new Exception("'password' in ts.jte must not be null ");
      }
      if (mode == null) {
        throw new Exception("'platform.mode' in ts.jte must not be null");
      }
      queues = new ArrayList(3);
      connections = new ArrayList(5);
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e);
      throw new Fault("setup failed!", e);
    }
  }

  /* cleanup */

  /*
   * cleanup() is called after each test
   * 
   * @exception Fault
   */
  public void cleanup() throws Fault {
    try {
      TestUtil.logMsg("Close Consumer objects");
      if (jmscontextTest) {
        if (jmsconsumer != null) {
          jmsconsumer.close();
          jmsconsumer = null;
        }
        TestUtil.logMsg("Close JMSContext objects");
        if (context != null) {
          context.close();
          context = null;
        }
        if (contextToSendMsg != null) {
          contextToSendMsg.close();
          contextToSendMsg = null;
        }
        if (contextToCreateMsg != null) {
          contextToCreateMsg.close();
          contextToCreateMsg = null;
        }
      } else {
        if (consumer != null) {
          consumer.close();
          consumer = null;
        }
      }
      TestUtil.logMsg("Flush any messages left on Queue");
      tool.flushDestination();
      tool.closeAllResources();
      tool.getDefaultConnection().close();
      producer = null;
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e);
      throw new Fault("cleanup failed!", e);
    }
  }

  /*
   * @testName: illegalStateExceptionTest1
   * 
   * @assertion_ids: JMS:JAVADOC:1351;
   * 
   * @test_Strategy: Calling Connection.close() from a CompletionListener MUST
   * throw IllegalStateException.
   */
  public void illegalStateExceptionTest1() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg(
          "Testing Connection.close() from CompletionListener (expect IllegalStateException)");
      try {
        // Setup for QUEUE
        setupForQueue();

        // Create CompetionListener
        MyCompletionListener listener = new MyCompletionListener(connection);

        // Create TextMessage
        TestUtil.logMsg("Creating TextMessage");
        TextMessage expTextMessage = session
            .createTextMessage("Call connection close method");
        TestUtil.logMsg("Set some values in TextMessage");
        expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
            "illegalStateExceptionTest1");

        TestUtil.logMsg(
            "Send async message specifying CompletionListener to recieve async message");
        TestUtil.logMsg(
            "CompletionListener will call Connection.close() (expect IllegalStateException)");
        producer.send(expTextMessage, listener);
        TextMessage actTextMessage = null;
        TestUtil.logMsg("Poll listener until we receive exception");
        for (int i = 0; i < 30; i++) {
          if (listener.isComplete()) {
            listener.setComplete(false);
            break;
          } else {
            TestUtil.sleepSec(2);
          }
        }
        TestUtil.logMsg(
            "Check if we got correct exception from Connection.close()");
        if (listener.gotException()) {
          if (listener.gotCorrectException()) {
            TestUtil.logMsg("Got correct IllegalStateException");
          } else {
            TestUtil.logErr("Expected IllegalStateException, received: "
                + listener.getException());
            pass = false;
          }
        } else {
          TestUtil.logErr("Expected IllegalStateException, got no exception");
          pass = false;
        }
      } catch (javax.jms.IllegalStateException e) {
        TestUtil.logMsg("Caught IllegalStateException as expected");
      } catch (Exception e) {
        TestUtil.logErr("Expected IllegalStateException, received " + e);
        TestUtil.printStackTrace(e);
        pass = false;
      }

      try {
        cleanup();
      } catch (Exception e) {
        TestUtil.logMsg("Exception during cleanup: " + e);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e);
      throw new Fault("illegalStateExceptionTest1", e);
    }

    if (!pass) {
      throw new Fault("illegalStateExceptionTest1 failed");
    }
  }

  /*
   * @testName: illegalStateExceptionTest2
   * 
   * @assertion_ids: JMS:JAVADOC:1351; JMS:JAVADOC:1352;
   * 
   * @test_Strategy: Calling Connection.close() or Connection.stop() from a
   * MessageListener MUST throw IllegalStateException.
   */
  public void illegalStateExceptionTest2() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "Testing Connection.close() from MessageListener (expect IllegalStateException)");
      try {
        // Setup for QUEUE
        setupForQueue();

        // Create MessageListener
        MyMessageListener listener = new MyMessageListener(connection);

        // Create TextMessage
        TestUtil.logMsg("Creating TextMessage");
        TextMessage expTextMessage = session
            .createTextMessage("Call connection close method");
        TestUtil.logMsg("Set some values in TextMessage");
        expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
            "illegalStateExceptionTest2");

        TestUtil.logMsg("Set MessageListener to receive async message");
        consumer.setMessageListener(listener);

        TestUtil.logMsg("Send async message to MessageListener");
        TestUtil.logMsg(
            "MessageListener will call Connection.close() (expect IllegalStateException)");
        producer.send(expTextMessage);
        TextMessage actTextMessage = null;
        TestUtil.logMsg("Poll listener until we receive exception");
        for (int i = 0; i < 30; i++) {
          if (listener.isComplete()) {
            listener.setComplete(false);
            break;
          } else {
            TestUtil.sleepSec(2);
          }
        }
        TestUtil.logMsg(
            "Check if we got correct exception from Connection.close()");
        if (listener.gotException()) {
          if (listener.gotCorrectException()) {
            TestUtil.logMsg("Got correct IllegalStateException");
          } else {
            TestUtil.logErr("Expected IllegalStateException, received: "
                + listener.getException());
            pass = false;
          }
        } else {
          TestUtil.logErr("Expected IllegalStateException, got no exception");
          pass = false;
        }
      } catch (javax.jms.IllegalStateException e) {
        TestUtil.logMsg("Caught IllegalStateException as expected");
      } catch (Exception e) {
        TestUtil.logErr("Expected IllegalStateException, received " + e);
        TestUtil.printStackTrace(e);
        pass = false;
      }

      try {
        cleanup();
      } catch (Exception e) {
        TestUtil.logMsg("Exception during cleanup: " + e);
      }

      TestUtil.logMsg(
          "Testing Connection.stop() from MessageListener (expect IllegalStateException)");
      try {
        // Setup for QUEUE
        setupForQueue();

        // Create MessageListener
        MyMessageListener listener = new MyMessageListener(connection);

        // Create TextMessage
        TestUtil.logMsg("Creating TextMessage");
        TextMessage expTextMessage = session
            .createTextMessage("Call connection stop method");
        TestUtil.logMsg("Set some values in TextMessage");
        expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
            "illegalStateExceptionTest2");

        TestUtil.logMsg("Set MessageListener to receive async message");
        consumer.setMessageListener(listener);

        TestUtil.logMsg("Send async message to MessageListener");
        TestUtil.logMsg(
            "MessageListener will call Connection.stop() (expect IllegalStateException)");
        producer.send(expTextMessage);
        TextMessage actTextMessage = null;
        TestUtil.logMsg("Poll listener until we receive exception");
        for (int i = 0; i < 30; i++) {
          if (listener.isComplete()) {
            listener.setComplete(false);
            break;
          } else {
            TestUtil.sleepSec(2);
          }
        }
        TestUtil
            .logMsg("Check if we got correct exception from Connection.stop()");
        if (listener.gotException()) {
          if (listener.gotCorrectException()) {
            TestUtil.logMsg("Got correct IllegalStateException");
          } else {
            TestUtil.logErr("Expected IllegalStateException, received: "
                + listener.getException());
            pass = false;
          }
        } else {
          TestUtil.logErr("Expected IllegalStateException, got no exception");
          pass = false;
        }
      } catch (javax.jms.IllegalStateException e) {
        TestUtil.logMsg("Caught IllegalStateException as expected");
      } catch (Exception e) {
        TestUtil.logErr("Expected IllegalStateException, received " + e);
        TestUtil.printStackTrace(e);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e);
      throw new Fault("illegalStateExceptionTest2", e);
    }

    if (!pass) {
      throw new Fault("illegalStateExceptionTest2 failed");
    }
  }

  /*
   * @testName: illegalStateExceptionTest3
   * 
   * @assertion_ids: JMS:JAVADOC:1356;
   * 
   * @test_Strategy: Calling Session.close() from a CompletionListener or
   * MessageListener MUST throw IllegalStateException.
   */
  public void illegalStateExceptionTest3() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "Testing Session.close() from CompletionListener (expect IllegalStateException)");
      try {
        // Setup for QUEUE
        setupForQueue();

        // Create CompetionListener
        MyCompletionListener listener = new MyCompletionListener(session);

        // Create TextMessage
        TestUtil.logMsg("Creating TextMessage");
        TextMessage expTextMessage = session
            .createTextMessage("Call session close method");
        TestUtil.logMsg("Set some values in TextMessage");
        expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
            "illegalStateExceptionTest3");

        TestUtil.logMsg("Send message specifying CompletionListener");
        TestUtil.logMsg(
            "CompletionListener will call Session.close() (expect IllegalStateException)");
        producer.send(expTextMessage, listener);
        TextMessage actTextMessage = null;
        TestUtil.logMsg("Poll listener until we receive exception");
        for (int i = 0; i < 30; i++) {
          if (listener.isComplete()) {
            listener.setComplete(false);
            break;
          } else {
            TestUtil.sleepSec(2);
          }
        }
        TestUtil
            .logMsg("Check if we got correct exception from Session.close()");
        if (listener.gotException()) {
          if (listener.gotCorrectException()) {
            TestUtil.logMsg("Got correct IllegalStateException");
          } else {
            TestUtil.logErr("Expected IllegalStateException, received: "
                + listener.getException());
            pass = false;
          }
        } else {
          TestUtil.logErr("Expected IllegalStateException, got no exception");
          pass = false;
        }
      } catch (javax.jms.IllegalStateException e) {
        TestUtil.logMsg("Caught IllegalStateException as expected");
      } catch (Exception e) {
        TestUtil.logErr("Expected IllegalStateException, received " + e);
        TestUtil.printStackTrace(e);
        pass = false;
      }

      try {
        cleanup();
      } catch (Exception e) {
        TestUtil.logMsg("Exception during cleanup: " + e);
      }

      TestUtil.logMsg(
          "Testing Session.close() from MessageListener (expect IllegalStateException)");
      try {
        // Setup for QUEUE
        setupForQueue();

        // Create MessageListener
        MyMessageListener listener = new MyMessageListener(session);

        // Create TextMessage
        TestUtil.logMsg("Creating TextMessage");
        TextMessage expTextMessage = session
            .createTextMessage("Call session close method");
        TestUtil.logMsg("Set some values in TextMessage");
        expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
            "illegalStateExceptionTest6");

        TestUtil.logMsg("Set MessageListener to receive async message");
        consumer.setMessageListener(listener);

        TestUtil.logMsg("Send async message to MessageListener");
        TestUtil.logMsg(
            "MessageListener will call Session.close() (expect IllegalStateException)");
        producer.send(expTextMessage);
        TextMessage actTextMessage = null;
        TestUtil.logMsg("Poll listener until we receive exception");
        for (int i = 0; i < 30; i++) {
          if (listener.isComplete()) {
            listener.setComplete(false);
            break;
          } else {
            TestUtil.sleepSec(2);
          }
        }
        TestUtil
            .logMsg("Check if we got correct exception from Session.close()");
        if (listener.gotException()) {
          if (listener.gotCorrectException()) {
            TestUtil.logMsg("Got correct IllegalStateException");
          } else {
            TestUtil.logErr("Expected IllegalStateException, received: "
                + listener.getException());
            pass = false;
          }
        } else {
          TestUtil.logErr("Expected IllegalStateException, got no exception");
          pass = false;
        }
      } catch (javax.jms.IllegalStateException e) {
        TestUtil.logMsg("Caught IllegalStateException as expected");
      } catch (Exception e) {
        TestUtil.logErr("Expected IllegalStateException, received " + e);
        TestUtil.printStackTrace(e);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e);
      throw new Fault("illegalStateExceptionTest3", e);
    }

    if (!pass) {
      throw new Fault("illegalStateExceptionTest3 failed");
    }
  }

  /*
   * @testName: callingMessageConsumerCloseIsAllowed
   * 
   * @assertion_ids: JMS:JAVADOC:338;
   * 
   * @test_Strategy: Calling MessageConsumer.close() from a MessageListener is
   * allowed.
   */
  public void callingMessageConsumerCloseIsAllowed() throws Fault {
    boolean pass = true;
    try {

      try {
        // Setup for QUEUE
        setupForQueueWithMultipleSessions();

        // Create MessageListener
        MyMessageListener listener = new MyMessageListener(consumer);

        // Create TextMessage
        TestUtil.logMsg("Creating TextMessage");
        // TextMessage expTextMessage = session
        // .createTextMessage("Call MessageConsumer close method");
        TextMessage expTextMessage = new TextMessageTestImpl();
        expTextMessage.setText("Call MessageConsumer close method");

        TestUtil.logMsg("Set some values in TextMessage");
        expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
            "callingMessageConsumerCloseIsAllowed");

        TestUtil.logMsg("Set MessageListener to receive async message");
        consumer.setMessageListener(listener);

        TestUtil.logMsg("Send async message to MessageListener");
        TestUtil.logMsg(
            "MessageListener will call MessageConsumer.close() which is allowed");
        producer.send(expTextMessage);
        TextMessage actTextMessage = null;
        TestUtil.logMsg("Poll listener until complete");
        for (int i = 0; i < 30; i++) {
          if (listener.isComplete()) {
            listener.setComplete(false);
            break;
          } else {
            TestUtil.sleepSec(2);
          }
        }
        TestUtil.logMsg("Make sure MessageConsumer.close() was allowed");
        if (!listener.gotException()) {
          TestUtil.logMsg("MessageConsumer.close() was allowed");
        } else {
          TestUtil.logErr("MessageConsumer.close() through an exception");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: " + e);
        TestUtil.printStackTrace(e);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e);
      throw new Fault("callingMessageConsumerCloseIsAllowed", e);
    }

    if (!pass) {
      throw new Fault("callingMessageConsumerCloseIsAllowed failed");
    }
  }

  /*
   * @testName: callingJMSConsumerCloseIsAllowed
   * 
   * @assertion_ids: JMS:JAVADOC:1098;
   * 
   * @test_Strategy: Calling JMSConsumer.close() from a MessageListsner is
   * allowed.
   */
  public void callingJMSConsumerCloseIsAllowed() throws Fault {
    boolean pass = true;
    try {

      try {
        // Setup for QUEUE
        setupForQueue2();

        // Create MessageListener
        MyMessageListener listener = new MyMessageListener(jmsconsumer);

        // Create TextMessage
        TestUtil.logMsg("Creating TextMessage");
        TextMessage expTextMessage = contextToCreateMsg
            .createTextMessage("Call JMSConsumer close method");
        TestUtil.logMsg("Set some values in TextMessage");
        expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
            "callingJMSConsumerCloseIsAllowed");

        TestUtil.logMsg("Set MessageListener to receive async message");
        jmsconsumer.setMessageListener(listener);

        TestUtil.logMsg("Send async message to MessageListener");
        TestUtil.logMsg(
            "MessageListener will call JMSConsumer.close() which is allowed");
        jmsproducer.send(destination, expTextMessage);
        TextMessage actTextMessage = null;
        TestUtil.logMsg("Poll listener until complete");
        for (int i = 0; i < 30; i++) {
          if (listener.isComplete()) {
            listener.setComplete(false);
            break;
          } else {
            TestUtil.sleepSec(2);
          }
        }
        TestUtil.logMsg("Make sure JMSConsumer.close() was allowed");
        if (!listener.gotException()) {
          TestUtil.logMsg("JMSConsumer.close() was allowed");
        } else {
          TestUtil.logErr("JMSConsumer.close() through an exception");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: " + e);
        TestUtil.printStackTrace(e);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e);
      throw new Fault("callingJMSConsumerCloseIsAllowed", e);
    }

    if (!pass) {
      throw new Fault("callingJMSConsumerCloseIsAllowed failed");
    }
  }
}
