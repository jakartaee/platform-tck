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

package com.sun.ts.tests.jms.ee.mdb.mdb_rec;

import java.io.*;
import java.util.Properties;
import javax.ejb.EJB;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

/**
 * The MDBClient class invokes a test bean to send each type of 
 * jms message to a message driven bean for a queue and a topic 
 */
import com.sun.javatest.Status;

public class MDBClient extends EETest {

  @EJB(name = "ejb/MDB_AR_Test")
  private static MDB_AR_Test hr;

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
  // Tests mdb asynchronous receives
  // Test with Queue and Topic
  // Test with Text,Stream,Byte,Map and Object messages
  //

  /*
   * @testName: asynRecTextMsgQueueTest
   * 
   * @assertion_ids: EJB:SPEC:506; JMS:JAVADOC:270; JMS:JAVADOC:274;
   * JMS:JAVADOC:221; JMS:JAVADOC:188; JMS:JAVADOC:120; JMS:JAVADOC:425;
   * JMS:JAVADOC:198;
   *
   * @test_Strategy: Test with a Text message Create a stateful Session EJB
   * Bean. Deploy it on the J2EE server. Have the EJB component send a text
   * message to a Queue Destination. handled by a message-driven bean Verify
   * that the mdb received the message
   *
   *
   */
  public void asynRecTextMsgQueueTest() throws Fault {
    String matchMe;

    try {
      // Have the EJB invoke the MDB
      logMsg("Call bean - have it send mdb a message;");

      matchMe = "mdb_asynchRecFromQueue_Text_Msg_Test";
      hr.sendTextMessageToQ(matchMe);
      if (!hr.checkOnResponse(matchMe)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: test1 failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: asynRecMapMsgQueueTest
   * 
   * @assertion_ids: EJB:SPEC:506; JMS:JAVADOC:270; JMS:JAVADOC:274;
   * JMS:JAVADOC:221; JMS:JAVADOC:188; JMS:JAVADOC:120; JMS:JAVADOC:425;
   * JMS:JAVADOC:198; JMS:JAVADOC:211; JMS:JAVADOC:473;
   *
   * @test_Strategy: Test with a Map message Create a stateful Session EJB Bean.
   * Deploy it on the J2EE server. Have the EJB component send a text message to
   * a Queue Destination. handled by a message-driven bean Verify that the mdb
   * received the message
   *
   *
   */
  public void asynRecMapMsgQueueTest() throws Fault {
    String matchMe;

    try {
      // Have the EJB invoke the MDB
      logMsg("Call bean  - have it send mdb a message;");

      matchMe = "mdb_asynchRecFromQueue_MapMessage_Test";
      TestUtil.logTrace("Have bean send mdb a map message;");
      hr.sendMapMessageToQ(matchMe);
      if (!hr.checkOnResponse(matchMe)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: test3 failed");
      }

      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: asynRecObjectMsgQueueTest
   * 
   * @assertion_ids: EJB:SPEC:506; JMS:JAVADOC:270; JMS:JAVADOC:274;
   * JMS:JAVADOC:221; JMS:JAVADOC:188; JMS:JAVADOC:120; JMS:JAVADOC:425;
   * JMS:JAVADOC:198; JMS:JAVADOC:215; JMS:JAVADOC:289;
   *
   * @test_Strategy: Test with a Object message Create a stateful Session EJB
   * Bean. Deploy it on the J2EE server. Have the EJB component send a text
   * message to a Queue Destination. handled by a message-driven bean Verify
   * that the mdb received the message
   *
   *
   */
  public void asynRecObjectMsgQueueTest() throws Fault {
    String matchMe;

    try {
      // Have the EJB invoke the MDB
      logMsg("Call bean - have it send mdb a message;");

      matchMe = "mdb_asynchRecFromQueue_ObjectMessage_Test";
      TestUtil.logTrace("Have bean send mdb a object message;");
      hr.sendObjectMessageToQ(matchMe);
      if (!hr.checkOnResponse(matchMe)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: test5 failed");
      }

      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: asynRecTextMsgTopicTest
   * 
   * @assertion_ids: EJB:SPEC:506; JMS:JAVADOC:270; JMS:JAVADOC:274;
   * JMS:JAVADOC:221; JMS:JAVADOC:188; JMS:JAVADOC:120; JMS:JAVADOC:425;
   * JMS:JAVADOC:198; JMS:JAVADOC:109; JMS:JAVADOC:111; JMS:JAVADOC:91;
   * JMS:JAVADOC:99;
   *
   * @test_Strategy: Testing with a Text message Create a stateful Session EJB
   * Bean. Deploy it on the J2EE server. Have the EJB component send a Text
   * message to a Topic Destination. handled by a message-driven bean Verify
   * that the mdb received the message
   *
   *
   */
  public void asynRecTextMsgTopicTest() throws Fault {
    String matchMe = "mdb_asynchRecFromTopic_Text_Msg_Test";
    try {
      // Have the EJB invoke the MDB
      logMsg("Call bean  - have it send mdb a message;");
      hr.sendTextMessageToTopic(matchMe);
      if (!hr.checkOnResponse(matchMe)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: test6 failed");
      }
      logMsg("Test passed;");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: asynRecBytesMsgQueueTest
   * 
   * @assertion_ids: EJB:SPEC:506; JMS:JAVADOC:270; JMS:JAVADOC:274;
   * JMS:JAVADOC:221; JMS:JAVADOC:188; JMS:JAVADOC:120; JMS:JAVADOC:425;
   * JMS:JAVADOC:198; JMS:JAVADOC:209; JMS:JAVADOC:562;
   *
   * @test_Strategy: Test with a Bytes message Create a stateful Session EJB
   * Bean. Deploy it on the J2EE server. Have the EJB component send a text
   * message to a Queue Destination. handled by a message-driven bean Verify
   * that the mdb received the message
   *
   *
   */
  public void asynRecBytesMsgQueueTest() throws Fault {
    String matchMe;

    try {
      // Have the EJB invoke the MDB
      logMsg("Call bean  - have it send mdb a message;");

      matchMe = "mdb_asynchRecFromQueue_BytesMessage_Test";
      TestUtil.logTrace("Have bean send mdb a bytes message;");
      hr.sendBytesMessageToQ(matchMe);
      if (!hr.checkOnResponse(matchMe)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: test2 failed");
      }

      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: asynRecStreamMsgQueueTest
   * 
   * @assertion_ids: EJB:SPEC:506; JMS:JAVADOC:270; JMS:JAVADOC:274;
   * JMS:JAVADOC:221; JMS:JAVADOC:188; JMS:JAVADOC:120; JMS:JAVADOC:425;
   * JMS:JAVADOC:198; JMS:JAVADOC:219; JMS:JAVADOC:166;
   *
   * @test_Strategy: Test with a Stream message Create a stateful Session EJB
   * Bean. Deploy it on the J2EE server. Have the EJB component send a text
   * message to a Queue Destination. handled by a message-driven bean Verify
   * that the mdb received the message
   *
   *
   */
  public void asynRecStreamMsgQueueTest() throws Fault {
    String matchMe;

    try {
      // Have the EJB invoke the MDB
      logMsg("Call bean - have it send mdb a message;");

      matchMe = "mdb_asynchRecFromQueue_StreamMessage_Test";
      TestUtil.logTrace("Have bean send mdb a stream message;");
      hr.sendStreamMessageToQ(matchMe);
      if (!hr.checkOnResponse(matchMe)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: test4 failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: asynRecMapMsgTopicTest
   * 
   * @assertion_ids: EJB:SPEC:506; JMS:JAVADOC:270; JMS:JAVADOC:274;
   * JMS:JAVADOC:221; JMS:JAVADOC:188; JMS:JAVADOC:120; JMS:JAVADOC:425;
   * JMS:JAVADOC:198; JMS:JAVADOC:109; JMS:JAVADOC:111; JMS:JAVADOC:91;
   * JMS:JAVADOC:99; JMS:JAVADOC:211; JMS:JAVADOC:473;
   *
   * @test_Strategy: Testing with a Map message Create a stateful Session EJB
   * Bean. Deploy it on the J2EE server. Have the EJB component send a Map
   * message to a Topic Destination. handled by a message-driven bean Verify
   * that the mdb received the message
   *
   *
   */
  public void asynRecMapMsgTopicTest() throws Fault {
    String matchMe = "mdb_asynchRecFromTopic_Map_Msg_Test";
    try {
      // Have the EJB invoke the MDB
      logMsg("Call bean - have it send mdb a message;");
      hr.sendMapMessageToTopic(matchMe);
      if (!hr.checkOnResponse(matchMe)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: asynRecMapMsgTopicTest failed");
      }
      logMsg("Test passed;");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: asynRecObjectMsgTopicTest
   * 
   * @assertion_ids: EJB:SPEC:506; JMS:JAVADOC:270; JMS:JAVADOC:274;
   * JMS:JAVADOC:221; JMS:JAVADOC:188; JMS:JAVADOC:120; JMS:JAVADOC:425;
   * JMS:JAVADOC:198; JMS:JAVADOC:109; JMS:JAVADOC:111; JMS:JAVADOC:91;
   * JMS:JAVADOC:99; JMS:JAVADOC:215; JMS:JAVADOC:289;
   *
   * @test_Strategy: Testing with a Object message Create a stateful Session EJB
   * Bean. Deploy it on the J2EE server. Have the EJB component send a Object
   * message to a Topic Destination. handled by a message-driven bean Verify
   * that the mdb received the message
   *
   *
   */
  public void asynRecObjectMsgTopicTest() throws Fault {
    String matchMe = "mdb_asynchRecFromTopic_Object_Msg_Test";
    try {
      // Have the EJB invoke the MDB
      logMsg("Call bean - have it send mdb a message;");
      hr.sendObjectMessageToTopic(matchMe);
      if (!hr.checkOnResponse(matchMe)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: asynRecObjectMsgTopicTest failed");
      }
      logMsg("Test passed;");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: asynRecBytesMsgTopicTest
   * 
   * @assertion_ids: EJB:SPEC:506; JMS:JAVADOC:270; JMS:JAVADOC:274;
   * JMS:JAVADOC:221; JMS:JAVADOC:188; JMS:JAVADOC:120; JMS:JAVADOC:425;
   * JMS:JAVADOC:198; JMS:JAVADOC:109; JMS:JAVADOC:111; JMS:JAVADOC:91;
   * JMS:JAVADOC:99; JMS:JAVADOC:209; JMS:JAVADOC:562;
   *
   * @test_Strategy: Testing with a Bytes message Create a stateful Session EJB
   * Bean. Deploy it on the J2EE server. Have the EJB component send a Bytes
   * message to a Topic Destination. handled by a message-driven bean Verify
   * that the mdb received the message
   *
   *
   */
  public void asynRecBytesMsgTopicTest() throws Fault {
    String matchMe = "mdb_asynchRecFromTopic_Bytes_Msg_Test";
    try {
      // Have the EJB invoke the MDB
      logMsg("Call bean - have it send mdb a message;");
      hr.sendBytesMessageToTopic(matchMe);
      if (!hr.checkOnResponse(matchMe)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: asynRecBytesMsgTopicTest failed");
      }
      logMsg("Test passed;");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: asynRecStreamMsgTopicTest
   * 
   * @assertion_ids: EJB:SPEC:506; JMS:JAVADOC:270; JMS:JAVADOC:274;
   * JMS:JAVADOC:221; JMS:JAVADOC:188; JMS:JAVADOC:120; JMS:JAVADOC:425;
   * JMS:JAVADOC:198; JMS:JAVADOC:109; JMS:JAVADOC:111; JMS:JAVADOC:91;
   * JMS:JAVADOC:99; JMS:JAVADOC:219; JMS:JAVADOC:166;
   *
   * @test_Strategy: Testing with a Stream message Create a stateful Session EJB
   * Bean. Deploy it on the J2EE server. Have the EJB component send a Stream
   * message to a Topic Destination. handled by a message-driven bean Verify
   * that the mdb received the message
   *
   *
   */
  public void asynRecStreamMsgTopicTest() throws Fault {
    String matchMe = "mdb_asynchRecFromTopic_Stream_Msg_Test";
    try {
      // Have the EJB invoke the MDB
      logMsg("Call bean - have it send mdb a message;");
      hr.sendStreamMessageToTopic(matchMe);
      if (!hr.checkOnResponse(matchMe)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: asynRecStreamMsgTopicTest failed");
      }
      logMsg("Test passed;");
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
      logErr("Unexpected Exception cleaning up the Queue", e);
    } finally {
      TestUtil.logTrace("Closing all connections");
      try {
        hr.cleanup();
      } catch (Exception oh) {
        TestUtil.logErr("Fail: unexpected exception closing Connections", oh);
        TestUtil.printStackTrace(oh);
        throw new Fault("Fail");
      }
      TestUtil.logTrace("Removing EJBs");
      try {
        hr.remove();
      } catch (Exception oh) {
        TestUtil.logErr("Fail: unexpected exception removing EJB", oh);
        TestUtil.printStackTrace(oh);
        throw new Fault("Fail");
      }
    }
  }
}
