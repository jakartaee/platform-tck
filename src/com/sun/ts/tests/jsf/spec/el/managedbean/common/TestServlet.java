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
package com.sun.ts.tests.jsf.spec.el.managedbean.common;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.faces.application.Application;
import javax.faces.el.ValueBinding;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public final class TestServlet extends HttpTCKServlet {

  private static final String[] SCOPE_NAMES = { "request", "session",
      "application", "view" };

  private static final int SCOPE_NAMES_LENGTH = SCOPE_NAMES.length;

  /**
   * <p>
   * Initialize this <code>Servlet</code> instance.
   * </p>
   * 
   * @param config
   *          the configuration for this <code>Servlet</code>
   * 
   * @throws javax.servlet.ServletException
   *           indicates initialization failure
   */
  public void init(ServletConfig config) throws ServletException {

    super.init(config);

  } // END init

  // ------------------------------------------------------------ Test Methods
  public void managedBeanScopeTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    // ensure ManagedBeans are accessible based on the defined scope
    // in the faces-config.xml. Not interested in accessing the properties
    // themselves, but in the actual instance type returned
    PrintWriter out = response.getWriter();
    Application application = getApplication();
    FacesContext facesContext = getFacesContext();
    ExternalContext extContext = facesContext.getExternalContext();

    String[] beanNames = { "requestScoped", "sessionScoped",
        "applicationScoped", "viewScoped" };

    // Bean classes
    Class[] beanTypes = { RequestScopedBean.class, SessionScopedBean.class,
        ApplicationScopedBean.class };

    // ValueBinding expressions
    String[] expressions = { "#{requestScoped}", "#{sessionScoped}",
        "#{applicationScoped}", "#{viewScoped}" };

    Map[] scopes = { extContext.getRequestMap(), extContext.getSessionMap(),
        extContext.getApplicationMap() };

    boolean passed = true;

    for (int i = 0; i < beanTypes.length; i++) {

      ValueBinding binding = application.createValueBinding(expressions[i]);

      Object val = binding.getValue(facesContext);

      if (val == null) {
        out.println("Test FAILED.  Expression " + expressions[i]
            + " evaluated to null.");
        passed = false;
      }

      if (val != null && !(beanTypes[i].isInstance(val))) {
        out.println("Test FAILED.  Object returned from evaluation of"
            + expressions[i] + " was not an instance of"
            + beanTypes[i].getName() + JSFTestUtil.NL + ".  Type returned:"
            + " " + val.getClass().getName());
        passed = false;
      }

      // now validate the scope
      if (scopes[i].get(beanNames[i]) == null) {
        out.println("Test FAILED.  Unabled to find " + beanNames[i] + "in '"
            + SCOPE_NAMES[i] + "' scope.");
        passed = false;
      }
    }

    if (passed) {
      out.println(JSFTestUtil.PASS);
    }

  } // END mangedBeanScopeTest

  public void managedBeanNoScopeTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    // if scope is set to NONE for a partciular managed bean,
    // then it will not be placed into request, session, or
    // application scopes when instantiated.
    PrintWriter out = response.getWriter();
    Application application = getApplication();
    FacesContext facesContext = getFacesContext();
    ExternalContext extContext = facesContext.getExternalContext();

    String expression = "#{noScopeBean}";

    ValueBinding binding = application.createValueBinding(expression);

    Object val = binding.getValue(facesContext);
    boolean passed = true;

    Map[] scopes = { extContext.getRequestMap(), extContext.getSessionMap(),
        extContext.getApplicationMap() };

    if (val == null) {
      out.println(
          "Test FAILED.  Expression " + expression + " evaluated to null.");
      passed = false;
    }

    if (val != null && !(NoScopeBean.class.isInstance(val))) {
      out.println("Test FAILED.  Object returned from evaluation of"
          + expression + " was not an instance of" + NoScopeBean.class.getName()
          + JSFTestUtil.NL + ". Type returned:" + " "
          + val.getClass().getName());
      passed = false;
    }

    for (int i = 0; i < scopes.length; i++) {
      if (scopes[i].get("noScopeBean") != null) {
        out.println("Test FAILED.  Incorrectly found 'noScopeBean'" + "in '"
            + SCOPE_NAMES[i] + "' scope.");
        passed = false;
      }
    }

    if (passed) {
      out.println(JSFTestUtil.PASS);
    }

  } // END managedBeanNoScopeTest

  public void managedBeanNullManagedPropertyTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    // If <null-value/> element is associated with a particular
    // managed-property, ensure that null is explicitly set.

    PrintWriter out = response.getWriter();
    Application application = getApplication();
    FacesContext facesContext = getFacesContext();

    String expression = "#{managedPropertyBean}";
    ValueBinding binding = application.createValueBinding(expression);

    ManagedPropertyBean bean = (ManagedPropertyBean) binding
        .getValue(facesContext);

    if (bean == null) {
      out.println(
          "Test FAILED.  Expression " + expression + " evaluated to null.");
      return;
    }

    if (bean.getNullProperty() != null) {
      out.println("Test FAILED.  Managed property configured with"
          + "<null-value/> did not have the property explicitly set to"
          + " null when the bean was instantiated and processed by"
          + " the managed-bean facility.");
      out.println("Value returned: " + bean.getNullProperty());
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // END managedBeanNullManagedPropertyTest

  public void managedBeanTrimmedManagedPropertyValueTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    // when using the <value> element with a managed-property,
    // the value must be trimmed of leading/trailing whitespace
    PrintWriter out = response.getWriter();
    Application application = getApplication();
    FacesContext facesContext = getFacesContext();

    String expression = "#{managedPropertyBean}";
    ValueBinding binding = application.createValueBinding(expression);

    ManagedPropertyBean bean = (ManagedPropertyBean) binding
        .getValue(facesContext);

    if (bean == null) {
      out.println(
          "Test FAILED.  Expression " + expression + " evaluated to null.");
      return;
    }

    if (!("propertyValue".equals(bean.getStringProperty()))) {
      out.println("Test FAILED.  Expected managed property configured"
          + " using the <value> element to have leading/trailing "
          + "whitespace removed.");
      out.println("Expected: 'propertyValue'");
      out.println("Received: '" + bean.getStringProperty() + "'");
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // END managedBeanTrimmedManagedPropertyValueTest

  public void managedBeanMapEntriesManagedPropertyTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    Application application = getApplication();
    FacesContext facesContext = getFacesContext();

    String expression = "#{managedPropertyBean}";
    ValueBinding binding = application.createValueBinding(expression);

    ManagedPropertyBean bean = (ManagedPropertyBean) binding
        .getValue(facesContext);

    if (bean == null) {
      out.println(
          "Test FAILED.  Expression " + expression + " evaluated to null.");
      return;
    }

    Map map = bean.getMapProperty();

    if (map.get("nullKey") != null) {
      out.println("Test FAILED.  map-entry configured with"
          + "<null-value/> did not have the map value explicitly set to"
          + " null when the bean was instantiated and processed by"
          + " the managed-bean facility.");
      out.println("Value returned: " + map.get("nullKey"));
      return;
    }

    if (!("propertyValue".equals(map.get("trimmedKey")))) {
      out.println("Test FAILED.  Expected map-entry configured"
          + " using the <value> element to have leading/trailing "
          + "whitespace removed.");
      out.println("Expected: 'propertyValue'");
      out.println("Received: '" + map.get("trimmedKey") + "'");
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // END managedBeanMapEntriesManagedPropertyTest

  public void managedBeanListEntriesManagedPropertyTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    Application application = getApplication();
    FacesContext facesContext = getFacesContext();

    String expression = "#{managedPropertyBean}";
    ValueBinding binding = application.createValueBinding(expression);

    ManagedPropertyBean bean = (ManagedPropertyBean) binding
        .getValue(facesContext);

    if (bean == null) {
      out.println(
          "Test FAILED.  Expression " + expression + " evaluated to null.");
      return;
    }

    List list = bean.getListProperty();

    if (list.size() != 2) {
      out.println("Test FAILED.  Expected the managed list property"
          + " to have 2 elements.");
      out.println("Actual element count: " + list.size());
      return;
    }

    if (!("propertyValue".equals(list.get(0)))) {
      out.println("Test FAILED.  Expected list-entries configured"
          + " using the <value> element to have leading/trailing "
          + "whitespace removed.");
      out.println("Expected: 'propertyValue'");
      out.println("Received: '" + list.get(0) + "'");
      return;
    }

    if (list.get(1) != null) {
      out.println("Test FAILED.  map-entry configured with"
          + "<null-value/> did not have the list value explicitly set to"
          + " null when the bean was instantiated and processed by"
          + " the managed-bean facility.");
      out.println("Value returned: " + list.get(1));
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // END managedBeanListEntriesManagedPropertyTest

  public void managedBeanMapKeyValueConversionTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    // ensure the JSF implementation performs the expected conversion
    // of key and value types when key-class and value-class are defined.
    PrintWriter out = response.getWriter();
    Application application = getApplication();
    FacesContext facesContext = getFacesContext();

    String expression = "#{managedPropertyBean}";
    ValueBinding binding = application.createValueBinding(expression);

    ManagedPropertyBean bean = (ManagedPropertyBean) binding
        .getValue(facesContext);

    if (bean == null) {
      out.println(
          "Test FAILED.  Expression " + expression + " evaluated to null.");
      return;
    }

    Class[] types = { java.lang.Byte.class, java.lang.Character.class,
        java.lang.Boolean.class, java.lang.Short.class, java.lang.Integer.class,
        java.lang.Long.class, java.lang.Float.class, java.lang.Double.class };

    Object[][] control = { { Byte.valueOf((byte) 1), Byte.valueOf((byte) 2) },
        { Character.valueOf('a'), Character.valueOf('b') },
        { Boolean.TRUE, Boolean.FALSE },
        { Short.valueOf((short) 10), Short.valueOf((short) 11) },
        { Integer.valueOf(100), Integer.valueOf(111) },
        { Long.valueOf(1000l), Long.valueOf(1111l) },
        { Float.valueOf(1.0f), Float.valueOf(1.1f) },
        { Double.valueOf(10.0d), Double.valueOf(11.1d) } };

    // all of the get<TYPE>MapProperty calls refer to the same
    // Map instance.
    Map typedMapProperty = bean.getByteMapProperty();
    boolean passed = true;
    for (int i = 0; i < 8; i++) {
      Object value = typedMapProperty.get(control[i][0]);
      if (!(control[i][1].equals(value))) {
        out.println("Test FAILED.  Unexpected value returned for "
            + "key of type '" + types[i] + "'");
        out.println("Expected: " + control[i][1]);
        out.println("Received: " + value + JSFTestUtil.NL);
        passed = false;
      }
    }

    if (passed) {
      out.println(JSFTestUtil.PASS);
    }

  } // END managedBeanMapKeyValueConversionTest

  public void managedBeanListValueConversionTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    // ensure the JSF implementation performs the expected conversion
    // of of values are performed per the defined value-class element
    // for the list entry.
    // This has the side-effect of ensuring that the entries are added
    // in the order they appear in the config file.
    PrintWriter out = response.getWriter();
    Application application = getApplication();
    FacesContext facesContext = getFacesContext();

    String expression = "#{managedPropertyBean}";
    ValueBinding binding = application.createValueBinding(expression);

    ManagedPropertyBean bean = (ManagedPropertyBean) binding
        .getValue(facesContext);

    if (bean == null) {
      out.println(
          "Test FAILED.  Expression " + expression + " evaluated to null.");
      return;
    }

    // all of the get<TYPE>ListProperty calls refer to the same
    // List instance.
    List typedListProperty = bean.getByteListProperty();

    Class[] types = { java.lang.Byte.class, java.lang.Character.class,
        java.lang.Boolean.class, java.lang.Short.class, java.lang.Integer.class,
        java.lang.Long.class, java.lang.Float.class, java.lang.Double.class };

    Object[] control = { Byte.valueOf((byte) 1), Character.valueOf('a'),
        Boolean.TRUE, Short.valueOf((short) 1), Integer.valueOf(11),
        Long.valueOf(111l), Float.valueOf(1.0f), Double.valueOf(11.1d) };

    boolean passed = true;
    for (int i = 0; i < 8; i++) {
      Object value = typedListProperty.get(i);
      if (!(control[i].equals(value))) {
        out.println("Unexpected value stored in managed List, or"
            + " property was added out of order.");
        out.println("Expected element '" + i + "' to be: [" + types[i].getName()
            + "] " + control[i]);
        out.println("Received: [" + value.getClass().getName() + "] " + value
            + JSFTestUtil.NL);
        passed = false;
      }
    }

    if (passed) {
      out.println(JSFTestUtil.PASS);
    }

  } // END managedBeanListValueConversionTest

  public void managedBeanPropertyValueConversionTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    // ensure converstion without the property-class element present
    // is in fact automatic and follows the rules per the JSP spec.
    // This test has the side effect of ensuring the properties are
    // set in the order they appear in the faces-config.
    PrintWriter out = response.getWriter();
    Application application = getApplication();
    FacesContext facesContext = getFacesContext();

    String expression = "#{managedPropertyBean}";
    ValueBinding binding = application.createValueBinding(expression);

    ManagedPropertyBean bean = (ManagedPropertyBean) binding
        .getValue(facesContext);

    if (bean == null) {
      out.println(
          "Test FAILED.  Expression " + expression + " evaluated to null.");
      return;
    }

    Class[] types = { java.lang.Byte.class, java.lang.Character.class,
        java.lang.Boolean.class, java.lang.Short.class, java.lang.Integer.class,
        java.lang.Long.class, java.lang.Float.class, java.lang.Double.class };

    Object[] control = { Byte.valueOf((byte) 1), Character.valueOf('a'),
        Boolean.TRUE, Short.valueOf((short) 11), Integer.valueOf(111),
        Long.valueOf(1111l), Float.valueOf(1.1f), Double.valueOf(11.1d) };

    List primList = bean.getPrimList();
    List objList = bean.getObjList();

    int primSize = primList.size();
    if (primSize != 8) {
      out.println("Test FAILED: Expected the list containing boxed "
          + "primitives to contain 8 elements.  Number of elements " + "found: "
          + primSize);
      return;
    }

    int objSize = objList.size();
    if (objSize != 8) {
      out.println("Test FAILED: Expected the Object list to contain 8 "
          + "elements.  Number of elements found: " + primSize);
      return;
    }

    boolean passed = true;
    for (int i = 0; i < primSize; i++) {
      Object value = primList.get(i);
      if (!(control[i].equals(value))) {
        out.println(
            "Test FAILED(prim). Unexpected value or value out of order.");
        out.println("Expected: [" + types[i] + "] " + control[i]);
        out.println("Received: [" + value.getClass().getName() + "] " + value
            + JSFTestUtil.NL);
        passed = false;
      }
    }

    for (int i = 0; i < objSize; i++) {
      Object value = objList.get(i);
      if (!(control[i].equals(value))) {
        out.println(
            "Test FAILED(obj). Unexpected value or value out of order.");
        out.println("Expected: [" + types[i] + "] " + control[i]);
        out.println("Received: [" + value.getClass().getName() + "] " + value
            + JSFTestUtil.NL);
        passed = false;
      }
    }

    if (passed) {
      out.println(JSFTestUtil.PASS);
    }

  } // END managedBeanPropertyValueConversionTest

  public void managedBeanNoClassExistsTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    Application application = getApplication();
    FacesContext facesContext = getFacesContext();

    String expression = "#{noSuchBean}";
    ValueBinding binding = application.createValueBinding(expression);

    try {
      binding.getValue(facesContext);
      out.println("Test FAILED.  No error condition was raised when "
          + "managed bean was associated with a non-existent class.");
    } catch (Exception e) {
      out.println(JSFTestUtil.PASS);
    }

  } // END managedBeanNoClassExistsTest

  public void managedBeanNoZeroArgCtorTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    Application application = getApplication();
    FacesContext facesContext = getFacesContext();

    String expression = "#{noZeroArgCtorBean}";
    ValueBinding binding = application.createValueBinding(expression);

    try {
      binding.getValue(facesContext);
      out.println("Test FAILED.  No error condition was raised when "
          + "managed bean was associated with class containing no"
          + " zero argument constructor.");
    } catch (Exception e) {
      out.println(JSFTestUtil.PASS);
    }

  } // END managedBeanNoZeroArgCtorTest

  public void managedBeanTypeConversionErrorTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    Application application = getApplication();
    FacesContext facesContext = getFacesContext();

    String expression = "#{typeConversionErrorBean}";
    ValueBinding binding = application.createValueBinding(expression);

    try {
      binding.getValue(facesContext);
      out.println("Test FAILED.  No error condition was raised when "
          + "managed propery value cannot be converted to the specified "
          + "type.");
    } catch (Exception e) {
      out.println(JSFTestUtil.PASS);
    }

  } // END managedBeanTypeConversionErrorTest

  public void managedBeanNoSetterTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    Application application = getApplication();
    FacesContext facesContext = getFacesContext();

    String expression = "#{noSuchPropertyBean}";
    ValueBinding binding = application.createValueBinding(expression);

    try {
      binding.getValue(facesContext);
      out.println("Test FAILED.  No error condition was raised when "
          + "referenced managed property did not exist.");
    } catch (Exception e) {
      out.println(JSFTestUtil.PASS);
    }

  } // END managedBeanNoSetterTest

  public void managedBeanPrivateSetterTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    Application application = getApplication();
    FacesContext facesContext = getFacesContext();

    String expression = "#{noPublicPropertyBean}";
    ValueBinding binding = application.createValueBinding(expression);

    try {
      binding.getValue(facesContext);
      out.println("Test FAILED.  No error condition was raised when "
          + "referenced managed property setter has private method access.");
    } catch (Exception e) {
      out.println(JSFTestUtil.PASS);
    }

  } // END managedBeanPrivatePropertyAccessTest

  public void managedBeanCyclicReferenceTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    Application application = getApplication();
    FacesContext facesContext = getFacesContext();

    String[] expressions = { "#{cyclic1}", "#{cyclic3}", "#{cyclic4}" };

    boolean passed = true;
    for (int i = 0; i < expressions.length; i++) {
      ValueBinding binding = application.createValueBinding(expressions[i]);

      try {
        binding.getValue(facesContext);
        out.println("Test FAILED.  No error condition was raised when "
            + "cyclic references are present between two managed beans.");
        passed = false;
      } catch (Exception e) {
        ;
      }
    }

    if (passed) {
      out.println(JSFTestUtil.PASS);
    }

  } // END managedBeanCyclicReferenceTest

  public void managedBeanScopedReferencesTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    Application application = getApplication();
    FacesContext facesContext = getFacesContext();

    String[] negExpressions = { "#{applicationSession}",
        "#{applicationRequest}", "#{sessionRequest.stringProperty}",
        "#{noneApplication}", "#{noneSession}", "#{noneRequest}",
        "#{applicationSessionImplicit}", "#{applicationRequestImplicit}",
        "#{sessionRequestImplicit}", "#{noneApplicationImplicit}",
        "#{noneSessionImplicit}", "#{noneRequestImplicit}" };

    String[] posExpressions = { "#{noneNone}", "#{applicationNone}",
        "#{applicationApplication}", "#{sessionNone}", "#{sessionApplication}",
        "#{sessionSession}", "#{requestNone}", "#{requestSession}",
        "#{requestApplication}", "#{requestRequest}" };

    boolean passed = true;
    for (int i = 0; i < negExpressions.length; i++) {
      System.out.println("NEG [" + negExpressions[i] + ']');
      ValueBinding binding = application.createValueBinding(negExpressions[i]);

      try {
        binding.getValue(facesContext);
        out.println("Test FAILED.  [" + negExpressions[i] + "] No error "
            + "condition was raised when managed bean contained a "
            + "reference to another bean with a potentially shorter "
            + "scope than what the bean was configured for.");
        passed = false;
      } catch (Exception e) {
        System.out.println("Exception correctly thrown.");
        System.out.println(e);
      }
    }

    for (int i = 0; i < posExpressions.length; i++) {
      System.out.println("POS [" + posExpressions[i] + ']');
      ValueBinding binding = application.createValueBinding(posExpressions[i]);

      try {
        binding.getValue(facesContext);
      } catch (Exception e) {
        passed = false;
        out.println("Test FAILED. [" + posExpressions[i] + "] Error "
            + "incorrectly raised when managed bean contained a "
            + "reference to another bean with a 'longer' scope than "
            + "what the bean was configured for.");
      }
    }

    if (passed) {
      out.println(JSFTestUtil.PASS);
    }

  } // END managedBeanScopedReferencesTest

  public void managedBeanCreateStoreListTypeTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    // if getter for List property returns null, the implementation
    // must create and store an ArrayList.
    PrintWriter out = response.getWriter();
    Application application = getApplication();
    FacesContext facesContext = getFacesContext();

    String expression = "#{managedPropertyBean}";
    ValueBinding binding = application.createValueBinding(expression);

    ManagedPropertyBean bean = (ManagedPropertyBean) binding
        .getValue(facesContext);

    if (bean == null) {
      out.println(
          "Test FAILED.  Expression " + expression + " evaluated to null.");
      return;
    }

    List list = bean.getListProperty();

    if (!(list instanceof ArrayList)) {
      out.println("Test FAILED.  Expected implementation to create and"
          + " store an ArrayList when managed property getter for the list property"
          + " initially returned null.");
      out.println("Type returned: " + list.getClass().getName());
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // END managedBeanCreateStoreListTypeTest

  public void managedBeanCreateStoreMapTypeTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    // if getter for Map property returns null, the implementation
    // must create and store a HashMap.
    PrintWriter out = response.getWriter();
    Application application = getApplication();
    FacesContext facesContext = getFacesContext();

    String expression = "#{managedPropertyBean}";
    ValueBinding binding = application.createValueBinding(expression);

    ManagedPropertyBean bean = (ManagedPropertyBean) binding
        .getValue(facesContext);

    if (bean == null) {
      out.println(
          "Test FAILED.  Expression " + expression + " evaluated to null.");
      return;
    }

    Map map = bean.getByteMapProperty();

    if (!(map instanceof HashMap)) {
      out.println("Test FAILED.  Expected implementation to create and"
          + " store a HashMap when managed property getter for the map property"
          + " initially returned null.");
      out.println("Type returned: " + map.getClass().getName());
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // END managedBeanCreateStoreMapTypeTest

  public void managedBeanPropertyMapEntryOrderTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    // ensure map entries are added in the order specified in the
    // config file
    PrintWriter out = response.getWriter();
    Application application = getApplication();
    FacesContext facesContext = getFacesContext();

    String expression = "#{orderedMap}";
    ValueBinding binding = application.createValueBinding(expression);

    ManagedPropertyBean bean = (ManagedPropertyBean) binding
        .getValue(facesContext);

    if (bean == null) {
      out.println(
          "Test FAILED.  Expression " + expression + " evaluated to null.");
      return;
    }

    LinkedHashMap map = (LinkedHashMap) bean.getOrderedMap();

    String[] control = { "one", "two", "three", "four" };

    List orderedKeys = map.getKeyList();

    if (control.length != orderedKeys.size()) {
      out.println("Test FAILED.  Managed Map property didn't contain"
          + " the expected number of elements (4).");
      out.println("Number of elements: " + orderedKeys.size());
      return;
    }

    boolean passed = true;
    for (int i = 0; i < control.length; i++) {
      if (!control[i].equals(orderedKeys.get(i))) {
        out.println("Test FAILED.  Element out of order.");
        out.println("Expected key at index " + i + ": " + control[i]);
        out.println("Key found at index " + i + ": " + orderedKeys.get(i));
        passed = false;
      }
    }

    if (passed) {
      out.println(JSFTestUtil.PASS);
    }

  } // END managedBeanPropertyMapEntryOrderTest

  public void managedBeanPropertyListEntryOrderTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    // ensure map entries are added in the order specified in the
    // config file
    PrintWriter out = response.getWriter();
    Application application = getApplication();
    FacesContext facesContext = getFacesContext();

    String expression = "#{orderedList}";
    ValueBinding binding = application.createValueBinding(expression);

    ManagedPropertyBean bean = (ManagedPropertyBean) binding
        .getValue(facesContext);

    if (bean == null) {
      out.println(
          "Test FAILED.  Expression " + expression + " evaluated to null.");
      return;
    }

    List orderedList = bean.getOrderedList();
    String[] control = { "one", "two", "three", "four" };

    if (control.length != orderedList.size()) {
      out.println("Test FAILED.  Managed List property didn't contain"
          + " the expected number of elements (4).");
      out.println("Number of elements: " + orderedList.size());
      return;
    }

    boolean passed = true;
    for (int i = 0; i < control.length; i++) {
      if (!control[i].equals(orderedList.get(i))) {
        out.println("Test FAILED.  Element out of order.");
        out.println("Expected value at index " + i + ": " + control[i]);
        out.println("Value found at index " + i + ": " + orderedList.get(i));
        passed = false;
      }
    }

    if (passed) {
      out.println(JSFTestUtil.PASS);
    }

  } // END managedBeanPropertyListEntryOrderTest

  public void managedBeanPropertyArrayTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    Application application = getApplication();
    FacesContext facesContext = getFacesContext();

    String expression = "#{arrayBean}";
    ValueBinding binding = application.createValueBinding(expression);

    ManagedPropertyBean bean = (ManagedPropertyBean) binding
        .getValue(facesContext);

    if (bean == null) {
      out.println(
          "Test FAILED.  Expression " + expression + " evaluated to null.");
      return;
    }

    int[] arrayProperty = bean.getArrayProperty();

    int[] control = { 0, 1, 2, 3, 4 };

    if (arrayProperty.length != control.length) {
      out.println("Test FAILED.  Unexpected number of elements"
          + " contained in the array returned by getArrayProperty().");
      out.println("Expected: 5");
      out.println("Recevied: " + arrayProperty.length);
      return;
    }

    boolean passed = true;
    for (int i = 0; i < control.length; i++) {
      if (control[i] != arrayProperty[i]) {
        out.println("Test FAILED.  Element out of order.");
        out.println("Expected value at index " + i + ": " + control[i]);
        out.println("Value found at index " + i + ": " + arrayProperty[i]);
        passed = false;
      }
    }

    if (passed) {
      out.println(JSFTestUtil.PASS);
    }

  } // END managedBeanPropertyArrayTest

  public void managedBeanMapBeanTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    Application application = getApplication();
    FacesContext facesContext = getFacesContext();

    String expression = "#{MapBean}";
    ValueBinding binding = application.createValueBinding(expression);

    MapBean bean = (MapBean) binding.getValue(facesContext);

    if (bean == null) {
      out.println(
          "Test FAILED.  Expression " + expression + " evaluated to null.");
      return;
    }

    String[] controlKeys = { "key1", "key2" };
    String[] controlValues = { "value1", "value2" };

    boolean passed = true;
    for (int i = 0; i < controlKeys.length; i++) {
      if (!bean.containsKey(controlKeys[i])
          || !controlValues[i].equals(bean.get(controlKeys[i]))) {
        out.println("Test FAILED.  MapBean didn't either didn't contain"
            + " the expected key, or didn't contain the expected value for "
            + "that key.");
        out.println(
            "Expected Key/Value: " + controlKeys[i] + '/' + controlValues[i]);
        out.println("Contains Key: " + bean.containsKey(controlKeys[i]));
        out.println("Value for Key above: " + bean.get(controlKeys[i]));
        passed = false;
      }
    }

    if (passed) {
      out.println(JSFTestUtil.PASS);
    }

  } // END managedBeanMapBeanTest

  public void managedBeanListBeanTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    Application application = getApplication();
    FacesContext facesContext = getFacesContext();

    String expression = "#{ListBean}";
    ValueBinding binding = application.createValueBinding(expression);

    ListBean bean = (ListBean) binding.getValue(facesContext);

    if (bean == null) {
      out.println(
          "Test FAILED.  Expression " + expression + " evaluated to null.");
      return;
    }

    String[] controlValues = { "value1", "value2" };

    boolean passed = true;
    for (int i = 0; i < controlValues.length; i++) {
      if (!controlValues[i].equals(bean.get(i))) {
        out.println("Test FAILED.  ListBean didn't contain the expected"
            + " value at index '" + i + "'");
        out.println("Expected: " + controlValues[i]);
        out.println("Received: " + bean.get(i));
        passed = false;
      }
    }

    if (passed) {
      out.println(JSFTestUtil.PASS);
    }

  } // END managedBeanListBeanTest

  public void managedBeanPostConstructTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    // ensure ManagedBeans implement the @PostConstruct annotation for
    // package private, private, public, and protected methods in
    // request, session, and application scopes.
    PrintWriter out = response.getWriter();
    Application application = getApplication();
    FacesContext facesContext = getFacesContext();

    String[][] beanNames = {
        { "requestScopedPackagePrivate", "requestScopedPrivate",
            "requestScopedProtected", "requestScopedPublic" },
        { "sessionScopedPackagePrivate", "sessionScopedPrivate",
            "sessionScopedProtected", "sessionScopedPublic" },
        { "applicationScopedPackagePrivate", "applicationScopedPrivate",
            "applicationScopedProtected", "applicationScopedPublic" },
        { "viewScopedPackagePrivate", "viewScopedPrivate",
            "viewScopedProtected", "viewScopedPublic" } };

    // ValueBinding expressions
    String[][] expressions = {
        { "#{requestScopedPackagePrivate}", "#{requestScopedPrivate}",
            "#{requestScopedProtected}", "#{requestScopedPublic}" },
        { "#{sessionScopedPackagePrivate}", "#{sessionScopedPrivate}",
            "#{sessionScopedProtected}", "#{sessionScopedPublic}" },
        { "#{applicationScopedPackagePrivate}", "#{applicationScopedPrivate}",
            "#{applicationScopedProtected}", "#{applicationScopedPublic}" },
        { "#{viewScopedPackagePrivate}", "#{viewScopedPrivate}",
            "#{viewScopedProtected}", "#{viewScopedPublic}" } };

    String[] accessModifiers = { "package private", "private", "protected",
        "public" };

    boolean passed = true;

    for (int i = 0; i < SCOPE_NAMES_LENGTH; i++) {

      for (int j = 0; j < accessModifiers.length; ++j) {

        ValueBinding binding = application
            .createValueBinding(expressions[i][j]);

        Object val = binding.getValue(facesContext);

        out.println("testing " + beanNames[i][j]);
        String expectedValue = SCOPE_NAMES[i] + " " + accessModifiers[j]
            + " PostConstruct method invoked";
        String computedValue = ((ScopedBean) val).getPostConstructProperty();
        if (!computedValue.equals(expectedValue)) {
          out.println("Test FAILED:");
          out.println("Expected value = " + expectedValue);
          out.println("Computed value = " + computedValue);
          passed = false;
        }

      }
    }

    if (passed) {
      out.println(JSFTestUtil.PASS);
    }

  } // END managedBeanPostConstructTest

  public void managedBeanPreDestroyTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    // ensure ManagedBeans implement the @PreDestroy annotation for
    // package private, private, public, and protected methods in
    // request, session, and application scopes.
    PrintWriter out = response.getWriter();
    Application application = getApplication();
    FacesContext facesContext = getFacesContext();
    PageContext pageContext = JspFactory.getDefaultFactory().getPageContext(
        this, request, response, null, true, JspWriter.NO_BUFFER, false);

    String[][] beanNames = {
        { "requestScopedPackagePrivate", "requestScopedPrivate",
            "requestScopedProtected", "requestScopedPublic" },
        { "sessionScopedPackagePrivate", "sessionScopedPrivate",
            "sessionScopedProtected", "sessionScopedPublic" },
        { "applicationScopedPackagePrivate", "applicationScopedPrivate",
            "applicationScopedProtected", "applicationScopedPublic" },
        { "viewScopedPackagePrivate", "viewScopedPrivate",
            "viewScopedProtected", "viewScopedPublic" } };

    // ValueBinding expressions
    String[][] expressions = {
        { "#{requestScopedPackagePrivate}", "#{requestScopedPrivate}",
            "#{requestScopedProtected}", "#{requestScopedPublic}" },
        { "#{sessionScopedPackagePrivate}", "#{sessionScopedPrivate}",
            "#{sessionScopedProtected}", "#{sessionScopedPublic}" },
        { "#{applicationScopedPackagePrivate}", "#{applicationScopedPrivate}",
            "#{applicationScopedProtected}", "#{applicationScopedPublic}" },
        { "#{viewScopedPackagePrivate}", "#{viewScopedPrivate}",
            "#{viewScopedProtected}", "#{viewScopedPublic}" } };

    String[] accessModifiers = { "package private", "private", "protected",
        "public" };

    boolean passed = true;

    for (int i = 0; i < SCOPE_NAMES_LENGTH; i++) {

      for (int j = 0; j < accessModifiers.length; ++j) {

        ValueBinding binding = application
            .createValueBinding(expressions[i][j]);
        binding.getValue(facesContext);

        pageContext.removeAttribute(beanNames[i][j]);

        out.println("testing " + beanNames[i][j]);
        String expectedValue = SCOPE_NAMES[i] + " " + accessModifiers[j]
            + " PreDestroy method invoked";
        String computedValue = PreDestroyProp.getPreDestroyProperty();

        if (!computedValue.equals(expectedValue)) {
          out.println("Test FAILED:");
          out.println("Expected value = " + expectedValue);
          out.println("Computed value = " + computedValue);
          passed = false;
        }

      }
    }

    if (passed) {
      out.println(JSFTestUtil.PASS);
    }

  } // END managedBeanPreDestroyTest

  public void managedBeanPostConstructExceptionTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    Application application = getApplication();
    FacesContext facesContext = getFacesContext();

    String beanName = "RuntimeExceptionPostConstruct";
    String expression = "#{RuntimeExceptionPostConstruct}";
    try {
      ValueBinding binding = application.createValueBinding(expression);
      Object val = binding.getValue(facesContext);
      if (val instanceof RuntimeExceptionPostConstructBean) {
        out.println("Bean with runtime exception in method annotated");
        out.println("with @PostConstruct placed into service");
        out.println("Test FAILED");
      }
    } catch (Exception e) {
      out.println(e.getMessage());
      out.println(JSFTestUtil.PASS);
    }
  } // END managedBeanPostConstructExceptionTest

  public void managedBeanPreDestroyExceptionTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    Application application = getApplication();
    FacesContext facesContext = getFacesContext();
    PageContext pageContext = JspFactory.getDefaultFactory().getPageContext(
        this, request, response, null, false, JspWriter.NO_BUFFER, false);

    String beanName = "RuntimeExceptionPreDestroy";
    String expression = "#{RuntimeExceptionPreDestroy}";
    String expectedValue = "RuntimeExceptionPreDestroyBean.onPreDestroy invoked";
    boolean pass = true;

    try {
      ValueBinding binding = application.createValueBinding(expression);
      binding.getValue(facesContext);
      pageContext.removeAttribute(beanName);

      String computedValue = PreDestroyProp.getPreDestroyProperty();
      if (!computedValue.equals(expectedValue)) {
        out.println("Test FAILED:");
        out.println("PreDestroy method not properly invoked");
        out.println("Expected value = " + expectedValue);
        out.println("Computed value = " + computedValue);
        pass = false;
      }

    } catch (Exception e) {
      out.println("Test FAILED");
      out.println(e.getMessage());
      out.println("Bean with runtime exception in method annotated");
      out.println("with @PreDestroy caused exception");
      pass = false;
    }

    if (pass == true) {
      out.println(JSFTestUtil.PASS);
    }

  } // END managedBeanPreDestroyExceptionTest
}
