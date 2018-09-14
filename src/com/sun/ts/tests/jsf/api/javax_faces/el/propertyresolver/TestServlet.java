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

package com.sun.ts.tests.jsf.api.javax_faces.el.propertyresolver;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;
import com.sun.ts.tests.jsf.api.javax_faces.el.common.TestBean;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.faces.el.PropertyResolver;
import javax.faces.el.PropertyNotFoundException;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.beans.Beans;
import java.math.BigDecimal;

public final class TestServlet extends HttpTCKServlet {

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  // ------------------------------------------- Test Methods ----

  // PropertyResolver.getValue(Object, int)
  public void propResolverGetValueIndexTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    PropertyResolver resolver = getApplication().getPropertyResolver();

    if (resolver == null) {
      out.println(
          JSFTestUtil.FAIL + " Unable to obtain PropertyResolver instance"
              + "from Application object.");
      return;
    }

    Set set = new HashSet();
    Vector v = new Vector();
    StringBuffer sb = new StringBuffer();
    List list = new ArrayList();
    list.add(v);
    list.add(sb);
    list.add(set);

    long[] lArray = new long[] { 500L };

    try {
      Object value = resolver.getValue(list, 2);
      if (!set.equals(value)) {
        out.println(JSFTestUtil.FAIL + " PropertyResolver returned"
            + " unexpected value.");
        out.println("Expected: " + set);
        out.println("Received: " + value);
      }
    } catch (Exception e) {
      e.printStackTrace();
      out.println("Test FAILED[1]. Unexpected Exception calling "
          + "PropertyResolver.getValue().");
      out.println(e);
      return;
    }

    // Expected Value
    Long expected = Long.valueOf(500L);
    try {
      Object value = resolver.getValue(lArray, 0);
      if (!expected.equals(value)) {
        out.println(JSFTestUtil.FAIL + " PropertyResolver returned"
            + " unexpected value.");
        out.println("Expected: " + expected);
        out.println("Received: " + value);
      }
    } catch (Exception e) {
      e.printStackTrace();
      out.println("Test FAILED[2]. Unexpected Exception calling "
          + "PropertyResolver.getValue().");
      out.println(e);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // If base object is null, null is returned
  public void propResolverGetValueIndexNullTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    PropertyResolver resolver = getApplication().getPropertyResolver();

    if (resolver == null) {
      out.println(
          JSFTestUtil.FAIL + " Unable to obtain PropertyResolver instance"
              + "from Application object.");
      return;
    }

    Object result = null;
    try {
      result = resolver.getValue(null, 0);
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        out.println("Unexected exception when passing null to"
            + " getValue(Object, int).");
        out.println("Exception received: " + e);
        return;
      }
    }

    if (result != null) {
      out.println("Expected null to be returned by getValue("
          + "Object, int) when the Object argument is null.");
      out.println("Value received: " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void propResolverGetValueIndexIOBNullTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    PropertyResolver resolver = getApplication().getPropertyResolver();

    if (resolver == null) {
      out.println(
          JSFTestUtil.FAIL + " Unable to obtain PropertyResolver instance"
              + "from Application object.");
      return;
    }

    List list = new ArrayList();
    list.add("val1");
    Object result = null;
    try {
      result = resolver.getValue(new Object[] { "val" }, -1);
    } catch (Exception e) {
      if (!(e instanceof IndexOutOfBoundsException)) {
        out.println(JSFTestUtil.FAIL
            + " Unexected exception when passing an out-of-range"
            + "index to getValue(Object, int).");
        out.println("Exception received: " + e);
        return;
      }
    }

    if (result != null) {
      out.println("Expected null to be returned by getValue("
          + "Object, int) when the Object argument is null.");
      out.println("Value received: " + result);
      return;
    }

    try {
      result = resolver.getValue(list, 3);
    } catch (Exception e) {
      if (!(e instanceof IndexOutOfBoundsException)) {
        out.println(
            "Test FAILED[2].  Unexected exception when passing an out-of-range"
                + "index to getValue(Object, int).");
        out.println("Exception received: " + e);
        return;
      }
    }

    if (result != null) {
      out.println("Expected null to be returned by getValue("
          + "Object, int) when the Object argument is null.");
      out.println("Value received: " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // PropertyResolver.getValue(Object, String)
  public void propResolverGetValueTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    PropertyResolver resolver = getApplication().getPropertyResolver();

    if (resolver == null) {
      out.println(
          JSFTestUtil.FAIL + " Unable to obtain PropertyResolver instance"
              + "from Application object.");
      return;
    }

    TestBean bean = getTestBean();
    Integer expected = Integer.valueOf(1);

    try {
      Object result = resolver.getValue(bean, "readOnly");
      if (!expected.equals(result)) {
        out.println("Test FAILED[1].  Unexpected result returned"
            + "by PropertyResolver.getValue(Object, String) when"
            + "trying to resolve a property of a Java Bean.");
        out.println("Expected: " + expected);
        out.println("Received: " + result);
      }
    } catch (Exception e) {
      throw new ServletException("Test FAILED[1].  Unexpected Exception", e);
    }

    Map map = new HashMap();
    map.put("key", "value");
    map.put(" key", "value1");

    try {
      Object result = resolver.getValue(map, "key");
      if (!"value".equals(result)) {
        out.println("Test FAILED[1].  Unexpected result returned"
            + "by PropertyResolver.getValue(Object, String) when"
            + "trying to resolve a value from a Map.");
        out.println("Expected: value");
        out.println("Received: " + result);
      }
    } catch (Exception e) {
      throw new ServletException("Test FAILED[1].  Unexpected Exception", e);
    }

    out.println(JSFTestUtil.PASS);
  }

  public void propResolverGetValueNullTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    PropertyResolver resolver = getApplication().getPropertyResolver();

    if (resolver == null) {
      out.println(
          JSFTestUtil.FAIL + " Unable to obtain PropertyResolver instance"
              + "from Application object.");
      return;
    }

    Object result = null;
    try {
      result = resolver.getValue(getTestBean(), null);
    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + " Unexpected exception when passing a"
          + " null value to getValue(Object, Object).");
      out.println("Exception received: " + e);
      return;
    }

    if (result != null) {
      out.println(JSFTestUtil.FAIL + " getValue(Object, Object) didn't return"
          + " null when the value provided was null.");
      out.println("Value received: " + result);
    }

    try {
      result = resolver.getValue(null, "value");
    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + " Unexpected exception when passing a"
          + " null base to getValue(Object, Object).");
      out.println("Exception received: " + e);
      return;
    }

    if (result != null) {
      out.println(JSFTestUtil.FAIL + " getValue(Object, Object) didn't return"
          + " null when the base provided was null.");
      out.println("Value received: " + result);
    }

    out.println(JSFTestUtil.PASS);
  }

  public void propResolverGetValuePNFETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    PropertyResolver resolver = getApplication().getPropertyResolver();

    if (resolver == null) {
      out.println(
          JSFTestUtil.FAIL + " Unable to obtain PropertyResolver instance"
              + "from Application object.");
      return;
    }

    // Check for non-exist property
    TestBean bean = getTestBean();

    try {
      resolver.getValue(bean, "nosuchproperty");
      out.println(JSFTestUtil.FAIL + " No Exception thrown when trying to get"
          + " the value of a non-existent property.");
      return;
    } catch (Exception e) {
      if (!(e instanceof PropertyNotFoundException)) {
        out.println(JSFTestUtil.FAIL + " An Exception was thrown when trying"
            + " to get the value of a non-existent property, but"
            + " it wasn't an instance of PropertyNotFoundException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    // Check for write only property

    try {
      resolver.getValue(bean, "onlyWritable");
      out.println(JSFTestUtil.FAIL + " No Exception thrown when trying to get"
          + " the value of a write-only property.");
      return;
    } catch (Exception e) {
      if (!(e instanceof PropertyNotFoundException)) {
        out.println(JSFTestUtil.FAIL + " An Exception was thrown when trying"
            + " to get the value of a write-only property, but"
            + " it wasn't an instance of PropertyNotFoundException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  // PropertyResolver.setValue(Object, int, Object)
  public void propResolverSetValueIndexTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    PropertyResolver resolver = getApplication().getPropertyResolver();

    if (resolver == null) {
      out.println(
          JSFTestUtil.FAIL + " Unable to obtain PropertyResolver instance"
              + "from Application object.");
      return;
    }

    Set set = new HashSet();
    Vector v = new Vector();
    StringBuffer sb = new StringBuffer();
    List list = new ArrayList();
    list.add(v);
    list.add(sb);
    list.add(set);

    long[] lArray = new long[] { 500L };

    try {
      resolver.setValue(list, 1, set);
      Object value = resolver.getValue(list, 1);
      if (!set.equals(value)) {
        out.println(JSFTestUtil.FAIL + " PropertyResolver returned"
            + " unexpected value after setting.");
        out.println("Expected: " + set);
        out.println("Received: " + value);
      }
    } catch (Exception e) {
      e.printStackTrace();
      out.println("Test FAILED[1]. Unexpected Exception calling "
          + "PropertyResolver.get/setValue().");
      out.println(e);
      return;
    }

    try {
      Long expected = Long.valueOf(300L);
      resolver.setValue(lArray, 0, expected);
      Object value = resolver.getValue(lArray, 0);

      if (!expected.equals(value)) {
        out.println(JSFTestUtil.FAIL + " PropertyResolver returned"
            + " unexpected value after setting.");
        out.println("Expected: " + expected);
        out.println("Received: " + value);
      }
    } catch (Exception e) {
      e.printStackTrace();
      out.println("Test FAILED[2]. Unexpected Exception calling "
          + "PropertyResolver.get/setValue().");
      out.println(e);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // PropertyResolver.setValue(Object, int, Object) throws PFNE
  // when invalid index specified or null input is provided
  public void propResolverSetValueIndexPNFETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    PropertyResolver resolver = getApplication().getPropertyResolver();

    if (resolver == null) {
      out.println(
          JSFTestUtil.FAIL + " Unable to obtain PropertyResolver instance"
              + "from Application object.");
      return;
    }

    List list = new ArrayList();
    list.add("val1");

    try {
      resolver.setValue(new Object[] { "val" }, -1, null);
      out.println("Test FAILED[1].  No Exception thrown when an invalid"
          + " index was provided to PropertyResolver.setValue("
          + "Object, int, Object).");
      return;
    } catch (Exception e) {
      if (!(e instanceof PropertyNotFoundException)) {
        out.println("Test FAILED[1].  Exception thrown when an invalid"
            + " index was provided to PropertyResolver.set"
            + "Value(Object, int, Object), but it wasn't an instance of"
            + "PropertyNotFoundException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    try {
      resolver.setValue(list, 3, null);
      out.println("Test FAILED[2].  No Exception thrown when an invalid"
          + " index was provided to PropertyResolver.setValue("
          + "Object, int, Object).");
      return;
    } catch (Exception e) {
      if (!(e instanceof PropertyNotFoundException)) {
        out.println("Test FAILED[2].  Exception thrown when an invalid"
            + " index was provided to PropertyResolver.set"
            + "Value(Object, int, Object), but it wasn't an instance of"
            + "PropertyNotFoundException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    // null inputs

    try {
      resolver.setValue(null, 0, "");
      out.println(JSFTestUtil.FAIL + " No Exception thrown when a null"
          + " Object argument was passed to PropertyRes"
          + "olver.setValue(Object, int, Object)");
      return;
    } catch (Exception e) {
      if (!(e instanceof PropertyNotFoundException)) {
        out.println(JSFTestUtil.FAIL + " An Exception was thrown when a null"
            + "Object was passed to PropertyResolver.setValue("
            + "Object, int, Object), but it wasn't an instance of"
            + "PropertyNotFoundException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    Object[] objArray = new Object[1];
    try {
      resolver.setValue(objArray, 0, null);

    } catch (Exception e) {
      e.printStackTrace();
      out.println(JSFTestUtil.FAIL + " Unepxected Exception thrown when calling"
          + "PropertyResolver.setValue(Object, int, Object) when"
          + "the second Object argument was null.");
      out.println(e);
    }

    out.println(JSFTestUtil.PASS);
  }

  // PropertyResolver.setValue(Object, String, Object)
  public void propResolverSetValueTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    PropertyResolver resolver = getApplication().getPropertyResolver();

    if (resolver == null) {
      out.println(
          JSFTestUtil.FAIL + " Unable to obtain PropertyResolver instance"
              + "from Application object.");
      return;
    }

    TestBean bean = getTestBean();

    try {
      resolver.setValue(bean, "writable", Boolean.FALSE);
      Object result = resolver.getValue(bean, "writable");
      if (!Boolean.FALSE.equals(result)) {
        out.println("Test FAILED[1].  Unexpected result returned"
            + "by PropertyResolver.setValue(Object, String, Object) when"
            + "trying to set a property of a Java Bean.");
        out.println("Expected: " + Boolean.FALSE);
        out.println("Received: " + result);
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServletException(
          "Test FAILED[1].  Unexpected Exception" + e.toString(), e);
    }

    Map map = new HashMap();
    map.put("key", "value");
    map.put(" key", "value1");

    try {
      resolver.setValue(map, "key", "newValue");
      Object result = resolver.getValue(map, "key");
      if (!"newValue".equals(result)) {
        out.println("Test FAILED[1].  Unexpected result returned"
            + "by PropertyResolver.setValue(Object, String, Object) when"
            + "trying to set a value from a Map.");
        out.println("Expected: value");
        out.println("Received: " + result);
      }
    } catch (Exception e) {
      throw new ServletException("Test FAILED[1].  Unexpected Exception", e);
    }

    out.println(JSFTestUtil.PASS);
  }

  // PropertyResolver.setValue(Object, String, Object) throws PNFE
  // if property is not writable, or doesn't exist, or either
  // base or name are null
  public void propResolverSetValuePNFETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    PropertyResolver resolver = getApplication().getPropertyResolver();

    if (resolver == null) {
      out.println(
          JSFTestUtil.FAIL + " Unable to obtain PropertyResolver instance"
              + "from Application object.");
      return;
    }

    // Check for non-exist property
    TestBean bean = getTestBean();

    try {
      resolver.setValue(bean, "nosuchproperty", "value");
      out.println(JSFTestUtil.FAIL + " No Exception thrown when trying to set"
          + " the value of a non-existent property.");
      return;
    } catch (Exception e) {
      if (!(e instanceof PropertyNotFoundException)) {
        out.println(JSFTestUtil.FAIL + " An Exception was thrown when trying"
            + " to set the value of a non-existent property, but"
            + " it wasn't an instance of PropertyNotFoundException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    // Check for read only property

    try {
      resolver.setValue(bean, "readOnly", "value");
      out.println(JSFTestUtil.FAIL + " No Exception thrown when trying to set"
          + " the value of a read-only property.");
      return;
    } catch (Exception e) {
      if (!(e instanceof PropertyNotFoundException)) {
        out.println(JSFTestUtil.FAIL + " An Exception was thrown when trying"
            + " to set the value of a read-only property, but"
            + " it wasn't an instance of PropertyNotFoundException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    // Check null inputs

    try {
      resolver.setValue(null, 0, "");
      out.println(JSFTestUtil.FAIL + " No Exception thrown when a null"
          + " Object argument was passed to PropertyRes"
          + "olver.setValue(Object, int, Object)");
      return;
    } catch (Exception e) {
      if (!(e instanceof PropertyNotFoundException)) {
        out.println(JSFTestUtil.FAIL + " An Exception was thrown when a null"
            + "Object was passed to PropertyResolver.setValue("
            + "Object, int, Object), but it wasn't an instance of"
            + "PropertyNotFoundException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    Object[] objArray = new Object[1];
    try {
      resolver.setValue(objArray, 0, null);

    } catch (Exception e) {
      e.printStackTrace();
      out.println(JSFTestUtil.FAIL + " Unepxected Exception thrown when calling"
          + "PropertyResolver.setValue(Object, int, Object) when"
          + "the second Object argument was null.");
      out.println(e);
    }

    out.println(JSFTestUtil.PASS);
  }

  // PropertyResolver.getType(Object, int) throws PNFE when invalid index
  // is provided.
  public void propResolverGetTypeIndexPNFETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    PropertyResolver resolver = getApplication().getPropertyResolver();

    if (resolver == null) {
      out.println(
          JSFTestUtil.FAIL + " Unable to obtain PropertyResolver instance"
              + "from Application object.");
      return;
    }

    List list = new ArrayList();
    list.add("val1");

    try {
      resolver.getType(new Object[] { "val" }, -1);
      out.println("Test FAILED[1].  No Exception thrown when an invalid"
          + " index was provided to PropertyResolver.getType("
          + "Object, int).");
      return;
    } catch (Exception e) {
      if (!(e instanceof PropertyNotFoundException)) {
        out.println("Test FAILED[1].  Exception thrown when an invalid"
            + " index was provided to PropertyResolver.get"
            + "Type(Object, int), but it wasn't an instance of"
            + "PropertyNotFoundException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    try {
      resolver.getType(list, 3);
      out.println("Test FAILED[2].  No Exception thrown when an invalid"
          + " index was provided to PropertyResolver.getType("
          + "Object, int).");
      return;
    } catch (Exception e) {
      if (!(e instanceof PropertyNotFoundException)) {
        out.println("Test FAILED[2].  Exception thrown when an invalid"
            + " index was provided to PropertyResolver.get"
            + "Type(Object, int), but it wasn't an instance of"
            + "PropertyNotFoundException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    // null input
    try {
      resolver.getType(null, 0);
      out.println(JSFTestUtil.FAIL + " No Exception thrown when a null"
          + " Object argument was passed to PropertyRes"
          + "olver.getType(Object, int)");
      return;
    } catch (Exception e) {
      if (!(e instanceof PropertyNotFoundException)) {
        out.println(JSFTestUtil.FAIL + " An Exception was thrown when a null"
            + "Object was passed to PropertyResolver.getType("
            + "Object, int), but it wasn't an instance of"
            + "PropertyNotFoundException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  // PropertyResolver.getType(Object, String) throws
  // PropertyNotFoundException if null inputs or property doesn't exist
  public void propResolverGetTypePNFETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    PropertyResolver resolver = getApplication().getPropertyResolver();

    if (resolver == null) {
      out.println(
          JSFTestUtil.FAIL + " Unable to obtain PropertyResolver instance"
              + "from Application object.");
      return;
    }

    // Check for non-exist property
    TestBean bean = getTestBean();

    try {
      resolver.getType(bean, "nosuchproperty");
      out.println(JSFTestUtil.FAIL + " No Exception thrown when trying to get"
          + " the type of a non-existent property.");
      return;
    } catch (Exception e) {
      if (!(e instanceof PropertyNotFoundException)) {
        out.println(JSFTestUtil.FAIL + " An Exception was thrown when trying"
            + " to get the type of a non-existent property, but"
            + " it wasn't an instance of PropertyNotFoundException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    // null inputs
    try {
      resolver.getType(null, "");
      out.println(JSFTestUtil.FAIL + " No Exception thrown when a null"
          + " Object argument was passed to PropertyRes"
          + "olver.getType(Object, String)");
      return;
    } catch (Exception e) {
      if (!(e instanceof PropertyNotFoundException)) {
        out.println(JSFTestUtil.FAIL + " An Exception was thrown when a null"
            + "Object was passed to PropertyResolver.getType("
            + "Object, String), but it wasn't an instance of"
            + "PropertyNotFoundException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  // ------------------------------------------------------- Private Methods -

  private TestBean getTestBean() throws ServletException {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    TestBean bean;
    try {
      bean = (TestBean) Beans.instantiate(loader,
          "com.sun.ts.tests.jsf.api.javax_faces.el.common.TestBean");
    } catch (Exception e) {
      throw new ServletException(
          JSFTestUtil.FAIL + " Unable to instantiate TestBean", e);

    }
    return bean;
  }

}
