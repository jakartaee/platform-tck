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

package com.sun.ts.tests.jsf.api.javax_faces.factoryfinder.factoryfinder;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.render.RenderKitFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import javax.faces.component.search.SearchExpressionContextFactory;
import javax.faces.component.visit.VisitContextFactory;
import javax.faces.context.ExceptionHandlerFactory;
import javax.faces.context.ExternalContextFactory;
import javax.faces.context.FlashFactory;
import javax.faces.context.PartialViewContextFactory;
import javax.faces.flow.FlowHandlerFactory;
import javax.faces.lifecycle.ClientWindowFactory;
import javax.faces.view.ViewDeclarationLanguageFactory;
import javax.faces.view.facelets.FaceletCacheFactory;
import javax.faces.view.facelets.TagHandlerDelegateFactory;

public class TestServlet extends HttpTCKServlet {

  public void getFactoryTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    String[] factoryTypes = { FactoryFinder.APPLICATION_FACTORY,
        FactoryFinder.CLIENT_WINDOW_FACTORY,
        FactoryFinder.EXCEPTION_HANDLER_FACTORY,
        FactoryFinder.EXTERNAL_CONTEXT_FACTORY,
        FactoryFinder.FACELET_CACHE_FACTORY,
        FactoryFinder.FACES_CONTEXT_FACTORY, FactoryFinder.FLASH_FACTORY,
        FactoryFinder.FLOW_HANDLER_FACTORY, FactoryFinder.LIFECYCLE_FACTORY,
        FactoryFinder.PARTIAL_VIEW_CONTEXT_FACTORY,
        FactoryFinder.RENDER_KIT_FACTORY,
        FactoryFinder.SEARCH_EXPRESSION_CONTEXT_FACTORY,
        FactoryFinder.TAG_HANDLER_DELEGATE_FACTORY,
        FactoryFinder.VIEW_DECLARATION_LANGUAGE_FACTORY,
        FactoryFinder.VISIT_CONTEXT_FACTORY };

    Class[] classes = { ApplicationFactory.class, ClientWindowFactory.class,
        ExceptionHandlerFactory.class, ExternalContextFactory.class,
        FaceletCacheFactory.class, FacesContextFactory.class,
        FlashFactory.class, FlowHandlerFactory.class, LifecycleFactory.class,
        PartialViewContextFactory.class, RenderKitFactory.class,
        SearchExpressionContextFactory.class, TagHandlerDelegateFactory.class,
        ViewDeclarationLanguageFactory.class, VisitContextFactory.class };

    for (int i = 0; i < factoryTypes.length; i++) {
      Object o = FactoryFinder.getFactory(factoryTypes[i]);
      if (o != null) {
        if (classes[i].isAssignableFrom(o.getClass())) {
          pw.println("Test PASSED");
        } else {
          pw.println(JSFTestUtil.FAIL + " Factory object was not of the "
              + "exected type.");
          pw.println("Expected: " + classes[i].getName());
          pw.println("Received: " + o.getClass().getName());
        }
      } else {
        pw.println(JSFTestUtil.FAIL + " Null factory returned when "
            + "requesting a factory of type: " + factoryTypes[i]);
      }
    }
  }

  public void getFactoryNullPointerExceptionTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      FactoryFinder.getFactory(null);
      pw.println("Test FAILED: FactoryFinder.getFactory(null) failed to "
          + "throw a NullPointerException");
    } catch (NullPointerException npe) {
      pw.println("Test PASSED");

    } catch (Exception e) {
      pw.println(JSFTestUtil.FAIL
          + " FactoryFinder.getFactory(null) did NullPointerException.");
      e.printStackTrace();
    }
  }

  public void getFactoryNoConfiguredIllegalArgumentExceptionTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      FactoryFinder.getFactory("FailingFactory");
      pw.println("Test FAILED: FactoryFinder.getFactory("
          + "<INVALID_FACTORY>) failed to throw an "
          + "IllegalArgumentException");

    } catch (IllegalArgumentException iae) {
      pw.println("Test PASSED");

    } catch (Exception e) {
      pw.println(JSFTestUtil.FAIL + " FactoryFinder.getFactory("
          + "<INVALID_FACTORY>) did raise an Exception, but not "
          + "of the expected type.");
      e.printStackTrace();
    }
  }

  public void setFactoryNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    try {
      FactoryFinder.setFactory(null, "bogus");
      pw.println("Test FAILED: FactoryFinder.setFactory(null, implName) "
          + "failed to throw a NullPointerException");
    } catch (NullPointerException npe) {
      pw.println("Test PASSED");

    } catch (Exception e) {
      pw.println(JSFTestUtil.FAIL + " FactoryFinder.getFactory(null) did "
          + "raise an Exception, but not of the expected type.");
      pw.println("Expected: java.lang.NullPointerException");
      pw.println("Received: " + e.getClass().getName());
    }
  }

  public void setFactoryIAETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    try {
      FactoryFinder.setFactory("bogus", "bogus");
      pw.println("Test FAILED: FactoryFinder.setFactory(bogusName, "
          + "implName) failed to throw a IllegalArgumentException");
    } catch (IllegalArgumentException iae) {
      pw.println("Test PASSED");

    } catch (Exception e) {
      pw.println(JSFTestUtil.FAIL + " FactoryFinder.getFactory(null) did "
          + "raise an Exception, but not of the expected type.");
      pw.println("Expected: java.lang.IllegalArgumentException");
      pw.println("Received: " + e.getClass().getName());
    }
  }
}
