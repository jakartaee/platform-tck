/*
 * Copyright (c) 2011, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.criteriaapi.parameter;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.tests.jpa.common.PMClientBase;

import javax.persistence.*;
import javax.persistence.criteria.*;

import java.util.*;

public class Client extends PMClientBase {

  Employee[] empRef = new Employee[5];

  final java.sql.Date d1 = getSQLDate("2000-02-14");

  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    try {
      super.setup(args, p);
      getEntityManager();
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  public void setupEmployee(String[] args, Properties p) throws Fault {
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

  /*
   * @testName: parameterTest1
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:381; PERSISTENCE:JAVADOC:382;
   * PERSISTENCE:JAVADOC:383; PERSISTENCE:JAVADOC:1093;
   * PERSISTENCE:JAVADOC:1092;
   * 
   * @test_Strategy: Create a query with 2 named parameters and retrieve
   * information about the parameters.
   */
  public void parameterTest1() throws Fault {
    TestUtil.logTrace("Starting parameterTest1");
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    boolean pass4 = false;
    boolean pass5 = false;
    boolean pass6 = false;
    boolean pass7 = false;
    boolean pass8 = false;
    boolean pass9 = false;
    CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();
    CriteriaQuery<Employee> cquery = qbuilder.createQuery(Employee.class);
    if (cquery != null) {
      Root<Employee> employee = cquery.from(Employee.class);

      cquery.select(employee);

      List<Predicate> criteria = new ArrayList<Predicate>();
      ParameterExpression<String> pe = qbuilder.parameter(String.class,
          "first");
      Class<?> c = pe.getParameterType();
      if (c.isAssignableFrom(java.lang.String.class)) {
        TestUtil.logTrace("Received expected type from getParameterType()");
        pass1 = true;
      } else {
        TestUtil.logErr(
            "Expected type String from getParameterType(), instead got:" + c);
      }
      String name = pe.getName();
      if (name != null) {
        if (!name.equals("first")) {
          TestUtil.logErr(
              "getName() returned wrong name, expected: first, actual:" + name);
        } else {
          pass2 = true;
        }
      } else {
        TestUtil.logErr("getName() returned null");
      }
      Integer position = pe.getPosition();
      if (position != null) {
        TestUtil
            .logErr("getPosition() returned:" + position + ", instead of null");
      } else {
        pass3 = true;
      }

      ParameterExpression<String> pe2 = qbuilder.parameter(String.class,
          "last");
      criteria.add(qbuilder.equal(employee.get("firstName"), pe));
      criteria.add(qbuilder.equal(employee.get("lastName"), pe2));

      cquery.where(qbuilder.or(criteria.toArray(new Predicate[0])));

      Query q = getEntityManager().createQuery(cquery);

      if (TestUtil.traceflag) {
        List<Object> list = new ArrayList<Object>(q.getParameters());
        for (int i = 0; i < list.size(); i++) {
          Parameter p = (Parameter) list.get(i);
          TestUtil.logTrace("parameter name = " + p.getName());
          TestUtil.logTrace("parameter position = " + p.getPosition());
          TestUtil.logTrace("parameter type =" + p.getParameterType());
        }
      }

      String sExpected = "first";
      Parameter p1 = q.getParameter(sExpected);
      String sActual = p1.getName();
      if (!sActual.equals(sExpected)) {
        TestUtil.logErr(
            "p1.getName() - Expected: " + sExpected + ", actual:" + sActual);
      } else {
        pass4 = true;
      }

      sExpected = null;
      Integer iActual = p1.getPosition();
      if (iActual != null) {
        TestUtil.logErr("p1.getPosition() - Expected: " + sExpected
            + ", actual:" + iActual);
      } else {
        pass5 = true;
      }
      sExpected = "java.lang.String";
      sActual = p1.getParameterType().getName();
      if (!sActual.equals(sExpected)) {
        TestUtil.logErr("p1.getParameterType() - Expected: " + sExpected
            + ", actual:" + sActual);
      } else {
        pass6 = true;
      }

      sExpected = "last";
      Parameter p2 = q.getParameter(sExpected);
      sActual = p2.getName();
      if (!sActual.equals(sExpected)) {
        TestUtil.logErr(
            "p2.getName() - Expected: " + sExpected + ", actual:" + sActual);
      } else {
        pass7 = true;
      }
      sExpected = null;
      iActual = p2.getPosition();
      if (iActual != null) {
        TestUtil.logErr("p2.getPosition() - Expected: " + sExpected
            + ", actual:" + iActual);
      } else {
        pass8 = true;
      }

      sExpected = "java.lang.String";
      sActual = p2.getParameterType().getName();
      if (!sActual.equals(sExpected)) {
        TestUtil.logErr("p2.getParameterType() - Expected: " + sExpected
            + ", actual:" + sActual);
      } else {
        pass9 = true;
      }

    } else {
      TestUtil.logErr("Failed to get Non-null Criteria Query");
    }

    if (!pass1 || !pass2 || !pass3 || !pass4 || !pass5 || !pass6 || !pass7
        || !pass8 || !pass9) {
      throw new Fault("parameterTest1 test failed");

    }

  }

  /*
   * @testName: parameterTest2
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:381; PERSISTENCE:JAVADOC:382;
   * PERSISTENCE:JAVADOC:383;
   * 
   * @test_Strategy: Create a query with a named parameter that is a float and
   * retrieve information about the parameter.
   */
  public void parameterTest2() throws Fault {
    TestUtil.logTrace("Starting parameterTest2");
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();

    CriteriaQuery<Employee> cquery = qbuilder.createQuery(Employee.class);
    if (cquery != null) {
      Root<Employee> employee = cquery.from(Employee.class);
      cquery.select(employee);

      List<Predicate> criteria = new ArrayList<Predicate>();
      ParameterExpression<Float> pe = qbuilder.parameter(Float.class, "salary");
      criteria.add(qbuilder.equal(employee.get("salary"), pe));

      cquery.where(qbuilder.or(criteria.toArray(new Predicate[0])));

      Query q = getEntityManager().createQuery(cquery);

      if (TestUtil.traceflag) {
        List<Object> list = new ArrayList<Object>(q.getParameters());
        for (int i = 0; i < list.size(); i++) {
          Parameter p = (Parameter) list.get(i);
          TestUtil.logTrace("parameter name = " + p.getName());
          TestUtil.logTrace("parameter position = " + p.getPosition());
          TestUtil.logTrace("parameter type =" + p.getParameterType());
        }
      }

      String sExpected = "salary";
      Parameter p1 = q.getParameter(sExpected);
      String sActual = p1.getName();
      if (!sActual.equals(sExpected)) {
        TestUtil.logErr(
            "p1.getName() - Expected: " + sExpected + ", actual:" + sActual);
      } else {
        pass1 = true;
      }

      sExpected = null;
      Integer iActual = p1.getPosition();
      if (iActual != null) {
        TestUtil.logErr("p1.getPosition() - Expected: " + sExpected
            + ", actual:" + iActual);
      } else {
        pass2 = true;
      }
      sExpected = "java.lang.Float";
      sActual = p1.getParameterType().getName();
      if (!sActual.equals(sExpected)) {
        TestUtil.logErr("p1.getParameterType() - Expected: " + sExpected
            + ", actual:" + sActual);
      } else {
        pass3 = true;
      }

    } else {
      TestUtil.logErr("Failed to get Non-null Criteria Query");
    }

    if (!pass1 || !pass2 || !pass3) {
      throw new Fault("parameterTest2 test failed");

    }
  }

  /*
   * @testName: parameterTest3
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:381; PERSISTENCE:JAVADOC:382;
   * PERSISTENCE:JAVADOC:383;
   * 
   * @test_Strategy: Create a query with a named parameter that is a date and
   * and retrieve information about the parameter.
   */
  public void parameterTest3() throws Fault {
    TestUtil.logTrace("Starting parameterTest3");
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();

    CriteriaQuery<Employee> cquery = qbuilder.createQuery(Employee.class);
    if (cquery != null) {
      Root<Employee> employee = cquery.from(Employee.class);
      cquery.select(employee);

      List<Predicate> criteria = new ArrayList<Predicate>();
      ParameterExpression<java.sql.Date> pe = qbuilder
          .parameter(java.sql.Date.class, "hdate");
      criteria.add(qbuilder.equal(employee.get("hireDate"), pe));

      cquery.where(qbuilder.or(criteria.toArray(new Predicate[0])));

      Query q = getEntityManager().createQuery(cquery);

      if (TestUtil.traceflag) {
        List<Object> list = new ArrayList<Object>(q.getParameters());
        for (int i = 0; i < list.size(); i++) {
          Parameter p = (Parameter) list.get(i);
          TestUtil.logTrace("parameter name = " + p.getName());
          TestUtil.logTrace("parameter position = " + p.getPosition());
          TestUtil.logTrace("parameter type =" + p.getParameterType());
        }
      }

      String sExpected = "hdate";
      Parameter p1 = q.getParameter(sExpected);
      String sActual = p1.getName();
      if (!sActual.equals(sExpected)) {
        TestUtil.logErr(
            "p1.getName() - Expected: " + sExpected + ", actual:" + sActual);
      } else {
        pass1 = true;
      }

      sExpected = null;
      Integer iActual = p1.getPosition();
      if (iActual != null) {
        TestUtil.logErr("p1.getPosition() - Expected: " + sExpected
            + ", actual:" + iActual);
      } else {
        pass2 = true;
      }
      sExpected = "java.sql.Date";
      sActual = p1.getParameterType().getName();
      if (!sActual.equals(sExpected)) {
        TestUtil.logErr("p1.getParameterType() - Expected: " + sExpected
            + ", actual:" + sActual);
      } else {
        pass3 = true;
      }

    } else {
      TestUtil.logErr("Failed to get Non-null Criteria Query");
    }

    if (!pass1 || !pass2 || !pass3) {
      throw new Fault("parameterTest3 test failed");

    }
  }

  /*
   * @testName: parameterTest4
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:382; PERSISTENCE:JAVADOC:383;
   * 
   * @test_Strategy: Create a query with a parameter where the name is not
   * specified and retrieve information about the parameter.
   */
  public void parameterTest4() throws Fault {
    TestUtil.logTrace("Starting parameterTest4");
    boolean pass1 = false;
    boolean pass2 = true;
    CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();

    CriteriaQuery<Employee> cquery = qbuilder.createQuery(Employee.class);
    if (cquery != null) {
      Root<Employee> employee = cquery.from(Employee.class);
      cquery.select(employee);

      List<Predicate> criteria = new ArrayList<Predicate>();

      // No name is being assigned to the parameter
      ParameterExpression<String> pe = qbuilder.parameter(String.class);

      criteria.add(qbuilder.equal(employee.get("firstName"), pe));

      cquery.where(qbuilder.or(criteria.toArray(new Predicate[0])));

      Query q = getEntityManager().createQuery(cquery);
      List<Object> list = new ArrayList<Object>(q.getParameters());
      if (TestUtil.traceflag) {
        for (int i = 0; i < list.size(); i++) {
          Parameter p = (Parameter) list.get(i);
          TestUtil.logTrace("parameter position = " + p.getPosition());
          TestUtil.logTrace("parameter type =" + p.getParameterType());
        }
      }

      for (int i = 0; i < list.size(); i++) {
        Parameter p = (Parameter) list.get(i);
        pass1 = true;
        // the value returned by getName() in this instance where no name has
        // been assigned to the parameter is undefined in the spec therefore
        // we will not test for it.

        String sExpected = null;
        Integer iActual = p.getPosition();
        if (iActual != null) {
          TestUtil.logErr("p1.getPosition() - Expected: " + sExpected
              + ", actual:" + iActual);
        }
        sExpected = "java.lang.String";
        String sActual = p.getParameterType().getName();
        if (!sActual.equals(sExpected)) {
          TestUtil.logErr("p1.getParameterType() - Expected: " + sExpected
              + ", actual:" + sActual);
        }
      }

    } else {
      TestUtil.logErr("Failed to get Non-null Criteria Query");
    }

    if (!pass1 || !pass2) {
      throw new Fault("parameterTest4 test failed");

    }

  }

  /*
   * @testName: parameterExpressionInObjectArrayTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1086;
   * 
   * @test_Strategy: SELECT e FROM EMPLOYEE e WHERE (1 IN (1, 2))
   */
  @SetupMethod(name = "setupEmployee")
  public void parameterExpressionInObjectArrayTest() throws Fault {
    boolean pass = false;
    try {
      List<Integer> expected = new ArrayList<Integer>();
      expected.add(empRef[0].getId());
      expected.add(empRef[1].getId());
      expected.add(empRef[2].getId());
      expected.add(empRef[3].getId());
      expected.add(empRef[4].getId());

      getEntityTransaction().begin();
      CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery<Employee> cquery = qbuilder.createQuery(Employee.class);
      if (cquery != null) {
        Root<Employee> employee = cquery.from(Employee.class);

        cquery.select(employee);

        List<Predicate> criteria = new ArrayList<Predicate>();
        ParameterExpression<Integer> pe = qbuilder.parameter(Integer.class,
            "num");

        Object[] o = { Integer.valueOf(1), Integer.valueOf(2) };

        criteria.add(pe.in(o));

        cquery.where(qbuilder.or(criteria.toArray(new Predicate[0])));

        Query q = getEntityManager().createQuery(cquery);

        q.setParameter("num", 1);
        List<Employee> result = q.getResultList();

        List<Integer> actual = new ArrayList<Integer>();
        for (Employee e : result) {
          actual.add(e.getId());
        }
        Collections.sort(actual);
        TestUtil.logTrace("actual" + actual);

        if (expected.equals(actual)) {
          TestUtil.logTrace("Successfully returned expected results");
          pass = true;
        } else {
          TestUtil.logErr("expected: " + expected + ", actual: " + actual);
        }
      } else {
        TestUtil.logErr("Failed to get Non-null Criteria Query");
      }
      getEntityTransaction().commit();
    } finally {
      removeTestData();
    }

    if (!pass) {
      throw new Fault("parameterExpressionInObjectArrayTest test failed");

    }
  }

  /*
   * @testName: parameterExpressionInExpressionArrayTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1087;
   * 
   * @test_Strategy: SELECT e FROM EMPLOYEE e WHERE (1 IN (1, 2))
   */
  @SetupMethod(name = "setupEmployee")
  public void parameterExpressionInExpressionArrayTest() throws Fault {
    boolean pass = false;
    try {
      List<Integer> expected = new ArrayList<Integer>();
      expected.add(empRef[0].getId());
      expected.add(empRef[1].getId());
      expected.add(empRef[2].getId());
      expected.add(empRef[3].getId());
      expected.add(empRef[4].getId());

      getEntityTransaction().begin();
      CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery<Employee> cquery = qbuilder.createQuery(Employee.class);
      if (cquery != null) {
        Root<Employee> employee = cquery.from(Employee.class);

        cquery.select(employee);

        List<Predicate> criteria = new ArrayList<Predicate>();
        ParameterExpression<String> pe = qbuilder.parameter(String.class,
            "sid");

        Expression[] exp = new Expression[] { qbuilder.literal("1"),
            qbuilder.literal("2") };

        criteria.add(pe.in(exp));

        cquery.where(qbuilder.or(criteria.toArray(new Predicate[0])));

        Query q = getEntityManager().createQuery(cquery);

        q.setParameter("sid", "1");
        List<Employee> result = q.getResultList();

        List<Integer> actual = new ArrayList<Integer>();
        for (Employee e : result) {
          actual.add(e.getId());
        }
        Collections.sort(actual);
        TestUtil.logTrace("actual" + actual);

        if (expected.equals(actual)) {
          TestUtil.logTrace("Successfully returned expected results");
          pass = true;
        } else {
          TestUtil.logErr("expected: " + expected + ", actual: " + actual);
        }
      } else {
        TestUtil.logErr("Failed to get Non-null Criteria Query");
      }
      getEntityTransaction().commit();
    } finally {
      removeTestData();
    }

    if (!pass) {
      throw new Fault("parameterExpressionInExpressionArrayTest test failed");

    }
  }

  /*
   * @testName: parameterExpressionInCollectionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1088;
   * 
   * @test_Strategy: SELECT e FROM EMPLOYEE e WHERE (1 IN (1, 2))
   */
  @SetupMethod(name = "setupEmployee")
  public void parameterExpressionInCollectionTest() throws Fault {
    boolean pass = false;
    try {
      List<Integer> expected = new ArrayList<Integer>();
      expected.add(empRef[0].getId());
      expected.add(empRef[1].getId());
      expected.add(empRef[2].getId());
      expected.add(empRef[3].getId());
      expected.add(empRef[4].getId());

      getEntityTransaction().begin();
      CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery<Employee> cquery = qbuilder.createQuery(Employee.class);
      if (cquery != null) {
        Root<Employee> employee = cquery.from(Employee.class);

        cquery.select(employee);

        List<Predicate> criteria = new ArrayList<Predicate>();
        ParameterExpression<String> pe = qbuilder.parameter(String.class,
            "sid");

        Collection col = new ArrayList();
        col.add("1");
        col.add("2");

        criteria.add(pe.in(col));

        cquery.where(qbuilder.or(criteria.toArray(new Predicate[0])));

        Query q = getEntityManager().createQuery(cquery);

        q.setParameter("sid", "1");
        List<Employee> result = q.getResultList();

        List<Integer> actual = new ArrayList<Integer>();
        for (Employee e : result) {
          actual.add(e.getId());
        }
        Collections.sort(actual);
        TestUtil.logTrace("actual" + actual);

        if (expected.equals(actual)) {
          TestUtil.logTrace("Successfully returned expected results");
          pass = true;
        } else {
          TestUtil.logErr("expected: " + expected + ", actual: " + actual);
        }
      } else {
        TestUtil.logErr("Failed to get Non-null Criteria Query");
      }
      getEntityTransaction().commit();
    } finally {
      removeTestData();
    }

    if (!pass) {
      throw new Fault("parameterExpressionInCollectionTest test failed");

    }
  }

  /*
   * @testName: parameterExpressionInExpressionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1089;
   * 
   * @test_Strategy: SELECT e FROM EMPLOYEE e WHERE (1 = 1)
   */
  @SetupMethod(name = "setupEmployee")
  public void parameterExpressionInExpressionTest() throws Fault {
    boolean pass = false;
    try {
      List<Integer> expected = new ArrayList<Integer>();
      expected.add(empRef[0].getId());
      expected.add(empRef[1].getId());
      expected.add(empRef[2].getId());
      expected.add(empRef[3].getId());
      expected.add(empRef[4].getId());

      getEntityTransaction().begin();
      CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery<Employee> cquery = qbuilder.createQuery(Employee.class);
      if (cquery != null) {
        Root<Employee> employee = cquery.from(Employee.class);

        cquery.select(employee);

        List<Predicate> criteria = new ArrayList<Predicate>();
        ParameterExpression<String> pe = qbuilder.parameter(String.class,
            "sid");

        Expression exp = qbuilder.literal("1");

        criteria.add(pe.in(exp));

        cquery.where(qbuilder.or(criteria.toArray(new Predicate[0])));

        Query q = getEntityManager().createQuery(cquery);

        q.setParameter("sid", "1");
        List<Employee> result = q.getResultList();

        List<Integer> actual = new ArrayList<Integer>();
        for (Employee e : result) {
          actual.add(e.getId());
        }
        Collections.sort(actual);
        TestUtil.logTrace("actual" + actual);

        if (expected.equals(actual)) {
          TestUtil.logTrace("Successfully returned expected results");
          pass = true;
        } else {
          TestUtil.logErr("expected: " + expected + ", actual: " + actual);
        }
      } else {
        TestUtil.logErr("Failed to get Non-null Criteria Query");
      }
      getEntityTransaction().commit();
    } finally {
      removeTestData();
    }

    if (!pass) {
      throw new Fault("parameterExpressionInExpressionTest test failed");

    }
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("calling super.cleanup");
    removeTestData();
    super.cleanup();
  }

  private void createTestData() {

    try {
      getEntityTransaction().begin();

      TestUtil.logMsg("Creating Employees");

      final java.sql.Date d2 = getSQLDate("2001-06-27");
      final java.sql.Date d3 = getSQLDate("2002-07-07");
      final java.sql.Date d4 = getSQLDate("2003-03-03");
      final java.sql.Date d5 = getSQLDate();

      empRef[0] = new Employee(1, "Alan", "Frechette", d1, (float) 35000.0);
      empRef[1] = new Employee(2, "Arthur", "Frechette", d2, (float) 35000.0);
      empRef[2] = new Employee(3, "Shelly", "McGowan", d3, (float) 50000.0);
      empRef[3] = new Employee(4, "Robert", "Bissett", d4, (float) 55000.0);
      empRef[4] = new Employee(5, "Stephen", "DMilla", d5, (float) 25000.0);
      for (Employee e : empRef) {
        if (e != null) {
          getEntityManager().persist(e);
          TestUtil.logTrace("persisted employee:" + e);
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
