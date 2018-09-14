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
 *  $Id$
 */

package com.sun.ts.tests.ejb.ee.bb.localaccess.mdbtaccesstest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jms.common.*;
import com.sun.ts.tests.jms.commonee.*;

import java.util.*;
import javax.ejb.*;
import java.rmi.*;
import javax.jms.Topic;
import javax.jms.TopicPublisher;
import javax.jms.Message;
import javax.jms.QueueReceiver;
import com.sun.javatest.Status;

public class MDBClient extends Client {
  private String generateSQL;

  private Topic topic = null;

  private TopicPublisher tPub = null;

  private static final String myMessage = "mdb local access tests for ejb";

  private static final String testST = "java:comp/env/jms/MDB_TOPIC";

  public static void main(String[] args) {
    MDBClient theTests = new MDBClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.setup_props:
   * 
   * jms_timeout; user; password;generateSQL;
   * 
   * @class.testArgs: -ap tssql.stmt
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    try {
      super.setup(args, p);
      topic = (Topic) context.lookup(testST);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("Setup failed:", e);
    }
  }

  /* Run test */

  /*
   * @testName: test1
   * 
   * @assertion_ids: EJB:SPEC:2.3
   * 
   * @test_Strategy: Create a Message-Driven Bean for a Topic that accesses a
   * Local Entity Bean within the same EAR. Deploy the EAR on the J2EE server.
   * Verify local access from Message-Driven Bean to a local Entity Bean.(CMP)
   */

  public void test1() throws Fault {
    String TestCase = "local_access_from_mdb_topic_to_entity_bean_test1";
    logTrace("local_access_from_mdb_topic_to_entity_bean_test1");
    boolean pass = false;
    int TestNum = 1;
    try {
      pass = doTest(TestNum, TestCase);
    } catch (Exception e) {
      throw new Fault("test1 failed", e);
    }
    if (!pass)
      throw new Fault("test1 failed");
  }

  /*
   * @testName: test2
   * 
   * @assertion_ids: EJB:SPEC:2.1
   * 
   * @test_Strategy: Create a Message-Driven Bean for a Topic that accesses a
   * Local Session Bean within the same EAR. Deploy the EAR on the J2EE server.
   * Verify local access from Message-Driven Bean to a local Session Bean. (SL)
   */

  public void test2() throws Fault {
    String TestCase = "local_access_from_mdb_topic_to_session_bean_test2";
    boolean pass = false;
    int TestNum = 2;
    try {
      pass = doTest(TestNum, TestCase);
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception caught running test2", e);
    } finally {
      try {
        createTestMessage("remove_stateful_bean", 0);
        tPub.publish(msg);
      } catch (Exception ee) {
        TestUtil.logTrace("Exception caught removing SSF bean", ee);
      }
    }

    if (!pass)
      throw new Fault("test2 failed");
  }

  /*
   * @testName: test3
   * 
   * @assertion_ids: EJB:SPEC:2.2
   * 
   * @test_Strategy: Create a Message-Driven Bean for a Topic that accesses a
   * Local Session Bean within the same EAR. Deploy the EAR on the J2EE server.
   * Verify local access from Message-Driven Bean to a local Entity Bean (BMP).
   */

  public void test3() throws Fault {
    String TestCase = "local_access_from_mdb_topic_to_session_bean_test3";
    boolean pass = false;
    int TestNum = 3;
    try {
      pass = doTest(TestNum, TestCase);
    } catch (Exception e) {
      throw new Fault("test3 failed", e);
    }
    if (!pass)
      throw new Fault("test3 failed");
  }

  /*
   * @testName: test4
   * 
   * @assertion_ids: EJB:SPEC:2.1
   * 
   * @test_Strategy: Create a Message-Driven Bean for a Topic that accesses a
   * Local Session Bean within the same EAR. Deploy the EAR on the J2EE server.
   * Verify local access from Message-Driven Bean to a local Session Bean (SF).
   */

  public void test4() throws Fault {
    String TestCase = "local_access_from_mdb_topic_to_session_bean_test4";
    boolean pass = false;
    int TestNum = 4;
    try {
      pass = doTest(TestNum, TestCase);
    } catch (Exception e) {
      throw new Fault("test4 failed", e);
    }
    if (!pass)
      throw new Fault("test4 failed");
  }

  private boolean doTest(int TestNum, String TestCase) throws Exception {
    logTrace(TestCase);
    boolean result = false;
    // create and send a test message
    tPub = tSession.createPublisher(topic);
    // send the message
    createTestMessage(TestCase, TestNum);
    TestUtil.logTrace("MDBClient - sending msg to mdb");
    TestUtil.logTrace("MDBClient - ");
    tPub.publish(msg);

    // verify success from mdb
    if (!checkOnResponse(TestCase)) {
      throw new Exception("Error: Did not get expected response from mdb!");
    } else
      result = true;
    return result;
  }

  private void cleanTheQueue() {
    // make sure nothing is left in QUEUE_REPLY
    Message m = null;
    try {
      QueueReceiver rcvr = session.createReceiver(rcvrQueue);
      m = rcvr.receive(timeout);
      while (m != null) {
        m = rcvr.receive(timeout);
        TestUtil.logTrace("Cleaning a message in the topic");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logTrace("Error in cleanTheQueue");
    }
  }

}
