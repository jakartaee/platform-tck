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
package com.sun.ts.tests.jms.ee.mdb.mdb_msgHdrT;

import java.io.*;
import java.util.*;
import javax.annotation.Resource;
import javax.ejb.EJB;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.javatest.Status;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.tests.jms.commonee.*;

public class MDBClient extends EETest {

  @EJB(name = "ejb/MDB_MSGHdrT_Test")
  private static MDB_T_Test hr;

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
  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    try {
      if (hr == null) {
        throw new Fault("@EJB injection failed");
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
      throw new Fault("Setup Failed!", e);
    }
  }

  /*
   * @testName: mdbMsgHdrTimeStampTTest
   *
   * @assertion_ids: JMS:SPEC:7; JMS:JAVADOC:347;
   *
   * @test_Strategy: Invoke a stateful session bean have the session bean call
   * an mdb to Send a single Text, map, bytes, stream, and object message check
   * time of send against time send returns JMSTimeStamp should be between these
   * two Send Pass/Fail message to MDB_QUEUE_REPLY Have session bean check the
   * queue for pass/fail results
   */
  public void mdbMsgHdrTimeStampTTest() throws Fault {
    String testCase = "msgHdrTimeStampTTest";
    try {
      // Have the EJB invoke the MDB
      TestUtil
          .logTrace("Call bean - have it tell mdb to run msgHdrTimeStampTTest");
      hr.askMDBToRunATest(testCase);
      if (!hr.checkOnResponse(testCase)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: msgHdrTimeStampTTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMsgHdrCorlIdTTextTest
   *
   * @assertion_ids: JMS:SPEC:246.7; JMS:JAVADOC:355; JMS:JAVADOC:357;
   * 
   * @test_Strategy: Invoke a stateful session bean have the session bean call
   * an mdb to Send a text message to a Topic with CorrelationID set. Receive
   * msg and verify the correlationid is as set by client Send Pass/Fail message
   * to MDB_QUEUE_REPLY Have session bean check the queue for pass/fail results
   * 
   */
  public void mdbMsgHdrCorlIdTTextTest() throws Fault {
    String testCase1 = "msgHdrCorlIdTTextTestCreate";
    String testCase2 = "msgHdrCorlIdTTextTest";
    try {
      // Have the EJB invoke the MDB
      TestUtil.logTrace(
          "Call bean - have it tell mdb to run msgHdrCorlIdTTextTestCreate");
      hr.askMDBToRunATest(testCase1);
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: msgHdrCorlIdTTextTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMsgHdrCorlIdTBytesTest
   *
   * @assertion_ids: JMS:SPEC:246.7; JMS:JAVADOC:355; JMS:JAVADOC:357;
   *
   * @test_Strategy: Invoke a stateful session bean have the session bean call
   * an mdb to send a Bytes message to a Topic with CorrelationID set. Receive
   * msg and verify the correlationid is as set by client Send Pass/Fail message
   * to MDB_QUEUE_REPLY Have session bean check the queue for pass/fail results
   */
  public void mdbMsgHdrCorlIdTBytesTest() throws Fault {
    String testCase1 = "msgHdrCorlIdTBytesTestCreate";
    String testCase2 = "msgHdrCorlIdTBytesTest";
    try {
      // Have the EJB invoke the MDB
      TestUtil.logTrace(
          "Call bean - have it tell mdb to run msgHdrCorlIdTBytesTestCreate");
      hr.askMDBToRunATest(testCase1);
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: msgHdrCorlIdTBytesTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMsgHdrCorlIdTMapTest
   *
   * @assertion_ids: JMS:SPEC:246.7; JMS:JAVADOC:355; JMS:JAVADOC:357;
   *
   * @test_Strategy: Invoke a stateful session bean have the session bean call
   * an mdb to send a map message to a Topic with CorrelationID set. Receive msg
   * and verify the correlationid is as set by client Send Pass/Fail message to
   * MDB_QUEUE_REPLY Have session bean check the queue for pass/fail results
   * 
   */
  public void mdbMsgHdrCorlIdTMapTest() throws Fault {
    String testCase1 = "msgHdrCorlIdTMapTestCreate";
    String testCase2 = "msgHdrCorlIdTMapTest";
    try {
      // Have the EJB invoke the MDB
      TestUtil.logTrace(
          "Call bean - have it tell mdb to run msgHdrCorlIdTMapTestCreate");
      hr.askMDBToRunATest(testCase1);
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: msgHdrCorlIdTMapTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMsgHdrCorlIdTStreamTest
   *
   * @assertion_ids: JMS:SPEC:246.7; JMS:JAVADOC:119; JMS:SPEC:12;
   * JMS:JAVADOC:355; JMS:JAVADOC:357;
   *
   * @test_Strategy: Invoke a stateful session bean have the session bean call
   * an mdb to send a stream message to a Topic with CorrelationID set. Receive
   * msg and verify the correlationid is as set by client Send Pass/Fail message
   * to MDB_QUEUE_REPLY Have session bean check the queue for pass/fail results
   * 
   */
  public void mdbMsgHdrCorlIdTStreamTest() throws Fault {
    String testCase1 = "msgHdrCorlIdTStreamTestCreate";
    String testCase2 = "msgHdrCorlIdTStreamTest";
    try {
      // Have the EJB invoke the MDB
      TestUtil.logTrace(
          "Call bean - have it tell mdb to run msgHdrCorlIdTStreamTestCreate");
      hr.askMDBToRunATest(testCase1);
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: msgHdrCorlIdTStreamTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMsgHdrCorlIdTObjectTest
   *
   * @assertion_ids: JMS:SPEC:246.7; JMS:JAVADOC:119; JMS:SPEC:12;
   * JMS:JAVADOC:355; JMS:JAVADOC:357;
   *
   * @test_Strategy: Invoke a stateful session bean have the session bean call
   * an mdb to send a stream message to a Topic with CorrelationID set. Receive
   * msg and verify the correlationid is as set by client Send Pass/Fail message
   * to MDB_QUEUE_REPLY Have session bean check the queue for pass/fail results
   * 
   */
  public void mdbMsgHdrCorlIdTObjectTest() throws Fault {
    String testCase1 = "msgHdrCorlIdTObjectTestCreate";
    String testCase2 = "msgHdrCorlIdTObjectTest";
    try {
      // Have the EJB invoke the MDB
      TestUtil.logTrace(
          "Call bean - have it tell mdb to run msgHdrCorlIdTObjectTestCreate");
      hr.askMDBToRunATest(testCase1);
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: msgHdrCorlIdTObjectTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMsgHdrReplyToTTest
   *
   * @assertion_ids: JMS:SPEC:12; JMS:JAVADOC:359; JMS:JAVADOC:361;
   * JMS:JAVADOC:286; JMS:JAVADOC:289; JMS:JAVADOC:562; JMS:JAVADOC:166;
   * JMS:SPEC:246.8;
   *
   * @test_Strategy: Invoke a session bean. Have the session bean request an mdb
   * to send a message to a Topic with ReplyTo set to a destination. Have the
   * mdb verify on receive. Send Pass/Fail message to MDB_QUEUE_REPLY Have
   * session bean check the queue for pass/fail results
   * 
   */
  public void mdbMsgHdrReplyToTTest() throws Fault {
    String testCase1 = "msgHdrReplyToTTestCreate";
    String testCase2 = "msgHdrReplyToTTest";
    try {
      // Have the EJB invoke the MDB
      TestUtil.logTrace(
          "Call bean - have it tell mdb to run msgHdrReplyToTTestCreate");
      hr.askMDBToRunATest(testCase1);
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: msgHdrReplyToTTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMsgHdrJMSTypeTTest
   *
   * @assertion_ids: JMS:SPEC:246.9; JMS:JAVADOC:375; JMS:JAVADOC:377;
   *
   * @test_Strategy: Invoke a session bean. Have the session bean request an mdb
   * to send a message to a Topic with JMSType set to TESTMSG verify on receive.
   * Send Pass/Fail message to MDB_QUEUE_REPLY Have session bean check the queue
   * for pass/fail results
   * 
   */
  public void mdbMsgHdrJMSTypeTTest() throws Fault {
    String testCase1 = "msgHdrJMSTypeTTestCreate";
    String testCase2 = "msgHdrJMSTypeTTest";
    try {
      // Have the EJB invoke the MDB
      TestUtil.logTrace(
          "Call bean - have it tell mdb to run msgHdrJMSTypeTTestCreate");
      hr.askMDBToRunATest(testCase1);
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: msgHdrJMSTypeTTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMsgHdrJMSPriorityTTest
   *
   * @assertion_ids: JMS:SPEC:16; JMS:SPEC:18; JMS:SPEC:140; JMS:JAVADOC:305;
   * JMS:JAVADOC:383;
   *
   * @test_Strategy: Invoke a stateful session bean have the session bean call
   * an mdb to send a message to a Topic with JMSPriority set to 2 test with
   * Text, map, object, byte, and stream messages Send Pass/Fail message to
   * MDB_QUEUE_REPLY Have session bean check the queue for pass/fail results
   * 
   */
  public void mdbMsgHdrJMSPriorityTTest() throws Fault {
    String testCase1 = "msgHdrJMSPriorityTTestCreate";
    String testCase2 = "msgHdrJMSPriorityTTest";
    try {
      // Have the EJB invoke the MDB
      TestUtil.logTrace(
          "Call bean - have it tell mdb to run mdbMsgHdrJMSTypeTTestCreate");
      hr.askMDBToRunATest(testCase1);
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: mdbMsgHdrJMSTypeTTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMsgHdrJMSExpirationTopicTest
   *
   * @assertion_ids: JMS:SPEC:15.2; JMS:SPEC:15.3; JMS:SPEC:140;
   * JMS:JAVADOC:309; JMS:JAVADOC:379;
   *
   * @test_Strategy: Invoke a stateful session bean have the session bean call
   * an mdb to send a message to a Topic with time to live set to 0 Verify that
   * JMSExpiration gets set to 0 test with Text, map, object, byte, and stream
   * messages Send Pass/Fail message to MDB_QUEUE_REPLY Have session bean check
   * the queue for pass/fail results
   * 
   */
  public void mdbMsgHdrJMSExpirationTopicTest() throws Fault {
    String testCase1 = "msgHdrJMSExpirationTopicTestCreate";
    String testCase2 = "msgHdrJMSExpirationTopicTest";
    try {
      // Have the EJB invoke the MDB
      TestUtil.logTrace(
          "Call bean - have it tell mdb to run msgHdrJMSExpirationTopicTestCreate");
      hr.askMDBToRunATest(testCase1);
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: msgHdrJMSExpirationTopicTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMsgHdrJMSDestinationTTest
   * 
   * @assertion_ids: JMS:SPEC:2; JMS:JAVADOC:363; JMS:JAVADOC:286;
   *
   * @test_Strategy: Invoke a stateful session bean have the session bean call
   * an mdb to create and send a message to the mdb Topic. Receive msg and
   * verify that JMSDestination is set as expected test with Text, map, object,
   * byte, and stream messages Send Pass/Fail message to MDB_QUEUE_REPLY Have
   * session bean check the queue for pass/fail results
   */
  public void mdbMsgHdrJMSDestinationTTest() throws Fault {
    String testCase1 = "msgHdrJMSDestinationTTestCreate";
    String testCase2 = "msgHdrJMSDestinationTTest";
    try {
      // Have the EJB invoke the MDB
      TestUtil.logTrace(
          "Call bean - have it tell mdb to run msgHdrJMSDestinationTTestCreate");
      hr.askMDBToRunATest(testCase1);
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: msgHdrJMSDestinationTTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMsgHdrJMSDeliveryModeTTest
   *
   * @assertion_ids: JMS:SPEC:3; JMS:SPEC:140; JMS:JAVADOC:367; JMS:SPEC:246.2;
   * JMS:JAVADOC:301;
   *
   * @test_Strategy: Invoke a stateful session bean have the session bean call
   * an mdb to create and send a message to the default Topic. Receive the msg
   * and verify that JMSDeliveryMode is set the default delivery mode of
   * persistent. Create and test another message with a nonpersistent delivery
   * mode. test with Text, map, object, byte, and stream messages Send Pass/Fail
   * message to MDB_QUEUE_REPLY Have session bean check the queue for pass/fail
   * results
   */
  public void mdbMsgHdrJMSDeliveryModeTTest() throws Fault {
    String testCase1 = "msgHdrJMSDeliveryModeTTestCreate";
    String testCase2 = "msgHdrJMSDeliveryModeTTest";
    try {
      // Have the EJB invoke the MDB
      TestUtil.logTrace(
          "Call bean - have it tell mdb to run msgHdrJMSDeliveryModeTTestCreate");
      hr.askMDBToRunATest(testCase1);
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: msgHdrJMSDeliveryModeTTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMsgHdrIDTTest
   *
   * @assertion_ids: JMS:SPEC:4; JMS:JAVADOC:343;
   *
   * @test_Strategy: Invoke a stateful session bean have the session bean call
   * an mdb to send and receive single Text, map, bytes, stream, and object
   * message call getJMSMessageID and verify that it starts with ID: Send
   * Pass/Fail message to MDB_QUEUE_REPLY Have session bean check the queue for
   * pass/fail results
   */
  public void mdbMsgHdrIDTTest() throws Fault {

    String testCase1 = "msgHdrIDTTestCreate";
    String testCase2 = "msgHdrIDTTest";
    try {
      // Have the EJB invoke the MDB
      TestUtil
          .logTrace("Call bean - have it tell mdb to run msgHdrIDTTestCreate");
      hr.askMDBToRunATest(testCase1);
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: msgHdrIDTTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /* cleanup -- none in this case */
  public void cleanup() throws Fault {
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
