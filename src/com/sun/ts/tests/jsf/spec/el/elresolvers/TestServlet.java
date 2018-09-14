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
package com.sun.ts.tests.jsf.spec.el.elresolvers;

import java.beans.FeatureDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.PropertyNotWritableException;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.PageContext;

import com.sun.ts.tests.jsf.common.resourcebundle.SimpleResourceBundle_de;
import com.sun.ts.tests.jsf.common.resourcebundle.SimpleResourceBundle_en;
import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public final class TestServlet extends HttpTCKServlet {

  private static final int FACES_PHASE = 0;

  private static final int JSP_PHASE = 1;

  private List<ImplicitObjectInfo> implicitInfo;

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
    implicitInfo = new ArrayList<ImplicitObjectInfo>();
    implicitInfo.add(new ImplicitObjectInfo("application", null, Object.class,
        FACES_PHASE, true));
    implicitInfo.add(
        new ImplicitObjectInfo("cookie", null, Map.class, FACES_PHASE, true));
    implicitInfo.add(
        new ImplicitObjectInfo("header", null, Map.class, FACES_PHASE, true));
    implicitInfo.add(new ImplicitObjectInfo("headerValues", null, Map.class,
        FACES_PHASE, true));
    implicitInfo.add(new ImplicitObjectInfo("initParam", null, Map.class,
        FACES_PHASE, true));
    implicitInfo.add(
        new ImplicitObjectInfo("param", null, Map.class, FACES_PHASE, true));
    implicitInfo.add(new ImplicitObjectInfo("paramValues", null, Map.class,
        FACES_PHASE, true));
    implicitInfo.add(new ImplicitObjectInfo("request", null, Object.class,
        FACES_PHASE, true));
    implicitInfo.add(new ImplicitObjectInfo("session", null, Object.class,
        FACES_PHASE, true));
    implicitInfo.add(new ImplicitObjectInfo("applicationScope", null, Map.class,
        FACES_PHASE, true));
    implicitInfo.add(new ImplicitObjectInfo("sessionScope", null, Map.class,
        FACES_PHASE, true));
    implicitInfo.add(new ImplicitObjectInfo("requestScope", null, Map.class,
        FACES_PHASE, true));

    // these will be check in both JSP and FACES phases

    implicitInfo.add(new ImplicitObjectInfo("facesContext", null,
        FacesContext.class, JSP_PHASE, true));
    implicitInfo.add(new ImplicitObjectInfo("view", null, UIViewRoot.class,
        JSP_PHASE, true));

  } // END init

  // ------------------------------------------------------------ Test Methods
  public void managedBeanELResolverFeatureDescriptorTest(
      HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    PrintWriter out = response.getWriter();
    ELContext[] elContexts = getELContexts(request, response);
    ELResolver[] resolvers = getELResolvers(elContexts);
    boolean passed = true;
    for (int i = 0; i < resolvers.length; i++) {

      out.println("Testing phase: " + getTestPhase(i));

      boolean nondescFound = false;
      boolean descFound = false;

      FeatureDescriptor control = new FeatureDescriptor();
      for (Iterator<?> j = resolvers[i].getFeatureDescriptors(elContexts[i],
          null); j.hasNext();) {

        FeatureDescriptor descriptor = (FeatureDescriptor) j.next();
        if ("nodescription".equals(descriptor.getName())) {
          nondescFound = true;

          control.setDisplayName("nodescription");
          control.setShortDescription("");
          control.setExpert(false);
          control.setHidden(false);
          control.setPreferred(true);
          control.setValue("type", java.util.Date.class);
          control.setValue("resolvable", Boolean.TRUE);

          String result = validateFeatureDescriptor(control, descriptor);
          if (result.length() > 0) {
            passed = false;
            out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
                + "Validating the feature descriptor for the "
                + "'nodescription' managed bean!" + JSFTestUtil.NL + result);
          }

          continue;
        }

        if ("description".equals(descriptor.getName())) {
          descFound = true;

          control.setDisplayName("description");
          control.setShortDescription("This is a description");
          control.setExpert(false);
          control.setHidden(false);
          control.setPreferred(true);
          control.setValue("type", java.util.Date.class);
          control.setValue("resolvable", Boolean.TRUE);

          String result = validateFeatureDescriptor(control, descriptor);
          if (result.length() > 0) {
            passed = false;
            out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
                + "Validating the feature descriptor for the "
                + "'description' managed bean" + JSFTestUtil.NL + result);
          }

          continue;
        }

        if (nondescFound && descFound) {
          break;
        }

      }
    }

    if (passed) {
      out.println(JSFTestUtil.PASS);
    }

  } // END managedBeanELResolverFeatureDescriptorTest

  public void facesImplicitObjectResolverFeatureDescriptorTest(
      HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    PrintWriter out = response.getWriter();
    ELContext[] elContexts = getELContexts(request, response);
    ELResolver[] resolvers = getELResolvers(elContexts);
    boolean passed = true;

    FeatureDescriptor control = new FeatureDescriptor();
    for (int i = 0, size = resolvers.length; i < size; i++) {

      boolean viewFound = false;
      boolean facesContextFound = false;
      boolean applicationFound = false;
      boolean applicationScopeFound = false;
      boolean cookieFound = false;
      boolean headerFound = false;
      boolean headerValuesFound = false;
      boolean initParamFound = false;
      boolean paramFound = false;
      boolean paramValuesFound = false;
      boolean requestFound = false;
      boolean requestScopeFound = false;
      boolean sessionFound = false;
      boolean sessionScopeFound = false;

      for (Iterator<?> j = resolvers[i].getFeatureDescriptors(elContexts[i],
          null); j.hasNext();) {

        FeatureDescriptor descriptor = (FeatureDescriptor) j.next();

        String phase;
        switch (i) {
        case 0:
          phase = "FACES_PHASE";
          break;
        case 1:
          phase = "JSP_PHASE";
          break;
        default:
          phase = "UNKNOWN";
          break;
        }

        out.println("*** Checking Phase: " + phase + " ***");
        out.println("*** Resolver Name: " + j.toString());
        out.println("*** Descriptor Name: " + descriptor.getName() + " ***");

        if ("view".equals(descriptor.getName())) {
          viewFound = true;
          control.setDisplayName("view");
          control.setShortDescription("");
          control.setExpert(false);
          control.setHidden(false);
          control.setPreferred(true);
          control.setValue("type", javax.faces.component.UIViewRoot.class);
          control.setValue("resolvable", Boolean.TRUE);

          String result = validateFeatureDescriptor(control, descriptor);
          if (result.length() > 0) {
            passed = false;
            out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
                + "validating the feature descriptor"
                + " for the 'view' implicit object");
            out.println(result);
            out.println("\n\n");
          }

          continue;
        }

        if ("facesContext".equals(descriptor.getName())) {
          facesContextFound = true;
          control.setDisplayName("facesContext");
          control.setShortDescription("");
          control.setExpert(false);
          control.setHidden(false);
          control.setPreferred(true);
          control.setValue("type", javax.faces.context.FacesContext.class);
          control.setValue("resolvable", Boolean.TRUE);

          String result = validateFeatureDescriptor(control, descriptor);
          if (result.length() > 0) {
            passed = false;
            out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
                + "validating the feature descriptor"
                + " for the 'facesContext' implicit object");
            out.println(result);
          }

          continue;
        }

        if (i == FACES_PHASE) {
          if ("application".equals(descriptor.getName())) {
            applicationFound = true;
            control.setDisplayName("application");
            control.setShortDescription("");
            control.setExpert(false);
            control.setHidden(false);
            control.setPreferred(true);
            control.setValue("type", java.lang.Object.class);
            control.setValue("resolvable", Boolean.TRUE);

            String result = validateFeatureDescriptor(control, descriptor);
            if (result.length() > 0) {
              passed = false;
              out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
                  + "validating the feature descriptor"
                  + " for the 'application' implicit object");
              out.println(result);
            }

            continue;
          }

          if ("applicationScope".equals(descriptor.getName())) {
            applicationScopeFound = true;
            control.setDisplayName("applicationScope");
            control.setShortDescription("");
            control.setExpert(false);
            control.setHidden(false);
            control.setPreferred(true);
            control.setValue("type", java.util.Map.class);
            control.setValue("resolvable", Boolean.TRUE);

            String result = validateFeatureDescriptor(control, descriptor);
            if (result.length() > 0) {
              passed = false;
              out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
                  + "validating the feature descriptor"
                  + " for the 'applicationScope' implicit object");
              out.println(result);
            }

            continue;
          }

          if ("cookie".equals(descriptor.getName())) {
            cookieFound = true;
            control.setDisplayName("cookie");
            control.setShortDescription("");
            control.setExpert(false);
            control.setHidden(false);
            control.setPreferred(true);
            control.setValue("type", java.util.Map.class);
            control.setValue("resolvable", Boolean.TRUE);

            String result = validateFeatureDescriptor(control, descriptor);
            if (result.length() > 0) {
              passed = false;
              out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
                  + "validating the feature descriptor"
                  + " for the 'cookie' implicit object");
              out.println(result);
            }

            continue;
          }

          if ("header".equals(descriptor.getName())) {
            headerFound = true;
            control.setDisplayName("header");
            control.setShortDescription("");
            control.setExpert(false);
            control.setHidden(false);
            control.setPreferred(true);
            control.setValue("type", java.util.Map.class);
            control.setValue("resolvable", Boolean.TRUE);

            String result = validateFeatureDescriptor(control, descriptor);
            if (result.length() > 0) {
              passed = false;
              out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
                  + "validating the feature descriptor"
                  + " for the 'header' implicit object");
              out.println(result);
            }

            continue;
          }

          if ("headerValues".equals(descriptor.getName())) {
            headerValuesFound = true;
            control.setDisplayName("headerValues");
            control.setShortDescription("");
            control.setExpert(false);
            control.setHidden(false);
            control.setPreferred(true);
            control.setValue("type", java.util.Map.class);
            control.setValue("resolvable", Boolean.TRUE);

            String result = validateFeatureDescriptor(control, descriptor);
            if (result.length() > 0) {
              passed = false;
              out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
                  + "validating the feature descriptor"
                  + " for the 'headerValues' implicit object");
              out.println(result);
            }

            continue;
          }

          if ("initParam".equals(descriptor.getName())) {
            initParamFound = true;
            control.setDisplayName("initParam");
            control.setShortDescription("");
            control.setExpert(false);
            control.setHidden(false);
            control.setPreferred(true);
            control.setValue("type", java.util.Map.class);
            control.setValue("resolvable", Boolean.TRUE);

            String result = validateFeatureDescriptor(control, descriptor);
            if (result.length() > 0) {
              passed = false;
              out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
                  + "validating the feature descriptor"
                  + " for the 'initParam' implicit object");
              out.println(result);
            }

            continue;
          }

          if ("param".equals(descriptor.getName())) {
            paramFound = true;
            control.setDisplayName("param");
            control.setShortDescription("");
            control.setExpert(false);
            control.setHidden(false);
            control.setPreferred(true);
            control.setValue("type", java.util.Map.class);
            control.setValue("resolvable", Boolean.TRUE);

            String result = validateFeatureDescriptor(control, descriptor);
            if (result.length() > 0) {
              passed = false;
              out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
                  + "validating the feature descriptor"
                  + " for the 'param' implicit object");
              out.println(result);
            }

            continue;
          }

          if ("paramValues".equals(descriptor.getName())) {
            paramValuesFound = true;
            control.setDisplayName("paramValues");
            control.setShortDescription("");
            control.setExpert(false);
            control.setHidden(false);
            control.setPreferred(true);
            control.setValue("type", java.util.Map.class);
            control.setValue("resolvable", Boolean.TRUE);

            String result = validateFeatureDescriptor(control, descriptor);
            if (result.length() > 0) {
              passed = false;
              out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
                  + "validating the feature descriptor"
                  + " for the 'paramValues' implicit object");
              out.println(result);
            }

            continue;
          }

          if ("request".equals(descriptor.getName())) {
            requestFound = true;
            control.setDisplayName("request");
            control.setShortDescription("");
            control.setExpert(false);
            control.setHidden(false);
            control.setPreferred(true);
            control.setValue("type", java.lang.Object.class);
            control.setValue("resolvable", Boolean.TRUE);

            String result = validateFeatureDescriptor(control, descriptor);
            if (result.length() > 0) {
              passed = false;
              out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
                  + "validating the feature descriptor"
                  + " for the 'request' implicit object");
              out.println(result);
            }

            continue;
          }

          if ("requestScope".equals(descriptor.getName())) {
            requestScopeFound = true;
            control.setDisplayName("requestScope");
            control.setShortDescription("");
            control.setExpert(false);
            control.setHidden(false);
            control.setPreferred(true);
            control.setValue("type", java.util.Map.class);
            control.setValue("resolvable", Boolean.TRUE);

            String result = validateFeatureDescriptor(control, descriptor);
            if (result.length() > 0) {
              passed = false;
              out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
                  + "validating the feature descriptor"
                  + " for the 'requestScope' implicit object");
              out.println(result);
            }

            continue;
          }

          if ("session".equals(descriptor.getName())) {
            sessionFound = true;
            control.setDisplayName("session");
            control.setShortDescription("");
            control.setExpert(false);
            control.setHidden(false);
            control.setPreferred(true);
            control.setValue("type", java.lang.Object.class);
            control.setValue("resolvable", Boolean.TRUE);

            String result = validateFeatureDescriptor(control, descriptor);
            if (result.length() > 0) {
              passed = false;
              out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
                  + "validating the feature descriptor"
                  + " for the 'session' implicit object");
              out.println(result);
            }

            continue;
          }

          if ("sessionScope".equals(descriptor.getName())) {
            sessionScopeFound = true;
            control.setDisplayName("sessionScope");
            control.setShortDescription("");
            control.setExpert(false);
            control.setHidden(false);
            control.setPreferred(true);
            control.setValue("type", java.util.Map.class);
            control.setValue("resolvable", Boolean.TRUE);

            String result = validateFeatureDescriptor(control, descriptor);
            if (result.length() > 0) {
              passed = false;
              out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
                  + "validating the feature descriptor"
                  + " for the 'sessionScope' implicit object");
              out.println(result);
            }

          }
        }
      }

      if (i == FACES_PHASE) {
        if (!(viewFound && facesContextFound && applicationFound
            && applicationScopeFound && requestFound && requestScopeFound
            && sessionFound && sessionScopeFound && cookieFound && headerFound
            && headerValuesFound && paramFound && paramValuesFound
            && initParamFound)) {
          passed = false;
          out.println(
              JSFTestUtil.FAIL + JSFTestUtil.NL + " One or more implicit object"
                  + "feature descriptors were not resolved when "
                  + "validating the Faces implicit object ELResolver");
          out.println("view: " + viewFound);
          out.println("facesContext: " + facesContextFound);
          out.println("application: " + applicationFound);
          out.println("applicationScope: " + applicationScopeFound);
          out.println("request: " + requestFound);
          out.println("requestScope: " + requestScopeFound);
          out.println("session: " + sessionFound);
          out.println("sessionScope: " + sessionScopeFound);
          out.println("header: " + headerFound);
          out.println("headerValues: " + headerValuesFound);
          out.println("param: " + paramFound);
          out.println("paramValues: " + paramValuesFound);
          out.println("cookie: " + cookieFound);
          out.println("initParam: " + initParamFound);
        }
      } else {
        if (!(viewFound && facesContextFound)) {
          passed = false;
          out.println(JSFTestUtil.FAIL + " One or more implicit object"
              + "feature descriptors (for JSP) were not resolved when "
              + "validating theFaces implicit object ELResolver");
          out.println("view: " + viewFound);
          out.println("facesContext: " + facesContextFound);
        }
      }
    }

    if (passed) {
      out.println(JSFTestUtil.PASS);
    }

  } // END facesImplicitObjectResolverForJSPFeatureDescriptorTest

  public void facesImplicitObjectResolverGetValueTest(
      HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    PrintWriter out = response.getWriter();
    ELContext[] elContexts = getELContexts(request, response);
    ELResolver[] resolvers = getELResolvers(elContexts);
    boolean passed = true;

    for (int i = 0; i < resolvers.length; i++) {

      out.println("Testing phase: " + getTestPhase(i));

      for (ImplicitObjectInfo aImplicitInfo : implicitInfo) {

        String result = null;
        if (i == JSP_PHASE) {
          if (aImplicitInfo.getPhase() == JSP_PHASE) {
            result = validateImplicitObjectValue(elContexts[i], resolvers[i],
                aImplicitInfo);
          }
        } else {
          result = validateImplicitObjectValue(elContexts[i], resolvers[i],
              aImplicitInfo);
        }

        if (result != null && result.length() > 0) {
          passed = false;
          out.println(result);
        }

        elContexts[i].setPropertyResolved(false);
      }
    }

    if (passed) {
      out.println(JSFTestUtil.PASS);
    }

  } // END facesImplicitObjectResolverGetValueTest

  public void facesImplicitObjectResolverGetTypeTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {

    PrintWriter out = response.getWriter();
    ELContext[] elContexts = getELContexts(request, response);
    ELResolver[] resolvers = getELResolvers(elContexts);
    boolean passed = true;

    for (int i = 0; i < resolvers.length; i++) {

      out.println("Testing phase: " + getTestPhase(i));

      for (ImplicitObjectInfo aImplicitInfo : implicitInfo) {

        String result = null;
        if (i == JSP_PHASE) {
          if (aImplicitInfo.getPhase() == JSP_PHASE) {
            result = validateImplicitObjectType(elContexts[i], resolvers[i],
                aImplicitInfo);
          }
        } else {
          result = validateImplicitObjectType(elContexts[i], resolvers[i],
              aImplicitInfo);
        }

        if (result != null && result.length() > 0) {
          passed = false;
          out.println(result);
        }

        elContexts[i].setPropertyResolved(false);
      }
    }

    if (passed) {
      out.println(JSFTestUtil.PASS);
    }

  } // END facesImplicitObjectResolverGetTypeTest

  public void facesImplicitObjectResolverSetValueTest(
      HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    PrintWriter out = response.getWriter();
    ELContext[] elContexts = getELContexts(request, response);
    ELResolver[] resolvers = getELResolvers(elContexts);
    boolean passed = true;

    for (int i = 0; i < resolvers.length; i++) {

      out.println("Testing phase: " + getTestPhase(i));

      for (ImplicitObjectInfo aImplicitInfo : implicitInfo) {

        String result = null;
        if (i == JSP_PHASE) {
          if (aImplicitInfo.getPhase() == JSP_PHASE) {
            result = validateImplicitObjectSetValue(elContexts[i], resolvers[i],
                aImplicitInfo);
          }
        } else {
          result = validateImplicitObjectSetValue(elContexts[i], resolvers[i],
              aImplicitInfo);
        }

        if (result != null && result.length() > 0) {
          passed = false;
          out.println(result);
        }
      }
    }

    if (passed) {
      out.println(JSFTestUtil.PASS);
    }

  } // END facesImplicitObjectResolverSetValueTest

  public void facesImplicitObjectResolverIsReadOnlyTest(
      HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    PrintWriter out = response.getWriter();
    ELContext[] elContexts = getELContexts(request, response);
    ELResolver[] resolvers = getELResolvers(elContexts);
    boolean passed = true;

    for (int i = 0; i < resolvers.length; i++) {

      out.println("Testing phase: " + getTestPhase(i));

      for (ImplicitObjectInfo aImplicitInfo : implicitInfo) {

        String result = null;
        if (i == JSP_PHASE) {
          if (aImplicitInfo.getPhase() == JSP_PHASE) {
            result = validateImplicitObjectReadOnly(elContexts[i], resolvers[i],
                aImplicitInfo);
          }
        } else {
          result = validateImplicitObjectReadOnly(elContexts[i], resolvers[i],
              aImplicitInfo);
        }

        if (result != null && result.length() > 0) {
          passed = false;
          out.println(result);
        }
      }
    }

    if (passed) {
      out.println(JSFTestUtil.PASS);
    }

  } // END facesImplicitObjectResolverIsReadOnlyTest

  public void facesManagedBeanResolverGetValueTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {

    PrintWriter out = response.getWriter();
    ELContext[] elContexts = getELContexts(request, response);
    ELResolver[] resolvers = getELResolvers(elContexts);
    String attributeName = "description";

    for (int i = 0, size = resolvers.length; i < size; i++) {

      out.println("Testing phase: " + getTestPhase(i));

      /*
       * If an object is associated with the name "description" in either
       * request, session, or application return that object (searching in that
       * order)
       */
      ExternalContext extContext = getFacesContext().getExternalContext();
      extContext.getRequestMap().put(attributeName, "request");
      extContext.getSessionMap().put(attributeName, "session");
      extContext.getApplicationMap().put(attributeName, "application");

      String result = null;
      try {
        result = (String) resolvers[i].getValue(elContexts[i], null,
            attributeName);

      } catch (ClassCastException cce) {
        out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
            + "Should be getting a 'String' back from Request "
            + "Scope! Instead of 'Date' from Managedbean!");
        return;
      }

      if (!"request".equals(result)) {
        out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
            + "Expected managed bean resolver to search request "
            + "scope first, but instead it searched " + result);
        return;
      }

      // Remove Attribute from Request Scope.
      extContext.getRequestMap().remove(attributeName);

      try {
        result = (String) resolvers[i].getValue(elContexts[i], null,
            attributeName);

      } catch (ClassCastException cce) {
        out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
            + "Should be getting a 'String' back from Session "
            + "Scope! Instead of 'Date' from Managedbean!");
        return;
      }

      if (!"session".equals(result)) {
        out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
            + "Expected managed bean resolver to search "
            + "session scope first, but instead it searched " + result);
        return;
      }

      // Remove Attribute from Session Scope.
      extContext.getSessionMap().remove(attributeName);

      try {
        result = (String) resolvers[i].getValue(elContexts[i], null,
            attributeName);

      } catch (ClassCastException e) {
        out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
            + "Should be getting a 'String' back from Application "
            + "Scope! Instead of 'Date' from Managedbean!");
        return;
      }

      if (!"application".equals(result)) {
        out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
            + "Expected managed bean resolver to search "
            + "application scope first, but instead it searched " + result);
        return;
      }

      // Remove attribute from Application Scope.
      extContext.getApplicationMap().remove(attributeName);

      Date date = (Date) resolvers[i].getValue(elContexts[i], null,
          attributeName);

      if (date == null) {
        out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
            + "ELResolver.getValue() with a null "
            + "base and a property matching the name of a managed "
            + "bean did not result in the bean being instantiated "
            + "and returned.");
        return;
      }

      if (!elContexts[i].isPropertyResolved()) {
        out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
            + "A new managed bean was created, "
            + "but isPropertyResolved() returned false.");
        return;
      }
    }

    out.println(JSFTestUtil.PASS);

  } // END facesManagedBeanResolverGetValueTest

  public void facesArrayListMapBeanResolverTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {

    // This test ensures that the List, Map, and Bean ELResolvers
    // are present in the ELResolver chain returned by the faces
    // implementation.

    PrintWriter out = response.getWriter();
    ELContext elContext = getFacesContext().getELContext();
    ELResolver resolver = elContext.getELResolver();

    ArrayList<String> list = new ArrayList<String>();
    list.add("val1");
    list.add("val2");

    HashMap<String, String> map = new HashMap<String, String>();
    map.put("key1", "value1");
    map.put("key2", "value2");

    Date date = new Date();
    Long longResult = date.getTime();

    String[] array = new String[] { "str1", "str2" };

    // begin test

    boolean passed = true;

    Object result = resolver.getValue(elContext, list, "0");
    if (!"val1".equals(result)) {
      passed = false;
      out.println(
          JSFTestUtil.FAIL + " ELResolver failed to handle a List properly"
              + " which indicates that either the ListELResolver isn't present"
              + " or it is present and not implemented correctly.");
      out.println("Expected resolver to return 'val1', recevied: " + result);
    }

    elContext.setPropertyResolved(false);

    result = resolver.getValue(elContext, map, "key1");
    if (!"value1".equals(result)) {
      passed = false;
      out.println(
          JSFTestUtil.FAIL + " ELResolver failed to handle a Map properly"
              + " which indicates that either the MapELResolver isn't present"
              + " or it is present and not implemented correctly.");
      out.println("Expected resolver to return 'value1', recevied: " + result);
    }

    elContext.setPropertyResolved(false);

    result = resolver.getValue(elContext, date, "time");
    if (!longResult.equals(result)) {
      passed = false;
      out.println(
          JSFTestUtil.FAIL + " ELResolver failed to handle a Bean properly"
              + " which indicates that either the BeanELResolver isn't present"
              + " or it is present and not implemented correctly.");
      out.println("Expected resolver to return '" + longResult.toString()
          + "', recevied: '" + result.toString() + '\'');
    }

    elContext.setPropertyResolved(false);

    result = resolver.getValue(elContext, array, "0");
    if (!"str1".equals(result)) {
      passed = false;
      out.println(
          JSFTestUtil.FAIL + " ELResolver failed to handle an Array properly"
              + " which indicates that either the ArrayELResolver isn't present"
              + " or it is present and not implemented correctly.");
      out.println("Expected resolver to return 'str1', recevied: " + result);
    }

    if (passed) {
      out.println(JSFTestUtil.PASS);
    }

  } // END facesArrayListMapBeanResolverTest

  public void facesScopedAttributeResolverGetValueTest(
      HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    PrintWriter out = response.getWriter();
    ELContext elContext = getFacesContext().getELContext();
    ELResolver resolver = elContext.getELResolver();
    ExternalContext extContext = getFacesContext().getExternalContext();
    boolean bOne, bTwo, bThree = false;

    // Populate the scopes.
    extContext.getRequestMap().put("desc", "request");
    extContext.getSessionMap().put("desc", "session");
    extContext.getApplicationMap().put("desc", "application");

    // Test Request scope
    bOne = this.testELGetValue("request", resolver, elContext, out);
    extContext.getRequestMap().remove("desc");
    elContext.setPropertyResolved(false);

    // Test Session scope
    bTwo = this.testELGetValue("session", resolver, elContext, out);
    extContext.getSessionMap().remove("desc");
    elContext.setPropertyResolved(false);

    // Test Application scope
    bThree = this.testELGetValue("application", resolver, elContext, out);

    if (bOne && bTwo && bThree) {
      out.println(JSFTestUtil.PASS);
    }

  } // END facesScopedAttributeResolverGetValueTest

  public void facesScopedAttributeResolverSetValueTest(
      HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    PrintWriter out = response.getWriter();
    ELContext elContext = getFacesContext().getELContext();
    ELResolver resolver = elContext.getELResolver();
    ExternalContext extContext = getFacesContext().getExternalContext();
    boolean passed = true;

    Map<String, Object> reqMap = extContext.getRequestMap();
    Map<String, Object> sesMap = extContext.getSessionMap();
    Map<String, Object> appMap = extContext.getApplicationMap();

    reqMap.put("desc", "request");
    sesMap.put("desc", "session");
    appMap.put("desc", "application");

    resolver.setValue(elContext, null, "desc", "replaced");
    if (!"replaced".equals(reqMap.get("desc"))) {
      passed = false;
      out.println("ELResolver.setValue() failed to replace existing"
          + " request scoped attribute.");
      out.println("Expected value to be: 'replaced', received: '"
          + reqMap.get("desc") + '\'');
    } else {
      if (!"session".equals(sesMap.get("desc"))) {
        passed = false;
        out.println("Test FAILED. ELResolver incorrectly replaced "
            + "session scoped attribute when only the "
            + "request scoped attribute should have been replaced");
      }
      if (!"application".equals(appMap.get("desc"))) {
        passed = false;
        out.println("Test FAILED. ELResolver incorrectly replaced "
            + "application scoped attribute when only the "
            + "request scoped attribute should have been replaced");
      }
      if (!elContext.isPropertyResolved()) {
        passed = false;
        out.println(
            JSFTestUtil.FAIL + " Request scoped attribute properly replaced, "
                + "but the propertyResolved property on the ELContext was"
                + " not set to true");
      }
    }

    reqMap.remove("desc");
    elContext.setPropertyResolved(false);

    resolver.setValue(elContext, null, "desc", "replaced");
    if (!"replaced".equals(sesMap.get("desc"))) {
      passed = false;
      out.println("ELResolver.setValue() failed to replace existing"
          + " session scoped attribute.");
      out.println("Expected value to be: 'replaced', received: '"
          + reqMap.get("desc") + '\'');
    } else {
      if (reqMap.get("desc") != null) {
        passed = false;
        out.println("Test FAILED. ELResolver incorrectly added a "
            + "request scoped attribute when only the "
            + "session scoped attribute should have been replaced");
      }
      if (!"application".equals(appMap.get("desc"))) {
        passed = false;
        out.println("Test FAILED. ELResolver incorrectly replaced "
            + "application scoped attribute when only the "
            + "session scoped attribute should have been replaced");
      }
      if (!elContext.isPropertyResolved()) {
        passed = false;
        out.println(
            JSFTestUtil.FAIL + " session scoped attribute properly replaced, "
                + "but the propertyResolved property on the ELContext was"
                + " not set to true");
      }
    }

    sesMap.remove("desc");
    elContext.setPropertyResolved(false);

    resolver.setValue(elContext, null, "desc", "replaced");
    if (!"replaced".equals(appMap.get("desc"))) {
      passed = false;
      out.println("ELResolver.setValue() failed to replace existing"
          + " application scoped attribute.");
      out.println("Expected value to be: 'replaced', received: '"
          + reqMap.get("desc") + '\'');
    } else {
      if (reqMap.get("desc") != null) {
        passed = false;
        out.println("Test FAILED. ELResolver incorrectly added a "
            + "request scoped attribute when only the "
            + "application scoped attribute should have been replaced");
      }
      if (sesMap.get("desc") != null) {
        passed = false;
        out.println("Test FAILED. ELResolver incorrectly added a "
            + "session scoped attribute when only the "
            + "application scoped attribute should have been replaced");
      }
      if (!elContext.isPropertyResolved()) {
        passed = false;
        out.println(JSFTestUtil.FAIL
            + " application scoped attribute properly replaced, "
            + "but the propertyResolved property on the ELContext was"
            + " not set to true");
      }
    }

    appMap.remove("desc");
    elContext.setPropertyResolved(false);
    resolver.setValue(elContext, null, "desc", "newValue");
    if (!"newValue".equals(reqMap.get("desc"))) {
      passed = false;
      out.println("ELResolver.setValue() failed to add a new"
          + " request scoped attribute.");
      out.println("Expected value to be: 'newValue', received: '"
          + reqMap.get("desc") + '\'');
    } else {
      if (sesMap.get("desc") != null) {
        passed = false;
        out.println("Test FAILED. ELResolver incorrectly added a "
            + "session scoped attribute when only the "
            + "request scoped attribute should have been added");
      }
      if (appMap.get("desc") != null) {
        passed = false;
        out.println("Test FAILED. ELResolver incorrectly added an "
            + "application scoped attribute when only the "
            + "request scoped attribute should have been added");
      }
      if (!elContext.isPropertyResolved()) {
        passed = false;
        out.println(
            JSFTestUtil.FAIL + " request scoped attribute properly added, "
                + "but the propertyResolved property on the ELContext was"
                + " not set to true");
      }
    }

    if (passed) {
      out.println(JSFTestUtil.PASS);
    }

  } // END facesScopedAttributeResolverSetValueTest

  public void facesScopedAttributeResolverFeatureDescriptorTest(
      HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    PrintWriter out = response.getWriter();
    ELContext elContext = getFacesContext().getELContext();
    ELResolver resolver = elContext.getELResolver();
    ExternalContext extContext = getFacesContext().getExternalContext();
    boolean passed = true;

    Map<String, Object> reqMap = extContext.getRequestMap();
    Map<String, Object> sesMap = extContext.getSessionMap();
    Map<String, Object> appMap = extContext.getApplicationMap();

    // ensure we have at least 1 in each scope
    reqMap.put("req", "value1");
    sesMap.put("ses", "value1");
    appMap.put("app", "value1");

    HashMap<Object, Class<? extends Object>> names = new HashMap<Object, Class<? extends Object>>();
    HashMap<String, FeatureDescriptor> controlDescriptors = new HashMap<String, FeatureDescriptor>();

    for (Object key : reqMap.keySet()) {
      names.put(key, reqMap.get(key).getClass());
    }

    for (Object key : sesMap.keySet()) {
      names.put(key, sesMap.get(key).getClass());
    }

    for (Object key : appMap.keySet()) {
      names.put(key, appMap.get(key).getClass());
    }

    // next build a map of 'control' FeatureDescriptors keyed off the name
    for (Map.Entry entry : names.entrySet()) {
      String name = (String) entry.getKey();
      Class clazz = (Class) entry.getValue();
      FeatureDescriptor descriptor = new FeatureDescriptor();
      descriptor.setName(name);
      descriptor.setDisplayName(name);
      descriptor.setValue("type", clazz);
      descriptor.setValue("resolvable", Boolean.TRUE);
      descriptor.setExpert(false);
      descriptor.setHidden(false);
      descriptor.setPreferred(true);
      descriptor.setShortDescription("");
      controlDescriptors.put(name, descriptor);
    }

    // begin test test
    for (Iterator i = resolver.getFeatureDescriptors(elContext, null); i
        .hasNext();) {

      FeatureDescriptor underTest = (FeatureDescriptor) i.next();

      String name = underTest.getName();
      if (controlDescriptors.containsKey(name)) {
        String result = validateFeatureDescriptor(controlDescriptors.get(name),
            underTest);

        if (result.length() > 0) {
          passed = false;
          out.println("Test FAILED when validating the FeatureDescriptor"
              + " for the '" + name + "' scoped attribute.");
          out.println(result);
        }
      }
    }

    reqMap.remove("req");
    sesMap.remove("ses");
    appMap.remove("app");

    if (passed) {
      out.println(JSFTestUtil.PASS);
    }

  } // END facesScopedAttributeResolverFeatureDescriptorTest

  // ------------------------------------------------ ResourceBundleELResolver
  public void facesResourceBundleResolverGetValueTest(
      HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    PrintWriter out = response.getWriter();
    ELContext elContext = getFacesContext().getELContext();
    ELResolver resolver = elContext.getELResolver();
    boolean passed = true;

    getFacesContext().getViewRoot().setLocale(Locale.ENGLISH);
    SimpleResourceBundle_en result = (SimpleResourceBundle_en) resolver
        .getValue(elContext, null, "simplerb");

    if (result == null) {
      passed = false;
      out.println(JSFTestUtil.FAIL + " Unable to resolve defined "
          + "ResourceBundle 'simplerb'");
    } else {
      if (!elContext.isPropertyResolved()) {
        passed = false;
        out.println(JSFTestUtil.FAIL + " ResourceBundle resolved, but"
            + " the propertyResolved property of the "
            + " ELContext was not set to true.");
      }
    }

    getFacesContext().getViewRoot().setLocale(Locale.GERMAN);
    elContext.setPropertyResolved(false);
    SimpleResourceBundle_de result2 = (SimpleResourceBundle_de) resolver
        .getValue(elContext, null, "simplerb");

    if (result2 == null) {
      passed = false;
      out.println(JSFTestUtil.FAIL + " Unable to resolve defined "
          + "ResourceBundle(DE) 'simplerb'");
    } else {
      if (!elContext.isPropertyResolved()) {
        passed = false;
        out.println(JSFTestUtil.FAIL + " ResourceBundle(DE) resolved, but"
            + " the propertyResolved property of the "
            + " ELContext was not set to true.");
      }
    }

    if (passed) {
      out.println(JSFTestUtil.PASS);
    }
  }

  public void facesResourceBundleResolverGetTypeTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {

    PrintWriter out = response.getWriter();
    ELContext elContext = getFacesContext().getELContext();
    ELResolver resolver = elContext.getELResolver();
    boolean passed = true;

    Class type = resolver.getType(elContext, null, "simplerb");

    if (type != ResourceBundle.class) {
      passed = false;
      out.println(JSFTestUtil.FAIL + " Unexpected type '" + type
          + "' returned by getType().  Expected "
          + "java.util.ResourceBundle.class");
    } else {
      if (!elContext.isPropertyResolved()) {
        passed = false;
        out.println(JSFTestUtil.FAIL + " Correct type returned, but"
            + " the propertyResolved property of the "
            + " ELContext was not set to true.");
      }
    }

    if (passed) {
      out.println(JSFTestUtil.PASS);
    }

  } // END facesResourceBundleResolverGetTypeTest

  public void facesResourceBundleResolverSetValueTest(
      HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    PrintWriter out = response.getWriter();
    ELContext elContext = getFacesContext().getELContext();
    ELResolver resolver = elContext.getELResolver();
    boolean passed = true;

    try {
      resolver.setValue(elContext, null, "simplerb", "someValue");
      passed = false;
      out.println(JSFTestUtil.FAIL + " No Exception thrown when"
          + " attemtping to call setValue() in the case where"
          + " the resolved property is a ResourceBundle.");
    } catch (Exception e) {
      if (!(e instanceof PropertyNotWritableException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when attempting"
            + " to call setValue() in the case where"
            + " the resolved property is a ResourceBundle, but"
            + " it wasn't an instance of" + " PropertyNotWriteableException.");
        passed = false;
      }
    }

    if (passed) {
      out.println(JSFTestUtil.PASS);
    }

  } // END facesResourceBundleResolverSetValueTest

  public void facesResourceBundleResolverIsReadOnlyTest(
      HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    PrintWriter out = response.getWriter();
    ELContext elContext = getFacesContext().getELContext();
    ELResolver resolver = elContext.getELResolver();
    boolean passed = true;

    if (!resolver.isReadOnly(elContext, null, "simplerb")) {
      out.println(JSFTestUtil.FAIL + " Expected isReadOnly() to return"
          + " true when the resolved property is a " + " ResourceBundle.");
      passed = false;
    } else {
      if (!elContext.isPropertyResolved()) {
        out.println(JSFTestUtil.FAIL + " isReadOnly() returned the correct"
            + " value, but the propertyResolved property of the"
            + " ELContext was not set to true.");
        passed = false;
      }
    }

    if (passed) {
      out.println(JSFTestUtil.PASS);
    }

  } // END facesResourceBundleResolverIsReadOnlyTest

  public void facesResourceBundleResolverFeatureDescriptorTest(
      HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    PrintWriter out = response.getWriter();
    ELContext elContext = getFacesContext().getELContext();
    ELResolver resolver = elContext.getELResolver();
    boolean fd_Found = false;

    // Setup golden FeatureDescriptor.
    FeatureDescriptor controlDesc = new FeatureDescriptor();
    controlDesc.setValue("resolvable", Boolean.TRUE);
    controlDesc.setValue("type", ResourceBundle.class);
    controlDesc.setName("simplerb");
    controlDesc.setDisplayName("simple");
    controlDesc.setExpert(false);
    controlDesc.setHidden(false);
    controlDesc.setPreferred(true);
    controlDesc.setShortDescription("");

    for (Iterator i = resolver.getFeatureDescriptors(elContext, null); i
        .hasNext();) {
      FeatureDescriptor test = (FeatureDescriptor) i.next();
      //
      // out.println("DEBUG ===> FeatureDescriptor Name: " +
      // test.getName() + NL);

      if ("simplerb".equals(test.getName())) {
        String result = validateFeatureDescriptor(controlDesc, test);
        fd_Found = true;

        if (result.length() != 0) {
          out.println(result);
        }
        break;
      }
    }

    if (fd_Found) {
      out.println(JSFTestUtil.PASS);
    } else {
      out.println(
          "Test FAILED! Could not find FeatureDescriptor with name of 'simplerb'");
    }

  } // END facesResourceBundleResolverFeatureDescriptorTest

  public void facesConfigELResolverRegistrationTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {

    PrintWriter out = response.getWriter();
    ELContext elContext = getFacesContext().getELContext();
    ELResolver resolver = elContext.getELResolver();

    Collection<String> base = new ArrayList<String>();
    base.add("value1");
    base.add("value2");
    base.add("value3");

    try {
      Integer result = (Integer) resolver.getValue(elContext, base, "size");

      if (result == null) {
        out.println(JSFTestUtil.FAIL + " Custom resolver not called.");
        return;
      }

      if (!"3".equals(result.toString())) {
        out.println(JSFTestUtil.FAIL + " Unexpected result returned;"
            + " custom resolver not called.");
        return;
      }
    } catch (Exception e) {
      out.println(
          "Test FAILED Unexpected exception thrown " + "during test: " + e);
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // END facesConfigELResolverRegistrationTest

  // --------------------------------------------------------- Private Methods
  private boolean testELGetValue(String scope, ELResolver resolver,
      ELContext context, PrintWriter out) {

    boolean passed = true;

    String result = (String) resolver.getValue(context, null, "desc");

    if (!scope.equals(result)) {
      passed = false;
      out.println(
          JSFTestUtil.FAIL + " Expected managed bean resolver to " + "search "
              + scope + " scope first, but instead it " + "searched " + result);
    } else {
      if (!context.isPropertyResolved()) {
        passed = false;
        out.println(JSFTestUtil.FAIL + " ELResolver failed to set "
            + "propertyResolved to true after resolving a " + scope
            + "scoped attribute.");
      }
    }

    return passed;
  }

  private static String validateFeatureDescriptor(FeatureDescriptor control,
      FeatureDescriptor underTest) {

    StringBuffer sb = new StringBuffer(64);

    if (!control.getDisplayName().equals(underTest.getDisplayName())) {
      sb.append("\tExpected displayName to be '");
      sb.append(control.getDisplayName());
      sb.append("', received: '");
      sb.append(underTest.getDisplayName()).append("'\n");
    }

    if (!control.getValue("type").equals(underTest.getValue(ELResolver.TYPE))) {
      sb.append("\tExpected ELResolver.TYPE property to return ");
      sb.append(control.getValue("type")).append(" (type is ");
      sb.append(control.getValue("type").getClass().getName());
      sb.append("), received: ");
      sb.append(underTest.getValue(ELResolver.TYPE));
      sb.append(" (type is ");
      sb.append(underTest.getValue(ELResolver.TYPE).getClass().getName());
      sb.append(")\n");
    }

    if (!control.getValue("resolvable")
        .equals(underTest.getValue(ELResolver.RESOLVABLE_AT_DESIGN_TIME))) {
      sb.append("\tExpected ELResolver.RESOLVABLE_AT_DESIGN_TYPE");
      sb.append("to be ").append(control.getValue("resolvable"));
      sb.append(", received: ");
      sb.append(underTest.getValue(ELResolver.RESOLVABLE_AT_DESIGN_TIME));
      sb.append('\n');
    }

    if (control.getShortDescription().length() == 0) {
      if (underTest.getShortDescription() == null) {
        sb.append("\tExpected a non-null description.\n");
      }
    } else {
      if (!control.getShortDescription()
          .equals(underTest.getShortDescription().trim())) {
        sb.append("\tExpected description to be '");
        sb.append(control.getShortDescription()).append("', ");
        sb.append("received: '").append(underTest.getShortDescription());
        sb.append("'\n");
      }
    }

    if (control.isExpert() != underTest.isExpert()) {
      sb.append("\tExpected expert property to be ");
      sb.append(control.isExpert()).append('\n');
    }

    if (control.isHidden() != underTest.isHidden()) {
      sb.append("\tExpected hidden property to be ");
      sb.append(control.isHidden()).append('\n');
    }

    if (control.isPreferred() != underTest.isPreferred()) {
      sb.append("\tExpected preferred property to be ");
      sb.append(control.isPreferred()).append('\n');
    }

    return sb.toString();

  } // END validateFeatureDescriptor

  /**
   * @return return a two element array with the Faces ELResolver as the first
   *         element, and the JSP ELResolver as the second
   */
  private ELResolver[] getELResolvers(ELContext[] contexts) {

    ELResolver[] resolvers = new ELResolver[2];
    resolvers[FACES_PHASE] = contexts[FACES_PHASE].getELResolver();
    resolvers[JSP_PHASE] = contexts[JSP_PHASE].getELResolver();

    return resolvers;

  } // END getELResolvers

  /**
   * @return return a two element array with the Faces ELContext as the first
   *         element, and the JSP ELContext as the second
   */
  private ELContext[] getELContexts(HttpServletRequest request,
      HttpServletResponse response) {

    ELContext[] contexts = new ELContext[2];
    contexts[FACES_PHASE] = getFacesContext().getELContext();

    PageContext context = JspFactory.getDefaultFactory().getPageContext(this,
        request, response, null, true, 1024, true);
    contexts[JSP_PHASE] = context.getELContext();

    return contexts;

  } // END getELContexts

  private static String getTestPhase(int phase) {

    if (phase < FACES_PHASE || phase > JSP_PHASE) {
      throw new IllegalArgumentException("Unknown test phase");
    }
    if (phase == FACES_PHASE) {
      return "[FACES]";
    } else {
      return "[JSP]";
    }

  } // END getTestPhase

  private static String validateImplicitObjectValue(ELContext context,
      ELResolver resolver, ImplicitObjectInfo info) {

    StringBuffer sb = new StringBuffer(64);
    String name = info.getName();
    Object result = resolver.getValue(context, null, name);

    if (!info.getValueType().isAssignableFrom(result.getClass())) {
      sb.append(JSFTestUtil.FAIL + " Passing a null base, and '");
      sb.append(name).append("' as the property");
      sb.append(" didn't result getValue() returning an instance of ");
      sb.append(info.getValueType().getName());
      sb.append("\nObject received: ").append(result.getClass().getName());
    } else {
      if (!context.isPropertyResolved()) {
        sb.append(JSFTestUtil.FAIL + " ELResolver.getValue() ");
        sb.append("property resolved the '").append(name);
        sb.append("' implicit object, but it didn't call");
        sb.append(" ELContext.setPropertyResolved(true).");
      }
    }

    return sb.toString();

  } // END validateImplicitObjectValue

  private static String validateImplicitObjectSetValue(ELContext context,
      ELResolver resolver, ImplicitObjectInfo info) {

    StringBuffer sb = new StringBuffer();
    try {
      resolver.setValue(context, null, info.getName(), "value");
      sb.append(
          JSFTestUtil.FAIL + " No exception thrown when calling setValue()");
      sb.append(" with the base parameter set to null, and the property");
      sb.append(" parameter set to '").append(info.getName());
      sb.append("'.");
    } catch (PropertyNotWritableException pnwe) {
      // we're good to go
    } catch (Exception e) {
      sb.append(
          JSFTestUtil.FAIL + " Expected a PropertyNotWritableException to");
      sb.append(
          " be thrown when calling setValue() with a null base parameter");
      sb.append(" and a property parameter set to '");
      sb.append(info.getName()).append("'.\n");
      sb.append("Exception received: ").append(e.getClass().getName());
    }

    if (context.isPropertyResolved()) {
      sb.append(JSFTestUtil.FAIL + " propertyResolved property on ELContext");
      sb.append(" was set to true even though property was not writable.");
    }

    return sb.toString();
  }

  private static String validateImplicitObjectReadOnly(ELContext context,
      ELResolver resolver, ImplicitObjectInfo info) {

    StringBuffer sb = new StringBuffer();
    String name = info.getName();
    if (!resolver.isReadOnly(context, null, name)) {
      sb.append(JSFTestUtil.FAIL + " Expected isReadOnly() to return true");
      sb.append(" for implicit object '").append(name).append("'.");
    }

    if (!context.isPropertyResolved()) {
      sb.append(
          JSFTestUtil.FAIL + " isReadOnly() returned the expected result");
      sb.append(" but didn't set the propertyResolved property of the");
      sb.append(" ELContext to true.");
    }

    return sb.toString();

  }

  private static String validateImplicitObjectType(ELContext context,
      ELResolver resolver, ImplicitObjectInfo info) {

    StringBuffer sb = new StringBuffer();
    String name = info.getName();
    Object result = resolver.getType(context, null, name);
    if (info.getType() == null) {
      if (result != null) {

        sb.append(
            JSFTestUtil.FAIL + " Expected getType() to return null, when");
        sb.append(" the base parameter was null and the property parameter ");
        sb.append("was '").append(name).append('\'');
        sb.append("\nReceived: ").append(result);
      } else {
        if (!context.isPropertyResolved()) {

          sb.append(JSFTestUtil.FAIL + " ELResolver.getType() returned the");
          sb.append(" the the expected value for the '").append(name);
          sb.append("' implicit object, but it didn't call");
          sb.append(" ELContext.setPropertyResolved(true).");
        }
      }
    } else {
      if (!info.getType().equals(result)) {
        sb.append(JSFTestUtil.FAIL + " Expected getType() to return '");
        sb.append(info.getType().getName()).append("' when");
        sb.append(" the base parameter was null and the property");
        sb.append(" parameter was '").append(name).append('\'');
        sb.append("\nReceived: ").append(result);
      } else {
        if (!context.isPropertyResolved()) {

          sb.append(JSFTestUtil.FAIL + " ELResolver.getType() returned the");
          sb.append(" the the expected value for the '").append(name);
          sb.append("' implicit object, but it didn't call");
          sb.append(" ELContext.setPropertyResolved(true).");
        }
      }
    }

    return sb.toString();

  } // END validateImplicitObjectType

  // ---------------------------------------------------------- Nested Classes
  private static class ImplicitObjectInfo {

    private String name;

    private Class type;

    private int phase;

    private boolean isReadOnly;

    private Class valueType;

    ImplicitObjectInfo(String name, Class type, Class valueType, int phase,
        boolean isReadOnly) {

      this.name = name;
      this.type = type;
      this.valueType = valueType;
      this.phase = phase;
      this.isReadOnly = isReadOnly;

    } // END ImplicitObjectInfo

    public String getName() {

      return name;

    } // END getName

    public Class getType() {

      return type;

    } // END getType

    public int getPhase() {

      return phase;

    } // END getPhase

    public boolean isReadOnly() {

      return isReadOnly;

    } // END isReadOnly

    public Class getValueType() {

      return valueType;

    } // END getValueType
  } // END ImplicitObjectInfo
}
