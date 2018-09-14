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
 * @(#)Client.java	1.28 03/05/16
 */

package com.sun.ts.tests.ejb.ee.pm.ejbql.select_clause;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import com.sun.ts.tests.ejb.ee.pm.ejbql.schema.*;

import java.io.*;
import java.util.*;
import java.lang.*;
import java.math.*;
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
   * @testName: selectTest1
   * 
   * @assertion_ids: EJB:SPEC:313.4; EJB:SPEC:265; EJB:SPEC:314; EJB:SPEC:314.1
   * 
   * @test_Strategy: Return local interface type by executing the
   * "findCustomerByHomePhone" method which is defined on the local home
   * interface thus returning an EJBLocalObject.
   */

  public void selectTest1() throws Fault {
    boolean pass = true;
    Customer c = null;
    try {
      TestUtil.logMsg("Find Customer By Home Phone Number");
      c = customerHome.getCustomerByHomePhoneNumber("223-8888");
      if (!Util.checkEJB(c, "3")) {
        TestUtil.logErr("UnSuccessfully found customer Shelly D. Mcgowan");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully found customer Shelly D. Mcgowan");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception selectTest1: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("selectTest1 failed", e);
    }

    if (!pass)
      throw new Fault("selectTest1 failed");
  }

  /*
   * @testName: selectTest2
   * 
   * @assertion_ids: EJB:SPEC:414; EJB:SPEC:291; EJB:SPEC:292; EJB:SPEC:373
   * 
   * @test_Strategy: Execute the ejbSelectHomeAddress method and ensure that a
   * reference of an EJBLocalObject has been returned. The return-type-mapping
   * element is defined as LOCAL.
   *
   */

  public void selectTest2() throws Fault {
    boolean pass = true;
    AddressDVC expAddressDVC = null;
    AddressDVC a = null;

    try {
      TestUtil
          .logMsg("Find the home address for Alan E. Frechette via ejbSelect");
      expAddressDVC = Schema.addressDVC[0];
      a = customerHome.selectHomeAddress();
      if (!Util.checkAddressDVC(a, expAddressDVC)) {
        TestUtil.logErr(
            "UnSuccessfully found the home address for Alan E. Frechette");
        pass = false;
      } else {
        TestUtil.logMsg(
            "Successfully found the home address for Alan E. Frechette");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception selectTest2: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("selectTest2 failed", e);
    }
    if (!pass)
      throw new Fault("selectTest2 failed");
  }

  /*
   * @testName: selectTest3
   * 
   * @assertion_ids: EJB:SPEC:371; EJB:SPEC:276; EJB:SPEC:282; EJB:SPEC:293;
   * EJB:SPEC:313.1
   * 
   * @test_Strategy: If the result type of an ejbSelectMETHOD is java.util.Set
   * it is the responsibility of the Persistence Manager to ensure that
   * duplicates are removed even if the keyword DISTINCT is not used within the
   * SELECT clause. Execute the ejbSelectHomeZipCodesByCity method and ensure
   * the query returns zip codes without duplicates.
   * 
   */

  public void selectTest3() throws Fault {
    boolean pass = true;
    Set s = null;

    try {
      TestUtil.logMsg("Find home zip codes via ejbSelectHomeZipCodesByCity");
      s = customerHome.selectHomeZipCodesByCity("Peabody");
      String expectedZip = "88444";
      if (s.size() != 1) {
        TestUtil.logErr("selectHomeZipCodesByCity returned " + s.size()
            + " references, expected 1 reference");
        pass = false;
      } else {
        if (!s.contains(expectedZip)) {
          TestUtil.logErr(
              "selectHomeZipCodesByCity returned " + s + " expected: 88444");
          pass = false;
        } else {
          TestUtil.logMsg("Successfully removed duplicate zip codes ");
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception selectTest3: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("selectTest3 failed", e);
    }
    if (!pass)
      throw new Fault("selectTest3 failed");
  }

  /*
   * @testName: selectTest4
   * 
   * @assertion_ids: EJB:SPEC:313.2
   * 
   * @test_Strategy: The result type of a select method can be a collection of
   * cmp-field values. Execute the ejbSelectAllHomeZipCodesByCity and ensure
   * that the query returns expected zip codes.
   * 
   */

  public void selectTest4() throws Fault {
    boolean pass = true;
    Collection c = null;

    try {
      TestUtil
          .logMsg("Find all home zip codes via ejbSelectAllHomeZipCodesByCity");
      c = customerHome.selectAllHomeZipCodesByCity("Peabody");
      String expectedZip1 = "88444";
      String expectedZip2 = "88444";
      if (c.size() != 2) {
        TestUtil.logErr("selectHomeZipCodesByCity returned " + c.size()
            + " references, expected 2 references");
        pass = false;
      } else {
        Iterator i = c.iterator();
        String s1 = (String) i.next();
        String s2 = (String) i.next();
        if (!expectedZip1.equals(s1) && !expectedZip2.equals(s2)) {
          TestUtil.logErr("selectAllHomeZipCodesByCity returned " + expectedZip1
              + "," + expectedZip2 + "expected: 88444, 88444");
          pass = false;
        } else {
          TestUtil
              .logMsg("Successfully returned duplicate zip codes as expected");
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception selectTest4: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("selectTest4 failed", e);
    }
    if (!pass)
      throw new Fault("selectTest4 failed");
  }

  /*
   * @testName: selectTest5
   * 
   * @assertion_ids: EJB:SPEC:320; EJB:SPEC:313.7; EJB:SPEC:370
   * 
   * @test_Strategy: Find all orders whose totalPrice is greater than that of
   * the specified customer by executing the findAllOrdersWithGreaterPrice
   * query.
   * 
   */

  public void selectTest5() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection o = null;

    try {
      TestUtil.logMsg("Find all orders in OrderEJB");
      o = orderHome.findAllOrdersWithGreaterPrice();
      expectedPKs = new String[4];
      expectedPKs[0] = "2";
      expectedPKs[1] = "5";
      expectedPKs[2] = "11";
      expectedPKs[3] = "16";

      if (!Util.checkEJBs(o, Schema.ORDERREF, expectedPKs)) {
        TestUtil.logErr(
            "UnSuccessfully found all orders with a greater total price");
        pass = false;
      } else {
        TestUtil
            .logMsg("Successfully found all orders with a greater total price");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception selectTest5: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("selectTest5 failed", e);
    }
    if (!pass)
      throw new Fault("selectTest5 failed");
  }

  /*
   * @testName: selectTest6
   * 
   * @assertion_ids: EJB:SPEC:414; EJB:SPEC:277
   * 
   * @test_Strategy: Return all customer references by executing the overloaded
   * ejbSelectCustomersByAlias method and ensure that remote references to have
   * been returned. The return-type-mapping element is defined as REMOTE.
   */

  public void selectTest6() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection c = null;

    try {
      TestUtil.logMsg("Find all customers with alias: fish");
      c = customerHome.selectCustomersByAlias("fish");
      expectedPKs = new String[2];
      expectedPKs[0] = "1";
      expectedPKs[1] = "2";
      if (!Util.checkEJBs(c, Schema.CUSTOMERREF, expectedPKs)) {
        TestUtil.logErr("UnSuccessfully found all customers with alias: fish");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully found customers with alias: fish");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception selectTest6: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("selectTest6 failed", e);
    }
    if (!pass)
      throw new Fault("selectTest6 failed");
  }

  /*
   * @testName: selectTest7
   * 
   * @assertion_ids: EJB:SPEC:372
   * 
   * @test_Strategy: The SELECT clause must be specified to return a
   * single-valued expression. Find all line items related to an order.
   * 
   */

  public void selectTest7() throws Fault {
    boolean pass = true;
    LineItemDVC expectedDVCs[] = null;
    Collection o = null;

    try {
      TestUtil.logMsg("Find all line items related to the orders");
      o = orderHome.selectAllLineItems();
      expectedDVCs = new LineItemDVC[Schema.NUMOFLINEITEMS];
      for (int i = 0; i < Schema.NUMOFLINEITEMS; i++)
        expectedDVCs[i] = Schema.lineItemDVC[i];
      if (!Util.checkLineItemDVCs(o, expectedDVCs)) {
        TestUtil.logErr("UnSuccessfully found all line items");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully found all line items");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception selectTest7: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("selectTest7 failed", e);
    }
    if (!pass)
      throw new Fault("selectTest7 failed");

  }

  /*
   * @testName: selectTest8
   * 
   * @assertion_ids: EJB:SPEC:313.5
   * 
   * @test_Strategy: Find all work addresses by executing the
   * ejbSelectAllWorkAddresses method.
   * 
   */

  public void selectTest8() throws Fault {
    boolean pass = true;
    AddressDVC expAddressDVCs[] = null;
    Collection c = null;

    try {
      TestUtil.logMsg("Find all work addresses via ejbSelect");
      expAddressDVCs = setExpectedWorkAddresses();
      c = customerHome.selectAllWorkAddresses();

      if (!Util.checkAddressDVCs(c, expAddressDVCs)) {
        TestUtil.logErr("UnSuccessfully found all work addresses");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully found all work addresses");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception selectTest8: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("selectTest8 failed", e);
    }

    if (!pass)
      throw new Fault("selectTest8 failed");
  }

  /*
   * @testName: selectTest9
   * 
   * @assertion_ids: EJB:SPEC:313.5; EJB:SPEC:149
   * 
   * @test_Strategy: Return local interface type by executing the select method,
   * ejbSelectPhonesByArea, returning a Collection of local references. The
   * return-type-mapping element is set to LOCAL.
   *
   * 
   */

  public void selectTest9() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection c = null;

    try {
      TestUtil.logMsg("Find customers by home phone area code: 603");
      c = customerHome.selectPhonesByArea("603");
      expectedPKs = new String[2];
      expectedPKs[0] = "5";
      expectedPKs[1] = "6";
      if (!Util.checkEJBs(c, Schema.CUSTOMERREF, expectedPKs)) {
        TestUtil.logErr(
            "UnSuccessfully found all customers with the 603 area code");
        pass = false;
      } else {
        TestUtil
            .logMsg("Successfully found all customers with the 603 area code");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception selectTest9: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("selectTest9 failed", e);
    }
    if (!pass)
      throw new Fault("selectTest9 failed");

  }

  /*
   * @testName: selectTest10
   * 
   * @assertion_ids: EJB:SPEC:371; EJB:SPEC:374
   * 
   * @test_Strategy: Execute the ejbSelectCustomerAddressBySet which includes
   * the DISTINCT keyword in its' query and ensure the query returns work
   * addresses without duplicates as expected.
   * 
   */

  public void selectTest10() throws Fault {
    boolean pass = true;
    Set s = null;

    try {
      TestUtil.logMsg("Find Customer Addresses by Set");
      s = customerHome.selectCustomerAddressBySet("MA");
      String expectedAddr = "1 Network Drive";
      if (s.size() != 1) {
        TestUtil.logErr("selectCustomerAddressBySet returned " + s.size()
            + " references, expected 1 reference");
        pass = false;
      } else {
        if (!s.contains(expectedAddr)) {
          TestUtil.logErr("selectCustomerAddressBySet returned " + s
              + " expected: 1 Network Drive");
          pass = false;
        } else {
          TestUtil.logMsg("Successfully returned addresses without duplicates");
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception selectTest10: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("selectTest10 failed", e);
    }
    if (!pass)
      throw new Fault("selectTest10 failed");
  }

  /*
   * @testName: selectTest11
   * 
   * @assertion_ids: EJB:SPEC:374
   * 
   * @test_Strategy: If the result type of an ejbSelectMETHOD is
   * java.util.Collection and the reserved word DISTINCT is used, duplicates
   * will be removed. Execute the ejbSelectCustomerAddressByCollection method
   * and ensure that the query returns the work addresses without duplicates.
   *
   */

  public void selectTest11() throws Fault {
    boolean pass = true;
    Collection c = null;

    try {
      TestUtil
          .logMsg("Find addresses via ejbSelectCustomerAddressByCollection");
      c = customerHome.selectCustomerAddressByCollection("MA");
      String expectedAddr = "1 Network Drive";
      if (c.size() != 1) {
        TestUtil.logErr("selectCustomerAddressByCollection returned " + c.size()
            + " references, expected 1 reference");
        pass = false;
      } else {
        if (!c.contains(expectedAddr)) {
          TestUtil.logErr("selectCustomerAddressByCollection returned " + c
              + " expected: 1 Network Drive");
          pass = false;

        } else {
          TestUtil.logMsg(
              "Successfully returned addresses without duplicates as expected");
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception selectTest11: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("selectTest11 failed", e);
    }
    if (!pass)
      throw new Fault("selectTest11 failed");
  }

  /*
   * @testName: selectTest12
   * 
   * @assertion_ids: EJB:SPEC:314.2; EJB:SPEC:271; EJB:SPEC:226; EJB:SPEC:271;
   * EJB:SPEC:290
   * 
   * @test_Strategy: How the results type of a query is mapped depends on
   * whether the query is defined on a finder method on the remote interface,
   * for a finder on the local inteface, or for a select method. Query result
   * values that designate the abstract schema type of an entity bean will be
   * mapped to either entity bean local or remote interface types depending on
   * the query method. Return local interface type by executing the
   * "findCustomerByWorkCity" method which is defined on the local home
   * interface thus returning a Collection of local references.
   * 
   */

  public void selectTest12() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection c = null;
    try {
      TestUtil.logMsg("Find All Customers By Work City");
      c = customerHome.getCustomersByWorkCity("Burlington");
      expectedPKs = new String[Schema.NUMOFCUSTOMERS];
      for (int i = 0; i < Schema.NUMOFCUSTOMERS; i++)
        expectedPKs[i] = Integer.toString(i + 1);
      if (!Util.checkEJBs(c, Schema.CUSTOMERREF, expectedPKs)) {
        TestUtil.logErr(
            "UnSuccessfully found all customers working in City: Burlington");
        pass = false;
      } else {
        TestUtil.logMsg(
            "Successfully found all customers working in City: Burlington");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception selectTest12: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("selectTest12 failed", e);
    }

    if (!pass)
      throw new Fault("selectTest12 failed");
  }

  /*
   * @testName: selectTest13
   * 
   * @assertion_ids: EJB:SPEC:374
   * 
   * @test_Strategy: Execute the findCustomersByQuery30 method where the query
   * contains the reserved word DISTINCT and ensure the query returns customers
   * without duplicates.
   * 
   */

  public void selectTest13() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection c = null;

    try {
      TestUtil
          .logMsg("Find DISTINCT customers whose orders are less than $1250");
      c = customerHome.findCustomersByQuery30();
      expectedPKs = new String[11];
      expectedPKs[0] = "1";
      expectedPKs[1] = "3";
      expectedPKs[2] = "4";
      expectedPKs[3] = "6";
      expectedPKs[4] = "7";
      expectedPKs[5] = "8";
      expectedPKs[6] = "9";
      expectedPKs[7] = "11";
      expectedPKs[8] = "12";
      expectedPKs[9] = "13";
      expectedPKs[10] = "14";

      if (!Util.checkEJBs(c, Schema.CUSTOMERREF, expectedPKs)) {
        TestUtil.logErr(
            "UnSuccessfully found DISTINCT customers whose orders are less than $1250");
        pass = false;
      } else {
        TestUtil.logMsg(
            "Successfully found DISTINCT customers with orders less than $1250");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception selectTest13: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("selectTest13 failed", e);
    }
    if (!pass)
      throw new Fault("selectTest13 failed");
  }

  /*
   * @testName: selectTest15
   * 
   * @assertion_ids: EJB:SPEC:381; EJB:SPEC:313.3; EJB:SPEC:386; EJB:SPEC:406;
   * EJB:SPEC:406.1
   * 
   * @test_Strategy: Execute the single-object select method ejbSelectMinSingle
   * which contains the aggregate function MIN. Verify the results are
   * accurately returned.
   * 
   */

  public void selectTest15() throws Fault {
    boolean pass = true;
    String s1 = new String("4");
    String s2 = null;

    try {
      TestUtil.logMsg("Find MINIMUM order id for Robert E. Bissett");
      s2 = orderHome.selectMinSingle();
      if (s2.equals(s1)) {
        TestUtil.logMsg("Successfully returned expected results");
      } else {
        TestUtil.logErr("selectMinSingle returned " + s2 + "expected: " + s1);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception selectTest15: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("selectTest15 failed", e);
    }
    if (!pass)
      throw new Fault("selectTest15 failed");
  }

  /*
   * @testName: selectTest17
   * 
   * @assertion_ids: EJB:SPEC:382; EJB:SPEC:387; EJB:SPEC:406; EJB:SPEC:406.1
   * 
   * @test_Strategy: Execute the single-object select method ejbSelectMaxSingle
   * which contains the aggregate function MAX. Verify the results are
   * accurately returned.
   * 
   */

  public void selectTest17() throws Fault {
    boolean pass = true;
    int i1 = 8;
    int i2 = 0;

    try {
      TestUtil.logMsg(
          "Find MAXIMUM number of lineItem quantities available an order may have");
      i2 = orderHome.selectMaxSingle();
      if (i2 != i1) {
        TestUtil.logErr("selectMaxSingle returned:" + i2 + "expected: " + i1);
        pass = false;
      } else {
        TestUtil.logMsg("Successfully returned expected results");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception selectTest17: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("selectTest17 failed", e);
    }
    if (!pass)
      throw new Fault("selectTest17 failed");
  }

  /*
   * @testName: selectTest19
   * 
   * @assertion_ids: EJB:SPEC:380; EJB:SPEC:406; EJB:SPEC:406.1; EJB:SPEC:385
   * 
   * @test_Strategy: Execute the single-object select method ejbSelectAvgSingle
   * which contains the aggregate function AVG. Verify the results are
   * accurately returned.
   * 
   */

  public void selectTest19() throws Fault {
    boolean pass = true;
    double d1 = 1538.49;
    double d2 = 1538.50;

    try {
      TestUtil.logMsg("Find AVERAGE price of all orders");
      double d3 = orderHome.selectAvgSingle();
      if (((d3 >= d1) && (d3 < d2))) {
        TestUtil.logMsg("selectAvgSingle returned expected results: " + d1);
      } else {
        TestUtil.logErr("selectAvgSingle returned " + d3 + "expected: " + d1);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception selectTest19: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("selectTest19 failed", e);
    }
    if (!pass)
      throw new Fault("selectTest19 failed");
  }

  /*
   * @testName: selectTest21
   * 
   * @assertion_ids: EJB:SPEC:383; EJB:SPEC:406; EJB:SPEC:406.1; EJB:SPEC:388
   * 
   * @test_Strategy: Execute the single-object select method ejbSelectSumSingle
   * which contains the aggregate function SUM. Verify the results are
   * accurately returned.
   * 
   */

  public void selectTest21() throws Fault {
    boolean pass = true;
    double d1 = 9907.14;
    double d2 = 9907.15;

    try {
      TestUtil.logMsg("Find SUM of all product prices");
      double d3 = productHome.selectSumSingle();
      if (((d3 >= d1) && (d3 < d2))) {
        TestUtil.logMsg("selectSumSingle returned expected results: " + d1);
      } else {
        TestUtil.logErr("selectSumSingle returned " + d3 + "expected: " + d1);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception selectTest21: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("selectTest21 failed", e);
    }
    if (!pass)
      throw new Fault("selectTest21 failed");
  }

  /*
   * @testName: selectTest23
   * 
   * @assertion_ids: EJB:SPEC:384; EJB:SPEC:406; EJB:SPEC:406.4
   * 
   * @test_Strategy: Execute the single-object select method
   * ejbSelectCountSingle which contains the aggregate function Count. Verify
   * the results are accurately returned.
   * 
   */

  public void selectTest23() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    long l1 = 18;
    long l2 = 0;

    try {
      TestUtil.logMsg("Find the total number of products available");
      l2 = productHome.selectCountSingle();
      if (l1 != l2) {
        TestUtil.logErr("selectCountSingle returned " + l2 + "expected: " + l1);
        pass = false;
      } else {
        TestUtil.logMsg("Successfully returned expected results");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception selectTest23: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("selectTest23 failed", e);
    }
    if (!pass)
      throw new Fault("selectTest23 failed");
  }

  /*
   * @testName: selectTest25
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: Execute two queries of different context and ensure the
   * same cardinality results between the two.
   *
   */

  public void selectTest25() throws Fault {
    boolean pass = true;
    Collection c1 = null;
    Collection c2 = null;
    try {
      TestUtil.logMsg(
          "Execute two queries composed differently and verify results");
      c1 = customerHome.selectCustomersByNotNullWorkZipCode();

      c2 = customerHome.findCustomersByQuery26();

      if (c2.size() != c1.size()) {
        TestUtil.logErr("ERROR: Incorrect results received, Collection 1: "
            + c1.size() + "Collection 2: " + c2.size());
        pass = false;
      } else {
        TestUtil.logMsg("Both queries returned same cardinality");
        TestUtil.logTrace("Correct results received, Collection 1: " + c1.size()
            + "Collection 2: " + c2.size());

      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception selectTest25: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("selectTest25 failed", e);
    }
    if (!pass)
      throw new Fault("selectTest25 failed");
  }

  /*
   * @testName: selectTest26
   * 
   * @assertion_ids: EJB:SPEC:329
   * 
   * @test_Strategy: Find home addresses using a query to verify "inner join"
   * semantics.
   * 
   */

  public void selectTest26() throws Fault {
    boolean pass = true;
    AddressDVC expAddressDVCs[] = new AddressDVC[] { Schema.addressDVC[0],
        Schema.addressDVC[2] };
    Collection c = null;

    try {
      TestUtil.logMsg("Find all home addresses for customers like ette");
      c = customerHome.selectCustomersByQuery42();

      if (!Util.checkAddressDVCs(c, expAddressDVCs)) {
        TestUtil.logErr("UnSuccessfully found expected home addresses");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully found expected home addresses");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception selectTest26: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("selectTest26 failed", e);
    }

    if (!pass)
      throw new Fault("selectTest26 failed");
  }

  /*
   * @testName: selectTest28
   * 
   * @assertion_ids: EJB:SPEC:414; EJB:SPEC:277
   * 
   * @test_Strategy: Return all customer references by executing the
   * ejbSelectCustomersByAlias method which is overloaded and the correct
   * references to have been returned. The return-type-mapping element is
   * defined as REMOTE.
   */

  public void selectTest28() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection c = null;

    try {
      TestUtil.logMsg("Find all customers with alias: kellieann and id: 7");
      c = customerHome.selectCustomersByAlias("kellieann", "7");
      expectedPKs = new String[2];
      expectedPKs[0] = "3";
      expectedPKs[1] = "14";
      if (!Util.checkEJBs(c, Schema.CUSTOMERREF, expectedPKs)) {
        TestUtil.logErr("UnSuccessfully found expected customers");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully found customers");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception selectTest28: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("selectTest28 failed", e);
    }
    if (!pass)
      throw new Fault("selectTest28 failed");
  }

  public void cleanup() throws Fault {
    TestUtil.logMsg("cleanup ok");
  }

  private AddressDVC[] setExpectedHomeAddresses() {
    AddressDVC addressDVC[] = new AddressDVC[Schema.NUMOFHOMEADDRESSES];

    addressDVC[0] = Schema.addressDVC[0];
    addressDVC[1] = Schema.addressDVC[2];
    addressDVC[2] = Schema.addressDVC[4];
    addressDVC[3] = Schema.addressDVC[6];
    addressDVC[4] = Schema.addressDVC[8];
    addressDVC[5] = Schema.addressDVC[10];
    addressDVC[6] = Schema.addressDVC[12];
    addressDVC[7] = Schema.addressDVC[14];
    addressDVC[8] = Schema.addressDVC[16];
    addressDVC[9] = Schema.addressDVC[18];
    addressDVC[10] = Schema.addressDVC[20];
    addressDVC[11] = Schema.addressDVC[22];
    addressDVC[12] = Schema.addressDVC[24];
    addressDVC[13] = Schema.addressDVC[26];
    return addressDVC;
  }

  private AddressDVC[] setExpectedWorkAddresses() {
    AddressDVC addressDVC[] = new AddressDVC[Schema.NUMOFWORKADDRESSES];

    addressDVC[0] = Schema.addressDVC[1];
    addressDVC[1] = Schema.addressDVC[3];
    addressDVC[2] = Schema.addressDVC[5];
    addressDVC[3] = Schema.addressDVC[7];
    addressDVC[4] = Schema.addressDVC[9];
    addressDVC[5] = Schema.addressDVC[11];
    addressDVC[6] = Schema.addressDVC[13];
    addressDVC[7] = Schema.addressDVC[15];
    addressDVC[8] = Schema.addressDVC[17];
    addressDVC[9] = Schema.addressDVC[19];
    addressDVC[10] = Schema.addressDVC[21];
    addressDVC[11] = Schema.addressDVC[23];
    addressDVC[12] = Schema.addressDVC[25];
    addressDVC[13] = Schema.addressDVC[27];
    return addressDVC;
  }

}
