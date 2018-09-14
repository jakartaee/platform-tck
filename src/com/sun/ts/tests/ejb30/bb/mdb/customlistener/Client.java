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

package com.sun.ts.tests.ejb30.bb.mdb.customlistener;

import com.sun.javatest.Status;
import com.sun.ts.tests.ejb30.common.callback.MDBClientBase;
import javax.annotation.Resource;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;

/**
 * If a Message Listener Interface(MLI) is bundled into a standalone .rar file,
 * then that MLI should be accessible to MDB's that wishes to implement it but
 * not bundle it in the ejb jar file. (Bug id 6559421)
 *
 * This MDB is a EJB3.0 based Message Driven Bean, which uses custom
 * MessageListener TSMessageListenerInterface.
 *
 * The purpose of this test is to verify, that the custom Message Listener
 * interface is not required to be packaged along with ejb jar files, since the
 * custom Message Listener interface(TSMessageListenerInterface) is already
 * availale in the resource adapter, it should be available for the MDB.
 *
 * The test client verifies that the annotated PostConstruct method is called
 * during the PostConstruct stage of the message driven bean(MDBean).
 *
 * Note: This MDB makes use of the whitebox-tx resource adapter from connector
 * test area.
 */
public class Client extends MDBClientBase {
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

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: jms_timeout; user; password; harness.log.traceflag;
   * harness.log.port;
   */
  public void setup(String[] args, java.util.Properties p)
      throws com.sun.ts.lib.harness.EETest.Fault {
    super.setup(args, p);
  }

  /*
   * @testName: isPostConstructCalledTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: The test client verifies that the annotated PostConstruct
   * method is called during the PostConstruct stage of the message driven
   * bean(MDBean).
   * 
   * This test also indirectly verifies the custom Message Listener interface is
   * not required to be packaged along with ejb jar files, since the custom
   * Message Listener interface(TSMessageListenerInterface) is already packaged
   * in the resource adapter, it should be available for the MDB.
   */

}
