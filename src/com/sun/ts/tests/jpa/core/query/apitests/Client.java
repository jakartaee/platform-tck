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

package com.sun.ts.tests.jpa.core.query.apitests;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.CleanupMethod;
import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DecimalFormat;

import java.util.*;

public class Client extends PMClientBase {

  private final Employee empRef[] = new Employee[21];

  private final Date d1 = getSQLDate("2000-02-14");

  private final java.util.Date dateId = getUtilDate("2009-01-10");

  final Department deptRef[] = new Department[5];

  private static final DecimalFormat df = new DecimalFormat();

  public Client() {
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setupNoData(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    try {
      super.setup(args, p);
      TestUtil.logTrace("Done creating test data");
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception caught in Setup: ", e);
      throw new Fault("Setup failed:", e);

    }
  }

  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    try {
      super.setup(args, p);
      removeTestData();
      createTestData();
      TestUtil.logTrace("Done creating test data");
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception caught in Setup: ", e);
      throw new Fault("Setup failed:", e);

    }
  }

  public void setupDataTypes2(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    try {
      super.setup(args, p);
      removeTestData();
      createDataTypes2Data();
      TestUtil.logTrace("Done creating test data");
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception caught in Setup: ", e);
      throw new Fault("Setup failed:", e);

    }
  }

  public void cleanupNoData() throws Fault {
    TestUtil.logTrace("in cleanupNoData");
    super.cleanup();
  }

  /*
   * BEGIN Test Cases
   */
  /*
   * @testName: setFirstResultTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:728; PERSISTENCE:SPEC:400;
   * PERSISTENCE:JAVADOC:636; PERSISTENCE:JAVADOC:172; PERSISTENCE:JAVADOC:399;
   * PERSISTENCE:JAVADOC:440; PERSISTENCE:JAVADOC:665; PERSISTENCE:JAVADOC:682;
   * 
   * @test_Strategy: Verify results of setFirstResult using JOIN in the FROM
   * clause projecting on state_field in the select clause. Verify that number
   * of rows skipped are 1-1 with specified value for setFirstResult.
   *
   * The elements of a query result whose SELECT clause consists of more than
   * one value are of type Object[].
   *
   * Create a TypedQuery where id <= 10 sorted by id. setFirstResult(5) and
   * verify the results that were returned
   */
  public void setFirstResultTest() throws Fault {
    List q;
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    boolean pass4 = true;
    boolean pass11 = false;
    boolean pass12 = false;
    boolean pass13 = false;
    boolean pass14 = true;
    final Object[][] expectedResultSet = new Object[][] { new Object[] { 4, 4 },
        new Object[] { 4, 9 }, new Object[] { 4, 14 }, new Object[] { 4, 19 },
        new Object[] { 5, 5 }, new Object[] { 5, 10 }, new Object[] { 5, 15 },
        new Object[] { 5, 20 } };

    try {
      getEntityTransaction().begin();
      TestUtil.logMsg("Testing Query version");

      Query query = getEntityManager().createQuery(
          "select d.id, e.id from Department d join d.employees e where d.id <= 5 "
              + " order by d.id, e.id");
      int gfr = query.getFirstResult();
      if (gfr != 0) {
        TestUtil.logErr("getFirstResult() - Expecting result=0, actual=" + gfr);
      } else {
        pass1 = true;
      }
      query.setFirstResult(13);
      gfr = query.getFirstResult();
      if (gfr != 13) {
        TestUtil
            .logErr("getFirstResult() - Expecting result=11, actual=" + gfr);
      } else {
        pass2 = true;
      }

      q = query.getResultList();

      if (TestUtil.traceflag) {
        TestUtil.logTrace("query returned " + q.size() + " results.");
        int i = 0;
        for (Object obj : q) {
          TestUtil.logTrace((i++) + "=" + Arrays.asList((Object[]) obj));
        }

      }
      if (q.size() == 8) {
        TestUtil.logTrace("Expected size received, verify contents . . . ");
        // each element of the list q should be a size-2 array,
        // for instance [4,5]
        int i = 0;
        pass3 = true;

        for (Object obj : q) {
          Object[] expected = expectedResultSet[i++];
          Object[] departmentIdEmpId = null;
          if (obj instanceof Object[]) {
            TestUtil.logTrace(
                "The element in the result list is of type Object[], continue . . .");
            // good, this element of type Object[]
            departmentIdEmpId = (Object[]) obj;
            if (!Arrays.equals(expected, departmentIdEmpId)) {
              TestUtil.logErr("Expecting element value: "
                  + Arrays.asList(expected) + ", actual element value: "
                  + Arrays.asList(departmentIdEmpId));
              pass4 = false;
              break;
            }
          } else {
            TestUtil.logErr(
                "The element in the result list is not of type Object[]:"
                    + obj);
            pass4 = false;
            break;
          }
        }
      } else {
        TestUtil.logErr("Did not get expected results.  Expected: 10, "
            + "got: " + q.size());
        TestUtil.logErr("Expected results:");
        int i = 0;
        for (Object obj[] : expectedResultSet) {
          TestUtil.logErr((i++) + "=" + Arrays.toString(obj));
        }

        TestUtil.logErr("Actual results:");
        i = 0;
        for (Object obj : q) {
          TestUtil.logErr((i++) + "=" + Arrays.asList((Object[]) obj));
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
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception in while rolling back TX:", re);
      }
    }

    TestUtil.logMsg("Testing TypedQuery version");

    try {
      getEntityTransaction().begin();

      TypedQuery<Integer> query = getEntityManager().createQuery(
          "select e.id from Employee e where e.id <= 10 order by e.id",
          Integer.class);
      int gfr = query.getFirstResult();
      if (gfr != 0) {
        TestUtil.logErr("getFirstResult() - Expecting result=0, actual=" + gfr);
      } else {
        pass11 = true;
      }
      query.setFirstResult(5);
      gfr = query.getFirstResult();
      if (gfr != 5) {
        TestUtil.logErr("getFirstResult() - Expecting result=5, actual=" + gfr);
      } else {
        pass12 = true;
      }

      Collection<Integer> actual = query.getResultList();
      String[] expected = new String[5];
      expected[0] = "6";
      expected[1] = "7";
      expected[2] = "8";
      expected[3] = "9";
      expected[4] = "10";

      if (!checkEntityPK(actual, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.length + " references, got: " + actual.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass13 = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      pass14 = false;
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

    if (!pass1 || !pass2 || !pass3 || !pass4 || !pass11 || !pass12 || !pass13
        || !pass14) {
      throw new Fault("setFirstResultTest failed");
    }

  }

  /*
   * @testName: setFirstResultIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:585; PERSISTENCE:JAVADOC:618
   * 
   * @test_Strategy: Create a Query. setFirstResult(-5) and verify
   * IllegalArgumentException is thrown
   *
   * Create a TypedQuery. setFirstResult(-5) and verify IllegalArgumentException
   * is thrown
   */
  public void setFirstResultIllegalArgumentExceptionTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;

    try {
      TestUtil.logMsg("Testing Query version");

      Query query = getEntityManager().createQuery(
          "select e.id from Employee e where e.id <= 10 order by e.id");
      query.setFirstResult(-5);
      TestUtil.logErr("IllegalArgumentException was not thrown");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
      pass1 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    TestUtil.logMsg("Testing TypedQuery version");

    try {

      TypedQuery<Integer> query = getEntityManager().createQuery(
          "select e.id from Employee e where e.id <= 10 order by e.id",
          Integer.class);
      query.setFirstResult(-5);
      TestUtil.logErr("IllegalArgumentException was not thrown");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
      pass2 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass1 || !pass2) {
      throw new Fault("setFirstResultIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: getParameterTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:405; PERSISTENCE:JAVADOC:645
   * 
   * @test_Strategy: call query.getParameter(String, Class) and verify returned
   * Parameter or that IllegalStateException is thrown. call
   * TypedQuery.getParameter(String, Class) and verify returned Parameter or
   * that IllegalStateException is thrown.
   */
  public void getParameterTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    try {
      TestUtil.logMsg("Testing Query version");

      Query q = getEntityManager()
          .createQuery("select e from Employee e where e.firstName = :fName")
          .setParameter("fName", "Stephen");

      Parameter p = q.getParameter("fName", String.class);
      String s = p.getName();
      if (!s.equals("fName")) {
        TestUtil.logErr("getName() - Expected:fName, actual:" + s);
      } else {
        pass1 = true;
      }
    } catch (IllegalStateException ise) {
      pass1 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    TestUtil.logMsg("Testing TypedQuery version");

    try {
      TypedQuery<Employee> q = getEntityManager()
          .createQuery("select e from Employee e where e.firstName = :fName",
              Employee.class)
          .setParameter("fName", "Stephen");

      Parameter p = q.getParameter("fName", String.class);
      String s = p.getName();
      if (!s.equals("fName")) {

        TestUtil.logErr("getName() - Expected:fName, actual:" + s);
      } else {
        pass2 = true;
      }
    } catch (IllegalStateException ise) {
      pass2 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    }

    if (!pass1 || !pass2) {
      throw new Fault("getParameterTest failed");
    }
  }

  /*
   * @testName: getParameterIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:558; PERSISTENCE:JAVADOC:646
   * 
   * @test_Strategy: call Query.getParameter(String, String) with a name that
   * does not exist and verify that IllegalArgumentException is thrown.
   *
   * call TypedQuery.getParameter(String, String) with a name that does not
   * match and verify that IllegalArgumentException is thrown.
   */
  public void getParameterIllegalArgumentExceptionTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    try {
      TestUtil.logMsg("Testing Query version");
      Query q = getEntityManager()
          .createQuery("select e from Employee e where e.firstName = :fName");

      q.getParameter("doesnotexist", String.class);
      TestUtil.logErr("IllegalArgumentException was not thrown");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
      pass1 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    TestUtil.logMsg("Testing TypedQuery version");

    try {
      TypedQuery<Employee> q = getEntityManager().createQuery(
          "select e from Employee e where e.firstName = :fName",
          Employee.class);

      q.getParameter("doesnotexist", String.class);
      TestUtil.logErr("IllegalArgumentException was not thrown");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
      pass2 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass1 || !pass2) {
      throw new Fault("getParameterIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: getParameterIllegalArgumentException2Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:556; PERSISTENCE:JAVADOC:643
   * 
   * @test_Strategy: call Query.getParameter(String) with name that does not
   * exist and verify that IllegalArgumentException is thrown. call
   * TypedQuery.getParameter(String) with name that does not exist and verify
   * that IllegalArgumentException is thrown.
   */
  public void getParameterIllegalArgumentException2Test() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    try {
      TestUtil.logMsg("Testing Query version");
      Query q = getEntityManager()
          .createQuery("select e from Employee e where e.firstName = :fName");

      q.getParameter("doesnotexist");
      TestUtil.logErr("IllegalArgumentException was not thrown");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
      pass1 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    TestUtil.logMsg("Testing TypedQuery version");

    try {
      TypedQuery<Employee> q = getEntityManager().createQuery(
          "select e from Employee e where e.firstName = :fName",
          Employee.class);

      q.getParameter("doesnotexist");
      TestUtil.logErr("IllegalArgumentException was not thrown");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
      pass2 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    if (!pass1 || !pass2) {
      throw new Fault("getParameterIllegalArgumentException2Test failed");
    }
  }

  /*
   * @testName: getParameterIntClassTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:407; PERSISTENCE:JAVADOC:648
   * 
   * @test_Strategy: call Query.getParameter(int, Class) and verify returned
   * Parameter or that IllegalStateException is thrown. Also a null should be
   * returned by getName() for a positional parameter call
   * TypedQuery.getParameter(int, Class) and verify returned Parameter or that
   * IllegalStateException is thrown. Also a null should be returned by
   * getName() for a positional parameter
   *
   */
  public void getParameterIntClassTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    try {
      TestUtil.logMsg("Testing Query version");
      Query q = getEntityManager()
          .createQuery("select e from Employee e where e.firstName = ?1")
          .setParameter(1, "Stephen");

      Parameter p = q.getParameter(1, String.class);
      String s = p.getName();
      if (s != null) {
        TestUtil.logErr("getName() - Expected:null, actual:" + s);
      } else {
        pass1 = true;
      }

    } catch (IllegalStateException ise) {
      // implementation does not support this use
      pass1 = true;
      TestUtil.logTrace("Received expected IllegalArgumentException");
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    TestUtil.logMsg("Testing TypedQuery version");

    try {
      TypedQuery<Employee> q = getEntityManager()
          .createQuery("select e from Employee e where e.firstName = ?1",
              Employee.class)
          .setParameter(1, "Stephen");

      Parameter p = q.getParameter(1, String.class);
      String s = p.getName();
      if (s != null) {
        TestUtil.logErr("getName() - Expected:null, actual:" + s);
      } else {
        pass2 = true;
      }

    } catch (IllegalStateException ise) {
      // implementation does not support this use
      pass2 = true;
      TestUtil.logTrace("Received expected IllegalArgumentException");
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    if (!pass1 || !pass2) {
      throw new Fault("getParameterIntClassTest failed");
    }
  }

  /*
   * @testName: getParameterIntIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:560; PERSISTENCE:JAVADOC:649
   * 
   * @test_Strategy: create query and set a positional parameter. Verify
   * getParameter for a position that does not exist throws
   * IllegalArgumentException create TypedQuery and set a positional parameter.
   * Verify getParameter for a position that does not exist throws
   * IllegalArgumentException*
   */
  public void getParameterIntIllegalArgumentExceptionTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    try {
      TestUtil.logTrace(
          "Creating query for getParameterIntIllegalArgumentExceptionTest");
      TestUtil.logMsg("Testing Query version");
      Query query = getEntityManager()
          .createQuery("select e from Employee e where e.firstName = ?1")
          .setParameter(1, "foo");

      query.getParameter(99);
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
      pass1 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    TestUtil.logMsg("Testing TypedQuery version");

    try {
      TypedQuery<Employee> query = getEntityManager()
          .createQuery("select e from Employee e where e.firstName = ?1",
              Employee.class)
          .setParameter(1, "foo");
      query.getParameter(99);
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
      pass2 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    if (!pass1 || !pass2) {
      throw new Fault("getParameterIntIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: getParameterIntIllegalArgumentException2Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:562; PERSISTENCE:JAVADOC:652
   * 
   * @test_Strategy: create query and set a positional parameter. Verify calling
   * getParameter with a class that is not assignable to the type throws
   * IllegalArgumentException create TypedQuery and set a positional parameter.
   * Verify calling getParameter with a class that is not assignable to the type
   * throws IllegalArgumentException
   */
  @SetupMethod(name = "setupNoData")
  @CleanupMethod(name = "cleanupNoData")
  public void getParameterIntIllegalArgumentException2Test() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    try {
      TestUtil.logMsg("Testing Query version");
      Query query = getEntityManager()
          .createQuery("select e from Employee e where e.firstName = ?1")
          .setParameter(1, "Tom");
      query.getParameter(1, java.util.List.class);
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
      pass1 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    TestUtil.logMsg("Testing TypedQuery version");

    try {
      TypedQuery<Employee> query = getEntityManager()
          .createQuery("select e from Employee e where e.firstName = ?1",
              Employee.class)
          .setParameter(1, "Tom");
      query.getParameter(1, java.util.List.class);
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
      pass2 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    if (!pass1 || !pass2) {
      throw new Fault("getParameterIntIllegalArgumentException2Test failed");
    }
  }

  /*
   * @testName: getParameterValueParameterTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:404; PERSISTENCE:JAVADOC:409;
   * PERSISTENCE:JAVADOC:645; PERSISTENCE:JAVADOC:656; PERSISTENCE:SPEC:1540;
   * 
   * @test_Strategy: create query and set a String parameter. Verify
   * getParameterValue can retrieve that value
   *
   * create TypedQuery and set a String parameter. Verify getParameterValue can
   * retrieve that value
   */
  public void getParameterValueParameterTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    String expectedName = "fName";
    String expectedValue = "Stephen";

    try {
      TestUtil.logMsg("Testing Query version");
      Query query = getEntityManager()
          .createQuery("select e from Employee e where e.firstName = :fName")
          .setParameter(expectedName, expectedValue);

      Parameter p = query.getParameter(expectedName);
      if (p != null) {
        String s = (String) query.getParameterValue(p);
        if (!s.equals(expectedValue)) {
          TestUtil.logErr("Expected:" + expectedValue + ",Actual=" + s);
        } else {
          pass1 = true;
        }
      } else {
        TestUtil.logErr("getParameter returned null");
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    TestUtil.logMsg("Testing TypedQuery version");

    try {
      TypedQuery<Employee> query = getEntityManager()
          .createQuery("select e from Employee e where e.firstName = :fName",
              Employee.class)
          .setParameter(expectedName, expectedValue);

      Parameter p = query.getParameter(expectedName);
      if (p != null) {
        String s = (String) query.getParameterValue(p);

        if (!s.equals(expectedValue)) {
          TestUtil.logErr("Expected:" + expectedValue + ",Actual=" + s);
        } else {
          pass2 = true;
        }
      } else {
        TestUtil.logErr("getParameter returned null");
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass1 || !pass2) {
      throw new Fault("getParameterValueParameterTest failed");
    }
  }

  /*
   * @testName: getParameterValueParameterIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:565; PERSISTENCE:JAVADOC:657
   * 
   * @test_Strategy: create two querys and set a String parameter. Try to get
   * the first parameter value from the second query and verify
   * IllegalArgumentException is thrown create two TypedQuerys and set a String
   * parameter. Try to get the first parameter value from the second query and
   * verify IllegalArgumentException is thrown*
   */
  public void getParameterValueParameterIllegalArgumentExceptionTest()
      throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;

    try {
      TestUtil.logMsg("Testing Query version");
      Query query1 = getEntityManager()
          .createQuery("select e from Employee e where e.firstName = :fname1")
          .setParameter("fname1", "fnameValue1");
      Query query2 = getEntityManager()
          .createQuery("select e from Employee e where e.firstName = :fname2")
          .setParameter("fname2", "fnameValue2");

      Parameter p = query2.getParameter("fname2");
      query1.getParameterValue(p);
      TestUtil.logErr("IllegalArgumentException was not thrown");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
      pass1 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    TestUtil.logMsg("Testing TypedQuery version");

    try {
      TypedQuery<Employee> query1 = getEntityManager()
          .createQuery("select e from Employee e where e.firstName = :fname1",
              Employee.class)
          .setParameter("fname1", "fnameValue1");
      TypedQuery<Employee> query2 = getEntityManager()
          .createQuery("select e from Employee e where e.firstName = :fname2",
              Employee.class)
          .setParameter("fname2", "fnameValue2");

      Parameter p = query2.getParameter("fname2");
      query1.getParameterValue(p);
      TestUtil.logErr("IllegalArgumentException was not thrown");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
      pass2 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    if (!pass1 || !pass2) {
      throw new Fault(
          "getParameterValueParameterIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: getParameterValueParameterIllegalStateExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:566; PERSISTENCE:JAVADOC:658
   * 
   * @test_Strategy: create query with a parameter that is not set. call
   * getParameterValue(Parameter) for that parameter and verify
   * IllegalArgumentException is thrown create TypedQuery with a parameter that
   * is not set. call getParameterValue(Parameter) for that parameter and verify
   * IllegalArgumentException is thrown
   */
  public void getParameterValueParameterIllegalStateExceptionTest()
      throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    boolean pass4 = false;

    TestUtil.logMsg("Testing Query version");
    Query query1 = getEntityManager()
        .createQuery("select e from Employee e where e.firstName = :fName1");

    Set<Parameter<?>> set = query1.getParameters();
    if (set.size() != 1) {
      TestUtil.logErr("Expected one parameter, actual=" + set.size());
      for (Parameter p : set) {
        TestUtil.logErr("Parameter:" + p.toString());
      }
    } else {
      pass1 = true;
    }
    try {
      for (Parameter p : set) {
        query1.getParameterValue(p);
      }
      TestUtil.logErr("IllegalStateException was not thrown");
    } catch (IllegalStateException iae) {
      TestUtil.logTrace("Received expected IllegalStateException");
      pass2 = true;

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    TestUtil.logMsg("Testing TypedQuery version");

    TypedQuery<Employee> tquery1 = getEntityManager().createQuery(
        "select e from Employee e where e.firstName = :fName1", Employee.class);
    set = tquery1.getParameters();
    if (set.size() != 1) {
      TestUtil.logErr("Expected one parameter, actual=" + set.size());
      for (Parameter p : set) {
        TestUtil.logErr("Parameter:" + p.toString());
      }
    } else {
      pass3 = true;
    }
    try {
      for (Parameter p : set) {
        tquery1.getParameterValue(p);
      }
      TestUtil.logErr("IllegalStateException was not thrown");
    } catch (IllegalStateException iae) {
      TestUtil.logTrace("Received expected IllegalStateException");
      pass4 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    if (!pass1 || !pass2 || !pass3 || !pass4) {
      throw new Fault(
          "getParameterValueParameterIllegalStateExceptionTest failed");
    }
  }

  /*
   * @testName: setParameterParameterObjectIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:589; PERSISTENCE:JAVADOC:622;
   * PERSISTENCE:JAVADOC:692;
   * 
   * @test_Strategy: create query with a parameter that is not set. call
   * setParameter(Parameter,Object) for that parameter and verify
   * IllegalArgumentException is thrown create TypedQuery with a parameter that
   * is not set. call setParameter(Parameter,Object) for that parameter and
   * verify IllegalArgumentException is thrown
   */
  public void setParameterParameterObjectIllegalArgumentExceptionTest()
      throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    boolean pass4 = false;

    TestUtil.logMsg("Testing Query version");
    Query query1 = getEntityManager()
        .createQuery("select e from Employee e where e.firstName = :fName1");
    Query query2 = getEntityManager()
        .createQuery("select e from Employee e where e.firstName = :fName2");

    Set<Parameter<?>> set = query2.getParameters();
    if (set.size() != 1) {
      TestUtil.logErr("Expected one parameter, actual=" + set.size());
      for (Parameter p : set) {
        TestUtil.logErr("Parameter:" + p.toString());
      }
    } else {
      pass1 = true;
    }
    try {
      for (Parameter p : set) {
        query1.setParameter(p, "object1");
      }
      TestUtil.logErr("IllegalArgumentException was not thrown");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
      pass2 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    TestUtil.logMsg("Testing TypedQuery version");

    TypedQuery<Employee> tquery1 = getEntityManager().createQuery(
        "select e from Employee e where e.firstName = :fName1", Employee.class);

    TypedQuery<Employee> tquery2 = getEntityManager().createQuery(
        "select e from Employee e where e.firstName = :fName2", Employee.class);
    set = tquery2.getParameters();
    if (set.size() != 1) {
      TestUtil.logErr("Expected one parameter, actual=" + set.size());
      for (Parameter p : set) {
        TestUtil.logErr("Parameter:" + p.toString());
      }
    } else {
      pass3 = true;
    }
    try {
      for (Parameter p : set) {
        tquery1.setParameter(p, "object1");
      }
      TestUtil.logErr("IllegalArgumentException was not thrown");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
      pass4 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    if (!pass1 || !pass2 || !pass3 || !pass4) {
      throw new Fault(
          "setParameterParameterObjectIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName:
   * setParameterParameterCalendarTemporalTypeIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:590; PERSISTENCE:JAVADOC:623;
   * PERSISTENCE:JAVADOC:694;
   * 
   * @test_Strategy: create query with a parameter that is not set. call
   * setParameter(Parameter,Calendar,TemporalType) for that parameter and verify
   * IllegalArgumentException is thrown create TypedQuery with a parameter that
   * is not set. call setParameter(Parameter,Calendar,TemporalType) for that
   * parameter and verify IllegalArgumentException is thrown
   */
  public void setParameterParameterCalendarTemporalTypeIllegalArgumentExceptionTest()
      throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    boolean pass4 = false;

    TestUtil.logMsg("Testing Query version");
    Query query1 = getEntityManager()
        .createQuery("select e from Employee e where e.hireDate = :date1");
    Query query2 = getEntityManager()
        .createQuery("select e from Employee e where e.hireDate = :date2");

    Set<Parameter<?>> set = query2.getParameters();
    if (set.size() != 1) {
      TestUtil.logErr("Expected one parameter, actual=" + set.size());
      for (Parameter p : set) {
        TestUtil.logErr("Parameter:" + p.toString());
      }
    } else {
      pass1 = true;
    }
    try {
      for (Parameter p : set) {
        query1.setParameter(p, getCalDate(), TemporalType.DATE);
      }
      TestUtil.logErr("IllegalArgumentException was not thrown");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
      pass2 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    TestUtil.logMsg("Testing TypedQuery version");

    TypedQuery<Employee> tquery1 = getEntityManager().createQuery(
        "select e from Employee e where e.hireDate = :date1", Employee.class);

    TypedQuery<Employee> tquery2 = getEntityManager().createQuery(
        "select e from Employee e where e.hireDate = :date2", Employee.class);
    set = tquery2.getParameters();
    if (set.size() != 1) {
      TestUtil.logErr("Expected one parameter, actual=" + set.size());
      for (Parameter p : set) {
        TestUtil.logErr("Parameter:" + p.toString());
      }
    } else {
      pass3 = true;
    }
    try {
      for (Parameter p : set) {
        tquery1.setParameter(p, getCalDate(), TemporalType.DATE);
      }
      TestUtil.logErr("IllegalArgumentException was not thrown");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
      pass4 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    if (!pass1 || !pass2 || !pass3 || !pass4) {
      throw new Fault(
          "setParameterParameterCalendarTemporalTypeIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName:
   * setParameterParameterDateTemporalTypeIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:591; PERSISTENCE:JAVADOC:624;
   * PERSISTENCE:JAVADOC:696;
   * 
   * @test_Strategy: create query with a parameter that is not set. call
   * setParameter(Parameter,Date,TemporalType) for that parameter and verify
   * IllegalArgumentException is thrown create TypedQuery with a parameter that
   * is not set. call setParameter(Parameter,Date,TemporalType) for that
   * parameter and verify IllegalArgumentException is thrown
   */
  public void setParameterParameterDateTemporalTypeIllegalArgumentExceptionTest()
      throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    boolean pass4 = false;

    TestUtil.logMsg("Testing Query version");
    Query query1 = getEntityManager()
        .createQuery("select e from Employee e where e.hireDate = :date1");
    Query query2 = getEntityManager()
        .createQuery("select e from Employee e where e.hireDate = :date2");

    Set<Parameter<?>> set = query2.getParameters();
    if (set.size() != 1) {
      TestUtil.logErr("Expected one parameter, actual=" + set.size());
      for (Parameter p : set) {
        TestUtil.logErr("Parameter:" + p.toString());
      }
    } else {
      pass1 = true;
    }
    try {
      for (Parameter p : set) {
        query1.setParameter(p, dateId, TemporalType.DATE);
      }
      TestUtil.logErr("IllegalArgumentException was not thrown");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
      pass2 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    TestUtil.logMsg("Testing TypedQuery version");

    TypedQuery<Employee> tquery1 = getEntityManager().createQuery(
        "select e from Employee e where e.hireDate = :date1", Employee.class);

    TypedQuery<Employee> tquery2 = getEntityManager().createQuery(
        "select e from Employee e where e.hireDate = :date2", Employee.class);
    set = tquery2.getParameters();
    if (set.size() != 1) {
      TestUtil.logErr("Expected one parameter, actual=" + set.size());
      for (Parameter p : set) {
        TestUtil.logErr("Parameter:" + p.toString());
      }
    } else {
      pass3 = true;
    }
    try {
      for (Parameter p : set) {
        tquery1.setParameter(p, dateId, TemporalType.DATE);
      }
      TestUtil.logErr("IllegalArgumentException was not thrown");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
      pass4 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    if (!pass1 || !pass2 || !pass3 || !pass4) {
      throw new Fault(
          "setParameterParameterDateTemporalTypeIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: getParameterValueStringTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:410;PERSISTENCE:JAVADOC:659
   * 
   * @test_Strategy: create query and set a String parameter. Verify
   * getParameterValue can retrieve that value create TypedQuery and set a
   * String parameter. Verify getParameterValue can retrieve that value
   */

  public void getParameterValueStringTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    String expectedName = "fName";
    String expectedValue = "Stephen";

    try {
      TestUtil.logMsg("Testing Query version");
      Query query = getEntityManager()
          .createQuery("select e from Employee e where e.firstName = :fName")
          .setParameter(expectedName, expectedValue);

      String s = (String) query.getParameterValue(expectedName);

      if (!s.equals(expectedValue)) {
        TestUtil.logErr("Expected:" + expectedValue + ",Actual=" + s);
      } else {
        pass1 = true;
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    TestUtil.logMsg("Testing TypedQuery version");

    try {
      TypedQuery<Employee> query = getEntityManager()
          .createQuery("select e from Employee e where e.firstName = :fName",
              Employee.class)
          .setParameter(expectedName, expectedValue);
      String s = (String) query.getParameterValue(expectedName);

      if (!s.equals(expectedValue)) {
        TestUtil.logErr("Expected:" + expectedValue + ",Actual=" + s);
      } else {
        pass2 = true;
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass1 || !pass2) {
      throw new Fault("getParameterValueStringTest failed");
    }
  }

  /*
   * @testName: getParameterValueStringIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:568; PERSISTENCE:JAVADOC:661
   * 
   * @test_Strategy: create a query and set a String parameter. Try to get the
   * parameter value from the query and verify IllegalArgumentException is
   * thrown create a TypedQuery and set a String parameter. Try to get the
   * parameter value from the query and verify IllegalArgumentException is
   * thrown*
   */
  public void getParameterValueStringIllegalArgumentExceptionTest()
      throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;

    try {
      TestUtil.logMsg("Testing Query version");
      Query query = getEntityManager()
          .createQuery("select e from Employee e where e.firstName = :fName1")
          .setParameter("fName1", "fnameValue");

      query.getParameterValue("fName2");
      TestUtil.logErr("IllegalArgumentException was not thrown");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
      pass1 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    TestUtil.logMsg("Testing TypedQuery version");

    try {
      TypedQuery<Employee> query = getEntityManager()
          .createQuery("select e from Employee e where e.firstName = :fName1",
              Employee.class)
          .setParameter("fName1", "fnameValue");

      query.getParameterValue("fName2");
      TestUtil.logErr("IllegalArgumentException was not thrown");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
      pass2 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    if (!pass1 || !pass2) {
      throw new Fault(
          "getParameterValueStringIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: getParameterValueStringIllegalStateExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:567; PERSISTENCE:JAVADOC:660
   * 
   * @test_Strategy: create a query and don't set a name parameter. Verify
   * getParameterValue for a parameter that is not bound throws an
   * IllegalStateException
   */
  public void getParameterValueStringIllegalStateExceptionTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;

    try {
      TestUtil.logMsg("Testing Query version");
      Query query = getEntityManager()
          .createQuery("select e from Employee e where e.firstName = :fname1");

      query.getParameterValue("fname1");
      TestUtil.logErr("IllegalStateException was not thrown");
    } catch (IllegalStateException iae) {
      TestUtil.logTrace("Received expected IllegalStateException");
      pass1 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    }
    TestUtil.logMsg("Testing TypedQuery version");

    try {
      TypedQuery<Employee> query = getEntityManager().createQuery(
          "select e from Employee e where e.firstName = :fname1",
          Employee.class);

      query.getParameterValue("fname1");
      TestUtil.logErr("IllegalStateException was not thrown");
    } catch (IllegalStateException iae) {
      TestUtil.logTrace("Received expected IllegalStateException");
      pass2 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    }
    if (!pass1 || !pass2) {
      throw new Fault(
          "getParameterValueStringIllegalStateExceptionTest failed");
    }
  }

  /*
   * @testName: getParameterValueIntTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:411; PERSISTENCE:JAVADOC:662
   * 
   * @test_Strategy: create query and set a positional parameter. Verify
   * getParameterValue can retrieve that value create TypedQuery and set a
   * positional parameter. Verify getParameterValue can retrieve that value
   */
  public void getParameterValueIntTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    String expectedValue = "Stephen";

    try {
      TestUtil.logMsg("Testing Query version");
      Query query = getEntityManager()
          .createQuery("select e from Employee e where e.firstName = ?1")
          .setParameter(1, expectedValue);

      String s = (String) query.getParameterValue(1);

      if (!s.equals(expectedValue)) {
        TestUtil.logErr("Expected:" + expectedValue + ",Actual=" + s);
      } else {
        pass1 = true;
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    TestUtil.logMsg("Testing TypedQuery version");

    try {
      TypedQuery<Employee> query = getEntityManager()
          .createQuery("select e from Employee e where e.firstName = ?1",
              Employee.class)
          .setParameter(1, expectedValue);
      String s = (String) query.getParameterValue(1);

      if (!s.equals(expectedValue)) {
        TestUtil.logErr("Expected:" + expectedValue + ",Actual=" + s);
      } else {
        pass2 = true;
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass1 || !pass2) {
      throw new Fault("getParameterValueIntTest failed");
    }
  }

  /*
   * @testName: getParameterValueIntIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:570; PERSISTENCE:JAVADOC:664
   * 
   * @test_Strategy: create query and set a positional parameter. Verify
   * getParameterValue for a position that does not exist throws
   * IllegalArgumentException create TypedQuery and set a positional parameter.
   * Verify getParameterValue for a position that does not exist throws
   * IllegalArgumentException
   */
  public void getParameterValueIntIllegalArgumentExceptionTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    try {
      TestUtil.logMsg("Testing Query version");
      Query query = getEntityManager()
          .createQuery("select e from Employee e where e.firstName = ?1")
          .setParameter(1, "foo");

      query.getParameterValue(99);
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
      pass1 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    }
    TestUtil.logMsg("Testing TypedQuery version");

    try {
      TypedQuery<Employee> query = getEntityManager()
          .createQuery("select e from Employee e where e.firstName = ?1",
              Employee.class)
          .setParameter(1, "foo");
      query.getParameterValue(99);
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
      pass2 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    }
    if (!pass1 || !pass2) {
      throw new Fault(
          "getParameterValueIntIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: getParameterValueIntIllegalStateExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:569; PERSISTENCE:JAVADOC:663
   * 
   * @test_Strategy: create query and don't set a positional parameter. Verify
   * getParameterValue for a position that is not bound throws
   * IllegalStateException create TypedQuery and don't set a positional
   * parameter. Verify getParameterValue for a position that is not bound throws
   * IllegalStateException
   */
  public void getParameterValueIntIllegalStateExceptionTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    try {
      TestUtil.logMsg("Testing Query version");
      Query query = getEntityManager()
          .createQuery("select e from Employee e where e.firstName = ?1");
      query.getParameterValue(1);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("Received expected IllegalStateException");
      pass1 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    }
    TestUtil.logMsg("Testing TypedQuery version");

    try {
      TypedQuery<Employee> query = getEntityManager().createQuery(
          "select e from Employee e where e.firstName = ?1", Employee.class);
      query.getParameterValue(1);
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException e) {
      TestUtil.logTrace("Received expected IllegalStateException");
      pass2 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    }
    if (!pass1 || !pass2) {
      throw new Fault("getParameterValueIntIllegalStateExceptionTest failed");
    }
  }

  /*
   * @testName: setParameter1Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:176; PERSISTENCE:JAVADOC:412;
   * PERSISTENCE:JAVADOC:448; PERSISTENCE:JAVADOC:697; PERSISTENCE:SPEC:1305;
   * 
   * @test_Strategy: Obtain employees using valid name/value data in query.
   * Obtain employees using valid name/value data in a TypedQuery.
   */
  public void setParameter1Test() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    Integer[] expected = new Integer[2];
    expected[0] = empRef[4].getId();
    expected[1] = empRef[6].getId();

    try {
      TestUtil.logMsg("Testing Query version");
      getEntityTransaction().begin();

      Query query = getEntityManager()
          .createQuery("select e from Employee e where e.firstName = :fName")
          .setParameter("fName", "Stephen");

      List<Employee> result = query.getResultList();

      if (!checkEntityPK(result, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.length + " references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass1 = true;
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
        TestUtil.logErr("Unexpected Exception in while rolling back TX:", re);
      }
    }

    TestUtil.logMsg("Testing TypedQuery version");

    try {
      getEntityTransaction().begin();

      TypedQuery<Employee> query = getEntityManager()
          .createQuery("select e from Employee e where e.firstName = :fName",
              Employee.class)
          .setParameter("fName", "Stephen");

      List<Employee> result = query.getResultList();

      if (!checkEntityPK(result, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.length + " references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass2 = true;
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
        TestUtil.logErr("Unexpected Exception in while rolling back TX:", re);
      }
    }

    if (!pass1 || !pass2) {
      throw new Fault("setParameter1Test failed");
    }
  }

  /*
   * @testName: setParameter2Test
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1306; PERSISTENCE:SPEC:1307;
   * 
   * @test_Strategy: Test named parameters for case-sensitive
   */
  public void setParameter2Test() throws Fault {
    boolean pass = false;
    Integer[] expected = new Integer[3];
    expected[0] = empRef[0].getId();
    expected[1] = empRef[4].getId();
    expected[2] = empRef[6].getId();

    try {
      getEntityTransaction().begin();

      Query query = getEntityManager().createQuery(
          "select e from Employee e where e.firstName = :fName or e.firstName = :Fname")
          .setParameter("fName", "Stephen").setParameter("Fname", "Alan");

      List<Employee> result = query.getResultList();

      if (!checkEntityPK(result, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.length + " references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
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
        TestUtil.logErr("Unexpected Exception in while rolling back TX:", re);
      }
    }

    if (!pass) {
      throw new Fault("setParameter2Test failed");
    }
  }

  /*
   * @testName: setParameterStringObject1IllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:732; PERSISTENCE:JAVADOC:176;
   * PERSISTENCE:JAVADOC:625
   * 
   * @test_Strategy: setParameter(String, Object) containing an argument of an
   * incorrect type should throw an IllegalArgumentException.
   * TypedQuery.setParameter(String, Object) containing an argument of an
   * incorrect type should throw an IllegalArgumentException.*
   */
  public void setParameterStringObject1IllegalArgumentExceptionTest()
      throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    TestUtil.logMsg("Testing Query version");
    try {
      getEntityManager()
          .createQuery("select e from Employee e where e.firstName = :fName")
          .setParameter("fName", 5.0F);
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
      pass1 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    }

    TestUtil.logMsg("Testing TypedQuery version");

    try {
      getEntityManager()
          .createQuery("select e from Employee e where e.firstName = :fName",
              Employee.class)
          .setParameter("fName", 5.0F);
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
      pass2 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    }

    if (!pass1 || !pass2) {
      throw new Fault(
          "setParameterStringObject1IllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: setParameterStringObject2IllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:732; PERSISTENCE:JAVADOC:176;
   * PERSISTENCE:JAVADOC:592; PERSISTENCE:JAVADOC:625; PERSISTENCE:JAVADOC:698;
   * 
   * @test_Strategy: setParameter(String, Object) containing a parameter name
   * that does not exist should throw an IllegalArgumentException.
   * TypedQuery.setParameter(String, Object) containing a parameter name that
   * does not exist should throw an IllegalArgumentException.
   *
   */
  public void setParameterStringObject2IllegalArgumentExceptionTest()
      throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    TestUtil.logMsg("Testing Query version");
    try {
      getEntityManager()
          .createQuery("select e from Employee e where e.firstName = :fName")
          .setParameter("doesnotexist", "foo");
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
      pass1 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    }

    TestUtil.logMsg("Testing TypedQuery version");

    try {
      getEntityManager()
          .createQuery("select e from Employee e where e.firstName = :fName",
              Employee.class)
          .setParameter("doesnotexist", "foo");
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
      pass2 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    }

    if (!pass1 || !pass2) {
      throw new Fault(
          "setParameterStringObject2IllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: setParameterStringDateTemporalTypeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:177; PERSISTENCE:JAVADOC:450;
   * PERSISTENCE:JAVADOC:701; PERSISTENCE:SPEC:1514; PERSISTENCE:SPEC:1514.1;
   * 
   * @test_Strategy: Obtain employees using valid name/value data in query.
   * Obtain employees using valid name/value data in TypedQuery.
   */
  public void setParameterStringDateTemporalTypeTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;

    Collection<Employee> cEmp = new ArrayList<Employee>();
    cEmp.add(empRef[0]);
    cEmp.add(empRef[19]);

    TestUtil.logMsg("Testing Query version");

    try {
      getEntityTransaction().begin();

      Query query = getEntityManager()
          .createQuery("select e from Employee e where e.hireDate = :hDate");
      Collection<Employee> q = query
          .setParameter("hDate", d1, TemporalType.DATE).getResultList();

      if (!checkEntityPK(q, cEmp)) {
        TestUtil.logErr("Did not get expected results. Expected " + cEmp.size()
            + " references, got: " + q.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass1 = true;
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
        TestUtil.logErr("Unexpected Exception in while rolling back TX:", re);
      }
    }

    TestUtil.logMsg("Testing TypedQuery version");

    try {

      getEntityTransaction().begin();

      TypedQuery<Employee> tquery = getEntityManager().createQuery(
          "select e from Employee e where e.hireDate = :hDate", Employee.class);

      Collection<Employee> q = tquery
          .setParameter("hDate", d1, TemporalType.DATE).getResultList();

      if (!checkEntityPK(q, cEmp)) {
        TestUtil.logErr("Did not get expected results. Expected " + cEmp.size()
            + " references, got: " + q.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass2 = true;
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
        TestUtil.logErr("Unexpected Exception in while rolling back TX:", re);
      }
    }

    if (!pass1 || !pass2) {
      throw new Fault("setParameterStringDateTemporalTypeTest failed");
    }
  }

  /*
   * @testName: setParameterStringDateTemporalTypeIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:730; PERSISTENCE:JAVADOC:594;
   * PERSISTENCE:JAVADOC:627; PERSISTENCE:JAVADOC:702;
   * 
   * @test_Strategy: Query.setParameter(String name, Date value, TemporalType
   * type) containing a parameter name that does not correspond to parameter in
   * query string should throw an IllegalArgumentException.
   * TypedQuery.setParameter(String name, Date value, TemporalType type)
   * containing a parameter name that does not correspond to parameter in query
   * string should throw an IllegalArgumentException.
   */
  public void setParameterStringDateTemporalTypeIllegalArgumentExceptionTest()
      throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    TestUtil.logMsg("Testing Query version");
    try {
      getEntityManager()
          .createQuery("select e from Employee e where e.hireDate = :hDate")
          .setParameter("doesnotexist", d1, TemporalType.DATE);
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("IllegalArgumentException Caught as Expected");
      pass1 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    }

    TestUtil.logMsg("Testing TypedQuery version");

    try {
      getEntityManager()
          .createQuery("select e from Employee e where e.hireDate = :hDate",
              Employee.class)
          .setParameter("doesnotexist", d1, TemporalType.DATE);
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("IllegalArgumentException Caught as Expected");
      pass2 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    }

    if (!pass1 || !pass2) {
      throw new Fault(
          "setParameterStringDateTemporalTypeIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: setParameterStringCalendarTemporalTypeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:178; PERSISTENCE:JAVADOC:449;
   * PERSISTENCE:JAVADOC:699;
   * 
   * @test_Strategy: Obtain employees using Query.setParameter(String, Calendar,
   * TemporalType). Obtain employees using TypedQuery.setParameter(String,
   * Calendar, TemporalType).
   */
  public void setParameterStringCalendarTemporalTypeTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;

    Collection<Employee> cEmp = new ArrayList<Employee>();
    cEmp.add(empRef[20]);

    try {
      TestUtil.logMsg("Testing Query version");

      getEntityTransaction().begin();

      Query query = getEntityManager()
          .createQuery("select e from Employee e where e.hireDate = :hDate");
      Collection<Employee> q = query
          .setParameter("hDate", getCalDate(), TemporalType.DATE)
          .getResultList();

      if (!checkEntityPK(q, cEmp)) {
        TestUtil.logErr("Did not get expected results. Expected " + cEmp.size()
            + " references, got: " + q.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass1 = true;
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
        TestUtil.logErr("Unexpected Exception in while rolling back TX:", re);
      }
    }

    TestUtil.logMsg("Testing TypedQuery version");

    try {
      getEntityTransaction().begin();

      TypedQuery<Employee> tquery = getEntityManager().createQuery(
          "select e from Employee e where e.hireDate = :hDate", Employee.class);
      Collection<Employee> q = tquery
          .setParameter("hDate", getCalDate(), TemporalType.DATE)
          .getResultList();

      if (!checkEntityPK(q, cEmp)) {
        TestUtil.logErr("Did not get expected results. Expected " + cEmp.size()
            + " references, got: " + q.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass2 = true;
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
        TestUtil.logErr("Unexpected Exception in while rolling back TX:", re);
      }
    }

    if (!pass1 || !pass2) {
      throw new Fault("setParameterStringCalendarTemporalTypeTest failed");
    }
  }

  /*
   * @testName:
   * setParameterStringCalendarTemporalTypeTestIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:178; PERSISTENCE:JAVADOC:593;
   * PERSISTENCE:JAVADOC:626; PERSISTENCE:JAVADOC:700;
   * 
   * @test_Strategy: Query.setParameter(String, Calendar, TemporalType)
   * containing a parameter name that does not correspond to parameter in query
   * string should throw an IllegalArgumentException.
   *
   * TypedQuery.setParameter(String, Calendar, TemporalType) containing a
   * parameter name that does not correspond to parameter in query string should
   * throw an IllegalArgumentException.
   */
  public void setParameterStringCalendarTemporalTypeTestIllegalArgumentExceptionTest()
      throws Fault {
    final java.util.Calendar c = Calendar.getInstance();
    boolean pass1 = false;
    boolean pass2 = false;
    TestUtil.logMsg("Testing Query version");
    try {
      getEntityManager()
          .createQuery("select d from Department d where :param > 1")
          .setParameter("badName", c, TemporalType.TIMESTAMP);
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("IllegalArgumentException Caught as Expected");
      pass1 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    }
    TestUtil.logMsg("Testing TypedQuery version");

    try {
      getEntityManager()
          .createQuery("select d from Department d where :param > 1",
              Department.class)
          .setParameter("badName", c, TemporalType.TIMESTAMP);
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("IllegalArgumentException Caught as Expected");
      pass2 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    }

    if (!pass1 || !pass2) {
      throw new Fault(
          "setParameterStringCalendarTemporalTypeTestIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: setParameterIntObjectTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:179; PERSISTENCE:JAVADOC:451;
   * PERSISTENCE:JAVADOC:703;
   * 
   * @test_Strategy: Obtain employees using positional parameter data in query.
   * Obtain employees using positional parameter data in TypedQuery.
   */
  public void setParameterIntObjectTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;

    Collection<Employee> cEmp = new ArrayList<Employee>();
    cEmp.add(empRef[4]);
    cEmp.add(empRef[6]);

    try {
      TestUtil.logMsg("Testing Query version");

      getEntityTransaction().begin();

      Query query = getEntityManager()
          .createQuery("select e from Employee e where e.firstName = ?1");
      Collection<Employee> q = query.setParameter(1, "Stephen").getResultList();

      if (!checkEntityPK(q, cEmp)) {
        TestUtil.logErr("Did not get expected results. Expected " + cEmp.size()
            + " references, got: " + q.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass1 = true;
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
        TestUtil.logErr("Unexpected Exception in while rolling back TX:", re);
      }
    }

    TestUtil.logMsg("Testing TypedQuery version");

    try {
      getEntityTransaction().begin();

      TypedQuery<Employee> tquery = getEntityManager().createQuery(
          "select e from Employee e where e.firstName = ?1", Employee.class);
      Collection<Employee> q = tquery.setParameter(1, "Stephen")
          .getResultList();

      if (!checkEntityPK(q, cEmp)) {
        TestUtil.logErr("Did not get expected results. Expected " + cEmp.size()
            + " references, got: " + q.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass2 = true;
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
        TestUtil.logErr("Unexpected Exception in while rolling back TX:", re);
      }
    }

    if (!pass1 || !pass2) {
      throw new Fault("setParameterIntObjectTest failed");
    }
  }

  /*
   * @testName: setParameterIntObjectIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:595; PERSISTENCE:JAVADOC:628;
   * PERSISTENCE:JAVADOC:704;
   * 
   * @test_Strategy: Query.setParameter(int position, Object value) which sets a
   * positional parameter which is not used in the query string. An
   * IllegalArgumentException should be thrown.
   *
   * TypedQuery.setParameter(int position, Object value) which sets a positional
   * parameter which is not used in the query string. An
   * IllegalArgumentException should be thrown.
   */
  public void setParameterIntObjectIllegalArgumentExceptionTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    boolean pass4 = false;

    TestUtil.logMsg("Testing query version");
    try {
      TestUtil.logMsg("Testing a parm that does not exist ");
      getEntityManager()
          .createQuery("select e from Employee e where e.firstName = ?1")
          .setParameter(1, "Kellie").setParameter(2, "Lee");
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("IllegalArgumentException Caught as Expected");
      pass1 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    }

    try {
      TestUtil.logMsg("Testing a parm of incorrect type");
      getEntityManager()
          .createQuery("select e from Employee e where e.firstName = ?1")
          .setParameter(1, 1);
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("IllegalArgumentException Caught as Expected");
      pass2 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    }
    TestUtil.logMsg("Testing TypedQuery version");

    try {
      TestUtil.logMsg("Testing a parm that does not exist");
      getEntityManager()
          .createQuery("select e from Employee e where e.firstName = ?1",
              Employee.class)
          .setParameter(1, "Kellie").setParameter(2, "Lee");
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("IllegalArgumentException Caught as Expected");
      pass3 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    }
    try {
      TestUtil.logMsg("Testing a parm of incorrect type");
      getEntityManager()
          .createQuery("select e from Employee e where e.firstName = ?1",
              Employee.class)
          .setParameter(1, 1);
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("IllegalArgumentException Caught as Expected");
      pass4 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    }

    if (!pass1 || !pass2 || !pass3 || !pass4) {
      throw new Fault(
          "setParameterIntObjectIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: setParameterIntDateTemporalTypeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:180; PERSISTENCE:JAVADOC:453;
   * PERSISTENCE:JAVADOC:707;
   * 
   * @test_Strategy: Obtain employees using positional parameter data in the
   * query.
   *
   * Obtain employees using positional parameter data in the TypedQuery.
   */
  public void setParameterIntDateTemporalTypeTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    Collection<Employee> cEmp = new ArrayList<Employee>();
    cEmp.add(empRef[0]);
    cEmp.add(empRef[19]);

    try {
      TestUtil.logMsg("Testing Query version");
      getEntityTransaction().begin();

      Collection<Employee> q = getEntityManager()
          .createQuery("select e from Employee e where e.hireDate = ?1")
          .setParameter(1, d1, TemporalType.DATE).getResultList();

      if (!checkEntityPK(q, cEmp)) {
        TestUtil.logErr("Did not get expected results. Expected " + cEmp.size()
            + " references, got: " + q.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass1 = true;
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
        TestUtil.logErr("Unexpected Exception in while rolling back TX:", re);
      }
    }

    TestUtil.logMsg("Testing TypedQuery version");

    try {
      getEntityTransaction().begin();
      TypedQuery<Employee> tquery = getEntityManager().createQuery(
          "select e from Employee e where e.hireDate = ?1", Employee.class);
      Collection<Employee> q = tquery.setParameter(1, d1, TemporalType.DATE)
          .getResultList();

      if (!checkEntityPK(q, cEmp)) {
        TestUtil.logErr("Did not get expected results. Expected " + cEmp.size()
            + " references, got: " + q.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass2 = true;
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
        TestUtil.logErr("Unexpected Exception in while rolling back TX:", re);
      }
    }

    if (!pass1 || !pass2) {
      throw new Fault("setParameterIntDateTemporalTypeTest failed");
    }
  }

  /*
   * @testName: setParameterIntDateTemporalTypeIllegalArgumentException1Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:180; PERSISTENCE:JAVADOC:597;
   * PERSISTENCE:JAVADOC:630; PERSISTENCE:JAVADOC:708;
   * 
   * @test_Strategy: Query.setParameter(int position, Date value, TemporalType
   * type) containing a positional parameter that does not correspond to
   * parameter in query string should throw an IllegalArgumentException.
   * TypedQuery.setParameter(int position, Date value, TemporalType type)
   * containing a positional parameter that does not correspond to parameter in
   * query string should throw an IllegalArgumentException.
   */
  public void setParameterIntDateTemporalTypeIllegalArgumentException1Test()
      throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    TestUtil.logMsg("Testing Query version");
    try {
      getEntityManager()
          .createQuery("select d from Department d where :hDate > 1")
          .setParameter(5, d1, TemporalType.DATE);
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("IllegalArgumentException Caught as Expected");
      pass1 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    }
    TestUtil.logMsg("Testing TypedQuery version");

    try {
      getEntityManager()
          .createQuery("select d from Department d where :hDate > 1",
              Department.class)
          .setParameter(5, d1, TemporalType.DATE);
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("IllegalArgumentException Caught as Expected");
      pass2 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    }

    if (!pass1 || !pass2) {
      throw new Fault(
          "setParameterIntDateTemporalTypeIllegalArgumentException1Test failed");
    }
  }

  /*
   * @testName: setParameterIntCalendarTemporalTypeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:181; PERSISTENCE:JAVADOC:452;
   * PERSISTENCE:JAVADOC:705;
   * 
   * @test_Strategy: Obtain employees using Query.setParameter(int position,
   * Calendar value, TemporalType type).
   *
   * Obtain employees using TypedQuery.setParameter(int position, Calendar
   * value, TemporalType type).
   */
  public void setParameterIntCalendarTemporalTypeTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;

    Collection<Employee> cEmp = new ArrayList<Employee>();
    cEmp.add(empRef[20]);

    try {
      TestUtil.logMsg("Testing Query version");
      getEntityTransaction().begin();

      Collection<Employee> q = getEntityManager()
          .createQuery("select e from Employee e where e.hireDate = ?1")
          .setParameter(1, getCalDate(), TemporalType.DATE).getResultList();

      if (!checkEntityPK(q, cEmp)) {
        TestUtil.logErr("Did not get expected results. Expected " + cEmp.size()
            + " references, got: " + q.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass1 = true;
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
        TestUtil.logErr("Unexpected Exception in while rolling back TX:", re);
      }
    }

    TestUtil.logMsg("Testing TypedQuery version");

    try {
      getEntityTransaction().begin();
      TypedQuery<Employee> tquery = getEntityManager().createQuery(
          "select e from Employee e where e.hireDate = ?1", Employee.class);

      Collection<Employee> q = tquery
          .setParameter(1, getCalDate(), TemporalType.DATE).getResultList();

      if (!checkEntityPK(q, cEmp)) {
        TestUtil.logErr("Did not get expected results. Expected " + cEmp.size()
            + " references, got: " + q.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass2 = true;
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
        TestUtil.logErr("Unexpected Exception in while rolling back TX:", re);
      }
    }

    if (!pass1 || !pass2) {
      throw new Fault("setParameterIntCalendarTemporalTypeTest failed");
    }
  }

  /*
   * @testName: setParameterIntCalendarTemporalTypeIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:596; PERSISTENCE:JAVADOC:629;
   * PERSISTENCE:JAVADOC:706
   * 
   * @test_Strategy: Query.setParameter(int position, Calendar value,
   * TemporalType type) containing a parameter name that does not correspond to
   * parameter in query string should throw an IllegalArgumentException.
   *
   * TypedQuery.setParameter(int position, Calendar value, TemporalType type)
   * containing a parameter name that does not correspond to parameter in query
   * string should throw an IllegalArgumentException.
   */
  public void setParameterIntCalendarTemporalTypeIllegalArgumentExceptionTest()
      throws Fault {
    final java.util.Calendar c = Calendar.getInstance();
    boolean pass1 = false;
    boolean pass2 = false;
    TestUtil.logMsg("Testing Query version");
    try {
      getEntityManager().createQuery("select d from Department d where ?1 > 1")
          .setParameter(5, c, TemporalType.TIMESTAMP);
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("IllegalArgumentException Caught as Expected");
      pass1 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    }
    TestUtil.logMsg("Testing TypedQuery version");

    try {
      getEntityManager().createQuery("select d from Department d where ?1 > 1",
          Department.class).setParameter(5, c, TemporalType.TIMESTAMP);
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("IllegalArgumentException Caught as Expected");
      pass2 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    }

    if (!pass1 || !pass2) {
      throw new Fault(
          "setParameterIntCalendarTemporalTypeIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: setParameter7Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:414; PERSISTENCE:JAVADOC:445;
   * PERSISTENCE:JAVADOC:691;
   * 
   * @test_Strategy: create Query and set a positional parameter. Use
   * setParameter(Parameter, Object) to change the original parameter value then
   * execute query.
   *
   * create TypedQuery and set a positional parameter. Use
   * setParameter(Parameter, Object) to change the original parameter value then
   * execute query.
   */
  public void setParameter7Test() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;

    Collection<Employee> cEmp = new ArrayList<Employee>();
    cEmp.add(empRef[1]);

    try {
      TestUtil.logMsg("Testing Query version");

      getEntityTransaction().begin();

      Query query = getEntityManager()
          .createQuery("select e from Employee e where e.firstName = ?1")
          .setParameter(1, "Stephen");

      Parameter p = query.getParameter(1);
      query.setParameter(p, "Arthur");

      Collection<Employee> q = query.getResultList();

      if (!checkEntityPK(q, cEmp)) {
        TestUtil.logErr("Did not get expected results. Expected " + cEmp.size()
            + " references, got: " + q.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass1 = true;
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
        TestUtil.logErr("Unexpected Exception in while rolling back TX:", re);
      }
    }

    TestUtil.logMsg("Testing TypedQuery version");

    try {
      getEntityTransaction().begin();

      TypedQuery<Employee> query = getEntityManager()
          .createQuery("select e from Employee e where e.firstName = ?1",
              Employee.class)
          .setParameter(1, "Stephen");

      Parameter p = query.getParameter(1);
      query.setParameter(p, "Arthur");

      Collection<Employee> q = query.getResultList();

      if (!checkEntityPK(q, cEmp)) {
        TestUtil.logErr("Did not get expected results. Expected " + cEmp.size()
            + " references, got: " + q.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass2 = true;
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
        TestUtil.logErr("Unexpected Exception in while rolling back TX:", re);
      }
    }

    if (!pass1 || !pass2) {
      throw new Fault("setParameter7Test failed");
    }
  }

  /*
   * @testName: setParameterParameterCalendarTemporalTypeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:415; PERSISTENCE:JAVADOC:446;
   * PERSISTENCE:JAVADOC:693;
   * 
   * @test_Strategy: create query with a parameter. call
   * setParameter(Parameter,Calendar,TemporalType) for that parameter and verify
   * the correct result is returned create TypedQuery with a parameter. call
   * setParameter(Parameter,Calendar,TemporalType) for that parameter and verify
   * the correct result is returned
   */
  public void setParameterParameterCalendarTemporalTypeTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;

    Collection<Employee> cEmp = new ArrayList<Employee>();
    cEmp.add(empRef[20]);
    try {
      getEntityTransaction().begin();

      TestUtil.logMsg("Testing Query version");
      Query query = getEntityManager()
          .createQuery("select e from Employee e where e.hireDate = ?1");

      Parameter p = query.getParameter(1);
      query.setParameter(p, getCalDate(), TemporalType.DATE);

      Collection<Employee> q = query.getResultList();
      if (!checkEntityPK(q, cEmp)) {
        TestUtil.logErr("Did not get expected results. Expected " + cEmp.size()
            + " references, got: " + q.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass1 = true;
      }

      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    TestUtil.logMsg("Testing TypedQuery version");

    try {
      getEntityTransaction().begin();

      TypedQuery<Employee> tquery = getEntityManager().createQuery(
          "select e from Employee e where e.hireDate = ?1", Employee.class);

      Parameter p = tquery.getParameter(1);
      tquery.setParameter(p, getCalDate(), TemporalType.DATE);

      Collection<Employee> q = tquery.getResultList();
      if (!checkEntityPK(q, cEmp)) {
        TestUtil.logErr("Did not get expected results. Expected " + cEmp.size()
            + " references, got: " + q.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass2 = true;
      }

      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    if (!pass1 || !pass2) {
      throw new Fault("setParameterParameterCalendarTemporalTypeTest failed");
    }

  }

  /*
   * @testName: setParameterParameterDateTemporalTypeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:416; PERSISTENCE:JAVADOC:447;
   * PERSISTENCE:JAVADOC:695;
   * 
   * @test_Strategy: create query with a parameter. call
   * setParameter(Parameter,Date,TemporalType) for that parameter and verify the
   * correct result is returned create TypedQuery with a parameter. call
   * setParameter(Parameter,Date,TemporalType) for that parameter and verify the
   * correct result is returned
   */
  public void setParameterParameterDateTemporalTypeTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;

    Collection<Employee> cEmp = new ArrayList<Employee>();
    cEmp.add(empRef[5]);
    cEmp.add(empRef[14]);
    try {
      getEntityTransaction().begin();

      TestUtil.logMsg("Testing Query version");
      Query query = getEntityManager()
          .createQuery("select e from Employee e where e.hireDate = ?1");

      Parameter p = query.getParameter(1);
      query.setParameter(p, getUtilDate("2005-02-18"), TemporalType.DATE);

      Collection<Employee> q = query.getResultList();
      if (!checkEntityPK(q, cEmp)) {
        TestUtil.logErr("Did not get expected results. Expected " + cEmp.size()
            + " references, got: " + q.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass1 = true;
      }

      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    TestUtil.logMsg("Testing TypedQuery version");

    try {
      getEntityTransaction().begin();

      TypedQuery<Employee> tquery = getEntityManager().createQuery(
          "select e from Employee e where e.hireDate = ?1", Employee.class);

      Parameter p = tquery.getParameter(1);
      tquery.setParameter(p, getUtilDate("2005-02-18"), TemporalType.DATE);

      Collection<Employee> q = tquery.getResultList();
      if (!checkEntityPK(q, cEmp)) {
        TestUtil.logErr("Did not get expected results. Expected " + cEmp.size()
            + " references, got: " + q.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass2 = true;
      }

      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    if (!pass1 || !pass2) {
      throw new Fault("setParameterParameterDateTemporalTypeTest failed");
    }

  }

  /*
   * @testName: setParameter8Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:416
   * 
   * @test_Strategy: Obtain employees using Query.setParameter(int position,
   * Date value, TemporalType type).
   *
   * Obtain employees using TypedQuery.setParameter(int position, Date value,
   * TemporalType type).
   */
  public void setParameter8Test() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;

    Collection<Employee> cEmp = new ArrayList<Employee>();
    cEmp.add(empRef[5]);
    cEmp.add(empRef[14]);
    try {
      TestUtil.logMsg("Testing Query version");
      getEntityTransaction().begin();

      Collection<Employee> q = getEntityManager()
          .createQuery("select e from Employee e where e.hireDate = ?1")
          .setParameter(1, getUtilDate("2005-02-18"), TemporalType.DATE)
          .getResultList();

      if (!checkEntityPK(q, cEmp)) {
        TestUtil.logErr("Did not get expected results. Expected " + cEmp.size()
            + " references, got: " + q.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass1 = true;
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
        TestUtil.logErr("Unexpected Exception in while rolling back TX:", re);
      }
    }

    TestUtil.logMsg("Testing TypedQuery version");

    try {
      getEntityTransaction().begin();

      Collection<Employee> q = getEntityManager()
          .createQuery("select e from Employee e where e.hireDate = ?1",
              Employee.class)
          .setParameter(1, getUtilDate("2005-02-18"), TemporalType.DATE)
          .getResultList();

      if (!checkEntityPK(q, cEmp)) {
        TestUtil.logErr("Did not get expected results. Expected " + cEmp.size()
            + " references, got: " + q.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass2 = true;
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
        TestUtil.logErr("Unexpected Exception in while rolling back TX:", re);
      }
    }

    if (!pass1 || !pass2) {
      throw new Fault("setParameter8Test failed");
    }
  }

  /*
   * @testName: getSingleResultNoResultExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:744; PERSISTENCE:JAVADOC:577;
   * PERSISTENCE:JAVADOC:610; PERSISTENCE:JAVADOC:2706; PERSISTENCE:JAVADOC:673;
   * 
   * @test_Strategy: Query.getSingleResult() is expected to return a single
   * result. If the query does not return a result, an NoResultException is
   * thrown. TypedQuery.getSingleResult() is expected to return a single result.
   * If the query does not return a result, an NoResultException is thrown.
   */
  public void getSingleResultNoResultExceptionTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    try {
      getEntityTransaction().begin();
      TestUtil.logMsg("Testing Query version");
      getEntityManager()
          .createQuery("select d.name from Department d where d.id = 99")
          .getSingleResult();
      TestUtil.logErr("NoResultException was not thrown");
      getEntityTransaction().commit();
    } catch (NoResultException nre) {
      TestUtil.logTrace("Exception Caught as Expected:" + nre);
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
    TestUtil.logMsg("Testing TypedQuery version");

    try {
      getEntityTransaction().begin();
      getEntityManager()
          .createQuery("select d.name from Department d where d.id = 99",
              String.class)
          .getSingleResult();
      TestUtil.logErr("NoResultException was not thrown");
      getEntityTransaction().commit();
    } catch (NoResultException nre) {
      TestUtil.logTrace("Exception Caught as Expected:" + nre);
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
      throw new Fault("getSingleResultNoResultExceptionTest failed");
    }
  }

  /*
   * @testName: getSingleResultTransactionRequiredException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:581; PERSISTENCE:JAVADOC:614;
   * PERSISTENCE:JAVADOC:2710; PERSISTENCE:JAVADOC:677;
   * 
   * @test_Strategy: create query that is an update which has a lock mode set
   * without a transaction being active then call getSingleResult() and verify a
   * TransactionRequiredException is thrown create TypedQuery that is an update
   * which has a lock mode set without a transaction being active then call
   * getSingleResult() and verify a TransactionRequiredException is thrown
   */
  public void getSingleResultTransactionRequiredException() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;

    TestUtil.logMsg("Testing Query version");

    try {
      Query q = getEntityManager()
          .createQuery("select d from  Department d where d.id = 1");
      q.setLockMode(LockModeType.WRITE);
      q.getSingleResult();
      TestUtil.logErr("TransactionRequiredException was not thrown");
    } catch (TransactionRequiredException ise) {
      TestUtil.logTrace("Exception Caught as Expected");
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
    TestUtil.logMsg("Testing TypedQuery version");

    try {
      TypedQuery<Department> q = getEntityManager().createQuery(
          "select d from  Department d where d.id = 1", Department.class);

      q.setLockMode(LockModeType.WRITE);
      q.getSingleResult();
      TestUtil.logErr("TransactionRequiredException was not thrown");
    } catch (TransactionRequiredException ise) {
      TestUtil.logTrace("Exception Caught as Expected");
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
      throw new Fault("getSingleResultTransactionRequiredException failed");
    }
  }

  /*
   * @testName: getSingleResultNonUniqueResultExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:745; PERSISTENCE:JAVADOC:578;
   * PERSISTENCE:JAVADOC:611; PERSISTENCE:JAVADOC:2707; PERSISTENCE:JAVADOC:674;
   * 
   * @test_Strategy: Query.getSingleResult() is expected to return a single
   * result. If the query returns more than one result, a
   * NonUniqueResultException is thrown. TypedQuery.getSingleResult() is
   * expected to return a single result. If the query returns more than one
   * result, a NonUniqueResultException is thrown.
   */
  public void getSingleResultNonUniqueResultExceptionTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    try {
      TestUtil.logMsg("Testing Query version");
      getEntityTransaction().begin();
      getEntityManager()
          .createQuery("select d.name from Department d where d.id > 1")
          .getSingleResult();
      TestUtil.logErr("NoResultException was not thrown");
      getEntityTransaction().commit();
    } catch (NonUniqueResultException nure) {
      TestUtil.logTrace("Exception Caught as Expected:" + nure);
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

    TestUtil.logMsg("Testing TypedQuery version");

    try {
      getEntityTransaction().begin();
      getEntityManager()
          .createQuery("select d.name from Department d where d.id > 1",
              String.class)
          .getSingleResult();
      TestUtil.logErr("NoResultException was not thrown");
      getEntityTransaction().commit();
    } catch (NonUniqueResultException nure) {
      TestUtil.logTrace("Exception Caught as Expected:" + nure);
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
      throw new Fault("getSingleResultNonUniqueResultExceptionTest failed");
    }
  }

  /*
   * @testName: isBoundTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:412; PERSISTENCE:JAVADOC:681
   * 
   * @test_Strategy: Create a query and set a parameter. Verify isbound knows
   * setParameter has set a value. Create a TypedQuery and set a parameter.
   * Verify isbound knows setParameter has set a value.
   */
  public void isBoundTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    String expectedName = "fName";
    String expectedValue = "Stephen";
    try {
      TestUtil.logTrace("Creating query for isBoundTest");
      TestUtil.logMsg("Testing Query version");

      Query query = getEntityManager()
          .createQuery("select e from Employee e where e.firstName = :fName")
          .setParameter(expectedName, expectedValue);

      Parameter p = query.getParameter(expectedName);
      if (p != null) {
        if (!query.isBound(p)) {
          TestUtil.logErr(
              "isbound returned false even though a value is bound to the parameter "
                  + expectedName + "[" + p.getName() + "]");
        } else {
          pass1 = true;
        }
      } else {
        TestUtil.logErr("getParameter returned null");
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    }
    TestUtil.logMsg("Testing TypedQuery version");

    try {
      TypedQuery<Employee> query = getEntityManager()
          .createQuery("select e from Employee e where e.firstName = :fName",
              Employee.class)
          .setParameter(expectedName, expectedValue);

      Parameter p = query.getParameter(expectedName);
      if (p != null) {
        if (!query.isBound(p)) {
          TestUtil.logErr(
              "isbound returned false even though a value is bound to the parameter "
                  + expectedName + "[" + p.getName() + "]");
        } else {
          pass2 = true;
        }
      } else {
        TestUtil.logErr("getParameter returned null");
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    }

    if (!pass1 || !pass2) {
      throw new Fault("isBoundTest failed");
    }
  }

  /*
   * @testName: setFirstResult
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:172
   * 
   * @test_Strategy: If the select clause selects an object, then the number of
   * rows skipped with setFirstResult will correspond to the number of objects
   * specified by setFirstResult."
   */
  public void setFirstResult() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;

    List<Integer> expected = new ArrayList<Integer>();
    expected.add(2);
    expected.add(3);
    expected.add(3);
    expected.add(4);
    expected.add(4);
    expected.add(5);

    Collection<Integer> actual;

    TestUtil.logMsg("Testing query");
    try {
      getEntityTransaction().begin();

      Query q = getEntityManager().createQuery(
          "select e.department.id from Employee e where e.id < 10 order by e.department.id")
          .setFirstResult(3);
      actual = q.getResultList();
      if (!checkEntityPK(actual, expected, true)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.size() + " references, got: " + actual.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass1 = true;
      }
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    try {
      getEntityTransaction().begin();

      TestUtil.logMsg("Testing TypedQuery");
      TypedQuery<Integer> tq = getEntityManager().createQuery(
          "select e.department.id from Employee e where e.id < 10 order by e.department.id",
          Integer.class).setFirstResult(3);
      actual = tq.getResultList();
      if (!checkEntityPK(actual, expected, true)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.size() + " references, got: " + actual.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass2 = true;
      }

      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass1 || !pass2) {
      throw new Fault("setFirstResult failed");
    }
  }

  /*
   * @testName: queryAPITest11
   * 
   * @assertion_ids: PERSISTENCE:SPEC:729; PERSISTENCE:SPEC:731
   * 
   * @test_Strategy: setParameter(int position, Object value) which has a
   * positional parameter value specified that does not correspond to a
   * positional parameter in the query string. An IllegalArgumentException is
   * thrown.
   */
  public void queryAPITest11() throws Fault {
    boolean pass = false;
    try {
      TestUtil.logMsg("Testing Query version");

      getEntityTransaction().begin();
      Query query = null;
      try {
        query = getEntityManager().createQuery(
            "select e from Employee e where e.firstName = ?1 and e.lastName = ?3")
            .setParameter(1, "Kellie").setParameter(2, "Lee");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("IllegalArgumentException Caught as Expected");
        pass = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exeception", e);
      }
      if (!pass) {
        try {
          query.getResultList();
        } catch (RuntimeException e) {
          TestUtil.logMsg("Did not get expected IllegalArgumentException when "
              + "setting an invalid parameter on a query, but got "
              + "expected RuntimeException when executing the query: " + e);
          pass = true;
        }
      }
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

    if (!pass) {
      throw new Fault("queryAPITest11 failed");
    }
  }

  /*
   * @testName: queryAPITest12
   * 
   * @assertion_ids: PERSISTENCE:SPEC:729; PERSISTENCE:SPEC:731
   * 
   * @test_Strategy: setParameter(int position, Object value) which defines a
   * value of the incorrect type should throw an IllegalArgumentException.
   */
  public void queryAPITest12() throws Fault {
    boolean pass = false;
    TestUtil.logTrace("invoke query for queryAPITest12 ...");
    TestUtil.logMsg("Testing Query version");
    try {
      getEntityTransaction().begin();
      Query query = null;

      try {
        query = getEntityManager().createQuery(
            "select e from Employee e where e.firstName = ?1 and e.lastName = ?2")
            .setParameter(1, "Kate").setParameter(2, 10);
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("IllegalArgumentException Caught as Expected");
        pass = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exeception", e);
      }
      if (!pass) {
        try {
          query.getResultList();
        } catch (RuntimeException e) {
          TestUtil.logMsg("Did not get expected IllegalArgumentException when "
              + "setting an invalid parameter on a query, but got "
              + "expected RuntimeException when executing the query: " + e);
          pass = true;
        }
      }
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

    if (!pass) {
      throw new Fault("queryAPITest12 failed");
    }
  }

  /*
   * @testName: setFirstResultIllegalArgumentException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:172; PERSISTENCE:JAVADOC:683;
   * 
   * @test_Strategy: setFirstResult(int startPosition) with a negative value for
   * startPosition should throw an IllegalArgumentException.
   */
  public void setFirstResultIllegalArgumentException() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    getEntityTransaction().begin();

    TestUtil.logMsg("Testing query version");
    try {

      getEntityManager().createQuery("select d from Department d")
          .setFirstResult(-3);

      TestUtil.logErr("IllegalArgumentException was not thrown");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("IllegalArgumentException Caught as Expected");
      pass1 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    TestUtil.logMsg("Testing TypedQuery version");
    try {

      getEntityManager()
          .createQuery("select d from Department d", Department.class)
          .setFirstResult(-3);

      TestUtil.logErr("IllegalArgumentException was not thrown");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("IllegalArgumentException Caught as Expected");
      pass2 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    getEntityTransaction().rollback();

    if (!pass1 || !pass2) {
      throw new Fault("setFirstResultIllegalArgumentException failed");
    }
  }

  /*
   * @testName: setGetMaxResultsTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:175; PERSISTENCE:JAVADOC:403;
   * PERSISTENCE:JAVADOC:444; PERSISTENCE:JAVADOC:641; PERSISTENCE:JAVADOC:170;
   * PERSISTENCE:JAVADOC:438
   * 
   * @test_Strategy: Using Query.setMaxResult() set the maximum number of
   * results to a value which exceeds number of expected result and verify the
   * result set. Using TypedQuery.setMaxResult() set the maximum number of
   * results to a value which exceeds number of expected result and verify the
   * result set.
   */
  public void setGetMaxResultsTest() throws Fault {
    Collection<Department> q;
    boolean pass1 = false;
    boolean pass2 = false;

    final Integer expected[] = { 1, 2, 3, 4, 5 };

    try {
      TestUtil.logMsg("Testing Query version");

      getEntityTransaction().begin();

      Query query = getEntityManager()
          .createQuery("select d from Department d order by d.id");
      int gmr = query.getMaxResults();
      if (gmr != Integer.MAX_VALUE) {
        TestUtil.logErr(
            "getMaxResults() called when setMaxResults() not called - Expected:"
                + Integer.MAX_VALUE + ", actual:" + gmr);
        pass1 = false;
      } else {
        query.setMaxResults(15);
        gmr = query.getMaxResults();
        if (gmr != 15) {
          TestUtil.logErr("getMaxResults() - Expected: 15, Actual:" + gmr);
          pass1 = false;
        } else {
          q = query.getResultList();
          if (!checkEntityPK(q, expected)) {
            TestUtil.logErr("Did not get expected results. Expected "
                + expected.length + " references, got: " + q.size());
          } else {
            TestUtil.logTrace("Expected results received");
            pass1 = true;
          }
        }
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      pass1 = false;
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

    TestUtil.logMsg("Testing TypedQuery version");

    try {
      getEntityTransaction().begin();

      TypedQuery<Department> query = getEntityManager().createQuery(
          "select d from Department d order by d.id", Department.class);

      int gmr = query.getMaxResults();
      if (gmr != Integer.MAX_VALUE) {
        TestUtil.logErr(
            "getMaxResults() called when setMaxResults() not called - Expected:"
                + Integer.MAX_VALUE + ", actual:" + gmr);
        pass2 = false;
      } else {
        query.setMaxResults(15);
        gmr = query.getMaxResults();
        if (gmr != 15) {
          TestUtil.logErr("getMaxResults() - Expected: 15, Actual:" + gmr);
          pass2 = false;
        } else {
          q = query.getResultList();
          if (!checkEntityPK(q, expected)) {
            TestUtil.logErr("Did not get expected results. Expected "
                + expected.length + " references, got: " + q.size());
          } else {
            TestUtil.logTrace("Expected results received");
            pass2 = true;
          }
        }
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      pass2 = false;
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
      throw new Fault("setGetMaxResultsTest failed");
    }
  }

  /*
   * @testName: setMaxResultsIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:588; PERSISTENCE:JAVADOC:621;
   * PERSISTENCE:JAVADOC:690;
   * 
   * @test_Strategy: Call Query.setMaxResult(-1) and verify an
   * IllegalArgumentException is thrown Call TypedQuery.setMaxResult(-1) and
   * verify an IllegalArgumentException is thrown
   */
  public void setMaxResultsIllegalArgumentExceptionTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    try {
      TestUtil.logMsg("Testing Query version");

      getEntityTransaction().begin();

      getEntityManager().createQuery("select d from Department d order by d.id")
          .setMaxResults(-15);
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
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
    TestUtil.logMsg("Testing TypedQuery version");

    try {
      getEntityTransaction().begin();

      getEntityManager().createQuery("select d from Department d order by d.id",
          Department.class).setMaxResults(-15);
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
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
      throw new Fault("setMaxResultsIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: getResultListTransactionRequiredExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:573; PERSISTENCE:JAVADOC:606;
   * PERSISTENCE:JAVADOC:668;
   *
   * @test_Strategy: Using Query.setMaxResult() set the maximum number of
   * results to a value which exceeds number of expected result and verify the
   * result set. Using TypedQuery.setMaxResult() set the maximum number of
   * results to a value which exceeds number of expected result and verify the
   * result set.
   */
  public void getResultListTransactionRequiredExceptionTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    try {
      TestUtil.logMsg("Testing Query version");

      Query query = getEntityManager()
          .createQuery("select d from Department d ");
      query.setLockMode(LockModeType.READ);

      query.getResultList();
      TestUtil.logErr("TransactionRequiredException not thrown");
    } catch (TransactionRequiredException tre) {
      TestUtil.logTrace("Received expected TransactionRequiredException ");
      pass1 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    }

    TestUtil.logMsg("Testing TypedQuery version");

    try {
      TypedQuery<Department> query = getEntityManager()
          .createQuery("select d from Department d ", Department.class);
      query.setLockMode(LockModeType.READ);

      query.getResultList();
    } catch (TransactionRequiredException tre) {
      TestUtil.logTrace("Received expected TransactionRequiredException ");
      pass2 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    }

    if (!pass1 || !pass2) {
      throw new Fault("getResultListTransactionRequiredExceptionTest failed");
    }
  }

  /*
   * @testName: setMaxResults
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:175
   * 
   * @test_Strategy: Using setMaxResult() set the maximum number of results to a
   * value which is less than that of the expected results and verify the result
   * set returned is only contains the number of results requested to be
   * retrieved.
   */
  public void setMaxResults() throws Fault {
    Collection<Department> q;
    boolean pass = false;
    int found = 0;
    final Integer expected[] = { 4, 1 };

    try {
      TestUtil.logTrace("Invoking query");
      getEntityTransaction().begin();
      q = getEntityManager()
          .createQuery("select d from Department d order by d.name")
          .setMaxResults(2).getResultList();

      if (!checkEntityPK(q, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.length + " references, got: " + q.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
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
        TestUtil.logErr("Unexpected Exception in while rolling back TX:", re);
      }
    }

    if (!pass) {
      throw new Fault("setMaxResults failed");
    }
  }

  /*
   * @testName: queryAPITest16
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:571;
   * 
   * @test_Strategy: getResultList() should throw an IllegalStateException if
   * called for an EJB QL Update statement.
   */
  public void queryAPITest16() throws Fault {
    boolean pass1 = false;
    try {
      TestUtil.logMsg("Testing Query version");
      getEntityTransaction().begin();
      Query q = getEntityManager().createQuery(
          "UPDATE Employee e SET e.salary = e.salary * 10.0 where e.salary > :minsal")
          .setParameter("minsal", (float) 50000.0);
      q.getResultList();

      getEntityTransaction().commit();
      TestUtil.logErr("IllegalStateException not thrown");
    } catch (IllegalStateException ise) {
      TestUtil.logTrace("IllegalStateException Caught as Expected");
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

    if (!pass1) {
      throw new Fault("queryAPITest16 failed");
    }
  }

  /*
   * @testName: queryAPITest17
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:170
   * 
   * @test_Strategy: getResultList() should throw an IllegalStateException if
   * called for an EJB QL Delete statement.
   */
  public void queryAPITest17() throws Fault {
    Query q;
    boolean pass = false;

    try {
      TestUtil.logTrace("Invoking query");
      getEntityTransaction().begin();
      q = getEntityManager()
          .createQuery("DELETE FROM Employee e where e.salary > :minsal")
          .setParameter("minsal", (float) 50000.0);
      q.getResultList();

      getEntityTransaction().commit();
    } catch (IllegalStateException ise) {
      TestUtil.logTrace("IllegalStateException Caught as Expected");
      pass = true;
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

    if (!pass) {
      throw new Fault("queryAPITest17 failed");
    }
  }

  /*
   * @testName: getSingleResultTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:171; PERSISTENCE:JAVADOC:439;
   * PERSISTENCE:JAVADOC:672;
   * 
   * @test_Strategy: create query and call getSingleResult() and verify result
   * create TypedQuery and call getSingleResult() and verify result
   */
  public void getSingleResultTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    try {
      TestUtil.logMsg("Testing Query version");
      getEntityTransaction().begin();
      Query q = getEntityManager()
          .createQuery("select e from Employee e where e.id = 1");
      Object o = q.getSingleResult();
      if (o instanceof Employee) {
        Employee e = (Employee) o;
        if (e.getId() != 1) {
          TestUtil.logErr("Expected employee with id:1, actual:" + e.getId());
        } else {
          pass1 = true;
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
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception in while rolling back TX:", re);
      }
    }
    TestUtil.logMsg("Testing TypedQuery version");

    try {
      getEntityTransaction().begin();
      TypedQuery<Employee> q = getEntityManager().createQuery(
          "select e from Employee e where e.id = 1", Employee.class);
      Employee e = q.getSingleResult();
      if (e.getId() != 1) {
        TestUtil.logErr("Expected employee with id:1, actual:" + e.getId());
      } else {
        pass2 = true;
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
        TestUtil.logErr("Unexpected Exception in while rolling back TX:", re);
      }
    }
    if (!pass1 || !pass2) {
      throw new Fault("getSingleResultTest failed");
    }
  }

  /*
   * @testName: getSingleResultIllegalStateException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:2708; PERSISTENCE:JAVADOC:579;
   *
   * @test_Strategy: getSingleResult() should throw an IllegalStateException if
   * called for an update or delete statement.
   */
  public void getSingleResultIllegalStateException() throws Fault {
    Query q;
    boolean pass1 = false;
    boolean pass2 = false;
    getEntityTransaction().begin();

    TestUtil.logMsg("Testing delete query");
    try {
      q = getEntityManager()
          .createQuery("DELETE FROM Employee e where e.salary > 50000.0");
      q.getSingleResult();
      TestUtil.logErr("IllegalStateException was not thrown");
    } catch (IllegalStateException ise) {
      TestUtil.logTrace("IllegalStateException Caught as Expected");
      pass1 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    TestUtil.logMsg("Testing update query");
    try {
      q = getEntityManager()
          .createQuery("Update Employee e SET e.salary = e.salary * 10.0");
      q.getSingleResult();
      TestUtil.logErr("IllegalStateException was not thrown");
    } catch (IllegalStateException ise) {
      TestUtil.logTrace("IllegalStateException Caught as Expected");
      pass2 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    try {
      if (getEntityTransaction().isActive()) {
        getEntityTransaction().rollback();
      }
    } catch (Exception re) {
      TestUtil.logErr("Unexpected Exception in while rolling back TX:", re);
    }
    if (!pass1 || !pass2) {
      throw new Fault("getSingleResultIllegalStateException failed");
    }
  }

  /*
   * @testName: executeUpdateIllegalStateException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:551; PERSISTENCE:JAVADOC:632
   * 
   * @test_Strategy: Query.executeUpdate() should throw an IllegalStateException
   * if called for a JPQL Select statement. TypedQuery.executeUpdate() should
   * throw an IllegalStateException if called for a JPQL Select statement.
   */
  public void executeUpdateIllegalStateException() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;

    TestUtil.logMsg("Testing Query version");

    try {

      getEntityTransaction().begin();
      getEntityManager().createQuery("select d.id from Department d")
          .executeUpdate();
      TestUtil.logErr("IllegalStateException was not thrown");

      getEntityTransaction().commit();

    } catch (IllegalStateException ise) {
      TestUtil.logTrace("IllegalStateException Caught as Expected");
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
    TestUtil.logMsg("Testing TypedQuery version");

    try {
      getEntityTransaction().begin();
      getEntityManager()
          .createQuery("select d.id from Department d", Integer.class)
          .executeUpdate();
      TestUtil.logErr("IllegalStateException was not thrown");
      getEntityTransaction().commit();

    } catch (IllegalStateException ise) {
      TestUtil.logTrace("IllegalStateException Caught as Expected");
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
      throw new Fault("executeUpdateIllegalStateException failed");
    }
  }

  /*
   * @testName: executeUpdateTransactionRequiredExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:552;
   * 
   * @test_Strategy: Query.executeUpdate() should throw an
   * TransactionRequiredException when no Transaction is active
   */
  public void executeUpdateTransactionRequiredExceptionTest() throws Fault {
    boolean pass1 = false;

    TestUtil.logMsg("Testing Query version");

    try {
      getEntityManager()
          .createQuery(
              "update Department d  set d.name = NULLIF(d.name, 'Engineering')")
          .executeUpdate();
      TestUtil.logErr("TransactionRequiredException was not thrown");
    } catch (TransactionRequiredException ise) {
      TestUtil.logTrace("TransactionRequiredException Caught as Expected");
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

    if (!pass1) {
      throw new Fault("executeUpdateTransactionRequiredExceptionTest failed");
    }
  }

  /*
   * @testName: queryAPITest21
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:173
   * 
   * @test_Strategy: setFlushMode - AUTO
   *
   */
  public void queryAPITest21() throws Fault {
    boolean pass = false;

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("Starting queryAPITest21");
      Department dept1 = getEntityManager().find(Department.class, 1);
      dept1.setName("Research and Development");
      getEntityManager().createQuery(
          "SELECT d FROM Department d WHERE d.name = 'Research and Development'")
          .setFlushMode(FlushModeType.AUTO).getResultList();

      Department newDepartment = getEntityManager().find(Department.class, 1);
      if (newDepartment.getName().equals("Research and Development")) {
        TestUtil.logTrace("Expected results received");
        pass = true;
      } else {
        TestUtil.logErr("Expected result:Research and Development, actual:"
            + newDepartment.getName());
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception:", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception in while rolling back TX:", re);
      }
    }

    if (!pass) {
      throw new Fault("queryAPITest21 failed");
    }
  }

  /*
   * @testName: queryAPITest22
   * 
   * @assertion_ids: PERSISTENCE:SPEC:746.2
   * 
   * @test_Strategy: Update Query
   *
   */
  public void queryAPITest22() throws Fault {
    Query q;
    int result_size = 0;
    boolean pass = false;

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("Setting department name to IT for id 1");
      q = getEntityManager()
          .createQuery("update Department d set d.name='IT' where d.id=1");

      result_size = q.executeUpdate();
      if (result_size == 1) {
        TestUtil.logTrace("Updated 1 rows");
      }

      doFlush();
      clearCache();
      Department dept = getEntityManager().find(Department.class, 1);
      if (dept != null) {
        if (dept.getId() == 1) {
          if (dept.getName().equals("IT")) {
            TestUtil.logTrace("Received expected name:" + dept.getName());
            pass = true;
          } else {
            TestUtil.logErr("Received unexpected d.name =" + dept.getName());
          }
        } else {
          TestUtil.logErr(
              "Received incorrect Department, expected id=1 and name=IT, actual id="
                  + dept.getId() + " and name=" + dept.getName());
        }

      } else {
        TestUtil.logErr("department returned was null");
      }

      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    }

    if (!pass) {
      throw new Fault("queryAPITest22 failed");
    }
  }

  /*
   * @testName: queryAPITest23
   * 
   * @assertion_ids: PERSISTENCE:SPEC:746.3; PERSISTENCE:SPEC:786;
   * PERSISTENCE:SPEC:840; PERSISTENCE:SPEC:841; PERSISTENCE:SPEC:1596;
   * 
   * @test_Strategy: Delete Query
   *
   */
  public void queryAPITest23() throws Fault {
    Query q;
    int result_size = 0;
    boolean pass = false;

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("Deleting Employee where id 1");
      q = getEntityManager().createQuery("delete from Employee e where e.id=1");

      result_size = q.executeUpdate();
      if (result_size == 1) {
        TestUtil.logTrace("Updated 1 rows");
      }

      doFlush();
      clearCache();
      Employee emp = getEntityManager().find(Employee.class, 1);
      if (emp == null) {
        TestUtil.logTrace("Employee returned expected null");
        pass = true;
      } else {
        TestUtil
            .logErr("Expected null Employee, instead got:" + emp.toString());
      }

      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    }

    if (!pass) {
      throw new Fault("queryAPITest23 failed");
    }
  }

  /*
   * @testName: queryAPITest24
   * 
   * @assertion_ids: PERSISTENCE:SPEC:759; PERSISTENCE:SPEC:787;
   * PERSISTENCE:SPEC:837; PERSISTENCE:SPEC:838; PERSISTENCE:SPEC:839;
   * 
   * @test_Strategy: Bulk Update Query
   *
   */
  public void queryAPITest24() throws Fault {
    Query q;
    int result_size = 0;
    boolean pass = true;

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("Setting department name to IT for ids < 5");
      q = getEntityManager()
          .createQuery("update Department d set d.name='IT' where d.id<5");

      result_size = q.executeUpdate();
      if (result_size == 4) {
        TestUtil.logTrace("Updated 4 rows");
      }

      doFlush();
      clearCache();
      TestUtil.logMsg("Testing ids 1 to 4");

      for (int i = 1; i < 5; i++) {
        Department dept = getEntityManager().find(Department.class, i);
        if (dept != null) {
          if (dept.getId() == i) {
            if (dept.getName().equals("IT")) {
              TestUtil.logTrace("Received expected name:" + dept.getName());
            } else {
              TestUtil.logErr("Received unexpected d.name =" + dept.getName());
              pass = false;
            }
          } else {
            TestUtil.logErr(
                "Received incorrect Department, expected id=1 and name=IT, actual id="
                    + dept.getId() + " and name=" + dept.getName());
          }

        } else {
          TestUtil.logErr("department returned was null");
        }
      }
      TestUtil.logMsg("Testing id 5");
      Department dept = getEntityManager().find(Department.class, 5);
      if (dept != null) {
        if (dept.getId() == 5) {
          if (dept.getName().equals(deptRef[4].getName())) {
            TestUtil.logTrace("Received expected name:" + dept.getName());
          } else {
            TestUtil.logErr("Received unexpected name =" + dept.getName());
            pass = false;
          }
        } else {
          TestUtil.logErr(
              "Received incorrect Department, expected id=1 and name=IT, actual id="
                  + dept.getId() + " and name=" + dept.getName());
        }

      } else {
        TestUtil.logErr("department returned was null");
      }

      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    }

    if (!pass) {
      throw new Fault("queryAPITest24 failed");
    }
  }

  /*
   * @testName: queryAPITest25
   * 
   * @assertion_ids: PERSISTENCE:SPEC:759
   * 
   * @test_Strategy: Bulk Delete Query
   *
   */
  public void queryAPITest25() throws Fault {
    Query q;
    int result_size = 0;
    boolean pass = false;

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("Deleting Employee where id < 21");
      q = getEntityManager()
          .createQuery("delete from Employee e where e.id<21");

      result_size = q.executeUpdate();
      if (result_size == 20) {
        TestUtil.logTrace("Updated 20 rows");
      }

      doFlush();
      clearCache();
      for (int i = 1; i < 21; i++) {
        Employee emp = getEntityManager().find(Employee.class, i);
        if (emp == null) {
          TestUtil.logTrace("Employee " + i + " returned was null");
          pass = true;
        } else {
          TestUtil
              .logErr("Expected null Employee, instead got:" + emp.toString());
        }
      }
      Employee emp = getEntityManager().find(Employee.class, 21);
      if (emp != null) {
        TestUtil.logTrace("Employee 21 returned was expected non-null");
        pass = true;
      } else {
        TestUtil.logErr("Expected non-null Employee for id 21");
      }
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    }

    if (!pass) {
      throw new Fault("queryAPITest25 failed");
    }
  }

  /*
   * @testName: queryAPITest27
   * 
   * @assertion_ids: PERSISTENCE:SPEC:527;
   * 
   * @test_Strategy: Usage of Date literals in Query
   */
  public void queryAPITest27() throws Fault {
    Query q;
    Collection<Date> result;
    int result_size = 0;
    boolean pass1 = false;
    boolean pass2 = true;

    try {
      TestUtil.logTrace("Invoking query");
      getEntityTransaction().begin();
      q = getEntityManager().createQuery(
          "select e.hireDate from Employee e where e.hireDate = '2000-02-14'");

      result = q.getResultList();
      result_size = result.size();
      TestUtil.logTrace("Result Size = " + result_size);

      // There are two employees hired on 2000-02-14
      if (result_size == 2) {
        Date expectedHireDate = Date.valueOf("2000-02-14");
        pass1 = true;
        TestUtil.logTrace("Received expected count 2");
        for (Date d : result) {
          TestUtil.logTrace("date=" + d);
          if (d.equals(expectedHireDate)) {
            TestUtil.logTrace("Received expected HireDate ");
          } else {
            TestUtil.logErr(
                "Received unexpected Employee HireDate = " + d.toString());
            pass2 = false;
          }
        }

      } else {
        TestUtil.logTrace("Received unexpected count " + result);
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
        TestUtil.logErr("Unexpected Exception in while rolling back TX:", re);
      }
    }

    if (!pass1 || !pass2) {
      throw new Fault("queryAPITest27 failed");
    }
  }

  /*
   * @testName: queryAPITest28
   * 
   * @assertion_ids: PERSISTENCE:SPEC:527;
   * 
   * @test_Strategy: Usage of Time literal in Query
   *
   */
  @SetupMethod(name = "setupDataTypes2")
  public void queryAPITest28() throws Fault {

    Collection<Time> result;

    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    boolean pass4 = true;
    java.sql.Time timeValue = getTimeData("10:30:15");
    TestUtil.logTrace("time Value = " + timeValue.toString());

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("FIND D2: " + dateId);
      DataTypes2 dataTypes2 = getEntityManager().find(DataTypes2.class, dateId);

      if (null != dataTypes2) {
        TestUtil.logTrace("DataType Entity is not null, setting TimeData ");
        dataTypes2.setTimeData(timeValue);
        pass1 = true;
      } else {
        TestUtil.logErr("Null returned during initial find");
      }

      getEntityManager().merge(dataTypes2);
      doFlush();
      clearCache();

      TestUtil.logTrace("Make sure update occurred");
      TestUtil.logTrace("FIND D2 again:");
      dataTypes2 = getEntityManager().find(DataTypes2.class, dateId);

      if (null != dataTypes2) {
        if (dataTypes2.getTimeData().equals(timeValue)) {
          TestUtil.logTrace("Update occurred properly:" + dataTypes2);
          pass2 = true;
        } else {
          TestUtil.logErr("Update did not occur properly");
        }
      } else {
        TestUtil.logErr("Find returned null after update");
      }

      TestUtil.logTrace("Retrieving all results first");

      Collection<DataTypes2> cDataTypes2 = getEntityManager()
          .createQuery("select d from DataTypes2 d").getResultList();
      for (DataTypes2 d : cDataTypes2) {
        TestUtil.logTrace("result:" + d.toString());
      }

      TestUtil.logTrace("Check results when testing for Time");
      result = getEntityManager()
          .createQuery(
              "select d.timeData from DataTypes2 d where d.timeData = :time")
          .setParameter("time", timeValue).getResultList();

      int result_size = result.size();
      TestUtil.logTrace("Result Size = " + result_size);

      if (result_size == 1) {
        pass3 = true;
        TestUtil.logTrace("Received expected result size");
        for (Time t : result) {
          TestUtil.logTrace("time=" + t);
          if (t.equals(timeValue)) {
            TestUtil.logTrace("Received expected Time ");
          } else {
            pass4 = false;
            TestUtil.logErr("Received unexpected Time = " + t.toString());
          }
        }

      } else {
        TestUtil.logErr("Expected 1 result, instead got: " + result_size);
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
        TestUtil.logErr("Unexpected Exception during Rollback:", re);
      }
    }

    if (!pass1 || !pass2 || !pass3 || !pass4) {
      throw new Fault("queryAPITest28 failed");
    }
  }

  /*
   * @testName: queryAPITest29
   * 
   * @assertion_ids: PERSISTENCE:SPEC:527;
   * 
   * @test_Strategy: Usage of TimeStamp literal in Query
   *
   */
  @SetupMethod(name = "setupDataTypes2")
  public void queryAPITest29() throws Fault {

    TestUtil.logTrace("Begin queryAPITest29");
    Query q;
    Collection<Timestamp> result;
    int result_size = 0;

    boolean pass1 = false;
    boolean pass2 = true;

    java.sql.Timestamp tsValue = getTimestampData("2006-11-11");
    TestUtil.logTrace("timestamp Value = " + tsValue.toString());

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("FIND D2");

      DataTypes2 dataTypes2 = getEntityManager().find(DataTypes2.class, dateId);

      if (null != dataTypes2) {
        TestUtil.logTrace("DataType Entity is not null, setting TimeData ");
        dataTypes2.setTsData(tsValue);
      }

      getEntityManager().merge(dataTypes2);
      doFlush();
      clearCache();

      TestUtil.logTrace("Check results");
      if ((null != dataTypes2)) {
        // && (dataTypes2.getTimeData().equals(timeValue))

        q = getEntityManager().createQuery(
            "select d.tsData from DataTypes2 d where d.tsData = '2006-11-11 10:10:10'");

        result = q.getResultList();
        result_size = result.size();
        TestUtil.logTrace("Result Size = " + result_size);

        if (result_size == 1) {
          pass1 = true;
          TestUtil.logTrace("Received expected result size");

          for (Timestamp t : result) {
            TestUtil.logTrace("time=" + t);
            if (t.equals(tsValue)) {
              TestUtil.logTrace("Received expected TimeStamp ");
            } else {
              TestUtil
                  .logErr("Received unexpected TimeStamp = " + t.toString());
              pass2 = false;
            }
          }
        } else {
          TestUtil.logErr("Did not get expected results. " + " Expected "
              + tsValue + " , got: " + dataTypes2.getTsData());
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
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception during Rollback:", re);
      }
    }

    if (!pass1 || !pass2) {
      throw new Fault("queryAPITest29 failed");
    }
  }

  /*
   * @testName: getResultListIllegalStateException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:2699;PERSISTENCE:JAVADOC:666
   * 
   * @test_Strategy: Try to execute a delete query
   *
   */
  public void getResultListIllegalStateException() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;

    TestUtil.logMsg("Testing Delete");
    getEntityTransaction().begin();
    Query query = getEntityManager()
        .createQuery("DELETE FROM Employee e where e.id in (1,2,3)");
    try {
      query.getResultList();
      TestUtil.logTrace("Did not throw IllegalStateException");
    } catch (IllegalStateException ise) {
      TestUtil.logTrace("Received expected IllegalStateException");
      pass1 = true;
    } catch (Exception ex) {
      TestUtil.logErr("Received unexpected exception", ex);
    }

    TestUtil.logMsg("Testing Update");

    Query q = getEntityManager()
        .createQuery("Update Employee e SET e.salary=0 where e.id in (1,2,3)");
    try {
      q.getResultList();
      TestUtil.logTrace("Did not throw IllegalStateException");
    } catch (IllegalStateException ise) {
      TestUtil.logTrace("Received expected IllegalStateException");
      pass2 = true;
    } catch (Exception ex) {
      TestUtil.logErr("Received unexpected exception", ex);
    }

    try {
      if (getEntityTransaction().isActive()) {
        getEntityTransaction().rollback();
      }
    } catch (Exception re) {
      TestUtil.logErr("Unexpected Exception while rolling back TX:", re);
    }
    if (!pass1 || !pass2) {
      throw new Fault("getResultListIllegalStateException failed");
    }

  }

  /*
   * @testName: noTransactionLockModeTypeNoneTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1518;PERSISTENCE:SPEC:1519;
   * PERSISTENCE:SPEC:1520; PERSISTENCE:SPEC:1521;
   * 
   * @test_Strategy: execute query/TypedQuery with no transaction and lock mode
   * type set to none and getSingleResult and getResultList should execute
   * successfully.
   */
  public void noTransactionLockModeTypeNoneTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    boolean pass4 = false;
    TestUtil.logMsg("Query getSingleResult test");
    try {
      Query q = getEntityManager()
          .createQuery("select e from Employee e where e.id = 1");
      q.setLockMode(LockModeType.NONE);
      Object o = q.getSingleResult();
      if (o instanceof Employee) {
        Employee e = (Employee) o;
        if (e.getId() == 1) {
          pass1 = true;
          TestUtil.logTrace("Received expected employee id:" + e.getId());
        } else {
          TestUtil.logErr("Expected employee with id:1, actual:" + e.getId());
        }
      } else {
        TestUtil.logErr("Received non Employee object:" + o);
      }

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
    TestUtil.logMsg("TypedQuery getSingleResult test");
    try {
      TypedQuery<Employee> q = getEntityManager().createQuery(
          "select e from Employee e where e.id = 1", Employee.class);
      q.setLockMode(LockModeType.NONE);
      Employee e = q.getSingleResult();
      if (e.getId() == 1) {
        pass2 = true;
        TestUtil.logTrace("Received expected employee id:" + e.getId());
      } else {
        TestUtil.logErr("Expected employee with id:1, actual:" + e.getId());
      }
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
    TestUtil.logMsg("Query getResultList test");
    try {
      Query q = getEntityManager()
          .createQuery("select e from Employee e where e.id = 1");
      q.setLockMode(LockModeType.NONE);
      Collection c = q.getResultList();
      if (c.size() == 1) {
        Object o = c.iterator().next();
        if (o instanceof Employee) {
          Employee e = (Employee) o;
          if (e.getId() == 1) {
            pass3 = true;
            TestUtil.logTrace("Received expected employee id:" + e.getId());
          } else {
            TestUtil.logErr("Expected employee with id:1, actual:" + e.getId());
          }
        } else {
          TestUtil.logErr("Received non Employee object:" + o);
        }
      } else {
        TestUtil.logErr("Got more than one result");
      }
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
    TestUtil.logMsg("TypedQuery getResultList test");
    try {
      TypedQuery<Employee> q = getEntityManager().createQuery(
          "select e from Employee e where e.id = 1", Employee.class);
      q.setLockMode(LockModeType.NONE);
      List<Employee> le = q.getResultList();
      if (le.size() == 1) {
        Employee e = le.get(0);
        if (e.getId() == 1) {
          pass4 = true;
          TestUtil.logTrace("Received expected employee id:" + e.getId());
        } else {
          TestUtil.logErr("Expected employee with id:1, actual:" + e.getId());
        }
      } else {
        TestUtil.logErr("Got more than one result");
      }

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

    if (!pass1 || !pass2 || !pass3 || !pass4) {
      throw new Fault("noTransactionLockModeTypeNoneTest failed");
    }
  }

  private void createTestData() throws Exception {
    TestUtil.logTrace("createTestData");

    final Insurance insRef[] = new Insurance[5];
    final Date d2 = getSQLDate("2001-06-27");
    final Date d3 = getSQLDate("2002-07-07");
    final Date d4 = getSQLDate("2003-03-03");
    final Date d5 = getSQLDate("2004-04-10");
    final Date d6 = getSQLDate("2005-02-18");
    final Date d7 = getSQLDate("2000-09-17");
    final Date d8 = getSQLDate("2001-11-14");
    final Date d9 = getSQLDate("2002-10-04");
    final Date d10 = getSQLDate("2003-01-25");
    final Date d11 = getSQLDate();

    try {

      getEntityTransaction().begin();

      // TestUtil.logTrace("Create 5 Departments");
      deptRef[0] = new Department(1, "Engineering");
      deptRef[1] = new Department(2, "Marketing");
      deptRef[2] = new Department(3, "Sales");
      deptRef[3] = new Department(4, "Accounting");
      deptRef[4] = new Department(5, "Training");

      TestUtil.logTrace("Start to persist departments ");
      for (Department d : deptRef) {
        if (d != null) {
          getEntityManager().persist(d);
          TestUtil.logTrace("persisted department " + d);
        }
      }

      // TestUtil.logTrace("Create 3 Insurance Carriers");
      insRef[0] = new Insurance(1, "Prudential");
      insRef[1] = new Insurance(2, "Cigna");
      insRef[2] = new Insurance(3, "Sentry");

      TestUtil.logTrace("Start to persist insurance ");
      for (Insurance i : insRef) {
        if (i != null) {
          getEntityManager().persist(i);
          TestUtil.logTrace("persisted insurance " + i);
        }
      }

      // TestUtil.logTrace("Create 20 employees");
      empRef[0] = new Employee(1, "Alan", "Frechette", d1, (float) 35000.0);
      empRef[0].setDepartment(deptRef[0]);
      empRef[0].setInsurance(insRef[0]);

      empRef[1] = new Employee(2, "Arthur", "Frechette", d2, (float) 35000.0);
      empRef[1].setDepartment(deptRef[1]);
      empRef[1].setInsurance(insRef[1]);

      empRef[2] = new Employee(3, "Shelly", "McGowan", d3, (float) 50000.0);
      empRef[2].setDepartment(deptRef[2]);
      empRef[2].setInsurance(insRef[2]);

      empRef[3] = new Employee(4, "Robert", "Bissett", d4, (float) 55000.0);
      empRef[3].setDepartment(deptRef[3]);
      empRef[3].setInsurance(insRef[0]);

      empRef[4] = new Employee(5, "Stephen", "DMilla", d5, (float) 25000.0);
      empRef[4].setDepartment(deptRef[4]);
      empRef[4].setInsurance(insRef[1]);

      empRef[5] = new Employee(6, "Karen", "Tegan", d6, (float) 80000.0);
      empRef[5].setDepartment(deptRef[0]);
      empRef[5].setInsurance(insRef[2]);

      empRef[6] = new Employee(7, "Stephen", "Cruise", d7, (float) 90000.0);
      empRef[6].setDepartment(deptRef[1]);
      empRef[6].setInsurance(insRef[0]);

      empRef[7] = new Employee(8, "Irene", "Caruso", d8, (float) 20000.0);
      empRef[7].setDepartment(deptRef[2]);
      empRef[7].setInsurance(insRef[1]);

      empRef[8] = new Employee(9, "William", "Keaton", d9, (float) 35000.0);
      empRef[8].setDepartment(deptRef[3]);
      empRef[8].setInsurance(insRef[2]);

      empRef[9] = new Employee(10, "Kate", "Hudson", d10, (float) 20000.0);
      empRef[9].setDepartment(deptRef[4]);
      empRef[9].setInsurance(insRef[0]);

      empRef[10] = new Employee(11, "Jonathan", "Smith", d10, 40000.0F);
      empRef[10].setDepartment(deptRef[0]);
      empRef[10].setInsurance(insRef[1]);

      empRef[11] = new Employee(12, "Mary", "Macy", d9, 40000.0F);
      empRef[11].setDepartment(deptRef[1]);
      empRef[11].setInsurance(insRef[2]);

      empRef[12] = new Employee(13, "Cheng", "Fang", d8, 40000.0F);
      empRef[12].setDepartment(deptRef[2]);
      empRef[12].setInsurance(insRef[0]);

      empRef[13] = new Employee(14, "Julie", "OClaire", d7, 60000.0F);
      empRef[13].setDepartment(deptRef[3]);
      empRef[13].setInsurance(insRef[1]);

      empRef[14] = new Employee(15, "Steven", "Rich", d6, 60000.0F);
      empRef[14].setDepartment(deptRef[4]);
      empRef[14].setInsurance(insRef[2]);

      empRef[15] = new Employee(16, "Kellie", "Lee", d5, 60000.0F);
      empRef[15].setDepartment(deptRef[0]);
      empRef[15].setInsurance(insRef[0]);

      empRef[16] = new Employee(17, "Nicole", "Martin", d4, 60000.0F);
      empRef[16].setDepartment(deptRef[1]);
      empRef[16].setInsurance(insRef[1]);

      empRef[17] = new Employee(18, "Mark", "Francis", d3, 60000.0F);
      empRef[17].setDepartment(deptRef[2]);
      empRef[17].setInsurance(insRef[2]);

      empRef[18] = new Employee(19, "Will", "Forrest", d2, 60000.0F);
      empRef[18].setDepartment(deptRef[3]);
      empRef[18].setInsurance(insRef[0]);

      empRef[19] = new Employee(20, "Katy", "Hughes", d1, 60000.0F);
      empRef[19].setDepartment(deptRef[4]);
      empRef[19].setInsurance(insRef[1]);

      empRef[20] = new Employee(21, "Jane", "Smmith", d11, 60000.0F);
      empRef[20].setDepartment(deptRef[0]);
      empRef[20].setInsurance(insRef[2]);

      // TestUtil.logTrace("Start to persist employees ");
      for (Employee e : empRef) {
        if (e != null) {
          getEntityManager().persist(e);
          TestUtil.logTrace("persisted employee " + e);
        }
      }

      getEntityTransaction().commit();
      TestUtil.logTrace("Created TestData");

    } catch (Exception re) {
      TestUtil.logErr("Unexpected Exception in createTestData:", re);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr(
            "Unexpected Exception in createTestData while rolling back TX:",
            re);
      }
    }

  }

  private void createDataTypes2Data() throws Exception {
    TestUtil.logTrace("createDataTypes2Data");
    try {

      getEntityTransaction().begin();

      DataTypes2 dT2 = new DataTypes2(dateId);
      dT2.setDateData(dateId);
      dT2.setTimeData(getTimeData("01:01:01"));
      getEntityManager().persist(dT2);

      java.util.Date d = getUtilDate("2010-02-11");
      dT2 = new DataTypes2(d);
      dT2.setDateData(d);
      dT2.setTimeData(getTimeData("02:02:02"));
      getEntityManager().persist(dT2);

      d = getUtilDate("2011-03-12");
      dT2 = new DataTypes2(d);
      dT2.setDateData(d);
      dT2.setTimeData(getTimeData("03:03:03"));
      getEntityManager().persist(dT2);

      d = getUtilDate("2012-04-01");
      dT2 = new DataTypes2(d);
      dT2.setDateData(d);
      dT2.setTimeData(getTimeData("04:04:04"));
      getEntityManager().persist(dT2);

      getEntityTransaction().commit();
      TestUtil.logTrace("Created TestData");

    } catch (Exception re) {
      TestUtil.logErr("Unexpected Exception in createTestData:", re);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr(
            "Unexpected Exception in createTestData while rolling back TX:",
            re);
      }
    }
  }

  private static void logErrorEmp(Collection c) {
    for (Object o : c) {
      Employee e = (Employee) o;
      TestUtil.logErr("id=" + e.getId() + ", " + "first=" + e.getFirstName()
          + ", last=" + e.getLastName() + ", hireDate=" + e.getHireDate());
    }
  }

  private static void logTraceEmp(Collection c) {
    for (Object o : c) {
      Employee e = (Employee) o;
      TestUtil.logTrace("id=" + e.getId() + ", " + "first=" + e.getFirstName()
          + ", last=" + e.getLastName() + ", hireDate=" + e.getHireDate());
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
      getEntityManager().createNativeQuery("DELETE FROM INSURANCE")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM DATATYPES2")
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
