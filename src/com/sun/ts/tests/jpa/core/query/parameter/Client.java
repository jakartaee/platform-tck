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

package com.sun.ts.tests.jpa.core.query.parameter;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.CleanupMethod;
import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import javax.persistence.*;
import java.util.*;

public class Client extends PMClientBase {
  protected final Employee empRef[] = new Employee[5];

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
      getEntityManager();
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  public void setupEmployee(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setupEmployee");
    try {
      super.setup(args, p);
      createEmployeeData();
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  /*
   * @testName: parameterTest1
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:381; PERSISTENCE:JAVADOC:382;
   * PERSISTENCE:JAVADOC:383; PERSISTENCE:JAVADOC:322; PERSISTENCE:JAVADOC:404;
   * PERSISTENCE:JAVADOC:412; PERSISTENCE:SPEC:1634;
   * 
   * @test_Strategy: Create a query with 2 named parameters and retrieve
   * information about the parameters.
   *
   */
  public void parameterTest1() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    boolean pass4 = false;
    boolean pass5 = false;
    boolean pass6 = false;
    boolean pass7 = false;
    boolean pass8 = false;

    Query query = getEntityManager().createQuery(
        "SELECT e FROM Employee e WHERE e.firstName = :first or e.lastName = :last");

    if (TestUtil.traceflag) {
      Set<Parameter<?>> sParameters = query.getParameters();
      for (Parameter p : sParameters) {
        TestUtil.logTrace("Parameter name = " + p.getName());
        TestUtil.logTrace("Parameter position = " + p.getPosition());
        TestUtil.logTrace("Parameter type =" + p.getParameterType());
      }
    }

    String sExpected = "first";
    Parameter p1 = query.getParameter(sExpected);
    if (query.isBound(p1)) {
      TestUtil
          .logErr("isBound believes there is a value bound to the parameter:"
              + sExpected);
    } else {
      pass1 = true;
    }

    String sActual = p1.getName();
    if (!sActual.equals(sExpected)) {
      TestUtil.logErr(
          "p1.getName() - Expected: " + sExpected + ", actual:" + sActual);
    } else {
      pass2 = true;
    }

    sExpected = null;
    Integer iActual = p1.getPosition();
    if (iActual != null) {
      TestUtil.logErr(
          "p1.getPosition() - Expected: " + sExpected + ", actual:" + iActual);
    } else {
      pass3 = true;
    }

    try {
      sExpected = "java.lang.String";
      sActual = p1.getParameterType().getName();
      if (!sActual.equals(sExpected)) {
        TestUtil.logErr("p1.getParameterType() - Expected: " + sExpected
            + ", actual:" + sActual);
      } else {
        pass4 = true;
      }
    } catch (IllegalStateException ise) {
      TestUtil.logMsg(
          "warning: p1.getParameterType() threw IllegalStateException, this is not considered a failure");
    }

    sExpected = "last";
    Parameter p2 = query.getParameter(sExpected);
    if (query.isBound(p2)) {
      TestUtil
          .logErr("isBound believes there is a value bound to the parameter:"
              + sExpected);
    } else {
      pass5 = true;
    }

    sActual = p2.getName();
    if (!sActual.equals(sExpected)) {
      TestUtil.logErr(
          "p2.getName() - Expected: " + sExpected + ", actual:" + sActual);
    } else {
      pass6 = true;
    }
    sExpected = null;
    iActual = p2.getPosition();
    if (iActual != null) {
      TestUtil.logErr(
          "p2.getPosition() - Expected: " + sExpected + ", actual:" + iActual);
    } else {
      pass7 = true;
    }

    try {
      sExpected = "java.lang.String";
      sActual = p2.getParameterType().getName();
      if (!sActual.equals(sExpected)) {
        TestUtil.logErr("p2.getParameterType() - Expected: " + sExpected
            + ", actual:" + sActual);
      } else {
        pass8 = true;
      }
    } catch (IllegalStateException ise) {
      TestUtil.logMsg(
          "warning: p2.getParameterType() threw IllegalStateException, this is not considered a failure");
    }

    if (!pass1 || !pass2 || !pass3 || !pass4 || !pass5 || !pass6 || !pass7
        || !pass8) {
      throw new Fault("parameterTest1 failed");
    }
  }

  /*
   * @testName: parameterTestTQ1
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:642; PERSISTENCE:JAVADOC:681
   * 
   * @test_Strategy: Create a TypedQuery with 2 named parameters and retrieve
   * information about the parameters.
   *
   */
  public void parameterTestTQ1() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    boolean pass4 = false;
    boolean pass5 = false;
    boolean pass6 = false;
    boolean pass7 = false;
    boolean pass8 = false;

    TypedQuery<Employee> query = getEntityManager().createQuery(
        "SELECT e FROM Employee e WHERE e.firstName = :first or e.lastName = :last",
        Employee.class);

    if (TestUtil.traceflag) {
      Set<Parameter<?>> sParameters = query.getParameters();
      for (Parameter p : sParameters) {/**/
        TestUtil.logTrace("Parameter name = " + p.getName());
        TestUtil.logTrace("Parameter position = " + p.getPosition());
        TestUtil.logTrace("Parameter type =" + p.getParameterType());
      }
    }

    String sExpected = "first";
    Parameter p1 = query.getParameter(sExpected);
    if (query.isBound(p1)) {
      TestUtil
          .logErr("isBound believes there is a value bound to the parameter:"
              + sExpected);
    } else {
      pass1 = true;
    }

    String sActual = p1.getName();
    if (!sActual.equals(sExpected)) {
      TestUtil.logErr(
          "p1.getName() - Expected: " + sExpected + ", actual:" + sActual);
    } else {
      pass2 = true;
    }

    sExpected = null;
    Integer iActual = p1.getPosition();
    if (iActual != null) {
      TestUtil.logErr(
          "p1.getPosition() - Expected: " + sExpected + ", actual:" + iActual);
    } else {
      pass3 = true;
    }

    try {
      sExpected = "java.lang.String";
      sActual = p1.getParameterType().getName();
      if (!sActual.equals(sExpected)) {
        TestUtil.logErr("p1.getParameterType() - Expected: " + sExpected
            + ", actual:" + sActual);
      } else {
        pass4 = true;
      }
    } catch (IllegalStateException ise) {
      TestUtil.logMsg(
          "warning: p1.getParameterType() threw IllegalStateException, this is not considered a failure");
    }

    sExpected = "last";
    Parameter p2 = query.getParameter(sExpected);
    if (query.isBound(p2)) {
      TestUtil
          .logErr("isBound believes there is a value bound to the parameter:"
              + sExpected);
    } else {
      pass5 = true;
    }

    sActual = p2.getName();
    if (!sActual.equals(sExpected)) {
      TestUtil.logErr(
          "p2.getName() - Expected: " + sExpected + ", actual:" + sActual);
    } else {
      pass6 = true;
    }
    sExpected = null;
    iActual = p2.getPosition();
    if (iActual != null) {
      TestUtil.logErr(
          "p2.getPosition() - Expected: " + sExpected + ", actual:" + iActual);
    } else {
      pass7 = true;
    }

    try {
      sExpected = "java.lang.String";
      sActual = p2.getParameterType().getName();
      if (!sActual.equals(sExpected)) {
        TestUtil.logErr("p2.getParameterType() - Expected: " + sExpected
            + ", actual:" + sActual);
      } else {
        pass8 = true;
      }
    } catch (IllegalStateException ise) {
      TestUtil.logMsg(
          "warning: p2.getParameterType() threw IllegalStateException, this is not considered a failure");
    }

    if (!pass1 || !pass2 || !pass3 || !pass4 || !pass5 || !pass6 || !pass7
        || !pass8) {
      throw new Fault("parameterTestTQ1 failed");
    }
  }

  /*
   * @testName: parameterTest2
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:381; PERSISTENCE:JAVADOC:382;
   * PERSISTENCE:JAVADOC:383; PERSISTENCE:JAVADOC:406; PERSISTENCE:JAVADOC:412;
   * PERSISTENCE:SPEC:1634; PERSISTENCE:SPEC:1636;
   * 
   * @test_Strategy: Create a query with 2 positional parameters and retrieve
   * information about the parameters.
   */
  public void parameterTest2() throws Fault {
    int pass1 = 0;
    int pass2 = 0;
    int pass3 = 0;

    Query query = getEntityManager().createQuery(
        "SELECT e FROM Employee e WHERE e.firstName = ?1 or e.lastName = ?2");

    Set<Parameter<?>> sParameters = query.getParameters();
    if (sParameters.size() == 2) {
      for (Parameter p : sParameters) {
        if (TestUtil.traceflag) {
          TestUtil.logTrace("parameter name = " + p.getName());
          TestUtil.logTrace("parameter position = " + p.getPosition());
          TestUtil.logTrace("parameter type =" + p.getParameterType());
        }
        if (query.isBound(p)) {
          TestUtil.logErr(
              "isBound believes there is a value bound to the parameter:" + p);
        } else {
          TestUtil.logTrace("query isBound = " + query.isBound(p));
          pass1++;

        }
        Integer pos = p.getPosition();
        if (pos != null) {
          if (pos == 1 || pos == 2) {
            String sActual = p.getName();
            if (sActual != null) {
              TestUtil.logErr("getName() - Expected: null, actual:" + sActual);
            } else {
              pass2++;
            }
            try {
              String sExpected = "java.lang.String";
              sActual = p.getParameterType().getName();
              if (!sActual.equals(sExpected)) {
                TestUtil.logErr("getParameterType().getName() - Expected: "
                    + sExpected + ", actual:" + sActual);
              } else {
                pass3++;
              }
            } catch (IllegalStateException ise) {
              TestUtil.logMsg(
                  "warning: getParameterType().getName() threw IllegalStateException, this is not considered a failure");
            }
          } else {
            TestUtil
                .logErr("getPosition() returned an invalid position:" + pos);
          }
        } else {
          TestUtil.logErr("getPosition() returned null");
        }

      }
    } else {
      TestUtil.logErr("query.getParameters(): Expected: 2 parameters, actual: "
          + sParameters.size());
    }

    if (pass1 != 2 || pass2 != 2 || pass3 != 2) {
      throw new Fault("parameterTest2 failed");
    }
  }

  /*
   * @testName: parameterTQTest2
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:648;
   * 
   * @test_Strategy: Create a TypedQuery with 2 positional parameters and
   * retrieve information about the parameters.
   */
  public void parameterTQTest2() throws Fault {
    int pass1 = 0;
    int pass2 = 0;
    int pass3 = 0;

    TypedQuery<Employee> query = getEntityManager().createQuery(
        "SELECT e FROM Employee e WHERE e.firstName = ?1 or e.lastName = ?2",
        Employee.class);
    Set<Parameter<?>> sParameters = query.getParameters();
    if (sParameters.size() == 2) {
      for (Parameter p : sParameters) {
        if (TestUtil.traceflag) {
          TestUtil.logTrace("parameter name = " + p.getName());
          TestUtil.logTrace("parameter position = " + p.getPosition());
          TestUtil.logTrace("parameter type =" + p.getParameterType());
        }
        if (query.isBound(p)) {
          TestUtil.logErr(
              "isBound believes there is a value bound to the parameter:" + p);
        } else {
          TestUtil.logTrace("query isBound = " + query.isBound(p));
          pass1++;
        }
        Integer pos = p.getPosition();
        if (pos != null) {
          if (pos == 1 || pos == 2) {
            String sActual = p.getName();
            if (sActual != null) {
              TestUtil.logErr("getName() - Expected: null, actual:" + sActual);
            } else {
              pass2++;
            }
            try {
              String sExpected = "java.lang.String";
              sActual = p.getParameterType().getName();
              if (!sActual.equals(sExpected)) {
                TestUtil.logErr("getParameterType().getName() - Expected: "
                    + sExpected + ", actual:" + sActual);
              } else {
                pass3++;
              }
            } catch (IllegalStateException ise) {
              TestUtil.logMsg(
                  "warning: getParameterType().getName() threw IllegalStateException, this is not considered a failure");
            }
          } else {
            TestUtil
                .logErr("getPosition() returned an invalid position:" + pos);
          }
        } else {
          TestUtil.logErr("getPosition() returned null");
        }

      }
    } else {
      TestUtil.logErr("query.getParameters(): Expected: 2 parameters, actual: "
          + sParameters.size());
    }
    if (pass1 != 2 || pass2 != 2 || pass3 != 2) {
      throw new Fault("parameterTQTest2 failed");
    }
  }

  /*
   * @testName: parameterTest4
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:381; PERSISTENCE:JAVADOC:383;
   * 
   * @test_Strategy: Create a query with a named parameter that is a date and
   * and retrieve information about the parameter.
   */
  public void parameterTest4() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;

    Query query = getEntityManager()
        .createQuery("SELECT e FROM Employee e WHERE e.hireDate = :hDate ");

    if (TestUtil.traceflag) {
      Set<Parameter<?>> sParameters = query.getParameters();
      for (Parameter p : sParameters) {
        TestUtil.logTrace("Parameter name = " + p.getName());
        TestUtil.logTrace("Parameter position = " + p.getPosition());
      }
    }

    String sExpected = "hDate";
    Parameter p1 = query.getParameter(sExpected);
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
      TestUtil.logErr(
          "p1.getPosition() - Expected: " + sExpected + ", actual:" + iActual);
    } else {
      pass2 = true;
    }

    if (!pass1 || !pass2) {
      throw new Fault("parameterTest4 failed");
    }
  }

  /*
   * @testName: parameterTest5
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:381; PERSISTENCE:JAVADOC:383;
   * 
   * @test_Strategy: Create a query with a named parameter that is a float and
   * retrieve information about the parameter.
   *
   */
  public void parameterTest5() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;

    Query query = getEntityManager()
        .createQuery("SELECT e FROM Employee e WHERE e.salary = :salary ");

    if (TestUtil.traceflag) {
      Set<Parameter<?>> sParameters = query.getParameters();
      for (Parameter p : sParameters) {
        TestUtil.logTrace("Parameter name = " + p.getName());
        TestUtil.logTrace("Parameter position = " + p.getPosition());
      }
    }

    String sExpected = "salary";
    Parameter p1 = query.getParameter(sExpected);
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
      TestUtil.logErr(
          "p1.getPosition() - Expected: " + sExpected + ", actual:" + iActual);
    } else {
      pass2 = true;
    }

    if (!pass1 || !pass2) {
      throw new Fault("parameterTest5 failed");
    }
  }

  /*
   * @testName: getParametersTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:408;
   * 
   * @test_Strategy: Create a query with 2 named parameters and retrieve all
   * parameters.
   *
   */
  public void getParametersTest() throws Fault {
    boolean pass = false;
    TestUtil.logTrace("Starting getParametersTest");

    try {
      Query query = getEntityManager().createQuery(
          "SELECT e FROM Employee e WHERE e.firstName = :first or e.lastName = :last");

      if (TestUtil.traceflag) {
        Set<Parameter<?>> sParameters = query.getParameters();
        for (Parameter p : sParameters) {
          TestUtil.logTrace("Parameter name = " + p.getName());
          TestUtil.logTrace("Parameter position = " + p.getPosition());
          TestUtil.logTrace("Parameter type =" + p.getParameterType());
        }
      }

      boolean foundFirstName = false;
      boolean foundLastName = false;
      int found = 0;
      List<Object> list = new ArrayList<Object>(query.getParameters());
      for (int i = 0; i < list.size(); i++) {
        Parameter p = (Parameter) list.get(i);
        TestUtil.logTrace("Parameter name = " + p.getName());
        if (p.getName().equals("first")) {
          foundFirstName = true;
          found++;
        }
        if (p.getName().equals("last")) {
          foundLastName = true;
          found++;
        }
      }
      if (found == 2 && foundFirstName && foundLastName) {
        pass = true;
      } else {
        if (found != 2) {
          TestUtil
              .logErr("Wrong number of parameters returned, expected:2, actual:"
                  + found);
        }
        if (!foundFirstName) {
          TestUtil.logErr("Parameter 'first' was not returned");
        }
        if (!foundLastName) {
          TestUtil.logErr("Parameter 'last' was not returned");
        }
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    if (!pass) {
      throw new Fault("getParametersTest failed");
    }
  }

  /*
   * @testName: getParametersTQTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:654
   * 
   * @test_Strategy: Create a typedQuery with 2 named parameters and retrieve
   * all parameters.
   *
   */
  public void getParametersTQTest() throws Fault {
    boolean pass = false;
    TestUtil.logTrace("Starting getParametersTest");

    try {
      TypedQuery<Employee> query = getEntityManager().createQuery(
          "SELECT e FROM Employee e WHERE e.firstName = :first or e.lastName = :last",
          Employee.class);

      if (TestUtil.traceflag) {
        Set<Parameter<?>> sParameters = query.getParameters();
        for (Parameter p : sParameters) {
          TestUtil.logTrace("Parameter name = " + p.getName());
          TestUtil.logTrace("Parameter position = " + p.getPosition());
          TestUtil.logTrace("Parameter type =" + p.getParameterType());
        }
      }

      boolean foundFirstName = false;
      boolean foundLastName = false;
      int found = 0;
      List<Object> list = new ArrayList<Object>(query.getParameters());
      for (int i = 0; i < list.size(); i++) {
        Parameter p = (Parameter) list.get(i);
        TestUtil.logTrace("Parameter name = " + p.getName());
        if (p.getName().equals("first")) {
          foundFirstName = true;
          found++;
        }
        if (p.getName().equals("last")) {
          foundLastName = true;
          found++;
        }
      }
      if (found == 2 && foundFirstName && foundLastName) {
        pass = true;
      } else {
        if (found != 2) {
          TestUtil
              .logErr("Wrong number of parameters returned, expected:2, actual:"
                  + found);
        }
        if (!foundFirstName) {
          TestUtil.logErr("Parameter 'first' was not returned");
        }
        if (!foundLastName) {
          TestUtil.logErr("Parameter 'last' was not returned");
        }
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    if (!pass) {
      throw new Fault("getParametersTQTest failed");
    }
  }

  /*
   * @testName: getParametersNoParametersTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:408;
   * 
   * @test_Strategy: Create a query with no parameters
   */
  public void getParametersNoParametersTest() throws Fault {
    boolean pass = false;
    try {
      TestUtil.logTrace("Starting getParametersTest");
      Query query = getEntityManager().createQuery("SELECT e FROM Employee e");

      if (TestUtil.traceflag) {
        Set<Parameter<?>> sParameters = query.getParameters();
        for (Parameter p : sParameters) {
          TestUtil.logTrace("parameter name = " + p.getName());
          TestUtil.logTrace("parameter position = " + p.getPosition());
          TestUtil.logTrace("pParameter type =" + p.getParameterType());
        }
      }

      if (query.getParameters().size() != 0) {
        List<Object> list = new ArrayList<Object>(query.getParameters());
        for (int i = 0; i < list.size(); i++) {
          Parameter p = (Parameter) list.get(i);
          TestUtil.logErr("parameter name = " + p.getName());
        }
      } else {
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass) {
      throw new Fault("getParametersNoParametersTest failed");
    }

  }

  /*
   * @testName: parameterPositionalTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1636; PERSISTENCE:SPEC:1638;
   * PERSISTENCE:SPEC:1640;
   * 
   * @test_Strategy: Create a query with a having clause with a positional
   * parameter and retrieve information about the parameter.
   */
  @SetupMethod(name = "setupEmployee")
  @CleanupMethod(name = "cleanupEmployee")
  public void parameterPositionalTest() throws Fault {
    boolean pass = false;
    List result;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "SELECT e.id FROM Employee e WHERE e.id > ?2 GROUP BY e.id HAVING e.id <=?1")
          .setParameter(2, 2).setParameter(1, 4).getResultList();

      expectedPKs = new String[2];
      expectedPKs[0] = "3";
      expectedPKs[1] = "4";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("Expected 1 result, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception:", e);
    }
    if (!pass)
      throw new Fault("parameterPositionalTest failed");
  }

  /*
   * @testName: parameterUpdateTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1636; PERSISTENCE:SPEC:1638;
   * PERSISTENCE:SPEC:1639;
   * 
   * @test_Strategy: Create an update query with a positional parameter and
   * retrieve information about the parameter.
   */
  @SetupMethod(name = "setupEmployee")
  @CleanupMethod(name = "cleanupEmployee")
  public void parameterUpdateTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      Query q = getEntityManager().createQuery(
          "UPDATE Employee e SET e.firstName=?1, e.lastName=?1 WHERE e.id=1")
          .setParameter(1, "foo");

      int result_size = q.executeUpdate();
      if (result_size == 1) {
        TestUtil.logTrace("Updated 1 rows");
      }

      doFlush();
      clearCache();
      Employee emp = getEntityManager().find(Employee.class, 1);

      if (emp.getFirstName().equals("foo") && emp.getLastName().equals("foo")) {
        TestUtil.logTrace("Received expected result:" + emp.toString());
        pass = true;
      } else {
        TestUtil
            .logErr("Expected: firstName=foo, lastName=foo, actual: firstName="
                + emp.getFirstName() + ", lastName=" + emp.getLastName());
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception:", e);
    }
    if (!pass)
      throw new Fault("parameterUpdateTest failed");
  }

  /*
   * @testName: parameterCaseTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1641;
   * 
   * @test_Strategy: Create a query with a name parameter using different cases
   * and retrieve information about the parameter.
   */
  @SetupMethod(name = "setupEmployee")
  @CleanupMethod(name = "cleanupEmployee")
  public void parameterCaseTest() throws Fault {
    boolean pass = false;
    List result;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "SELECT e.id FROM Employee e WHERE e.id > :Id GROUP BY e.id HAVING e.id <=:iD")
          .setParameter("Id", 2).setParameter("iD", 4).getResultList();

      expectedPKs = new String[2];
      expectedPKs[0] = "3";
      expectedPKs[1] = "4";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("Expected 1 result, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception:", e);
    }
    if (!pass)
      throw new Fault("parameterCaseTest failed");
  }

  /*
   * @testName: parameterNamedParameterTwiceTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1642;
   * 
   * @test_Strategy: Create a query using a name parameter twice and retrieve
   * information about the parameter.
   */
  @SetupMethod(name = "setupEmployee")
  @CleanupMethod(name = "cleanupEmployee")
  public void parameterNamedParameterTwiceTest() throws Fault {
    boolean pass = false;
    List result;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      Query q = getEntityManager().createQuery(
          "SELECT e FROM Employee e WHERE e.id >=:ID AND e.id <=:ID");
      result = q.setParameter("ID", 2).getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "2";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("Expected 1 result, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception:", e);
    }
    if (!pass)
      throw new Fault("parameterNamedParameterTwiceTest failed");
  }

  public void createEmployeeData() throws Exception {
    TestUtil.logTrace("createDepartmentEmployeeData");
    getEntityTransaction().begin();

    try {
      empRef[0] = new Employee(1, "Alan", "Frechette");
      empRef[1] = new Employee(2, "Arthur", "Frechette");
      empRef[2] = new Employee(3, "Shelly", "McGowan");
      empRef[3] = new Employee(4, "Robert", "Bissett");
      empRef[4] = new Employee(5, "Stephen", "DMilla");
      TestUtil.logTrace("Start to persist employees ");
      for (Employee emp : empRef) {
        if (emp != null) {
          getEntityManager().persist(emp);
          TestUtil.logTrace("persisted employee " + emp.getId());
        }
      }
      doFlush();
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("createDepartmentEmployeeData failed:", e);
    }
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("calling super.cleanup");
    super.cleanup();
  }

  public void cleanupEmployee() throws Fault {
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
