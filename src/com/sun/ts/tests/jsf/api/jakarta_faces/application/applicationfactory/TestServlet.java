/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jsf.api.jakarta_faces.application.applicationfactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import jakarta.el.ELContextListener;
import jakarta.el.ELException;
import jakarta.el.ELResolver;
import jakarta.el.ExpressionFactory;
import jakarta.el.ValueExpression;
import jakarta.faces.FacesException;
import jakarta.faces.FactoryFinder;
import jakarta.faces.application.Application;
import jakarta.faces.application.ApplicationFactory;
import jakarta.faces.application.NavigationHandler;
import jakarta.faces.application.StateManager;
import jakarta.faces.application.ViewHandler;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.behavior.Behavior;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.el.MethodExpression;
import jakarta.el.ELException;
import jakarta.el.ValueExpression;
import jakarta.faces.event.ActionListener;
import jakarta.faces.event.SystemEvent;
import jakarta.faces.event.SystemEventListenerHolder;
import jakarta.faces.validator.Validator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TestServlet extends HttpTCKServlet {

  // ApplicationFactory.getApplication()
  // ApplicationFactory.setApplication(Application)
  public void applicationFactoryGetSetApplicationTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationFactory factory = (ApplicationFactory) FactoryFinder
        .getFactory(FactoryFinder.APPLICATION_FACTORY);

    if (factory == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unable to obtain ApplicationFactory!");
      return;
    }

    Application originalApplication = factory.getApplication();
    FacesContext context = getFacesContext();

    /*
     * since there is only one Application instance per webapp, the application
     * returned by the factory should be the same as that returned by
     * FacesContext.getApplication().
     */
    if (originalApplication != context.getApplication()) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "ApplicationFactory returned a different Application "
          + "instance then what is returned by "
          + "FacesContext.getApplication(). "
          + "There should only be on Application per webapp" + JSFTestUtil.NL
          + "Application instance from " + "ApplicationFactory: "
          + originalApplication + JSFTestUtil.NL
          + "Application instance from FacesContext: "
          + context.getApplication());
      return;
    }

    Application tckApplication = new TCKApplication();

    factory.setApplication(tckApplication);

    Application retApplication = factory.getApplication();

    if (!retApplication.equals(tckApplication)) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "ApplicationFactory.getApplication() "
          + "didn't return the Application previously set via "
          + "ApplicationFactory.setApplication(Application)." + JSFTestUtil.NL
          + "Expected Application: " + tckApplication + JSFTestUtil.NL
          + "Application received: " + retApplication);
      return;
    }
    factory.setApplication(originalApplication);
    out.println(JSFTestUtil.PASS);
  }

  public void applicationFactorySetApplicationNPETest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ApplicationFactory factory = (ApplicationFactory) FactoryFinder
        .getFactory(FactoryFinder.APPLICATION_FACTORY);

    if (factory == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unable to obtain ApplicationFactory!");
      return;
    }

    // ApplicationFactory.setApplication(null)
    JSFTestUtil.checkForNPE(factory, "setApplication",
        new Class<?>[] { Application.class }, new Object[] { null }, out);

  }

  public void applicationFactoryGetWrappedTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ApplicationFactory factory = (ApplicationFactory) FactoryFinder
        .getFactory(FactoryFinder.APPLICATION_FACTORY);

    if (!(factory.getWrapped() instanceof ApplicationFactory
        || factory.getWrapped() == null)) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "ApplicationFactpry.getWrapped() did not "
          + "return an instance of 'ApplicationFactory' or null");
      return;
    }

    out.println("Test PASSED.");

  }

  // --------------------------------------------- private implementations
  private static class TCKApplication extends Application {

    @Override
    public StateManager getStateManager() {
      return null;
    }

    @Override
    public ResourceBundle getResourceBundle(FacesContext facesContext,
        String string) {
      return null;
    }

    @Override
    public void addELResolver(ELResolver resolver) {
      // do nothing
    }

    @Override
    public ELResolver getELResolver() {
      return null;
    }

    @Override
    public UIComponent createComponent(ValueExpression componentExpression,
        FacesContext context, String componentType) throws FacesException {
      return null;
    }

    @Override
    public ExpressionFactory getExpressionFactory() {
      return null;
    }

    @Override
    public Object evaluateExpressionGet(FacesContext context, String expression,
        Class expectedType) throws ELException {
      return null;
    }

    @Override
    public void addELContextListener(ELContextListener listener) {
      // do nothing
    }

    @Override
    public void removeELContextListener(ELContextListener listener) {
      // do nothing
    }

    @Override
    public ELContextListener[] getELContextListeners() {
      return new ELContextListener[0];
    }

    @Override
    public void setStateManager(StateManager manager) {
      // do nothing
    }

    public MethodExpression createMethodExpression(String ref, Class params[])
        throws ELException {
      return null;
    }

    public ValueExpression createValueExpression(String ref)
        throws ELException {
      return null;
    }

    @Override
    public void setMessageBundle(String messageBundle) {
      // do nothing
    }

    @Override
    public String getMessageBundle() {
      return null;
    }

    @Override
    public Iterator getSupportedLocales() {
      return null;
    }

    @Override
    public void setSupportedLocales(Collection newLocales) {
      // do nothing
    }

    @Override
    public Locale getDefaultLocale() {
      return null;
    }

    @Override
    public void setDefaultLocale(Locale newLocale) {
      // do nothing
    }

    @Override
    public void addConverter(String converterId, String converterClass) {
      // do nothing
    }

    @Override
    public Iterator getConverterIds() {
      return null;
    }

    @Override
    public Iterator getConverterTypes() {
      return null;
    }

    @Override
    public ActionListener getActionListener() {
      return null;
    }

    @Override
    public void addConverter(Class targetClass, String converterClass) {
      // do nothing
    }

    @Override
    public void addComponent(String componentType, String componentClass) {
      // do nothing
    }

    @Override
    public UIComponent createComponent(String componentType)
        throws FacesException {
      return null;
    }

    @Override
    public void addValidator(String validatorId, String validatorClass) {
      // do nothing
    }

    @Override
    public Validator createValidator(String validatorId) throws FacesException {
      return null;
    }

    @Override
    public void setNavigationHandler(NavigationHandler handler) {
      // do nothing
    }

    public Converter createConverter(Class targetClass) {
      return null;
    }

    public ViewHandler getViewHandler() {
      return null;
    }

    @Override
    public Iterator getValidatorIds() {
      return null;
    }

    @Override
    public Converter createConverter(String converterId) {
      return null;
    }

    @Override
    public void setViewHandler(ViewHandler handler) {
      // do nothing
    }

    @Override
    public void setActionListener(ActionListener listener) {
      // do nothing
    }

    @Override
    public NavigationHandler getNavigationHandler() {
      return null;
    }

    @Override
    public Iterator getComponentTypes() {
      return null;
    }

    @Override
    public String getDefaultRenderKitId() {
      return null;
    }

    @Override
    public void setDefaultRenderKitId(String renderKitId) {
      // do nothing
    }

    public void publishEvent(Class<? extends SystemEvent> arg0,
        SystemEventListenerHolder arg1) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addBehavior(String arg0, String arg1) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Behavior createBehavior(String arg0) throws FacesException {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Iterator<String> getBehaviorIds() {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }
}
