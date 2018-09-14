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
 * @(#)HelperEJB.java	1.7 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.entity.cmp20.entitybeantest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.ejb.*;
import java.rmi.*;
import java.sql.*;

public class HelperEJB implements SessionBean {
  private SessionContext sctx = null;

  private Properties harnessProps = null;

  private boolean ejbCreateFlag = false;

  private boolean ejbActivateFlag = false;

  private boolean ejbPassivateFlag = false;

  private boolean ejbRemoveFlag = false;

  private boolean ejbContextFlag = false;

  private boolean ejbUnsetContextFlag = false;

  private boolean ejbLoadFlag = false;

  private boolean ejbStoreFlag = false;

  private boolean createLifeCycleFlag = false;

  private int createMethodCalledFlag = 0;

  public void ejbCreate(Properties p) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    harnessProps = p;
    try {
      TestUtil.logMsg("initialize remote logging");
      TestUtil.init(p);
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new CreateException(e.getMessage());
    }
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
  // Helper interface (our business methods)

  public void reset() {
    ejbCreateFlag = false;
    ejbActivateFlag = false;
    ejbPassivateFlag = false;
    ejbRemoveFlag = false;
    ejbContextFlag = false;
    ejbUnsetContextFlag = false;
    ejbLoadFlag = false;
    ejbStoreFlag = false;
    createLifeCycleFlag = false;
    createMethodCalledFlag = 0;
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
    ejbContextFlag = b;
  }

  public boolean isContext() {
    return ejbContextFlag;
  }

  public void setUnsetContext(boolean b) {
    ejbUnsetContextFlag = b;
  }

  public boolean isUnsetContext() {
    return ejbUnsetContextFlag;
  }

  public void setLoad(boolean b) {
    ejbLoadFlag = b;
  }

  public boolean isLoad() {
    return ejbLoadFlag;
  }

  public void setStore(boolean b) {
    ejbStoreFlag = b;
  }

  public boolean isStore() {
    return ejbStoreFlag;
  }

  public void setCreateLifeCycle(boolean b) {
    createLifeCycleFlag = b;
  }

  public boolean isCreateLifeCycle() {
    return createLifeCycleFlag;
  }

  public void setCreateMethodCalled(int b) {
    createMethodCalledFlag = b;
  }

  public int isCreateMethodCalled() {
    return createMethodCalledFlag;
  }

  public boolean isCreateLifeCycle1() {
    boolean status;
    TestUtil.logTrace("isCreateLifeCycle1");
    if (createLifeCycleFlag && createMethodCalledFlag == 1)
      status = true;
    else
      status = false;
    reset();
    return status;
  }

  public boolean isCreateLifeCycle2() {
    boolean status;
    TestUtil.logTrace("isCreateLifeCycle2");
    if (createLifeCycleFlag && createMethodCalledFlag == 2)
      status = true;
    else
      status = false;
    reset();
    return status;
  }
}
