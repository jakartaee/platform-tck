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

package com.sun.ts.tests.ejb30.bb.session.stateful.noattrremotelocal;

import com.sun.ts.tests.ejb30.common.busiface.BusinessLocalIF1;
import com.sun.ts.tests.ejb30.common.busiface.BusinessLocalIF2;
import com.sun.ts.tests.ejb30.common.busiface.TestBeanBase;
import com.sun.ts.tests.ejb30.common.busiface.TestIF;
import javax.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.Remote;
import jakarta.ejb.Remove;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateful;

//no-attribute @Remote and @Local should work if there is only one potential
//business interface, which can only be TestIF for this bean.
//@Remote({TestIF.class})
@Remote
@Stateful
public class TestBean extends TestBeanBase implements TestIF {
  @Resource(name = "sctx")
  private SessionContext sctx;

  @EJB(beanName = "BusinessBean", name = "localBean1")
  private BusinessLocalIF1 localBean1;

  // @EJB(beanName="BusinessBean", name="localBean2")
  // private BusinessLocalIF2 localBean2;

  @EJB(beanName = "SerializableLocalBean", name = "serializableLocalBean")
  private BusinessLocalIF1 serializableLocalBean;

  @EJB(beanName = "ExternalizableLocalBean", name = "externalizableLocalBean")
  private BusinessLocalIF1 externalizableLocalBean;

  @EJB(beanName = "SessionBeanLocalBean", name = "sessionBeanLocalBean")
  private BusinessLocalIF1 sessionBeanLocalBean;

  protected BusinessLocalIF2 getLocalBean2() {
    // return (BusinessLocalIF2) (sctx.lookup("localBean2"));
    // this test not used in this directory
    return null;
  }

  protected BusinessLocalIF1 getLocalBean1() {
    return (BusinessLocalIF1) (sctx.lookup("localBean1"));
  }

  protected BusinessLocalIF1 getSerializableLocalBean() {
    return (BusinessLocalIF1) (sctx.lookup("serializableLocalBean"));
  }

  protected BusinessLocalIF1 getSessionBeanLocalBean() {
    return (BusinessLocalIF1) (sctx.lookup("sessionBeanLocalBean"));
  }

  protected BusinessLocalIF1 getExternalizableLocalBean() {
    return (BusinessLocalIF1) (sctx.lookup("externalizableLocalBean"));
  }

  // this one does not apply to slsb
  protected BusinessLocalIF1 getSessionSynchronizationLocalBean() {
    return null;
  }

  protected BusinessLocalIF1 getTimedObjectLocalBean() {
    // return (BusinessLocalIF1) (sctx.lookup("timedObjectLocalBean"));
    return null;
  }

  @Remove(retainIfException = false)
  public void remove() {
    super.remove();
  }
}
