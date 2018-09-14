/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.criteriaapi.misc;

import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.schema30.*;
import com.sun.ts.tests.jpa.common.schema30.Order;

import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.criteria.Expression;

import javax.persistence.metamodel.EntityType;
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

  /*
   * @testName: predicateIsNegatedTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1118; PERSISTENCE:JAVADOC:1119
   *
   * @test_Strategy:
   *
   */
  public void predicateIsNegatedTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;

    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

    TestUtil.logMsg("Testing default");
    Predicate pred = cbuilder.equal(cbuilder.literal("1"), "1");
    Boolean result = pred.isNegated();
    if (!result) {
      TestUtil.logTrace("Received expected result:" + result);
      pass1 = true;
    } else {
      TestUtil.logErr("Expected:false , actual:" + result);
    }
    pred = null;
    TestUtil.logMsg("Testing when Predicate.not is present");
    pred = cbuilder.equal(cbuilder.literal("1"), "1").not();
    result = pred.isNegated();

    if (result) {
      TestUtil.logTrace("Received expected result:" + result);
      pass2 = true;
    } else {
      TestUtil.logErr("Expected:true, actual:" + result);
    }
    pred = null;
    TestUtil.logMsg("Testing when CriteriaBuilder.not is present");
    pred = cbuilder.not(cbuilder.equal(cbuilder.literal("1"), "1"));
    result = pred.isNegated();
    if (result) {
      TestUtil.logTrace("Received expected result:" + result);
      pass3 = true;

    } else {
      TestUtil.logErr("Expected:true, actual:" + result);
    }

    if (!pass1 || !pass2 || !pass3) {
      throw new Fault("predicateIsNegatedTest failed");

    }
  }

  /*
   * @testName: predicateBooleanOperatorTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1127; PERSISTENCE:JAVADOC:1128
   *
   * @test_Strategy:
   */
  public void predicateBooleanOperatorTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;

    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

    getEntityTransaction().begin();
    CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
    if (cquery != null) {

      TestUtil.logMsg("Testing values()");
      Predicate.BooleanOperator[] results = Predicate.BooleanOperator.values();
      if (results.length == 2) {
        if (results[0].equals(Predicate.BooleanOperator.AND)
            && results[1].equals(Predicate.BooleanOperator.OR)
            || results[0].equals(Predicate.BooleanOperator.OR)
                && results[1].equals(Predicate.BooleanOperator.AND)) {
          TestUtil.logTrace("Received expected values from values()");
          pass1 = true;
        }

      } else {
        TestUtil
            .logErr("Expected number of values: 2, actual:" + results.length);
      }

      TestUtil.logMsg("Testing valueOf(...)");
      for (Predicate.BooleanOperator pb : Predicate.BooleanOperator.values()) {
        TestUtil.logTrace("Testing:" + pb.name());
        try {
          Predicate.BooleanOperator.valueOf(pb.name());
          pass2 = true;
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace(
              "Received unexpected IllegalArgumentException exception");
        } catch (Exception e) {
          TestUtil.logErr("Received unexpected exception", e);
        }
      }

      TestUtil.logMsg("Testing valueOf(Invalid_value)");
      try {
        Predicate.BooleanOperator.valueOf("Invalid_value");
        TestUtil.logErr("Did not received IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected exception");
        pass3 = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception", e);
      }

    } else {
      TestUtil.logErr("Failed to get Non-null Criteria Query");
    }

    if (!pass1 || !pass2 || !pass3) {
      throw new Fault("predicateBooleanOperatorTest failed");

    }
  }

  /*
   * @testName: predicateGetOperatorTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1117
   *
   * @test_Strategy:
   */
  public void predicateGetOperatorTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;

    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

    CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
    if (cquery != null) {
      Root<Customer> customer = cquery.from(Customer.class);

      EntityType<Customer> Customer_ = customer.getModel();

      TestUtil.logMsg("Testing default");
      Predicate predicate = cbuilder.equal(
          customer.get(Customer_.getSingularAttribute("id", String.class)),
          "1");
      Predicate.BooleanOperator result = predicate.getOperator();
      if (!result.equals(Predicate.BooleanOperator.AND)) {
        TestUtil.logErr("Expected:" + Predicate.BooleanOperator.AND.name()
            + ", actual:" + result.name());
      } else {
        pass1 = true;
      }
      TestUtil.logMsg("Testing AND");
      predicate = cbuilder.and(
          cbuilder.equal(
              customer.get(Customer_.getSingularAttribute("id", String.class)),
              "1"),
          cbuilder.equal(
              customer.get(Customer_.getSingularAttribute("id", String.class)),
              "1"));
      if (!predicate.getOperator().equals(Predicate.BooleanOperator.AND)) {
        TestUtil.logErr("Expected:" + Predicate.BooleanOperator.AND.name()
            + ", actual:" + result.name());
      } else {
        pass2 = true;
      }
      TestUtil.logMsg("Testing OR");
      predicate = cbuilder.or(
          cbuilder.equal(
              customer.get(Customer_.getSingularAttribute("id", String.class)),
              "1"),
          cbuilder.equal(
              customer.get(Customer_.getSingularAttribute("id", String.class)),
              "1"));
      if (!predicate.getOperator().equals(Predicate.BooleanOperator.OR)) {
        TestUtil.logErr("Expected:" + Predicate.BooleanOperator.OR.name()
            + ", actual:" + result.name());
      } else {
        pass3 = true;
      }
    } else {
      TestUtil.logErr("Failed to get Non-null Criteria Query");
    }

    if (!pass1 || !pass2 || !pass3) {
      throw new Fault("predicateGetOperatorTest failed");

    }
  }

  /*
   * @testName: predicateGetExpressionsTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1116
   *
   * @test_Strategy:
   */
  @SetupMethod(name = "setupCustomerData")
  public void predicateGetExpressionsTest() throws Fault {
    boolean pass2 = false;
    boolean pass3 = false;
    boolean pass4 = false;
    boolean pass5 = false;

    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

    CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
    if (cquery != null) {
      Root<Customer> customer = cquery.from(Customer.class);

      EntityType<Customer> Customer_ = customer.getModel();

      List<Integer> actual = new ArrayList<Integer>();
      List<Integer> expected = new ArrayList<Integer>();
      expected.add(Integer.parseInt(customerRef[0].getId()));
      expected.add(Integer.parseInt(customerRef[1].getId()));

      TestUtil.logMsg("Testing disjunction");
      getEntityTransaction().begin();
      Expression expr1 = cbuilder.equal(
          customer.get(Customer_.getSingularAttribute("id", String.class)),
          "1");
      Expression expr2 = cbuilder.equal(
          customer.get(Customer_.getSingularAttribute("id", String.class)),
          "2");
      Predicate predicate = cbuilder.or(expr1, expr2);
      List<Expression<Boolean>> c = predicate.getExpressions();
      if (c.size() != 2) {
        TestUtil.logErr(
            "Expected a predicate expression size of:2, actual:" + c.size());
      } else {
        pass2 = true;
      }
      cquery.select(customer);
      cquery.where(cbuilder.or(c.get(0), c.get(1)));
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      List<Customer> result = tquery.getResultList();
      for (Customer cust : result) {
        TestUtil.logTrace("result:" + cust);
        actual.add(Integer.parseInt(cust.getId()));
      }
      Collections.sort(actual);
      if (expected.containsAll(actual) && actual.containsAll(expected)
          && expected.size() == actual.size()) {

        TestUtil.logTrace("Received expected results");
        pass3 = true;
      } else {
        TestUtil.logErr("Did not get expected results");
        for (Integer i : expected) {
          TestUtil.logErr("expected:" + i);
        }
        for (Integer i : actual) {
          TestUtil.logErr("actual:" + i);
        }
      }

      actual = new ArrayList<Integer>();
      expected = new ArrayList<Integer>();
      expected.add(Integer.parseInt(customerRef[0].getId()));
      cquery = null;
      cquery = cbuilder.createQuery(Customer.class);
      customer = cquery.from(Customer.class);

      TestUtil.logMsg("Testing conjunction");
      expr1 = cbuilder.equal(
          customer.get(Customer_.getSingularAttribute("id", String.class)),
          "1");
      expr2 = cbuilder.equal(
          customer.get(Customer_.getSingularAttribute("name", String.class)),
          "Alan E. Frechette");
      predicate = cbuilder.and(expr1, expr2);
      c = predicate.getExpressions();
      if (c.size() != 2) {
        TestUtil.logErr(
            "Expected a predicate expression size of:2, actual:" + c.size());
      } else {
        pass4 = true;
      }
      cquery.select(customer);
      cquery.where(cbuilder.and(c.get(0), c.get(1)));

      tquery = getEntityManager().createQuery(cquery);
      result = tquery.getResultList();
      for (Customer cust : result) {
        TestUtil.logTrace("result:" + cust);
        actual.add(Integer.parseInt(cust.getId()));
      }
      Collections.sort(actual);
      if (expected.containsAll(actual) && actual.containsAll(expected)
          && expected.size() == actual.size()) {

        TestUtil.logTrace("Received expected results");
        pass5 = true;
      } else {
        TestUtil.logErr("Did not get expected results");
        for (Integer i : expected) {
          TestUtil.logErr("expected:" + i);
        }
        for (Integer i : actual) {
          TestUtil.logErr("actual:" + i);
        }
      }
      getEntityTransaction().commit();

    } else {
      TestUtil.logErr("Failed to get Non-null Criteria Query");
    }

    if (!pass2 || !pass3 || !pass4 || !pass5) {
      throw new Fault("predicateGetExpressionsTest failed");

    }
  }

  /*
   * @testName: predicateIsNotNullTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1125;
   *
   * @test_Strategy: SELECT c FROM Customer c WHERE c.name IS NOT NULL
   */
  @SetupMethod(name = "setupCustomerData")
  public void predicateIsNotNullTest() throws Fault {
    boolean pass = false;

    String[] expected = new String[19];
    expected[0] = customerRef[0].getId();
    expected[1] = customerRef[1].getId();
    expected[2] = customerRef[2].getId();
    expected[3] = customerRef[3].getId();
    expected[4] = customerRef[4].getId();
    expected[5] = customerRef[5].getId();
    expected[6] = customerRef[6].getId();
    expected[7] = customerRef[7].getId();
    expected[8] = customerRef[8].getId();
    expected[9] = customerRef[9].getId();
    expected[10] = customerRef[10].getId();
    expected[11] = customerRef[12].getId();
    expected[12] = customerRef[13].getId();
    expected[13] = customerRef[14].getId();
    expected[14] = customerRef[15].getId();
    expected[15] = customerRef[16].getId();
    expected[16] = customerRef[17].getId();
    expected[17] = customerRef[18].getId();
    expected[18] = customerRef[19].getId();

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
      getEntityTransaction().begin();

      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      if (customer != null) {

        Predicate pred = customer.<String> get("name").isNotNull();
        cquery.where(pred);
        cquery.select(customer);

        TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
        List<Customer> clist = tquery.getResultList();

        if (!checkEntityPK(clist, expected)) {
          TestUtil.logErr("Did not get expected results. Expected "
              + expected.length + " references, got: " + clist.size());
        } else {
          TestUtil.logTrace("Expected results received");
          pass = true;
        }
      } else {
        TestUtil.logErr("Failed to get Non-null root");
      }

      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault("predicateIsNotNullTest failed");
    }
  }

  /*
   * @testName: predicateIsNullTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1126;
   *
   * @test_Strategy: SELECT c FROM Customer c WHERE c.name IS NULL
   */
  @SetupMethod(name = "setupCustomerData")
  public void predicateIsNullTest() throws Fault {
    boolean pass = false;

    String[] expected = new String[1];
    expected[0] = customerRef[11].getId();

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
      getEntityTransaction().begin();

      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      if (customer != null) {

        Predicate pred = customer.<String> get("name").isNull();
        cquery.where(pred);
        cquery.select(customer);

        TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
        List<Customer> clist = tquery.getResultList();

        if (!checkEntityPK(clist, expected)) {
          TestUtil.logErr("Did not get expected results. Expected "
              + expected.length + " references, got: " + clist.size());
        } else {
          TestUtil.logTrace("Expected results received");
          pass = true;
        }
      } else {
        TestUtil.logErr("Failed to get Non-null root");
      }

      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault("predicateIsNullTest failed");
    }
  }

  /*
   * @testName: pathInObjectArrayTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1106;
   *
   * @test_Strategy: SELECT c FROM Customer c WHERE c.ID IN (1,2)
   */
  @SetupMethod(name = "setupCustomerData")
  public void pathInObjectArrayTest() throws Fault {
    boolean pass = false;

    String[] expected = new String[2];
    expected[0] = customerRef[0].getId();
    expected[1] = customerRef[1].getId();

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
    getEntityTransaction().begin();

    CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
    Root<Customer> customer = cquery.from(Customer.class);
    if (customer != null) {

      EntityType<Customer> Customer_ = customer.getModel();
      Path<String> idPath = customer
          .get(Customer_.getSingularAttribute("id", String.class));
      Object[] o = { "1", "2" };
      cquery.where(idPath.in(o));
      cquery.select(customer);

      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      List<Customer> clist = tquery.getResultList();

      if (!checkEntityPK(clist, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.length + " references, got: " + clist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
    } else {
      TestUtil.logErr("Failed to get Non-null root");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("pathInObjectArrayTest failed");
    }
  }

  /*
   * @testName: pathInExpressionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1109;
   *
   * @test_Strategy: SELECT c FROM Customer c WHERE c.ID = 1
   */
  @SetupMethod(name = "setupCustomerData")
  public void pathInExpressionTest() throws Fault {
    boolean pass = false;

    String[] expected = new String[1];
    expected[0] = customerRef[0].getId();

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
    getEntityTransaction().begin();

    CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
    Root<Customer> customer = cquery.from(Customer.class);
    if (customer != null) {

      EntityType<Customer> Customer_ = customer.getModel();
      Path<String> idPath = customer
          .get(Customer_.getSingularAttribute("id", String.class));
      Expression e = cbuilder.literal("1");
      cquery.where(idPath.in(e));

      cquery.select(customer);

      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      List<Customer> clist = tquery.getResultList();

      if (!checkEntityPK(clist, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.length + " references, got: " + clist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
    } else {
      TestUtil.logErr("Failed to get Non-null root");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("pathInExpressionTest failed");
    }
  }

  /*
   * @testName: pathInExpressionArrayTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1107;
   *
   * @test_Strategy: SELECT c FROM Customer c WHERE c.ID IN (1,2)
   *
   *
   */
  @SetupMethod(name = "setupCustomerData")
  public void pathInExpressionArrayTest() throws Fault {
    boolean pass = false;

    String[] expected = new String[2];
    expected[0] = customerRef[0].getId();
    expected[1] = customerRef[1].getId();

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
    getEntityTransaction().begin();

    CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
    Root<Customer> customer = cquery.from(Customer.class);
    if (customer != null) {

      EntityType<Customer> Customer_ = customer.getModel();
      Path<String> idPath = customer
          .get(Customer_.getSingularAttribute("id", String.class));
      Expression[] e = { cbuilder.literal("1"), cbuilder.literal("2") };
      cquery.where(idPath.in(e));

      cquery.select(customer);

      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      List<Customer> clist = tquery.getResultList();

      if (!checkEntityPK(clist, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.length + " references, got: " + clist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
    } else {
      TestUtil.logErr("Failed to get Non-null root");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("pathInExpressionArrayTest failed");
    }
  }

  /*
   * @testName: pathInCollectionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1108;
   *
   * @test_Strategy: SELECT c FROM Customer c WHERE c.ID IN (1,2)
   */
  @SetupMethod(name = "setupCustomerData")
  public void pathInCollectionTest() throws Fault {
    boolean pass = false;

    String[] expected = new String[2];
    expected[0] = customerRef[0].getId();
    expected[1] = customerRef[1].getId();

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
    getEntityTransaction().begin();

    CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
    Root<Customer> customer = cquery.from(Customer.class);
    if (customer != null) {

      EntityType<Customer> Customer_ = customer.getModel();
      Path<String> idPath = customer
          .get(Customer_.getSingularAttribute("id", String.class));
      Collection<String> col = new ArrayList<String>();
      col.add("1");
      col.add("2");

      cquery.where(idPath.in(col));
      cquery.select(customer);

      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      List<Customer> clist = tquery.getResultList();

      if (!checkEntityPK(clist, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.length + " references, got: " + clist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
    } else {
      TestUtil.logErr("Failed to get Non-null root");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("pathInCollectionTest failed");
    }
  }

  /*
   * @testName: pathIsNotNullTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1110;
   *
   * @test_Strategy: SELECT c FROM Customer c WHERE c.name IS NOT NULL
   *
   */
  @SetupMethod(name = "setupCustomerData")
  public void pathIsNotNullTest() throws Fault {
    boolean pass = false;

    String[] expected = new String[19];
    expected[0] = customerRef[0].getId();
    expected[1] = customerRef[1].getId();
    expected[2] = customerRef[2].getId();
    expected[3] = customerRef[3].getId();
    expected[4] = customerRef[4].getId();
    expected[5] = customerRef[5].getId();
    expected[6] = customerRef[6].getId();
    expected[7] = customerRef[7].getId();
    expected[8] = customerRef[8].getId();
    expected[9] = customerRef[9].getId();
    expected[10] = customerRef[10].getId();
    expected[11] = customerRef[12].getId();
    expected[12] = customerRef[13].getId();
    expected[13] = customerRef[14].getId();
    expected[14] = customerRef[15].getId();
    expected[15] = customerRef[16].getId();
    expected[16] = customerRef[17].getId();
    expected[17] = customerRef[18].getId();
    expected[18] = customerRef[19].getId();

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
    getEntityTransaction().begin();

    CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
    Root<Customer> customer = cquery.from(Customer.class);
    if (customer != null) {

      EntityType<Customer> Customer_ = customer.getModel();
      Path<String> idPath = customer
          .get(Customer_.getSingularAttribute("name", String.class));
      cquery.where(idPath.isNotNull());

      cquery.select(customer);

      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      List<Customer> clist = tquery.getResultList();

      if (!checkEntityPK(clist, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.length + " references, got: " + clist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
    } else {
      TestUtil.logErr("Failed to get Non-null root");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("pathIsNotNullTest failed");
    }
  }

  /*
   * @testName: pathIsNullTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1111;
   *
   * @test_Strategy: SELECT c FROM Customer c WHERE c.name IS NULL
   */
  @SetupMethod(name = "setupCustomerData")
  public void pathIsNullTest() throws Fault {
    boolean pass = false;

    String[] expected = new String[1];
    expected[0] = customerRef[11].getId();

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
    getEntityTransaction().begin();

    CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
    Root<Customer> customer = cquery.from(Customer.class);
    if (customer != null) {

      EntityType<Customer> Customer_ = customer.getModel();
      Path<String> idPath = customer
          .get(Customer_.getSingularAttribute("name", String.class));
      cquery.where(idPath.isNull());
      cquery.select(customer);

      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      List<Customer> clist = tquery.getResultList();

      if (!checkEntityPK(clist, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.length + " references, got: " + clist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
    } else {
      TestUtil.logErr("Failed to get Non-null root");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("pathIsNullTest failed");
    }
  }

  /*
   * @testName: compoundSelectionGetCompoundSelectionItemsTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:731; PERSISTENCE:JAVADOC:733;
   * PERSISTENCE:JAVADOC:773
   *
   * @test_Strategy:
   *
   *
   */
  @SetupMethod(name = "setupOrderData")
  public void compoundSelectionGetCompoundSelectionItemsTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = true;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
    getEntityTransaction().begin();
    Expression exp1 = cbuilder.literal("1");
    Expression exp2 = cbuilder.literal("2");
    CompoundSelection cs = cbuilder.tuple(exp1, exp2);
    boolean bActual = cs.isCompoundSelection();
    if (bActual == true) {
      List<Selection<?>> lSel = cs.getCompoundSelectionItems();
      if (lSel.size() == 2) {

        CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();

        Root<Order> order = cquery.from(Order.class);
        cquery.select(cs);
        cquery.where(cbuilder.equal(order.get("id"), "1"));

        TypedQuery<Tuple> tquery = getEntityManager().createQuery(cquery);
        Collection<Tuple> result = tquery.getResultList();
        if (result.size() == 1) {
          int i = 0;
          for (Tuple actual : result) {
            pass1 = true;
            TestUtil.logTrace(
                "first=" + actual.get(0) + ", second=" + actual.get(1));
            if (!actual.get(0).equals("1")) {
              TestUtil.logErr("Expected: 1, actual:" + actual.get(0));
              pass2 = false;
            }
            if (!actual.get(1).equals("2")) {
              TestUtil.logErr("Expected: 2, actual:" + actual.get(1));
              pass2 = false;
            }
            i++;
          }
        } else {
          TestUtil.logErr("Expected: 1 tuple, actual:" + result.size());
          for (Tuple actual : result) {
            TestUtil
                .logErr("first=" + actual.get(0) + ", second=" + actual.get(1));
          }
        }
      } else {
        TestUtil.logErr(
            "Expected: 2 compound selection item, actual:" + lSel.size());
        for (Selection s : lSel) {
          TestUtil.logErr("selection:" + s.toString());
        }
      }
    } else {
      TestUtil.logErr(
          "Expected isCompoundSelection() to return: true, actual:" + bActual);
    }
    getEntityTransaction().commit();

    if (!pass1 || !pass2) {
      throw new Fault("compoundSelectionGetCompoundSelectionItemsTest failed");
    }
  }

  /*
   * @testName: selectionGetCompoundSelectionItemsTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1165;
   *
   * @test_Strategy:
   *
   *
   */
  @SetupMethod(name = "setupOrderData")
  public void selectionGetCompoundSelectionItemsTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = true;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    Expression exp1 = cbuilder.literal("1");
    Expression exp2 = cbuilder.literal("2");
    Selection sel = cbuilder.tuple(exp1, exp2);
    boolean bActual = sel.isCompoundSelection();
    if (bActual == true) {
      List<Selection<?>> lSel = sel.getCompoundSelectionItems();
      if (lSel.size() == 2) {

        CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();

        Root<Order> order = cquery.from(Order.class);
        cquery.select(sel);
        cquery.where(cbuilder.equal(order.get("id"), "1"));

        TypedQuery<Tuple> tquery = getEntityManager().createQuery(cquery);
        Collection<Tuple> result = tquery.getResultList();
        if (result.size() == 1) {
          for (Tuple actual : result) {
            pass1 = true;
            TestUtil.logTrace(
                "first=" + actual.get(0) + ", second=" + actual.get(1));
            if (!actual.get(0).equals("1")) {
              TestUtil.logErr("Expected: 1, actual:" + actual.get(0));
              pass2 = false;
            }
            if (!actual.get(1).equals("2")) {
              TestUtil.logErr("Expected: 2, actual:" + actual.get(1));
              pass2 = false;
            }
          }
        } else {
          TestUtil.logErr("Expected: 1 tuple, actual:" + result.size());
          for (Tuple actual : result) {
            TestUtil
                .logErr("first=" + actual.get(0) + ", second=" + actual.get(1));
          }
        }
      } else {
        TestUtil.logErr(
            "Expected: 2 compound selection item, actual:" + lSel.size());
        for (Selection s : lSel) {
          TestUtil.logErr("selection:" + s.toString());
        }
      }
    } else {
      TestUtil.logErr(
          "Expected isCompoundSelection() to return: true, actual:" + bActual);
    }

    if (!pass1 || !pass2) {
      throw new Fault("selectionGetCompoundSelectionItemsTest failed");
    }
  }

  /*
   * @testName: selectionGetCompoundSelectionItemsIllegalStateExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1166;
   * 
   * @test_Strategy:
   */
  public void selectionGetCompoundSelectionItemsIllegalStateExceptionTest()
      throws Fault {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
    Root<Customer> customer = cquery.from(Customer.class);

    Selection sel = cbuilder.length(customer.get(Customer_.id));
    try {
      sel.getCompoundSelectionItems();
      TestUtil.logErr("Did not throw IllegalStateException");
    } catch (IllegalStateException ise) {
      TestUtil.logTrace("Received IllegalStateException");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }

    if (!pass) {
      throw new Fault(
          "selectionGetCompoundSelectionItemsIllegalStateExceptionTest failed");
    }
  }

  /*
   * @testName: expressionIsCompoundSelectionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:961;
   *
   * @test_Strategy:
   *
   * expression will never be a compound expression
   */
  public void expressionIsCompoundSelectionTest() throws Fault {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    Expression exp = cbuilder.literal("1");
    boolean actual = exp.isCompoundSelection();
    if (actual == false) {
      TestUtil.logTrace("Received expected result:" + actual);
      pass = true;
    } else {
      TestUtil.logErr(
          "Expected isCompoundSelection() to return: false, actual:" + actual);
    }
    if (!pass) {
      throw new Fault("expressionGetCompoundSelectionItemsTest failed");
    }
  }

  /*
   * @testName: expressionGetCompoundSelectionItemsIllegalStateExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:960;
   * 
   * @test_Strategy:
   */
  public void expressionGetCompoundSelectionItemsIllegalStateExceptionTest()
      throws Fault {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    Expression exp = cbuilder.literal("1");
    try {
      exp.getCompoundSelectionItems();
      TestUtil.logErr("Did not throw IllegalStateException");
    } catch (IllegalStateException ise) {
      TestUtil.logTrace("Received IllegalStateException");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }

    if (!pass) {
      throw new Fault(
          "expressionGetCompoundSelectionItemsIllegalStateExceptionTest failed");
    }
  }

  /*
   * @testName: subqueryInObjectArrayTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1207;
   *
   * @test_Strategy:
   *
   * SELECT c.id FROM Customer c WHERE c.ID IN (SELECT c1.id FROM Customer c1
   * WHERE (c1.ID IN (1)))
   *
   */
  @SetupMethod(name = "setupCustomerData")
  public void subqueryInObjectArrayTest() throws Fault {
    boolean pass = false;

    String[] expected = new String[1];
    expected[0] = customerRef[0].getId();

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
    getEntityTransaction().begin();

    CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
    Root<Customer> customer = cquery.from(Customer.class);
    if (customer != null) {

      cquery.select(customer);

      Subquery<String> sq = cquery.subquery(String.class);
      Root<Customer> sqc = sq.from(Customer.class);
      Object[] o = { "1" };
      sq.where(sqc.get(Customer_.id).in(o));
      sq.select(sqc.get(Customer_.id));

      cquery.where(customer.get(Customer_.id).in(sq));

      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);

      List<Customer> clist = tquery.getResultList();

      if (!checkEntityPK(clist, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.length + " references, got: " + clist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
    } else {
      TestUtil.logErr("Failed to get Non-null root");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("subqueryInObjectArrayTest failed");
    }
  }

  /*
   * @testName: subqueryInExpressionArrayTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1208;
   *
   * @test_Strategy:
   *
   * SELECT c.id FROM Customer c WHERE c.ID IN (SELECT c1.id FROM Customer c1
   * WHERE (c1.ID IN (1)))
   *
   */
  @SetupMethod(name = "setupCustomerData")
  public void subqueryInExpressionArrayTest() throws Fault {
    boolean pass = false;

    String[] expected = new String[1];
    expected[0] = customerRef[0].getId();

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
    getEntityTransaction().begin();

    CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
    Root<Customer> customer = cquery.from(Customer.class);
    if (customer != null) {

      cquery.select(customer);

      Subquery<String> sq = cquery.subquery(String.class);
      Root<Customer> sqc = sq.from(Customer.class);
      Expression[] exp = { cbuilder.literal("1") };
      sq.where(sqc.get(Customer_.id).in(exp));
      sq.select(sqc.get(Customer_.id));

      cquery.where(customer.get(Customer_.id).in(sq));

      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);

      List<Customer> clist = tquery.getResultList();

      if (!checkEntityPK(clist, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.length + " references, got: " + clist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
    } else {
      TestUtil.logErr("Failed to get Non-null root");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("subqueryInExpressionArrayTest failed");
    }
  }

  /*
   * @testName: subqueryInExpressionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1210;
   *
   * @test_Strategy:
   *
   * SELECT c.id FROM Customer c WHERE c.ID IN (SELECT c1.id FROM Customer c1
   * WHERE (c1.ID IN (1)))
   *
   */
  @SetupMethod(name = "setupCustomerData")
  public void subqueryInExpressionTest() throws Fault {
    boolean pass = false;

    String[] expected = new String[1];
    expected[0] = customerRef[0].getId();

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
    getEntityTransaction().begin();

    CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
    Root<Customer> customer = cquery.from(Customer.class);
    if (customer != null) {

      cquery.select(customer);

      Subquery<String> sq = cquery.subquery(String.class);
      Root<Customer> sqc = sq.from(Customer.class);
      Expression exp = cbuilder.literal("1");
      sq.where(sqc.get(Customer_.id).in(exp));
      sq.select(sqc.get(Customer_.id));

      cquery.where(customer.get(Customer_.id).in(sq));

      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);

      List<Customer> clist = tquery.getResultList();

      if (!checkEntityPK(clist, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.length + " references, got: " + clist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
    } else {
      TestUtil.logErr("Failed to get Non-null root");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("subqueryInExpressionTest failed");
    }
  }

  /*
   * @testName: subqueryInCollectionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1209;
   *
   * @test_Strategy:
   *
   * SELECT c.id FROM Customer c WHERE c.ID IN (SELECT c1.id FROM Customer c1
   * WHERE (c1.ID IN (1)))
   *
   */
  @SetupMethod(name = "setupCustomerData")
  public void subqueryInCollectionTest() throws Fault {
    boolean pass = false;

    String[] expected = new String[1];
    expected[0] = customerRef[0].getId();

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
    getEntityTransaction().begin();

    CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
    Root<Customer> customer = cquery.from(Customer.class);
    if (customer != null) {

      cquery.select(customer);

      Subquery<String> sq = cquery.subquery(String.class);
      Root<Customer> sqc = sq.from(Customer.class);
      Collection col = new ArrayList();
      col.add("1");
      sq.where(sqc.get(Customer_.id).in(col));
      sq.select(sqc.get(Customer_.id));

      // sq.where(cbuilder.in(customer.get("id"), "1"));
      cquery.where(customer.get(Customer_.id).in(sq));

      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);

      List<Customer> clist = tquery.getResultList();

      if (!checkEntityPK(clist, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.length + " references, got: " + clist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
    } else {
      TestUtil.logErr("Failed to get Non-null root");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("subqueryInCollectionTest failed");
    }
  }

  /*
   * @testName: subqueryIsNotNull
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1211;
   *
   * @test_Strategy: SELECT c.id FROM Customer c WHERE c.id IN (SELECT c1.ID
   * FROM Customer c1 WHERE ((c1.NAME IS NOT NULL) AND (t1.ID = "1")))
   *
   */
  @SetupMethod(name = "setupCustomerData")
  public void subqueryIsNotNull() throws Fault {
    boolean pass = false;

    String[] expected = new String[1];
    expected[0] = customerRef[0].getId();

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
    getEntityTransaction().begin();

    CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
    Root<Customer> customer = cquery.from(Customer.class);
    if (customer != null) {

      Subquery<String> sq = cquery.subquery(String.class);
      Root<Customer> sqc = sq.from(Customer.class);
      sq.where(cbuilder.and(cbuilder.isNotNull(sqc.get(Customer_.name)),
          cbuilder.equal(sqc.get(Customer_.id), "1")));
      sq.select(sqc.get(Customer_.id));

      cquery.where(customer.get(Customer_.id).in(sq));
      cquery.select(customer);

      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      List<Customer> clist = tquery.getResultList();

      if (!checkEntityPK(clist, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.length + " references, got: " + clist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
    } else {
      TestUtil.logErr("Failed to get Non-null root");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("subqueryIsNotNull test failed");

    }

  }

  /*
   * @testName: subqueryIsNull
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1212;
   *
   * @test_Strategy: SELECT c.id FROM Customer c WHERE c.id IN (SELECT c1.ID
   * FROM Customer c1 WHERE (c1.NAME IS NULL))
   *
   */
  @SetupMethod(name = "setupCustomerData")
  public void subqueryIsNull() throws Fault {
    boolean pass = false;

    String[] expected = new String[1];
    expected[0] = customerRef[11].getId();

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
    getEntityTransaction().begin();

    CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
    Root<Customer> customer = cquery.from(Customer.class);
    if (customer != null) {

      Subquery<String> sq = cquery.subquery(String.class);
      Root<Customer> sqc = sq.from(Customer.class);
      sq.where(cbuilder.isNull(sqc.get(Customer_.name)));
      sq.select(sqc.get(Customer_.id));

      cquery.where(customer.get(Customer_.id).in(sq));
      cquery.select(customer);

      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      List<Customer> clist = tquery.getResultList();

      if (!checkEntityPK(clist, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.length + " references, got: " + clist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
    } else {
      TestUtil.logErr("Failed to get Non-null root");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("subqueryIsNull test failed");

    }

  }

  /*
   * @testName: pathGetPluralAttributeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1097;
   * 
   * @test_Strategy:
   *
   * SELECT c FROM Customer c WHERE ((SELECT COUNT(o.id) FROM Order o WHERE
   * (o.customer.id = c.ID)) > 1)
   * 
   */
  @SetupMethod(name = "setupOrderData")
  public void pathGetPluralAttributeTest() throws Fault {
    boolean pass = false;

    String[] expected = new String[2];
    expected[0] = customerRef[3].getId();
    expected[1] = customerRef[13].getId();

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Path<Customer> customer = cquery.from(Customer.class);
      cquery
          .where(cbuilder.gt(cbuilder.size(customer.get(Customer_.orders)), 1))
          .select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      List<Customer> clist = tquery.getResultList();

      if (!checkEntityPK(clist, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.length + " references, got: " + clist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);

    }

    if (!pass) {
      throw new Fault("pathGetPluralAttributeTest failed");
    }
  }

  /*
   * @testName: subquery
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:801; PERSISTENCE:JAVADOC:1172;
   *
   * @test_Strategy: Use LIKE expression in a sub query. Select the customers
   * with name like Caruso. The name Caruso is derived in the subquery.
   */
  @SetupMethod(name = "setupOrderData")
  public void subquery() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Order> cquery = qbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      cquery.select(order);
      // create correlated subquery
      Subquery<Customer> sq = cquery.subquery(Customer.class);
      Root<Order> sqo = sq.correlate(order);
      Join<Order, Customer> sqc = sqo.join("customer");
      sq.where(qbuilder.like(sqc.<String> get("name"), "%Caruso")).select(sqc);
      cquery.where(qbuilder.exists(sq));
      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      List<Order> result = tquery.getResultList();

      expectedPKs = new String[2];
      expectedPKs[0] = "7";
      expectedPKs[1] = "8";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("test_subquery_like:  Did not get expected "
            + " results.  Expected 2 references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception subquery: ", e);
    }

    if (!pass) {
      throw new Fault("subquery failed");
    }
  }

  /*
   * @testName: subqueryGroupByExpressionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1182; PERSISTENCE:JAVADOC:1192;
   *
   * @test_Strategy: Use LIKE expression in a sub query. Select the customers
   * with name like Caruso. The name Caruso is derived in the subquery.
   */
  @SetupMethod(name = "setupOrderData")
  public void subqueryGroupByExpressionTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    boolean pass4 = false;
    boolean pass5 = false;
    String expectedPKs[];

    CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Order> cquery = qbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      EntityType<Order> Order_ = order.getModel();

      cquery.select(order);

      Subquery<String> subquery = cquery.subquery(String.class);
      List<Expression<?>> gList = subquery.getGroupList();
      if (gList.size() == 0) {
        TestUtil.logTrace(
            "Received expected empty list from getGroupList() when there is no groupBy expressions");
        pass1 = true;
      } else {
        TestUtil.logErr(
            "Did not received empty list from getGroupList() when there is no groupBy expressions");
        for (Expression e : gList) {
          TestUtil.logErr("Item:" + e.toString());
        }
      }
      Expression sel = subquery.getSelection();
      if (sel == null) {
        TestUtil.logTrace(
            "Received expected null from getSelection() when there is no selection specified");
        pass2 = true;
      } else {
        TestUtil.logErr(
            "Did not received null from getSelection() when there is no selection specified:"
                + sel.toString());
      }
      Root<Customer> customer = subquery.from(Customer.class);
      EntityType<Customer> Customer_ = customer.getModel();
      subquery.select(
          customer.get(Customer_.getSingularAttribute("name", String.class)));
      sel = subquery.getSelection();
      if (sel != null) {
        TestUtil.logTrace("Received non-result from getSelection()");
        pass3 = true;
      } else {
        TestUtil.logErr(
            "Received null from getSelection() when there is a selection specified");
      }
      subquery.where(qbuilder.like(
          customer.get(Customer_.getSingularAttribute("name", String.class)),
          "%Caruso"));
      Expression exp = customer
          .get(Customer_.getSingularAttribute("name", String.class));

      subquery.groupBy(exp);
      gList = subquery.getGroupList();
      if (gList != null) {
        TestUtil.logTrace(
            "Received non-null from getGroupList() when there is groupBy expressions");
        if (gList.size() == 1) {
          TestUtil.logTrace("Received one groupBy expression");
          pass4 = true;
        } else {
          TestUtil.logErr(
              "Expected one groupBy expression, actual:" + gList.size());

          for (Expression e : gList) {
            TestUtil.logErr("Did not get expected result:" + e);
          }
        }
      } else {

        TestUtil.logErr(
            "Received null from getGroupList() when there is groupBy expressions");
      }
      cquery.where(
          order.get(Order_.getSingularAttribute("customer", Customer.class))
              .get(Customer_.getSingularAttribute("name", String.class))
              .in(subquery));

      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      List<Order> result = tquery.getResultList();

      expectedPKs = new String[2];
      expectedPKs[0] = "7";
      expectedPKs[1] = "8";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("Did not get expected "
            + " results.  Expected 2 references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass5 = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: ", e);

    }

    if (!pass1 || !pass2 || !pass3 || !pass4 || !pass5) {
      throw new Fault("subqueryGroupByExpressionTest failed");
    }
  }

  /*
   * @testName: subqueryGroupByExpressionArrayTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1198;
   *
   * @test_Strategy: Use groupBy expression in a sub query. Select the customers
   * with name like Caruso. The name Caruso is derived in the subquery.
   */
  @SetupMethod(name = "setupOrderData")
  public void subqueryGroupByExpressionArrayTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Order> cquery = qbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      EntityType<Order> Order_ = order.getModel();

      cquery.select(order);

      Subquery<String> subquery = cquery.subquery(String.class);
      Root<Customer> customer = subquery.from(Customer.class);
      EntityType<Customer> Customer_ = customer.getModel();

      subquery.select(
          customer.get(Customer_.getSingularAttribute("id", String.class)));
      subquery.where(qbuilder.like(
          customer.get(Customer_.getSingularAttribute("name", String.class)),
          "%Caruso"));
      Expression[] exp = {
          customer.get(Customer_.getSingularAttribute("id", String.class)) };

      subquery.groupBy(exp);

      cquery.where(
          order.get(Order_.getSingularAttribute("customer", Customer.class))
              .get(Customer_.getSingularAttribute("id", String.class))
              .in(subquery));
      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      List<Order> result = tquery.getResultList();

      expectedPKs = new String[2];
      expectedPKs[0] = "7";
      expectedPKs[1] = "8";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("Did not get expected "
            + " results.  Expected 2 references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception subquery: ", e);
    }

    if (!pass) {
      throw new Fault("subqueryGroupByExpressionArrayTest failed");
    }
  }

  /*
   * @testName: subqueryGroupByListTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1183; PERSISTENCE:JAVADOC:1199;
   *
   * @test_Strategy: Use groupBy expression in a sub query. Select the customers
   * with name like Caruso. The name Caruso is derived in the subquery.
   */
  @SetupMethod(name = "setupOrderData")
  public void subqueryGroupByListTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Order> cquery = qbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      EntityType<Order> Order_ = order.getModel();

      cquery.select(order);

      Subquery<String> subquery = cquery.subquery(String.class);
      Root<Customer> customer = subquery.from(Customer.class);
      EntityType<Customer> Customer_ = customer.getModel();

      subquery.select(
          customer.get(Customer_.getSingularAttribute("id", String.class)));
      subquery.where(qbuilder.like(
          customer.get(Customer_.getSingularAttribute("name", String.class)),
          "%Caruso"));
      List list = new ArrayList();
      list.add(
          customer.get(Customer_.getSingularAttribute("id", String.class)));

      subquery.groupBy(list);

      cquery.where(
          order.get(Order_.getSingularAttribute("customer", Customer.class))
              .get(Customer_.getSingularAttribute("id", String.class))
              .in(subquery));
      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      List<Order> result = tquery.getResultList();

      expectedPKs = new String[2];
      expectedPKs[0] = "7";
      expectedPKs[1] = "8";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("Did not get expected "
            + " results.  Expected 2 references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception subquery: ", e);
    }

    if (!pass) {
      throw new Fault("subqueryGroupByListTest failed");
    }
  }

  /*
   * @testName: getRoots
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:941;
   *
   * @test_Strategy: convert the following JPQL to CriteriaQuery
   *
   */
  public void getRoots() throws Fault {
    boolean pass = false;

    try {
      CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();

      CriteriaQuery<Tuple> cquery = qbuilder.createTupleQuery();
      if (cquery != null) {

        cquery.from(Customer.class);
        cquery.from(Order.class);

        Set<Root<?>> rootSet = cquery.getRoots();

        if (rootSet != null) {

          if (rootSet.size() == 2) {
            int count = 0;
            boolean foundCustomer = false;
            boolean foundOrder = false;
            for (Root newRoot : rootSet) {
              EntityType eType1 = newRoot.getModel();
              String name = eType1.getName();
              TestUtil.logTrace("entityType Name = " + name);
              if (name.equals("Customer")) {
                TestUtil.logTrace("Received expected name:" + name);
                foundCustomer = true;
                count++;
              }
              if (name.equals("Order")) {
                TestUtil.logTrace("Received expected name:" + name);
                foundOrder = true;
                count++;
              }
            }
            if (count == 2 && foundCustomer && foundOrder) {
              pass = true;
            } else {
              TestUtil.logErr(
                  "Did not get Customer and Order roots back from getRoot");
            }
          } else {
            TestUtil.logErr("getRoots did not return 2 entries in the set");
          }

        } else {
          TestUtil.logErr("getRoots returned null");

        }

      } else {
        TestUtil.logErr("Failed to get Non-null Criteria Query");
      }

    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault("getRoots test failed");
    }
  }

  /*
   * @testName: getSelection
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1167; PERSISTENCE:JAVADOC:942
   * 
   * @test_Strategy:
   *
   *
   */
  public void getSelection() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    try {

      CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();

      TestUtil.logMsg("Testing non-compound selection");

      CriteriaQuery<Customer> cquery = qbuilder.createQuery(Customer.class);

      if (cquery != null) {
        Root<Customer> customer = cquery.from(Customer.class);
        cquery.select(customer);
        Selection<Customer> _select = cquery.getSelection();
        if (_select != null) {
          if (!_select.isCompoundSelection()) {
            TestUtil.logTrace("isCompoundSelection returned expected false");
            pass1 = true;
          } else {
            TestUtil
                .logErr("isCompoundSelection returned true instead of false");
          }

          String javaName = _select.getJavaType().getName();
          if (javaName
              .equals("com.sun.ts.tests.jpa.common.schema30.Customer")) {
            pass2 = true;
          } else {
            TestUtil.logErr(
                "Expected: com.sun.ts.tests.jpa.common.schema30.Customer, actual:"
                    + javaName);
          }
        } else {
          TestUtil.logErr("get Selection returned null");
        }

        TestUtil.logMsg("Testing compound selection");
        CriteriaQuery cquery1 = qbuilder.createQuery();
        customer = cquery1.from(Customer.class);
        EntityType<Customer> CUSTOMER_ = customer.getModel();
        cquery1.multiselect(
            customer.get(CUSTOMER_.getSingularAttribute("id", String.class)),
            customer.get(CUSTOMER_.getSingularAttribute("name", String.class)));

        Selection _select1 = cquery1.getSelection();
        if (_select1 != null) {
          if (_select1.isCompoundSelection()) {
            TestUtil.logTrace("isCompoundSelection returned expected true");
            pass3 = true;
          } else {
            TestUtil.logErr("isCompoundSelection returned false");
          }
        } else {
          TestUtil.logErr("get Selection returned null");
        }
      } else {
        TestUtil.logErr("Failed to get Non-null Criteria Query");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception ", e);
    }
    if (!pass1 || !pass2 || !pass3) {
      throw new Fault("getSelection failed");

    }
  }

  /*
   * @testName: getGroupList
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1129; PERSISTENCE:JAVADOC:937
   *
   * @test_Strategy: select c.country.code FROM Customer c GROUP BY
   * c.country.code"
   */
  public void getGroupList() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;

    try {
      CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();

      CriteriaQuery<Customer> cquery = qbuilder.createQuery(Customer.class);
      if (cquery != null) {
        Root<Customer> customer = cquery.from(Customer.class);

        TestUtil.logMsg("Testing with NO group expressions");

        List<Expression<?>> groupList = cquery.getGroupList();
        if (groupList != null) {
          if (groupList.size() == 0) {
            TestUtil.logTrace("Received empty list from getGroupList");
            pass1 = true;
          } else {
            TestUtil.logErr("Expected : 0" + " Received :" + groupList.size());
            for (Expression strExpr : groupList) {
              TestUtil.logErr("Expression:" + strExpr.toString());
            }

          }
        } else {
          TestUtil.logErr(
              "getGroupList returned null instead of empty list when no groupby expressions have been specified");

        }
        TestUtil.logMsg("Testing with group expressions");

        Expression e = customer.get("name");
        cquery.groupBy(e);

        groupList = cquery.getGroupList();
        if (groupList != null) {
          if (groupList.size() == 1) {
            for (Expression strExpr : groupList) {
              String sType = strExpr.getJavaType().getName();
              if (sType.equals("java.lang.String")) {
                TestUtil.logTrace("Received expected type:" + sType);
                pass2 = true;

              } else {
                TestUtil
                    .logErr("Expected type: java.lang.String, actual:" + sType);

              }
            }
          } else {
            TestUtil.logErr("Expected : 1" + " Received :" + groupList.size());
            for (Expression strExpr : groupList) {
              TestUtil.logErr("Actual expression:" + strExpr.toString());
            }

          }
        } else {
          TestUtil.logErr(
              "getGroupList returned null instead of a populated list when groupby expressions have been specified");

        }
      } else {
        TestUtil.logErr("Failed to get Non-null Criteria Query");

      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception groupBy: " + e);

    }

    if (!pass1 || !pass2) {
      throw new Fault("getGroupList failed");
    }
  }

  /*
   * @testName: getGroupRestriction
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:938
   *
   * @test_Strategy:
   */

  @SetupMethod(name = "setupCustomerData")
  public void getGroupRestriction() throws Fault {
    boolean pass = false;

    try {
      CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();

      CriteriaQuery cquery = qbuilder.createQuery();
      if (cquery != null) {
        Root<Customer> customer = cquery.from(Customer.class);
        Predicate[] pred = {
            qbuilder.like(customer.get(Customer_.name), "K%") };
        cquery.having(pred);
        Predicate restriction = cquery.getGroupRestriction();
        if (restriction != null) {
          cquery.groupBy(customer.get("name"));
          EntityType<Customer> Customer_ = customer.getModel();

          cquery.select(customer
              .get(Customer_.getSingularAttribute("name", String.class)));

          List<String> actual = getEntityManager().createQuery(cquery)
              .getResultList();

          List<String> expected = new ArrayList<String>();
          expected.add(customerRef[5].getName());
          expected.add(customerRef[9].getName());
          expected.add(customerRef[13].getName());

          if (expected.containsAll(actual) && actual.containsAll(expected)
              && expected.size() == actual.size()) {

            TestUtil.logTrace("Received expected results");
            pass = true;
          } else {
            TestUtil.logErr("Did not get expected results");
            for (String s : expected) {
              TestUtil.logErr("expected:" + s);
            }
            for (String s : actual) {
              TestUtil.logErr("actual:" + s);
            }
          }

        } else {
          TestUtil.logErr(
              "getGroupRestriction returned null instead of groupBy expressions");
        }
      } else {
        TestUtil.logErr("Failed to get Non-null Criteria Query");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception getGroupRestriction: " + e);
    }

    if (!pass) {
      throw new Fault("getGroupRestriction failed");
    }
  }

  /*
   * @testName: isDistinct
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:947
   *
   * @test_Strategy: Use Conjunction
   *
   *
   */
  public void isDistinct() throws Fault {
    boolean pass = false;

    try {
      CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();

      CriteriaQuery cquery = qbuilder.createQuery();
      if (cquery != null) {
        cquery.from(Customer.class);
        cquery.distinct(true);

        Boolean isDistinct = cquery.isDistinct();
        if (isDistinct) {
          pass = true;
        }

      } else {
        TestUtil.logErr("Failed to get Non-null Criteria Query");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected excetion: " + e);
    }

    if (!pass) {
      throw new Fault("isDistinct test failed");

    }
  }

  /*
   * @testName: getResultType
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:940
   *
   * @test_Strategy: Use Conjunction Select Distinct c FROM Customer c where
   * customer.name = 'Robert E. Bissett'
   *
   *
   */
  public void getResultType() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;

    try {
      CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();

      TestUtil.logMsg("Testing specific class return type");

      String expected = "com.sun.ts.tests.jpa.common.schema30.Customer";
      CriteriaQuery cquery = qbuilder.createQuery(Customer.class);
      if (cquery != null) {
        Class resultType = cquery.getResultType();
        if (resultType != null) {
          if (resultType.getName().equals(expected)) {
            TestUtil.logTrace("Got Expected Result Type");
            pass1 = true;
          } else {
            TestUtil.logErr(
                "Received  UnExpected Result Type :" + resultType.getName());
          }

        } else {
          TestUtil.logErr("getResultType returned null instead of:" + expected);
        }
      } else {
        TestUtil
            .logErr("Failed to get Non-null Criteria Query for:" + expected);
      }

      TestUtil.logMsg("Testing Tuple return type");
      expected = "javax.persistence.Tuple";

      cquery = qbuilder.createQuery(Tuple.class);
      if (cquery != null) {
        Class resultType = cquery.getResultType();

        if (resultType != null) {
          if (resultType.getName().equals(expected)) {
            TestUtil.logTrace("Got Expected Result Type");
            pass2 = true;

          } else {
            TestUtil.logErr(
                "Received  UnExpected Result Type :" + resultType.getName());
          }

        } else {
          TestUtil.logErr("getResultType returned null instead of:" + expected);
        }
      } else {
        TestUtil.logErr("getResultType returned null instead of:" + expected);
      }

      TestUtil.logMsg("Testing Object return type");
      expected = "java.lang.Object";

      cquery = qbuilder.createQuery();
      if (cquery != null) {
        Class resultType = cquery.getResultType();

        if (resultType != null) {
          if (resultType.getName().equals(expected)) {
            TestUtil.logTrace("Got Expected Result Type");
            pass3 = true;

          } else {
            TestUtil.logErr(
                "Received  UnExpected Result Type :" + resultType.getName());
          }

        } else {
          TestUtil.logErr("getResultType returned null instead of:" + expected);
        }
      } else {
        TestUtil.logErr("getResultType returned null instead of:" + expected);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception getGroupRestriction: " + e);
    }
    if (!pass1 || !pass2 || !pass3) {
      throw new Fault("getResultType test failed");

    }
  }

}
