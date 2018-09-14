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
package com.sun.ts.tests.servlet.api.javax_servlet_http.sessioncookieconfig;

import com.sun.ts.tests.servlet.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.servlet.common.util.ServletTestUtil;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpSession;

public class TestServlet extends HttpTCKServlet {

  public void constructortest1(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    request.getSession(true);

    String results = getServletContext().getSessionCookieConfig().getComment();

    if (results.indexOf("-FAILED-") > -1) {
      ServletTestUtil.printResult(
          new PrintWriter("At least on test failed.  " + results), false);
    }

  }

  public void setNameTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    String name = "WHO_SHOULD_NOT_BE_NAMED_HERE";
    Boolean pass = true;
    PrintWriter pw = response.getWriter();
    HttpSession session = request.getSession();

    try {
      pw.println("calling method setName");
      getServletContext().getSessionCookieConfig().setName(name);
      pass = false;
      pw.println("Expected IllegalStateException not thrown");
    } catch (IllegalStateException ex) {
      pw.println("Expected IllegalStateException thrown");
    } finally {
      session.invalidate();
      ServletTestUtil.printResult(pw, pass);
    }
  }

  public void setCommentTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    String comment = "WHO_SHOULD_NOT_BE_NAMED_HERE";
    Boolean pass = true;
    PrintWriter pw = response.getWriter();
    HttpSession session = request.getSession();

    try {
      pw.println("calling method setComment");
      getServletContext().getSessionCookieConfig().setComment(comment);
      pass = false;
      pw.println("Expected IllegalStateException not thrown");
    } catch (IllegalStateException ex) {
      pw.println("Expected IllegalStateException thrown");
    } finally {
      session.invalidate();
      ServletTestUtil.printResult(pw, pass);
    }
  }

  public void setPathTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    String path = "WHO_SHOULD_NOT_BE_NAMED_HERE";
    Boolean pass = true;
    PrintWriter pw = response.getWriter();
    HttpSession session = request.getSession();

    try {
      pw.println("calling method setPath");
      getServletContext().getSessionCookieConfig().setPath(path);
      pass = false;
      pw.println("Expected IllegalStateException not thrown");
    } catch (IllegalStateException ex) {
      pw.println("Expected IllegalStateException thrown");
    } finally {
      session.invalidate();
      ServletTestUtil.printResult(pw, pass);
    }
  }

  public void setDomainTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    String domain = "WHO_SHOULD_NOT_BE_NAMED_HERE";
    Boolean pass = true;
    PrintWriter pw = response.getWriter();
    HttpSession session = request.getSession();

    try {
      pw.println("calling method setDomain");
      getServletContext().getSessionCookieConfig().setDomain(domain);
      pass = false;
      pw.println("Expected IllegalStateException not thrown");
    } catch (IllegalStateException ex) {
      pw.println("Expected IllegalStateException thrown");
    } finally {
      session.invalidate();
      ServletTestUtil.printResult(pw, pass);
    }
  }

  public void setMaxAgeTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    int maxage = 12345;
    Boolean pass = true;
    PrintWriter pw = response.getWriter();
    HttpSession session = request.getSession();

    try {
      pw.println("calling method setMaxAge");
      getServletContext().getSessionCookieConfig().setMaxAge(maxage);
      pass = false;
      pw.println("Expected IllegalStateException not thrown");
    } catch (IllegalStateException ex) {
      pw.println("Expected IllegalStateException thrown");
    } finally {
      session.invalidate();
      ServletTestUtil.printResult(pw, pass);
    }
  }

  public void setHttpOnlyTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    Boolean http = true;
    Boolean pass = true;
    PrintWriter pw = response.getWriter();
    HttpSession session = request.getSession();

    try {
      pw.println("calling method setHttpOnly");
      getServletContext().getSessionCookieConfig().setHttpOnly(http);
      pass = false;
      pw.println("Expected IllegalStateException not thrown");
    } catch (IllegalStateException ex) {
      pw.println("Expected IllegalStateException thrown");
    } finally {
      session.invalidate();
      ServletTestUtil.printResult(pw, pass);
    }
  }

  public void setSecureTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    Boolean secure = true;
    Boolean pass = true;
    PrintWriter pw = response.getWriter();
    HttpSession session = request.getSession();

    try {
      pw.println("calling method setSecure");
      getServletContext().getSessionCookieConfig().setSecure(secure);
      pass = false;
      pw.println("Expected IllegalStateException not thrown");
    } catch (IllegalStateException ex) {
      pw.println("Expected IllegalStateException thrown");
    } finally {
      session.invalidate();
      ServletTestUtil.printResult(pw, pass);
    }
  }
}
