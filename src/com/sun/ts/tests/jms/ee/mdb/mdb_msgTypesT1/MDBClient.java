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
package com.sun.ts.tests.jms.ee.mdb.mdb_msgTypesT1;

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

  @EJB(name = "ejb/MDB_MSGT1_Test")
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
   * @testName: mdbBytesMsgNullStreamTopicTest
   *
   * @assertion_ids: JMS:SPEC:86.1; JMS:JAVADOC:714;
   *
   * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
   * create a byte message. Use writeObject to write a null. verify a
   * java.lang.NullPointerException is thrown.
   * 
   *
   */
  public void mdbBytesMsgNullStreamTopicTest() throws Fault {
    String testCase = "bytesMsgNullStreamTopicTest";
    try {
      // Have the EJB invoke the MDB
      TestUtil.logTrace(
          "Call bean - have it tell mdb to run bytesMsgNullStreamTopicTest");
      hr.askMDBToRunATest(testCase);
      if (!hr.checkOnResponse(testCase)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: mdbBytesMsgNullStreamTopicTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }
  /*
   * @testName: mdbBytesMessageTopicTestsFullMsg
   * 
   * @assertion_ids: JMS:JAVADOC:560; JMS:JAVADOC:562; JMS:JAVADOC:564;
   * JMS:JAVADOC:566; JMS:JAVADOC:568; JMS:JAVADOC:570; JMS:JAVADOC:572;
   * JMS:JAVADOC:574; JMS:JAVADOC:576; JMS:JAVADOC:578; JMS:JAVADOC:580;
   * JMS:JAVADOC:582; JMS:JAVADOC:534; JMS:JAVADOC:536; JMS:JAVADOC:540;
   * JMS:JAVADOC:544; JMS:JAVADOC:546; JMS:JAVADOC:548; JMS:JAVADOC:550;
   * JMS:JAVADOC:552; JMS:JAVADOC:554; JMS:JAVADOC:556; JMS:JAVADOC:558;
   * 
   * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
   * create a BytesMessage -. write to the message using each type of method and
   * as an object. Send the message. Verify the data received was as sent.
   * 
   */

  public void mdbBytesMessageTopicTestsFullMsg() throws Fault {
    String testCase1 = "bytesMessageTopicTestsFullMsgCreate";
    String testCase2 = "bytesMessageTopicTestsFullMsg";
    try {
      // Have the EJB invoke the MDB
      System.out
          .println("client - run testcase bytesMessageTopicTestsFullMsgCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_TOPIC
      // System.out.println("client - run testcase2
      // bytesMessageTopicTestsFullMsg")
      // hr.askMDBToRunATest(testCase2) ; // read and verify message sent
      System.out.println(
          "client - Check for response from bytesMessageTopicTestsFullMsg");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: bytesMessageTopicTestsFullMsg failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMapMessageFullMsgTopicTest
   *
   * @assertion_ids: JMS:SPEC:74; JMS:JAVADOC:211; JMS:JAVADOC:457;
   * JMS:JAVADOC:459; JMS:JAVADOC:475; JMS:JAVADOC:477; JMS:JAVADOC:479;
   * JMS:JAVADOC:461; JMS:JAVADOC:463; JMS:JAVADOC:465; JMS:JAVADOC:467;
   * JMS:JAVADOC:469; JMS:JAVADOC:471; JMS:JAVADOC:473; JMS:JAVADOC:433;
   * JMS:JAVADOC:435; JMS:JAVADOC:437; JMS:JAVADOC:439; JMS:JAVADOC:441;
   * JMS:JAVADOC:443; JMS:JAVADOC:445; JMS:JAVADOC:447; JMS:JAVADOC:449;
   * JMS:JAVADOC:451; JMS:JAVADOC:453;
   *
   * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
   * create a MapMessage -. write to the message using each type of method and
   * as an object. Send the message. Verify the data received was as sent.
   * 
   */
  public void mdbMapMessageFullMsgTopicTest() throws Fault {
    String testCase1 = "mapMessageFullMsgTopicTestCreate";
    String testCase2 = "mapMessageFullMsgTopicTest";
    try {
      // Have the EJB invoke the MDB
      System.out
          .println("client - run testcase mapMessageFullMsgTopicTestCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_TOPIC
      System.out.println(
          "client - Check for response from mapMessageFullMsgTopicTest");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Exception("ERROR: mapMessageFullMsgTopicTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }
  /*
   * @testName: mdbMapMessageConversionTopicTestsBoolean
   * 
   * @assertion_ids: JMS:SPEC:75.1; JMS:SPEC:75.2; JMS:JAVADOC:457;
   * JMS:JAVADOC:433; JMS:JAVADOC:449; JMS:JAVADOC:796; JMS:JAVADOC:797;
   * JMS:JAVADOC:798; JMS:JAVADOC:799; JMS:JAVADOC:800; JMS:JAVADOC:801;
   * JMS:JAVADOC:802; JMS:JAVADOC:804;
   * 
   * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
   * create a MapMessage -. use MapMessage method writeBoolean to write a
   * boolean to the message. Verify the proper conversion support as in 3.11.3
   */

  public void mdbMapMessageConversionTopicTestsBoolean() throws Fault {
    String testCase1 = "mapMessageConversionTopicTestsBooleanCreate";
    String testCase2 = "mapMessageConversionTopicTestsBoolean";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase mapMessageConversionTopicTestsBooleanCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_TOPIC
      System.out.println(
          "client - Check for response from mapMessageConversionTopicTestsBoolean");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Exception(
            "ERROR: mapMessageConversionTopicTestsBoolean failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMapMessageConversionTopicTestsByte
   * 
   * @assertion_ids: JMS:SPEC:75.3; JMS:SPEC:75.4; JMS:JAVADOC:459;
   * JMS:JAVADOC:435; JMS:JAVADOC:437; JMS:JAVADOC:441; JMS:JAVADOC:443;
   * JMS:JAVADOC:449; JMS:JAVADOC:795; JMS:JAVADOC:798; JMS:JAVADOC:801;
   * JMS:JAVADOC:802; JMS:JAVADOC:804;
   * 
   * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
   * create a MapMessage -. use MapMessage method setByte to write a byte.
   * Verify the proper conversion support as in 3.11.3
   * 
   */
  public void mdbMapMessageConversionTopicTestsByte() throws Fault {
    String testCase1 = "mapMessageConversionTopicTestsByteCreate";
    String testCase2 = "mapMessageConversionTopicTestsByte";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase mapMessageConversionTopicTestsByteCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_TOPIC
      System.out.println(
          "client - Check for response from mapMessageConversionTopicTestsByte");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Exception("ERROR: mapMessageConversionTopicTestsByte failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMapMessageConversionTopicTestsShort
   * 
   * @assertion_ids: JMS:SPEC:75.5; JMS:SPEC:75.6; JMS:JAVADOC:461;
   * JMS:JAVADOC:437; JMS:JAVADOC:441; JMS:JAVADOC:443; JMS:JAVADOC:449;
   * JMS:JAVADOC:795; JMS:JAVADOC:796; JMS:JAVADOC:798; JMS:JAVADOC:801;
   * JMS:JAVADOC:802; JMS:JAVADOC:804;
   * 
   * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
   * create a MapMessage -. use MapMessage method writeShort to write a short.
   * Verify the proper conversion support as in 3.11.3
   * 
   */
  public void mdbMapMessageConversionTopicTestsShort() throws Fault {
    String testCase1 = "mapMessageConversionTopicTestsShortCreate";
    String testCase2 = "mapMessageConversionTopicTestsShort";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase mapMessageConversionTopicTestsShortCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_TOPIC
      System.out.println(
          "client - Check for response from mapMessageConversionTopicTestsShort");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Exception(
            "ERROR: mapMessageConversionTopicTestsShort failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMapMessageConversionTopicTestsChar
   * 
   * @assertion_ids: JMS:SPEC:75.7; JMS:SPEC:75.8; JMS:JAVADOC:463;
   * JMS:JAVADOC:439; JMS:JAVADOC:449; JMS:JAVADOC:795; JMS:JAVADOC:796;
   * JMS:JAVADOC:797; JMS:JAVADOC:799; JMS:JAVADOC:800; JMS:JAVADOC:801;
   * JMS:JAVADOC:802; JMS:JAVADOC:804;
   * 
   * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
   * create a MapMessage -. use MapMessage method writeChar to write a Char.
   * Verify the proper conversion support as in 3.11.3
   * 
   */
  public void mdbMapMessageConversionTopicTestsChar() throws Fault {
    String testCase1 = "mapMessageConversionTopicTestsCharCreate";
    String testCase2 = "mapMessageConversionTopicTestsChar";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase mapMessageConversionTopicTestsCharCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_TOPIC
      System.out.println(
          "client - Check for response from mapMessageConversionTopicTestsChar");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Exception("ERROR: mapMessageConversionTopicTestsChar failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMapMessageConversionTopicTestsInt
   * 
   * @assertion_ids: JMS:SPEC:75.9; JMS:SPEC:75.10; JMS:JAVADOC:465;
   * JMS:JAVADOC:441; JMS:JAVADOC:443; JMS:JAVADOC:449; JMS:JAVADOC:795;
   * JMS:JAVADOC:796; JMS:JAVADOC:797; JMS:JAVADOC:798; JMS:JAVADOC:801;
   * JMS:JAVADOC:802; JMS:JAVADOC:804;
   * 
   * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
   * create a MapMessage -. use MapMessage method writeInt to write a int.
   * Verify the proper conversion support as in 3.11.3
   * 
   */
  public void mdbMapMessageConversionTopicTestsInt() throws Fault {
    String testCase1 = "mapMessageConversionTopicTestsIntCreate";
    String testCase2 = "mapMessageConversionTopicTestsInt";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase mapMessageConversionTopicTestsIntCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_TOPIC
      System.out.println(
          "client - Check for response from mapMessageConversionTopicTestsInt");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Exception("ERROR: mapMessageConversionTopicTestsInt failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMapMessageConversionTopicTestsLong
   * 
   * @assertion_ids: JMS:SPEC:75.11; JMS:SPEC:75.12; JMS:JAVADOC:467;
   * JMS:JAVADOC:443; JMS:JAVADOC:449; JMS:JAVADOC:795; JMS:JAVADOC:796;
   * JMS:JAVADOC:797; JMS:JAVADOC:798; JMS:JAVADOC:799; JMS:JAVADOC:801;
   * JMS:JAVADOC:802; JMS:JAVADOC:804;
   * 
   * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
   * create a MapMessage -. use MapMessage method writeLong to write a long.
   * Verify the proper conversion support as in 3.11.3
   * 
   */
  public void mdbMapMessageConversionTopicTestsLong() throws Fault {
    String testCase1 = "mapMessageConversionTopicTestsLongCreate";
    String testCase2 = "mapMessageConversionTopicTestsLong";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase mapMessageConversionTopicTestsLongCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_TOPIC
      System.out.println(
          "client - Check for response from mapMessageConversionTopicTestsLong");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Exception("ERROR: mapMessageConversionTopicTestsLong failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMapMessageConversionTopicTestsFloat
   * 
   * @assertion_ids: JMS:SPEC:75.13; JMS:SPEC:75.14; JMS:JAVADOC:469;
   * JMS:JAVADOC:445; JMS:JAVADOC:449; JMS:JAVADOC:795; JMS:JAVADOC:796;
   * JMS:JAVADOC:797; JMS:JAVADOC:798; JMS:JAVADOC:799; JMS:JAVADOC:800;
   * JMS:JAVADOC:802; JMS:JAVADOC:804;
   * 
   * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
   * create a MapMessage -. use MapMessage method writeFloat to write a float.
   * Verify the proper conversion support as in 3.11.3
   * 
   */
  public void mdbMapMessageConversionTopicTestsFloat() throws Fault {
    String testCase1 = "mapMessageConversionTopicTestsFloatCreate";
    String testCase2 = "mapMessageConversionTopicTestsFloat";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase mapMessageConversionTopicTestsFloatCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_TOPIC
      System.out.println(
          "client - Check for response from mapMessageConversionTopicTestsFloat");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Exception(
            "ERROR: mapMessageConversionTopicTestsFloat failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMapMessageConversionTopicTestsDouble
   * 
   * @assertion_ids: JMS:SPEC:75.15; JMS:SPEC:75.16; JMS:JAVADOC:471;
   * JMS:JAVADOC:447; JMS:JAVADOC:449; JMS:JAVADOC:795; JMS:JAVADOC:796;
   * JMS:JAVADOC:797; JMS:JAVADOC:798; JMS:JAVADOC:799; JMS:JAVADOC:800;
   * JMS:JAVADOC:801; JMS:JAVADOC:804;
   * 
   * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
   * create a MapMessage -. use MapMessage method writeDouble to write a double.
   * Verify the proper conversion support as in 3.11.3
   * 
   */
  public void mdbMapMessageConversionTopicTestsDouble() throws Fault {
    String testCase1 = "mapMessageConversionTopicTestsDoubleCreate";
    String testCase2 = "mapMessageConversionTopicTestsDouble";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase mapMessageConversionTopicTestsDoubleCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_TOPIC
      System.out.println(
          "client - Check for response from mapMessageConversionTopicTestsDouble");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Exception(
            "ERROR: mapMessageConversionTopicTestsDouble failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMapMessageConversionTopicTestsString
   * 
   * @assertion_ids: JMS:SPEC:75.17; JMS:SPEC:75.18; JMS:JAVADOC:473;
   * JMS:JAVADOC:433; JMS:JAVADOC:435; JMS:JAVADOC:437; JMS:JAVADOC:441;
   * JMS:JAVADOC:443; JMS:JAVADOC:445; JMS:JAVADOC:447; JMS:JAVADOC:449;
   * JMS:JAVADOC:798; JMS:JAVADOC:804;
   * 
   * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
   * create a MapMessage -. use MapMessage method writeString to write a String.
   * Verify the proper conversion support as in 3.11.3
   * 
   */
  public void mdbMapMessageConversionTopicTestsString() throws Fault {
    String testCase1 = "mapMessageConversionTopicTestsStringCreate";
    String testCase2 = "mapMessageConversionTopicTestsString";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase mapMessageConversionTopicTestsStringCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_TOPIC
      System.out.println(
          "client - Check for response from mapMessageConversionTopicTestsString");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Exception(
            "ERROR: mapMessageConversionTopicTestsString failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMapMessageConversionTopicTestsBytes
   * 
   * @assertion_ids: JMS:SPEC:75.19; JMS:SPEC:75.20; JMS:JAVADOC:475;
   * JMS:JAVADOC:451; JMS:JAVADOC:795; JMS:JAVADOC:796; JMS:JAVADOC:797;
   * JMS:JAVADOC:798; JMS:JAVADOC:799; JMS:JAVADOC:800; JMS:JAVADOC:801;
   * JMS:JAVADOC:802; JMS:JAVADOC:803;
   * 
   * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
   * create a MapMessage -. use MapMessage method writeBytes to write a Bytes.
   * Verify the proper conversion support as in 3.11.3
   * 
   */
  public void mdbMapMessageConversionTopicTestsBytes() throws Fault {
    String testCase1 = "mapMessageConversionTopicTestsBytesCreate";
    String testCase2 = "mapMessageConversionTopicTestsBytes";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase mapMessageConversionTopicTestsBytesCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_TOPIC
      System.out.println(
          "client - Check for response from mapMessageConversionTopicTestsBytes");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Exception(
            "ERROR: mapMessageConversionTopicTestsBytes failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMapMessageConversionTopicTestsInvFormatString
   * 
   * @assertion_ids: JMS:SPEC:76;
   * 
   * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
   * create a MapMessage -. use MapMessage method setString to write a text
   * string of "mytest string". Verify NumberFormatException is thrown
   * 
   */
  public void mdbMapMessageConversionTopicTestsInvFormatString() throws Fault {
    String testCase1 = "mapMessageConversionTopicTestsInvFormatStringCreate";
    String testCase2 = "mapMessageConversionTopicTestsInvFormatString";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase mapMessageConversionTopicTestsInvFormatStringCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_TOPIC
      System.out.println(
          "client - Check for response from mapMessageConversionTopicTestsInvFormatString");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Exception(
            "ERROR: mapMessageConversionTopicTestsInvFormatString failed");
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
