/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.el.api.javax_el.elcontext;

import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.el.ELContext;
import javax.el.ELManager;
import javax.el.EvaluationListener;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.el.common.util.ELTestUtil;

public class ELClient extends ServiceEETest {

  private Properties testProps;

  public static void main(String[] args) {
    ELClient theTests = new ELClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {
    this.testProps = p;
  }

  /**
   * Does nothing...
   * 
   * @throws Fault
   */
  public void cleanup() throws Fault {
    // does nothing at this point
  }

  /**
   * @testName: elContextPutGetContextTest
   * @assertion_ids: EL:JAVADOC:33; EL:JAVADOC:39; EL:JAVADOC:328;
   *                 EL:JAVADOC:326; EL:JAVADOC:321
   * @test_Strategy: Assert that we get back the expected value from
   *                 getContext() that we put in with putContext().
   * 
   * @since: 3.0
   */

  public void elContextPutGetContextTest() throws Fault {
    ELManager elm = new ELManager();
    ELContext elc = elm.getELContext();
    String testStr = "TCKContext";

    elc.putContext(String.class, testStr);
    String result = elc.getContext(String.class).toString();

    if (!testStr.equals(result)) {
      throw new Fault(ELTestUtil.FAIL + " Unexpected Context Returned!"
          + TestUtil.NEW_LINE + "Expected: " + testStr + TestUtil.NEW_LINE
          + "Received: " + result);
    }

  } // end elContextPutGetContextTest

  /**
   * @testName: elContextGetSetLocaleTest
   * @assertion_ids: EL:JAVADOC:36; EL:JAVADOC:40; EL:JAVADOC:328;
   *                 EL:JAVADOC:329
   * @test_Strategy: Assert that we get back the Locale we set.
   * 
   * @since: 3.0
   */

  public void elContextGetSetLocaleTest() throws Fault {
    ELManager elm = new ELManager();
    ELContext elc = elm.getELContext();

    String disName = "english";
    Locale en = new Locale(disName);
    elc.setLocale(en);
    String result = elc.getLocale().getDisplayName();

    if (!result.equalsIgnoreCase(disName)) {
      throw new Fault(ELTestUtil.FAIL + " Unexpected Locale Returned!"
          + TestUtil.NEW_LINE + "Expected: " + disName + TestUtil.NEW_LINE
          + "Received: " + result);
    }

  } // end elContextGetSetLocaleTest

  /**
   * @testName: elContextIsSetPropertyResolvedTest
   * @assertion_ids: EL:JAVADOC:38; EL:JAVADOC:41; EL:JAVADOC:328;
   *                 EL:JAVADOC:329; EL:JAVADOC:322
   * @test_Strategy: Assert that when we call setPropertyResolved that
   *                 isPropertyResolved returns true.
   * 
   * @since: 3.0
   */

  public void elContextIsSetPropertyResolvedTest() throws Fault {
    ELManager elm = new ELManager();
    ELContext elc = elm.getELContext();

    boolean isProp = elc.isPropertyResolved();

    if (isProp) {
      throw new Fault(
          ELTestUtil.FAIL + " Unexpected result from isPropertyResolved()!"
              + TestUtil.NEW_LINE + "Should have been false!");
    }

    elc.setPropertyResolved(true);
    isProp = elc.isPropertyResolved();

    if (!isProp) {
      throw new Fault(
          ELTestUtil.FAIL + " Unexpected result from isPropertyResolved()!"
              + TestUtil.NEW_LINE + "Should have been true!");
    }

  } // end elContextIsSetPropertyResolvedTest

  /**
   * @testName: elContextPutContextNPETest
   * @assertion_ids: EL:JAVADOC:33; EL:JAVADOC:198; EL:JAVADOC:328;
   *                 EL:JAVADOC:329; EL:JAVADOC:327
   * @test_Strategy: Validate that a NullPointerException is thrown if Class is
   *                 null or Object is null.
   * 
   * @since: 3.0
   */

  public void elContextPutContextNPETest() throws Fault {
    ELManager elm = new ELManager();
    ELContext elc = elm.getELContext();

    TestUtil.logMsg("Testing: ELContext.putContext(String.class, null)");
    ELTestUtil.checkForNPE(elc, "putContext",
        new Class<?>[] { Class.class, Object.class },
        new Object[] { String.class, null });

    TestUtil.logMsg("Testing: ELContext.putContext(null, testStrg)");
    ELTestUtil.checkForNPE(elc, "putContext",
        new Class<?>[] { Class.class, Object.class },
        new Object[] { String.class, null });

  } // end elContextPutContextNPETest

  /**
   * @testName: elContextGetContextNPETest
   * @assertion_ids: EL:JAVADOC:33; EL:JAVADOC:194; EL:JAVADOC:328;
   *                 EL:JAVADOC:329; EL:JAVADOC:321
   * @test_Strategy: Validate that a NullPointerException is thrown if key is
   *                 null.
   * 
   * @since: 3.0
   */

  public void elContextGetContextNPETest() throws Fault {
    ELManager elm = new ELManager();
    ELContext elc = elm.getELContext();

    TestUtil.logMsg("Testing: ELContext.getContext(null)");
    ELTestUtil.checkForNPE(elc, "getContext", new Class<?>[] { Class.class },
        new Object[] { null });

  } // end elContextGetContextNPETest

  /**
   * @testName: elContextAddGetListenersTest
   * @assertion_ids: EL:JAVADOC:33; EL:JAVADOC:191; EL:JAVADOC:197;
   *                 EL:JAVADOC:328; EL:JAVADOC:329
   * @test_Strategy: Validate that a NullPointerException is thrown if key is
   *                 null.
   * 
   * @since: 3.0
   */

  public void elContextAddGetListenersTest() throws Fault {
    ELManager elm = new ELManager();
    ELContext elc = elm.getELContext();

    EvaluationListener listenerOne = new TCKEvalListener();
    EvaluationListener listenerTwo = new TCKEvalListener();

    elc.addEvaluationListener(listenerOne);
    elc.addEvaluationListener(listenerTwo);

    List<EvaluationListener> listeners = elc.getEvaluationListeners();

    if (!(listeners.contains(listenerOne) && listeners.contains(listenerTwo))) {
      throw new Fault(
          ELTestUtil.FAIL + " Was unable to find test listeners in List "
              + "returned form " + "ElContext.getListeners()!");
    }

  } // end elContextAddGetListenersTest

  // ---------------------------------------------- private classes

  private static class TCKEvalListener extends EvaluationListener {
    // do nothing this is just for test purposes.
  }
}
