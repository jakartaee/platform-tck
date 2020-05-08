/*
 * Copyright (c) 2013, 2018, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jms.ee20.ra.activationconfig.topic.noselnocidautodurable.annotated;

import com.sun.ts.tests.jms.ee20.ra.activationconfig.common.ActivationConfigBeanBase;
import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.EJBContext;
import jakarta.ejb.MessageDrivenContext;
import javax.annotation.Resource;
import jakarta.ejb.MessageDriven;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.jms.MessageListener;

//This MDB implements jakarta.jms.MessageListener interface, so no need to
//use annotation element messageListenerInterface, nor descritpor element
//messaging-type
@MessageDriven(name = "ActivationConfigBean", activationConfig = {
    @ActivationConfigProperty(propertyName = "connectionFactoryLookup", propertyValue = "jms/QueueConnectionFactory"),
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "MY_TOPIC"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Topic"),
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"),
    @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "MySubscriptionName3ForRATests") })

@TransactionManagement(TransactionManagementType.BEAN)
public class ActivationConfigBean extends ActivationConfigBeanBase
    implements MessageListener {

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
