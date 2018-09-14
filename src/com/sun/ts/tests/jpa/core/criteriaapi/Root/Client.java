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

package com.sun.ts.tests.jpa.core.criteriaapi.Root;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;
import com.sun.ts.tests.jpa.common.schema30.*;
import com.sun.ts.tests.jpa.common.schema30.Order;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.sql.Date;
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1144;
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
      Root<Customer> customer = cquery.from(Customer.class);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1146;
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
      Root<Customer> customer = cquery.from(Customer.class);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1145; PERSISTENCE:JAVADOC:1147;
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
      Root<Customer> customer = cquery.from(Customer.class);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1134;
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
      Root<Customer> customer = cquery.from(Customer.class);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1135;
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
      Root<Customer> customer = cquery.from(Customer.class);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1136;
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
      Root<Customer> customer = cquery.from(Customer.class);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1140;
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
      Root<Customer> customer = cquery.from(Customer.class);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1148;
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
      Root<Customer> customer = cquery.from(Customer.class);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1150;
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
      Root<Customer> customer = cquery.from(Customer.class);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1149; PERSISTENCE:JAVADOC:1151;
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
      Root<Customer> customer = cquery.from(Customer.class);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1137;
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
      Root<Customer> customer = cquery.from(Customer.class);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1141;
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
      Root<Customer> customer = cquery.from(Customer.class);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1160;
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
      Root<Customer> customer = cquery.from(Customer.class);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1162;
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
      Root<Customer> customer = cquery.from(Customer.class);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1161; PERSISTENCE:JAVADOC:1163;
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
      Root<Customer> customer = cquery.from(Customer.class);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1138;
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
      Root<Customer> customer = cquery.from(Customer.class);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1142;
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
      Root<Customer> customer = cquery.from(Customer.class);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1153; PERSISTENCE:JAVADOC:1155;
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1152;
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
      Root<Customer> customer = cquery.from(Customer.class);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1154;
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
      Root<Customer> customer = cquery.from(Customer.class);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1139;
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1143;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * SELECT d FROM Department d INNER JOIN d.lastNameEmployees e WHERE (e.id =
   * 1)
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1156;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * SELECT d FROM Department d JOIN d.lastNameEmployees e WHERE (e.id = 1)
   */
  @SetupMethod(name = "setupDepartmentEmployeeData")
  public void joinMapStringTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();

      CriteriaQuery<Department> cquery = cbuilder.createQuery(Department.class);
      Root<Department> department = cquery.from(Department.class);
      MapJoin<Department, String, Employee> employee = department
          .joinMap("lastNameEmployees");
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
      throw new Fault("joinMapStringTest failed");
    }
  }

  /*
   * @testName: joinMapStringJoinTypeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1158;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * SELECT d FROM Department d INNER JOIN d.lastNameEmployees e WHERE (e.id =
   * 1)
   */
  @SetupMethod(name = "setupDepartmentEmployeeData")
  public void joinMapStringJoinTypeTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();

      CriteriaQuery<Department> cquery = cbuilder.createQuery(Department.class);
      Root<Department> department = cquery.from(Department.class);
      MapJoin<Department, String, Employee> employee = department
          .joinMap("lastNameEmployees", JoinType.INNER);
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
      throw new Fault("joinMapStringJoinTypeTest failed");
    }
  }

  /*
   * @testName: joinMapIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1157; PERSISTENCE:JAVADOC:1159
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
   * @testName: rootGetCorrelationParentIllegalStateExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1131;
   * 
   * @test_Strategy:
   */
  public void rootGetCorrelationParentIllegalStateExceptionTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
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
          "rootGetCorrelationParentIllegalStateExceptionTest failed");
    }
  }
}
