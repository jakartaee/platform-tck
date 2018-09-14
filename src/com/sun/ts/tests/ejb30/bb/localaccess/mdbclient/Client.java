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

package com.sun.ts.tests.ejb30.bb.localaccess.mdbclient;

import java.util.Properties;
import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import javax.annotation.Resource;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;

public class Client extends com.sun.ts.tests.ejb30.common.messaging.ClientBase {
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
   * @class.setup_props: jms_timeout, timout; user, jms user; password,
   * password; harness.log.traceflag, log; harness.log.port, log;
   */
  public void setup(String[] args, Properties p) throws Fault {
    super.setup(args, p);
  }

  // cleanup should not be overridden. The super class cleans up queue.
  // public void cleanup() {
  //
  // }

  /////////////////////////////////////////////////////////////////////////
  // 1 localStateless
  /////////////////////////////////////////////////////////////////////////
  /*
   * @testName: passByReferenceTest1
   * 
   * @assertion_ids: EJB:JAVADOC:84
   * 
   * @test_Strategy:
   */
  public void passByReferenceTest1() throws Fault {
    sendReceive();
  }

  /*
   * @testName: exceptionTest1
   * 
   * @assertion_ids: EJB:JAVADOC:84
   * 
   * @test_Strategy:
   */
  public void exceptionTest1() throws Fault {
    sendReceive();
  }

  /*
   * @testName: runtimeExceptionTest1
   * 
   * @assertion_ids: EJB:JAVADOC:84; EJB:JAVADOC:87; EJB:JAVADOC:88
   * 
   * @test_Strategy:
   */
  public void runtimeExceptionTest1() throws Fault {
    sendReceive();
  }

  /////////////////////////////////////////////////////////////////////////
  // 2 defaultdefaultLocalStateless
  /////////////////////////////////////////////////////////////////////////
  /*
   * @testName: passByReferenceTest2
   * 
   * @assertion_ids: EJB:JAVADOC:84
   * 
   * @test_Strategy:
   */
  public void passByReferenceTest2() throws Fault {
    sendReceive();
  }

  /*
   * @testName: exceptionTest2
   * 
   * @assertion_ids: EJB:JAVADOC:84
   * 
   * @test_Strategy:
   */
  public void exceptionTest2() throws Fault {
    sendReceive();
  }

  /*
   * @testName: runtimeExceptionTest2
   * 
   * @assertion_ids: EJB:JAVADOC:84; EJB:JAVADOC:87; EJB:JAVADOC:88
   * 
   * @test_Strategy:
   */
  public void runtimeExceptionTest2() throws Fault {
    sendReceive();
  }

  /////////////////////////////////////////////////////////////////////////
  // 3 localStateful
  /////////////////////////////////////////////////////////////////////////
  /*
   * @testName: passByReferenceTest3
   * 
   * @assertion_ids: EJB:JAVADOC:84
   * 
   * @test_Strategy:
   */
  public void passByReferenceTest3() throws Fault {
    sendReceive();
  }

  /*
   * @testName: exceptionTest3
   * 
   * @assertion_ids: EJB:JAVADOC:84
   * 
   * @test_Strategy:
   */
  public void exceptionTest3() throws Fault {
    sendReceive();
  }

  /*
   * @testName: runtimeExceptionTest3
   * 
   * @assertion_ids: EJB:JAVADOC:84; EJB:JAVADOC:87; EJB:JAVADOC:88
   * 
   * @test_Strategy:
   */
  public void runtimeExceptionTest3() throws Fault {
    sendReceive();
  }

  /////////////////////////////////////////////////////////////////////////
  // 4 defaultdefaultLocalStateful
  /////////////////////////////////////////////////////////////////////////
  /*
   * @testName: passByReferenceTest4
   * 
   * @assertion_ids: EJB:JAVADOC:84
   * 
   * @test_Strategy:
   */
  public void passByReferenceTest4() throws Fault {
    sendReceive();
  }

  /*
   * @testName: exceptionTest4
   * 
   * @assertion_ids: EJB:JAVADOC:84
   * 
   * @test_Strategy:
   */
  public void exceptionTest4() throws Fault {
    sendReceive();
  }

  /*
   * @testName: runtimeExceptionTest4
   * 
   * @assertion_ids: EJB:JAVADOC:84; EJB:JAVADOC:87; EJB:JAVADOC:88
   * 
   * @test_Strategy:
   */
  public void runtimeExceptionTest4() throws Fault {
    sendReceive();
  }

  //////////////////////////////////////////////////////////////////////
  // localStateless2
  //////////////////////////////////////////////////////////////////////
  /*
   * @testName: passByReferenceTest5
   * 
   * @assertion_ids: EJB:JAVADOC:84
   * 
   * @test_Strategy:
   */
  public void passByReferenceTest5() throws Fault {
    sendReceive();
  }

  /*
   * @testName: exceptionTest5
   * 
   * @assertion_ids: EJB:JAVADOC:84
   * 
   * @test_Strategy:
   */
  public void exceptionTest5() throws Fault {
    sendReceive();
  }

  /*
   * @testName: runtimeExceptionTest5
   * 
   * @assertion_ids: EJB:JAVADOC:84; EJB:JAVADOC:87; EJB:JAVADOC:88
   * 
   * @test_Strategy:
   */
  public void runtimeExceptionTest5() throws Fault {
    sendReceive();
  }

}
