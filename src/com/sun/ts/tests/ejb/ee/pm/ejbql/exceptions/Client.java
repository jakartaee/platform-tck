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

package com.sun.ts.tests.ejb.ee.pm.ejbql.exceptions;

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
   * @testName: exceptionTest1
   * 
   * @assertion_ids: EJB:JAVADOC:146; EJB:SPEC:285
   * 
   * @test_Strategy: Execute the findProductByName method defined to expect a
   * single-object with the result set that does not exist. Check that an
   * ObjectNotFoundException is thrown.
   *
   */

  public void exceptionTest1() throws Fault {
    boolean pass = true;
    Product p = null;

    try {
      TestUtil.logMsg("Find products with name: Sparcstation 5");
      p = productHome.findProductByName("Sparcstation 5");
      TestUtil.logErr("Exception not thrown as expected");
      pass = false;
    } catch (ObjectNotFoundException e) {
      TestUtil.logMsg("Caught ObjectNotFoundException as expected: " + e);
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception exceptionTest1: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("exceptionTest1 failed", e);
    }

    if (!pass)
      throw new Fault("exceptionTest1 failed");
  }

  /*
   * @testName: exceptionTest2
   * 
   * @assertion_ids: EJB:SPEC:284
   * 
   * @test_Strategy: Execute findAllProductsByName method defined to expect a
   * collection with the result set that returns no matches. Ensure that an
   * exception is not thrown.
   * 
   */

  public void exceptionTest2() throws Fault {

    boolean pass = true;
    String expectedPKs[] = null;
    Collection p = null;
    try {
      TestUtil.logMsg("Find all products by quantity");
      p = productHome.findAllProductsByQuantity();
      expectedPKs = new String[0];
      if (!Util.checkEJBs(p, Schema.PRODUCTREF, expectedPKs)) {
        TestUtil.logErr("UnSuccessfully handled a collection return of 0");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully returned a collection of 0");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception exceptionTest2: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("exceptionTest2 failed", e);
    }

    if (!pass)
      throw new Fault("exceptionTest2 failed");
  }

  /*
   * @testName: exceptionTest3
   * 
   * @assertion_ids: EJB:JAVADOC:146; EJB:SPEC:285
   * 
   * @test_Strategy: Execute ejbSelectProductName method defined to expect a
   * single-object with the result set that does not exist. Check that an
   * ObjectNotFoundException is thrown.
   * 
   */

  public void exceptionTest3() throws Fault {
    boolean pass = true;
    Product p = null;

    try {
      TestUtil.logMsg("Find product by Name: Ultra Sparc 10");
      p = productHome.selectProductByName("Ultra Sparc 10");
      TestUtil.logErr("Exception not thrown as expected");
      pass = false;
    } catch (ObjectNotFoundException e) {
      TestUtil.logMsg("Caught ObjectNotFoundException as expected: " + e);
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception exceptionTest3: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("exceptionTest3 failed", e);
    }

    if (!pass)
      throw new Fault("exceptionTest3 failed");
  }

  /*
   * @testName: exceptionTest4
   * 
   * @assertion_ids: EJB:SPEC:284; EJB:JAVADOC:145
   * 
   * @test_Strategy: Execute ejbSelectAllProducts method defined to expect a
   * collection with the result set that returns no matches. Ensure that an
   * exception is not thrown.
   *
   */

  public void exceptionTest4() throws Fault {

    boolean pass = true;
    String expectedPKs[] = null;
    Collection p = null;
    try {
      TestUtil.logMsg("Find all products by name");
      p = productHome.selectAllProducts();
      expectedPKs = new String[0];
      if (!Util.checkEJBs(p, Schema.PRODUCTREF, expectedPKs)) {
        TestUtil.logErr("UnSuccessfully handled a collection return of 0");
        pass = false;
      } else {
        TestUtil.logMsg("Successfully returned a collection of 0");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception exceptionTest4: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("exceptionTest4 failed", e);
    }

    if (!pass)
      throw new Fault("exceptionTest4 failed");
  }

  /*
   * @testName: exceptionTest5
   * 
   * @assertion_ids: EJB:JAVADOC:119; EJB:SPEC:279; EJB:SPEC:378
   * 
   * @test_Strategy: Execute ejbSelectProductByType method defined to expect a
   * single object with the result set that returns more than one. Ensure that a
   * FinderException is thrown.
   *
   */

  public void exceptionTest5() throws Fault {
    boolean pass = true;
    Product p = null;
    try {
      TestUtil.logMsg("Find PC products");
      p = productHome.selectProductByType();
      TestUtil.logErr("Exception not thrown as expected");
      pass = false;
    } catch (FinderException e) {
      TestUtil.logMsg("Caught FinderException as expected: " + e);
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception exceptionTest5: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("exceptionTest5 failed", e);
    }

    if (!pass)
      throw new Fault("exceptionTest5 failed");
  }

  /*
   * @testName: exceptionTest6
   * 
   * @assertion_ids: EJB:JAVADOC:118; EJB:SPEC:10283; EJB:SPEC:378
   * 
   * @test_Strategy: Execute findProductByName method defined to expect a single
   * object with the result set that returns more than one. Ensure that a
   * FinderException is thrown.
   * 
   */

  public void exceptionTest6() throws Fault {
    boolean pass = true;
    Product p = null;
    try {
      TestUtil.logMsg("Find Programming products");
      p = productHome.findProductByName("Java_2%Programming");
      TestUtil.logErr("Exception not thrown as expected");
      pass = false;
    } catch (FinderException e) {
      TestUtil.logMsg("Caught FinderException as expected");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception exceptionTest6: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("exceptionTest6 failed", e);
    }

    if (!pass)
      throw new Fault("exceptionTest6 failed");
  }

  public void cleanup() throws Fault {
    TestUtil.logMsg("cleanup ok");
  }
}
