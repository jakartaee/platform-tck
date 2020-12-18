/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.bb.mdb.interceptor.listener.descriptor;

import com.sun.ts.tests.ejb30.common.interceptor.AroundInvokeBase;
import com.sun.ts.tests.ejb30.common.interceptor.AroundInvokeTestMDBImpl;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import jakarta.ejb.MessageDriven;
import jakarta.ejb.MessageDrivenContext;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.Queue;
import jakarta.jms.QueueConnectionFactory;

@MessageDriven(name = "AroundInvokeBean", description = "A Simple MDB AroundInvokeBean", messageListenerInterface = MessageListener.class)

// activationConfig ={
// @ActivationConfigProperty(propertyName="destinationType",
// propertyValue="jakarta.jms.Queue")

// This bean must use cmt, since it uses setRollbackOnly
@TransactionManagement(TransactionManagementType.CONTAINER)

// @Interceptors({
// com.sun.ts.tests.ejb30.common.interceptor.InterceptorMDB1.class,
// com.sun.ts.tests.ejb30.common.interceptor.InterceptorMDB2.class
// })

public class AroundInvokeBean extends AroundInvokeBase {
  @Resource(name = "ejbContext")
  private MessageDrivenContext ejbContext;

  @Resource(name = "qFactory")
  private QueueConnectionFactory qFactory;

  @Resource(name = "replyQueue")
  private Queue replyQueue;

  public AroundInvokeBean() {
    super();
  }

  public void onMessage(Message msg) {
    AroundInvokeTestMDBImpl.ensureRollbackOnly(msg, getEJBContext());
  }

  @AroundInvoke
  public Object intercept(InvocationContext ctx) throws Exception {
    // this interceptor should be invoked last, unless overrid by deployment
    // descriptor.
    Object result = null;
    int orderInChain = 3;
    result = AroundInvokeTestMDBImpl.intercept2(ctx, orderInChain);
    return result;
  }

  @PostConstruct
  private void postConstruct() {

  }

  @PreDestroy
  private void preDestroy() {

  }

  // ============ abstract methods from super ==========================
  protected jakarta.ejb.EJBContext getEJBContext() {
    return this.ejbContext;
  }
}
