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
package com.sun.ts.tests.jms.ee.mdb.xa;

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

public class MDBClient extends Client {

  private Queue cmtQ;

  private Queue bmtQ;

  private Topic bmtT;

  private Topic cmtT;

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
   * synchronous receive; user; password;
   */
  public void setup(String[] args, Properties p) throws Fault {

    props = p;
    super.setup(args, p);

    try {
      cmtQ = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE_CMT");
      bmtQ = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE_BMT");
      rcvrQueue = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE_REPLY");

      bmtT = (Topic) context.lookup("java:comp/env/jms/MDB_DURABLE_BMT");
      cmtT = (Topic) context.lookup("java:comp/env/jms/MDB_DURABLE_CMT");
    } catch (Exception e) {
      throw new Fault("Setup Failed!", e);
    }
  }

  /* Run tests */

  /*
   * @testName: Test1
   *
   * @assertion_ids: JMS:SPEC:13; JMS:SPEC:129; JMS:SPEC:246.10;
   * JMS:JAVADOC:371; EJB:SPEC:586.1; EJB:SPEC:586.1.1; EJB:SPEC:583.2;
   * EJB:SPEC:583.2.1;
   *
   * @test_Strategy: Send a test message to a container managed queue. The mdb
   * tests if this is the first time this message was received by checking the
   * JMSRedelivered flag. Rollback the msg. Second time the msg is received is a
   * pass! If the message is not requeued and received again this is a failure.
   */

  public void Test1() throws Fault {
    String TestCase = "xaTest1";
    int TestNum = 1;
    try {
      // construct a message for this test case
      qSender = session.createSender(cmtQ);
      // set up password and username
      createTestMessage(TestCase, TestNum);
      JmsUtil.addPropsToMessage(msg, props);
      System.out.println("Client: sending test message");
      // send the message
      qSender.send(msg);
      // verify that message was requeued and pass
      System.out.println("Client:  response  message");
      if (!checkOnResponse(TestCase)) {
        throw new Exception("oops! error!");
      }
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: Test2
   *
   * @assertion_ids: EJB:SPEC:586.1; EJB:SPEC:586.1.1; EJB:SPEC:583.2;
   * EJB:SPEC:583.2.1; JMS:SPEC:13; JMS:SPEC:129; JMS:SPEC:246.10;
   * JMS:JAVADOC:371;
   *
   * @test_Strategy: Send a test message to a container managed topic. The mdb
   * tests if this is the first time this message was received by checking the
   * JMSRedelivered flag. Send a msg, rollback. Second time the msg is received
   * is a pass! If the message is not requeued and received again this is a
   * failure.
   */
  public void Test2() throws Fault {
    String TestCase = "xaTest2";
    int TestNum = 2;
    try {

      // construct a message for this test case
      tPub = tSession.createPublisher(cmtT);
      // create a text message
      createTestMessage(TestCase, TestNum);
      JmsUtil.addPropsToMessage(msg, props);
      // send the message
      tPub.publish(msg);
      // verify that message was requeued and pass
      System.out.println("Client:  response  message");
      if (!checkOnResponse(TestCase)) {
        throw new Exception("oops! error!");
      }
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: Test3
   *
   * @assertion_ids: EJB:SPEC:509; EJB:SPEC:510;
   *
   * @test_Strategy: Send a test message to a container managed queue. Check
   * rollback status with getRollbackOnly Should be false for not set for
   * rollback! If not test fails
   *
   */
  public void Test3() throws Fault {
    String TestCase = "xaTest3";
    int TestNum = 3;
    try {
      qSender = session.createSender(cmtQ);
      // create a text message
      createTestMessage(TestCase, TestNum);
      JmsUtil.addPropsToMessage(msg, props);
      // send the message
      qSender.send(msg);

      // verify that message was requeued and pass
      if (!checkOnResponse(TestCase)) {
        throw new Exception("Test3 - getRollbackOnly returned wrong value");
      }
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: Test4
   *
   * @assertion_ids: EJB:SPEC:511;
   *
   * @test_Strategy: Send a test message to a bean managed queue. Check the
   * transaction status.
   *
   */
  public void Test4() throws Fault {
    String TestCase = "xaTest4";
    int TestNum = 4;
    try {

      qSender = session.createSender(bmtQ);
      // create a text message
      createTestMessage(TestCase, TestNum);
      JmsUtil.addPropsToMessage(msg, props);
      // send the message
      qSender.send(msg);

      // verify that message was requeued and pass
      if (!checkOnResponse(TestCase)) {
        throw new Exception("Test4 - ");
      }
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: Test5
   *
   * @assertion_ids: EJB:SPEC:543; EJB:SPEC:543.1;
   *
   * @test_Strategy: Invoke an bmt mdb by writing to MDB_QUEUE_BMT. mdb begins a
   * transaction, sends a msg to MDB_QUEUE. mdb commits. Verify that MDB_QUEUE
   * has the msg sent. .
   *
   */
  public void Test5() throws Fault {
    String TestCase = "xaTest5";
    int TestNum = 5;
    try {

      qSender = session.createSender(bmtQ);
      // create a text message
      createTestMessage(TestCase, TestNum);
      JmsUtil.addPropsToMessage(msg, props);
      // send the message
      qSender.send(msg);

      // verify that message was requeued and pass
      if (!checkOnResponse(TestCase)) {
        throw new Exception("Test5 - ");
      }
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: Test6
   *
   * @assertion_ids: EJB:SPEC:543; EJB:SPEC:543.1;
   *
   * @test_Strategy: Invoke an bmt mdb by writing to MDB_DURABLE_BMT. mdb begins
   * a transaction, sends a msg to MDB_QUEUE_REPLY. mdb commits. Verify that
   * MDB_QUEUE_REPLY has the msg sent.
   *
   */
  public void Test6() throws Fault {
    String TestCase = "xaTest6";
    int TestNum = 6;
    try {
      tPub = tSession.createPublisher(bmtT);
      // create a text message
      createTestMessage(TestCase, TestNum);
      JmsUtil.addPropsToMessage(msg, props);
      // send the message
      tPub.publish(msg);

      // verify that message was requeued and pass
      if (!checkOnResponse(TestCase)) {
        throw new Exception("Test6 - ");
      }
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: Test7
   *
   * @assertion_ids: JMS:SPEC:13; JMS:SPEC:129; JMS:SPEC:246.10;
   * JMS:JAVADOC:371; EJB:SPEC:586.1; EJB:SPEC:586.1.1; EJB:SPEC:583.2;
   * EJB:SPEC:583.2.1;
   *
   * @test_Strategy: Send a test message to a container managed topic. The mdb
   * tests if this is the first time this message was received by checking the
   * JMSRedelivered flag. Second time the msg is received is a a pass! If the
   * message is not requeued and received again this is a failure.
   */
  public void Test7() throws Fault {
    String TestCase = "xaTest7";
    int TestNum = 7;
    try {
      // construct a message for this test case
      tPub = tSession.createPublisher(cmtT);
      // create a text message
      createTestMessage(TestCase, TestNum);
      JmsUtil.addPropsToMessage(msg, props);
      msg.setStringProperty("TestCase", TestCase);
      // send the message
      tPub.publish(msg);
      // verify that message was requeued and pass
      System.out.println("Client:  response  message");
      if (!checkOnResponse(TestCase)) {
        throw new Exception("oops! error!");
      }
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: Test8
   *
   * @assertion_ids: EJB:SPEC:547.2;
   *
   * @test_Strategy: Invoke an bmt mdb by writing to MDB_QUEUE_BMT. mdb begins a
   * transaction, sends a msg to MDB_QUEUE. then rollsback. Verify the 2nd msg
   * is received.
   * 
   *
   */
  public void Test8() throws Fault {
    String TestCase = "xaTest8";
    int TestNum = 8;
    try {

      qSender = session.createSender(bmtQ);
      // create a text message
      createTestMessage(TestCase, TestNum);
      JmsUtil.addPropsToMessage(msg, props);
      // send the message
      qSender.send(msg);

      // verify that message was requeued and pass
      if (!checkOnResponse(TestCase)) {
        throw new Exception("Test8 - ");
      }
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: Test9
   *
   * @assertion_ids: EJB:SPEC:547.2;
   *
   * @test_Strategy: Invoke an bmt mdb by writing to MDB_DURABLE_BMT. mdb begins
   * a transaction, sends a msg to MDB_QUEUE_REPLY. then rollsback. Send msg and
   * commits. Verify the 2nd msg is received.
   * 
   *
   */
  public void Test9() throws Fault {
    String TestCase = "xaTest9";
    int TestNum = 9;
    try {

      tPub = tSession.createPublisher(bmtT);
      // create a text message
      createTestMessage(TestCase, TestNum);
      JmsUtil.addPropsToMessage(msg, props);
      // send the message
      tPub.publish(msg);

      // verify that message was requeued and pass
      if (!checkOnResponse(TestCase)) {
        throw new Exception("Test9 - ");
      }
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: Test10
   *
   * @assertion_ids: JMS:SPEC:13; JMS:SPEC:129; JMS:SPEC:246.10;
   * JMS:JAVADOC:371; EJB:SPEC:586.1; EJB:SPEC:586.1.1; EJB:SPEC:583.2;
   * EJB:SPEC:583.2.1;
   *
   * @test_Strategy: Send a test message to a container managed queue. The mdb
   * tests if this is the first time this message was received by checking the
   * JMSRedelivered flag. Send a msg, rollback. send a second message, pass if
   * 2nd msg received.
   * 
   *
   */
  public void Test10() throws Fault {
    String TestCase = "xaTest10";
    int TestNum = 10;
    try {

      // construct a message for this test case
      qSender = session.createSender(cmtQ);
      // create a text message
      createTestMessage(TestCase, TestNum);
      JmsUtil.addPropsToMessage(msg, props);
      // send the message
      qSender.send(msg);
      // verify that message was requeued and pass
      System.out.println("Client:  response  message");
      if (!checkOnResponse(TestCase)) {
        throw new Exception("oops! error!");
      }
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  public void createTestMessage(String TestCase, int num) {
    String myMessage = "Transaction tests";
    try {
      msg = session.createTextMessage();
      msg.setStringProperty("user", jmsUser);
      msg.setStringProperty("password", jmsPassword);
      msg.setText(myMessage);
      msg.setIntProperty("TestCaseNum", num);
      msg.setStringProperty("COM_SUN_JMS_TESTNAME", TestCase);

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg("Error setting user and password in jms msg");
    }
  }

  public boolean checkOnResponse(String prop) {
    boolean status = false;
    try {
      TestUtil.logTrace("@checkOnResponse");
      status = recvMessageInternal(session, prop);
      TestUtil.logTrace("Close the session");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
    return status;
  }

  public boolean recvMessageInternal(QueueSession session, String prop)
      throws JMSException {
    boolean retcode = false;
    TestUtil.logTrace("@recvMessageInternal");
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

  private void cleanTheQueue() {
    // make sure nothing is left in QUEUE_REPLY
  }

}
