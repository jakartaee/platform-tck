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
package com.sun.ts.tests.jms.ee.mdb.mdb_msgTypesQ1;

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

  @EJB(name = "ejb/MDB_MSGQ1_Test")
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
   * @testName: mdbBytesMsgNullStreamQTest
   *
   * @assertion_ids: JMS:SPEC:86.1; JMS:JAVADOC:714;
   *
   * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
   * create a byte message. Use writeObject to write a null. verify a
   * java.lang.NullPointerException is thrown.
   * 
   *
   */
  public void mdbBytesMsgNullStreamQTest() throws Fault {
    String testCase = "bytesMsgNullStreamQTest";
    try {
      // Have the EJB invoke the MDB
      TestUtil.logTrace(
          "Call bean - have it tell mdb to run bytesMsgNullStreamQTest");
      hr.askMDBToRunATest(testCase);
      if (!hr.checkOnResponse(testCase)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: mdbBytesMsgNullStreamQTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbBytesMessageQTestsFullMsg
   * 
   * @assertion_ids: JMS:JAVADOC:560; JMS:JAVADOC:562; JMS:JAVADOC:564;
   * JMS:JAVADOC:566; JMS:JAVADOC:568; JMS:JAVADOC:570; JMS:JAVADOC:572;
   * JMS:JAVADOC:574; JMS:JAVADOC:576; JMS:JAVADOC:578; JMS:JAVADOC:580;
   * JMS:JAVADOC:582; JMS:JAVADOC:534; JMS:JAVADOC:536; JMS:JAVADOC:540;
   * JMS:JAVADOC:544; JMS:JAVADOC:546; JMS:JAVADOC:548; JMS:JAVADOC:550;
   * JMS:JAVADOC:552; JMS:JAVADOC:554; JMS:JAVADOC:556; JMS:JAVADOC:558;
   * JMS:JAVADOC:538; JMS:JAVADOC:542; JMS:JAVADOC:532;
   * 
   * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
   * create a BytesMessage -. write to the message using each type of method and
   * as an object. Send the message. Verify the data received was as sent.
   * 
   */

  public void mdbBytesMessageQTestsFullMsg() throws Fault {
    String testCase1 = "bytesMessageQTestsFullMsgCreate";
    String testCase2 = "bytesMessageQTestsFullMsg";
    try {
      // Have the EJB invoke the MDB
      System.out
          .println("client - run testcase bytesMessageQTestsFullMsgCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      // System.out.println("client - run testcase2 bytesMessageQTestsFullMsg")
      // hr.askMDBToRunATest(testCase2) ; // read and verify message sent
      System.out.println(
          "client - Check for response from bytesMessageQTestsFullMsg");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: bytesMessageQTestsFullMsg failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMapMessageFullMsgQTest
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
  public void mdbMapMessageFullMsgQTest() throws Fault {
    String testCase1 = "mapMessageFullMsgQTestCreate";
    String testCase2 = "mapMessageFullMsgQTest";
    try {
      // Have the EJB invoke the MDB
      System.out.println("client - run testcase mapMessageFullMsgQTestCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out
          .println("client - Check for response from mapMessageFullMsgQTest");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Exception("ERROR: mapMessageFullMsgQTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMapMessageConversionQTestsBoolean
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

  public void mdbMapMessageConversionQTestsBoolean() throws Fault {
    String testCase1 = "mapMessageConversionQTestsBooleanCreate";
    String testCase2 = "mapMessageConversionQTestsBoolean";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase mapMessageConversionQTestsBooleanCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from mapMessageConversionQTestsBoolean");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Exception("ERROR: mapMessageConversionQTestsBoolean failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMapMessageConversionQTestsByte
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
  public void mdbMapMessageConversionQTestsByte() throws Fault {
    String testCase1 = "mapMessageConversionQTestsByteCreate";
    String testCase2 = "mapMessageConversionQTestsByte";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase mapMessageConversionQTestsByteCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from mapMessageConversionQTestsByte");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Exception("ERROR: mapMessageConversionQTestsByte failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMapMessageConversionQTestsShort
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
  public void mdbMapMessageConversionQTestsShort() throws Fault {
    String testCase1 = "mapMessageConversionQTestsShortCreate";
    String testCase2 = "mapMessageConversionQTestsShort";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase mapMessageConversionQTestsShortCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from mapMessageConversionQTestsShort");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Exception("ERROR: mapMessageConversionQTestsShort failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMapMessageConversionQTestsChar
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
  public void mdbMapMessageConversionQTestsChar() throws Fault {
    String testCase1 = "mapMessageConversionQTestsCharCreate";
    String testCase2 = "mapMessageConversionQTestsChar";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase mapMessageConversionQTestsCharCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from mapMessageConversionQTestsChar");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Exception("ERROR: mapMessageConversionQTestsChar failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMapMessageConversionQTestsInt
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
  public void mdbMapMessageConversionQTestsInt() throws Fault {
    String testCase1 = "mapMessageConversionQTestsIntCreate";
    String testCase2 = "mapMessageConversionQTestsInt";
    try {
      // Have the EJB invoke the MDB
      System.out
          .println("client - run testcase mapMessageConversionQTestsIntCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from mapMessageConversionQTestsInt");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Exception("ERROR: mapMessageConversionQTestsInt failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMapMessageConversionQTestsLong
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
  public void mdbMapMessageConversionQTestsLong() throws Fault {
    String testCase1 = "mapMessageConversionQTestsLongCreate";
    String testCase2 = "mapMessageConversionQTestsLong";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase mapMessageConversionQTestsLongCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from mapMessageConversionQTestsLong");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Exception("ERROR: mapMessageConversionQTestsLong failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMapMessageConversionQTestsFloat
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
  public void mdbMapMessageConversionQTestsFloat() throws Fault {
    String testCase1 = "mapMessageConversionQTestsFloatCreate";
    String testCase2 = "mapMessageConversionQTestsFloat";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase mapMessageConversionQTestsFloatCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from mapMessageConversionQTestsFloat");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Exception("ERROR: mapMessageConversionQTestsFloat failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMapMessageConversionQTestsDouble
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
  public void mdbMapMessageConversionQTestsDouble() throws Fault {
    String testCase1 = "mapMessageConversionQTestsDoubleCreate";
    String testCase2 = "mapMessageConversionQTestsDouble";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase mapMessageConversionQTestsDoubleCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from mapMessageConversionQTestsDouble");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Exception("ERROR: mapMessageConversionQTestsDouble failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMapMessageConversionQTestsString
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
  public void mdbMapMessageConversionQTestsString() throws Fault {
    String testCase1 = "mapMessageConversionQTestsStringCreate";
    String testCase2 = "mapMessageConversionQTestsString";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase mapMessageConversionQTestsStringCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from mapMessageConversionQTestsString");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Exception("ERROR: mapMessageConversionQTestsString failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMapMessageConversionQTestsBytes
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
  public void mdbMapMessageConversionQTestsBytes() throws Fault {
    String testCase1 = "mapMessageConversionQTestsBytesCreate";
    String testCase2 = "mapMessageConversionQTestsBytes";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase mapMessageConversionQTestsBytesCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from mapMessageConversionQTestsBytes");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Exception("ERROR: mapMessageConversionQTestsBytes failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMapMessageConversionQTestsInvFormatString
   * 
   * @assertion_ids: JMS:SPEC:76;
   * 
   * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
   * create a MapMessage -. use MapMessage method setString to write a text
   * string of "mytest string". Verify NumberFormatException is thrown
   * 
   */
  public void mdbMapMessageConversionQTestsInvFormatString() throws Fault {
    String testCase1 = "mapMessageConversionQTestsInvFormatStringCreate";
    String testCase2 = "mapMessageConversionQTestsInvFormatString";
    try {
      // Have the EJB invoke the MDB
      System.out.println(
          "client - run testcase mapMessageConversionQTestsInvFormatStringCreate");
      hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
      System.out.println(
          "client - Check for response from mapMessageConversionQTestsInvFormatString");
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Exception(
            "ERROR: mapMessageConversionQTestsInvFormatString failed");
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
