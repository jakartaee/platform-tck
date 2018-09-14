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

package com.sun.ts.tests.ejb30.common.callback;

import com.sun.ts.tests.ejb30.common.helper.TLogger;
import com.sun.ts.tests.ejb30.common.messaging.StatusReporter;
import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;

abstract public class MDBCallbackBeanBase extends CallbackBeanBase
    implements MessageListener {
  // @Resource(name="qFactory")
  // private QueueConnectionFactory qFactory;
  //
  // @Resource(name="replyQueue")
  // private Queue replyQueue;

  // ================== business methods ====================================
  public void onMessage(javax.jms.Message msg) {
    boolean status = false;
    String reason = null;
    String testname = null;
    try {
      testname = msg.getStringProperty(
          com.sun.ts.tests.ejb30.common.messaging.Constants.TEST_NAME_KEY);
    } catch (javax.jms.JMSException e) {
      status = false;
      reason = "Failed to get test name from message: " + msg;
      TLogger.log(reason);
      StatusReporter.report(testname, status, reason,
          (QueueConnectionFactory) getEJBContext().lookup("qFactory"),
          (Queue) getEJBContext().lookup("replyQueue"));
      return;
    }
    if (testname.equals(isInjectionDoneTest)) {
      status = isInjectionDone();
      reason = "isInjectionDone() in onMessage returns: " + status;
    } else if (testname.equals(isPostConstructCalledTest)) {
      status = isPostConstructCalled();
      reason = "isPostConstructCalled() in onMessage returns: " + status;
    } else if (testname.equals(isPostConstructOrPreDestroyCalledTest)) {
      status = isPostConstructCalled() || isPreDestroyCalled();
      reason = "isPostConstructCalled() or isPreDestroyCalled() in onMessage returns: "
          + status;
    } else {
      status = false;
      reason = "Unrecognized test: " + testname;
    }
    TLogger.log(reason);
    StatusReporter.report(testname, status, reason,
        (QueueConnectionFactory) getEJBContext().lookup("qFactory"),
        (Queue) getEJBContext().lookup("replyQueue"));
  }
}
