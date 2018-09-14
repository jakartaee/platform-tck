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
package com.sun.ts.tests.ejb.ee.sec.stateful.mdb;

import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueSession;
import javax.jms.QueueSender;
import javax.jms.QueueReceiver;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.jms.Message;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.RemoteLoggingInitException;

import java.util.Properties;
import java.util.Enumeration;

public class MDB_SND_TestEJB implements SessionBean {
  private SessionContext sessionContext;

  private java.util.Properties p = null;

  private transient Queue rcvrQueue;

  private transient QueueConnection qConnect;

  private transient Queue q;

  private transient QueueConnectionFactory qFactory;

  TSNamingContext context;

  private long timeout;

  private String jmsUser;

  private String jmsPassword;

  public MDB_SND_TestEJB() {
  }

  public void ejbCreate(java.util.Properties props) throws CreateException {
    p = props;
    try {
      TestUtil.init(props);
      // get props
      timeout = Long.parseLong(props.getProperty("jms_timeout"));
      // check props for errors
      if (timeout < 1) {
        throw new Exception("'timeout' (milliseconds) in cts.jte must be > 0");
      }
      jmsUser = props.getProperty("authuser");
      jmsPassword = props.getProperty("authpassword");
      TestUtil.logTrace("@ejbCreate:" + jmsUser + " " + jmsPassword);
      // get a connection object
      // get context
      context = new TSNamingContext();
      TestUtil.logTrace("got the context");
      //
      qFactory = (QueueConnectionFactory) context
          .lookup("java:comp/env/jms/MyQueueConnectionFactory");
      TestUtil.logTrace("looked up queue connection factory object");
      q = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE");
      TestUtil.logTrace("looked up the queue");

      rcvrQueue = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE_REPLY");

    } catch (Exception e) {
      TestUtil.logErr("init failed", e);
    }
  }

  public boolean askMDBToSendAMessage(String typeOfMessage) {
    TestUtil.logTrace("@askMDBToSendAMessage");
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
      msg.setText(myMessage);
      msg.setStringProperty("MessageType", typeOfMessage);
      // Pass the properties object for the TestUtil logging mechanism.
      // If this is not passed then logging could become problematic in multi vm
      // mode.
      // msg.setObjectProperty("properties", p);
      // send the message
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
        TestUtil.logErr("Error closing QueueConnection", e);
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
      TestUtil.logTrace("@checkOnResponse");
      qConnect = qFactory.createQueueConnection(jmsUser, jmsPassword);
      QueueSession session = qConnect.createQueueSession(true, 0);
      qConnect.start();
      status = recvMessageInternal(session, prop);
      TestUtil.logMsg("Close the session" + status);
      session.close();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception in checkOnResponse:", e);
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
    TestUtil.logTrace("@recvMessageInternal");
    // Create a message producer.
    QueueReceiver rcvr = session.createReceiver(rcvrQueue);
    // dequeue the response from the mdb
    Message msgRec = null;
    for (int i = 0; i < 50; ++i) {
      TestUtil
          .logTrace("@recvMessageInternal trying to receive the message: " + i);
      msgRec = rcvr.receive(timeout);
      if (msgRec != null) {
        break;
      }
    } // end for loop
    if (msgRec != null) {
      if (msgRec.getStringProperty("MessageType").equals(prop)) {
        TestUtil.logTrace("Success: received Msg from Q!  "
            + msgRec.getStringProperty("MessageType"));
        TestUtil.logTrace("Pass: we got the expected msg back! ");
        retcode = true;
      } else {
        TestUtil.logTrace("Fail: we didnt get the expected msg back! ");
        TestUtil.logTrace(
            "Msg from Q:  " + msgRec.getStringProperty("MessageType"));
      }
    } else if (msgRec == null) {
      TestUtil.logTrace("Fail: we didnt get the expected msg back! ");
    }
    return retcode;
  }

  public boolean isThereSomethingInTheQueue() {
    TestUtil.logTrace("@isThereSomethingInTheQueue");
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
      TestUtil.logErr("Unexpected Exception in isThereSomethingInTheQueue:", e);
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
    TestUtil.logTrace("@cleanTheQueue");
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
      qBrowser.close();
      session.close();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception in cleanTheQueue:", e);
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

  public void setSessionContext(SessionContext sc) {
    TestUtil.logTrace("@setSessionContext");
    sessionContext = sc;
  }

  public void ejbRemove() {
    TestUtil.logTrace("@ejbRemove");
  }

  public void ejbActivate() {
    TestUtil.logTrace("@ejbActivate");
    try {
      TestUtil.logTrace("look up queue connection factory object");
      qFactory = (QueueConnectionFactory) context
          .lookup("java:comp/env/jms/MyQueueConnectionFactory");
      TestUtil.logTrace("looked up the queue");
      q = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE");
      rcvrQueue = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE_REPLY");

    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception in activation:", e);
    }

  }

  public void ejbPassivate() {
    TestUtil.logTrace("@ejbPassivate");
    rcvrQueue = null;
    qConnect = null;
    q = null;
    qFactory = null;
  }

}
