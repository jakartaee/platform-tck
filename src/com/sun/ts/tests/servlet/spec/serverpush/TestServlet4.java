/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.servlet.spec.serverpush;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.PushBuilder;
import java.io.IOException;
import java.io.PrintWriter;

public class TestServlet4 extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    PrintWriter pw = resp.getWriter();
    Cookie cookie1 = new Cookie("foo", "bar");
    cookie1.setMaxAge(1000);
    resp.addCookie(cookie1);

    Cookie cookie2 = new Cookie("baz", "qux");
    cookie2.setMaxAge(-1);
    resp.addCookie(cookie2);
    pw.println("add cookies [foo,bar] [baz,qux] to response");

    HttpSession session = req.getSession(true);
    pw.println("create session: " + session);

    PushBuilder pb = req.newPushBuilder();
    pw.println("Cookie header in PushBuilder: " + pb.getHeader("Cookie"));
    pb.path("index.html");
    pb.push();
  }
}
