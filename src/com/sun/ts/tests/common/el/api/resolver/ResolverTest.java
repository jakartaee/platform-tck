/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.common.el.api.resolver;

import java.beans.FeatureDescriptor;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import javax.el.ArrayELResolver;
import javax.el.BeanELResolver;
import javax.el.CompositeELResolver;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.ListELResolver;
import javax.el.MapELResolver;
import javax.el.MethodNotFoundException;
import javax.el.PropertyNotFoundException;
import javax.el.PropertyNotWritableException;

import com.sun.ts.lib.util.TestUtil;

public class ResolverTest {
  private static final String NL = System.getProperty("line.separator", "\n");

  /**
   * Private as this class will only have static methods and members.
   */
  private ResolverTest() {
  }

  public static boolean testCompositeELResolver(ELContext elContext,
      CompositeELResolver compResolver, StringBuffer buf) {

    boolean pass = true;

    // add()
    BarELResolver barResolver = new BarELResolver();
    try {
      compResolver.add(barResolver);
      buf.append("add() tested successfully" + NL);

    } catch (Exception e) {
      buf.append("add() method Test Failed");
      pass = false;
    }

    // setValue()
    elContext.setPropertyResolved(false);
    compResolver.setValue(elContext, null, "Nothing", "rien");
    if (!elContext.isPropertyResolved()) {
      buf.append(
          "setValue() tested successfully on non-existent " + "property" + NL);
    } else {
      buf.append("setValue() set property resolved on non-existent "
          + "property" + NL);
      pass = false;
    }

    elContext.setPropertyResolved(false);
    try {
      compResolver.setValue(elContext, null, "Bar", "rien");
      pass = false;
    } catch (PropertyNotWritableException pnwe) {
      buf.append(
          "setValue() tested successfully on non-writable " + "property" + NL);
      buf.append("PropertyNotWritableException caught as expected" + NL);
    }

    // getValue()
    elContext.setPropertyResolved(false);
    String result = (String) compResolver.getValue(elContext, null, "Bar");
    if (!elContext.isPropertyResolved()) {
      buf.append("getValue() did not resolve" + NL);
      pass = false;
    } else if (!result.equals("Foo")) {
      buf.append("Invalid value from BarELResolver: " + result + NL);
      pass = false;
    } else {
      buf.append("getValue() tested successfully" + NL);
    }

    // getType()
    elContext.setPropertyResolved(false);
    Class type = compResolver.getType(elContext, null, "Bar");
    if (!elContext.isPropertyResolved()) {
      buf.append("getType() did not resolve" + NL);
      pass = false;
    } else if (type != null) {
      buf.append("getType() returns " + type.getName() + NL);
      buf.append("not expected result for non-writable property" + NL);
      pass = false;
    } else {
      buf.append("getType() returns null as expected for non-writable "
          + "property" + NL);
    }

    // isReadOnly
    elContext.setPropertyResolved(false);
    boolean nonWritable = compResolver.isReadOnly(elContext, null, "Bar");
    if (!elContext.isPropertyResolved()) {
      buf.append("isReadOnly() did not resolve" + NL);
      pass = false;
    } else if (!nonWritable) {
      buf.append("isReadOnly() did not return true" + NL);
      pass = false;
    } else {
      buf.append("isReadOnly() returns true as expected" + NL);
    }

    // getCommonPropertyType()
    elContext.setPropertyResolved(false);
    Class commonPropertyType = (compResolver.getCommonPropertyType(elContext,
        null));
    buf.append("getCommonPropertyType() returns ");
    buf.append(commonPropertyType.getName() + NL);

    // getFeatureDescriptors()
    elContext.setPropertyResolved(false);
    Iterator i = compResolver.getFeatureDescriptors(elContext, null);
    boolean fdPass = ResolverTest.testFeatureDescriptors(i, compResolver, null,
        buf);
    if (!fdPass) {
      pass = false;
    }

    return pass;
  }

  public static boolean testELResolver(ELContext elContext, ELResolver resolver,
      Object base, Object property, Object value, StringBuffer buf,
      boolean readOnly) {

    boolean pass = true;

    // setValue()
    elContext.setPropertyResolved(false);
    try {
      resolver.setValue(elContext, base, property, value);
      if (readOnly) {
        buf.append("setValue() failed on non-writable property");
        pass = false;
      } else {
        buf.append(
            "setValue() tested successfully on writable " + "property" + NL);
      }

    } catch (PropertyNotWritableException pnwe) {
      if (readOnly) {
        buf.append("setValue() tested successfully on non-writable "
            + "property" + NL);
        buf.append("PropertyNotWritableException caught as expected" + NL);
      } else {
        buf.append("setValue() failed on non-writable property" + NL);
        buf.append("Unexpected PropertyNotWritableException caught" + NL);
        pass = false;
      }
    }

    // getValue()
    elContext.setPropertyResolved(false);
    Object valueRetrieved = resolver.getValue(elContext, base, property);
    if (!elContext.isPropertyResolved()) {
      buf.append("getValue() did not resolve" + NL);
      pass = false;
    }

    if (!readOnly && valueRetrieved != value) {
      if (valueRetrieved == null) {
        buf.append("null value returned from getValue() method call!" + NL);
        pass = false;
      } else {
        buf.append("Invalid value from getValue():" + NL);
        buf.append("Value expected: " + value.toString() + NL);
        buf.append("Value retrieved: " + valueRetrieved.toString() + NL);
        pass = false;
      }
    } else {
      buf.append("getValue() tested successfully" + NL);
      if (!readOnly) {
        buf.append("Value expected: " + value.toString() + NL);
        buf.append("Value retrieved: " + valueRetrieved.toString() + NL);
      }
    }

    // getType()
    elContext.setPropertyResolved(false);
    Class type = resolver.getType(elContext, base, property);
    if (!elContext.isPropertyResolved()) {
      buf.append("getType() did not resolve" + NL);
      pass = false;
    } else if (type != null) {
      buf.append("getType() returns " + type.getName() + NL);
      if (readOnly) {
        buf.append("result not expected!" + NL);
        pass = false;
      } else {
        buf.append("non-null as expected" + NL);
      }
    } else {
      buf.append("getType() returns null " + NL);
      if (!readOnly) {
        buf.append("result not expected!" + NL);
        pass = false;
      } else {
        buf.append("as expected" + NL);
      }
    }

    // isReadOnly
    elContext.setPropertyResolved(false);
    boolean nonWritable = resolver.isReadOnly(elContext, base, property);
    if (!elContext.isPropertyResolved()) {
      buf.append("isReadOnly() did not resolve" + NL);
      pass = false;
    } else if (nonWritable != readOnly) {
      buf.append("isReadOnly() returned unexpected value: " + nonWritable + NL);
      pass = false;
    } else {
      buf.append("isReadOnly() returns " + readOnly + " as expected" + NL);
    }

    // getCommonPropertyType()
    elContext.setPropertyResolved(false);
    Class commonPropertyType = (resolver.getCommonPropertyType(elContext,
        base));
    buf.append("getCommonPropertyType() returns ");
    buf.append(commonPropertyType.getName() + "" + NL);

    // getFeatureDescriptors()
    elContext.setPropertyResolved(false);
    Iterator i = resolver.getFeatureDescriptors(elContext, base);
    boolean fdPass = ResolverTest.testFeatureDescriptors(i, resolver, base,
        buf);
    if (!fdPass) {
      pass = false;
    }
    return pass;
  }

  /**
   * Test the ELResolver.invoke() mthod.
   * 
   * @param elContext
   *          - elContext
   * @param resolver
   *          - el Resolver to be tested.
   * @param beanName
   *          - The javabean to be used.
   * @param methodName
   *          - The method in the javabean to call.
   * @param types
   *          - The @class types of the aurgs for the method.
   * @param values
   *          - The args that you want to pass into the method.
   * @param negTest
   *          - Is this a negetive test or not(true, false).
   * @param buf
   *          - String buffer used to capture loggig info.
   * @return
   */
  public static boolean testELResolverInvoke(ELContext elContext,
      ELResolver resolver, Object beanName, Object methodName, Class[] types,
      Object[] values, Boolean negTest, StringBuffer buf) {

    boolean pass = true;

    // invoke
    elContext.setPropertyResolved(false);
    try {
      Boolean nameMatch = (Boolean) resolver.invoke(elContext, beanName,
          methodName, types, values);

      if (!nameMatch) {
        buf.append("invoke() did not Run properly." + NL);
        pass = false;
      }

    } catch (MethodNotFoundException mnfe) {
      if (negTest) {
        buf.append("Test Passed  invoke() threw MethodNotFoundException");
      } else {
        pass = false;
        mnfe.printStackTrace();
      }
    } catch (Exception e) {
      pass = false;
      e.printStackTrace();
    }

    return pass;
  }

  public static boolean testFeatureDescriptors(Iterator i, ELResolver resolver,
      Object base, StringBuffer buf) {

    if (i == null) {
      buf.append("getFeatureDescriptors() returns null" + NL);
      if (resolver instanceof ArrayELResolver
          || resolver instanceof ListELResolver) {
        return true;
      } else if (resolver instanceof MapELResolver && !(base instanceof Map)) {
        return true;
      } else if (resolver instanceof BeanELResolver && base != null) {
        return true;
      } else if (resolver instanceof BarELResolver) {
        return true;
      } else {
        return false;
      }
    }

    while (i.hasNext()) {
      Object obj = i.next();
      if (!(obj instanceof FeatureDescriptor)) {
        buf.append("getFeatureDescriptors() ");
        buf.append(
            "does not return a collection of " + "FeatureDescriptors" + NL);
        return false;
      } else {
        int numAttribs = 0;
        FeatureDescriptor fd = (FeatureDescriptor) obj;
        Enumeration e = fd.attributeNames();
        while (e.hasMoreElements()) {
          String attrib = (String) e.nextElement();
          if (attrib.equals("type")) {
            ++numAttribs;
            if (!(fd.getValue(attrib) instanceof Class)) {
              buf.append("getFeatureDescriptors(): ");
              buf.append("Invalid attribute for type." + NL);
              return false;
            }
          }
          if (attrib.equals("resolvableAtDesignTime")) {
            ++numAttribs;
            if (!(fd.getValue(attrib) instanceof Boolean)) {
              buf.append("getFeatureDescriptors(): ");
              buf.append(
                  "Invalid attribute for " + "resolvableAtDesignTime." + NL);
              return false;
            }
          }
        }
        if (numAttribs < 2) {
          buf.append("getFeatureDescriptors(): ");
          buf.append("Required attribute missing." + NL);
          return false;
        }
      } // else
    } // while

    buf.append("Passed all getFeatureDescriptors() tests" + NL);
    return true;
  }

  // --- Start Negative Method Tests ---
  public static boolean testELResolverNPE(ELResolver resolver, Object base,
      Object property, Object value, StringBuffer buf) {

    boolean pass = true;

    // getType()
    try {
      resolver.getType(null, base, property);
      pass = false;
      buf.append("getType test failed with: " + NL
          + "EXPECTED: NullPointerException to be thrown " + NL
          + "RECEIVED: NO EXCEPTION THROWN AT ALL! " + NL);
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        buf.append("getType test failed with: " + NL + "EXPECTED: "
            + "NullPointerException to be thrown " + NL + "RECEIVED: "
            + e.toString() + NL);
        pass = false;

      } else {
        buf.append("getType() test Passed: throws "
            + "NullPointerException as expected " + NL);
      }
    }

    // getValue
    try {
      resolver.getValue(null, base, property);
      buf.append("Test Failed  getValue() did not throw any exception." + NL
          + "Expected: NullPointerException " + NL);

      pass = false;
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        buf.append("Test Failed  getValue() threw the wrong exception " + NL
            + "Expected: NullPointerException " + NL + "Received: "
            + e.toString());

        pass = false;
      } else {
        buf.append("Test Passed  getValue() threw NullPointerException " + NL);
      }
    }

    // setValue()
    try {
      resolver.setValue(null, base, property, value);
      buf.append("Test Failed  setValue() did not throw any exception." + NL
          + "Expected: NullPointerException " + NL);

      pass = false;
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        buf.append("Test Failed  setValue() threw the wrong exception " + NL
            + "Expected: NullPointerException " + NL + "Received: "
            + e.toString());

        pass = false;
      } else {
        buf.append("Test Passed  setValue() threw NullPointerException " + NL);
      }
    }

    // isReadOnly()
    try {
      resolver.isReadOnly(null, base, property);
      buf.append("Test Failed  isReadOnly() did not throw any exception." + NL
          + "Expected: NullPointerException " + NL);

      pass = false;
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        buf.append("Test Failed  isReadOnly() threw the wrong exception " + NL
            + "Expected: NullPointerException " + NL + "Received: "
            + e.toString());

        pass = false;
      } else {
        buf.append(
            "Test Passed  isReadOnly() NullPointerException " + "thrown " + NL);
      }
    }

    return pass;
  }

  public static boolean testELResolverPNFE(ELContext elcontext,
      ELResolver resolver, Object base, Object property, StringBuffer buf) {

    boolean pass = true;

    // getType()
    try {
      resolver.getType(elcontext, base, property);
      buf.append("Test Failed  getType() did not throw any exception." + NL
          + "Expected: PropertyNotFoundException " + NL);

      pass = false;
    } catch (Exception e) {
      if (!(e instanceof PropertyNotFoundException)) {
        buf.append("Test Failed  getType() threw the wrong exception " + NL
            + "Expected: PropertyNotFoundException " + NL + "Received: "
            + e.toString());

        pass = false;
      } else {
        buf.append("Test Passed  getType() threw "
            + "PropertyNotFoundException " + NL);
      }
    }

    // isReadOnly()
    try {
      resolver.isReadOnly(elcontext, base, property);
      buf.append("Test Failed  isReadOnly() did not throw any exception." + NL
          + "Expected: PropertyNotFoundException " + NL);

      pass = false;
    } catch (Exception e) {
      if (!(e instanceof PropertyNotFoundException)) {
        buf.append("Test Failed  isReadOnly() threw the wrong exception " + NL
            + "Expected: PropertyNotFoundException " + NL + "Received: "
            + e.toString());

        pass = false;
      } else {
        buf.append("Test Passed  isReadOnly() threw "
            + "PropertyNotFoundException " + NL);
      }
    }

    // setValue()
    try {
      resolver.setValue(elcontext, base, property, "arbitrary value");
      buf.append("Test Failed  setValue() did not throw any exception." + NL
          + "Expected: PropertyNotFoundException " + NL);

      pass = false;
    } catch (Exception e) {
      if (!(e instanceof PropertyNotFoundException)) {
        buf.append("Test Failed  setValue() threw the wrong exception " + NL
            + "Expected: PropertyNotFoundException " + NL + "Received: "
            + e.toString());

        pass = false;
      } else {
        buf.append("Test Passed  setValue() threw "
            + "PropertyNotFoundException " + NL);
      }
    }

    if (!(resolver instanceof BeanELResolver)) {
      return pass;
    }

    // getValue()
    try {
      resolver.getValue(elcontext, base, property);
      buf.append("Test Failed  getValue() did not throw any exception." + NL
          + "Expected: PropertyNotFoundException " + NL);

      pass = false;
    } catch (Exception e) {
      if (!(e instanceof PropertyNotFoundException)) {
        buf.append("Test Failed  getValue() threw the wrong exception " + NL
            + "Expected: PropertyNotFoundException " + NL + "Received: "
            + e.toString());

        pass = false;
      } else {
        buf.append("Test Passed  getValue() threw "
            + "PropertyNotFoundException " + NL);
      }
    }

    return pass;
  }

  public static boolean testELResolverIAE(ELContext elcontext,
      ELResolver resolver, Object base, Object property, Object value,
      StringBuffer buf) {

    boolean pass = true;

    // getValue()
    try {
      resolver.getValue(elcontext, base, value);
      buf.append("Test Failed  getValue() did not throw any exception." + NL
          + "Expected: IllegalArgumentException " + NL);

      pass = false;
    } catch (Exception e) {
      if (!(e instanceof IllegalArgumentException)) {
        buf.append("Test Failed  getValue() threw the wrong exception " + NL
            + "Expected: IllegalArgumentException " + NL + "Received: "
            + e.toString());

        pass = false;
      } else {
        buf.append("Test Passed  getValue() threw "
            + "IllegalArgumentException " + NL);
      }
    }

    // setValue()
    try {
      resolver.setValue(elcontext, base, property, value);
      buf.append("Test Failed  setValue() did not throw any exception." + NL
          + "Expected: IllegalArgumentException " + NL);

      pass = false;
    } catch (Exception e) {
      if (!(e instanceof IllegalArgumentException)) {
        buf.append("Test Failed  setValue() threw the wrong exception "
            + "Expected: IllegalArgumentException " + NL + "Received: "
            + e.toString());

        pass = false;
      } else {
        buf.append("Test Passed  setValue() threw "
            + "IllegalArgumentException " + NL);
      }
    }

    return pass;
  }

  public static boolean testELResolverPNWE(ELContext elcontext,
      ELResolver resolver, Object base, Object property, Object value,
      StringBuffer buf) {

    boolean pass = true;

    // setValue()
    try {
      resolver.setValue(elcontext, base, property, value);
      buf.append("Test Failed  setValue() did not throw any exception." + NL
          + "Expected: PropertyNotWritableException " + NL);
      pass = false;

    } catch (Exception e) {
      if (!(e instanceof PropertyNotWritableException)) {
        buf.append("Test Failed  setValue() threw the wrong exception " + NL
            + "Expected: PropertyNotWritableException " + NL + "Received: "
            + e.toString());
        pass = false;

      } else {
        buf.append("Test Passed  setValue() threw "
            + "PropertyNotWritableException " + NL);
      }
    }

    return pass;
  }
}
