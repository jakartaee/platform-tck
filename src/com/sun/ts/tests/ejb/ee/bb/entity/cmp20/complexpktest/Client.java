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
 * @(#)Client.java	1.19 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.entity.cmp20.complexpktest;

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
  private static final String testBean = "java:comp/env/ejb/TestBean";

  private static final String lineItemBean = "java:comp/env/ejb/LineItem";

  private TestBean beanRef = null;

  private Properties props = new Properties();

  private TestBeanHome beanHome = null;

  private LineItemHome liHome = null;

  private TSNamingContext nctx = null;

  private boolean setupOK = false;

  private static final int NUMEJBS = 4;

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
      logMsg("Looking up home interface for EJB: " + lineItemBean);
      liHome = (LineItemHome) nctx.lookup(lineItemBean, LineItemHome.class);
      setupOK = true;
      logMsg("Setup ok");
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }

  }

  /* Run test */

  /*
   * @testName: test1
   * 
   * @assertion_ids: EJB:SPEC:310.2; EJB:SPEC:131; EJB:SPEC:132; EJB:SPEC:133
   * 
   * @test_Strategy: Create several Entity EJB's via the EJBHome interface where
   * the primary key maps to multiple fields. Deploy them on the J2EE server.
   * Find the instances using findByPrimaryKey.
   *
   */

  public void test1() throws Fault {

    boolean pass = false;

    TestBean[] beanRef = new TestBean[NUMEJBS];
    TestBean testRef1 = null;
    TestBean testRef2 = null;

    try {
      TestUtil.logMsg("Create EJB instance test1");
      beanRef[0] = (TestBean) beanHome.create(1, "Hazelnut", 1.00, 1, "USA");
      beanRef[0].initLogging(props);
      beanRef[1] = (TestBean) beanHome.create(1, "Villacreme", 1.25, 2,
          "Columbia");
      beanRef[1].initLogging(props);
      beanRef[2] = (TestBean) beanHome.create(1, "MochoJava", 1.50, 3,
          "Brazil");
      beanRef[2].initLogging(props);
      beanRef[3] = (TestBean) beanHome.create(2, "Hazelnut", 1.75, 4, "USA");
      beanRef[3].initLogging(props);

      testRef1 = (TestBean) beanHome
          .findByPrimaryKey(new ComplexPK(1, "Hazelnut"));

      testRef2 = (TestBean) beanHome
          .findByPrimaryKey(new ComplexPK(1, "Villacreme"));

      if (testRef1.isIdentical(beanRef[0]) && testRef2.isIdentical(beanRef[1]))
        pass = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught exception test1: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test1 failed", e);
    } finally {
      try {
        for (int i = 0; i < NUMEJBS; i++)
          beanRef[i].remove();

      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }

    if (!pass)
      throw new Fault("test1 failed");
  }

  /*
   * @testName: test2
   * 
   * @assertion_ids: EJB:SPEC:384; EJB:SPEC:406.2; EJB:SPEC:392; EJB:SPEC:390
   * 
   * @test_Strategy: Create several Entity EJB's via the EJBHome interface where
   * the primary key maps to multiple fields. Deploy them on the J2EE server.
   * Execute an EJB QL query using aggregate function COUNT where the bean
   * instance has a complex primary key.
   *
   */

  public void test2() throws Fault {

    boolean pass = false;

    TestBean[] beanRef = new TestBean[3];
    LineItem[] lineItemRef = new LineItem[5];
    int expectedResult = 3;
    int result = 0;

    try {
      TestUtil.logMsg("Create LineItem instance data");
      lineItemRef[0] = (LineItem) liHome.create("1", 1);
      lineItemRef[1] = (LineItem) liHome.create("2", 2);
      lineItemRef[2] = (LineItem) liHome.create("3", 5);
      lineItemRef[3] = (LineItem) liHome.create("4", 1);
      lineItemRef[4] = (LineItem) liHome.create("5", 3);
      TestUtil.logMsg("Create EJB instance data");
      beanRef[0] = (TestBean) beanHome.create(1, "Hazelnut", 1.00, 1, "USA");
      beanRef[0].addLineItem(lineItemRef[0]);
      beanRef[0].addLineItem(lineItemRef[3]);
      beanRef[0].initLogging(props);

      beanRef[1] = (TestBean) beanHome.create(1, "Villacreme", 1.25, 2,
          "Columbia");
      beanRef[1].addLineItem(lineItemRef[1]);
      beanRef[1].initLogging(props);

      beanRef[2] = (TestBean) beanHome.create(1, "MochoJava", 1.50, 3,
          "Brazil");
      beanRef[2].addLineItem(lineItemRef[2]);
      beanRef[2].addLineItem(lineItemRef[4]);
      beanRef[2].initLogging(props);

      TestUtil.logMsg("Count the number of brandNames available");
      result = liHome.selectCountByBrandName();
      if (result != expectedResult) {
        pass = false;
      } else {
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception test2: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test2 failed", e);
    }

    finally {
      try {
        TestUtil.logTrace("Removing Bean Instances");
        for (int i = 0; i < 3; i++)
          beanRef[i].remove();

      } catch (Exception e) {
        TestUtil.logErr("Exception caught removing bean instance data" + e, e);
      }
    }

    if (!pass)
      throw new Fault("test2 failed");
  }

  /*
   * @testName: test3
   * 
   * @assertion_ids: EJB:SPEC:297
   * 
   * @test_Strategy: Create several Entity EJB's via the EJBHome interface.
   * Deploy them on the J2EE server. Perform a find of a range of Entity EJB's
   * and verify that the correct Entity EJB's were found.
   *
   */

  public void test3() throws Fault {

    boolean pass = true;
    TestBean[] beanRef = new TestBean[NUMEJBS];
    TestBean[] testRef = new TestBean[NUMEJBS];

    try {
      TestUtil.logMsg("Create EJB instance test3");
      beanRef[0] = (TestBean) beanHome.create(1, "Hazelnut", 1.00, 1, "USA");
      beanRef[0].initLogging(props);
      beanRef[1] = (TestBean) beanHome.create(1, "Villacreme", 1.25, 2,
          "Columbia");
      beanRef[1].initLogging(props);
      beanRef[2] = (TestBean) beanHome.create(1, "MochoJava", 1.50, 3,
          "Brazil");
      beanRef[2].initLogging(props);
      beanRef[3] = (TestBean) beanHome.create(2, "Hazelnut", 1.75, 4, "USA");
      beanRef[3].initLogging(props);

      Collection c = beanHome.findByName("Hazelnut");

      TestUtil.logMsg("Check if we found the correct EJB references");
      TestUtil.logMsg("Number of EJB references returned = " + c.size());

      Iterator i = c.iterator();
      int j = 0;
      while (i.hasNext())
        testRef[j++] = (TestBean) PortableRemoteObject.narrow(i.next(),
            TestBean.class);
      if (c.size() != 2) {
        TestUtil.logErr("findByName returned " + c.size()
            + " references, expected 2 references");
        pass = false;
      }

      else {

        for (int k = 0; k < c.size(); k++) {
          boolean found = false;
          for (int l = 0; l < 4; l++) {
            if (beanRef[l].isIdentical(testRef[k])) {
              found = true;
              break;
            }
          }
          if (!found) {
            TestUtil.logErr(
                "findByName returned " + "incorrect reference for k=" + k);
            pass = false;
          } else {
            TestUtil.logMsg(
                "findByName returned " + "correct reference for k=" + k);
          }
        }
        if (testRef[0].isIdentical(testRef[1])) {
          TestUtil.logErr("findByName returned references not all unique");
          pass = false;
        }
      }

    }

    catch (Exception e) {
      TestUtil.logErr("Caught exception test3: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test3 failed", e);
    } finally {
      try {
        for (int i = 0; i < NUMEJBS; i++)
          beanRef[i].remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }

    if (!pass)
      throw new Fault("test3 failed");
  }

  /*
   * @testName: test4
   * 
   * @assertion_ids: EJB:SPEC:296
   * 
   * @test_Strategy: Create several Entity EJB's via the EJBHome interface.
   * Deploy them on the J2EE server. Perform a find of a range of Entity EJB's
   * and verify that the correct Entity EJB's were found.
   *
   */

  public void test4() throws Fault {

    boolean pass = true;
    TestBean[] beanRef = new TestBean[NUMEJBS];
    TestBean[] testRef = new TestBean[NUMEJBS];

    try {
      TestUtil.logMsg("Create EJB instance test4");
      beanRef[0] = (TestBean) beanHome.create(1, "Hazelnut", 1.00, 1, "USA");
      beanRef[0].initLogging(props);
      beanRef[1] = (TestBean) beanHome.create(1, "Villacreme", 1.25, 2,
          "Columbia");
      beanRef[1].initLogging(props);
      beanRef[2] = (TestBean) beanHome.create(1, "MochoJava", 1.50, 3,
          "Brazil");
      beanRef[2].initLogging(props);
      beanRef[3] = (TestBean) beanHome.create(2, "Hazelnut", 1.75, 4, "USA");
      beanRef[3].initLogging(props);

      Collection c = beanHome.findById(new Integer(1));

      TestUtil.logMsg("Check if we found the correct EJB references");
      TestUtil.logMsg("Number of EJB references returned = " + c.size());

      Iterator i = c.iterator();
      int j = 0;
      while (i.hasNext())
        testRef[j++] = (TestBean) PortableRemoteObject.narrow(i.next(),
            TestBean.class);
      if (c.size() != 3) {
        TestUtil.logErr("findById returned " + c.size()
            + " references, expected 3 references");
        pass = false;
      }

      else {
        for (int k = 0; k < c.size(); k++) {
          boolean found = false;
          for (int l = 0; l < 4; l++) {
            if (beanRef[l].isIdentical(testRef[k])) {
              found = true;
              break;
            }
          }
          if (!found) {
            TestUtil.logErr(
                "findById returned " + "incorrect reference for k=" + k);
            pass = false;
          } else {
            TestUtil
                .logMsg("findById returned " + "correct reference for k=" + k);
          }
        }
        if (testRef[0].isIdentical(testRef[1])
            || testRef[1].isIdentical(testRef[2])
            || testRef[0].isIdentical(testRef[2])) {
          TestUtil.logErr("findById returned references not all unique");
          pass = false;
        }
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception test4: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test4 failed", e);
    } finally {
      try {
        for (int i = 0; i < NUMEJBS; i++)
          beanRef[i].remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }

    if (!pass)
      throw new Fault("test4 failed");
  }

  /*
   * @testName: test5
   * 
   * @assertion_ids: EJB:SPEC:296
   * 
   * @test_Strategy: Create several Entity EJB's via the EJBHome interface.
   * Deploy them on the J2EE server. Perform a find of one of the Entity EJB's
   * and verify that the correct Entity EJB was found.
   *
   */

  public void test5() throws Fault {

    boolean pass = true;
    TestBean[] beanRef = new TestBean[NUMEJBS];
    TestBean testRef = null;

    try {
      TestUtil.logMsg("Create EJB instance test5");
      beanRef[0] = (TestBean) beanHome.create(1, "Hazelnut", 1.00, 1, "USA");
      beanRef[0].initLogging(props);
      beanRef[1] = (TestBean) beanHome.create(1, "Villacreme", 1.25, 2,
          "Columbia");
      beanRef[1].initLogging(props);
      beanRef[2] = (TestBean) beanHome.create(1, "MochoJava", 1.50, 3,
          "Brazil");
      beanRef[2].initLogging(props);
      beanRef[3] = (TestBean) beanHome.create(2, "Hazelnut", 1.75, 4, "USA");
      beanRef[3].initLogging(props);

      Collection c = beanHome.findByPrice(1.25);

      TestUtil.logMsg("Check if we found the correct EJB references");
      TestUtil.logMsg("Number of EJB references returned = " + c.size());

      if (c.size() != 1) {
        TestUtil.logErr("findByPrice returned " + c.size()
            + " references, expected 1 reference");
        pass = false;
      } else {
        TestUtil.logMsg("Check if we found the correct EJB reference");
        Iterator i = c.iterator();
        testRef = (TestBean) PortableRemoteObject.narrow(i.next(),
            TestBean.class);
        if (beanRef[1].isIdentical(testRef)) {
          TestUtil.logMsg("findByPrice returned correct reference");
          pass = true;
        } else {
          TestUtil.logErr("findByPrice returned incorrect reference");
          pass = false;
        }
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception test5: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test5 failed", e);
    } finally {
      try {
        for (int i = 0; i < NUMEJBS; i++)
          beanRef[i].remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }

    if (!pass)
      throw new Fault("test5 failed");
  }

  /*
   * @testName: test6
   * 
   * @assertion_ids: EJB:SPEC:289; EJB:JAVADOC:119
   * 
   * @test_Strategy: The container throws the FinderException from the
   * implementation of a finder or select method to indicate an
   * application-level error in the finder or select method. Create several
   * Entity EJB's via the EJBHome interface. Deploy them on the J2EE server.
   *
   */

  public void test6() throws Fault {

    boolean pass = false;

    TestBean[] beanRef = new TestBean[NUMEJBS];
    TestBean testRef = null;

    try {
      TestUtil.logMsg("Create EJB instance test6");
      beanRef[0] = (TestBean) beanHome.create(1, "Hazelnut", 1.00, 1, "USA");
      beanRef[0].initLogging(props);
      beanRef[1] = (TestBean) beanHome.create(1, "Villacreme", 1.25, 2,
          "Columbia");
      beanRef[1].initLogging(props);
      beanRef[2] = (TestBean) beanHome.create(1, "MochoJava", 1.50, 3,
          "Brazil");
      beanRef[2].initLogging(props);
      beanRef[3] = (TestBean) beanHome.create(2, "Hazelnut", 1.75, 4, "USA");
      beanRef[3].initLogging(props);

      testRef = (TestBean) beanHome.findByPrimaryKey(new ComplexPK(1, "foo"));

      TestUtil.logErr("No finder exception occurred");
      pass = false;

    } catch (FinderException fe) {
      TestUtil.logMsg("Caught FinderException as expected: " + fe);
      pass = true;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test6 failed", e);
    } finally {
      try {
        for (int i = 0; i < NUMEJBS; i++)
          beanRef[i].remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }

    if (!pass)
      throw new Fault("test6 failed");
  }

  /*
   * @testName: test7
   * 
   * @assertion_ids: EJB:SPEC:297
   * 
   * @test_Strategy: Create several Entity EJB's via the EJBHome interface.
   * Deploy them on the J2EE server. Perform a find of a range of Entity EJB's
   * and verify that the correct Entity EJB's were found.
   *
   */

  public void test7() throws Fault {

    boolean pass = true;
    TestBean[] beanRef = new TestBean[NUMEJBS];
    TestBean[] testRef = new TestBean[NUMEJBS];

    try {
      TestUtil.logMsg("Create EJB instance test7");
      beanRef[0] = (TestBean) beanHome.create(1, "Hazelnut", 1.00, 1, "USA");
      beanRef[0].initLogging(props);
      beanRef[1] = (TestBean) beanHome.create(1, "Villacreme", 1.25, 2,
          "Columbia");
      beanRef[1].initLogging(props);
      beanRef[2] = (TestBean) beanHome.create(1, "MochoJava", 1.50, 3,
          "Brazil");
      beanRef[2].initLogging(props);
      beanRef[3] = (TestBean) beanHome.create(2, "Hazelnut", 1.75, 4, "USA");
      beanRef[3].initLogging(props);

      Collection c = beanHome.findByProduct();

      TestUtil.logMsg("Check if we found the correct EJB references");
      TestUtil.logMsg("Number of EJB references returned = " + c.size());

      Iterator i = c.iterator();
      int j = 0;
      while (i.hasNext())
        testRef[j++] = (TestBean) PortableRemoteObject.narrow(i.next(),
            TestBean.class);
      if (c.size() != 4) {
        TestUtil.logErr("findByProduct returned " + c.size()
            + " references, expected 4 references");
        pass = false;
      }

      else {

        for (int k = 0; k < c.size(); k++) {
          boolean found = false;
          for (int l = 0; l < 4; l++) {
            if (beanRef[l].isIdentical(testRef[k])) {
              found = true;
              break;
            }
          }
          if (!found) {
            TestUtil.logErr(
                "findByProduct returned " + "incorrect reference for k=" + k);
            pass = false;
          } else {
            TestUtil.logMsg(
                "findByProduct returned " + "correct reference for k=" + k);
          }
        }
        if (testRef[0].isIdentical(testRef[1])
            || testRef[1].isIdentical(testRef[2])
            || testRef[2].isIdentical(testRef[3])
            || testRef[0].isIdentical(testRef[2])
            || testRef[0].isIdentical(testRef[3])
            || testRef[1].isIdentical(testRef[3])) {
          TestUtil.logErr("findByProduct returned references not all unique");
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception test7: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test7 failed", e);
    } finally {
      try {
        for (int i = 0; i < NUMEJBS; i++)
          beanRef[i].remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }

    if (!pass)
      throw new Fault("test7 failed");
  }

  /*
   * @testName: test8
   * 
   * @assertion_ids: EJB:SPEC:297
   * 
   * @test_Strategy: Create several Entity EJB's via the EJBHome interface.
   * Deploy them on the J2EE server. Perform a find of a range of Entity EJB's
   * and verify that the correct Entity EJB's were found. Verify EJB QL query
   * returns correct result set with compound PK.
   *
   */

  public void test8() throws Fault {

    boolean pass = true;
    TestBean[] beanRef = new TestBean[NUMEJBS];
    TestBean[] testRef = new TestBean[NUMEJBS];

    try {
      TestUtil.logMsg("Create EJB instance test8");
      beanRef[0] = (TestBean) beanHome.create(1, "Hazelnut", 1.00, 1, "USA");
      beanRef[0].initLogging(props);
      beanRef[1] = (TestBean) beanHome.create(1, "Villacreme", 1.25, 2,
          "Columbia");
      beanRef[1].initLogging(props);
      beanRef[2] = (TestBean) beanHome.create(1, "MochoJava", 1.50, 3,
          "Brazil");
      beanRef[2].initLogging(props);
      beanRef[3] = (TestBean) beanHome.create(2, "Hazelnut", 1.75, 4, "USA");
      beanRef[3].initLogging(props);

      Collection c = beanHome.findByType();

      TestUtil.logMsg("Check if we found the correct EJB references");
      TestUtil.logMsg("Number of EJB references returned = " + c.size());

      Iterator i = c.iterator();
      int j = 0;
      while (i.hasNext())
        testRef[j++] = (TestBean) PortableRemoteObject.narrow(i.next(),
            TestBean.class);
      if (c.size() != 4) {
        TestUtil.logErr("findByType returned " + c.size()
            + " references, expected 4 references");
        pass = false;
      }

      else {

        for (int k = 0; k < c.size(); k++) {
          boolean found = false;
          for (int l = 0; l < 4; l++) {
            if (beanRef[l].isIdentical(testRef[k])) {
              found = true;
              break;
            }
          }
          if (!found) {
            TestUtil.logErr(
                "findByType returned " + "incorrect reference for k=" + k);
            pass = false;
          } else {
            TestUtil.logMsg(
                "findByType returned " + "correct reference for k=" + k);
          }
        }
        if (testRef[0].isIdentical(testRef[1])
            || testRef[1].isIdentical(testRef[2])
            || testRef[2].isIdentical(testRef[3])
            || testRef[0].isIdentical(testRef[2])
            || testRef[0].isIdentical(testRef[3])
            || testRef[1].isIdentical(testRef[3])) {

          TestUtil.logErr("findByType returned references not all unique");
          pass = false;
        }
      }

    }

    catch (Exception e) {
      TestUtil.logErr("Caught exception test8: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("test8 failed", e);
    } finally {
      try {
        for (int i = 0; i < NUMEJBS; i++)
          beanRef[i].remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }

    if (!pass)
      throw new Fault("test8 failed");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

}
