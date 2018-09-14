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

package com.sun.ts.tests.ejb.ee.pm.selfXself;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.ejb.*;
import java.rmi.*;

public abstract class EmployeeEJB implements EntityBean {

  // JNDI Names for Local Home Interfaces

  private static final String DepartmentLocal = "java:comp/env/ejb/DepartmentLocal";

  private static final String EmployeeLocal = "java:comp/env/ejb/EmployeeLocal";

  private EntityContext ectx = null;

  private TSNamingContext nctx = null;

  // ===========================================================
  // getters and setters for CMP fields

  public abstract Integer getId();

  public abstract void setId(Integer v);

  public abstract String getFirstName();

  public abstract void setFirstName(String v);

  public abstract String getLastName();

  public abstract void setLastName(String v);

  public abstract java.util.Date getHireDate();

  public abstract void setHireDate(java.util.Date d);

  public abstract float getSalary();

  public abstract void setSalary(float f);

  // ===========================================================
  // getters and setters for CMR fields

  // 1x1
  public abstract DepartmentLocal getDepartment();

  public abstract void setDepartment(DepartmentLocal v);

  // 1x1
  public abstract EmployeeLocal getManager();

  public abstract void setManager(EmployeeLocal v);

  // ===========================================================
  // life cycle methods
  // ===========================================================

  public Integer ejbCreate(Integer id, String firstName, String lastName,
      java.util.Date hireDate, float salary) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    try {
      setId(id);
      setFirstName(firstName);
      setLastName(lastName);
      setHireDate(hireDate);
      setSalary(salary);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    }
    return null;
  }

  public void ejbPostCreate(Integer id, String firstName, String lastName,
      java.util.Date hireDate, float salary) throws CreateException {
    TestUtil.logTrace("ejbPostCreate");
  }

  public void setEntityContext(EntityContext c) {
    TestUtil.logTrace("setEntityContext");
    ectx = c;
  }

  public void unsetEntityContext() {
    TestUtil.logTrace("unsetEntityContext");
  }

  public void ejbRemove() throws RemoveException {
    TestUtil.logTrace("ejbRemove");
  }

  public void ejbActivate() {
    TestUtil.logTrace("ejbActivate");
  }

  public void ejbPassivate() {
    TestUtil.logTrace("ejbPassivate");
  }

  public void ejbStore() {
    TestUtil.logTrace("ejbStore");
  }

  public void ejbLoad() {
    TestUtil.logTrace("ejbLoad");
  }

  // ===========================================================
  // Employee interface business methods

  public void addDepartment(Department d) {
    TestUtil.logTrace("addDepartment");
    try {
      TSNamingContext nctx = new TSNamingContext();
      Integer deptPK = (Integer) d.getPrimaryKey();
      DepartmentLocalHome deptLocalHome = (DepartmentLocalHome) nctx
          .lookup(DepartmentLocal);

      DepartmentLocal deptLocal = deptLocalHome.findByPrimaryKey(deptPK);

      setDepartment(deptLocal);

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("addDepartment:" + e);
    }
  }

  public void addManager(Employee e) {
    TestUtil.logTrace("addManager");
    try {

      Integer employeePK = (Integer) e.getPrimaryKey();
      EmployeeLocalHome empLocalHome = (EmployeeLocalHome) ectx
          .getEJBLocalHome();
      EmployeeLocal empLocal = empLocalHome.findByPrimaryKey(employeePK);

      setManager(empLocal);

    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new EJBException("addManager:" + ex);
    }
  }

  public void initLogging(Properties p) {
    TestUtil.logTrace("initLogging");
    try {
      TestUtil.init(p);
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    }
  }

  private boolean nullTest() {
    EmployeeLocal eOne = null;
    EmployeeLocal m1 = null;
    try {
      TestUtil.logTrace("nullTest");
      EmployeeLocalHome empLocalHome = (EmployeeLocalHome) ectx
          .getEJBLocalHome();

      eOne = (EmployeeLocal) empLocalHome.findByPrimaryKey(new Integer(8));

      m1 = eOne.getManager();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logErr("Exception occurred in nullTest: ", e);
    }

    if (m1 == null)
      return true;
    else
      return false;
  }

  public boolean test3() {
    TestUtil.logTrace("test3");
    return nullTest();
  }

  public boolean test4() {
    try {
      TestUtil.logTrace("test4");
      EmployeeLocalHome empLocalHome = (EmployeeLocalHome) ectx
          .getEJBLocalHome();

      EmployeeLocal eOne = (EmployeeLocal) empLocalHome
          .findByPrimaryKey(new Integer(1));
      EmployeeLocal eTwo = (EmployeeLocal) empLocalHome
          .findByPrimaryKey(new Integer(2));
      EmployeeLocal mOne = (EmployeeLocal) empLocalHome
          .findByPrimaryKey(new Integer(3));
      EmployeeLocal mTwo = (EmployeeLocal) empLocalHome
          .findByPrimaryKey(new Integer(4));

      mOne = eOne.getManager();
      mTwo = eTwo.getManager();
      EmployeeLocal m1, m2;

      TestUtil.logTrace("set eOne manager to mTwo manager");
      eOne.setManager(mTwo);

      m1 = eOne.getManager();
      m2 = eTwo.getManager();

      TestUtil.logTrace("test4");
      if (m1.isIdentical(mTwo) && m2 == null) {
        TestUtil.logMsg("Relationship assignment passed");
        return true;
      } else {
        TestUtil.logMsg("Relationship assignment failed");
        if (!m1.isIdentical(mTwo))
          TestUtil.logErr("m1 not identical to m2");
        if (m2 != null)
          TestUtil.logErr("m2 not null");
        return false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e, e);
      return false;
    }
  }

}
