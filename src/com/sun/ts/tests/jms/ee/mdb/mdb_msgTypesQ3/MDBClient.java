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
package com.sun.ts.tests.jms.ee.mdb.mdb_msgTypesQ3;

import java.io.*;
import java.util.*;
import javax.ejb.EJB;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.javatest.Status;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.tests.jms.commonee.*;

public class MDBClient extends EETest {

  @EJB(name = "ejb/MDB_MSGQ3_Test")
  private static MDB_Q_Test hr;

  private Properties props = null;

  public static void main(String[] args) {
    MDBClient theTests = new MDBClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup: */
  /*
   * @class.setup_props: jms_timeout; user; password;
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
   * @testName: mdbMsgClearBodyQueueTextTest
   *
   * @assertion_ids: JMS:SPEC:71; JMS:SPEC:72; JMS:JAVADOC:431; JMS:JAVADOC:473;
   * JMS:JAVADOC:449; JMS:SPEC:178; JMS:JAVADOC:291;
   *
   * @test_Strategy: Call a session bean. Have the session bean invoke an mdb to
   * Create and send a Text message. Have the mdb read the message call
   * clearBody, verify body is empty after clearBody. verify properties are not
   * effected by clearBody. Write to the message again 3.11
   */
  public void mdbMsgClearBodyQueueTextTest() throws Fault {
    String testCase1 = "msgClearBodyQueueTextTestCreate";
    String testCase2 = "msgClearBodyQueueTextTest";
    try {
      // Have the EJB invoke the MDB
      System.out
          .println("client - run testcase msgClearBodyQueueTextTestCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from msgClearBodyQueueTextTest");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: msgClearBodyQueueTextTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMsgClearBodyQueueObjectTest
   *
   * @assertion_ids: JMS:SPEC:71; JMS:SPEC:72; JMS:JAVADOC:431; JMS:JAVADOC:473;
   * JMS:JAVADOC:449; JMS:SPEC:178; JMS:JAVADOC:291;
   *
   * @test_Strategy: Call a session bean. Have the session bean invoke an mdb to
   * Create and send a Object message. Have the mdb read the message call
   * clearBody, verify body is empty after clearBody. verify properties are not
   * effected by clearBody. Write to the message again 3.11
   */
  public void mdbMsgClearBodyQueueObjectTest() throws Fault {
    String testCase1 = "msgClearBodyQueueObjectTestCreate";
    String testCase2 = "msgClearBodyQueueObjectTest";
    try {
      // Have the EJB invoke the MDB
      System.out
          .println("client - run testcase msgClearBodyQueueObjectTestCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from msgClearBodyQueueObjectTest");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: msgClearBodyQueueObjectTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMsgClearBodyQueueMapTest
   *
   * @assertion_ids: JMS:SPEC:71; JMS:SPEC:72; JMS:JAVADOC:431; JMS:JAVADOC:473;
   * JMS:JAVADOC:449; JMS:SPEC:178; JMS:JAVADOC:291;
   *
   * @test_Strategy: Call a session bean. Have the session bean invoke an mdb to
   * Create and send a Map message. Have the mdb read the message call
   * clearBody, verify body is empty after clearBody. verify properties are not
   * effected by clearBody. Write to the message again 3.11
   */
  public void mdbMsgClearBodyQueueMapTest() throws Fault {
    String testCase1 = "msgClearBodyQueueMapTestCreate";
    String testCase2 = "msgClearBodyQueueMapTest";
    try {
      // Have the EJB invoke the MDB
      System.out
          .println("client - run testcase msgClearBodyQueueMapTestCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out
          .println("client - Check for response from msgClearBodyQueueMapTest");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: msgClearBodyQueueMapTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMsgClearBodyQueueBytesTest
   *
   * @assertion_ids: JMS:SPEC:71; JMS:SPEC:72; JMS:JAVADOC:431; JMS:JAVADOC:473;
   * JMS:JAVADOC:449; JMS:SPEC:178; JMS:JAVADOC:291;
   *
   * @test_Strategy: Call a session bean. Have the session bean invoke an mdb to
   * Create and send a Bytes message. Have the mdb read the message call
   * clearBody, verify body is empty after clearBody. verify properties are not
   * effected by clearBody. Write to the message again 3.11
   */
  public void mdbMsgClearBodyQueueBytesTest() throws Fault {
    String testCase1 = "msgClearBodyQueueBytesTestCreate";
    String testCase2 = "msgClearBodyQueueBytesTest";
    try {
      // Have the EJB invoke the MDB
      System.out
          .println("client - run testcase msgClearBodyQueueBytesTestCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from msgClearBodyQueueBytesTest");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: msgClearBodyQueueBytesTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMsgClearBodyQueueStreamTest
   *
   * @assertion_ids: JMS:SPEC:71; JMS:SPEC:72; JMS:JAVADOC:431; JMS:JAVADOC:473;
   * JMS:JAVADOC:449; JMS:SPEC:178; JMS:JAVADOC:291;
   *
   * @test_Strategy: Call a session bean. Have the session bean invoke an mdb to
   * Create and send a Stream message. Have the mdb read the message call
   * clearBody, verify body is empty after clearBody. verify properties are not
   * effected by clearBody. Write to the message again 3.11
   */
  public void mdbMsgClearBodyQueueStreamTest() throws Fault {
    String testCase1 = "msgClearBodyQueueStreamTestCreate";
    String testCase2 = "msgClearBodyQueueStreamTest";
    try {
      // Have the EJB invoke the MDB
      System.out
          .println("client - run testcase msgClearBodyQueueStreamTestCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from msgClearBodyQueueStreamTest");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: msgClearBodyQueueStreamTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMsgResetQueueTest
   *
   * @assertion_ids: JMS:JAVADOC:174; JMS:JAVADOC:584;
   *
   * @test_Strategy: Call a session bean. Have the session bean invoke an mdb to
   * create a stream message and a byte message. write to the message body, call
   * the reset method, try to write to the body expect a
   * MessageNotWriteableException to be thrown.
   */
  public void mdbMsgResetQueueTest() throws Fault {
    String testCase = "msgResetQueueTest";
    try {
      // Have the EJB invoke the MDB
      System.out.println("client - run testcase msgResetQueueTest");
      hr.askMDBToRunATest(testCase); // create and send message to MDB_QUEUE
      System.out.println("client - Check for response from msgResetQueueTest");
      if (!hr.checkOnResponse(testCase)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: msgResetQueueTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbReadNullCharNotValidQueueMapTest
   *
   * @assertion_ids: JMS:SPEC:79; JMS:JAVADOC:134; JMS:JAVADOC:439;
   *
   * @test_Strategy: Call a session bean. Have the session bean invoke an mdb to
   * write a null string to a MapMessage. Attempt to read the null value as a
   * char.
   */
  public void mdbReadNullCharNotValidQueueMapTest() throws Fault {
    String testCase1 = "readNullCharNotValidQueueMapTestCreate";
    String testCase2 = "readNullCharNotValidQueueMapTest";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase readNullCharNotValidQueueMapTestCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from readNullCharNotValidQueueMapTest");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: readNullCharNotValidQueueMapTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }
  /*
   * @testName: mdbReadNullCharNotValidQueueStreamTest
   *
   * @assertion_ids: JMS:SPEC:79; JMS:JAVADOC:134; JMS:JAVADOC:439;
   *
   * @test_Strategy: Call a session bean. Have the session bean invoke an mdb to
   * write a null string to a StreamMessage. Attempt to read the null value as a
   * char.
   */

  public void mdbReadNullCharNotValidQueueStreamTest() throws Fault {
    String testCase1 = "readNullCharNotValidQueueStreamTestCreate";
    String testCase2 = "readNullCharNotValidQueueStreamTest";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase readNullCharNotValidQueueStreamTestCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from readNullCharNotValidQueueStreamTest");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: readNullCharNotValidQueueStreamTest failed");
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
