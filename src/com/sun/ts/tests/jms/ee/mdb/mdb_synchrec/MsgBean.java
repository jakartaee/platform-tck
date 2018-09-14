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

package com.sun.ts.tests.jms.ee.mdb.mdb_synchrec;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.tests.jms.commonee.*;
import java.io.Serializable;
import java.util.Properties;
import javax.ejb.EJBException;
import javax.ejb.EJBHome;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.naming.*;
import javax.jms.*;
import java.security.*;
import java.sql.*;
import javax.sql.*;
import javax.transaction.UserTransaction;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

public class MsgBean implements MessageDrivenBean, MessageListener {

  // properties object needed for logging,
  // get this from the message object passed into
  // the onMessage method.
  private java.util.Properties p = null;

  // Contexts
  private TSNamingContext context = null;

  protected MessageDrivenContext mdc = null;

  // JMS PTP
  private QueueConnectionFactory qFactory;

  private QueueConnection qConnection = null;

  private Queue queueR = null;

  private Queue queueS = null;

  private QueueSender mSender = null;

  private boolean result = false;

  public MsgBean() {
    TestUtil.logTrace("@MsgBean()!");
  };

  public void ejbCreate() {
    TestUtil.logTrace(
        "In Message Bean ======================================EJBCreate");
    try {

      context = new TSNamingContext();
      qFactory = (QueueConnectionFactory) context
          .lookup("java:comp/env/jms/MyQueueConnectionFactory");
      queueR = (Queue) context.lookup("java:comp/env/jms/MY_QUEUE");
      queueS = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE_REPLY");
      p = new Properties();

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("MDB ejbCreate Error", e);
    }
  }

  public void onMessage(Message msg) {
    long timeout = 10000;
    QueueSession qSession = null;
    TextMessage messageSent = null;
    TextMessage msgRec = null;
    String mdbMessage = "my mdb message";
    String testName = null;
    QueueSender qSender = null;
    QueueReceiver rcvr = null;
    boolean result = false;

    JmsUtil.initHarnessProps(msg, p);
    TestUtil.logTrace(
        "In Message Bean ======================================onMessage");
    try {
      testName = "mdbResponse";
      qConnection = qFactory.createQueueConnection();
      if (qConnection == null)
        throw new EJBException("MDB connection Error!");

      qConnection.start();

      qSession = qConnection.createQueueSession(true, 0);
      TestUtil.logTrace("will run TestCase: " + testName);

      rcvr = qSession.createReceiver(queueR);

      TestUtil.logTrace("Verify the synchronous receive");
      TestUtil.logTrace(
          "HHHHHHHHHHHHH+++++++++  Trying to receive message from the Queue: ");
      msgRec = (TextMessage) rcvr.receive(timeout);

      if (msgRec != null) {
        //
        TestUtil.logTrace("mdb received a msg from MY_QUEUE");
        if (msgRec.getStringProperty("TestCase").equals(mdbMessage)) {
          TestUtil.logTrace("Success: Correct msg recvd from MY_QUEUE");
          result = true;
        }
      }
      // send results to QUEUE_REPLY

      JmsUtil.sendTestResults(testName, result, qSession, queueS);
      TestUtil.logTrace("Mdb test results send to queue reply");

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    } finally {
      if (qConnection != null) {
        try {
          qConnection.close();
        } catch (Exception e) {
          TestUtil.printStackTrace(e);
        }
      }
    }
  }

  public void setMessageDrivenContext(MessageDrivenContext mdc) {
    TestUtil.logTrace("@MsgBean:setMessageDrivenContext()!");
    this.mdc = mdc;
  }

  public void ejbRemove() {
    TestUtil.logTrace("@ejbRemove()");
  }
}
