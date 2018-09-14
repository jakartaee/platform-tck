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
 * @(#)Client.java	1.17 03/05/16
 */

package com.sun.ts.tests.ejb.ee.pm.ejbql.equality;

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
   * @testName: equalityTest1
   * 
   * @assertion_ids: EJB:SPEC:428
   * 
   * @test_Strategy: Execute findCustomerByQuery16 method to compare the
   * equality of strings.
   *
   */

  public void equalityTest1() throws Fault {
    boolean pass = true;
    Customer c = null;

    try {
      TestUtil.logMsg("Determine the equality of two strings");
      c = customerHome.findCustomerByQuery16();
      if (!Util.checkEJB(c, "3")) {
        TestUtil.logErr("UnSuccessfully found customer Shelly D. Mcgowan");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully found customer Shelly D. Mcgowan");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception equalityTest1: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("equalityTest1 failed", e);
    }

    if (!pass)
      throw new Fault("equalityTest1 failed");
  }

  /*
   * @testName: equalityTest2
   * 
   * @assertion_ids: EJB:SPEC:427
   * 
   * @test_Strategy: Execute findCustomerByQuery17 method to compare the
   * equality of entity objects.
   * 
   */

  public void equalityTest2() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Customer c = null;

    try {
      TestUtil.logMsg("Determine the equality of two entity objects");
      c = customerHome.findCustomerByQuery17("Shelly D. Mcgowan");
      if (!Util.checkEJB(c, "3")) {
        TestUtil.logErr(
            "UnSuccessfully compared the equality of two entity objects");
        pass = false;
      } else {
        TestUtil
            .logMsg("Successfully compared the equality of two entity objects");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception equalityTest2: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("equalityTest2 failed", e);
    }

    if (!pass)
      throw new Fault("equalityTest2 failed");
  }

  /*
   * @testName: equalityTest3
   * 
   * @assertion_ids: EJB:SPEC:428
   * 
   * @test_Strategy: Execute findCustomersByQuery27 method to compare like
   * types; in this case, strings.
   * 
   * 
   */

  public void equalityTest3() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection c = null;

    try {
      TestUtil.logMsg("Compare the equality of strings");
      c = customerHome.findCustomersByQuery27();
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
      expectedPKs[11] = "13";
      expectedPKs[12] = "14";

      if (!Util.checkEJBs(c, Schema.CUSTOMERREF, expectedPKs)) {
        TestUtil.logErr("UnSuccessfully compared the equality of strings");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully compared the equality of strings.");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception equalityTest3: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("equalityTest3 failed", e);
    }

    if (!pass)
      throw new Fault("equalityTest3 failed");
  }

  /*
   * @testName: equalityTest4
   * 
   * @assertion_ids: EJB:SPEC:429
   * 
   * @test_Strategy: Execute the findApprovedCreditCards method to compare
   * booleans using <>.
   * 
   */

  public void equalityTest4() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection o = null;

    try {
      TestUtil.logMsg("Compare Boolean fields");
      o = orderHome.findApprovedCreditCards();
      expectedPKs = new String[4];
      expectedPKs[0] = "1";
      expectedPKs[1] = "7";
      expectedPKs[2] = "11";
      expectedPKs[3] = "13";

      if (!Util.checkEJBs(o, Schema.ORDERREF, expectedPKs)) {
        TestUtil.logErr("UnSuccessfully compared Boolean fields");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully compared Boolean fields.");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception equalityTest4: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("equalityTest4 failed", e);
    }

    if (!pass)
      throw new Fault("equalityTest4 failed");
  }

  /*
   * @testName: equalityTest5
   * 
   * @assertion_ids: EJB:SPEC:348.3
   * 
   * @test_Strategy: Compare two strings using the = character.
   */

  public void equalityTest5() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection o = null;

    try {
      TestUtil.logMsg("Find credit cards that expired on 03/05.");
      o = orderHome.selectAllExpiredCreditCards();
      expectedPKs = new String[1];
      expectedPKs[0] = "8";
      if (!Util.checkEJBs(o, Schema.ORDERREF, expectedPKs)) {
        TestUtil.logErr("UnSuccessfully found expired credit cards.");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully found expired credit cards.");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception equalityTest5: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("equalityTest5 failed", e);
    }

    if (!pass)
      throw new Fault("equalityTest5 failed");
  }

  /*
   * @testName: equalityTest6
   * 
   * @assertion_ids: EJB:SPEC:430
   * 
   * @test_Strategy: Execute findProductsByQuery1 method to determine the
   * validity of the comparison.
   * 
   */

  public void equalityTest6() throws Fault {
    boolean pass = true;
    String expectedPKs[] = null;
    Collection p = null;

    try {
      TestUtil.logMsg(
          "Compare exact numeric values and approximate numeric values");
      p = productHome.findProductsByQuery1();
      expectedPKs = new String[2];
      expectedPKs[0] = "1";
      expectedPKs[1] = "2";
      if (!Util.checkEJBs(p, Schema.PRODUCTREF, expectedPKs)) {
        TestUtil.logErr(
            "UnSuccessfully compared exact and approximate numeric values");
        pass = false;
      } else {
        TestUtil.logMsg(
            "Successfully compared exact and approximate numeric values.");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception equalityTest6: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("equalityTest6 failed", e);
    }

    if (!pass)
      throw new Fault("equalityTest6 failed");
  }

  public void cleanup() throws Fault {
    TestUtil.logMsg("cleanup ok");
  }
}
