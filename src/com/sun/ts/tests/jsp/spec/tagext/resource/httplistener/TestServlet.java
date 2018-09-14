/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsp.spec.tagext.resource.httplistener;

import com.sun.ts.tests.servlet.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.servlet.common.util.ServletTestUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class TestServlet extends HttpTCKServlet {

  public void testResourceSL(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    boolean passed = true;

    String[] expected = { "SessionListener sessionCreated", "passed DataSource",
        "passed QueueConnectionFactory", "passed TopicConnectionFactory",
        "passed TopicConnectionFactory", "passed ConnectionFactory",
        "passed Queue", "passed Topic", "passed Session", "passed URL" };

    HttpSession session = request.getSession(true);

    ServletConfig config = getServletConfig();
    ServletContext context = config.getServletContext();
    Object o = context.getAttribute("CTSTestSessionListener");
    if (o != null) {
      if (o instanceof String) {
        String actual = (String) o;
        int i = 0;
        while (i < 10) {
          if (!actual.contains(expected[i])) {
            passed = false;
            pw.println("missing expected=" + expected[i]);
          }
          i++;
        }
        pw.println("actual=" + actual);
      } else {
        ServletTestUtil.printResult(pw, false);
        pw.println("Object returned was not and instance of String");
      }
    } else {
      passed = false;
      pw.println("SessionListener attribute not found");
    }
    ServletTestUtil.printResult(pw, passed);
  }

  public void testResourceSAL(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    boolean passed = true;

    String[] expected = { "SessionAttributeListener attributeAdded",
        "passed DataSource", "passed QueueConnectionFactory",
        "passed TopicConnectionFactory", "passed TopicConnectionFactory",
        "passed ConnectionFactory", "passed Queue", "passed Topic",
        "passed Session", "passed URL" };

    HttpSession session = request.getSession(true);
    session.setAttribute("attributeReplacedTest", "Attribute1");

    ServletConfig config = getServletConfig();
    ServletContext context = config.getServletContext();

    Object o = context.getAttribute("CTSTestSessionAttributeListener");
    if (o != null) {
      if (o instanceof String) {
        String actual = (String) o;
        int i = 0;
        while (i < 10) {
          if (!actual.contains(expected[i])) {
            passed = false;
            pw.println("missing expected=" + expected[i]);
          }
          i++;
        }
        pw.println("actual=" + actual);
      } else {
        passed = false;
        pw.println("Object returned was not and instance of String");
      }
    } else {
      passed = false;
      pw.println("CTSTestSessionAttributeListener attribute not found");
    }
    ServletTestUtil.printResult(pw, passed);
  }
}
