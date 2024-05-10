/*
 * Copyright (c) 2012, 2021 Oracle and/or its affiliates and others.
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

package com.sun.ts.tests.el.api.jakarta_el.staticfieldelresolver;

import java.util.Iterator;
import java.util.Properties;



import com.sun.ts.tests.el.common.api.resolver.ResolverTest;
import com.sun.ts.tests.el.common.util.ELTestUtil;

import jakarta.el.ELClass;
import jakarta.el.ELContext;
import jakarta.el.ELManager;
import jakarta.el.PropertyNotFoundException;
import jakarta.el.PropertyNotWritableException;
import jakarta.el.StaticFieldELResolver;

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
   * @testName: staticFieldELResolverTest
   * 
   * @assertion_ids: EL:JAVADOC:330; EL:JAVADOC:331; EL:JAVADOC:332;
   *                 EL:JAVADOC:335; EL:JAVADOC:338; EL:JAVADOC:341;
   *                 EL:JAVADOC:343; EL:JAVADOC:346; EL:JAVADOC:189;
   *                 EL:JAVADOC:204
   * 
   * 
   * @test_Strategy: Verify the following method calls work as expected:
   *                 getValue() getType() setValue() isReadOnly()
   *                 getCommonPropertyType() 
   */
  @Test
  public void staticFieldELResolverTest() throws Exception {
    StringBuffer buf = new StringBuffer();
    boolean pass = true;

    ELManager elm = new ELManager();
    StaticFieldELResolver resolver = new StaticFieldELResolver();
    elm.addELResolver(resolver);
    ELContext context = elm.getELContext();

    Object base = new ELClass(TCKELClass.class);
    Object property = "firstName";
    Object value = "Ender";

    // setValue()
    context.setPropertyResolved(false);
    try {
      resolver.setValue(context, base, property, value);
      buf.append(ELTestUtil.FAIL + ELTestUtil.NL
          + "Expected PropertyNotWritableException to be "
          + "thrown when calling setValue()!" + ELTestUtil.NL
          + "No exception was thown!" + ELTestUtil.NL);
      pass = false;

    } catch (PropertyNotWritableException pnwe) {
      buf.append(ELTestUtil.PASS + ELTestUtil.NL
          + "PropertyNotWritableException Thrown as expected for "
          + "setValue()!" + ELTestUtil.NL);
    } catch (Exception e) {
      buf.append(ELTestUtil.FAIL + ELTestUtil.NL
          + "Wrong Exception Thrownfor setValue()!" + ELTestUtil.NL
          + "Expected: PropertyNotWritableException" + ELTestUtil.NL
          + "Received: " + e.getClass().getSimpleName() + ELTestUtil.NL);
    }

    // getValue()
    context.setPropertyResolved(false);
    Object valueRetrieved = resolver.getValue(context, base, property);
    if (!context.isPropertyResolved()) {
      buf.append("getValue() did not resolve" + ELTestUtil.NL);
      pass = false;
    }

    if (valueRetrieved != value) {
      buf.append("Invalid value from getValue():" + ELTestUtil.NL
          + "Value expected: " + value.toString() + ELTestUtil.NL
          + "Value retrieved: " + valueRetrieved.toString()
          + ELTestUtil.NL);
      pass = false;
    }

    // getType()
    context.setPropertyResolved(false);
    Class<?> type = resolver.getType(context, base, property);
    if (!context.isPropertyResolved()) {
      buf.append("getType() did not resolve" + ELTestUtil.NL);
      pass = false;
    } else if (type != null) {
      buf.append("getType() returns " + type.getName() + " rather than null" + ELTestUtil.NL);
      pass = false;
    } else {
      buf.append("getType() returns null" + ELTestUtil.NL + "as expected." + ELTestUtil.NL);
    }

    // isReadOnly
    context.setPropertyResolved(false);
    boolean nonWritable = resolver.isReadOnly(context, base, property);
    if (!context.isPropertyResolved()) {
      buf.append("isReadOnly() did not resolve" + ELTestUtil.NL);
      pass = false;

    } else if (!nonWritable) {
      buf.append("isReadOnly() returned unexpected value: " + ELTestUtil.NL
          + "Expected: false" + ELTestUtil.NL + "Received: " + nonWritable
          + ELTestUtil.NL);
      pass = false;

    } else {
      buf.append("isReadOnly() returns false as expected" + ELTestUtil.NL);
    }

    // getCommonPropertyType()
    context.setPropertyResolved(false);
    Class<?> commonPropertyType = (resolver.getCommonPropertyType(context,
        base));
    buf.append("getCommonPropertyType() returns " + commonPropertyType.getName()
        + ELTestUtil.NL);

    // getFeatureDescriptors() commenting below as the method is deprecated in EL 6.0
    // context.setPropertyResolved(false);
    // Iterator<?> i = resolver.getFeatureDescriptors(context, base);

    // if (i == null) {
    //   buf.append("getFeatureDescriptors() returns null" + ELTestUtil.NL);
    // }

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + ELTestUtil.NL + buf.toString());
    }

  } // End staticFieldELResolverTest

  /**
   * @testName: staticFieldResolverInvokeMNFETest
   * 
   * @assertion_ids: EL:JAVADOC:339; EL:JAVADOC:189; EL:JAVADOC:204
   * 
   * @test_Strategy: Verify that the invoke() method throws
   *                 MethodNotFoundException if no suitable method can be found.
   */
  @Test
  public void staticFieldResolverInvokeMNFETest() throws Exception {
    StringBuffer buf = new StringBuffer();
    boolean pass = false;

    ELManager elm = new ELManager();
    StaticFieldELResolver resolver = new StaticFieldELResolver();
    elm.addELResolver(resolver);
    ELContext context = elm.getELContext();

    Class<?>[] types = { String.class, String.class };
    String[] values = { "Doug", "Donahue" };

    try {
      pass = ResolverTest.testELResolverInvoke(context, context.getELResolver(),
          new ELClass(TCKELClass.class), "bogue_method", types, values, true,
          buf);

    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }
    logger.log(Logger.Level.INFO, buf.toString());

  }// End staticFieldResolverInvokeMNFETest

  /**
   * @testName: staticFieldELResolverInvokeTest
   * 
   * @assertion_ids: EL:JAVADOC:338; EL:JAVADOC:189; EL:JAVADOC:204
   * 
   * @test_Strategy: Verify that API calls work as expected for
   *                 BeanNameELResolver.invoke().
   */
  @Test
  public void staticFieldELResolverInvokeTest() throws Exception {
    StringBuffer buf = new StringBuffer();
    boolean pass = false;

    ELManager elm = new ELManager();
    StaticFieldELResolver resolver = new StaticFieldELResolver();
    elm.addELResolver(resolver);
    ELContext context = elm.getELContext();

    Class<?>[] types = { String.class };
    String[] values = { "Ender" };

    try {
      pass = ResolverTest.testELResolverInvoke(context, context.getELResolver(),
          new ELClass(TCKELClass.class), "isName", types, values, false, buf);

    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }

    logger.log(Logger.Level.INFO, buf.toString());

  }// End staticFieldELResolverInvokeTest

  /**
   * @testName: staticFieldELResolverNPETest
   * 
   * @assertion_ids: EL:JAVADOC:333; EL:JAVADOC:336; EL:JAVADOC:342;
   *                 EL:JAVADOC:344; EL:JAVADOC:189
   * 
   * @test_Strategy: Verify that the following methods throw a
   *                 NullPointerException, if context is null:
   * 
   *                 getType() getValue() isReadOnly() setValue()
   */
  @Test
  public void staticFieldELResolverNPETest() throws Exception {
    boolean pass = false;
    StringBuffer buf = new StringBuffer();

    ELManager elm = new ELManager();
    StaticFieldELResolver resolver = new StaticFieldELResolver();
    elm.addELResolver(resolver);

    try {
      pass = ResolverTest.testELResolverNPE(resolver,
          new ELClass(TCKELClass.class), "intention", "billy", buf);

    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }

    logger.log(Logger.Level.TRACE, buf.toString());

  } // End staticFieldELResolverNPETest

  /**
   * @testName: staticFieldResolverInvokePNFETest
   * 
   * @assertion_ids: EL:JAVADOC:334; EL:JAVADOC:337
   * 
   * @test_Strategy: Verify that the invoke() method throws
   *                 PropertyNotFoundException the specified class does not
   *                 exist, or if the field is not a public static filed of the
   *                 class, or if the field is inaccessible.
   */
  @Test
  public void staticFieldResolverInvokePNFETest() throws Exception {

    Object base = new ELClass(TCKELClass.class);

    // Test for non static field
    testForPNFE("notStatic", base);

    // Test for Private static field
    testForPNFE("privStatic", base);

    // Test for non existent Class
    testForPNFE("privStatic", "bogus");

  }// End staticFieldResolverInvokePNFETest

  // ------------------------- private methods

  private void testForPNFE(String property, Object base) {
    StringBuffer buf = new StringBuffer();

    ELManager elm = new ELManager();
    StaticFieldELResolver resolver = new StaticFieldELResolver();
    elm.addELResolver(resolver);
    ELContext context = elm.getELContext();

    // getType()
    try {
      resolver.getType(context, base, property);
      buf.append(ELTestUtil.FAIL + " getType() did not throw any exception."
          + ELTestUtil.NL + "Expected: PropertyNotFoundException "
          + ELTestUtil.NL);

    } catch (PropertyNotFoundException pnfe) {
      buf.append(ELTestUtil.PASS);

    } catch (Exception e) {
      buf.append(ELTestUtil.FAIL + "Wrong Exception thrown for getType()!"
          + ELTestUtil.NL + " Expected: PropertyNotFoundException"
          + ELTestUtil.NL + "Received: " + e.getClass().getSimpleName());
    }

    // getValue()
    try {
      resolver.getValue(context, base, property);
      buf.append(ELTestUtil.FAIL + " getValue() did not throw any exception."
          + ELTestUtil.NL + "Expected: PropertyNotFoundException "
          + ELTestUtil.NL);

    } catch (PropertyNotFoundException pnfe) {
      buf.append(ELTestUtil.PASS);

    } catch (Exception e) {
      buf.append(ELTestUtil.FAIL + "Wrong Exception thrown getValue()!"
          + ELTestUtil.NL + " Expected: PropertyNotFoundException"
          + ELTestUtil.NL + "Received: " + e.getClass().getSimpleName());
    }

  }// End testForPNFE

}
