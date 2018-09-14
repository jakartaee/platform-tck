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

package com.sun.ts.tests.ejb30.bb.session.stateless.sessioncontext.descriptor;

import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import com.sun.ts.tests.ejb30.common.sessioncontext.AcceptLocalIF;
import com.sun.ts.tests.ejb30.common.sessioncontext.TestBeanBase;
import com.sun.ts.tests.ejb30.common.sessioncontext.ThreeLocal1IF;
import com.sun.ts.tests.ejb30.common.sessioncontext.ThreeLocal2IF;
import com.sun.ts.tests.ejb30.common.sessioncontext.TwoLocalHome;
import com.sun.ts.tests.ejb30.common.sessioncontext.TwoLocalIF;
import javax.ejb.CreateException;
import javax.ejb.SessionContext;
import javax.ejb.EJB;

//@Stateless(name="TestBean")
//@Remote({TestIF.class})
public class TestBean extends TestBeanBase {
  // @Resource (name="sessionContext")
  private SessionContext sessionContext;

  // @EJB(name="local1")
  private ThreeLocal1IF local1;

  // @EJB(name="local2")
  private ThreeLocal2IF local2;

  // @EJB(name="acceptLocal")
  private AcceptLocalIF acceptLocal;

  @EJB(name = "twoSessionContextBeanHome")
  private TwoLocalHome twoSessionContextBeanLocalHome;

  protected TwoLocalIF getTwoLocal() throws TestFailedException {
    Object obj = null;
    try {
      obj = twoSessionContextBeanLocalHome.create();
    } catch (CreateException e) {
      throw new TestFailedException(e);
    }
    return (TwoLocalIF) obj;
  }

  protected SessionContext getSessionContext() {
    return sessionContext;
  }

  protected AcceptLocalIF getAcceptLocalBean() {
    return (AcceptLocalIF) (sessionContext.lookup("acceptLocal"));
  }

  protected ThreeLocal1IF getLocal1() {
    return (ThreeLocal1IF) (sessionContext.lookup("local1"));
  }

  protected ThreeLocal2IF getLocal2() {
    return (ThreeLocal2IF) (sessionContext.lookup("local2"));
  }

  public TestBean() {
  }

  public void remove() {
  }

}
