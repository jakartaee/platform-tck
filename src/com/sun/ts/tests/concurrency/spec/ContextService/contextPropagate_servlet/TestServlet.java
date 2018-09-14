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

package com.sun.ts.tests.concurrency.spec.ContextService.contextPropagate_servlet;

import com.sun.ts.lib.util.BASE64Decoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    BASE64Decoder decoder = new BASE64Decoder();
    byte[] proxyAsBytes = decoder.decodeBuffer(req.getParameter("proxy"));
    ObjectInputStream in = new ObjectInputStream(
        new ByteArrayInputStream(proxyAsBytes));
    try {
      Object proxy = in.readObject();
      resp.getWriter().write(((TestWorkInterface) proxy).doSomeWork());
    } catch (Exception e) {
      throw new ServletException(e);
    }
  }
}
