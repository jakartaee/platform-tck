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

package com.sun.ts.tests.ejb30.common.covariant;

import com.sun.ts.tests.servlet.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.servlet.common.util.Data;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.ejb.EJBTransactionRequiredException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestServletBase extends HttpTCKServlet {
  @EJB(name = "remoteFuzzyBean")
  private FuzzyRemoteIF remoteFuzzyBean;

  @EJB(name = "localFuzzyBean")
  private FuzzyLocalIF localFuzzyBean;

  public void getMessage(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    boolean pass1 = false;
    boolean pass2 = false;
    StringBuilder reason = new StringBuilder();
    try {
      Object o = remoteFuzzyBean.getMessage();
      reason.append(
          "Failed to get expected EJBTransactionRequiredException when invoking .");
      reason.append(remoteFuzzyBean).append(" getMessage() method");
    } catch (EJBTransactionRequiredException e) {
      pass1 = true;
      reason.append(
          " Got expected EJBTransactionRequiredException when invoking .");
      reason.append(remoteFuzzyBean).append(" getMessage() method");
    }
    try {
      Object o = localFuzzyBean.getMessage();
      reason.append(
          "Failed to get expected EJBTransactionRequiredException when invoking .");
      reason.append(localFuzzyBean).append(" getMessage() method");
    } catch (EJBTransactionRequiredException e) {
      pass2 = true;
      reason.append(
          " Got expected EJBTransactionRequiredException when invoking .");
      reason.append(localFuzzyBean).append(" getMessage() method");
    }
    pw.println((pass1 && pass2) ? Data.PASSED : Data.FAILED);
    pw.println(reason.toString());
  }

  public void getMessages(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    boolean pass1 = false;
    boolean pass2 = false;
    StringBuilder reason = new StringBuilder();
    try {
      Object[] oo = remoteFuzzyBean.getMessages();
      reason.append(
          "Failed to get expected EJBTransactionRequiredException when invoking .");
      reason.append(remoteFuzzyBean).append(" getMessages() method");
    } catch (EJBTransactionRequiredException e) {
      pass1 = true;
      reason.append(
          " Got expected EJBTransactionRequiredException when invoking .");
      reason.append(remoteFuzzyBean).append(" getMessages() method");
    }
    try {
      Object o = localFuzzyBean.getMessages();
      reason.append(
          "Failed to get expected EJBTransactionRequiredException when invoking .");
      reason.append(localFuzzyBean).append(" getMessages() method");
    } catch (EJBTransactionRequiredException e) {
      pass2 = true;
      reason.append(
          " Got expected EJBTransactionRequiredException when invoking .");
      reason.append(localFuzzyBean).append(" getMessages() method");
    }
    pw.println((pass1 && pass2) ? Data.PASSED : Data.FAILED);
    pw.println(reason.toString());
  }

  public void getNumbers(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    boolean pass1 = false;
    boolean pass2 = false;
    StringBuilder reason = new StringBuilder();
    try {
      Object[] oo = remoteFuzzyBean.getNumbers();
      reason.append(
          "Failed to get expected EJBTransactionRequiredException when invoking .");
      reason.append(remoteFuzzyBean).append(" getNumbers() method");
    } catch (EJBTransactionRequiredException e) {
      pass1 = true;
      reason.append(
          " Got expected EJBTransactionRequiredException when invoking .");
      reason.append(remoteFuzzyBean).append(" getNumbers() method");
    }
    try {
      Object o = localFuzzyBean.getNumbers();
      reason.append(
          "Failed to get expected EJBTransactionRequiredException when invoking .");
      reason.append(localFuzzyBean).append(" getNumbers() method");
    } catch (EJBTransactionRequiredException e) {
      pass2 = true;
      reason.append(
          " Got expected EJBTransactionRequiredException when invoking .");
      reason.append(localFuzzyBean).append(" getNumbers() method");
    }
    pw.println((pass1 && pass2) ? Data.PASSED : Data.FAILED);
    pw.println(reason.toString());
  }
}
