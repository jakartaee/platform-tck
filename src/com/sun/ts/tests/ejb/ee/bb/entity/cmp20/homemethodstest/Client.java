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
 * @(#)Client.java	1.14 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.entity.cmp20.homemethodstest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.util.*;
import javax.ejb.*;
import javax.transaction.*;
import javax.rmi.PortableRemoteObject;
import java.rmi.*;

import com.sun.javatest.Status;

public class Client extends EETest {
  private static final String testName = "HomeTest";

  private static final String testBean = "java:comp/env/ejb/TestBean";

  private static final String testProps = "homemethodstest.properties";

  private TestBean beanRef = null;

  private TestBean beanRef2 = null;

  private TestBeanHome beanHome = null;

  private Properties props = null;

  private TSNamingContext nctx = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   * generateSQL;
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    try {
      logMsg("Obtain naming context");
      nctx = new TSNamingContext();

      // Get EJB Home ...
      logMsg("Looking up home interface for EJB: " + testBean);
      beanHome = (TestBeanHome) nctx.lookup(testBean, TestBeanHome.class);

      logTrace("Check for existing data before test run");
      prepareTestData();

      logMsg("Setup ok");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("Setup failed:", e);
    }
  }

  /* Run test */

  /*
   * @testName: test1
   * 
   * @assertion_ids: EJB:SPEC:127; EJB:SPEC:231; EJB:SPEC:128
   * 
   * @test_Strategy: Call the create method of an Entity Bean and verify it was
   * successful.
   */

  public void test1() throws Fault {
    boolean pass = true;
    try {
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(1, "John", "Edward", "Brown",
          "100-200-300");
      beanRef.initLogging(props);
      logMsg("Look up Customer by Primary Key");
      beanRef2 = (TestBean) beanHome.findByPrimaryKey(new Integer(1));
      pass = true;
      logMsg("Customer Found by Primary Key ");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception test1: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      try {
        if (beanRef != null) {
          beanRef.remove();
          TestUtil.logTrace("Removing beanRef from test1");
        } else {
          TestUtil.logTrace("beanRef in test1 is null");
        }
      } catch (Exception e) {
        TestUtil.logErr("Caught exception test1: " + e);
        TestUtil.printStackTrace(e);
      }
    }
    if (!pass)
      throw new Fault("test1 failed");
  }

  /*
   * @testName: test2
   * 
   * @assertion_ids: EJB:SPEC:127; EJB:SPEC:231
   * 
   * @test_Strategy: Call the overloaded create method of an Entity Bean and
   * verify it was successful.
   */

  public void test2() throws Fault {
    TestBean beanRef = null;
    TestBean beanRef2 = null;
    boolean pass = true;
    try {
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(2, "100-200-300", "VISA", 1000.00,
          "3450678912348090", "1102");
      beanRef.initLogging(props);
      logMsg("Look up Payment by Primary Key");
      beanRef2 = (TestBean) beanHome.findByPrimaryKey(new Integer(2));
      pass = true;
      logMsg("Payment Found by Primary Key");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception test2: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      try {
        if (beanRef != null) {
          beanRef.remove();
          TestUtil.logTrace("Removing beanRef from test2");
        } else {
          TestUtil.logTrace("beanRef in test2 is null");
        }
      } catch (Exception e) {
        TestUtil.logErr("Caught exception test2: " + e);
        TestUtil.printStackTrace(e);
      }
    }
    if (!pass)
      throw new Fault("test2 failed");
  }

  /*
   * @testName: test3
   * 
   * @assertion_ids: EJB:SPEC:127; EJB:SPEC:231; EJB:SPEC:140
   * 
   * @test_Strategy: Call the createMETHOD method of an Entity Bean and verify
   * it was successful.
   */

  public void test3() throws Fault {
    boolean pass = true;
    try {
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.createHomeAddress(3, "10 Spring Street",
          "Boston", "MA", 02115);
      beanRef.initLogging(props);
      logMsg("Look up Address by Primary Key");
      beanRef2 = (TestBean) beanHome.findByPrimaryKey(new Integer(3));
      pass = true;
      logMsg("Address Found by Primary Key ");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception test3: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      try {
        if (beanRef != null) {
          beanRef.remove();
          TestUtil.logTrace("Removing beanRef from test3");
        } else {
          TestUtil.logTrace("beanRef in test3 is null");
        }
      } catch (Exception e) {
        TestUtil.logErr("Caught exception test3: " + e);
        TestUtil.printStackTrace(e);
      }
    }
    if (!pass)
      throw new Fault("test3 failed");
  }

  /*
   * @testName: test4
   * 
   * @assertion_ids: EJB:SPEC:127; EJB:SPEC:140; EJB:SPEC:128
   * 
   * @test_Strategy: Call the createMETHOD method of an Entity Bean and verify
   * it was successful.
   */

  public void test4() throws Fault {
    boolean pass = true;
    try {
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.createCountry(4, "United States", "USA");
      beanRef.initLogging(props);
      logMsg("Look up Country by Primary Key");
      beanRef2 = (TestBean) beanHome.findByPrimaryKey(new Integer(4));
      pass = true;
      logMsg("Country Found by Primary Key ");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception test4: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      try {
        if (beanRef != null) {
          beanRef.remove();
          TestUtil.logTrace("Removing beanRef from test4");
        } else {
          TestUtil.logTrace("beanRef in test4 is null");
        }
      } catch (Exception e) {
        TestUtil.logErr("Caught exception test4: " + e);
        TestUtil.printStackTrace(e);
      }
    }
    if (!pass)
      throw new Fault("test4 failed");
  }

  /*
   * @testName: test5
   * 
   * @assertion_ids: EJB:SPEC:127; EJB:SPEC:231; EJB:SPEC:128
   * 
   * @test_Strategy: Call the createMETHOD method of an Entity Bean and verify
   * it was successful.
   */

  public void test5() throws Fault {
    boolean pass = true;
    try {
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.createPhone(5, "617-225-9086",
          "781-288-9843");
      beanRef.initLogging(props);
      logMsg("Look up Phone by Primary Key");
      beanRef2 = (TestBean) beanHome.findByPrimaryKey(new Integer(5));
      pass = true;
      logMsg("Phone Found by Primary Key ");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception test5: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      try {
        if (beanRef != null) {
          beanRef.remove();
          TestUtil.logTrace("Removing beanRef from test5");
        } else {
          TestUtil.logTrace("beanRef in test5 is null");
        }
      } catch (Exception e) {
        TestUtil.logErr("Caught exception test5: " + e);
        TestUtil.printStackTrace(e);
      }
    }
    if (!pass)
      throw new Fault("test5 failed");
  }

  /*
   * @testName: test6
   * 
   * @assertion_ids: EJB:SPEC:141; EJB:SPEC:299; EJB:SPEC:307
   * 
   * @test_Strategy: An enity bean's home interface can define one or more home
   * methods that the bean provider supplies for business logic that is not
   * specific to an entity bean instance. Call the home method on different bean
   * instances and verify the results.
   */

  public void test6() throws Fault {
    TestBean beanRef = null;
    TestBean beanRef2 = null;
    TestBean beanRef3 = null;
    boolean pass = true;
    double fee = 50.00;
    double minimumBal = 500.00;
    double expectedBal1 = 509.00;
    double expectedBal2 = 2025.00;
    double expectedBal3 = 350.00;
    Collection b1 = null;

    try {
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(3, "103-203-303", "MASTERCARD",
          459.00, "4569543201237989", "0801");
      beanRef.initLogging(props);

      beanRef2 = (TestBean) beanHome.create(4, "104-204-304", "AMEX", 2025.00,
          "8365901745363321", "0502");
      logMsg("Initialize logging");
      beanRef2.initLogging(props);

      beanRef3 = (TestBean) beanHome.create(5, "105-205-305", "VISA", 300.00,
          "763839306103976", "0703");
      logMsg("Initialize logging");
      beanRef3.initLogging(props);

      TestUtil.logTrace("Check card balances before fees: " + " MASTERCARD is: "
          + beanRef.getCardBalance() + " AMEX is: " + beanRef2.getCardBalance()
          + " VISA is: " + beanRef3.getCardBalance());

      b1 = beanHome.findAllBeans();
      if (!b1.isEmpty()) {
        TestUtil.logTrace("Collection not empty.  Size is: " + b1.size());
        Iterator i1 = b1.iterator();
        while (i1.hasNext()) {
          Object o = i1.next();
          TestBean bRef = (TestBean) PortableRemoteObject.narrow(o,
              TestBean.class);
          if (bRef.getCardBalance() < minimumBal) {
            Integer key = (Integer) bRef.getPrimaryKey();
            beanHome.addCardFee(key, fee);
          } else {
            TestUtil.logTrace("Account has minimum balance");
          }
        }
      } else {
        TestUtil.logTrace("Collection is empty");
        pass = false;
      }

      double currentBalance1 = beanRef.getCardBalance();
      double currentBalance2 = beanRef2.getCardBalance();
      double currentBalance3 = beanRef3.getCardBalance();

      if (currentBalance1 == expectedBal1 && currentBalance2 == expectedBal2
          && currentBalance3 == expectedBal3) {
        pass = true;
      } else {
        TestUtil.logErr("Did not get expected results: " + "Expected: "
            + expectedBal1 + " and " + expectedBal2 + " and " + expectedBal3
            + ".  Got: " + currentBalance1 + " and " + currentBalance2
            + currentBalance3);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception test6: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      try {
        if (beanRef != null) {
          beanRef.remove();
          TestUtil.logTrace("Removing beanRef from test6");
        } else {
          TestUtil.logTrace("beanRef in test6 is null");
        }
        if (beanRef2 != null) {
          beanRef2.remove();
          TestUtil.logTrace("Removing beanRef2 from test6");
        } else {
          TestUtil.logTrace("beanRef2 in test6 is null");
        }
        if (beanRef3 != null) {
          beanRef3.remove();
          TestUtil.logTrace("Removing beanRef3 from test6");
        } else {
          TestUtil.logTrace("beanRef3 in test6 is null");
        }
      } catch (Exception e) {
        TestUtil.logErr("Caught exception test6: " + e);
        TestUtil.printStackTrace(e);
      }

      if (!pass)
        throw new Fault("test6 failed");
    }
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  private void prepareTestData() {
    TestUtil.logMsg("Entering method prepareTestData");
    Collection c1 = null;

    try {
      c1 = beanHome.findAllBeans();
      if (!c1.isEmpty()) {
        TestUtil.logTrace("Collection not empty.  Size is: " + c1.size());
        Iterator i1 = c1.iterator();
        while (i1.hasNext()) {
          Object o = i1.next();
          TestBean bRef = (TestBean) PortableRemoteObject.narrow(o,
              TestBean.class);
          for (int l = 0; l < c1.size(); l++)
            bRef.remove();
        }
      } else {
        TestUtil.logTrace("No entity data to clean up");
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception caught preparing test data:" + e, e);
    }
    TestUtil.logTrace("Exiting method prepareTestData");
  }
}
