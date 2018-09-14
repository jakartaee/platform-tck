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

package com.sun.ts.tests.jpa.core.annotations.orderby;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.CleanupMethod;
import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import java.util.*;

public class Client extends PMClientBase {

  List<Address> addrRef;

  Address addr1 = null;

  Address addr2 = null;

  Address addr3 = null;

  List<Address2> addrRef2;

  Address2 addr11 = null;

  Address2 addr12 = null;

  Address2 addr13 = null;

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
      createTestData();
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  public void setupAddress(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setupAddress");
    try {
      super.setup(args, p);
      removeAddressData();
      createAddressData();
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
   * @testName: orderByTest1
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1103; PERSISTENCE:SPEC:1104;
   * PERSISTENCE:SPEC:1106; PERSISTENCE:JAVADOC:145;
   * 
   * @test_Strategy: The OrderBy annotation specifies the ordering of the
   * elements of a collection valued association at the point when the
   * association is retrieved.
   * 
   * The property name must correspond to that of a persistenct property of the
   * associated class.
   *
   * The property used in the ordering must correspond to columns for which
   * comparison operations are supported.
   *
   * If DESC is specified, the elements will be ordered in descending order.
   * 
   * Retrieve the Collection using getter property accessor.
   */

  public void orderByTest1() throws Fault {

    TestUtil.logTrace("Begin orderByTest1");
    boolean pass1 = true;
    boolean pass2 = false;
    List resultsList = new ArrayList();
    final String[] expectedResult = new String[] { "Zoe", "Song", "Jie", "Ay" };

    try {
      getEntityTransaction().begin();

      Employee empChange = getEntityManager().find(Employee.class, 65);

      empChange.setFirstName("Ay");
      getEntityManager().merge(empChange);
      getEntityManager().flush();

      final Insurance newIns = getEntityManager().find(Insurance.class, 60);
      getEntityManager().refresh(newIns);

      final List insResult = newIns.getEmployees();

      if (insResult.size() != 4) {
        TestUtil.logTrace(
            "orderByTest1:  Did not get expected results.  Expected: 4, "
                + "got: " + insResult.size());
        pass1 = false;
      } else if (pass1) {
        Iterator i1 = insResult.iterator();
        TestUtil.logTrace("Check Employee Collection for expected first names");
        while (i1.hasNext()) {
          Employee e1 = (Employee) i1.next();
          resultsList.add((String) e1.getFirstName());
          TestUtil.logTrace("orderByTest1: got Employee FirstName:"
              + (String) e1.getFirstName());
        }

        TestUtil.logTrace(
            "Compare first names received with expected first names ");
        String[] result = (String[]) (resultsList
            .toArray(new String[resultsList.size()]));
        pass2 = Arrays.equals(expectedResult, result);

      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass2 = false;
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception in rollback:", re);
      }
    }

    if (!pass1 || !pass2)
      throw new Fault("orderByTest1 failed");
  }

  /*
   * @testName: orderByTest2
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1103; PERSISTENCE:SPEC:1104;
   * PERSISTENCE:SPEC:1106; PERSISTENCE:SPEC:1109; PERSISTENCE:SPEC:1110;
   * PERSISTENCE:JAVADOC:145; PERSISTENCE:SPEC:653
   * 
   * @test_Strategy: The OrderBy annotation specifies the ordering of the
   * elements of a collection valued association at the point when the
   * association is retrieved.
   *
   * The property name must correspond to that of a persistenct property of the
   * associated class.
   *
   * The property used in the ordering must correspond to columns for which
   * comparison operations are supported.
   *
   * If ASC is specified, the elements will be ordered in ascending order.
   * 
   * Retrieve the Collection using getter property accessor.
   */

  public void orderByTest2() throws Fault {

    TestUtil.logTrace("Begin orderByTest2");
    boolean pass1 = true;
    boolean pass2 = false;
    List resultsList = new ArrayList();
    final String[] expectedResult = new String[] { "Jie", "Song", "Yay",
        "Zoe" };

    try {
      getEntityTransaction().begin();

      Employee emp2Change = getEntityManager().find(Employee.class, 65);

      emp2Change.setFirstName("Yay");
      getEntityManager().merge(emp2Change);
      getEntityManager().flush();

      final Department newDept = getEntityManager().find(Department.class, 50);
      getEntityManager().refresh(newDept);

      final List deptResult = newDept.getEmployees();

      if (deptResult.size() != 4) {
        TestUtil.logTrace(
            "orderByTest2:  Did not get expected results.  Expected: 4, "
                + "got: " + deptResult.size());
        pass1 = false;
      } else if (pass1) {
        Iterator i2 = deptResult.iterator();
        TestUtil.logTrace("Check Employee Collection for expected first names");
        while (i2.hasNext()) {
          Employee e2 = (Employee) i2.next();
          resultsList.add((String) e2.getFirstName());
          TestUtil.logTrace("orderByTest2: got Employee FirstName:"
              + (String) e2.getFirstName());
        }

        TestUtil.logTrace(
            "Compare first names received with expected first names ");
        String[] result = (String[]) (resultsList
            .toArray(new String[resultsList.size()]));
        pass2 = Arrays.equals(expectedResult, result);

      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception in rollback:", re);
      }
    }

    if (!pass1 || !pass2)
      throw new Fault("orderByTest2 failed");
  }

  /*
   * @testName: orderByTest3
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1103; PERSISTENCE:SPEC:1104;
   * PERSISTENCE:SPEC:1106; PERSISTENCE:JAVADOC:145
   * 
   * @test_Strategy: The OrderBy annotation specifies the ordering of the
   * elements of a collection valued association at the point when the
   * association is retrieved.
   *
   * If DESC is specified, the elements will be ordered in descending order.
   * 
   * Add to the Collection then retrieve the updated Collection and ensure the
   * list is ordered.
   * 
   */

  public void orderByTest3() throws Fault {
    TestUtil.logTrace("Begin orderByTest3");
    boolean pass1 = true;
    boolean pass2 = false;
    List insResult;
    List resultsList = new ArrayList();
    final String[] expectedResult = new String[] { "Zoe", "Song", "Penelope",
        "May", "Jie" };

    try {
      getEntityTransaction().begin();

      Employee emp3Change = getEntityManager().find(Employee.class, 85);
      Insurance ins = getEntityManager().find(Insurance.class, 60);

      emp3Change.setInsurance(ins);
      getEntityManager().merge(emp3Change);
      ins.getEmployees().add(emp3Change);
      getEntityManager().merge(ins);
      getEntityManager().flush();

      getEntityManager().refresh(ins);

      insResult = ins.getEmployees();

      if (insResult.size() != 5) {
        TestUtil.logErr("orderByTest3: Expected List Size of 5 " + "got: "
            + insResult.size());
        pass1 = false;
      } else if (pass1) {
        Iterator i3 = insResult.iterator();
        TestUtil.logTrace("Check Employee Collection for expected first names");
        while (i3.hasNext()) {
          Employee e3 = (Employee) i3.next();
          resultsList.add((String) e3.getFirstName());
          TestUtil.logTrace("orderByTest3: got Employee FirstName:"
              + (String) e3.getFirstName());
        }

        TestUtil.logTrace(
            "orderByTest3: Expected size received, check ordering . . .");
        String[] result = (String[]) (resultsList
            .toArray(new String[resultsList.size()]));
        pass2 = Arrays.equals(expectedResult, result);

      }

      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception in rollback:", re);
      }
    }

    if (!pass1 || !pass2)
      throw new Fault("orderByTest3 failed");
  }

  /*
   * @testName: orderByTest4
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1103; PERSISTENCE:SPEC:1104;
   * PERSISTENCE:SPEC:1106; PERSISTENCE:JAVADOC:145
   * 
   * @test_Strategy: The OrderBy annotation specifies the ordering of the
   * elements of a collection valued association at the point when the
   * association is retrieved.
   * 
   * If ASC is specified, the elements will be ordered in ascending order.
   * 
   * Retrieve the Collection, add to the Collection and retrieve it again making
   * sure the list is ordered .
   * 
   */

  public void orderByTest4() throws Fault {
    TestUtil.logTrace("Begin orderByTest4");
    boolean pass1 = true;
    boolean pass2 = false;
    List resultsList = new ArrayList();
    final String[] expectedResult = new String[] { "Jie", "May", "Penelope",
        "Song", "Zoe" };

    try {
      getEntityTransaction().begin();

      Employee emp4Change = getEntityManager().find(Employee.class, 85);
      Department dept = getEntityManager().find(Department.class, 50);

      emp4Change.setDepartment(dept);
      getEntityManager().merge(emp4Change);
      dept.getEmployees().add(emp4Change);
      getEntityManager().merge(dept);
      getEntityManager().flush();

      getEntityManager().refresh(dept);
      final List deptResult = dept.getEmployees();

      if (deptResult.size() != 5) {
        TestUtil.logErr("orderByTest4: Expected Collection Size of 5 " + "got: "
            + deptResult.size());
        pass1 = false;
      } else if (pass1) {
        Iterator i4 = deptResult.iterator();
        TestUtil.logTrace("Check Employee Collection for expected first names");
        while (i4.hasNext()) {
          Employee e4 = (Employee) i4.next();
          resultsList.add((String) e4.getFirstName());
          TestUtil.logTrace("orderByTest4: got Employee FirstName:"
              + (String) e4.getFirstName());
        }

        TestUtil.logTrace(
            "orderByTest4: Expected size received, check ordering . . .");
        String[] result = (String[]) (resultsList
            .toArray(new String[resultsList.size()]));
        pass2 = Arrays.equals(expectedResult, result);
      }

      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception in rollback:", re);
      }
    }

    if (!pass1 || !pass2)
      throw new Fault("orderByTest4 failed");
  }

  /*
   * @testName: propertyDotNotationTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:2089; PERSISTENCE:SPEC:2092;
   * PERSISTENCE:SPEC:2091;
   * 
   * @test_Strategy: identifier is the name of the respective property
   *
   */
  @SetupMethod(name = "setupAddress")
  @CleanupMethod(name = "cleanupAddress")
  public void propertyDotNotationTest() throws Fault {
    boolean pass = false;

    try {
      addrRef = new ArrayList<Address>();
      List<Address> expected = new ArrayList<Address>();
      expected.add(addr2);
      expected.add(addr3);
      expected.add(addr1);

      clearCache();
      A a = getEntityManager().find(A.class, "1");

      List<Address> actual = a.getAddressList();

      if (actual.size() == expected.size()) {
        int count = 0;
        for (int i = 0; i < expected.size(); i++) {
          TestUtil.logTrace("Testing - expected[" + expected.get(i)
              + "], actual[" + actual.get(i) + "]");

          if (expected.get(i).equals(actual.get(i))) {
            count++;
          }
        }
        if (count == expected.size()) {
          pass = true;
        } else {
          TestUtil
              .logErr("count=" + count + ", expected size:" + expected.size());
          for (Address aa : expected) {
            TestUtil.logErr("expected:" + aa);
          }
          TestUtil.logErr("------------");
          for (Address aa : actual) {
            TestUtil.logErr("actual:" + aa);
          }
        }
      } else {
        TestUtil.logErr("Expected list size:" + expected.size()
            + ", actual size:" + actual.size());
        for (Address aa : expected) {
          TestUtil.logErr("expected:" + aa);
        }
        TestUtil.logErr("------------");
        for (Address aa : actual) {
          TestUtil.logErr("actual:" + aa);
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    if (!pass) {
      throw new Fault("propertyDotNotationTest failed");
    }
  }

  /*
   * @testName: fieldDotNotationTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:2089; PERSISTENCE:SPEC:2092;
   * PERSISTENCE:SPEC:2091;
   * 
   * @test_Strategy: identifier is the name of the respective field
   *
   */
  @SetupMethod(name = "setupAddress")
  @CleanupMethod(name = "cleanupAddress")
  public void fieldDotNotationTest() throws Fault {
    boolean pass = false;

    try {
      addrRef = new ArrayList<Address>();
      List<Address2> expected = new ArrayList<Address2>();
      expected.add(addr12);
      expected.add(addr13);
      expected.add(addr11);

      TestUtil.logTrace("Clearing the cache");
      clearCache();
      A2 a = getEntityManager().find(A2.class, "2");

      List<Address2> actual = a.getAddressList();

      if (actual.size() == expected.size()) {
        int count = 0;
        for (int i = 0; i < expected.size(); i++) {
          TestUtil.logTrace("Testing - expected[" + expected.get(i)
              + "], actual[" + actual.get(i) + "]");

          if (expected.get(i).equals(actual.get(i))) {
            count++;
          }
        }

        if (count == expected.size()) {
          pass = true;
        } else {
          TestUtil.logTrace(
              "count=" + count + ", expected size:" + expected.size());
          for (Address2 aa : expected) {
            TestUtil.logErr("expected:" + aa);
          }
          TestUtil.logErr("------------");
          for (Address2 aa : actual) {
            TestUtil.logErr("actual:" + aa);
          }
        }
      } else {
        TestUtil.logErr("Expected list size:" + expected.size()
            + ", actual size:" + actual.size());
        for (Address2 aa : expected) {
          TestUtil.logErr("expected:" + aa);
        }
        TestUtil.logErr("------------");
        for (Address2 aa : actual) {
          TestUtil.logErr("actual:" + aa);
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    if (!pass) {
      throw new Fault("propertyDotNotationTest failed");
    }
  }

  /*
   * @testName: propertyElementCollectionBasicType
   * 
   * @assertion_ids: PERSISTENCE:SPEC:2090
   * 
   * @test_Strategy: ElementCollection of a basic type
   */
  @SetupMethod(name = "setupCust")
  @CleanupMethod(name = "cleanupCust")
  public void propertyElementCollectionBasicType() throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      Customer expected = new Customer("1");
      List<String> expectedphones = new ArrayList<String>();
      expectedphones.add("781-442-2010");
      expectedphones.add("781-442-2011");
      expectedphones.add("781-442-2012");

      expected.setPhones(expectedphones);
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
            TestUtil.logErr("expected:" + s);
          }
          TestUtil.logErr("actual:");
          for (String s : cust.getPhones()) {
            TestUtil.logErr("actual:" + s);
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
      throw new Fault("propertyElementCollectionBasicType failed");
    }
  }

  /*
   * @testName: fieldElementCollectionBasicType
   * 
   * @assertion_ids: PERSISTENCE:SPEC:2090
   * 
   * @test_Strategy: ElementCollection of a basic type
   */
  @SetupMethod(name = "setupCust")
  @CleanupMethod(name = "cleanupCust")
  public void fieldElementCollectionBasicType() throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      Customer2 expected = new Customer2("2");
      List<String> expectedphones = new ArrayList<String>();
      expectedphones.add("781-442-2010");
      expectedphones.add("781-442-2011");
      expectedphones.add("781-442-2012");

      expected.setPhones(expectedphones);
      TestUtil.logTrace("Persisting Customer2:" + expected.toString());
      getEntityManager().persist(expected);
      getEntityManager().flush();
      getEntityTransaction().commit();
      clearCache();
      getEntityTransaction().begin();
      Customer2 cust = getEntityManager().find(Customer2.class,
          expected.getId());
      if (cust != null) {
        TestUtil.logTrace("Found Customer2: " + cust.toString());
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
            TestUtil.logErr("expected:" + s);
          }
          TestUtil.logErr("actual:");
          for (String s : cust.getPhones()) {
            TestUtil.logErr("actual:" + s);
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
      throw new Fault("fieldElementCollectionBasicType failed");
    }
  }

  private void createTestData() throws Exception {
    try {
      TestUtil.logTrace("createTestData");
      getEntityTransaction().begin();
      final float salary = 10000.00F;

      Department d1 = new Department(50, "SJSAS Appserver");
      getEntityManager().persist(d1);

      Insurance s1 = new Insurance(60, "United");
      getEntityManager().persist(s1);

      final Employee e1 = new Employee(70, "Jie", "Leng", salary, d1, s1);
      final Employee e2 = new Employee(80, "Zoe", "Leng", salary, d1, s1);
      final Employee e3 = new Employee(90, "Song", "Leng", salary, d1, s1);
      final Employee e4 = new Employee(65, "May", "Leng", salary, d1, s1);
      final Employee e5 = new Employee(85, "Penelope", "Leng", salary);
      getEntityManager().persist(e1);
      getEntityManager().persist(e2);
      getEntityManager().persist(e3);
      getEntityManager().persist(e4);
      getEntityManager().persist(e5);

      List<Employee> link = new ArrayList<Employee>();
      link.add(e1);
      link.add(e2);
      link.add(e3);
      link.add(e4);

      d1.setEmployees(link);
      getEntityManager().merge(d1);

      s1.setEmployees(link);
      getEntityManager().merge(s1);

      TestUtil.logTrace("persisted Entity Data");
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

  private void createAddressData() throws Exception {
    try {
      TestUtil.logTrace("createAddressData");
      getEntityTransaction().begin();

      addr1 = new Address("1 Network Drive", "Burlington", "MA",
          new ZipCode("01801"));
      addr2 = new Address("634 Goldstar Road", "Peabody", "MA",
          new ZipCode("88444"));
      addr3 = new Address("3212 Boston Road", "Chelmsford", "MA",
          new ZipCode("01824"));
      addrRef = new ArrayList<Address>();
      addrRef.add(addr1);
      addrRef.add(addr2);
      addrRef.add(addr3);
      A a1 = new A("1", "b1", addrRef);

      addr11 = new Address2("1 Network Drive", "Burlington", "MA",
          new ZipCode2("01801"));
      addr12 = new Address2("634 Goldstar Road", "Peabody", "MA",
          new ZipCode2("88444"));
      addr13 = new Address2("3212 Boston Road", "Chelmsford", "MA",
          new ZipCode2("01824"));
      addrRef2 = new ArrayList<Address2>();
      addrRef2.add(addr11);
      addrRef2.add(addr12);
      addrRef2.add(addr13);
      A2 a2 = new A2("2", "b2", addrRef2);

      getEntityManager().persist(a1);
      getEntityManager().persist(a2);

      getEntityManager().flush();
      getEntityManager().refresh(a1);
      getEntityManager().refresh(a2);

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

  public void cleanupAddress() throws Fault {
    TestUtil.logTrace("cleanup");
    removeAddressData();
    TestUtil.logTrace("cleanup complete, calling super.cleanup");
    super.cleanup();
  }

  public void cleanupCust() throws Fault {
    TestUtil.logTrace("cleanup");
    removeCustTestData();
    TestUtil.logTrace("cleanup complete, calling super.cleanup");
    super.cleanup();
  }

  private void removeCustTestData() {
    TestUtil.logTrace("removeCustTestData");
    if (getEntityTransaction().isActive()) {
      getEntityTransaction().rollback();
    }
    try {
      getEntityTransaction().begin();
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

  private void removeTestData() {
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
      getEntityManager().createNativeQuery("Delete from INSURANCE")
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

  private void removeAddressData() {
    TestUtil.logTrace("removeAddressData");
    if (getEntityTransaction().isActive()) {
      getEntityTransaction().rollback();
    }
    try {
      getEntityTransaction().begin();
      getEntityManager().createNativeQuery("Delete from COLTAB_ADDRESS")
          .executeUpdate();
      getEntityManager().createNativeQuery("Delete from COLTAB")
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
