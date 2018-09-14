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

package com.sun.ts.tests.ejb30.tx.mdb.bmt.annotated;

import javax.ejb.EJBContext;
import javax.ejb.MessageDrivenContext;
import javax.annotation.Resource;
import javax.ejb.MessageDriven;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.jms.MessageListener;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

//This MDB implements javax.jms.MessageListener interface, so no need to
//use annotation element messageListenerInterface, nor descritpor element
//messaging-type
@MessageDriven(name = "DestBean", activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue") })
@TransactionManagement(TransactionManagementType.BEAN)
public class DestBean
    extends com.sun.ts.tests.ejb30.bb.mdb.dest.common.DestBeanBase
    implements MessageListener {
  @Resource(name = "mdc")
  private MessageDrivenContext mdc;

  @Resource(name = "ut")
  private UserTransaction ut;

  public DestBean() {
    super();
  }

  public EJBContext getEJBContext() {
    return this.mdc;
  }

  public void onMessage(javax.jms.Message msg) {
    try {
      ut.begin();
      super.onMessage(msg);
      ut.rollback();
    } catch (NotSupportedException e) {
      throw new IllegalStateException(e);
    } catch (SystemException e) {
      throw new IllegalStateException(e);
    }
  }

}
