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

package com.sun.ts.tests.ejb30.bb.mdb.activationconfig.topic.selectordupsnondurable.override;

import com.sun.ts.tests.ejb30.bb.mdb.activationconfig.common.ActivationConfigBeanBase;

import jakarta.annotation.Resource;
import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.EJBContext;
import jakarta.ejb.MessageDriven;
import jakarta.ejb.MessageDrivenContext;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;

//This MDB does not implement jakarta.jms.MessageListener interface.  It's 
//specified in ejb-jar.xml
//
//some of the annotated data are overridden by ejb-jar.xml
//
@MessageDriven(name = "ActivationConfigBean", activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue"),
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Dups-ok-acknowledge"),
    @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"),
    @ActivationConfigProperty(propertyName = "messageSelector", propertyValue = "COM_SUN_JMS_TESTNAME='test1' OR TestCaseNum NOT BETWEEN 1 AND 9999") })

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
