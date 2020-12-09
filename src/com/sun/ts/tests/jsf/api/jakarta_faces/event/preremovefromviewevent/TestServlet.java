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
package com.sun.ts.tests.jsf.api.jakarta_faces.event.preremovefromviewevent;

import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.jsf.api.jakarta_faces.event.common.BaseComponentSystemEventTestServlet;
import com.sun.ts.tests.jsf.api.jakarta_faces.event.common.TestSystemEventListener;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import jakarta.faces.component.UICommand;
import jakarta.faces.component.UIComponent;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.faces.event.PreRemoveFromViewEvent;
import jakarta.faces.event.SystemEventListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TestServlet extends BaseComponentSystemEventTestServlet {

  private static final UICommand UIC = new UICommand();

  @Override
  protected ComponentSystemEvent createEvent(UIComponent component) {
    return new PreRemoveFromViewEvent(component);

  }

  @Override
  protected UIComponent getTestComponent() {
    return UIC;
  }
  // ------------------------------------------------------------
  // PreRemoveFromViewEvent Tests

  public void preRemoveFromViewEventIsApproiateListenerPostiveTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    SystemEventListener testListener = new TestSystemEventListener();
    ComponentSystemEvent cse = createEvent(getTestComponent());
    String eventName = cse.getClass().getName();

    // make sure the return value is true if SystemEventListener is
    // passed as a parameter to isAppropriateListener.

    if (cse.isAppropriateListener(testListener)) {
      pw.println(JSFTestUtil.PASS);

    } else {
      pw.println("Test FAILED. " + eventName + ".isAppropriateListener "
          + "did not return true when SystemEventListener was passed in "
          + "as a parameter");
    }
  }

}
