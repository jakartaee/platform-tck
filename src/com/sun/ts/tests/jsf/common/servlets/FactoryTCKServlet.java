/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsf.common.servlets;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FactoryTCKServlet extends HttpServlet {

  /**
   * A basic implementation of the <code>doGet</code> method which will call
   * invokeTest.
   *
   * @param req
   *          - <code>HttpServletRequest</code>
   * @param res
   *          - <code>HttpServletResponse</code>
   * @exception ServletException
   *              if an error occurs
   * @exception IOException
   *              if an IO error occurs
   */
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    invokeTest(req, res);
  }

  /**
   * A basic implementation of the <code>doPost</code> method which will call
   * invokeTest.
   *
   * @param req
   *          - <code>HttpServletRequest</code>
   * @param res
   *          - <code>HttpServletResponse</code>
   * @exception ServletException
   *              if an error occurs
   * @exception IOException
   *              if an IO error occurs
   */
  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {

    invokeTest(req, res);

  }

  // ---------------------------------------------------------- private methods
  /**
   * <code>invokeTest</code> uses reflection to invoke test methods in child
   * classes of this particular class.
   *
   * @param req
   *          - <code>HttpServletRequest</code>
   * @param res
   *          - <code>HttpServletResponse</code>
   * @exception ServletException
   *              if an error occurs
   */
  @SuppressWarnings("static-access")
  protected void invokeTest(HttpServletRequest req, HttpServletResponse res)
      throws ServletException {
    res.setContentType("text/plain");
    char[] temp = req.getParameter("testname").toCharArray();
    temp[0] = Character.toLowerCase(temp[0]);
    String test = new String(temp);

    Method[] methods = this.getClass().getMethods();
    // (test, TEST_ARGS);
    Method method = null;
    for (int i = 0; i < methods.length; i++) {
      method = methods[i];
      if (method.getName().equals(test)) {
        break;
      }
    }
    if (method == null) {
      throw new ServletException("Test: " + test + " does not exist.");
    }
    try {
      method.invoke(this, new Object[] { req, res });
    } catch (InvocationTargetException ite) {
      throw new ServletException(ite.getTargetException());
    } catch (Exception e) {
      throw new ServletException("Error executing test: " + test, e);

    }
  }
}
