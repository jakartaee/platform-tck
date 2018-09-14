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
 * @(#)Client.java	1.11 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.entity.cmp20.unknownpktest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.lang.*;
import java.util.*;
import javax.ejb.*;

import com.sun.javatest.Status;

public class Client extends EETest {
  private static final String testName = "UnknownPKTest";

  private static final String testBean = "java:comp/env/ejb/TestBean";

  private static final String testProps = "unknownpktest.properties";

  private TestBean beanRef = null;

  private TestBean beanRef1 = null;

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
      logMsg("Setup ok");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("Setup failed:", e);
    }
  }

  /* Run test */

  /*
   * @testName: unknownPKTest1
   * 
   * @assertion_ids: EJB:SPEC:311
   * 
   * @test_Strategy: Call the create method of an Entity Bean and verify the
   * instance can be retrieved via the Primary Key.
   */

  public void unknownPKTest1() throws Fault {
    boolean pass = true;
    Object PK = null;
    try {
      TestUtil.logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create("John", "Edward", "Brown",
          "100-200-300");
      TestUtil.logMsg("Instance Created");
      beanRef.initLogging(props);

      TestUtil.logMsg("Create another EJB instance");
      beanRef1 = (TestBean) beanHome.create("William", "James", "Black",
          "450-978-568");
      TestUtil.logMsg("Instance Created");
      beanRef1.initLogging(props);

      TestUtil.logMsg("Get Customer's Primary Key");
      PK = beanRef.getPrimaryKey();

      TestUtil.logMsg("Find Customer's Primary Key");
      beanRef2 = (TestBean) beanHome.findByPrimaryKey(PK);

      TestUtil.logMsg("Check references");
      if (beanRef.isIdentical(beanRef2)) {
        TestUtil.logMsg("Customer references are identical");
        pass = true;
      } else {
        TestUtil.logErr("Incorrect reference returned");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception unknownPKTest1: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.logErr("Caught exception removing beanRef: " + e);
        TestUtil.printStackTrace(e);
      }
      try {
        beanRef1.remove();
      } catch (Exception e) {
        TestUtil.logErr("Caught exception removing beanRef1: " + e);
        TestUtil.printStackTrace(e);
      }
    }
    if (!pass)
      throw new Fault("unknownPKTest1 failed");
  }

  /*
   * @testName: unknownPKTest2
   * 
   * @assertion_ids: EJB:SPEC:311
   * 
   * @test_Strategy: Call the createHomeAddress method of an Entity Bean and
   * verify the instance can be retrieved via the Primary Key.
   */

  public void unknownPKTest2() throws Fault {
    boolean pass = true;
    Object PK = null;
    try {
      TestUtil.logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.createHomeAddress("10 Circle Street",
          "Weston", "Utah", 91877);
      TestUtil.logMsg("Instance Created");
      beanRef.initLogging(props);

      TestUtil.logMsg("Create AnotherEJB instance");
      beanRef1 = (TestBean) beanHome.createHomeAddress("9 Lowell Avenue",
          "Charleton", "Nevada", 84375);
      TestUtil.logMsg("Second Instance Created");
      beanRef1.initLogging(props);

      TestUtil.logMsg("Get Primary Key");
      PK = beanRef1.getPrimaryKey();

      TestUtil.logMsg("Find By Primary Key");
      beanRef2 = (TestBean) beanHome.findByPrimaryKey(PK);

      TestUtil.logMsg("Check References");
      if (beanRef1.isIdentical(beanRef2)) {
        TestUtil.logMsg("Address references are identical");
        pass = true;
      } else {
        TestUtil.logErr("Incorrect reference returned");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception unknownPKTest2: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.logErr("Caught exception removing beanRef: " + e);
        TestUtil.printStackTrace(e);
      }
      try {
        beanRef1.remove();
      } catch (Exception e) {
        TestUtil.logErr("Caught exception removing beanRef1: " + e);
        TestUtil.printStackTrace(e);
      }
    }
    if (!pass)
      throw new Fault("unknownPKTest2 failed");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }
}
