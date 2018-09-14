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

package com.sun.ts.tests.ejb30.bb.mdb.listenerintf.implementing.externalizable;

import javax.ejb.EJBContext;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.annotation.Resource;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import com.sun.ts.tests.ejb30.common.messaging.Constants;
import com.sun.ts.tests.ejb30.common.messaging.StatusReporter;
import javax.ejb.MessageDriven;
import javax.ejb.ActivationConfigProperty;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;

//This MDB implements javax.jms.MessageListener interface, so no need to
//use annotation element messageListenerInterface, nor descritpor element
//messaging-type
//It also implements java.io.Externalizable, and javax.ejb.MessageDrivenBean,
//but it should not be considered
//when determining the messaging type.
//
@MessageDriven(name = "DestBean", activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue") })
public class DestBean
    extends com.sun.ts.tests.ejb30.bb.mdb.dest.common.DestBeanBase
    implements java.io.Externalizable, MessageDrivenBean, MessageListener {
  private MessageDrivenContext messageDrivenContext;

  public DestBean() {
    super();
  }

  public EJBContext getEJBContext() {
    return messageDrivenContext;
  }

  //////////////////////////////////////////////////////////////////////
  // methods in Externalizable
  //////////////////////////////////////////////////////////////////////
  public void readExternal(java.io.ObjectInput in)
      throws java.io.IOException, ClassNotFoundException {
  }

  public void writeExternal(java.io.ObjectOutput out)
      throws java.io.IOException {
  }

  //////////////////////////////////////////////////////////////////////
  // methods from MessageDrivenBean
  //////////////////////////////////////////////////////////////////////
  @Resource(name = "messageDrivenContext")
  public void setMessageDrivenContext(MessageDrivenContext messageDrivenContext)
      throws javax.ejb.EJBException {
    this.messageDrivenContext = messageDrivenContext;
  }

  public void ejbRemove() throws javax.ejb.EJBException {
  }

}
