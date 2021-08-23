/*
 * Copyright (c) 2011, 2020 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jsf.api.jakarta_faces.application.configurablenavigationhandlerwrapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import jakarta.faces.application.Application;
import jakarta.faces.application.ConfigurableNavigationHandler;
import jakarta.faces.application.ConfigurableNavigationHandlerWrapper;
import jakarta.faces.application.NavigationCase;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TestServlet extends HttpTCKServlet {

  // Test for getNavigationCase(FacesContext, String, String)
  public void configNavihandlerwprGetNavigationCaseTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = FacesContext.getCurrentInstance();
    Application application = getApplication();
    String fromAct = "#{bogus.postOne}";

    if (application != null) {
      ConfigurableNavigationHandler cnh = new TCKConfNaviHandler();

      if (fromAct.equals(
          cnh.getNavigationCase(context, fromAct, "").getFromAction())) {
        out.println(JSFTestUtil.PASS);
      } else {
        out.println(JSFTestUtil.FAIL);
      }
    }
  }// End configNavihandlerwprGetNavigationCaseTest

  // Test for getNavigationCase(FacesContext, String, String) throws
  // NullPointerException
  public void configNavihandlerwprGetNavigationCaseNPETest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    if (application != null) {
      ConfigurableNavigationHandler cnh = new TCKConfNaviHandler();

      try {
        cnh.getNavigationCase(null, "#{bogus.postOne}", "");
        out.println(JSFTestUtil.FAIL
            + ": Expected an exception to be thrown, but nothing was!");

      } catch (NullPointerException npe) {
        out.println(JSFTestUtil.PASS);

      } catch (Exception e) {
        out.println(
            JSFTestUtil.FAIL + ": Expected NullPointerException to be thrown!");
        e.printStackTrace();
      }
    }
  }// End configNavihandlerwprGetNavigationCaseNPETest

  // Test for getNavigationCases(FacesContext, String, String)
  public void configNavihandlerwprGetNavigationCasesTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    List<String> goldActs = new ArrayList<String>();
    goldActs.add("#{bogus.postOne}");
    goldActs.add("#{bogus.postTwo}");

    if (application != null) {
      ConfigurableNavigationHandler cnh = new TCKConfNaviHandler();

      Map<String, Set<NavigationCase>> navCases = cnh.getNavigationCases();

      Iterator<Entry<String, Set<NavigationCase>>> itr = navCases.entrySet()
          .iterator();

      while (itr.hasNext()) {
        Entry<String, Set<NavigationCase>> entry = itr.next();

        String key = (String) entry.getKey();

        Iterator<NavigationCase> navItr = entry.getValue().iterator();
        while (navItr.hasNext()) {
          NavigationCase navi = navItr.next();
          String result = navi.getFromAction();

          if (!(goldActs.contains(result))) {
            out.println(JSFTestUtil.FAIL + " Expected " + result
                + " to be in 'from-view-id: " + key);
            return;
          }
        }
      }

      out.println(JSFTestUtil.PASS);
    }

  }// End configNavihandlerwprGetNavigationCasesTest

  // ------------------------------------------------------------------ private
  // classes

  private class TCKConfNaviHandler
      extends ConfigurableNavigationHandlerWrapper {
    Application application = getApplication();

    @Override
    public ConfigurableNavigationHandler getWrapped() {
      return (ConfigurableNavigationHandler) application.getNavigationHandler();
    }

  }
}
