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

package com.sun.ts.tests.ejb30.bb.mdb.dest.jarwar;

import com.sun.javatest.Status;
import javax.annotation.Resource;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import java.net.URL;

public class Client
    extends com.sun.ts.tests.ejb30.bb.mdb.dest.common.ClientBase {

  @Resource(name = "sendQueue")
  private static Queue sendQueue;

  @Resource(name = "receiveQueue")
  private static Queue receiveQueue;

  @Resource(name = "queueConnectionFactory")
  private static QueueConnectionFactory queueConnectionFactory;

  @Resource(name = "url")
  private static URL url;

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
   * harness.log.port; webServerHost; webServerPort;
   */

  /*
   * @testName: test1
   * 
   * @assertion_ids: EJB:SPEC:778; EJB:SPEC:779; EJB:SPEC:780
   * 
   * @test_Strategy: test message destination related elements in deployment
   * descriptors: message-destination, message-destination-ref,
   * message-destination-link message-destination is declared in two.jar, and
   * must be accessible from one.jar, two.jar, and appclient-client.jar
   */

  /*
   * @testName: test2
   * 
   * @assertion_ids: EJB:SPEC:778; EJB:SPEC:779; EJB:SPEC:780
   * 
   * @test_Strategy:
   */
  public void test2() throws Fault {
    urlTest(url);
  }
}
