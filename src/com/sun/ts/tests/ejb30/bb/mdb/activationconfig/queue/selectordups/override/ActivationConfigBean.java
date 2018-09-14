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

package com.sun.ts.tests.ejb30.bb.mdb.activationconfig.queue.selectordups.override;

import com.sun.ts.tests.ejb30.bb.mdb.activationconfig.common.ActivationConfigBeanBase;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJBContext;
import javax.ejb.MessageDrivenContext;
import javax.annotation.Resource;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.jms.MessageListener;

//some metadata are overridden by ejb-jar.xml
//this bean doesn't implement MessageListener, and ejb-jar.xml does not specify
//messaging-type.  It's annotated.
@MessageDriven(name = "ActivationConfigBean", messageListenerInterface = MessageListener.class, activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "messageSelector", propertyValue = "COM_SUN_JMS_TESTNAME<>'test1' OR TestCaseNum >= 1") })

// assembler not permitted to override transaction management type.
@TransactionManagement(TransactionManagementType.BEAN)

public class ActivationConfigBean extends ActivationConfigBeanBase {
  @Resource(name = "mdc")
  private MessageDrivenContext mdc;

  public ActivationConfigBean() {
    super();
  }

  public EJBContext getEJBContext() {
    return this.mdc;
  }

  // ================== business methods ====================================

}
