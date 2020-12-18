/*
 * Copyright (c) 2012, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsf.api.jakarta_faces.event.postconstructcustomscopeevent;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import com.sun.ts.tests.jsf.api.jakarta_faces.event.common.BaseSystemEventTestServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import jakarta.faces.application.Application;
import jakarta.faces.event.PostConstructCustomScopeEvent;
import jakarta.faces.event.ScopeContext;
import jakarta.faces.event.SystemEvent;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TestServlet extends BaseSystemEventTestServlet {
  private static final String SCOPE_NAME = "tckScope";

  private HashMap<String, Object> tckScope = new HashMap<String, Object>();

  {
    tckScope.put("csContext", "scope");
  }

  private ScopeContext tck_scope_context = new ScopeContext(SCOPE_NAME,
      tckScope);

  @Override
  protected SystemEvent createEvent(Object src) {
    return new PostConstructCustomScopeEvent(tck_scope_context);
  }

  // ------------------------------------------- PostKeepFlashValueEvent

  public void postConstructCustomScopeEventGetContextTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    Application app = getFacesContext().getApplication();

    if (app != null) {
      PostConstructCustomScopeEvent pScope = (PostConstructCustomScopeEvent) createEvent(
          app);
      String result = pScope.getContext().getScopeName();

      if (!SCOPE_NAME.equals(result)) {
        pw.println(JSFTestUtil.FAIL + " Wrong name for ScopeContext!"
            + JSFTestUtil.NL + "Expected: " + SCOPE_NAME + JSFTestUtil.NL
            + "Received: " + result);

      } else {
        pw.println(JSFTestUtil.PASS);

      }

    } else {
      pw.println(JSFTestUtil.FAIL + " Unexpected problem obtaining "
          + "Application instance.");
    }
  }

} // TestServlet
