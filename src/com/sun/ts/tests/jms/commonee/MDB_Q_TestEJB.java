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
 * $Id: MDB_Q_TestEJB.java 59995 2009-10-14 12:05:29Z af70133 $
 */
package com.sun.ts.tests.jms.commonee;

import javax.jms.*;
import javax.ejb.*;
import javax.annotation.Resource;
import java.util.Enumeration;
import java.util.Properties;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.jms.common.*;

@Stateful
@Remote(MDB_Q_Test.class)
public class MDB_Q_TestEJB implements MDB_Q_Test {

  @Resource
  private SessionContext sessionContext;

  private Properties p = null;

  @Resource(name = "jms/MDB_QUEUE_REPLY")
  private transient Queue rcvrQueue;

  @Resource(name = "jms/MDB_QUEUE")
  private transient Queue q;

  @Resource(name = "jms/MyQueueConnectionFactory")
  private transient QueueConnectionFactory qFactory;

  private transient QueueConnection qConnect;

  private String jmsUser;

  private String jmsPassword;

  private long timeout;

  public MDB_Q_TestEJB() {
  }

  public void setup(Properties props) {
    TestUtil.logTrace("MDB_Q_TestEJB.setup()");
    p = props;
    try {
      TestUtil.init(props);
      // get props
      timeout = Long.parseLong(props.getProperty("jms_timeout"));
      jmsUser = props.getProperty("user");
      jmsPassword = props.getProperty("password");
      // check props for errors
      if (timeout < 1) {
        throw new Exception(
            "'jms_timeout' (milliseconds) in ts.jte must be > 0");
      }
      if (jmsUser == null) {
        throw new Exception("'user' in ts.jte must be null");
      }
      if (jmsPassword == null) {
        throw new Exception("'password' in ts.jte must be null");
      }
      if (qFactory == null || q == null | rcvrQueue == null
          || sessionContext == null) {
        throw new Exception("@Resource injection failed");
      }
    } catch (Exception e) {
      throw new EJBException("@setup failed: ", e);
    }
  }

  public boolean askMDBToRunATest(String typeOfTest) {
    TestUtil.logTrace("MDB_Q_TestEJB.askMDBToRunATest()");
    boolean ok = true;
    String myMessage = "Sending a message to mdb";

    try {
      qConnect = qFactory.createQueueConnection(jmsUser, jmsPassword);
      QueueSession session = qConnect.createQueueSession(true, 0);
      qConnect.start();
      QueueSender qSender = session.createSender(q);

      // create a text message
      TextMessage msg = session.createTextMessage();
      JmsUtil.addPropsToMessage(msg, p);
      msg.setText(myMessage);
      msg.setStringProperty("TestCase", typeOfTest);

      qSender.send(msg);

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("askMDBToRunATest: Error!", e);
    } finally {
      try {
        if (qConnect != null) {
          qConnect.close();
        }
      } catch (Exception e) {
        TestUtil.logErr("Error closing QueueConnection", e);
      }
    }
    return ok;
  }

  public boolean askMDBToSendAMessage(String typeOfMessage) {
    TestUtil.logTrace("MDB_Q_TestEJB.askMDBToSendAMessage()");
    boolean ok = true;
    String myMessage = "I want you to send a message";

    try {
      qConnect = qFactory.createQueueConnection(jmsUser, jmsPassword);
      QueueSession session = qConnect.createQueueSession(true, 0);
      qConnect.start();

      QueueSender qSender = session.createSender(q);
      TestUtil
          .logTrace("got a q sender for: " + qSender.getQueue().getQueueName());

      // create a text message
      TextMessage msg = session.createTextMessage();
      JmsUtil.addPropsToMessage(msg, p);
      msg.setText(myMessage);
      msg.setStringProperty("MessageType", typeOfMessage);

      qSender.send(msg);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("@askMDBToSendAMessage: Error!", e);
    } finally {
      try {
        if (qConnect != null) {
          qConnect.close();
        }
      } catch (Exception e) {
        TestUtil.logErr("Error closing connection in askMDBToSendAMessage", e);
      }
    }
    return ok;
  }

  // Validate that a given message was sent by mdb
  // prop = validate string
  //
  public boolean checkOnResponse(String prop) {

    boolean status = false;
    try {
      TestUtil.logTrace("MDB_Q_TestEJB.checkOnResponse()");
      qConnect = qFactory.createQueueConnection(jmsUser, jmsPassword);
      QueueSession session = qConnect.createQueueSession(true, 0);
      qConnect.start();
      status = recvMessageInternal(session, prop);
      TestUtil.logTrace("Close the session");
      session.close();
    } catch (Exception e) {
      TestUtil.logErr("Error in checkOnResponse", e);
    } finally {
      try {
        if (qConnect != null) {
          qConnect.close();
        }
      } catch (Exception e) {
        TestUtil.logErr("Error closing QueueConnection", e);
      }
    }
    return status;
  }

  private boolean recvMessageInternal(QueueSession session, String prop)
      throws JMSException {
    boolean retcode = false;
    TestUtil.logTrace("MDB_Q_TestEJB.recvMessageInternal()");
    // Create a message producer.
    QueueReceiver rcvr = session.createReceiver(rcvrQueue);
    // dequeue the response from the mdb
    Message msgRec = null;

    for (int i = 0; i < 10; ++i) {
      TestUtil
          .logTrace("@recvMessageInternal trying to receive the message: " + i);
      msgRec = rcvr.receive(timeout);
      if (msgRec != null) {
        break;
      }
    } // end for loop

    if (msgRec != null) {
      if ((msgRec.getStringProperty("TestCase") != null)
          || (msgRec.getStringProperty("Status") != null)) {
        if (msgRec.getStringProperty("TestCase").equals(prop)
            && msgRec.getStringProperty("Status").equals("Pass")) {
          TestUtil
              .logTrace("TestCase: " + msgRec.getStringProperty("TestCase"));
          TestUtil.logTrace(
              "Status from msg: " + msgRec.getStringProperty("Status"));
          TestUtil.logTrace("Pass: we got the expected msg back! ");
          retcode = true;

        } else if (msgRec.getStringProperty("Status").equals("Fail")) {
          TestUtil
              .logTrace("TestCase: " + msgRec.getStringProperty("TestCase"));
          TestUtil.logTrace(
              "Status from msg: " + msgRec.getStringProperty("Status"));
          TestUtil.logTrace("Fail: Error(s) occurred! ");
        } else {
          TestUtil.logTrace("Fail: we didnt get the expected msg back! ");
          TestUtil
              .logTrace("TestCase:  " + msgRec.getStringProperty("TestCase"));
        }
      } else if (msgRec.getStringProperty("MessageType") != null) {
        if (msgRec.getStringProperty("MessageType").equals(prop)) {
          TestUtil.logTrace("Success: received Msg from Q!  "
              + msgRec.getStringProperty("MessageType"));
          TestUtil.logTrace("Pass: we got the expected msg back! ");
          retcode = true;

        } else {
          TestUtil.logTrace("Fail: we didnt get the expected msg back! ");
          TestUtil.logTrace(
              "MessageType:  " + msgRec.getStringProperty("MessageType"));
        }
      } else {
        TestUtil.logTrace("Fail: we didnt get the expected msg back! ");
      }
    } else {
      TestUtil.logTrace("Fail: we didnt get any msg back! ");
    }
    return retcode;
  }

  public boolean isThereSomethingInTheQueue() {
    TestUtil.logTrace("MDB_Q_TestEJB.isThereSomethingInTheQueue()");
    QueueBrowser qBrowser = null;
    Enumeration msgs = null;
    boolean ret = false;

    try {
      // Hopefully nothing is left in the queue
      qConnect = qFactory.createQueueConnection(jmsUser, jmsPassword);
      QueueSession session = qConnect.createQueueSession(true, 0);
      qConnect.start();
      qBrowser = session.createBrowser(rcvrQueue);
      msgs = qBrowser.getEnumeration();
      if (msgs.hasMoreElements()) {
        ret = true;
      }
      qBrowser.close();
      session.close();

    } catch (Exception e) {
      TestUtil.logErr("Error in isThereSomethingInTheQueue", e);
    } finally {
      try {
        if (qConnect != null) {
          qConnect.close();
        }
      } catch (Exception e) {
        TestUtil.logErr("Error closing QueueConnection", e);
      }
    }
    return ret;
  }

  public void cleanTheQueue() {
    TestUtil.logTrace("MDB_Q_TestEJB.cleanTheQueue()");
    QueueBrowser qBrowser = null;
    Enumeration msgs = null;
    int numMsgs = 0;

    try {
      qConnect = qFactory.createQueueConnection(jmsUser, jmsPassword);
      QueueSession session = qConnect.createQueueSession(true, 0);
      qConnect.start();
      TextMessage msgRec = null;

      // delete anything left in the queue
      qBrowser = session.createBrowser(rcvrQueue);
      // count the number of messages
      msgs = qBrowser.getEnumeration();
      while (msgs.hasMoreElements()) {
        msgs.nextElement();
        numMsgs++;
      }
      qBrowser.close();

      // Read messages until Q is cleaned
      QueueReceiver rcvr = session.createReceiver(rcvrQueue);
      TestUtil.logTrace("Cleaning " + numMsgs + " messages from the Q: "
          + rcvrQueue.getQueueName());
      for (int n = 0; n < numMsgs; n++) {
        //
        TestUtil.logTrace(
            "dequeuing msg: " + n + " from the Q: " + rcvrQueue.getQueueName());
        for (int i = 0; i < 10; ++i) {
          msgRec = (TextMessage) rcvr.receive(timeout);
          if (msgRec != null) {
            TestUtil.logTrace("dequeued message: " + n);
            break;
          }
          TestUtil.logTrace(
              "Attempt no: " + i + " Trying to dequeue message: " + n);
        } // end of internal for loop
      }
      session.close();
    } catch (Exception e) {
      TestUtil.logErr("Error in cleanTheQueue", e);
    } finally {
      try {
        if (qConnect != null) {
          qConnect.close();
        }
      } catch (Exception e) {
        TestUtil.logErr("Error closing QueueConnection", e);
      }
    }
  }

  @Remove
  public void remove() {
    TestUtil.logTrace("MDB_Q_TestEJB.remove()");
  }

  @PostActivate
  public void activate() {
    TestUtil.logTrace("MDB_Q_TestEJB.activate()");

    try {
      TSNamingContext context = new TSNamingContext();
      TestUtil.logTrace("got the context");

      rcvrQueue = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE_REPLY");
      q = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE");
      qFactory = (QueueConnectionFactory) context
          .lookup("java:comp/env/jms/MyQueueConnectionFactory");
    } catch (Exception e) {
      TestUtil.logErr("Error looking up Queue, QueueConnectionFactory objects",
          e);
      throw new EJBException("@activate: Error!", e);
    }
  }

  @PrePassivate
  public void passivate() {
    TestUtil.logTrace("MDB_Q_TestEJB.passivate()");

    if (qConnect != null) {
      try {
        qConnect.close();
      } catch (Exception e) {
        TestUtil.logErr("Error closing QueueConnection", e);
      }
      qConnect = null;
    }
    rcvrQueue = null;
    q = null;
    qFactory = null;
  }
}
