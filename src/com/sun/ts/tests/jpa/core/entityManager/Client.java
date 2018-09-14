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

package com.sun.ts.tests.jpa.core.entityManager;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.CleanupMethod;
import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.Metamodel;
import java.util.*;

public class Client extends PMClientBase {

  List<Employee> empRef = new ArrayList<Employee>();

  Employee emp0 = null;

  Order[] orders = new Order[5];

  Properties props = null;

  Map map = new HashMap<String, Object>();

  String dataBaseName = null;

  final static String ORACLE = "oracle";

  final static String POSTGRESQL = "postgresql";

  public Client() {
  }

  public static void main(String[] args) {
    Client theTests = new Client();

    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * setup() is called before each test
   *
   * @class.setup_props: jdbc.db;
   */
  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    this.props = p;
    try {
      super.setup(args, p);
      map.putAll(getEntityManager().getProperties());
      map.put("foo", "bar");
      displayMap(map);
      dataBaseName = p.getProperty("jdbc.db");
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  /*
   * setupOrderData() is called before each test
   *
   * @class.setup_props: jdbc.db;
   */
  public void setupOrderData(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setupOrderData");
    this.props = p;
    try {
      super.setup(args, p);
      removeTestData();
      createOrderData();
      map.putAll(getEntityManager().getProperties());
      map.put("foo", "bar");
      displayMap(map);
      dataBaseName = p.getProperty("jdbc.db");
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  /*
   * setupEmployeeData() is called before each test
   *
   * @class.setup_props: jdbc.db;
   */
  public void setupEmployeeData(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setupOrderData");
    this.props = p;
    try {
      super.setup(args, p);
      removeTestData();
      createEmployeeData();
      map.putAll(getEntityManager().getProperties());
      map.put("foo", "bar");
      displayMap(map);
      dataBaseName = p.getProperty("jdbc.db");
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("cleanup complete, calling super.cleanup");
    super.cleanup();
  }

  public void cleanupData() throws Fault {
    TestUtil.logTrace("Cleanup data");
    removeTestData();
    cleanup();
  }

  public List<List> getResultSetsFromStoredProcedure(StoredProcedureQuery spq) {
    TestUtil.logTrace("in getResultSetsFromStoredProcedure");
    boolean results = true;
    List<List> listOfList = new ArrayList<List>();
    int rsnum = 1;
    int rowsAffected = 0;

    do {
      if (results) {
        TestUtil.logTrace("Processing set:" + rsnum);
        List<Employee> empList = new ArrayList<Employee>();
        List list = spq.getResultList();
        if (list != null) {
          TestUtil.logTrace(
              "Getting result set: " + (rsnum) + ", size:" + list.size());
          for (Object o : list) {
            if (o instanceof Employee) {
              Employee e = (Employee) o;
              TestUtil.logTrace("Saving:" + e);
              empList.add(e);
            } else {
              TestUtil.logErr("Did not get instance of Employee, instead got:"
                  + o.getClass().getName());
            }
          }
          if (empList.size() > 0) {
            listOfList.add(empList);
          }
        } else {
          TestUtil.logErr("Result set[" + rsnum + "] returned was null");
        }
        rsnum++;
      } else {
        rowsAffected = spq.getUpdateCount();
        if (rowsAffected >= 0)
          TestUtil.logTrace("rowsAffected:" + rowsAffected);
      }
      results = spq.hasMoreResults();
      TestUtil.logTrace("Results:" + results);

    } while (results || rowsAffected != -1);
    return listOfList;
  }

  public boolean verifyListOfListEmployeeIds(List<Integer> expected,
      List<List> listOfList) {
    boolean result = false;
    int count = 0;
    for (List<Employee> lEmp : listOfList) {

      if (lEmp.size() > 0) {
        List<Integer> actual = new ArrayList<Integer>();
        for (Employee e : lEmp) {
          actual.add(e.getId());
        }

        if (expected.containsAll(actual) && actual.containsAll(expected)
            && expected.size() == actual.size()) {
          TestUtil.logTrace("Received expected result:");
          for (Integer a : actual) {
            TestUtil.logTrace("id:" + a);
          }
          count++;
        } else {
          TestUtil.logErr("Did not receive expected result:");
          for (Integer e : expected) {
            TestUtil.logErr(" Expected id:" + e);
          }
          for (Integer a : actual) {
            TestUtil.logErr("Actual id:" + a);
          }
        }

      } else {
        TestUtil.logErr("Result set that was returned had 0 length");
      }

    }
    if (count == listOfList.size()) {
      result = true;
    }
    return result;
  }

  public boolean verifyListOfListEmployees(List<Employee> expected,
      List<List> listOfList) {
    boolean result = false;
    int count = 0;
    for (List<Employee> lEmp : listOfList) {

      if (lEmp.size() > 0) {
        List<Employee> actual = new ArrayList<Employee>();
        for (Employee e : lEmp) {
          actual.add(e);
        }
        if (verifyListEmployees(expected, actual)) {
          count++;
        }
      } else {
        TestUtil.logErr("Result set that was returned had 0 length");
      }
    }
    if (count == listOfList.size()) {
      result = true;
    }
    return result;
  }

  public boolean verifyListEmployees(List<Employee> expected,
      List<Employee> actual) {
    boolean result = false;
    if (expected.containsAll(actual) && actual.containsAll(expected)
        && expected.size() == actual.size()) {
      for (Employee e : expected) {
        TestUtil.logTrace("Received expected result:" + e);
      }
      result = true;
    } else {
      TestUtil.logErr("Did not receive expected result:");
      for (Employee e : expected) {
        TestUtil.logErr("expected employee:" + e);
      }
      for (Employee e : actual) {
        TestUtil.logErr("actual employee :" + e);
      }
    }
    return result;
  }

  /*
   * @testName: mergeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:660
   * 
   * @test_Strategy: Merge new entity
   */
  public void mergeTest() throws Fault {
    boolean pass = false;

    try {
      getEntityTransaction().begin();
      Order o1 = getEntityManager().merge(new Order(9, 999, "desc999"));
      getEntityTransaction().commit();
      clearCache();
      Order o2 = getEntityManager().find(Order.class, 9);
      if (o1.equals(o2)) {
        TestUtil.logTrace("Received expected results");
        pass = true;
      } else {
        TestUtil.logErr("Did not get expected results - expected:"
            + o1.toString() + ", actual:" + o2.toString());
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass) {
      throw new Fault("mergeTest failed");
    }
  }

  /*
   * @testName: mergeExceptionsTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:504
   * 
   * @test_Strategy: Call EntityManager.merge() method
   */
  public void mergeExceptionsTest() throws Fault {
    boolean pass = false;

    TestUtil.logMsg("Testing merge(Object");

    TestUtil.logMsg("Testing invalid object ");
    try {
      getEntityTransaction().begin();
      getEntityManager().merge(this);
      getEntityTransaction().commit();
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
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

    TestUtil.logMsg("Testing removed entity ");
    try {
      getEntityTransaction().begin();
      Order o = getEntityManager().find(Order.class, 1);
      getEntityManager().remove(o);
      getEntityTransaction().commit();

      getEntityTransaction().begin();
      getEntityManager().merge(o);
      getEntityTransaction().commit();
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
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

    if (!pass) {
      throw new Fault("mergeExceptionsTest failed");
    }
  }

  /*
   * @testName: persistExceptionsTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:31; PERSISTENCE:JAVADOC:506;
   * PERSISTENCE:JAVADOC:507; PERSISTENCE:SPEC:618.1; PERSISTENCE:SPEC:618.2
   * 
   * @test_Strategy: Call EntityManager.persist()
   */
  @SetupMethod(name = "setupOrderData")
  @CleanupMethod(name = "cleanupData")
  public void persistExceptionsTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    TestUtil.logMsg("Testing persisting an entity twice ");

    try {
      getEntityManager().detach(orders[0]);
      getEntityTransaction().begin();
      TestUtil.logTrace("Try to persist an existing Order");
      getEntityManager().persist(orders[0]);
      getEntityManager().flush();
      getEntityTransaction().commit();

      TestUtil.logErr("A PersistenceException was not thrown");
    } catch (EntityExistsException eee) {
      TestUtil.logTrace("EntityExistsException Caught as Expected:", eee);
      pass1 = true;
    } catch (PersistenceException pe) {
      TestUtil.logTrace("A PersistentException was caught:", pe);
      pass1 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception in while rolling back TX:", re);
      }
    }

    TestUtil.logMsg("Testing non-entity ");
    try {
      getEntityTransaction().begin();
      getEntityManager().persist(this);
      TestUtil.logErr("IllegalArgumentException was not thrown");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("IllegalArgumentException caught as expected");
      pass2 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception in while rolling back TX:", re);
      }
    }

    if (!pass1 || !pass2) {
      throw new Fault("persistExceptionsTest failed");
    }
  }

  /*
   * @testName: removeExceptionsTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:527
   * 
   * @test_Strategy: Call EntityManager.remove() method
   */
  public void removeExceptionsTest() throws Fault {
    boolean pass = false;
    TestUtil.logMsg("Testing findClassObjectIllegalStateException");

    TestUtil.logMsg("Invalid Object test");
    try {
      getEntityTransaction().begin();
      getEntityManager().remove(this);
      getEntityTransaction().commit();
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("IllegalArgumentException Caught as Expected.");
      pass = true;
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
    if (!pass) {
      throw new Fault("removeExceptionsTest failed");
    }
  }

  /*
   * @testName: lockIllegalStateExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:490; PERSISTENCE:JAVADOC:497;
   * 
   * @test_Strategy: Call EntityManager.lock() method
   */
  public void lockIllegalStateExceptionTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    Map<String, Object> myMap = new HashMap<String, Object>();
    myMap.put("some.cts.specific.property", "nothing.in.particular");

    TestUtil.logMsg("Testing invalid object for lock(Object, LockModeType)");
    try {
      getEntityTransaction().begin();
      getEntityManager().lock(this, LockModeType.WRITE);
      getEntityTransaction().commit();
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass1 = true;
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

    TestUtil
        .logMsg("Testing invalid object for lock(Object, LockModeType, Map)");
    try {
      getEntityTransaction().begin();
      getEntityManager().lock(this, LockModeType.WRITE, myMap);
      getEntityTransaction().commit();
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass2 = true;
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

    if (!pass1 || !pass2) {
      throw new Fault("lockIllegalStateExceptionTest failed");
    }
  }

  /*
   * @testName: refreshInvalidObjectIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:509
   * 
   * @test_Strategy: Call EntityManager.refresh() method
   */
  public void refreshInvalidObjectIllegalArgumentExceptionTest() throws Fault {
    boolean pass = false;

    try {
      getEntityTransaction().begin();
      getEntityManager().refresh(this);
      getEntityTransaction().commit();
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
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

    if (!pass) {
      throw new Fault(
          "refreshInvalidObjectIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: refreshNonManagedObjectIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:509
   *
   * @test_Strategy: Call EntityManager.refresh() method
   */
  public void refreshNonManagedObjectIllegalArgumentExceptionTest()
      throws Fault {
    boolean pass = false;

    try {
      getEntityTransaction().begin();
      getEntityManager().refresh(new Order(99, 999));
      getEntityTransaction().commit();
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
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

    if (!pass) {
      throw new Fault(
          "refreshNonManagedObjectIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: refreshRemovedObjectEntityNotFoundExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:511
   *
   * @test_Strategy: Call EntityManager.refresh() method
   */
  @SetupMethod(name = "setupOrderData")
  @CleanupMethod(name = "cleanupData")
  public void refreshRemovedObjectEntityNotFoundExceptionTest() throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("Finding Order");
      Order o = getEntityManager().find(Order.class, 1);
      TestUtil.logTrace("Removing all data");
      getEntityManager().createNativeQuery("DELETE FROM PURCHASE_ORDER")
          .executeUpdate();
      TestUtil.logTrace("Refreshing previous order");
      getEntityManager().refresh(o);
      getEntityTransaction().commit();
      TestUtil.logErr("EntityNotFoundException not thrown");
    } catch (EntityNotFoundException e) {
      TestUtil.logTrace("EntityNotFoundException Caught as Expected.");
      pass = true;
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

    if (!pass) {
      throw new Fault("refreshRemovedObjectEntityNotFoundExceptionTest failed");
    }
  }

  /*
   * @testName: refreshInvalidObjectMapIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:512
   *
   * @test_Strategy: Call EntityManager.refresh() method
   */
  public void refreshInvalidObjectMapIllegalArgumentExceptionTest()
      throws Fault {
    boolean pass = false;
    Map<String, Object> myMap = new HashMap<String, Object>();
    myMap.put("some.cts.specific.property", "nothing.in.particular");
    try {
      getEntityTransaction().begin();
      getEntityManager().refresh(this, myMap);
      getEntityTransaction().commit();
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
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

    if (!pass) {
      throw new Fault(
          "refreshInvalidObjectMapIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: refreshNonManagedObjectMapIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:512
   *
   * @test_Strategy: Call EntityManager.refresh() method
   */
  public void refreshNonManagedObjectMapIllegalArgumentExceptionTest()
      throws Fault {
    boolean pass = false;
    Map<String, Object> myMap = new HashMap<String, Object>();
    myMap.put("some.cts.specific.property", "nothing.in.particular");
    try {
      getEntityTransaction().begin();
      getEntityManager().refresh(new Order(99, 999), myMap);
      getEntityTransaction().commit();
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
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

    if (!pass) {
      throw new Fault(
          "refreshNonManagedObjectMapIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: refreshRemovedObjectMapEntityNotFoundExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:514;
   *
   * @test_Strategy: Call EntityManager.refresh() method
   */
  @SetupMethod(name = "setupOrderData")
  @CleanupMethod(name = "cleanupData")
  public void refreshRemovedObjectMapEntityNotFoundExceptionTest()
      throws Fault {
    boolean pass = false;
    Map<String, Object> myMap = new HashMap<String, Object>();
    myMap.put("some.cts.specific.property", "nothing.in.particular");
    try {
      getEntityTransaction().begin();
      Order o = getEntityManager().find(Order.class, 2);
      TestUtil.logTrace("Removing all data");
      getEntityManager().createNativeQuery("DELETE FROM PURCHASE_ORDER")
          .executeUpdate();
      TestUtil.logTrace("Refreshing previous order");
      getEntityManager().refresh(o, myMap);
      getEntityTransaction().commit();
      TestUtil.logErr("EntityNotFoundException not thrown");
    } catch (EntityNotFoundException e) {
      TestUtil.logTrace("EntityNotFoundException Caught as Expected.");
      pass = true;
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

    if (!pass) {
      throw new Fault(
          "refreshRemovedObjectMapEntityNotFoundExceptionTest failed");
    }
  }

  /*
   * @testName: refreshInvalidObjectLockModeTypeIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:515
   *
   * @test_Strategy: Call EntityManager.refresh() method
   */
  public void refreshInvalidObjectLockModeTypeIllegalArgumentExceptionTest()
      throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      getEntityManager().refresh(this, LockModeType.WRITE);
      getEntityTransaction().commit();
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
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

    if (!pass) {
      throw new Fault(
          "refreshInvalidObjectLockModeTypeIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: refreshNonManagedObjectLockModeTypeIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:515
   *
   * @test_Strategy: Call EntityManager.refresh() method
   */
  public void refreshNonManagedObjectLockModeTypeIllegalArgumentExceptionTest()
      throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      getEntityManager().refresh(new Order(99, 999), LockModeType.WRITE);
      getEntityTransaction().commit();
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
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

    if (!pass) {
      throw new Fault(
          "refreshNonManagedObjectLockModeTypeIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: refreshRemovedObjectLockModeTypeEntityNotFoundExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:517
   *
   * @test_Strategy: Call EntityManager.refresh() method
   */
  @SetupMethod(name = "setupOrderData")
  @CleanupMethod(name = "cleanupData")
  public void refreshRemovedObjectLockModeTypeEntityNotFoundExceptionTest()
      throws Fault {
    boolean pass = false;

    try {
      getEntityTransaction().begin();
      Order o = getEntityManager().find(Order.class, 3);
      TestUtil.logTrace("Removing all data");
      getEntityManager().createNativeQuery("DELETE FROM PURCHASE_ORDER")
          .executeUpdate();
      getEntityManager().refresh(o, LockModeType.PESSIMISTIC_READ);
      TestUtil.logTrace("Refreshing previous order");
      getEntityManager().refresh(o, LockModeType.PESSIMISTIC_READ);
      getEntityTransaction().commit();
      TestUtil.logErr("EntityNotFoundException not thrown");
    } catch (EntityNotFoundException e) {
      TestUtil.logTrace("EntityNotFoundException Caught as Expected.");
      pass = true;
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

    if (!pass) {
      throw new Fault(
          "refreshRemovedObjectLockModeTypeEntityNotFoundExceptionTest failed");
    }
  }

  /*
   * @testName: refreshInvalidObjectLockModeTypeMapIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:521
   *
   * @test_Strategy: Call EntityManager.refresh() method
   */
  public void refreshInvalidObjectLockModeTypeMapIllegalArgumentExceptionTest()
      throws Fault {
    boolean pass = false;
    Map<String, Object> myMap = new HashMap<String, Object>();
    myMap.put("some.cts.specific.property", "nothing.in.particular");
    try {
      getEntityTransaction().begin();
      getEntityManager().refresh(this, LockModeType.WRITE, myMap);
      getEntityTransaction().commit();
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
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

    if (!pass) {
      throw new Fault(
          "refreshInvalidObjectLockModeTypeMapIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName:
   * refreshNonManagedObjectLockModeTypeMapIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:521
   *
   * @test_Strategy: Call EntityManager.refresh() method
   */
  public void refreshNonManagedObjectLockModeTypeMapIllegalArgumentExceptionTest()
      throws Fault {
    boolean pass = false;
    Map<String, Object> myMap = new HashMap<String, Object>();
    myMap.put("some.cts.specific.property", "nothing.in.particular");
    try {
      getEntityTransaction().begin();
      getEntityManager().refresh(new Order(99, 999), LockModeType.WRITE, myMap);
      getEntityTransaction().commit();
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
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

    if (!pass) {
      throw new Fault(
          "refreshNonManagedObjectLockModeTypeMapIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: refreshRemovedObjectLockModeTypeMapEntityNotFoundExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:523
   *
   * @test_Strategy: Call EntityManager.refresh() method
   */
  @SetupMethod(name = "setupOrderData")
  @CleanupMethod(name = "cleanupData")
  public void refreshRemovedObjectLockModeTypeMapEntityNotFoundExceptionTest()
      throws Fault {
    boolean pass = false;
    Map<String, Object> myMap = new HashMap<String, Object>();
    myMap.put("some.cts.specific.property", "nothing.in.particular");
    try {
      getEntityTransaction().begin();
      Order o = getEntityManager().find(Order.class, 4);
      TestUtil.logTrace("Removing all data");
      getEntityManager().createNativeQuery("DELETE FROM PURCHASE_ORDER")
          .executeUpdate();
      getEntityManager().refresh(o, LockModeType.PESSIMISTIC_READ, myMap);
      TestUtil.logTrace("Refreshing previous order");
      getEntityManager().refresh(o, LockModeType.PESSIMISTIC_READ, myMap);
      getEntityTransaction().commit();
      TestUtil.logErr("EntityNotFoundException not thrown");
    } catch (EntityNotFoundException e) {
      TestUtil.logTrace("EntityNotFoundException Caught as Expected.");
      pass = true;
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

    if (!pass) {
      throw new Fault(
          "refreshRemovedObjectLockModeTypeMapEntityNotFoundExceptionTest failed");
    }
  }

  /*
   * @testName: containsIllegalArgumentException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:458
   * 
   * @test_Strategy: Call EntityManager.contains() method passing an Object that
   * is not an Entity
   */
  public void containsIllegalArgumentException() throws Fault {
    boolean pass = false;

    try {
      getEntityTransaction().begin();

      getEntityManager().contains("notanentity");
      TestUtil.logErr("IllegalArgumentException not thrown");
      getEntityTransaction().commit();
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("IllegalArgumentException Caught as Expected.");
      pass = true;
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

    if (!pass) {
      throw new Fault("containsIllegalArgumentException failed");
    }
  }

  /*
   * @testName: createNamedQueryIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:459; PERSISTENCE:JAVADOC:460
   * 
   * @test_Strategy: Call EntityManager.createNamedQuery() with a query string
   * that is invalid, verify IllegalArgumentException is thrown Call
   * EntityManager.createNamedQuery() with a TypedQuery string that is invalid,
   * verify IllegalArgumentException is thrown. Call
   * EntityManager.createNamedQuery() with a TypedQuery string with a result
   * type not assignable to the specified type, verify IllegalArgumentException
   * is thrown.
   */
  public void createNamedQueryIllegalArgumentExceptionTest() throws Fault {
    boolean pass1 = false, pass2 = false;
    TestUtil.logMsg("Testing TypedQuery version");

    try {
      getEntityManager().createNamedQuery("CTS NamedQuery");
      TestUtil.logErr("IllegalArgumentException was not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("IllegalArgumentException Caught as Expected.");
      pass1 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    TestUtil.logMsg("Testing TypedQuery version");
    try {
      getEntityManager().createNamedQuery("CTS NamedQuery", Order.class);
      TestUtil.logErr("IllegalArgumentException was not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("IllegalArgumentException Caught as Expected.");
      pass2 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    TestUtil.logMsg("Testing TypedQuery with incorrect result type version");
    try {
      getEntityManager().createNamedQuery("SELECT o from ORDER o",
          String.class);
      TestUtil.logErr("IllegalArgumentException was not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("IllegalArgumentException Caught as Expected.");
      pass2 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass1 || !pass2) {
      throw new Fault("createNamedQueryIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: createQueryIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:461; PERSISTENCE:JAVADOC:463;
   * PERSISTENCE:JAVADOC:462; PERSISTENCE:SPEC:609; PERSISTENCE:SPEC:1372;
   * 
   * @test_Strategy: Call EntityManager.createQuery(String) with a query string
   * that is invalid, verify IllegalArgumentException is thrown Call
   * EntityManager.createQuery(String, Class) with a TypedQuery string with a
   * result type not assignable to the specified type, verify
   * IllegalArgumentException is thrown. Call
   * EntityManager.createQuery(CriteriaQuery) with an invalid CriteriaQuery
   * verify IllegalArgumentException is thrown.*
   */
  public void createQueryIllegalArgumentExceptionTest() throws Fault {
    boolean pass1 = false, pass2 = false, pass3 = false;
    TestUtil.logTrace("Testing String version");

    try {
      Query q = getEntityManager().createQuery("CTS Query");
      TestUtil.logMsg("IllegalArgumentException was not thrown");
      try {
        q.getResultList();
        TestUtil.logErr(
            "Neither IllegalArgumentException nor PersistenceException was thrown");
      } catch (PersistenceException e) {
        TestUtil.logTrace("PersistenceException Caught during execution.");
        pass1 = true;
      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred during execution", e);
      }
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("IllegalArgumentException Caught as Expected.");
      pass1 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    TestUtil.logMsg("Testing String, Class version");
    try {
      getEntityManager().createQuery("SELECT o from ORDER o", String.class);
      TestUtil.logErr("IllegalArgumentException was not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("IllegalArgumentException Caught as Expected.");
      pass2 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    TestUtil.logMsg("Testing CriteriaQuery version");
    try {
      CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = qbuilder.createQuery(null);
      Query q = getEntityManager().createQuery(cquery);
      TestUtil.logMsg("IllegalArgumentException was not thrown");
      try {
        q.getResultList();
        TestUtil.logErr(
            "Neither IllegalArgumentException nor PersistenceException was thrown");
      } catch (PersistenceException e) {
        TestUtil.logTrace("PersistenceException Caught during execution.");
        pass3 = true;
      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred during execution", e);
      }
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("IllegalArgumentException Caught as Expected.");
      pass3 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass1 || !pass2 || !pass3) {
      throw new Fault("createQueryIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: detachIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:464
   * 
   * @test_Strategy: Call EntityManager.detach(String), verify
   * IllegalArgumentException is thrown
   */
  public void detachIllegalArgumentExceptionTest() throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      getEntityManager().detach(Client.class);
      TestUtil.logErr("IllegalArgumentException was not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("IllegalArgumentException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception", re);
      }
    }

    if (!pass) {
      throw new Fault("detachIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: getEntityManagerFactoryTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:328;
   * 
   * @test_Strategy: Get EntityManagerFactory
   */
  public void getEntityManagerFactoryTest() throws Fault {
    boolean pass = false;
    try {
      EntityManager em = getEntityManager();
      EntityManagerFactory emf = em.getEntityManagerFactory();
      if (emf == null) {
        TestUtil.logErr("getEntityManagerFactory() returned a null result");
      } else {
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    if (!pass) {
      throw new Fault("getEntityManagerFactoryTest failed");
    }
  }

  /*
   * @testName: emGetMetamodelTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:330;
   * 
   * @test_Strategy: Get a MetaModel Object from the EntityManager an make sure
   * it is not null
   */
  public void emGetMetamodelTest() throws Fault {
    boolean pass = false;
    try {
      EntityManager em = getEntityManager();
      Metamodel mm = em.getMetamodel();
      if (mm == null) {
        TestUtil.logErr("getMetamodel() returned a null result");
      } else {
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    if (!pass) {
      throw new Fault("emGetMetamodelTest failed");
    }
  }

  /*
   * @testName: setPropertyTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:336;
   * 
   * @test_Strategy: Set a standard property in the EntityManager and retrieve
   * it.
   */
  public void setPropertyTest() throws Fault {
    boolean foundKey = false;
    boolean foundValue = false;

    try {
      EntityManager em = getEntityManager();
      String expectedKey = "javax.persistence.cache.retrieveMode";
      CacheRetrieveMode expectedValue = CacheRetrieveMode.USE;
      TestUtil.logTrace(
          "Setting property:" + expectedKey + "," + expectedValue.toString());
      em.setProperty(expectedKey, expectedValue);

      // gather the props from the EntityManager

      TestUtil.logTrace("Retrieve all EntityManger properties:");
      Map<String, Object> em_entry = em.getProperties();

      for (Map.Entry<String, Object> entry : em_entry.entrySet()) {
        String key = entry.getKey();
        TestUtil.logMsg("Key = " + key);
        if (key.contains(expectedKey)) {
          foundKey = true;
          Object oValue = entry.getValue();
          if (oValue instanceof CacheRetrieveMode) {
            CacheRetrieveMode value = (CacheRetrieveMode) oValue;
            if (value.equals(expectedValue)) {
              TestUtil.logMsg("Received expected value:" + value.toString());
              foundValue = true;
            } else {
              TestUtil.logErr("Key:" + expectedKey + " -  expected value:"
                  + expectedKey + ", actual value" + value);
            }
          } else {
            TestUtil.logErr("The value for Key:" + expectedKey
                + "was not an instance of String:" + oValue);
          }
        }
      }
      if (!foundKey) {
        TestUtil.logErr(
            "Property key:" + expectedKey + ", not found in EntityManager");
      }
      if (!foundValue) {
        TestUtil.logErr("The value for Key:" + expectedKey
            + ", was not found in EntityManager");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    if (!foundKey || !foundValue) {
      throw new Fault("setPropertyTest failed");
    }
  }

  /*
   * @testName: getCriteriaBuilderTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:327; PERSISTENCE:SPEC:1702;
   *
   * @test_Strategy: access EntityManager.getCriteriaBuilder and verify it can
   * be used to create a query
   *
   */
  public void getCriteriaBuilderTest() throws Fault {
    boolean pass = false;
    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
      if (cbuilder != null) {
        getEntityTransaction().begin();
        CriteriaQuery<Object> cquery = cbuilder.createQuery();
        if (cquery != null) {
          TestUtil.logTrace("Obtained Non-null Criteria Query");
          pass = true;
        } else {
          TestUtil.logErr("Failed to get Non-null Criteria Query");
        }

        getEntityTransaction().commit();
      } else {
        TestUtil.logErr("getCriteriaBuilder() returned null");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    if (!pass) {
      throw new Fault("getCriteriaBuilderTest failed");
    }
  }

  /*
   * @testName: isJoinedToTransactionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1526
   *
   * @test_Strategy:
   *
   */
  public void isJoinedToTransactionTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    TestUtil.logMsg("Test when no transaction active");
    if (getEntityManager().isJoinedToTransaction() == false) {
      TestUtil.logTrace("Received expected result:false");
      pass1 = true;
    } else {
      TestUtil.logErr("Returned true when not in a transaction");
    }
    TestUtil.logMsg("Test when transaction active");

    getEntityTransaction().begin();
    if (getEntityManager().isJoinedToTransaction() == true) {
      TestUtil.logTrace("Received expected result:true");
      pass2 = true;
    } else {
      TestUtil.logErr("Returned false when in a transaction");
    }
    getEntityTransaction().commit();

    if (!pass1 || !pass2) {
      throw new Fault("isJoinedToTransactionTest failed");
    }

  }

  /*
   * @testName: createStoredProcedureQueryStringTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1520
   *
   * @test_Strategy:
   *
   */
  @SetupMethod(name = "setupEmployeeData")
  @CleanupMethod(name = "cleanupData")
  public void createStoredProcedureQueryStringTest() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    try {
      StoredProcedureQuery spq = getEntityManager()
          .createStoredProcedureQuery("GetEmpOneFirstNameFromOut");
      spq.registerStoredProcedureParameter(1, String.class, ParameterMode.OUT);
      spq.execute();

      Object oActual = spq.getOutputParameterValue(1);
      if (oActual instanceof String) {
        String actual = (String) oActual;
        if (actual.equals(emp0.getFirstName())) {
          TestUtil.logTrace("Received expected result:" + actual);
          pass = true;
        } else {
          TestUtil.logErr(
              "Expected result: " + emp0.getFirstName() + ", actual:" + actual);
        }
      } else {
        TestUtil.logErr(
            "Expected String to be returned, actual:" + oActual.getClass());
      }
    } catch (Exception ex) {
      TestUtil.logErr("Received unexpected exception:", ex);
    }
    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("createStoredProcedureQueryStringTest failed");
    }

  }

  /*
   * @testName: createStoredProcedureQueryStringIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1521
   *
   * @test_Strategy:
   *
   */
  public void createStoredProcedureQueryStringIllegalArgumentExceptionTest()
      throws Fault {
    boolean pass = false;
    StringBuilder msg = new StringBuilder();
    try {
      StoredProcedureQuery spq = getEntityManager()
          .createStoredProcedureQuery("DOESNOTEXIST");
      msg.append("Did not throw IllegalArgumentException");
      try {
        spq.execute();
        msg.append("or a PersistenceException from execute()");
      } catch (PersistenceException pe) {
        TestUtil.logTrace("Received PersistenceException");
        pass = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception after execute()", e);
      }
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }

    if (!pass) {
      TestUtil.logErr(msg.toString());

      throw new Fault(
          "createStoredProcedureQueryStringIllegalArgumentExceptionTest failed");
    }

  }

  /*
   * @testName: createStoredProcedureQueryStringClassArrayTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1522
   *
   * @test_Strategy:
   *
   */
  @SetupMethod(name = "setupEmployeeData")
  @CleanupMethod(name = "cleanupData")
  public void createStoredProcedureQueryStringClassArrayTest() throws Fault {
    boolean pass = false;
    getEntityTransaction().begin();
    try {
      Class[] cArray = { Employee.class };
      StoredProcedureQuery spq = getEntityManager()
          .createStoredProcedureQuery("GetEmpASCFromRS", cArray);
      if (dataBaseName.equalsIgnoreCase(ORACLE)
          || dataBaseName.equalsIgnoreCase(POSTGRESQL)) {
        TestUtil.logTrace("register refcursor parameter");
        spq.registerStoredProcedureParameter(1, void.class,
            ParameterMode.REF_CURSOR);
      }
      if (spq.execute()) {
        List<List> listOfList = getResultSetsFromStoredProcedure(spq);
        if (listOfList.size() == 1) {
          List<Integer> expected = new ArrayList<Integer>();
          for (Employee e : empRef) {
            expected.add(e.getId());
          }
          pass = verifyListOfListEmployeeIds(expected, listOfList);
        } else {
          TestUtil.logErr(
              "Did not get the correct number of result sets returned, expected: 1, actual:"
                  + listOfList.size());
        }
      } else {
        TestUtil.logErr("Expected execute() to return true, actual: false");
      }

    } catch (Exception ex) {
      TestUtil.logErr("Received unexpected exception:", ex);
    }
    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("createStoredProcedureQueryStringClassArrayTest failed");
    }

  }

  /*
   * @testName:
   * createStoredProcedureQueryStringClassArrayIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1523
   *
   * @test_Strategy:
   *
   */
  public void createStoredProcedureQueryStringClassArrayIllegalArgumentExceptionTest()
      throws Fault {
    boolean pass = false;
    Class[] cArray = { Integer.class };
    StringBuilder msg = new StringBuilder();
    try {
      StoredProcedureQuery spq = getEntityManager()
          .createStoredProcedureQuery("DOESNOTEXIST", cArray);
      msg.append("Did not throw IllegalArgumentException");
      try {
        spq.execute();
        msg.append("or a PersistenceException after execute()");

      } catch (PersistenceException pe) {
        TestUtil.logTrace("Received PersistenceException");
        pass = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception from execute()", e);
      }
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }

    if (!pass) {
      TestUtil.logErr(msg.toString());
      throw new Fault(
          "createStoredProcedureQueryStringClassArrayIllegalArgumentExceptionTest failed");
    }

  }

  /*
   * @testName: createStoredProcedureQueryStringStringArrayTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1524; PERSISTENCE:SPEC:1571;
   *
   * @test_Strategy:
   *
   */
  @SetupMethod(name = "setupEmployeeData")
  @CleanupMethod(name = "cleanupData")
  public void createStoredProcedureQueryStringStringArrayTest() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    clearCache();
    try {
      String[] sArray = { "id-firstname-lastname" };
      StoredProcedureQuery spq = getEntityManager()
          .createStoredProcedureQuery("GetEmpIdFNameLNameFromRS", sArray);
      spq.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
      if (dataBaseName.equalsIgnoreCase(ORACLE)
          || dataBaseName.equalsIgnoreCase(POSTGRESQL)) {
        TestUtil.logTrace("register refcursor parameter");
        spq.registerStoredProcedureParameter(2, void.class,
            ParameterMode.REF_CURSOR);
      }
      spq.setParameter(1, 1);

      if (spq.execute()) {

        List<List> listOfList = getResultSetsFromStoredProcedure(spq);
        if (listOfList.size() == 1) {
          List<Employee> expected = new ArrayList<Employee>();
          expected.add(new Employee(emp0.getId(), emp0.getFirstName(),
              emp0.getLastName()));
          pass = verifyListOfListEmployees(expected, listOfList);
        } else {
          TestUtil.logErr(
              "Did not get the correct number of result sets returned, expected: 1, actual:"
                  + listOfList.size());
        }
      } else {
        TestUtil.logErr("Expected execute() to return true, actual: false");
      }
    } catch (Exception ex) {
      TestUtil.logErr("Received unexpected exception:", ex);
    }
    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("createStoredProcedureQueryStringStringArrayTest failed");
    }

  }

  /*
   * @testName:
   * createStoredProcedureQueryStringStringArrayIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1525
   *
   * @test_Strategy:
   *
   */
  public void createStoredProcedureQueryStringStringArrayIllegalArgumentExceptionTest()
      throws Fault {
    boolean pass = false;
    StringBuilder msg = new StringBuilder();
    try {
      String[] sArray = { "doesnotexist" };
      StoredProcedureQuery spq = getEntityManager()
          .createStoredProcedureQuery("DOESNOTEXIST", sArray);
      msg.append("Did not throw IllegalArgumentException");
      try {
        spq.execute();
        msg.append("or a PersistenceException from execute()");
      } catch (PersistenceException pe) {
        TestUtil.logTrace("Received PersistenceException");
        pass = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception after execute()", e);
      }

    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }

    if (!pass) {
      TestUtil.logErr(msg.toString());
      throw new Fault(
          "createStoredProcedureQueryStringStringArrayIllegalArgumentExceptionTest failed");
    }

  }

  /*
   * @testName: createNamedStoredProcedureQueryStringTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1514; PERSISTENCE:JAVADOC:1530;
   * PERSISTENCE:JAVADOC:1532; PERSISTENCE:JAVADOC:1533;
   * PERSISTENCE:JAVADOC:1534; PERSISTENCE:JAVADOC:1541;
   * PERSISTENCE:JAVADOC:1543;
   * 
   * @test_Strategy:
   *
   */
  @SetupMethod(name = "setupEmployeeData")
  @CleanupMethod(name = "cleanupData")
  public void createNamedStoredProcedureQueryStringTest() throws Fault {
    boolean pass = false;
    getEntityTransaction().begin();
    clearCache();
    try {
      StoredProcedureQuery spq = null;
      if (dataBaseName.equalsIgnoreCase(ORACLE)
          || dataBaseName.equalsIgnoreCase(POSTGRESQL)) {
        TestUtil.logTrace(
            "Calling refcursor specific named stored procedure query");
        spq = getEntityManager().createNamedStoredProcedureQuery(
            "get-id-firstname-lastname-refcursor");
      } else {
        spq = getEntityManager()
            .createNamedStoredProcedureQuery("get-id-firstname-lastname");
      }
      spq.setParameter(1, 1);
      if (spq.execute()) {
        List<List> listOfList = getResultSetsFromStoredProcedure(spq);
        if (listOfList.size() == 1) {
          List<Employee> expected = new ArrayList<Employee>();
          expected.add(new Employee(emp0.getId(), emp0.getFirstName(),
              emp0.getLastName()));
          pass = verifyListOfListEmployees(expected, listOfList);
        } else {
          TestUtil.logErr(
              "Did not get the correct number of result sets returned, expected: 1, actual:"
                  + listOfList.size());
        }
      } else {
        TestUtil.logErr("Expected execute() to return true, actual: false");
      }

    } catch (Exception ex) {
      TestUtil.logErr("Received unexpected exception:", ex);
    }
    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("createNamedStoredProcedureQueryStringTest failed");
    }

  }

  /*
   * @testName:
   * createNamedStoredProcedureQueryStringIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1515
   *
   * @test_Strategy:
   *
   */
  public void createNamedStoredProcedureQueryStringIllegalArgumentExceptionTest()
      throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    try {
      getEntityManager().createNamedStoredProcedureQuery("DOESNOTEXIST");
      TestUtil.logErr("Did not throw IllegalArgumentException");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
      pass = true;
    }
    getEntityTransaction().rollback();

    if (!pass) {
      throw new Fault(
          "createNamedStoredProcedureQueryStringIllegalArgumentExceptionTest failed");
    }

  }

  private void createOrderData() {

    try {
      getEntityTransaction().begin();
      TestUtil.logMsg("Creating Orders");
      orders[0] = new Order(1, 111, "desc1");
      orders[1] = new Order(2, 222, "desc2");
      orders[2] = new Order(3, 333, "desc3");
      orders[3] = new Order(4, 444, "desc4");
      orders[4] = new Order(5, 555, "desc5");
      for (Order o : orders) {
        TestUtil.logTrace("Persisting order:" + o.toString());
        getEntityManager().persist(o);
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

  private void createEmployeeData() {

    try {
      getEntityTransaction().begin();
      TestUtil.logMsg("Creating Employees");

      final Date d1 = getUtilDate("2000-02-14");
      final Date d2 = getUtilDate("2001-06-27");
      final Date d3 = getUtilDate("2002-07-07");
      final Date d4 = getUtilDate("2003-03-03");
      final Date d5 = getUtilDate();

      emp0 = new Employee(1, "Alan", "Frechette", d1, (float) 35000.0);
      empRef.add(emp0);
      empRef.add(new Employee(2, "Arthur", "Frechette", d2, (float) 35000.0));
      empRef.add(new Employee(3, "Shelly", "McGowan", d3, (float) 50000.0));
      empRef.add(new Employee(4, "Robert", "Bissett", d4, (float) 55000.0));
      empRef.add(new Employee(5, "Stephen", "DMilla", d5, (float) 25000.0));
      for (Employee e : empRef) {
        if (e != null) {
          getEntityManager().persist(e);
          TestUtil.logTrace("persisted employee:" + e);
        }
      }
      getEntityManager().flush();
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
      getEntityManager().createNativeQuery("DELETE FROM PURCHASE_ORDER")
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
