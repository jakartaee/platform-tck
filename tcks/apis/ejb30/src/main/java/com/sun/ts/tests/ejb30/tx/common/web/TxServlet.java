/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.tx.common.web;

import static com.sun.ts.tests.ejb30.tx.common.web.Constants.interServletTxPropagation;
import static com.sun.ts.tests.ejb30.tx.common.web.Constants.interServletTxPropagation2;
import static com.sun.ts.tests.ejb30.tx.common.web.Constants.servletLocalCmt;
import static com.sun.ts.tests.ejb30.tx.common.web.Constants.servletLocalCmtMandatory;
import static com.sun.ts.tests.ejb30.tx.common.web.Constants.servletLocalCmtNever;
import static com.sun.ts.tests.ejb30.tx.common.web.Constants.servletLocalCmtRequiresNew;
import static com.sun.ts.tests.ejb30.tx.common.web.Constants.servletRemoteCmt;
import static com.sun.ts.tests.ejb30.tx.common.web.Constants.servletRemoteCmtMandatory;
import static com.sun.ts.tests.ejb30.tx.common.web.Constants.servletRemoteCmtNever;
import static com.sun.ts.tests.ejb30.tx.common.web.Constants.servletRemoteCmtRequiresNew;
import static com.sun.ts.tests.ejb30.tx.common.web.Constants.testname;

import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.servlet.common.util.Data;

import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Status;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;

public class TxServlet extends HttpServlet {
  @Resource(name = "ut")
  private UserTransaction ut;

  @EJB(name = "localBean")
  private LocalIF localBean;

  @EJB(name = "remoteBean")
  private RemoteIF remoteBean;

  public TxServlet() {
  }

  public void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    doGet(req, resp);
  }

  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // start a UserTransaction, and invoke a bean
    String testName = req.getParameter(testname);
    if (testName == null) {
      throw new ServletException(
          "request parameter " + testname + " is not set.");
    }
    if (interServletTxPropagation.equals(testName)) {
      interServletTxPropagation(req, resp);
      return;
    } else if (interServletTxPropagation2.equals(testName)) {
      interServletTxPropagation2(req, resp);
      return;
    } else if (servletRemoteCmtRequiresNew.equals(testName)) {
      remoteBean.requiresNew();
      return;
    } else if (servletLocalCmtRequiresNew.equals(testName)) {
      localBean.requiresNew();
      return;
    } else if (servletRemoteCmtNever.equals(testName)) {
      servletRemoteCmtNever(req, resp);
      return;
    } else if (servletLocalCmtNever.equals(testName)) {
      servletLocalCmtNever(req, resp);
      return;
    }
    try {
      ut.begin();
      if (servletRemoteCmt.equals(testName)) {
        remoteBean.required();
      } else if (servletLocalCmt.equals(testName)) {
        localBean.required();
      } else if (servletRemoteCmtMandatory.equals(testName)) {
        remoteBean.mandatory();
      } else if (servletLocalCmtMandatory.equals(testName)) {
        localBean.mandatory();
      }
    } catch (jakarta.transaction.NotSupportedException e) {
      throw new ServletException(e);
    } catch (jakarta.transaction.SystemException e) {
      throw new ServletException(e);
    }
  }

  private void servletRemoteCmtNever(HttpServletRequest req,
      HttpServletResponse resp) throws ServletException, IOException {
    PrintWriter pw = resp.getWriter();
    try {
      remoteBean.never();
      pw.println(Data.FAILED + ", didn't get expected EJBException");
    } catch (EJBException e) {
      pw.println(Data.PASSED + ", got expected exception");
    }
  }

  private void servletLocalCmtNever(HttpServletRequest req,
      HttpServletResponse resp) throws ServletException, IOException {
    PrintWriter pw = resp.getWriter();
    try {
      localBean.never();
      pw.println(Data.FAILED + ", didn't get expected EJBException");
    } catch (EJBException e) {
      pw.println(Data.PASSED + ", got expected exception");
    }
  }

  private void interServletTxPropagation(HttpServletRequest req,
      HttpServletResponse resp) throws ServletException, IOException {
    // tx should be propagated from the calling servlet TestServlet
    PrintWriter pw = resp.getWriter();
    try {
      int status = ut.getStatus();
      if (status == Status.STATUS_ACTIVE) {
        pw.println(Data.PASSED + ", tx is active in the called servlet");
      } else {
        pw.println(Data.FAILED + ", tx is not active in the called servlet. "
            + "tx status is " + status);
      }

    } catch (SystemException e) {
      throw new ServletException(e);
    }
  }

  private void interServletTxPropagation2(HttpServletRequest req,
      HttpServletResponse resp) throws ServletException, IOException {
    interServletTxPropagation(req, resp);
    try {
      ut.rollback();
    } catch (SystemException e) {
      throw new ServletException(e);
    }
  }

}
