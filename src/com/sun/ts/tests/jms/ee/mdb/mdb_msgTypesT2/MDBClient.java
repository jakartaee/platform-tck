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
package com.sun.ts.tests.jms.ee.mdb.mdb_msgTypesT2;

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

  @EJB(name = "ejb/MDB_MSGT2_Test")
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
   * @testName: mdbMessageObjectCopyTopicTest
   *
   * @assertion_ids: JMS:SPEC:85; JMS:JAVADOC:291;
   *
   * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
   * create an object message. Write a StringBuffer to the message.. modify the
   * StringBuffer and send the msg, verify that it does not effect the msg
   */
  public void mdbMessageObjectCopyTopicTest() throws Fault {
    String testCase1 = "messageObjectCopyTopicTestCreate";
    String testCase2 = "messageObjectCopyTopicTest";
    try {
      // Have the EJB invoke the MDB
      System.out
          .println("client - run testcase messageObjectCopyTopicTestCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from messageObjectCopyTopicTest");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: messageObjectCopyTopicTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbStreamMessageConversionTopicTestsBoolean
   * 
   * @assertion_ids: JMS:SPEC:75.1; JMS:SPEC:75.2; JMS:JAVADOC:219;
   * JMS:JAVADOC:150; JMS:JAVADOC:128; JMS:JAVADOC:144; JMS:JAVADOC:723;
   * JMS:JAVADOC:726; JMS:JAVADOC:729; JMS:JAVADOC:732; JMS:JAVADOC:735;
   * JMS:JAVADOC:738; JMS:JAVADOC:741; JMS:JAVADOC:747;
   * 
   * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
   * create a StreamMessage -. use StreamMessage method writeBoolean to write a
   * boolean to the message. Verify the proper conversion support as in 3.11.3
   */

  public void mdbStreamMessageConversionTopicTestsBoolean() throws Fault {
    String testCase1 = "streamMessageConversionTopicTestsBooleanCreate";
    String testCase2 = "streamMessageConversionTopicTestsBoolean";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase streamMessageConversionTopicTestsBooleanCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from streamMessageConversionTopicTestsBoolean");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault(
            "ERROR: streamMessageConversionTopicTestsBoolean failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbStreamMessageConversionTopicTestsByte
   * 
   * @assertion_ids: JMS:SPEC:75.3; JMS:SPEC:75.4; JMS:JAVADOC:152;
   * JMS:JAVADOC:130; JMS:JAVADOC:132; JMS:JAVADOC:136; JMS:JAVADOC:138;
   * JMS:JAVADOC:144; JMS:JAVADOC:720; JMS:JAVADOC:729; JMS:JAVADOC:738;
   * JMS:JAVADOC:741; JMS:JAVADOC:747;
   * 
   * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
   * create a StreamMessage -. use StreamMessage method writeByte to write a
   * byte. Verify the proper conversion support as in 3.11.3
   * 
   */
  public void mdbStreamMessageConversionTopicTestsByte() throws Fault {
    String testCase1 = "streamMessageConversionTopicTestsByteCreate";
    String testCase2 = "streamMessageConversionTopicTestsByte";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase streamMessageConversionTopicTestsByteCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from streamMessageConversionTopicTestsByte");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: streamMessageConversionTopicTestsByte failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbStreamMessageConversionTopicTestsShort
   * 
   * @assertion_ids: JMS:SPEC:75.5; JMS:SPEC:75.6; JMS:JAVADOC:154;
   * JMS:JAVADOC:132; JMS:JAVADOC:136; JMS:JAVADOC:138; JMS:JAVADOC:144;
   * JMS:JAVADOC:720; JMS:JAVADOC:723; JMS:JAVADOC:729; JMS:JAVADOC:738;
   * JMS:JAVADOC:741; JMS:JAVADOC:747;
   * 
   * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
   * create a StreamMessage -. use StreamMessage method writeShort to write a
   * short. Verify the proper conversion support as in 3.11.3
   * 
   */
  public void mdbStreamMessageConversionTopicTestsShort() throws Fault {
    String testCase1 = "streamMessageConversionTopicTestsShortCreate";
    String testCase2 = "streamMessageConversionTopicTestsShort";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase streamMessageConversionTopicTestsShortCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from streamMessageConversionTopicTestsShort");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: streamMessageConversionTopicTestsShort failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbStreamMessageConversionTopicTestsInt
   * 
   * @assertion_ids: JMS:SPEC:75.9; JMS:SPEC:75.10; JMS:JAVADOC:158;
   * JMS:JAVADOC:136; JMS:JAVADOC:138; JMS:JAVADOC:144; JMS:JAVADOC:720;
   * JMS:JAVADOC:723; JMS:JAVADOC:725; JMS:JAVADOC:729; JMS:JAVADOC:738;
   * JMS:JAVADOC:741; JMS:JAVADOC:747;
   * 
   * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
   * create a StreamMessage -. use StreamMessage method writeInt to write an
   * int. Verify the proper conversion support as in 3.11.3
   * 
   */
  public void mdbStreamMessageConversionTopicTestsInt() throws Fault {
    String testCase1 = "streamMessageConversionTopicTestsIntCreate";
    String testCase2 = "streamMessageConversionTopicTestsInt";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase streamMessageConversionTopicTestsIntCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from streamMessageConversionTopicTestsInt");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: streamMessageConversionTopicTestsInt failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbStreamMessageConversionTopicTestsLong
   * 
   * @assertion_ids: JMS:SPEC:75.11; JMS:SPEC:75.12; JMS:JAVADOC:160;
   * JMS:JAVADOC:138; JMS:JAVADOC:144; JMS:JAVADOC:720; JMS:JAVADOC:723;
   * JMS:JAVADOC:725; JMS:JAVADOC:729; JMS:JAVADOC:732; JMS:JAVADOC:738;
   * JMS:JAVADOC:741; JMS:JAVADOC:747;
   * 
   * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
   * create a StreamMessage -. use StreamMessage method writeLong to write a
   * long. Verify the proper conversion support as in 3.11.3
   * 
   */
  public void mdbStreamMessageConversionTopicTestsLong() throws Fault {
    String testCase1 = "streamMessageConversionTopicTestsLongCreate";
    String testCase2 = "streamMessageConversionTopicTestsLong";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase streamMessageConversionTopicTestsLongCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from streamMessageConversionTopicTestsLong");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: streamMessageConversionTopicTestsLong failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbStreamMessageConversionTopicTestsFloat
   * 
   * @assertion_ids: JMS:SPEC:75.13; JMS:SPEC:75.14; JMS:JAVADOC:162;
   * JMS:JAVADOC:140; JMS:JAVADOC:144; JMS:JAVADOC:720; JMS:JAVADOC:723;
   * JMS:JAVADOC:725; JMS:JAVADOC:729; JMS:JAVADOC:732; JMS:JAVADOC:735;
   * JMS:JAVADOC:741; JMS:JAVADOC:747;
   * 
   * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
   * create a StreamMessage -. use StreamMessage method writeFloat to write a
   * float. Verify the proper conversion support as in 3.11.3
   * 
   */
  public void mdbStreamMessageConversionTopicTestsFloat() throws Fault {
    String testCase1 = "streamMessageConversionTopicTestsFloatCreate";
    String testCase2 = "streamMessageConversionTopicTestsFloat";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase streamMessageConversionTopicTestsFloatCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from streamMessageConversionTopicTestsFloat");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: streamMessageConversionTopicTestsFloat failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbStreamMessageConversionTopicTestsDouble
   * 
   * @assertion_ids: JMS:SPEC:75.15; JMS:SPEC:75.16; JMS:JAVADOC:164;
   * JMS:JAVADOC:142; JMS:JAVADOC:144; JMS:JAVADOC:720; JMS:JAVADOC:723;
   * JMS:JAVADOC:725; JMS:JAVADOC:729; JMS:JAVADOC:732; JMS:JAVADOC:735;
   * JMS:JAVADOC:738; JMS:JAVADOC:747;
   * 
   * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
   * create a StreamMessage -. use StreamMessage method writeDouble to write a
   * double. Verify the proper conversion support as in 3.11.3
   * 
   */
  public void mdbStreamMessageConversionTopicTestsDouble() throws Fault {
    String testCase1 = "streamMessageConversionTopicTestsDoubleCreate";
    String testCase2 = "streamMessageConversionTopicTestsDouble";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase streamMessageConversionTopicTestsDoubleCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from streamMessageConversionTopicTestsDouble");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault(
            "ERROR: streamMessageConversionTopicTestsDouble failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbStreamMessageConversionTopicTestsString
   * 
   * @assertion_ids: JMS:SPEC:75.17; JMS:SPEC:75.18; JMS:SPEC:77;
   * JMS:JAVADOC:166; JMS:JAVADOC:128; JMS:JAVADOC:130; JMS:JAVADOC:132;
   * JMS:JAVADOC:136; JMS:JAVADOC:138; JMS:JAVADOC:140; JMS:JAVADOC:142;
   * JMS:JAVADOC:144; JMS:JAVADOC:729; JMS:JAVADOC:747;
   * 
   * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
   * create a StreamMessage -. use StreamMessage method writeString to write a
   * string. Verify the proper conversion support as in 3.11.3
   * 
   */
  public void mdbStreamMessageConversionTopicTestsString() throws Fault {
    String testCase1 = "streamMessageConversionTopicTestsStringCreate";
    String testCase2 = "streamMessageConversionTopicTestsString";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase streamMessageConversionTopicTestsStringCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from streamMessageConversionTopicTestsString");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault(
            "ERROR: streamMessageConversionTopicTestsString failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbStreamMessageConversionTopicTestsChar
   * 
   * @assertion_ids: JMS:SPEC:75.7; JMS:SPEC:75.8; JMS:JAVADOC:156;
   * JMS:JAVADOC:134; JMS:JAVADOC:144; JMS:JAVADOC:720; JMS:JAVADOC:723;
   * JMS:JAVADOC:726; JMS:JAVADOC:732; JMS:JAVADOC:735; JMS:JAVADOC:738;
   * JMS:JAVADOC:741; JMS:JAVADOC:747;
   * 
   * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
   * create a StreamMessage -. use StreamMessage method writeChar to write a
   * char. Verify the proper conversion support as in 3.11.3
   * 
   */
  public void mdbStreamMessageConversionTopicTestsChar() throws Fault {
    String testCase1 = "streamMessageConversionTopicTestsCharCreate";
    String testCase2 = "streamMessageConversionTopicTestsChar";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase streamMessageConversionTopicTestsCharCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from streamMessageConversionTopicTestsChar");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: streamMessageConversionTopicTestsChar failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbStreamMessageConversionTopicTestsBytes
   * 
   * @assertion_ids: JMS:SPEC:75.19; JMS:SPEC:75.20; JMS:JAVADOC:168;
   * JMS:JAVADOC:146; JMS:JAVADOC:720; JMS:JAVADOC:723; JMS:JAVADOC:725;
   * JMS:JAVADOC:729; JMS:JAVADOC:732; JMS:JAVADOC:735; JMS:JAVADOC:738;
   * JMS:JAVADOC:741; JMS:JAVADOC:744;
   * 
   * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
   * create a StreamMessage -. use StreamMessage method writeBytes to write a
   * byte[] to the message. Verify the proper conversion support as in 3.11.3
   */
  public void mdbStreamMessageConversionTopicTestsBytes() throws Fault {
    String testCase1 = "streamMessageConversionTopicTestsBytesCreate";
    String testCase2 = "streamMessageConversionTopicTestsBytes";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase streamMessageConversionTopicTestsBytesCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from streamMessageConversionTopicTestsBytes");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: streamMessageConversionTopicTestsBytes failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbStreamMessageConversionTopicTestsInvFormatString
   * 
   * @assertion_ids: JMS:SPEC:76; JMS:SPEC:81; JMS:JAVADOC:166; JMS:JAVADOC:130;
   * JMS:JAVADOC:132; JMS:JAVADOC:136; JMS:JAVADOC:138; JMS:JAVADOC:140;
   * JMS:JAVADOC:142; JMS:JAVADOC:144; JMS:JAVADOC:146;
   * 
   * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
   * create a StreamMessage -. use StreamMessage method writeString to write a
   * text string of "mytest string". Verify NumberFormatException is thrown
   * Verify that the pointer was not incremented by doing a read string
   * 
   */
  public void mdbStreamMessageConversionTopicTestsInvFormatString()
      throws Fault {
    String testCase1 = "streamMessageConversionTopicTestsInvFormatStringCreate";
    String testCase2 = "streamMessageConversionTopicTestsInvFormatString";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase streamMessageConversionTopicTestsInvFormatStringCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from streamMessageConversionTopicTestsInvFormatString");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault(
            "ERROR: streamMessageConversionTopicTestsInvFormatString failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbStreamMessageTopicTestsFullMsg
   * 
   * @assertion_ids: JMS:SPEC:82; JMS:JAVADOC:150; JMS:JAVADOC:152;
   * JMS:JAVADOC:154; JMS:JAVADOC:156; JMS:JAVADOC:158; JMS:JAVADOC:160;
   * JMS:JAVADOC:162; JMS:JAVADOC:164; JMS:JAVADOC:166; JMS:JAVADOC:168;
   * JMS:JAVADOC:170; JMS:JAVADOC:172; JMS:JAVADOC:128; JMS:JAVADOC:130;
   * JMS:JAVADOC:132; JMS:JAVADOC:134; JMS:JAVADOC:136; JMS:JAVADOC:138;
   * JMS:JAVADOC:140; JMS:JAVADOC:142; JMS:JAVADOC:144; JMS:JAVADOC:146;
   * JMS:JAVADOC:148;
   * 
   * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
   * create a StreamMessage -. write one of each primitive type. Send the
   * message. Verify the data received was as sent.
   * 
   */
  public void mdbStreamMessageTopicTestsFullMsg() throws Fault {
    String testCase1 = "streamMessageTopicTestsFullMsgCreate";
    String testCase2 = "streamMessageTopicTestsFullMsg";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase streamMessageTopicTestsFullMsgCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from streamMessageTopicTestsFullMsg");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: streamMessageTopicTestsFullMsg failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbStreamMessageTopicTestNull
   * 
   * @assertion_ids: JMS:SPEC:78; JMS:SPEC:86; JMS:JAVADOC:144; JMS:JAVADOC:172;
   * 
   * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
   * create a StreamMessage -. Use writeString to write a null, then use
   * readString to read it back.
   */
  public void mdbStreamMessageTopicTestNull() throws Fault {
    String testCase1 = "streamMessageTopicTestNullCreate";
    String testCase2 = "streamMessageTopicTestNull";
    try {
      // Have the EJB invoke the MDB
      System.out
          .println("client - run testcase streamMessageTopicTestNullCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from streamMessageTopicTestNull");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: streamMessageTopicTestNull failed");
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
