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
package com.sun.ts.tests.ejb30.timer.interceptor.business.mdb;

import java.util.Properties;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;

import com.sun.ts.tests.ejb30.timer.common.MessageSenderBean;

public class Client extends
    com.sun.ts.tests.ejb30.timer.interceptor.business.common.ClientBase {

  @Resource(name = "sendQueue")
  private Queue sendQueue;

  @Resource(name = "queueConnectionFactory")
  private QueueConnectionFactory queueConnectionFactory;

  @EJB(beanInterface = TestBean.class, beanName = "TestBean")
  private TestBean testBean;

  @Override
  public void setup(String[] args, Properties p) {
    super.setup(args, p);

    // instantiate a dummy instance to satisfy super.aroundInvokeMethods
    this.businessTimerBean = new BusinessTimerBean();

  }

  /*
   * @testName: aroundInvokeMethods
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: create a timer in all interceptor methods. Verify they
   * expire as expected.
   */
  @Override
  public void aroundInvokeMethods() {
    MessageSenderBean.sendMessage(queueConnectionFactory, sendQueue,
        getTestName(), 0);
    super.aroundInvokeMethods();
  }

  /*
   * @testName: messageFromSingletonBeanToMDB
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: This test is not necessarily related to timer. It verifies
   * that a Singleton bean can send a message to the queue bound to the MDB.
   */
  public void messageFromSingletonBeanToMDB() throws InterruptedException {
    testBean.messageFromSingletonBeanToMDB(getTestName());
    assertEquals("Check the reply message from BusinessTimerBean.",
        getTestName(), testBean.getReplyFromMDB());
  }
}
