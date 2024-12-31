/*
 * Copyright (c) 2007, 2024 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.samples.ejb.ee.twobean;

import java.util.Properties;

import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;

import jakarta.ejb.EJBException;

public class TestBean1EJB {
  private Properties props = null;

  private Properties testProps = null;

  // testBean2 JNDI name
  private static final String testBean2 = "java:comp/env/ejb/TestBean2";

  private TestBean2 ref = null;

  public void initialize(Properties p) {
    TestUtil.logTrace("initialize");
    testProps = p;
    try {
      TestUtil.init(p);
    } catch (Exception e) {
      TestUtil.logErr("init failed", e);
      throw new EJBException("initialize failed" + e.getMessage());
    }
  }

  // ===========================================================
  // TestBean interface (our business methods)

  public boolean simpleTest1() {
    boolean pass = true;
    TSNamingContext jc = null;

    TestUtil.logTrace("simpleTest1");
    TestUtil.logMsg("call business method in another bean");

    try {
      // lookup EJB
      jc = new TSNamingContext();
      ref = (TestBean2) jc.lookup(testBean2, TestBean2.class);
      TestUtil.logMsg("TestBean2Ref=" + ref);
      // call initLogging
      ref.initLogging(testProps);
      ref.bean2Test();
      pass = true;
    } catch (EJBException ex) {
      TestUtil.logErr("call to Bean2 failed", ex);
      pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
      pass = false;
    }
    return pass;
  }

  public boolean simpleTest2(int n) {
    boolean pass = false;
    TestUtil.logTrace("simpleTest2");
    try {
      TestUtil.logMsg("Sleeping " + n + " milliseconds");
      Thread.sleep(n);
      TestUtil.logMsg("Finished sleeping ... ");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      // TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  // ===========================================================

}
