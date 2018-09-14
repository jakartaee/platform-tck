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
 * $Id: TestsEJB.java 59995 2009-10-14 12:05:29Z af70133 $
 */
package com.sun.ts.tests.jms.commonee;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import javax.ejb.*;
import javax.jms.*;
import java.io.*;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.annotation.*;

@Stateful
@Remote(Tests.class)
public class TestsEJB implements Tests {

  @Resource
  private SessionContext sessionContext;

  private Properties harnessProps = null;

  private TSNamingContext nctx = null;

  private transient Destination testDestination = null;

  private transient ConnectionFactory cf = null;

  private transient Connection conn = null;

  private transient Queue queue = null;

  private transient QueueConnectionFactory qcf = null;

  private transient QueueConnection qconn = null;

  private static final String TESTQUEUENAME = "java:comp/env/jms/MY_QUEUE";

  private static final String TESTTOPICNAME = "java:comp/env/jms/MY_TOPIC";

  private static final String QUEUECONNECTIONFACTORY = "java:comp/env/jms/MyQueueConnectionFactory";

  private static final String DURABLETOPICCONNECTIONFACTORY = "java:comp/env/jms/MyTopicConnectionFactory";

  private TextMessage messageSent = null;

  private BytesMessage messageSentB = null;

  private String username = null;

  private String password = null;

  private long timeout;

  private boolean booleanValue = false;

  private byte byteValue = 127;

  private byte byteValue1 = -12;

  private int byteValue2 = 244;

  private byte[] bytesValue = { 127, -127, 1, 0 };

  private byte[] bytesValueRecvd = { 0, 0, 0, 0 };

  private byte[] byteValues = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

  private byte[] byteValues2 = { 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };

  private byte[] byteValuesReturned = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

  private byte[] byteValuesReturned2 = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

  private char charValue = 'Z';

  private double doubleValue = 6.02e23;

  private float floatValue = 6.02e23f;

  private int intValue = 2147483647;

  private long longValue = 9223372036854775807L;

  private Integer nInteger = new Integer(-2147483648);

  private short shortValue = -32768;

  private short shortValue1 = -28679;

  private int shortValue2 = 36857;

  private String utfValue = "what";

  private String stringValue = "Map Message Test";

  private String sTesting = "Testing StreamMessages";

  private String type = "JMSTCKTESTMSG";

  private String jmsCorrelationID = "JMSTCKCorrelationID";

  private int priority = 2;

  private long forever = 0L;

  public TestsEJB() {
    TestUtil.logTrace("TestsEJB => default constructor called");
  }

  @PostConstruct
  public void postConstruct() {
    TestUtil.logTrace("postConstruct");
    try {
      TestUtil.logMsg("obtain naming context");
      nctx = new TSNamingContext();
    } catch (Exception e) {
      TestUtil.logErr("Error obtaining naming context: ", e);
      throw new EJBException("postConstruct: Failed!", e);
    }
  }

  @PostActivate
  public void activate() {
    TestUtil.logTrace("activate");
    try {
      common_Q();
      setup_Q();
    } catch (Exception e) {
      TestUtil.logErr("Error during common Queue setup: ", e);
      throw new EJBException("activate: Failed!", e);
    }
  }

  @PrePassivate
  public void passivate() {
    TestUtil.logTrace("passivate");

    testDestination = null;
    cf = null;
    conn = null;
    queue = null;
    qcf = null;
    qconn = null;
  }

  @Remove
  public void remove() {
    TestUtil.logTrace("remove");
  }

  public void initLogging(Properties p) {
    TestUtil.logTrace("initLogging");
    harnessProps = p;
    try {
      TestUtil.logMsg("initialize remote logging");
      TestUtil.init(p);
      timeout = Long.parseLong(harnessProps.getProperty("jms_timeout"));
      username = harnessProps.getProperty("user");
      password = harnessProps.getProperty("password");
      // check props for errors
      if (timeout < 1) {
        throw new EJBException(
            "'jms_timeout' (milliseconds) in ts.jte must be > 0");
      }
      if (username == null) {
        throw new EJBException("'user' in ts.jte must be null");
      }
      if (password == null) {
        throw new EJBException("'password' in ts.jte must be null");
      }
      if (sessionContext == null) {
        throw new EJBException("@Resource injection failed");
      }
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("initLogging: Failed!", e);
    }
  }

  private void common_Q() throws Exception {

    TestUtil.logTrace("Getting ConnectionFactory " + QUEUECONNECTIONFACTORY);
    cf = (ConnectionFactory) nctx.lookup(QUEUECONNECTIONFACTORY);

    TestUtil.logTrace("Getting Destination " + TESTQUEUENAME);
    testDestination = (Destination) nctx.lookup(TESTQUEUENAME);

    // create default connection
    TestUtil.logTrace("Creating Connection with username, " + username
        + " password, " + password);
    conn = cf.createConnection(username, password);
  }

  private void setup_Q() throws Exception {

    TestUtil.logTrace("Getting ConnectionFactory " + QUEUECONNECTIONFACTORY);
    qcf = (QueueConnectionFactory) nctx.lookup(QUEUECONNECTIONFACTORY);

    TestUtil.logTrace("Getting Queue" + TESTQUEUENAME);
    queue = (Queue) nctx.lookup(TESTQUEUENAME);

    // create default QueueConnection
    TestUtil.logTrace("Creating QueueConnection with username, " + username
        + " password, " + password);
    qconn = qcf.createQueueConnection(username, password);
  }

  private void common_T() throws Exception {

    TestUtil
        .logTrace("Getting ConnectionFactory " + DURABLETOPICCONNECTIONFACTORY);
    cf = (ConnectionFactory) nctx.lookup(DURABLETOPICCONNECTIONFACTORY);

    TestUtil.logTrace("Getting Destination " + TESTTOPICNAME);
    testDestination = (Destination) nctx.lookup(TESTTOPICNAME);

    // create default connection
    TestUtil.logTrace("Creating Connection with  username, " + username
        + " password, " + password);
    conn = cf.createConnection(username, password);

  }

  public ArrayList sendTextMessage_CQ(String TestName, String message) {
    ArrayList valueAtSend = null;
    long timeBeforeSend = 0L;
    long timeAfterSend = 0L;

    try {
      // get ConnectionFactory and Connection
      common_Q();

      // create default Session
      TestUtil.logTrace("Creating Session");
      Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);

      // create default consumer/producer
      TestUtil.logTrace("Creating messageProducer");
      MessageProducer sender = sess.createProducer(testDestination);

      TestUtil.logMsg("Creating 1 TextMessage");
      messageSent = sess.createTextMessage();
      messageSent.setText(message);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", TestName);

      TestUtil.logMsg("Sending a TextMessage");
      timeBeforeSend = System.currentTimeMillis();
      sender.send(messageSent);
      timeAfterSend = System.currentTimeMillis();

      valueAtSend = logPropertyAtSend(messageSent, timeBeforeSend,
          timeAfterSend);

    } catch (Exception e) {
      TestUtil.logErr("Failed to send a Message in sendTextMessage_CQ");
      TestUtil.printStackTrace(e);
    } finally {
      if (conn != null) {
        try {
          TestUtil.logTrace("Closing Connection in sendTextMessage_CQ");
          conn.close();
        } catch (Exception ce) {
          TestUtil.logErr(
              "Error closing conn in sendTextMessage_CQ" + ce.getMessage(), ce);
        }
      }
    }
    return valueAtSend;
  }

  private ArrayList logPropertyAtSend(Message messageSent, long timeBeforeSend,
      long timeAfterSend) throws Exception {
    ArrayList valueAtSend = new ArrayList(9);

    valueAtSend.add(0, Long.valueOf(timeBeforeSend));
    TestUtil.logTrace("Time before send..." + valueAtSend.get(0));

    valueAtSend.add(1, Long.valueOf(timeAfterSend));
    TestUtil.logTrace("Time after send...." + valueAtSend.get(1));

    valueAtSend.add(2, Long.valueOf(messageSent.getJMSTimestamp()));
    TestUtil.logTrace("JMSTimeStamp......." + valueAtSend.get(2));

    valueAtSend.add(3, Long.valueOf(messageSent.getJMSExpiration()));
    TestUtil.logTrace("JMSExpiration......" + valueAtSend.get(3));

    valueAtSend.add(4, messageSent.getJMSDestination());
    TestUtil.logTrace("JMSDestination....." + valueAtSend.get(4));

    valueAtSend.add(5, Long.valueOf(messageSent.getJMSPriority()));
    TestUtil.logTrace("JMSPriority........" + valueAtSend.get(5));

    valueAtSend.add(6, Long.valueOf(messageSent.getJMSDeliveryMode()));
    TestUtil.logTrace("JMSDeliveryMode...." + valueAtSend.get(6));

    valueAtSend.add(7, messageSent.getJMSMessageID());
    TestUtil.logTrace("JMSMessageID......." + valueAtSend.get(7));

    valueAtSend.add(8, messageSent.getJMSCorrelationID());
    TestUtil.logTrace("JMSCorrelationID..." + valueAtSend.get(8));

    return valueAtSend;
  }

  public String receiveTextMessage_CQ() {
    TextMessage receivedM = null;
    String tmp = null;
    try {
      // get ConnectionFactory and Connection
      common_Q();
      conn.start();

      // create default Session
      TestUtil.logTrace("Creating Session");
      Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);

      // create default consumer
      TestUtil.logTrace("Creating MessageConsumer");
      MessageConsumer receiver = sess.createConsumer(testDestination);

      receivedM = (TextMessage) receiver.receive(timeout);
      tmp = receivedM.getText();
    } catch (Exception e) {
      TestUtil.logErr("Failed to receive a message in receiveTextMessage_CQ: ",
          e);
      throw new EJBException(e);
    } finally {
      if (conn != null) {
        try {
          TestUtil.logTrace("Closing Connection in receiveTextMessage_CQ");
          conn.close();
        } catch (Exception ce) {
          TestUtil.logErr(
              "Error closing conn in receiveTextMessage_CQ" + ce.getMessage(),
              ce);
        }
      }
    }
    return tmp;
  }

  public String receiveMessageS_CQ(String selector) {
    TextMessage receivedM = null;
    String tmp = null;

    try {
      // get ConnectionFactory and Connection
      common_Q();
      conn.start();

      // create default Session
      TestUtil.logTrace("Creating Session");
      Session sess = conn.createSession(true, Session.AUTO_ACKNOWLEDGE);

      // create default consumer
      TestUtil.logTrace("Creating MessageConsumer");
      MessageConsumer receiver = sess.createConsumer(testDestination, selector);

      receivedM = (TextMessage) receiver.receive(timeout);
      tmp = receivedM.getText();
    } catch (Exception e) {
      TestUtil.logErr("Failed to receive a message in receiveMessageS_CQ: ", e);
      throw new EJBException(e);
    } finally {
      if (conn != null) {
        try {
          TestUtil.logTrace("Closing Connection in receiveMessageS_CQ");
          conn.close();
        } catch (Exception ce) {
          TestUtil.logErr(
              "Error closing conn in receiveMessageS_CQ" + ce.getMessage(), ce);
        }
      }
    }
    return tmp;
  }

  public int browseTextMessage_CQ(int num, String message) {
    QueueBrowser browseAll = null;
    int msgCount = 0;
    int totalMsg = 0;
    TextMessage tempMsg = null;
    try {
      // get ConnectionFactory and Connection
      common_Q();
      conn.start();

      // create default Session
      TestUtil.logTrace("Creating Session");
      Session sess = conn.createSession(true, Session.AUTO_ACKNOWLEDGE);

      // create browser
      TestUtil.logTrace("Creating QueueBrowser");
      browseAll = sess.createBrowser((Queue) testDestination);

      // getting Emumeration that contains at least two test messages
      // without getting into dead loop.
      // Workaround for possible timing problem
      Enumeration msgs = null;
      int i = 0;
      do {
        i++;
        msgCount = 0;
        totalMsg = 0;
        msgs = browseAll.getEnumeration();
        TestUtil.logTrace("getting Enumeration " + i);
        while (msgs.hasMoreElements()) {
          tempMsg = (TextMessage) msgs.nextElement();
          totalMsg++;
          if (!(tempMsg.getText().indexOf(message) < 0))
            msgCount++;
        }
        TestUtil.logTrace("found " + msgCount + " messages total in browser");
      } while ((msgCount < num) && (i < 10));

    } catch (Exception e) {
      TestUtil.logErr("Failed to browse message in browseTextMessage_CQ: ", e);
      throw new EJBException(e);
    } finally {
      if (conn != null) {
        try {
          TestUtil.logTrace("Closing Connection in browseTextMessage_CQ");
          conn.close();
        } catch (Exception ce) {
          TestUtil.logErr(
              "Error closing conn in browseTextMessage_CQ" + ce.getMessage(),
              ce);
        }
      }
    }
    return totalMsg;
  }

  public int browseMessageS_CQ(int num, String message, String selector) {
    QueueBrowser selectiveBrowser = null;
    int msgCount = 0;
    int totalMsg = 0;
    TextMessage tempMsg = null;

    try {
      // get ConnectionFactory and Connection
      common_Q();
      conn.start();

      // create default Session
      TestUtil.logTrace("Creating Session");
      Session sess = conn.createSession(true, Session.AUTO_ACKNOWLEDGE);

      // create browser
      TestUtil.logTrace("Creating QueueBrowser");
      selectiveBrowser = sess.createBrowser((Queue) testDestination, selector);

      // getting Emumeration that contains at least two test messages
      // without getting into dead loop.
      // Workaround for possible timing problem
      Enumeration msgs = null;
      int i = 0;
      do {
        i++;
        msgCount = 0;
        totalMsg = 0;
        msgs = selectiveBrowser.getEnumeration();
        TestUtil.logTrace("getting Enumeration " + i);
        while (msgs.hasMoreElements()) {
          tempMsg = (TextMessage) msgs.nextElement();
          totalMsg++;
          if (!(tempMsg.getText().indexOf(message) < 0))
            msgCount++;
        }
        TestUtil.logTrace("found " + msgCount + " messages total in browser");
      } while ((msgCount < num) && (i < 10));

    } catch (Exception e) {
      TestUtil.logErr("Failed to browse message in browseMessageS_CQ: ", e);
      throw new EJBException(e);
    } finally {
      if (conn != null) {
        try {
          TestUtil.logTrace("Closing Connection in browseMessageS_CQ");
          conn.close();
        } catch (Exception ce) {
          TestUtil.logErr(
              "Error closing conn in browseMessageS_CQ" + ce.getMessage(), ce);
        }
      }
    }
    return totalMsg;
  }

  public ArrayList sendMessageP_CQ(String TestName, String message,
      boolean val) {
    ArrayList valueAtSend = null;
    long timeBeforeSend = 0L;
    long timeAfterSend = 0L;

    try {
      // get ConnectionFactory and Connection
      common_Q();

      // create default Session
      TestUtil.logTrace("Creating Session");
      Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);

      // create default consumer/producer
      TestUtil.logTrace("Creating messageProducer");
      MessageProducer sender = sess.createProducer(testDestination);

      TestUtil.logMsg("Creating 1 TextMessage");
      messageSent = sess.createTextMessage();
      messageSent.setText(message);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", TestName);
      messageSent.setBooleanProperty("lastMessage", val);

      TestUtil.logMsg("Sending a TextMessage");
      timeBeforeSend = System.currentTimeMillis();
      sender.send(messageSent);
      timeAfterSend = System.currentTimeMillis();

      valueAtSend = logPropertyAtSend(messageSent, timeBeforeSend,
          timeAfterSend);

    } catch (Exception e) {
      TestUtil.logErr("Failed to send a Message in sendMessageP_CQ");
      TestUtil.printStackTrace(e);
    } finally {
      if (conn != null) {
        try {
          TestUtil.logTrace("Closing Connection in sendMessageP_CQ");
          conn.close();
        } catch (Exception ce) {
          TestUtil.logErr(
              "Error closing conn in sendMessageP_CQ" + ce.getMessage(), ce);
        }
      }
    }
    return valueAtSend;
  }

  public ArrayList sendMessagePP_CQ(String TestName, String message,
      boolean val, String p, String value) {
    ArrayList valueAtSend = null;
    long timeBeforeSend = 0L;
    long timeAfterSend = 0L;

    try {
      // get ConnectionFactory and Connection
      common_Q();

      // create default Session
      TestUtil.logTrace("Creating Session");
      Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);

      // create default consumer/producer
      TestUtil.logTrace("Creating messageProducer");
      MessageProducer sender = sess.createProducer(testDestination);

      TestUtil.logMsg("Creating 1 TextMessage");
      messageSent = sess.createTextMessage();
      messageSent.setText(message);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", TestName);
      messageSent.setStringProperty(p, value);
      messageSent.setBooleanProperty("lastMessage", val);

      TestUtil.logMsg("Sending a TextMessage");
      timeBeforeSend = System.currentTimeMillis();
      sender.send(messageSent);
      timeAfterSend = System.currentTimeMillis();

      valueAtSend = logPropertyAtSend(messageSent, timeBeforeSend,
          timeAfterSend);

    } catch (Exception e) {
      TestUtil.logErr("Failed to send a Message in sendMessagePP_CQ");
      TestUtil.printStackTrace(e);
    } finally {
      if (conn != null) {
        try {
          TestUtil.logTrace("Closing Connection in sendMessagePP_CQ");
          conn.close();
        } catch (Exception ce) {
          TestUtil.logErr(
              "Error closing conn in sendMessagePP_CQ" + ce.getMessage(), ce);
        }
      }
    }
    return valueAtSend;
  }

  public ArrayList sendTextMessage_CT(String TestName, String message) {
    ArrayList valueAtSend = null;
    long timeBeforeSend = 0L;
    long timeAfterSend = 0L;

    try {
      // get ConnectionFactory and Connection
      common_T();

      // create default Session
      TestUtil.logTrace("Creating Session");
      Session sess = conn.createSession(true, Session.AUTO_ACKNOWLEDGE);

      // create default consumer/producer
      TestUtil.logTrace("Creating messageProducer");
      MessageProducer sender = sess.createProducer(testDestination);

      TestUtil.logMsg("Creating 1 TextMessage");
      messageSent = sess.createTextMessage();
      messageSent.setText(message);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", TestName);

      TestUtil.logMsg("Sending a TextMessage");
      timeBeforeSend = System.currentTimeMillis();
      sender.send(messageSent);
      timeAfterSend = System.currentTimeMillis();

      valueAtSend = logPropertyAtSend(messageSent, timeBeforeSend,
          timeAfterSend);

    } catch (Exception e) {
      TestUtil.logErr("Failed to send a Message in sendTextMessage_CT");
      TestUtil.printStackTrace(e);
    } finally {
      if (conn != null) {
        try {
          TestUtil.logTrace("Closing Connection in sendTextMessage_CT");
          conn.close();
        } catch (Exception ce) {
          TestUtil.logErr(
              "Error closing conn in sendTextMessage_CT" + ce.getMessage(), ce);
        }
      }
    }
    return valueAtSend;
  }

  public String receiveTextMessage_CT() {
    TextMessage receivedM = null;
    String tmp = null;

    try {
      // get ConnectionFactory and Connection
      common_T();
      conn.start();

      // create default Session
      TestUtil.logTrace("Creating Session");
      Session sess = conn.createSession(true, Session.AUTO_ACKNOWLEDGE);

      // create default consumer
      TestUtil.logTrace("Creating MessageConsumer");
      MessageConsumer receiver = sess.createConsumer(testDestination);

      receivedM = (TextMessage) receiver.receive(timeout);
      tmp = receivedM.getText();
    } catch (Exception e) {
      TestUtil.logErr("Failed to receive a message in receiveTextMessage_CT: ",
          e);
      throw new EJBException(e);
    } finally {
      if (conn != null) {
        try {
          TestUtil.logTrace("Closing Connection in receiveTextMessage_CT");
          conn.close();
        } catch (Exception ce) {
          TestUtil.logErr(
              "Error closing conn in receiveTextMessage_CT" + ce.getMessage(),
              ce);
        }
      }
    }
    return tmp;
  }

  public int getAck_CT() {
    int mode = 0;

    try {
      common_T();
      Session sess = conn.createSession(true, Session.AUTO_ACKNOWLEDGE);
      mode = sess.getAcknowledgeMode();
      TestUtil
          .logTrace("AcknowledgeMode is set at " + Session.AUTO_ACKNOWLEDGE);
      TestUtil.logTrace("AcknowledgeMode returned as " + mode);
    } catch (Exception e) {
      TestUtil.logErr("Failed to getAcknowledgeMode: ", e);
      throw new EJBException(e);
    } finally {
      if (conn != null) {
        try {
          TestUtil.logTrace("Closing Connection in getAck_CT");
          conn.close();
        } catch (Exception ce) {
          TestUtil.logErr(
              "Error closing conn in getAck_CT():" + ce.getMessage(), ce);
        }
      }
    }
    return mode;
  }

  public int getAck_CQ() {
    int mode = 0;

    try {
      common_Q();
      Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
      mode = sess.getAcknowledgeMode();
    } catch (Exception e) {
      TestUtil.logErr("Failed to getAcknowledgeMode: ", e);
      throw new EJBException(e);
    } finally {
      if (conn != null) {
        try {
          TestUtil.logTrace("Closing Connection in getAck_CQ");
          conn.close();
        } catch (Exception ce) {
          TestUtil.logErr(
              "Error closing conn in getAck_CQ():" + ce.getMessage(), ce);
        }
      }
    }
    return mode;
  }

  public boolean getQueue() {
    QueueBrowser browseAll = null;
    boolean pass = true;

    try {
      common_Q();
      Session sess = conn.createSession(true, Session.AUTO_ACKNOWLEDGE);

      // create browser
      TestUtil.logTrace("Creating QueueBrowser");
      browseAll = sess.createBrowser((Queue) testDestination);

      if (!browseAll.getQueue().toString().equals(testDestination.toString())) {
        pass = false;
        TestUtil.logErr("Error: QueueBrowser.getQueue test failed");
        TestUtil.logErr(
            "QueueBrowser.getQueue=" + browseAll.getQueue().toString() + ".");
        TestUtil.logErr("testDestination=" + testDestination.toString() + ".");
      }
      browseAll.close();

    } catch (Exception e) {
      TestUtil.logErr("Failed to getQueue: ", e);
      throw new EJBException(e);
    } finally {
      if (conn != null) {
        try {
          TestUtil.logTrace("Closing Connection in getQueue");
          conn.close();
        } catch (Exception ce) {
          TestUtil.logErr("Error closing conn in getQueue():" + ce.getMessage(),
              ce);
        }
      }
    }
    return pass;
  }

  public boolean getSelector(String selector) {
    QueueBrowser selectiveBrowser = null;
    boolean pass = true;

    try {
      common_Q();
      Session sess = conn.createSession(true, Session.AUTO_ACKNOWLEDGE);

      // create browser
      TestUtil.logTrace("Creating QueueBrowser");
      selectiveBrowser = sess.createBrowser((Queue) testDestination, selector);

      String tmp = selectiveBrowser.getMessageSelector();
      if (tmp.indexOf("TEST") < 0 || tmp.indexOf("test") < 0) {
        pass = false;
        TestUtil.logErr("Error: QueueBrowser.getMessageSelector test failed");
        TestUtil.logErr("selectiveBrowser.getMessageSelector()="
            + selectiveBrowser.getMessageSelector());
      }

      selectiveBrowser.close();
    } catch (Exception e) {
      TestUtil.logErr("Failed to getMessageSelector: ", e);
      throw new EJBException(e);
    } finally {
      if (conn != null) {
        try {
          TestUtil.logTrace("Closing Connection in getSelector");
          conn.close();
        } catch (Exception ce) {
          TestUtil.logErr(
              "Error closing conn in getSelector():" + ce.getMessage(), ce);
        }
      }
    }
    return pass;
  }

  public ArrayList sendTextMessage_Q(String TestName) {
    return sendTextMessage_Q(TestName, null, false, DeliveryMode.PERSISTENT,
        true);
  }

  public ArrayList sendTextMessage_Q(String TestName, String text) {
    return sendTextMessage_Q(TestName, text, false, DeliveryMode.PERSISTENT,
        true);
  }

  public ArrayList sendTextMessage_Q(String TestName, boolean setDest) {
    return sendTextMessage_Q(TestName, null, setDest, DeliveryMode.PERSISTENT,
        true);
  }

  public ArrayList sendTextMessage_Q(String TestName, boolean setDest,
      int mode) {
    return sendTextMessage_Q(TestName, null, setDest, mode, true);
  }

  public ArrayList sendTextMessage_Q(String TestName, String text,
      boolean setDest, int mode) {
    return sendTextMessage_Q(TestName, text, setDest, mode, true);
  }

  public ArrayList sendTextMessage_Q(String TestName, String text,
      Queue testQueue) {
    return sendTextMessage_Q(TestName, text, false, DeliveryMode.PERSISTENT,
        false);
  }

  private ArrayList sendTextMessage_Q(String TestName, String text,
      boolean setDest, int mode, boolean setQueue) {
    ArrayList valueAtSend = null;
    long timeBeforeSend = 0L;
    long timeAfterSend = 0L;

    QueueSender qsender = null;

    try {
      // get QueueConnectionFactory and QueueConnection
      setup_Q();

      // create default QueueSession
      TestUtil.logTrace("Creating QueueSession");
      QueueSession qsess = qconn.createQueueSession(true,
          Session.AUTO_ACKNOWLEDGE);

      // create default QueueSender
      TestUtil.logTrace("Creating QueueSender");
      if (setQueue)
        qsender = qsess.createSender(queue);
      else
        qsender = qsess.createSender((Queue) null);
      qsender.setPriority(priority);
      qsender.setTimeToLive(forever);
      if (mode != DeliveryMode.PERSISTENT)
        qsender.setDeliveryMode(mode);

      TestUtil.logMsg("Creating 1 TextMessage");
      messageSent = qsess.createTextMessage();

      if (text != null)
        messageSent.setText(text);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", TestName);
      messageSent.setJMSCorrelationID(jmsCorrelationID);
      messageSent.setJMSType(type);

      if (setDest)
        messageSent.setJMSReplyTo(queue);

      // -----------------------------------------------------------------------------
      TestUtil.logMsg("Sending a TextMessage");
      timeBeforeSend = System.currentTimeMillis();
      if (setQueue)
        qsender.send(messageSent);
      else
        qsender.send(queue, messageSent);
      timeAfterSend = System.currentTimeMillis();

      valueAtSend = logPropertyAtSend(messageSent, timeBeforeSend,
          timeAfterSend);

    } catch (Exception e) {
      TestUtil.logErr("Failed to send a Message in sendTextMessage_Q", e);
    } finally {
      if (qconn != null) {
        try {
          TestUtil.logTrace("Closing QueueConnection in sendTextMessage_Q");
          qconn.close();
        } catch (Exception ce) {
          TestUtil.logErr("Error closing QueueConnection in sendTextMessage_Q",
              ce);
        }
      }
    }
    return valueAtSend;
  }

  /*
   * public String receiveTextMessage_Q() { TextMessage receivedM = null; String
   * tmp = null;
   * 
   * try { //get QueueConnectionFactory and QueueConnection setup_Q();
   * qconn.start();
   * 
   * // create default Session TestUtil.logTrace("Creating QueueSession");
   * QueueSession qsess = qconn.createQueueSession(true,
   * Session.AUTO_ACKNOWLEDGE);
   * 
   * // create default QueueReceiver
   * TestUtil.logTrace("Creating QueueReceiver"); QueueReceiver qreceiver =
   * qsess.createReceiver(queue);
   * 
   * receivedM = (TextMessage) qreceiver.receive(timeout); tmp =
   * receivedM.getText(); } catch (Exception e ) {
   * TestUtil.logErr("Failed to receive a message in receiveTextMessage_Q: ",
   * e); throw new EJBException(e); } finally { if ( qconn != null) { try {
   * TestUtil.logTrace("Closing Connection in receiveTextMessage_Q");
   * qconn.close(); } catch (Exception ce) {
   * TestUtil.logErr("Error closing conn in receiveTextMessage_Q" +
   * ce.getMessage(), ce); } } } return tmp; }
   */

  public ArrayList sendFullBytesMessage_Q(String TestName) {
    return sendBytesMessage_Q(TestName, false, DeliveryMode.PERSISTENT);
  }

  public ArrayList sendBytesMessage_Q(String TestName, boolean setDest) {
    return sendBytesMessage_Q(TestName, setDest, DeliveryMode.PERSISTENT);
  }

  public ArrayList sendBytesMessage_Q(String TestName, boolean setDest,
      int mode) {
    ArrayList valueAtSend = null;
    long timeBeforeSend = 0L;
    long timeAfterSend = 0L;

    try {
      // get QueueConnectionFactory and QueueConnection
      setup_Q();

      // create default QueueSession
      TestUtil.logTrace("Creating QueueSession");
      QueueSession qsess = qconn.createQueueSession(true,
          Session.AUTO_ACKNOWLEDGE);

      // create default QueueSender
      TestUtil.logTrace("Creating QueueSender");
      QueueSender qsender = qsess.createSender(queue);
      qsender.setPriority(priority);
      qsender.setTimeToLive(forever);
      if (mode != DeliveryMode.PERSISTENT)
        qsender.setDeliveryMode(mode);

      TestUtil.logMsg("Creating 1 BytesMessage");
      messageSentB = qsess.createBytesMessage();

      messageSentB.setStringProperty("COM_SUN_JMS_TESTNAME", TestName);
      messageSentB.setJMSCorrelationID(jmsCorrelationID);
      messageSentB.setJMSType(type);

      if (setDest)
        messageSentB.setJMSReplyTo(queue);

      // -----------------------------------------------------------------------------
      TestUtil.logMsg("Writing one of each primitive type to the message");

      // -----------------------------------------------------------------------------
      messageSentB.writeBoolean(booleanValue);
      messageSentB.writeByte(byteValue);
      messageSentB.writeByte(byteValue1);
      messageSentB.writeChar(charValue);
      messageSentB.writeDouble(doubleValue);
      messageSentB.writeFloat(floatValue);
      messageSentB.writeInt(intValue);
      messageSentB.writeLong(longValue);
      messageSentB.writeObject(nInteger);
      messageSentB.writeShort(shortValue);
      messageSentB.writeShort(shortValue1);
      messageSentB.writeUTF(utfValue);
      messageSentB.writeBytes(bytesValue);
      messageSentB.writeBytes(bytesValue, 0, 1);

      TestUtil.logMsg("Sending a BytesMessage");
      timeBeforeSend = System.currentTimeMillis();
      qsender.send(messageSentB);
      timeAfterSend = System.currentTimeMillis();

      valueAtSend = logPropertyAtSend(messageSentB, timeBeforeSend,
          timeAfterSend);

    } catch (Exception e) {
      TestUtil.logErr("Failed to send a Message in sendFullBytesMessage_Q", e);
    } finally {
      if (qconn != null) {
        try {
          TestUtil
              .logTrace("Closing QueueConnection in sendFullBytesMessage_Q");
          qconn.close();
        } catch (Exception ce) {
          TestUtil.logErr(
              "Error closing QueueConnection in sendFullBytesMessage_Q", ce);
        }
      }
    }
    return valueAtSend;
  }

  public boolean verifyFullBytesMessage() {
    TestUtil.logTrace("In verifyFullBytesMessage ...");

    BytesMessage messageReceived = null;

    try {
      // get QueueConnectionFactory and QueueConnection
      setup_Q();
      qconn.start();

      // create default Session
      TestUtil.logTrace("Creating QueueSession");
      QueueSession qsess = qconn.createQueueSession(true,
          Session.AUTO_ACKNOWLEDGE);

      // create default QueueReceiver
      TestUtil.logTrace("Creating QueueReceiver");
      QueueReceiver qreceiver = qsess.createReceiver(queue);

      messageReceived = (BytesMessage) qreceiver.receive(timeout);
    } catch (Exception e) {
      TestUtil.logErr("Failed to receive a message in verifyFullBytesMessage: ",
          e);
      throw new EJBException(e);
    } finally {
      if (qconn != null) {
        try {
          TestUtil.logTrace("Closing Connection in verifyFullBytesMessage");
          qconn.close();
        } catch (Exception ce) {
          TestUtil.logErr("Error closing conn in verifyFullBytesMessage", ce);
        }
      }
    }

    boolean pass = true;

    try {
      if (!messageReceived.readBoolean() == booleanValue) {
        TestUtil.logErr("Error: boolean not returned as expected");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Error: unexpected exception was thrown", e);
      pass = false;
    }

    try {
      if (messageReceived.readByte() != byteValue) {
        TestUtil.logErr("Error: Byte not returned as expected");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Error: unexpected exception was thrown", e);
      pass = false;
    }

    try {
      int tmp = messageReceived.readUnsignedByte();

      if (tmp != byteValue2) {
        TestUtil.logErr(
            "Fail: readUnsignedByte not returned expected value: " + tmp);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Error: unexpected exception was thrown", e);
      pass = false;
    }

    try {
      if (messageReceived.readChar() != charValue) {
        TestUtil.logErr("Fail: char not returned as expected");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Error: unexpected exception was thrown", e);
      pass = false;
    }

    try {
      if (messageReceived.readDouble() != doubleValue) {
        TestUtil.logErr("Fail: double not returned as expected");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Error: unexpected exception was thrown", e);
      pass = false;
    }

    try {
      if (messageReceived.readFloat() != floatValue) {
        TestUtil.logErr("Fail: float not returned as expected");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Error: unexpected exception was thrown", e);
      pass = false;
    }

    try {
      if (messageReceived.readInt() != intValue) {
        TestUtil.logErr("Fail: int not returned as expected");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Error: unexpected exception was thrown", e);
      pass = false;
    }

    try {
      if (messageReceived.readLong() != longValue) {
        TestUtil.logErr("Fail: long not returned as expected");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Error: unexpected exception was thrown", e);
      pass = false;
    }

    try {
      if (messageReceived.readInt() != nInteger.intValue()) {
        TestUtil.logErr("Fail: Integer not returned as expected");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Error: unexpected exception was thrown", e);
      pass = false;
    }

    try {
      if (messageReceived.readShort() != shortValue) {
        TestUtil.logErr("Fail: short not returned as expected");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Error: unexpected exception was thrown", e);
      pass = false;
    }

    try {
      int tmps = messageReceived.readUnsignedShort();
      if (tmps != shortValue2) {
        TestUtil.logErr(
            "Fail: readUnsignedShort did not return expected value: " + tmps);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Error: unexpected exception was thrown", e);
      pass = false;
    }

    try {
      if (!messageReceived.readUTF().equals(utfValue)) {
        TestUtil.logErr("Fail: UTF not returned as expected");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Error: unexpected exception was thrown", e);
      pass = false;
    }

    try {
      int nCount = messageReceived.readBytes(bytesValueRecvd);

      for (int i = 0; i < nCount; i++) {
        if (bytesValueRecvd[i] != bytesValue[i]) {
          TestUtil.logErr("Fail: bytes value incorrect");
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Error: unexpected exception was thrown", e);
      pass = false;
    }

    try {
      int nCount = messageReceived.readBytes(bytesValueRecvd);

      TestUtil.logTrace("count returned " + nCount);
      if (bytesValueRecvd[0] != bytesValue[0]) {
        TestUtil.logErr("Fail: bytes value incorrect");
        pass = false;
      }

      if (nCount != 1) {
        TestUtil.logErr("Error: count not returned as expected");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Error: unexpected exception was thrown", e);
      pass = false;
    }

    try {
      long length = 37l;
      long tmpl = messageReceived.getBodyLength();
      if (tmpl < length) {
        TestUtil
            .logErr("getBodyLength test failed with incorrect length=" + tmpl);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Error: unexpected exception was thrown", e);
      pass = false;
    }
    return pass;
  }

  public ArrayList sendFullMapMessage_Q(String testName) {
    return sendMapMessage_Q(testName, false, DeliveryMode.PERSISTENT);
  }

  public ArrayList sendMapMessage_Q(String testName, boolean setDest) {
    return sendMapMessage_Q(testName, setDest, DeliveryMode.PERSISTENT);
  }

  public ArrayList sendMapMessage_Q(String testName, boolean setDest,
      int mode) {
    ArrayList valueAtSend = null;
    long timeBeforeSend = 0L;
    long timeAfterSend = 0L;

    try {
      setup_Q();

      MapMessage messageSent = null;
      MapMessage msgReceivedM = null;

      // create default QueueSession
      TestUtil.logTrace("Creating QueueSession");
      QueueSession qsess = qconn.createQueueSession(true,
          Session.AUTO_ACKNOWLEDGE);

      // create default QueueSender
      TestUtil.logTrace("Creating QueueSender");
      QueueSender qsender = qsess.createSender(queue);
      qsender.setPriority(priority);
      qsender.setTimeToLive(forever);
      if (mode != DeliveryMode.PERSISTENT)
        qsender.setDeliveryMode(mode);

      TestUtil.logMsg("Creating 1 MapMessage");
      messageSent = qsess.createMapMessage();

      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
      messageSent.setJMSCorrelationID(jmsCorrelationID);
      messageSent.setJMSType(type);

      if (setDest)
        messageSent.setJMSReplyTo(queue);

      messageSent.setBoolean("booleanValue", booleanValue);
      messageSent.setByte("byteValue", byteValue);
      messageSent.setBytes("bytesValue", bytesValue);
      messageSent.setBytes("bytesValue2", bytesValue, 0, 1);
      messageSent.setChar("charValue", charValue);
      messageSent.setDouble("doubleValue", doubleValue);
      messageSent.setFloat("floatValue", floatValue);
      messageSent.setInt("intValue", intValue);
      messageSent.setLong("longValue", longValue);
      messageSent.setObject("nInteger", nInteger);
      messageSent.setShort("shortValue", shortValue);
      messageSent.setString("stringValue", stringValue);
      messageSent.setString("nullTest", null);

      TestUtil.logTrace("Sending a MapMessage...");
      timeBeforeSend = System.currentTimeMillis();
      qsender.send(messageSent);
      timeAfterSend = System.currentTimeMillis();

      valueAtSend = logPropertyAtSend(messageSent, timeBeforeSend,
          timeAfterSend);

    } catch (Exception e) {
      TestUtil.logErr("Failed to send a Message in sendFullMapMessage_Q", e);
    } finally {
      if (qconn != null) {
        try {
          TestUtil.logTrace("Closing QueueConnection in sendFullMapMessage_Q");
          qconn.close();
        } catch (Exception ce) {
          TestUtil.logErr(
              "Error closing QueueConnection in sendFullMapMessage_Q", ce);
        }
      }
    }
    return valueAtSend;
  }

  public boolean verifyFullMapMessage() {
    boolean pass = true;
    MapMessage msgReceivedM = null;

    try {
      // get QueueConnectionFactory and QueueConnection
      setup_Q();
      qconn.start();

      // create default Session
      TestUtil.logTrace("Creating QueueSession");
      QueueSession qsess = qconn.createQueueSession(true,
          Session.AUTO_ACKNOWLEDGE);

      // create default QueueReceiver
      TestUtil.logTrace("Creating QueueReceiver");
      QueueReceiver qreceiver = qsess.createReceiver(queue);

      msgReceivedM = (MapMessage) qreceiver.receive(timeout);
    } catch (Exception e) {
      TestUtil.logErr("Failed to receive a message in verifyFullMapMessage: ",
          e);
      throw new EJBException(e);
    } finally {
      if (qconn != null) {
        try {
          TestUtil.logTrace("Closing Connection in verifyFullMapMessage");
          qconn.close();
        } catch (Exception ce) {
          TestUtil.logErr("Error closing conn in verifyFullMapMessage", ce);
        }
      }
    }

    try {
      if (msgReceivedM.getBoolean("booleanValue") != booleanValue) {
        TestUtil.logErr("Fail: invalid boolean returned: "
            + msgReceivedM.getBoolean("booleanValue"));
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Fail: unexpected exception " + e.getClass().getName()
          + " was returned", e);
      pass = false;
    }

    try {
      if (msgReceivedM.getByte("byteValue") != byteValue) {
        TestUtil.logErr("Fail: invalid byte returned: "
            + msgReceivedM.getByte("byteValue"));
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Fail: unexpected exception " + e.getClass().getName()
          + " was returned", e);
      pass = false;
    }

    try {
      byte[] b = msgReceivedM.getBytes("bytesValue");

      for (int i = 0; i < b.length; i++) {
        if (b[i] != bytesValue[i]) {
          TestUtil.logErr("Fail: byte array " + i + " not valid: " + b[i]);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Fail: unexpected exception " + e.getClass().getName()
          + " was returned", e);
      pass = false;
    }

    try {
      byte[] b = msgReceivedM.getBytes("bytesValue2");

      if (b[0] != bytesValue[0]) {
        TestUtil.logErr("Fail: byte array not valid " + b[0]);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Fail: unexpected exception " + e.getClass().getName()
          + " was returned", e);
      pass = false;
    }

    try {
      if (msgReceivedM.getChar("charValue") != charValue) {
        TestUtil.logErr("Fail: invalid char returned: "
            + msgReceivedM.getChar("charValue"));
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Fail: unexpected exception " + e.getClass().getName()
          + " was returned", e);
      pass = false;
    }

    try {
      if (msgReceivedM.getDouble("doubleValue") != doubleValue) {
        TestUtil.logErr("Fail: invalid double returned: "
            + msgReceivedM.getDouble("doubleValue"));
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Fail: unexpected exception " + e.getClass().getName()
          + " was returned", e);
      pass = false;
    }

    try {
      if (msgReceivedM.getFloat("floatValue") != floatValue) {
        TestUtil.logErr("Fail: invalid float returned");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Fail: unexpected exception " + e.getClass().getName()
          + " was returned", e);
      pass = false;
    }

    try {
      if (msgReceivedM.getInt("intValue") != intValue) {
        TestUtil.logErr("Fail: invalid int returned");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Fail: unexpected exception " + e.getClass().getName()
          + " was returned", e);
      pass = false;
    }

    try {
      if (msgReceivedM.getLong("longValue") != longValue) {
        TestUtil.logErr("Fail: invalid long returned");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Fail: unexpected exception " + e.getClass().getName()
          + " was returned", e);
      pass = false;
    }

    try {
      if (!msgReceivedM.getObject("nInteger").toString()
          .equals(nInteger.toString())) {
        TestUtil.logErr("Fail: invalid object returned");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Fail: unexpected exception " + e.getClass().getName()
          + " was returned", e);
      pass = false;
    }

    try {
      if (msgReceivedM.getShort("shortValue") != shortValue) {
        TestUtil.logErr("Fail: invalid short returned");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Fail: unexpected exception " + e.getClass().getName()
          + " was returned", e);
      pass = false;
    }

    try {
      if (!msgReceivedM.getString("stringValue").equals(stringValue)) {
        TestUtil.logErr("Fail: invalid string returned");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Fail: unexpected exception " + e.getClass().getName()
          + " was returned", e);
      pass = false;
    }

    try {
      if (msgReceivedM.getString("nullTest") != null) {
        TestUtil.logErr("Fail:  null not returned from getString: "
            + msgReceivedM.getString("nullTest"));
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Fail: unexpected exception " + e.getClass().getName()
          + " was returned", e);
      pass = false;
    }
    return pass;
  }

  public ArrayList sendFullStreamMessage_Q(String testName) {
    return sendStreamMessage_Q(testName, false, DeliveryMode.PERSISTENT);
  }

  public ArrayList sendStreamMessage_Q(String testName, boolean setDest) {
    return sendStreamMessage_Q(testName, setDest, DeliveryMode.PERSISTENT);
  }

  public ArrayList sendStreamMessage_Q(String testName, boolean setDest,
      int mode) {
    ArrayList valueAtSend = null;
    long timeBeforeSend = 0L;
    long timeAfterSend = 0L;

    try {
      setup_Q();

      StreamMessage messageSent = null;

      // create default QueueSession
      TestUtil.logTrace("Creating QueueSession");
      QueueSession qsess = qconn.createQueueSession(true,
          Session.AUTO_ACKNOWLEDGE);

      // create default QueueSender
      TestUtil.logTrace("Creating QueueSender");
      QueueSender qsender = qsess.createSender(queue);
      qsender.setPriority(priority);
      qsender.setTimeToLive(forever);
      if (mode != DeliveryMode.PERSISTENT)
        qsender.setDeliveryMode(mode);

      TestUtil.logMsg("Creating 1 StreamMessage");
      messageSent = qsess.createStreamMessage();

      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
      messageSent.setJMSCorrelationID(jmsCorrelationID);
      messageSent.setJMSType(type);

      if (setDest)
        messageSent.setJMSReplyTo(queue);

      messageSent.writeBytes(byteValues2, 0, byteValues.length);
      messageSent.writeBoolean(booleanValue);
      messageSent.writeByte(byteValue);
      messageSent.writeBytes(byteValues);
      messageSent.writeChar(charValue);
      messageSent.writeDouble(doubleValue);
      messageSent.writeFloat(floatValue);
      messageSent.writeInt(intValue);
      messageSent.writeLong(longValue);
      messageSent.writeObject(sTesting);
      messageSent.writeShort(shortValue);
      messageSent.writeString(stringValue);
      messageSent.writeObject(null);

      TestUtil.logTrace("Sending a StreamMessage ...");
      timeBeforeSend = System.currentTimeMillis();
      qsender.send(messageSent);
      timeAfterSend = System.currentTimeMillis();

      valueAtSend = logPropertyAtSend(messageSent, timeBeforeSend,
          timeAfterSend);

    } catch (Exception e) {
      TestUtil.logErr("Failed to send a Message in sendFullStreamMessage_Q", e);
    } finally {
      if (qconn != null) {
        try {
          TestUtil
              .logTrace("Closing QueueConnection in sendFullStreamMessage_Q");
          qconn.close();
        } catch (Exception ce) {
          TestUtil.logErr(
              "Error closing QueueConnection in sendFullStreamMessage_Q", ce);
        }
      }
    }
    return valueAtSend;
  }

  public boolean verifyFullStreamMessage() {
    TestUtil.logTrace("In verifyFullStreamMessage");

    boolean pass = true;
    StreamMessage messageReceived = null;

    try {
      // get QueueConnectionFactory and QueueConnection
      setup_Q();
      qconn.start();

      // create default Session
      TestUtil.logTrace("Creating QueueSession");
      QueueSession qsess = qconn.createQueueSession(true,
          Session.AUTO_ACKNOWLEDGE);

      // create default QueueReceiver
      TestUtil.logTrace("Creating QueueReceiver");
      QueueReceiver qreceiver = qsess.createReceiver(queue);

      messageReceived = (StreamMessage) qreceiver.receive(timeout);
    } catch (Exception e) {
      TestUtil.logErr(
          "Failed to receive a message in verifyFullStreamMessage: ", e);
      throw new EJBException(e);
    } finally {
      if (qconn != null) {
        try {
          TestUtil.logTrace("Closing Connection in verifyFullStreamMessage");
          qconn.close();
        } catch (Exception ce) {
          TestUtil.logErr("Error closing conn in verifyFullStreamMessage", ce);
        }
      }
    }

    try {
      int nCount;
      do {
        nCount = messageReceived.readBytes(byteValuesReturned2);
        TestUtil.logTrace("nCount is " + nCount);
        if (nCount != -1) {
          for (int i = 0; i < byteValuesReturned2.length; i++) {
            if (byteValuesReturned2[i] != byteValues2[i]) {
              TestUtil.logErr("Fail: byte[] " + i + " is not valid ="
                  + byteValuesReturned2[i]);
              pass = false;
            }
          }
        }
      } while (nCount >= byteValuesReturned2.length);
    } catch (Exception e) {
      TestUtil.logErr(
          "Error: exception was thrown in verifyFullStreamMessage: ", e);
      pass = false;
    }

    try {
      if (messageReceived.readBoolean() != booleanValue) {
        TestUtil.logErr("Fail: boolean not returned as expected");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr(
          "Error: exception was thrown in verifyFullStreamMessage: ", e);
      pass = false;
    }

    try {
      if (messageReceived.readByte() != byteValue) {
        TestUtil.logErr("Fail: Byte not returned as expected");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr(
          "Error: exception was thrown in verifyFullStreamMessage: ", e);
      pass = false;
    }

    try {
      int nCount;
      do {
        nCount = messageReceived.readBytes(byteValuesReturned);
        TestUtil.logTrace("nCount is " + nCount);
        if (nCount != -1) {
          for (int i = 0; i < byteValuesReturned.length; i++) {
            if (byteValuesReturned[i] != byteValues[i]) {
              TestUtil.logErr("Fail: byte[] " + i + " is not valid");
              pass = false;
            }
          }
        }
      } while (nCount >= byteValuesReturned.length);
    } catch (Exception e) {
      TestUtil.logErr(
          "Error: exception was thrown in verifyFullStreamMessage: ", e);
      pass = false;
    }

    try {
      if (messageReceived.readChar() != charValue) {
        TestUtil.logErr("Fail: char not returned as expected");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr(
          "Error: exception was thrown in verifyFullStreamMessage: ", e);
      pass = false;
    }

    try {
      if (messageReceived.readDouble() != doubleValue) {
        TestUtil.logErr("Fail: double not returned as expected");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr(
          "Error: exception was thrown in verifyFullStreamMessage: ", e);
      pass = false;
    }

    try {
      if (messageReceived.readFloat() != floatValue) {
        TestUtil.logErr("Fail: float not returned as expected");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr(
          "Error: exception was thrown in verifyFullStreamMessage: ", e);
      pass = false;
    }

    try {
      if (messageReceived.readInt() != intValue) {
        TestUtil.logErr("Fail: int not returned as expected");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr(
          "Error: exception was thrown in verifyFullStreamMessage: ", e);
      pass = false;
    }

    try {
      if (messageReceived.readLong() != longValue) {
        TestUtil.logErr("Fail: long not returned as expected");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr(
          "Error: exception was thrown in verifyFullStreamMessage: ", e);
      pass = false;
    }

    try {
      if (!messageReceived.readObject().equals(sTesting)) {
        TestUtil.logErr("Fail: object not returned as expected");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr(
          "Error: exception was thrown in verifyFullStreamMessage: ", e);
      pass = false;
    }

    try {
      if (messageReceived.readShort() != shortValue) {
        TestUtil.logErr("Fail: short not returned as expected");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr(
          "Error: exception was thrown in verifyFullStreamMessage: ", e);
      pass = false;
    }

    try {
      if (!messageReceived.readString().equals(stringValue)) {
        TestUtil.logErr("Fail: string not returned as expected");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr(
          "Error: exception was thrown in verifyFullStreamMessage: ", e);
      pass = false;
    }

    try {
      if (messageReceived.readObject() != null) {
        TestUtil.logErr("Fail: object not returned as expected");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr(
          "Error: exception was thrown in verifyFullStreamMessage: ", e);
      pass = false;
    }

    return pass;
  }

  public ArrayList sendObjectMessage_Q(String testName) {
    return sendObjectMessage_Q(testName, false, DeliveryMode.PERSISTENT);
  }

  public ArrayList sendObjectMessage_Q(String testName, boolean setDest) {
    return sendObjectMessage_Q(testName, setDest, DeliveryMode.PERSISTENT);
  }

  public ArrayList sendObjectMessage_Q(String testName, boolean setDest,
      int mode) {
    ArrayList valueAtSend = null;
    long timeBeforeSend = 0L;
    long timeAfterSend = 0L;

    try {
      setup_Q();

      ObjectMessage messageSent = null;

      // create default QueueSession
      TestUtil.logTrace("Creating QueueSession");
      QueueSession qsess = qconn.createQueueSession(true,
          Session.AUTO_ACKNOWLEDGE);

      // create default QueueSender
      TestUtil.logTrace("Creating QueueSender");
      QueueSender qsender = qsess.createSender(queue);
      qsender.setPriority(priority);
      qsender.setTimeToLive(forever);
      if (mode != DeliveryMode.PERSISTENT)
        qsender.setDeliveryMode(mode);

      TestUtil.logMsg("Creating 1 ObjectMessage");
      messageSent = qsess.createObjectMessage();

      messageSent.setObject(String.valueOf("HeaderIDTest for ObjectMessage"));
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
      messageSent.setJMSCorrelationID(jmsCorrelationID);
      messageSent.setJMSType(type);

      if (setDest)
        messageSent.setJMSReplyTo(queue);

      TestUtil.logTrace("Sending an ObjectMessage...");
      timeBeforeSend = System.currentTimeMillis();
      qsender.send(messageSent);
      timeAfterSend = System.currentTimeMillis();
      valueAtSend = logPropertyAtSend(messageSentB, timeBeforeSend,
          timeAfterSend);

    } catch (Exception e) {
      TestUtil.logErr("Failed to send a Message in sendObjectMessage_Q", e);
    } finally {
      if (qconn != null) {
        try {
          TestUtil.logTrace("Closing QueueConnection in sendObjectMessage_Q");
          qconn.close();
        } catch (Exception ce) {
          TestUtil.logErr(
              "Error closing QueueConnection in sendObjectMessage_Q", ce);
        }
      }
    }
    return valueAtSend;
  }

  public ObjectMessage receiveObjectMessage_Q() {
    ObjectMessage receivedM = null;

    try {
      // get QueueConnectionFactory and QueueConnection
      setup_Q();
      qconn.start();

      // create default Session
      TestUtil.logTrace("Creating QueueSession");
      QueueSession qsess = qconn.createQueueSession(true,
          Session.AUTO_ACKNOWLEDGE);

      // create default QueueReceiver
      TestUtil.logTrace("Creating QueueReceiver");
      QueueReceiver qreceiver = qsess.createReceiver(queue);

      receivedM = (ObjectMessage) qreceiver.receive(timeout);
    } catch (Exception e) {
      TestUtil.logErr("Failed to receive a message in receiveObjectMessage_Q: ",
          e);
      throw new EJBException(e);
    } finally {
      if (qconn != null) {
        try {
          TestUtil.logTrace("Closing Connection in receiveObjectMessage_Q");
          qconn.close();
        } catch (Exception ce) {
          TestUtil.logErr(
              "Error closing conn in receiveObjectMessage_Q" + ce.getMessage(),
              ce);
        }
      }
    }
    return receivedM;
  }

  public Message receiveMessage_Q() {
    Message receivedM = null;

    try {
      // get QueueConnectionFactory and QueueConnection
      setup_Q();
      qconn.start();

      // create default Session
      TestUtil.logTrace("Creating QueueSession");
      QueueSession qsess = qconn.createQueueSession(true,
          Session.AUTO_ACKNOWLEDGE);

      // create default QueueReceiver
      TestUtil.logTrace("Creating QueueReceiver");
      QueueReceiver qreceiver = qsess.createReceiver(queue);

      receivedM = (Message) qreceiver.receive(timeout);
    } catch (Exception e) {
      TestUtil.logErr("Failed to receive a message in receiveMessage_Q: ", e);
      throw new EJBException(e);
    } finally {
      if (qconn != null) {
        try {
          TestUtil.logTrace("Closing Connection in receiveMessage_Q");
          qconn.close();
        } catch (Exception ce) {
          TestUtil.logErr(
              "Error closing conn in receiveMessage_Q" + ce.getMessage(), ce);
        }
      }
    }
    return receivedM;
  }

  public String getMessageID() {
    Message msg = receiveMessage_Q();

    String id = null;
    try {
      id = msg.getJMSMessageID();
    } catch (Exception e) {
      TestUtil.logErr("Exception calling getJMSMessageID in getMessageID: ", e);
    }
    return id;
  }

  public long getTimeStamp() {
    Message msg = receiveMessage_Q();
    long JMSTimestamp = 0L;
    try {
      JMSTimestamp = msg.getJMSTimestamp();
    } catch (Exception e) {
      TestUtil.logErr("Exception calling getJMSTimestamp in getTimeStamp: ", e);
    }
    return JMSTimestamp;
  }

  public String getCorrelationID() {
    String jmsCorrelationID = null;
    Message msg = receiveMessage_Q();

    try {
      jmsCorrelationID = msg.getJMSCorrelationID();
    } catch (Exception e) {
      TestUtil.logErr(
          "Exception calling getJMSCorrelationID in getCorrelationID: ", e);
    }
    return jmsCorrelationID;
  }

  public String getReplyTo() {
    Message msg = receiveMessage_Q();
    Destination replyto = null;

    try {
      replyto = msg.getJMSReplyTo();
      if (replyto != null)
        return ((Queue) replyto).getQueueName();
      else
        return null;
    } catch (Exception e) {
      TestUtil.logErr("Exception calling getJMSReplyTo in getReplyTo: ", e);
      return null;
    }
  }

  public String getType() {
    Message msg = receiveMessage_Q();
    String jmsType = null;

    try {
      jmsType = msg.getJMSType();
    } catch (Exception e) {
      TestUtil.logErr("Exception calling getJMSType in getType: ", e);
    }
    return jmsType;
  }

  public int getPriority() {
    Message msg = receiveMessage_Q();
    int jmsPriority = 0;

    try {
      jmsPriority = msg.getJMSPriority();
    } catch (Exception e) {
      TestUtil.logErr("Exception calling getJMSPriority in getPriority: ", e);
    }
    return jmsPriority;
  }

  public long getExpiration() {
    Message msg = receiveMessage_Q();
    long jmsExpiration = 0L;

    try {
      jmsExpiration = msg.getJMSExpiration();
    } catch (Exception e) {
      TestUtil.logErr("Exception calling getJMSExpiration in getExpiration: ",
          e);
    }
    return jmsExpiration;
  }

  public String getDestination_1() {
    String tmp = null;
    try {
      TestUtil.logTrace("Getting Destination " + TESTQUEUENAME);
      Destination dest = (Destination) nctx.lookup(TESTQUEUENAME);
      tmp = ((Queue) dest).getQueueName();

    } catch (Exception e) {
      TestUtil.logErr("Exception in getDestination_1: ", e);
    }
    return tmp;
  }

  public String getDestination() {
    Message msg = receiveMessage_Q();
    String tmp = null;

    try {
      tmp = ((Queue) msg.getJMSDestination()).getQueueName();
    } catch (Exception e) {
      TestUtil.logErr("Exception calling getJMSDestination in getDestination: ",
          e);
    }
    return tmp;
  }

  public int getDeliveryMode() {
    Message msg = receiveMessage_Q();
    int jmsMode = 0;

    try {
      jmsMode = msg.getJMSDeliveryMode();
    } catch (Exception e) {
      TestUtil.logErr(
          "Exception calling getJMSDeliveryMode in getDeliveryMode: ", e);
    }
    return jmsMode;
  }

  public String getText() {
    Message msg = receiveMessage_Q();
    String text = null;

    try {
      text = ((TextMessage) msg).getText();
    } catch (Exception e) {
      TestUtil.logErr("Exception calling getText in getText: ", e);
    }
    return text;
  }

}
