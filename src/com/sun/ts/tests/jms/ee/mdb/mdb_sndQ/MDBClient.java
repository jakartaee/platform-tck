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
package com.sun.ts.tests.jms.ee.mdb.mdb_sndQ;

import java.io.*;
import java.util.*;
import javax.ejb.EJB;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.javatest.Status;

/**
 * The MDBClient class invokes a test session bean, which will send messages to
 * ask the message driven bean to send a text, byte, map, stream, and object
 * messages to a Destination
 */

public class MDBClient extends EETest {

  @EJB(name = "ejb/MDB_SNDQ_Test")
  private static MDB_SNDQ_Test hr = null;

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
   * @testName: mdbSendTextMsgTest
   *
   * @assertion_ids: JMS:JAVADOC:334; JMS:JAVADOC:122; JMS:JAVADOC:504;
   * JMS:JAVADOC:510; JMS:JAVADOC:242; JMS:JAVADOC:244; JMS:JAVADOC:221;
   * JMS:JAVADOC:317;
   *
   * @test_Strategy: Instruct the mdb to send a text msg.
   *
   * Create a stateful Session Bean and a Message-Diven Bean. Deploy them on the
   * J2EE server. Have the Session EJb send a message to a Queue Destination.
   * handled by a message-driven bean Tell the mdb to send a text message with
   * MessageProducer. Verify that the text message was sent.
   *
   */
  public void mdbSendTextMsgTest() throws Fault {
    String messageType = "TextMessage";
    String matchMe = "TextMessageFromMsgBean";
    try {
      // Have the EJB invoke the MDB
      TestUtil.logTrace("Call bean - have it tell mdb to send a text message;");
      hr.askMDBToSendAMessage(messageType);
      if (!hr.checkOnResponse(matchMe)) {
        TestUtil.logErr("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: mdbSendTextMsgTest failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbSendBytesMsgTest
   *
   * @assertion_ids: JMS:JAVADOC:334; JMS:JAVADOC:122; JMS:JAVADOC:504;
   * JMS:JAVADOC:510; JMS:JAVADOC:242; JMS:JAVADOC:244; JMS:JAVADOC:221;
   * JMS:JAVADOC:317;
   *
   * @test_Strategy: Instruct the mdb to send a bytes msg.
   *
   * Create a stateful Session Bean and a Message-Diven Bean. Deploy them on the
   * J2EE server. Have the Session EJb send a message to a Queue Destination.
   * handled by a message-driven bean Tell the mdb to send a BytesMessage with
   * MessageProducer. Verify that the BytesMessage was sent.
   *
   */
  public void mdbSendBytesMsgTest() throws Fault {
    String messageType = "BytesMessage";
    String matchMe = "BytesMessageFromMsgBean";
    try {
      // Have the EJB invoke the MDB
      TestUtil
          .logTrace("Call bean - have it tell mdb to send a Bytes message;");
      hr.askMDBToSendAMessage(messageType);
      if (!hr.checkOnResponse(matchMe)) {
        TestUtil.logErr("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: mdbSendBytesMsgTest failed");
      }

      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbSendMapMsgTest
   *
   * @assertion_ids: JMS:JAVADOC:334; JMS:JAVADOC:122; JMS:JAVADOC:504;
   * JMS:JAVADOC:510; JMS:JAVADOC:242; JMS:JAVADOC:244; JMS:JAVADOC:221;
   * JMS:JAVADOC:317;
   *
   * @test_Strategy: Instruct the mdb to send a MapMessage.
   *
   * Create a stateful Session Bean and a Message-Diven Bean. Deploy them on the
   * J2EE server. Have the Session EJB send a message to a Queue Destination.
   * handled by a message-driven bean Tell the mdb to send a MapMessage with
   * MessageProducer. Verify that the MapMessage was sent.
   *
   */
  public void mdbSendMapMsgTest() throws Fault {
    String matchMe = "MapMessageFromMsgBean";
    String messageType = "MapMessage";
    try {
      // Have the EJB invoke the MDB
      TestUtil.logTrace("Call bean - have it tell mdb to send a map message;");
      hr.askMDBToSendAMessage(messageType);
      if (!hr.checkOnResponse(matchMe)) {
        TestUtil.logErr("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: mdbSendMapMsgTest failed");
      }

      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbSendStreamMsgTest
   *
   * @assertion_ids: JMS:JAVADOC:334; JMS:JAVADOC:122; JMS:JAVADOC:504;
   * JMS:JAVADOC:510; JMS:JAVADOC:242; JMS:JAVADOC:244; JMS:JAVADOC:221;
   * JMS:JAVADOC:317;
   *
   * @test_Strategy: Instruct the mdb to send a StreamMessage.
   *
   * Create a stateful Session Bean and a Message-Diven Bean. Deploy them on the
   * J2EE server. Have the Session EJB send a message to a Queue Destination.
   * handled by a message-driven bean Tell the mdb to send a StreamMessage with
   * MessageProducer. Verify that the StreamMessage was sent.
   *
   */
  public void mdbSendStreamMsgTest() throws Fault {
    String matchMe = "StreamMessageFromMsgBean";
    String messageType = "StreamMessage";
    try {
      // Have the EJB invoke the MDB
      TestUtil
          .logTrace("Call bean - have it tell mdb to send a stream message;");
      hr.askMDBToSendAMessage(messageType);
      if (!hr.checkOnResponse(matchMe)) {
        TestUtil.logErr("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: mdbSendStreamMsgTest failed");
      }

      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: mdbSendObjectMsgTest
   *
   * @assertion_ids: JMS:JAVADOC:334; JMS:JAVADOC:122; JMS:JAVADOC:504;
   * JMS:JAVADOC:510; JMS:JAVADOC:242; JMS:JAVADOC:244; JMS:JAVADOC:221;
   * JMS:JAVADOC:317;
   *
   * @test_Strategy: Instruct the mdb to send an ObjectMessage.
   *
   * Create a stateful Session Bean and a Message-Diven Bean. Deploy them on the
   * J2EE server. Have the Session EJB send a message to a Queue Destination.
   * handled by a message-driven bean Tell the mdb to send an ObjectMessage with
   * MessageProducer. Verify that the ObjectMessage was sent.
   */
  public void mdbSendObjectMsgTest() throws Fault {
    String matchMe = "ObjectMessageFromMsgBean";
    String messageType = "ObjectMessage";
    try {
      // Have the EJB invoke the MDB
      TestUtil
          .logTrace("Call bean - have it tell mdb to send an object message;");
      hr.askMDBToSendAMessage(messageType);
      if (!hr.checkOnResponse(matchMe)) {
        TestUtil.logErr("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: mdbSendObjectMsgTest failed");
      }

      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /* cleanup -- none in this case */
  public void cleanup() throws Fault {

    if (hr != null) {
      try {
        if (hr.isThereSomethingInTheQueue()) {
          TestUtil.logTrace("Error: message(s) left in Q");
          hr.cleanTheQueue();
        } else {
          TestUtil.logTrace("Nothing left in queue");
        }
      } catch (Exception e) {
        TestUtil.logErr("Error cleaning up messages", e);
      } finally {
        try {
          hr.remove();
        } catch (Exception er) {
          TestUtil.logErr("Error removing bean", er);
        }
      }
    }
    logMsg("End  of client cleanup;");
  }
}
