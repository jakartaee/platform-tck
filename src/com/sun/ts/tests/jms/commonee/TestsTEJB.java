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
 * $Id: TestsTEJB.java 59995 2009-10-14 12:05:29Z af70133 $
 */
package com.sun.ts.tests.jms.commonee;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import javax.ejb.*;
import javax.jms.*;
import java.io.*;
import java.util.*;
import javax.annotation.*;

@Stateful
@Remote(TestsT.class)
public class TestsTEJB implements TestsT {

  @Resource
  private SessionContext sessionContext;

  private Properties harnessProps = null;

  private TSNamingContext nctx = null;

  private transient Destination testDestination = null;

  private transient ConnectionFactory cf = null;

  private transient Connection conn = null;

  private transient Connection connr = null;

  private static final String TESTTOPICNAME = "java:comp/env/jms/MY_TOPIC";

  private static final String DURABLETOPICCONNECTIONFACTORY = "java:comp/env/jms/DURABLE_SUB_CONNECTION_FACTORY";

  private String name = "ctssub";

  private String username = null;

  private String password = null;

  private long timeout;

  public TestsTEJB() {
    TestUtil.logTrace("TestsTEJB => default constructor called");
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

  @PostConstruct
  public void postConstruct() {
    TestUtil.logTrace("postConstruct");
    try {
      TestUtil.logMsg("obtain naming context");
      nctx = new TSNamingContext();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("unable to obtain naming context");
    }
  }

  @PostActivate
  public void activate() {
    TestUtil.logTrace("activate");
    try {
      TestUtil.logTrace(
          "Getting ConnectionFactory " + DURABLETOPICCONNECTIONFACTORY);
      cf = (ConnectionFactory) nctx.lookup(DURABLETOPICCONNECTIONFACTORY);

      TestUtil.logTrace("Getting Destination " + TESTTOPICNAME);
      testDestination = (Destination) nctx.lookup(TESTTOPICNAME);

    } catch (Exception ex) {
      TestUtil.logErr("Unexpected Exception in Activate: ", ex);
    }
  }

  @PrePassivate
  public void passivate() {
    TestUtil.logTrace("passivate");
    testDestination = null;
    cf = null;
    conn = null;
    connr = null;
  }

  @Remove
  public void remove() {
    TestUtil.logTrace("remove");
  }

  private void common_T() throws Exception {

    TestUtil
        .logTrace("Getting ConnectionFactory " + DURABLETOPICCONNECTIONFACTORY);
    cf = (ConnectionFactory) nctx.lookup(DURABLETOPICCONNECTIONFACTORY);

    TestUtil.logTrace("Getting Destination " + TESTTOPICNAME);
    testDestination = (Destination) nctx.lookup(TESTTOPICNAME);

  }

  public void common_T_setup() {
    try {
      common_T();

      connr = cf.createConnection(username, password);

      Session sessr = connr.createSession(true, Session.AUTO_ACKNOWLEDGE);
      TopicSubscriber recr = sessr
          .createDurableSubscriber((Topic) testDestination, name);
      recr.close();
    } catch (Exception el) {
      TestUtil.printStackTrace(el);
      TestUtil.logErr("Failed to set up Consumer for Topic", el);
    } finally {
      try {
        connr.close();
      } catch (Exception e) {
        TestUtil.logErr("Exception closing receiving Connection:", e);
      }
    }
  }

  public void sendTextMessage_CT(String TestName, String message) {
    try {
      // get ConnectionFactory and Connection
      common_T();

      // create default connection
      TestUtil.logTrace("Creating Connection with  username, " + username
          + " password, " + password);
      conn = cf.createConnection(username, password);
      // create default Session
      TestUtil.logTrace("Creating Session");
      Session sess = conn.createSession(true, Session.AUTO_ACKNOWLEDGE);

      // create default consumer/producer
      TestUtil.logTrace("Creating messageProducer");
      MessageProducer sender = sess.createProducer(testDestination);

      TestUtil.logMsg("Creating 1 message");
      TextMessage messageSent = sess.createTextMessage();
      messageSent.setText(message);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", TestName);
      TestUtil.logMsg("Sending message");
      sender.send(messageSent);
      sender.close();

    } catch (Exception e) {
      TestUtil.logErr("Failed to send a Message in sendTextMessage_CQ");
      TestUtil.printStackTrace(e);
    } finally {
      if (conn != null) {
        try {
          TestUtil.logTrace("Closing Connection in sendTextMessage_CT");
          conn.close();
        } catch (Exception ce) {
          TestUtil.logErr(
              "Error closing conn in sendTextMessage_CQ" + ce.getMessage(), ce);
        }
      }
    }
  }

  public String receiveTextMessage_CT() {
    TextMessage receivedM = null;
    String tmp = null;

    try {
      // create default connection
      TestUtil.logTrace("Creating Connection with  username, " + username
          + " password, " + password);
      connr = cf.createConnection(username, password);

      connr.start();
      Session sessr = connr.createSession(true, Session.AUTO_ACKNOWLEDGE);
      TopicSubscriber recr = sessr
          .createDurableSubscriber((Topic) testDestination, name);
      receivedM = (TextMessage) recr.receive(timeout);
      recr.close();

      if (receivedM != null)
        tmp = receivedM.getText();
      else
        return null;
    } catch (Exception e) {
      TestUtil.logErr("Failed to receive a message in receiveTextMessage_CT: ",
          e);
      throw new EJBException(e);
    } finally {
      if (connr != null) {
        try {
          TestUtil.logTrace("Closing Connection in receiveTextMessage_CT");
          connr.close();
        } catch (Exception ce) {
          TestUtil.logErr(
              "Error closing conn in receiveTextMessage_CT" + ce.getMessage(),
              ce);
        }
      }
    }
    return tmp;
  }
}
