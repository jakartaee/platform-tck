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

package com.sun.ts.tests.jpa.core.criteriaapi.Join;

import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.schema30.*;
import com.sun.ts.tests.jpa.common.schema30.Order;

import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.PluralAttribute;
import java.util.*;

public class Client extends Util {

  /* Test setup */
  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("Entering Setup");
    try {
      super.setup(args, p);
      getEntityManager();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      throw new Fault("Setup failed:", e);
    }
  }

  /*
   * @testName: joinStringTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1052; PERSISTENCE:JAVADOC:1040;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * select c FROM Customer c JOIN c.orders o JOIN o.lineItems l where (l.id =
   * 1)
   */
  @SetupMethod(name = "setupOrderData")
  public void joinStringTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = true;
    boolean pass4 = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);

      Set sJoins = customer.getJoins();
      if (!sJoins.isEmpty()) {
        TestUtil.logErr("Expected getJoins() to return empty set instead got:");
        for (Iterator<Join<Order, ?>> i = sJoins.iterator(); i.hasNext();) {
          Join j = i.next();
          TestUtil.logErr("join:" + j.toString());
        }
      } else {
        TestUtil.logTrace("getJoin() returned empty set as expected");
        pass1 = true;
      }

      Join<Customer, Order> order = customer.join(Customer_.orders);

      Set<Join<Customer, ?>> s = customer.getJoins();
      if (s.isEmpty()) {
        TestUtil.logErr("Expected getJoins() to return non empty set");
      } else {

        if (s.size() == 1) {
          TestUtil.logTrace("getJoins returned:");
          for (Iterator<Join<Customer, ?>> i = s.iterator(); i.hasNext();) {
            pass2 = true;
            Join j = i.next();
            TestUtil.logTrace("join:" + j.toString());
            String name = j.getAttribute().getName();
            if (name.equals("orders")) {
              TestUtil.logTrace("Received expected attribute: orders");
            } else {
              TestUtil.logErr("Expected attribute: orders, actual:" + name);
              pass3 = false;
            }
          }
        } else {
          pass3 = false;
          TestUtil
              .logErr("Expected getJoins to return 1 join, actual:" + s.size());
          TestUtil.logErr("getJoins returned:");
          for (Iterator<Join<Customer, ?>> i = s.iterator(); i.hasNext();) {
            Join j = i.next();
            TestUtil.logErr("join:" + j.toString());
          }
        }

      }
      Join<Order, LineItem> lineItem = order.join("lineItemsCollection");

      cquery.where(cbuilder.equal(lineItem.get("id"), "1")).select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "1";

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
      TestUtil.logErr("Caught unexpected exception", e);

    }

    if (!pass1 || !pass2 || !pass3 || !pass4) {
      throw new Fault("joinStringTest failed");
    }
  }

  /*
   * @testName: joinStringJoinTypeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1054;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * select c FROM Customer c JOIN c.orders o INNER JOIN o.lineItems l where
   * (l.id = 1)
   *
   */
  @SetupMethod(name = "setupOrderData")
  public void joinStringJoinTypeTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> order = customer.join(Customer_.orders);
      Join<Order, LineItem> lineItem = order.join("lineItemsCollection",
          JoinType.INNER);
      cquery.where(cbuilder.equal(lineItem.get("id"), "1")).select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "1";

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
      throw new Fault("joinStringJoinTypeTest failed");
    }
  }

  /*
   * @testName: joinStringIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1053; PERSISTENCE:JAVADOC:1055;
   * 
   * @test_Strategy:
   *
   */
  public void joinStringIllegalArgumentExceptionTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    TestUtil.logMsg("String Test");
    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> order = customer.join(Customer_.orders);
      try {
        order.join("doesnotexist");
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected exception");
        pass1 = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception", e);
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);

    }

    TestUtil.logMsg("String, JoinType Test");

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> order = customer.join(Customer_.orders);
      try {
        order.join("doesnotexist", JoinType.INNER);
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected exception");
        pass2 = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception", e);
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);

    }

    if (!pass1 || !pass2) {
      throw new Fault("joinStringIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: joinSingularAttributeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1042;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * SELECT c FROM Customer c JOIN c.work o WHERE (o.id in (4))
   */
  @SetupMethod(name = "setupOrderData")
  public void joinSingularAttributeTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> order = customer.join(Customer_.orders);
      Join<Order, CreditCard> creditCard = order.join("creditCard");
      Expression e = cbuilder.literal("4");
      cquery.where(creditCard.get("id").in(e)).select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
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
      TestUtil.logErr("Caught unexpected exception", e);

    }

    if (!pass) {
      throw new Fault("joinSingularAttributeTest failed");
    }
  }

  /*
   * @testName: joinSingularAttributeJoinTypeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1043; PERSISTENCE:SPEC:1698;
   * PERSISTENCE:SPEC:1786; PERSISTENCE:SPEC:1786.1;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * SELECT c FROM Customer c INNER JOIN c.work o WHERE (o.id in (4))
   */
  @SetupMethod(name = "setupOrderData")
  public void joinSingularAttributeJoinTypeTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> order = customer.join(Customer_.orders);
      Join<Order, CreditCard> creditCard = order.join("creditCard",
          JoinType.INNER);
      Expression e = cbuilder.literal("4");
      cquery.where(creditCard.get("id").in(e)).select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
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
      TestUtil.logErr("Caught unexpected exception", e);

    }

    if (!pass) {
      throw new Fault("joinSingularAttributeJoinTypeTest failed");
    }
  }

  /*
   * @testName: joinCollectionAttributeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1044; PERSISTENCE:JAVADOC:729
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * select c FROM Customer c JOIN c.orders o JOIN o.lineItems l where (l.id =
   * 1)
   *
   */
  @SetupMethod(name = "setupOrderData")
  public void joinCollectionAttributeTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> order = customer.join(Customer_.orders);
      CollectionJoin<Order, LineItem> lineItem = order
          .join(Order_.lineItemsCollection);
      PluralAttribute pa = lineItem.getModel();
      String name = pa.getName();
      if (name.equals("lineItemsCollection")) {
        TestUtil.logTrace("Received expected attribute:" + name);
        pass1 = true;
      } else {
        TestUtil
            .logErr("getModel - Expected: lineItemsCollection, actual:" + name);
      }

      cquery.where(cbuilder.equal(lineItem.get("id"), "1")).select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "1";
      if (!checkEntityPK(clist, expectedPKs)) {
        TestUtil
            .logErr("Did not get expected results.  Expected 1 reference, got: "
                + clist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass2 = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);

    }

    if (!pass1 || !pass2) {
      throw new Fault("joinCollectionAttributeTest failed");
    }
  }

  /*
   * @testName: joinCollectionAttributeJoinTypeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1048;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * select c FROM Customer c JOIN c.orders o INNER JOIN o.lineItems l where
   * (l.id = 1)
   *
   */
  @SetupMethod(name = "setupOrderData")
  public void joinCollectionAttributeJoinTypeTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> order = customer.join(Customer_.orders);
      CollectionJoin<Order, LineItem> lineItem = order
          .join(Order_.lineItemsCollection, JoinType.INNER);
      cquery.where(cbuilder.equal(lineItem.get("id"), "1")).select(customer);

      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "1";
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
      throw new Fault("joinCollectionAttributeJoinTypeTest failed");
    }
  }

  /*
   * @testName: joinCollectionStringTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1056; PERSISTENCE:JAVADOC:728;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * select c FROM Customer c JOIN c.orders o JOIN o.lineItems l where (l.id =
   * 1)
   *
   */
  @SetupMethod(name = "setupOrderData")
  public void joinCollectionStringTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> order = customer.join(Customer_.orders);
      CollectionJoin<Order, LineItem> lineItem = order
          .joinCollection("lineItemsCollection");
      String name = lineItem.getModel().getName();
      if (name.equals("lineItemsCollection")) {
        TestUtil.logTrace("Received expected CollectionAttribute:" + name);
      } else {
        TestUtil
            .logErr("Expected CollectionAttribute:lineItems, actual:" + name);
      }
      cquery.where(cbuilder.equal(lineItem.get("id"), "1")).select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "1";
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
      throw new Fault("joinCollectionStringTest failed");
    }
  }

  /*
   * @testName: joinCollectionStringJoinTypeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1058;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * select c FROM Customer c JOIN c.orders o INNER JOIN o.lineItems l where
   * (l.id = 1)
   */
  @SetupMethod(name = "setupOrderData")
  public void joinCollectionStringJoinTypeTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> order = customer.join(Customer_.orders);
      CollectionJoin<Order, LineItem> lineItem = order
          .joinCollection("lineItemsCollection", JoinType.INNER);
      cquery.where(cbuilder.equal(lineItem.get("id"), "1")).select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "1";
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
      throw new Fault("joinCollectionStringJoinTypeTest failed");
    }
  }

  /*
   * @testName: joinCollectionIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1057; PERSISTENCE:JAVADOC:1059;
   * 
   * @test_Strategy:
   *
   */
  public void joinCollectionIllegalArgumentExceptionTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;

    TestUtil.logMsg("String Test");

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> order = customer.join(Customer_.orders);
      try {
        order.joinCollection("doesnotexist");
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected exception");
        pass1 = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception", e);
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);

    }

    TestUtil.logMsg("String, JoinType Test");
    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> order = customer.join(Customer_.orders);
      try {
        order.joinCollection("doesnotexist", JoinType.INNER);
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected exception");
        pass2 = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception", e);

      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);

    }

    if (!pass1 || !pass2) {
      throw new Fault("joinCollectionIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: joinSetAttributeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1045
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * select c FROM Customer c JOIN c.orders2 o JOIN o.lineItemsSet l where (l.id
   * = 1)
   *
   */
  @SetupMethod(name = "setupOrderData")
  public void joinSetAttributeTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> order = customer.join(Customer_.orders2);
      SetJoin<Order, LineItem> lineItem = order.join(Order_.lineItemsSet);
      cquery.where(cbuilder.equal(lineItem.get("id"), "1")).select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "1";
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
      throw new Fault("joinSetAttributeTest failed");
    }
  }

  /*
   * @testName: joinSetAttributeJoinTypeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1049
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * select c FROM Customer c JOIN c.orders2 o INNER JOIN o.lineItemsSet l where
   * (l.id = 1)
   */
  @SetupMethod(name = "setupOrderData")
  public void joinSetAttributeJoinTypeTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> order = customer.join(Customer_.orders2);
      SetJoin<Order, LineItem> lineItem = order.join(Order_.lineItemsSet,
          JoinType.INNER);
      cquery.where(cbuilder.equal(lineItem.get("id"), "1")).select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "1";
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
      throw new Fault("joinSetAttributeJoinTypeTest failed");
    }
  }

  /*
   * @testName: joinSetStringTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1068;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * select c FROM Customer c JOIN c.orders2 o JOIN o.lineItemsSet l where (l.id
   * = 1)
   */
  @SetupMethod(name = "setupOrderData")
  public void joinSetStringTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> order = customer.join(Customer_.orders2);
      SetJoin<Order, LineItem> lineItem = order.joinSet("lineItemsSet");
      cquery.where(cbuilder.equal(lineItem.get("id"), "1")).select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "1";
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
      throw new Fault("joinSetStringTest failed");
    }
  }

  /*
   * @testName: joinSetStringJoinTypeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1070;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * select c FROM Customer c JOIN c.orders2 o INNER JOIN o.lineItemsSet l where
   * (l.id = 1)
   */
  @SetupMethod(name = "setupOrderData")
  public void joinSetStringJoinTypeTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> order = customer.join(Customer_.orders2);
      SetJoin<Order, LineItem> lineItem = order.joinSet("lineItemsSet",
          JoinType.INNER);
      cquery.where(cbuilder.equal(lineItem.get("id"), "1")).select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "1";
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
      throw new Fault("joinSetStringJoinTypeTest failed");
    }
  }

  /*
   * @testName: joinSetIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1069; PERSISTENCE:JAVADOC:1027;
   * PERSISTENCE:JAVADOC:1071;
   * 
   * @test_Strategy:
   *
   */
  public void joinSetIllegalArgumentExceptionTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    TestUtil.logMsg("String Test");

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> order = customer.join(Customer_.orders2);
      try {
        order.joinSet("doesnotexist");
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected exception");
        pass1 = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception", e);
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);

    }

    TestUtil.logMsg("String, JoinType Test");
    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> order = customer.join(Customer_.orders2);
      try {
        order.joinSet("doesnotexist", JoinType.INNER);
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected exception");
        pass2 = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception", e);
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);

    }

    if (!pass1 || !pass2) {
      throw new Fault("joinSetIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: joinListAttributeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1046; PERSISTENCE:JAVADOC:1076;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * select c FROM Customer c JOIN c.orders3 o JOIN o.lineItemsList l where
   * (l.id = 1)
   */
  @SetupMethod(name = "setupOrderData")
  public void joinListAttributeTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> order = customer.join(Customer_.orders2);
      ListJoin<Order, LineItem> lineItem = order.joinList("lineItemsList");
      PluralAttribute pa = lineItem.getModel();
      String name = pa.getName();
      if (name.equals("lineItemsList")) {
        TestUtil.logTrace("Received expected attribute:" + name);
      } else {
        TestUtil.logErr("getModel - Expected: lineItemsList, actual:" + name);
      }
      cquery.where(cbuilder.equal(lineItem.get("id"), "1")).select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "1";
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
      throw new Fault("joinListAttributeTest failed");
    }
  }

  /*
   * @testName: joinListAttributeJoinTypeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1050;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * select c FROM Customer c JOIN c.orders3 o INNER JOIN o.lineItemsList l
   * where (l.id = 1)
   */
  @SetupMethod(name = "setupOrderData")
  public void joinListAttributeJoinTypeTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> order = customer.join(Customer_.orders2);
      ListJoin<Order, LineItem> lineItem = order.joinList("lineItemsList",
          JoinType.INNER);
      cquery.where(cbuilder.equal(lineItem.get("id"), "1")).select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "1";
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
      throw new Fault("joinListAttributeJoinTypeTest failed");
    }
  }

  /*
   * @testName: joinListIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1061; PERSISTENCE:JAVADOC:1063;
   * 
   * @test_Strategy:
   */
  public void joinListIllegalArgumentExceptionTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    TestUtil.logMsg("Testing String");

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> order = customer.join("orders3");
      try {
        order.joinList("doesnotexist");
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
        pass1 = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception", e);
      }
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);

    }

    TestUtil.logMsg("Testing String, JoinType");
    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> order = customer.join("orders3");
      try {
        order.joinList("doesnotexist", JoinType.INNER);
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
        pass2 = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception", e);
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);

    }

    if (!pass1 || !pass2) {
      throw new Fault("joinListIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: joinListStringTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1060; PERSISTENCE:JAVADOC:1074;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * select c FROM Customer c JOIN c.orders3 o JOIN o.lineItemsList l where
   * (l.id = 1)
   */
  @SetupMethod(name = "setupOrderData")
  public void joinListStringTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> order = customer.joinList("orders3");
      ListJoin<Order, LineItem> lineItem = order.joinList("lineItemsList");
      String name = lineItem.getModel().getName();
      if (name.equals("lineItemsList")) {
        TestUtil.logTrace("Received expected ListAttribute:" + name);
        pass1 = true;
      } else {
        TestUtil.logErr("Expected ListAttribute:lineItemsList, actual:" + name);
      }
      cquery.where(cbuilder.equal(lineItem.get("id"), "1")).select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "1";
      if (!checkEntityPK(clist, expectedPKs)) {
        TestUtil
            .logErr("Did not get expected results.  Expected 1 reference, got: "
                + clist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass2 = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);

    }

    if (!pass1 || !pass2) {
      throw new Fault("joinListStringTest failed");
    }
  }

  /*
   * @testName: joinListStringJoinTypeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1062;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * select c FROM Customer c JOIN c.orders3 o INNER JOIN o.lineItemsList l
   * where (l.id = 1)
   */
  @SetupMethod(name = "setupOrderData")
  public void joinListStringJoinTypeTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> order = customer.joinList("orders3");
      ListJoin<Order, LineItem> lineItem = order.joinList("lineItemsList",
          JoinType.INNER);
      cquery.where(cbuilder.equal(lineItem.get("id"), "1")).select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "1";
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
      throw new Fault("joinListStringJoinTypeTest failed");
    }
  }

  /*
   * @testName: joinMapAttributeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1047; PERSISTENCE:JAVADOC:1081;
   * PERSISTENCE:JAVADOC:1078;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * SELECT e FROM Employee e JOIN e.department d JOIN d.lastNameEmployees e1
   * WHERE (e1.id = 1)
   *
   */
  @SetupMethod(name = "setupDepartmentEmployeeData")
  public void joinMapAttributeTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Employee> cquery = cbuilder.createQuery(Employee.class);
      Root<Employee> employee = cquery.from(Employee.class);
      Join<Employee, Department> department = employee
          .join(Employee_.department);
      MapJoin<Department, String, Employee> mEmployee = department
          .join(Department_.lastNameEmployees);
      PluralAttribute pa = mEmployee.getModel();
      String name = pa.getName();
      if (name.equals("lastNameEmployees")) {
        TestUtil.logTrace("Received expected attribute:" + name);
      } else {
        TestUtil
            .logErr("getModel - Expected: lastNameEmployees, actual:" + name);
      }
      cquery.where(cbuilder.equal(mEmployee.get("id"), "1")).select(employee);
      TypedQuery<Employee> tquery = getEntityManager().createQuery(cquery);
      List<Employee> clist = tquery.getResultList();

      expectedPKs = new String[3];
      expectedPKs[0] = "1";
      expectedPKs[1] = "3";
      expectedPKs[2] = "5";
      if (!checkEntityPK(clist, expectedPKs)) {
        TestUtil
            .logErr("Did not get expected results.  Expected 3 reference, got: "
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
      throw new Fault("joinMapAttributeTest failed");
    }
  }

  /*
   * @testName: joinMapAttributeJoinTypeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1051;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * SELECT e FROM Employee e JOIN e.department d INNER JOIN d.lastNameEmployees
   * e1 WHERE (e1.id = 1)
   *
   */
  @SetupMethod(name = "setupDepartmentEmployeeData")
  public void joinMapAttributeJoinTypeTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Employee> cquery = cbuilder.createQuery(Employee.class);
      Root<Employee> employee = cquery.from(Employee.class);
      Join<Employee, Department> department = employee
          .join(Employee_.department);
      MapJoin<Department, String, Employee> mEmployee = department
          .join(Department_.lastNameEmployees, JoinType.INNER);
      cquery.where(cbuilder.equal(mEmployee.get("id"), "1")).select(employee);
      TypedQuery<Employee> tquery = getEntityManager().createQuery(cquery);
      List<Employee> clist = tquery.getResultList();

      expectedPKs = new String[3];
      expectedPKs[0] = "1";
      expectedPKs[1] = "3";
      expectedPKs[2] = "5";
      if (!checkEntityPK(clist, expectedPKs)) {
        TestUtil
            .logErr("Did not get expected results.  Expected 3 reference, got: "
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
      throw new Fault("joinMapAttributeJoinTypeTest failed");
    }
  }

  /*
   * @testName: joinMapStringTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1064; PERSISTENCE:JAVADOC:1112;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * SELECT e FROM Employee e JOIN e.department d JOIN d.lastNameEmployees e1
   * WHERE (e1.id = 1)
   *
   */
  @SetupMethod(name = "setupDepartmentEmployeeData")
  public void joinMapStringTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Employee> cquery = cbuilder.createQuery(Employee.class);
      Root<Employee> employee = cquery.from(Employee.class);
      Join<Employee, Department> department = employee
          .join(Employee_.department);
      MapJoin<Department, String, Employee> mEmployee = department
          .joinMap("lastNameEmployees");
      String name = mEmployee.getModel().getName();
      if (name.equals("lastNameEmployees")) {
        TestUtil.logTrace("Received expected attribute:" + name);
      } else {
        TestUtil
            .logErr("getModel - Expected: lastNameEmployees, actual:" + name);
      }
      cquery.where(cbuilder.equal(mEmployee.get("id"), "1")).select(employee);
      TypedQuery<Employee> tquery = getEntityManager().createQuery(cquery);
      List<Employee> clist = tquery.getResultList();

      expectedPKs = new String[3];
      expectedPKs[0] = "1";
      expectedPKs[1] = "3";
      expectedPKs[2] = "5";
      if (!checkEntityPK(clist, expectedPKs)) {
        TestUtil
            .logErr("Did not get expected results.  Expected 3 reference, got: "
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
      throw new Fault("joinMapStringTest failed");
    }
  }

  /*
   * @testName: joinMapStringJoinTypeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1066;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * SELECT e FROM Employee e JOIN e.department d INNER JOIN d.lastNameEmployees
   * e1 WHERE (e1.id = 1)
   *
   */
  @SetupMethod(name = "setupDepartmentEmployeeData")
  public void joinMapStringJoinTypeTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Employee> cquery = cbuilder.createQuery(Employee.class);
      Root<Employee> employee = cquery.from(Employee.class);
      Join<Employee, Department> department = employee
          .join(Employee_.department);
      MapJoin<Department, String, Employee> mEmployee = department
          .joinMap("lastNameEmployees", JoinType.INNER);
      cquery.where(cbuilder.equal(mEmployee.get("id"), "1")).select(employee);
      TypedQuery<Employee> tquery = getEntityManager().createQuery(cquery);
      List<Employee> clist = tquery.getResultList();

      expectedPKs = new String[3];
      expectedPKs[0] = "1";
      expectedPKs[1] = "3";
      expectedPKs[2] = "5";
      if (!checkEntityPK(clist, expectedPKs)) {
        TestUtil
            .logErr("Did not get expected results.  Expected 3 reference, got: "
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
      throw new Fault("joinMapStringJoinTypeTest failed");
    }
  }

  /*
   * @testName: joinMapIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1065; PERSISTENCE:JAVADOC:1067
   * 
   * @test_Strategy:
   */
  public void joinMapIllegalArgumentExceptionTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    TestUtil.logMsg("Testing String");

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Employee> cquery = cbuilder.createQuery(Employee.class);
      Root<Employee> employee = cquery.from(Employee.class);
      Join<Employee, Department> department = employee
          .join(Employee_.department);

      try {
        department.joinMap("doesnotexist");
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
        pass1 = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception", e);
      }
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);

    }

    TestUtil.logMsg("Testing String, JoinType");
    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Employee> cquery = cbuilder.createQuery(Employee.class);
      Root<Employee> employee = cquery.from(Employee.class);
      Join<Employee, Department> department = employee
          .join(Employee_.department);
      try {
        department.joinMap("doesnotexist", JoinType.INNER);
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
        pass2 = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception", e);
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);

    }

    if (!pass1 || !pass2) {
      throw new Fault("joinMapIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: pluralJoinTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1113; PERSISTENCE:JAVADOC:1114;
   * PERSISTENCE:JAVADOC:1115; PERSISTENCE:JAVADOC:1112;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * select c FROM Customer c JOIN c.orders o JOIN o.lineItems l where (l.id =
   * 1)
   *
   */
  @SetupMethod(name = "setupOrderData")
  public void pluralJoinTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    boolean pass4 = false;
    boolean pass5 = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> order = customer.join(Customer_.orders);
      PluralJoin lineItem = order.joinCollection("lineItemsCollection");
      String name = lineItem.getModel().getName();
      if (name.equals("lineItemsCollection")) {
        TestUtil.logTrace("Received expected PluralAttribute:" + name);
        pass1 = true;
      } else {
        TestUtil.logErr("Expected PluralAttribute:lineItems, actual:" + name);
      }
      name = lineItem.getAttribute().getName();
      if (name.equals("lineItemsCollection")) {
        TestUtil.logTrace("Received expected attribute:" + name);
        pass2 = true;
      } else {
        TestUtil.logErr("Expected attribute name: lineItems, actual:" + name);
      }
      JoinType type = lineItem.getJoinType();
      if (type.equals(JoinType.INNER)) {
        TestUtil.logTrace("Received expected JoinType:" + type);
        pass3 = true;
      } else {
        TestUtil.logErr("Expected JoinType : " + JoinType.INNER.toString()
            + ", actual:" + type);
      }
      From from = lineItem.getParent();
      String parent = from.getJavaType().getName();
      if (parent.equals("com.sun.ts.tests.jpa.common.schema30.Order")) {
        TestUtil.logTrace("Received expected parent:" + parent);
        pass4 = true;
      } else {
        TestUtil.logErr(
            "Expected parent: com.sun.ts.tests.jpa.common.schema30.Order, actual:"
                + parent);
      }
      cquery.where(cbuilder.equal(lineItem.get("id"), "1")).select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "1";
      if (!checkEntityPK(clist, expectedPKs)) {
        TestUtil
            .logErr("Did not get expected results.  Expected 1 reference, got: "
                + clist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass5 = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);

    }

    if (!pass1 || !pass2 || !pass3 || !pass4 || !pass5) {
      throw new Fault("pluralJoinTest failed");
    }
  }

  /*
   * @testName: pluralJoinOnExpressionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1723; PERSISTENCE:JAVADOC:1722;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   */
  @SetupMethod(name = "setupOrderData")
  public void pluralJoinOnExpressionTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Long> cquery = cbuilder.createQuery(Long.class);
      Root<Customer> customer = cquery.from(Customer.class);
      PluralJoin order = customer.join(Customer_.orders);
      if (order.getOn() == null) {
        TestUtil.logTrace("Received expected null from getOn()");
        pass1 = true;
      } else {
        TestUtil
            .logErr("Expected null from getOn(), actual:" + order.toString());
      }

      Join join = order.on(cbuilder.equal(order.get("id"), "1"));
      Predicate pred = join.getOn();
      if (pred != null) {
        TestUtil.logTrace("Received expected non-null from getOn()");
        pass2 = true;
      } else {
        TestUtil.logErr("Received unexpected null from getOn()");
      }
      cquery.select(cbuilder.count(join));
      TypedQuery<Long> tquery = getEntityManager().createQuery(cquery);
      Long actual = tquery.getSingleResult();
      if (actual == 1) {
        TestUtil.logTrace("Received expected number: " + actual);
        pass3 = true;
      } else {
        TestUtil.logErr("Expected: 1, actual:" + actual);
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);

    }

    if (!pass1 || !pass2 || !pass3) {
      throw new Fault("pluralJoinOnExpressionTest failed");
    }
  }

  /*
   * @testName: pluralJoinOnPredicateArrayTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1724; PERSISTENCE:JAVADOC:1722;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   */
  @SetupMethod(name = "setupOrderData")
  public void pluralJoinOnPredicateArrayTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Long> cquery = cbuilder.createQuery(Long.class);
      Root<Customer> customer = cquery.from(Customer.class);
      PluralJoin order = customer.join(Customer_.orders);

      Predicate pred1 = cbuilder.equal(customer.get("id"), "1");
      Predicate pred2 = cbuilder.equal(customer.get("country").get("code"),
          "USA");
      Predicate[] pred = { pred1, pred2 };
      Join join = order.on(pred);

      Predicate pred3 = join.getOn();
      if (pred3.getExpressions().size() == 2) {
        TestUtil.logTrace("Received expected number of predicates");
        pass1 = true;
      } else {
        TestUtil.logErr(
            "Expected: 2 predicates, actual:" + pred3.getExpressions().size());

      }

      cquery.select(cbuilder.count(join));
      TypedQuery<Long> tquery = getEntityManager().createQuery(cquery);
      Long actual = tquery.getSingleResult();
      if (actual == 1) {
        TestUtil.logTrace("Received expected number: " + actual);
        pass2 = true;
      } else {
        TestUtil.logErr("Expected: 1, actual:" + actual);
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);

    }

    if (!pass1 || !pass2) {
      throw new Fault("pluralJoinOnPredicateArrayTest failed");
    }
  }

  /*
   * @testName: collectionJoinOnExpressionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1668;
   * 
   * @test_Strategy: SELECT o FROM LineItem l INNER JOIN Order o ON (l.id =
   * o.ID) where (l.QUANTITY > 5))
   */
  @SetupMethod(name = "setupOrderData")
  public void collectionJoinOnExpressionTest() throws Fault {
    boolean pass = false;
    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    getEntityTransaction().begin();
    try {
      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      CollectionJoin<Order, LineItem> lineItem = order
          .joinCollection("lineItemsCollection", JoinType.INNER);
      Expression exp = cbuilder.gt(lineItem.<Number> get("quantity"), 5);
      lineItem.on(exp);
      cquery.select(order);

      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      List<Order> oList = tquery.getResultList();

      String expectedPKs[] = new String[2];
      expectedPKs[0] = "10";
      expectedPKs[1] = "12";

      if (!checkEntityPK(oList, expectedPKs)) {
        TestUtil.logErr("Did not get expected results.  Expected: "
            + expectedPKs.length + "  reference, got: " + oList.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("collectionJoinOnExpressionTest failed");
  }

  /*
   * @testName: collectionJoinOnPredicateArrayTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1669
   * 
   * @test_Strategy: SELECT o FROM LineItem l INNER JOIN Order o ON (l.id =
   * o.ID) where ((l.QUANTITY > 5) AND (l.QUANTITY < 8))
   */
  @SetupMethod(name = "setupOrderData")
  public void collectionJoinOnPredicateArrayTest() throws Fault {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    getEntityTransaction().begin();
    try {
      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      CollectionJoin<Order, LineItem> lineItem = order
          .joinCollection("lineItemsCollection", JoinType.INNER);
      Predicate[] pred = { cbuilder.gt(lineItem.<Number> get("quantity"), 5),
          cbuilder.lt(lineItem.<Number> get("quantity"), 8) };
      lineItem.on(pred);
      cquery.select(order);

      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      List<Order> oList = tquery.getResultList();

      String expectedPKs[] = new String[1];
      expectedPKs[0] = "12";

      if (!checkEntityPK(oList, expectedPKs)) {
        TestUtil.logErr("Did not get expected results.  Expected: "
            + expectedPKs.length + "  reference, got: " + oList.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("collectionJoinOnPredicateArrayTest failed");
  }

  /*
   * @testName: listJoinOnExpressionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1718
   * 
   * @test_Strategy: SELECT o FROM LineItem l INNER JOIN Order o ON (l.id =
   * o.ID) where (l.QUANTITY > 5)
   */
  @SetupMethod(name = "setupOrderData")
  public void listJoinOnExpressionTest() throws Fault {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    getEntityTransaction().begin();
    try {
      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      ListJoin<Order, LineItem> lineItem = order.joinList("lineItemsList",
          JoinType.INNER);
      Expression exp = cbuilder.gt(lineItem.<Number> get("quantity"), 5);
      lineItem.on(exp);
      cquery.select(order);

      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      List<Order> oList = tquery.getResultList();

      String expectedPKs[] = new String[2];
      expectedPKs[0] = "10";
      expectedPKs[1] = "12";

      if (!checkEntityPK(oList, expectedPKs)) {
        TestUtil.logErr("Did not get expected results.  Expected: "
            + expectedPKs.length + "  reference, got: " + oList.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("listJoinOnExpressionTest failed");
  }

  /*
   * @testName: listJoinOnPredicateArrayTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1719
   * 
   * @test_Strategy: SELECT o FROM LineItem l INNER JOIN Order o ON (l.id =
   * o.ID) where ((l.QUANTITY > 5) AND (l.QUANTITY < 8))
   */
  @SetupMethod(name = "setupOrderData")
  public void listJoinOnPredicateArrayTest() throws Fault {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    getEntityTransaction().begin();
    try {
      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      ListJoin<Order, LineItem> lineItem = order.joinList("lineItemsList",
          JoinType.INNER);
      Predicate[] pred = { cbuilder.gt(lineItem.<Number> get("quantity"), 5),
          cbuilder.lt(lineItem.<Number> get("quantity"), 8) };
      lineItem.on(pred);
      cquery.select(order);

      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      List<Order> oList = tquery.getResultList();

      String expectedPKs[] = new String[1];
      expectedPKs[0] = "12";

      if (!checkEntityPK(oList, expectedPKs)) {
        TestUtil.logErr("Did not get expected results.  Expected: "
            + expectedPKs.length + "  reference, got: " + oList.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("listJoinOnPredicateArrayTest failed");
  }

  /*
   * @testName: setJoinOnExpressionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1725
   * 
   * @test_Strategy: SELECT o FROM LineItem l INNER JOIN Order o ON (l.id =
   * o.ID) where (l.QUANTITY > 5)
   */
  @SetupMethod(name = "setupOrderData")
  public void setJoinOnExpressionTest() throws Fault {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    getEntityTransaction().begin();
    try {
      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      SetJoin<Order, LineItem> lineItem = order.joinSet("lineItemsSet",
          JoinType.INNER);
      Expression exp = cbuilder.gt(lineItem.<Number> get("quantity"), 5);
      lineItem.on(exp);
      cquery.select(order);

      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      List<Order> oList = tquery.getResultList();

      String expectedPKs[] = new String[2];
      expectedPKs[0] = "10";
      expectedPKs[1] = "12";

      if (!checkEntityPK(oList, expectedPKs)) {
        TestUtil.logErr("Did not get expected results.  Expected: "
            + expectedPKs.length + "  reference, got: " + oList.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("setJoinOnExpressionTest failed");
  }

  /*
   * @testName: setJoinOnPredicateArrayTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1726
   * 
   * @test_Strategy: SELECT o FROM LineItem l INNER JOIN Order o ON (l.id =
   * o.ID) where ((l.QUANTITY > 5) AND (l.QUANTITY < 8))
   */
  @SetupMethod(name = "setupOrderData")
  public void setJoinOnPredicateArrayTest() throws Fault {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    getEntityTransaction().begin();
    try {
      CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
      Root<Order> order = cquery.from(Order.class);
      SetJoin<Order, LineItem> lineItem = order.joinSet("lineItemsSet",
          JoinType.INNER);
      Predicate[] pred = { cbuilder.gt(lineItem.<Number> get("quantity"), 5),
          cbuilder.lt(lineItem.<Number> get("quantity"), 8) };
      lineItem.on(pred);
      cquery.select(order);

      TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
      List<Order> oList = tquery.getResultList();

      String expectedPKs[] = new String[1];
      expectedPKs[0] = "12";

      if (!checkEntityPK(oList, expectedPKs)) {
        TestUtil.logErr("Did not get expected results.  Expected: "
            + expectedPKs.length + "  reference, got: " + oList.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("setJoinOnPredicateArrayTest failed");
  }

  /*
   * @testName: mapJoinValueTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1080;
   * 
   * @test_Strategy:
   *
   * SELECT e.id, e.firstname, d.LASTNAME FROM Employee e Join
   * e.lastNameEmployees d
   *
   */
  @SetupMethod(name = "setupDepartmentEmployeeData")
  public void mapJoinValueTest() throws Fault {
    boolean pass = false;
    List<String> expected = new ArrayList<String>();

    expected.add("1, Alan, Frechette");
    expected.add("1, Alan, McGowan");
    expected.add("1, Alan, DMilla");
    expected.add("3, Shelly, Frechette");
    expected.add("3, Shelly, McGowan");
    expected.add("3, Shelly, DMilla");
    expected.add("5, Stephen, Frechette");
    expected.add("5, Stephen, McGowan");
    expected.add("5, Stephen, DMilla");
    expected.add("2, Arthur, Frechette");
    expected.add("2, Arthur, Bissett");
    expected.add("4, Robert, Frechette");
    expected.add("4, Robert, Bissett");

    List<String> actual = new ArrayList<String>();

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
      Root<Employee> employee = cquery.from(Employee.class);
      Join<Employee, Department> department = employee
          .join(Employee_.department);
      MapJoin<Department, String, Employee> mEmployee = department
          .join(Department_.lastNameEmployees);
      cquery.multiselect(employee.get(Employee_.id),
          employee.get(Employee_.firstName),
          mEmployee.value().<String> get("lastName"));
      TypedQuery<Tuple> tquery = getEntityManager().createQuery(cquery);
      List<Tuple> clist = tquery.getResultList();

      for (Tuple t : clist) {
        TestUtil
            .logTrace("result:" + t.get(0) + ", " + t.get(1) + ", " + t.get(2));
        actual.add(t.get(0) + ", " + t.get(1) + ", " + t.get(2));
      }
      if (expected.containsAll(actual) && actual.containsAll(expected)
          && expected.size() == actual.size()) {

        TestUtil.logTrace("Received expected results");
        pass = true;
      } else {
        TestUtil.logErr("Did not get expected results:");
        for (String s : expected) {
          TestUtil.logErr("expected:" + s);
        }
        for (String s : actual) {
          TestUtil.logErr("actual:" + s);
        }
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);

    }

    if (!pass) {
      throw new Fault("mapJoinValueTest failed");
    }
  }
}
