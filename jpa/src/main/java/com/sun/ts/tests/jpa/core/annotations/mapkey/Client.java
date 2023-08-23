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

package com.sun.ts.tests.jpa.core.annotations.mapkey;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

public class Client extends PMClientBase {

  public Client() {
  }
  
  protected Employee2 empRef2;

  protected Employee3 empRef3;

  protected Employee4 empRef4;

  protected static Department deptRef[] = new Department[5];
  
  protected Employee empRef[] = new Employee[10];


  /*
   * 
   * Business Methods to set up data for Test Cases
   */

  public void createTestDataCommon() throws Exception {
    try {

      TestUtil.logTrace("createTestDataCommon");

      getEntityTransaction().begin();
      TestUtil.logTrace("Create 2 Departments");
      deptRef[0] = new Department(1, "Marketing");
      deptRef[1] = new Department(2, "Administration");

      TestUtil.logTrace("Start to persist departments ");
      for (Department dept : deptRef) {
        if (dept != null) {
          getEntityManager().persist(dept);
          TestUtil.logTrace("persisted department " + dept.getName());
        }
      }

      getEntityManager().flush();
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception creating test data:", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception in rollback:", re);
      }
    }
  }

  protected void removeTestData() {
    TestUtil.logTrace("removeTestData");
    if (getEntityTransaction().isActive()) {
      getEntityTransaction().rollback();
    }
    try {
      getEntityTransaction().begin();
      getEntityManager().createNativeQuery("Delete from EMPLOYEE")
          .executeUpdate();
      getEntityManager().createNativeQuery("Delete from DEPARTMENT")
          .executeUpdate();
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Exception encountered while removing entities:", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception in RemoveSchemaData:", re);
      }
    }
  }
}
