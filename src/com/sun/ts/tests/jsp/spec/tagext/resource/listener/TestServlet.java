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
 * @(#)TestServlet.java	1.10 05/10/24
 */

package com.sun.ts.tests.jsp.spec.tagext.resource.listener;

import com.sun.ts.tests.servlet.common.servlets.GenericTCKServlet;
import com.sun.ts.tests.servlet.common.util.ServletTestUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class TestServlet extends GenericTCKServlet {

  public void testResourceCL(ServletRequest request, ServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    boolean passed = true;

    String[] expected = { "ContextListener contextInitialized",
        "passed DataSource", "passed QueueConnectionFactory",
        "passed TopicConnectionFactory", "passed TopicConnectionFactory",
        "passed ConnectionFactory", "passed Queue", "passed Topic",
        "passed Session", "passed URL" };

    ServletConfig config = getServletConfig();
    ServletContext context = config.getServletContext();
    Object o = context.getAttribute("ContextListener");
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
      pw.println("ContextListener attribute not found");
    }
    ServletTestUtil.printResult(pw, passed);
  }

  public void testResourceCAL(ServletRequest request, ServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    boolean passed = true;

    String[] expected = { "ContextAttributeListener attributeAdded",
        "passed DataSource", "passed QueueConnectionFactory",
        "passed TopicConnectionFactory", "passed TopicConnectionFactory",
        "passed ConnectionFactory", "passed Queue", "passed Topic",
        "passed Session", "passed URL" };

    ServletConfig config = getServletConfig();
    ServletContext context = config.getServletContext();

    context.setAttribute("CTSTestContextAttributesListener", "CTS Test");
    context.setAttribute("CTSTest", "CTS Test");
    Object o = context.getAttribute("CTSTestContextAttributeListener");
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
      pw.println("CTSTestContextAttributeListener attribute not found");
    }
    ServletTestUtil.printResult(pw, passed);
  }

  public void testResourceRL(ServletRequest request, ServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    boolean passed = true;

    String[] expected = { "RequestListener requestInitialized",
        "passed DataSource", "passed QueueConnectionFactory",
        "passed TopicConnectionFactory", "passed TopicConnectionFactory",
        "passed ConnectionFactory", "passed Queue", "passed Topic",
        "passed Session", "passed URL" };

    ServletConfig config = getServletConfig();
    ServletContext context = config.getServletContext();

    Object o = context.getAttribute("CTSTestRequestListener");
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
      pw.println("CTSTestRequestListener attribute not found");
      passed = false;
    }
    ServletTestUtil.printResult(pw, passed);
  }

  public void testResourceRAL(ServletRequest request, ServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    boolean passed = true;

    String[] expected = { "RequestAttributeListener attributeAdded",
        "passed DataSource", "passed QueueConnectionFactory",
        "passed TopicConnectionFactory", "passed TopicConnectionFactory",
        "passed ConnectionFactory", "passed Queue", "passed Topic",
        "passed Session", "passed URL" };

    request.setAttribute("CTSTEST", "CTSTEST");

    ServletConfig config = getServletConfig();
    ServletContext context = config.getServletContext();

    Object o = context.getAttribute("CTSTestRequestAttributeListener");
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
      pw.println("CTSTestRequestAttributeListener attribute not found");
    }
    ServletTestUtil.printResult(pw, passed);
  }
}
