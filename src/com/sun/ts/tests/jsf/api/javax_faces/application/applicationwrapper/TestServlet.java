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
package com.sun.ts.tests.jsf.api.javax_faces.application.applicationwrapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.el.CompositeELResolver;
import javax.el.ELContext;
import javax.el.ELContextListener;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.ApplicationWrapper;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ResourceHandler;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.convert.BooleanConverter;
import javax.faces.el.MethodBinding;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionListener;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.faces.validator.BeanValidator;
import javax.faces.validator.LengthValidator;
import javax.faces.validator.Validator;
import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspFactory;

import com.sun.ts.tests.jsf.common.beans.TestBean;
import com.sun.ts.tests.jsf.common.behavior.TCKBehavior;
import com.sun.ts.tests.jsf.common.event.TCKSystemEvent;
import com.sun.ts.tests.jsf.common.listener.TCKActionListener;
import com.sun.ts.tests.jsf.common.listener.TCKELContextListener;
import com.sun.ts.tests.jsf.common.listener.TCKSystemEventListener;
import com.sun.ts.tests.jsf.common.navigation.TCKNavigationHandler;
import com.sun.ts.tests.jsf.common.resolver.TCKELResolver;
import com.sun.ts.tests.jsf.common.resourcehandler.TCKResourceHandler;
import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.statemanager.TCKStateManager;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;
import com.sun.ts.tests.jsf.common.viewhandler.TCKViewHandler;

public class TestServlet extends HttpTCKServlet {

  /*
   * private indicators that a test has previously passed. this is necessary if
   * a test is run multiple times without reloading the application
   */
  private ServletContext servletContext;

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    servletContext = config.getServletContext();
  }

  // ApplicationWrapper.addComponent(String, String)
  public void applicationWrapperAddComponentTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper application = new TCKApplication();

    application.addComponent("TCKComponent", "javax.faces.component.UIOutput");

    // should now be able to create the TCKComponent component
    UIOutput output = (UIOutput) application.createComponent("TCKComponent");

    if (output == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unable to create component 'TCKComponent' after "
          + "adding a mapping to the Application instance.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // ApplicationWrapper.createComponent(String)
  public void applicationWrapperCreateComponentTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ApplicationWrapper application = new TCKApplication();

    application.addComponent("TCKComponent", "javax.faces.component.UIOutput");
    UIOutput output = (UIOutput) application.createComponent("TCKComponent");

    if (output == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unable to create component TCKComponent!" + JSFTestUtil.NL
          + "Using Application.createComponent(String)");
    }

    out.println(JSFTestUtil.PASS);

  }

  // ApplicationWrapper.createComponent(FacesContext, String, 'null')
  public void applicationWrapperCreateComponentFSSNullTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    ApplicationWrapper applicationWrapper = new TCKApplication();

    applicationWrapper.addComponent("TCKComponent",
        "javax.faces.component.UIOutput");
    try {
      applicationWrapper.createComponent(context, "TCKComponent", null);
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unable to create component 'TCKComponent'" + JSFTestUtil.NL
          + "Using Application.createComponent(FacesContext, "
          + "String, 'null')");

      out.println(e.toString());
    }

  }

  // Application.createComponent(FacesContext, String, String)
  public void applicationWrapperCreateComponentFSSTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    ApplicationWrapper applicationWrapper = new TCKApplication();

    applicationWrapper.addComponent("TCKComponent",
        "javax.faces.component.UIOutput");
    try {
      applicationWrapper.createComponent(context, "TCKComponent",
          "TCKRenderer");
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + "  Unable to create component "
          + "'TCKComponent'" + JSFTestUtil.NL
          + "Using Application.createComponent(FacesContext, "
          + "String, String)");

      out.println(e.toString());
    }

  }

  // Application.createComponent(ValueBinding, FacesContext, String)
  public void applicationWrapperCreateComponentBindingTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    ApplicationWrapper applicationWrapper = new TCKApplication();
    ELContext elcontext = context.getELContext();

    request.setAttribute("TestBean", new TestBean());
    applicationWrapper.addComponent("TCKComponent",
        "javax.faces.component.UIOutput");

    ExpressionFactory ef = applicationWrapper.getExpressionFactory();
    ValueExpression binding = ef.createValueExpression(elcontext,
        "#{TestBean.component}", UIComponent.class);

    // ensures failure if the value binding lookup fails
    UIInput input = (UIInput) applicationWrapper.createComponent(binding,
        context, "TCKComponent");

    if (input == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.createComponent(ValueBinding, "
          + "FacesContext, String) returned null when using a "
          + "valid valueBinding.");
      return;
    }

    /*
     * Next validate that if the ValueBinding doesn't return a UIComponent
     * instance, then create the component based on the identifier.
     */
    TestBean bean = (TestBean) request.getAttribute("TestBean");
    bean.setComponent(null);

    UIOutput output = (UIOutput) applicationWrapper.createComponent(binding,
        getFacesContext(), "TCKComponent");
    if (output == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.createComponent(ValueBinding, "
          + "FacesContext, String) returned null when falling back to the "
          + "component identifier to create the component.");

      return;
    }

    /*
     * Since the ValueBinding evaluated to null, a new component has been
     * created (see above), the createComponent() implementation in this case
     * must call setValue() with the newly created component instance. We should
     * be able to call getComponent() on the bean to verify.
     */
    UIOutput result = (UIOutput) bean.getComponent();
    if (result == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.createComponent(ValueBinding, "
          + "FacesContext, String) didn't call setValue() on the "
          + "ValueBinding passed when creating a new fallback "
          + "component in the case the ValueBinding returned null."
          + JSFTestUtil.NL + "Expected a return of UIOutput, "
          + "but received a null value.");

      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // Application.getComponentTypes();
  public void applicationWrapperGetComponentTypesTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();

    applicationWrapper.addComponent("TCKComponent",
        "javax.faces.component.UIOutput");
    applicationWrapper.addComponent("TCKBadComponent",
        "javax.faces.component.base.UIOutputBase");

    Iterator<?> i = applicationWrapper.getComponentTypes();
    if (i == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.getComponentTypes() returned null.");
      return;
    }

    // check the iterator for the entries we've added for testing
    if (!(JSFTestUtil.checkIterator(i,
        new String[] { "TCKComponent", "TCKBadComponent" }, false, true))) {

      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + " Unable to find either both or one of the following "
          + "component entires: 'TCKComponent', 'TCKBadComponent' in "
          + "the Iterator returned by Application.getComponentEntries."
          + JSFTestUtil.NL + "Entries returned: "
          + JSFTestUtil.getAsString(applicationWrapper.getComponentTypes()));

      return;
    }

    out.println(JSFTestUtil.PASS);

  }

  // Application.addConverter(String, String)
  // Application.createConverter(String)
  public void applicationWrapperAddCreateConverterByStringTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();

    applicationWrapper.addConverter("TCKConverter",
        "javax.faces.convert.BooleanConverter");
    BooleanConverter converter;

    try {
      converter = (BooleanConverter) applicationWrapper
          .createConverter("TCKConverter");

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Exception thrown when creating converter.");
      e.printStackTrace();
      return;
    }

    if (converter == null) {
      out.println(
          JSFTestUtil.FAIL + "Application.createConverter(String) returned "
              + "null for valid converter added via "
              + "Application.addConverter(String, String).");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // Application.addConverter (Class, String)
  // Application.createConverter(Class)
  public void applicationWrapperAddCreateConverterByClassTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();

    /*
     * Doens't make much sense, but we're only testing the mapping and creation
     * not if the value can be converted
     */
    applicationWrapper.addConverter(Servlet.class,
        "javax.faces.convert.BooleanConverter");

    /*
     * first find see if the implementation reruns the BooleanConverter for this
     * class
     */
    BooleanConverter converter;
    try {
      converter = (BooleanConverter) applicationWrapper
          .createConverter(this.getClass());
      if (converter == null) {
        out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
            + "Application.createConverter(Class) returned null "
            + "when attempting to find a converter via an " + "Interface.");
        return;
      }

      applicationWrapper.addConverter(GenericServlet.class,
          "javax.faces.convert.BooleanConverter");
      converter = (BooleanConverter) applicationWrapper
          .createConverter(this.getClass());

      if (converter == null) {
        out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
            + "Application.createConverter(Class) returned null "
            + "when attempting to find the converter via a " + "super class.");
        return;
      }

      applicationWrapper.addConverter(HttpServlet.class,
          "javax.faces.convert.BooleanConverter");
      converter = (BooleanConverter) applicationWrapper
          .createConverter(this.getClass());

      if (converter == null) {
        out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
            + "Application.createConverter(Class) returned null "
            + "when attempting to find the converter via a super " + "class.");
        return;
      }

      applicationWrapper.addConverter(this.getClass(),
          "javax.faces.convert.BooleanConverter");
      converter = (BooleanConverter) applicationWrapper
          .createConverter(this.getClass());

      if (converter == null) {
        out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
            + "Application.createConverter(Class) returned null "
            + "when class passed was the same that was used for "
            + "the addConverter(Class, String) call.");
        return;
      }
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Exception thrown when creating converter." + JSFTestUtil.NL);
      e.printStackTrace();
    }
  }

  // Application.getConverterIds()
  public void applicationWrapperGetConverterIdsTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();

    applicationWrapper.addConverter("TCKConverter",
        "javax.faces.conver.BooleanConverter");

    Iterator<String> i = applicationWrapper.getConverterIds();
    if (i == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.getConverterIds() returned null.");
      return;
    }

    if (!(JSFTestUtil.checkIterator(i, new String[] { "TCKConverter" }, false,
        true))) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unable to find converter id 'TCKConverter' in returned "
          + "Iterator.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // Application.getConverterTypes()
  public void applicationWrapperGetConverterTypesTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();

    applicationWrapper.addConverter(this.getClass(),
        "javax.faces.convert.BooleanConverter");

    Iterator<Class<?>> i = applicationWrapper.getConverterTypes();
    if (i == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.getConverterIds() returned null.");
      return;
    }

    boolean found = false;
    while (i.hasNext()) {
      Class<?> c = (Class<?>) i.next();
      if (c.equals(this.getClass())) {
        found = true;
        break;
      }
    }

    if (!found) {
      out.println(
          JSFTestUtil.FAIL + JSFTestUtil.NL + "Unable to find converter class '"
              + this.getClass().getName() + "' in " + "returned Iterator.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // Application.addValidator(String, String)
  // Application.createValidator(String)
  public void applicationWrapperAddCreateValidatorTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();

    applicationWrapper.addValidator("TCKValidator",
        "javax.faces.validator.LengthValidator");
    try {
      Validator validator = applicationWrapper.createValidator("TCKValidator");

      if (validator == null) {
        out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
            + "Application.createValidator(String) returned null "
            + "when provided a valid id.");
        return;
      }

      if (!(validator instanceof LengthValidator)) {
        out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
            + "Validator returned was not instance of " + "LengthValidator."
            + JSFTestUtil.NL + "Validator type received: "
            + validator.getClass().getName());

        return;
      }

      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Exception thrown when creating converter." + JSFTestUtil.NL);
      e.printStackTrace();
    }
  }

  // Application.getValidatorIds()
  public void applicationWrapperGetValidatorIdsTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();

    applicationWrapper.addValidator("TCKValidator",
        "javax.faces.validator.LengthValidator");

    Iterator<String> i = applicationWrapper.getValidatorIds();
    if (i == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.getValidatorIds() returned null.");
      return;
    }

    if (!(JSFTestUtil.checkIterator(i, new String[] { "TCKValidator" }, false,
        true))) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unable to find validator id 'TCKValidator' in the "
          + "Iterator returned by Application.getValidatorIds().");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // Application.getActionListener()
  public void applicationWrapperGetActionListenerTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();

    ActionListener defaultListener = applicationWrapper.getActionListener();
    if (defaultListener == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.getActionListener() returned null.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // Application.setActionListener(ActionListener)
  public void applicationWrapperSetActionListenerTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();
    ActionListener origListener = applicationWrapper.getActionListener();
    ActionListener tckActionListener = new TCKActionListener();

    try {
      applicationWrapper.setActionListener(tckActionListener);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Exception thrown when creating converter.");
      e.printStackTrace();
      return;
    }

    ActionListener listener = applicationWrapper.getActionListener();
    if (listener == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.getActionListener() returned null after "
          + "calling Application.setActionListener(ActionListener).");
      return;
    }

    if (!listener.equals(tckActionListener)) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "TCKActionListener not returned by "
          + "Application.getActionListener() as expected." + JSFTestUtil.NL
          + "ActionListener type: " + listener.getClass().getName());
    }

    out.println(JSFTestUtil.PASS);

    // restore original listener
    applicationWrapper.setActionListener(origListener);
  }

  // Application.getNavigationHandler();
  // Application.setNavigationHandler(ViewHandler);
  public void applicationWrapperGetSetNavigationHandlerTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();
    NavigationHandler handler = applicationWrapper.getNavigationHandler();

    if (handler == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.getNavigationHandler() returned null.");
      return;
    }

    NavigationHandler tckNavigationHandler = new TCKNavigationHandler();
    applicationWrapper.setNavigationHandler(tckNavigationHandler);
    NavigationHandler newNavigationHandler = applicationWrapper
        .getNavigationHandler();

    if (newNavigationHandler == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.getNavigationHandler() returned null "
          + "after calling"
          + "Application.setNavigationHandler(NavigationHandler).");
      return;
    }

    if (!newNavigationHandler.equals(tckNavigationHandler)) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.getNavigationHandler() didn't return "
          + "the expected NavigationHandler.");
      return;
    }

    out.println(JSFTestUtil.PASS);

    // restore original handler
    applicationWrapper.setNavigationHandler(handler);
  }

  // Application.createValueBinding(String)
  public void applicationWrapperCreateValueBindingTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();

    request.setAttribute("TestBean", new TestBean());
    ValueBinding binding;

    try {
      binding = applicationWrapper.createValueBinding("#{TestBean.boolProp}");

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.getValueBinding(String) threw an expected "
          + "Exception.");
      e.printStackTrace();
      return;
    }

    if (binding == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.getValueBinding() returned null for a "
          + "valid value reference expression.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // ApplicationWrapper.createValueBinding(String) throws
  // ReferenceSyntaxException
  // when provided an invalid ref
  public void applicationWrapperCreateValueBindingRSETest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper application = new TCKApplication();

    request.setAttribute("TestBean", new TestBean());
    out.println("Testing #{TestBean[}");

    try {
      application.createValueBinding("#{TestBean[}");
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "No Exception thrown when value reference expression "
          + "provided was invalid.");

    } catch (ReferenceSyntaxException rse) {
      out.println(JSFTestUtil.PASS);
    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL
          + ":   Exception thrown when invalid value reference expression was provided"
          + " to Application.getValueBinding(String), but wasn't an instance of ReferenceSyntaxException.");
      e.printStackTrace();
    }
  } // End applicationWrapperCreateValueBindingRSETest

  // ApplicationWrapper.createValidator(String) throws FacesException
  public void applicationWrapperCreateValidatorFETest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application applicationWrapper = new TCKApplication().getWrapped();

    try {
      applicationWrapper.createValidator("Bogus");
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + " Expected a FacesException to be thrown"
          + " when Validator could not be cunstructed.");

    } catch (FacesException fe) {
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(
          JSFTestUtil.FAIL + JSFTestUtil.NL + "Unexpected Exception thrown."
              + JSFTestUtil.NL + "Expected: FacesException" + JSFTestUtil.NL
              + "Recieved: " + e.getClass().getSimpleName());
      e.printStackTrace();
    }

  }

  // Application.getViewHandler()
  // Application.setViewHandler(ViewHandler)
  public void applicationWrapperGetSetViewHandlerTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();
    ViewHandler origHandler = applicationWrapper.getViewHandler();

    if (origHandler == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.getViewHandler() returned null.");
      return;
    }

    ViewHandler tckHandler = new TCKViewHandler();
    applicationWrapper.setViewHandler(tckHandler);
    ViewHandler resolver = applicationWrapper.getViewHandler();

    if (resolver == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.getViewHandler() returned null after "
          + "calling Application.setViewHandler() with a new "
          + "ViewHandler instance.");
      return;
    }

    if (!resolver.equals(tckHandler)) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.getViewHandler didn't return the expected"
          + " ViewHandler.");
      return;
    }

    out.println(JSFTestUtil.PASS);

    // restore the original handler
    applicationWrapper.setViewHandler(origHandler);
  }

  public void applicationWrapperCreateMethodBindingTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();

    TestBean bean = new TestBean();
    request.setAttribute("TestBean", bean);

    MethodBinding binding = applicationWrapper.createMethodBinding(
        "#{requestScope.TestBean.setBoolProp}", new Class[] { Boolean.TYPE });

    if (binding == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.createMethodBinding()" + " returned a null value.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void applicationWrapperSetGetDefaultLocaleTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();

    if (applicationWrapper.getDefaultLocale() != null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Expected Application.getDefaultLocale() "
          + "to return null if the default locale has not "
          + "been explicitly set by a call to "
          + "Application.setDefaultLocale(Locale).");
      return;
    }

    applicationWrapper.setDefaultLocale(Locale.US);

    if (!Locale.US.equals(applicationWrapper.getDefaultLocale())) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Expected Application.getDefaultLocale()"
          + "to return Locale.US as set via call to Application."
          + "setDefaultLocale()." + JSFTestUtil.NL + "Localed received: "
          + applicationWrapper.getDefaultLocale());
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void applicationWrapperSetGetSupportedLocalesTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();

    Collection<Locale> collection = new ArrayList<Locale>();
    collection.add(Locale.ENGLISH);
    collection.add(Locale.FRENCH);
    collection.add(Locale.GERMAN);

    applicationWrapper.setSupportedLocales(collection);

    Locale[] locales = { Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN };
    List<Locale> retLocales = new ArrayList<Locale>();
    for (Iterator<Locale> i = applicationWrapper.getSupportedLocales(); i
        .hasNext();) {
      retLocales.add(i.next());
    }

    Locale[] returnedLocales = (Locale[]) retLocales
        .toArray(new Locale[retLocales.size()]);

    if (!Arrays.equals(locales, returnedLocales)) {
      out.println(
          JSFTestUtil.FAIL + JSFTestUtil.NL + "Unexpected result returned from "
              + "Application.getSupportedLocales()." + JSFTestUtil.NL
              + "Expected: " + JSFTestUtil.getAsString(locales) + JSFTestUtil.NL
              + "Received: " + JSFTestUtil.getAsString(returnedLocales));
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // ApplicationWrapper.getDefaultValidatorInfo()
  public void applicationWrapperGetDefaultValidatorInfoTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();

    if (applicationWrapper.getDefaultValidatorInfo() == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unable to obtain Default validator info.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // -------------------------------------------------- Tests Added for 1.2
  public void applicationWrapperGetELResolverTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();
    ELResolver resolver = applicationWrapper.getELResolver();

    if (resolver == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.getELResolver() returned null.");
      return;
    }

    if (!(resolver instanceof CompositeELResolver)) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.getELResolver() returned "
          + "an ELResolver instance, but it wasn't an instance "
          + "of CompositeELResolver.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void applicationWrapperGetExpressionFactoryTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();

    ExpressionFactory controlFactory = JspFactory.getDefaultFactory()
        .getJspApplicationContext(servletContext).getExpressionFactory();

    if (controlFactory == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unable to obtain ExpressionFactory "
          + "from JspApplicationContext.");
      return;
    }

    ExpressionFactory exprFactory = applicationWrapper.getExpressionFactory();

    if (exprFactory == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unable to obtain ExpressionFactory" + " instance.");
      return;
    }

    // Left in for debugging.
    //
    // ExpressionFactory controlFactoryTwo =
    // JspFactory.getDefaultFactory().
    // getJspApplicationContext(servletContext).getExpressionFactory();
    //
    // ExpressionFactory exprFactoryTwo =
    // applicationWrapper.getExpressionFactory();
    // out.println("*** First Call applicationWrapper.getExpressionFactory() "
    // +
    // "Returns ***: " + exprFactory.toString());
    // out.println("*** Second Call applicationWrapper.getExpressionFactory() "
    // +
    // "Returns ***: " + exprFactoryTwo.toString());
    // out.println("*** First Call - JspFactory.getDefaultFactory()." +
    // "getJspApplicationContext(servletContext)." +
    // "getExpressionFactory() Returns ***: " +
    // controlFactory.toString());
    // out.println("*** Second Call - JspFactory.getDefaultFactory()." +
    // "getJspApplicationContext(servletContext)." +
    // "getExpressionFactory() Returns ***: " +
    // controlFactoryTwo.toString());

    if (!controlFactory.equals(exprFactory)) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Expected Application.getExpressionFactory to return "
          + "the same instance as that returned by"
          + " JspApplicationContext.getExpressionFactory!" + JSFTestUtil.NL
          + "Found: " + controlFactory.toString() + JSFTestUtil.NL
          + "Expected: " + exprFactory.toString());
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void applicationWrapperEvaluationExpressionGetTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();
    String testName = "applicationWrapperEvaluationExpressionGetTest";

    String value = (String) applicationWrapper.evaluateExpressionGet(
        getFacesContext(), "#{param.testname}", java.lang.String.class);

    if (!testName.equals(value)) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unexpected evaluation using "
          + "Application.evaluateExpressionGet()." + JSFTestUtil.NL
          + "Expected: " + testName + JSFTestUtil.NL + "Received: " + value);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // Application.createComponent(ValueExpression, FacesContext, String)
  public void applicationWrapperCreateComponentExpressionTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();

    request.setAttribute("TestBean", new TestBean());
    applicationWrapper.addComponent("TCKComponent",
        "javax.faces.component.UIOutput");
    ELContext elContext = getFacesContext().getELContext();
    ValueExpression expr = applicationWrapper.getExpressionFactory()
        .createValueExpression(elContext, "#{TestBean.component}",
            javax.faces.component.UIComponent.class);

    if (expr == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unable to obtain ValueExpression " + "instance from Application.");
      return;
    }

    // ensures failure if the ValueExpression lookup fails
    UIInput input = (UIInput) applicationWrapper.createComponent(expr,
        getFacesContext(), "TCKComponent");
    if (input == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.createComponent(ValueExpression,"
          + " FacesContext, String) returned null when using "
          + "a valid ValueExpression.");
      return;
    }

    /*
     * next validate that if the ValueExpression doesn't return a UIComponent
     * instance, then create the component based on the identifier.
     */
    TestBean bean = (TestBean) request.getAttribute("TestBean");
    bean.setComponent(null);

    UIOutput output = (UIOutput) applicationWrapper.createComponent(expr,
        getFacesContext(), "TCKComponent");
    if (output == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.createComponent(ValueExpression,"
          + " FacesContext, String, String) returned "
          + "null when falling back to the component "
          + "identifier to create the component.");
      return;
    }

    /*
     * Since the ValueExpression evaluated to null, a new component has been
     * created (see above), the createComponent() implementation in this case
     * must call setValue() with the newly created component instance. We should
     * be able to call getComponent() on the bean to verify.
     */
    UIOutput result = (UIOutput) bean.getComponent();
    if (result == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.createComponent(ValueExpression, "
          + "FacesContext, String) didn't call setValue() "
          + "on the ValueExpression passed when creating "
          + "a new fallback component in the case the "
          + "ValueExpression returned null." + JSFTestUtil.NL
          + "Expected a return of UIOutput, " + "but received a null value.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // Application.createComponent(ValueExpression, FacesContext, String,
  // String)
  public void applicationWrapperCreateComponentExpressionFSSTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();

    request.setAttribute("TestBean", new TestBean());
    applicationWrapper.addComponent("TCKComponent",
        "javax.faces.component.UIOutput");
    ELContext elContext = getFacesContext().getELContext();
    ValueExpression expr = applicationWrapper.getExpressionFactory()
        .createValueExpression(elContext, "#{TestBean.component}",
            javax.faces.component.UIComponent.class);

    if (expr == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unable to obtain ValueExpression instance from " + "Application.");
      return;
    }

    // ensures failure if the ValueExpression lookup fails
    UIInput input = (UIInput) applicationWrapper.createComponent(expr,
        getFacesContext(), "TCKComponent", "TCKRenderer");
    if (input == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.createComponent(ValueExpression,"
          + " FacesContext, String) returned null when using "
          + "a valid ValueExpression.");
      return;
    }

    /*
     * next validate that if the ValueExpression doesn't return a UIComponent
     * instance, then create the component based on the identifier.
     */
    TestBean bean = (TestBean) request.getAttribute("TestBean");
    bean.setComponent(null);

    UIOutput output = (UIOutput) applicationWrapper.createComponent(expr,
        getFacesContext(), "TCKComponent");
    if (output == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.createComponent(ValueExpression,"
          + " FacesContext, String, 'null) returned "
          + "null when falling back to the component "
          + "identifier to create the component.");
      return;
    }

    /*
     * Since the ValueExpression evaluated to null, a new component has been
     * created (see above), the createComponent() implementation in this case
     * must call setValue() with the newly created component instance. We should
     * be able to call getComponent() on the bean to verify.
     */
    UIOutput result = (UIOutput) bean.getComponent();
    if (result == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.createComponent(ValueExpression, "
          + "FacesContext, String) didn't call setValue() "
          + "on the ValueExpression passed when creating "
          + "a new fallback component in the case the "
          + "ValueExpression returned null." + JSFTestUtil.NL
          + "Expected a return of UIOutput, but received a " + "null value.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // Application.createComponent(ValueExpression, FacesContext, String,
  // 'null')
  public void applicationWrapperCreateComponentExpressionFSSNullTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();

    request.setAttribute("TestBean", new TestBean());
    applicationWrapper.addComponent("TCKComponent",
        "javax.faces.component.UIOutput");
    ELContext elContext = getFacesContext().getELContext();
    ValueExpression expr = applicationWrapper.getExpressionFactory()
        .createValueExpression(elContext, "#{TestBean.component}",
            javax.faces.component.UIComponent.class);

    if (expr == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unable to obtain ValueExpression instance " + "from Application.");
      return;
    }

    // ensures failure if the ValueExpression lookup fails
    UIInput input = (UIInput) applicationWrapper.createComponent(expr,
        getFacesContext(), "TCKComponent", null);
    if (input != null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.createComponent(ValueExpression,"
          + " FacesContext, String) returned null when using "
          + "a valid ValueExpression.");
      return;
    }

    /*
     * next validate that if the ValueExpression doesn't return a UIComponent
     * instance, then create the component based on the identifier.
     */
    TestBean bean = (TestBean) request.getAttribute("TestBean");
    bean.setComponent(null);

    UIOutput output = (UIOutput) applicationWrapper.createComponent(expr,
        getFacesContext(), "TCKComponent");
    if (output == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.createComponent(ValueExpression,"
          + " FacesContext, String) returned null when "
          + "falling back to the component "
          + "identifier to create the component.");
      return;
    }

    /*
     * Since the ValueExpression evaluated to null, a new component has been
     * created (see above), the createComponent() implementation in this case
     * must call setValue() with the newly created component instance. We should
     * be able to call getComponent() on the bean to verify.
     */
    UIOutput result = (UIOutput) bean.getComponent();
    if (result == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.createComponent(ValueExpression, "
          + "FacesContext, String) didn't call setValue() "
          + "on the ValueExpression passed when creating "
          + "a new fallback component in the case the "
          + "ValueExpression returned null." + JSFTestUtil.NL
          + "Expected a return of UIOutput, but received a " + "null value.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void applicationWrapperAddELResolverTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();

    try {
      applicationWrapper.addELResolver(new TCKELResolver());

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + "  Unexpected Exception...");
      e.printStackTrace();
    }

    out.println(JSFTestUtil.PASS);

  }

  public void applicationWrapperAddGetRemoveELContextListenerTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();
    int contextCount = applicationWrapper.getELContextListeners().length;
    TCKELContextListener listener = new TCKELContextListener();
    applicationWrapper.addELContextListener(listener);

    if ((contextCount + 1) != applicationWrapper
        .getELContextListeners().length) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Called Application.addELContextListener()"
          + "but the length of the array returned by "
          + "Application.getELContextListeners() didn't increase " + "by one."
          + JSFTestUtil.NL + "Expected length: " + (contextCount + 1)
          + JSFTestUtil.NL + "Actual length: "
          + applicationWrapper.getELContextListeners().length);
      return;
    }

    applicationWrapper.removeELContextListener(listener);
    List<ELContextListener> listeners = Arrays
        .asList(applicationWrapper.getELContextListeners());

    if (listeners.contains(listener)) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "ELContextListener not removed as expected.");
      return;
    }

    out.println(JSFTestUtil.PASS);

  }

  // Test for Application.getResourceHandler()
  public void applicationWrapperGetResourceHandlerTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();

    // Set a specific ResourceHandler.
    TCKResourceHandler myRH = new TCKResourceHandler();
    applicationWrapper.setResourceHandler(myRH);

    if (applicationWrapper.getResourceHandler() == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unable to obtain ResourceHandler " + "instance.");
      return;
    }

    if (!myRH.equals(applicationWrapper.getResourceHandler())) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "ResourceHandler was not able to be set!");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void applicationWrapperGetResourceBundleTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    ApplicationWrapper applicationWrapper = new TCKApplication();

    try {
      String expectedName = "com.sun.ts.tests.jsf.common."
          + "resourcebundle.SimpleResourceBundle_en";

      getFacesContext().getViewRoot().setLocale(Locale.ENGLISH);

      ResourceBundle rb = applicationWrapper.getResourceBundle(context,
          "simpleRB");

      String resultName = rb.getClass().getName();

      if (!resultName.equals(expectedName)) {
        out.println("Test FAILED Unexpected Result for "
            + "getResourceBundle()." + JSFTestUtil.NL + "Expected: "
            + expectedName + JSFTestUtil.NL + "Received: " + resultName);
        return;
      }

      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + "  Unexpected Exception Thrown!");
      e.printStackTrace();
    }

  }

  public void applicationWrapperGetSetMessageBundleTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();
    String bundleName;
    String myBundle;

    bundleName = "com.sun.ts.tests.jsf.common.resourcebundle."
        + "SimpleResourceBundle";

    try {
      applicationWrapper.setMessageBundle(bundleName);
      myBundle = applicationWrapper.getMessageBundle();

      if (!myBundle.equals(bundleName)) {
        out.println("Test FAILED Unexpected Result for "
            + "get/setMessageBundle()." + JSFTestUtil.NL + "Expected: "
            + bundleName + JSFTestUtil.NL + "Received: " + myBundle);
        return;
      }

      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + "  Unexpected Exception Thrown!");
      e.printStackTrace();
    }

  }

  // Test for ApplicationWrapper.setDefaultRenderKitId()
  // ApplicationWrapper.getDefaultRenderKitId()
  public void applicationWrapperGetSetDefaultRenderKitIDTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();

    try {
      applicationWrapper.setDefaultRenderKitId("TCKRenderKit");

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unexpected Exception Thrown when trying to set default "
          + "RenderKit ID!" + JSFTestUtil.NL
          + "application.setDefaultRenderKitId('TCKRenderKit')");
      e.printStackTrace();
    }

    try {
      String result = applicationWrapper.getDefaultRenderKitId();

      if (!"TCKRenderKit".equals(result)) {
        out.println("Test FAILED Unexpected RenderKit ID returned "
            + "from application.getDefaultRenderKitId()." + JSFTestUtil.NL
            + "Expected: TCKRenderKit" + JSFTestUtil.NL + "Received: "
            + result);
        return;
      }

      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + "  Unexpected Exception Thrown!");
      e.printStackTrace();
    }

  }

  // Test for Application.setResourceHandler()
  public void applicationWrapperSetResourceHandlerTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();

    ResourceHandler rh = applicationWrapper.getResourceHandler();
    applicationWrapper.setResourceHandler(rh);

    if (!rh.equals(applicationWrapper.getResourceHandler())) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unable to obtain ResourceHandler " + "instance.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // Test for ApplicationWrapper.setResourceHandler() throws
  // NullPointerException
  public void applicationWrapperSetResourceHandlerNPETest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();

    JSFTestUtil.checkForNPE(applicationWrapper, "setResourceHandler",
        new Class<?>[] { ResourceHandler.class }, new Object[] { null }, out);

  } // End applicationWrapperSetResourceHandlerNPETest

  // Test for Application.getProjectStage() "Default settings"
  public void applicationWrapperGetProjectStageTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();

    String stage = applicationWrapper.getProjectStage().toString();

    if (!("production".equalsIgnoreCase(stage))) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL);
      out.println("Expected: production");
      out.println("Received" + stage);
      return;
    }

    out.println(JSFTestUtil.PASS);

  }

  // Test for Application.publishEvent
  public void applicationWrapperPublishEventTest1(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();

    UIComponent uic = getApplication().createComponent(UIOutput.COMPONENT_TYPE);
    SystemEvent se = new TCKSystemEvent(uic);

    // Application.publishEvent(FacesContext, class, object)
    try {
      applicationWrapper.publishEvent(getFacesContext(), se.getClass(), uic);
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL);
      e.printStackTrace();
    }

  } // End applicationWrapperPublishEventTest1

  public void applicationWrapperPublishEventTest2(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();

    UIComponent uic = getApplication().createComponent(UIOutput.COMPONENT_TYPE);
    SystemEvent se = new TCKSystemEvent(uic);

    // Application.publishEvent(getFacesContext, class, class, source)
    try {
      applicationWrapper.publishEvent(getFacesContext(), se.getClass(),
          uic.getClass(), uic);
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL);
      e.printStackTrace();
    }

  } // End applicationWrapperPublishEventTest2

  // Test for Application.subscribeToEvent
  public void applicationWrapperSubscribeToEventTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();

    UIComponent uic = getApplication().createComponent(UIOutput.COMPONENT_TYPE);
    SystemEvent se = new TCKSystemEvent(uic);
    SystemEventListener sel = new TCKSystemEventListener();

    try {
      applicationWrapper.subscribeToEvent(se.getClass(), uic.getClass(), sel);
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL);
      e.printStackTrace();
    }

  }// End applicationWrapperSubscribeToEventTest

  // Test for Application.subscribeToEvent
  public void applicationWrapperSubscribeToEventNullTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();

    UIComponent uic = getApplication().createComponent(UIOutput.COMPONENT_TYPE);
    SystemEvent se = new TCKSystemEvent(uic);
    SystemEventListener sel = new TCKSystemEventListener();

    try {
      applicationWrapper.subscribeToEvent(se.getClass(), null, sel);
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL);
      e.printStackTrace();
    }

  }// End applicationWrapperSubscribeToEventNullTest

  // Test for Application.subscribeToEvent
  public void applicationWrapperSubscribeToEventNoSrcClassTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();

    UIComponent uic = getApplication().createComponent(UIOutput.COMPONENT_TYPE);
    SystemEvent se = new TCKSystemEvent(uic);
    SystemEventListener sel = new TCKSystemEventListener();

    try {
      applicationWrapper.subscribeToEvent(se.getClass(), sel);
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL);
      e.printStackTrace();
    }

  }// End applicationWrapperSubscribeToEventNoSrcClassTest

  // Test for Application.unsubscribeFromEvent
  public void applicationWrapperUnsubscribeFromEventTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();

    UIComponent uic = getApplication().createComponent(UIOutput.COMPONENT_TYPE);
    SystemEvent se = new TCKSystemEvent(uic);
    SystemEventListener sel = new TCKSystemEventListener();

    try {
      applicationWrapper.unsubscribeFromEvent(se.getClass(), uic.getClass(),
          sel);
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL);
      e.printStackTrace();
    }

  }

  // Test for Application.unsubscribeFromEvent
  public void applicationWrapperUnsubscribeFromEventNoSrcClassTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();

    UIComponent uic = getApplication().createComponent(UIOutput.COMPONENT_TYPE);
    SystemEvent se = new TCKSystemEvent(uic);
    SystemEventListener sel = new TCKSystemEventListener();

    try {
      applicationWrapper.unsubscribeFromEvent(se.getClass(), sel);
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL);
      e.printStackTrace();
    }

  }

  // Test for Application.addBehavior
  public void applicationWrapperAddBehaviorTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();
    String mb = "myBehavior";

    applicationWrapper.addBehavior(mb,
        (new TCKBehavior().getClass().getName()));

    Iterator<String> i = applicationWrapper.getBehaviorIds();

    while (i.hasNext()) {
      if (mb.equals(i.next())) {
        out.println(JSFTestUtil.PASS);
        return;
      }
    }

    out.println(JSFTestUtil.FAIL);

  }// End applicationWrapperAddBehaviorTest

  // Test for Application.createBehavior
  public void applicationWrapperCreateBehaviorTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();
    String mb = "myBehavior";

    applicationWrapper.addBehavior(mb,
        (new TCKBehavior().getClass().getName()));

    TCKBehavior firstBehavior = (TCKBehavior) applicationWrapper
        .createBehavior(mb);

    // Test to make sure we get back the default name.
    if (!("default_name".equals(firstBehavior.getName()))) {
      out.println(
          JSFTestUtil.FAIL + JSFTestUtil.NL + "Unexpected value returned."
              + JSFTestUtil.NL + "Expected: default_name" + JSFTestUtil.NL
              + "Received: " + firstBehavior.getName());
      return;
    }

    firstBehavior.setName("changed_name");
    TCKBehavior secondBehavior = (TCKBehavior) applicationWrapper
        .createBehavior(mb);

    // Test to make sure we get back the default name again.
    if (!("default_name".equals(secondBehavior.getName()))) {
      out.println(
          JSFTestUtil.FAIL + JSFTestUtil.NL + "Unexpected value returned."
              + JSFTestUtil.NL + "Expected: default_name" + JSFTestUtil.NL
              + "Received: " + secondBehavior.getName());
      return;
    }

    out.println(JSFTestUtil.PASS);

  }// End applicationWrapperCreateBehaviorTest

  // Test for ApplicationWrapper.createBehavior() throws FacesException
  public void applicationWrapperCreateBehaviorFETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper application = new TCKApplication();

    try {
      application.addBehavior("bad", "com.sun.ts.tests.jsf.api."
          + "javax_faces.application.application.BadBehavior");

      application.createBehavior("bad");

      // Exception not thrown!
      out.println(
          JSFTestUtil.FAIL + " expected a FacesException to be " + "thrown.");

    } catch (FacesException fe) {
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unexpected Exception thrown when Behavior "
          + "could not be created!" + JSFTestUtil.NL
          + "Expected: FacesException" + JSFTestUtil.NL + "Receieved: "
          + JSFTestUtil.NL + e.toString());
    }

  }// End applicationCreateBehaviorFETest

  // Test for Application.addDefaultValidatorId
  public void applicationWrapperAddDefaultValidatorIdTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();
    String mv = "myValidator";

    applicationWrapper.addValidator(mv, BeanValidator.class.getName());
    applicationWrapper.addDefaultValidatorId(mv);

    Map<String, String> dvMap = applicationWrapper.getDefaultValidatorInfo();
    Iterator<String> keys = dvMap.keySet().iterator();

    while (keys.hasNext()) {
      if (mv.equals(keys.next())) {
        out.println(JSFTestUtil.PASS);
        return;
      }
    }

    out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
        + "Call to addDefaultValidatorId did not register a " + "Validator!");

  }// End applicationWrapperAddDefaultValidatorIdTest

  // Test for Application.getBehaviorIds
  public void applicationWrapperGetBehaviorIdsTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();

    if (!(applicationWrapper.getBehaviorIds() instanceof Iterator)) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Expected 'Application.geBehaviorIds()' "
          + "to return an Iterator!");
      return;
    }

    out.println(JSFTestUtil.PASS);

  }// End applicationWrapperGetBehaviorIdsTest

  // Test for ApplicationWrapper.setStatemanager()
  // ApplicationWrapper.getStatemanager()
  public void applicationWrapperGetSetStateManagerTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();

    // Test to make sure we get a StateManger by default.
    try {
      applicationWrapper.getStateManager();

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unexpected Exception thrown when calling "
          + "Application.getStateManager" + JSFTestUtil.NL);
      e.printStackTrace();
    }

    try {
      StateManager tckstatemgr = new TCKStateManager();
      applicationWrapper.setStateManager(tckstatemgr);
      String result = applicationWrapper.getStateManager().getClass()
          .getSimpleName();

      if (!"TCKStateManager".equals(result)) {
        out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
            + "Unexpected value returned after setting StateManger"
            + JSFTestUtil.NL + "Expected: TCKStateManager" + JSFTestUtil.NL
            + "Received: " + result);
        return;
      }

      out.println("Test PASSED.");

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unexpected Exception thrown when calling "
          + "Application.getStateManager" + JSFTestUtil.NL);
      e.printStackTrace();
    }

  }

  // Test for ApplicationWrapper.setStatemanager() throws NullPointerException
  public void applicationWrapperSetStateManagerNPETest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();

    JSFTestUtil.checkForNPE(applicationWrapper, "setStateManager",
        new Class<?>[] { StateManager.class }, new Object[] { null }, out);

  }

  // Test for ApplicationWrapper.setViewHandler() throws NullPointerException
  public void applicationWrapperSetViewHandlerNPETest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper applicationWrapper = new TCKApplication();

    JSFTestUtil.checkForNPE(applicationWrapper, "setViewHandler",
        new Class<?>[] { ViewHandler.class }, new Object[] { null }, out);
  }

  // Application.createComponent(String) throws FacesException
  public void applicationWrapperCreateComponentFETest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper application = new TCKApplication();

    application.addComponent("TCKBadComponent",
        "javax.faces.component.base.UIOutputBase");
    try {
      // should fail as UIOutput is an interface
      application.createComponent("TCKBadComponent");
      out.println(JSFTestUtil.FAIL + "  No Exception thrown when calling"
          + "ApplicationWrapper.createComponent shouldn't have"
          + "been able to create the specified component.");

    } catch (FacesException fe) {
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Exception thrown, but was not an instance of "
          + "FacesException.");
      e.printStackTrace();
    }

  }

  // ApplicationWrapper.createComponent(ValueExpression, FacesContext, String)
  // throws FacesException
  public void applicationWrapperCreateComponentExpressionFETest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationWrapper application = new TCKApplication();

    request.setAttribute("TestBean", new TestBean());
    application.addComponent("TCKBadComponent",
        "javax.faces.component.base.UIOutputBase");
    try {
      ELContext elContext = getFacesContext().getELContext();
      ValueExpression expr = application.getExpressionFactory()
          .createValueExpression(elContext, "#{TestBean.boolProp}",
              javax.faces.component.UIComponent.class);

      if (expr == null) {
        out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
            + "Unable to obtain ValueExpression from Application.");
        return;
      }

      // should fail as UIOutput is an interface
      application.createComponent(expr, getFacesContext(), "TCKBadComponent");
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "FacesException 'NOT' thrown when Application.createComponent("
          + "ValueExpression, FacesContext, String) should not have been able to create the specified component.");
      return;

    } catch (FacesException fe) {
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + " Exception thrown, but was not an instance of " + "FacesException."
          + JSFTestUtil.NL);
      e.printStackTrace();
    }

  }

  // ------------------------------------------------- Private Implementations

  private static class TCKApplication extends ApplicationWrapper {
    FacesContext context;

    @Override
    public Application getWrapped() {
      context = FacesContext.getCurrentInstance();
      return context.getApplication();
    }

  }

}
