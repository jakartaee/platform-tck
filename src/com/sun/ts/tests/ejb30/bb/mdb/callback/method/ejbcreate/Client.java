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

package com.sun.ts.tests.ejb30.bb.mdb.callback.method.ejbcreate;

import com.sun.javatest.Status;
import com.sun.ts.tests.ejb30.common.callback.MDBClientBase;
import javax.annotation.Resource;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;

/**
 * A test client for callback methods. Note that since callback methods cannot
 * throw application exception, so we can only convey test result back to client
 * through the returned value.
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

  /*
   * @testName: isPostConstructCalledTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: ejbCreate() in MDB is treated as PostConstruct, even when
   * it's not annotated with @PostConstruct, or when the bean class does not
   * implement MessageDrivenBean interface. (ejb3 spec section 4.5.8, 4.3.10.2)
   * 
   * callback methods can be in the superclass of the bean class.
   */

  /*
   * @testName: isInjectionDoneTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: o using annotations: o CallbackListener o PostConstruct o
   * PreDestroy o Resource o verify dependency injection has occurred when
   * callback method is called
   */

}
