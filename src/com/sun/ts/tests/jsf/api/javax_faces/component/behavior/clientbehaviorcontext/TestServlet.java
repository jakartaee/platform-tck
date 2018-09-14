/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * $Id: $
 */
package com.sun.ts.tests.jsf.api.javax_faces.component.behavior.clientbehaviorcontext;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashSet;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.api.javax_faces.component.behavior.common.TCKClientBehaviorContext;
import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public class TestServlet extends HttpTCKServlet {

  protected ServletContext servletContext;

  /**
   * <p>
   * Initializes this {@link javax.servlet.Servlet}.
   * </p>
   * 
   * @param config
   *          this Servlet's configuration
   * @throws ServletException
   *           if an error occurs
   */
  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    servletContext = config.getServletContext();
  }

  // --------------------------------------- test methods

  // createClientBehaviorContext() throws NullPointerException
  public void createClientBehaviorContextNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ClientBehaviorContext cbc = new TCKClientBehaviorContext();

    HashSet<ClientBehaviorContext.Parameter> params = new HashSet<ClientBehaviorContext.Parameter>();

    ClientBehaviorContext.Parameter paramOne = new ClientBehaviorContext.Parameter(
        "one", "alpha");

    params.add(paramOne);

    // context as null
    JSFTestUtil.checkForNPE(cbc, "createClientBehaviorContext",
        new Class<?>[] { FacesContext.class, UIComponent.class, String.class,
            String.class, Collection.class },
        new Object[] { null, new UIInput(), "abc", "xyz", params }, out);

    // component as null
    JSFTestUtil.checkForNPE(cbc, "createClientBehaviorContext",
        new Class<?>[] { FacesContext.class, UIComponent.class, String.class,
            String.class, Collection.class },
        new Object[] { getFacesContext(), null, "abc", "xyz", params }, out);

    // eventName as null
    JSFTestUtil.checkForNPE(cbc, "createClientBehaviorContext",
        new Class<?>[] { FacesContext.class, UIComponent.class, String.class,
            String.class, Collection.class },
        new Object[] { getFacesContext(), new UIInput(), null, "xyz", params },
        out);

  } // END createClientBehaviorContextNPETest

  public void createClientBehaviorContextParamaterTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    String expectedName = "one";
    String expectedVal = "alpha";

    ClientBehaviorContext.Parameter paramOne = new ClientBehaviorContext.Parameter(
        expectedName, expectedVal);

    String resultName = paramOne.getName();
    String resultVal = (String) paramOne.getValue();

    if (!expectedName.equals(resultName)) {
      out.append(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unexpected value for Name!" + JSFTestUtil.NL + "Expected: "
          + expectedName + JSFTestUtil.NL + "Received: " + resultName);
      return;
    }

    if (!expectedVal.equals(resultVal)) {
      out.append(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unexpected value for Value!" + JSFTestUtil.NL + "Expected: "
          + expectedVal + JSFTestUtil.NL + "Received: " + resultVal);
      return;
    }

    out.append(JSFTestUtil.PASS);

  } // END createClientBehaviorContextParamaterTest
}
