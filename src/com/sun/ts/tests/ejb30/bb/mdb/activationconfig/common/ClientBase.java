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

package com.sun.ts.tests.ejb30.bb.mdb.activationconfig.common;

import javax.annotation.Resource;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;

abstract public class ClientBase
    extends com.sun.ts.tests.ejb30.common.messaging.ClientBase
    implements com.sun.ts.tests.ejb30.common.messaging.Constants {
  @Resource(name = "sendQueue")
  private static Queue sendQueue;

  @Resource(name = "receiveQueue")
  private static Queue receiveQueue;

  @Resource(name = "queueConnectionFactory")
  private static QueueConnectionFactory queueConnectionFactory;

  protected void initSendQueue() {
    setSendQueue(sendQueue);
  }

  protected void initReceiveQueue() {
    setReceiveQueue(receiveQueue);
  }

  protected void initQueueConnectionFactory() {
    setQueueConnectionFactory(queueConnectionFactory);
  }

  /*
   * testName: test1
   * 
   * @assertion_ids: EJB:SPEC:520; EJB:SPEC:521; EJB:SPEC:524
   * 
   * @test_Strategy: test activation-config related elements in deployment
   * descriptors, and their annotation counterparts.
   */
  public void test1() throws Fault {
    sendReceive();
  }

  /*
   * testName: negativeTest1
   * 
   * @assertion_ids: EJB:SPEC:520; EJB:SPEC:521; EJB:SPEC:524
   * 
   * @test_Strategy: test activation-config related elements in deployment
   * descriptors, and their annotation counterparts.
   */
  public void negativeTest1() throws Fault {
    // the next messages should be filtered out by the ActivationConfigBean
    sendReceiveNegative("test1", 1);
  }

  /*
   * testName: negativeTest2
   * 
   * @assertion_ids: EJB:SPEC:520; EJB:SPEC:521; EJB:SPEC:524
   * 
   * @test_Strategy: test activation-config related elements in deployment
   * descriptors, and their annotation counterparts.
   */
  public void negativeTest2() throws Fault {
    // the next messages should be filtered out by the ActivationConfigBean
    sendReceiveNegative("negativeTest2", 0);
  }
}
