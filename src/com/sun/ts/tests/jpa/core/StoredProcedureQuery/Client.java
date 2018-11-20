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

package com.sun.ts.tests.jpa.core.StoredProcedureQuery;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import javax.persistence.*;
import java.util.*;

public class Client extends PMClientBase {

  List<Employee> empRef = new ArrayList<Employee>();

  List<Employee2> empRef2 = new ArrayList<Employee2>();

  Employee emp0 = null;

  Employee2 emp2 = null;

  final Date utilDate = getUtilDate("2000-02-14");

  final Calendar calDate = getCalDate(2000, 02, 14);

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
    try {
      super.setup(args, p);
      removeTestData();
      createEmployeeTestData();
      dataBaseName = p.getProperty("jdbc.db");
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  /*
   * setupEmployee2Data() is called before each test
   *
   * @class.setup_props: jdbc.db;
   */
  public void setupEmployee2Data(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    try {
      super.setup(args, p);
      removeTestData();
      createEmployee2TestData();
      dataBaseName = p.getProperty("jdbc.db");
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("Cleanup data");
    removeTestData();
    TestUtil.logTrace("cleanup complete, calling super.cleanup");
    super.cleanup();
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
                  + o.getClass().getSimpleName());
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

  public boolean verifyEmployeeIds(List<Integer> expected,
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
   * @testName: executeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1557; PERSISTENCE:SPEC:1515;
   * PERSISTENCE:SPEC:1566; PERSISTENCE:SPEC:1568; PERSISTENCE:SPEC:1565;
   * PERSISTENCE:SPEC:1572; PERSISTENCE:SPEC:1576; PERSISTENCE:SPEC:1577;
   * PERSISTENCE:SPEC:1578; PERSISTENCE:SPEC:1580;
   *
   * @test_Strategy:
   *
   */
  public void executeTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;

    try {
      getEntityTransaction().begin();
      try {
        TestUtil.logMsg("Testing using name,class");
        clearCache();
        StoredProcedureQuery spq = getEntityManager()
            .createStoredProcedureQuery("GetEmpASCFromRS", Employee.class);
        if (dataBaseName.equalsIgnoreCase(ORACLE)
            || dataBaseName.equalsIgnoreCase(POSTGRESQL)) {
          TestUtil.logTrace("register refcursor parameter");
          spq.registerStoredProcedureParameter(1, void.class,
              ParameterMode.REF_CURSOR);
        }
        TestUtil.logTrace("executing:" + spq.toString());
        if (spq.execute()) {

          List<List> listOfList = getResultSetsFromStoredProcedure(spq);
          if (listOfList.size() == 1) {
            List<Integer> expected = new ArrayList<Integer>();
            for (Employee e : empRef) {
              expected.add(e.getId());
            }
            pass1 = verifyEmployeeIds(expected, listOfList);
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
      try {
        TestUtil.logMsg("Testing using name,result mapping");
        clearCache();
        StoredProcedureQuery spq = getEntityManager()
            .createStoredProcedureQuery("GetEmpIdFNameLNameFromRS",
                "id-firstname-lastname");
        spq.registerStoredProcedureParameter(1, Integer.class,
            ParameterMode.IN);
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
            pass2 = verifyListOfListEmployees(expected, listOfList);
          } else {
            TestUtil.logErr(
                "Did not get the correct number of result sets returned, expected: 1, actual:"
                    + listOfList.size());
          }
        } else {
          TestUtil.logErr("Expected execute() to return false, actual: true");
        }
      } catch (Exception ex) {
        TestUtil.logErr("Received unexpected exception:", ex);
      }
      try {
        TestUtil.logMsg("Testing using named stored procedure");
        clearCache();
        StoredProcedureQuery spq;
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
            pass3 = verifyListOfListEmployees(expected, listOfList);
          } else {
            TestUtil.logErr(
                "Did not get the correct number of result sets returned, expected: 1, actual:"
                    + listOfList.size());
          }
        } else {
          TestUtil.logErr("Expected execute() to return false, actual: true");
        }
      } catch (Exception ex) {
        TestUtil.logErr("Received unexpected exception:", ex);
      }

      getEntityTransaction().commit();

    } catch (Exception ex) {
      TestUtil.logErr("Received unexpected exception:", ex);
    }

    if (!pass1 || !pass2 || !pass3) {
      throw new Fault("executeTest failed");
    }

  }

  /*
   * @testName: getOutputParameterValueIntTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1547; PERSISTENCE:SPEC:1569;
   * PERSISTENCE:SPEC:1570; PERSISTENCE:SPEC:1583;
   * 
   * @test_Strategy:
   *
   */
  public void getOutputParameterValueIntTest() throws Fault {
    boolean pass2 = false;
    boolean pass4 = false;
    boolean pass6 = false;

    Object oActual = null;
    try {
      getEntityTransaction().begin();

      TestUtil.logMsg("Get the value from an OUT only parameter");
      StoredProcedureQuery spq3 = getEntityManager()
          .createStoredProcedureQuery("GetEmpOneFirstNameFromOut");
      spq3.registerStoredProcedureParameter(1, String.class, ParameterMode.OUT);
      if (!spq3.execute()) {

        oActual = spq3.getOutputParameterValue(1);
        if (oActual instanceof String) {
          String actual = (String) oActual;
          if (actual.equals(emp0.getFirstName())) {
            TestUtil.logTrace("Received expected result:" + actual);
            pass6 = true;
          } else {
            TestUtil.logErr("Expected result: " + emp0.getFirstName()
                + ", actual:" + actual);
          }
        } else {
          TestUtil.logErr("Did not get instance of String, instead:" + oActual);
        }
      } else {
        TestUtil.logErr("Expected execute() to return false, actual: true");
      }
    } catch (Exception ex) {
      TestUtil.logErr("Received unexpected exception:", ex);
    }
    try {
      TestUtil.logMsg("Get the value from an IN and OUT parameter");
      StoredProcedureQuery spq1 = getEntityManager()
          .createStoredProcedureQuery("GetEmpFirstNameFromOut");
      spq1.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
      spq1.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
      spq1.setParameter(1, 1);
      if (!spq1.execute()) {

        oActual = spq1.getOutputParameterValue(2);
        if (oActual instanceof String) {
          String actual = (String) oActual;
          if (actual.equals(emp0.getFirstName())) {
            TestUtil.logTrace("Received expected result:" + actual);
            pass2 = true;
          } else {
            TestUtil.logErr("Expected result: " + emp0.getFirstName()
                + ", actual:" + actual);
          }
        } else {
          TestUtil.logErr("Did not get instance of String, instead:" + oActual);
        }
      } else {
        TestUtil.logErr("Expected execute() to return false, actual: true");
      }
    } catch (Exception ex) {
      TestUtil.logErr("Received unexpected exception:", ex);
    }
    try {
      TestUtil.logMsg("Get the value from an INOUT parameter");
      StoredProcedureQuery spq2 = getEntityManager()
          .createStoredProcedureQuery("GetEmpLastNameFromInOut");
      spq2.registerStoredProcedureParameter(1, String.class,
          ParameterMode.INOUT);
      spq2.setParameter(1, "1");

      if (!spq2.execute()) {

        oActual = spq2.getOutputParameterValue(1);
        if (oActual instanceof String) {
          String actual = (String) oActual;
          if (actual.equals(emp0.getLastName())) {
            TestUtil.logTrace("Received expected result:" + actual);
            pass4 = true;
          } else {
            TestUtil.logErr("Expected result: " + emp0.getLastName()
                + ", actual:" + actual);
          }
        } else {
          TestUtil.logErr("Expected Integer to be returned, actual:" + oActual);
        }
      } else {
        TestUtil.logErr("Expected execute() to return false, actual: true");
      }
      getEntityTransaction().commit();

    } catch (Exception ex) {
      TestUtil.logErr("Received unexpected exception:", ex);
    }

    if (!pass2 || !pass4 || !pass6) {
      throw new Fault("getOutputParameterValueIntTest failed");
    }

  }

  /*
   * @testName: getOutputParameterValueIntIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1548; PERSISTENCE:SPEC:1302.4;
   *
   * @test_Strategy:
   *
   */
  public void getOutputParameterValueIntIllegalArgumentExceptionTest()
      throws Fault {
    boolean pass2 = false;
    boolean pass4 = false;

    try {
      getEntityTransaction().begin();

      TestUtil.logMsg("Get a value that does not exist");
      StoredProcedureQuery spq1 = getEntityManager()
          .createStoredProcedureQuery("GetEmpFirstNameFromOut");
      spq1.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
      spq1.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
      spq1.setParameter(1, 1);

      if (!spq1.execute()) {
        try {
          spq1.getOutputParameterValue(99);
          TestUtil.logErr("Did not throw IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("Received expected IllegalArgumentException");
          if (getEntityTransaction().getRollbackOnly() != true) {
            pass2 = true;
            TestUtil.logTrace("Transaction was not marked for rollback");
          } else {
            TestUtil.logErr(
                "Transaction was marked for rollback and should not have been");
          }
        } catch (Exception e) {
          TestUtil.logErr("Caught unexpected exception", e);
        }
      } else {
        TestUtil.logErr("Expected execute() to return false, actual: true");
      }
      TestUtil.logMsg("Get the value from an IN parameter");
      StoredProcedureQuery spq2 = getEntityManager()
          .createStoredProcedureQuery("GetEmpLastNameFromInOut");
      spq2.registerStoredProcedureParameter(1, String.class,
          ParameterMode.INOUT);
      spq2.setParameter(1, "1");

      if (!spq2.execute()) {
        try {
          spq2.getOutputParameterValue(99);
          TestUtil.logErr("Did not throw IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("Received expected IllegalArgumentException");
          pass4 = true;
        } catch (Exception e) {
          TestUtil.logErr("Caught unexpected exception", e);
        }

        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } else {
        TestUtil.logErr("Expected execute() to return false, actual: true");
      }
    } catch (Exception ex) {
      TestUtil.logErr("Received unexpected exception:", ex);
    }

    if (!pass2 || !pass4) {
      throw new Fault(
          "getOutputParameterValueIntIllegalArgumentExceptionTest failed");
    }

  }

  /*
   * @testName: getFirstResultTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1585; PERSISTENCE:JAVADOC:3482;
   * PERSISTENCE:SPEC:1515;
   *
   * @test_Strategy:
   *
   */
  public void getFirstResultTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    try {
      getEntityTransaction().begin();

      StoredProcedureQuery spq = getEntityManager()
          .createStoredProcedureQuery("GetEmpASCFromRS", Employee.class);
      if (dataBaseName.equalsIgnoreCase(ORACLE)
          || dataBaseName.equalsIgnoreCase(POSTGRESQL)) {
        TestUtil.logTrace("register refcursor parameter");
        spq.registerStoredProcedureParameter(1, void.class,
            ParameterMode.REF_CURSOR);
      }
      int num = spq.getFirstResult();
      if (num == 0) {
        TestUtil
            .logTrace("Received expected first result when none is set:" + num);
        pass1 = true;
      } else {
        TestUtil
            .logErr("Expected first result when none is set: 0, actual:" + num);
      }

      List<Employee> actual = spq.getResultList();
      num = spq.getFirstResult();
      if (num == 0) {
        TestUtil.logTrace(
            "Received expected first result:" + num + " after getResultList");
        pass2 = true;
      } else {
        TestUtil.logErr(
            "Expected first result: 0, actual:" + num + " after getResultList");
      }
      if (actual.size() > 0) {
        pass3 = verifyListEmployees(empRef, actual);
      } else {
        TestUtil.logErr("getResultList() returned 0 results");
      }
      getEntityTransaction().commit();

    } catch (Exception ex) {
      TestUtil.logErr("Received unexpected exception:", ex);
    }

    if (!pass1 || !pass2 || !pass3) {
      throw new Fault("getFirstResultTest failed");
    }

  }

  /*
   * @testName: getMaxResultsTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1590;
   * 
   * @test_Strategy:
   *
   */
  public void getMaxResultsTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;

    try {
      getEntityTransaction().begin();

      StoredProcedureQuery spq = getEntityManager()
          .createStoredProcedureQuery("GetEmpASCFromRS", Employee.class);
      if (dataBaseName.equalsIgnoreCase(ORACLE)
          || dataBaseName.equalsIgnoreCase(POSTGRESQL)) {
        TestUtil.logTrace("register refcursor parameter");
        spq.registerStoredProcedureParameter(1, void.class,
            ParameterMode.REF_CURSOR);
      }
      int num = spq.getMaxResults();
      if (num == Integer.MAX_VALUE) {
        TestUtil.logTrace("Received expected max result:" + num);
        pass1 = true;
      } else {
        TestUtil.logErr(
            "Expected max result: " + Integer.MAX_VALUE + ", actual:" + num);
      }
      List<Employee> actual = spq.getResultList();
      num = spq.getMaxResults();
      if (num == Integer.MAX_VALUE) {
        TestUtil.logTrace(
            "Received expected max result:" + num + " after getResultList");
        pass2 = true;
      } else {
        TestUtil.logErr("Expected max result:" + Integer.MAX_VALUE + ", actual:"
            + num + " after getResultList");
      }
      if (actual.size() > 0) {
        pass3 = verifyListEmployees(empRef, actual);
      } else {
        TestUtil.logErr("getResultList() returned 0 results");
      }
      getEntityTransaction().commit();

    } catch (Exception ex) {
      TestUtil.logErr("Received unexpected exception:", ex);
    }

    if (!pass1 || !pass2 || !pass3) {
      throw new Fault("getMaxResultsTest failed");
    }

  }

  /*
   * @testName: getSingleResultTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:3482; PERSISTENCE:SPEC:1515;
   * 
   * @test_Strategy: Get single result from returned resultset.
   *
   */
  public void getSingleResultTest() throws Fault {
    boolean pass = false;

    try {
      getEntityTransaction().begin();
      clearCache();

      StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery(
          "GetEmpIdFNameLNameFromRS", "id-firstname-lastname");
      spq.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
      if (dataBaseName.equalsIgnoreCase(ORACLE)
          || dataBaseName.equalsIgnoreCase(POSTGRESQL)) {
        TestUtil.logTrace("register refcursor parameter");
        spq.registerStoredProcedureParameter(2, void.class,
            ParameterMode.REF_CURSOR);
      }
      spq.setParameter(1, 1);
      Employee expected = new Employee(emp0.getId(), emp0.getFirstName(),
          emp0.getLastName());
      Object o = spq.getSingleResult();
      if (o instanceof Employee) {
        Employee actual = (Employee) o;
        if (actual.equals(expected)) {
          TestUtil.logTrace("Received expected result:" + actual);
          pass = true;
        } else {
          TestUtil.logErr("Expected result:" + expected + ", actual:" + actual);
        }
      } else {
        TestUtil.logErr("Did not get Integer result:" + o);
      }
      getEntityTransaction().commit();

    } catch (Exception ex) {
      TestUtil.logErr("Received unexpected exception:", ex);
    }

    if (!pass) {
      throw new Fault("getSingleResultTest failed");
    }

  }

  /*
   * @testName: getSingleResultNoResultExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:3483
   * 
   * @test_Strategy: Get single result from returned resultset that is empty
   *
   */
  public void getSingleResultNoResultExceptionTest() throws Fault {
    boolean pass = false;

    try {
      getEntityTransaction().begin();

      StoredProcedureQuery spq = getEntityManager()
          .createStoredProcedureQuery("GetEmpIdFNameLNameFromRS");
      spq.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
      if (dataBaseName.equalsIgnoreCase(ORACLE)
          || dataBaseName.equalsIgnoreCase(POSTGRESQL)) {
        TestUtil.logTrace("register refcursor parameter");
        spq.registerStoredProcedureParameter(2, void.class,
            ParameterMode.REF_CURSOR);
      }
      spq.setParameter(1, 99);
      try {
        spq.getSingleResult();
        TestUtil.logErr("Did not throw NoResultException");
      } catch (NoResultException nre) {
        TestUtil.logTrace("Received expected NoResultException");
        pass = true;
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception", e);

      }
      if (getEntityTransaction().isActive()) {
        getEntityTransaction().rollback();
      }
    } catch (Exception ex) {
      TestUtil.logErr("Received unexpected exception:", ex);
    }

    if (!pass) {
      throw new Fault("getSingleResultNoResultExceptionTest failed");
    }

  }

  /*
   * @testName: getSingleResultNonUniqueResultExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:3484
   * 
   * @test_Strategy: Get single result from returned resultset that contains
   * multiple values
   *
   */
  public void getSingleResultNonUniqueResultExceptionTest() throws Fault {
    boolean pass = false;

    try {
      getEntityTransaction().begin();

      StoredProcedureQuery spq = getEntityManager()
          .createStoredProcedureQuery("GetEmpASCFromRS", Employee.class);
      if (dataBaseName.equalsIgnoreCase(ORACLE)
          || dataBaseName.equalsIgnoreCase(POSTGRESQL)) {
        TestUtil.logTrace("register refcursor parameter");
        spq.registerStoredProcedureParameter(1, void.class,
            ParameterMode.REF_CURSOR);
      }
      try {
        spq.getSingleResult();
        TestUtil.logErr("Did not throw NonUniqueResultException");
      } catch (NonUniqueResultException nure) {
        TestUtil.logTrace("Received expected NonUniqueResultException");
        pass = true;
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception", e);
      }
      if (getEntityTransaction().isActive()) {
        getEntityTransaction().rollback();
      }
    } catch (Exception ex) {
      TestUtil.logErr("Received unexpected exception:", ex);
    }

    if (!pass) {
      throw new Fault("getSingleResultNonUniqueResultExceptionTest failed");
    }

  }

  /*
   * @testName: setgetFlushModeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1586; PERSISTENCE:JAVADOC:1559;
   * 
   * @test_Strategy: Set and Get the various flushModes of a Query
   */

  public void setgetFlushModeTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;

    try {
      TestUtil.logMsg("Testing StoredProcedureQuery");
      StoredProcedureQuery spq = getEntityManager()
          .createStoredProcedureQuery("GetEmpASCFromRS", Employee.class);
      if (dataBaseName.equalsIgnoreCase(ORACLE)
          || dataBaseName.equalsIgnoreCase(POSTGRESQL)) {
        TestUtil.logTrace("register refcursor parameter");
        spq.registerStoredProcedureParameter(1, void.class,
            ParameterMode.REF_CURSOR);
      }
      FlushModeType fmt = spq.getFlushMode();
      if (fmt != null) {
        if (fmt.equals(getEntityManager().getFlushMode())) {
          TestUtil.logTrace("Setting mode to returned default mode");
          spq.setFlushMode(fmt);
          TestUtil.logTrace("Setting mode to FlushModeType.COMMIT");
          spq.setFlushMode(FlushModeType.COMMIT);
          fmt = spq.getFlushMode();
          if (fmt.equals(FlushModeType.COMMIT)) {
            TestUtil.logTrace("Setting mode to FlushModeType.AUTO");
            spq.setFlushMode(FlushModeType.AUTO);
            fmt = spq.getFlushMode();
            if (fmt.equals(FlushModeType.AUTO)) {
              TestUtil.logTrace("Query object returned from setFlushMode");
              Query q = spq.setFlushMode(FlushModeType.COMMIT);
              fmt = q.getFlushMode();
              if (fmt.equals(FlushModeType.COMMIT)) {
                TestUtil.logTrace(
                    "Received expected Query FlushModeType:" + fmt.name());
                pass1 = true;
              } else {
                TestUtil.logErr("Expected a value of:"
                    + FlushModeType.COMMIT.name() + ", actual:" + fmt.name());
              }
            } else {
              TestUtil.logErr("Expected a value of:" + FlushModeType.AUTO.name()
                  + ", actual:" + fmt.name());
            }
          } else {
            TestUtil.logErr("Expected a default value of:"
                + FlushModeType.COMMIT.name() + ", actual:" + fmt.name());
          }
        } else {
          TestUtil.logErr("Expected EntityManager value of:"
              + getEntityManager().getFlushMode() + ", actual:" + fmt.name());
        }
      } else {
        TestUtil.logErr("getFlushMode return null");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing Query object returned from setFlushMode");
      StoredProcedureQuery spq2 = getEntityManager()
          .createStoredProcedureQuery("GetEmpASCFromRS", Employee.class);

      TestUtil.logTrace("Setting mode to FlushModeType.AUTO");
      Query q = spq2.setFlushMode(FlushModeType.AUTO);
      FlushModeType fmt = q.getFlushMode();
      if (fmt.equals(FlushModeType.AUTO)) {
        TestUtil.logTrace("Received expected value of:" + fmt.name());
        pass2 = true;
      } else {
        TestUtil.logErr("Expected a value of:" + FlushModeType.AUTO.name()
            + ", actual:" + fmt.name());
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass1 || !pass2)
      throw new Fault("setgetFlushModeTest failed");
  }

  /*
   * @testName: setLockModeIllegalStateExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1637;
   * 
   * @test_Strategy:
   */

  public void setLockModeIllegalStateExceptionTest() throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();

      StoredProcedureQuery spq = getEntityManager()
          .createStoredProcedureQuery("UpdateEmpSalaryColumn");
      try {
        spq.setLockMode(LockModeType.PESSIMISTIC_READ);
        TestUtil.logErr("Did not throw IllegalStateException");
      } catch (IllegalStateException ise) {
        TestUtil.logTrace("Received expected IllegalStateException");
        pass = true;
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception", e);
      }
      if (getEntityTransaction().isActive()) {
        getEntityTransaction().rollback();
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("setLockModeIllegalStateExceptionTest failed");
  }
  /*
   * @testName: getLockModeIllegalStateExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1589;
   * 
   * @test_Strategy:
   */

  public void getLockModeIllegalStateExceptionTest() throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();

      StoredProcedureQuery spq = getEntityManager()
          .createStoredProcedureQuery("UpdateEmpSalaryColumn");
      try {
        spq.getLockMode();
        TestUtil.logErr("Did not throw IllegalStateException");
      } catch (IllegalStateException ise) {
        TestUtil.logTrace("Received expected IllegalStateException");
        pass = true;
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception", e);
      }
      if (getEntityTransaction().isActive()) {
        getEntityTransaction().rollback();
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("getLockModeIllegalStateExceptionTest failed");
  }

  /*
   * @testName: setGetParameterIntTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1597; PERSISTENCE:JAVADOC:1574;
   * 
   * @test_Strategy:
   */

  public void setGetParameterIntTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    try {
      getEntityTransaction().begin();

      TestUtil.logMsg("Testing StoredProcedureQuery");
      StoredProcedureQuery spq = getEntityManager()
          .createStoredProcedureQuery("GetEmpFirstNameFromOut");
      spq.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
      spq.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
      spq.setParameter(1, 1);
      Parameter p = spq.getParameter(1);
      int pos = p.getPosition();
      if (pos == 1) {
        TestUtil.logTrace("Received expected parameter:" + pos);
        pass1 = true;
      } else {
        TestUtil.logErr("Expected position: 1, actual:" + pos);
      }

      TestUtil.logMsg("Testing Query object returned from getParameter");
      StoredProcedureQuery spq2 = getEntityManager()
          .createStoredProcedureQuery("GetEmpFirstNameFromOut");
      spq2.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
      spq2.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
      Query q = spq2.setParameter(1, 1);
      p = q.getParameter(1);
      pos = p.getPosition();
      if (pos == 1) {
        TestUtil.logTrace("Received expected parameter:" + pos);
        pass2 = true;
      } else {
        TestUtil.logErr("Expected position: 1, actual:" + pos);
      }
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass1 || !pass2)
      throw new Fault("setGetParameterIntTest failed");
  }

  // disabling this test since Store Procedures with named parameters
  // isn't supported by all DB's at this time
  /*
   * testName: setGetParameterStringTest assertion_ids:
   * PERSISTENCE:JAVADOC:1549; PERSISTENCE:JAVADOC:1558;
   * PERSISTENCE:JAVADOC:1568; PERSISTENCE:JAVADOC:1591; test_Strategy:
   */

  /*
   * public void setGetParameterStringTest() throws Fault { boolean pass1 =
   * false; boolean pass2 = false; boolean pass3 = false; try {
   * getEntityTransaction().begin();
   * 
   * TestUtil.logMsg("Testing StoredProcedureQuery"); StoredProcedureQuery spq =
   * getEntityManager().createStoredProcedureQuery("GetEmpFirstNameFromOut");
   * spq.registerStoredProcedureParameter("IN_PARAM", Integer.class,
   * ParameterMode.IN); spq.registerStoredProcedureParameter("OUT_PARAM",
   * String.class, ParameterMode.OUT); spq.setParameter("IN_PARAM", 1);
   * 
   * Parameter p = spq.getParameter("OUT_PARAM"); Integer pos = p.getPosition();
   * if (pos == null){ TestUtil.logTrace("Received expected null"); pass1 =
   * true; } else { TestUtil.logErr("Expected position: null, actual:" + pos); }
   * 
   * spq.execute(); Object oActual = spq.getOutputParameterValue("OUT_PARAM");
   * if (oActual instanceof String) { String actual = (String) oActual; if
   * (actual.equals(emp0.getFirstName())) {
   * TestUtil.logTrace("Received expected result:" + actual); pass2 = true; }
   * else { TestUtil.logErr("Expected result: " + emp0.getFirstName() +
   * ", actual:" + actual); } } else {
   * TestUtil.logErr("Did not get instance of String, instead:" +
   * oActual.getClass()); } getEntityTransaction().commit();
   * 
   * } catch (Exception e) { TestUtil.logErr("Caught exception: ", e); }
   * 
   * if (!pass1 || !pass2 ) throw new Fault("setGetParameterStringTest failed");
   * }
   */

  /*
   * @testName: getParameterStringExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1609; PERSISTENCE:JAVADOC:1569;
   * 
   * @test_Strategy:
   */

  public void getParameterStringExceptionTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    try {
      getEntityTransaction().begin();
      TestUtil.logMsg("Testing StoredProcedureQuery ");

      StoredProcedureQuery spq = getEntityManager()
          .createStoredProcedureQuery("GetEmpFirstNameFromOut");
      spq.registerStoredProcedureParameter("ID", Integer.class,
          ParameterMode.IN);
      spq.registerStoredProcedureParameter("FIRSTNAME", String.class,
          ParameterMode.OUT);
      spq.setParameter("ID", 1);
      try {
        spq.getParameter("DOESNOTEXIST");
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
        pass1 = true;
      } catch (IllegalStateException ise) {
        TestUtil.logTrace("Received expected IllegalStateException");
        pass1 = true;
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception", e);
      }

      TestUtil.logMsg("Testing Query object returned from getParameter");
      StoredProcedureQuery spq2 = getEntityManager()
          .createStoredProcedureQuery("GetEmpFirstNameFromOut");
      spq2.registerStoredProcedureParameter("ID", Integer.class,
          ParameterMode.IN);
      spq2.registerStoredProcedureParameter("FIRSTNAME", String.class,
          ParameterMode.OUT);
      Query q = spq2.setParameter("ID", 1);
      try {
        q.getParameter("DOESNOTEXIST");
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
        pass2 = true;
      } catch (IllegalStateException ise) {
        TestUtil.logTrace("Received expected IllegalStateException");
        pass2 = true;
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception", e);
      }

      if (getEntityTransaction().isActive()) {
        getEntityTransaction().rollback();
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception", e);
    }

    if (!pass1 || !pass2)
      throw new Fault("getParameterStringExceptionTest failed");
  }

  /*
   * @testName: getParameterIntIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1598;
   * 
   * @test_Strategy:
   */

  public void getParameterIntIllegalArgumentExceptionTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    try {
      getEntityTransaction().begin();
      TestUtil.logMsg("Testing StoredProcedureQuery ");

      StoredProcedureQuery spq = getEntityManager()
          .createStoredProcedureQuery("GetEmpFirstNameFromOut");
      spq.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
      spq.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
      spq.setParameter(1, 1);
      try {
        spq.getParameter(99);
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
        pass1 = true;
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception", e);
      }

      TestUtil.logMsg("Testing Query object returned from getParameter");
      StoredProcedureQuery spq2 = getEntityManager()
          .createStoredProcedureQuery("GetEmpFirstNameFromOut");
      spq2.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
      spq2.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
      Query q = spq2.setParameter(1, 1);
      try {
        q.getParameter(99);
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
        pass2 = true;
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception", e);
      }

      if (getEntityTransaction().isActive()) {
        getEntityTransaction().rollback();
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception", e);
    }

    if (!pass1 || !pass2)
      throw new Fault("getParameterIntIllegalArgumentExceptionTest failed");
  }

  /*
   * @testName: setParameterParameterObjectTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1562;
   * 
   * @test_Strategy:
   */

  public void setParameterParameterObjectTest() throws Fault {
    boolean pass2 = false;
    boolean pass4 = false;
    try {
      getEntityTransaction().begin();

      TestUtil.logMsg("Testing StoredProcedureQuery ");

      StoredProcedureQuery spq = getEntityManager()
          .createStoredProcedureQuery("GetEmpFirstNameFromOut");
      spq.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
      spq.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
      spq.setParameter(1, 99);
      Parameter p = spq.getParameter(1);
      spq.setParameter(p, 1);

      if (!spq.execute()) {
        Object oActual = spq.getOutputParameterValue(2);
        if (oActual instanceof String) {
          String actual = (String) oActual;
          if (actual.equals(emp0.getFirstName())) {
            TestUtil.logTrace("Received expected result:" + actual);
            pass2 = true;
          } else {
            TestUtil.logErr("Expected result: " + emp0.getFirstName()
                + ", actual:" + actual);
          }
        } else {
          TestUtil.logErr(
              "Did not get instance of String, instead:" + oActual.getClass());
        }
      } else {
        TestUtil.logErr("Expected execute() to return false, actual: true");
      }
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Caught exception", e);
      if (getEntityTransaction().isActive()) {
        getEntityTransaction().rollback();
      }
    }
    try {
      getEntityTransaction().begin();

      TestUtil.logMsg("Testing Query object returned from setParameter");
      StoredProcedureQuery spq2 = getEntityManager()
          .createStoredProcedureQuery("GetEmpFirstNameFromOut");
      spq2.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
      spq2.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
      Query q = spq2;
      q.setParameter(1, 99);
      Parameter p = q.getParameter(1);
      q.setParameter(p, 1);
      StoredProcedureQuery spq3 = (StoredProcedureQuery) q;
      if (!spq3.execute()) {
        Object oActual = spq3.getOutputParameterValue(2);
        if (oActual instanceof String) {
          String actual = (String) oActual;
          if (actual.equals(emp0.getFirstName())) {
            TestUtil.logTrace("Received expected result:" + actual);
            pass4 = true;
          } else {
            TestUtil.logErr("Expected result: " + emp0.getFirstName()
                + ", actual:" + actual);
          }
        } else {
          TestUtil.logErr("Did not get instance of String, instead:" + oActual);
        }
      } else {
        TestUtil.logErr("Expected execute() to return false, actual: true");
      }
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Caught exception", e);
      if (getEntityTransaction().isActive()) {
        getEntityTransaction().rollback();
      }
    }

    if (!pass2 || !pass4)
      throw new Fault("setParameterParameterObjectTest failed");

  }

  /*
   * @testName: setParameterParameterObjectIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1563;
   * 
   * @test_Strategy: call setParameter(Parameter, Object) using parameter from
   * different query.
   */

  public void setParameterParameterObjectIllegalArgumentExceptionTest()
      throws Fault {
    boolean pass1 = false;
    try {
      getEntityTransaction().begin();

      TestUtil.logTrace("Get parameter from other stored procedure");
      StoredProcedureQuery spq = getEntityManager()
          .createStoredProcedureQuery("GetEmpLastNameFromInOut");
      spq.registerStoredProcedureParameter(1, String.class,
          ParameterMode.INOUT);
      spq.setParameter(1, "INOUT");
      // Parameter to be used in next StoredProcedure
      Parameter p = spq.getParameter(1);

      StoredProcedureQuery spq2 = getEntityManager()
          .createStoredProcedureQuery("GetEmpFirstNameFromOut");
      spq2.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
      spq2.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);

      try {
        spq2.setParameter(p, 1);
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
        pass1 = true;
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception", e);
      }

      if (getEntityTransaction().isActive()) {
        getEntityTransaction().rollback();
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception", e);
    }

    if (!pass1)
      throw new Fault(
          "setParameterParameterObjectIllegalArgumentExceptionTest failed");
  }

  /*
   * @testName: setParameterIntObjectIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1575;
   * 
   * @test_Strategy:
   */
  public void setParameterIntObjectIllegalArgumentExceptionTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    boolean pass4 = false;
    try {
      getEntityTransaction().begin();
      TestUtil.logMsg(
          "Testing StoredProcedureQuery with incorrect position specified");

      StoredProcedureQuery spq1 = getEntityManager()
          .createStoredProcedureQuery("GetEmpFirstNameFromOut");
      spq1.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
      spq1.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
      try {
        spq1.setParameter(99, 1);
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
        pass1 = true;
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception", e);
      }

      TestUtil
          .logMsg("Testing StoredProcedureQuery with incorrect type specified");

      StoredProcedureQuery spq2 = getEntityManager()
          .createStoredProcedureQuery("GetEmpFirstNameFromOut");
      spq2.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
      spq2.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
      try {
        spq2.setParameter(1, new java.util.Date());
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
        pass2 = true;
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception", e);
      }

      TestUtil.logMsg("Testing Query object with incorrect position specified");
      StoredProcedureQuery spq3 = getEntityManager()
          .createStoredProcedureQuery("GetEmpFirstNameFromOut");
      spq3.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
      spq3.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
      Query q1 = spq3.setParameter(1, 1);
      try {
        spq3.setParameter(99, 1);
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
        pass3 = true;
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception", e);
      }

      TestUtil.logMsg("Testing Query object with incorrect type specified");
      StoredProcedureQuery spq4 = getEntityManager()
          .createStoredProcedureQuery("GetEmpFirstNameFromOut");
      spq4.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
      spq4.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
      Query q2 = spq4.setParameter(1, 1);
      try {
        q2.setParameter(1, new java.util.Date());
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
        pass4 = true;
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception", e);
      }
      if (getEntityTransaction().isActive()) {
        getEntityTransaction().rollback();
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception", e);
    }

    if (!pass1 || !pass2 || !pass3 || !pass4)
      throw new Fault(
          "setParameterIntObjectIllegalArgumentExceptionTest failed");
  }

  /*
   * @testName: getParametersTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1597; PERSISTENCE:JAVADOC:1603;
   * 
   * @test_Strategy:
   */

  public void getParametersTest() throws Fault {
    boolean pass1 = false;
    boolean pass3 = false;
    try {
      getEntityTransaction().begin();

      StoredProcedureQuery spq = getEntityManager()
          .createStoredProcedureQuery("GetEmpFirstNameFromOut");
      Set<Parameter<?>> p1 = spq.getParameters();
      if (p1.size() == 0) {
        TestUtil
            .logTrace("Received expected number of parameters when non exist:"
                + p1.size());
        pass1 = true;
      } else {
        TestUtil.logErr(
            "Expected number of parameters when non exist: 0, actual:" + p1);
      }
      spq.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
      spq.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
      spq.setParameter(1, 1);

      if (!spq.execute()) {
        Set<Parameter<?>> p2 = spq.getParameters();
        if (p2.size() == 2) {
          TestUtil
              .logTrace("Received expected number of parameters:" + p2.size());
          pass3 = true;
        } else {
          TestUtil
              .logErr("Expected number of parameters: 2, actual:" + p2.size());
        }
      } else {
        TestUtil.logErr("Expected execute() to return false, actual: true");
      }
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass1 || !pass3)
      throw new Fault("getParametersTest failed");

  }

  /*
   * @testName: setParameterIntCalendarTemporalTypeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1576;
   * 
   * @test_Strategy:
   */
  @SetupMethod(name = "setupEmployee2Data")
  public void setParameterIntCalendarTemporalTypeTest() throws Fault {
    boolean pass2 = false;
    boolean pass4 = false;
    try {
      getEntityTransaction().begin();
      try {
        TestUtil.logMsg("Testing StoredProcedureQuery object");

        StoredProcedureQuery spq = getEntityManager()
            .createStoredProcedureQuery("GetEmpIdUsingHireDateFromOut");
        spq.registerStoredProcedureParameter(1, Calendar.class,
            ParameterMode.IN);
        spq.registerStoredProcedureParameter(2, Integer.class,
            ParameterMode.OUT);
        spq.setParameter(1, calDate, TemporalType.DATE);

        if (!spq.execute()) {
          Object o = spq.getOutputParameterValue(2);
          if (o instanceof Integer) {
            int actual = (Integer) o;
            if (actual == emp2.getId()) {
              TestUtil.logTrace("Received expected id:" + actual);
              pass2 = true;
            } else {
              TestUtil.logErr(
                  "Expected id: " + emp2.getId() + ", actual:" + actual);
            }

          } else {
            TestUtil.logErr("Did not get instance of Integer back:" + o);
          }
        } else {
          TestUtil.logErr("Expected execute() to return false, actual: true");
        }
      } catch (Exception e) {
        TestUtil.logErr("Caught exception: ", e);
      }
      try {
        TestUtil.logMsg("Testing Query object");
        StoredProcedureQuery spq1 = getEntityManager()
            .createStoredProcedureQuery("GetEmpIdUsingHireDateFromOut");
        spq1.registerStoredProcedureParameter(1, Calendar.class,
            ParameterMode.IN);
        spq1.registerStoredProcedureParameter(2, Integer.class,
            ParameterMode.OUT);
        Query q = spq1;
        q.setParameter(1, getCalDate(), TemporalType.DATE);
        StoredProcedureQuery spq2 = (StoredProcedureQuery) q;
        if (!spq2.execute()) {
          Object o = spq2.getOutputParameterValue(2);
          if (o instanceof Integer) {
            int actual = (Integer) o;
            if (actual == 5) {
              TestUtil.logTrace("Received expected id:" + actual);
              pass4 = true;
            } else {
              TestUtil.logErr("Expected id: 5, actual:" + actual);
            }

          } else {
            TestUtil.logErr("Did not get instance of Integer back:" + o);
          }
        } else {
          TestUtil.logErr("Expected execute() to return false, actual: true");
        }
      } catch (Exception e) {
        TestUtil.logErr("Caught exception: ", e);
      }

      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass2 || !pass4)
      throw new Fault("setParameterIntCalendarTemporalTypeTest failed");

  }

  /*
   * @testName: setParameterIntCalendarTemporalTypeIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1577;
   * 
   * @test_Strategy:
   */
  @SetupMethod(name = "setupEmployee2Data")
  public void setParameterIntCalendarTemporalTypeIllegalArgumentExceptionTest()
      throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    try {
      getEntityTransaction().begin();
      TestUtil.logMsg(
          "Testing StoredProcedureQuery with incorrect position specified");

      StoredProcedureQuery spq1 = getEntityManager()
          .createStoredProcedureQuery("GetEmpIdUsingHireDateFromOut");
      spq1.registerStoredProcedureParameter(1, Calendar.class,
          ParameterMode.IN);
      try {
        spq1.setParameter(99, getCalDate(), TemporalType.DATE);
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
        pass1 = true;
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception", e);
      }

      TestUtil.logMsg("Testing Query object with incorrect position specified");
      StoredProcedureQuery spq3 = getEntityManager()
          .createStoredProcedureQuery("GetEmpIdUsingHireDateFromOut");
      spq3.registerStoredProcedureParameter(1, Calendar.class,
          ParameterMode.IN);
      Query q1 = spq3.setParameter(1, getCalDate(), TemporalType.DATE);
      try {
        q1.setParameter(99, getCalDate());
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
        pass2 = true;
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception", e);
      }

      if (getEntityTransaction().isActive()) {
        getEntityTransaction().rollback();
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception", e);
    }
    if (!pass1 || !pass2)
      throw new Fault(
          "setParameterIntCalendarTemporalTypeIllegalArgumentExceptionTest failed");

  }

  /*
   * @testName: setParameterIntDateTemporalTypeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1578;
   * 
   * @test_Strategy:
   */

  public void setParameterIntDateTemporalTypeTest() throws Fault {
    boolean pass2 = false;
    boolean pass4 = false;
    try {
      getEntityTransaction().begin();
      try {
        TestUtil.logMsg("Testing StoredProcedureQuery");

        StoredProcedureQuery spq = getEntityManager()
            .createStoredProcedureQuery("GetEmpIdUsingHireDateFromOut");
        spq.registerStoredProcedureParameter(1, Date.class, ParameterMode.IN);
        spq.registerStoredProcedureParameter(2, Integer.class,
            ParameterMode.OUT);
        spq.setParameter(1, utilDate, TemporalType.DATE);

        if (!spq.execute()) {

          Object o = spq.getOutputParameterValue(2);
          if (o instanceof Integer) {
            Integer actual = (Integer) o;
            if (actual == 1) {
              TestUtil.logTrace("Received expected id:" + actual);
              pass2 = true;
            } else {
              TestUtil.logErr("Expected id: 1, actual:" + actual);
            }

          } else {
            TestUtil.logErr("Did not get instance of Integer back:" + o);
          }
        } else {
          TestUtil.logErr("Expected execute() to return false, actual: true");
        }
      } catch (Exception e) {
        TestUtil.logErr("Caught exception: ", e);
      }
      try {
        TestUtil.logMsg("Testing Query object");
        StoredProcedureQuery spq1 = getEntityManager()
            .createStoredProcedureQuery("GetEmpIdUsingHireDateFromOut");
        spq1.registerStoredProcedureParameter(1, Date.class, ParameterMode.IN);
        spq1.registerStoredProcedureParameter(2, Integer.class,
            ParameterMode.OUT);
        Query q = spq1;
        q.setParameter(1, utilDate, TemporalType.DATE);
        StoredProcedureQuery spq2 = (StoredProcedureQuery) q;

        if (!spq2.execute()) {

          Object o = spq2.getOutputParameterValue(2);
          if (o instanceof Integer) {
            int actual = (Integer) o;
            if (actual == 1) {
              TestUtil.logTrace("Received expected id:" + actual);
              pass4 = true;
            } else {
              TestUtil.logErr("Expected id: 1, actual:" + actual);
            }

          } else {
            TestUtil.logErr("Did not get instance of Integer back:" + o);
          }
        } else {
          TestUtil.logErr("Expected execute() to return false, actual: true");
        }
      } catch (Exception e) {
        TestUtil.logErr("Caught exception: ", e);
      }
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass2 || !pass4)
      throw new Fault("setParameterIntDateTemporalTypeTest failed");

  }

  /*
   * @testName: setParameterIntDateTemporalTypeIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1579;
   * 
   * @test_Strategy:
   */

  public void setParameterIntDateTemporalTypeIllegalArgumentExceptionTest()
      throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    try {
      getEntityTransaction().begin();
      TestUtil.logMsg(
          "Testing StoredProcedureQuery with incorrect position specified");

      StoredProcedureQuery spq1 = getEntityManager()
          .createStoredProcedureQuery("GetEmpIdUsingHireDateFromOut");
      spq1.registerStoredProcedureParameter(1, Date.class, ParameterMode.IN);
      try {
        spq1.setParameter(99, utilDate, TemporalType.DATE);
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
        pass1 = true;
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception", e);
      }

      TestUtil.logMsg("Testing Query object with incorrect position specified");
      StoredProcedureQuery spq3 = getEntityManager()
          .createStoredProcedureQuery("GetEmpIdUsingHireDateFromOut");
      spq3.registerStoredProcedureParameter(1, Date.class, ParameterMode.IN);
      Query q1 = spq3.setParameter(1, getUtilDate());
      try {
        q1.setParameter(99, utilDate, TemporalType.DATE);
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
        pass2 = true;
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception", e);
      }

      if (getEntityTransaction().isActive()) {
        getEntityTransaction().rollback();
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception", e);
    }

    if (!pass1 || !pass2)
      throw new Fault(
          "setParameterIntDateTemporalTypeIllegalArgumentExceptionTest failed");
  }

  /*
   * @testName: setParameterParameterCalendarTemporalTypeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1564; PERSISTENCE:SPEC:1576;
   * 
   * @test_Strategy:
   */
  @SetupMethod(name = "setupEmployee2Data")
  public void setParameterParameterCalendarTemporalTypeTest() throws Fault {
    boolean pass2 = false;
    boolean pass3 = false;
    boolean pass5 = false;
    try {
      TestUtil.logMsg("Testing StoredProcedure");
      getEntityTransaction().begin();
      StoredProcedureQuery spq = getEntityManager()
          .createStoredProcedureQuery("GetEmpIdUsingHireDateFromOut");
      spq.registerStoredProcedureParameter(1, Calendar.class, ParameterMode.IN);
      spq.registerStoredProcedureParameter(2, Integer.class, ParameterMode.OUT);

      spq.setParameter(1, getCalDate(), TemporalType.DATE);
      Parameter p = spq.getParameter(1);
      spq.setParameter(p, calDate, TemporalType.DATE);

      if (!spq.execute()) {
        Object o = spq.getOutputParameterValue(2);
        if (o instanceof Integer) {
          int actual = (Integer) o;
          if (actual == emp2.getId()) {
            TestUtil.logTrace("Received expected id:" + actual);
            pass2 = true;
          } else {
            TestUtil
                .logErr("Expected id: " + emp2.getId() + ", actual:" + actual);
          }

        } else {
          TestUtil.logErr("Did not get instance of Integer back:" + o);
        }
      } else {
        TestUtil.logErr("Expected execute() to return false, actual: true");
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
      if (getEntityTransaction().isActive()) {
        getEntityTransaction().rollback();
      }
    }
    try {
      getEntityTransaction().begin();
      TestUtil.logMsg("Testing Query object");
      StoredProcedureQuery spq1 = getEntityManager()
          .createStoredProcedureQuery("GetEmpIdUsingHireDateFromOut");
      spq1.registerStoredProcedureParameter(1, Calendar.class,
          ParameterMode.IN);
      spq1.registerStoredProcedureParameter(2, Integer.class,
          ParameterMode.OUT);
      spq1.setParameter(1, getCalDate(), TemporalType.DATE);
      Query q = spq1;

      Parameter p = q.getParameter(1);
      q.setParameter(p, calDate, TemporalType.DATE);
      Parameter p2 = q.getParameter(1);
      if (p.getPosition().equals(p2.getPosition())
          && p.getParameterType().equals(p2.getParameterType())) {
        TestUtil.logTrace("Received expected parameter");
        pass3 = true;
      } else {
        TestUtil.logErr("Expected parameter:" + p + ", actual:" + p2);
      }

      StoredProcedureQuery spq2 = (StoredProcedureQuery) q;

      if (!spq2.execute()) {
        Object o = spq2.getOutputParameterValue(2);
        if (o instanceof Integer) {
          int actual = (Integer) o;
          if (actual == emp2.getId()) {
            TestUtil.logTrace("Received expected id:" + actual);
            pass5 = true;
          } else {
            TestUtil
                .logErr("Expected id: " + emp2.getId() + ", actual:" + actual);
          }

        } else {
          TestUtil.logErr("Did not get instance of Integer back:" + o);
        }
      } else {
        TestUtil.logErr("Expected execute() to return false, actual: true");
      }
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
      if (getEntityTransaction().isActive()) {
        getEntityTransaction().rollback();
      }
    }

    if (!pass2 || !pass3 || !pass5)
      throw new Fault("setParameterParameterCalendarTemporalTypeTest failed");
  }

  /*
   * @testName:
   * setParameterParameterCalendarTemporalTypeIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1565;
   * 
   * @test_Strategy:
   */
  @SetupMethod(name = "setupEmployee2Data")
  public void setParameterParameterCalendarTemporalTypeIllegalArgumentExceptionTest()
      throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("Get parameter from other stored procedure");
      StoredProcedureQuery spq = getEntityManager()
          .createStoredProcedureQuery("GetEmpLastNameFromInOut");
      spq.registerStoredProcedureParameter(1, String.class,
          ParameterMode.INOUT);
      spq.setParameter(1, "INOUT");
      // Parameter to be used in next StoredProcedure
      Parameter p = spq.getParameter(1);

      TestUtil.logMsg(
          "Testing StoredProcedureQuery with parameter specified from another query");
      StoredProcedureQuery spq1 = getEntityManager()
          .createStoredProcedureQuery("GetEmpIdUsingHireDateFromOut");
      spq1.registerStoredProcedureParameter(1, Calendar.class,
          ParameterMode.IN);
      spq1.setParameter(1, getCalDate(), TemporalType.DATE);
      try {
        spq1.setParameter(p, getCalDate(), TemporalType.DATE);
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
        pass1 = true;
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception", e);
      }

      TestUtil.logMsg(
          "Testing Query object with parameter specified from another query");
      StoredProcedureQuery spq3 = getEntityManager()
          .createStoredProcedureQuery("GetEmpIdUsingHireDateFromOut");
      spq3.registerStoredProcedureParameter(1, Calendar.class,
          ParameterMode.IN);
      Query q1 = spq3.setParameter(1, getCalDate());
      try {
        q1.setParameter(p, getCalDate(), TemporalType.DATE);
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
        pass2 = true;
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception", e);
      }

      if (getEntityTransaction().isActive()) {
        getEntityTransaction().rollback();
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception", e);
    }

    if (!pass1 || !pass2)
      throw new Fault(
          "setParameterParameterCalendarTemporalTypeIllegalArgumentExceptionTest failed");
  }

  /*
   * @testName: setParameterParameterDateTemporalTypeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1566;
   * 
   * @test_Strategy:
   */
  public void setParameterParameterDateTemporalTypeTest() throws Fault {
    boolean pass2 = false;
    boolean pass3 = false;
    boolean pass5 = false;
    try {
      getEntityTransaction().begin();

      StoredProcedureQuery spq = getEntityManager()
          .createStoredProcedureQuery("GetEmpIdUsingHireDateFromOut");
      spq.registerStoredProcedureParameter(1, Date.class, ParameterMode.IN);
      spq.registerStoredProcedureParameter(2, Integer.class, ParameterMode.OUT);
      spq.setParameter(1, getUtilDate(), TemporalType.DATE);

      Parameter p = spq.getParameter(1);
      spq.setParameter(p, utilDate, TemporalType.DATE);

      if (!spq.execute()) {
        Object o = spq.getOutputParameterValue(2);
        if (o instanceof Integer) {
          int actual = (Integer) o;
          if (actual == 1) {
            TestUtil.logTrace("Received expected id:" + actual);
            pass2 = true;
          } else {
            TestUtil.logErr("Expected id: 1, actual:" + actual);
          }

        } else {
          TestUtil.logErr("Did not get instance of Integer back:" + o);
        }
      } else {
        TestUtil.logErr("Expected execute() to return false, actual: true");
      }
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
      if (getEntityTransaction().isActive()) {
        getEntityTransaction().rollback();
      }
    }
    try {
      getEntityTransaction().begin();

      TestUtil.logMsg("Testing Query object");
      StoredProcedureQuery spq1 = getEntityManager()
          .createStoredProcedureQuery("GetEmpIdUsingHireDateFromOut");
      spq1.registerStoredProcedureParameter(1, Date.class, ParameterMode.IN);
      spq1.registerStoredProcedureParameter(2, Integer.class,
          ParameterMode.OUT);
      spq1.setParameter(1, getCalDate().getTime(), TemporalType.DATE);

      Query q = spq1;

      // override the previously set parameter
      Parameter p = q.getParameter(1);
      q.setParameter(p, utilDate, TemporalType.DATE);

      Parameter p2 = q.getParameter(1);
      if (p.getPosition().equals(p2.getPosition())
          && p.getParameterType().equals(p2.getParameterType())) {
        TestUtil.logTrace("Received expected parameter");
        pass3 = true;
      } else {
        TestUtil.logErr("Expected parameter:" + p + ", actual:" + p2);
      }
      StoredProcedureQuery spq2 = (StoredProcedureQuery) q;
      if (!spq2.execute()) {

        Object o = spq2.getOutputParameterValue(2);
        if (o instanceof Integer) {
          int actual = (Integer) o;
          if (actual == emp0.getId()) {
            TestUtil.logTrace("Received expected id:" + actual);
            pass5 = true;
          } else {
            TestUtil.logErr("Expected id: 1, actual:" + actual);
          }

        } else {
          TestUtil.logErr("Did not get instance of Integer back:" + o);
        }
      } else {
        TestUtil.logErr("Expected execute() to return false, actual: true");
      }
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
      if (getEntityTransaction().isActive()) {
        getEntityTransaction().rollback();
      }
    }

    if (!pass2 || !pass3 || !pass5)
      throw new Fault("setParameterParameterDateTemporalTypeTest failed");
  }

  /*
   * @testName:
   * setParameterParameterDateTemporalTypeIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1567;
   * 
   * @test_Strategy:
   */

  public void setParameterParameterDateTemporalTypeIllegalArgumentExceptionTest()
      throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("Get parameter from other stored procedure");
      StoredProcedureQuery spq = getEntityManager()
          .createStoredProcedureQuery("GetEmpLastNameFromInOut");
      spq.registerStoredProcedureParameter(1, String.class,
          ParameterMode.INOUT);
      spq.setParameter(1, "INOUT");
      // Parameter to be used in next StoredProcedure
      Parameter p = spq.getParameter(1);
      TestUtil.logMsg(
          "Testing StoredProcedureQuery with parameter specified from another query");
      StoredProcedureQuery spq1 = getEntityManager()
          .createStoredProcedureQuery("GetEmpIdUsingHireDateFromOut");
      spq1.registerStoredProcedureParameter(1, Date.class, ParameterMode.IN);
      spq1.setParameter(1, getUtilDate(), TemporalType.DATE);
      try {
        spq1.setParameter(p, getUtilDate(), TemporalType.DATE);
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
        pass1 = true;
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception", e);
      }

      TestUtil.logMsg(
          "Testing Query object with parameter specified from another query");
      StoredProcedureQuery spq3 = getEntityManager()
          .createStoredProcedureQuery("GetEmpIdUsingHireDateFromOut");
      spq3.registerStoredProcedureParameter(1, Calendar.class,
          ParameterMode.IN);
      Query q1 = spq3.setParameter(1, getCalDate());
      try {
        q1.setParameter(p, getCalDate(), TemporalType.DATE);
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
        pass2 = true;
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception", e);
      }

      if (getEntityTransaction().isActive()) {
        getEntityTransaction().rollback();
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception", e);
    }

    if (!pass1 || !pass2)
      throw new Fault(
          "setParameterParameterDateTemporalTypeIllegalArgumentExceptionTest failed");
  }

  /*
   * @testName: executeUpdateOfAnUpdateTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1580; PERSISTENCE:JAVADOC:1551;
   * PERSISTENCE:SPEC:1516; PERSISTENCE:SPEC:1580;
   * 
   * @test_Strategy:
   */
  public void executeUpdateOfAnUpdateTest() throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      StoredProcedureQuery spq = getEntityManager()
          .createStoredProcedureQuery("UpdateEmpSalaryColumn");

      spq.executeUpdate();
      int updateCount = spq.getUpdateCount();
      if (updateCount == -1) {
        TestUtil.logTrace("Received expected update count:" + updateCount);
        pass = true;
      } else {
        TestUtil.logErr("Expected update count: -1, actual:" + updateCount);
      }
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass)
      throw new Fault("executeUpdateOfAnUpdateTest failed");
  }

  /*
   * @testName: executeUpdateOfADeleteTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1580; PERSISTENCE:JAVADOC:1551;
   * PERSISTENCE:SPEC:1516;
   * 
   * @test_Strategy:
   */
  public void executeUpdateOfADeleteTest() throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      StoredProcedureQuery spq = getEntityManager()
          .createStoredProcedureQuery("DeleteAllEmp");
      spq.executeUpdate();
      int updateCount = spq.getUpdateCount();
      if (updateCount == -1) {
        TestUtil.logTrace("Received expected update count:" + updateCount);
        pass = true;
      } else {
        TestUtil.logErr("Expected update count: -1, actual:" + updateCount);
      }
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass)
      throw new Fault("executeUpdateOfADeleteTest failed");
  }

  /*
   * @testName: executeUpdateTransactionRequiredExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1582;
   * 
   * @test_Strategy:
   */
  public void executeUpdateTransactionRequiredExceptionTest() throws Fault {
    boolean pass = false;
    try {
      StoredProcedureQuery spq = getEntityManager()
          .createStoredProcedureQuery("DeleteAllEmp");
      try {
        TestUtil
            .logMsg("IsActive returns:" + getEntityTransaction().isActive());
        spq.executeUpdate();
        TestUtil.logErr("Did not throw TransactionRequiredException");
      } catch (TransactionRequiredException tre) {
        TestUtil.logTrace("Received expected TransactionRequiredException");
        pass = true;
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception", e);
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass)
      throw new Fault("executeUpdateTransactionRequiredExceptionTest failed");
  }

  /*
   * @testName: getParameterValueParameterTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1605; PERSISTENCE:JAVADOC:1630;
   * 
   * @test_Strategy:
   */
  public void getParameterValueParameterTest() throws Fault {
    boolean pass1 = false;
    boolean pass3 = false;
    try {
      getEntityTransaction().begin();
      StoredProcedureQuery spq = getEntityManager()
          .createStoredProcedureQuery("GetEmpFirstNameFromOut");
      spq.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
      spq.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
      spq.setParameter(1, 1);
      Parameter p = spq.getParameter(1);
      boolean b = spq.isBound(p);
      if (b == true) {
        TestUtil.logTrace("Received expected from isBound():" + b);
        pass1 = true;
      } else {
        TestUtil.logErr("Expected result from isBound():true, actual:" + b);
      }
      if (!spq.execute()) {
        p = spq.getParameter(1);
        Object o = spq.getParameterValue(p);
        if (o instanceof Integer) {
          TestUtil.logTrace("Received expected parameter type: Integer");
          Integer i = (Integer) o;
          if (i.equals(emp0.getId())) {
            TestUtil.logTrace("Received expected parameter value:" + i);
            pass3 = true;
          } else {
            TestUtil.logErr("Expected getParameterValue() result: "
                + emp0.getId() + ", actual:" + i);
          }

        } else {
          TestUtil.logErr(
              "Did not get instance of Integer from getParameterValue():" + o);
        }

      } else {
        TestUtil.logErr("Expected execute() to return false, actual: true");
      }
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass1 || !pass3)
      throw new Fault("getParameterValueParameterTest failed");

  }

  /*
   * @testName: getParameterValueParameterIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1606;
   * 
   * @test_Strategy: execute getParameterValue using parameter from different
   * query
   */
  public void getParameterValueParameterIllegalArgumentExceptionTest()
      throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();

      StoredProcedureQuery spq = getEntityManager()
          .createStoredProcedureQuery("GetEmpLastNameFromInOut");
      spq.registerStoredProcedureParameter(1, String.class,
          ParameterMode.INOUT);
      spq.setParameter(1, "1");
      Parameter p = spq.getParameter(1);

      StoredProcedureQuery spq1 = getEntityManager()
          .createStoredProcedureQuery("GetEmpFirstNameFromOut");
      spq1.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
      spq1.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
      spq1.setParameter(1, 1);
      try {
        spq1.getParameterValue(p);
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
        pass = true;
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception", e);
      }
      if (getEntityTransaction().isActive()) {
        getEntityTransaction().rollback();
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass)
      throw new Fault(
          "getParameterValueParameterIllegalArgumentExceptionTest failed");

  }

  /*
   * @testName: getParameterValueParameterIllegalStateExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1607; PERSISTENCE:JAVADOC:1630;
   * 
   * @test_Strategy:
   */
  public void getParameterValueParameterIllegalStateExceptionTest()
      throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    try {
      getEntityTransaction().begin();
      StoredProcedureQuery spq = getEntityManager()
          .createStoredProcedureQuery("GetEmpFirstNameFromOut");
      spq.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
      spq.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
      spq.setParameter(1, 1);
      Parameter p = spq.getParameter(1);
      boolean b = spq.isBound(p);
      if (b == true) {
        TestUtil.logTrace("Received expected from isBound():" + b);
        pass1 = true;
      } else {
        TestUtil.logErr("Expected result from isBound():true, actual:" + b);
      }
      Parameter p2 = spq.getParameter(2);

      try {
        spq.getParameterValue(p2);
        TestUtil.logErr("Did not throw IllegalStateException");
      } catch (IllegalStateException iae) {
        TestUtil.logTrace("Received expected IllegalStateException");
        pass2 = true;
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception", e);
      }
      if (getEntityTransaction().isActive()) {
        getEntityTransaction().rollback();
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass1 || !pass2)
      throw new Fault(
          "getParameterValueParameterIllegalStateExceptionTest failed");

  }

  /*
   * @testName: getParameterValueIntTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1611; PERSISTENCE:SPEC:1572;
   * 
   * @test_Strategy:
   */
  public void getParameterValueIntTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    boolean pass4 = false;
    try {
      getEntityTransaction().begin();

      TestUtil.logMsg("Testing createStoredProcedureQuery");
      StoredProcedureQuery spq = getEntityManager()
          .createStoredProcedureQuery("GetEmpLastNameFromInOut");
      spq.registerStoredProcedureParameter(1, String.class,
          ParameterMode.INOUT);
      spq.setParameter(1, "1");
      Object o = spq.getParameterValue(1);
      if (o instanceof String) {
        TestUtil.logTrace("Received expected parameter type: String");
        String s = (String) o;
        if (s.equals("1")) {
          TestUtil.logTrace("Received expected parameter value:" + s);
          pass1 = true;
        } else {
          TestUtil
              .logErr("Expected getParameterValue() result: 1, actual:" + s);
        }

      } else {
        TestUtil.logErr(
            "Did not get instance of String from getParameterValue():" + o);
      }

      if (!spq.execute()) {
        o = spq.getParameterValue(1);
        if (o instanceof String) {
          TestUtil.logTrace("Received expected parameter type: String");
          String s = (String) o;
          if (s.equals("1")) {
            TestUtil.logTrace("Received expected parameter value:" + s);
            pass2 = true;
          } else {
            TestUtil
                .logErr("Expected getParameterValue() result: 1, actual:" + s);
          }
        }
      } else {
        TestUtil.logErr("Expected execute() to return false, actual: true");
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      if (getEntityTransaction().isActive()) {
        getEntityTransaction().rollback();
      }
    }
    try {
      getEntityTransaction().begin();

      TestUtil.logMsg("Testing createNamedStoredProcedureQuery");
      StoredProcedureQuery spq = getEntityManager()
          .createNamedStoredProcedureQuery("getemplastnamefrominout");
      spq.setParameter(1, "1");
      Object o = spq.getParameterValue(1);
      if (o instanceof String) {
        TestUtil.logTrace("Received expected parameter type: String");
        String s = (String) o;
        if (s.equals("1")) {
          TestUtil.logTrace("Received expected parameter value:" + s);
          pass3 = true;
        } else {
          TestUtil
              .logErr("Expected getParameterValue() result: 1, actual:" + s);
        }

      } else {
        TestUtil.logErr(
            "Did not get instance of String from getParameterValue():" + o);
      }

      if (!spq.execute()) {
        o = spq.getParameterValue(1);
        if (o instanceof String) {
          TestUtil.logTrace("Received expected parameter type: String");
          String s = (String) o;
          if (s.equals("1")) {
            TestUtil.logTrace("Received expected parameter value:" + s);
            pass4 = true;
          } else {
            TestUtil
                .logErr("Expected getParameterValue() result: 1, actual:" + s);
          }
        }
      } else {
        TestUtil.logErr("Expected execute() to return false, actual: true");
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      if (getEntityTransaction().isActive()) {
        getEntityTransaction().rollback();
      }
    }

    if (!pass1 || !pass2 || !pass3 || !pass4)
      throw new Fault("getParameterValueIntTest failed");

  }

  /*
   * @testName: getParameterValueIntIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1613;
   * 
   * @test_Strategy:
   */

  public void getParameterValueIntIllegalArgumentExceptionTest() throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();

      StoredProcedureQuery spq = getEntityManager()
          .createStoredProcedureQuery("GetEmpFirstNameFromOut");
      spq.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
      spq.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
      spq.setParameter(1, 1);
      try {
        spq.getParameterValue(99);
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
        pass = true;
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception", e);
      }
      if (getEntityTransaction().isActive()) {
        getEntityTransaction().rollback();
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass)
      throw new Fault(
          "getParameterValueIntIllegalArgumentExceptionTest failed");

  }

  /*
   * @testName: getParameterValueIntIllegalStateExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1612;
   * 
   * @test_Strategy:
   */
  public void getParameterValueIntIllegalStateExceptionTest() throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      StoredProcedureQuery spq = getEntityManager()
          .createStoredProcedureQuery("GetEmpFirstNameFromOut");
      spq.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
      spq.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
      spq.setParameter(1, 1);
      try {
        spq.getParameterValue(2);
        TestUtil.logErr("Did not throw IllegalStateException");
      } catch (IllegalStateException iae) {
        TestUtil.logTrace("Received expected IllegalStateException");
        pass = true;
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception", e);
      }
      if (getEntityTransaction().isActive()) {
        getEntityTransaction().rollback();
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass)
      throw new Fault("getParameterValueIntIllegalStateExceptionTest failed");

  }

  /*
   * @testName: setHintStringObjectTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1560;
   * 
   * @test_Strategy: Vendor-specific hints that are not recognized by a provider
   * must be silently ignored.
   */
  public void setHintStringObjectTest() throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      StoredProcedureQuery spq = getEntityManager()
          .createStoredProcedureQuery("GetEmpFirstNameFromOut");
      spq.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
      spq.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
      spq.setParameter(1, 1);
      spq.getParameterValue(1);
      spq.setHint("property.that.is.not.recognized",
          "property.that.is.not.recognized");
      spq.execute();
      getEntityTransaction().commit();
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass)
      throw new Fault("setHintStringObjectTest failed");

  }

  /*
   * @testName: xmlOverridesNamedStoredProcedureQueryTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:2239; PERSISTENCE:SPEC:2241;
   * PERSISTENCE:SPEC:2242;
   * 
   * @test_Strategy: verify xml overrides NamedStoredProcedureQuery annotation
   */
  public void xmlOverridesNamedStoredProcedureQueryTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;

    try {

      StoredProcedureQuery spq = getEntityManager()
          .createNamedStoredProcedureQuery("tobeoverridden1");
      spq.setParameter(1, 1);
      Object o = spq.getParameterValue(1);
      if (o instanceof Integer) {
        TestUtil.logTrace("Received expected parameter type: Integer");
        Integer i = (Integer) o;
        if (i.equals(1)) {
          TestUtil.logTrace("Received expected parameter value:" + i);
          pass1 = true;
        } else {
          TestUtil
              .logErr("Expected getParameterValue() result: 1, actual:" + i);
        }

      } else {
        TestUtil.logErr(
            "Did not get instance of Integer from getParameterValue():" + o);
      }

      if (!spq.execute()) {
        o = spq.getOutputParameterValue(2);
        if (o instanceof String) {
          TestUtil.logTrace("Received expected parameter type: String");
          String s = (String) o;
          if (s.equals(emp0.getFirstName())) {
            TestUtil.logTrace("Received expected parameter value:" + s);
            pass2 = true;
          } else {
            TestUtil.logErr("Expected getParameterValue() result: "
                + emp0.getFirstName() + ", actual:" + s);
          }
        }
      } else {
        TestUtil.logErr("Expected execute() to return false, actual: true");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass1 || !pass2)
      throw new Fault("xmlOverridesNamedStoredProcedureQueryTest failed");

  }

  /*
   * @testName: xmlOverridesSqlResultSetMappingAnnotationTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:2243; PERSISTENCE:SPEC:2245;
   * PERSISTENCE:SPEC:2246;
   * 
   * @test_Strategy: verify xml overrides SqlResultSetMapping annotation
   *
   */
  public void xmlOverridesSqlResultSetMappingAnnotationTest() throws Fault {
    boolean pass = false;
    try {
      getEntityTransaction().begin();
      try {
        TestUtil.logMsg("Testing using name,result mapping");
        clearCache();
        StoredProcedureQuery spq = getEntityManager()
            .createStoredProcedureQuery("GetEmpIdFNameLNameFromRS",
                "tobeoverridden2");
        spq.registerStoredProcedureParameter(1, Integer.class,
            ParameterMode.IN);
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

    } catch (Exception ex) {
      TestUtil.logErr("Received unexpected exception:", ex);
    }

    if (!pass) {
      throw new Fault("xmlOverridesSqlResultSetMappingAnnotationTest failed");
    }

  }

  private void createEmployeeTestData() {

    try {
      getEntityTransaction().begin();

      TestUtil.logMsg("Creating Employees");

      final Date d2 = getUtilDate("2001-06-27");
      final Date d3 = getUtilDate("2002-07-07");
      final Date d4 = getUtilDate("2003-03-03");
      final Date d5 = getUtilDate();

      emp0 = new Employee(1, "Alan", "Frechette", utilDate, (float) 35000.0);
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

  private void createEmployee2TestData() {

    try {
      getEntityTransaction().begin();

      TestUtil.logMsg("Creating Employee2");

      final Calendar d2 = getCalDate(2001, 06, 27);
      final Calendar d3 = getCalDate(2002, 07, 07);
      final Calendar d4 = getCalDate(2003, 03, 03);
      final Calendar d5 = getCalDate();

      emp2 = new Employee2(1, "Alan", "Frechette", calDate, (float) 35000.0);
      empRef2.add(emp2);
      empRef2.add(new Employee2(2, "Arthur", "Frechette", d2, (float) 35000.0));
      empRef2.add(new Employee2(3, "Shelly", "McGowan", d3, (float) 50000.0));
      empRef2.add(new Employee2(4, "Robert", "Bissett", d4, (float) 55000.0));
      empRef2.add(new Employee2(5, "Stephen", "DMilla", d5, (float) 25000.0));
      for (Employee2 e : empRef2) {
        if (e != null) {
          getEntityManager().persist(e);
          TestUtil.logTrace("persisted Employee2:" + e);
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
      getEntityManager().createNativeQuery("DELETE FROM INSURANCE")
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
