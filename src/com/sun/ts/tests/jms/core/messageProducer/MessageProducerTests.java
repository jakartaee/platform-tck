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
package com.sun.ts.tests.jms.core.messageProducer;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import java.util.Properties;
import java.io.*;
import javax.jms.*;
import com.sun.javatest.Status;

public class MessageProducerTests extends ServiceEETest {
  private static final String testName = "com.sun.ts.tests.jms.core.messageProducer.MessageProducerTests";

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

  /**
   * Main method is used when not run from the JavaTest GUI.
   * 
   * @param args
   */
  public static void main(String[] args) {
    MessageProducerTests theTests = new MessageProducerTests();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup: */

  /*
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
      mode = p.getProperty("platform.mode");
      timeout = Long.parseLong(p.getProperty("jms_timeout"));
      if (timeout < 1) {
        throw new Exception("'timeout' (milliseconds) in ts.jte must be > 0");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("Setup failed!", e);
    }
  }

  /*
   * cleanup() is called after each test
   */
  public void cleanup() throws Fault {

    if (tool != null) {
      try {
        if (tool.getDefaultConnection() != null) {
          TestUtil.logTrace("Closing default Connection");
          tool.getDefaultConnection().close();
        }
      } catch (Exception e) {
        TestUtil.logErr("Error closing Connection in cleanup: ", e);
      }
    }
  }

  private void flushTheQueue() throws Fault {
    try {
      if (tool != null)
        if (tool.getDefaultConnection() != null)
          cleanup();

      tool = new JmsTool(JmsTool.COMMON_Q, jmsUser, jmsPassword, mode);

      TestUtil.logTrace("Closing default Connection");
      tool.getDefaultConnection().close();
    } catch (Exception e) {
      TestUtil.logErr("Error closing connection and creating JmsTool: ", e);
    } finally {
      try {
        tool.flushDestination();
      } catch (Exception e) {
        TestUtil.logErr("Error flush Destination: ", e);
      }
    }
  }

  /*
   * @testName: sendQueueTest1
   * 
   * @assertion_ids: JMS:JAVADOC:321; JMS:SPEC:253;
   * 
   * @test_Strategy: Send and receive single message using
   * MessageProducer.send(Destination, Message) and
   * MessageConsumer.receive(long). Verify message receipt.
   */

  public void sendQueueTest1() throws Fault {
    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      String testName = "sendQueueTest1";
      String testMessage = "Just a test from sendQueueTest1";
      boolean pass = true;
      MessageProducer msgproducer = null;

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.COMMON_Q, jmsUser, jmsPassword, mode);
      tool.getDefaultProducer().close();
      msgproducer = tool.getDefaultSession().createProducer((Queue) null);
      tool.getDefaultConnection().start();

      logMsg("Creating 1 message");
      messageSent = tool.getDefaultSession().createTextMessage();
      messageSent.setText(testMessage);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", testName);

      logMsg("Sending message");
      msgproducer.send(tool.getDefaultDestination(), messageSent);

      logMsg("Receiving message");
      messageReceived = (TextMessage) tool.getDefaultConsumer()
          .receive(timeout);
      if (messageReceived == null) {
        throw new Exception("didn't get any message");
      }

      // Check to see if correct message received
      if (!messageReceived.getText().equals(messageSent.getText())) {
        throw new Exception("didn't get the right message");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault(testName);
    } finally {
      try {
        flushTheQueue();
      } catch (Exception e) {
        TestUtil.logErr("Error flushing Queue", e);
      }
    }
  }

  /*
   * @testName: sendQueueTest2
   * 
   * @assertion_ids: JMS:JAVADOC:323; JMS:SPEC:253;
   * 
   * @test_Strategy: Send and receive single message using
   * MessageProducer.send(Destination, Message, int, int, long) and
   * MessageConsumer.receive(long). Verify message receipt.
   */

  public void sendQueueTest2() throws Fault {
    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      String testName = "sendQueueTest2";
      String testMessage = "Just a test from sendQueueTest2";
      boolean pass = true;
      MessageProducer msgproducer = null;

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.COMMON_Q, jmsUser, jmsPassword, mode);
      tool.getDefaultProducer().close();
      msgproducer = tool.getDefaultSession().createProducer((Queue) null);
      tool.getDefaultConnection().start();

      logMsg("Creating 1 message");
      messageSent = tool.getDefaultSession().createTextMessage();
      messageSent.setText(testMessage);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", testName);

      logMsg("Sending message");
      msgproducer.send(tool.getDefaultDestination(), messageSent,
          DeliveryMode.NON_PERSISTENT, Message.DEFAULT_PRIORITY - 1, 0L);

      logMsg("Receiving message");
      messageReceived = (TextMessage) tool.getDefaultConsumer()
          .receive(timeout);
      if (messageReceived == null) {
        logErr("didn't get any message");
        pass = false;
      } else if (!messageReceived.getText().equals(messageSent.getText())
          || messageReceived.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT
          || messageReceived
              .getJMSPriority() != (Message.DEFAULT_PRIORITY - 1)) {
        pass = false;
        logErr("didn't get the right message.");
        logErr("text =" + messageReceived.getText());
        logErr("DeliveryMode =" + messageReceived.getJMSDeliveryMode());
        logErr("Priority =" + messageReceived.getJMSPriority());
      }

      if (!pass)
        throw new Fault(testName + " falied");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault(testName);
    } finally {
      try {
        flushTheQueue();
      } catch (Exception e) {
        TestUtil.logErr("Error flushing Queue", e);
      }
    }
  }

  /*
   * @testName: sendQueueTest3
   * 
   * @assertion_ids: JMS:JAVADOC:319; JMS:JAVADOC:313; JMS:SPEC:253;
   * 
   * @test_Strategy: Send and receive single message using
   * MessageProducer.send(Message, int, int, long) and
   * MessageConsumer.receive(long). Verify message receipt.
   */

  public void sendQueueTest3() throws Fault {
    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      String testName = "sendQueueTest3";
      String testMessage = "Just a test from sendQueueTest3";
      boolean pass = true;

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.COMMON_Q, jmsUser, jmsPassword, mode);
      tool.getDefaultConnection().start();

      if (!((Queue) tool.getDefaultProducer().getDestination()).getQueueName()
          .equals(((Queue) tool.getDefaultDestination()).getQueueName())) {
        pass = false;
        logErr("getDestination test failed: "
            + ((Queue) tool.getDefaultProducer().getDestination())
                .getQueueName());
      }

      logMsg("Creating 1 message");
      messageSent = tool.getDefaultSession().createTextMessage();
      messageSent.setText(testMessage);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", testName);

      logMsg("Sending message");
      tool.getDefaultProducer().send(messageSent, DeliveryMode.NON_PERSISTENT,
          Message.DEFAULT_PRIORITY - 1, 0L);

      logMsg("Receiving message");
      messageReceived = (TextMessage) tool.getDefaultConsumer()
          .receive(timeout);
      if (messageReceived == null) {
        logErr("didn't get any message");
        pass = false;
      } else if (!messageReceived.getText().equals(messageSent.getText())
          || messageReceived.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT
          || messageReceived
              .getJMSPriority() != (Message.DEFAULT_PRIORITY - 1)) {
        pass = false;
        logErr("didn't get the right message.");
        logErr("text =" + messageReceived.getText());
        logErr("DeliveryMode =" + messageReceived.getJMSDeliveryMode());
        logErr("Priority =" + messageReceived.getJMSPriority());
      }

      if (!pass)
        throw new Fault(testName + " falied");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault(testName);
    } finally {
      try {
        flushTheQueue();
      } catch (Exception e) {
        TestUtil.logErr("Error flushing Queue", e);
      }
    }
  }

  /*
   * @testName: sendTopicTest4
   * 
   * @assertion_ids: JMS:JAVADOC:321; JMS:SPEC:253;
   * 
   * @test_Strategy: Send and receive single message using
   * MessageProducer.send(Destination, Message) and
   * MessageConsumer.receive(long). Verify message receipt.
   */

  public void sendTopicTest4() throws Fault {
    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      String testName = "sendTopicTest4";
      String testMessage = "Just a test from sendTopicTest4";
      boolean pass = true;
      MessageProducer msgproducer = null;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.COMMON_T, jmsUser, jmsPassword, mode);
      tool.getDefaultProducer().close();
      msgproducer = tool.getDefaultSession().createProducer((Topic) null);
      tool.getDefaultConnection().start();

      logMsg("Creating 1 message");
      messageSent = tool.getDefaultSession().createTextMessage();
      messageSent.setText(testMessage);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", testName);

      logMsg("Sending message");
      msgproducer.send(tool.getDefaultDestination(), messageSent);

      logMsg("Receiving message");
      messageReceived = (TextMessage) tool.getDefaultConsumer()
          .receive(timeout);
      if (messageReceived == null) {
        throw new Exception("didn't get any message");
      }

      // Check to see if correct message received
      if (!messageReceived.getText().equals(messageSent.getText())) {
        throw new Exception("didn't get the right message");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault(testName);
    }

  }

  /*
   * @testName: sendTopicTest5
   * 
   * @assertion_ids: JMS:JAVADOC:323; JMS:SPEC:253;
   * 
   * @test_Strategy: Send and receive single message using
   * MessageProducer.send(Destination, Message, int, int, long). and
   * MessageConsumer.receive(long). Verify message receipt.
   */

  public void sendTopicTest5() throws Fault {
    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      String testName = "sendTopicTest5";
      String testMessage = "Just a test from sendTopicTest5";
      boolean pass = true;
      MessageProducer msgproducer = null;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.COMMON_T, jmsUser, jmsPassword, mode);
      tool.getDefaultProducer().close();
      msgproducer = tool.getDefaultSession().createProducer((Topic) null);
      tool.getDefaultConnection().start();

      logMsg("Creating 1 message");
      messageSent = tool.getDefaultSession().createTextMessage();
      messageSent.setText(testMessage);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", testName);

      logMsg("Sending message");
      msgproducer.send(tool.getDefaultDestination(), messageSent,
          DeliveryMode.NON_PERSISTENT, Message.DEFAULT_PRIORITY - 1, 0L);

      logMsg("Receiving message");
      messageReceived = (TextMessage) tool.getDefaultConsumer()
          .receive(timeout);
      if (messageReceived == null) {
        logErr("didn't get any message");
        pass = false;
      } else if (!messageReceived.getText().equals(messageSent.getText())
          || messageReceived.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT
          || messageReceived
              .getJMSPriority() != (Message.DEFAULT_PRIORITY - 1)) {
        pass = false;
        logErr("didn't get the right message.");
        logErr("text =" + messageReceived.getText());
        logErr("DeliveryMode =" + messageReceived.getJMSDeliveryMode());
        logErr("Priority =" + messageReceived.getJMSPriority());
      }

      if (!pass)
        throw new Fault(testName + " falied");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault(testName);
    }
  }

  /*
   * @testName: sendTopicTest6
   * 
   * @assertion_ids: JMS:JAVADOC:319; JMS:JAVADOC:313; JMS:SPEC:253;
   * 
   * @test_Strategy: Send and receive single message using
   * MessageProducer.send(Message, int, int, long) and
   * MessageConsumer.receive(long). Verify message receipt.
   */

  public void sendTopicTest6() throws Fault {
    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      String testName = "sendTopicTest6";
      String testMessage = "Just a test from sendTopicTest6";
      boolean pass = true;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.COMMON_T, jmsUser, jmsPassword, mode);
      tool.getDefaultConnection().start();

      if (!((Topic) tool.getDefaultProducer().getDestination()).getTopicName()
          .equals(((Topic) tool.getDefaultDestination()).getTopicName())) {
        pass = false;
        logErr("getDestination test failed: "
            + ((Topic) tool.getDefaultProducer().getDestination())
                .getTopicName());
      }

      logMsg("Creating 1 message");
      messageSent = tool.getDefaultSession().createTextMessage();
      messageSent.setText(testMessage);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", testName);

      logMsg("Sending message");
      tool.getDefaultProducer().send(messageSent, DeliveryMode.NON_PERSISTENT,
          Message.DEFAULT_PRIORITY - 1, 0L);

      logMsg("Receiving message");
      messageReceived = (TextMessage) tool.getDefaultConsumer()
          .receive(timeout);
      if (messageReceived == null) {
        logErr("didn't get any message");
        pass = false;
      } else if (!messageReceived.getText().equals(messageSent.getText())
          || messageReceived.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT
          || messageReceived
              .getJMSPriority() != (Message.DEFAULT_PRIORITY - 1)) {
        pass = false;
        logErr("didn't get the right message.");
        logErr("text =" + messageReceived.getText());
        logErr("DeliveryMode =" + messageReceived.getJMSDeliveryMode());
        logErr("Priority =" + messageReceived.getJMSPriority());
      }

      if (!pass)
        throw new Fault(testName + " falied");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault(testName);
    }
  }
}
