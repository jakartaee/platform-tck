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

package com.sun.ts.tests.ejb30.bb.mdb.interceptor.listener.annotated;

import com.sun.javatest.Status;
import javax.annotation.Resource;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;

public class Client
    extends com.sun.ts.tests.ejb30.common.interceptor.MDBClientBase {
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
   * @testName: getBeanTest
   * 
   * @assertion_ids: EJB:JAVADOC:128; EJB:JAVADOC:131
   * 
   * @test_Strategy: o using @AroundInvoke annotation in bean class o test
   * InvocationContext methods o interceptor method can throw exceptions
   */

  /*
   * @testName: getParametersTest
   * 
   * @assertion_ids: EJB:JAVADOC:128; EJB:JAVADOC:131
   * 
   * @test_Strategy: o using @AroundInvoke annotation in bean class o test
   * InvocationContext methods o interceptor method can throw exceptions
   */

  /*
   * @testName: setParametersTest
   * 
   * @assertion_ids: EJB:JAVADOC:128; EJB:JAVADOC:131
   * 
   * @test_Strategy: o using @AroundInvoke annotation in bean class o test
   * InvocationContext methods o interceptor method can throw exceptions
   */

  /*
   * @testName: getContextDataTest
   * 
   * @assertion_ids: EJB:JAVADOC:128; EJB:JAVADOC:131
   * 
   * @test_Strategy: o using @AroundInvoke annotation in bean class o test
   * InvocationContext methods o interceptor method can throw exceptions
   */

  /*
   * @testName: getMethodTest
   * 
   * @assertion_ids: EJB:JAVADOC:128; EJB:JAVADOC:131
   * 
   * @test_Strategy: o using @AroundInvoke annotation in bean class o test
   * InvocationContext methods o interceptor method can throw exceptions
   */

  ///////////////////////////////////////////////////////////////////////////
  // orderTest and sameInvocationContextTest can only be tested when multiple
  // AroundInvoke methods are used. Since bean class can only have one
  // AroundInvoke method, do not inherite the two tests if you are only using
  // AroundInvoke method in bean class.
  ///////////////////////////////////////////////////////////////////////////
  /*
   * @testName: orderTest
   * 
   * @assertion_ids: EJB:JAVADOC:128; EJB:JAVADOC:131
   * 
   * @test_Strategy: o the order of interceptors
   */

  /*
   * @testName: sameInvocationContextTest
   * 
   * @assertion_ids: EJB:JAVADOC:128; EJB:JAVADOC:131
   * 
   * @test_Strategy: o the same instance of InvocationContext is passed to all
   * interceptors
   */
}
