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
 * @(#)SecTestEJB.java	1.10 03/05/16
 */

package com.sun.ts.tests.ejb.ee.sec.cmp.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import java.util.*;
import javax.ejb.*;
import java.rmi.*;

import java.sql.*;

public class SecTestEJB implements EntityBean {

  public String BRAND_NAME;

  public Integer KEY_ID;

  public float PRICE;

  private EntityContext ectx = null;

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

  public void ejbStore() {
    TestUtil.logTrace("ejbStore");
  }

  public boolean IsCaller(String caller) {
    TestUtil.logMsg("EntityContext.getCallerPrincipal().getName(): "
        + ectx.getCallerPrincipal());

    TestUtil.logMsg("Comparing getCallerPrincipal with caller " + caller);

    if (ectx.getCallerPrincipal().getName().indexOf(caller) < 0) {
      TestUtil
          .logMsg("get CallerPrincipal didn't match with the caller " + caller);
      return false;
    } else {
      TestUtil.logMsg("get CallerPrincipal matched with the caller " + caller);
      return true;

    }
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
