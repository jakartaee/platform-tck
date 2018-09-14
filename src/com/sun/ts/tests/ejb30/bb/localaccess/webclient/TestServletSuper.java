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
package com.sun.ts.tests.ejb30.bb.localaccess.webclient;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import com.sun.ts.tests.ejb30.bb.localaccess.common.Constants;
import com.sun.ts.tests.ejb30.bb.localaccess.common.TestBeanIF;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import com.sun.ts.tests.servlet.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.servlet.common.util.Data;

public class TestServletSuper extends HttpTCKServlet {
  protected Object getInjectedFieldInSubclass() {
    return "";
  }

  protected int postConstructCallsCount;

  @Resource(name = "ut")
  private UserTransaction ut;

  @EJB
  private TestBeanIF testBean;

  @PostConstruct
  private void postConstruct() {
    TLogger.log("In PostConstruct method of " + this);
    postConstructCallsCount++;
  }

  @PreDestroy
  private void preDestroy() {
    TLogger.log("In PreDestroy method of " + this);
    postConstructCallsCount = 0;
  }

  public void injectedIntoTestServletSuperEvenNoAnnotationInTestServlet(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    if (ut != null) {
      pw.println(
          Data.PASSED + " field correctly injected into TestServletSuper "
              + "even when TestServlet itself contains no annotation: " + ut);
    } else {
      pw.println(Data.FAILED + " field not injected into TestServletSuper "
          + "when TestServlet itself contains no annotation");
      if (!"".equals(getInjectedFieldInSubclass())) {
        pw.println("The field injected into TestServlet: "
            + getInjectedFieldInSubclass());
      }
    }
  }

  public void postConstructCalledInTestServletSuperEvenNoAnnotationInTestServlet(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    if (postConstructCallsCount == 1) {
      pw.println(Data.PASSED + " PostConstruct in TestServletSuper called once "
          + "even when TestServlet itself contains no annotation: " + ut);
    } else {
      pw.println(
          Data.FAILED + " PostConstruct in TestServletSuper not called once "
              + "when TestServlet itself contains no annotation. "
              + "postConstructCallsCount is " + postConstructCallsCount);
    }
  }

  public void passByValueTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    String[] args = new String[] { Constants.CLIENT_MSG };
    String expected = args[0];
    try {
      testBean.passByValueTest(args);
    } catch (TestFailedException ex) {
      throw new ServletException(ex);
    }
    String actual = args[0];
    if (expected.equals(actual)) {
      pw.println(Data.PASSED
          + " Got the expected result: the array element modified in remote bean is not visible in servlet client.");
    } else {
      pw.println(Data.FAILED + " Expecting '" + expected + "', but actual '"
          + actual + "'");
    }

    StringBuffer sb = new StringBuffer("param StringBuffer");
    StringBuffer returnedSb = testBean.passByValueTest3(sb);
    if (sb == returnedSb) {
      pw.println(Data.FAILED + " Expecting not equal, but actual equal: " + sb);
    } else {
      pw.println(Data.PASSED + " Got expected param != ret");
    }
  }
}
