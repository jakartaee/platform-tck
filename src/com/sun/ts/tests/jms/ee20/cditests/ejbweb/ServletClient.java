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

package com.sun.ts.tests.jms.ee20.cditests.ejbweb;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.jms.common.*;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ArrayList;
import javax.transaction.*;
import javax.naming.*;
import javax.jms.*;
import javax.inject.Inject;
import javax.annotation.Resource;
import javax.enterprise.inject.*;

public class ServletClient extends HttpServlet {
  private static final long serialVersionUID = 1L;

  private Properties harnessProps = null;

  long timeout;

  String user;

  String password;

  String mode;

  ArrayList queues = null;

  ArrayList connections = null;

  private static final int numMessages = 3;

  private static final int iterations = 5;

  private static int testsExecuted = 0;

  // JMSContext CDI injection specifying QueueConnectionFactory
  @Inject
  @JMSConnectionFactory("jms/QueueConnectionFactory")
  transient JMSContext context1;

  // JMSContext CDI injection specifying TopicConnectionFactory
  @Inject
  @JMSConnectionFactory("jms/TopicConnectionFactory")
  transient JMSContext context2;

  // JMSContext CDI injection specifying ConnectionFactory,
  // Password Credentials, and Session Mode
  @Inject
  @JMSConnectionFactory("jms/ConnectionFactory")
  @JMSPasswordCredential(userName = "j2ee", password = "j2ee")
  @JMSSessionMode(JMSContext.DUPS_OK_ACKNOWLEDGE)
  transient JMSContext context3;

  // JMSContext CDI injection for default Connection Factory
  @Inject
  transient JMSContext context4;

  @Resource(name = "jms/MyConnectionFactory")
  private transient ConnectionFactory cfactory;

  @Resource(name = "jms/MyQueueConnectionFactory")
  private transient QueueConnectionFactory qcfactory;

  @Resource(name = "jms/MyTopicConnectionFactory")
  private transient TopicConnectionFactory tcfactory;

  @Resource(name = "jms/MY_QUEUE")
  private transient Queue queue;

  @Resource(name = "jms/MY_TOPIC")
  private transient Topic topic;

  @Resource(name = "mybean")
  private MyManagedBean mybean;

  /*
   * Utility method to return the session mode as a String
   */
  private String printSessionMode(int sessionMode) {
    switch (sessionMode) {
    case JMSContext.SESSION_TRANSACTED:
      return "SESSION_TRANSACTED";
    case JMSContext.AUTO_ACKNOWLEDGE:
      return "AUTO_ACKNOWLEDGE";
    case JMSContext.CLIENT_ACKNOWLEDGE:
      return "CLIENT_ACKNOWLEDGE";
    case JMSContext.DUPS_OK_ACKNOWLEDGE:
      return "DUPS_OK_ACKNOWLEDGE";
    default:
      return "UNEXPECTED_SESSIONMODE";
    }
  }

  private void cleanup() {
    TestUtil.logMsg("cleanup");
  }

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    System.out.println("ServletClient:init() Entering");
    System.out.println("queue=" + queue);
    System.out.println("topic=" + topic);
    System.out.println("cfactory=" + cfactory);
    System.out.println("qcfactory=" + qcfactory);
    System.out.println("tcfactory=" + tcfactory);
    System.out.println("mybean=" + mybean);
    if (queue == null || topic == null || context1 == null || context2 == null
        || context3 == null || context4 == null || cfactory == null
        || qcfactory == null || tcfactory == null || mybean == null) {
      throw new ServletException("init() failed: port injection failed");
    }
    System.out.println("ServletClient:init() Leaving");
  }

  public void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    boolean pass = true;
    Properties p = new Properties();
    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    try {
      String test = harnessProps.getProperty("TEST");
      System.out.println("doGet: test to execute is: " + test);
      if (test.equals("sendRecvQueueTestUsingCDIFromServlet")) {
        if (sendRecvQueueTestUsingCDIFromServlet())
          p.setProperty("TESTRESULT", "pass");
        else
          p.setProperty("TESTRESULT", "fail");
      } else if (test.equals("sendRecvTopicTestUsingCDIFromServlet")) {
        if (sendRecvTopicTestUsingCDIFromServlet())
          p.setProperty("TESTRESULT", "pass");
        else
          p.setProperty("TESTRESULT", "fail");
      } else if (test.equals("sendRecvUsingCDIDefaultFactoryFromServlet")) {
        if (sendRecvUsingCDIDefaultFactoryFromServlet())
          p.setProperty("TESTRESULT", "pass");
        else
          p.setProperty("TESTRESULT", "fail");
      } else if (test.equals("verifySessionModeOnCDIJMSContextFromServlet")) {
        if (verifySessionModeOnCDIJMSContextFromServlet())
          p.setProperty("TESTRESULT", "pass");
        else
          p.setProperty("TESTRESULT", "fail");
      } else if (test.equals("testRestrictionsOnCDIJMSContextFromServlet")) {
        if (testRestrictionsOnCDIJMSContextFromServlet())
          p.setProperty("TESTRESULT", "pass");
        else
          p.setProperty("TESTRESULT", "fail");
      } else if (test.equals("sendRecvQueueTestUsingCDIFromManagedBean")) {
        if (sendRecvQueueTestUsingCDIFromManagedBean())
          p.setProperty("TESTRESULT", "pass");
        else
          p.setProperty("TESTRESULT", "fail");
      } else if (test.equals("sendRecvTopicTestUsingCDIFromManagedBean")) {
        if (sendRecvTopicTestUsingCDIFromManagedBean())
          p.setProperty("TESTRESULT", "pass");
        else
          p.setProperty("TESTRESULT", "fail");
      } else {
        p.setProperty("TESTRESULT", "fail");
      }
      cleanup();
      p.list(out);
    } catch (Exception e) {
      TestUtil.logErr("doGet: Exception: " + e);
      System.out.println("doGet: Exception: " + e);
      p.setProperty("TESTRESULT", "fail");
      p.list(out);
    }
    out.close();
  }

  public void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    harnessProps = new Properties();
    Enumeration enumlist = req.getParameterNames();
    while (enumlist.hasMoreElements()) {
      String name = (String) enumlist.nextElement();
      String value = req.getParameter(name);
      harnessProps.setProperty(name, value);
    }

    try {
      TestUtil.init(harnessProps);
      mybean.init(harnessProps);
      timeout = Long.parseLong(harnessProps.getProperty("jms_timeout"));
      user = harnessProps.getProperty("user");
      password = harnessProps.getProperty("password");
    } catch (Exception e) {
      System.out.println("doPost: Exception: " + e);
      e.printStackTrace();
      throw new ServletException("unable to initialize remote logging");
    }
    doGet(req, res);
    harnessProps = null;
  }

  public boolean sendRecvQueueTestUsingCDIFromServlet() {
    boolean pass = true;
    JMSConsumer consumer = null;
    System.out.println("sendRecvQueueTestUsingCDIFromServlet");
    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;

      TestUtil.logMsg(
          "Using CDI injected context1 specifying QueueConnectionFactory");

      // Create JMSConsumer from JMSContext
      consumer = context1.createConsumer(queue);

      TestUtil.logMsg("Creating TextMessage");
      messageSent = context1.createTextMessage();
      messageSent.setText("just a test");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "sendRecvQueueTestUsingCDIFromServlet");
      TestUtil.logMsg("Sending TextMessage");
      context1.createProducer().send(queue, messageSent);
      TestUtil.logMsg("Receiving TextMessage");
      messageReceived = (TextMessage) consumer.receive(timeout);

      // Check to see if correct message received
      if (messageReceived == null) {
        TestUtil.logErr("No message was received");
        pass = false;
      } else {
        TestUtil.logMsg("Message Sent: \"" + messageSent.getText() + "\"");
        TestUtil
            .logMsg("Message Received: \"" + messageReceived.getText() + "\"");
        if (messageReceived.getText().equals(messageSent.getText())) {
          TestUtil.logMsg("Received correct message");
        } else {
          TestUtil.logErr("Received incorrect message");
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      pass = false;
    } finally {
      try {
        consumer.receive(timeout);
        while (consumer.receiveNoWait() != null)
          ;
        consumer.close();
      } catch (Exception e) {
      }
    }
    return pass;
  }

  public boolean sendRecvTopicTestUsingCDIFromServlet() {
    boolean pass = true;
    JMSConsumer consumer = null;
    System.out.println("sendRecvTopicTestUsingCDIFromServlet");
    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;

      TestUtil.logMsg(
          "Using CDI injected context2 specifying TopicConnectionFactory");

      // Create JMSConsumer from JMSContext
      consumer = context2.createConsumer(topic);

      TestUtil.logMsg("Creating TextMessage");
      messageSent = context2.createTextMessage();
      messageSent.setText("just a test");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "sendRecvTopicTestUsingCDIFromServlet");
      TestUtil.logMsg("Sending TextMessage");
      context2.createProducer().send(topic, messageSent);
      TestUtil.logMsg("Receiving TextMessage");
      messageReceived = (TextMessage) consumer.receive(timeout);

      // Check to see if correct message received
      if (messageReceived == null) {
        TestUtil.logErr("No message was received");
        pass = false;
      } else {
        TestUtil.logMsg("Message Sent: \"" + messageSent.getText() + "\"");
        TestUtil
            .logMsg("Message Received: \"" + messageReceived.getText() + "\"");
        if (messageReceived.getText().equals(messageSent.getText())) {
          TestUtil.logMsg("Received correct message");
        } else {
          TestUtil.logErr("Received incorrect message");
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      pass = false;
    } finally {
      try {
        consumer.close();
      } catch (Exception e) {
      }
    }
    return pass;
  }

  public boolean sendRecvUsingCDIDefaultFactoryFromServlet() {
    boolean pass = true;
    JMSConsumer consumer = null;
    JMSConsumer consumer2 = null;
    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      TextMessage messageReceived2 = null;

      TestUtil.logMsg(
          "Using CDI injected context4 using default system connection factory");

      // Create JMSConsumer from JMSContext for Queue
      TestUtil.logMsg("Creating Consumer for Queue");
      consumer = context4.createConsumer(queue);

      // Create JMSConsumer from JMSContext for Topic
      TestUtil.logMsg("Creating Consumer for Topic");
      consumer2 = context4.createConsumer(topic);

      TestUtil.logMsg("Creating TextMessage");
      messageSent = context4.createTextMessage();
      messageSent.setText("just a test");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "sendRecvUsingCDIDefaultFactoryFromServlet");
      TestUtil.logMsg("Sending TextMessage to Queue");
      context4.createProducer().send(queue, messageSent);
      TestUtil.logMsg("Sending TextMessage to Topic");
      context4.createProducer().send(topic, messageSent);
      TestUtil.logMsg("Receiving TextMessage from Queue consumer");
      messageReceived = (TextMessage) consumer.receive(timeout);
      TestUtil.logMsg("Receiving TextMessage from Topic consumer");
      messageReceived2 = (TextMessage) consumer2.receive(timeout);

      // Check to see if correct message received from Queue consumer
      TestUtil.logMsg("Check received message from Queue consumer");
      if (messageReceived == null) {
        TestUtil.logErr("No message was received");
        pass = false;
      } else {
        TestUtil.logMsg("Message Sent: \"" + messageSent.getText() + "\"");
        TestUtil
            .logMsg("Message Received: \"" + messageReceived.getText() + "\"");
        if (messageReceived.getText().equals(messageSent.getText())) {
          TestUtil.logMsg("Received correct message");
        } else {
          TestUtil.logErr("Received incorrect message");
          pass = false;
        }
      }

      // Check to see if correct message received from Queue consumer
      TestUtil.logMsg("Check received message from Topic consumer");
      if (messageReceived2 == null) {
        TestUtil.logErr("No message was received");
        pass = false;
      } else {
        TestUtil.logMsg("Message Sent: \"" + messageSent.getText() + "\"");
        TestUtil
            .logMsg("Message Received: \"" + messageReceived2.getText() + "\"");
        if (messageReceived2.getText().equals(messageSent.getText())) {
          TestUtil.logMsg("Received correct message");
        } else {
          TestUtil.logErr("Received incorrect message");
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      pass = false;
    } finally {
      try {
        consumer.receive(timeout);
        while (consumer.receiveNoWait() != null)
          ;
        consumer.close();
        consumer2.close();
      } catch (Exception e) {
      }
    }
    return pass;
  }

  public boolean verifySessionModeOnCDIJMSContextFromServlet() {
    boolean pass = true;
    System.out.println("verifySessionModeOnCDIJMSContextFromServlet");
    try {
      TestUtil.logMsg("Checking session mode of context3 should be "
          + printSessionMode(JMSContext.DUPS_OK_ACKNOWLEDGE));
      if (context3.getSessionMode() != JMSContext.DUPS_OK_ACKNOWLEDGE) {
        TestUtil.logErr("Incorrect session mode returned: "
            + printSessionMode(context3.getSessionMode()) + "  expected: "
            + printSessionMode(JMSContext.DUPS_OK_ACKNOWLEDGE));
        pass = false;
      } else {
        TestUtil.logMsg("Returned correct session mode: "
            + printSessionMode(JMSContext.DUPS_OK_ACKNOWLEDGE));
      }

      TestUtil.logMsg("Checking session mode of context2 should be "
          + printSessionMode(JMSContext.AUTO_ACKNOWLEDGE));
      if (context2.getSessionMode() != JMSContext.AUTO_ACKNOWLEDGE) {
        TestUtil.logErr("Incorrect session mode returned: "
            + printSessionMode(context2.getSessionMode()) + "  expected: "
            + printSessionMode(JMSContext.AUTO_ACKNOWLEDGE));
        pass = false;
      } else {
        TestUtil.logMsg("Returned correct session mode: "
            + printSessionMode(JMSContext.AUTO_ACKNOWLEDGE));
      }

      TestUtil.logMsg("Checking session mode of context1 should be "
          + printSessionMode(JMSContext.AUTO_ACKNOWLEDGE));
      if (context1.getSessionMode() != JMSContext.AUTO_ACKNOWLEDGE) {
        TestUtil.logErr("Incorrect session mode returned: "
            + printSessionMode(context1.getSessionMode()) + "  expected: "
            + printSessionMode(JMSContext.AUTO_ACKNOWLEDGE));
        pass = false;
      } else {
        TestUtil.logMsg("Returned correct session mode: "
            + printSessionMode(JMSContext.AUTO_ACKNOWLEDGE));
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      pass = false;
    }
    return pass;
  }

  public boolean testRestrictionsOnCDIJMSContextFromServlet() {
    boolean pass = true;
    System.out.println("testRestrictionsOnCDIJMSContextFromServlet");
    try {
      TestUtil.logMsg(
          "Calling JMSContext.acknowledge() MUST throw IllegalStateRuntimeException");
      try {
        context1.acknowledge();
      } catch (IllegalStateRuntimeException e) {
        TestUtil.logMsg("Caught expected IllegalStateRuntimeException");
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected Exception: " + e);
        pass = false;
      }
      TestUtil.logMsg(
          "Calling JMSContext.setClientID(String) MUST throw IllegalStateRuntimeException");
      try {
        context1.setClientID("test");
      } catch (IllegalStateRuntimeException e) {
        TestUtil.logMsg("Caught expected IllegalStateRuntimeException");
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected Exception: " + e);
        pass = false;
      }
      TestUtil.logMsg(
          "Calling JMSContext.setExceptionListener(ExceptionListener) MUST throw IllegalStateRuntimeException");
      try {
        context1.setExceptionListener(null);
      } catch (IllegalStateRuntimeException e) {
        TestUtil.logMsg("Caught expected IllegalStateRuntimeException");
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected Exception: " + e);
        pass = false;
      }
      TestUtil.logMsg(
          "Calling JMSContext.start() MUST throw IllegalStateRuntimeException");
      try {
        context1.start();
      } catch (IllegalStateRuntimeException e) {
        TestUtil.logMsg("Caught expected IllegalStateRuntimeException");
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected Exception: " + e);
        pass = false;
      }
      TestUtil.logMsg(
          "Calling JMSContext.stop() MUST throw IllegalStateRuntimeException");
      try {
        context1.stop();
      } catch (IllegalStateRuntimeException e) {
        TestUtil.logMsg("Caught expected IllegalStateRuntimeException");
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected Exception: " + e);
        pass = false;
      }
      TestUtil.logMsg(
          "Calling JMSContext.commit() MUST throw IllegalStateRuntimeException");
      try {
        context1.commit();
      } catch (IllegalStateRuntimeException e) {
        TestUtil.logMsg("Caught expected IllegalStateRuntimeException");
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected Exception: " + e);
        pass = false;
      }
      TestUtil.logMsg(
          "Calling JMSContext.rollback() MUST throw IllegalStateRuntimeException");
      try {
        context1.rollback();
      } catch (IllegalStateRuntimeException e) {
        TestUtil.logMsg("Caught expected IllegalStateRuntimeException");
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected Exception: " + e);
        pass = false;
      }
      TestUtil.logMsg(
          "Calling JMSContext.recover() MUST throw IllegalStateRuntimeException");
      try {
        context1.recover();
      } catch (IllegalStateRuntimeException e) {
        TestUtil.logMsg("Caught expected IllegalStateRuntimeException");
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected Exception: " + e);
        pass = false;
      }
      TestUtil.logMsg(
          "Calling JMSContext.setAutoStart(boolean) MUST throw IllegalStateRuntimeException");
      try {
        context1.setAutoStart(true);
      } catch (IllegalStateRuntimeException e) {
        TestUtil.logMsg("Caught expected IllegalStateRuntimeException");
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected Exception: " + e);
        pass = false;
      }
      TestUtil.logMsg(
          "Calling JMSContext.close() MUST throw IllegalStateRuntimeException");
      try {
        context1.close();
      } catch (IllegalStateRuntimeException e) {
        TestUtil.logMsg("Caught expected IllegalStateRuntimeException");
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected Exception: " + e);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      pass = false;
    }
    return pass;
  }

  public boolean sendRecvQueueTestUsingCDIFromManagedBean() {
    TestUtil.logMsg("DEBUG: sendRecvQueueTestUsingCDIFromManagedBean");
    return mybean.sendRecvQueueTestUsingCDIFromManagedBean();
  }

  public boolean sendRecvTopicTestUsingCDIFromManagedBean() {
    TestUtil.logMsg("DEBUG: sendRecvTopicTestUsingCDIFromManagedBean");
    return mybean.sendRecvTopicTestUsingCDIFromManagedBean();
  }
}
