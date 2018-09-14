/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.annotations.onexmanyuni;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.Properties;

public class Client extends PMClientBase {

  final private static long ORDER1_ID = 786l;

  final private static long ORDER2_ID = 787l;

  final private static long ORDER3_ID = 788l;

  final private static long ORDER4_ID = 789l;

  final private static double COST1 = 53;

  final private static double COST2 = 540;

  final private static double COST3 = 155;

  final private static double COST4 = 256;

  final private static long CUST1_ID = 2l;

  final private static long CUST2_ID = 4l;

  final private static String CUST1_NAME = "Ross";

  final private static String CUST2_NAME = "Joey";

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
    } catch (Exception e) {
      TestUtil.logErr("Exception:test failed ", e);
    }
  }

  /*
   * @testName: oneXmanyUniJoinColumn
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1038; PERSISTENCE:SPEC:1039;
   * PERSISTENCE:SPEC:1040; PERSISTENCE:SPEC:1041; PERSISTENCE:SPEC:1042;
   * PERSISTENCE:SPEC:1046; PERSISTENCE:SPEC:1048; PERSISTENCE:SPEC:1097;
   * PERSISTENCE:SPEC:1214; PERSISTENCE:SPEC:1243; PERSISTENCE:JAVADOC:374;
   * 
   * @test_Strategy: The two entities "Customer1" and "RetailOrder2" have
   * One-to-Many relationship.
   * 
   */
  public void oneXmanyUniJoinColumn() throws Fault {

    EntityManager em = getEntityManager();
    EntityTransaction tx = getEntityTransaction();
    Customer1 customer1 = createCustomer(CUST1_ID, CUST1_NAME);
    Customer1 customer2 = createCustomer(CUST2_ID, CUST2_NAME);

    final RetailOrder2 order1 = createOrder(ORDER1_ID, COST1);
    final RetailOrder2 order2 = createOrder(ORDER2_ID, COST2);
    final RetailOrder2 order3 = createOrder(ORDER3_ID, COST3);
    final RetailOrder2 order4 = createOrder(ORDER4_ID, COST4);

    try {
      tx.begin();
      em.persist(customer1);
      em.persist(customer2);
      em.flush();
      em.persist(order1);
      em.persist(order2);
      em.persist(order3);
      em.persist(order4);
      customer1.addOrder(order1);
      customer1.addOrder(order2);
      customer2.addOrder(order3);
      customer2.addOrder(order4);
      em.flush();
      TestUtil.logTrace("Test Passed");
    } catch (Exception e) {

      throw new Fault("Test failed" + e);
    } finally {
      em.remove(order1);
      em.remove(order2);
      em.remove(order3);
      em.remove(order4);
      em.remove(customer1);
      em.remove(customer2);
      tx.commit();
    }

  }

  private RetailOrder2 createOrder(final long id, final double cost) {
    RetailOrder2 order = new RetailOrder2();
    order.setId(id);
    order.setCost(cost);
    return order;
  }

  private Customer1 createCustomer(final long id, final String name) {
    Customer1 customer = new Customer1();
    customer.setId(id);
    customer.setName(name);
    return customer;
  }

  public void cleanup() throws Fault {
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
      getEntityManager().createNativeQuery("Delete from CUSTOMER1")
          .executeUpdate();
      getEntityManager().createNativeQuery("Delete from RETAILORDER2")
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
