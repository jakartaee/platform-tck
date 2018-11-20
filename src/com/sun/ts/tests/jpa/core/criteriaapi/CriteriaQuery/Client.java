/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.criteriaapi.CriteriaQuery;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.tests.jpa.common.schema30.*;
import com.sun.ts.tests.jpa.common.schema30.Order;

import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.Bindable;
import javax.persistence.metamodel.EmbeddableType;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

public class Client extends Util {

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

  public void setupAData(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setupData");
    try {
      super.setup(args, p);
      removeATestData();
      createATestData();
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /*
   * @testName: fromClass
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:935; PERSISTENCE:SPEC:1509;
   * PERSISTENCE:SPEC:1513; PERSISTENCE:SPEC:1792; PERSISTENCE:SPEC:1700;
   *
   * @test_Strategy: Select o FROM Order o WHERE NOT o.totalPrice < 4500
   *
   *
   */
  @SetupMethod(name = "setupOrderData")
  public void fromClass() throws Fault {
    boolean pass = false;
    final Double expectedTotalPrice = 4500.0D;

    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

    getEntityTransaction().begin();
    CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
    if (cquery != null) {
      Root<Order> order = cquery
          .from(getEntityManager().getMetamodel().entity(Order.class));

      // Get Metamodel from Root
      EntityType<Order> Order_ = order.getModel();

      cquery.select(order);
      cquery.where(cbuilder.not(cbuilder.lt(
          order.get(Order_.getSingularAttribute("totalPrice", Double.class)),
          expectedTotalPrice)));

      Query q = getEntityManager().createQuery(cquery);

      List result = q.getResultList();
      int expectedResultSize = 3;

      if (result != null) {
        if (result.size() == expectedResultSize) {
          TestUtil.logTrace("Successfully returned expected results");
          pass = true;
        } else {
          TestUtil.logErr("Mismatch in received results - expected = "
              + expectedResultSize + " received = " + result.size());
        }
      } else {
        TestUtil.logErr("Missing expected result");
      }

    } else {
      TestUtil.logErr("Failed to get Non-null Criteria Query");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("fromClass failed");

    }
  }

  /*
   * @testName: fromEntityType
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1096; PERSISTENCE:JAVADOC:1104;
   * PERSISTENCE:JAVADOC:936;
   *
   * @test_Strategy: Select o FROM Order o WHERE NOT o.totalPrice < 4500
   *
   *
   */
  @SetupMethod(name = "setupOrderData")
  public void fromEntityType() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    final Double expectedTotalPrice = 4500.0D;

    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

    getEntityTransaction().begin();
    CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
    if (cquery != null) {
      Root<Order> order = cquery
          .from(getEntityManager().getMetamodel().entity(Order.class));

      // Get Metamodel from Root
      EntityType<Order> Order_ = order.getModel();

      Path<Order> p = (Path) order;
      Expression exp = (Expression) p;

      Expression type = p.type();
      if (type != null) {
        TestUtil.logTrace("Path.type() returned non-null result");
        pass1 = true;
      } else {
        TestUtil.logErr("Expected non-null Path.type()");
      }
      cquery.select(order);
      cquery.where(cbuilder.not(cbuilder.lt(
          p.get(Order_.getSingularAttribute("totalPrice", Double.class)),
          expectedTotalPrice)));

      Query q = getEntityManager().createQuery(cquery);

      List result = q.getResultList();
      int expectedResultSize = 3;

      if (result != null) {
        if (result.size() == expectedResultSize) {
          TestUtil.logTrace("Successfully returned expected results");
          pass2 = true;
        } else {
          TestUtil.logErr("Mismatch in received results - expected = "
              + expectedResultSize + " received = " + result.size());
        }
      } else {
        TestUtil.logErr("Missing expected result");
      }

    } else {
      TestUtil.logErr("Failed to get Non-null Criteria Query");
    }

    getEntityTransaction().commit();

    if (!pass1 || !pass2) {
      throw new Fault("fromEntityType failed");

    }
  }

  /*
   * @testName: select
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:930; PERSISTENCE:SPEC:1751;
   * PERSISTENCE:SPEC:1752; PERSISTENCE:SPEC:1753;
   *
   * @test_Strategy: Select o FROM Order o WHERE NOT o.totalPrice < 4500
   *
   *
   */
  @SetupMethod(name = "setupOrderData")
  public void select() throws Fault {
    boolean pass = false;
    final Double expectedTotalPrice = 4500.0D;

    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

    getEntityTransaction().begin();
    CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
    if (cquery != null) {
      Root<Order> order = cquery.from(Order.class);

      // Get Metamodel from Root
      EntityType<Order> Order_ = order.getModel();

      cquery.select(order);
      cquery.where(cbuilder.not(cbuilder.lt(
          order.get(Order_.getSingularAttribute("totalPrice", Double.class)),
          expectedTotalPrice)));

      Query q = getEntityManager().createQuery(cquery);

      List result = q.getResultList();
      int expectedResultSize = 3;

      if (result != null) {
        if (result.size() == expectedResultSize) {
          TestUtil.logTrace("Successfully returned expected results");
          pass = true;
        } else {
          TestUtil.logErr("Mismatch in received results - expected = "
              + expectedResultSize + " received = " + result.size());
        }
      } else {
        TestUtil.logErr("Missing expected result");
      }

    } else {
      TestUtil.logErr("Failed to get Non-null Criteria Query");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("select test failed");

    }
  }

  /*
   * @testName: selectIllegalArgumentException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:931
   *
   * @test_Strategy:
   */
  @SetupMethod(name = "setupAliasData")
  public void selectIllegalArgumentException() throws Fault {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
    CriteriaQuery cquery = cbuilder.createQuery();
    if (cquery != null) {
      Root<Customer> customer = cquery.from(Customer.class);

      TestUtil.logTrace(
          "Creating select using selection items with the same alias");
      try {
        CompoundSelection<java.lang.Object[]> c = cbuilder.array(
            customer.get("id").alias("SAMEALIAS"),
            customer.get("name").alias("SAMEALIAS"));

        cquery.select(c);

        TestUtil.logErr("Did not thrown IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
        pass = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception", e);
      }
    }

    if (!pass) {
      throw new Fault("selectIllegalArgumentException failed");

    }
  }

  /*
   * @testName: multiselect
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:924; PERSISTENCE:SPEC:1751;
   *
   * @test_Strategy: convert the following JPQL to CriteriaQuery Select c.id,
   * c.name from Customer c
   *
   */
  @SetupMethod(name = "setupCustomerData")
  public void multiselect() throws Fault {
    boolean pass = false;
    final int expectedResultSize = 20;

    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

    getEntityTransaction().begin();
    CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
    if (cquery != null) {
      TestUtil.logTrace("Obtained Non-null Criteria Query");
      Root<Customer> customer = cquery.from(Customer.class);

      // Get Metamodel from Root
      EntityType<Customer> Customer_ = customer.getModel();
      TestUtil.logTrace("Use Tuple Query");

      cquery.multiselect(
          customer.get(Customer_.getSingularAttribute("id", String.class)),
          customer.get(Customer_.getSingularAttribute("name", String.class)));

      Query q = getEntityManager().createQuery(cquery);

      List result = q.getResultList();

      if (result.size() == expectedResultSize) {
        TestUtil.logTrace("Result size =" + result.size());
        pass = true;

      } else {
        TestUtil.logErr("Received incorrect result size =" + result.size());
      }

    } else {
      TestUtil.logErr("Failed to get Non-null Criteria Query");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("multiselect test failed");
    }
  }

  /*
   * @testName: multiselectListTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:926
   *
   * @test_Strategy: Select c.id, c.name from Customer c
   *
   */
  @SetupMethod(name = "setupCustomerData")
  public void multiselectListTest() throws Fault {
    boolean pass = false;
    final int expectedResultSize = 20;

    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

    getEntityTransaction().begin();
    CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
    if (cquery != null) {
      TestUtil.logTrace("Obtained Non-null Criteria Query");
      Root<Customer> customer = cquery.from(Customer.class);

      // Get Metamodel from Root
      EntityType<Customer> Customer_ = customer.getModel();
      TestUtil.logTrace("Use Tuple Query");

      List list = new ArrayList();
      list.add(
          customer.get(Customer_.getSingularAttribute("id", String.class)));
      list.add(
          customer.get(Customer_.getSingularAttribute("name", String.class)));

      cquery.multiselect(list);

      Query q = getEntityManager().createQuery(cquery);

      List result = q.getResultList();

      if (result.size() == expectedResultSize) {
        TestUtil.logTrace("Result size =" + result.size());
        pass = true;

      } else {
        TestUtil.logErr("Received incorrect result size =" + result.size());
      }

    } else {
      TestUtil.logErr("Failed to get Non-null Criteria Query");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("multiselectListTest failed");
    }
  }

  /*
   * @testName: multiselectIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:925; PERSISTENCE:JAVADOC:927
   *
   * @test_Strategy: Create a multiselect using selection items with the same
   * alias and verify exception is thrown
   *
   */
  @SetupMethod(name = "setupAliasData")
  public void multiselectIllegalArgumentExceptionTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;

    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

    TestUtil.logMsg("Testing multiselect invalid item");
    CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
    if (cquery != null) {
      TestUtil.logTrace("Obtained Non-null Criteria Query");
      Root<Customer> customer = cquery.from(Customer.class);

      TestUtil.logTrace(
          "Creating multiselect using selection array of items that do not exist");
      try {
        cquery.multiselect(customer.get("doesnotexist").alias("ALIAS1"),
            customer.get("doesnotexist2").alias("ALIAS2"));
        TestUtil.logErr("Did not thrown IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
        pass1 = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception", e);
      }
    }

    TestUtil.logMsg("Testing multiselect selection[]");
    cquery = cbuilder.createTupleQuery();
    if (cquery != null) {
      TestUtil.logTrace("Obtained Non-null Criteria Query");
      Root<Customer> customer = cquery.from(Customer.class);

      TestUtil.logTrace(
          "Creating multiselect using selection array of items with the same alias");
      Selection[] selection = { customer.get("id").alias("SAMEALIAS"),
          customer.get("name").alias("SAMEALIAS") };

      try {
        cquery.multiselect(selection);
        TestUtil.logErr("Did not thrown IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("received expected IllegalArgumentException");
        pass2 = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception", e);
      }
    }
    TestUtil.logMsg("Testing multiselect List");
    cquery = cbuilder.createTupleQuery();
    if (cquery != null) {
      TestUtil.logTrace("Obtained Non-null Criteria Query");
      Root<Customer> customer = cquery.from(Customer.class);

      TestUtil.logTrace(
          "Creating multiselect using selection items with the same alias");
      try {
        List list = new ArrayList();
        list.add(customer.get("id").alias("SAMEALIAS"));
        list.add(customer.get("name").alias("SAMEALIAS"));

        cquery.multiselect(list);
        TestUtil.logErr("Did not thrown IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("received expected IllegalArgumentException");
        pass3 = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception", e);
      }
    }
    if (!pass1 || !pass2 || !pass3) {
      throw new Fault("multiselectIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: where
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:932; PERSISTENCE:SPEC:1725;
   * PERSISTENCE:SPEC:1726; PERSISTENCE:SPEC:1735; PERSISTENCE:SPEC:1735.2;
   *
   * @test_Strategy: Use Conjunction Select c FROM Customer c where
   * customer.name = 'Robert E. Bissett'
   *
   *
   */
  @SetupMethod(name = "setupCustomerData")
  public void where() throws Fault {
    boolean pass = false;
    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

    getEntityTransaction().begin();
    CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
    if (cquery != null) {
      Root<Customer> customer = cquery.from(Customer.class);

      EntityType<Customer> Customer_ = customer.getModel();
      cquery.select(customer);
      cquery.where(cbuilder.equal(
          customer.get(Customer_.getSingularAttribute("name", String.class)),
          "Robert E. Bissett"));

      Query q = getEntityManager().createQuery(cquery);

      List result = q.getResultList();
      int expectedResultSize = 1;

      if (result != null) {
        if (result.size() == expectedResultSize) {
          TestUtil.logTrace("Successfully returned expected results");
          pass = true;
        } else {
          TestUtil.logErr("Mismatch in received results - expected = "
              + expectedResultSize + " received = " + result.size());
        }
      } else {
        TestUtil.logErr("Missing expected result");
      }

    } else {
      TestUtil.logErr("Failed to get Non-null Criteria Query");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("where test failed");

    }
  }

  /*
   * @testName: createQueryCriteriaUpdateTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1516
   *
   * @test_Strategy: UPDATE Customer c SET c.name = 'foobar' WHERE c.id = 1
   *
   */
  @SetupMethod(name = "setupCustomerData")
  public void createQueryCriteriaUpdateTest() throws Fault {
    boolean pass = false;
    String expected = "foobar";
    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

    getEntityTransaction().begin();
    CriteriaUpdate<Customer> cquery = cbuilder
        .createCriteriaUpdate(Customer.class);
    Root<Customer> customer = cquery.from(Customer.class);
    cquery.set(customer.get("name"), expected);
    cquery.where(cbuilder.equal(customer.get("id"), "1"));

    getEntityManager().createQuery(cquery).executeUpdate();
    getEntityTransaction().commit();
    clearCache();

    getEntityTransaction().begin();

    Customer actual = getEntityManager().find(Customer.class, "1");
    if (actual == null) {
      TestUtil.logErr("Received null result from find");
    } else {
      if (actual.getName().equals(expected)) {
        TestUtil.logTrace("Name was successfully updated");
        pass = true;
      } else {
        TestUtil
            .logErr("Expected:" + expected + ", actual:" + actual.getName());
      }
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("createQueryCriteriaUpdateTest failed");

    }
  }

  /*
   * @testName: createQueryCriteriaDeleteTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1518
   *
   * @test_Strategy: DELETE FROM Customer c WHERE c.id = "1"
   *
   *
   */
  @SetupMethod(name = "setupCustomerData")
  public void createQueryCriteriaDeleteTest() throws Fault {
    boolean pass = false;
    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

    getEntityTransaction().begin();
    CriteriaDelete<Customer> cquery = cbuilder
        .createCriteriaDelete(Customer.class);
    Root<Customer> customer = cquery.from(Customer.class);
    EntityType<Customer> Customer_ = customer.getModel();
    cquery.where(cbuilder.equal(
        customer.get(Customer_.getSingularAttribute("id", String.class)), "1"));

    getEntityManager().createQuery(cquery).executeUpdate();
    getEntityTransaction().commit();
    clearCache();

    getEntityTransaction().begin();

    Customer actual = getEntityManager().find(Customer.class, "1");
    if (actual != null) {
      TestUtil.logErr(
          "Expected null result from find, actual = " + actual.toString());
    } else {
      TestUtil.logTrace("Customer was successfully deleted");
      pass = true;
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("createQueryCriteriaDeleteTest failed");

    }
  }

  /*
   * @testName: wherePredicateArrayTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:933; PERSISTENCE:SPEC:1726;
   * PERSISTENCE:SPEC:1728; PERSISTENCE:SPEC:1728.1; PERSISTENCE:SPEC:1795;
   * 
   * @test_Strategy: Pass a predicate array to the where clause and verify
   * results
   */
  @SetupMethod(name = "setupOrderData")
  public void wherePredicateArrayTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;

    List<Integer> expected = new ArrayList<Integer>();
    expected.add(Integer.valueOf(customerRef[3].getId()));
    Collections.sort(expected);
    List<Integer> actual = new ArrayList<Integer>();

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logMsg("Testing initial query");
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery.where(cbuilder.equal(customer.get("name"), "Robert E. Bissett"));
      cquery.select(customer);

      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      TestUtil
          .logMsg("Modify query but this change should not yet take effect");
      cquery.where(
          cbuilder.like(customer.get(Customer_.home).get(Address_.zip), "%77"));

      Predicate[] predArray = {
          cbuilder.like(customer.get(Customer_.name), "Karen%"),
          cbuilder.like(customer.get(Customer_.name), "%Tegan") };
      cquery.where(predArray);
      List<Customer> result = tquery.getResultList();
      for (Customer c : result) {
        actual.add(Integer.parseInt(c.getId()));
      }

      Collections.sort(actual);

      if (!checkEntityPK(actual, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.size() + " references, got: " + actual.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass1 = true;
      }
      TestUtil.logMsg("Now verify query change does take effect");
      actual.clear();
      expected.clear();
      result.clear();
      expected.add(Integer.valueOf(customerRef[5].getId()));
      tquery = getEntityManager().createQuery(cquery);

      result = tquery.getResultList();
      for (Customer c : result) {
        actual.add(Integer.parseInt(c.getId()));
        TestUtil.logTrace("Found id:" + c.getId());
      }

      Collections.sort(actual);

      if (!checkEntityPK(actual, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.size() + " references, got: " + actual.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass2 = true;
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: ", e);
    }

    if (!pass1 || !pass2) {
      throw new Fault("wherePredicateArrayTest failed");
    }
  }

  /*
   * @testName: fromGetStringTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1029;
   * 
   * @test_Strategy: Verify get(String) returns the correct results
   *
   * Select c FROM Customer c where customer.name = 'Karen R. Tegan'
   */
  @SetupMethod(name = "setupCustomerData")
  public void fromGetStringTest() throws Fault {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery.where(cbuilder.equal(customer.get("name"), "Karen R. Tegan"));
      cquery.select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      List<Customer> result = tquery.getResultList();

      if (result.size() == 1) {
        if (!result.get(0).getId().equals("6")) {
          TestUtil.logErr("Expected id:6, actual:" + result.get(0).getId());
        } else {
          pass = true;
        }
      } else {
        TestUtil
            .logErr("Did not get correct number of results, expected:1, actual:"
                + result.size());
        for (Customer c : result) {
          TestUtil.logErr("id:" + c.getId());
        }
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: ", e);
    }

    if (!pass) {
      throw new Fault("fromGetStringTest failed");
    }
  }

  /*
   * @testName: fromGetStringIllegalStateExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1030;
   * 
   * @test_Strategy: Verify get(String) returns an exception for a basic type
   */
  public void fromGetStringIllegalStateExceptionTest() throws Fault {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<A> cquery = cbuilder.createQuery(A.class);
      try {
        cquery.from(A.class).get("value").get("value2");
        TestUtil.logErr("IllegalStateException not thrown");
      } catch (IllegalStateException ise) {
        TestUtil.logTrace("Received expected IllegalStateException");
        pass = true;
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: ", e);
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: ", e);
    }

    if (!pass) {
      throw new Fault("fromGetStringIllegalStateExceptionTest failed");
    }
  }

  /*
   * @testName: pathGetStringIllegalStateExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1100;
   * 
   * @test_Strategy: Verify get(String) returns an exception for a basic type
   */
  public void pathGetStringIllegalStateExceptionTest() throws Fault {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<A> cquery = cbuilder.createQuery(A.class);
      Path path = cquery.from(A.class).get("value");
      try {
        path.get("value2");
        TestUtil.logErr("IllegalStateException not thrown");
      } catch (IllegalStateException ise) {
        TestUtil.logTrace("Received expected IllegalStateException");
        pass = true;
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: ", e);
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: ", e);
    }

    if (!pass) {
      throw new Fault("pathGetStringIllegalStateExceptionTest failed");
    }
  }

  /*
   * @testName: fromGetStringIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1031;
   * 
   * @test_Strategy: Verify get(String) returns an exception
   *
   */
  public void fromGetStringIllegalArgumentExceptionTest() throws Fault {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<A> cquery = cbuilder.createQuery(A.class);
      try {
        cquery.from(A.class).get("doesnotexist");
        TestUtil.logErr("IllegalArgumentException not thrown");
      } catch (IllegalArgumentException ise) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
        pass = true;
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: ", e);
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: ", e);
    }

    if (!pass) {
      throw new Fault("fromGetStringIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: fromGetModelTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1032;
   * 
   * @test_Strategy: Verify getModel() returns correct result.
   *
   */
  public void fromGetModelTest() throws Fault {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    String expected = "com.sun.ts.tests.jpa.common.schema30.Customer";
    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Bindable bind = cquery.from(Customer.class).getModel();
      String name = bind.getBindableJavaType().getName();
      if (name.equals(expected)) {
        TestUtil.logTrace("Received expected name:" + name);
        pass = true;
      } else {
        TestUtil.logErr("Expected:" + expected + ", actual:" + name);
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: ", e);
    }

    if (!pass) {
      throw new Fault("fromGetModelTest failed");
    }
  }

  /*
   * @testName: fromGetParentPathTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1033;
   * 
   * @test_Strategy: Verify getParentPath() returns correct result.
   *
   */
  @SetupMethod(name = "setupOrderData")
  public void fromGetParentPathTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;

    String expected = "com.sun.ts.tests.jpa.common.schema30.Order";

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
    try {
      getEntityTransaction().begin();
      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      From<Order, Order> from = (From) order;

      Path path = from.getParentPath();
      if (path == null) {
        TestUtil.logTrace("Received expected null");
        pass1 = true;
      } else {
        TestUtil.logErr("Expected:null, actual:" + path);
      }

      Path p = from.join(Order_.creditCard).getParentPath();
      if (p != null) {
        String name = p.getModel().getBindableJavaType().getName();
        if (name != null) {
          if (name.equals(expected)) {
            TestUtil.logTrace("Received expected name:" + name);
            pass2 = true;
          } else {
            TestUtil.logErr("Expected:" + expected + ", actual:" + name);
          }
        } else {
          TestUtil.logErr(
              "Null was returned for p.getModel().getBindableJavaType().getName()");
          TestUtil.logErr("p.getModel:" + p.getModel());
          TestUtil.logErr("p.getModel().getBindableJavaType():"
              + p.getModel().getBindableJavaType());
          TestUtil.logErr("p.getModel().getBindableJavaType().getName():"
              + p.getModel().getBindableJavaType().getName());
        }
      } else {
        TestUtil.logErr("getParentPath() returned null");
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: ", e);
    }

    if (!pass1 || !pass2) {
      throw new Fault("fromGetParentPathTest failed");
    }
  }

  /*
   * @testName: groupBy
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:321; PERSISTENCE:SPEC:1770;
   *
   * @test_Strategy: select c.country.code, count(c.country.code) FROM Customer
   * c GROUP BY c.country.code ORDER BY c.country.code"
   */
  @SetupMethod(name = "setupCustomerData")
  public void groupBy() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;

    final ArrayList<ExpectedResult> expected = new ArrayList<ExpectedResult>();
    expected.add(new ExpectedResult("CHA", "4"));
    expected.add(new ExpectedResult("GBR", "2"));
    expected.add(new ExpectedResult("IRE", "2"));
    expected.add(new ExpectedResult("JPN", "1"));
    expected.add(new ExpectedResult("USA", "11"));

    try {

      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

      Metamodel mm = getEntityManagerFactory().getMetamodel();

      getEntityTransaction().begin();
      CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
      Root<Customer> customer = cquery.from(Customer.class);
      EntityType<Customer> Customer_ = customer.getModel();
      EmbeddableType<Country> Country_ = mm.embeddable(Country.class);

      cquery.multiselect(
          customer.get(Customer_.getSingularAttribute("country", Country.class))
              .get(Country_.getSingularAttribute("code", String.class)),
          cbuilder.count(customer
              .get(Customer_.getSingularAttribute("country", Country.class))
              .get(Country_.getSingularAttribute("code", String.class))));
      cquery.groupBy(
          customer.get(Customer_.getSingularAttribute("country", Country.class))
              .get(Country_.getSingularAttribute("code", String.class)));
      cquery.orderBy(cbuilder.asc(
          customer.get(Customer_.getSingularAttribute("country", Country.class))
              .get(Country_.getSingularAttribute("code", String.class))));

      TypedQuery<Tuple> tquery = getEntityManager().createQuery(cquery);
      Collection<Tuple> result = tquery.getResultList();

      int i = 0;
      int passCount1 = 0;
      int passCount2 = 0;
      if (result.size() == expected.size()) {
        for (Tuple actual : result) {
          TestUtil
              .logTrace("code=" + actual.get(0) + ", count=" + actual.get(1));
          if (!actual.get(0).equals(expected.get(i).arg1)) {
            TestUtil.logErr("Expected: " + expected.get(i).arg1 + ", actual:"
                + actual.get(0));
          } else {
            passCount1++;
          }
          if (!actual.get(1).equals(Long.parseLong(expected.get(i).arg2))) {
            TestUtil.logErr("Expected: " + expected.get(i).arg2 + ", actual:"
                + actual.get(1));
          } else {
            passCount2++;
          }
          i++;
        }
        if (passCount1 == expected.size()) {
          pass1 = true;
        }
        if (passCount2 == expected.size()) {
          pass2 = true;
        }
      } else {
        TestUtil.logErr("Did not get expected number of entries, expected:"
            + expected.size() + ", actual:" + result.size());
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception groupBy: ", e);
    }

    if (!pass1 || !pass2) {
      throw new Fault("groupBy failed");
    }
  }

  /*
   * @testName: groupByExpArrayTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:920; PERSISTENCE:JAVADOC:943;
   * PERSISTENCE:SPEC:1772; PERSISTENCE:SPEC:1775;
   *
   * @test_Strategy: Create a groupBy clause with one expression, then create a
   * second one and verify the second overrides the first. select
   * c.country.code, c.id FROM Customer c GROUP BY c.country.code, c.id ORDER BY
   * c.country.code, c.id"
   */
  @SetupMethod(name = "setupCustomerData")
  public void groupByExpArrayTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    final ArrayList<ExpectedResult> expected = new ArrayList<ExpectedResult>();
    expected.add(new ExpectedResult("CHA", "13"));
    expected.add(new ExpectedResult("CHA", "18"));
    expected.add(new ExpectedResult("CHA", "19"));
    expected.add(new ExpectedResult("CHA", "20"));
    expected.add(new ExpectedResult("GBR", "11"));
    expected.add(new ExpectedResult("GBR", "16"));
    expected.add(new ExpectedResult("IRE", "12"));
    expected.add(new ExpectedResult("IRE", "17"));
    expected.add(new ExpectedResult("JPN", "14"));
    expected.add(new ExpectedResult("USA", "1"));
    expected.add(new ExpectedResult("USA", "10"));
    expected.add(new ExpectedResult("USA", "15"));
    expected.add(new ExpectedResult("USA", "2"));
    expected.add(new ExpectedResult("USA", "3"));
    expected.add(new ExpectedResult("USA", "4"));
    expected.add(new ExpectedResult("USA", "5"));
    expected.add(new ExpectedResult("USA", "6"));
    expected.add(new ExpectedResult("USA", "7"));
    expected.add(new ExpectedResult("USA", "8"));
    expected.add(new ExpectedResult("USA", "9"));

    try {

      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

      Metamodel mm = getEntityManagerFactory().getMetamodel();

      getEntityTransaction().begin();
      CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
      Root<Customer> customer = cquery.from(Customer.class);

      EntityType<Customer> Customer_ = customer.getModel();
      EmbeddableType<Country> Country_ = mm.embeddable(Country.class);
      Selection[] selection = {
          customer.get(Customer_.getSingularAttribute("country", Country.class))
              .get(Country_.getSingularAttribute("code", String.class)),
          customer.get(Customer_.getSingularAttribute("id", String.class)) };

      Expression[] expressionArray1 = {
          customer.get(Customer_.getSingularAttribute("country", Country.class))
              .get(Country_.getSingularAttribute("code", String.class)) };
      Expression[] expressionArray2 = {
          customer.get(Customer_.getSingularAttribute("country", Country.class))
              .get(Country_.getSingularAttribute("code", String.class)),
          customer.get(Customer_.getSingularAttribute("id", String.class)) };
      cquery.multiselect(selection);
      cquery.groupBy(expressionArray1);
      cquery.groupBy(expressionArray2);
      cquery.orderBy(
          cbuilder.asc(customer
              .get(Customer_.getSingularAttribute("country", Country.class))
              .get(Country_.getSingularAttribute("code", String.class))),
          cbuilder.asc(customer
              .get(Customer_.getSingularAttribute("id", String.class))));

      TypedQuery<Tuple> tquery = getEntityManager().createQuery(cquery);
      Collection<Tuple> result = tquery.getResultList();
      if (TestUtil.traceflag) {
        for (Tuple actual : result) {
          TestUtil.logTrace(
              "Actual - code=" + actual.get(0) + ", id=" + actual.get(1));
        }
      }

      int i = 0;
      int passCount1 = 0;
      int passCount2 = 0;
      for (Tuple actual : result) {
        TestUtil.logTrace(
            "verifying: code=" + actual.get(0) + ", id=" + actual.get(1));

        if (!actual.get(0).equals(expected.get(i).getArg1())) {
          TestUtil.logErr("Expected getArg1:" + expected.get(i).getArg1()
              + ", actual.get(0):" + actual.get(0));
        } else {
          passCount1++;
        }
        if (!actual.get(1).equals(expected.get(i).getArg2())) {
          TestUtil.logErr("Expected getArg2:" + expected.get(i).getArg2()
              + ", actual.get(1):" + actual.get(1));
        } else {
          passCount2++;
        }

        i++;
      }
      if (passCount1 == expected.size()) {
        pass1 = true;
      }
      if (passCount2 == expected.size()) {
        pass2 = true;
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception ", e);
    }

    if (!pass1 || !pass2) {
      throw new Fault("groupByExpArrayTest failed");
    }
  }

  /*
   * @testName: groupByListTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:921; PERSISTENCE:JAVADOC:929;
   * PERSISTENCE:JAVADOC:944
   *
   * @test_Strategy: Create a groupBy clause using a List with one expression,
   * then create a second one and verify the second overrides the first. Create
   * a orderBy clause using a List with one expression, then create a second one
   * and verify the second overrides the first. select c.country.code, c.id FROM
   * Customer c GROUP BY c.country.code, c.id ORDER BY c.country.code, c.id"
   */
  @SetupMethod(name = "setupCustomerData")
  public void groupByListTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;

    final ArrayList<ExpectedResult> expected = new ArrayList<ExpectedResult>();
    expected.add(new ExpectedResult("CHA", "13"));
    expected.add(new ExpectedResult("CHA", "18"));
    expected.add(new ExpectedResult("CHA", "19"));
    expected.add(new ExpectedResult("CHA", "20"));
    expected.add(new ExpectedResult("GBR", "11"));
    expected.add(new ExpectedResult("GBR", "16"));
    expected.add(new ExpectedResult("IRE", "12"));
    expected.add(new ExpectedResult("IRE", "17"));
    expected.add(new ExpectedResult("JPN", "14"));
    expected.add(new ExpectedResult("USA", "1"));
    expected.add(new ExpectedResult("USA", "10"));
    expected.add(new ExpectedResult("USA", "15"));
    expected.add(new ExpectedResult("USA", "2"));
    expected.add(new ExpectedResult("USA", "3"));
    expected.add(new ExpectedResult("USA", "4"));
    expected.add(new ExpectedResult("USA", "5"));
    expected.add(new ExpectedResult("USA", "6"));
    expected.add(new ExpectedResult("USA", "7"));
    expected.add(new ExpectedResult("USA", "8"));
    expected.add(new ExpectedResult("USA", "9"));

    try {

      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

      Metamodel mm = getEntityManagerFactory().getMetamodel();

      getEntityTransaction().begin();
      CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
      Root<Customer> customer = cquery.from(Customer.class);

      EntityType<Customer> Customer_ = customer.getModel();
      EmbeddableType<Country> Country_ = mm.embeddable(Country.class);
      Selection[] selection = {
          customer.get(Customer_.getSingularAttribute("country", Country.class))
              .get(Country_.getSingularAttribute("code", String.class)),
          customer.get(Customer_.getSingularAttribute("id", String.class)) };

      List groupByList1 = new ArrayList();
      groupByList1.add(
          customer.get(Customer_.getSingularAttribute("country", Country.class))
              .get(Country_.getSingularAttribute("code", String.class)));

      List groupByList2 = new ArrayList();
      groupByList2.add(
          customer.get(Customer_.getSingularAttribute("country", Country.class))
              .get(Country_.getSingularAttribute("code", String.class)));
      groupByList2.add(
          customer.get(Customer_.getSingularAttribute("id", String.class)));

      cquery.multiselect(selection);
      cquery.groupBy(groupByList1);
      cquery.groupBy(groupByList2);

      List orderList1 = new ArrayList();
      orderList1.add(cbuilder.asc(
          customer.get(Customer_.getSingularAttribute("country", Country.class))
              .get(Country_.getSingularAttribute("code", String.class))));

      List orderList2 = new ArrayList();
      orderList2.add(cbuilder.asc(
          customer.get(Customer_.getSingularAttribute("country", Country.class))
              .get(Country_.getSingularAttribute("code", String.class))));
      orderList2.add(cbuilder.asc(
          customer.get(Customer_.getSingularAttribute("id", String.class))));

      cquery.orderBy(orderList1);
      cquery.orderBy(orderList2);

      TypedQuery<Tuple> tquery = getEntityManager().createQuery(cquery);
      Collection<Tuple> result = tquery.getResultList();

      if (TestUtil.traceflag) {
        for (Tuple actual : result) {
          TestUtil.logTrace(
              "Actual - code=" + actual.get(0) + ", id=" + actual.get(1));
        }
      }

      int i = 0;
      int passCount1 = 0;
      int passCount2 = 0;
      for (Tuple actual : result) {
        TestUtil.logTrace(
            "verifying: code=" + actual.get(0) + ", id=" + actual.get(1));

        if (!actual.get(0).equals(expected.get(i).getArg1())) {
          TestUtil.logErr("Expected getArg1:" + expected.get(i).getArg1()
              + ", actual.get(0):" + actual.get(0));
        } else {
          passCount1++;
        }
        if (!actual.get(1).equals(expected.get(i).getArg2())) {
          TestUtil.logErr("Expected getArg2:" + expected.get(i).getArg2()
              + ", actual.get(1):" + actual.get(1));
        } else {
          passCount2++;
        }

        i++;
      }
      if (passCount1 == expected.size()) {
        pass1 = true;
      }
      if (passCount2 == expected.size()) {
        pass2 = true;
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception ", e);
    }

    if (!pass1 || !pass2) {
      throw new Fault("groupByListTest failed");
    }
  }

  /*
   * @testName: having
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:922; PERSISTENCE:JAVADOC:945;
   * PERSISTENCE:SPEC:1735; PERSISTENCE:SPEC:1735.1; PERSISTENCE:SPEC:1735.3;
   * PERSISTENCE:SPEC:1771;
   *
   * @test_Strategy: SELECT COUNT(c) FROM Customer c GROUP BY c.country.code
   * HAVING c.country.code in ('GR', 'CHA')
   *
   */
  @SetupMethod(name = "setupCustomerData")
  public void having() throws Fault {
    boolean pass = false;
    final Long expectedGBR = 2L;
    final Long expectedCHA = 4L;
    final int expectedRows = 2;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
    try {
      getEntityTransaction().begin();

      CriteriaQuery<Long> cquery = cbuilder.createQuery(Long.class);
      Root<Customer> customer = cquery.from(Customer.class);
      EntityType<Customer> Customer_ = customer.getModel();
      cquery.groupBy(
          customer.get(Customer_.getSingularAttribute("country", Country.class))
              .get("code"));
      Expression exp = customer
          .get(Customer_.getSingularAttribute("country", Country.class))
          .get("code").in("GBR", "CHA");
      cquery.having(exp).select(cbuilder.count(customer));

      TypedQuery<Long> tquery = getEntityManager().createQuery(cquery);
      List<Long> result = tquery.getResultList();

      int numOfExpected = 0;
      for (Long val : result) {
        if ((val.equals(expectedGBR)) || (val.equals(expectedCHA))) {
          numOfExpected++;
        }
      }

      if (numOfExpected == expectedRows) {
        TestUtil.logTrace("Expected results received.");
        pass = true;

      } else {
        TestUtil.logErr(
            "Did not get expected results.  Expected 2 Values returned : "
                + "2 with Country Code GBR and 4 with Country Code CHA. "
                + "Received: " + result.size());
        for (Long val : result) {
          TestUtil.logErr("Count of Codes Returned: " + val);
        }

      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass) {
      throw new Fault("having failed");
    }
  }

  /*
   * @testName: distinct
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:917; PERSISTENCE:JAVADOC:934;
   * PERSISTENCE:SPEC:1760; PERSISTENCE:SPEC:1761;
   *
   * @test_Strategy: SELECT DISTINCT CODE FROM CUSTOMER_TABLE SELECT CODE FROM
   * CUSTOMER_TABLE SELECT CODE FROM CUSTOMER_TABLE *
   *
   */
  @SetupMethod(name = "setupCustomerData")
  public void distinct() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;

    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

    getEntityTransaction().begin();
    TestUtil.logMsg("True test");
    CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
    if (cquery != null) {
      Root<Customer> customer = cquery.from(Customer.class);

      // Get Metamodel from Root
      EntityType<Customer> Customer_ = customer.getModel();

      Metamodel mm = getEntityManagerFactory().getMetamodel();
      EmbeddableType<Country> Country_ = mm.embeddable(Country.class);

      cquery.select(
          customer.get(Customer_.getSingularAttribute("country", Country.class))
              .get(Country_.getSingularAttribute("code", String.class)));

      cquery.distinct(true);

      Query q = getEntityManager().createQuery(cquery);

      List<String> actual = q.getResultList();
      Collections.sort(actual);

      List<String> expected = new ArrayList<String>();
      expected.add("CHA");
      expected.add("GBR");
      expected.add("IRE");
      expected.add("JPN");
      expected.add("USA");
      Collections.sort(expected);

      if (actual != null) {
        if (expected.containsAll(actual) && actual.containsAll(expected)
            && expected.size() == actual.size()) {
          pass1 = true;
          if (TestUtil.traceflag) {

            TestUtil
                .logTrace("Received expected results(" + actual.size() + "):");
            for (String s : actual) {
              TestUtil.logTrace("code:" + s);
            }
          }
        } else {
          TestUtil.logTrace("Expected(" + expected.size() + "):");
          for (String s : expected) {
            TestUtil.logTrace("code:" + s);
          }
          TestUtil.logTrace("Actual(" + actual.size() + "):");
          for (String s : actual) {
            TestUtil.logTrace("code:" + s);
          }
        }
      } else {
        TestUtil.logErr("getResultList() returned null result");
      }

    } else {
      TestUtil.logErr("Failed to get Non-null Criteria Query");
    }

    TestUtil.logMsg("False test");
    cquery = cbuilder.createQuery(String.class);
    if (cquery != null) {
      Root<Customer> customer = cquery.from(Customer.class);

      // Get Metamodel from Root
      EntityType<Customer> Customer_ = customer.getModel();

      Metamodel mm = getEntityManagerFactory().getMetamodel();
      EmbeddableType<Country> Country_ = mm.embeddable(Country.class);

      cquery.select(
          customer.get(Customer_.getSingularAttribute("country", Country.class))
              .get(Country_.getSingularAttribute("code", String.class)));

      cquery.distinct(false);

      Query q = getEntityManager().createQuery(cquery);

      List<String> actual = q.getResultList();
      Collections.sort(actual);

      List<String> expected = new ArrayList<String>();
      expected.add("CHA");
      expected.add("CHA");
      expected.add("CHA");
      expected.add("CHA");
      expected.add("GBR");
      expected.add("GBR");
      expected.add("IRE");
      expected.add("IRE");
      expected.add("JPN");
      expected.add("USA");
      expected.add("USA");
      expected.add("USA");
      expected.add("USA");
      expected.add("USA");
      expected.add("USA");
      expected.add("USA");
      expected.add("USA");
      expected.add("USA");
      expected.add("USA");
      expected.add("USA");
      Collections.sort(expected);

      if (actual != null) {
        if (expected.containsAll(actual) && actual.containsAll(expected)
            && expected.size() == actual.size()) {
          pass2 = true;
          if (TestUtil.traceflag) {

            TestUtil
                .logTrace("Received expected results(" + actual.size() + "):");
            for (String s : expected) {
              TestUtil.logTrace("code:" + s);
            }
          }
        } else {
          TestUtil.logTrace("Expected(" + expected.size() + "):");
          for (String s : expected) {
            TestUtil.logTrace("code:" + s);
          }
          TestUtil.logTrace("Actual(" + actual.size() + "):");
          for (String s : actual) {
            TestUtil.logTrace("code:" + s);
          }
        }
      } else {
        TestUtil.logErr("getResultList() returned null result");
      }

    } else {
      TestUtil.logErr("Failed to get Non-null Criteria Query");
    }
    TestUtil.logMsg("Default test");
    cquery = cbuilder.createQuery(String.class);
    if (cquery != null) {
      Root<Customer> customer = cquery.from(Customer.class);

      // Get Metamodel from Root
      EntityType<Customer> Customer_ = customer.getModel();

      Metamodel mm = getEntityManagerFactory().getMetamodel();
      EmbeddableType<Country> Country_ = mm.embeddable(Country.class);

      cquery.select(
          customer.get(Customer_.getSingularAttribute("country", Country.class))
              .get(Country_.getSingularAttribute("code", String.class)));

      Query q = getEntityManager().createQuery(cquery);

      List<String> actual = q.getResultList();
      Collections.sort(actual);

      List<String> expected = new ArrayList<String>();
      expected.add("CHA");
      expected.add("CHA");
      expected.add("CHA");
      expected.add("CHA");
      expected.add("GBR");
      expected.add("GBR");
      expected.add("IRE");
      expected.add("IRE");
      expected.add("JPN");
      expected.add("USA");
      expected.add("USA");
      expected.add("USA");
      expected.add("USA");
      expected.add("USA");
      expected.add("USA");
      expected.add("USA");
      expected.add("USA");
      expected.add("USA");
      expected.add("USA");
      expected.add("USA");
      Collections.sort(expected);

      if (actual != null) {
        if (expected.containsAll(actual) && actual.containsAll(expected)
            && expected.size() == actual.size()) {
          pass3 = true;
          if (TestUtil.traceflag) {

            TestUtil
                .logTrace("Received expected results(" + actual.size() + "):");
            for (String s : expected) {
              TestUtil.logTrace("code:" + s);
            }
          }
        } else {
          TestUtil.logTrace("Expected(" + expected.size() + "):");
          for (String s : expected) {
            TestUtil.logTrace("code:" + s);
          }
          TestUtil.logTrace("Actual(" + actual.size() + "):");
          for (String s : actual) {
            TestUtil.logTrace("code:" + s);
          }
        }
      } else {
        TestUtil.logErr("getResultList() returned null result");
      }
    } else {
      TestUtil.logErr("Failed to get Non-null Criteria Query");
    }
    getEntityTransaction().commit();

    if (!pass1 || !pass2 || !pass3) {
      throw new Fault("distinct test failed");

    }
  }

  /*
   * @testName: orderBy
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:928; PERSISTENCE:JAVADOC:1083;
   * PERSISTENCE:JAVADOC:1099; PERSISTENCE:JAVADOC:1082; PERSISTENCE:SPEC:1736;
   * 
   * @test_Strategy: Select c.work.zip from Customer c where c.work.zip IS NOT
   * NULL ORDER BY c.work.zip ASC
   */
  @SetupMethod(name = "setupCustomerData")
  public void orderBy() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;

    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

    final String[] expectedZips = new String[] { "00252", "00252", "00252",
        "00252", "00252", "00252", "00252", "00252", "00252", "00252", "00252",
        "00252", "00252", "00252", "00252", "00252", "11345" };
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find work zip codes that are not null");
      CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery.where(cbuilder.isNotNull(customer.get("work").get("zip")))
          .select(customer.get("work").<String> get("zip"));
      Expression<Path> exp1 = customer.get("work").get("zip");
      cquery.orderBy(cbuilder.asc(exp1));
      List<javax.persistence.criteria.Order> lOrder = cquery.getOrderList();
      if (lOrder.size() == 1) {
        javax.persistence.criteria.Order o = lOrder.get(0);
        if (!o.isAscending()) {
          TestUtil.logErr("isAscending() did not return an order of ascending");
        } else {
          pass1 = true;
        }
        if (o.getExpression() != null) {
          TestUtil.logTrace("getExpression() returned non-null expression");
          pass2 = true;
        } else {
          TestUtil.logErr("getExpression() returned null");
        }
      } else {
        TestUtil.logErr("Expected a size of 1, actual:" + lOrder.size());
      }

      TypedQuery<String> tquery = getEntityManager().createQuery(cquery);
      List<String> clist = tquery.getResultList();

      String[] result = clist.toArray(new String[clist.size()]);
      TestUtil.logTrace("Compare results of work zip codes");
      // if pass = false, don't call next comparison, it could
      // cause a false positive depending on the result
      pass3 = Arrays.equals(expectedZips, result);
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception queryTest47: ", e);
    }

    if (!pass1 || !pass2 || !pass3) {
      throw new Fault("orderBy failed");
    }
  }

  /*
   * @testName: pathGetIllegalArgumentException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1101
   * 
   * @test_Strategy:
   */
  public void pathGetIllegalArgumentException() throws Fault {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
    try {
      CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
      Root<Customer> customer = cquery.from(Customer.class);
      customer.get("doesnotexist");
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: ", e);
    }

    if (!pass) {
      throw new Fault("pathGetIllegalArgumentException failed");
    }
  }

  /*
   * @testName: orderReverseTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1083; PERSISTENCE:JAVADOC:1084
   * 
   * @test_Strategy: Select c.work.zip from Customer c where c.work.zip IS NOT
   * NULL ORDER BY c.work.zip ASC
   */
  @SetupMethod(name = "setupCustomerData")
  public void orderReverseTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;

    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

    final String[] expectedZips = new String[] { "11345", "00252", "00252",
        "00252", "00252", "00252", "00252", "00252", "00252", "00252", "00252",
        "00252", "00252", "00252", "00252", "00252", "00252" };
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find work zip codes that are not null");
      CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery.where(cbuilder.isNotNull(customer.get("work").get("zip")));
      cquery.select(customer.get("work").<String> get("zip"));
      cquery.orderBy(cbuilder.asc(customer.get("work").get("zip")).reverse());
      List<javax.persistence.criteria.Order> lOrder = cquery.getOrderList();
      if (lOrder.size() == 1) {
        javax.persistence.criteria.Order o = lOrder.get(0);
        if (o.isAscending()) {
          TestUtil
              .logErr("isAscending() did not return an order of descending");
        } else {
          pass1 = true;
        }
      } else {
        TestUtil.logErr("Expected a size of 1, actual:" + lOrder.size());
      }

      TypedQuery<String> tquery = getEntityManager().createQuery(cquery);
      List<String> clist = tquery.getResultList();

      String[] result = clist.toArray(new String[clist.size()]);
      TestUtil.logTrace("Compare results of work zip codes");
      // if pass = false, don't call next comparison, it could
      // cause a false positive depending on the result
      pass2 = Arrays.equals(expectedZips, result);
      if (!pass2) {
        TestUtil.logErr("Results are incorrect:");
        for (String s : expectedZips) {
          TestUtil.logErr("Expected:" + s);
        }
        for (String s : result) {
          TestUtil.logErr("actual:" + s);
        }
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: ", e);
    }

    if (!pass1 || !pass2) {
      throw new Fault("orderReverseTest failed");
    }
  }

  /*
   * @testName: getOrderList
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:918
   * 
   * @test_Strategy: Select c.work.zip from Customer c where c.work.zip IS NOT
   * NULL ORDER BY c.work.zip ASC
   */
  @SetupMethod(name = "setupCustomerData")
  public void getOrderList() throws Fault {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find work zip codes that are not null");
      CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery.where(cbuilder.isNotNull(customer.get("work").get("zip")))
          .select(customer.get("work").<String> get("zip"))
          .orderBy(cbuilder.asc(customer.get("work").get("zip")));

      List<javax.persistence.criteria.Order> orderedList = cquery
          .getOrderList();

      if (orderedList != null) {
        if (orderedList.size() == 1) {
          pass = true;
          TestUtil.logTrace("Received expected results");
        } else {
          TestUtil.logErr("Received Unexpected results");
        }
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception queryTest47: ", e);
    }

    if (!pass) {
      throw new Fault("getOrderList failed");
    }
  }

  /*
   * @testName: getParameters
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:919
   *
   * @test_Strategy: Select o FROM Order o WHERE NOT o.totalPrice < 4500
   *
   *
   */
  @SetupMethod(name = "setupOrderData")
  public void getParameters() throws Fault {
    boolean pass = false;
    final Double expectedTotalPrice = 4500.0D;

    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

    getEntityTransaction().begin();
    CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
    if (cquery != null) {
      Root<Order> order = cquery.from(Order.class);

      // Get Metamodel from Root
      EntityType<Order> Order_ = order.getModel();
      cquery.select(order);

      cquery.where(cbuilder.not(cbuilder.lt(
          order.get(Order_.getSingularAttribute("totalPrice", Double.class)),
          expectedTotalPrice)));

      Set<ParameterExpression<?>> paramSet = cquery.getParameters();

      if (paramSet != null) {
        if (paramSet.isEmpty()) {
          pass = true;
          TestUtil.logTrace("Received expected results");
        } else {
          TestUtil.logErr("Received Incorrect results");
        }
      }

    } else {
      TestUtil.logErr("Failed to get Non-null Criteria Query");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getParameters test failed");

    }
  }

  /*
   * @testName: executeUpdateIllegalStateException1Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:551
   * 
   * @test_Strategy: Use the
   * CriteriaBuilder.createQuery(CriteriaQuery).executeUpdate() for a
   * CriteriaAPI and verify a IllegalStateException is thrown
   *
   */
  public void executeUpdateIllegalStateException1Test() throws Fault {
    boolean pass = false;

    try {
      getEntityTransaction().begin();

      CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

      CriteriaQuery<Order> cquery = cb.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      cquery.select(order);
      cquery.where(cb.equal(order.get("id"), "1"));
      getEntityManager().createQuery(cquery).executeUpdate();
      TestUtil.logErr("IllegalStateException was not thrown");

    } catch (IllegalStateException ise) {
      pass = true;
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
      throw new Fault("executeUpdateIllegalStateException1Test failed");
  }

  /*
   * @testName: typeTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1738
   * 
   * @test_Strategy: test path.type()
   *
   * Select p from Product p where TYPE(p) = HardwareProduct
   */
  @SetupMethod(name = "setupProductData")
  public void typeTest() throws Fault {
    boolean pass = false;
    List<Integer> expected = new ArrayList<Integer>();
    for (Product p : hardwareRef) {
      expected.add(Integer.valueOf(p.getId()));
    }
    Collections.sort(expected);
    List<Integer> actual = new ArrayList<Integer>();

    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Product> cquery = cbuilder.createQuery(Product.class);
      Root<Product> product = cquery.from(Product.class);
      cquery.where(
          cbuilder.and(cbuilder.equal(product.type(), HardwareProduct.class)));
      cquery.select(product);
      Query q = getEntityManager().createQuery(cquery);

      List<Product> result = q.getResultList();
      for (Product p : result) {
        actual.add(Integer.parseInt(p.getId()));
      }

      Collections.sort(actual);

      if (!checkEntityPK(actual, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.size() + " references, got: " + actual.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception queryTest47: ", e);
    }

    if (!pass) {
      throw new Fault("typeTest failed");
    }
  }

  /*
   * @testName: modifiedQueryTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1787; PERSISTENCE:SPEC:1790;
   *
   * @test_Strategy: Modify a query after it has been executed and verify
   * results. Select c FROM Customer c where customer.name = 'Robert E. Bissett'
   * Select c FROM Customer c where customer.name = 'Irene M. Caruso'
   *
   *
   */
  @SetupMethod(name = "setupCustomerData")
  public void modifiedQueryTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;

    List<Integer> expected = new ArrayList<Integer>();
    expected.add(Integer.valueOf(customerRef[3].getId()));

    List<Integer> actual = new ArrayList<Integer>();
    TestUtil.logMsg("Testing initial query");

    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
    getEntityTransaction().begin();
    CriteriaQuery cquery = cbuilder.createQuery();
    if (cquery != null) {
      Root<Customer> customer = cquery.from(Customer.class);

      EntityType<Customer> Customer_ = customer.getModel();
      cquery.select(customer);
      cquery.where(cbuilder.equal(
          customer.get(Customer_.getSingularAttribute("name", String.class)),
          "Robert E. Bissett"));
      Query q = getEntityManager().createQuery(cquery);

      List<Customer> result = q.getResultList();
      for (Customer c : result) {
        actual.add(Integer.parseInt(c.getId()));
      }

      Collections.sort(actual);

      if (!checkEntityPK(actual, expected)) {
        TestUtil
            .logErr("Did not get expected results for first query. Expected "
                + expected.size() + " references, got: " + actual.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass1 = true;
      }
      TestUtil.logMsg("Testing modified query");

      expected.clear();
      expected.add(Integer.valueOf(customerRef[7].getId()));
      actual.clear();
      cquery.select(customer.get("id"));
      cquery.where(cbuilder.equal(
          customer.get(Customer_.getSingularAttribute("name", String.class)),
          "Irene M. Caruso"));
      q = getEntityManager().createQuery(cquery);
      List<String> lResult = q.getResultList();
      if (lResult.size() == 1) {
        Object o = lResult.get(0);
        actual.add(Integer.parseInt((String) o));
      } else {
        TestUtil.logErr("Expected 1 result, actual:" + lResult.size());
      }

      Collections.sort(actual);

      if (!checkEntityPK(actual, expected)) {
        TestUtil
            .logErr("Did not get expected results for second query. Expected "
                + expected.size() + " references, got: " + actual.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass2 = true;
      }
    } else {
      TestUtil.logErr("Failed to get Non-null Criteria Query");
    }

    getEntityTransaction().commit();

    if (!pass1 || !pass2) {
      throw new Fault("modifiedQueryTest test failed");

    }
  }

  /*
   * @testName: distinctNotSpecifiedTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1664;
   * 
   * @test_Strategy: Verify duplicates are returned when distinct is not
   * specified SELECT o.CUSTOMER.ID FROM ORDER_TABLE o
   * 
   */
  @SetupMethod(name = "setupOrderData")
  public void distinctNotSpecifiedTest() throws Fault {
    boolean pass = false;
    Integer expectedPKs[];
    List<String> o;

    try {
      TestUtil.logTrace("find All customer ids from Orders");

      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      getEntityTransaction().begin();
      CriteriaQuery cquery = cbuilder.createQuery();
      if (cquery != null) {
        Root<Order> order = cquery.from(Order.class);
        EntityType<Order> Order_ = order.getModel();

        Metamodel mm = getEntityManager().getMetamodel();
        EntityType<Customer> Customer_ = mm
            .entity(com.sun.ts.tests.jpa.common.schema30.Customer.class);

        cquery.select(
            order.get(Order_.getSingularAttribute("customer", Customer.class))
                .get(Customer_.getSingularAttribute("id", String.class)));

        TypedQuery<String> tq = getEntityManager().createQuery(cquery);
        o = tq.getResultList();

        expectedPKs = new Integer[20];
        expectedPKs[0] = Integer.parseInt("1");
        expectedPKs[1] = Integer.parseInt("2");
        expectedPKs[2] = Integer.parseInt("3");
        expectedPKs[3] = Integer.parseInt("4");
        expectedPKs[4] = Integer.parseInt("4");
        expectedPKs[5] = Integer.parseInt("5");
        expectedPKs[6] = Integer.parseInt("6");
        expectedPKs[7] = Integer.parseInt("7");
        expectedPKs[8] = Integer.parseInt("8");
        expectedPKs[9] = Integer.parseInt("9");
        expectedPKs[10] = Integer.parseInt("10");
        expectedPKs[11] = Integer.parseInt("11");
        expectedPKs[12] = Integer.parseInt("12");
        expectedPKs[13] = Integer.parseInt("13");
        expectedPKs[14] = Integer.parseInt("14");
        expectedPKs[15] = Integer.parseInt("14");
        expectedPKs[16] = Integer.parseInt("15");
        expectedPKs[17] = Integer.parseInt("16");
        expectedPKs[18] = Integer.parseInt("17");
        expectedPKs[19] = Integer.parseInt("18");

        if (!checkEntityPK(o, expectedPKs, true, true)) {
          TestUtil.logErr("Did not get expected results.  Expected "
              + expectedPKs.length + " references, got: " + o.size());
        } else {
          TestUtil.logTrace("Expected results received");
          pass = true;
        }
      } else {
        TestUtil.logErr("Failed to get Non-null Criteria Query");
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("distinctNotSpecifiedTest failed");
  }

  /*
   * @testName: DoubleOperandResultTypeTests
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1677; PERSISTENCE:SPEC:1677.1;
   * PERSISTENCE:SPEC:1685;
   * 
   * @test_Strategy: Test various operands result in various types
   *
   */
  @SetupMethod(name = "setupAData")
  public void DoubleOperandResultTypeTests() throws Fault {
    boolean pass1, pass2, pass3, pass4, pass5, pass6, pass7, pass8;
    pass1 = pass2 = pass3 = pass4 = pass5 = pass6 = pass7 = pass8 = false;
    Object p;
    try {
      TestUtil.logMsg("Testing + Double operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();
      cquery.select(cbuilder.sum(
          a.get(A_.getSingularAttribute("basicBigDouble", Double.class)), 1));
      ParameterExpression<Double> param = cbuilder.parameter(Double.class);
      cquery.where(cbuilder.equal(
          a.get(A_.getSingularAttribute("basicBigDouble", Double.class)),
          param));
      double whereValue = 1234.5;
      p = getEntityManager().createQuery(cquery).setParameter(param, whereValue)
          .getSingleResult();
      if (p instanceof Double) {
        TestUtil.logTrace("Received expected Double type");
        pass1 = true;
      } else {
        TestUtil
            .logErr("Result was not of type Double:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing - Double operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();
      cquery.select(cbuilder.diff(
          a.get(A_.getSingularAttribute("basicBigDouble", Double.class)), 1d));
      ParameterExpression<Double> param = cbuilder.parameter(Double.class);
      cquery.where(cbuilder.gt(
          a.get(A_.getSingularAttribute("basicBigDouble", Double.class)),
          param));
      double whereValue = 1234.0;
      p = getEntityManager().createQuery(cquery).setParameter(param, whereValue)
          .getSingleResult();
      if (p instanceof Double) {
        TestUtil.logTrace("Received expected Double type");
        pass2 = true;
      } else {
        TestUtil
            .logErr("Result was not of type Double:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing * Double operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();
      cquery.select(cbuilder.prod(
          a.get(A_.getSingularAttribute("basicBigDouble", Double.class)), 1));
      ParameterExpression<Double> param = cbuilder.parameter(Double.class);
      cquery.where(cbuilder.lt(
          a.get(A_.getSingularAttribute("basicBigDouble", Double.class)),
          param));
      double whereValue = 1235.0;
      p = getEntityManager().createQuery(cquery).setParameter(param, whereValue)
          .getSingleResult();
      if (p instanceof Double) {
        TestUtil.logTrace("Received expected Double type");
        pass3 = true;
      } else {
        TestUtil
            .logErr("Result was not of type Double:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing / Double operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();
      cquery.select(cbuilder.quot(
          a.get(A_.getSingularAttribute("basicBigDouble", Double.class)), 1));
      ParameterExpression<Double> param = cbuilder.parameter(Double.class);
      cquery.where(cbuilder.notEqual(
          a.get(A_.getSingularAttribute("basicBigDouble", Double.class)),
          param));
      double whereValue = 1235.0;
      p = getEntityManager().createQuery(cquery).setParameter(param, whereValue)
          .getSingleResult();
      if (p instanceof Double) {
        TestUtil.logTrace("Received expected Double type");
        pass4 = true;
      } else {
        TestUtil
            .logErr("Result was not of type Double:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing + double operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();
      cquery.select(cbuilder
          .sum(a.get(A_.getSingularAttribute("basicDouble", Double.class)), 1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Double) {
        TestUtil.logTrace("Received expected double type");
        pass5 = true;
      } else {
        TestUtil
            .logErr("Result was not of type double:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing - double operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();
      cquery.select(cbuilder.diff(
          a.get(A_.getSingularAttribute("basicDouble", Double.class)), 1d));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Double) {
        TestUtil.logTrace("Received expected double type");
        pass6 = true;
      } else {
        TestUtil
            .logErr("Result was not of type double:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing * double operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();
      cquery.select(cbuilder.prod(
          a.get(A_.getSingularAttribute("basicDouble", Double.class)), 1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Double) {
        TestUtil.logTrace("Received expected double type");
        pass7 = true;
      } else {
        TestUtil
            .logErr("Result was not of type double:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing / double operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();
      cquery.select(cbuilder.quot(
          a.get(A_.getSingularAttribute("basicDouble", Double.class)), 1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Double) {
        TestUtil.logTrace("Received expected double type");
        pass8 = true;
      } else {
        TestUtil
            .logErr("Result was not of type double:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    if (!pass1 || !pass2 || !pass3 || !pass4 || !pass5 || !pass6 || !pass7
        || !pass8)
      throw new Fault("DoubleOperandResultTypeTests failed");
  }

  /*
   * @testName: FloatOperandResultTypeTests
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1677; PERSISTENCE:SPEC:1677.2;
   * 
   * @test_Strategy: Test various operands result in various types
   *
   */
  @SetupMethod(name = "setupAData")
  public void FloatOperandResultTypeTests() throws Fault {
    boolean pass1, pass2, pass3, pass4, pass5, pass6, pass7, pass8;
    pass1 = pass2 = pass3 = pass4 = pass5 = pass6 = pass7 = pass8 = false;
    Object p;
    try {
      TestUtil.logMsg("Testing + Float operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder.sum(
          a.get(A_.getSingularAttribute("basicBigFloat", Float.class)), 1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Float) {
        TestUtil.logTrace("Received expected Float type");
        pass1 = true;
      } else {
        TestUtil
            .logErr("Result was not of type Float:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing - Float operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder.diff(
          a.get(A_.getSingularAttribute("basicBigFloat", Float.class)), 1f));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Float) {
        TestUtil.logTrace("Received expected Float type");
        pass2 = true;
      } else {
        TestUtil
            .logErr("Result was not of type Float:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing * Float operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder.prod(
          a.get(A_.getSingularAttribute("basicBigFloat", Float.class)), 1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Float) {
        TestUtil.logTrace("Received expected Float type");
        pass3 = true;
      } else {
        TestUtil
            .logErr("Result was not of type Float:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing / Float operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder.quot(
          a.get(A_.getSingularAttribute("basicBigFloat", Float.class)), 1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Float) {
        TestUtil.logTrace("Received expected Float type");
        pass4 = true;
      } else {
        TestUtil
            .logErr("Result was not of type Float:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing + float operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder
          .sum(a.get(A_.getSingularAttribute("basicFloat", Float.class)), 1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Float) {
        TestUtil.logTrace("Received expected float type");
        pass5 = true;
      } else {
        TestUtil
            .logErr("Result was not of type float:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing - float operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder
          .diff(a.get(A_.getSingularAttribute("basicFloat", Float.class)), 1f));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Float) {
        TestUtil.logTrace("Received expected float type");
        pass6 = true;
      } else {
        TestUtil
            .logErr("Result was not of type float:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing * float operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder
          .prod(a.get(A_.getSingularAttribute("basicFloat", Float.class)), 1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Float) {
        TestUtil.logTrace("Received expected float type");
        pass7 = true;
      } else {
        TestUtil
            .logErr("Result was not of type float:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing / float operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder
          .quot(a.get(A_.getSingularAttribute("basicFloat", Float.class)), 1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Float) {
        TestUtil.logTrace("Received expected Float type");
        pass8 = true;
      } else {
        TestUtil
            .logErr("Result was not of type Float:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    if (!pass1 || !pass2 || !pass3 || !pass4 || !pass5 || !pass6 || !pass7
        || !pass8)
      throw new Fault("FloatOperandResultTypeTests failed");
  }

  /*
   * @testName: BigDecimalOperandResultTypeTests
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1677; PERSISTENCE:SPEC:1677.3;
   * 
   * @test_Strategy: Test various operands result in various types
   *
   */
  @SetupMethod(name = "setupAData")
  public void BigDecimalOperandResultTypeTests() throws Fault {
    boolean pass1, pass2, pass3, pass4;
    pass1 = pass2 = pass3 = pass4 = false;
    Object p;
    try {
      TestUtil.logMsg("Testing + BigDecimal operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder.sum(
          a.get(A_.getSingularAttribute("basicBigDecimal", BigDecimal.class)),
          1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof BigDecimal) {
        TestUtil.logTrace("Received expected BigDecimal type");
        pass1 = true;
      } else {
        TestUtil.logErr(
            "Result was not of type BigDecimal:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing - BigDecimal operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder.diff(
          a.get(A_.getSingularAttribute("basicBigDecimal", BigDecimal.class)),
          new BigDecimal(1)));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof BigDecimal) {
        TestUtil.logTrace("Received expected BigDecimal type");
        pass2 = true;
      } else {
        TestUtil.logErr(
            "Result was not of type BigDecimal:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing * BigDecimal operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder.prod(
          a.get(A_.getSingularAttribute("basicBigDecimal", BigDecimal.class)),
          1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof BigDecimal) {
        TestUtil.logTrace("Received expected BigDecimal type");
        pass3 = true;
      } else {
        TestUtil.logErr(
            "Result was not of type BigDecimal:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing / BigDecimal operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder.quot(
          a.get(A_.getSingularAttribute("basicBigDecimal", BigDecimal.class)),
          1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();

      if (p instanceof BigDecimal) {
        TestUtil.logTrace("Received expected BigDecimal type");
        pass4 = true;
      } else {
        TestUtil.logErr(
            "Result was not of type BigDecimal:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    if (!pass1 || !pass2 || !pass3 || !pass4)
      throw new Fault("BigDecimalOperandResultTypeTests failed");
  }

  /*
   * @testName: BigIntegerOperandResultTypeTests
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1677; PERSISTENCE:SPEC:1677.4;
   * 
   * @test_Strategy: Test various operands result in various types
   *
   */
  @SetupMethod(name = "setupAData")
  public void BigIntegerOperandResultTypeTests() throws Fault {
    boolean pass1, pass2, pass3;
    pass1 = pass2 = pass3 = false;
    Object p;
    try {
      TestUtil.logMsg("Testing + BigInteger operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder.sum(
          a.get(A_.getSingularAttribute("basicBigInteger", BigInteger.class)),
          1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof BigInteger) {
        TestUtil.logTrace("Received expected BigInteger type");
        pass1 = true;
      } else {
        TestUtil.logErr(
            "Result was not of type BigInteger:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing - BigInteger operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder.diff(
          a.get(A_.getSingularAttribute("basicBigInteger", BigInteger.class)),
          new BigInteger("1")));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof BigInteger) {
        TestUtil.logTrace("Received expected BigInteger type");
        pass2 = true;
      } else {
        TestUtil.logErr(
            "Result was not of type BigInteger:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing * BigInteger operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder.prod(
          a.get(A_.getSingularAttribute("basicBigInteger", BigInteger.class)),
          1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof BigInteger) {
        TestUtil.logTrace("Received expected BigInteger type");
        pass3 = true;
      } else {
        TestUtil.logErr(
            "Result was not of type BigInteger:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass1 || !pass2 || !pass3)
      throw new Fault("BigIntegerOperandResultTypeTests failed");
  }

  /*
   * @testName: LongOperandResultTypeTests
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1677; PERSISTENCE:SPEC:1677.5;
   * 
   * @test_Strategy: Test various operands result in various types
   *
   */
  @SetupMethod(name = "setupAData")
  public void LongOperandResultTypeTests() throws Fault {
    boolean pass1, pass2, pass3, pass4, pass5, pass6;
    pass1 = pass2 = pass3 = pass4 = pass5 = pass6 = false;
    Object p;
    try {
      TestUtil.logMsg("Testing + Long operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder
          .sum(a.get(A_.getSingularAttribute("basicBigLong", Long.class)), 1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Long) {
        TestUtil.logTrace("Received expected Long type");
        pass1 = true;
      } else {
        TestUtil
            .logErr("Result was not of type Long:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing - Long operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder.diff(
          a.get(A_.getSingularAttribute("basicBigLong", Long.class)), 1L));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Long) {
        TestUtil.logTrace("Received expected Long type");
        pass2 = true;
      } else {
        TestUtil
            .logErr("Result was not of type Long:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing * Long operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder
          .prod(a.get(A_.getSingularAttribute("basicBigLong", Long.class)), 1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Long) {
        TestUtil.logTrace("Received expected Long type");
        pass3 = true;
      } else {
        TestUtil
            .logErr("Result was not of type Long:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    try {
      TestUtil.logMsg("Testing + long operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder
          .sum(a.get(A_.getSingularAttribute("basicLong", Long.class)), 1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Long) {
        TestUtil.logTrace("Received expected long type");
        pass4 = true;
      } else {
        TestUtil
            .logErr("Result was not of type long:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing - long operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder
          .diff(a.get(A_.getSingularAttribute("basicLong", Long.class)), 1L));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Long) {
        TestUtil.logTrace("Received expected long type");
        pass5 = true;
      } else {
        TestUtil
            .logErr("Result was not of type long:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing * long operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder
          .prod(a.get(A_.getSingularAttribute("basicLong", Long.class)), 1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Long) {
        TestUtil.logTrace("Received expected long type");
        pass6 = true;
      } else {
        TestUtil
            .logErr("Result was not of type long:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass1 || !pass2 || !pass3 || !pass4 || !pass5 || !pass6)
      throw new Fault("LongOperandResultTypeTests failed");
  }

  /*
   * @testName: ShortOperandResultTypeTests
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1677; PERSISTENCE:SPEC:1677.6;
   * 
   * @test_Strategy: Test various operands of integral type and verify the
   * result of the operation is of type Integer
   *
   */
  @SetupMethod(name = "setupAData")
  public void ShortOperandResultTypeTests() throws Fault {
    boolean pass1, pass2, pass3, pass4, pass5, pass6;
    pass1 = pass2 = pass3 = pass4 = pass5 = pass6 = false;
    Object p;
    try {
      TestUtil.logMsg("Testing + Short operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder.sum(
          a.get(A_.getSingularAttribute("basicBigShort", Short.class)), 1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Integer) {
        TestUtil.logTrace("Received expected Integer type");
        pass1 = true;
      } else {
        TestUtil
            .logErr("Result was not of type Integer:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing - Short operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder.diff(
          a.get(A_.getSingularAttribute("basicBigShort", Short.class)), 1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Integer) {
        TestUtil.logTrace("Received expected Integer type");
        pass2 = true;
      } else {
        TestUtil
            .logErr("Result was not of type Integer:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing * Short operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder.prod(
          a.get(A_.getSingularAttribute("basicBigShort", Short.class)), 1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Integer) {
        TestUtil.logTrace("Received expected Integer type");
        pass3 = true;
      } else {
        TestUtil
            .logErr("Result was not of type Integer:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing + short operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder
          .sum(a.get(A_.getSingularAttribute("basicShort", Short.class)), 1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Integer) {
        TestUtil.logTrace("Received expected Integer type");
        pass4 = true;
      } else {
        TestUtil
            .logErr("Result was not of type Integer:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing - short operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder
          .diff(a.get(A_.getSingularAttribute("basicShort", Short.class)), 1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Integer) {
        TestUtil.logTrace("Received expected Integer type");
        pass5 = true;
      } else {
        TestUtil
            .logErr("Result was not of type Integer:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing * short operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder
          .prod(a.get(A_.getSingularAttribute("basicShort", Short.class)), 1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Integer) {
        TestUtil.logTrace("Received expected Integer type");
        pass6 = true;
      } else {
        TestUtil
            .logErr("Result was not of type Integer:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass1 || !pass2 || !pass3 || !pass4 || !pass5 || !pass6)
      throw new Fault("ShortOperandResultTypeTests failed");
  }

  /*
   * @testName: resultContainsFetchReference
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1718;
   * 
   * @test_Strategy: SELECT d FROM Department d LEFT JOIN FETCH
   * d.lastNameEmployees WHERE d.id = 1
   */
  @SetupMethod(name = "setupDepartmentEmployeeData")
  public void resultContainsFetchReference() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();

    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
    CriteriaQuery<Department> cquery = cbuilder.createQuery(Department.class);
    if (cquery != null) {

      Root<Department> d = cquery.from(Department.class);
      d.fetch(Department_.lastNameEmployees, JoinType.LEFT);
      cquery.where(cbuilder.equal(d.get(Department_.id), 1)).select(d);
      cquery.distinct(true);
      Query q = getEntityManager().createQuery(cquery);
      List<Department> result = q.getResultList();

      List<Integer> expected = new ArrayList<Integer>();
      expected.add(deptRef[0].getId());

      if (result.size() == 1) {
        List<Integer> actual = new ArrayList<Integer>();
        actual.add(result.get(0).getId());
        if (!checkEntityPK(actual, expected)) {
          TestUtil.logErr("Did not get expected results. Expected "
              + expected.size() + " references, got: " + result.size());
        } else {
          TestUtil.logTrace("Expected results received");
          pass = true;
        }
      } else {
        TestUtil.logErr("More than 1 result got returned:");
        for (Department dept : result) {
          TestUtil.logErr("Dept:" + dept.toString());
        }
      }
    } else {
      TestUtil.logErr("Failed to get Non-null Criteria Query");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("resultContainsFetchReference test failed");

    }
  }

  /*
   *
   * Setup for Query Language Tests
   *
   */

  public static class ExpectedResult {
    String arg1 = null;

    String arg2 = null;

    public ExpectedResult(String arg1) {
      this.arg1 = arg1;
    }

    public ExpectedResult(String arg1, String arg2) {
      this.arg1 = arg1;
      this.arg2 = arg2;
    }

    public String getArg1() {
      return this.arg1;
    }

    public void setArg1(String arg1) {
      this.arg1 = arg1;
    }

    public String getArg2() {
      return this.arg2;
    }

    public void setArg2(String arg2) {
      this.arg2 = arg2;
    }

  }

  public void createATestData() {
    try {
      getEntityTransaction().begin();
      final Integer integer = 1234;
      final short basicShort = 12;
      final Short basicBigShort = basicShort;
      final float basicFloat = 12.3f;
      final Float basicBigFloat = basicFloat;
      final long basicLong = 1234l;
      final Long basicBigLong = basicLong;
      final double basicDouble = 1234.5;
      final Double basicBigDouble = basicDouble;
      final char[] charArray = { 'a', 'b', 'c' };
      final Character[] bigCharacterArray = { 'a', 'b', 'c' };
      final byte[] byteArray = "abc".getBytes();
      final Byte[] bigByteArray = { (byte) 111, (byte) 101, (byte) 100 };
      final BigInteger bigInteger = new BigInteger("12345");
      final BigDecimal bigDecimal = new BigDecimal(bigInteger);
      final Date date = new Date();
      final long timeInSeconds = date.getTime();
      final Time time = new Time(timeInSeconds);
      final Timestamp timeStamp = new Timestamp(timeInSeconds);
      final Calendar calendar = Calendar.getInstance();

      A aRef = new A("9", null, 9, integer, basicShort, basicBigShort,
          basicFloat, basicBigFloat, basicLong, basicBigLong, basicDouble,
          basicBigDouble, 'a', charArray, bigCharacterArray, byteArray,
          bigByteArray, bigInteger, bigDecimal, date, time, timeStamp,
          calendar);

      getEntityManager().persist(aRef);
      getEntityManager().flush();
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception in createTestData:", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception during Rollback:", re);
      }
    }

  }

  private void removeATestData() {
    TestUtil.logTrace("removeTestData");
    if (getEntityTransaction().isActive()) {
      getEntityTransaction().rollback();
    }
    try {
      getEntityTransaction().begin();
      getEntityManager().createNativeQuery("DELETE FROM A_BASIC")
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
