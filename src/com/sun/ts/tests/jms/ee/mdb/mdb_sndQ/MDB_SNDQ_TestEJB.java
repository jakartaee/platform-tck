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
package com.sun.ts.tests.jms.ee.mdb.mdb_sndQ;

import javax.jms.*;
import javax.ejb.*;
import javax.annotation.Resource;
import java.util.Properties;
import java.util.Enumeration;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.jms.common.*;

@Stateful
@Remote(MDB_SNDQ_Test.class)
public class MDB_SNDQ_TestEJB {

  @Resource
  private SessionContext sessionContext;

  @Resource(name = "jms/MDB_QUEUE")
  private transient Queue Dest;

  @Resource(name = "jms/MDB_QUEUE_REPLY")
  private transient Queue receiveDest;

  @Resource(name = "jms/MyQueueConnectionFactory")
  private transient ConnectionFactory cf;

  private transient Connection Conn;

  private Properties p = null;

  private long timeout;

  private String jmsUser;

  private String jmsPassword;

  public MDB_SNDQ_TestEJB() {
  }

  public void setup(Properties props) {
    TestUtil.logTrace("MDB_SNDQ_TestEJB.askMDBToSendAMessage()");
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
        throw new Exception("'user' in ts.jte must not be null");
      }
      if (jmsPassword == null) {
        throw new Exception("'password' in ts.jte must not be null");
      }
      if (cf == null || Dest == null || receiveDest == null
          || sessionContext == null) {
        throw new Exception("@Resource injection failed");
      }
    } catch (Exception e) {
      throw new EJBException("@setup failed: ", e);
    }
  }

  public boolean askMDBToSendAMessage(String typeOfMessage) {
    TestUtil.logTrace("MDB_SNDQ_TestEJB.askMDBToSendAMessage()");
    boolean ok = true;
    String myMessage = "I want you to send a message";
    try {
      Conn = cf.createConnection(jmsUser, jmsPassword);
      Session session = Conn.createSession(true, 0);
      Conn.start();

      MessageProducer mSender = session.createProducer(Dest);

      // create a text message
      TextMessage msg = session.createTextMessage();
      JmsUtil.addPropsToMessage(msg, p);
      msg.setText(myMessage);
      msg.setStringProperty("MessageType", typeOfMessage);

      // send the message
      mSender.send(msg);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("@askMDBToSendAMessage: Error!", e);
    } finally {
      try {
        if (Conn != null) {
          Conn.close();
        }
      } catch (Exception e) {
        TestUtil.logErr("Error closing connection in askMDBToSendAMessage", e);
      }
    }
    return ok;
  }

  // Validate that a given message was sent by mdb
  // prop = validate string
  public boolean checkOnResponse(String prop) {
    boolean status = false;
    try {
      TestUtil.logTrace("MDB_SNDQ_TestEJB.checkOnResponse()");
      Conn = cf.createConnection(jmsUser, jmsPassword);
      Conn.start();

      Session session = Conn.createSession(true, 0);
      status = recvMessageInternal(session, prop);
      TestUtil.logTrace("Close the session");
      session.close();
    } catch (Exception e) {
      TestUtil.logErr("Error in checkOnResponse", e);
    } finally {
      try {
        if (Conn != null) {
          Conn.close();
        }
      } catch (Exception e) {
        TestUtil.logErr("Error closing connection in checkOnResponse", e);
      }
    }
    return status;
  }

  private boolean recvMessageInternal(Session session, String prop)
      throws JMSException {
    boolean retcode = false;
    TestUtil.logTrace("MDB_SNDQ_TestEJB.recvMessageInternal()");

    // Create a message producer.
    MessageConsumer rcvr = session.createConsumer(receiveDest);

    // remove the response from the mdb
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
      if (msgRec.getStringProperty("MessageType").equals(prop)) {
        TestUtil.logTrace("Success: received Msg from Q!  "
            + msgRec.getStringProperty("MessageType"));
        TestUtil.logTrace("Pass: we got the expected msg back! ");
        retcode = true;
      } else {
        TestUtil.logErr("Fail: we didnt get the expected msg back! ");
        TestUtil
            .logErr("Msg from Q:  " + msgRec.getStringProperty("MessageType"));
      }
    } else {
      TestUtil.logErr("Fail: we didnt get the expected msg back! ");
    }
    return retcode;
  }

  public boolean isThereSomethingInTheQueue() {
    TestUtil.logTrace("MDB_SNDQ_TestEJB.isThereSomethingInTheQueue()");
    QueueBrowser qBrowser = null;
    Enumeration msgs = null;
    boolean ret = false;
    try {
      // Hopefully nothing is left in the Destination
      Conn = cf.createConnection(jmsUser, jmsPassword);
      Conn.start();

      Session session = Conn.createSession(true, 0);
      qBrowser = session.createBrowser((javax.jms.Queue) receiveDest);
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
        if (Conn != null) {
          Conn.close();
        }
      } catch (Exception e) {
        TestUtil.logErr(
            "Error closing connection in isThereSomethingInTheQueue", e);
      }
    }
    return ret;
  }

  public void cleanTheQueue() {
    TestUtil.logTrace("MDB_SNDQ_TestEJB.cleanTheQueue()");

    QueueBrowser qBrowser = null;
    Enumeration msgs = null;
    int numMsgs = 0;
    TextMessage msgRec = null;
    MessageConsumer rcvr = null;

    try {
      Conn = cf.createConnection(jmsUser, jmsPassword);
      Session session = Conn.createSession(true, 0);
      Conn.start();

      // delete anything left in the Destination
      qBrowser = session.createBrowser((Queue) receiveDest);
      // count the number of messages
      msgs = qBrowser.getEnumeration();
      while (msgs.hasMoreElements()) {
        msgs.nextElement();
        numMsgs++;
      }
      qBrowser.close();

      // Read messages until Destination is cleaned
      rcvr = session.createConsumer(receiveDest);
      for (int n = 0; n < numMsgs; n++) {
        for (int i = 0; i < 10; ++i) {
          msgRec = (TextMessage) rcvr.receive(timeout);
          if (msgRec != null) {
            TestUtil.logTrace("Removed message from Destination: " + n);
            break;
          }
          TestUtil
              .logTrace("Attempt no: " + i + " Trying to delete message: " + n);
        } // end of internal for loop
      }
      session.close();
    } catch (Exception e) {
      TestUtil.logErr("Error in cleanTheQueue", e);
    } finally {
      try {
        if (Conn != null) {
          Conn.close();
        }
      } catch (Exception e) {
        TestUtil.logErr("Error closing connection in cleanTheQueue", e);
      }
    }
  }

  @Remove
  public void remove() {
    TestUtil.logTrace("MDB_SNDQ_TestEJB.remove()");
  }

  @PostActivate
  public void activate() {
    TestUtil.logTrace("MDB_SNDQ_TestEJB.activate()");
    try {
      TSNamingContext context = new TSNamingContext();
      cf = (ConnectionFactory) context
          .lookup("java:comp/env/jms/MyQueueConnectionFactory");
      TestUtil.logTrace("looked up connection factory object");

      Dest = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE");
      TestUtil.logTrace("looked up the Destination");
      receiveDest = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE_REPLY");
    } catch (Exception e) {
      TestUtil.logErr(
          "Error looking up Queue, Reply Queue, and ConnectionFactory objects",
          e);
      throw new EJBException("@activate: Error!", e);
    }
  }

  @PrePassivate
  public void passivate() {
    TestUtil.logTrace("MDB_SNDQ_TestEJB.passivate()");
    receiveDest = null;
    Conn = null;
    Dest = null;
    cf = null;
  }
}
