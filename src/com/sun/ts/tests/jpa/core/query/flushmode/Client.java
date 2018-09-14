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

package com.sun.ts.tests.jpa.core.query.flushmode;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.schema30.*;

import javax.persistence.*;
import java.sql.Date;
import java.util.*;

public class Client extends Util {

  private final Date d1 = getSQLDate("2000-02-14");

  public Client() {
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

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

  /* Run test */

  /*
   * @testName: flushModeTest1
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:173; PERSISTENCE:JAVADOC:400;
   * PERSISTENCE:JAVADOC:637; PERSISTENCE:JAVADOC:684;
   * 
   * @test_Strategy: Query accessing a simple field The following updates the
   * name of a customer and then executes an EJBQL query selecting customers
   * having the updated name.
   *
   * TypedQuery accessing a simple field The following updates the name of a
   * customer and then executes an EJBQL query selecting customers having the
   * updated name.*
   *
   */
  @SetupMethod(name = "setupCustomerData")
  public void flushModeTest1() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    boolean pass4 = false;
    boolean pass5 = false;
    boolean pass6 = false;
    String expectedPKs[];
    TestUtil.logTrace("Testing TypedQuery version");

    try {
      getEntityTransaction().begin();
      EntityManager em = getEntityManager();
      TestUtil.logTrace("Calling find");
      Customer cust1 = em.find(Customer.class, "1");
      cust1.setName("Michael Bouschen");
      Query q = em.createQuery(
          "SELECT c FROM Customer c WHERE c.name = 'Michael Bouschen'");
      TestUtil.logTrace(
          "EntityManager.getFlushMode() returned:" + em.getFlushMode());
      TestUtil.logTrace("Calling Query.getFlushMode()");
      FlushModeType fmt = q.getFlushMode();
      if (!fmt.equals(em.getFlushMode())) {
        TestUtil.logErr("getFlushMode() called when no mode set expected:"
            + em.getFlushMode() + ", actual:" + fmt);
      } else {
        pass1 = true;

        TestUtil.logTrace("Setting mode to FlushModeType.AUTO");
        q.setFlushMode(FlushModeType.AUTO);
        fmt = q.getFlushMode();
        if (!fmt.equals(FlushModeType.AUTO)) {
          TestUtil.logErr("getFlushMode() called when no mode set expected:"
              + FlushModeType.AUTO + ", actual:" + fmt);
        } else {
          pass2 = true;

          List<Customer> c = q.getResultList();
          expectedPKs = new String[1];
          expectedPKs[0] = "1";

          if (!checkEntityPK(c, expectedPKs)) {
            TestUtil.logErr(
                "Did not get expected results.  Expected 1 references, got: "
                    + c.size());
          } else {
            Customer newCust = em.find(Customer.class, "1");
            if (newCust.getName().equals("Michael Bouschen")) {
              TestUtil.logTrace("Expected results received");

              pass3 = true;
            }
          }
        }
      }
      getEntityTransaction().rollback();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: ", e);
    }

    TestUtil.logTrace("Testing TypedQuery version");

    try {
      getEntityTransaction().begin();
      EntityManager em = getEntityManager();
      TestUtil.logTrace("Calling find");
      Customer cust1 = em.find(Customer.class, "1");
      cust1.setName("Michael Bouschen");
      TypedQuery<Customer> q = em.createQuery(
          "SELECT c FROM Customer c WHERE c.name = 'Michael Bouschen'",
          Customer.class);
      TestUtil.logTrace("Calling getFlushMode()");
      FlushModeType fmt = q.getFlushMode();
      if (!fmt.equals(em.getFlushMode())) {
        TestUtil.logErr("getFlushMode() called when no mode set expected:"
            + em.getFlushMode() + ", actual:" + fmt);
      } else {
        pass4 = true;

        TestUtil.logTrace("Setting mode to FlushModeType.AUTO");
        q.setFlushMode(FlushModeType.AUTO);
        fmt = q.getFlushMode();
        if (!fmt.equals(FlushModeType.AUTO)) {
          TestUtil.logErr("getFlushMode() called when no mode set expected:"
              + FlushModeType.AUTO + ", actual:" + fmt);
        } else {
          pass5 = true;

          List<Customer> c = q.getResultList();
          expectedPKs = new String[1];
          expectedPKs[0] = "1";

          if (!checkEntityPK(c, expectedPKs)) {
            TestUtil.logErr(
                "Did not get expected results.  Expected 1 references, got: "
                    + c.size());
          } else {
            Customer newCust = em.find(Customer.class, "1");
            if (newCust.getName().equals("Michael Bouschen")) {
              pass6 = true;

              TestUtil.logTrace("Expected results received");
            }
          }
        }
      }
      getEntityTransaction().rollback();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: ", e);
    }

    if (!pass1 || !pass2 || !pass3 || !pass4 || !pass5 || !pass6)
      throw new Fault("flushModeTest1 failed");
  }

  /*
   * @testName: flushModeTest2
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:173
   * 
   * @test_Strategy: Query navigating a single-valued relationship. The
   * following updates the customer relationship of an order. It then executes
   * an EJBQL query selecting orders where the related customer has the name of
   * the customer used in the setCustomer call.
   * 
   */
  @SetupMethod(name = "setupOrderData")
  public void flushModeTest2() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("Execute Starting flushModeTest2");
      Order o1 = getEntityManager().find(Order.class, "1");
      Customer cust2 = getEntityManager().find(Customer.class, "2");
      o1.setCustomer(cust2);
      List<Order> result = getEntityManager().createQuery(
          "SELECT o FROM Order o WHERE o.customer.name = 'Arthur D. Frechette'")
          .setFlushMode(FlushModeType.AUTO).getResultList();

      expectedPKs = new String[2];
      expectedPKs[0] = "1";
      expectedPKs[1] = "2";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 2 references, got: "
                + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().rollback();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexception: " + e);
    }

    if (!pass)
      throw new Fault("flushModeTest2 failed");
  }

  /*
   * @testName: flushModeTest3
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:173
   * 
   * @test_Strategy: Query navigating a single-valued relationship. The
   * following updates the name of a customer. It then executes an EJBQL query
   * selecting orders where the related customer has the updated name.
   */
  @SetupMethod(name = "setupOrderData")
  public void flushModeTest3() throws Fault {
    boolean pass = false;
    String expectedPKs[];
    List o;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("Execute Starting flushModeTest3");
      Customer cust1 = getEntityManager().find(Customer.class, "1");
      cust1.setName("Michael Bouschen");
      o = getEntityManager().createQuery(
          "SELECT o FROM Order o WHERE o.customer.name = 'Michael Bouschen'")
          .setFlushMode(FlushModeType.AUTO).getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "1";
      if (!checkEntityPK(o, expectedPKs)) {
        TestUtil
            .logErr("Did not get expected results.  Expected 1 reference, got: "
                + o.size());
      } else {
        Customer newCust = getEntityManager().find(Customer.class, "1");
        if (newCust.getName().equals("Michael Bouschen")) {
          pass = true;
        }

        TestUtil.logTrace("Expected results received");
      }
      getEntityTransaction().rollback();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexception: " + e);
    }

    if (!pass)
      throw new Fault("flushModeTest3 failed");
  }

  /*
   * @testName: flushModeTest4
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:173; PERSISTENCE:SPEC:2079;
   * 
   * @test_Strategy: Query navigating multiple single-valued relationships The
   * following updates the spouse relationship of a customer. It then executes
   * an EJBQL query selecting orders where the spouse of the related customer
   * has the name of the new spouse.
   *
   */
  @SetupMethod(name = "setupOrderData")
  public void flushModeTest4() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("Execute Starting flushModeTest4");
      Customer cust6 = getEntityManager().find(Customer.class, "6");
      Spouse s4 = getEntityManager().find(Spouse.class, "4");
      cust6.setSpouse(s4);
      getEntityManager().merge(cust6);
      s4.setCustomer(cust6);
      getEntityManager().merge(s4);
      List<Order> result = getEntityManager().createQuery(
          "SELECT o FROM Order o WHERE o.customer.spouse.lastName = 'Mullen'")
          .setFlushMode(FlushModeType.AUTO).getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "6";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("Did not get expected results.  Expected "
            + " 2 references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;

      }
      getEntityTransaction().rollback();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexception: " + e);
    }

    if (!pass)
      throw new Fault("flushModeTest4 failed");
  }

  /*
   * @testName: flushModeTest5
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:173
   * 
   * @test_Strategy: Query navigating multiple single-valued relationships The
   * following updates the name of a spouse. It then executes an EJBQL query
   * selecting orders where the related spouse of the related customer has the
   * updated name.
   */
  @SetupMethod(name = "setupOrderData")
  public void flushModeTest5() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("Starting flushModeTest5");
      Spouse s4 = getEntityManager().find(Spouse.class, "4");
      s4.setLastName("Miller");
      List<Order> result = getEntityManager().createQuery(
          "SELECT o FROM Order o WHERE o.customer.spouse.lastName = 'Miller'")
          .setFlushMode(FlushModeType.AUTO).getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "11";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("Did not get expected results.  Expected "
            + " 1 reference, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().rollback();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexception: " + e);
    }

    if (!pass)
      throw new Fault("flushModeTest5 failed");
  }

  /*
   * @testName: flushModeTest6
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:173
   * 
   * @test_Strategy: Query navigating a collection-valued relationship The
   * following removes an order from the customer's orders relationship. It then
   * executes an EJBQL query selecting customers having an order with the
   * removed number.
   */
  @SetupMethod(name = "setupOrderData")
  public void flushModeTest6() throws Fault {
    boolean pass = false;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("Starting flushModeTest6");
      Customer cust4 = getEntityManager().find(Customer.class, "4");
      Order order4 = getEntityManager().find(Order.class, "4");
      Order order9 = getEntityManager().find(Order.class, "9");
      order9.setCustomer(cust4);
      getEntityManager().merge(order9);
      order4.setCustomer(null);
      getEntityManager().merge(order4);
      Vector<Order> orders = new Vector<Order>();
      orders.add(order9);
      cust4.setOrders(orders);
      getEntityManager().merge(cust4);
      List<Customer> result = getEntityManager()
          .createQuery(
              "SELECT c FROM Customer c JOIN c.orders o where o.id = '4' ")
          .setFlushMode(FlushModeType.AUTO).getResultList();

      expectedPKs = new String[0];

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("Did not get expected results.  Expected "
            + " 0 references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().rollback();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("flushModeTest6 failed");
  }

  /*
   * @testName: flushModeTest7
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:173
   * 
   * @test_Strategy: Query navigating a single-valued and a collection-valued
   * relationship The following changes the number of a credit card. It then
   * executes an EJBQL query selecting a spouse whose customer has an order with
   * an credit card having the new number.
   */
  @SetupMethod(name = "setupOrderData")
  public void flushModeTest7() throws Fault {
    boolean pass = false;
    String[] expected = new String[1];
    expected[0] = "2";

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("Starting flushModeTest7");

      CreditCard c17 = getEntityManager().find(CreditCard.class, "17");
      c17.setNumber("1111-1111-1111-1111");
      List<Spouse> result = getEntityManager()
          .createQuery(
              "SELECT s FROM Spouse s JOIN s.customer c JOIN c.orders o "
                  + "WHERE o.creditCard.number = '1111-1111-1111-1111'")
          .setFlushMode(FlushModeType.AUTO).getResultList();

      if (!checkEntityPK(result, expected)) {
        TestUtil.logErr("Did not get expected results.  Expected "
            + expected.length + " references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }

      getEntityTransaction().rollback();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("flushModeTest7 failed");
  }

  /*
   * @testName: secondaryTablesValueTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:192; PERSISTENCE:JAVADOC:193;
   * 
   * @test_Strategy:
   */
  @SetupMethod(name = "setupProductData")
  public void secondaryTablesValueTest() throws Fault {
    boolean pass = false;

    String[] expected = new String[4];
    expected[0] = "20";
    expected[1] = "24";
    expected[2] = "31";
    expected[3] = "37";

    try {
      getEntityTransaction().begin();

      List<Product> result = getEntityManager()
          .createQuery("SELECT p FROM Product p WHERE p.wareHouse = 'Lowell' ")
          .getResultList();

      if (!checkEntityPK(result, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.length + " references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }

      getEntityTransaction().rollback();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("secondaryTablesValueTest failed");
  }

}
