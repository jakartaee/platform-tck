/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.annotations.mapkeytemporal;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Client extends PMClientBase {

    private static final long serialVersionUID = 20L;
    final Date d1 = getUtilDate("2000-02-14");
    final Date d2 = getUtilDate("2001-06-27");
    final Date d3 = getUtilDate("2002-07-07");
    final Date d4 = getUtilDate("2003-03-03");
    final Date d5 = getSQLDate();
    final Date d6 = getUtilDate("2000-10-14");
    final Date d7 = getUtilDate("2001-11-27");

    public Client() {
    }

    private static Employee empRef[] = new Employee[5];
    private static Employee2 empRef2[] = new Employee2[5];
    private static Employee4 empRef3[] = new Employee4[5];

    public static void main(String[] args) {
        Client theTests = new Client();
        Status s = theTests.run(args, System.out, System.err);
        s.exit();
    }

    public void setup(String[] args, Properties p) throws Fault {
        TestUtil.logTrace("setup");
        try {
            super.setup(args, p);
            removeTestData();
            createTestData();
            TestUtil.logTrace("Done creating test data");

        } catch (Exception e) {
            TestUtil.logErr("Exception: ", e);
            throw new Fault("Setup failed:", e);
        }
    }
    public void setupCust(String[] args, Properties p) throws Fault {
        TestUtil.logTrace("setup");
        try {
            super.setup(args, p);
            removeCustTestData();
        } catch (Exception e) {
            TestUtil.logErr("Exception: ", e);
            throw new Fault("Setup failed:", e);

        }
    }
    /*
    * @testName: mapKeyTemporalTest
    * @assertion_ids: PERSISTENCE:SPEC:2064; PERSISTENCE:SPEC:2065;
    *                   PERSISTENCE:SPEC:2065.2;
    * @test_Strategy:
    *
    */
    public void mapKeyTemporalTest() throws Fault {

        boolean pass1 = false;
        boolean pass2 = false;
        Set<Date> expected = new HashSet<Date>();
        expected.add(d1);
        expected.add(d2);
        expected.add(d3);

        try {
            getEntityTransaction().begin();
            Employee emp = getEntityManager().find(Employee.class, 1);
            TestUtil.logTrace("Name:"  + emp.getLastName());
            Department dept = emp.getDepartment();
            TestUtil.logTrace("Dept=" + dept.getName());
            Map<Date, Employee> emps = dept.getLastNameEmployees();
            if (TestUtil.traceflag) {
                for (Map.Entry<Date, Employee> entry : emps.entrySet()) {
                    TestUtil.logTrace("map:" + entry.getKey() + ", " + entry.getValue().getId() + " "
                            + entry.getValue().getLastName());
                }
            }
            Set<Date> keys = emps.keySet();
            for (Date key : keys) {
                TestUtil.logTrace("key:" + key);
            }
            if (expected.containsAll(keys) && keys.containsAll(expected) && expected.size() == keys.size()) {
                TestUtil.logTrace("Received expected keys");
                pass1 = true;
            } else {
                TestUtil.logErr("Did not received expected keys");
                TestUtil.logErr("Expected:");
                for (Date key : expected) {
                    TestUtil.logTrace("key:" + key);
                }
                TestUtil.logErr("Actual:");
                for (Date key : keys) {
                    TestUtil.logTrace("key:" + key);
                }
            }

            Set<Employee> sExpected = new HashSet<Employee>();
            sExpected.add(empRef[0]);
            sExpected.add(empRef[1]);
            sExpected.add(empRef[2]);


            Collection<Employee> employees = emps.values();
            for (Employee e : employees) {
                TestUtil.logTrace("values:" + e.getId() + " " + e.getLastName());
            }
            if (sExpected.containsAll(employees) && employees.containsAll(sExpected) && sExpected.size() == employees.size()) {
                TestUtil.logTrace("Received expected values");
                pass2 = true;
            } else {
                TestUtil.logErr("Did not received expected values");
                TestUtil.logErr("Expected:");
                for (Employee e : sExpected) {
                    TestUtil.logTrace("Employee:" + e.getId() + " " + e.getLastName());
                }
                TestUtil.logErr("Actual:");
                for (Employee e : employees) {
                    TestUtil.logTrace("Employee:" + e.getId() + " " + e.getLastName());
                }
            }
            getEntityTransaction().commit();

        } catch (Exception ex) {
            TestUtil.logErr("Unexpected exception occurred", ex);
        }

        if (!pass1 || !pass2) {
            throw new Fault("mapKeyTemporalTest failed");
        }
    }

    /*
    * @testName: mapKeyEnumeratedWithMayKeyClassAnnotationTest
    * @assertion_ids: PERSISTENCE:SPEC:2066; PERSISTENCE:SPEC:2064;
    *                  PERSISTENCE:SPEC:2065; PERSISTENCE:SPEC:2065.2;
    * @test_Strategy:
    *
    */
    public void mapKeyEnumeratedWithMayKeyClassAnnotationTest() throws Fault {

        boolean pass1 = false;
        boolean pass2 = false;
        Set<Date> expected = new HashSet<Date>();
        expected.add(d4);
        expected.add(d5);

        try {
            getEntityTransaction().begin();
            Employee2 emp = getEntityManager().find(Employee2.class, 2);
            TestUtil.logTrace("Name:" +  emp.getLastName());
            Department2 dept = emp.getDepartment();
            TestUtil.logTrace("Dept=" + dept.getName());
            Map<Date, Employee2> emps = dept.getLastNameEmployees();
            if (TestUtil.traceflag) {
                for (Map.Entry<Date, Employee2> entry : emps.entrySet()) {
                    TestUtil.logTrace("map:" + entry.getKey() + ", " + entry.getValue().getId() + " "
                            + entry.getValue().getLastName());
                }
            }
            Set<Date> keys = emps.keySet();
            for (Date key : keys) {
                TestUtil.logTrace("key:" + key);
            }
            if (expected.containsAll(keys) && keys.containsAll(expected) && expected.size() == keys.size()) {
                TestUtil.logTrace("Received expected keys");
                pass1 = true;
            } else {
                TestUtil.logErr("Did not received expected keys");
                TestUtil.logErr("Expected:");
                for (Date key : expected) {
                    TestUtil.logTrace("key:" + key);
                }
                TestUtil.logErr("Actual:");
                for (Date key : keys) {
                    TestUtil.logTrace("key:" + key);
                }
            }

            Set<Employee2> sExpected = new HashSet<Employee2>();
            sExpected.add(empRef2[0]);
            sExpected.add(empRef2[1]);


            Collection<Employee2> employees = emps.values();
            for (Employee2 e : employees) {
                TestUtil.logTrace("values:" + e.getId() + " " +  e.getLastName());
            }
            if (sExpected.containsAll(employees) && employees.containsAll(sExpected) && sExpected.size() == employees.size()) {
                TestUtil.logTrace("Received expected values");
                pass2 = true;
            } else {
                TestUtil.logErr("Did not received expected values");
                TestUtil.logErr("Expected:");
                for (Employee2 e : sExpected) {
                    TestUtil.logTrace("Employee:" + e.getId() + " " + e.getLastName());
                }
                TestUtil.logErr("Actual:");
                for (Employee2 e : employees) {
                    TestUtil.logTrace("Employee:" + e.getId() + " "  + e.getLastName());
                }
            }
            getEntityTransaction().commit();

        } catch (Exception ex) {
            TestUtil.logErr("Unexpected exception occurred", ex);
        }

        if (!pass1 || !pass2) {
            throw new Fault("mapKeyEnumeratedWithMayKeyClassAnnotationTest failed");
        }
    }
   /*
    * @testName: elementCollectionTest
    * @assertion_ids: PERSISTENCE:SPEC:2064; PERSISTENCE:SPEC:2065;
    *                   PERSISTENCE:SPEC:2065.1;
    * @test_Strategy:
    */
   public void elementCollectionTest() throws Fault {

        boolean pass1 = false;
        boolean pass2 = false;
        Set<Date> expected = new HashSet<Date>();
        expected.add(d6);
        expected.add(d7);

        try {
            getEntityTransaction().begin();
            Employee4 emp = getEntityManager().find(Employee4.class, 6);
            TestUtil.logTrace("Name:" +  emp.getLastName());
            Department4 dept = emp.getDepartment();
            TestUtil.logTrace("Dept=" + dept.getName());
            Map<Date, EmbeddedEmployee> emps = dept.getLastNameEmployees();
            if (TestUtil.traceflag) {
                for (Entry<Date, EmbeddedEmployee> entry : emps.entrySet()) {
                    TestUtil.logTrace("set:" + entry.getKey() + ", " + entry.getValue().employeeId + " "
                            + entry.getValue().employeeName);
                }
            }
            Set<Date> keys = emps.keySet();
            for (Date key : keys) {
                TestUtil.logTrace("key:" + key);
            }
            if (expected.containsAll(keys) && keys.containsAll(expected) && expected.size() == keys.size()) {
                TestUtil.logTrace("Received expected keys");
                pass1 = true;
            } else {
                TestUtil.logErr("Did not received expected keys");
                TestUtil.logErr("Expected:");
                for (Date key : expected) {
                    TestUtil.logTrace("key:" + key);
                }
                TestUtil.logErr("Actual:");
                for (Date key : keys) {
                    TestUtil.logTrace("key:" + key);
                }
            }

            Set<Employee4> sExpected = new HashSet<Employee4>();
            sExpected.add(empRef3[0]);
            sExpected.add(empRef3[1]);


            Collection<Employee4> employees = emps.values().stream().flatMap(a->Stream.of(new Employee4(a.employeeId, a.employeeName))).collect(Collectors.toList());
            for (Employee4 e : employees) {
                TestUtil.logTrace("values:" + e.getId() + " " +  e.getLastName());
            }
            if (sExpected.containsAll(employees) && employees.containsAll(sExpected) && sExpected.size() == employees.size()) {
                TestUtil.logTrace("Received expected values");
                pass2 = true;
            } else {
                TestUtil.logErr("Did not received expected values");
                TestUtil.logErr("Expected:");
                for (Employee4 e : sExpected) {
                    TestUtil.logTrace("Employee:" + e.getId() + " " + e.getLastName());
                }
                TestUtil.logErr("Actual:");
                for (Employee4 e : employees) {
                    TestUtil.logTrace("Employee:" + e.getId() + " "  + e.getLastName());
                }
            }
            getEntityTransaction().commit();

        } catch (Exception ex) {
            TestUtil.logErr("Unexpected exception occurred", ex);
        }

        if (!pass1 || !pass2) {
            throw new Fault("elementCollectionTest failed");
        }
    }
    /*
    *  Business Methods to set up data for Test Cases
    */
    private void createTestData() throws Exception {
        TestUtil.logTrace("createTestData");
        try {
            getEntityTransaction().begin();


            TestUtil.logTrace("Create 2 - Departments");
            Department deptRef[] = new Department[2];
            deptRef[0] = new Department(1, "Marketing");
            deptRef[1] = new Department(2, "Administration");

            TestUtil.logTrace("Persist departments ");
            for (Department dept : deptRef) {
                if (dept != null) {
                    getEntityManager().persist(dept);
                    TestUtil.logTrace("persisted department " + dept.getName());
                }
            }
            TestUtil.logTrace("Create 2 - Department2");
            Department2 deptRef2[] = new Department2[2];
            deptRef2[0] = new Department2(3, "Development");
            deptRef2[1] = new Department2(4, "Release");

            TestUtil.logTrace("Persist Department2 ");
            for (Department2 dept : deptRef2) {
                if (dept != null) {
                    getEntityManager().persist(dept);
                    TestUtil.logTrace("persisted department2 " + dept.getName());
                }
            }


            TestUtil.logTrace("Create 2 - Department3");
            Department4 deptRef3[] = new Department4[2];
            deptRef3[0] = new Department4(5, "Shipping");
            deptRef3[1] = new Department4(6, "Receiving");

            TestUtil.logTrace("Persist Department2s ");
            for (Department4 dept : deptRef3) {
                if (dept != null) {
                    getEntityManager().persist(dept);
                    TestUtil.logTrace("persisted department3 " + dept.getName());
                }
            }

            TestUtil.logTrace("Create 5 - Employee");
            empRef[0] = new Employee(1, "Frechette");
            empRef[0].setDepartment(deptRef[0]);

            empRef[1] = new Employee(3, "McGowan");
            empRef[1].setDepartment(deptRef[0]);

            empRef[2] = new Employee(5, "DMilla");
            empRef[2].setDepartment(deptRef[0]);

            empRef2[0] = new Employee2(2, "Smith");
            empRef2[0].setDepartment(deptRef2[0]);

            empRef2[1] = new Employee2(4, "Bissett");
            empRef2[1].setDepartment(deptRef2[0]);

            empRef3[0] = new Employee4(6, "Donahue");
            empRef3[0].setDepartment(deptRef3[0]);

            empRef3[1] = new Employee4(7, "Sanborn");
            empRef3[1].setDepartment(deptRef3[0]);


            Map<Date, Employee> link = new HashMap<Date, Employee>();

            link.put(d1, empRef[0]);
            link.put(d2, empRef[1]);
            link.put(d3, empRef[2]);
            deptRef[0].setLastNameEmployees(link);

            Map<Date, Employee2> link2 = new HashMap<Date, Employee2>();
            link2.put(d4, empRef2[0]);
            link2.put(d5, empRef2[1]);
            deptRef2[0].setLastNameEmployees(link2);

            Map<Date,EmbeddedEmployee> link3 = new HashMap<Date,EmbeddedEmployee>();
            link3.put(d6, new EmbeddedEmployee(empRef3[0]));
            link3.put(d7, new EmbeddedEmployee(empRef3[1]));
            deptRef3[0].setLastNameEmployees(link3);

            TestUtil.logTrace("Persist Employee ");
            for (Employee emp : empRef) {
                if (emp != null) {
                    getEntityManager().persist(emp);
                    TestUtil.logTrace("persisted Employee " + emp.getId());
                }
            }
            TestUtil.logTrace("Persist Employee2 ");
            for (Employee2 emp : empRef2) {
                if (emp != null) {
                    getEntityManager().persist(emp);
                    TestUtil.logTrace("persisted Employee2 " + emp.getId());
                }
            }
            TestUtil.logTrace("Persist Employee3 ");
            for (Employee4 emp : empRef3) {
                if (emp != null) {
                    getEntityManager().persist(emp);
                    TestUtil.logTrace("persisted Employee3 " + emp.getId());
                }
            }
            // Merge Department
            TestUtil.logTrace("Merge Department ");
            for (Department dept : deptRef) {
                if (dept != null) {
                    getEntityManager().merge(dept);
                    TestUtil.logTrace("merged Department " + dept.getName());

                }
            }

            // Merge Department
            TestUtil.logTrace("Merge Department2 ");
            for (Department2 dept : deptRef2) {
                if (dept != null) {
                    getEntityManager().merge(dept);
                    TestUtil.logTrace("merged Department2 " + dept.getName());

                }
            }

            // Merge Department
            TestUtil.logTrace("Merge Department3 ");
            for (Department4 dept : deptRef3) {
                if (dept != null) {
                    getEntityManager().merge(dept);
                    TestUtil.logTrace("merged Department3 " + dept.getName());

                }
            }
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

    public void cleanup() throws Fault {
        TestUtil.logTrace("cleanup");
        removeTestData();
        TestUtil.logTrace("cleanup complete, calling super.cleanup");
        super.cleanup();
    }
    public void cleanupCust() throws Fault {
        TestUtil.logTrace("cleanup");
        removeCustTestData();
        TestUtil.logTrace("cleanup complete, calling super.cleanup");
        super.cleanup();
    }
    private void removeTestData() {
        TestUtil.logTrace("removeTestData");

        if (getEntityTransaction().isActive()) {
            getEntityTransaction().rollback();
        }
        try {
            getEntityTransaction().begin();
            getEntityManager().createNativeQuery("Delete from EMP_MAPKEYCOL2").executeUpdate();
            getEntityManager().createNativeQuery("Delete from DEPARTMENT2").executeUpdate();
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
    private void removeCustTestData() {
         TestUtil.logTrace("removeCustTestData");
         if (getEntityTransaction().isActive()) {
             getEntityTransaction().rollback();
         }
         try {
             getEntityTransaction().begin();
             getEntityManager().createNativeQuery("DELETE FROM CUST_TABLE").executeUpdate();
             getEntityManager().createNativeQuery("DELETE FROM DATES_TABLE").executeUpdate();
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
