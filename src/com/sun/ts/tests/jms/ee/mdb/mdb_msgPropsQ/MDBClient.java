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
package com.sun.ts.tests.jms.ee.mdb.mdb_msgPropsQ;

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

  @EJB(name = "ejb/MDB_MSGPropsQ_Test")
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

  /*
   * @testName: mdbMsgPropertiesQTest
   *
   * @assertion_ids: JMS:SPEC:20.1; JMS:SPEC:20.2; JMS:SPEC:20.3; JMS:SPEC:20.4;
   * JMS:SPEC:20.5; JMS:SPEC:20.6; JMS:SPEC:20.7; JMS:SPEC:20.8; JMS:SPEC:21;
   * JMS:SPEC:23; JMS:SPEC:24; JMS:SPEC:25; JMS:SPEC:26; JMS:SPEC:10;
   * JMS:SPEC:27; JMS:SPEC:28; JMS:SPEC:29; JMS:SPEC:31; JMS:SPEC:32;
   * JMS:JAVADOC:411; JMS:JAVADOC:413; JMS:JAVADOC:415; JMS:JAVADOC:417;
   * JMS:JAVADOC:419; JMS:JAVADOC:421; JMS:JAVADOC:423; JMS:JAVADOC:425;
   * JMS:JAVADOC:427; JMS:JAVADOC:409; JMS:JAVADOC:391; JMS:JAVADOC:393;
   * JMS:JAVADOC:395; JMS:JAVADOC:397; JMS:JAVADOC:399; JMS:JAVADOC:401;
   * JMS:JAVADOC:403; JMS:JAVADOC:405; JMS:JAVADOC:407; JMS:JAVADOC:500;
   * JMS:JAVADOC:516; JMS:JAVADOC:387;
   *
   * @test_Strategy: create a session bean. Have the session bean send a message
   * to the mdb. The mdb will create a test message and send the test message to
   * the Queue for which it is a message listener. It will then verify the
   * results. pass or fail results are sent to the MDB_REPLY_QUEUE The session
   * bean checks MDB_QUEUE_REPLY for the results. Specifics: set and read
   * properties for boolean, byte, short, int, long, float, double, and String.
   * Verify expected results set and read properties for Boolean, Byte, Short,
   * Int, Long, Float, Double, and String. Verify expected results.
   *
   * When a client receives a message it is in read-only mode. Send a message
   * and have the client attempt modify the properties. Verify that a
   * MessageNotWriteableException is thrown. Call setObject property with an
   * invalid object and verify that a MessageFormatException is thrown
   *
   * call property get methods( other than getStringProperty and
   * getObjectProperty) for non-existent properties and verify that a null
   * pointer exception is returned. call getStringProperty and getObjectProperty
   * for non-existent properties and verify that a null is returned.
   *
   * set object properties and verify the correct value is returned with the
   * getObjectProperty method.
   *
   * call the clearProperties method on the received message and verify that the
   * messages properties were deleted. Test that getObjectProperty returns a
   * null and the getShortProperty throws a null pointer exception. Verify that
   * after clearing properties, you will be able to set and get properties.
   *
   * After clearing the message properties, call getText and verify that the
   * message body has not been cleared.
   *
   * call getJMSXPropertyNames() and verify that the names of the required JMSX
   * properties for JMSXGroupID and JMSXGroupSeq are returned.
   */
  public void mdbMsgPropertiesQTest() throws Fault {
    String testCase1 = "msgPropertiesQTestCreate";
    String testCase2 = "msgPropertiesQTest";
    try {
      // Have the EJB invoke the MDB
      TestUtil.logTrace(
          "Call bean - have it tell mdb to run msgPropertiesQTestCreate");
      hr.askMDBToRunATest(testCase1);
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: msgPropertiesQTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbMsgPropertiesConversionQTest
   *
   * @assertion_ids: JMS:SPEC:22.1; JMS:SPEC:22.2; JMS:SPEC:22.3; JMS:SPEC:22.4;
   * JMS:SPEC:22.5; JMS:SPEC:22.6; JMS:SPEC:22.7; JMS:SPEC:22.8; JMS:SPEC:22.9;
   * JMS:SPEC:22.10; JMS:SPEC:22.11; JMS:SPEC:22.12; JMS:SPEC:22.13;
   * JMS:SPEC:22.14; JMS:SPEC:22.15; JMS:SPEC:22.16;
   *
   * @test_Strategy: create a session bean. have the session bean create a
   * message and send to mdb. THe mdb will create a message, set properties for
   * all of the primitive types, send that message to the Queue that it is a
   * listener for and then verify the conversion by getting the properties. pass
   * or fail results are then sent to the MDB_REPLY_QUEUE The session bean
   * checks MDB_QUEUE_REPLY for the results.
   */
  public void mdbMsgPropertiesConversionQTest() throws Fault {
    String testCase1 = "msgPropertiesConversionQTestCreate";
    String testCase2 = "msgPropertiesConversionQTest";
    try {
      // Have the EJB invoke the MDB
      TestUtil.logTrace(
          "Call bean - have it tell mdb to run msgPropertiesConversionQTestCreate");
      hr.askMDBToRunATest(testCase1);
      if (!hr.checkOnResponse(testCase2)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: msgPropertiesConversionQTest failed");
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
