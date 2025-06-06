/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jms.ee.mdb.mdb_msgHdrQ;

import java.util.Properties;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.commonee.MDB_Q_Test;

import jakarta.ejb.EJB;

public class MDBClient extends EETest {

  @EJB(name = "ejb/MDB_MSGHdrQ_Test")
  private static MDB_Q_Test hr;

  private Properties props = null;

  public static void main(String[] args) {
    MDBClient theTests = new MDBClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup: */
  /*
   * @class.setup_props: jms_timeout; user; password; harness.log.port;
   * harness.log.traceflag;
   */
  public void setup(String[] args, Properties p) throws Exception {
    props = p;
    try {
      if (hr == null) {
        throw new Exception("@EJB injection failed");
      }
      hr.setup(p);
      if (hr.isThereSomethingInTheQueue()) {
        TestUtil.logTrace("Error: message(s) left in Q");
        hr.cleanTheQueue();
      } else {
        TestUtil.logTrace("Nothing left in queue");
      }
      logMsg("Setup ok;");
    } catch (Exception e) {
      throw new Exception("Setup Failed!", e);
    }
  }

  /* Run tests */
  //
  /*
   * @testName: mdbMsgHdrTimeStampQTest
   *
   * @assertion_ids: JMS:SPEC:7; JMS:JAVADOC:347;
   *
   * @test_Strategy: With a queue destination Invoke a session bean, have the
   * bean request an mdb for a queue
   * 
   * to send a single Text, map, bytes, stream, and object message check time of
   * send against time send returns JMSTimeStamp should be between these two
   * Have the mdb send the test results to MDB_QUEUE_REPLY
   */
  public void mdbMsgHdrTimeStampQTest() throws Exception {
    String testCase = "msgHdrTimeStampQTest";
    try {
      // Have the EJB invoke the MDB
      TestUtil
          .logTrace("Call bean - have it tell mdb to run msgHdrTimeStampQTest");
      hr.askMDBToRunATest(testCase);
      if (!hr.checkOnResponse(testCase)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Exception("ERROR: msgHdrTimeStampQTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Exception("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMsgHdrCorlIdQTextTest
   *
   * @assertion_ids: JMS:SPEC:246.7; JMS:JAVADOC:355; JMS:JAVADOC:357;
   *
   * @test_Strategy: Invoke a session bean, have the bean request an mdb for a
   * queue to send a text message to a Queue with CorrelationID set. Receive msg
   * and verify the correlationid is as set by client Have the mdb send the test
   * results to MDB_QUEUE_REPLY
   * 
   */
  public void mdbMsgHdrCorlIdQTextTest() throws Exception {
    String testCase1 = "msgHdrCorlIdQTextTestCreate";
    String testCase2 = "msgHdrCorlIdQTextTest";
    try {
      // Have the EJB invoke the MDB
      TestUtil.logTrace(
          "Call bean - have it tell mdb to run msgHdrCorlIdQTextTestCreate");
      hr.askMDBToRunATest(testCase1);
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Exception("ERROR: msgHdrCorlIdQTextTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Exception("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMsgHdrCorlIdQBytesTest
   *
   * @assertion_ids: JMS:SPEC:246.7; JMS:JAVADOC:355; JMS:JAVADOC:357;
   * 
   * @test_Strategy: Invoke a session bean, have the bean request an mdb for a
   * queue to send a Bytes message to a Queue with CorrelationID set. Receive
   * msg and verify the correlationid is as set by client Have the mdb send the
   * test results to MDB_QUEUE_REPLY
   * 
   */
  public void mdbMsgHdrCorlIdQBytesTest() throws Exception {
    String testCase1 = "msgHdrCorlIdQBytesTestCreate";
    String testCase2 = "msgHdrCorlIdQBytesTest";
    try {
      // Have the EJB invoke the MDB
      TestUtil.logTrace(
          "Call bean - have it tell mdb to run msgHdrCorlIdQBytesTestCreate");
      hr.askMDBToRunATest(testCase1);
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Exception("ERROR: msgHdrCorlIdQBytesTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Exception("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMsgHdrCorlIdQMapTest
   *
   * @assertion_ids: JMS:SPEC:246.7; JMS:JAVADOC:355; JMS:JAVADOC:357;
   *
   * @test_Strategy: Invoke a session bean, have the bean request an mdb for a
   * queue to send a map message to a Queue with CorrelationID set. Receive msg
   * and verify the correlationid is as set by client Have the mdb send the test
   * results to MDB_QUEUE_REPLY
   * 
   */
  public void mdbMsgHdrCorlIdQMapTest() throws Exception {
    String testCase1 = "msgHdrCorlIdQMapTestCreate";
    String testCase2 = "msgHdrCorlIdQMapTest";
    try {
      // Have the EJB invoke the MDB
      TestUtil.logTrace(
          "Call bean - have it tell mdb to run msgHdrCorlIdQMapTestCreate");
      hr.askMDBToRunATest(testCase1);
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Exception("ERROR: msgHdrCorlIdQMapTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Exception("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMsgHdrCorlIdQStreamTest
   *
   * @assertion_ids: JMS:SPEC:246.7; JMS:JAVADOC:355; JMS:JAVADOC:357;
   * 
   * @test_Strategy: Invoke a session bean, have the bean request an mdb for a
   * queue to send a stream message to a Queue with CorrelationID set. Receive
   * msg and verify the correlationid is as set by client Have the mdb send the
   * test results to MDB_QUEUE_REPLY
   * 
   */
  public void mdbMsgHdrCorlIdQStreamTest() throws Exception {
    String testCase1 = "msgHdrCorlIdQStreamTestCreate";
    String testCase2 = "msgHdrCorlIdQStreamTest";
    try {
      // Have the EJB invoke the MDB
      TestUtil.logTrace(
          "Call bean - have it tell mdb to run msgHdrCorlIdQStreamTestCreate");
      hr.askMDBToRunATest(testCase1);
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Exception("ERROR: msgHdrCorlIdQStreamTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Exception("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMsgHdrCorlIdQObjectTest
   *
   * @assertion_ids: JMS:SPEC:246.7; JMS:JAVADOC:355; JMS:JAVADOC:357;
   * 
   *
   * @test_Strategy: Send an Object message to a Queue with CorrelationID set.
   * Receive msg and verify the correlationid is as set by client Have the mdb
   * send the test results to MDB_QUEUE_REPLY
   * 
   */
  public void mdbMsgHdrCorlIdQObjectTest() throws Exception {
    String testCase1 = "msgHdrCorlIdQObjectTestCreate";
    String testCase2 = "msgHdrCorlIdQObjectTest";
    try {
      // Have the EJB invoke the MDB
      TestUtil.logTrace(
          "Call bean - have it tell mdb to run msgHdrCorlIdQObjectTestCreate");
      hr.askMDBToRunATest(testCase1);
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Exception("ERROR: msgHdrCorlIdQObjectTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Exception("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMsgHdrReplyToQTest
   *
   * @assertion_ids: JMS:SPEC:12; JMS:SPEC:246.8; JMS:JAVADOC:359;
   * JMS:JAVADOC:361; JMS:JAVADOC:286; JMS:JAVADOC:562; JMS:JAVADOC:166;
   * 
   *
   * @test_Strategy: Invoke a session bean. Have the session bean request an mdb
   * to send a message to a Queue with ReplyTo set to a destination. Have the
   * mdb verify on receive. Have the mdb send the test results to
   * MDB_QUEUE_REPLY
   * 
   */
  public void mdbMsgHdrReplyToQTest() throws Exception {
    String testCase1 = "msgHdrReplyToQTestCreate";
    String testCase2 = "msgHdrReplyToQTest";
    try {
      // Have the EJB invoke the MDB
      TestUtil.logTrace(
          "Call bean - have it tell mdb to run msgHdrReplyToQTestCreate");
      hr.askMDBToRunATest(testCase1);
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Exception("ERROR: msgHdrReplyToQTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Exception("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMsgHdrJMSTypeQTest
   *
   * @assertion_ids: JMS:SPEC:246.9; JMS:JAVADOC:375; JMS:JAVADOC:377;
   *
   * @test_Strategy: Invoke a session bean. Have the session bean request an mdb
   * to send a message to a Queue with JMSType set to TESTMSG verify on receive.
   * Have the mdb send the test results to MDB_QUEUE_REPLY
   * 
   */
  public void mdbMsgHdrJMSTypeQTest() throws Exception {
    String testCase1 = "msgHdrJMSTypeQTestCreate";
    String testCase2 = "msgHdrJMSTypeQTest";
    try {
      // Have the EJB invoke the MDB
      TestUtil.logTrace(
          "Call bean - have it tell mdb to run msgHdrJMSTypeQTestCreate");
      hr.askMDBToRunATest(testCase1);
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Exception("ERROR: msgHdrJMSTypeQTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Exception("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMsgHdrJMSPriorityQTest
   *
   * @assertion_ids: JMS:SPEC:16; JMS:SPEC:18; JMS:SPEC:140; JMS:JAVADOC:305;
   * JMS:JAVADOC:383;
   *
   * @test_Strategy: Invoke a session bean, have the bean request an mdb for a
   * queue to send a message to a Queue with JMSPriority set to 2 test with
   * Text, map, object, byte, and stream messages verify on receive. Have the
   * mdb send the test results to MDB_QUEUE_REPLY
   * 
   */
  public void mdbMsgHdrJMSPriorityQTest() throws Exception {
    String testCase1 = "msgHdrJMSPriorityQTestCreate";
    String testCase2 = "msgHdrJMSPriorityQTest";
    try {
      // Have the EJB invoke the MDB
      TestUtil.logTrace(
          "Call bean - have it tell mdb to run mdbMsgHdrJMSTypeQTestCreate");
      hr.askMDBToRunATest(testCase1);
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Exception("ERROR: mdbMsgHdrJMSTypeQTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Exception("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMsgHdrJMSExpirationQueueTest
   *
   * @assertion_ids: JMS:SPEC:15.2; JMS:SPEC:15.3; JMS:SPEC:140;
   * JMS:JAVADOC:309; JMS:JAVADOC:379;
   *
   * @test_Strategy: Invoke a session bean, have the bean request an mdb for a
   * queue to send a message to a Queue with time to live set to 0 Verify that
   * JMSExpiration gets set to 0 test with Text, map, object, byte, and stream
   * messages verify on receive. Have the mdb send the test results to
   * MDB_QUEUE_REPLY
   * 
   */
  public void mdbMsgHdrJMSExpirationQueueTest() throws Exception {
    String testCase1 = "msgHdrJMSExpirationQueueTestCreate";
    String testCase2 = "msgHdrJMSExpirationQueueTest";
    try {
      // Have the EJB invoke the MDB
      TestUtil.logTrace(
          "Call bean - have it tell mdb to run msgHdrJMSExpirationQueueTestCreate");
      hr.askMDBToRunATest(testCase1);
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Exception("ERROR: msgHdrJMSExpirationQueueTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Exception("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMsgHdrJMSDestinationQTest
   * 
   * @assertion_ids: JMS:SPEC:2; JMS:JAVADOC:363; JMS:JAVADOC:286;
   *
   * @test_Strategy: Invoke a session bean, have the bean request an mdb for a
   * queue to create and send a message set to the default Queue. Receive msg
   * and verify that JMSDestination is set to the default Queue test with Text,
   * map, object, byte, and stream messages Have the mdb send the test results
   * to MDB_QUEUE_REPLY
   */
  public void mdbMsgHdrJMSDestinationQTest() throws Exception {
    String testCase1 = "msgHdrJMSDestinationQTestCreate";
    String testCase2 = "msgHdrJMSDestinationQTest";
    try {
      // Have the EJB invoke the MDB
      TestUtil.logTrace(
          "Call bean - have it tell mdb to run msgHdrJMSDestinationQTestCreate");
      hr.askMDBToRunATest(testCase1);
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Exception("ERROR: msgHdrJMSDestinationQTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Exception("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMsgHdrJMSDeliveryModeQTest
   *
   * @assertion_ids: JMS:SPEC:246.2; JMS:SPEC:3; JMS:SPEC:140; JMS:JAVADOC:367;
   * JMS:JAVADOC:301;
   *
   * @test_Strategy: Invoke a session bean, have the bean request an mdb for a
   * queue to create and send a message. Receive the msg and verify that
   * JMSDeliveryMode is set the default delivery mode of persistent. Create and
   * test another message with a nonpersistent delivery mode. test with Text,
   * map, object, byte, and stream messages Have the mdb send the test results
   * to MDB_QUEUE_REPLY
   */
  public void mdbMsgHdrJMSDeliveryModeQTest() throws Exception {
    String testCase1 = "msgHdrJMSDeliveryModeQTestCreate";
    String testCase2 = "msgHdrJMSDeliveryModeQTest";
    try {
      // Have the EJB invoke the MDB
      TestUtil.logTrace(
          "Call bean - have it tell mdb to run msgHdrJMSDeliveryModeQTestCreate");
      hr.askMDBToRunATest(testCase1);
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Exception("ERROR: msgHdrJMSDeliveryModeQTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Exception("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMsgHdrIDQTest
   *
   * @assertion_ids: JMS:SPEC:4; JMS:JAVADOC:343;
   *
   * @test_Strategy: Invoke a session bean, have the bean request an mdb for a
   * queue to send and receive single Text, map, bytes, stream, and object
   * message call getJMSMessageID and verify that it starts with ID: 3.4.3 Have
   * the mdb send the test results to MDB_QUEUE_REPLY
   */
  public void mdbMsgHdrIDQTest() throws Exception {

    String testCase1 = "msgHdrIDQTestCreate";
    String testCase2 = "msgHdrIDQTest";
    try {
      // Have the EJB invoke the MDB

      TestUtil
          .logTrace("Call bean - have it tell mdb to run msgHdrIDQTestCreate");
      hr.askMDBToRunATest(testCase1);
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Exception("ERROR: msgHdrIDQTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Exception("Test Failed!", e);
    }
  }

  /* cleanup -- none in this case */
  public void cleanup() throws Exception {
    try {
      if (hr.isThereSomethingInTheQueue()) {
        TestUtil.logTrace("Error: message(s) left in Q");
        hr.cleanTheQueue();
      } else {
        TestUtil.logTrace("Nothing left in queue");
      }
      logMsg("End  of client cleanup;");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
    ;
  }

}
