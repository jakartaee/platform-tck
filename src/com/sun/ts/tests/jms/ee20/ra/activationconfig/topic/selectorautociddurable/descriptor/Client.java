/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jms.ee20.ra.activationconfig.topic.selectorautociddurable.descriptor;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.*;
import javax.annotation.Resource;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;

public class Client extends
    com.sun.ts.tests.jms.ee20.ra.activationconfig.common.TopicClientBase {

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: jms_timeout; user; password; harness.log.traceflag;
   * harness.log.port;
   */

  /*
   * @testName: test1
   * 
   * @assertion_ids: JMS:SPEC:276; JMS:SPEC:276.1; JMS:SPEC:276.2;
   * JMS:SPEC:276.3; JMS:SPEC:276.4; JMS:SPEC:276.5; JMS:SPEC:276.6;
   * JMS:SPEC:276.7; JMS:SPEC:276.8; JMS:SPEC:276.9;
   * 
   * @test_Strategy: test activation-config related elements in deployment
   * descriptors, and their annotation counterparts.
   *
   * Sends message and waits for response. The message should reach the target
   * MDB, and a response should be received by this client.
   */
  public void test1() throws Fault {
    TestUtil.logMsg("Testing the following activationConfig properties");
    TestUtil.logMsg("  connectionFactoryLookup=jms/QueueConnectionFactory");
    TestUtil.logMsg("  destinationLookup=MDB_TOPIC");
    TestUtil.logMsg("  destinationType=javax.jms.Topic");
    TestUtil.logMsg("  acknowledgeMode=Auto-acknowledge");
    TestUtil.logMsg("  subscriptionDurability=Durable");
    TestUtil.logMsg("  subscriptionName=MySubscriptionName2ForRATests");
    TestUtil.logMsg("  clientId=MyClientId2ForRATests");
    TestUtil.logMsg(
        "  messageSelector=\"(COM_SUN_JMS_TESTNAME='test1' AND TestCaseNum NOT BETWEEN 1 AND 9999)\"");
    TestUtil.logMsg("Send message that MATCHES the message selector");
    TestUtil.logMsg("Set message property: COM_SUN_JMS_TESTNAME=test1");
    TestUtil.logMsg("Set message property: TestCaseNum=0");
    TestUtil
        .logMsg("Must receive message back since it MATCHES message selector");
    super.test1();
  }

  /*
   * @testName: negativeTest1
   * 
   * @assertion_ids: JMS:SPEC:276; JMS:SPEC:276.1; JMS:SPEC:276.2;
   * JMS:SPEC:276.3; JMS:SPEC:276.4; JMS:SPEC:276.5; JMS:SPEC:276.6;
   * JMS:SPEC:276.7; JMS:SPEC:276.8; JMS:SPEC:276.9;
   * 
   * @test_Strategy: test activation-config related elements in deployment
   * descriptors, and their annotation counterparts.
   *
   * Sends message and waits for response. The message should not reach the
   * target MDB, and no response should be received by this client.
   */
  public void negativeTest1() throws Fault {
    TestUtil.logMsg("Testing the following activationConfig properties");
    TestUtil.logMsg("  connectionFactoryLookup=jms/QueueConnectionFactory");
    TestUtil.logMsg("  destinationLookup=MDB_TOPIC");
    TestUtil.logMsg("  destinationType=javax.jms.Topic");
    TestUtil.logMsg("  acknowledgeMode=Auto-acknowledge");
    TestUtil.logMsg("  subscriptionDurability=Durable");
    TestUtil.logMsg("  subscriptionName=MySubscriptionName2ForRATests");
    TestUtil.logMsg("  clientId=MyClientId2ForRATests");
    TestUtil.logMsg(
        "  messageSelector=\"(COM_SUN_JMS_TESTNAME='test1' AND TestCaseNum NOT BETWEEN 1 AND 9999)\"");
    TestUtil.logMsg("Send message that DOES NOT MATCH the message selector");
    TestUtil.logMsg("Set message property: COM_SUN_JMS_TESTNAME=test1");
    TestUtil.logMsg("Set message property: TestCaseNum=1");
    TestUtil.logMsg(
        "Must not receive message back since it DOES NOT MATCH message selector");
    super.negativeTest1();
  }

  /*
   * @testName: negativeTest2
   * 
   * @assertion_ids: JMS:SPEC:276; JMS:SPEC:276.1; JMS:SPEC:276.2;
   * JMS:SPEC:276.3; JMS:SPEC:276.4; JMS:SPEC:276.5; JMS:SPEC:276.6;
   * JMS:SPEC:276.7; JMS:SPEC:276.8; JMS:SPEC:276.9;
   * 
   * @test_Strategy: test activation-config related elements in deployment
   * descriptors, and their annotation counterparts.
   *
   * Sends message and waits for response. The message should not reach the
   * target MDB, and no response should be received by this client.
   */
  public void negativeTest2() throws Fault {
    TestUtil.logMsg("Testing the following activationConfig properties");
    TestUtil.logMsg("  connectionFactoryLookup=jms/QueueConnectionFactory");
    TestUtil.logMsg("  destinationLookup=MDB_TOPIC");
    TestUtil.logMsg("  destinationType=javax.jms.Topic");
    TestUtil.logMsg("  acknowledgeMode=Auto-acknowledge");
    TestUtil.logMsg("  subscriptionDurability=Durable");
    TestUtil.logMsg("  subscriptionName=MySubscriptionName2ForRATests");
    TestUtil.logMsg("  clientId=MyClientId2ForRATests");
    TestUtil.logMsg(
        "  messageSelector=\"(COM_SUN_JMS_TESTNAME='test1' AND TestCaseNum NOT BETWEEN 1 AND 9999)\"");
    TestUtil.logMsg("Send message that DOES NOT MATCH the message selector");
    TestUtil.logMsg("Set message property: COM_SUN_JMS_TESTNAME=negativeTest2");
    TestUtil.logMsg("Set message property: TestCaseNum=0");
    TestUtil.logMsg(
        "Must not receive message back since it DOES NOT MATCH message selector");
    super.negativeTest2();
  }
}
