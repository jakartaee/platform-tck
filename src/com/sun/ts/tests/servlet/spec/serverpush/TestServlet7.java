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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.PushBuilder;
import java.io.IOException;
import java.io.PrintWriter;

public class TestServlet7 extends HttpServlet {

  private final static String[] METHODS = { "", "POST", "PUT", "DELETE",
      "CONNECT", "OPTIONS", "TRACE" };

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    PrintWriter pw = resp.getWriter();
    PushBuilder pb = req.newPushBuilder();
    boolean pass = true;
    try {
      pb.push();
      pw.println(
          "IllegalStateException should be thrown if there was no call to path(java.lang.String) on this instance between its instantiation.");
      pass = false;
    } catch (IllegalStateException e) {
    }

    pb = req.newPushBuilder();
    pb.path("index.html");
    pb.push();
    try {
      pb.push();
      pw.println(
          "IllegalStateException should be thrown if there was no call to path(java.lang.String) on this instance between the last call to push() that did not throw an IllegalStateException.");
      pass = false;
    } catch (IllegalStateException e) {
    }

    pb = req.newPushBuilder();
    try {
      pb.method(null);
      pw.println(
          "NullPointerException should be thrown if the argument of method() is null");
      pass = false;
    } catch (NullPointerException e) {
    }

    for (String method : METHODS) {
      if (!testMethod(pb, pw, method))
        pass = false;
    }

    if (pass)
      pw.println("test passed");
  }

  private boolean testMethod(PushBuilder pb, PrintWriter pw, String method) {
    try {
      pb.method(method);
      pw.println("IllegalArgumentException should be thrown if set method \""
          + method + "\" to be used for the push");
      return false;
    } catch (IllegalArgumentException e) {
    }
    return true;
  }
}
