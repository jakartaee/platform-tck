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
package com.sun.ts.tests.jms.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.jms.*;

public final class Cleanup {
  private String user = null;

  private String pass = null;

  private ConnectionFactory cf = null;

  public static final String JMSDEFAULT = "jmsDefault";

  /**
   * Default constructor
   */
  public Cleanup() {
    this(JMSDEFAULT, JMSDEFAULT, null);
  }

  /**
   * Second constructor
   *
   * @param ConnectionFactory
   *          connfactory the connection factory object
   */
  public Cleanup(ConnectionFactory cf) {
    this(JMSDEFAULT, JMSDEFAULT, cf);
  }

  /**
   * Third constructor
   *
   * @param String
   *          user the username credentials
   * @param String
   *          pass the password credentials
   * @param ConnectionFactory
   *          connfactory the connection factory object
   */
  public Cleanup(String user, String pass, ConnectionFactory cf) {
    this.user = user;
    this.pass = pass;
    this.cf = cf;
  }

  /**
   * Use this method at cleanup time to remove any connections and messages that
   * have remained on the queue.
   *
   * @param ArrayList
   *          connections list of open connections
   * @param ArrayList
   *          queues list of queues to flush
   */
  public void doClientQueueTestCleanup(ArrayList connections,
      ArrayList queues) {
    TestUtil.logTrace("Entering doClientQueueTestCleanup()");
    try {
      closeAllConnections(connections);
      flushQueue(queues);
      if (queues != null) {
        queues.clear();
      }

      if (connections != null) {
        connections.clear();
      }
    } catch (Exception e) {
      TestUtil.logTrace("Ignoring exception: " + e);
    }
    TestUtil.logTrace("Leaving doClientQueueTestCleanup()");
  }

  /**
   * Close any connections opened by the tests
   *
   * @param ArrayList
   *          connections list of connections to close
   */
  public void closeAllConnections(ArrayList connections) {
    TestUtil.logTrace("Entering closeAllConnections()");
    try {
      if (connections != null) {
        if (!connections.isEmpty()) {
          for (int i = 0; i < connections.size(); i++) {
            ((Connection) connections.get(i)).close();
          }
        }
      }
    } catch (Exception e) {
    }
    TestUtil.logTrace("Leaving closeAllConnections()");
  }

  /**********************************************************************************
   * flushDestination(Destination)
   *
   * Use this method at cleanup time to remove any messages that have remained
   * on the queue.
   **********************************************************************************/
  public void flushDestination(Destination destination) throws Exception {
    Connection conn = null;
    MessageConsumer consumer = null;
    MessageProducer producer = null;
    Session sess = null;
    ObjectMessage msg = null;
    int priority = 0;
    int numMsgsFlushed = 0;

    TestUtil.logTrace("Entering flushDestination()");
    try {
      TestUtil.logTrace(
          "Create new Connection,Session,MessageProducer,MessageConsumer to flush Destination");
      conn = cf.createConnection(user, pass);
      sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
      producer = sess.createProducer(destination);
      consumer = sess.createConsumer(destination);
      conn.start(); // start the connections so that messages may be received.

      // send a low priority message
      // any other messages on the queue should be received first
      // and low priority message should signal the end
      msg = sess.createObjectMessage();
      msg.setObject("Flush Destination");
      msg.setStringProperty("COM_SUN_JMS_TESTNAME", "flushDestination");
      TestUtil.logTrace(
          "Send low priority message to Destination to signal the last message");
      producer.send(msg, javax.jms.Message.DEFAULT_DELIVERY_MODE, priority,
          javax.jms.Message.DEFAULT_TIME_TO_LIVE);

      // flush the Destination
      TestUtil.logTrace("Now flush the Destination");
      Message rmsg = consumer.receive(5000);
      while (rmsg != null) {
        String tname = rmsg.getStringProperty("COM_SUN_JMS_TESTNAME");
        if (tname != null && tname.equals("flushDestination")) {
          // Should be last message (try receiveNoWait() to make sure it is)
          rmsg = consumer.receiveNoWait();
          while (rmsg != null) {
            numMsgsFlushed++;
            rmsg = consumer.receiveNoWait();
          }
          break;
        } else {
          numMsgsFlushed++;
          rmsg = consumer.receiveNoWait();
        }
      }
      if (numMsgsFlushed > 0) {
        TestUtil.logTrace("#######flushed " + numMsgsFlushed + " messages");
      }
    } catch (Exception e) {
      TestUtil.logErr("flushDestination exception: " + e.toString());
      TestUtil.printStackTrace(e);
    } finally {
      try {
        conn.close();
      } catch (Exception e) {
      }
    }
    TestUtil.logTrace("Leaving flushDestination()");
  }

  /**********************************************************************************
   * flushQueue(ArrayList)
   *
   * Use this method at cleanup time to remove any messages that have remained
   * on the queue.
   *
   * @param Queue
   *          qToFlush[] QUEUES
   **********************************************************************************/
  public void flushQueue(ArrayList queues) throws Exception {
    Connection qc = null;
    MessageConsumer qconsumer = null;
    Session qs = null;
    ObjectMessage msg = null;
    Enumeration msgs = null;
    int priority = 0;
    int numMsgsFlushed = 0;
    int numMsgs = 0;

    TestUtil.logTrace("Entering flushQueue(Arraylist)");
    try {
      TestUtil.logTrace("Create new Connection,Session to flush Queue");
      qc = cf.createConnection(user, pass);
      qs = qc.createSession(false, Session.AUTO_ACKNOWLEDGE);
      qc.start(); // start the connections so that messages may be received.

      for (int i = 0; i < queues.size(); i++) {
        TestUtil.logTrace(
            "Create QueueBrowser to count number of messages left on Queue");
        QueueBrowser qBrowser = qs.createBrowser((Queue) queues.get(i));
        // count the number of messages
        msgs = qBrowser.getEnumeration();
        while (msgs.hasMoreElements()) {
          msgs.nextElement();
          numMsgs++;
        }

        if (numMsgs == 0) {
          TestUtil.logTrace("No messages left on Queue "
              + ((Queue) queues.get(i)).getQueueName());
        } else {
          TestUtil.logTrace(numMsgs + " messages left on Queue "
              + ((Queue) queues.get(i)).getQueueName());

          TestUtil.logTrace(
              "Create new MessageConsumer to flush messages in Queue");
          qconsumer = qs.createConsumer((Queue) queues.get(i));

          // flush the queue
          TestUtil.logTrace("Now flush the Queue");
          Message rmsg = qconsumer.receive(5000);
          while (rmsg != null) {
            numMsgsFlushed++;
            rmsg = qconsumer.receiveNoWait();
            if (rmsg == null) {
              // Should be last message (try receiveNoWait() one more time to
              // make sure it is)
              rmsg = qconsumer.receiveNoWait();
            }
          }

          if (numMsgsFlushed > 0) {
            TestUtil.logTrace("Flushed " + numMsgsFlushed + " messages");
          }
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("flushQueue exception: " + e.toString());
      TestUtil.printStackTrace(e);
    } finally {
      try {
        qc.close();
      } catch (Exception e) {
      }
    }
    TestUtil.logTrace("Leaving flushQueue(ArrayList)");
  }

  /**********************************************************************************
   * flushQueue(Queue, Session)
   *
   * Use this method at cleanup time to remove any messages that have remained
   * on the queue.
   *
   * @param Queue
   *          queue the queue to flush
   * @param Session
   *          session the session
   **********************************************************************************/
  public void flushQueue(Queue queue, Session session) throws Exception {
    int numMsgsFlushed = 0;
    int numMsgs = 0;
    Enumeration msgs = null;

    TestUtil.logTrace("Entering flushQueue(Queue, Session)");
    try {
      QueueBrowser qBrowser = session.createBrowser(queue);
      MessageConsumer consumer = session.createConsumer(queue);
      // count the number of messages
      msgs = qBrowser.getEnumeration();
      while (msgs.hasMoreElements()) {
        msgs.nextElement();
        numMsgs++;
      }

      if (numMsgs == 0) {
        TestUtil.logTrace("No messages left on Queue " + queue.getQueueName());
      } else {
        TestUtil.logTrace(
            numMsgs + " messages left on Queue " + queue.getQueueName());
        if (consumer != null) {
          // flush the queue
          Message msg = consumer.receiveNoWait();
          while (msg != null) {
            numMsgsFlushed++;
            msg = consumer.receiveNoWait();
          }
          if (numMsgsFlushed > 0) {
            TestUtil.logTrace("Flushed " + numMsgsFlushed + " messages");
          }

          // if Session is transacted be sure to commit consumed messages
          if (numMsgsFlushed > 0 && session.getTransacted()) {
            session.commit();
          }
        }
      }
    } catch (Exception e) {
    }
    TestUtil.logTrace("Leaving flushQueue(Queue, Session)");
  }
}
