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

package com.sun.ts.tests.jpa.core.query.language;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.schema30.*;
import com.sun.ts.lib.harness.SetupMethod;

import javax.persistence.NoResultException;
import javax.persistence.Query;
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

  /* Run test */

  /*
   * @testName: queryTest1
   * 
   * @assertion_ids: PERSISTENCE:SPEC:312; PERSISTENCE:SPEC:322;
   * PERSISTENCE:SPEC:602; PERSISTENCE:SPEC:603; PERSISTENCE:JAVADOC:91;
   * PERSISTENCE:SPEC:785; PERSISTENCE:SPEC:786; PERSISTENCE:SPEC:1595;
   * PERSISTENCE:SPEC:1600;
   * 
   * @test_Strategy: This query is defined on a many-one relationship. Verify
   * the results were accurately returned.
   *
   */
  @SetupMethod(name = "setupOrderData")
  public void queryTest1() throws Fault {
    boolean pass = false;
    String expectedPKs[];
    List o;

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find All Orders for Customer: Robert E. Bissett");
      o = getEntityManager()
          .createQuery(
              "Select Distinct o from Order AS o WHERE o.customer.name = :name")
          .setParameter("name", "Robert E. Bissett").getResultList();

      expectedPKs = new String[2];
      expectedPKs[0] = "4";
      expectedPKs[1] = "9";
      if (!checkEntityPK(o, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 2 references, got: "
                + o.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest1 failed");
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
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("Execute findAllCustomers");
      List result = getEntityManager()
          .createQuery("Select Distinct Object(c) FROM Customer AS c")
          .getResultList();

      expectedPKs = new String[customerRef.length];
      for (int i = 0; i < customerRef.length; i++)
        expectedPKs[i] = Integer.toString(i + 1);

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("Did not get expected results.  Expected "
            + customerRef.length + " references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest2 failed");
  }

  /*
   * @testName: queryTest3
   * 
   * @assertion_ids: PERSISTENCE:SPEC:321; PERSISTENCE:SPEC:317.2;
   * PERSISTENCE:SPEC:332; PERSISTENCE:SPEC:323; PERSISTENCE:SPEC:517;
   * PERSISTENCE:SPEC:518; PERSISTENCE:SPEC:519; PERSISTENCE:JAVADOC:93;
   * PERSISTENCE:JAVADOC:94
   * 
   * @test_Strategy: This query is defined on a many-many relationship. Verify
   * the results were accurately returned.
   */

  @SetupMethod(name = "setupAliasData")
  public void queryTest3() throws Fault {
    boolean pass = false;
    String expectedPKs[];
    List c;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find All Customers with Alias: imc");
      c = getEntityManager().createQuery(
          "Select Distinct Object(c) FrOm Customer c, In(c.aliases) a WHERE a.alias = :aName")
          .setParameter("aName", "imc").getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "8";
      if (!checkEntityPK(c, expectedPKs)) {
        TestUtil
            .logErr("Did not get expected results.  Expected 1 reference, got: "
                + c.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest3 failed");
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
    Query q;

    try {
      getEntityTransaction().begin();
      Customer expected = getEntityManager().find(Customer.class, "3");
      TestUtil.logTrace("find Customer with Home Address in Swansea");
      q = getEntityManager().createQuery(
          "SELECT c from Customer c WHERE c.home.street = :street AND c.home.city = :city AND c.home.state = :state and c.home.zip = :zip")
          .setParameter("street", "125 Moxy Lane")
          .setParameter("city", "Swansea").setParameter("state", "MA")
          .setParameter("zip", "11345");

      c = (Customer) q.getSingleResult();

      if (expected == c) {
        TestUtil.logTrace("Expected results received");
        pass = true;
      } else {
        TestUtil.logErr("Did not get expected results.");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest4 failed");
  }

  /*
   * @testName: queryTest5
   * 
   * @assertion_ids: PERSISTENCE:SPEC:323; PERSISTENCE:SPEC:760;
   * PERSISTENCE:SPEC:761
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
    List c;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find all Customers with AXP Credit Cards");
      c = getEntityManager().createQuery(
          "Select Distinct Object(c) fRoM Customer c, IN(c.creditCards) a where a.type = :ccard")
          .setParameter("ccard", "AXP").getResultList();

      expectedPKs = new String[7];
      expectedPKs[0] = "1";
      expectedPKs[1] = "4";
      expectedPKs[2] = "5";
      expectedPKs[3] = "8";
      expectedPKs[4] = "9";
      expectedPKs[5] = "12";
      expectedPKs[6] = "15";

      if (!checkEntityPK(c, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 7 references, got: "
                + c.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest5 failed");
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
    List c;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find Customers with Home Address Information");

      c = getEntityManager().createQuery(
          "SELECT DISTINCT c from Customer c WHERE c.home.street = :street OR c.home.city = :city OR c.home.state = :state or c.home.zip = :zip")
          .setParameter("street", "47 Skyline Drive")
          .setParameter("city", "Chelmsford").setParameter("state", "VT")
          .setParameter("zip", "02155").getResultList();

      expectedPKs = new String[4];
      expectedPKs[0] = "1";
      expectedPKs[1] = "10";
      expectedPKs[2] = "11";
      expectedPKs[3] = "13";

      if (!checkEntityPK(c, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 4 references, got: "
                + c.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest6 failed");
  }

  /*
   * @testName: queryTest7
   * 
   * @assertion_ids: PERSISTENCE:SPEC:319; PERSISTENCE:SPEC:735;
   * PERSISTENCE:SPEC:784
   * 
   * @test_Strategy: Ensure identification variables can be interpreted
   * correctly regardless of case. Verify the results were accurately returned.
   *
   */
  @SetupMethod(name = "setupProductData")
  public void queryTest7() throws Fault {
    boolean pass = false;
    List p;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find All Products");
      p = getEntityManager()
          .createQuery(
              "Select DISTINCT Object(P) From Product p where P.quantity < 10")
          .getResultList();

      String[] expectedPKs = new String[2];
      expectedPKs[0] = "15";
      expectedPKs[1] = "21";

      if (!checkEntityPK(p, expectedPKs)) {
        TestUtil.logErr("Did not get expected results.  Expected "
            + expectedPKs.length + " references, got: " + p.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest7 failed");
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
    List o;

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "find all orders where the total price is NOT less than $4500");
      o = getEntityManager().createQuery(
          "Select Distinct Object(o) FROM Order o WHERE NOT o.totalPrice < 4500")
          .getResultList();

      expectedPKs = new String[3];
      expectedPKs[0] = "5";
      expectedPKs[1] = "11";
      expectedPKs[2] = "16";
      if (!checkEntityPK(o, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 3 references, got: "
                + o.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest8 failed");
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
    List o;

    try {
      getEntityTransaction().begin();
      TestUtil
          .logTrace("find all orders where the customer name is Karen R. Tegan"
              + " OR the total price is less than $100");
      o = getEntityManager().createQuery(
          "SeLeCt DiStInCt oBjEcT(o) FROM Order AS o WHERE o.customer.name = 'Karen R. Tegan' OR o.totalPrice < 100")
          .getResultList();

      expectedPKs = new String[5];
      expectedPKs[0] = "6";
      expectedPKs[1] = "9";
      expectedPKs[2] = "10";
      expectedPKs[3] = "12";
      expectedPKs[4] = "13";
      if (!checkEntityPK(o, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 5 references, got: "
                + o.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest9 failed");
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
    List o;

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find all orders where line item quantity is 1 AND the"
          + " order total less than 100 or customer name is Robert E. Bissett");

      o = getEntityManager().createQuery(
          "select distinct Object(o) FROM Order AS o, in(o.lineItemsCollection) l WHERE (l.quantity < 2) AND ((o.totalPrice < (3 + 54 * 2 + -8)) OR (o.customer.name = 'Robert E. Bissett'))")
          .getResultList();

      expectedPKs = new String[4];
      expectedPKs[0] = "4";
      expectedPKs[1] = "9";
      expectedPKs[2] = "12";
      expectedPKs[3] = "13";
      if (!checkEntityPK(o, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 4 references, got: "
                + o.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest10 failed");
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
    List o;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find all orders with line item quantity < 2"
          + " for customer Robert E. Bissett");
      o = getEntityManager().createQuery(
          "SELECT DISTINCT Object(o) FROM Order o, in(o.lineItemsCollection) l WHERE l.quantity < 2 AND o.customer.name = 'Robert E. Bissett'")
          .getResultList();

      expectedPKs = new String[2];
      expectedPKs[0] = "4";
      expectedPKs[1] = "9";
      if (!checkEntityPK(o, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 2 references, got: "
                + o.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest11 failed");
  }

  /*
   * @testName: queryTest12
   * 
   * @assertion_ids: PERSISTENCE:SPEC:349; PERSISTENCE:SPEC:348.3
   * 
   * @test_Strategy: Execute a query containing the comparison operator BETWEEN.
   * Verify the results were accurately returned.
   * 
   */
  @SetupMethod(name = "setupOrderData")
  public void queryTest12() throws Fault {
    boolean pass = false;
    String expectedPKs[];
    List o;

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "find all orders with a total price BETWEEN $1000 and $1200");
      o = getEntityManager().createQuery(
          "SELECT DISTINCT Object(o) From Order o where o.totalPrice BETWEEN 1000 AND 1200")
          .getResultList();

      expectedPKs = new String[5];
      expectedPKs[0] = "1";
      expectedPKs[1] = "3";
      expectedPKs[2] = "7";
      expectedPKs[3] = "8";
      expectedPKs[4] = "14";
      if (!checkEntityPK(o, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 5 references, got: "
                + o.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest12 failed");
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
    List o;

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "find all orders with a total price NOT BETWEEN $1000 and $1200");
      o = getEntityManager().createQuery(
          "SELECT DISTINCT Object(o) From Order o where o.totalPrice NOT bETwEeN 1000 AND 1200")
          .getResultList();

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
      if (!checkEntityPK(o, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 15 references, got: "
                + o.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest13 failed");
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
    List o;

    try {
      getEntityTransaction().begin();
      TestUtil
          .logTrace("find all orders that do not have approved Credit Cards");
      o = getEntityManager().createQuery(
          "select distinct Object(o) From Order o WHERE o.creditCard.approved = FALSE")
          .getResultList();

      expectedPKs = new String[6];
      expectedPKs[0] = "1";
      expectedPKs[1] = "7";
      expectedPKs[2] = "11";
      expectedPKs[3] = "13";
      expectedPKs[4] = "18";
      expectedPKs[5] = "20";
      if (!checkEntityPK(o, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 6 references, got: "
                + o.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest14 failed");
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
    Query q;

    try {
      getEntityTransaction().begin();
      Customer expected = getEntityManager().find(Customer.class, "5");
      TestUtil.logTrace("find customer with name: Stephen S. D'Milla");
      q = getEntityManager()
          .createQuery("sElEcT c FROM Customer c Where c.name = :cName")
          .setParameter("cName", "Stephen S. D'Milla");

      c = (Customer) q.getSingleResult();

      if (expected == c) {
        TestUtil.logTrace("Expected results received");
        pass = true;
      } else {
        TestUtil.logErr("Did not get expected results.");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest15 failed");
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
    List c;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find all customers IN home city: Lexington");
      c = getEntityManager().createQuery(
          "select distinct c FROM Customer c WHERE c.home.city IN ('Lexington')")
          .getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "2";
      if (!checkEntityPK(c, expectedPKs)) {
        TestUtil
            .logErr("Did not get expected results.  Expected 1 reference, got: "
                + c.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest16 failed");
  }

  /*
   * @testName: queryTest17
   * 
   * @assertion_ids: PERSISTENCE:SPEC:352; PERSISTENCE:SPEC:353
   * 
   * @test_Strategy: Execute a query using comparison operator NOT IN in a
   * comparison expression within the WHERE clause. Verify the results were
   * accurately returned.
   */
  @SetupMethod(name = "setupCustomerData")
  public void queryTest17() throws Fault {
    boolean pass = false;
    String expectedPKs[];
    List c;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "find all customers NOT IN home city: Swansea or Brookline");
      c = getEntityManager().createQuery(
          "SELECT DISTINCT Object(c) FROM Customer c Left Outer Join c.home h WHERE "
              + " h.city Not iN ('Swansea', 'Brookline')")
          .getResultList();

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

      if (!checkEntityPK(c, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 15 references, got: "
                + c.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest17 failed");
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
    List c;
    try {
      getEntityTransaction().begin();
      TestUtil
          .logTrace("find All Customers with home ZIP CODE that ends in 77");
      c = getEntityManager().createQuery(
          "select distinct Object(c) FROM Customer c WHERE c.home.zip LIKE '%77'")
          .getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "2";
      if (!checkEntityPK(c, expectedPKs)) {
        TestUtil
            .logErr("Did not get expected results.  Expected 1 reference, got: "
                + c.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest18 failed");
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
    List c;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "find all customers with a home zip code that does not contain"
              + " 44 in the third and fourth position");
      c = getEntityManager().createQuery(
          "Select Distinct Object(c) FROM Customer c WHERE c.home.zip not like '%44_'")
          .getResultList();

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
      if (!checkEntityPK(c, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 15 references, got: "
                + c.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest19 failed");
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
    List c;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find all customers who do not have aliases");
      c = getEntityManager().createQuery(
          "Select Distinct Object(c) FROM Customer c WHERE c.aliases IS EMPTY")
          .getResultList();

      expectedPKs = new String[7];
      expectedPKs[0] = "6";
      expectedPKs[1] = "15";
      expectedPKs[2] = "16";
      expectedPKs[3] = "17";
      expectedPKs[4] = "18";
      expectedPKs[5] = "19";
      expectedPKs[6] = "20";

      if (!checkEntityPK(c, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 7 references, got: "
                + c.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest20 failed");
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
    List c;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find all customers who have aliases");
      c = getEntityManager().createQuery(
          "Select Distinct Object(c) FROM Customer c WHERE c.aliases IS NOT EMPTY")
          .getResultList();

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

      if (!checkEntityPK(c, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 15 reference, got: "
                + c.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest21 failed");
  }

  /*
   * @testName: queryTest22
   * 
   * @assertion_ids: PERSISTENCE:SPEC:359; PERSISTENCE:SPEC:763
   * 
   * @test_Strategy: Execute a query using the IS NULL comparison operator in
   * the WHERE clause. Verify the results were accurately returned.
   */
  @SetupMethod(name = "setupCustomerData")
  public void queryTest22() throws Fault {
    boolean pass = false;
    String expectedPKs[];
    List c;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find All Customers who have a null work zip code");
      c = getEntityManager().createQuery(
          "sELEct dIsTiNcT oBjEcT(c) FROM Customer c WHERE c.work.zip IS NULL")
          .getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "13";
      if (!checkEntityPK(c, expectedPKs)) {
        TestUtil
            .logErr("Did not get expected results.  Expected 1 reference, got: "
                + c.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest22 failed");
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
    List c;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "find all customers who do not have null work zip code entry");
      c = getEntityManager().createQuery(
          "Select Distinct Object(c) FROM Customer c WHERE c.work.zip IS NOT NULL")
          .getResultList();

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

      if (!checkEntityPK(c, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 17 references, got: "
                + c.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest23 failed");
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
  @SetupMethod(name = "setupAliasData")
  public void queryTest24() throws Fault {
    boolean pass = false;
    String expectedPKs[];
    List a;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find all aliases who have match: stevie");
      a = getEntityManager().createQuery(
          "Select Distinct Object(a) From Alias a WHERE a.alias = CONCAT('ste', 'vie')")
          .getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "14";
      if (!checkEntityPK(a, expectedPKs)) {
        TestUtil
            .logErr("Did not get expected results.  Expected 1 reference, got: "
                + a.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest24 failed");
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
  @SetupMethod(name = "setupAliasData")
  public void queryTest25() throws Fault {
    boolean pass = false;
    String expectedPKs[];
    List a;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find all aliases containing the substring: iris");
      a = getEntityManager().createQuery(
          "Select Distinct Object(a) From Alias a WHERE a.alias = SUBSTRING(:string1, :int2, :int3)")
          .setParameter("string1", "iris").setParameter("int2", 1)
          .setParameter("int3", 4).getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "20";
      if (!checkEntityPK(a, expectedPKs)) {
        TestUtil
            .logErr("Did not get expected results.  Expected 1 reference, got: "
                + a.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest25 failed");
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
  @SetupMethod(name = "setupAliasData")
  public void queryTest26() throws Fault {
    boolean pass = false;
    String expectedPKs[];
    List a;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "find aliases whose alias name is greater than 4 characters");
      a = getEntityManager().createQuery(
          "Select Distinct OBjeCt(a) From Alias a WHERE LENGTH(a.alias) > 4")
          .getResultList();

      expectedPKs = new String[7];
      expectedPKs[0] = "8";
      expectedPKs[1] = "10";
      expectedPKs[2] = "13";
      expectedPKs[3] = "14";
      expectedPKs[4] = "18";
      expectedPKs[5] = "28";
      expectedPKs[6] = "29";
      if (!checkEntityPK(a, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 7 references, got: "
                + a.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest26 failed");
  }

  /*
   * @testName: queryTest27
   * 
   * @assertion_ids: PERSISTENCE:SPEC:369.5; PERSISTENCE:SPEC:368;
   * 
   * @test_Strategy: Execute a query which includes the arithmetic function ABS
   * in a functional expression within the WHERE clause. Verify the results were
   * accurately returned.
   */
  @SetupMethod(name = "setupOrderData")
  public void queryTest27() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    String expectedPKs[];
    List o;
    try {
      getEntityTransaction().begin();
      TestUtil.logMsg("Testing ABS with numeric Java object types");
      TestUtil.logTrace("find all Orders with a total price greater than 1180");
      o = getEntityManager().createQuery(
          "Select DISTINCT Object(o) From Order o WHERE :dbl < ABS(o.totalPrice)")
          .setParameter("dbl", 1180D).getResultList();

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
      if (!checkEntityPK(o, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 9 references, got: "
                + o.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass1 = true;
      }
      TestUtil.logMsg("Testing ABS with primitive numeric type");
      TestUtil.logTrace("find all Orders with a total price greater than 1180");
      o = getEntityManager().createQuery(
          "Select DISTINCT Object(o) From Order o WHERE o.totalPrice > ABS(:dbl)")
          .setParameter("dbl", 1180.55D).getResultList();

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
      if (!checkEntityPK(o, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 9 references, got: "
                + o.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass2 = true;
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass1 || !pass2)
      throw new Fault("queryTest27 failed");
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
  @SetupMethod(name = "setupAliasData")
  public void queryTest28() throws Fault {
    boolean pass = false;
    String expectedPKs[];
    List a;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "find all aliases who contain the string: ev in their alias name");
      a = getEntityManager().createQuery(
          "Select Distinct Object(a) from Alias a where LOCATE('ev', a.alias) = 3")
          .getResultList();

      expectedPKs = new String[3];
      expectedPKs[0] = "13";
      expectedPKs[1] = "14";
      expectedPKs[2] = "18";
      if (!checkEntityPK(a, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 3 references, got: "
                + a.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest28 failed");
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
  @SetupMethod(name = "setupAliasData")
  public void queryTest29() throws Fault {
    boolean pass = false;
    String expectedPKs[];
    List a;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find aliases who are members of customersNoop");
      a = getEntityManager().createQuery(
          "Select Distinct Object(a) FROM Alias a WHERE a.customerNoop MEMBER OF a.customersNoop")
          .getResultList();

      expectedPKs = new String[0];
      if (!checkEntityPK(a, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 0 references, got: "
                + a.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest29 failed");
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
  @SetupMethod(name = "setupAliasData")
  public void queryTest30() throws Fault {
    boolean pass = false;
    String expectedPKs[];
    List a;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find aliases who are NOT members of collection");
      a = getEntityManager().createQuery(
          "Select Distinct Object(a) FROM Alias a WHERE a.customerNoop NOT MEMBER OF a.customersNoop")
          .getResultList();

      expectedPKs = new String[aliasRef.length];
      for (int i = 0; i < aliasRef.length; i++)
        expectedPKs[i] = Integer.toString(i + 1);
      if (!checkEntityPK(a, expectedPKs)) {
        TestUtil.logErr("Did not get expected results.  Expected "
            + aliasRef.length + "references, got: " + a.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest30 failed");
  }

  /*
   * @testName: queryTest31
   * 
   * @assertion_ids: PERSISTENCE:SPEC:358
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
    List c;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find all customers with an alias LIKE: sh_ll");
      c = getEntityManager().createQuery(
          "select distinct Object(c) FROM Customer c, in(c.aliases) a WHERE a.alias LIKE 'sh\\_ll' escape '\\'")
          .getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "3";
      if (!checkEntityPK(c, expectedPKs)) {
        TestUtil
            .logErr("Did not get expected results.  Expected 1 reference, got: "
                + c.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest31 failed");
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
    List o;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "find all orders where line items are members of the orders");
      o = getEntityManager().createQuery(
          "Select Distinct Object(o) FROM Order o, LineItem l WHERE l MEMBER o.lineItemsCollection")
          .getResultList();

      expectedPKs = new String[orderRef.length];
      for (int i = 0; i < orderRef.length; i++)
        expectedPKs[i] = Integer.toString(i + 1);

      if (!checkEntityPK(o, expectedPKs)) {
        TestUtil.logErr("Did not get expected results.  Expected "
            + orderRef.length + "references, got: " + o.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest32 failed");
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
    List o;
    LineItem liDvc;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "find orders whose orders are do NOT contain the specified line items");
      liDvc = getEntityManager().find(LineItem.class, "30");
      o = getEntityManager().createQuery(
          "Select Distinct Object(o) FROM Order o WHERE :param NOT MEMBER o.lineItemsCollection")
          .setParameter("param", liDvc).getResultList();

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

      if (!checkEntityPK(o, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 19 references, got: "
                + o.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;

      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest33 failed");
  }

  /*
   * @testName: queryTest34
   * 
   * @assertion_ids: PERSISTENCE:SPEC:363.1
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
    List o;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find orders who have Samples in their orders");
      o = getEntityManager().createQuery(
          "Select Distinct Object(o) FROM Order o WHERE o.sampleLineItem MEMBER OF o.lineItemsCollection")
          .getResultList();

      expectedPKs = new String[2];
      expectedPKs[0] = "1";
      expectedPKs[1] = "6";
      if (!checkEntityPK(o, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 2 references, got: "
                + o.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;

      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest34 failed");
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
    List o;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "find all orders which contain lineitems not of quantities 1 or 5");
      o = getEntityManager().createQuery(
          "Select Distinct Object(o) from Order o, in(o.lineItemsCollection) l where l.quantity NOT IN (1, 5)")
          .getResultList();

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
      if (!checkEntityPK(o, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 9 references, got: "
                + o.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest35 failed");
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
    List c;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find all customers who lives in city Attleboro");
      c = getEntityManager()
          .createQuery("SELECT c From Customer c where c.home.city IN(:city)")
          .setParameter("city", "Attleboro").getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "13";
      if (!checkEntityPK(c, expectedPKs)) {
        TestUtil
            .logErr("Did not get expected results.  Expected 1 reference, got: "
                + c.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest36 failed");
  }

  /*
   * @testName: queryTest37
   * 
   * @assertion_ids: PERSISTENCE:SPEC:354
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
    List c1;
    List c2;

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "Execute two queries composed differently and verify results");
      c1 = getEntityManager().createQuery(
          "SELECT DISTINCT Object(c) from Customer c where c.home.state IN('NH', 'RI')")
          .getResultList();

      expectedPKs = new String[5];
      expectedPKs[0] = "5";
      expectedPKs[1] = "6";
      expectedPKs[2] = "12";
      expectedPKs[3] = "14";
      expectedPKs[4] = "16";

      c2 = getEntityManager().createQuery(
          "SELECT DISTINCT Object(c) from Customer c WHERE (c.home.state = 'NH') OR (c.home.state = 'RI')")
          .getResultList();

      expectedPKs2 = new String[5];
      expectedPKs2[0] = "5";
      expectedPKs2[1] = "6";
      expectedPKs2[2] = "12";
      expectedPKs2[3] = "14";
      expectedPKs2[4] = "16";

      if (!checkEntityPK(c1, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results for first query.  Expected 5 reference, got: "
                + c1.size());
      } else {
        TestUtil.logTrace("Expected results received for first query");
        pass1 = true;
      }

      if (!checkEntityPK(c2, expectedPKs2)) {
        TestUtil.logErr(
            "Did not get expected results for second query.  Expected 5 reference, got: "
                + c2.size());
      } else {
        TestUtil.logTrace("Expected results received for second query");
        pass2 = true;
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass1 || !pass2)
      throw new Fault("queryTest37 failed");
  }

  /*
   * @testName: queryTest38
   * 
   * @assertion_ids: PERSISTENCE:SPEC:369.7; PERSISTENCE:SPEC:368;
   * 
   * @test_Strategy: Execute a query which includes the arithmetic function MOD
   * in a functional expression within the WHERE clause. Verify the results were
   * accurately returned.
   */
  @SetupMethod(name = "setupProductData")
  public void queryTest38() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    String expectedPKs[];
    List p;

    try {
      getEntityTransaction().begin();
      TestUtil.logMsg("Testing MOD with numeric Java object types");
      TestUtil.logTrace("find orders that have the quantity of 50 available");
      Integer value1 = 550;
      Integer value2 = 100;
      p = getEntityManager()
          .createQuery("Select DISTINCT Object(p) From Product p where MOD("
              + value1 + "," + value2 + ") = p.quantity")
          .getResultList();

      expectedPKs = new String[2];
      expectedPKs[0] = "5";
      expectedPKs[1] = "20";
      if (!checkEntityPK(p, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 2 references, got: "
                + p.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass1 = true;
      }

      TestUtil.logMsg("Testing MOD with primitive numeric type");
      TestUtil.logTrace("find orders that have the quantity of 50 available");
      p = getEntityManager().createQuery(
          "Select DISTINCT Object(p) From Product p where MOD(550, 100) = p.quantity")
          .getResultList();

      expectedPKs = new String[2];
      expectedPKs[0] = "5";
      expectedPKs[1] = "20";
      if (!checkEntityPK(p, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 2 references, got: "
                + p.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass2 = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass1 || !pass2)
      throw new Fault("queryTest38 failed");
  }

  /*
   * @testName: queryTest39
   * 
   * @assertion_ids: PERSISTENCE:SPEC:369.6; PERSISTENCE:SPEC:814;
   * PERSISTENCE:SPEC:816; PERSISTENCE:SPEC:368;
   * 
   * @test_Strategy: Execute a query which includes the arithmetic function SQRT
   * in a functional expression within the WHERE clause. Verify the results were
   * accurately returned.
   */
  @SetupMethod(name = "setupOrderData")
  public void queryTest39() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    String expectedPKs[];
    List c;

    try {
      getEntityTransaction().begin();
      TestUtil.logMsg("Testing SQRT with numeric Java object types");
      TestUtil.logTrace("find customers with specific credit card balance");
      c = getEntityManager().createQuery(
          "Select Distinct OBJECT(c) from Customer c, IN(c.creditCards) b where SQRT(b.balance) = :dbl")
          .setParameter("dbl", 50D).getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "3";

      if (!checkEntityPK(c, expectedPKs)) {
        TestUtil
            .logErr("Did not get expected results.  Expected 1 reference, got: "
                + c.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass1 = true;
      }

      TestUtil.logMsg("Testing SQRT with primitive numeric type");
      TestUtil.logTrace("find customers with specific credit card balance");
      c = getEntityManager().createQuery(
          "Select Distinct OBJECT(c) from Customer c, IN(c.creditCards) b where SQRT(b.balance) = SQRT(:dbl)")
          .setParameter("dbl", 2500D).getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "3";

      if (!checkEntityPK(c, expectedPKs)) {
        TestUtil
            .logErr("Did not get expected results.  Expected 1 reference, got: "
                + c.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass2 = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass1 || !pass2)
      throw new Fault("queryTest39 failed");
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
    List p1;
    List p2;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "Execute two queries composed differently and verify results"
              + " Execute Query 1");
      p1 = getEntityManager().createQuery(
          "Select DISTINCT OBJECT(p) From Product p where p.quantity BETWEEN 10 AND 20")
          .getResultList();
      expectedPKs = new String[7];
      expectedPKs[0] = "8";
      expectedPKs[1] = "9";
      expectedPKs[2] = "17";
      expectedPKs[3] = "27";
      expectedPKs[4] = "28";
      expectedPKs[5] = "31";
      expectedPKs[6] = "36";

      TestUtil.logTrace("Execute Query 2");
      p2 = getEntityManager().createQuery(
          "Select DISTINCT OBJECT(p) From Product p where (p.quantity >= 10) AND (p.quantity <= 20)")
          .getResultList();

      expectedPKs2 = new String[7];
      expectedPKs2[0] = "8";
      expectedPKs2[1] = "9";
      expectedPKs2[2] = "17";
      expectedPKs2[3] = "27";
      expectedPKs2[4] = "28";
      expectedPKs2[5] = "31";
      expectedPKs2[6] = "36";

      if (!checkEntityPK(p1, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results for first query in queryTest40. "
                + "  Expected 7 references, got: " + p1.size());
      } else {
        TestUtil.logTrace(
            "Expected results received for first query in queryTest40.");
        pass1 = true;
      }

      if (!checkEntityPK(p2, expectedPKs2)) {
        TestUtil.logErr(
            "Did not get expected results for second query in queryTest40. "
                + "  Expected 7 references, got: " + p2.size());
      } else {
        TestUtil.logTrace(
            "Expected results received for second query in queryTest40.");
        pass2 = true;
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass1 || !pass2)
      throw new Fault("queryTest40 failed");
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
    List p1;
    List p2;

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "Execute two queries composed differently and verify results"
              + " Execute first query");
      p1 = getEntityManager().createQuery(
          "Select DISTINCT Object(p) From Product p where p.quantity NOT BETWEEN 20 AND 200")
          .getResultList();

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

      if (!checkEntityPK(p1, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results for first query.  Expected 31 references, got: "
                + p1.size());
      } else {
        TestUtil.logTrace("Expected results received for first query");
        pass1 = true;
      }

      TestUtil.logTrace("Execute second query");
      p2 = getEntityManager().createQuery(
          "Select DISTINCT Object(p) From Product p where (p.quantity < 20) OR (p.quantity > 200)")
          .getResultList();

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

      if (!checkEntityPK(p2, expectedPKs2)) {
        TestUtil.logErr(
            "Did not get expected results for second query.  Expected 31 references, got: "
                + p2.size());
      } else {
        TestUtil.logTrace("Expected results received for second query");
        pass2 = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass1 || !pass2)
      throw new Fault("queryTest41 failed");
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
    List o;
    try {
      getEntityTransaction().begin();
      TestUtil
          .logTrace("find all orders where related customer name is not null");
      o = getEntityManager().createQuery(
          "Select Distinct Object(o) from Order o where o.customer.name IS NOT NULL")
          .getResultList();

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

      if (!checkEntityPK(o, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 19 references, got: "
                + o.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest42 failed");
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
    List p;
    try {
      getEntityTransaction().begin();
      TestUtil
          .logTrace("Check results of AND operator: False AND False = False");
      p = getEntityManager().createQuery(
          "Select Distinct Object(p) from Product p where (p.quantity > (500 + :int1)) AND (p.partNumber IS NULL)")
          .setParameter("int1", 100).getResultList();

      expectedPKs = new String[0];
      if (!checkEntityPK(p, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 0 references, got: "
                + p.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest43 failed");
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
    List p;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "provide a null value for a comparison operation and verify the results");
      p = getEntityManager()
          .createQuery(
              "Select Distinct Object(p) from Product p where p.name = ?1")
          .setParameter(1, null).getResultList();

      expectedPKs = new String[0];

      if (!checkEntityPK(p, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 0 references, got: "
                + p.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest44 failed");
  }

  /*
   * @testName: queryTest45
   * 
   * @assertion_ids: PERSISTENCE:SPEC:361
   * 
   * @test_Strategy: Execute a query using IS NOT EMPTY in a
   * collection_valued_association_field where the field is EMPTY.
   */
  @SetupMethod(name = "setupCustomerData")
  public void queryTest45() throws Fault {
    boolean pass = false;
    String expectedPKs[];
    List c;

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find customers whose id is greater than 1 "
          + "OR where the relationship is NOT EMPTY");
      c = getEntityManager().createQuery(
          "Select Object(c) from Customer c where c.aliasesNoop IS NOT EMPTY or c.id <> '1'")
          .getResultList();

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
      if (!checkEntityPK(c, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 19 references, got: "
                + c.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest45 failed");
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
    List c;
    final String[] expectedZips = new String[] { "00252", "00252", "00252",
        "00252", "00252", "00252", "00252", "00252", "00252", "00252", "00252",
        "00252", "00252", "00252", "00252", "00252", "11345" };
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find work zip codes that are not null");
      c = getEntityManager().createQuery(
          "Select c.work.zip from Customer c where c.work.zip IS NOT NULL ORDER BY c.work.zip ASC")
          .getResultList();

      String[] result = (String[]) (c.toArray(new String[c.size()]));
      TestUtil.logTrace("Compare results of work zip codes");
      pass = Arrays.equals(expectedZips, result);
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest47 failed");
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
    List o;
    final Double[] expectedBalances = new Double[] { 400D, 500D, 750D, 1000D,
        1400D, 1500D, 2000D, 2500D, 4400D, 5000D, 5500D, 7000D, 7400D, 8000D,
        9500D, 13000D, 15000D, 23000D };
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find all credit card balances");
      o = getEntityManager().createQuery(
          "Select Distinct o.creditCard.balance from Order o ORDER BY o.creditCard.balance ASC")
          .getResultList();

      Double[] result = (Double[]) (o.toArray(new Double[o.size()]));
      Iterator i = o.iterator();
      while (i.hasNext()) {
        TestUtil.logTrace("query results returned:  " + (Double) i.next());
      }
      TestUtil.logTrace("Compare expected results to query results");
      pass = Arrays.equals(expectedBalances, result);

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest48 failed");
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
    List c;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find All Customers who have a null relationship");
      c = getEntityManager().createQuery(
          "Select Distinct Object(c) FROM Customer c, in(c.aliases) a WHERE a.customerNoop IS NULL")
          .getResultList();

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

      if (!checkEntityPK(c, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 13 references, got: "
                + c.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest49 failed");
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
    List c;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "find all customers with an alias that contains an underscore");
      c = getEntityManager().createQuery(
          "select distinct Object(c) FROM Customer c, in(c.aliases) a WHERE a.alias LIKE '%\\_%' escape '\\'")
          .getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "3";
      if (!checkEntityPK(c, expectedPKs)) {
        TestUtil
            .logErr("Did not get expected results.  Expected 1 reference, got: "
                + c.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;

      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest50 failed");
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
  @SetupMethod(name = "setupCustomerData")
  public void queryTest51() throws Fault {
    boolean pass = false;
    List c;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find All Customers who do not have null relationship");
      c = getEntityManager().createQuery(
          "sElEcT Distinct oBJeCt(c) FROM Customer c, IN(c.aliases) a WHERE a.customerNoop IS NOT NULL")
          .getResultList();

      if (c.size() != 0) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 0 references, got: "
                + c.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest51 failed");
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

  @SetupMethod(name = "setupAliasData")
  public void queryTest52() throws Fault {
    boolean pass = false;
    String expectedPKs[];
    List c;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("determine if customer has a NULL relationship");
      c = getEntityManager().createQuery(
          "select Distinct Object(c) from Customer c, in(c.aliases) a where c.name = :cName AND a.customerNoop IS NULL")
          .setParameter("cName", "Shelly D. McGowan").getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "3";
      if (!checkEntityPK(c, expectedPKs)) {
        TestUtil
            .logErr("Did not get expected results.  Expected 1 reference, got: "
                + c.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest52 failed");
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
    List c;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("determine if customer has a NULL relationship");
      c = getEntityManager().createQuery(
          "select distinct object(c) fRoM Customer c, IN(c.aliases) a where c.name = :cName OR a.customerNoop IS NULL")
          .setParameter("cName", "Arthur D. Frechette").getResultList();

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

      if (!checkEntityPK(c, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 13 references, got: "
                + c.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest53 failed");
  }

  /*
   * @testName: queryTest54
   * 
   * @assertion_ids: PERSISTENCE:SPEC:426
   * 
   * @test_Strategy: Define a query using Boolean operator NOT in a conditional
   * test (NOT True = False) where the relationship is NULL. Verify the results
   * were accurately returned.
   */
  @SetupMethod(name = "setupCustomerData")
  public void queryTest54() throws Fault {
    boolean pass = false;
    String expectedPKs[];
    List c;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("determine if customers have a NULL relationship");
      c = getEntityManager().createQuery(
          "SELECT DISTINCT Object(c) from Customer c, in(c.aliases) a where NOT a.customerNoop IS NULL")
          .getResultList();

      if (c.size() != 0) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 0 references, got: "
                + c.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest54 failed");
  }

  /*
   * @testName: queryTest55
   * 
   * @assertion_ids: PERSISTENCE:SPEC:358
   * 
   * @test_Strategy: The LIKE expression uses an input parameter for the
   * condition. Verify the results were accurately returned.
   *
   */
  @SetupMethod(name = "setupPhoneData")
  public void queryTest55() throws Fault {
    boolean pass = false;
    String expectedPKs[];
    List c;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace(
          "determine which customers have an area code beginning with 9");
      c = getEntityManager().createQuery(
          "SELECT Distinct Object(c) From Customer c, IN(c.home.phones) p where p.area LIKE :area")
          .setParameter("area", "9%").getResultList();

      expectedPKs = new String[3];
      expectedPKs[0] = "3";
      expectedPKs[1] = "12";
      expectedPKs[2] = "16";

      if (!checkEntityPK(c, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 3 references, got: "
                + c.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest55 failed");
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
    List c;
    String[] expectedZips = new String[] { "00252", "00252", "00252", "00252",
        "00252", "00252", "00252", "00252", "00252", "00252", "00252", "00252",
        "00252", "00252", "00252", "00252", "11345" };

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find all work zip codes");
      c = getEntityManager().createQuery("Select c.work.zip from Customer c")
          .getResultList();

      if (c.size() != 18) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 18 references, got: "
                + c.size());
      } else {
        Iterator i = c.iterator();
        int numOfNull = 0;
        int foundZip = 0;
        while (i.hasNext()) {
          pass1 = true;
          TestUtil.logTrace("Check contents of List for null");
          Object o = i.next();
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
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass1 || !pass2)
      throw new Fault("queryTest56 failed");
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

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find home zip codes");
      s = getEntityManager().createQuery(
          "Select c.name from Customer c where c.home.street = '212 Edgewood Drive'")
          .getSingleResult();

      if (s != null) {
        TestUtil.logErr("Did not get expected results.  Expected null.");
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    if (!pass)
      throw new Fault("queryTest58 failed");
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
    List c;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("determine which customers have an null name");
      c = getEntityManager()
          .createQuery(
              "Select Distinct Object(c) from Customer c where c.name is null")
          .getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "12";

      if (!checkEntityPK(c, expectedPKs)) {
        TestUtil
            .logErr("Did not get expected results.  Expected 1 reference, got: "
                + c.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest59 failed");
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
    String expectedPKs[];
    List c;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find Customers with an Order");
      c = getEntityManager()
          .createQuery("SELECT DISTINCT c FROM Customer c, IN(c.orders) o")
          .getResultList();

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

      if (!checkEntityPK(c, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 18 references, got: "
                + c.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest60 failed");
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
    List c;
    try {
      getEntityTransaction().begin();
      c = getEntityManager()
          .createQuery("select Distinct c FROM Customer c LEFT OUTER JOIN "
              + "c.work workAddress where workAddress.zip IS NULL")
          .getResultList();

      expectedPKs = new String[3];
      expectedPKs[0] = "13";
      expectedPKs[1] = "19";
      expectedPKs[2] = "20";

      if (!checkEntityPK(c, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 3 references, got: "
                + c.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest61 failed");
  }

  /*
   *
   * @testName: queryTest62
   * 
   * @assertion_ids: PERSISTENCE:SPEC:369.8
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
    List c;

    try {
      getEntityTransaction().begin();

      c = getEntityManager()
          .createQuery(
              "SELECT DISTINCT c FROM Customer c WHERE SIZE(c.orders) >= 2")
          .getResultList();

      expectedPKs = new String[2];
      expectedPKs[0] = "4";
      expectedPKs[1] = "14";

      if (!checkEntityPK(c, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 2 references, got: "
                + c.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest62 failed");
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
    List c;

    try {
      getEntityTransaction().begin();

      c = getEntityManager()
          .createQuery(
              "SELECT DISTINCT c FROM Customer c WHERE SIZE(c.orders) > 100")
          .getResultList();

      expectedPKs = new String[0];

      if (!checkEntityPK(c, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 0 references, got: "
                + c.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest63 failed");
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
    List c;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      c = getEntityManager()
          .createQuery(
              "SELECT NEW com.sun.ts.tests.jpa.common.schema30.Customer "
                  + "(c.id, c.name, c.country, c.work) FROM Customer c where "
                  + " c.work.city = :workcity")
          .setParameter("workcity", "Burlington").getResultList();

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

      if (!checkEntityPK(c, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 18 references, got: "
                + c.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("queryTest64 failed");

  }

  /*
   * @testName: queryTest65
   * 
   * @assertion_ids: PERSISTENCE:SPEC:381; PERSISTENCE:SPEC:406;
   * PERSISTENCE:SPEC:825; PERSISTENCE:SPEC:822; PERSISTENCE:SPEC:1674
   * 
   * @test_Strategy: Execute a query which contains the aggregate function MIN.
   * Verify the results are accurately returned.
   *
   */
  @SetupMethod(name = "setupOrderData")
  public void queryTest65() throws Fault {
    boolean pass1, pass2;
    pass1 = pass2 = false;
    final Integer i1 = 1;
    Query q, q1;
    List<Integer> i2;
    List<Integer> i3;

    try {
      TestUtil.logTrace(
          "select MINIMUM number of lineItem quantities available an order may have");
      q = getEntityManager()
          .createQuery("SELECT DISTINCT MIN(l.quantity) FROM LineItem l");
      i2 = q.getResultList();

      q1 = getEntityManager()
          .createQuery("SELECT MIN(l.quantity) FROM LineItem l");
      i3 = q1.getResultList();

      TestUtil.logMsg("Verify select WITH DISTINCT keyword");
      if (i2.size() == 1) {
        Integer result = i2.get(0);
        if (result != null) {
          if (result.equals(i1)) {
            TestUtil.logTrace("Received expected results:" + result);
            pass1 = true;
          } else {
            TestUtil.logTrace("Expected: " + i1 + ", actual:" + result);
          }
        } else {
          TestUtil.logErr("Receive null result from query");
        }
      } else {
        TestUtil.logErr("Receive more than one result:");
        for (Integer i : i2) {
          TestUtil.logErr("Received:" + i);
        }
      }
      TestUtil.logMsg("Verify Select WITHOUT DISTINCT keyword");
      if (i3.size() == 1) {
        Integer result = i3.get(0);
        if (result != null) {
          if (result.equals(i1)) {
            TestUtil.logTrace("Received expected results:" + result);
            pass2 = true;
          } else {
            TestUtil.logTrace("Expected: " + i1 + ", actual:" + result);
          }
        } else {
          TestUtil.logErr("Receive null result from query");
        }
      } else {
        TestUtil.logErr("Receive more than one result:");
        for (Integer i : i3) {
          TestUtil.logErr("Received:" + i);
        }
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    if (!pass1 || !pass2)
      throw new Fault("queryTest65 failed");
  }

  /*
   * @testName: queryTest66
   * 
   * @assertion_ids: PERSISTENCE:SPEC:382; PERSISTENCE:SPEC:406;
   * PERSISTENCE:SPEC:825; PERSISTENCE:SPEC:822; PERSISTENCE:SPEC:1674
   * 
   * @test_Strategy: Execute a query which contains the aggregate function MAX.
   * Verify the results are accurately returned.
   *
   */
  @SetupMethod(name = "setupOrderData")
  public void queryTest66() throws Fault {
    boolean pass1, pass2;
    pass1 = pass2 = false;
    final Integer i1 = 8;
    Query q, q1;
    List<Integer> i2;
    List<Integer> i3;

    try {
      TestUtil.logTrace(
          "find MAXIMUM number of lineItem quantities available an order may have");
      q = getEntityManager()
          .createQuery("SELECT DISTINCT MAX(l.quantity) FROM LineItem l");
      i2 = q.getResultList();

      q1 = getEntityManager()
          .createQuery("SELECT MAX(l.quantity) FROM LineItem l");
      i3 = q1.getResultList();

      TestUtil.logMsg("Testing select WITH DISTINCT keyword");
      if (i2.size() == 1) {
        Integer result = i2.get(0);
        if (result != null) {
          if (result.equals(i1)) {
            TestUtil.logTrace("Received expected results:" + result);
            pass1 = true;
          } else {
            TestUtil.logTrace("Expected: " + i1 + ", actual:" + result);
          }
        } else {
          TestUtil.logErr("Receive null result from query");
        }
      } else {
        TestUtil.logErr("Receive more than one result:");
        for (Integer i : i2) {
          TestUtil.logErr("Received:" + i);
        }
      }
      TestUtil.logMsg("Testing Select WITHOUT DISTINCT keyword");
      if (i3.size() == 1) {
        Integer result = i3.get(0);
        if (result != null) {
          if (result.equals(i1)) {
            TestUtil.logTrace("Received expected results:" + result);
            pass2 = true;
          } else {
            TestUtil.logTrace("Expected: " + i1 + ", actual:" + result);
          }
        } else {
          TestUtil.logErr("Receive null result from query");
        }
      } else {
        TestUtil.logErr("Receive more than one result:");
        for (Integer i : i3) {
          TestUtil.logErr("Received:" + i);
        }
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    if (!pass1 || !pass2)
      throw new Fault("queryTest66 failed");
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
    Double d1 = 1487.29;
    Double d2 = 1487.30;
    Double d3;
    Query q;

    try {
      TestUtil.logTrace("find AVERAGE price of all orders");
      q = getEntityManager()
          .createQuery("SELECT AVG(o.totalPrice) FROM Order o");

      d3 = (Double) q.getSingleResult();

      if (((d3 >= d1) && (d3 < d2))) {
        TestUtil.logTrace("Returned expected results: " + d1);
        pass = true;
      } else {
        TestUtil.logTrace("Returned " + d3 + "expected: " + d1);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    if (!pass)
      throw new Fault("queryTest67 failed");
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
    Query q;

    try {
      TestUtil.logTrace("find SUM of all product prices");
      q = getEntityManager().createQuery("SELECT Sum(p.price) FROM Product p");

      d3 = (Double) q.getSingleResult();

      if (((d3 >= d1) && (d3 < d2))) {
        TestUtil.logTrace("Returned expected results: " + d1);
        pass = true;
      } else {
        TestUtil.logTrace("Returned " + d3 + "expected: " + d1);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    if (!pass)
      throw new Fault("queryTest68 failed");
  }

  /*
   * @testName: queryTest69
   * 
   * @assertion_ids: PERSISTENCE:SPEC:384; PERSISTENCE:SPEC:389;
   * PERSISTENCE:SPEC:406; PERSISTENCE:SPEC:824; PERSISTENCE:SPEC:392;
   * PERSISTENCE:SPEC:393;
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
    Query q1;
    Query q2;
    final Long expectedResult1 = 17L;
    final Long expectedResult2 = 16L;

    try {
      TestUtil.logTrace(
          "Execute two queries composed differently and verify results");

      q1 = getEntityManager()
          .createQuery("Select Count(c.home.city) from Customer c");
      Long result1 = (Long) q1.getSingleResult();

      if (!(result1.equals(expectedResult1))) {
        TestUtil.logErr("Query1 in queryTest69 returned:" + result1
            + " expected: " + expectedResult1);
      } else {
        TestUtil
            .logTrace("pass:  Query1 in queryTest69 returned expected results");
        pass1 = true;
      }

      q2 = getEntityManager()
          .createQuery("Select Count(Distinct c.home.city) from Customer c");

      Long result2 = (Long) q2.getSingleResult();

      if (!(result2.equals(expectedResult2))) {
        TestUtil.logErr("Query 2 in queryTest69 returned:" + result2
            + " expected: " + expectedResult2);
      } else {
        TestUtil.logTrace(
            "pass:  Query 2 in queryTest69 returned expected results");
        pass2 = true;
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass1 || !pass2)
      throw new Fault("queryTest69 failed");

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
    final Long expectedValue = 3277L;
    Long result;
    Query q;

    try {
      TestUtil.logTrace("find SUM of all product prices");
      q = getEntityManager()
          .createQuery("SELECT Sum(p.quantity) FROM Product p");

      result = (Long) q.getSingleResult();

      if (expectedValue.equals(result)) {
        TestUtil.logTrace("Returned expected results: " + result);
        pass = true;
      } else {
        TestUtil.logTrace("Returned " + result + "expected: " + expectedValue);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception queryTest70: ", e);
    }
    if (!pass)
      throw new Fault("queryTest70 failed");
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

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("Check if a spouse is related to a customer");
      getEntityManager()
          .createQuery("Select s.customer from Spouse s where s.id = '7'")
          .getSingleResult();

      getEntityTransaction().commit();
    } catch (NoResultException e) {
      TestUtil
          .logTrace("queryTest71: NoResultException caught as expected : " + e);
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception caught in queryTest71: " + e);
    }

    if (!pass)
      throw new Fault("queryTest71 failed");
  }

  /*
   * @testName: test_leftouterjoin_1xM
   ** 
   * @assertion_ids: PERSISTENCE:SPEC:780
   * 
   * @test_Strategy: LEFT OUTER JOIN for 1-M relationship. Retrieve credit card
   * information for a customer with name like Caruso.
   *
   */
  @SetupMethod(name = "setupCustomerData")
  public void test_leftouterjoin_1xM() throws Fault {
    List result;
    boolean pass = false;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "SELECT DISTINCT c from Customer c LEFT OUTER JOIN c.creditCards cc where c.name LIKE '%Caruso'")
          .getResultList();

      expectedPKs = new String[2];
      expectedPKs[0] = "7";
      expectedPKs[1] = "8";

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
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault("test_leftouterjoin_1x1 failed");
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
    List q;
    boolean pass1 = false;
    boolean pass2 = true;
    Object[][] expectedResultSet = new Object[][] { new Object[] { "15", "14" },
        new Object[] { "16", "14" } };

    try {
      getEntityTransaction().begin();
      q = getEntityManager().createQuery(
          "SELECT o.id, cust.id from Order o LEFT OUTER JOIN o.customer cust"
              + " where cust.name=?1 ORDER BY o.id")
          .setParameter(1, "Kellie A. Sanborn").getResultList();

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
          Object[] orderAndCustomer;
          if (obj instanceof Object[]) {
            TestUtil.logTrace(
                "The element in the result list is of type Object[], continue . . .");
            orderAndCustomer = (Object[]) obj;
            if (!Arrays.equals(orderAndCustomerExpected, orderAndCustomer)) {
              TestUtil.logErr("Expecting element value: "
                  + Arrays.asList(orderAndCustomerExpected)
                  + ", actual element value: "
                  + Arrays.asList(orderAndCustomer));
              pass2 = false;
              break;
            }
          } else {
            pass2 = false;
            TestUtil.logErr(
                "The element in the result list is not of type Object[]:"
                    + obj);
            break;
          }
        }
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass1 || !pass2)
      throw new Fault("test_leftouterjoin_Mx1 failed");
  }

  /*
   * @testName: test_leftouterjoin_MxM
   * 
   * @assertion_ids: PERSISTENCE:SPEC:780; PERSISTENCE:SPEC:317;
   * PERSISTENCE:SPEC:317.3; PERSISTENCE:SPEC:320; PERSISTENCE:SPEC:811
   * 
   * @test_Strategy: Left Outer Join for M-M relationship. Retrieve all aliases
   * where customer name like Ste.
   *
   */
  @SetupMethod(name = "setupAliasData")
  public void test_leftouterjoin_MxM() throws Fault {

    List q;
    boolean pass1 = false;
    boolean pass2 = true;
    Object[][] expectedResultSet = new Object[][] { new Object[] { "7", "sjc" },
        new Object[] { "5", "ssd" }, new Object[] { "7", "stevec" },
        new Object[] { "5", "steved" }, new Object[] { "5", "stevie" },
        new Object[] { "7", "stevie" } };
    try {
      getEntityTransaction().begin();
      q = getEntityManager().createQuery(
          "SELECT c.id, a.alias from Customer c LEFT OUTER JOIN c.aliases a "
              + "where c.name LIKE 'Ste%' ORDER BY a.alias, c.id")
          .getResultList();

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
          Object[] customerAndAlias;
          if (obj instanceof Object[]) {
            TestUtil.logTrace(
                "The element in the result list is of type Object[], continue . . .");
            customerAndAlias = (Object[]) obj;
            if (!Arrays.equals(customerAndAliasExpected, customerAndAlias)) {
              TestUtil.logErr("Expecting element value: "
                  + Arrays.asList(customerAndAliasExpected)
                  + ", actual element value: "
                  + Arrays.asList(customerAndAlias));
              pass2 = false;
              break;
            }
          } else {
            pass2 = false;
            TestUtil.logErr(
                "The element in the result list is not of type Object[]:"
                    + obj);
            break;
          }
        }
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass1 || !pass2)
      throw new Fault("test_leftouterjoin_MxM failed");
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

    List result;
    boolean pass = false;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "select Object(c) FROM Customer c JOIN c.aliases a where UPPER(a.alias)='SJC' ")
          .getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "7";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 1 references, got: "
                + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }

    } catch (Exception e) {
      TestUtil.logErr(
          "Unexpected exception caught exception in test_upperStringExpression: ",
          e);
    }

    if (!pass)
      throw new Fault("test_upperStringExpression failed");
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

    List result;
    boolean pass = false;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "select Object(c) FROM Customer c JOIN c.aliases a where LOWER(a.alias)='sjc' ")
          .getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "7";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 1 references, got: "
                + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }

    } catch (Exception e) {
      TestUtil.logErr(
          "Unexpected exception caught exception in test_lowerStringExpression: ",
          e);
    }

    if (!pass)
      throw new Fault("test_lowerStringExpression failed");
  }

  /*
   * @testName: test_groupBy
   * 
   * @assertion_ids: PERSISTENCE:SPEC:810; PERSISTENCE:SPEC:756;
   * PERSISTENCE:SPEC:1623;
   * 
   * @test_Strategy: Test for Only Group By in a simple select statement.
   * Country is an Embeddable entity.
   *
   */
  @SetupMethod(name = "setupCustomerData")
  public void test_groupBy() throws Fault {
    boolean pass = false;
    List result;
    String expected[] = new String[] { "CHA", "GBR", "IRE", "JPN", "USA" };

    try {
      getEntityTransaction().begin();
      result = getEntityManager()
          .createQuery(
              "select c.country.code FROM Customer c GROUP BY c.country.code")
          .getResultList();

      String[] output = (String[]) (result.toArray(new String[result.size()]));
      Arrays.sort(output);

      pass = Arrays.equals(expected, output);

      if (!pass) {
        TestUtil.logErr("Did not get expected results");
        for (String s : expected) {
          TestUtil.logErr("Expected:" + s);
        }
        for (String s : output) {
          TestUtil.logErr("Actual:" + s);
        }
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault("test_groupBy failed");
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
    List result;
    String expected[] = new String[] { "AXP", "MCARD", "VISA" };

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "select cc.type FROM CreditCard cc JOIN cc.customer cust GROUP BY cc.type")
          .getResultList();

      String[] output = (String[]) (result.toArray(new String[result.size()]));
      Arrays.sort(output);

      pass = Arrays.equals(expected, output);

      if (!pass) {
        TestUtil.logErr("Did not get expected results");
        for (String s : expected) {
          TestUtil.logErr("Expected:" + s);
        }
        for (String s : output) {
          TestUtil.logErr("Actual:" + s);
        }
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault("test_groupBy_1 failed");
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
  public void test_innerjoin_1x1() throws Fault {
    List result;
    boolean pass = false;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();

      result = getEntityManager()
          .createQuery("SELECT c from Customer c INNER JOIN c.spouse s")
          .getResultList();

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
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault("test_innerjoin_1x1 failed");
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
    List result;
    boolean pass = false;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();

      result = getEntityManager().createQuery(
          "SELECT DISTINCT object(c) from Customer c INNER JOIN c.creditCards cc where cc.type='VISA' ")
          .getResultList();

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
        TestUtil.logErr(
            "Did not get expected results.  Expected 8 references, got: "
                + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault("test_innerjoin_1xM failed");
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
    List result;
    boolean pass = false;
    String expectedPKs[];
    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "SELECT Object(o) from Order o INNER JOIN o.customer cust where cust.name = ?1")
          .setParameter(1, "Kellie A. Sanborn").getResultList();

      expectedPKs = new String[2];
      expectedPKs[0] = "15";
      expectedPKs[1] = "16";

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
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault("test_innerjoin_Mx1 failed");
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
    List result;
    boolean pass = false;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "SELECT Object(c) from Customer c INNER JOIN c.aliases a where a.alias = :aName ")
          .setParameter("aName", "fish").getResultList();

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
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault("test_innerjoin_MxM failed");
  }

  /*
   * @testName: test_fetchjoin_1x1
   * 
   * @assertion_ids: PERSISTENCE:SPEC:781; PERSISTENCE:SPEC:774;
   * PERSISTENCE:SPEC:776
   * 
   * @test_Strategy: JOIN FETCH for 1-1 relationship. Prefetch the spouses for
   * all Customers.
   */
  @SetupMethod(name = "setupCustomerData")
  public void test_fetchjoin_1x1() throws Fault {
    List result;
    boolean pass = false;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      result = getEntityManager()
          .createQuery("SELECT c from Customer c JOIN FETCH c.spouse ")
          .getResultList();

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
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault("test_fetchjoin_1x1 failed");
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
  @SetupMethod(name = "setupOrderData")
  public void test_fetchjoin_1xM() throws Fault {
    List result;
    boolean pass = false;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "SELECT DISTINCT c from Customer c LEFT JOIN FETCH c.orders where c.home.state IN('NY','RI')")
          .getResultList();

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
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault("test_fetchjoin_1xM failed");
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
    List result;
    boolean pass = false;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "select o from Order o LEFT JOIN FETCH o.customer where o.customer.home.city='Lawrence'")
          .getResultList();

      expectedPKs = new String[2];
      expectedPKs[0] = "15";
      expectedPKs[1] = "16";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 8 references, got: "
                + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault("test_fetchjoin_Mx1 failed");
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
    List result;
    boolean pass = false;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "select Object(o) from Order o LEFT JOIN FETCH o.customer where o.customer.name LIKE '%Caruso' ")
          .getResultList();

      expectedPKs = new String[2];
      expectedPKs[0] = "7";
      expectedPKs[1] = "8";

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
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault("test_fetchjoin_Mx1_1 failed");
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
    List result;
    boolean pass = false;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("FETCHJOIN-MXM Executing Query");
      result = getEntityManager().createQuery(
          "SELECT DISTINCT a from Alias a LEFT JOIN FETCH a.customers where a.alias LIKE 'a%' ")
          .getResultList();

      expectedPKs = new String[4];
      expectedPKs[0] = "1";
      expectedPKs[1] = "2";
      expectedPKs[2] = "5";
      expectedPKs[3] = "6";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 4 references, got: "
                + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault("test_fetchjoin_MxM failed");
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
    List result;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      Date date1 = getSQLDate(2000, 2, 14);
      Date date6 = getSQLDate(2005, 2, 18);
      TestUtil.logTrace("The dates used in test_betweenDates is : " + date1
          + " and " + date6);
      result = getEntityManager().createQuery(
          "SELECT DISTINCT p From Product p where p.shelfLife.soldDate BETWEEN :date1 AND :date6")
          .setParameter("date1", date1).setParameter("date6", date6)
          .getResultList();

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
      TestUtil.logErr("Caught exception:", e);
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
    List result;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "SELECT DISTINCT o From Order o where o.totalPrice NOT BETWEEN 1000 AND 1200")
          .getResultList();

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
        TestUtil.logErr(
            "Did not get expected results.  Expected 15 references, got: "
                + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault("test_notBetweenArithmetic failed");
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
    List result;
    String expectedPKs[];
    final Date date1 = getSQLDate("2000-02-14");
    final Date newdate = getSQLDate("2005-02-17");
    TestUtil.logTrace("The dates used in test_betweenDates is : " + date1
        + " and " + newdate);

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "SELECT DISTINCT p From Product p where p.shelfLife.soldDate NOT BETWEEN :date1 AND :newdate")
          .setParameter("date1", date1).setParameter("newdate", newdate)
          .getResultList();

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
      TestUtil.logErr("Caught exception:", e);
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
    List result;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "select Object(o) FROM Order AS o WHERE o.customer.name = 'Karen R. Tegan' AND o.totalPrice > 500")
          .getResultList();

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
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault("test_ANDconditionTT failed");
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
    List result;

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "select Object(o) FROM Order AS o WHERE o.customer.name = 'Karen R. Tegan' AND o.totalPrice > 10000")
          .getResultList();

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
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault("test_ANDconditionTF failed");
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
    List result;

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "select Object(o) FROM Order AS o WHERE o.customer.id = '1001' AND o.totalPrice < 1000 ")
          .getResultList();

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
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault("test_ANDconditionFT failed");
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
    List result;

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "select Object(o) FROM Order AS o WHERE o.customer.id = '1001' AND o.totalPrice > 10000")
          .getResultList();

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
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault("test_ANDconditionFF failed");
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
    List result;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "select DISTINCT o FROM Order AS o WHERE o.customer.name = 'Karen R. Tegan' OR o.totalPrice > 5000")
          .getResultList();

      expectedPKs = new String[3];
      expectedPKs[0] = "6";
      expectedPKs[1] = "11";
      expectedPKs[2] = "16";

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
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault("test_ORconditionTT failed");
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
    List result;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "select Object(o) FROM Order AS o WHERE o.customer.name = 'Karen R. Tegan' OR o.totalPrice > 10000")
          .getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "6";

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
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault("test_ORconditionTF failed");
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
    List result;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "select Distinct Object(o) FROM Order AS o WHERE o.customer.id = '1001' OR o.totalPrice < 1000 ")
          .getResultList();

      expectedPKs = new String[7];
      expectedPKs[0] = "9";
      expectedPKs[1] = "10";
      expectedPKs[2] = "12";
      expectedPKs[3] = "13";
      expectedPKs[4] = "15";
      expectedPKs[5] = "19";
      expectedPKs[6] = "20";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 7 references, got: "
                + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault("test_ORconditionFT failed");
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
    List result;

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "select Object(o) FROM Order AS o WHERE o.customer.id = '1001' OR o.totalPrice > 10000")
          .getResultList();

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
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault("test_ORconditionFF failed");
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
    List result;
    final String[] expected = new String[] { "Jonathan K. Smith",
        "Kellie A. Sanborn", "Robert E. Bissett" };

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "select c.name FROM Customer c JOIN c.orders o WHERE o.totalPrice BETWEEN 90 AND 160 GROUP BY c.name")
          .getResultList();

      String[] output = (String[]) (result.toArray(new String[result.size()]));

      Arrays.sort(output);
      pass = Arrays.equals(expected, output);

      if (!pass) {
        TestUtil.logErr("Did not get expected results");
        for (String s : expected) {
          TestUtil.logErr("Expected:" + s);
        }
        for (String s : output) {
          TestUtil.logErr("Actual:" + s);
        }
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault("test_groupByWhereClause failed");
  }

  /*
   * @testName: test_groupByHaving
   * 
   * @assertion_ids: PERSISTENCE:SPEC:808; PERSISTENCE:SPEC:353;
   * PERSISTENCE:SPEC:757; PERSISTENCE:SPEC:391; PERSISTENCE:SPEC:786;
   * PERSISTENCE:SPEC:1595; PERSISTENCE:SPEC:1624;
   * 
   * @test_Strategy: Test for Group By and Having in a select statement Select
   * the count of customers in each country where Country is China, England
   */
  @SetupMethod(name = "setupCustomerData")
  public void test_groupByHaving() throws Fault {
    boolean pass = false;
    List result;
    final Long expectedGBR = 2L;
    final Long expectedCHA = 4L;

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "select Count(c) FROM Customer c GROUP BY c.country.code "
              + "HAVING c.country.code IN ('GBR', 'CHA') ")
          .getResultList();

      Iterator i = result.iterator();
      int numOfExpected = 0;
      while (i.hasNext()) {
        TestUtil.logTrace("Check result received . . . ");
        Long l = (Long) i.next();
        if ((l.equals(expectedGBR)) || (l.equals(expectedCHA))) {
          numOfExpected++;
        }
      }

      if (numOfExpected != 2) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 2 Values returned : "
                + "2 with Country Code GBR and 4 with Country Code CHA. "
                + "Received: " + result.size());
        Iterator it = result.iterator();
        while (it.hasNext()) {
          TestUtil.logTrace("count of Codes Returned: " + it.next());
        }
      } else {
        TestUtil.logTrace("Expected results received.");
        pass = true;
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception:", e);

    }

    if (!pass)
      throw new Fault("test_groupByHaving failed");
  }

  /*
   * @testName: test_substringHavingClause
   * 
   * @assertion_ids: PERSISTENCE:SPEC:807
   * 
   * @test_Strategy:Test for Functional Expression: substring in Having Clause
   * Select all customers with alias = fish
   */
  @SetupMethod(name = "setupAliasData")
  public void test_substringHavingClause() throws Fault {
    boolean pass = false;
    Query q;
    Object result;
    final Long expectedCount = 2L;

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("Executing Query");
      q = getEntityManager().createQuery(
          "select count(c) FROM Customer c JOIN c.aliases a GROUP BY a.alias "
              + "HAVING a.alias = SUBSTRING(:string1, :int1, :int2)")
          .setParameter("string1", "fish").setParameter("int1", 1)
          .setParameter("int2", 4);

      result = (Long) q.getSingleResult();

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
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault(" test_substringHavingClause failed");
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
    Query q;
    String result;
    final String expectedCustomer = "Margaret Mills";

    try {
      getEntityTransaction().begin();
      q = getEntityManager().createQuery(
          "select c.name FROM Customer c Group By c.name HAVING c.name = concat(:fmname, :lname) ")
          .setParameter("fmname", "Margaret ").setParameter("lname", "Mills");
      result = (String) q.getSingleResult();

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
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault("test_concatHavingClause failed");
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
    boolean pass = false;
    List result;
    final Long expectedCount = 2L;

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "select count(c.country.code) FROM Customer c GROUP BY c.country.code "
              + " HAVING LOWER(c.country.code) = 'gbr' ")
          .getResultList();

      Iterator it = result.iterator();
      while (it.hasNext()) {
        Long l = (Long) it.next();
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
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault(" test_lowerHavingClause failed");
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
    boolean pass = false;
    List result;
    final Long expectedCount = 2L;

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "select count(c.country.country) FROM Customer c GROUP BY c.country.country "
              + "HAVING UPPER(c.country.country) = 'ENGLAND' ")
          .getResultList();

      Iterator it = result.iterator();
      while (it.hasNext()) {
        Long l = (Long) it.next();
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
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault("test_upperHavingClause failed");
  }

  /*
   * @testName: test_lengthHavingClause
   * 
   * @assertion_ids: PERSISTENCE:SPEC:807; PERSISTENCE:SPEC:369.4;
   * PERSISTENCE:SPEC:1626;
   * 
   * @test_Strategy:Test for Functional Expression: length in Having Clause
   * Select all customer names having the length of the city of the home address
   * = 10
   */
  @SetupMethod(name = "setupCustomerData")
  public void test_lengthHavingClause() throws Fault {
    boolean pass = false;
    List result;
    final String[] expected = new String[] { "Burlington", "Chelmsford",
        "Roslindale" };

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "select a.city  FROM Customer c JOIN c.home a GROUP BY a.city HAVING LENGTH(a.city) = 10 ")
          .getResultList();

      String[] output = (String[]) (result.toArray(new String[result.size()]));
      Arrays.sort(output);

      pass = Arrays.equals(expected, output);

      if (!pass) {
        TestUtil.logErr("Did not get expected result:");
        for (String s : expected) {
          TestUtil.logTrace("expected:" + s);
        }
        for (String s : output) {
          TestUtil.logTrace("actual:" + s);
        }

      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault("test_lengthHavingClause failed");
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
    List result;
    final String[] expected = new String[] { "Alan E. Frechette",
        "Arthur D. Frechette" };

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "select c.name FROM Customer c GROUP BY c.name HAVING LOCATE('Frechette', c.name) > 0 ")
          .getResultList();

      String[] output = (String[]) (result.toArray(new String[result.size()]));
      Arrays.sort(output);

      pass = Arrays.equals(expected, output);

      if (!pass) {
        TestUtil.logErr("Did not get expected result:");
        for (String s : expected) {
          TestUtil.logTrace("expected:" + s);
        }
        for (String s : output) {
          TestUtil.logTrace("actual:" + s);
        }
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault(" test_locateHavingClause failed");
  }

  /*
   * testName: test_trimHavingClause_01 assertion_ids: PERSISTENCE:SPEC:369.9
   * test_Strategy: Test for TRIM BOTH characters (blank) in the Having Clause
   *
   * DISABLE THIS TEST FOR NOW
   * 
   * @SetupMethod(name = "setupTrimData") public void test_trimHavingClause_01()
   * throws Fault { boolean pass = false; String result; Query q; final String
   * expected = " David R. Vincent             "; final String expected2 =
   * "'David R. Vincent'";
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
   * String stmt = "select t.name  FROM Trim t Group by t.name HAVING " +
   * "trim(BOTH from t.name) = " + expected2; TestUtil.logTrace("sql query:" +
   * stmt); q = getEntityManager().createQuery(stmt);
   * 
   * result = (String) q.getSingleResult();
   * 
   * if (expected.equals(result)) { pass = true;
   * TestUtil.logTrace("Expected results received"); pass = true; } else {
   * TestUtil.logErr("Did not get expected results," + "Expected:|" + expected +
   * "|, got: |" + result + "|"); }
   * 
   * getEntityTransaction().commit(); } catch (Exception e) {
   * TestUtil.logErr("Caught exception:", e); }
   * 
   * if (!pass) throw new Fault(" test_trimHavingClause_01 failed"); }
   * 
   */

  /*
   * testName: test_trimHavingClause_02 assertion_ids: PERSISTENCE:SPEC:369.9
   * test_Strategy: Test for TRIM LEADING characters (blank) in the Having
   * Clause
   * 
   * DISABLE THIS TEST FOR NOW
   * 
   * 
   * @SetupMethod(name = "setupTrimData") public void test_trimHavingClause_02()
   * throws Fault { boolean pass = false; Query q; String result; final String
   * expected = " David R. Vincent             "; final String expected2 =
   * "'David R. Vincent             '";
   * 
   * 
   * try { getEntityTransaction().begin();
   * 
   * Trim tTrim = getEntityManager().find(Trim.class, "19");
   * TestUtil.logTrace("Trim(19):" + tTrim.toString()); if
   * (!tTrim.getName().equals(expected)) {
   * TestUtil.logErr("Name returned by find does not match expected");
   * TestUtil.logErr("Expected:|" + expected + "|, actual:|" + tTrim.getName() +
   * "|"); } String stmt = "select t.name  FROM Trim t Group by t.name HAVING "
   * + "trim(LEADING from t.name) = " + expected2;
   * TestUtil.logTrace("sql query:" + stmt); q =
   * getEntityManager().createQuery(stmt);
   * 
   * result = (String) q.getSingleResult();
   * 
   * if (expected.equals(result)) { pass = true;
   * TestUtil.logTrace("Expected results received"); pass = true; } else {
   * TestUtil.logErr("Did not get expected results," + "Expected:|" + expected +
   * "|, got: |" + result + "|"); }
   * 
   * getEntityTransaction().commit(); } catch (Exception e) {
   * TestUtil.logErr("Caught exception:", e); }
   * 
   * if (!pass) throw new Fault("test_trimHavingClause_02 failed"); }
   */

  /*
   * testName: test_trimHavingClause_03 assertion_ids: PERSISTENCE:SPEC:369.9
   * test_Strategy: Test for TRIM TRAILING characters (blank) in the Having
   * Clause
   * 
   * DISABLE THIS TEST FOR NOW
   * 
   * 
   * @SetupMethod(name = "setupTrimData") public void test_trimHavingClause_03()
   * throws Fault {
   * 
   * boolean pass = false; String result; Query q;
   * 
   * final String expected = " David R. Vincent             "; final String
   * expected2 = "' David R. Vincent'";
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
   * String stmt = "select t.name  FROM Trim t Group by t.name HAVING " +
   * "trim(TRAILING from t.name) = " + expected2; TestUtil.logTrace("sql query:"
   * + stmt); q = getEntityManager().createQuery(stmt);
   * 
   * result = (String) q.getSingleResult();
   * 
   * if (expected.equals(result)) { pass = true;
   * TestUtil.logTrace("Expected results received"); pass = true; } else {
   * TestUtil.logErr("Did not get expected results," + "Expected:|" + expected +
   * "|, got: |" + result + "|"); }
   * 
   * getEntityTransaction().commit(); } catch (Exception e) {
   * TestUtil.logErr("Caught exception:", e); }
   * 
   * if (!pass) throw new Fault("test_trimHavingClause_03 failed"); }
   * 
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
    Query q;
    Object result;
    final Double expectedPrice = 10191.90D;

    try {
      getEntityTransaction().begin();
      q = getEntityManager().createQuery(
          "select sum(o.totalPrice) FROM Order o GROUP BY o.totalPrice HAVING ABS(o.totalPrice) = :doubleValue ")
          .setParameter("doubleValue", 5095.95);

      result = (Double) q.getSingleResult();

      if (expectedPrice.equals(result)) {
        pass = true;
        TestUtil.logTrace("Expected results received");
        pass = true;
      } else {
        TestUtil.logErr("test_ABSHavingClause:  Did not get expected results."
            + "Expected 10190, got: " + (Double) result);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault("test_ABSHavingClause failed");
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
    List result;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("SQRT: Executing Query");
      result = getEntityManager().createQuery(
          "select object(o) FROM Order o Where SQRT(o.totalPrice) > :doubleValue ")
          .setParameter("doubleValue", 70D).getResultList();

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
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault(" test_SQRTWhereClause failed");

  }

  /*
   * @testName: test_subquery_exists_01
   * 
   * @assertion_ids: PERSISTENCE:SPEC:791;PERSISTENCE:SPEC:792;
   * PERSISTENCE:SPEC:1599;
   * 
   * @test_Strategy: Test NOT EXISTS in the Where Clause for a correlated query.
   * Select the customers without orders.
   */
  @SetupMethod(name = "setupOrderData")
  public void test_subquery_exists_01() throws Fault {
    boolean pass = false;
    List result;
    final String[] expectedPKs = { "19", "20" };

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "SELECT c FROM Customer c WHERE NOT EXISTS (SELECT o1 FROM c.orders o1) ")
          .getResultList();

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil
            .logErr("test_subquery_exists_01:  Did not get expected results.  "
                + "Expected 2 references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault(" test_subquery_exists_01 failed");
  }

  /*
   * @testName: test_subquery_exists_02
   * 
   * @assertion_ids: PERSISTENCE:SPEC:791;PERSISTENCE:SPEC:792
   * 
   * @test_Strategy: Test for EXISTS in the Where Clause for a correlated query.
   * Select the customers with orders where total order > 1500.
   */
  @SetupMethod(name = "setupOrderData")
  public void test_subquery_exists_02() throws Fault {

    boolean pass = false;
    List result;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "SELECT DISTINCT c FROM Customer c WHERE EXISTS (SELECT o FROM c.orders o where o.totalPrice > 1500 ) ")
          .getResultList();

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
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault(" test_subquery_exists_02 failed");
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
    List result;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      result = getEntityManager()
          .createQuery("Select Object(o) from Order o WHERE EXISTS "
              + "(Select c From o.customer c WHERE c.name LIKE '%Caruso') ")
          .getResultList();

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
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault(" test_subquery_like failed");
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
    List result;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      result = getEntityManager()
          .createQuery("Select DISTINCT c from Customer c WHERE c.home.state IN"
              + "(Select distinct w.state from c.work w where w.state = :state ) ")
          .setParameter("state", "MA").getResultList();

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
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault("test_subquery_in failed");
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
    List result;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("Execute query for test_subquery_between");
      result = getEntityManager().createQuery(
          "SELECT DISTINCT c FROM Customer c WHERE EXISTS (SELECT o FROM c.orders o where o.totalPrice BETWEEN 1000 AND 1200)")
          .getResultList();

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
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault(" test_subquery_between failed");

  }

  /*
   * @testName: test_subquery_join
   * 
   * @assertion_ids: PERSISTENCE:SPEC:800;PERSISTENCE:SPEC:801;
   * PERSISTENCE:SPEC:802; PERSISTENCE:SPEC:765
   * 
   * @test_Strategy: Use JOIN in a sub query. Select the customers whose orders
   * have line items of quantity > 2.
   */
  @SetupMethod(name = "setupOrderData")
  public void test_subquery_join() throws Fault {

    boolean pass = false;
    List result;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "SELECT DISTINCT c FROM Customer c JOIN c.orders o WHERE EXISTS "
              + "(SELECT o FROM o.lineItemsCollection l where l.quantity > 3 ) ")
          .getResultList();

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
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault(" test_subquery_join failed");
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
    List result;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "SELECT DISTINCT c FROM Customer c, IN(c.orders) co WHERE co.totalPrice > "
              + "ALL (Select o.totalPrice FROM Order o, in(o.lineItemsCollection) l WHERE l.quantity > 3) ")
          .getResultList();

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
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault(" test_subquery_ALL_GT failed");
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
    List result;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "SELECT distinct object(C) FROM Customer C, IN(C.orders) co WHERE co.totalPrice < "
              + "ALL (Select o.totalPrice FROM Order o, IN(o.lineItemsCollection) l WHERE l.quantity > 3) ")
          .getResultList();

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
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault("test_subquery_ALL_LT failed");
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
    List result;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "SELECT DISTINCT c FROM Customer c, IN(c.orders) co WHERE co.totalPrice = ALL"
              + " (Select MIN(o.totalPrice) FROM Order o) ")
          .getResultList();

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
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault("test_subquery_ALL_EQ failed");

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
    List result;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "SELECT c FROM Customer c, IN(c.orders) co WHERE co.totalPrice <= ALL"
              + " (Select o.totalPrice FROM Order o, IN(o.lineItemsCollection) l WHERE l.quantity > 3) ")
          .getResultList();

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
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault("test_subquery_ALL_LTEQ failed");
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
    List result;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "SELECT DISTINCT object(c) FROM Customer C, IN(c.orders) co WHERE co.totalPrice >= ALL"
              + " (Select o.totalPrice FROM Order o, IN(o.lineItemsCollection) l WHERE l.quantity >= 3) ")
          .getResultList();

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
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault("test_subquery_ALL_GTEQ failed");
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
    List result;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "SELECT Distinct object(c) FROM Customer c, IN(c.orders) co WHERE co.totalPrice <> "
              + "ALL (Select MIN(o.totalPrice) FROM Order o) ")
          .getResultList();

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
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault("test_subquery_ALL_NOTEQ failed");
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
    List result;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "SELECT DISTINCT c FROM Customer c, IN(c.orders) co WHERE co.totalPrice > ANY"
              + " (Select o.totalPrice FROM Order o, IN(o.lineItemsCollection) l WHERE l.quantity = 3) ")
          .getResultList();

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
      TestUtil.logErr("Caught exception:", e);
    }
    if (!pass)
      throw new Fault("test_subquery_ANY_GT failed");
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
    List result;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "SELECT Distinct Object(c) FROM Customer c, IN(c.orders) co WHERE co.totalPrice < ANY"
              + " (Select o.totalPrice FROM Order o, IN(o.lineItemsCollection) l WHERE l.quantity = 3)")
          .getResultList();

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
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault("test_subquery_ANY_LT failed");
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
    List result;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "SELECT Distinct object(c) FROM Customer c, IN(c.orders) co WHERE co.totalPrice = ANY"
              + " (Select MAX(o.totalPrice) FROM Order o) ")
          .getResultList();

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
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault("test_subquery_ANY_EQ failed");
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
    List result;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "SELECT DISTINCT object(c) FROM Customer c, IN(c.orders) co WHERE co.totalPrice <= SOME"
              + " (Select o.totalPrice FROM Order o, IN(o.lineItemsCollection) l WHERE l.quantity = 3) ")
          .getResultList();

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
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault(" test_subquery_SOME_LTEQ failed");
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
    List result;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "SELECT Distinct object(c) FROM Customer c, IN(c.orders) co WHERE co.totalPrice >= SOME"
              + " (Select o.totalPrice FROM Order o, IN(o.lineItemsCollection) l WHERE l.quantity = 3) ")
          .getResultList();

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
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault("test_subquery_SOME_GTEQ failed");
  }

  /*
   * @testName: fetchStringJoinTypeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:980
   * 
   * @test_Strategy: JOIN FETCH for 1-1 relationship. Prefetch the spouses for
   * all Customers.
   */
  @SetupMethod(name = "setupCustomerData")
  public void fetchStringJoinTypeTest() throws Fault {
    boolean pass = false;
    List result;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      result = getEntityManager()
          .createQuery("SELECT c FROM Customer c INNER JOIN fetch c.spouse")
          .getResultList();

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
      TestUtil.logErr("Caught unexpected exception", e);
    }

    if (!pass) {
      throw new Fault("fetchStringJoinTypeTest failed");
    }
  }

  /*
   * @testName: treatJoinClassTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1678
   * 
   * @test_Strategy:
   *
   */
  @SetupMethod(name = "setupOrderData")
  public void treatJoinClassTest() throws Fault {
    boolean pass = false;
    List<String> actual;
    List<String> expected = new ArrayList<String>();
    expected.add(softwareRef[0].getName());
    expected.add(softwareRef[1].getName());
    expected.add(softwareRef[2].getName());
    expected.add(softwareRef[3].getName());
    expected.add(softwareRef[4].getName());
    expected.add(softwareRef[5].getName());

    try {
      getEntityTransaction().begin();
      actual = getEntityManager().createQuery(
          "SELECT s.name FROM LineItem l JOIN TREAT(l.product AS SoftwareProduct) s")
          .getResultList();

      Collections.sort(actual);
      for (String s : actual) {
        TestUtil.logTrace("result:" + s);

      }
      if (expected.containsAll(actual) && actual.containsAll(expected)
          && expected.size() == actual.size()) {
        TestUtil.logTrace("Received expected results");
        pass = true;
      } else {
        TestUtil.logErr("Did not get expected result:");
        for (String s : expected) {
          TestUtil.logTrace("expected:" + s);
        }
        for (String s : actual) {
          TestUtil.logTrace("actual:" + s);
        }
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);
    }

    if (!pass) {
      throw new Fault("treatJoinClassTest failed");
    }
  }

  /*
   * @testName: treatInWhereClauseTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1620; PERSISTENCE:SPEC:1627;
   * 
   * @test_Strategy:
   *
   */
  @SetupMethod(name = "setupOrderData")
  public void treatInWhereClauseTest() throws Fault {
    boolean pass = false;
    List<String> actual;
    List<String> expected = new ArrayList<String>();
    expected.add(softwareRef[0].getName());
    expected.add(softwareRef[7].getName());

    try {
      getEntityTransaction().begin();
      actual = getEntityManager().createQuery(
          "SELECT p.name FROM Product p where TREAT(p AS SoftwareProduct).revisionNumber = 1.0")
          .getResultList();

      Collections.sort(actual);
      for (String s : actual) {
        TestUtil.logTrace("result:" + s);

      }
      if (expected.containsAll(actual) && actual.containsAll(expected)
          && expected.size() == actual.size()) {
        TestUtil.logTrace("Received expected results");
        pass = true;
      } else {
        TestUtil.logErr("Did not get expected result:");
        for (String s : expected) {
          TestUtil.logTrace("expected:" + s);
        }
        for (String s : actual) {
          TestUtil.logTrace("actual:" + s);
        }
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);
    }

    if (!pass) {
      throw new Fault("treatInWhereClauseTest failed");
    }
  }

  /*
   * @testName: appropriateSuffixesTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1628;
   * 
   * @test_Strategy:
   *
   */
  @SetupMethod(name = "setupOrderData")
  public void appropriateSuffixesTest() throws Fault {
    boolean pass = false;
    List<String> actual;
    List<String> expected = new ArrayList<String>();
    expected.add(softwareRef[0].getName());
    expected.add(softwareRef[7].getName());

    try {
      getEntityTransaction().begin();
      actual = getEntityManager().createQuery(
          "SELECT p.name FROM Product p where TREAT(p AS SoftwareProduct).revisionNumber = 1.0D")
          .getResultList();

      Collections.sort(actual);
      for (String s : actual) {
        TestUtil.logTrace("result:" + s);

      }
      if (expected.containsAll(actual) && actual.containsAll(expected)
          && expected.size() == actual.size()) {
        TestUtil.logTrace("Received expected results");
        pass = true;
      } else {
        TestUtil.logErr("Did not get expected result:");
        for (String s : expected) {
          TestUtil.logTrace("expected:" + s);
        }
        for (String s : actual) {
          TestUtil.logTrace("actual:" + s);
        }
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);
    }

    if (!pass) {
      throw new Fault("appropriateSuffixesTest failed");
    }
  }

  /*
   * @testName: sqlApproximateNumericLiteralTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1627;
   * 
   * @test_Strategy:
   *
   */
  @SetupMethod(name = "setupOrderData")
  public void sqlApproximateNumericLiteralTest() throws Fault {
    boolean pass = false;
    List<String> actual;
    List<String> expected = new ArrayList<String>();
    expected.add(softwareRef[0].getName());
    expected.add(softwareRef[7].getName());

    try {
      getEntityTransaction().begin();
      actual = getEntityManager().createQuery(
          "SELECT p.name FROM Product p where TREAT(p AS SoftwareProduct).revisionNumber = 1E0")
          .getResultList();

      Collections.sort(actual);
      for (String s : actual) {
        TestUtil.logTrace("result:" + s);

      }
      if (expected.containsAll(actual) && actual.containsAll(expected)
          && expected.size() == actual.size()) {
        TestUtil.logTrace("Received expected results");
        pass = true;
      } else {
        TestUtil.logErr("Did not get expected result:");
        for (String s : expected) {
          TestUtil.logTrace("expected:" + s);
        }
        for (String s : actual) {
          TestUtil.logTrace("actual:" + s);
        }
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);
    }

    if (!pass) {
      throw new Fault("sqlApproximateNumericLiteralTest failed");
    }
  }

  /*
   * @testName: joinOnExpressionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1716
   * 
   * @test_Strategy:
   */
  @SetupMethod(name = "setupOrderData")
  public void joinOnExpressionTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];
    List<Order> actual;
    try {
      getEntityTransaction().begin();
      actual = getEntityManager().createQuery(
          "select o FROM Order o INNER JOIN o.lineItemsCollection l ON (l.quantity > 5)")
          .getResultList();

      expectedPKs = new String[2];
      expectedPKs[0] = "10";
      expectedPKs[1] = "12";

      for (Order o : actual) {
        TestUtil.logTrace("order:" + o.getId() + ":");
        Collection<LineItem> li = o.getLineItemsCollection();
        for (LineItem i : li) {
          TestUtil.logTrace("   item:" + i.getId() + ", " + i.getQuantity());
        }
      }

      if (!checkEntityPK(actual, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 3 references, got: "
                + actual.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("joinOnExpressionTest failed");
  }

  /*
   * @testName: aggregateFunctionsWithNoValuesTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:828; PERSISTENCE:SPEC:829;
   * 
   * @test_Strategy: Execute a query which contains aggregate functions when
   * there are no values and see that null or zero is returned.
   *
   */
  @SetupMethod(name = "setupProductData")
  public void aggregateFunctionsWithNoValuesTest() throws Fault {
    boolean pass = false;
    Query q;

    try {
      TestUtil.logMsg("Testing SUM");
      TestUtil.logTrace("find SUM of all product prices");
      q = getEntityManager()
          .createQuery("SELECT Sum(p.price) FROM Product p where p.id='9999' ");
      Object o = q.getSingleResult();
      if (o == null) {
        TestUtil.logTrace("Returned expected null results");
        pass = true;
      } else {
        TestUtil.logErr("Returned " + o.toString() + ", instead of null");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing AVG");
      TestUtil.logTrace("find AVG of all product prices");
      q = getEntityManager()
          .createQuery("SELECT AVG(p.price) FROM Product p where p.id='9999' ");
      Object o = q.getSingleResult();
      if (o == null) {
        TestUtil.logTrace("Returned expected null results");
        pass = true;
      } else {
        TestUtil.logErr("Returned " + o.toString() + ", instead of null");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing MAX");
      TestUtil.logTrace("find MAX of all product prices");
      q = getEntityManager()
          .createQuery("SELECT MAX(p.price) FROM Product p where p.id='9999' ");
      Object o = q.getSingleResult();
      if (o == null) {
        TestUtil.logTrace("Returned expected null results");
        pass = true;
      } else {
        TestUtil.logErr("Returned " + o.toString() + ", instead of null");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing MIN");
      TestUtil.logTrace("find MIN of all product prices");
      q = getEntityManager()
          .createQuery("SELECT MIN(p.price) FROM Product p where p.id='9999' ");
      Object o = q.getSingleResult();
      if (o == null) {
        TestUtil.logTrace("Returned expected null results");
        pass = true;
      } else {
        TestUtil.logErr("Returned " + o.toString() + ", instead of null");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing COUNT");
      TestUtil.logTrace("find COUNT of all product prices");
      q = getEntityManager().createQuery(
          "SELECT COUNT(p.price) FROM Product p where p.id='9999' ");
      Object o = q.getSingleResult();
      if (o != null) {
        if (o instanceof Long) {
          Long i = (Long) o;
          if (i == 0L) {
            TestUtil.logTrace("Returned expected 0 result");
            pass = true;
          }
        } else {
          TestUtil.logErr("Did not get instance of Long");
        }
      } else {
        TestUtil.logErr("Received null instead of 0");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    if (!pass)
      throw new Fault("aggregateFunctionsWithNoValuesTest failed");
  }

  /*
   * @testName: primaryKeyJoinColumnTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1120; PERSISTENCE:SPEC:1121;
   * PERSISTENCE:SPEC:1121.1;
   *
   * @test_Strategy: Select p from Product p where p.whouse = "WH5"
   */
  @SetupMethod(name = "setupProductData")
  public void primaryKeyJoinColumnTest() throws Fault {
    boolean pass = false;
    String expectedPKs[];
    List<Product> actual;
    try {
      getEntityTransaction().begin();
      actual = getEntityManager()
          .createQuery("Select p from Product p where p.wareHouse = 'WH5'")
          .getResultList();

      if (actual.size() == 1 && actual.get(0).getWareHouse().equals("WH5")) {
        TestUtil.logTrace(
            "Expected results received:" + actual.get(0).getWareHouse());
        pass = true;
      } else {
        TestUtil.logErr("test returned: " + actual.get(0).getWareHouse()
            + ", expected: WH5");
        for (Product p : actual) {
          TestUtil.logErr("**id=" + p.getId() + ", model=" + p.getWareHouse());
        }
      }
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass) {
      throw new Fault("primaryKeyJoinColumnTest  failed");
    }
  }

  /*
   * @testName: typeTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1738; PERSISTENCE:SPEC:1658;
   * 
   * @test_Strategy: test path.type()
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

    getEntityTransaction().begin();
    List<Product> result = getEntityManager()
        .createQuery("Select p from Product p where TYPE(p) = HardwareProduct")
        .getResultList();

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

    if (!pass) {
      throw new Fault("primaryKeyJoinColumnTest  failed");
    }
  }

  /*
   * @testName: subqueryVariableOverridesQueryVariableTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1599;
   * 
   * @test_Strategy: variable in a query is overridden in a subquery
   */
  @SetupMethod(name = "setupOrderData")
  public void subqueryVariableOverridesQueryVariableTest() throws Fault {

    boolean pass = false;
    List result;
    String expectedPKs[];

    try {
      getEntityTransaction().begin();
      result = getEntityManager().createQuery(
          "SELECT c FROM Customer c WHERE c.id in (SELECT c.id FROM Order c where c.id='10' ) ")
          .getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "10";

      if (!checkEntityPK(result, expectedPKs)) {
        TestUtil.logErr("Expected 1 result, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception:", e);
    }

    if (!pass)
      throw new Fault("subqueryVariableOverridesQueryVariableTest failed");
  }

  /*
   * @testName: longIdentifierNameTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1586; PERSISTENCE:SPEC:1590
   * 
   * @test_Strategy: verify a long identifier name can be used and that the
   * identifier evaluates to a value of the type of the expression
   */
  @SetupMethod(name = "setupOrderData")
  public void longIdentifierNameTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = true;
    String expectedPKs[];
    List o;

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find All Orders for Customer: Robert E. Bissett");
      o = getEntityManager().createQuery(
          "Select Distinct variable01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789 from Order variable01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789 WHERE variable01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789.customer.name = :name")
          .setParameter("name", "Robert E. Bissett").getResultList();
      if (o.size() > 0) {
        for (Object oo : o) {
          if (!(oo instanceof Order)) {
            TestUtil.logErr("Object returned was not of type Order:"
                + oo.getClass().getName());
            pass2 = false;
          }
        }
      } else {
        TestUtil.logErr("Not results were returned");
        pass2 = false;
      }
      expectedPKs = new String[2];
      expectedPKs[0] = "4";
      expectedPKs[1] = "9";
      if (!checkEntityPK(o, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 2 references, got: "
                + o.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass1 = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass1 || !pass2)
      throw new Fault("longIdentifierNameTest failed");
  }

  /*
   * @testName: underscoreIdentifierNameTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1587; PERSISTENCE:SPEC:1588;
   * 
   * @test_Strategy: verify an identifier name can begin with an underscore.
   */
  @SetupMethod(name = "setupOrderData")
  public void underscoreIdentifierNameTest() throws Fault {
    boolean pass1, pass2;
    pass1 = pass2 = false;
    String expectedPKs[];
    List o;

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find All Orders for Customer: Robert E. Bissett");
      String variable = "_01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789";
      if (checkIdentifierValues(variable)) {
        TestUtil.logTrace("Identifier is valid");
        pass1 = true;
      } else {
        TestUtil.logTrace("Identifier[" + variable + "] is invalid");
      }
      String sQuery = "Select Distinct " + variable + " from Order " + variable
          + " WHERE " + variable + ".customer.name = :name";
      TestUtil.logTrace("Query=" + sQuery);
      o = getEntityManager().createQuery(sQuery)
          .setParameter("name", "Robert E. Bissett").getResultList();

      expectedPKs = new String[2];
      expectedPKs[0] = "4";
      expectedPKs[1] = "9";
      if (!checkEntityPK(o, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 2 references, got: "
                + o.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass2 = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass1 || !pass2)
      throw new Fault("underscoreIdentifierNameTest failed");
  }

  /*
   * @testName: dollarsignIdentifierNameTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1587; PERSISTENCE:SPEC:1588;
   * 
   * @test_Strategy: verify an identifier name can begin with a dollarsign.
   */
  @SetupMethod(name = "setupOrderData")
  public void dollarsignIdentifierNameTest() throws Fault {
    boolean pass1, pass2;
    pass1 = pass2 = false;
    String expectedPKs[];
    List o;

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find All Orders for Customer: Robert E. Bissett");

      String variable = "$01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789";
      if (checkIdentifierValues(variable)) {
        TestUtil.logTrace("Identifier is valid");
        pass1 = true;
      } else {
        TestUtil.logTrace("Identifier[" + variable + "] is invalid");
      }
      String sQuery = "Select Distinct " + variable + " from Order " + variable
          + " WHERE " + variable + ".customer.name = :name";
      TestUtil.logTrace("Query=" + sQuery);
      o = getEntityManager().createQuery(sQuery)
          .setParameter("name", "Robert E. Bissett").getResultList();

      expectedPKs = new String[2];
      expectedPKs[0] = "4";
      expectedPKs[1] = "9";
      if (!checkEntityPK(o, expectedPKs)) {
        TestUtil.logErr(
            "Did not get expected results.  Expected 2 references, got: "
                + o.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass2 = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass1 || !pass2)
      throw new Fault("dollarsignIdentifierNameTest failed");
  }

  /*
   * @testName: entityTypeLiteralTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1632; PERSISTENCE:SPEC:1658;
   * 
   * @test_Strategy: Test an entity type literal can be specified in a query
   *
   */
  @SetupMethod(name = "setupProductData")
  public void entityTypeLiteralTest() throws Fault {
    boolean pass = false;
    List p;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find All Products");
      p = getEntityManager()
          .createQuery(
              "Select p From Product p where TYPE(p) in (SoftwareProduct)")
          .getResultList();

      String[] expectedPKs = new String[softwareRef.length];
      int i = 0;
      for (SoftwareProduct sf : softwareRef) {
        expectedPKs[i++] = sf.getId();
      }

      if (!checkEntityPK(p, expectedPKs)) {
        TestUtil.logErr("Did not get expected results.  Expected "
            + expectedPKs.length + " references, got: " + p.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("entityTypeLiteralTest failed");
  }

  /*
   * @testName: scalarExpressionsTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:2512;
   * 
   * @test_Strategy: Test various scalar expressions test
   *
   */
  @SetupMethod(name = "setupProductData")
  public void scalarExpressionsTest() throws Fault {
    boolean pass1, pass2;
    pass1 = pass2 = false;
    List p;
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("Testing arithmetic expression:");
      p = getEntityManager()
          .createQuery("Select p From Product p where ((p.quantity) + 10 < 25)")
          .getResultList();

      String[] expectedPKs = new String[5];
      expectedPKs[0] = "8";
      expectedPKs[1] = "9";
      expectedPKs[2] = "15";
      expectedPKs[3] = "17";
      expectedPKs[4] = "21";

      if (!checkEntityPK(p, expectedPKs)) {
        TestUtil.logErr("Did not get expected results.  Expected "
            + expectedPKs.length + " references, got: " + p.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass1 = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("Testing string expression:");
      p = getEntityManager()
          .createQuery("Select p From Product p where (p.name like 'Java%')")
          .getResultList();

      String[] expectedPKs = new String[4];
      expectedPKs[0] = "1";
      expectedPKs[1] = "2";
      expectedPKs[2] = "20";
      expectedPKs[3] = "34";

      if (!checkEntityPK(p, expectedPKs)) {
        TestUtil.logErr("Did not get expected results.  Expected "
            + expectedPKs.length + " references, got: " + p.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass2 = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass1 || !pass2)
      throw new Fault("scalarExpressionsTest failed");
  }

  /*
   * @testName: distinctNotSpecifiedTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1664;
   * 
   * @test_Strategy: Verify duplicates are returned when distinct is not
   * specified
   *
   */
  @SetupMethod(name = "setupOrderData")
  public void distinctNotSpecifiedTest() throws Fault {
    boolean pass = false;
    Integer expectedPKs[];
    List<String> o;

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("find All customer ids from Orders");
      o = getEntityManager()
          .createQuery("Select o.customer.id from Order AS o ").getResultList();

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
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("distinctNotSpecifiedTest failed");
  }

  /*
   * @testName: resultVariableTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1666;
   * 
   * @test_Strategy: A result variable may be used to name a select item in the
   * query result
   */
  @SetupMethod(name = "setupOrderData")
  public void resultVariableTest() throws Fault {

    boolean pass = false;
    List<String> o;

    try {
      getEntityTransaction().begin();

      o = getEntityManager().createQuery("Select o.id AS OID from Order o "
          + "WHERE (o.totalPrice < 100.0) ORDER BY OID").getResultList();

      Integer expectedPKs[] = new Integer[4];
      expectedPKs[0] = Integer.parseInt("9");
      expectedPKs[1] = Integer.parseInt("10");
      expectedPKs[2] = Integer.parseInt("12");
      expectedPKs[3] = Integer.parseInt("13");

      if (!checkEntityPK(o, expectedPKs, true, true)) {
        TestUtil.logErr("Did not get expected results.  Expected "
            + expectedPKs.length + " references, got: " + o.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass)
      throw new Fault("resultVariableTest failed");
  }

  /*
   * @testName: embeddableNotManagedTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1671;
   * 
   * @test_Strategy: verify modified detached entity does not effect managed
   * entity
   */
  @SetupMethod(name = "setupCustomerData")
  public void embeddableNotManagedTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    List<Object[]> q;
    Customer cust;
    try {
      getEntityTransaction().begin();
      q = getEntityManager().createQuery(
          "SELECT c, c.country FROM Customer c where c.home.city = :homecity")
          .setParameter("homecity", "Bedford").getResultList();

      if (q.size() == 1) {
        for (Object[] o : q) {
          TestUtil.logMsg("Testing initial values");
          cust = (Customer) o[0];
          Country country = (Country) o[1];
          TestUtil.logTrace("Customer:" + cust.toString());
          TestUtil.logTrace("Country:" + country.toString());
          if (cust.getCountry() != country) {
            TestUtil.logTrace(
                "Customer country object does not equal Country from query as expected");
            pass1 = true;
          } else {
            TestUtil
                .logErr("Customer country object equals Country from query");
          }
          TestUtil.logMsg("Change values of country");
          country.setCode("CHA");
          country.setCountry("China");
          TestUtil.logTrace("Customer:" + cust.toString());
          TestUtil.logTrace("Country:" + country.toString());
          TestUtil.logTrace("Flush and refresh");
          getEntityManager().flush();
          getEntityManager().refresh(cust);
          TestUtil.logMsg("Test values again");
          TestUtil.logTrace("Customer:" + cust.toString());
          TestUtil.logTrace("Country:" + country.toString());
          if (cust.getCountry() != country) {
            if (!cust.getCountry().getCountry().equals("China")
                && (!cust.getCountry().getCode().equals("CHA"))) {
              TestUtil.logTrace(
                  "Customer.country does not contain the modifications made to the Country object");
              pass2 = true;
            } else {
              TestUtil.logErr(
                  "Customer.country contains the modifications made to the Country object");
            }
          } else {
            TestUtil
                .logErr("Customer country object equals Country from query");
          }
        }
      } else {
        TestUtil.logErr("Did not get 1 result back:" + q.size());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass1 || !pass2)
      throw new Fault("embeddableNotManagedTest failed");

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
    List<Department> result;

    getEntityTransaction().begin();

    result = getEntityManager().createQuery(
        "SELECT d FROM Department d LEFT JOIN FETCH d.lastNameEmployees WHERE d.id = 1")
        .getResultList();

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

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("resultContainsFetchReference test failed");

    }
  }

  private boolean checkIdentifierValues(String var) {
    boolean pass = true;
    TestUtil.logMsg("Testing that identifier is valid");
    char[] c = var.toCharArray();
    boolean first = true;
    for (char cc : c) {
      if (first) {
        if (!Character.isJavaIdentifierStart(cc)) {
          TestUtil.logErr("Value[" + cc + "is not a valid start character");
          pass = false;
        }
        // TestUtil.logTrace("start["+cc+"]:"+Character.isJavaIdentifierStart(cc));
        first = false;
      } else {
        // TestUtil.logTrace("part["+cc+"]:"+Character.isJavaIdentifierPart(cc));
        if (!Character.isJavaIdentifierPart(cc)) {
          TestUtil.logErr("Value[" + cc + "is not a valid part character");
          pass = false;
        }
      }
    }
    return pass;
  }

}
