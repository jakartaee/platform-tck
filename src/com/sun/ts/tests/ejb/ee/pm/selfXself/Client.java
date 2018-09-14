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
import com.sun.ts.lib.harness.*;

import java.util.*;
import javax.ejb.*;
import javax.rmi.*;

import com.sun.javatest.Status;

public class Client extends EETest {
  public static final int EMPLOYEEREF = 1;

  public static final int DEPTREF = 2;

  public static final int NUMOFEMPLOYEES = 10;

  public static final int NUMOFDEPTS = 5;

  public static final String EmployeeBean = "java:comp/env/ejb/Employee";

  public static final String DeptBean = "java:comp/env/ejb/Department";

  public static EmployeeHome employeeHome = null;

  public static DepartmentHome deptHome = null;

  public final static Date emp0date = new Date(12345678);

  public final static Date emp1date = new Date(23456781);

  public final static Date emp2date = new Date(34567812);

  public final static Date emp3date = new Date(45678123);

  public final static Date emp4date = new Date(56781234);

  public final static Date emp5date = new Date(67812345);

  public final static Date emp6date = new Date(78123456);

  public final static Date emp7date = new Date(81234567);

  public final static Date emp8date = new Date(45348281);

  public final static Date emp9date = new Date(23672932);

  private static Employee employeeRef[] = new Employee[20];

  private static Department deptRef[] = new Department[20];

  public static Properties props = null;

  private static TSNamingContext nctx = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   *
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   * generateSQL;
   */

  public static void setup(String[] args, Properties p) throws Exception {
    TestUtil.logTrace("Setup");

    TestUtil.logMsg("Obtain Naming Context");
    nctx = new TSNamingContext();
    props = p;

    TestUtil.logMsg("Lookup EmployeeBean: " + EmployeeBean);
    employeeHome = (EmployeeHome) nctx.lookup(EmployeeBean, EmployeeHome.class);
    TestUtil.logMsg("Lookup DepartmentBean: " + DeptBean);
    deptHome = (DepartmentHome) nctx.lookup(DeptBean, DepartmentHome.class);
    try {
      TestUtil
          .logMsg("Check if test data already exists in Persistent Storage");
      if (SchemaAlreadyExists())
        return;

      TestUtil.logMsg("Begin creating test data in Persistent Storage");

      TestUtil.logMsg("Create Test EJB Data");
      createEJBs(p);
      TestUtil.logMsg("Done creating data in Persistent Storage");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      RemoveTestEJBs();
      throw new Exception("Exception occurred: " + e);
    }
  }

  /* Run tests */

  /*
   * @testName: selfRefTest1
   * 
   * @assertion_ids: EJB:SPEC:377; EJB:SPEC:409; EJB:SPEC:270
   * 
   * @test_Strategy: This test verifies a single-object finder returns a cmr
   * value as the result.
   */

  public void selfRefTest1() throws Fault {
    boolean pass = true;
    Employee e = null;
    Employee expectedResult = null;

    try {
      TestUtil.logMsg("Get employee whose manager's last name is ");
      expectedResult = (Employee) employeeHome.findByPrimaryKey(new Integer(2));
      TestUtil.logMsg("Find employee's manager");
      e = employeeHome.findEmployeeByQuery1("Green");
      TestUtil.logMsg("Check that we received the correct employee");
      if (!expectedResult.isIdentical(e)) {
        TestUtil.logErr("findCustomerByQuery1 did not return expected result");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully returned expected results");
      }
    } catch (Exception ex) {
      TestUtil.logErr("Caught exception selfRefTest1: " + ex);
      TestUtil.printStackTrace(ex);
      throw new Fault("selfRefTest1 failed", ex);
    }
    if (!pass)
      throw new Fault("selfRefTest1 failed");
  }

  /*
   * @testName: selfRefTest2
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   */

  public void selfRefTest2() throws Fault {
    boolean pass = true;
    Employee e = null;
    Integer expectedResult = new Integer(5);

    try {
      TestUtil.logMsg("Find Employee by First Name");
      e = employeeHome.findEmployeeByQuery2("Russo");
      TestUtil
          .logMsg("Check that we received the correct employee by last name");
      if (!expectedResult.equals(e.getId())) {
        pass = false;
      } else {
        TestUtil.logMsg("Successfully returned expected results");
      }
    } catch (Exception ex) {
      TestUtil.logErr("Caught exception selfRefTest2: " + ex);
      TestUtil.printStackTrace(ex);
      throw new Fault("selfRefTest2 failed", ex);
    }
    if (!pass)
      throw new Fault("selfRefTest2 failed");
  }

  /*
   * @testName: selfRefTest3
   * 
   * @assertion_ids: EJB:SPEC:232.1
   * 
   * @test_Strategy: A 1x1 uni-directional relationship between entitybean
   * between entitybean objects. Do set relationship fields to null. The results
   * should be set to null. Deploy EAR on the J2EE server. Ensure the entitybean
   * objects were created and that the persistence manager has null settings for
   * the relationship fields not set.
   *
   */

  public void selfRefTest3() throws Fault {
    boolean pass = true;
    Employee empRef = null;
    try {
      // Create entity bean with two entitybean objects in a 1x1
      // uni-directional relationship with each other
      logMsg("Create Entity Bean");
      empRef = (Employee) employeeHome.findByPrimaryKey(new Integer(4));

      // Uni-Directional relationship fields should be null for
      // entitybean object
      if (empRef.test3())
        TestUtil.logMsg("relationship fields are null - expected");
      else {
        TestUtil.logErr("relationship fields are nonnull - unexpected");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logErr("Exception caught removing bean ref" + e);
      throw new Fault("selfRefTest3 failed", e);
    }

    if (!pass)
      throw new Fault("selfRefTest3 failed");
  }

  /*
   * @testName: selfRefTest4
   * 
   * @assertion_ids: EJB:SPEC:204
   * 
   * @test_Strategy: A 1x1 uni-directional relationship between entitybean
   * objects. Create a 1x1 uni-directional relationship between entitybean
   * objects. Deploy EAR on the J2EE server. Perform the relationship assignment
   * per assertion tag. Ensure the proper relationship results are correct after
   * the assignment by the persistence manager.
   *
   */

  public void selfRefTest4() throws Fault {
    boolean pass = false;
    Employee empRef = null;
    Date test4date = new Date(34458281);
    try {

      // Create entity bean with two entitybean objects in a 1x1
      // uni-directional relationship with each other
      logMsg("Create Entity Bean");
      empRef = employeeHome.create(new Integer(99), "Tamara", "Jones",
          test4date, (float) 10500.0);
      empRef.initLogging(props);
      pass = empRef.test4();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("selfRefTest4 failed", e);
    } finally {
      try {
        if (empRef != null) {
          empRef.remove();
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logErr("Error caught removing bean:", e);
      }

      if (!pass)
        throw new Fault("selfRefTest4 failed");
    }
  }

  /* Business Methods */

  private static boolean SchemaAlreadyExists() throws Exception {
    boolean schemaExists = true;

    TestUtil.logTrace("SchemaAlreadyExists");

    Collection empCol = employeeHome.findAllEmployees();
    Collection deptCol = deptHome.findAllDepartments();

    if (empCol.size() != NUMOFEMPLOYEES || deptCol.size() != NUMOFDEPTS) {
      TestUtil.logMsg("Number of employees found = " + empCol.size());
      TestUtil.logMsg("Number of departments found = " + deptCol.size());
      schemaExists = false;
    }

    if (schemaExists) {
      TestUtil.logMsg("Test data already exists in Persistent Storage");
      return true;
    } else {
      TestUtil.logMsg("Test Data does not exist in Persistent Storage");
      RemoveTestEJBs();
      return false;
    }
  }

  private static void RemoveTestEJBs() {
    TestUtil.logTrace("RemoveTestEJBs");

    try {
      TestUtil.logTrace("Removing Employee EJBs");
      Collection col = employeeHome.findAllEmployees();
      Iterator i = col.iterator();
      while (i.hasNext()) {
        Employee eref = (Employee) PortableRemoteObject.narrow(i.next(),
            Employee.class);
        try {
          eref.remove();
        } catch (Exception e) {
          TestUtil.printStackTrace(e);
        }
      }
      TestUtil.logTrace("Removing Department EJBs");
      col = deptHome.findAllDepartments();
      i = col.iterator();
      while (i.hasNext()) {
        Department dref = (Department) PortableRemoteObject.narrow(i.next(),
            Department.class);
        try {
          dref.remove();
        } catch (Exception e) {
          TestUtil.printStackTrace(e);
        }
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  private static void createEJBs(Properties p) throws Exception {
    props = p;

    TestUtil.logTrace("createEJBs");
    TestUtil.logMsg("Create Employee EJB's [10 employees]");
    TestUtil.logMsg("Create Employee 1");
    employeeRef[0] = employeeHome.create(new Integer(1), "Alan", "Brown",
        emp0date, (float) 95000.0);
    employeeRef[0].initLogging(props);

    TestUtil.logMsg("Create Employee 2");
    employeeRef[1] = employeeHome.create(new Integer(2), "Arthur", "Fiedler",
        emp1date, (float) 45000.0);
    employeeRef[1].initLogging(props);

    TestUtil.logMsg("Create Employee 3");
    employeeRef[2] = employeeHome.create(new Integer(3), "Sheila", "Murrow",
        emp2date, (float) 23000.0);
    employeeRef[2].initLogging(props);

    TestUtil.logMsg("Create Employee 4");
    employeeRef[3] = employeeHome.create(new Integer(4), "Robert", "Redford",
        emp3date, (float) 100500.0);
    employeeRef[3].initLogging(props);

    TestUtil.logMsg("Create Employee 5");
    employeeRef[4] = employeeHome.create(new Integer(5), "Stephen", "Russo",
        emp4date, (float) 35000.0);
    employeeRef[4].initLogging(props);

    TestUtil.logMsg("Create Employee 6");
    employeeRef[5] = employeeHome.create(new Integer(6), "Karen", "Barry",
        emp5date, (float) 85898.0);
    employeeRef[5].initLogging(props);

    TestUtil.logMsg("Create Employee 7");
    employeeRef[6] = employeeHome.create(new Integer(7), "Jared", "Green",
        emp6date, (float) 93568.0);
    employeeRef[6].initLogging(props);

    TestUtil.logMsg("Create Employee 8");
    employeeRef[7] = employeeHome.create(new Integer(8), "Irene", "Carras",
        emp7date, (float) 24598.0);
    employeeRef[7].initLogging(props);

    TestUtil.logMsg("Create Employee 9");
    employeeRef[8] = employeeHome.create(new Integer(9), "William", "Leeson",
        emp8date, (float) 75980.0);
    employeeRef[8].initLogging(props);

    TestUtil.logMsg("Create Employee 10");
    employeeRef[9] = employeeHome.create(new Integer(10), "Hudson", "Phillips",
        emp9date, (float) 65432.0);
    employeeRef[9].initLogging(props);

    TestUtil.logMsg("Create Department EJB's [5 departments]");
    deptRef[0] = deptHome.create(new Integer(1), "engineering");
    deptRef[0].initLogging(props);

    deptRef[1] = deptHome.create(new Integer(2), "marketing");
    deptRef[1].initLogging(props);

    deptRef[2] = deptHome.create(new Integer(3), "sales");
    deptRef[2].initLogging(props);

    deptRef[3] = deptHome.create(new Integer(4), "services");
    deptRef[3].initLogging(props);

    deptRef[4] = deptHome.create(new Integer(5), "support");
    deptRef[4].initLogging(props);

    TestUtil.logMsg("Setting additional relationships for employeeRef[0]");
    employeeRef[0].addDepartment(deptRef[0]);
    employeeRef[0].addManager(employeeRef[5]);

    TestUtil.logMsg("Setting additional relationships for employeeRef[1]");
    employeeRef[1].addDepartment(deptRef[1]);
    employeeRef[1].addManager(employeeRef[6]);

    TestUtil.logMsg("Setting additional relationships for employeeRef[2]");
    employeeRef[2].addDepartment(deptRef[2]);
    employeeRef[2].addManager(employeeRef[7]);

    TestUtil.logMsg("Setting additional relationships for employeeRef[3]");
    employeeRef[3].addDepartment(deptRef[3]);
    employeeRef[3].addManager(employeeRef[8]);

    TestUtil.logMsg("Setting additional relationships for employeeRef[4]");
    employeeRef[4].addDepartment(deptRef[4]);
    employeeRef[4].addManager(employeeRef[9]);
  }

  public void cleanup() throws Fault {
    TestUtil.logMsg("Cleanup Entity Data");
    RemoveTestEJBs();
    TestUtil.logMsg("cleanup ok");
  }

}
