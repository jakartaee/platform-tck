/*
 * Copyright (c) 2011, 2023 Oracle and/or its affiliates. All rights reserved.
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import jakarta.persistence.Parameter;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@ExtendWith(ArquillianExtension.class)
@TestInstance(Lifecycle.PER_CLASS)

public class Client2IT extends PMClientBase {

  Employee[] empRef = new Employee[5];

  final java.sql.Date d1 = getSQLDate("2000-02-14");
  
  @Deployment(testable = false, managed = false)
 	public static JavaArchive createDeployment() throws Exception {

 		String pkgNameWithoutSuffix = Client2IT.class.getPackageName();
 		String pkgName = Client2IT.class.getPackageName() + ".";
 		String[] classes = { pkgName + "Employee"};
 		return createDeploymentJar("jpa_core_criteriaapi_parameter2.jar", pkgNameWithoutSuffix, classes);
 }


@BeforeAll
  public void setupEmployee() throws Exception {
    TestUtil.logTrace("setup");
    try {
      super.setup();
      removeTestData();
      createTestData();
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Exception("Setup failed:", e);
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
  @Test
  public void parameterExpressionInObjectArrayTest() throws Exception {
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
      throw new Exception("parameterExpressionInObjectArrayTest test failed");

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
  @Test
  public void parameterExpressionInExpressionArrayTest() throws Exception {
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
      throw new Exception("parameterExpressionInExpressionArrayTest test failed");

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
  @Test
  public void parameterExpressionInCollectionTest() throws Exception {
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
      throw new Exception("parameterExpressionInCollectionTest test failed");

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
  @Test
  public void parameterExpressionInExpressionTest() throws Exception {
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
      throw new Exception("parameterExpressionInExpressionTest test failed");

    }
  }

  @AfterAll
  public void cleanup() throws Exception {
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
