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
package com.sun.ts.tests.jsf.spec.navigation;

import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import jakarta.faces.application.NavigationHandler;
import jakarta.faces.application.ViewHandler;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public final class TestServlet extends HttpTCKServlet {

  /**
   * <p>
   * Initialize this <code>Servlet</code> instance.
   * </p>
   * 
   * @param config
   *          the configuration for this <code>Servlet</code>
   * 
   * @throws jakarta.servlet.ServletException
   *           indicates initialization failure
   */
  public void init(ServletConfig config) throws ServletException {

    super.init(config);

  } // init

  // --------------------------------------------------------- Private Methods

  private void setup(FacesContext context, String viewId) {

    ViewHandler origHandler = context.getApplication().getViewHandler();
    context.getApplication().setViewHandler(new TestViewHandler(origHandler));
    context.setViewRoot(origHandler.createView(context, viewId));

  } // END setup

  private void postTest(FacesContext context) {

    TestViewHandler handler = (TestViewHandler) context.getApplication()
        .getViewHandler();

    context.getApplication().setViewHandler(handler.getDelegate());

  } // END postTest

  // ------------------------------------------------------------ Test Methods

  public void navHandlerNullOutcomeTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    String expectedView = "/nullOutcome.xhtml";
    setup(context, expectedView);
    NavigationHandler handler = getApplication().getNavigationHandler();

    // if outcome is null, the current view should be redisplayed which
    // means the view ID shouldn't change
    handler.handleNavigation(context, "#{someBean.someAction}", null);

    if (!expectedView.equals(context.getViewRoot().getViewId())) {
      out.println("Test FAILED.  Test called handleNavigation() with a"
          + " null outcome.  The view ID shouldn't have changed.");
      out.println("View Expected: " + expectedView);
      out.println("View Received: " + context.getViewRoot().getViewId());
      return;
    }

    handler.handleNavigation(context, null, null);
    if (!expectedView.equals(context.getViewRoot().getViewId())) {
      out.println("Test FAILED[2].  Test called handleNavigation() with a"
          + " null outcome.  The view ID shouldn't have changed.");
      out.println("View Expected: " + expectedView);
      out.println("View Received: " + context.getViewRoot().getViewId());
      return;
    }

    // cleanup
    postTest(context);

    out.println(JSFTestUtil.PASS);

  } // END navHandlerNullOutcomeTest

  public void navHandlerFromViewExactMatchTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    String fromView = "/exactMatch.xhtml";
    setup(context, fromView);
    NavigationHandler handler = getApplication().getNavigationHandler();

    handler.handleNavigation(context, null, "match");

    if (!"/passed.xhtml".equals(context.getViewRoot().getViewId())) {
      out.println("Test FAILED.  Unexpected view ID set in FacesContext"
          + " where test was configured for an exact match on the "
          + "from-view-id.");
      out.println("View ID Recieved: " + context.getViewRoot().getViewId());
      out.println("View ID Expected: /passed.xhtml");
      return;
    }

    // cleanup
    postTest(context);

    out.println(JSFTestUtil.PASS);

  } // END navHandlerFromViewExactMatchTest

  public void navHandlerFromViewPatternMatchTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    // validate pattern matching...the to-view-id with the longest
    // wildcard match should be returned.
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    String fromView = "/longestview.xhtml";
    setup(context, fromView);
    NavigationHandler handler = getApplication().getNavigationHandler();

    handler.handleNavigation(context, null, "match");

    if (!"/passed.xhtml".equals(context.getViewRoot().getViewId())) {
      out.println("Test FAILED.  Unexpected view ID set in FacesContext"
          + " where test was configured for a pattern match on the "
          + "from-view-id.");
      out.println("View ID Recieved: " + context.getViewRoot().getViewId());
      out.println("View ID Expected: /passed.xhtml");
      return;
    }

    // cleanup
    postTest(context);

    out.println(JSFTestUtil.PASS);

  } // END navHandlerFromViewPatternMatchTest

  public void navHandlerFromViewAsteriskOnlyTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    String fromView = "/someView.xhtml";
    setup(context, fromView);
    NavigationHandler handler = getApplication().getNavigationHandler();

    handler.handleNavigation(context, null, "match");

    if (!"/wildcard.xhtml".equals(context.getViewRoot().getViewId())) {
      out.println("Test FAILED.  Unexpected view ID set in FacesContext"
          + " where test was configured for a 'any view' match on "
          + "from-view-id.");
      out.println("View ID Recieved: " + context.getViewRoot().getViewId());
      out.println("View ID Expected: /passed.xhtml");
      return;
    }

    // cleanup
    postTest(context);

    out.println(JSFTestUtil.PASS);
  } // END navHandlerFromViewAsteriskOnlyTest

  public void navHandlerFromViewSearchOrderTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    setup(context, "/operation1.xhtml");
    NavigationHandler handler = getApplication().getNavigationHandler();

    handler.handleNavigation(context, null, "match");

    if (!"/passed1.xhtml".equals(context.getViewRoot().getViewId())) {
      out.println("Test FAILED.  Expected exact match to be considered"
          + " first when matching the from-view-id.");
      out.println("View ID Expected: /passed1.xhtml");
      out.println("View ID Received: " + context.getViewRoot().getViewId());
      return;
    }

    setup(context, "/operation11.xhtml");
    handler.handleNavigation(context, null, "match");

    if (!"/passed2.xhtml".equals(context.getViewRoot().getViewId())) {
      out.println("Test FAILED.  Expected pattern match to be considered"
          + " second when matching the from-view-id.");
      out.println("View ID Expected: /passed2.xhtml");
      out.println("View ID Received: " + context.getViewRoot().getViewId());
      return;
    }

    setup(context, "/operatio.xhtml");
    handler.handleNavigation(context, null, "match");
    if (!"/wildcard.xhtml".equals(context.getViewRoot().getViewId())) {
      out.println("Test FAILED.  Expected 'any view' match to be considered"
          + " last when matching the from-view-id.");
      out.println("View ID Expected: /wildcard.xhtml");
      out.println("View ID Received: " + context.getViewRoot().getViewId());
      return;
    }

    // cleanup
    postTest(context);

    out.println(JSFTestUtil.PASS);
  } // END navHandlerFromViewSearchOrderTest

  public void navHandlerNavigationCaseTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    setup(context, "/navigationCase.xhtml");
    NavigationHandler handler = getApplication().getNavigationHandler();

    String action = "#{someBean.someAction}";
    String action2 = "#{someBean.someOtherAction}";
    String outcome = "go";
    String outcome2 = "stop";

    handler.handleNavigation(context, action, outcome);

    if (!"/passed1.xhtml".equals(context.getViewRoot().getViewId())) {
      out.println(
          "Test FAILED.  Unexpected view returned when passing '" + action
              + "' as the action and '" + outcome + "' as the " + "outcome.");
      out.println("View ID Expected: /passed1.xhtml");
      out.println("View ID Received: " + context.getViewRoot().getViewId());
      return;
    }

    setup(context, "/navigationCase.xhtml");
    handler.handleNavigation(context, action, outcome2);

    if (!"/passed3.xhtml".equals(context.getViewRoot().getViewId())) {
      out.println(
          "Test FAILED.  Unexpected view returned when passing '" + action
              + "' as the action and '" + outcome2 + "' as the " + "outcome.");
      out.println("View ID Expected: /passed3.xhtml");
      out.println("View ID Received: " + context.getViewRoot().getViewId());
      return;
    }

    setup(context, "/navigationCase.xhtml");
    handler.handleNavigation(context, action2, outcome);

    if (!"/passed2.xhtml".equals(context.getViewRoot().getViewId())) {
      out.println(
          "Test FAILED.  Unexpected view returned when passing '" + action
              + "' as the action and '" + outcome2 + "' as the " + "outcome.");
      out.println("View ID Expected: /passed2.xhtml");
      out.println("View ID Received: " + context.getViewRoot().getViewId());
      return;
    }

    setup(context, "/navigationCase.xhtml");
    handler.handleNavigation(context, null, outcome);

    if (!"/passed2.xhtml".equals(context.getViewRoot().getViewId())) {
      out.println("Test FAILED.  Unexpected view returned when passing '" + null
          + "' as the action and '" + outcome2 + "' as the " + "outcome.");
      out.println("View ID Expected: /passed2.xhtml");
      out.println("View ID Received: " + context.getViewRoot().getViewId());
      return;
    }

    setup(context, "/navigationCase.xhtml");
    handler.handleNavigation(context, action2, outcome2);

    if (!"/passed4.xhtml".equals(context.getViewRoot().getViewId())) {
      out.println("Test FAILED.  Unexpected view returned when passing '"
          + action + "' as the from-action and '" + outcome2 + "' as the "
          + "outcome.");
      out.println("View ID Expected: /passed4.xhtml");
      out.println("View ID Received: " + context.getViewRoot().getViewId());
      return;
    }

    // cleanup
    postTest(context);

    out.println(JSFTestUtil.PASS);

  } // END navHandlerNavigationCaseTest

  public void navHandlerRedirectTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    setup(context, "/redirect.xhtml");
    NavigationHandler handler = getApplication().getNavigationHandler();

    handler.handleNavigation(context, null, "match");

    boolean redirectSent = ((TestServletResponseWrapper) response)
        .getRedirectSent();

    if (!redirectSent) {
      out.println("Test FAILED.  Expected implementation to call sendRedirect()"
          + " if matching navigation case contained <redirect/>.");
      return;
    }

    if (!context.getResponseComplete()) {
      out.println("Test FAILED.  Expected the implementation to call"
          + " FacesContext.responseComplete() to return 'true' if the"
          + " matching navigation case contained a 'redirect' element.");
      return;
    }

    // cleanup
    postTest(context);

    out.println(JSFTestUtil.PASS);

  } // END navHandlerRedirectTest

  public void navHandlerNoNavCaseGoToNextRuleTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    // If no matching navigation case is found within a particular rule
    // move on to the matching navigation-rule
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    setup(context, "/matching.xhtml");
    NavigationHandler handler = getApplication().getNavigationHandler();

    handler.handleNavigation(context, null, "match");

    if (!"/passed.xhtml".equals(context.getViewRoot().getViewId())) {
      out.println("Test FAILED.  Implementation didn't continue through"
          + " the navigation rules as expected if the first matching "
          + "navigation rule didn't have a matching navigation case.");
      out.println("View ID Expected: /passed.xhtml");
      out.println("View ID Received: " + context.getViewRoot().getViewId());
      return;
    }

    // cleanup
    postTest(context);

    out.println(JSFTestUtil.PASS);

  } // END navHandlerNoNavCaseGoToNextRuleTest
}
