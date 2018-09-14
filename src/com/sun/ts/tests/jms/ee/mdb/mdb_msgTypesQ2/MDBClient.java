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
package com.sun.ts.tests.jms.ee.mdb.mdb_msgTypesQ2;

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

  @EJB(name = "ejb/MDB_MSGQ2_Test")
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

  //
  /*
   * @testName: mdbMessageObjectCopyQTest
   *
   * @assertion_ids: JMS:SPEC:85; JMS:JAVADOC:291;
   *
   * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
   * create an object message. Write a StringBuffer to the message.. modify the
   * StringBuffer and send the msg, verify that it does not effect the msg
   */
  public void mdbMessageObjectCopyQTest() throws Fault {
    String testCase1 = "messageObjectCopyQTestCreate";
    String testCase2 = "messageObjectCopyQTest";
    try {
      // Have the EJB invoke the MDB
      System.out.println("client - run testcase messageObjectCopyQTestCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out
          .println("client - Check for response from messageObjectCopyQTest");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: messageObjectCopyQTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbStreamMessageConversionQTestsBoolean
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

  public void mdbStreamMessageConversionQTestsBoolean() throws Fault {
    String testCase1 = "streamMessageConversionQTestsBooleanCreate";
    String testCase2 = "streamMessageConversionQTestsBoolean";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase streamMessageConversionQTestsBooleanCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from streamMessageConversionQTestsBoolean");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: streamMessageConversionQTestsBoolean failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbStreamMessageConversionQTestsByte
   * 
   * @assertion_ids: JMS:SPEC:75.3; JMS:SPEC:75.4; JMS:JAVADOC:152;
   * JMS:JAVADOC:130; JMS:JAVADOC:132; JMS:JAVADOC:136; JMS:JAVADOC:138;
   * JMS:JAVADOC:144; JMS:JAVADOC:720; JMS:JAVADOC:729; JMS:JAVADOC:738;
   * JMS:JAVADOC:741; JMS:JAVADOC:747;
   * 
   * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
   * create a StreamMessage -. use StreamMessage method writeByte to write a
   * byte. Verify the proper conversion support as in 3.11.3
   */
  public void mdbStreamMessageConversionQTestsByte() throws Fault {
    String testCase1 = "streamMessageConversionQTestsByteCreate";
    String testCase2 = "streamMessageConversionQTestsByte";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase streamMessageConversionQTestsByteCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from streamMessageConversionQTestsByte");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: streamMessageConversionQTestsByte failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbStreamMessageConversionQTestsShort
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
  public void mdbStreamMessageConversionQTestsShort() throws Fault {
    String testCase1 = "streamMessageConversionQTestsShortCreate";
    String testCase2 = "streamMessageConversionQTestsShort";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase streamMessageConversionQTestsShortCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from streamMessageConversionQTestsShort");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: streamMessageConversionQTestsShort failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbStreamMessageConversionQTestsInt
   * 
   * @assertion_ids: JMS:SPEC:75.9; JMS:SPEC:75.10; JMS:JAVADOC:158;
   * JMS:JAVADOC:136; JMS:JAVADOC:138; JMS:JAVADOC:144; JMS:JAVADOC:720;
   * JMS:JAVADOC:723; JMS:JAVADOC:726; JMS:JAVADOC:729; JMS:JAVADOC:738;
   * JMS:JAVADOC:741; JMS:JAVADOC:747;
   * 
   * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
   * create a StreamMessage -. use StreamMessage method writeInt to write an
   * int. Verify the proper conversion support as in 3.11.3
   */
  public void mdbStreamMessageConversionQTestsInt() throws Fault {
    String testCase1 = "streamMessageConversionQTestsIntCreate";
    String testCase2 = "streamMessageConversionQTestsInt";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase streamMessageConversionQTestsIntCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from streamMessageConversionQTestsInt");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: streamMessageConversionQTestsInt failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbStreamMessageConversionQTestsLong
   * 
   * @assertion_ids: JMS:SPEC:75.11; JMS:SPEC:75.12; JMS:JAVADOC:160;
   * JMS:JAVADOC:138; JMS:JAVADOC:144; JMS:JAVADOC:720; JMS:JAVADOC:723;
   * JMS:JAVADOC:726; JMS:JAVADOC:729; JMS:JAVADOC:732; JMS:JAVADOC:738;
   * JMS:JAVADOC:741; JMS:JAVADOC:747;
   * 
   * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
   * create a StreamMessage -. use StreamMessage method writeLong to write a
   * long. Verify the proper conversion support as in 3.11.3
   */
  public void mdbStreamMessageConversionQTestsLong() throws Fault {
    String testCase1 = "streamMessageConversionQTestsLongCreate";
    String testCase2 = "streamMessageConversionQTestsLong";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase streamMessageConversionQTestsLongCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from streamMessageConversionQTestsLong");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: streamMessageConversionQTestsLong failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbStreamMessageConversionQTestsFloat
   * 
   * @assertion_ids: JMS:SPEC:75.13; JMS:SPEC:75.14; JMS:JAVADOC:162;
   * JMS:JAVADOC:140; JMS:JAVADOC:144; JMS:JAVADOC:720; JMS:JAVADOC:723;
   * JMS:JAVADOC:726; JMS:JAVADOC:729; JMS:JAVADOC:732; JMS:JAVADOC:735;
   * JMS:JAVADOC:741; JMS:JAVADOC:747;
   * 
   * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
   * create a StreamMessage -. use StreamMessage method writeFloat to write a
   * float. Verify the proper conversion support as in 3.11.3
   * 
   */
  public void mdbStreamMessageConversionQTestsFloat() throws Fault {
    String testCase1 = "streamMessageConversionQTestsFloatCreate";
    String testCase2 = "streamMessageConversionQTestsFloat";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase streamMessageConversionQTestsFloatCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from streamMessageConversionQTestsFloat");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: streamMessageConversionQTestsFloat failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbStreamMessageConversionQTestsDouble
   * 
   * @assertion_ids: JMS:SPEC:75.15; JMS:SPEC:75.16; JMS:JAVADOC:164;
   * JMS:JAVADOC:142; JMS:JAVADOC:144; JMS:JAVADOC:720; JMS:JAVADOC:723;
   * JMS:JAVADOC:726; JMS:JAVADOC:729; JMS:JAVADOC:732; JMS:JAVADOC:735;
   * JMS:JAVADOC:738; JMS:JAVADOC:747;
   * 
   * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
   * create a StreamMessage -. use StreamMessage method writeDouble to write a
   * double. Verify the proper conversion support as in 3.11.3
   * 
   */
  public void mdbStreamMessageConversionQTestsDouble() throws Fault {
    String testCase1 = "streamMessageConversionQTestsDoubleCreate";
    String testCase2 = "streamMessageConversionQTestsDouble";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase streamMessageConversionQTestsDoubleCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from streamMessageConversionQTestsDouble");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: streamMessageConversionQTestsDouble failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbStreamMessageConversionQTestsString
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
  public void mdbStreamMessageConversionQTestsString() throws Fault {
    String testCase1 = "streamMessageConversionQTestsStringCreate";
    String testCase2 = "streamMessageConversionQTestsString";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase streamMessageConversionQTestsStringCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from streamMessageConversionQTestsString");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: streamMessageConversionQTestsString failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbStreamMessageConversionQTestsChar
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
  public void mdbStreamMessageConversionQTestsChar() throws Fault {
    String testCase1 = "streamMessageConversionQTestsCharCreate";
    String testCase2 = "streamMessageConversionQTestsChar";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase streamMessageConversionQTestsCharCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from streamMessageConversionQTestsChar");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: streamMessageConversionQTestsChar failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbStreamMessageConversionQTestsBytes
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
  public void mdbStreamMessageConversionQTestsBytes() throws Fault {
    String testCase1 = "streamMessageConversionQTestsBytesCreate";
    String testCase2 = "streamMessageConversionQTestsBytes";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase streamMessageConversionQTestsBytesCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from streamMessageConversionQTestsBytes");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: streamMessageConversionQTestsBytes failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbStreamMessageConversionQTestsInvFormatString
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
  public void mdbStreamMessageConversionQTestsInvFormatString() throws Fault {
    String testCase1 = "streamMessageConversionQTestsInvFormatStringCreate";
    String testCase2 = "streamMessageConversionQTestsInvFormatString";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase streamMessageConversionQTestsInvFormatStringCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from streamMessageConversionQTestsInvFormatString");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault(
            "ERROR: streamMessageConversionQTestsInvFormatString failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbStreamMessageQTestsFullMsg
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
   */
  public void mdbStreamMessageQTestsFullMsg() throws Fault {
    String testCase1 = "streamMessageQTestsFullMsgCreate";
    String testCase2 = "streamMessageQTestsFullMsg";
    try {
      // Have the EJB invoke the MDB
      System.out
          .println("client - run testcase streamMessageQTestsFullMsgCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from streamMessageQTestsFullMsg");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: streamMessageQTestsFullMsg failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbStreamMessageQTestNull
   * 
   * @assertion_ids: JMS:SPEC:78; JMS:SPEC:86; JMS:JAVADOC:144; JMS:JAVADOC:172;
   * 
   * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
   * create a StreamMessage -. Use writeString to write a null, then use
   * readString to read it back.
   */
  public void mdbStreamMessageQTestNull() throws Fault {
    String testCase1 = "streamMessageQTestNullCreate";
    String testCase2 = "streamMessageQTestNull";
    try {
      // Have the EJB invoke the MDB
      System.out.println("client - run testcase streamMessageQTestNullCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out
          .println("client - Check for response from streamMessageQTestNull");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: streamMessageQTestNull failed");
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
