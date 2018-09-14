/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsf.api.javax_faces.event.predestroyapplicationevent;

import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.jsf.api.javax_faces.event.common.BaseSystemEventTestServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.event.PreDestroyApplicationEvent;
import javax.faces.event.SystemEvent;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestServlet extends BaseSystemEventTestServlet {

  @Override
  protected SystemEvent createEvent(Object src) {
    return new PreDestroyApplicationEvent((Application) src);
  }

  // ----------------------------- PreDestroyApplicationEvent test methods

  public void preDestroyApplicationEventCtorTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    Application app = getFacesContext().getApplication();

    if (app != null) {
      PreDestroyApplicationEvent pda = (PreDestroyApplicationEvent) createEvent(
          app);
      if (pda == null) {
        pw.println(JSFTestUtil.FAIL + " Unable to create Event");
      } else {
        pw.println(JSFTestUtil.PASS);
      }
    } else {
      pw.println(JSFTestUtil.FAIL + " Unexpected problem obtaining "
          + "Application instance.");
    }
  }

  public void preDestroyApplicationEventGetAppTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    Application app = getFacesContext().getApplication();
    PreDestroyApplicationEvent pda = (PreDestroyApplicationEvent) createEvent(
        app);

    if (app != null) {
      if (app != pda.getApplication()) {
        pw.println(
            JSFTestUtil.FAIL + "PreDestroyApplicationEvent.getApplication() "
                + "returned unexpected result.");
      } else {
        pw.println(JSFTestUtil.PASS);
      }

    } else {
      pw.println("Test FAILED. Unexpected problem obtaining "
          + "Application instance.");
    }
  }

}
