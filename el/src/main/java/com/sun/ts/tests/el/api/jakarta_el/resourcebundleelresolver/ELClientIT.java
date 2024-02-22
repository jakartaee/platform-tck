/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates and others.
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

package com.sun.ts.tests.el.api.jakarta_el.resourcebundleelresolver;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.StringTokenizer;


import com.sun.ts.tests.el.common.api.resolver.ResolverTest;
import com.sun.ts.tests.el.common.elcontext.BareBonesELContext;
import com.sun.ts.tests.el.common.util.ELTestUtil;

import jakarta.el.ELContext;
import jakarta.el.ResourceBundleELResolver;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.lang.System.Logger;


public class ELClientIT {

  private static final Logger logger = System.getLogger(ELClientIT.class.getName());

  private TckResourceBundle tckrb;

  public ELClientIT(){
    tckrb = new TckResourceBundle();
  }

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
   * @testName: resourceBundleELResolverTest
   *
   * @assertion_ids: EL:JAVADOC:103; EL:JAVADOC:104; EL:JAVADOC:105;
   *                 EL:JAVADOC:106; EL:JAVADOC:107; EL:JAVADOC:108;
   *                 EL:JAVADOC:109
   *
   * @test_Strategy: Verify that API calls work as expected:
   *                 ResourceBundleELResolver() setValue() getValue() getType()
   *                 isReadOnly() getCommonPropertyType()
   *                 getFeatureDescriptors()
   */
  @Test
  public void resourceBundleELResolverTest() throws Exception {

    boolean pass = false;
    StringBuffer buf = new StringBuffer();

    try {
      ResourceBundleELResolver rsbResolver = new ResourceBundleELResolver();
      BareBonesELContext barebonesContext = new BareBonesELContext();
      ELContext context = barebonesContext.getELContext();

      pass = ResolverTest.testELResolver(context, rsbResolver, tckrb, "Dinner",
          "DINNER", buf, true);
    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass)
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    logger.log(Logger.Level.TRACE, buf.toString());
  }

  /**
   * @testName: resourceBundleELResolverNPETest
   *
   * @assertion_ids: EL:JAVADOC:106; EL:JAVADOC:105; EL:JAVADOC:107;
   *                 EL:JAVADOC:108; EL:JAVADOC:109; EL:JAVADOC:313;
   *                 EL:JAVADOC:314; EL:JAVADOC:316; EL:JAVADOC:317
   *
   * @test_Strategy: Verify that the following methods throw a
   *                 NullPointerException, if context is null:
   *
   *                 getValue() getType() setValue() isReadOnly()
   */
  @Test
  public void resourceBundleELResolverNPETest() throws Exception {

    boolean pass = false;
    StringBuffer buf = new StringBuffer();

    ResourceBundleELResolver resolver = new ResourceBundleELResolver();

    try {
      pass = ResolverTest.testELResolverNPE(resolver, tckrb, "brunch", "BRUNCH",
          buf);
    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass)
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    logger.log(Logger.Level.TRACE, buf.toString());
  }

  /**
   * @testName: resourceBundleELResolverPNWETest
   *
   * @assertion_ids: EL:JAVADOC:108; EL:JAVADOC:109; EL:JAVADOC:318
   *
   * @test_Strategy: Verify that the following methods throw a
   *                 PropertyNotWritableException, since
   *                 ResourceBundleELResolvers are non-writable.
   *
   *                 setValue()
   */
  @Test
  public void resourceBundleELResolverPNWETest() throws Exception {

    boolean pass = false;
    StringBuffer buf = new StringBuffer();

    BareBonesELContext barebonesContext = new BareBonesELContext();
    ELContext context = barebonesContext.getELContext();
    ResourceBundleELResolver rsbResolver = new ResourceBundleELResolver();

    try {
      pass = ResolverTest.testELResolverPNWE(context, rsbResolver, tckrb,
          "snack", "SNACK", buf);
    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass)
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    logger.log(Logger.Level.TRACE, buf.toString());
  }

  private static class TckResourceBundle extends ResourceBundle
      implements Serializable {

    private static final String KEYS = "Breakfast Lunch Dinner";

    public Object handleGetObject(String key) {
      if ("Breakfast".equals(key))
        return "BREAKFAST";
      if ("Lunch".equals(key))
        return "LUNCH";
      if ("Dinner".equals(key))
        return "DINNER";

      return null;
    }

    public Enumeration getKeys() {
      StringTokenizer keyTokenizer = new StringTokenizer(KEYS);

      return keyTokenizer;

    }
  }
}
