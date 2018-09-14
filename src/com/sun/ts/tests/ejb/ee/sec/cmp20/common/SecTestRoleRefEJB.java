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

package com.sun.ts.tests.ejb.ee.sec.cmp20.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import java.util.*;
import javax.ejb.*;
import java.rmi.*;

import java.sql.*;
import javax.naming.*;

public abstract class SecTestRoleRefEJB implements EntityBean {

  private EntityContext ectx = null;

  public void SecTestRoleRefEJB() throws CreateException {
    TestUtil.logTrace("In constructor");
  }

  // Entity instance data
  public abstract Integer getId();

  public abstract void setId(Integer i);

  public abstract String getBrandName();

  public abstract void setBrandName(String s);

  public abstract float getPrice();

  public abstract void setPrice(float p);

  public Integer ejbCreate(int id, String brandName, float price)
      throws CreateException {
    TestUtil.logTrace("ejbCreate");
    Integer pk = new Integer(id);
    try {
      setId(pk);
      setBrandName(brandName);
      setPrice(price);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    }
    return null;
  }

  public void ejbPostCreate(int id, String brandName, float price) {
    TestUtil.logTrace("ejbPostCreate");
  }

  public boolean EjbSecRoleRefScope(String role) {
    return ectx.isCallerInRole(role);
  }

  public void ejbRemove() throws RemoveException {
    TestUtil.logTrace("ejbRemove");
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
