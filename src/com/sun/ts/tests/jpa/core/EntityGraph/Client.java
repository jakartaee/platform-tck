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

package com.sun.ts.tests.jpa.core.EntityGraph;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.CleanupMethod;
import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import javax.persistence.*;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;
import java.util.*;

public class Client extends PMClientBase {

  Employee3[] empRef = new Employee3[5];

  Department[] deptRef = new Department[2];

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
      displayMap(p);
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  public void setupEmployeeData(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setupOrderData");
    try {
      super.setup(args, p);
      removeTestData();
      createEmployeeData();
      displayMap(p);
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("cleanup complete, calling super.cleanup");
    super.cleanup();
  }

  public void cleanupEmployeeData() throws Fault {
    TestUtil.logTrace("Cleanup data");
    removeTestData();
    cleanup();
  }

  /*
   * @testName: addAttributeNodesStringArrayTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:3360; PERSISTENCE:JAVADOC:3362;
   * PERSISTENCE:JAVADOC:3398; PERSISTENCE:JAVADOC:3402;
   * 
   * @test_Strategy: Create attribute Nodes and retrieve them
   */
  public void addAttributeNodesStringArrayTest() throws Fault {
    boolean pass = false;
    List<String> expected = new ArrayList<String>();
    expected.add("firstName");
    expected.add("lastName");

    EntityGraph<Employee> empEG = getEntityManager()
        .createEntityGraph(Employee.class);
    empEG.addAttributeNodes(expected.toArray(new String[] {}));

    List<AttributeNode<?>> result = empEG.getAttributeNodes();
    List<String> actual = new ArrayList<String>();
    for (AttributeNode an : result) {
      actual.add(an.getAttributeName());
    }

    if (actual.containsAll(expected) && expected.containsAll(actual)
        && actual.size() == expected.size()) {
      TestUtil.logTrace("Received expected results");
      pass = true;
    } else {
      TestUtil.logErr("Expected results");
      for (String s : expected) {
        TestUtil.logErr("expected:" + s);
      }
      TestUtil.logErr("Actual results");
      for (String s : actual) {
        TestUtil.logErr("actual:" + s);
      }
    }

    if (!pass) {
      throw new Fault("addAttributeNodesStringArrayTest failed");
    }
  }

  /*
   * @testName: addAttributeNodesStringArrayIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:3363;
   * 
   * @test_Strategy: Create attribute Node that does not exist
   */
  public void addAttributeNodesStringArrayIllegalArgumentExceptionTest()
      throws Fault {
    boolean pass = false;

    EntityGraph<Employee2> empEG = getEntityManager()
        .createEntityGraph(Employee2.class);
    try {
      empEG.addAttributeNodes("doesnotexist");
      TestUtil.logErr("Did not throw IllegalArgumentException");
    } catch (IllegalArgumentException iae) {
      pass = true;
      TestUtil.logTrace("Received expected IllegalArgumentException");
    }
    if (!pass) {
      throw new Fault(
          "addAttributeNodesStringArrayIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: addAttributeNodesAttributeArrayTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:3365; PERSISTENCE:JAVADOC:3398;
   * 
   * @test_Strategy: Create attribute Nodes via Attribute[] and retrieve them
   */
  public void addAttributeNodesAttributeArrayTest() throws Fault {
    boolean pass = false;

    Metamodel mm = getEntityManager().getMetamodel();

    ManagedType<Employee> mTypeOrder = mm.managedType(Employee.class);

    List<String> sExpected = new ArrayList<String>();
    sExpected.add("firstName");
    sExpected.add("lastName");

    List<Attribute> expected = new ArrayList<Attribute>();
    for (String s : sExpected) {
      expected.add(mTypeOrder.getDeclaredAttribute(s));
    }

    EntityGraph<Employee> empEG = getEntityManager()
        .createEntityGraph(Employee.class);
    empEG.addAttributeNodes(expected.toArray(new Attribute[] {}));

    List<AttributeNode<?>> result = empEG.getAttributeNodes();
    List<String> actual = new ArrayList<String>();
    for (AttributeNode an : result) {
      actual.add(an.getAttributeName());
    }

    if (actual.containsAll(sExpected) && sExpected.containsAll(actual)
        && actual.size() == sExpected.size()) {
      TestUtil.logTrace("Received expected results");
      pass = true;
    } else {
      TestUtil.logErr("Expected results");
      for (String s : sExpected) {
        TestUtil.logErr("expected:" + s);
      }
      TestUtil.logErr("Actual results");
      for (String s : actual) {
        TestUtil.logErr("actual:" + s);
      }
    }

    if (!pass) {
      throw new Fault("addAttributeNodesAttributeArrayTest failed");
    }
  }

  /*
   * @testName: createEntityGraphStringTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:3403
   * 
   * @test_Strategy: Use createEntityGraph to create the named entity graphs in
   * the Employee entity
   */
  public void createEntityGraphStringTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;

    TestUtil.logMsg("Test that the named entity graph is returned");
    EntityGraph eg = getEntityManager().createEntityGraph("first_last_graph");
    if (eg != null) {
      TestUtil.logTrace("Received non-null EntityGraph:" + eg.getName());
      pass1 = true;
    } else {
      TestUtil.logErr("Null was returned for EntityGraph that does exist");
    }

    TestUtil.logMsg("Test that null is returned");
    try {
      eg = getEntityManager().createEntityGraph("doesnotexist");

      if (eg == null) {
        TestUtil.logTrace("Received expected null EntityGraph");
        pass2 = true;
      } else {
        TestUtil.logErr("Non-null EntityGraph was returned instead of a null:"
            + eg.getName());
      }
    } catch (Exception ex) {
      TestUtil.logErr("Received unexpected exception", ex);
    }

    if (!pass1 || !pass2) {
      throw new Fault("createEntityGraphStringTest failed");
    }
  }

  /*
   * @testName: getEntityGraphStringTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:3404
   * 
   * @test_Strategy: Use getEntityGraph to get the named entity graphs in the
   * Employee entity
   */
  public void getEntityGraphStringTest() throws Fault {
    boolean pass = false;

    EntityGraph eg = getEntityManager().getEntityGraph("first_last_graph");
    if (eg != null) {
      TestUtil.logTrace("Received non-null EntityGraph:" + eg.getName());
      pass = true;
    } else {
      TestUtil.logErr("Null was returned for EntityGraph that does exist");
    }

    if (!pass) {
      throw new Fault("getEntityGraphStringTest failed");
    }
  }

  /*
   * @testName: entityGraphGetNameTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:3399
   * 
   * @test_Strategy: Use getName to get the name of the named entity graph in
   * the Employee entity
   */
  public void entityGraphGetNameTest() throws Fault {
    boolean pass = false;

    String expected = "first_last_graph";
    EntityGraph eg = getEntityManager().getEntityGraph(expected);
    if (eg != null) {
      if (eg.getName().equals(expected)) {
        TestUtil.logTrace("Received expected EntityGraph name:" + eg.getName());
        pass = true;
      } else {
        TestUtil
            .logErr("Expected name: " + expected + ", actual:" + eg.getName());
      }
    } else {
      TestUtil.logErr("Null was returned for EntityGraph that does exist");
    }

    if (!pass) {
      throw new Fault("entityGraphGetNameTest failed");
    }
  }

  /*
   * @testName: entityGraphGetNameNoNameExistsTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:2458;
   * 
   * @test_Strategy: Use getName to get the name of the named entity graph in
   * the Employee2 entity that has no name
   */
  public void entityGraphGetNameNoNameExistsTest() throws Fault {
    boolean pass = false;

    List<EntityGraph<? super Employee2>> egs = getEntityManager()
        .getEntityGraphs(Employee2.class);
    if (egs.size() == 1) {
      EntityGraph e = egs.get(0);
      if (e.getName().equals("Employee2")) {
        TestUtil.logTrace("Received expected name:" + e.getName());
        pass = true;
      } else {
        TestUtil.logErr("Expected name: Employee2, actual:" + e.getName());
      }
    } else {
      TestUtil
          .logErr("Expected 1 graph to be returned, instead got:" + egs.size());
    }

    if (!pass) {
      throw new Fault("entityGraphGetNameNoNameExistsTest failed");
    }
  }

  /*
   * @testName: getNameTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:3399
   * 
   * @test_Strategy: Use getName to get the name of the entity graph
   */
  public void getNameTest() throws Fault {
    boolean pass = false;

    EntityGraph<Employee2> eg = getEntityManager()
        .createEntityGraph(Employee2.class);

    if (eg.getName() == null) {
      TestUtil.logTrace("Received expected null");
      pass = true;
    } else {
      TestUtil.logErr("Expected name: null, actual:" + eg.getName());
    }

    if (!pass) {
      throw new Fault("getNameTest failed");
    }
  }

  /*
   * @testName: getEntityGraphStringIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:3405;
   * 
   * @test_Strategy: get entity graph that does not exist
   */
  public void getEntityGraphStringIllegalArgumentExceptionTest() throws Fault {
    boolean pass = false;

    try {
      getEntityManager().getEntityGraph("doesnotexist");
      TestUtil.logErr("Did not throw IllegalArgumentException");
    } catch (IllegalArgumentException iae) {
      pass = true;
      TestUtil.logTrace("Received expected IllegalArgumentException");
    }
    if (!pass) {
      throw new Fault(
          "getEntityGraphStringIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: getEntityGraphsClassTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:3406; PERSISTENCE:JAVADOC:3420;
   * 
   * @test_Strategy: Use getEntityGraph to get the named entity graphs in the
   * Employee entity
   */
  public void getEntityGraphsClassTest() throws Fault {
    boolean pass = false;
    List<String> expected = new ArrayList<String>();
    expected.add("first_last_graph");
    expected.add("last_salary_graph");
    expected.add("lastname_department_subgraphs");

    List<String> actual = new ArrayList<String>();

    List<EntityGraph<? super Employee3>> eg = getEntityManager()
        .getEntityGraphs(Employee3.class);
    if (eg.size() > 0) {
      for (EntityGraph e : eg) {
        actual.add(e.getName());
      }
      if (actual.containsAll(expected) && expected.containsAll(actual)
          && actual.size() == expected.size()) {
        TestUtil.logTrace("Received expected results");
        pass = true;
      } else {
        TestUtil.logErr("Expected results");
        for (String s : expected) {
          TestUtil.logErr("expected:" + s);
        }
        TestUtil.logErr("Actual results");
        for (String s : actual) {
          TestUtil.logErr("actual:" + s);
        }
      }
    } else {
      TestUtil.logErr(
          "No named entity graphs were returned eventhough they exist in entity");
    }

    if (!pass) {
      throw new Fault("getEntityGraphsClassTest failed");
    }
  }

  /*
   * @testName: addNamedEntityGraphStringEntityGraphTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:3411;
   * 
   * @test_Strategy: add an entity graph as a named entitygraph
   */
  public void addNamedEntityGraphStringEntityGraphTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    boolean pass4 = false;

    String sExpected = "new_named_entity_graph";

    TestUtil.logMsg("add entity graph to EMF");
    EntityGraph eg = getEntityManager().createEntityGraph(Employee.class);
    eg.addAttributeNodes("id");
    getEntityManager().getEntityManagerFactory().addNamedEntityGraph(sExpected,
        eg);
    EntityGraph eg2 = getEntityManager().getEntityGraph(sExpected);
    if (eg2 != null) {
      if (eg2.getName() != null) {
        if (eg2.getName().equals(sExpected)) {
          TestUtil.logTrace("Received expected entity graph:" + eg2.getName());
          pass1 = true;
        } else {
          TestUtil
              .logErr("Expected: named_entity_graph, actual:" + eg2.getName());
        }
      } else {
        TestUtil.logErr(
            "getName() returned null for a named entity graph added via addNamedEntityGraph");
      }
    } else {
      TestUtil.logErr("getEntityGraph() returned null");
    }
    TestUtil.logMsg("verify nodes");
    List<String> expected = new ArrayList<String>();
    expected.add("id");
    List<String> actual = new ArrayList<String>();

    List<AttributeNode<?>> listNodes = eg2.getAttributeNodes();
    for (AttributeNode an : listNodes) {
      actual.add(an.getAttributeName());
    }
    if (expected.containsAll(actual) && actual.containsAll(expected)
        && expected.size() == actual.size()) {
      TestUtil.logTrace("Received expected AttributeNode");
      pass2 = true;
    } else {
      TestUtil.logErr("Did not received expected AttributeNodes");
      TestUtil.logErr("Expected results");
      for (String s : expected) {
        TestUtil.logErr("expected:" + s);
      }
      TestUtil.logErr("Actual results");
      for (String s : actual) {
        TestUtil.logErr("actual:" + s);
      }
    }

    TestUtil.logMsg("override previous entity graph");
    eg = getEntityManager().createEntityGraph(Employee.class);
    eg.addAttributeNodes("lastName");
    getEntityManager().getEntityManagerFactory().addNamedEntityGraph(sExpected,
        eg);
    eg2 = getEntityManager().getEntityGraph(sExpected);
    if (eg2 != null) {
      if (eg2.getName() != null) {
        if (eg2.getName().equals(sExpected)) {
          TestUtil.logTrace("Received expected entity graph:" + eg2.getName());
          pass3 = true;
        } else {
          TestUtil
              .logErr("Expected: named_entity_graph, actual:" + eg2.getName());
        }
      } else {
        TestUtil.logErr(
            "getName() returned null for a named entity graph added via addNamedEntityGraph");
      }
    } else {
      TestUtil.logErr("getEntityGraph() returned null");
    }
    TestUtil.logMsg("verify nodes of overriden named entity graph");
    expected = new ArrayList<String>();
    expected.add("lastName");
    actual = new ArrayList<String>();

    listNodes = eg2.getAttributeNodes();
    for (AttributeNode an : listNodes) {
      actual.add(an.getAttributeName());
    }
    if (expected.containsAll(actual) && actual.containsAll(expected)
        && expected.size() == actual.size()) {
      TestUtil.logTrace("Received expected AttributeNode");
      pass4 = true;
    } else {
      TestUtil.logErr("Did not received expected AttributeNodes");
      TestUtil.logErr("Expected results");
      for (String s : expected) {
        TestUtil.logErr("expected:" + s);
      }
      TestUtil.logErr("Actual results");
      for (String s : actual) {
        TestUtil.logErr("actual:" + s);
      }
    }

    if (!pass1 || !pass2 || !pass3 || !pass4) {
      throw new Fault("addNamedEntityGraphStringEntityGraphTest failed");
    }
  }

  /*
   * @testName: getEntityGraphsClassIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:3407;
   * 
   * @test_Strategy: get entity graphs from a non entity class
   */
  public void getEntityGraphsClassIllegalArgumentExceptionTest() throws Fault {
    boolean pass = false;

    try {
      getEntityManager().getEntityGraphs(Client.class);
      TestUtil.logErr("Did not throw IllegalArgumentException");
    } catch (IllegalArgumentException iae) {
      pass = true;
      TestUtil.logTrace("Received expected IllegalArgumentException");
    }
    if (!pass) {
      throw new Fault(
          "getEntityGraphsClassIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: annotationsTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:3414; PERSISTENCE:JAVADOC:3415;
   * PERSISTENCE:JAVADOC:3417;
   * 
   * @test_Strategy: test that various data contained in the annotations is
   * returnable
   */
  public void annotationsTest() throws Fault {
    boolean pass = false;
    List<String> expected = new ArrayList<String>();
    expected.add("lastName");
    expected.add("salary");

    List<String> actual = new ArrayList<String>();

    EntityGraph eg = getEntityManager().getEntityGraph("last_salary_graph");
    List<AttributeNode<?>> listNodes = eg.getAttributeNodes();

    for (AttributeNode an : listNodes) {
      actual.add(an.getAttributeName());
    }

    if (actual.containsAll(expected) && expected.containsAll(actual)
        && actual.size() == expected.size()) {
      TestUtil.logTrace("Received expected results");
      pass = true;
    } else {
      TestUtil.logErr("Expected results");
      for (String s : expected) {
        TestUtil.logErr("expected:" + s);
      }
      TestUtil.logErr("Actual results");
      for (String s : actual) {
        TestUtil.logErr("actual:" + s);
      }
    }

    if (!pass) {
      throw new Fault("annotationsTest failed");
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

      deptRef[0] = new Department(1, "Marketing");
      deptRef[1] = new Department(2, "Administration");
      for (Department d : deptRef) {
        getEntityManager().persist(d);
        TestUtil.logTrace("persisted department:" + d);
      }

      empRef[0] = new Employee3(1, "Alan", "Frechette", d1, (float) 35000.0);
      empRef[0].setDepartment(deptRef[0]);
      empRef[1] = new Employee3(2, "Arthur", "Frechette", d2, (float) 35000.0);
      empRef[1].setDepartment(deptRef[0]);
      empRef[2] = new Employee3(3, "Shelly", "McGowan", d3, (float) 50000.0);
      empRef[2].setDepartment(deptRef[1]);
      empRef[3] = new Employee3(4, "Robert", "Bissett", d4, (float) 55000.0);
      empRef[3].setDepartment(deptRef[1]);
      empRef[4] = new Employee3(5, "Stephen", "DMilla", d5, (float) 25000.0);
      empRef[4].setDepartment(deptRef[1]);

      for (Employee3 e : empRef) {
        if (e != null) {
          getEntityManager().persist(e);
          TestUtil.logTrace("persisted employee3:" + e);
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
      getEntityManager().createNativeQuery("DELETE FROM DEPARTMENT")
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
