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

package com.sun.ts.tests.el.api.javax_el.staticfieldelresolver;

import java.beans.FeatureDescriptor;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.el.ArrayELResolver;
import javax.el.BeanELResolver;
import javax.el.ELClass;
import javax.el.ELContext;
import javax.el.ELManager;
import javax.el.ELResolver;
import javax.el.ListELResolver;
import javax.el.MapELResolver;
import javax.el.PropertyNotFoundException;
import javax.el.PropertyNotWritableException;
import javax.el.StaticFieldELResolver;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.el.api.resolver.BarELResolver;
import com.sun.ts.tests.common.el.api.resolver.ResolverTest;
import com.sun.ts.tests.el.common.util.ELTestUtil;

public class ELClient extends ServiceEETest {

  private Properties testProps;

  public static void main(String[] args) {
    ELClient theTests = new ELClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("Setup method called");
    this.testProps = p;
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("Cleanup method called");
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
   *                 getCommonPropertyType() getFeatureDescriptors()
   */
  public void staticFieldELResolverTest() throws Fault {
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
      buf.append(ELTestUtil.FAIL + TestUtil.NEW_LINE
          + "Expected PropertyNotWritableException to be "
          + "thrown when calling setValue()!" + TestUtil.NEW_LINE
          + "No exception was thown!" + TestUtil.NEW_LINE);
      pass = false;

    } catch (PropertyNotWritableException pnwe) {
      buf.append(ELTestUtil.PASS + TestUtil.NEW_LINE
          + "PropertyNotWritableException Thrown as expected for "
          + "setValue()!" + TestUtil.NEW_LINE);
    } catch (Exception e) {
      buf.append(ELTestUtil.FAIL + TestUtil.NEW_LINE
          + "Wrong Exception Thrownfor setValue()!" + TestUtil.NEW_LINE
          + "Expected: PropertyNotWritableException" + TestUtil.NEW_LINE
          + "Received: " + e.getClass().getSimpleName() + TestUtil.NEW_LINE);
    }

    // getValue()
    context.setPropertyResolved(false);
    Object valueRetrieved = resolver.getValue(context, base, property);
    if (!context.isPropertyResolved()) {
      buf.append("getValue() did not resolve" + TestUtil.NEW_LINE);
      pass = false;
    }

    if (valueRetrieved != value) {
      buf.append("Invalid value from getValue():" + TestUtil.NEW_LINE
          + "Value expected: " + value.toString() + TestUtil.NEW_LINE
          + "Value retrieved: " + valueRetrieved.toString()
          + TestUtil.NEW_LINE);
      pass = false;
    }

    // getType()
    context.setPropertyResolved(false);
    Class<?> type = resolver.getType(context, base, property);
    if (!context.isPropertyResolved()) {
      buf.append("getType() did not resolve" + TestUtil.NEW_LINE);
      pass = false;

    } else if (type.isInstance(String.class)) {
      buf.append("getType() returns " + type.getName() + TestUtil.NEW_LINE
          + "as expected." + TestUtil.NEW_LINE);
    }

    // isReadOnly
    context.setPropertyResolved(false);
    boolean nonWritable = resolver.isReadOnly(context, base, property);
    if (!context.isPropertyResolved()) {
      buf.append("isReadOnly() did not resolve" + TestUtil.NEW_LINE);
      pass = false;

    } else if (!nonWritable) {
      buf.append("isReadOnly() returned unexpected value: " + TestUtil.NEW_LINE
          + "Expected: false" + TestUtil.NEW_LINE + "Received: " + nonWritable
          + TestUtil.NEW_LINE);
      pass = false;

    } else {
      buf.append("isReadOnly() returns false as expected" + TestUtil.NEW_LINE);
    }

    // getCommonPropertyType()
    context.setPropertyResolved(false);
    Class<?> commonPropertyType = (resolver.getCommonPropertyType(context,
        base));
    buf.append("getCommonPropertyType() returns " + commonPropertyType.getName()
        + TestUtil.NEW_LINE);

    // getFeatureDescriptors()
    context.setPropertyResolved(false);
    Iterator<?> i = resolver.getFeatureDescriptors(context, base);

    if (i == null) {
      buf.append("getFeatureDescriptors() returns null" + TestUtil.NEW_LINE);
    }

    if (!pass) {
      throw new Fault(ELTestUtil.FAIL + TestUtil.NEW_LINE + buf.toString());
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
  public void staticFieldResolverInvokeMNFETest() throws Fault {
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
      throw new Fault(ex);
    }

    if (!pass) {
      throw new Fault(ELTestUtil.FAIL + buf.toString());
    }
    TestUtil.logMsg(buf.toString());

  }// End staticFieldResolverInvokeMNFETest

  /**
   * @testName: staticFieldELResolverInvokeTest
   * 
   * @assertion_ids: EL:JAVADOC:338; EL:JAVADOC:189; EL:JAVADOC:204
   * 
   * @test_Strategy: Verify that API calls work as expected for
   *                 BeanNameELResolver.invoke().
   */
  public void staticFieldELResolverInvokeTest() throws Fault {
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
      throw new Fault(ex);
    }

    if (!pass) {
      throw new Fault(ELTestUtil.FAIL + buf.toString());
    }

    TestUtil.logMsg(buf.toString());

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
  public void staticFieldELResolverNPETest() throws Fault {
    boolean pass = false;
    StringBuffer buf = new StringBuffer();

    ELManager elm = new ELManager();
    StaticFieldELResolver resolver = new StaticFieldELResolver();
    elm.addELResolver(resolver);

    try {
      pass = ResolverTest.testELResolverNPE(resolver,
          new ELClass(TCKELClass.class), "intention", "billy", buf);

    } catch (Exception ex) {
      throw new Fault(ex);
    }

    if (!pass) {
      throw new Fault(ELTestUtil.FAIL + buf.toString());
    }

    TestUtil.logTrace(buf.toString());

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
  public void staticFieldResolverInvokePNFETest() throws Fault {

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
          + TestUtil.NEW_LINE + "Expected: PropertyNotFoundException "
          + TestUtil.NEW_LINE);

    } catch (PropertyNotFoundException pnfe) {
      buf.append(ELTestUtil.PASS);

    } catch (Exception e) {
      buf.append(ELTestUtil.FAIL + "Wrong Exception thrown for getType()!"
          + TestUtil.NEW_LINE + " Expected: PropertyNotFoundException"
          + TestUtil.NEW_LINE + "Received: " + e.getClass().getSimpleName());
    }

    // getValue()
    try {
      resolver.getValue(context, base, property);
      buf.append(ELTestUtil.FAIL + " getValue() did not throw any exception."
          + TestUtil.NEW_LINE + "Expected: PropertyNotFoundException "
          + TestUtil.NEW_LINE);

    } catch (PropertyNotFoundException pnfe) {
      buf.append(ELTestUtil.PASS);

    } catch (Exception e) {
      buf.append(ELTestUtil.FAIL + "Wrong Exception thrown getValue()!"
          + TestUtil.NEW_LINE + " Expected: PropertyNotFoundException"
          + TestUtil.NEW_LINE + "Received: " + e.getClass().getSimpleName());
    }

  }// End testForPNFE

}
