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

package com.sun.ts.tests.el.api.jakarta_el.elresolver;

import java.util.Properties;


import com.sun.ts.tests.el.common.api.resolver.ResolverTest;
import com.sun.ts.tests.el.common.elcontext.BarELContext;
import com.sun.ts.tests.el.common.util.ELTestUtil;
import com.sun.ts.tests.el.common.util.SimpleBean;

import jakarta.el.ArrayELResolver;
import jakarta.el.BeanELResolver;
import jakarta.el.CompositeELResolver;
import jakarta.el.ELContext;
import jakarta.el.ELManager;
import jakarta.el.ELResolver;

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
   * @testName: elResolverTest
   * 
   * @assertion_ids: EL:JAVADOC:50; EL:JAVADOC:51; EL:JAVADOC:52; EL:JAVADOC:53;
   *                 EL:JAVADOC:54; EL:JAVADOC:55; EL:JAVADOC:229
   * 
   * @test_Strategy: Create an ELContext and get its ELResolver. Verify that API
   *                 calls work as expected: getValue() getType() setValue()
   *                 isReadOnly() getCommonPropertyType()
   *                 getFeatureDescriptors()
   */
  @Test
  public void elResolverTest() throws Exception {

    boolean pass = false;
    StringBuffer buf = new StringBuffer();

    try {
      BarELContext barContext = new BarELContext();
      ELContext context = barContext.getELContext();

      if (barContext != null) {
        ELResolver resolver = barContext.getELResolver();
        pass = ResolverTest.testELResolver(context, resolver, null, "Bar",
            "Foo", buf, true);
      }
    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass)
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    logger.log(Logger.Level.TRACE, buf.toString());

  } // end elResolverTest

  /**
   * @testName: elResolverNPETest
   * 
   * @assertion_ids: EL:JAVADOC:233; EL:JAVADOC:239; EL:JAVADOC:242
   * 
   * @test_Strategy: Verify that a NullPointerException is thrown as expected
   *                 for the following methods: getValue() getType() setValue()
   *                 isReadOnly()
   * 
   * @since: 3.0
   */
  @Test
  public void elResolverNPETest() throws Exception {
    boolean pass = false;
    StringBuffer buf = new StringBuffer();

    ELContext context = new ELManager().getELContext();

    // Create dummy ELResolvers to populate the CompositeELResolver.
    ELResolver aResolver = new ArrayELResolver();
    ELResolver bResolver = new BeanELResolver();

    // Create the CompositeELResolver & add the dummy ELResolvers to it.
    CompositeELResolver compResolver = (CompositeELResolver) context
        .getELResolver();
    compResolver.add(aResolver);
    compResolver.add(bResolver);

    try {
      pass = ResolverTest.testELResolverNPE(compResolver, names,
          Integer.valueOf(2), "steve", buf);

    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass)
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    logger.log(Logger.Level.TRACE, buf.toString());

  }// end elResolverNPETest

  /**
   * @testName: elResolverPNFETest
   * 
   * @assertion_ids: EL:JAVADOC:231; EL:JAVADOC:234; EL:JAVADOC:240
   * 
   * @test_Strategy: Verify that API calls throw PropertyNotFoundException as
   *                 expected: getType() setValue() isReadOnly()
   */
  @Test
  public void elResolverPNFETest() throws Exception {
    boolean pass = true;
    StringBuffer buf = new StringBuffer();

    ELContext context = new ELManager().getELContext();

    // Create an ArrayELResolver to populate the CompositeELResolver.
    ELResolver aResolver = new ArrayELResolver();

    // Create the CompositeELResolver & add the ELResolver to it.
    CompositeELResolver compResolver = (CompositeELResolver) context
        .getELResolver();
    compResolver.add(aResolver);

    try {
      pass = ResolverTest.testELResolverPNFE(context, compResolver, names,
          Integer.valueOf(10), buf);

    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass)
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    logger.log(Logger.Level.TRACE, buf.toString());

  }// end elResolverPNFETest

  /**
   * @testName: elResolverPNWETest
   * 
   * @assertion_ids: EL:JAVADOC:50; EL:JAVADOC:244
   * 
   * @test_Strategy: Verify that the setValue() method throws a
   *                 PropertyNotWritableException, if the resolver was
   *                 constructed in read-only mode.
   * 
   *                 setValue()
   */
  @Test
  public void elResolverPNWETest() throws Exception {
    boolean pass = false;
    StringBuffer buf = new StringBuffer();
    SimpleBean sb = new SimpleBean();

    ELContext context = new ELManager().getELContext();

    ELResolver resolver = new BeanELResolver(true);
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

  } // end elResolverPNWETest

}
