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

package com.sun.ts.tests.ejb30.bb.mdb.customlistener;

import javax.ejb.EJBContext;
import javax.ejb.MessageDrivenContext;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import com.sun.ts.tests.ejb30.common.callback.CallbackBeanBase;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import com.sun.ts.tests.ejb30.common.messaging.StatusReporter;
import com.sun.ts.tests.common.connector.util.TSMessageListenerInterface;
import javax.ejb.MessageDriven;
import javax.ejb.ActivationConfigProperty;
import javax.interceptor.Interceptors;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

/**
 * If a Message Listener Interface(MLI) is bundled into a standalone .rar file,
 * then that MLI should be accessible to MDB's that wishes to implement it but
 * not bundle it in the ejb jar file. (Bug id 6559421)
 *
 * This MDB is a EJB3.0 based Message Driven Bean, which uses custom
 * MessageListener TSMessageListenerInterface.
 *
 * The purpose of this MDB is to verify, that the custom Message Listener
 * interface is not required to be packaged along with ejb jar files, since the
 * custom Message Listener interface(TSMessageListenerInterface) is already
 * availale in the resource adapter, it should be available for the MDB.
 *
 * Note: This MDB makes use of the whitebox-tx resource adapter from connector
 * test area.
 */

@MessageDriven(name = "MDBean", messageListenerInterface = TSMessageListenerInterface.class, activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "java.lang.String") })
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MDBean extends CallbackBeanBase
    implements TSMessageListenerInterface {
  @Resource(name = "mdc")
  private MessageDrivenContext mdc;

  @Resource(name = "qFactory")
  private QueueConnectionFactory qFactory;

  @Resource(name = "replyQueue")
  private Queue replyQueue;

  private static boolean replySent = false;

  public MDBean() {
    super();
  }

  public EJBContext getEJBContext() {
    return this.mdc;
  }

  // This method should called during PostConstruct of this bean.
  @PostConstruct
  private void setValue() {
    this.setPostConstructCalled(true);
  }

  @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
  public void onMessage(String msg) {
    boolean status = false;
    String reason = null;
    String testname = "isPostConstructCalledTest";
    TLogger.log("**** MDBean onMessage called with message = " + msg + " ****");

    status = isPostConstructCalled();
    reason = "isPostConstructCalled() in onMessage returns: " + status;
    TLogger.log(reason);

    // Send only one reply (This is to filter out all the unwanted messages
    // from whitebox-tx resource adapter)
    if (!replySent) {
      StatusReporter.report(testname, status, reason,
          (QueueConnectionFactory) getEJBContext().lookup("qFactory"),
          (Queue) getEJBContext().lookup("replyQueue"));
      replySent = true;

    }
  }

}
