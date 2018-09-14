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
package com.sun.ts.tests.jms.ee.mdb.mdb_msgTypesT3;

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

/**
 * The MDBClient class invokes a test session bean, which will ask and the
 * message driven bean to send a text, byte, map, stream, and object message to
 * a queue
 */

public class MDBClient extends EETest {

  @EJB(name = "ejb/MDB_MSGT3_Test")
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

  /* Run tests */
  //

  /*
   * @testName: mdbMsgClearBodyTopicTextTest
   *
   * @assertion_ids: JMS:SPEC:71; JMS:SPEC:72; JMS:JAVADOC:431; JMS:JAVADOC:473;
   * JMS:JAVADOC:449; JMS:SPEC:178; JMS:JAVADOC:291;
   *
   * @test_Strategy: Call a session bean. Have the session bean invoke an mdb to
   * Create and send a Text message. Have the mdb read the message call
   * clearBody, verify body is empty after clearBody. verify properties are not
   * effected by clearBody. Write to the message again 3.11
   */
  public void mdbMsgClearBodyTopicTextTest() throws Fault {
    String testCase1 = "msgClearBodyTopicTextTestCreate";
    String testCase2 = "msgClearBodyTopicTextTest";
    try {
      // Have the EJB invoke the MDB
      System.out
          .println("client - run testcase msgClearBodyTopicTextTestCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from msgClearBodyTopicTextTest");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: msgClearBodyTopicTextTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMsgClearBodyTopicObjectTest
   *
   * @assertion_ids: JMS:SPEC:71; JMS:SPEC:72; JMS:JAVADOC:431; JMS:JAVADOC:473;
   * JMS:JAVADOC:449; JMS:SPEC:178; JMS:JAVADOC:291;
   *
   * @test_Strategy: Call a session bean. Have the session bean invoke an mdb to
   * Create and send a Object message. Have the mdb read the message call
   * clearBody, verify body is empty after clearBody. verify properties are not
   * effected by clearBody. Write to the message again 3.11
   */
  public void mdbMsgClearBodyTopicObjectTest() throws Fault {
    String testCase1 = "msgClearBodyTopicObjectTestCreate";
    String testCase2 = "msgClearBodyTopicObjectTest";
    try {
      // Have the EJB invoke the MDB
      System.out
          .println("client - run testcase msgClearBodyTopicObjectTestCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from msgClearBodyTopicObjectTest");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: msgClearBodyTopicObjectTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMsgClearBodyTopicMapTest
   *
   * @assertion_ids: JMS:SPEC:71; JMS:SPEC:72; JMS:JAVADOC:431; JMS:JAVADOC:473;
   * JMS:JAVADOC:449; JMS:SPEC:178; JMS:JAVADOC:291;
   *
   * @test_Strategy: Call a session bean. Have the session bean invoke an mdb to
   * Create and send a Map message. Have the mdb read the message call
   * clearBody, verify body is empty after clearBody. verify properties are not
   * effected by clearBody. Write to the message again 3.11
   */
  public void mdbMsgClearBodyTopicMapTest() throws Fault {
    String testCase1 = "msgClearBodyTopicMapTestCreate";
    String testCase2 = "msgClearBodyTopicMapTest";
    try {
      // Have the EJB invoke the MDB
      System.out
          .println("client - run testcase msgClearBodyTopicMapTestCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out
          .println("client - Check for response from msgClearBodyTopicMapTest");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: msgClearBodyTopicMapTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMsgClearBodyTopicBytesTest
   *
   * @assertion_ids: JMS:SPEC:71; JMS:SPEC:72; JMS:JAVADOC:431; JMS:JAVADOC:473;
   * JMS:JAVADOC:449; JMS:SPEC:178; JMS:JAVADOC:291;
   *
   * @test_Strategy: Call a session bean. Have the session bean invoke an mdb to
   * Create and send a Bytes message. Have the mdb read the message call
   * clearBody, verify body is empty after clearBody. verify properties are not
   * effected by clearBody. Write to the message again 3.11
   */
  public void mdbMsgClearBodyTopicBytesTest() throws Fault {
    String testCase1 = "msgClearBodyTopicBytesTestCreate";
    String testCase2 = "msgClearBodyTopicBytesTest";
    try {
      // Have the EJB invoke the MDB
      System.out
          .println("client - run testcase msgClearBodyTopicBytesTestCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from msgClearBodyTopicBytesTest");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: msgClearBodyTopicBytesTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMsgClearBodyTopicStreamTest
   *
   * @assertion_ids: JMS:SPEC:71; JMS:SPEC:72; JMS:JAVADOC:431; JMS:JAVADOC:473;
   * JMS:JAVADOC:449; JMS:SPEC:178; JMS:JAVADOC:291;
   *
   * @test_Strategy: Call a session bean. Have the session bean invoke an mdb to
   * Create and send a Stream message. Have the mdb read the message call
   * clearBody, verify body is empty after clearBody. verify properties are not
   * effected by clearBody. Write to the message again 3.11
   */
  public void mdbMsgClearBodyTopicStreamTest() throws Fault {
    String testCase1 = "msgClearBodyTopicStreamTestCreate";
    String testCase2 = "msgClearBodyTopicStreamTest";
    try {
      // Have the EJB invoke the MDB
      System.out
          .println("client - run testcase msgClearBodyTopicStreamTestCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from msgClearBodyTopicStreamTest");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: msgClearBodyTopicStreamTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMsgResetTopicTest
   *
   * @assertion_ids: JMS:JAVADOC:174; JMS:JAVADOC:584;
   *
   * @test_Strategy: Call a session bean. Have the session bean invoke an mdb to
   * create a stream message and a byte message. write to the message body, call
   * the reset method, try to write to the body expect a
   * MessageNotWriteableException to be thrown.
   * 
   * 
   * 
   */
  public void mdbMsgResetTopicTest() throws Fault {
    String testCase = "msgResetTopicTest";
    try {
      // Have the EJB invoke the MDB
      System.out.println("client - run testcase msgResetTopicTest");
      hr.askMDBToRunATest(testCase); // create and send message to MDB_QUEUE
      System.out.println("client - Check for response from msgResetTopicTest");
      if (!hr.checkOnResponse(testCase)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: msgResetTopicTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbReadNullCharNotValidTopicMapTest
   *
   * @assertion_ids: JMS:SPEC:79; JMS:JAVADOC:134; JMS:JAVADOC:439;
   *
   * @test_Strategy: Call a session bean. Have the session bean invoke an mdb to
   * write a null string to a MapMessage. Attempt to read the null value as a
   * char.
   */
  public void mdbReadNullCharNotValidTopicMapTest() throws Fault {
    String testCase1 = "readNullCharNotValidTopicMapTestCreate";
    String testCase2 = "readNullCharNotValidTopicMapTest";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase readNullCharNotValidTopicMapTestCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from readNullCharNotValidTopicMapTest");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: readNullCharNotValidTopicMapTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }
  /*
   * @testName: mdbReadNullCharNotValidTopicStreamTest
   *
   * @assertion_ids: JMS:SPEC:79; JMS:JAVADOC:134; JMS:JAVADOC:439;
   *
   * @test_Strategy: Call a session bean. Have the session bean invoke an mdb to
   * write a null string to a StreamMessage. Attempt to read the null value as a
   * char.
   */

  public void mdbReadNullCharNotValidTopicStreamTest() throws Fault {
    String testCase1 = "readNullCharNotValidTopicStreamTestCreate";
    String testCase2 = "readNullCharNotValidTopicStreamTest";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase readNullCharNotValidTopicStreamTestCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from readNullCharNotValidTopicStreamTest");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: readNullCharNotValidTopicStreamTest failed");
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
