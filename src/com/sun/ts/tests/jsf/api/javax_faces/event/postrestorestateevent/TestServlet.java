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

package com.sun.ts.tests.jsf.api.javax_faces.event.postrestorestateevent;

import com.sun.ts.tests.jsf.api.javax_faces.event.common.BaseComponentSystemEventTestServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.event.PostRestoreStateEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import javax.faces.component.UIInput;

public class TestServlet extends BaseComponentSystemEventTestServlet {

  private static final UICommand uic = new UICommand();

  @Override
  protected ComponentSystemEvent createEvent(UIComponent component) {
    return new PostRestoreStateEvent(component);
  }

  @Override
  protected UIComponent getTestComponent() {
    return uic;
  }

  // ------------------------------------------------------------
  // PostRestoreStateEvent Tests

  public void postRestoreStateEventSetComponentTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    UIComponent uic = new UICommand();
    UIComponent uii = new UIInput();

    PostRestoreStateEvent arse = (PostRestoreStateEvent) createEvent(uic);

    if (!uic.equals(arse.getComponent())) {
      pw.println(JSFTestUtil.FAIL + " PostRestoreStateEvent.getComponent() "
          + "didn't return the initial UIComponent provided to its "
          + "constructor.");
    }

    // Now use setComponent to change the current UICommand component to an
    // UIInput component.
    arse.setComponent(uii);

    if (uii.equals(arse.getComponent())) {
      pw.println("Test PASSED.");
    } else {
      pw.println(JSFTestUtil.FAIL + " PostRestoreStateEvent.getComponent() "
          + "didn't return the UIComponent that was set with "
          + "AfterRestreStateEvent.setComponent()");
    }

  }

}
