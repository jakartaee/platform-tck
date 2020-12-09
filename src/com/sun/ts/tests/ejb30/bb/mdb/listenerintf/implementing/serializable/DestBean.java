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

package com.sun.ts.tests.ejb30.bb.mdb.listenerintf.implementing.serializable;

import jakarta.annotation.Resource;
import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.EJBContext;
import jakarta.ejb.MessageDriven;
import jakarta.ejb.MessageDrivenBean;
import jakarta.ejb.MessageDrivenContext;
import jakarta.jms.MessageListener;

//This MDB implements jakarta.jms.MessageListener interface, so no need to
//use annotation element messageListenerInterface, nor descritpor element
//messaging-type
//It also implements java.io.Serializable, jakarta.ejb.MessageDrivenBean, 
//but it should not be considered
//when determining the messaging type.
//
@MessageDriven(name = "DestBean", description = "a simple MDB", activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue") })
public class DestBean
    extends com.sun.ts.tests.ejb30.bb.mdb.dest.common.DestBeanBase
    implements java.io.Serializable, MessageDrivenBean, MessageListener {
  private MessageDrivenContext messageDrivenContext;

  public DestBean() {
    super();
  }

  public EJBContext getEJBContext() {
    return messageDrivenContext;
  }

  // ================== business methods ====================================

  //////////////////////////////////////////////////////////////////////
  // methods from MessageDrivenBean
  //////////////////////////////////////////////////////////////////////
  @Resource(name = "messageDrivenContext")
  public void setMessageDrivenContext(MessageDrivenContext messageDrivenContext)
      throws jakarta.ejb.EJBException {
    this.messageDrivenContext = messageDrivenContext;
  }

  public void ejbRemove() throws jakarta.ejb.EJBException {
  }

}
