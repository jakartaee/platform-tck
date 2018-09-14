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

package com.sun.ts.tests.servlet.spec.servletresponse;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class TestServlet extends GenericServlet {

  public void service(ServletRequest request, ServletResponse response)
      throws ServletException, IOException {

    response.setContentType("text/plain");
    PrintWriter pw = response.getWriter();

    pw.write("flushBufferTest for compatibility\n\r");
    System.out.println("flushBufferTest for compatibility");
    response.flushBuffer();

    try {
      Thread.sleep(10000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // write the second part of the document and flush by exit method
    pw.write("Test Failed\n\r");
  }

  public void doGet(ServletRequest request, ServletResponse response)
      throws ServletException, IOException {
    service(request, response);
  }

  public void doPost(ServletRequest request, ServletResponse response)
      throws ServletException, IOException {
    service(request, response);
  }
}
