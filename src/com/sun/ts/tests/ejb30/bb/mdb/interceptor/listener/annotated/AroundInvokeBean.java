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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.interceptor.Interceptors;
import javax.annotation.Resource;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import com.sun.ts.tests.ejb30.common.interceptor.AroundInvokeBase;
import com.sun.ts.tests.ejb30.common.interceptor.AroundInvokeTestMDBImpl;
import javax.ejb.MessageDrivenContext;
import javax.ejb.MessageDriven;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.jms.Message;
import javax.jms.MessageListener;
import com.sun.ts.tests.ejb30.common.interceptor.InterceptorMDB1;
import com.sun.ts.tests.ejb30.common.interceptor.InterceptorMDB2;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;

@MessageDriven(name = "AroundInvokeBean", description = "a simple MDB AroundInvokeBean", messageListenerInterface = MessageListener.class, activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue") })

// This bean must use cmt, since it uses setRollbackOnly
@TransactionManagement(TransactionManagementType.CONTAINER)
@Interceptors({ com.sun.ts.tests.ejb30.common.interceptor.InterceptorMDB1.class,
    com.sun.ts.tests.ejb30.common.interceptor.InterceptorMDB2.class })

public class AroundInvokeBean extends AroundInvokeBase
    implements MessageListener {
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
  protected javax.ejb.EJBContext getEJBContext() {
    return this.ejbContext;
  }
}
