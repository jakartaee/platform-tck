/*
 * Copyright (c) 2011, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jsf.api.javax_faces.application.navigationcase;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.faces.application.Application;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.NavigationCase;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.common.navigation.TCKNavigationCase;
import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public class TestServlet extends HttpTCKServlet {

  // Test for .getNavigationCase(FacesContext, String, String)
  public void navigationCaseEqualsTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    FacesContext context = FacesContext.getCurrentInstance();
    Application application = getApplication();
    Set<NavigationCase> myNav = new HashSet<NavigationCase>();

    String viewId = "/stop.xhtml";
    context.getViewRoot().setViewId(viewId);

    // Golden NavigationCase.
    NavigationCase testCase = TCKNavigationCase.getCase("blue");
    myNav.add(testCase);

    NavigationCase result = this.getResultCase(context, application, viewId,
        myNav, "#{color.result}", "Blue");

    if (testCase.equals(result)) {
      out.println(JSFTestUtil.PASS);
    } else {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "NavigationCases should be equal to each other, when calling "
          + "NavigationCase.equals().");
    }

  }// End navigationCaseEqualsTest

  // Test for .getActionURL(FacesContext)
  public void navigationCaseGetActionURLTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = FacesContext.getCurrentInstance();
    Application application = getApplication();
    Set<NavigationCase> myNav = new HashSet<NavigationCase>();

    String viewId = "/stop.xhtml";
    context.getViewRoot().setViewId(viewId);

    // Golden NavigationCase.
    NavigationCase testCase = TCKNavigationCase.getCase("blue");
    myNav.add(testCase);

    URL golden = testCase.getActionURL(context);
    URL result = this.getResultCase(context, application, viewId, myNav,
        "#{color.result}", "Blue").getActionURL(context);

    if (golden.sameFile(result)) {
      out.println(JSFTestUtil.PASS);
    } else {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "NavigationCase's URLs should be equal to each other, when calling "
          + "NavigationCase.getActionURL()." + JSFTestUtil.NL + "Expected: "
          + golden.toString() + JSFTestUtil.NL + "Received: "
          + result.toString());
    }

  }// End navigationCaseGetActionURLTest

  // Test for .getRedirectURL(FacesContext)
  public void navigationCaseGetRedirectURLTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = FacesContext.getCurrentInstance();
    Application application = getApplication();
    context.getViewRoot().setViewId("/stop.xhtml");

    Set<NavigationCase> myNav = new HashSet<NavigationCase>();

    String viewId = "/stop.xhtml";
    context.getViewRoot().setViewId(viewId);

    // Golden NavigationCase.
    NavigationCase testCase = TCKNavigationCase.getCase("blue");
    myNav.add(testCase);

    URL golden = testCase.getRedirectURL(context);
    URL result = this.getResultCase(context, application, viewId, myNav,
        "#{color.result}", "Blue").getRedirectURL(context);

    if (golden.sameFile(result)) {
      out.println(JSFTestUtil.PASS);
    } else {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "NavigationCase's URLs  should be equal to each other, when calling "
          + "NavigationCase.getRedirectURL()." + JSFTestUtil.NL + "Expected: "
          + golden.toString() + JSFTestUtil.NL + "Received: "
          + result.toString());
    }

  }// End navigationCaseGetRedirectURLTest

  // Test for .getBookmarkableURL(FacesContext)
  public void navigationCaseGetBookmarkableURLTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = FacesContext.getCurrentInstance();
    Application application = getApplication();
    Set<NavigationCase> myNav = new HashSet<NavigationCase>();

    String viewId = "/stop.xhtml";
    context.getViewRoot().setViewId(viewId);

    // Golden NavigationCase.
    NavigationCase testCase = TCKNavigationCase.getCase("blue");
    myNav.add(testCase);

    URL golden = testCase.getBookmarkableURL(context);
    URL result = this.getResultCase(context, application, viewId, myNav,
        "#{color.result}", "Blue").getBookmarkableURL(context);

    if (golden.sameFile(result)) {
      out.println(JSFTestUtil.PASS);
    } else {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "NavigationCase's URLs  should be equal to each other, when calling "
          + "getBookMarkableURL()." + JSFTestUtil.NL + "Expected: "
          + golden.toString() + JSFTestUtil.NL + "Received: "
          + result.toString());
    }

  }// End navigationCaseGetBookmarkableURLTest

  // Test for .getCondition(FacesContext)
  public void navigationCaseGetConditionTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = FacesContext.getCurrentInstance();
    Application application = getApplication();
    Set<NavigationCase> myNav = new HashSet<NavigationCase>();

    String viewId = "/stop.xhtml";
    context.getViewRoot().setViewId(viewId);

    // Golden NavigationCase.
    NavigationCase testCase = TCKNavigationCase.getCase("blue");
    myNav.add(testCase);

    NavigationCase result = this.getResultCase(context, application, viewId,
        myNav, "#{color.result}", "Blue");

    if (result.getCondition(context)) {
      out.println(JSFTestUtil.PASS);
    } else {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Expected: NavigationCase.getCondition(FacesContext) to return 'true'");
    }

  }// End navigationCaseGetConditionTest

  // Test for .hasCondition(FacesContext)
  public void navigationCaseHasConditionTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = FacesContext.getCurrentInstance();
    Application application = getApplication();
    Set<NavigationCase> myNav = new HashSet<NavigationCase>();

    String viewId = "/stop.xhtml";
    context.getViewRoot().setViewId(viewId);

    // Golden NavigationCase.
    NavigationCase testCase = TCKNavigationCase.getCase("blue");
    myNav.add(testCase);

    NavigationCase result = this.getResultCase(context, application, viewId,
        myNav, "#{color.result}", "Blue");

    if (result.hasCondition()) {
      out.println(JSFTestUtil.PASS);
    } else {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Expected: NavigationCase.hasCondition(FacesContext) to return 'true'");
    }

  }// End navigationCaseHasConditionTest

  // Test for .getFromAction()
  public void navigationGetFromActionTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = FacesContext.getCurrentInstance();
    Application application = getApplication();
    Set<NavigationCase> myNav = new HashSet<NavigationCase>();

    String viewId = "/stop.xhtml";
    context.getViewRoot().setViewId(viewId);

    // Golden NavigationCase.
    NavigationCase testCase = TCKNavigationCase.getCase("blue");
    myNav.add(testCase);

    String golden = testCase.getFromAction();
    String result = this.getResultCase(context, application, viewId, myNav,
        "#{color.result}", "Blue").getFromAction();

    if (golden.equals(result)) {
      out.println(JSFTestUtil.PASS);
    } else {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "NavigationCase's from-actions  should be equal to each other, when calling "
          + "getFromAction()." + JSFTestUtil.NL + "Expected: " + golden
          + JSFTestUtil.NL + "Received: " + result);
    }

  }// End navigationGetFromActionTest

  // Test for .getFromOutcome()
  public void navigationGetFromOutcomeTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = FacesContext.getCurrentInstance();
    Application application = getApplication();
    Set<NavigationCase> myNav = new HashSet<NavigationCase>();

    String viewId = "/stop.xhtml";
    context.getViewRoot().setViewId(viewId);

    // Golden NavigationCase.
    NavigationCase testCase = TCKNavigationCase.getCase("blue");
    myNav.add(testCase);

    String golden = testCase.getFromOutcome();
    String result = this.getResultCase(context, application, viewId, myNav,
        "#{color.result}", "Blue").getFromOutcome();

    if (golden.equals(result)) {
      out.println(JSFTestUtil.PASS);
    } else {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "NavigationCase's from-actions  should be equal to each other, when calling "
          + "getFromOutcome()." + JSFTestUtil.NL + "Expected: " + golden
          + JSFTestUtil.NL + "Received: " + result);
    }

  }// End navigationGetFromOutcomeTest

  // Test for .getFromViewId()
  public void navigationGetFromViewIdTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = FacesContext.getCurrentInstance();
    Application application = getApplication();
    Set<NavigationCase> myNav = new HashSet<NavigationCase>();

    String viewId = "/stop.xhtml";
    context.getViewRoot().setViewId(viewId);

    // Golden NavigationCase.
    NavigationCase testCase = TCKNavigationCase.getCase("blue");
    myNav.add(testCase);

    String golden = testCase.getFromViewId();
    String result = this.getResultCase(context, application, viewId, myNav,
        "#{color.result}", "Blue").getFromViewId();

    if (golden.equals(result)) {
      out.println(JSFTestUtil.PASS);
    } else {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "NavigationCase's from-viewIds  should be equal to each other, when calling "
          + "getFromViewId()." + JSFTestUtil.NL + "Expected: " + golden
          + JSFTestUtil.NL + "Received: " + result);
    }

  }// End navigationGetFromViewIdTest

  // Test for .getToViewId(FacesContext)
  public void navigationGetToViewIdTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = FacesContext.getCurrentInstance();
    Application application = getApplication();
    Set<NavigationCase> myNav = new HashSet<NavigationCase>();

    String viewId = "/stop.xhtml";
    context.getViewRoot().setViewId(viewId);

    // Golden NavigationCase.
    NavigationCase testCase = TCKNavigationCase.getCase("blue");
    myNav.add(testCase);

    String golden = testCase.getToViewId(context);
    String result = this.getResultCase(context, application, viewId, myNav,
        "#{color.result}", "Blue").getToViewId(context);

    if (golden.equals(result)) {
      out.println(JSFTestUtil.PASS);
    } else {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "NavigationCase's to-viewIds  should be equal to each other, when calling "
          + "getToViewId(FacesContext)." + JSFTestUtil.NL + "Expected: "
          + golden + JSFTestUtil.NL + "Received: " + result);
    }

  }// End navigationGetToViewIdTest

  // Test for .isRedirect()
  public void navigationIsRedirectTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = FacesContext.getCurrentInstance();
    Application application = getApplication();
    Set<NavigationCase> myNav = new HashSet<NavigationCase>();

    String viewId = "/stop.xhtml";
    context.getViewRoot().setViewId(viewId);

    // Golden NavigationCase.
    NavigationCase testCase = TCKNavigationCase.getCase("green");
    myNav.add(testCase);

    NavigationCase result = this.getResultCase(context, application, viewId,
        myNav, "#{color.result}", "");

    if (result.isRedirect()) {
      out.println(JSFTestUtil.PASS);
    } else {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Expected: NavigationCase.isRedirect() to return 'true'");
    }

  }// End navigationIsRedirectTest

  // Test for .isIncludeViewParams()
  public void navigationIsIncludeViewParamsTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = FacesContext.getCurrentInstance();
    Application application = getApplication();
    Set<NavigationCase> myNav = new HashSet<NavigationCase>();

    String viewId = "/stop.xhtml";
    context.getViewRoot().setViewId(viewId);

    // Golden NavigationCase.
    NavigationCase testCase = TCKNavigationCase.getCase("green");
    myNav.add(testCase);

    NavigationCase result = this.getResultCase(context, application, viewId,
        myNav, "#{color.result}", "");

    if (result.isIncludeViewParams()) {
      out.println(JSFTestUtil.PASS);
    } else {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Expected: NavigationCase.isIncludeViewParams() to return 'true'");
    }

  }// End navigationIsIncludeViewParamsTest

  // ------------------- private/protected methods ------------------
  private NavigationCase getResultCase(FacesContext context,
      Application application, String viewId, Set<NavigationCase> navSet,
      String fromAction, String fromOutCome) {
    NavigationCase result = null;

    if (application != null && context != null && navSet != null) {
      ConfigurableNavigationHandler cnh = (ConfigurableNavigationHandler) application
          .getNavigationHandler();

      // Add NavigationCase to NavigationHandler's Map.
      cnh.getNavigationCases().put(viewId, navSet);

      // retrieve newly added NavigationCase.
      result = cnh.getNavigationCase(context, fromAction, fromOutCome);
    }

    return result;
  }
}
