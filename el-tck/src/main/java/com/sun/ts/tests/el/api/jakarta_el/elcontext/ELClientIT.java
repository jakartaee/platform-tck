/*
 * Copyright (c) 2012, 2020 Oracle and/or its affiliates and others.
 * All rights reserved.
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

package com.sun.ts.tests.el.api.jakarta_el.elcontext;

import java.util.List;
import java.util.Locale;
import java.util.Properties;



import com.sun.ts.tests.el.common.util.ELTestUtil;

import jakarta.el.ELContext;
import jakarta.el.ELManager;
import jakarta.el.EvaluationListener;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.lang.System.Logger;

public class ELClientIT {

  private static final Logger logger = System.getLogger(ELClientIT.class.getName());

  @AfterEach
  public void cleanup() throws Exception {
    logger.log(Logger.Level.INFO, "Cleanup method called");
  }

  @BeforeEach
  void logStartTest(TestInfo testInfo) {
    logger.log(Logger.Level.INFO, "STARTING TEST : "+testInfo.getDisplayName());
  }

  @AfterEach
  void logFinishTest(TestInfo testInfo) {
    logger.log(Logger.Level.INFO, "FINISHED TEST : "+testInfo.getDisplayName());
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
  @Test
  public void elContextPutGetContextTest() throws Exception {
    ELManager elm = new ELManager();
    ELContext elc = elm.getELContext();
    String testStr = "TCKContext";

    elc.putContext(String.class, testStr);
    String result = elc.getContext(String.class).toString();

    if (!testStr.equals(result)) {
      throw new Exception(ELTestUtil.FAIL + " Unexpected Context Returned!"
          + ELTestUtil.NL + "Expected: " + testStr + ELTestUtil.NL
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
  @Test
  public void elContextGetSetLocaleTest() throws Exception {
    ELManager elm = new ELManager();
    ELContext elc = elm.getELContext();

    String disName = "english";
    Locale en = new Locale(disName);
    elc.setLocale(en);
    String result = elc.getLocale().getDisplayName();

    if (!result.equalsIgnoreCase(disName)) {
      throw new Exception(ELTestUtil.FAIL + " Unexpected Locale Returned!"
          + ELTestUtil.NL + "Expected: " + disName + ELTestUtil.NL
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
  @Test
  public void elContextIsSetPropertyResolvedTest() throws Exception {
    ELManager elm = new ELManager();
    ELContext elc = elm.getELContext();

    boolean isProp = elc.isPropertyResolved();

    if (isProp) {
      throw new Exception(
          ELTestUtil.FAIL + " Unexpected result from isPropertyResolved()!"
              + ELTestUtil.NL + "Should have been false!");
    }

    elc.setPropertyResolved(true);
    isProp = elc.isPropertyResolved();

    if (!isProp) {
      throw new Exception(
          ELTestUtil.FAIL + " Unexpected result from isPropertyResolved()!"
              + ELTestUtil.NL + "Should have been true!");
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
  @Test
  public void elContextPutContextNPETest() throws Exception {
    ELManager elm = new ELManager();
    ELContext elc = elm.getELContext();

    logger.log(Logger.Level.INFO, "Testing: ELContext.putContext(String.class, null)");
    ELTestUtil.checkForNPE(elc, "putContext",
        new Class<?>[] { Class.class, Object.class },
        new Object[] { String.class, null });

    logger.log(Logger.Level.INFO, "Testing: ELContext.putContext(null, testStrg)");
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
  @Test
  public void elContextGetContextNPETest() throws Exception {
    ELManager elm = new ELManager();
    ELContext elc = elm.getELContext();

    logger.log(Logger.Level.INFO, "Testing: ELContext.getContext(null)");
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
  @Test
  public void elContextAddGetListenersTest() throws Exception {
    ELManager elm = new ELManager();
    ELContext elc = elm.getELContext();

    EvaluationListener listenerOne = new TCKEvalListener();
    EvaluationListener listenerTwo = new TCKEvalListener();

    elc.addEvaluationListener(listenerOne);
    elc.addEvaluationListener(listenerTwo);

    List<EvaluationListener> listeners = elc.getEvaluationListeners();

    if (!(listeners.contains(listenerOne) && listeners.contains(listenerTwo))) {
      throw new Exception(
          ELTestUtil.FAIL + " Was unable to find test listeners in List "
              + "returned form " + "ElContext.getListeners()!");
    }

  } // end elContextAddGetListenersTest

  // ---------------------------------------------- private classes

  private static class TCKEvalListener extends EvaluationListener {
    // do nothing this is just for test purposes.
  }
}
