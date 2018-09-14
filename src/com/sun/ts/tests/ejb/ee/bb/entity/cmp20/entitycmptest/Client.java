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
 * @(#)Client.java	1.10 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.entity.cmp20.entitycmptest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.util.*;
import javax.ejb.*;
import javax.naming.*;
import java.rmi.*;
import java.lang.*;

import com.sun.javatest.Status;

public class Client extends EETest {
  private static final int NUMLOOPS = 5;

  private static final String testBean = "java:comp/env/ejb/TestBean";

  private static Properties props = null;

  private static TSNamingContext nctx = null;

  private TestBeanHome beanHome = null;

  private TestBean beanRef = null;

  private Integer pkey = null;

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
      logMsg("Setup ok");
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /* Run test */

  /*
   * @testName: test1
   * 
   * @assertion_ids: EJB:SPEC:162.4; EJB:SPEC:162.5
   * 
   * @test_Strategy: Client test to demonstrate entitybean CMP2.0 persistence is
   * automatically maintained by the container. Test loops performing a
   * get/update to the cmp field data followed by a data comparison. If any
   * comparison yields an error the test fails.
   *
   */

  public void test1() throws Fault {
    int errors = 0;
    byte[] b = { 0 };
    try {
      pkey = new Integer(1);
      logMsg("Create entity EJB with Primary Key = " + pkey.toString());
      beanRef = (TestBean) beanHome.create(pkey.intValue(), "coffee-1", 1, b);
      beanRef.initLogging(props);
      logMsg("Find entity EJB with Primary Key = " + pkey.toString());
      TestBean beanRef = (TestBean) beanHome.findByPrimaryKey(pkey);
      logMsg("Initialize remote logging");
      beanRef.initLogging(props);
      logMsg("Calling entity EJB methods getPrice()/updatePrice()");
      float price = (float) 1;
      for (int i = 0; i < NUMLOOPS; i++) {
        float currentPrice = beanRef.getPrice();
        beanRef.updatePrice((float) (currentPrice + 1));
        float updatePrice = beanRef.getPrice();
        logMsg("Entity EJB currentPrice = " + currentPrice + ", updatePrice = "
            + updatePrice);

        if (currentPrice != price
            && updatePrice != (float) (currentPrice + 1)) {
          errors++;
          if (currentPrice != price)
            TestUtil.logErr("currentPrice mismatch, expected" + price);
          if (updatePrice != (float) (currentPrice + 1))
            TestUtil.logErr(
                "updatePrice mismatch, expected" + (float) (currentPrice + 1));
        }
        price = updatePrice;
      }

      if (errors > 0) {
        logErr("The number of errors were: " + errors);
        throw new Fault("test1 failed");
      }

    } catch (Exception e) {
      throw new Fault("test1 failed", e);
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
  }

  /*
   * @testName: test2
   * 
   * @assertion_ids: EJB:SPEC:172; EJB:SPEC:173
   * 
   * @test_Strategy: Assignment of dependent value class values to a cmp-field
   * using the set accessor method causes the value to be copied to the target
   * cmp field.
   *
   */

  public void test2() throws Fault {
    boolean testResult1 = false;
    boolean testResult2 = false;
    boolean testResult3 = false;
    byte[] b = { 31, 32, 33, 63, 64, 65 };
    byte bv = 5;

    try {
      pkey = new Integer(2);
      logMsg("Create entity EJB with Primary Key = " + pkey.toString());
      beanRef = (TestBean) beanHome.create(pkey.intValue(), "coffee-2", 2, b);
      beanRef.initLogging(props);

      TestUtil.logMsg("test2a");
      byte[] a = beanRef.getB();
      a[0] = (byte) (a[0] + bv);
      b = beanRef.getB();

      if (Arrays.equals(a, b)) {
        TestUtil
            .logMsg("ERROR: Unexpected result in array comparison in test2a. ");
        for (int i = 0; i < a.length; i++) {
          TestUtil.logMsg("Array a in test2a equals: " + a[i]);
        }
        for (int j = 0; j < b.length; j++) {
          TestUtil.logMsg("Array b in test2a equals: " + b[j]);
        }
        testResult1 = false;
      } else {
        TestUtil.logMsg("Expected results received for test2a");
        testResult1 = true;
      }

      TestUtil.logMsg("test2b");
      beanRef.setB(a);
      byte[] c = beanRef.getB();

      if (!Arrays.equals(a, c)) {
        TestUtil
            .logMsg("ERROR: Unexpected result in array comparison in test2b. ");
        for (int k = 0; k < a.length; k++) {
          TestUtil.logMsg("Array a in test2b equals: " + a[k]);
        }
        for (int l = 0; l < c.length; l++) {
          TestUtil.logMsg("Array c in test2b equals: " + c[l]);
        }
        testResult2 = false;
      } else {
        TestUtil.logMsg("Expected results received for test2b");
        testResult2 = true;
      }

      TestUtil.logMsg("test2c");
      a[1] = (byte) (a[1] + bv);

      if (Arrays.equals(a, c)) {
        TestUtil
            .logMsg("ERROR: Unexpected result in array comparison in test2c. ");
        for (int m = 0; m < a.length; m++) {
          TestUtil.logMsg("Array a in test2c equals: " + a[m]);
        }
        for (int n = 0; n < c.length; n++) {
          TestUtil.logMsg("Array c in test2c equals: " + c[n]);
        }
        testResult3 = false;
      } else {
        TestUtil.logMsg("Expected results received for test2c");
        testResult3 = true;
      }

    } catch (Exception e) {
      TestUtil.logErr("Exception caught during test2", e);
      TestUtil.printStackTrace(e);
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }

    if (!testResult1 || !testResult2 || !testResult3)
      throw new Fault("test2 failed");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }
}
