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

package com.sun.ts.tests.ejb.ee.pm.ejbql.from_clause;

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
   * @testName: fromTest1
   * 
   * @assertion_ids: EJB:SPEC:312; EJB:SPEC:322
   * 
   * @test_Strategy: Execute findAllOrdersByCustomerName method to determine how
   * many orders per customer. Verify the results were accurately returned. This
   * query is defined on a many-one relationship.
   *
   */

  public void fromTest1() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection o = null;

    try {
      TestUtil.logMsg("Find All Orders for Customer: Robert E. Bissett");
      o = orderHome.findAllOrdersByCustomerName("Robert E. Bissett");
      expectedPKs = new String[2];
      expectedPKs[0] = "4";
      expectedPKs[1] = "9";
      if (!Util.checkEJBs(o, Schema.ORDERREF, expectedPKs)) {
        TestUtil
            .logErr("UnSuccessfully found all orders for Robert E. Bissett");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully found all orders for Robert E. Bissett");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception fromTest1: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("fromTest1 failed", e);
    }

    if (!pass)
      throw new Fault("fromTest1 failed");
  }

  /*
   * @testName: fromTest2
   * 
   * @assertion_ids: EJB:SPEC:317.1
   * 
   * @test_Strategy: Execute the findAllCustomers method to find all customers.
   * Verify the results were accurately returned.
   * 
   */

  public void fromTest2() throws Fault {

    boolean pass = true;
    String expectedPKs[] = null;
    Collection c = null;
    try {
      TestUtil.logMsg("Find All Customers");
      c = customerHome.findAllCustomers();
      expectedPKs = new String[Schema.NUMOFCUSTOMERS];
      for (int i = 0; i < Schema.NUMOFCUSTOMERS; i++)
        expectedPKs[i] = Integer.toString(i + 1);
      if (!Util.checkEJBs(c, Schema.CUSTOMERREF, expectedPKs)) {
        TestUtil.logErr("UnSuccessfully found all customers in CustomerEJB");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully found all customers in CustomerEJB");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception fromTest2: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("fromTest2 failed", e);
    }

    if (!pass)
      throw new Fault("fromTest2 failed");
  }

  /*
   * @testName: fromTest3
   * 
   * @assertion_ids: EJB:SPEC:321; EJB:SPEC:317.2; EJB:SPEC:323
   * 
   * @test_Strategy: Execute the findAllCustomersByAliasName method to find all
   * customers with a given alias name. This query is defined on a many-many
   * relationship.
   */

  public void fromTest3() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection c = null;
    try {
      TestUtil.logMsg("Find All Customers with Alias: imc");
      c = customerHome.findAllCustomersByAliasName("imc");
      expectedPKs = new String[1];
      expectedPKs[0] = "8";
      if (!Util.checkEJBs(c, Schema.CUSTOMERREF, expectedPKs)) {
        TestUtil.logErr("UnSuccessfully found all customers with Alias: imc");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully found all customers with Alias: imc");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception fromTest3: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("fromTest3 failed", e);
    }

    if (!pass)
      throw new Fault("fromTest3 failed");
  }

  /*
   * @testName: fromTest4
   * 
   * @assertion_ids: EJB:SPEC:322
   * 
   * @test_Strategy: Execute the findCustomerByHomeAddress method to find
   * customer with given address information. Verify the results are accurate.
   * This query is defined on a one-one relationship and used conditional AND in
   * query.
   *
   */

  public void fromTest4() throws Fault {
    boolean pass = true;
    Customer c = null;
    try {
      TestUtil.logMsg("Find Customer with Home Address in Swansea");
      c = customerHome.findCustomerByHomeAddress("125 Moxy Lane", "Swansea",
          "MA", "11345");
      if (!Util.checkEJB(c, "3")) {
        TestUtil.logErr("UnSuccessfully found customer Shelly D. Mcgowan");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully found customer Shelly D. Mcgowan");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception fromTest4: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("fromTest4 failed", e);
    }

    if (!pass)
      throw new Fault("fromTest4 failed");
  }

  /*
   * @testName: fromTest5
   * 
   * @assertion_ids: EJB:SPEC:323
   * 
   * @test_Strategy: Execute the findCustomersByCreditCardType method to find
   * customers with a certain credit card type. This query is defined on a
   * one-many relationship. Verify the results.
   * 
   */

  public void fromTest5() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection c = null;
    try {
      TestUtil.logMsg("Find all Customers with AXP Credit Cards");
      c = customerHome.findCustomersByCreditCardType("AXP");
      expectedPKs = new String[6];
      expectedPKs[0] = "1";
      expectedPKs[1] = "4";
      expectedPKs[2] = "5";
      expectedPKs[3] = "8";
      expectedPKs[4] = "9";
      expectedPKs[5] = "12";

      if (!Util.checkEJBs(c, Schema.CUSTOMERREF, expectedPKs)) {
        TestUtil
            .logErr("UnSuccessfully found all customers with AXP Credit Cards");
        pass = false;
      } else {
        TestUtil
            .logMsg("Successfully found all customers with AXP Credit Cards");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception fromTest4: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("fromTest5 failed", e);
    }

    if (!pass)
      throw new Fault("fromTest5 failed");
  }

  /*
   * @testName: fromTest6
   * 
   * @assertion_ids: EJB:SPEC:348.4; EJB:SPEC:338; EJB:SPEC:339
   * 
   * @test_Strategy: Execute the findCustomerByHomeInfo method to find customers
   * with a given home information. This query is defined on a one-one
   * relationship using conditional OR in query.
   * 
   */

  public void fromTest6() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection c = null;
    try {
      TestUtil.logMsg("Find Customers with Home Address Information");
      c = customerHome.findCustomersByHomeInfo("47 Skyline Drive", "Chelmsford",
          "VT", "02155");
      expectedPKs = new String[4];
      expectedPKs[0] = "1";
      expectedPKs[1] = "10";
      expectedPKs[2] = "11";
      expectedPKs[3] = "13";

      if (!Util.checkEJBs(c, Schema.CUSTOMERREF, expectedPKs)) {
        TestUtil.logErr("UnSuccessfully found expected customers");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully found expected customers");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception fromTest6: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("fromTest6 failed", e);
    }

    if (!pass)
      throw new Fault("fromTest6 failed");
  }

  /*
   * @testName: fromTest7
   * 
   * @assertion_ids: EJB:SPEC:319
   * 
   * @test_Strategy: Execute the findProductsByQuery9 and ensure the
   * identification variables can be interpreted correctly regardless of case.
   *
   */

  public void fromTest7() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection p = null;
    try {
      TestUtil.logMsg("Find All Products");
      p = productHome.findProductsByQuery9();
      expectedPKs = new String[Schema.NUMOFPRODUCTS];
      for (int i = 0; i < Schema.NUMOFPRODUCTS; i++)
        expectedPKs[i] = Integer.toString(i + 1);
      if (!Util.checkEJBs(p, Schema.PRODUCTREF, expectedPKs)) {
        TestUtil.logErr("UnSuccessfully found all products");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully found all products");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception fromTest7: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("fromTest7 failed", e);
    }

    if (!pass)
      throw new Fault("fromTest7 failed");
  }

  public void cleanup() throws Fault {
    TestUtil.logMsg("cleanup ok");
  }
}
