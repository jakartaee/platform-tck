/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jms.ee20.ra.activationconfig.topic.selectorautociddurable.descriptor;

import com.sun.ts.tests.jms.ee20.ra.activationconfig.common.ActivationConfigBeanBase;
import javax.ejb.EJBContext;
import javax.ejb.MessageDrivenContext;
import javax.annotation.Resource;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.jms.MessageListener;

//This MDB implements javax.jms.MessageListener interface, so no need to
//use annotation element messageListenerInterface, nor descritpor element
//messaging-type

//assembler not permitted to override transaction management type. 
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
