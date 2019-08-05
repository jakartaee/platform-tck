/*
 * Copyright (c) 2017, 2019 Oracle and/or its affiliates and others.
 * All rights reserved.
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
import javax.servlet.http.PushBuilder;
import java.io.IOException;
import java.io.PrintWriter;

public class TestServlet2 extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    PrintWriter pw = resp.getWriter();
    req.getSession(true);
    PushBuilder pb = req.newPushBuilder();
    String defaultMethod = pb.getMethod();
    pw.println("Method:" + defaultMethod);
    PushBuilder pb2 = req.newPushBuilder();
    pw.println("Return new instance:" + String.valueOf(pb != pb2));
    pw.println("JSESSIONID: " + pb.getSessionId());
    pw.println("The headers of PushBuilder: ");
    for (String name : pb.getHeaderNames()) {
      pw.print(name);
      pw.print("=");
      pw.println(pb.getHeader(name));
    }
  }
}
