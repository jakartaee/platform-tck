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

package com.sun.ts.tests.el.api.jakarta_el.compositeelresolver;

import java.util.Properties;



import com.sun.ts.tests.el.common.api.resolver.ResolverTest;
import com.sun.ts.tests.el.common.elcontext.BareBonesELContext;
import com.sun.ts.tests.el.common.util.ELTestUtil;
import com.sun.ts.tests.el.common.util.SimpleBean;

import jakarta.el.ArrayELResolver;
import jakarta.el.BeanELResolver;
import jakarta.el.CompositeELResolver;
import jakarta.el.ELContext;
import jakarta.el.ELManager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.lang.System.Logger;

public class ELClientIT {

  private static final Logger logger = System.getLogger(ELClientIT.class.getName());

  private static final String[] names = { "doug", "nick", "roger", "ryan",
      "ed" };

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
   * @testName: compositeELResolverTest
   * 
   * @assertion_ids: EL:JAVADOC:24; EL:JAVADOC:25; EL:JAVADOC:26; EL:JAVADOC:27;
   *                 EL:JAVADOC:28; EL:JAVADOC:29; EL:JAVADOC:30; EL:JAVADOC:31;
   *                 EL:JAVADOC:34
   * 
   * @test_Strategy: Verify that API calls work as expected:
   *                 CompositeELResolver() add() getValue() getType() setValue()
   *                 isReadOnly() getCommonPropertyType()
   *                 getFeatureDescriptors() ELContext.getELResolver()
   */
  @Test
  public void compositeELResolverTest() throws Exception {

    boolean pass = false;
    StringBuffer buf = new StringBuffer();

    try {
      BareBonesELContext compContext = new BareBonesELContext();
      ELContext context = compContext.getELContext();

      // Create the CompositeELResolver.
      CompositeELResolver compResolver = (CompositeELResolver) context
          .getELResolver();

      pass = ResolverTest.testCompositeELResolver(context, compResolver, buf);

    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass)
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    logger.log(Logger.Level.TRACE, buf.toString());
  }

  /**
   * @testName: compositeELResolverInvokeTest
   * 
   * @assertion_ids: EL:JAVADOC:24; EL:JAVADOC:25; EL:JAVADOC:181
   * 
   * @test_Strategy: Verify that API calls work as expected for
   *                 BeanELResolver.invoke().
   */
  @Test
  public void compositeELResolverInvokeTest() throws Exception {
    SimpleBean sb = new SimpleBean();
    boolean pass = false;
    StringBuffer buf = new StringBuffer();

    try {
      BeanELResolver beanResolver = new BeanELResolver();
      ELManager elm = new ELManager();
      ELContext context = elm.getELContext();

      // Create the CompositeELResolver.
      CompositeELResolver compResolver = (CompositeELResolver) context
          .getELResolver();

      compResolver.add(beanResolver);

      Class<?>[] types = { String.class, String.class };
      String[] values = { "Doug", "Donahue" };

      pass = ResolverTest.testELResolverInvoke(context, compResolver, sb,
          "isName", types, values, false, buf);

    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }
    logger.log(Logger.Level.TRACE, buf.toString());

  }// End beanELResolverInvokeTest

  /**
   * @testName: compositeELResolverAddNPETest
   * 
   * @assertion_ids: EL:JAVADOC:24; EL:JAVADOC:25; EL:JAVADOC:174
   * 
   * @test_Strategy: Verify that the CompositeELResolver.add method throws a
   *                 NullPointerException if the provided resolver is null.
   */
  @Test
  public void compositeELResolverAddNPETest() throws Exception {

    boolean pass = true;
    StringBuffer buf = new StringBuffer();

    try {
      BareBonesELContext compContext = new BareBonesELContext();
      ELContext context = compContext.getELContext();
      CompositeELResolver compResolver = (CompositeELResolver) context
          .getELResolver();

      compResolver.add(null);

      buf.append(ELTestUtil.NL + "Tested Failed, CompositeResolver.add(), "
          + "should have thrown NullPointerException!");
      pass = false;

    } catch (NullPointerException npe) {
      logger.log(Logger.Level.INFO, "Expected Exception thrown, when providing a"
          + " null resolver to Composite.add()");

    } catch (Exception e) {
      buf.append("test failed with: " + ELTestUtil.NL + "EXPECTED: "
          + "NullPointerException to be thrown " + ELTestUtil.NL
          + "RECEIVED: " + e.toString() + "" + ELTestUtil.NL);
      pass = false;
    }

    if (!pass)
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    logger.log(Logger.Level.TRACE, buf.toString());
  }

  /**
   * @testName: compositeELResolverNPETest
   * 
   * @assertion_ids: EL:JAVADOC:25; EL:JAVADOC:26; EL:JAVADOC:27; EL:JAVADOC:28;
   *                 EL:JAVADOC:29; EL:JAVADOC:30; EL:JAVADOC:31;
   *                 EL:JAVADOC:175; EL:JAVADOC:178; EL:JAVADOC:175;
   *                 EL:JAVADOC:182; EL:JAVADOC:185
   * 
   * @test_Strategy: Verify that API calls work as expected: getValue()
   *                 getType() setValue() isReadOnly() getCommonPropertyType()
   *                 getFeatureDescriptors()
   */
  @Test
  public void compositeELResolverNPETest() throws Exception {

    boolean pass = false;
    StringBuffer buf = new StringBuffer();

    try {
      BareBonesELContext compContext = new BareBonesELContext();
      ELContext context = compContext.getELContext();

      // Create dummy ELResolvers to populate the CompositeELResolver.
      ArrayELResolver aResolver = new ArrayELResolver();
      BeanELResolver bResolver = new BeanELResolver();

      // Create the CompositeELResolver & add the dummy ELResolvers to it.
      CompositeELResolver compResolver = (CompositeELResolver) context
          .getELResolver();
      compResolver.add(aResolver);
      compResolver.add(bResolver);

      pass = ResolverTest.testELResolverNPE(compResolver, names,
          Integer.valueOf(2), "steve", buf);

    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass)
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    logger.log(Logger.Level.TRACE, buf.toString());
  }

  /**
   * @testName: compositeELResolverPNFETest
   * 
   * @assertion_ids: EL:JAVADOC:25; EL:JAVADOC:28; EL:JAVADOC:30; EL:JAVADOC:31;
   *                 EL:JAVADOC:176; EL:JAVADOC:179; EL:JAVADOC:183;
   *                 EL:JAVADOC:186
   * 
   * @test_Strategy: Verify that API calls throw PropertyNotFoundException as
   *                 expected: getType() setValue() isReadOnly()
   */
  @Test
  public void compositeELResolverPNFETest() throws Exception {

    boolean pass = true;
    StringBuffer buf = new StringBuffer();

    try {
      BareBonesELContext compContext = new BareBonesELContext();
      ELContext context = compContext.getELContext();

      // Create an ArrayELResolver to populate the CompositeELResolver.
      ArrayELResolver aResolver = new ArrayELResolver();

      // Create the CompositeELResolver & add the ELResolver to it.
      CompositeELResolver compResolver = (CompositeELResolver) context
          .getELResolver();
      compResolver.add(aResolver);

      pass = ResolverTest.testELResolverPNFE(context, compResolver, names,
          Integer.valueOf(10), buf);

    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass)
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    logger.log(Logger.Level.TRACE, buf.toString());
  }

  /**
   * @testName: compositeELResolverPNWETest
   * 
   * @assertion_ids: EL:JAVADOC:25; EL:JAVADOC:187
   * 
   * @test_Strategy: Verify that the setValue() method throws a
   *                 PropertyNotWritableException, if the resolver was
   *                 constructed in read-only mode.
   * 
   *                 setValue()
   */
  @Test
  public void compositeELResolverPNWETest() throws Exception {
    boolean pass = false;
    StringBuffer buf = new StringBuffer();
    SimpleBean sb = new SimpleBean();

    BareBonesELContext compContext = new BareBonesELContext();
    ELContext context = compContext.getELContext();

    // Create an ArrayELResolver to populate the CompositeELResolver.
    BeanELResolver resolver = new BeanELResolver(true);

    // Create the CompositeELResolver & add the ELResolver to it.
    CompositeELResolver compResolver = (CompositeELResolver) context
        .getELResolver();
    compResolver.add(resolver);

    try {
      pass = ResolverTest.testELResolverPNWE(context, resolver, sb, "intention",
          "billy", buf);
    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }
    logger.log(Logger.Level.TRACE, buf.toString());
  }

}
