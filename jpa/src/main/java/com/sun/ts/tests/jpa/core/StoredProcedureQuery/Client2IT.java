/*
 * Copyright (c) 2013, 2023 Oracle and/or its affiliates. All rights reserved.
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;

import jakarta.persistence.Parameter;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.TemporalType;

public class Client2IT extends Client {

  public Client2IT() {
  }
  
  @Deployment(testable = false, managed = false)
  public static JavaArchive createDeployment() throws Exception {
     
     String pkgNameWithoutSuffix = Client2IT.class.getPackageName();
     String pkgName = Client2IT.class.getPackageName() + ".";
     String[] classes = { pkgName + "Employee", pkgName + "Employee2", pkgName + "EmployeeMappedSc"};
     return createDeploymentJar("jpa_core_types_StoredProcedureQuery2.jar", pkgNameWithoutSuffix, classes);

  }



  /*
   * setupEmployee2Data() is called before each test
   *
   * @class.setup_props: jdbc.db;
   */
  @BeforeAll
  public void setupEmployee2Data() throws Exception {
    TestUtil.logTrace("setup");
    try {
      super.setup();
      removeTestData();
      createEmployee2TestData();
      dataBaseName = System.getProperty("jdbc.db");
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Exception("Setup failed:", e);
    }
  }

  @AfterAll
  public void cleanup() throws Exception {
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
   * @testName: setParameterIntCalendarTemporalTypeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1576;
   * 
   * @test_Strategy:
   */
  @Test
  public void setParameterIntCalendarTemporalTypeTest() throws Exception {
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
      throw new Exception("setParameterIntCalendarTemporalTypeTest failed");

  }

  /*
   * @testName: setParameterIntCalendarTemporalTypeIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1577;
   * 
   * @test_Strategy:
   */
  @Test
  public void setParameterIntCalendarTemporalTypeIllegalArgumentExceptionTest()
      throws Exception {
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
      throw new Exception(
          "setParameterIntCalendarTemporalTypeIllegalArgumentExceptionTest failed");

  }

 
  /*
   * @testName: setParameterParameterCalendarTemporalTypeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1564; PERSISTENCE:SPEC:1576;
   * 
   * @test_Strategy:
   */
  @Test
  public void setParameterParameterCalendarTemporalTypeTest() throws Exception {
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
      throw new Exception("setParameterParameterCalendarTemporalTypeTest failed");
  }

  /*
   * @testName:
   * setParameterParameterCalendarTemporalTypeIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1565;
   * 
   * @test_Strategy:
   */
  @Test
  public void setParameterParameterCalendarTemporalTypeIllegalArgumentExceptionTest()
      throws Exception {
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
      throw new Exception(
          "setParameterParameterCalendarTemporalTypeIllegalArgumentExceptionTest failed");
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

}
