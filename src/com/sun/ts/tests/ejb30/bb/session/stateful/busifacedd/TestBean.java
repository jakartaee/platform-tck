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

package com.sun.ts.tests.ejb30.bb.session.stateful.busifacedd;

import com.sun.ts.tests.ejb30.common.busiface.BusinessLocalIF1;
import com.sun.ts.tests.ejb30.common.busiface.BusinessLocalIF2;
import com.sun.ts.tests.ejb30.common.busiface.TestBeanBase;
import com.sun.ts.tests.ejb30.common.busiface.TestIF;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.annotation.Resource;

//@Remote({TestIF.class})
//@Stateless
public class TestBean extends TestBeanBase implements TestIF {
  @Resource(name = "sctx")
  private SessionContext sctx;

  @EJB(beanName = "BusinessBean", name = "localBean1")
  private BusinessLocalIF1 localBean1;

  @EJB(beanName = "BusinessBean", name = "localBean2")
  private BusinessLocalIF2 localBean2;

  @EJB(beanName = "SerializableLocalBean", name = "serializableLocalBean")
  private BusinessLocalIF1 serializableLocalBean;

  @EJB(beanName = "ExternalizableLocalBean", name = "externalizableLocalBean")
  private BusinessLocalIF1 externalizableLocalBean;

  @EJB(beanName = "SessionSynchronizationLocalBean", name = "sessionSynchronizationLocalBean")
  private BusinessLocalIF1 sessionSynchronizationLocalBean;

  @EJB(beanName = "SessionBeanLocalBean", name = "sessionBeanLocalBean")
  private BusinessLocalIF1 sessionBeanLocalBean;

  protected BusinessLocalIF2 getLocalBean2() {
    return (BusinessLocalIF2) (sctx.lookup("localBean2"));
  }

  protected BusinessLocalIF1 getLocalBean1() {
    return (BusinessLocalIF1) (sctx.lookup("localBean1"));
  }

  protected BusinessLocalIF1 getSerializableLocalBean() {
    return (BusinessLocalIF1) (sctx.lookup("serializableLocalBean"));
  }

  protected BusinessLocalIF1 getExternalizableLocalBean() {
    return (BusinessLocalIF1) (sctx.lookup("externalizableLocalBean"));
  }

  protected BusinessLocalIF1 getSessionSynchronizationLocalBean() {
    return (BusinessLocalIF1) (sctx.lookup("sessionSynchronizationLocalBean"));
  }

  protected BusinessLocalIF1 getTimedObjectLocalBean() {
    return null;
  }

  protected BusinessLocalIF1 getSessionBeanLocalBean() {
    return (BusinessLocalIF1) (sctx.lookup("sessionBeanLocalBean"));
  }
}
