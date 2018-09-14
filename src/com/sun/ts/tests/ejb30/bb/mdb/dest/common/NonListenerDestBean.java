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

package com.sun.ts.tests.ejb30.bb.mdb.dest.common;

import javax.ejb.EJBContext;
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

//This MDB does not implement javax.jms.MessageListener interface, doesn't
//use annotation element messageListenerInterface, nor descritpor element
//messaging-type.
//the default is javax.jms.MessageListener
//destinationType is also annotated here, though it should also be optional.
//optional destinationType is tested in mdb/dest/optional and mdb/dest/optional2.
//
@MessageDriven(name = "NonListenerDestBean", activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue") })
public class NonListenerDestBean
    extends com.sun.ts.tests.ejb30.bb.mdb.dest.common.DestBeanBase {
  // implements MessageListener {
  @Resource(name = "mdc")
  private MessageDrivenContext mdc;

  public NonListenerDestBean() {
    super();
  }

  public EJBContext getEJBContext() {
    return this.mdc;
  }

  // ================== business methods ====================================

}
