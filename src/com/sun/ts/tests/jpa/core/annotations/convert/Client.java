/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.annotations.convert;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Client extends PMClientBase {

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
      removeTestData();
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  /*
   * @testName: convertTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:3310; PERSISTENCE:JAVADOC:3311;
   * PERSISTENCE:SPEC:1487; PERSISTENCE:SPEC:1489; PERSISTENCE:SPEC:1489.1;
   * PERSISTENCE:SPEC:1956; PERSISTENCE:SPEC:1956.1; PERSISTENCE:SPEC:1991;
   * 
   * @test_Strategy:
   */
  public void convertTest() throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      Employee expected = new Employee(1, "Alan", "Smith", "3#5#0#0#0.0");

      TestUtil.logTrace("Persisting Employee");
      getEntityManager().persist(expected);
      getEntityManager().flush();
      getEntityTransaction().commit();
      clearCache();
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "find the previously persisted Employees and verify them and their departments");
      Employee emp = getEntityManager().find(Employee.class, expected.getId());
      if (emp != null) {
        TestUtil.logTrace("Found employee: " + emp.getId());
        // Remove the '#' symbols from the expected salary and then do
        // comparison
        expected.setSalary(expected.getSalary().replace("#", ""));
        if (emp.equals(expected)) {
          TestUtil.logTrace("Received expected employee:" + emp.toString());
          pass = true;
        } else {
          TestUtil.logErr(
              "Expected:" + expected.toString() + ", actual:" + emp.toString());
        }

      } else {
        TestUtil.logErr("Find returned null Employee instead of:" + expected);
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred: ", e);
      pass = false;
    }
    if (!pass) {
      throw new Fault("convertTest failed");
    }
  }

  /*
   * @testName: convertExceptionDuringPersistTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1495; PERSISTENCE:JAVADOC:3312;
   * PERSISTENCE:JAVADOC:3313; PERSISTENCE:SPEC:1483; PERSISTENCE:SPEC:1483.1;
   * 
   * @test_Strategy: Converter throws a RuntimeException wrapped by a
   * PersistenceException during a persist
   */
  public void convertExceptionDuringPersistTest() throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("isActive():" + getEntityTransaction().isActive());
      B b = new B("1", "name1", 1,
          new Address("500 Oracle Parkway", "Redwood Shores", -1));
      getEntityManager().persist(b);
      getEntityManager().flush();
      getEntityTransaction().commit();
      TestUtil.logErr("Did not received expected PersistenceException");
    } catch (PersistenceException pe) {
      TestUtil.logTrace("Received expected PersistenceException");
      if (getEntityTransaction().getRollbackOnly()) {
        TestUtil.logTrace("Transaction was marked for rollback");
        pass = true;
      } else {
        TestUtil.logErr("Transaction was not marked for rollback");
      }

    } catch (Exception ex) {
      TestUtil.logErr("Unexpected exception received:", ex);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception while rolling back TX:", re);
      }
    }
    if (!pass) {
      throw new Fault("convertExceptionDuringPersistTest failed");
    }
  }

  /*
   * @testName: convertExceptionDuringLoadTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1495; PERSISTENCE:JAVADOC:3312;
   * PERSISTENCE:JAVADOC:3313; PERSISTENCE:SPEC:1483; PERSISTENCE:SPEC:1483.1;
   * 
   * @test_Strategy: Converter throws a RuntimeException wrapped by a
   * PersistenceException during a load
   */
  public void convertExceptionDuringLoadTest() throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("isActive():" + getEntityTransaction().isActive());

      B b = new B("1", "name1", 1,
          new Address("500 Oracle Parkway", "Redwood Shores", -2));
      TestUtil.logTrace("Persisting B");
      getEntityManager().persist(b);
      getEntityManager().flush();
      getEntityTransaction().commit();
      clearCache();
      getEntityTransaction().begin();
      try {
        TestUtil.logTrace("Finding B");

        B b1 = getEntityManager().find(B.class, "1");
        TestUtil.logErr("Did not received expected PersistenceException");
      } catch (PersistenceException pe) {
        TestUtil.logTrace("Received expected PersistenceException");
        if (getEntityTransaction().getRollbackOnly()) {
          TestUtil.logTrace("Transaction was marked for rollback");
          pass = true;
        } else {
          TestUtil.logErr("Transaction was not marked for rollback");
        }
      } catch (Exception ex) {
        TestUtil.logErr("Unexpected exception received:", ex);
      } finally {
        try {
          if (getEntityTransaction().isActive()) {
            getEntityTransaction().rollback();
          }
        } catch (Exception re) {
          TestUtil.logErr("Unexpected Exception while rolling back TX:", re);
        }
      }

    } catch (Exception ex) {
      TestUtil.logErr("Unexpected exception received:", ex);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception while rolling back TX:", re);
      }
    }
    if (!pass) {
      throw new Fault("convertExceptionDuringLoadTest failed");
    }
  }

  /*
   * @testName: convertsTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:3312; PERSISTENCE:JAVADOC:3313;
   * PERSISTENCE:JAVADOC:3314; PERSISTENCE:JAVADOC:3315;
   * PERSISTENCE:JAVADOC:3316; PERSISTENCE:SPEC:1482; PERSISTENCE:SPEC:1483;
   * PERSISTENCE:SPEC:1483.1; PERSISTENCE:SPEC:1484; PERSISTENCE:SPEC:1489.3;
   * 
   * @test_Strategy: Converter throws a RuntimeException during a persist
   */
  public void convertsTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    try {
      getEntityTransaction().begin();
      String street = "500.Oracle.Parkway";
      String city = "Redwood Shores";
      // 1 - MA , 2 - CA
      B b = new B("1", "name1", 1000, new Address(street, city, 1));
      getEntityManager().persist(b);
      getEntityManager().flush();
      getEntityTransaction().commit();
      clearCache();
      getEntityTransaction().begin();
      B b1 = getEntityManager().find(B.class, "1");
      TestUtil.logTrace("B:" + b1.toString());
      if (b1.getBValue().equals(1110)) {
        TestUtil.logTrace("Received expected value:" + b1.getBValue());
        pass1 = true;
      } else {
        TestUtil.logErr(
            "Converter was not properly applied, expected value:1002, actual"
                + b1.getBValue());
      }
      Address a = b1.getAddress();
      if (a.getStreet().equals(street.replace(".", "_"))) {
        TestUtil.logTrace("Received expected street:" + a.getStreet());
        pass2 = true;
      } else {
        TestUtil.logErr("Converter was not properly applied, expected street:"
            + street + ", actual:" + a.getStreet());
      }
      if (a.getState() == 1) {
        TestUtil.logTrace("Received expected state:" + a.getState());
        pass3 = true;
      } else {
        TestUtil.logErr(
            "Converter was not properly applied, expected state: 1, actual: "
                + a.getState());
      }
      getEntityTransaction().commit();

    } catch (Exception ex) {
      TestUtil.logErr("Unexpected exception received:", ex);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception while rolling back TX:", re);
      }
    }

    if (!pass1 || !pass2 || !pass3) {
      throw new Fault("convertsTest failed");
    }
  }

  /*
   * @testName: mappedSuperClassTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1490; PERSISTENCE:SPEC:1481;
   * PERSISTENCE:SPEC:1489.2; PERSISTENCE:SPEC:1957;
   * 
   * @test_Strategy: Test converter with property access and attributes from
   * MappedSuperClass
   */
  public void mappedSuperClassTest() throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      char[] c = { 'D', 'o', 'e' };
      FullTimeEmployee expected = new FullTimeEmployee(1, "John", c,
          "3#5#0#0#0.0");
      TestUtil.logTrace("Persisting FullTimeEmployee:" + expected.toString());
      getEntityManager().persist(expected);
      getEntityManager().flush();
      getEntityTransaction().commit();
      clearCache();
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "find the previously persisted Employees and verify them and their departments");
      FullTimeEmployee emp = getEntityManager().find(FullTimeEmployee.class,
          expected.getId());
      if (emp != null) {
        TestUtil.logTrace("Found FullTimeEmployee: " + emp.getId());
        expected.setSalary(expected.getSalary().replace("#", ""));
        c = new char[] { 'J', 'a', 'm', 'e', 's' };
        expected.setLastName(c);
        if (emp.equals(expected)) {
          TestUtil
              .logTrace("Received expected FullTimeEmployee:" + emp.toString());
          pass = true;
        } else {
          TestUtil.logErr(
              "Expected:" + expected.toString() + ", actual:" + emp.toString());
        }
      } else {
        TestUtil.logErr(
            "Find returned null FullTimeEmployee instead of:" + expected);
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred: ", e);
      pass = false;
    }
    if (!pass) {
      throw new Fault("mappedSuperClassTest failed");
    }
  }

  /*
   * @testName: mappedSuperClass2Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1488; PERSISTENCE:SPEC:1491;
   * PERSISTENCE:SPEC:1992;
   * 
   * @test_Strategy: Test overriding a converter
   */
  public void mappedSuperClass2Test() throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      FullTimeEmployee2 expected = new FullTimeEmployee2(1, "J.o.h.n", "Hill",
          "3500.0");
      TestUtil.logTrace("Persisting FullTimeEmployee2:" + expected.toString());
      getEntityManager().persist(expected);
      getEntityManager().flush();
      getEntityTransaction().commit();
      clearCache();
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "find the previously persisted Employees and verify them and their departments");
      FullTimeEmployee2 emp = getEntityManager().find(FullTimeEmployee2.class,
          expected.getId());
      if (emp != null) {
        TestUtil.logTrace("Found FullTimeEmployee2: " + emp.getId());
        expected.setFirstName(expected.getFirstName().replace(".", "#"));
        if (emp.equals(expected)) {
          TestUtil
              .logTrace("Received expected FullTimeEmployee:" + emp.toString());
          pass = true;
        } else {
          TestUtil.logErr(
              "Expected:" + expected.toString() + ", actual:" + emp.toString());
        }
      } else {
        TestUtil.logErr(
            "Find returned null FullTimeEmployee instead of:" + expected);
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred: ", e);
      pass = false;
    }
    if (!pass) {
      throw new Fault("mappedSuperClass2Test failed");
    }
  }

  /*
   * @testName: convert2Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1492; PERSISTENCE:SPEC:1494;
   * 
   * @test_Strategy: Test converters are applied when a JPQL or CriteriaQuery
   * reads from the DB
   */
  public void convert2Test() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    try {
      getEntityTransaction().begin();
      Employee expected = new Employee(1, "Alan", "Smith", "3#5#0#0#0.0");
      getEntityManager().persist(expected);
      getEntityManager().flush();
      getEntityTransaction().commit();
      pass1 = true;
    } catch (Exception ex) {
      TestUtil.logErr("Unexpected exception received:", ex);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception while rolling back TX:", re);
      }
    }
    clearCache();
    try {
      getEntityTransaction().begin();
      TestUtil.logMsg("Testing JPQL");
      List<Employee> o = getEntityManager()
          .createQuery("Select e from Employee e WHERE e.id = 1",
              Employee.class)
          .getResultList();

      if (o.size() == 1) {
        Employee emp = o.get(0);
        TestUtil.logTrace("Employee:" + emp.toString());
        if (emp.getSalary().equals("35000.0")) {
          TestUtil.logTrace("Received expected value:" + emp.getSalary());
          pass2 = true;
        } else {
          TestUtil.logErr(
              "Converter was not properly applied, expected value:35000.0, actual"
                  + emp.getSalary());
        }
      } else {
        TestUtil.logErr("Expected 1 entity to be returned, actual:" + o.size());
      }
      getEntityTransaction().commit();

    } catch (Exception ex) {
      TestUtil.logErr("Unexpected exception received:", ex);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception while rolling back TX:", re);
      }
    }
    clearCache();
    try {
      getEntityTransaction().begin();

      TestUtil.logMsg("Testing Criteria");
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
      CriteriaQuery<Employee> cquery = cbuilder.createQuery(Employee.class);
      if (cquery != null) {
        TestUtil.logTrace("Obtained Non-null Criteria Query");
        Root<Employee> employee = cquery.from(Employee.class);
        cquery.select(employee).where(cbuilder.equal(employee.get("id"), 1));
        Employee emp = getEntityManager().createQuery(cquery).getSingleResult();
        if (emp != null) {
          TestUtil.logTrace("Employee:" + emp.toString());
          if (emp.getSalary().equals("35000.0")) {
            TestUtil.logTrace("Received expected value:" + emp.getSalary());
            pass3 = true;
          } else {
            TestUtil.logErr(
                "Converter was not properly applied, expected value:35000.0, actual"
                    + emp.getSalary());
          }
        } else {
          TestUtil.logErr("Null Employee result was returned");
        }
      } else {
        TestUtil.logErr("createQuery returned null result");
      }
      getEntityTransaction().commit();

    } catch (Exception ex) {
      TestUtil.logErr("Unexpected exception received:", ex);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception while rolling back TX:", re);
      }
    }

    if (!pass1 || !pass2 || !pass3) {
      throw new Fault("converts2Test failed");
    }
  }

  /*
   * @testName: convert3Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1492; PERSISTENCE:SPEC:1493;
   * PERSISTENCE:SPEC:1597;
   * 
   * @test_Strategy: Test converters are applied when a JPQL or CriteriaQuery
   * write to the DB
   */
  public void convert3Test() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    try {
      getEntityTransaction().begin();
      Employee expected = new Employee(1, "Alan", "Smith", "0");
      getEntityManager().persist(expected);
      getEntityManager().flush();
      getEntityTransaction().commit();
      pass1 = true;
    } catch (Exception ex) {
      TestUtil.logErr("Unexpected exception received:", ex);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception while rolling back TX:", re);
      }
    }
    clearCache();
    try {
      getEntityTransaction().begin();
      TestUtil.logMsg("Testing JPQL");
      String expected = "54321.0";
      getEntityManager()
          .createQuery(
              "Update Employee e set e.salary='5#4#3#2#1.0'  WHERE e.id = 1")
          .executeUpdate();
      getEntityManager().flush();
      getEntityTransaction().commit();
      clearCache();
      getEntityTransaction().begin();
      Employee emp = getEntityManager().find(Employee.class, 1);
      if (emp != null) {
        TestUtil.logTrace("Found employee: " + emp.getId());
        if (emp.getSalary().equals(expected)) {
          TestUtil.logTrace("Received expected value:" + emp.getSalary());
          pass2 = true;
        } else {
          TestUtil.logErr("Converter was not properly applied, expected value:"
              + expected + ", actual" + emp.getSalary());
        }
      } else {
        TestUtil.logErr("Find returned null Employee");
      }
      getEntityTransaction().commit();

    } catch (Exception ex) {
      TestUtil.logErr("Unexpected exception received:", ex);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception while rolling back TX:", re);
      }
    }
    clearCache();
    try {
      getEntityTransaction().begin();

      TestUtil.logMsg("Testing Criteria");
      String expected = "3500.0";
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      CriteriaUpdate<Employee> cd = cbuilder
          .createCriteriaUpdate(Employee.class);
      Root<Employee> employee = cd.from(Employee.class);
      TestUtil.logTrace("Obtained expected root");
      cd.set("salary", "3#5#0#0.0");
      cd.where(cbuilder.equal(employee.get("id"), 1));
      int actual = getEntityManager().createQuery(cd).executeUpdate();
      getEntityManager().flush();
      getEntityTransaction().commit();
      if (actual == 1) {
        clearCache();
        getEntityTransaction().begin();
        Employee emp = getEntityManager().find(Employee.class, 1);
        if (emp != null) {
          TestUtil.logTrace("Found employee: " + emp.getId());
          if (emp.getSalary().equals("3500.0")) {
            TestUtil.logTrace("Received expected value:" + emp.getSalary());
            pass3 = true;
          } else {
            TestUtil
                .logErr("Converter was not properly applied, expected value:"
                    + expected + ", actual" + emp.getSalary());
          }
        } else {
          TestUtil.logErr("Find returned null Employee instead of:" + expected);
        }
        getEntityTransaction().commit();
      } else {
        TestUtil.logErr("Expected 1 update, actual:" + actual);
      }
    } catch (Exception ex) {
      TestUtil.logErr("Unexpected exception received:", ex);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception while rolling back TX:", re);
      }
    }

    if (!pass1 || !pass2 || !pass3) {
      throw new Fault("converts3Test failed");
    }
  }

  /*
   * @testName: embeddedTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1991; PERSISTENCE:SPEC:1994;
   * 
   * @test_Strategy: Test converter in an embedded entity
   */
  public void embeddedTest() throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      String name = "john smith";
      String country = "United States";
      Customer expected = new Customer("1", name, new Country(country, "USA"));
      TestUtil.logTrace("Persisting Customer:" + expected.toString());
      getEntityManager().persist(expected);
      getEntityManager().flush();
      getEntityTransaction().commit();
      clearCache();
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "find the previously persisted Customer and Country and verify them");
      Customer cust = getEntityManager().find(Customer.class, expected.getId());
      if (cust != null) {
        TestUtil.logTrace("Found Customer: " + cust.toString());
        String tmp = country.replace(" ", "_");
        if (cust.getCountry().getCountry().equals(tmp)) {
          TestUtil.logTrace(
              "Received expected Country:" + cust.getCountry().getCountry());
          pass = true;
        } else {
          TestUtil.logErr(
              "Expected:" + tmp + ", actual:" + cust.getCountry().getCountry());
        }
      } else {
        TestUtil.logErr("Find returned null Customer");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred: ", e);
      pass = false;
    }
    if (!pass) {
      throw new Fault("embeddedTest failed");
    }
  }

  /*
   * @testName: elementCollectionBasicType
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1994;
   * 
   * @test_Strategy: Test converter of an element collection of basic type
   */
  public void elementCollectionBasicType() throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      Customer expected = new Customer("1");
      List<String> phones = new ArrayList<String>();
      phones.add("781,442,2010");
      phones.add("781,442,2011");
      phones.add("781,442,2012");
      List<String> expectedphones = new ArrayList<String>();
      for (String p : phones) {
        expectedphones.add(p.replace(",", "-"));
      }

      expected.setPhones(phones);
      TestUtil.logTrace("Persisting Customer:" + expected.toString());
      getEntityManager().persist(expected);
      getEntityManager().flush();
      getEntityTransaction().commit();
      clearCache();
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "find the previously persisted Customer and Country and verify them");
      Customer cust = getEntityManager().find(Customer.class, expected.getId());
      if (cust != null) {
        TestUtil.logTrace("Found Customer: " + cust.toString());
        if (cust.getPhones().containsAll(expectedphones)
            && expectedphones.containsAll(cust.getPhones())
            && cust.getPhones().size() == expectedphones.size()) {
          TestUtil.logTrace("Received expected Phones:");
          for (String s : cust.getPhones()) {
            TestUtil.logTrace("phone:" + s);
          }
          pass = true;
        } else {
          TestUtil.logErr("Did not get expected results.");
          for (String s : expectedphones) {
            TestUtil.logTrace("expected:" + s);
          }
          TestUtil.logErr("actual:");
          for (String s : cust.getPhones()) {
            TestUtil.logTrace("actual:" + s);
          }
        }
      } else {
        TestUtil.logErr("Find returned null Customer");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred: ", e);
      pass = false;
    }
    if (!pass) {
      throw new Fault("elementCollectionBasicType failed");
    }
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("cleanup");
    removeTestData();
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
      getEntityManager().createNativeQuery("DELETE FROM EMPLOYEE")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM DEPARTMENT")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM B_EMBEDDABLE")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM CUST_TABLE")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM PHONES")
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
