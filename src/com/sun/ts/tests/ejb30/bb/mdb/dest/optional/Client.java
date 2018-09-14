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

package com.sun.ts.tests.ejb30.bb.mdb.dest.optional;

import com.sun.javatest.Status;
import javax.annotation.Resource;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;

public class Client
    extends com.sun.ts.tests.ejb30.bb.mdb.dest.common.ClientBase {
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

  /*
   * @testName: test1
   * 
   * @assertion_ids: EJB:SPEC:778; EJB:SPEC:779; EJB:SPEC:780
   * 
   * @test_Strategy: test message destination related elements in deployment
   * descriptors: message-destination, message-destination-ref,
   * message-destination-link
   *
   * This test directory (optional) uses message-destination linking mechanism,
   * which is declared in ejb-jar.xml and application-client.xml and deployment
   * plans. Message destination type (queue or topic) is not specified. This
   * configuration should work since this piece of information, whether declared
   * in descriptor or annotation, is optional.
   */
}
