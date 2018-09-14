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

package com.sun.ts.tests.jsf.api.javax_faces.component.uinamingcontainer;

import java.io.IOException;
import java.io.PrintWriter;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.api.javax_faces.component.common.BaseComponentTestServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public class TestServlet extends BaseComponentTestServlet {

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    setRendererType(null);
  }

  /**
   * <p>
   * Creates a new {@link javax.faces.component.UIComponent} instance.
   * </p>
   * 
   * @return a new {@link javax.faces.component.UIComponent} instance.
   */
  @Override
  protected UIComponentBase createComponent() {
    return new UINamingContainer();
  }

  // ------------------------------------------- Test Methods ----

  // -------------------------------------------------------------
  // UINamingContainer Specific

  public void uiNamingContainerGetSeparatorCharTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();

    char golden = ':';
    char result = UINamingContainer.getSeparatorChar(context);

    if (!(golden == result)) {
      out.println(JSFTestUtil.FAIL + " Unexpected result calling "
          + "UINamingContainer.getSeparatorChar()!" + JSFTestUtil.NL
          + "Expected: " + golden + JSFTestUtil.NL + "Received: " + result);

    } else {
      out.println(JSFTestUtil.PASS);
    }
  }

}
