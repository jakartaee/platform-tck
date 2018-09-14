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
 * @(#)Client.java	1.26 03/05/16
 */

package com.sun.ts.tests.ejb.ee.pm.ejbql.where_clause;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import com.sun.ts.tests.ejb.ee.pm.ejbql.schema.*;

import java.io.*;
import java.util.*;
import javax.ejb.*;
import javax.transaction.*;
import javax.rmi.PortableRemoteObject;
import java.rmi.*;

import com.sun.javatest.Status;

public class Client extends EETest {
  private CustomerHome customerHome = null;

  private OrderHome orderHome = null;

  private AliasHome aliasHome = null;

  private ProductHome productHome = null;

  private Properties props = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   */

  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    try {
      props = p;
      Schema.setup(p);
      customerHome = Schema.customerHome;
      orderHome = Schema.orderHome;
      aliasHome = Schema.aliasHome;
      productHome = Schema.productHome;
    } catch (Exception e) {
      TestUtil.logMsg("Exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("Setup failed:", e);

    }
  }

  /* Run test */

  /*
   * @testName: whereTest1
   * 
   * @assertion_ids: EJB:SPEC:331; EJB:SPEC:412.2
   * 
   * @test_Strategy: Execute the findOrdersByQuery1 method using an exact
   * numeric literal in a conditional expression of the WHERE clause. Verify the
   * results were accurately returned.
   * 
   */

  public void whereTest1() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection o = null;

    try {
      TestUtil.logMsg("Find all orders with a total price < $100");
      o = orderHome.findOrdersByQuery1();
      expectedPKs = new String[4];
      expectedPKs[0] = "9";
      expectedPKs[1] = "10";
      expectedPKs[2] = "12";
      expectedPKs[3] = "13";
      if (!Util.checkEJBs(o, Schema.ORDERREF, expectedPKs)) {
        TestUtil
            .logErr("UnSuccessfully found all orders with total price < $100");
        pass = false;
      } else {
        TestUtil
            .logMsg("Successfully found all orders with total price < $100");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest1: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest1 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest1 failed");
  }

  /*
   * @testName: whereTest2
   * 
   * @assertion_ids: EJB:SPEC:333
   * 
   * @test_Strategy: Execute the findOrdersByQuery2 method using an approximate
   * numeric literal in the conditional expression of the WHERE clause. Verify
   * the results were accurately returned.
   * 
   */

  public void whereTest2() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection o = null;

    try {
      TestUtil.logMsg("Find all orders with a total price > $553.95");
      o = orderHome.findOrdersByQuery2();
      expectedPKs = new String[11];
      expectedPKs[0] = "1";
      expectedPKs[1] = "2";
      expectedPKs[2] = "3";
      expectedPKs[3] = "4";
      expectedPKs[4] = "5";
      expectedPKs[5] = "6";
      expectedPKs[6] = "7";
      expectedPKs[7] = "8";
      expectedPKs[8] = "11";
      expectedPKs[9] = "14";
      expectedPKs[10] = "16";
      if (!Util.checkEJBs(o, Schema.ORDERREF, expectedPKs)) {
        TestUtil.logErr(
            "UnSuccessfully found all orders with total price > $553.95");
        pass = false;
      } else {
        TestUtil
            .logMsg("Successfully found all orders with total price > $553.95");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest2: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest2 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest2 failed");
  }

  /*
   * @testName: whereTest3
   * 
   * @assertion_ids: EJB:SPEC:348.4; EJB:SPEC:345
   * 
   * @test_Strategy: Execute the findOrdersByQuery3 method containing a
   * conditional expression composed with logical operator NOT. Verify the
   * results were accurately returned.
   * 
   */

  public void whereTest3() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection o = null;

    try {
      TestUtil.logMsg(
          "Find all orders where the total price is NOT less than $4500");
      o = orderHome.findOrdersByQuery3();
      expectedPKs = new String[3];
      expectedPKs[0] = "5";
      expectedPKs[1] = "11";
      expectedPKs[2] = "16";
      if (!Util.checkEJBs(o, Schema.ORDERREF, expectedPKs)) {
        TestUtil.logErr("UnSuccessfully found all orders for Query 3");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully found all orders for Query 3");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest3: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest3 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest3 failed");
  }

  /*
   * @testName: whereTest4
   * 
   * @assertion_ids: EJB:SPEC:348.4; EJB:SPEC:345
   * 
   * @test_Strategy: Execute the findOrdersByQuery4 method containing a
   * conditional expression composed with logical operator AND. Verify the
   * results were accurately returned.
   * 
   */

  public void whereTest4() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection o = null;

    try {
      TestUtil.logMsg("Find all orders where the line item quantity is 1 "
          + "AND the customer name is Robert E. Bissett");
      o = orderHome.findOrdersByQuery4();
      expectedPKs = new String[2];
      expectedPKs[0] = "4";
      expectedPKs[1] = "9";
      if (!Util.checkEJBs(o, Schema.ORDERREF, expectedPKs)) {
        TestUtil.logErr("UnSuccessfully found all orders for Query 4");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully found all orders for Query 4");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest4: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest4 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest4 failed");
  }

  /*
   * @testName: whereTest5
   * 
   * @assertion_ids: EJB:SPEC:348.4; EJB:SPEC:345
   * 
   * @test_Strategy: Execute the findOrdersByQuery5 method containing a a
   * conditional expression composed with logical operator OR. Verify the
   * results were accurately returned.
   * 
   */

  public void whereTest5() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection o = null;

    try {
      TestUtil
          .logMsg("Find all orders where the customer name is Karen R. Tegan"
              + " OR the total price is less than $100");
      o = orderHome.findOrdersByQuery5();
      expectedPKs = new String[5];
      expectedPKs[0] = "6";
      expectedPKs[1] = "9";
      expectedPKs[2] = "10";
      expectedPKs[3] = "12";
      expectedPKs[4] = "13";
      if (!Util.checkEJBs(o, Schema.ORDERREF, expectedPKs)) {
        TestUtil.logErr("UnSuccessfully found all orders for Query5");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully found all orders for Query5");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest5: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest5 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest5 failed");
  }

  /*
   * @testName: whereTest6
   * 
   * @assertion_ids: EJB:SPEC:346; EJB:SPEC:347; EJB:SPEC:348.2; EJB:SPEC:344
   * 
   * @test_Strategy: Execute the findOrdersByQuery6 method containing a
   * conditional expression composed with AND and OR and using standard
   * bracketing () for ordering. The comparison operator < and arithmetic
   * operations are also used in the query. Verify the results were accurately
   * returned.
   * 
   */

  public void whereTest6() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection o = null;

    try {
      TestUtil.logMsg("Find all orders where line item quantity is 1 AND the"
          + " order total less than 100 or customer name is Robert E. Bissett");
      o = orderHome.findOrdersByQuery6();
      expectedPKs = new String[4];
      expectedPKs[0] = "4";
      expectedPKs[1] = "9";
      expectedPKs[2] = "12";
      expectedPKs[3] = "13";
      if (!Util.checkEJBs(o, Schema.ORDERREF, expectedPKs)) {
        TestUtil.logErr("UnSuccessfully found all orders for Query 6");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully found all orders for Query 6");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest6: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest6 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest6 failed");
  }

  /*
   * @testName: whereTest7
   * 
   * @assertion_ids: EJB:SPEC:338; EJB:SPEC:339; EJB:SPEC:341
   * 
   * @test_strategy: Execute the findOrdersByQuery9 method using a WHERE clause
   * conditional expression composed with AND with an input parameter as a
   * conditional factor. The comparison operator < is also used in the query.
   * Verify the results were accurately returned.
   * 
   */

  public void whereTest7() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection o = null;
    try {
      TestUtil.logMsg("Find all orders with line item quantity < 2"
          + " for customer Robert E. Bissett");
      o = orderHome.findOrdersByQuery9("Robert E. Bissett");
      expectedPKs = new String[2];
      expectedPKs[0] = "4";
      expectedPKs[1] = "9";
      if (!Util.checkEJBs(o, Schema.ORDERREF, expectedPKs)) {
        TestUtil.logErr("UnSuccessfully found all orders for Query 9");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully found all orders for Query 9");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest7: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest7 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest7 failed");
  }

  /*
   * @testName: whereTest8
   * 
   * @assertion_ids: EJB:SPEC:349
   * 
   * @test_Strategy: Execute the findOrdersByQuery12 method containing the
   * comparison operator BETWEEN. Verify the results were accurately returned.
   * 
   */

  public void whereTest8() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection o = null;

    try {
      TestUtil
          .logMsg("Find all orders with a total price BETWEEN $1000 and $1200");
      o = orderHome.findOrdersByQuery12();
      expectedPKs = new String[5];
      expectedPKs[0] = "1";
      expectedPKs[1] = "3";
      expectedPKs[2] = "7";
      expectedPKs[3] = "8";
      expectedPKs[4] = "14";
      if (!Util.checkEJBs(o, Schema.ORDERREF, expectedPKs)) {
        TestUtil.logErr(
            "UnSuccessfully found all orders with total price BETWEEN $1000 and $1200");
        pass = false;
      } else {
        TestUtil.logMsg(
            "Successfully found all orders with total price BETWEEN $1000 and $1200");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest8: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest8 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest8 failed");
  }

  /*
   * @testName: whereTest9
   * 
   * @assertion_ids: EJB:SPEC:349
   * 
   * @test_Strategy: Execute the findOrdersByQuery13 method containing the
   * comparison operator NOT BETWEEN. Verify the results were accurately
   * returned.
   */

  public void whereTest9() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection o = null;

    try {
      TestUtil.logMsg(
          "Find all orders with a total price NOT BETWEEN $1000 and $1200");
      o = orderHome.findOrdersByQuery13();
      expectedPKs = new String[11];
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
      if (!Util.checkEJBs(o, Schema.ORDERREF, expectedPKs)) {
        TestUtil.logErr(
            "UnSuccessfully found all orders with total price NOT BETWEEN $1000 and $1200");
        pass = false;
      } else {
        TestUtil.logMsg(
            "Successfully found all orders with total price NOT BETWEEN $1000 and $1200");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest9: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest9 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest9 failed");
  }

  /*
   * @testName: whereTest10
   * 
   * @assertion_ids: EJB:SPEC:345; EJB:SPEC:334
   * 
   * @test_Strategy: Conditional expressions are composed of other conditional
   * expressions, comparison operators, logical operations, path expressions
   * that evaluate to boolean values and boolean literals.
   *
   * Execute the findOrdersByQuery14 method that contains a conditional
   * expression with a path expression that evaluates to a boolean literal.
   * Verify the results were accurately returned.
   */

  public void whereTest10() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection o = null;

    try {
      TestUtil.logMsg("Find all orders that do not have approved Credit Cards");
      o = orderHome.findOrdersByQuery14();
      expectedPKs = new String[4];
      expectedPKs[0] = "1";
      expectedPKs[1] = "7";
      expectedPKs[2] = "11";
      expectedPKs[3] = "13";
      if (!Util.checkEJBs(o, Schema.ORDERREF, expectedPKs)) {
        TestUtil
            .logErr("UnSuccessfully found orders with unapproved Credit Cards");
        pass = false;
      } else {
        TestUtil.logMsg(
            "Successfully found all orders with unapproved Credit Cards");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest10: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest10 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest10 failed");
  }

  /*
   * @testName: whereTest11
   * 
   * @assertion_ids: EJB:SPEC:330; EJB:SPEC:313.6; EJB:SPEC:412.1
   * 
   * @test_Strategy: Execute the findCustomerByName method using a string
   * literal enclosed in single quotes (the string includes a single quote) in
   * the conditional expression of the WHERE clause. Verify the results were
   * accurately returned.
   */

  public void whereTest11() throws Fault {
    boolean pass = true;
    Customer c = null;
    try {
      TestUtil.logMsg("Find customer with name: Stephen S. D'Milla");
      c = customerHome.findCustomerByName("Stephen S. D'Milla");
      if (!Util.checkEJB(c, "5")) {
        TestUtil.logErr("UnSuccessfully found customer Stephen S. D'Milla");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully found customer Stephen S. D'Milla");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest11: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest11 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest11 failed");
  }

  /*
   * @testName: whereTest12
   * 
   * @assertion_ids: EJB:SPEC:352
   * 
   * @test_Strategy: Execute the findCustomersByQuery8 method using comparison
   * operator IN in a comparison expression within the WHERE clause. Verify the
   * results were accurately returned.
   */

  public void whereTest12() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection c = null;
    try {
      TestUtil.logMsg("Find all customers IN home city: Lexington");
      c = customerHome.findCustomersByQuery8();
      expectedPKs = new String[1];
      expectedPKs[0] = "2";
      if (!Util.checkEJBs(c, Schema.CUSTOMERREF, expectedPKs)) {
        TestUtil.logErr(
            "UnSuccessfully found all customers IN home city: Lexington");
        pass = false;
      } else {
        TestUtil
            .logMsg("Successfully found all customers IN home city: Lexington");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest12: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest12 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest12 failed");
  }

  /*
   * @testName: whereTest13
   * 
   * @assertion_ids: EJB:SPEC:352; EJB:SPEC:353
   * 
   * @test_Strategy: Execute the findCustomersByQuery9 method using comparison
   * operator NOT IN in a comparison expression within the WHERE clause. Verify
   * the results were accurately returned.
   */

  public void whereTest13() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection c = null;
    try {
      TestUtil
          .logMsg("Find all customers NOT IN home city: Swansea or Brookline");
      c = customerHome.findCustomersByQuery9();
      expectedPKs = new String[11];
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
      if (!Util.checkEJBs(c, Schema.CUSTOMERREF, expectedPKs)) {
        TestUtil.logErr(
            "UnSuccessfully found all customers NOT IN home city: Swansea or Brookline");
        pass = false;
      } else {
        TestUtil.logMsg(
            "Successfully found all customers NOT IN home city: Swansea or Brookline");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest13: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest13 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest13 failed");
  }

  /*
   * @testName: whereTest14
   * 
   * @assertion_ids: EJB:SPEC:358
   * 
   * @test_Strategy: Execute the findCustomersByQuery10 method using the
   * comparison operator LIKE in a comparison expression within the WHERE
   * clause. The pattern-value includes a percent character. Verify the results
   * were accurately returned.
   */

  public void whereTest14() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection c = null;
    try {
      TestUtil.logMsg("Find All Customers with home ZIP CODE that ends in 77");
      c = customerHome.findCustomersByQuery10();
      expectedPKs = new String[1];
      expectedPKs[0] = "2";
      if (!Util.checkEJBs(c, Schema.CUSTOMERREF, expectedPKs)) {
        TestUtil.logErr(
            "UnSuccessfully found all customers with home ZIP CODE ending in 77");
        pass = false;
      } else {
        TestUtil.logMsg(
            "Successfully found all customers with home ZIP CODE ending in 77");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest14: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest14 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest14 failed");
  }

  /*
   * @testName: whereTest15
   * 
   * @assertion_ids: EJB:SPEC:358
   * 
   * @test_Strategy: Execute the findCustomersByQuery11 method using the
   * comparison operator NOT LIKE in a comparison expression within the WHERE
   * clause. The pattern-value includes a percent character and an underscore.
   * Verify the results were accurately returned.
   *
   */

  public void whereTest15() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection c = null;
    try {
      TestUtil.logMsg(
          "Find all customers with a home zip code that does not contain"
              + " 44 in the third and fourth position");
      c = customerHome.findCustomersByQuery11();
      expectedPKs = new String[11];
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
      if (!Util.checkEJBs(c, Schema.CUSTOMERREF, expectedPKs)) {
        TestUtil.logErr(
            "UnSuccessfully found all customers whose home zip code does not contain 44");
        pass = false;
      } else {
        TestUtil.logMsg(
            "Successfully found all customers whose ZIP CODE does not contain 44");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest15: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest15 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest15 failed");
  }

  /*
   * @testName: whereTest16
   * 
   * @assertion_ids: EJB:SPEC:361
   * 
   * @test_Strategy: Execute the findCustomersByQuery12 method using the
   * comparison operator IS EMPTY in a comparison expression within the WHERE
   * clause. Verify the results were accurately returned.
   *
   */

  public void whereTest16() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection c = null;
    try {
      TestUtil.logMsg("Find all customers who do not have aliases");
      c = customerHome.findCustomersByQuery12();
      expectedPKs = new String[1];
      expectedPKs[0] = "6";
      if (!Util.checkEJBs(c, Schema.CUSTOMERREF, expectedPKs)) {
        TestUtil.logErr(
            "UnSuccessfully found customers who do not have aliases defined");
        pass = false;
      } else {
        TestUtil.logMsg(
            "Successfully found all customers who do not have aliases defined");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest16: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest16 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest16 failed");
  }

  /*
   * @testName: whereTest17
   * 
   * @assertion_ids: EJB:SPEC:361
   * 
   * @test_Strategy: Execute the findCustomersByQuery13 method using the
   * comparison operator IS NOT EMPTY in a comparison expression within the
   * WHERE clause. Verify the results were accurately returned.
   */

  public void whereTest17() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection c = null;
    try {
      TestUtil.logMsg("Find all customers who have aliases");
      c = customerHome.findCustomersByQuery13();
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
      if (!Util.checkEJBs(c, Schema.CUSTOMERREF, expectedPKs)) {
        TestUtil
            .logErr("UnSuccessfully found customers who have aliases defined");
        pass = false;
      } else {
        TestUtil.logMsg(
            "Successfully found all customers who have aliases defined");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest17: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest17 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest17 failed");
  }

  /*
   * @testName: whereTest18
   * 
   * @assertion_ids: EJB:SPEC:359; EJB:SPEC:316
   * 
   * @test_Strategy: Execute the findCustomersByQuery25 method using the IS NULL
   * comparison operator within the WHERE clause. Verify the results were
   * accurately returned. (This test is executed against non-NULL data. For NULL
   * data tests with this comparison operator, see the tests in the
   * ejbql/null_values test directory.)
   */

  public void whereTest18() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection c = null;
    try {
      TestUtil.logMsg("Find All Customers who have a null work zip code entry");
      c = customerHome.findCustomersByQuery25();
      expectedPKs = new String[1];
      expectedPKs[0] = "13";
      if (!Util.checkEJBs(c, Schema.CUSTOMERREF, expectedPKs)) {
        TestUtil.logErr(
            "UnSuccessfully found customers who have null work zip codes");
        pass = false;
      } else {
        TestUtil.logMsg(
            "Successfully found all customers who have null work zip codes");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest18: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest18 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest18 failed");
  }

  /*
   * @testName: whereTest19
   * 
   * @assertion_ids: EJB:SPEC:359
   * 
   * @test_Strategy: Execute the findCustomersByQuery26 method using the IS NOT
   * NULL comparison operator within the WHERE clause. Verify the results were
   * accurately returned. (This test is executed against non-NULL data. For NULL
   * data tests with this comparison operator, see the tests in the
   * ejbql/null_values test directory.)
   */

  public void whereTest19() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection c = null;
    try {
      TestUtil.logMsg(
          "Find all customers who do not have null work zip code entry");
      c = customerHome.findCustomersByQuery26();
      expectedPKs = new String[13];
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
      if (!Util.checkEJBs(c, Schema.CUSTOMERREF, expectedPKs)) {
        TestUtil.logErr(
            "UnSuccessfully found customers who do not have null work zip codes");
        pass = false;
      } else {
        TestUtil.logMsg(
            "Successfully found all customers do not have null work zip codes");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest19: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest19 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest19 failed");
  }

  /*
   * @testName: whereTest20
   * 
   * @assertion_ids: EJB:SPEC:369.1
   * 
   * @test_Strategy: Execute the findAliasesByQuery2 method which includes the
   * string function CONCAT in a functional expression within the WHERE clause.
   * Verify the results were accurately returned.
   */

  public void whereTest20() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection a = null;
    try {
      TestUtil.logMsg("Find all aliases who have match: stevie");
      a = aliasHome.findAliasesByQuery2();
      expectedPKs = new String[1];
      expectedPKs[0] = "14";
      if (!Util.checkEJBs(a, Schema.ALIASREF, expectedPKs)) {
        TestUtil.logErr("UnSuccessfully found matches to alias: stevie");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully found all matches with alias: stevie");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest20: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest20 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest20 failed");
  }

  /*
   * @testName: whereTest21
   * 
   * @assertion_ids: EJB:SPEC:369.2
   * 
   * @test_Strategy: Execute the findAliasesByQuery3 method which includes the
   * string function SUBSTRING in a functional expression within the WHERE
   * clause. Verify the results were accurately returned.
   */

  public void whereTest21() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection a = null;
    try {
      TestUtil.logMsg("Find all aliases containing the substring: iris");
      a = aliasHome.findAliasesByQuery3("iris", 1, 4);
      expectedPKs = new String[1];
      expectedPKs[0] = "20";
      if (!Util.checkEJBs(a, Schema.ALIASREF, expectedPKs)) {
        TestUtil.logErr("UnSuccessfully found expected aliases");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully found expected aliases");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest21: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest21 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest21 failed");
  }

  /*
   * @testName: whereTest22
   * 
   * @assertion_ids: EJB:SPEC:369.4
   * 
   * @test_Strategy: Execute the findAliasesByQuery4 method which includes the
   * string function LENGTH in a functional expression within the WHERE clause.
   * Verify the results were accurately returned.
   */

  public void whereTest22() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection a = null;
    try {
      TestUtil
          .logMsg("Find aliases whose alias name is greater than 4 characters");
      a = aliasHome.findAliasesByQuery4();
      expectedPKs = new String[7];
      expectedPKs[0] = "8";
      expectedPKs[1] = "10";
      expectedPKs[2] = "13";
      expectedPKs[3] = "14";
      expectedPKs[4] = "18";
      expectedPKs[5] = "28";
      expectedPKs[6] = "29";
      if (!Util.checkEJBs(a, Schema.ALIASREF, expectedPKs)) {
        TestUtil.logErr(
            "UnSuccessfully found alias names with greater than 4 characters");
        pass = false;
      } else {
        TestUtil.logMsg(
            "Successfully found alias names with greater than 4 characters");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest22: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest22 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest22 failed");
  }

  /*
   * @testName: whereTest23
   * 
   * @assertion_ids: EJB:SPEC:369.5
   * 
   * @test_Strategy: Execute the findOrdersByQuery16 method which includes the
   * arithmetic function ABS in a functional expression within the WHERE clause.
   * Verify the results were accurately returned.
   */

  public void whereTest23() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection o = null;
    try {
      TestUtil.logMsg("Find all Orders with a total price greater than 1180");
      o = orderHome.findOrdersByQuery16((double) 1180.00);
      expectedPKs = new String[7];
      expectedPKs[0] = "1";
      expectedPKs[1] = "2";
      expectedPKs[2] = "4";
      expectedPKs[3] = "5";
      expectedPKs[4] = "6";
      expectedPKs[5] = "11";
      expectedPKs[6] = "16";
      if (!Util.checkEJBs(o, Schema.ORDERREF, expectedPKs)) {
        TestUtil.logErr(
            "UnSuccessfully found orders with total price greater than 1180.00");
        pass = false;
      } else {
        TestUtil.logMsg(
            "Successfully found orders with total price greater than 1180.00");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest23: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest23 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest23 failed");
  }

  /*
   * @testName: whereTest24
   * 
   * @assertion_ids: EJB:SPEC:369.3
   * 
   * @test_Strategy: Execute the findAliasesByQuery5 method which includes the
   * string function LOCATE in a functional expression within the WHERE clause.
   * Verify the results were accurately returned.
   */

  public void whereTest24() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection a = null;
    try {
      TestUtil.logMsg(
          "Find all aliases who contain the string: ev in their alias name");
      a = aliasHome.findAliasesByQuery5();
      expectedPKs = new String[3];
      expectedPKs[0] = "13";
      expectedPKs[1] = "14";
      expectedPKs[2] = "18";
      if (!Util.checkEJBs(a, Schema.ALIASREF, expectedPKs)) {
        TestUtil.logErr(
            "UnSuccessfully found aliases who match the given string: ev");
        pass = false;
      } else {
        TestUtil.logMsg(
            "Successfully found all aliases who match the string: ev in their alias name");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest24: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest24 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest24 failed");
  }

  /*
   * @testName: whereTest25
   * 
   * @assertion_ids: EJB:SPEC:363.1; EJB:SPEC:365
   * 
   * @test_Strategy: Execute the findAliasesByQuery6 method using the comparison
   * operator MEMBER OF in a collection member expression. Verify the results
   * were accurately returned.
   */

  public void whereTest25() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection a = null;
    try {
      TestUtil.logMsg("Find aliases who are members of customersNoop");
      a = aliasHome.findAliasesByQuery6();
      expectedPKs = new String[0];
      if (!Util.checkEJBs(a, Schema.ALIASREF, expectedPKs)) {
        TestUtil.logErr("UnSuccessfully found aliases in MEMBER collection");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully found aliases in MEMBER collection");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest25: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest25 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest25 failed");
  }

  /*
   * @testName: whereTest26
   * 
   * @assertion_ids: EJB:SPEC:363; EJB:SPEC:365
   * 
   * @test_Strategy: Execute the findAliasesByQuery7 method using the comparison
   * operator NOT MEMBER in a collection member expression. Verify the results
   * were accurately returned.
   */

  public void whereTest26() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection a = null;
    try {
      TestUtil.logMsg("Find aliases who are NOT members of collection");
      a = aliasHome.findAliasesByQuery7();
      expectedPKs = new String[Schema.NUMOFALIASES];
      for (int i = 0; i < Schema.NUMOFALIASES; i++)
        expectedPKs[i] = Integer.toString(i + 1);
      if (!Util.checkEJBs(a, Schema.ALIASREF, expectedPKs)) {
        TestUtil
            .logErr("UnSuccessfully found aliases NOT in member collection");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully found aliases NOT in member collection");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest26: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest26 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest26 failed");
  }

  /*
   * @testName: whereTest27
   * 
   * @assertion_ids: EJB:SPEC:358
   * 
   * @test_Strategy: Execute the findCustomersByQuery28 method using the
   * comparison operator LIKE in a comparison expression within the WHERE
   * clause. The optional ESCAPE syntax is used to escape the underscore. Verify
   * the results were accurately returned.
   *
   */

  public void whereTest27() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection c = null;
    try {
      TestUtil.logMsg("Find all customers with an alias LIKE: sh_ll");
      c = customerHome.findCustomersByQuery28();
      expectedPKs = new String[1];
      expectedPKs[0] = "3";
      if (!Util.checkEJBs(c, Schema.CUSTOMERREF, expectedPKs)) {
        TestUtil.logErr(
            "UnSuccessfully found all customers with an alias LIKE: sh_ll");
        pass = false;
      } else {
        TestUtil.logMsg(
            "Successfully found all customers with an alias LIKE: sh_ll");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest27: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest27 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest27 failed");
  }

  /*
   * @testName: whereTest28
   * 
   * @assertion_ids: EJB:SPEC:341; EJB:SPEC:340; EJB:SPEC:266
   * 
   * @test_Strategy: Execute the findCustomerByQuery29 method on the local home
   * interface. The method name and signature of this method is the same as in
   * the remote home interface. Verify the results were accurately returned.
   * 
   */

  public void whereTest28() throws Fault {
    boolean pass = true;
    Customer c = null;
    try {
      TestUtil.logMsg("Find customer with home address in Lexington, MA");
      c = customerHome.getCustomerByQuery29("10 Griffin Road", "Lexington",
          "MA", "02277");
      if (!Util.checkEJB(c, "2")) {
        TestUtil.logErr("UnSuccessfully found customer Arthur D. Frechette");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully found customer Arthur D. Frechette");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest28: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest28 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest28 failed");
  }

  /*
   * @testName: whereTest29
   * 
   * @assertion_ids: EJB:SPEC:340; EJB:SPEC:341; EJB:SPEC:266
   * 
   * @test_Strategy: Execute the findCustomerByQuery29 method defined on the
   * remote home interface only providing two of the input parameters in the
   * query. The method name and signature of this method is the same as in the
   * local home interface. Verify the results were accurately returned.
   * 
   */

  public void whereTest29() throws Fault {
    boolean pass = true;
    Customer c = null;
    try {
      TestUtil
          .logMsg("Find customer with home address in Hudson, New Hampshire");
      c = customerHome.findCustomerByQuery29("100 Forrest Drive", "Hudson",
          "NH", "78654");
      if (!Util.checkEJB(c, "5")) {
        TestUtil.logErr("UnSuccessfully found customer Stephen S. DMilla");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully found customer Stephen S. DMilla");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest29: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest29 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest29 failed");
  }

  /*
   * 
   * /*
   * 
   * @testName: whereTest30
   * 
   * @assertion_ids: EJB:SPEC:266
   * 
   * @test_Strategy: In the case that both the remote home interface and local
   * home interface define a finder method with the same name and argument
   * types, the EJB QL query string specified by the query element defines the
   * semantics of both. Verify the results were accurately returned.
   * 
   */

  public void whereTest30() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection c = null;
    try {
      TestUtil.logMsg("Find customers with home city of Peabody");
      c = customerHome.getCustomersByQuery32("Peabody");
      expectedPKs = new String[2];
      expectedPKs[0] = "7";
      expectedPKs[1] = "8";
      if (!Util.checkEJBs(c, Schema.CUSTOMERREF, expectedPKs)) {
        TestUtil
            .logErr("UnSuccessfully found customers with home city of Peabody");
        pass = false;
      } else {
        TestUtil
            .logMsg("Successfully found customers with home city of Peabody");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest30: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest30 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest30 failed");
  }

  /*
   * @testName: whereTest31
   * 
   * @assertion_ids: EJB:SPEC:266
   * 
   * @test_Strategy: In the case that both the remote home interface and local
   * home interface define a finder method with the same name and argument
   * types, the EJB QL query string specified by the query element defines the
   * semantics of both.
   *
   * Execute the findCustomersByQuery32 method defined on the remote home
   * interface only providing two of the input parameters in the query. The
   * method name and signature of this method is the same as in the local home
   * interface and returns a Collection of EJBObjects. Verify the results were
   * accurately returned.
   * 
   */

  public void whereTest31() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection c = null;
    try {
      TestUtil.logMsg("Find customers with home city Peabody");
      c = customerHome.findCustomersByQuery32("Peabody");
      expectedPKs = new String[2];
      expectedPKs[0] = "7";
      expectedPKs[1] = "8";
      if (!Util.checkEJBs(c, Schema.CUSTOMERREF, expectedPKs)) {
        TestUtil
            .logErr("UnSuccessfully found customers with home city of Peabody");
        pass = false;
      } else {
        TestUtil
            .logMsg("Successfully found customers with home city of Peabody");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest31: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest31 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest31 failed");
  }

  /*
   * @testName: whereTest32
   * 
   * @assertion_ids: EJB:SPEC:363
   * 
   * @test_Strategy: Execute the findOrdersByQuery17 method using the comparison
   * operator MEMBER in a collection member expression with an identification
   * variable and omitting the optional reserved word OF. Verify the results
   * were accurately returned.
   */

  public void whereTest32() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection o = null;
    try {
      TestUtil
          .logMsg("Find all orders where line items are members of the orders");
      o = orderHome.findOrdersByQuery17();
      expectedPKs = new String[Schema.NUMOFORDERS];
      for (int i = 0; i < Schema.NUMOFORDERS; i++)
        expectedPKs[i] = Integer.toString(i + 1);
      if (!Util.checkEJBs(o, Schema.ORDERREF, expectedPKs)) {
        TestUtil
            .logErr("UnSuccessfully found all orders with line item members");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully found orders with line item members");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest32: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest32 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest32 failed");
  }

  /*
   * @testName: whereTest33
   * 
   * @assertion_ids: EJB:SPEC:363.3
   * 
   * @test_Strategy: Execute the ejbSelectSampleLineItems method using the
   * comparison operator NOT MEMBER OF in a collection member expression with
   * input parameter. Verify the results were accurately returned.
   */

  public void whereTest33() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection o = null;
    LineItemDVC liDvc = null;
    try {
      TestUtil.logMsg(
          "Find orders whose orders are do NOT contain the specified line items");
      liDvc = Schema.lineItemDVC[29];
      o = orderHome.selectSampleLineItems(liDvc);
      expectedPKs = new String[15];
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

      if (!Util.checkEJBs(o, Schema.ORDERREF, expectedPKs)) {
        TestUtil.logErr(
            "UnSuccessfully found orders NOT containing specified line item");
        pass = false;
      } else {
        TestUtil.logMsg(
            "Successfully found orders NOT containing specified line item");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest33: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest33 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest33 failed");
  }

  /*
   * @testName: whereTest34
   * 
   * @assertion_ids: EJB:SPEC:363.1
   * 
   * @test_Strategy: Execute the findOrdersByQuery18 method using the comparison
   * operator MEMBER OF in a collection member expression using
   * single_valued_navigation. Verify the results were accurately returned.
   */

  public void whereTest34() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection o = null;
    try {
      TestUtil.logMsg("Find orders who have Samples in their orders");
      o = orderHome.findOrdersByQuery18();
      expectedPKs = new String[2];
      expectedPKs[0] = "1";
      expectedPKs[1] = "6";
      if (!Util.checkEJBs(o, Schema.ORDERREF, expectedPKs)) {
        TestUtil.logErr("UnSuccessfully found orders with Samples");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully found orders with Samples");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest34: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest34 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest34 failed");
  }

  /*
   * @testName: whereTest35
   * 
   * @assertion_ids: EJB:SPEC:352
   * 
   * @test_Strategy: Execute the findOrdersByQuery19 method using comparison
   * operator NOT IN in a comparison expression within the WHERE clause where
   * the value for the cmp_path_expression contains numeric values. Verify the
   * results were accurately returned.
   */

  public void whereTest35() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection o = null;
    try {
      TestUtil.logMsg(
          "Find all orders which contain lineitems not of quantities 1 or 5");
      o = orderHome.findOrdersByQuery19();
      expectedPKs = new String[5];
      expectedPKs[0] = "10";
      expectedPKs[1] = "12";
      expectedPKs[2] = "14";
      expectedPKs[3] = "15";
      expectedPKs[4] = "16";
      if (!Util.checkEJBs(o, Schema.ORDERREF, expectedPKs)) {
        TestUtil.logErr(
            "UnSuccessfully found all orders with lineitem quantities not of 1 or 5");
        pass = false;
      } else {
        TestUtil.logMsg(
            "Successfully found all orders with lineitems of quantities not of 1 or 5");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest35: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest35 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest35 failed");
  }

  /*
   * @testName: whereTest36
   * 
   * @assertion_ids: EJB:SPEC:352
   * 
   * @test_Strategy: Execute the findCustomersByQuery37 method using comparison
   * operator IN in a comparison expression within the WHERE clause where the
   * value for the IN expression is an input parameter. Verify the results were
   * accurately returned.
   */

  public void whereTest36() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection c = null;
    try {
      TestUtil.logMsg("Find all customers whose lives in city Attleboro");
      c = customerHome.findCustomersByQuery37("Attleboro");
      expectedPKs = new String[1];
      expectedPKs[0] = "13";
      if (!Util.checkEJBs(c, Schema.CUSTOMERREF, expectedPKs)) {
        TestUtil
            .logErr("UnSuccessfully found all customers living in Attleboro");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully found all customers living in Attleboro");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest36: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest36 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest36 failed");
  }

  /*
   * @testName: whereTest37
   * 
   * @assertion_ids: EJB:SPEC:354
   * 
   * @test_Strategy: Execute two methods using the comparison operator IN in a
   * comparison expression within the WHERE clause and verify the results of the
   * two queries are equivalent regardless of the way the expression is
   * composed.
   */

  public void whereTest37() throws Fault {
    boolean pass1 = true;
    boolean pass2 = true;
    String expectedPKs[] = null;
    String expectedPKs2[] = null;
    Collection c1 = null;
    Collection c2 = null;

    try {
      TestUtil.logMsg(
          "Execute two queries composed differently and verify results");
      c1 = customerHome.findCustomersByQuery33();
      expectedPKs = new String[4];
      expectedPKs[0] = "5";
      expectedPKs[1] = "6";
      expectedPKs[2] = "12";
      expectedPKs[3] = "14";

      c2 = customerHome.findCustomersByQuery34();
      expectedPKs2 = new String[4];
      expectedPKs2[0] = "5";
      expectedPKs2[1] = "6";
      expectedPKs2[2] = "12";
      expectedPKs2[3] = "14";

      if (!Util.checkEJBs(c1, Schema.CUSTOMERREF, expectedPKs)) {
        TestUtil.logErr(
            "ERROR: findCustomersByQuery33 returned unexpected results");
        pass1 = false;
      } else {
        TestUtil.logMsg("findCustomersByQuery33 returned expected results");
      }

      if (!Util.checkEJBs(c2, Schema.CUSTOMERREF, expectedPKs2)) {
        TestUtil.logErr(
            "ERROR: findCustomersByQuery34 returned unexpected results");
        pass2 = false;
      } else {
        TestUtil.logMsg("findCustomersByQuery34 returned expected results");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest37: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest37 failed", e);
    }

    if (!pass1 || !pass2)
      throw new Fault("whereTest37 failed");
  }

  /*
   * @testName: whereTest38
   * 
   * @assertion_ids: EJB:SPEC:355; EJB:SPEC:348.1
   * 
   * @test_Strategy: Execute two methods using the comparison operator NOT IN in
   * a comparison expression within the WHERE clause and verify the results of
   * the two queries are equivalent regardless of the way the expression is
   * composed.
   * 
   */

  public void whereTest38() throws Fault {
    boolean pass1 = true;
    boolean pass2 = true;
    String expectedPKs[] = null;
    String expectedPKs2[] = null;
    Collection c1 = null;
    Collection c2 = null;
    try {
      TestUtil.logMsg(
          "Execute two queries composed differently and verify results");
      c1 = customerHome.findCustomersByQuery35();
      expectedPKs = new String[10];
      expectedPKs[0] = "1";
      expectedPKs[1] = "2";
      expectedPKs[2] = "3";
      expectedPKs[3] = "4";
      expectedPKs[4] = "7";
      expectedPKs[5] = "8";
      expectedPKs[6] = "9";
      expectedPKs[7] = "10";
      expectedPKs[8] = "11";
      expectedPKs[9] = "13";

      c2 = customerHome.findCustomersByQuery36();
      expectedPKs2 = new String[10];
      expectedPKs2[0] = "1";
      expectedPKs2[1] = "2";
      expectedPKs2[2] = "3";
      expectedPKs2[3] = "4";
      expectedPKs2[4] = "7";
      expectedPKs2[5] = "8";
      expectedPKs2[6] = "9";
      expectedPKs2[7] = "10";
      expectedPKs2[8] = "11";
      expectedPKs2[9] = "13";

      if (!Util.checkEJBs(c1, Schema.CUSTOMERREF, expectedPKs)) {
        TestUtil.logErr(
            "ERROR: findCustomersByQuery35 returned unexpected results");
        pass1 = false;
      } else {
        TestUtil.logMsg("findCustomersByQuery35 returned expected results");
      }

      if (!Util.checkEJBs(c2, Schema.CUSTOMERREF, expectedPKs2)) {
        TestUtil.logErr(
            "ERROR: findCustomersByQuery36 returned unexpected results");
        pass2 = false;
      } else {
        TestUtil.logMsg("findCustomersByQuery36 returned expected resuts");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest38: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest38 failed", e);
    }

    if (!pass1 || !pass2)
      throw new Fault("whereTest38 failed");
  }

  /*
   * @testName: whereTest39
   * 
   * @assertion_ids: EJB:SPEC:350
   * 
   * @test_Strategy: Execute two methods using the comparison operator BETWEEN
   * in a comparison expression within the WHERE clause and verify the results
   * of the two queries are equivalent regardless of the way the expression is
   * composed.
   */

  public void whereTest39() throws Fault {
    boolean pass1 = true;
    boolean pass2 = true;
    String expectedPKs[] = null;
    String expectedPKs2[] = null;
    Collection p1 = null;
    Collection p2 = null;
    try {
      TestUtil.logMsg(
          "Execute two queries composed differently and verify results");
      p1 = productHome.findProductsByQuery2();
      expectedPKs = new String[3];
      expectedPKs[0] = "8";
      expectedPKs[1] = "9";
      expectedPKs[2] = "17";

      p2 = productHome.findProductsByQuery3();
      expectedPKs2 = new String[3];
      expectedPKs2[0] = "8";
      expectedPKs2[1] = "9";
      expectedPKs2[2] = "17";

      if (!Util.checkEJBs(p1, Schema.PRODUCTREF, expectedPKs)) {
        TestUtil
            .logErr("ERROR:  findProductsByQuery2 returned unexpected results");
        pass1 = false;
      } else {
        TestUtil.logMsg("findProductsByQuery2 returned expected results");
      }

      if (!Util.checkEJBs(p2, Schema.PRODUCTREF, expectedPKs2)) {
        TestUtil
            .logErr("ERROR:  findProductsByQuery3 returned unexpected results");
        pass2 = false;
      } else {
        TestUtil.logMsg("findProductsByQuery3 returned expected results");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest39: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest39 failed", e);
    }

    if (!pass1 || !pass2)
      throw new Fault("whereTest39 failed");
  }

  /*
   * @testName: whereTest40
   * 
   * @assertion_ids: EJB:SPEC:350
   * 
   * @test_Strategy: Execute two methods using the comparison operator NOT
   * BETWEEN in a comparison expression within the WHERE clause and verify the
   * results of the two queries are equivalent regardless of the way the
   * expression is composed.
   */

  public void whereTest40() throws Fault {
    boolean pass1 = true;
    boolean pass2 = true;
    String expectedPKs[] = null;
    String expectedPKs2[] = null;
    Collection p1 = null;
    Collection p2 = null;

    try {
      TestUtil.logMsg(
          "Execute two queries composed differently and verify results");
      p1 = productHome.findProductsByQuery4();
      expectedPKs = new String[15];
      expectedPKs[0] = "1";
      expectedPKs[1] = "2";
      expectedPKs[2] = "3";
      expectedPKs[3] = "4";
      expectedPKs[4] = "5";
      expectedPKs[5] = "6";
      expectedPKs[6] = "7";
      expectedPKs[7] = "10";
      expectedPKs[8] = "11";
      expectedPKs[9] = "12";
      expectedPKs[10] = "13";
      expectedPKs[11] = "14";
      expectedPKs[12] = "15";
      expectedPKs[13] = "16";
      expectedPKs[14] = "18";

      p2 = productHome.findProductsByQuery5();
      expectedPKs2 = new String[15];
      expectedPKs2[0] = "1";
      expectedPKs2[1] = "2";
      expectedPKs2[2] = "3";
      expectedPKs2[3] = "4";
      expectedPKs2[4] = "5";
      expectedPKs2[5] = "6";
      expectedPKs2[6] = "7";
      expectedPKs2[7] = "10";
      expectedPKs2[8] = "11";
      expectedPKs2[9] = "12";
      expectedPKs2[10] = "13";
      expectedPKs2[11] = "14";
      expectedPKs2[12] = "15";
      expectedPKs2[13] = "16";
      expectedPKs2[14] = "18";

      if (!Util.checkEJBs(p1, Schema.PRODUCTREF, expectedPKs)) {
        TestUtil
            .logErr("ERROR: findProductsByQuery4 returned unexpected results");
        pass1 = false;
      } else {
        TestUtil.logMsg("findProductsByQuery4 returned expected results");
      }

      if (!Util.checkEJBs(p2, Schema.PRODUCTREF, expectedPKs2)) {
        TestUtil
            .logErr("ERROR: findProductsByQuery5 returned unexpected results");
        pass2 = false;
      } else {
        TestUtil.logMsg("findProductsByQuery5 returned expected results");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest40: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest40 failed", e);
    }

    if (!pass1 || !pass2)
      throw new Fault("whereTest40 failed");
  }

  /*
   * @testName: whereTest41
   * 
   * @assertion_ids: EJB:SPEC:369.7
   * 
   * @test_Strategy: Execute the findProductsByQuery6 method which includes the
   * arithmetic function MOD in a functional expression within the WHERE clause.
   * Verify the results were accurately returned.
   */

  public void whereTest41() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection p = null;

    try {
      TestUtil.logMsg("Find orders that have the quantity of 50 available");
      p = productHome.findProductsByQuery6();
      expectedPKs = new String[1];
      expectedPKs[0] = "5";
      if (!Util.checkEJBs(p, Schema.PRODUCTREF, expectedPKs)) {
        TestUtil.logErr("UnSuccessfully found all products with quantity 50");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully found all products of quantity 50");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest41: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest41 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest41 failed");
  }

  /*
   * @testName: whereTest42
   * 
   * @assertion_ids: EJB:SPEC:290
   * 
   * @test_Strategy: Execute the overloaded findOrdersByQuery21. Use variable
   * for finder argument. Verify the results were accurately returned.
   */

  public void whereTest42() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection o = null;
    double dbl = 2500;

    try {
      TestUtil.logMsg(
          "Find orders specifying creditcard type and credit card balance");
      o = orderHome.findOrdersByQuery21("VISA", dbl);
      expectedPKs = new String[1];
      expectedPKs[0] = "3";
      if (!Util.checkEJBs(o, Schema.ORDERREF, expectedPKs)) {
        TestUtil.logErr("UnSuccessfully found expected orders");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully found expected orders");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest42: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest42 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest42 failed");
  }

  /*
   * @testName: whereTest43
   * 
   * @assertion_ids: EJB:SPEC:290
   * 
   * @test_Strategy: Execute the overloaded findOrdersByQuery21. Verify the
   * correct query results are returned.
   */

  public void whereTest43() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection o = null;

    try {
      TestUtil.logMsg("Find orders specifying creditcard type ONLY");
      o = orderHome.findOrdersByQuery21("AXP");
      expectedPKs = new String[4];
      expectedPKs[0] = "8";
      expectedPKs[1] = "9";
      expectedPKs[2] = "10";
      expectedPKs[3] = "13";
      if (!Util.checkEJBs(o, Schema.ORDERREF, expectedPKs)) {
        TestUtil.logErr("UnSuccessfully found expected orders");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully found expected orders");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest43: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest43 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest43 failed");
  }

  /*
   * @testName: whereTest44
   * 
   * @assertion_ids: EJB:SPEC:369.6
   * 
   * @test_Strategy: Execute the findCustomersByQuery42 method which includes
   * the arithmetic function SQRT in a functional expression within the WHERE
   * clause. Verify the results were accurately returned.
   */

  public void whereTest44() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection c = null;
    double dbl = 50;

    try {
      TestUtil.logMsg("Find customers with specific credit card balance");
      c = customerHome.findCustomersByQuery42(dbl);
      expectedPKs = new String[1];
      expectedPKs[0] = "3";

      if (!Util.checkEJBs(c, Schema.CUSTOMERREF, expectedPKs)) {
        TestUtil.logErr("UnSuccessfully returned expected results");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully returned expected results");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest44: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest44 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest44 failed");
  }

  /*
   * @testName: whereTest45
   * 
   * @assertion_ids: EJB:SPEC:358
   * 
   * @test_Strategy: Execute the findCustomersByQuery44 method using the
   * comparison operator LIKE in a comparison expression within the WHERE clause
   * using percent (%) to wild card any expression including the optional ESCAPE
   * syntax. Verify the results were accurately returned.
   *
   */

  public void whereTest45() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection c = null;
    try {
      TestUtil.logMsg(
          "Find all customers with an alias that contains an underscore");
      c = customerHome.findCustomersByQuery44();
      expectedPKs = new String[1];
      expectedPKs[0] = "3";
      if (!Util.checkEJBs(c, Schema.CUSTOMERREF, expectedPKs)) {
        TestUtil.logErr(
            "UnSuccessfully found all customers with an alias LIKE: %_%");
        pass = false;
      } else {
        TestUtil
            .logMsg("Successfully found all customers with an alias LIKE: %_%");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception whereTest45: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("whereTest45 failed", e);
    }

    if (!pass)
      throw new Fault("whereTest45 failed");
  }

  public void cleanup() throws Fault {
    TestUtil.logMsg("cleanup ok");
  }
}
