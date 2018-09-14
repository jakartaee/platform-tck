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

package com.sun.ts.tests.jpa.core.criteriaapi.From;

import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.schema30.*;
import com.sun.ts.tests.jpa.common.schema30.Order;

import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
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
   * @assertion_ids: PERSISTENCE:JAVADOC:997;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * SELECT c FROM Customer c JOIN c.work o WHERE (o.id in (4))
   */
  @SetupMethod(name = "setupCustomerData")
  public void joinStringTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      From<Customer, Customer> customer = cquery.from(Customer.class);
      Join<Customer, Address> address = customer.join("work");
      Expression e = cbuilder.literal("4");
      cquery.where(address.get("id").in(e)).select(customer);
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
      throw new Fault("joinStringTest failed");
    }
  }

  /*
   * @testName: joinStringJoinTypeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:999;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * SELECT c FROM Customer c INNER JOIN c.work o WHERE (o.id in (4))
   */
  @SetupMethod(name = "setupCustomerData")
  public void joinStringJoinTypeTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      From<Customer, Customer> customer = cquery.from(Customer.class);
      Join<Customer, Address> address = customer.join("work", JoinType.INNER);
      Expression e = cbuilder.literal("4");
      cquery.where(address.get("id").in(e)).select(customer);
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
      throw new Fault("joinStringJoinTypeTest failed");
    }
  }

  /*
   * @testName: joinStringIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:998; PERSISTENCE:JAVADOC:1000;
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
      From<Customer, Customer> customer = cquery.from(Customer.class);
      try {
        customer.join("doesnotexist");
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
      From<Customer, Customer> customer = cquery.from(Customer.class);
      try {
        customer.join("doesnotexist", JoinType.INNER);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:987;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * SELECT c FROM Customer c JOIN c.work o WHERE (o.id in (4))
   */
  @SetupMethod(name = "setupCustomerData")
  public void joinSingularAttributeTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      From<Customer, Customer> customer = cquery.from(Customer.class);
      Join<Customer, Address> address = customer.join(Customer_.work);
      Expression e = cbuilder.literal("4");
      cquery.where(address.get("id").in(e)).select(customer);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:988;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * SELECT c FROM Customer c INNER JOIN c.work o WHERE (o.id in (4))
   */
  @SetupMethod(name = "setupCustomerData")
  public void joinSingularAttributeJoinTypeTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      From<Customer, Customer> customer = cquery.from(Customer.class);
      Join<Customer, Address> address = customer.join(Customer_.work,
          JoinType.INNER);
      Expression e = cbuilder.literal("4");
      cquery.where(address.get("id").in(e)).select(customer);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:989;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * SELECT c FROM Customer c JOIN c.orders o WHERE (o.id = 1)
   */
  @SetupMethod(name = "setupOrderData")
  public void joinCollectionAttributeTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      From<Customer, Customer> customer = cquery.from(Customer.class);
      CollectionJoin order = customer.join(Customer_.orders);
      cquery.where(cbuilder.equal(order.get("id"), "1")).select(customer);
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
      throw new Fault("joinCollectionAttributeTest failed");
    }
  }

  /*
   * @testName: joinCollectionAttributeJoinTypeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:993;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * SELECT c FROM Customer c INNER JOIN c.orders o WHERE (o.id = 1)
   */
  @SetupMethod(name = "setupOrderData")
  public void joinCollectionAttributeJoinTypeTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      From<Customer, Customer> customer = cquery.from(Customer.class);
      CollectionJoin<Customer, Order> order = customer.join(Customer_.orders,
          JoinType.INNER);
      cquery.where(cbuilder.equal(order.get("id"), "1")).select(customer);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1001;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * SELECT c FROM Customer c JOIN c.orders o WHERE (o.id = 1)
   */
  @SetupMethod(name = "setupOrderData")
  public void joinCollectionStringTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      From<Customer, Customer> customer = cquery.from(Customer.class);
      CollectionJoin<Customer, Order> order = customer.joinCollection("orders");
      cquery.where(cbuilder.equal(order.get("id"), "1")).select(customer);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1003;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * SELECT c FROM Customer c INNER JOIN c.orders o WHERE (o.id = 1)
   */
  @SetupMethod(name = "setupOrderData")
  public void joinCollectionStringJoinTypeTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      From<Customer, Customer> customer = cquery.from(Customer.class);
      CollectionJoin<Customer, Order> order = customer.joinCollection("orders",
          JoinType.INNER);
      cquery.where(cbuilder.equal(order.get("id"), "1")).select(customer);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1002; PERSISTENCE:JAVADOC:1004;
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
      From<Customer, Customer> customer = cquery.from(Customer.class);
      try {
        customer.joinCollection("doesnotexist");
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
      From<Customer, Customer> customer = cquery.from(Customer.class);
      try {
        customer.joinCollection("doesnotexist", JoinType.INNER);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:990;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * SELECT c FROM Customer c JOIN c.orders2 o WHERE (o.id = 1)
   */
  @SetupMethod(name = "setupOrderData")
  public void joinSetAttributeTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      From<Customer, Customer> customer = cquery.from(Customer.class);
      SetJoin<Customer, Order> order = customer.join(Customer_.orders2);
      cquery.where(cbuilder.equal(order.get("id"), "1")).select(customer);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:994
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * SELECT c FROM Customer c INNER JOIN c.orders2 o WHERE (o.id = 1)
   */
  @SetupMethod(name = "setupOrderData")
  public void joinSetAttributeJoinTypeTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      From<Customer, Customer> customer = cquery.from(Customer.class);
      SetJoin<Customer, Order> order = customer.join(Customer_.orders2,
          JoinType.INNER);
      cquery.where(cbuilder.equal(order.get("id"), "1")).select(customer);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1013;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * SELECT c FROM Customer c JOIN c.orders2 o WHERE (o.id = 1)
   */
  @SetupMethod(name = "setupOrderData")
  public void joinSetStringTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      From<Customer, Customer> customer = cquery.from(Customer.class);
      SetJoin<Customer, Order> order = customer.joinSet("orders2");
      cquery.where(cbuilder.equal(order.get("id"), "1")).select(customer);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1015;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * SELECT c FROM Customer c INNER JOIN c.orders o WHERE (o.id = 1)
   */
  @SetupMethod(name = "setupOrderData")
  public void joinSetStringJoinTypeTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      From<Customer, Customer> customer = cquery.from(Customer.class);
      SetJoin<Customer, Order> order = customer.joinSet("orders2",
          JoinType.INNER);
      cquery.where(cbuilder.equal(order.get("id"), "1")).select(customer);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1014; PERSISTENCE:JAVADOC:1016;
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
      From<Customer, Customer> customer = cquery.from(Customer.class);
      try {
        customer.joinSet("doesnotexist");
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
      From<Customer, Customer> customer = cquery.from(Customer.class);
      try {
        customer.joinSet("doesnotexist", JoinType.INNER);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:991;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * SELECT c FROM Customer c JOIN c.orders3 o WHERE (o.id = 1)
   */
  @SetupMethod(name = "setupOrderData")
  public void joinListAttributeTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      From<Customer, Customer> customer = cquery.from(Customer.class);
      ListJoin<Customer, Order> order = customer.join(Customer_.orders3);
      cquery.where(cbuilder.equal(order.get("id"), "1")).select(customer);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:995;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * SELECT c FROM Customer c INNER JOIN c.orders3 o WHERE (o.id = 1)
   */
  @SetupMethod(name = "setupOrderData")
  public void joinListAttributeJoinTypeTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      From<Customer, Customer> customer = cquery.from(Customer.class);
      ListJoin<Customer, Order> order = customer.join(Customer_.orders3,
          JoinType.INNER);
      cquery.where(cbuilder.equal(order.get("id"), "1")).select(customer);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1006; PERSISTENCE:JAVADOC:1008;
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
      Root<Customer> root = cquery.from(Customer.class);
      try {
        root.joinList("doesnotexist");
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
      Root<Customer> root = cquery.from(Customer.class);
      try {
        root.joinList("doesnotexist", JoinType.INNER);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1005;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * SELECT c FROM Customer c JOIN c.orders3 o WHERE (o.id = 1)
   */
  @SetupMethod(name = "setupOrderData")
  public void joinListStringTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      From<Customer, Customer> customer = cquery.from(Customer.class);
      ListJoin<Customer, Order> order = customer.joinList("orders3");
      cquery.where(cbuilder.equal(order.get("id"), "1")).select(customer);
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
      throw new Fault("joinListStringTest failed");
    }
  }

  /*
   * @testName: joinListStringJoinTypeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1007;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * SELECT c FROM Customer c INNER JOIN c.orders3 o WHERE (o.id = 1)
   */
  @SetupMethod(name = "setupOrderData")
  public void joinListStringJoinTypeTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      From<Customer, Customer> customer = cquery.from(Customer.class);
      ListJoin<Customer, Order> order = customer.joinList("orders3",
          JoinType.INNER);
      cquery.where(cbuilder.equal(order.get("id"), "1")).select(customer);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:992;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * SELECT d FROM Department d JOIN d.lastNameEmployees e WHERE (e.id = 1)
   *
   */
  @SetupMethod(name = "setupDepartmentEmployeeData")
  public void joinMapAttributeTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();

      CriteriaQuery<Department> cquery = cbuilder.createQuery(Department.class);
      Root<Department> department = cquery.from(Department.class);
      MapJoin<Department, String, Employee> employee = department
          .join(Department_.lastNameEmployees);
      cquery.where(cbuilder.equal(employee.get("id"), "1")).select(department);
      TypedQuery<Department> tquery = getEntityManager().createQuery(cquery);
      List<Department> clist = tquery.getResultList();

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
      throw new Fault("joinMapAttributeTest failed");
    }
  }

  /*
   * @testName: joinMapAttributeJoinTypeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:996;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * SELECT d FROM Department d INNER JOIN d.lastNameEmployees e WHERE (e.id =
   * 1)
   *
   */
  @SetupMethod(name = "setupDepartmentEmployeeData")
  public void joinMapAttributeJoinTypeTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
      getEntityTransaction().begin();

      CriteriaQuery<Department> cquery = cbuilder.createQuery(Department.class);
      Root<Department> department = cquery.from(Department.class);
      MapJoin<Department, String, Employee> employee = department
          .join(Department_.lastNameEmployees, JoinType.INNER);
      cquery.where(cbuilder.equal(employee.get("id"), "1")).select(department);
      TypedQuery<Department> tquery = getEntityManager().createQuery(cquery);
      List<Department> clist = tquery.getResultList();

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
      throw new Fault("joinMapAttributeJoinTypeTest failed");
    }
  }

  /*
   * @testName: joinMapStringTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1009;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * SELECT d.id, d.name, e.lastname FROM DEPARTMENT d JOIN d.lastNameEmployees
   * e WHERE (e.id = 1)
   *
   */
  @SetupMethod(name = "setupDepartmentEmployeeData")
  public void joinMapStringTest() throws Fault {
    boolean pass = false;
    List<String> expected = new ArrayList<String>();

    expected.add("1, Marketing, Frechette");

    List<String> actual = new ArrayList<String>();
    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
      From<Department, Department> department = cquery.from(Department.class);
      MapJoin<Department, String, Employee> mEmployee = department
          .joinMap("lastNameEmployees");

      cquery.where(cbuilder.equal(mEmployee.get("id"), "1"));
      cquery.multiselect(department.get(Department_.id),
          department.get(Department_.name),
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
      throw new Fault("joinMapStringTest failed");
    }
  }

  /*
   * @testName: joinMapStringJoinTypeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1011;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * SELECT d.id, d.name, e.lastname FROM DEPARTMENT d INNER JOIN
   * d.lastNameEmployees e WHERE (e.id = 1)
   */
  @SetupMethod(name = "setupDepartmentEmployeeData")
  public void joinMapStringJoinTypeTest() throws Fault {
    boolean pass = false;
    List<String> expected = new ArrayList<String>();

    expected.add("1, Marketing, Frechette");

    List<String> actual = new ArrayList<String>();
    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
      From<Department, Department> department = cquery.from(Department.class);
      MapJoin<Department, String, Employee> mEmployee = department
          .joinMap("lastNameEmployees", JoinType.INNER);

      cquery.where(cbuilder.equal(mEmployee.get("id"), "1"));
      cquery.multiselect(department.get(Department_.id),
          department.get(Department_.name),
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
      throw new Fault("joinMapStringJoinTypeTest failed");
    }
  }

  /*
   * @testName: joinMapIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1010; PERSISTENCE:JAVADOC:1012
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
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> root = cquery.from(Customer.class);
      try {
        root.joinMap("doesnotexist");
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
      Root<Customer> root = cquery.from(Customer.class);
      try {
        root.joinMap("doesnotexist", JoinType.INNER);
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
   * @testName: fromGetCorrelationParentIllegalStateExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:984;
   * 
   * @test_Strategy:
   */
  public void fromGetCorrelationParentIllegalStateExceptionTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      From<Customer, Customer> customer = cquery.from(Customer.class);
      boolean isCorr = customer.isCorrelated();
      if (!isCorr) {
        TestUtil.logTrace("isCorrelated() return false");
        pass1 = true;
      } else {
        TestUtil.logErr(
            "Expected isCorrelated() to return false, actual:" + isCorr);
      }
      try {
        customer.getCorrelationParent();
        TestUtil.logErr("Did not throw IllegalStateException");
      } catch (IllegalStateException ise) {
        TestUtil.logTrace("Received expected IllegalStateException");
        pass2 = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception", e);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);
    }

    if (!pass1 || !pass2) {
      throw new Fault(
          "fromGetCorrelationParentIllegalStateExceptionTest failed");
    }
  }

  /*
   * @testName: fromGetCorrelationParentTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:983
   * 
   * @test_Strategy:
   */
  @SetupMethod(name = "setupCustomerData")
  public void fromGetCorrelationParentTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    String expectedPKs[];

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      cquery.select(customer);
      Subquery<String> sq = cquery.subquery(String.class);
      From<Customer, Customer> sqc = sq.correlate(customer);
      boolean isCorr = sqc.isCorrelated();
      if (isCorr) {
        TestUtil.logTrace("From.isCorrelated() return true");
        pass1 = true;
      } else {
        TestUtil.logErr(
            "Expected From.isCorrelated() to return true, actual:" + isCorr);
      }
      From f = sqc.getCorrelationParent();
      String name = f.getJavaType().getSimpleName();
      if (name.equals("Customer")) {
        TestUtil.logTrace("Received expected parent:" + name);
        pass2 = true;
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
        pass3 = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);
    }

    if (!pass1 || !pass2 || !pass3) {
      throw new Fault("fromGetCorrelationParentTest failed");
    }
  }

  /*
   * @testName: fromGetMapAttributeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1028;
   * 
   * @test_Strategy:
   *
   * SELECT d.lastNameEmployees FROM DEPARTMENT d WHERE d.ID = 1
   */
  @SetupMethod(name = "setupDepartmentEmployeeData")

  public void fromGetMapAttributeTest() throws Fault {
    boolean pass = false;
    List<String> expected = new ArrayList<String>();
    expected.add("1, Alan, Frechette");
    expected.add("3, Shelly, McGowan");
    expected.add("5, Stephen, DMilla");

    List<String> actual = new ArrayList<String>();
    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();

      CriteriaQuery cquery = cbuilder.createQuery(Expression.class);
      From<Department, Department> department = cquery.from(Department.class);
      cquery.where(cbuilder.equal(department.get("id"), 1));
      cquery.select(department.get(Department_.lastNameEmployees));
      TypedQuery tquery = getEntityManager().createQuery(cquery);
      List<Employee> list = tquery.getResultList();

      for (Employee e : list) {
        TestUtil.logTrace(" employee:" + e.getId() + ", " + e.getFirstName()
            + ", " + e.getLastName());
        actual
            .add(e.getId() + ", " + e.getFirstName() + ", " + e.getLastName());

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
      throw new Fault("fromGetMapAttributeTest failed");
    }
  }

  /*
   * @testName: pathGetMapAttributeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1098;
   * 
   * @test_Strategy:
   *
   * SELECT d.lastNameEmployees FROM DEPARTMENT d WHERE d.ID = 1
   */
  @SetupMethod(name = "setupDepartmentEmployeeData")
  public void pathGetMapAttributeTest() throws Fault {
    boolean pass = false;
    List<String> expected = new ArrayList<String>();
    expected.add("1, Alan, Frechette");
    expected.add("3, Shelly, McGowan");
    expected.add("5, Stephen, DMilla");

    List<String> actual = new ArrayList<String>();
    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();

      CriteriaQuery cquery = cbuilder.createQuery(Expression.class);
      Path<Department> department = cquery.from(Department.class);
      cquery.where(cbuilder.equal(department.get("id"), 1));
      cquery.select(department.get(Department_.lastNameEmployees));
      TypedQuery tquery = getEntityManager().createQuery(cquery);
      List<Employee> list = tquery.getResultList();

      for (Employee e : list) {
        TestUtil.logTrace(" employee:" + e.getId() + ", " + e.getFirstName()
            + ", " + e.getLastName());
        actual
            .add(e.getId() + ", " + e.getFirstName() + ", " + e.getLastName());

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
      throw new Fault("pathGetMapAttributeTest failed");
    }
  }

}
