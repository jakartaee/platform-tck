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
package com.sun.ts.tests.ejb30.tx.session.stateless.cm.varargs;

import com.sun.ts.lib.deliverable.cts.resource.Dog;
import com.sun.ts.tests.servlet.common.servlets.HttpTCKServlet;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sun.ts.tests.servlet.common.util.Data;

public class TestServlet extends HttpTCKServlet {

  @EJB
  private VarargsLocalIF varargsLocalBean;

  @EJB
  private VarargsRemoteIF varargsRemoteBean;

  public void formatLocal(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    format0(varargsLocalBean, request, response);
  }

  public void formatRemote(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    format0(varargsRemoteBean, request, response);
  }

  public void addLocal(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    add0(varargsLocalBean, request, response);
  }

  public void addRemote(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    add0(varargsRemoteBean, request, response);
  }

  public void listDogsLocal(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    listDogs0(varargsLocalBean, request, response);
  }

  public void listDogsRemote(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    listDogs0(varargsRemoteBean, request, response);
  }

  private void format0(VarargsCommonIF varargsBean, HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    String s = "format";
    String a = "a", b = "b", c = "c";
    String expected = s + a + b + c;
    String result = varargsBean.format(s, a, b, c);
    verify(expected, result, pw, false);
    result = varargsBean.format(s, new Object[] { a, b, c });
    verify(expected, result, pw, true);
  }

  private void listDogs0(VarargsCommonIF varargsBean,
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    Dog a = Dog.getInstance();
    String expected = a.toString() + a.toString();
    String result = varargsBean.listDogs(a, a);
    verify(expected, result, pw, false);
    result = varargsBean.listDogs(new Dog[] { a, a });
    verify(expected, result, pw, true);
  }

  private void add0(VarargsCommonIF varargsBean, HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    int a = 1, b = 2, c = 3;
    int expected = a + b + c;
    int result = varargsBean.add(a, b, c);
    verify(expected, result, pw, false);
    result = varargsBean.add(new int[] { a, b, c });
    verify(expected, result, pw, true);
  }

  // this method may be called multiple times for a specific test. We
  // only want to print the pass status once at the end, to avoid false
  // positive in case of results like PASS...FAIL...FAIL.
  // printPass controls whether to print PASS or not.
  // We always want to print FAIL status if the verification fails.
  private boolean verify(Object expected, Object result, PrintWriter pw,
      boolean printPass) throws IOException {
    if (expected.equals(result)) {
      if (printPass) {
        pw.println(Data.PASSED);
      }
      pw.println(" Got expected result " + expected);
      return true;
    }
    pw.println(
        Data.FAILED + " Expecting " + expected + ", but actual " + result);
    return false;
  }
}
