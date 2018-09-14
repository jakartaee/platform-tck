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

package com.sun.ts.tests.jpa.core.criteriaapi.metamodelquery;

import com.sun.ts.lib.harness.CleanupMethod;
import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.schema30.*;
import com.sun.ts.tests.jpa.common.schema30.Order;

import javax.persistence.NoResultException;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.*;
import java.sql.Date;
import java.util.*;

/**
 * @author Sarada Kommalapati
 */
public class Client extends Util {

  /* Run test */
  /*
   * @testName: queryTest1
   * 
   * @assertion_ids: PERSISTENCE:SPEC:312; PERSISTENCE:SPEC:322;
   * PERSISTENCE:SPEC:602; PERSISTENCE:SPEC:603; PERSISTENCE:JAVADOC:91;
   * PERSISTENCE:SPEC:785; PERSISTENCE:SPEC:1686; PERSISTENCE:SPEC:1687;
   * PERSISTENCE:SPEC:1688; PERSISTENCE:SPEC:1689; PERSISTENCE:SPEC:1691;
   * PERSISTENCE:SPEC:1692; PERSISTENCE:SPEC:1692.1; PERSISTENCE:SPEC:1692.2;
   * PERSISTENCE:SPEC:1692.3; PERSISTENCE:SPEC:1693; PERSISTENCE:SPEC:1693.1;
   * PERSISTENCE:SPEC:1693.2; PERSISTENCE:SPEC:1693.3; PERSISTENCE:SPEC:1693.4;
   * PERSISTENCE:SPEC:1695; PERSISTENCE:SPEC:1696;
   * 
   * @test_Strategy: This query is defined on a many-one relationship. Verify
   * the results were accurately returned.
   *
   */
  @SetupMethod(name = "setupOrderData")
  public void queryTest1() throws Fault {
    boolean pass = false;
    String expectedPKs[];
    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find All Orders for Customer: Robert E. Bissett");

      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);

      cquery
          .where(cbuilder.equal(order.get(Order_.customer).get(Customer_.name),
              cbuilder.parameter(String.class, "name")))
          .select(order);
      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      tquery.setParameter("name", "Robert E. Bissett");
      tquery.setMaxResults(orderRef.length);
      List<Order> olist = tquery.getResultList();

      expectedPKs = new String[2];
      expectedPKs[0] = "4";
      expectedPKs[1] = "9";
      if (!checkEntityPK(olist, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 2 references, got: "
                + olist.size());

      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest1 failed");
    }
  }

  /*
   * @testName: queryTest2
   * 
   * @assertion_ids: PERSISTENCE:SPEC:317.1; PERSISTENCE:SPEC:750;
   * PERSISTENCE:SPEC:764; PERSISTENCE:SPEC:746.1
   * 
   * @test_Strategy: Find All Customers. Verify the results were accurately
   * returned.
   * 
   */
  @SetupMethod(name = "setupCustomerData")
  public void queryTest2() throws Fault {
    boolean pass = false;
    String expectedPKs[];
    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("Execute findAllCustomers");
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> c = cquery.from(Customer.class);
      cquery.select(c);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[customerRef.length];
      for (int i = 0; i < customerRef.length; i++) {
        expectedPKs[i] = Integer.toString(i + 1);
      }

      if (!checkEntityPK(clist, expectedPKs)) {
        TestUtil.logErr("Did not get expected results.  Expected "
            + customerRef.length + " references, got: " + clist.size());

      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest2 failed");
    }
  }

  /*
   * @testName: queryTest3
   * 
   * @assertion_ids: PERSISTENCE:SPEC:321; PERSISTENCE:SPEC:317.2;
   * PERSISTENCE:SPEC:332; PERSISTENCE:SPEC:323; PERSISTENCE:SPEC:517;
   * PERSISTENCE:SPEC:518; PERSISTENCE:SPEC:519; PERSISTENCE:JAVADOC:93;
   * PERSISTENCE:JAVADOC:94; PERSISTENCE:JAVADOC:985; PERSISTENCE:JAVADOC:1132;
   * 
   * @test_Strategy: This query is defined on a many-many relationship. Verify
   * the results were accurately returned.
   */
  @SetupMethod(name = "setupAliasData")
  public void queryTest3() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = true;
    boolean pass4 = false;
    String expectedPKs[];
    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find All Customers with Alias: imc");
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Set set = customer.getJoins();
      if (set.size() != 0) {
        TestUtil.logErr("Expected getJoins to return 0, actual:" + set.size());
      } else {
        pass1 = true;
      }
      Join<Customer, Alias> alias = customer.join(Customer_.aliases);
      set = customer.getJoins();
      if (set.size() > 0) {
        for (Object o : set) {
          pass2 = true;
          Join j = (Join) o;
          String s = j.getAttribute().getName();
          if (s.equals("aliases")) {
            TestUtil.logTrace("Received expected join:aliases");
          } else {
            pass3 = false;
            TestUtil.logErr("Expected getJoins: value, actual:" + s);
          }
        }
      } else {
        TestUtil.logErr("Expected 1 join, actual:" + set.size());
      }
      cquery.where(cbuilder.equal(alias.get(Alias_.alias),
          cbuilder.parameter(String.class, "aName"))).select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setParameter("aName", "imc");
      tquery.setMaxResults(customerRef.length);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "8";
      if (!checkEntityPK(clist, expectedPKs)) {
        TestUtil
            .logErr("Did not get expected results.  Expected 1 reference, got: "
                + clist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass4 = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass1 || !pass2 || !pass3 || !pass4) {
      throw new Fault("queryTest3 failed");
    }
  }

  /*
   * @testName: queryTest4
   * 
   * @assertion_ids: PERSISTENCE:SPEC:322; PERSISTENCE:SPEC:394;
   * PERSISTENCE:SPEC:751; PERSISTENCE:SPEC:753; PERSISTENCE:SPEC:754;
   * PERSISTENCE:SPEC:755
   * 
   * @test_Strategy: This query is defined on a one-one relationship and used
   * conditional AND in query. Verify the results were accurately returned.
   *
   */
  @SetupMethod(name = "setupCustomerData")
  public void queryTest4() throws Fault {
    boolean pass = false;
    Customer c;
    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      Customer expected = getEntityManager().find(Customer.class, "3");
      TestUtil.logTrace("find Customer with Home Address in Swansea");
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery.where(
          cbuilder.equal(customer.get(Customer_.home).get(Address_.street),
              cbuilder.parameter(String.class, "street")),
          cbuilder.equal(customer.get(Customer_.home).get(Address_.city),
              cbuilder.parameter(String.class, "city")),
          cbuilder.equal(customer.get(Customer_.home).get(Address_.state),
              cbuilder.parameter(String.class, "state")),
          cbuilder.equal(customer.get(Customer_.home).get(Address_.zip),
              cbuilder.parameter(String.class, "zip")))
          .select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setParameter("street", "125 Moxy Lane")
          .setParameter("city", "Swansea").setParameter("state", "MA")
          .setParameter("zip", "11345");
      c = tquery.getSingleResult();

      if (expected == c) {
        TestUtil.logTrace("Expected results received");
        pass = true;
      } else {
        TestUtil.logErr("Did not get expected results.");

      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest4 failed");
    }
  }

  /*
   * @testName: queryTest5
   * 
   * @assertion_ids: PERSISTENCE:SPEC:323; PERSISTENCE:SPEC:760;
   * PERSISTENCE:SPEC:761; PERSISTENCE:JAVADOC:989
   * 
   * @test_Strategy: Execute a query to find customers with a certain credit
   * card type. This query is defined on a one-many relationship. Verify the
   * results were accurately returned.
   * 
   */
  @SetupMethod(name = "setupOrderData")
  public void queryTest5() throws Fault {
    boolean pass = false;
    String expectedPKs[];
    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find all Customers with AXP Credit Cards");
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, CreditCard> a = customer.join(Customer_.creditCards);
      cquery.where(cbuilder.equal(a.get(CreditCard_.type),
          cbuilder.parameter(String.class, "ccard")));
      cquery.select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setParameter("ccard", "AXP");
      tquery.setMaxResults(customerRef.length);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[7];
      expectedPKs[0] = "1";
      expectedPKs[1] = "4";
      expectedPKs[2] = "5";
      expectedPKs[3] = "8";
      expectedPKs[4] = "9";
      expectedPKs[5] = "12";
      expectedPKs[6] = "15";

      if (!checkEntityPK(clist, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 7 references, got: "
                + clist.size());

      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest5 failed");
    }
  }

  /*
   * @testName: queryTest6
   * 
   * @assertion_ids: PERSISTENCE:SPEC:348.4; PERSISTENCE:SPEC:338;
   * 
   * @test_Strategy: This query is defined on a one-one relationship using
   * conditional OR in query. Verify the results were accurately returned.
   * 
   */
  @SetupMethod(name = "setupCustomerData")
  public void queryTest6() throws Fault {
    boolean pass = false;
    String expectedPKs[];
    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find Customers with Home Address Information");

      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery.select(customer);
      cquery.where(cbuilder.or(
          cbuilder.equal(customer.get(Customer_.home).get(Address_.street),
              cbuilder.parameter(String.class, "street")),
          cbuilder.equal(customer.get(Customer_.home).get(Address_.city),
              cbuilder.parameter(String.class, "city")),
          cbuilder.equal(customer.get(Customer_.home).get(Address_.state),
              cbuilder.parameter(String.class, "state")),
          cbuilder.equal(customer.get(Customer_.home).get(Address_.zip),
              cbuilder.parameter(String.class, "zip"))));
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setParameter("street", "47 Skyline Drive");
      tquery.setParameter("city", "Chelmsford");
      tquery.setParameter("state", "VT");
      tquery.setParameter("zip", "02155");
      tquery.setMaxResults(customerRef.length);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[4];
      expectedPKs[0] = "1";
      expectedPKs[1] = "10";
      expectedPKs[2] = "11";
      expectedPKs[3] = "13";

      if (!checkEntityPK(clist, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 4 references, got: "
                + clist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest6 failed");
    }
  }

  /*
   * @testName: queryTest7
   * 
   * @assertion_ids: PERSISTENCE:SPEC:735; PERSISTENCE:SPEC:784;
   * 
   * @test_Strategy:
   *
   */
  @SetupMethod(name = "setupProductData")
  public void queryTest7() throws Fault {
    boolean pass = false;
    String expectedPKs[];
    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find All Products");
      CriteriaQuery<Product> cquery = cbuilder.createQuery(Product.class);
      Root<Product> product = cquery.from(Product.class);
      cquery.where(cbuilder.lt(product.get(Product_.quantity), 10));
      cquery.select(product);
      TypedQuery<Product> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(productRef.length);
      List<Product> plist = tquery.getResultList();

      /*
       * for (Product p : plist){
       * TestUtil.logTrace("id:"+p.getId()+", name:"+p.getName()+","+p.getClass(
       * ).getSimpleName()); }
       */
      expectedPKs = new String[2];
      expectedPKs[0] = "15";
      expectedPKs[1] = "21";

      if (!checkEntityPK(plist, expectedPKs)) {
        TestUtil.logErr("Did not get expected results.  Expected:"
            + expectedPKs.length + " references, got: " + plist.size());

      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest7 failed");
    }
  }

  /*
   * @testName: queryTest8
   * 
   * @assertion_ids: PERSISTENCE:SPEC:348.4; PERSISTENCE:SPEC:345
   * 
   * @test_Strategy: Execute a query containing a conditional expression
   * composed with logical operator NOT. Verify the results were accurately
   * returned.
   *
   */
  @SetupMethod(name = "setupOrderData")
  public void queryTest8() throws Fault {
    boolean pass = false;
    String expectedPKs[];
    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "find all orders where the total price is NOT less than $4500");
      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      cquery.where(cbuilder.lt(order.get(Order_.totalPrice), 4500).not());
      cquery.select(order);

      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(orderRef.length);
      List<Order> olist = tquery.getResultList();

      expectedPKs = new String[3];
      expectedPKs[0] = "5";
      expectedPKs[1] = "11";
      expectedPKs[2] = "16";
      if (!checkEntityPK(olist, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 3 references, got: "
                + olist.size());

      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest8 failed");
    }
  }

  /*
   * @testName: queryTest9
   * 
   * @assertion_ids: PERSISTENCE:SPEC:348.4; PERSISTENCE:SPEC:345
   * 
   * @test_Strategy: Execute a query containing a a conditional expression
   * composed with logical operator OR. Verify the results were accurately
   * returned.
   * 
   */
  @SetupMethod(name = "setupOrderData")
  public void queryTest9() throws Fault {
    boolean pass = false;
    String expectedPKs[];
    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil
          .logTrace("find all orders where the customer name is Karen R. Tegan"
              + " OR the total price is less than $100");
      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      cquery.where(cbuilder.or(
          cbuilder.equal(order.get(Order_.customer).get(Customer_.name),
              "Karen R. Tegan"),
          cbuilder.lt(order.get(Order_.totalPrice), 100)));
      cquery.select(order);
      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(orderRef.length);
      List<Order> olist = tquery.getResultList();

      expectedPKs = new String[5];
      expectedPKs[0] = "6";
      expectedPKs[1] = "9";
      expectedPKs[2] = "10";
      expectedPKs[3] = "12";
      expectedPKs[4] = "13";
      if (!checkEntityPK(olist, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 5 references, got: "
                + olist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest9 failed");
    }
  }

  /*
   * @testName: queryTest10
   * 
   * @assertion_ids: PERSISTENCE:SPEC:346; PERSISTENCE:SPEC:347;
   * PERSISTENCE:SPEC:348.2; PERSISTENCE:SPEC:344
   * 
   * @test_Strategy: Execute a query containing a conditional expression
   * composed with AND and OR and using standard bracketing () for ordering. The
   * comparison operator < and arithmetic operations are also used in the query.
   * Verify the results were accurately returned.
   * 
   */
  @SetupMethod(name = "setupOrderData")
  public void queryTest10() throws Fault {
    boolean pass = false;
    String expectedPKs[];
    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find all orders where line item quantity is 1 AND the"
          + " order total less than 100 or customer name is Robert E. Bissett");
      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      Join<Order, LineItem> l = order.join(Order_.lineItemsCollection);
      cquery
          .where(
              cbuilder.or(
                  cbuilder
                      .and(cbuilder.lt(l.get(LineItem_.quantity), 2),
                          cbuilder
                              .lt(order.get(Order_.totalPrice),
                                  cbuilder
                                      .sum(
                                          cbuilder.sum(cbuilder.literal(3),
                                              cbuilder.prod(
                                                  cbuilder.literal(54), 2)),
                                          -8))),
                  cbuilder.equal(order.get(Order_.customer).get(Customer_.name),
                      "Robert E. Bissett")));
      cquery.select(order).distinct(true);
      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(orderRef.length);
      List<Order> olist = tquery.getResultList();

      expectedPKs = new String[4];
      expectedPKs[0] = "4";
      expectedPKs[1] = "9";
      expectedPKs[2] = "12";
      expectedPKs[3] = "13";
      if (!checkEntityPK(olist, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 4 references, got: "
                + olist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest10 failed");
    }
  }

  /*
   * @testName: queryTest11
   * 
   * @assertion_ids: PERSISTENCE:SPEC:338;
   * 
   * @test_Strategy: Execute the findOrdersByQuery9 method using conditional
   * expression composed with AND with an input parameter as a conditional
   * factor. The comparison operator < is also used in the query. Verify the
   * results were accurately returned. //CHANGE THIS TO INPUT/NAMED PARAMETER
   * 
   */
  @SetupMethod(name = "setupOrderData")
  public void queryTest11() throws Fault {
    boolean pass = false;
    String expectedPKs[];
    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find all orders with line item quantity < 2"
          + " for customer Robert E. Bissett");
      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      Join<Order, LineItem> l = order.join(Order_.lineItemsCollection);
      cquery.where(cbuilder.and(cbuilder.lt(l.get(LineItem_.quantity), 2),
          cbuilder.equal(order.get(Order_.customer).get(Customer_.name),
              "Robert E. Bissett")));
      cquery.select(order).distinct(true);
      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(orderRef.length);
      List<Order> olist = tquery.getResultList();

      expectedPKs = new String[2];
      expectedPKs[0] = "4";
      expectedPKs[1] = "9";
      if (!checkEntityPK(olist, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 2 references, got: "
                + olist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest11 failed");
    }
  }

  /*
   * @testName: queryTest12
   * 
   * @assertion_ids: PERSISTENCE:SPEC:349; PERSISTENCE:SPEC:348.3;
   * PERSISTENCE:JAVADOC:744
   * 
   * @test_Strategy: Execute a query containing the comparison operator BETWEEN.
   * Verify the results were accurately returned.
   * 
   */
  @SetupMethod(name = "setupOrderData")
  public void queryTest12() throws Fault {
    boolean pass = false;
    String expectedPKs[];
    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "find all orders with a total price BETWEEN $1000 and $1200");
      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      cquery.where(cbuilder.between(order.get(Order_.totalPrice), 1000D, 1200D))
          .select(order);
      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(orderRef.length);
      List<Order> olist = tquery.getResultList();

      expectedPKs = new String[5];
      expectedPKs[0] = "1";
      expectedPKs[1] = "3";
      expectedPKs[2] = "7";
      expectedPKs[3] = "8";
      expectedPKs[4] = "14";
      if (!checkEntityPK(olist, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 5 references, got: "
                + olist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest12 failed");
    }
  }

  /*
   * @testName: queryTest13
   * 
   * @assertion_ids: PERSISTENCE:SPEC:349
   * 
   * @test_Strategy: Execute a query containing NOT BETWEEN. Verify the results
   * were accurately returned.
   */
  @SetupMethod(name = "setupOrderData")
  public void queryTest13() throws Fault {
    boolean pass = false;
    String expectedPKs[];
    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "find all orders with a total price NOT BETWEEN $1000 and $1200");
      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      cquery.where(
          cbuilder.between(order.get(Order_.totalPrice), 1000D, 1200D).not())
          .select(order);
      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(orderRef.length);
      List<Order> olist = tquery.getResultList();

      expectedPKs = new String[15];
      expectedPKs[0] = "2";
      expectedPKs[1] = "4";
      expectedPKs[2] = "5";
      expectedPKs[3] = "6";
      expectedPKs[4] = "9";
      expectedPKs[5] = "10";
      expectedPKs[6] = "11";
      expectedPKs[7] = "12";
      expectedPKs[8] = "13";
      expectedPKs[9] = "15";
      expectedPKs[10] = "16";
      expectedPKs[11] = "17";
      expectedPKs[12] = "18";
      expectedPKs[13] = "19";
      expectedPKs[14] = "20";
      if (!checkEntityPK(olist, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 15 references, got: "
                + olist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest13 failed");
    }
  }

  /*
   * @testName: queryTest14
   * 
   * @assertion_ids: PERSISTENCE:SPEC:345; PERSISTENCE:SPEC:334
   * 
   * @test_Strategy: Conditional expressions are composed of other conditional
   * expressions, comparison operators, logical operations, path expressions
   * that evaluate to boolean values and boolean literals.
   *
   * Execute a query method that contains a conditional expression with a path
   * expression that evaluates to a boolean literal. Verify the results were
   * accurately returned.
   */
  @SetupMethod(name = "setupOrderData")
  public void queryTest14() throws Fault {
    boolean pass = false;
    String expectedPKs[];
    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil
          .logTrace("find all orders that do not have approved Credit Cards");
      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      cquery.where(cbuilder
          .isFalse(order.get(Order_.creditCard).get(CreditCard_.approved)));
      cquery.select(order);
      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(orderRef.length);
      List<Order> olist = tquery.getResultList();

      expectedPKs = new String[6];
      expectedPKs[0] = "1";
      expectedPKs[1] = "7";
      expectedPKs[2] = "11";
      expectedPKs[3] = "13";
      expectedPKs[4] = "18";
      expectedPKs[5] = "20";
      if (!checkEntityPK(olist, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 6 references, got: "
                + olist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest14 failed");
    }
  }

  /*
   * @testName: queryTest15
   * 
   * @assertion_ids: PERSISTENCE:SPEC:330;
   * 
   * @test_Strategy: Execute a query method with a string literal enclosed in
   * single quotes (the string includes a single quote) in the conditional
   * expression of the WHERE clause. Verify the results were accurately
   * returned.
   */
  @SetupMethod(name = "setupCustomerData")
  public void queryTest15() throws Fault {
    boolean pass = false;
    Customer c;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      Customer expected = getEntityManager().find(Customer.class, "5");
      TestUtil.logTrace("find customer with name: Stephen S. D'Milla");
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery.where(cbuilder.equal(customer.get(Customer_.name),
          cbuilder.parameter(String.class, "cName")));
      cquery.select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setParameter("cName", "Stephen S. D'Milla");
      c = tquery.getSingleResult();

      if (expected == c) {
        TestUtil.logTrace("Expected results received");
        pass = true;
      } else {
        TestUtil.logErr("Did not get expected results.");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest15 failed");
    }
  }

  /*
   * @testName: queryTest16
   * 
   * @assertion_ids: PERSISTENCE:SPEC:352; PERSISTENCE:SPEC:348.3
   * 
   * @test_Strategy: Execute a query method using comparison operator IN in a
   * comparison expression within the WHERE clause. Verify the results were
   * accurately returned.
   */
  @SetupMethod(name = "setupCustomerData")
  public void queryTest16() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find all customers IN home city: Lexington");
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery.where(
          customer.get(Customer_.home).get(Address_.city).in("Lexington"));
      cquery.select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "2";
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
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest16 failed");
    }
  }

  /*
   * @testName: queryTest17
   * 
   * @assertion_ids: PERSISTENCE:SPEC:352; PERSISTENCE:SPEC:353;
   * PERSISTENCE:JAVADOC:988
   * 
   * @test_Strategy: Execute a query using comparison operator NOT IN in a
   * comparison expression within the WHERE clause. Verify the results were
   * accurately returned.
   */
  @SetupMethod(name = "setupCustomerData")
  public void queryTest17() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "find all customers NOT IN home city: Swansea or Brookline");
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Address> h = customer.join(Customer_.home, JoinType.LEFT);
      cquery.where(h.get(Address_.city).in("Swansea", "Brookline").not());
      cquery.select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[15];
      expectedPKs[0] = "1";
      expectedPKs[1] = "2";
      expectedPKs[2] = "5";
      expectedPKs[3] = "6";
      expectedPKs[4] = "7";
      expectedPKs[5] = "8";
      expectedPKs[6] = "10";
      expectedPKs[7] = "11";
      expectedPKs[8] = "12";
      expectedPKs[9] = "13";
      expectedPKs[10] = "14";
      expectedPKs[11] = "15";
      expectedPKs[12] = "16";
      expectedPKs[13] = "17";
      expectedPKs[14] = "18";

      if (!checkEntityPK(clist, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 15 references, got: "
                + clist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest17 failed");
    }
  }

  /*
   * @testName: queryTest18
   * 
   * @assertion_ids: PERSISTENCE:SPEC:358; PERSISTENCE:SPEC:348.3
   * 
   * @test_Strategy: Execute a query using the comparison operator LIKE in a
   * comparison expression within the WHERE clause. The pattern-value includes a
   * percent character. Verify the results were accurately returned.
   */
  @SetupMethod(name = "setupCustomerData")
  public void queryTest18() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil
          .logTrace("find All Customers with home ZIP CODE that ends in 77");
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery.where(
          cbuilder.like(customer.get(Customer_.home).get(Address_.zip), "%77"));
      cquery.select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "2";
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
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest18 failed");
    }
  }

  /*
   * @testName: queryTest19
   * 
   * @assertion_ids: PERSISTENCE:SPEC:358; PERSISTENCE:SPEC:348.3
   * 
   * @test_Strategy: Execute a query using the comparison operator NOT LIKE in a
   * comparison expression within the WHERE clause. The pattern-value includes a
   * percent character and an underscore. Verify the results were accurately
   * returned.
   *
   */
  @SetupMethod(name = "setupCustomerData")
  public void queryTest19() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "find all customers with a home zip code that does not contain"
              + " 44 in the third and fourth position");
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery.where(cbuilder
          .notLike(customer.get(Customer_.home).get(Address_.zip), "%44_"));
      cquery.select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[15];
      expectedPKs[0] = "1";
      expectedPKs[1] = "2";
      expectedPKs[2] = "3";
      expectedPKs[3] = "4";
      expectedPKs[4] = "5";
      expectedPKs[5] = "9";
      expectedPKs[6] = "10";
      expectedPKs[7] = "11";
      expectedPKs[8] = "12";
      expectedPKs[9] = "13";
      expectedPKs[10] = "14";
      expectedPKs[11] = "15";
      expectedPKs[12] = "16";
      expectedPKs[13] = "17";
      expectedPKs[14] = "18";
      if (!checkEntityPK(clist, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 15 references, got: "
                + clist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest19 failed");
    }
  }

  /*
   * @testName: queryTest20
   * 
   * @assertion_ids: PERSISTENCE:SPEC:361; PERSISTENCE:SPEC:348.3;
   * 
   * @test_Strategy: Execute a query using the comparison operator IS EMPTY.
   * Verify the results were accurately returned.
   *
   * 
   */
  @SetupMethod(name = "setupAliasData")
  public void queryTest20() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find all customers who do not have aliases");
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery.where(cbuilder.isEmpty(customer.get(Customer_.aliases)));
      cquery.select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[7];
      expectedPKs[0] = "6";
      expectedPKs[1] = "15";
      expectedPKs[2] = "16";
      expectedPKs[3] = "17";
      expectedPKs[4] = "18";
      expectedPKs[5] = "19";
      expectedPKs[6] = "20";

      if (!checkEntityPK(clist, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 7 references, got: "
                + clist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest20 failed");
    }
  }

  /*
   * @testName: queryTest21
   * 
   * @assertion_ids: PERSISTENCE:SPEC:361; PERSISTENCE:SPEC:348.3
   * 
   * @test_Strategy: Execute a query using the comparison operator IS NOT EMPTY.
   * Verify the results were accurately returned.
   */
  @SetupMethod(name = "setupAliasData")
  public void queryTest21() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find all customers who have aliases");
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery.where(cbuilder.isNotEmpty(customer.get(Customer_.aliases)));
      cquery.select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[13];
      expectedPKs[0] = "1";
      expectedPKs[1] = "2";
      expectedPKs[2] = "3";
      expectedPKs[3] = "4";
      expectedPKs[4] = "5";
      expectedPKs[5] = "7";
      expectedPKs[6] = "8";
      expectedPKs[7] = "9";
      expectedPKs[8] = "10";
      expectedPKs[9] = "11";
      expectedPKs[10] = "12";
      expectedPKs[11] = "13";
      expectedPKs[12] = "14";

      if (!checkEntityPK(clist, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 15 reference, got: "
                + clist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest21 failed");
    }
  }

  /*
   * @testName: queryTest22
   * 
   * @assertion_ids: PERSISTENCE:SPEC:359;
   * 
   * @test_Strategy: Execute a query using the IS NULL comparison operator in
   * the WHERE clause. Verify the results were accurately returned.
   */
  @SetupMethod(name = "setupCustomerData")
  public void queryTest22() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find All Customers who have a null work zip code");
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery.where(customer.get(Customer_.work).get(Address_.zip).isNull());
      cquery.select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "13";
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
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest22 failed");
    }
  }

  /*
   * @testName: queryTest23
   * 
   * @assertion_ids: PERSISTENCE:SPEC:359
   * 
   * @test_Strategy: Execute a query using the IS NOT NULL comparison operator
   * within the WHERE clause. Verify the results were accurately returned. (This
   * query is executed against non-NULL data. For NULL data, see test
   * queryTest47)
   */
  @SetupMethod(name = "setupCustomerData")
  public void queryTest23() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "find all customers who do not have null work zip code entry");
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery.where(customer.get(Customer_.work).get(Address_.zip).isNotNull());
      cquery.select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[17];
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
      expectedPKs[12] = "14";
      expectedPKs[13] = "15";
      expectedPKs[14] = "16";
      expectedPKs[15] = "17";
      expectedPKs[16] = "18";

      if (!checkEntityPK(clist, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 17 references, got: "
                + clist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest23 failed");
    }
  }

  /*
   * @testName: queryTest24
   * 
   * @assertion_ids: PERSISTENCE:SPEC:369.1
   * 
   * @test_Strategy: Execute a query which includes the string function CONCAT
   * in a functional expression within the WHERE clause. Verify the results were
   * accurately returned.
   */
  @SetupMethod(name = "setupAliasOnlyData")
  public void queryTest24() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find all aliases who have match: stevie");
      CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
      Root<Alias> alias = cquery.from(Alias.class);
      cquery.where(cbuilder.equal(alias.get(Alias_.alias),
          cbuilder.concat(cbuilder.literal("ste"), "vie")));
      cquery.select(alias);
      TypedQuery<Alias> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(aliasRef.length);
      List<Alias> alist = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "14";
      if (!checkEntityPK(alist, expectedPKs)) {
        TestUtil
            .logErr("Did not get expected results.  Expected 1 reference, got: "
                + alist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest24 failed");
    }
  }

  /*
   * @testName: queryTest25
   * 
   * @assertion_ids: PERSISTENCE:SPEC:369.2
   * 
   * @test_Strategy: Execute a query which includes the string function
   * SUBSTRING in a functional expression within the WHERE clause. Verify the
   * results were accurately returned.
   */
  @SetupMethod(name = "setupAliasOnlyData")
  public void queryTest25() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find all aliases containing the substring: iris");
      CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
      Root<Alias> alias = cquery.from(Alias.class);
      cquery.where(cbuilder.equal(alias.get(Alias_.alias),
          cbuilder.substring(cbuilder.parameter(String.class, "string1"),
              cbuilder.parameter(Integer.class, "int2"),
              cbuilder.parameter(Integer.class, "int3"))));
      cquery.select(alias);
      TypedQuery<Alias> tquery = getEntityManager().createQuery(cquery);
      tquery.setParameter("string1", "iris");
      tquery.setParameter("int2", Integer.valueOf(1));
      tquery.setParameter("int3", Integer.valueOf(4));
      tquery.setMaxResults(aliasRef.length);
      List<Alias> alist = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "20";
      if (!checkEntityPK(alist, expectedPKs)) {
        TestUtil
            .logErr("Did not get expected results.  Expected 1 reference, got: "
                + alist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest25 failed");
    }
  }

  /*
   * @testName: queryTest26
   * 
   * @assertion_ids: PERSISTENCE:SPEC:369.4
   * 
   * @test_Strategy: Execute a query which includes the string function LENGTH
   * in a functional expression within the WHERE clause. Verify the results were
   * accurately returned.
   */
  @SetupMethod(name = "setupAliasOnlyData")
  public void queryTest26() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "find aliases whose alias name is greater than 4 characters");
      CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
      Root<Alias> alias = cquery.from(Alias.class);
      cquery.where(cbuilder.gt(cbuilder.length(alias.get(Alias_.alias)), 4));
      cquery.select(alias);
      TypedQuery<Alias> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(aliasRef.length);
      List<Alias> alist = tquery.getResultList();

      expectedPKs = new String[7];
      expectedPKs[0] = "8";
      expectedPKs[1] = "10";
      expectedPKs[2] = "13";
      expectedPKs[3] = "14";
      expectedPKs[4] = "18";
      expectedPKs[5] = "28";
      expectedPKs[6] = "29";
      if (!checkEntityPK(alist, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 7 references, got: "
                + alist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest26 failed");
    }
  }

  /*
   * @testName: queryTest27
   * 
   * @assertion_ids: PERSISTENCE:SPEC:369.5
   * 
   * @test_Strategy: Execute a query which includes the arithmetic function ABS
   * in a functional expression within the WHERE clause. Verify the results were
   * accurately returned.
   */
  @SetupMethod(name = "setupOrderData")
  public void queryTest27() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find all Orders with a total price greater than 1180");
      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      cquery.where(cbuilder.lt(cbuilder.parameter(Double.class, "dbl"),
          cbuilder.abs(order.get(Order_.totalPrice))));
      cquery.select(order);
      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      tquery.setParameter("dbl", 1180D);
      tquery.setMaxResults(orderRef.length);
      List<Order> olist = tquery.getResultList();

      expectedPKs = new String[9];
      expectedPKs[0] = "1";
      expectedPKs[1] = "2";
      expectedPKs[2] = "4";
      expectedPKs[3] = "5";
      expectedPKs[4] = "6";
      expectedPKs[5] = "11";
      expectedPKs[6] = "16";
      expectedPKs[7] = "17";
      expectedPKs[8] = "18";
      if (!checkEntityPK(olist, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 9 references, got: "
                + olist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest27 failed");
    }
  }

  /*
   * @testName: queryTest28
   * 
   * @assertion_ids: PERSISTENCE:SPEC:369.3
   * 
   * @test_Strategy: Execute a query which includes the string function LOCATE
   * in a functional expression within the WHERE clause. Verify the results were
   * accurately returned.
   */
  @SetupMethod(name = "setupAliasOnlyData")
  public void queryTest28() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "find all aliases who contain the string: ev in their alias name");
      CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
      Root<Alias> alias = cquery.from(Alias.class);
      cquery.where(
          cbuilder.equal(cbuilder.locate(alias.get(Alias_.alias), "ev"), 3));
      cquery.select(alias);
      TypedQuery<Alias> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(aliasRef.length);
      List<Alias> alist = tquery.getResultList();

      expectedPKs = new String[3];
      expectedPKs[0] = "13";
      expectedPKs[1] = "14";
      expectedPKs[2] = "18";
      if (!checkEntityPK(alist, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 3 references, got: "
                + alist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest28 failed");
    }
  }

  /*
   * @testName: queryTest29
   * 
   * @assertion_ids: PERSISTENCE:SPEC:363.1; PERSISTENCE:SPEC:365
   * 
   * @test_Strategy: Execute a query using the comparison operator MEMBER OF in
   * a collection member expression. Verify the results were accurately
   * returned.
   */
  @SetupMethod(name = "setupOrderData")
  public void queryTest29() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("finding a line item from order 1");

      LineItem li = getEntityManager().find(LineItem.class, "1");
      TestUtil.logTrace("LineItem found was:" + li.toString());

      TestUtil.logTrace("find line items who are members of line item found");
      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);

      cquery
          .where(cbuilder.isMember(li, order.get(Order_.lineItemsCollection)));
      cquery.select(order);
      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      List<Order> actual = tquery.getResultList();

      if (TestUtil.traceflag) {
        for (Order o : actual) {
          TestUtil.logTrace("actual:" + o.toString());
          for (LineItem li2 : o.getLineItemsCollection()) {
            TestUtil.logTrace("   LineItems:" + li2.toString());

          }
        }
      }
      expectedPKs = new String[1];
      expectedPKs[0] = orderRef[0].getId();
      if (!checkEntityPK(actual, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 0 references, got: "
                + actual.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest29 failed");
    }
  }

  /*
   * @testName: queryTest30
   * 
   * @assertion_ids: PERSISTENCE:SPEC:363; PERSISTENCE:SPEC:365
   * 
   * @test_Strategy: Execute a query using the comparison operator NOT MEMBER in
   * a collection member expression. Verify the results were accurately
   * returned.
   */
  @SetupMethod(name = "setupOrderData")
  public void queryTest30() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("finding a line item from order 11");

      LineItem li = getEntityManager().find(LineItem.class, "11");
      TestUtil.logTrace("LineItem found was:" + li.toString());

      TestUtil.logTrace(
          "find orders who have line items that are not members of the line item found");
      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);

      cquery.where(
          cbuilder.isNotMember(li, order.get(Order_.lineItemsCollection)));
      cquery.select(order);
      cquery.distinct(true);
      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      List<Order> actual = tquery.getResultList();

      Collections.sort(actual);

      expectedPKs = new String[19];
      expectedPKs[0] = orderRef[0].getId();
      expectedPKs[1] = orderRef[1].getId();
      expectedPKs[2] = orderRef[2].getId();
      expectedPKs[3] = orderRef[4].getId();
      expectedPKs[4] = orderRef[5].getId();
      expectedPKs[5] = orderRef[6].getId();
      expectedPKs[6] = orderRef[7].getId();
      expectedPKs[7] = orderRef[8].getId();
      expectedPKs[8] = orderRef[9].getId();
      expectedPKs[9] = orderRef[10].getId();
      expectedPKs[10] = orderRef[11].getId();
      expectedPKs[11] = orderRef[12].getId();
      expectedPKs[12] = orderRef[13].getId();
      expectedPKs[13] = orderRef[14].getId();
      expectedPKs[14] = orderRef[15].getId();
      expectedPKs[15] = orderRef[16].getId();
      expectedPKs[16] = orderRef[17].getId();
      expectedPKs[17] = orderRef[18].getId();
      expectedPKs[18] = orderRef[19].getId();

      if (!checkEntityPK(actual, expectedPKs)) {
        TestUtil.logErr("Did not get expected results.  Expected:"
            + expectedPKs.length + " references, got: " + actual.size());
        if (TestUtil.traceflag) {
          for (Order o : actual) {
            TestUtil.logTrace("actual:" + o.toString());
            for (LineItem li2 : o.getLineItemsCollection()) {
              TestUtil.logTrace("   LineItems:" + li2.toString());
            }
          }
        }
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }
    if (!pass) {
      throw new Fault("queryTest30 failed");
    }
  }

  /*
   * @testName: queryTest31
   * 
   * @assertion_ids: PERSISTENCE:SPEC:358; PERSISTENCE:JAVADOC:805
   * 
   * @test_Strategy: Execute a query using the comparison operator LIKE in a
   * comparison expression within the WHERE clause. The optional ESCAPE syntax
   * is used to escape the underscore. Verify the results were accurately
   * returned.
   *
   */
  @SetupMethod(name = "setupAliasData")
  public void queryTest31() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find all customers with an alias LIKE: sh_ll");
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Alias> a = customer.join(Customer_.aliases);
      cquery.where(cbuilder.like(a.get(Alias_.alias), "sh\\_ll", '\\'));
      cquery.select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "3";
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
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest31 failed");
    }
  }

  /*
   * @testName: likeExpExpCharTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:803
   * 
   * @test_Strategy: Execute a query using the comparison operator LIKE in a
   * comparison expression within the WHERE clause. The optional ESCAPE syntax
   * is used to escape the underscore. Verify the results were accurately
   * returned.
   *
   */
  @SetupMethod(name = "setupAliasData")
  public void likeExpExpCharTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find all customers with an alias LIKE: sh_ll");
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Alias> a = customer.join(Customer_.aliases);
      cquery.where(cbuilder.like(a.get(Alias_.alias),
          cbuilder.literal("sh\\_ll"), '\\'));
      cquery.select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "3";
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
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("likeExpExpCharTest failed");
    }
  }

  /*
   * @testName: likeExpExpExpTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:802
   * 
   * @test_Strategy: Execute a query using the comparison operator LIKE in a
   * comparison expression within the WHERE clause. The optional ESCAPE syntax
   * is used to escape the underscore. Verify the results were accurately
   * returned.
   *
   */
  @SetupMethod(name = "setupAliasData")
  public void likeExpExpExpTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find all customers with an alias LIKE: sh_ll");
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Alias> a = customer.join(Customer_.aliases);
      cquery.where(cbuilder.like(a.get(Alias_.alias),
          cbuilder.literal("sh\\_ll"), cbuilder.literal('\\')));
      cquery.select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "3";
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
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("likeExpExpExpTest failed");
    }
  }

  /*
   * @testName: likeExpStringExpTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:804
   * 
   * @test_Strategy: Execute a query using the comparison operator LIKE in a
   * comparison expression within the WHERE clause. The optional ESCAPE syntax
   * is used to escape the underscore. Verify the results were accurately
   * returned.
   *
   */
  @SetupMethod(name = "setupAliasData")
  public void likeExpStringExpTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find all customers with an alias LIKE: sh_ll");
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Alias> a = customer.join(Customer_.aliases);
      cquery.where(cbuilder.like(a.get(Alias_.alias), "sh\\_ll",
          cbuilder.literal('\\')));
      cquery.select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "3";
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
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("likeExpStringExpTest failed");
    }
  }

  /*
   * @testName: queryTest32
   * 
   * @assertion_ids: PERSISTENCE:SPEC:363
   * 
   * @test_Strategy: Execute a query using the comparison operator MEMBER in a
   * collection member expression with an identification variable and omitting
   * the optional reserved word OF. Verify the results were accurately returned.
   */
  @SetupMethod(name = "setupOrderData")
  public void queryTest32() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "find all orders where line items are members of the orders");
      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      Root<LineItem> l = cquery.from(LineItem.class);
      cquery.where(cbuilder.isMember(l, order.get(Order_.lineItemsCollection)));
      cquery.select(order).distinct(true);
      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(orderRef.length);
      List<Order> olist = tquery.getResultList();

      expectedPKs = new String[orderRef.length];
      for (int i = 0; i < orderRef.length; i++) {
        expectedPKs[i] = Integer.toString(i + 1);
      }

      if (!checkEntityPK(olist, expectedPKs)) {
        TestUtil.logErr("Did not get expected results.  Expected "
            + orderRef.length + "references, got: " + olist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest32 failed");
    }
  }

  /*
   * @testName: queryTest33
   * 
   * @assertion_ids: PERSISTENCE:SPEC:352.1
   * 
   * @test_Strategy: Execute a query using the comparison operator NOT MEMBER in
   * a collection member expression with input parameter omitting the optional
   * use of 'OF'. Verify the results were accurately returned.
   */
  @SetupMethod(name = "setupOrderData")
  public void queryTest33() throws Fault {
    boolean pass = false;
    String expectedPKs[];
    LineItem liDvc;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "find orders whose orders are do NOT contain the specified line items");
      liDvc = getEntityManager().find(LineItem.class, "30");
      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      cquery.where(
          cbuilder.isNotMember(cbuilder.parameter(LineItem.class, "liDvc"),
              order.get(Order_.lineItemsCollection)));
      cquery.select(order);
      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      tquery.setParameter("liDvc", liDvc);
      tquery.setMaxResults(orderRef.length);
      List<Order> olist = tquery.getResultList();

      expectedPKs = new String[19];
      expectedPKs[0] = "1";
      expectedPKs[1] = "2";
      expectedPKs[2] = "3";
      expectedPKs[3] = "4";
      expectedPKs[4] = "5";
      expectedPKs[5] = "7";
      expectedPKs[6] = "8";
      expectedPKs[7] = "9";
      expectedPKs[8] = "10";
      expectedPKs[9] = "11";
      expectedPKs[10] = "12";
      expectedPKs[11] = "13";
      expectedPKs[12] = "14";
      expectedPKs[13] = "15";
      expectedPKs[14] = "16";
      expectedPKs[15] = "17";
      expectedPKs[16] = "18";
      expectedPKs[17] = "19";
      expectedPKs[18] = "20";

      if (!checkEntityPK(olist, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 19 references, got: "
                + olist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;

      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest33 failed");
    }
  }

  /*
   * @testName: queryTest34
   * 
   * @assertion_ids: PERSISTENCE:SPEC:363.1; PERSISTENCE:JAVADOC:783
   * 
   * @test_Strategy: Execute a query using the comparison operator MEMBER OF in
   * a collection member expression using
   * single_valued_association_path_expression. Verify the results were
   * accurately returned.
   */
  @SetupMethod(name = "setupOrderData")
  public void queryTest34() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find orders who have Samples in their orders");
      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      cquery.where(cbuilder.isMember(order.get(Order_.sampleLineItem),
          order.get(Order_.lineItemsCollection)));
      cquery.select(order);
      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(orderRef.length);
      List<Order> olist = tquery.getResultList();

      expectedPKs = new String[2];
      expectedPKs[0] = "1";
      expectedPKs[1] = "6";
      if (!checkEntityPK(olist, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 2 references, got: "
                + olist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;

      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest34 failed");
    }
  }

  /*
   * @testName: queryTest35
   * 
   * @assertion_ids: PERSISTENCE:SPEC:352
   * 
   * @test_Strategy: Execute a query using comparison operator NOT IN in a
   * comparison expression within the WHERE clause where the value for the
   * state_field_path_expression contains numeric values. Verify the results
   * were accurately returned.
   */
  @SetupMethod(name = "setupOrderData")
  public void queryTest35() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "find all orders which contain lineitems not of quantities 1 or 5");
      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      Join<Order, LineItem> l = order.join(Order_.lineItemsCollection);
      cquery.where(l.get(LineItem_.quantity).in(1, 5).not());
      cquery.select(order).distinct(true);
      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(orderRef.length);
      List<Order> olist = tquery.getResultList();

      expectedPKs = new String[9];
      expectedPKs[0] = "10";
      expectedPKs[1] = "12";
      expectedPKs[2] = "14";
      expectedPKs[3] = "15";
      expectedPKs[4] = "16";
      expectedPKs[5] = "17";
      expectedPKs[6] = "18";
      expectedPKs[7] = "19";
      expectedPKs[8] = "20";
      if (!checkEntityPK(olist, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 9 references, got: "
                + olist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest35 failed");
    }
  }

  /*
   * @testName: queryTest36
   * 
   * @assertion_ids: PERSISTENCE:SPEC:352
   * 
   * @test_Strategy: Execute a query using comparison operator IN in a
   * conditional expression within the WHERE clause where the value for the IN
   * expression is an input parameter. Verify the results were accurately
   * returned.
   */
  @SetupMethod(name = "setupCustomerData")
  public void queryTest36() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find all customers who lives in city Attleboro");
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery.where(customer.get(Customer_.home).get(Address_.city)
          .in(cbuilder.parameter(String.class, "city")));
      cquery.select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setParameter("city", "Attleboro");
      tquery.setMaxResults(customerRef.length);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "13";
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
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest36 failed");
    }
  }

  /*
   * @testName: queryTest37
   * 
   * @assertion_ids: PERSISTENCE:SPEC:354; PERSISTENCE:JAVADOC:833
   * 
   * @test_Strategy: Execute two methods using the comparison operator IN in a
   * comparison expression within the WHERE clause and verify the results of the
   * two queries are equivalent regardless of the way the expression is
   * composed.
   */
  @SetupMethod(name = "setupCustomerData")
  public void queryTest37() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    String expectedPKs[];
    String expectedPKs2[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "Execute two queries composed differently and verify results");
      CriteriaQuery<Customer> cquery1 = cbuilder.createQuery(Customer.class);
      Root<Customer> customer1 = cquery1.from(Customer.class);
      cquery1
          .where(
              customer1.get(Customer_.home).get(Address_.state).in("NH", "RI"))
          .select(customer1);
      TypedQuery<Customer> tquery1 = getEntityManager().createQuery(cquery1);
      tquery1.setMaxResults(customerRef.length);
      List<Customer> clist1 = tquery1.getResultList();

      expectedPKs = new String[5];
      expectedPKs[0] = "5";
      expectedPKs[1] = "6";
      expectedPKs[2] = "12";
      expectedPKs[3] = "14";
      expectedPKs[4] = "16";

      CriteriaQuery<Customer> cquery2 = cbuilder.createQuery(Customer.class);
      Root<Customer> customer2 = cquery2.from(Customer.class);
      cquery2.where(cbuilder.or(
          cbuilder.equal(customer2.get(Customer_.home).get(Address_.state),
              "NH"),
          cbuilder.equal(customer2.get(Customer_.home).get(Address_.state),
              "RI")))
          .select(customer2);
      TypedQuery<Customer> tquery2 = getEntityManager().createQuery(cquery2);
      tquery2.setMaxResults(customerRef.length);
      List<Customer> clist2 = tquery2.getResultList();

      expectedPKs2 = new String[5];
      expectedPKs2[0] = "5";
      expectedPKs2[1] = "6";
      expectedPKs2[2] = "12";
      expectedPKs2[3] = "14";
      expectedPKs2[4] = "16";

      if (!checkEntityPK(clist1, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results for first query.  Expected 5 reference, got: "
                + clist1.size());
      } else {
        TestUtil.logTrace("Expected results received for first query");
        pass1 = true;
      }

      if (!checkEntityPK(clist2, expectedPKs2)) {
        TestUtil.logErr(
            "Did not get expected results for second query.  Expected 5 reference, got: "
                + clist2.size());
      } else {
        TestUtil.logTrace("Expected results received for second query");
        pass2 = true;
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass1 || !pass2) {
      throw new Fault("queryTest37 failed");
    }
  }

  /*
   * @testName: queryTest38
   * 
   * @assertion_ids: PERSISTENCE:SPEC:369.7
   * 
   * @test_Strategy: Execute a query which includes the arithmetic function MOD
   * in a functional expression within the WHERE clause. Verify the results were
   * accurately returned.
   */
  @SetupMethod(name = "setupProductData")
  public void queryTest38() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find orders that have the quantity of 50 available");
      CriteriaQuery<Product> cquery = cbuilder.createQuery(Product.class);
      Root<Product> product = cquery.from(Product.class);
      cquery.where(cbuilder.equal(cbuilder.mod(cbuilder.literal(550), 100),
          product.get(Product_.quantity)));
      cquery.select(product);
      TypedQuery<Product> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(productRef.length);
      List<Product> plist = tquery.getResultList();

      expectedPKs = new String[2];
      expectedPKs[0] = "5";
      expectedPKs[1] = "20";
      if (!checkEntityPK(plist, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 2 references, got: "
                + plist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest38 failed");
    }
  }

  /*
   * @testName: queryTest39
   * 
   * @assertion_ids: PERSISTENCE:SPEC:369.6; PERSISTENCE:SPEC:814;
   * PERSISTENCE:SPEC:816
   * 
   * @test_Strategy: Execute a query which includes the arithmetic function SQRT
   * in a functional expression within the WHERE clause. Verify the results were
   * accurately returned.
   */
  @SetupMethod(name = "setupOrderData")
  public void queryTest39() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find customers with specific credit card balance");
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, CreditCard> b = customer.join(Customer_.creditCards);
      cquery.where(cbuilder.equal(cbuilder.sqrt(b.get(CreditCard_.balance)),
          cbuilder.parameter(Double.class, "dbl")));
      cquery.select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setParameter("dbl", 50D);
      tquery.setMaxResults(customerRef.length);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "3";

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
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest39 failed");
    }
  }

  /*
   * @testName: queryTest40
   * 
   * @assertion_ids: PERSISTENCE:SPEC:350
   * 
   * @test_Strategy: Execute two methods using the comparison operator BETWEEN
   * in a comparison expression within the WHERE clause and verify the results
   * of the two queries are equivalent regardless of the way the expression is
   * composed.
   */
  @SetupMethod(name = "setupProductData")
  public void queryTest40() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    String expectedPKs[];
    String expectedPKs2[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "Execute two queries composed differently and verify results"
              + " Execute Query 1");
      CriteriaQuery<Product> cquery1 = cbuilder.createQuery(Product.class);
      Root<Product> product1 = cquery1.from(Product.class);
      cquery1.where(cbuilder.between(product1.get(Product_.quantity), 10, 20));
      cquery1.select(product1);
      TypedQuery<Product> tquery1 = getEntityManager().createQuery(cquery1);
      tquery1.setMaxResults(productRef.length);
      List<Product> plist1 = tquery1.getResultList();

      expectedPKs = new String[7];
      expectedPKs[0] = "8";
      expectedPKs[1] = "9";
      expectedPKs[2] = "17";
      expectedPKs[3] = "27";
      expectedPKs[4] = "28";
      expectedPKs[5] = "31";
      expectedPKs[6] = "36";

      TestUtil.logTrace("Execute Query 2");
      CriteriaQuery<Product> cquery2 = cbuilder.createQuery(Product.class);
      Root<Product> product2 = cquery2.from(Product.class);
      cquery2
          .where(cbuilder.and(cbuilder.ge(product2.get(Product_.quantity), 10),
              cbuilder.le(product2.get(Product_.quantity), 20)))
          .select(product2);
      TypedQuery<Product> tquery2 = getEntityManager().createQuery(cquery2);
      tquery2.setMaxResults(productRef.length);
      List<Product> plist2 = tquery2.getResultList();

      expectedPKs2 = new String[7];
      expectedPKs2[0] = "8";
      expectedPKs2[1] = "9";
      expectedPKs2[2] = "17";
      expectedPKs2[3] = "27";
      expectedPKs2[4] = "28";
      expectedPKs2[5] = "31";
      expectedPKs2[6] = "36";

      if (!checkEntityPK(plist1, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results for first query in queryTest40. "
                + "  Expected 7 references, got: " + plist1.size());
      } else {
        TestUtil.logTrace(
            "Expected results received for first query in queryTest40.");
        pass1 = true;
      }

      if (!checkEntityPK(plist2, expectedPKs2)) {
        TestUtil.logErr(
            "Did not get expected results for second query in queryTest40. "
                + "  Expected 7 references, got: " + plist2.size());
      } else {
        TestUtil.logTrace(
            "Expected results received for second query in queryTest40.");
        pass2 = true;
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass1 || !pass2) {
      throw new Fault("queryTest40 failed");
    }
  }

  /*
   * @testName: queryTest41
   * 
   * @assertion_ids: PERSISTENCE:SPEC:350
   * 
   * @test_Strategy: Execute two methods using the comparison operator NOT
   * BETWEEN in a comparison expression within the WHERE clause and verify the
   * results of the two queries are equivalent regardless of the way the
   * expression is composed.
   */
  @SetupMethod(name = "setupProductData")
  public void queryTest41() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    String expectedPKs[];
    String expectedPKs2[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "Execute two queries composed differently and verify results"
              + " Execute first query");
      CriteriaQuery<Product> cquery1 = cbuilder.createQuery(Product.class);
      Root<Product> product1 = cquery1.from(Product.class);
      cquery1.where(
          cbuilder.between(product1.get(Product_.quantity), 20, 200).not());
      cquery1.select(product1);
      TypedQuery<Product> tquery1 = getEntityManager().createQuery(cquery1);
      tquery1.setMaxResults(productRef.length);
      List<Product> plist1 = tquery1.getResultList();

      expectedPKs = new String[10];
      expectedPKs[0] = "8";
      expectedPKs[1] = "9";
      expectedPKs[2] = "10";
      expectedPKs[3] = "11";
      expectedPKs[4] = "14";
      expectedPKs[5] = "15";
      expectedPKs[6] = "17";
      expectedPKs[7] = "21";
      expectedPKs[8] = "29";
      expectedPKs[9] = "31";

      if (TestUtil.traceflag) {
        Collections.sort(plist1);
        for (Product p : plist1) {
          TestUtil
              .logTrace("id:" + p.getId() + ", quantity:" + p.getQuantity());
        }
      }
      if (!checkEntityPK(plist1, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results for first query.  Expected 31 references, got: "
                + plist1.size());
      } else {
        TestUtil.logTrace("Expected results received for first query");
        pass1 = true;

      }
      TestUtil.logTrace("Execute second query");
      CriteriaQuery<Product> cquery2 = cbuilder.createQuery(Product.class);
      Root<Product> product2 = cquery2.from(Product.class);
      cquery2
          .where(cbuilder.or(cbuilder.lt(product2.get(Product_.quantity), 20),
              cbuilder.gt(product2.get(Product_.quantity), 200)))
          .select(product2);
      TypedQuery<Product> tquery2 = getEntityManager().createQuery(cquery2);
      tquery2.setMaxResults(productRef.length);
      List<Product> plist2 = tquery2.getResultList();

      expectedPKs2 = new String[10];
      expectedPKs2[0] = "8";
      expectedPKs2[1] = "9";
      expectedPKs2[2] = "10";
      expectedPKs2[3] = "11";
      expectedPKs2[4] = "14";
      expectedPKs2[5] = "15";
      expectedPKs2[6] = "17";
      expectedPKs2[7] = "21";
      expectedPKs2[8] = "29";
      expectedPKs2[9] = "31";

      if (TestUtil.traceflag) {
        Collections.sort(plist2);
        for (Product p : plist2) {
          TestUtil
              .logTrace("id:" + p.getId() + ", quantity:" + p.getQuantity());
        }
      }

      if (!checkEntityPK(plist2, expectedPKs2)) {
        TestUtil.logErr(
            "Did not get expected results for second query.  Expected 31 references, got: "
                + plist2.size());
      } else {
        TestUtil.logTrace("Expected results received for second query");
        pass2 = true;

      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass1 || !pass2) {
      throw new Fault("queryTest41 failed");
    }
  }

  /*
   * @testName: queryTest42
   * 
   * @assertion_ids: PERSISTENCE:SPEC:423
   * 
   * @test_Strategy: This tests that nulls are eliminated using a
   * single-valued_association_field with IS NOT NULL. Verify results are
   * accurately returned.
   *
   */
  @SetupMethod(name = "setupOrderData")
  public void queryTest42() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil
          .logTrace("find all orders where related customer name is not null");
      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      cquery.where(
          cbuilder.isNotNull(order.get(Order_.customer).get(Customer_.name)));
      cquery.select(order);
      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(orderRef.length);
      List<Order> olist = tquery.getResultList();

      expectedPKs = new String[19];
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
      expectedPKs[12] = "14";
      expectedPKs[13] = "15";
      expectedPKs[14] = "16";
      expectedPKs[15] = "17";
      expectedPKs[16] = "18";
      expectedPKs[17] = "19";
      expectedPKs[18] = "20";

      if (!checkEntityPK(olist, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 19 references, got: "
                + olist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest42 failed");
    }
  }

  /*
   * @testName: queryTest43
   * 
   * @assertion_ids: PERSISTENCE:SPEC:425
   * 
   * @test_Strategy: Execute a query using Boolean operator AND in a conditional
   * test ( False AND False = False) where the second condition is not NULL.
   *
   */
  @SetupMethod(name = "setupProductData")
  public void queryTest43() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil
          .logTrace("Check results of AND operator: False AND False = False");
      CriteriaQuery<Product> cquery = cbuilder.createQuery(Product.class);
      Root<Product> product = cquery.from(Product.class);
      cquery.where(cbuilder.and(
          cbuilder.gt(product.get(Product_.quantity),
              cbuilder.sum(cbuilder.literal(500),
                  cbuilder.parameter(Integer.class, "int1"))),
          cbuilder.isNull(product.get(Product_.partNumber))));
      cquery.select(product);
      TypedQuery<Product> tquery = getEntityManager().createQuery(cquery);
      tquery.setParameter("int1", Integer.valueOf(100));
      tquery.setMaxResults(productRef.length);
      List<Product> plist = tquery.getResultList();

      expectedPKs = new String[0];
      if (!checkEntityPK(plist, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 0 references, got: "
                + plist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest43 failed");
    }
  }

  /*
   * @testName: queryTest44
   * 
   * @assertion_ids: PERSISTENCE:SPEC:416
   * 
   * @test_Strategy: If an input parameter is NULL, comparison operations
   * involving the input parameter will return an unknown value.
   *
   */
  @SetupMethod(name = "setupProductData")
  public void queryTest44() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "provide a null value for a comparison operation and verify the results");
      CriteriaQuery<Product> cquery = cbuilder.createQuery(Product.class);
      Root<Product> product = cquery.from(Product.class);
      cquery.where(cbuilder.equal(product.get(Product_.name),
          cbuilder.parameter(String.class, "num1")));
      cquery.select(product);
      TypedQuery<Product> tquery = getEntityManager().createQuery(cquery);
      tquery.setParameter("num1", null);
      tquery.setMaxResults(productRef.length);
      List<Product> plist = tquery.getResultList();

      expectedPKs = new String[0];

      if (!checkEntityPK(plist, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 0 references, got: "
                + plist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest44 failed");
    }
  }

  /*
   * @testName: queryTest45
   * 
   * @assertion_ids: PERSISTENCE:SPEC:361
   * 
   * @test_Strategy: Execute a query using IS NOT EMPTY in a
   * collection_valued_association_field where the field is EMPTY.
   */
  @SetupMethod(name = "setupAliasData")
  public void queryTest45() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find customers whose id is greater than 1 "
          + "OR where the relationship is NOT EMPTY");
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery.where(
          cbuilder.or(cbuilder.isNotEmpty(customer.get(Customer_.aliasesNoop)),
              cbuilder.notEqual(customer.get(Customer_.id), "1")));
      cquery.select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[19];
      expectedPKs[0] = "2";
      expectedPKs[1] = "3";
      expectedPKs[2] = "4";
      expectedPKs[3] = "5";
      expectedPKs[4] = "6";
      expectedPKs[5] = "7";
      expectedPKs[6] = "8";
      expectedPKs[7] = "9";
      expectedPKs[8] = "10";
      expectedPKs[9] = "11";
      expectedPKs[10] = "12";
      expectedPKs[11] = "13";
      expectedPKs[12] = "14";
      expectedPKs[13] = "15";
      expectedPKs[14] = "16";
      expectedPKs[15] = "17";
      expectedPKs[16] = "18";
      expectedPKs[17] = "19";
      expectedPKs[18] = "20";
      if (!checkEntityPK(clist, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 19 references, got: "
                + clist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest45 failed");
    }
  }

  /*
   * @testName: queryTest47
   * 
   * @assertion_ids: PERSISTENCE:SPEC:376; PERSISTENCE:SPEC:401;
   * PERSISTENCE:SPEC:399.3; PERSISTENCE:SPEC:422; PERSISTENCE:SPEC:752;
   * PERSISTENCE:SPEC:753
   * 
   * @test_Strategy: The IS NOT NULL construct can be used to eliminate the null
   * values from the result set of the query. Verify the results are accurately
   * returned.
   */
  @SetupMethod(name = "setupCustomerData")
  public void queryTest47() throws Fault {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    final String[] expectedZips = new String[] { "00252", "00252", "00252",
        "00252", "00252", "00252", "00252", "00252", "00252", "00252", "00252",
        "00252", "00252", "00252", "00252", "00252", "11345" };
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find work zip codes that are not null");
      CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery
          .where(cbuilder
              .isNotNull(customer.get(Customer_.work).get(Address_.zip)))
          .select(customer.get(Customer_.work).get(Address_.zip)).orderBy(
              cbuilder.asc(customer.get(Customer_.work).get(Address_.zip)));
      TypedQuery<String> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<String> clist = tquery.getResultList();

      String[] result = (String[]) (clist.toArray(new String[clist.size()]));
      TestUtil.logTrace("Compare results of work zip codes");
      pass = Arrays.equals(expectedZips, result);
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest47 failed");
    }
  }

  /*
   * @testName: queryTest48
   * 
   * @assertion_ids: PERSISTENCE:SPEC:329; PERSISTENCE:SPEC:348.1;
   * PERSISTENCE:SPEC:399.1;
   * 
   * @test_Strategy: This query, which includes a null non-terminal
   * association-field, verifies the null is not included in the result set.
   */
  @SetupMethod(name = "setupOrderData")
  public void queryTest48() throws Fault {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    final Double[] expectedBalances = new Double[] { 400D, 500D, 750D, 1000D,
        1400D, 1500D, 2000D, 2500D, 4400D, 5000D, 5500D, 7000D, 7400D, 8000D,
        9500D, 13000D, 15000D, 23000D };
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find all credit card balances");
      CriteriaQuery<Double> cquery = cbuilder.createQuery(Double.class);
      Root<Order> order = cquery.from(Order.class);
      cquery.select(order.get(Order_.creditCard).get(CreditCard_.balance))
          .distinct(true);
      cquery.orderBy(
          cbuilder.asc(order.get(Order_.creditCard).get(CreditCard_.balance)));
      TypedQuery<Double> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(orderRef.length);
      Collection<Double> olist = tquery.getResultList();

      Double[] result = (olist.toArray(new Double[olist.size()]));

      if (TestUtil.traceflag) {
        for (Double d : olist) {
          TestUtil.logTrace("query results returned:  " + d);
        }
      }
      TestUtil.logTrace("Compare expected results to query results");
      pass = Arrays.equals(expectedBalances, result);

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest48 failed");
    }
  }

  /*
   * @testName: queryTest49
   * 
   * @assertion_ids: PERSISTENCE:SPEC:359
   * 
   * @test_Strategy: Use the operator IS NULL in a null comparison expression
   * using a single_valued_path_expression. Verify the results were accurately
   * returned.
   */
  @SetupMethod(name = "setupAliasData")
  public void queryTest49() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find All Customers who have a null relationship");
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Alias> a = customer.join(Customer_.aliases);
      cquery.where(cbuilder.isNull(a.get(Alias_.customerNoop)));
      cquery.select(customer).distinct(true);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[13];
      expectedPKs[0] = "1";
      expectedPKs[1] = "2";
      expectedPKs[2] = "3";
      expectedPKs[3] = "4";
      expectedPKs[4] = "5";
      expectedPKs[5] = "7";
      expectedPKs[6] = "8";
      expectedPKs[7] = "9";
      expectedPKs[8] = "10";
      expectedPKs[9] = "11";
      expectedPKs[10] = "12";
      expectedPKs[11] = "13";
      expectedPKs[12] = "14";

      if (!checkEntityPK(clist, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 13 references, got: "
                + clist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest49 failed");
    }
  }

  /*
   * @testName: queryTest50
   * 
   * @assertion_ids: PERSISTENCE:SPEC:358
   * 
   * @test_Strategy: Execute a query using the comparison operator LIKE in a
   * comparison expression within the WHERE clause using percent (%) to wild
   * card any expression including the optional ESCAPE syntax. Verify the
   * results were accurately returned.
   *
   */
  @SetupMethod(name = "setupAliasData")
  public void queryTest50() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "find all customers with an alias that contains an underscore");
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Alias> a = customer.join(Customer_.aliases);
      cquery.where(cbuilder.like(a.get(Alias_.alias), "%\\_%", '\\'));
      cquery.select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "3";
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
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest50 failed");
    }
  }

  /*
   * @testName: queryTest51
   * 
   * @assertion_ids: PERSISTENCE:SPEC:359
   * 
   * @test_Strategy: Use the operator IS NOT NULL in a null comparision
   * expression within the WHERE clause where the single_valued_path_expression
   * is NULL. Verify the results were accurately returned.
   * 
   */
  @SetupMethod(name = "setupAliasData")
  public void queryTest51() throws Fault {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find All Customers who do not have null relationship");
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Alias> a = customer.join(Customer_.aliases);
      cquery.where(cbuilder.isNotNull(a.get(Alias_.customerNoop)));
      cquery.select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> clist = tquery.getResultList();

      if (clist.size() != 0) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 0 references, got: "
                + clist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest51 failed");
    }
  }

  /*
   * @testName: queryTest52
   * 
   * @assertion_ids: PERSISTENCE:SPEC:424; PERSISTENCE:SPEC:789
   * 
   * @test_Strategy: Define a query using Boolean operator AND in a conditional
   * test ( True AND True = True) where the second condition is NULL. Verify the
   * results were accurately returned.
   */
  @SetupMethod(name = "setupCustomerData")
  public void queryTest52() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery.where(
          cbuilder.and(cbuilder.isNotNull(customer.get(Customer_.country)),
              cbuilder.equal(customer.get(Customer_.name),
                  cbuilder.parameter(String.class, "cName"))));
      cquery.select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setParameter("cName", "Shelly D. McGowan");
      tquery.setMaxResults(customerRef.length);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "3";
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
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest52 failed");
    }
  }

  /*
   * @testName: queryTest53
   * 
   * @assertion_ids: PERSISTENCE:SPEC:425
   * 
   * @test_Strategy: Define a query using Boolean operator OR in a conditional
   * test (True OR True = True) where the second condition is NULL. Verify the
   * results were accurately returned.
   */
  @SetupMethod(name = "setupAliasData")
  public void queryTest53() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("determine if customer has a NULL relationship");
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Alias> a = customer.join(Customer_.aliases);
      cquery.where(cbuilder.or(
          cbuilder.equal(customer.get(Customer_.name),
              cbuilder.parameter(String.class, "cName")),
          cbuilder.isNull(a.get(Alias_.customerNoop))));
      cquery.select(customer).distinct(true);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setParameter("cName", "Arthur D. Frechette");
      tquery.setMaxResults(customerRef.length);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[13];
      expectedPKs[0] = "1";
      expectedPKs[1] = "2";
      expectedPKs[2] = "3";
      expectedPKs[3] = "4";
      expectedPKs[4] = "5";
      expectedPKs[5] = "7";
      expectedPKs[6] = "8";
      expectedPKs[7] = "9";
      expectedPKs[8] = "10";
      expectedPKs[9] = "11";
      expectedPKs[10] = "12";
      expectedPKs[11] = "13";
      expectedPKs[12] = "14";

      if (!checkEntityPK(clist, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 13 references, got: "
                + clist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest53 failed");
    }
  }

  /*
   * @testName: queryTest54
   * 
   * @assertion_ids: PERSISTENCE:SPEC:426
   * 
   * @test_Strategy: Define a query using Boolean operator NOT in a conditional
   * test (NOT True = False) where the relationship is NOT NULL. Verify the
   * results were accurately returned.
   */
  @SetupMethod(name = "setupAliasData")
  public void queryTest54() throws Fault {
    boolean pass = false;
    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("determine if customers have a NULL relationship");
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Alias> a = customer.join(Customer_.aliases);
      cquery.where(cbuilder.isNotNull(a.get(Alias_.customerNoop)));
      cquery.select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> clist = tquery.getResultList();

      if (clist.size() != 0) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 0 references, got: "
                + clist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest54 failed");
    }
  }

  /*
   * @testName: queryTest55
   * 
   * @assertion_ids: PERSISTENCE:SPEC:358; PERSISTENCE:SPEC:1712;
   * PERSISTENCE:SPEC:1713
   * 
   * @test_Strategy: The LIKE expression uses an input parameter for the
   * condition. Verify the results were accurately returned.
   *
   */
  @SetupMethod(name = "setupPhoneData")
  public void queryTest55() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "determine which customers have an area code beginning with 9");
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Address, Phone> p = customer.join(Customer_.home)
          .join(Address_.phones);
      cquery.where(cbuilder.like(p.get(Phone_.area),
          cbuilder.parameter(String.class, "area")));
      cquery.select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setParameter("area", "9%");
      tquery.setMaxResults(customerRef.length);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[3];
      expectedPKs[0] = "3";
      expectedPKs[1] = "12";
      expectedPKs[2] = "16";

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
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest55 failed");
    }
  }

  /*
   * @testName: queryTest56
   * 
   * @assertion_ids: PERSISTENCE:SPEC:375; PERSISTENCE:SPEC:410;
   * PERSISTENCE:SPEC:403; PERSISTENCE:SPEC:814; PERSISTENCE:SPEC:816
   * 
   * @test_Strategy: This query returns a null
   * single_valued_association_path_expression. Verify the results were
   * accurately returned.
   */
  @SetupMethod(name = "setupCustomerData")
  public void queryTest56() throws Fault {

    boolean pass1 = false;
    boolean pass2 = true;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    final String[] expectedZips = new String[] { "00252", "00252", "00252",
        "00252", "00252", "00252", "00252", "00252", "00252", "00252", "00252",
        "00252", "00252", "00252", "00252", "00252", "11345" };

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find all work zip codes");
      CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery.select(customer.get(Customer_.work).get(Address_.zip));
      TypedQuery<String> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<String> clist = tquery.getResultList();

      if (clist.size() != 18) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 18 references, got: "
                + clist.size());
      } else {
        pass1 = true;
        int numOfNull = 0;
        int foundZip = 0;
        for (String s : clist) {
          TestUtil.logTrace("Check contents of List for null");
          Object o = s;
          if (o == null) {
            numOfNull++;
            continue;
          }

          TestUtil.logTrace("Check List for expected zip codes");

          for (int l = 0; l < 17; l++) {
            if (expectedZips[l].equals(o)) {
              foundZip++;
              break;
            }
          }
        }
        if ((numOfNull != 1) || (foundZip != 17)) {
          TestUtil.logErr("Did not get expected results");
          pass2 = false;
        } else {
          TestUtil.logTrace("Expected results received");
        }
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass1 || !pass2) {
      throw new Fault("queryTest56 failed");
    }
  }

  /*
   * @testName: queryTest58
   * 
   * @assertion_ids: PERSISTENCE:SPEC:410;
   * 
   * @test_Strategy: This query returns a null
   * single_valued_association_path_expression. Verify the results are
   * accurately returned.
   */
  @SetupMethod(name = "setupCustomerData")
  public void queryTest58() throws Fault {
    boolean pass = false;
    Object s;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find home zip codes");
      CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery.where(
          cbuilder.equal(customer.get(Customer_.home).get(Address_.street),
              "212 Edgewood Drive"))
          .select(customer.get(Customer_.name));
      TypedQuery<String> tquery = getEntityManager().createQuery(cquery);
      s = tquery.getSingleResult();

      if (s != null) {
        TestUtil.logErr("Did not get expected results.  Expected null.");
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }
    if (!pass) {
      throw new Fault("queryTest58 failed");
    }
  }

  /*
   * @testName: queryTest59
   * 
   * @assertion_ids: PERSISTENCE:SPEC:408
   * 
   * @test_Strategy: This tests a null single_valued_association_path_expression
   * is returned using IS NULL. Verify the results are accurately returned.
   *
   */
  @SetupMethod(name = "setupCustomerData")
  public void queryTest59() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("determine which customers have an null name");
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery.where(cbuilder.isNull(customer.get(Customer_.name)));
      cquery.select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "12";

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
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest59 failed");
    }
  }

  /*
   * @testName: queryTest60
   * 
   * @assertion_ids: PERSISTENCE:SPEC:775; PERSISTENCE:SPEC:773
   * 
   * @test_Strategy: This query contains an identification variable defined in a
   * collection member declaration which is not used in the rest of the query
   * however, a JOIN operation needs to be performed for the correct result set.
   * Verify the results are accurately returned.
   *
   */
  @SetupMethod(name = "setupOrderData")
  public void queryTest60() throws Fault {
    boolean pass = false;
    String[] expectedPKs = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
        "11", "12", "13", "14", "15", "16", "17", "18" };

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find Customers with an Order");
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      customer.join(Customer_.orders);
      cquery.select(customer).distinct(true);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> clist = tquery.getResultList();

      if (!checkEntityPK(clist, expectedPKs)) {
        TestUtil.logErr("Did not get expected results.  Expected "
            + expectedPKs.length + " references, got: " + clist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest60 failed");
    }
  }

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
  public void queryTest61() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Address> w = customer.join(Customer_.work, JoinType.LEFT);
      cquery.where(cbuilder.isNull(w.get(Address_.zip)));
      cquery.select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
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
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest61 failed");
    }
  }

  /*
   *
   * @testName: queryTest62
   * 
   * @assertion_ids: PERSISTENCE:SPEC:369.8;PERSISTENCE:JAVADOC:1027;
   * 
   * @test_Strategy: Execute a query which includes the arithmetic function SIZE
   * in a functional expression within the WHERE clause. The SIZE function
   * returns an integer value the number of elements of the Collection. Verify
   * the results were accurately returned.
   */
  @SetupMethod(name = "setupOrderData")
  public void queryTest62() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      From<Customer, Customer> customer = cquery.from(Customer.class);
      cquery
          .where(cbuilder.ge(cbuilder.size(customer.get(Customer_.orders)), 2));
      cquery.select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[2];
      expectedPKs[0] = "4";
      expectedPKs[1] = "14";

      if (!checkEntityPK(clist, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 2 references, got: "
                + clist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest62 failed");
    }
  }

  /*
   * @testName: queryTest63
   * 
   * @assertion_ids: PERSISTENCE:SPEC:369.8
   * 
   * @test_Strategy: Execute a query which includes the arithmetic function SIZE
   * in a functional expression within the WHERE clause. The SIZE function
   * returns an integer value the number of elements of the Collection.
   *
   * If the Collection is empty, the SIZE function evaluates to zero.
   *
   * Verify the results were accurately returned.
   */
  @SetupMethod(name = "setupOrderData")
  public void queryTest63() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery.where(
          cbuilder.gt(cbuilder.size(customer.get(Customer_.orders)), 100));
      cquery.select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[0];

      if (!checkEntityPK(clist, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 0 references, got: "
                + clist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest63 failed");
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
  public void queryTest64() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery
          .where(cbuilder.equal(customer.get(Customer_.work).get(Address_.city),
              cbuilder.parameter(String.class, "workcity")));
      cquery.select(cbuilder.construct(Customer.class,
          customer.get(Customer_.id), customer.get(Customer_.name)));
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setParameter("workcity", "Burlington");
      tquery.setMaxResults(customerRef.length);
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
        pass = false;
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest64 failed");
    }

  }

  /*
   * @testName: queryTest65
   * 
   * @assertion_ids: PERSISTENCE:SPEC:381; PERSISTENCE:SPEC:406;
   * PERSISTENCE:SPEC:825; PERSISTENCE:SPEC:822
   * 
   * @test_Strategy: Execute a query which contains the aggregate function MIN.
   * Verify the results are accurately returned.
   *
   */
  @SetupMethod(name = "setupOrderData")
  public void queryTest65() throws Fault {
    boolean pass = false;
    final String s1 = "4";
    String s2;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      TestUtil.logTrace("find MINIMUM order id for Robert E. Bissett");
      CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
      Root<Order> order = cquery.from(Order.class);
      cquery
          .where(cbuilder.equal(order.get(Order_.customer).get(Customer_.name),
              "Robert E. Bissett"))
          .select(cbuilder.least(order.get(Order_.id)));
      TypedQuery<String> tquery = getEntityManager().createQuery(cquery);
      s2 = tquery.getSingleResult();

      if (s2.equals(s1)) {
        TestUtil.logTrace("Successfully returned expected results");
        pass = true;
      } else {
        TestUtil.logTrace("queryTest65 returned " + s2 + "expected: " + s1);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }
    if (!pass) {
      throw new Fault("queryTest65 failed");
    }
  }

  /*
   * @testName: queryTest66
   * 
   * @assertion_ids: PERSISTENCE:SPEC:382; PERSISTENCE:SPEC:406;
   * PERSISTENCE:SPEC:825; PERSISTENCE:SPEC:822
   * 
   * @test_Strategy: Execute a query which contains the aggregate function MAX.
   * Verify the results are accurately returned.
   *
   */
  @SetupMethod(name = "setupOrderData")
  public void queryTest66() throws Fault {
    boolean pass = false;
    final Integer i1 = Integer.valueOf(8);
    Integer i2;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      TestUtil.logTrace(
          "find MAXIMUM number of lineItem quantities available an order may have");
      CriteriaQuery<Integer> cquery = cbuilder.createQuery(Integer.class);
      Root<LineItem> l = cquery.from(LineItem.class);
      cquery.select(cbuilder.max(l.get(LineItem_.quantity)));
      TypedQuery<Integer> tquery = getEntityManager().createQuery(cquery);
      i2 = tquery.getSingleResult();

      if (i2.equals(i1)) {
        TestUtil.logTrace("Successfully returned expected results");
        pass = true;
      } else {
        TestUtil.logTrace("queryTest66 returned:" + i2 + "expected: " + i1);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }
    if (!pass) {
      throw new Fault("queryTest66 failed");
    }
  }

  /*
   * @testName: queryTest67
   * 
   * @assertion_ids: PERSISTENCE:SPEC:380; PERSISTENCE:SPEC:406;
   * PERSISTENCE:SPEC:826; PERSISTENCE:SPEC:821; PERSISTENCE:SPEC:814;
   * PERSISTENCE:SPEC:818
   * 
   * @test_Strategy: Execute a query using the aggregate function AVG. Verify
   * the results are accurately returned.
   *
   */
  @SetupMethod(name = "setupOrderData")
  public void queryTest67() throws Fault {
    boolean pass = false;
    final Double d1 = 1487.29D;
    final Double d2 = 1487.30D;
    Double d3;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      TestUtil.logTrace("find AVERAGE price of all orders");
      CriteriaQuery<Double> cquery = cbuilder.createQuery(Double.class);
      Root<Order> order = cquery.from(Order.class);
      cquery.select(cbuilder.avg(order.get(Order_.totalPrice)));
      TypedQuery<Double> tquery = getEntityManager().createQuery(cquery);
      d3 = tquery.getSingleResult();

      if (((d3 >= d1) && (d3 < d2))) {
        TestUtil.logTrace("queryTest67 returned expected results: " + d1);
        pass = true;
      } else {
        TestUtil.logTrace("queryTest67 returned " + d3 + "expected: " + d1);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }
    if (!pass) {
      throw new Fault("queryTest67 failed");
    }
  }

  /*
   * @testName: queryTest68
   * 
   * @assertion_ids: PERSISTENCE:SPEC:383; PERSISTENCE:SPEC:406;
   * 
   * @test_Strategy: Execute a query which contains the aggregate function SUM.
   * SUM returns Double when applied to state-fields of floating types. Verify
   * the results are accurately returned.
   *
   */
  @SetupMethod(name = "setupProductData")
  public void queryTest68() throws Fault {
    boolean pass = false;
    final Double d1 = 33387.14D;
    final Double d2 = 33387.15D;
    Double d3;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      TestUtil.logTrace("find SUM of all product prices");
      CriteriaQuery<Double> cquery = cbuilder.createQuery(Double.class);
      Root<Product> product = cquery.from(Product.class);
      cquery.select(cbuilder.sum(product.get(Product_.price)));
      TypedQuery<Double> tquery = getEntityManager().createQuery(cquery);
      d3 = tquery.getSingleResult();

      if (((d3 >= d1) && (d3 < d2))) {
        TestUtil.logTrace("queryTest68 returned expected results: " + d1);
        pass = true;
      } else {
        TestUtil.logTrace("queryTest68 returned " + d3 + "expected: " + d1);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }
    if (!pass) {
      throw new Fault("queryTest68 failed");
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
  public void queryTest69() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    final Long expectedResult1 = Long.valueOf(17);
    final Long expectedResult2 = Long.valueOf(16);

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      CriteriaQuery<Long> cquery1 = cbuilder.createQuery(Long.class);
      Root<Customer> customer1 = cquery1.from(Customer.class);
      cquery1.select(
          cbuilder.count(customer1.get(Customer_.home).get(Address_.city)));
      TypedQuery<Long> tquery1 = getEntityManager().createQuery(cquery1);
      Long result1 = tquery1.getSingleResult();

      if (!(result1.equals(expectedResult1))) {
        TestUtil.logErr("Query1 in queryTest69 returned:" + result1
            + " expected: " + expectedResult1);
      } else {
        TestUtil.logTrace("query1 in queryTest69 returned expected results");
        pass1 = true;
      }
      CriteriaQuery<Long> cquery2 = cbuilder.createQuery(Long.class);
      Root<Customer> customer2 = cquery2.from(Customer.class);
      cquery2.select(cbuilder
          .countDistinct(customer2.get(Customer_.home).get(Address_.city)));
      TypedQuery<Long> tquery2 = getEntityManager().createQuery(cquery2);
      Long result2 = tquery2.getSingleResult();

      if (!(result2.equals(expectedResult2))) {
        TestUtil.logErr("Query 2 in queryTest69 returned:" + result2
            + " expected: " + expectedResult2);
      } else {
        TestUtil.logTrace("query 2 in queryTest69 returned expected results");
        pass2 = true;
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass1 || !pass2) {
      throw new Fault("queryTest69 failed");
    }

  }

  /*
   * @testName: queryTest70
   * 
   * @assertion_ids: PERSISTENCE:SPEC:383; PERSISTENCE:SPEC:406;
   * PERSISTENCE:SPEC:827; PERSISTENCE:SPEC:821
   * 
   * @test_Strategy: Execute a query which contains the aggregate function SUM.
   * SUM returns Long when applied to state-fields of integral types. Verify the
   * results are accurately returned.
   *
   */
  @SetupMethod(name = "setupProductData")
  public void queryTest70() throws Fault {
    boolean pass = false;
    final Integer expectedValue = Integer.valueOf(3277);
    Integer result;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      TestUtil.logTrace("find SUM of all product prices");
      CriteriaQuery<Integer> cquery = cbuilder.createQuery(Integer.class);
      Root<Product> product = cquery.from(Product.class);
      cquery.select(cbuilder.sum(product.get(Product_.quantity)));
      TypedQuery<Integer> tquery = getEntityManager().createQuery(cquery);
      result = tquery.getSingleResult();

      if (expectedValue.equals(result)) {
        TestUtil.logTrace("queryTest70 returned expected results: " + result);
        pass = true;
      } else {
        TestUtil.logTrace(
            "queryTest70 returned " + result + "expected: " + expectedValue);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }
    if (!pass) {
      throw new Fault("queryTest70 failed");
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
  public void queryTest71() throws Fault {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("Check if a spouse is related to a customer");
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Spouse> spouse = cquery.from(Spouse.class);
      cquery.where(cbuilder.equal(spouse.get(Spouse_.id), "7"))
          .select(spouse.get(Spouse_.customer));
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.getSingleResult();

      getEntityTransaction().commit();
    } catch (NoResultException e) {
      TestUtil
          .logTrace("queryTest71: NoResultException caught as expected : " + e);
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("queryTest71 failed");
    }
  }

  /*
   * @testName: test_leftouterjoin_1xM
   * 
   * @assertion_ids: PERSISTENCE:SPEC:780
   * 
   * @test_Strategy: LEFT OUTER JOIN for 1-M relationship. Retrieve credit card
   * information for a customer with name like Caruso.
   *
   */
  @SetupMethod(name = "setupCustomerData")
  public void test_leftouterjoin_1xM() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      customer.join(Customer_.creditCards, JoinType.LEFT);
      cquery.where(cbuilder.like(customer.get(Customer_.name), "%Caruso"))
          .select(customer).distinct(true);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[2];
      expectedPKs[0] = "7";
      expectedPKs[1] = "8";

      if (!checkEntityPK(clist, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 2 references, got: "
                + clist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("test_leftouterjoin_1xM failed");
    }
  }

  /*
   * @testName: test_leftouterjoin_Mx1
   * 
   * @assertion_ids: PERSISTENCE:SPEC:780; PERSISTENCE:SPEC:399.1;
   * PERSISTENCE:SPEC:399
   * 
   * @test_Strategy: Left Outer Join for M-1 relationship. Retrieve customer
   * information from Order.
   *
   */
  @SetupMethod(name = "setupOrderData")
  public void test_leftouterjoin_Mx1() throws Fault {
    boolean pass1 = false;
    boolean pass2 = true;
    final Object[][] expectedResultSet = new Object[][] {
        new Object[] { "15", "14" }, new Object[] { "16", "14" } };

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
      Root<Order> order = cquery.from(Order.class);
      Join<Order, Customer> c = order.join(Order_.customer, JoinType.LEFT);
      // criteria queries don't support positional parameters, using "one"
      // as parameter name instead
      cquery.where(cbuilder.equal(c.get(Customer_.name),
          cbuilder.parameter(String.class, "one")));
      cquery.multiselect(order.get(Order_.id), c.get(Customer_.id));
      cquery.orderBy(cbuilder.asc(order.get(Order_.id)));
      TypedQuery tquery = getEntityManager().createQuery(cquery);
      tquery.setParameter("one", "Kellie A. Sanborn");
      tquery.setMaxResults(orderRef.length);
      List<Tuple> q = tquery.getResultList();

      if (q.size() != 2) {
        TestUtil
            .logTrace("test_leftouterjoin_Mx1:  Did not get expected results. "
                + "Expected 2,  got: " + q.size());
      } else {

        TestUtil.logTrace("Expected size received, verify contents . . . ");
        // each element of the list q should be a size-2 array
        for (int i = 0; i < q.size(); i++) {
          pass1 = true;
          Object obj = q.get(i);
          Object[] orderAndCustomerExpected = expectedResultSet[i];
          Tuple orderAndCustomerTuple = null;
          Object[] orderAndCustomer = null;
          if (obj instanceof Tuple) {
            TestUtil.logTrace(
                "The element in the result list is of type Object[], continue . . .");
            orderAndCustomerTuple = (Tuple) obj;
            orderAndCustomer = orderAndCustomerTuple.toArray();
            if (!Arrays.equals(orderAndCustomerExpected, orderAndCustomer)) {
              TestUtil.logErr("Expecting element value: "
                  + Arrays.asList(orderAndCustomerExpected)
                  + ", actual element value: "
                  + Arrays.asList(orderAndCustomer));
              pass2 = false;
              break;
            }
          } else {
            TestUtil.logErr(
                "The element in the result list is not of type Object[]:"
                    + obj);
            pass2 = false;
            break;
          }
        }
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass1 || !pass2) {
      throw new Fault("test_leftouterjoin_Mx1 failed");
    }
  }

  /*
   * @testName: test_leftouterjoin_MxM
   * 
   * @assertion_ids: PERSISTENCE:SPEC:780; PERSISTENCE:SPEC:317;
   * PERSISTENCE:SPEC:317.3; PERSISTENCE:SPEC:320; PERSISTENCE:SPEC:811;
   * PERSISTENCE:JAVADOC:993
   * 
   * @test_Strategy: Left Outer Join for M-M relationship. Retrieve all aliases
   * where customer name like Ste.
   *
   */
  @SetupMethod(name = "setupAliasData")
  public void test_leftouterjoin_MxM() throws Fault {

    boolean pass1 = false;
    boolean pass2 = true;
    final Object[][] expectedResultSet = new Object[][] {
        new Object[] { "7", "sjc" }, new Object[] { "5", "ssd" },
        new Object[] { "7", "stevec" }, new Object[] { "5", "steved" },
        new Object[] { "5", "stevie" }, new Object[] { "7", "stevie" } };

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Alias> a = customer.join(Customer_.aliases, JoinType.LEFT);
      cquery.where(cbuilder.like(customer.get(Customer_.name), "Ste%"));
      cquery.multiselect(customer.get(Customer_.id), a.get(Alias_.alias))
          .orderBy(cbuilder.asc(a.get(Alias_.alias)),
              cbuilder.asc(customer.get(Customer_.id)));
      TypedQuery<Tuple> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Tuple> q = tquery.getResultList();

      if (q.size() != 6) {
        TestUtil
            .logTrace("test_leftouterjoin_MxM:  Did not get expected results. "
                + "Expected 6,  got: " + q.size());
      } else {
        TestUtil.logTrace("Expected size received, verify contents . . . ");
        // each element of the list q should be a size-2 array
        for (int i = 0; i < q.size(); i++) {
          pass1 = true;
          Object obj = q.get(i);
          Object[] customerAndAliasExpected = expectedResultSet[i];
          Tuple customerAndAliasTuple = null;
          Object[] customerAndAlias = null;
          if (obj instanceof Tuple) {
            TestUtil.logTrace(
                "The element in the result list is of type Object[], continue . . .");
            customerAndAliasTuple = (Tuple) obj;
            customerAndAlias = customerAndAliasTuple.toArray();
            if (!Arrays.equals(customerAndAliasExpected, customerAndAlias)) {
              TestUtil.logErr("Expecting element value: "
                  + Arrays.asList(customerAndAliasExpected)
                  + ", actual element value: "
                  + Arrays.asList(customerAndAlias));
              pass2 = false;
              break;
            }
          } else {
            TestUtil.logErr(
                "The element in the result list is not of type Object[]:"
                    + obj);
            pass2 = false;
            break;
          }
        }
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass1 || !pass2) {
      throw new Fault("test_leftouterjoin_MxM failed");
    }
  }

  /*
   * @testName: test_upperStringExpression
   * 
   * @assertion_ids: PERSISTENCE:SPEC:369.11
   * 
   * @test_Strategy: Test for Upper expression in the Where Clause Select the
   * customer with alias name = UPPER(SJC)
   *
   */
  @SetupMethod(name = "setupAliasData")
  public void test_upperStringExpression() throws Fault {

    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Alias> a = customer.join(Customer_.aliases);
      cquery.where(cbuilder.equal(cbuilder.upper(a.get(Alias_.alias)), "SJC"));
      cquery.select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      List<Customer> result = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "7";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expectedPKs.length + " references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("test_upperStringExpression failed");
    }
  }

  /*
   * @testName: test_lowerStringExpression
   * 
   * @assertion_ids: PERSISTENCE:SPEC:369.10
   * 
   * @test_Strategy: Test for Lower expression in the Where Clause Select the
   * customer with alias name = LOWER(sjc)
   *
   */
  @SetupMethod(name = "setupAliasData")
  public void test_lowerStringExpression() throws Fault {

    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Alias> a = customer.join(Customer_.aliases);
      cquery.where(cbuilder.equal(cbuilder.lower(a.get(Alias_.alias)), "sjc"))
          .select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      List<Customer> result = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "7";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expectedPKs.length + " references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("test_lowerStringExpression failed");
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
  public void test_groupBy() throws Fault {
    boolean pass = false;
    final String expectedCodes[] = new String[] { "CHA", "GBR", "IRE", "JPN",
        "USA" };

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery.select(customer.get(Customer_.country).get(Country_.code));
      cquery.groupBy(customer.get(Customer_.country).get(Country_.code));
      TypedQuery<String> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(country.length);
      List<String> result = tquery.getResultList();

      String[] output = (String[]) (result.toArray(new String[result.size()]));
      Arrays.sort(output);

      pass = Arrays.equals(expectedCodes, output);

      if (!pass) {
        TestUtil
            .logErr("Did not get expected results.  Expected 4 Country Codes: "
                + "CHA, GBR, JPN, USA. Received: " + result.size());
        for (String s : result) {
          TestUtil.logTrace(" Credit Card Type: " + s);
        }
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("test_groupBy failed");
    }
  }

  /*
   * @testName: test_groupBy_1
   * 
   * @assertion_ids: PERSISTENCE:SPEC:810
   * 
   * @test_Strategy: Test for Only Group By in a simple select statement without
   * using an Embeddable Entity in the query.
   *
   */
  @SetupMethod(name = "setupOrderData")
  public void test_groupBy_1() throws Fault {
    boolean pass = false;
    final String expectedTypes[] = new String[] { "AXP", "MCARD", "VISA" };

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
      Root<CreditCard> cc = cquery.from(CreditCard.class);
      cc.join(CreditCard_.customer);
      cquery.select(cc.get(CreditCard_.type)).groupBy(cc.get(CreditCard_.type));
      TypedQuery<String> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(creditCard.length);
      List<String> result = tquery.getResultList();

      String[] output = result.toArray(new String[result.size()]);
      Arrays.sort(output);

      pass = Arrays.equals(expectedTypes, output);

      if (!pass) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 3 Credit Card Types: "
                + "AXP, MCARD, VISA. Received: " + result.size());
        for (String s : result) {
          TestUtil.logTrace(" Credit Card Type: " + s);
        }

      }
      // verify this
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("test_groupBy_1 failed");
    }
  }

  /*
   * @testName: test_innerjoin_1x1
   * 
   * @assertion_ids: PERSISTENCE:SPEC:779; PERSISTENCE:SPEC:372;
   * PERSISTENCE:SPEC:372.2; PERSISTENCE:JAVADOC:987
   * 
   * @test_Strategy: Inner Join for 1-1 relationship. Select all customers with
   * spouses.
   */
  @SetupMethod(name = "setupCustomerData")
  public void test_innerjoin_1x1() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      customer.join(Customer_.spouse);
      cquery.select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> result = tquery.getResultList();

      expectedPKs = new String[5];
      expectedPKs[0] = "7";
      expectedPKs[1] = "10";
      expectedPKs[2] = "11";
      expectedPKs[3] = "12";
      expectedPKs[4] = "13";
      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expectedPKs.length + " references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("test_innerjoin_1x1 failed");
    }
  }

  /*
   * @testName: test_innerjoin_1xM
   * 
   * @assertion_ids: PERSISTENCE:SPEC:779
   * 
   * @test_Strategy: Inner Join for 1-M relationship. Retrieve credit card
   * information for all customers.
   */
  @SetupMethod(name = "setupOrderData")
  public void test_innerjoin_1xM() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, CreditCard> cc = customer.join(Customer_.creditCards);
      cquery.where(cbuilder.equal(cc.get(CreditCard_.type), "VISA"))
          .select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> result = tquery.getResultList();

      expectedPKs = new String[8];
      expectedPKs[0] = "1";
      expectedPKs[1] = "2";
      expectedPKs[2] = "3";
      expectedPKs[3] = "6";
      expectedPKs[4] = "7";
      expectedPKs[5] = "10";
      expectedPKs[6] = "14";
      expectedPKs[7] = "17";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expectedPKs.length + " references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("test_innerjoin_1xM failed");
    }
  }

  /*
   * @testName: test_innerjoin_Mx1
   * 
   * @assertion_ids: PERSISTENCE:SPEC:779; PERSISTENCE:SPEC:373
   * 
   * @test_Strategy: Inner Join for M-1 relationship. Retrieve customer
   * information from Order. customer name = Kellie A. Sanborn
   */
  @SetupMethod(name = "setupOrderData")
  public void test_innerjoin_Mx1() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      Join<Order, Customer> c = order.join(Order_.customer);
      // note: uses named parameter, not positional one
      cquery.where(cbuilder.equal(c.get(Customer_.name),
          cbuilder.parameter(String.class, "one")));
      cquery.select(order);
      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      tquery.setParameter("one", "Kellie A. Sanborn");
      tquery.setMaxResults(orderRef.length);
      List<Order> result = tquery.getResultList();

      expectedPKs = new String[2];
      expectedPKs[0] = "15";
      expectedPKs[1] = "16";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expectedPKs.length + " references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("test_innerjoin_Mx1 failed");
    }
  }

  /*
   * @testName: test_innerjoin_MxM
   * 
   * @assertion_ids: PERSISTENCE:SPEC:779
   * 
   * @test_Strategy: Inner Join for M-M relationship. Retrieve aliases for alias
   * name=fish.
   */
  @SetupMethod(name = "setupAliasData")
  public void test_innerjoin_MxM() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Alias> a = customer.join(Customer_.aliases);
      cquery.where(cbuilder.equal(a.get(Alias_.alias),
          cbuilder.parameter(String.class, "aName")));
      cquery.select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setParameter("aName", "fish");
      tquery.setMaxResults(customerRef.length);
      List<Customer> result = tquery.getResultList();

      expectedPKs = new String[2];
      expectedPKs[0] = "1";
      expectedPKs[1] = "2";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expectedPKs.length + " references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("test_innerjoin_MxM failed");
    }
  }

  /*
   * @testName: fetchFetchSingularAttributeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:965
   * 
   * @test_Strategy:
   */
  @SetupMethod(name = "setupCustomerData")
  public void fetchFetchSingularAttributeTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);

      Root<Customer> customer = cquery.from(Customer.class);
      Fetch f = customer.fetch(Customer_.spouse);

      Fetch f1 = f.fetch(Customer_.spouse);
      Attribute attr = f1.getAttribute();
      if (attr.getName().equals(Customer_.spouse.getName())) {
        TestUtil.logTrace("Received expected attribute:" + attr.getName());
        pass1 = true;
      } else {

        TestUtil.logErr("Expected attribute:" + Customer_.spouse.getName()
            + ", actual:" + attr.getName());
      }
      JoinType jt = f1.getJoinType();
      if (jt.equals(JoinType.INNER)) {
        TestUtil.logTrace("Received expected JoinType:" + jt.name());
        pass2 = true;
      } else {

        TestUtil.logErr("Expected JoinType:" + JoinType.INNER.name()
            + ", actual:" + jt.name());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {

      TestUtil.logErr("Caught unexpected exception:", e);
    }

    if (!pass1 || !pass2) {
      throw new Fault("fetchFetchSingularAttributeTest failed");
    }
  }

  /*
   * @testName: fetchFetchSingularAttributeJoinTypeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:966
   * 
   * @test_Strategy:
   */
  @SetupMethod(name = "setupCustomerData")
  public void fetchFetchSingularAttributeJoinTypeTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);

      Root<Customer> customer = cquery.from(Customer.class);
      Fetch f = customer.fetch(Customer_.spouse);

      Fetch f1 = f.fetch(Customer_.spouse, JoinType.INNER);
      Attribute attr = f1.getAttribute();
      if (attr.getName().equals(Customer_.spouse.getName())) {
        TestUtil.logTrace("Received expected attribute:" + attr.getName());
        pass1 = true;
      } else {
        TestUtil.logErr("Expected attribute:" + Customer_.spouse.getName()
            + ", actual:" + attr.getName());
      }
      JoinType jt = f1.getJoinType();
      if (jt.equals(JoinType.INNER)) {
        TestUtil.logTrace("Received expected JoinType:" + jt.name());
        pass2 = true;
      } else {
        TestUtil.logErr("Expected JoinType:" + JoinType.INNER.name()
            + ", actual:" + jt.name());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);
    }

    if (!pass1 || !pass2) {
      throw new Fault("fetchFetchSingularAttributeJoinTypeTest failed");
    }
  }

  /*
   * @testName: fetchFetchPluralAttributeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:967
   * 
   * @test_Strategy:
   */
  @SetupMethod(name = "setupOrderData")
  public void fetchFetchPluralAttributeTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);

      Root<Customer> customer = cquery.from(Customer.class);
      Fetch f = customer.fetch(Customer_.orders2);

      SetJoin<Customer, Order> o = customer.join(Customer_.orders2);
      PluralAttribute pa = o.getModel();

      Fetch f1 = f.fetch(pa);
      Attribute attr = f1.getAttribute();
      if (attr.getName().equals(Customer_.orders2.getName())) {
        TestUtil.logTrace("Received expected attribute:" + attr.getName());
        pass1 = true;
      } else {
        TestUtil.logErr("Expected attribute:" + Customer_.orders2.getName()
            + ", actual:" + attr.getName());
      }
      JoinType jt = f1.getJoinType();
      if (jt.equals(JoinType.INNER)) {
        TestUtil.logTrace("Received expected JoinType:" + jt.name());
        pass2 = true;
      } else {
        TestUtil.logErr("Expected JoinType:" + JoinType.INNER.name()
            + ", actual:" + jt.name());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);
    }

    if (!pass1 || !pass2) {
      throw new Fault("fetchFetchPluralAttributeTest failed");
    }
  }

  /*
   * @testName: fetchFetchPluralAttributeJoinTypeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:968
   * 
   * @test_Strategy:
   */
  @SetupMethod(name = "setupOrderData")
  public void fetchFetchPluralAttributeJoinTypeTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);

      Root<Customer> customer = cquery.from(Customer.class);
      Fetch f = customer.fetch(Customer_.orders2);

      SetJoin<Customer, Order> o = customer.join(Customer_.orders2);
      PluralAttribute pa = o.getModel();

      Fetch f1 = f.fetch(pa, JoinType.INNER);
      Attribute attr = f1.getAttribute();
      if (attr.getName().equals(Customer_.orders2.getName())) {
        TestUtil.logTrace("Received expected attribute:" + attr.getName());
        pass1 = true;
      } else {
        TestUtil.logErr("Expected attribute:" + Customer_.orders2.getName()
            + ", actual:" + attr.getName());
      }
      JoinType jt = f1.getJoinType();
      if (jt.equals(JoinType.INNER)) {
        TestUtil.logTrace("Received expected JoinType:" + jt.name());
        pass2 = true;
      } else {
        TestUtil.logErr("Expected JoinType:" + JoinType.INNER.name()
            + ", actual:" + jt.name());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);
    }

    if (!pass1 || !pass2) {
      throw new Fault("fetchFetchPluralAttributeJoinTypeTest failed");
    }
  }

  /*
   * @testName: fetchFetchStringTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:969
   * 
   * @test_Strategy:
   */
  @SetupMethod(name = "setupOrderData")
  public void fetchFetchStringTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      From<Customer, Customer> customer = cquery.from(Customer.class);

      Fetch f = customer.fetch("orders");
      Fetch f1 = f.fetch("creditCard");
      Attribute attr = f1.getAttribute();
      if (attr.getName().equals("creditCard")) {
        TestUtil.logTrace("Received expected attribute:" + attr.getName());
        pass1 = true;
      } else {
        TestUtil
            .logErr("Expected attribute:creditCard, actual:" + attr.getName());
      }
      JoinType jt = f1.getJoinType();
      if (jt.equals(JoinType.INNER)) {
        TestUtil.logTrace("Received expected JoinType:" + jt.name());
        pass2 = true;
      } else {
        TestUtil.logErr("Expected JoinType:" + JoinType.INNER.name()
            + ", actual:" + jt.name());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);
    }

    if (!pass1 || !pass2) {
      throw new Fault("fetchFetchStringTest failed");
    }
  }

  /*
   * @testName: fetchFetchStringJoinTypeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:971; PERSISTENCE:SPEC:1698;
   * PERSISTENCE:SPEC:1786; PERSISTENCE:SPEC:1786.2;
   * 
   * @test_Strategy:
   */
  @SetupMethod(name = "setupOrderData")
  public void fetchFetchStringJoinTypeTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      From<Customer, Customer> customer = cquery.from(Customer.class);

      Fetch f = customer.fetch("orders");
      Fetch f1 = f.fetch("creditCard", JoinType.INNER);
      Attribute attr = f1.getAttribute();
      if (attr.getName().equals("creditCard")) {
        TestUtil.logTrace("Received expected attribute:" + attr.getName());
        pass1 = true;
      } else {
        TestUtil.logErr("Expected attribute:" + Customer_.orders.getName()
            + ", actual:" + attr.getName());
      }
      JoinType jt = f1.getJoinType();
      if (jt.equals(JoinType.INNER)) {
        TestUtil.logTrace("Received expected JoinType:" + jt.name());
        pass2 = true;
      } else {
        TestUtil.logErr("Expected JoinType:" + JoinType.INNER.name()
            + ", actual:" + jt.name());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);
    }

    if (!pass1 || !pass2) {
      throw new Fault("fetchFetchStringJoinTypeTest failed");
    }
  }

  /*
   * @testName: fetchFetchStringIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:970
   * 
   * @test_Strategy:
   */
  @SetupMethod(name = "setupOrderData")
  public void fetchFetchStringIllegalArgumentExceptionTest() throws Fault {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      From<Customer, Customer> customer = cquery.from(Customer.class);

      Fetch f = customer.fetch("orders");
      try {
        Fetch f1 = f.fetch("doesnotexist");
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
        pass = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception", e);
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);
    }

    if (!pass) {
      throw new Fault("fetchFetchStringIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: fetchFetchStringJoinTypeIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:972
   * 
   * @test_Strategy:
   */
  @SetupMethod(name = "setupOrderData")
  public void fetchFetchStringJoinTypeIllegalArgumentExceptionTest()
      throws Fault {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      From<Customer, Customer> customer = cquery.from(Customer.class);

      Fetch f = customer.fetch("orders");
      try {
        Fetch f1 = f.fetch("doesnotexist", JoinType.INNER);
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
        pass = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception", e);
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);
    }

    if (!pass) {
      throw new Fault(
          "fetchFetchStringJoinTypeIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: fetchTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:962; PERSISTENCE:JAVADOC:963;
   * 
   * @test_Strategy:
   */
  @SetupMethod(name = "setupCustomerData")
  public void fetchTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    boolean pass4 = false;
    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      From<Customer, Customer> customer = cquery.from(Customer.class);

      JoinType jt = customer.fetch(Customer_.spouse).getJoinType();
      if (jt.equals(JoinType.INNER)) {
        TestUtil.logTrace("Received expected JoinType:" + jt.name());
        pass1 = true;
      } else {
        TestUtil.logErr("Expected JoinType:" + JoinType.INNER.name()
            + ", actual:" + jt.name());
      }
      jt = customer.fetch(Customer_.spouse, JoinType.INNER).getJoinType();
      if (jt.equals(JoinType.INNER)) {
        TestUtil.logTrace("Received expected JoinType:" + jt.name());
        pass2 = true;

      } else {
        TestUtil.logErr("Expected JoinType:" + JoinType.INNER.name()
            + ", actual:" + jt.name());
      }
      jt = customer.fetch(Customer_.spouse, JoinType.LEFT).getJoinType();
      if (jt.equals(JoinType.LEFT)) {
        TestUtil.logTrace("Received expected JoinType:" + jt.name());
        pass3 = true;

      } else {
        TestUtil.logErr("Expected JoinType:" + JoinType.LEFT.name()
            + ", actual:" + jt.name());
      }

      /*
       * JoinType.RIGHT Not Required in JPA 2.0 jt =
       * customer.fetch(Customer_.spouse,JoinType.RIGHT).getJoinType(); if
       * (jt.equals(JoinType.RIGHT)) {
       * TestUtil.logTrace("Received expected JoinType"); pass=true; } else {
       * TestUtil.logErr("Expected JoinType:" + JoinType.RIGHT.name() +
       * ", actual:" + jt.name()); }
       */

      Attribute attr = customer.fetch(Customer_.spouse).getAttribute();
      if (attr.getName().equals(Customer_.spouse.getName())) {
        TestUtil.logTrace("Received expected attribute:" + attr.getName());
        pass4 = true;
      } else {
        TestUtil.logErr("Expected attribute:" + Customer_.spouse.getName()
            + ", actual:" + attr.getName());
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);
    }

    if (!pass1 || !pass2 || !pass3 || !pass4) {
      throw new Fault("fetchTest failed");
    }
  }

  /*
   * @testName: fetchGetParentTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:964
   * 
   * @test_Strategy: Get FetchParent then get the fetches that it contains and
   * verify we got the correct one.
   */
  @SetupMethod(name = "setupCustomerData")
  public void fetchGetParentTest() throws Fault {
    boolean pass = false;
    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      From<Customer, Customer> customer = cquery.from(Customer.class);

      FetchParent fp = customer.fetch(Customer_.spouse).getParent();
      Set<Fetch<?, ?>> f = fp.getFetches();
      if (f.size() == 1) {
        for (Fetch tmpFetch : f) {
          String name = tmpFetch.getAttribute().getName();
          if (name.equals("spouse")) {
            TestUtil.logTrace("Received expected Fetch:" + name
                + ", which means the correct FetchParent was returned");
            pass = true;
          } else {
            TestUtil.logErr("Expected: spouse, actual:" + name);
          }
        }
      } else {
        TestUtil.logErr(
            "Expected 1 fetch to be returned from FetchParent.getFetches(), got:"
                + f.size());
        for (Fetch tmpFetch : f) {
          String name = tmpFetch.getAttribute().getName();
          TestUtil.logErr("Fetch returned was:" + name);
        }
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);
    }

    if (!pass) {
      throw new Fault("fetchGetParentTest failed");
    }
  }

  /*
   * @testName: test_fetchjoin_1x1
   * 
   * @assertion_ids: PERSISTENCE:SPEC:781; PERSISTENCE:SPEC:774;
   * PERSISTENCE:SPEC:776; PERSISTENCE:JAVADOC:974; PERSISTENCE:JAVADOC:982;
   * PERSISTENCE:JAVADOC:974; PERSISTENCE:JAVADOC:1017;
   * 
   * @test_Strategy: JOIN FETCH for 1-1 relationship. Prefetch the spouses for
   * all Customers.
   */
  @SetupMethod(name = "setupCustomerData")
  public void test_fetchjoin_1x1() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      From<Customer, Customer> customer = cquery.from(Customer.class);
      Collection cFetches = customer.getFetches();
      if (cFetches.size() != 0) {
        TestUtil
            .logErr("Did not get correct number of fetches, expected:0, actual:"
                + cFetches.size());
        for (Object o : cFetches) {
          TestUtil.logErr("Fetch:" + o.toString());
        }
      }

      customer.fetch(Customer_.spouse);
      cFetches = customer.getFetches();
      if (cFetches.size() == 1) {
        TestUtil
            .logTrace("Received expected number of fetches" + cFetches.size());
      } else {
        TestUtil
            .logErr("Did not get correct number of fetches, expected:1, actual:"
                + cFetches.size());
        for (Object o : cFetches) {
          TestUtil.logErr("Fetch:" + o.toString());
        }

      }
      cquery.select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> result = tquery.getResultList();

      expectedPKs = new String[5];
      expectedPKs[0] = "7";
      expectedPKs[1] = "10";
      expectedPKs[2] = "11";
      expectedPKs[3] = "12";
      expectedPKs[4] = "13";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expectedPKs.length + " references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("test_fetchjoin_1x1 failed");
    }
  }

  /*
   * @testName: fetchSingularAttributeJoinType1X1Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:975; PERSISTENCE:JAVADOC:1018;
   * 
   * @test_Strategy: JOIN FETCH for 1-1 relationship. Prefetch the spouses for
   * all Customers.
   */
  @SetupMethod(name = "setupCustomerData")
  public void fetchSingularAttributeJoinType1X1Test() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      From<Customer, Customer> customer = cquery.from(Customer.class);
      customer.fetch(Customer_.spouse, JoinType.INNER);
      cquery.select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> result = tquery.getResultList();

      expectedPKs = new String[5];
      expectedPKs[0] = "7";
      expectedPKs[1] = "10";
      expectedPKs[2] = "11";
      expectedPKs[3] = "12";
      expectedPKs[4] = "13";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expectedPKs.length + " references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);
    }

    if (!pass) {
      throw new Fault("fetchSingularAttributeJoinType1X1Test failed");
    }
  }

  /*
   * @testName: test_fetchjoin_1xM
   * 
   * @assertion_ids: PERSISTENCE:SPEC:782; PERSISTENCE:SPEC:374;
   * PERSISTENCE:SPEC:777; PERSISTENCE:SPEC:783; PERSISTENCE:JAVADOC:977;
   * PERSISTENCE:SPEC:1715;
   * 
   * @test_Strategy: Fetch Join for 1-M relationship. Retrieve customers from NY
   * or RI who have orders.
   *
   */
  @SetupMethod(name = "setupCustomerData")
  public void test_fetchjoin_1xM() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      customer.fetch(Customer_.orders, JoinType.LEFT);
      cquery.where(
          customer.get(Customer_.home).get(Address_.state).in("NY", "RI"));
      cquery.select(customer).distinct(true);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> result = tquery.getResultList();

      expectedPKs = new String[2];
      expectedPKs[0] = "14";
      expectedPKs[1] = "17";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expectedPKs.length + " references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("test_fetchjoin_1xM failed", e);
    }

    if (!pass) {
      throw new Fault("test_fetchjoin_1xM failed");
    }
  }

  /*
   * @testName: fetchPluralAttribute1xMTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:976; PERSISTENCE:JAVADOC:1019;
   * 
   * @test_Strategy: Fetch Join for 1-M relationship. Retrieve customers from NY
   * or RI who have orders.
   *
   */
  @SetupMethod(name = "setupOrderData")
  public void fetchPluralAttribute1xMTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      From<Customer, Customer> customer = cquery.from(Customer.class);
      customer.fetch(Customer_.orders);
      cquery.where(
          customer.get(Customer_.home).get(Address_.state).in("NY", "RI"));
      cquery.select(customer).distinct(true);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> result = tquery.getResultList();

      expectedPKs = new String[2];
      expectedPKs[0] = "14";
      expectedPKs[1] = "17";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expectedPKs.length + " references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);
    }

    if (!pass) {
      throw new Fault("fetchPluralAttribute1xMTest failed");
    }
  }

  /*
   * @testName: fetchPluralAttributeJoinType1xMTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1020;
   * 
   * @test_Strategy: Fetch Join for 1-M relationship. Retrieve customers from NY
   * or RI who have orders.
   *
   */
  @SetupMethod(name = "setupOrderData")
  public void fetchPluralAttributeJoinType1xMTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      From<Customer, Customer> customer = cquery.from(Customer.class);
      customer.fetch(Customer_.orders, JoinType.INNER);
      cquery.where(
          customer.get(Customer_.home).get(Address_.state).in("NY", "RI"));
      cquery.select(customer).distinct(true);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> result = tquery.getResultList();

      expectedPKs = new String[2];
      expectedPKs[0] = "14";
      expectedPKs[1] = "17";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expectedPKs.length + " references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);
    }

    if (!pass) {
      throw new Fault("fetchPluralAttributeJoinType1xMTest failed");
    }
  }

  /*
   * @testName: test_fetchjoin_Mx1
   * 
   * @assertion_ids: PERSISTENCE:SPEC:781; PERSISTENCE:SPEC:654
   * 
   * @test_Strategy: Retrieve customer information from Order.
   *
   */
  @SetupMethod(name = "setupOrderData")
  public void test_fetchjoin_Mx1() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      order.fetch(Order_.customer, JoinType.LEFT);
      cquery.where(cbuilder.equal(
          order.get(Order_.customer).get(Customer_.home).get(Address_.city),
          "Lawrence")).select(order);
      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(orderRef.length);
      List<Order> result = tquery.getResultList();

      expectedPKs = new String[2];
      expectedPKs[0] = "15";
      expectedPKs[1] = "16";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expectedPKs.length + " references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);
    }

    if (!pass) {
      throw new Fault("test_fetchjoin_Mx1 failed");
    }
  }

  /*
   * @testName: test_fetchjoin_Mx1_1
   * 
   * @assertion_ids: PERSISTENCE:SPEC:781
   * 
   * @test_Strategy: Retrieve customer information from Order.
   *
   */
  @SetupMethod(name = "setupOrderData")
  public void test_fetchjoin_Mx1_1() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      order.fetch(Order_.customer, JoinType.LEFT);
      cquery.where(cbuilder.like(order.get(Order_.customer).get(Customer_.name),
          "%Caruso")).select(order);
      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(orderRef.length);
      List<Order> result = tquery.getResultList();

      expectedPKs = new String[2];
      expectedPKs[0] = "7";
      expectedPKs[1] = "8";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expectedPKs.length + " references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("test_fetchjoin_Mx1_1 failed");
    }
  }

  /*
   * @testName: test_fetchjoin_MxM
   * 
   * @assertion_ids: PERSISTENCE:SPEC:781
   * 
   * @test_Strategy: Left Join Fetch for M-M relationship. Retrieve customers
   * with orders that live in NH.
   */
  @SetupMethod(name = "setupAliasData")
  public void test_fetchjoin_MxM() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("FETCHJOIN-MXM Executing Query");
      CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
      Root<Alias> alias = cquery.from(Alias.class);
      alias.fetch(Alias_.customers, JoinType.LEFT);
      cquery.where(cbuilder.like(alias.get(Alias_.alias), "a%")).select(alias);
      TypedQuery<Alias> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(aliasRef.length);
      List<Alias> result = tquery.getResultList();

      expectedPKs = new String[4];
      expectedPKs[0] = "1";
      expectedPKs[1] = "2";
      expectedPKs[2] = "5";
      expectedPKs[3] = "6";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expectedPKs.length + " references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("test_fetchjoin_MxM failed");
    }
  }

  /*
   * @testName: test_betweenDates
   * 
   * @assertion_ids: PERSISTENCE:SPEC:349.2; PERSISTENCE:SPEC:553;
   * PERSISTENCE:JAVADOC:15; PERSISTENCE:JAVADOC:166; PERSISTENCE:JAVADOC:189;
   * PERSISTENCE:SPEC:1049; PERSISTENCE:SPEC:1059; PERSISTENCE:SPEC:1060
   * 
   * @test_Strategy: Execute a query containing using the operator BETWEEN with
   * datetime_expression. Verify the results were accurately returned.
   *
   */
  @SetupMethod(name = "setupProductData")
  public void test_betweenDates() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      final Date date1 = getSQLDate(2000, 2, 14);
      final Date date6 = getSQLDate(2005, 2, 18);
      TestUtil.logTrace("The dates used in test_betweenDates is : " + date1
          + " and " + date6);
      CriteriaQuery<Product> cquery = cbuilder.createQuery(Product.class);
      Root<Product> product = cquery.from(Product.class);
      cquery.where(cbuilder.between(
          product.get(Product_.shelfLife).get(ShelfLife_.soldDate),
          cbuilder.parameter(java.sql.Date.class, "date1"),
          cbuilder.parameter(java.sql.Date.class, "date6")));
      cquery.select(product);
      TypedQuery<Product> tquery = getEntityManager().createQuery(cquery);
      tquery.setParameter("date1", date1);
      tquery.setParameter("date6", date6);
      tquery.setMaxResults(productRef.length);
      List<Product> result = tquery.getResultList();

      expectedPKs = new String[4];
      expectedPKs[0] = "31";
      expectedPKs[1] = "32";
      expectedPKs[2] = "33";
      expectedPKs[3] = "37";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 3 references, got: "
                + result.size());
      } else {

        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass)
      throw new Fault("test_betweenDates failed");
  }

  /*
   * @testName: test_notBetweenArithmetic
   * 
   * @assertion_ids: PERSISTENCE:SPEC:349
   * 
   * @test_Strategy: Execute a query containing using the operator BETWEEN and
   * NOT BETWEEN. Verify the results were accurately returned.
   */
  @SetupMethod(name = "setupOrderData")
  public void test_notBetweenArithmetic() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      cquery.where(cbuilder
          .not(cbuilder.between(order.get(Order_.totalPrice), 1000D, 1200D)));
      cquery.select(order);
      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(orderRef.length);
      List<Order> result = tquery.getResultList();

      expectedPKs = new String[15];
      expectedPKs[0] = "2";
      expectedPKs[1] = "4";
      expectedPKs[2] = "5";
      expectedPKs[3] = "6";
      expectedPKs[4] = "9";
      expectedPKs[5] = "10";
      expectedPKs[6] = "11";
      expectedPKs[7] = "12";
      expectedPKs[8] = "13";
      expectedPKs[9] = "15";
      expectedPKs[10] = "16";
      expectedPKs[11] = "17";
      expectedPKs[12] = "18";
      expectedPKs[13] = "19";
      expectedPKs[14] = "20";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expectedPKs.length + " references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("test_notBetweenArithmetic failed");
    }
  }

  /*
   * @testName: test_notBetweenDates
   * 
   * @assertion_ids: PERSISTENCE:SPEC:349.2; PERSISTENCE:SPEC:600
   * 
   * @test_Strategy: Execute a query containing using the operator NOT BETWEEN.
   * Verify the results were accurately returned.
   */
  @SetupMethod(name = "setupProductData")
  public void test_notBetweenDates() throws Fault {
    boolean pass = false;
    String expectedPKs[];
    final Date date1 = getSQLDate("2000-02-14");
    final Date newdate = getSQLDate("2005-02-17");
    TestUtil.logTrace("The dates used in test_betweenDates is : " + date1
        + " and " + newdate);

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Product> cquery = cbuilder.createQuery(Product.class);
      Root<Product> product = cquery.from(Product.class);
      cquery.where(cbuilder.not(cbuilder.between(
          product.get(Product_.shelfLife).get(ShelfLife_.soldDate),
          cbuilder.parameter(java.sql.Date.class, "date1"),
          cbuilder.parameter(java.sql.Date.class, "newdate"))));
      cquery.select(product);
      TypedQuery<Product> tquery = getEntityManager().createQuery(cquery);
      tquery.setParameter("date1", date1);
      tquery.setParameter("newdate", newdate);
      tquery.setMaxResults(productRef.length);
      List<Product> result = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "31";
      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 1 references, got: "
                + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass)
      throw new Fault("test_notBetweenDates failed");
  }

  /*
   * @testName: test_ANDconditionTT
   * 
   * @assertion_ids: PERSISTENCE:SPEC:424;
   * 
   * @test_Strategy: Both the conditions in the WHERE Clause are True and hence
   * the result is also TRUE Verify the results were accurately returned.
   */
  @SetupMethod(name = "setupOrderData")
  public void test_ANDconditionTT() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      cquery.where(cbuilder.and(
          cbuilder.equal(order.get(Order_.customer).get(Customer_.name),
              "Karen R. Tegan"),
          cbuilder.gt(order.get(Order_.totalPrice), 500)));
      cquery.select(order);
      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(orderRef.length);
      List<Order> result = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "6";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil
            .logErr("Did not get expected results.  Expected 1 reference, got: "
                + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("test_ANDconditionTT failed");
    }
  }

  /*
   * @testName: test_ANDconditionTF
   * 
   * @assertion_ids: PERSISTENCE:SPEC:424
   * 
   * @test_Strategy: First condition is True and Second is False and hence the
   * result is also False
   */
  @SetupMethod(name = "setupOrderData")
  public void test_ANDconditionTF() throws Fault {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      cquery.where(cbuilder.and(
          cbuilder.equal(order.get(Order_.customer).get(Customer_.name),
              "Karen R. Tegan"),
          cbuilder.gt(order.get(Order_.totalPrice), 10000)));
      cquery.select(order);
      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      List<Order> result = tquery.getResultList();

      if (result.size() == 0) {
        TestUtil.logTrace("Expected results received");
        pass = true;
      } else {
        TestUtil.logErr(
            "Did not get expected results.  Expected 0 references, got: "
                + result.size());
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("test_ANDconditionTF failed");
    }
  }

  /*
   * @testName: test_ANDconditionFT
   * 
   * @assertion_ids: PERSISTENCE:SPEC:424
   * 
   * @test_Strategy: First condition is FALSE and Second is TRUE and hence the
   * result is also False
   */
  @SetupMethod(name = "setupOrderData")
  public void test_ANDconditionFT() throws Fault {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      cquery
          .where(cbuilder.equal(order.get(Order_.customer).get(Customer_.id),
              "1001"), cbuilder.lt(order.get(Order_.totalPrice), 1000))
          .select(order);
      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(orderRef.length);
      List<Order> result = tquery.getResultList();

      if (result.size() == 0) {
        TestUtil.logTrace("Expected results received");
        pass = true;
      } else {
        TestUtil.logErr(
            "Did not get expected results.  Expected 0 references, got: "
                + result.size());
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("test_ANDconditionFT failed");
    }
  }

  /*
   * @testName: test_ANDconditionFF
   * 
   * @assertion_ids: PERSISTENCE:SPEC:424
   * 
   * @test_Strategy: First condition is FALSE and Second is FALSE and hence the
   * result is also False
   */
  @SetupMethod(name = "setupOrderData")
  public void test_ANDconditionFF() throws Fault {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      cquery
          .where(
              cbuilder.and(
                  cbuilder.equal(order.get(Order_.customer).get(Customer_.id),
                      "1001"),
                  cbuilder.gt(order.get(Order_.totalPrice), 10000)))
          .select(order);
      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(orderRef.length);
      List<Order> result = tquery.getResultList();

      if (result.size() == 0) {
        TestUtil.logTrace("Expected results received");
        pass = true;
      } else {
        TestUtil.logErr(
            "Did not get expected results.  Expected 0 references, got: "
                + result.size());
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("test_ANDconditionFF failed");
    }
  }

  /*
   * @testName: test_ORconditionTT
   * 
   * @assertion_ids: PERSISTENCE:SPEC:425
   * 
   * @test_Strategy: First condition is TRUE OR Second is TRUE and hence the
   * result is also TRUE
   */
  @SetupMethod(name = "setupOrderData")
  public void test_ORconditionTT() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      cquery.where(cbuilder.or(
          cbuilder.equal(order.get(Order_.customer).get(Customer_.name),
              "Karen R. Tegan"),
          cbuilder.gt(order.get(Order_.totalPrice), 5000))).select(order);
      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(orderRef.length);
      List<Order> result = tquery.getResultList();

      expectedPKs = new String[3];
      expectedPKs[0] = "6";
      expectedPKs[1] = "11";
      expectedPKs[2] = "16";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expectedPKs.length + " references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("test_ORconditionTT failed");
    }
  }

  /*
   * @testName: test_ORconditionTF
   * 
   * @assertion_ids: PERSISTENCE:SPEC:425
   * 
   * @test_Strategy: First condition is TRUE OR Second is FALSE and hence the
   * result is also TRUE
   */
  @SetupMethod(name = "setupOrderData")
  public void test_ORconditionTF() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      cquery.where(cbuilder.or(
          cbuilder.equal(order.get(Order_.customer).get(Customer_.name),
              "Karen R. Tegan"),
          cbuilder.gt(order.get(Order_.totalPrice), 10000))).select(order);
      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(orderRef.length);
      List<Order> result = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "6";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expectedPKs.length + " references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("test_ORconditionTF failed");
    }
  }

  /*
   * @testName: test_ORconditionFT
   * 
   * @assertion_ids: PERSISTENCE:SPEC:425
   * 
   * @test_Strategy: First condition is FALSE OR Second is TRUE and hence the
   * result is also TRUE
   */
  @SetupMethod(name = "setupOrderData")
  public void test_ORconditionFT() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      cquery.where(cbuilder.or(
          cbuilder.equal(order.get(Order_.customer).get(Customer_.id), "1001"),
          cbuilder.lt(order.get(Order_.totalPrice), 1000))).select(order);
      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(orderRef.length);
      List<Order> result = tquery.getResultList();

      expectedPKs = new String[7];
      expectedPKs[0] = "9";
      expectedPKs[1] = "10";
      expectedPKs[2] = "12";
      expectedPKs[3] = "13";
      expectedPKs[4] = "15";
      expectedPKs[5] = "19";
      expectedPKs[6] = "20";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expectedPKs.length + " references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("test_ORconditionFT failed");
    }
  }

  /*
   * @testName: test_ORconditionFF
   * 
   * @assertion_ids: PERSISTENCE:SPEC:425
   * 
   * @test_Strategy: First condition is FALSE OR Second is FALSE and hence the
   * result is also FALSE
   */
  @SetupMethod(name = "setupOrderData")
  public void test_ORconditionFF() throws Fault {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      cquery
          .where(
              cbuilder.or(
                  cbuilder.equal(order.get(Order_.customer).get(Customer_.id),
                      "1001"),
                  cbuilder.gt(order.get(Order_.totalPrice), 10000)))
          .select(order);
      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      List<Order> result = tquery.getResultList();

      if (result.size() == 0) {
        TestUtil.logTrace("Expected results received");
        pass = true;
      } else {
        TestUtil.logErr(
            "Did not get expected results.  Expected 0 references, got: "
                + result.size());
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("test_ORconditionFF failed");
    }
  }

  /*
   * @testName: test_groupByWhereClause
   * 
   * @assertion_ids: PERSISTENCE:SPEC:808
   * 
   * @test_Strategy: Test for Group By within a WHERE clause
   */
  @SetupMethod(name = "setupOrderData")
  public void test_groupByWhereClause() throws Fault {
    boolean pass = false;
    final String[] expectedCusts = new String[] { "Jonathan K. Smith",
        "Kellie A. Sanborn", "Robert E. Bissett" };

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> o = customer.join(Customer_.orders);
      cquery.where(cbuilder.between(o.get(Order_.totalPrice), 90D, 160D))
          .groupBy(customer.get(Customer_.name))
          .select(customer.get(Customer_.name));
      TypedQuery<String> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<String> result = tquery.getResultList();

      String[] output = (String[]) (result.toArray(new String[result.size()]));

      Arrays.sort(output);
      pass = Arrays.equals(expectedCusts, output);

      if (!pass) {
        TestUtil.logErr("Did not get expected results.  Expected 3 Customers : "
            + "Jonathan K. Smith, Kellie A. Sanborn and Robert E. Bissett. Received: "
            + result.size());
        for (String s : result) {
          TestUtil.logTrace(" Customer: " + s);
        }
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("test_groupByWhereClause failed");
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
  public void test_groupByHaving() throws Fault {
    boolean pass = false;
    final Long expectedGBR = Long.valueOf(2);
    final Long expectedCHA = Long.valueOf(4);

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Long> cquery = cbuilder.createQuery(Long.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery.groupBy(customer.get(Customer_.country).get(Country_.code)).having(
          customer.get(Customer_.country).get(Country_.code).in("GBR", "CHA"))
          .select(cbuilder.count(customer));
      TypedQuery<Long> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(country.length);
      List<Long> result = tquery.getResultList();

      int numOfExpected = 0;
      for (Long l : result) {
        TestUtil.logTrace("Check result received . . . ");
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
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("test_groupByHaving failed");
    }
  }

  /*
   * @testName: substringHavingExpressionTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:807; PERSISTENCE:JAVADOC:922;
   * 
   * @test_Strategy:Test for Functional Expression: substring in Having Clause
   * Select all customers with alias = fish
   */
  @SetupMethod(name = "setupAliasData")
  public void substringHavingExpressionTest() throws Fault {
    boolean pass = false;
    Object result;
    final Long expectedCount = Long.valueOf(2);

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("Executing Query");
      CriteriaQuery<Long> cquery = cbuilder.createQuery(Long.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Alias> a = customer.join(Customer_.aliases);
      Expression exp = cbuilder.equal(a.get(Alias_.alias),
          cbuilder.substring(cbuilder.parameter(String.class, "string1"),
              cbuilder.parameter(Integer.class, "int1"),
              cbuilder.parameter(Integer.class, "int2")));
      cquery.select(cbuilder.count(customer)).groupBy(a.get(Alias_.alias))
          .having(exp);
      TypedQuery<Long> tquery = getEntityManager().createQuery(cquery);
      tquery.setParameter("string1", "fish");
      tquery.setParameter("int1", Integer.valueOf(1));
      tquery.setParameter("int2", Integer.valueOf(4));
      result = tquery.getSingleResult();

      TestUtil.logTrace("Check results received .  .  .");
      if (expectedCount.equals(result)) {
        TestUtil.logTrace("Expected results received");
        pass = true;
      } else {
        TestUtil
            .logErr("Did not get expected results. Expected Count of 2, got: "
                + result);
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault(" substringHavingExpressionTest failed");
    }
  }

  /*
   * @testName: substringHavingExpressionPredicateArrayTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:923; PERSISTENCE:JAVADOC:946;
   * 
   * @test_Strategy:Test for Functional Expression: substring in Having Clause
   * Select all customers with alias = fish
   */
  @SetupMethod(name = "setupAliasData")
  public void substringHavingExpressionPredicateArrayTest() throws Fault {
    boolean pass = false;
    Object result;
    final Long expectedCount = Long.valueOf(2);

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("Executing Query");
      CriteriaQuery<Long> cquery = cbuilder.createQuery(Long.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Alias> a = customer.join(Customer_.aliases);
      Predicate[] pred = {
          cbuilder.equal(a.get(Alias_.alias),
              cbuilder.substring(cbuilder.parameter(String.class, "string1"),
                  cbuilder.parameter(Integer.class, "int1"),
                  cbuilder.parameter(Integer.class, "int2"))),
          cbuilder.equal(a.get(Alias_.alias),
              cbuilder.substring(cbuilder.parameter(String.class, "string1"),
                  cbuilder.parameter(Integer.class, "int1"),
                  cbuilder.parameter(Integer.class, "int2"))) };

      cquery.select(cbuilder.count(customer)).groupBy(a.get(Alias_.alias))
          .having(pred);
      TypedQuery<Long> tquery = getEntityManager().createQuery(cquery);
      tquery.setParameter("string1", "fish");
      tquery.setParameter("int1", Integer.valueOf(1));
      tquery.setParameter("int2", Integer.valueOf(4));
      result = tquery.getSingleResult();

      TestUtil.logTrace("Check results received .  .  .");
      if (expectedCount.equals(result)) {
        TestUtil.logTrace("Expected results received");
        pass = true;
      } else {
        TestUtil
            .logErr("Did not get expected results. Expected Count of 2, got: "
                + result);
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("substringHavingExpressionPredicateArrayTest failed");
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
  public void test_concatHavingClause() throws Fault {
    boolean pass = false;
    String result;
    final String expectedCustomer = "Margaret Mills";

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery.select(customer.get(Customer_.name))
          .groupBy(customer.get(Customer_.name))
          .having(cbuilder.equal(customer.get(Customer_.name), cbuilder
              .concat("Margaret ", cbuilder.parameter(String.class, "lname"))));
      TypedQuery<String> tquery = getEntityManager().createQuery(cquery);
      tquery.setParameter("lname", "Mills");
      result = tquery.getSingleResult();

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
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("test_concatHavingClause failed");
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
  public void test_lowerHavingClause() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    final Long expectedCount = Long.valueOf(2);

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Long> cquery = cbuilder.createQuery(Long.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery
          .select(cbuilder
              .count(customer.get(Customer_.country).get(Country_.code)))
          .groupBy(customer.get(Customer_.country).get(Country_.code)).having(
              cbuilder.equal(
                  cbuilder.lower(
                      customer.get(Customer_.country).get(Country_.code)),
                  "gbr"));
      TypedQuery<Long> tquery = getEntityManager().createQuery(cquery);
      List<Long> result = tquery.getResultList();

      if (result.size() == 1) {
        pass1 = true;
        Long l = (Long) result.get(0);
        if (l.equals(expectedCount)) {
          TestUtil.logTrace("Expected results received");
          pass2 = true;
        } else {
          TestUtil.logErr("Expected result:" + expectedCount + ", actual:" + l);
        }
      } else {
        TestUtil.logErr(
            "Did not get expected results. Expected 1, got: " + result.size());
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass1 || !pass2) {
      throw new Fault(" test_lowerHavingClause failed");
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
  public void test_upperHavingClause() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    final Long expectedCount = Long.valueOf(2);

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Long> cquery = cbuilder.createQuery(Long.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery
          .select(cbuilder
              .count(customer.get(Customer_.country).get(Country_.country)))
          .groupBy(customer.get(Customer_.country).get(Country_.country))
          .having(cbuilder.equal(
              cbuilder
                  .upper(customer.get(Customer_.country).get(Country_.country)),
              "ENGLAND"));
      TypedQuery<Long> tquery = getEntityManager().createQuery(cquery);
      List<Long> result = tquery.getResultList();

      if (result.size() == 1) {
        pass1 = true;
        Long l = (Long) result.get(0);
        if (l.equals(expectedCount)) {
          TestUtil.logTrace("Expected results received");
          pass2 = true;
        } else {
          TestUtil.logErr("Expected result:" + expectedCount + ", actual:" + l);
        }
      } else {
        TestUtil.logErr(
            "Did not get expected results. Expected 1, got: " + result.size());
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass1 || !pass2) {
      throw new Fault("test_upperHavingClause failed");
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
  public void test_lengthHavingClause() throws Fault {
    boolean pass = false;
    final String[] expectedCities = new String[] { "Burlington", "Chelmsford",
        "Roslindale" };

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Address> a = customer.join(Customer_.home);
      cquery.groupBy(a.get(Address_.city))
          .having(cbuilder.equal(cbuilder.length(a.get(Address_.city)), 10))
          .select(a.get(Address_.city));
      TypedQuery<String> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<String> result = tquery.getResultList();

      String[] output = (String[]) (result.toArray(new String[result.size()]));
      Arrays.sort(output);

      pass = Arrays.equals(expectedCities, output);

      if (!pass) {
        TestUtil
            .logErr("Did not get expected results.  Expected 2 Cities, got: "
                + result.size());
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("test_lengthHavingClause failed");
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
  public void test_locateHavingClause() throws Fault {
    boolean pass = false;
    final String[] expectedCusts = new String[] { "Alan E. Frechette",
        "Arthur D. Frechette" };

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery.groupBy(customer.get(Customer_.name))
          .having(cbuilder.gt(
              cbuilder.locate(customer.get(Customer_.name), "Frechette"), 0))
          .select(customer.get(Customer_.name));
      TypedQuery<String> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
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
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault(" test_locateHavingClause failed");
    }
  }

  /*
   * testName: test_trimHavingClause_01 assertion_ids: PERSISTENCE:SPEC:369.9
   * test_Strategy: Test for TRIM BOTH characters (blank) in the Having Clause
   *
   * DISABLE THIS TEST FOR NOW
   * 
   * @SetupMethod(name = "setupTrimData") public void test_trimHavingClause_01()
   * throws Fault { boolean pass = false; String result; final String expected =
   * " David R. Vincent             "; final String expected2 =
   * "'David R. Vincent'";
   * 
   * CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
   * 
   * 
   * try { getEntityTransaction().begin();
   * 
   * Trim tTrim = getEntityManager().find(Trim.class, "19");
   * TestUtil.logTrace("Trim(19):" + tTrim.toString()); if
   * (!tTrim.getName().equals(expected)) {
   * TestUtil.logErr("Name returned by find does not match expected");
   * TestUtil.logErr("Expected:|" + expected + "|, actual:|" + tTrim.getName() +
   * "|"); }
   * 
   * 
   * CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
   * Root<Trim> trim = cquery.from(Trim.class);
   * cquery.select(trim.get(Trim_.name)).groupBy(trim.get(Trim_.name)).having(
   * cbuilder.equal(cbuilder.trim(CriteriaBuilder.Trimspec.BOTH,
   * trim.get(Trim_.name)), expected2)); TypedQuery<String> tquery =
   * getEntityManager().createQuery(cquery); result = tquery.getSingleResult();
   * 
   * if (expected.equals(result)) { pass = true;
   * TestUtil.logTrace("Expected results received"); } else {
   * TestUtil.logErr("Did not get expected results," + "Expected:|" + expected +
   * "|, got: |" + result + "|"); }
   * 
   * getEntityTransaction().commit(); } catch (Exception e) {
   * TestUtil.logErr("Caught unexpected exception:", e);
   * 
   * }
   * 
   * if (!pass) { throw new Fault(" test_trimHavingClause_01 failed"); } }
   */

  /*
   * testName: test_trimHavingClause_02 assertion_ids: PERSISTENCE:SPEC:369.9
   * test_Strategy: Test for TRIM LEADING characters (blank) in the Having
   * Clause
   * 
   * DISABLE THIS TEST FOR NOW
   * 
   * @SetupMethod(name = "setupTrimData") public void test_trimHavingClause_02()
   * throws Fault { boolean pass = false; String result; final String expected =
   * " David R. Vincent             "; final String expected2 =
   * "'David R. Vincent             '";
   * 
   * CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
   * 
   * 
   * try { getEntityTransaction().begin(); Trim tTrim =
   * getEntityManager().find(Trim.class, "19"); TestUtil.logTrace("Trim(19):" +
   * tTrim.toString()); if (!tTrim.getName().equals(expected)) {
   * TestUtil.logErr("Name returned by find does not match expected");
   * TestUtil.logErr("Expected:|" + expected + "|, actual:|" + tTrim.getName() +
   * "|"); }
   * 
   * CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
   * Root<Trim> trim = cquery.from(Trim.class);
   * cquery.select(trim.get(Trim_.name)).groupBy(trim.get(Trim_.name)).having(
   * cbuilder.equal(cbuilder.trim(CriteriaBuilder.Trimspec.LEADING,
   * trim.get(Trim_.name)), expected2)); TypedQuery<String> tquery =
   * getEntityManager().createQuery(cquery); result = tquery.getSingleResult();
   * 
   * if (expected.equals(result)) { pass = true;
   * TestUtil.logTrace("Expected results received"); } else {
   * TestUtil.logErr("Did not get expected results," + "Expected:|" + expected +
   * "|, got: |" + result + "|"); }
   * 
   * getEntityTransaction().commit(); } catch (Exception e) {
   * TestUtil.logErr("Caught unexpected exception:", e);
   * 
   * }
   * 
   * if (!pass) { throw new Fault("test_trimHavingClause_02 failed"); } }
   */

  /*
   * testName: test_trimHavingClause_03 assertion_ids: PERSISTENCE:SPEC:369.9
   * test_Strategy: Test for TRIM TRAILING characters (blank) in the Having
   * Clause
   * 
   * DISABLE THIS TEST FOR NOW
   * 
   * @SetupMethod(name = "setupTrimData") public void test_trimHavingClause_03()
   * throws Fault {
   * 
   * boolean pass = false; String result; final String expected =
   * " David R. Vincent             "; final String expected2 =
   * "' David R. Vincent'";
   * 
   * CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
   * 
   * 
   * try { getEntityTransaction().begin(); Trim tTrim =
   * getEntityManager().find(Trim.class, "19"); TestUtil.logTrace("Trim(19):" +
   * tTrim.toString()); if (!tTrim.getName().equals(expected)) {
   * TestUtil.logErr("Name returned by find does not match expected");
   * TestUtil.logErr("Expected:|" + expected + "|, actual:|" + tTrim.getName() +
   * "|"); }
   * 
   * CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
   * Root<Trim> trim = cquery.from(Trim.class);
   * cquery.select(trim.get(Trim_.name)).groupBy(trim.get(Trim_.name)).having(
   * cbuilder.equal(cbuilder.trim(CriteriaBuilder.Trimspec.TRAILING,
   * trim.get(Trim_.name)), expected2)); TypedQuery<String> tquery =
   * getEntityManager().createQuery(cquery); result = tquery.getSingleResult();
   * 
   * if (expected.equals(result)) { pass = true;
   * TestUtil.logTrace("Expected results received"); } else {
   * TestUtil.logErr("Did not get expected results," + "Expected:|" + expected +
   * "|, got: |" + result + "|"); }
   * 
   * getEntityTransaction().commit(); } catch (Exception e) {
   * TestUtil.logErr("Caught unexpected exception:", e);
   * 
   * }
   * 
   * if (!pass) { throw new Fault("test_trimHavingClause_03 failed"); } }
   */

  /*
   * @testName: test_ABSHavingClause
   * 
   * @assertion_ids: PERSISTENCE:SPEC:369.5
   * 
   * @test_Strategy: Test for ABS expression in the Having Clause
   */
  @SetupMethod(name = "setupOrderData")
  public void test_ABSHavingClause() throws Fault {
    boolean pass = false;
    Object result;
    final Double expectedPrice = 10191.90D;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Double> cquery = cbuilder.createQuery(Double.class);
      Root<Order> order = cquery.from(Order.class);
      cquery.select(cbuilder.sum(order.get(Order_.totalPrice)))
          .groupBy(order.get(Order_.totalPrice))
          .having(cbuilder.equal(cbuilder.abs(order.get(Order_.totalPrice)),
              cbuilder.parameter(Double.class, "doubleValue")));
      TypedQuery<Double> tquery = getEntityManager().createQuery(cquery);
      tquery.setParameter("doubleValue", 5095.95D);
      result = tquery.getSingleResult();

      if (expectedPrice.equals(result)) {
        pass = true;
        TestUtil.logTrace("Expected results received");
      } else {
        TestUtil.logErr("test_ABSHavingClause:  Did not get expected results."
            + "Expected 10190, got: " + (Double) result);
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("test_ABSHavingClause failed");
    }
  }

  /*
   * @testName: test_SQRTWhereClause
   * 
   * @assertion_ids: PERSISTENCE:SPEC:369.6
   * 
   * @test_Strategy: Test for SQRT expression in the WHERE Clause
   */
  @SetupMethod(name = "setupOrderData")
  public void test_SQRTWhereClause() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("SQRT: Executing Query");
      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      cquery.where(cbuilder.gt(cbuilder.sqrt(order.get(Order_.totalPrice)),
          cbuilder.parameter(Double.class, "doubleValue"))).select(order);
      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      tquery.setParameter("doubleValue", 70D);
      tquery.setMaxResults(orderRef.length);
      List<Order> result = tquery.getResultList();

      expectedPKs = new String[2];
      expectedPKs[0] = "11";
      expectedPKs[1] = "16";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("test_SQRTWhereClause:  Did not get expected results."
            + "  Expected 2 references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault(" test_SQRTWhereClause failed");
    }

  }

  /*
   * @testName: subQueryGetSelectionGetParentTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1180; PERSISTENCE:JAVADOC:1181
   * 
   * @test_Strategy: Test SubQuery with a Having statement.
   */
  @SetupMethod(name = "setupOrderData")
  public void subQueryGetSelectionGetParentTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {

      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      cquery.select(order);

      Subquery<String> sq = cquery.subquery(String.class);
      Root<Order> order2 = sq.from(Order.class);

      AbstractQuery<?> parent = sq.getParent();
      if (parent != null) {
        TestUtil.logTrace("Received non-null parent query");
        if (parent instanceof CriteriaQuery || parent instanceof Subquery) {
          pass1 = true;
        } else {
          TestUtil.logErr(
              "getParent() did not return a query of type CriteriaQuery or Subquery:"
                  + parent);
        }
      } else {
        TestUtil.logErr("getParent() returned null");

      }

      Expression selExpression1 = order2.get(Order_.id);
      sq.select(selExpression1);
      Expression selExpression2 = sq.getSelection();
      if (selExpression1.getJavaType().equals(selExpression2.getJavaType())) {
        TestUtil.logTrace("Received expected selection expression"
            + selExpression1.getJavaType());
        pass2 = true;
      } else {
        TestUtil.logErr("Receive unexpected expression - Expected:"
            + selExpression1.getJavaType() + ", actual"
            + selExpression2.getJavaType());

      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass1 || !pass2) {
      throw new Fault("subQueryGetSelectionGetParentTest failed");
    }
  }

  /*
   * @testName: subQueryHavingExpressionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1184; PERSISTENCE:JAVADOC:1180;
   * PERSISTENCE:JAVADOC:1200; PERSISTENCE:JAVADOC:1193;
   * 
   * @test_Strategy: Test SubQuery with a Having statement.
   */
  @SetupMethod(name = "setupOrderData")
  public void subQueryHavingExpressionTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();

      /*
       * List q = getEntityManager().
       * createNativeQuery("SELECT o.ID FROM ORDER_TABLE o WHERE o.ID IN (SELECT o2.ID FROM ORDER_TABLE o2 GROUP BY o2.ID HAVING (o2.ID < '2' ) ) "
       * ).getResultList(); for (Object obj : q) { String o = (String) obj;
       * TestUtil.logTrace("id:" + o); }
       */

      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      cquery.select(order);

      Subquery<String> sq = cquery.subquery(String.class);
      Root<Order> order2 = sq.from(Order.class);

      AbstractQuery<?> parent = sq.getParent();
      if (parent != null) {
        TestUtil.logTrace("Received non-null parent query");
        if (parent instanceof CriteriaQuery || parent instanceof Subquery) {
          pass1 = true;
        } else {
          TestUtil.logErr(
              "getParent() did not return a query of type CriteriaQuery or Subquery:"
                  + parent);
        }
      } else {
        TestUtil.logErr("SubQuery.getParent returned null");
      }

      Expression selExpression1 = order2.get(Order_.id);
      sq.select(selExpression1);
      Expression selExpression2 = sq.getSelection();
      if (selExpression1.getJavaType().equals(selExpression2.getJavaType())) {
        TestUtil.logTrace("Received expected selection expression");
        pass2 = true;
      } else {
        TestUtil.logErr("SubQuery.getParent returned null");

      }

      sq.groupBy(order2.get(Order_.id));
      Predicate pred = sq.getGroupRestriction();
      if (pred == null) {
        TestUtil.logTrace(
            "Received null result from subquery.getGroupRestriction() when no restrictions exist");
        Expression exp = cbuilder.lessThan(order2.get(Order_.id), "2");
        sq.having(exp);

        // this having overrides the previous
        Expression exp2 = cbuilder.lessThanOrEqualTo(order2.get(Order_.id),
            "1");
        sq.having(exp2);
        pred = sq.getGroupRestriction();
        if (pred != null) {
          TestUtil.logTrace(
              "Received non-null result from subquery.getGroupRestriction() when restrictions do exist");

          cquery.where(cbuilder.in(order.get(Order_.id)).value(sq));
          TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
          List<Order> result = tquery.getResultList();
          if (TestUtil.traceflag) {
            for (Order o : result) {
              TestUtil.logTrace("id:" + o.getId());
            }
          }

          String[] expectedPKs = new String[1];
          expectedPKs[0] = "1";

          if (!checkEntityPK(result, expectedPKs)) {
            TestUtil.logErr("Did not get expected results.");

          } else {
            TestUtil.logTrace("Expected results received");
            pass3 = true;
          }
        } else {
          TestUtil.logErr(
              "Received null result from subquery.getGroupRestriction() when restrictions do exist");

        }
      } else {
        TestUtil.logErr(
            "Expected null result from subquery.getGroupRestriction() when no restrictions exist:"
                + pred.toString());

      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass1 || !pass2 | !pass3) {
      throw new Fault("subQueryHavingExpressionTest failed");
    }
  }

  /*
   * @testName: subQueryHavingPredicateArrayTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1185; PERSISTENCE:JAVADOC:1201;
   * 
   * @test_Strategy: Test SubQuery with a Having statement.
   */
  @SetupMethod(name = "setupOrderData")
  public void subQueryHavingPredicateArrayTest() throws Fault {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();

      /*
       * List q = getEntityManager().
       * createNativeQuery("SELECT o FROM ORDER_TABLE o WHERE o.ID IN (SELECT o2.ID FROM ORDER_TABLE o2 GROUP BY o2.ID HAVING (o2.ID > '1' AND o2.ID < '2' ) ) "
       * ).getResultList(); for (Object obj : q) { String o = (String) obj;
       * TestUtil.logTrace("id:" + o); }
       */

      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      cquery.select(order);

      Subquery<String> sq = cquery.subquery(String.class);
      Root<Order> order2 = sq.from(Order.class);

      sq.select(order2.get(Order_.id));
      sq.groupBy(order2.get(Order_.id));
      Predicate[] predArray1 = {
          cbuilder.greaterThan(order2.get(Order_.id), "3"),
          cbuilder.lessThan(order2.get(Order_.id), "4") };
      sq.having(predArray1);

      // this having overrides the previous
      Predicate[] predArray2 = {
          cbuilder.greaterThan(order2.get(Order_.id), "1"),
          cbuilder.lessThan(order2.get(Order_.id), "2") };
      sq.having(predArray2);

      cquery.where(cbuilder.in(order.get(Order_.id)).value(sq));
      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      List<Order> result = tquery.getResultList();
      if (TestUtil.traceflag) {
        for (Order o : result) {
          TestUtil.logTrace(
              "id:" + o.getId() + ", totalprice:" + o.getTotalPrice());
        }
      }
      String[] expectedPKs = new String[10];
      expectedPKs[0] = "10";
      expectedPKs[1] = "11";
      expectedPKs[2] = "12";
      expectedPKs[3] = "13";
      expectedPKs[4] = "14";
      expectedPKs[5] = "15";
      expectedPKs[6] = "16";
      expectedPKs[7] = "17";
      expectedPKs[8] = "18";
      expectedPKs[9] = "19";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("Did not get expected results.");
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("subQueryHavingPredicateArrayTest failed");
    }
  }

  /*
   * @testName: subQuerySelectExpressionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1186
   * 
   * @test_Strategy: Test SubQuery with a Select (Expression) statement.
   */
  @SetupMethod(name = "setupOrderData")
  public void subQuerySelectExpressionTest() throws Fault {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      /*
       * List q = getEntityManager().
       * createNativeQuery("SELECT o FROM ORDER_TABLE o WHERE o.TOTALPRICE IN (SELECT MAX(o2.TOTALPRICE) FROM ORDER_TABLE o2) "
       * ).getResultList(); for (Order o : result) {
       * TestUtil.logTrace("id:"+o.getId()+", totalprice:"+o.getTotalPrice()); }
       */
      String[] expectedPKs = new String[2];
      expectedPKs[0] = "11";
      expectedPKs[1] = "16";

      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      cquery.select(order);

      Subquery<Double> sq = cquery.subquery(Double.class);
      Root<Order> order2 = sq.from(Order.class);
      sq.select(cbuilder.min(order2.get(Order_.totalPrice)));
      // this select overrides the previous
      sq.select(cbuilder.max(order2.get(Order_.totalPrice)));
      cquery.where(cbuilder.in(order.get(Order_.totalPrice)).value(sq));

      TypedQuery<Order> tq = getEntityManager().createQuery(cquery);

      List<Order> result = tq.getResultList();
      if (TestUtil.traceflag) {
        for (Order o : result) {
          TestUtil.logTrace(
              "id:" + o.getId() + ", totalprice:" + o.getTotalPrice());
        }
      }

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("Did not get expected results.");
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("subQuerySelectExpressionTest failed");
    }
  }

  /*
   * @testName: subQueryWhereExpressionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1187; PERSISTENCE:JAVADOC:1204
   * 
   * @test_Strategy: Test SubQuery with a Where Expression statement. SELECT o
   * FROM ORDER_TABLE o WHERE o.ID IN (SELECT o2.ID FROM ORDER_TABLE o2 WHERE
   * (o2.ID <= 1 )
   */
  @SetupMethod(name = "setupOrderData")
  public void subQueryWhereExpressionTest() throws Fault {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();

      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      cquery.select(order);

      Subquery<String> sq = cquery.subquery(String.class);

      TestUtil.logTrace(
          "Received null from subquery.getRestriction() when no restrictions exist");

      Root<Order> order2 = sq.from(Order.class);

      sq.select(order2.get(Order_.id));

      Expression exp1 = cbuilder.lessThan(order2.get(Order_.id), "2");
      sq.where(exp1);
      Expression exp2 = cbuilder.lessThanOrEqualTo(order2.get(Order_.id), "1");
      sq.where(exp2);

      cquery.where(cbuilder.in(order.get(Order_.id)).value(sq));
      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      List<Order> result = tquery.getResultList();

      String[] expectedPKs = new String[1];
      expectedPKs[0] = "1";

      if (TestUtil.traceflag) {
        for (Order o : result) {
          TestUtil.logTrace("id:" + o.getId());
        }
      }

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("Did not get expected results.");
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("subQueryWhereExpressionTest failed");
    }
  }

  /*
   * @testName: subQueryWherePredicateArrayTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1188; PERSISTENCE:JAVADOC:1205;
   * 
   * @test_Strategy: Test SubQuery with a Where Predicate[] statement.
   */
  @SetupMethod(name = "setupOrderData")
  public void subQueryWherePredicateArrayTest() throws Fault {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();

      /*
       * List q = getEntityManager().
       * createNativeQuery("SELECT o FROM ORDER_TABLE o WHERE o.ID IN (SELECT o2.ID FROM ORDER_TABLE o2 WHERE ((o2.ID > 1) AND (o2.ID < 2)) "
       * ).getResultList(); for (Object obj : q) { String o = (String) obj;
       * TestUtil.logTrace("id:" + o); }
       */

      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      cquery.select(order);

      Subquery<String> sq = cquery.subquery(String.class);
      Root<Order> order2 = sq.from(Order.class);

      sq.select(order2.get(Order_.id));

      Predicate[] predArray1 = {
          cbuilder.greaterThan(order2.get(Order_.id), "3"),
          cbuilder.lessThan(order2.get(Order_.id), "4") };
      sq.where(predArray1);

      // this having overrides the previous
      Predicate[] predArray2 = {
          cbuilder.greaterThan(order2.get(Order_.id), "1"),
          cbuilder.lessThan(order2.get(Order_.id), "2") };
      sq.where(predArray2);

      cquery.where(cbuilder.in(order.get(Order_.id)).value(sq));
      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      List<Order> result = tquery.getResultList();

      String[] expectedPKs = new String[10];
      expectedPKs[0] = "10";
      expectedPKs[1] = "11";
      expectedPKs[2] = "12";
      expectedPKs[3] = "13";
      expectedPKs[4] = "14";
      expectedPKs[5] = "15";
      expectedPKs[6] = "16";
      expectedPKs[7] = "17";
      expectedPKs[8] = "18";
      expectedPKs[9] = "19";

      if (TestUtil.traceflag) {
        for (Order o : result) {
          TestUtil.logTrace("id:" + o.getId());
        }
      }

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("Did not get expected results.");
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("subQueryWherePredicateArrayTest failed");
    }
  }

  /*
   * @testName: subQueryDistinctTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1178; PERSISTENCE:JAVADOC:1195;
   * PERSISTENCE:JAVADOC:1196; PERSISTENCE:JAVADOC:1202;
   * 
   * @test_Strategy: Test SubQuery with a Where Predicate[] statement.
   */
  @SetupMethod(name = "setupOrderData")
  public void subQueryDistinctTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass4 = false;
    boolean pass5 = true;
    boolean pass6 = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      /*
       * List<Object[]> q = getEntityManager().
       * createQuery("SELECT l.id, l.product.id, l.product.price FROM LineItem l WHERE l.product.id = (SELECT DISTINCT l.product.id FROM LineItem l WHERE l.product.price > 5000 )"
       * ).getResultList(); for (Object[] o : q) {
       * TestUtil.logTrace("lineitem id:" + o[0] + ", prod id:" + o[1]+
       * ", prod price:" + o[2]); }
       */
      CriteriaQuery<LineItem> cquery = cbuilder.createQuery(LineItem.class);
      Root<LineItem> lineItem = cquery.from(LineItem.class);
      cquery.select(lineItem);

      Subquery<String> sq = cquery.subquery(String.class);
      String name = sq.getResultType().getName();
      if (name.equals("java.lang.String")) {
        TestUtil.logTrace("Received expected type:" + name);
        pass1 = true;
      } else {
        TestUtil.logErr("Expected type: java.lang.String, actual: " + name);

      }

      Root<LineItem> lineItem2 = sq.from(LineItem.class);
      sq.where(cbuilder.greaterThan(
          lineItem2.get(LineItem_.product).get(Product_.price), 5000D))
          .select(lineItem2.get(LineItem_.product).get(Product_.id));
      sq.distinct(true);
      boolean b = sq.isDistinct();
      if (b) {
        TestUtil.logTrace("Received expected isDistinct result:" + b);
        pass2 = true;
      } else {
        TestUtil.logErr("Expected isDistinct: true, actual:" + b);

      }

      cquery.where(
          cbuilder.equal(lineItem.get(LineItem_.product).get(Product_.id), sq));

      Set<Root<?>> s = sq.getRoots();
      if (s.size() == 1) {
        TestUtil.logTrace("Received expected size:" + s.size());
        for (Root r : s) {
          pass4 = true;
          name = r.getJavaType().getName();
          if (name.equals("com.sun.ts.tests.jpa.common.schema30.LineItem")) {
            TestUtil.logTrace("Received expected root:" + name);
          } else {
            TestUtil.logErr(
                "Expected Root: com.sun.ts.tests.jpa.common.schema30.LineItem, actual:"
                    + name);
            pass5 = false;
          }
        }
      } else {
        TestUtil.logErr("Expected size: 1, actual:" + s.size());

      }

      TypedQuery<LineItem> tquery = getEntityManager().createQuery(cquery);
      List<LineItem> result = tquery.getResultList();

      String[] expectedPKs = new String[2];
      expectedPKs[0] = "33";
      expectedPKs[1] = "44";

      if (TestUtil.traceflag) {
        for (LineItem l : result) {
          TestUtil.logTrace(" lineitem id:" + l.getId() + ", prod id:"
              + l.getProduct().getId() + ", prod price:"
              + l.getProduct().getPrice());
        }
      }

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("Did not get expected results.");

      } else {
        TestUtil.logTrace("Expected results received");
        pass6 = true;
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass1 || !pass2 || !pass4 || !pass5 || !pass6) {
      throw new Fault("subQueryDistinctTest failed");
    }
  }

  /*
   * @testName: getCorrelatedJoinsTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1179;
   * 
   * @test_Strategy: Test getting correlated joins from subquery.
   */
  @CleanupMethod(name = "cleanupNoData")
  public void getCorrelatedJoinsTest() throws Fault {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery.select(customer);
      Subquery<Order> sq = cquery.subquery(Order.class);
      Set cJoins = sq.getCorrelatedJoins();
      if (cJoins != null) {
        if (cJoins.size() == 0) {
          TestUtil.logTrace(
              "Received expected 0 correlated joins from subquery.getCorrelatedJoins() when none exist");

          // correlate subquery
          Join<Customer, Order> sqo = sq
              .correlate(customer.join(Customer_.orders));
          sq.select(sqo);
          cJoins = sq.getCorrelatedJoins();
          if (cJoins != null) {
            if (cJoins.size() == 1) {
              TestUtil.logTrace(
                  "Received expected 1 correlated join from subquery.getCorrelatedJoins()");
              pass = true;
            } else {
              TestUtil.logErr("Received " + cJoins.size()
                  + " correlated joins from subquery.getCorrelatedJoins() when 1 exist");

            }
          } else {
            TestUtil.logErr(
                "Received null from subquery.getCorrelatedJoins() when 1 correlated join exists");

          }
        } else {
          TestUtil.logErr("Received " + cJoins.size()
              + " unexpected correlated joins from subquery.getCorrelatedJoins() when non exist");

        }
      } else {
        TestUtil.logErr(
            "Received null from subquery.getCorrelatedJoins() instead of empty set when non exist");

      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault(" getCorrelatedJoinsTest failed");
    }
  }

  /*
   * @testName: test_subquery_exists_02
   * 
   * @assertion_ids: PERSISTENCE:SPEC:791;
   * 
   * @test_Strategy: Test for EXISTS in the Where Clause for a correlated query.
   * Select the customers with orders where total order > 1500.
   */
  @SetupMethod(name = "setupOrderData")
  public void test_subquery_exists_02() throws Fault {

    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery.select(customer);
      // create correlated subquery
      Subquery<Order> sq = cquery.subquery(Order.class);
      Root<Customer> sqc = sq.correlate(customer);
      Join<Customer, Order> sqo = sqc.join(Customer_.orders);
      sq.where(cbuilder.gt(sqo.get(Order_.totalPrice), 1500D));
      cquery.where(cbuilder.exists(sq));
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> result = tquery.getResultList();

      expectedPKs = new String[4];
      expectedPKs[0] = "5";
      expectedPKs[1] = "10";
      expectedPKs[2] = "14";
      expectedPKs[3] = "15";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil
            .logErr("test_subquery_exists_02:  Did not get expected results. "
                + " Expected 4 references, got: " + result.size());

      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault(" test_subquery_exists_02 failed");
    }
  }

  /*
   * @testName: test_subquery_like
   * 
   * @assertion_ids: PERSISTENCE:SPEC:791;PERSISTENCE:SPEC:792;
   * PERSISTENCE:SPEC:800;PERSISTENCE:SPEC:801; PERSISTENCE:SPEC:802
   * 
   * @test_Strategy: Use LIKE expression in a sub query. Select the customers
   * with name like Caruso. The name Caruso is derived in the subquery.
   */
  @SetupMethod(name = "setupOrderData")
  public void test_subquery_like() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      cquery.select(order);
      // create correlated subquery
      Subquery<Customer> sq = cquery.subquery(Customer.class);
      Root<Order> sqo = sq.correlate(order);
      Join<Order, Customer> sqc = sqo.join(Order_.customer);
      sq.where(cbuilder.like(sqc.get(Customer_.name), "%Caruso")).select(sqc);
      cquery.where(cbuilder.exists(sq));
      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(orderRef.length);
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
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault(" test_subquery_like failed");
    }
  }

  /*
   * @testName: test_subquery_in
   * 
   * @assertion_ids: PERSISTENCE:SPEC:800;PERSISTENCE:SPEC:801;
   * PERSISTENCE:SPEC:802; PERSISTENCE:SPEC:352.2
   * 
   * @test_Strategy: Use IN expression in a sub query.
   */
  @SetupMethod(name = "setupCustomerData")
  public void test_subquery_in() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery.select(customer);
      Subquery<String> sq = cquery.subquery(String.class);
      Root<Customer> sqc = sq.correlate(customer);
      Join<Customer, Address> w = sqc.join(Customer_.work);
      sq.select(w.get(Address_.state));
      sq.where(cbuilder.equal(w.get(Address_.state),
          cbuilder.parameter(String.class, "state")));
      cquery.where(customer.get(Customer_.home).get(Address_.state).in(sq));
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setParameter("state", "MA");
      tquery.setMaxResults(customerRef.length);
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
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("test_subquery_in failed");
    }
  }

  /*
   * @testName: test_subquery_between
   * 
   * @assertion_ids: PERSISTENCE:SPEC:800;PERSISTENCE:SPEC:801;
   * PERSISTENCE:SPEC:802
   * 
   * @test_Strategy: Use BETWEEN expression in a sub query. Select the customers
   * whose orders total price is between 1000 and 2000.
   */
  @SetupMethod(name = "setupOrderData")
  public void test_subquery_between() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("Execute query for test_subquery_between");
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Subquery<Order> sq = cquery.subquery(Order.class);
      Root<Customer> sqc = sq.correlate(customer);
      Join<Customer, Order> sqo = sqc.join(Customer_.orders);
      sq.where(cbuilder.between(sqo.get(Order_.totalPrice), 1000D, 1200D));
      cquery.where(cbuilder.exists(sq));
      cquery.select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> result = tquery.getResultList();

      expectedPKs = new String[5];
      expectedPKs[0] = "1";
      expectedPKs[1] = "3";
      expectedPKs[2] = "7";
      expectedPKs[3] = "8";
      expectedPKs[4] = "13";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("test_subquery_between:  Did not get expected "
            + " results.  Expected 5 references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault(" test_subquery_between failed");
    }

  }

  /*
   * @testName: test_subquery_join
   * 
   * @assertion_ids: PERSISTENCE:SPEC:800;PERSISTENCE:SPEC:801;
   * PERSISTENCE:SPEC:802; PERSISTENCE:SPEC:765; PERSISTENCE:JAVADOC:1173;
   * PERSISTENCE:JAVADOC:1038;
   * 
   * @test_Strategy: Use JOIN in a sub query. Select the customers whose orders
   * have line items of quantity > 2.
   */
  @SetupMethod(name = "setupOrderData")
  public void test_subquery_join() throws Fault {

    boolean pass1 = false;
    boolean pass2 = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> o = customer.join(Customer_.orders);
      Subquery<Order> sq = cquery.subquery(Order.class);
      Join<Customer, Order> sqo = sq.correlate(o);
      From f = sqo.getCorrelationParent();
      String name = f.getModel().getBindableJavaType().getSimpleName();
      if (name.equals("Order")) {
        TestUtil.logTrace("Received expected CorrelationParent:" + name);
        pass1 = true;
      } else {
        TestUtil.logErr("Expected CorrelationParent: Order, actual:" + name);

      }
      Join<Order, LineItem> sql = sqo.join(Order_.lineItemsCollection);
      sq.where(cbuilder.gt(sql.get(LineItem_.quantity), 3)).select(sqo);
      cquery.select(customer);
      cquery.where(cbuilder.exists(sq));
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> result = tquery.getResultList();

      expectedPKs = new String[5];
      expectedPKs[0] = "6";
      expectedPKs[1] = "9";
      expectedPKs[2] = "11";
      expectedPKs[3] = "13";
      expectedPKs[4] = "16";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("test_subquery_join:  Did not get expected results."
            + "  Expected 5 references, got: " + result.size());

      } else {
        TestUtil.logTrace("Expected results received");
        pass2 = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass1 || !pass2) {
      throw new Fault(" test_subquery_join failed");
    }
  }

  /*
   * @testName: getCorrelationParentIllegalStateExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1039; PERSISTENCE:JAVADOC:1041;
   * 
   * @test_Strategy:
   */
  @SetupMethod(name = "setupOrderData")
  public void getCorrelationParentIllegalStateExceptionTest() throws Fault {

    boolean pass1 = false;
    boolean pass2 = false;
    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Subquery<Order> sq = cquery.subquery(Order.class);
      Root<Order> order = sq.from(Order.class);
      Join<Order, LineItem> sql = order.join(Order_.lineItemsCollection);
      boolean b = sql.isCorrelated();
      if (!b) {
        TestUtil
            .logTrace("Received expected result from isCorrelated(): false");
        pass1 = true;
      } else {
        TestUtil.logErr("Expected false from isCorrelated(), actual:" + b);

      }
      try {
        From f = sql.getCorrelationParent();

        TestUtil.logErr("Did not throw IllegalStateException");
        TestUtil.logErr("getCorrelationParent() returned:" + f);
      } catch (IllegalStateException ise) {
        TestUtil.logTrace("Received expected IllegalStateException");
        pass2 = true;
      } catch (Exception ex) {

        TestUtil.logErr("Received unexpected exception", ex);
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass1 || !pass2) {
      throw new Fault("getCorrelationParentIllegalStateExceptionTest failed");
    }
  }

  /*
   * @testName: subquerySetJoinTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1170; PERSISTENCE:JAVADOC:1175;
   * PERSISTENCE:JAVADOC:1137; PERSISTENCE:JAVADOC:1171;
   * 
   * @test_Strategy: Use JOIN in a sub query.
   */
  @SetupMethod(name = "setupOrderData")
  public void subquerySetJoinTest() throws Fault {

    boolean pass1 = false;
    boolean pass2 = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      SetJoin<Customer, Order> o = customer.join(Customer_.orders2);
      PluralAttribute pa = o.getModel();
      String paName = pa.getCollectionType().name();
      if (paName.equals("SET")) {
        TestUtil.logTrace("Received expected PluralAttribute:" + paName);
        pass1 = true;
      } else {
        TestUtil.logTrace("Expected PluralAttribute: SET, actual: " + paName);

      }

      SetAttribute sa = o.getModel();
      String saName = sa.getName();
      if (saName.equals("orders2")) {
        TestUtil.logTrace("Received expected SetAttribute:" + saName);
      } else {
        TestUtil.logTrace("Expected SetAttribute: orders2, actual: " + saName);

      }
      Subquery<Order> sq = cquery.subquery(Order.class);
      Join<Customer, Order> sqo = sq.correlate(o);
      Join<Order, LineItem> sql = sqo.join(Order_.lineItemsCollection);
      sq.where(cbuilder.gt(sql.get(LineItem_.quantity), 3)).select(sqo);
      cquery.select(customer);
      cquery.where(cbuilder.exists(sq));
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> result = tquery.getResultList();

      expectedPKs = new String[5];
      expectedPKs[0] = "6";
      expectedPKs[1] = "9";
      expectedPKs[2] = "11";
      expectedPKs[3] = "13";
      expectedPKs[4] = "16";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("test_subquery_join:  Did not get expected results."
            + "  Expected 5 references, got: " + result.size());

      } else {
        TestUtil.logTrace("Expected results received");
        pass2 = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass1 || !pass2) {
      throw new Fault("subquerySetJoinTest failed");
    }
  }

  /*
   * @testName: subqueryListJoinTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1176; PERSISTENCE:JAVADOC:1138;
   * 
   * @test_Strategy: Use JOIN in a sub query.
   */
  @SetupMethod(name = "setupOrderData")
  public void subqueryListJoinTest() throws Fault {

    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      ListJoin<Customer, Order> o = customer.join(Customer_.orders3);
      Subquery<Order> sq = cquery.subquery(Order.class);
      Join<Customer, Order> sqo = sq.correlate(o);
      Join<Order, LineItem> sql = sqo.join(Order_.lineItemsCollection);
      sq.where(cbuilder.gt(sql.get(LineItem_.quantity), 3)).select(sqo);
      cquery.select(customer);
      cquery.where(cbuilder.exists(sq));
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> result = tquery.getResultList();

      expectedPKs = new String[5];
      expectedPKs[0] = "6";
      expectedPKs[1] = "9";
      expectedPKs[2] = "11";
      expectedPKs[3] = "13";
      expectedPKs[4] = "16";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("test_subquery_join:  Did not get expected results."
            + "  Expected 5 references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("subqueryListJoinTest failed");
    }
  }

  /*
   * @testName: subqueryCollectionJoinTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1174
   * 
   * @test_Strategy: Use JOIN in a sub query.
   */
  @SetupMethod(name = "setupOrderData")
  public void subqueryCollectionJoinTest() throws Fault {

    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> o = customer.join(Customer_.orders);
      Subquery<Order> sq = cquery.subquery(Order.class);
      Join<Customer, Order> sqo = sq.correlate(o);
      Join<Order, LineItem> sql = sqo.join(Order_.lineItemsCollection);
      sq.where(cbuilder.gt(sql.get(LineItem_.quantity), 3)).select(sqo);
      cquery.select(customer);
      cquery.where(cbuilder.exists(sq));
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> result = tquery.getResultList();

      expectedPKs = new String[5];
      expectedPKs[0] = "6";
      expectedPKs[1] = "9";
      expectedPKs[2] = "11";
      expectedPKs[3] = "13";
      expectedPKs[4] = "16";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("test_subquery_join:  Did not get expected results."
            + "  Expected 5 references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("subqueryCollectionJoinTest failed");
    }
  }

  /*
   * @testName: test_subquery_ALL_GT
   * 
   * @assertion_ids: PERSISTENCE:SPEC:794; PERSISTENCE:SPEC:797;
   * PERSISTENCE:SPEC:766; PERSISTENCE:SPEC:793; PERSISTENCE:SPEC:799
   * 
   * @test_Strategy: Test for ALL in a subquery with the relational operator
   * ">". Select all customers where total price of orders is greater than ALL
   * the values in the result set.
   */
  @SetupMethod(name = "setupOrderData")
  public void test_subquery_ALL_GT() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> o = customer.join(Customer_.orders);
      Subquery<Double> sq = cquery.subquery(Double.class);
      Root<Order> sqo = sq.from(Order.class);
      Join<Order, LineItem> l = sqo.join(Order_.lineItemsCollection);
      sq.where(cbuilder.gt(l.get(LineItem_.quantity), 3));
      sq.select(sqo.get(Order_.totalPrice));
      cquery.select(customer);
      cquery.where(cbuilder.gt(o.get(Order_.totalPrice), cbuilder.all(sq)));
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> result = tquery.getResultList();

      expectedPKs = new String[4];
      expectedPKs[0] = "5";
      expectedPKs[1] = "10";
      expectedPKs[2] = "14";
      expectedPKs[3] = "15";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("test_subquery_ALL_GT:  Did not get expected results. "
            + " Expected 4 references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {

      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault(" test_subquery_ALL_GT failed");
    }
  }

  /*
   * @testName: test_subquery_ALL_LT
   * 
   * @assertion_ids: PERSISTENCE:SPEC:794; PERSISTENCE:SPEC:797
   * 
   * @test_Strategy: Test for ALL in a subquery with the relational operator
   * "<". Select all customers where total price of orders is less than ALL the
   * values in the result set.
   */
  @SetupMethod(name = "setupOrderData")
  public void test_subquery_ALL_LT() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> o = customer.join(Customer_.orders);
      cquery.select(customer);
      Subquery<Double> sq = cquery.subquery(Double.class);
      Root<Order> sqo = sq.from(Order.class);
      Join<Order, LineItem> sql = sqo.join(Order_.lineItemsCollection);
      sq.where(cbuilder.gt(sql.get(LineItem_.quantity), 3));
      sq.select(sqo.get(Order_.totalPrice));
      cquery.where(cbuilder.lt(o.get(Order_.totalPrice), cbuilder.all(sq)));
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> result = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "12";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("test_subquery_ALL_LT:  Did not get expected results."
            + "  Expected 1 reference, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("test_subquery_ALL_LT failed");
    }
  }

  /*
   * @testName: test_subquery_ALL_EQ
   * 
   * @assertion_ids: PERSISTENCE:SPEC:794; PERSISTENCE:SPEC:797
   * 
   * @test_Strategy: Test for ALL in a subquery with the relational operator
   * "=". Select all customers where total price of orders is = ALL the values
   * in the result set. The result set contains the min of total price of
   * orders.
   */
  @SetupMethod(name = "setupOrderData")
  public void test_subquery_ALL_EQ() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> o = customer.join(Customer_.orders);
      cquery.select(customer);
      Subquery<Double> sq = cquery.subquery(Double.class);
      Root<Order> sqo = sq.from(Order.class);
      sq.select(cbuilder.min(sqo.get(Order_.totalPrice)));
      cquery.where(cbuilder.equal(o.get("totalPrice"), cbuilder.all(sq)));
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> result = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "12";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("test_subquery_ALL_EQ:  Did not get expected results. "
            + " Expected 1 reference, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("test_subquery_ALL_EQ failed");
    }

  }

  /*
   * @testName: test_subquery_ALL_LTEQ
   * 
   * @assertion_ids: PERSISTENCE:SPEC:794; PERSISTENCE:SPEC:797
   * 
   * @test_Strategy: Test for ALL in a subquery with the relational operator
   * "<=". Select all customers where total price of orders is <= ALL the values
   * in the result set. The result set contains the total price of orders where
   * count of lineItems > 3.
   */
  @SetupMethod(name = "setupOrderData")
  public void test_subquery_ALL_LTEQ() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> o = customer.join(Customer_.orders);
      Subquery<Double> sq = cquery.subquery(Double.class);
      Root<Order> sqo = sq.from(Order.class);
      Join<Order, LineItem> sql = sqo.join(Order_.lineItemsCollection);
      sq.select(sqo.get(Order_.totalPrice));
      sq.where(cbuilder.gt(sql.get(LineItem_.quantity), 3));
      cquery.where(cbuilder.le(o.get(Order_.totalPrice), cbuilder.all(sq)))
          .select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> result = tquery.getResultList();

      expectedPKs = new String[2];
      expectedPKs[0] = "9";
      expectedPKs[1] = "12";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil
            .logErr("test_subquery_ALL_LTEQ:  Did not get expected results.  "
                + "Expected 2 references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("test_subquery_ALL_LTEQ failed");
    }
  }

  /*
   * @testName: test_subquery_ALL_GTEQ
   * 
   * @assertion_ids: PERSISTENCE:SPEC:794; PERSISTENCE:SPEC:797
   * 
   * @test_Strategy: Test for ALL in a subquery with the relational operator
   * ">=". Select all customers where total price of orders is >= ALL the values
   * in the result set.
   */
  @SetupMethod(name = "setupOrderData")
  public void test_subquery_ALL_GTEQ() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> o = customer.join(Customer_.orders);
      Subquery<Double> sq = cquery.subquery(Double.class);
      Root<Order> sqo = sq.from(Order.class);
      Join<Order, LineItem> sql = sqo.join(Order_.lineItemsCollection);
      sq.select(sqo.get(Order_.totalPrice));
      sq.where(cbuilder.ge(sql.get(LineItem_.quantity), 3));
      cquery.where(cbuilder.ge(o.get(Order_.totalPrice), cbuilder.all(sq)))
          .select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> result = tquery.getResultList();

      expectedPKs = new String[2];
      expectedPKs[0] = "10";
      expectedPKs[1] = "14";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil
            .logErr("test_subquery_ALL_GTEQ:  Did not get expected results. "
                + " Expected 2 references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("test_subquery_ALL_GTEQ failed");
    }
  }

  /*
   * @testName: test_subquery_ALL_NOTEQ
   * 
   * @assertion_ids: PERSISTENCE:SPEC:794; PERSISTENCE:SPEC:797;
   * PERSISTENCE:SPEC:798
   * 
   * @test_Strategy: Test for ALL in a subquery with the relational operator
   * "<>". Select all customers where total price of orders is <> ALL the values
   * in the result set.
   */
  @SetupMethod(name = "setupOrderData")
  public void test_subquery_ALL_NOTEQ() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> o = customer.join(Customer_.orders);
      Subquery<Double> sq = cquery.subquery(Double.class);
      Root<Order> sqo = sq.from(Order.class);
      sq.select(cbuilder.min(sqo.get(Order_.totalPrice)));
      cquery
          .where(cbuilder.notEqual(o.get(Order_.totalPrice), cbuilder.all(sq)))
          .select(customer).distinct(true);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> result = tquery.getResultList();

      expectedPKs = new String[17];
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
      expectedPKs[11] = "13";
      expectedPKs[12] = "14";
      expectedPKs[13] = "15";
      expectedPKs[14] = "16";
      expectedPKs[15] = "17";
      expectedPKs[16] = "18";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil
            .logErr("test_subquery_ALL_NOTEQ:  Did not get expected results."
                + "  Expected 17 references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("test_subquery_ALL_NOTEQ failed");
    }
  }

  /*
   * @testName: test_subquery_ANY_GT
   * 
   * @assertion_ids: PERSISTENCE:SPEC:794; PERSISTENCE:SPEC:797;
   * PERSISTENCE:SPEC:798
   * 
   * @test_Strategy: Test for ANY in a subquery with the relational operator
   * ">". Select all customers where total price of orders is greater than ANY
   * of the values in the result. The result set contains the total price of
   * orders where count of lineItems = 3.
   */
  @SetupMethod(name = "setupOrderData")
  public void test_subquery_ANY_GT() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> o = customer.join(Customer_.orders);
      Subquery<Double> sq = cquery.subquery(Double.class);
      Root<Order> sqo = sq.from(Order.class);
      Join<Order, LineItem> sql = sqo.join(Order_.lineItemsCollection);
      sq.select(sqo.get(Order_.totalPrice));
      sq.where(cbuilder.equal(sql.get(LineItem_.quantity), 3));
      cquery.where(cbuilder.gt(o.get(Order_.totalPrice), cbuilder.any(sq)))
          .select(customer).distinct(true);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> result = tquery.getResultList();

      expectedPKs = new String[16];
      expectedPKs[0] = "1";
      expectedPKs[1] = "2";
      expectedPKs[2] = "3";
      expectedPKs[3] = "4";
      expectedPKs[4] = "5";
      expectedPKs[5] = "6";
      expectedPKs[6] = "7";
      expectedPKs[7] = "8";
      expectedPKs[8] = "10";
      expectedPKs[9] = "11";
      expectedPKs[10] = "13";
      expectedPKs[11] = "14";
      expectedPKs[12] = "15";
      expectedPKs[13] = "16";
      expectedPKs[14] = "17";
      expectedPKs[15] = "18";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("test_subquery_ANY_GT:  Did not get expected results. "
            + "  Expected 16 references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }
    if (!pass) {
      throw new Fault("test_subquery_ANY_GT failed");
    }
  }

  /*
   * @testName: test_subquery_ANY_LT
   * 
   * @assertion_ids: PERSISTENCE:SPEC:794; PERSISTENCE:SPEC:797;
   * PERSISTENCE:SPEC:798
   * 
   * @test_Strategy: Test for ANY in a subquery with the relational operator
   * "<". Select all customers where total price of orders is less than ANY of
   * the values in the result set. The result set contains the total price of
   * orders where count of lineItems = 3.
   */
  @SetupMethod(name = "setupOrderData")
  public void test_subquery_ANY_LT() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> o = customer.join(Customer_.orders);
      Subquery<Double> sq = cquery.subquery(Double.class);
      Root<Order> sqo = sq.from(Order.class);
      Join<Order, LineItem> sql = sqo.join(Order_.lineItemsCollection);
      sq.select(sqo.get(Order_.totalPrice));
      sq.where(cbuilder.equal(sql.get(LineItem_.quantity), 3));
      cquery.where(cbuilder.lt(o.get(Order_.totalPrice), cbuilder.any(sq)))
          .select(customer).distinct(true);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> result = tquery.getResultList();

      expectedPKs = new String[17];
      expectedPKs[0] = "1";
      expectedPKs[1] = "2";
      expectedPKs[2] = "3";
      expectedPKs[3] = "4";
      expectedPKs[4] = "5";
      expectedPKs[5] = "6";
      expectedPKs[6] = "7";
      expectedPKs[7] = "8";
      expectedPKs[8] = "9";
      expectedPKs[9] = "11";
      expectedPKs[10] = "12";
      expectedPKs[11] = "13";
      expectedPKs[12] = "14";
      expectedPKs[13] = "15";
      expectedPKs[14] = "16";
      expectedPKs[15] = "17";
      expectedPKs[16] = "18";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("test_subquery_ANY_LT:  Did not get expected results.  "
            + "Expected 17 references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("test_subquery_ANY_LT failed");
    }
  }

  /*
   * @testName: test_subquery_ANY_EQ
   * 
   * @assertion_ids: PERSISTENCE:SPEC:794; PERSISTENCE:SPEC:797;
   * PERSISTENCE:SPEC:798
   * 
   * @test_Strategy: Test for ANY in a subquery with the relational operator
   * "=". Select all customers where total price of orders is = ANY the values
   * in the result set. The result set contains the min and avg of total price
   * of orders.
   */
  @SetupMethod(name = "setupOrderData")
  public void test_subquery_ANY_EQ() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> o = customer.join(Customer_.orders);
      Subquery<Double> sq = cquery.subquery(Double.class);
      Root<Order> sqo = sq.from(Order.class);
      sq.select(cbuilder.max(sqo.get(Order_.totalPrice)));
      cquery.where(cbuilder.equal(o.get(Order_.totalPrice), cbuilder.any(sq)))
          .select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> result = tquery.getResultList();

      expectedPKs = new String[2];
      expectedPKs[0] = "10";
      expectedPKs[1] = "14";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("test_subquery_ANY_EQ:  Did not get expected results.  "
            + "Expected 2 references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;

      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("test_subquery_ANY_EQ failed");
    }
  }

  /*
   * @testName: test_subquery_SOME_LTEQ
   * 
   * @assertion_ids: PERSISTENCE:SPEC:794; PERSISTENCE:SPEC:795;
   * PERSISTENCE:SPEC:797; PERSISTENCE:SPEC:798
   * 
   * @test_Strategy: SOME with less than or equal to The result set contains the
   * total price of orders where count of lineItems = 3.
   */
  @SetupMethod(name = "setupOrderData")
  public void test_subquery_SOME_LTEQ() throws Fault {

    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> o = customer.join(Customer_.orders);
      Subquery<Double> sq = cquery.subquery(Double.class);
      Root<Order> sqo = sq.from(Order.class);
      Join<Order, LineItem> sql = sqo.join(Order_.lineItemsCollection);
      sq.select(sqo.get(Order_.totalPrice));
      sq.where(cbuilder.equal(sql.get(LineItem_.quantity), 3));
      cquery.where(cbuilder.le(o.get(Order_.totalPrice), cbuilder.some(sq)))
          .select(customer).distinct(true);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> result = tquery.getResultList();

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

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil
            .logErr("test_subquery_SOME_LTEQ:  Did not get expected results. "
                + " Expected 18 references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault(" test_subquery_SOME_LTEQ failed");
    }
  }

  /*
   * @testName: test_subquery_SOME_GTEQ
   * 
   * @assertion_ids: PERSISTENCE:SPEC:794; PERSISTENCE:SPEC:795;
   * PERSISTENCE:SPEC:797; PERSISTENCE:SPEC:798
   * 
   * @test_Strategy: Test for SOME in a subquery with the relational operator
   * ">=". Select all customers where total price of orders is >= SOME the
   * values in the result set. The result set contains the total price of orders
   * where count of lineItems = 3.
   */
  @SetupMethod(name = "setupOrderData")
  public void test_subquery_SOME_GTEQ() throws Fault {

    boolean pass = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> o = customer.join(Customer_.orders);
      Subquery<Double> sq = cquery.subquery(Double.class);
      Root<Order> sqo = sq.from(Order.class);
      Join<Order, LineItem> sql = sqo.join(Order_.lineItemsCollection);
      sq.select(sqo.get(Order_.totalPrice));
      sq.where(cbuilder.equal(sql.get(LineItem_.quantity), 3));
      cquery.where(cbuilder.ge(o.get(Order_.totalPrice), cbuilder.some(sq)))
          .select(customer).distinct(true);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      tquery.setMaxResults(customerRef.length);
      List<Customer> result = tquery.getResultList();

      expectedPKs = new String[17];
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
      expectedPKs[11] = "13";
      expectedPKs[12] = "14";
      expectedPKs[13] = "15";
      expectedPKs[14] = "16";
      expectedPKs[15] = "17";
      expectedPKs[16] = "18";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil
            .logErr("test_subquery_SOME_GTEQ:  Did not get expected results. "
                + " Expected 17 references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);

    }

    if (!pass) {
      throw new Fault("test_subquery_SOME_GTEQ failed");
    }
  }

}
