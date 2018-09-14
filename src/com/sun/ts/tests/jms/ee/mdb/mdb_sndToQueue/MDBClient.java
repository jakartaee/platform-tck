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
package com.sun.ts.tests.jms.ee.mdb.mdb_sndToQueue;

import java.io.*;
import java.util.*;
import javax.ejb.EJB;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.javatest.Status;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.tests.jms.commonee.*;

/**
 * The MDBClient class invokes a test session bean, which will ask the message
 * driven bean to send a text, byte, map, stream, and object message to a queue
 */

public class MDBClient extends EETest {

  @EJB(name = "ejb/MDB_SNDToQueue_Test")
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
  // Tests mdb sending jms messages
  // Test with Queue
  // Test with Text,Stream,Byte,Map and Object messages
  //
  /*
   * @testName: mdbSendTextMsgToQueueTest
   *
   * @assertion_ids: JavaEE:SPEC:214; JMS:JAVADOC:270; JMS:JAVADOC:522;
   * JMS:JAVADOC:188; JMS:JAVADOC:221; JMS:JAVADOC:120; JMS:JAVADOC:425;
   * JMS:JAVADOC:198; JavaEE:SPEC:128;
   *
   * @test_Strategy: Instruct the mdb to send a text msg. Create a stateful
   * Session EJB Bean. Deploy it on the J2EE server. Have the EJB component send
   * a message to a Queue Destination. handled by a message-driven bean Tell the
   * mdb to send a text message with QueueSender. Verify that the text message
   * was sent.
   *
   */
  public void mdbSendTextMsgToQueueTest() throws Fault {
    String messageType = "TextMessage";
    String matchMe = "TextMessageFromMsgBean";
    try {
      // Have the EJB invoke the MDB
      TestUtil.logTrace("Call bean - have it tell mdb to send a text message;");
      hr.askMDBToSendAMessage(messageType);
      if (!hr.checkOnResponse(matchMe)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: mdbSendTextMsgToQueueTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbSendBytesMsgToQueueTest
   *
   * @assertion_ids: JavaEE:SPEC:214; JMS:JAVADOC:270; JMS:JAVADOC:522;
   * JMS:JAVADOC:188; JMS:JAVADOC:221; JMS:JAVADOC:120; JMS:JAVADOC:425;
   * JMS:JAVADOC:198; JMS:JAVADOC:209; JMS:JAVADOC:562; JavaEE:SPEC:128;
   *
   * @test_Strategy: Instruct the mdb to send a bytes msg. Create a stateful
   * Session EJB Bean. Deploy it on the J2EE server. Have the EJB component send
   * a message to a Queue Destination. handled by a message-driven bean Tell the
   * mdb to send a bytes message with QueueSender. Verify that a Bytes message
   * was sent
   *
   */
  public void mdbSendBytesMsgToQueueTest() throws Fault {
    String messageType = "BytesMessage";
    String matchMe = "BytesMessageFromMsgBean";
    try {
      // Have the EJB invoke the MDB
      TestUtil
          .logTrace("Call bean - have it tell mdb to send a Bytes message;");
      hr.askMDBToSendAMessage(messageType);
      if (!hr.checkOnResponse(matchMe)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: mdbSendBytesMsgToQueueTest failed");
      }

      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbSendMapMsgToQueueTest
   *
   * @assertion_ids: JavaEE:SPEC:214; JMS:JAVADOC:270; JMS:JAVADOC:522;
   * JMS:JAVADOC:188; JMS:JAVADOC:221; JMS:JAVADOC:120; JMS:JAVADOC:425;
   * JMS:JAVADOC:198; JMS:JAVADOC:211; JMS:JAVADOC:473; JavaEE:SPEC:128;
   *
   * @test_Strategy: Instruct the mdb to send a map msg. Create a stateful
   * Session EJB Bean. Deploy it on the J2EE server. Have the EJB component send
   * a message to a Queue Destination. handled by a message-driven bean Tell the
   * mdb to send a map message with QueueSender. Verify that a Map message was
   * sent
   *
   */
  public void mdbSendMapMsgToQueueTest() throws Fault {
    String matchMe = "MapMessageFromMsgBean";
    String messageType = "MapMessage";
    try {
      // Have the EJB invoke the MDB
      TestUtil.logTrace("Call bean - have it tell mdb to send a map message;");
      hr.askMDBToSendAMessage(messageType);
      if (!hr.checkOnResponse(matchMe)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: mdbSendMapMsgToQueueTest failed");
      }

      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }

  }

  /*
   * @testName: mdbSendStreamMsgToQueueTest
   *
   * @assertion_ids: JavaEE:SPEC:214; JMS:JAVADOC:270; JMS:JAVADOC:522;
   * JMS:JAVADOC:188; JMS:JAVADOC:221; JMS:JAVADOC:120; JMS:JAVADOC:425;
   * JMS:JAVADOC:198; JMS:JAVADOC:219; JMS:JAVADOC:166; JavaEE:SPEC:128;
   *
   * @test_Strategy: Instruct the mdb to send a stream msg. Create a stateful
   * Session EJB Bean. Deploy it on the J2EE server. Have the EJB component send
   * a message to a Queue Destination. handled by a message-driven bean Tell the
   * mdb to send a stream message with QueueSender. Verify that a Stream message
   * was sent
   *
   */
  public void mdbSendStreamMsgToQueueTest() throws Fault {
    String matchMe = "StreamMessageFromMsgBean";
    String messageType = "StreamMessage";
    try {
      // Have the EJB invoke the MDB
      TestUtil
          .logTrace("Call bean - have it tell mdb to send a stream message;");
      hr.askMDBToSendAMessage(messageType);
      if (!hr.checkOnResponse(matchMe)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: mdbSendStreamMsgToQueueTest failed");
      }

      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbSendObjectMsgToQueueTest
   *
   * @assertion_ids: JavaEE:SPEC:214; JMS:JAVADOC:270; JMS:JAVADOC:522;
   * JMS:JAVADOC:188; JMS:JAVADOC:221; JMS:JAVADOC:120; JMS:JAVADOC:425;
   * JMS:JAVADOC:198; JMS:JAVADOC:215; JMS:JAVADOC:289; JavaEE:SPEC:128;
   *
   * @test_Strategy: Instruct the mdb to send an object msg. Create a stateful
   * Session EJB Bean. Deploy it on the J2EE server. Have the EJB component send
   * a message to a Queue Destination. handled by a message-driven bean Tell the
   * mdb to send an Object message with QueueSender. Verify that an Object
   * message was sent
   *
   */
  public void mdbSendObjectMsgToQueueTest() throws Fault {
    String matchMe = "ObjectMessageFromMsgBean";
    String messageType = "ObjectMessage";
    try {
      // Have the EJB invoke the MDB
      TestUtil
          .logTrace("Call bean - have it tell mdb to send an object message;");
      hr.askMDBToSendAMessage(messageType);
      if (!hr.checkOnResponse(matchMe)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: mdbSendObjectMsgToQueueTest failed");
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
