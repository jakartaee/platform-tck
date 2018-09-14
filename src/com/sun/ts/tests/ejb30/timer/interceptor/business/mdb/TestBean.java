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

import javax.annotation.Resource;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.interceptor.ExcludeDefaultInterceptors;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;

import com.sun.ts.tests.ejb30.timer.common.MessageSenderBean;

@Singleton
@ExcludeDefaultInterceptors
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class TestBean {
  @Resource(name = "sendQueue")
  private Queue sendQueue;

  @Resource(name = "queueConnectionFactory")
  private QueueConnectionFactory queueConnectionFactory;

  // This field will be updated by BusinessTimerBean,
  // after which its value is retrieved by Client class (the caller
  // of this class.
  private String replyFromMDB;

  public void messageFromSingletonBeanToMDB(String testName) {
    MessageSenderBean.sendMessage(queueConnectionFactory, sendQueue, testName,
        0);
  }

  public String getReplyFromMDB() throws InterruptedException {
    synchronized (this) {
      while (replyFromMDB == null) {
        wait();
      }
    }
    return replyFromMDB;
  }

  public void setReplyFromMDB(String reply) {
    synchronized (this) {
      this.replyFromMDB = reply;
      notify();
    }
  }
}
