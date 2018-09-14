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
 * @(#)SecTestEJB.java	1.7 03/05/16
 */

package com.sun.ts.tests.ejb.ee.sec.cmp20.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import java.util.*;
import javax.ejb.*;
import java.rmi.*;

import java.sql.*;

public abstract class SecTestEJB implements EntityBean {

  private EntityContext ectx = null;

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
      TestUtil.logMsg("Obtain naming context");
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

  public void ejbRemove() throws RemoveException {
    TestUtil.logTrace("ejbRemove");
  }

  public void ejbStore() {
    TestUtil.logTrace("ejbStore");
  }

  public boolean IsCaller(String caller) {
    if (ectx.getCallerPrincipal().getName().indexOf(caller) < 0)
      return false;
    else
      return true;
  }

  public boolean EjbNotAuthz() {
    return true;
  }

  public boolean EjbIsAuthz() {
    return true;
  }

  public boolean EjbSecRoleRef(String role) {
    return ectx.isCallerInRole(role);
  }

  public boolean EjbOverloadedSecRoleRefs(String role1) {
    TestUtil.logMsg(
        "isCallerInRole(" + role1 + ") = " + ectx.isCallerInRole(role1));
    return ectx.isCallerInRole(role1);
  }

  public boolean EjbOverloadedSecRoleRefs(String role1, String role2) {
    TestUtil
        .logMsg("isCallerInRole(" + role1 + ")= " + ectx.isCallerInRole(role1)
            + "isCallerInRole(" + role2 + ")= " + ectx.isCallerInRole(role2));
    return ectx.isCallerInRole(role1) && ectx.isCallerInRole(role2);
  }

  public boolean checktest1() {
    return true;
  }

  public boolean excludetest1() {
    return true;
  }

  public void setEntityContext(EntityContext sc) {
    ectx = sc;
  }

  public void unsetEntityContext() {
    TestUtil.logTrace("unsetEntityContext");
  }

  public void ejbLoad() {
    TestUtil.logTrace("ejbLoad");
  }

  public void ejbActivate() {
  }

  public void ejbPassivate() {
  }
}
