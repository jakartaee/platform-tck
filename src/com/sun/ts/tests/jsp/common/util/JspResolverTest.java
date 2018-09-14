/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsp.common.util;

import com.sun.ts.tests.common.el.api.resolver.ResolverTest;
import java.util.Iterator;
import javax.el.ELContext;
import javax.el.PropertyNotWritableException;
import javax.servlet.jsp.el.ImplicitObjectELResolver;
import javax.servlet.jsp.el.ScopedAttributeELResolver;

public class JspResolverTest {

  /**
   * Private as this class will only have static methods and members.
   */
  private JspResolverTest() {
  }

  public static boolean testImplicitObjELResolver(ELContext elContext,
      ImplicitObjectELResolver resolver, Object base, Object property,
      Object value, StringBuffer buf) throws ClassNotFoundException {

    boolean pass = true;

    buf.append("base is " + base + "\n");
    buf.append("property is " + property + "\n");
    buf.append("value is " + value + "\n");

    // setValue()
    elContext.setPropertyResolved(false);
    try {
      resolver.setValue(elContext, base, property, value);
      if (base != null) {
        buf.append("setValue() returned when base was non-null\n");
      } else {
        buf.append("setValue() allowed to write a value\n");
        pass = false;
      }
    } catch (PropertyNotWritableException pnwe) {
      if (base != null) {
        buf.append("setValue() - PropertyNotWritableException ");
        buf.append("when base is non-null\n");
        pass = false;
      } else {
        buf.append("setValue() tested successfully\n");
        buf.append("PropertyNotWritableException caught as expected\n");
      }
    }

    // getValue()
    elContext.setPropertyResolved(false);
    Object valueRetrieved = resolver.getValue(elContext, base, property);
    if (base != null) {
      buf.append("getValue() returned when base was non-null\n");
    } else if (!elContext.isPropertyResolved()) {
      buf.append("getValue() did not resolve\n");
      pass = false;
    } else {
      buf.append("getValue() tested successfully\n");
      buf.append("Value retrieved: " + valueRetrieved.toString() + "\n");
    }

    // getType()
    elContext.setPropertyResolved(false);
    Class type = resolver.getType(elContext, base, property);
    if (base != null) {
      buf.append("getType() returned when base was non-null\n");
    } else if (!elContext.isPropertyResolved()) {
      buf.append("getType() did not resolve\n");
      pass = false;
    } else if (type != null) {
      buf.append("getType() did not return null as expected\n");
      pass = false;
    } else {
      buf.append("getType() returns null as expected\n");
    }

    // isReadOnly
    elContext.setPropertyResolved(false);
    boolean readOnly = resolver.isReadOnly(elContext, base, property);
    if (base != null) {
      buf.append("isReadOnly() returned when base was non-null\n");
    } else if (!elContext.isPropertyResolved()) {
      buf.append("isReadOnly() did not resolve\n");
      pass = false;
    } else if (!readOnly) {
      buf.append("isReadOnly() returned false\n");
      pass = false;
    } else
      buf.append("isReadOnly() returns true as expected\n");

    // getCommonPropertyType()
    elContext.setPropertyResolved(false);
    Class commonPropertyType = (resolver.getCommonPropertyType(elContext,
        base));
    if (base != null) {
      if (commonPropertyType != null) {
        buf.append("getCommonPropertyType() returned non-null value ");
        buf.append("for non-null value of base\n");
        pass = false;
      } else {
        buf.append("getCommonPropertyType() returned null value ");
        buf.append("for non-null value of base as expected\n");
      }

    } else if (commonPropertyType != Class.forName("java.lang.String")) {
      buf.append("getCommonPropertyType did not return String as expecte\n");
      pass = false;
    } else {
      buf.append("getCommonPropertyType() returns Class java.lang.String\n");
    }

    // getFeatureDescriptors()
    elContext.setPropertyResolved(false);
    Iterator i = resolver.getFeatureDescriptors(elContext, base);
    if (base != null) {
      if (i == null) {
        buf.append("getFeatureDescriptors() returned null for ");
        buf.append("a non-null base as expected\n");
      } else {
        buf.append("getFeatureDescriptors() returned non-null ");
        buf.append("value for a non-null base\n");
        pass = false;
      }
    } else {
      boolean fdPass = ResolverTest.testFeatureDescriptors(i, resolver, base,
          buf);
      if (!fdPass)
        pass = false;
    }
    return pass;
  }

  public static boolean testScopedAttrELResolver(ELContext elContext,
      ScopedAttributeELResolver resolver, Object base, Object property,
      Object value, StringBuffer buf) throws ClassNotFoundException {

    boolean pass = true;

    buf.append("base is " + base + "\n");
    buf.append("property is " + property + "\n");
    buf.append("value is " + value + "\n");

    // setValue()
    elContext.setPropertyResolved(false);
    try {
      resolver.setValue(elContext, base, property, value);
      if (base != null) {
        buf.append("setValue() returned when base was non-null\n");
      } else {
        buf.append("setValue() allowed to write a value as expected\n");
      }
    } catch (PropertyNotWritableException pnwe) {
      buf.append("setValue() - PropertyNotWritableException ");
      pass = false;
    }

    // getValue()
    elContext.setPropertyResolved(false);
    Object valueRetrieved = resolver.getValue(elContext, base, property);
    if (base != null) {
      buf.append("getValue() returned when base was non-null\n");
    } else if (!elContext.isPropertyResolved()) {
      buf.append("getValue() did not resolve\n");
      pass = false;
    } else if (valueRetrieved == null) {
      buf.append("Unexpected null value for valueRetrieved\n");
      pass = false;
    } else if (valueRetrieved.toString().equals(value.toString())) {
      buf.append("getValue() tested successfully\n");
    } else {
      buf.append("getValue() returned incorrect value:\n");
      buf.append("Value retrieved: " + valueRetrieved.toString() + "\n");
      buf.append("Value expected: " + value.toString() + "\n");
      pass = false;
    }

    // getType()
    elContext.setPropertyResolved(false);
    Class type = resolver.getType(elContext, base, property);
    if (base != null) {
      buf.append("getType() returned when base was non-null\n");
    } else if (!elContext.isPropertyResolved()) {
      buf.append("getType() did not resolve\n");
      pass = false;
    } else if (type == null) {
      buf.append("getType() returned null unexpectedly\n");
      pass = false;
    } else if (type == Class.forName("java.lang.Object")) {
      buf.append("getType() returned Object as expected\n");
    } else {
      buf.append("getType() returned " + type.toString() + "\n");
      pass = false;
    }

    // isReadOnly
    elContext.setPropertyResolved(false);
    boolean readOnly = resolver.isReadOnly(elContext, base, property);
    if (base != null) {
      buf.append("isReadOnly() returned when base was non-null\n");
    } else if (!elContext.isPropertyResolved()) {
      buf.append("isReadOnly() did not resolve\n");
      pass = false;
    } else if (readOnly) {
      buf.append("isReadOnly() returned true\n");
      pass = false;
    } else
      buf.append("isReadOnly() returns false as expected\n");

    // getCommonPropertyType()
    elContext.setPropertyResolved(false);
    Class commonPropertyType = (resolver.getCommonPropertyType(elContext,
        base));
    if (base != null) {
      if (commonPropertyType != null) {
        buf.append("getCommonPropertyType() returned non-null value ");
        buf.append("for non-null value of base\n");
        pass = false;
      } else {
        buf.append("getCommonPropertyType() returned null value ");
        buf.append("for non-null value of base as expected\n");
      }

    } else if (commonPropertyType != Class.forName("java.lang.String")) {
      buf.append("getCommonPropertyType did not return String as expected\n");
      pass = false;
    } else {
      buf.append("getCommonPropertyType() returns Class java.lang.String\n");
    }

    // getFeatureDescriptors()
    elContext.setPropertyResolved(false);
    Iterator i = resolver.getFeatureDescriptors(elContext, base);
    if (base != null) {
      if (i == null) {
        buf.append("getFeatureDescriptors() returned null for ");
        buf.append("a non-null base as expected\n");
      } else {
        buf.append("getFeatureDescriptors() returned non-null ");
        buf.append("value for a non-null base\n");
        pass = false;
      }
    } else {
      boolean fdPass = ResolverTest.testFeatureDescriptors(i, resolver, base,
          buf);
      if (!fdPass)
        pass = false;
    }
    return pass;
  }
}
