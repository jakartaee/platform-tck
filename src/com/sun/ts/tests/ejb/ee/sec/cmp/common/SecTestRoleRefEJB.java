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
 * @(#)SecTestRoleRefEJB.java	1.7 03/05/16
 */

// The purpose of this EJB is to test scoping of security propagation of role references.

package com.sun.ts.tests.ejb.ee.sec.cmp.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import java.util.*;
import javax.ejb.*;
import java.rmi.*;

import java.sql.*;
import javax.naming.*;

public class SecTestRoleRefEJB implements EntityBean {

  private EntityContext ectx = null;

  public String BRAND_NAME;

  public Integer KEY_ID;

  public float PRICE;

  public void SecTestRoleRefEJB() throws CreateException {
    TestUtil.logTrace("In constructor");
  }

  public boolean EjbSecRoleRefScope(String role) {
    return ectx.isCallerInRole(role);
  }

  public Integer ejbCreate(Properties p, boolean newTable, int KEY_ID,
      String BRAND_NAME, float PRICE) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    try {
      TestUtil.init(p);
      this.KEY_ID = new Integer(KEY_ID);
      this.BRAND_NAME = BRAND_NAME;
      this.PRICE = PRICE;

    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new CreateException(e.getMessage());
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    }
    return this.KEY_ID;
  }

  public void ejbRemove() throws RemoveException {
    TestUtil.logTrace("ejbRemove");
  }

  public void ejbPostCreate(Properties p, boolean newTable, int KEY_ID,
      String BRAND_NAME, float PRICE) {
    TestUtil.logTrace("In ejbPostCreate !!");
  }

  public void ejbLoad() {
    TestUtil.logTrace("ejbLoad");
  }

  public void ejbStore() {
    TestUtil.logTrace("ejbStore");
  }

  public void setEntityContext(EntityContext sc) {
    ectx = sc;
  }

  public void unsetEntityContext() {
    TestUtil.logTrace("ejb.unsetEntityContext");
  }

  public void ejbActivate() {
    TestUtil.logTrace("ejbActivate");
  }

  public void ejbPassivate() {
    TestUtil.logTrace("ejbPassivate");
  }
}
