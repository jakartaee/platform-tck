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

import java.io.*;
import java.util.Properties;
import javax.ejb.EJBHome;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.tests.jms.commonee.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import javax.jms.*;
import com.sun.javatest.Status;

public class MDBClient extends EETest {

  // Naming specific member variables
  private TSNamingContextInterface context = null;

  private Properties props = null;

  private Queue mdbRcvrQueue;

  private Queue rcvrQueue;

  private QueueConnection qConnect;

  private Queue cmtQ;

  private QueueSession session;

  private QueueConnectionFactory qFactory;

  private QueueSender qSender;

  private String jmsUser = null;

  private String jmsPassword = null;

  private TextMessage msg = null;

  // get this from ts.jte
  long timeout;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    MDBClient theTests = new MDBClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * Test setup:
   * 
   * @class.setup_props: jms_timeout, in milliseconds - how long to wait on
   * synchronous receive; user;password;harness.log.port; harness.log.traceflag;
   *
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    jmsUser = p.getProperty("user");
    jmsPassword = p.getProperty("password");
    try {
      timeout = Integer.parseInt(p.getProperty("jms_timeout"));

      TestUtil.logTrace("in client setup");

      context = new TSNamingContext();
      TestUtil.logTrace("Client: Do lookups!");
      qFactory = (QueueConnectionFactory) context
          .lookup("java:comp/env/jms/MyQueueConnectionFactory");
      cmtQ = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE");
      rcvrQueue = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE_REPLY");
      mdbRcvrQueue = (Queue) context.lookup("java:comp/env/jms/MY_QUEUE");
      qConnect = qFactory.createQueueConnection(jmsUser, jmsPassword);
      session = qConnect.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
      qConnect.start();
      cleanTheQueue(rcvrQueue);
      cleanTheQueue(mdbRcvrQueue);

    } catch (Exception e) {
      throw new Fault("Setup Failed!", e);
    }
  }
  /* Run tests */

  /*
   * @testName: test1
   *
   * @assertion_ids: JavaEE:SPEC:214; JMS:JAVADOC:270; JMS:JAVADOC:522;
   * JMS:JAVADOC:188; JMS:JAVADOC:221; JMS:JAVADOC:120; JMS:JAVADOC:425;
   * JMS:JAVADOC:198; JMS:JAVADOC:184; JMS:JAVADOC:334; JMS:JAVADOC:405;
   * 
   * @test_Strategy: Verify synchronous receive in an mdb. send a msg to
   * MDB_QURUR_REPLY - mdb will do a synchronous rec on it Invoke a cmt mdb by
   * writing to MDB_QUEUE In onMessage mdb method, do a synchronous receive
   * Notify the client by sending a message to QUEUE_REPLY if mdb was able to
   * successfully receive the message
   *
   */
  public void test1() throws Fault {
    String TestCase = "syncRecTest1";
    int TestNum = 1;
    String mdbMessage = "my mdb message";

    try {
      // create a text message
      createTestMessage(TestCase, TestNum);
      // send a message to receiver queue that the mdb can synchronously receive
      qSender = session.createSender(mdbRcvrQueue);
      // send the message to invoke mdb
      JmsUtil.addPropsToMessage(msg, props);
      msg.setStringProperty("TestCase", mdbMessage);
      qSender.send(msg);

      msg.setStringProperty("COM_SUN_JMS_TESTNAME", TestCase);
      qSender = session.createSender(cmtQ);
      // send the message to invoke mdb
      qSender.send(msg);

      // verify that message was requeued and pass
      TestCase = "mdbResponse";
      if (!checkOnResponse(TestCase)) {
        throw new Exception("syncRecTest1 - ");
      }
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /* cleanup -- none in this case */
  public void cleanup() throws Fault {
    try {
      msg = null;
      if (qConnect != null) {
        qConnect.close();
      }
      logMsg("End  of client cleanup;");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  private void createTestMessage(String TestCase, int num) {
    String myMessage = "MDB synchronous receive test";
    try {
      msg = session.createTextMessage();
      msg.setStringProperty("user", jmsUser);
      msg.setStringProperty("password", jmsPassword);
      msg.setText(myMessage);
      msg.setIntProperty("TestCaseNum", num);

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg("Error setting user and password in jms msg");
    }
  }

  public boolean checkOnResponse(String prop) {
    boolean status = false;
    try {
      TestUtil.logTrace("@checkOnResponse");
      for (int i = 0; i < 10; i++) {
        status = getMessage(session, prop);
        if (status)
          break;
      }
      TestUtil.logTrace("Close the session");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
    return status;
  }

  private boolean getMessage(QueueSession session, String prop)
      throws Exception {
    try {
      TestUtil.logTrace("top of getMessage");
      boolean gotit = false;
      String selector = "TestCase = 'mdbResponse'";
      QueueReceiver rcvr = session.createReceiver(rcvrQueue, selector);
      // dequeue the response from the mdb
      Message msgRec = null;
      msgRec = rcvr.receive(timeout);
      if (msgRec == null) {
        // not good
        TestUtil.logTrace("No message to receive!!!");
      } else {
        TestUtil.logTrace("Success: getMessage received a msg!!!");
        gotit = recvMessageInternal(msgRec, prop);
      }

      return gotit;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logErr("exception: ", e);
      throw new Exception("getMessage threw an exception!");
    }

  }

  private boolean recvMessageInternal(Message msgRec, String prop)
      throws JMSException {
    boolean retcode = false;
    TestUtil.logTrace("@recvMessageInternal");
    if (msgRec != null) {

      TestUtil.logTrace("Msg: " + msgRec.toString());
      TestUtil.logTrace("TestCase: " + msgRec.getStringProperty("TestCase"));
      TestUtil.logTrace("Status: " + msgRec.getStringProperty("Status"));
      TestUtil.logTrace("=================================================");
      TestUtil.logTrace("Msg: " + msgRec.toString());

      if (msgRec.getStringProperty("TestCase").equals(prop)
          && msgRec.getStringProperty("Status").equals("Pass")) {
        TestUtil.logTrace("TestCase: " + msgRec.getStringProperty("TestCase"));
        TestUtil
            .logTrace("Status from msg: " + msgRec.getStringProperty("Status"));
        TestUtil.logTrace("Pass: we got the expected msg back! ");
        retcode = true;
      } else if (msgRec.getStringProperty("Status").equals("Fail")) {
        TestUtil.logTrace("TestCase: " + msgRec.getStringProperty("TestCase"));
        TestUtil
            .logTrace("Status from msg: " + msgRec.getStringProperty("Status"));
        TestUtil.logTrace("Fail: Error(s) occurred! ");
      } else {
        TestUtil.logTrace("Fail: we didnt get the expected msg back! ");
        TestUtil.logTrace("TestCase:  " + msgRec.getStringProperty("TestCase"));
      }
    } else if (msgRec == null) {
      TestUtil.logTrace("Fail: we didnt get the expected msg back! ");
    }
    return retcode;
  }

  private void cleanTheQueue(Queue q) {

    try {
      // make sure nothing is left in QUEUE_REPLY
      TestUtil.logTrace("Check if any messages left in queue");
      QueueReceiver qR = session.createReceiver(q);
      Message msg = qR.receive(timeout);
      while (msg != null) {
        TestUtil.logTrace("Cleaned up a message in QUEUE!");
        msg = qR.receive(timeout);
      }
      qR.close();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logTrace("Error in cleanTheQueue");
    }

  }

}
