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
package com.sun.ts.tests.jsf.spec.navigation;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.faces.application.ViewHandler;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;

import java.io.IOException;
import java.io.PrintWriter;

public final class TestServlet extends HttpTCKServlet {

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
    String expectedView = "/nullOutcome.jsp";
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
    String fromView = "/exactMatch.jsp";
    setup(context, fromView);
    NavigationHandler handler = getApplication().getNavigationHandler();

    handler.handleNavigation(context, null, "match");

    if (!"/passed.jsp".equals(context.getViewRoot().getViewId())) {
      out.println("Test FAILED.  Unexpected view ID set in FacesContext"
          + " where test was configured for an exact match on the "
          + "from-view-id.");
      out.println("View ID Recieved: " + context.getViewRoot().getViewId());
      out.println("View ID Expected: /passed.jsp");
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
    String fromView = "/longestview.jsp";
    setup(context, fromView);
    NavigationHandler handler = getApplication().getNavigationHandler();

    handler.handleNavigation(context, null, "match");

    if (!"/passed.jsp".equals(context.getViewRoot().getViewId())) {
      out.println("Test FAILED.  Unexpected view ID set in FacesContext"
          + " where test was configured for a pattern match on the "
          + "from-view-id.");
      out.println("View ID Recieved: " + context.getViewRoot().getViewId());
      out.println("View ID Expected: /passed.jsp");
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
    String fromView = "/someView.jsp";
    setup(context, fromView);
    NavigationHandler handler = getApplication().getNavigationHandler();

    handler.handleNavigation(context, null, "match");

    if (!"/wildcard.jsp".equals(context.getViewRoot().getViewId())) {
      out.println("Test FAILED.  Unexpected view ID set in FacesContext"
          + " where test was configured for a 'any view' match on "
          + "from-view-id.");
      out.println("View ID Recieved: " + context.getViewRoot().getViewId());
      out.println("View ID Expected: /passed.jsp");
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
    setup(context, "/operation1.jsp");
    NavigationHandler handler = getApplication().getNavigationHandler();

    handler.handleNavigation(context, null, "match");

    if (!"/passed1.jsp".equals(context.getViewRoot().getViewId())) {
      out.println("Test FAILED.  Expected exact match to be considered"
          + " first when matching the from-view-id.");
      out.println("View ID Expected: /passed1.jsp");
      out.println("View ID Received: " + context.getViewRoot().getViewId());
      return;
    }

    setup(context, "/operation11.jsp");
    handler.handleNavigation(context, null, "match");

    if (!"/passed2.jsp".equals(context.getViewRoot().getViewId())) {
      out.println("Test FAILED.  Expected pattern match to be considered"
          + " second when matching the from-view-id.");
      out.println("View ID Expected: /passed2.jsp");
      out.println("View ID Received: " + context.getViewRoot().getViewId());
      return;
    }

    setup(context, "/operatio.jsp");
    handler.handleNavigation(context, null, "match");
    if (!"/wildcard.jsp".equals(context.getViewRoot().getViewId())) {
      out.println("Test FAILED.  Expected 'any view' match to be considered"
          + " last when matching the from-view-id.");
      out.println("View ID Expected: /wildcard.jsp");
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
    setup(context, "/navigationCase.jsp");
    NavigationHandler handler = getApplication().getNavigationHandler();

    String action = "#{someBean.someAction}";
    String action2 = "#{someBean.someOtherAction}";
    String outcome = "go";
    String outcome2 = "stop";

    handler.handleNavigation(context, action, outcome);

    if (!"/passed1.jsp".equals(context.getViewRoot().getViewId())) {
      out.println(
          "Test FAILED.  Unexpected view returned when passing '" + action
              + "' as the action and '" + outcome + "' as the " + "outcome.");
      out.println("View ID Expected: /passed1.jsp");
      out.println("View ID Received: " + context.getViewRoot().getViewId());
      return;
    }

    setup(context, "/navigationCase.jsp");
    handler.handleNavigation(context, action, outcome2);

    if (!"/passed3.jsp".equals(context.getViewRoot().getViewId())) {
      out.println(
          "Test FAILED.  Unexpected view returned when passing '" + action
              + "' as the action and '" + outcome2 + "' as the " + "outcome.");
      out.println("View ID Expected: /passed3.jsp");
      out.println("View ID Received: " + context.getViewRoot().getViewId());
      return;
    }

    setup(context, "/navigationCase.jsp");
    handler.handleNavigation(context, action2, outcome);

    if (!"/passed2.jsp".equals(context.getViewRoot().getViewId())) {
      out.println(
          "Test FAILED.  Unexpected view returned when passing '" + action
              + "' as the action and '" + outcome2 + "' as the " + "outcome.");
      out.println("View ID Expected: /passed2.jsp");
      out.println("View ID Received: " + context.getViewRoot().getViewId());
      return;
    }

    setup(context, "/navigationCase.jsp");
    handler.handleNavigation(context, null, outcome);

    if (!"/passed2.jsp".equals(context.getViewRoot().getViewId())) {
      out.println("Test FAILED.  Unexpected view returned when passing '" + null
          + "' as the action and '" + outcome2 + "' as the " + "outcome.");
      out.println("View ID Expected: /passed2.jsp");
      out.println("View ID Received: " + context.getViewRoot().getViewId());
      return;
    }

    setup(context, "/navigationCase.jsp");
    handler.handleNavigation(context, action2, outcome2);

    if (!"/passed4.jsp".equals(context.getViewRoot().getViewId())) {
      out.println("Test FAILED.  Unexpected view returned when passing '"
          + action + "' as the from-action and '" + outcome2 + "' as the "
          + "outcome.");
      out.println("View ID Expected: /passed4.jsp");
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
    setup(context, "/redirect.jsp");
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
    setup(context, "/matching.jsp");
    NavigationHandler handler = getApplication().getNavigationHandler();

    handler.handleNavigation(context, null, "match");

    if (!"/passed.jsp".equals(context.getViewRoot().getViewId())) {
      out.println("Test FAILED.  Implementation didn't continue through"
          + " the navigation rules as expected if the first matching "
          + "navigation rule didn't have a matching navigation case.");
      out.println("View ID Expected: /passed.jsp");
      out.println("View ID Received: " + context.getViewRoot().getViewId());
      return;
    }

    // cleanup
    postTest(context);

    out.println(JSFTestUtil.PASS);

  } // END navHandlerNoNavCaseGoToNextRuleTest
}
