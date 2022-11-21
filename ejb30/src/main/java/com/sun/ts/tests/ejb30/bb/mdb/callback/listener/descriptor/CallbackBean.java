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

package com.sun.ts.tests.ejb30.bb.mdb.callback.listener.descriptor;

import com.sun.ts.tests.ejb30.common.callback.MDBCallbackBeanBase;

import jakarta.annotation.Resource;
import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.EJBContext;
import jakarta.ejb.MessageDriven;
import jakarta.ejb.MessageDrivenContext;
import jakarta.jms.MessageListener;
import jakarta.jms.Queue;
import jakarta.jms.QueueConnectionFactory;

@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue") })
// @Interceptors({MDBCallbackListener.class})
public class CallbackBean extends MDBCallbackBeanBase
    implements MessageListener {
  @Resource(name = "mdc")
  private MessageDrivenContext mdc;

  @Resource(name = "qFactory")
  private QueueConnectionFactory qFactory;

  @Resource(name = "replyQueue")
  private Queue replyQueue;

  public CallbackBean() {
    super();
  }

  public EJBContext getEJBContext() {
    return this.mdc;
  }

  // ================= callback methods ====================================

  // ================== business methods ====================================

}
