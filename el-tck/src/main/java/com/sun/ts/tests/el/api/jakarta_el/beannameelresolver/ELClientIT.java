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

package com.sun.ts.tests.el.api.jakarta_el.beannameelresolver;

import java.util.Properties;


import com.sun.ts.tests.el.common.api.resolver.ResolverTest;
import com.sun.ts.tests.el.common.util.ELTestUtil;
import com.sun.ts.tests.el.common.util.SimpleBean;

import jakarta.el.BeanNameELResolver;
import jakarta.el.BeanNameResolver;
import jakarta.el.ELContext;
import jakarta.el.ELManager;

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
   * @testName: beanNameELResolverTest
   * 
   * @assertion_ids: EL:JAVADOC:152; EL:JAVADOC:153; EL:JAVADOC:154;
   *                 EL:JAVADOC:155; EL:JAVADOC:158; EL:JAVADOC:161;
   *                 EL:JAVADOC:164
   * 
   * @test_Strategy: Verify the following method calls work as expected:
   *                 getValue() getType() setValue() isReadOnly()
   *                 getCommonPropertyType() getFeatureDescriptors()
   */
  @Test
  public void beanNameELResolverTest() throws Exception {
    StringBuffer buf = new StringBuffer();
    boolean pass = false;

    ELManager elm = new ELManager();
    BeanNameResolver bnr = new TCKBeanNameResolver();
    BeanNameELResolver belr = new BeanNameELResolver(bnr);

    elm.addBeanNameResolver(bnr);
    elm.addELResolver(belr);
    elm.defineBean("simpleBean", new SimpleBean());

    ELContext context = elm.getELContext();

    try {
      pass = ResolverTest.testELResolver(context, context.getELResolver(),
          bnr.getBean("simpleBean"), "intention", "GOLDEN", buf, false);

    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }

    logger.log(Logger.Level.INFO, buf.toString());
  }

  /**
   * @testName: beanNameELResolverInvokeMNFETest
   * 
   * @assertion_ids: EL:JAVADOC:9; EL:JAVADOC:11; EL:JAVADOC:12; EL:JAVADOC:13;
   *                 EL:JAVADOC:14; EL:JAVADOC:15; EL:JAVADOC:16;
   *                 EL:JAVADOC:143; EL:JAVADOC:237
   * 
   * @test_Strategy: Verify that the invoke() method throws
   *                 MethodNotFoundException if no suitable method can be found.
   */
  @Test
  public void beanNameELResolverInvokeMNFETest() throws Exception {
    StringBuffer buf = new StringBuffer();
    boolean pass = false;

    ELManager elm = new ELManager();
    BeanNameResolver bnr = new TCKBeanNameResolver();
    BeanNameELResolver belr = new BeanNameELResolver(bnr);

    elm.addBeanNameResolver(bnr);
    elm.addELResolver(belr);
    elm.defineBean("simpleBean", new SimpleBean());

    ELContext context = elm.getELContext();

    Class<?>[] types = { String.class, String.class };
    String[] values = { "Doug", "Donahue" };

    try {
      pass = ResolverTest.testELResolverInvoke(context, context.getELResolver(),
          new SimpleBean(), "bogue_method", types, values, true, buf);

    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }
    logger.log(Logger.Level.INFO, buf.toString());

  }// End beanELResolverInvokeMNFETest

  /**
   * @testName: beanNameELResolverInvokeTest
   * 
   * @assertion_ids: EL:JAVADOC:9; EL:JAVADOC:11; EL:JAVADOC:12; EL:JAVADOC:13;
   *                 EL:JAVADOC:14; EL:JAVADOC:15; EL:JAVADOC:16;
   *                 EL:JAVADOC:142; EL:JAVADOC:199; EL:JAVADOC:200;
   *                 EL:JAVADOC:202; EL:JAVADOC:203; EL:JAVADOC:236
   * 
   * @test_Strategy: Verify that API calls work as expected for
   *                 BeanNameELResolver.invoke().
   */
  @Test
  public void beanNameELResolverInvokeTest() throws Exception {
    StringBuffer buf = new StringBuffer();
    boolean pass = false;

    ELManager elm = new ELManager();
    BeanNameResolver bnr = new TCKBeanNameResolver();
    BeanNameELResolver belr = new BeanNameELResolver(bnr);

    elm.addBeanNameResolver(bnr);
    elm.addELResolver(belr);
    elm.defineBean("simpleBean", new SimpleBean());

    ELContext context = elm.getELContext();

    Class<?>[] types = { String.class, String.class };
    String[] values = { "Doug", "Donahue" };

    try {
      pass = ResolverTest.testELResolverInvoke(context, context.getELResolver(),
          new SimpleBean(), "isName", types, values, false, buf);

    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }
    logger.log(Logger.Level.INFO, buf.toString());

  }// End beanELResolverInvokeTest

  /**
   * @testName: beanNameELResolverGetTypeNPETest
   * 
   * @assertion_ids: EL:JAVADOC:156
   * 
   * @test_Strategy: Validate a NullPointerException is thrown if context is
   *                 null.
   */
  @Test
  public void beanNameELResolverGetTypeNPETest() throws Exception {
    BeanNameResolver bnr = new TCKBeanNameResolver();
    BeanNameELResolver belr = new BeanNameELResolver(bnr);

    ELTestUtil.checkForNPE(belr, "getType",
        new Class<?>[] { ELContext.class, Object.class, Object.class },
        new Object[] { null, "Ender", "Wiggins" });

  }// End beanNameELResolverGetTypeNPETest

  /**
   * @testName: beanNameELResolverGetValueNPETest
   * 
   * @assertion_ids: EL:JAVADOC:159
   * 
   * @test_Strategy: Validate a NullPointerException is thrown if context is
   *                 null.
   */
  @Test
  public void beanNameELResolverGetValueNPETest() throws Exception {
    BeanNameResolver bnr = new TCKBeanNameResolver();
    BeanNameELResolver belr = new BeanNameELResolver(bnr);

    ELTestUtil.checkForNPE(belr, "getValue",
        new Class<?>[] { ELContext.class, Object.class, Object.class },
        new Object[] { null, "Ender", "Wiggins" });

  }// End beanNameELResolverGetValueNPETest

  /**
   * @testName: beanNameELResolverIsReadOnlyNPETest
   * 
   * @assertion_ids: EL:JAVADOC:162
   * 
   * @test_Strategy: Validate a NullPointerException is thrown if context is
   *                 null.
   */
  @Test
  public void beanNameELResolverIsReadOnlyNPETest() throws Exception {
    BeanNameResolver bnr = new TCKBeanNameResolver();
    BeanNameELResolver belr = new BeanNameELResolver(bnr);

    ELTestUtil.checkForNPE(belr, "isReadOnly",
        new Class<?>[] { ELContext.class, Object.class, Object.class },
        new Object[] { null, "Ender", "Wiggins" });

  }// End beanNameELResolverIsReadOnlyNPETest

  /**
   * @testName: beanNameELResolverSetValueNPETest
   * 
   * @assertion_ids: EL:JAVADOC:165
   * 
   * @test_Strategy: Validate a NullPointerException is thrown if context is
   *                 null.
   */
  @Test
  public void beanNameELResolverSetValueNPETest() throws Exception {
    BeanNameResolver bnr = new TCKBeanNameResolver();
    BeanNameELResolver belr = new BeanNameELResolver(bnr);

    ELTestUtil.checkForNPE(belr, "setValue",
        new Class<?>[] { ELContext.class, Object.class, Object.class,
            Object.class },
        new Object[] { null, "Ender", "Valintine", "Wiggins" });

  }// End beanNameELResolverSetValueNPETest

  // -------------------------------------------- private classes

  private static class TCKBeanNameResolver extends BeanNameResolver {

  }
}
