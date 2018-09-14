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
package com.sun.ts.tests.jsf.api.javax_faces.view.location;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import javax.faces.view.Location;

public class TestServlet extends HttpTCKServlet {

  private static final int LINE = 1;

  private static final int COLUMN = 2;

  private static final String PATH = "testPath";

  /**
   * <code>init</code> initializes the servlet.
   *
   * @param config
   *          - <code>ServletConfig</code>
   */
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  /**
   * Constructor for Location.
   * 
   * @param request
   *          - <code>HttpServletRequest</code>
   * @param response
   *          - <code>HttpServletResponse</code>
   * @throws ServletException
   *           - if an unexpected container error occurs
   * @throws IOException
   *           - if an unexpected I/O error occurs
   */
  public void locationCtorTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      new Location(PATH, LINE, COLUMN);
      pw.println(JSFTestUtil.PASS);
    } catch (Exception e) {
      pw.println("The constructor for Location threw an unexpected exception");
      e.printStackTrace();
    }
  }// End locationCtorTest

  /**
   * No-arg getColumn() for Location.
   * 
   * @param request
   *          - <code>HttpServletRequest</code>
   * @param response
   *          - <code>HttpServletResponse</code>
   * @throws ServletException
   *           - if an unexpected container error occurs
   * @throws IOException
   *           - if an unexpected I/O error occurs
   */
  public void locationGetColumnTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      Location loc = new Location(PATH, LINE, COLUMN);
      int result = loc.getColumn();

      if (!(result == COLUMN)) {
        pw.println("Test FAILED" + JSFTestUtil.NL + "Expected: " + COLUMN
            + JSFTestUtil.NL + "Received: " + result);
        return;
      }

      pw.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      pw.println("The constructor for Location threw an unexpected exception.");
      e.printStackTrace();
    }
  }// End locationGetColumnTest

  /**
   * No-arg getLine() for Location.
   * 
   * @param request
   *          - <code>HttpServletRequest</code>
   * @param response
   *          - <code>HttpServletResponse</code>
   * @throws ServletException
   *           - if an unexpected container error occurs
   * @throws IOException
   *           - if an unexpected I/O error occurs
   */
  public void locationGetLineTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      Location loc = new Location(PATH, LINE, COLUMN);
      int result = loc.getLine();

      if (!(result == LINE)) {
        pw.println("Test FAILED" + JSFTestUtil.NL + "Expected: " + LINE
            + JSFTestUtil.NL + "Received: " + result);
        return;
      }

      pw.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      pw.println("The constructor for Location threw an unexpected exception.");
      e.printStackTrace();
    }
  }// End locationGetLineTest

  /**
   * No-arg getPath() for Location.
   * 
   * @param request
   *          - <code>HttpServletRequest</code>
   * @param response
   *          - <code>HttpServletResponse</code>
   * @throws ServletException
   *           - if an unexpected container error occurs
   * @throws IOException
   *           - if an unexpected I/O error occurs
   */
  public void locationGetPathTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      Location loc = new Location(PATH, LINE, COLUMN);
      String result = loc.getPath();

      if (!(result.equals(PATH))) {
        pw.println("Test FAILED" + JSFTestUtil.NL + "Expected: " + PATH
            + JSFTestUtil.NL + "Received: " + result);
        return;
      }

      pw.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      pw.println("The constructor for Location threw an unexpected exception.");
      e.printStackTrace();
    }
  }// End locationGetPathTest
}
