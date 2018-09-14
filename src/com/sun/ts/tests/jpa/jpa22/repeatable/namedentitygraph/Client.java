/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.jpa22.repeatable.namedentitygraph;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.persistence.EntityGraph;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

public class Client extends PMClientBase {

  private static final long serialVersionUID = 22L;

  Employee3[] empRef = new Employee3[5];

  Department[] deptRef = new Department[2];

  public Client() {
  }

  public static void main(String[] args) {
    Client theTests = new Client();

    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    try {
      super.setup(args, p);
      displayMap(p);
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  public void setupEmployeeData(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setupOrderData");
    try {
      super.setup(args, p);
      removeTestData();
      createEmployeeData();
      displayMap(p);
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("cleanup complete, calling super.cleanup");
    super.cleanup();
  }

  public void cleanupEmployeeData() throws Fault {
    TestUtil.logTrace("Cleanup data");
    removeTestData();
    cleanup();
  }

  /*
   * @testName: entityGraphGetNameNoNameExistsTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:3417;
   * 
   * @test_Strategy: Use getName to get the name of the named entity graph in
   * the Employee2 entity that has no name
   */
  public void entityGraphGetNameNoNameExistsTest() throws Fault {
    boolean pass = false;

    List<EntityGraph<? super Employee2>> egs = getEntityManager()
        .getEntityGraphs(Employee2.class);
    if (egs.size() == 1) {
      EntityGraph<?> e = egs.get(0);
      if (e.getName().equals("Employee2")) {
        TestUtil.logTrace("Received expected name:" + e.getName());
        pass = true;
      } else {
        TestUtil.logErr("Expected name: Employee2, actual:" + e.getName());
      }
    } else {
      TestUtil
          .logErr("Expected 1 graph to be returned, instead got:" + egs.size());
    }

    if (!pass) {
      throw new Fault("entityGraphGetNameNoNameExistsTest failed");
    }
  }

  /*
   * @testName: getNameTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:3417;
   * 
   * @test_Strategy: Use getName to get the name of the entity graph
   */
  public void getNameTest() throws Fault {
    boolean pass = false;

    EntityGraph<Employee2> eg = getEntityManager()
        .createEntityGraph(Employee2.class);

    if (eg.getName() == null) {
      TestUtil.logTrace("Received expected null");
      pass = true;
    } else {
      TestUtil.logErr("Expected name: null, actual:" + eg.getName());
    }

    if (!pass) {
      throw new Fault("getNameTest failed");
    }
  }

  /*
   * @testName: getEntityGraphsClassTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:3417;
   * 
   * @test_Strategy: Use getEntityGraph to get the named entity graphs in the
   * Employee entity
   */
  public void getEntityGraphsClassTest() throws Fault {
    boolean pass = false;
    List<String> expected = new ArrayList<String>();
    expected.add("first_last_graph");
    expected.add("last_salary_graph");
    expected.add("lastname_department_subgraphs");

    List<String> actual = new ArrayList<String>();

    List<EntityGraph<? super Employee3>> eg = getEntityManager()
        .getEntityGraphs(Employee3.class);
    if (eg.size() > 0) {
      for (EntityGraph<?> e : eg) {
        actual.add(e.getName());
      }
      if (actual.containsAll(expected) && expected.containsAll(actual)
          && actual.size() == expected.size()) {
        TestUtil.logTrace("Received expected results");
        pass = true;
      } else {
        TestUtil.logErr("Expected results");
        for (String s : expected) {
          TestUtil.logErr("expected:" + s);
        }
        TestUtil.logErr("Actual results");
        for (String s : actual) {
          TestUtil.logErr("actual:" + s);
        }
      }
    } else {
      TestUtil.logErr(
          "No named entity graphs were returned eventhough they exist in entity");
    }

    if (!pass) {
      throw new Fault("getEntityGraphsClassTest failed");
    }
  }

  private void createEmployeeData() {

    try {
      getEntityTransaction().begin();
      TestUtil.logMsg("Creating Employees");

      final Date d1 = getUtilDate("2000-02-14");
      final Date d2 = getUtilDate("2001-06-27");
      final Date d3 = getUtilDate("2002-07-07");
      final Date d4 = getUtilDate("2003-03-03");
      final Date d5 = getUtilDate();

      deptRef[0] = new Department(1, "Marketing");
      deptRef[1] = new Department(2, "Administration");
      for (Department d : deptRef) {
        getEntityManager().persist(d);
        TestUtil.logTrace("persisted department:" + d);
      }

      empRef[0] = new Employee3(1, "Alan", "Frechette", d1, (float) 35000.0);
      empRef[0].setDepartment(deptRef[0]);
      empRef[1] = new Employee3(2, "Arthur", "Frechette", d2, (float) 35000.0);
      empRef[1].setDepartment(deptRef[0]);
      empRef[2] = new Employee3(3, "Shelly", "McGowan", d3, (float) 50000.0);
      empRef[2].setDepartment(deptRef[1]);
      empRef[3] = new Employee3(4, "Robert", "Bissett", d4, (float) 55000.0);
      empRef[3].setDepartment(deptRef[1]);
      empRef[4] = new Employee3(5, "Stephen", "DMilla", d5, (float) 25000.0);
      empRef[4].setDepartment(deptRef[1]);

      for (Employee3 e : empRef) {
        if (e != null) {
          getEntityManager().persist(e);
          TestUtil.logTrace("persisted employee3:" + e);
        }
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
  }

  private void removeTestData() {
    TestUtil.logTrace("removeTestData");
    if (getEntityTransaction().isActive()) {
      getEntityTransaction().rollback();
    }
    try {
      getEntityTransaction().begin();
      getEntityManager().createNativeQuery("DELETE FROM EMPLOYEE")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM DEPARTMENT")
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
        TestUtil.logErr("Unexpected Exception in removeTestData:", re);
      }
    }
  }
}
