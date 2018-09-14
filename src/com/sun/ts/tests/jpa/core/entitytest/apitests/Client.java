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

/*
 * $Id$
 */

package com.sun.ts.tests.jpa.core.entitytest.apitests;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Properties;

public class Client extends PMClientBase {

  private static Coffee cRef[] = new Coffee[5];

  public Client() {
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * setup() is called before each test
   */
  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    try {
      super.setup(args, p);
      removeTestData();
      createTestData();
      TestUtil.logTrace("Done creating test data");
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  /*
   * @testName: entityAPITest1
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:53
   * 
   * @test_Strategy: persist throws an IllegalArgumentException if the argument
   * is not an entity
   */

  public void entityAPITest1() throws Fault {

    Foo notAnEntity = new Foo();
    boolean pass = false;

    try {
      getEntityTransaction().begin();
      getEntityManager().persist(notAnEntity);
      getEntityTransaction().commit();

    } catch (IllegalArgumentException e) {
      pass = true;
      TestUtil.logTrace("Exception Caught as Expected: " + e);
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

    if (!pass)
      throw new Fault("entityAPITest1 failed");
  }

  /*
   * @testName: entityAPITest2
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:43
   * 
   * @test_Strategy: find(Class entityClass, Object PK) returns null if the
   * entity does not exist.
   */

  public void entityAPITest2() throws Fault {

    boolean pass = false;

    try {

      Coffee doesNotExist = getEntityManager().find(Coffee.class, 55);

      if (null == doesNotExist) {
        TestUtil.logTrace("find returned null as expected");
        pass = true;
      }

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

    if (!pass)
      throw new Fault("entityAPITest2 failed");
  }

  /*
   * @testName: entityAPITest3
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:43
   * 
   * @test_Strategy: find(Class entityClass, Object PK) throws an
   * IllegalArgumentException if the first argument does not denote an entity
   * type
   */

  public void entityAPITest3() throws Fault {
    boolean pass = false;

    try {

      getEntityManager().find(Foo.class, 1);

    } catch (IllegalArgumentException iae) {
      pass = true;
      TestUtil.logTrace("Exception Caught as Expected: " + iae);
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

    if (!pass)
      throw new Fault("entityAPITest3 failed");
  }

  /*
   * @testName: entityAPITest4
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:43
   * 
   * @test_Strategy: find(Class entityClass, Object PK) throws an
   * IllegalArgumentException if the second argument is not a valid type for
   * that entity's primary key
   */

  public void entityAPITest4() throws Fault {

    long longId = 55L;
    boolean pass = false;

    try {

      Coffee coffee = getEntityManager().find(Coffee.class, longId);

      if (coffee == null) {
        TestUtil.logTrace("coffee is null");
      }

    } catch (IllegalArgumentException iae) {
      pass = true;
      TestUtil.logTrace("Exception Caught as Expected: " + iae);
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

    if (!pass)
      throw new Fault("entityAPITest4 failed");

  }

  /*
   * @testName: getReferenceExceptionsTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:486; PERSISTENCE:JAVADOC:487
   * 
   * @test_Strategy: getReference(Class entityClass, Object PK) throws an
   * IllegalArgumentException if the first argument does not denote an entity
   * type
   */

  public void getReferenceExceptionsTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    TestUtil.logMsg("Testing getReference invalid entity");

    try {
      getEntityManager().getReference(Foo.class, 1);
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
      pass1 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    TestUtil.logMsg("Testing getReference with invalid PK");
    try {
      getEntityManager().getReference(Coffee.class, "1");
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
      pass2 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    TestUtil.logMsg("Testing getReference with null PK");
    try {
      getEntityManager().getReference(Coffee.class, null);
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
      pass3 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass1 || !pass2 || !pass3)
      throw new Fault("getReferenceExceptionsTest failed");
  }

  /*
   * @testName: getReferenceTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:47
   * 
   * @test_Strategy: getReference(Class entityClass, Object PK)
   */

  public void getReferenceTest() throws Fault {

    boolean pass = false;

    try {

      getEntityTransaction().begin();
      Coffee thisCoffee = getEntityManager().getReference(Coffee.class, 1);
      if (thisCoffee != null) {
        pass = true;
        TestUtil.logTrace("Got Reference");
      } else {
        TestUtil.logErr("getReference(Coffee.class, 1) returned null");
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

    if (!pass)
      throw new Fault("getReferenceTest failed");

  }

  /*
   * @testName: entityAPITest8
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:54
   * 
   * @test_Strategy: refresh throws an IllegalArgumentException if the argument
   * is not an entity
   */

  public void entityAPITest8() throws Fault {

    Foo notAnEntity = new Foo();
    boolean pass = false;

    try {

      getEntityTransaction().begin();
      getEntityManager().refresh(notAnEntity);
      getEntityTransaction().commit();

    } catch (IllegalArgumentException iae) {
      pass = true;
      TestUtil.logTrace("Exception Caught as Expected: " + iae);
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

    if (!pass)
      throw new Fault("entityAPITest8 failed");

  }

  /*
   * @testName: entityAPITest10
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:37
   * 
   * @test_Strategy: contains throws an IllegalArgumentException if the argument
   * is not an entity
   */

  public void entityAPITest10() throws Fault {

    Foo notAnEntity = new Foo();
    boolean pass = false;

    try {

      getEntityTransaction().begin();
      getEntityManager().contains(notAnEntity);
      getEntityTransaction().commit();

    } catch (IllegalArgumentException iae) {
      pass = true;
      TestUtil.logTrace("Exception Caught as Expected: " + iae);
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

    if (!pass)
      throw new Fault("entityAPITest10 failed");

  }

  /*
   * @testName: entityAPITest12
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:38; PERSISTENCE:JAVADOC:119;
   * PERSISTENCE:JAVADOC:117; PERSISTENCE:JAVADOC:118; PERSISTENCE:JAVADOC:123;
   * PERSISTENCE:JAVADOC:124; PERSISTENCE:JAVADOC:121; PERSISTENCE:SPEC:1004;
   * PERSISTENCE:JAVADOC:373; PERSISTENCE:SPEC:1238; PERSISTENCE:SPEC:1311;
   * PERSISTENCE:SPEC:1312; PERSISTENCE:SPEC:1929;
   * 
   * @test_Strategy: createNamedQuery creates an instance of Query in JPQL.
   */

  public void entityAPITest12() throws Fault {
    List<Coffee> result = null;
    boolean pass = false;
    int passCounter = 0;
    int i = 0;
    Integer[] expected = new Integer[cRef.length];
    for (Coffee c : cRef) {
      expected[i++] = c.getId();
    }
    try {
      getEntityTransaction().begin();

      result = getEntityManager().createNamedQuery("findAllCoffees")
          .getResultList();

      if (!checkEntityPK(result, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.length + " references, got: " + result.size());
      } else {
        if (verifyEntity(result) == cRef.length) {
          pass = true;
        }
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    if (!pass)
      throw new Fault("entityAPITest12 failed");
  }

  /*
   * @testName: entityAPITest13
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:38; PERSISTENCE:SPEC:1007;
   * PERSISTENCE:SPEC:1009; PERSISTENCE:SPEC:1005; PERSISTENCE:SPEC:1008;
   * PERSISTENCE:SPEC:1930;
   * 
   * @test_Strategy: createNamedQuery creates an instance of Query in SQL. Use
   * the resultSetMapping to name the result set
   */

  public void entityAPITest13() throws Fault {

    List<Coffee> result = null;
    boolean pass = false;
    int passCounter = 0;
    int i = 0;
    Integer[] expected = new Integer[cRef.length];
    for (Coffee c : cRef) {
      expected[i++] = c.getId();
    }

    try {
      getEntityTransaction().begin();

      TestUtil.logTrace("invoke query method, findallSQLCoffees");
      result = getEntityManager().createNamedQuery("findAllSQLCoffees")
          .getResultList();

      if (!checkEntityPK(result, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.length + " references, got: " + result.size());
      } else {
        if (verifyEntity(result) == cRef.length) {
          pass = true;
        }
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }
    if (!pass)
      throw new Fault("entityAPITest13 failed");
  }

  /*
   * @testName: entityAPITest14
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:38; PERSISTENCE:SPEC:372.5
   * 
   * @test_Strategy: Execute a named query that uses a Constructor expression to
   * return a collection of Java Instances.
   */

  public void entityAPITest14() throws Fault {
    List<Coffee> result = null;
    boolean pass = false;
    int passCounter = 0;
    int i = 0;
    Integer[] expected = new Integer[cRef.length];
    for (Coffee c : cRef) {
      expected[i++] = c.getId();
    }

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("invoke query method, findAllNewCoffees");
      result = getEntityManager().createNamedQuery("findAllNewCoffees")
          .getResultList();

      if (!checkEntityPK(result, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.length + " references, got: " + result.size());
      } else {
        if (verifyEntity(result) == cRef.length) {
          pass = true;
        }
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }
    if (!pass)
      throw new Fault("entityAPITest14 failed");
  }

  /*
   * @testName: entityAPITest15
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:42; PERSISTENCE:SPEC:372.5;
   * PERSISTENCE:SPEC:820
   * 
   * @test_Strategy: Execute a Java Persistence QL query that uses a Constructor
   * expression to return a collection of Java Instances.
   */

  public void entityAPITest15() throws Fault {

    List<Coffee> result = null;
    boolean pass = false;
    int passCounter = 0;
    int i = 0;
    Integer[] expected = new Integer[cRef.length];
    for (Coffee c : cRef) {
      expected[i++] = c.getId();
    }

    try {
      getEntityTransaction().begin();
      TestUtil
          .logTrace("try same constructor expression query using createQuery");
      result = getEntityManager().createQuery(
          "Select NEW com.sun.ts.tests.jpa.core.entitytest.apitests.Coffee(c.id, c.brandName, c.price)"
              + " from Coffee c where c.price <> 0")
          .getResultList();

      if (!checkEntityPK(result, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.length + " references, got: " + result.size());
      } else {
        if (verifyEntity(result) == cRef.length) {
          pass = true;
        }
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }

    if (!pass)
      throw new Fault("entityAPITest15 failed");
  }

  /*
   * @testName: entityAPITest16
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:42; PERSISTENCE:SPEC:372.5;
   * PERSISTENCE:SPEC:819
   * 
   * @test_Strategy: A constructor expression is not required to be an entity or
   * mapped to the database. Invoked a query with a Constructor expression using
   * a non-entity class.
   */

  public void entityAPITest16() throws Fault {

    List<Bar> result = null;
    boolean pass = false;
    Integer[] expected = new Integer[1];
    expected[0] = cRef[4].getId();

    try {
      getEntityTransaction().begin();

      TestUtil.logTrace("Execute query in entityAPITest16");
      result = getEntityManager().createQuery(
          "Select NEW com.sun.ts.tests.jpa.core.entitytest.apitests.Bar(c.id, c.brandName, c.price)"
              + " from Coffee c where c.brandName = 'mocha'")
          .getResultList();

      if (!checkEntityPK(result, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.length + " references, got: " + result.size());
      } else {
        TestUtil.logTrace("Received expected id");
        if (result.get(0).getBrandName().equals(cRef[4].getBrandName())
            && result.get(0).getPrice().equals(cRef[4].getPrice())) {
          pass = true;

        } else {
          TestUtil.logErr("Did not get expected results. Expected "
              + expected[0].toString() + ", got: " + result.get(0).toString());

        }
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }

    if (!pass)
      throw new Fault("entityAPITest16 failed");
  }

  /*
   * @testName: entityAPITest17
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:55
   * 
   * @test_Strategy: remove throws an IllegalArgumentException if the instance
   * is not an entity
   */

  public void entityAPITest17() throws Fault {

    Foo notAnEntity = new Foo();
    boolean pass = false;

    try {
      getEntityTransaction().begin();
      getEntityManager().remove(notAnEntity);
      getEntityTransaction().commit();

    } catch (IllegalArgumentException e) {
      pass = true;
      TestUtil.logTrace("Exception Caught as Expected: " + e);
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

    if (!pass)
      throw new Fault("entityAPITest17 failed");
  }

  /*
   * @testName: entityAPITest18
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:38; PERSISTENCE:SPEC:1006;
   * PERSISTENCE:JAVADOC:120; PERSISTENCE:JAVADOC:115; PERSISTENCE:SPEC:1932;
   * 
   * @test_Strategy: createNamedQuery creates an instance of Query in SQL. using
   * resultClass for the resulting instances
   */

  public void entityAPITest18() throws Fault {

    List<Coffee> result = null;
    boolean pass = false;
    int i = 0;
    Integer[] expected = new Integer[cRef.length];
    for (Coffee c : cRef) {
      expected[i++] = c.getId();
    }

    try {
      getEntityTransaction().begin();

      TestUtil.logTrace("invoke query method, findAllSQLCoffees2");
      result = getEntityManager().createNamedQuery("findAllSQLCoffees2")
          .getResultList();

      if (!checkEntityPK(result, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.length + " references, got: " + result.size());
      } else {
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }

    if (!pass)
      throw new Fault("entityAPITest18 failed");
  }

  /*
   * @testName: xmlOverridesNamedNativeQueryTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:2238;
   * 
   * @test_Strategy: xml overrides the specified annotation.
   */

  public void xmlOverridesNamedNativeQueryTest() throws Fault {

    List<Coffee> result = null;
    boolean pass = false;
    Integer[] expected = new Integer[] { cRef[2].getId() };

    try {
      getEntityTransaction().begin();

      result = getEntityManager()
          .createNamedQuery("xmlOverridesNamedNativeQuery").getResultList();

      if (!checkEntityPK(result, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.length + " references, got: " + result.size());
      } else {
        if (verifyEntity(result) == 1) {
          pass = true;
        }
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }

    if (!pass)
      throw new Fault("xmlOverridesNamedNativeQueryTest failed");
  }

  /*
   * @testName: xmlNamedNativeQueryTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:2235;PERSISTENCE:SPEC:2237;
   * 
   * @test_Strategy: call queries defined in xml
   */

  public void xmlNamedNativeQueryTest() throws Fault {

    List<Coffee> result = null;
    boolean pass = false;
    Integer[] expected = new Integer[] { cRef[2].getId() };

    try {
      getEntityTransaction().begin();

      result = getEntityManager().createNamedQuery("xmlNamedNativeQuery")
          .getResultList();

      if (!checkEntityPK(result, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.length + " references, got: " + result.size());
      } else {
        if (verifyEntity(result) == 1) {
          pass = true;
        }
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }

    if (!pass)
      throw new Fault("xmlNamedNativeQueryTest failed");
  }

  /*
   * @testName: xmlOverridesNamedQueryTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:2234;
   * 
   * @test_Strategy: xml overrides the specified annotation.
   */

  public void xmlOverridesNamedQueryTest() throws Fault {

    List<Coffee> result = null;
    boolean pass = false;
    Integer[] expected = new Integer[] { cRef[2].getId() };

    try {
      getEntityTransaction().begin();

      result = getEntityManager().createNamedQuery("xmlOverridesNamedQuery")
          .getResultList();

      if (!checkEntityPK(result, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.length + " references, got: " + result.size());
      } else {
        if (verifyEntity(result) == 1) {
          pass = true;
        }
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }

    if (!pass)
      throw new Fault("xmlOverridesNamedQueryTest failed");
  }

  /*
   * @testName: xmlNamedQueryTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:2231; PERSISTENCE:SPEC:2233;
   * 
   * @test_Strategy: call queries defined in xml
   */

  public void xmlNamedQueryTest() throws Fault {

    List<Coffee> result = null;
    boolean pass = false;
    Integer[] expected = new Integer[] { cRef[2].getId() };

    try {
      getEntityTransaction().begin();

      result = getEntityManager().createNamedQuery("xmlNamedQuery")
          .getResultList();

      if (!checkEntityPK(result, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.length + " references, got: " + result.size());
      } else {
        if (verifyEntity(result) == 1) {
          pass = true;
        }
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }

    if (!pass)
      throw new Fault("xmlNamedQueryTest failed");
  }

  /*
   * @testName: namedNativeQueryInMappedSuperClass
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1932;
   * 
   * @test_Strategy: use NamedQuery and NamedQueries from MappedSuperClass
   */

  public void namedNativeQueryInMappedSuperClass() throws Fault {
    List<Coffee> result = null;
    boolean pass = false;
    Integer[] expected = new Integer[1];
    expected[0] = 3;

    try {
      getEntityTransaction().begin();

      result = getEntityManager().createNamedQuery("findDecafSQLCoffees")
          .getResultList();

      if (!checkEntityPK(result, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.length + " references, got: " + result.size());
      } else {
        if (verifyEntity(result) == 1) {
          pass = true;
        }
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    if (!pass)
      throw new Fault("namedNativeQueryInMappedSuperClass failed");
  }

  /*
   * @testName: namedQueryInMappedSuperClass
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1929;
   * 
   * @test_Strategy: use NamedQuery and NamedQueries from MappedSuperClass
   */

  public void namedQueryInMappedSuperClass() throws Fault {
    List<Coffee> result = null;
    boolean pass = false;
    Integer[] expected = new Integer[1];
    expected[0] = 3;

    try {
      getEntityTransaction().begin();

      result = getEntityManager().createNamedQuery("findDecafCoffees")
          .getResultList();

      if (!checkEntityPK(result, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.length + " references, got: " + result.size());
      } else {
        if (verifyEntity(result) == 1) {
          pass = true;
        }
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    if (!pass)
      throw new Fault("namedQueryInMappedSuperClass failed");
  }

  /*
   * Business Methods to set up data for Test Cases
   */

  private void createTestData() throws Exception {
    try {

      TestUtil.logTrace("createTestData");

      getEntityTransaction().begin();
      TestUtil.logTrace("Create 5 Coffees");
      cRef[0] = new Coffee(1, "hazelnut", 1.0F);
      cRef[1] = new Coffee(2, "vanilla creme", 2.0F);
      cRef[2] = new Coffee(3, "decaf", 3.0F);
      cRef[3] = new Coffee(4, "breakfast blend", 4.0F);
      cRef[4] = new Coffee(5, "mocha", 5.0F);

      TestUtil.logTrace("Start to persist coffees ");
      for (Coffee c : cRef) {
        if (c != null) {
          getEntityManager().persist(c);
          TestUtil.logTrace("persisted coffee " + c);
        }
      }
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

  private int verifyEntity(List<Coffee> result) {
    TestUtil.logTrace("in verifyEntity");
    int passCounter = 0;
    for (Coffee c : result) {
      TestUtil.logTrace("Verifying:" + c);
      if (c.getBrandName().equals(cRef[c.getId() - 1].getBrandName())
          && c.getPrice().equals(cRef[c.getId() - 1].getPrice())) {
        passCounter++;
      } else {
        TestUtil.logErr("Did not get expected results. Expected "
            + cRef[c.getId() - 1] + ", got: " + c);
      }

    }
    return passCounter;
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("Cleanup data");
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
      getEntityManager().createNativeQuery("DELETE FROM \"COFFEE\"")
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
