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

package com.sun.ts.tests.ejb.ee.bb.session.stateless.sessionbeantest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import javax.ejb.*;
import java.rmi.*;

public class CallBackEJB implements SessionBean {
  private boolean ejbCreateFlag = false;

  private boolean ejbActivateFlag = false;

  private boolean ejbPassivateFlag = false;

  private boolean ejbRemoveFlag = false;

  private boolean ejbSessionContextFlag = false;

  private SessionContext sctx = null;

  public void ejbCreate() throws CreateException {
    TestUtil.logTrace("ejbCreate");
  }

  public void setSessionContext(SessionContext sc) {
    TestUtil.logTrace("setSessionContext");
    this.sctx = sc;
  }

  public void ejbRemove() {
    TestUtil.logTrace("ejbRemove");
  }

  public void ejbActivate() {
    TestUtil.logTrace("ejbActivate");
  }

  public void ejbPassivate() {
    TestUtil.logTrace("ejbPassivate");
  }

  // ===========================================================
  // CallBack interface (our business methods)

  public void reset() {
    ejbCreateFlag = false;
    ejbActivateFlag = false;
    ejbPassivateFlag = false;
    ejbRemoveFlag = false;
    ejbSessionContextFlag = false;
  }

  public void setCreate(boolean b) {
    ejbCreateFlag = b;
  }

  public boolean isCreate() {
    return ejbCreateFlag;
  }

  public void setActivate(boolean b) {
    ejbActivateFlag = b;
  }

  public boolean isActivate() {
    return ejbActivateFlag;
  }

  public void setPassivate(boolean b) {
    ejbPassivateFlag = b;
  }

  public boolean isPassivate() {
    return ejbPassivateFlag;
  }

  public void setRemove(boolean b) {
    ejbRemoveFlag = b;
  }

  public boolean isRemove() {
    return ejbRemoveFlag;
  }

  public void setContext(boolean b) {
    ejbSessionContextFlag = b;
  }

  public boolean isContext() {
    return ejbSessionContextFlag;
  }
}
