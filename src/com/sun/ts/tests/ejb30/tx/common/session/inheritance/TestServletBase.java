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
package com.sun.ts.tests.ejb30.tx.common.session.inheritance;

import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import com.sun.ts.tests.servlet.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.servlet.common.util.Data;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJBs;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@EJBs({
    @EJB(name = "abeanRemote", beanName = "ABean", beanInterface = TxRemoteIF.class, description = "remote ABean"),
    @EJB(name = "bbeanRemote", beanName = "BBean", beanInterface = TxRemoteIF.class, description = "remote BBean"),
    @EJB(name = "cbeanRemote", beanName = "CBean", beanInterface = TxRemoteIF.class, description = "remote CBean"),
    @EJB(name = "dbeanRemote", beanName = "DBean", beanInterface = TxRemoteIF.class, description = "remote DBean"),
    @EJB(name = "ebeanRemote", beanName = "EBean", beanInterface = TxRemoteIF.class, description = "remote EBean"),
    @EJB(name = "fbeanRemote", beanName = "FBean", beanInterface = TxRemoteIF.class, description = ""),
    @EJB(name = "abeanLocal", beanName = "ABean", beanInterface = TxLocalIF.class, description = "local ABean"),
    @EJB(name = "bbeanLocal", beanName = "BBean", beanInterface = TxLocalIF.class, description = "local BBean"),
    @EJB(name = "cbeanLocal", beanName = "CBean", beanInterface = TxLocalIF.class, description = "local CBean"),
    @EJB(name = "dbeanLocal", beanName = "DBean", beanInterface = TxLocalIF.class, description = "local DBean"),
    @EJB(name = "ebeanLocal", beanName = "EBean", beanInterface = TxLocalIF.class, description = "local EBean"),
    @EJB(name = "fbeanLocal", beanName = "FBean", beanInterface = TxLocalIF.class, description = "") })
abstract public class TestServletBase extends HttpTCKServlet {
  public void aBeanRemote(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    StringBuilder reason = new StringBuilder();
    try {
      TestLogic.aBeanRemote(reason);
      pw.println(Data.PASSED + reason.toString());
    } catch (TestFailedException e) {
      pw.println(Data.FAILED + e.getMessage());
    }
  }

  public void aBeanLocal(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    StringBuilder reason = new StringBuilder();
    try {
      TestLogic.aBeanLocal(reason);
      pw.println(Data.PASSED + reason.toString());
    } catch (TestFailedException e) {
      pw.println(Data.FAILED + e.getMessage());
    }
  }

  public void bBeanRemote(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    StringBuilder reason = new StringBuilder();
    try {
      TestLogic.bBeanRemote(reason);
      pw.println(Data.PASSED + reason.toString());
    } catch (TestFailedException e) {
      pw.println(Data.FAILED + e.getMessage());
    }
  }

  public void bBeanLocal(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    StringBuilder reason = new StringBuilder();
    try {
      TestLogic.bBeanLocal(reason);
      pw.println(Data.PASSED + reason.toString());
    } catch (TestFailedException e) {
      pw.println(Data.FAILED + e.getMessage());
    }
  }

  public void cBeanRemote(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    StringBuilder reason = new StringBuilder();
    try {
      TestLogic.cBeanRemote(reason);
      pw.println(Data.PASSED + reason.toString());
    } catch (TestFailedException e) {
      pw.println(Data.FAILED + e.getMessage());
    }
  }

  public void cBeanLocal(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    StringBuilder reason = new StringBuilder();
    try {
      TestLogic.cBeanLocal(reason);
      pw.println(Data.PASSED + reason.toString());
    } catch (TestFailedException e) {
      pw.println(Data.FAILED + e.getMessage());
    }
  }

  public void dBeanRemote(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    StringBuilder reason = new StringBuilder();
    try {
      TestLogic.dBeanRemote(reason);
      pw.println(Data.PASSED + reason.toString());
    } catch (TestFailedException e) {
      pw.println(Data.FAILED + e.getMessage());
    }
  }

  public void dBeanLocal(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    StringBuilder reason = new StringBuilder();
    try {
      TestLogic.dBeanLocal(reason);
      pw.println(Data.PASSED + reason.toString());
    } catch (TestFailedException e) {
      pw.println(Data.FAILED + e.getMessage());
    }
  }

  public void eBeanRemote(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    StringBuilder reason = new StringBuilder();
    try {
      TestLogic.eBeanRemote(reason);
      pw.println(Data.PASSED + reason.toString());
    } catch (TestFailedException e) {
      pw.println(Data.FAILED + e.getMessage());
    }
  }

  public void eBeanLocal(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    StringBuilder reason = new StringBuilder();
    try {
      TestLogic.eBeanLocal(reason);
      pw.println(Data.PASSED + reason.toString());
    } catch (TestFailedException e) {
      pw.println(Data.FAILED + e.getMessage());
    }
  }

  public void overloadedMethodsTxLocal(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    StringBuilder reason = new StringBuilder();
    try {
      TestLogic.overloadedMethodsTxLocal(reason);
      pw.println(Data.PASSED + reason.toString());
    } catch (TestFailedException e) {
      pw.println(Data.FAILED + e.getMessage());
    }
  }

  public void overloadedMethodsTxRemote(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    StringBuilder reason = new StringBuilder();
    try {
      TestLogic.overloadedMethodsTxRemote(reason);
      pw.println(Data.PASSED + reason.toString());
    } catch (TestFailedException e) {
      pw.println(Data.FAILED + e.getMessage());
    }
  }
}
