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

package com.sun.ts.tests.compat12.ejb.entitycmptest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.util.*;
import javax.ejb.*;
import javax.naming.*;
import java.rmi.*;

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
   * @testName: compat12EntityCmpTest1
   * 
   * @assertion_ids: JavaEE:SPEC:283
   *
   * @test_Strategy: Client test to demonstrate entitybean CMP1.1 persistence is
   * automatically maintained by the container. Test loops performing a
   * get/update to the cmp field data followed by a data comparison. This test
   * is built on the J2EE platform to ensure application portability.
   *
   */

  public void compat12EntityCmpTest1() throws Fault {
    int errors = 0;
    try {
      pkey = new Integer(1);
      logMsg("Create entity EJB with Primary Key = " + pkey.toString());
      beanRef = (TestBean) beanHome.create(props, pkey.intValue(), "coffee-1",
          1);
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
        throw new Fault("compat12EntityCmpTest1 failed");
      }

    } catch (Exception e) {
      throw new Fault("compat12EntityCmpTest1 failed", e);
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
      }
    }
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }
}
