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
package com.sun.ts.tests.jsf.api.javax_faces.application.application;

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
import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ProjectStage;
import javax.faces.application.Resource;
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
import java.util.Set;
import javax.faces.FactoryFinder;
import javax.faces.component.ContextCallback;
import javax.faces.component.search.SearchExpressionContext;
import javax.faces.component.search.SearchExpressionContextFactory;
import javax.faces.component.search.SearchExpressionHandler;
import javax.faces.component.search.SearchExpressionHint;
import javax.faces.component.search.SearchKeywordContext;
import javax.faces.component.search.SearchKeywordResolver;
import javax.faces.component.visit.VisitHint;

public class TestServlet extends HttpTCKServlet {

  private static final String RESOURCE_NAME = "compOne.xhtml";

  /*
   * private indicators that a test has previously passed. this is necessary if
   * a test is run multiple times without reloading the application
   */
  private boolean setGetLocaleTestPassed = false;

  private ServletContext servletContext;

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    servletContext = config.getServletContext();
  }

  // Test for Application.addComponent(String, String)
  public void applicationAddComponentTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    application.addComponent("TCKComponent", "javax.faces.component.UIOutput");

    // should now be able to create the TCKComponent component
    UIOutput output = (UIOutput) application.createComponent("TCKComponent");

    if (output == null) {
      out.println(JSFTestUtil.FAIL + ": Unable to create component "
          + "'TCKComponent' after adding a mapping to "
          + "the Application instance.");
      return;
    }

    out.println(JSFTestUtil.PASS);

  }

  // Application.addComponent(String, String) throws NPE if either arg is null
  public void applicationAddComponentNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    JSFTestUtil.checkForNPE(application, "addComponent",
        new Class<?>[] { String.class, String.class },
        new Object[] { null, "javax.faces.component.UIOutput" }, out);

    JSFTestUtil.checkForNPE(application, "addComponent",
        new Class<?>[] { String.class, String.class },
        new Object[] { "TCKComponent2", null }, out);

  }

  // Application.createComponent(String)
  public void applicationCreateComponentTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    application.addComponent("TCKComponent", "javax.faces.component.UIOutput");
    UIOutput output = (UIOutput) application.createComponent("TCKComponent");

    if (output == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unable to create component 'TCKComponent'"
          + "Using Application.createComponent(String)");
      return;
    }
    out.println(JSFTestUtil.PASS);

  }

  // Application.createComponent(FacesContext, String, 'null')
  public void applicationCreateComponentFSSNullTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    application.addComponent("TCKComponent", "javax.faces.component.UIOutput");
    try {
      application.createComponent(context, "TCKComponent", null);
      out.println(JSFTestUtil.PASS);
    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL
          + ": Unable to create component 'TCKComponent'!" + JSFTestUtil.NL
          + "Using Application.createComponent(FacesContext, "
          + "String, 'null')");
      out.println(e.toString());
    }

  }

  // Application.createComponent(FacesContext, String, String)
  public void applicationCreateComponentFSSTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    application.addComponent("TCKComponent", "javax.faces.component.UIOutput");
    try {
      application.createComponent(context, "TCKComponent", "TCKRenderer");
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + ": Unable to create component "
          + "'TCKComponent'" + JSFTestUtil.NL
          + "Using Application.createComponent(FacesContext, "
          + "String, String)");
      out.println(e.toString());
    }

  }

  public void applicationCreateComponentFRNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    ResourceHandler handler = getFacesContext().getApplication()
        .getResourceHandler();
    Resource resource = handler.createResource(RESOURCE_NAME);

    // Application.createComponent(null, Resource)
    JSFTestUtil.checkForNPE(application, "createComponent",
        new Class<?>[] { FacesContext.class, Resource.class },
        new Object[] { null, resource }, out);

    // Application.createComponent(FacesContext, null)
    JSFTestUtil.checkForNPE(application, "createComponent",
        new Class<?>[] { FacesContext.class, Resource.class },
        new Object[] { getFacesContext(), null }, out);

  }// End applicationCreateComponentFRNPETest

  public void applicationCreateComponentFSSNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    application.addComponent("TCKComponent", "javax.faces.component.UIOutput");

    // createComponent(FacesContext, null, String)
    JSFTestUtil.checkForNPE(application, "createComponent",
        new Class<?>[] { FacesContext.class, String.class, String.class },
        new Object[] { context, null, null }, out);

    // createComponent(null, String, String)
    JSFTestUtil.checkForNPE(application, "createComponent",
        new Class<?>[] { FacesContext.class, String.class, String.class },
        new Object[] { null, "TCKComponent", null }, out);

  }// End applicationCreateComponentFSSNPETest

  public void applicationCreateComponentFETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    FacesContext fc = getFacesContext();
    Application application = fc.getApplication();
    ELContext elContext = fc.getELContext();

    TestBean bean = new TestBean();
    bean.setComponent(null);
    request.setAttribute("TestBean", bean);

    ValueExpression expr = application.getExpressionFactory()
        .createValueExpression(elContext, "#{TestBean.component}",
            javax.faces.component.UIComponent.class);

    application.addComponent("TCKBadComponent",
        "javax.faces.component.base.UIOutputBase");

    // createComponent(String)
    JSFTestUtil.checkForFE(application, "createComponent",
        new Class<?>[] { String.class }, new Object[] { "TCKBadComponent" },
        pw);

    // createComponent(ValueExpression, FacesContext, String)
    JSFTestUtil.checkForFE(
        application, "createComponent", new Class<?>[] { ValueExpression.class,
            FacesContext.class, String.class },
        new Object[] { expr, fc, "TCKBadComponent" }, pw);

  }

  public void applicationCreateComponentResNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    // createComponent(FacesContext, null)
    JSFTestUtil.checkForNPE(application, "createComponent",
        new Class<?>[] { FacesContext.class, Resource.class },
        new Object[] { getFacesContext(), null }, out);

    ResourceHandler handler = getFacesContext().getApplication()
        .getResourceHandler();
    Resource resource = handler.createResource(RESOURCE_NAME);

    // createComponent(null, Resource)
    JSFTestUtil.checkForNPE(application, "createComponent",
        new Class<?>[] { FacesContext.class, Resource.class },
        new Object[] { null, resource }, out);

  }// End applicationCreateComponentResNPETest

  public void applicationCreateComponentNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }
    // createComponent(String)
    JSFTestUtil.checkForNPE(application, "createComponent",
        new Class<?>[] { String.class }, new Object[] { null }, out);

  }// End applicationCreateComponentNPETest

  // Application.createComponent(ValueBinding, FacesContext, String)
  public void applicationCreateComponentBindingTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    Application application = getApplication();
    ELContext elcontext = context.getELContext();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    request.setAttribute("TestBean", new TestBean());
    application.addComponent("TCKComponent", "javax.faces.component.UIOutput");

    ExpressionFactory ef = application.getExpressionFactory();
    ValueExpression binding = ef.createValueExpression(elcontext,
        "#{TestBean.component}", UIComponent.class);

    // ensures failure if the value binding lookup fails
    UIInput input = (UIInput) application.createComponent(binding, context,
        "TCKComponent");

    if (input == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.createComponent(ValueBinding,"
          + " FacesContext, String) returned null when using a "
          + "valid valueBinding.");
      return;
    }

    /*
     * next validate that if the ValueBinding doesn't return a UIComponent
     * instance, then create the component based on the identifier.
     */
    TestBean bean = (TestBean) request.getAttribute("TestBean");
    bean.setComponent(null);

    UIOutput output = (UIOutput) application.createComponent(binding,
        getFacesContext(), "TCKComponent");
    if (output == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.createComponent(ValueBinding,"
          + " FacesContext, String) returned null when falling back to the component "
          + "identifier to create the component.");
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
          + "Application.createComponent(ValueBinding,"
          + " FacesContext, String) didn't call setValue() "
          + "on the ValueBinding passed when creating a new "
          + "fallback component in the case the ValueBinding "
          + "returned null." + JSFTestUtil.NL
          + "Expected a return of UIOutput, but received a " + "null value.");
      return;
    } else {
      out.println(JSFTestUtil.PASS);
    }

  }

  // Application.getComponentTypes();
  public void applicationGetComponentTypesTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    application.addComponent("TCKComponent", "javax.faces.component.UIOutput");
    application.addComponent("TCKBadComponent",
        "javax.faces.component.base.UIOutputBase");

    Iterator<String> i = application.getComponentTypes();

    if (i == null) {
      out.println(JSFTestUtil.FAIL
          + ": Application.getComponentTypes() returned null.");
      return;
    }

    // check the iterator for the entries we've added for testing
    if (JSFTestUtil.checkIterator(i,
        new String[] { "TCKComponent", "TCKBadComponent" }, false, true)) {
      out.println(JSFTestUtil.PASS);

    } else {
      out.println(JSFTestUtil.FAIL
          + ": Unable to find either both or one of the following "
          + "component entires:'TCKComponent', 'TCKBadComponent' in "
          + "the Iterator returned by Application.getComponentEntries."
          + JSFTestUtil.NL + "Entries returned: "
          + JSFTestUtil.getAsString(application.getComponentTypes()));
    }
  }

  // Application.addConverter(String, String)
  // Application.createConverter(String)
  public void applicationAddCreateConverterByStringTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    application.addConverter("TCKConverter",
        "javax.faces.convert.BooleanConverter");
    BooleanConverter converter;
    try {
      converter = (BooleanConverter) application
          .createConverter("TCKConverter");

    } catch (Exception e) {
      out.println(
          JSFTestUtil.FAIL + ": Exception thrown when creating converter.");
      e.printStackTrace();
      return;
    }

    if (converter == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.createConverter(String) returned null for"
          + " valid converter added via "
          + "Application.addConverter(String, String).");
      return;
    }
    out.println(JSFTestUtil.PASS);

  }

  // Application.addConverter(null, String)
  // Application.addConverter(String, null)
  public void applicationAddCreateConverterByStringNPETest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      pw.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    // Application.addConverter(null, String)
    JSFTestUtil.checkForNPE(application, "addConverter",
        new Class<?>[] { String.class, String.class },
        new Object[] { null, "javax.faces.convert.BooleanConverter" }, pw);

    // Application.addConverter(String, null)
    JSFTestUtil.checkForNPE(application, "addConverter",
        new Class<?>[] { String.class, String.class },
        new Object[] { "TCKConverter", null }, pw);

  }

  // Application.addConverter (Class, String)
  // Application.createConverter(Class)
  public void applicationAddCreateConverterByClassTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    // doens't make much sense, but we're only testing the mapping and
    // creation
    // not if the value can be converted

    application.addConverter(Servlet.class,
        "javax.faces.convert.BooleanConverter");

    // first find see if the implementation reruns the BooleanConverter for
    // this class
    BooleanConverter converter;
    try {
      converter = (BooleanConverter) application
          .createConverter(this.getClass());
      if (converter == null) {
        out.println(JSFTestUtil.FAIL
            + ": Application.createConverter(Class) returned null when attempting to find"
            + " a converter via an Interface[1].");
        return;
      }

      application.addConverter(GenericServlet.class,
          "javax.faces.convert.BooleanConverter");
      converter = (BooleanConverter) application
          .createConverter(this.getClass());
      if (converter == null) {
        out.println(JSFTestUtil.FAIL
            + ": Application.createConverter(Class) returned null when attempting to find"
            + " the converter via a super class[1].");
        return;
      }

      application.addConverter(HttpServlet.class,
          "javax.faces.convert.BooleanConverter");
      converter = (BooleanConverter) application
          .createConverter(this.getClass());
      if (converter == null) {
        out.println(JSFTestUtil.FAIL
            + ": Application.createConverter(Class) returned null when attempting to find"
            + " the converter via a super class[2].");
        return;
      }

      application.addConverter(this.getClass(),
          "javax.faces.convert.BooleanConverter");
      converter = (BooleanConverter) application
          .createConverter(this.getClass());

      if (converter == null) {
        out.println(JSFTestUtil.FAIL
            + ": Application.createConverter(Class) returned null when "
            + " class passed was the same that was used for the addConverter(Class, String) call.");
        return;
      }
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(
          JSFTestUtil.FAIL + ": Exception thrown when creating converter.");
      e.printStackTrace();
    }
  }

  public void applicationAddCreateConverterByClassNPETest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      pw.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    // Application.addConverter(null, String)
    JSFTestUtil.checkForNPE(application, "addConverter",
        new Class<?>[] { Class.class, String.class },
        new Object[] { null, "javax.faces.convert.BooleanConverter" }, pw);

    // Application.addConverter(Class, null)
    JSFTestUtil.checkForNPE(application, "addConverter",
        new Class<?>[] { Class.class, String.class },
        new Object[] { Servlet.class, null }, pw);
  }

  // Application.createConverter(String) throws NPE for null argument
  public void applicationCreateConverterByStringNPETest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    JSFTestUtil.checkForNPE(application, "createConverter",
        new Class<?>[] { String.class }, new Object[] { null }, out);

  }// End applicationCreateConverterByStringNPETest

  // Application.createConverter(Class) throws NPE for null argument
  public void applicationCreateConverterByClassNPETest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    JSFTestUtil.checkForNPE(application, "createConverter",
        new Class<?>[] { Class.class }, new Object[] { (Class<?>) null }, out);

  }// End applicationCreateConverterByClassNPETest

  public void applicationCreateConverterFETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      pw.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    application.addConverter(Boolean.class,
        "javax.faces.convert.Converter.class");

    // createConverter(Class)
    JSFTestUtil.checkForFE(application, "createConverter",
        new Class<?>[] { Class.class }, new Object[] { Boolean.class }, pw);

    application.addConverter("boogus", "javax.faces.convert.Converter.class");

    // createConverter(String)
    JSFTestUtil.checkForFE(application, "createConverter",
        new Class<?>[] { String.class }, new Object[] { "boogus" }, pw);

  }

  // Application.getConverterIds()
  public void applicationGetConverterIdsTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    application.addConverter("TCKConverter",
        "javax.faces.conver.BooleanConverter");

    Iterator<String> i = application.getConverterIds();
    if (i == null) {
      out.println(
          JSFTestUtil.FAIL + ": Application.getConverterIds() returned null.");
      return;
    }

    if (JSFTestUtil.checkIterator(i, new String[] { "TCKConverter" }, false,
        true)) {
      out.println(JSFTestUtil.PASS);
    } else {
      out.println(JSFTestUtil.FAIL
          + ": Unable to find converter id 'TCKConverter' in returned"
          + " Iterator.");
    }
  }

  // Application.getConverterTypes()
  public void applicationGetConverterTypesTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();
    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    application.addConverter(this.getClass(),
        "javax.faces.convert.BooleanConverter");

    Iterator<Class<?>> i = application.getConverterTypes();
    if (i == null) {
      out.println(
          JSFTestUtil.FAIL + ": Application.getConverterIds() returned null.");
      return;
    }

    boolean found = false;
    while (i.hasNext()) {
      if (i.next().equals(this.getClass())) {
        found = true;
        break;
      }
    }

    if (found) {
      out.println(JSFTestUtil.PASS);
    } else {
      out.println(JSFTestUtil.FAIL + ": Unable to find converter class '"
          + this.getClass().getName() + "' in" + " returned Iterator.");
    }
  }

  // Application.addValidator(String, String)
  // Application.createValidator(String)
  public void applicationAddCreateValidatorTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    application.addValidator("TCKValidator",
        "javax.faces.validator.LengthValidator");
    try {
      Validator validator = application.createValidator("TCKValidator");

      if (validator == null) {
        out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
            + "Application.createValidator(String) returned null "
            + "when provided a valid id.");
        return;
      }

      if (validator instanceof LengthValidator) {
        out.println(JSFTestUtil.PASS);
      } else {
        out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
            + "Validator returned was not instance of "
            + "LengthValidator. Validator type received: "
            + validator.getClass().getName());
      }

    } catch (Exception e) {
      out.println(
          JSFTestUtil.FAIL + ": Unexpected exception creating validator.");
      e.printStackTrace();
    }
  }

  // Application.addValidator(String, String) throws NPE if any args are null
  public void applicationAddValidatorNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    JSFTestUtil.checkForNPE(application, "addValidator",
        new Class<?>[] { String.class, String.class },
        new Object[] { null, "javax.faces.validator.LengthValidator" }, out);

    JSFTestUtil.checkForNPE(application, "addValidator",
        new Class<?>[] { String.class, String.class },
        new Object[] { "NullValidator", null }, out);

  } // End applicationAddValidatorNPETest

  // Application.createValidator(String) throws NullPointerException
  public void applicationCreateValidatorNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    JSFTestUtil.checkForNPE(application, "createValidator",
        new Class<?>[] { String.class }, new Object[] { null }, out);

  }

  // Application.createValidator(String) throws FacesException
  public void applicationCreateValidatorFETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    Application application = getApplication();

    JSFTestUtil.checkForFE(application, "createValidator",
        new Class<?>[] { String.class }, new Object[] { "boogus" }, pw);

  }

  // Application.getValidatorIds()
  public void applicationGetValidatorIdsTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    application.addValidator("TCKValidator",
        "javax.faces.validator.LengthValidator");

    Iterator<String> i = application.getValidatorIds();
    if (i == null) {
      out.println(
          JSFTestUtil.FAIL + ": Application.getValidatorIds() returned null.");
      return;
    }

    if (JSFTestUtil.checkIterator(i, new String[] { "TCKValidator" }, false,
        true)) {
      out.println(JSFTestUtil.PASS);
    } else {
      out.println(JSFTestUtil.FAIL
          + ": Unable to find validator id 'TCKValidator' in the "
          + "Iterator returned by Application.getValidatorIds().");
    }
  }

  // Application.getDefaultValidatorInfo()
  public void applicationGetDefaultValidatorInfoTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    if (application.getDefaultValidatorInfo() == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unable to obtain Default validator info.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // Application.getActionListener()
  public void applicationGetActionListenerTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    ActionListener defaultListener = application.getActionListener();
    if (defaultListener == null) {
      out.println(JSFTestUtil.FAIL
          + ": Application.getActionListener() returned null.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // Application.setActionListener(ActionListener)
  public void applicationSetActionListenerTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    ActionListener origListener = application.getActionListener();
    ActionListener tckActionListener = new TCKActionListener();

    try {
      application.setActionListener(tckActionListener);
    } catch (Exception e) {
      out.println(
          JSFTestUtil.FAIL + ": Unexpected exception setting ActionListener.");
      e.printStackTrace();
      return;
    }

    ActionListener listener = application.getActionListener();
    if (listener == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.getActionListener() returned null after "
          + "calling Application.setActionListener(ActionListener).");
      return;
    }

    if (!listener.equals(tckActionListener)) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "TCKActionListener not returned by "
          + "Application.getActionListener() as" + "expected." + JSFTestUtil.NL
          + "ActionListener type: " + listener.getClass().getName());
      return;
    }

    out.println(JSFTestUtil.PASS);
    // restore original listener
    application.setActionListener(origListener);
  }

  // Application.setActionListener(ActionListener) throws NPE if arg is null
  public void applicationSetActionListenerNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    JSFTestUtil.checkForNPE(application, "setActionListener",
        new Class<?>[] { ActionListener.class }, new Object[] { null }, out);
  }

  // Application.getNavigationHandler();
  // Application.setNavigationHandler(ViewHandler);
  public void applicationGetSetNavigationHandlerTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    NavigationHandler handler = application.getNavigationHandler();
    if (handler == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.getNavigationHandler() returned null.");
      return;
    }

    NavigationHandler tckNavigationHandler = new TCKNavigationHandler();
    application.setNavigationHandler(tckNavigationHandler);

    NavigationHandler newNavigationHandler = application.getNavigationHandler();

    if (newNavigationHandler == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.getNavigationHandler() returned null "
          + "after calling"
          + " Application.setNavigationHandler(NavigationHandler).");
      return;
    }

    if (!newNavigationHandler.equals(tckNavigationHandler)) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.getNavigationHandler() didn't return the "
          + "expected NavigationHandler.");
    }

    out.println(JSFTestUtil.PASS);
    // restore original handler
    application.setNavigationHandler(handler);
  }

  public void applicationSetNavigationHandlerNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    // Application.setNavigationHandler(null)
    JSFTestUtil.checkForNPE(application, "setNavigationHandler",
        new Class<?>[] { NavigationHandler.class }, new Object[] { null }, out);

  }

  // Application.createValueBinding(String)
  public void applicationCreateValueBindingTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    request.setAttribute("TestBean", new TestBean());
    ValueBinding binding;
    try {
      binding = application.createValueBinding("#{TestBean.boolProp}");

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.getValueBinding(String) threw an expected "
          + "Exception.");
      e.printStackTrace();
      return;
    }

    if (binding == null) {
      out.println(JSFTestUtil.FAIL
          + ": Application.getValueBinding() returned null for a valid "
          + "value reference expression.");
      return;

    }

    out.println(JSFTestUtil.PASS);

  }

  // Application.createValueBinding(String) throws ReferenceSyntaxException
  // when provided an invalid ref
  public void applicationCreateValueBindingRSETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();
    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    request.setAttribute("TestBean", new TestBean());
    out.println("Testing #{TestBean[}");

    try {
      application.createValueBinding("#{TestBean[}");
      out.println(JSFTestUtil.FAIL
          + ": No Exception thrown when value reference expression "
          + "provided was invalid.");

    } catch (ReferenceSyntaxException rse) {
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL
          + ": Exception thrown when invalid value reference expression was provided"
          + " to Application.getValueBinding(String), but wasn't an instance of ReferenceSyntaxException.");
      e.printStackTrace();
    }
  }

  // Application.createValueBinding(String) throws NullPointerException when
  // provided will a null arg
  public void applicationCreateValueBindingNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    try {
      application.createValueBinding(null);
      out.println(JSFTestUtil.FAIL + ": Expected NullPointerException, "
          + "No Exception thrown using: "
          + "Application.createValueBinding(null)");

    } catch (NullPointerException npe) {
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL
          + ": Exception thrown when Application.getValueBinding(String) "
          + "was provided a null argument, but it wasn't an instance of NullPointerException.");
      e.printStackTrace();
    }

  }

  // Application.getViewHandler()
  // Application.setViewHandler(ViewHandler)
  public void applicationGetSetViewHandlerTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    ViewHandler origHandler = application.getViewHandler();

    if (origHandler == null) {
      out.println(
          JSFTestUtil.FAIL + ": Application.getViewHandler() returned null.");
      return;
    }

    ViewHandler tckHandler = new TCKViewHandler();
    application.setViewHandler(tckHandler);
    ViewHandler resolver = application.getViewHandler();

    if (resolver == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.getViewHandler() returned null after "
          + "calling Application.setViewHandler() with a new "
          + "ViewHandler instance.");

      return;
    }

    if (!resolver.equals(tckHandler)) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.getViewHandler didn't return the expected "
          + "ViewHandler.");

      return;
    }

    out.println(JSFTestUtil.PASS);
    // restore the original handler
    application.setViewHandler(origHandler);
  }

  public void applicationGetSetMessageBundleTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();
    String bundleName;
    String myBundle;

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    bundleName = "com.sun.ts.tests.jsf.common.resourcebundle."
        + "SimpleResourceBundle";

    try {
      application.setMessageBundle(bundleName);
      myBundle = application.getMessageBundle();

      if (!myBundle.equals(bundleName)) {
        out.println("Test FAILED Unexpected Result for "
            + "get/setMessageBundle()." + JSFTestUtil.NL + "Expected: "
            + bundleName + JSFTestUtil.NL + "Received: " + myBundle);
        return;
      }

      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + ": Unexpected Exception Thrown!");
      e.printStackTrace();
    }

  }

  public void applicationGetSetDefaultRenderKitIDTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    try {
      application.setDefaultRenderKitId("TCKRenderKit");

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL
          + ": Unexpected Exception Thrown when trying to set default "
          + "RenderKit ID!" + JSFTestUtil.NL
          + "application.setDefaultRenderKitId('TCKRenderKit')");
      e.printStackTrace();
    }

    try {
      String result = application.getDefaultRenderKitId();

      if (!"TCKRenderKit".equals(result)) {
        out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
            + "Unexpected RenderKit ID returned from "
            + "application.getDefaultRenderKitId()." + JSFTestUtil.NL
            + "Expected: TCKRenderKit" + JSFTestUtil.NL + "Received: "
            + result);
        return;
      }
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + ": Unexpected Exception Thrown!");
      e.printStackTrace();
    }

  }

  public void applicationSetMessageBundleNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    JSFTestUtil.checkForNPE(application, "setMessageBundle",
        new Class<?>[] { String.class }, new Object[] { null }, out);

  }

  public void applicationGetResourceBundleTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    Application application = context.getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    try {
      String expectedName = "com.sun.ts.tests.jsf.common."
          + "resourcebundle.SimpleResourceBundle_en";

      getFacesContext().getViewRoot().setLocale(Locale.ENGLISH);

      ResourceBundle rb = application.getResourceBundle(context, "simpleRB");

      String resultName = rb.getClass().getName();

      if (resultName.equals(expectedName)) {
        out.println(JSFTestUtil.PASS);
      } else {
        out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
            + "Unexpected Result for getResourceBundle()." + JSFTestUtil.NL
            + "Expected: " + expectedName + JSFTestUtil.NL + "Received: "
            + resultName);
      }
    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + ": Unexpected Exception Thrown!");
      e.printStackTrace();
    }
  }

  public void applicationGetResourceBundleNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    Application application = context.getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    // FacesContext is null.
    JSFTestUtil.checkForNPE(application, "getResourceBundle",
        new Class<?>[] { FacesContext.class, String.class },
        new Object[] { null, "simpleRB" }, out);

    // ResourceBundle is null
    JSFTestUtil.checkForNPE(application, "getResourceBundle",
        new Class<?>[] { FacesContext.class, String.class },
        new Object[] { context, null }, out);

  }// End applicationGetResourceBundleNPETest

  // Application.setViewHandler(ViewHandler) throws NPE if arg is null
  public void applicationSetViewHandlerNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    JSFTestUtil.checkForNPE(application, "setViewHandler",
        new Class<?>[] { ViewHandler.class }, new Object[] { null }, out);

  }// End applicationSetViewHandlerNPETest

  // Test for: Application.setStateManager() throws NullPointerException
  public void applicationStateManagerNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    JSFTestUtil.checkForNPE(application, "setStateManager",
        new Class<?>[] { StateManager.class }, new Object[] { null }, out);

  } // End applicationStateManagerNPETest

  public void applicationCreateMethodBindingTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();
    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    TestBean bean = new TestBean();
    request.setAttribute("TestBean", bean);

    MethodBinding binding = application.createMethodBinding(
        "#{requestScope.TestBean.setBoolProp}", new Class[] { Boolean.TYPE });

    if (binding == null) {
      out.println(JSFTestUtil.FAIL + ": Application.createMethodBinding()"
          + " returned a null value.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void applicationCreateMethodBindingNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    JSFTestUtil.checkForNPE(application, "createMethodBinding",
        new Class<?>[] { String.class, Class[].class },
        new Object[] { null, new Class[] { Boolean.TYPE } }, out);
  }

  public void applicationCreateMethodBindingRSETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    try {
      application.createMethodBinding("requestScope.getRemoteHost",
          new Class[] { Boolean.TYPE });
      out.println(JSFTestUtil.FAIL
          + ": No Exception thrown if an invalid method reference"
          + " was passed to Application.createMethodBinding().");

    } catch (ReferenceSyntaxException rse) {
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(
          JSFTestUtil.FAIL + ": Exception thrown if an invalid method reference"
              + " was passed to Application.createMethodBinding(), but it"
              + " wasn't an instance of ReferenceSyntaxException.");
      e.printStackTrace();
    }

  }

  public void applicationSetGetDefaultLocaleTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    if (!setGetLocaleTestPassed) {
      Application application = getApplication();

      if (application.getDefaultLocale() != null) {
        out.println(
            JSFTestUtil.FAIL + ": Expected Application.getDefaultLocale() "
                + "to return null if the default locale has not been explicitly"
                + " set by a call to Application.setDefaultLocale(Locale).");
        return;
      }

      application.setDefaultLocale(Locale.US);

      if (!Locale.US.equals(application.getDefaultLocale())) {
        out.println(
            JSFTestUtil.FAIL + ": Expected Application.getDefaultLocale()"
                + "to return Locale.US as set via call to Application."
                + "setDefaultLocale().");
        out.println("Localed received: " + application.getDefaultLocale());
        return;
      }

      setGetLocaleTestPassed = true;
    }

    if (setGetLocaleTestPassed) {
      out.println(JSFTestUtil.PASS);
    }

  }

  public void applicationSetSupportedLocalesNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    JSFTestUtil.checkForNPE(application, "setSupportedLocales",
        new Class<?>[] { Collection.class }, new Object[] { null }, out);

  }

  public void applicationSetDefaultLocaleNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    JSFTestUtil.checkForNPE(application, "setDefaultLocale",
        new Class<?>[] { Locale.class }, new Object[] { null }, out);

  }

  public void applicationSetGetSupportedLocalesTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    Collection<Locale> collection = new ArrayList<Locale>();
    collection.add(Locale.ENGLISH);
    collection.add(Locale.FRENCH);
    collection.add(Locale.GERMAN);

    application.setSupportedLocales(collection);

    Locale[] locales = { Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN };
    List<Locale> retLocales = new ArrayList<Locale>();
    for (Iterator<Locale> i = application.getSupportedLocales(); i.hasNext();) {
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

  // ----------------------------------------------------- Tests Added for 1.2
  // Application.getELContextListenersTest()
  public void applicationGetELContextListenersTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    if (application.getELContextListeners() == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unable to obtain ElContext Listeners.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void applicationGetELResolverTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    ELResolver resolver = application.getELResolver();

    if (resolver == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.getELResolver() returned" + " null.");
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

  public void applicationGetExpressionFactoryTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    ExpressionFactory controlFactory = JspFactory.getDefaultFactory()
        .getJspApplicationContext(servletContext).getExpressionFactory();

    if (controlFactory == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unable to obtain ExpressionFactory "
          + "from JspApplicationContext.");
      return;
    }

    ExpressionFactory exprFactory = application.getExpressionFactory();

    if (exprFactory == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unable to obtain ExpressionFactory instance.");
      return;
    }

    // Left in for debugging.
    //
    // ExpressionFactory controlFactoryTwo =
    // JspFactory.getDefaultFactory().
    // getJspApplicationContext(servletContext).getExpressionFactory();
    //
    // ExpressionFactory exprFactoryTwo =
    // application.getExpressionFactory();
    // out.println("*** First Call application.getExpressionFactory() " +
    // "Returns ***: " + exprFactory.toString());
    // out.println("*** Second Call application.getExpressionFactory() " +
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
          + "the same instance as that returned by "
          + "JspApplicationContext.getExpressionFactory!" + JSFTestUtil.NL
          + "Found: " + controlFactory.toString() + JSFTestUtil.NL
          + "Expected: " + exprFactory.toString());
      return;
    }

    out.println(JSFTestUtil.PASS);

  }

  public void applicationEvaluationExpressionGetTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    String testName = "applicationEvaluationExpressionGetTest";

    String value = (String) application.evaluateExpressionGet(getFacesContext(),
        "#{param.testname}", java.lang.String.class);

    if (!testName.equals(value)) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unexpected evaluation using Application."
          + "evaluateExpressionGet()." + JSFTestUtil.NL + "Expected: "
          + testName + JSFTestUtil.NL + "Received: " + value);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // Application.createComponent(ValueExpression, FacesContext, String)
  public void applicationCreateComponentExpressionTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    request.setAttribute("TestBean", new TestBean());
    application.addComponent("TCKComponent", "javax.faces.component.UIOutput");
    ELContext elContext = getFacesContext().getELContext();
    ValueExpression expr = application.getExpressionFactory()
        .createValueExpression(elContext, "#{TestBean.component}",
            javax.faces.component.UIComponent.class);

    if (expr == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unable to obtain ValueExpression instance from " + "Application.");
      return;
    }

    // ensures failure if the ValueExpression lookup fails
    UIInput input = (UIInput) application.createComponent(expr,
        getFacesContext(), "TCKComponent");
    if (input == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.createComponent(ValueExpression, "
          + "FacesContext, String) returned null when using "
          + "a valid ValueExpression.");
      return;
    }

    /*
     * Next validate that if the ValueExpression doesn't return a UIComponent
     * instance, then create the component based on the identifier.
     */
    TestBean bean = (TestBean) request.getAttribute("TestBean");
    bean.setComponent(null);

    UIOutput output = (UIOutput) application.createComponent(expr,
        getFacesContext(), "TCKComponent");
    if (output == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.createComponent(ValueExpression, "
          + "FacesContext, String, String) returned "
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
          + "Expected a return of UIOutput, but received " + "a null value.");
      return;
    }

    out.println(JSFTestUtil.PASS);

  }

  // Application.createComponent(ValueExpression, FacesContext, String,
  // String)
  public void applicationCreateComponentExpressionFSSTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    request.setAttribute("TestBean", new TestBean());
    application.addComponent("TCKComponent", "javax.faces.component.UIOutput");
    ELContext elContext = getFacesContext().getELContext();
    ValueExpression expr = application.getExpressionFactory()
        .createValueExpression(elContext, "#{TestBean.component}",
            javax.faces.component.UIComponent.class);

    if (expr == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unable to obtain ValueExpression " + "instance from Application.");
      return;
    }

    // ensures failure if the ValueExpression lookup fails
    UIInput input = (UIInput) application.createComponent(expr,
        getFacesContext(), "TCKComponent", "TCKRenderer");
    if (input == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.createComponent(ValueExpression, "
          + "FacesContext, String) returned null when using "
          + "a valid ValueExpression.");
      return;
    }

    /*
     * Next validate that if the ValueExpression doesn't return a UIComponent
     * instance, then create the component based on the identifier.
     */
    TestBean bean = (TestBean) request.getAttribute("TestBean");
    bean.setComponent(null);

    UIOutput output = (UIOutput) application.createComponent(expr,
        getFacesContext(), "TCKComponent");
    if (output == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Application.createComponent(ValueExpression, "
          + "FacesContext, String, 'null) returned "
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
          + "Expected a return of UIOutput, but received " + "a null value.");
      return;
    }

    out.println(JSFTestUtil.PASS);

  }

  // Application.createComponent(ValueExpression, FacesContext, String,
  // 'null')
  public void applicationCreateComponentExpressionFSSNullTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    request.setAttribute("TestBean", new TestBean());
    application.addComponent("TCKComponent", "javax.faces.component.UIOutput");
    ELContext elContext = getFacesContext().getELContext();
    ValueExpression expr = application.getExpressionFactory()
        .createValueExpression(elContext, "#{TestBean.component}",
            javax.faces.component.UIComponent.class);

    if (expr == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unable to obtain ValueExpression " + "instance from Application.");
      return;
    }

    // ensures failure if the ValueExpression lookup fails
    UIInput input = (UIInput) application.createComponent(expr,
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

    UIOutput output = (UIOutput) application.createComponent(expr,
        getFacesContext(), "TCKComponent");
    if (output == null) {
      out.println(JSFTestUtil.FAIL + ": "
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
          + "Expected a return of UIOutput, but received " + "a null value.");
      return;
    }

    out.println(JSFTestUtil.PASS);

  }

  // Application.createComponent(ValueExpression, FacesContext, String)
  // NPE is arg is null
  public void applicationCreateComponentExpressionNPETest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    TestBean bean = new TestBean();
    bean.setComponent(null);
    request.setAttribute("TestBean", bean);
    application.addComponent("TCKComponent", "javax.faces.component.UIOutput");
    ELContext elContext = getFacesContext().getELContext();
    ValueExpression expr = application.getExpressionFactory()
        .createValueExpression(elContext, "#{TestBean.component}",
            javax.faces.component.UIComponent.class);

    // ValueBinding as null
    JSFTestUtil.checkForNPE(application, "createComponent",
        new Class<?>[] { ValueExpression.class, FacesContext.class,
            String.class },
        new Object[] { null, getFacesContext(), "TCKComponent" }, out);

    // FacesContext as null
    JSFTestUtil.checkForNPE(
        application, "createComponent", new Class<?>[] { ValueExpression.class,
            FacesContext.class, String.class },
        new Object[] { expr, null, "TCKComponent" }, out);

    // String as null
    JSFTestUtil.checkForNPE(application, "createComponent",
        new Class<?>[] { ValueExpression.class, FacesContext.class,
            String.class },
        new Object[] { expr, getFacesContext(), null }, out);

  } // End applicationCreateComponentExpressionNPETest

  // Application.createComponent(ValueExpression, FacesContext, String,
  // String)
  public void applicationCreateComponentExpressionFSSNPETest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    TestBean bean = new TestBean();
    bean.setComponent(null);
    request.setAttribute("TestBean", bean);
    application.addComponent("TCKComponent", "javax.faces.component.UIOutput");
    ELContext elContext = context.getELContext();
    ValueExpression expr = application.getExpressionFactory()
        .createValueExpression(elContext, "#{TestBean.component}",
            javax.faces.component.UIComponent.class);

    // createComponent(null, FacesContext, String, String)
    JSFTestUtil.checkForNPE(application, "createComponent",
        new Class<?>[] { ValueExpression.class, FacesContext.class,
            String.class, String.class },
        new Object[] { null, context, "TCKComponent", null }, out);

    // createComponent(ValueExpression, null, String, String)
    JSFTestUtil.checkForNPE(application, "createComponent",
        new Class<?>[] { ValueExpression.class, FacesContext.class,
            String.class, String.class },
        new Object[] { expr, null, "TCKComponent", null }, out);

    // createComponent(ValueExpression, FacesContext, null, String)
    JSFTestUtil.checkForNPE(application, "createComponent",
        new Class<?>[] { ValueExpression.class, FacesContext.class,
            String.class, String.class },
        new Object[] { expr, context, null, null }, out);

  }// End applicationCreateComponentExpressionFSSNPETest

  public void applicationAddELResolverTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    try {
      application.addELResolver(new TCKELResolver());

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + ": Unexpected Exception...");
      e.printStackTrace();
    }

    out.println(JSFTestUtil.PASS);

  }

  public void applicationAddGetRemoveELContextListenerTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    int contextCount = application.getELContextListeners().length;
    TCKELContextListener listener = new TCKELContextListener();
    application.addELContextListener(listener);

    if ((contextCount + 1) != application.getELContextListeners().length) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Called Application.addELContextListener()"
          + "but the length of the array returned by "
          + "Application.getELContextListeners() didn't " + "increase by one."
          + JSFTestUtil.NL + "Expected length: " + (contextCount + 1)
          + JSFTestUtil.NL + "Actual length: "
          + application.getELContextListeners().length);
      return;
    }

    application.removeELContextListener(listener);

    List<ELContextListener> listeners = Arrays
        .asList(application.getELContextListeners());

    if (listeners.contains(listener)) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "ELContextListener not removed as expected.");
      return;
    }

    out.println(JSFTestUtil.PASS);

  }

  // Test for Application.getResourceHandler()
  public void applicationGetResourceHandlerTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    // Set a specific ResourceHandler.
    TCKResourceHandler myRH = new TCKResourceHandler();
    application.setResourceHandler(myRH);

    if (application.getResourceHandler() == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unable to obtain ResourceHandler " + "instance.");
      return;
    }

    if (!myRH.equals(application.getResourceHandler())) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "ResourceHandler was not able to be set!");
      return;
    }

    out.println(JSFTestUtil.PASS);

  }

  // Test for Application.setStatemanager()
  // Application.getStatemanager()
  public void applicationGetSetStateManagerTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    // Test to make sure we get a StateManger by default.
    try {
      application.getStateManager();

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unexpected Exception thrown when calling "
          + "Application.getStateManager" + JSFTestUtil.NL);
      e.printStackTrace();
    }

    try {
      StateManager tckstatemgr = new TCKStateManager();
      application.setStateManager(tckstatemgr);
      String result = application.getStateManager().getClass().getSimpleName();

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

  // Test for Application.setResourceHandler()
  public void applicationSetResourceHandlerTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    ResourceHandler rh = application.getResourceHandler();
    application.setResourceHandler(rh);

    if (!rh.equals(application.getResourceHandler())) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unable to obtain ResourceHandler " + "instance.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // Test for Application.setResourceHandler() throws NullPointerException
  public void applicationSetResourceHandlerNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    JSFTestUtil.checkForNPE(application, "setResourceHandler",
        new Class<?>[] { ResourceHandler.class }, new Object[] { null }, out);

  }

  // Test for Application.getProjectStage() "Default settings"

  public void applicationGetProjectStageTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    String stage = ProjectStage.Production.toString();

    if (!("production".equalsIgnoreCase(stage))) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL + "Expected: production"
          + JSFTestUtil.NL + "Received" + stage);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // Test for Application.publishEvent throws NullPointerException
  public void applicationPublishEventNPETest1(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();
    FacesContext context = getFacesContext();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    UIComponent uic = application.createComponent(UIOutput.COMPONENT_TYPE);
    SystemEvent se = new TCKSystemEvent(uic);

    // Application.publishEvent(null, Class, Object)
    JSFTestUtil.checkForNPE(application, "publishEvent",
        new Class<?>[] { FacesContext.class, Class.class, Object.class, },
        new Object[] { null, se.getClass(), "object" }, out);

    // Application.publishEvent(FacesContext, null, Object)
    JSFTestUtil.checkForNPE(application, "publishEvent",
        new Class<?>[] { FacesContext.class, Class.class, Object.class, },
        new Object[] { context, null, "object" }, out);

    // Application.publishEvent(FacesContext, Class, null)
    JSFTestUtil.checkForNPE(application, "publishEvent",
        new Class<?>[] { FacesContext.class, Class.class, Object.class, },
        new Object[] { context, se.getClass(), null }, out);

  }// End applicationPublishEventNPETest1

  // Test for Application.publishEvent throws NullPointerException
  public void applicationPublishEventNPETest2(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();
    FacesContext context = getFacesContext();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    UIComponent uic = application.createComponent(UIOutput.COMPONENT_TYPE);

    // Application.publishEvent(null, Class, Class, Object)
    JSFTestUtil.checkForNPE(application, "publishEvent",
        new Class<?>[] { FacesContext.class, Class.class, Class.class,
            Object.class, },
        new Object[] { null, TCKSystemEvent.class, uic.getClass(), "object" },
        out);

    // Application.publishEvent(FacesContext, null, Class, Object)
    JSFTestUtil.checkForNPE(application, "publishEvent",
        new Class<?>[] { FacesContext.class, Class.class, Class.class,
            Object.class, },
        new Object[] { context, null, uic.getClass(), "object" }, out);

    // Application.publishEvent(FacesContext, Class, Class, null)
    JSFTestUtil.checkForNPE(application, "publishEvent",
        new Class<?>[] { FacesContext.class, Class.class, Class.class,
            Object.class, },
        new Object[] { context, TCKSystemEvent.class, uic.getClass(), null },
        out);

  }// End applicationPublishEventNPETest2

  // Test for Application.publishEvent
  public void applicationPublishEventTest1(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    UIComponent uic = getApplication().createComponent(UIOutput.COMPONENT_TYPE);
    SystemEvent se = new TCKSystemEvent(uic);

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    // Application.publishEvent(FacesContext, class, object)
    try {
      application.publishEvent(getFacesContext(), se.getClass(), uic);
      out.println(JSFTestUtil.PASS);
    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL);
      e.printStackTrace();
    }

  } // End applicationPublishEventTest1

  public void applicationPublishEventTest2(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    UIComponent uic = getApplication().createComponent(UIOutput.COMPONENT_TYPE);
    SystemEvent se = new TCKSystemEvent(uic);

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    // Application.publishEvent(getFacesContext, class, class, source)
    try {
      application.publishEvent(getFacesContext(), se.getClass(), uic.getClass(),
          uic);
      out.println(JSFTestUtil.PASS);
    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL);
      e.printStackTrace();
    }

  } // End applicationPublishEventTest2

  // Test for Application.subscribeToEvent
  public void applicationSubscribeToEventTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    UIComponent uic = application.createComponent(UIOutput.COMPONENT_TYPE);
    SystemEvent se = new TCKSystemEvent(uic);
    SystemEventListener sel = new TCKSystemEventListener();

    try {
      application.subscribeToEvent(se.getClass(), uic.getClass(), sel);
      out.println(JSFTestUtil.PASS);
    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL);
      e.printStackTrace();
    }

  }// End applicationSubscribeToEventTest

  // Test for Application.subscribeToEvent
  public void applicationSubscribeToEventNullTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    UIComponent uic = application.createComponent(UIOutput.COMPONENT_TYPE);
    SystemEvent se = new TCKSystemEvent(uic);
    SystemEventListener sel = new TCKSystemEventListener();

    try {
      application.subscribeToEvent(se.getClass(), null, sel);
      out.println(JSFTestUtil.PASS);
    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL);
      e.printStackTrace();
    }

  }// End applicationSubscribeToEventNullTest

  // Test for Application.subscribeToEvent
  public void applicationSubscribeToEventNoSrcClassTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    UIComponent uic = application.createComponent(UIOutput.COMPONENT_TYPE);
    SystemEvent se = new TCKSystemEvent(uic);
    SystemEventListener sel = new TCKSystemEventListener();

    try {
      application.subscribeToEvent(se.getClass(), sel);
      out.println(JSFTestUtil.PASS);
    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL);
      e.printStackTrace();
    }

  }// End applicationSubscribeToEventNoSrcClassTest

  // Test for Application.subscribeToEvent
  public void applicationSubscribeToEventNoSrcClassNPETest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    UIComponent uic = application.createComponent(UIOutput.COMPONENT_TYPE);
    SystemEvent se = new TCKSystemEvent(uic);
    SystemEventListener sel = new TCKSystemEventListener();

    // Application.subscribeToEvent(null, SystemEventListener)
    JSFTestUtil.checkForNPE(application, "subscribeToEvent",
        new Class<?>[] { Class.class, SystemEventListener.class },
        new Object[] { null, sel }, out);

    // Application.subscribeToEvent(Class, null)
    JSFTestUtil.checkForNPE(application, "subscribeToEvent",
        new Class<?>[] { Class.class, SystemEventListener.class },
        new Object[] { se.getClass(), null }, out);

  }

  // Test for Application.subscribeToEvent
  public void applicationSubscribeToEventNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    UIComponent uic = application.createComponent(UIOutput.COMPONENT_TYPE);
    SystemEventListener sel = new TCKSystemEventListener();
    Class<?> srcClass = uic.getClass();

    out.append("Running==> application.subscribeToEvent(null, null, "
        + "SystemEventListener)" + JSFTestUtil.NL);
    JSFTestUtil.checkForNPE(application, "subscribeToEvent",
        new Class<?>[] { Class.class, Class.class, SystemEventListener.class },
        new Object[] { null, null, sel }, out);

    out.append("Running==> application.subscribeToEvent(null, Class, "
        + "SystemEventListener)" + JSFTestUtil.NL);
    JSFTestUtil.checkForNPE(application, "subscribeToEvent",
        new Class<?>[] { Class.class, Class.class, SystemEventListener.class },
        new Object[] { null, srcClass, sel }, out);

    out.append("Running==> application.subscribeToEvent(Class, "
        + "SystemEventListener, null)" + JSFTestUtil.NL);
    JSFTestUtil.checkForNPE(application, "subscribeToEvent",
        new Class<?>[] { Class.class, Class.class, SystemEventListener.class },
        new Object[] { TCKSystemEvent.class, srcClass, null }, out);

    out.append("Running==> application.subscribeToEvent(Class, null, null)"
        + JSFTestUtil.NL);
    JSFTestUtil.checkForNPE(application, "subscribeToEvent",
        new Class<?>[] { Class.class, Class.class, SystemEventListener.class },
        new Object[] { TCKSystemEvent.class, srcClass, null }, out);

    out.append("Running==> application.subscribeToEvent(null, null, null)"
        + JSFTestUtil.NL);
    JSFTestUtil.checkForNPE(application, "subscribeToEvent",
        new Class<?>[] { Class.class, Class.class, SystemEventListener.class },
        new Object[] { null, null, null }, out);

    out.close();
  } // End applicationSubscribeToEventNPETest

  // Test for Application.unsubscribeFromEvent
  public void applicationUnsubscribeFromEventTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    UIComponent uic = application.createComponent(UIOutput.COMPONENT_TYPE);
    SystemEvent se = new TCKSystemEvent(uic);
    SystemEventListener sel = new TCKSystemEventListener();

    try {
      application.unsubscribeFromEvent(se.getClass(), uic.getClass(), sel);
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL);
      e.printStackTrace();
    }

  }

  // Test for Application.unsubscribeFromEvent(systemEventClass, listener)
  public void applicationUnsubscribeFromEventSLTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    UIComponent uic = application.createComponent(UIOutput.COMPONENT_TYPE);
    SystemEvent se = new TCKSystemEvent(uic);
    SystemEventListener sel = new TCKSystemEventListener();

    try {
      application.unsubscribeFromEvent(se.getClass(), sel);
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL);
      e.printStackTrace();
    }

  }

  // Test for Application.unsubscribeFromEvent
  public void applicationUnsubscribeFromEventNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    UIComponent uic = application.createComponent(UIOutput.COMPONENT_TYPE);
    SystemEvent se = new TCKSystemEvent(uic);
    SystemEventListener sel = new TCKSystemEventListener();

    // Application.unsubscribeFromEvent(null, Class, SystemEventListener)
    JSFTestUtil.checkForNPE(application, "unsubscribeFromEvent",
        new Class<?>[] { Class.class, Class.class, SystemEventListener.class },
        new Object[] { null, uic.getClass(), sel }, out);

    // Application.unsubscribeFromEvent(Class, Class, null)
    JSFTestUtil.checkForNPE(application, "unsubscribeFromEvent",
        new Class<?>[] { Class.class, Class.class, SystemEventListener.class },
        new Object[] { se.getClass(), uic.getClass(), null }, out);

    // Application.unsubscribeFromEvent(null, SystemEventListener)
    JSFTestUtil.checkForNPE(application, "unsubscribeFromEvent",
        new Class<?>[] { Class.class, SystemEventListener.class },
        new Object[] { null, sel }, out);

    // Application.unsubscribeFromEvent(Class, null)
    JSFTestUtil.checkForNPE(application, "unsubscribeFromEvent",
        new Class<?>[] { Class.class, SystemEventListener.class },
        new Object[] { se.getClass(), null }, out);

  }

  // Test for Application.unsubscribeFromEvent
  public void applicationUnsubscribeFromEventNoSrcClassTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    UIComponent uic = application.createComponent(UIOutput.COMPONENT_TYPE);
    SystemEvent se = new TCKSystemEvent(uic);
    SystemEventListener sel = new TCKSystemEventListener();

    try {
      application.unsubscribeFromEvent(se.getClass(), sel);
      out.println(JSFTestUtil.PASS);
    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL);
      e.printStackTrace();
    }

  }

  // Test for Application.addBehavior & Application.getBehaviorIds
  public void applicationAddBehaviorTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    String mb = "myBehavior";

    application.addBehavior(mb, (new TCKBehavior().getClass().getName()));

    Iterator<String> i = application.getBehaviorIds();
    while (i.hasNext()) {
      if (mb.equals(i.next())) {
        out.println(JSFTestUtil.PASS);
        return;
      }
    }

    out.println(JSFTestUtil.FAIL);

  }// End applicationAddBehaviorTest

  // Test for Application.addBehavior
  public void applicationAddBehaviorNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    // Application.addBehaviorTest(null, String)
    JSFTestUtil.checkForNPE(application, "addBehavior",
        new Class<?>[] { String.class, String.class },
        new Object[] { null, new TCKBehavior().getClass().getName() }, out);

    // Application.addBehaviorTest(String, null)
    JSFTestUtil.checkForNPE(application, "addBehavior",
        new Class<?>[] { String.class, String.class },
        new Object[] { "myBehavior", null }, out);

  }// End applicationAddBehaviorNPETest

  // Test for Application.createBehavior
  public void applicationCreateBehaviorTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    String mb = "myBehavior";
    application.addBehavior(mb, (new TCKBehavior().getClass().getName()));
    TCKBehavior firstBehavior = (TCKBehavior) application.createBehavior(mb);

    // Test to make sure we get back the default name.
    if (!("default_name".equals(firstBehavior.getName()))) {
      out.println(
          JSFTestUtil.FAIL + JSFTestUtil.NL + "Unexpected value returned!"
              + JSFTestUtil.NL + "Expected: default_name" + JSFTestUtil.NL
              + "Received: " + firstBehavior.getName());
      return;
    }

    firstBehavior.setName("changed_name");
    TCKBehavior secondBehavior = (TCKBehavior) application.createBehavior(mb);

    // Test to make sure we get back the default name again.
    if (!("default_name".equals(secondBehavior.getName()))) {
      out.println(
          JSFTestUtil.FAIL + JSFTestUtil.NL + "Unexpected value returned."
              + JSFTestUtil.NL + "Expected: default_name" + JSFTestUtil.NL
              + "Received: " + secondBehavior.getName());
      return;
    }

    out.println(JSFTestUtil.PASS);

  }// End applicationCreateBehaviorTest

  // Test for Application.createBehavior
  public void applicationCreateBehaviorNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    JSFTestUtil.checkForNPE(application, "createBehavior",
        new Class<?>[] { String.class }, new Object[] { null }, out);

  }// End applicationCreateBehaviorNPETest

  // Test for Application.createBehavior
  public void applicationCreateBehaviorFETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      pw.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    application.addBehavior("bad", "com.sun.ts.tests.jsf.api."
        + "javax_faces.application.application.BadBehavior");

    JSFTestUtil.checkForFE(application, "createBehavior",
        new Class<?>[] { String.class }, new Object[] { "bad" }, pw);

  }// End applicationCreateBehaviorFETest

  // Test for Application.addDefaultValidatorId
  public void applicationAddDefaultValidatorIdTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    String mv = "myValidator";

    application.addValidator(mv, BeanValidator.class.getName());
    application.addDefaultValidatorId(mv);

    Map<String, String> dvMap = application.getDefaultValidatorInfo();
    Iterator<String> keys = dvMap.keySet().iterator();

    while (keys.hasNext()) {
      if (mv.equals(keys.next())) {
        out.println(JSFTestUtil.PASS);
        return;
      }
    }

    out.println(JSFTestUtil.FAIL);

  }// End applicationAddDefaultValidatorIdTest

  // Test for Application.getSearchExpressionHandler
  public void applicationGetSearchExpressionHandlerTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    if (application.getSearchExpressionHandler() == null) {
      out.println(JSFTestUtil.FAIL);
      return;
    }

    out.println(JSFTestUtil.PASS);

  }// End applicationGetSearchExpressionHandlerTest

  // Test for Application.setSearchExpressionHandler
  public void applicationSetSearchExpressionHandlerTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application == null) {
      out.println(JSFTestUtil.APP_NULL_MSG);
      return;
    }

    SearchExpressionHandler oldHandler = application
        .getSearchExpressionHandler();

    SearchExpressionHandler handler = new SearchExpressionHandler() {
      @Override
      public String resolveClientId(
          SearchExpressionContext searchExpressionContext, String expression) {
        throw new UnsupportedOperationException("Not supported yet.");
      }

      @Override
      public List<String> resolveClientIds(
          SearchExpressionContext searchExpressionContext, String expressions) {
        throw new UnsupportedOperationException("Not supported yet.");
      }

      @Override
      public void resolveComponent(
          SearchExpressionContext searchExpressionContext, String expression,
          ContextCallback callback) {
        throw new UnsupportedOperationException("Not supported yet.");
      }

      @Override
      public void resolveComponents(
          SearchExpressionContext searchExpressionContext, String expressions,
          ContextCallback callback) {
        throw new UnsupportedOperationException("Not supported yet.");
      }

      @Override
      public void invokeOnComponent(
          SearchExpressionContext searchExpressionContext, UIComponent previous,
          String expression, ContextCallback callback) {
        throw new UnsupportedOperationException("Not supported yet.");
      }

      @Override
      public String[] splitExpressions(FacesContext context,
          String expressions) {
        throw new UnsupportedOperationException("Not supported yet.");
      }

      @Override
      public boolean isPassthroughExpression(
          SearchExpressionContext searchExpressionContext, String expression) {
        throw new UnsupportedOperationException("Not supported yet.");
      }

      @Override
      public boolean isValidExpression(
          SearchExpressionContext searchExpressionContext, String expression) {
        throw new UnsupportedOperationException("Not supported yet.");
      }
    };
    application.setSearchExpressionHandler(handler);

    if (application.getSearchExpressionHandler() != handler) {
      out.println(JSFTestUtil.FAIL);
      return;
    }

    application.setSearchExpressionHandler(oldHandler);

    out.println(JSFTestUtil.PASS);

  }// End applicationSetSearchExpressionHandlerTest
}
