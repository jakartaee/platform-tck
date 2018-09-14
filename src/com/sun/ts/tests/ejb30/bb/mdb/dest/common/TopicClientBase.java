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

package com.sun.ts.tests.ejb30.bb.mdb.dest.common;

import javax.annotation.Resource;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;

abstract public class TopicClientBase
    extends com.sun.ts.tests.ejb30.common.messaging.TopicClientBase
    implements com.sun.ts.tests.ejb30.common.messaging.Constants {

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
   * @assertion_ids: EJB:SPEC:778; EJB:SPEC:779; EJB:SPEC:780
   * 
   * @test_Strategy: test message destination related elements in deployment
   * descriptors: message-destination, message-destination-ref,
   * message-destination-link
   */
  public void test1() throws Fault {
    sendReceive();
  }
}
