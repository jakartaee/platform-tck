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
package com.sun.ts.tests.jsf.api.javax_faces.factoryfinder.metainflast;

import com.sun.ts.tests.jsf.common.servlets.FactoryTCKServlet;
import javax.faces.FactoryFinder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import javax.faces.context.FacesContextFactory;

public class TestServlet extends FactoryTCKServlet {

  public void getFactoryMetainflastTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    String factory = "com.sun.ts.tests.jsf.common.factories."
        + "TCKContextFactoryTwo";
    try {

      FacesContextFactory fcf = (FacesContextFactory) FactoryFinder
          .getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);

      String factName = fcf.getWrapped().getClass().getCanonicalName();

      if (factName.contains("TCKContextFactoryTwo")) {
        pw.println("Test PASSED");
      } else {
        pw.println("Wrong FacesContextFactory Being Used");
        pw.println("Found: " + FactoryFinder
            .getFactory(FactoryFinder.FACES_CONTEXT_FACTORY).toString());
        pw.println("Expected: " + factory);
        pw.println("Test FAILED.");
      }

    } catch (Exception e) {
      pw.println("Test FAILED.");
      pw.println(e.toString());
    }
  }
}
