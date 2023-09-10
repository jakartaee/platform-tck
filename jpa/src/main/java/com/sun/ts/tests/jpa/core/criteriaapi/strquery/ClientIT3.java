/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.criteriaapi.strquery;

import java.sql.Date;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.schema30.Address;
import com.sun.ts.tests.jpa.common.schema30.Alias;
import com.sun.ts.tests.jpa.common.schema30.CreditCard;
import com.sun.ts.tests.jpa.common.schema30.Customer;
import com.sun.ts.tests.jpa.common.schema30.LineItem;
import com.sun.ts.tests.jpa.common.schema30.Order;
import com.sun.ts.tests.jpa.common.schema30.Phone;
import com.sun.ts.tests.jpa.common.schema30.Product;
import com.sun.ts.tests.jpa.common.schema30.Spouse;
import com.sun.ts.tests.jpa.common.schema30.UtilCustomerData;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.persistence.metamodel.Attribute;


public class ClientIT3 extends UtilCustomerData {

  /* Run test */

  /*
   * @testName: queryTest61
   * 
   * @assertion_ids: PERSISTENCE:SPEC:778;PERSISTENCE:SPEC:780;
   * PERSISTENCE:SPEC:1714; PERSISTENCE:SPEC:1715;
   * 
   * @test_Strategy: Execute a query defining an identification variable for
   * c.work in an OUTER JOIN clause. The JOIN operation will include customers
   * without addresses. Verify the results are accurately returned.
   */
  @SetupMethod(name = "setupCustomerData")
  @Test
  public void queryTest61() throws Exception {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Address> w = customer.join("work", JoinType.LEFT);
      cquery.where(cbuilder.isNull(w.get("zip")));
      cquery.select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[3];
      expectedPKs[0] = "13";
      expectedPKs[1] = "19";
      expectedPKs[2] = "20";

      if (!checkEntityPK(clist, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 3 references, got: "
                + clist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);
    }

    if (!pass) {
      throw new Exception("queryTest61 failed");
    }
  }

  /*
   * @testName: queryTest64
   * 
   * @assertion_ids: PERSISTENCE:SPEC:372.5;PERSISTENCE:SPEC:817;
   * PERSISTENCE:SPEC:395
   * 
   * @test_Strategy: A constructor may be used in the SELECT list to return a
   * collection of Java instances. The specified class is not required to be an
   * entity or mapped to the database. The constructor name must be fully
   * qualified.
   *
   * Verify the results were accurately returned.
   */
  @SetupMethod(name = "setupCustomerData")
  @Test
  public void queryTest64() throws Exception {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery.where(cbuilder.equal(customer.get("work").get("city"),
          cbuilder.parameter(String.class, "workcity")));
      cquery.select(cbuilder.construct(Customer.class, customer.get("id"),
          customer.get("name")));
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setParameter("workcity", "Burlington");
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[18];
      expectedPKs[0] = "1";
      expectedPKs[1] = "2";
      expectedPKs[2] = "3";
      expectedPKs[3] = "4";
      expectedPKs[4] = "5";
      expectedPKs[5] = "6";
      expectedPKs[6] = "7";
      expectedPKs[7] = "8";
      expectedPKs[8] = "9";
      expectedPKs[9] = "10";
      expectedPKs[10] = "11";
      expectedPKs[11] = "12";
      expectedPKs[12] = "13";
      expectedPKs[13] = "14";
      expectedPKs[14] = "15";
      expectedPKs[15] = "16";
      expectedPKs[16] = "17";
      expectedPKs[17] = "18";

      if (!checkEntityPK(clist, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 18 references, got: "
                + clist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);
    }

    if (!pass) {
      throw new Exception("queryTest64 failed");
    }

  }

  /*
   * @testName: queryTest69
   * 
   * @assertion_ids: PERSISTENCE:SPEC:384; PERSISTENCE:SPEC:389;
   * PERSISTENCE:SPEC:406; PERSISTENCE:SPEC:824; PERSISTENCE:SPEC:392;
   * 
   * @test_Strategy: This test verifies the same results of two queries using
   * the keyword DISTINCT or not using DISTINCT in the query with the aggregate
   * keyword COUNT to verity the NULL values are eliminated before the aggregate
   * is applied.
   *
   */
  @SetupMethod(name = "setupCustomerData")
  @Test
  public void queryTest69() throws Exception {
    boolean pass1 = false;
    boolean pass2 = false;
    final Long expectedResult1 = Long.valueOf(17);
    final Long expectedResult2 = Long.valueOf(16);

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      CriteriaQuery<Long> cquery1 = cbuilder.createQuery(Long.class);
      Root<Customer> customer1 = cquery1.from(Customer.class);
      cquery1.select(cbuilder.count(customer1.get("home").get("city")));
      TypedQuery<Long> tquery1 = getEntityManager().createQuery(cquery1);
      Long result1 = (Long) tquery1.getSingleResult();

      if (!(result1.equals(expectedResult1))) {
        TestUtil.logErr("Query1 in queryTest69 returned:" + result1
            + " expected: " + expectedResult1);
      } else {
        TestUtil
            .logTrace("pass:  Query1 in queryTest69 returned expected results");
        pass1 = true;
      }
      CriteriaQuery<Long> cquery2 = cbuilder.createQuery(Long.class);
      Root<Customer> customer2 = cquery2.from(Customer.class);
      cquery2.select(cbuilder.countDistinct(customer2.get("home").get("city")));
      TypedQuery<Long> tquery2 = getEntityManager().createQuery(cquery2);
      Long result2 = (Long) tquery2.getSingleResult();

      if (!(result2.equals(expectedResult2))) {
        TestUtil.logErr("Query 2 in queryTest69 returned:" + result2
            + " expected: " + expectedResult2);
      } else {
        TestUtil.logTrace(
            "pass:  Query 2 in queryTest69 returned expected results");
        pass2 = true;
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);
    }

    if (!pass1 || !pass2) {
      throw new Exception("queryTest69 failed");
    }

  }

  /*
   * @testName: queryTest71
   * 
   * @assertion_ids: PERSISTENCE:SPEC:744;PERSISTENCE:JAVADOC:128
   * 
   * @test_Strategy: The NoResultException is thrown by the persistence provider
   * when Query.getSingleResult is invoked and there are not results to return.
   * Verify the results are accurately returned.
   */
  @SetupMethod(name = "setupCustomerData")
  @Test
  public void queryTest71() throws Exception {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("Check if a spouse is related to a customer");
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Spouse> spouse = cquery.from(Spouse.class);
      cquery.where(cbuilder.equal(spouse.get("id"), "7"))
          .select(spouse.<Customer> get("customer"));
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.getSingleResult();

      getEntityTransaction().commit();
    } catch (NoResultException e) {
      TestUtil
          .logTrace("queryTest71: NoResultException caught as expected : " + e);
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);
    }
    if (!pass) {
      throw new Exception("queryTest71 failed");
    }
  }

  /*
   * @testName: test_groupBy
   * 
   * @assertion_ids: PERSISTENCE:SPEC:810; PERSISTENCE:SPEC:756;
   * 
   * @test_Strategy: Test for Only Group By in a simple select statement.
   * Country is an Embeddable entity.
   *
   */
  @SetupMethod(name = "setupCustomerData")
  @Test
  public void test_groupBy() throws Exception {
    boolean pass = false;
    final String expectedCodes[] = new String[] { "CHA", "GBR", "IRE", "JPN",
        "USA" };

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery.select(customer.get("country").<String> get("code"));
      cquery.groupBy(customer.get("country").get("code"));
      TypedQuery<String> tquery = getEntityManager().createQuery(cquery);
      List<String> result = tquery.getResultList();

      String[] output = (String[]) (result.toArray(new String[result.size()]));
      Arrays.sort(output);

      pass = Arrays.equals(expectedCodes, output);

      if (!pass) {
        TestUtil
            .logErr("Did not get expected results.  Expected 4 Country Codes: "
                + "CHA, GBR, JPN, USA. Received: " + result.size());
        for (String s : result) {
          TestUtil.logErr(" Credit Card Type: " + s);
        }
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);
    }

    if (!pass) {
      throw new Exception("test_groupBy failed");
    }
  }

  /*
   * @testName: test_innerjoin_1x1
   * 
   * @assertion_ids: PERSISTENCE:SPEC:779; PERSISTENCE:SPEC:372;
   * PERSISTENCE:SPEC:372.2
   * 
   * @test_Strategy: Inner Join for 1-1 relationship. Select all customers with
   * spouses.
   */
  @SetupMethod(name = "setupCustomerData")
  @Test
  public void test_innerjoin_1x1() throws Exception {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      customer.join("spouse");
      cquery.select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      List<Customer> result = tquery.getResultList();

      expectedPKs = new String[5];
      expectedPKs[0] = "7";
      expectedPKs[1] = "10";
      expectedPKs[2] = "11";
      expectedPKs[3] = "12";
      expectedPKs[4] = "13";
      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 5 references, got: "
                + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);
    }

    if (!pass) {
      throw new Exception("test_innerjoin_1x1 failed");
    }
  }

  /*
   * @testName: test_fetchjoin_1x1
   * 
   * @assertion_ids: PERSISTENCE:SPEC:781; PERSISTENCE:SPEC:774;
   * PERSISTENCE:SPEC:776; PERSISTENCE:JAVADOC:978;
   * 
   * @test_Strategy: JOIN FETCH for 1-1 relationship. Prefetch the spouses for
   * all Customers.
   */
  @SetupMethod(name = "setupCustomerData")
  @Test
  public void test_fetchjoin_1x1() throws Exception {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      customer.fetch("spouse");
      cquery.select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      List<Customer> result = tquery.getResultList();

      expectedPKs = new String[5];
      expectedPKs[0] = "7";
      expectedPKs[1] = "10";
      expectedPKs[2] = "11";
      expectedPKs[3] = "12";
      expectedPKs[4] = "13";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil
            .logErr("Did not get expected results. Expected 5 references, got: "
                + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);
    }

    if (!pass) {
      throw new Exception("test_fetchjoin_1x1 failed");
    }
  }

  /*
   * @testName: test_fetchjoin_1xM
   * 
   * @assertion_ids: PERSISTENCE:SPEC:782; PERSISTENCE:SPEC:374;
   * PERSISTENCE:SPEC:777; PERSISTENCE:SPEC:783
   * 
   * @test_Strategy: Fetch Join for 1-M relationship. Retrieve customers from NY
   * or RI who have orders.
   *
   */
  @SetupMethod(name = "setupCustomerData")
  @Test
  public void test_fetchjoin_1xM() throws Exception {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery.where(customer.get("home").get("state").in("NY", "RI"));
      cquery.select(customer).distinct(true);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      List<Customer> result = tquery.getResultList();

      expectedPKs = new String[2];
      expectedPKs[0] = "14";
      expectedPKs[1] = "17";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 2 references, got: "
                + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);
    }

    if (!pass) {
      throw new Exception("test_fetchjoin_1xM failed");
    }
  }

  /*
   * @testName: test_groupByHaving
   * 
   * @assertion_ids: PERSISTENCE:SPEC:808; PERSISTENCE:SPEC:353;
   * PERSISTENCE:SPEC:757; PERSISTENCE:SPEC:391
   * 
   * @test_Strategy: Test for Group By and Having in a select statement Select
   * the count of customers in each country where Country is China, England
   */
  @SetupMethod(name = "setupCustomerData")
  @Test
  public void test_groupByHaving() throws Exception {
    boolean pass = false;
    final Long expectedGBR = Long.valueOf(2);
    final Long expectedCHA = Long.valueOf(4);

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Long> cquery = cbuilder.createQuery(Long.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery.groupBy(customer.get("country").get("code"))
          .having(customer.get("country").get("code").in("GBR", "CHA"))
          .select(cbuilder.count(customer));
      TypedQuery<Long> tquery = getEntityManager().createQuery(cquery);
      List<Long> result = tquery.getResultList();

      int numOfExpected = 0;
      TestUtil.logTrace("Check result received . . . ");
      for (Long l : result) {
        if ((l.equals(expectedGBR)) || (l.equals(expectedCHA))) {
          numOfExpected++;
        }
      }

      if (numOfExpected != 2) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 2 Values returned : "
                + "2 with Country Code GBR and 4 with Country Code CHA. "
                + "Received: " + result.size());
        for (Long l : result) {
          TestUtil.logTrace("count of Codes Returned: " + l);
        }
      } else {
        TestUtil.logTrace("Expected results received.");
        pass = true;
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);

    }

    if (!pass) {
      throw new Exception("test_groupByHaving failed");
    }
  }

  /*
   * @testName: test_concatHavingClause
   * 
   * @assertion_ids: PERSISTENCE:SPEC:807; PERSISTENCE:SPEC:803;
   * PERSISTENCE:SPEC:804; PERSISTENCE:SPEC:805; PERSISTENCE:SPEC:806;
   * PERSISTENCE:SPEC:734
   * 
   * @test_Strategy:Test for Functional Expression: concat in Having Clause Find
   * customer Margaret Mills by firstname-lastname concatenation.
   */
  @SetupMethod(name = "setupCustomerData")
  @Test
  public void test_concatHavingClause() throws Exception {
    boolean pass = false;
    String result;
    final String expectedCustomer = "Margaret Mills";

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery.select(customer.<String> get("name")).groupBy(customer.get("name"))
          .having(cbuilder.equal(customer.get("name"), cbuilder
              .concat("Margaret ", cbuilder.parameter(String.class, "lname"))));
      TypedQuery<String> tquery = getEntityManager().createQuery(cquery);
      tquery.setParameter("lname", "Mills");
      result = (String) tquery.getSingleResult();

      if (result.equals(expectedCustomer)) {
        TestUtil.logTrace("Expected results received");
        pass = true;
      } else {
        TestUtil
            .logTrace("test_concatHavingClause:  Did not get expected results. "
                + "Expected: " + expectedCustomer + ", got: " + result);
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);
    }

    if (!pass) {
      throw new Exception("test_concatHavingClause failed");
    }
  }

  /*
   * @testName: test_lowerHavingClause
   * 
   * @assertion_ids: PERSISTENCE:SPEC:807; PERSISTENCE:SPEC:369.10
   * 
   * @test_Strategy:Test for Functional Expression: lower in Having Clause
   * Select all customers in country with code GBR
   */
  @SetupMethod(name = "setupCustomerData")
  @Test
  public void test_lowerHavingClause() throws Exception {
    boolean pass = false;
    final Long expectedCount = Long.valueOf(2);

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Long> cquery = cbuilder.createQuery(Long.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery
          .select(cbuilder.count(customer.get("country").<String> get("code")))
          .groupBy(customer.get("country").get("code"))
          .having(cbuilder.equal(
              cbuilder.lower(customer.get("country").<String> get("code")),
              "gbr"));
      TypedQuery<Long> tquery = getEntityManager().createQuery(cquery);
      List<Long> result = tquery.getResultList();

      for (Long l : result) {
        if (l.equals(expectedCount)) {
          pass = true;
          TestUtil.logTrace("Expected results received");
          pass = true;
        } else {
          TestUtil.logErr(
              "Did not get expected results. Expected 2 references, got: "
                  + result.size());
        }
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);
    }

    if (!pass) {
      throw new Exception(" test_lowerHavingClause failed");
    }
  }

  /*
   * @testName: test_upperHavingClause
   * 
   * @assertion_ids: PERSISTENCE:SPEC:807; PERSISTENCE:SPEC:369.11
   * 
   * @test_Strategy:Test for Functional Expression: upper in Having Clause
   * Select all customers in country ENGLAND
   */
  @SetupMethod(name = "setupCustomerData")
  @Test
  public void test_upperHavingClause() throws Exception {
    boolean pass = false;
    final Long expectedCount = Long.valueOf(2);

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Long> cquery = cbuilder.createQuery(Long.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery
          .select(
              cbuilder.count(customer.get("country").<String> get("country")))
          .groupBy(customer.get("country").get("country"))
          .having(cbuilder.equal(
              cbuilder.upper(customer.get("country").<String> get("country")),
              "ENGLAND"));
      TypedQuery<Long> tquery = getEntityManager().createQuery(cquery);
      List<Long> result = tquery.getResultList();

      for (Long l : result) {
        if (l.equals(expectedCount)) {
          pass = true;
          TestUtil.logTrace("Expected results received");
          pass = true;
        } else {
          TestUtil.logErr(
              "Did not get expected results. Expected 2 references, got: "
                  + result.size());
        }
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);
    }

    if (!pass) {
      throw new Exception("test_upperHavingClause failed");
    }
  }

  /*
   * @testName: test_lengthHavingClause
   * 
   * @assertion_ids: PERSISTENCE:SPEC:807; PERSISTENCE:SPEC:369.4
   * 
   * @test_Strategy:Test for Functional Expression: length in Having Clause
   * Select all customer names having the length of the city of the home address
   * = 10
   */
  @SetupMethod(name = "setupCustomerData")
  @Test
  public void test_lengthHavingClause() throws Exception {
    boolean pass = false;
    final String[] expectedCities = new String[] { "Burlington", "Chelmsford",
        "Roslindale" };

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Address> a = customer.join("home");
      cquery.groupBy(a.get("city"))
          .having(cbuilder.equal(cbuilder.length(a.<String> get("city")), 10))
          .select(a.<String> get("city"));
      TypedQuery<String> tquery = getEntityManager().createQuery(cquery);
      List<String> result = tquery.getResultList();

      String[] output = (String[]) (result.toArray(new String[result.size()]));
      Arrays.sort(output);

      pass = Arrays.equals(expectedCities, output);

      if (!pass) {
        TestUtil
            .logErr("Did not get expected results.  Expected 2 Cities, got: "
                + result.size());
        for (String s : expectedCities) {
          TestUtil.logErr("Expected:" + s);
        }
        for (String s : output) {
          TestUtil.logErr("Actual:" + s);
        }
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);
    }

    if (!pass) {
      throw new Exception("test_lengthHavingClause failed");
    }
  }

  /*
   * @testName: test_locateHavingClause
   * 
   * @assertion_ids: PERSISTENCE:SPEC:807; PERSISTENCE:SPEC:369.3
   * 
   * @test_Strategy: Test for LOCATE expression in the Having Clause Select
   * customer names if there the string "Frechette" is located in the customer
   * name.
   */
  @SetupMethod(name = "setupCustomerData")
  @Test
  public void test_locateHavingClause() throws Exception {
    boolean pass = false;
    final String[] expectedCusts = new String[] { "Alan E. Frechette",
        "Arthur D. Frechette" };

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery.groupBy(customer.get("name"))
          .having(cbuilder.gt(
              cbuilder.locate(customer.<String> get("name"), "Frechette"), 0))
          .select(customer.<String> get("name"));
      TypedQuery<String> tquery = getEntityManager().createQuery(cquery);
      List<String> result = tquery.getResultList();

      String[] output = (String[]) (result.toArray(new String[result.size()]));
      Arrays.sort(output);

      pass = Arrays.equals(expectedCusts, output);

      if (!pass) {
        TestUtil
            .logErr("Did not get expected results.  Expected 2 Customers, got: "
                + result.size());
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);
    }

    if (!pass) {
      throw new Exception(" test_locateHavingClause failed");
    }
  }

  /*
   * @testName: test_subquery_in
   * 
   * @assertion_ids: PERSISTENCE:SPEC:800;PERSISTENCE:SPEC:801;
   * PERSISTENCE:SPEC:802; PERSISTENCE:SPEC:352.2; PERSISTENCE:JAVADOC:1133;
   * PERSISTENCE:JAVADOC:1130;
   * 
   * @test_Strategy: Use IN expression in a sub query.
   */
  @SetupMethod(name = "setupCustomerData")
  @Test
  public void test_subquery_in() throws Exception {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery.select(customer);
      Subquery<String> sq = cquery.subquery(String.class);
      boolean isCorr = customer.isCorrelated();
      if (!isCorr) {
        TestUtil.logTrace("Root.isCorrelated() return false");
      } else {
        TestUtil.logErr(
            "Expected Root.isCorrelated() to return false, actual:" + isCorr);
      }
      Root<Customer> sqc = sq.correlate(customer);
      isCorr = sqc.isCorrelated();
      if (isCorr) {
        TestUtil.logTrace("Root.isCorrelated() return true");
      } else {
        TestUtil.logErr(
            "Expected Root.isCorrelated() to return true, actual:" + isCorr);
      }
      From f = sqc.getCorrelationParent();
      String name = f.getJavaType().getSimpleName();
      if (name.equals("Customer")) {
        TestUtil.logTrace("Received expected parent:" + name);
      } else {
        TestUtil.logErr(
            "Expected getCorrelationParent() to return Customer, actual:"
                + name);
      }
      Join<Customer, Address> w = sqc.join("work");
      sq.select(w.<String> get("state"));
      sq.where(cbuilder.equal(w.get("state"),
          cbuilder.parameter(String.class, "state")));
      cquery.where(customer.get("home").get("state").in(sq));
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setParameter("state", "MA");
      List<Customer> result = tquery.getResultList();

      expectedPKs = new String[11];
      expectedPKs[0] = "1";
      expectedPKs[1] = "2";
      expectedPKs[2] = "3";
      expectedPKs[3] = "4";
      expectedPKs[4] = "7";
      expectedPKs[5] = "8";
      expectedPKs[6] = "9";
      expectedPKs[7] = "11";
      expectedPKs[8] = "13";
      expectedPKs[9] = "15";
      expectedPKs[10] = "18";
      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("test_subquery_in:  Did not get expected results. "
            + " Expected 11 references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);
    }

    if (!pass) {
      throw new Exception("test_subquery_in failed");
    }
  }

  /*
   * @testName: fromIsCorrelatedTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:986;
   * 
   * @test_Strategy:
   */
  @SetupMethod(name = "setupCustomerData")
  @Test
  public void fromIsCorrelatedTest() throws Exception {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      From<Customer, Customer> rCustomer = (From) customer;
      boolean isCorr = rCustomer.isCorrelated();
      if (!isCorr) {
        TestUtil.logTrace("isCorrelated() return false");
      } else {
        TestUtil.logErr(
            "Expected isCorrelated() to return false, actual:" + isCorr);
      }
      cquery.select(customer);
      Subquery<String> sq = cquery.subquery(String.class);

      From<Customer, Customer> sqc = sq.correlate(customer);
      isCorr = sqc.isCorrelated();
      if (isCorr) {
        TestUtil.logTrace("isCorrelated() return true");
      } else {
        TestUtil
            .logErr("Expected isCorrelated() to return true, actual:" + isCorr);
      }
      Join<Customer, Address> w = sqc.join("work");
      sq.select(w.<String> get("state"));
      sq.where(cbuilder.equal(w.get("state"),
          cbuilder.parameter(String.class, "state")));
      cquery.where(customer.get("home").get("state").in(sq));
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setParameter("state", "MA");
      List<Customer> result = tquery.getResultList();

      expectedPKs = new String[11];
      expectedPKs[0] = "1";
      expectedPKs[1] = "2";
      expectedPKs[2] = "3";
      expectedPKs[3] = "4";
      expectedPKs[4] = "7";
      expectedPKs[5] = "8";
      expectedPKs[6] = "9";
      expectedPKs[7] = "11";
      expectedPKs[8] = "13";
      expectedPKs[9] = "15";
      expectedPKs[10] = "18";
      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("test_subquery_in:  Did not get expected results. "
            + " Expected 11 references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);
    }

    if (!pass) {
      throw new Exception("fromIsCorrelatedTest failed");
    }
  }

  /*
   * @testName: fetchStringTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1021;
   * 
   * @test_Strategy: JOIN FETCH for 1-1 relationship. SELECT c FROM Customer c
   * JOIN fetch c.spouse
   */
  @SetupMethod(name = "setupCustomerData")
  @Test
  public void fetchStringTest() throws Exception {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      From<Customer, Customer> customer = cquery.from(Customer.class);
      customer.fetch("spouse");
      cquery.select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      List<Customer> result = tquery.getResultList();

      expectedPKs = new String[5];
      expectedPKs[0] = "7";
      expectedPKs[1] = "10";
      expectedPKs[2] = "11";
      expectedPKs[3] = "12";
      expectedPKs[4] = "13";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil
            .logErr("Did not get expected results. Expected 5 references, got: "
                + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);
    }

    if (!pass) {
      throw new Exception("fetchStringTest failed");
    }
  }

  /*
   * @testName: fetchStringJoinTypeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:980; PERSISTENCE:JAVADOC:1023;
   * PERSISTENCE:JAVADOC:1025; PERSISTENCE:JAVADOC:973;
   * 
   * @test_Strategy: JOIN FETCH for 1-1 relationship.
   *
   * SELECT c FROM Customer c INNER JOIN fetch c.spouse
   */
  @SetupMethod(name = "setupCustomerData")
  @Test
  public void fetchStringJoinTypeTest() throws Exception {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      From<Customer, Customer> customer = cquery.from(Customer.class);
      customer.fetch("spouse", JoinType.INNER);
      Set<Fetch<Customer, ?>> s = customer.getFetches();
      if (s.size() == 1) {
        TestUtil.logTrace("Received expected size:" + s.size());

        for (Fetch f : s) {
          String name = f.getAttribute().getName();
          if (name.equals("spouse")) {
            TestUtil.logTrace("Received expected attribute:" + name);
          } else {
            TestUtil.logErr("Expected attribute: spouse, actual:" + name);
          }
          JoinType type = f.getJoinType();
          if (type.equals(JoinType.INNER)) {
            TestUtil.logTrace("Received expected JoinType:" + type);
          } else {
            TestUtil.logErr(
                "Expected JoinType:" + JoinType.INNER + ", actual:" + type);
          }
        }
      } else {
        TestUtil.logErr("Expected size: 1, actual:" + s.size());
      }

      cquery.select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      List<Customer> result = tquery.getResultList();

      expectedPKs = new String[5];
      expectedPKs[0] = "7";
      expectedPKs[1] = "10";
      expectedPKs[2] = "11";
      expectedPKs[3] = "12";
      expectedPKs[4] = "13";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil
            .logErr("Did not get expected results. Expected 5 references, got: "
                + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);
    }

    if (!pass) {
      throw new Exception("fetchStringJoinTypeTest failed");
    }
  }

  /*
   * @testName: isNullOneToOneTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1684
   * 
   * @test_Strategy: Execute a query using the IS NULL comparison operator for a
   * single-valued object field Verify the results were accurately returned.
   */
  @SetupMethod(name = "setupCustomerData")
  @Test
  public void isNullOneToOneTest() throws Exception {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find All Customers who have a null work zip code");
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery.where(customer.get("home").isNull());
      cquery.select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[2];
      expectedPKs[0] = "19";
      expectedPKs[1] = "20";
      if (!checkEntityPK(clist, expectedPKs)) {
        TestUtil
            .logErr("Did not get expected results.  Expected 1 reference, got: "
                + clist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);

    }

    if (!pass) {
      throw new Exception("isNullOneToOneTest failed");
    }
  }

}
