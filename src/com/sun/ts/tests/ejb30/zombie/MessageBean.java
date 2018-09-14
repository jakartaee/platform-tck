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

package com.sun.ts.tests.ejb30.zombie;

import javax.ejb.EJBContext;
import javax.ejb.MessageDrivenContext;
import javax.annotation.Resource;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import com.sun.ts.tests.ejb30.common.messaging.Constants;
import javax.ejb.MessageDriven;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import static com.sun.ts.tests.ejb30.common.messaging.Constants.TEST_NAME_KEY;
import static com.sun.ts.tests.ejb30.common.messaging.Constants.TEST_NUMBER_KEY;

//This MDB implements javax.jms.MessageListener interface, so no need to
//use annotation element messageListenerInterface, nor descritpor element
//messaging-type
@MessageDriven(name = "MessageBean", activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue") })
@TransactionManagement(TransactionManagementType.BEAN)
public class MessageBean implements MessageListener {
  @Resource(name = "mdc")
  private MessageDrivenContext mdc;

  public MessageBean() {
    super();
  }

  public EJBContext getEJBContext() {
    return this.mdc;
  }

  public void onMessage(javax.jms.Message msg) {
    String info = null;
    try {
      String testname = msg.getStringProperty(TEST_NAME_KEY);
      int testNumber = msg.getIntProperty(TEST_NUMBER_KEY);
      info = TEST_NAME_KEY + "=" + testname + ", " + TEST_NUMBER_KEY + "="
          + testNumber;
    } catch (javax.jms.JMSException e) {
      info = msg.toString();
    }
    TLogger.log(this + " consumed message " + info);
  }
}
