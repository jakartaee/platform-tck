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

package com.sun.ts.tests.jms.ee20.ra.activationconfig.common;

import javax.annotation.Resource;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;

abstract public class TopicClientBase
    extends com.sun.ts.tests.jms.ee20.ra.common.messaging.TopicClientBase
    implements com.sun.ts.tests.jms.ee20.ra.common.messaging.Constants {

  @Resource(name = "sendTopic")
  private static Topic sendTopic;

  @Resource(name = "receiveQueue")
  private static Queue receiveQueue;

  @Resource(name = "topicConnectionFactory")
  private static TopicConnectionFactory topicConnectionFactory;

  @Resource(name = "queueConnectionFactory")
  private static QueueConnectionFactory queueConnectionFactory;

  protected void initSendTopic() {
    setSendTopic(sendTopic);
  }

  protected void initReceiveQueue() {
    setReceiveQueue(receiveQueue);
  }

  protected void initTopicConnectionFactory() {
    setTopicConnectionFactory(topicConnectionFactory);
  }

  protected void initQueueConnectionFactory() {
    setQueueConnectionFactory(queueConnectionFactory);
  }

  /*
   * testName: test1
   * 
   * @assertion_ids: JMS:SPEC:276; JMS:SPEC:276.1; JMS:SPEC:276.2;
   * JMS:SPEC:276.3; JMS:SPEC:276.4; JMS:SPEC:276.5; JMS:SPEC:276.6;
   * 
   * @test_Strategy: test activation-config related elements in deployment
   * descriptors, and their annotation counterparts.
   *
   * Sends message and waits for response. The message should reach the target
   * MDB, and a response should be received by this client.
   */
  public void test1() throws Fault {
    sendReceive("test1", 0);
  }

  /*
   * testName: negativeTest1
   * 
   * @assertion_ids: JMS:SPEC:276; JMS:SPEC:276.1; JMS:SPEC:276.2;
   * JMS:SPEC:276.3; JMS:SPEC:276.4; JMS:SPEC:276.5; JMS:SPEC:276.6;
   * 
   * @test_Strategy: test activation-config related elements in deployment
   * descriptors, and their annotation counterparts.
   *
   * Sends message and waits for response. The message should not reach the
   * target MDB, and no response should be received by this client.
   */
  public void negativeTest1() throws Fault {
    // the next messages should be filtered out by the ActivationConfigBean
    sendReceiveNegative("test1", 1);
  }

  /*
   * testName: negativeTest2
   * 
   * @assertion_ids: JMS:SPEC:276; JMS:SPEC:276.1; JMS:SPEC:276.2;
   * JMS:SPEC:276.3; JMS:SPEC:276.4; JMS:SPEC:276.5; JMS:SPEC:276.6;
   * 
   * @test_Strategy: test activation-config related elements in deployment
   * descriptors, and their annotation counterparts.
   *
   * Sends message and waits for response. The message should not reach the
   * target MDB, and no response should be received by this client.
   */
  public void negativeTest2() throws Fault {
    // the next messages should be filtered out by the ActivationConfigBean
    sendReceiveNegative("negativeTest2", 0);
  }
}
