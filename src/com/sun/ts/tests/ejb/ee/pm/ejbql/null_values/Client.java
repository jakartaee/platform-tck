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

package com.sun.ts.tests.ejb.ee.pm.ejbql.null_values;

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
   * @testName: nullTest1
   * 
   * @assertion_ids: EJB:SPEC:359
   * 
   * @test_Strategy: Use the operator IS NULL in a null comparison expression
   * using a single_valued_path_expression. Execute the findCustomersByQuery14
   * method. Verify the results were accurately returned.
   */

  public void nullTest1() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection c = null;
    try {
      TestUtil.logMsg("Find All Customers who have a null relationship");
      c = customerHome.findCustomersByQuery14();
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
        TestUtil.logErr(
            "UnSuccessfully found customers who have a null relationship");
        pass = false;
      } else {
        TestUtil.logMsg(
            "Successfully found all customers have a null relationship");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception nullTest1: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("nullTest1 failed", e);
    }

    if (!pass)
      throw new Fault("nullTest1 failed");
  }

  /*
   * @testName: nullTest2
   * 
   * @assertion_ids: EJB:SPEC:359
   * 
   * @test_Strategy: Use the operator IS NOT NULL in a null comparision
   * expression within the WHERE CLAUSE where the relationship is NULL. Use a
   * single_valued_path_expression. Execute the findCustomersByQuery15 method.
   * Verify the results were accurately returned.
   * 
   */

  public void nullTest2() throws Fault {
    boolean pass = true;
    Collection c = null;
    try {
      TestUtil.logMsg("Find All Customers who do not have null relationship");
      c = customerHome.findCustomersByQuery15();
      if (c.size() != 0) {
        TestUtil.logErr("findCustomersByQuery15 returned " + c.size()
            + " references, expected 0 references");
        pass = false;
      } else {
        TestUtil.logMsg(
            "Successfully found all customers without null relationships");
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception nullTest2: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("nullTest2 failed", e);
    }

    if (!pass)
      throw new Fault("nullTest2 failed");
  }

  /*
   * @testName: nullTest3
   * 
   * @assertion_ids: EJB:SPEC:424
   * 
   * @test_Strategy: Define a query using Boolean operator AND in a conditional
   * test ( True AND True = True) where the relationship is NULL. Execute the
   * findCustomersByQuery22 method. Verify the results were accurately returned.
   */

  public void nullTest3() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection c = null;
    try {
      TestUtil.logMsg("Determine if customer has a NULL relationship");
      c = customerHome.findCustomersByQuery22("Shelly D. Mcgowan");
      expectedPKs = new String[1];
      expectedPKs[0] = "3";
      if (!Util.checkEJBs(c, Schema.CUSTOMERREF, expectedPKs)) {
        TestUtil.logErr("UnSuccessfully found customer Shelly D. Mcgowan");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully found customer Shelly D. Mcgowan");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception nullTest3: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("nullTest3 failed", e);
    }

    if (!pass)
      throw new Fault("nullTest3 failed");
  }

  /*
   * @testName: nullTest4
   * 
   * @assertion_ids: EJB:SPEC:425
   * 
   * @test_Strategy: Define a query using Boolean operator OR in a conditional
   * test (True OR True = True) where the relationship is NULL. Execute the
   * findCustomersByQuery23 method. Verify the results were accurately returned.
   */

  public void nullTest4() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection c = null;
    try {
      TestUtil.logMsg("Determine if customer has a NULL relationship");
      c = customerHome.findCustomersByQuery23("Arthur D. Frechette");
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
        TestUtil.logErr(
            "UnSuccessfully found customer OR customers have null relationships");
        pass = false;
      } else {
        TestUtil.logMsg(
            "Successfully found customer Arthur D. Frechette OR customers have null relationships");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception nullTest4: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("nullTest4 failed", e);
    }

    if (!pass)
      throw new Fault("nullTest4 failed");
  }

  /*
   * @testName: nullTest5
   * 
   * @assertion_ids: EJB:SPEC:426
   * 
   * @test_Strategy: Define a query using Boolean operator NOT in a conditional
   * test (NOT True = False) where the relationship is NULL. Execute the
   * findCustomersByQuery24 method. Verify the results were accurately returned.
   */

  public void nullTest5() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection c = null;
    try {
      TestUtil.logMsg("Determine if customers have a NULL relationship");
      c = customerHome.findCustomersByQuery24();
      if (c.size() != 0) {
        TestUtil.logErr("findCustomersByQuery24 returned " + c.size()
            + " references, expected 0 references");
        pass = false;
      } else {
        TestUtil
            .logMsg("Successfully found all customers have null relationships");
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception nullTest5: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("nullTest5 failed", e);
    }

    if (!pass)
      throw new Fault("nullTest5 failed");
  }

  /*
   * @testName: nullTest6
   * 
   * @assertion_ids: EJB:SPEC:375; EJB:SPEC:358; EJB:SPEC:274
   * 
   * @test_Strategy: This test verifies that a multi-valued finder method on a
   * cmr-field which contains contains null values in the cmp-field used as its'
   * condition null returns the correct result set. The LIKE expression uses an
   * input parameter for the condition.
   *
   */

  public void nullTest6() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection c = null;
    try {
      TestUtil.logMsg(
          "Determine which customers have an area code beginning with 9");
      c = customerHome.findCustomersByQuery38("9%");
      expectedPKs = new String[2];
      expectedPKs[0] = "3";
      expectedPKs[1] = "12";

      if (!Util.checkEJBs(c, Schema.CUSTOMERREF, expectedPKs)) {
        TestUtil.logErr("UnSuccessfully returned expected results");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully returned expected results");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception nullTest6: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("nullTest6 failed", e);
    }

    if (!pass)
      throw new Fault("nullTest6 failed");
  }

  /*
   * @testName: nullTest7
   * 
   * @assertion_ids: EJB:SPEC:375; EJB:SPEC:410; EJB:SPEC:283;EJB:SPEC:403
   * 
   * @test_Strategy: This test verifies that a multi-valued select method on
   * cmp-field includes nulls in the result set.
   */

  public void nullTest7() throws Fault {

    boolean pass1 = true;
    boolean pass2 = true;
    Collection c = null;
    String[] expectedZips = new String[] { "00252", "00252", "00252", "00252",
        "00252", "00252", "00252", "00252", "00252", "00252", "00252", "00252",
        "11345" };

    try {
      TestUtil.logMsg(
          "Find all work zip codes via ejbSelectCustomersByWorkZipCodes");
      c = customerHome.selectCustomersByWorkZipCode();

      if (c.size() != 14) {
        TestUtil.logErr("selectCustomersByWorkZipCode returned " + c.size()
            + " references, expected 14 references");
        pass1 = false;
      } else

      if (pass1) {
        Iterator i = c.iterator();
        int numOfNull = 0;
        int foundZip = 0;
        while (i.hasNext()) {
          TestUtil.logTrace("Check contents of collection for null");
          Object o = i.next();
          if (o == null) {
            numOfNull++;
            continue;
          }

          TestUtil.logTrace("Check collection for expected zip codes");

          for (int l = 0; l < 13; l++) {
            if (expectedZips[l].equals(o)) {
              foundZip++;
              break;
            }
          }
        }
        if ((numOfNull != 1) || (foundZip != 13)) {
          TestUtil.logErr(
              "selectCustomersByWorkZipCode did not return expected results");
          pass2 = false;
        } else {
          TestUtil.logMsg("Successfully returned expected results");
        }
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception nullTest7: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("nullTest7 failed", e);
    }

    if (!pass1 || !pass2)
      throw new Fault("nullTest7 failed");
  }

  /*
   * @testName: nullTest8
   * 
   * @assertion_ids: EJB:SPEC:377; EJB:SPEC:409; EJB:SPEC:270
   * 
   * @test_Strategy: This test verifies a single-object finder returns a null
   * cmr value as the result.
   */

  public void nullTest8() throws Fault {
    boolean pass = true;
    Customer c = null;

    try {
      TestUtil.logMsg("Find customer spouse");
      c = customerHome.findCustomerByQuery40();
      if (c != null) {
        TestUtil
            .logErr("findCustomerByQuery40 returned " + c + " expected: null");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully returned expected results");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception nullTest8: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("nullTest8 failed", e);
    }
    if (!pass)
      throw new Fault("nullTest8 failed");
  }

  /*
   * @testName: nullTest9
   * 
   * @assertion_ids: EJB:SPEC:377; EJB:SPEC:410; EJB:SPEC:280
   * 
   * @test_Strategy: This test verifies a single-object select method returns a
   * null cmp value as the result.
   */

  public void nullTest9() throws Fault {
    boolean pass = true;
    String s = null;

    try {
      TestUtil.logMsg("Find home zip codes via ejbSelectCustomerByHomeAddress");
      s = customerHome.selectCustomerByHomeAddress();
      if (s != null) {
        TestUtil.logErr(
            "selectCustomerByHomeAddress returned " + s + " expected: null");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully returned expected results");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception nullTest9: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("nullTest9 failed", e);
    }
    if (!pass)
      throw new Fault("nullTest9 failed");
  }

  /*
   * @testName: nullTest10
   * 
   * @assertion_ids: EJB:SPEC:420; EJB:SPEC:408
   * 
   * @test_Strategy: This tests a null cmp-field using IS NULL.
   *
   */

  public void nullTest10() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection c = null;
    try {
      TestUtil.logMsg("Determine which customers have an null name");
      c = customerHome.findCustomersByQuery39();
      expectedPKs = new String[1];
      expectedPKs[0] = "12";

      if (!Util.checkEJBs(c, Schema.CUSTOMERREF, expectedPKs)) {
        TestUtil.logErr("UnSuccessfully returned expected results");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully returned expected results");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception nullTest10: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("nullTest10 failed", e);
    }

    if (!pass)
      throw new Fault("nullTest10 failed");
  }

  /*
   * @testName: nullTest11
   * 
   * @assertion_ids: EJB:SPEC:423
   * 
   * @test_Strategy: This tests a null single-valued cmr-field using IS NOT
   * NULL.
   *
   */

  public void nullTest11() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection o = null;
    try {
      TestUtil
          .logMsg("Find all orders where related customer name is not null");
      o = orderHome.findOrdersByQuery20();
      expectedPKs = new String[15];
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

      if (!Util.checkEJBs(o, Schema.ORDERREF, expectedPKs)) {
        TestUtil.logErr("UnSuccessfully returned expected results");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully returned expected results");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception nullTest11: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("nullTest11 failed", e);
    }

    if (!pass)
      throw new Fault("nullTest11 failed");
  }

  /*
   * @testName: nullTest12
   * 
   * @assertion_ids: EJB:SPEC:425
   * 
   * @test_Strategy: Define a query using Boolean operator AND in a conditional
   * test ( False AND False = False) where the relationship is NULL.
   *
   */

  public void nullTest12() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection p = null;
    try {
      TestUtil.logMsg("Check results of AND operator: False AND False = False");
      p = productHome.findProductsByQuery7(100);
      expectedPKs = new String[0];
      if (!Util.checkEJBs(p, Schema.PRODUCTREF, expectedPKs)) {
        TestUtil.logErr("UnSuccessfully returned expected results");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully returned expected results");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception nullTest12: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("nullTest12 failed", e);
    }

    if (!pass)
      throw new Fault("nullTest12 failed");
  }

  /*
   * @testName: nullTest13
   * 
   * @assertion_ids: EJB:SPEC:416
   * 
   * @test_Strategy: If an input parameter is NULL, comparison operations
   * involving the input parameter will return an unknown value.
   *
   */

  public void nullTest13() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection p = null;
    try {
      TestUtil.logMsg(
          "Provide a null value for a comparison operation and verify the results");
      p = productHome.findProductsByQuery8(null);
      expectedPKs = new String[0];

      if (!Util.checkEJBs(p, Schema.PRODUCTREF, expectedPKs)) {
        TestUtil.logErr("UnSuccessfully returned expected results");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully returned expected results");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception nullTest13: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("nullTest13 failed", e);
    }

    if (!pass)
      throw new Fault("nullTest13 failed");
  }

  /*
   * @testName: nullTest14
   * 
   * @assertion_ids: EJB:SPEC:393
   * 
   * @test_Strategy: This test verifies the same results of two queries using
   * the keyword DISTINCT or not using DISTINCT in the query with the aggregate
   * keyword COUNT to verity the NULL values are eliminated before the aggregate
   * is applied.
   *
   */

  public void nullTest14() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    long c1 = 0;
    long c2 = 0;
    long expectedResult1 = 13;
    long expectedResult2 = 12;
    try {
      TestUtil.logMsg(
          "Execute two queries composed differently and verify results");
      c1 = customerHome.selectAllHomeCities();

      c2 = customerHome.selectNotNullHomeCities();

      if (c1 != expectedResult1) {
        TestUtil.logErr("ERROR: selectAllHomeCities returned:" + c1
            + " expected: " + expectedResult1);
        pass1 = false;
      } else {
        TestUtil.logMsg("PASS:  selectAllHomeCities returned expected results");
        pass1 = true;
      }
      if (c2 != expectedResult2) {
        TestUtil.logErr("ERROR: selectNotNullHomeCities returned:" + c2
            + " expected: " + expectedResult2);
        pass2 = false;
      } else {
        TestUtil
            .logMsg("PASS:  selectNotNullHomeCities returned expected results");
        pass2 = true;
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception nullTest14: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("nullTest14 failed", e);
    }

    if (!pass1 || !pass2)
      throw new Fault("nullTest14 failed");
  }

  /*
   * @testName: nullTest15
   * 
   * @assertion_ids: EJB:SPEC:361
   * 
   * @test_Strategy: Execute the findCustomersByQuery43 using IS NOT EMPTY in a
   * ManyXMany relationship where the relationship is EMPTY.
   */

  public void nullTest15() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection c = null;

    try {
      TestUtil.logMsg("Find customers whose id is greater than 1 "
          + "and where the relationship is NOT EMPTY");
      c = customerHome.findCustomersByQuery43();
      expectedPKs = new String[13];
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
      if (!Util.checkEJBs(c, Schema.CUSTOMERREF, expectedPKs)) {
        TestUtil.logErr("UnSuccessfully found expected customers");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully found expected customers");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception nullTest15: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("nullTest15 failed", e);
    }

    if (!pass)
      throw new Fault("nullTest15 failed");
  }

  /*
   * @testName: nullTest16
   * 
   * @assertion_ids: EJB:SPEC:337; EJB:SPEC:342; EJB:SPEC:359
   * 
   * @test_Strategy: Use the operator IS NULL in a null comparision expression
   * within the WHERE CLAUSE where the relationship is NULL. Use an input
   * parameter.
   */

  public void nullTest16() throws Fault {
    boolean pass = false;
    String[] expectedResult1 = new String[] { null };
    String[] expectedResult2 = new String[] { null, null, null };
    Collection s = null;
    try {
      TestUtil.logMsg("Execute query with input parameter and IS NULL");
      s = aliasHome.selectNullAlias(null);
      if (s.size() == 1) {
        TestUtil.logMsg(
            "Checking results for alias names with size of " + s.size());
        String[] result = (String[]) (s.toArray(new String[s.size()]));
        pass = Arrays.equals(expectedResult1, result);
        TestUtil.logMsg("Received expected results with size :" + s.size());
      } else {
        if (s.size() == 3) {
          String[] result = (String[]) (s.toArray(new String[s.size()]));
          pass = Arrays.equals(expectedResult2, result);
          TestUtil.logMsg("Received expected results with size :" + s.size());
        } else {
          TestUtil.logErr("ERROR: Did not received expected results");
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception nullTest16: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("nullTest16 failed", e);
    }

    if (!pass)
      throw new Fault("nullTest16 failed");
  }

  /*
   * @testName: nullTest17
   * 
   * @assertion_ids: EJB:SPEC:359
   * 
   * @test_Strategy: Use the operator IS NOT NULL in a null comparision
   * expression within the WHERE CLAUSE where the relationship is NULL. Use an
   * input parameter.
   */

  public void nullTest17() throws Fault {
    boolean pass = false;
    String expectedPKs[] = null;
    String expectedPKs1[] = null;
    Collection a = null;
    try {
      TestUtil.logMsg("Execute query with input parameter and IS NOT NULL");
      a = aliasHome.findAliasesByQuery8("adf");
      expectedPKs = new String[29];
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
      expectedPKs[18] = "19";
      expectedPKs[19] = "20";
      expectedPKs[20] = "21";
      expectedPKs[21] = "22";
      expectedPKs[22] = "23";
      expectedPKs[23] = "24";
      expectedPKs[24] = "25";
      expectedPKs[25] = "26";
      expectedPKs[26] = "27";
      expectedPKs[27] = "28";
      expectedPKs[28] = "29";

      expectedPKs1 = new String[27];
      expectedPKs1[0] = "1";
      expectedPKs1[1] = "2";
      expectedPKs1[2] = "3";
      expectedPKs1[3] = "4";
      expectedPKs1[4] = "5";
      expectedPKs1[5] = "6";
      expectedPKs1[6] = "7";
      expectedPKs1[7] = "8";
      expectedPKs1[8] = "9";
      expectedPKs1[9] = "10";
      expectedPKs1[10] = "11";
      expectedPKs1[11] = "12";
      expectedPKs1[12] = "13";
      expectedPKs1[13] = "14";
      expectedPKs1[14] = "17";
      expectedPKs1[15] = "18";
      expectedPKs1[16] = "19";
      expectedPKs1[17] = "20";
      expectedPKs1[18] = "21";
      expectedPKs1[19] = "22";
      expectedPKs1[20] = "23";
      expectedPKs1[21] = "24";
      expectedPKs1[22] = "25";
      expectedPKs1[23] = "26";
      expectedPKs1[24] = "27";
      expectedPKs1[25] = "28";
      expectedPKs1[26] = "29";

      if (a.size() == 29) {
        TestUtil.logMsg(
            "Checking results for expectedPKs with size of " + a.size());
        pass = Util.checkEJBs(a, Schema.ALIASREF, expectedPKs);
      } else {
        if (a.size() == 27) {
          TestUtil.logMsg(
              "Checking results for expectedPK1s with size of " + a.size());
          pass = Util.checkEJBs(a, Schema.ALIASREF, expectedPKs1);
        } else {
          TestUtil.logErr("ERROR: Did not received expected results");
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception nullTest17: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("nullTest17 failed", e);
    }

    if (!pass)
      throw new Fault("nullTest17 failed");
  }

  /*
   * @testName: nullTest18
   * 
   * @assertion_ids: EJB:SPEC:376
   * 
   * @test_Strategy: The Bean Provider can use IS NOT NULL construct to
   * eliminate the null from the result set of the query.
   */

  public void nullTest18() throws Fault {
    boolean pass = false;
    Collection c = null;
    String[] expectedZips = new String[] { "00252", "00252", "00252", "00252",
        "00252", "00252", "00252", "00252", "00252", "00252", "00252", "00252",
        "11345" };
    try {
      TestUtil.logMsg(
          "Find work zip codes that are not null via ejbSelectCustomersByNotNullWorkZipCodes");
      c = customerHome.selectCustomersByNotNullWorkZipCode();
      String[] result = (String[]) (c.toArray(new String[c.size()]));
      TestUtil.logMsg("Compare results of work zip codes");
      pass = Arrays.equals(expectedZips, result);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception nullTest18: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("nullTest18 failed", e);
    }

    if (!pass)
      throw new Fault("nullTest18 failed");
  }

  /*
   * @testName: nullTest19
   * 
   * @assertion_ids: EJB:SPEC:426
   * 
   * @test_Strategy: Define a query using Boolean operator NOT in a conditional
   * test (NOT False = True) where the cmp-field is NULL.
   *
   */

  public void nullTest19() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection a = null;
    try {
      TestUtil.logMsg("Find all aliases where Customer name is not null");
      a = aliasHome.findAliasesByQuery10();
      expectedPKs = new String[27];
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
      expectedPKs[14] = "17";
      expectedPKs[15] = "18";
      expectedPKs[16] = "19";
      expectedPKs[17] = "20";
      expectedPKs[18] = "21";
      expectedPKs[19] = "22";
      expectedPKs[20] = "23";
      expectedPKs[21] = "24";
      expectedPKs[22] = "26";
      expectedPKs[23] = "27";
      expectedPKs[24] = "28";
      expectedPKs[25] = "29";
      expectedPKs[26] = "30";
      if (!Util.checkEJBs(a, Schema.ALIASREF, expectedPKs)) {
        TestUtil.logErr("UnSuccessfully returned expected results");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully returned expected results");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception nullTest19: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("nullTest19 failed", e);
    }

    if (!pass)
      throw new Fault("nullTest19 failed");
  }

  /*
   * @testName: nullTest20
   * 
   * @assertion_ids: EJB:SPEC:424
   * 
   * @test_Strategy: Define a query using Boolean operator AND in a conditional
   * test (False AND True = False) where the condition is NULL.
   */

  public void nullTest20() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection c = null;
    try {
      TestUtil.logMsg("Check results of AND operator: False AND True = False");
      c = customerHome.findCustomersByQuery22("George W. Bush");
      expectedPKs = new String[0];
      if (!Util.checkEJBs(c, Schema.CUSTOMERREF, expectedPKs)) {
        TestUtil.logErr("UnSuccessfully returned expected results");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully returned expected results");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception nullTest20: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("nullTest20 failed", e);
    }

    if (!pass)
      throw new Fault("nullTest20 failed");
  }

  /*
   * @testName: nullTest21
   * 
   * @assertion_ids: EJB:SPEC:425
   * 
   * @test_Strategy: Define a query using Boolean operator OR in a conditional
   * test (False OR True = True) where the condition is NULL.
   */

  public void nullTest21() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection c = null;
    try {
      TestUtil.logMsg("Check results of OR operator: False OR True = True");
      c = customerHome.findCustomersByQuery23("George W. Bush");
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
        TestUtil.logErr("UnSuccessfully returned expected results");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully returned expected results");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception nullTest21: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("nullTest21 failed", e);
    }

    if (!pass)
      throw new Fault("nullTest21 failed");
  }

  /*
   * @testName: nullTest22
   * 
   * @assertion_ids: EJB:SPEC:425
   * 
   * @test_Strategy: Define a query using Boolean operator OR in a conditional
   * test (False OR False = False) where the both conditions are NULL.
   */

  public void nullTest22() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection a = null;
    try {
      TestUtil.logMsg("Check results of OR operator: False OR False = False");
      a = aliasHome.findAliasesByQuery9("99");
      expectedPKs = new String[0];
      if (!Util.checkEJBs(a, Schema.ALIASREF, expectedPKs)) {
        TestUtil.logErr("UnSuccessfully returned expected results");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully returned expected results");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception nullTest22: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("nullTest22 failed", e);
    }

    if (!pass)
      throw new Fault("nullTest22 failed");
  }

  /*
   * @testName: nullTest23
   * 
   * @assertion_ids: EJB:SPEC:375; EJB:SPEC:409
   * 
   * @test_Strategy: This test verifies that a multi-valued finder method on a
   * cmr-field which contains a null value is included in the result set.
   */

  public void nullTest23() throws Fault {
    boolean pass1 = true;
    boolean pass2 = true;
    Collection c = null;
    String expectedPKs[] = null;

    try {
      TestUtil.logMsg("Find customers of spouses");
      c = customerHome.findCustomersByQuery41();
      expectedPKs = new String[5];
      expectedPKs[0] = "7";
      expectedPKs[1] = "10";
      expectedPKs[2] = "11";
      expectedPKs[3] = "12";
      expectedPKs[4] = "13";

      if (c.size() != 6) {
        TestUtil.logErr("findCustomersByQuery41 returned " + c.size()
            + " references, expected 6 references");
        pass1 = false;
      } else

      if (pass1) {
        Iterator i = c.iterator();
        int numOfNull = 0;
        int foundPK = 0;
        while (i.hasNext()) {
          TestUtil.logTrace("Check contents of collection for null");
          Object o = i.next();
          if (o == null) {
            numOfNull++;
            continue;
          }
          TestUtil.logTrace("Check collection for expected references");
          Customer cRef = (Customer) PortableRemoteObject.narrow(o,
              Customer.class);
          for (int l = 0; l < 5; l++) {
            if (expectedPKs[l].equals(cRef.getId())) {
              foundPK++;
              break;
            }
          }

        }
        if ((numOfNull != 1) || (foundPK != 5)) {
          TestUtil
              .logErr("findCustomersByQuery41 did not return expected results");
          pass2 = false;
        } else {
          TestUtil.logMsg("Successfully returned expected results");
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception nullTest23: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("nullTest23 failed", e);
    }

    if (!pass1 || !pass2)
      throw new Fault("nullTest23 failed");
  }

  /*
   * @testName: nullTest24
   * 
   * @assertion_ids: EJB:SPEC:377; EJB:SPEC:411
   * 
   * @test_Strategy: This test verifies a single-object select returns a null
   * value as the result.
   * 
   */

  public void nullTest24() throws Fault {
    boolean pass = true;
    String s = null;

    try {
      TestUtil.logMsg("Find spouse information via ejbSelectSpouseInfo");
      s = customerHome.getSpouseInfo();
      if (s != null) {
        TestUtil.logErr("selectSpouseInfo " + s + " expected: null");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully returned expected results");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception nullTest24: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("nullTest24 failed", e);
    }

    if (!pass)
      throw new Fault("nullTest24 failed");
  }

  /*
   * @testName: nullTest25
   * 
   * @assertion_ids: EJB:SPEC:329; EJB:SPEC:415; EJB:SPEC:425
   * 
   * @test_Strategy: Define a query using Boolean operator OR in a conditional
   * test (TRUE OR UNKNOWN = TRUE) where the first condition will return true
   * and the second condition will return NULL. Verify the query returns TRUE.
   *
   */

  public void nullTest25() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection a = null;

    try {
      TestUtil.logMsg(
          "Execute query with OR condition where first condition is TRUE");
      a = aliasHome.findAliasesByQuery11();
      expectedPKs = new String[Schema.NUMOFALIASES];

      for (int i = 0; i < Schema.NUMOFALIASES; i++)
        expectedPKs[i] = Integer.toString(i + 1);

      if (!Util.checkEJBs(a, Schema.ALIASREF, expectedPKs)) {
        TestUtil.logErr("UnSuccessfully returned expected results");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully returned expected results");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception nullTest25: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("nullTest25 failed", e);
    }

    if (!pass)
      throw new Fault("nullTest25 failed");
  }

  /*
   * @testName: nullTest26
   * 
   * @assertion_ids: EJB:SPEC:329; EJB:SPEC:424
   * 
   * @test_Strategy: Define a query using Boolean operator AND in a conditional
   * test (TRUE AND UNKNOWN = UNKNOWN) where the first condition will return
   * true and the second condition will return NULL. Verify the query returns no
   * results.
   *
   */

  public void nullTest26() throws Fault {
    boolean pass = true;
    Collection a = null;
    try {
      TestUtil.logMsg("Find all aliases who are not null AND are not equal");
      a = aliasHome.findAliasesByQuery12();
      if (a.size() != 0) {
        TestUtil.logErr("findAliasesByQuery12 returned " + a.size()
            + " references, expected 0 references");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully returned expected results");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception nullTest26: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("nullTest26 failed", e);
    }

    if (!pass)
      throw new Fault("nullTest26 failed");
  }

  /*
   * @testName: nullTest27
   * 
   * @assertion_ids: EJB:SPEC:329
   * 
   * @test_Strategy: This test verifies that a multi-valued select method which
   * includes a null non-terminal cmr-field in the path expression in not
   * included in the result set.
   */

  public void nullTest27() throws Fault {

    boolean pass1 = true;
    boolean pass2 = true;
    Collection o = null;
    Double[] expectedBalances = new Double[] { new Double(500D),
        new Double(750D), new Double(1000D), new Double(1400D),
        new Double(1500D), new Double(2000D), new Double(2500D),
        new Double(4400D), new Double(5000D), new Double(5500D),
        new Double(7000D), new Double(8000D), new Double(13000D),
        new Double(15000D), new Double(23000D) };

    try {
      TestUtil.logMsg(
          "Find all credit card balances via ejbSelectAllCreditCardBalances");
      o = orderHome.selectAllCreditCardBalances();

      if (o.size() != 15) {
        TestUtil.logErr("selectAllCreditCardBalances returned " + o.size()
            + " references, expected 15 references");
        pass1 = false;
      } else

      if (pass1) {
        Iterator i = o.iterator();
        int numOfNull = 0;
        int foundBalance = 0;
        while (i.hasNext()) {
          TestUtil.logTrace("Check contents of collection for null");
          Object b = i.next();
          if (b == null) {
            numOfNull++;
            continue;
          }

          TestUtil.logTrace("Check collection for expected balances");

          for (int l = 0; l < 15; l++) {
            if (expectedBalances[l].equals(b)) {
              foundBalance++;
              break;
            }
          }
        }
        if ((numOfNull != 0) || (foundBalance != 15)) {
          TestUtil.logErr(
              "selectAllCreditCardBalances did not return expected results");
          pass2 = false;
        } else {
          TestUtil.logMsg("Successfully returned expected results");
        }
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception nullTest27: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("nullTest27 failed", e);
    }

    if (!pass1 || !pass2)
      throw new Fault("nullTest27 failed");
  }

  /*
   * @testName: nullTest28
   * 
   * @assertion_ids: EJB:SPEC:329
   * 
   * @test_Strategy: This test verifies that a multi-valued finder method which
   * includes a null non-terminal cmr-field in the path expression in not
   * included in the result set.
   */

  public void nullTest28() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection o = null;
    try {
      TestUtil.logMsg("Check CreditCard Balances for Orders");
      o = orderHome.findOrdersByQuery22((double) 1000);
      expectedPKs = new String[2];
      expectedPKs[0] = "11";
      expectedPKs[1] = "12";

      if (!Util.checkEJBs(o, Schema.ORDERREF, expectedPKs)) {
        TestUtil.logErr("UnSuccessfully returned expected results");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully returned expected results");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception nullTest28: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("nullTest28 failed", e);
    }

    if (!pass)
      throw new Fault("nullTest28 failed");
  }

  public void cleanup() throws Fault {
    TestUtil.logMsg("cleanup ok");
  }
}
