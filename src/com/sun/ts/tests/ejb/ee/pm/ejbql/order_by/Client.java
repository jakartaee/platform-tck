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
 * @(#)Client.java	1.5 03/05/16
 */

package com.sun.ts.tests.ejb.ee.pm.ejbql.order_by;

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
   * @testName: orderByTest1
   * 
   * @assertion_ids: EJB:SPEC:399.2; EJB:SPEC:404
   * 
   * @test_Strategy: Execute a query where the select clause denotes a
   * collection of orders using an identification variable and ordered by the
   * order's total price for the customer defined as the argument to the query
   * method.
   */

  public void orderByTest1() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection o = null;
    try {
      TestUtil
          .logMsg("Find Orders for Kellie A. Sanborn ordered by Total Price");
      o = orderHome.findOrdersByPrice("Kellie A. Sanborn");
      expectedPKs = new String[2];
      expectedPKs[0] = "15";
      expectedPKs[1] = "16";

      if (!Util.checkEJBs(o, Schema.ORDERREF, expectedPKs)) {
        TestUtil
            .logErr("UnSuccessfully found all orders for Kellie A. Sanborn");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully found all orders for Kellie A. Sanborn");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception orderByTest1: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("orderByTest1 failed", e);
    }

    if (!pass)
      throw new Fault("orderByTest1 failed");
  }

  /*
   * @testName: orderByTest2
   * 
   * @assertion_ids: EJB:SPEC:399.3; EJB:SPEC:401
   * 
   * @test_Strategy: Execute a query where the select clause denotes a
   * cmp_path_expression and the query is ordered by same. Ascending order is
   * expected as the ordering is not explictly specified and ascending is
   * default.
   *
   */

  public void orderByTest2() throws Fault {
    boolean pass = false;
    Collection p = null;
    Long[] expectedPartNumbers = new Long[] { new Long(0L),
        new Long(123456789L), new Long(219876543L), new Long(234567891L),
        new Long(321987654L), new Long(345678912L), new Long(432198765L),
        new Long(456789123L), new Long(543219876L), new Long(567891234L),
        new Long(654321987L), new Long(678912345L), new Long(765432198L),
        new Long(789123456L), new Long(876543219L), new Long(891234567L),
        new Long(912345678L), new Long(987654321L) };

    try {
      TestUtil.logMsg("Select all part numbers in ascending order");
      p = productHome.selectProductsByPartNumber();
      Long[] result = (Long[]) (p.toArray(new Long[p.size()]));
      TestUtil.logMsg("Compare results of part numbers");
      pass = Arrays.equals(expectedPartNumbers, result);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception orderByTest2: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("orderByTest2 failed", e);
    }
    if (!pass)
      throw new Fault("orderByTest2 failed");
  }

  /*
   * @testName: orderByTest3
   * 
   * @assertion_ids: EJB:SPEC:329; EJB:SPEC:399.1; EJB:SPEC:401
   * 
   * @test_Strategy: Execute a query where the select clause denotes a
   * single_valued_cmr_path expression and the query is ordered using same.
   * Keyword ASC is used to return results in Ascending order.
   * 
   */

  public void orderByTest3() throws Fault {
    boolean pass = false;
    Collection o = null;
    Double[] expectedBalances = new Double[] { new Double(500D),
        new Double(750D), new Double(1000D), new Double(1400D),
        new Double(1500D), new Double(2000D), new Double(2500D),
        new Double(4400D), new Double(5000D), new Double(5500D),
        new Double(7000D), new Double(8000D), new Double(13000D),
        new Double(15000D), new Double(23000D) };

    try {
      TestUtil.logMsg("Find credit card balances in ascending order");
      o = orderHome.selectCreditCardBalances();
      Double[] result = (Double[]) (o.toArray(new Double[o.size()]));

      TestUtil.logMsg("Compare results of credit card balances received");
      pass = (Arrays.equals(expectedBalances, result));
    } catch (Exception e) {
      TestUtil.logErr("Caught exception orderByTest3: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("orderByTest3 failed", e);
    }
    if (!pass)
      throw new Fault("orderByTest3 failed");
  }

  /*
   * @testName: orderByTest4
   * 
   * @assertion_ids: EJB:SPEC:402
   * 
   * @test_Strategy: Execute a query where the select clause denotes a
   * collection of products which are ordered by the available quantity using
   * the keyword DESC to obtain the result set from of products with highest
   * available quantity to the lowest.
   * 
   */

  public void orderByTest4() throws Fault {
    boolean pass = true;
    Collection p = null;
    String expectedPKs[] = null;

    try {
      TestUtil.logMsg("Obtain a list of orders with the highest total price");
      p = productHome.findProductsByHighestQuantity();
      expectedPKs = new String[Schema.NUMOFPRODUCTS];
      for (int i = 0; i < Schema.NUMOFPRODUCTS; i++)
        expectedPKs[i] = Integer.toString(i + 1);

      if (!Util.checkEJBs(p, Schema.PRODUCTREF, expectedPKs)) {
        TestUtil.logErr(
            "UnSuccessfully returned all product with highest quantity");
        pass = false;
      } else {
        TestUtil.logMsg(
            "Successfully found all products and returned result set in descending order");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception orderByTest4: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("orderByTest4 failed", e);
    }

    if (!pass)
      throw new Fault("orderByTest4 failed");
  }

  /*
   * @testName: orderByTest5
   * 
   * @assertion_ids: EJB:SPEC:400
   * 
   * @test_Strategy: Execute a query where the select clause denotes a
   * collection of aliases which are ordered by multiple orderby_item elements.
   * The expected result set is ordered from left-to-right.
   *
   */

  public void orderByTest5() throws Fault {
    boolean pass = true;
    Collection a = null;
    String expectedPKs[] = null;

    try {
      TestUtil.logMsg(
          "Obtain a list of aliases orderedy by alias name and alias id");
      a = aliasHome.findCustomerAliasesByOrder();
      expectedPKs = new String[27];
      expectedPKs[0] = "5";
      expectedPKs[1] = "1";
      expectedPKs[2] = "2";
      expectedPKs[3] = "6";
      expectedPKs[4] = "11";
      expectedPKs[5] = "24";
      expectedPKs[6] = "10";
      expectedPKs[7] = "21";
      expectedPKs[8] = "3";
      expectedPKs[9] = "19";
      expectedPKs[10] = "20";
      expectedPKs[11] = "27";
      expectedPKs[12] = "26";
      expectedPKs[13] = "23";
      expectedPKs[14] = "28";
      expectedPKs[15] = "9";
      expectedPKs[16] = "7";
      expectedPKs[17] = "8";
      expectedPKs[18] = "22";
      expectedPKs[19] = "17";
      expectedPKs[20] = "29";
      expectedPKs[21] = "12";
      expectedPKs[22] = "18";
      expectedPKs[23] = "13";
      expectedPKs[24] = "14";
      expectedPKs[25] = "25";
      expectedPKs[26] = "4";

      if (!Util.checkEJBs(a, Schema.ALIASREF, expectedPKs)) {
        TestUtil.logErr("UnSuccessfully returned expected aliases");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully found all aliases");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception orderByTest5: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("orderByTest5 failed", e);
    }

    if (!pass)
      throw new Fault("orderByTest5 failed");
  }

  public void cleanup() throws Fault {
    TestUtil.logMsg("cleanup ok");
  }
}
