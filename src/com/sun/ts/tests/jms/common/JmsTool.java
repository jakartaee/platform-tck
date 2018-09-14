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
import java.util.ArrayList;
import java.util.Enumeration;
import java.rmi.RemoteException;
import javax.jms.*;

public class JmsTool {

  // QUEUE declarations
  private Queue testQueue = null;

  private QueueConnection qConnection = null;

  private QueueSession qSession = null;

  private QueueReceiver qReceiver = null;

  private QueueSender qSender = null;

  // TOPIC declarations
  private Topic testTopic = null;

  private TopicConnection tConnection = null;

  private TopicSession tSession = null;

  private TopicSubscriber tSubscriber = null;

  private TopicPublisher tPublisher = null;

  // COMMON declarations
  private Destination testDestination = null;

  private Connection conn = null;

  private Session sess = null;

  private MessageConsumer receiver = null;

  private MessageProducer sender = null;

  private boolean durableTopic = false;

  private boolean transacted = false;

  private int ttype = 0;

  private int ftype = 5;

  private String username = null;

  private String password = null;

  private String jndiLookupName = null;

  // constants
  public static final int QUEUE = 0;

  public static final int TOPIC = 1;

  public static final int TX_QUEUE = 2;

  public static final int TX_TOPIC = 3;

  public static final int DURABLE_TOPIC = 4;

  public static final int DURABLE_TX_TOPIC = 5;

  public static final int FACTORIES_ONLY = 6;

  public static final int QUEUE_FACTORY = 7;

  public static final int TOPIC_FACTORY = 8;

  public static final int DURABLE_TOPIC_FACTORY = 9;

  public static final int FACTORY_Q = 10;

  public static final int FACTORY_T = 11;

  public static final int FACTORY_DT = 12;

  public static final int COMMON_Q = 13;

  public static final int COMMON_T = 14;

  public static final int COMMON_QTX = 15;

  public static final int COMMON_TTX = 16;

  public static final int COMMON_FACTORY = 17;

  public static final String JMS_VERSION = "2.0";

  public static final int JMS_MAJOR_VERSION = 2;

  public static final int JMS_MINOR_VERSION = 0;

  // JNDI names for JMS objects (Standalone mode)
  public static final String TCKTESTQUEUENAME = "MY_QUEUE";

  public static final String TCKTESTTOPICNAME = "MY_TOPIC";

  public static final String TCKCONNECTIONFACTORY = "MyConnectionFactory";

  public static final String TCKQUEUECONNECTIONFACTORY = "MyQueueConnectionFactory";

  public static final String TCKTOPICCONNECTIONFACTORY = "MyTopicConnectionFactory";

  public static final String TCKDURABLETOPICCONNECTIONFACTORY = "DURABLE_SUB_CONNECTION_FACTORY";

  // JNDI names for JMS objects (JavaEE mode)
  public static final String TESTQUEUENAME = "java:comp/env/jms/MY_QUEUE";

  public static final String TESTTOPICNAME = "java:comp/env/jms/MY_TOPIC";

  public static final String CONNECTIONFACTORY = "java:comp/env/jms/MyConnectionFactory";

  public static final String QUEUECONNECTIONFACTORY = "java:comp/env/jms/MyQueueConnectionFactory";

  public static final String TOPICCONNECTIONFACTORY = "java:comp/env/jms/MyTopicConnectionFactory";

  public static final String DURABLETOPICCONNECTIONFACTORY = "java:comp/env/jms/DURABLE_SUB_CONNECTION_FACTORY";

  public static final String JMSDEFAULT = "jmsDefault";

  // statics
  private TSNamingContext jndiContext = null;

  private QueueConnectionFactory qcf = null;

  private TopicConnectionFactory tcf = null;

  private TopicConnectionFactory tcf2 = null;

  private ConnectionFactory cf = null;

  private ConnectionFactory cf2 = null;

  private TSJMSObjectsInterface jmsObjects = null;

  private String mode = "javaEE";

  /**********************************************************************************
   * Public constructor. Takes a connection type and mode argument. Create
   * connection factory, connection type, and single producer/consumer for
   * either QUEUE or TOPIC client.
   * 
   * @param int
   *          type (QUEUE type or TOPIC type)
   * @param String
   *          m (JavaEE mode or Standalone mode)
   **********************************************************************************/
  public JmsTool(int type, String m) throws Exception {

    this(type, JMSDEFAULT, JMSDEFAULT, m);
  }

  /**********************************************************************************
   * Public constructor. Takes connection type, username, password, jndi lookup
   * name, and mode argument. Create connection factory, connection type, and
   * single producer/consumer for TOPIC client.
   *
   * @param int
   *          type (TOPIC type)
   * @param String
   *          user (username)
   * @param String
   *          pw (password)
   * @param String
   *          lookup (connection factory to lookup)
   * @param String
   *          m (JavaEE mode or Standalone mode)
   **********************************************************************************/
  public JmsTool(int type, String user, String pw, String lookup, String m)
      throws Exception {
    username = user;
    password = pw;
    ttype = type;
    mode = m;

    if (mode.equals("javaEE")) {
      getJNDIContext();
    } else {
      jmsObjects = TSJMSObjects.getJMSObjectsInstance();
    }

    if (type == TOPIC) {
      transacted = false;
      createTopicSetup(lookup);
    } else if (type == TX_TOPIC) {
      transacted = true;
      createTopicSetup(lookup);
    } else if (type == DURABLE_TOPIC) {
      transacted = false;
      createTopicSetup(lookup);
    } else if (type == DURABLE_TX_TOPIC) {
      transacted = true;
      createTopicSetup(lookup);
    } else if (type == COMMON_T) {
      transacted = false;
      createCommonTSetup(lookup);
    } else if (type == COMMON_TTX) {
      transacted = true;
      createCommonTSetup(lookup);
    } else {
      String eMsg = "Type must be JmsTool.TOPIC, JmsTool.TX_TOPIC, JmsTool.DURABLE_TOPIC, "
          + "JmsTool.DURABLE_TX_TOPIC, JmsTool.COMMON_T, JmsTool.COMMON_TTX.";
      throw new Exception(eMsg);
    }
  }

  /**********************************************************************************
   * Public constructor. Takes connection type, username, password, and mode
   * argument. Create connection factory, connection type, and single
   * producer/consumer for either QUEUE or TOPIC client. If just a FACTORY type
   * is passed then just create the connection factory type.
   *
   * @param int
   *          type (QUEUE type or TOPIC type or FACTORY type)
   * @param String
   *          user (username)
   * @param String
   *          pw (password)
   * @param String
   *          m (JavaEE mode or Standalone mode)
   **********************************************************************************/
  public JmsTool(int type, String user, String pw, String m) throws Exception {
    username = user;
    password = pw;
    ttype = type;
    mode = m;

    if (mode.equals("javaEE")) {
      getJNDIContext();
    } else {
      jmsObjects = TSJMSObjects.getJMSObjectsInstance();
    }

    if (type == QUEUE) {
      transacted = false;
      createQueueSetup();
    } else if (type == TX_QUEUE) {
      transacted = true;
      createQueueSetup();
    } else if (type == TOPIC) {
      durableTopic = false;
      transacted = false;
      createTopicSetup();
    } else if (type == TX_TOPIC) {
      durableTopic = false;
      transacted = true;
      createTopicSetup();
    } else if (type == DURABLE_TOPIC) {
      durableTopic = true;
      transacted = false;
      createTopicSetup();
    } else if (type == DURABLE_TX_TOPIC) {
      durableTopic = true;
      transacted = true;
      createTopicSetup();
    } else if (type == COMMON_Q) {
      transacted = false;
      createCommonQSetup();
    } else if (type == COMMON_T) {
      transacted = false;
      createCommonTSetup();
    } else if (type == COMMON_QTX) {
      transacted = true;
      createCommonQSetup();
    } else if (type == COMMON_TTX) {
      transacted = true;
      createCommonTSetup();
    } else if ((type == FACTORIES_ONLY) || (type == QUEUE_FACTORY)
        || (type == DURABLE_TOPIC_FACTORY) || (type == TOPIC_FACTORY)
        || (type == COMMON_FACTORY) || (type == FACTORY_Q)
        || (type == FACTORY_DT) || (type == FACTORY_T))
      getConnectionFactoriesOnly(type);
    else {
      String eMsg = "Type must be JmsTool.QUEUE, JmsTool.TOPIC, JmsTool.TX_QUEUE, JmsTool.TX_TOPIC, "
          + "JmsTool.DURABLE_TOPIC, JmsTool.DURABLE_TX_TOPIC, JmsTool.FACTORIES_ONLY, "
          + "JmsTool.QUEUE_FACTORY, JmsTool.TOPIC_FACTORY, JmsTool.COMMON_FACTORY, "
          + "JmsTool.FACTORY_Q, JmsTool.FACTORY_T, JmsTool.FACTORY_DT, "
          + "JmsTool.DURABLE_TOPIC_FACTORY, JmsTool.COMMON_Q, JmsTool.COMMON_T, "
          + "JmsTool.COMMON_QTX, or JmsTool.COMMON_TTX.";
      throw new Exception(eMsg);
    }
  }

  private void getJNDIContext() throws Exception {

    if (jndiContext == null) {

      try {
        TestUtil.logTrace("Getting initial context");
        jndiContext = new TSNamingContext();
      } catch (javax.naming.NamingException ne) {
        TestUtil.logErr("Could not create JNDI context because: ", ne);
        throw ne;
      }
    }
  }

  /************************************************************************
   * Used by tests that create all their own connections
   ***********************************************************************/
  private void getConnectionFactoriesOnly(int factype) throws Exception {

    try {
      ftype = factype;
      this.getConnectionFactoriesOnly();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw e;
    }
  }

  /************************************************************************
   * Used by tests that create all their own connections
   ***********************************************************************/
  private void getConnectionFactoriesOnly() throws Exception {

    if ((ftype == QUEUE_FACTORY) || (ftype == FACTORIES_ONLY)) {
      try {
        if (mode.equals("javaEE")) {
          TestUtil.logTrace(
              "Getting QueueConnectionFactory " + QUEUECONNECTIONFACTORY);
          qcf = (QueueConnectionFactory) jndiContext
              .lookup(QUEUECONNECTIONFACTORY);
          jndiLookupName = QUEUECONNECTIONFACTORY;
        } else {
          TestUtil.logTrace(
              "Getting QueueConnectionFactory " + TCKQUEUECONNECTIONFACTORY);
          qcf = (QueueConnectionFactory) jmsObjects
              .getQueueConnectionFactory(TCKQUEUECONNECTIONFACTORY);
          jndiLookupName = TCKQUEUECONNECTIONFACTORY;
        }
      } catch (Exception e) {
        TestUtil.logErr("Failed to lookup connection factory using name "
            + jndiLookupName + " because: ", e);
        TestUtil.printStackTrace(e);
        throw e;
      }
    }

    if ((ftype == TOPIC_FACTORY) || (ftype == FACTORIES_ONLY)) {
      try {
        if (mode.equals("javaEE")) {
          TestUtil.logTrace(
              "Getting TopicConnectionFactory " + TOPICCONNECTIONFACTORY);
          tcf = (TopicConnectionFactory) jndiContext
              .lookup(TOPICCONNECTIONFACTORY);
          jndiLookupName = TOPICCONNECTIONFACTORY;
        } else {
          TestUtil.logTrace(
              "Getting TopicConnectionFactory " + TCKTOPICCONNECTIONFACTORY);
          tcf = (TopicConnectionFactory) jmsObjects
              .getTopicConnectionFactory(TCKTOPICCONNECTIONFACTORY);
          jndiLookupName = TCKTOPICCONNECTIONFACTORY;
        }

      } catch (Exception e) {
        TestUtil.logErr("Failed to lookup connection factory using name "
            + jndiLookupName + " because: ", e);
        TestUtil.printStackTrace(e);
        throw e;
      }
    }

    if (ftype == DURABLE_TOPIC_FACTORY) {
      try {
        if (mode.equals("javaEE")) {
          TestUtil.logTrace("Getting Durable TopicConnectionFactory "
              + DURABLETOPICCONNECTIONFACTORY);
          tcf = (TopicConnectionFactory) jndiContext
              .lookup(DURABLETOPICCONNECTIONFACTORY);
          jndiLookupName = DURABLETOPICCONNECTIONFACTORY;
        } else {
          TestUtil.logTrace("Getting Durable TopicConnectionFactory "
              + TCKDURABLETOPICCONNECTIONFACTORY);
          tcf = (TopicConnectionFactory) jmsObjects
              .getTopicConnectionFactory(TCKDURABLETOPICCONNECTIONFACTORY);
          jndiLookupName = TCKDURABLETOPICCONNECTIONFACTORY;
        }

      } catch (Exception e) {
        TestUtil.logErr("Failed to lookup connection factory using name "
            + jndiLookupName + " because: ", e);
        TestUtil.printStackTrace(e);
        throw e;
      }
    }

    if (ftype == COMMON_FACTORY) {
      try {
        if (mode.equals("javaEE")) {
          TestUtil.logTrace("Getting ConnectionFactory " + CONNECTIONFACTORY);
          cf = (ConnectionFactory) jndiContext.lookup(CONNECTIONFACTORY);
          jndiLookupName = CONNECTIONFACTORY;
        } else {
          TestUtil
              .logTrace("Getting ConnectionFactory " + TCKCONNECTIONFACTORY);
          cf = (ConnectionFactory) jmsObjects
              .getConnectionFactory(TCKCONNECTIONFACTORY);
          jndiLookupName = TCKCONNECTIONFACTORY;
        }
      } catch (Exception e) {
        TestUtil.logErr("Failed to lookup connection factory using name "
            + jndiLookupName + " because: ", e);
        throw e;
      }
    }

    if ((ftype == FACTORY_T) || (ftype == FACTORIES_ONLY)) {
      try {
        if (mode.equals("javaEE")) {
          TestUtil
              .logTrace("Getting TopicConnectionFactory as a ConnectionFactory "
                  + TOPICCONNECTIONFACTORY);
          cf = (ConnectionFactory) jndiContext.lookup(TOPICCONNECTIONFACTORY);
          jndiLookupName = TOPICCONNECTIONFACTORY;
        } else {
          TestUtil
              .logTrace("Getting TopicConnectionFactory as a ConnectionFactory "
                  + TCKTOPICCONNECTIONFACTORY);
          cf = (ConnectionFactory) jmsObjects
              .getTopicConnectionFactory(TCKTOPICCONNECTIONFACTORY);
          jndiLookupName = TCKTOPICCONNECTIONFACTORY;
        }

      } catch (Exception e) {
        TestUtil.logErr("Failed to lookup connection factory using name "
            + jndiLookupName + " because: ", e);
        throw e;
      }
    }

    if (ftype == FACTORY_DT) {
      try {
        if (mode.equals("javaEE")) {
          TestUtil.logTrace(
              "Getting Durable TopicConnectionFactory as a ConnectionFactory "
                  + DURABLETOPICCONNECTIONFACTORY);
          cf = (ConnectionFactory) jndiContext
              .lookup(DURABLETOPICCONNECTIONFACTORY);
          jndiLookupName = DURABLETOPICCONNECTIONFACTORY;
        } else {
          TestUtil.logTrace(
              "Getting Durable TopicConnectionFactory as a ConnectionFactory "
                  + TCKDURABLETOPICCONNECTIONFACTORY);
          cf = (ConnectionFactory) jmsObjects
              .getTopicConnectionFactory(TCKDURABLETOPICCONNECTIONFACTORY);
          jndiLookupName = TCKDURABLETOPICCONNECTIONFACTORY;
        }

      } catch (Exception e) {
        TestUtil.logErr("Failed to lookup connection factory using name "
            + jndiLookupName + " because: ", e);
        throw e;
      }
    }

    if ((ftype == FACTORY_Q) || (ftype == FACTORIES_ONLY)) {
      try {
        if (mode.equals("javaEE")) {
          TestUtil
              .logTrace("Getting QueueConnectionFactory as a ConnectionFactory "
                  + QUEUECONNECTIONFACTORY);
          cf = (ConnectionFactory) jndiContext.lookup(QUEUECONNECTIONFACTORY);
          jndiLookupName = QUEUECONNECTIONFACTORY;
        } else {
          TestUtil
              .logTrace("Getting QueueConnectionFactory as a ConnectionFactory "
                  + TCKQUEUECONNECTIONFACTORY);
          cf = (ConnectionFactory) jmsObjects
              .getQueueConnectionFactory(TCKQUEUECONNECTIONFACTORY);
          jndiLookupName = TCKQUEUECONNECTIONFACTORY;
        }
        qcf = (QueueConnectionFactory) cf;
      } catch (Exception e) {
        TestUtil.logErr("Failed to lookup connection factory using name "
            + jndiLookupName + " because: ", e);
        throw e;
      }
    }

  }

  /************************************************************************
   * Queue setup using Queue specific classes/interfaces
   ************************************************************************/
  private void createQueueSetup() throws Exception {

    String eMsg = ""; // error Message if exception thrown

    try {
      if (mode.equals("javaEE")) {
        TestUtil.logTrace(
            "Getting QueueConnectionFactory " + QUEUECONNECTIONFACTORY);
        qcf = (QueueConnectionFactory) jndiContext
            .lookup(QUEUECONNECTIONFACTORY);
        eMsg = "Failed to lookup QueueConnectionFactory using name "
            + QUEUECONNECTIONFACTORY;
      } else {
        TestUtil.logTrace(
            "Getting QueueConnectionFactory " + TCKQUEUECONNECTIONFACTORY);
        qcf = (QueueConnectionFactory) jmsObjects
            .getQueueConnectionFactory(TCKQUEUECONNECTIONFACTORY);
        eMsg = "Failed to lookup QueueConnectionFactory using name "
            + TCKQUEUECONNECTIONFACTORY;
      }

      // now lookup the queue
      if (mode.equals("javaEE")) {
        TestUtil.logTrace("Getting Queue " + TESTQUEUENAME);
        testQueue = (Queue) jndiContext.lookup(TESTQUEUENAME);
        eMsg = "Failed to lookup Queue " + TESTQUEUENAME;
      } else {
        TestUtil.logTrace("Getting Queue " + TCKTESTQUEUENAME);
        testQueue = (Queue) jmsObjects.getQueue(TCKTESTQUEUENAME);
        eMsg = "Failed to lookup Queue " + TCKTESTQUEUENAME;
      }

      // create default connection
      TestUtil.logTrace("Creating QueueConnection");
      eMsg = "Failed to create queue connection using username, " + username
          + " password, " + password;
      qConnection = (QueueConnection) createNewConnection(ttype, username,
          password);

      // create default QueueSession and Queue reference
      TestUtil.logTrace("Creating QueueSession");
      eMsg = "Failed to create queue session";

      qSession = qConnection.createQueueSession(transacted,
          Session.AUTO_ACKNOWLEDGE);

      // create default consumer/producer
      TestUtil.logTrace("Creating receiver");
      eMsg = "Failed to create receiver for queue " + testQueue;
      qReceiver = qSession.createReceiver(testQueue);

      TestUtil.logTrace("Creating sender");
      eMsg = "Failed to create sender for queue " + testQueue;
      qSender = qSession.createSender(testQueue);
      TestUtil.logTrace("Success - Queue Setup done");
    } catch (Exception e) {
      TestUtil.logErr(eMsg + "due to ", e);
      TestUtil.printStackTrace(e);
      throw e;
    }
  }

  /************************************************************************
   * Topic setup using Topic specific classes/interfaces
   ************************************************************************/
  private void createTopicSetup() throws Exception {
    String eMsg = "";

    try {

      if (durableTopic) {
        if (mode.equals("javaEE")) {
          TestUtil.logTrace("Getting Durable TopicConnectionFactory "
              + DURABLETOPICCONNECTIONFACTORY);
          tcf = (TopicConnectionFactory) jndiContext
              .lookup(DURABLETOPICCONNECTIONFACTORY);
          eMsg = "Failed to lookup TopicConnectionFactory using name "
              + DURABLETOPICCONNECTIONFACTORY;
        } else {
          TestUtil.logTrace("Getting Durable TopicConnectionFactory "
              + TCKDURABLETOPICCONNECTIONFACTORY);
          tcf = (TopicConnectionFactory) jmsObjects
              .getTopicConnectionFactory(TCKDURABLETOPICCONNECTIONFACTORY);
          eMsg = "Failed to lookup TopicConnectionFactory using name "
              + TCKDURABLETOPICCONNECTIONFACTORY;
        }
      } else {
        if (mode.equals("javaEE")) {
          TestUtil.logTrace(
              "Getting TopicConnectionFactory " + TOPICCONNECTIONFACTORY);
          tcf = (TopicConnectionFactory) jndiContext
              .lookup(TOPICCONNECTIONFACTORY);
          eMsg = "Failed to lookup TopicConnectionFactory using name "
              + TOPICCONNECTIONFACTORY;
        } else {
          TestUtil.logTrace(
              "Getting TopicConnectionFactory " + TCKTOPICCONNECTIONFACTORY);
          tcf = (TopicConnectionFactory) jmsObjects
              .getTopicConnectionFactory(TCKTOPICCONNECTIONFACTORY);
          eMsg = "Failed to lookup TopicConnectionFactory using name "
              + TCKTOPICCONNECTIONFACTORY;
        }
      }

      if (mode.equals("javaEE")) {
        TestUtil.logTrace("Getting Topic " + TESTTOPICNAME);
        testTopic = (Topic) jndiContext.lookup(TESTTOPICNAME);
        eMsg = "Failed to lookup Topic " + TESTTOPICNAME;
      } else {
        TestUtil.logTrace("Getting Topic " + TCKTESTTOPICNAME);
        testTopic = (Topic) jmsObjects.getTopic(TCKTESTTOPICNAME);
        eMsg = "Failed to lookup Topic " + TCKTESTTOPICNAME;
      }

      // create default connection
      TestUtil.logTrace("Creating TopicConnection");
      eMsg = "Failed to create topic connection using username, " + username
          + " password, " + password;
      tConnection = (TopicConnection) createNewConnection(ttype, username,
          password);

      // create default TopicSession
      TestUtil.logTrace("Creating TopicSession");
      eMsg = "Failed to create topic session";
      tSession = tConnection.createTopicSession(transacted,
          Session.AUTO_ACKNOWLEDGE);

      // create default consumer/producer
      TestUtil.logTrace("Creating subscriber");
      eMsg = "Failed to create subscriber for topic " + testTopic;
      tSubscriber = tSession.createSubscriber(testTopic);

      TestUtil.logTrace("Creating publisher");
      eMsg = "Failed to create publisher for topic " + testTopic;
      tPublisher = tSession.createPublisher(testTopic);

    } catch (Exception e) {
      TestUtil.logErr(eMsg + "due to ", e);
      throw e;
    }
  }

  /************************************************************************
   * Topic setup using Topic specific classes/interfaces
   ************************************************************************/
  private void createTopicSetup(String lookup) throws Exception {
    String eMsg = "";

    try {

      TestUtil.logTrace("Getting TopicConnectionFactory " + lookup);
      if (mode.equals("javaEE")) {
        tcf = (TopicConnectionFactory) jndiContext
            .lookup("java:comp/env/jms/" + lookup);
        eMsg = "Failed to lookup TopicConnectionFactory using name java:comp/env/jms/"
            + lookup;
      } else {
        tcf = (TopicConnectionFactory) jmsObjects
            .getTopicConnectionFactory(lookup);
        eMsg = "Failed to lookup TopicConnectionFactory using name " + lookup;
      }

      if (mode.equals("javaEE")) {
        TestUtil.logTrace("Getting Topic " + TESTTOPICNAME);
        testTopic = (Topic) jndiContext.lookup(TESTTOPICNAME);
        eMsg = "Failed to lookup Topic " + TESTTOPICNAME;
      } else {
        TestUtil.logTrace("Getting Topic " + TCKTESTTOPICNAME);
        testTopic = (Topic) jmsObjects.getTopic(TCKTESTTOPICNAME);
        eMsg = "Failed to lookup Topic " + TCKTESTTOPICNAME;
      }

      // create default connection
      TestUtil.logTrace("Creating TopicConnection");
      eMsg = "Failed to create topic connection using username, " + username
          + " password, " + password;
      tConnection = (TopicConnection) createNewConnection(ttype, username,
          password);

      // create default TopicSession
      TestUtil.logTrace("Creating TopicSession");
      eMsg = "Failed to create topic session";
      tSession = tConnection.createTopicSession(transacted,
          Session.AUTO_ACKNOWLEDGE);

      // create default consumer/producer
      TestUtil.logTrace("Creating subscriber");
      eMsg = "Failed to create subscriber for topic " + testTopic;
      tSubscriber = tSession.createSubscriber(testTopic);

      TestUtil.logTrace("Creating publisher");
      eMsg = "Failed to create publisher for topic " + testTopic;
      tPublisher = tSession.createPublisher(testTopic);

    } catch (Exception e) {
      TestUtil.logErr(eMsg + "due to ", e);
      throw e;
    }
  }

  /************************************************************************
   * Queue setup using common classes/interfaces
   ************************************************************************/
  private void createCommonQSetup() throws Exception {
    String eMsg = "";

    try {

      if (mode.equals("javaEE")) {
        TestUtil
            .logTrace("Getting ConnectionFactory " + QUEUECONNECTIONFACTORY);
        cf = (ConnectionFactory) jndiContext.lookup(QUEUECONNECTIONFACTORY);
        eMsg = "Failed to lookup ConnectionFactory using name "
            + QUEUECONNECTIONFACTORY;
      } else {
        TestUtil
            .logTrace("Getting ConnectionFactory " + TCKQUEUECONNECTIONFACTORY);
        cf = (ConnectionFactory) jmsObjects
            .getQueueConnectionFactory(TCKQUEUECONNECTIONFACTORY);
        eMsg = "Failed to lookup ConnectionFactory using name "
            + TCKQUEUECONNECTIONFACTORY;
      }
      qcf = (QueueConnectionFactory) cf;

      if (mode.equals("javaEE")) {
        TestUtil.logTrace("Getting Queue " + TESTQUEUENAME);
        testDestination = (Destination) jndiContext.lookup(TESTQUEUENAME);
        eMsg = "Failed to lookup Queue " + TESTQUEUENAME;
      } else {
        TestUtil.logTrace("Getting Queue " + TCKTESTQUEUENAME);
        testDestination = (Destination) jmsObjects.getQueue(TCKTESTQUEUENAME);
        eMsg = "Failed to lookup Queue " + TCKTESTQUEUENAME;
      }

      // create default connection
      TestUtil.logTrace("Creating Connection");
      eMsg = "Failed to create connection using username, " + username
          + " password, " + password;
      conn = cf.createConnection(username, password);

      // create default Session
      TestUtil.logTrace("Creating Session");
      eMsg = "Failed to create session";
      sess = conn.createSession(transacted, Session.AUTO_ACKNOWLEDGE);

      // create default consumer/producer
      TestUtil.logTrace("Creating messageProducer");
      eMsg = "Failed to create producer for destination " + testDestination;
      sender = sess.createProducer(testDestination);

      TestUtil.logTrace("Creating MessageConsumer");
      eMsg = "Failed to create consumer for destination " + testDestination;
      receiver = sess.createConsumer(testDestination);

    } catch (Exception e) {
      TestUtil.logErr(eMsg + "due to ", e);
      throw e;
    }
  }

  /************************************************************************
   * Topic setup using common classes/interfaces
   ************************************************************************/
  private void createCommonTSetup() throws Exception {
    String eMsg = "";

    try {

      if (mode.equals("javaEE")) {
        TestUtil
            .logTrace("Getting ConnectionFactory " + TOPICCONNECTIONFACTORY);
        cf = (ConnectionFactory) jndiContext.lookup(TOPICCONNECTIONFACTORY);
        eMsg = "Failed to lookup ConnectionFactory using name "
            + TOPICCONNECTIONFACTORY;
      } else {
        TestUtil
            .logTrace("Getting ConnectionFactory " + TCKTOPICCONNECTIONFACTORY);
        cf = (ConnectionFactory) jmsObjects
            .getTopicConnectionFactory(TCKTOPICCONNECTIONFACTORY);
        eMsg = "Failed to lookup ConnectionFactory using name "
            + TCKTOPICCONNECTIONFACTORY;
      }

      TestUtil.logTrace("Getting Topic " + TESTTOPICNAME);
      if (mode.equals("javaEE")) {
        testDestination = (Destination) jndiContext.lookup(TESTTOPICNAME);
        eMsg = "Failed to lookup Topic " + TESTTOPICNAME;
      } else {
        testDestination = (Destination) jmsObjects.getTopic(TCKTESTTOPICNAME);
        eMsg = "Failed to lookup Topic " + TCKTESTTOPICNAME;
      }

      // create default connection
      TestUtil.logTrace("Creating Connection");
      eMsg = "Failed to create connection using username, " + username
          + " password, " + password;
      conn = cf.createConnection(username, password);

      // create default Session
      TestUtil.logTrace("Creating Session");
      eMsg = "Failed to create session";
      sess = conn.createSession(transacted, Session.AUTO_ACKNOWLEDGE);

      // create default consumer/producer
      TestUtil.logTrace("Creating messageProducer");
      eMsg = "Failed to create producer for destination " + testDestination;
      sender = sess.createProducer(testDestination);

      TestUtil.logTrace("Creating MessageConsumer");
      eMsg = "Failed to create consumer for destination " + testDestination;
      receiver = sess.createConsumer(testDestination);

    } catch (Exception e) {
      TestUtil.logErr(eMsg + "due to ", e);
      throw e;
    }
  }

  /************************************************************************
   * Topic setup using common classes/interfaces
   ************************************************************************/
  private void createCommonTSetup(String lookup) throws Exception {
    String eMsg = "";

    try {

      TestUtil.logTrace("Getting ConnectionFactory " + lookup);
      if (mode.equals("javaEE")) {
        cf = (ConnectionFactory) jndiContext
            .lookup("java:comp/env/jms/" + lookup);
        eMsg = "Failed to lookup ConnectionFactory using name java:comp/env/jms/"
            + lookup;
      } else {
        cf = (ConnectionFactory) jmsObjects.getTopicConnectionFactory(lookup);
        eMsg = "Failed to lookup ConnectionFactory using name " + lookup;
      }

      TestUtil.logTrace("Getting Topic " + TESTTOPICNAME);
      if (mode.equals("javaEE")) {
        testDestination = (Destination) jndiContext.lookup(TESTTOPICNAME);
        eMsg = "Failed to lookup Topic " + TESTTOPICNAME;
      } else {
        testDestination = (Destination) jmsObjects.getTopic(TCKTESTTOPICNAME);
        eMsg = "Failed to lookup Topic " + TCKTESTTOPICNAME;
      }

      // create default connection
      TestUtil.logTrace("Creating Connection");
      eMsg = "Failed to create connection using username, " + username
          + " password, " + password;
      conn = cf.createConnection(username, password);

      // create default Session
      TestUtil.logTrace("Creating Session");
      eMsg = "Failed to create session";
      sess = conn.createSession(transacted, Session.AUTO_ACKNOWLEDGE);

      // create default consumer/producer
      TestUtil.logTrace("Creating messageProducer");
      eMsg = "Failed to create producer for destination " + testDestination;
      sender = sess.createProducer(testDestination);

      TestUtil.logTrace("Creating MessageConsumer");
      eMsg = "Failed to create consumer for destination " + testDestination;
      receiver = sess.createConsumer(testDestination);

    } catch (Exception e) {
      TestUtil.logErr(eMsg + "due to ", e);
      throw e;
    }
  }

  /***********************************************************************
   * Default getter's for COMMON QUEUE or COMMON TOPIC created objects
   ***********************************************************************/
  public ConnectionFactory getConnectionFactory() throws Exception {
    return cf;
  }

  public Connection getDefaultConnection() throws Exception {
    return conn;
  }

  public Session getDefaultSession() throws Exception {
    return sess;
  }

  public MessageProducer getDefaultProducer() throws Exception {
    return sender;
  }

  public MessageConsumer getDefaultConsumer() throws Exception {
    return receiver;
  }

  public Destination getDefaultDestination() throws Exception {
    return testDestination;
  }

  /**********************************************************************************
   * Creates a new Topic for tests that require more than the Topic. The topic
   * should be setup by the administrator
   * 
   * @param String
   *          the topic name
   **********************************************************************************/
  public Topic createNewTopic(String topicName) throws Exception {
    Topic testT = null;
    if (mode.equals("javaEE"))
      testT = (Topic) jndiContext.lookup("java:comp/env/jms/" + topicName);
    else
      testT = (Topic) jmsObjects.getTopic(topicName);

    return testT;
  }

  /**********************************************************************************
   * Creates a new Queue for tests that require more than the Queue. The queue
   * should already be setup by the administrator
   * 
   * @param String
   *          the queue name
   **********************************************************************************/
  public Queue createNewQueue(String queueName) throws Exception {
    Queue testQ = null;
    if (mode.equals("javaEE"))
      testQ = (Queue) jndiContext.lookup("java:comp/env/jms/" + queueName);
    else
      testQ = (Queue) jmsObjects.getQueue(queueName);
    return testQ;
  }

  /**********************************************************************************
   * Close all resources created by JmsTool except connection resource which
   * gets closed in the closeAllConnections() or closeDefaultConnections()
   * methods.
   *
   * @exception Exception
   * 
   **********************************************************************************/
  public void closeAllResources() throws Exception {
    // Close QUEUE resource objects
    try {
      if (qSession != null)
        qSession.close();
    } catch (JMSException e) {
    }

    try {
      if (qSender != null)
        qSender.close();
    } catch (JMSException e) {
    }

    try {
      if (qReceiver != null)
        qReceiver.close();
    } catch (JMSException e) {
    }

    qSession = null;
    qReceiver = null;
    qSender = null;

    // Close TOPIC resource objects
    try {
      if (tSession != null)
        tSession.close();
    } catch (JMSException e) {
    }

    try {
      if (tPublisher != null)
        tPublisher.close();
    } catch (JMSException e) {
    }

    try {
      if (tSubscriber != null)
        tSubscriber.close();
    } catch (JMSException e) {
    }

    tSession = null;
    tSubscriber = null;
    tPublisher = null;

    // Close COMMON resource objects
    try {
      if (sess != null)
        sess.close();
    } catch (JMSException e) {
    }
    try {
      if (sender != null)
        sender.close();
    } catch (JMSException e) {
    }
    try {
      if (receiver != null)
        receiver.close();
    } catch (JMSException e) {
    }

    sess = null;
    receiver = null;
    sender = null;
  }

  /**********************************************************************************
   * Close any connections opened by the tests
   *
   * @exception Exception
   * 
   * @see It is allowable to do a second call to close connection per the JMS
   *      Specification
   **********************************************************************************/
  public void closeAllConnections(ArrayList connections) throws Exception {
    try {
      closeDefaultConnections();
      if (connections != null) {
        if (!connections.isEmpty()) {
          for (int i = 0; i < connections.size(); i++) {
            ((Connection) connections.get(i)).close();
            TestUtil.logTrace("Closing non default connection");
          }
        }
      }
    } catch (JMSException e) {
      TestUtil.logErr("Problem closing connections", e);
    }
  }

  /**********************************************************************************
   * Close default connections
   *
   * @see It is allowable to do a second call to close connection per the JMS
   *      Specification
   **********************************************************************************/
  public void closeDefaultConnections() throws Exception {
    try {
      if (conn != null) {
        TestUtil.logTrace("JmstTool: Closing default Connection");
        conn.close();
      }

      if (qConnection != null) {
        TestUtil.logTrace("JmstTool: Closing default QueueConnection");
        qConnection.close();
      }

      if (tConnection != null) {
        TestUtil.logTrace("JmsTool: Closing default TopicConnection");
        tConnection.close();
      }
    } catch (JMSException e) {

      /*
       * Connection may already be closed by test method. If it is another type
       * of excption, pass it up to the calling method. Should only catch
       * JMSException if there is a regression in the RI.
       */
      TestUtil.logErr("Problem closing connections", e);
    }
  }

  /***********************************************************************
   * Default getter's for QUEUE created objects
   ***********************************************************************/
  public QueueConnectionFactory getQueueConnectionFactory() {
    return qcf;
  }

  public QueueConnection getDefaultQueueConnection() {
    return qConnection;
  }

  public QueueSession getDefaultQueueSession() {
    return qSession;
  }

  public QueueReceiver getDefaultQueueReceiver() {
    return qReceiver;
  }

  public QueueSender getDefaultQueueSender() {
    return qSender;
  }

  public Queue getDefaultQueue() {
    return testQueue;
  }

  public Destination getQueueDestination(String lookup) throws Exception {
    Destination dest = null;
    if (mode.equals("javaEE"))
      dest = (Destination) jndiContext.lookup("java:comp/env/jms/" + lookup);
    else
      dest = (Destination) jmsObjects.getQueue(lookup);
    return dest;
  }

  /***********************************************************************
   * Default getter's for TOPIC created objects
   ***********************************************************************/
  public TopicConnectionFactory getTopicConnectionFactory() {
    return tcf;
  }

  public TopicConnection getDefaultTopicConnection() {
    return tConnection;
  }

  public TopicSession getDefaultTopicSession() {
    return tSession;
  }

  public TopicSubscriber getDefaultTopicSubscriber() {
    return tSubscriber;
  }

  public TopicPublisher getDefaultTopicPublisher() {
    return tPublisher;
  }

  public Topic getDefaultTopic() {
    return testTopic;
  }

  public Destination getTopicDestination(String lookup) throws Exception {
    Destination dest = null;
    if (mode.equals("javaEE"))
      dest = (Destination) jndiContext.lookup("java:comp/env/jms/" + lookup);
    else
      dest = (Destination) jmsObjects.getTopic(lookup);
    return dest;
  }

  /**********************************************************************************
   * Use this method at cleanup time to remove any connections and messages that
   * have remained on the queue.
   *
   * @param ArrayList
   *          connections list of open connections
   * @param ArrayList
   *          queues list of queues to flush
   **********************************************************************************/
  public void doClientQueueTestCleanup(ArrayList connections,
      ArrayList queues) {
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
      TestUtil.logErr("Cleanup error: " + e.toString());
      TestUtil.printStackTrace(e);
    }
  }

  /**********************************************************************************
   * Use this method at cleanup time to remove any messages that have remained
   * on the queue.
   **********************************************************************************/
  public void flushDestination() throws Exception {
    Connection cC = null;
    MessageConsumer receiver = null;
    MessageProducer sender = null;
    Session sess = null;
    ObjectMessage msg = null;
    int priority = 0; // lowest priority
    int numMsgsFlushed = 0;

    try {
      if (conn != null) {
        TestUtil.logTrace("Closing default connection in flushDestination()");
        try {
          conn.close();
        } catch (Exception ex) {
          TestUtil.logErr("Error closing default connection", ex);
        }
      }

      TestUtil.logTrace(
          "Create new Connection,Session,MessageProducer,MessageConsumer to flush Destination");
      cC = createNewConnection(ttype, username, password);
      sess = cC.createSession(false, Session.AUTO_ACKNOWLEDGE);
      cC.start(); // start the connections so that messages may be received.
      sender = sess.createProducer(testDestination);
      receiver = sess.createConsumer(testDestination);

      // create and send a low priority message
      // any other messages on the queue should be received first
      // and low priority message should signal the end
      msg = sess.createObjectMessage();
      msg.setObject("Flush Destination");
      msg.setStringProperty("COM_SUN_JMS_TESTNAME", "flushDestination");
      TestUtil.logTrace(
          "Send low priority message to Destination to signal the last message");
      sender.send(msg, javax.jms.Message.DEFAULT_DELIVERY_MODE, priority,
          javax.jms.Message.DEFAULT_TIME_TO_LIVE);

      // flush the Destination
      TestUtil.logTrace("Now flush the Destination");
      Message rmsg = receiver.receive(5000);
      while (rmsg != null) {
        String tname = rmsg.getStringProperty("COM_SUN_JMS_TESTNAME");
        if (tname != null && tname.equals("flushDestination")) {
          // Should be last message (try receiveNoWait() one more time to make
          // sure it is)
          rmsg = receiver.receiveNoWait();
          if (rmsg != null)
            numMsgsFlushed++;
        } else {
          numMsgsFlushed++;
        }
        rmsg = receiver.receive(1000);
      }

      if (numMsgsFlushed > 0) {
        TestUtil.logTrace("Flushed " + numMsgsFlushed + " messages");
      } else {
        TestUtil.logTrace("No messages to flush");
      }

    } catch (Exception e) {
      TestUtil.logErr(
          "Cleanup error attempting to flush Destination: " + e.toString());
    } finally {
      try {
        cC.close();
      } catch (Exception e) {
        TestUtil.logErr(
            "Error closing Connection in flushDestination()" + e.toString());
      }
    }
  }

  /**********************************************************************************
   * Use this method at cleanup time to remove any messages that have remained
   * on the queue.
   *
   * @param Queue
   *          qToFlush[] QUEUE
   **********************************************************************************/
  public void flushQueue(ArrayList qToFlush) throws Exception {
    QueueConnection qc = null;
    QueueReceiver qr = null;
    QueueSession qs = null;
    QueueSender qsndr = null;
    ObjectMessage msg = null;
    Enumeration msgs = null;
    int priority = 0; // lowest priority
    int numMsgsFlushed = 0;
    int numMsgs = 0;

    try {

      if (getDefaultQueue() != null) {
        qToFlush.add(getDefaultQueue());
      }
      TestUtil
          .logTrace("Create new QueueConnection,QueueSession to flush Queue");
      qc = (QueueConnection) createNewConnection(QUEUE, username, password);
      qs = qc.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
      qc.start(); // start the connections so that messages may be received.

      for (int i = 0; i < qToFlush.size(); i++) {
        TestUtil.logTrace(
            "Create QueueBrowser to count number of messages left on Queue");
        QueueBrowser qBrowser = qs.createBrowser((Queue) qToFlush.get(i));
        // count the number of messages
        msgs = qBrowser.getEnumeration();
        while (msgs.hasMoreElements()) {
          msgs.nextElement();
          numMsgs++;
        }

        if (numMsgs == 0) {
          TestUtil.logTrace("No Messages left on Queue "
              + ((Queue) qToFlush.get(i)).getQueueName());
        } else {
          TestUtil.logTrace(numMsgs + " Messages left on Queue "
              + ((Queue) qToFlush.get(i)).getQueueName());

          TestUtil
              .logTrace("Create new QueueReceiver to flush messages in Queue");
          qr = qs.createReceiver((Queue) qToFlush.get(i));

          // flush the queue
          TestUtil.logTrace("Now flush the Queue");
          Message rmsg = qr.receive(5000);
          while (rmsg != null) {
            numMsgsFlushed++;
            rmsg = qr.receiveNoWait();
            if (rmsg == null) {
              // Should be last message (try receive(1000) one more time to make
              // sure it is)
              rmsg = qr.receive(1000);
            }
          }

          if (numMsgsFlushed > 0) {
            TestUtil.logTrace("Flushed " + numMsgsFlushed + " messages");
          }
        }
      }
    } catch (Exception e) {
      TestUtil
          .logErr("Cleanup error attempting to flush Queue: " + e.toString());
    } finally {
      try {
        qc.close();
      } catch (Exception e) {
        TestUtil.logErr("Error closing QueueConnection in flushQueue(Array)"
            + e.toString());
      }
    }
  }

  public void flushQueue() throws Exception {
    int numMsgsFlushed = 0;
    int numMsgs = 0;
    Enumeration msgs = null;

    try {
      TestUtil.logTrace(
          "Create QueueBrowser to count number of messages left on Queue");
      QueueBrowser qBrowser = getDefaultQueueSession()
          .createBrowser(getDefaultQueue());
      // count the number of messages
      msgs = qBrowser.getEnumeration();
      while (msgs.hasMoreElements()) {
        msgs.nextElement();
        numMsgs++;
      }

      if (numMsgs == 0) {
        TestUtil.logTrace(
            "No Messages left on Queue " + getDefaultQueue().getQueueName());
      } else {
        TestUtil.logTrace(numMsgs + " Messages left on Queue "
            + getDefaultQueue().getQueueName());
        if (getDefaultQueueReceiver() != null) {
          // flush the queue
          Message msg = getDefaultQueueReceiver().receive(5000);
          while (msg != null) {
            numMsgsFlushed++;
            msg = getDefaultQueueReceiver().receiveNoWait();
            if (msg == null) {
              // Should be last message (try receive(1000) one more time to make
              // sure it is)
              msg = getDefaultQueueReceiver().receive(1000);
            }
          }
          if (numMsgsFlushed > 0) {
            TestUtil.logTrace("Flushed " + numMsgsFlushed + " messages");
          }

          // if default QueueSession is transacted,
          // be sure to commit consumed messages.
          if (numMsgsFlushed > 0 && getDefaultQueueSession().getTransacted()) {
            getDefaultQueueSession().commit();
          }
        }
      }
    } catch (Exception e) {
    }
  }

  /**********************************************************************************
   * Returns a the default Connection. The returned Connection object must be
   * explicitly cast into a QueueConnection or TopicConnection.
   *
   * @param int
   *          type (QUEUE type or TOPIC type)
   * @return Connection from the default Queue or Topic or Common
   *         ConnectionFactory
   **********************************************************************************/
  private Connection createNewConnection(int type, String username,
      String password) throws Exception {
    QueueConnection qC = null;
    TopicConnection tC = null;

    if ((type == QUEUE) || (type == TX_QUEUE)) {
      if (username.equals(JMSDEFAULT) || password.equals(JMSDEFAULT)) {
        qC = qcf.createQueueConnection();
        return qC;
      } else {
        qC = qcf.createQueueConnection(username, password);
        return qC;
      }
    } else if ((type == TOPIC) || (type == TX_TOPIC)) {
      if (username.equals(JMSDEFAULT) || password.equals(JMSDEFAULT)) {
        tC = tcf.createTopicConnection();
        return tC;
      } else {
        tC = tcf.createTopicConnection(username, password);
        return tC;
      }
    } else if ((type == DURABLE_TOPIC) || (type == DURABLE_TX_TOPIC)) {
      if (username.equals(JMSDEFAULT) || password.equals(JMSDEFAULT)) {
        tC = tcf.createTopicConnection();
        return tC;
      } else {
        tC = tcf.createTopicConnection(username, password);
        return tC;
      }
    } else if ((type == COMMON_Q) || (type == COMMON_T) || (type == COMMON_QTX)
        || (type == COMMON_TTX)) {
      if (username.equals(JMSDEFAULT) || password.equals(JMSDEFAULT)) {
        conn = cf.createConnection();
        return conn;
      } else {
        conn = cf.createConnection(username, password);
        return conn;
      }

    } else {
      throw new Exception("Failed to create new Connection");
    }
  }

  /**********************************************************************************
   * Returns a new Queue Connection for tests that require more than the default
   * connection. The returned Connection object must be explicitly cast into a
   * QueueConnection.
   *
   * @param int
   *          type (QUEUE type)
   * @return Connection from the default ConnectionFactory
   **********************************************************************************/
  public Connection getNewConnection(int type, String username, String password)
      throws Exception {
    QueueConnection qC = null;
    Connection cC = null;

    if ((type == QUEUE) || (type == TX_QUEUE)) {
      if (username.equals(JMSDEFAULT) || password.equals(JMSDEFAULT)) {
        qC = qcf.createQueueConnection();
        return qC;
      } else {
        qC = qcf.createQueueConnection(username, password);
        return qC;
      }
    } else if (type == COMMON_Q) {
      if (username.equals(JMSDEFAULT) || password.equals(JMSDEFAULT)) {
        cC = cf.createConnection();
        return cC;
      } else {
        cC = cf.createConnection(username, password);
        return cC;
      }
    } else {
      throw new Exception("Failed to get new Connection");
    }
  }

  /**********************************************************************************
   * Returns a new Topic Connection for tests that require more than the default
   * connection. The returned Connection object must be explicitly cast into a
   * TopicConnection.
   *
   * @param int
   *          type (TOPIC type)
   * @return Connection from the default ConnectionFactory
   **********************************************************************************/
  public Connection getNewConnection(int type, String username, String password,
      String lookup) throws Exception {
    TopicConnection tC = null;
    Connection cC = null;

    if ((type == TOPIC) || (type == TX_TOPIC)) {
      if (mode.equals("javaEE"))
        tcf2 = (TopicConnectionFactory) jndiContext
            .lookup("java:comp/env/jms/" + lookup);
      else
        tcf2 = (TopicConnectionFactory) jmsObjects
            .getTopicConnectionFactory(lookup);
      if (username.equals(JMSDEFAULT) || password.equals(JMSDEFAULT)) {
        tC = tcf2.createTopicConnection();
        return tC;
      } else {
        tC = tcf2.createTopicConnection(username, password);
        return tC;
      }
    } else if ((type == DURABLE_TOPIC) || (type == DURABLE_TX_TOPIC)) {
      if (username.equals(JMSDEFAULT) || password.equals(JMSDEFAULT)) {
        tC = tcf.createTopicConnection();
        return tC;
      } else {
        tC = tcf.createTopicConnection(username, password);
        return tC;
      }
    } else if ((type == COMMON_T) || (type == COMMON_TTX)) {
      if (mode.equals("javaEE"))
        cf2 = (TopicConnectionFactory) jndiContext
            .lookup("java:comp/env/jms/" + lookup);
      else
        cf2 = (TopicConnectionFactory) jmsObjects
            .getTopicConnectionFactory(lookup);
      if (username.equals(JMSDEFAULT) || password.equals(JMSDEFAULT)) {
        cC = cf2.createConnection();
        return cC;
      } else {
        cC = cf2.createConnection(username, password);
        return cC;
      }
    } else {
      throw new Exception("Failed to get new Connection");
    }
  }

  /**********************************************************************************
   * Returns a new Connection for tests that require more than the default
   * connection. The returned Connection object must be explicitly cast into a
   * QueueConnection or TopicConnection.
   * 
   * @param int
   *          type (QUEUE type or TOPIC type)
   * @return Connection from the default Queue or Topic ConnectionFactory
   **********************************************************************************/
  public Connection getNewConnection(int type) throws Exception {
    return getNewConnection(type, JMSDEFAULT, JMSDEFAULT);
  }

  /***************************************************************
   * Return connection type (QUEUE or TOPIC)
   **************************************************************/
  public int getType() {
    return ttype;
  }

  /**********************************************************************************
   * flushDestinationJMSContext Flush destination Queue using JMSContext
   *
   * Use this method at cleanup time to remove any messages that have remained
   * on the queue.
   **********************************************************************************/
  public void flushDestinationJMSContext() throws Exception {
    JMSConsumer consumer = null;
    JMSContext context = null;
    int numMsgsFlushed = 0;

    try {
      if (getDefaultConnection() != null) {
        TestUtil.logTrace(
            "Closing default connection in flushDestinationJMSContext()");
        try {
          getDefaultConnection().close();
        } catch (Exception ex) {
          TestUtil.logErr("Error closing default connection", ex);
        }
      }

      TestUtil.logTrace(
          "Create new JMSContext and JMSConsumer to flush Destination");
      context = createNewJMSContext(ttype, username, password);
      consumer = context.createConsumer(testDestination);

      TestUtil.logTrace("Now flush the Destination");
      Message rmsg = consumer.receive(5000);
      while (rmsg != null) {
        numMsgsFlushed++;
        rmsg = consumer.receiveNoWait();
        if (rmsg == null) {
          // Should be last message (try receive(1000) one more time to make
          // sure it is)
          rmsg = consumer.receive(1000);
        }
      }

      if (numMsgsFlushed > 0) {
        TestUtil.logTrace("Flushed " + numMsgsFlushed + " messages");
      } else {
        TestUtil.logTrace("No messages to flush");
      }
    } catch (Exception e) {
      TestUtil.logErr(
          "Cleanup error attempting to flush Destination: " + e.toString());
    } finally {
      try {
        consumer.close();
        context.close();
      } catch (Exception e) {
      }
    }
  }

  /**********************************************************************************
   * createNewJMSContext Return a new JMSContext.
   *
   * @param int
   *          type (QUEUE type or TOPIC type)
   * @param String
   *          (username)
   * @param String
   *          (password)
   * @return JMSContext
   **********************************************************************************/
  private JMSContext createNewJMSContext(int type, String username,
      String password) throws Exception {
    JMSContext context = null;
    if ((type == QUEUE) || (type == TX_QUEUE)) {
      if (username.equals(JMSDEFAULT) || password.equals(JMSDEFAULT)) {
        context = qcf.createContext();
      } else {
        context = qcf.createContext(username, password);
      }
    } else if ((type == TOPIC) || (type == TX_TOPIC) || (type == DURABLE_TOPIC)
        || (type == DURABLE_TX_TOPIC)) {
      if (username.equals(JMSDEFAULT) || password.equals(JMSDEFAULT)) {
        context = tcf.createContext();
      } else {
        context = tcf.createContext(username, password);
      }
    } else if ((type == COMMON_Q) || (type == COMMON_T) || (type == COMMON_QTX)
        || (type == COMMON_TTX)) {
      if (username.equals(JMSDEFAULT) || password.equals(JMSDEFAULT)) {
        context = cf.createContext();
      } else {
        context = cf.createContext(username, password);
      }
    } else {
      throw new Exception("Failed to create new JMSContext");
    }
    return context;
  }
}
